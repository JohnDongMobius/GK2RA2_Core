package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class CallsIgw implements Serializable {
	
	private static final long serialVersionUID = -5421868248296685093L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private long callId;
	private long oCallId;
	private String callTime;
	private int duration;
	private int callType;
	private int swId;
	private long sMsisdn;
	private long oMsisdn;
	private long sImei;
	private long sImsi;
	private int sCi;
	private int sLac;
	private int trunkIn;
	private int trunkOut;
	private int termCause;
	private int termReason;
	private int ssCode;
	private int chargeIndicator;

	public long getCallId() {
		return callId;
	}
	public void setCallId(long callId) {
		this.callId = callId;
	}
	public long getoCallId() {
		return oCallId;
	}
	public void setoCallId(long oCallId) {
		this.oCallId = oCallId;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getCallType() {
		return callType;
	}
	public void setCallType(int callType) {
		this.callType = callType;
	}
	public int getSwId() {
		return swId;
	}
	public void setSwId(int swId) {
		this.swId = swId;
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
	public long getsImei() {
		return sImei;
	}
	public void setsImei(long sImei) {
		this.sImei = sImei;
	}
	public long getsImsi() {
		return sImsi;
	}
	public void setsImsi(long sImsi) {
		this.sImsi = sImsi;
	}
	public int getsCi() {
		return sCi;
	}
	public void setsCi(int sCi) {
		this.sCi = sCi;
	}
	public int getsLac() {
		return sLac;
	}
	public void setsLac(int sLac) {
		this.sLac = sLac;
	}
	public int getTrunkIn() {
		return trunkIn;
	}
	public void setTrunkIn(int trunkIn) {
		this.trunkIn = trunkIn;
	}
	public int getTrunkOut() {
		return trunkOut;
	}
	public void setTrunkOut(int trunkOut) {
		this.trunkOut = trunkOut;
	}
	public int getTermCause() {
		return termCause;
	}
	public void setTermCause(int termCause) {
		this.termCause = termCause;
	}
	public int getTermReason() {
		return termReason;
	}
	public void setTermReason(int termReason) {
		this.termReason = termReason;
	}
	public int getSsCode() {
		return ssCode;
	}
	public void setSsCode(int ssCode) {
		this.ssCode = ssCode;
	}
	public int getChargeIndicator() {
		return chargeIndicator;
	}
	public void setChargeIndicator(int chargeIndicator) {
		this.chargeIndicator = chargeIndicator;
	}
}