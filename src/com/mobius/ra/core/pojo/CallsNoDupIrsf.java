package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class CallsNoDupIrsf implements Serializable {
	
	private static final long serialVersionUID = -6703049472019041040L;

	private String callTime;
	private long duration;
	private int callType;
	private long sImsi;
	private long sMsisdn;
	private long oMsisdn;
	
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public int getCallType() {
		return callType;
	}
	public void setCallType(int callType) {
		this.callType = callType;
	}
	public long getsImsi() {
		return sImsi;
	}
	public void setsImsi(long sImsi) {
		this.sImsi = sImsi;
	}
	public long getsMsisdn() {
		return sMsisdn;
	}
	public void setsMsisdn(long sMsisdn) {
		this.sMsisdn = sMsisdn;
	}
	public long getoMsisdn() {
		return oMsisdn;
	}
	public void setoMsisdn(long oMsisdn) {
		this.oMsisdn = oMsisdn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}