package com.furence.recsee.common.model;

public class PacketVO {
	private String custCode;
	private String phoneSetting;
	private String msg;
	private String returnUrl;
	
	private String nortpDate;
	private String nortpTime;
	private String nortpExt;
	private String sendResult;

	private String sDate;
	private String eDate;
	private String sTime;
	private String eTime;

	private String topCount;
	private String limitUse;

	private Integer count;
	private Integer posStart;
	
	private String sysCode;
	private String sysName;
	private String custPhone;
	private String callId;
	
	
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getCustPhone() {
		return custPhone;
	}
	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
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
	public String getNortpDate() {
		String tmpDate = nortpDate;
		if(nortpDate != null)	tmpDate = String.format("%s-%s-%s", nortpDate.substring(0, 4), nortpDate.substring(4, 6), nortpDate.substring(6, 8));
		
		return tmpDate;
	}
	public void setNortpDate(String nortpDate) {
		this.nortpDate = nortpDate;
	}
	public String getNortpTime() {
		String tmpTime = nortpTime;
		if(nortpTime != null)	tmpTime = String.format("%s:%s:%s", nortpTime.substring(0, 2), nortpTime.substring(2, 4), nortpTime.substring(4, 6));
		
		return tmpTime;
	}
	public void setNortpTime(String nortpTime) {
		this.nortpTime = nortpTime;
	}
	public String getNortpExt() {
		return nortpExt;
	}
	public void setNortpExt(String nortpExt) {
		this.nortpExt = nortpExt;
	}
	public String getSendResult() {
		return sendResult;
	}
	public void setSendResult(String sendResult) {
		this.sendResult = sendResult;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getPhoneSetting() {
		return phoneSetting;
	}
	public void setPhoneSetting(String phoneSetting) {
		this.phoneSetting = phoneSetting;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
	
	
}
