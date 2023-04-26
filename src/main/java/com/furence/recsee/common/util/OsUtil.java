package com.furence.recsee.common.util;


public class OsUtil {
	
	private static OsUtil instance = new OsUtil();
	
	private OsUtil() {
	}
	
	public static OsUtil getInstance() {
		return instance;
	}
	
	public String OsCheck() {
		String os = System.getProperty("os.name");
		if(os.toUpperCase().contains("win".toUpperCase())) {
			return "window";
		}else {
			return "linux";
		}
	}

}
