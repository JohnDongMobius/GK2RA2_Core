package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallType;
import com.mobius.ra.core.pojo.CdrDetail;
import com.mobius.ra.core.pojo.NumberingPlan;
import com.mobius.ra.core.pojo.RaTrunk;
import com.mobius.ra.core.pojo.RatePlan;
import com.mobius.ra.core.pojo.Report;

public class CdrDetailDao {
	public boolean beenGenerated(String alias, String date) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from cdr_detail where call_time >= ? and call_time <= ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date + " 00:00:00");
			baseDao.setString(2, date + " 23:59:59");
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

	public List<String> getIgnoreTrunks(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<String> ignoreTrunks = new ArrayList<String>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select trunk_value from ra_trunk_excluded where trunk_type=2 ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				ignoreTrunks.add(rs.getString("trunk_value"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return ignoreTrunks;
	}

	public List<String> getTrunkNames(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<String> trunkNames = new ArrayList<String>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			String sqlString = " select trunk_name from ra_trunk where  trunk_name <> '*'";
			sqlSb.append(sqlString);
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				trunkNames.add(rs.getString("trunk_name"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return trunkNames;
	}

	public HashMap<String, RaTrunk> getRaTrunks(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, RaTrunk> raTrunks = new HashMap<String, RaTrunk>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select * from ra_trunk where trunk_scope in('International','national') ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				RaTrunk raTrunk = new RaTrunk();
				raTrunk.setInterconnect_operator(rs.getString("interconnect_operator"));
				raTrunk.setSource_operator_id(rs.getString("source_operator_id"));
				raTrunk.setTraffic_direction(rs.getString("traffic_direction"));
				raTrunk.setTrunk_name(rs.getString("trunk_name"));
				raTrunk.setTrunk_scope(rs.getString("trunk_scope"));
				raTrunks.put(raTrunk.getSource_operator_id() + raTrunk.getTrunk_name() + raTrunk.getTraffic_direction(), raTrunk);
				// System.out.println(raTrunk.getSource_operator_id() +
				// raTrunk.getTrunk_name() + raTrunk.getTraffic_direction());
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return raTrunks;
	}

	public void batchInsertCdrDetail(List<CdrDetail> cdrDetails, String alias, String timeZoneString) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(
					" insert into cdr_detail (call_id,call_type,call_time,duration,s_msisdn,o_msisdn,s_imsi, s_imei,s_code,o_code,zone_id,time_period,retail_rate,product_id,sub_product_id,s_type,o_type,in_trunk_name,out_trunk_name,in_interconnect_operator,out_interconnect_operator,in_trunk_type,out_trunk_type,in_traffic_type,out_traffic_type,in_interconnect_rate,out_interconnect_rate,transit_id,forwarded_num,tap_code,duplicated,duplicated_call_id)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			baseDao.prepareStatement(sqlSb.toString());
			for (CdrDetail cdrDetail : cdrDetails) {
				baseDao.setLong(1, cdrDetail.getCall_id());
				baseDao.setInt(2, cdrDetail.getCall_type());
				baseDao.setString(3, cdrDetail.getCall_time());
				baseDao.setLong(4, cdrDetail.getDuration());
				baseDao.setLong(5, Tools.getLongValue(cdrDetail.getS_msisdn()));
				baseDao.setLong(6, Tools.getLongValue(cdrDetail.getO_msisdn()));
				baseDao.setLong(7, cdrDetail.getS_imsi());
				baseDao.setLong(8, cdrDetail.getS_imei());
				baseDao.setString(9, cdrDetail.getS_code());
				baseDao.setString(10, cdrDetail.getO_code());
				baseDao.setInt(11, cdrDetail.getZone_id());
				baseDao.setString(12, cdrDetail.getTime_period());
				baseDao.setDouble(13, cdrDetail.getRetail_rate());
				baseDao.setString(14, cdrDetail.getProduct_id());
				baseDao.setString(15, cdrDetail.getSub_product_id());
				baseDao.setString(16, cdrDetail.getS_type());
				baseDao.setString(17, cdrDetail.getO_type());
				baseDao.setString(18, cdrDetail.getIn_trunk_name());
				baseDao.setString(19, cdrDetail.getOut_trunk_name());
				baseDao.setString(20, cdrDetail.getIn_interconnect_operator());
				baseDao.setString(21, cdrDetail.getOut_interconnect_operator());
				baseDao.setInt(22, cdrDetail.getIn_trunk_type());
				baseDao.setInt(23, cdrDetail.getOut_trunk_type());
				baseDao.setInt(24, cdrDetail.getIn_traffic_type());
				baseDao.setInt(25, cdrDetail.getOut_traffic_type());
				baseDao.setDouble(26, cdrDetail.getIn_interconnect_rate());
				baseDao.setDouble(27, cdrDetail.getOut_interconnect_rate());
				baseDao.setLong(28, cdrDetail.getTransit_id());
				baseDao.setLong(29, cdrDetail.getForwarded_num());
				baseDao.setString(30, cdrDetail.getTap_code());
				baseDao.setInt(31, cdrDetail.getDuplicated());
				if (cdrDetail.getDuplicated_call_id() != null) {
					baseDao.setLong(32, Long.parseLong(cdrDetail.getDuplicated_call_id()));
				} else {
					baseDao.setLong(32, 0);
				}
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

	public HashMap<String, String> getTrunks(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, String> trunks = new HashMap<String, String>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select trunk_id, trunk_value from trunk ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				trunks.put(rs.getString("trunk_id"), rs.getString("trunk_value"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return trunks;
	}

	public List<CdrDetail> getCdrDetails(String alias, String date) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<CdrDetail> cdrDetails = new ArrayList<CdrDetail>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			String sqlString = " select * from cdr_detail where call_time >= ? and call_time <= ? ";
			sqlSb.append(sqlString);
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date + " 00:00:00");
			baseDao.setString(2, date + " 23:59:59");
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				CdrDetail cdrDetail = new CdrDetail();
				cdrDetail.setCall_id(rs.getLong("call_id"));
				cdrDetail.setCall_type(rs.getInt("call_type"));
				cdrDetail.setCall_time(rs.getString("call_time"));
				cdrDetail.setDuration(rs.getLong("duration"));
				cdrDetail.setS_msisdn(rs.getString("s_msisdn"));
				cdrDetail.setO_msisdn(rs.getString("o_msisdn"));
				cdrDetail.setS_imsi(rs.getLong("s_imsi"));
				cdrDetail.setS_imei(rs.getLong("s_imei"));
				cdrDetail.setS_code(rs.getString("s_code"));
				cdrDetail.setO_code(rs.getString("o_code"));
				cdrDetail.setZone_id(rs.getInt("zone_id"));
				cdrDetail.setTime_period(rs.getString("time_period"));
				cdrDetail.setRetail_rate(rs.getDouble("retail_rate"));
				cdrDetail.setProduct_id(rs.getString("product_id"));
				cdrDetail.setSub_product_id(rs.getString("sub_product_id"));
				cdrDetail.setS_type(rs.getString("s_type"));
				cdrDetail.setO_type(rs.getString("o_type"));
				cdrDetail.setIn_trunk_name(rs.getString("in_trunk_name"));
				cdrDetail.setOut_trunk_name(rs.getString("out_trunk_name"));
				cdrDetail.setIn_interconnect_operator(rs.getString("in_interconnect_operator"));
				cdrDetail.setOut_interconnect_operator(rs.getString("out_interconnect_operator"));
				cdrDetail.setIn_trunk_type(rs.getInt("in_trunk_type"));
				cdrDetail.setOut_trunk_type(rs.getInt("out_trunk_type"));
				cdrDetail.setIn_traffic_type(rs.getInt("in_traffic_type"));
				cdrDetail.setOut_traffic_type(rs.getInt("out_traffic_type"));
				cdrDetail.setIn_interconnect_rate(rs.getDouble("in_interconnect_rate"));
				cdrDetail.setOut_interconnect_rate(rs.getDouble("out_interconnect_rate"));
				cdrDetail.setTransit_id(rs.getLong("transit_id"));
				cdrDetail.setForwarded_num(rs.getLong("forwarded_num"));
				cdrDetail.setTap_code(rs.getString("tap_code"));
				cdrDetails.add(cdrDetail);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return cdrDetails;
	}

	public boolean checkIfDataProcessedByPP(String date, String operator, boolean isRealTime, Report report) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		int count = 0;
		date = date.replace("-", "") + "23";
		try {
			StringBuilder sqlSb = new StringBuilder();
			if (isRealTime) {
				sqlSb.append("select count(*) as count from postprocess_config_data where config_parameter=? and config_value > ? ");
				baseDao.prepareStatement(sqlSb.toString());
				baseDao.setString(1, report.getMscPpFlg());
				baseDao.setString(2, date);
			} else {
				sqlSb.append("select count(*) as count from postprocess_config_data where config_parameter=? and config_value > ? ");
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
		return count > 0 ? true : false;
	}

	public List<CdrDetail> getCalls(String alias, String date, String timezoneStr, Report report) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<CdrDetail> cdrDetails = new ArrayList<CdrDetail>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			// String sqlString =
			// " select * from calls_nodup where call_type in (1,2,27,28,29) and
			// call_time >= ? and call_time <= ? ";
			sqlSb.append(report.getSqls().get("getMscList"));
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getLocalCallTime(timezoneStr, date + " 00:00:00", "GMT"));
			baseDao.setString(2, Tools.getLocalCallTime(timezoneStr, date + " 23:59:59", "GMT"));
			System.out.println(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				CdrDetail cdrDetail = new CdrDetail();
				cdrDetail.setCall_id(rs.getLong("call_id"));
				cdrDetail.setCall_type(rs.getInt("call_type"));
				cdrDetail.setCall_time(Tools.getLocalCallTime("GMT", rs.getString("call_time"), timezoneStr));
				cdrDetail.setDuration(rs.getInt("duration"));
				cdrDetail.setO_msisdn(rs.getString("o_msisdn"));
				cdrDetail.setS_msisdn(rs.getString("s_msisdn"));
				cdrDetail.setS_imsi(rs.getLong("s_imsi"));
				cdrDetail.setS_imei(rs.getLong("s_imei"));
				cdrDetail.setTrunkIn(rs.getLong("trunk_in"));
				cdrDetail.setTrunkOut(rs.getLong("trunk_out"));
				// cdrDetail.setTransit_id(rs.getLong("transit_call_id"));
				// cdrDetail.setForwarded_num(rs.getLong("forwarded_num"));
				cdrDetails.add(cdrDetail);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return cdrDetails;
	}

	public HashMap<String, NumberingPlan> getNumberingPlans(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, NumberingPlan> numberingPlans = new HashMap<String, NumberingPlan>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select num_prefix, num_code from numbering_plan ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				NumberingPlan numberingPlan = new NumberingPlan();
				numberingPlan.setNumPrefix(rs.getString("num_prefix"));
				numberingPlan.setNumCode(rs.getString("num_code"));
				numberingPlans.put(numberingPlan.getNumPrefix(), numberingPlan);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return numberingPlans;
	}

	public HashMap<String, RatePlan> getRatePlansCallNonePeak(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, RatePlan> ratePlans = new HashMap<String, RatePlan>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(
					" select rate_plan_code,product_ID,s_num_code,o_num_code,rate_plan.rate as rate, zone.rate zone_rate,call_type,time_period,zone.id from rate_plan left join zone on rate_plan.zone_id=zone.id where type=1 and time_period = '' ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				RatePlan ratePlan = new RatePlan();
				ratePlan.setRatePlanCode(rs.getString("rate_plan_code"));
				ratePlan.setProductID(rs.getString("product_ID"));
				ratePlan.setsNumCode(rs.getString("s_num_code"));
				ratePlan.setoNumCode(rs.getString("o_num_code"));
				ratePlan.setZoneID(rs.getInt("zone.id"));
				if (rs.getInt("zone.id") > 0) {
					ratePlan.setRate(rs.getFloat("zone_rate"));
				} else {
					ratePlan.setRate(rs.getFloat("rate"));
				}
				ratePlan.setCallType(rs.getInt("call_type"));
				ratePlan.setTimePeriod(rs.getString("time_period"));
				ratePlans.put(ratePlan.getsNumCode() + ratePlan.getoNumCode(), ratePlan);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return ratePlans;
	}

	public HashMap<String, RatePlan> getRatePlansSMS(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, RatePlan> ratePlans = new HashMap<String, RatePlan>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(
					" select rate_plan_code,product_ID,s_num_code,o_num_code,rate_plan.rate as rate, zone.rate zone_rate,call_type,time_period,zone.id from rate_plan left join zone on rate_plan.zone_id=zone.id where type=2");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				RatePlan ratePlan = new RatePlan();
				ratePlan.setRatePlanCode(rs.getString("rate_plan_code"));
				ratePlan.setProductID(rs.getString("product_ID"));
				ratePlan.setsNumCode(rs.getString("s_num_code"));
				ratePlan.setoNumCode(rs.getString("o_num_code"));
				ratePlan.setZoneID(rs.getInt("zone.id"));
				if (rs.getInt("zone.id") > 0) {
					ratePlan.setRate(rs.getFloat("zone_rate"));
				} else {
					ratePlan.setRate(rs.getFloat("rate"));
				}
				ratePlan.setCallType(rs.getInt("call_type"));
				ratePlan.setTimePeriod(rs.getString("time_period"));
				ratePlans.put(ratePlan.getsNumCode() + ratePlan.getoNumCode(), ratePlan);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return ratePlans;
	}

	public ArrayList<CallType> getCallTypes(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		ArrayList<CallType> callTypes = new ArrayList<CallType>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select call_type_id, call_type_value from call_type ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				CallType callType = new CallType();
				callType.setCallTypeID(rs.getInt("call_type_id"));
				callType.setCallTypeValue(rs.getString("call_type_value"));
				callTypes.add(callType);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return callTypes;
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

	public HashMap<String, RatePlan> getRatePlansCallPeak(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, RatePlan> ratePlans = new HashMap<String, RatePlan>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(
					" select rate_plan_code,product_ID,s_num_code,o_num_code,rate_plan.rate as rate, zone.rate zone_rate,call_type,time_period,zone.id from rate_plan left join zone on rate_plan.zone_id=zone.id where type=1 and time_period != '' ");
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				RatePlan ratePlan = new RatePlan();
				ratePlan.setRatePlanCode(rs.getString("rate_plan_code"));
				ratePlan.setProductID(rs.getString("product_ID"));
				ratePlan.setsNumCode(rs.getString("s_num_code"));
				ratePlan.setoNumCode(rs.getString("o_num_code"));
				ratePlan.setZoneID(rs.getInt("zone.id"));
				if (rs.getInt("zone.id") > 0) {
					ratePlan.setRate(rs.getFloat("zone_rate"));
				} else {
					ratePlan.setRate(rs.getFloat("rate"));
				}
				ratePlan.setCallType(rs.getInt("call_type"));
				ratePlan.setTimePeriod(rs.getString("time_period"));
				ratePlans.put(ratePlan.getsNumCode() + ratePlan.getoNumCode() + ratePlan.getTimePeriod(), ratePlan);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return ratePlans;
	}

	public void deleteCdrDetail(String trafficDate, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("delete from cdr_detail where call_time >= ? and call_time <= ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate + " 00:00:00");
			baseDao.setString(2, trafficDate + " 23:59:59");
			baseDao.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	public HashMap<String, String> getCompareHashMap(String alias, String date, String timezoneStr, String callTypes) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, String> compareHashMap = new HashMap<String, String>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			String sqlString = " select * from calls_nodup where call_type in (" + callTypes + ") and call_time >= ? and call_time <= ? ";
			sqlSb.append(sqlString);
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getLocalCallTime(timezoneStr, date + " 00:00:00", "GMT"));
			baseDao.setString(2, Tools.getLocalCallTime(timezoneStr, date + " 23:59:59", "GMT"));
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				compareHashMap.put(Tools.getLocalCallTime("GMT", rs.getString("call_time"), timezoneStr) + rs.getString("s_msisdn") + rs.getInt("duration"),
						rs.getString("call_id"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return compareHashMap;
	}
}
