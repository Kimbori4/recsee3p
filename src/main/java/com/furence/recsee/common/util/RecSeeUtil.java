package com.furence.recsee.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.initech.shttp.server.Logger;

public class RecSeeUtil {
	
	public String getLocalServerIp(){
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	return inetAddress.getHostAddress().toString();
		            }
		        }
		    }
		}
		catch (SocketException ex) {Logger.error("", "", "", ex.toString());}
		return null;
	}

	public static double MyRound(double num, int pos ) {
		double result = 0.0;
		double half = 0.5;
		double factor = 1;

		//[2]
		for (int i = 0; i< pos; i++){
			half *= 0.0; //1;
			factor *= 10;
		}
		result = (int)(((num*100)+half)*factor)/(double)factor;

		//[3]
		return result;
	}

	public String getSecToTime(Integer sec) {
		Integer hours = sec / 3600;
		Integer minutes = (sec % 3600) / 60;
		Integer seconds = sec % 60;

		return String.format("%02d:%02d:%02d",  hours, minutes, seconds);
	}

	public String makePhoneNumber(String phoneNumber) {
		
		if(phoneNumber == null || ("").equals(phoneNumber)) {
			return phoneNumber;
		}
		
		if(phoneNumber.length() > 8) {
			//String regEx1 = "(^02.{0}|^01.{1}|^0[0-9]{2})([0-9]+)([0-9]{4})";
			String regEx1 = "(^02.{0}|^01.{1}|^[0-9]{3})([0-9]+)([0-9]{4})";
			if(!Pattern.matches(regEx1, phoneNumber)) return phoneNumber;
			return phoneNumber.replaceAll(regEx1, "$1-$2-$3");
			//아래는 라오스
			/*if(phoneNumber.length() > 10){
				String regEx1 = "(^01.{1}|^0[0-9]{2})([0-9]+)([0-9]{4})";
				if(!Pattern.matches(regEx1, phoneNumber)) return phoneNumber;
				return phoneNumber.replaceAll(regEx1, "$1-$2-$3");
			}else{
				String regEx1 = "(^[0-9].{2}|^01.{1}|^0[0-9]{2})([0-9]+)([0-9]{3})";
				if(!Pattern.matches(regEx1, phoneNumber)) return phoneNumber;
				return phoneNumber.replaceAll(regEx1, "$1-$2-$3");
			}*/
			//라오스끝
		} else {
			String regEx2 = "(^[1-9][0-9]+)([0-9]{4})";
			if(!Pattern.matches(regEx2, phoneNumber)) {
				String regEx3 = "(^0[1-9]{2})([0-9]+)";
				if(!Pattern.matches(regEx3, phoneNumber)) return phoneNumber;
				return phoneNumber.replaceAll(regEx2, "$1-$2");
			}
			return phoneNumber.replaceAll(regEx2, "02-$1-$2");
		}
	}

	public String makingName (String name){
		StringBuffer sb = new StringBuffer(name);
		String result=new String(name);

		if(name.length()>0){
			if(name.length()>1){
				sb.replace(1, 2,"*");
				result=new String(sb);
			}
		}

		return result;
	}

	public String maskingNumber(String pNum){
		if (StringUtil.isNull(pNum,true))
			return "";
		if(pNum.length()<=5)
			return pNum;
		
		StringBuffer sb = new StringBuffer(pNum);
		Integer findHyphen= pNum.indexOf("-"); //3
		Integer loopCount = 4;
		
		if (findHyphen == -1) 
			loopCount = pNum.length();
		
		for(int i=0 ; i < loopCount ; i++){
			String findWord = pNum.substring(findHyphen+1,findHyphen+2);
			if(findWord.equals("-")){
				break;
			}else{
				sb.replace(findHyphen+1, findHyphen+2,"*");
				findHyphen++;
			}
		}
		
		String result = new String(sb);
		return result;
	}

	public String prefixPhoneNum(String pNum, String[] arrPrefixInfo) {
		if (StringUtil.isNull(pNum,true))
			return "";
		if(pNum.length()<=5)
			return pNum;

		StringBuffer sb = new StringBuffer(pNum);
		
		
		for (int i = 0; i < arrPrefixInfo.length; i++) {
			if (pNum.startsWith(arrPrefixInfo[i])) {
				sb.delete(0, arrPrefixInfo[i].length());
				break;
			}
		}

		String result = new String(sb);
		return result;
	}
	
	
	public String maskingPhoneNum(String pNum, int startIdx, int ea){
		if (StringUtil.isNull(pNum,true))
			return "";
		if(pNum.length()<startIdx+ea)
			return pNum;
		
		StringBuffer sb = new StringBuffer(pNum);
//		Integer findHyphen= pNum.indexOf("-");
//		Integer loopCount = 4;
//
//		if (findHyphen == -1) 
//			loopCount = pNum.length();
		Integer loopCount = pNum.length();
		int idx = startIdx;
		int maskingEA = startIdx + ea;
		
//		Integer prefixIdx = pNum.indexOf(" ");
//		int cnt = 0;
//		if (prefixIdx != -1) {
//			cnt = prefixIdx + 1;
//			idx += cnt;
//			maskingEA += cnt;
//		}
		
		
		for(int i = 0 ; i < loopCount ; i++){
			if (idx == maskingEA) {
				break;
			}
			String findWord = pNum.substring(idx, idx+1);
			
			if(findWord.equals("-")){
				idx++;
				maskingEA++;
			}else{
				sb.replace(idx, idx+1,"*");
				idx++;
			}
		}
		
		String result = new String(sb);
		return result;
	}
	
	public String maskingPhoneNumLast(String pNum, int startIdx, int ea){
		if (StringUtil.isNull(pNum,true))
			return "";
		if(pNum.length()<startIdx+ea)
			return pNum;
		
		StringBuffer sb = new StringBuffer(pNum);
		Integer loopCount = pNum.length();
		int idx = pNum.length() - startIdx -1;
		int maskingEA = pNum.length() - (startIdx + ea) -1;
		
		Integer prefixIdx = pNum.indexOf(" ");
		int cnt = 0;
		if (prefixIdx != -1) {
			cnt = prefixIdx + 1;
			idx += cnt;
			maskingEA += cnt;
		}
		
		
		for(int i = cnt ; i < loopCount ; i--){
			if (idx == maskingEA) {
				break;
			}
			String findWord = pNum.substring(idx, idx+1);
			
			if(findWord.equals("-")){
				idx--;
				maskingEA--;
			}else{
				sb.replace(idx, idx+1,"*");
				idx--;
			}
		}
		
		String result = new String(sb);
		return result;
	}
	
	public String setHyphenNum(String pNum, String h1, String h2){
		if (StringUtil.isNull(pNum,true))
			return "";
		if(pNum.length()<=5)
			return pNum;
		
		
//		Integer prefixIdx = pNum.indexOf(" ");
		int cnt = 0;
//		if (prefixIdx != -1) {
//			cnt = prefixIdx + 1;
//		}

		
		int hyphen2 = Integer.parseInt(h2);
		String resultNum = "";

//		Integer findHyphen= pNum.indexOf("-");
//		Integer loopCount = 4;
//
//		if (findHyphen == -1) 
//			loopCount = pNum.length();
		if (!h1.equals("N")) {
			//SUBSTRING(R_CUST_PHONE1, 1, 3)||'-'||SUBSTRING(R_CUST_PHONE1, 4, 4)||'-'||SUBSTRING(R_CUST_PHONE1, 8);
			int hyphen1 = Integer.parseInt(h1) + cnt;
			if(pNum.length()-cnt < hyphen1+hyphen2)
				return pNum;
			
			resultNum = pNum.substring(0, cnt) + pNum.substring(cnt, hyphen1) + "-" + pNum.substring(hyphen1, hyphen1+hyphen2) + "-" + pNum.substring(hyphen1+hyphen2);
		} else {
			if(pNum.length()-cnt < hyphen2)
				return pNum;
			resultNum = pNum.substring(0, cnt) + pNum.substring(cnt, hyphen2) + "-" + pNum.substring(hyphen2);
		}
		return resultNum;
	}
	
	public String getSHA256(String str){
		String SHA = "";
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();

		}catch(NoSuchAlgorithmException e){
			Logger.error("", "", "", e.toString());
			SHA = null;
		}
		return SHA;
	}
	
	
	public static  String getClientIp(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        	//System.out.println("Forwarded + ip::"+ ip);
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
            //System.out.println("Proxy-Client-IP::"+ ip);
        } 
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
            //System.out.println("WL-Proxy-Client-IP::"+ ip);
        } 
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
            //System.out.println("HTTP_CLIENT_IP::"+ ip);
        } 
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
            //System.out.println("HTTP_X_FORWARDED_FOR::"+ ip);
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
           // System.out.println("X-Real-IP::"+ ip);
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
           // System.out.println("X-RealIP::"+ ip);
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
            //System.out.println("REMOTE_ADDR::"+ ip);
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
            //System.out.println("getRemoteAddr::"+ ip);
        }
        
        
        return ip;
    }
}
