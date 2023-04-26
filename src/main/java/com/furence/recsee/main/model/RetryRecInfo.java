package com.furence.recsee.main.model;

public class RetryRecInfo {
	private String retryRecHistoryPk;
	private String date;
	private String time;
	private String userId;
	private String callKeyAp;
	private String retryReason;
	private String retryReasonDetail;
	public String getRetryRecHistoryPk() {
		return retryRecHistoryPk;
	}
	public void setRetryRecHistoryPk(String retryRecHistoryPk) {
		this.retryRecHistoryPk = retryRecHistoryPk;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCallKeyAp() {
		return callKeyAp;
	}
	public void setCallKeyAp(String callKeyAp) {
		this.callKeyAp = callKeyAp;
	}
	public String getRetryReason() {
		return retryReason;
	}
	public void setRetryReason(String retryReason) {
		this.retryReason = retryReason;
	}
	public String getRetryReasonDetail() {
		return retryReasonDetail;
	}
	public void setRetryReasonDetail(String retryReasonDetail) {
		this.retryReasonDetail = retryReasonDetail;
	}
}
