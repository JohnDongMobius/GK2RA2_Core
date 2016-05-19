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

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.MscIrsfDao;
import com.mobius.ra.core.dao.TapcodeDao;
import com.mobius.ra.core.pojo.HourlyReportStatus;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.Tapcode;
import com.mobius.ra.core.thread.MscIrsfSummaryThread;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class MscIrsfService implements ServiceInterface {
	private static Logger logger = Logger.getLogger("RA-MSC-IRSF");
	
	@Override
	public boolean summary(String[] args, String operator, Report report)
			throws SQLException, ParseException {
		boolean flag = false;
		boolean isRedo = false;
		TapcodeDao tapcodeDao = new TapcodeDao(report);
		MscIrsfDao mscIrsfDao = new MscIrsfDao(report);
		String coreAlias = Constants.DB_NAME_CORE + Constants.coreCfg.getOperatorCode();
		String fraudAlias = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
		List<Tapcode> tapCodeList = tapcodeDao.getAllTapcodes(fraudAlias, report);
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Constants.IRSF_PROPERTIES_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String mscIrsfReportType = (String) properties.get("MSC_IRSF_REPORT_TYPE");
		
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
			if(mscIrsfDao.checkStatus(fraudAlias, currentDay, mscIrsfReportType)) {
				mscIrsfDao.insertOneDayIntoHourlyMscIrsfReportStatus(fraudAlias, currentDay, mscIrsfReportType);
				while(mscIrsfDao.checkStatus(fraudAlias, currentDay, mscIrsfReportType)) {
					//Create multiple threads for hourly reports
					//Get hourlyReportStatusList from 24 hours split by thread num such as 8, loop 24/8=3 times
					List<HourlyReportStatus> hourlyReportStatusList = mscIrsfDao.getOneDayHourlyMscIrsfReportStatusList(fraudAlias, currentDay, report.getThreadNum(), mscIrsfReportType);
					
					int threadNum = hourlyReportStatusList.size();
					MscIrsfSummaryThread thread[] = new MscIrsfSummaryThread[threadNum];
					int n = 0;
					for (HourlyReportStatus hourlyReportStatus : hourlyReportStatusList) {
						thread[n] = new MscIrsfSummaryThread(report, mscIrsfDao, tapCodeList, coreAlias, fraudAlias, hourlyReportStatus, isRedo);
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
		TapcodeDao tapcodeDao = new TapcodeDao(report);
		MscIrsfDao mscIrsfDao = new MscIrsfDao(report);
		String coreAlias = Constants.DB_NAME_CORE + Constants.coreCfg.getOperatorCode();
		String fraudAlias = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
		List<Tapcode> tapCodeList = tapcodeDao.getAllTapcodes(fraudAlias, report);
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Constants.IRSF_PROPERTIES_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String mscIrsfReportType = (String) properties.get("MSC_IRSF_REPORT_TYPE");
		
		//Initialize status, reports and details before redo
		String startDay = mscIrsfDao.getDayByConfig(report.getExecuteDaysDefault());
		String endDay = mscIrsfDao.getDayByConfig(report.getStartDateBeforeRedo());
		
		//Start redo by manually status change
		mscIrsfDao.updateHourlyMscIrsfReportStatusBeforeRedo(fraudAlias, startDay, endDay, mscIrsfReportType);
		
		mscIrsfDao.deleteHourlyMscIrsfReportBeforeRedo(fraudAlias, startDay, endDay);
		mscIrsfDao.deleteIrsfDetailBeforeRedo(fraudAlias, startDay, endDay);
		
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
			if(mscIrsfDao.checkStatus(fraudAlias, currentDay, mscIrsfReportType)) {
				while(mscIrsfDao.checkStatus(fraudAlias, currentDay, mscIrsfReportType)) {
					mscIrsfDao.insertOneDayIntoHourlyMscIrsfReportStatus(fraudAlias, currentDay, mscIrsfReportType);
					
					//Create multiple threads for hourly reports
					//Get hourlyReportStatusList from 24 hours split by thread num such as 8, loop 24/8=3 times
					List<HourlyReportStatus> hourlyReportStatusList = mscIrsfDao.getOneDayHourlyMscIrsfReportStatusList(fraudAlias, currentDay, report.getThreadNum(), mscIrsfReportType);
					
					int threadNum = hourlyReportStatusList.size();
					MscIrsfSummaryThread thread[] = new MscIrsfSummaryThread[threadNum];
					int n = 0;
					for (HourlyReportStatus hourlyReportStatus : hourlyReportStatusList) {
						thread[n] = new MscIrsfSummaryThread(report, mscIrsfDao, tapCodeList, coreAlias, fraudAlias, hourlyReportStatus, isRedo);
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
			
			//When after one day passed, create one day report again
			c.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		flag = true;
		return flag;
	}

}
