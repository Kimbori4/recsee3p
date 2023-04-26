package com.furence.recsee.transcript.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.transcript.service.TranscriptService;
import com.initech.shttp.server.Logger;

@Controller
@RequestMapping("/transcript")
public class TranscriptController {

	@Autowired
	EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	TranscriptService transcriptService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@RequestMapping(value = "/transcript")
	public ModelAndView search(HttpServletRequest request, Locale local, Model model) {
		
		return setModelAndView("/transcript/transcript", request, local, model, "transcript");
	}
	
	@RequestMapping(value = "/learning")
	public ModelAndView learning(HttpServletRequest request, Locale local, Model model) {
		
		return setModelAndView("/transcript/learning", request, local, model, "learningTranscript");
	}
	
	@RequestMapping(value = "/sttModel")
	public ModelAndView sttModel(HttpServletRequest request, Locale local, Model model) {
		
		return setModelAndView("/transcript/sttModel", request, local, model, "sttModel");
	}
	
	@RequestMapping(value = "/sttEnginState")
	public ModelAndView sttEnginState(HttpServletRequest request, Locale local, Model model) {
		
		return setModelAndView("/transcript/sttEnginState", request, local, model, "sttEnginState");
	}
	
	@RequestMapping(value = "/sttResultManage")
	public ModelAndView sttResultManage(HttpServletRequest request, Locale local, Model model) {
		
		return setModelAndView("/transcript/sttResultManage", request, local, model, "sttResultManage");
	}
	
	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");

		if(userInfo != null) {
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();

			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10064");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			if(accessResult.size() > 0){
				model.addAttribute("writeYn", accessResult.get(0).getWriteYn());
				model.addAttribute("delYn", accessResult.get(0).getDelYn());
			}
			
			EtcConfigInfo etcConfigInfo= new EtcConfigInfo(); 
			etcConfigInfo.setGroupKey("URL");
			etcConfigInfo.setConfigKey("HTTPS");					
			List<EtcConfigInfo> httpResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			if (httpResult.size() > 0) {
				model.addAttribute("http", httpResult.get(0).getConfigValue());
			}
			
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
			
			model.addAttribute("userInfo", userInfo);
			
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}
			
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);
			model.addAttribute("nowAccessInfo", nowAccessInfo);

			ModelAndView result = new ModelAndView();
			if( nowAccessInfo.getAccessLevel() != null ){
				model.addAttribute("nowAccessInfo", nowAccessInfo);
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
}
