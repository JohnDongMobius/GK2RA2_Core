package com.mobius.ra.core.pojo;

public class CountDuration {
	private String callType = null;
	private long count = 0;
	private long duration = 0;

	public CountDuration(String callType) {
		this.callType = callType;
	}

	public String getCallType() {
		return callType;
	}

	public long getCount() {
		return count;
	}

	public long getDuration() {
		return duration;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
