package com.furence.recsee.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import jxl.read.biff.BiffException;

import javax.servlet.ServletException;

import org.postgresql.util.PSQLException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.furence.recsee.common.model.LoginVO;

public class FileUploadUtil {
//	//@Autowired
//	// FIXME: 위치 재지정하기
//	//private static LogInfoService logInfoService;
//
//
//	// announce voice 파일 업로드
//	public static boolean fileUploadProc(MultipartHttpServletRequest request, MultipartFile mFile, String targetFileName, String targetPath) throws IOException, ServletException, BiffException, PSQLException {
//		boolean rst = true;
//
//		try {
//			LoginVO userInfo = SessionManager.getUserInfo(request);
//			if(userInfo != null) {
//				String announceFilePath = System.getProperty("catalina.home") + File.separator + "AnnounceFile";
//				if(targetPath != null) {
//					announceFilePath = targetPath;
//				}
//
//				File dir = new File(announceFilePath);
//				if(!dir.exists()) {
//					dir.mkdirs();
//				}
//
//				// MultipartHttpServletRequest 생성
//				MultipartHttpServletRequest mhsr =  request;
//				Iterator<String> iter = mhsr.getFileNames();
//
//				MultipartFile mfile = null;
//				String fieldName = "";
//				String origName = "";
//
//				// 값이 나올때까지
//				while (iter.hasNext()) {
//					fieldName = iter.next(); // 내용을 가져와서
//					mfile = mhsr.getFile(fieldName);
//
//					origName = new String(mfile.getOriginalFilename());
//					// 파일명이 없다면
//					if ("".equals(origName)) {
//						continue;
//					}
//
//					// 파일 명 변경(uuid로 암호화)
//					String saveFileName = origName;
//
//					// 설정한 path에 파일저장
//					File serverFile = new File(announceFilePath + File.separator + saveFileName);
//					mfile.transferTo(serverFile);
//				}
//
//			} else {
//				rst = false;
//				//logInfoService.writeLog(request, "Announce file Upload - Logout");
//			}
//		} catch (UnsupportedEncodingException e) {
//			//e.printStackTrace();
//			rst = false;
//
//			//logInfoService.writeLog(request, "Announce file Upload - File upload exception", e.getMessage());
//		}catch (IllegalStateException e) {
//			//e.printStackTrace();
//			rst = false;
//
//			//logInfoService.writeLog(request, "Announce file Upload - File upload exception", e.getMessage());
//		} catch (IOException e) {
//			//e.printStackTrace();
//			rst = false;
//
//			//logInfoService.writeLog(request, "Announce file Upload - File upload exception", e.getMessage());
//		}
//
//		return rst;
//	}
}
