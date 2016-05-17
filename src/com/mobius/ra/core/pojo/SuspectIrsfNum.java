package com.mobius.ra.core.pojo;

import java.io.Serializable;

/**
 * @author John Dong
 * @date Aug 11, 2015
 * @version v 1.0
 */
public class SuspectIrsfNum implements Serializable {

	private static final long serialVersionUID = 3315484113819088920L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private long id;
	private String source;
	private String type;
	private String irsfNumber;
	private String country;
	private String cdrCountry;
	private String cdrAttribute;
	private String description;
	private String dateEntered;
	private String enteredBy;
	private String dateUpdated;
	private String updatedBy;
	private String expirationDate;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIrsfNumber() {
		return irsfNumber;
	}
	public void setIrsfNumber(String irsfNumber) {
		this.irsfNumber = irsfNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCdrCountry() {
		return cdrCountry;
	}
	public void setCdrCountry(String cdrCountry) {
		this.cdrCountry = cdrCountry;
	}
	public String getCdrAttribute() {
		return cdrAttribute;
	}
	public void setCdrAttribute(String cdrAttribute) {
		this.cdrAttribute = cdrAttribute;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(String dateEntered) {
		this.dateEntered = dateEntered;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
}
