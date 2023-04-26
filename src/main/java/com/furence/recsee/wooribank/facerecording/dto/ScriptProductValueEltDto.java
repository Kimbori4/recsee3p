package com.furence.recsee.wooribank.facerecording.dto;



import java.util.Arrays;
import java.util.List;

import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;

public class ScriptProductValueEltDto  extends scriptProductValueInfo {
	
	private List<String> rsProductValueValList;
		
	public ScriptProductValueEltDto(scriptProductValueInfo info) {
		this.setRsProductCode(info.getRsProductCode());
		this.setRsProductType(info.getRsProductType());
		this.setRsProductValueCode(info.getRsProductValueCode());
		this.setRsProductValueName(info.getRsProductValueName());
		this.setRsProductValuePk(info.getRsProductValuePk());
		this.setRsProductValueRealtimeTTS(info.getRsProductValueRealtimeTTS());
		this.setRsProductValueVal(info.getRsProductValueVal());
		if(this.getRsProductValueVal() != null) {
			this.rsProductValueValList = Arrays.asList(this.getRsProductValueVal().split("\\^"));
		}
	}

	public List<String> getRsProductValueValList() {
		return rsProductValueValList;
	}

	public void setRsProductValueValList(List<String> rsProductValueValList) {
		this.rsProductValueValList = rsProductValueValList;
	}

	@Override
	public String toString() {
		return "ScriptProductValueEltDto [rsProductValueValList=" + rsProductValueValList + "]";
	}

	
	
		
}
