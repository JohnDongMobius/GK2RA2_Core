package com.mobius.ra.core.pojo;

public class InboundRoamingSummary {
	private long amountCount;
	private long callCount;
	private long callCountSms;
	private long callCountVoice;
	private long durationCount;
	private String hour;
	private String imsiPrefix;
	private long maxCallId;
	private long minCallId;
	private float rate;
	private int reportType;
	private String tapcode;
	private String trafficDate;
	private String trafficDirection;
	

	public InboundRoamingSummary() {
	}

	public long getAmountCount() {
		return amountCount;
	}

	public long getCallCount() {
		return callCount;
	}

	public long getCallCountSms() {
		return callCountSms;
	}

	public long getCallCountVoice() {
		return callCountVoice;
	}

	public long getDurationCount() {
		return durationCount;
	}

	public String getHour() {
		return hour;
	}

	public String getImsiPrefix() {
		return imsiPrefix;
	}

	public long getMaxCallId() {
		return maxCallId;
	}
	

	public long getMinCallId() {
		return minCallId;
	}

	public float getRate() {
		return rate;
	}

	public int getReportType() {
		return reportType;
	}

	public String getTapcode() {
		return tapcode;
	}

	public String getTrafficDate() {
		return trafficDate;
	}


	public String getTrafficDirection() {
		return trafficDirection;
	}

	public void setAmountCount(long amountCount) {
		this.amountCount = amountCount;
	}

	public void setCallCount(long callCount) {
		this.callCount = callCount;
	}

	public void setCallCountSms(long callCountSms) {
		this.callCountSms = callCountSms;
	}

	public void setCallCountVoice(long callCountVoice) {
		this.callCountVoice = callCountVoice;
	}

	public void setDurationCount(long durationCount) {
		this.durationCount = durationCount;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public void setImsiPrefix(String imsiPrefix) {
		this.imsiPrefix = imsiPrefix;
	}

	public void setMaxCallId(long maxCallId) {
		this.maxCallId = maxCallId;
	}

	public void setMinCallId(long minCallId) {
		this.minCallId = minCallId;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}


	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public void setTapcode(String tapcode) {
		this.tapcode = tapcode;
	}

	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}

	public void setTrafficDirection(String trafficDirection) {
		this.trafficDirection = trafficDirection;
	}


}
