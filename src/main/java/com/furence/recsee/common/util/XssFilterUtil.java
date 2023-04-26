package com.furence.recsee.common.util;

public class XssFilterUtil {

	//보안권고 xss공격 막기
	public static String XssFilter(String value) {
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
   //   value = value.replaceAll("script", "");        
        value = value.replaceAll("`", "");
        return value;
	}
}
