package com.furence.recsee.wooribank.script.controller.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.client.RestTemplate;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.batch.BatchTaskExecutor;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.param.request.ScriptApproveParam;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.repository.dao.ExcelUploadDao;
import com.furence.recsee.wooribank.script.service.AsyncTaskService;
import com.furence.recsee.wooribank.script.service.ScriptEditApproveService;

@Controller
@RequestMapping("/wooribank/script/api/approval")
public class ScriptEditApproveApiController {
		
	private static final Logger logger = LoggerFactory.getLogger(ScriptEditApproveApiController.class);
	
	private ScriptEditApproveService approveService;
	
	private AsyncTaskService asyncService;
	
	private BatchTaskExecutor batchExecutor;
	
	@Autowired
	public ScriptEditApproveApiController(
			ScriptEditApproveService approveService, 
			AsyncTaskService asyncService, 
			BatchTaskExecutor batchExecutor ){
		
		this.approveService = approveService;
		this.asyncService = asyncService;
		this.batchExecutor = batchExecutor;
	}
	
	@Autowired
	private ExcelUploadDao uploadMapper;
	
	
	/**
	 * 상신목록 조회
	 * @param resourceType
	 * @param paging
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list/{resourceType}", 
	method 	= { RequestMethod.GET } , 
	produces= "application/xml")
	public dhtmlXGridXml searchWaitingApproveList(
			@PathVariable(value = "resourceType") String resourceType,
			ScriptApproveParam.Search search,
			HttpServletRequest request) {
		
		logger.info("ScriptApproveParam.Search:" + search.toString());
		
		String header = StringUtils.stripToEmpty(search.getHeader());
		boolean isHeader = header.contentEquals("true");				

		// 헤더는 우선 처리
		if( isHeader ){
			String contextPath = request.getContextPath();
			String [] args = new String[2];
			args[0] = contextPath;
			args[1] = search.getApproveStatus();
			return this.approveService.getDhtmlxHeaderOfApprovalList(search, args);
		}
		
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> accessInfo = (List<MMenuAccessInfo>)SessionManager.getSession(request).getAttribute("AccessInfo");
		
		try {

			// 권한 기준으로 결재권한 판단
			accessInfo.stream()	
				.filter(info -> info.getProgramCode() != null 
								&& (info.getProgramCode().equals("P20006") || info.getProgramCode().equals("P20008")) )
				.map( info -> Optional.ofNullable(info.getAccessLevel()).orElse("") )
				.filter( level -> level.length() == 2 && level.startsWith("P"))
				.findFirst()
				.ifPresent( level -> {
					search.setProductPart(level);
					logger.info("ScriptApproveParam.Search:" + search.toString());
				});
			
			return this.approveService.getDhtmlxRowOfApprovalList(search);
			
		} catch(Exception e) {
			logger.error("error", e);
			return new dhtmlXGridXml();
		}
		
	}

	
	/**
	 * 수정예정인 스크립트 미리보기
	 * @param transactionId
	 * @param scriptType
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{transactionId}",
					method = { RequestMethod.GET } ,
					produces= "application/json;charset=UTF-8")
	public AJaxResult searchWaitingApproveDetail(
			@PathVariable(value = "transactionId") String transactionId,
			@RequestParam(value = "scriptType" , defaultValue = "c") String scriptType, 
			HttpServletRequest request) {
		
		logger.info("transactionId:" + transactionId);
		ResultCode resultCode = ResultCode.SUCCESS;
		
		try {
			
			Const.ScriptKind kind = 
					Optional.ofNullable(Const.ScriptKind.create(scriptType.toUpperCase()))
						.orElseThrow();	
			
			return new AJaxResult.Builder(resultCode)
					.attribute("compareData", this.approveService.getComparablePreviewScript(transactionId, kind))
					.build();
			
		} catch(Exception e) {
			logger.error("error", e);			
		}
  
		return new AJaxResult.Builder(ResultCode.SYSTEM_ERROR).build();
	}
	
	
	
	/**
	 * 상신 결재처리
	 * @param transactionId
	 * @param scriptType
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{transactionId}",
					method = { RequestMethod.PUT } ,
					produces= "application/json;charset=UTF-8")
	public AJaxResult completeApprove(
			@PathVariable(value = "transactionId") String transactionId,
			@RequestBody ScriptApproveParam approve,
			HttpServletRequest request) {
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		approve.setScriptEditId(transactionId);
		approve.setApproveUser(userInfo.getUserId());
		logger.info("ScriptEditParam.Approve:" + approve);
		
		ResultCode resultCode = this.approveService.completeApprove(approve);
  
		return new AJaxResult.Builder(resultCode).build();
	}
	
	@RequestMapping(value = "dailytask" , method = RequestMethod.GET)
	public String batchTest() throws Exception{
		
		this.asyncService.execute(() -> {
			this.batchExecutor.executeDailyScriptSnapshot();
		});		
		return "1";
	}
	@RequestMapping(value = "/elt/{transactionId}" , method = RequestMethod.PUT)
	public String nonRelEltUpdate(HttpServletRequest request , @PathVariable(value = "transactionId") String tId) {
		
		//트랜잭션 Id로 detailPk 가져오기
		String flag;
		try {
			flag = uploadMapper.selectNonRefEltCheck(tId);
			if(flag.toLowerCase().equals("y")) {
				uploadMapper.updateNonRefEltAttr(tId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("error", e);
		}
		
		//비정규 ELT 체크
		return "1";
	}
	/*
	*/
}
