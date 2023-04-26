package com.furence.recsee.wooribank.script.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.ScriptApproveParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultBase;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.service.ScriptCommonService;

@Controller
@RequestMapping("/wooribank/script/api/common")
public class ScriptCommonApiController {

	private static final Logger logger = LoggerFactory.getLogger(ScriptCommonApiController.class);
	
	@Autowired
	private ScriptCommonService commonService;
	
	/**
	 * 공통스크립트 편집가능 여부 확인
	 * @param rsScriptCommonPk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{rsScriptCommonPk}/status", 
					method = RequestMethod.GET, 
					produces = "application/json")
	public AJaxResult checkStatusForCommonScript(
			@PathVariable(value = "rsScriptCommonPk") String scriptCommonPk) {
		
		boolean exists = this.commonService.isExistApprovalRequest(scriptCommonPk);
		ResultCode result = exists ? ResultCode.ALREADY_REQUESTED : ResultCode.SUCCESS;
		return new AJaxResult.Builder(result).build();
	}
	
	
	/**
	 * 공통 스크립트 추가
	 * @param commonDto
	 * @param request
	 * @return
	 * @throws Exception
	 */		
	@ResponseBody 
	@RequestMapping(value = {"","/"}, 
					method = RequestMethod.POST, 
					produces = "application/json")
	public AJaxResult addCommonScript( 			
			@RequestBody ScriptEditParam.Common edit,				
			HttpServletRequest request) throws Exception {
		
		// inteceptor에서 이미 로그인확인을 한 뒤에 넘어오므로 널체크없이 바로 씀
		LoginVO userInfo = SessionManager.getUserInfo(request);

		edit.setCommonScriptEditUser(userInfo.getUserId());
		edit.setEditType(EditTypes.Create);
		
		logger.info("ScriptEditParam.Common:" + edit.toString());
		
		ResultBase result = this.commonService.runTask(EditTypes.Create, edit);		
		ResultCode resultCode = result.toResucltCode();		
		return new AJaxResult.Builder(resultCode)
				.attribute("rsScriptCommEditId", result.getUserData())
				.build();
	}
	
	
	/**
	 * 공통 스크립트 수정
	 * @param rsScriptCommonPk
	 * @param commonDto
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{rsScriptCommonPk}", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult updateCommonScript(
			@PathVariable(value = "rsScriptCommonPk") String rsScriptCommonPk,
			@RequestBody ScriptEditParam.Common edit,
			HttpServletRequest request) throws Exception {
		
		// inteceptor에서 이미 로그인확인을 한 뒤에 넘어오므로 널체크없이 바로 씀
		LoginVO userInfo = SessionManager.getUserInfo(request);
				
		edit.setCommonScriptPk(rsScriptCommonPk);
		edit.setCommonScriptEditUser(userInfo.getUserId());
		edit.setEditType(EditTypes.Update);
		
		logger.info("ScriptEditParam.Common:" + edit.toString());		
		
		ResultBase result = this.commonService.runTask(EditTypes.Create, edit);		
		ResultCode resultCode = result.toResucltCode();
		
		return new AJaxResult.Builder(resultCode).build();
	}
	
	/**
	 * 공통 스크립트 삭제
	 * @param rsScriptCommonPk
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{rsScriptCommonPk}", 
					method = RequestMethod.DELETE, 
					produces = "application/json")
	public AJaxResult deleteCommonScript(
			@PathVariable(value = "rsScriptCommonPk") String rsScriptCommonPk,
			@RequestBody ScriptEditParam.Common edit,
			HttpServletRequest request) throws Exception {
		
		// inteceptor에서 이미 로그인확인을 한 뒤에 넘어오므로 널체크없이 바로 씀
		LoginVO userInfo = SessionManager.getUserInfo(request);
				
		logger.info("rsScriptCommonPk:" + rsScriptCommonPk);
		
		edit.setCommonScriptEditUser(userInfo.getUserId());
		edit.setCommonScriptPk(rsScriptCommonPk);
		edit.setEditType(EditTypes.Delete);
		
		logger.info("ScriptEditParam.Common:" + edit.toString());
		
		ResultBase result = this.commonService.runTask(EditTypes.Delete, edit);		
		ResultCode resultCode = result.toResucltCode();
		return new AJaxResult.Builder(resultCode).build();
	}
		
	/**
	 * 결재대기중인 공통스크립트에 대한 결재/반려/취소 처리
	 * @param rsScriptCommonEditId
	 * @param approveDto
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/approve/{rsScriptCommonEditId}", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult approveEditedCommonScript(
			@PathVariable(value = "rsScriptCommonEditId") String commonEditId,
			ScriptApproveParam approve,
			HttpServletRequest request) throws Exception {
	
		LoginVO userInfo = SessionManager.getUserInfo(request);
		approve.setApproveUser(userInfo.getUserId());
		approve.setScriptEditId(commonEditId);
		
		logger.info("ScriptApproveParam:" + approve.toString());

		ResultCode resultCode = ResultCode.SUCCESS;
				
		return new AJaxResult.Builder(resultCode).build();
	}
}
