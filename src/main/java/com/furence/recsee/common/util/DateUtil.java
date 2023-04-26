package com.furence.recsee.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.initech.shttp.server.Logger;

public class DateUtil {
	
	// DateUtil 인스턴스 생성 불가
	private DateUtil(){}
	
	private static String DEFAULT_FORMAT = "yyyyMMddHHmmss";
	private static DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat(DEFAULT_FORMAT);
	
	/**
	 * 기본 데이트 포멧 형태의 문자열로부터 Date타입의 인스턴스를 반환한다. 
	 * 
	 * @param strDate 날짜형식의 문자열
	 * @return Date 타입의 인스턴스
	 */
	public static Date toDate(String strDate) {
		return toDate(strDate, DEFAULT_FORMAT);
	}
	
	/**
	 * 지정 데이트 포멧 형태의 문자열로부터 Date타입의 인스턴스를 반환한다. 
	 * 
	 * @param strDate 날짜형식의 문자열
	 * @param format strDate의 포멧
	 * @return Date 타입의 인스턴스
	 */
	public static Date toDate(String strDate, String format) {
		
		DateFormat dateFormat = DEFAULT_DATEFORMAT;
		if (!StringUtil.isNull(format, true) && !DEFAULT_FORMAT.equals(format)) {
			dateFormat = new SimpleDateFormat(format);	
		}
		
		return toDate(strDate, dateFormat);
	}
	
	/**
	 * 지정 데이트 포멧을 이용하여 지정 데이트 포멧의 문자열로부터 Date타입의 인스턴스를 반환한다. 
	 * 
	 * @param strDate 날짜형식의 문자열
	 * @param dateFormat DateFormat인스턴슨
	 * @return Date 타입의 인스턴스
	 */
	public static Date toDate(String strDate, DateFormat dateFormat) {
			try {
				return dateFormat.parse(strDate);
			} catch (ParseException e) {
				Logger.error("", "", "", e.toString());
				
				return null;
			}
	}
	
	/**
	 * Date타입의 인스턴스로 부터 기본 포멧의 날짜를 반환한다. 
	 * 
	 * @param date Date타입의 인스턴스
	 * @return 날짜형식의 문자열
	 */
	public static String toString(Date date) {
		return toString(date, DEFAULT_FORMAT);
	}
	
	/**
	 * Date타입의 인스턴스로 부터 지정 포멧의 날짜를 반환한다. 
	 * 
	 * @param date Date타입의 인스턴스
	 * @param format 지정 날짜 포멧
	 * @return 날짜형식의 문자열
	 */
	public static String toString(Date date, String format) {
		DateFormat dateFormat = DEFAULT_DATEFORMAT;
		if (!StringUtil.isNull(format, true) && !DEFAULT_FORMAT.equals(format)) {
			dateFormat = new SimpleDateFormat(format);	
		}
		return toString(date, dateFormat);
	}
	
	/**
	 * Date타입의 인스턴스로 부터 지정 포멧의 날짜를 반환한다. 
	 * 
	 * @param date Date타입의 인스턴스
	 * @param dateFormat DateFormat인스턴스
	 * @return 날짜형식의 문자열
	 */
	public static String toString(Date date, DateFormat dateFormat) {
		return dateFormat.format(date);
	}
	
	/**
	 * 두 날짜의 시간차이를 구한다(초). 
	 * 
	 * @param beginDate 시작시간
	 * @param endDate 끝시간
	 * @return 시간차(초)
	 */
	public static long diffOfSecond(String beginDate, String endDate) {
		Date beginDt = toDate(beginDate, DEFAULT_DATEFORMAT);
		Date endDt = toDate(endDate, DEFAULT_DATEFORMAT);
		
		return diffOfSecond(beginDt, endDt);
	}
	
	/**
	 * 두 날짜의 시간차이를 구한다(초). 
	 * 
	 * @param beginDate 시작시간
	 * @param endDate 끝시간
	 * @return 시간차(초)
	 */
	public static long diffOfSecond(Date beginDt, Date endDt) {
		return (endDt.getTime() / 1000) - (beginDt.getTime() / 1000);
	}
	
	/**
	 * @param diffOfTime 시간차(초)
	 * @return 시간차 포멧 (시간:분:초)
	 */
	public static String diffOfTime(long diffOfTime) {
		
		long diffHour = (diffOfTime / 60 / 60);
		long diffMin = (diffOfTime  / 60) % 60;
		long diffSec = diffOfTime % 60;
		
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtil.lpad(diffHour, 2, "0")).append(":");
		sb.append(StringUtil.lpad(diffMin, 2, "0")).append(":");
		sb.append(StringUtil.lpad(diffSec, 2, "0"));
		
		return sb.toString();
	}
	
	/**
	 * @param date 비교할 날짜
	 * @param sDate 비교 시작일
	 * @param eDate 비교 종료일
	 * @return 비교할 날짜가 시작일과 종료일 포함 사이에 있을 시 true / false
	 */
	public static boolean isBetweenDate(Date date, Date sDate, Date eDate) {
		if(date != null && sDate != null && eDate != null) {
			
			Calendar sCal = Calendar.getInstance();
			Calendar eCal = Calendar.getInstance();
			Calendar cal = Calendar.getInstance();
			sCal.setTime(sDate);
			eCal.setTime(eDate);
			cal.setTime(date);
			
			sCal.add(Calendar.DATE, -1);
			eCal.add(Calendar.DATE, +1);

			if(cal.after(sCal) && cal.before(eCal)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * @param Date date 구하려는 날짜
	 * @param Integer add 더하려는 일자
	 * @return add일 뒤(전)의 날짜
	 */
	public static Date calcDate(Date date, String ymd ,int calcValue) {
		if((date != null) && (!StringUtil.isNull(ymd,true))) {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			switch(ymd) {
				case "year":
					cal.add(Calendar.YEAR, calcValue);
					break;
				case "month":
					cal.add(Calendar.MONTH, calcValue);
					break;
				case "day":
				default:
					cal.add(Calendar.DATE, calcValue);
					break;
			}

			return new Date(cal.getTimeInMillis());
		}
		return date;
	}
}