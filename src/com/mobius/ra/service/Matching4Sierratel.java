package com.mobius.ra.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.service.MatchingAbstract;

public class Matching4Sierratel extends MatchingAbstract {
	private static Logger logger = Logger.getLogger("RA-Billing-Seirratel");

	public Matching4Sierratel() {

	}

	@Override
	public void matchOrder(Map<String, Object> objsMap) throws SQLException {
		// logger.info("match order operation...");
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);
		@SuppressWarnings("unchecked")
		List<CallsNoDup> mscList = (List<CallsNoDup>) objsMap.get(Constants.KEY_MSC_LIST);

		// set subscriber number
		mscList = super.setSubsriberNum(mscList, report);

		@SuppressWarnings("unchecked")
		List<RaBilling> billingList = (List<RaBilling>) objsMap.get(Constants.KEY_BILLING_LIST);

		// HashMap<String, CallsNoDup> mscHashMap = (HashMap<String,
		// CallsNoDup>)objsMap.get(GkConstant.KEY_MSC_MAP);
		// HashMap<String, RaBilling> billingHashMap = (HashMap<String,
		// RaBilling>)objsMap.get(GkConstant.KEY_BILLING_MAP);

		long mscCallCountNotBilling = 0;
		long mscDurationCountNotBilling = 0;
		RaBilling raBilling = null;
		for (CallsNoDup msc : mscList) {
			boolean compareResult1 = false;
			for (RaBilling billing : billingList) {
				raBilling = billing;
				if (msc.getCallingNum().equals(billing.getCallingNum()) && Tools.checkCallTime(msc.getCallTime(), billing.getCallTime())) {
					compareResult1 = true;
					break;
				}
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
		// logger.info("match reverse operation...");
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);
		List<CallsNoDup> mscList = (List<CallsNoDup>) objsMap.get(Constants.KEY_MSC_LIST);
		List<RaBilling> billingList = (List<RaBilling>) objsMap.get(Constants.KEY_BILLING_LIST);
		// set subscriber number
		mscList = super.setSubsriberNum(mscList, report);
		long billingCallCountNotMsc = 0;
		long billingDurationCountNotMsc = 0;
		CallsNoDup callsNoDup = null;
		for (RaBilling billing : billingList) {
			boolean compareResult2 = false;
			for (CallsNoDup msc : mscList) {
				callsNoDup = msc;
				if (msc.getCallingNum().equals(billing.getCallingNum()) && Tools.checkCallTime(msc.getCallTime(), billing.getCallTime())) {
					compareResult2 = true;
					break;
				}
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
		//setFileName(objsMap); no need for "IN only"
		// insert into ra_detail table via batch.
		super.batchInsetRaDetailTable(objsMap);
		super.batchInsetRaMatchedTable(objsMap);
		logger.info("new:" + billingList.size() + "," + billingCallCountNotMsc + "," + billingDurationCountNotMsc);
	}
}
