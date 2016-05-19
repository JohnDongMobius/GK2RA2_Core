package com.mobius.ra.core.thread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.dao.DashboardSummaryFDDao;
import com.mobius.ra.core.dao.MscIrsfDao;
import com.mobius.ra.core.pojo.CallsNoDupIrsf;
import com.mobius.ra.core.pojo.DashboardSummaryFD;
import com.mobius.ra.core.pojo.HourlyMscIrsfReport;
import com.mobius.ra.core.pojo.HourlyReportStatus;
import com.mobius.ra.core.pojo.MscIrsfDetail;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.Tapcode;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class MscIrsfSummaryThread extends Thread {
	private static Logger logger = Logger.getLogger("RA-MSC-IRSF");

	private Report report;
	private MscIrsfDao mscIrsfDao;
	private List<Tapcode> tapCodeList;
	private String coreAlias;
	private String fraudAlias;
	private HourlyReportStatus hourlyReportStatus;
	private boolean isRedo;
	private DashboardSummaryFDDao dashboardSummaryFDDao = new DashboardSummaryFDDao(report);
	private List<CallsNoDupIrsf> callsNoDupIrsfList = new ArrayList<CallsNoDupIrsf>();
	
	public MscIrsfSummaryThread(Report report, MscIrsfDao mscIrsfDao, List<Tapcode> tapCodeList, String coreAlias, String fraudAlias, HourlyReportStatus hourlyReportStatus, boolean isRedo) {
		this.report = report;
		this.mscIrsfDao = mscIrsfDao;
		this.tapCodeList = tapCodeList;
		this.coreAlias = coreAlias;
		this.fraudAlias = fraudAlias;
		this.hourlyReportStatus = hourlyReportStatus;
		this.isRedo = isRedo;
	}

	@Override
	public void run() {
		String date = hourlyReportStatus.getDate();
		int hour = hourlyReportStatus.getHour();
		
		String hourlyStartTime = formatHour(date, hour, 0);
		String hourlyEndTime = formatHour(date, hour, 1);
		
		String timeZoneString = Constants.coreCfg.getLocalTimezone();
		try {
			List<CallsNoDupIrsf> hourlyCallsNoDupIrsfList = mscIrsfDao.getHourlyCallsNoDupList(coreAlias, timeZoneString, report, hourlyStartTime, hourlyEndTime);
			
//			Rule 1: calls summary duration => 900 seconds 
//			Rule 2: calls to unique bnumber => 50% 
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(Constants.IRSF_PROPERTIES_PATH));
			
			String mscIrsfReportType = (String) properties.get("MSC_IRSF_REPORT_TYPE");
			long callsSummaryDuration = Long.parseLong((String) properties.get("CALLS_SUMMARY_DURATION"));
			double callsUniqueBNumber = Double.parseDouble((String) properties.get("CALLS_UNIQUE_BNUMBER"));
			
			Map<Long, List<CallsNoDupIrsf>> callsNoDupIrsfMap = new HashMap<Long, List<CallsNoDupIrsf>>();
			if(hourlyCallsNoDupIrsfList!=null&&hourlyCallsNoDupIrsfList.size()>0) {
				for (int i = 0; i < hourlyCallsNoDupIrsfList.size(); i++) {
					
					if((i+1) < hourlyCallsNoDupIrsfList.size()) {
						if(hourlyCallsNoDupIrsfList.get(i).getsMsisdn() == hourlyCallsNoDupIrsfList.get(i+1).getsMsisdn()){
							callsNoDupIrsfList.add(hourlyCallsNoDupIrsfList.get(i));
						}else{
							callsNoDupIrsfList.add(hourlyCallsNoDupIrsfList.get(i));
							callsNoDupIrsfMap.put(hourlyCallsNoDupIrsfList.get(i).getsMsisdn(), callsNoDupIrsfList);
							callsNoDupIrsfList = new ArrayList<CallsNoDupIrsf>();
						}
					}else if ((i+1)==hourlyCallsNoDupIrsfList.size()){
						if(hourlyCallsNoDupIrsfList.get(i-1).getsMsisdn() == hourlyCallsNoDupIrsfList.get(i).getsMsisdn()){
							callsNoDupIrsfList.add(hourlyCallsNoDupIrsfList.get(i));
							callsNoDupIrsfMap.put(hourlyCallsNoDupIrsfList.get(i).getsMsisdn(), callsNoDupIrsfList);
						}else{
							callsNoDupIrsfList = new ArrayList<CallsNoDupIrsf>();
							callsNoDupIrsfList.add(hourlyCallsNoDupIrsfList.get(i));
							callsNoDupIrsfMap.put(hourlyCallsNoDupIrsfList.get(i).getsMsisdn(), callsNoDupIrsfList);
							
						}
					}else{
						
					}
				}
			}
			
			List<HourlyMscIrsfReport> hourlyMscIrsfReportList = new ArrayList<HourlyMscIrsfReport>();
			List<MscIrsfDetail> mscIrsfDetailList = new  ArrayList<MscIrsfDetail>();
			
			for (Map.Entry<Long, List<CallsNoDupIrsf>> entry : callsNoDupIrsfMap.entrySet()) {
				Long sMsisdn = entry.getKey();
				List<CallsNoDupIrsf> callsNoDupIrsfList = entry.getValue();
				long sumDuration = 0;
				int numberOfCalls = 0;
				HashSet<Long> oMsisdnSet = new HashSet<Long>();
				for(CallsNoDupIrsf callsNoDupIrsf : callsNoDupIrsfList) {
					sumDuration += callsNoDupIrsf.getDuration();
					numberOfCalls ++;
					oMsisdnSet.add(callsNoDupIrsf.getoMsisdn());
				}
				if(sumDuration >= callsSummaryDuration){
					HourlyMscIrsfReport hourlyMscIrsfReport = new HourlyMscIrsfReport();
					hourlyMscIrsfReport.setTrafficDate(hourlyReportStatus.getDate());
					hourlyMscIrsfReport.setTrafficHour(String.valueOf(hourlyReportStatus.getHour()));
					hourlyMscIrsfReport.setsMsisdn(sMsisdn);
					hourlyMscIrsfReport.setsImsi(callsNoDupIrsfList.get(0).getsImsi());
					hourlyMscIrsfReport.setTapcode(getTapcodeFromImsi(String.valueOf(callsNoDupIrsfList.get(0).getsImsi()), tapCodeList));
					hourlyMscIrsfReport.setNumberOfCalls(numberOfCalls);
					hourlyMscIrsfReport.setRoleId(1);
					hourlyMscIrsfReportList.add(hourlyMscIrsfReport);
					for(CallsNoDupIrsf callsNoDupIrsf : callsNoDupIrsfList) {
						MscIrsfDetail mscIrsfDetail = new MscIrsfDetail();
						mscIrsfDetail.setTrafficDate(hourlyReportStatus.getDate());
						mscIrsfDetail.setCallTime(callsNoDupIrsf.getCallTime());
						mscIrsfDetail.setsMsisdn(callsNoDupIrsf.getsMsisdn());
						mscIrsfDetail.setsImsi(callsNoDupIrsf.getsImsi());
						mscIrsfDetail.setCallType(callsNoDupIrsf.getCallType());
						mscIrsfDetail.setoMsisdn(callsNoDupIrsf.getoMsisdn());
						mscIrsfDetail.setDuration(callsNoDupIrsf.getDuration());
						mscIrsfDetail.setRuleId(1);
						mscIrsfDetailList.add(mscIrsfDetail);
					}
				} else if ((oMsisdnSet.size()/callsNoDupIrsfList.size())>=callsUniqueBNumber) {
					HourlyMscIrsfReport hourlyMscIrsfReport = new HourlyMscIrsfReport();
					hourlyMscIrsfReport.setTrafficDate(hourlyReportStatus.getDate());
					hourlyMscIrsfReport.setTrafficHour(String.valueOf(hourlyReportStatus.getHour()));
					hourlyMscIrsfReport.setsMsisdn(sMsisdn);
					hourlyMscIrsfReport.setsImsi(callsNoDupIrsfList.get(0).getsImsi());
					hourlyMscIrsfReport.setTapcode(getTapcodeFromImsi(String.valueOf(callsNoDupIrsfList.get(0).getsImsi()), tapCodeList));
					hourlyMscIrsfReport.setNumberOfCalls(numberOfCalls);
					hourlyMscIrsfReport.setRoleId(2);
					hourlyMscIrsfReportList.add(hourlyMscIrsfReport);
					for(CallsNoDupIrsf callsNoDupIrsf : callsNoDupIrsfList) {
						MscIrsfDetail mscIrsfDetail = new MscIrsfDetail();
						mscIrsfDetail.setTrafficDate(hourlyReportStatus.getDate());
						mscIrsfDetail.setCallTime(callsNoDupIrsf.getCallTime());
						mscIrsfDetail.setsMsisdn(callsNoDupIrsf.getsMsisdn());
						mscIrsfDetail.setsImsi(callsNoDupIrsf.getsImsi());
						mscIrsfDetail.setCallType(callsNoDupIrsf.getCallType());
						mscIrsfDetail.setoMsisdn(callsNoDupIrsf.getoMsisdn());
						mscIrsfDetail.setDuration(callsNoDupIrsf.getDuration());
						mscIrsfDetail.setRuleId(2);
						mscIrsfDetailList.add(mscIrsfDetail);
					}
				} else {
					
				}
			}
			
			//Dashboard summary
			List<DashboardSummaryFD> dsfdList = new ArrayList<DashboardSummaryFD>();
			DashboardSummaryFD hourlyMscDS = new DashboardSummaryFD();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createTime = new Date();
			
			hourlyMscDS.setTrafficDate(hourlyStartTime);
			hourlyMscDS.setReportName(Constants.HOURLY_MSC_IRSF_REPORT_NAME);
			hourlyMscDS.setRecordCount(hourlyMscIrsfReportList.size());
			hourlyMscDS.setCreateTime(sdf.format(createTime));
			hourlyMscDS.setUpdateTime(sdf.format(createTime));
			
			mscIrsfDao.insertHourlyMscIrsfReport(fraudAlias, hourlyMscIrsfReportList);
			mscIrsfDao.insertMscIrsfDetail(fraudAlias, mscIrsfDetailList);
			
			mscIrsfDao.markHourlyIrsfReportStatusFinished(fraudAlias, date, hour, mscIrsfReportType);
			
			dsfdList.add(hourlyMscDS);
			dashboardSummaryFDDao.insertOrUpdateDashboardSummaryList(fraudAlias, dsfdList);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Format hour by date and hour number
	private static String formatHour(String date, int hour, int flag) {
		if (hour<9) {
			if (flag==0) {
				return date + " 0" + hour + ":00:00";
			} else {
				hour = hour +1;
				return date + " 0" + hour + ":00:00";
			}
		} else if (hour==9) {
			if (flag==0) {
				return date + " 0" + hour + ":00:00";
			} else {
				hour = hour +1;
				return date + " " + hour + ":00:00";
			}
		} else if(hour<23) {
			if (flag==0) {
				return date + " " + hour + ":00:00";
			} else {
				hour = hour +1;
				return date + " " + hour + ":00:00";
			}
		} else if (hour == 23) {
			if (flag==0) {
				return date +" 23:00:00";
			} else {
				return date +" 23:59:59";
			}
		} else {
			return "";
		}
	}
	
	//Mapping tapcodes
	private String getTapcodeFromImsi(String sImsi, List<Tapcode> tapcodeList) {
		String imsiPrefix = "";
		String tapcodeString = "";
		for (Tapcode tapcode : tapcodeList) {
			imsiPrefix = tapcode.getImsiPrefix();
			if(sImsi.contains(imsiPrefix)){
				tapcodeString = tapcode.getTapCode();
			}else{
			}
		}
		return tapcodeString;
	}
}
