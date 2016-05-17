package com.mobius.ra.core.pojo;

public class RaTrunk {
	private String source_operator_id;
	private String trunk_name;
	private String interconnect_operator;
	private String traffic_direction;
	private String trunk_scope;

	public String getTrunk_scope() {
		return trunk_scope;
	}

	public void setTrunk_scope(String trunk_scope) {
		this.trunk_scope = trunk_scope;
	}

	public String getSource_operator_id() {
		return source_operator_id;
	}

	public void setSource_operator_id(String source_operator_id) {
		this.source_operator_id = source_operator_id;
	}

	public String getTrunk_name() {
		return trunk_name;
	}

	public void setTrunk_name(String trunk_name) {
		this.trunk_name = trunk_name;
	}

	public String getInterconnect_operator() {
		return interconnect_operator;
	}

	public void setInterconnect_operator(String interconnect_operator) {
		this.interconnect_operator = interconnect_operator;
	}

	public String getTraffic_direction() {
		return traffic_direction;
	}

	public void setTraffic_direction(String traffic_direction) {
		this.traffic_direction = traffic_direction;
	}

}
