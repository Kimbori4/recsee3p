package com.furence.recsee.wooribank.script.param.response;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AJaxResult implements Serializable{
	
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
	private final String success;
	
	@JsonProperty("result")
	private final int resultCode;
	
	@JsonProperty("message")
	private final String resultMessage;
	
	@JsonProperty("resData")
	private Map<String, Object> resultData;
	
	public static class Builder{
		
		private RESULT isSuccess = RESULT.FAIL;
		
		private int resultCode = 0;		
		
		private String resultMessage = "";
		
		private Map<String, Object> resultData;
		
		public Builder(ResultCode resultCode) {
			this.isSuccess = (resultCode == ResultCode.SUCCESS) ? RESULT.SUCCESS : RESULT.FAIL;
			this.resultCode = resultCode.getCode();
			this.resultMessage = resultCode.getMessage();
		}
		
		public Builder(boolean isSuccess) {
			isSuccess(isSuccess);
		}
		
		public Builder isSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess ? RESULT.SUCCESS : RESULT.FAIL;
			return this;
		}
		
		public Builder resultCode(int resultCode) {
			this.resultCode = resultCode;
			return this;
		}
		
		public Builder resultMessage(String resultMessage) {
			this.resultMessage = resultMessage;
			return this;
		}
		
		public Builder resultCode(ResultCode resultCode) {
			this.resultCode = resultCode.getCode();
			this.resultMessage = resultCode.getMessage();
			return this;
		}
		
		private Map<String, Object> getResultMap(){
			if(null == this.resultData) {
				this.resultData = new LinkedHashMap<String, Object>();
			}
			return this.resultData;
		}	
		
		public Builder attribute(String key, Object value) {
			getResultMap().put(key, value);
			return this;
		}
		
		public Builder attribute(Map<String, Object> attrs) {
			getResultMap().putAll(attrs);
			return this;
		}
		
		public AJaxResult build() {
			return new AJaxResult(this);
		}
	}
	
	private AJaxResult(Builder builder) {
		this.success = builder.isSuccess.getValue();
		this.resultCode = builder.resultCode;
		this.resultMessage = builder.resultMessage;
		if( null != builder.resultData ) {
			this.resultData = new LinkedHashMap<String, Object>();
			this.resultData.putAll(builder.resultData);	
		}
	}
	
	public AJaxResult() {
		this.success = null;
		this.resultCode = 0;
		this.resultMessage = null;
	}
}
