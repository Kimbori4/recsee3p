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
import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.service.ScriptEditTransactionService;


@Controller
@RequestMapping("/wooribank/script/api/edit")
public class ScriptEditTransactionApiController {

	private static final Logger logger = 
			LoggerFactory.getLogger(ScriptEditTransactionApiController.class);
	
	@Autowired
	private ScriptEditTransactionService transactionService;
	
	
	/**
	 * 스크립트 수정 트랜잭션 시작
	 * @param commonDto
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}/begin", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult beginTransaction(	
			@PathVariable(value = "productPk") String productPk ,
			ScriptEditParam.Transaction transactionDto ,
			HttpServletRequest  request) 
			throws Exception{
		
		LoginVO userInfo = SessionManager.getUserInfo(request);

		transactionDto.setEditUser(userInfo.getUserId());
		transactionDto.setProductPk(productPk);
		
		logger.info("ScriptEditTransactionDTO:" + transactionDto);
		
		ResultCode resultCode = this.transactionService.transactionBegin(transactionDto);
		
		// 실패라면
		if(resultCode != ResultCode.SUCCESS) {
			return new AJaxResult.Builder(resultCode).build();
		}
		
		String newTransactionId = transactionDto.getTransactionId();
		
		// 상품 PK에 대해서 트랜잭션 아이디를 생성하여 세션에 임시 저장
		SessionManager.setAttribute(request, newTransactionId, productPk);
		
		return new AJaxResult.Builder(true)
				.attribute("transactionId", newTransactionId)
				.build();
	}
	
	
	/**
	 * 스크립트 수정 트랜잭션 종료
	 * @param transactionId
	 * @param transactionDto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}/end", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult endTransaction(	
			@PathVariable(value = "productPk") String productPk ,
			@RequestBody ScriptEditParam.Transaction transactionDto,
			HttpServletRequest  request)
			throws Exception{

		logger.info("ScriptEditTransactionDTO:" + transactionDto);
				
		// 세션에 저장된 트랜잭션
		String transactionId = transactionDto.getTransactionId();
		String savedProduckPk = SessionManager.getStringAttr(request, transactionId);
		
		// 세션에 트랜잭션이 없으면 리턴
		if( null == savedProduckPk || 
			false == savedProduckPk.equals(productPk) ) {
			return new AJaxResult.Builder(ResultCode.NOT_FOUND_TRANSACTION).build();
		}
		LoginVO userInfo = SessionManager.getUserInfo(request);
		transactionDto.setApproveUser(userInfo.getUserId());		
		transactionDto.setProductPk(productPk);
		
		ResultCode resultCode = this.transactionService.transactionEnd(transactionDto);
		
		if (resultCode == ResultCode.SUCCESS) {
			// 트랜잭션 삭제
			SessionManager.getSession(request).removeAttribute(transactionId);
		}
		
		return new AJaxResult.Builder(resultCode).build();
	}
	
//	/**
//	 * 다른 상품에 적용된 스크립트를 스크립트가 미등록된  상품에 복사적용.
//	 * @param productPk
//	 * @param copy
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/copy/{productPk}", 
//					method = RequestMethod.PUT, 
//					produces = "application/json")
//	public AJaxResult productScriptCopy(
//			@PathVariable(value = "productPk") String productPk ,
//			@RequestBody ProductParam.Copy copy ) {
//		
//		copy.setSrcProductPk(productPk);
//		
//		logger.info("ScriptEditParam.Copy:{}", copy);
//		
//		ResultCode code = this.transactionService.copyScript(copy);
//		
//		return new AJaxResult.Builder(code).build();
//	}
	
	
	/**
	 * 다른 상품에 적용된 스크립트를 스크립트가 미등록된  상품에 복사적용 결재 요청
	 * @param productPk
	 * @param copy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copy/{productPk}", 
					method = RequestMethod.PUT, 
					produces = "application/json")
	public AJaxResult productScriptCopyApprove(
			@PathVariable(value = "productPk") String productPk ,
			@RequestBody ProductParam.CopyApprove copy,
			HttpServletRequest  request) {
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		copy.setApproveUser(userInfo.getUserId());	
		copy.setSrcProductPk(productPk);
		
		logger.info("ScriptEditParam.Copy:{}", copy);
		
		ResultCode code = this.transactionService.copyScriptForApprove(copy);
		
		return new AJaxResult.Builder(code).build();
	}
}
