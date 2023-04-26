package com.furence.recsee.adw.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.furence.recsee.adw.dao.AdwDao;
import com.furence.recsee.adw.model.CsvInfo;
import com.furence.recsee.adw.service.AdwService;
import com.furence.recsee.adw.util.CsvFactory;

@Controller
public class adwController {
	
	@Autowired
	AdwService adwService;
	
	@Autowired
	AdwDao mapper;
	
	@Value("#{scriptManageProperties['file.path']}")
	private String filePath;
	
	@RequestMapping(value = "/adw/{sTime}/{eTime}/csv")
	public void search(HttpServletRequest request,
				 	   HttpServletResponse response,
				 	   Model model,
				 	   @PathVariable(value = "sTime") String sTime,
				 	   @PathVariable(value = "eTime") String eTime) throws Exception {
		File file = null;
		
		HashMap<String, String> dateHash = new HashMap<String, String>();
		dateHash.put("sTime", sTime);
		dateHash.put("eTime", eTime);
		
		String fileName = sTime+"_"+eTime+"_adw";
		
		List<CsvInfo> selectAdwData = mapper.selectAdwData(dateHash);
		selectAdwData = Optional.of(selectAdwData).orElse(Collections.EMPTY_LIST);
		if(selectAdwData.isEmpty()) {
		}
		file = CsvFactory.writeCSV(selectAdwData, fileName,filePath);
		
		
		// 1. 브라우저별 한글 파일이름 표시
		String header = request.getHeader("User-Agent");
		String downloadFileName = file.getName(); // file이 null인 경우 500에러 발생, 이 컨트롤러에서 따로 처리x
		
		downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 2. 클라이언트에게 전달할 파일 복사 -- 상품 버전별 파일을 생성 후 보관, 파일 삭제 x
		try (OutputStream out = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);){		
			FileCopyUtils.copy(fis, out);
		} 
		
	}

}
