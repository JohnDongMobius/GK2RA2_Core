package com.mobius.ra.core.pojo;

public class RevenueSummary {
	private double amountCount;
	private long call_count;
	private String callType;
	private long durationCount;
	private String FranchiseID;
	private String insertTime;
	private boolean isDeled;
	private String o_msisdn;
	private String oNumCode;
	private String productID;
	private float rate;
	private int reportType;
	private String s_msisdn;
	private String sNumCode;
	private String timePeriod;
	private String trafficDate;

	public double getAmountCount() {
		return amountCount;
	}

	public long getCall_count() {
		return call_count;
	}

	public String getCallType() {
		return callType;
	}

	public long getDurationCount() {
		return durationCount;
	}

	public String getFranchiseID() {
		return FranchiseID;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public String getoNumCode() {
		return oNumCode;
	}

	public String getProductID() {
		return productID;
	}

	public float getRate() {
		return rate;
	}

	public int getReportType() {
		return reportType;
	}

	public String getsNumCode() {
		return sNumCode;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public String getTrafficDate() {
		return trafficDate;
	}

	public boolean isDeled() {
		return isDeled;
	}

	public void setAmountCount(double amountCount) {
		this.amountCount = amountCount;
	}

	public void setCall_count(long call_count) {
		this.call_count = call_count;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public void setDeled(boolean isDeled) {
		this.isDeled = isDeled;
	}

	public void setDurationCount(long durationCount) {
		this.durationCount = durationCount;
	}

	public void setFranchiseID(String franchiseID) {
		FranchiseID = franchiseID;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public void setoNumCode(String oNumCode) {
		this.oNumCode = oNumCode;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public void setsNumCode(String sNumCode) {
		this.sNumCode = sNumCode;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}

	public String getO_msisdn() {
		return o_msisdn;
	}

	public void setO_msisdn(String o_msisdn) {
		this.o_msisdn = o_msisdn;
	}

	public String getS_msisdn() {
		return s_msisdn;
	}

	public void setS_msisdn(String s_msisdn) {
		this.s_msisdn = s_msisdn;
	}
}
