package com.mobius.ra.core.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.dao.CdrDetailDao;
import com.mobius.ra.core.pojo.CdrDetail;
import com.mobius.ra.core.pojo.Operator;
import com.mobius.ra.core.pojo.RaTrunk;
import com.mobius.ra.core.pojo.Report;

public class CdrDetailSvc {
	private final Logger LOG = Logger.getLogger("");
	private CdrDetailDao cdrDetailDao = new CdrDetailDao();
	private String timeZoneString = Constants.coreCfg.getLocalTimezone();
	private String fraudOverallDB = Constants.coreCfg.getOperatorCode();
	private HashMap<String, String> trunkMap;
	private HashMap<String, RaTrunk> raTrunkMap;
	// private HashMap<String, String> fCompareHashMap;
	// private HashMap<String, String> otfCompareHashMap;
	private List<String> trunkNames;
	private Report report;

	public void generateCdrDetails(Operator operator, String date) throws SQLException {
		if (cdrDetailDao.beenGenerated(operator.getFraudAlias(), date)) {
			LOG.info(date + " cdr detail data has been generated");
		} else {
			generateCdr(operator, date);
		}
	}

	public CdrDetailSvc(Operator operator, Report report) throws SQLException {
		this.report = report;
		raTrunkMap = cdrDetailDao.getRaTrunks(Constants.DB_NAME_FRAUD + fraudOverallDB);
		trunkMap = cdrDetailDao.getTrunks(operator.getCoreAlias());
		trunkNames = cdrDetailDao.getTrunkNames(Constants.DB_NAME_FRAUD + fraudOverallDB);
	}

	private List<CdrDetail> generateCdr(Operator operator, String date) throws SQLException {
		if (cdrDetailDao.checkIfDataProcessedByPP(date, operator.code, true, report)) {
			List<CdrDetail> cdrDetails = cdrDetailDao.getCalls(operator.getCoreAlias(), date, timeZoneString, report);
			LOG.info(date + " Get " + cdrDetails.size() + " records from calls_nodup");
			// fCompareHashMap =
			// cdrDetailDao.getCompareHashMap(operator.getCoreAlias(), date,
			// timeZoneString, "29");
			// otfCompareHashMap =
			// cdrDetailDao.getCompareHashMap(operator.getCoreAlias(), date,
			// timeZoneString, "1,2,29");
			for (CdrDetail cdrDetail : cdrDetails) {
				cdrDetail = populateCdrDetail(operator, cdrDetail);
			}
			cdrDetailDao.batchInsertCdrDetail(cdrDetails, operator.getFraudAlias(), timeZoneString);
			LOG.info(date + " Insert " + cdrDetails.size() + " records into cdr_detail");
			return cdrDetails;
		} else {
			return null;
		}
	}

	private CdrDetail populateCdrDetail(Operator operator, CdrDetail cdrDetail) throws SQLException {
		cdrDetail = getInterconnectInfo(operator, cdrDetail);
		return cdrDetail;
	}

	private String changeTrunkName(String trunkName) {
		for (String raTrunkName : trunkNames) {
			if (Constants.coreCfg.getOperatorCode().equals("472_2")) {
				if (trunkName.equals(raTrunkName)) {
					return raTrunkName;
				}
			} else if (Constants.coreCfg.getOperatorCode().equals("425_6")) {
				String[] raTrunkNames = raTrunkName.split("%");
				if (trunkName.startsWith(raTrunkNames[0]) && trunkName.endsWith(raTrunkNames[1])) {
					return raTrunkName;
				}
			}
		}
		return "Unknown";
	}

	private boolean ifStartsWithPrefix(String num, String prefix) {
		String[] prefixArray = prefix.split(",");
		for (int i = 0; i < prefixArray.length; i++) {
			if (num.startsWith(prefixArray[i])) {
				return true;
			}
		}
		return false;
	}

	private CdrDetail getInterconnectInfo(Operator operator, CdrDetail cdrDetail) throws SQLException {
		// String duplicated_call_id = getDupCallID(cdrDetail);
		// if (duplicated_call_id != null) {
		// cdrDetail.setDuplicated(1);
		// cdrDetail.setDuplicated_call_id(duplicated_call_id);
		// return cdrDetail;
		// } else {
		// cdrDetail.setDuplicated(0);
		cdrDetail.setS_type(getMsisdnType(cdrDetail.getS_msisdn()));
		cdrDetail.setO_type(getMsisdnType(cdrDetail.getO_msisdn()));
		cdrDetail = getRaTrunkInfo(operator, cdrDetail, true);
		cdrDetail = getRaTrunkInfo(operator, cdrDetail, false);

		if (operator.getCode().equals("472_2"))
			cdrDetail = setCallType(operator, cdrDetail); // set transit 27.

		return cdrDetail;
		// }
	}

