package com.mobius.ra.core.pojo;

public class RaTrafficSummary {
	private long amountCount;
	private String callType;
	private long duration;
	private long durationCount;
	private int feedType;

	private long recordCount;

	private int reportType;
	private String trafficDate;

	public RaTrafficSummary() {
	}

	public RaTrafficSummary(String callType, long recordCount, long durationCount, long amountCount, String trafficDate, int reportType, int feedType) {
		this.callType = callType;
		this.recordCount = recordCount;
		this.trafficDate = trafficDate;
		this.durationCount = durationCount;
		this.amountCount = amountCount;
	}

	public RaTrafficSummary(String callType, long recordCount, long duration, String trafficDate) {
		this.callType = callType;
		this.recordCount = recordCount;
		this.duration = duration;
		this.trafficDate = trafficDate;
	}

	public long getAmountCount() {
		return amountCount;
	}

	public String getCallType() {
		return callType;
	}

	public long getDuration() {
		return duration;
	}

	public long getDurationCount() {
		return durationCount;
	}

	public int getFeedType() {
		return feedType;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public int getReportType() {
		return reportType;
	}

	public String getTrafficDate() {
		return trafficDate;
	}

	public void setAmountCount(long amountCount) {
		this.amountCount = amountCount;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setDurationCount(long durationCount) {
		this.durationCount = durationCount;
	}

	public void setFeedType(int feedType) {
		this.feedType = feedType;
	}

	public void setRecordCount(long mocOnNetCount) {
		this.recordCount = mocOnNetCount;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}
}
