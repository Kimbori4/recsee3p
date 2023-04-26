package com.furence.recsee.wooribank.script.param.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultBase {

	private int code;
	
	private String message;
	
	private String userData;
	
	public ResultBase() { }
	
	private ResultBase(int code) { 
		this.code = code;
	}
	
	private ResultBase(int code, String message) { 
		this.code = code;
		this.message = message;
	}
	
	private ResultBase(int code, String message, String userData) { 
		this.code = code;
		this.message = message;
		this.userData = userData;
	}
	
	public static ResultBase valueOf(int code) {
		return new ResultBase(code);
	}
	
	public static ResultBase valueOf(int code, String message) {
		return new ResultBase(code, message);
	}
	
	public static ResultBase valueOf(int code, String message, String userData) {
		return new ResultBase(code, message, userData);
	}
	
	public static ResultBase from(ResultCode resultCode) {
		return new ResultBase(resultCode.getCode(), resultCode.getMessage());
	}
	
	public static ResultBase from(ResultCode resultCode, String ext) {
		return new ResultBase(resultCode.getCode(), resultCode.getMessage(), ext);
	}
	
	public ResultCode toResucltCode() {
		return ResultCode.create(this.code);
	}
	
}
