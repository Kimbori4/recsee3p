package com.furence.recsee.recording.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.wooribank.facerecording.model.FaceRecordingInfo;
import com.furence.recsee.wooribank.facerecording.service.FaceRecordingService;

@Controller
public class AjaxRecordingController {
	@Autowired
	private FaceRecordingService faceRecordingService;
	
	@RequestMapping(value = "/insertFaceRecInfo.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO insertFaceRecInfo(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			FaceRecordingInfo faceRecordingInfo = new FaceRecordingInfo();

			if (!StringUtil.isNull(request.getParameter("productCode"), true))
				faceRecordingInfo.setProductCode(request.getParameter("productCode"));
				
			Date date = new Date();
			String faceRecKey = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
			faceRecordingInfo.setFaceRecKey("B" + faceRecKey);
			faceRecordingInfo.setUserId(userInfo.getUserId());
			faceRecordingInfo.setUserName(userInfo.getUserName());
			if (!StringUtil.isNull(request.getParameter("faceRecTtime"), true))
				faceRecordingInfo.setFaceRecTtime(request.getParameter("faceRecTtime"));
			if (!StringUtil.isNull(request.getParameter("custId"), true))
				faceRecordingInfo.setCustId(request.getParameter("custId"));
			if (!StringUtil.isNull(request.getParameter("custName"), true))
				faceRecordingInfo.setCustName(request.getParameter("custName"));
			if (!StringUtil.isNull(request.getParameter("custPhone"), true))
				faceRecordingInfo.setCustPhone(request.getParameter("custPhone"));

			Integer result = faceRecordingService.insertFaceRecInfo(faceRecordingInfo);
			if(result > 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		
		return jRes;
	}
}
