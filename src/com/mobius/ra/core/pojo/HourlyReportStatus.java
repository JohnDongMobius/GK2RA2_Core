package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class HourlyReportStatus implements Serializable {
	
	private static final long serialVersionUID = 4606861005870705757L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private long id;
	private String date;
	private int hour;
	private String reportType;
	private int status;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
