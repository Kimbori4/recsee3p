package com.furence.recsee.wooribank.script.controller.api;

import java.io.IOException;
import java.util.List;

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

import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.request.TTSParam;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultBase;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.repository.entity.ScriptDetail;
import com.furence.recsee.wooribank.script.service.ScriptDetailService;
import com.furence.recsee.wooribank.script.service.ScriptTtsService;

@Controller
@RequestMapping("/wooribank/script/api/step/{scriptStepPk}/detail")
public class ScriptDetailApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptDetailApiController.class);
		
	private ScriptDetailService detailService;
	
	private ScriptTtsService ttsService;
	
	@Autowired
	public ScriptDetailApiController(ScriptDetailService detailService ,
			ScriptTtsService ttsService) {
		this.detailService = detailService;
		this.ttsService = ttsService;
	}
	
	/**
	 * 상품 스크립트의 스텝별 상세 스크립트 정보 가져오기 API
	 * @param request
	 * @param detailDTO
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value  	= {"","/"}, 
					method 	= { RequestMethod.GET } , 
					produces= "application/json;charset=UTF-8")
	@ResponseBody
	public AJaxResult searchScriptDetail( 
		HttpServletRequest request,  
		@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
		ScriptEditParam.Detail detail ) throws IOException {
			
		logger.info("ScriptEditParam.Detail:" + detail.toString());
				
		ResultCode resultCode = ResultCode.SUCCESS;
		List<ScriptDetail> detailList = null;
		
		try {			
			detailList = this.detailService.getListOfScriptDetail(detail);
		} catch(Exception e) {
			resultCode = ResultCode.SYSTEM_ERROR;
			logger.error("error", e);
		}
		
		return new AJaxResult.Builder(resultCode)
				.attribute("scriptDetailList", detailList)
				.build();
	}
	
	/**
	 * 상품 스크립트의 스텝별 상세 스크립트 정보 가져오기 API
	 * @param request
	 * @param detailDTO
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value  	= {"/preview"}, 
					method 	= { RequestMethod.GET } , 
					produces= "application/json;charset=UTF-8")
	@ResponseBody
	public AJaxResult searchScriptDetailPreview( 
		HttpServletRequest request,  
		@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
		ScriptEditParam.Detail detail ) throws IOException {
			
		logger.info("ScriptEditParam.Detail:" + detail.toString());
				
		ResultCode resultCode = ResultCode.SUCCESS;
		List<ScriptDetail> detailList = null;
		
		try {			
			detailList = this.detailService.getListOfScriptDetailPreview(detail);
		} catch(Exception e) {
			resultCode = ResultCode.SYSTEM_ERROR;
			logger.error("error", e);
		}
		
		return new AJaxResult.Builder(resultCode)
				.attribute("scriptDetailList", detailList)
				.build();
	}
	
	/**
	 * 스크립트 디테일 추가
	 * @param detailEditDTO
	 * @param scriptStepPk
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = {"","/"}, 
					method = RequestMethod.POST, 
					produces = "application/json")
	public AJaxResult addScriptDetail(			
			@RequestBody ScriptEditParam.Detail detail,
			@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
			HttpServletRequest request) throws Exception{
		
		detail.setScriptStepPk(scriptStepPk);
		detail.setEditType(EditTypes.Create);
		
		logger.info("ScriptEditParam.Detail:" + detail.toString());
		
		ResultBase result = this.detailService.runTask(EditTypes.Create, detail);	
		ResultCode resultCode = result.toResucltCode();		
		return new AJaxResult.Builder(resultCode)
				.attribute("rsScriptStepDetailPk", detail.getScriptStepDetailPk())
				.build();
	}
	
	/**
	 * 스크립트 디테일 수정
	 * @param detailEditDTO
	 * @param scriptStepPk
	 * @param scriptStepDetailPk
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{scriptStepDetailPk}", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult updateScriptDetail(			
			@RequestBody ScriptEditParam.Detail detail,
			@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
			@PathVariable(value = "scriptStepDetailPk" ) String scriptStepDetailPk,
			HttpServletRequest request) throws Exception{
		
		detail.setScriptStepPk(scriptStepPk);
		detail.setScriptStepDetailPk(scriptStepDetailPk);
		detail.setEditType(EditTypes.Update);
		
		logger.info("ScriptEditParam.Detail:" + detail.toString());
		
		ResultBase result = this.detailService.runTask(EditTypes.Update, detail);	
		ResultCode resultCode = result.toResucltCode();		
		return new AJaxResult.Builder(resultCode).build();
	}
	
	/**
	 * 스크립트 디테일 삭제
	 * @param detailEditDTO
	 * @param scriptStepPk
	 * @param scriptStepDetailPk
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{scriptStepDetailPk}", 
					method = RequestMethod.DELETE, 
					produces = "application/json")
	public AJaxResult deleteScriptDetail(			
			@RequestBody ScriptEditParam.Detail detail,
			@PathVariable(value = "scriptStepPk" ) String scriptStepPk,
			@PathVariable(value = "scriptStepDetailPk" ) String scriptStepDetailPk,
			HttpServletRequest request) throws Exception{
		
		detail.setScriptStepPk(scriptStepPk);
		detail.setScriptStepDetailPk(scriptStepDetailPk);
		detail.setEditType(EditTypes.Delete);
		
		logger.info("ScriptEditParam.Detail:" + detail.toString());
		
		ResultBase result = this.detailService.runTask(EditTypes.Delete, detail);		
		ResultCode resultCode = result.toResucltCode();		
		return new AJaxResult.Builder(resultCode).build();
	}
	
	@ResponseBody 
	@RequestMapping(value = "/listen" , 
					method = RequestMethod.POST, 
					produces = "application/json")
	public AJaxResult getPrelistenTTS(
			@RequestBody TTSParam.Prelisten param) {
		
		String ttsFilePath = this.ttsService.requestRealtiemTTS(param);	
		ResultCode resultCode =  ResultCode.SUCCESS;
		return new AJaxResult.Builder(resultCode)
				.attribute("filePath", ttsFilePath)
				.build();
	}
}
