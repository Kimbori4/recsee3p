package com.furence.recsee.audioCalibration.controller;

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
import com.furence.recsee.common.util.StringUtil;
import com.initech.shttp.server.Logger;

@Controller
@RequestMapping("/audioCalibration")
public class AudioCalibrationController {
	
	@RequestMapping(value = "/audio_calibration")
	public ModelAndView face_recording(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/audioCalibration/audio_calibration", request, local, model, "audioCalibration");
	}
	
	@RequestMapping(value = "/audio_calibration_popup")
	public ModelAndView audioCalibrationPopup(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		model.addAttribute("userId",userInfo.getUserId());
		
		if(!StringUtil.isNull(request.getParameter("audioFilePath"))) {
			model.addAttribute("audioPath",request.getParameter("audioFilePath"));
		}
		
		result.setViewName("/audioCalibration/audio_calibration_popup");		
		
		return result;
	}
	
	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");

		if(userInfo != null) {
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}
			model.addAttribute("userInfo", userInfo);
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