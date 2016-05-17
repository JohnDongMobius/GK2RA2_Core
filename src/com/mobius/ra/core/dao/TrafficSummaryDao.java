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
import com.mobius.ra.core.pojo.RaTrafficSummary;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date Nov 11, 2013
 * @version v 1.0
 */
public class TrafficSummaryDao extends CommonDao{
	private final Logger logger = Logger.getLogger("RA-Billing");

	public TrafficSummaryDao(Report report) {
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
			sqlSb.append("select count(*) as count from ra_traffic_summary where traffic_date=? and report_type=?");

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
	 * insetCountTable
	 * 
	 * @param HashMap
	 *            <String, String>, operator
	 * @throws SQLException
	 */
	public void insertRaTrafficSummary(List<RaTrafficSummary> raTrafficSummaries, String operator)
			throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_traffic_summary (traffic_date, call_type, record_count, duration_count, amount_count, insert_time, feed_type, report_type)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?) ");

			baseDao.prepareStatement(sqlSb.toString());

			for (RaTrafficSummary raTrafficSummary : raTrafficSummaries) {
				baseDao.setString(1, raTrafficSummary.getTrafficDate());
				baseDao.setString(2, raTrafficSummary.getCallType());
				baseDao.setLong(3, raTrafficSummary.getRecordCount());
				baseDao.setLong(4, raTrafficSummary.getDurationCount());
				baseDao.setLong(5,raTrafficSummary.getAmountCount());
				baseDao.setString(6, Tools.getCalFullStr(Calendar.getInstance()));
				baseDao.setInt(7, raTrafficSummary.getFeedType());
				baseDao.setInt(8, raTrafficSummary.getReportType());
				
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