	private CdrDetail setCallType(Operator operator, CdrDetail cdrDetail) {
		String trunkName, trunkNameOther, trunkDirection, trunkDirectionOther;
		trunkName = cdrDetail.getIn_trunk_name();
		trunkNameOther = cdrDetail.getOut_trunk_name();
		trunkDirection = "Incoming";
		trunkDirectionOther = "Outgoing";

		RaTrunk raTrunk = raTrunkMap.get(operator.getCode() + trunkName + trunkDirection);
		RaTrunk raTrunkOther = raTrunkMap.get(operator.getCode() + changeTrunkName(trunkNameOther) + trunkDirectionOther);

		// if the Trunk_in is not ooredoo and Trunk_out not ooredoo then Traffic
		// Direction is "Transit" and Carrier is "Trunk_in carrier name"
		if (raTrunk != null && raTrunkOther != null) {
			if (!"Internal".equals(raTrunk.getTrunk_scope()) && !"Internal".equals(raTrunkOther.getTrunk_scope())) {
				cdrDetail.setCall_type(27);// transit
			}
		}
		return cdrDetail;
	}

	private String getMsisdnType(String msisdn) {
		if (msisdn.length() > 6 && msisdn.substring(0, 6).equals("101795")) {
			return "D";
		} else if ((msisdn.length() > 6 && msisdn.length() < 15) && !(msisdn.substring(0, 3).equals("227"))) {
			return "I";
		} else if ((msisdn.length() > 8 && msisdn.substring(0, 3).equals("227"))) {
			return "L";
		} else if (msisdn.length() <= 8 && msisdn.length() > 2 && msisdn.substring(0, 3).equals("227")) {
			return "S";
		} else if (msisdn.equals("0")) {
			return "Z";
		} else {
			return "U";
		}
	}

