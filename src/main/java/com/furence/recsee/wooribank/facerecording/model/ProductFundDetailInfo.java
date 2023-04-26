package com.furence.recsee.wooribank.facerecording.model;

public class ProductFundDetailInfo {
	
	
	private String rProductCode;
	
	private String rProductFundBCode;
	
	private String rProductFundDetailCode;
	
	private String rProductFundDetailName;
	
	private String rProductFundDetailText;
	
	private String rProductInsuranceCode;
	
	private String rProductInsuranceName;
	
	

	public String getrProductInsuranceCode() {
		return rProductInsuranceCode;
	}

	public void setrProductInsuranceCode(String rProductInsuranceCode) {
		this.rProductInsuranceCode = rProductInsuranceCode;
	}

	public String getrProductInsuranceName() {
		return rProductInsuranceName;
	}

	public void setrProductInsuranceName(String rProductInsuranceName) {
		this.rProductInsuranceName = rProductInsuranceName;
	}

	public String getrProductCode() {
		return rProductCode;
	}

	public void setrProductCode(String rProductCode) {
		this.rProductCode = rProductCode;
	}

	public String getrProductFundBCode() {
		return rProductFundBCode;
	}

	public void setrProductFundBCode(String rProductFundBCode) {
		this.rProductFundBCode = rProductFundBCode;
	}

	public String getrProductFundDetailCode() {
		return rProductFundDetailCode;
	}

	public void setrProductFundDetailCode(String rProductFundDetailCode) {
		this.rProductFundDetailCode = rProductFundDetailCode;
	}

	public String getrProductFundDetailName() {
		return rProductFundDetailName;
	}

	public void setrProductFundDetailName(String rProductFundDetailName) {
		this.rProductFundDetailName = rProductFundDetailName;
	}

	public String getrProductFundDetailText() {
		return rProductFundDetailText;
	}

	public void setrProductFundDetailText(String rProductFundDetailText) {
		this.rProductFundDetailText = rProductFundDetailText;
	}

	@Override
	public String toString() {
		return "ProductFundDetailInfo [rProductCode=" + rProductCode + ", rProductFundBCode=" + rProductFundBCode
				+ ", rProductFundDetailCode=" + rProductFundDetailCode + ", rProductFundDetailName="
				+ rProductFundDetailName + ", rProductFundDetailText=" + rProductFundDetailText + "]";
	}
	
	

}
