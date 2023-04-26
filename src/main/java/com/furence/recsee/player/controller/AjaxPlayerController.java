package com.furence.recsee.player.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.player.model.PlayerInfo;
import com.furence.recsee.player.service.PlayerService;

@Controller
@RequestMapping("/player")
public class AjaxPlayerController {
	
	@Autowired
	PlayerService playerService;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SearchListInfoService searchListInfoService;
	
	@RequestMapping(value="/getFileListenUrl" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getFileListenUrl(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		if(userInfo!=null) {
			
			String port=null;
			String url=null;
			String http="http";
			
			//https 사용 유무 체크
			EtcConfigInfo listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("SYSTEM");
			listenTYpe.setConfigKey("HTTPS");
			List<EtcConfigInfo>HttpSResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);
			
			if("https".equals(HttpSResult.get(0).getConfigValue()))
				http="https";

			listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("LISTEN");
			listenTYpe.setConfigKey("TYPE");

			List<EtcConfigInfo>listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);
			
			listenTYpe.setConfigKey("PORT");
			port=etcConfigInfoService.selectEtcConfigInfo(listenTYpe).get(0).getConfigValue();
			

			//리슨타입이 path일떄

			PlayerInfo playerInfo = new PlayerInfo();
			playerInfo.setrRecDate(params.get("recDate"));
			playerInfo.setrRecTime(params.get("recTime"));
			playerInfo.setrExtNum(params.get("ext"));
			List<PlayerInfo> listenUrl = new ArrayList<PlayerInfo>();
			
			if("URL".equals(listenTypeResult.get(0).getConfigValue())) {
				listenUrl = playerService.selectListenUrl2(playerInfo);
				if("https".equals(http))
					url=listenUrl.get(0).getrListenUrl().replace("http", "https").replace("HTTP", "HTTPS").replace("28881", port);
				else
					url=listenUrl.get(0).getrListenUrl();
			}else {
				listenTYpe.setConfigKey("PORT");
				
				port=etcConfigInfoService.selectEtcConfigInfo(listenTYpe).get(0).getConfigValue();

				listenUrl = playerService.selectListenUrl(playerInfo);
				url=listenUrl.get(0).getrListenUrl().replace("http", http).replace("HTTP", http);
			}
				
			if( listenUrl.size() > 0) {
				jRes.addAttribute("listenUrl" , url);
				jRes.setSuccess(jRes.SUCCESS_Y);
			}else {
				jRes.setSuccess(jRes.SUCCESS_N);	
			}
			
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		
		return jRes;
	}
	
