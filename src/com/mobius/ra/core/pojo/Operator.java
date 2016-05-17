package com.mobius.ra.core.pojo;

import com.mobius.ra.core.common.Constants;

public class Operator {
	public String code;
	public String name;

	public Operator(String name, String code) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getCoreAlias() {
		return Constants.DB_NAME_CORE + code;
	}

	public String getFraudAlias() {
		return Constants.DB_NAME_FRAUD + code;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}
}
