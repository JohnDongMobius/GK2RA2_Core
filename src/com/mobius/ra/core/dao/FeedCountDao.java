package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.DashboardSummary;
import com.mobius.ra.core.pojo.RaDetail;
import com.mobius.ra.core.pojo.RaTrafficSummary;

public class FeedCountDao {
	private Logger logger = Logger.getLogger("RA-Billing");

	public boolean checkCountCallTypeReportGenerated(String trafficDate, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from ra_traffic_summary where traffic_date = ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			ResultSet rs = baseDao.executeQuery();
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
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkFileFeedTypeExist(String trafficDate, String alias, int type, String respectiveX) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from dashboard_summary where traffic_date = ? and type = ? and respective_x =?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			baseDao.setInt(2, type);
			baseDao.setString(3, respectiveX);
			ResultSet rs = baseDao.executeQuery();
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
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkReportGenerated(String trafficDate, String alias, int type) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from dashboard_summary where traffic_date = ? and type = ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			baseDao.setInt(2, type);
			ResultSet rs = baseDao.executeQuery();
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
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<DashboardSummary> countCalls(String trafficDate, String alias, int type, String timezoneStr) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		ArrayList<DashboardSummary> dashboardSummaryList = new ArrayList<DashboardSummary>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select call_type, count(call_type) as count from calls_nodup_redo where call_time >= ? and call_time <= ? group by call_type");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getLocalCallTime(timezoneStr, trafficDate + " 00:00:00", "GMT"));
			baseDao.setString(2, Tools.getLocalCallTime(timezoneStr, trafficDate + " 23:59:59", "GMT"));
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				DashboardSummary dashboardSummary = new DashboardSummary();
				dashboardSummary.setTraffic_date(trafficDate);
				dashboardSummary.setType(type);
				dashboardSummary.setRepective_x(Integer.toString(rs.getInt("call_type")));
				dashboardSummary.setCount_y(rs.getInt("count"));
				dashboardSummary.setInsert_time(Tools.getCalFullStr(Calendar.getInstance()));
				dashboardSummaryList.add(dashboardSummary);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return dashboardSummaryList;
	}

