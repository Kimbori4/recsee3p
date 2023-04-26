package com.furence.recsee.wooribank.facerecording.model;

public class ScriptStepVo {
	
	private int rScriptStepPk;
	
	private int rScriptStepFk;
	
	private int rScriptStepParent;
	
	private String rScriptStepName;
	
	private int rScriptStepOrder;
	
	private String rScriptStepType;
	
	private String rUseYn;

	public int getrScriptStepPk() {
		return rScriptStepPk;
	}

	public void setrScriptStepPk(int rScriptStepPk) {
		this.rScriptStepPk = rScriptStepPk;
	}

	public int getrScriptStepFk() {
		return rScriptStepFk;
	}

	public void setrScriptStepFk(int rScriptStepFk) {
		this.rScriptStepFk = rScriptStepFk;
	}

	public int getrScriptStepParent() {
		return rScriptStepParent;
	}

	public void setrScriptStepParent(int rScriptStepParent) {
		this.rScriptStepParent = rScriptStepParent;
	}

	public String getrScriptStepName() {
		return rScriptStepName;
	}

	public void setrScriptStepName(String rScriptStepName) {
		this.rScriptStepName = rScriptStepName;
	}

	public int getrScriptStepOrder() {
		return rScriptStepOrder;
	}

	public void setrScriptStepOrder(int rScriptStepOrder) {
		this.rScriptStepOrder = rScriptStepOrder;
	}

	public String getrScriptStepType() {
		return rScriptStepType;
	}

	public void setrScriptStepType(String rScriptStepType) {
		this.rScriptStepType = rScriptStepType;
	}

	public String getrUseYn() {
		return rUseYn;
	}

	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}

	@Override
	public String toString() {
		return "ScriptStepVo [rScriptStepPk=" + rScriptStepPk + ", rScriptStepFk=" + rScriptStepFk
				+ ", rScriptStepParent=" + rScriptStepParent + ", rScriptStepName=" + rScriptStepName
				+ ", rScriptStepOrder=" + rScriptStepOrder + ", rScriptStepType=" + rScriptStepType + ", rUseYn="
				+ rUseYn + "]";
	}

	
	
	

}
