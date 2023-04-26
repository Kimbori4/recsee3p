package com.furence.recsee.wooribank.script.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.furence.recsee.wooribank.script.param.request.FileDownloadParam;
import com.furence.recsee.wooribank.script.service.FileProviderService;
import com.furence.recsee.wooribank.script.service.file.types.FileService.FileServiceType;
import com.furence.recsee.wooribank.script.service.file.types.FileService.ProviderParam;
import com.furence.recsee.wooribank.script.service.file.types.FileType;

@Controller
@RequestMapping("/wooribank/script/download")
public class FileDownloadController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	@Autowired
	private FileProviderService fileService;
	
	@RequestMapping(value = "/product/{productPk}/script/{version}", 
					method = RequestMethod.GET )
	public void downloadSnapshotFile(
			@PathVariable(value = "productPk") String productPk,
			@PathVariable(value = "version") String version,
			@RequestParam(value = "fileType") String fileType,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		ProviderParam<FileDownloadParam.VersionSnapshot> providerParam = ProviderParam.of(FileServiceType.History);
		FileDownloadParam.VersionSnapshot historyParam = providerParam.getParamter();
		historyParam.setProductPk(productPk);
		historyParam.setScriptVersion(version);
		historyParam.setFileType( FileType.create(fileType) );
		
		logger.info("FileDownloadParam.ScriptHistory:" + historyParam);
				
		// 요청 파라미터를 확인해서 , 처리할 비즈니스 로직을 결정
		File file = fileService.createFile(providerParam); 
		logger.info("file returned to controller: " +file);
		
		/* response에 파일 반환 */

		// 1. 브라우저별 한글 파일이름 표시
		String header = request.getHeader("User-Agent");
		String downloadFileName = file.getName(); // file이 null인 경우 500에러 발생, 이 컨트롤러에서 따로 처리x
		
		if(header.contains("Edge") || header.contains("MSIE") || header.contains("Trident")) {
			downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
		}else{ //  if(header.contains("Chrome") || header.contains("Firefox")) 
			downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName);
		response.setHeader("Content-Transfer-Encoding", "binary");

		// 2. 클라이언트에게 전달할 파일 복사 -- 상품 버전별 파일을 생성 후 보관, 파일 삭제 x
		try (OutputStream out = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);){		
			FileCopyUtils.copy(fis, out);
		} 
	}
	
	
	@RequestMapping(value = "/product/{productPk}/script/latest", 
			method = RequestMethod.GET )
		public void downloadScriptInfoFile(
			@PathVariable(value = "productPk") String productPk,
			@RequestParam(value = "fileType") String fileType,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		ProviderParam<FileDownloadParam.ScriptCurrentInfo> providerParam = ProviderParam.of(FileServiceType.ScriptInfo);
		FileDownloadParam.ScriptCurrentInfo scriptParam = providerParam.getParamter();
		scriptParam.setProductPk(productPk);
		scriptParam.setFileType( FileType.create(fileType) );
		
		logger.info("FileDownloadParam.ScriptInfo:" + scriptParam);
				
		// 요청 파라미터를 확인해서 , 처리할 비즈니스 로직을 결정
		File file = fileService.createFile(providerParam); 
		logger.info("file returned to controller: " +file);
		
		/* response에 파일 반환 */
		
		// 1. 브라우저별 한글 파일이름 표시
		String header = request.getHeader("User-Agent");
		String downloadFileName = file.getName(); // file이 null인 경우 500에러 발생, 이 컨트롤러에서 따로 처리x
		
		if(header.contains("Edge") || header.contains("MSIE") || header.contains("Trident")) {
			downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
		}else{ //  if(header.contains("Chrome") || header.contains("Firefox")) 
			downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 2. 클라이언트에게 전달할 파일 복사 -- 상품 버전별 파일을 생성 후 보관, 파일 삭제 x
		try (OutputStream out = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);){		
			FileCopyUtils.copy(fis, out);
		} 
	}
	
	@RequestMapping(value = "/product/{callkey}/script/call", 
			method = RequestMethod.GET )
		public void downloadScriptCallFile(
			@PathVariable(value = "callkey") String callkey,
			@RequestParam(value = "fileType") String fileType,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		ProviderParam<FileDownloadParam.ScriptCallInfo> providerParam = ProviderParam.of(FileServiceType.CallScript);
		FileDownloadParam.ScriptCallInfo callScriptParam = providerParam.getParamter();
		callScriptParam.setCallKey(callkey);
		callScriptParam.setFileType( FileType.create(fileType) );
		
		logger.info("FileDownloadParam.CallScript:" + callScriptParam);
				
		// 요청 파라미터를 확인해서 , 처리할 비즈니스 로직을 결정
		File file = fileService.createFile(providerParam); 
		logger.info("file returned to controller: " +file);
		
		/* response에 파일 반환 */
		
		// 1. 브라우저별 한글 파일이름 표시
		String header = request.getHeader("User-Agent");
		String downloadFileName = file.getName(); // file이 null인 경우 500에러 발생, 이 컨트롤러에서 따로 처리x
		
		if(header.contains("Edge") || header.contains("MSIE") || header.contains("Trident")) {
			downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
		}else{ //  if(header.contains("Chrome") || header.contains("Firefox")) 
			downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 2. 클라이언트에게 전달할 파일 복사 -- 상품 버전별 파일을 생성 후 보관, 파일 삭제 x
		try (OutputStream out = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);){		
			FileCopyUtils.copy(fis, out);
		} 
	}
	
}
