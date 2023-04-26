package com.furence.recsee.admin.model;

public class ScreenInfo {
	String rBgCode;
	String rMgCode;
	String rSgCode;
	String rUserId;
	String rExtNo;
	String rScreenYn;
	String rScreenStatus; // 상태 표시 용 (임시)
	String sExtNo;
	
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
	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrExtNo() {
		return rExtNo;
	}
	public void setrExtNo(String rExtNo) {
		this.rExtNo = rExtNo;
	}
	public String getrScreenYn() {
		return rScreenYn;
	}
	public void setrScreenYn(String rScreenYn) {
		this.rScreenYn = rScreenYn;
	}
	public String getrScreenStatus() {
		return rScreenStatus;
	}
	public void setrScreenStatus(String rScreenStatus) {
		this.rScreenStatus = rScreenStatus;
	}
	public String getsExtNo() {
		return sExtNo;
	}
	public void setsExtNo(String sExtNo) {
		this.sExtNo = sExtNo;
	}
	@Override
	public String toString() {
		return "ScreenInfo [rBgCode=" + rBgCode + ", rMgCode=" + rMgCode
				+ ", rSgCode=" + rSgCode + ", rUserId=" + rUserId + ", rExtNo="
				+ rExtNo + ", rScreenYn=" + rScreenYn + ", rScreenStatus="
				+ rScreenStatus + ", sExtNo=" + sExtNo + "]";
	}
}
