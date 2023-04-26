package com.furence.recsee.wooribank.script.controller.api;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.request.TTSParam;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.param.response.ScriptSnapshot;
import com.furence.recsee.wooribank.script.repository.entity.ProductGroup;
import com.furence.recsee.wooribank.script.repository.entity.ProductHistory;
import com.furence.recsee.wooribank.script.service.ProductService;
import com.furence.recsee.wooribank.script.service.ScriptTtsService;
import com.furence.recsee.wooribank.script.util.DhtmlGridDataProvider;


@Controller
@RequestMapping("/wooribank/script/api/product")
public class ProductApiController {

	private static final Logger logger = LoggerFactory.getLogger(ProductApiController.class);
	
	private ProductService productService;
	
	private ScriptTtsService ttsService;
	
	
	@Autowired
	public ProductApiController(ProductService productService, 
			ScriptTtsService ttsService ) {
		
		this.productService = productService;
		this.ttsService = ttsService;
	}
	
	/**
	 * 상품 리스트 조회
	 * @param resourceType
	 * @param searchDto
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody 
	@RequestMapping(value = "/list/{resourceType}", 
					method = RequestMethod.GET, 
					produces = "application/xml")
	public dhtmlXGridXml searchProductList(
			@PathVariable(value = "resourceType") String resourceType,
			ProductParam.Search searchDto,			
			HttpServletRequest request)  {
		
		logger.info("searchDto:" + searchDto.toString());
		
		String contextPath = request.getContextPath();
		String [] args = new String[2];
		args[0] = contextPath;
					
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		
		// 헤더는 우선 처리
		String header = StringUtils.stripToEmpty(searchDto.getHeader());
		boolean isHeader = header.contentEquals("true");
		
		if( isHeader ){
			return this.productService.getDhtmlxHeaderOfProductList(searchDto, sessAccessList, args);
		}
		
		return this.productService.getDhtmlxRowOfProductList(searchDto, sessAccessList, args);
		
	} 
	
	/**
	 * 상품의 pk(그룹pk가 아님)를 전달받아 , 상품정보 및 소속 상품그룹의 상품리스트 조회
	 * @param productPk
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}", 
					method = RequestMethod.GET, 
					produces = "application/json")
	public AJaxResult searchProductInfo(
			@PathVariable(value = "productPk") String productListPk,
			HttpServletRequest request) throws Exception{
		
		logger.info("productPk:" + productListPk);
		
		ResultCode resultCode = ResultCode.SUCCESS;
		ProductGroup group = null;
		
		try {
			group = this.productService.getProductById(productListPk);			
			group.setProductPk(productListPk);
			logger.info("ProductGroup:{}", group);
			
		} catch(Exception e) {
			resultCode = ResultCode.SYSTEM_ERROR;
			logger.error("error", e);			
		}
		
		return new AJaxResult.Builder(resultCode)
				.attribute("productInfo", group)
				.build();
	}

	/**
	 * 상품스크립트 변경이력 조회
	 * @param productPk
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}/history/check", 
					method = RequestMethod.GET, 
					produces = "application/json")
	public AJaxResult checkProductHistory(
			@PathVariable(value = "productPk") String productListPk,
			HttpServletRequest request) throws Exception{
		
		logger.info("productPk:" + productListPk);
		
		ResultCode resultCode = ResultCode.SUCCESS;
		
		try {
			
			boolean isExist = this.productService.isExistHistory(productListPk);
			return new AJaxResult.Builder(resultCode)
					.attribute("isExist",isExist)
					.build();
			
		} catch(Exception e) {
			logger.error("error", e);			
		}
		
		return new AJaxResult.Builder(ResultCode.SYSTEM_ERROR).build();
	}
	
	/**
	 * 상품스크립트 변경이력 조회
	 * @param productPk
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}/history", 
					method = RequestMethod.GET, 
					produces = "application/json")
	public AJaxResult searchProductHistory(
			@PathVariable(value = "productPk") String productListPk,
			@RequestParam(value = "v") String dummyString,
			HttpServletRequest request) throws Exception{
		
		logger.info("productPk:" + productListPk);
		
		ResultCode resultCode = ResultCode.SUCCESS;
		
		try {
			
			List<ProductHistory> list = this.productService.getHistoryById(productListPk);
			return new AJaxResult.Builder(resultCode)
					.attribute("history",list)
					.build();
			
		} catch(Exception e) {
			logger.error("error", e);			
		}
		
		return new AJaxResult.Builder(ResultCode.SYSTEM_ERROR).build();
	}
	
	/**
	 * 상품스크립트 변경이력 상세 조회
	 * @param productPk
	 * @param version
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}/history/{version}", 
					method = RequestMethod.GET, 
					produces = "application/json")
	public AJaxResult searchProductHistoryDetail(
			@PathVariable(value = "productPk") String productListPk,
			@PathVariable(value = "version") String version,
			HttpServletRequest request) throws Exception{
				
		ScriptEditParam.History param = new ScriptEditParam.History();
		param.setProductPk(productListPk);
		param.setScriptVersion(version);
		
		logger.info("ScriptEditParam.ScriptHistory:" + param);
		
		ResultCode resultCode = ResultCode.SUCCESS;
		
		try {
			ScriptSnapshot snapshot = this.productService.getVersionSnapshot(param);
			return new AJaxResult.Builder(resultCode)
					.attribute("scriptSnapshot",snapshot)
					.build();
			
		} catch(Exception e) {
			logger.error("error", e);			
		}
		
		return new AJaxResult.Builder(ResultCode.SYSTEM_ERROR).build();
	}
	
	
	
	/**
	 * 상품 리스트 조회
	 * @param resourceType
	 * @param searchDto
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody 
	@RequestMapping(value = "/list/notregistered/{resourceType}", 
					method = RequestMethod.GET, 
					produces = "application/xml")
	public dhtmlXGridXml searchProductCopy(
			@PathVariable(value = "resourceType") String resourceType,
			ProductParam.NotRegistered copyParam,			
			HttpServletRequest request)  {

		logger.info("notregistered:" + copyParam.toString());
		
		String header = StringUtils.stripToEmpty(copyParam.getHeader());
		boolean isHeader = header.contentEquals("true");
		
		// 헤더는 우선 처리
		if( isHeader ){
			String contextPath = request.getContextPath();
			String [] args = {contextPath};
			return DhtmlGridDataProvider.ScriptCopyGrid.getHeaders( args );
		}
		
		// 예외발생시 빈 로우 리턴
		try {
			return this.productService.getDhtmlxRowOfCopyProductList(copyParam);
		} catch(Exception e) {
			logger.error("error", e);
			return new dhtmlXGridXml();
		}
	} 
	

	/**
	 * 상품 스크립트디테일의 텍스트 TTS 미리듣기 처리
	 * @param productPk
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/{productPk}/tts/premake", 
					method = RequestMethod.POST, 
					produces = "application/json")
	public AJaxResult generatePremakeTTSFile(
			@PathVariable(value = "productPk") String productPk,
			@RequestBody TTSParam.Prelisten param,
			HttpServletRequest request) throws Exception{
				
		param.setProductPk(productPk);
		
		logger.info("TTSParam.Premake:" + param);
		
		ResultCode resultCode = ResultCode.SUCCESS;
		
		// TTS 생성 요청 및 다운로드후 경로
		String ttsFilePath = this.ttsService.requestRealtiemTTS(param);
		
		return new AJaxResult.Builder(resultCode)
				.attribute("ttsFilePath", ttsFilePath).build();
	}
	
	
	/**
	 * ELT 상품체크 YN 반환
	 * @param productPk
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/eltcheck", 
	method = RequestMethod.POST, 
	produces = "application/json")
	public AJaxResult eltCheck(HttpServletRequest request , @RequestParam(value = "productListPk") String productListPk) throws Exception{
		
		logger.info("productListPk:" + productListPk);
		
		ResultCode resultCode = ResultCode.SUCCESS;
		
		String check = productService.isEltProductCheck(productListPk);
		
		// TTS 생성 요청 및 다운로드후 경로
		
		return new AJaxResult.Builder(resultCode).attribute("result", check)
				.build();
	}
	
}
