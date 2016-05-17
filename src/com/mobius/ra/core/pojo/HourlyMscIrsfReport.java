package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class HourlyMscIrsfReport implements Serializable {
	
	private static final long serialVersionUID = -5637747001882555372L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private String trafficDate;
	private String trafficHour;
	private long sMsisdn;
	private long sImsi;
	private String tapcode;
	private int numberOfCalls;
	private long totalDuration;
	private int roleId;

	public String getTrafficDate() {
		return trafficDate;
	}
	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}
	public String getTrafficHour() {
		return trafficHour;
	}
	public void setTrafficHour(String trafficHour) {
		this.trafficHour = trafficHour;
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
	public String getTapcode() {
		return tapcode;
	}
	public void setTapcode(String tapcode) {
		this.tapcode = tapcode;
	}
	public int getNumberOfCalls() {
		return numberOfCalls;
	}
	public void setNumberOfCalls(int numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}
	public long getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}