	private CdrDetail getRaTrunkInfo(Operator operator, CdrDetail cdrDetail, boolean isTrunkIn) throws SQLException {
		long trunkIn = cdrDetail.getTrunkIn();
		cdrDetail.setIn_trunk_name(trunkMap.get(Long.toString(trunkIn)));
		long trunkOut = cdrDetail.getTrunkOut();
		cdrDetail.setOut_trunk_name(trunkMap.get(Long.toString(trunkOut)));
		String trunkName, trunkNameOther, trunkDirection, trunkDirectionOther;
		if (isTrunkIn) {
			trunkName = cdrDetail.getIn_trunk_name();
			trunkNameOther = cdrDetail.getOut_trunk_name();
			trunkDirection = "Incoming";
			trunkDirectionOther = "Outgoing";
		} else {
			trunkName = cdrDetail.getOut_trunk_name();
			trunkNameOther = cdrDetail.getIn_trunk_name();
			trunkDirection = "Outgoing";
			trunkDirectionOther = "Incoming";
		}
		RaTrunk raTrunk = null;
		if (Constants.coreCfg.getOperatorCode().equals("472_2")) {
			raTrunk = raTrunkMap.get(operator.getCode() + trunkName + trunkDirection);
		} else {
			raTrunk = raTrunkMap.get(operator.getCode() + changeTrunkName(trunkName) + trunkDirection);
		}
		@SuppressWarnings("unused")
		RaTrunk raTrunkOther = raTrunkMap.get(operator.getCode() + changeTrunkName(trunkNameOther) + trunkDirectionOther);
		if (raTrunk != null) {
			if (isTrunkIn) {
				cdrDetail.setIn_interconnect_operator(raTrunk.getInterconnect_operator());
				cdrDetail.setIn_trunk_type(getTrunkScopeID(raTrunk.getTrunk_scope()));
			} else {
				cdrDetail.setOut_interconnect_operator(raTrunk.getInterconnect_operator());
				cdrDetail.setOut_trunk_type(getTrunkScopeID(raTrunk.getTrunk_scope()));
			}
			if (Constants.coreCfg.getOperatorCode().equals("472_2")) {// maldives
				if (isTrunkIn) {
					// if (raTrunk.getTrunk_scope().equals("International")) {
					// cdrDetail.setIn_traffic_type(3);
					// cdrDetail.setIn_interconnect_rate(3);
					// } else if (raTrunk.getTrunk_scope().equals("National")) {
					// cdrDetail.setIn_traffic_type(2);
					// cdrDetail.setIn_interconnect_rate(1);
					// } else {
					// cdrDetail.setIn_traffic_type(8);
					// cdrDetail.setIn_interconnect_rate(18);
					// }
					cdrDetail.setIn_traffic_type(3);
				} else {
					// if (raTrunk.getTrunk_scope().equals("International")) {
					// cdrDetail.setOut_traffic_type(3);
					// cdrDetail.setOut_interconnect_rate(3);
					// }
					// if (raTrunk.getTrunk_scope().equals("National")) {
					// cdrDetail.setOut_traffic_type(2);
					// cdrDetail.setOut_interconnect_rate(1);
					// } else {
					// cdrDetail.setOut_traffic_type(9);
					// cdrDetail.setOut_interconnect_rate(19);
					// }
					cdrDetail.setOut_traffic_type(3);
				}
			} else {// palestin
				if (isTrunkIn) {
					if (inCallTypes(cdrDetail.getCall_type(), "1,2,27,28,29") && raTrunk.getTrunk_scope().equals("International") && !tobeIgnored(cdrDetail)) {
						cdrDetail.setIn_traffic_type(3);
						cdrDetail.setIn_interconnect_rate(3);
					} else if (inCallTypes(cdrDetail.getCall_type(), "1,2,27,28,29") && raTrunk.getTrunk_scope().equals("National")) {
						cdrDetail.setIn_traffic_type(2);
						cdrDetail.setIn_interconnect_rate(1);
					} else {
						cdrDetail.setIn_traffic_type(8);
						cdrDetail.setIn_interconnect_rate(18);
					}
				} else {
					if (inCallTypes(cdrDetail.getCall_type(), "1,2,27,28,29") && raTrunk.getTrunk_scope().equals("International")) {
						cdrDetail.setOut_traffic_type(3);
						cdrDetail.setOut_interconnect_rate(3);
					}
					if (inCallTypes(cdrDetail.getCall_type(), "1,2,27,28,29") && raTrunk.getTrunk_scope().equals("National")) {
						cdrDetail.setOut_traffic_type(2);
						cdrDetail.setOut_interconnect_rate(1);
					} else {
						cdrDetail.setOut_traffic_type(9);
						cdrDetail.setOut_interconnect_rate(19);
					}
				}
			}
		}
		return cdrDetail;
	}

	private boolean inCallTypes(int callType, String callTypes) {
		String[] typeArray = callTypes.split(",");
		for (int i = 0; i < typeArray.length; i++) {
			if (callType == Integer.parseInt(typeArray[i])) {
				return true;
			}
		}
		return false;
	}

	private int getTrunkScopeID(String trunkScopeName) {
		int trunkScopeID = 0;
		if (trunkScopeName.equals("Internal")) {
			trunkScopeID = 1;
		} else if (trunkScopeName.equals("Domestic")) {
			trunkScopeID = 2;
		} else if (trunkScopeName.equals("International")) {
			trunkScopeID = 3;
		}
		return trunkScopeID;
	}

	private boolean tobeIgnored(CdrDetail cdrDetail) throws SQLException {
		List<String> ignoreTrunks = cdrDetailDao.getIgnoreTrunks(Constants.DB_NAME_FRAUD + fraudOverallDB);
		for (String trunk : ignoreTrunks) {
			if (cdrDetail.getOut_trunk_name().equals(trunk)) {
				return true;
			}
		}
		return false;
	}

	// private String getDupCallID(CdrDetail cdrDetail) {
	// if (cdrDetail.getCall_type() == 1 || cdrDetail.getCall_type() == 2) {
	// String callID = fCompareHashMap.get(cdrDetail.getCall_time() +
	// cdrDetail.getS_msisdn()
	// + cdrDetail.getDuration());
	// return callID;
	// } else if (cdrDetail.getCall_type() == 28 || cdrDetail.getCall_type() ==
	// 27) {
	// String callID = otfCompareHashMap.get(cdrDetail.getCall_time() +
	// cdrDetail.getS_msisdn()
	// + cdrDetail.getDuration());
	// return callID;
	// } else {
	// return null;
	// }
	// }
}
