package com.furence.recsee.wooribank.facerecording.model;

import java.util.List;

public class ScriptStepListAndCallKey {
	
	private List<ScriptStepVo> stepList;
	
	private String callKey;

	public List<ScriptStepVo> getStepList() {
		return stepList;
	}

	public void setStepList(List<ScriptStepVo> stepList) {
		this.stepList = stepList;
	}

	public String getCallKey() {
		return callKey;
	}

	public void setCallKey(String callKey) {
		this.callKey = callKey;
	}

	@Override
	public String toString() {
		return "ScriptStepListAndCallKey [stepList=" + stepList + ", callKey=" + callKey + "]";
	}

	public ScriptStepListAndCallKey(List<ScriptStepVo> stepList, String callKey) {
		this.stepList = stepList;
		this.callKey = callKey;
	}
	
	

}
