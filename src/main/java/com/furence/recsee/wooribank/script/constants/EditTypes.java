package com.furence.recsee.wooribank.script.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EditTypes{
	Create("C"),
	Update("U"),
	Delete("D");
	
	private String value = "";
	
	EditTypes(String s){
		this.value = s;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@JsonCreator
	public static EditTypes create(String value) {
		for(EditTypes type: EditTypes.values()) {
			if(type.getValue().toLowerCase().equals(value.toLowerCase()) 
				|| type.name().toLowerCase().equals(value.toLowerCase())) {
				return type;
			}
		}
		return null;
	}
}
