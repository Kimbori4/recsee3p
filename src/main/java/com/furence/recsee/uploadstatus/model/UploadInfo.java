package com.furence.recsee.uploadstatus.model;

import java.util.HashMap;
import java.util.List;

public class UploadInfo {
	private String srecDate;
	private String erecDate;
	private String srecTime;
	private String erecTime;
	private String sysCode;
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String callKeyAp;
	private String workReason;
	private String regDatetime;
	private String resvDatetime;
	private String procDatetime;
	private String sregDatetime;
	private String sresvDatetime;
	private String sprocDatetime;
	private String eregDatetime;
	private String eresvDatetime;
	private String eprocDatetime;
	private String desc;
	
	private String totalUpload;
	private String results;
	private String resultf;
	private String resultr;
	private String workTyped;
	private String workTypeb;
	private String selectDate;
	
	private Integer chartLimit;

	
	
	
	
	
	private String recDate;
	private String recTime;
	private String extNum;
	private String custPhone1;
	private String userId;
	private String workType;
	private String result;
	private String record;
	private String fTryDatetime;
	private String sTryDatetime;
	private String tTryDatetime;
	
	private Integer posStart;
	private Integer count;
	
	public String getCustPhone1() {
		return custPhone1;
	}
	public void setCustPhone1(String custPhone1) {
		this.custPhone1 = custPhone1;
	}
	private List<HashMap<String, String>> authyInfo;

