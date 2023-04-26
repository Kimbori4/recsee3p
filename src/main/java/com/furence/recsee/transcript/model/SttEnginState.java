package com.furence.recsee.transcript.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SttEnginState {
	
	private String rSysCode;
	private String rUpdateReqTime;
	private String rUpdateProcTime;
	private String rModelName;
	private String rType;
	private String rWorkState;
	
	private String rSysName;
	private String rSysIp;
	
	public String getrSysCode() {
		return rSysCode;
	}

	public String getrUpdateReqTime() {
		return rUpdateReqTime;
	}

	public String getrUpdateProcTime() {
		return rUpdateProcTime;
	}

	public String getrModelName() {
		return rModelName;
	}

	public String getrType() {
		return rType;
	}

	public String getrWorkState() {
		return rWorkState;
	}

	public String getrSysName() {
		return rSysName;
	}

	public String getrSysIp() {
		return rSysIp;
	}

	public void setrSysCode(String rSysCode) {
		this.rSysCode = rSysCode;
	}

	public void setrUpdateReqTime(String rUpdateReqTime) {
		this.rUpdateReqTime = rUpdateReqTime;
	}

	public void setrUpdateProcTime(String rUpdateProcTime) {
		this.rUpdateProcTime = rUpdateProcTime;
	}

	public void setrModelName(String rModelName) {
		this.rModelName = rModelName;
	}

	public void setrType(String rType) {
		this.rType = rType;
	}

	public void setrWorkState(String rWorkState) {
		this.rWorkState = rWorkState;
	}

	public void setrSysName(String rSysName) {
		this.rSysName = rSysName;
	}

	public void setrSysIp(String rSysIp) {
		this.rSysIp = rSysIp;
	}
	
	public HashMap<String,String> getAllItem() {
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();
		if(this.rSysCode != null && !this.rSysCode.trim().isEmpty()) allItem.put("rSysCode", this.rSysCode);
		if(this.rUpdateReqTime != null && !this.rUpdateReqTime.trim().isEmpty()) allItem.put("rUpdateReqTime", this.rUpdateReqTime);
		if(this.rUpdateProcTime != null && !this.rUpdateProcTime.trim().isEmpty()) allItem.put("rUpdateProcTime", this.rUpdateProcTime);
		if(this.rModelName != null && !this.rModelName.trim().isEmpty()) allItem.put("rModelName", this.rModelName);
		if(this.rType != null && !this.rType.trim().isEmpty()) allItem.put("rType", this.rType);
		if(this.rWorkState != null && !this.rWorkState.trim().isEmpty()) allItem.put("rWorkState", this.rWorkState);
		if(this.rSysIp != null && !this.rSysIp.trim().isEmpty()) allItem.put("rSysIp", this.rSysIp);
		if(this.rSysName != null && !this.rSysName.trim().isEmpty()) allItem.put("rSysName", this.rSysName);
		return allItem;
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rSysCode = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rUpdateReqTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rUpdateProcTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rModelName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rType = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rWorkState = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rSysIp = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rSysName = setValues[i++]; }
	}
	
	@Override
	public String toString() {
		return "SttEnginState [rSysCode=" + rSysCode + ", rUpdateReqTime=" + rUpdateReqTime
				+ ", rUpdateProcTime=" + rUpdateProcTime + ", rModelName=" + rModelName
				+ ", rType=" + rType + ", rWorkState=" + rWorkState
				+ ", rSysIp=" + rSysIp + ", rSysName=" + rSysName + "]";
	}
	
}
