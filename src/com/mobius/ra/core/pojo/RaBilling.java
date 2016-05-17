package com.mobius.ra.core.pojo;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class RaBilling {
	// call time
	protected String callTime;
	// call duration
	protected long duration;
	// call type
	protected String callType;
	protected long swID;
	protected String callingNum;
	protected String calledNum;
	protected long sImsi;
	protected long sImei;
	protected long sCi;
	protected long sLac;
	protected long trunkIn;
	protected long trunkOut;
	protected long termCause;
	protected long termReason;
	protected long ssCode;
	protected long chargeIndicator;
	protected String fileName;
	protected String insertTime;
	protected long volumeDownload;
	protected long volumeUpload;
	protected long chargeIdentifier;

	protected String sgsnAddress;
	protected String ggsnAddress;
	protected String pdpAddress;
	
	protected int callAmount;
	
	

	public int getCallAmount() {
		return callAmount;
	}

	public void setCallAmount(int callAmount) {
		this.callAmount = callAmount;
	}

	public String getSgsnAddress() {
		return sgsnAddress;
	}

	public void setSgsnAddress(String sgsnAddress) {
		this.sgsnAddress = sgsnAddress;
	}

	public String getGgsnAddress() {
		return ggsnAddress;
	}

	public void setGgsnAddress(String ggsnAddress) {
		this.ggsnAddress = ggsnAddress;
	}

	public String getPdpAddress() {
		return pdpAddress;
	}

	public void setPdpAddress(String pdpAddress) {
		this.pdpAddress = pdpAddress;
	}

	public long getVolumeDownload() {
		return volumeDownload;
	}

	public void setVolumeDownload(long volumeDownload) {
		this.volumeDownload = volumeDownload;
	}

	public long getVolumeUpload() {
		return volumeUpload;
	}

	public void setVolumeUpload(long volumeUpload) {
		this.volumeUpload = volumeUpload;
	}

	public long getChargeIdentifier() {
		return chargeIdentifier;
	}

	public void setChargeIdentifier(long chargeIdentifier) {
		this.chargeIdentifier = chargeIdentifier;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public long getSwID() {
		return swID;
	}

	public void setSwID(long swID) {
		this.swID = swID;
	}

	public String getCallingNum() {
		return callingNum;
	}

	public void setCallingNum(String callingNum) {
		this.callingNum = callingNum;
	}

	public String getCalledNum() {
		return calledNum;
	}

	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}

	public long getsImsi() {
		return sImsi;
	}

	public void setsImsi(long sImsi) {
		this.sImsi = sImsi;
	}

	public long getsImei() {
		return sImei;
	}

	public void setsImei(long sImei) {
		this.sImei = sImei;
	}

	public long getsCi() {
		return sCi;
	}

	public void setsCi(long sCi) {
		this.sCi = sCi;
	}

	public long getsLac() {
		return sLac;
	}

	public void setsLac(long sLac) {
		this.sLac = sLac;
	}

	public long getTrunkIn() {
		return trunkIn;
	}

	public void setTrunkIn(long trunkIn) {
		this.trunkIn = trunkIn;
	}

	public long getTrunkOut() {
		return trunkOut;
	}

	public void setTrunkOut(long trunkOut) {
		this.trunkOut = trunkOut;
	}

	public long getTermCause() {
		return termCause;
	}

	public void setTermCause(long termCause) {
		this.termCause = termCause;
	}

	public long getTermReason() {
		return termReason;
	}

	public void setTermReason(long termReason) {
		this.termReason = termReason;
	}

	public long getSsCode() {
		return ssCode;
	}

	public void setSsCode(long ssCode) {
		this.ssCode = ssCode;
	}

	public long getChargeIndicator() {
		return chargeIndicator;
	}

	public void setChargeIndicator(long chargeIndicator) {
		this.chargeIndicator = chargeIndicator;
	}

}
