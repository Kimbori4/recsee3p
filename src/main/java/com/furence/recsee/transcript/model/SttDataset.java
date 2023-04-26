package com.furence.recsee.transcript.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SttDataset {
	
	private String rDatasetName;
	private String rDatasetDate;
	private String rDatasetTime;
	private String rDatasetSize;
	
	private String sDate;
	private String eDate;
	private String sTime;
	private String eTime;

	public String getrDatasetName() {
		return rDatasetName;
	}

	public String getrDatasetDate() {
		return rDatasetDate;
	}

	public String getrDatasetTime() {
		return rDatasetTime;
	}

	public String getrDatasetSize() {
		return rDatasetSize;
	}

	public String getsDate() {
		return sDate;
	}

	public String geteDate() {
		return eDate;
	}

	public String getsTime() {
		return sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void setrDatasetName(String rDatasetName) {
		this.rDatasetName = rDatasetName;
	}

	public void setrDatasetDate(String rDatasetDate) {
		this.rDatasetDate = rDatasetDate;
	}

	public void setrDatasetTime(String rDatasetTime) {
		this.rDatasetTime = rDatasetTime;
	}

	public void setrDatasetSize(String rDatasetSize) {
		this.rDatasetSize = rDatasetSize;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public HashMap<String,String> getAllItem() {
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();

		if(this.rDatasetName != null && !this.rDatasetName.trim().isEmpty()) allItem.put("rDatasetName", this.rDatasetName);
		if(this.rDatasetDate != null && !this.rDatasetDate.trim().isEmpty()) allItem.put("rDatasetDate", this.rDatasetDate);
		if(this.rDatasetTime != null && !this.rDatasetTime.trim().isEmpty()) allItem.put("rDatasetTime", this.rDatasetTime);
		if(this.rDatasetSize != null && !this.rDatasetSize.trim().isEmpty()) allItem.put("rDatasetSize", this.rDatasetSize);
		return allItem;
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDatasetName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDatasetDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDatasetTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDatasetSize = setValues[i++]; }
	}
	
	@Override
	public String toString() {
		return "SttDataset [rDatasetName=" + rDatasetName
				+ ", rDatasetDate=" + rDatasetDate + ", rDatasetTime=" + rDatasetTime
				+ ", rDatasetSize=" + rDatasetSize + "]";
	}
	
}
