package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallsNoDupIrsf;
import com.mobius.ra.core.pojo.HourlyMscIrsfReport;
import com.mobius.ra.core.pojo.HourlyReportStatus;
import com.mobius.ra.core.pojo.MscIrsfDetail;
import com.mobius.ra.core.pojo.Report;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class MscIrsfDao extends CommonDao {
	private static Logger logger = Logger.getLogger("RA-MSC-IRSF");
	
	private String timeZoneGMT = Constants.GMT;
	
	public MscIrsfDao(Report report) {
		super.report = report;
	}
	
	public List<CallsNoDupIrsf> getHourlyCallsNoDupList(String alias, String timeZoneString, Report report, String startTime, String endTime) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<CallsNoDupIrsf> hourlyCallsNoDupList = new CopyOnWriteArrayList<CallsNoDupIrsf>();
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(report.getSqls().get("getCallsNoDup"));
//		logger.info(sqlSb.toString()+" start time :"+Tools.getLocalCallTime(timeZoneString, startTime, timeZoneGMT)+" ,end time : "+ Tools.getLocalCallTime(timeZoneString, endTime, timeZoneGMT));
		try {
			baseDao.prepareStatement(sqlSb.toString());
			//Query call_time by GMT time zone from calls_igw (e.g. Indian/Maldives is GMT+5, than call_time between start time/end time -5 hours)
			baseDao.setString(1, Tools.getLocalCallTime(timeZoneString, startTime, timeZoneGMT));
			baseDao.setString(2, Tools.getLocalCallTime(timeZoneString, endTime, timeZoneGMT));
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				CallsNoDupIrsf callsNoDup = new CallsNoDupIrsf();
				callsNoDup.setCallTime(Tools.getLocalCallTime(timeZoneGMT, rs.getString("call_time"), timeZoneString));
				callsNoDup.setCallType(rs.getInt("call_type"));
				callsNoDup.setDuration(rs.getInt("duration"));
				callsNoDup.setsImsi(rs.getLong("s_imsi"));
				callsNoDup.setsMsisdn(rs.getLong("s_msisdn"));
				callsNoDup.setoMsisdn(rs.getLong("o_msisdn"));
				hourlyCallsNoDupList.add(callsNoDup);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return hourlyCallsNoDupList;
	}
	
	//Delete hourly_msc_irsf_report records by start day and end day before redo
	public void deleteHourlyMscIrsfReportBeforeRedo(String alias, String startDay, String endDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_msc_irsf_report where traffic_date between '" + startDay + "'" + " and '" + endDay + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void deleteAllHourlyIrsfMscReport(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_msc_irsf_report");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertHourlyMscIrsfReport(String alias, List<HourlyMscIrsfReport> hourlyMscIrsfReportList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into hourly_msc_irsf_report (traffic_date, traffic_hour, s_msisdn, s_imsi, tap_code, number_of_calls, total_duration, rule_id)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			
			//for test
//			if (hourlyMscIrsfReportList.size()>0) {
//				logger.info(sqlSb.toString()+":::"+hourlyMscIrsfReportList.get(0).getTrafficDate()+" "+hourlyMscIrsfReportList.get(0).getTrafficHour());
//			}
			
			for (HourlyMscIrsfReport hourlyMscIrsfReport : hourlyMscIrsfReportList) {
				baseDao.setString(1, hourlyMscIrsfReport.getTrafficDate());
				baseDao.setString(2, formatHour(hourlyMscIrsfReport.getTrafficHour()));
				baseDao.setLong(3, hourlyMscIrsfReport.getsMsisdn());
				baseDao.setLong(4, hourlyMscIrsfReport.getsImsi());
				baseDao.setString(5, hourlyMscIrsfReport.getTapcode());
				baseDao.setInt(6, hourlyMscIrsfReport.getNumberOfCalls());
				baseDao.setLong(7, hourlyMscIrsfReport.getTotalDuration());
				baseDao.setInt(8, hourlyMscIrsfReport.getRoleId());
				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	//Delete msc_irsf_detail records by start day and end day before redo
	public void deleteIrsfDetailBeforeRedo(String alias, String startDay, String endDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from ra_msc_irsf_detail where call_time between '" + startDay + "'" + " and '" + endDay + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void deleteAllMscIrsfDetail(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from ra_msc_irsf_detail");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertMscIrsfDetail(String alias, List<MscIrsfDetail> mscIrsfDetailList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into ra_msc_irsf_detail (traffic_date, call_time, s_msisdn, s_imsi, call_type, o_msisdn, duration, rule_id)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			for (MscIrsfDetail mscIrsfDetail : mscIrsfDetailList) {
				baseDao.setString(1, mscIrsfDetail.getTrafficDate());
				baseDao.setString(2, mscIrsfDetail.getCallTime());
				baseDao.setLong(3, Tools.getLongValue(mscIrsfDetail.getsMsisdn()));
				baseDao.setLong(4, Tools.getLongValue(mscIrsfDetail.getsImsi()));
				baseDao.setInt(5, mscIrsfDetail.getCallType());
				baseDao.setLong(6, Tools.getLongValue(mscIrsfDetail.getoMsisdn()));
				baseDao.setLong(7, Tools.getLongValue(mscIrsfDetail.getDuration()));
				baseDao.setInt(8, mscIrsfDetail.getRuleId());
				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	//Initialize all hours' report status
	public boolean checkStatus(String alias, String currentDay, String mscIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from hourly_report_status where status=1 and date = '" + currentDay + "' and report_type = '" + mscIrsfReportType + "'");
		List<HourlyReportStatus> hourlyReportStatusList = new ArrayList<HourlyReportStatus>();
		try {
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				HourlyReportStatus hourlyReportStatus = new HourlyReportStatus();
				hourlyReportStatus.setId(rs.getLong("id"));
				hourlyReportStatus.setDate(rs.getString("date"));
				hourlyReportStatus.setHour(rs.getInt("hour"));
				hourlyReportStatus.setReportType(rs.getString("report_type"));
				hourlyReportStatus.setStatus(rs.getInt("status"));
				hourlyReportStatusList.add(hourlyReportStatus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		if (hourlyReportStatusList.size() < 24) {
			return true;
		} else {
			return false;
		}
		
	}
	
	//Delete records between startDate and endDate
	public void deleteHourlyIrsfReportStatus(String alias, String startDay, String endDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_report_status where date between '" + startDay + "'" + " and '" + endDay + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	//Update report status before redo by start-date-before-redo
	public void updateHourlyMscIrsfReportStatusBeforeRedo(String alias, String startDay, String endDay, String mscIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("update hourly_report_status set status=? where report_type = '" + mscIrsfReportType + "' + and date between '" + startDay + "'" + " and '" + endDay + "'");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, "0");
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertOneDayIntoHourlyMscIrsfReportStatus(String alias, String currentDay, String mscIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into hourly_report_status (date,hour,report_type,status)");
			sqlSb.append(" values (?,?,?,?)");
			sqlSb.append(" on duplicate key update status=?");
			baseDao.prepareStatement(sqlSb.toString());
			for (int i = 0; i < 24; i++) {
				baseDao.setString(1, currentDay);
				baseDao.setInt(2, i);
				baseDao.setString(3, mscIrsfReportType);
				baseDao.setString(4, "0");
				baseDao.setInt(5, 0);
				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertHourlyIrsfReportStatus(String alias, String startDay, String endDay, String type) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into hourly_report_status (date,hour,report_type,status)");
			sqlSb.append(" values (?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			List<Date> dateList = dateSplit(startDay, endDay);
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			//insert for each hour from start day to end day, each day 24hs (0-23)
			for (Date date : dateList) {
				for (int i = 0; i < 24; i++) {
					baseDao.setString(1, sdf.format(date));
					baseDao.setInt(2, i);
					baseDao.setString(3, type);
					baseDao.setString(4, "0");
					baseDao.addBatch();
				}
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	private String formatHour(String trafficHour) {
		int hour = Integer.parseInt(trafficHour);
		if(hour < 10) {
			trafficHour = "0" + trafficHour;
		}
		return trafficHour;
	}
	
	private static List<Date> dateSplit(String startDateStr, String endDateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
		Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        if (!startDate.equals(endDate)) {
        	if (!startDate.before(endDate)) {
        		 throw new Exception("Start date should be before end date");
        	}
		}
	    Long spi = endDate.getTime() - startDate.getTime();
	    Long step = spi / (24 * 60 * 60 * 1000);// days between start date and end date

	    List<Date> dateList = new ArrayList<Date>();
	    dateList.add(endDate);
	    for (int i = 1; i <= step; i++) {
	        dateList.add(new Date(dateList.get(i - 1).getTime()
	                - (24 * 60 * 60 * 1000)));// one day after one day
	    }
	    Collections.reverse(dateList);//date ASC
	    return dateList;
	}

	public void markHourlyIrsfReportStatusFinished(String alias, String date, int hour, String reportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("update hourly_report_status set status=? where date=? and hour=? and report_type=?");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setInt(1, 1);
			baseDao.setString(2, date);
			baseDao.setInt(3, hour);
			baseDao.setString(4, reportType);
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	//Calculate start day and end day
	public String getDayByConfig(int num) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return sdf.format(calendar.getTime());
	}
	
	public String getOneDayBefore(String day) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
		Date startDate = sdf.parse(day);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(calendar.getTime());
	}
	
	public String getOneDayAfter(String day) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
		Date startDate = sdf.parse(day);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(calendar.getTime());
	}
	
	//Get hourly report list between from 24 hours, once upon thread number
	public List<HourlyReportStatus> getOneDayHourlyMscIrsfReportStatusList(String alias, String currentDay, int threadNum, String mscIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from hourly_report_status where status=0 and date = '" + currentDay + "' and report_type = '" + mscIrsfReportType + "' order by hour asc limit " + threadNum);
		List<HourlyReportStatus> hourlyReportStatusList = new ArrayList<HourlyReportStatus>();
		try {
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				HourlyReportStatus hourlyReportStatus = new HourlyReportStatus();
				hourlyReportStatus.setId(rs.getLong("id"));
				hourlyReportStatus.setDate(rs.getString("date"));
				hourlyReportStatus.setHour(rs.getInt("hour"));
				hourlyReportStatus.setReportType(rs.getString("report_type"));
				hourlyReportStatus.setStatus(rs.getInt("status"));
				hourlyReportStatusList.add(hourlyReportStatus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return hourlyReportStatusList;
	}
	
}
	