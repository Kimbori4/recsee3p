package com.furence.recsee.uploadstatus.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.uploadstatus.model.UploadInfo;
import com.furence.recsee.uploadstatus.service.UploadInfoService;

@Controller
public class AjaxUploadStatusController {
	
	@Autowired
	private UploadInfoService uploadInfoService;
	
	@RequestMapping(value="/insertUploadSend.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO insertUploadSend(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		if(userInfo != null) {
			Integer requestCount = 0;
			List<String> recDateArr = new ArrayList<String>();
			List<String> recTimeArr = new ArrayList<String>();
			List<String> extArr = new ArrayList<String>();
			
			for(String s : params.get("recDateArr").split(",")) { recDateArr.add(s); }
			for(String s : params.get("recTimeArr").split(","))	{ recTimeArr.add(s); }
			for(String s :params.get("extArr").split(",",-1)) {	extArr.add(s); }
		
			for(int i = 0 ; i < recDateArr.size() ; i++) {

				UploadInfo uploadInfo = new UploadInfo();
				uploadInfo.setRecDate(recDateArr.get(i).replaceAll("-",""));
				uploadInfo.setRecTime(recTimeArr.get(i).replaceAll(":",""));
				uploadInfo.setExtNum(extArr.get(i));
				
				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					uploadInfo.setPosStart(posStart);
				}
				Integer count = 50;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("count"));
					uploadInfo.setCount(count);
				}
				try {
					requestCount += uploadInfoService.insertUpload(uploadInfo);
				}catch (Exception e) {
					jRes.addAttribute("msg", "SOME REQUEST FAIL");
				}
			}

			if(requestCount>0){
				jRes.setSuccess(jRes.SUCCESS_Y);
			}else {
				jRes.setSuccess(jRes.SUCCESS_N);
				jRes.addAttribute("msg", "REQUEST FAIL");
			}
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "NOT LOGIN");
		}
		return jRes;
	}
}
