package com.furence.recsee.wooribank.facerecording.model;

public class ScriptStepDto {
	
	private int tKey;
	private int depth;
	private int rScriptStepParent;
	private int rProductListPk;
	private String rProductName;
	private String rProductCode;
	private String rScriptStepName;
	private String callKey;
	private String tOrder;
	
	
	public int getrScriptStepParent() {
		return rScriptStepParent;
	}
	public void setrScriptStepParent(int rScriptStepParent) {
		this.rScriptStepParent = rScriptStepParent;
	}
	public int gettKey() {
		return tKey;
	}
	public void settKey(int tKey) {
		this.tKey = tKey;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getrProductListPk() {
		return rProductListPk;
	}
	public void setrProductListPk(int rProductListPk) {
		this.rProductListPk = rProductListPk;
	}
	public String getrProductName() {
		return rProductName;
	}
	public void setrProductName(String rProductName) {
		this.rProductName = rProductName;
	}
	public String getrProductCode() {
		return rProductCode;
	}
	public void setrProductCode(String rProductCode) {
		this.rProductCode = rProductCode;
	}
	public String getrScriptStepName() {
		return rScriptStepName;
	}
	public void setrScriptStepName(String rScriptStepName) {
		this.rScriptStepName = rScriptStepName;
	}
	public String getCallKey() {
		return callKey;
	}
	public void setCallKey(String callKey) {
		this.callKey = callKey;
	}

	
	
	public String gettOrder() {
		return tOrder;
	}
	public void settOrder(String tOrder) {
		this.tOrder = tOrder;
	}
	public void setMoreProductStepFirst(String productCode,String callKey,String productName,int order) {
		this.callKey = callKey;
		this.rProductCode = productCode;
		this.rScriptStepName = productName;
		this.rProductName = productName;
		this.depth = 1;
		this.tKey = 1;
		this.rScriptStepParent = 1;
		this.tOrder = ""+order;
	}
	public void setMoreProductStepEnd(String productCode,String callKey,String productName,int order,int parent) {
		this.callKey = callKey;
		this.rProductCode = productCode;
		this.rScriptStepName = productName;
		this.rProductName = productName;
		this.depth = 1;
		this.tKey = 999999;
		this.tOrder = "1";
		this.rScriptStepParent = parent;
	}
	@Override
	public String toString() {
		return "ScriptStepDto [tKey=" + tKey + ", rScriptStepParent=" + rScriptStepParent + ", rProductCode="
				+ rProductCode + ", rScriptStepName=" + rScriptStepName + ", tOrder=" + tOrder + "]";
	}
	
	

}
