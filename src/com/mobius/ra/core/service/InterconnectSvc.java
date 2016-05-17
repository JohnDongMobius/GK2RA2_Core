package com.mobius.ra.core.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.dao.InterconnectDao;
import com.mobius.ra.core.pojo.CallIds;
import com.mobius.ra.core.pojo.Interconnect;
import com.mobius.ra.core.pojo.Operator;
import com.mobius.ra.core.pojo.Report;

public class InterconnectSvc implements ServiceInterface {
	private InterconnectDao interconnectDao = new InterconnectDao();
	private final Logger LOG = Logger.getLogger("");
	String timeZoneString;
	Operator operator;
	String reportDate;
	Report report;

	public void generate() throws SQLException, IOException, ClassNotFoundException {
		Calendar base = Calendar.getInstance();
		int calDays = report.getExecuteDaysDefault();
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, -calDays);
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		while (c.compareTo(base) > 0) {
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			this.reportDate = formatter.format(c.getTime());
			generateCore();
			c.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	public Interconnect setMocMTCTST(Interconnect interconnect, boolean isTrunkIn, boolean isInternational, boolean isTransit) throws SQLException {
		int trafficType;
		if (isInternational) {
			trafficType = 3;
		} else {
			trafficType = 2;
		}

		if (isTransit) {
			if (interconnect.getIn_traffic_type() != trafficType) {
				return null;
			} else {
				interconnect.setInterconnectOperator(interconnect.getIn_interconnect_operator());
				interconnect.setTrunk_scope(interconnect.getIn_trunk_type());
				interconnect.setTraffic_type(interconnect.getIn_traffic_type());
				interconnect.setRate(interconnect.getIn_interconnect_rate());
				interconnect.setTrunk_name(interconnect.getIn_trunk_name());
				interconnect.setTrafficDirection("Transit");
			}
		} else {
			if (isTrunkIn) {
				if (interconnect.getIn_traffic_type() != trafficType) {
					return null;
				} else {
					interconnect.setInterconnectOperator(interconnect.getIn_interconnect_operator());
					interconnect.setTrunk_scope(interconnect.getIn_trunk_type());
					interconnect.setTraffic_type(interconnect.getIn_traffic_type());
					interconnect.setRate(interconnect.getIn_interconnect_rate());
					interconnect.setTrunk_name(interconnect.getIn_trunk_name());
					interconnect.setTrafficDirection("Incoming");
				}
			} else {
				if (interconnect.getOut_traffic_type() != trafficType) {
					return null;
				} else {
					interconnect.setInterconnectOperator(interconnect.getOut_interconnect_operator());
					interconnect.setTrunk_scope(interconnect.getOut_trunk_type());
					interconnect.setTraffic_type(interconnect.getOut_traffic_type());
					interconnect.setRate(interconnect.getOut_interconnect_rate());
					interconnect.setTrunk_name(interconnect.getOut_trunk_name());
					interconnect.setTrafficDirection("Outgoing");
				}
			}
		}

		return interconnect;
	}

	public void generateByPeriod(String callTimeQuery, Operator operator, boolean isInternational, boolean isTransit)
			throws SQLException, IOException, ClassNotFoundException {
		ArrayList<Interconnect> interconnects;
		interconnects = interconnectDao.getCdrDetails(callTimeQuery, operator.getFraudAlias(), timeZoneString, operator.getName(), 304, report, isInternational,
				isTransit);
		LOG.info("select " + interconnects.size() + " cdr details" + isInternational);
		if (isTransit) {
			interconnects = groupCallTransit(interconnects, isInternational);
		} else {
			interconnects = groupCall(interconnects, isInternational);
		}
		for (Interconnect interconnect : interconnects) {
			interconnect.setAmountCount(interconnect.getDurationCount() * interconnect.getRate());
		}
		CallIds callIds = interconnectDao.getCallIds(callTimeQuery, operator.getFraudAlias());
		interconnectDao.insertDmcIntRve(interconnects, callIds, operator.getFraudAlias());
		// LOG.info("Domestic Interconnect Revenue {} {} Inserted {}
		// interconnect revenues",
		// super.operator.getName(),
		// super.reportDate, interconnects.size());
		LOG.info("Interconnect Revenue/ Inserted interconnect revenues " + reportDate + "/" + interconnects.size() + isInternational);
	}

	public void generateCore() throws SQLException, IOException, ClassNotFoundException {
		if (interconnectDao.ifDomIntRevReportGenerated(reportDate, operator.getFraudAlias())) {
			LOG.info("Interconnect Revenue/" + operator.getName() + "/" + reportDate + " " + " SKIP");
		} else {
			CdrDetailSvc cdrDetailGenerator = new CdrDetailSvc(operator, report);
			cdrDetailGenerator.generateCdrDetails(operator, reportDate);
			String trafficDateStart, trafficDateEnd, callTimeQuery;
			trafficDateStart = "'" + reportDate + " 00:00:00" + "'";
			trafficDateEnd = "'" + reportDate + " 23:59:59" + "'";
			callTimeQuery = " call_time >= " + trafficDateStart + " and call_time <= " + trafficDateEnd;
			if (this.operator.getCode().equals("425_6")) {
				generateByPeriod(callTimeQuery, operator, true, false);
				generateByPeriod(callTimeQuery, operator, false, false);
			} else {// maldives
				generateByPeriod(callTimeQuery, operator, true, false);
				generateByPeriod(callTimeQuery, operator, true, true);
			}

		}
	}

	public ArrayList<Interconnect> groupCall(ArrayList<Interconnect> interconnects, boolean isInternational) throws SQLException {
		ArrayList<Interconnect> interconnects2 = new ArrayList<Interconnect>();
		for (Interconnect interconnect : interconnects) {
			Interconnect interconnectIn = (Interconnect) interconnect.clone();
			interconnectIn = setMocMTCTST(interconnectIn, true, isInternational, false);
			if (interconnectIn != null) {
				if (interconnects2.size() == 0) {
					interconnects2.add(interconnectIn);
				} else {
					boolean merged = false;
					for (int i = 0; i < interconnects2.size(); i++) {
						if (interconnectIn.getTrafficDirection().equals(interconnects2.get(i).getTrafficDirection())
								&& interconnectIn.getTraffic_type() == interconnects2.get(i).getTraffic_type()
								&& interconnectIn.getTrunk_name().equals(interconnects2.get(i).getTrunk_name())) {
							Interconnect interconnectTmp = new Interconnect();
							interconnectTmp = interconnects2.get(i);
							interconnectTmp.setCall_count(interconnectIn.getCall_count() + interconnectTmp.getCall_count());
							interconnectTmp.setDurationCount(interconnectIn.getDurationCount() + interconnectTmp.getDurationCount());
							interconnects2.set(i, interconnectTmp);
							merged = true;
							break;
						}
					}
					if (merged == false) {
						interconnects2.add(interconnectIn);
					}
				}
			}

			Interconnect interconnectOut = (Interconnect) interconnect.clone();
			interconnectOut = setMocMTCTST(interconnectOut, false, isInternational, false);
			if (interconnectOut != null) {
				if (interconnects2.size() == 0) {
					interconnects2.add(interconnectOut);
				} else {
					boolean merged = false;
					for (int i = 0; i < interconnects2.size(); i++) {
						if (interconnectOut.getTrafficDirection().equals(interconnects2.get(i).getTrafficDirection())
								&& interconnectOut.getTraffic_type() == interconnects2.get(i).getTraffic_type()
								&& interconnectOut.getTrunk_name().equals(interconnects2.get(i).getTrunk_name())) {
							Interconnect interconnectTmp = new Interconnect();
							interconnectTmp = interconnects2.get(i);
							interconnectTmp.setCall_count(interconnectOut.getCall_count() + interconnectTmp.getCall_count());
							interconnectTmp.setDurationCount(interconnectOut.getDurationCount() + interconnectTmp.getDurationCount());
							interconnects2.set(i, interconnectTmp);
							merged = true;
							break;
						}
					}
					if (merged == false) {
						interconnects2.add(interconnectOut);
					}
				}
			}
		}
		return interconnects2;
	}

	public ArrayList<Interconnect> groupCallTransit(ArrayList<Interconnect> interconnects, boolean isInternational) throws SQLException {
		ArrayList<Interconnect> interconnects2 = new ArrayList<Interconnect>();
		for (Interconnect interconnect : interconnects) {
			Interconnect interconnectIn = (Interconnect) interconnect.clone();
			interconnectIn = setMocMTCTST(interconnectIn, true, isInternational, true);
			if (interconnectIn != null) {
				if (interconnects2.size() == 0) {
					interconnects2.add(interconnectIn);
				} else {
					boolean merged = false;
					for (int i = 0; i < interconnects2.size(); i++) {
						if (interconnectIn.getTrafficDirection().equals(interconnects2.get(i).getTrafficDirection())
								&& interconnectIn.getTraffic_type() == interconnects2.get(i).getTraffic_type()
								&& interconnectIn.getTrunk_name().equals(interconnects2.get(i).getTrunk_name())) {
							Interconnect interconnectTmp = new Interconnect();
							interconnectTmp = interconnects2.get(i);
							interconnectTmp.setCall_count(interconnectIn.getCall_count() + interconnectTmp.getCall_count());
							interconnectTmp.setDurationCount(interconnectIn.getDurationCount() + interconnectTmp.getDurationCount());
							interconnects2.set(i, interconnectTmp);
							merged = true;
							break;
						}
					}
					if (merged == false) {
						interconnects2.add(interconnectIn);
					}
				}
			}
		}
		return interconnects2;
	}

	@Override
	public boolean summary(String[] args, String operator, Report report) throws SQLException, ParseException {
		try {
			String operatorName = null;
			if (operator.equals("425_6")) {
				operatorName = "Palestine";
			} else if (operator.equals("472_2")) {
				operatorName = "Maldives";
			}
			this.report = report;
			this.operator = new Operator(operatorName, operator);
			timeZoneString = Constants.coreCfg.getLocalTimezone();
			generate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean summaryRedo(String[] args, String operator, Report report) throws SQLException, ParseException {
		try {
			String operatorName = null;
			if (operator.equals("425_6")) {
				operatorName = "Palestine";
			} else if (operator.equals("472_2")) {
				operatorName = "Maldives";
			}
			this.report = report;
			this.operator = new Operator(operatorName, operator);
			timeZoneString = Constants.coreCfg.getLocalTimezone();
			generate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
