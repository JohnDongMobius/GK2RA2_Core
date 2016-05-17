package com.mobius.ra.core.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.FeedDao;
import com.mobius.ra.core.dao.XMscSummaryDao;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.RaFileName;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.thread.MscXSummaryThread;
import com.mobius.ra.core.thread.XMscSummaryThread;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class XMscSummaryServiceHourly implements ServiceInterface{
	private static Logger logger = Logger.getLogger("RA-Billing");

	/**
	 * for realtime operation.
	 * 
	 * @param args
	 * @param operator
	 * @param report
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Override
	public boolean summary(String args[], String operator, Report report) throws SQLException, ParseException {
		boolean flag = false;
		XMscSummaryDao dao = new XMscSummaryDao(report);
		Calendar base = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		String startDate = null;
		int calDays = report.getExecuteDaysDefault();
		String detailTypes[] = report.getSqls().get(Constants.DETAIL_TYPE).split(",");
		logger.debug("detailTypes: " + detailTypes[0] + "," + detailTypes[1]);

		if (args != null & args.length > 1) {
			startDate = args[0];
			base = Tools.getCalByStrHms(startDate);
			c = Tools.getCalByStrHms(startDate);
			calDays = new Integer(args[1]);
			logger.info("args[] startDate,calDays,base:" + startDate + "," + calDays + "," + base.getTime());
		}

		base.add(Calendar.DAY_OF_MONTH, -calDays);

		while (c.compareTo(base) > 0) {

			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			String yes = formatter.format(c.getTime());
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("traffic_date", yes);

			long startTime4Day = System.currentTimeMillis();
			// System.out.println(yes+" Executing start: " + new Date());
			logger.info(yes + " Executing start: " + new Date());

			// step0.1: check if the day's moc data and billing data has been
			// generated, if no,
			// return.
			Calendar c2 = c;
			SimpleDateFormat formatter2 = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY2);
			// formatter2.setTimeZone(TimeZone.getTimeZone(GkConstant.GMT));
			String yes2 = formatter2.format(c2.getTime()) + Constants.START_HOUR;
			logger.info("yes2: " + yes2);
			if (!dao.checkIfDataProcessedByPP(yes2, operator, true, report)) {
				// System.out.println("The Moc Data of " +
				// formatter2.format(c.getTime()) +
				// " hasn't been generated...");
				logger.info("The Data of " + formatter2.format(c.getTime()) + " hasn't been generated by pp...");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			// step0.2: check if the day's summary has been generated, if yes,
			// return.
			if (dao.checkIfExist(yes, operator, report.getType())) {
				// System.out.println("The report of "+yes+" has existed...");
				logger.info("The report of " + yes + " has existed...");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			if (report.getSubscriberType() == 1) {
				// step0.3: check if prepaid list is updaing now, if yes,
				// return.
				if (dao.checkIfPrepaidListIsUpading(null, operator)) {
					logger.info("The prepaid list is updating now, waiting for a moment...");
					try {
						Thread.sleep(Constants.coreCfg.getSleepDurationOfPaid());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
			}

			// step1: load MSC data
			// step1.1: load MSC data of one day
			Map<String, Object> map = dao.getMsc(yes, operator, true);
			@SuppressWarnings("unchecked")
			List<CallsNoDup> mscList = (List<CallsNoDup>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, CallsNoDup> mscHashMap = (HashMap<String, CallsNoDup>) map.get("hashMap");

			// step1.1: load boundary MSC data
			map = dao.getBoundaryMsc(yes, operator, true);
			@SuppressWarnings("unchecked")
			List<CallsNoDup> mscBoundaryList = (List<CallsNoDup>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, CallsNoDup> mscBoundayHashMap = (HashMap<String, CallsNoDup>) map.get("hashMap");
			mscHashMap.putAll(mscBoundayHashMap);

			// step2: load Billing data
			// step2.1: load MSC data of one day
			map = dao.getBilling(yes, operator, true);
			@SuppressWarnings("unchecked")
			List<RaBilling> billingList = (List<RaBilling>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, RaBilling> billingHashMap = (HashMap<String, RaBilling>) map.get("hashMap");

			// step2.2: load boundary MSC data
			map = dao.getBoundaryBilling(yes, operator, true);
			@SuppressWarnings("unchecked")
			List<RaBilling> billingBoundaryList = (List<RaBilling>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, RaBilling> billingBoundaryHashMap = (HashMap<String, RaBilling>) map.get("hashMap");
			billingHashMap.putAll(billingBoundaryHashMap);

			if (Constants.coreCfg.getNoDataComparison().equals("1")) {
				if (mscList.size() == 0 && billingList.size() == 0) {
					logger.info("msc and billing data is null.");
					c.add(Calendar.DAY_OF_MONTH, -1);
					continue;
				}
			} else {
				if (mscList.size() == 0 || billingList.size() == 0) {
					logger.info("msc or billing data is null.");
					c.add(Calendar.DAY_OF_MONTH, -1);
					continue;
				}
			}

			// step3: get MSC and MSC/IN
			long startTime = System.currentTimeMillis();
			// System.out.println("loop1 start : " + new Date());
			logger.info("loop1 start : " + new Date());
			int mocThreadNum = report.getThreadNum();
			int mocSize = mscList.size();
			MscXSummaryThread[] thread = new MscXSummaryThread[mocThreadNum];

			// step3.0: add billing boundary list as referred data.
			billingList.addAll(billingBoundaryList);

			HashMap<String, String> fileNameHashMap = new HashMap();
			
			if (report.getMscFilenameUpdate() == Constants.INT_ONE) {
				fileNameHashMap = dao.getFileNameHashMap(yes, Constants.DB_NAME_MEDIATOR + Constants.coreCfg.getOperatorCode());
			}
				
			// step3.1: create sub-threads to process.
			for (int i = 0; i < mocThreadNum; i++) {
				List<CallsNoDup> subList = null;
				if (i < mocThreadNum - 1) {
					subList = mscList.subList(mocSize / mocThreadNum * i, mocSize / mocThreadNum * (i + 1));
				} else {
					subList = mscList.subList(mocSize / mocThreadNum * i, mocSize);
				}

				Map<String, Object> objsMap = new HashMap<String, Object>();
				// XMscSummaryDao dao2 = new XMscSummaryDao(report);
				objsMap.put(Constants.KEY_MSC_LIST, subList);
				objsMap.put(Constants.KEY_BILLING_MAP, billingHashMap);
				objsMap.put(Constants.KEY_BILLING_LIST, billingList);
				objsMap.put(Constants.KEY_OPERATOR, operator);
				objsMap.put(Constants.KEY_DAO, dao);
				objsMap.put(Constants.KEY_REPORTTYPE, report.getType());
				objsMap.put(Constants.KEY_DETAILTYPE, detailTypes[0]);
				objsMap.put(Constants.KEY_REPORT, report);
				objsMap.put(Constants.KEY_DATE, yes);
				objsMap.put(Constants.KEY_FILENAME_HASHMAP, fileNameHashMap);

				thread[i] = new MscXSummaryThread(objsMap);
				thread[i].start();
			}

			// step3.2: confirm all sub-threads is not alive.
			for (int i = 0; i < mocThreadNum; i++) {
				while (true) {
					if (thread[i].isAlive())
						try {
							Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						break;
				}
			}

			long endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step4: get IN and IN/MOC
			startTime = System.currentTimeMillis();
			logger.info("loop2 start : " + new Date());
			int billingThreadNum = report.getThreadNum();
			XMscSummaryThread[] inThread = new XMscSummaryThread[billingThreadNum];

			// step4.0: remove billing boundary list firstly and add MSC
			// boundary list as referred data.
			billingList.removeAll(billingBoundaryList);
			mscList.addAll(mscBoundaryList);
			int billingSize = billingList.size();

			// step4.1: create sub-threads to process.
			for (int i = 0; i < billingThreadNum; i++) {
				List<RaBilling> subList = null;
				if (i < billingThreadNum - 1) {
					subList = billingList.subList(billingSize / billingThreadNum * i, billingSize / billingThreadNum * (i + 1));
				} else {
					subList = billingList.subList(billingSize / billingThreadNum * i, billingSize);
				}

				Map<String, Object> objsMap = new HashMap<String, Object>();
				// XMscSummaryDao dao2 = new XMscSummaryDao(report);
				objsMap.put(Constants.KEY_BILLING_LIST, subList);
				objsMap.put(Constants.KEY_MSC_MAP, mscHashMap);
				objsMap.put(Constants.KEY_MSC_LIST, mscList);
				objsMap.put(Constants.KEY_OPERATOR, operator);
				objsMap.put(Constants.KEY_DAO, dao);
				objsMap.put(Constants.KEY_DETAILTYPE, detailTypes[1]);
				objsMap.put(Constants.KEY_REPORTTYPE, report.getType());
				objsMap.put(Constants.KEY_REPORT, report);
				objsMap.put(Constants.KEY_DATE, yes);
				objsMap.put(Constants.KEY_FILENAME_HASHMAP, fileNameHashMap);

				inThread[i] = new XMscSummaryThread(objsMap);
				inThread[i].start();
			}

			// step4.2: confirm all sub-threads is not alive.
			for (int i = 0; i < billingThreadNum; i++) {
				while (true) {
					if (inThread[i].isAlive())
						try {
							Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						break;
				}
			}

			endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step5.1: get files of MSC
			Set<String> mscFileSet = new HashSet<String>();
			// pending.

			// step5.2: get files of Billing.
			Set<String> billingFileSet = new HashSet<String>();
			List<RaFileName> fdetailList = new ArrayList<RaFileName>();
			for (RaBilling billing : billingList) {
				RaFileName fileName = new RaFileName();
				String localTimezoneStr = Constants.coreCfg.getLocalTimezone();

				if (!Tools.checkNullorSpace(billing.getFileName()) && !billingFileSet.contains(billing.getFileName())) {
					fileName.setReportType(new Integer(report.getType()));
					fileName.setCallTime(Tools.getLocalCallTime("GMT", billing.getCallTime(), localTimezoneStr));
					fileName.setRespectiveType(Constants.RESPECTIVE_TYPE_B_PARTY);
					fileName.setFileName(billing.getFileName());
					fileName.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));

					fdetailList.add(fileName);
					billingFileSet.add(billing.getFileName());
				}
			}
			dao.batchInsertRaFileNameTable(fdetailList, operator);

			// step5.3: insert ra_feed.
			FeedService feedService = new FeedService();
			try {
				feedService.insertRAFeed(yes, 0, report);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// step6: get all kinds of count.
			long mscCallCount = mocSize;
			long mscDurationCount = 0;
			long mscCallCountNotBilling = 0;
			long mscDurationCountNotBilling = 0;
			long billingCallCount = billingList.size();
			long billingDurationCount = 0;
			long billingCallCountNotMsc = 0;
			long billingDurationCountNotMsc = 0;
			long mscFileCount = 0;
			long billingFileCount = 0;
			for (CallsNoDup moc : mscList) {
				mscDurationCount += moc.getDuration();
			}
			for (RaBilling in : billingList) {
				billingDurationCount += in.getDuration();
			}

			long mocCounts[] = dao.getXOnlyCallCount(yes, operator, detailTypes[0]);
			long inCounts[] = dao.getXOnlyCallCount(yes, operator, detailTypes[1]);
			mscCallCountNotBilling = mocCounts[0];
			mscDurationCountNotBilling = mocCounts[1];
			billingCallCountNotMsc = inCounts[0];
			billingDurationCountNotMsc = inCounts[1];

			FeedDao feedDao = new FeedDao();
			String alias = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
			mscFileCount = feedDao.getFeedCount(yes, report.getMscFeedType(), alias);
			billingFileCount = feedDao.getFeedCount(yes, report.getBillingFeedType(), alias);

			logger.info("The files count of mscFileCount/billingFileCount: " + mscFileCount + "/" + billingFileCount);

			result.put("moc_call_count", mscCallCount + "");
			result.put("moc_duration", mscDurationCount + "");
			result.put("billing_call_count", billingCallCount + "");
			result.put("billing_duration", billingDurationCount + "");

			result.put("moc_call_count_without_billing", mscCallCountNotBilling + "");
			result.put("moc_duration_without_billing", mscDurationCountNotBilling + "");
			result.put("billing_call_count_without_moc", billingCallCountNotMsc + "");
			result.put("billing_duration_without_moc", billingDurationCountNotMsc + "");
			result.put("report_type", report.getType());

			result.put("msc_file_count", mscFileCount + "");
			result.put("billing_file_count", billingFileCount + "");

			// step7: insert count values.
			if ("0".equals(result.get("moc_call_count").trim()) && "0".equals(result.get("moc_duration").trim())
					&& "0".equals(result.get("billing_call_count").trim()) && "0".equals(result.get("billing_duration").trim())
					&& "0".equals(result.get("moc_call_count_without_billing").trim()) && "0".equals(result.get("moc_duration_without_billing").trim())
					&& "0".equals(result.get("billing_call_count_without_moc").trim()) && "0".equals(result.get("billing_duration_without_moc").trim())) {
				// System.out.println("all count are 0, so no inserting/updating");
				logger.info("all count are 0, so no inserting/updating");
			}
			// else if (InMocMaldivesDao.checkIfExist(yes, operator))
			// InMocMaldivesDao.updateCountTable(result, operator);
			else {
				dao.insertCountTable(result, operator);
			}

			// step8: update status for exporter and wait for exporting.
			dao.updateStatus4Exporter(yes, operator);

			// step9: day-1, reverse order.
			c.add(Calendar.DAY_OF_MONTH, -1);

			long endTime4Day = System.currentTimeMillis();
			// System.out.println(yes+" Cost Time: " +
			// (endTime4Day-startTime4Day) + " ms\n");
			logger.info(yes + " Cost Time: " + (endTime4Day - startTime4Day) + " ms\n");
		}

		flag = true;
		return flag;
	}

	/**
	 * for redo operation.
	 * 
	 * @param args
	 * @param operator
	 * @param report
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Override
	public boolean summaryRedo(String args[], String operator, Report report) throws SQLException, ParseException {
		boolean flag = false;
		XMscSummaryDao dao = new XMscSummaryDao(report);
		Calendar base = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		// execute before two days.
		base.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeRedo());
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeRedo());
		String startDate = null;
		int calDays = report.getExecuteDaysDefault();
		String detailTypes[] = report.getSqls().get(Constants.DETAIL_TYPE).split(",");
		logger.debug("detailTypes: " + detailTypes[0] + "," + detailTypes[1]);

		if (args != null & args.length > 1) {
			startDate = args[0];
			base = Tools.getCalByStrHms(startDate);
			c = Tools.getCalByStrHms(startDate);
			calDays = new Integer(args[1]);
			logger.info("args[] startDate,calDays,base:" + startDate + "," + calDays + "," + base.getTime());
		}

		base.add(Calendar.DAY_OF_MONTH, -calDays);

		while (c.compareTo(base) > 0) {

			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			String yes = formatter.format(c.getTime());
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("traffic_date", yes);

			long startTime4Day = System.currentTimeMillis();
			// logger.info("");
			logger.info(yes + " Executing start: " + new Date());

			// step0: check if needing redo, if no, return.
			int redo_times = dao.getRedoTimes(yes, operator, report.getType());
			if (redo_times > 0) {// if >0, redo complete.
				logger.info("The report of " + yes + " has been redone...");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}
			if (redo_times == -1) {// if -1, no report, need redeem.
				logger.info("The report of " + yes + " isn't exist, need redeeming...");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			// step0.1: check if the day's MOC data has been redone by PP, if
			// no, return.
			Calendar c2 = c;
			SimpleDateFormat formatter2 = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY2);
			// formatter2.setTimeZone(TimeZone.getTimeZone(GkConstant.GMT));
			String yes2 = formatter2.format(c2.getTime()) + Constants.START_HOUR;
			logger.info("yes2: " + yes2);
			if (!dao.checkIfDataProcessedByPP(yes2, operator, false, report)) {
				logger.info("The Data of " + formatter.format(c.getTime()) + " hasn't been redone by PP...");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			logger.info("The report of " + yes + " start to redo...");

			// step0.2: delete old recon detail of MSC and Billing.
			logger.info("Delete the detail data&files of MSC/Billing...");
			dao.deleteRaDetailByDate(yes, operator, report.getType());

			// step0.3: delete old file detail of billing.
			dao.deleteRaFileNameByDate(yes, operator, report.getType());

			if (report.getSubscriberType() == 1) {
				// step0.4: check if prepaid list is updaing now, if yes,
				// return.
				if (dao.checkIfPrepaidListIsUpading(null, operator)) {
					logger.info("The prepaid list is updating now, waiting for a moment...");
					try {
						Thread.sleep(Constants.coreCfg.getSleepDurationOfPaid());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
			}

			// step1: load MSC data
			// step1.1: load MSC data of one day
			Map<String, Object> map = dao.getMsc(yes, operator, false);
			@SuppressWarnings("unchecked")
			List<CallsNoDup> mscList = (List<CallsNoDup>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, CallsNoDup> mscHashMap = (HashMap<String, CallsNoDup>) map.get("hashMap");

			// step1.1: load boundary MSC data
			map = dao.getBoundaryMsc(yes, operator, true);
			@SuppressWarnings("unchecked")
			List<CallsNoDup> mscBoundaryList = (List<CallsNoDup>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, CallsNoDup> mscBoundayHashMap = (HashMap<String, CallsNoDup>) map.get("hashMap");
			mscHashMap.putAll(mscBoundayHashMap);

			// step2: load Billing data
			// step2.1: load MSC data of one day
			map = dao.getBilling(yes, operator, false);
			@SuppressWarnings("unchecked")
			List<RaBilling> billingList = (List<RaBilling>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, RaBilling> billingHashMap = (HashMap<String, RaBilling>) map.get("hashMap");

			// step2.2: load boundary MSC data
			map = dao.getBoundaryBilling(yes, operator, false);
			@SuppressWarnings("unchecked")
			List<RaBilling> billingBoundaryList = (List<RaBilling>) map.get("list");
			@SuppressWarnings("unchecked")
			HashMap<String, RaBilling> billingBoundaryHashMap = (HashMap<String, RaBilling>) map.get("hashMap");
			billingHashMap.putAll(billingBoundaryHashMap);

			if (Constants.coreCfg.getNoDataComparison().equals("1")) {
				if (mscList.size() == 0 && billingList.size() == 0) {
					logger.info("msc and billing data is null.");
					c.add(Calendar.DAY_OF_MONTH, -1);
					continue;
				}
			} else {
				if (mscList.size() == 0 || billingList.size() == 0) {
					logger.info("msc or billing data is null.");
					c.add(Calendar.DAY_OF_MONTH, -1);
					continue;
				}
			}

			// step3: get MOC and MOC/IN
			long startTime = System.currentTimeMillis();
			logger.info("loop1 start : " + new Date());
			int mscThreadNum = report.getThreadNum();
			int mscSize = mscList.size();
			MscXSummaryThread[] thread = new MscXSummaryThread[mscThreadNum];

			// step3.0: add billing boundary list as referred data.
			billingList.addAll(billingBoundaryList);

			HashMap<String, String> fileNameHashMap = new HashMap();
			if (report.getMscFilenameUpdate() == Constants.INT_ONE) {
				fileNameHashMap = dao.getFileNameHashMap(yes, Constants.DB_NAME_MEDIATOR + Constants.coreCfg.getOperatorCode());
			}
			// step3.1: create sub-threads to process.
			for (int i = 0; i < mscThreadNum; i++) {
				List<CallsNoDup> subList = null;
				if (i < mscThreadNum - 1) {
					subList = mscList.subList(mscSize / mscThreadNum * i, mscSize / mscThreadNum * (i + 1));
				} else {
					subList = mscList.subList(mscSize / mscThreadNum * i, mscSize);
				}

				Map<String, Object> objsMap = new HashMap<String, Object>();
				// XMscSummaryDao dao2 = new XMscSummaryDao(report);
				objsMap.put(Constants.KEY_MSC_LIST, subList);
				objsMap.put(Constants.KEY_BILLING_MAP, billingHashMap);
				objsMap.put(Constants.KEY_BILLING_LIST, billingList);
				objsMap.put(Constants.KEY_DAO, dao);
				objsMap.put(Constants.KEY_OPERATOR, operator);
				objsMap.put(Constants.KEY_DETAILTYPE, detailTypes[0]);
				objsMap.put(Constants.KEY_REPORTTYPE, report.getType());
				objsMap.put(Constants.KEY_REPORT, report);
				objsMap.put(Constants.KEY_FILENAME_HASHMAP, fileNameHashMap);//missing.

				thread[i] = new MscXSummaryThread(objsMap);
				thread[i].start();
			}

			// step3.2: confirm all sub-threads is not alive.
			for (int i = 0; i < mscThreadNum; i++) {
				while (true) {
					if (thread[i].isAlive())
						try {
							Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						break;
				}
			}

			long endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step4: get IN and IN/MOC
			startTime = System.currentTimeMillis();
			logger.info("loop2 start : " + new Date());
			int billingThreadNum = report.getThreadNum();
			XMscSummaryThread[] billingThread = new XMscSummaryThread[billingThreadNum];

			// step4.0: remove billing boundary list firstly and add MSC
			// boundary list as referred data.
			billingList.removeAll(billingBoundaryList);
			mscList.addAll(mscBoundaryList);
			int billingSize = billingList.size();

			// step4.1: create sub-threads to process.
			for (int i = 0; i < billingThreadNum; i++) {
				List<RaBilling> subList = null;
				if (i < billingThreadNum - 1) {
					subList = billingList.subList(billingSize / billingThreadNum * i, billingSize / billingThreadNum * (i + 1));
				} else {
					subList = billingList.subList(billingSize / billingThreadNum * i, billingSize);
				}

				Map<String, Object> objsMap = new HashMap<String, Object>();
				// XMscSummaryDao dao2 = new XMscSummaryDao(report);
				objsMap.put(Constants.KEY_BILLING_LIST, subList);
				objsMap.put(Constants.KEY_MSC_MAP, mscHashMap);
				objsMap.put(Constants.KEY_MSC_LIST, mscList);
				objsMap.put(Constants.KEY_DAO, dao);
				objsMap.put(Constants.KEY_OPERATOR, operator);
				objsMap.put(Constants.KEY_DETAILTYPE, detailTypes[1]);
				objsMap.put(Constants.KEY_REPORTTYPE, report.getType());
				objsMap.put(Constants.KEY_REPORT, report);
				objsMap.put(Constants.KEY_DATE, yes);
				objsMap.put(Constants.KEY_FILENAME_HASHMAP, fileNameHashMap);

				billingThread[i] = new XMscSummaryThread(objsMap);
				billingThread[i].start();
			}

			// step4.2: confirm all sub-threads is not alive.
			for (int i = 0; i < billingThreadNum; i++) {
				while (true) {
					if (billingThread[i].isAlive())
						try {
							Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						break;
				}
			}

			endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step5.1: get files of MSC
			Set<String> mscFileSet = new HashSet<String>();
			// pending.

			// step5.2: get files of Billing.
			Set<String> billingFileSet = new HashSet<String>();
			List<RaFileName> fdetailList = new ArrayList<RaFileName>();
			for (RaBilling billing : billingList) {
				RaFileName detail = new RaFileName();
				String localTimezoneStr = Constants.coreCfg.getLocalTimezone();

				if (!Tools.checkNullorSpace(billing.getFileName()) && !billingFileSet.contains(billing.getFileName())) {
					detail.setReportType(new Integer(report.getType()));
					detail.setCallTime(Tools.getLocalCallTime("GMT", billing.getCallTime(), localTimezoneStr));
					detail.setRespectiveType(Constants.RESPECTIVE_TYPE_B_PARTY);
					detail.setFileName(billing.getFileName());
					detail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));

					fdetailList.add(detail);
					billingFileSet.add(billing.getFileName());
				}
			}
			dao.batchInsertRaFileNameTable(fdetailList, operator);

			// step5.3: insert ra_feed.
			FeedService feedService = new FeedService();
			try {
				feedService.insertRAFeed(yes, 1, report);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// step6: get all kinds of count.
			long mocCallCount = mscSize;
			long mocDurationCount = 0;
			long mocCallCountNotIn = 0;
			long mocDurationCountNotIn = 0;
			long inCallCount = billingList.size();
			long inDurationCount = 0;
			long inCallCountNotMoc = 0;
			long inDurationCountNotMoc = 0;
			long mscFileCount = 0;
			long billingFileCount = 0;
			for (CallsNoDup moc : mscList) {
				mocDurationCount += moc.getDuration();
			}
			for (RaBilling in : billingList) {
				inDurationCount += in.getDuration();
			}

			long mocCounts[] = dao.getXOnlyCallCount(yes, operator, detailTypes[0]);
			long inCounts[] = dao.getXOnlyCallCount(yes, operator, detailTypes[1]);
			mocCallCountNotIn = mocCounts[0];
			mocDurationCountNotIn = mocCounts[1];
			inCallCountNotMoc = inCounts[0];
			inDurationCountNotMoc = inCounts[1];

			FeedDao feedDao = new FeedDao();
			String alias = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
			mscFileCount = feedDao.getFeedCount(yes, report.getMscFeedType(), alias);
			billingFileCount = feedDao.getFeedCount(yes, report.getBillingFeedType(), alias);
			logger.info("The files count of mscFileCount/billingFileCount: " + mscFileCount + "/" + billingFileCount);

			result.put("moc_call_count", mocCallCount + "");
			result.put("moc_duration", mocDurationCount + "");
			result.put("in_call_count", inCallCount + "");
			result.put("in_duration", inDurationCount + "");

			result.put("moc_call_count_without_in", mocCallCountNotIn + "");
			result.put("moc_duration_without_in", mocDurationCountNotIn + "");
			result.put("in_call_count_without_moc", inCallCountNotMoc + "");
			result.put("in_duration_without_moc", inDurationCountNotMoc + "");
			result.put("report_type", report.getType());

			result.put("msc_file_count", mscFileCount + "");
			result.put("billing_file_count", billingFileCount + "");

			result.put("transition_status", Constants.coreCfg.getTransitionStatus() != null ? Constants.coreCfg.getTransitionStatus().getUpdatingFilename()
					: "1");

			// step7: insert count values.
			if ("0".equals(result.get("moc_call_count").trim()) && "0".equals(result.get("moc_duration").trim())
					&& "0".equals(result.get("in_call_count").trim()) && "0".equals(result.get("in_duration").trim())
					&& "0".equals(result.get("moc_call_count_without_in").trim()) && "0".equals(result.get("moc_duration_without_in").trim())
					&& "0".equals(result.get("in_call_count_without_moc").trim()) && "0".equals(result.get("in_duration_without_moc").trim())) {
				logger.info("all count are 0, so no inserting/updating");
			} else if (dao.checkIfExist(yes, operator, report.getType()))
				dao.updateCountTable(result, operator);
			else {
				// dao.insertCountTable(result, operator);
			}

			// step8: update status for exporter and wait for exporting.
			dao.updateStatus4Exporter(yes, operator);

			// step10: day-1, reverse order.
			c.add(Calendar.DAY_OF_MONTH, -1);

			long endTime4Day = System.currentTimeMillis();
			logger.info(yes + " Cost Time: " + (endTime4Day - startTime4Day) + " ms\n");

		}
		flag = true;
		return flag;
	}
}
