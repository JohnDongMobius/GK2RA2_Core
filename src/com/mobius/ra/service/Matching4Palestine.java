package com.mobius.ra.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.XMscSummaryDao;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.service.MatchingAbstract;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class Matching4Palestine extends MatchingAbstract {
	private static Logger logger = Logger.getLogger("RA-Billing-Palestine");
	private List<String> shortNums = null;

	public Matching4Palestine() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void matchOrder(Map<String, Object> objsMap) throws SQLException {
		shortNums = getShortNums();
		List<CallsNoDup> mscList = (List<CallsNoDup>) objsMap.get(Constants.KEY_MSC_LIST);
		HashMap<String, RaBilling> billingHashMap = (HashMap<String, RaBilling>) objsMap.get(Constants.KEY_BILLING_MAP);

		billingHashMap = removeMapShortNums(billingHashMap);

		Report report = (Report) objsMap.get(Constants.KEY_REPORT);

		long mscCallCountNotBilling = 0;
		long mscDurationCountNotBilling = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		java.util.Calendar calMove = java.util.Calendar.getInstance();

		for (CallsNoDup msc : mscList) {
			boolean compareResult1 = false;
			try {
				date = sdf.parse(msc.getCallTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			calMove.setTime(date);
			calMove.add(java.util.Calendar.SECOND, -Constants.coreCfg.getCtDeviation());

			RaBilling raBilling1 = null;
			RaBilling raBilling2 = null;
			for (int i = -Constants.coreCfg.getCtDeviation(); i <= Constants.coreCfg.getCtDeviation(); i++) {
				raBilling1 = billingHashMap.get(Tools.changePrefix(msc.getCallingNum()) + Tools.changePrefix(msc.getCalledNum())
						+ sdf.format(calMove.getTime()));
				raBilling2 = billingHashMap.get(Tools.changePrefix(msc.getCalledNum()) + Tools.changePrefix(msc.getCallingNum())
						+ sdf.format(calMove.getTime()));

				if (raBilling1 != null || ("1".equals(report.getExchangeMsisdn() + "") && raBilling2 != null)) {
					compareResult1 = true;
					break;
				}
				calMove.add(java.util.Calendar.SECOND, 1);
			}

			if (!compareResult1) {
				mscCallCountNotBilling++;
				mscDurationCountNotBilling += msc.getDuration();
				// add into RaDetail object.
				super.insertRaDetail4Msc(msc, objsMap);
			}
		}

		// setFileName(report);
		setFileName(objsMap);
		// insert into ra_detail table via batch.
		super.batchInsetRaDetailTable(objsMap);
		logger.info("new:" + mscList.size() + "," + mscCallCountNotBilling + "," + mscDurationCountNotBilling);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void matchReverse(Map<String, Object> objsMap) throws SQLException {
		shortNums = getShortNums();
		List<RaBilling> billingList = (List<RaBilling>) objsMap.get(Constants.KEY_BILLING_LIST);

		billingList = removeListShortNums(billingList);
		HashMap<String, CallsNoDup> mscHashMap = (HashMap<String, CallsNoDup>) objsMap.get(Constants.KEY_MSC_MAP);
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);

		long billingCallCountNotMsc = 0;
		long billingDurationCountNotMsc = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		java.util.Calendar callMove = java.util.Calendar.getInstance();

		for (RaBilling billing : billingList) {
			boolean compareResult2 = false;

			try {
				date = sdf.parse(billing.getCallTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			callMove.setTime(date);
			callMove.add(java.util.Calendar.SECOND, -Constants.coreCfg.getCtDeviation());

			for (int i = -Constants.coreCfg.getCtDeviation(); i <= Constants.coreCfg.getCtDeviation(); i++) {
				if (mscHashMap.get(Tools.changePrefix(billing.getCallingNum()) + Tools.changePrefix(billing.getCalledNum())
						+ sdf.format(callMove.getTime())) != null
						|| ("1".equals(report.getExchangeMsisdn() + "") && mscHashMap.get(Tools.changePrefix(billing.getCalledNum())
								+ Tools.changePrefix(billing.getCallingNum()) + sdf.format(callMove.getTime())) != null)) {
					compareResult2 = true;
					break;
				}
				callMove.add(java.util.Calendar.SECOND, 1);
			}

			if (!compareResult2) {
				billingCallCountNotMsc++;
				billingDurationCountNotMsc += billing.getDuration();

				// add into RaDetail object.
				super.insertRaDetail4Billing(billing, objsMap);
			}
		}

		// setFileName(report);
		// setFileName(objsMap); no need for IN only
		// insert into ra_detail table via batch.
		super.batchInsetRaDetailTable(objsMap);
		logger.info("new:" + billingList.size() + "," + billingCallCountNotMsc + "," + billingDurationCountNotMsc);
	}

	private List<String> getShortNums() {
		XMscSummaryDao xMscSummaryDao = new XMscSummaryDao(null);
		List<String> shortNums = null;
		try {
			shortNums = xMscSummaryDao.getShorNums(Constants.DB_NAME_FRAUD + "425_6");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shortNums;
	}

	private List<RaBilling> removeListShortNums(List<RaBilling> billingList) {
		List<RaBilling> billingList2 = new ArrayList<RaBilling>();
		for (RaBilling raBilling : billingList) {
			if (!isShortNum(raBilling.getCalledNum())) {
				billingList2.add(raBilling);
			}
		}
		return billingList2;
	}

	private HashMap<String, RaBilling> removeMapShortNums(HashMap<String, RaBilling> billingHashMap) {
		HashMap<String, RaBilling> billingHashMap2 = new HashMap<String, RaBilling>();
		Iterator iter = billingHashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			RaBilling raBilling = (RaBilling) entry.getValue();
			if (!isShortNum(raBilling.getCalledNum())) {
				billingHashMap2.put(key, raBilling);
			}
		}
		return billingHashMap2;
	}

	private boolean isShortNum(String o) {
		boolean isShortNum = false;
		for (String shortNum : this.shortNums) {
			if (o.equals(shortNum)) {
				isShortNum = true;
			}
		}
		return isShortNum;
	}
}