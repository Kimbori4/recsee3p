package com.furence.recsee.admin.model;

public class MultiPartInfo {
	String rType;
	String rTarget;
	String rBgCode;
	String rMgCode;
	String rSgCode;
	String rExcept;
	
	public String getrType() {
		return rType;
	}
	public void setrType(String rType) {
		this.rType = rType;
	}
	public String getrTarget() {
		return rTarget;
	}
	public void setrTarget(String rTarget) {
		this.rTarget = rTarget;
	}
	public String getrBgCode() {
		return rBgCode;
	}
	public void setrBgCode(String rBgCode) {
		this.rBgCode = rBgCode;
	}
	public String getrMgCode() {
		return rMgCode;
	}
	public void setrMgCode(String rMgCode) {
		this.rMgCode = rMgCode;
	}
	public String getrSgCode() {
		return rSgCode;
	}
	public void setrSgCode(String rSgCode) {
		this.rSgCode = rSgCode;
	}
	public String getrExcept() {
		return rExcept;
	}
	public void setrExcept(String rExcept) {
		this.rExcept = rExcept;
	}
	@Override
	public String toString() {
		return "MultiPartInfo [rType=" + rType + ", rTarget=" + rTarget
				+ ", rBgCode=" + rBgCode + ", rMgCode=" + rMgCode
				+ ", rSgCode=" + rSgCode + ", rExcept=" + rExcept + "]";
	}
}
