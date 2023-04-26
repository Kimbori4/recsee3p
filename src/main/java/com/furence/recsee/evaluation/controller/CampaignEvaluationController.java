package com.furence.recsee.evaluation.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.evaluation.service.EvaluationResultInfoService;
import com.furence.recsee.evaluation.service.EvaluationService;

@Controller
@RequestMapping("/evaluation")
public class CampaignEvaluationController {
	private static final Logger logger = LoggerFactory.getLogger(CampaignEvaluationController.class);

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private EvaluationResultInfoService evaluationResultInfoService;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private MessageSource messageSource;

	//  평가1
	@RequestMapping(value = "/evaluation")
	public ModelAndView evaluation(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/evaluation", request, local, model, "evaluation");
		String evalThema = (String)request.getSession().getAttribute("evalThema");

		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/evaluation", request, local, model, "evaluation");
		}else {
			return setModelAndView("/evaluation/evaluation", request, local, model, "evaluation");
		}
	}
	//  평가2
	@RequestMapping(value = "/evaluation_manage")
	public ModelAndView evaluationManage(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/evaluation_manage", request, local, model, "sheetManage");
		String evalThema = (String)request.getSession().getAttribute("evalThema");
//		System.out.println("현재 사용중인 평가 테마 :: "+evalThema);
	
		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/evaluation_manage", request, local, model, "sheetManage");
		}else {
			return setModelAndView("/evaluation/evaluation_manage", request, local, model, "sheetManage");
		}
	}
	@RequestMapping(value = "/campaign_manage")
	public ModelAndView campaignManage(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/campaign_manage", request, local, model, "campaignManage");
		String evalThema = (String)request.getSession().getAttribute("evalThema");

		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/campaign_manage", request, local, model, "campaignManage");
		}else {
			return setModelAndView("/evaluation/campaign_manage", request, local, model, "campaignManage");
		}
	}
	@RequestMapping(value = "/question_manage")
	public ModelAndView questionManage(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/question_manage", request, local, model, "itemManage");
		String evalThema = (String)request.getSession().getAttribute("evalThema");

		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/question_manage", request, local, model, "itemManage");
		}else {
			return setModelAndView("/evaluation/question_manage", request, local, model, "itemManage");
		}
	}
	@RequestMapping(value = "/evaluation_result")
	public ModelAndView evaluationResult(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/evaluation_result", request, local, model, "evaluationList");
		String evalThema = (String)request.getSession().getAttribute("evalThema");

		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/evaluation_result", request, local, model, "evaluationList");
		}else {
			return setModelAndView("/evaluation/evaluation_result", request, local, model, "evaluationList");
		}
	}
	@RequestMapping(value = "/evaluationStatistics")
	public ModelAndView evaluationStatistics(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/evaluationStatistics", request, local, model, "evaluationStatistics");
		String evalThema = (String)request.getSession().getAttribute("evalThema");

		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/evaluationStatistics", request, local, model, "evaluationStatistics");
		}else {
			return setModelAndView("/evaluation/evaluationStatistics", request, local, model, "evaluationStatistics");
		}
	}

	@RequestMapping(value = "/evaluation_evaluating")
	public ModelAndView evaluating(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/evaluation/evaluation_evaluating", request, local, model, "evaluating");
		String evalThema = (String)request.getSession().getAttribute("evalThema");

		if(evalThema.equals("master")) {
			return setModelAndView("/evaluation_master/evaluation_evaluating", request, local, model, "evaluating");
		}else {
			return setModelAndView("/evaluation/evaluation_evaluating", request, local, model, "evaluating");
		}
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
				logger.error("error",e);
			}
			model.addAttribute("userInfoJson", json);

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
