package com.furence.recsee.common.util;

import java.io.File;

public class DirectoryUtil {

	/**
	 * * @description 폴더 체크  *
	 * 
	 * @param path
	 *            폴더가 존재하는지 체크할경로 *
	 * @return boolean 
	 *         	  true : 존재
	 *         	  false 미 존재
	 */
	public static boolean checkDirectory(File file) {
		boolean checkFolder = checkFolder(file);
		return checkFolder;
	}
	/**
	 * @description 폴더생성
	 * 
	 * @param file
	 * @return boolean
	 */
	
//	default = /usr/local/tomcat9/ 
//	file = /usr/local/tomcat9/tts/history/
	public static boolean mkdirDirectory(File file) {
		boolean checkFolder = checkFolder(file);
		if(!checkFolder) {
			return file.mkdirs();
		}
		
		return false;
//		File parentFile = file.getParentFile();
//		//부모 파일이 없으면 재귀시작
//		if(!checkFolder(parentFile)) {
//			boolean flag = mkdirDirectory(parentFile);
//			if(flag) {
//				return file.mkdir();
//			}
//			
//		}
//		return file.mkdir();
////	
//		boolean checkFolder = checkFolder(file);
//		if(checkFolder) {
//			return false;
//		}
//		boolean mkdir = file.mkdir();
//		return mkdir? true : false;
	}
	
	
	
	
	public static boolean checkFolder(File file) {
		if(file != null) {
			if(file.exists()) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
}
