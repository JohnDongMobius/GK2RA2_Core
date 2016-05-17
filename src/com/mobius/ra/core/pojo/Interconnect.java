package com.mobius.ra.core.pojo;

public class Interconnect implements Cloneable {
	private String trafficDate;
	private int reportType;
	private String FranchiseID;
	private String trafficDirection;
	private String interconnectOperator;
	private String productID;
	private String in_trunk_name;
	private String out_trunk_name;
	private String trunk_name;
	private double rate;
	private long callCountMTC;
	private double durationMTC;
	private long callCountMOC;
	private double durationMOC;
	private long callCountTST;
	private double durationTST;
	private long call_count;
	private double durationCount;
	private double amountCount;
	private String s_msisdn;
	private String o_msisdn;
	protected long trunkIn;
	protected long trunkOut;
	private boolean isDeled;
	private String insertTime;
	private int trunk_scope;
	protected String callType;
	private int productIDType;
	private int in_trunk_type;
	private int out_trunk_type;
	private int in_traffic_type;
	private int out_traffic_type;
	private int traffic_type;
	private double in_interconnect_rate;
	private double out_interconnect_rate;
	private String in_interconnect_operator;
	private String out_interconnect_operator;

	public String getTrunk_name() {
		return trunk_name;
	}

	public void setTrunk_name(String trunk_name) {
		this.trunk_name = trunk_name;
	}

	public int getTrunk_scope() {
		return trunk_scope;
	}

	public void setTrunk_scope(int trunk_scope) {
		this.trunk_scope = trunk_scope;
	}

	public String getIn_interconnect_operator() {
		return in_interconnect_operator;
	}

	public void setIn_interconnect_operator(String in_interconnect_operator) {
		this.in_interconnect_operator = in_interconnect_operator;
	}

	public int getTraffic_type() {
		return traffic_type;
	}

	public void setTraffic_type(int traffic_type) {
		this.traffic_type = traffic_type;
	}

	public String getOut_interconnect_operator() {
		return out_interconnect_operator;
	}

	public void setOut_interconnect_operator(String out_interconnect_operator) {
		this.out_interconnect_operator = out_interconnect_operator;
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

	public long getCallCountMTC() {
		return callCountMTC;
	}

	public void setCallCountMTC(long callCountMTC) {
		this.callCountMTC = callCountMTC;
	}

	public double getDurationMTC() {
		return durationMTC;
	}

	public void setDurationMTC(double d) {
		this.durationMTC = d;
	}

	public long getCallCountMOC() {
		return callCountMOC;
	}

	public void setCallCountMOC(long callCountMOC) {
		this.callCountMOC = callCountMOC;
	}

	public double getDurationMOC() {
		return durationMOC;
	}

	public void setDurationMOC(double d) {
		this.durationMOC = d;
	}

	public long getCallCountTST() {
		return callCountTST;
	}

	public void setCallCountTST(long callCountTST) {
		this.callCountTST = callCountTST;
	}

	public double getDurationTST() {
		return durationTST;
	}

	public void setDurationTST(double d) {
		this.durationTST = d;
	}

	public int getProductIDType() {
		return productIDType;
	}

	public void setProductIDType(int productIDType) {
		this.productIDType = productIDType;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	@Override
	public Object clone() {
		Interconnect dr = null;
		try {
			dr = (Interconnect) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return dr;
	}

	public String getTrafficDate() {
		return trafficDate;
	}

	public void setTrafficDate(String trafficDate) {
		this.trafficDate = trafficDate;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public String getFranchiseID() {
		return FranchiseID;
	}

	public void setFranchiseID(String franchiseID) {
		FranchiseID = franchiseID;
	}

	public String getTrafficDirection() {
		return trafficDirection;
	}

	public void setTrafficDirection(String trafficDirection) {
		this.trafficDirection = trafficDirection;
	}

	public String getInterconnectOperator() {
		return interconnectOperator;
	}

	public void setInterconnectOperator(String interconnectOperator) {
		this.interconnectOperator = interconnectOperator;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
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

	public double getRate() {
		return rate;
	}

	public void setRate(double d) {
		this.rate = d;
	}

	public long getCall_count() {
		return call_count;
	}

	public void setCall_count(long call_count) {
		this.call_count = call_count;
	}

	public double getDurationCount() {
		return durationCount;
	}

	public void setDurationCount(double d) {
		this.durationCount = d;
	}

	public double getAmountCount() {
		return amountCount;
	}

	public void setAmountCount(double amountCount) {
		this.amountCount = amountCount;
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

	public boolean isDeled() {
		return isDeled;
	}

	public void setDeled(boolean isDeled) {
		this.isDeled = isDeled;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

}
