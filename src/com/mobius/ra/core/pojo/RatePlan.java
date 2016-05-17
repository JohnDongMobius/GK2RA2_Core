package com.mobius.ra.core.pojo;

public class RatePlan {
	private int callType;
	private String oNumCode;
	private String productID;
	private float rate;
	private String ratePlanCode;
	private String sNumCode;
	private String timePeriod;
	private int zoneID;

	public int getZoneID() {
		return zoneID;
	}

	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}

	public int getCallType() {
		return callType;
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

	public String getRatePlanCode() {
		return ratePlanCode;
	}

	public String getsNumCode() {
		return sNumCode;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setCallType(int callType) {
		this.callType = callType;
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

	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}

	public void setsNumCode(String sNumCode) {
		this.sNumCode = sNumCode;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
}
