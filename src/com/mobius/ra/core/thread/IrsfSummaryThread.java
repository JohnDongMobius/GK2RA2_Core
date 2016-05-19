package com.mobius.ra.core.thread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.DashboardSummaryFDDao;
import com.mobius.ra.core.dao.IrsfDao;
import com.mobius.ra.core.pojo.CallsIgw;
import com.mobius.ra.core.pojo.DashboardSummaryFD;
import com.mobius.ra.core.pojo.HotIrsfNum;
import com.mobius.ra.core.pojo.HourlyHotIrsfReport;
import com.mobius.ra.core.pojo.HourlyReportStatus;
import com.mobius.ra.core.pojo.HourlySuspectIrsfReport;
import com.mobius.ra.core.pojo.IrsfDetail;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.SuspectIrsfNum;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class IrsfSummaryThread extends Thread {
	private static Logger logger = Logger.getLogger("RA-IRSF");

	private Report report;
	private IrsfDao irsfDao;
	private String coreAlias;
	private String fraudAlias;
	private List<HotIrsfNum> hotIrsfNumList;
	private List<SuspectIrsfNum> suspectIrsfNumList;
	private HourlyReportStatus hourlyReportStatus;
	private boolean isRedo;
	private DashboardSummaryFDDao dashboardSummaryFDDao = new DashboardSummaryFDDao(report);
	
	public IrsfSummaryThread(Report report, IrsfDao irsfDao, String coreAlias, String fraudAlias, List<HotIrsfNum> hotIrsfNumList, List<SuspectIrsfNum> suspectIrsfNumList, HourlyReportStatus hourlyReportStatus, boolean isRedo) {
		this.report = report;
		this.irsfDao = irsfDao;
		this.coreAlias = coreAlias;
		this.fraudAlias = fraudAlias;
		this.hotIrsfNumList = hotIrsfNumList;
		this.suspectIrsfNumList = suspectIrsfNumList;
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
			List<CallsIgw> hourlyCallsIgwList = irsfDao.getHourlyCallsIgwList(coreAlias, timeZoneString, report, hourlyStartTime, hourlyEndTime);
			
//			Hot report
//			1. Do data check with the hourly igw data by the rules in design doc.
//			2. Summary A#, group and insert irsf_report table.
//			3. Insert each group to irsf_detail.
			
//			Rule 1 if
//			i.	IRSF HOT List is RANGE
//			ii.	CALL_TYPE = MOC
//			iii.	B# calls received IRSF_SEC >= 200 sec
//			iv.	B# calls received IRSF_CALL_COUNT >= 5
//			v.	Go back 1 hour of IGW records to get stats
//			Rule 2 if
//			i.	IRSF HOT List is NUMBER
//			ii.	CALL_TYPE = MOC
//			iii.	B# calls received IRSF_SEC >= 30 sec
//			iv.	B# calls received IRSF_CALL_COUNT >= 1
//			v.	Go back 1 hour of IGW records to get stats
			Properties properties = new Properties();
			properties.load(new FileInputStream(Constants.IRSF_PROPERTIES_PATH));
			
			String hotIrsfReportType = (String) properties.get("HOT_IRSF_REPORT_TYPE"); //irsf_detail.report_type
			int hotRangeIrsfSec = Integer.parseInt((String) properties.get("HOT_RANGE_IRSF_SEC"));
			int hotRangeIrsfCallCount = Integer.parseInt((String) properties.get("HOT_RANGE_IRSF_CALL_COUNT"));
			int hotNumberIrsfSec = Integer.parseInt((String) properties.get("HOT_NUMBER_IRSF_SEC"));
			int hotNumberIrsfCallCount = Integer.parseInt((String) properties.get("HOT_NUMBER_IRSF_CALL_COUNT"));
			
			Map<Long, HourlyHotIrsfReport> callsIgwANumberMap = new HashMap<Long, HourlyHotIrsfReport>();
			Map<Long, HourlyHotIrsfReport> callsIgwARangeMap = new HashMap<Long, HourlyHotIrsfReport>();
			
//			Suspect report
//			1. Do data check with the hourly igw data by the rules in design doc.
//			2. Summary B#, group and insert irsf_report table.
//			3. Insert each group to irsf_detail.
			
//			Rule 1 if
//			i.	IRSF Suspect List is NUMBER/RANGE
//			ii.	CALL_TYPE = MOC
//			iii.	B# calls received IRSF_SEC >= 600 sec
//			iv.	B# calls received IRSF_CALL_COUNT >= 20
//			v.	Go back 1 hour of IGW records to get stats
			String suspectIrsfReportType = (String) properties.get("SUSPECT_IRSF_REPORT_TYPE"); //irsf_detail.report_type
			int suspectRangeIrsfSec = Integer.parseInt((String) properties.get("SUSPECT_RANGE_IRSF_SEC"));
			int suspectRangeIrsfCallCount = Integer.parseInt((String) properties.get("SUSPECT_RANGE_IRSF_CALL_COUNT"));
			int suspectNumberIrsfSec = Integer.parseInt((String) properties.get("SUSPECT_NUMBER_IRSF_SEC"));
			int suspectNumberIrsfCallCount = Integer.parseInt((String) properties.get("SUSPECT_NUMBER_IRSF_CALL_COUNT"));
			
			Map<Long, HourlySuspectIrsfReport> callsIgwBNumberMap = new HashMap<Long, HourlySuspectIrsfReport>();
			Map<Long, HourlySuspectIrsfReport> callsIgwBRangeMap = new HashMap<Long, HourlySuspectIrsfReport>();
			
			//Mark processed list for neither hot or suspect, then new suspect rules.
			List<CallsIgw> processedCallsIgwList = new CopyOnWriteArrayList<CallsIgw>();
			processedCallsIgwList = hourlyCallsIgwList;
			for (CallsIgw callsIgw : hourlyCallsIgwList) {
				long oMsisdn = callsIgw.getoMsisdn();
				//B# found in IRSF Hot list
//				1> First check for IRSF number match if found no need to check range match
//				2> If not found in number then check in range
//				3> If not found in range then tag the record as processed and proceed
				for (HotIrsfNum hotIrsfNum : hotIrsfNumList) {
					//Number
					if (hotIrsfNum.getType().equals(Constants.IRSF_NUMBER_TYPE)) {
						if (Long.toString(oMsisdn).equals(hotIrsfNum.getIrsfNumber())) {
							//Found in hot then remove.
							processedCallsIgwList.remove(callsIgw);
							
							Long key = callsIgw.getsMsisdn();
							HourlyHotIrsfReport hourlyHotIrsfReport = callsIgwANumberMap.get(key);
							if (hourlyHotIrsfReport != null) {
								List<CallsIgw> callsIgwList = callsIgwANumberMap.get(key).getCallsIgwList();
								callsIgwList.add(callsIgw);
								hourlyHotIrsfReport.setCallsIgwList(callsIgwList);
								hourlyHotIrsfReport.setIrsfNumberRange(hotIrsfNum.getIrsfNumber());
								callsIgwANumberMap.put(key, hourlyHotIrsfReport);
								break;
							} else {
								hourlyHotIrsfReport = new HourlyHotIrsfReport();
								List<CallsIgw> callsIgwList = new ArrayList<CallsIgw>();
								callsIgwList.add(callsIgw);
								hourlyHotIrsfReport.setCallsIgwList(callsIgwList);
								hourlyHotIrsfReport.setIrsfNumberRange(hotIrsfNum.getIrsfNumber());
								callsIgwANumberMap.put(key, hourlyHotIrsfReport);
								break;
							}
						}
					//Range
					} else if (hotIrsfNum.getType().equals(Constants.IRSF_RANGE_TYPE)) {
						if (Long.toString(oMsisdn).startsWith(hotIrsfNum.getIrsfNumber())) {
							//Found in hot then remove.
							processedCallsIgwList.remove(callsIgw);
							
							Long key = callsIgw.getsMsisdn();
							HourlyHotIrsfReport hourlyHotIrsfReport = callsIgwARangeMap.get(key);
							if (hourlyHotIrsfReport != null) {
								List<CallsIgw> callsIgwList = callsIgwARangeMap.get(key).getCallsIgwList();
								callsIgwList.add(callsIgw);
								hourlyHotIrsfReport.setCallsIgwList(callsIgwList);
								hourlyHotIrsfReport.setIrsfNumberRange(hotIrsfNum.getIrsfNumber());
								callsIgwARangeMap.put(key, hourlyHotIrsfReport);
								break;
							} else {
								hourlyHotIrsfReport = new HourlyHotIrsfReport();
								List<CallsIgw> callsIgwList = new ArrayList<CallsIgw>();
								callsIgwList.add(callsIgw);
								hourlyHotIrsfReport.setCallsIgwList(callsIgwList);
								hourlyHotIrsfReport.setIrsfNumberRange(hotIrsfNum.getIrsfNumber());
								callsIgwARangeMap.put(key, hourlyHotIrsfReport);
								break;
							}
						}
					} else {
						
					}
				}
			}
			
			//processedCallsIgwList: not found from hot then go through suspect
			for (CallsIgw callsIgw : processedCallsIgwList) {
				long oMsisdn = callsIgw.getoMsisdn();
				//B# found in IRSF Suspect list
				for (SuspectIrsfNum suspectIrsfNum : suspectIrsfNumList) {
					//Number
					if (suspectIrsfNum.getType().equals(Constants.IRSF_NUMBER_TYPE)) {
						if (Long.toString(oMsisdn).equals(suspectIrsfNum.getIrsfNumber())) {
							//Found in suspect then remove.
							processedCallsIgwList.remove(callsIgw);
							
							Long key = callsIgw.getoMsisdn();
							HourlySuspectIrsfReport hourlySuspectIrsfReport = callsIgwBNumberMap.get(key);
							if (hourlySuspectIrsfReport != null) {
								List<CallsIgw> callsIgwList = callsIgwBNumberMap.get(key).getCallsIgwList();
								callsIgwList.add(callsIgw);
								hourlySuspectIrsfReport.setCallsIgwList(callsIgwList);
								hourlySuspectIrsfReport.setInternationalCountry(suspectIrsfNum.getCountry());
								hourlySuspectIrsfReport.setIrsfNumberRange(suspectIrsfNum.getIrsfNumber());
								callsIgwBNumberMap.put(key, hourlySuspectIrsfReport);
								break;
							} else {
								hourlySuspectIrsfReport = new HourlySuspectIrsfReport();
								List<CallsIgw> callsIgwList = new ArrayList<CallsIgw>();
								callsIgwList.add(callsIgw);
								hourlySuspectIrsfReport.setCallsIgwList(callsIgwList);
								hourlySuspectIrsfReport.setInternationalCountry(suspectIrsfNum.getCountry());
								hourlySuspectIrsfReport.setIrsfNumberRange(suspectIrsfNum.getIrsfNumber());
								callsIgwBNumberMap.put(key, hourlySuspectIrsfReport);
								break;
							}
						}
					//Range
					} else if (suspectIrsfNum.getType().equals(Constants.IRSF_RANGE_TYPE)) {
						if (Long.toString(oMsisdn).startsWith(suspectIrsfNum.getIrsfNumber())) {
							//Found in suspect then remove.
							processedCallsIgwList.remove(callsIgw);
							
							Long key = callsIgw.getoMsisdn();
							HourlySuspectIrsfReport hourlySuspectIrsfReport = callsIgwBRangeMap.get(key);
							if (hourlySuspectIrsfReport != null) {
								List<CallsIgw> callsIgwList = callsIgwBRangeMap.get(key).getCallsIgwList();
								callsIgwList.add(callsIgw);
								hourlySuspectIrsfReport.setCallsIgwList(callsIgwList);
								hourlySuspectIrsfReport.setInternationalCountry(suspectIrsfNum.getCountry());
								hourlySuspectIrsfReport.setIrsfNumberRange(suspectIrsfNum.getIrsfNumber());
								callsIgwBRangeMap.put(key, hourlySuspectIrsfReport);
								break;
							} else {
								hourlySuspectIrsfReport = new HourlySuspectIrsfReport();
								List<CallsIgw> callsIgwList = new ArrayList<CallsIgw>();
								callsIgwList.add(callsIgw);
								hourlySuspectIrsfReport.setCallsIgwList(callsIgwList);
								hourlySuspectIrsfReport.setInternationalCountry(suspectIrsfNum.getCountry());
								hourlySuspectIrsfReport.setIrsfNumberRange(suspectIrsfNum.getIrsfNumber());
								callsIgwBRangeMap.put(key, hourlySuspectIrsfReport);
								break;
							}
						}
					} else {
						
					}
				}
			}
			
			List<HourlyHotIrsfReport> hourlyHotIrsfReportList = new ArrayList<HourlyHotIrsfReport>();
			List<HourlySuspectIrsfReport> hourlySuspectIrsfReportList = new ArrayList<HourlySuspectIrsfReport>();
			List<IrsfDetail> irsfDetailList = new ArrayList<IrsfDetail>();
			
			//Dashboard summary
			List<DashboardSummaryFD> dsfdList = new ArrayList<DashboardSummaryFD>();
			DashboardSummaryFD hourlyHotDS = new DashboardSummaryFD();
			DashboardSummaryFD hourlySuspectDS = new DashboardSummaryFD();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createTime = new Date();
			
			//Show all A# making calls to known IRSF#s
			int hotRangeRecordCount = 0;
			for (Map.Entry<Long, HourlyHotIrsfReport> entry : callsIgwARangeMap.entrySet()) {
				Long key = entry.getKey();
				HourlyHotIrsfReport hourlyHotIrsfReport = entry.getValue();
				List<CallsIgw> callsIgwList = hourlyHotIrsfReport.getCallsIgwList();
				//Rules 1 for Range
				long sumDuration = 0;
				for (CallsIgw callsIgw : callsIgwList) {
					sumDuration += callsIgw.getDuration();
				}
				if (sumDuration > hotRangeIrsfSec && callsIgwList.size() > hotRangeIrsfCallCount) {
//				if (true) {  //Open this for test
					hotRangeRecordCount ++;
					for (CallsIgw callsIgw : callsIgwList) {
						IrsfDetail irsfDetail = new IrsfDetail();
						irsfDetail.setCallType(callsIgw.getCallType());
						irsfDetail.setType(Constants.IRSF_RANGE_TYPE);
						irsfDetail.setsMsisdn(callsIgw.getsMsisdn());
						irsfDetail.setoMsisdn(callsIgw.getoMsisdn());
						irsfDetail.setDuration(callsIgw.getDuration());
						irsfDetail.setCallTime(callsIgw.getCallTime());
						irsfDetail.setSwId(callsIgw.getSwId());
						irsfDetail.setsImsi(callsIgw.getsImsi());
						irsfDetail.setsImei(callsIgw.getsImei());
						irsfDetail.setsCi(callsIgw.getsCi());
						irsfDetail.setsLac(callsIgw.getsLac());
						irsfDetail.setTrunkIn(callsIgw.getTrunkIn());
						irsfDetail.setTrunkOut(callsIgw.getTrunkOut());
						irsfDetail.setTermCause(callsIgw.getTermCause());
						irsfDetail.setTermReason(callsIgw.getTermReason());
						irsfDetail.setSsCode(callsIgw.getSsCode());
						irsfDetail.setChargeIndicator(callsIgw.getChargeIndicator());
						irsfDetail.setDetailType(0);
						irsfDetail.setReportType(hotIrsfReportType);
						irsfDetail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
						irsfDetail.setFileName("");
						irsfDetail.setVolumeDownload(0);
						irsfDetail.setVolumeUpload(0);
						irsfDetail.setChargeIdentifier(0);
						irsfDetail.setSgsnAddress("");
						irsfDetail.setGgsnAddress("");
						irsfDetail.setPdpAddress("");
						irsfDetail.setShortNum(0);
						irsfDetail.setGroupNum(0);
						irsfDetail.setIrsfNumberRange(hourlyHotIrsfReport.getIrsfNumberRange());
						irsfDetailList.add(irsfDetail);
					}
					hourlyHotIrsfReport.setReportType(hotIrsfReportType);
					hourlyHotIrsfReport.setType(Constants.IRSF_RANGE_TYPE);
					hourlyHotIrsfReport.setTrafficDate(hourlyReportStatus.getDate());
					hourlyHotIrsfReport.setTrafficHour(String.valueOf(hourlyReportStatus.getHour()));
					hourlyHotIrsfReport.setsMsisdn(key);
					if(key.toString().startsWith(Constants.MALDIVES_COUNTRY_CODE)) {
						hourlyHotIrsfReport.setMsisdnListGroup(Constants.DOMESTIC_MSISDN_LIST_GROUP);
					} else {
						hourlyHotIrsfReport.setMsisdnListGroup(Constants.INTERNATIONAL_MSISDN_LIST_GROUP);
					}
					hourlyHotIrsfReport.setIrsfCallCount(callsIgwList.size());
					hourlyHotIrsfReport.setIrsfDuration(sumDuration);
					//Summary by key(s_msisdn/A#) call_out_count, call_out_duration, b_call_count, b_ratio put into map
					int callOutCount = 0;
					long callOutDuration = 0;
					int bCallCount = 0;
					double bRatio = 0;
					Set<Long> bCallSet = new HashSet<Long>();
					for (CallsIgw callsIgw : hourlyCallsIgwList) {
						if (callsIgw.getsMsisdn()==key) {
							callOutCount ++;
							callOutDuration += callsIgw.getDuration();
							bCallSet.add(callsIgw.getoMsisdn());
						}
					}
					bCallCount = bCallSet.size();
					if (callOutCount==0) {
						bRatio = 0;
					} else {
						bRatio = bCallCount/callOutCount;
					}
					hourlyHotIrsfReport.setCallOutCount(callOutCount);
					hourlyHotIrsfReport.setCallOutDuration(callOutDuration);
					hourlyHotIrsfReport.setbCallCount(bCallCount);
					hourlyHotIrsfReport.setbRatio(bRatio);
					
					hourlyHotIrsfReportList.add(hourlyHotIrsfReport);
				}
			}
			
			int hotNumberRecordCount = 0;
			for (Map.Entry<Long, HourlyHotIrsfReport> entry : callsIgwANumberMap.entrySet()) {
				Long key = entry.getKey();
				HourlyHotIrsfReport hourlyHotIrsfReport = entry.getValue();
				List<CallsIgw> callsIgwList = hourlyHotIrsfReport.getCallsIgwList();
				//Rules 2 for Number
				long sumDuration = 0;
				for (CallsIgw callsIgw : callsIgwList) {
					sumDuration += callsIgw.getDuration();
				}
				if (sumDuration > hotNumberIrsfSec && callsIgwList.size() > hotNumberIrsfCallCount) {
//				if (true) {  //Open this for test
					hotNumberRecordCount ++;
					for (CallsIgw callsIgw : callsIgwList) {
						IrsfDetail irsfDetail = new IrsfDetail();
						irsfDetail.setCallType(callsIgw.getCallType());
						irsfDetail.setType(Constants.IRSF_NUMBER_TYPE);
						irsfDetail.setsMsisdn(callsIgw.getsMsisdn());
						irsfDetail.setoMsisdn(callsIgw.getoMsisdn());
						irsfDetail.setDuration(callsIgw.getDuration());
						irsfDetail.setCallTime(callsIgw.getCallTime());
						irsfDetail.setSwId(callsIgw.getSwId());
						irsfDetail.setsImsi(callsIgw.getsImsi());
						irsfDetail.setsImei(callsIgw.getsImei());
						irsfDetail.setsCi(callsIgw.getsCi());
						irsfDetail.setsLac(callsIgw.getsLac());
						irsfDetail.setTrunkIn(callsIgw.getTrunkIn());
						irsfDetail.setTrunkOut(callsIgw.getTrunkOut());
						irsfDetail.setTermCause(callsIgw.getTermCause());
						irsfDetail.setTermReason(callsIgw.getTermReason());
						irsfDetail.setSsCode(callsIgw.getSsCode());
						irsfDetail.setChargeIndicator(callsIgw.getChargeIndicator());
						irsfDetail.setDetailType(0);
						irsfDetail.setReportType(hotIrsfReportType);
						irsfDetail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
						irsfDetail.setFileName("");
						irsfDetail.setVolumeDownload(0);
						irsfDetail.setVolumeUpload(0);
						irsfDetail.setChargeIdentifier(0);
						irsfDetail.setSgsnAddress("");
						irsfDetail.setGgsnAddress("");
						irsfDetail.setPdpAddress("");
						irsfDetail.setShortNum(0);
						irsfDetail.setGroupNum(0);
						irsfDetail.setIrsfNumberRange(hourlyHotIrsfReport.getIrsfNumberRange());
						irsfDetailList.add(irsfDetail);
					}
					hourlyHotIrsfReport.setReportType(hotIrsfReportType);
					hourlyHotIrsfReport.setType(Constants.IRSF_NUMBER_TYPE);
					hourlyHotIrsfReport.setTrafficDate(hourlyReportStatus.getDate());
					hourlyHotIrsfReport.setTrafficHour(String.valueOf(hourlyReportStatus.getHour()));
					hourlyHotIrsfReport.setsMsisdn(key);
					if(key.toString().startsWith(Constants.MALDIVES_COUNTRY_CODE)) {
						hourlyHotIrsfReport.setMsisdnListGroup(Constants.DOMESTIC_MSISDN_LIST_GROUP);
					} else {
						hourlyHotIrsfReport.setMsisdnListGroup(Constants.INTERNATIONAL_MSISDN_LIST_GROUP);
					}
					hourlyHotIrsfReport.setIrsfCallCount(callsIgwList.size());
					hourlyHotIrsfReport.setIrsfDuration(sumDuration);
					//Summary by key(s_msisdn/A#) call_out_count, call_out_duration, b_call_count, b_ratio put into map
					int callOutCount = 0;
					long callOutDuration = 0;
					int bCallCount = 0;
					double bRatio = 0;
					Set<Long> bCallSet = new HashSet<Long>();
					for (CallsIgw callsIgw : hourlyCallsIgwList) {
						if (callsIgw.getsMsisdn()==key) {
							callOutCount ++;
							callOutDuration += callsIgw.getDuration();
							bCallSet.add(callsIgw.getoMsisdn());
						}
					}
					bCallCount = bCallSet.size();
					if (callOutCount==0) {
						bRatio = 0;
					} else {
						bRatio = bCallCount/callOutCount;
					}
					hourlyHotIrsfReport.setCallOutCount(callOutCount);
					hourlyHotIrsfReport.setCallOutDuration(callOutDuration);
					hourlyHotIrsfReport.setbCallCount(bCallCount);
					hourlyHotIrsfReport.setbRatio(bRatio);
					
					hourlyHotIrsfReportList.add(hourlyHotIrsfReport);
				}
			}
			
			hourlyHotDS.setTrafficDate(hourlyStartTime);
			hourlyHotDS.setReportName(Constants.HOURLY_HOT_IRSF_REPORT_NAME);
			hourlyHotDS.setRecordCount(hotRangeRecordCount + hotNumberRecordCount);
			hourlyHotDS.setCreateTime(sdf.format(createTime));
			hourlyHotDS.setUpdateTime(sdf.format(createTime));
			
			//Suspect reports. Show all B#s that can be potential new IRSF#s
			int suspectRangeRecordCount = 0;
			for (Map.Entry<Long, HourlySuspectIrsfReport> entry : callsIgwBRangeMap.entrySet()) {
				Long key = entry.getKey();
				HourlySuspectIrsfReport hourlySuspectIrsfReport = entry.getValue();
				List<CallsIgw> callsIgwList = hourlySuspectIrsfReport.getCallsIgwList();
				//Rules 2 for Number
				long sumDuration = 0;
				for (CallsIgw callsIgw : callsIgwList) {
					sumDuration += callsIgw.getDuration();
				}
				if (sumDuration > suspectRangeIrsfSec && callsIgwList.size() > suspectRangeIrsfCallCount) {
//				if (true) {  //Open this for test
					suspectRangeRecordCount ++;
					for (CallsIgw callsIgw : callsIgwList) {
						IrsfDetail irsfDetail = new IrsfDetail();
						irsfDetail.setCallType(callsIgw.getCallType());
						irsfDetail.setType(Constants.IRSF_RANGE_TYPE);
						irsfDetail.setsMsisdn(callsIgw.getsMsisdn());
						irsfDetail.setoMsisdn(callsIgw.getoMsisdn());
						irsfDetail.setDuration(callsIgw.getDuration());
						irsfDetail.setCallTime(callsIgw.getCallTime());
						irsfDetail.setSwId(callsIgw.getSwId());
						irsfDetail.setsImsi(callsIgw.getsImsi());
						irsfDetail.setsImei(callsIgw.getsImei());
						irsfDetail.setsCi(callsIgw.getsCi());
						irsfDetail.setsLac(callsIgw.getsLac());
						irsfDetail.setTrunkIn(callsIgw.getTrunkIn());
						irsfDetail.setTrunkOut(callsIgw.getTrunkOut());
						irsfDetail.setTermCause(callsIgw.getTermCause());
						irsfDetail.setTermReason(callsIgw.getTermReason());
						irsfDetail.setSsCode(callsIgw.getSsCode());
						irsfDetail.setChargeIndicator(callsIgw.getChargeIndicator());
						irsfDetail.setDetailType(0);
						irsfDetail.setReportType(hotIrsfReportType);
						irsfDetail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
						irsfDetail.setFileName("");
						irsfDetail.setVolumeDownload(0);
						irsfDetail.setVolumeUpload(0);
						irsfDetail.setChargeIdentifier(0);
						irsfDetail.setSgsnAddress("");
						irsfDetail.setGgsnAddress("");
						irsfDetail.setPdpAddress("");
						irsfDetail.setShortNum(0);
						irsfDetail.setGroupNum(0);
						irsfDetail.setIrsfNumberRange(hourlySuspectIrsfReport.getIrsfNumberRange());
						irsfDetailList.add(irsfDetail);
					}
					hourlySuspectIrsfReport.setReportType(hotIrsfReportType);
					hourlySuspectIrsfReport.setType(Constants.IRSF_RANGE_TYPE);
					hourlySuspectIrsfReport.setTrafficDate(hourlyReportStatus.getDate());
					hourlySuspectIrsfReport.setTrafficHour(String.valueOf(hourlyReportStatus.getHour()));
					hourlySuspectIrsfReport.setoMsisdn(key);
					//Summary by key(o_msisdn/B#) international_country, international_calls_in_count, international_calls_in_duration, a_call_count, a_ratio put into map
					//TODO Compare 3 prefix between o and s if same international
					int callInCount = 0;
					long callInDuration = 0;
					int aCallCount = 0;
					double aRatio = 0;
					Set<Long> aCallSet = new HashSet<Long>();
					for (CallsIgw callsIgw : hourlyCallsIgwList) {
						if (callsIgw.getoMsisdn()==key) {
							callInCount ++;
							callInDuration += callsIgw.getDuration();
							aCallSet.add(callsIgw.getsMsisdn());
						}
					}
					aCallCount = aCallSet.size();
					if (callInCount==0) {
						aRatio = 0;
					} else {
						aRatio = aCallCount/callInCount;
					}
					hourlySuspectIrsfReport.setInternationalCallInCount(callInCount);
					hourlySuspectIrsfReport.setInternationalCallInDuration(callInDuration);
					hourlySuspectIrsfReport.setaCallCount(aCallCount);
					hourlySuspectIrsfReport.setaRatio(aRatio);
					
					hourlySuspectIrsfReportList.add(hourlySuspectIrsfReport);
				}
			}
			
			int suspectNumberRecordCount = 0;
			for (Map.Entry<Long, HourlySuspectIrsfReport> entry : callsIgwBNumberMap.entrySet()) {
				Long key = entry.getKey();
				HourlySuspectIrsfReport hourlySuspectIrsfReport = entry.getValue();
				List<CallsIgw> callsIgwList = hourlySuspectIrsfReport.getCallsIgwList();
				//Rules 2 for Number
				long sumDuration = 0;
				for (CallsIgw callsIgw : callsIgwList) {
					sumDuration += callsIgw.getDuration();
				}
				if (sumDuration > suspectNumberIrsfSec && callsIgwList.size() > suspectNumberIrsfCallCount) {
//				if (true) {  //Open this for test
					suspectNumberRecordCount ++;
					for (CallsIgw callsIgw : callsIgwList) {
						IrsfDetail irsfDetail = new IrsfDetail();
						irsfDetail.setCallType(callsIgw.getCallType());
						irsfDetail.setType(Constants.IRSF_NUMBER_TYPE);
						irsfDetail.setsMsisdn(callsIgw.getsMsisdn());
						irsfDetail.setoMsisdn(callsIgw.getoMsisdn());
						irsfDetail.setDuration(callsIgw.getDuration());
						irsfDetail.setCallTime(callsIgw.getCallTime());
						irsfDetail.setSwId(callsIgw.getSwId());
						irsfDetail.setsImsi(callsIgw.getsImsi());
						irsfDetail.setsImei(callsIgw.getsImei());
						irsfDetail.setsCi(callsIgw.getsCi());
						irsfDetail.setsLac(callsIgw.getsLac());
						irsfDetail.setTrunkIn(callsIgw.getTrunkIn());
						irsfDetail.setTrunkOut(callsIgw.getTrunkOut());
						irsfDetail.setTermCause(callsIgw.getTermCause());
						irsfDetail.setTermReason(callsIgw.getTermReason());
						irsfDetail.setSsCode(callsIgw.getSsCode());
						irsfDetail.setChargeIndicator(callsIgw.getChargeIndicator());
						irsfDetail.setDetailType(0);
						irsfDetail.setReportType(hotIrsfReportType);
						irsfDetail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
						irsfDetail.setFileName("");
						irsfDetail.setVolumeDownload(0);
						irsfDetail.setVolumeUpload(0);
						irsfDetail.setChargeIdentifier(0);
						irsfDetail.setSgsnAddress("");
						irsfDetail.setGgsnAddress("");
						irsfDetail.setPdpAddress("");
						irsfDetail.setShortNum(0);
						irsfDetail.setGroupNum(0);
						irsfDetail.setIrsfNumberRange(hourlySuspectIrsfReport.getIrsfNumberRange());
						irsfDetailList.add(irsfDetail);
					}
					hourlySuspectIrsfReport.setReportType(hotIrsfReportType);
					hourlySuspectIrsfReport.setType(Constants.IRSF_NUMBER_TYPE);
					hourlySuspectIrsfReport.setTrafficDate(hourlyReportStatus.getDate());
					hourlySuspectIrsfReport.setTrafficHour(String.valueOf(hourlyReportStatus.getHour()));
					hourlySuspectIrsfReport.setoMsisdn(key);
					//Summary by key(o_msisdn/B#) international_country, international_calls_in_count, international_calls_in_duration, a_call_count, a_ratio put into map
					//TODO Compare 3 prefix between o and s if same international
					int callInCount = 0;
					long callInDuration = 0;
					int aCallCount = 0;
					double aRatio = 0;
					Set<Long> aCallSet = new HashSet<Long>();
					for (CallsIgw callsIgw : hourlyCallsIgwList) {
						if (callsIgw.getoMsisdn()==key) {
							callInCount ++;
							callInDuration += callsIgw.getDuration();
							aCallSet.add(callsIgw.getsMsisdn());
						}
					}
					aCallCount = aCallSet.size();
					if (callInCount==0) {
						aRatio = 0;
					} else {
						aRatio = aCallCount/callInCount;
					}
					hourlySuspectIrsfReport.setInternationalCallInCount(callInCount);
					hourlySuspectIrsfReport.setInternationalCallInDuration(callInDuration);
					hourlySuspectIrsfReport.setaCallCount(aCallCount);
					hourlySuspectIrsfReport.setaRatio(aRatio);
					
					hourlySuspectIrsfReportList.add(hourlySuspectIrsfReport);
				}
			}
			
			//New suspect rules
			long sumDuration = 0;
			Map<Long, List<CallsIgw>> callsIgwListMap = new HashMap<Long, List<CallsIgw>>();
			for (CallsIgw callsIgw : processedCallsIgwList) {
				//Group by oMsisdn, summary duration for each oMsisdn
				if (callsIgwListMap.containsKey(callsIgw.getoMsisdn())) {
					callsIgwListMap.get(callsIgw.getoMsisdn()).add(callsIgw);
				} else {
					List<CallsIgw> callsIgwList = new ArrayList<CallsIgw>();
					callsIgwList.add(callsIgw);
					callsIgwListMap.put(callsIgw.getoMsisdn(), callsIgwList);
				}
			}
			
			List<SuspectIrsfNum> suspectIrsfNumList = new ArrayList<SuspectIrsfNum>();
			for (Map.Entry<Long, List<CallsIgw>> entry : callsIgwListMap.entrySet()) {
				Long key = entry.getKey();
				List<CallsIgw> callsIgwList = entry.getValue();
				for (CallsIgw callsIgw : callsIgwList) {
					sumDuration += callsIgw.getDuration();
				}
				if (sumDuration > suspectNumberIrsfSec && processedCallsIgwList.size() > suspectNumberIrsfCallCount) {
					SuspectIrsfNum suspectIrsfNum = new SuspectIrsfNum();
					//TODO set fields below
					suspectIrsfNum.setSource("");
					suspectIrsfNum.setType(Constants.IRSF_NUMBER_TYPE);
					suspectIrsfNum.setIrsfNumber(key.toString());
					suspectIrsfNum.setCdrCountry("");
					suspectIrsfNum.setCdrAttribute("O_MSISDN");
					suspectIrsfNum.setDescription("");
					suspectIrsfNum.setDateEntered(Tools.getCalFullStr(Calendar.getInstance()));
					suspectIrsfNum.setDateUpdated("0000-00-00 00:00:00");
					suspectIrsfNum.setEnteredBy("");
					suspectIrsfNum.setUpdatedBy("");
					suspectIrsfNum.setExpirationDate("0000-00-00 00:00:00");
					
					suspectIrsfNumList.add(suspectIrsfNum);
				}
			}
			irsfDao.insertSuspectIrsfNum(fraudAlias, suspectIrsfNumList);
			
			hourlySuspectDS.setTrafficDate(hourlyStartTime);
			hourlySuspectDS.setReportName(Constants.HOURLY_SUSPECT_IRSF_REPORT_NAME);
			hourlySuspectDS.setRecordCount(suspectRangeRecordCount + suspectNumberRecordCount);
			hourlySuspectDS.setCreateTime(sdf.format(createTime));
			hourlySuspectDS.setUpdateTime(sdf.format(createTime));
			
			irsfDao.insertHourlyHotIrsfReport(fraudAlias, hourlyHotIrsfReportList);
			irsfDao.insertHourlySuspectIrsfReport(fraudAlias, hourlySuspectIrsfReportList);
			irsfDao.insertIrsfDetail(fraudAlias, irsfDetailList);
			
			irsfDao.markHourlyIrsfReportStatusFinished(fraudAlias, date, hour, hotIrsfReportType);
			irsfDao.markHourlyIrsfReportStatusFinished(fraudAlias, date, hour, suspectIrsfReportType);
			
			dsfdList.add(hourlyHotDS);
			dsfdList.add(hourlySuspectDS);
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
}
