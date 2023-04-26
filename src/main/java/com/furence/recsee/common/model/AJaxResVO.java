package com.furence.recsee.common.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.furence.recsee.common.util.StringUtil;

public class AJaxResVO implements Serializable {

	private static final long serialVersionUID = 1546950804264681014L;
	public static final String SUCCESS_Y = "Y";
	public static final String SUCCESS_N = "N";

	/** AJax처리 중 에러가 발생 했는지 판단하는 플래그 ('Y':에러 없음, 'N':에러발생)*/
	private String success = "Y";

	/** 처리결과가 어떻게 됬는지 판단하는 플래그로 필요에 따라 사용함 (기본값:'0')*/
	private String result = "0";

	/** AJax처리 후 반환할 데이터 */
	private Map<String, Object> resData = new LinkedHashMap<String, Object>();

	public Map<String, Object> addAttribute(String name, Object value) {
		if (!StringUtil.isNull(name, true)) {
			resData.put(name, value);
		}
		return resData;
	}

	public Map<String, Object> addAttribute(Map<String, Object> attr) {
		if (attr != null && !attr.isEmpty()) {
			resData.putAll(attr);
		}
		return resData;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, Object> getResData() {
		return resData;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AJaxResVO [success=");
		builder.append(success);
		builder.append(", result=");
		builder.append(result);
		builder.append(", resData=");
		builder.append(resData);
		builder.append("]");
		return builder.toString();
	}
	
	
}
