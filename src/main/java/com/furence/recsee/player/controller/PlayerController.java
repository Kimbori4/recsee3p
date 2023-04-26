package com.furence.recsee.player.controller;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;


@Controller
public class PlayerController {

	@Autowired
	EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SearchListInfoService searchListInfoService;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	
	
	@RequestMapping(value = "/recPlayer")
	public ModelAndView recPlayer(HttpServletRequest request, Model model) {
		String HTTP = (String)request.getSession().getAttribute("http");
		EtcConfigInfo listenServerInfo = new EtcConfigInfo();
		listenServerInfo.setGroupKey("LISTEN");
		listenServerInfo.setConfigKey("IP");
		List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		String ip=resultInfo.get(0).getConfigValue();

		listenServerInfo.setConfigKey("PORT");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);

		String port=resultInfo.get(0).getConfigValue();

		model.addAttribute("ip", ip);
		model.addAttribute("port", port);
		model.addAttribute("HTTP", HTTP);
		
		String key = Optional.ofNullable(request.getParameter("callKey")).orElse("");
		model.addAttribute("callKey", key);
		
		ModelAndView result = new ModelAndView();
		result.setViewName("/recseePlayer/recPlayer");
		return  result;
		
		
		
	}
	
	// 대면녹취용으로 따로 만듬
	@RequestMapping(value = "/faceRecseePlayer")
	public ModelAndView faceRecseePlayer(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();

		String HTTP="http";
		EtcConfigInfo listenServerInfo = new EtcConfigInfo();
		listenServerInfo.setGroupKey("LISTEN");
		listenServerInfo.setConfigKey("IP");
		List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		String ip=resultInfo.get(0).getConfigValue();

		listenServerInfo.setConfigKey("PORT");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);

		String port=resultInfo.get(0).getConfigValue();
		
		listenServerInfo.setGroupKey("SYSTEM");
		listenServerInfo.setConfigKey("HTTPS");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		if("https".equals(resultInfo.get(0).getConfigValue()))
			HTTP="https";

		model.addAttribute("ip", ip);
		model.addAttribute("port", port);
		model.addAttribute("HTTP", HTTP);

		
		MMenuAccessInfo menuAccess = new MMenuAccessInfo();
		menuAccess.setLevelCode("E1001");
		menuAccess.setDisplayLevel(100);

		List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccess);
		int menuAccessListTotal = menuAccessList.size();
		
		if(menuAccessListTotal > 3) {
			SessionManager.setAttribute(request, "AccessInfo", menuAccessList);
		}
