package com.furence.recsee.common.util;


public class StringUtil {

	/**
	 * 지정 문자열이 NULL인지 체크
	 * @param str 지정 문자열
	 * @return 지정 문자열이 null일 경우
	 */
	public static boolean isNull(String str) {
		return isNull(str, false);
	}
	
	/**
	 * 지정 문자열이 NULL 또는  공백문자 제거 후 빈문자열 인지 체크
	 * @param str 지정 문자열
	 * @param withTrim 좌우 공백 제거 후 체크 널 체크를 할지 지정
	 * @return 지정 문자열이 null 또는 공백일 경우 true를 반환
	 */
	public static boolean isNull(String str, boolean withTrim) {
		boolean result = (str == null) ? true : false;

		if (!result && withTrim) {
			result = (str.trim().length() == 0) ? true : false;
		}
		return result;
	}
	
	
	/**
	 * 지정 오브젝트를 문자열 타입으로 반환한다.
	 * 
	 * @param obj
	 * @return 지정 오브젝트의 문자열 타입
	 */
	public static String toString(Object obj) {
		return toString(obj, "");
	}
	
	
	/**
	 * 지정 오브젝트를 문자열 타입으로 반환한다.
	 * 
	 * @param obj 지정 오브젝트
	 * @param defaultStr 기본 문자열
	 * @return 지정 오브젝트의 문자열 타입
	 */
	public static String toString(Object obj, String defaultStr) {
		return toString(obj, defaultStr, false);
	}
	
	/**
	 * 지정 오브젝트를 문자열 타입으로 반환한다.
	 * 
	 * @param obj 지정 오브젝트
	 * @param defaultStr 기본 문자열
	 * @param withTrim 공백제거 유무
	 * @return 지정 오브젝트의 문자열 타입
	 */
	public static String toString(Object obj, String defaultStr, boolean withTrim) {
		String retVal = "";
		if (obj == null) {
			// 지정 오브젝트가 NULL일 경우 기본문자열을 반환한다
			retVal = defaultStr;
		} else {
			if (obj instanceof String) {
				retVal = (String) obj;
				
			} else if (obj instanceof java.sql.Timestamp) {
				retVal = DateUtil.toString((java.util.Date)obj);
				
			} else if (obj instanceof java.sql.Date) {
				retVal = DateUtil.toString((java.util.Date)obj);
				
			} else if (obj instanceof java.util.Date) {
				retVal = DateUtil.toString((java.util.Date)obj);

			} else {
				retVal =  obj.toString();
			}
		}
		
		if (withTrim) {
			retVal = retVal.trim();
		}
		
		return retVal;
	}
	
	
	/**
	 * 문자열에 지정 문자를 왼쪽에 채워서 지정 자리수의 문자로 반환한다.
	 * @param str 문자열
	 * @param length 자리수
	 * @param padChar 왼쪽에 채울 문자
	 * @return 지정된 자리수로 변환된 문자열
	 */
	public static String lpad(String str, int length, String padChar) {
		if (isNull(str)) {
			str = "";
		}
		
		StringBuilder sb = new StringBuilder();
		int padLen = length - str.length();
		for (int i = 0; i < padLen; i++) {
			sb.append(padChar);
		}
		sb.append(str);
		
		return sb.toString();
	}
	
	/**
	 * 숫자에 지정 문자를 왼쪽에 채워서 지정 자리수의 문자로 반환한다.
	 * @param num 숫자
	 * @param length 자리수
	 * @param padChar 왼쪽에 채울 문자
	 * @return 지정된 자리수로 변환된 문자열
	 */
	public static String lpad(Number num, int length, String padChar) {
		String str = "";
		if (num != null) {
			str = lpad(num.toString(), length, padChar);
		}
		
		return str;
	}
	
	/**
	 * 문자열에 지정 문자를 왼쪽에 채워서 지정 자리수의 문자로 반환한다.
	 * @param str 문자열
	 * @param length 자리수
	 * @param padChar 왼쪽에 채울 문자
	 * @return 지정된 자리수로 변환된 문자열
	 */
	public static String rpad(String str, int length, String padChar) {
		if (isNull(str)) {
			str = "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(str);
		int padLen = length - str.length();
		for (int i = 0; i < padLen; i++) {
			sb.append(padChar);
		}
		
		return sb.toString();
	}
	
	/**
	 * 숫자에 지정 문자를 오른쪽에 채워서 지정 자리수의 문자로 반환한다.
	 * @param num 숫자
	 * @param length 자리수
	 * @param padChar 왼쪽에 채울 문자
	 * @return 지정된 자리수로 변환된 문자열
	 */
	public static String rpad(Number num, int length, String padChar) {
		String str = "";
		if (num != null) {
			str = lpad(num.toString(), length, padChar);
		}
		
		return str;
	}
}
