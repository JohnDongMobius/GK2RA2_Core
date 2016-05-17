package com.mobius.ra.core.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class HourlyHotIrsfReport implements Serializable {
	
	private static final long serialVersionUID = -5637747001882555372L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private long id;
	private String reportType;
	private String type;
	private String trafficDate;
	private String trafficHour;
	private long sMsisdn;
	private String msisdnListGroup;
	private int irsfCallCount;
	private long irsfDuration;
	private int callOutCount;
	private long callOutDuration;
	private int bCallCount;
	private double bRatio;
	private List<CallsIgw> callsIgwList;
	private String irsfNumberRange;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public String getMsisdnListGroup() {
		return msisdnListGroup;
	}
	public void setMsisdnListGroup(String msisdnListGroup) {
		this.msisdnListGroup = msisdnListGroup;
	}
	public int getIrsfCallCount() {
		return irsfCallCount;
	}
	public void setIrsfCallCount(int irsfCallCount) {
		this.irsfCallCount = irsfCallCount;
	}
	public long getIrsfDuration() {
		return irsfDuration;
	}
	public void setIrsfDuration(long irsfDuration) {
		this.irsfDuration = irsfDuration;
	}
	public int getCallOutCount() {
		return callOutCount;
	}
	public void setCallOutCount(int callOutCount) {
		this.callOutCount = callOutCount;
	}
	public long getCallOutDuration() {
		return callOutDuration;
	}
	public void setCallOutDuration(long callOutDuration) {
		this.callOutDuration = callOutDuration;
	}
	public int getbCallCount() {
		return bCallCount;
	}
	public void setbCallCount(int bCallCount) {
		this.bCallCount = bCallCount;
	}
	public double getbRatio() {
		return bRatio;
	}
	public void setbRatio(double bRatio) {
		this.bRatio = bRatio;
	}
	public List<CallsIgw> getCallsIgwList() {
		return callsIgwList;
	}
	public void setCallsIgwList(List<CallsIgw> callsIgwList) {
		this.callsIgwList = callsIgwList;
	}
	public String getIrsfNumberRange() {
		return irsfNumberRange;
	}
	public void setIrsfNumberRange(String irsfNumberRange) {
		this.irsfNumberRange = irsfNumberRange;
	}
}