//		@SuppressWarnings("unchecked")
//		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
//
//		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "player.menu");
//		model.addAttribute("menu", nowAccessInfo);
//		//다운로드 버튼 활성화
//		if("Y".equals(nowAccessInfo.getPrereciptYn())) {
//			EtcConfigInfo playerDownloadInfo = new EtcConfigInfo();
//			playerDownloadInfo.setGroupKey("download");
//			playerDownloadInfo.setConfigKey("PLAYER_PASSWORD");
//			resultInfo = etcConfigInfoService.selectEtcConfigInfo(playerDownloadInfo);
//			String playerPassword=resultInfo.get(0).getConfigValue();
//			model.addAttribute("playerPassword", playerPassword);
//			playerDownloadInfo.setConfigKey("PLAYER_PERIOD");
//			resultInfo = etcConfigInfoService.selectEtcConfigInfo(playerDownloadInfo);
//			String playerPeriod=resultInfo.get(0).getConfigValue();
//			model.addAttribute("playerPeriod", playerPeriod);
//		}
//		
//		nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "player.sectionMenu");
//		model.addAttribute("sectionMenu", nowAccessInfo);

		String listenUrl= null;
		if(!StringUtil.isNull(request.getParameter("key"),true))
			listenUrl = request.getParameter("key");
		else
			listenUrl = "empty";
		model.addAttribute("listenUrl",listenUrl);
		result.setViewName("/recseePlayer/faceRecseePlayer");
		return result;
	}
	
	

	// 렉시 플레이어 오디오 전용
	@RequestMapping(value = "/Player")
	public ModelAndView recseePlayer(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();

		String HTTP="http";
		EtcConfigInfo listenServerInfo = new EtcConfigInfo();
		listenServerInfo.setGroupKey("LISTEN");
		listenServerInfo.setConfigKey("IP");
		List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		String ip=resultInfo.get(0).getConfigValue();

		listenServerInfo.setConfigKey("PORT");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);

		String port=resultInfo.get(0).getConfigValue();
		
		listenServerInfo.setGroupKey("SYSTEM");
		listenServerInfo.setConfigKey("HTTPS");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		if("https".equals(resultInfo.get(0).getConfigValue()))
			HTTP="https";

		model.addAttribute("ip", ip);
		model.addAttribute("port", port);
		model.addAttribute("HTTP", HTTP);

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");

		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "player.menu");
		model.addAttribute("menu", nowAccessInfo);
		//다운로드 버튼 활성화
		if("Y".equals(nowAccessInfo.getPrereciptYn())) {
			EtcConfigInfo playerDownloadInfo = new EtcConfigInfo();
			playerDownloadInfo.setGroupKey("download");
			playerDownloadInfo.setConfigKey("PLAYER_PASSWORD");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(playerDownloadInfo);
			String playerPassword=resultInfo.get(0).getConfigValue();
			model.addAttribute("playerPassword", playerPassword);
			playerDownloadInfo.setConfigKey("PLAYER_PERIOD");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(playerDownloadInfo);
			String playerPeriod=resultInfo.get(0).getConfigValue();
			model.addAttribute("playerPeriod", playerPeriod);
		}
		
		nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "player.sectionMenu");
		model.addAttribute("sectionMenu", nowAccessInfo);

		String listenUrl= null;
		if(!StringUtil.isNull(request.getParameter("key"),true))
			listenUrl = request.getParameter("key");
		else
			listenUrl = "empty";
		model.addAttribute("listenUrl",listenUrl);
		result.setViewName("/recseePlayer/recseePlayer");
		return result;
	}
	// 렉시 플레이어 오디오 전용
	@RequestMapping(value = "/Player2")
	public ModelAndView recseePlayer2(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		
		String HTTP="http";
		EtcConfigInfo listenServerInfo = new EtcConfigInfo();
		listenServerInfo.setGroupKey("LISTEN");
		listenServerInfo.setConfigKey("IP");
		List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		String ip=resultInfo.get(0).getConfigValue();
		
		listenServerInfo.setConfigKey("PORT");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		
		String port=resultInfo.get(0).getConfigValue();
		
		listenServerInfo.setGroupKey("SYSTEM");
		listenServerInfo.setConfigKey("HTTPS");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		if("https".equals(resultInfo.get(0).getConfigValue()))
			HTTP="https";
		
		model.addAttribute("ip", ip);
		model.addAttribute("port", port);
		model.addAttribute("HTTP", HTTP);
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "player.menu");
		model.addAttribute("menu", nowAccessInfo);
		//다운로드 버튼 활성화
		if("Y".equals(nowAccessInfo.getPrereciptYn())) {
			EtcConfigInfo playerDownloadInfo = new EtcConfigInfo();
			playerDownloadInfo.setGroupKey("download");
			playerDownloadInfo.setConfigKey("PLAYER_PASSWORD");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(playerDownloadInfo);
			String playerPassword=resultInfo.get(0).getConfigValue();
			model.addAttribute("playerPassword", playerPassword);
			playerDownloadInfo.setConfigKey("PLAYER_PERIOD");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(playerDownloadInfo);
			String playerPeriod=resultInfo.get(0).getConfigValue();
			model.addAttribute("playerPeriod", playerPeriod);
		}
		
		nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "player.sectionMenu");
		model.addAttribute("sectionMenu", nowAccessInfo);
		
		String listenUrl= null;
		if(!StringUtil.isNull(request.getParameter("key"),true))
			listenUrl = request.getParameter("key");
		else
			listenUrl = "empty";
		model.addAttribute("listenUrl",listenUrl);
		result.setViewName("/recseePlayer/recseePlayer2");
		return result;
	}

	
	// 렉시 플레이어 비디오 겸용
	@RequestMapping(value = "/videoPlayer" )
	public ModelAndView videoPlayer(HttpServletRequest request, Model model) throws UnsupportedEncodingException {

		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {

			EtcConfigInfo listenServerInfo = new EtcConfigInfo();
			listenServerInfo.setGroupKey("LISTEN");
			listenServerInfo.setConfigKey("IP");
			List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
			String ip=resultInfo.get(0).getConfigValue();

			listenServerInfo.setConfigKey("PORT");
			resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);

			String port=resultInfo.get(0).getConfigValue();

			model.addAttribute("ip", ip);
			model.addAttribute("port", port);

			ModelAndView result = new ModelAndView();

			String listenUrl= null;
			String screenUrl= null;
			if(!StringUtil.isNull(request.getParameter("listenUrl"),true))
				listenUrl = request.getParameter("listenUrl");
			if(!StringUtil.isNull(request.getParameter("screenUrl"),true))
				screenUrl = request.getParameter("screenUrl");

			model.addAttribute("listenUrl",listenUrl);
			model.addAttribute("screenUrl",screenUrl);
			model.addAttribute("userId",userInfo.getUserId());
			result.setViewName("/recseePlayer/videoPlayer");
			return result;
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}

	// qa플레이어 테스트
	@RequestMapping(value = "/qaPlayer")
	public ModelAndView recseeQaPlayer(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		result.setViewName("/recseePlayer/recseeQaPlayer");
		return result;
	}


	// 아래 URL로 요청시 여기 탐 
	// http://localhost:8080/recsee3p/listenSimple.html?key=http://localhost:28881/listen?url=D:/REC/RecSee_Data/20200810/22/202006050721230927_31345_GSS059_A.mp3
	// REC3.ini 설정 : PLAYER_URL? 'http://localhost:8080/recsee3p/listenSimple.html'
	// 심플플레이어
	@RequestMapping(value = "/listenSimple")
	public ModelAndView recseeSimplePlayer(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		
		EtcConfigInfo listenServerInfo = new EtcConfigInfo();
		listenServerInfo.setGroupKey("LISTEN");
		listenServerInfo.setConfigKey("IP");
		List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		String ip=resultInfo.get(0).getConfigValue();

		listenServerInfo.setConfigKey("PORT");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);

		String port=resultInfo.get(0).getConfigValue();

		String listenUrl = "";

		if(!StringUtil.isNull(request.getParameter("url"))){
			listenUrl = request.getParameter("url");
		}else if (!StringUtil.isNull(request.getParameter("key"))){
			listenUrl = request.getParameter("key");
		}		
		
		model.addAttribute("listenUrl",listenUrl);		
		result.setViewName("/recseePlayer/recseeSimplePlayer");
		return result;
		
	}

	/*// 심플플레이어 리다이렉트
	@RequestMapping(value = "/listenSimplePlayer")
	public ModelAndView listenSimplePlayer(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();

		HttpSession session = request.getSession();
	    Map<String,String> inputFlashMap = new HashMap<String,String>();
			
		if (RequestContextUtils.getInputFlashMap(request)!=null) {
			inputFlashMap = (Map<String, String>) RequestContextUtils.getInputFlashMap(request).get("map");
			session.setAttribute("simple_ip", inputFlashMap.get("ip"));
			session.setAttribute("simple_port", inputFlashMap.get("port"));
			session.setAttribute("simple_listenUrl", inputFlashMap.get("listenUrl"));
		}
		else {
			inputFlashMap.put("ip", (String) session.getAttribute("simple_ip"));
			inputFlashMap.put("port", (String) session.getAttribute("simple_port"));
			inputFlashMap.put("listenUrl", (String) session.getAttribute("simple_listenUrl"));
		}
		

		model.addAttribute("ip", inputFlashMap.get("ip"));
		model.addAttribute("port", inputFlashMap.get("port"));
		model.addAttribute("listenUrl",inputFlashMap.get("listenUrl"));

		result.setViewName("/recseePlayer/recseeSimplePlayer");
		return result;
	}*/
	
	
	// TEST
	@RequestMapping(value = "/test")
	public ModelAndView test(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		result.setViewName("/recseePlayer/test");
		return result;
	}

	//우수콜편집플레이어
	@RequestMapping(value = "/SharePlayer")
	public ModelAndView recseeSharePlayer(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		String listenUrl= null;
		String userId=null;
		String userName=null;
		String bgCode=null;
		String mgCode=null;
		String sgCode=null;
		String ip=null;
		
		//ip받기..짜증남 테섭이 ip가 4개라서..eth0인 ip를 가져오기..하드코딩해버림..하하하하하..미안해여..집가고 싶어요..회사오기 싫어어
		/*NetworkInterface networkInterface=null;
		try {
			networkInterface = NetworkInterface.getByName("eth0");
		} catch (SocketException e) {
			//e.printStackTrace();
			logger.error("eth0::"+e.toString());
		}*/
		
		/*Enumeration<NetworkInterface> n=null;
		try {
			n = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(n.hasMoreElements()) {
			NetworkInterface nn=n.nextElement();
			
			Enumeration<InetAddress> kk = nn.getInetAddresses();
			
			while (kk.hasMoreElements()) {
				InetAddress inetAddress = kk.nextElement();
				if(!inetAddress.isLoopbackAddress()&&!inetAddress.isLinkLocalAddress()&&inetAddress.isSiteLocalAddress()) {
					ip=inetAddress.getHostAddress().toString();
				}
			}
		}*/
		
		try {
			ip=InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
		}
		
		
		/*Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
		InetAddress currentAdress=null;
		
		if(inetAddress.hasMoreElements()) {
			while(inetAddress.hasMoreElements()) {
				currentAdress=inetAddress.nextElement();
				if(!currentAdress.isLoopbackAddress()&&!currentAdress.isLinkLocalAddress()&&currentAdress.isSiteLocalAddress()) {
					ip=currentAdress.getHostAddress().toString();
					break;
				}
			}
		}else {
			Enumeration<NetworkInterface> nienum=null;
			try {
				nienum = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e) {
				//e.printStackTrace();
				logger.error("nienum::"+e.toString());
			}
			
			while (nienum.hasMoreElements()) {
				NetworkInterface ni = nienum.nextElement();
				
				Enumeration<InetAddress> kk = ni.getInetAddresses();
				
				while(kk.hasMoreElements()) {
					int i=0;
					InetAddress inetAddress1=kk.nextElement();
					
					if(!inetAddress1.isLoopbackAddress()&&!inetAddress1.isLinkLocalAddress()&&inetAddress1.isSiteLocalAddress()) {
						ip=inetAddress1.getHostAddress().toString();
					}
				}
			}
		}*/
		
		
		if(!StringUtil.isNull(request.getParameter("bgCode"),true))
			bgCode = request.getParameter("bgCode");
		else 
			bgCode="";
		
		if(!StringUtil.isNull(request.getParameter("mgCode"),true))
			mgCode = request.getParameter("mgCode");
		else 
			mgCode="";
		
		if(!StringUtil.isNull(request.getParameter("sgCode"),true))
			sgCode = request.getParameter("sgCode");
		else 
			sgCode="";
		
		if(!StringUtil.isNull(request.getParameter("userId"),true))
			userId = request.getParameter("userId");
		else 
			userId="";
		
		if(!StringUtil.isNull(request.getParameter("userName"),true))
			userName = request.getParameter("userName");
		else 
			userName="";
		
		if(!StringUtil.isNull(request.getParameter("url"),true))
			listenUrl = request.getParameter("url");
		else
			listenUrl = "empty"; 
					
		model.addAttribute("ip",ip);
		model.addAttribute("userId",userId);
		model.addAttribute("userName",userName);
		model.addAttribute("bgCode",bgCode);
		model.addAttribute("mgCode",mgCode);
		model.addAttribute("sgCode",sgCode);
		model.addAttribute("listenUrl",listenUrl);
		
		result.setViewName("/recseePlayer/recseeSharePlayer");
		return result;
	}
	
	// stt플레이어
	@RequestMapping(value = "/SttPlayer")
	public ModelAndView STTPlayer(HttpServletRequest request, Model model) throws Exception{
		ModelAndView result = new ModelAndView();
		EtcConfigInfo listenServerInfo = new EtcConfigInfo();
		
		String recDate = request.getParameter("recDate");
		String recTime = request.getParameter("recTime");
		String Ext = request.getParameter("ext");
		String saveIp = null;
		
		SearchListInfo searchListInfo = new SearchListInfo();
		searchListInfo.setRecDateRaw(recDate);
		searchListInfo.setRecTimeRaw(recTime);
		searchListInfo.setExtNum(Ext);
		
		List<SearchListInfo> searchListResult = searchListInfoService.selectFullPath(searchListInfo);
		String ip=searchListResult.get(0).getvRecIp();
		
		
		listenServerInfo.setGroupKey("LISTEN");
		listenServerInfo.setConfigKey("PORT");
		List<EtcConfigInfo> resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		
		String port=resultInfo.get(0).getConfigValue();
		
		String HTTP = "http";
		listenServerInfo.setGroupKey("SYSTEM");
		listenServerInfo.setConfigKey("HTTPS");
		resultInfo = etcConfigInfoService.selectEtcConfigInfo(listenServerInfo);
		if("https".equals(resultInfo.get(0).getConfigValue()))
			HTTP="https";
		
		model.addAttribute("ip", ip);
		model.addAttribute("port", port);
		model.addAttribute("http", HTTP);
		model.addAttribute("recDate", recDate);
		model.addAttribute("recTime", recTime);
		model.addAttribute("Ext", Ext);
		
		try {
			saveIp=InetAddress.getLocalHost().getHostAddress().toString();
			model.addAttribute("saveIp", saveIp);
		} catch (UnknownHostException e) {
		}
		
		
		if(!StringUtil.isNull(request.getParameter("userId"))) 
			model.addAttribute("bestUserId", request.getParameter("userId"));				
		if(!StringUtil.isNull(request.getParameter("userName"))) 
			model.addAttribute("bestUserName", URLDecoder.decode(request.getParameter("userName"),"UTF-8"));	
		if(!StringUtil.isNull(request.getParameter("bgCode"))) 
			model.addAttribute("bestBgCode", request.getParameter("bgCode"));
		if(!StringUtil.isNull(request.getParameter("mgCode"))) 
			model.addAttribute("bestMgCode", request.getParameter("mgCode"));		
		if(!StringUtil.isNull(request.getParameter("sgCode"))) 
			model.addAttribute("bestSgCode", request.getParameter("sgCode"));		
		
		//model.addAttribute("listenUrl",request.getParameter("url"));
		
		result.setViewName("/recseePlayer/STTplayer");
		return result;
	}
	
	// stt플레이어
	@RequestMapping(value = "/callScript")
	public ModelAndView callScript(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();
		
		String recDate = request.getParameter("recDate");
		String recTime = request.getParameter("recTime");
		String Ext = request.getParameter("ext");
		
		model.addAttribute("recDate", recDate);
		model.addAttribute("recTime", recTime);
		model.addAttribute("Ext", Ext);
		
		result.setViewName("/recseePlayer/callScript");
		return result;
	}
}