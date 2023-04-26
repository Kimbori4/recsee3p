package com.furence.recsee.common.util;

public class PwCheck {

	private static boolean checkDuplicate3Character(String d) {
		int p = d.length();
		byte[] b = d.getBytes();
		for (int i = 0; i < ((p * 2) / 3); i++) {
			int b1 = b[i + 1];
			int b2 = b[i + 2];
 
			if ((b[i] == b1) && (b[i] == b2)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
	
	private static int digitCheck(String passwd) {
		int varDigit = 0;
		int varAlpha = 0;
		int varHex = 0;
		int varSum = 0;
		for (int i = 0; i < passwd.length(); i++) {
			char index = passwd.charAt(i);
 
			if (index >= '0' && index <= '9') {
				varDigit = 1;
			} else if ( (index >= 'a' && index <= 'z') || (index >= 'A' && index <= 'Z') ) {
				varAlpha = 1;
			} else if (index == '!' || index == '@' || index == '#' || index == '$'
					|| index == '%' || index == '^' || index == '&'
					|| index == '*') {
				varHex = 1;
			} 
		}
 
		varSum = varDigit + varAlpha + varHex;
		
		return varSum;
	}
	
	public static String pwCheck(String passwd, String id, String lang){
		
		String returnValue = null;
		    // 영문+숫자+특수문자 조합
		
		if (digitCheck(passwd) < 3) {
	    	if("jp".equals(lang)) {
	    		returnValue = "パスワードは英数字、特殊文字(!_%_*)の組み合わせで入力してください。";
	    	}else {
	    		returnValue = "비밀번호는 영문, 숫자, 특수문자(!@$%^&*) 조합으로 입력해주세요.";	
	    	}
		    // 아이디 포함 여부
	    }else if(passwd.indexOf(id) != -1){
	    	if("jp".equals(lang)) {
	    		returnValue = "パスワードにIDが含まれています。";
	    	}else {
	    		returnValue = "비밀번호에 아이디가 포함되어있습니다.";
	    	}
	    	// 중복된 3자 이상의 문자 또는 숫자 사용불가
	    }else if (checkDuplicate3Character(passwd)) {
	    	if("jp".equals(lang)) {
	    		returnValue = "パスワードは連続した3文字以上の文字を使用できません。";
	    	}else {
	    		returnValue = "비밀번호는 연속된 3자 이상의 문자 사용불가입니다.";
	    	}
		}else if ( passwd.length() < 8 || passwd.length() > 20 ) {
			if("jp".equals(lang)) {
	    		returnValue = "パスワードは8~20文字の間から入力してください!";
	    	}else {
	    		returnValue = "비밀번호는 8~20자 사이로 입력 해 주세요!";
	    	}
		}
		return returnValue;
	}
	
	//영어 / 숫자 / 특수문자중 2가지 이상 조합일 경우 10자리 이상, 3가지 조합 이상일 경우 8자리 이상 로 설정 예정	
	public static String pwCheckHard(String passwd, String id, String lang){
		String returnValue = null;
		    // 영문+숫자+특수문자 조합
	    if (digitCheck(passwd) < 3) { //영어 / 숫자 / 특수문자중 2가지 이상 조합
	    	if ( passwd.length() > 10 )
	    		if("jp".equals(lang)) {
		    		returnValue = "パスワードは10文字以上入力してください。";
		    	}else {
		    		returnValue = "비밀번호는 10자 이상 입력 해 주세요!";
		    	}
	    }else if(digitCheck(passwd) >= 3){
	    	if ( passwd.length() > 8 )
	    		if("jp".equals(lang)) {
		    		returnValue = "パスワードは8文字以上入力してください!";
		    	}else {
		    		returnValue = "비밀번호는 8자 이상 입력 해 주세요!";
		    	}
	    } 
		return returnValue;
	}
	
	
}