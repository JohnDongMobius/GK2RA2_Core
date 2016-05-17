package com.mobius.ra.core.pojo;

public class CdrDetail {
	private long call_id;
	private int call_type;
	private String call_time;
	private long duration;
	private String s_msisdn;
	private String o_msisdn;
	private long s_imsi;
	private long s_imei;
	private long file_name;
	private long trunkIn;
	private long trunkOut;
	private String s_code;
	private String o_code;
	private String product_id;
	private String sub_product_id;
	private String time_period;
	private double retail_rate;
	private int zone_id;
	private String s_type;
	private String o_type;
	private String in_trunk_name;
	private String out_trunk_name;
	private String in_interconnect_operator;
	private String out_interconnect_operator;
	private int in_trunk_type;
	private int out_trunk_type;
	private int in_traffic_type;
	private int out_traffic_type;
	private double in_interconnect_rate;
	private double out_interconnect_rate;
	private long transit_id;
	private long forwarded_num;
	private String tap_code;
	private int duplicated;
	private String duplicated_call_id;

	public String getDuplicated_call_id() {
		return duplicated_call_id;
	}

	public void setDuplicated_call_id(String duplicated_call_id) {
		this.duplicated_call_id = duplicated_call_id;
	}

	public int getDuplicated() {
		return duplicated;
	}

	public void setDuplicated(int duplicated) {
		this.duplicated = duplicated;
	}

	public long getS_imsi() {
		return s_imsi;
	}

	public void setS_imsi(long s_imsi) {
		this.s_imsi = s_imsi;
	}

	public long getS_imei() {
		return s_imei;
	}

	public void setS_imei(long s_imei) {
		this.s_imei = s_imei;
	}

	public long getFile_name() {
		return file_name;
	}

	public void setFile_name(long file_name) {
		this.file_name = file_name;
	}

	public String getIn_interconnect_operator() {
		return in_interconnect_operator;
	}

	public void setIn_interconnect_operator(String in_interconnect_operator) {
		this.in_interconnect_operator = in_interconnect_operator;
	}

	public String getOut_interconnect_operator() {
		return out_interconnect_operator;
	}

	public void setOut_interconnect_operator(String out_interconnect_operator) {
		this.out_interconnect_operator = out_interconnect_operator;
	}

	public int getIn_traffic_type() {
		return in_traffic_type;
	}

	public void setIn_traffic_type(int in_traffic_type) {
		this.in_traffic_type = in_traffic_type;
	}

	public int getOut_traffic_type() {
		return out_traffic_type;
	}

	public void setOut_traffic_type(int out_traffic_type) {
		this.out_traffic_type = out_traffic_type;
	}

	public double getIn_interconnect_rate() {
		return in_interconnect_rate;
	}

	public void setIn_interconnect_rate(double in_interconnect_rate) {
		this.in_interconnect_rate = in_interconnect_rate;
	}

	public double getOut_interconnect_rate() {
		return out_interconnect_rate;
	}

	public void setOut_interconnect_rate(double out_interconnect_rate) {
		this.out_interconnect_rate = out_interconnect_rate;
	}

	public String getTap_code() {
		return tap_code;
	}

	public void setTap_code(String tap_code) {
		this.tap_code = tap_code;
	}

	public long getTrunkIn() {
		return trunkIn;
	}

	public void setTrunkIn(long trunkIn) {
		this.trunkIn = trunkIn;
	}

	public long getTrunkOut() {
		return trunkOut;
	}

	public void setTrunkOut(long trunkOut) {
		this.trunkOut = trunkOut;
	}

	public long getCall_id() {
		return call_id;
	}

	public void setCall_id(long call_id) {
		this.call_id = call_id;
	}

	public int getCall_type() {
		return call_type;
	}

	public void setCall_type(int call_type) {
		this.call_type = call_type;
	}

	public String getCall_time() {
		return call_time;
	}

	public void setCall_time(String call_time) {
		this.call_time = call_time;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getS_msisdn() {
		return s_msisdn;
	}

	public void setS_msisdn(String s_msisdn) {
		this.s_msisdn = s_msisdn;
	}

	public String getO_msisdn() {
		return o_msisdn;
	}

	public void setO_msisdn(String o_msisdn) {
		this.o_msisdn = o_msisdn;
	}

	public String getS_code() {
		return s_code;
	}

	public void setS_code(String s_code) {
		this.s_code = s_code;
	}

	public String getO_code() {
		return o_code;
	}

	public void setO_code(String o_code) {
		this.o_code = o_code;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getSub_product_id() {
		return sub_product_id;
	}

	public void setSub_product_id(String sub_product_id) {
		this.sub_product_id = sub_product_id;
	}

	public String getTime_period() {
		return time_period;
	}

	public void setTime_period(String time_period) {
		this.time_period = time_period;
	}

	public double getRetail_rate() {
		return retail_rate;
	}

	public void setRetail_rate(double retail_rate) {
		this.retail_rate = retail_rate;
	}

	public String getS_type() {
		return s_type;
	}

	public void setS_type(String s_type) {
		this.s_type = s_type;
	}

	public String getO_type() {
		return o_type;
	}

	public void setO_type(String o_type) {
		this.o_type = o_type;
	}

	public String getIn_trunk_name() {
		return in_trunk_name;
	}

	public void setIn_trunk_name(String in_trunk_name) {
		this.in_trunk_name = in_trunk_name;
	}

	public String getOut_trunk_name() {
		return out_trunk_name;
	}

	public void setOut_trunk_name(String out_trunk_name) {
		this.out_trunk_name = out_trunk_name;
	}

	public int getIn_trunk_type() {
		return in_trunk_type;
	}

	public void setIn_trunk_type(int in_trunk_type) {
		this.in_trunk_type = in_trunk_type;
	}

	public int getOut_trunk_type() {
		return out_trunk_type;
	}

	public void setOut_trunk_type(int out_trunk_type) {
		this.out_trunk_type = out_trunk_type;
	}

	public long getTransit_id() {
		return transit_id;
	}

	public void setTransit_id(long transit_id) {
		this.transit_id = transit_id;
	}

	public long getForwarded_num() {
		return forwarded_num;
	}

	public void setForwarded_num(long forwarded_num) {
		this.forwarded_num = forwarded_num;
	}

	public int getZone_id() {
		return zone_id;
	}

	public void setZone_id(int zone_id) {
		this.zone_id = zone_id;
	}
}
