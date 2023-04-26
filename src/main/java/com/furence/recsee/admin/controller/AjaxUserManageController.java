package com.furence.recsee.admin.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.model.CustomizeItemInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;
import com.furence.recsee.admin.model.EnWord;
import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.admin.model.PwHistory;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.admin.service.PasswordPolicyService;
import com.furence.recsee.admin.service.PwHistoryService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.SelectOptionVO;
import com.furence.recsee.common.model.SendMailInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MAccessLevelInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.EncryptUtil;
import com.furence.recsee.common.util.ExcelView;
import com.furence.recsee.common.util.FileUploadUtil;
import com.furence.recsee.common.util.KeyDefine;
import com.furence.recsee.common.util.PwCheck;
import com.furence.recsee.common.util.SendMailUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;
import com.furence.recsee.login.service.LoginService;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.search.service.SearchService;
import jxl.read.biff.BiffException;

@Controller
public class AjaxUserManageController {

	private static final Logger logger = LoggerFactory.getLogger(AjaxUserManageController.class);

	@Autowired
	private MAccessLevelInfoService accessLevelInfoService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private AllowableRangeInfoService allowableRangeInfoService;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private PasswordPolicyService rPasswordPolicyService;

	@Autowired
	private PwHistoryService pwHistoryService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private CustomizeInfoService customizeInfoService;

	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private SearchListInfoService searchListInfoService;

	// 사용자 추가
	@RequestMapping(value = "/insertUserInfo.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody AJaxResVO insertUserInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				RUserInfo ruserInfo = new RUserInfo();
				String password = null;
				String userId = null;

				String phoneNumber = null;
				Integer phoneNumberCount;
				String recsee_mobile="N";
				String usageLimit="N";
				boolean insertUserInfo=true;
				String BatchUsageStatus = null;
				
				// 계정 정보 수정 권한 체크
				String userLevel = userInfo.getUserLevel();
				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userLevel);
				accessInfo.setProgramCode("P10043");
				List<MMenuAccessInfo> userAuth = menuAccessInfoService.checkAccessInfo(accessInfo);
				String writeYn = "N";
				if(userAuth.size() > 0) {
					writeYn = userAuth.get(0).getWriteYn();
				}
				
				if(writeYn.equals("N")) {
					throw new Exception();
				}
			
				if(request.getSession().getAttribute("recsee_mobile")!=null) {
					recsee_mobile = (String)request.getSession().getAttribute("recsee_mobile");	
				}
				
