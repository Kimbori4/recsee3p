package com.furence.recsee.transcript.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SttModel {
	
	private String rCreateDate;
	private String rCreateTime;
	private String rStartDate;
	private String rStartTime;
	private String rEndDate;
	private String rEndTime;
	private String rType;
	private String rModelName;
	private String rModelPath;
	private String rBaseModel;
	private String rBaseModelPath;
	private String rRecogAmModelPath;
	private String rRecogLmModelPath;
	private String rDataPath;
	private String rDataTotalCount;
	private String rDataTotalTime;
	private String rWorkStatus;
	private String rWorkStatusMsg;
	private String rRecogRate;
	private String rRecogRateMsg;
	private String rDatasetName;
	private String rApplyYn;
	
	private String sDate;
	private String eDate;
	private String sTime;
	private String eTime;
	
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

	public String getrCreateDate() {
		return rCreateDate;
	}

	public String getrCreateTime() {
		return rCreateTime;
	}

	public String getrStartDate() {
		return rStartDate;
	}

	public String getrStartTime() {
		return rStartTime;
	}

	public String getrEndDate() {
		return rEndDate;
	}

	public String getrEndTime() {
		return rEndTime;
	}

	public String getrType() {
		return rType;
	}

	public String getrModelName() {
		return rModelName;
	}

	public String getrModelPath() {
		return rModelPath;
	}

	public String getrBaseModel() {
		return rBaseModel;
	}

	public String getrBaseModelPath() {
		return rBaseModelPath;
	}

	public String getrDataPath() {
		return rDataPath;
	}

	public String getrDataTotalCount() {
		return rDataTotalCount;
	}

	public String getrDataTotalTime() {
		return rDataTotalTime;
	}

	public String getrWorkStatus() {
		return rWorkStatus;
	}

	public String getrWorkStatusMsg() {
		return rWorkStatusMsg;
	}

	public String getrRecogRate() {
		return rRecogRate;
	}

	public String getrRecogRateMsg() {
		return rRecogRateMsg;
	}

	public String getrDatasetName() {
		return rDatasetName;
	}

	public void setrCreateDate(String rCreateDate) {
		this.rCreateDate = rCreateDate;
	}

	public void setrCreateTime(String rCreateTime) {
		this.rCreateTime = rCreateTime;
	}

	public void setrStartDate(String rStartDate) {
		this.rStartDate = rStartDate;
	}

	public void setrStartTime(String rStartTime) {
		this.rStartTime = rStartTime;
	}

	public void setrEndDate(String rEndDate) {
		this.rEndDate = rEndDate;
	}

	public void setrEndTime(String rEndTime) {
		this.rEndTime = rEndTime;
	}

	public void setrType(String rType) {
		this.rType = rType;
	}

	public void setrModelName(String rModelName) {
		this.rModelName = rModelName;
	}

	public void setrModelPath(String rModelPath) {
		this.rModelPath = rModelPath;
	}

	public void setrBaseModel(String rBaseModel) {
		this.rBaseModel = rBaseModel;
	}

	public void setrBaseModelPath(String rBaseModelPath) {
		this.rBaseModelPath = rBaseModelPath;
	}

	public void setrDataPath(String rDataPath) {
		this.rDataPath = rDataPath;
	}

	public void setrDataTotalCount(String rDataTotalCount) {
		this.rDataTotalCount = rDataTotalCount;
	}

	public void setrDataTotalTime(String rDataTotalTime) {
		this.rDataTotalTime = rDataTotalTime;
	}

	public void setrWorkStatus(String rWorkStatus) {
		this.rWorkStatus = rWorkStatus;
	}

	public void setrWorkStatusMsg(String rWorkStatusMsg) {
		this.rWorkStatusMsg = rWorkStatusMsg;
	}

	public void setrRecogRate(String rRecogRate) {
		this.rRecogRate = rRecogRate;
	}

	public void setrRecogRateMsg(String rRecogRateMsg) {
		this.rRecogRateMsg = rRecogRateMsg;
	}

	public void setrDatasetName(String rDatasetName) {
		this.rDatasetName = rDatasetName;
	}
	
	public String getrApplyYn() {
		return rApplyYn;
	}

	public void setrApplyYn(String rApplyYn) {
		this.rApplyYn = rApplyYn;
	}
	public String getrRecogAmModelPath() {
		return rRecogAmModelPath;
	}

	public String getrRecogLmModelPath() {
		return rRecogLmModelPath;
	}

	public void setrRecogAmModelPath(String rRecogAmModelPath) {
		this.rRecogAmModelPath = rRecogAmModelPath;
	}

	public void setrRecogLmModelPath(String rRecogLmModelPath) {
		this.rRecogLmModelPath = rRecogLmModelPath;
	}

	public HashMap<String,String> getAllItem() {
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();

		if(this.rCreateDate != null && !this.rCreateDate.trim().isEmpty()) allItem.put("rCreateDate", this.rCreateDate);
		if(this.rCreateTime != null && !this.rCreateTime.trim().isEmpty()) allItem.put("rCreateTime", this.rCreateTime);
		if(this.rStartDate != null && !this.rStartDate.trim().isEmpty()) allItem.put("rStartDate", this.rStartDate);
		if(this.rStartTime != null && !this.rStartTime.trim().isEmpty()) allItem.put("rStartTime", this.rStartTime);
		if(this.rEndDate != null && !this.rEndDate.trim().isEmpty()) allItem.put("rEndDate", this.rEndDate);
		if(this.rEndTime != null && !this.rEndTime.trim().isEmpty()) allItem.put("rEndTime", this.rEndTime);
		if(this.rType != null && !this.rType.trim().isEmpty()) allItem.put("rType", this.rType);
		if(this.rModelName != null && !this.rModelName.trim().isEmpty()) allItem.put("rModelName", this.rModelName);
		if(this.rModelPath != null && !this.rModelPath.trim().isEmpty()) allItem.put("rModelPath", this.rModelPath);
		if(this.rBaseModel != null && !this.rBaseModel.trim().isEmpty()) allItem.put("rBaseModel", this.rBaseModel);
		if(this.rBaseModelPath != null && !this.rBaseModelPath.trim().isEmpty()) allItem.put("rBaseModelPath", this.rBaseModelPath);
		if(this.rRecogAmModelPath != null && !this.rRecogAmModelPath.trim().isEmpty()) allItem.put("rRecogAmModelPath", this.rRecogAmModelPath);
		if(this.rRecogLmModelPath != null && !this.rRecogLmModelPath.trim().isEmpty()) allItem.put("rRecogLmModelPath", this.rRecogLmModelPath);
		if(this.rDataPath != null && !this.rDataPath.trim().isEmpty()) allItem.put("rDataPath", this.rDataPath);
		if(this.rDataTotalCount != null && !this.rDataTotalCount.trim().isEmpty()) allItem.put("rDataTotalCount", this.rDataTotalCount);
		if(this.rDataTotalTime != null && !this.rDataTotalTime.trim().isEmpty()) allItem.put("rDataTotalTime", this.rDataTotalTime);
		if(this.rWorkStatus != null && !this.rWorkStatus.trim().isEmpty()) allItem.put("rWorkStatus", this.rWorkStatus);
		if(this.rWorkStatusMsg != null && !this.rWorkStatusMsg.trim().isEmpty()) allItem.put("rWorkStatusMsg", this.rWorkStatusMsg);
		if(this.rRecogRate != null && !this.rRecogRate.trim().isEmpty()) allItem.put("rRecogRate", this.rRecogRate);
		if(this.rRecogRateMsg != null && !this.rRecogRateMsg.trim().isEmpty()) allItem.put("rRecogRateMsg", this.rRecogRateMsg);
		if(this.rDatasetName != null && !this.rDatasetName.trim().isEmpty()) allItem.put("rDatasetName", this.rDatasetName);
		if(this.rApplyYn != null && !this.rApplyYn.trim().isEmpty()) allItem.put("rApplyYn", this.rApplyYn);
		return allItem;
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rCreateDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rCreateTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rStartDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rStartTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rEndDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rEndTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rType = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rModelName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rModelPath = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rBaseModel = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rBaseModelPath = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecogAmModelPath = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecogLmModelPath = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDataPath = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDataTotalCount = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDataTotalTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rWorkStatus = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rWorkStatusMsg = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecogRate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecogRateMsg = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDatasetName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rApplyYn = setValues[i++]; }
	}
	
	@Override
	public String toString() {
		return "SttModel [rCreateDate=" + rCreateDate + ", rCreateTime=" + rCreateTime
				+ ", rStartDate=" + rStartDate + ", rStartTime=" + rStartTime + ", rEndDate="
				+ rEndDate + ", rEndTime=" + rEndTime + ", rType=" + rType
				+ ", rModelName=" + rModelName + ", rModelPath=" + rModelPath + ", rBaseModel=" + rBaseModel
				+ ", rBaseModelPath=" + rBaseModelPath + ", rRecogAmModelPath=" + rRecogAmModelPath
				+ ", rRecogLmModelPath=" + rRecogLmModelPath + ", rDataPath=" + rDataPath
				+ ", rDataTotalCount=" + rDataTotalCount + ", rDataTotalTime=" + rDataTotalTime
				+ ", rWorkStatus=" + rWorkStatus + ", rWorkStatusMsg=" + rWorkStatusMsg
				+ ", rRecogRate=" + rRecogRate + ", rRecogRateMsg=" + rRecogRateMsg
				+ ", rDatasetName=" + rDatasetName + ", rApplyYn=" + rApplyYn + "]";
	}
	
}
