package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class MscIrsfDetail implements Serializable {
	
	private static final long serialVersionUID = 5597962964071389497L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private String trafficDate;
	private String callTime;
	private long sMsisdn;
	private long sImsi;
	private int callType;
	private long oMsisdn;
	private long duration;
	private int ruleId;

	public String getTrafficDate() {
		return trafficDate;
	}
	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public long getsMsisdn() {
		return sMsisdn;
	}
	public void setsMsisdn(long sMsisdn) {
		this.sMsisdn = sMsisdn;
	}
	public long getsImsi() {
		return sImsi;
	}
	public void setsImsi(long sImsi) {
		this.sImsi = sImsi;
	}
	public int getCallType() {
		return callType;
	}
	public void setCallType(int callType) {
		this.callType = callType;
	}
	public long getoMsisdn() {
		return oMsisdn;
	}
	public void setoMsisdn(long oMsisdn) {
		this.oMsisdn = oMsisdn;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
}