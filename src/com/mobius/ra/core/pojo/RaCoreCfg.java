package com.mobius.ra.core.pojo;

import java.util.List;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class RaCoreCfg {
	private String countryPrefix;
	
	private int ctDeviation;
	private int drDeviation;
	private List<FeedInfo> feedInfoList;
	private String generateOrder;
	private String isProductMode;
	
	
	private String localTimezone;
	private String msisdnPrefix;
	private String[] numberingPlan;
	private String noDataComparison;

	private String operatorCode;

	private List<Report> reports;

	private int sleepDurationAfterOneLoop;

	private int sleepDurationOfPaid;

	private int sleepDurationOfSubthread;

	private TransitionStatus transitionStatus;

	public String getCountryPrefix() {
		return countryPrefix;
	}
	public int getCtDeviation() {
		return ctDeviation;
	}

	public int getDrDeviation() {
		return drDeviation;
	}

	public List<FeedInfo> getFeedInfoList() {
		return feedInfoList;
	}

	public String getGenerateOrder() {
		return generateOrder;
	}

	public String getIsProductMode() {
		return isProductMode;
	}

	public String getLocalTimezone() {
		return localTimezone;
	}

	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}

	public String[] getNumberingPlan() {
		return numberingPlan;
	}

	public String getNoDataComparison() {
		return noDataComparison;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public List<Report> getReports() {
		return reports;
	}

	public int getSleepDurationAfterOneLoop() {
		return sleepDurationAfterOneLoop;
	}

	public int getSleepDurationOfPaid() {
		return sleepDurationOfPaid;
	}

	public int getSleepDurationOfSubthread() {
		return sleepDurationOfSubthread;
	}

	public TransitionStatus getTransitionStatus() {
		return transitionStatus;
	}
	public void setCountryPrefix(String countryPrefix) {
		this.countryPrefix = countryPrefix;
	}
	public void setCtDeviation(int ctDeviation) {
		this.ctDeviation = ctDeviation;
	}

	public void setDrDeviation(int drDeviation) {
		this.drDeviation = drDeviation;
	}

	public void setFeedInfoList(List<FeedInfo> feedInfoList) {
		this.feedInfoList = feedInfoList;
	}

	public void setGenerateOrder(String generateOrder) {
		this.generateOrder = generateOrder;
	}

	public void setIsProductMode(String isProductMode) {
		this.isProductMode = isProductMode;
	}

	public void setLocalTimezone(String localTimezone) {
		this.localTimezone = localTimezone;
	}

	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}

	public void setNoDataComparison(String noDataComparison) {
		this.noDataComparison = noDataComparison;
	}
	public void setNumberingPlan(String[] numberingPlan) {
		this.numberingPlan = numberingPlan;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public void setSleepDurationAfterOneLoop(int sleepDurationAfterOneLoop) {
		this.sleepDurationAfterOneLoop = sleepDurationAfterOneLoop;
	}

	public void setSleepDurationOfPaid(int sleepDurationOfPaid) {
		this.sleepDurationOfPaid = sleepDurationOfPaid;
	}

	public void setSleepDurationOfSubthread(int sleepDurationOfSubthread) {
		this.sleepDurationOfSubthread = sleepDurationOfSubthread;
	}

	public void setTransitionStatus(TransitionStatus transitionStatus) {
		this.transitionStatus = transitionStatus;
	}

}
