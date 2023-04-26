package com.furence.recsee.common.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class AJaxResBuilder{
	
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
		
	private String success;
	
	private String result;
	
	private Map<String, Object> resultMap;
	
	private AJaxResBuilder() {}
	
	private AJaxResBuilder(RESULT res) {
		this.success = res.getValue();
	}
	
	public static AJaxResBuilder builder(RESULT res) {		
		return new AJaxResBuilder(res);
	}
	
	public AJaxResBuilder result(String result) {
		this.result = result;
		return this;
	}
	
	public AJaxResBuilder success(RESULT res) {
		this.success = res.getValue();
		return this;
	}

	private Map<String, Object> getResultMap(){
		if(null == this.resultMap) {
			this.resultMap = new LinkedHashMap<String, Object>();
		}
		return this.resultMap;
	}	
	
	public AJaxResBuilder attribute(String key, Object value) {
		getResultMap().put(key, value);
		return this;
	}
	
	public AJaxResBuilder attribute(Map<String, Object> attrs) {
		getResultMap().putAll(attrs);
		return this;
	}
	

	public AJaxResBuilder message(String message) {
		getResultMap().put("msg", message);
		return this;
	}
	
	public AJaxResVO build() {
		AJaxResVO res = new AJaxResVO();
		res.setSuccess(this.success);
		if( null != this.result) res.setResult(result);
		if( null != this.resultMap ) {
			res.addAttribute(this.resultMap);
		}
		
		return res;
	}
	
	
}
