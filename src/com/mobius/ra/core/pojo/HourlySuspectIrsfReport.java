package com.mobius.ra.core.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class HourlySuspectIrsfReport implements Serializable {
	
	private static final long serialVersionUID = -2640044337646294928L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private long id;
	private String reportType;
	private String trafficDate;
	private String trafficHour;
	private long oMsisdn;
	private String type;
	private String internationalCountry;
	private int internationalCallInCount;
	private long internationalCallInDuration;
	private int aCallCount;
	private double aRatio;
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
	public long getoMsisdn() {
		return oMsisdn;
	}
	public void setoMsisdn(long oMsisdn) {
		this.oMsisdn = oMsisdn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInternationalCountry() {
		return internationalCountry;
	}
	public void setInternationalCountry(String internationalCountry) {
		this.internationalCountry = internationalCountry;
	}
	public int getInternationalCallInCount() {
		return internationalCallInCount;
	}
	public void setInternationalCallInCount(int internationalCallInCount) {
		this.internationalCallInCount = internationalCallInCount;
	}
	public long getInternationalCallInDuration() {
		return internationalCallInDuration;
	}
	public void setInternationalCallInDuration(long internationalCallInDuration) {
		this.internationalCallInDuration = internationalCallInDuration;
	}
	public int getaCallCount() {
		return aCallCount;
	}
	public void setaCallCount(int aCallCount) {
		this.aCallCount = aCallCount;
	}
	public double getaRatio() {
		return aRatio;
	}
	public void setaRatio(double aRatio) {
		this.aRatio = aRatio;
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
