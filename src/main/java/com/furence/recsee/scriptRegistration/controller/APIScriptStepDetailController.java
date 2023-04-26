package com.furence.recsee.scriptRegistration.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.mapping.ResultFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResBuilder;
import com.furence.recsee.common.model.AJaxResBuilder.RESULT;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailDTO;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailVO;
import com.furence.recsee.scriptRegistration.service.ScriptRegistrationService;


@Controller
public class APIScriptStepDetailController {
	
	private static final Logger logger = LoggerFactory.getLogger(APIScriptStepDetailController.class);

	private LogInfoService logInfoService;
	
	private ScriptRegistrationService scriptRegistrationService;
	
	@Autowired
	public APIScriptStepDetailController(LogInfoService logInfoService, ScriptRegistrationService scriptRegistrationService) {
		this.logInfoService = logInfoService;
		this.scriptRegistrationService = scriptRegistrationService;
	}
	
	
	/**
	 * 상품 스크립트의 스텝별 상세 스크립트 정보 가져오기 API
	 * @param request
	 * @param detailDTO
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value  	= "/script/step/{rScriptStepPk}/detail/", 
					method 	= { RequestMethod.GET } , 
					produces= "application/json;charset=UTF-8")
	@ResponseBody
	public AJaxResVO selectScriptDetailListAPI( 
		HttpServletRequest request,  
		@PathVariable(value = "rScriptStepPk" ) String rScriptStepPk,
		ScriptStepDetailDTO detailDTO ) throws IOException {
			
		logger.info("detailDTO:" + detailDTO.toString());
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		AJaxResBuilder resultBuiler = AJaxResBuilder.builder(RESULT.FAIL);
		
		// 미로그인 리턴
		if (userInfo == null) {
			logInfoService.writeLog(request, "Etc - Logout");			
			return 	resultBuiler
					.message("login fail")
					.build();
		}
		
		try {
			
			List<ScriptStepDetailVO> detailList = scriptRegistrationService.selectScriptStepDetailList(detailDTO);
			
			boolean hasItems = null != detailList && false == detailList.isEmpty();
			int itemSize = hasItems ? detailList.size() : 0;
						
			// 성공시
			return resultBuiler
					.success(hasItems ? RESULT.SUCCESS : RESULT.FAIL)
					.result(itemSize+"")
					.message("OK")
					.attribute("scriptDetailList", detailList)
					.build();
			
			
		} catch(Exception e) {
			logger.error("error", e);
		}
		
		return resultBuiler
				.result("0")
				.message("no result")					
				.build();
	}
	
}
