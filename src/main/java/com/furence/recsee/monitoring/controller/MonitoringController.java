 package com.furence.recsee.monitoring.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.ChannelInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.TemplateKeyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MAccessLevelInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.KeyDefine;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.login.controller.LoginManager;
import com.furence.recsee.login.service.LoginService;
import com.furence.recsee.monitoring.model.BluePrintInfo;
import com.furence.recsee.monitoring.model.SystemRealtimeInfo;
import com.furence.recsee.monitoring.service.MonitoringInfoService;
import com.initech.shttp.server.Logger;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MonitoringController.class);
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private ChannelInfoService channelInfoService;
	
	@Autowired
	private MonitoringInfoService monitoringService;
	
	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;	
	
	@Autowired 
	private MAccessLevelInfoService accessLevelInfoService;
		
	@Autowired
	private LogService logService;
	
//	LoginManager loginManager = LoginManager.getInstance();
	
	@Autowired 
	private LoginManager loginManager;
	
	// 모니터링 - 실시간 모니터링 기본 카드 뷰
	@RequestMapping(value = "/realtime")
	public ModelAndView realtimeMonitoring(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/monitoring/realtime", request, local, model, "realtimeMonitoring");
	}

	// 모니터링 - 실시간 모니터링 그리드 뷰
		@RequestMapping(value = "/realtime_grid")
		public ModelAndView realtimeGridMonitoring(HttpServletRequest request, Locale local, Model model) {
			return setModelAndView("/monitoring/realtime_grid", request, local, model, "realtimeMonitoringGrid");
	}

	// 모니터링 - 오피스 모니터링 -팝업
	@RequestMapping(value = "/office_monitoring")
	public ModelAndView officeMonitoring(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/monitoring/office_monitoring", request, local, model, "realtimeMonitoringOffice");
	}

	// 모니터링 - 오피스 모니터링 - 기본 화면
	@RequestMapping(value = "/office_monitoring_view")
	public ModelAndView officeMonitoringView(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/monitoring/office_monitoring_view", request, local, model, "realtimeMonitoringOffice");
	}
	
	// 모니터링 - 도면 모니터링
	@RequestMapping(value = "/realtime_blueprint")
	public ModelAndView realtimeBlueprint(HttpServletRequest request, Locale local, Model model) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		BluePrintInfo bluePrintInfo = new BluePrintInfo();
		bluePrintInfo.setrBlueprintjoinId(userInfo.getUserId());
		bluePrintInfo.setrBlueprintshareId(userInfo.getUserId());
		List<BluePrintInfo> selectBluePrint = monitoringService.selectBluePrint(bluePrintInfo);
		
		model.addAttribute("bluePrint",selectBluePrint);
		
		return setModelAndView("/monitoring/realtime_blueprint", request, local, model, "BluePrintMonitoring");
	}

	// 모니터링 - 시스템 모니터링
	@RequestMapping(value = "/system")
	public ModelAndView systemMonitoring(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/monitoring/system", request, local, model, "systemMonitoring");
	}
	@RequestMapping(value = "/system2")
	public ModelAndView systemMonitoring2(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/monitoring/system2", request, local, model, "systemMonitoring");
	}	
	// 모니터링 - 시스템 모니터링
	@RequestMapping(value = "/channel")
	public ModelAndView channelMonitoring(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/monitoring/channel", request, local, model, "serverMonitoring");
	}

	@RequestMapping(value = "/system_realtime")
	public ModelAndView system_pie(HttpServletRequest request, Locale local, Model model) {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("SYSTEM_MONITORING_REDIS_IP");
		List<EtcConfigInfo> redisIpConfig = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		String redisIpResult = "";
		if (redisIpConfig.size() > 0) {
			String redisGroupCode = redisIpConfig.get(0).getConfigValue();
			SystemRealtimeInfo sysRealtimeInfo = new SystemRealtimeInfo();
			sysRealtimeInfo.setrSysGroupCode(redisGroupCode);
			List<SystemRealtimeInfo> sysRealtimeInfoResult = monitoringService.selectSystemRealtimeInfo(sysRealtimeInfo);
			
			for (int i = 0; i < sysRealtimeInfoResult.size(); i++) {
				redisIpResult += ","+sysRealtimeInfoResult.get(i).getrSysIp();
			}
			redisIpResult = redisIpResult.substring(1);
		} else {
			redisIpResult = "127.0.0.1";
		}
		
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("REDIS_PORT");
		List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("REDIS_PW");
		List<EtcConfigInfo> redisPw = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		String redisTimeoutResult = "0";
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("REDIS_TIMEOUT");
		try {
			List<EtcConfigInfo> redisTimeout = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			redisTimeoutResult = redisTimeout.get(0).getConfigValue();
		} catch(Exception e) {
			etcConfigInfo.setConfigValue("0");
			etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
		}
		
		model.addAttribute("redisIp",redisIpResult);
		model.addAttribute("redisPort",redisPort.get(0).getConfigValue());
		model.addAttribute("redisPw",redisPw.get(0).getConfigValue());
		model.addAttribute("redisTimeout",redisTimeoutResult);

		return setModelAndView("/monitoring/system_realtime", request, local, model, "systemRealtimeMonitoring");
	}

	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		String evalThema = (String)request.getSession().getAttribute("evalThema");
		String HTTP = (String)request.getSession().getAttribute("http");
		
		if("/monitoring/realtime".equals(viewName) && !StringUtil.isNull(request.getParameter("userid"))){
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("POPUP_USE");
			List<EtcConfigInfo> popupUse = null;
			try {
				popupUse = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if("N".equals(popupUse.get(0).getConfigValue())) {
					return null;
				}		
				
			}catch (Exception e) {
				return null;
			}
			
			
			if(StringUtil.isNull(request.getParameter("userid"),true)) {
				return null;
			}
			
			
			String userid = request.getParameter("userid");
			RUserInfo rUserInfo = new RUserInfo();
			
			rUserInfo.setUserId(userid);
			List<RUserInfo> rUserList = ruserInfoService.selectTreeViewRUserInfo(rUserInfo);
			
			if(rUserList.size() == 0) {
				
				return null;
			}
			
			String loginUserId = rUserList.get(0).getUserId();
			
			
			
			LoginVO beenChk = new LoginVO();
			beenChk.setUserId(loginUserId);
			
			LoginVO bean = new LoginVO();
			LoginVO userInfoChk= loginService.selectUserInfo(beenChk);
			
			if(userInfoChk == null) {
				userInfoChk=loginService.selectAuserInfo(beenChk);
			}
			
			// 로그인 체크
			userInfo=null;
			bean.setUserId(loginUserId);
			userInfo= loginService.selectUserInfo(bean);
			if(userInfo == null) {
				userInfo=loginService.selectAuserInfo(bean);
			}

			model.addAttribute("userInfo", userInfo);

			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}

			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LISTEN");
			etcConfigInfo.setConfigKey("IP");

			List<EtcConfigInfo> listenIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			//CTI 사용유무(미사용시 ringing상태일때 calling으로 강제변환 시킴)
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("CTI_USE");
			List<EtcConfigInfo> ctiUse = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(ctiUse.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				ctiUse = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			
			//	처음 인입시 채널 정보로 채널 사용하는지 안하는지 구분 하기 위하여
			List<ChannelInfo> channelInfoResult = channelInfoService.selectChannelInfoGet();
			Integer channelInfoResultTotal = channelInfoResult.size();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < channelInfoResultTotal; i++) {
				obj.put(channelInfoResult.get(i).getExtNum(), channelInfoResult.get(i).getRecYn());
			}
			
			AJaxResVO jRes = new AJaxResVO();
			
			jRes = login_Success(request, userInfo, jRes);
			
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			model.addAttribute("listenIp",listenIp.get(0).getConfigValue());
			model.addAttribute("channelInfo",obj);
			model.addAttribute("evalThema", evalThema);
			model.addAttribute("HTTP", HTTP);
			model.addAttribute("ctiUse",ctiUse.get(0).getConfigValue());
			model.addAttribute("PopupCheck","true");
			/*model.addAttribute("telnoUse", telnoUse);*/
			
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);
			model.addAttribute("nowAccessInfo", nowAccessInfo);


			userInfo = SessionManager.getUserInfo(request);
			systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
			defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
			evalThema = (String)request.getSession().getAttribute("evalThema");
			HTTP = (String)request.getSession().getAttribute("http");
			
			
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
		}else if(userInfo != null) {
			model.addAttribute("userInfo", userInfo);

			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LISTEN");
			etcConfigInfo.setConfigKey("IP");

			List<EtcConfigInfo> listenIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			//CTI 사용유무(미사용시 ringing상태일때 calling으로 강제변환 시킴)
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("CTI_USE");
			List<EtcConfigInfo> ctiUse = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(ctiUse.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				ctiUse = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String masking = "N";
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("Masking");
			List<EtcConfigInfo> maskingYN = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(!maskingYN.isEmpty()) {
				masking = maskingYN.get(0).getConfigValue();
			}
			
			//자번호 사용유무
			/*String telnoUse = "N";
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("company_telno");
			List<EtcConfigInfo> telnoUseYN = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(!telnoUseYN.isEmpty()) {
				telnoUse = telnoUseYN.get(0).getConfigValue();
			}*/
			
			//	처음 인입시 채널 정보로 채널 사용하는지 안하는지 구분 하기 위하여
			List<ChannelInfo> channelInfoResult = channelInfoService.selectChannelInfoGet();
			Integer channelInfoResultTotal = channelInfoResult.size();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < channelInfoResultTotal; i++) {
				obj.put(channelInfoResult.get(i).getExtNum(), channelInfoResult.get(i).getRecYn());
			}
			
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			model.addAttribute("listenIp",listenIp.get(0).getConfigValue());
			model.addAttribute("channelInfo",obj);
			model.addAttribute("evalThema", evalThema);
			model.addAttribute("HTTP", HTTP);
			model.addAttribute("ctiUse",ctiUse.get(0).getConfigValue());
			model.addAttribute("masking",masking);
			/*model.addAttribute("telnoUse", telnoUse);*/
			
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
		}else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}
		
	public AJaxResVO login_Success(HttpServletRequest request, LoginVO userInfo, AJaxResVO jRes) {
		
		// 아이디, 아이피 중복 로그인시 이전 세션 끊었는지 확인하는 용도
		Boolean preSessionId = false;
		Boolean preSessionIp = false;
		
		// 아이디 중복 로그인 가능여부(Y:가능, N:불가능, C:이전 로그인 세션 해제)
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("LOGIN");
		etcConfigInfo.setConfigKey("DUP_LOGIN_ID");
		List<EtcConfigInfo> systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		String dupLoginId = "Y";
		if(systemConfigInfoResult.isEmpty()) {
			etcConfigInfo.setConfigValue("Y");
			etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			etcConfigInfo.setConfigValue(null);
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		}
		dupLoginId = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
		Boolean loginId = true;
		if("N".equals(dupLoginId)) {
			if(loginManager.isUsing(userInfo.getUserId())) {
				loginId = false;
			}
		}else if("C".equals(dupLoginId)) {
			if(loginManager.isUsing(userInfo.getUserId())) {
				loginManager.removeSession(userInfo.getUserId());
				preSessionId = true;
			}
		}
		
		// 아이피 중복 로그인 가능여부(Y:가능, N:불가능, C:이전 로그인 세션 해제)
		etcConfigInfo.setGroupKey("LOGIN");
		etcConfigInfo.setConfigKey("DUP_LOGIN_IP");
		systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		String dupLoginIp = "Y";
		if(systemConfigInfoResult.isEmpty()) {
			etcConfigInfo.setConfigValue("Y");
			etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			etcConfigInfo.setConfigValue(null);
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		}
		dupLoginIp = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
		Boolean loginIp = true;
		if("N".equals(dupLoginIp)) {
			if(loginManager.isUsing(RecSeeUtil.getClientIp(request))) {
				loginIp = false;
			}
		}else if("C".equals(dupLoginIp)) {
			if(loginManager.isUsing(RecSeeUtil.getClientIp(request))) {
				loginManager.removeIpSession(RecSeeUtil.getClientIp(request));
				preSessionIp = true;
			}
		}
		
		
		// 아이디, 아이피 모두 중복조건 통과한 경우
		if(loginId && loginIp) {
			//프로토콜 타입
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("HTTPS");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("http");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String HTTP = systemConfigInfoResult.get(0).getConfigValue();
			
			//ui종류... 이것도 확인 필요
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("TEMPLATES");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("rs");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String systemTemplates = systemConfigInfoResult.get(0).getConfigValue();
	
			//기본스킨? 어디서 쓰는건지 확인...
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("DEFAULT_SKIN");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("recsee");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String defaultSkin = systemConfigInfoResult.get(0).getConfigValue();
			
			//평가테마 확인
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("Evaluation");
			etcConfigInfo.setConfigKey("Eval_Thema");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("recsee");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String defaultEval_thema = systemConfigInfoResult.get(0).getConfigValue();
	
			//모바일 렉시 여부
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("RECSEE_MOBILE");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String RECSEE_MOBILE = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();		
			
			
			String clientIpChk="";
			//로그인 IP체크 여부
			etcConfigInfo.setGroupKey("LOGIN");
			etcConfigInfo.setConfigKey("LOGIN_IP_CHK");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			clientIpChk = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
			
			
			String ipChkAuth="";
			String ipChkDup = "N";
			if("Y".equals(clientIpChk)) {
				etcConfigInfo.setGroupKey("LOGIN");
				etcConfigInfo.setConfigKey("LOGIN_IP_CHK_AUTH");
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if(systemConfigInfoResult.isEmpty()) {
					etcConfigInfo.setConfigValue("E1001");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
					etcConfigInfo.setConfigValue(null);
					systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				}
				
				ipChkAuth = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
				
				etcConfigInfo.setGroupKey("LOGIN");
				etcConfigInfo.setConfigKey("LOGIN_IP_CHK_DUP");
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if(systemConfigInfoResult.isEmpty()) {
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
					etcConfigInfo.setConfigValue(null);
					systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				}
				ipChkDup = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
				
			}
			
			
			String listen_period="";
			//조회 및 청취 기간 제한 여부
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("LISTEN_PERIOD");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			listen_period = systemConfigInfoResult.get(0).getConfigValue().trim();
			
			
			String listen_period_excpet="";
			//조회 및 청취 기간 제한 예외 권한
			if(!"".equals(listen_period)) {
				etcConfigInfo.setGroupKey("SEARCH");
				etcConfigInfo.setConfigKey("LISTEN_PERIOD_LIMITLESS");
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if(systemConfigInfoResult.isEmpty()) {
					etcConfigInfo.setConfigValue("E1001");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
					etcConfigInfo.setConfigValue(null);
					systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				}
				
				listen_period_excpet = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
	
			}
			
			// 다운로드 포맷 선택 기능 사용 여부
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("FORMAT_CHOOSE");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String downloadFormat = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
	
			// STT플레이어 사용 여부
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("PLAYER");
			etcConfigInfo.setConfigKey("STT_PLAYER");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String STTPlayerYN = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
	
			// 탭기능 사용 여부
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MODE");
			etcConfigInfo.setConfigKey("TAB");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String tabMode = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
			
			// 플레이어 변환 기능 사용 여부
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("SEPARATION_SPEAKER");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String separation_speaker = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
			
			//녹취다운로드시 사유 입력
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("REQUEST_REASON");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			
			String requestReason = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
			
			//상담사 그룹정보 변경시 녹취 이력 테이블 업데이트 여부
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("USER");
			etcConfigInfo.setConfigKey("CHANGE_CALL_HISTORY");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			
			String updateGroupinfoYn = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
			
			
			//상담사 그룹정보 변경시 녹취 이력 테이블 업데이트 기간
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("USER");
			etcConfigInfo.setConfigKey("CHANGE_CALL_HISTORY_PERIOD");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("12");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			
			String updateGroupinfoPeriod = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();

			
			//고객전화번호 암호화 사용여부 확인
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("CUST_ENCRYPT_DATE");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(systemConfigInfoResult.isEmpty()) {
				etcConfigInfo.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfo.setConfigValue(null);
				systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			}
			String custEncryptDate = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
			
			
			//자번호 사용유무 asd
			String telnoUse = "N";
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("company_telno");
			List<EtcConfigInfo> telnoUseYN = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(!telnoUseYN.isEmpty()) {
				telnoUse = telnoUseYN.get(0).getConfigValue();
			}
			
			if("Y".equals(telnoUse)) {
				userInfo.setBgCode("");
				userInfo.setMgCode("");
				userInfo.setSgCode("");
				userInfo.setBgCodeName("");
				userInfo.setMgCodeName("");
				userInfo.setSgCodeName("");
			}
			
			//SessionManager.setAttribute(request, KeyDefine.SES_KEY_SYS_CONFIG, systemConfigInfoResult.get(0));
			SessionManager.setAttribute(request, KeyDefine.SES_KEY_USER_INFO, userInfo);
	
			MMenuAccessInfo menuAccess = new MMenuAccessInfo();
			menuAccess.setLevelCode(userInfo.getUserLevel());
			menuAccess.setDisplayLevel(100);
	
			List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccess);
			int menuAccessListTotal = menuAccessList.size();
			
			// 메뉴리스트에 중메뉴(관리하기 하위)도 포함되어 있어서 3개이상임... 중메뉴 늘어나는 경우 숫자 변경해줘야됨
			if(menuAccessListTotal > 3) {
				SessionManager.setAttribute(request, "AccessInfo", menuAccessList);
				SessionManager.setAttribute(request, "systemTemplates", systemTemplates);
				SessionManager.setAttribute(request, "defaultSkin", defaultSkin);
				SessionManager.setAttribute(request, "evalThema", defaultEval_thema);
				SessionManager.setAttribute(request, "recsee_mobile", RECSEE_MOBILE);
				SessionManager.setAttribute(request, "clientIpChk", clientIpChk);
				if("Y".equals(clientIpChk)) {
					SessionManager.setAttribute(request, "ipChkAuth", ipChkAuth);
					SessionManager.setAttribute(request, "ipChkDup", ipChkDup);
				}
				if(!"".equals(listen_period) && listen_period_excpet.indexOf(userInfo.getUserLevel())<0) {
					SessionManager.setAttribute(request, "listen_period", listen_period);
				}
				SessionManager.setAttribute(request, "downloadFormat", downloadFormat);
				SessionManager.setAttribute(request, "STTPlayer", STTPlayerYN);
				SessionManager.setAttribute(request, "tabMode", tabMode);
				SessionManager.setAttribute(request, "http", HTTP);
				SessionManager.setAttribute(request, "separation_speaker", separation_speaker);
				SessionManager.setAttribute(request, "custEncryptDate", custEncryptDate);
				SessionManager.setAttribute(request, "requestReason", requestReason);
				SessionManager.setAttribute(request, "updateGroupinfoYn", updateGroupinfoYn);
				SessionManager.setAttribute(request, "updateGroupinfoPeriod", updateGroupinfoPeriod);
				SessionManager.setAttribute(request, "telnoUse", telnoUse);
				SessionManager.setAttribute(request, "ssoPath", "/monitoring/realtime");
				
				
				
				
				TemplateKeyInfo templateKeyInfo = new TemplateKeyInfo();
				templateKeyInfo.setTemplateKey(systemTemplates);
	
				List<TemplateKeyInfo> templateKeyResult = etcConfigInfoService.selectTemplateKeyInfo(templateKeyInfo);
				for (int i = 0; i < templateKeyResult.size(); i++) {
					SessionManager.setAttribute(request, templateKeyResult.get(i).getColorKey(), templateKeyResult.get(i).getColorValue());
				}
	
				MMenuAccessInfo menuItem = menuAccessList.get(0);
	
				String mainPath = menuItem.getProgramPath();
	
				// 세션 중복 체크용
				HttpSession session = request.getSession();
				if(!"Y".equals(dupLoginId)) {
					loginManager.setSession(session, userInfo.getUserId());
				}
				if(!"Y".equals(dupLoginIp)) {
					loginManager.setSession(session, RecSeeUtil.getClientIp(request));
				}
				
				
				if(session.getAttribute("_RSA_WEB_Key_")==null) {
					//암호화 관련
					KeyPairGenerator generator;
					try {
						generator = KeyPairGenerator.getInstance("RSA");
						generator.initialize(2048);
						KeyPair keyPair = generator.genKeyPair();
						KeyFactory keyFactory = KeyFactory.getInstance("RSA");
						PublicKey publicKey = keyPair.getPublic();
						PrivateKey privateKey = keyPair.getPrivate();
						session.setAttribute("_RSA_WEB_Key_", privateKey);
						RSAPublicKeySpec publicSpec;
						try {
							publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
							String publicKeyModulus = publicSpec.getModulus().toString(16);
							String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
							session.setAttribute("RSAModulus", publicKeyModulus);     //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
							session.setAttribute("RSAExponent", publicKeyExponent);   //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
						} catch (InvalidKeySpecException e) {
							logger.info(e.toString());
						}
					} catch (NoSuchAlgorithmException e) {
						logger.info(e.toString());
					}
				}
				
				jRes.setResult("1");
				jRes.addAttribute("mainPath", mainPath);
				userInfo.setFailReason(messageSource.getMessage("log.etc.loginSuccess", null,Locale.getDefault()));
				if(preSessionId) {
					jRes.setResult("preSessionId");
					userInfo.setFailReason(messageSource.getMessage("log.etc.dupID", null,Locale.getDefault()));
				}else if(preSessionIp) {
					jRes.setResult("preSessionIp");
					userInfo.setFailReason(messageSource.getMessage("log.etc.dupIP", null,Locale.getDefault()));
				}
				try {
					MAccessLevelInfo forLog = new MAccessLevelInfo();
					forLog.setLevelCode(userInfo.getUserLevel());
					List<MAccessLevelInfo> foLogResult = accessLevelInfoService.selectAccessLevelInfo(forLog);
					userInfo.setUserLevelName(foLogResult.get(0).getLevelName());
				} catch (Exception e) {
					logger.error("error",e);
				}
				logService.writeLog(request, "CONNECT", "SUCCESS", userInfo.toString(messageSource));
				
			} else {
				jRes = login_Fail(request, userInfo, jRes, "noAuthy", messageSource.getMessage("log.etc.noAuthy", null,Locale.getDefault()));
			}
			loginService.updateLastLoginDate(userInfo);
		}else {
			if(!loginId) {
				jRes = login_Fail(request, userInfo, jRes, "dupId", messageSource.getMessage("log.etc.dupID", null,Locale.getDefault()));
			}else {
				jRes = login_Fail(request, userInfo, jRes, "dupIp", messageSource.getMessage("log.etc.dupIP", null,Locale.getDefault()));
			}
		}
		return jRes;
	}
	
	// 로그인 실패
	public AJaxResVO login_Fail(HttpServletRequest request, LoginVO userInfo, AJaxResVO jRes, String result, String failReason) {
		jRes.setResult(result);
		jRes.setSuccess(AJaxResVO.SUCCESS_N);
		userInfo.setFailReason(failReason);
		try {
			MAccessLevelInfo forLog = new MAccessLevelInfo();
			forLog.setLevelCode(userInfo.getUserLevel());
			List<MAccessLevelInfo> foLogResult = accessLevelInfoService.selectAccessLevelInfo(forLog);
			userInfo.setUserLevelName(foLogResult.get(0).getLevelName());
		} catch (Exception e) {
			logger.error("error",e);
		}
		logService.writeLog(request, "CONNECT", "FAIL", userInfo.toString(messageSource));
		return jRes;
	}
}
