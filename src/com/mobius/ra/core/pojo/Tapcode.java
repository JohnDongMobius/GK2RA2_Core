package com.mobius.ra.core.pojo;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class Tapcode {

	private String imsiPrefix;
	private String tapCode	;
	private String countryName;
	private String imsiRange;
	private String msisdnPrefix;
	private String operator;
	private String insertTime;
	private String updateTime;
	
	public String getImsiPrefix() {
		return imsiPrefix;
	}
	public void setImsiPrefix(String imsiPrefix) {
		this.imsiPrefix = imsiPrefix;
	}
	public String getTapCode() {
		return tapCode;
	}
	public void setTapCode(String tapCode) {
		this.tapCode = tapCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getImsiRange() {
		return imsiRange;
	}
	public void setImsiRange(String imsiRange) {
		this.imsiRange = imsiRange;
	}
	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}
	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}