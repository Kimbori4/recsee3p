package com.furence.recsee.admin.model;

public class PwHistory {
	
	private String rUserId;
	private String rPassword;
	private String rHistoryDate;
	private String count;
	private String rPastPwUse;
	private String rPastPwCount;
	
	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrPassword() {
		return rPassword;
	}
	public void setrPassword(String rPassword) {
		this.rPassword = rPassword;
	}
	public String getrHistoryDate() {
		return rHistoryDate;
	}
	public void setrHistoryDate(String rHistoryDate) {
		this.rHistoryDate = rHistoryDate;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getrPastPwCount() {
		return rPastPwCount;
	}
	public void setrPastPwCount(String rPastPwCount) {
		this.rPastPwCount = rPastPwCount;
	}
	public String getrPastPwUse() {
		return rPastPwUse;
	}
	public void setrPastPwUse(String rPastPwUse) {
		this.rPastPwUse = rPastPwUse;
	}
}
