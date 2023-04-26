package com.furence.recsee.scriptRegistration.model;

import java.util.ArrayList;
import java.util.List;

public class ScriptHistoryCreateParam {

	private String rScriptCallKey;
	private String rScriptStepFk;
	private String rProductCode;
	private String rProductType;
	
	private String rScriptCaseDetail;
	
	
	
	
	
	
	
	
	public String getrScriptCaseDetail() {
		return rScriptCaseDetail;
	}

	public void setrScriptCaseDetail(String rScriptCaseDetail) {
		this.rScriptCaseDetail = rScriptCaseDetail;
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

	private int cpClassFlag;
	
	final private List<String> rScriptDetailList = new ArrayList<>();

	public String getrScriptCallKey() {
		return rScriptCallKey;
	}

	public void setrScriptCallKey(String rScriptCallKey) {
		this.rScriptCallKey = rScriptCallKey;
	}

	public String getrScriptStepFk() {
		return rScriptStepFk;
	}

	public void setrScriptStepFk(String rScriptStepFk) {
		this.rScriptStepFk = rScriptStepFk;
	}

	public List<String> getrScriptDetailList() {
		return rScriptDetailList;
	}
	
	public void addScriptDetailList(String value) {
		this.rScriptDetailList.add(value);
	}
	
	

	public int getCpClassFlag() {
		return cpClassFlag;
	}

	public void setCpClassFlag(int cpClassFlag) {
		this.cpClassFlag = cpClassFlag;
	}

	@Override
	public String toString() {
		return "ScriptHistoryCreateParam [rScriptCallKey=" + rScriptCallKey + ", rScriptStepFk=" + rScriptStepFk
				+ ", cpClassFlag=" + cpClassFlag + ", rScriptDetailList=" + rScriptDetailList + "]";
	}

	
	
	
}
