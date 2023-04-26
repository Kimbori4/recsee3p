package com.furence.recsee.scriptRegistration.model;

public class ScriptStepDetailDTO {

	private int rScriptStepPk;
	private int rScriptStepFk;
	private String rProductType;
	private String rProductCode;
	private String rUseYn = "Y";

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

	public String getrProductType() {
		return rProductType;
	}

	public void setrProductType(String rProductType) {
		this.rProductType = rProductType;
	}

	public String getrProductCode() {
		return rProductCode;
	}

	public void setrProductCode(String rProductCode) {
		this.rProductCode = rProductCode;
	}

	public String getrUseYn() {
		return rUseYn;
	}

	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}

	@Override
	public String toString() {
		return "ScriptStepDetailDTO [rScriptStepPk=" + rScriptStepPk + ", rScriptStepFk=" + rScriptStepFk
				+ ", rProductType=" + rProductType + ", rProductCode=" + rProductCode + ", rUseYn=" + rUseYn + "]";
	}
	
	
}
