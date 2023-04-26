package com.furence.recsee.scriptRegistration.model;

public class scriptProductValueInfo {

	private	Integer rsProductValuePk;
	private Integer rsProductType;
	private String rsProductCode;
	private String rsProductValueName;
	private String rsProductValueCode;
	private String rsProductValueVal;
	private String rsProductValueRealtimeTTS;
	public String getRsProductValueRealtimeTTS() {
		return rsProductValueRealtimeTTS;
	}
	public void setRsProductValueRealtimeTTS(String rsProductValueRealtimeTTS) {
		this.rsProductValueRealtimeTTS = rsProductValueRealtimeTTS;
	}
	public Integer getRsProductValuePk() {
		return rsProductValuePk;
	}
	public void setRsProductValuePk(Integer rsProductValuePk) {
		this.rsProductValuePk = rsProductValuePk;
	}
	public Integer getRsProductType() {
		return rsProductType;
	}
	public void setRsProductType(Integer rsProductType) {
		this.rsProductType = rsProductType;
	}
	public String getRsProductCode() {
		return rsProductCode;
	}
	public void setRsProductCode(String rsProductCode) {
		this.rsProductCode = rsProductCode;
	}
	public String getRsProductValueName() {
		return rsProductValueName;
	}
	public void setRsProductValueName(String rsProductValueName) {
		this.rsProductValueName = rsProductValueName;
	}
	public String getRsProductValueCode() {
		return rsProductValueCode;
	}
	public void setRsProductValueCode(String rsProductValueCode) {
		this.rsProductValueCode = rsProductValueCode;
	}
	public String getRsProductValueVal() {
		return rsProductValueVal;
	}
	public void setRsProductValueVal(String rsProductValueVal) {
		this.rsProductValueVal = rsProductValueVal;
	}
	@Override
	public String toString() {
		return "scriptProductValueInfo [rsProductValuePk=" + rsProductValuePk + ", rsProductType=" + rsProductType
				+ ", rsProductCode=" + rsProductCode + ", rsProductValueName=" + rsProductValueName
				+ ", rsProductValueCode=" + rsProductValueCode + ", rsProductValueVal=" + rsProductValueVal + "]";
	}
	
	
	
}
