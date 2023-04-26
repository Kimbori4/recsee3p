package com.furence.recsee.transcript.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TranscriptMap {
	
	private String rDatasetName;
	private String rTranscriptSerial;
	
	public String getrDatasetName() {
		return rDatasetName;
	}

	public String getrTranscriptSerial() {
		return rTranscriptSerial;
	}

	public void setrDatasetName(String rDatasetName) {
		this.rDatasetName = rDatasetName;
	}

	public void setrTranscriptSerial(String rTranscriptSerial) {
		this.rTranscriptSerial = rTranscriptSerial;
	}

	public HashMap<String,String> getAllItem() {
		
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();

		allItem.put("rDatasetName", this.rDatasetName);
		allItem.put("rTranscriptSerial", this.rTranscriptSerial);
		
		return allItem;
		
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rDatasetName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rTranscriptSerial = setValues[i++]; }
		
	}
	
	
	@Override
	public String toString() {
		
		return "TranscriptMap [rDatasetName=" + rDatasetName + ", rTranscriptSerial=" + rTranscriptSerial + "]";
		
	}
	
	
	
	
}
