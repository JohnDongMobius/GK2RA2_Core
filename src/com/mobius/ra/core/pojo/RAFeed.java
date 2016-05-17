package com.mobius.ra.core.pojo;

public class RAFeed implements Cloneable{
	private String fileName;
	private long fileSizeBytes;
	private int respectiveType;
	private int type;
	private String unprocessedReason;

	public String getFileName() {
		return fileName;
	}

	public long getFileSizeBytes() {
		return fileSizeBytes;
	}

	public int getRespectiveType() {
		return respectiveType;
	}

	public int getType() {
		return type;
	}

	public String getUnprocessedReason() {
		return unprocessedReason;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSizeBytes(long fileSizeBytes) {
		this.fileSizeBytes = fileSizeBytes;
	}

	public void setRespectiveType(int respectiveType) {
		this.respectiveType = respectiveType;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUnprocessedReason(String unprocessedReason) {
		this.unprocessedReason = unprocessedReason;
	}
	
	public RAFeed clone() throws CloneNotSupportedException { 
		return (RAFeed)super.clone();  
	}
	
}
