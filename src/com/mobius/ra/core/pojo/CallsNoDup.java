package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 * 
 * @date July 24, 2014
 * Add callId for minCallId and maxCallId
 * 
 */
public class CallsNoDup implements Serializable {
	private static final long serialVersionUID = -9041086949877965758L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	// called number
	protected String calledNum;
	protected long callId;
	// calling number
	protected String callingNum;
	// call time
	protected String callTime;
	// call type
	protected String callType;

	protected long chargeIdentifier;
	protected long chargeIndicator;
	// call duration
	protected long duration;
	protected String fileName;
	protected String ggsnAddress;
	protected long groupNum;
	protected String insertTime;
	protected String pdpAddress;
	protected long sCi;
	protected String sgsnAddress;
	protected int shortNum;

	protected long sImei;
	protected long sImsi;

	protected long sLac;
	protected long ssCode;
	protected long swId;

	protected long termCause;
	protected long termReason;
	protected long trunkIn;

	protected long trunkOut;
	protected long volumeDownload;

	protected long volumeUpload;

	public String getCalledNum() {
		return calledNum;
	}

	public long getCallId() {
		return callId;
	}

	public String getCallingNum() {
		return callingNum;
	}

	public String getCallTime() {
		return callTime;
	}

	public String getCallType() {
		return callType;
	}

	public long getChargeIdentifier() {
		return chargeIdentifier;
	}

	public long getChargeIndicator() {
		return chargeIndicator;
	}

	public long getDuration() {
		return duration;
	}

	public String getFileName() {
		return fileName;
	}

	public String getGgsnAddress() {
		return ggsnAddress;
	}

	public long getGroupNum() {
		return groupNum;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public String getPdpAddress() {
		return pdpAddress;
	}

	public long getsCi() {
		return sCi;
	}

	public String getSgsnAddress() {
		return sgsnAddress;
	}

	public int getShortNum() {
		return shortNum;
	}

	public long getsImei() {
		return sImei;
	}

	public long getsImsi() {
		return sImsi;
	}

	public long getsLac() {
		return sLac;
	}

	public long getSsCode() {
		return ssCode;
	}

	public long getSwId() {
		return swId;
	}

	public long getTermCause() {
		return termCause;
	}

	public long getTermReason() {
		return termReason;
	}

	public long getTrunkIn() {
		return trunkIn;
	}

	public long getTrunkOut() {
		return trunkOut;
	}

	public long getVolumeDownload() {
		return volumeDownload;
	}

	public long getVolumeUpload() {
		return volumeUpload;
	}

	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}

	public void setCallId(long callId) {
		this.callId = callId;
	}

	public void setCallingNum(String callingNum) {
		this.callingNum = callingNum;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public void setChargeIdentifier(long chargeIdentifier) {
		this.chargeIdentifier = chargeIdentifier;
	}

	public void setChargeIndicator(long chargeIndicator) {
		this.chargeIndicator = chargeIndicator;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setGgsnAddress(String ggsnAddress) {
		this.ggsnAddress = ggsnAddress;
	}

	public void setGroupNum(long groupNum) {
		this.groupNum = groupNum;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public void setPdpAddress(String pdpAddress) {
		this.pdpAddress = pdpAddress;
	}

	public void setsCi(long sCi) {
		this.sCi = sCi;
	}

	public void setSgsnAddress(String sgsnAddress) {
		this.sgsnAddress = sgsnAddress;
	}

	public void setShortNum(int shortNum) {
		this.shortNum = shortNum;
	}

	public void setsImei(long sImei) {
		this.sImei = sImei;
	}

	public void setsImsi(long sImsi) {
		this.sImsi = sImsi;
	}

	public void setsLac(long sLac) {
		this.sLac = sLac;
	}

	public void setSsCode(long ssCode) {
		this.ssCode = ssCode;
	}

	public void setSwId(long swId) {
		this.swId = swId;
	}

	public void setTermCause(long termCause) {
		this.termCause = termCause;
	}

	public void setTermReason(long termReason) {
		this.termReason = termReason;
	}

	public void setTrunkIn(long trunkIn) {
		this.trunkIn = trunkIn;
	}

	public void setTrunkOut(long trunkOut) {
		this.trunkOut = trunkOut;
	}

	public void setVolumeDownload(long volumeDownload) {
		this.volumeDownload = volumeDownload;
	}

	public void setVolumeUpload(long volumeUpload) {
		this.volumeUpload = volumeUpload;
	}

}