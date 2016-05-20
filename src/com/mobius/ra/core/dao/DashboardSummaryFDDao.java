package com.mobius.ra.core.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.mobius.ra.core.pojo.DashboardSummaryFD;
import com.mobius.ra.core.pojo.Report;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class DashboardSummaryFDDao extends CommonDao {
	private static Logger logger = Logger.getLogger("RA-Dashboard-Summary");
	
	public DashboardSummaryFDDao(Report report) {
		super.report = report;
	}
	
	//Delete dashboard summary between startDate and endDate and by report name
	public void deleteDashboardSummaryByDate(String alias, String startDate, String endDate, String reportName) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from dashboard_summary where traffic_date between '" + startDate + "'" + " and '" + endDate + "' and report_name = '" + reportName + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void deleteAllDashboardSummary(String alias, String reportName) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from dashboard_summary where report_name = '" + reportName + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	//Insert or update by (traffic_date , report_name) unique index
//	public void insertOrUpdateDashboardSummary(String alias, DashboardSummaryFD dashboardSummary) throws SQLException {
//		BaseDao baseDao = new BaseDao(alias, false);
//		StringBuilder sqlSb = new StringBuilder();
//		sqlSb.append("insert into dashboard_summary (traffic_date, report_name, record_count, create_time, update_time)");
//		sqlSb.append(" values (?,?,?,?,?)");
//		sqlSb.append(" on duplicate key update record_count=?, update_time=?");
//		System.out
//				.println(sqlSb.toString());
//		try {
//			baseDao.prepareStatement(sqlSb.toString());
//			baseDao.setString(1, dashboardSummary.getTrafficDate());
//			baseDao.setString(2, dashboardSummary.getReportName());
//			baseDao.setInt(3, dashboardSummary.getRecordCount());
//			baseDao.setString(4, dashboardSummary.getCreateTime());
//			baseDao.setString(5, dashboardSummary.getUpdateTime());
//			baseDao.setInt(6, dashboardSummary.getRecordCount());
//			baseDao.setString(7, dashboardSummary.getUpdateTime());
//			baseDao.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.printStackTrace();
//		} finally {
//			baseDao.close();
//		}
//	}
	
	//Batch insert or update by (traffic_date , report_name) unique index
	public void insertOrUpdateDashboardSummaryList(String alias, List<DashboardSummaryFD> dashboardSummaryList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("insert into dashboard_summary (traffic_date, report_name, record_count, create_time, update_time)");
		sqlSb.append(" values (?,?,?,?,?)");
		sqlSb.append(" on duplicate key update record_count=?, update_time=?");
		//for test
//		if (dashboardSummaryList.size()>0) {
//			logger.info(sqlSb.toString()+"  "+dashboardSummaryList.get(0).getTrafficDate());
//		}
		try {
			baseDao.releaseStmt();
			baseDao.prepareStatement(sqlSb.toString());
			for (DashboardSummaryFD dashboardSummary : dashboardSummaryList) {
				baseDao.setString(1, dashboardSummary.getTrafficDate());
				baseDao.setString(2, dashboardSummary.getReportName());
				baseDao.setInt(3, dashboardSummary.getRecordCount());
				baseDao.setString(4, dashboardSummary.getCreateTime());
				baseDao.setString(5, dashboardSummary.getUpdateTime());
				baseDao.setInt(6, dashboardSummary.getRecordCount());
				baseDao.setString(7, dashboardSummary.getUpdateTime());
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
}
	