package com.furence.recsee.common.model;

public class RecMemo {
	private String memoIdx;
	private String recDate;
	private String recTime;
	private String extNum;
	private String userId;
	private String vFileName;
	private String startTime;
	private String endTime;
	private String memo;
	private String memoType;
	private String contextPath;
	private String serverIp;
	
	private String type;
	
	private String bgName;
	private String mgName;
	private String sgName;
	private String userName;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMemoIdx() {
		return memoIdx;
	}
	public void setMemoIdx(String memoIdx) {
		this.memoIdx = memoIdx;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public String getExtNum() {
		return extNum;
	}
	public void setExtNum(String extNum) {
		this.extNum = extNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getvFileName() {
		return vFileName;
	}
	public void setvFileName(String vFileName) {
		this.vFileName = vFileName;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getMemoType() {
		return memoType;
	}
	public void setMemoType(String memoType) {
		this.memoType = memoType;
	}
	public String getBgName() {
		return bgName;
	}
	public void setBgName(String bgName) {
		this.bgName = bgName;
	}
	public String getMgName() {
		return mgName;
	}
	public void setMgName(String mgName) {
		this.mgName = mgName;
	}
	public String getSgName() {
		return sgName;
	}
	public void setSgName(String sgName) {
		this.sgName = sgName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
