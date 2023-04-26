package com.furence.recsee.wooribank.facerecording.model;


import java.util.ArrayList;
import java.util.List;



public class RequestRetryRecReason {

	private List<String> scriptCallKeyArr;

	private String callKeyAp;
	
	private String retryReason;
	
	private String retryReasonDetail;
	
	private String retryRecHistoryPk;
	
	private String date;
	
	private String time;
	
	private String userId;
	
	private String callKey;
	
	
	
	public String getCallKey() {
		return callKey;
	}


	public void setCallKey(String callKey) {
		this.callKey = callKey;
	}


	public void MakescriptCallKeyArr() {
		if(callKeyAp==null) {return;}
		this.scriptCallKeyArr = new ArrayList<String>();
		String[] split = callKeyAp.split(",");
		for (String string : split) {
			scriptCallKeyArr.add(string);
		}
	}
	

	public List<String> getScriptCallKeyArr() {
		return scriptCallKeyArr;
	}

	public void setScriptCallKeyArr(List<String> scriptCallKeyArr) {
		this.scriptCallKeyArr = scriptCallKeyArr;
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
	
	
}
