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
import com.mobius.ra.core.pojo.CallsIgw;
import com.mobius.ra.core.pojo.HotIrsfNum;
import com.mobius.ra.core.pojo.HourlyHotIrsfReport;
import com.mobius.ra.core.pojo.HourlyReportStatus;
import com.mobius.ra.core.pojo.HourlySuspectIrsfReport;
import com.mobius.ra.core.pojo.IrsfDetail;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.SuspectIrsfNum;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class IrsfDao extends CommonDao {
	private static Logger logger = Logger.getLogger("RA-IRSF");
	
	private String timeZoneGMT = Constants.GMT;
	
	public IrsfDao(Report report) {
		super.report = report;
	}
	
	public List<HotIrsfNum> getHotIrsfNumList(String alias, String timeZoneString, Report report) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<HotIrsfNum> hotIrstNumList = new ArrayList<HotIrsfNum>();
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(report.getSqls().get("getHotIrsfNumList"));
		try {
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				HotIrsfNum hotIrsfNum = new HotIrsfNum();
				hotIrsfNum.setId(rs.getLong("id"));
				hotIrsfNum.setSource(rs.getString("source"));
				hotIrsfNum.setType(rs.getString("type"));
				hotIrsfNum.setIrsfNumber(rs.getString("irsf_number"));
				hotIrsfNum.setCountry(rs.getString("country"));
				hotIrsfNum.setCdrAttribute(rs.getString("cdr_attribute"));
				hotIrsfNum.setDescription(rs.getString("description"));
				hotIrsfNum.setDateEntered(Tools.getLocalCallTime(timeZoneGMT, rs.getString("date_entered"), timeZoneString));
				hotIrsfNum.setEnteredBy(rs.getString("entered_by"));
				hotIrsfNum.setDateUpdated("");
				hotIrsfNum.setUpdatedBy(rs.getString("updated_by"));
				hotIrsfNum.setExpirationDate("");
				hotIrstNumList.add(hotIrsfNum);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return hotIrstNumList;
	}
	
	public List<SuspectIrsfNum> getSuspectIrsfNumList(String alias, String timeZoneString, Report report) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<SuspectIrsfNum> suspectIrstNumList = new ArrayList<SuspectIrsfNum>();
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(report.getSqls().get("getSuspectIrsfNumList"));
		try {
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				SuspectIrsfNum suspectIrsfNum = new SuspectIrsfNum();
				suspectIrsfNum.setId(rs.getLong("id"));
				suspectIrsfNum.setSource(rs.getString("source"));
				suspectIrsfNum.setType(rs.getString("type"));
				suspectIrsfNum.setIrsfNumber(rs.getString("irsf_number"));
				suspectIrsfNum.setCountry(rs.getString("country"));
				suspectIrsfNum.setCdrAttribute(rs.getString("cdr_attribute"));
				suspectIrsfNum.setDescription(rs.getString("description"));
				suspectIrsfNum.setDateEntered(Tools.getLocalCallTime(timeZoneGMT, rs.getString("date_entered"), timeZoneString));
				suspectIrsfNum.setEnteredBy(rs.getString("entered_by"));
				suspectIrsfNum.setDateUpdated("");
				suspectIrsfNum.setUpdatedBy(rs.getString("updated_by"));
				suspectIrsfNum.setExpirationDate("");
				suspectIrstNumList.add(suspectIrsfNum);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return suspectIrstNumList;
	}
	
	public List<CallsIgw> getHourlyCallsIgwList(String alias, String timeZoneString, Report report, String startTime, String endTime) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<CallsIgw> hourlyCallsIgwList = new CopyOnWriteArrayList<CallsIgw>();
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(report.getSqls().get("getIgwList"));
		try {
			baseDao.prepareStatement(sqlSb.toString());
			//Query call_time by GMT time zone from calls_igw (e.g. Indian/Maldives is GMT+5, than call_time between start time/end time -5 hours)
			baseDao.setString(1, Tools.getLocalCallTime(timeZoneString, startTime, timeZoneGMT));
			baseDao.setString(2, Tools.getLocalCallTime(timeZoneString, endTime, timeZoneGMT));
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				CallsIgw callsIgw = new CallsIgw();
				callsIgw.setCallId(rs.getLong("call_id"));
				callsIgw.setoCallId(rs.getLong("o_call_id"));
				callsIgw.setCallTime(Tools.getLocalCallTime(timeZoneGMT, rs.getString("call_time"), timeZoneString));
				callsIgw.setDuration(rs.getInt("duration"));
				callsIgw.setCallType(rs.getInt("call_type"));
				callsIgw.setSwId(rs.getInt("sw_id"));
				callsIgw.setsMsisdn(rs.getLong("s_msisdn"));
				callsIgw.setoMsisdn(rs.getLong("o_msisdn"));
				callsIgw.setsImsi(rs.getLong("s_imsi"));
				callsIgw.setsImei(rs.getLong("s_imei"));
				callsIgw.setsCi(rs.getInt("s_ci"));
				callsIgw.setsLac(rs.getInt("s_lac"));
				callsIgw.setTrunkIn(rs.getInt("trunk_in"));
				callsIgw.setTrunkOut(rs.getInt("trunk_out"));
				callsIgw.setTermCause(rs.getInt("term_cause"));
				callsIgw.setTermReason(rs.getInt("term_reason"));
				callsIgw.setSsCode(rs.getInt("ss_code"));
				callsIgw.setChargeIndicator(rs.getInt("charge_indicator"));
				hourlyCallsIgwList.add(callsIgw);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return hourlyCallsIgwList;
	}
	
	//Delete hourly_hot_irsf_report records by start day and end day before redo
	public void deleteHourlyHotIrsfReportBeforeRedo(String alias, String startDay, String endDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_hot_irsf_report where traffic_date between '" + startDay + "'" + " and '" + endDay + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void deleteAllHourlyHotIrsfReport(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_hot_irsf_report");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertHourlyHotIrsfReport(String alias, List<HourlyHotIrsfReport> hourlyHotIrsfReportList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into hourly_hot_irsf_report (report_type, type, traffic_date, traffic_hour, s_msisdn, msisdn_list_group, irsf_call_count, irsf_duration, call_out_count, call_out_duration, b_call_count, b_ratio, irsf_number_range)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			for (HourlyHotIrsfReport hourlyHotIrsfReport : hourlyHotIrsfReportList) {
				baseDao.setString(1, hourlyHotIrsfReport.getReportType());
				baseDao.setString(2, hourlyHotIrsfReport.getType());
				baseDao.setString(3, hourlyHotIrsfReport.getTrafficDate());
				baseDao.setString(4, formatHour(hourlyHotIrsfReport.getTrafficHour()));
				baseDao.setLong(5, hourlyHotIrsfReport.getsMsisdn());
				baseDao.setString(6, hourlyHotIrsfReport.getMsisdnListGroup());
				baseDao.setInt(7, hourlyHotIrsfReport.getIrsfCallCount());
				baseDao.setLong(8, hourlyHotIrsfReport.getIrsfDuration());
				baseDao.setInt(9, hourlyHotIrsfReport.getCallOutCount());
				baseDao.setLong(10, hourlyHotIrsfReport.getCallOutDuration());
				baseDao.setInt(11, hourlyHotIrsfReport.getbCallCount());
				baseDao.setDouble(12, hourlyHotIrsfReport.getbRatio());
				baseDao.setString(13, hourlyHotIrsfReport.getIrsfNumberRange());
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
	
	//Delete hourly_suspect_irsf_report records by start day and end day before redo
	public void deleteHourlySuspectIrsfReportBeforeRedo(String alias, String startDay, String endDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_suspect_irsf_report where traffic_date between '" + startDay + "'" + " and '" + endDay + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void deleteAllHourlySuspectIrsfReport(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from hourly_suspect_irsf_report");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertHourlySuspectIrsfReport(String alias, List<HourlySuspectIrsfReport> hourlySuspectIrsfReportList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into hourly_suspect_irsf_report (report_type, traffic_date, traffic_hour, o_msisdn, type, international_country, international_call_in_count, international_call_in_duration, a_call_count, a_ratio, irsf_number_range)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			for (HourlySuspectIrsfReport hourlySuspectIrsfReport : hourlySuspectIrsfReportList) {
				baseDao.setString(1, hourlySuspectIrsfReport.getReportType());
				baseDao.setString(2, hourlySuspectIrsfReport.getTrafficDate());
				baseDao.setString(3, formatHour(hourlySuspectIrsfReport.getTrafficHour()));
				baseDao.setLong(4, hourlySuspectIrsfReport.getoMsisdn());
				baseDao.setString(5, hourlySuspectIrsfReport.getType());
				baseDao.setString(6, hourlySuspectIrsfReport.getInternationalCountry());
				baseDao.setInt(7, hourlySuspectIrsfReport.getInternationalCallInCount());
				baseDao.setLong(8, hourlySuspectIrsfReport.getInternationalCallInDuration());
				baseDao.setInt(9, hourlySuspectIrsfReport.getaCallCount());
				baseDao.setDouble(10, hourlySuspectIrsfReport.getaRatio());
				baseDao.setString(11, hourlySuspectIrsfReport.getIrsfNumberRange());
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
	
	//Delete irsf_detail records by start day and end day before redo
	public void deleteIrsfDetailBeforeRedo(String alias, String startDay, String endDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from irsf_detail where call_time between '" + startDay + "'" + " and '" + endDay + "'");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void deleteAllIrsfDetail(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from irsf_detail");
		try {
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	public void insertIrsfDetail(String alias, List<IrsfDetail> irsfDetailList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into irsf_detail (call_type, type, s_msisdn, o_msisdn, duration, call_time,sw_id,s_imsi,s_imei,s_ci,s_lac,trunk_in,trunk_out,term_cause,term_reason,ss_code,charge_indicator,detail_type,report_type,insert_time,file_name,volume_download,volume_upload,charge_identifier,sgsn_address,ggsn_address,pdp_address,short_num,group_num, irsf_number_range)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			for (IrsfDetail irsfDetail : irsfDetailList) {
				baseDao.setInt(1, irsfDetail.getCallType());
				baseDao.setString(2, irsfDetail.getType());
				baseDao.setLong(3, Tools.getLongValue(irsfDetail.getsMsisdn()));
				baseDao.setLong(4, Tools.getLongValue(irsfDetail.getoMsisdn()));
				baseDao.setLong(5, irsfDetail.getDuration());
				baseDao.setString(6, irsfDetail.getCallTime());
				baseDao.setLong(7, Tools.getLongValue(irsfDetail.getSwId()));
				baseDao.setLong(8, Tools.getLongValue(irsfDetail.getsImsi()));
				baseDao.setLong(9, Tools.getLongValue(irsfDetail.getsImei()));
				baseDao.setInt(10, irsfDetail.getsCi());
				baseDao.setInt(11, irsfDetail.getsLac());
				baseDao.setLong(12, Tools.getLongValue(irsfDetail.getTrunkIn()));
				baseDao.setLong(13, Tools.getLongValue(irsfDetail.getTrunkOut()));
				baseDao.setLong(14, Tools.getLongValue(irsfDetail.getTermCause()));
				baseDao.setLong(15, Tools.getLongValue(irsfDetail.getTermReason()));
				baseDao.setInt(16, irsfDetail.getSsCode());
				baseDao.setInt(17, irsfDetail.getChargeIndicator());
				baseDao.setLong(18, irsfDetail.getDetailType());
				baseDao.setString(19, irsfDetail.getReportType());
				baseDao.setString(20, irsfDetail.getInsertTime());
				baseDao.setString(21, irsfDetail.getFileName());
				baseDao.setLong(22, Tools.getLongValue(irsfDetail.getVolumeDownload()));
				baseDao.setLong(23, Tools.getLongValue(irsfDetail.getVolumeUpload()));
				baseDao.setLong(24, Tools.getLongValue(irsfDetail.getChargeIdentifier()));
				baseDao.setString(25, irsfDetail.getSgsnAddress());
				baseDao.setString(26, irsfDetail.getGgsnAddress());
				baseDao.setString(27, irsfDetail.getPdpAddress());
				baseDao.setInt(28, irsfDetail.getShortNum());
				baseDao.setInt(29, irsfDetail.getGroupNum());
				baseDao.setString(30, irsfDetail.getIrsfNumberRange());
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
	
	//Initialize all hours' report status
	public boolean checkStatus(String alias, String currentDay, String hotIrsfReportType, String suspectIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from hourly_report_status where status=1 and date = '" + currentDay + "' and (report_type = '" + hotIrsfReportType + "' or report_type = '" + suspectIrsfReportType + "')");
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
		if (hourlyReportStatusList.size() < 48) {
			return true;
		} else {
			return false;
		}
		
	}
	
	//Delete records between startDate and endDate
//	public void deleteHourlyIrsfReportStatus(String alias, String startDay, String endDay) throws SQLException {
//		BaseDao baseDao = new BaseDao(alias);
//		StringBuilder sqlSb = new StringBuilder();
//		sqlSb.append("delete from hourly_report_status where date between '" + startDay + "'" + " and '" + endDay + "'");
//		try {
//			baseDao.prepareStatement(sqlSb.toString());
//			baseDao.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			baseDao.close();
//		}
//	}
	
	//Update report status before redo by start-date-before-redo
	public void updateHourlyIrsfReportStatusBeforeRedo(String alias, String startDay, String endDay, String hotIrsfReportType, String suspectIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("update hourly_report_status set status=? where (report_type = '" + hotIrsfReportType + "' or report_type = '" + suspectIrsfReportType + "') and date between '" + startDay + "'" + " and '" + endDay + "'");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, "0");
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}
	
	//Delete all report status records
//	public void deleteAllHourlyIrsfReportStatus(String alias) throws SQLException {
//		BaseDao baseDao = new BaseDao(alias);
//		StringBuilder sqlSb = new StringBuilder();
//		sqlSb.append("delete from hourly_report_status");
//		try {
//			baseDao.prepareStatement(sqlSb.toString());
//			baseDao.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			baseDao.close();
//		}
//	}
	
	public void insertOneDayIntoHourlyIrsfReportStatus(String alias, String currentDay, String type) throws SQLException {
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
				baseDao.setString(3, type);
				baseDao.setString(4, "0");
				baseDao.setInt(5, 0);
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
	
	//Get hourly report list between start day and end day, once upon thread number
	public List<HourlyReportStatus> getHourlyReportStatusList(String alias, String startDay, String endDay, int threadNum) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from hourly_report_status where status=0 and date between '" + startDay + "'" + " and '" + endDay + "' order by date,hour asc limit " + threadNum);
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
	
	//Get hourly report list between from 24 hours, once upon thread number
	public List<HourlyReportStatus> getOneDayHourlyIrsfReportStatusList(String alias, String currentDay, int threadNum, String hotIrsfReportType, String suspectIrsfReportType) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from hourly_report_status where status=0 and date = '" + currentDay + "' and (report_type = '" + hotIrsfReportType + "' or report_type = '" + suspectIrsfReportType + "') order by hour asc limit " + threadNum);
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
	
	//Insert new suspect number list
	public void insertSuspectIrsfNum(String alias, List<SuspectIrsfNum> suspectIrsfNumList) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into suspect_irsf_num (source, type, irsf_number, country, cdr_attribute, description, date_entered, entered_by, date_updated, updated_by, expiration_date)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?)");
			baseDao.prepareStatement(sqlSb.toString());
			for (SuspectIrsfNum suspectIrsfNum : suspectIrsfNumList) {
				baseDao.setString(1, suspectIrsfNum.getSource());
				baseDao.setString(2, suspectIrsfNum.getType());
				baseDao.setString(3, suspectIrsfNum.getIrsfNumber());
				baseDao.setString(4, suspectIrsfNum.getCountry());
				baseDao.setString(5, suspectIrsfNum.getCdrAttribute());
				baseDao.setString(6, suspectIrsfNum.getDescription());
				baseDao.setString(7, suspectIrsfNum.getDateEntered());
				baseDao.setString(8, suspectIrsfNum.getEnteredBy());
				baseDao.setString(9, suspectIrsfNum.getDateUpdated());
				baseDao.setString(10, suspectIrsfNum.getUpdatedBy());
				baseDao.setString(11, suspectIrsfNum.getExpirationDate());
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
	