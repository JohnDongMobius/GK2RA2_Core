package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.InboundRoamingSummary;
import com.mobius.ra.core.pojo.IndRoamingSmy4Maldives;
import com.mobius.ra.core.pojo.RaDetail;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date Jul 18, 2014
 * @version v 1.0
 */
public class IndRoamingSmyDao4Maldives {
	private final Logger logger = Logger.getLogger("RA-Billing");
	protected Report report;

	public IndRoamingSmyDao4Maldives(Report report) {
		this.report = report;
	}

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
	 * check if report has been generated.
	 * 
	 * @param date
	 * @param operator
	 * @param billingType
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfExist(String date, String operator, String reportType) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		int count = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("select count(*) as count from ra_roaming_summary where traffic_date=? and report_type=?");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date);
			baseDao.setString(2, reportType);

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
		return count > 0 ? true : false;
	}

	/**
	 * getTapcodeMap
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> getTapcodeMap(String operator) throws SQLException {
		long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		Map<String, String> tapCodeMap = new HashMap<String, String>();

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("select * from tap_code");

			baseDao.prepareStatement(sqlSb.toString());

			ResultSet rs = baseDao.executeQuery();

			long endTime = System.currentTimeMillis();
			this.logger.info("SQL : " + sqlSb.toString());
			this.logger.info("Cost Time: " + (endTime - startTime) + " ms");

			while (rs.next()) {
				tapCodeMap.put(rs.getString("imsi_prefix"),
						rs.getString("imsi_prefix") + ":" + rs.getString("tap_code") + ":" + rs.getString("country_name"));

			}

			this.logger.info("TapCode Map size: " + tapCodeMap.size());

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}

		return tapCodeMap;

	}

	/**
	 * insetCountTable
	 * 
	 * @param HashMap
	 *            <String, String>, operator
	 * @throws SQLException
	 */
	public void insertInRoamingSummary(List<IndRoamingSmy4Maldives> inRoamingList, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_roaming_summary (traffic_date, traffic_hour, report_type, tap_code, traffic_direction, imsi_prefix, call_count, duration_count, amount_count, rate, min_call_id, max_call_id, call_count_voice, call_count_sms,country_name,download_volume_count,upload_volume_count,call_count_data)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

			baseDao.prepareStatement(sqlSb.toString());

			for (IndRoamingSmy4Maldives inRoamingSummary : inRoamingList) {
				baseDao.setString(1, inRoamingSummary.getTrafficDate());
				baseDao.setString(2, inRoamingSummary.getHour());
				baseDao.setInt(3, inRoamingSummary.getReportType());
				baseDao.setString(4, inRoamingSummary.getTapcode());
				baseDao.setString(5, inRoamingSummary.getTrafficDirection());
				baseDao.setString(6, inRoamingSummary.getImsiPrefix());
				baseDao.setLong(7, inRoamingSummary.getCallCount());
				baseDao.setLong(8, inRoamingSummary.getDurationCount());
				baseDao.setLong(9, inRoamingSummary.getAmountCount());
				baseDao.setFloat(10, inRoamingSummary.getRate());
				baseDao.setLong(11, inRoamingSummary.getMinCallId());
				baseDao.setLong(12, inRoamingSummary.getMaxCallId());
				baseDao.setLong(13, inRoamingSummary.getCallCountVoice());
				baseDao.setLong(14, inRoamingSummary.getCallCountSms());
				baseDao.setString(15, inRoamingSummary.getCountryName());
				baseDao.setLong(16, inRoamingSummary.getDownloadVolumeCount());
				baseDao.setLong(17, inRoamingSummary.getUploadVolumeCount());
				baseDao.setLong(18, inRoamingSummary.getCallCountData());

				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			// baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

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

	public Map<String, Object> getMsc(String date, String operator, boolean isRealTime) throws SQLException {
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(date));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));
		return this.getMscListMap(startDate, endDate, operator, isRealTime);
	}

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
			baseDao.setString(3, startDate);
			baseDao.setString(4, endDate);
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
					hashMap.put(
							Tools.changePrefix(msc.getCallingNum()) + Tools.changePrefix(msc.getCalledNum()) + msc.getCallTime().substring(0, 19),
							msc);
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

	/**
	 * insetCountTable
	 * 
	 * @param HashMap
	 *            <String, String>, operator
	 * @throws SQLException
	 */
	public void updateInRoamingSummary(List<InboundRoamingSummary> inRoamingList, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" update ra_roaming_summary set call_count=?, duration_count=?, amount_count=?, rate=?, min_call_id=?, max_call_id=?, redo_times=1");
			sqlSb.append(" where traffic_date=? and hour=? and report_type=? and imsi_prefix=? and traffic_direction=?");

			baseDao.prepareStatement(sqlSb.toString());

			for (InboundRoamingSummary inRoamingSummary : inRoamingList) {

				baseDao.setLong(1, inRoamingSummary.getCallCount());
				baseDao.setLong(2, inRoamingSummary.getDurationCount());
				baseDao.setLong(3, inRoamingSummary.getAmountCount());
				baseDao.setFloat(4, inRoamingSummary.getRate());
				baseDao.setLong(5, inRoamingSummary.getMinCallId());
				baseDao.setLong(6, inRoamingSummary.getMaxCallId());

				baseDao.setString(7, inRoamingSummary.getTrafficDate());
				baseDao.setString(8, inRoamingSummary.getHour());
				baseDao.setInt(9, inRoamingSummary.getReportType());
				baseDao.setString(10, inRoamingSummary.getImsiPrefix());
				baseDao.setString(11, inRoamingSummary.getTrafficDirection());

				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			// baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
}
