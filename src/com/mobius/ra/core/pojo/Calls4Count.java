package com.mobius.ra.core.pojo;

public class Calls4Count {
	protected String calledNum;
	protected String callingNum;
	protected String callType;
	protected long duration;

	public String getCalledNum() {
		return calledNum;
	}

	public String getCallingNum() {
		return callingNum;
	}

	public String getCallType() {
		return callType;
	}

	public long getDuration() {
		return duration;
	}

	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}

	public void setCallingNum(String callingNum) {
		this.callingNum = callingNum;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
