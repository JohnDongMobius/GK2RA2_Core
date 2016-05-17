package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.RaDetail;
import com.mobius.ra.core.pojo.Report;

/**
 * In this Dao Class, there are several common Dao method for Reconciliation
 * Summary, Traffic Summary, Revenue Summary and other summary reports.
 * 
 * @author Daniel.liu
 * @date Nov 11, 2013
 * @version v 1.0
 */
public class CommonDao {
	private final Logger logger = Logger.getLogger("RA-Billing");
	protected Report report;

	CommonDao() {
	}

	public CommonDao(Report report) {
		this.report = report;
	}

	/**
	 * insert ra details via batch. This can be used for Reconciliation reports
	 * and some summary reports with a few of details, such as international,
	 * roaming and etc.
	 * 
	 * @param detailList
	 * @param operator
	 * @throws SQLException
	 */
	public void batchInsertRaDetailTable(List<RaDetail> detailList, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_detail (call_type, s_msisdn, o_msisdn, duration, call_time,sw_id,s_imsi,s_imei,s_ci,s_lac,trunk_in,trunk_out,term_cause,term_reason,ss_code,charge_indicator,detail_type,report_type,file_name,insert_time,volume_download,volume_upload,charge_identifier,sgsn_address,ggsn_address,pdp_address,short_num,group_num");
			sqlSb.append(") values (");
			sqlSb.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

			// @20141023, add this for inbound roaming report.
			if ("401".equals(this.report.getType()))
				sqlSb.replace(sqlSb.indexOf(") values ("), sqlSb.indexOf(") values (") + 10, ",tap_code) values (?,");

			baseDao.prepareStatement(sqlSb.toString());

			for (RaDetail detail : detailList) {
				baseDao.setString(1, detail.getCallType());
				baseDao.setLong(2, Tools.getLongValue(detail.getCallingNum()));
				baseDao.setLong(3, Tools.getLongValue(detail.getCalledNum()));
				baseDao.setLong(4, detail.getDuration());
				baseDao.setString(5, detail.getCallTime());
				baseDao.setLong(6, Tools.getLongValue(detail.getSwId()));
				baseDao.setLong(7, Tools.getLongValue(detail.getsImsi()));
				baseDao.setLong(8, Tools.getLongValue(detail.getsImei()));
				baseDao.setLong(9, Tools.getLongValue(detail.getsCi()));

				baseDao.setLong(10, Tools.getLongValue(detail.getsLac()));
				baseDao.setLong(11, Tools.getLongValue(detail.getTrunkIn()));
				baseDao.setLong(12, Tools.getLongValue(detail.getTrunkOut()));
				baseDao.setLong(13, Tools.getLongValue(detail.getTermCause()));
				baseDao.setLong(14, Tools.getLongValue(detail.getTermReason()));
				baseDao.setLong(15, Tools.getLongValue(detail.getSsCode()));
				baseDao.setLong(16, Tools.getLongValue(detail.getChargeIndicator()));
				baseDao.setLong(17, detail.getDetailType());
				baseDao.setLong(18, detail.getReportType());
				baseDao.setString(19, detail.getFileName());
				baseDao.setString(20, detail.getInsertTime());
				baseDao.setLong(21, Tools.getLongValue(detail.getVolumeDownload()));
				baseDao.setLong(22, Tools.getLongValue(detail.getVolumeUpload()));
				baseDao.setLong(23, Tools.getLongValue(detail.getChargeIdentifier()));
				baseDao.setString(24, detail.getSgsnAddress());
				baseDao.setString(25, detail.getGgsnAddress());
				baseDao.setString(26, detail.getPdpAddress());
				baseDao.setInt(27, detail.getShortNum());
				baseDao.setLong(28, detail.getGroupNum());

				// @20141023, add this for inbound roaming report.
				if ("401".equals(this.report.getType()))
					baseDao.setString(29, detail.getTapCode());

				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	/**
	 * checkIfMocDataProcessedByPP
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfDataProcessedByPP(String date, String operator, boolean isRealTime, Report report) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		int count = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			if (isRealTime) {
				sqlSb.append("select count(*) as count from postprocess_config_data where (config_parameter=? and config_value > ?) or (config_parameter=? and config_value > ?) ");
				baseDao.prepareStatement(sqlSb.toString());
				baseDao.setString(1, report.getMscPpFlg());
				baseDao.setString(2, date);
				baseDao.setString(3, report.getBillingPpFlg());
				baseDao.setString(4, date);
			} else {
				sqlSb.append("select count(*) as count from postprocess_config_data where (config_parameter=? and config_value > ?) or (config_parameter=? and config_value > ?) ");
				baseDao.prepareStatement(sqlSb.toString());
				baseDao.setString(1, report.getMscPpRedoFlg());
				baseDao.setString(2, date);
				baseDao.setString(3, report.getBillingPpRedoFlg());
				baseDao.setString(4, date);
			}

			ResultSet rs = baseDao.executeQuery();
			// long endTime = System.currentTimeMillis();
			// LogToolkit.info("SQL : " + sqlSb.toString());
			// LogToolkit.info("Cost Time: " + (endTime - startTime) + " ms");
			while (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return count > 1 ? true : false;
	}

	/**
	 * checkIfMSCDataProcessedByPP, used for MSC reports.
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfMSCDataProcessedByPP(String date, String operator, boolean isRealTime, Report report) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		int count = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			if (isRealTime) {
				sqlSb.append("select count(*) as count from postprocess_config_data where (config_parameter=? and config_value > ?)");
				baseDao.prepareStatement(sqlSb.toString());
				baseDao.setString(1, report.getMscPpFlg());
				baseDao.setString(2, date);
			} else {
				sqlSb.append("select count(*) as count from postprocess_config_data where (config_parameter=? and config_value > ?)");
				baseDao.prepareStatement(sqlSb.toString());
				baseDao.setString(1, report.getMscPpRedoFlg());
				baseDao.setString(2, date);
			}

			ResultSet rs = baseDao.executeQuery();
			// long endTime = System.currentTimeMillis();
			// LogToolkit.info("SQL : " + sqlSb.toString());
			// LogToolkit.info("Cost Time: " + (endTime - startTime) + " ms");
			while (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return count >= 1 ? true : false;
	}

	/**
	 * get billing data from ra_billing table.
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBilling(String date, String operator, boolean isRealTime) throws SQLException {
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(date));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));
		return this.getBillingListMap(startDate, endDate, operator, isRealTime);
	}

	/**
	 * getBillingList
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> getBillingListMap(String startDate, String endDate, String operator, boolean isRealTime) throws SQLException {
		long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		List<RaBilling> list = new ArrayList<RaBilling>();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, RaBilling> hashMap = new HashMap<String, RaBilling>();

		try {
			StringBuilder sqlSb = new StringBuilder();
			if (isRealTime) {
				if (this.report == null)
					System.out.println("this.report");
				if (this.report.getSqls() == null)
					System.out.println("this.report.getSqls() is null");
				sqlSb.append(this.report.getSqls().get(Constants.GET_BILLING_LIST));
			} else {
				sqlSb.append(this.report.getSqls().get(Constants.GET_BILLING_LIST_REDO));
			}

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, startDate);
			baseDao.setString(2, endDate);
			this.logger.info(startDate + ", " + endDate);

			ResultSet rs = baseDao.executeQuery();

			long endTime = System.currentTimeMillis();
			this.logger.info("SQL : " + sqlSb.toString());
			this.logger.info("Cost Time: " + (endTime - startTime) + " ms");

			while (rs.next()) {
				RaBilling raBill = new RaBilling();
				raBill.setCallTime(rs.getString("call_time"));
				raBill.setDuration(rs.getLong("duration"));
				raBill.setCallType(rs.getString("call_type"));
				raBill.setSwID(rs.getInt("sw_id"));
				raBill.setCallingNum(rs.getString("s_msisdn"));

				String s = "";
				if (rs.getString("o_msisdn") != null) {
					s = rs.getString("o_msisdn").replaceAll("#", "");
				}
				if (s.contains("*0")) {
					int len = s.length();
					raBill.setCalledNum(s.substring(0, len - 2));
				} else {
					raBill.setCalledNum(s);
				}

				raBill.setsImsi(rs.getLong("s_imsi"));
				raBill.setsImei(rs.getLong("s_imei"));
				raBill.setsCi(rs.getLong("s_ci"));
				raBill.setsLac(rs.getInt("s_lac"));
				raBill.setTrunkIn(rs.getInt("trunk_in"));
				raBill.setTrunkOut(rs.getInt("trunk_out"));
				raBill.setTermCause(rs.getInt("term_cause"));
				raBill.setTermReason(Tools.getLongValue(rs.getString("term_reason")));
				raBill.setSsCode(rs.getInt("ss_code"));
				raBill.setChargeIndicator(rs.getInt("charge_indicator"));

				// String fileName = rs.getString("file_name");
				// if(!Tools.checkNullorSpace(fileName))
				// raBill.setFileName(fileName.substring(0,
				// fileName.length()-1));

				raBill.setFileName(rs.getString("file_name"));
				raBill.setInsertTime(rs.getString("insert_time"));

				raBill.setVolumeDownload(Tools.getLongValue(rs.getString("volume_download")));
				raBill.setVolumeUpload(Tools.getLongValue(rs.getString("volume_upload")));
				raBill.setChargeIdentifier(Tools.getLongValue(rs.getString("charge_identifier")));
				raBill.setSgsnAddress(rs.getString("sgsn_address"));
				raBill.setGgsnAddress(rs.getString("ggsn_address"));
				raBill.setPdpAddress(rs.getString("pdp_address"));
				raBill.setCallAmount(rs.getInt("call_amount"));
				list.add(raBill);
				if (operator.equals("472_2")) {
					hashMap.put(Tools.changePrefix(raBill.getCallingNum()) + raBill.getCallTime().substring(0, 19), raBill);
				} else {
					hashMap.put(Tools.changePrefix(raBill.getCallingNum()) + Tools.changePrefix(raBill.getCalledNum())
							+ raBill.getCallTime().substring(0, 19), raBill);
				}
			}

			this.logger.info("The records size of msisdn in Billing is: " + list.size());

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}

		map.put("list", list);
		map.put("hashMap", hashMap);
		return map;
	}

	/**
	 * get boundary billing data from ra_billing table.
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBoundaryBilling(String date, String operator, boolean isRealTime) throws SQLException, ParseException {
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(date));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d1 = sdf.parse(startDate);
		java.util.Calendar cal1 = java.util.Calendar.getInstance();
		cal1.setTime(d1);
		cal1.add(java.util.Calendar.SECOND, -Constants.coreCfg.getCtDeviation());

		java.util.Date d2 = sdf.parse(endDate);
		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal2.setTime(d2);
		cal2.add(java.util.Calendar.SECOND, Constants.coreCfg.getCtDeviation());

		Map<String, Object> map1 = this.getBillingListMap(sdf.format(cal1.getTime()), startDate, operator, isRealTime);
		Map<String, Object> map2 = this.getBillingListMap(endDate, sdf.format(cal2.getTime()), operator, isRealTime);
		Map<String, RaBilling> raMap1 = (HashMap<String, RaBilling>) map1.get("hashMap");
		Map<String, RaBilling> raMap2 = (HashMap<String, RaBilling>) map2.get("hashMap");
		List<RaBilling> raList1 = (ArrayList<RaBilling>) map1.get("list");
		List<RaBilling> raList2 = (ArrayList<RaBilling>) map2.get("list");
		this.logger.info("Boundary billing data: " + (raList1 != null ? raList1.size() : 0) + ", " + (raList2 != null ? raList2.size() : 0));

		raMap1.putAll(raMap2);
		raList1.addAll(raList2);
		return map1;
	}

	/**
	 * get boundary MSC data from calls_nodup or calls_nodup_redo table.
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBoundaryMsc(String date, String operator, boolean isRealTime) throws SQLException, ParseException {
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(date));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d1 = sdf.parse(startDate);
		java.util.Calendar cal1 = java.util.Calendar.getInstance();
		cal1.setTime(d1);
		cal1.add(java.util.Calendar.SECOND, -Constants.coreCfg.getCtDeviation());

		java.util.Date d2 = sdf.parse(endDate);
		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal2.setTime(d2);
		cal2.add(java.util.Calendar.SECOND, Constants.coreCfg.getCtDeviation());

		Map<String, Object> map1 = this.getMscListMap(sdf.format(cal1.getTime()), startDate, operator, isRealTime);
		Map<String, Object> map2 = this.getMscListMap(endDate, sdf.format(cal2.getTime()), operator, isRealTime);
		Map<String, RaBilling> raMap1 = (HashMap<String, RaBilling>) map1.get("hashMap");
		Map<String, RaBilling> raMap2 = (HashMap<String, RaBilling>) map2.get("hashMap");
		List<RaBilling> raList1 = (ArrayList<RaBilling>) map1.get("list");
		List<RaBilling> raList2 = (ArrayList<RaBilling>) map2.get("list");
		this.logger.info("Boundary MSC data: " + (raList1 != null ? raList1.size() : 0) + ", " + (raList2 != null ? raList2.size() : 0));

		raMap1.putAll(raMap2);
		raList1.addAll(raList2);
		return map1;
	}

	/**
	 * get MSC data for realtime or redo.
	 * 
	 * @param date
	 * @param operator
	 * @param isRealTime
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getMsc(String date, String operator, boolean isRealTime) throws SQLException {
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(date));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));
		return this.getMscListMap(startDate, endDate, operator, isRealTime);
	}

	/**
	 * getMocList
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getMscListMap(String startDate, String endDate, String operator, boolean isRealTime) throws SQLException {
		long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		List<CallsNoDup> list = new ArrayList<CallsNoDup>();
		Map<String, String> numMap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, CallsNoDup> hashMap = new HashMap<String, CallsNoDup>();

		try {
			StringBuilder sqlSb = new StringBuilder();
			if (isRealTime) {
				sqlSb.append(this.report.getSqls().get(Constants.GET_MSC_LIST));
			} else {
				sqlSb.append(this.report.getSqls().get(Constants.GET_MSC_LIST_REDO));
			}

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, startDate);
			baseDao.setString(2, endDate);
			this.logger.info(startDate + ", " + endDate);

			ResultSet rs = baseDao.executeQuery();

			long endTime = System.currentTimeMillis();
			this.logger.info("SQL : " + sqlSb.toString());
			this.logger.info("Cost Time: " + (endTime - startTime) + " ms");

			if (this.report.getSubscriberType() == 1) {
				// get prepaid list.
				numMap = this.getNumList("1", operator, endDate.substring(0, 10));
			}

			while (rs.next()) {
				if (this.report.getSubscriberType() != 1 || (numMap != null && numMap.containsKey(rs.getString("s_msisdn")))) {
					CallsNoDup msc = this.insertObj(rs);
					list.add(msc);
					if (operator.equals("472_2")) {
						hashMap.put(Tools.changePrefix(msc.getCallingNum()) + msc.getCallTime().substring(0, 19), msc);
					} else {
						hashMap.put(
								Tools.changePrefix(msc.getCallingNum()) + Tools.changePrefix(msc.getCalledNum()) + msc.getCallTime().substring(0, 19),
								msc);
					}
				}
			}
			this.logger.info("The records size of msisdn in MSC is: " + list.size());

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}

		map.put("list", list);
		map.put("hashMap", hashMap);
		return map;
	}

	/**
	 * getNumList
	 * 
	 * @param type
	 *            : 1-prepaid, 2-postpaid, 3-service/short num
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> getNumList(String type, String operator, String date) throws SQLException {
		Map<String, String> map;
		String date2;
		for (int i = 0; i < Constants.LOOP_TIMES_FOR_PAIDLIST; i++) {
			date2 = Tools.addDays(date, i);
			map = this.getNumListForOneDay(type, operator, date2);
			if (map.size() > 0) {
				return map;
			}
			date2 = Tools.addDays(date, -i);
			map = this.getNumListForOneDay(type, operator, date2);
			if (map.size() > 0) {
				return map;
			}
		}
		return null;
	}

	private Map<String, String> getNumListForOneDay(String type, String operator, String date) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select * from msisdn_paid_info b where type = ? and p_date = ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, type);
			baseDao.setString(2, date.replace("-", ""));

			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("msisdn"), rs.getString("msisdn"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return map;
	}

	// set the record set to CallsNoDup object
	private CallsNoDup insertObj(ResultSet rs) throws SQLException {
		CallsNoDup moc = new CallsNoDup();
		moc.setCallId(rs.getLong("call_id"));
		moc.setCallingNum(rs.getString("s_msisdn"));
		moc.setCalledNum(rs.getString("o_msisdn"));
		moc.setCallTime(rs.getString("call_time"));
		moc.setDuration(Tools.getLongValue(rs.getString("duration")));
		moc.setCallType(rs.getString("call_type"));
		moc.setSwId(Tools.getLongValue(rs.getString("sw_id")));
		moc.setsImsi(Tools.getLongValue(rs.getString("s_imsi")));
		moc.setsImei(Tools.getLongValue(rs.getString("s_imei")));
		moc.setsCi(Tools.getLongValue(rs.getString("s_ci")));
		moc.setsLac(Tools.getLongValue(rs.getString("s_lac")));
		moc.setTrunkIn(Tools.getLongValue(rs.getString("trunk_in")));
		moc.setTrunkOut(Tools.getLongValue(rs.getString("trunk_out")));
		moc.setTermCause(Tools.getLongValue(rs.getString("term_cause")));
		moc.setTermReason(Tools.getLongValue(rs.getString("term_reason")));
		moc.setSsCode(Tools.getLongValue(rs.getString("ss_code")));
		moc.setChargeIndicator(Tools.getLongValue(rs.getString("charge_indicator")));
		moc.setVolumeDownload(Tools.getLongValue(rs.getString("volume_download")));
		moc.setVolumeUpload(Tools.getLongValue(rs.getString("volume_upload")));
		moc.setChargeIdentifier(Tools.getLongValue(rs.getString("charge_identifier")));
		moc.setSgsnAddress(rs.getString("sgsn_address"));
		moc.setGgsnAddress(rs.getString("ggsn_address"));
		moc.setPdpAddress(rs.getString("pdp_address"));
		moc.setCalledNum(rs.getString("o_msisdn"));
		moc.setCalledNum(rs.getString("o_msisdn"));
		moc.setFileName(rs.getString("file_name"));
		return moc;
	}

	/**
	 * updateStatus4Exporter
	 * 
	 * @param
	 * @throws SQLException
	 */
	public void updateStatus4Exporter(String date, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();

			sqlSb.append("update decode_info set is_finished=1 where decode_date=?");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date.replaceAll("-", ""));

			baseDao.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

}
