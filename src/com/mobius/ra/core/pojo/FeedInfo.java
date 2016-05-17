package com.mobius.ra.core.pojo;

public class FeedInfo {
	private int activeFlg;
	private String database;
	FileReport fileReport;
	private String sql;
	private int type;

	public int getActiveFlg() {
		return activeFlg;
	}

	public String getDatabase() {
		return database;
	}

	public FileReport getFileReport() {
		return fileReport;
	}

	public String getSql() {
		return sql;
	}

	public int getType() {
		return type;
	}

	public void setActiveFlg(int activeFlg) {
		this.activeFlg = activeFlg;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public void setFileReport(FileReport fileReport) {
		this.fileReport = fileReport;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setType(int type) {
		this.type = type;
	}

}
