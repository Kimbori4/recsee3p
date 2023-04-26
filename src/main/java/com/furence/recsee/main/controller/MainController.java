package com.furence.recsee.main.controller;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;

//
@Controller
//@RequestMapping("/search")
public class MainController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MainController.class);
//
//	@Autowired
//	private MenuAccessInfoService menuAccessInfoService;
//
//	@Autowired
//	private EtcConfigInfoService etcConfigInfoService;
//
//	@Autowired
//	private MessageSource messageSource;
//
//	// 메인 - 녹취 조회 화면
//	@RequestMapping(value = "/search")
//	public ModelAndView searchFrom(HttpServletRequest request, Locale locale, Model model) {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
//		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
//
//		if(userInfo != null) {
//
//			@SuppressWarnings("unchecked")
//			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
//			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "search_listen");
//
//			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("PLAYER");
//			etcConfigInfo.setConfigKey("MODE");
//			List<EtcConfigInfo> playerModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String playerMode = "AUDIO";
//			if(playerModeResult.size() > 0) {
//				playerMode = playerModeResult.get(0).getConfigValue();
//			}
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("PLAYER");
//			etcConfigInfo.setConfigKey("EXPENSION");
//			List<EtcConfigInfo> expensionPlayerReuslt = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String expensionPlayer = "N";
//			if(expensionPlayerReuslt.size() > 0) {
//				expensionPlayer = expensionPlayerReuslt.get(0).getConfigValue();
//			}
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("PLAYER");
//			etcConfigInfo.setConfigKey("WAVESURFER");
//			List<EtcConfigInfo> waveSurferReuslt = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String waveSurfer = "N";
//			if(waveSurferReuslt.size() > 0) {
//				waveSurfer = waveSurferReuslt.get(0).getConfigValue();
//			}
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("SEARCH");
//			etcConfigInfo.setConfigKey("EXPENSION");
//			List<EtcConfigInfo> expensionSearchReuslt = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String expensionSearch = "N";
//			if(expensionSearchReuslt.size() > 0) {
//				expensionSearch = expensionSearchReuslt.get(0).getConfigValue();
//			}
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("SEARCH");
//			etcConfigInfo.setConfigKey("PAGING_MODE");
//			List<EtcConfigInfo> pagingModeReuslt = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String pagingMode = "A";
//			if(expensionSearchReuslt.size() > 0) {
//				pagingMode = pagingModeReuslt.get(0).getConfigValue();
//			}
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("SEARCH");
//			etcConfigInfo.setConfigKey("BIG_PAGING");
//			List<EtcConfigInfo> bigPagingResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String bigPaging = "N";
//			if(bigPagingResult.size() > 0) {
//				bigPaging = bigPagingResult.get(0).getConfigValue();
//			}
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("SEARCH");
//			etcConfigInfo.setConfigKey("LIST_PLAYER_MODE");
//			List<EtcConfigInfo> listPlayerModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String listPlayerMode = "Multi";
//			if(listPlayerModeResult.size() > 0) {
//				listPlayerMode = listPlayerModeResult.get(0).getConfigValue();
//			}
//
//			model.addAttribute("userName", userInfo.getUserName());
//			model.addAttribute("userId", userInfo.getUserId());
//			model.addAttribute("nowAccessInfo", nowAccessInfo);
//			model.addAttribute("systemTemplates", systemTemplates);
//			model.addAttribute("defaultSkin", defaultSkin);
//			model.addAttribute("playerMode", playerMode);
//			model.addAttribute("expensionPlayer", expensionPlayer);
//			model.addAttribute("waveSurfer", waveSurfer);
//			model.addAttribute("expensionSearch", expensionSearch);
//			model.addAttribute("pagingMode", pagingMode);
//			model.addAttribute("bBigPaging", bigPaging);
//			model.addAttribute("listPlayerMode", listPlayerMode);
//
//			String browserDetails = request.getHeader("User-Agent");
//			String userAgent       =   browserDetails;
//	        String user            =   userAgent.toLowerCase();
//	        String browser = "";
//
//	        if (user.contains("msie"))
//	        {
//	            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
//	            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
//	        } else if(user.contains("rv"))
//	        {
//	            browser="IE";
//	        }
//
//	        //System.out.println("browser :" + browser );
//	        model.addAttribute("isIE", browser.trim().isEmpty() ? false: true);
//
//			ModelAndView result = new ModelAndView();
//			result.setViewName("/search/search");
//
//			return result;
//		} else {
//			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
//			rv.setExposeModelAttributes(false);
//
//			return new ModelAndView(rv);
//		}
//	}

	// 메인화면
	@RequestMapping(value = "/main")
	public ModelAndView searchFrom(HttpServletRequest request, Locale locale, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException {
		Logger logger = Logger.getLogger(getClass());
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String tabMode ="";
		String path = "";
		
		if(userInfo==null) {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
		if(request.getSession().getAttribute("tabMode")!=null) {
			tabMode = (String)request.getSession().getAttribute("tabMode");
		}
		
		if(StringUtil.isNull(request.getParameter("path"),true)) {
			path = (String)request.getSession().getAttribute("ssoPath");
		}else {
			path = XssFilterUtil.XssFilter(request.getParameter("path"));
		}
		Map<String,?>fm = RequestContextUtils.getInputFlashMap(request);
		
		if(fm != null) {
			String getPath = fm.get("path").toString();
			path = getPath;
		}
		
		String sso = (String)request.getSession().getAttribute("sso");
		if (sso != null && !"".equals(sso) && "script".equals(sso)) {
			path = "scriptRegistration/script_Registration";
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
		} catch (IOException e) {
			logger.error(e);
			logger.error("error",e);
		}
		
		ModelAndView result = new ModelAndView();
		model.addAttribute("tabMode", tabMode);
		model.addAttribute("userInfoJson", json);
		model.addAttribute("bgCode", userInfo.getBgCode());
		model.addAttribute("mgCodeName", userInfo.getMgCodeName());
		model.addAttribute("sgCodeName", userInfo.getSgCodeName());
		model.addAttribute("userName", userInfo.getUserName());
		model.addAttribute("userId", userInfo.getUserId());
		
		if(userInfo != null && !StringUtil.isNull(path,true)&&!"Y".equals(tabMode)) {
			model.addAttribute("path", path);
			result.setViewName("/main");
			return result;
		}else if(userInfo != null && "Y".equals(tabMode)) {
			result.setViewName("/tab");
			return result;
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}
	@RequestMapping(value = "/recPlayerMain")
	public ModelAndView recseePlayerMain(HttpServletRequest request, Locale locale, Model model) throws NoSuchAlgorithmException, InvalidKeySpecException {
		ModelAndView result = new ModelAndView();
		String callKey = Optional.ofNullable(request.getParameter("callKey")).orElse("");
		model.addAttribute("key", callKey);
		result.setViewName("/recseePlayer/recPlayerMain");
		return result;
	}
	
	// sso duplication
	@RequestMapping(value = "/ssoDuplication")
	public ModelAndView ssoDuplication(HttpServletRequest request, Locale locale, Model model) {
		ModelAndView result = new ModelAndView();
		result.setViewName("/login/ssoDuplication");
		return result;
	}
	// 로그인 실패 유저 없음
	@RequestMapping(value = "/loginFailByNotUsed")
	public ModelAndView loginFailByNotUsed(HttpServletRequest request, Locale locale, Model model) {
		ModelAndView result = new ModelAndView();
		result.setViewName("/login/loginFailByNotUsed");
		return result;
	}
	
	// 로그인 실패
	@RequestMapping(value = "/loginFail")
	public ModelAndView loginFail(HttpServletRequest request, Locale locale, Model model) {
		ModelAndView result = new ModelAndView();
		result.setViewName("/loginFail");
		return result;
	}
		
}
