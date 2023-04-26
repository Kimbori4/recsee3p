package com.furence.recsee.api;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.admin.service.PasswordPolicyService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.TemplateKeyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MAccessLevelInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.EncryptUtil;
import com.furence.recsee.common.util.KeyDefine;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.login.controller.LoginController;
import com.furence.recsee.login.controller.LoginManager;
import com.furence.recsee.login.service.LoginService;
import com.furence.recsee.myfolder.model.MyFolderInfo;
import com.furence.recsee.myfolder.service.MyFolderService;
import com.furence.recsee.search.controller.SearchController;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/login")
public class ApiLoginController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ApiLoginController.class);
	@Autowired
	private RUserInfoService ruserInfoService;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private PasswordPolicyService rPasswordPolicyService;
	
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
	
	@Autowired
	private LogInfoService logInfoService;
	
	@Autowired
	MyFolderService myFolderService;

	@Autowired
	CustomizeInfoService customizeInfoService;
	
	LoginManager loginManager = LoginManager.getInstance();
	
	@RequestMapping(value = "/ssoLogin")
	@ResponseBody
	public void ssoLogin(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model) {
			
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("LOGIN");
		etcConfigInfo.setConfigKey("DIRECT_LOGIN");
		List<EtcConfigInfo> ssoLoingUse = null;
		
		try {
			 ssoLoingUse = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if("N".equals(ssoLoingUse.get(0).getConfigValue())) {
				return;
			}		
			
		}catch (Exception e) {
			return;
		}
		
		
		if(StringUtil.isNull(request.getParameter("userid"),true)) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.println("<script>alert('userid를 입력해 주십시오'); self.close()</script>");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
			if(writer != null)
				writer.flush(); 
			return;
		}
		
		String userIdtemp = request.getParameter("userid");
		
		if("admin".equals(userIdtemp)) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.println("<script>alert('슈퍼관리자는 로그인 할 수 없습니다.'); self.close()</script>");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
			if(writer != null)
				writer.flush(); 
			return;
		}
		
				
		RUserInfo rUserInfo = new RUserInfo();
		
		rUserInfo.setUserId(userIdtemp);
		List<RUserInfo> rUserList = ruserInfoService.selectTreeViewRUserInfo(rUserInfo);
				
		if(rUserList.size() == 0) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.println("<script>alert('사번을 확인해 주십시오.'); self.close()</script>");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
			if(writer != null)
				writer.flush(); 
			return;
		}
		
		
		String loginUserId = rUserList.get(0).getUserId();
		
		AJaxResVO jRes = new AJaxResVO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {

			LoginVO bean = new LoginVO();

			String userId = loginUserId;
			String unqLang = "ko";//request.getParameter("unqLang");

			HttpSession session = request.getSession();
			
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
			
			PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");

			if (privateKey == null ){
				jRes.setResult("0");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg","cryte module load fail");
			} else {

				EncryptUtil encryptUtil = new EncryptUtil();
				String _userId = loginUserId;//encryptUtil.decryptRsa(privateKey, userId);
				//String _userPw = encryptUtil.decryptRsa(privateKey, userPw);
				if (!StringUtil.isNull(_userId, true)){
					bean.setUserId(_userId);
					
					String salt = EncryptUtil.salt; // fcpass604! 임의 값
					//bean.setUserPass(EncryptUtil.encryptSHA512(_userPw,salt));
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
					
					etcConfigInfo = new EtcConfigInfo();
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
								return;
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
										return;
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
												return;
	
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
									return;
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
					
					return;
				}
			}
		} catch (NullPointerException e) {
			jRes = login_Fail(request, new LoginVO(), jRes, null, e.toString());
			logger.error("error",e);
		}catch (Exception e) {
			jRes = login_Fail(request, new LoginVO(), jRes, null, e.toString());
			logger.error("error",e);
		}
		
		
		
		String viewName = request.getContextPath()+"/main";		
		
		try {
			response.sendRedirect(viewName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("error",e);
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
				SessionManager.setAttribute(request, "ssoPath", "/recsee3p/search/search");
				
				
				
				
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
			e.printStackTrace();
		}
		logService.writeLog(request, "CONNECT", "FAIL", userInfo.toString(messageSource));
		return jRes;
	}
	

	private class ModifiableHttpServletRequest extends HttpServletRequestWrapper {

	    private HashMap<String, Object> params;

	    @SuppressWarnings("unchecked")
	    public ModifiableHttpServletRequest(HttpServletRequest request) {
	        super(request);
	        this.params = new HashMap<String, Object>(request.getParameterMap());
	    }

	    public String getParameter(String name) {
	        String returnValue = null;
	        String[] paramArray = getParameterValues(name);
	        if (paramArray != null && paramArray.length > 0) {
	            returnValue = paramArray[0];
	        }
	        return returnValue;
	    }

	    @SuppressWarnings("unchecked")
	    public Map getParameterMap() {
	        return Collections.unmodifiableMap(params);
	    }

	    @SuppressWarnings("unchecked")
	    public Enumeration getParameterNames() {
	        return Collections.enumeration(params.keySet());
	    }

	    public String[] getParameterValues(String name) {
	        String[] result = null;
	        String[] temp = (String[]) params.get(name);
	        if (temp != null) {
	            result = new String[temp.length];
	            System.arraycopy(temp, 0, result, 0, temp.length);
	        }
	        return result;
	    }

	    public void setParameter(String name, String value) {
	        String[] oneParam = { value };
	        setParameter(name, oneParam);
	    }

	    public void setParameter(String name, String[] value) {
	        params.put(name, value);
	    }
	}	
}//
