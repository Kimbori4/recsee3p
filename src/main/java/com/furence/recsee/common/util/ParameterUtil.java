package com.furence.recsee.common.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ParameterUtil {

	/**
	 * @function getAllParameter
	 * @description
	 * 		request의 모든 parameter를 Map에 담아 받는다
	 * @param request
	 * @return Map (parameter)
	 */
	public static Map<String, Object> getAllParameter(HttpServletRequest request) {
		return getAllParameter(request.getParameterMap());
	}
	public static Map<String, Object> getAllParameter(Map<String, Object> getParameterMap) {
		JSONObject param = new JSONObject();
		for(String key: getParameterMap.keySet()) {
			Object[] v = (Object[]) getParameterMap.get(key);
			param.put(key, v[0]);
		}
		return param;
	}
}
