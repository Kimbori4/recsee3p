package com.furence.recsee.wooribank.script.repository.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@ToString
public class CommonCode {

	@JsonProperty("type")
	private String code;

	@JsonProperty("name")
	private String value;

	@JsonProperty("values")
	@JsonInclude(Include.NON_NULL)
	private List<CommonCode> list;


	public void addListElement(CommonCode commonCodeVO) {
		if (this.list == null) {
			this.list = new ArrayList<>();
		}
		this.list.add(commonCodeVO);
	}

	public void addListElement(int index, CommonCode commonCodeVO) {
		if (this.list == null) {
			this.list = new ArrayList<>();
		}
		this.list.add(index, commonCodeVO);
	}

	
}