	public int countDecodedFile(String trafficDate, String alias, String timezoneStr) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from call_count where (file_first_call_time >= ? and file_first_call_time <= ?) or (file_last_call_time >= ? and file_last_call_time <= ?)");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getLocalCallTime(timezoneStr, trafficDate + " 00:00:00", "GMT"));
			baseDao.setString(2, Tools.getLocalCallTime(timezoneStr, trafficDate + " 23:59:59", "GMT"));
			baseDao.setString(3, Tools.getLocalCallTime(timezoneStr, trafficDate + " 00:00:00", "GMT"));
			baseDao.setString(4, Tools.getLocalCallTime(timezoneStr, trafficDate + " 23:59:59", "GMT"));
			ResultSet rs = baseDao.executeQuery();
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
		return count;
	}

	public ArrayList<DashboardSummary> countFileFeedType(String trafficDate, String alias, int type, String timezoneStr) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		ArrayList<DashboardSummary> dashboardSummaryList = new ArrayList<DashboardSummary>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			if (type == Constants.REPORT_FEED_VOLUME_DAILY) {
				sqlSb.append(" select feed_type as fileFeedType, count(feed_type) as count from call_count where (file_first_call_time >= ? and file_first_call_time <= ?) or (file_last_call_time >= ? and file_last_call_time <= ?) group by feed_type");
			} else if (type == Constants.REPORT_FILE_TYPE_DAILY) {
				sqlSb.append(" select file_type as fileFeedType, count(file_type) as count from call_count where (file_first_call_time >= ? and file_first_call_time <= ?) or (file_last_call_time >= ? and file_last_call_time <= ?) group by file_type");
			}
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getLocalCallTime(timezoneStr, trafficDate + " 00:00:00", "GMT"));
			baseDao.setString(2, Tools.getLocalCallTime(timezoneStr, trafficDate + " 23:59:59", "GMT"));
			baseDao.setString(3, Tools.getLocalCallTime(timezoneStr, trafficDate + " 00:00:00", "GMT"));
			baseDao.setString(4, Tools.getLocalCallTime(timezoneStr, trafficDate + " 23:59:59", "GMT"));
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				DashboardSummary dashboardSummary = new DashboardSummary();
				dashboardSummary.setTraffic_date(trafficDate);
				dashboardSummary.setType(type);
				dashboardSummary.setRepective_x(Integer.toString(rs.getInt("fileFeedType")));
				dashboardSummary.setCount_y(rs.getInt("count"));
				dashboardSummary.setInsert_time(Tools.getCalFullStr(Calendar.getInstance()));
				dashboardSummaryList.add(dashboardSummary);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return dashboardSummaryList;
	}

	public HashMap<String, String> getNumberingPlanHashMap(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, String> numberingPlanHashMap = new HashMap<String, String>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select mcc,mnc, num_prefix from numbering_plan");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				numberingPlanHashMap.put(rs.getString("num_prefix"), rs.getString("mcc") + rs.getString("mnc"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return numberingPlanHashMap;
	}

	public List<RaDetail> getRaDetails(String sql, String alias, String timeZone) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<RaDetail> raDetails = new ArrayList<RaDetail>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(sql);
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				RaDetail raDetail = this.insertObj(rs, timeZone);
				raDetails.add(raDetail);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return raDetails;
	}

	public void insertDashboardSummary(ArrayList<DashboardSummary> dashboardSummaryList, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into dashboard_summary (traffic_date, type, repective_x,count_y,insert_time)");
			sqlSb.append(" values (?,?,?,?,?) ");

			baseDao.prepareStatement(sqlSb.toString());

			for (DashboardSummary dashboardSummary : dashboardSummaryList) {
				baseDao.setString(1, dashboardSummary.getTraffic_date());
				baseDao.setInt(2, dashboardSummary.getType());
				if (dashboardSummary.getType() == Constants.REPORT_FEED_VOLUME_DAILY || dashboardSummary.getType() == Constants.REPORT_FILE_TYPE_DAILY
						|| dashboardSummary.getType() == Constants.REPORT_CALL_VOLUME_DAILY) {
					baseDao.setString(3, lookup(Integer.parseInt(dashboardSummary.getRepective_x()), dashboardSummary.getType(), "fraud_614_0"));
				} else {
					baseDao.setString(3, dashboardSummary.getRepective_x());
				}
				baseDao.setLong(4, dashboardSummary.getCount_y());
				baseDao.setString(5, dashboardSummary.getInsert_time());
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

	private RaDetail insertObj(ResultSet rs, String timeZoneStr) throws SQLException {
		RaDetail raDetail = new RaDetail();
		raDetail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
		raDetail.setCallingNum(rs.getString("s_msisdn"));
		raDetail.setCalledNum(rs.getString("o_msisdn"));
		raDetail.setCallTime(Tools.getLocalCallTime("GMT", rs.getString("call_time"), timeZoneStr));
		raDetail.setDuration(Tools.getLongValue(rs.getString("duration")));
		raDetail.setCallType(rs.getString("call_type"));
		raDetail.setSwId(Tools.getLongValue(rs.getString("sw_id")));
		raDetail.setsImsi(Tools.getLongValue(rs.getString("s_imsi")));
		raDetail.setsImei(Tools.getLongValue(rs.getString("s_imei")));
		raDetail.setsCi(Tools.getLongValue(rs.getString("s_ci")));
		raDetail.setsLac(Tools.getLongValue(rs.getString("s_lac")));
		raDetail.setTrunkIn(Tools.getLongValue(rs.getString("trunk_in")));
		raDetail.setTrunkOut(Tools.getLongValue(rs.getString("trunk_out")));
		raDetail.setTermCause(Tools.getLongValue(rs.getString("term_cause")));
		raDetail.setTermReason(Tools.getLongValue(rs.getString("term_reason")));
		raDetail.setSsCode(Tools.getLongValue(rs.getString("ss_code")));
		raDetail.setChargeIndicator(Tools.getLongValue(rs.getString("charge_indicator")));
		// raDetail.setVolumeDownload(Tools.getLongValue(rs.getString("volume_download")));
		// raDetail.setVolumeUpload(Tools.getLongValue(rs.getString("volume_upload")));
		// raDetail.setChargeIdentifier(Tools.getLongValue(rs.getString("charge_identifier")));
		// raDetail.setSgsnAddress(rs.getString("sgsn_address"));
		// raDetail.setGgsnAddress(rs.getString("ggsn_address"));
		// raDetail.setPdpAddress(rs.getString("pdp_address"));
		raDetail.setCalledNum(rs.getString("o_msisdn"));
		raDetail.setCalledNum(rs.getString("o_msisdn"));
		// raDetail.setFileName(rs.getString("file_name"));
		return raDetail;
	}

	public void insertRaTrafficSummary(ArrayList<RaTrafficSummary> raTrafficSummaries, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_traffic_summary (traffic_date, call_type, record_count,duration_count, insert_time)");
			sqlSb.append(" values (?,?,?,?,?) ");

			baseDao.prepareStatement(sqlSb.toString());

			for (RaTrafficSummary raTrafficSummary : raTrafficSummaries) {
				baseDao.setString(1, raTrafficSummary.getTrafficDate());
				baseDao.setString(2, raTrafficSummary.getCallType());
				baseDao.setLong(3, raTrafficSummary.getRecordCount());
				baseDao.setLong(4, raTrafficSummary.getDuration());
				baseDao.setString(5, Tools.getCalFullStr(Calendar.getInstance()));
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

	public String lookup(int code, int type, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		String name = "";
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select name from lookup where code = ? and report_type = ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setInt(1, code);
			baseDao.setInt(2, type);
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return name;
	}
}