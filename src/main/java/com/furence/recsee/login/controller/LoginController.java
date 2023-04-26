package com.furence.recsee.login.controller;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.admin.model.PwHistory;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.PasswordPolicyService;
import com.furence.recsee.admin.service.PwHistoryService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.SSOEncryptUtil;
import com.furence.recsee.common.model.TemplateKeyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MAccessLevelInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.DateUtil;
import com.furence.recsee.common.util.EncryptUtil;
//import com.furence.recsee.common.service.OraGetDbService;
import com.furence.recsee.common.util.KeyDefine;
import com.furence.recsee.common.util.PwCheck;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.login.service.LoginService;
import com.initech.safedb.core.json.JSONObject;

@Controller
public class LoginController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private LoginService loginService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	//@Autowired
	//private OraGetDbService oraGetDbService;

	@Autowired
	private PwHistoryService pwHistoryService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private PasswordPolicyService rPasswordPolicyService;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private LogService logService;
	
	@Autowired 
	private ServletContext servletContext;
	
	@Autowired 
	private MAccessLevelInfoService accessLevelInfoService;
	LoginManager loginManager = LoginManager.getInstance();

	/**
	 * 로그인 화면 초기표시
	 * /login/login.jsp 로 이동한다.
	 */
	@RequestMapping(value = "/login/init")
	public String init(HttpServletRequest request, Locale locale) {
		String url = "/login/init.do";
		return "redirect:" + url;
	}

	/**
	 * 로그인 화면 초기표시
	 * /login/login.jsp 로 이동한다.
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/init.do")
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response, Locale locale, Model model) throws Exception {
		JSONObject json = new JSONObject();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for( String key : parameterMap.keySet()) {
			json.put(key, parameterMap.get(key)[0]);
		};
		logger.info("===========================================================================================");
		logger.info("login params [" + json + "]");
		logger.info("===========================================================================================");
		
		HttpSession session = request.getSession();
		
		//SSOLOGIN 사용 여부
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("LOGIN");
		etcConfigInfo.setConfigKey("SSO_LOGIN");
		List<EtcConfigInfo> ssoCheckConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		String secret = "";
		secret = (!StringUtil.isNull(request.getParameter("secret")))?request.getParameter("secret") : "false" ;

		//sso로그인 
		if(ssoCheckConfigInfoResult.size()>0 && !secret.equals("true")) {
			if(ssoCheckConfigInfoResult.get(0).getConfigValue().equals("Y")) {
				String keyValue = "!aiprecsys.woori";              // 암호화 Key정보(16자리)
				
				String encryptValue = request.getParameter("FINDATA"); // 암호화된 사용자 정보
				if (encryptValue == null || "".equals(encryptValue)) {
					etcConfigInfo.setConfigKey("SSO_PATH");
					ssoCheckConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					String sso_path = "http://eportal.woorifg.com/eplogin/login"; // 포탈 로그인 페이지
					// http://eportal.woorifg.com/eplogin/login?contextType=external&username=string&password=secure_string&challenge_url=http%3A%2F%2Feportal.woorifg.com%2Feplogin%2Flogin&ChallengeRedirectMethod=GET&request_id=-6859257582058364393&authn_try_count=0&locale=ko&resource_url=http%253A%252F%252Feportal.woorifg.com%252Feplogin%252FauthenticSSO%253Fsite_code%253Dnewrecord
					if(ssoCheckConfigInfoResult != null && ssoCheckConfigInfoResult.size() > 0) {
						sso_path = ssoCheckConfigInfoResult.get(0).getConfigValue();
					}
					response.sendRedirect(sso_path);
				} else {
					// encryptValue = 286c3290fb4302d64cacd382f3ee3b3e11df25d0423ea7af61dec1f2af2a2523ca62c12d77916fd4035b126f7105c684472881757559d6b67893015bdd51b338f92009e538e15131535ca6e1be9e601ac97559d89e43622d6a86a40b0705dab8b1c5a30a762dfaeef193b0d1e46f3296
					String decStrAES = SSOEncryptUtil.decryptAES(encryptValue, keyValue); // 암호화된 사용자정보 복호화 처리  keyValue
					JSONObject jsonObj = new JSONObject(decStrAES);

					String userID = jsonObj.get("USER_ID").toString();
					String ENT_CODE = jsonObj.get("ENT_CODE").toString(); // 회사코드
					String UNIT_CODE = jsonObj.get("UNIT_CODE").toString(); // 부서 코드
					String REAL_UNIT_CODE = jsonObj.get("REAL_UNIT_CODE").toString(); // 점코드
					String sso = request.getParameter("sso") != null ? request.getParameter("sso") : "";
					
					// 복호화한 SSOID 를 업무 Session에 기록한다.
					session.setAttribute("SSO_ID", userID);
					session.setAttribute("ENT_CODE", ENT_CODE);
					session.setAttribute("UNIT_CODE", UNIT_CODE);
					session.setAttribute("REAL_UNIT_CODE", REAL_UNIT_CODE);
					session.setAttribute("sso", sso);
					response.sendRedirect(request.getContextPath()+"/ssoLoginCheck.do");
				}
			}
		}
		
		if(session.getAttribute("_RSA_WEB_Key_")==null || session.getAttribute("RSAModulus")==null || session.getAttribute("RSAExponent")==null) {
			
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair keyPair = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			String publicKeyModulus = publicSpec.getModulus().toString(16);
			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
			
			session.setAttribute("_RSA_WEB_Key_", privateKey);
			session.setAttribute("RSAModulus", publicKeyModulus);
			session.setAttribute("RSAExponent", publicKeyExponent);
		}		
		

		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("TEMPLATES");
		List<EtcConfigInfo> systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		String systemTemplates = "rs";
		if (systemConfigInfoResult.get(0).getConfigValue() != null) {
			systemTemplates = systemConfigInfoResult.get(0).getConfigValue().toLowerCase();
		}

		TemplateKeyInfo templateKeyInfo = new TemplateKeyInfo();
		templateKeyInfo.setTemplateKey(systemTemplates);

		List<TemplateKeyInfo> templateKeyResult = etcConfigInfoService.selectTemplateKeyInfo(templateKeyInfo);

		for (int i = 0; i < templateKeyResult.size(); i++) {
			request.setAttribute(templateKeyResult.get(i).getColorKey(), templateKeyResult.get(i).getColorValue());

		}
		request.setAttribute("systemTemplates", systemTemplates);
		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("DEFAULT_SKIN");
		systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		String defaultSkin = "web";
		if (systemConfigInfoResult.get(0).getConfigValue() != null) {
			defaultSkin = systemConfigInfoResult.get(0).getConfigValue().toLowerCase();
		}
		request.setAttribute("defaultSkin", defaultSkin);
		
		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("Evaluation");
		etcConfigInfo.setConfigKey("Eval_Thema");
		systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		String defaultEval_thema = "recsee";
		try {
			if(systemConfigInfoResult.size()>0) {
				if (systemConfigInfoResult.get(0).getConfigValue() != null) {
					defaultEval_thema = systemConfigInfoResult.get(0).getConfigValue().toLowerCase();
				}
			}			
		}catch(Exception e) {
			logger.error("error",e);
			throw new Exception();
		}
		
		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("RECSEE_MOBILE");
		systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		String RECSEE_MOBILE = "N";
		try {
			if(systemConfigInfoResult.size()>0) {
				if (systemConfigInfoResult.get(0).getConfigValue() != null) {
					RECSEE_MOBILE = systemConfigInfoResult.get(0).getConfigValue().toUpperCase();
				}
			}			
		}catch(Exception e) {
			logger.error("error",e);
			throw new Exception();
		}
		
		// version 정보 ini 파일에서 읽기
		String iniFilePath = servletContext.getRealPath("/")+"WEB-INF/config/"; //config 풀경로 받아오기
		File iniFile = new File(iniFilePath+"version.ini"); //version.ini 파일 읽기
		Ini ini = new Ini(iniFile);
		String RecseeVersion = ini.get("RECSEE", "VERSION");
		
		
		request.setAttribute("recseeVersion", RecseeVersion); //Recsee 버전 받기
		request.setAttribute("evalThema", defaultEval_thema);
		request.setAttribute("recsee_mobile", RECSEE_MOBILE);

		ModelAndView result = new ModelAndView();

		String loginPath = "/login/login";

		result.setViewName(loginPath);
		return result;
	}

	/**
	 * 로그인 체크
	 * /common/login/login.jsp login_chk() 함수에서
	 * ajax를 이용하여 받아온 값으로 DB를 조회하여
	 * 데이터가 있으면 Session에 로그인 계정 정보를 저장 후 1을 반환하고
	 * 데이터가 없으면 0을 반환한다.
	 *
	 * user_id : 입력 받은 계정
	 * user_pw : 입력 받은 비밀번호
	 *
	 */
	@RequestMapping(value = "/login/loginChk.do", method = RequestMethod.POST)
	public @ResponseBody AJaxResVO loginChk(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, ParseException {
		AJaxResVO jRes = new AJaxResVO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {

			LoginVO bean = new LoginVO();

			String userId = request.getParameter("userId");
			String userPw = request.getParameter("userPw");
			String unqLang = request.getParameter("unqLang");

			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");
			
			if (privateKey == null ){
				jRes.setResult("0");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg","cryte module load fail");
			} else {

				EncryptUtil encryptUtil = new EncryptUtil();
				String _userId = encryptUtil.decryptRsa(privateKey, userId);
				String _userPw = encryptUtil.decryptRsa(privateKey, userPw);
				if (!StringUtil.isNull(_userId, true)){
					bean.setUserId(_userId);
					
					String salt = EncryptUtil.salt; // fcpass604! 임의 값
					bean.setUserPass(EncryptUtil.encryptSHA512(_userPw,salt));
					//bean.setUserPass(_userPw);
					Locale setLocale = new Locale(unqLang);
					LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
					if ( unqLang.equals("ko") ) {
						localeResolver.setLocale(request,response,setLocale);
						java.util.Locale.setDefault(setLocale);
					} else {
						localeResolver.setLocale(request,response,setLocale);
						java.util.Locale.setDefault(setLocale);
					}
	
					//로그인시 ip 체크 여부
					String ipchek="N";
					String ipChkAuth = "E1001"; //예외 권환
					String ipChkDup = "N"; //중복 허용 
					String clientIp="";
					
					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("LOGIN");
					etcConfigInfo.setConfigKey("LOGIN_IP_CHK");
					List<EtcConfigInfo> ipCheckConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					
					if(ipCheckConfigInfoResult.size()==1&&!StringUtil.isNull(ipCheckConfigInfoResult.get(0).getConfigValue(), true)) {
						clientIp = RecSeeUtil.getClientIp(request);
						ipchek=ipCheckConfigInfoResult.get(0).getConfigValue();
					}
					
					
					if("Y".equals(ipchek)) {
						etcConfigInfo.setGroupKey("LOGIN");
						etcConfigInfo.setConfigKey("LOGIN_IP_CHK_AUTH");
						List<EtcConfigInfo> ipCheckAuthConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
						if(ipCheckAuthConfigInfoResult.size()==1&&ipCheckAuthConfigInfoResult.get(0).getConfigValue() != null) {
							ipChkAuth=ipCheckAuthConfigInfoResult.get(0).getConfigValue();
						}
						
						etcConfigInfo.setGroupKey("LOGIN");
						etcConfigInfo.setConfigKey("LOGIN_IP_CHK_DUP");
						List<EtcConfigInfo> ipCheckDupConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
						if(ipCheckDupConfigInfoResult.size()==1&&!StringUtil.isNull(ipCheckDupConfigInfoResult.get(0).getConfigValue(), true)) {
							ipChkDup=ipCheckDupConfigInfoResult.get(0).getConfigValue();
						}
						
					}
					
					
					LoginVO beenChk = new LoginVO();
					beenChk.setUserId(_userId);
					
					
					LoginVO userInfoChk= loginService.selectUserInfo(beenChk);
					
					if(userInfoChk == null) {
						userInfoChk=loginService.selectAuserInfo(beenChk);
					}
					
					// 로그인 체크
					LoginVO userInfo=null;
										
					userInfo= loginService.selectUserInfo(bean);
					if(userInfo == null) {
						userInfo=loginService.selectAuserInfo(bean);
					}

					//특정포트의 경우 특정권한은 접속불가
					etcConfigInfo.setGroupKey("LOGIN");
					etcConfigInfo.setConfigKey("LOGIN_NO_ENTRY_PORT");
					List<EtcConfigInfo> noEntryPortConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					
					if(noEntryPortConfigInfoResult.size() > 0) {
						String localPort = String.valueOf(request.getLocalPort());
						if(noEntryPortConfigInfoResult.get(0).getConfigValue().contains(localPort)) {
							etcConfigInfo.setGroupKey("LOGIN");
							etcConfigInfo.setConfigKey("LOGIN_NO_ENTRY_AUTH");
							List<EtcConfigInfo> noEntryAuthConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
							
							if(noEntryAuthConfigInfoResult.size() > 0) {
								if(noEntryAuthConfigInfoResult.get(0).getConfigValue().contains(userInfo.getUserLevel())) {
									jRes = login_Fail(request, userInfoChk, jRes, "0", messageSource.getMessage("log.etc.noEntryAuth", null,Locale.getDefault()));
									jRes.setResult("no entry auth");
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									return jRes;
								}
							
							}
							
						}
					}
					
					
					// 쿠키에 언어 저장
					Cookie cookie = new Cookie("unqLang", unqLang.toString());
					cookie.setMaxAge(86400*30);
					cookie.setPath("/");
					response.addCookie(cookie);
	
					//비밀번호 변경 정책 사용 체크
					PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();
					List<PasswordPolicyInfo> list = rPasswordPolicyService.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
					
					//평가 테마 설정
					etcConfigInfo.setGroupKey("Evaluation");
					etcConfigInfo.setConfigKey("Eval_Thema");
					List<EtcConfigInfo> masterYNigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					String master="";
					if(masterYNigInfoResult.size()>0)
						master = masterYNigInfoResult.get(0).getConfigValue();
					
					//비밀번호 정책 제외 권한인지 확인
					// 비밀번호 불일치로 인한 잠금 미적용 권한
					etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("PASSWORD");
					etcConfigInfo.setConfigKey("TRY_LIMITLESS_AUTHY");
					List<EtcConfigInfo> limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					if(limitlessAuthy.isEmpty()) {
						etcConfigInfo.setConfigValue("E1001");
						etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
						etcConfigInfo.setConfigValue(null);
						limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					}				
					String [] tryLimitlessResult = null;
					Boolean tryLimitless = true;
					if(!StringUtil.isNull(limitlessAuthy.get(0).getConfigValue(),true)) {
						tryLimitlessResult = limitlessAuthy.get(0).getConfigValue().split(",");
						for(int i=0; i<tryLimitlessResult.length; i++) {
							if(userInfoChk.getUserLevel().equals(tryLimitlessResult[i])) {// 이거 널포인트 떳는데
								tryLimitless=false;
								break;
							}			
						}
					}
					
					// 장기 미접속 계정 잠금 미적용 권한
					etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("PASSWORD");
					etcConfigInfo.setConfigKey("PERIOD_LIMITLESS_AUTHY");
					limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					if(limitlessAuthy.isEmpty()) {
						etcConfigInfo.setConfigValue("E1001");
						etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
						etcConfigInfo.setConfigValue(null);
						limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					}				
					String [] periodLimitlessResult = null;
					Boolean periodLimitless = true;
					if(!StringUtil.isNull(limitlessAuthy.get(0).getConfigValue(),true)) {
						periodLimitlessResult = limitlessAuthy.get(0).getConfigValue().split(",");
						for(int i=0; i<periodLimitlessResult.length; i++) {
							if(userInfoChk.getUserLevel().equals(periodLimitlessResult[i])) {
								periodLimitless=false;
								break;
							}			
						}
					}
					
					// 비밀번호 변경주기 미적용 권한
					etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("PASSWORD");
					etcConfigInfo.setConfigKey("CYCLE_LIMITLESS_AUTHY");
					limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					if(limitlessAuthy.isEmpty()) {
						etcConfigInfo.setConfigValue("E1001");
						etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
						etcConfigInfo.setConfigValue(null);
						limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					}				
					String [] cycleLimitlessResult = null;
					Boolean cycleLimitless = true;
					if(!StringUtil.isNull(limitlessAuthy.get(0).getConfigValue(),true)) {
						cycleLimitlessResult = limitlessAuthy.get(0).getConfigValue().split(",");
						for(int i=0; i<cycleLimitlessResult.length; i++) {
							if(userInfoChk.getUserLevel().equals(cycleLimitlessResult[i])) {
								cycleLimitless=false;
								break;
							}			
						}
					}
					
					// 등록 후 최초 로그인 시 비밀번호 강제 변경 여부
					etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("PASSWORD");
					etcConfigInfo.setConfigKey("CHANGE_FIRST_LOGIN");
					List<EtcConfigInfo> changeFirstLoginList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					if(changeFirstLoginList.isEmpty()) {
						etcConfigInfo.setConfigValue("N");
						etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
						etcConfigInfo.setConfigValue(null);
						changeFirstLoginList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					}				
					String changeFirstLoginYn = changeFirstLoginList.get(0).getConfigValue();
	
					
					// 아이디는 있음
					if(userInfoChk != null) {
						
						// 로그인 실패 시(비번틀림)
						if(userInfo == null) {
							
							if (list != null && list.size() > 0 && "Y".equals(list.get(0).getrPolicyUse())){	// 비번 변경 정책 사용 시 로직
								if("Y".equals(list.get(0).getrTryUse())&& tryLimitless){
									// 시도횟수 증가
									loginService.updateTryCount(bean);
									if("Y".equals(userInfoChk.getLockYn())) {
										jRes = login_Fail(request, userInfoChk, jRes, "ID is Locked", messageSource.getMessage("log.etc.lockID", null,Locale.getDefault()));
									}else {
										// 로그인 여러번 시도 실패시 계정 잠궈버림
										rPasswordPolicyInfo = list.get(0);
										String tryCountStr = rPasswordPolicyInfo.getrTryCount();
										// 시도 횟수가 널이면  0으로 처리되게
										Integer tryCount = Integer.parseInt(StringUtil.isNull(tryCountStr,true) ? "0" : tryCountStr );
										// ShortRange에 비번 정책에서 사용 할 로그인 시도 횟수를 보냄.
										// 쿼리에서 ShortRange에 값이 있을경우, 현재 시도 횟수랑 비교해서 현재 시도횟수가 더 많을경우 잠금 처리 함.
										bean.setShortRange(String.valueOf(tryCount));
										bean.setLockYn("Y");
										// 잠금 설정
										loginService.updateUserLock(bean);
										jRes = login_Fail(request, userInfoChk, jRes, "0", messageSource.getMessage("log.etc.noEqualPw", null,Locale.getDefault()));
									}
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									
								}else {
									jRes = login_Fail(request, userInfoChk, jRes, "0", messageSource.getMessage("log.etc.noEqualPw", null,Locale.getDefault()));
								}
							} else {
								jRes = login_Fail(request, userInfoChk, jRes, "0", messageSource.getMessage("log.etc.noEqualPw", null,Locale.getDefault()));
							}
						
						} else {// 아이디 비밀번호 일치
															
							
							SessionManager.setAttribute(request, "unqLang", unqLang);//asd
							
							//첫 로그인 시 비번 강제 변경
							if("Y".equals(changeFirstLoginYn) && "00000000".equals(userInfo.getPwEditDate())) {
								session.setAttribute("pwchangeId", _userId);
								session.setAttribute("pwchange", "F");
								jRes = login_Fail(request, userInfoChk, jRes, "PWCHANGE", messageSource.getMessage("log.etc.rePw", null,Locale.getDefault()));
								return jRes;
							}													
							
							// 비번 변경 정책 사용 시 로직
							if (list != null && list.size() > 0 && "Y".equals(list.get(0).getrPolicyUse())){
								rPasswordPolicyInfo = list.get(0);
								// 장기 미접속시 계정 잠금
								if("Y".equals(rPasswordPolicyInfo.getrLockUse()) && periodLimitless) {
									String lockType = rPasswordPolicyInfo.getrLockType();
									String lockCountStr = rPasswordPolicyInfo.getrLockCount();
									// 락 카운트가 널이면  0으로 처리되게
									Integer lockCount = Integer.parseInt(StringUtil.isNull(lockCountStr,true) ? "0" : lockCountStr );
									// 최종 로그인
									Calendar loginDate = Calendar.getInstance();
									loginDate.setTime(sdf.parse(userInfo.getLastLoginDate()));
									loginDate.add(Calendar.DATE, 1);
									// 최종 로그인 날짜 + 잠금 설정 날짜 = 잠금이 시작되는 날짜
									switch(lockType) {
									case "Y":
										loginDate.add(Calendar.YEAR, lockCount);
										break;
									case "M":
										loginDate.add(Calendar.MONTH, lockCount);
										break;
									case "W":
										loginDate.add(Calendar.WEDNESDAY, lockCount);
										break;
									case "D":
										loginDate.add(Calendar.DATE, lockCount);
										break;
									}
									// 오늘 날짜
									Calendar today = Calendar.getInstance();
	
									// 오늘 날짜가 리밋 날짜보다 클 경우 계정 잠금 처리 => 잠금시점이 지났을 경우
									if(today.after(loginDate)) {
										userInfo.setLockYn("Y");
										loginService.updateUserLock(userInfo);
									}
								}
								
								if("Y".equals(list.get(0).getrTryUse())&& tryLimitless){
									
									// 로그인 여러번 시도 실패시 계정 잠궈버림
									rPasswordPolicyInfo = list.get(0);
									String tryCountStr = rPasswordPolicyInfo.getrTryCount();
									// 시도 횟수가 널이면  0으로 처리되게
									Integer tryCount = Integer.parseInt(StringUtil.isNull(tryCountStr,true) ? "0" : tryCountStr );
	
									// ShortRange에 비번 정책에서 사용 할 로그인 시도 횟수를 보냄.
									// 쿼리에서 ShortRange에 값이 있을경우, 현재 시도 횟수랑 비교해서 현재 시도횟수가 더 많을경우 잠금 처리 함.
									bean.setShortRange(String.valueOf(tryCount));
									bean.setLockYn("Y");
									// 잠금 설정
									loginService.updateUserLock(bean);
									if(Integer.parseInt(userInfo.getTryCount())>=tryCount) {
										userInfo.setLockYn("Y");
									}
								}
								
								
								// 계정이 잠겼을 경우
								if("Y".equals(userInfo.getLockYn())) {
									jRes = login_Fail(request, userInfoChk, jRes, "ID is Locked", messageSource.getMessage("log.etc.lockID", null,Locale.getDefault()));
								}else{
									
									//접속 ip 체크
									if("Y".equals(ipchek) //사용 유무
										&&ipChkAuth.indexOf(userInfoChk.getUserLevel())<0 //예외 권한 조직인지
										&&!clientIp.equals(userInfoChk.getClientIp())/*아이피 일치하는지*/) {
										if("Y".equals(list.get(0).getrTryUse())&& tryLimitless){
											
											// 로그인 여러번 시도 실패시 계정 잠궈버림
											rPasswordPolicyInfo = list.get(0);
											String tryCountStr = rPasswordPolicyInfo.getrTryCount();
											// 시도 횟수가 널이면  0으로 처리되게
											Integer tryCount = Integer.parseInt(StringUtil.isNull(tryCountStr,true) ? "0" : tryCountStr );
	
											// 시도횟수 증가
											loginService.updateTryCount(bean);
	
											// ShortRange에 비번 정책에서 사용 할 로그인 시도 횟수를 보냄.
											// 쿼리에서 ShortRange에 값이 있을경우, 현재 시도 횟수랑 비교해서 현재 시도횟수가 더 많을경우 잠금 처리 함.
											bean.setShortRange(String.valueOf(tryCount));
											bean.setLockYn("Y");
											// 잠금 설정
											loginService.updateUserLock(bean);
											
										}
										
										jRes = login_Fail(request, userInfoChk, jRes, "no match ip", messageSource.getMessage("log.etc.noMatchIP", null,Locale.getDefault()));
										return jRes;
									}
									
									// 비밀번호 변경 주기 사용 시
									if(!rPasswordPolicyInfo.getrCycleUse().equals("N") && cycleLimitless){
	
										if(!StringUtil.isNull(userInfo.getPwEditDate(),true)){
	
											String cycleType = rPasswordPolicyInfo.getrCycleType();
											String cycleCountStr = rPasswordPolicyInfo.getrCycle();
											// 락 카운트가 널이면  0으로 처리되게
											Integer cycleCount = Integer.parseInt(StringUtil.isNull(cycleCountStr,true) ? "0" : cycleCountStr );
											// 최종 비밀번호 변경일
											Calendar pwEditDate = Calendar.getInstance();
	
											// 최종 로그인 날짜 + 비밀번호 변경 설정 날짜 = 비밀번호 변경 권장이 시작되는 날짜
											switch(cycleType) {
											case "Y":
												pwEditDate.setTime(sdf.parse(userInfo.getPwEditDate().substring(0,8)));
												pwEditDate.add(Calendar.YEAR, cycleCount);
												break;
											case "M":
												pwEditDate.setTime(sdf.parse(userInfo.getPwEditDate().substring(0,8)));
												pwEditDate.add(Calendar.MONTH, cycleCount);
												break;
											case "W":
												pwEditDate.setTime(sdf.parse(userInfo.getPwEditDate()));
												pwEditDate.add(Calendar.WEDNESDAY, cycleCount);
												break;
											case "D":
												pwEditDate.setTime(sdf.parse(userInfo.getPwEditDate()));
												pwEditDate.add(Calendar.DATE, cycleCount);
												break;
											}
											// 오늘 날짜
											Calendar today = Calendar.getInstance();
	
											// 오늘 날짜가 리밋 날짜보다 클 경우 비밀번호 변경 로직 => 비밀번호 변경 시점이 지났을 경우
											if(today.after(pwEditDate)) {
	
												if("F".equals(rPasswordPolicyInfo.getrCycleUse())) {
													session.setAttribute("pwchangeId", _userId);
													session.setAttribute("pwchange", "F");
													jRes = login_Fail(request, userInfoChk, jRes, "PWCHANGE_F", messageSource.getMessage("log.etc.expiredPw", null,Locale.getDefault()));
												}else if("Y".equals(rPasswordPolicyInfo.getrCycleUse())) {
													session.setAttribute("pwchangeId", _userId);
													session.setAttribute("pwchange", "Y");
													jRes = login_Fail(request, userInfoChk, jRes, "PWCHANGE_Y", messageSource.getMessage("log.etc.expiredPw", null,Locale.getDefault()));
												}
												return jRes;
	
											// 아니면 로그인 처리
											}else{
												jRes = login_Success(request, userInfo, jRes);
											}
										//비밀번호 변경일 값이 null인경우 로그인 처리하고 변경일을 오늘날짜로 업데이트	
										}else {
											loginService.updateLastLoginDate(userInfo);
											jRes = login_Success(request, userInfo, jRes);
										}
									// 비밀번호 변경 주기 미사용시
									}else{
										jRes = login_Success(request, userInfo, jRes);
									}
								}
							// 정책 미사용 시 로그인 진행
							}else{
								
								//접속 ip 체크
								if("Y".equals(ipchek) //사용 유무
									&&ipChkAuth.indexOf(userInfoChk.getUserLevel())<0 //예외 권한 조직인지
									&&!clientIp.equals(userInfoChk.getClientIp())/*아이피 일치하는지*/) {
									
									jRes = login_Fail(request, userInfoChk, jRes, "no match ip", messageSource.getMessage("log.etc.noMatchIP", null,Locale.getDefault()));
									return jRes;
								}
								
								jRes = login_Success(request, userInfo, jRes);
							}
						}
					// 없는 아이디로 로그인 시도
					}else {
						jRes = login_Fail(request, new LoginVO(), jRes, null, messageSource.getMessage("log.etc.nullID", null,Locale.getDefault()));
					}
				}else {
					jRes.setResult("session over");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					return jRes;
				}
			}
		} catch (NullPointerException e) {
			jRes = login_Fail(request, new LoginVO(), jRes, null, e.toString());
			logger.error("error",e);
		}catch (Exception e) {
			jRes = login_Fail(request, new LoginVO(), jRes, null, e.toString());
			logger.error("error",e);
		}
		return jRes;
	}

	@RequestMapping(value="/login/changeMessage.do")
	public @ResponseBody AJaxResVO changeLanguage(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, ParseException {
		AJaxResVO jRes = new AJaxResVO();

		if( request.getParameter("changeLang") != null ) {
			Locale procLocale = new Locale(request.getParameter("changeLang"));
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			localeResolver.setLocale(request,response,procLocale);
			java.util.Locale.setDefault(procLocale);

			Cookie cookie = new Cookie("unqLang", request.getParameter("changeLang"));
			cookie.setMaxAge(86400*30);
			cookie.setPath("/");
			response.addCookie(cookie);

		}
		
		try {
			//jRes.addAttribute("loginloginpagetitle", messageSource.getMessage("login.login.page.title", null,Locale.getDefault()));
			jRes.addAttribute("loginloginplaceholderid", messageSource.getMessage("login.login.placeholder.id", null,Locale.getDefault()));
			jRes.addAttribute("loginloginplaceholderpassword", messageSource.getMessage("login.login.placeholder.password", null,Locale.getDefault()));
			jRes.addAttribute("loginlogintitlerememberMe", messageSource.getMessage("login.login.title.rememberMe", null,Locale.getDefault()));
			/*jRes.addAttribute("loginloginalertnotinputcode", messageSource.getMessage("login.login.alert.not.input.code", null,Locale.getDefault()));
			jRes.addAttribute("loginloginalertnotinputid", messageSource.getMessage("login.login.alert.not.input.code", null,Locale.getDefault()));
			jRes.addAttribute("loginloginalertnotinputpw", messageSource.getMessage("login.login.alert.not.input.pw", null,Locale.getDefault()));
			jRes.addAttribute("loginloginalertnotinputext", messageSource.getMessage("login.login.alert.not.input.ext", null,Locale.getDefault()));
			jRes.addAttribute("loginloginalertnotinputlang", messageSource.getMessage("login.login.alert.not.input.lang", null,Locale.getDefault()));
			jRes.addAttribute("loginloginalertfaillogin", messageSource.getMessage("login.login.alert.fail.login", null,Locale.getDefault()));*/

			jRes.setResult("1");
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} catch(NullPointerException e) {
			jRes.setResult("0");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}catch(Exception e) {
			jRes.setResult("0");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	@RequestMapping(value = "/logout")
	public ModelAndView apLogout(HttpServletRequest request,HttpServletResponse response, Locale locale) throws Exception{
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		ModelAndView result = new ModelAndView();
		String Path  = "";

		//SSOLOGIN Y,N 처리
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("LOGIN");
		etcConfigInfo.setConfigKey("SSO_LOGIN");

		List<EtcConfigInfo> ssoCheckConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		if(ssoCheckConfigInfoResult.size()>0) {
			if(ssoCheckConfigInfoResult.get(0).getConfigValue().equals("Y")) {
				Path = "/login/logout";
				SessionManager.sessionClear(request);
			}else {
				Path = "/login/login";
				SessionManager.sessionClear(request);
			}
		}else {
			Path = "/login/login";
			SessionManager.sessionClear(request);
		}

		if (userInfo !=null) {
			userInfo.setFailReason(messageSource.getMessage("log.etc.logout", null,Locale.getDefault()));
			try {
				MAccessLevelInfo forLog = new MAccessLevelInfo();
				forLog.setLevelCode(userInfo.getUserLevel());
				List<MAccessLevelInfo> foLogResult = accessLevelInfoService.selectAccessLevelInfo(forLog);
				userInfo.setUserLevelName(foLogResult.get(0).getLevelName());
			} catch (Exception e) {
				logger.error("error",e);
			}
			logService.writeLog(request, "CONNECT", "LOGOUT", userInfo.toString(messageSource));

//			HttpSession session = request.getSession();
//			String userId = userInfo.getUserId();
//			session.removeAttribute(userId); 
		}

		result.setViewName(Path);

		return result;
	}

	// pwChange 페이지
	@RequestMapping(value = "/pwChange")
	public ModelAndView pwChange(HttpServletRequest request, Locale locale) throws InvalidKeySpecException, NoSuchAlgorithmException{

		ModelAndView result = new ModelAndView();
		HttpSession session = request.getSession();
		request.setAttribute("userId", session.getAttribute("pwchangeId"));
		request.setAttribute("type", session.getAttribute("pwchange"));
		
		//암호화 관련
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.genKeyPair();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		session.setAttribute("_RSA_WEB_Key_", privateKey);
		RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
		session.setAttribute("RSAModulus", publicKeyModulus);     //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		session.setAttribute("RSAExponent", publicKeyExponent);   //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		
		result.setViewName("/login/pwChange");
		return result;
	}

	// 비밀번호 정책에 의한 변경
	@RequestMapping(value = "/changePw.do")
	public @ResponseBody AJaxResVO changePw(HttpServletRequest request, Locale local, Model model) {
		Logger logger = Logger.getLogger(getClass());
		AJaxResVO jRes = new AJaxResVO();
			
		try {
			LoginVO bean = new LoginVO();
			
			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");

			if (privateKey == null ){
				jRes.setResult("0");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg","cryte module load fail");
			} else {

				EncryptUtil encryptUtil = new EncryptUtil();
				String userId = encryptUtil.decryptRsa(privateKey, request.getParameter("userId"));
				
				String userPw = null; // 변경되는 비밀번호
				String nowPw = null; // 변경 전 사용중인 비밀번호
			
				// 비번변경 뒤로 미루는 경우는 아이디만 체크해서 비번은 놔두고 비번변경날짜만 변경해줄거임
				if( request.getParameter("userPw") != null ) {
					userPw = encryptUtil.decryptRsa(privateKey, request.getParameter("userPw"));
				}
				if( request.getParameter("nowPw") != null ) {
					nowPw = encryptUtil.decryptRsa(privateKey, request.getParameter("nowPw"));
				}
			
				if(!StringUtil.isNull(userId,true)){
					bean.setUserId(userId);
				}
				//세션에 아이디만 저장
				session.setAttribute("USER_INFO", bean);
				
				// 암호화
				if(!StringUtil.isNull(nowPw,true)){
					String salt = EncryptUtil.salt; // fcpass604! 임의 값
					bean.setUserPass(EncryptUtil.encryptSHA512(nowPw,salt));
				}
				// 현재 비밀번호가 맞는지 췤췤
				LoginVO userInfo = loginService.selectUserInfo(bean);
	
				if(userInfo == null){
					// 일치하는 데이터 없을시
					jRes.setResult("NOMATCH");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				} else{
	
					// 유저 정보 변경
					RUserInfo ruserInfo = new RUserInfo();
					ruserInfo.setUserId(userId);
	
					String salt = EncryptUtil.salt; // fcpass604! 임의 값
					if(!StringUtil.isNull(userPw,true)){
						ruserInfo.setPassword(EncryptUtil.encryptSHA512(userPw,salt));
					}
					
					// 다음에 변경하기
					if(StringUtil.isNull(userPw,true)) {
						Integer updateRUserResult = ruserInfoService.updateRUserInfo(ruserInfo);
	
						if (updateRUserResult == 1) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("UPDATESUCCESS");
							jRes.addAttribute("msg", "user update success");
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.pushBackChange", null,Locale.getDefault()));
							logService.writeLog(request, "USER", "UPDATE-SUCCESS", ruserInfo.toString(messageSource));
						}
						else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("UPDATEFAIL");
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.pushBackChangeFail", null,Locale.getDefault()));
							logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
						}
						
					// 비밀번호 변경하기					
					}else {
						
						// 비밀번호 재사용 제한 미적용 권한
						EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
						etcConfigInfo.setGroupKey("PASSWORD");
						etcConfigInfo.setConfigKey("PASTPW_LIMITLESS_AUTHY");
						List<EtcConfigInfo> limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
						if(limitlessAuthy.isEmpty()) {
							etcConfigInfo.setConfigValue("E1001");
							etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
							limitlessAuthy = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
						}
						String [] pastpwLimitlessResult = null;
						Boolean pastpwLimitless = true;
						if(!StringUtil.isNull(limitlessAuthy.get(0).getConfigValue(),true)) {
							pastpwLimitlessResult = limitlessAuthy.get(0).getConfigValue().split(",");
							for(int i=0; i<pastpwLimitlessResult.length; i++) {
								if(userInfo.getUserLevel().equals(pastpwLimitlessResult[i])) {
									pastpwLimitless=false;
									break;
								}			
							}
						}
						
						String unqLang = SessionManager.getStringAttr(request, "unqLang");
						
						// 비밀번호 패턴 정규식 검사 영 대/소 숫자 특수문자 8~20자리 사이
						String checkPattern = PwCheck.pwCheck(userPw, userId,unqLang);
						// 패턴 통과 하면
						if (StringUtil.isNull(checkPattern)) {
							PwHistory pwHistory = new PwHistory();
							// 비번 변경 히스토리에 남기기
							pwHistory.setrUserId(userId);
							pwHistory.setrPassword(EncryptUtil.encryptSHA512(userPw,salt));
							//passwordPolicyService
							PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();
							List<PasswordPolicyInfo> list = rPasswordPolicyService.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
	
							// 비밀번호 정책 데이터가 있고, 사용 이면
							if(list.size() > 0 && "Y".equals(list.get(0).getrPolicyUse())) {
								// 과거 비밀번호 미허용 이고 해당 사항 제한권한이 아니면
								if(!list.get(0).getrPastPwUse().equals("Y") && pastpwLimitless) {
									int pwCnt = pwHistoryService.selectPwHistory(pwHistory);
	
									// 횟수 제한 일 경우
									if(list.get(0).getrPastPwUse().equals("C")) {
										if(Integer.parseInt(list.get(0).getrPastPwCount()) <= pwCnt) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.setResult("PW Count Over");
											ruserInfo.setLogContents(messageSource.getMessage("log.etc.cntOverPw", null,Locale.getDefault()));
											logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
											return jRes;
										}
									// 아예 사용 불가능 이면
									} else {
										if(pwCnt > 0) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.setResult("PW Dont Use");
											ruserInfo.setLogContents(messageSource.getMessage("log.etc.noPrePw", null,Locale.getDefault()));
											logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
											return jRes;
										}
									}
								}
							}
							ruserInfo.setPassword(EncryptUtil.encryptSHA512(userPw,salt));
							Integer updateRUserResult = ruserInfoService.updateRUserInfo(ruserInfo);
	
							if (updateRUserResult == 1) {
								pwHistoryService.insertPwHistory(pwHistory);
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("UPDATESUCCESS");
								jRes.addAttribute("msg", "user update success");
								ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChange", null,Locale.getDefault()));
								logService.writeLog(request, "USER", "UPDATE-SUCCESS", ruserInfo.toString(messageSource));
							}
							else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("UPDATEFAIL");
								ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChangeFail", null,Locale.getDefault()));
								logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("PASSWORD PATTERN IS MISS MATCH");
							jRes.addAttribute("msg", checkPattern);
						}
						
					}
					
				}
			}
		}catch (NullPointerException e) {
			logger.error("",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);	logger.error("error",e);
		}catch (Exception e) {
			logger.error("",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);	logger.error("error",e);
		}

		return jRes;
	}

	/**
	 * 로그인 화면 초기표시
	 * /login/ssoLogin 로 이동한다.
	 * ssoLOGIN 위해 리다이렉트
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/ssoLogin.do")
	public @ResponseBody AJaxResVO ssoLogin(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		AJaxResVO jRes = new AJaxResVO();
		// 우리에선 필요없음
		String Path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		response.sendRedirect(Path+"/ssoLoginCheck.do");

		return jRes;
	}

	
	@RequestMapping(value = "/ssoLoginCheck.do")
	public String ssoLoginCheck(RedirectAttributes redirectAttr,HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO bean = new LoginVO();
		StringBuffer mainPath = new StringBuffer();
		Boolean chk=false;
		Boolean ruser=true;
		String Path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();	//현재 URL PATH 반환
		/*
		 *
		 *  SSO Login 처리 후 메인 페이지 이동
		 *
		 */

		HttpSession session = request.getSession();
		String ssoKey = (String)session.getAttribute("SSO_ID");

		if(ssoKey != null) {
			//session.setAttribute("SSO_ID", ssoKey);			//세션 키값 저장
			session.setMaxInactiveInterval(10800);
		}

		bean.setUserId(ssoKey);

		// 로그인 체크
		LoginVO userInfo = loginService.selectUserInfo(bean);

		if(userInfo == null) {
			userInfo=loginService.selectAuserInfo(bean);
			ruser=false;
			if(userInfo == null) {
				response.sendRedirect(Path);
			}
		}


		MMenuAccessInfo menuAccess = new MMenuAccessInfo();
		if(userInfo.getUserLevel() != null)
			menuAccess.setLevelCode(userInfo.getUserLevel());
		menuAccess.setDisplayLevel(100);

		List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccess);
		int menuAccessListTotal = menuAccessList.size();

		/*
		 *
		 * DEFAULT SKIN
		 *
		 */
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("TEMPLATES");
		List<EtcConfigInfo> systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		String systemTemplates = "rs";
		if(systemConfigInfoResult.size() > 0 && systemConfigInfoResult.get(0).getConfigValue() != null) {
			systemTemplates = systemConfigInfoResult.get(0).getConfigValue();
		}

		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("DEFAULT_SKIN");
		systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		String defaultSkin = "recsee";
		if(systemConfigInfoResult.size() > 0 && systemConfigInfoResult.get(0).getConfigValue() != null) {
			defaultSkin = systemConfigInfoResult.get(0).getConfigValue();
		}
		
		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("Evaluation");
		etcConfigInfo.setConfigKey("Eval_Thema");
		systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		String defaultEval_thema = "master";
		if (systemConfigInfoResult.get(0).getConfigValue() != null) {
			defaultEval_thema = systemConfigInfoResult.get(0).getConfigValue().toLowerCase();
		}
		

		SessionManager.setAttribute(request, KeyDefine.SES_KEY_USER_INFO, userInfo);

		/*
		 *  세션에 스킨 저장
		 */
		if(menuAccessListTotal > 0) {
			SessionManager.setAttribute(request, "AccessInfo", menuAccessList);
			SessionManager.setAttribute(request, "systemTemplates", systemTemplates);
			SessionManager.setAttribute(request, "defaultSkin", defaultSkin);
			SessionManager.setAttribute(request, "evalThema", defaultEval_thema);

			TemplateKeyInfo templateKeyInfo = new TemplateKeyInfo();
			templateKeyInfo.setTemplateKey(systemTemplates);

			List<TemplateKeyInfo> templateKeyResult = etcConfigInfoService.selectTemplateKeyInfo(templateKeyInfo);
			for (int i = 0; i < templateKeyResult.size(); i++) {
				SessionManager.setAttribute(request, templateKeyResult.get(i).getColorKey(), templateKeyResult.get(i).getColorValue());
			}

			MMenuAccessInfo menuItem = menuAccessList.get(0);

			mainPath.append(menuItem.getProgramPath()).delete(0, 1);

			jRes.setResult("1");
			jRes.addAttribute("mainPath", mainPath);

			redirectAttr.addFlashAttribute("path",mainPath);
			String loginKey = ssoKey + "|"+request.getRemoteAddr();
			session.setAttribute("loginKey",loginKey);
			if(userInfo.getLastLoginDate() != null || !userInfo.getLastLoginDate().equals("")) {
				Date st=new SimpleDateFormat("yyyyMMdd").parse(userInfo.getLastLoginDate());
				Date ex=DateUtil.calcDate(st, "day", +90);
				chk=DateUtil.isBetweenDate(new Date(),st,ex);
			}else {
				chk=true;
			}

			if(false && loginManager.isUsing(loginKey)) {
				logService.writeLog(request, "CONNECT", "FAIL", "["+messageSource.getMessage("log.etc.loginDup", null,Locale.getDefault())+" ID="+ssoKey+"]");
				return "redirect:/ssoDuplication";
			}
			else if(false && !chk) {
				logService.writeLog(request, "CONNECT", "FAIL", "["+messageSource.getMessage("log.etc.day90", null,Locale.getDefault())+" ID="+ssoKey+"]");
				return "redirect:/loginFailByNotUsed";
			}
			else {
				jRes = login_Success(request, userInfo, jRes);
				logService.writeLog(request, "CONNECT", "SUCCESS", "["+messageSource.getMessage("log.etc.loginSuccess", null,Locale.getDefault())+" ID="+ssoKey+"]");
				loginManager.setSession(session, loginKey);
				if(ruser)
					loginService.updateLastLoginDate(userInfo);
				else
					loginService.updateLastLoginDateAuser(userInfo);
				return "redirect:/main";
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			//logInfoService.writeLog(request, "Login check - Login fail", null, ssoKey);
			logService.writeLog(request, "CONNECT", "FAIL", "["+messageSource.getMessage("log.etc.loginFail", null,Locale.getDefault())+" ID="+ssoKey+"]");
			return "redirect:/loginFail";
		}
	}

	// 세션 팅기기
	@RequestMapping(value = "/disconnect.do")
	public @ResponseBody AJaxResVO disconnect(HttpServletRequest request, Locale locale) {

		AJaxResVO jRes = new AJaxResVO();
		HttpSession session = request.getSession();

		if(!StringUtil.isNull(request.getParameter("userID"))) {
			loginManager.printloginUsers();
			loginManager.removeSession(request.getParameter("userID"));
		}else {
			loginManager.printloginUsers();
			loginManager.removeSession((String) session.getAttribute("userId"));
		}

		LoginVO userInfo = new LoginVO();
		userInfo.setUserId(request.getParameter("userID"));

		if("Y".equals(request.getParameter("ruser")))
			loginService.updateLastLoginDate(userInfo);
		else
			loginService.updateLastLoginDateAuser(userInfo);

		jRes.setResult("DISSCONNECT");
		jRes.setSuccess(AJaxResVO.SUCCESS_Y);

		return jRes;
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
			// 로그인 성공한 유저 정보를 세션에 저장
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
				} else if(preSessionIp) {
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
