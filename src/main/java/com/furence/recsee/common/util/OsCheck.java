package com.furence.recsee.common.util;

public class OsCheck {

	/**
	 * 클라이언트 Os 판별해주는 유틸
	 */
	public static boolean clientIsWindow7( String OS ){
		if ( OS.indexOf("NT 6.1") >= 0) 
			return false;
		else
			return true;
	}
	
	/**
	 * Os 판별해주는 유틸
	 */
	public static boolean isWindow(){
		String OS = System.getProperty("os.name").toLowerCase();
		if ( OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ) 
			return false;
		else
			return true;
	}

	/**
	 * Os bit 체크 해주는 유틸
	 */
	public static boolean is64bit(){
		
		String arch = System.getProperty("os.arch");
		return (arch.endsWith("64")|| arch != null )? true : false;
	}
}