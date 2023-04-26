package com.furence.recsee.statistics.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.util.SessionManager;
import com.initech.shttp.server.Logger;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

	// 대시보드
	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/statistics/dashboard", request, local, model, "dashboard");
	}
	// 전체 콜 통계
	@RequestMapping(value = "/report_call_all")
	public ModelAndView callAll(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/statistics/report_call_all", request, local, model, "callAll");
	}
	// 사용자별 콜 통계
	@RequestMapping(value = "/report_call_user")
	public ModelAndView callUser(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/statistics/report_call_user", request, local, model, "callUser");
	}
	// 모바일 콜 통계
	@RequestMapping(value = "/report_call_mobile")
	public ModelAndView callMobile(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/statistics/report_call_mobile", request, local, model, "callMobile");
	}
	// 일자 시간 사용자별 콜 통계
	@RequestMapping(value = "/report_call_day_time_user")
	public ModelAndView callDayTimeUser(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/statistics/report_call_day_time_user", request, local, model, "callDayTimeUser");
	}

	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		String evalThema = (String)request.getSession().getAttribute("evalThema");
		
		if(userInfo != null) {
			
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
			model.addAttribute("evalThema", evalThema);
			
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
