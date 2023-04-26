package com.furence.recsee.wooribank.script.param.response;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiResponse implements Serializable{
	
	private static final long serialVersionUID = 1546950804264681014L;
	
	public enum RESULT{
		SUCCESS("Y"),
		FAIL("N");
		
		private String value;
		
		RESULT(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	@JsonProperty("success")
	private String success;
	
	@JsonProperty("result")
	private int resultCode;
	
	@JsonProperty("message")
	private String resultMessage;
	
	@JsonProperty("resData")
	private Map<String, Object> resultData;
	
	public void setResultCode(ResultCode resultCode) {
		this.success = (resultCode == ResultCode.SUCCESS) ? RESULT.SUCCESS.getValue() : RESULT.FAIL.getValue();
		this.resultCode = resultCode.getCode();
		this.resultMessage = resultCode.getMessage();
	}
	
}
