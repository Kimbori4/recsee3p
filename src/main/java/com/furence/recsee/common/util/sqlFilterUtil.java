package com.furence.recsee.common.util;

public class sqlFilterUtil {

	//@Kyle
	//보안권고 sql 전송 전 필터링 필수..
	public static String sqlFilter(String str) {
		String Arr[] = {"cmdshell","union","drop","select","xp_","sp_","\'", "\"", "--", ",", "\\(", "\\)", "#", ">", "<", "=", "\\*/", "/\\*", "\\+", "%", ",", "@", ";", "\\\\", "|", "\\[", "\\]", "&"};
		for(int i=0; i< Arr.length; i++){
			//str = str.replaceAll(Arr[i],"");
			str = str;
			//str = "";
		}
		return str;
	}
	
	public static String sqlFilter2(String str) {
		String Arr[] = {"cmdshell","union","drop","select"};
		for(int i=0; i< Arr.length; i++){
			str = str.replaceAll(Arr[i],"");
		}
		return str;
	}
}
