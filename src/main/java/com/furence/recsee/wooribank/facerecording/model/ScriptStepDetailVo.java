package com.furence.recsee.wooribank.facerecording.model;

import org.json.simple.JSONObject;

import lombok.ToString;
@ToString
public class ScriptStepDetailVo implements Cloneable {
	

	private int rScriptDetailPk;
	private String rScriptDetailType;
	private String rScriptDetailComKind;
	private String rScriptDetailText;
	private String rScriptDetailIfCase;
	private String rScriptStepFk;
	private String rScriptDetailIfCaseDetail;
	private String rScriptDetailRealtimeTTS;
	private String rScriptDetailCreateDate;
	private String rScriptDetailUdateDate;
	private String rScriptDetailUpdateUser;
	private String rScriptDetailConfirm;
	private String rScriptDetailConfirmDate;
	private String rScriptDetailReservDate;
	private String rScriptDetailCreateUser;
	private String rScriptDetailConfirmUser;
	private String rScriptDetailConfirmOrder;
	private String rScriptDetailConfirmComFk;
	private String rScriptDetailIfCaseCode;
	private String rScriptDetailIfCaseDetailCode;
	private String rUseYn;
	private Integer rScriptDetailEltCase;
	private String rProductAttributesExt;
	private String rProductAttributes;
	private String rScriptStepDetailCaseAttributes;
	
	
	
	
	
	
	
