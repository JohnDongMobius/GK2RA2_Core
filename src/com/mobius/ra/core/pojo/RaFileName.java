package com.mobius.ra.core.pojo;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class RaFileName {
	protected String callTime;
	protected String fileName;
	protected int reportType;
	protected int respectiveType;
	protected String insertTime;

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public int getRespectiveType() {
		return respectiveType;
	}

	public void setRespectiveType(int respectiveType) {
		this.respectiveType = respectiveType;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

}
