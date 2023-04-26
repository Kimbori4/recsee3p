package com.furence.recsee.wooribank.script.service.file.types;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FileType {
	EXCEL("excel"){
		@Override
		public <T1,T2> File addFileMaker(FileMakerFuntion<T1,T2> maker, T1 data, T2 param) {
			return maker.makeFile(data, param);
		}
		
	},
	PDF("pdf"){
		@Override
		public <T1,T2> File addFileMaker(FileMakerFuntion<T1,T2> maker, T1 data, T2 param) {
			return maker.makeFile(data, param);
		}
	};
	
	private String value = "";
	
	FileType(String s){
		this.value = s;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@JsonCreator
	public static FileType create(String value) {
		
		if (value == null) return null;
		
		for(FileType type: FileType.values()) {
			if(type.getValue().toLowerCase().equals(value.toLowerCase()) 
				|| type.name().toLowerCase().equals(value.toLowerCase())) {
				return type;
			}
		}
		return null;
	}

	public abstract <T1,T2> File addFileMaker(FileMakerFuntion<T1,T2> maker, T1 data, T2 param); 
}
