package com.mobius.ra.core.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.IrsfDao;
import com.mobius.ra.core.pojo.HotIrsfNum;
import com.mobius.ra.core.pojo.HourlyReportStatus;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.SuspectIrsfNum;
import com.mobius.ra.core.thread.IrsfSummaryThread;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class IrsfService implements ServiceInterface {
	private final Logger logger = LoggerFactory.getLogger(IrsfService.class);
	
	private String timeZoneString = Constants.coreCfg.getLocalTimezone();

	@Override
	public boolean summary(String[] args, String operator, Report report)
			throws SQLException, ParseException {
		boolean flag = false;
		boolean isRedo = false;
		IrsfDao irsfDao = new IrsfDao(report);
		String coreAlias = Constants.DB_NAME_CORE + Constants.coreCfg.getOperatorCode();
		String fraudAlias = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
		List<HotIrsfNum> hotIrsfNumList = irsfDao.getHotIrsfNumList(fraudAlias, timeZoneString, report);
		List<SuspectIrsfNum> suspectIrsfNumList = irsfDao.getSuspectIrsfNumList(fraudAlias, timeZoneString, report);
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Constants.IRSF_PROPERTIES_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String hotIrsfReportType = (String) properties.get("HOT_IRSF_REPORT_TYPE");
		String suspectIrsfReportType = (String) properties.get("SUSPECT_IRSF_REPORT_TYPE");
		
		Calendar base = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		String startDate = null;
		int calDays = report.getExecuteDaysDefault();

		if (args != null & args.length > 1) {
			startDate = args[0];
			base = Tools.getCalByStrHms(startDate);
			c = Tools.getCalByStrHms(startDate);
			calDays = new Integer(args[1]);
			logger.info("args[] startDate,calDays,base:" + startDate + "," + calDays + "," + base.getTime());
		}

		base.add(Calendar.DAY_OF_MONTH, -calDays);

		String currentDay = "";
		while (c.compareTo(base) > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			currentDay = sdf.format(c.getTime());
			
			//Initialize all hours' report status by start time and end time
			//Insert one day 24h records status 0, if exist and status already is 1, break don't create threads.
			if(irsfDao.checkStatus(fraudAlias, currentDay)) {
				irsfDao.insertOneDayIntoHourlyIrsfReportStatus(fraudAlias, currentDay, hotIrsfReportType);
				irsfDao.insertOneDayIntoHourlyIrsfReportStatus(fraudAlias, currentDay, suspectIrsfReportType);
				while(irsfDao.checkStatus(fraudAlias, currentDay)) {
					//Create multiple threads for hourly reports
					//Get hourlyReportStatusList from 24 hours split by thread num such as 8, loop 24/8=3 times
					List<HourlyReportStatus> hourlyReportStatusList = irsfDao.getOneDayHourlyReportStatusList(fraudAlias, currentDay, report.getThreadNum());
					
					int threadNum = hourlyReportStatusList.size();
					IrsfSummaryThread thread[] = new IrsfSummaryThread[threadNum];
					int n = 0;
					for (HourlyReportStatus hourlyReportStatus : hourlyReportStatusList) {
						thread[n] = new IrsfSummaryThread(report, irsfDao, coreAlias, fraudAlias, hotIrsfNumList,suspectIrsfNumList, hourlyReportStatus, isRedo);
						thread[n].start();
						n++;
					}
					
					//Confirm all sub-threads is not alive.
					for (int i = 0; i < threadNum; i++) {
						while (true) {
							if (thread[i].isAlive())
								try {
									Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							else
								break;
						}
					}
				}
			}
			
			//Insert IGW Feed Summary and Details when generating IRSF report.
			FeedService feedService = new FeedService();
			try {
				feedService.insertRAFeed(currentDay, 0, report);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			
			//When after one day passed, create one day report again
			c.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		flag = true;
		return flag;
	}

	@Override
	public boolean summaryRedo(String[] args, String operator, Report report)
			throws SQLException, ParseException {
		boolean flag = false;
		boolean isRedo = true;
		IrsfDao irsfDao = new IrsfDao(report);
		String coreAlias = Constants.DB_NAME_CORE + Constants.coreCfg.getOperatorCode();
		String fraudAlias = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
		List<HotIrsfNum> hotIrsfNumList = irsfDao.getHotIrsfNumList(fraudAlias, timeZoneString, report);
		List<SuspectIrsfNum> suspectIrsfNumList = irsfDao.getSuspectIrsfNumList(fraudAlias, timeZoneString, report);
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Constants.IRSF_PROPERTIES_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String hotIrsfReportType = (String) properties.get("HOT_IRSF_REPORT_TYPE");
		String suspectIrsfReportType = (String) properties.get("SUSPECT_IRSF_REPORT_TYPE");
		
		//Initialize status, reports and details before redo
		String startDay = irsfDao.getDayByConfig(report.getExecuteDaysDefault());
		String endDay = irsfDao.getDayByConfig(report.getStartDateBeforeRedo());
		
		//Start redo by manually status change
		irsfDao.updateHourlyIrsfReportStatusBeforeRedo(fraudAlias, startDay, endDay);
		
		irsfDao.deleteHourlyHotIrsfReportBeforeRedo(fraudAlias, startDay, endDay);
		irsfDao.deleteHourlySuspectIrsfReportBeforeRedo(fraudAlias, startDay, endDay);
		irsfDao.deleteIrsfDetailBeforeRedo(fraudAlias, startDay, endDay);
		
		Calendar base = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeRedo());
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeRedo());
		String startDate = null;
		int calDays = report.getExecuteDaysDefault();

		if (args != null & args.length > 1) {
			startDate = args[0];
			base = Tools.getCalByStrHms(startDate);
			c = Tools.getCalByStrHms(startDate);
			calDays = new Integer(args[1]);
			logger.info("args[] startDate,calDays,base:" + startDate + "," + calDays + "," + base.getTime());
		}

		base.add(Calendar.DAY_OF_MONTH, -calDays);

		String currentDay = "";
		while (c.compareTo(base) > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			currentDay = sdf.format(c.getTime());
			
			//Initialize all hours' report status by start time and end time
			//Insert one day 24h records status 0, if exist and status already is 1, break don't create threads.
			if(irsfDao.checkStatus(fraudAlias, currentDay)) {
				while(irsfDao.checkStatus(fraudAlias, currentDay)) {
					irsfDao.insertOneDayIntoHourlyIrsfReportStatus(fraudAlias, currentDay, hotIrsfReportType);
					irsfDao.insertOneDayIntoHourlyIrsfReportStatus(fraudAlias, currentDay, suspectIrsfReportType);
					
					//Create multiple threads for hourly reports
					//Get hourlyReportStatusList from 24 hours split by thread num such as 8, loop 24/8=3 times
					List<HourlyReportStatus> hourlyReportStatusList = irsfDao.getOneDayHourlyReportStatusList(fraudAlias, currentDay, report.getThreadNum());
					
					int threadNum = hourlyReportStatusList.size();
					IrsfSummaryThread thread[] = new IrsfSummaryThread[threadNum];
					int n = 0;
					for (HourlyReportStatus hourlyReportStatus : hourlyReportStatusList) {
						thread[n] = new IrsfSummaryThread(report, irsfDao, coreAlias, fraudAlias, hotIrsfNumList,suspectIrsfNumList, hourlyReportStatus, isRedo);
						thread[n].start();
						n++;
					}
					
					//Confirm all sub-threads is not alive.
					for (int i = 0; i < threadNum; i++) {
						while (true) {
							if (thread[i].isAlive())
								try {
									Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							else
								break;
						}
					}
				}
			}
			
			//Insert IGW Feed Summary and Details when generating IRSF report.
			FeedService feedService = new FeedService();
			try {
				feedService.insertRAFeed(currentDay, 0, report);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			
			//When after one day passed, create one day report again
			c.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		flag = true;
		return flag;
	}

}
