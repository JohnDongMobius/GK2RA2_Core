package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.pojo.InboundRoamingSummary;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date Jul 18, 2014
 * @version v 1.0
 */
public class InBoundRoamingSummaryDao extends CommonDao{
	private final Logger logger = Logger.getLogger("RA-Billing");

	public InBoundRoamingSummaryDao(Report report) {
		super.report = report;
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
	public Map<String, String> getTapcodeMap(String operator)
			throws SQLException {
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
				tapCodeMap.put(rs.getString("imsi_range"), rs.getString("imsi_prefix")+":"+rs.getString("tap_code"));
				
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
	public void insertInRoamingSummary(List<InboundRoamingSummary> inRoamingList, String operator)
			throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_roaming_summary (traffic_date, traffic_hour, report_type, tap_code, traffic_direction, imsi_prefix, call_count, duration_count, amount_count, rate, min_call_id, max_call_id, call_count_voice, call_count_sms)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

			baseDao.prepareStatement(sqlSb.toString());

			for (InboundRoamingSummary inRoamingSummary : inRoamingList) {
				baseDao.setString(1, inRoamingSummary.getTrafficDate());
				baseDao.setString(2, inRoamingSummary.getHour());
				baseDao.setInt(3, inRoamingSummary.getReportType());
				baseDao.setString(4, inRoamingSummary.getTapcode());
				baseDao.setString(5, inRoamingSummary.getTrafficDirection());
				baseDao.setString(6, inRoamingSummary.getImsiPrefix());
				baseDao.setLong(7, inRoamingSummary.getCallCount());
				baseDao.setLong(8, inRoamingSummary.getDurationCount());
				baseDao.setLong(9,inRoamingSummary.getAmountCount());
				baseDao.setFloat(10, inRoamingSummary.getRate());
				baseDao.setLong(11, inRoamingSummary.getMinCallId());
				baseDao.setLong(12, inRoamingSummary.getMaxCallId());
				baseDao.setLong(13, inRoamingSummary.getCallCountVoice());
				baseDao.setLong(14, inRoamingSummary.getCallCountSms());
				
				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			//baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
		
		/**
		 * insetCountTable
		 * 
		 * @param HashMap
		 *            <String, String>, operator
		 * @throws SQLException
		 */
		public void updateInRoamingSummary(List<InboundRoamingSummary> inRoamingList, String operator)
				throws SQLException {
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
					baseDao.setLong(3,inRoamingSummary.getAmountCount());
					baseDao.setFloat(4, inRoamingSummary.getRate());
					baseDao.setLong(5, inRoamingSummary.getMinCallId());
					baseDao.setLong(6, inRoamingSummary.getMaxCallId());
					
					baseDao.setString(7, inRoamingSummary.getTrafficDate());
					baseDao.setString(8, inRoamingSummary.getHour());
					baseDao.setInt(9,inRoamingSummary.getReportType());
					baseDao.setString(10, inRoamingSummary.getImsiPrefix());
					baseDao.setString(11, inRoamingSummary.getTrafficDirection());
					
					baseDao.addBatch();
				}
				baseDao.exeBatchUpdate();
				//baseDao.conCommit();
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace();
			} finally {
				baseDao.close();
			}
		}
}
