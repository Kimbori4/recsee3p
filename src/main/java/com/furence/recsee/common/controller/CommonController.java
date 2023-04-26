package com.furence.recsee.common.controller;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.admin.controller.AjaxUserManageController;
import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.model.QueueInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.ChannelInfoService;
import com.furence.recsee.admin.service.QueueInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.model.CustConfigInfo;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.Log;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.LogoVO;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.CommonCodeService;
import com.furence.recsee.common.service.CustConfigInfoService;
import com.furence.recsee.common.service.CustomerInfoService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MAccessLevelInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.DateUtil;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;
import com.furence.recsee.main.model.dhtmlXGridCombo;
import com.furence.recsee.main.model.dhtmlXGridComboOption;
import com.furence.recsee.statistics.model.StatisticsInfo;
import com.furence.recsee.statistics.service.StatisticsInfoService;
import com.itextpdf.text.pdf.AcroFields.Item;

@Controller
public class CommonController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private MAccessLevelInfoService accessLevelInfoService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private ChannelInfoService channelInfoService;

/*	@Autowired
	private EvaluationService evaluationService;*/

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private CustConfigInfoService custConfigInfoService;

	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private StatisticsInfoService statisticsinfoservice;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private QueueInfoService queueInfoService;

	@Autowired
	private LogService logService;

	@Autowired
	private SubNumberInfoService subNumberInfoService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CommonCodeService commonCodeService;

	
	// 접속 유지
	@RequestMapping(value="/keep_alive.do", method=RequestMethod.GET)
	public @ResponseBody AJaxResVO keepAlive(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo != null) {
			jRes.setResult("1");
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			jRes.setResult("0");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	// 대중소 셀렉트 리턴
	@RequestMapping(value="/organizationSelect.do", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody  AJaxResVO organizationSelect(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{

			if(userInfo != null){
				String optionResult = "";
				String optionStr1 = "<option value=\"";
				String optionStr2 = "\">";
				String optionStr3 = "\" selected>";
				String optionStr4 = "</option>";

				OrganizationInfo organizationInfo = new OrganizationInfo();
				List<OrganizationInfo> organizationInfoResult = null;
				List<RUserInfo> ruserInfoResult = null;
				List<StatisticsInfo> statisticsInfoResult = null;

				if(request.getParameter("notAdmin") != null) {
					organizationInfo.setNotAdmin("Y");
				}

				if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("bgCode")) {
					if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
						organizationInfo.setrBgCode(request.getParameter("bgCode"));
					}
					/*if(!StringUtil.isNull(request.getParameter("subOpt"),true) && request.getParameter("subOpt").equals("callCenter")) {
						EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
						etcConfigInfo.setGroupKey("CALLCENTER");
						etcConfigInfo.setConfigKey("CALLCENTER");
						List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

						String[] lista = etcConfigResult.get(0).getConfigValue().split(",");
						List<String> list = new ArrayList<String>();

						for(int i=0;i<lista.length;i++) {
							list.add(lista[i]);
						}

						organizationInfo.setList(list);
					}*/

					/*if(!StringUtil.isNull(request.getParameter("subOpt"),true) && request.getParameter("subOpt").equals("notCallCenter")) {
						EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
						etcConfigInfo.setGroupKey("CALLCENTER");
						etcConfigInfo.setConfigKey("CALLCENTER");
						List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

						String[] lista = etcConfigResult.get(0).getConfigValue().split(",");
						List<String> list = new ArrayList<String>();

						for(int i=0;i<lista.length;i++) {
							list.add(lista[i]);
						}
						organizationInfo.setNotIvr("Y");
						organizationInfo.setaUser("Y");
						organizationInfo.setList(list);
					}*/

					if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("A") ) {
						organizationInfo.setrBgCode(userInfo.getBgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B"))
							organizationInfo.setrMgCode(userInfo.getMgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B") && !request.getParameter("accessLevel").equals("M"))
							organizationInfo.setrSgCode(userInfo.getSgCode());

					}
					organizationInfoResult = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
				} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("mgCode")) {
					if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
						organizationInfo.setrBgCode(request.getParameter("bgCode"));
					}
					if(request.getParameter("mgCode") != null && !request.getParameter("mgCode").isEmpty()) {
						organizationInfo.setrMgCode(request.getParameter("mgCode"));
					}
					if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("A") ) {
						organizationInfo.setrBgCode(userInfo.getBgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B"))
							organizationInfo.setrMgCode(userInfo.getMgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B") && !request.getParameter("accessLevel").equals("M"))
							organizationInfo.setrSgCode(userInfo.getSgCode());

					}
					organizationInfoResult = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
				} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("sgCode")) {
					if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
						organizationInfo.setrBgCode(request.getParameter("bgCode"));
					}
					if(request.getParameter("mgCode") != null && !request.getParameter("mgCode").isEmpty()) {
						organizationInfo.setrMgCode(request.getParameter("mgCode"));
					}
					if(request.getParameter("sgCode") != null && !request.getParameter("sgCode").isEmpty()) {
						organizationInfo.setrSgCode(request.getParameter("sgCode"));
					}
					if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("A") ) {
						organizationInfo.setrBgCode(userInfo.getBgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B"))
							organizationInfo.setrMgCode(userInfo.getMgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B") && !request.getParameter("accessLevel").equals("M"))
							organizationInfo.setrSgCode(userInfo.getSgCode());

					}
					organizationInfoResult = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("agentName")) {
					//	상담사명 으로 검색 하였을 경우
					RUserInfo ruserInfo = new RUserInfo();
					if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("A") ) {
						ruserInfo.setBgCode(userInfo.getBgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B"))
							ruserInfo.setMgCode(userInfo.getMgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B") && !request.getParameter("accessLevel").equals("M"))
							ruserInfo.setSgCode(userInfo.getSgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && request.getParameter("accessLevel").equals("U"))
							ruserInfo.setUserId(userInfo.getUserId());

					};
					ruserInfo.setSelectAll("Y");
					ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("agentNum")) {
					//	아이디로 검색 하였을 경우
					RUserInfo ruserInfo = new RUserInfo();
					if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("A") ) {
						ruserInfo.setBgCode(userInfo.getBgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B"))
							ruserInfo.setMgCode(userInfo.getMgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B") && !request.getParameter("accessLevel").equals("M"))
							ruserInfo.setSgCode(userInfo.getSgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && request.getParameter("accessLevel").equals("U"))
							ruserInfo.setUserId(userInfo.getUserId());

					};
					ruserInfo.setSelectAll("Y");
					ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("agentExt")) {
					//	내선번호로 검색 하였을 경우
					RUserInfo ruserInfo = new RUserInfo();
					if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("A") ) {
						ruserInfo.setBgCode(userInfo.getBgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B"))
							ruserInfo.setMgCode(userInfo.getMgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && !request.getParameter("accessLevel").equals("B") && !request.getParameter("accessLevel").equals("M"))
							ruserInfo.setSgCode(userInfo.getSgCode());

						if(!StringUtil.isNull(request.getParameter("accessLevel"),true) && request.getParameter("accessLevel").equals("U"))
							ruserInfo.setUserId(userInfo.getUserId());

					};
					ruserInfo.setSelectAll("Y");
					ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("category")) {
					//	카테고리 목록
					StatisticsInfo statisticsInfo = new StatisticsInfo();
					statisticsInfoResult = statisticsinfoservice.selectCategory(statisticsInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("keyword")) {
					//	키워드 목록
					StatisticsInfo statisticsInfo = new StatisticsInfo();
					if(request.getParameter("subOpt") != null && "C".equals(request.getParameter("subOpt").substring(0, 1))) {
						statisticsInfo.setCategoryCode(request.getParameter("subOpt"));
					}
					statisticsInfoResult = statisticsinfoservice.selectKeyword(statisticsInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("cbgCode")) {
					
					String temp1 = "";
					String temp2 = "";			
					
					
					if(request.getParameter("serviceCode") != null && !request.getParameter("serviceCode").isEmpty()) {
						temp1 = request.getParameter("serviceCode");
					}
					
					if(request.getParameter("receiptCode") != null && !request.getParameter("receiptCode").isEmpty()) {
						temp2 = request.getParameter("receiptCode");
					}
					
					if("1000".equals(temp1) && !"RM".equals(temp2)) {
						organizationInfo.setType("1");
					}else if("1000".equals(temp1) && "RM".equals(temp2)) {
						organizationInfo.setType("2");
					}else if("2000".equals(temp1) && !"RM".equals(temp2)) {
						organizationInfo.setType("3");
					}else if("2000".equals(temp1) && "RM".equals(temp2)) {
						organizationInfo.setType("4");
					}
					
					organizationInfoResult = organizationInfoService.selectOrganizationCBgInfo(organizationInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("cmgCode")) {
					
					if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
						organizationInfo.setrBgCode(request.getParameter("bgCode"));
					}
										
					organizationInfoResult = organizationInfoService.selectOrganizationCMgInfo(organizationInfo);
				}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("csgCode")) {
										
					
					if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
						organizationInfo.setrBgCode(request.getParameter("bgCode"));
					}
					
					if(request.getParameter("mgCode") != null && !request.getParameter("mgCode").isEmpty()) {
						organizationInfo.setrMgCode(request.getParameter("mgCode"));
					}
					
					organizationInfoResult = organizationInfoService.selectOrganizationCSgInfo(organizationInfo);
				}

				Integer organizationInfoTotal = 0;
				Integer ruserInfoTotal = 0;
				Integer statisticInfoTotal = 0;

				if(organizationInfoResult != null) {
					organizationInfoTotal = organizationInfoResult.size();
				}
				if(ruserInfoResult != null) {
					ruserInfoTotal = ruserInfoResult.size();
				}
				if(statisticsInfoResult != null) {
					statisticInfoTotal = statisticsInfoResult.size();
				}

				/*if(request.getParameter("subOpt") != null && request.getParameter("subOpt").equals("ALL")) {
					optionResult += optionStr1+"";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;
				}*/

				if(organizationInfoResult!=null && organizationInfoTotal > 0) {

					for(int i = 1; i <= organizationInfoTotal; i++) {
						OrganizationInfo item = organizationInfoResult.get(i-1);
							
						if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("bgCode")) {
								
							if(item.getrBgCode() != null && !item.getrBgCode().isEmpty())
								optionResult += optionStr1+item.getrBgCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrBgName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrBgCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							if(item.getrBgName() != null && !item.getrBgName().isEmpty())
								optionResult += item.getrBgName();
							optionResult += optionStr4;

						} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("mgCode")) {
							if(item.getrMgCode() != null && !item.getrMgCode().isEmpty())
								optionResult += optionStr1+item.getrMgCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrMgName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrMgCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							if(item.getrMgName() != null && !item.getrMgName().isEmpty())
								optionResult += item.getrMgName();

							optionResult += optionStr4;

						} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("sgCode")) {
							if(item.getrSgCode() != null && !item.getrSgCode().isEmpty())
								optionResult += optionStr1+item.getrSgCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrSgName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrSgCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							if(item.getrSgName() != null && !item.getrSgName().isEmpty())
								optionResult += item.getrSgName();

							optionResult += optionStr4;
						} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("cbgCode")) {

							if(item.getrBgCode() != null && !item.getrBgCode().isEmpty())
								optionResult += optionStr1+item.getrBgCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrBgName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrBgCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							if(item.getrBgName() != null && !item.getrBgName().isEmpty())
								optionResult += item.getrBgName();
							optionResult += optionStr4;

						} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("cmgCode")) {
							if(item.getrMgCode() != null && !item.getrMgCode().isEmpty())
								optionResult += optionStr1+item.getrMgCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrMgName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrMgCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							if(item.getrMgName() != null && !item.getrMgName().isEmpty())
								optionResult += item.getrMgName();

							optionResult += optionStr4;
						}else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("csgCode")) {
							if(item.getrSgCode() != null && !item.getrSgCode().isEmpty())
								optionResult += optionStr1+item.getrSgCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrSgName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrSgCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							if(item.getrSgName() != null && !item.getrSgName().isEmpty())
								optionResult += item.getrSgName();

							optionResult += optionStr4;
						}
						
					}

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				}else if(ruserInfoTotal > 0) {
					//	상담사명,사번,내선번호로 검색하였을 경우
						for(int i = 1; i <= ruserInfoTotal; i++) {
							RUserInfo item = ruserInfoResult.get(i-1);
							 if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("agentName")) {
								 if(item.getUserName() != null && !item.getUserName().isEmpty())
										optionResult += optionStr1+item.getUserName();

								if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
									optionResult += optionStr3;
								else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getUserName()))
									optionResult += optionStr3;
								else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getUserName()))
									optionResult += optionStr3;
								else
									optionResult += optionStr2;

								if(item.getUserName() != null && !item.getUserName().isEmpty())
									optionResult += item.getUserName();
							 }else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("agentNum")) {
								 if(item.getUserId() != null && !item.getUserId().isEmpty())
										optionResult += optionStr1+item.getUserId();

								if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
									optionResult += optionStr3;
								else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getUserId()))
									optionResult += optionStr3;
								else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getUserId()))
									optionResult += optionStr3;
								else
									optionResult += optionStr2;

								if(item.getUserId() != null && !item.getUserId().isEmpty())
									optionResult += item.getUserId();
							 }else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("agentExt")) {
								 if(item.getExtNo() != null && !item.getExtNo().isEmpty() && !item.getExtNo().equals(" "))
										optionResult += optionStr1+item.getExtNo()+optionStr2;

								if(item.getExtNo() != null && !item.getExtNo().isEmpty() && !item.getExtNo().equals(" "))
									optionResult += item.getExtNo()+optionStr4;
							 }
						}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					
				}else if(statisticsInfoResult!= null && statisticInfoTotal > 0) {
					//	키워드 목록
					for(int i = 1; i <= statisticInfoTotal; i++) {
						StatisticsInfo item = statisticsInfoResult.get(i-1);
						 if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("keyword")) {
							if(item.getKeywordName() != null && !item.getKeywordName().isEmpty() && !item.getKeywordName().equals(" ")) {
								optionResult += optionStr1+item.getKeywordName()+optionStr2;
								optionResult += item.getKeywordName()+optionStr4;
							}
						 }else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("category")) {
							if(item.getCategoryCode() != null && !item.getCategoryCode().isEmpty() && !item.getCategoryCode().equals(" "))
									optionResult += optionStr1+item.getCategoryCode()+optionStr2;

							if(item.getCategoryName() != null && !item.getCategoryName().isEmpty() && !item.getCategoryName().equals(" "))
								optionResult += item.getCategoryName()+optionStr4;
						 }
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTIONG");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGIN FAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (NullPointerException e) {
			logger.error("", e);
			jRes.setResult("FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}catch (Exception e) {
			logger.error("", e);
			jRes.setResult("FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
	return jRes;
	}

	// html셀렉트 값 내용 리턴해쥬는거 > _<
	@RequestMapping(value="/selectOption.do", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody  AJaxResVO selectOption(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			// if(userInfo != null){
				String optionResult = "";
				String optionStr1 = "<option value=\"";
				String optionStr2 = "\">";
				String optionStr3 = "\" selected>";
				String optionStr4 = "</option>";

				if(request.getParameter("comboType").equals("authy")) {

					MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();

					List<MAccessLevelInfo> accessLevelInfoList = accessLevelInfoService.selectAccessLevelInfo(accessLevelInfo);
					int accessLevelTotal = accessLevelInfoList.size();

					String levelCode = null;
					String levelName = null;

					if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {

						levelCode = "default";
						levelName = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

						optionResult += optionStr1+levelCode;

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0)))
							optionResult += optionStr3;
						else
							optionResult += optionStr2;
						optionResult += levelName;
						optionResult += optionStr4;
					}

					if(accessLevelTotal > 0) {

						for(int i = 0; i < accessLevelTotal; i++) {
							MAccessLevelInfo accessLevelItem = accessLevelInfoList.get(i);

							levelCode = accessLevelItem.getLevelCode();
							levelName = accessLevelItem.getLevelName();

							optionResult += optionStr1+levelCode;

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(accessLevelItem.getLevelName()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(accessLevelItem.getLevelCode()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							optionResult += levelName;
							optionResult += optionStr4;
						}

						jRes.setResult("SUCCESS");
						jRes.addAttribute("optionResult", optionResult);
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					}else{
						jRes.setResult("NOTIONG");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
					}
				}else if(request.getParameter("comboType").equals("channel")) {
					ChannelInfo channelInfo = new ChannelInfo();

					List<ChannelInfo> channelInfoList = channelInfoService.groupChannelInfo(channelInfo);
					int channelInfoTotal = channelInfoList.size();

					if(channelInfoTotal > 0) {

						optionResult += optionStr1+"";
						optionResult += optionStr2;
						optionResult += "";
						optionResult += optionStr4;

						for(int i = 0; i < channelInfoTotal; i++) {
							ChannelInfo channelItem = channelInfoList.get(i);

							String extNum = channelItem.getExtNum();
							if(extNum == null) extNum = "";

							optionResult += optionStr1+extNum;

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i)))
								optionResult += optionStr3;
							else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(channelItem.getExtNum()))
								optionResult += optionStr3;
							else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(channelItem.getExtNum()))
								optionResult += optionStr3;
							else
								optionResult += optionStr2;

							optionResult += extNum;
							optionResult += optionStr4;

						}

						jRes.setResult("SUCCESS");
						jRes.addAttribute("optionResult", optionResult);
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					}else{
						jRes.setResult("NOTIONG");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
					}
				} else if(request.getParameter("comboType").equals("Time") || request.getParameter("comboType").equals("rTime")) {

					String szTime = "";
					String szTimeName = "";

					/*optionResult += optionStr1;
					optionResult += szTime;

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionResult += optionStr3;
					}
					optionResult += optionStr2;
					optionResult += szTimeName;
					optionResult += optionStr4;*/
					int from = 0, to = 24;
					if (request.getParameter("type") != null && "search".equals(request.getParameter("type"))) {
						from = 9;
						to = 18;
					}
					for(int i = from; i < to; i++) {

						if (request.getParameter("type") != null && "search".equals(request.getParameter("type"))) {
							szTime = String.format("%02d:00", i);
							szTimeName = String.format("%02d:00", i);
						} else {
							szTime = String.format("%02d:00:00", i);
							szTimeName = String.format("%02d:00:00", i);
						}

						if(request.getParameter("comboType").equals("Time")) {
							if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("e")) {
								if (request.getParameter("type") != null && "search".equals(request.getParameter("type"))) {
									szTime = String.format("%02d:00", i);
									szTimeName = String.format("%02d:00", i);
								} else {
									szTime = String.format("%02d:59:59", i);
									szTimeName = String.format("%02d:59:59", i);
								}
							}
						} else {
							if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("e")) {
								szTime = String.format("%02d:59:59", i);
							}else if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("s")) {
								szTime = String.format("%02d", i);
							}
							szTimeName = String.format("%02d", i);
						}

						optionResult += optionStr1+"";
						optionResult += szTime;

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionResult+=optionStr3;
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(szTimeName)) {
							optionResult+=optionStr3;
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(szTime)) {
							optionResult+=optionStr3;
						} else
							optionResult += optionStr2;
						optionResult += szTimeName;
						optionResult += optionStr4;
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(request.getParameter("comboType").equals("tTime")) {

					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("SYSTEM");
					etcConfigInfo.setConfigKey("TTIME_RANGE");

					List<EtcConfigInfo> tTimeRangeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					Integer tTimeRangeResultTotal = tTimeRangeResult.size();

					String[] tTimeRange = null;
					if(tTimeRangeResultTotal < 1) {
						tTimeRange = "10,30,60,300,600".split(",");
					} else {
						tTimeRange = tTimeRangeResult.get(0).getConfigValue().split(",");
					}


					String szTime = "";
					String szTimeName = "";

					/*optionResult += optionStr1;
					optionResult += szTime;

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionResult += optionStr3;
					} else
						optionResult += optionStr2;
					optionResult += szTimeName;
					optionResult += optionStr4;*/

					for(int i = 0; i < tTimeRange.length; i++) {

						szTime = tTimeRange[i];
						szTimeName = new RecSeeUtil().getSecToTime(Integer.parseInt(tTimeRange[i]));

						optionResult += optionStr1;
						optionResult += szTime;

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(szTimeName)) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(szTime)) {
							optionResult += optionStr3;
						} else
							optionResult += optionStr2;
						optionResult += szTimeName;
						optionResult += optionStr4;
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("callType")) {

					optionResult += optionStr1;
					optionResult += "";

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionResult += optionStr3;
					}
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;

					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("SYSTEM");
					etcConfigInfo.setConfigKey("CALL_TYPE");
					List<EtcConfigInfo> callTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					String callType = "I,O";
					if(callTypeResult.size() > 0) {
						callType = callTypeResult.get(0).getConfigValue();
					}
					String[] callTypeArray = callType.split(",");

					for(int i = 0; i < callTypeArray.length; i++) {


						optionResult += optionStr1;
						optionResult += callTypeArray[i];

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("call.type."+callTypeArray[i], null,Locale.getDefault()))) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(callTypeArray[i])) {
							optionResult += optionStr3;
						} else
							optionResult += optionStr2;
						optionResult += messageSource.getMessage("call.type."+callTypeArray[i], null,Locale.getDefault());
						optionResult += optionStr4;

						jRes.setResult("SUCCESS");
						jRes.addAttribute("optionResult", optionResult);
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);

					}

				} else if(request.getParameter("comboType").equals("callKind")) {

					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("SYSTEM");
					etcConfigInfo.setConfigKey("START_TYPE");
					List<EtcConfigInfo> callTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					String callType = callTypeResult.get(0).getConfigValue();

					String[] callTypeArray = callType.split(",");

					optionResult += optionStr1;
					optionResult += "T";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.rec.type.tcr", null,Locale.getDefault());
					optionResult += optionStr4;

					for(int i=0;i<callTypeArray.length;i++) {
						if(callTypeArray[i].equals("T"))
							continue;

						optionResult += optionStr1;
						optionResult += callTypeArray[i];
						optionResult += optionStr2;
						optionResult += messageSource.getMessage("call.rec.startType."+callTypeArray[i], null,Locale.getDefault());
						optionResult += optionStr4;
					}
					/*optionResult += optionStr1;
					optionResult += "A";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.rec.type.acr", null,Locale.getDefault());
					optionResult += optionStr4;

					optionResult += optionStr1;
					optionResult += "S";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.rec.type.odr", null,Locale.getDefault());
					optionResult += optionStr4;


					optionResult += optionStr1;
					optionResult += "P";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.rec.type.tacr", null,Locale.getDefault());
					optionResult += optionStr4;


					optionResult += optionStr1;
					optionResult += "T";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.rec.type.tcr", null,Locale.getDefault());
					optionResult += optionStr4;*/

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("YN")) {

					/*optionResult += optionStr1;
					optionResult += "";

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionResult += optionStr3;
					}
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;*/

					optionResult += optionStr1;
					optionResult += "Y";

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(1))) {
						optionResult += optionStr3;
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("use.yes", null,Locale.getDefault()))) {
						optionResult += optionStr3;
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("Y")) {
						optionResult += optionStr3;
					} else
						optionResult += optionStr2;
					optionResult += messageSource.getMessage("use.yes", null,Locale.getDefault());
					optionResult += optionStr4;

					optionResult += optionStr1;
					optionResult += "N";

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(2))) {
						optionResult += optionStr3;
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("use.no", null,Locale.getDefault()))) {
						optionResult += optionStr3;
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("N")) {
						optionResult += optionStr3;
					} else
						optionResult += optionStr2;
					optionResult += messageSource.getMessage("use.no", null,Locale.getDefault());
					optionResult += optionStr4;

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("YN2")) {
					optionResult += optionStr1;
					optionResult += "Y";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("use.yes", null,Locale.getDefault());
					optionResult += optionStr4;

					optionResult += optionStr1;
					optionResult += "N";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("use.no", null,Locale.getDefault());
					optionResult += optionStr4;

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("YN5")) {
					optionResult += optionStr1;
					optionResult += "Y";
					optionResult += optionStr2;
					optionResult += "완료";
					optionResult += optionStr4;

					optionResult += optionStr1;
					optionResult += "N";
					optionResult += optionStr2;
					optionResult += "재시도";
					optionResult += optionStr4;

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("startType")) {

					/*optionResult += optionStr1;
					optionResult += "";

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionResult += optionStr3;
					}
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;*/

					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("SYSTEM");
					etcConfigInfo.setConfigKey("START_TYPE");
					List<EtcConfigInfo> callTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					String callType = "A";
					if(callTypeResult.size() > 0) {
						callType = callTypeResult.get(0).getConfigValue();
					}
					String[] callTypeArray = callType.split(",");

					for(int i = 0; i < callTypeArray.length; i++) {
						if(callTypeArray[i].equals("T")) {
							continue;
						}
						optionResult += optionStr1;
						optionResult += callTypeArray[i];

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("call.type."+callTypeArray[i], null,Locale.getDefault()))) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(callTypeArray[i])) {
							optionResult += optionStr3;
						} else
							optionResult += optionStr2;
						optionResult += messageSource.getMessage("call.type.startType."+callTypeArray[i], null,Locale.getDefault());
						optionResult += optionStr4;
					}

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("system")) {

					if(!StringUtil.isNull(request.getParameter("ALL"), true) && "all".equals(request.getParameter("ALL"))) {
						optionResult += optionStr1;
						optionResult += "";
						optionResult += optionStr2;
						optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
						optionResult += optionStr4;
					}

					SystemInfo systemInfo = new SystemInfo();
					List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);
					int systemInfoTotal = systemInfoList.size();

					if(systemInfoTotal > 0) {

						for(int i = 0; i < systemInfoTotal; i++) {
							SystemInfo sysItem = systemInfoList.get(i);

							optionResult += optionStr1;
							optionResult += sysItem.getSysId();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(sysItem.getSysName())) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(sysItem.getSysId())) {
								optionResult += optionStr3;
							} else if (request.getParameter("recIp") != null && request.getParameter("recIp").equals(sysItem.getSysIp())) {
								optionResult += optionStr3;
							} else {
								optionResult += optionStr2;
							}
							optionResult += sysItem.getSysName();
							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(request.getParameter("comboType").equals("systemCode")) {

					/*if(!StringUtil.isNull(request.getParameter("ALL"), true) && "all".equals(request.getParameter("ALL"))) {
						optionResult += optionStr1;
						optionResult += "";
						optionResult += optionStr2;
						optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
						optionResult += optionStr4;
					}*/

					SystemInfo systemInfo = new SystemInfo();
					List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);
					int systemInfoTotal = systemInfoList.size();

					if(systemInfoTotal > 0) {

						for(int i = 0; i < systemInfoTotal; i++) {
							SystemInfo sysItem = systemInfoList.get(i);

							optionResult += optionStr1;
							optionResult += sysItem.getSysId();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(sysItem.getSysName())) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(sysItem.getSysId())) {
								optionResult += optionStr3;
							} else if (request.getParameter("recIp") != null && request.getParameter("recIp").equals(sysItem.getSysIp())) {
								optionResult += optionStr3;
							} else {
								optionResult += optionStr2;
							}
							optionResult += sysItem.getSysId();
							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(request.getParameter("comboType").equals("logName")) {

					optionResult += optionStr1;
					optionResult += "";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;

					Log log = new Log();

					List<Log> logNameList = logService.selectLogName(log);
					int logNameTotal = logNameList.size();

					if(logNameTotal > 0) {

						for(int i = 0; i < logNameTotal; i++) {
							Log logItem = logNameList.get(i);

							optionResult += optionStr1;
							optionResult += logItem.getrLogCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(logItem.getrLogName())) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(logItem.getrLogCode())) {
								optionResult += optionStr3;
							} else
								optionResult += optionStr2;
							//optionResult += logItem.getrLogName(); // 언어팩 처리 필요
							optionResult += messageSource.getMessage("admin.log.name."+logItem.getrLogName(), null,Locale.getDefault());
							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(request.getParameter("comboType").equals("logContents")) {

					optionResult += optionStr1;
					optionResult += "";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;

					Log log = new Log();

					if (!StringUtil.isNull(request.getParameter("comboType2"),true))
						log.setrLogCode(request.getParameter("comboType2"));

					List<Log> logContentsList = logService.selectLogContents(log);
					int logContentsTotal = logContentsList.size();

					if(logContentsTotal > 0) {

						for(int i = 0; i < logContentsTotal; i++) {
							Log logItem = logContentsList.get(i);

							optionResult += optionStr1;
							optionResult += logItem.getrLogDetailCode();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(logItem.getrLogContents())) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(logItem.getrLogDetailCode())) {
								optionResult += optionStr3;
							} else
								optionResult += optionStr2;
							//optionResult += logItem.getrLogContents(); // 언어팩 처리 필요
							optionResult += messageSource.getMessage("admin.log.contents."+logItem.getrLogContents(), null,Locale.getDefault());
							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if (request.getParameter("comboType").equals("common")){

					/*if (!StringUtil.isNull(request.getParameter("ALL")) && !"not".equals(request.getParameter("ALL")) ) {
						optionResult += optionStr1;
						optionResult += "";
						optionResult += optionStr2;
						optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
						optionResult += optionStr4;
					}*/

					CommonCodeVO commonCode = new CommonCodeVO();
					commonCode.setParentCode(request.getParameter("comboType2"));
					List<CommonCodeVO> codeList = commonCodeService.selectCommonCode(commonCode);

					if(codeList.size() > 0) {

						for(int i = codeList.size()-1; i >= 0; i--) {
							CommonCodeVO item = codeList.get(i);
							try {
								if(Integer.parseInt(item.getCodeValue())>2) {
									continue;
								}
							}catch (Exception e) {
								logger.error("error",e);
							}
							optionResult += optionStr1;
							optionResult += item.getCodeValue();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getCodeName())) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getCodeValue())) {
								optionResult += optionStr3;
							} else
								optionResult += optionStr2;
							optionResult += item.getCodeName();
							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if (request.getParameter("comboType").equals("buffer4")){


					CommonCodeVO commonCode = new CommonCodeVO();
					commonCode.setParentCode("r_biz_dis");
					List<CommonCodeVO> codeList = commonCodeService.selectCommonCode(commonCode);

					if(codeList.size() > 0) {

						for(int i = 0; i < codeList.size(); i++) {
							CommonCodeVO item = codeList.get(i);

							optionResult += optionStr1;
							optionResult += item.getCodeValue();

							if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getCodeName())) {
								optionResult += optionStr3;
							} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getCodeValue())) {
								optionResult += optionStr3;
							} else
								optionResult += optionStr2;
							optionResult += item.getCodeName();
							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}  else if (request.getParameter("comboType").equals("etcConfig")) {

					optionResult += optionStr1;
					optionResult += "";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
					optionResult += optionStr4;

					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					if (!StringUtil.isNull(request.getParameter("comboType2"),true)) {
						etcConfigInfo.setGroupKey(request.getParameter("comboType2"));
					}
					List<EtcConfigInfo> etcConfigList = etcConfigInfoService.selectEtcConfigKey(etcConfigInfo);

					if(etcConfigList.size() > 0) {

						for(int i = 0; i < etcConfigList.size(); i++) {
							EtcConfigInfo item = etcConfigList.get(i);

							optionResult += optionStr1;
							if (StringUtil.isNull(request.getParameter("comboType2"),true))
								optionResult += item.getGroupKey();
							else
								optionResult += item.getConfigKey();

							optionResult += optionStr2;

							if (StringUtil.isNull(request.getParameter("comboType2"),true))
								optionResult += item.getGroupKey();
							else
								optionResult += item.getConfigKey();

							optionResult += optionStr4;
						}
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

					//녹취 보관 영구/일반
				}else if (request.getParameter("comboType").equals("buffer14")) {
					String optionResult2 = "";

					if(!StringUtil.isNull(request.getParameter("ALL"), true) && "all".equals(request.getParameter("ALL"))) {
						optionResult2 += optionStr1;
						optionResult2 += "";
						optionResult2 += optionStr2;
						optionResult2 += messageSource.getMessage("call.type.ALL", null,Locale.getDefault());
						optionResult2 += optionStr4;
					}

					optionResult2 += "<option value=\"1\">"+ "영구" + "</option>";
					optionResult2 += "<option value=\"2\">"+ "일반" + "</option></selected>";

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult2);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("Type")) {
					optionResult += optionStr1;
					optionResult += "am";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.am", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "lm";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.lm", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "recog";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.recog", null,Locale.getDefault());
					optionResult += optionStr4;

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("Type2")) {
					optionResult += optionStr1;
					optionResult += "am";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttEnginState.label.am", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "lm";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttEnginState.label.lm", null,Locale.getDefault());
					optionResult += optionStr4;
					
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("WorkStatus")) {
					optionResult += optionStr1;
					optionResult += "f";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.f", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "w";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.w", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "e";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.e", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "r";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttModel.label.r", null,Locale.getDefault());
					optionResult += optionStr4;

					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("WorkStatus2")) {
					optionResult += optionStr1;
					optionResult += "r";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttEnginState.label.r", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "u";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttEnginState.label.u", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "e";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttEnginState.label.e", null,Locale.getDefault());
					optionResult += optionStr4;
					
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				} else if(request.getParameter("comboType").equals("SttResult")) {
					optionResult += optionStr1;
					optionResult += "R";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttResult.type.R", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "CP";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttResult.type.CP", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "CC";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttResult.type.CC", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "SP";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttResult.type.SP", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "S";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttResult.type.S", null,Locale.getDefault());
					optionResult += optionStr4;
					
					optionResult += optionStr1;
					optionResult += "F";
					optionResult += optionStr2;
					optionResult += messageSource.getMessage("views.sttResult.type.F", null,Locale.getDefault());
					optionResult += optionStr4;
					
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				}
//			}else{
//				jRes.setResult("LOGIN FAIL");
//				jRes.setSuccess(AJaxResVO.SUCCESS_N);
//			}
		}catch (NullPointerException e) {
			logger.error("", e);
			jRes.setResult("FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}catch (Exception e) {
			logger.error("", e);
			jRes.setResult("FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
	return jRes;
	}


	// 좌측 메뉴 AJAX
	@RequestMapping(value="/menu.do", method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody AJaxResVO menu(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		AJaxResVO jRes = new AJaxResVO();
		if(userInfo != null) {
			String productName = "RecSee " + messageSource.getMessage("recsee.version", null,Locale.getDefault());
			String iconExtension = "svg";
			if(systemTemplates.equals("nx")) {
				productName = "nxVRS";
				iconExtension = "png";
			}

			MMenuAccessInfo menuAccessInfoChk = new MMenuAccessInfo();

			/**
			 * @creater : cypark
			 * @first date : 2015/08/17
			 * setLevelCode로 해당 등급에 대한 접근 메뉴를 DB에서 가져옴. "E1001"은 로그인한 사용자의 등급 코드(Level Code)를 사용해야 함. (수정 적용 필요)
			 */
			menuAccessInfoChk.setLevelCode(userInfo.getUserLevel());
			menuAccessInfoChk.setDisplayLevel(2);

			List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccessInfoChk);

			if(menuAccessList.size() > 0){
				jRes.setResult("1");
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("menuAccessList",menuAccessList);
			} else {
				jRes.setResult("0");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}else{
			jRes.setResult("LOGIN FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

















	/*// 좌측 메뉴
	@RequestMapping(value="/menu/menu.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody MenuCreate menu(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		MenuCreate xmls = null;

		if(userInfo != null) {
			String productName = "RecSee " + messageSource.getMessage("recsee.version", null,Locale.getDefault());
			String iconExtension = "svg";
			if(systemTemplates.equals("nx")) {
				productName = "nxVRS";
				iconExtension = "png";
			}

			MMenuAccessInfo menuAccessInfoChk = new MMenuAccessInfo();

			*//**
			 * @creater : cypark
			 * @first date : 2015/08/17
			 * setLevelCode로 해당 등급에 대한 접근 메뉴를 DB에서 가져옴. "E1001"은 로그인한 사용자의 등급 코드(Level Code)를 사용해야 함. (수정 적용 필요)
			 *//*
			menuAccessInfoChk.setLevelCode(userInfo.getUserLevel());
			menuAccessInfoChk.setDisplayLevel(1);

			List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccessInfoChk);

			int menuAccessListTotal = menuAccessList.size();

			*//**
			 * 등급 코드에 해당하는 접근 권한 항목이 있으면
			 *//*
			if (menuAccessListTotal>0){

				*//**
				 * Menu 생성 Xml 생성 클래스 생성.
				 * item 객체 배열 생성.
				 *//*
				xmls = new MenuCreate();
				xmls.setMenuFirstItem(new ArrayList<MenuFirstItem>());

				MenuFirstItem xmlFirst = null;
				MenuSecondItem xmlSecond = null;
				MenuThirdItem xmlThird = null;

				String prevMenuTop = "";			// 이전 최상위 메뉴명
				int prevDisplayLevel = -1;		// 이전 출력 등급
				String menuName = "menu.top.";
				*//**
				 * DB에서 가져온 Menu 접근 항목만큼 반복 처리
				 *//*
				for(int seqMenu = 0; seqMenu < menuAccessListTotal; seqMenu++) {
					*//**
					 * 가져온 Menu 객체 항목을 MMenuAccessInfo형 변수에 할당.
					 *//*
					MMenuAccessInfo menuItem = menuAccessList.get(seqMenu);

					*//**
					 * 이전 Top 메뉴와 현재 Top 메뉴가 다르고, 이전 메뉴 출력 등급이 '0'이면, 첫 Item 생성 및 속성 할당.
					 *//*
					if(!prevMenuTop.equals(menuItem.getProgramTop())) {
						if (seqMenu > 0) {
							xmls.getMenuFirstItem().add(xmlFirst);
							xmlFirst = null;
						}

						xmlFirst = new MenuFirstItem();
						xmlFirst.setAttrFirstId(menuItem.getProgramSrc());
						xmlFirst.setAttrFirstText(messageSource.getMessage(menuName + menuItem.getProgramSrc(), null,Locale.getDefault()));
						xmlFirst.setAttrFirstImg("icon_"+menuItem.getProgramSrc()+"." + iconExtension);
					}
					if(menuItem.getDisplayLevel() == 1) {
						if(prevDisplayLevel != menuItem.getDisplayLevel()) {
							xmlFirst.setMenuSecondItem(new ArrayList<MenuSecondItem>());
						}

						xmlSecond = new MenuSecondItem();

						xmlSecond.setAttrSecondId(menuItem.getProgramSrc());
						xmlSecond.setAttrSecondText(messageSource.getMessage(menuName + menuItem.getProgramSrc(), null,Locale.getDefault()));
						xmlSecond.setAttrSecondImg("icon_dot."+iconExtension);

						if(seqMenu == menuAccessListTotal-1 || !menuAccessList.get(seqMenu+1).equals("2")) {
							MMenuAccessInfo menuAccessThird = new MMenuAccessInfo();
							menuAccessThird.setLevelCode(userInfo.getUserLevel());
							menuAccessThird.setDisplayLevel(101);
							menuAccessThird.setProgramTop(menuItem.getProgramTop());
							menuAccessThird.setProgramSrc(menuItem.getProgramSrc());

							List<MMenuAccessInfo> menuAccessThirdList = menuAccessInfoService.selectMenuAccessInfo(menuAccessThird);
							int menuAccessThirdListTotal = menuAccessThirdList.size();
							if(menuAccessThirdListTotal > 0) {
								MMenuAccessInfo menuThiredItem = menuAccessThirdList.get(0);
								xmlSecond.getMenuSecondItemHref().add(request.getContextPath() + menuThiredItem.getProgramPath());
							} else
								xmlSecond.getMenuSecondItemHref().add(request.getContextPath() + menuItem.getProgramPath());

							xmlFirst.getMenuSecondItem().add(xmlSecond);
							xmlSecond = null;
						}
					}
					if(menuItem.getDisplayLevel() == 2) {
						if(prevDisplayLevel != menuItem.getDisplayLevel()) {
							xmlSecond.setMenuThirdItem(new ArrayList<MenuThirdItem>());
						}
						xmlThird = new MenuThirdItem();

						xmlThird.setAttrThirdId(menuItem.getProgramSrc());
						xmlThird.setAttrThirdText(messageSource.getMessage(menuName + menuItem.getProgramSrc(), null,Locale.getDefault()));
						//xmlThird.setAttrThirdImg("icon_"+menuItem.getProgramSrc()+".png");

						if(seqMenu == menuAccessListTotal-1 || menuAccessList.get(seqMenu+1).getDisplayLevel() != 3) {
							xmlThird.getMenuThirdItemHref().add(request.getContextPath() + menuItem.getProgramPath());

							xmlSecond.getMenuThirdItem().add(xmlThird);
							xmlThird = null;
						}
					}
					prevMenuTop = menuItem.getProgramTop();
					prevDisplayLevel = menuItem.getDisplayLevel();
				}
				if(prevDisplayLevel == 2) {
					xmlFirst.getMenuSecondItem().add(xmlSecond);
					xmls.getMenuFirstItem().add(xmlFirst);
				}
				if(prevDisplayLevel == 1) {
					xmlSecond = new MenuSecondItem();
					xmlSecond.setAttrSecondId("sp1");
					xmlSecond.setAttrSecondType("separator");
					xmlFirst.getMenuSecondItem().add(xmlSecond);
					xmlSecond = null;

					xmlSecond = new MenuSecondItem();
					xmlSecond.setAttrSecondId("version");
					xmlSecond.setAttrSecondText(productName);
					xmlSecond.setAttrSecondImg("icon_dot."+iconExtension);

					xmlFirst.getMenuSecondItem().add(xmlSecond);
					xmls.getMenuFirstItem().add(xmlFirst);
				}
			}
		}
		return xmls;
	}

	@RequestMapping(value = "/myinfo/account")
	public ModelAndView accountForm(HttpServletRequest request, Locale locale, Model model) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");

		if(userInfo != null) {
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);

			ModelAndView result = new ModelAndView();
			result.setViewName("/myinfo/account");

			return result;
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}*/

	@RequestMapping(value="/opt/organization_combo_option.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridCombo organization_combo_option(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridCombo xmls = new dhtmlXGridCombo();

		if(userInfo != null) {
			OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationInfoResult = null;

			if(request.getParameter("notAdmin") != null) {
				organizationInfo.setNotAdmin("Y");
			}

			if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("bgCode")) {
				if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
					organizationInfo.setrBgCode(request.getParameter("bgCode"));
				}
				organizationInfoResult = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("mgCode")) {
				if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
					organizationInfo.setrBgCode(request.getParameter("bgCode"));
				}
				if(request.getParameter("mgCode") != null && !request.getParameter("mgCode").isEmpty()) {
					organizationInfo.setrMgCode(request.getParameter("mgCode"));
				}
				organizationInfoResult = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("sgCode")) {
				if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").isEmpty()) {
					organizationInfo.setrBgCode(request.getParameter("bgCode"));
				}
				if(request.getParameter("mgCode") != null && !request.getParameter("mgCode").isEmpty()) {
					organizationInfo.setrMgCode(request.getParameter("mgCode"));
				}
				if(request.getParameter("sgCode") != null && !request.getParameter("sgCode").isEmpty()) {
					organizationInfo.setrSgCode(request.getParameter("sgCode"));
				}
				organizationInfoResult = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
			}

			Integer organizationInfoTotal = 0;
			if(organizationInfoResult != null) {
				organizationInfoTotal = organizationInfoResult.size();
			}

			xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

			dhtmlXGridComboOption optionItem = null;

			/*if(request.getParameter("subOpt") != null && request.getParameter("subOpt").equals("ALL")) {
				optionItem = new dhtmlXGridComboOption();

				optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("call.type.ALL", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);
			}*/
			if(organizationInfoResult!=null && organizationInfoTotal > 0) {

				for(int i = 1; i <= organizationInfoTotal; i++) {
					OrganizationInfo item = organizationInfoResult.get(i-1);

					optionItem = new dhtmlXGridComboOption();

					if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("bgCode")) {
						if(item.getrBgCode() != null && !item.getrBgCode().isEmpty()) {
							optionItem.setValue(item.getrBgCode());
						}
						if(item.getrBgName() != null && !item.getrBgName().isEmpty()) {
							optionItem.setValueElement(item.getrBgName());
						}
						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrBgName())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrBgCode())) {
							optionItem.setSelected("1");
						}
					} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("mgCode")) {
						if(item.getrMgCode() != null && !item.getrMgCode().isEmpty()) {
							optionItem.setValue(item.getrMgCode());
						}
						if(item.getrMgName() != null && !item.getrMgName().isEmpty()) {
							optionItem.setValueElement(item.getrMgName());
						}
						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrMgName())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrMgCode())) {
							optionItem.setSelected("1");
						}
					} else if(request.getParameter("comboType") != null && request.getParameter("comboType").equals("sgCode")) {
						if(item.getrSgCode() != null && !item.getrSgCode().isEmpty()) {
							optionItem.setValue(item.getrSgCode());
						}
						if(item.getrSgName() != null && !item.getrSgName().isEmpty()) {
							optionItem.setValueElement(item.getrSgName());
						}
						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrSgName())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrSgCode())) {
							optionItem.setSelected("1");
						}
					}
					xmls.getValueAttr().add(optionItem);
				}
			}

		}
		return xmls;
	}

	@RequestMapping(value="/opt/combo_option.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridCombo combo_option(HttpServletRequest request, HttpServletResponse response) throws SocketException {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridCombo xmls = new dhtmlXGridCombo();

		if(userInfo != null || (request.getParameter("exception") != null && request.getParameter("exception").equals("internal"))) {

			if(request.getParameter("comboType").equals("system")) {
				SystemInfo systemInfo = new SystemInfo();

				List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);
				int systemInfoTotal = systemInfoList.size();

				if(systemInfoTotal > 0) {
					xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

					for(int i = 0; i < systemInfoTotal; i++) {
						SystemInfo sysItem = systemInfoList.get(i);

						dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
						optionItem.setValue(sysItem.getSysId());
						optionItem.setValueElement(sysItem.getSysName());

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(sysItem.getSysId())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(sysItem.getSysName())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			} else if(request.getParameter("comboType").equals("callType")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				/*optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("call.type.ALL", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);*/

				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("CALL_TYPE");
				List<EtcConfigInfo> callTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				String callType = "I,O";
				if(callTypeResult.size() > 0) {
					callType = callTypeResult.get(0).getConfigValue();
				}
				String[] callTypeArray = callType.split(",");

				for(int i = 0; i < callTypeArray.length; i++) {
					optionItem = new dhtmlXGridComboOption();
					optionItem.setValue(callTypeArray[i]);
					optionItem.setValueElement(messageSource.getMessage("call.type."+callTypeArray[i], null,Locale.getDefault()));

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("call.type."+callTypeArray[i], null,Locale.getDefault()))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(callTypeArray[i])) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}

			} else if(request.getParameter("comboType").equals("callKind")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("A");
				optionItem.setValueElement(messageSource.getMessage("call.rec.type.acr", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("S");
				optionItem.setValueElement(messageSource.getMessage("call.rec.type.odr", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("P");
				optionItem.setValueElement(messageSource.getMessage("call.rec.type.tacr", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("T");
				optionItem.setValueElement(messageSource.getMessage("call.rec.type.tcr", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

		} else if(request.getParameter("comboType").equals("rbuffer4")) {
			xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

			dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();

			optionItem = new dhtmlXGridComboOption();
			optionItem.setValue("1");
			optionItem.setValueElement(messageSource.getMessage("call.product.type.1", null,Locale.getDefault()));

			xmls.getValueAttr().add(optionItem);

			optionItem = new dhtmlXGridComboOption();
			optionItem.setValue("2");
			optionItem.setValueElement(messageSource.getMessage("call.product.type.2", null,Locale.getDefault()));

			xmls.getValueAttr().add(optionItem);

			optionItem = new dhtmlXGridComboOption();
			optionItem.setValue("3");
			optionItem.setValueElement(messageSource.getMessage("call.product.type.3", null,Locale.getDefault()));

			xmls.getValueAttr().add(optionItem);
			
			optionItem = new dhtmlXGridComboOption();
			optionItem.setValue("4");
			optionItem.setValueElement(messageSource.getMessage("call.product.type.4", null,Locale.getDefault()));

			xmls.getValueAttr().add(optionItem);
			
			optionItem = new dhtmlXGridComboOption();
			optionItem.setValue("5");
			optionItem.setValueElement(messageSource.getMessage("call.product.type.5", null,Locale.getDefault()));

			xmls.getValueAttr().add(optionItem);

		}else if(request.getParameter("comboType").equals("YN")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				/*optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("call.type.ALL", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);*/

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("Y");
				optionItem.setValueElement(messageSource.getMessage("use.yes", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(1))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("use.yes", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("Y")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("N");
				optionItem.setValueElement(messageSource.getMessage("use.no", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(2))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("use.no", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("N")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);

			} else if(request.getParameter("comboType").equals("YN2")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("Y");
				optionItem.setValueElement(messageSource.getMessage("use.yes", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("N");
				optionItem.setValueElement(messageSource.getMessage("use.no", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

			} else if(request.getParameter("comboType").equals("YN3")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("Y");
				optionItem.setValueElement(messageSource.getMessage("admin.subNumber.label.selY", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("N");
				optionItem.setValueElement(messageSource.getMessage("admin.subNumber.label.selN", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);
			} else if(request.getParameter("comboType").equals("YN4")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("Y");
				optionItem.setValueElement(messageSource.getMessage("admin.phoneMapping.label.selY", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("N");
				optionItem.setValueElement(messageSource.getMessage("admin.phoneMapping.label.selN", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);
			} else if(request.getParameter("comboType").equals("YN5")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("Y");
				optionItem.setValueElement("완료");

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("N");
				optionItem.setValueElement("재시도");

				xmls.getValueAttr().add(optionItem);

			} else if(request.getParameter("comboType").equals("YN6")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("Y");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("N");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);
			} else if(request.getParameter("comboType").equals("schedule")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				
				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("d");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.option.daily", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("w");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.option.weekly", null,Locale.getDefault()));

				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("m");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.option.monthly", null,Locale.getDefault()));
				
				xmls.getValueAttr().add(optionItem);
			} else if(request.getParameter("comboType").equals("day")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				
				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("*");
				optionItem.setValueElement("*");
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("1");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.sunday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("2");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.monday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("3");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.tuesday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("4");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.wednesday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("5");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.thursday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("6");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.friday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
				
				optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("7");
				optionItem.setValueElement(messageSource.getMessage("admin.detail.label.week.saturday", null,Locale.getDefault()));
				xmls.getValueAttr().add(optionItem);
			} else if(request.getParameter("comboType").equals("channel")) {
				ChannelInfo channelInfo = new ChannelInfo();

				List<ChannelInfo> channelInfoList = channelInfoService.groupChannelInfo(channelInfo);
				int channelInfoTotal = channelInfoList.size();

				if(channelInfoTotal > 0) {

					xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

					dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();

					optionItem.setValue("");
					optionItem.setValueElement("");

					xmls.getValueAttr().add(optionItem);

					for(int i = 0; i < channelInfoTotal; i++) {
						ChannelInfo channelItem = channelInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						String extNum = channelItem.getExtNum();

						if(extNum == null) extNum = "";

						optionItem.setValue(extNum);
						optionItem.setValueElement(extNum);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(channelItem.getExtNum())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(channelItem.getExtNum())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			}	/*else if(request.getParameter("comboType").equals("sbgList")) {

				List<SheetInfo> itemlInfoList = evaluationService.sbgList(request.getParameter("sheetCode"));
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.getItemCode();
						itemCon = evItemList.getItemContent();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			} else if(request.getParameter("comboType").equals("smgList")) {

				List<SheetInfo> itemlInfoList = evaluationService.smgList(request.getParameter("sheetCode"));
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.getItemCode();
						itemCon = evItemList.getItemContent();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			}else if(request.getParameter("comboType").equals("ssgList")) {

				List<SheetInfo> itemlInfoList = evaluationService.ssgList(request.getParameter("sheetCode"));
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.getItemCode();
						itemCon = evItemList.getItemContent();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			} else if(request.getParameter("comboType").equals("itemList")) {

				List<SheetInfo> itemlInfoList = evaluationService.itemList(request.getParameter("sheetCode"));
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.getItemCode();
						itemCon = evItemList.getItemContent();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			}else if(request.getParameter("comboType").equals("subItemList")) {

				List<SheetInfo> itemlInfoList = evaluationService.subItemList(request.getParameter("itemCode"));
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.getItemCode();
						itemCon = evItemList.getItemContent();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			}else if(request.getParameter("comboType").equals("campaignList")) {

				List<SheetInfo> itemlInfoList = evaluationService.selectCampList();
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				} else if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("All")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "";
					itemCon = messageSource.getMessage("call.type.ALL", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.geteCampCode();
						itemCon = evItemList.getSheetName();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			}else if(request.getParameter("comboType").equals("sheetList")) {
				SheetInfo tmpEvtItem=new SheetInfo();
				List<SheetInfo> itemlInfoList = evaluationService.selectSheetList(tmpEvtItem);
				int itemlListTotal = itemlInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String itemCode = null;
				String itemCon = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "default";
					itemCon = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				} else if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("All")) {
					optionItem = new dhtmlXGridComboOption();

					itemCode = "";
					itemCon = messageSource.getMessage("call.type.ALL", null,Locale.getDefault());

					optionItem.setValue(itemCode);
					optionItem.setValueElement(itemCon);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(itemlListTotal > 0) {

					for(int i = 0; i < itemlListTotal; i++) {
						SheetInfo evItemList = itemlInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						itemCode = evItemList.geteCampCode();
						itemCon = evItemList.getSheetName();

						optionItem.setValue(itemCode);
						optionItem.setValueElement(itemCon);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(evItemList.getItemContent())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evItemList.getItemCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			}*/  else if(request.getParameter("comboType").equals("authy")) {

				MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();

				List<MAccessLevelInfo> accessLevelInfoList = accessLevelInfoService.selectAccessLevelInfo(accessLevelInfo);
				int accessLevelTotal = accessLevelInfoList.size();

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = null;

				String levelCode = null;
				String levelName = null;

				if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("default")) {
					optionItem = new dhtmlXGridComboOption();

					levelCode = "default";
					levelName = messageSource.getMessage("message.combo.default", null,Locale.getDefault());

					optionItem.setValue(levelCode);
					optionItem.setValueElement(levelName);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}

				if(accessLevelTotal > 0) {

					for(int i = 0; i < accessLevelTotal; i++) {
						MAccessLevelInfo accessLevelItem = accessLevelInfoList.get(i);

						optionItem = new dhtmlXGridComboOption();

						levelCode = accessLevelItem.getLevelCode();
						levelName = accessLevelItem.getLevelName();

						optionItem.setValue(levelCode);
						optionItem.setValueElement(levelName);

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(accessLevelItem.getLevelName())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(accessLevelItem.getLevelCode())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			} else if(request.getParameter("comboType").equals("grade")) {

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();

				String gradeCode = "A";
				String gradeName = messageSource.getMessage("access.grade.A", null,Locale.getDefault());

				optionItem.setValue(gradeCode);
				optionItem.setValueElement(gradeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("access.grade.A", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("A")) {
					optionItem.setSelected("1");
				}
				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();

				gradeCode = "B";
				gradeName = messageSource.getMessage("access.grade.B", null,Locale.getDefault());

				optionItem.setValue(gradeCode);
				optionItem.setValueElement(gradeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(1))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("access.grade.B", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("B")) {
					optionItem.setSelected("1");
				}
				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();

				gradeCode = "M";
				gradeName = messageSource.getMessage("access.grade.M", null,Locale.getDefault());

				optionItem.setValue(gradeCode);
				optionItem.setValueElement(gradeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(2))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("access.grade.M", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("M")) {
					optionItem.setSelected("1");
				}
				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();

				gradeCode = "S";
				gradeName = messageSource.getMessage("access.grade.S", null,Locale.getDefault());

				optionItem.setValue(gradeCode);
				optionItem.setValueElement(gradeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(3))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("access.grade.S", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("S")) {
					optionItem.setSelected("1");
				}
				xmls.getValueAttr().add(optionItem);

				optionItem = new dhtmlXGridComboOption();

				gradeCode = "U";
				gradeName = messageSource.getMessage("access.grade.U", null,Locale.getDefault());

				optionItem.setValue(gradeCode);
				optionItem.setValueElement(gradeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(4))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("access.grade.U", null,Locale.getDefault()))) {
					optionItem.setSelected("1");
				} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals("U")) {
					optionItem.setSelected("1");
				}
				xmls.getValueAttr().add(optionItem);
				

				
				optionItem = new dhtmlXGridComboOption();

				gradeCode = "allowableAdd";
				gradeName = messageSource.getMessage("access.grade.add", null,Locale.getDefault());/*허용추가*/

				optionItem.setValue(gradeCode);
				optionItem.setValueElement(gradeName);

				xmls.getValueAttr().add(optionItem);
				
				// 추가된 허용범위(멀티권한)이 있는 경우
				MAccessLevelInfo accessLevelInfo = new MAccessLevelInfo();
				List<MAccessLevelInfo> allowableListInfoList = accessLevelInfoService.selectAllowableInfo(accessLevelInfo);
				if (allowableListInfoList.size() > 0) {
					for(int i=0; i<allowableListInfoList.size(); i++) {
						optionItem = new dhtmlXGridComboOption();
						optionItem.setValue(allowableListInfoList.get(i).getLevelCode());
						optionItem.setValueElement(allowableListInfoList.get(i).getLevelName());
						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(5+i))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(allowableListInfoList.get(i).getLevelName())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(allowableListInfoList.get(i).getLevelCode())) {
							optionItem.setSelected("1");
						}
						xmls.getValueAttr().add(optionItem);
					}
				}
				
				

			} else if(request.getParameter("comboType").equals("Time") || request.getParameter("comboType").equals("rTime")) {

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();

				String szTime = "";
				String szTimeName = "";

				optionItem.setValue(szTime);
				optionItem.setValueElement(szTimeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);

				for(int i = 0; i < 24; i++) {
					optionItem = new dhtmlXGridComboOption();

					szTime = String.format("%02d:00:00", i);
					szTimeName = String.format("%02d:00:00", i);

					if(request.getParameter("comboType").equals("Time")) {
						if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("e")) {
							szTime = String.format("%02d:59:59", i);
							szTimeName = String.format("%02d:59:59", i);
						}
					} else {
						if(request.getParameter("comboType2") != null && request.getParameter("comboType2").equals("e")) {
							szTime = String.format("%02d:59:59", i);
						}
						szTimeName = String.format("%02d", i);
					}

					optionItem.setValue(szTime);
					optionItem.setValueElement(szTimeName);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(szTimeName)) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(szTime)) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}
			} else if(request.getParameter("comboType").equals("tTime")) {

				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("TTIME_RANGE");

				List<EtcConfigInfo> tTimeRangeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				Integer tTimeRangeResultTotal = tTimeRangeResult.size();

				String[] tTimeRange = null;
				if(tTimeRangeResultTotal < 1) {
					tTimeRange = "10,30,60,300,600".split(",");
				} else {
					tTimeRange = tTimeRangeResult.get(0).getConfigValue().split(",");
				}


				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();

				String szTime = "";
				String szTimeName = "";

				/*optionItem.setValue(szTime);
				optionItem.setValueElement(szTimeName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);*/

				for(int i = 0; i < tTimeRange.length; i++) {
					optionItem = new dhtmlXGridComboOption();

					szTime = tTimeRange[i];
					szTimeName = new RecSeeUtil().getSecToTime(Integer.parseInt(tTimeRange[i]));

					optionItem.setValue(szTime);
					optionItem.setValueElement(szTimeName);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(szTimeName)) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(szTime)) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}
			} else if(request.getParameter("comboType").equals("evalStatus")) {

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();

				String szStatus = "";
				String szStatusName = "";

				/*optionItem.setValue(szStatus);
				optionItem.setValueElement(szStatusName);

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(0))) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);*/

				String[] evalOptValue = messageSource.getMessage("evaluation.value.option.evalStatus", null,Locale.getDefault()).split(",");
				String[] evalOptText = messageSource.getMessage("evaluation.text.option.evalStatus", null,Locale.getDefault()).split(",");

				for(int i = 0; i < evalOptValue.length; i++) {
					optionItem = new dhtmlXGridComboOption();

					szStatus = evalOptValue[i];
					szStatusName = evalOptText[i];

					optionItem.setValue(szStatus);
					optionItem.setValueElement(szStatusName);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(szStatusName)) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(evalOptText)) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}
			} else if(request.getParameter("comboType").equals("startType")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				/*optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("call.type.ALL", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);*/

				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("START_TYPE");
				List<EtcConfigInfo> callTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				String callType = "T";
				if(callTypeResult.size() > 0) {
					callType = callTypeResult.get(0).getConfigValue();
				}
				String[] callTypeArray = callType.split(",");

				for(int i = 0; i < callTypeArray.length; i++) {
					optionItem = new dhtmlXGridComboOption();
					optionItem.setValue(callTypeArray[i]);
					optionItem.setValueElement(messageSource.getMessage("call.type.startType."+callTypeArray[i], null,Locale.getDefault()));

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("call.type."+callTypeArray[i], null,Locale.getDefault()))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(callTypeArray[i])) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}

			}else if(request.getParameter("comboType").equals("gridRow")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				/*optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("call.type.ALL", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);*/

				for(int i = 2; i <=  60; i++) {
					optionItem = new dhtmlXGridComboOption();
					optionItem.setValue(Integer.toString(i));
					optionItem.setValueElement(Integer.toString(i));

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(Integer.toString(i))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(Integer.toString(i))) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}

			}else if(request.getParameter("comboType").equals("filePattern")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("message.combo.select", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);

				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("SYSTEM");
				etcConfigInfo.setConfigKey("FILE_PATTERN");
				List<EtcConfigInfo> filePatternResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				String filePattern = "";
				if(filePatternResult.size() > 0) {
					filePattern = filePatternResult.get(0).getConfigValue();
				}
				String[] filePatternArray = filePattern.split(",");

				for(int i = 0; i < filePatternArray.length; i++) {
					optionItem = new dhtmlXGridComboOption();
					optionItem.setValue(filePatternArray[i]);
					optionItem.setValueElement(messageSource.getMessage("combo.filePattern."+filePatternArray[i], null,Locale.getDefault()));

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(messageSource.getMessage("combo.filePattern."+filePatternArray[i], null,Locale.getDefault()))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(filePatternArray[i])) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}
			} else if(request.getParameter("comboType").equals("ethernetCard")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("message.combo.select", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);

				Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
				for(NetworkInterface netint : Collections.list(nets)) {
					if(netint.isLoopback() == false && netint.isUp() == true ) {
						optionItem = new dhtmlXGridComboOption();
						optionItem.setValue(netint.getName().toUpperCase());
						optionItem.setValueElement(netint.getName().toUpperCase() + " " + netint.getDisplayName());

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(netint.getName().toUpperCase())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(netint.getName().toUpperCase())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			} else if(request.getParameter("comboType").equals("timezone")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("message.combo.select", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}

				xmls.getValueAttr().add(optionItem);

				String[] ids = TimeZone.getAvailableIDs();
				for(String id : ids) {
					TimeZone tz = TimeZone.getTimeZone(id);
					long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
					long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
									- TimeUnit.HOURS.toMinutes(hours);

					minutes = Math.abs(minutes);

					String tzResult = "";
					if(hours > 0) {
						tzResult = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
					} else {
						tzResult = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
					}

					optionItem = new dhtmlXGridComboOption();
					optionItem.setValue(tz.getID());
					optionItem.setValueElement(tzResult);

					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("1")) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(tzResult)) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(tz.getID())) {
						optionItem.setSelected("1");
					}

					xmls.getValueAttr().add(optionItem);
				}
			} else if (request.getParameter("comboType").equals("common")) {

				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				/*optionItem.setValue("");
				optionItem.setValueElement(messageSource.getMessage("message.combo.select", null,Locale.getDefault()));

				if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
					optionItem.setSelected("1");
				}*/

				xmls.getValueAttr().add(optionItem);

				CommonCodeVO commonCode = new CommonCodeVO();
				commonCode.setParentCode(request.getParameter("comboType2"));
				List<CommonCodeVO> codeList = commonCodeService.selectCommonCode(commonCode);

				if(codeList.size() > 0) {

					for(int i = 0; i < codeList.size(); i++) {
						CommonCodeVO item = codeList.get(i);

						optionItem = new dhtmlXGridComboOption();

						optionItem.setValue(item.getCodeValue());
						optionItem.setValueElement(item.getCodeName());

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getCodeName())) {
							optionItem.setSelected("1");
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getCodeValue())) {
							optionItem.setSelected("1");
						}

						xmls.getValueAttr().add(optionItem);
					}
				}
			} else if (request.getParameter("comboType").equals("volume")) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
				
				for(int i = 0; i < 3; i++) {
					optionItem = new dhtmlXGridComboOption();
					optionItem.setValue((i+1)+"");
					optionItem.setValueElement((i+1) + "배");

					xmls.getValueAttr().add(optionItem);
				}
			}
		}
		return xmls;
	}


	@RequestMapping(value = "/common/message_proc", produces="text/html;charset=UTF-8")
	public ModelAndView common_messges(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("msg", request.getParameter("msg"));

		ModelAndView result = new ModelAndView();
		result.setViewName("/common/message_proc");

		return result;

	}

	/*@RequestMapping(value = "/interface/VocInterface.do", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody AJaxResVO voc_interface(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, ParseException {
		AJaxResVO jRes = new AJaxResVO();

		EtcConfigInfo vocInfo = new EtcConfigInfo();
		vocInfo.setGroupKey("VocInterfece");
		List<EtcConfigInfo> vocInfoResult = etcConfigInfoService.selectEtcConfigInfo(vocInfo);
		if(vocInfoResult.size()>0) {
			String ServerIp = "";
			Integer ServerPort = 0;

			for(int i = 0; i < vocInfoResult.size(); i++) {
				EtcConfigInfo item = vocInfoResult.get(i);
				if(item.getConfigKey().equals("ServerIp") && !item.getConfigValue().isEmpty()) {
					ServerIp = item.getConfigValue();
				}
				if(item.getConfigKey().equals("ServerPort") && !item.getConfigValue().isEmpty()) {
					ServerPort = Integer.parseInt(item.getConfigValue());
				}
			}
			if(ServerIp.isEmpty()) {
				jRes.setResult("0");
				jRes.addAttribute("result", "Server Ip empty");

				return jRes;
			}
			if(ServerPort == 0) {
				jRes.setResult("0");
				jRes.addAttribute("result", "Server Port empty");

				return jRes;
			}

			TcpClient testClient = new TcpClient();
			String connectResult = testClient.serverConnect(ServerIp, ServerPort);
			if(connectResult.equals("error")) {
				jRes.setResult("0");
				jRes.addAttribute("result", connectResult);

				return jRes;
			}
			String result = testClient.serverSendMsg("test");
			if(result.equals("error")) {
				jRes.setResult("0");
				jRes.addAttribute("result", result);

				return jRes;
			} else {
				System.out.print("tcp socket test end");

				jRes.setResult("1");
				jRes.addAttribute("result", result);

				return jRes;
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("result", "no info");

			return jRes;
		}
	}

	// context Menu
	@RequestMapping(value="/menu/context_menu.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXMenu context_menu(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXMenu xmls = new dhtmlXMenu();

		if(userInfo != null) {
			if(!request.getParameter("proc").trim().isEmpty()) {
				if(request.getParameter("proc").trim().equals("group")) {
					xmls.setItemElement(new ArrayList<dhtmlXMenuItem>());

					dhtmlXMenuItem item = new dhtmlXMenuItem();
					item.setId("new");
					item.setText(messageSource.getMessage("message.btn.add", null, Locale.getDefault()));

					xmls.getItemElement().add(item);

					item = new dhtmlXMenuItem();

					item.setId("modify");
					item.setText(messageSource.getMessage("message.btn.modify", null, Locale.getDefault()));

					xmls.getItemElement().add(item);

					item = new dhtmlXMenuItem();

					item.setId("delete");
					item.setText(messageSource.getMessage("message.btn.del", null, Locale.getDefault()));

					xmls.getItemElement().add(item);

				} else if(request.getParameter("proc").trim().equals("all")) {
					xmls.setItemElement(new ArrayList<dhtmlXMenuItem>());

					dhtmlXMenuItem item = new dhtmlXMenuItem();
					item.setId("allChecked");
					item.setText(messageSource.getMessage("contextMenu.allChecked", null, Locale.getDefault()));

					xmls.getItemElement().add(item);

					item = new dhtmlXMenuItem();

					item.setId("allUnChecked");
					item.setText(messageSource.getMessage("contextMenu.allUnChecked", null, Locale.getDefault()));

					xmls.getItemElement().add(item);

				}

			}
		}
		return xmls;
	}*/

	@RequestMapping(value="/opt/user_combo_option.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridCombo user_combo_option(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridCombo xmls = new dhtmlXGridCombo();
		List<RUserInfo> ruserInfoResult=null;
		
		if(userInfo != null) {
			RUserInfo ruserInfo = new RUserInfo();

			if(request.getParameter("bgCode")!=null&&!request.getParameter("bgCode").equals("null")) {
				ruserInfo.setBgCode(request.getParameter("bgCode"));
			}else {
				/*EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("CALLCENTER");
				etcConfigInfo.setConfigKey("CALLCENTER");
				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

				String[] lista = etcConfigResult.get(0).getConfigValue().split(",");
				List<String> list = new ArrayList<String>();

				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}
				ruserInfo.setList(list);*/
			}

			if(request.getParameter("mgCode")!=null&&!request.getParameter("mgCode").equals("null"))
				ruserInfo.setMgCode(request.getParameter("mgCode"));
			if(request.getParameter("sgCode")!=null&&!request.getParameter("sgCode").equals("null"))
				ruserInfo.setSgCode(request.getParameter("sgCode"));
			if(request.getParameter("userId")!=null&&!request.getParameter("userId").equals("null"))
				ruserInfo.setUserId(request.getParameter("userId"));
			if(request.getParameter("mobile")!=null&&!request.getParameter("mobile").equals("null"))
				ruserInfo.setMobileYN(request.getParameter("mobile"));

			if(request.getParameter("bgCode")!=null) {
				ruserInfoResult = ruserInfoService.selectPeople(ruserInfo);
			}else {
				ruserInfoResult = ruserInfoService.selectRUserInfo(ruserInfo);
			}

			Integer ruserInfoTotal = ruserInfoResult.size();

			if(ruserInfoTotal > 0) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = null;

				for(int i = 0; i < ruserInfoTotal; i++) {
					RUserInfo item = ruserInfoResult.get(i);

					optionItem = new dhtmlXGridComboOption();

					if(request.getParameter("bgCode")!=null) {
						optionItem.setValue(item.getUserId());
						optionItem.setValueElement(item.getUserId() +" "+item.getUserName());
					}else {
						optionItem.setValue(item.getUserId());
						optionItem.setValueElement(item.getUserId() +" "+item.getUserName());
					}
					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getUserName())) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getUserId())) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}
			}
		}
		return xmls;
	}

	@RequestMapping(value="/opt/queue_combo_option.xml", method=RequestMethod.GET, produces="application/xml; charset=utf8")
	public @ResponseBody dhtmlXGridCombo queue_combo_option(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridCombo xmls = new dhtmlXGridCombo();

		if(userInfo != null || (request.getParameter("exception") != null && request.getParameter("exception").equals("internal"))) {
			QueueInfo queueInfo = new QueueInfo();
			List<QueueInfo> queueInfoResult = queueInfoService.selectQueueInfo(queueInfo);

			Integer queueInfoTotal = queueInfoResult.size();

			if(queueInfoTotal > 0) {
				xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());

				dhtmlXGridComboOption optionItem = null;

				optionItem = new dhtmlXGridComboOption();

				/*if(request.getParameter("type") != null && request.getParameter("type").equals("ALL")) {
					optionItem.setValue("");
					optionItem.setValueElement(messageSource.getMessage("combo.ALL", null, Locale.getDefault()));
					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}*/

				for(int i = 0; i < queueInfoTotal; i++) {
					QueueInfo item = queueInfoResult.get(i);

					optionItem = new dhtmlXGridComboOption();

					optionItem.setValue(item.getrQueueNum());
					optionItem.setValueElement(item.getrQueueName());
					if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrQueueName())) {
						optionItem.setSelected("1");
					} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrQueueNum())) {
						optionItem.setSelected("1");
					}
					xmls.getValueAttr().add(optionItem);
				}
			}
		}
		return xmls;
	}

	@RequestMapping(value="/queueSelectOption.do", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody AJaxResVO queueSelectOption(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null || (request.getParameter("exception") != null && request.getParameter("exception").equals("internal"))) {

				String optionResult = "";
				String optionStr1 = "<option value=\"";
				String optionStr2 = "\">";
				String optionStr3 = "\" selected>";
				String optionStr4 = "</option>";

				QueueInfo queueInfo = new QueueInfo();
				List<QueueInfo> queueInfoResult = queueInfoService.selectQueueInfo(queueInfo);

				Integer queueInfoTotal = queueInfoResult.size();

				if(queueInfoTotal > 0) {

					optionResult += optionStr1;

					/*if(request.getParameter("type") != null && request.getParameter("type").equals("ALL")) {

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals("0")) {
							optionResult += optionStr3;
						}
						optionResult+=optionStr2;
						optionResult+=messageSource.getMessage("combo.ALL", null, Locale.getDefault());
						optionResult+=optionStr4;
					}*/

					for(int i = 0; i < queueInfoTotal; i++) {
						QueueInfo item = queueInfoResult.get(i);

						optionResult += optionStr1;
						optionResult += item.getrQueueNum();

						if (request.getParameter("selectedIdx") != null && request.getParameter("selectedIdx").equals(String.valueOf(i+1))) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedName") != null && request.getParameter("selectedName").equals(item.getrQueueName())) {
							optionResult += optionStr3;
						} else if (request.getParameter("selectedValue") != null && request.getParameter("selectedValue").equals(item.getrQueueNum())) {
							optionResult += optionStr3;
						}
						optionResult += optionStr2;
						optionResult += item.getrQueueName();
						optionResult += optionStr4;
					}
					jRes.setResult("SUCCESS");
					jRes.addAttribute("optionResult", optionResult);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				}
			}else{
				jRes.setResult("LOGIN FAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (NullPointerException e) {
			logger.error("", e);
			jRes.setResult("FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}catch (Exception e) {
			logger.error("", e);
			jRes.setResult("FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	// 에러 페이지 처리
	@RequestMapping(value = "/error/error")
	public ModelAndView errorerror(HttpServletRequest request, Locale local, Model model) {
		ModelAndView result = new ModelAndView();
		result.setViewName("/error/error");
		return result;
	}

//	@RequestMapping(value = "/h/{pageName}")
//	public ModelAndView h(@PathVariable("pageName") String pageName, HttpServletRequest request, HttpServletResponse response) {
//
//		ModelAndView result = new ModelAndView();
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		if(userInfo != null) {
//			if ("admin".equals(userInfo.getUserId())) {
//				result.setViewName("/h/"+pageName);
//				return result;
//			}else {
//				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
//				rv.setExposeModelAttributes(false);
//				return new ModelAndView(rv);
//			}
//		}else {
//			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
//			rv.setExposeModelAttributes(false);
//			return new ModelAndView(rv);
//		}
//	}

	@RequestMapping(value="/downloadLog.do", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody AJaxResVO downloadLog(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		String recDate = DateUtil.toString(new Date(),"yyyyMMdd");
		String recTime = DateUtil.toString(new Date());
		recTime = recTime.substring(recTime.length()-6,recTime.length());
		String UserName = userInfo.getUserId();
		String recvFileName = "";
		String recvFileEnc = "";
		String fileName="";
		String reason="";
		
		if(!StringUtil.isNull(request.getParameter("recvFileName"))) {
			recvFileName = request.getParameter("recvFileName");
			
			String [] fileNametemp = recvFileName.split(",");
			
			for(int i = 0; i<fileNametemp.length;i++) {
				if(i+1==fileNametemp.length)
					fileName+=fileNametemp[i].split("url=")[1];
				else
					fileName+=fileNametemp[i].split("url=")[1]+",";
			}
		}
		if(!StringUtil.isNull(request.getParameter("recvFileEnc"))) {
			recvFileEnc = request.getParameter("recvFileEnc");
		}
		if(!StringUtil.isNull(request.getParameter("reasonStr"))) {
			reason = XssFilterUtil.XssFilter(request.getParameter("reasonStr"));
		}
		String LogStr = "Download File Info [ "+
				(recDate != null ? 		" Download Date="+recDate 			: "")+
				(recTime != null ? 		" Download Time="+recTime 			: "")+
				(UserName != null ? 	" UserName="+UserName 		: "")+
				(!"".equals(recvFileEnc) ? " Encryption="+recvFileEnc 	: "")+
				(!"".equals(fileName) ? " FileName="+ fileName:"")+
				(!"".equals(reason) ? " Reason="+ reason:"")+
				" ]";

		logService.writeLog(request, "FILEDOWN", "DO", LogStr);

		return jRes;
	}

	@RequestMapping(value="/excelLog.do", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody AJaxResVO excelLog(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		String recDate = DateUtil.toString(new Date(),"yyyyMMdd");
		String recTime = DateUtil.toString(new Date());
		recTime = recTime.substring(recTime.length()-6,recTime.length());
		String UserName = userInfo.getUserId();
		String fileName = "";
		String sDate = "";
		String eDate = "";
		String searchType = "";
		String sysCode = "";
		String codeFilter = "";
		String codeFilterResult = "";

		if(!StringUtil.isNull(request.getParameter("fileName"))) {
			if("AllCallStatisticsList".equals(request.getParameter("fileName")))
				fileName = "그룹 콜 통계";
			if("UserCallStatisticsList".equals(request.getParameter("fileName")))
				fileName = "사용자별 콜 통계";
		}
		if(!StringUtil.isNull(request.getParameter("sDate"))) {
			sDate = request.getParameter("sDate");
		}
		if(!StringUtil.isNull(request.getParameter("eDate"))) {
			eDate = request.getParameter("eDate");
		}

		if(!StringUtil.isNull(request.getParameter("dayTimeBy"))) {
			searchType = request.getParameter("dayTimeBy");
		}
		if(!StringUtil.isNull(request.getParameter("sysCode")) && !request.getParameter("sysCode").equals("undefined")) {
			sysCode = request.getParameter("sysCode");
		}
		if(!StringUtil.isNull(request.getParameter("codeFilter"))) {
			codeFilter = request.getParameter("codeFilter");
		}
		if(!StringUtil.isNull(request.getParameter("codeFilterResult"))) {
			codeFilterResult = request.getParameter("codeFilterResult");
		}

		String LogStr = "Search Condition Info [ "+
				(recDate != null ? 		" Download Date="+recDate 			: "")+
				(recTime != null ? 		" Download Time="+recTime 			: "")+
				(UserName != null ? 	" UserName="+UserName 		: "")+
				(fileName != null ? " FileName="+fileName 	: "")+
				(sDate != null  ? " Search Date Start="+sDate:"")+
				(eDate != null  ? " Search Date End="+eDate:"")+
				(searchType != null  ? " Search Type="+searchType:"")+
				(sysCode != null  ? " System Code="+sysCode:"")+
				(codeFilter != null  ? " Code Filter ="+codeFilter:"")+
				(codeFilterResult != null  ? " Select Code="+codeFilterResult:"")+
				" ]";

		logService.writeLog(request, "EXCELDOWN", "DO", LogStr);

		return jRes;
	}
	
	
	/* 언어팩 코드 -> 로케일 언어로 반환 */
	@RequestMapping(value="/msgConvert.do", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody  AJaxResVO msgConvert(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		try{

			String convertMsgCode = null;
			String convertedMsg = null;
			String defaultSiteId = "";

			if (request.getParameter("convertMsgCode") != null && !request.getParameter("convertMsgCode").isEmpty()) {
				convertMsgCode = request.getParameter("convertMsgCode");
			}

			//convertMsgCode = defaultSiteId+"."+convertMsgCode;
						
			//convertedMsg = messageSource.getMessage(convertMsgCode, null,localeResolver.resolveLocale(request));
			convertedMsg = messageSource.getMessage(convertMsgCode, null,Locale.getDefault());
			// 변환된 언어가 입력받은 코드가 아닐때 : 즉.. 코드를 받아서 변환 해주었다면 true 변환못하고 그대로 반환해준다면 false
			if(!convertedMsg.equals(request.getParameter("convertMsgCode"))){
				jRes.setResult("CONVERT_SUCCESS");
				jRes.addAttribute("convertedMsg", convertedMsg);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

			}else{
				jRes.setResult("CONVERT_FAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// 고객사별 전용 메뉴 가져오기(관리메뉴)
	@RequestMapping(value = "/customer_code.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO customer_code(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			// etcConfig에서 고객사 코드 가져오기
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("USER");
			etcConfigInfo.setConfigKey("CUSTOMER_CODE");
			
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			if (etcConfigResult.size() > 0 && etcConfigResult != null) {
				// custConfig에서 고객사 코드가 일치하고 configKey값이 'ProgramCode'인 value가져오기 
				CustConfigInfo custConfigInfo = new CustConfigInfo();
				custConfigInfo.setrCustCode(etcConfigResult.get(0).getConfigValue());
				custConfigInfo.setrConfigKey("ProgramCode");
				
				MMenuAccessInfo menuAccessInfo = new MMenuAccessInfo();
				
				// 일치하는 리스트
				/*List<CustConfigInfo> custConfigYResult = custConfigInfoService.selectMenuYCustConfigInfo(custConfigInfo);
				if(custConfigYResult != null) {
					for (int i = 0; i < custConfigYResult.size(); i++) {
						// 일치하는 리스트 읽기권한 Y
						menuAccessInfo.setProgramCode(custConfigYResult.get(i).getrConfigValue());
						menuAccessInfo.setLevelCode("E1001");
						menuAccessInfo.setReadYn("Y");
						menuAccessInfoService.updateMenuAccessInfo(menuAccessInfo);
					}
				}
				
				// 일치하지 않는 리스트(다른 고객사에만 있는 리스트 : 중복제거, 일치리스트에 있는 항목 제거) 
				List<CustConfigInfo> custConfigNResult = custConfigInfoService.selectMenuNCustConfigInfo(custConfigInfo);
				if(custConfigNResult != null) {
					for (int i = 0; i < custConfigNResult.size(); i++) {
						// 일치하지 않는 리스트 읽기권한 N
						menuAccessInfo.setProgramCode(custConfigNResult.get(i).getrConfigValue());
						menuAccessInfo.setLevelCode("E1001");
						menuAccessInfo.setReadYn("N");
						menuAccessInfoService.updateMenuAccessInfo(menuAccessInfo);
					}
				}*/
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}

	@RequestMapping(value = "/logoSetting.do", method = RequestMethod.POST)
	public @ResponseBody AJaxResVO customize(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException, IOException, ParseException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		AJaxResVO jRes = new AJaxResVO();
		List<LogoVO> logoVOInfoResult = null;
		String logoChangeUse = "N";
		String logoType="";
		if(!StringUtil.isNull(request.getParameter("logoType"))) {
			logoType = request.getParameter("logoType");
		}
		try {
			LogoVO logoInfo = new LogoVO();
			logoInfo.setLogoType(logoType);
			logoVOInfoResult = subNumberInfoService.selectLogoInfo(logoInfo);
			if(logoVOInfoResult.size()>0) {
				if (logoVOInfoResult.get(0).getLogoChangeUse() != null) {
					logoChangeUse = logoVOInfoResult.get(0).getLogoChangeUse();
					jRes.addAttribute("logoChangeUse", logoChangeUse);
				}
			}	
		} catch(Exception e) {
			//System.out.println(e);
			jRes.addAttribute("logoChangeUse", "N");
		}
		return jRes;
	}
	
}//end
