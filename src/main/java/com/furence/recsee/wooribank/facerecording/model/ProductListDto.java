package com.furence.recsee.wooribank.facerecording.model;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;
@ToString
public class ProductListDto {

	private String rProductListPk;
	private String rProductType;
	private String rProductCode;
	private String rProductName;
	private String rUseYn;
	private String rProductGroupCode; 
	private String rProductGroupYn;
	
	private String rProductGroupPk;
	
	private String rScriptCallKey;
	
	private String rProductBojungCode;
	
	private String rProductAttributes;
	
	private String rScriptStepDetailCaseAttributes;
	
	private String rSysdisType;
	
	private boolean rElfFlag;
	
	private int rEltCase;

	private int rElfAndEltFlagValue;
	
	private boolean upInvestProductYn;
	
	


	public boolean isUpInvestProductYn() {
		return upInvestProductYn;
	}



	public void setUpInvestProductYn(boolean upInvestProductYn) {
		this.upInvestProductYn = upInvestProductYn;
	}



	public int getrElfAndEltFlagValue() {
		return rElfAndEltFlagValue;
	}



	public void setrElfAndEltFlagValue(int rElfAndEltFlagValue) {
		this.rElfAndEltFlagValue = rElfAndEltFlagValue;
	}



	public int getrEltCase() {
		return rEltCase;
	}



	public void setrEltCase(int rEltCase) {
		this.rEltCase = rEltCase;
	}



	public String getrSysdisType() {
		return rSysdisType;
	}



	public void setrSysdisType(String rSysdisType) {
		this.rSysdisType = rSysdisType;
	}






	public boolean isrElfFlag() {
		return rElfFlag;
	}



	public void setrElfFlag(boolean rElfFlag) {
		this.rElfFlag = rElfFlag;
	}



	public boolean isAgtp() {
		return isAgtp;
	}



	public void setAgtp(boolean isAgtp) {
		this.isAgtp = isAgtp;
	}


	private String rProductAttributesExt;
	
	private boolean isKorean = false;
	
	private boolean isAgtp = true;
	
	
	
	
	
	
	
	public boolean isKorean() {
		return isKorean;
	}



	public void setKorean(boolean isKorean) {
		this.isKorean = isKorean;
	}



	public String getrProductAttributesExt() {
		return rProductAttributesExt;
	}



	public void setrProductAttributesExt(String rProductAttributesExt) {
		this.rProductAttributesExt = rProductAttributesExt;
	}



	public String getrProductAttributes() {
		return rProductAttributes;
	}



	public void setrProductAttributes(String rProductAttributes) {
		this.rProductAttributes = rProductAttributes;
	}



	public String getrProductBojungCode() {
		return rProductBojungCode;
	}



	public void setrProductBojungCode(String rProductBojungCode) {
		this.rProductBojungCode = rProductBojungCode;
	}



	public String getrScriptCallKey() {
		return rScriptCallKey;
	}



	public void setrScriptCallKey(String rScriptCallKey) {
		this.rScriptCallKey = rScriptCallKey;
	}



	public void setrProductListPk(String rProductListPk) {
		this.rProductListPk = rProductListPk;
	}


	private int cpClassFlag;
	
	
	
	public int getCpClassFlag() {
		return cpClassFlag;
	}



	public void setCpClassFlag(int cpClassFlag) {
		this.cpClassFlag = cpClassFlag;
	}



	public String getrProductGroupPk() {
		return rProductGroupPk;
	}



	public void setrProductGroupPk(String rProductGroupPk) {
		this.rProductGroupPk = rProductGroupPk;
	}


	private String callKey;

	final private List<String> rScriptDetailList = new ArrayList<>();

	
	public void addScriptDetailList(String value) {
		this.rScriptDetailList.add(value);
	}
	
	
	
	public String getrProductListPk() {
		return rProductListPk;
	}

	public List<String> getrScriptDetailList() {
		return rScriptDetailList;
	}


	public String getrProductType() {
		return rProductType;
	}

	public void setrProductType(String rProductType) {
		this.rProductType = rProductType;
	}

	public String getrProductCode() {
		return rProductCode;
	}

	public void setrProductCode(String rProductCode) {
		this.rProductCode = rProductCode;
	}

	public String getrProductName() {
		return rProductName;
	}

	public void setrProductName(String rProductName) {
		this.rProductName = rProductName;
	}

	public String getrUseYn() {
		return rUseYn;
	}

	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}

	public String getrProductGroupCode() {
		return rProductGroupCode;
	}

	public void setrProductGroupCode(String rProductGroupCode) {
		this.rProductGroupCode = rProductGroupCode;
	}

	public String getrProductGroupYn() {
		return rProductGroupYn;
	}

	public void setrProductGroupYn(String rProductGroupYn) {
		this.rProductGroupYn = rProductGroupYn;
	}

	public String getCallKey() {
		return callKey;
	}

	public void setCallKey(String callKey) {
		this.callKey = callKey;
	}

	@Override
	public String toString() {
		return "ProductListDto [rProductListPk=" + rProductListPk + ", rProductType=" + rProductType + ", rProductCode="
				+ rProductCode + ", rProductName=" + rProductName + ", rUseYn=" + rUseYn + ", rProductGroupCode="
				+ rProductGroupCode + ", rProductGroupYn=" + rProductGroupYn + ", callKey=" + callKey + "]";
	}
	
	
	public void setProductListDto(ProductListVo vo) {
		try {
			this.rSysdisType = vo.getrSysType();
			this.rProductCode = vo.getrProductCode();
			this.rProductGroupCode = vo.getrProductGroupCode();
			this.rProductGroupYn = vo.getrProductGroupYn();
			this.rProductListPk = vo.getrProductListPk();
			this.rProductName = vo.getrProductName();
			this.rProductType = vo.getrProductType();
			this.rUseYn = vo.getrUseYn();
//			this.rProductAttributes = vo.getrProductAttributes();
		}catch (Exception e) {
			throw new NullPointerException("상품 기본정보 누락");
		}
	}
	
	
	public void copyParamIfCaseList(List<String> caseList) {
		this.rScriptDetailList.clear();
		this.rScriptDetailList.addAll(caseList);
	}



	public String getrScriptStepDetailCaseAttributes() {
		return rScriptStepDetailCaseAttributes;
	}



	public void setrScriptStepDetailCaseAttributes(String rScriptStepDetailCaseAttributes) {
		this.rScriptStepDetailCaseAttributes = rScriptStepDetailCaseAttributes;
	}
	
	
}
