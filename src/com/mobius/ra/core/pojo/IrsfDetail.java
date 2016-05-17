package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class IrsfDetail implements Serializable {
	
	private static final long serialVersionUID = 5597962964071389497L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private long id;
	private int callType;
	private String type;
	private long sMsisdn;
	private long oMsisdn;
	private long duration;
	private String callTime;
	private long swId;
	private long sImsi;
	private long sImei;
	private int sCi;
	private int sLac;
	private long trunkIn;
	private long trunkOut;
	private long termCause;
	private long termReason;
	private int ssCode;
	private int chargeIndicator;
	private int detailType;
	private String reportType;
	private String insertTime;
	private String fileName;
	private long volumeDownload;
	private long volumeUpload;
	private long chargeIdentifier;
	private String sgsnAddress;
	private String ggsnAddress;
	private String pdpAddress;
	private int shortNum;
	private int groupNum;
	private String irsfNumberRange;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCallType() {
		return callType;
	}
	public void setCallType(int callType) {
		this.callType = callType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public long getSwId() {
		return swId;
	}
	public void setSwId(long swId) {
		this.swId = swId;
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
	public int getDetailType() {
		return detailType;
	}
	public void setDetailType(int detailType) {
		this.detailType = detailType;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
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
	public int getShortNum() {
		return shortNum;
	}
	public void setShortNum(int shortNum) {
		this.shortNum = shortNum;
	}
	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	public String getIrsfNumberRange() {
		return irsfNumberRange;
	}
	public void setIrsfNumberRange(String irsfNumberRange) {
		this.irsfNumberRange = irsfNumberRange;
	}
}
