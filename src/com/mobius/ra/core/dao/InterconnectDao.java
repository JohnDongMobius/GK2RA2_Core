package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallIds;
import com.mobius.ra.core.pojo.Interconnect;
import com.mobius.ra.core.pojo.Report;

public class InterconnectDao {

	public ArrayList<Interconnect> getCdrDetails(String period, String alias, String timezoneStr, String franchiseID, int reportType, Report report,
			boolean isInternational, boolean isTransit) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		ArrayList<Interconnect> domesticInterconnectRevenues = new ArrayList<Interconnect>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			if (isTransit) {
				sqlSb.append(report.getSqls().get("getCdrDetailTransit"));
			} else {
				if (isInternational) {
					sqlSb.append(report.getSqls().get("getCdrDetailListInternational"));
				} else {
					sqlSb.append(report.getSqls().get("getCdrDetailListNational"));
				}
			}
			sqlSb.append(" and  " + period + " order by in_trunk_name, out_trunk_name, s_msisdn ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				Interconnect interconnect = new Interconnect();
				interconnect.setAmountCount(0);
				interconnect.setCall_count(1);
				interconnect.setDeled(false);
				interconnect.setDurationCount(rs.getInt("duration"));
				interconnect.setFranchiseID(franchiseID);
				interconnect.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
				interconnect.setO_msisdn(rs.getString("o_msisdn"));
				interconnect.setReportType(reportType);
				interconnect.setS_msisdn(rs.getString("s_msisdn"));
				interconnect.setTrafficDate(rs.getString("call_time"));
				interconnect.setCallType(rs.getString("call_type"));
				interconnect.setIn_trunk_name(rs.getString("in_trunk_name"));
				interconnect.setOut_trunk_name(rs.getString("out_trunk_name"));
				interconnect.setIn_interconnect_operator(rs.getString("in_interconnect_operator"));
				interconnect.setOut_interconnect_operator(rs.getString("out_interconnect_operator"));
				interconnect.setIn_trunk_type(rs.getInt("in_trunk_type"));
				interconnect.setOut_trunk_type(rs.getInt("out_trunk_type"));
				interconnect.setIn_traffic_type(rs.getInt("in_traffic_type"));
				interconnect.setOut_traffic_type(rs.getInt("out_traffic_type"));
				interconnect.setIn_interconnect_rate(rs.getDouble("in_interconnect_rate"));
				interconnect.setOut_interconnect_rate(rs.getDouble("out_interconnect_rate"));
				domesticInterconnectRevenues.add(interconnect);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return domesticInterconnectRevenues;
	}

	public boolean ifDomIntRevReportGenerated(String trafficDate, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from ra_interconnect_revenue_report where traffic_date = ? ");
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

	public CallIds getCallIds(String period, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		CallIds callIds = new CallIds();
		try {
			StringBuilder sqlSb = new StringBuilder();
			String sqlString = " select min(call_id) as min_call_id, max(call_id) as max_call_id from cdr_detail where call_type in(1,2,27) and " + period;
			sqlSb.append(sqlString);
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				callIds.setMaxCallId(rs.getLong("max_call_id"));
				callIds.setMinCallId(rs.getLong("min_call_id"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return callIds;
	}

	public void insertDmcIntRve(ArrayList<Interconnect> interconnects, CallIds callIds, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(
					" insert into ra_interconnect_revenue_report (traffic_date, report_type, Franchise_ID,traffic_direction,interconnect_operator,traffic_type,trunk_name,rate,call_count,duration_count,amount_count,insert_time,trunk_scope,moc_call_count,moc_duration_count,mtc_call_count,mtc_duration_count,tst_call_count,tst_duration_count,min_call_id,max_call_id)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			baseDao.prepareStatement(sqlSb.toString());
			for (Interconnect interconnect : interconnects) {
				if (interconnect.getTraffic_type() == 3 || interconnect.getTraffic_type() == 2) {
					baseDao.setString(1, interconnect.getTrafficDate());
					baseDao.setInt(2, interconnect.getReportType());
					baseDao.setString(3, interconnect.getFranchiseID());
					baseDao.setString(4, interconnect.getTrafficDirection());
					baseDao.setString(5, interconnect.getInterconnectOperator());
					baseDao.setInt(6, interconnect.getTraffic_type());
					baseDao.setString(7, interconnect.getTrunk_name());
					baseDao.setDouble(8, interconnect.getRate());
					baseDao.setDouble(9, Tools.formatDouble(interconnect.getCall_count()));
					baseDao.setDouble(10, Tools.formatDouble(interconnect.getDurationCount()));
					baseDao.setDouble(11, Tools.formatDouble(interconnect.getAmountCount()));
					baseDao.setString(12, interconnect.getInsertTime());
					baseDao.setInt(13, interconnect.getTrunk_scope());
					baseDao.setLong(14, interconnect.getCallCountMOC());
					baseDao.setDouble(15, interconnect.getDurationMOC());
					baseDao.setLong(16, interconnect.getCallCountMTC());
					baseDao.setDouble(17, interconnect.getDurationMTC());
					baseDao.setLong(18, interconnect.getCallCountTST());
					baseDao.setDouble(19, interconnect.getDurationTST());
					baseDao.setLong(20, callIds.getMinCallId());
					baseDao.setLong(21, callIds.getMaxCallId());
					baseDao.addBatch();
				}
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
}
