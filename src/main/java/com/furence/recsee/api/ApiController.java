package com.furence.recsee.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.api.model.apiXml;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.service.ScriptRegistrationService;

@Controller
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	@Autowired 
	private ScriptRegistrationService scriptRegistrationService;
	
	@Autowired
	private SearchListInfoService searchListInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@RequestMapping(value = "/getRecState", produces = "application/xml")
	public @ResponseBody apiXml getRecState(HttpServletRequest request, HttpServletResponse response){
	
		response.setHeader("Access-Control-Allow-Origin", "*");
		boolean recUrl = true;
		apiXml xmls = null;
		xmls = new apiXml();
		xmls.setXmlns("RecFilesRoot");

		String recKey="";
		List<ScriptRegistrationInfo> result =null;
		xmls.setErrorMessage("");
		
		if(request.getParameter("key")!=null && !"".equals(request.getParameter("key"))){
			recKey=request.getParameter("key");
			xmls.setRet("Y");
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("HTTPS");
			String http = "http";
			List<EtcConfigInfo> etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (etcConfigInfoResult != null && etcConfigInfoResult.size() > 0) {
				http = etcConfigInfoResult.get(0).getConfigValue();
			}
			
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("PLAYER");
			etcConfigInfo.setConfigKey("IP");
			String playerIp = "127.0.0.1";
			etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (etcConfigInfoResult != null && etcConfigInfoResult.size() > 0) {
				playerIp = etcConfigInfoResult.get(0).getConfigValue();
			}
			
			etcConfigInfo.setConfigKey("PORT");
			String playerPort = "8080";
			etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (etcConfigInfoResult != null && etcConfigInfoResult.size() > 0) {
				playerPort = etcConfigInfoResult.get(0).getConfigValue();
			}
			
			
			
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			scriptRegistrationInfo.setrScriptCallKey(recKey);
			scriptRegistrationInfo.setrScriptRecState("Y");
			
			try {
				result = scriptRegistrationService.selectRecState(scriptRegistrationInfo);
			}catch(Exception e){
				logger.error("error",e);
			}
			
			if(result==null || result.size()==0) {
				xmls.setNormalRec("0");
				xmls.setFilePathMain("0");
				
				xmls.setErrorMessage("could not find rec list");
				return xmls;
			}else {
				xmls.setNormalRec("1");
				
				for(int i=0 ;i<result.size();i++ ) {
					String state = result.get(i).getrScriptRecState();
					String key = result.get(i).getrSccriptStepCallKey();
					
					if(key ==null || key.length()==0) {
						xmls.setNormalRec("0");
						xmls.setFilePathMain("0");
						return xmls;
					}else if(state==null || !"Y".equals(state)) {
						xmls.setNormalRec("0");
						xmls.setFilePathMain("0");
						return xmls;
					}
				}
				
				SearchListInfo searchInfo = new SearchListInfo();
				searchInfo.setCallId1(recKey);
				searchInfo.setBuffer10("Y");
				searchInfo.setRecVisible("N");
				
				List<SearchListInfo> result2 = searchListInfoService.selectApiListenUrl(searchInfo);
				
				if(result2!=null) {
					// xmls.setFilePathMain("https://aiprecsys.woorifg.com:8443/recseePlayer/?recKey="+recKey);
					xmls.setFilePathMain(http + "://"+playerIp+":"+playerPort+"/recseePlayer/?recKey="+recKey);
				}else {
					xmls.setFilePathMain("0");
				}
			}
			
		}else {
			xmls.setNormalRec("0");
			xmls.setFilePathMain("0");
			xmls.setErrorMessage("invalid key");
		}
		
		
		return xmls;
	}

	@RequestMapping(value = "/getTaState", produces = "application/xml")
	public @ResponseBody apiXml getTaState(HttpServletRequest request, HttpServletResponse response){
	
		response.setHeader("Access-Control-Allow-Origin", "*");
		boolean recUrl = true;
		apiXml xmls = null;
		xmls = new apiXml();
		xmls.setXmlns("RecFilesRoot");

		String recKey="";
		List<ScriptRegistrationInfo> result =null;
		xmls.setErrorMessage("");
		
		if(request.getParameter("key")!=null && !"".equals(request.getParameter("key"))){
			recKey=request.getParameter("key");
			xmls.setRet("Y");
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("HTTPS");
			String http = "http";
			List<EtcConfigInfo> etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (etcConfigInfoResult != null && etcConfigInfoResult.size() > 0) {
				http = etcConfigInfoResult.get(0).getConfigValue();
			}
			
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("PLAYER");
			etcConfigInfo.setConfigKey("IP");
			String playerIp = "127.0.0.1";
			etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (etcConfigInfoResult != null && etcConfigInfoResult.size() > 0) {
				playerIp = etcConfigInfoResult.get(0).getConfigValue();
			}
			
			etcConfigInfo.setConfigKey("PORT");
			String playerPort = "8080";
			etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (etcConfigInfoResult != null && etcConfigInfoResult.size() > 0) {
				playerPort = etcConfigInfoResult.get(0).getConfigValue();
			}
			
			
			
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			scriptRegistrationInfo.setrScriptCallKey(recKey);
			scriptRegistrationInfo.setrScriptRecState("Y");
			
			try {
				//TODO 이 부분 수정해야함
				result = scriptRegistrationService.selectTaState(scriptRegistrationInfo);
			}catch(Exception e){
				logger.error("error",e);
			}
			
			if(result==null || result.size()==0) {
				xmls.setNormalRec("0");
				xmls.setErrorMessage("could not find rec list");
				xmls.setFilePathMain("0");
				return xmls;
			}else {
				xmls.setNormalRec("2");
				xmls.setFilePathMain("0");
			}
			
		}else {
			xmls.setNormalRec("0");
			xmls.setFilePathMain("0");
			xmls.setErrorMessage("invalid key");
		}
		
		
		return xmls;
	}
	
}