	public List<HashMap<String, String>> getAuthyInfo() {
		return authyInfo;
	}
	public void setAuthyInfo(List<HashMap<String, String>> authyInfo) {
		this.authyInfo = authyInfo;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public Integer getPosStart() {
		return posStart;
	}
	public void setPosStart(Integer posStart) {
		this.posStart = posStart;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFTryDatetime() {
		return fTryDatetime;
	}
	public void setFTryDatetime(String fTryDatetime) {
		this.fTryDatetime = fTryDatetime;
	}
	public String getSTryDatetime() {
		return sTryDatetime;
	}
	public void setSTryDatetime(String sTryDatetime) {
		this.sTryDatetime = sTryDatetime;
	}
	public String getTTryDatetime() {
		return tTryDatetime;
	}
	public void setTTryDatetime(String tTryDatetime) {
		this.tTryDatetime = tTryDatetime;
	}
	public Integer getChartLimit() {
		return chartLimit;
	}
	public void setChartLimit(Integer chartLimit) {
		this.chartLimit = chartLimit;
	}
	public String getSelectDate() {
		return selectDate;
	}
	public void setSelectDate(String selectDate) {
		this.selectDate = selectDate;
	}
	public String getSrecDate() {
		return srecDate;
	}
	public void setSrecDate(String srecDate) {
		this.srecDate = srecDate;
	}
	public String getErecDate() {
		return erecDate;
	}
	public void setErecDate(String erecDate) {
		this.erecDate = erecDate;
	}
	public String getSrecTime() {
		return srecTime;
	}
	public void setSrecTime(String srecTime) {
		this.srecTime = srecTime;
	}
	public String getErecTime() {
		return erecTime;
	}
	public void setErecTime(String erecTime) {
		this.erecTime = erecTime;
	}
	public String getSregDatetime() {
		return sregDatetime;
	}
	public void setSregDatetime(String sregDatetime) {
		this.sregDatetime = sregDatetime;
	}
	public String getSresvDatetime() {
		return sresvDatetime;
	}
	public void setSresvDatetime(String sresvDatetime) {
		this.sresvDatetime = sresvDatetime;
	}
	public String getSprocDatetime() {
		return sprocDatetime;
	}
	public void setSprocDatetime(String sprocDatetime) {
		this.sprocDatetime = sprocDatetime;
	}
	public String getEregDatetime() {
		return eregDatetime;
	}
	public void setEregDatetime(String eregDatetime) {
		this.eregDatetime = eregDatetime;
	}
	public String getEresvDatetime() {
		return eresvDatetime;
	}
	public void setEresvDatetime(String eresvDatetime) {
		this.eresvDatetime = eresvDatetime;
	}
	public String getEprocDatetime() {
		return eprocDatetime;
	}
	public void setEprocDatetime(String eprocDatetime) {
		this.eprocDatetime = eprocDatetime;
	}
	public void setWorkTyped(String workTyped) {
		this.workTyped = workTyped;
	}
	public String getTotalUpload() {
		return totalUpload;
	}
	public void setTotalUpload(String totalUpload) {
		this.totalUpload = totalUpload;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getResultf() {
		return resultf;
	}
	public void setResultf(String resultf) {
		this.resultf = resultf;
	}
	public String getResultr() {
		return resultr;
	}
	public void setResultr(String resultr) {
		this.resultr= resultr;
	}
	public String getWorkTyped() {
		return workTyped;
	}
	public void setWorkTypel(String workTyped) {
		this.workTyped = workTyped;
	}
	public String getWorkTypeb() {
		return workTypeb;
	}
	public void setWorkTypeb(String workTypeb) {
		this.workTypeb = workTypeb;
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
	public String getSysCode() {
		return sysCode;
	}
	
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	
	public String getBgCode() {
		return bgCode;
	}
	public void setBgCode(String bgCode) {
		this.bgCode = bgCode;
	}
	public String getMgCode() {
		return mgCode;
	}
	public void setMgCode(String mgCode) {
		this.mgCode = mgCode;
	}
	public String getSgCode() {
		return sgCode;
	}
	public void setSgCode(String sgCode) {
		this.sgCode = sgCode;
	}
	public String getCallKeyAp() {
		return callKeyAp;
	}
	public void setCallKeyAp(String callKeyAp) {
		this.callKeyAp = callKeyAp;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getWorkReason() {
		return workReason;
	}
	public void setWorkReason(String workReason) {
		this.workReason = workReason;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getRegDatetime() {
		return (regDatetime != null ) ? regDatetime : "";
	}
	public void setRegDatetime(String regDatetime) {
		this.regDatetime = regDatetime;
	}
	
	public String getResvDatetime() {
		return (resvDatetime != null ) ? resvDatetime : "";
	}
	public void setResvDatetime(String resvDatetime) {
		this.resvDatetime = resvDatetime;
	}
	public String getProcDatetime() {
		return (procDatetime != null ) ? procDatetime : "";
	}
	public void setProcDatetime(String procDatetime) {
		this.procDatetime = procDatetime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "UploadInfo [recDate=" + recDate + ", srecDate=" + srecDate + ", erecDate=" + erecDate + ", recTime="
				+ recTime + ", srecTime=" + srecTime + ", erecTime=" + erecTime + ", extNum=" + extNum + ", sysCode="
				+ sysCode + ", bgCode=" + bgCode + ", mgCode=" + mgCode + ", sgCode=" + sgCode + ", callKeyAp="
				+ callKeyAp + ", workType=" + workType + ", workReason=" + workReason + ", result=" + result
				+ ", regDatetime=" + regDatetime + ", resvDatetime=" + resvDatetime + ", procDatetime=" + procDatetime
				+ ", sregDatetime=" + sregDatetime + ", sresvDatetime=" + sresvDatetime + ", sprocDatetime="
				+ sprocDatetime + ", eregDatetime=" + eregDatetime + ", eresvDatetime=" + eresvDatetime
				+ ", eprocDatetime=" + eprocDatetime + ", desc=" + desc + ", totalUpload=" + totalUpload + ", results="
				+ results + ", resultf=" + resultf + ", resultr=" + resultr + ", workTyped=" + workTyped
				+ ", workTypeb=" + workTypeb + ", selectDate=" + selectDate + ", getSelectDate()=" + getSelectDate()
				+ ", getSrecDate()=" + getSrecDate() + ", getErecDate()=" + getErecDate() + ", getSrecTime()="
				+ getSrecTime() + ", getErecTime()=" + getErecTime() + ", getSregDatetime()=" + getSregDatetime()
				+ ", getSresvDatetime()=" + getSresvDatetime() + ", getSprocDatetime()=" + getSprocDatetime()
				+ ", getEregDatetime()=" + getEregDatetime() + ", getEresvDatetime()=" + getEresvDatetime()
				+ ", getEprocDatetime()=" + getEprocDatetime() + ", getTotalUpload()=" + getTotalUpload()
				+ ", getResults()=" + getResults() + ", getResultf()=" + getResultf() + ", getResultr()=" + getResultr()
				+ ", getWorkTyped()=" + getWorkTyped() + ", getWorkTypeb()=" + getWorkTypeb() + ", getRecDate()="
				+ getRecDate() + ", getRecTime()=" + getRecTime() + ", getExtNum()=" + getExtNum() + ", getSysCode()="
				+ getSysCode() + ", getBgCode()=" + getBgCode() + ", getMgCode()=" + getMgCode() + ", getSgCode()="
				+ getSgCode() + ", getCallKeyAp()=" + getCallKeyAp() + ", getWorkType()=" + getWorkType()
				+ ", getWorkReason()=" + getWorkReason() + ", getResult()=" + getResult() + ", getRegDatetime()="
				+ getRegDatetime() + ", getResvDatetime()=" + getResvDatetime() + ", getProcDatetime()="
				+ getProcDatetime() + ", getDesc()=" + getDesc() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
}
