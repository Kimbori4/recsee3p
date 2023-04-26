package com.furence.recsee.scriptRegistration.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScriptCommonCodeVO {

	@JsonProperty("type")
	private String code;

	@JsonProperty("name")
	private String value;

	@JsonProperty("values")
	@JsonInclude(Include.NON_NULL)
	private List<ScriptCommonCodeVO> list;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void addListElement(ScriptCommonCodeVO commonCodeVO) {
		if (this.list == null) {
			this.list = new ArrayList<>();
		}
		this.list.add(commonCodeVO);
	}

	public void addListElement(int index, ScriptCommonCodeVO commonCodeVO) {
		if (this.list == null) {
			this.list = new ArrayList<>();
		}
		this.list.add(index, commonCodeVO);
	}

	public List<ScriptCommonCodeVO> getList() {
		return list;
	}

	public void setList(List<ScriptCommonCodeVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ScriptCommonCodeVO [code=" + code + ", value=" + value + ", list=" + list + "]";
	}

	

}
