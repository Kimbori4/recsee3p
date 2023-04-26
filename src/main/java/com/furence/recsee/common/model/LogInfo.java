package com.furence.recsee.common.model;

public class LogInfo {
	String rLogDate;
	String rLogTime;
	String rLogIp;
	String rLogServerIp;
	String rLogUserId;
	String rLogContents;
	String rLogEtc;
	
	String sDate;
	String eDate;
	String sTime;
	String eTime;

	private String topCount;
	private String limitUse;

	private Integer count;
	private Integer posStart;
	
	private String orderBy;
	private String direction;	
	
	public String getrLogDate() {
		String tmpDate = null;
		if(rLogDate != null)	tmpDate = String.format("%s-%s-%s", rLogDate.substring(0, 4), rLogDate.substring(4,  6), rLogDate.substring(6, 8));

		return tmpDate;
	}
	public void setrLogDate(String rLogDate) {
		this.rLogDate = rLogDate;
	}
	public String getrLogTime() {
		String tmpTime = null;
		if(rLogTime != null) tmpTime = String.format("%s:%s:%s", rLogTime.substring(0, 2), rLogTime.substring(2,  4), rLogTime.substring(4, 6));

		return tmpTime;
	}
	public void setrLogTime(String rLogTime) {
		this.rLogTime = rLogTime;
	}
	public String getrLogIp() {
		return rLogIp;
	}
	public void setrLogIp(String rLogIp) {
		this.rLogIp = rLogIp;
	}
	public String getrLogServerIp() {
		return rLogServerIp;
	}
	public void setrLogServerIp(String rLogServerIp) {
		this.rLogServerIp = rLogServerIp;
	}
	public String getrLogUserId() {
		return rLogUserId;
	}
	public void setrLogUserId(String rLogUserId) {
		this.rLogUserId = rLogUserId;
	}
	public String getrLogContents() {
		return rLogContents;
	}
	public void setrLogContents(String rLogContents) {
		this.rLogContents = rLogContents;
	}
	public String getrLogEtc() {
		return rLogEtc;
	}
	public void setrLogEtc(String rLogEtc) {
		this.rLogEtc = rLogEtc;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getTopCount() {
		return topCount;
	}
	public void setTopCount(String topCount) {
		this.topCount = topCount;
	}
	public String getLimitUse() {
		return limitUse;
	}
	public void setLimitUse(String limitUse) {
		this.limitUse = limitUse;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getPosStart() {
		return posStart;
	}
	public void setPosStart(Integer posStart) {
		this.posStart = posStart;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	@Override
	public String toString() {
		return "LogInfo [rLogDate=" + rLogDate + ", rLogTime=" + rLogTime
				+ ", rLogIp=" + rLogIp + ", rLogServerIp=" + rLogServerIp
				+ ", rLogUserId=" + rLogUserId + ", rLogContents="
				+ rLogContents + ", rLogEtc=" + rLogEtc + ", sDate=" + sDate
				+ ", eDate=" + eDate + ", sTime=" + sTime + ", eTime=" + eTime
				+ ", topCount=" + topCount + ", limitUse=" + limitUse
				+ ", count=" + count + ", posStart=" + posStart + ", orderBy="
				+ orderBy + ", direction=" + direction + "]";
	}
}
