package com.mobius.ra.core.pojo;

public class FileReport {
	private int activeFlg;
	private int compareDeviationDay;
	private String hostname;
	private String matchPattern;
	private String password;
	private String referSql;
	private String uploadedTxtPath;
	private String username;
	public int getActiveFlg() {
		return activeFlg;
	}
	
	public int getCompareDeviationDay() {
		return compareDeviationDay;
	}
	public String getHostname() {
		return hostname;
	}
	public String getMatchPattern() {
		return matchPattern;
	}
	public String getPassword() {
		return password;
	}
	public String getReferSql() {
		return referSql;
	}
	public String getUploadedTxtPath() {
		return uploadedTxtPath;
	}
	public String getUsername() {
		return username;
	}
	
	public void setActiveFlg(int activeFlg) {
		this.activeFlg = activeFlg;
	}

	public void setCompareDeviationDay(int compareDeviationDay) {
		this.compareDeviationDay = compareDeviationDay;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public void setMatchPattern(String matchPattern) {
		this.matchPattern = matchPattern;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setReferSql(String referSql) {
		this.referSql = referSql;
	}
	public void setUploadedTxtPath(String uploadedTxtPath) {
		this.uploadedTxtPath = uploadedTxtPath;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