				if(!StringUtil.isNull(request.getParameter("phoneNumber"), true)) {
					phoneNumber = request.getParameter("phoneNumber");					
				}
				// 신규번호 추가 가능한지 확인
				Integer mobileUsage = ruserInfoService.mobileUsage();				
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("LICENCE");
				etcConfigInfo.setConfigKey("RECSEE_MOBILE");
				List<EtcConfigInfo> etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				Integer mobileLicence = 0;
				if(etcConfigInfoResult.size() > 0 && etcConfigInfoResult.get(0).getConfigValue() != null) {
					mobileLicence = Integer.parseInt(etcConfigInfoResult.get(0).getConfigValue());
				}				
				if(mobileUsage>=mobileLicence) {
					usageLimit = "Y";
				}
				if("Y".equals(recsee_mobile) && "Y".equals(usageLimit) && !StringUtil.isNull(phoneNumber, true)){		
					phoneNumberCount = ruserInfoService.checkPhone(phoneNumber);
					// 중복번호라면 사용자 추가 가능
					if (phoneNumberCount > 0) {
						insertUserInfo = true;		
					// 신규번호라면 사용자 추가 불가능
					} else {
						insertUserInfo = false;							
					}
				}
				if(insertUserInfo) {
					HttpSession session = request.getSession();
					PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");
					EncryptUtil encryptUtil = new EncryptUtil();
					// 파라 미터 셋팅
					if (!StringUtil.isNull(request.getParameter("userId"), true)) {
						if (!StringUtil.isNull(request.getParameter("aUser"), true)) {
							ruserInfo.setUserId(request.getParameter("userId"));
						}else {						
							userId = encryptUtil.decryptRsa(privateKey, request.getParameter("userId"));
							ruserInfo.setUserId(userId);
						}
					}
					if (!StringUtil.isNull(request.getParameter("userName"), true))
						ruserInfo.setUserName(request.getParameter("userName"));
	
					if (!StringUtil.isNull(request.getParameter("password"), true)) {
						if (!StringUtil.isNull(request.getParameter("aUser"), true)) {
							password = request.getParameter("password");
						}else {	
							password = encryptUtil.decryptRsa(privateKey, request.getParameter("password"));
						}
						String salt = EncryptUtil.salt; // fcpass604! 임의 값
						ruserInfo.setPassword(EncryptUtil.encryptSHA512(password, salt));
					}
	
					if (!StringUtil.isNull(request.getParameter("extNum"), true))
						ruserInfo.setExtNo(request.getParameter("extNum"));
	
					if (!StringUtil.isNull(request.getParameter("authy"), true))
						ruserInfo.setUserLevel(request.getParameter("authy"));
	
					if (!StringUtil.isNull(request.getParameter("phoneNumber"),
							true))
						ruserInfo.setUserPhone(request.getParameter("phoneNumber"));
	
					if (!StringUtil.isNull(request.getParameter("sex"), true))
						ruserInfo.setUserSex(request.getParameter("sex"));
	
					if (!StringUtil.isNull(request.getParameter("bgCode"), true))
						ruserInfo.setBgCode(request.getParameter("bgCode"));
	
					if (!StringUtil.isNull(request.getParameter("mgCode"), true))
						ruserInfo.setMgCode(request.getParameter("mgCode"));
	
					if (!StringUtil.isNull(request.getParameter("sgCode"), true))
						ruserInfo.setSgCode(request.getParameter("sgCode"));
	
					if (!StringUtil.isNull(request.getParameter("empId"), true))
						ruserInfo.setEmpId(request.getParameter("empId"));
	
					if (!StringUtil.isNull(request.getParameter("email"), true))
						ruserInfo.setUserEmail(request.getParameter("email"));
	
					if (!StringUtil.isNull(request.getParameter("ctiId"), true))
						ruserInfo.setCtiId(request.getParameter("ctiId"));
	
					if (!StringUtil.isNull(request.getParameter("aUser"), true))
						ruserInfo.setaUser(request.getParameter("aUser"));
					
					if (!StringUtil.isNull(request.getParameter("EmploymentCategory"), true))
						ruserInfo.setrUseYn(request.getParameter("EmploymentCategory"));
					
					if (!StringUtil.isNull(request.getParameter("BatchUsageStatus"), true)) {
						if("N".equals(request.getParameter("BatchUsageStatus"))){
							ruserInfo.setSkinCode(request.getParameter("BatchUsageStatus"));
						}else {
							ruserInfo.setSkinCode(BatchUsageStatus);
						}
					}else {
						ruserInfo.setSkinCode(BatchUsageStatus);
					}
					
					// 이력에 분류명 남기려고 추가
					if (!StringUtil.isNull(request.getParameter("authyName"), true))
						ruserInfo.setUserLevelName(request.getParameter("authyName"));
					
					if (!StringUtil.isNull(request.getParameter("bgName"), true))
						ruserInfo.setBgName(request.getParameter("bgName"));
	
					if (!StringUtil.isNull(request.getParameter("mgName"), true))
						ruserInfo.setMgName(request.getParameter("mgName"));
	
					if (!StringUtil.isNull(request.getParameter("sgName"), true))
						ruserInfo.setSgName(request.getParameter("sgName"));
					
					Integer selectRUserIpResult =0;
					if ("Y".equals((String)request.getSession().getAttribute("clientIpChk")) && "N".equals((String)request.getSession().getAttribute("ipChkDup"))) {
						ruserInfo.setClientIp(request.getParameter("clientIp"));
						selectRUserIpResult = ruserInfoService.checkIP(ruserInfo);
					}
						
					
					Integer selectRUserResult = ruserInfoService.checkId(ruserInfo);
	
					
					
					if (selectRUserIpResult>0){
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "used ip");
						jRes.setResult("THIS IP IS ALREADY USED");
						ruserInfo.setLogContents(messageSource.getMessage("log.etc.IPDup", null,Locale.getDefault()));
						logService.writeLog(request, "USER", "INSERT-FAIL", ruserInfo.toString(messageSource));
					}else if (selectRUserResult > 0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "used id");
						jRes.setResult("THIS EMPLOYEE NUMBER IS ALREADY USED");
						ruserInfo.setLogContents(messageSource.getMessage("log.etc.IDDup", null,Locale.getDefault()));
						logService.writeLog(request, "USER", "INSERT-FAIL", ruserInfo.toString(messageSource));
					} else {
						Integer insertRUserResult = ruserInfoService.insertRUserInfo(ruserInfo);
						if (insertRUserResult == 1) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.addAttribute("msg", "user insert success");
	
							SystemInfo systemInfo = new SystemInfo();
							systemInfo.setChannelUpdateFlag("2");
							systemInfoService.updateSystemInfo(systemInfo);
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.regSuccess", null,Locale.getDefault()));
							logService.writeLog(request, "USER", "INSERT-SUCCESS", ruserInfo.toString(messageSource));
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "user insert fail");
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.regFail", null,Locale.getDefault()));
							logService.writeLog(request, "USER", "INSERT-FAIL", ruserInfo.toString(messageSource));
						}
					}
				} else {
					jRes.setResult("NEW NUMBER REGISTRATION IS NOT POSSIBLE");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					ruserInfo.setLogContents(messageSource.getMessage("log.etc.mobileLicenseExceed", null,Locale.getDefault()));
					logService.writeLog(request, "USER", "INSERT-FAIL", ruserInfo.toString(messageSource));
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			e.printStackTrace();
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	// 사용자 수정
	@RequestMapping(value = "/updateUserInfo.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO updateUserInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				RUserInfo ruserInfo = new RUserInfo();
				String password = null;
				String userId = null;
				String userName = null;
				String userLevel = null;
				String bgCode = null;
				String mgCode = null;
				String sgCode = null;
				String phoneNumber = null;
				String origPhoneNumber = null;
				Integer phoneNumberCount = 2;
				String recsee_mobile="N";
				String usageLimit="N";
				String changeGroup = "N";
				String BatchUsageStatus = null;
				String useYN = "Y";
				
				HttpSession session = request.getSession();
				PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");
				EncryptUtil encryptUtil = new EncryptUtil();
				
				// 권한 정보 불러오기
				userLevel = userInfo.getUserLevel();
				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userLevel);
				accessInfo.setProgramCode("P10043");
				List<MMenuAccessInfo> userAuth = menuAccessInfoService.checkAccessInfo(accessInfo);
				String modiYn = "N";
				if(userAuth.size() > 0) {
					modiYn = userAuth.get(0).getModiYn();
				}
				
				// 계정 정보 수정 권한 체크
				if (!StringUtil.isNull(request.getParameter("userId"), true)) {
					userId=encryptUtil.decryptRsa(privateKey, request.getParameter("userId"));
					ruserInfo.setUserId(userId);
					if(modiYn.equals("N") && !userInfo.getUserId().equals(userId)) {
						throw new Exception();
					}
				}
				
				// 권한변경여부(로그 찍는거때문에 확인)
				boolean authyCheck = false;
				if(!StringUtil.isNull(request.getParameter("authyCheck"), true) && "1".equals(request.getParameter("authyCheck"))) {
					authyCheck = true;
				}
				// 렉시모바일의 경우 라이센스제한에 따른 수정가능 여부 확인
				boolean updateUserInfo=true;
				
				if(request.getSession().getAttribute("recsee_mobile")!=null) {
					recsee_mobile = (String)request.getSession().getAttribute("recsee_mobile");	
				}
				
				if(!StringUtil.isNull(request.getParameter("phoneNumber"), true)) {
					phoneNumber = request.getParameter("phoneNumber");					
				}
				
				if(!StringUtil.isNull(request.getParameter("changeGroup"), true)) {
					changeGroup = request.getParameter("changeGroup");					
				}
				
				// 모바일렉시의 경우 신규번호 추가 가능한지 확인
				Integer mobileUsage = ruserInfoService.mobileUsage();				
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("LICENCE");
				etcConfigInfo.setConfigKey("RECSEE_MOBILE");
				List<EtcConfigInfo> etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				Integer mobileLicence = 0;
				if(etcConfigInfoResult.size() > 0 && etcConfigInfoResult.get(0).getConfigValue() != null) {
					mobileLicence = Integer.parseInt(etcConfigInfoResult.get(0).getConfigValue());
				}				
				if(mobileUsage>=mobileLicence) {
					usageLimit = "Y";
				}
				// 기존 번호 확인
				if (!StringUtil.isNull(request.getParameter("userId"), true)) {
					userId = request.getParameter("userId");
					ruserInfo.setUserId(userId);
					List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
					if(ruserInfoResult.size()>0) {
						origPhoneNumber = ruserInfoResult.get(0).getUserPhone();
					}					
				}
				
				// 변경 번호가 null이면 수정 가능
				if(!StringUtil.isNull(phoneNumber, true)) {
					if("Y".equals(recsee_mobile) && "Y".equals(usageLimit) && !phoneNumber.equals(origPhoneNumber)){		
						if(!StringUtil.isNull(origPhoneNumber, true)) {
							phoneNumberCount = ruserInfoService.checkPhone(origPhoneNumber);						
						}		
						// 기존번호가 중복번호인 경우 
						if (phoneNumberCount > 1) {
							phoneNumberCount = ruserInfoService.checkPhone(phoneNumber);
							// 변경번호가 중복번호라면 수정 가능
							if (phoneNumberCount > 0) {
								updateUserInfo = true;		
							// 변경번호가 신규번호라면 수정 불가능
							} else {
								updateUserInfo = false;							
							}
						// 기존번호가 단독번호(중복번호x)인 경우 신규번호로 수정 가능
						} else {
							updateUserInfo = true;			
						}
					}
				}

				if(updateUserInfo) {
					// 파라 미터 셋팅
					if (!StringUtil.isNull(request.getParameter("userId"), true)) {
						if (!StringUtil.isNull(request.getParameter("aUser"), true)) {
							ruserInfo.setUserId(request.getParameter("userId"));
						}else {						
							userId = encryptUtil.decryptRsa(privateKey, request.getParameter("userId"));
							ruserInfo.setUserId(userId);
						}
					}
					if (!StringUtil.isNull(request.getParameter("userName"), true)){
						userName = request.getParameter("userName");
						ruserInfo.setUserName(request.getParameter("userName"));
					}
						
	
					if (!StringUtil.isNull(request.getParameter("password"), true)) {
						if (!StringUtil.isNull(request.getParameter("aUser"), true)) {
							password = request.getParameter("password");
						}else {	
							password = encryptUtil.decryptRsa(privateKey, request.getParameter("password"));
						}
						String salt = EncryptUtil.salt; // fcpass604! 임의 값
						ruserInfo.setPassword(EncryptUtil.encryptSHA512(password, salt));
					}
	
//					if (!StringUtil.isNull(request.getParameter("extNum"), true)) {
//						// 등록된 내선일 경우 등록 불가
//						ruserInfo.setExtNo(request.getParameter("extNum"));
//					}
	
					if (!StringUtil.isNull(request.getParameter("extNum"),true)
							|| (!StringUtil.isNull(request.getParameter("extNumChanged"), true) 
									&& "Y".equals(request.getParameter("extNumChanged")))) // 수정 안해서 빈값 아니고 있던 번호 없애서 빈값일때 업데이트
							ruserInfo.setExtNo(request.getParameter("extNum"));
					
					if (!StringUtil.isNull(request.getParameter("authy"))) {
						userLevel = request.getParameter("authy");
						if (authyCheck) {
							ruserInfo.setUserLevel(request.getParameter("authy"));
						}
					}
	
					if (!StringUtil.isNull(request.getParameter("phoneNumber"),true)
						|| (!StringUtil.isNull(request.getParameter("isPhoneChanged"), true) 
								&& "Y".equals(request.getParameter("isPhoneChanged")))) // 수정 안해서 빈값 아니고 있던 번호 없애서 빈값일때 업데이트
						ruserInfo.setUserPhone(request.getParameter("phoneNumber"));
					
					if (!StringUtil.isNull(request.getParameter("sex"), true))
						ruserInfo.setUserSex(request.getParameter("sex"));
					
	
					if (!StringUtil.isNull(request.getParameter("bgCode"))) {
						bgCode = request.getParameter("bgCode");
						ruserInfo.setBgCode(request.getParameter("bgCode"));
					}
	
					if (!StringUtil.isNull(request.getParameter("mgCode"))) {
						mgCode = request.getParameter("mgCode");
						ruserInfo.setMgCode(request.getParameter("mgCode"));						
					}
	
					if (!StringUtil.isNull(request.getParameter("sgCode"))) {
						sgCode = request.getParameter("sgCode");
						ruserInfo.setSgCode(request.getParameter("sgCode"));
					}
	
					if (!StringUtil.isNull(request.getParameter("empId"), true))
						ruserInfo.setEmpId(request.getParameter("empId"));
					
//					if (!StringUtil.isNull(request.getParameter("email"), true))
//						ruserInfo.setUserEmail(request.getParameter("email"));
					if (!StringUtil.isNull(request.getParameter("email"),true)
							|| (!StringUtil.isNull(request.getParameter("emailChanged"), true) 
									&& "Y".equals(request.getParameter("emailChanged")))) // 수정 안해서 빈값 아니고 있던 번호 없애서 빈값일때 업데이트
							ruserInfo.setUserEmail(request.getParameter("email"));
					
//					if (!StringUtil.isNull(request.getParameter("ctiId"), true))
//						ruserInfo.setCtiId(request.getParameter("ctiId"));
					if (!StringUtil.isNull(request.getParameter("ctiId"),true)
							|| (!StringUtil.isNull(request.getParameter("ctiIdChanged"), true) 
									&& "Y".equals(request.getParameter("ctiIdChanged")))) // 수정 안해서 빈값 아니고 있던 번호 없애서 빈값일때 업데이트
							ruserInfo.setCtiId(request.getParameter("ctiId"));
					
					
					if (!StringUtil.isNull(request.getParameter("aUser"), true))
						ruserInfo.setaUser(request.getParameter("aUser"));

					if (!StringUtil.isNull(request.getParameter("useYN"), true))
						ruserInfo.setrUseYn(request.getParameter("useYN"));

					if (!StringUtil.isNull(request.getParameter("EmploymentCategory"), true))
						ruserInfo.setrUseYn(request.getParameter("EmploymentCategory"));
					
					if (!StringUtil.isNull(request.getParameter("BatchUsageStatus"), true)) {
						if("N".equals(request.getParameter("BatchUsageStatus"))){
							ruserInfo.setSkinCode(request.getParameter("BatchUsageStatus"));
						}else {
							ruserInfo.setSkinCode(BatchUsageStatus);
						}
					}else {
						ruserInfo.setSkinCode(BatchUsageStatus);
					}
					
					if (!StringUtil.isNull(request.getParameter("skinCode"), true))
						ruserInfo.setSkinCode(request.getParameter("skinCode"));

					// 이력에 권한명, 분류명 남기려고 추가
					if (!StringUtil.isNull(request.getParameter("authyName"), true))
						ruserInfo.setUserLevelName(request.getParameter("authyName"));
					
					if (!StringUtil.isNull(request.getParameter("bgName"), true))
						ruserInfo.setBgName(request.getParameter("bgName"));
	
					if (!StringUtil.isNull(request.getParameter("mgName"), true))
						ruserInfo.setMgName(request.getParameter("mgName"));
	
					if (!StringUtil.isNull(request.getParameter("sgName"), true))
						ruserInfo.setSgName(request.getParameter("sgName"));
					
					Integer selectRUserIpResult=0;
					if ("Y".equals((String)request.getSession().getAttribute("clientIpChk")) && "N".equals((String)request.getSession().getAttribute("ipChkDup"))) {
						ruserInfo.setClientIp(request.getParameter("clientIp"));
						selectRUserIpResult = ruserInfoService.checkIP(ruserInfo);
					}
	
					if(selectRUserIpResult>0) {
						jRes.setResult("IP IS USED");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						if(authyCheck) {
							logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
						}else {
							logService.writeLog(request, "USER", "UPDATE-FAIL",ruserInfo.toString(messageSource));
						}
						
					}else if (!StringUtil.isNull(password, true)) {
						
						String unqLang = SessionManager.getStringAttr(request, "unqLang");
						
						
						etcConfigInfo = new EtcConfigInfo();
						etcConfigInfo.setGroupKey("PASSWORD");
						etcConfigInfo.setConfigKey("PATTERN_USE");
						List<EtcConfigInfo> patternUseYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
						String patternUse = "Y";
						if(patternUseYn.isEmpty()) {
							etcConfigInfo.setConfigValue("Y");
							etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
							patternUseYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
							patternUse = patternUseYn.get(0).getConfigValue();
						} else {
							patternUse = patternUseYn.get(0).getConfigValue();
						}
						
						// 비밀번호 패턴 정규식 검사 영 대/소 숫자 특수문자 8~20자리 사이
						String checkPattern = PwCheck.pwCheck(password, userId, unqLang);
	
						// 패턴 통과 하면
						if (("Y".equals(patternUse) && StringUtil.isNull(checkPattern)) || "N".equals(patternUse)) {
							PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();
							List<PasswordPolicyInfo> list = rPasswordPolicyService.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
							// 비밀번호 정책 데이터가 있고, 사용 이면
							if(list.size() > 0 && "Y".equals(list.get(0).getrPolicyUse())) {
								// 비밀번호 재사용 제한 미적용 권한
								etcConfigInfo = new EtcConfigInfo();
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
										if(userLevel.equals(pastpwLimitlessResult[i])) {
											pastpwLimitless=false;
											break;
										}			
									}
								}
								
								// 과거 비밀번호 미허용 이고 해당 사항 제한권한이 아니면
								if(!list.get(0).getrPastPwUse().equals("Y") && pastpwLimitless) {
									PwHistory pwHistory = new PwHistory();
									String salt = EncryptUtil.salt; // fcpass604! 임의 값
									pwHistory.setrUserId(userId);
									pwHistory.setrPassword(EncryptUtil.encryptSHA512(password, salt));
									int pwCnt = pwHistoryService.selectPwHistory(pwHistory);
	
									// 횟수 제한 일 경우
									if(list.get(0).getrPastPwUse().equals("C")) {
										if(Integer.parseInt(list.get(0).getrPastPwCount()) <= pwCnt) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.setResult("PASSWORD IS USED");
											ruserInfo.setLogContents(messageSource.getMessage("log.etc.cntOverPw", null,Locale.getDefault()));
											if(authyCheck) {
												logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
											}else {
												logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
											}
											return jRes;
										}
									// 아예 사용 불가능 이면
									} else {
										if(pwCnt > 0) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.setResult("PASSWORD IS USED");
											ruserInfo.setLogContents(messageSource.getMessage("log.etc.noPrePw", null,Locale.getDefault()));
											if(authyCheck) {
												logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
											}else {
												logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
											}
											return jRes;
										}
									}
								}
							}
							
							if("Y".equals(request.getSession().getAttribute("updateGroupinfoYn")) &&"Y".equals(changeGroup)) {
								if(!updateGroupInfo(userId, request.getSession(),request)) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("update groupinfo Error");
									return jRes;
								}
							}
								
							Integer updateRUserResult = ruserInfoService.updateRUserInfo(ruserInfo);
							if (updateRUserResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "user update success");

								// 비번 변경 히스토리에 남기기
								PwHistory pwHistory = new PwHistory();
								String salt = EncryptUtil.salt; // fcpass604! 임의 값
								pwHistory.setrUserId(userId);
								pwHistory.setrPassword(EncryptUtil.encryptSHA512(password, salt));
								pwHistoryService.insertPwHistory(pwHistory);

								// 엔진에 변경 사실 알리기 위해 플래그 업데이트
								SystemInfo systemInfo = new SystemInfo();
								systemInfo.setChannelUpdateFlag("2");
								systemInfoService.updateSystemInfo(systemInfo);
								ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChange", null,Locale.getDefault()));
								if(authyCheck) {
									logService.writeLog(request, "USER", "AUTHY-UPDATE-SUCCESS",ruserInfo.toString(messageSource));
								}else {
									logService.writeLog(request, "USER", "UPDATE-SUCCESS", ruserInfo.toString(messageSource));
								}
								
							} else {
								jRes.setResult("UPDATE FAIL");
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChangeFail", null,Locale.getDefault()));
								if(authyCheck) {
									logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
								}else {
									logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("PASSWORD PATTERN IS MISS MATCH");
							jRes.addAttribute("msg", checkPattern);
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChangeFail", null,Locale.getDefault()));
							if(authyCheck) {
								logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
							}else {
								logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
							}
						}
					} else {
						
						if("Y".equals(request.getSession().getAttribute("updateGroupinfoYn")) &&"Y".equals(changeGroup)) {
							if(!updateGroupInfo(userId, request.getSession(), request)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("update groupinfo Error");
								return jRes;
							}
						}
						Integer updateRUserResult = ruserInfoService.updateRUserInfo(ruserInfo);
						if (updateRUserResult == 1) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "user update success");
	
							// 엔진에 변경 사실 알리기 위해 플래그 업데이트
							SystemInfo systemInfo = new SystemInfo();
							systemInfo.setChannelUpdateFlag("2");
							systemInfoService.updateSystemInfo(systemInfo);
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.userModi", null,Locale.getDefault()));
							if(authyCheck) {
								logService.writeLog(request, "USER", "AUTHY-UPDATE-SUCCESS",ruserInfo.toString(messageSource));
							}else {
								logService.writeLog(request, "USER", "UPDATE-SUCCESS", ruserInfo.toString(messageSource));
							}
							
						} else {
							jRes.setResult("UPDATE FAIL");
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.userModiFail", null,Locale.getDefault()));
							if(authyCheck) {
								logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
							}else {
								logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
							}
						}
					} 
				}else {
					jRes.setResult("NEW NUMBER REGISTRATION IS NOT POSSIBLE");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					ruserInfo.setLogContents(messageSource.getMessage("log.etc.mobileLicenseExceed", null,Locale.getDefault()));
					if(authyCheck) {
						logService.writeLog(request, "USER", "AUTHY-UPDATE-FAIL",ruserInfo.toString(messageSource));
					}else {
						logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
					}
				}
				// 본인 정보 수정시 세션 변경
				if("Y".equals(jRes.getSuccess())) {
					if(userId!=null && userInfo!=null && userId.equals(userInfo.getUserId())) {
						userInfo.setUserName(userName);
						userInfo.setUserLevel(userLevel);
						userInfo.setBgCode(bgCode);
						userInfo.setMgCode(mgCode);
						userInfo.setSgCode(sgCode);
					}
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("ERROR NullPointerException");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR Exception");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// 사용자 수정 본인 정보만..
	@RequestMapping(value = "/updateUserInfoIndevidaul.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO updateUserInfoIndevidaul(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				HttpSession session = request.getSession();
				PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");
				EncryptUtil encryptUtil = new EncryptUtil();
				RUserInfo ruserInfo = new RUserInfo();
				String password = null;
				String userId = null;
				
				// 권한 정보 불러오기
				String userLevel = userInfo.getUserLevel();
				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userLevel);
				accessInfo.setProgramCode("P10043");
				List<MMenuAccessInfo> userAuth = menuAccessInfoService.checkAccessInfo(accessInfo);
				String modiYn = "N";
				if(userAuth.size() > 0) {
					modiYn = userAuth.get(0).getModiYn();
				}
				
				// 파라 미터 셋팅
				if (!StringUtil.isNull(request.getParameter("userId"), true)) {
					userId=encryptUtil.decryptRsa(privateKey, request.getParameter("userId"));
					ruserInfo.setUserId(userId);
					if(modiYn.equals("N") && !userInfo.getUserId().equals(userId)) {
						throw new Exception();
					}
				}
				
				
				if (!StringUtil.isNull(request.getParameter("userName"), true)) {
					userInfo.setUserName(request.getParameter("userName"));
					ruserInfo.setUserName(request.getParameter("userName"));
				}

				if (!StringUtil.isNull(request.getParameter("password"), true)) {
					password = encryptUtil.decryptRsa(privateKey, request.getParameter("password"));

					String salt = EncryptUtil.salt; // fcpass604! 임의 값
					ruserInfo.setPassword(EncryptUtil.encryptSHA512(password, salt));
				}

				if (!StringUtil.isNull(request.getParameter("extNum"), true))
					ruserInfo.setExtNo(request.getParameter("extNum"));
				

				if (!StringUtil.isNull(request.getParameter("phoneNumber"),true))
					ruserInfo.setUserPhone(request.getParameter("phoneNumber"));
				

				if (!StringUtil.isNull(request.getParameter("sex"), true))
					ruserInfo.setUserSex(request.getParameter("sex"));

				if (!StringUtil.isNull(request.getParameter("empId"), true))
					ruserInfo.setEmpId(request.getParameter("empId"));
				else
					ruserInfo.setEmpId("");

				if (!StringUtil.isNull(request.getParameter("email"), true))
					ruserInfo.setUserEmail(request.getParameter("email"));
				else
					ruserInfo.setUserEmail("");

				if (!StringUtil.isNull(password, true)) {

					
					String unqLang = SessionManager.getStringAttr(request, "unqLang");
					
					// 비밀번호 패턴 정규식 검사 영 대/소 숫자 특수문자 8~20자리 사이
					String checkPattern = PwCheck.pwCheck(password, userId, unqLang);

					// 패턴 통과 하면
					if (StringUtil.isNull(checkPattern)) {
						
						PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();
						List<PasswordPolicyInfo> list = rPasswordPolicyService.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
						// 비밀번호 정책 데이터가 있고, 사용 이면
						if(list.size() > 0 && "Y".equals(list.get(0).getrPolicyUse())) {
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
							// 과거 비밀번호 사용제한 이고 해당 사항 제한권한이 아니면
							if(!list.get(0).getrPastPwUse().equals("Y") && pastpwLimitless) {
								PwHistory pwHistory = new PwHistory();
								String salt = EncryptUtil.salt; // fcpass604! 임의 값
								pwHistory.setrUserId(userId);
								pwHistory.setrPassword(EncryptUtil.encryptSHA512(password, salt));
								int pwCnt = pwHistoryService.selectPwHistory(pwHistory);

								// 횟수 제한 일 경우
								if(list.get(0).getrPastPwUse().equals("C")) {
									if(Integer.parseInt(list.get(0).getrPastPwCount()) <= pwCnt) {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
										jRes.setResult("PASSWORD IS USED");
										ruserInfo.setLogContents(messageSource.getMessage("log.etc.cntOverPw", null,Locale.getDefault()));
										logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
										return jRes;
									}
								// 아예 사용 불가능 이면
								} else {
									if(pwCnt > 0) {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
										jRes.setResult("PASSWORD IS USED");
										ruserInfo.setLogContents(messageSource.getMessage("log.etc.noPrePw", null,Locale.getDefault()));
										logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
										return jRes;
									}
								}
							}
						}
							
						Integer updateRUserResult = ruserInfoService.updateRUserInfo(ruserInfo);
						if (updateRUserResult == 1) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "user update success");

							// 비번 변경 히스토리에 남기기
							PwHistory pwHistory = new PwHistory();
							String salt = EncryptUtil.salt; // fcpass604! 임의 값
							pwHistory.setrUserId(userId);
							pwHistory.setrPassword(EncryptUtil.encryptSHA512(password, salt));
							pwHistoryService.insertPwHistory(pwHistory);

							// 엔진에 변경 사실 알리기 위해 플래그 업데이트
							SystemInfo systemInfo = new SystemInfo();
							systemInfo.setChannelUpdateFlag("2");
							systemInfoService.updateSystemInfo(systemInfo);
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChange", null,Locale.getDefault()));
							logService.writeLog(request, "USER", "UPDATE-SUCCESS", ruserInfo.toString(messageSource));
							
						} else {
							jRes.setResult("UPDATE FAIL");
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChangeFail", null,Locale.getDefault()));
							logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
						}
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("PASSWORD PATTERN IS MISS MATCH");
						jRes.addAttribute("msg", checkPattern);
						ruserInfo.setLogContents(messageSource.getMessage("log.etc.pwChangeFail", null,Locale.getDefault()));
						logService.writeLog(request, "USER", "UPDATE-FAIL", ruserInfo.toString(messageSource));
					}
				} else {
					Integer updateRUserResult = ruserInfoService.updateRUserInfo(ruserInfo);
					if (updateRUserResult == 1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
						jRes.addAttribute("msg", "user update success");
						ruserInfo.setLogContents(messageSource.getMessage("log.etc.userModi", null,Locale.getDefault()));
						logInfoService.writeLog(request,"User - Update success", ruserInfo.toString(messageSource),userInfo.getUserId());
					} else {
						jRes.setResult("UPDATE FAIL");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						ruserInfo.setLogContents(messageSource.getMessage("log.etc.userModiFail", null,Locale.getDefault()));
						logInfoService.writeLog(request, "User - Update fail",ruserInfo.toString(messageSource), userInfo.getUserId());
					}
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			e.printStackTrace();
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			e.printStackTrace();
		}
		return jRes;
	}
			
	// 사용자 삭제
	@RequestMapping(value = "/deleteUserInfo.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody AJaxResVO deleteUserInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				RUserInfo ruserInfo = new RUserInfo();

				// 파라 미터 셋팅
				if (!StringUtil.isNull(request.getParameter("userList"), true)) {
					String[] userList = request.getParameter("userList").split(
							",");
					if (!StringUtil.isNull(request.getParameter("aUser"), true)) {
						ruserInfo.setaUser(request.getParameter("aUser"));
					}
					for (int i = 0; i < userList.length; i++) {
						ruserInfo.setUserId(userList[i]);

						Integer insertRUserResult = ruserInfoService
								.deleteRUserInfo(ruserInfo);
						if (insertRUserResult == 1) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "user delete success");

							SystemInfo systemInfo = new SystemInfo();
							systemInfo.setChannelUpdateFlag("2");
							systemInfoService.updateSystemInfo(systemInfo);
							logService.writeLog(request, "USER", "DELETE-SUCCESS", ruserInfo.toString(messageSource));
							// logInfoService.writeLog(request,
							// "User - Delete success", ruserInfo.toString(messageSource),
							// userInfo.getUserId());

						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "user delete fail");
							logService.writeLog(request, "USER", "DELETE-FAIL", ruserInfo.toString(messageSource));
							// logInfoService.writeLog(request,
							// "User - Delete fail", ruserInfo.toString(messageSource),
							// userInfo.getUserId());

						}
					}
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// 엑셀 다운로드
	@RequestMapping(value="/userManageRecExcel.do")
	public void userManageRecExcel(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		LoginVO userInfo = SessionManager.getUserInfo(request);

		List<String[]> contents = new ArrayList<String[]>();
		String[] row = null;
		int colPos = 0;
		RUserInfo ruserInfo = new RUserInfo();

		@SuppressWarnings("unchecked")		
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "userManage.userManageRec");
		
		String recsee_mobile="N";
		
		if(request.getSession().getAttribute("recsee_mobile")!=null) {
			recsee_mobile = (String)request.getSession().getAttribute("recsee_mobile");	
		}

		if ("N".equals(nowAccessInfo.getExcelYn())) {
			row = new String[1];
			row[0] = messageSource.getMessage("views.search.alert.msg78", null,Locale.getDefault())/*"권한이 없는 사용자의 요청입니다.\n엑셀 다운로드 권한이 있는 사용자로 다운로드 요청을 해 주세요!"*/;
			contents.add(row);
			ModelMap.put("excelList", contents);
			ModelMap.put("target", "userList");
			String realPath = request.getSession().getServletContext().getRealPath("/search");
			ExcelView.createXlsx(ModelMap, realPath, response);
		}else if(userInfo != null) {
			//SAFE DB CHECK
			String loginIpChk="N";
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LOGIN");
			etcConfigInfo.setConfigKey("LOGIN_IP_CHK");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			if(etcConfigResult.size()>0&&"Y".equals(etcConfigResult.get(0).getConfigValue())) {
				loginIpChk="Y";
			}	
			if("Y".equals(recsee_mobile)) {
				row = new String[9];
				for(int i= 0; i < 9 ;i++ ) {
					switch(i) {
					case 0:
						row[i] = messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault());
						break;
					case 1:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.userName", null,Locale.getDefault());
						break;
					case 2:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.userId", null,Locale.getDefault());
						break;
					case 3:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.ext", null,Locale.getDefault());
						break;
					case 4:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.phoneNo", null,Locale.getDefault());
						break;
					case 5:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.userLevel", null,Locale.getDefault());
						break;
					case 6:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.level1", null,Locale.getDefault());
						break;
					case 7:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.level2", null,Locale.getDefault());
						break;
					case 8:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.level3", null,Locale.getDefault());
						break;				
					}
				}
				contents.add(row);
			
				
	
				if(request.getParameter("userName") != null) {
					ruserInfo.setUserName(request.getParameter("userName"));
				}
				if(request.getParameter("userId") != null) {
					ruserInfo.setUserId(request.getParameter("userId"));
				}
				if(request.getParameter("extNo") != null) {
					ruserInfo.setExtNo(request.getParameter("extNo"));
				}

				if(request.getParameter("Authy") != null) {
					ruserInfo.setUserLevel(request.getParameter("Authy"));
				}
				if(request.getParameter("bgCode") != null) {
					ruserInfo.setBgCode(request.getParameter("bgCode"));
				}

				if(request.getParameter("mgCode") != null) {
					ruserInfo.setMgCode(request.getParameter("mgCode"));
				}

				if(request.getParameter("sgCode") != null) {
					ruserInfo.setSgCode(request.getParameter("sgCode"));
				}
				
				List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelectExcel(ruserInfo);
				Integer ruserInfoResultTotal = ruserInfoResult.size();
				
				for(int i = 0; i < ruserInfoResultTotal; i++) {
					RUserInfo rUserInfoItem = ruserInfoResult.get(i);
					
					row = new String[row.length];
	
					colPos = 0;
					
					setValue(row, String.valueOf(i+1), colPos++);
					setValue(row, rUserInfoItem.getUserName(), colPos++);
					setValue(row, rUserInfoItem.getUserId(), colPos++);
					setValue(row, rUserInfoItem.getExtNo(), colPos++);
					setValue(row, rUserInfoItem.getUserPhone(), colPos++);
					setValue(row, rUserInfoItem.getLevelName(), colPos++);
					setValue(row, rUserInfoItem.getBgName(), colPos++);
					setValue(row, rUserInfoItem.getMgName(), colPos++);
					setValue(row, rUserInfoItem.getSgName(), colPos++);
					
					contents.add(row);
				}
			} else {
				row = new String[11];
				for(int i= 0; i < 11 ;i++ ) {
					switch(i) {
					case 0:
						row[i] = messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault());
						break;
					case 1:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.userName", null,Locale.getDefault());
						break;
					case 2:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.userId", null,Locale.getDefault());
						break;
					case 3:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.ext", null,Locale.getDefault());
						break;
					case 4:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.phoneNo", null,Locale.getDefault());
						break;
					case 5:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.sex", null,Locale.getDefault());
						break;
					case 6:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.userLevel", null,Locale.getDefault());
						break;
					case 7:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.level1", null,Locale.getDefault());
						break;
					case 8:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.level2", null,Locale.getDefault());
						break;
					case 9:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.level3", null,Locale.getDefault());
						break;
					case 10:
						row[i] = messageSource.getMessage("header.popup.modifyUser.label.email", null,Locale.getDefault());
						break;
					}
				}
				contents.add(row);
			
				
	
				if(request.getParameter("userName") != null) {
					ruserInfo.setUserName(request.getParameter("userName"));
				}
				if(request.getParameter("userId") != null) {
					ruserInfo.setUserId(request.getParameter("userId"));
				}
				if(request.getParameter("extNo") != null) {
					ruserInfo.setExtNo(request.getParameter("extNo"));
				}

				if(request.getParameter("Authy") != null) {
					ruserInfo.setUserLevel(request.getParameter("Authy"));
				}
				if(request.getParameter("bgCode") != null) {
					ruserInfo.setBgCode(request.getParameter("bgCode"));
				}

				if(request.getParameter("mgCode") != null) {
					ruserInfo.setMgCode(request.getParameter("mgCode"));
				}

				if(request.getParameter("sgCode") != null) {
					ruserInfo.setSgCode(request.getParameter("sgCode"));
				}
				
				List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelectExcel(ruserInfo);
				Integer ruserInfoResultTotal = ruserInfoResult.size();
				
				for(int i = 0; i < ruserInfoResultTotal; i++) {
					RUserInfo rUserInfoItem = ruserInfoResult.get(i);
					
					row = new String[row.length];
	
					colPos = 0;
					
					setValue(row, String.valueOf(i+1), colPos++);
					setValue(row, rUserInfoItem.getUserName(), colPos++);
					setValue(row, rUserInfoItem.getUserId(), colPos++);
					setValue(row, rUserInfoItem.getExtNo(), colPos++);
					setValue(row, rUserInfoItem.getUserPhone(), colPos++);
					if("m".equals(rUserInfoItem.getUserSex())) {
						setValue(row, messageSource.getMessage("log.etc.man", null,Locale.getDefault()), colPos++);
					}else if("w".equals(rUserInfoItem.getUserSex())) {
						setValue(row, messageSource.getMessage("log.etc.woman", null,Locale.getDefault()), colPos++);
					}else {
						setValue(row, rUserInfoItem.getUserSex(), colPos++);
					}
					setValue(row, rUserInfoItem.getLevelName(), colPos++);
					setValue(row, rUserInfoItem.getBgName(), colPos++);
					setValue(row, rUserInfoItem.getMgName(), colPos++);
					setValue(row, rUserInfoItem.getSgName(), colPos++);
					setValue(row, rUserInfoItem.getUserEmail(), colPos++);
					
					contents.add(row);
				}
			}
			ModelMap.put("excelList", contents);
			ModelMap.put("target", "userList");

			String realPath = request.getSession().getServletContext().getRealPath("/search");
			ExcelView.createXlsx(ModelMap, realPath, response);
			row = null;
			contents = null;
			logService.writeLog(request, "EXCELDOWN", "DO", ruserInfo.toLogString(messageSource));
		}
	}
	
	// 계정 잠금 풀기
	@RequestMapping(value = "/unlockUser.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO unlockUser( HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				
				RUserInfo ruserInfo = new RUserInfo();
				
				if(request.getParameter("userId") != null) {
					ruserInfo.setUserId(request.getParameter("userId"));
					
					String salt = EncryptUtil.salt; // fcpass604! 임의 값
					ruserInfo.setPassword(EncryptUtil.encryptSHA512(request.getParameter("userId"), salt));
					
					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("PASSWORD");
					etcConfigInfo.setConfigKey("CHANGE_FIRST_LOGIN");
					List<EtcConfigInfo> changeFirstLoginList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					String changeFirstLoginYn = changeFirstLoginList.get(0).getConfigValue();
					if("Y".equals(changeFirstLoginYn)) {
						ruserInfo.setSkinCode("Y");
					}
					
					Integer unlockUserResult = ruserInfoService.unlockUser(ruserInfo);
					if (unlockUserResult > 0) {
						jRes.setResult("UNLOCK_SUCCESS");
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						logService.writeLog(request, "USER", "UPDATE-SUCCESS", messageSource.getMessage("log.etc.unlockUser", null,Locale.getDefault())+" : ID="+ruserInfo.getUserId());
					} else {
						jRes.setResult("UNLOCK_FAIL");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
					}
				} else {
					jRes.setResult("NODATA");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("NULL ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// 모바일사용 현황 불러오기
	@RequestMapping(value = "/mobileUsage.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO mobileUsage( HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				
				Integer mobileUsage = ruserInfoService.mobileUsage();
				
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("LICENCE");
				etcConfigInfo.setConfigKey("RECSEE_MOBILE");
				List<EtcConfigInfo> etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				Integer mobileLicence = 0;
				if(etcConfigInfoResult.size() > 0 && etcConfigInfoResult.get(0).getConfigValue() != null) {
					mobileLicence = Integer.parseInt(etcConfigInfoResult.get(0).getConfigValue());
				}				
				
				if (mobileUsage >= 0 && mobileLicence>=0) {
					jRes.addAttribute("mobileUsage", mobileUsage);
					jRes.addAttribute("mobileLicence", mobileLicence);
					jRes.setResult("SUCCESS");
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setResult("NODATA");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("NULL ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
			
	
	// 다인그룹변경
	@RequestMapping(value = "/multiGroupModify.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody AJaxResVO multiGroupModify(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		try {
			if (userInfo != null) {
				RUserInfo ruserInfo = new RUserInfo();
				
				// 파라 미터 셋팅
				if (!StringUtil.isNull(request.getParameter("userid"), true)) {
					ruserInfo.setUserId(request.getParameter("userid"));
				}
				if (!StringUtil.isNull(request.getParameter("bgcode"), true)) {
					ruserInfo.setBgCode(request.getParameter("bgcode"));
				}
				if (!StringUtil.isNull(request.getParameter("mgcode"), true)) {
					ruserInfo.setMgCode(request.getParameter("mgcode"));
				}
				if (!StringUtil.isNull(request.getParameter("sgcode"), true)) {
					ruserInfo.setSgCode(request.getParameter("sgcode"));
				}
				
				Integer insertRUserResult = ruserInfoService.multiGroupModify(ruserInfo);
				
				if (insertRUserResult > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("1");
					jRes.addAttribute("msg", "user update success");

					SystemInfo systemInfo = new SystemInfo();
					systemInfo.setChannelUpdateFlag("2");
					systemInfoService.updateSystemInfo(systemInfo);
					logService.writeLog(request, "USER",
							"UPDATE-SUCCESS", ruserInfo.toString(messageSource));
					// logInfoService.writeLog(request,
					// "User - Delete success", ruserInfo.toString(messageSource),
					// userInfo.getUserId());

				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "user update fail");
					logService.writeLog(request, "USER", "UPDATE-FAIL",
							ruserInfo.toString(messageSource));
					// logInfoService.writeLog(request,
					// "User - Delete fail", ruserInfo.toString(messageSource),
					// userInfo.getUserId());
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
		
	// 비밀번호 정책 불러오기
	@RequestMapping(value = "/selectRPasswordPolicyInfo.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO selectRPasswordPolicyInfo(
			HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();

				List<PasswordPolicyInfo> list = rPasswordPolicyService
						.selectRPasswordPolicyInfo(rPasswordPolicyInfo);

				if (list.size() > 0) {
					jRes.addAttribute("policyInfo", list.get(0));
					jRes.setResult("SUCESS");
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setResult("NODATA");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}

			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	// 비밀번호 정책 수정하기
	@RequestMapping(value = "/updateRPasswordPolicyInfo.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO updateRPasswordPolicyInfo(
			HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try {
			if (userInfo != null) {
				PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();

				if (!StringUtil.isNull(request.getParameter("policyUse"), true))
					rPasswordPolicyInfo.setrPolicyUse(request
							.getParameter("policyUse"));

				if (!StringUtil.isNull(request.getParameter("cycleUse"), true))
					rPasswordPolicyInfo.setrCycleUse(request
							.getParameter("cycleUse"));

				if (!StringUtil.isNull(request.getParameter("cycle"), true))
					rPasswordPolicyInfo
							.setrCycle(request.getParameter("cycle"));

				if (!StringUtil.isNull(request.getParameter("cycleType"), true))
					rPasswordPolicyInfo.setrCycleType(request
							.getParameter("cycleType"));

				if (!StringUtil.isNull(request.getParameter("pastPwUse"), true))
					rPasswordPolicyInfo.setrPastPwUse(request
							.getParameter("pastPwUse"));

				if (!StringUtil.isNull(request.getParameter("pastPwCount"),
						true))
					rPasswordPolicyInfo.setrPastPwCount(request
							.getParameter("pastPwCount"));

				if (!StringUtil.isNull(request.getParameter("tryUse"), true))
					rPasswordPolicyInfo.setrTryUse(request
							.getParameter("tryUse"));

				if (!StringUtil.isNull(request.getParameter("tryCount"), true))
					rPasswordPolicyInfo.setrTryCount(request
							.getParameter("tryCount"));

				if (!StringUtil.isNull(request.getParameter("lockUse"), true))
					rPasswordPolicyInfo.setrLockUse(request
							.getParameter("lockUse"));

				if (!StringUtil.isNull(request.getParameter("lockCount"), true))
					rPasswordPolicyInfo.setrLockCount(request
							.getParameter("lockCount"));

				if (!StringUtil.isNull(request.getParameter("lockType"), true))
					rPasswordPolicyInfo.setrLockType(request
							.getParameter("lockType"));

				Integer result = rPasswordPolicyService
						.updateRPasswordPolicyInfo(rPasswordPolicyInfo);

				if (result > 0) {
					jRes.setResult("UPDATESUCESS");
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setResult("UPDATEFAIL");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	// Administer - 시스템 관리 - 상세관리
//	@RequestMapping(value = "/etc_proc.do", method = { RequestMethod.POST,
//			RequestMethod.GET }, headers = "Content-Type= multipart/form-data")
//	public @ResponseBody AJaxResVO etc_proc(
//			MultipartHttpServletRequest request, Locale local, Model model)
//			throws BiffException, PSQLException, IOException, ServletException {
//		AJaxResVO jRes = new AJaxResVO();
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//
//		if (userInfo != null) {
//			if (request.getParameter("proc") != null) {
//				if (request.getParameter("proc").equals("testMail")) {
//					SendMailInfo sendMailInfo = new SendMailInfo();
//
//					sendMailInfo.setHost(request.getParameter("send_server"));
//					sendMailInfo.setPort(Integer.parseInt(request
//							.getParameter("send_server_port")));
//
//					if (request.getParameter("email_id") != null
//							&& !request.getParameter("email_id").trim()
//									.isEmpty()) {
//						sendMailInfo.setUserName(request
//								.getParameter("email_id"));
//					}
//					if (request.getParameter("email_pw") != null
//							&& !request.getParameter("email_pw").trim()
//									.isEmpty()) {
//						sendMailInfo.setPassword(request
//								.getParameter("email_pw"));
//					}
//
//					sendMailInfo.setFrom(request.getParameter("from_email")
//							.trim());
//					sendMailInfo.setTo(request.getParameter(
//							"mailReceiverAddress").trim()); // 수신자 메일 주소
//					sendMailInfo.setSubject(messageSource.getMessage(
//							"management.etc.message.testmail.subject", null,
//							Locale.getDefault()));
//					sendMailInfo.setText(messageSource.getMessage(
//							"management.etc.message.testmail.text", null,
//							Locale.getDefault()));
//					boolean rst = SendMailUtil.sendMail(request, sendMailInfo);
//
//					if (rst) {
//						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//						jRes.setResult("1");
//						jRes.addAttribute("msg", "Etc - Test mail send success");
//
//						logInfoService.writeLog(request,
//								"Etc - Test mail send success",
//								sendMailInfo.toString(), userInfo.getUserId());
//					} else {
//						jRes.setSuccess(AJaxResVO.SUCCESS_N);
//						jRes.setResult("0");
//						jRes.addAttribute("msg", "Etc - Test mail send fail");
//
//						logInfoService.writeLog(request,
//								"Etc - Test mail send fail",
//								sendMailInfo.toString(), userInfo.getUserId());
//					}
//				} else if (request.getParameter("proc").equals("etcInfo")) {
//					EtcConfigInfo etcConfigInfo = null;
//
//					// 메일 서버 주소
//					if (request.getParameter("send_server") != null) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("send_server");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("send_server"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					} else {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("send_server");
//						etcConfigInfo.setConfigValue("");
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 메일 발송 포트
//					if (request.getParameter("send_server_port") != null) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("send_server_port");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("send_server_port"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					} else {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("send_server_port");
//						etcConfigInfo.setConfigValue("");
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 메일 발송 ID
//					if (request.getParameter("email_id") != null) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("email_id");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("email_id"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					} else {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("email_id");
//						etcConfigInfo.setConfigValue("");
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 메일 발송 비밀번호
//					if (request.getParameter("email_pw") != null) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("email_pw");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("email_pw"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					} else {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("email_pw");
//						etcConfigInfo.setConfigValue("");
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 발신자 메일 주소
//					if (request.getParameter("from_email") != null
//							&& !request.getParameter("from_email").trim()
//									.isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("from_email");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("from_email"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					} else {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("smtp");
//						etcConfigInfo.setConfigKey("from_email");
//						etcConfigInfo.setConfigValue("");
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// Fault 알림 주기 정보
//					if (request.getParameter("alert_rate") != null
//							&& !request.getParameter("alert_rate").trim()
//									.isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("xprocess");
//						etcConfigInfo.setConfigKey("alert_rate");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("alert_rate"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					} else {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("xprocess");
//						etcConfigInfo.setConfigKey("alert_rate");
//						etcConfigInfo.setConfigValue("");
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//
//					/*
//					 * if(request.getParameter("realTimeMonitoringCycle") !=
//					 * null &&
//					 * !request.getParameter("realTimeMonitoringCycle").trim
//					 * ().isEmpty()) { etcConfigInfo = new EtcConfigInfo();
//					 *
//					 * etcConfigInfo.setGroupKey("real_monitoring");
//					 * etcConfigInfo.setConfigKey("real_monitoring_interval");
//					 * etcConfigInfo.setConfigValue(request.getParameter(
//					 * "realTimeMonitoringCycle"));
//					 *
//					 * etcConfigInfoService.setEtcConfigInfo(etcConfigInfo); }
//					 */
//
//					// ipkts Alive Check
//					if (request.getParameter("ipkts_keep_alive_check") != null
//							&& !request.getParameter("ipkts_keep_alive_check")
//									.trim().isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("SYSTEM");
//						etcConfigInfo.setConfigKey("IPKTS_KEEP_ALIVE_CHECK");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("ipkts_keep_alive_check"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 랜카드
//					if (request.getParameter("len") != null
//							&& !request.getParameter("len").trim().isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("SYSTEM");
//						etcConfigInfo.setConfigKey("LEN");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("len"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 에이전트 ID 출력 옵션
//					if (request.getParameter("agentidoption") != null
//							&& !request.getParameter("agentidoption").trim()
//									.isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("Search");
//						etcConfigInfo.setConfigKey("AgentIdOption");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("agentidoption"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 다운로드 파일 확장자
//					if (request.getParameter("file_extension") != null
//							&& !request.getParameter("file_extension").trim()
//									.isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("download");
//						etcConfigInfo.setConfigKey("FILE_EXTENSION");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("file_extension"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// SIP Log
//					if (request.getParameter("sip_log") != null
//							&& !request.getParameter("sip_log").trim()
//									.isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("SYSTEM");
//						etcConfigInfo.setConfigKey("SIP_LOG");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("sip_log"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 암호화된 파일 다운로드
//					if (request.getParameter("encrypt") != null
//							&& !request.getParameter("encrypt").trim()
//									.isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("download");
//						etcConfigInfo.setConfigKey("encrypt");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("encrypt"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					/*
//					 * if(request.getParameter("haName") != null &&
//					 * !request.getParameter("haName").trim().isEmpty()) {
//					 * etcConfigInfo = new EtcConfigInfo();
//					 *
//					 * etcConfigInfo.setGroupKey("SYSTEM");
//					 * etcConfigInfo.setConfigKey("HA_NAME");
//					 * etcConfigInfo.setConfigValue
//					 * (request.getParameter("haName"));
//					 *
//					 * etcConfigInfoService.setEtcConfigInfo(etcConfigInfo); }
//					 */
//					// 안내 멘트 녹취
//					if (request.getParameter("announce_recording_yn") != null
//							&& !request.getParameter("announce_recording_yn")
//									.trim().isEmpty()) {
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("IPCR");
//						etcConfigInfo.setConfigKey("ANNOUNCE_RECORDING_YN");
//						etcConfigInfo.setConfigValue(request
//								.getParameter("announce_recording_yn"));
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//					// 파일명 패턴
//					if (request.getParameter("filename_a") != null
//							|| request.getParameter("filename_b") != null
//							|| request.getParameter("filename_c") != null) {
//
//						String filenamePattern = "";
//						if (request.getParameter("filename_a") != null
//								&& !request.getParameter("filename_a").trim()
//										.isEmpty()) {
//							filenamePattern += request
//									.getParameter("filename_a");
//						}
//
//						if (request.getParameter("filename_b") != null
//								&& !request.getParameter("filename_b").trim()
//										.isEmpty()) {
//							filenamePattern += (filenamePattern.length() > 0 ? "-"
//									: "")
//									+ request.getParameter("filename_b");
//						} else {
//							filenamePattern += (filenamePattern.length() > 0 ? "-"
//									: "");
//						}
//						if (request.getParameter("filename_c") != null
//								&& !request.getParameter("filename_c").trim()
//										.isEmpty()) {
//							filenamePattern += (filenamePattern.length() > 0 ? "-"
//									: "")
//									+ request.getParameter("filename_c");
//						} else {
//							filenamePattern += (filenamePattern.length() > 0 ? "-"
//									: "");
//						}
//
//						etcConfigInfo = new EtcConfigInfo();
//
//						etcConfigInfo.setGroupKey("SYSTEM");
//						etcConfigInfo.setConfigKey("FILENAME_PATTERN");
//						etcConfigInfo.setConfigValue(filenamePattern);
//
//						etcConfigInfoService.setEtcConfigInfo(etcConfigInfo);
//					}
//
//					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//					jRes.setResult("1");
//					jRes.addAttribute("msg", "Etc - Base info process");
//
//					logInfoService.writeLog(request, "Etc - Base Info process",
//							null, userInfo.getUserId());
//				} else if (request.getParameter("proc").equals("announceFile")) {
//
//					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//					etcConfigInfo.setGroupKey("UPLOAD");
//					etcConfigInfo.setConfigKey("ANNOUNCE_FILE_PATH");
//					List<EtcConfigInfo> announceFilePathResult = etcConfigInfoService
//							.selectEtcConfigInfo(etcConfigInfo);
//					String announceFilePath = System
//							.getProperty("catalina.home")
//							+ File.separator
//							+ "AnnounceFiles";
//					if (announceFilePathResult.size() > 0) {
//						if (announceFilePathResult.get(0).getConfigValue() != null) {
//							announceFilePath = announceFilePathResult.get(0)
//									.getConfigValue();
//						}
//					}
//
//					// MultipartHttpServletRequest 생성
//					MultipartHttpServletRequest mhsr = request;
//					Iterator<String> iter = mhsr.getFileNames();
//
//					MultipartFile mfile = null;
//					String fieldName = "";
//
//					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//					jRes.setResult("1");
//					jRes.addAttribute("msg",
//							"Etc - Announce File upload success");
//
//					// 값이 나올때까지
//					while (iter.hasNext()) {
//						fieldName = iter.next(); // 내용을 가져와서
//						mfile = mhsr.getFile(fieldName);
//
//						String targetName = "voice";
//						Pattern p = Pattern.compile("([2-9]|10)$");
//						Matcher m = p.matcher(fieldName);
//						if (m.find()) {
//							targetName += fieldName.replace("voiceFile", "");
//						}
//						targetName += ".au";
//
//						boolean rst = FileUploadUtil.fileUploadProc(request,
//								mfile, targetName, announceFilePath);
//
//						if (rst) {
//							logInfoService.writeLog(request,
//									"Etc - Announce File upload success",
//									targetName, userInfo.getUserId());
//						} else {
//							jRes.setSuccess(AJaxResVO.SUCCESS_N);
//							jRes.setResult("0");
//							jRes.addAttribute("msg",
//									"Announce File upload fail");
//
//							logInfoService.writeLog(request,
//									"Etc - Announce File upload fail",
//									targetName, userInfo.getUserId());
//
//							return jRes;
//						}
//					}
//
//					Enumeration<String> params = request.getParameterNames();
//					while (params.hasMoreElements()) {
//						String names = (String) params.nextElement();
//						if (request.getParameter(names) != null) {
//
//							if (!names.trim().contains("memo")) {
//								continue;
//							}
//
//							String configKey = "ANNOUNCE_FILE";
//							configKey += names.trim().replace("memo", "");
//
//							etcConfigInfo = new EtcConfigInfo();
//
//							etcConfigInfo.setGroupKey("IPCR");
//							etcConfigInfo.setConfigKey(configKey);
//							etcConfigInfo.setConfigValue(request
//									.getParameter(names));
//
//							Integer rst = etcConfigInfoService
//									.setEtcConfigInfo(etcConfigInfo);
//							if (rst == 1) {
//								logInfoService.writeLog(request,
//										"Etc - Announce memo set success",
//										etcConfigInfo.toString(),
//										userInfo.getUserId());
//							} else {
//								jRes.setSuccess(AJaxResVO.SUCCESS_N);
//								jRes.setResult("0");
//								jRes.addAttribute("msg",
//										"Etc - Announce memo set fail");
//
//								logInfoService.writeLog(request,
//										"Etc - Announce memo set fail",
//										etcConfigInfo.toString(),
//										userInfo.getUserId());
//
//								return jRes;
//							}
//						}
//					}
//				} else if (request.getParameter("proc").equals("updateFile")) {
//
//					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//					etcConfigInfo.setGroupKey("UPLOAD");
//					etcConfigInfo.setConfigKey("UPDATE_FILE_PATH");
//					List<EtcConfigInfo> announceFilePathResult = etcConfigInfoService
//							.selectEtcConfigInfo(etcConfigInfo);
//					String announceFilePath = System
//							.getProperty("catalina.home")
//							+ File.separator
//							+ "UpdateFiles";
//					if (announceFilePathResult.size() > 0) {
//						if (announceFilePathResult.get(0).getConfigValue() != null) {
//							announceFilePath = announceFilePathResult.get(0)
//									.getConfigValue();
//						}
//					}
//
//					// MultipartHttpServletRequest 생성
//					MultipartHttpServletRequest mhsr = request;
//					Iterator<String> iter = mhsr.getFileNames();
//
//					MultipartFile mfile = null;
//					String fieldName = "";
//
//					// 값이 나올때까지
//					while (iter.hasNext()) {
//						fieldName = iter.next(); // 내용을 가져와서
//						mfile = mhsr.getFile(fieldName);
//
//						String targetName = mfile.getOriginalFilename();
//
//						boolean rst = FileUploadUtil.fileUploadProc(request,
//								mfile, targetName, announceFilePath);
//
//						if (rst) {
//							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//							jRes.setResult("1");
//							jRes.addAttribute("msg",
//									"Etc - Update File upload success");
//
//							logInfoService.writeLog(request,
//									"Etc - Update File upload success",
//									targetName, userInfo.getUserId());
//						} else {
//							jRes.setSuccess(AJaxResVO.SUCCESS_N);
//							jRes.setResult("0");
//							jRes.addAttribute("msg", "Update File upload fail");
//
//							logInfoService.writeLog(request,
//									"Etc - Update File upload fail",
//									targetName, userInfo.getUserId());
//
//							return jRes;
//						}
//					}
//				} else if (request.getParameter("proc").equals("restart")) {
//					jRes.setSuccess(AJaxResVO.SUCCESS_N);
//					jRes.setResult("0");
//					jRes.addAttribute("msg", "Etc - Restart error");
//
//					logInfoService.writeLog(request, "Etc - Restart error",
//							request.getParameter("proc"), userInfo.getUserId());
//				} else if (request.getParameter("proc")
//						.equals("changeTimeZone")) {
//					if (request.getParameter("serverTimezone") != null
//							&& !request.getParameter("serverTimezone").trim()
//									.isEmpty()) {
//						TimeZone tzone = TimeZone.getTimeZone(request
//								.getParameter("serverTimezone"));
//						TimeZone.setDefault(tzone);
//
//						Calendar calendar = GregorianCalendar
//								.getInstance(TimeZone.getDefault());
//						Date currentDate = calendar.getTime();
//						String currentTime = String.format("%02d:%02d",
//								calendar.get(Calendar.HOUR_OF_DAY),
//								calendar.get(Calendar.MINUTE));
//
//						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//						jRes.setResult("1");
//						jRes.addAttribute("msg", "Etc - TimeZone success");
//						jRes.addAttribute("currentDate", currentDate.toString());
//						jRes.addAttribute("currentTime", currentTime);
//
//						logInfoService.writeLog(request,
//								"Etc - TimeZone success",
//								request.getParameter("proc"),
//								userInfo.getUserId());
//					} else {
//						jRes.setSuccess(AJaxResVO.SUCCESS_N);
//						jRes.setResult("0");
//						jRes.addAttribute("msg", "Etc - TimeZone error");
//
//						logInfoService.writeLog(request,
//								"Etc - TimeZone error",
//								request.getParameter("proc"),
//								userInfo.getUserId());
//
//					}
//				} else {
//					jRes.setSuccess(AJaxResVO.SUCCESS_N);
//					jRes.setResult("0");
//					jRes.addAttribute("msg", "etc proc command fail");
//
//					logInfoService.writeLog(request, "Etc - Proc command fail",
//							request.getParameter("proc"), userInfo.getUserId());
//				}
//			} else {
//				jRes.setSuccess(AJaxResVO.SUCCESS_N);
//				jRes.setResult("0");
//				jRes.addAttribute("msg", "proc fail");
//
//				logInfoService.writeLog(request, "Etc - Abnormal request",
//						null, userInfo.getUserId());
//			}
//		} else {
//			jRes.setSuccess(AJaxResVO.SUCCESS_N);
//			jRes.setResult("0");
//			jRes.addAttribute("msg", "login fail");
//
//			logInfoService.writeLog(request, "Etc - Logout");
//		}
//		return jRes;
//	}

	// Administer - 시스템 관리 - 상세관리 data load
	@RequestMapping(value = "/etc_load.do", method = { RequestMethod.POST,
			RequestMethod.GET }, headers = "Content-Type= multipart/form-data")
	public @ResponseBody AJaxResVO etc_load(
			MultipartHttpServletRequest request, Locale local, Model model)
			throws SocketException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			EtcConfigInfo tmpEtcConfigInfo = new EtcConfigInfo();
			List<EtcConfigInfo> resultList = new ArrayList<EtcConfigInfo>();
			List<EtcConfigInfo> tmpList = new ArrayList<EtcConfigInfo>();
			// select option으로 받는 list (select id, option name, option value)
			List<SelectOptionVO> optionData = new ArrayList<SelectOptionVO>();
			SelectOptionVO tempOptionData = new SelectOptionVO();

			// 메일정보 다 받음
			etcConfigInfo.setGroupKey("smtp");
			resultList = etcConfigInfoService
					.selectEtcConfigInfo(etcConfigInfo);

			// fault 알람 주기
			etcConfigInfo.setGroupKey("xprocess");
			etcConfigInfo.setConfigKey("alert_rate");
			// 적재한다.
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - IPKTS Alive check
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("IPKTS_KEEP_ALIVE_CHECK");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - SIP Alive check
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("SIP_KEEP_ALIVE_CHECK");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - SIP Log
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("SIP_LOG");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - Lan Card (option + selected value)
			Enumeration<NetworkInterface> nets = NetworkInterface
					.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
				if (netint.isLoopback() == false && netint.isUp() == true) {
					String value = netint.getName().toUpperCase();
					String valueElement = netint.getName().toUpperCase() + " "
							+ netint.getDisplayName();
					// 적재
					tempOptionData = new SelectOptionVO();
					tempOptionData.setSelectId("len");
					tempOptionData.setOptionName(value);
					tempOptionData.setOptionValue(value);
					optionData.add(tempOptionData);
				}
			}

			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("LEN");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - 암호화된 파일 다운로드
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("encrypt");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - 에이전트 ID 출력 옵션
			etcConfigInfo.setGroupKey("Search");
			etcConfigInfo.setConfigKey("AgentIdOption");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - 다운로드 파일 확장자
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("FILE_EXTENSION");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// 기타설정 - 안내 멘트 녹취
			etcConfigInfo.setGroupKey("IPCR");
			etcConfigInfo.setConfigKey("ANNOUNCE_RECORDING_YN");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpEtcConfigInfo = new EtcConfigInfo();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			tmpEtcConfigInfo.setGroupKey(tmpList.get(0).getGroupKey());
			tmpEtcConfigInfo.setConfigKey(tmpList.get(0).getConfigKey());
			tmpEtcConfigInfo.setConfigValue(tmpList.get(0).getConfigValue());
			resultList.add(tmpEtcConfigInfo);

			// / 파일명 패턴 (option + select value)
			EtcConfigInfo patternConf = new EtcConfigInfo();
			patternConf.setGroupKey("SYSTEM");
			patternConf.setConfigKey("FILE_PATTERN");
			List<EtcConfigInfo> filePatternResult = etcConfigInfoService
					.selectEtcConfigInfo(patternConf);
			String filePattern = "";
			if (filePatternResult.size() > 0) {
				filePattern = filePatternResult.get(0).getConfigValue();
			}
			String[] filePatternArray = filePattern.split(",");

			for (int i = 0; i < filePatternArray.length; i++) {
				String num_val = filePatternArray[i];
				for (int j = 0; j < 3; j++) {
					// filenameA~C까지 적재
					String val = filePatternArray[j];
					String valueElement = messageSource.getMessage(
							"admin.detail.option.filePattern."
									+ filePatternArray[j], null,
							Locale.getDefault());
					tempOptionData = new SelectOptionVO();
					tempOptionData.setSelectId("filename_" + num_val);
					tempOptionData.setOptionName(valueElement);
					tempOptionData.setOptionValue(val);
					optionData.add(tempOptionData);
				}
			}

			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("FILENAME_PATTERN");
			// 적재한다.
			tmpList = new ArrayList<EtcConfigInfo>();
			tmpList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String filePattern2 = "";
			if (tmpList.size() > 0) {
				filePattern2 = tmpList.get(0).getConfigValue();
			}
			String[] filePatternArray2 = filePattern2.split("-");

			for (int i = 0; i < filePatternArray2.length; i++) {
				String val = filePatternArray[i];
				String patter_val = filePatternArray2[i];
				tmpEtcConfigInfo = new EtcConfigInfo();
				tmpEtcConfigInfo.setGroupKey("SYSTEM");
				tmpEtcConfigInfo.setConfigKey("filename_" + val);
				tmpEtcConfigInfo.setConfigValue(patter_val);
				resultList.add(tmpEtcConfigInfo);
			}

			jRes.addAttribute("resultList", resultList);
			jRes.addAttribute("optionData", optionData);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");
		}
		return jRes;
	}

	// 사용자 관리 _ 트리
	@RequestMapping(value = "/getTreeList.do", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "text/plain;charset=utf8")
	public @ResponseBody AJaxResVO getTreeList(HttpServletRequest request) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			OrganizationInfo organizationInfo = new OrganizationInfo();
			RUserInfo rUserInfo = new RUserInfo();
			List<OrganizationInfo> mgList = null;
			List<OrganizationInfo> sgList = null;
			List<RUserInfo> userList = null;

			organizationInfo.setrBgCode("1000");
			List<OrganizationInfo> bgList = organizationInfoService
					.selectOrganizationBgInfo(organizationInfo);

			if (!StringUtil.isNull(request.getParameter("bgCode"))) {
				organizationInfo.setrBgCode(request.getParameter("bgCode"));
				mgList = organizationInfoService
						.selectOrganizationMgInfo(organizationInfo);
			}

			if (!StringUtil.isNull(request.getParameter("mgCode"))) {
				organizationInfo.setrMgCode(request.getParameter("mgCode"));
				sgList = organizationInfoService
						.selectOrganizationSgInfo(organizationInfo);
			}

			if (!StringUtil.isNull(request.getParameter("sgCode"))
					&& !"Y".equals(request.getParameter("aUser"))) {
				rUserInfo.setSgCode(request.getParameter("sgCode"));
				userList = ruserInfoService.adminUserManageSelect(rUserInfo);
			}

			if (!StringUtil.isNull(request.getParameter("sgCode"))
					&& "Y".equals(request.getParameter("aUser"))) {
				rUserInfo.setSgCode(request.getParameter("sgCode"));
				userList = ruserInfoService
						.adminAUserManageSelectTree(rUserInfo);
			}

			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("bgList", bgList);
			jRes.addAttribute("mgList", mgList);
			jRes.addAttribute("sgList", sgList);
			jRes.addAttribute("userList", userList);

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");
		}
		return jRes;
	}

	// 사용자 관리 - 사용자 관리
	@RequestMapping(value = "/group_proc.do")
	public @ResponseBody AJaxResVO group_proc(HttpServletRequest request,
			Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();

		jRes.setSuccess(AJaxResVO.SUCCESS_N);
		jRes.setResult("0");
		jRes.addAttribute("msg", "group proc fail");

		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			String bgList = "";
			if (userInfo != null) {

				OrganizationInfo groupInfo = new OrganizationInfo();
				Integer rst = 0;
				
				EtcConfigInfo BgCodeList = new EtcConfigInfo();
				BgCodeList.setGroupKey("CALLCENTER");
				BgCodeList.setConfigKey("CALLCENTER");
				List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(BgCodeList);
				if(result.size()>0) {
					bgList = result.get(0).getConfigValue();
				}				

				if (request.getParameter("proc") != null
						&& !request.getParameter("proc").equals("dragndrop")) {
					if (request.getParameter("classBgCode") != null
							&& !request.getParameter("classBgCode").isEmpty()) {
						groupInfo.setrBgCode(request
								.getParameter("classBgCode"));
					}
					if (request.getParameter("classBgName") != null
							&& !request.getParameter("classBgName").isEmpty()) {
						groupInfo.setrBgName(XssFilterUtil.XssFilter(request.getParameter("classBgName")));
					}
					if (request.getParameter("classMgCode") != null
							&& !request.getParameter("classMgCode").isEmpty()) {
						groupInfo.setrMgCode(request
								.getParameter("classMgCode"));
					}
					if (request.getParameter("classMgName") != null
							&& !request.getParameter("classMgName").isEmpty()) {
						groupInfo.setrMgName(XssFilterUtil.XssFilter(request.getParameter("classMgName")));
					}
					if (request.getParameter("classSgCode") != null
							&& !request.getParameter("classSgCode").isEmpty()) {
						groupInfo.setrSgCode(request
								.getParameter("classSgCode"));
					}
					if (request.getParameter("classSgName") != null
							&& !request.getParameter("classSgName").isEmpty()) {
						groupInfo.setrSgName(XssFilterUtil.XssFilter(request.getParameter("classSgName")));
					}

					String level = request.getParameter("level");
					switch (level) {
					case "1":
						groupInfo.setrTable("BG");
						break;
					case "2":
						groupInfo.setrTable("MG");
						break;
					case "3":
						groupInfo.setrTable("SG");
						break;
					}

					if (request.getParameter("groupUseYN") != null
							&& !request.getParameter("groupUseYN").isEmpty()) {
						groupInfo.setUseYn(request.getParameter("groupUseYN"));
					}
					
					
					if (request.getParameter("proc") != null&& request.getParameter("proc").equals("insert")) {
						
						try {
							rst = organizationInfoService.insertOrganizationInfo(groupInfo);
							
							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							logService.writeLog(request,
									"USER_REC", "INSERT-SUCCESS", groupInfo.toString(),
									userInfo.getUserId());
						} catch (Exception e) {
							rst = 0;
							
							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							logService.writeLog(request,
									"USER_REC", "INSERT-FAIL", groupInfo.toString(),
									userInfo.getUserId());
						}
						HashMap<String, String> bgCodeMap = organizationInfoService.selectOrganizationLastInfo(groupInfo);
						String addBgCode = (String)bgCodeMap.get("rcode");
						if(addBgCode.indexOf("B") == 0 && request.getParameter("groupType").equals("user_rec")) {
							if(bgList.split(",").length < 1) {
								BgCodeList.setGroupKey("CALLCENTER");
								BgCodeList.setConfigKey("CALLCENTER");
								BgCodeList.setConfigValue(addBgCode);
								etcConfigInfoService.updateEtcConfigInfo(BgCodeList);							
							}else {
								bgList += ","+addBgCode;
								BgCodeList.setGroupKey("CALLCENTER");
								BgCodeList.setConfigKey("CALLCENTER");
								BgCodeList.setConfigValue(bgList);
								etcConfigInfoService.updateEtcConfigInfo(BgCodeList);
							}
						}
						
					} else if (request.getParameter("proc") != null&& request.getParameter("proc").equals("modify")) {
						try {
							organizationInfoService.updateOrganizationInfo(groupInfo);
							rst = 1;
							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							
							logService.writeLog(request,
							"USER_REC", "UPDATE-SUCCESS", groupInfo.toString(),
							userInfo.getUserId());
						} catch (NullPointerException e) {
							rst = 0;

							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							logService.writeLog(request,"USER_REC", "UPDATE-FAIL",groupInfo.toString(), userInfo.getUserId());
						} catch (Exception e) {
							rst = 0;

							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							logService.writeLog(request,
							"USER_REC", "UPDATE-FAIL", groupInfo.toString(),
							userInfo.getUserId());
						}
					} else if (request.getParameter("proc") != null
							&& request.getParameter("proc").equals("delete")) {
						if (level.equals("1") || level.equals("2")|| level.equals("3")) {
							groupInfo.setrTable("SG");
							rst = organizationInfoService.deleteOrganizationInfo(groupInfo);
						}
						if (level.equals("1") || level.equals("2")) {
							groupInfo.setrTable("MG");
							groupInfo.setrSgCode(null);
							rst = organizationInfoService.deleteOrganizationInfo(groupInfo);

						}
						if (level.equals("1")) {
							groupInfo.setrTable("BG");
							groupInfo.setrMgCode(null);
							rst = organizationInfoService.deleteOrganizationInfo(groupInfo);
							if(request.getParameter("groupType").equals("user_rec")) {
								if(bgList.split(",").length < 1) {
									BgCodeList.setGroupKey("CALLCENTER");
									BgCodeList.setConfigKey("CALLCENTER");
									BgCodeList.setConfigValue("");
									etcConfigInfoService.updateEtcConfigInfo(BgCodeList);
								}else {
									String delBgCode = "";
									if(request.getParameter("classBgCode") != null && !request.getParameter("classBgCode").isEmpty()) {
										delBgCode = request.getParameter("classBgCode");
									}
									
									String []bgListArr = bgList.split(",");
									int size = bgListArr.length;
									if(size>0) {
										for(int i=0; i<size; i++) {			
											if(bgListArr[i].equals(delBgCode)) {
												for(int j=i; j<bgListArr.length-1; j++) {
													bgListArr[j]=bgListArr[j+1];					
												}
												size--;
												break;
											}
										}
										bgList = "";
										for(int i=0; i<size; i++) {			
											if((i+1)!=size) {
												bgList += bgListArr[i]+",";
											}else {
												bgList += bgListArr[i];
											}				
										}
									}
									
									BgCodeList.setGroupKey("CALLCENTER");
									BgCodeList.setConfigKey("CALLCENTER");
									BgCodeList.setConfigValue(bgList);
									etcConfigInfoService.updateEtcConfigInfo(BgCodeList);
								}
							}						
						}
						try {
							if(groupInfo.getrBgCode()!= null) {
								List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
								groupInfo.setrBgName(forBgLog.get(0).getrBgName());
							}
							if(groupInfo.getrMgCode()!= null) {
								List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
								groupInfo.setrMgName(forMgLog.get(0).getrMgName());
							}
						} catch (Exception ee) {
							ee.printStackTrace();
						}
						logService.writeLog(request,
								"USER_REC", "DELETE-SUCCESS", groupInfo.toString(),
								userInfo.getUserId());
					}
				} else if (request.getParameter("proc") != null
						&& request.getParameter("proc").equals("dragndrop")) {
					String[] tBgCodeList = null, tMgCodeList = null, tSgCodeList = null;
					String[] tBgNameList = null, tMgNameList = null, tSgNameList = null;

					if (request.getParameter("tBgCode") != null
							&& !request.getParameter("tBgCode").isEmpty()) {
						tBgCodeList = request.getParameter("tBgCode").split(
								"\\,");
					}
					if (request.getParameter("tBgName") != null
							&& !request.getParameter("tBgName").isEmpty()) {
						tBgNameList = request.getParameter("tBgName").split(
								"\\,");
					}
					if (request.getParameter("tMgCode") != null
							&& !request.getParameter("tMgCode").isEmpty()) {
						tMgCodeList = request.getParameter("tMgCode").split(
								"\\,");
					}
					if (request.getParameter("tMgName") != null
							&& !request.getParameter("tMgName").isEmpty()) {
						tMgNameList = request.getParameter("tMgName").split(
								"\\,");
					}
					if (request.getParameter("tSgCode") != null
							&& !request.getParameter("tSgCode").isEmpty()) {
						tSgCodeList = request.getParameter("tSgCode").split(
								"\\,");
					}
					if (request.getParameter("tSgName") != null
							&& !request.getParameter("tSgName").isEmpty()) {
						tSgNameList = request.getParameter("tSgName").split(
								"\\,");
					}

					String[] oBgCodeList = null, oMgCodeList = null, oSgCodeList = null;
					String[] oBgNameList = null, oMgNameList = null, oSgNameList = null;

					Map<String, OrganizationInfo> oBgMap = new HashMap<String, OrganizationInfo>();
					Map<String, OrganizationInfo> oMgMap = new HashMap<String, OrganizationInfo>();
					Map<String, OrganizationInfo> oSgMap = new HashMap<String, OrganizationInfo>();

					OrganizationInfo originInfo = new OrganizationInfo();
					if (request.getParameter("oSgCode") != null
							&& !request.getParameter("oSgCode").isEmpty()) {
						oSgCodeList = request.getParameter("oSgCode").split(
								"\\,");
						oSgNameList = request.getParameter("oSgName").split(
								"\\,");

						for (int i = 0; i < oSgCodeList.length; i++) {
							originInfo.setrTable("SG");
							originInfo.setrSgCode(oSgCodeList[i]);

							List<OrganizationInfo> tempInfo = organizationInfoService
									.selectOrganizationSgInfo(originInfo);
							if (tempInfo.size() > 0) {
								oSgMap.put(oSgNameList[i], tempInfo.get(0));
							}
						}
					}
					originInfo = new OrganizationInfo();
					if (request.getParameter("oMgCode") != null
							&& !request.getParameter("oMgCode").isEmpty()) {
						oMgCodeList = request.getParameter("oMgCode").split(
								"\\,");
						oMgNameList = request.getParameter("oMgName").split(
								"\\,");

						for (int i = 0; i < oMgCodeList.length; i++) {
							originInfo.setrTable("MG");
							originInfo.setrMgCode(oMgCodeList[i]);

							List<OrganizationInfo> tempInfo = organizationInfoService
									.selectOrganizationMgInfo(originInfo);
							if (tempInfo.size() > 0) {
								oMgMap.put(oMgNameList[i], tempInfo.get(0));
							}
						}
					}
					originInfo = new OrganizationInfo();
					if (request.getParameter("oBgCode") != null
							&& !request.getParameter("oBgCode").isEmpty()) {
						oBgCodeList = request.getParameter("oBgCode").split(
								"\\,");
						oBgNameList = request.getParameter("oBgName").split(
								"\\,");

						for (int i = 0; i < oBgCodeList.length; i++) {
							originInfo.setrTable("BG");
							originInfo.setrMgCode(oMgCodeList[i]);

							List<OrganizationInfo> tempInfo = organizationInfoService
									.selectOrganizationBgInfo(originInfo);
							if (tempInfo.size() > 0) {
								oBgMap.put(oBgNameList[i], tempInfo.get(0));
							}
						}
					}

					Map<String, OrganizationInfo> tBgMap = new HashMap<String, OrganizationInfo>();
					Map<String, OrganizationInfo> tMgMap = new HashMap<String, OrganizationInfo>();
					Map<String, OrganizationInfo> tSgMap = new HashMap<String, OrganizationInfo>();
					if (tBgNameList!=null) {
						for (int b = 0; b < tBgNameList.length; b++) {
							if (tBgCodeList != null && tBgCodeList[b] != null) {
								groupInfo.setrBgCode(tBgCodeList[b]);
							} else {
								groupInfo.setrBgName(tBgNameList[b]);
								groupInfo.setrTable("BG");

								rst = organizationInfoService
										.insertOrganizationInfo(groupInfo);
								try {
									if(groupInfo.getrBgCode()!= null) {
										List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
										groupInfo.setrBgName(forBgLog.get(0).getrBgName());
									}
									if(groupInfo.getrMgCode()!= null) {
										List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
										groupInfo.setrMgName(forMgLog.get(0).getrMgName());
									}
								} catch (Exception ee) {
									ee.printStackTrace();
								}
								
								logService.writeLog(request,
										"USER_REC", "INSERT-SUCCESS",
										groupInfo.toString(),
										userInfo.getUserId());

								HashMap<String, String> lastInfo = organizationInfoService
										.selectOrganizationLastInfo(groupInfo);
								groupInfo.setrBgCode(lastInfo.get("rcode"));

								tBgMap.put(tBgNameList[b], groupInfo);
							}

							if (tMgNameList != null) {
								for (int m = 0; m < tMgNameList.length; m++) {
									if (tMgCodeList != null
											&& tMgCodeList[m] != null) {
										groupInfo.setrMgCode(tMgCodeList[m]);
									} else {
										groupInfo.setrBgName(null);
										groupInfo.setrMgCode(null);
										groupInfo.setrMgName(tMgNameList[m]);
										groupInfo.setrTable("MG");
										
										HashMap<String, Object> map = new HashMap<String, Object>();
										rst = organizationInfoService
												.insertOrganizationInfo(groupInfo);
										try {
											if(groupInfo.getrBgCode()!= null) {
												List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
												groupInfo.setrBgName(forBgLog.get(0).getrBgName());
											}
											if(groupInfo.getrMgCode()!= null) {
												List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
												groupInfo.setrMgName(forMgLog.get(0).getrMgName());
											}
										} catch (Exception ee) {
											ee.printStackTrace();
										}
										
										logService.writeLog(request,
												"USER_REC", "INSERT-SUCCESS",
												groupInfo.toString(),
												userInfo.getUserId());

										HashMap<String, String> lastInfo = organizationInfoService
												.selectOrganizationLastInfo(groupInfo);
										groupInfo.setrMgCode(lastInfo.get("rcode"));

										tMgMap.put(tMgNameList[m], groupInfo);
									}

									if (tSgNameList != null) {
										for (int s = 0; s < tSgNameList.length; s++) {
											groupInfo.setrBgName(null);
											groupInfo.setrMgName(null);
											groupInfo.setrSgCode(null);
											groupInfo.setrSgName(tSgNameList[s]);
											groupInfo.setrTable("SG");
											
											HashMap<String, Object> map = new HashMap<String, Object>();
											rst = organizationInfoService
													.insertOrganizationInfo(groupInfo);
											try {
												if(groupInfo.getrBgCode()!= null) {
													List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
													groupInfo.setrBgName(forBgLog.get(0).getrBgName());
												}
												if(groupInfo.getrMgCode()!= null) {
													List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
													groupInfo.setrMgName(forMgLog.get(0).getrMgName());
												}
											} catch (Exception ee) {
												ee.printStackTrace();
											}
											logService.writeLog(request,
													"USER_REC", "INSERT-SUCCESS",
													groupInfo.toString(),
													userInfo.getUserId());

											HashMap<String, String> lastInfo = organizationInfoService
													.selectOrganizationLastInfo(groupInfo);
											groupInfo.setrSgCode(lastInfo
													.get("rcode"));

											tSgMap.put(tSgNameList[s], groupInfo);
										}
									}
								}
							}
						}
					

						groupInfo = new OrganizationInfo();

						if (request.getParameter("sBgCode") != null
								&& !request.getParameter("sBgCode").isEmpty()) {
							groupInfo.setrBgCode(request
									.getParameter("sBgCode"));
						}
						if (request.getParameter("sMgCode") != null
								&& !request.getParameter("sMgCode").isEmpty()) {
							groupInfo.setrMgCode(request
									.getParameter("sMgCode"));
						}
						if (request.getParameter("sSgCode") != null
								&& !request.getParameter("sSgCode").isEmpty()) {
							groupInfo.setrSgCode(request
									.getParameter("sSgCode"));
						}
						if (request.getParameter("sBgCode") != null
								&& !request.getParameter("sBgCode").isEmpty()) {
							groupInfo.setrTable("SG");
							rst = organizationInfoService
									.deleteOrganizationInfo(groupInfo);

							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							logService.writeLog(request,
									"USER_REC", "DELETE-SUCCESS",
									groupInfo.toString(),
									userInfo.getUserId());
						}
						if (request.getParameter("sBgCode") != null
								&& !request.getParameter("sBgCode").isEmpty()
								&& request.getParameter("sMgCode") != null
								&& !request.getParameter("sMgCode").isEmpty()
								&& (request.getParameter("sSgCode") == null || request
										.getParameter("sSgCode").isEmpty())) {
							groupInfo.setrTable("MG");
							groupInfo.setrSgCode(null);
							rst = organizationInfoService
									.deleteOrganizationInfo(groupInfo);

							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							logService.writeLog(request,
									"USER_REC", "DELETE-SUCCESS",
									groupInfo.toString(),
									userInfo.getUserId());
						}
						if (request.getParameter("sBgCode") != null
								&& !request.getParameter("sBgCode").isEmpty()
								&& (request.getParameter("sMgCode") == null || request
										.getParameter("sMgCode").isEmpty())
								&& (request.getParameter("sSgCode") == null || request
										.getParameter("sSgCode").isEmpty())) {
							groupInfo.setrTable("BG");
							groupInfo.setrSgCode(null);
							groupInfo.setrMgCode(null);
							rst = organizationInfoService
									.deleteOrganizationInfo(groupInfo);

							try {
								if(groupInfo.getrBgCode()!= null) {
									List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
									groupInfo.setrBgName(forBgLog.get(0).getrBgName());
								}
								if(groupInfo.getrMgCode()!= null) {
									List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
									groupInfo.setrMgName(forMgLog.get(0).getrMgName());
								}
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							logService.writeLog(request,
									"USER_REC", "DELETE-SUCCESS",
									groupInfo.toString(),
									userInfo.getUserId());
						}
					}
					if (oSgMap != null) {
						Set<Entry<String, OrganizationInfo>> set = oSgMap
								.entrySet();
						Iterator<Entry<String, OrganizationInfo>> it = set
								.iterator();
						while (it.hasNext()) {
							Map.Entry<String, OrganizationInfo> e = it.next();

							OrganizationInfo userGroupInfo = new OrganizationInfo();

							OrganizationInfo originGroupInfo = e.getValue();
							OrganizationInfo modifyGroupInfo = new OrganizationInfo();

							if (tSgMap.get(e.getKey()) != null) {
								modifyGroupInfo = tSgMap.get(e.getKey());
							} else if (tMgMap.get(e.getKey()) != null) {
								modifyGroupInfo = tMgMap.get(e.getKey());
							} else if (tBgMap.get(e.getKey()) != null) {
								modifyGroupInfo = tBgMap.get(e.getKey());
							}

							if (modifyGroupInfo != null) {
								if (originGroupInfo.getrBgCode() != null) {
									userGroupInfo.setoBgCode(originGroupInfo
											.getrBgCode());
								}
								if (originGroupInfo.getrMgCode() != null) {
									userGroupInfo.setoMgCode(originGroupInfo
											.getrMgCode());
								}
								if (originGroupInfo.getrSgCode() != null) {
									userGroupInfo.setoSgCode(originGroupInfo
											.getrSgCode());
								}

								if (originGroupInfo.getrBgCode() != null) {
									userGroupInfo.setrBgCode(modifyGroupInfo
											.getrBgCode());
								}
								if (originGroupInfo.getrMgCode() != null) {
									userGroupInfo.setrMgCode(modifyGroupInfo
											.getrMgCode());
								}
								if (originGroupInfo.getrSgCode() != null) {
									userGroupInfo.setrSgCode(modifyGroupInfo
											.getrSgCode());
								}
								organizationInfoService
										.updateGroupCodeInfo(userGroupInfo);

								try {
									if(groupInfo.getrBgCode()!= null) {
										List<OrganizationInfo> forBgLog = organizationInfoService.selectOrganizationBgInfo(groupInfo);
										groupInfo.setrBgName(forBgLog.get(0).getrBgName());
									}
									if(groupInfo.getrMgCode()!= null) {
										List<OrganizationInfo> forMgLog = organizationInfoService.selectOrganizationMgInfo(groupInfo);
										groupInfo.setrMgName(forMgLog.get(0).getrMgName());
									}
								} catch (Exception ee) {
									ee.printStackTrace();
								}
								logService.writeLog(request,
								"USER_REC", "UPDATE-SUCCESS",
								userGroupInfo.toString(),
								userInfo.getUserId());
								
							}
						}
					}
				}
				if (rst == 1) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("1");
					jRes.addAttribute("msg", "group proc success");

					if (groupInfo.getrTable() != null) {
						HashMap<String, String> lastInfo = organizationInfoService
								.selectOrganizationLastInfo(groupInfo);

						if (lastInfo != null && lastInfo.get("rcode") != null)
							jRes.addAttribute("id", lastInfo.get("rcode"));
						if (lastInfo != null && lastInfo.get("rname") != null)
							jRes.addAttribute("name", lastInfo.get("rname"));
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");

				logService.writeLog(request, "USER_REC", "COMMAND-FAIL", "No Session");
			}
		} catch (Exception e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");

			logInfoService.writeLog(request, "LOG", "EXCEPTION",
			e.getMessage());
		}

		return jRes;
	}

	// 권한 리스트 불러오기
	@RequestMapping(value = "/getAuthyList.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO log_proc(HttpServletRequest request,
			Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();

		if (userInfo != null) {

 			List<MAccessLevelInfo> accessLevelInfoList = accessLevelInfoService
					.selectAccessLevelInfo(accessLevelInfo);

			if (accessLevelInfoList.size() > 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("msg", "authy list get");
				jRes.addAttribute("authyList", accessLevelInfoList);

			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "no data");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
	
	// 권한 숨김 리스트 불러오기
	@RequestMapping(value = "/access_hide.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO access_hide(HttpServletRequest request,Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();

		if (userInfo != null) {

			List<MMenuAccessInfo> accessLevelInfoList = menuAccessInfoService.selectHideMenuAccessInfo();
			
			if (accessLevelInfoList.size() > 0) {
				String hiddenMenu = "";
				
				for(int i=0; i<accessLevelInfoList.size();i++) {
					if(i+1==accessLevelInfoList.size())
						hiddenMenu+=accessLevelInfoList.get(i).getProgramCode();
					else
						hiddenMenu+=accessLevelInfoList.get(i).getProgramCode()+",";
				}
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("hiddenMenu", hiddenMenu);

			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "no data");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
	
	// 권한 숨김 리스트 불러오기
	@RequestMapping(value = "/access_recdate_search.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO access_recdate_search(HttpServletRequest request,Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {

			String type = request.getParameter("type");
			String level = request.getParameter("numberlevel");
			
			if("select".equals(type)) {
				MMenuAccessInfo menuAccessInfo = new MMenuAccessInfo();
				
				menuAccessInfo.setLevelCode(level);
				menuAccessInfo.setProgramCode("P20003");
				
				List<MMenuAccessInfo> menuAccessInfosResult =  menuAccessInfoService.checkAccessInfo(menuAccessInfo);
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("resultValue", menuAccessInfosResult.get(0).getTopPriority());
				
			}else if("update".equals(type)) {
				String value = request.getParameter("dateValue");
				
				MMenuAccessInfo menuAccessInfo = new MMenuAccessInfo();
				
				menuAccessInfo.setLevelCode(level);
				menuAccessInfo.setProgramCode("P20003");
				menuAccessInfo.setTopPriority(Integer.parseInt(value));
				
				menuAccessInfoService.updatecAccessInfo(menuAccessInfo);
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("resultValue", "1");
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("resultValue", "1");
			}						
						
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}

	// 권한 관리 - 권한 관리
	@RequestMapping(value = "/access_proc.do")
	public @ResponseBody AJaxResVO access_proc(HttpServletRequest request,
			Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();

		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				if (request.getParameter("proc") != null) {
					if (request.getParameter("proc").equals("change") && request.getParameter("menuCode") != null && request.getParameter("levelCode") != null && request.getParameter("acesIdx") != null && request.getParameter("acesSta") != null) {

						MMenuAccessInfo menuAccessInfo = new MMenuAccessInfo();
						menuAccessInfo.setProgramCode(request.getParameter("menuCode"));
						menuAccessInfo.setLevelCode(request.getParameter("levelCode"));
						menuAccessInfo.setGetLevelName("Y");
						List<MMenuAccessInfo> checkAccessInfoResult = menuAccessInfoService.checkAccessInfo(menuAccessInfo);
						if (checkAccessInfoResult.size() > 0) {
							menuAccessInfo.setProgramName(checkAccessInfoResult.get(0).getProgramName());
							menuAccessInfo.setLevelName(checkAccessInfoResult.get(0).getLevelName());
							String State = "Y";
							if (!request.getParameter("acesSta").equals("true"))
								State = "N";
							switch (request.getParameter("acesIdx")) {
							case "2":
								menuAccessInfo.setAccessLevel(request.getParameter("acesSta"));
								break;
							case "3":
								menuAccessInfo.setReadYn(State);
								break;
							case "4":
								menuAccessInfo.setWriteYn(State);
								break;
							case "5":
								menuAccessInfo.setModiYn(State);
								break;
							case "6":
								menuAccessInfo.setDelYn(State);
								break;
							case "7":
								menuAccessInfo.setListenYn(State);
								break;
							case "8":
								menuAccessInfo.setDownloadYn(State);
								break;
							case "9":
								menuAccessInfo.setExcelYn(State);
								break;
							case "10":
								menuAccessInfo.setMaskingYn(State);
								break;
							case "11":
								menuAccessInfo.setPrereciptYn(State);
								break;
							case "12":
								menuAccessInfo.setReciptYn(State);
								break;
							case "13":
								menuAccessInfo.setApproveYn(State);
								break;
							case "14":
								menuAccessInfo.setDownloadApprove(State);
								break;
							case "15":
								menuAccessInfo.setEncYn(State);
								break;
							case "16":
								menuAccessInfo.setUploadYn(State);
								break;
							case "17":
								menuAccessInfo.setBestcallYn(State);
								break;
							default:
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "column fail");

								logInfoService.writeLog(request,
										"Authy - State change no column",
										request.getParameter("acesIdx"),
										userInfo.getUserId());
								return jRes;
							}

							if("allowableAdd".equals(request.getParameter("acesSta"))) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "allowableAdd");
							} else {
								Integer updateAccessInfoResult = menuAccessInfoService.updateMenuAccessInfo(menuAccessInfo);
								if (updateAccessInfoResult >= 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "change success");
									logService.writeLog(request, "AUTHORITY", "CHANGE-SUCCESS", menuAccessInfo.toString(messageSource));
									// logInfoService.writeLog(request,
									// "Authy - State change success",
									// menuAccessInfo.toString(),
									// userInfo.getUserId());
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "change fail");
									logService.writeLog(request, "AUTHORITY", "CHANGE-FAIL", menuAccessInfo.toString(messageSource));
									// logInfoService.writeLog(request,
									// "Authy - State change fail",
									// menuAccessInfo.toString(),
									// userInfo.getUserId());
								}
							}
							
						} else {
							String State = "N";
							menuAccessInfo.setAccessLevel("A");
							menuAccessInfo.setReadYn(State);
							menuAccessInfo.setWriteYn(State);
							menuAccessInfo.setModiYn(State);
							menuAccessInfo.setDelYn(State);
							menuAccessInfo.setListenYn(State);
							menuAccessInfo.setDownloadYn(State);
							menuAccessInfo.setExcelYn(State);
							menuAccessInfo.setMaskingYn(State);
							menuAccessInfo.setPrereciptYn(State);
							menuAccessInfo.setReciptYn(State);
							menuAccessInfo.setApproveYn(State);
							menuAccessInfo.setDownloadApprove(State);
							menuAccessInfo.setEncYn(State);
							menuAccessInfo.setUploadYn(State);
							menuAccessInfo.setBestcallYn(State);

							State = "Y";
							if (!request.getParameter("acesSta").equals("true"))
								State = "N";
							switch (request.getParameter("acesIdx")) {
							case "2":
								menuAccessInfo.setAccessLevel(request
										.getParameter("acesSta"));
								break;
							case "3":
								menuAccessInfo.setReadYn(State);
								break;
							case "4":
								menuAccessInfo.setWriteYn(State);
								break;
							case "5":
								menuAccessInfo.setModiYn(State);
								break;
							case "6":
								menuAccessInfo.setDelYn(State);
								break;
							case "7":
								menuAccessInfo.setListenYn(State);
								break;
							case "8":
								menuAccessInfo.setDownloadYn(State);
								break;
							case "9":
								menuAccessInfo.setExcelYn(State);
								break;
							case "10":
								menuAccessInfo.setMaskingYn(State);
								break;
							case "11":
								menuAccessInfo.setPrereciptYn(State);
								break;
							case "12":
								menuAccessInfo.setReciptYn(State);
								break;
							case "13":
								menuAccessInfo.setApproveYn(State);
								break;
							case "14":
								menuAccessInfo.setDownloadApprove(State);
								break;
							case "15":
								menuAccessInfo.setEncYn(State);
								break;
							case "16":
								menuAccessInfo.setUploadYn(State);
								break;
							case "17":
								menuAccessInfo.setBestcallYn(State);
								break;
							default:
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "column fail");

								logInfoService.writeLog(request,
										"Authy - State change no column",
										request.getParameter("acesIdx"),
										userInfo.getUserId());
								return jRes;
							}

							if("allowableAdd".equals(request.getParameter("acesSta"))) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "allowableAdd");
							} else {
								Integer insertAccessInfoResult = menuAccessInfoService.insertMenuAccessInfo(menuAccessInfo);
								if (insertAccessInfoResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "change success");
									logService.writeLog(request, "AUTHORITY", "CHANGE-SUCCESS", menuAccessInfo.toString(messageSource));
									// logInfoService.writeLog(request,
									// "Authy - State change success",
									// menuAccessInfo.toString(),
									// userInfo.getUserId());
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "change fail");
									logService.writeLog(request, "AUTHORITY", "CHANGE-FAIL", menuAccessInfo.toString(messageSource));
									// logInfoService.writeLog(request,
									// "Authy - State change fail",
									// menuAccessInfo.toString(),
									// userInfo.getUserId());
								}
							}
							
						}
					} else if (request.getParameter("proc").equals("insert") && request.getParameter("levelName") != null) {
						String levelCode = "";
						String levelName = request.getParameter("levelName");
						MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
						accessLevelInfo.setLevelName(levelName);

						MAccessLevelInfo checkAccessLevelInfoResult = accessLevelInfoService.checkAccessLevelInfo(accessLevelInfo);

						if (checkAccessLevelInfoResult.getDupCheck() < 1) {
							String prefixLevelCode = checkAccessLevelInfoResult
									.getMaxLevelCode().substring(0, 1);
							String newLevelCode = String.valueOf(Integer.parseInt(checkAccessLevelInfoResult.getMaxLevelCode().substring(1, 5)) + 1);

							levelCode = prefixLevelCode + newLevelCode;

							accessLevelInfo.setLevelCode(levelCode);
							accessLevelInfo.setLevelNote(levelName);
							accessLevelInfo.setLockCount(0);
							accessLevelInfo.setAccountDate(0);

							Integer insertAccessLevelInfoResult = accessLevelInfoService.insertAccessLevelInfo(accessLevelInfo);
							if (insertAccessLevelInfoResult != 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "name level info insert fail");
								logService.writeLog(request, "AUTHORITY", "CREATE-FAIL", accessLevelInfo.toString(messageSource));
								// logInfoService.writeLog(request,
								// "Authy - Level name insert fail",
								// accessLevelInfo.toString(messageSource),
								// userInfo.getUserId());
								return jRes;
							} else {
								logService.writeLog(request, "AUTHORITY", "CREATE-SUCCESS", accessLevelInfo.toString(messageSource));
								// logInfoService.writeLog(request,
								// "Authy - Level name insert success",
								// accessLevelInfo.toString(messageSource),
								// userInfo.getUserId());
							}
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "name duplication fail");
							logService.writeLog(request, "AUTHORITY", "CREATE-FAIL", messageSource.getMessage("log.etc.detail", null,Locale.getDefault())+" ["+messageSource.getMessage("log.etc.dupAuthy", null,Locale.getDefault())+"]");
							// logInfoService.writeLog(request,
							// "Authy - Level name duplication fail",
							// accessLevelInfo.toString(messageSource),
							// userInfo.getUserId());
							return jRes;
						}

						accessLevelInfo = new MAccessLevelInfo();
						accessLevelInfo.setLevelName(levelName);

						checkAccessLevelInfoResult = accessLevelInfoService.checkAccessLevelInfo(accessLevelInfo);

						String newLevelCode = checkAccessLevelInfoResult
								.getMaxLevelCode();

						MMenuAccessInfo menuAccessInfo = new MMenuAccessInfo();
						menuAccessInfo.setLevelCode(newLevelCode);

						Integer errorCount = 0;
						
						try {
							Integer insertAccessInfoResult = menuAccessInfoService.insertMenuAccessInfo(menuAccessInfo);
							if (insertAccessInfoResult == 0) {
								errorCount++;
							}
						} catch (NullPointerException e) {
							errorCount++;
							e.printStackTrace();
						} catch (Exception e) {
							errorCount++;
							e.printStackTrace();
						}
						
						if (errorCount > 0) {
							try {
								menuAccessInfo = new MMenuAccessInfo();
								menuAccessInfo.setLevelCode(newLevelCode);
								menuAccessInfoService.deleteMenuAccessInfo(menuAccessInfo);
							} catch (NullPointerException e) {
								logger.error("", e);
							} catch (Exception e) {
								logger.error("", e);
							}
							try {
								accessLevelInfo = new MAccessLevelInfo();
								accessLevelInfo.setLevelCode(newLevelCode);
								accessLevelInfoService.deleteAccessLevelInfo(accessLevelInfo);
							} catch (NullPointerException e) {
								logger.error("", e);
							} catch (Exception e) {
								logger.error("", e);
							}
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "insert fail");
							logService.writeLog(request, "AUTHORITY", "CREATE-FAIL", accessLevelInfo.toString(messageSource));
							// logInfoService.writeLog(request,
							// "Authy - Insert fail",
							// accessLevelInfo.toString(messageSource),
							// userInfo.getUserId());
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "insert success");
							jRes.addAttribute("newLevel", newLevelCode);
							// logService.writeLog(request, "AUTHORITY",
							// "CREATE-SUCCESS", accessLevelInfo.toString(messageSource));
							// logInfoService.writeLog(request,
							// "Authy - Insert success",
							// accessLevelInfo.toString(messageSource),
							// userInfo.getUserId());
						}
					} else if (request.getParameter("proc").equals("modify") && request.getParameter("levelName") != null && request.getParameter("levelCode") != null) {
						MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
						accessLevelInfo.setLevelName(request.getParameter("levelName"));
						accessLevelInfo.setLevelCode(request.getParameter("levelCode"));
						accessLevelInfo.setLockCount(0);
						accessLevelInfo.setAccountDate(0);
						
						MAccessLevelInfo checkAccessLevelInfoResult = accessLevelInfoService.checkAccessLevelInfo(accessLevelInfo);
						if (checkAccessLevelInfoResult.getDupCheck() < 1) {
							Integer updateResult = accessLevelInfoService.updateAccessLevelInfo(accessLevelInfo);
							if (updateResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "update success");
								logService.writeLog(request, "AUTHORITY", "CHANGE-SUCCESS", accessLevelInfo.toString(messageSource));
								// logInfoService.writeLog(request,
								// "Authy - Update fail",
								// accessLevelInfo.toString(messageSource),
								// userInfo.getUserId());
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "update fail");
								logService.writeLog(request, "AUTHORITY", "CHANGE-FAIL", accessLevelInfo.toString(messageSource));
								// logInfoService.writeLog(request,
								// "Authy - Update fail",
								// accessLevelInfo.toString(messageSource),
								// userInfo.getUserId());
							}
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "name duplication fail");
							logService.writeLog(request, "AUTHORITY", "CHANGE-FAIL", messageSource.getMessage("log.etc.detail", null,Locale.getDefault())+" ["+messageSource.getMessage("log.etc.dupAuthy", null,Locale.getDefault())+"]");
							// logInfoService.writeLog(request,
							// "Authy - Level name duplication fail",
							// accessLevelInfo.toString(messageSource),
							// userInfo.getUserId());
							return jRes;
						}
							
							
						
						

					} else if (request.getParameter("proc").equals("delete")
							&& request.getParameter("levelCode") != null) {

						if (!"E1001".equals(request.getParameter("levelCode"))) {
							MMenuAccessInfo menuAccessInfo = new MMenuAccessInfo();
							menuAccessInfo.setLevelCode(request
									.getParameter("levelCode"));
							menuAccessInfoService
									.deleteMenuAccessInfo(menuAccessInfo);

							MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
							accessLevelInfo.setLevelCode(request
									.getParameter("levelCode"));
							accessLevelInfoService
									.deleteAccessLevelInfo(accessLevelInfo);

							RUserInfo ruserInfo = new RUserInfo();
							ruserInfo.setUserLevel(request
									.getParameter("levelCode"));
							ruserInfoService.userLevelEmpty(ruserInfo);

							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "delete success");

							// logInfoService.writeLog(request,
							// "Authy - Delete menu access info success",
							// menuAccessInfo.toString(), userInfo.getUserId());
							// logInfoService.writeLog(request,
							// "Authy - Delete access level info success",
							// accessLevelInfo.toString(messageSource),
							// userInfo.getUserId());
							// logInfoService.writeLog(request,
							// "Authy - Delete user success",
							// ruserInfo.toString(messageSource), userInfo.getUserId());
							logService.writeLog(request, "AUTHORITY","DELETE-SUCCESS",accessLevelInfo.toString(messageSource));

						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg","admin authy is can not deleted");
						}
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "command fail");

						logInfoService.writeLog(request,"Authy - Abnormal request",request.getParameter("proc"),userInfo.getUserId());
					}

					MMenuAccessInfo menuAccess = new MMenuAccessInfo();
					menuAccess.setLevelCode(userInfo.getUserLevel());
					menuAccess.setDisplayLevel(100);

					List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccess);
					int menuAccessListTotal = menuAccessList.size();
					if (menuAccessListTotal > 0) {
						SessionManager.setAttribute(request, "AccessInfo", menuAccessList);
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "command fail");

					logInfoService.writeLog(request, "Authy - Proc is null", null, userInfo.getUserId());
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");

				logInfoService.writeLog(request, "Authy - Logout");
			}
		} catch (NullPointerException e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			logInfoService.writeLog(request, "Authy - Proc exception", e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			logInfoService.writeLog(request, "Authy - Proc exception", e.getMessage());
		}
		return jRes;
	}

	// 허용 범위 불러오기
	@RequestMapping(value = "/getAllowableList.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO getAllowableList(HttpServletRequest request,	Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();

		if (userInfo != null) {

			List<MAccessLevelInfo> allowableListInfoList = accessLevelInfoService.selectAllowableInfo(accessLevelInfo);

			if (allowableListInfoList.size() > 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("msg", "authy list get");
				jRes.addAttribute("allowableList", allowableListInfoList);
				// 0인경우에도 들어가도록...
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "no data");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
	
	// 허용 범위 관리
	@RequestMapping(value = "/allowable_proc.do")
	public @ResponseBody AJaxResVO allowable_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();

		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				if (request.getParameter("proc") != null) {
					if (request.getParameter("proc").equals("insert") && request.getParameter("levelName") != null) {
						String levelCode = "";
						String levelName = request.getParameter("levelName");
						MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
						accessLevelInfo.setLevelName(levelName);

						MAccessLevelInfo checkAllowableInfoResult = accessLevelInfoService.checkAllowableInfo(accessLevelInfo);
						if(checkAllowableInfoResult.getAllowableCount()!=0) {
							if (checkAllowableInfoResult.getDupCheck() < 1) {
								
								String prefixLevelCode = checkAllowableInfoResult.getMaxLevelCode().substring(0, 1);
								String newLevelCode = String.valueOf(Integer.parseInt(checkAllowableInfoResult.getMaxLevelCode().substring(1, 5)) + 1);

								levelCode = prefixLevelCode + newLevelCode;
	
								accessLevelInfo.setLevelCode(levelCode);
								accessLevelInfo.setLevelName(levelName);
	
								Integer insertAccessLevelInfoResult = accessLevelInfoService.insertAllowableInfo(accessLevelInfo);
								if (insertAccessLevelInfoResult != 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "name level info insert fail");
									logService.writeLog(request, "ALLOWABLE_RANGE", "CREATE-FAIL", accessLevelInfo.toString(messageSource));
									return jRes;
								} else {
									logService.writeLog(request, "ALLOWABLE_RANGE",	"CREATE-SUCCESS", accessLevelInfo.toString(messageSource));
								}
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "name duplication fail");
								logService.writeLog(request, "ALLOWABLE_RANGE",	"CREATE-FAIL", messageSource.getMessage("log.etc.detail", null,Locale.getDefault())+" ["+messageSource.getMessage("log.etc.dupAuthy", null,Locale.getDefault())+"]");
								return jRes;
							}
						} else {
							levelCode = "R1001";
							accessLevelInfo.setLevelCode(levelCode);
							accessLevelInfo.setLevelName(levelName);

							Integer insertAccessLevelInfoResult = accessLevelInfoService.insertAllowableInfo(accessLevelInfo);
							if (insertAccessLevelInfoResult != 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "name level info insert fail");
								logService.writeLog(request, "ALLOWABLE_RANGE", "CREATE-FAIL", accessLevelInfo.toString(messageSource));
								return jRes;
							} else {
								logService.writeLog(request, "ALLOWABLE_RANGE", "CREATE-SUCCESS", accessLevelInfo.toString(messageSource));
							}
						}
						
						
						
					} else if (request.getParameter("proc").equals("modify") && request.getParameter("levelName") != null && request.getParameter("levelCode") != null) {
						MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
						accessLevelInfo.setLevelName(request.getParameter("levelName"));
						accessLevelInfo.setLevelCode(request.getParameter("levelCode"));

						MAccessLevelInfo checkAllowableInfoResult = accessLevelInfoService.checkAllowableInfo(accessLevelInfo);
						if (checkAllowableInfoResult.getDupCheck() < 1) {
							
							Integer updateResult = accessLevelInfoService.updateAllowableInfo(accessLevelInfo);
							if (updateResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "update success");
								logService.writeLog(request, "ALLOWABLE_RANGE", "CHANGE-SUCCESS", accessLevelInfo.toString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "update fail");
								logService.writeLog(request, "ALLOWABLE_RANGE",	"CHANGE-FAIL", accessLevelInfo.toString(messageSource));
							}
							
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "name duplication fail");
							logService.writeLog(request, "ALLOWABLE_RANGE",	"CHANGE-FAIL", messageSource.getMessage("log.etc.detail", null,Locale.getDefault())+" ["+messageSource.getMessage("log.etc.dupAuthy", null,Locale.getDefault())+"]");
							return jRes;
						}
							
							
							
						

					} else if (request.getParameter("proc").equals("delete") && request.getParameter("levelCode") != null) {
						AllowableRangeInfo allowableRangeInfo = new AllowableRangeInfo();
						allowableRangeInfo.setrAllowableCode(request.getParameter("levelCode"));
						allowableRangeInfoService.deleteAllowableRangeInfo(allowableRangeInfo);

						MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
						accessLevelInfo.setLevelCode(request.getParameter("levelCode"));
						accessLevelInfoService.deleteAllowableInfo(accessLevelInfo);

						MMenuAccessInfo mMenuAccessInfo = new MMenuAccessInfo();
						mMenuAccessInfo.setAccessLevel(request.getParameter("levelCode"));
						menuAccessInfoService.accessLevelEmpty(mMenuAccessInfo);
						

						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
						jRes.addAttribute("msg", "delete success");

						logService.writeLog(request, "ALLOWABLE_RANGE", "DELETE-SUCCESS", accessLevelInfo.toString(messageSource));

					} else if (request.getParameter("proc").equals("save") && request.getParameter("allowableCode") != null) {
						String listCode = "";
						// 조직 삭제
						if(!StringUtil.isNull(request.getParameter("deleteList"), true)) {
							String [] deleteList = request.getParameter("deleteList").split("/");
							for(int i=0; i<deleteList.length; i++) {

								AllowableRangeInfo allowableRangeInfo = new AllowableRangeInfo();
								allowableRangeInfo.setrAllowableCode(request.getParameter("allowableCode"));
								String [] deleteCode = deleteList[i].split(",");
								if(deleteCode[1].equals("B")) {
									allowableRangeInfo.setrBgCode(deleteCode[0]);
								} else if(deleteCode[1].equals("M")) {
									allowableRangeInfo.setrMgCode(deleteCode[0]);
								} else if(deleteCode[1].equals("S")) {
									allowableRangeInfo.setrSgCode(deleteCode[0]);
								}
								Integer deleteResult = allowableRangeInfoService.deleteAllowableRangeInfo(allowableRangeInfo);
							}
						}
						// 조직 추가
						if(!StringUtil.isNull(request.getParameter("addList"), true)) {
							String [] addList = request.getParameter("addList").split("/");
							for(int i=0; i<addList.length; i++) {

								AllowableRangeInfo allowableRangeInfo = new AllowableRangeInfo();
								allowableRangeInfo.setrAllowableCode(request.getParameter("allowableCode"));
								AllowableRangeInfo checkAllowableRangeInfoResult = allowableRangeInfoService.checkAllowableRangeInfo(allowableRangeInfo);
								if(checkAllowableRangeInfoResult.getListCount()!=0) {
									String prefixListCode = checkAllowableRangeInfoResult.getMaxListCode().substring(0, 1);
									String newListCode = String.valueOf(Integer.parseInt(checkAllowableRangeInfoResult.getMaxListCode().substring(1, 5)) + 1);	
									listCode = prefixListCode + newListCode;
								} else {
									listCode = "W1001";
								}
								
								allowableRangeInfo.setrListCode(listCode);
								
								String [] addGroup = addList[i].split(",");
								if(addGroup.length==3) {
									allowableRangeInfo.setrBgCode(addGroup[0]);
									allowableRangeInfo.setrMgCode(addGroup[1]);
									allowableRangeInfo.setrSgCode(addGroup[2]);
								} else if(addGroup.length==2) {
									allowableRangeInfo.setrBgCode(addGroup[0]);
									allowableRangeInfo.setrMgCode(addGroup[1]);
									allowableRangeInfo.setrSgCode(null);
								} else if(addGroup.length==1) {
									allowableRangeInfo.setrBgCode(addGroup[0]);
									allowableRangeInfo.setrMgCode(null);
									allowableRangeInfo.setrSgCode(null);
								}
								Integer insertResult = allowableRangeInfoService.insertAllowableRangeInfo(allowableRangeInfo);
							}
						}
						
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
						jRes.addAttribute("msg", "delete success");
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "command fail");

						logInfoService.writeLog(request, "ALLOWABLE_RANGE - Abnormal request", request.getParameter("proc"), userInfo.getUserId());
					}

					MMenuAccessInfo menuAccess = new MMenuAccessInfo();
					menuAccess.setLevelCode(userInfo.getUserLevel());
					menuAccess.setDisplayLevel(100);

					List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccess);
					int menuAccessListTotal = menuAccessList.size();
					if (menuAccessListTotal > 0) {
						SessionManager.setAttribute(request, "AccessInfo", menuAccessList);
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "command fail");

					logInfoService.writeLog(request, "ALLOWABLE_RANGE - Proc is null", null, userInfo.getUserId());
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");

				logInfoService.writeLog(request, "ALLOWABLE_RANGE - Logout");
			}
		} catch (NullPointerException e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			logInfoService.writeLog(request, "ALLOWABLE_RANGE - Proc exception",
					e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			logInfoService.writeLog(request, "ALLOWABLE_RANGE - Proc exception",
					e.getMessage());
		}
		return jRes;
	}
	/*
	 * // 시스템 관리 - 시스템 설정 기존 로직 보존위해 우선........주석처리 ㅠㅠ.. 신한생명..하......
	 *
	 * @RequestMapping(value = "/search_customize_proc.do", method =
	 * {RequestMethod.POST,RequestMethod.GET}, produces =
	 * "text/plain;charset=UTF-8") public @ResponseBody AJaxResVO
	 * search_customize_proc(HttpServletRequest request, Locale local, Model
	 * model) { AJaxResVO jRes = new AJaxResVO(); LoginVO userInfo =
	 * SessionManager.getUserInfo(request);
	 *
	 * if(userInfo != null) { if(request.getParameter("proc") != null ) {
	 * if(request.getParameter("proc").equals("apply") &&
	 * request.getParameter("userId") != null && request.getParameter("columns")
	 * != null && request.getParameter("type") != null) {
	 *
	 * String typeString = "list";
	 *
	 * if(request.getParameter("type") != null &&
	 * !request.getParameter("type").isEmpty()) { typeString =
	 * request.getParameter("type"); }
	 *
	 * String[] paramArray = request.getParameter("columns").split("\\|");
	 *
	 * Integer customerInfoResult = 0; String customizeInfoString = ""; if
	 * (typeString.equals("item")) {
	 *
	 * CustomizeItemInfo customizeInfo = new CustomizeItemInfo();
	 *
	 * customizeInfo.setrUserId(request.getParameter("userId"));
	 * customizeInfo.setAllItem(paramArray);
	 *
	 * customizeInfoService.deleteCustomizeItemInfo(customizeInfo);
	 * customerInfoResult =
	 * customizeInfoService.insertCustomizeItemInfo(customizeInfo);
	 *
	 * customizeInfoString = customizeInfo.toString(); } else {
	 *
	 * CustomizeListInfo customizeInfo = new CustomizeListInfo();
	 * customizeInfo.setrUserId(request.getParameter("userId"));
	 * customizeInfo.setAllItem(paramArray);
	 *
	 * customizeInfoService.deleteCustomizeListInfo(customizeInfo);
	 * customerInfoResult =
	 * customizeInfoService.insertCustomizeListInfo(customizeInfo);
	 *
	 * customizeInfoString = customizeInfo.toString(); }
	 *
	 * if(customerInfoResult == 1) { jRes.setSuccess(AJaxResVO.SUCCESS_Y);
	 * jRes.setResult("1"); jRes.addAttribute("msg",
	 * "search_customize apply success");
	 *
	 * logInfoService.writeLog(request, "Search customize - Apply success",
	 * customizeInfoString, userInfo.getUserId()); } else {
	 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "search_customize apply fail");
	 *
	 * logInfoService.writeLog(request, "Search customize - Apply fail",
	 * customizeInfoString, userInfo.getUserId()); } } else {
	 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "search_customize apply command fail");
	 *
	 * logInfoService.writeLog(request, "Search customize - Apply command fail",
	 * request.getParameter("proc"), userInfo.getUserId()); } } else {
	 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "proc fail");
	 *
	 * logInfoService.writeLog(request, "Search customize - Abnormal request",
	 * null, userInfo.getUserId()); } } else {
	 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "login fail");
	 *
	 * logInfoService.writeLog(request, "Search customize logout"); } return
	 * jRes; }
	 */

	// 시스템 관리 - 시스템 설정
	@RequestMapping(value = "/search_customize_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO search_customize_proc(
			HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {
			if (request.getParameter("proc") != null) {
				if (request.getParameter("proc").equals("apply")
						&& request.getParameter("userId") != null
						&& request.getParameter("columns") != null
						&& request.getParameter("type") != null) {

					String typeString = "list";

					if (request.getParameter("type") != null
							&& !request.getParameter("type").isEmpty()) {
						typeString = request.getParameter("type");
					}

					String[] paramArray = request.getParameter("columns")
							.split("\\|");

					Integer customerInfoResult = 0;
					String customizeInfoString = "";
					if (typeString.equals("item")) {

						CustomizeItemInfo customizeInfo = new CustomizeItemInfo();

						customizeInfo
								.setrUserId(request.getParameter("userId"));
						customizeInfo.setAllItem(paramArray);

						customizeInfoService
								.deleteCustomizeItemInfo(customizeInfo);
						customerInfoResult = customizeInfoService
								.insertCustomizeItemInfo(customizeInfo);

						customizeInfoString = customizeInfo.toString();
					}else if (typeString.equals("copy")) {

						CustomizeCopyListInfo customizeInfo = new CustomizeCopyListInfo();

						customizeInfo
								.setrUserId(request.getParameter("userId"));
						customizeInfo.setAllItem(paramArray);

						customizeInfoService
								.deleteCustomizeCopyListInfo(customizeInfo);
						customerInfoResult = customizeInfoService
								.insertCustomizeCopyListInfo(customizeInfo);

						customizeInfoString = customizeInfo.toString();
					} else { // List이면

						CustomizeListInfo customizeInfo = new CustomizeListInfo();
						customizeInfo
								.setrUserId(request.getParameter("userId"));
						customizeInfo.setAllItem(paramArray);

						customizeInfoService
								.deleteCustomizeListInfo(customizeInfo);
						customerInfoResult = customizeInfoService
								.insertCustomizeListInfo(customizeInfo);

						customizeInfoString = customizeInfo.toString();
					}

					if (customerInfoResult == 1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
						jRes.addAttribute("msg",
								"search_customize apply success");

						logInfoService.writeLog(request,
								"Search customize - Apply success",
								customizeInfoString, userInfo.getUserId());
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "search_customize apply fail");

						logInfoService.writeLog(request,
								"Search customize - Apply fail",
								customizeInfoString, userInfo.getUserId());
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg",
							"search_customize apply command fail");

					logInfoService.writeLog(request,
							"Search customize - Apply command fail",
							request.getParameter("proc"), userInfo.getUserId());
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "proc fail");

				logInfoService.writeLog(request,
						"Search customize - Abnormal request", null,
						userInfo.getUserId());
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Search customize logout");
		}
		return jRes;
	}
	
	// 그룹 사용 여부 불러오기 
	@RequestMapping(value = "/selectGroupInfo.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO selectGroupInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		OrganizationInfo groupInfo = new OrganizationInfo();
		List<OrganizationInfo> groupInfoResult = new ArrayList<OrganizationInfo>();
		try {
			if (userInfo != null) {
				if (request.getParameter("bgCode") != null
						&& !request.getParameter("bgCode").isEmpty()) {
					groupInfo.setrBgName(request.getParameter("bgCode"));
				}
				if (request.getParameter("mgCode") != null
						&& !request.getParameter("mgCode").isEmpty()) {
					groupInfo.setrMgCode(request.getParameter("mgCode"));
				}
				if (request.getParameter("sgCode") != null
						&& !request.getParameter("sgCode").isEmpty()) {
					groupInfo.setrSgCode(request.getParameter("sgCode"));
				}
				groupInfo.setType("all");
				String level = request.getParameter("level");
				switch (level) {
				case "2": // 대그룹
					groupInfoResult = organizationInfoService.selectOrganizationBgInfo(groupInfo);
					break;
				case "3": // 중그룹
					groupInfoResult = organizationInfoService.selectOrganizationMgInfo(groupInfo);
					break;
				case "4": // 소그룹
					groupInfoResult = organizationInfoService.selectOrganizationSgInfo(groupInfo);
					break;
				}
				
				if (groupInfoResult.size() > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("useYN", groupInfoResult.get(0).getUseYn());
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	
	private void setValue(String[] arr, String value, Integer pos) {
		if (!StringUtil.isNull(value))
			arr[pos] = value;
		else
			arr[pos] = "";
	}
	
	public boolean updateGroupInfo(String userId, HttpSession session, HttpServletRequest request) {
		boolean result = true;
		
		int period=12;
		
		if(null!=session.getAttribute("updateGroupinfoPeriod")) {
			try {
				System.out.println(session.getAttribute("updateGroupinfoPeriod"));
				period=Integer.parseInt((String)session.getAttribute("updateGroupinfoPeriod"));
			}catch(Exception e) {
				System.out.println("updateGroupinfoPeriod parsing Error");
			}
		}
		Calendar cal = new GregorianCalendar(Locale.KOREA);
	    cal.setTime(new Date());
	    cal.add(Calendar.MONTH, -period);
	     
	    SimpleDateFormat fm = new SimpleDateFormat("yyyyMdd");
	    String strDate = fm.format(cal.getTime());
		
		SearchListInfo searchInfo = new SearchListInfo();
		
		searchInfo.setUserId(userId);
		searchInfo.setRecDateRaw(strDate);
		searchInfo.setBgCode(request.getParameter("bgCode"));
		searchInfo.setMgCode(request.getParameter("mgCode"));
		searchInfo.setSgCode(request.getParameter("sgCode"));
		
		try {
			searchListInfoService.updateGroupInfo(searchInfo);
		}catch(Exception e) {
			result = false;
			System.out.println("updateGroupInfo error :"+e);
		}
		return result;
	}
	
	// 모바일 보관기간 가져오기
	@RequestMapping(value = "/getMobileManageSetting", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO getMobileManageSetting(HttpServletRequest request,	Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {

			String noData = "";
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			List<EtcConfigInfo> etcConfigInfoResult = new ArrayList<EtcConfigInfo>();

			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("MOBILE_STORAGE_PERIOD");
			try {
				etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				
			}
			
			if (etcConfigInfoResult.size() == 0) {
				etcConfigInfo.setConfigValue("14");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} 
			
			if (etcConfigInfoResult.size() > 0) {
				jRes.addAttribute("storagePeriod", etcConfigInfoResult.get(0).getConfigValue());
				etcConfigInfoResult = null;
			} else {
				noData = ",storagePeriod";
			}
			
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("MOBILE_REQUEST_IP");
			try {
				etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				
			}
			
			if (etcConfigInfoResult.size() == 0) {
				etcConfigInfo.setConfigValue("");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} 

			if (etcConfigInfoResult.size() > 0) {
				jRes.addAttribute("requestIP", etcConfigInfoResult.get(0).getConfigValue());
				etcConfigInfoResult = null;
			} else {
				noData += ",requestIP";
			}
			
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("MOBILE_REQUEST_PORT");
			try {
				etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				
			}
			
			if (etcConfigInfoResult.size() == 0) {
				etcConfigInfo.setConfigValue("");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				etcConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} 

			if (etcConfigInfoResult.size() > 0) {
				jRes.addAttribute("requestPort", etcConfigInfoResult.get(0).getConfigValue());
				etcConfigInfoResult = null;
			} else {
				noData += ",requestPort";
			}
			
			if("".equals(noData)) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", noData);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
		
	// 모바일 보관기간 가져오기
	@RequestMapping(value = "/setStoragePeriod", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO setStoragePeriod(HttpServletRequest request,	Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();

		if (userInfo != null) {
			if (!StringUtil.isNull(request.getParameter("storagePeriodValue"), true)) {
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("MOBILE_STORAGE_PERIOD");
				etcConfigInfo.setConfigValue(request.getParameter("storagePeriodValue"));
				
				Integer result = 0;
				try {
					result = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);
				} catch(Exception e) {
					
				}
				
				if (result > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("1");
					jRes.addAttribute("msg", "UpdateSuccess");
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "UpdateFail");
				}
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "LoginFail");
		}
		return jRes;
	}
	
	
	// 모바일 보관기간 가져오기
	@RequestMapping(value = "/setRequestInfo", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO setRequestInfo(HttpServletRequest request,	Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		Integer result = 0;
		
		if (userInfo != null) {
			if (!StringUtil.isNull(request.getParameter("requestIP"), true)) {
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("MOBILE_REQUEST_IP");
				etcConfigInfo.setConfigValue(request.getParameter("requestIP"));
			}

			try {
				result = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				
			}
			
			if (!StringUtil.isNull(request.getParameter("requestPort"), true)) {
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("MOBILE_REQUEST_PORT");
				etcConfigInfo.setConfigValue(request.getParameter("requestPort"));
			}

			try {
				result = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				
			}
			
			if (result == 2) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("msg", "UpdateSuccess");
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "UpdateFail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "LoginFail");
		}
		return jRes;
	}
	
	
}
