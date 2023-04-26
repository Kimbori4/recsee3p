package com.furence.recsee.common.util;

import java.math.BigDecimal;

public class NumberUtil {
	/** NumberUtil 인스턴스 생성 불가 */
	private NumberUtil() {};
	
	/**
	 * 지정 문자열을 Integer형으로 변환한다.
	 * @param num 문자열
	 * @return Integer형
	 */
	public static Integer toInteger(String num) {
		return toInteger(num, null);
	}
	
	/**
	 * 지정 문자열을 Integer형으로 변환한다.
	 * @param num 문자열
	 * @param defVal 기본값(num이 null또는 숫자로 변환할 수 없는 문자열 일 경우,반환할 값)
	 * @return Integer형
	 */
	public static Integer toInteger(String num, Integer defVal) {
		try {
			return Integer.valueOf(num);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	/**
	 * 지정 문자열을 Long형으로 변환한다.
	 * @param num 문자열
	 * @return Long형
	 */
	public static Long toLong(String num) {
		return toLong(num, null);
	}
	/**
	 * 지정 문자열을 Long형으로 변환한다.
	 * @param num 문자열
	 * @param defVal 기본값(num이 null또는 숫자로 변환할 수 없는 문자열 일 경우,반환할 값)
	 * @return Long형
	 */
	public static Long toLong(String num, Long defVal) {
		try {
			return Long.valueOf(num);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	/**
	 * 지정 문자열을 Double형으로 변환한다.
	 * @param num 문자열
	 * @return Double형
	 */
	public static Double toDouble(String num) {
		return toDouble(num, null);
	}
	/**
	 * 지정 문자열을 Double형으로 변환한다.
	 * @param num 문자열
	 * @param defVal 기본값(num이 null또는 숫자로 변환할 수 없는 문자열 일 경우,반환할 값)
	 * @return Long형
	 */
	public static Double toDouble(String num, Double defVal) {
		try {
			return Double.valueOf(num);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	/**
	 * 지정 문자열을 BigDecimal형으로 변환한다.
	 * @param num 문자열
	 * @return BigDecimal형
	 */
	public static BigDecimal toBigDecimal(String num) {
		return toBigDecimal(num, null);
	}
	/**
	 * 지정 문자열을 BigDecimal형으로 변환한다.
	 * @param num 문자열
	 * @param defVal 기본값(num이 null또는 숫자로 변환할 수 없는 문자열 일 경우,반환할 값)
	 * @return BigDecimal형
	 */
	public static BigDecimal toBigDecimal(String num, BigDecimal defVal) {
		try {
			if ("0".equals(num)) {
				return BigDecimal.ZERO;
			} else if ("1".equals(num)) {
				return BigDecimal.ONE;
			} else if ("10".equals(num)) {
				return BigDecimal.TEN;
			}
			return new BigDecimal(num);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
}
