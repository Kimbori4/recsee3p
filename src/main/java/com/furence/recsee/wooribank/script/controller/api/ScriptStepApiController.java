package com.furence.recsee.wooribank.script.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultBase;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.service.ScriptStepService;
import com.furence.recsee.wooribank.script.util.DhtmlGridDataProvider;

@Controller
@RequestMapping("/wooribank/script/api/step")
public class ScriptStepApiController {
	
private static final Logger logger = LoggerFactory.getLogger(ScriptStepApiController.class);

	@Autowired
	private ScriptStepService stepService;
	
	
	/**
	 * 스크립트 스텝 목록
	 * @param productPk
	 * @param resourceType
	 * @param transactionId
	 * @return
	 */
	@RequestMapping(value = "/list/{rsProductPk}/{resourceType}", 
					method = {RequestMethod.GET},
					produces = "application/xml")
	public ResponseEntity<dhtmlXGridXml> getScriptStepList(
			HttpServletRequest request,
			@PathVariable(value = "rsProductPk") String productPk,
			@PathVariable(value = "resourceType") String resourceType,
			@RequestParam(value = "transactionId" , required = false) String transactionId) {
		
		ScriptEditParam.Step stepParam = new ScriptEditParam.Step();
		stepParam.setScriptEditId(transactionId);
		stepParam.setProductPk(productPk);
		
		String contextPath = request.getContextPath();
		String [] args = {contextPath};
		dhtmlXGridXml xml = DhtmlGridDataProvider.ScriptStepGrid.getHeaders( args );
		
		// 예외발생시 빈 로우 리턴
		try {
			xml.setRowElements(this.stepService.getDhtmlxRowOfScriptStepList(stepParam, args).getRowElements());
		} catch(Exception e) {
			logger.error("error", e);
		}
		
		return new ResponseEntity<dhtmlXGridXml>(xml, HttpStatus.OK);
	}
	
	/**
	 * 스크립트 스텝 추가
	 * @param stepEditDTO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = {"", "/"}, 
					method = RequestMethod.POST, 
					produces = "application/json")
	public AJaxResult addScriptStep(			
			@RequestBody ScriptEditParam.Step stepEditDTO,
			HttpServletRequest request) throws Exception{
		
		stepEditDTO.setEditType(EditTypes.Create);
		stepEditDTO.setScriptStepType(stepEditDTO.getScriptStepName().contains("설명")? "P": "N");
		logger.info("ScriptStepEditDTO:" + stepEditDTO.toString());
		
		// input 으로 시작하면 리젝
		if( stepEditDTO.getScriptStepName().trim().startsWith("<input") ){
			return new AJaxResult.Builder(ResultCode.INVALID_PARAMETER).build();
		}
		
		ResultBase result = this.stepService.runTask(EditTypes.Create, stepEditDTO);
		ResultCode resultCode = result.toResucltCode();

		return new AJaxResult.Builder(resultCode)
				.attribute("rsScriptStepPk", stepEditDTO.getScriptStepPk())
				.build();
	}
	
	/**
	 * 스크립트 스텝 수정
	 * @param stepEditDTO
	 * @param scriptStepPk
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{scriptStepPk}", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult updateScriptStep(			
			@RequestBody ScriptEditParam.Step stepEditDTO,
			@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
			HttpServletRequest request) throws Exception{
		
		stepEditDTO.setEditType(EditTypes.Update);
		stepEditDTO.setScriptStepPk(scriptStepPk);
		stepEditDTO.setScriptStepType(stepEditDTO.getScriptStepName().contains("설명")? "P": "N");
		logger.info("ScriptStepEditDTO:" + stepEditDTO.toString());
		
		// input 으로 시작하면 리젝
		if( stepEditDTO.getScriptStepName().trim().startsWith("<input") ){
			return new AJaxResult.Builder(ResultCode.INVALID_PARAMETER).build();
		}

		ResultBase result = this.stepService.runTask(EditTypes.Update, stepEditDTO);
		ResultCode resultCode = result.toResucltCode();

		return new AJaxResult.Builder(resultCode).build();
	}
	
	/**
	 * 스크립트 스텝 삭제
	 * @param stepEditDTO
	 * @param scriptStepPk
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{scriptStepPk}", 
					method = RequestMethod.DELETE, 
					produces = "application/json")
	public AJaxResult deleteScriptStep(			
			@RequestBody ScriptEditParam.Step stepEditDTO,
			@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
			HttpServletRequest request) throws Exception{
		
		stepEditDTO.setEditType(EditTypes.Delete);
		stepEditDTO.setScriptStepPk(scriptStepPk);
		logger.info("ScriptStepEditDTO:" + stepEditDTO.toString());
		
		ResultBase result = this.stepService.runTask(EditTypes.Delete, stepEditDTO);
		ResultCode resultCode = result.toResucltCode();

		return new AJaxResult.Builder(resultCode).build();
	}
	
}