	@RequestMapping(value="/getListType1" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getListType1(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setrRecDate(params.get("recDate"));
		playerInfo.setrRecTime(params.get("recTime"));
		playerInfo.setrExtNum(params.get("ext"));
		
		List<PlayerInfo> sttListType1 = playerService.selectListType1(playerInfo);
		
		if( sttListType1.size() > 0) {
			jRes.addAttribute("rxText" , sttListType1.get(0).getrTextRx());
			jRes.addAttribute("txText" , sttListType1.get(0).getrTextTx());
			jRes.setSuccess(jRes.SUCCESS_Y);
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}
		
		return jRes;
	}
	
	
	@RequestMapping(value="/getListType2" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getListType2(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setrRecDate(params.get("recDate"));
		playerInfo.setrRecTime(params.get("recTime"));
		playerInfo.setrExtNum(params.get("ext"));
		
		List<PlayerInfo> sttListType2 = playerService.selectListType2(playerInfo);
		
		if( sttListType2.size() > 0) {
			jRes.addAttribute("rxText" , sttListType2.get(0).getrTextRx());
			jRes.addAttribute("txText" , sttListType2.get(0).getrTextTx());
			jRes.setSuccess(jRes.SUCCESS_Y);
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}
		
		return jRes;
	}
	
	@RequestMapping(value="/getListType3" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getListType3(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setrRecDate(params.get("recDate"));
		playerInfo.setrRecTime(params.get("recTime"));
		playerInfo.setrExtNum(params.get("ext"));
		
		List<PlayerInfo> sttListType3 = playerService.selectListType3(playerInfo);
		
		if( sttListType3.size() > 0) {
			jRes.addAttribute("rxText" , sttListType3.get(0).getrTextRx());
			jRes.addAttribute("txText" , sttListType3.get(0).getrTextTx());
			jRes.setSuccess(jRes.SUCCESS_Y);
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}
		
		return jRes;
	}
	
	@RequestMapping(value="/getFileInfo" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getFileInfo(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setrRecDate(params.get("recDate"));
		playerInfo.setrRecTime(params.get("recTime"));
		playerInfo.setrExtNum(params.get("ext"));
		
		List<PlayerInfo> fileInfo = playerService.selectFileInfo(playerInfo);
		
		if( fileInfo.size() > 0) {
			jRes.addAttribute("recDate" , fileInfo.get(0).getrRecDate());
			jRes.addAttribute("recTime" , fileInfo.get(0).getrRecTime());
			jRes.addAttribute("Ext" , fileInfo.get(0).getrExtNum());
			jRes.addAttribute("rUserName" , fileInfo.get(0).getrUserName());
			jRes.addAttribute("rCustPhone" , fileInfo.get(0).getrCustPhone());
			jRes.setSuccess(jRes.SUCCESS_Y);
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}
		
		return jRes;
	}
	
	
	
	@RequestMapping(value="/getList" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getList(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setrRecDate(params.get("recDate"));
		playerInfo.setrRecTime(params.get("recTime"));
		playerInfo.setrExtNum(params.get("ext"));
		
		List<PlayerInfo> fileInfo = playerService.selectSttList(playerInfo);
		
		if( fileInfo.size() > 0) {
			jRes.addAttribute("count" , fileInfo.get(0).getCount());
			jRes.setSuccess(jRes.SUCCESS_Y);
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}
		
		return jRes;
	}
	
	@RequestMapping(value="/callScriptCheck" , produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO callScriptCheck(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setrRecDate(params.get("recDate"));
		playerInfo.setrRecTime(params.get("recTime"));
		playerInfo.setrExtNum(params.get("ext"));
		
		List<PlayerInfo> fileInfo = playerService.selectSttList(playerInfo);
		
		if( fileInfo.size() > 0) {
			jRes.addAttribute("count" , fileInfo.get(0).getCount());
			jRes.setSuccess(jRes.SUCCESS_Y);
		}else {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}
		
		return jRes;
	}
	@RequestMapping(value="/recdata/{callKey}" ,method = { RequestMethod.POST, RequestMethod.GET } )
	public @ResponseBody AJaxResVO getRecData(HttpServletRequest request, Locale local, Model model,@PathVariable(value = "callKey") String callKey) {
		AJaxResVO jRes = new AJaxResVO();		
		List<SearchListInfo> searchListResult = null;
		
		
		searchListResult  = searchListInfoService.selectMergeFileToCallKey(callKey);
		searchListResult = Optional.of(searchListResult).orElse(Collections.EMPTY_LIST);
		if(searchListResult.isEmpty()) {
			jRes.setSuccess(jRes.SUCCESS_N);	
		}else {
			try {
				makeTimeFormat(searchListResult.get(0));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			jRes.setSuccess(jRes.SUCCESS_Y);
			jRes.addAttribute("recData",searchListResult.get(0));
			jRes.addAttribute("recKey",searchListResult.get(0).getCallId1());
		}
		return jRes;
	}
	
	public void makeTimeFormat(SearchListInfo data) throws ParseException {
		data.setRecDate(dateFormat(data.getRecDate()));
		data.setRecTime(timeFormate(data.getRecTime()));
	}
	
	public String dateFormat(String d) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date parse = sdf.parse(d);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf2.format(parse);
		return format;
	}
	public String timeFormate(String d) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		Date parse = sdf.parse(d);
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		String format = sdf2.format(parse);
		return format;
	}
}
