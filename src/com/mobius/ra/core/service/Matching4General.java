package com.mobius.ra.core.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class Matching4General extends MatchingAbstract {
	private static Logger logger = Logger.getLogger("RA-Billing");

	public Matching4General() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void matchOrder(Map<String, Object> objsMap) throws SQLException {
		List<CallsNoDup> mscList = (List<CallsNoDup>) objsMap.get(Constants.KEY_MSC_LIST);
		HashMap<String, RaBilling> billingHashMap = (HashMap<String, RaBilling>) objsMap.get(Constants.KEY_BILLING_MAP);
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);

		long mscCallCountNotBilling = 0;
		long mscDurationCountNotBilling = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		java.util.Calendar calPlus = java.util.Calendar.getInstance();
		java.util.Calendar calMinus = java.util.Calendar.getInstance();

		for (CallsNoDup msc : mscList) {
			boolean compareResult1 = false;
			try {
				date = sdf.parse(msc.getCallTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			calPlus.setTime(date);
			calMinus.setTime(date);

			for (int i = 0; i <= Constants.coreCfg.getCtDeviation(); i++) {
				if (billingHashMap.get(Tools.changePrefix(msc.getCallingNum()) + Tools.changePrefix(msc.getCalledNum())
						+ sdf.format(calPlus.getTime())) != null
						|| ("1".equals(report.getExchangeMsisdn() + "") && billingHashMap.get(Tools.changePrefix(msc
								.getCalledNum()) + Tools.changePrefix(msc.getCallingNum()) + sdf.format(calPlus.getTime())) != null)) {
					compareResult1 = true;
					break;
				}
				calPlus.add(java.util.Calendar.SECOND, 1);
				if (billingHashMap.get(Tools.changePrefix(msc.getCallingNum()) + Tools.changePrefix(msc.getCalledNum())
						+ sdf.format(calMinus.getTime())) != null
						|| ("1".equals(report.getExchangeMsisdn() + "") && billingHashMap.get(Tools.changePrefix(msc
								.getCalledNum()) + Tools.changePrefix(msc.getCallingNum()) + sdf.format(calMinus.getTime())) != null)) {
					compareResult1 = true;
					break;
				}
				calMinus.add(java.util.Calendar.SECOND, -1);
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
		List<RaBilling> billingList = (List<RaBilling>) objsMap.get(Constants.KEY_BILLING_LIST);
		HashMap<String, CallsNoDup> mscHashMap = (HashMap<String, CallsNoDup>) objsMap.get(Constants.KEY_MSC_MAP);
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);

		long billingCallCountNotMsc = 0;
		long billingDurationCountNotMsc = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		java.util.Calendar calPlus = java.util.Calendar.getInstance();
		java.util.Calendar calMinus = java.util.Calendar.getInstance();

		for (RaBilling billing : billingList) {
			boolean compareResult2 = false;

			try {
				date = sdf.parse(billing.getCallTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			calPlus.setTime(date);
			calMinus.setTime(date);

			for (int i = 0; i <= Constants.coreCfg.getCtDeviation(); i++) {
				if (mscHashMap.get(Tools.changePrefix(billing.getCallingNum()) + Tools.changePrefix(billing.getCalledNum())
						+ sdf.format(calPlus.getTime())) != null
						|| ("1".equals(report.getExchangeMsisdn() + "") && mscHashMap.get(Tools.changePrefix(billing
								.getCalledNum()) + Tools.changePrefix(billing.getCallingNum()) + sdf.format(calPlus.getTime())) != null)) {
					compareResult2 = true;
					break;
				}
				calPlus.add(java.util.Calendar.SECOND, 1);
				if (mscHashMap.get(Tools.changePrefix(billing.getCallingNum()) + Tools.changePrefix(billing.getCalledNum())
						+ sdf.format(calMinus.getTime())) != null
						|| ("1".equals(report.getExchangeMsisdn() + "") && mscHashMap.get(Tools.changePrefix(billing
								.getCalledNum()) + Tools.changePrefix(billing.getCallingNum()) + sdf.format(calMinus.getTime())) != null)) {
					compareResult2 = true;
					break;
				}
				calMinus.add(java.util.Calendar.SECOND, -1);
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
}
