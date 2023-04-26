package com.furence.recsee.transcript.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TranscriptModel {
	
	private String rLearningModel;
	private String rModelDate;
	private String rModelTime;
	private String rModelSize;
	
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

	public String getrLearningModel() {
		return rLearningModel;
	}

	public String getrModelDate() {
		return rModelDate;
	}

	public String getrModelTime() {
		return rModelTime;
	}

	public String getrModelSize() {
		return rModelSize;
	}

	public void setrLearningModel(String rLearningModel) {
		this.rLearningModel = rLearningModel;
	}

	public void setrModelDate(String rModelDate) {
		this.rModelDate = rModelDate;
	}

	public void setrModelTime(String rModelTime) {
		this.rModelTime = rModelTime;
	}

	public void setrModelSize(String rModelSize) {
		this.rModelSize = rModelSize;
	}

	public HashMap<String,String> getAllItem() {
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();

		if(this.rLearningModel != null && !this.rLearningModel.trim().isEmpty()) allItem.put("rLearningModel", this.rLearningModel);
		if(this.rModelDate != null && !this.rModelDate.trim().isEmpty()) allItem.put("rModelDate", this.rModelDate);
		if(this.rModelTime != null && !this.rModelTime.trim().isEmpty()) allItem.put("rModelTime", this.rModelTime);
		if(this.rModelSize != null && !this.rModelSize.trim().isEmpty()) allItem.put("rModelSize", this.rModelSize);
		return allItem;
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rLearningModel = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rModelDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rModelTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rModelSize = setValues[i++]; }
	}
	
	@Override
	public String toString() {
		return "TranscriptModel [rLearningModel=" + rLearningModel
				+ ", rModelDate=" + rModelDate + ", rModelTime=" + rModelTime
				+ ", rModelSize=" + rModelSize + "]";
	}
	
}
