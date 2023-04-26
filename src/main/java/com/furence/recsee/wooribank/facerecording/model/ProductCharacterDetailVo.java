package com.furence.recsee.wooribank.facerecording.model;

public class ProductCharacterDetailVo {
	
	private String rProductCode;
	
	private String rProductType;
	
	private String rProductName;
	
	private String rProductDetailText;
	
	private String rProductSubCode;
	
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

	public String getrProductType() {
		return rProductType;
	}

	public void setrProductType(String rProductType) {
		this.rProductType = rProductType;
	}

	public String getrProductName() {
		return rProductName;
	}

	public void setrProductName(String rProductName) {
		this.rProductName = rProductName;
	}

	public String getrProductDetailText() {
		return rProductDetailText;
	}

	public void setrProductDetailText(String rProductDetailText) {
		this.rProductDetailText = rProductDetailText;
	}

	public String getrProductSubCode() {
		return rProductSubCode;
	}

	public void setrProductSubCode(String rProductSubCode) {
		this.rProductSubCode = rProductSubCode;
	}

	@Override
	public String toString() {
		return "ProductCharacterDetailVo [rProductCode=" + rProductCode + ", rProductType=" + rProductType
				+ ", rProductName=" + rProductName + ", rProductDetailText=" + rProductDetailText + ", rProductSubCode="
				+ rProductSubCode + "]";
	}
	
	
	
	
	
	

}
