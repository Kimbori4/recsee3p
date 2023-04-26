package com.furence.recsee.search.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.myfolder.model.MyFolderInfo;
import com.furence.recsee.myfolder.service.MyFolderService;

import org.apache.log4j.Logger;

@Controller
@RequestMapping("/search")
public class SearchController {


	@Autowired
	EtcConfigInfoService etcConfigInfoService;

	@Autowired
	MyFolderService myFolderService;

	@Autowired
	CustomizeInfoService customizeInfoService;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	// Search & Listen
	@RequestMapping(value = "/search")
	public ModelAndView search(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/search/search", request, local, model, "searchNListen");

	}

	//ModelAndView 함수
	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		String evalThema = (String)request.getSession().getAttribute("evalThema");
		String tabMode = (String)request.getSession().getAttribute("tabMode");
		String recsee_mobile = (String)request.getSession().getAttribute("recsee_mobile");
		String HTTP = (String)request.getSession().getAttribute("http");
		String separation_speaker = (String)request.getSession().getAttribute("separation_speaker");
		Logger logger = Logger.getLogger(getClass());

		if(userInfo != null) {
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();

			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10064");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			if(accessResult.size() > 0){
				model.addAttribute("transcriptYn", accessResult.get(0).getReadYn());
			}
			
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10041");
			accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			if(accessResult.size() > 0){
				model.addAttribute("myFolderYn", accessResult.get(0).getReadYn());
			}
			
			MyFolderInfo myfolderInfo = new MyFolderInfo();
			myfolderInfo.setrUserId(userInfo.getUserId());
			
			List <MyFolderInfo> myFolder =  myFolderService.selectMyFolderInfo(myfolderInfo);
			model.addAttribute("myFolder", myFolder);
			
			EtcConfigInfo listenServerInfo = new EtcConfigInfo();
			listenServerInfo.setGroupKey("LISTEN");
			listenServerInfo.setConfigKey("IP");
			List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
			String ip=resultInfo.get(0).getConfigValue();

			listenServerInfo.setConfigKey("PORT");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);

			String port=resultInfo.get(0).getConfigValue();

			model.addAttribute("ip", ip);
			model.addAttribute("port", port);

			// 그리드 복사 기능 사용 여부
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("GRID_COPY");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(resultInfo.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String gridCopy = resultInfo.get(0).getConfigValue().toUpperCase();
			model.addAttribute("gridCopy", gridCopy);
			
			// 데이터 복사 기능 사용 여부 
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("DATA_COPY");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(resultInfo.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String dataCopy = resultInfo.get(0).getConfigValue().toUpperCase();
			model.addAttribute("dataCopy", dataCopy);
			
			if("Y".equals(dataCopy)) { // 데이터 복사 기능 사용하는 경우
				// 전체 컬럼 복사 기능 허용 여부 
				etcConfigInfo.setGroupKey("SEARCH");
				etcConfigInfo.setConfigKey("DATA_COPY_ALL");
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if(resultInfo.isEmpty()) {
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
					etcConfigInfo.setConfigValue(null);
					resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				}
				String dataCopyAll = resultInfo.get(0).getConfigValue().toUpperCase();
				
				CustomizeCopyListInfo  customizeListInfo = new CustomizeCopyListInfo();
				if("N".equals(dataCopyAll) 
						&& userInfo.getUserLevel() != null 
							&& !userInfo.getUserLevel().isEmpty()) {
					customizeListInfo.setrUserId(userInfo.getUserLevel());
				} else if("Y".equals(dataCopyAll)) {
					customizeListInfo.setrUserId("all");
				}
				List<CustomizeCopyListInfo> customizeCopyList = customizeInfoService.selectCustomizeCopyListInfo(customizeListInfo);
				int customizeListTotal = customizeCopyList.size();
	
				ObjectMapper mapper = new ObjectMapper();
				String copyListjson = "";
				
				if(customizeListTotal > 0) { 
					try {
						copyListjson = mapper.writeValueAsString(customizeCopyList.get(0).getAllItem()).replaceAll("\"", "\'");
					} catch (IOException e) {
						logger.error(e);
					}	
				}
				model.addAttribute("customizeCopyList", copyListjson);
			}
			
			// 청취한 녹취 컬럼 색상 변경
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("LISTEN_COLOR");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(resultInfo.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String listenColor = resultInfo.get(0).getConfigValue().toUpperCase();
			model.addAttribute("listenColor", listenColor);
			
			
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				logger.error(e);
			}
			
			// 데이터 복사 기능 사용 여부 
			etcConfigInfo.setGroupKey("FILE_NAME_SETTING");
			etcConfigInfo.setConfigKey("USE_YN");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(resultInfo.isEmpty()) {
				etcConfigInfo.setDesc("FileName Setting Use YN");
				etcConfigInfo.setConfigOption("Y/N");
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				etcConfigInfo.setDesc(null);
				etcConfigInfo.setConfigOption(null);
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			
			model.addAttribute("fileNameSettingYN", resultInfo.get(0).getConfigValue());

			model.addAttribute("recsee_mobile", recsee_mobile);
			model.addAttribute("tabMode", tabMode);
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			model.addAttribute("evalThema", evalThema);
			model.addAttribute("HTTP", HTTP);
			model.addAttribute("gridCopy", gridCopy);
			model.addAttribute("separation_speaker", separation_speaker);
			
			
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);

			model.addAttribute("nowAccessInfo", nowAccessInfo);
			
			
			ModelAndView result = new ModelAndView();
			if( nowAccessInfo.getAccessLevel() != null ){
				model.addAttribute("nowAccessInfo", nowAccessInfo);

				//	마스킹 권한 추가
				nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "searchNListen.masking");
				model.addAttribute("maskingAccessInfo", nowAccessInfo);
				// 조회 및 청취	메모 작성 권한 추가
				nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "SearchNListenMemo");
				model.addAttribute("memoAccessInfo", nowAccessInfo);
//				System.out.println("viewName  :: "+viewName);
				result.setViewName(viewName);
				return result;
			}else{
				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
				rv.setExposeModelAttributes(false);
				return new ModelAndView(rv);
			}
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}
	
//	@RequestMapping(value = "/face_recording")
//	public ModelAndView faceRecording(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/faceRec/face_recording", request, local, model, "realtimeMonitoringOffice");
//	}
}