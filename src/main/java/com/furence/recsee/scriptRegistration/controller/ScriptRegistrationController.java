package com.furence.recsee.scriptRegistration.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.initech.shttp.server.Logger;
@Controller
@RequestMapping("/scriptRegistration")

public class ScriptRegistrationController {	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	// scriptRegistration(스크립트 관리)
	@RequestMapping(value = "/script_Registration")
	public ModelAndView scriptRegistration(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/scriptRegistration/script_Registration", request, local, model, "scriptRegistration");
	}
	
	
	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		String rScriptName = (String)request.getSession().getAttribute("rScriptName");
		String rScriptcode = (String)request.getSession().getAttribute("rScriptcode");

		if(userInfo != null) {
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("rScriptName", rScriptName);
			model.addAttribute("rScriptcode", rScriptcode);
			
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

			String sso = (String)request.getSession().getAttribute("sso") != null ? (String)request.getSession().getAttribute("sso") : "";
			String readYn = "N";
			if ("script".equals(sso)) {
				MMenuAccessInfo menuAccess = new MMenuAccessInfo();
				if(userInfo.getUserLevel() != null) menuAccess.setLevelCode(userInfo.getUserLevel());
				menuAccess.setProgramCode("P20005");
				List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.checkAccessInfo(menuAccess);
				if (menuAccessList != null && menuAccessList.size() > 0) {
					readYn = menuAccessList.get(0).getReadYn();
				}
				model.addAttribute("readYn", readYn);
			}
			
			ModelAndView result = new ModelAndView();
			if("script".equals(sso) && "N".equals(readYn)) {
				result.setViewName("/loginFail");
				return result;
			} else if(nowAccessInfo.getAccessLevel() != null ){
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

	
	// scriptRegistration(스크립트 검색)
		@RequestMapping(value = "/script_Search")
		public ModelAndView scriptSearch(HttpServletRequest request, Locale local, Model model) {
			return setModelAndView("/scriptRegistration/script_Search", request, local, model, "scriptRegistration");
		}
		
	

	
	
	
	
}