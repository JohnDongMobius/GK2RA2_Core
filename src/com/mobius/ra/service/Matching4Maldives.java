package com.mobius.ra.service;

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
import com.mobius.ra.core.service.MatchingAbstract;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class Matching4Maldives extends MatchingAbstract {
	private static Logger logger = Logger.getLogger("RA-Billing-Maldives");

	public Matching4Maldives() {

	}

	@SuppressWarnings("unused")
	private boolean checkOmsisdn(String mscMsisdn, String tapMsisdn) {
		boolean result = false;
		// if (!tapMsisdn.startsWith(GkConstant.MSISDN_PREFIX))
		// tapMsisdn = GkConstant.MSISDN_PREFIX + tapMsisdn;
		// if (!tapMsisdn.startsWith(getPropertyValue("MSISDN_PREFIX")))
		// tapMsisdn = getPropertyValue("MSISDN_PREFIX") + tapMsisdn;

		if (Tools.normalizeOmsisdn(mscMsisdn).equals(Tools.normalizeOmsisdn(tapMsisdn)))
			result = true;

		if (tapMsisdn.equals(mscMsisdn))
			result = true;

		if (likeStr(mscMsisdn, tapMsisdn))
			result = true;

		return result;
	}

	private boolean likeStr(String str1, String str2) {
		// for missing prefix.
		if (str1.contains(str2) || str2.contains(str1))
			return true;
		// for wrong prefix.
		else if (str1.substring(1).contains(str2.substring(1)) || str2.substring(1).contains(str1.substring(1)))
			return true;
		else
			return false;
	}

	@Override
	public void matchOrder(Map<String, Object> objsMap) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// logger.info("match order operation...");
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);
		@SuppressWarnings("unchecked")
		List<CallsNoDup> mscList = (List<CallsNoDup>) objsMap.get(Constants.KEY_MSC_LIST);
		@SuppressWarnings("unchecked")
		HashMap<String, RaBilling> billingHashMap = (HashMap<String, RaBilling>) objsMap.get(Constants.KEY_BILLING_MAP);

		if (billingHashMap.get("972508241634" + "2015-09-18 19:22:48") != null || billingHashMap.get("970508241634" + "2015-09-18 19:22:48") != null) {
			System.out.println("matchOrder is not null");
		} else {
			System.out.println("matchOrder is null");
		}

		// set subscriber number
		mscList = super.setSubsriberNum(mscList, report);

		long mscCallCountNotBilling = 0;
		long mscDurationCountNotBilling = 0;

		Date date = null;
		java.util.Calendar calMove = java.util.Calendar.getInstance();

		RaBilling raBilling = null;
		for (CallsNoDup msc : mscList) {
			boolean compareResult1 = false;

			try {
				date = sdf.parse(msc.getCallTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			calMove.setTime(date);
			calMove.add(java.util.Calendar.SECOND, -Constants.coreCfg.getCtDeviation());

			boolean print = false;
			if ((msc.getCallingNum().equals("970508241634") || msc.getCallingNum().equals("972508241634"))
					&& msc.getCallTime().substring(0, 19).equals("2015-09-18 19:22:48")) {
				print = true;
			}

			for (int i = -Constants.coreCfg.getCtDeviation(); i <= Constants.coreCfg.getCtDeviation(); i++) {
				if (print) {
					System.out.println(msc.getCallingNum() + "-matchOrder-" + sdf.format(calMove.getTime()));
				}
				raBilling = billingHashMap.get(Tools.changePrefix(msc.getCallingNum()) + sdf.format(calMove.getTime()));

				if (raBilling != null) {
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
			} else {
				super.insertRaMatched(msc, raBilling, 1);
			}
		}
		setFileName(objsMap);
		// insert into ra_detail table via batch.
		super.batchInsetRaDetailTable(objsMap);
		super.batchInsetRaMatchedTable(objsMap);
		logger.info("new:" + mscList.size() + "," + mscCallCountNotBilling + "," + mscDurationCountNotBilling);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void matchReverse(Map<String, Object> objsMap) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// logger.info("match reverse operation...");
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);
		List<CallsNoDup> mscList = (List<CallsNoDup>) objsMap.get(Constants.KEY_MSC_LIST);
		List<RaBilling> billingList = (List<RaBilling>) objsMap.get(Constants.KEY_BILLING_LIST);
		// set subscriber number
		mscList = super.setSubsriberNum(mscList, report);
		HashMap<String, CallsNoDup> mscHashMap = new HashMap<String, CallsNoDup>();
		for (CallsNoDup callsNoDup : mscList) {
			mscHashMap.put(Tools.changePrefix(callsNoDup.getCallingNum()) + callsNoDup.getCallTime().substring(0, 19), callsNoDup);
		}
		System.out.println("mscmap:" + mscHashMap.size());

		if (mscHashMap.get("972508241634" + "2015-09-18 19:22:48") != null || mscHashMap.get("970508241634" + "2015-09-18 19:22:48") != null) {
			System.out.println("matchReverse is not null");
		} else {
			System.out.println("matchReverse is null");
		}

		long billingCallCountNotMsc = 0;
		long billingDurationCountNotMsc = 0;
		Date date = null;
		java.util.Calendar callMove = java.util.Calendar.getInstance();

		CallsNoDup callsNoDup = null;
		for (RaBilling billing : billingList) {
			boolean compareResult2 = false;

			try {
				date = sdf.parse(billing.getCallTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			callMove.setTime(date);
			callMove.add(java.util.Calendar.SECOND, -Constants.coreCfg.getCtDeviation());

			boolean print = false;
			if ((billing.getCallingNum().equals("970508241634") || billing.getCallingNum().equals("972508241634"))
					&& billing.getCallTime().substring(0, 19).equals("2015-09-18 19:22:48")) {
				print = true;
			}

			for (int i = -Constants.coreCfg.getCtDeviation(); i <= Constants.coreCfg.getCtDeviation(); i++) {

				if (print) {
					System.out.println(billing.getCallingNum() + "-matchReverse-" + sdf.format(callMove.getTime()));
				}

				callsNoDup = mscHashMap.get(Tools.changePrefix(billing.getCallingNum()) + sdf.format(callMove.getTime()));
				if (callsNoDup != null) {
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
			} else {
				super.insertRaMatched(callsNoDup, billing, 0);
			}
		}
		// setFileName(objsMap); no need for IN
		// insert into ra_detail table via batch.
		super.batchInsetRaDetailTable(objsMap);
		super.batchInsetRaMatchedTable(objsMap);
		logger.info("new:" + billingList.size() + "," + billingCallCountNotMsc + "," + billingDurationCountNotMsc);
	}
}