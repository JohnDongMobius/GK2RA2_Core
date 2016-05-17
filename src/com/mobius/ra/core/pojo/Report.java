package com.mobius.ra.core.pojo;

import java.util.Map;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class Report {
	private String name;
	private String type;
	private int mscFeedType;
	private int billingFeedType;
	private int active;
	private int threadNum;
	private int redoSwitch;
	private int shortActive;
	private int mscFilenameUpdate;
	private int startDateBeforeCurrent;
	private int startDateBeforeRedo;
	private int executeDaysDefault;

	private int subscriberType;
	private int exchangeMsisdn;
	private Map<String, String> beans;
	private Map<String, String> sqls;
	private String mscPpFlg;
	private String mscPpRedoFlg;
	private String billingPpFlg;
	private String billingPpRedoFlg;
	
	private int[] feedType;
	private int[] furtherCallType;
	private String isRecon; 
	
	
	

	public int[] getFeedType() {
		return feedType;
	}

	public void setFeedType(int[] feedType) {
		this.feedType = feedType;
	}

	

	public int[] getFurtherCallType() {
		return furtherCallType;
	}

	public void setFurtherCallType(int[] furtherCallType) {
		this.furtherCallType = furtherCallType;
	}

	public String getIsRecon() {
		return isRecon;
	}

	public void setIsRecon(String isRecon) {
		this.isRecon = isRecon;
	}

	public int getMscFilenameUpdate() {
		return mscFilenameUpdate;
	}

	public void setMscFilenameUpdate(int mscFilenameUpdate) {
		this.mscFilenameUpdate = mscFilenameUpdate;
	}

	
	public int getShortActive() {
		return shortActive;
	}

	public void setShortActive(int shortActive) {
		this.shortActive = shortActive;
	}

	public int getMscFeedType() {
		return mscFeedType;
	}

	public void setMscFeedType(int mscFeedType) {
		this.mscFeedType = mscFeedType;
	}

	public int getBillingFeedType() {
		return billingFeedType;
	}

	public void setBillingFeedType(int billingFeedType) {
		this.billingFeedType = billingFeedType;
	}

	public String getMscPpRedoFlg() {
		return mscPpRedoFlg;
	}

	public void setMscPpRedoFlg(String mscPpRedoFlg) {
		this.mscPpRedoFlg = mscPpRedoFlg;
	}

	public String getBillingPpRedoFlg() {
		return billingPpRedoFlg;
	}

	public void setBillingPpRedoFlg(String billingPpRedoFlg) {
		this.billingPpRedoFlg = billingPpRedoFlg;
	}

	public String getMscPpFlg() {
		return mscPpFlg;
	}

	public void setMscPpFlg(String mscPpFlg) {
		this.mscPpFlg = mscPpFlg;
	}

	public String getBillingPpFlg() {
		return billingPpFlg;
	}

	public void setBillingPpFlg(String billingPpFlg) {
		this.billingPpFlg = billingPpFlg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getBeans() {
		return beans;
	}

	public void setBeans(Map<String, String> beans) {
		this.beans = beans;
	}

	public int getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(int subscriberType) {
		this.subscriberType = subscriberType;
	}

	public int getExchangeMsisdn() {
		return exchangeMsisdn;
	}

	public void setExchangeMsisdn(int exchangeMsisdn) {
		this.exchangeMsisdn = exchangeMsisdn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getRedoSwitch() {
		return redoSwitch;
	}

	public void setRedoSwitch(int redoSwitch) {
		this.redoSwitch = redoSwitch;
	}

	public int getStartDateBeforeCurrent() {
		return startDateBeforeCurrent;
	}

	public void setStartDateBeforeCurrent(int startDateBeforeCurrent) {
		this.startDateBeforeCurrent = startDateBeforeCurrent;
	}

	public int getStartDateBeforeRedo() {
		return startDateBeforeRedo;
	}

	public void setStartDateBeforeRedo(int startDateBeforeRedo) {
		this.startDateBeforeRedo = startDateBeforeRedo;
	}

	public int getExecuteDaysDefault() {
		return executeDaysDefault;
	}

	public void setExecuteDaysDefault(int executeDaysDefault) {
		this.executeDaysDefault = executeDaysDefault;
	}

	public Map<String, String> getSqls() {
		return sqls;
	}

	public void setSqls(Map<String, String> sqls) {
		this.sqls = sqls;
	}
}
