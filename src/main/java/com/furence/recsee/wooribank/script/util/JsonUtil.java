package com.furence.recsee.wooribank.script.util;

import com.fasterxml.jackson.databind.ObjectMapper;

final public class JsonUtil {
	
	private JsonUtil() {}
	
	public static <T> T strToObject(String str, Class<T> clazz) throws Exception{
		if(str == null) return null;
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(str, clazz);
	}
}