	public String getrScriptDetailIfCaseCode() {
		return rScriptDetailIfCaseCode;
	}
	public void setrScriptDetailIfCaseCode(String rScriptDetailIfCaseCode) {
		this.rScriptDetailIfCaseCode = rScriptDetailIfCaseCode;
	}
	public String getrScriptDetailIfCaseDetailCode() {
		return rScriptDetailIfCaseDetailCode;
	}
	public void setrScriptDetailIfCaseDetailCode(String rScriptDetailIfCaseDetailCode) {
		this.rScriptDetailIfCaseDetailCode = rScriptDetailIfCaseDetailCode;
	}
	public String getrScriptStepDetailCaseAttributes() {
		return rScriptStepDetailCaseAttributes;
	}
	public void setrScriptStepDetailCaseAttributes(String rScriptStepDetailCaseAttributes) {
		this.rScriptStepDetailCaseAttributes = rScriptStepDetailCaseAttributes;
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
	public Integer getrScriptDetailEltCase() {
		return rScriptDetailEltCase;
	}
	public void setrScriptDetailEltCase(Integer rScriptDetailEltCase) {
		this.rScriptDetailEltCase = rScriptDetailEltCase;
	}
	private String rScriptCallKey;
	
	public String getrScriptStepFk() {
		return rScriptStepFk;
	}
	public void setrScriptStepFk(String rScriptStepFk) {
		this.rScriptStepFk = rScriptStepFk;
	}
	public String getrScriptCallKey() {
		return rScriptCallKey;
	}
	public void setrScriptCallKey(String rScriptCallKey) {
		this.rScriptCallKey = rScriptCallKey;
	}
	public int getrScriptDetailPk() {
		return rScriptDetailPk;
	}
	public void setrScriptDetailPk(int rScriptDetailPk) {
		this.rScriptDetailPk = rScriptDetailPk;
	}
	public String getrScriptDetailType() {
		return rScriptDetailType;
	}
	public void setrScriptDetailType(String rScriptDetailType) {
		this.rScriptDetailType = rScriptDetailType;
	}
	public String getrScriptDetailComKind() {
		return rScriptDetailComKind;
	}
	public void setrScriptDetailComKind(String rScriptDetailComKind) {
		this.rScriptDetailComKind = rScriptDetailComKind;
	}
	public String getrScriptDetailText() {
		return rScriptDetailText;
	}
	public void setrScriptDetailText(String rScriptDetailText) {
		this.rScriptDetailText = rScriptDetailText;
	}
	public String getrScriptDetailIfCase() {
		return rScriptDetailIfCase;
	}
	public void setrScriptDetailIfCase(String rScriptDetailIfCase) {
		this.rScriptDetailIfCase = rScriptDetailIfCase;
	}
	public String getrScriptDetailIfCaseDetail() {
		return rScriptDetailIfCaseDetail;
	}
	public void setrScriptDetailIfCaseDetail(String rScriptDetailIfCaseDetail) {
		this.rScriptDetailIfCaseDetail = rScriptDetailIfCaseDetail;
	}
	public String getrScriptDetailRealtimeTTS() {
		return rScriptDetailRealtimeTTS;
	}
	public void setrScriptDetailRealtimeTTS(String rScriptDetailRealtimeTTS) {
		this.rScriptDetailRealtimeTTS = rScriptDetailRealtimeTTS;
	}
	public String getrScriptDetailCreateDate() {
		return rScriptDetailCreateDate;
	}
	public void setrScriptDetailCreateDate(String rScriptDetailCreateDate) {
		this.rScriptDetailCreateDate = rScriptDetailCreateDate;
	}
	public String getrScriptDetailUdateDate() {
		return rScriptDetailUdateDate;
	}
	public void setrScriptDetailUdateDate(String rScriptDetailUdateDate) {
		this.rScriptDetailUdateDate = rScriptDetailUdateDate;
	}
	public String getrScriptDetailUpdateUser() {
		return rScriptDetailUpdateUser;
	}
	public void setrScriptDetailUpdateUser(String rScriptDetailUpdateUser) {
		this.rScriptDetailUpdateUser = rScriptDetailUpdateUser;
	}
	public String getrScriptDetailConfirm() {
		return rScriptDetailConfirm;
	}
	public void setrScriptDetailConfirm(String rScriptDetailConfirm) {
		this.rScriptDetailConfirm = rScriptDetailConfirm;
	}
	public String getrScriptDetailConfirmDate() {
		return rScriptDetailConfirmDate;
	}
	public void setrScriptDetailConfirmDate(String rScriptDetailConfirmDate) {
		this.rScriptDetailConfirmDate = rScriptDetailConfirmDate;
	}
	public String getrScriptDetailReservDate() {
		return rScriptDetailReservDate;
	}
	public void setrScriptDetailReservDate(String rScriptDetailReservDate) {
		this.rScriptDetailReservDate = rScriptDetailReservDate;
	}
	public String getrScriptDetailCreateUser() {
		return rScriptDetailCreateUser;
	}
	public void setrScriptDetailCreateUser(String rScriptDetailCreateUser) {
		this.rScriptDetailCreateUser = rScriptDetailCreateUser;
	}
	public String getrScriptDetailConfirmUser() {
		return rScriptDetailConfirmUser;
	}
	public void setrScriptDetailConfirmUser(String rScriptDetailConfirmUser) {
		this.rScriptDetailConfirmUser = rScriptDetailConfirmUser;
	}
	public String getrScriptDetailConfirmOrder() {
		return rScriptDetailConfirmOrder;
	}
	public void setrScriptDetailConfirmOrder(String rScriptDetailConfirmOrder) {
		this.rScriptDetailConfirmOrder = rScriptDetailConfirmOrder;
	}
	public String getrScriptDetailConfirmComFk() {
		return rScriptDetailConfirmComFk;
	}
	public void setrScriptDetailConfirmComFk(String rScriptDetailConfirmComFk) {
		this.rScriptDetailConfirmComFk = rScriptDetailConfirmComFk;
	}
	public String getrUseYn() {
		return rUseYn;
	}
	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public void moreProductFiveStepEntity(String text) {
		this.rScriptDetailType = "T";
		this.rScriptDetailConfirmOrder = "1";
		this.rScriptDetailPk = 99999999;
		this.rScriptDetailIfCase = "N";
		this.rScriptDetailText = text;
		this.rScriptDetailRealtimeTTS ="Y";
		this.rScriptDetailComKind = "N";
		
	}
	public void moreProductSixStepEntityS(int pk , String text,String order) {
		this.rScriptDetailType = "S";
		this.rScriptDetailConfirmOrder = order;
		this.rScriptDetailPk = pk;
		this.rScriptDetailIfCase = "N";
		this.rScriptDetailText = text;
		this.rScriptDetailRealtimeTTS ="Y";
		this.rScriptDetailComKind = "N";
		
	}
	public void moreProductSixStepEntityG(int pk ,String text,String order) {
		this.rScriptDetailType = "G";
		this.rScriptDetailConfirmOrder = order;
		this.rScriptDetailPk = pk;
		this.rScriptDetailIfCase = "N";
		this.rScriptDetailText = text;
		this.rScriptDetailRealtimeTTS ="Y";
		this.rScriptDetailComKind = "N";
	}
	
	public void bkDetailOrderCommon(String fk,String type , int commonPk , String detailIfCaseCode , String datailIfCaseDetailCode , String attr , int order) {
		this.rScriptDetailType = type;
		this.rScriptDetailRealtimeTTS = "Y";
		this.rScriptDetailConfirmOrder = ""+order;
		this.rScriptDetailConfirmUser = "admin";
		this.rScriptDetailConfirm = "Y";
		this.rScriptStepFk = fk;
		
		//공통문구 유무
		if(commonPk != 0) {
			this.rScriptDetailComKind = "Y";
			this.rScriptDetailConfirmComFk = ""+commonPk;
		}else {
			this.rScriptDetailComKind = "N";
		}
		
		if(detailIfCaseCode != null && datailIfCaseDetailCode != null) {
			this.rScriptDetailIfCase ="N";
			this.rScriptDetailIfCaseCode = detailIfCaseCode;
			this.rScriptDetailIfCaseDetailCode = datailIfCaseDetailCode;
		}else {
			this.rScriptDetailIfCase ="N";
		}
		
		if(attr != null) {
			this.rScriptStepDetailCaseAttributes = attr;
		}
		
	}
	
	
}

