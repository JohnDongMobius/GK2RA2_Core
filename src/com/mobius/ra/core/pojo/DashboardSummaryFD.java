package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class DashboardSummaryFD implements Serializable {
	
	private static final long serialVersionUID = 3976093748786995932L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private String trafficDate;
	private String reportName;
	private int recordCount;
	private String createTime;
	private String updateTime;

	public String getTrafficDate() {
		return trafficDate;
	}
	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
