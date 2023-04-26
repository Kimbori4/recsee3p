package com.furence.recsee.transcript.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SttResult {
	private String rRecDate; 
	private String rRecTime;
	private String rExtNum;
	private String rVSysCode;
	private String rRecStartType;
	private String rWorkType;
	private String rResult;
	private String rResultReason;
	private String rRegDatetime;
	private String rResvDatetime;
	private String rProcStartDatetime;
	private String rProcEndDatetime;
	private String rProcEstTime;
	private String rDesc;
	private String rCallKeyAp;
	private String rWorkSysCode;
	
	private String sDate;
	private String eDate;
	private String sTime;
	private String eTime;
	
	
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

	public String getrRecDate() {
		if (rRecDate.length() == 8) {
			rRecDate = rRecDate.substring(0,4) + "-" + rRecDate.substring(4,6) + "-" +rRecDate.substring(6,8);
		}
		return rRecDate;
	}

	public void setrRecDate(String rRecDate) {
		this.rRecDate = rRecDate;
	}

	public String getrRecTime() {
		if (rRecTime.length() == 6) {
			rRecTime = rRecTime.substring(0,2) + ":" + rRecTime.substring(2,4) + ":" +rRecTime.substring(4,6);
		}
		return rRecTime;
	}

	public void setrRecTime(String rRecTime) {
		this.rRecTime = rRecTime;
	}

	public String getrExtNum() {
		return rExtNum;
	}

	public void setrExtNum(String rExtNum) {
		this.rExtNum = rExtNum;
	}

	public String getrVSysCode() {
		return rVSysCode;
	}

	public void setrVSysCode(String rVSysCode) {
		this.rVSysCode = rVSysCode;
	}

	public String getrRecStartType() {
		return rRecStartType;
	}

	public void setrRecStartType(String rRecStartType) {
		this.rRecStartType = rRecStartType;
	}

	public String getrWorkType() {
		return rWorkType;
	}

	public void setrWorkType(String rWorkType) {
		this.rWorkType = rWorkType;
	}

	public String getrResult() {
		return rResult;
	}

	public void setrResult(String rResult) {
		this.rResult = rResult;
	}

	public String getrResultReason() {
		return rResultReason;
	}

	public void setrResultReason(String rResultReason) {
		this.rResultReason = rResultReason;
	}

	public String getrRegDatetime() {
		if (rRegDatetime.length() == 14) {
			String rRegDate= rRegDatetime.substring(0,4) + "-" + rRegDatetime.substring(4,6) + "-" +rRegDatetime.substring(6,8);
			String rRegTime = rRegDatetime.substring(8,10) + ":" + rRegDatetime.substring(10,12) + ":" +rRegDatetime.substring(12,14);
			
			rRegDatetime = rRegDate + " " + rRegTime;
		}
		
		return rRegDatetime;
	}

	public void setrRegDatetime(String rRegDatetime) {
		this.rRegDatetime = rRegDatetime;
	}

	public String getrResvDatetime() {
		return rResvDatetime;
	}

	public void setrResvDatetime(String rResvDatetime) {
		this.rResvDatetime = rResvDatetime;
	}

	public String getrProcStartDatetime() {
		if (rProcStartDatetime.length() == 14) {
			String rProcStartDate= rProcStartDatetime.substring(0,4) + "-" + rProcStartDatetime.substring(4,6) + "-" +rProcStartDatetime.substring(6,8);
			String rProcStarttime = rProcStartDatetime.substring(8,10) + ":" + rProcStartDatetime.substring(10,12) + ":" +rProcStartDatetime.substring(12,14);
			
			rProcStartDatetime = rProcStartDate + " " + rProcStarttime;
		}
		
		return rProcStartDatetime;
	}

	public void setrProcStartDatetime(String rProcStartDatetime) {
		this.rProcStartDatetime = rProcStartDatetime;
	}

	public String getrProcEndDatetime() {
		if (rProcEndDatetime.length() == 14) {
			String rProcEndDate= rProcEndDatetime.substring(0,4) + "-" + rProcEndDatetime.substring(4,6) + "-" +rProcEndDatetime.substring(6,8);
			String rProcEndtime = rProcEndDatetime.substring(8,10) + ":" + rProcEndDatetime.substring(10,12) + ":" +rProcEndDatetime.substring(12,14);
			
			rProcEndDatetime = rProcEndDate + " " + rProcEndtime;
		}
		return rProcEndDatetime;
	}

	public void setrProcEndDatetime(String rProcEndDatetime) {
		this.rProcEndDatetime = rProcEndDatetime;
	}

	public String getrProcEstTime() {
		return rProcEstTime;
	}

	public void setrProcEstTime(String rProcEstTime) {
		this.rProcEstTime = rProcEstTime;
	}

	public String getrDesc() {
		return rDesc;
	}

	public void setrDesc(String rDesc) {
		this.rDesc = rDesc;
	}

	public String getrCallKeyAp() {
		return rCallKeyAp;
	}

	public void setrCallKeyAp(String rCallKeyAp) {
		this.rCallKeyAp = rCallKeyAp;
	}

	public String getrWorkSysCode() {
		return rWorkSysCode;
	}

	public void setrWorkSysCode(String rWorkSysCode) {
		this.rWorkSysCode = rWorkSysCode;
	}

	
	public HashMap<String,String> getAllItem() {
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();

		if(this.rRecDate != null && !this.rRecDate.trim().isEmpty()) allItem.put("rRecDate", this.rRecDate);
		if(this.rRecTime != null && !this.rRecTime.trim().isEmpty()) allItem.put("rRecTime", this.rRecTime);
		if(this.rExtNum != null && !this.rExtNum.trim().isEmpty()) allItem.put("rExtNum", this.rExtNum);
		if(this.rVSysCode != null && !this.rVSysCode.trim().isEmpty()) allItem.put("rVSysCode", this.rVSysCode);
		if(this.rRecStartType != null && !this.rRecStartType.trim().isEmpty()) allItem.put("rRecStartType", this.rRecStartType);
		if(this.rWorkType != null && !this.rWorkType.trim().isEmpty()) allItem.put("rWorkType", this.rWorkType);
		if(this.rResult != null && !this.rResult.trim().isEmpty()) allItem.put("rResult", this.rResult);
		if(this.rResultReason != null && !this.rResultReason.trim().isEmpty()) allItem.put("rResultReason", this.rResultReason);
		if(this.rRegDatetime != null && !this.rRegDatetime.trim().isEmpty()) allItem.put("rRegDatetime", this.rRegDatetime);
		if(this.rResvDatetime != null && !this.rResvDatetime.trim().isEmpty()) allItem.put("rResvDatetime", this.rResvDatetime);
		if(this.rProcStartDatetime != null && !this.rProcStartDatetime.trim().isEmpty()) allItem.put("rProcStartDatetime", this.rProcStartDatetime);
		if(this.rProcEndDatetime != null && !this.rProcEndDatetime.trim().isEmpty()) allItem.put("rProcEndDatetime", this.rProcEndDatetime);
		if(this.rProcEstTime != null && !this.rProcEstTime.trim().isEmpty()) allItem.put("rProcEstTime", this.rProcEstTime);
		if(this.rDesc != null && !this.rDesc.trim().isEmpty()) allItem.put("rDesc", this.rDesc);
		if(this.rCallKeyAp != null && !this.rCallKeyAp.trim().isEmpty()) allItem.put("rCallKeyAp", this.rCallKeyAp);
		if(this.rWorkSysCode != null && !this.rWorkSysCode.trim().isEmpty()) allItem.put("rWorkSysCode", this.rWorkSysCode);
		return allItem;
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rExtNum = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rVSysCode = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecStartType = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rWorkType = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rResult = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rResultReason = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRegDatetime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rResvDatetime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rProcStartDatetime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rProcEndDatetime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rProcEstTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDesc = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rCallKeyAp = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rWorkSysCode = setValues[i++]; }
	}
	
	@Override
	public String toString() {
		return "SttDataset [rRecDate=" + rRecDate
				+ ", rRecTime=" + rRecTime 
				+ ", rExtNum=" + rExtNum
				+ ", rVSysCode=" + rVSysCode
				+ ", rRecStartType=" + rRecStartType
				+ ", rWorkType=" + rWorkType
				+ ", rResult=" + rResult
				+ ", rResultReason=" + rResultReason
				+ ", rRegDatetime=" + rRegDatetime
				+ ", rResvDatetime=" + rResvDatetime
				+ ", rProcStartDatetime=" + rProcStartDatetime
				+ ", rProcEndDatetime=" + rProcEndDatetime
				+ ", rProcEstTime=" + rProcEstTime
				+ ", rDesc=" + rDesc
				+ ", rCallKeyAp=" + rCallKeyAp
				+ ", rWorkSysCode=" + rWorkSysCode
				+ "]";
	}
	
}
