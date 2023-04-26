package com.furence.recsee.monitoring.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.hyperic.sigar.SigarException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.PhoneMappingInfo;
import com.furence.recsee.common.model.SubNumberInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.PhoneMappingInfoService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.RedisService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.monitoring.model.BluePrintInfo;
import com.furence.recsee.monitoring.model.OfficeMonitoringRoomVO;
import com.furence.recsee.monitoring.model.OfficeMonitoringVO;
import com.furence.recsee.monitoring.model.RealTimeSettingInfo;
import com.furence.recsee.monitoring.model.SystemMonitoringProc;
import com.furence.recsee.monitoring.model.SystemRealtimeInfo;
import com.furence.recsee.monitoring.service.MonitoringInfoService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Controller
@RequestMapping("/monitoring")
public class ajaxMonitoringController {

	private static final org.slf4j.Logger logger= LoggerFactory.getLogger(ajaxMonitoringController.class);

	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private MonitoringInfoService monitoringInfoService;

	@Autowired
	private LogService logService;

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private SubNumberInfoService subNumberInfoService;

	@Autowired
	private PhoneMappingInfoService phoneMappingInfoService;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/listenStartLog.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO setListenStartLog(HttpServletRequest request, Locale locale, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		String listenDate = null;
		String listenTime = null;
		String listenId = null;
		String listenName = null;
		String listenExt = null;
		String custPhone = null;

		if(!StringUtil.isNull(request.getParameter("listenExt")))
			listenExt = request.getParameter("listenExt");

		if(!StringUtil.isNull(request.getParameter("listenName")))
			listenName = request.getParameter("listenName");

		if(!StringUtil.isNull(request.getParameter("listenId")))
			listenId = request.getParameter("listenId");

		if(!StringUtil.isNull(request.getParameter("custPhone")))
			custPhone = request.getParameter("custPhone");

		String LogStr = "실시간 감청 항목 [ "+
				(listenDate != null ? 		" 감청일="+listenDate 			: "")+
				(listenTime != null ? 		" 감청시간="+listenTime 			: "")+
				(listenId != null ? 		" 감청대상 아이디="+listenId 		: "")+
				(listenName != null ? 		" 감청대상 이름="+listenName 		: "")+
				(listenExt != null ? 		" 감청대상 내선번호="+listenExt 		: "")+
				(custPhone != null ? 	" 고객 전화번호="+custPhone 	: "")+
				" ]";

		logService.writeLog(request, "TAPPING", "LISTEN-START", LogStr);
		/*logInfoService.writeLog(request, "RealTimeListen - Listen Start", "ExtNum="+request.getParameter("extNum"), userInfo.getUserId());*/
		return jRes;
	}

	@RequestMapping(value = "/listenEndLog.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO setListenEndLog(HttpServletRequest request, Locale locale, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		String listenDate = null;
		String listenTime = null;
		String listenId = null;
		String listenName = null;
		String listenExt = null;
		String custPhone = null;

		if(!StringUtil.isNull(request.getParameter("listenExt")))
			listenExt = request.getParameter("listenExt");

		if(!StringUtil.isNull(request.getParameter("listenName")))
			listenName = request.getParameter("listenName");

		if(!StringUtil.isNull(request.getParameter("listenId")))
			listenId = request.getParameter("listenId");

		if(!StringUtil.isNull(request.getParameter("custPhone")))
			custPhone = request.getParameter("custPhone");

		String LogStr = "실시간 감청 항목 [ "+
				(listenDate != null ? 		" 감청일="+listenDate 			: "")+
				(listenTime != null ? 		" 감청시간="+listenTime 			: "")+
				(listenId != null ? 		" 감청대상 아이디="+listenId 		: "")+
				(listenName != null ? 		" 감청대상 이름="+listenName 		: "")+
				(listenExt != null ? 		" 감청대상 내선번호="+listenExt 		: "")+
				(custPhone != null ? 	" 고객 전화번호="+custPhone 	: "")+
				" ]";

		logService.writeLog(request, "TAPPING", "LISTEN-END", LogStr);
		/*logInfoService.writeLog(request, "RealTimeListen - Listen End", "ExtNum="+request.getParameter("extNum"), userInfo.getUserId());*/
		return jRes;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/realtime_init.json", method=RequestMethod.GET)
	public @ResponseBody void realtimeInit(HttpServletRequest request, HttpServletResponse response) throws IOException, SigarException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		JSONObject realTimeResult = new JSONObject();

		Calendar cal = Calendar.getInstance();
		String nowDate = String.valueOf(cal.get(Calendar.YEAR));
		nowDate += ( ( cal.get(Calendar.MONTH) + 1 ) < 10 ? "0" : "" ) + String.valueOf( cal.get(Calendar.MONTH) + 1 );
		nowDate += ( cal.get(Calendar.DATE) < 10 ? "0" : "" ) + String.valueOf(cal.get(Calendar.DATE));

		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("REALTIME");

			List<EtcConfigInfo> realTimeList = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			int realTimeTotal = realTimeList.size();

			if(realTimeTotal > 0) {
				JSONArray jsonRealTimeList = new JSONArray();

				for(int i = 0; i < realTimeTotal; i++) {
					JSONObject jsonRealTimeItem = new JSONObject();

					EtcConfigInfo realTimeItem = realTimeList.get(i);

					String key = String.format("%s", realTimeItem.getConfigKey().toString());
					String value = realTimeItem.getConfigValue();

					jsonRealTimeItem.put(key, value);

					jsonRealTimeList.add(jsonRealTimeItem);
				}
				realTimeResult.put("realtime", jsonRealTimeList);
			} else {
				realTimeResult.put("realtime", "");
			}

			SystemInfo systemInfo = new SystemInfo();

			List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);

			int systemInfoTotal = systemInfoList.size();

			if(systemInfoTotal > 0) {
				JSONArray jsonSystemInfoList = new JSONArray();

				for(int i = 0; i < systemInfoTotal; i++) {
					JSONObject jsonSystemInfoItem = new JSONObject();

					SystemInfo systemInfoItem = systemInfoList.get(i);

					String key = new RecSeeUtil().getSHA256(nowDate+systemInfoItem.getSysIp());
					jsonSystemInfoItem.put("system_name", systemInfoItem.getSysName());
					jsonSystemInfoItem.put("system_code", systemInfoItem.getSysId());
					jsonSystemInfoItem.put("system_ip", systemInfoItem.getSysIp());
					jsonSystemInfoItem.put("system_key", key);

					if(systemInfoItem.getStoragePath() != null ){
						JSONArray jsonHddList = new JSONArray();

						int hddCount = systemInfoItem.getStoragePath().split(",").length;
						String[] hddArray = new String[hddCount];
						hddArray = systemInfoItem.getStoragePath().split(",");

						Pattern splitPattern = Pattern.compile("\\\\");

						for(int j = 0; j < hddCount; j++) {
							String tmpHdd = hddArray[j];
							Matcher m = splitPattern.matcher(hddArray[j]);
							if(m.find()) {
								tmpHdd = hddArray[j].split("\\\\")[0];
							}
							jsonHddList.add(tmpHdd);
						}
						jsonSystemInfoItem.put("hdd", jsonHddList);
					} else {
						jsonSystemInfoItem.put("hdd", "");
					}
					jsonSystemInfoList.add(jsonSystemInfoItem);
				}
				realTimeResult.put("system", jsonSystemInfoList);
			} else {
				realTimeResult.put("system", "");
			}

			/***
			 * �ý��� ����͸� ����
			 **/
			JSONArray jsonHardware = new JSONArray();

			RecSeeUtil recseeUtil = new RecSeeUtil();
			String nowServerIp = recseeUtil.getLocalServerIp();
			//logger.info("Now server ip : " + nowServerIp );

	        systemInfo.setSysId(null);
	        systemInfo.setSysIp(nowServerIp);

			systemInfoList = systemInfoService.selectSystemInfo(systemInfo);

			systemInfoTotal = systemInfoList.size();

			if(systemInfoTotal > 0) {
				SystemMonitoringProc systemMonitoringProc = new SystemMonitoringProc();

				jsonHardware.add(systemMonitoringProc.systemMonitoringProc(systemInfoList));
			}

			realTimeResult.put("hardware", jsonHardware);
		}

		response.setContentType("application/json");
		response.getWriter().write(XssFilterUtil.XssFilter(realTimeResult.toString()));
	}

	// ���� �ð� �ܾ� ����
	@RequestMapping(value="/getCurrentTime.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO getCurrentTime(HttpServletRequest request) {

		AJaxResVO jRes = new AJaxResVO();
		jRes.setResult("DATA GET SUCCESS");
		jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		jRes.addAttribute("currentTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

		return jRes;
	}

	// ���� ����� ���� �ܾ����
	@RequestMapping(value="/selectRuserInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO updateOutboundNum(HttpServletRequest request) {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		try{

			if(userInfo != null){
				RUserInfo ruserInfo = new RUserInfo();
				if(!StringUtil.isNull(request.getParameter("monitoring")) && request.getParameter("monitoring").equals("true")) {
					ruserInfo.setrMonitoring("Y");
				}
				if(!StringUtil.isNull(request.getParameter("mobile")) && request.getParameter("mobile").equals("Y")) {
					ruserInfo.setMobileYN("Y");
				}
				List <RUserInfo> rUserList = ruserInfoService.selectRUserInfo(ruserInfo);

				if(rUserList.size() > 0){
					jRes.setResult("DATA GET SUCCESS");
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("rUserList", rUserList);
				}else{
					jRes.setResult("DATA GET FAIL");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}

			}else{
				jRes.setResult("LOGIN FAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (NullPointerException e){
			logger.error("",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}catch (Exception e){
			logger.error("",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
	return jRes;
	}

	//오피스 모니터링 방 조회 메서드
	@RequestMapping(value="/officeMonitoringSelectRoom.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO officeMonitoringSelectRoom(HttpServletRequest request, Locale locale, Model model) throws ParseException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			OfficeMonitoringRoomVO officeMonitoringRoomVo = new  OfficeMonitoringRoomVO();

			String Code = request.getParameter("code");

			//세션 로그인 아이디
			officeMonitoringRoomVo.setrUserId(userInfo.getUserId());

			List<OfficeMonitoringRoomVO> oMselect = monitoringInfoService.officeMonitoringSelect(officeMonitoringRoomVo);

			int roomCnt = oMselect.size();
			jRes.addAttribute("roomCount", roomCnt);
			String[] roomInfoArr = new String[roomCnt];
			String[] roomSeePersonArr = new String[roomCnt];
			String[] roomCode = new String[roomCnt];
			String[] roomIdx = new String[roomCnt];

			String[] roomBgLocation = new String[roomCnt];
			String[] roomMgLocation = new String[roomCnt];
			String[] roomSgLocation = new String[roomCnt];
			if(roomCnt>0) {
				for(int i=0;i<roomCnt;i++) {
					if(!StringUtil.isNull(Code)) {
						if(Code.equals("BGCODE")) {
							if(!StringUtil.isNull(oMselect.get(i).getrBgCode())) {
								roomCode[i] = oMselect.get(i).getrBgCode();
								roomInfoArr[i]= oMselect.get(i).getrRoomInfo();
								roomSeePersonArr[i]= oMselect.get(i).getrSeePerson();
								roomIdx[i]= oMselect.get(i).getrRoomIdx();
								roomBgLocation[i]= oMselect.get(i).getrBgLocation();
							}
						}else if(Code.equals("MGCODE")) {
							if(!StringUtil.isNull(oMselect.get(i).getrMgCode())) {
								roomCode[i] = oMselect.get(i).getrMgCode();
								roomInfoArr[i]= oMselect.get(i).getrRoomInfo();
								roomSeePersonArr[i]= oMselect.get(i).getrSeePerson();
								roomIdx[i]= oMselect.get(i).getrRoomIdx();
								roomMgLocation[i]= oMselect.get(i).getrMgLocation();
							}
						}else {
							if(!StringUtil.isNull(oMselect.get(i).getrSgCode())) {
								roomCode[i] = oMselect.get(i).getrSgCode();
								roomInfoArr[i]= oMselect.get(i).getrRoomInfo();
								roomSeePersonArr[i]= oMselect.get(i).getrSeePerson();
								roomIdx[i]= oMselect.get(i).getrRoomIdx();
								roomSgLocation[i]= oMselect.get(i).getrSgLocation();
							}
						}
					}
				}
			}
			jRes.addAttribute("roomInfo",roomInfoArr);
			jRes.addAttribute("roomSeePerson",roomSeePersonArr);
			jRes.addAttribute("roomCode",roomCode);
			jRes.addAttribute("roomIdx",roomIdx);
			jRes.addAttribute("roomBgLocation",roomBgLocation);
			jRes.addAttribute("roomMgLocation",roomMgLocation);
			jRes.addAttribute("roomSgLocation",roomSgLocation);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	//오피스 모니터링 방 조회 메서드
	@RequestMapping(value="/officeMonitoringSelectAgent.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO officeMonitoringSelectAgent(HttpServletRequest request, Locale locale, Model model) throws ParseException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			OfficeMonitoringVO officeMonitoringVo = new  OfficeMonitoringVO();
			//세션 로그인 아이디
			officeMonitoringVo.setrUserId(userInfo.getUserId());

			List<OfficeMonitoringVO> agentSelect = monitoringInfoService.officeMonitoringAgentSelect(officeMonitoringVo);

			jRes.addAttribute("agent",agentSelect);

			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	//오피스 모니터링 방 AGENT INSERT 메서드
	@RequestMapping(value="/officeMonitoringInsertAgent.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO officeMonitoringInsertAgent(HttpServletRequest request, Locale locale, Model model) throws ParseException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			OfficeMonitoringVO officeMonitoringVo = new  OfficeMonitoringVO();

			String codeFilter = request.getParameter("codeFilter");
			//세션 로그인 아이디
			officeMonitoringVo.setrUserId(userInfo.getUserId());
			officeMonitoringVo.setrExtNo(request.getParameter("agentExt"));
			if(!StringUtil.isNull(codeFilter,true)) {
				switch(codeFilter) {
					case "mBgCode":
						officeMonitoringVo.setrBgCode(request.getParameter("roomCode"));
						officeMonitoringVo.setrBgCodeSeat(request.getParameter("seatNum"));
					break;
					case "mMgCode":
						officeMonitoringVo.setrMgCode(request.getParameter("roomCode"));
						officeMonitoringVo.setrMgCodeSeat(request.getParameter("seatNum"));
					break;
					case "mSgCode":
						officeMonitoringVo.setrSgCode(request.getParameter("roomCode"));
						officeMonitoringVo.setrSgCodeSeat(request.getParameter("seatNum"));
					break;
				}
			}
			monitoringInfoService.officeMoitoringInsert(officeMonitoringVo);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	//오피스 모니터링 방 ROOM INSERT 메서드
	@RequestMapping(value="/officeMonitoringInsertRoom.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO officeMonitoringInsertRoom(HttpServletRequest request, Locale locale, Model model) throws ParseException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			OfficeMonitoringRoomVO officeMonitoringRoomVo = new  OfficeMonitoringRoomVO();

			String codeFilter = request.getParameter("codeFilter");
			//세션 로그인 아이디
			officeMonitoringRoomVo.setrUserId(userInfo.getUserId());
			officeMonitoringRoomVo.setrSeePerson(request.getParameter("roomSeat"));
			if(!StringUtil.isNull(codeFilter,true)) {
				switch(codeFilter) {
					case "mBgCode":
						officeMonitoringRoomVo.setrBgCode(request.getParameter("roomCode"));
						officeMonitoringRoomVo.setrBgLocation(request.getParameter("roomLocation"));
					break;
					case "mMgCode":
						officeMonitoringRoomVo.setrMgCode(request.getParameter("roomCode"));
						officeMonitoringRoomVo.setrMgLocation(request.getParameter("roomLocation"));
					break;
					case "mSgCode":
						officeMonitoringRoomVo.setrSgCode(request.getParameter("roomCode"));
						officeMonitoringRoomVo.setrSgLocation(request.getParameter("roomLocation"));
					break;
				}
			}
			monitoringInfoService.officeMoitoringRoomInsert(officeMonitoringRoomVo);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	//레디스 상담원 만들기 메서드
	@RequestMapping(value = "/realtimeSetting.do",produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO redisInsertTest(HttpServletRequest request, Locale locale, Model model){
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			String userId = userInfo.getUserId();
			RealTimeSettingInfo realTimeSettingInfo = new RealTimeSettingInfo();
			realTimeSettingInfo.setrUserId(userId);
	
			// 세팅 값 저장 온 일때
			if(!StringUtil.isNull(request.getParameter("settingYn"))) {
				String  settingYn = request.getParameter("settingYn");
				realTimeSettingInfo.setrSettingYn(settingYn);
				if(settingYn.equals("Y")) {
					monitoringInfoService.realTimeSettingInsert(realTimeSettingInfo);
				}else if(settingYn.equals("N")){
					monitoringInfoService.realTimeSettingInsert(realTimeSettingInfo);
				}
			}
			if(!StringUtil.isNull(request.getParameter("settingNum"))) {
				String  settingNum = request.getParameter("settingNum");
				realTimeSettingInfo.setrSettingNum(settingNum);
				monitoringInfoService.realTimeSettingInsert(realTimeSettingInfo);
	
			}
	
			if(!StringUtil.isNull(request.getParameter("settingType"))) {
				String  settingType = request.getParameter("settingType");
				realTimeSettingInfo.setrSettingType(settingType);
				monitoringInfoService.realTimeSettingInsert(realTimeSettingInfo);
	
			}
	
			List<RealTimeSettingInfo>realTimeSettingResult = monitoringInfoService.realTimeSettingSelect(realTimeSettingInfo);
	
			if(realTimeSettingResult.size()>0) {
				String settingYn = realTimeSettingResult.get(0).getrSettingYn();
				String settingType = realTimeSettingResult.get(0).getrSettingType();
				String settingNum = realTimeSettingResult.get(0).getrSettingNum();
	
				jRes.addAttribute("settingYn",settingYn);
				jRes.addAttribute("settingType",settingType);
				jRes.addAttribute("settingNum",settingNum);
			}
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}
	
	////오피스 모니터링 레디스 AJAX
	//@RequestMapping(value="/officeMonitoringSelect.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	//public @ResponseBody AJaxResVO officeMonitoringSelect(HttpServletRequest request, Locale locale, Model model) throws ParseException{
	//	AJaxResVO jRes = new AJaxResVO();
	//	LoginVO userInfo = SessionManager.getUserInfo(request);
	//	if (userInfo != null) {
	//
	//		boolean nowAcc = nowAccessChk(request,"realtimeMonitoring");
	//		if(!nowAcc) {
	//			return jRes;
	//		}
	//
	//		String sysId = "";
	//		String masking = "";
	//
	//		if(!StringUtil.isNull(request.getParameter("SYSID"),true))
	//			sysId = request.getParameter("SYSID");
	//
	//		//systemInfo.setSysId(sysId);
	//
	//		//List<SystemInfo> sysIp =  systemInfoService.selectSystemInfo(systemInfo);
	//		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
	//		etcConfigInfo.setGroupKey("MONITORING");
	//		etcConfigInfo.setConfigKey("REDIS_PORT");
	//		List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	//
	//		etcConfigInfo.setGroupKey("MONITORING");
	//		etcConfigInfo.setConfigKey("Masking");
	//
	//		List<EtcConfigInfo> maskingYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	//
	//		masking = maskingYn.get(0).getConfigValue();
	//
	//		JSONArray officeSelect = redisService.getExtStatusTEST(sysId,"RECSEE_EXTSTATUS",Integer.parseInt(redisPort.get(0).getConfigValue()),masking);
	//
	//		jRes.addAttribute("agentExt",officeSelect);
	//		jRes.setSuccess(AJaxResVO.SUCCESS_Y);
	//	}else {
	//		jRes.setSuccess(AJaxResVO.SUCCESS_N);
	//		jRes.setResult("Not LoginInfo");
	//	}
	//
	//	return jRes;
	//}
	
	
	
	//카드 모니터링 레디스 AJAX
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/cardMonitoringSelect.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO cardMonitoringSelect(HttpServletRequest request, Locale locale, Model model) throws Exception{
	
		AJaxResVO jRes = new AJaxResVO();
		String[] sysId = null;
		LoginVO userInfo = SessionManager.getUserInfo(request);
		JSONArray arr = new JSONArray();
		String masking = "";
	
		if (userInfo != null) {
	
			boolean nowAcc = nowAccessChk(request,"realtimeMonitoring");
	
			if(!nowAcc) {
				return jRes;
			}
	
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_IP");
			List<EtcConfigInfo> redisIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			sysId = redisIp.get(0).getConfigValue().split(",");
			
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_PORT");
			List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("Masking");
	
			List<EtcConfigInfo> maskingYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	
			masking = maskingYn.get(0).getConfigValue();
	
			Map<String, Object>  resultHMap = new HashMap<>();
			
			// 자번호 닉네임 표시 옵션 사용 여부
			EtcConfigInfo company_telno = new EtcConfigInfo();
			company_telno.setGroupKey("SEARCH");
			company_telno.setConfigKey("company_telno");
			EtcConfigInfo useExtNickYN = etcConfigInfoService.selectOptionYN(company_telno);
			String extNickYN = useExtNickYN.getConfigValue();
			
			// 자번호 리스트 조회 - 내선번호
			SubNumberInfo subNumberInfo = new SubNumberInfo();
			List<SubNumberInfo> subNumberInfoResult = null;
			
			if("Y".equals(extNickYN)) {
				subNumberInfoResult = subNumberInfoService.selectSubNumberInfo(subNumberInfo);		
			}
			
			// 전화번호 대체문자 표시 옵션 사용 여부
			EtcConfigInfo phone_mapping = new EtcConfigInfo();
			phone_mapping.setGroupKey("SEARCH");
			phone_mapping.setConfigKey("phone_mapping");
			EtcConfigInfo useCustphoneNickYN = etcConfigInfoService.selectOptionYN(phone_mapping);
			String custphoneNickYN = useCustphoneNickYN.getConfigValue();
			
			// 폰 맵핑 리스트 조회 - 고객 전화번호
			PhoneMappingInfo phoneMappingInfo = new PhoneMappingInfo();
			List<PhoneMappingInfo> phoneMappingInfoResult = null;
			
			if("Y".equals(custphoneNickYN)) {		
				phoneMappingInfoResult = phoneMappingInfoService.selectPhoneMappingInfo(phoneMappingInfo);
			}
	
			// 레디스 갯수
			int redisCnt = 0;
	
			//	현재시간
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
	
			boolean bAddFlag = false;
			
			for(int i=0;i<sysId.length;i++) {
				try {
					String selectCode = request.getParameter("selectCode");
					String selectArr = request.getParameter("selectArr");
					JSONArray selectAgent = redisService.getExtStatus(sysId[i],"RECSEE_EXTSTATUS",Integer.parseInt(redisPort.get(0).getConfigValue()),selectCode,selectArr);
					JSONObject resultObject;
					redisCnt = selectAgent.size();
					for(int j=0;j<redisCnt;j++) {
						Map<String,Object> currMap = getMapFromJsonObject((JSONObject)selectAgent.get(j));
						Map<String,Object> resultMap=null;
						try {
							logger.info("["+sysId[i]+"] "+j+"번째 currHMap.get('EXT') = "+currMap.get("EXT"));
							logger.info("["+sysId[i]+"] "+j+"번째 resultHMap.get(currMap.get('EXT')) = "+resultHMap.get(currMap.get("EXT")));
							if(resultHMap.containsKey(currMap.get("EXT"))) {
								if("1".equals(currMap.get("RTP"))) {
									if(resultHMap.get(currMap.get("EXT")) != null) {
										resultObject = new JSONObject((Map)resultHMap.get(currMap.get("EXT")));
										resultMap = getMapFromJsonObject(resultObject);
									} else {
										resultMap = null;
									}
								} else {
									continue;
								}
							} else {
								if(resultHMap.get(currMap.get("EXT")) != null) {
									resultObject = new JSONObject((Map)resultHMap.get(currMap.get("EXT")));
									resultMap = getMapFromJsonObject(resultObject);
								} else {
									resultMap = null;
								}
							}
						}catch (Exception e) {
							logger.error("error",e);
						}
						
						if(!"Y".equals(custphoneNickYN)) {
							String cust = new RecSeeUtil().makePhoneNumber((String) currMap.get("CUSTNUM"));
							if(masking.equals("Y")) {						
								cust = new RecSeeUtil().maskingNumber(cust);
								JSONObject chageCust = (JSONObject)selectAgent.get(j);
								chageCust.put("CUSTNNUM", cust);
							}
							currMap.put("CUSTNUM", cust);					
						}else {
							String cust = (String) currMap.get("CUSTNUM");
							String custChange = "";
							if(phoneMappingInfoResult!=null && !phoneMappingInfoResult.isEmpty()) {
								for (int s = 0; s < phoneMappingInfoResult.size(); s++) {
									String custPhone = phoneMappingInfoResult.get(s).getCustPhone();
									if ("Y".equals(phoneMappingInfoResult.get(s).getUseNickName()) && cust.equals(custPhone)) {
										custChange = phoneMappingInfoResult.get(s).getCustNickName();
										break;
									}
								}
							}
							
							if(!"".equals(custChange)) {
								currMap.put("CUSTNUM", custChange);	
							}
						}
						
						if("Y".equals(extNickYN)) {
							String agent = (String) currMap.get("AGENTID");
							String agentChange = "";
							
							for (int s = 0; s < subNumberInfoResult.size(); s++) {
								String subNumber = subNumberInfoResult.get(s).getTelNo();
								subNumber = Optional.ofNullable(subNumber).orElse("");
								if ("Y".equals(subNumberInfoResult.get(s).getUse()) && agent.equals(subNumber)) {
									agentChange = subNumberInfoResult.get(s).getNickName();
									break;
								}
							}
							
							if(!"".equals(agentChange)) {
								currMap.put("AGENTID", agentChange);	
							}	
						}
						
						if (resultMap  == null)
						{  
							long calDate = Calendar.getInstance().getTime().getTime() - dayTime.parse(currMap.get("UPDATETIME").toString()).getTime();
							long hou = Math.abs(calDate/(60*60*1000));
							long min = Math.abs((calDate%(60*60*1000))/(60*1000));
							long sec = Math.abs((calDate%((60*60*1000))%(60*1000))/(1000));
							String duration = null;
							if(hou>=10) {
								if(min>=10) {
									if(sec>=10) {
										duration = String.valueOf(hou)+":"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = String.valueOf(hou)+":"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}else {
									if(sec>=10) {
										duration = String.valueOf(hou)+":0"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = String.valueOf(hou)+":0"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}
							}else {
								if(min>=10) {
									if(sec>=10) {
										duration = "0"+String.valueOf(hou)+":"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = "0"+String.valueOf(hou)+":"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}else {
									if(sec>=10) {
										duration = "0"+String.valueOf(hou)+":0"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = "0"+String.valueOf(hou)+":0"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}
							}
							currMap.put("duration", duration);
							//최초
							//resultHMap.put((String)currMap.get("EXT"), selectAgent.get(j));
							resultHMap.put((String)currMap.get("EXT"), currMap);
						} else {
							//비교
							long curDateTime = dayTime.parse(currMap.get("UPDATETIME").toString()).getTime();
							long resultDateTime = dayTime.parse(resultMap.get("UPDATETIME").toString()).getTime();
							long minute = Math.abs((curDateTime - resultDateTime) / 60000 );
	
							if(minute <2) {
								if(currMap.get("CTI").equals("CALLING") && currMap.get("RTP").equals("1")) {
									//위에 플레그..
									if (bAddFlag == true)
									{
										resultHMap.put((String) currMap.get("EXT") , currMap);
										bAddFlag = false;
									}else
									{
										if(resultMap.get("CTI").equals("CALLING") && resultMap.get("RTP").equals("1")) {
											//버림.
										}else
										{
											resultHMap.put((String) currMap.get("EXT") , currMap);
										}
										bAddFlag = true;
									}
	
								}
							}else {
								if (curDateTime - resultDateTime > 0)
										resultHMap.put((String) currMap.get("EXT") , currMap);
							}
						}
	
					}
				}catch(NullPointerException e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
					logger.error("error",e);
				}
				catch(Exception e) {
					logger.error("",e);
					logger.error("error",e);
					
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
			}
	
	
			Collection<Object> values = resultHMap.values();
	
			for(Object obj : values) {
				arr.add(obj);
			}
			jRes.addAttribute("agentExt",arr);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
	
		return jRes;
	}
	
	
	//그리드 모니터링 레디스 AJAX
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/gridMonitoringSelect.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO gridMonitoringSelect(HttpServletRequest request, Locale locale, Model model) throws Exception{
	
		AJaxResVO jRes = new AJaxResVO();
		String[] sysId = null;
		LoginVO userInfo = SessionManager.getUserInfo(request);
		JSONArray arr = new JSONArray();
		String masking = "";
	
		if (userInfo != null) {
	
			boolean nowAcc = nowAccessChk(request,"realtimeMonitoring");
	
			if(!nowAcc) {
				return jRes;
			}
	
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_IP");
			List<EtcConfigInfo> redisIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			sysId = redisIp.get(0).getConfigValue().split(",");
	
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_PORT");
			List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("Masking");
	
			List<EtcConfigInfo> maskingYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	
			masking = maskingYn.get(0).getConfigValue();
	
			Map<String, Object>  resultHMap = new HashMap<>();
	
			// 레디스 갯수
			int redisCnt = 0;
	
			//	현재시간
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
	
			boolean bAddFlag = false;
			for(int i=0;i<sysId.length;i++) {
				try {
					String selectCode = request.getParameter("selectCode");
					String selectArr = request.getParameter("selectArr");
					JSONArray selectAgent = redisService.getExtStatus(sysId[i],"RECSEE_EXTSTATUS",Integer.parseInt(redisPort.get(0).getConfigValue()),selectCode,selectArr);
					JSONObject resultObject;
					redisCnt = selectAgent.size();
					for(int j=0;j<redisCnt;j++) {
	
						Map<String,Object> currMap = getMapFromJsonObject((JSONObject)selectAgent.get(j));
						//Map<String,Object> resultMap = getMapFromJsonObject((JSONObject)resultHMap.get(currMap.get("EXT")));
	
						Map<String,Object> resultMap=null;
						try {
							logger.info("["+sysId[i]+"] "+j+"번째 currHMap.get('EXT') = "+currMap.get("EXT"));
							logger.info("["+sysId[i]+"] "+j+"번째 resultHMap.get(currMap.get('EXT')) = "+resultHMap.get(currMap.get("EXT")));
							if(resultHMap.containsKey(currMap.get("EXT"))) {
								if("1".equals(currMap.get("RTP"))) {
									if(resultHMap.get(currMap.get("EXT")) != null) {
										resultObject = new JSONObject((Map)resultHMap.get(currMap.get("EXT")));
										resultMap = getMapFromJsonObject(resultObject);
									} else {
										resultMap = null;
									}
								} else {
									continue;
								}
							} else {
								if(resultHMap.get(currMap.get("EXT")) != null) {
									resultObject = new JSONObject((Map)resultHMap.get(currMap.get("EXT")));
									resultMap = getMapFromJsonObject(resultObject);
								} else {
									resultMap = null;
								}
							}
						}catch (Exception e) {
							logger.info("==========================================================");
							logger.info("gridMonitoring : " + j + ", " + sysId[i]);
							logger.info(currMap.get("EXT") + ", " + currMap.get("AGENTNAME"));
							logger.info("==========================================================");
							logger.error("error",e);
						}
						
						String cust = new RecSeeUtil().makePhoneNumber((String) currMap.get("CUSTNUM"));
						if(masking.equals("Y")) {
							cust = new RecSeeUtil().maskingNumber(cust);
							JSONObject chageCust = (JSONObject)selectAgent.get(j);
							chageCust.put("CUSTNNUM", cust);
						}
						currMap.put("CUSTNUM", cust);
	
						if (resultMap  == null)
						{
							long calDate = Calendar.getInstance().getTime().getTime() - dayTime.parse(currMap.get("UPDATETIME").toString()).getTime();
							long hou = Math.abs(calDate/(60*60*1000));
							long min = Math.abs((calDate%(60*60*1000))/(60*1000));
							long sec = Math.abs((calDate%((60*60*1000))%(60*1000))/(1000));
							String duration = null;
							if(hou>=10) {
								if(min>=10) {
									if(sec>=10) {
										duration = String.valueOf(hou)+":"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = String.valueOf(hou)+":"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}else {
									if(sec>=10) {
										duration = String.valueOf(hou)+":0"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = String.valueOf(hou)+":0"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}
							}else {
								if(min>=10) {
									if(sec>=10) {
										duration = "0"+String.valueOf(hou)+":"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = "0"+String.valueOf(hou)+":"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}else {
									if(sec>=10) {
										duration = "0"+String.valueOf(hou)+":0"+String.valueOf(min)+":"+String.valueOf(sec);
									}else {
										duration = "0"+String.valueOf(hou)+":0"+String.valueOf(min)+":0"+String.valueOf(sec);
									}
								}
							}
							currMap.put("duration", duration);
							//최초
							//resultHMap.put((String)currMap.get("EXT"), selectAgent.get(j));
							resultHMap.put((String)currMap.get("EXT"), currMap);
						}else
						{
							//비교
							long curDateTime = dayTime.parse(currMap.get("UPDATETIME").toString()).getTime();
							long resultDateTime = dayTime.parse(resultMap.get("UPDATETIME").toString()).getTime();
							long minute = Math.abs((curDateTime - resultDateTime) / 60000 );
	
							if(minute <2) {
								if(currMap.get("CTI").equals("CALLING") && currMap.get("RTP").equals("1")) {
									//위에 플레그..
									if (bAddFlag == true)
									{
										resultHMap.put((String) currMap.get("EXT") , currMap);
										bAddFlag = false;
									}else
									{
										if(resultMap.get("CTI").equals("CALLING") && resultMap.get("RTP").equals("1")) {
											//버림.
										}else
										{
											resultHMap.put((String) currMap.get("EXT") , currMap);
										}
										bAddFlag = true;
									}
	
								}
							}else {
								if (curDateTime - resultDateTime > 0)
										resultHMap.put((String) currMap.get("EXT") , currMap);
							}
						}
	
					}
				}catch(NullPointerException e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
				catch(Exception e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
			}
	
	
			Collection<Object> values = resultHMap.values();
	
			for(Object obj : values) {
				arr.add(obj);
			}
			jRes.addAttribute("agentExt",arr);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
	
		return jRes;
	}
	
	
	//오피스 모니터링 레디스 AJAX
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/officeMonitoringSelect.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO officeMonitoringSelect(HttpServletRequest request, Locale locale, Model model) throws Exception{
	
		AJaxResVO jRes = new AJaxResVO();
		String[] sysId = null;
		LoginVO userInfo = SessionManager.getUserInfo(request);
		JSONArray arr = new JSONArray();
		String masking = "";
	
		if (userInfo != null) {
	
			boolean nowAcc = nowAccessChk(request,"realtimeMonitoring");
	
			if(!nowAcc) {
				return jRes;
			}
	
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_IP");
			List<EtcConfigInfo> redisIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			sysId = redisIp.get(0).getConfigValue().split(",");
	
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_PORT");
			List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("Masking");
	
			List<EtcConfigInfo> maskingYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	
			masking = maskingYn.get(0).getConfigValue();
	
			Map<String, Object>  resultHMap = new HashMap<>();
	
			// 레디스 갯수
			int redisCnt = 0;
	
			//	현재시간
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
	
			boolean bAddFlag = false;
			for(int i=0;i<sysId.length;i++) {
				try {
					String selectCode = request.getParameter("selectCode");
					String selectArr = request.getParameter("selectArr");
					JSONArray selectAgent = redisService.getExtStatus(sysId[i],"RECSEE_EXTSTATUS",Integer.parseInt(redisPort.get(0).getConfigValue()),selectCode,selectArr);
					JSONObject resultObject;
					redisCnt = selectAgent.size();
					for(int j=0;j<redisCnt;j++) {
	
						Map<String,Object> currMap = getMapFromJsonObject((JSONObject)selectAgent.get(j));
						//Map<String,Object> resultMap = getMapFromJsonObject((JSONObject)resultHMap.get(currMap.get("EXT")));

						Map<String,Object> resultMap=null;
						try {
							if(resultHMap.containsKey(currMap.get("EXT"))) {
								if("1".equals(currMap.get("RTP"))) {
									if(resultHMap.get(currMap.get("EXT")) != null) {
										resultObject = new JSONObject((Map)resultHMap.get(currMap.get("EXT")));
										resultMap = getMapFromJsonObject(resultObject);
									} else {
										resultMap = null;
									}
								} else {
									continue;
								}
							} else {
								if(resultHMap.get(currMap.get("EXT")) != null) {
									resultObject = new JSONObject((Map)resultHMap.get(currMap.get("EXT")));
									resultMap = getMapFromJsonObject(resultObject);
								} else {
									resultMap = null;
								}
							}
						}catch (Exception e) {
							logger.info("==========================================================");
							logger.info("officeMonitoring : " + j + ", " + sysId[i]);
							logger.info(currMap.get("EXT") + ", " + currMap.get("AGENTNAME"));
							logger.info("==========================================================");
							logger.error("error",e);
						}
						
						String cust = new RecSeeUtil().makePhoneNumber((String) currMap.get("CUSTNUM"));
						if(masking.equals("Y")) {
							cust = new RecSeeUtil().maskingNumber(cust);
							JSONObject chageCust = (JSONObject)selectAgent.get(j);
							chageCust.put("CUSTNNUM", cust);
						}
						currMap.put("CUSTNUM", cust);
	
						if (resultMap  == null)
						{  //최초
							//resultHMap.put((String)currMap.get("EXT"), selectAgent.get(j));
							resultHMap.put((String)currMap.get("EXT"), currMap);
						}else
						{
							//비교
							long curDateTime = dayTime.parse(currMap.get("UPDATETIME").toString()).getTime();
							long resultDateTime = dayTime.parse(resultMap.get("UPDATETIME").toString()).getTime();
							long minute = Math.abs((curDateTime - resultDateTime) / 60000 );
	
							if(minute <2) {
								if(currMap.get("CTI").equals("CALLING") && currMap.get("RTP").equals("1")) {
									//위에 플레그..
									if (bAddFlag == true)
									{
										resultHMap.put((String) currMap.get("EXT") , currMap);
										bAddFlag = false;
									}else
									{
										if(resultMap.get("CTI").equals("CALLING") && resultMap.get("RTP").equals("1")) {
											//버림.
										}else
										{
											resultHMap.put((String) currMap.get("EXT") , currMap);
										}
										bAddFlag = true;
									}
	
								}
							}else {
								if (curDateTime - resultDateTime > 0)
										resultHMap.put((String) currMap.get("EXT") , currMap);
							}
						}
	
					}
				}catch(NullPointerException e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
				catch(Exception e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
			}
	
	
			Collection<Object> values = resultHMap.values();
	
			for(Object obj : values) {
				arr.add(obj);
			}
			jRes.addAttribute("agentExt",arr);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
	
		return jRes;
	}
	
	//그리드 모니터링 레디스 AJAX 헤드 불러오기
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/channelMonitoringHeader.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO channelMonitoringHeader(HttpServletRequest request, Locale locale, Model model) throws Exception{
	
		AJaxResVO jRes = new AJaxResVO();
		
		String systemIp = request.getParameter("serverip");
		String redisTable = request.getParameter("redisTable");	
		
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("REDIS_PORT");
		List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		ArrayList<String> selectTitle = redisService.getRecSeeRTPTitle(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));
			
		JSONArray selectData = redisService.getRecSeeRTPMessage(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));
		ArrayList<ArrayList<String>> arrayListfull = new ArrayList<>();
		
		
		for(int i = 0; i < selectData.size();i++) {
			JSONObject jsonObject = (JSONObject) selectData.get(i);
			ArrayList<String> arrayListRow = new ArrayList<>();	
			for(int j=0; j< selectTitle.size();j++) {
				String KeyValue = selectTitle.get(j);					
				
				if("cti".equals(KeyValue.toLowerCase())) {
					
					String status = ((String)jsonObject.get(KeyValue)).toLowerCase();
					String statusTxt = ((String)jsonObject.get(KeyValue)).toLowerCase();			
					if (statusTxt.equals("login")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg8", null,Locale.getDefault());  /*"로그인"*/;
					} else if (statusTxt.equals("logout")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg9", null,Locale.getDefault());  /*"로그아웃"*/;
					} else if (statusTxt.equals("calling")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg10", null,Locale.getDefault());  /*"전화중"*/;
					} else if (statusTxt.equals("aftercallwork")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg11", null,Locale.getDefault());  /*"후처리"*/;
					} else if (statusTxt.equals( "ready")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg12", null,Locale.getDefault());  /*"준비"*/;
					} else if (statusTxt.equals("ringing")){
						statusTxt = messageSource.getMessage("monitoring.alert.msg13", null,Locale.getDefault());  /*"연결중"*/;
					}else if(statusTxt.equals("offering")){
						statusTxt = messageSource.getMessage("monitoring.alert.msg9", null,Locale.getDefault());  /*"로그아웃"*/;
					} else {
						statusTxt = messageSource.getMessage("monitoring.alert.msg14", null,Locale.getDefault());  /*"이석"*/;
					}
					
					if(jsonObject.get("Status") != null){
						if(jsonObject.get("Status").equals("RECORDING")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg10", null,Locale.getDefault());  /*"전화중"*/;
							status = "calling";
						}
					}
	
					String statusString = "<p class='grid_status grid_status_login agnet_CTI'>로그인</p>".replace("login", status);
					statusString = statusString.replace("로그인", statusTxt);
					
					arrayListRow.add(statusString);
				}else if("rtp".equals(KeyValue.toLowerCase())) {
					String rtpValue = ((String)jsonObject.get(KeyValue));
					String ctiValue = ((String)jsonObject.get("CTI"));
					String stayListen = "";
					if(("1".equals(rtpValue) && "calling".toUpperCase().equals(ctiValue)) || "1".equals(rtpValue) && jsonObject.get("Status").equals("RECORDING")) {
						stayListen = "<p class='recodeButton card_record_press agnet_RTP' style='background-repeat: round'>"+rtpValue+"</p>";
					}else {
						stayListen = "<p class='recodeButton card_record_acw agnet_RTP' style='background-repeat: round'>"+rtpValue+"</p>";
					}
					arrayListRow.add(stayListen);
				}else if ("calltype".equals(KeyValue.toLowerCase())) {
					String callTypeValue = ((String)jsonObject.get(KeyValue));
					if(callTypeValue.length() > 0) {
						String statusTxt = messageSource.getMessage("call.type."+callTypeValue, null,Locale.getDefault());  /*"전화중"*/;
						arrayListRow.add(statusTxt);
					}else {
						arrayListRow.add((String)jsonObject.get(KeyValue));
					}
				}
				else {		
					arrayListRow.add((String)jsonObject.get(KeyValue));
				}			
				
			}
			arrayListfull.add(arrayListRow);				
		}
		
		
		if(selectData.size() > 0) {		
			jRes.addAttribute("selectData", arrayListfull);
			jRes.addAttribute("selectTitle", selectTitle);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not RedisInfo");
		}
	
		return jRes;
	}
	
	//그리드 모니터링 레디스 AJAX
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/channelMonitoringSelect.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO channelMonitoringSelect(HttpServletRequest request, Locale locale, Model model) throws Exception{
	
		AJaxResVO jRes = new AJaxResVO();
			
		String systemIp = request.getParameter("serverip");
		String redisTable = request.getParameter("redisTable");	
		
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("REDIS_PORT");
		List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		ArrayList<String> selectTitle = redisService.getRecSeeRTPTitle(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));
			
		JSONArray selectData = redisService.getRecSeeRTPMessage(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));
		ArrayList<ArrayList<String>> arrayListfull = new ArrayList<>();
		
		
		for(int i = 0; i < selectData.size();i++) {
			JSONObject jsonObject = (JSONObject) selectData.get(i);
			ArrayList<String> arrayListRow = new ArrayList<>();	
			for(int j=0; j< selectTitle.size();j++) {
				String KeyValue = selectTitle.get(j);					
				
				if("cti".equals(KeyValue.toLowerCase())) {
					
					String status = ((String)jsonObject.get(KeyValue)).toLowerCase();
					String statusTxt = ((String)jsonObject.get(KeyValue)).toLowerCase();			
					if (statusTxt.equals("login")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg8", null,Locale.getDefault());  /*"로그인"*/;
					} else if (statusTxt.equals("logout")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg9", null,Locale.getDefault());  /*"로그아웃"*/;
					} else if (statusTxt.equals("calling")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg10", null,Locale.getDefault());  /*"전화중"*/;
					} else if (statusTxt.equals("aftercallwork")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg11", null,Locale.getDefault());  /*"후처리"*/;
					} else if (statusTxt.equals( "ready")) {
						statusTxt = messageSource.getMessage("monitoring.alert.msg12", null,Locale.getDefault());  /*"준비"*/;
					} else if (statusTxt.equals("ringing")){
						statusTxt = messageSource.getMessage("monitoring.alert.msg13", null,Locale.getDefault());  /*"연결중"*/;
					}else if(statusTxt.equals("offering")){
						statusTxt = messageSource.getMessage("monitoring.alert.msg9", null,Locale.getDefault());  /*"로그아웃"*/;
					} else {
						statusTxt = messageSource.getMessage("monitoring.alert.msg14", null,Locale.getDefault());  /*"이석"*/;
					}
					
					if(jsonObject.get("Status") != null){
						if(jsonObject.get("Status").equals("RECORDING")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg10", null,Locale.getDefault());  /*"전화중"*/;
							status = "calling";
						}
					}
	
					String statusString = "<div class='grid_status grid_status_login'>로그인</div>".replace("login", status);
					statusString = statusString.replace("로그인", statusTxt);
					
					arrayListRow.add(statusString);
				}else if("rtp".equals(KeyValue.toLowerCase())) {
					String rtpValue = ((String)jsonObject.get(KeyValue));
					String ctiValue = ((String)jsonObject.get("CTI"));
					String stayListen = "";
					if(("1".equals(rtpValue) && "calling".toUpperCase().equals(ctiValue)) || "1".equals(rtpValue) && jsonObject.get("Status").equals("RECORDING")) {
						stayListen = "<div class='recodeButton card_record_press'></div>";
					}else {
						stayListen = "<div class='recodeButton card_record_acw'></div>";
					}
					arrayListRow.add(stayListen);
				}else if ("calltype".equals(KeyValue.toLowerCase())) {
					String callTypeValue = ((String)jsonObject.get(KeyValue));
					if(callTypeValue.length() > 0) {
						String statusTxt = messageSource.getMessage("call.type."+callTypeValue, null,Locale.getDefault());  /*"전화중"*/;
						arrayListRow.add(statusTxt);
					}else {
						arrayListRow.add((String)jsonObject.get(KeyValue));
					}
				}
				else {		
					arrayListRow.add((String)jsonObject.get(KeyValue));
				}			
				
			}
			arrayListfull.add(arrayListRow);				
		}
		
		
		if(selectData.size() > 0) {		
			jRes.addAttribute("selectData", arrayListfull);
			jRes.addAttribute("selectTitle", selectTitle);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not RedisInfo");
		}
	
		return jRes;
	}
	
	
	private boolean nowAccessChk(HttpServletRequest request,String string) {
		boolean readYn = false;
	
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, string);
	
		if(nowAccessInfo.getReadYn().equals("Y"))
		{
			readYn = true;
		}
		return readYn;
	}

	//그리드 모니터링 레디스 AJAX
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelMonitoringSysInfoSend.do", produces = "text/plain;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO channelMonitoringSysInfoSend(HttpServletRequest request, Locale locale, Model model)	throws Exception {
		AJaxResVO jRes = new AJaxResVO();

		SystemInfo systemInfo = new SystemInfo();
		List<SystemInfo> systemInfoResult = systemInfoService.selectSystemInfo(systemInfo);

		if(systemInfoResult.size() > 0) {		
			jRes.addAttribute("systemInfoResult", systemInfoResult);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not ServerInfo");
		}

		return jRes;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelMonitoringRedisInfoSend.do", produces = "text/plain;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO channelMonitoringRedisInfoSend(HttpServletRequest request, Locale locale, Model model)	throws Exception {
		AJaxResVO jRes = new AJaxResVO();
		ArrayList redisArrayList = new ArrayList<>();
		ArrayList redisArrayListName = new ArrayList<>();
		
		String redisIp = request.getParameter("serverip");		
		
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("MONITORING");
		etcConfigInfo.setConfigKey("REDIS_PORT");
		List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisIp, Integer.parseInt(redisPort.get(0).getConfigValue()),1000);
		
		Jedis jedis = jedisPool.getResource();
		
		Set set = jedis.keys("*");
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			String tempValue =iterator.next().toString();
			
			if(!(tempValue.toUpperCase().contains("EXTSTATUS")) && !(tempValue.toUpperCase().contains("LOG"))) {			
				redisArrayList.add(tempValue);
				redisArrayListName.add(tempValue.replaceAll("RECSEE_", ""));
				
			}
		}
	

		
		if(redisArrayList.size() > 0) {		
			jRes.addAttribute("redisArrayList", redisArrayList);
			jRes.addAttribute("redisArrayListName", redisArrayListName);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not RedisInfo");
		}

		return jRes;
	}
	
	@RequestMapping("getLimitValue")
	public @ResponseBody AJaxResVO getLimitValue(HttpServletRequest request, Locale locale, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String[] sysLimit = null;
		
		if (userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("SYS_LIMIT_VALUE");
			List<EtcConfigInfo> sysLimits = null;
			try {
				sysLimits = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				sysLimit = sysLimits.get(0).getConfigValue().split(",");
			} catch(Exception e) {
				etcConfigInfo.setConfigValue("80.0,80.0,80.0");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				sysLimit = etcConfigInfo.getConfigValue().split(",");
			}
			
		}
		jRes.addAttribute("sysLimit",sysLimit);
		
		return jRes;
	}
	
	@RequestMapping("getSysRedis")
	public @ResponseBody JSONArray getSysRedis(HttpServletRequest request, Locale locale, Model model) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		JSONArray systemInfoList = new JSONArray();
		String[] redisIp = {"127.0.0.1"};
		String redisPort = "6379";
		String redisPw = "0";
		String redisTimeout = "0";
		
		if(!StringUtil.isNull(request.getParameter("redisIp"))) {
			redisIp = request.getParameter("redisIp").split(",");
			Arrays.sort(redisIp);
		}
		if(!StringUtil.isNull(request.getParameter("redisPort"))) {
			redisPort = request.getParameter("redisPort");
		}
		if(!StringUtil.isNull(request.getParameter("redisPw"))) {
			redisPw = request.getParameter("redisPw");
		}
		if(!StringUtil.isNull(request.getParameter("redisTimeout"))) {
			redisTimeout = request.getParameter("redisTimeout");
		}
		
		if (userInfo != null) {
			for (int i = 0; i < redisIp.length; i++) {
				JSONArray getSystemResult = redisService.getSystem(redisIp[i], Integer.parseInt(redisPort), Integer.parseInt(redisTimeout), "SYS_MONITORING", redisPw);
				if (getSystemResult != null) {
					for(int j = 0; j < getSystemResult.size(); j++) {
						systemInfoList.add(getSystemResult.get(j)); // etcConfig 에서 redis 정보 받아오기
					}
				}
			}
		}
		
		return systemInfoList;
	}

	@RequestMapping("getSysRedisGroupCode")
	public @ResponseBody AJaxResVO getSysRedisIP(HttpServletRequest request, Locale locale, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String[] arrRedisIp = {"127.0.0.1"};
		
		if (userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("SYSTEM_MONITORING_REDIS_IP");
			List<EtcConfigInfo> redisIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (redisIp.size() > 0) {
				// 현재 설정값 선택시키기 위해 그룹코드 리스트 + 맨 마지막에 설정 값 넣어줌...   
				String tmpStr = redisIp.get(0).getConfigOption() + "/" + redisIp.get(0).getConfigValue();
				arrRedisIp = tmpStr.split("/");
			}
		}
		jRes.addAttribute("arrRedisIp",arrRedisIp);
		return jRes;
	}
	
	@RequestMapping(value = "/setSysRedisGroupCode", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO setSysRedisIP(HttpServletRequest request, Locale locale, Model model) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		
		if (userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("SYSTEM_MONITORING_REDIS_IP");
			
			String redisIpResult = "";
			if(!StringUtil.isNull(request.getParameter("sysRedisGroupCode"))) {
				String redisGroupCode = request.getParameter("sysRedisGroupCode");
				etcConfigInfo.setConfigValue(redisGroupCode);
				etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);
				
				SystemRealtimeInfo sysRealtimeInfo = new SystemRealtimeInfo();
				sysRealtimeInfo.setrSysGroupCode(redisGroupCode);
				List<SystemRealtimeInfo> sysRealtimeInfoResult = monitoringInfoService.selectSystemRealtimeInfo(sysRealtimeInfo);
				
				for (int i = 0; i < sysRealtimeInfoResult.size(); i++) {
					redisIpResult += ","+sysRealtimeInfoResult.get(i).getrSysIp();
				}
				redisIpResult = redisIpResult.substring(1);
				
				jRes.addAttribute("redisIp", redisIpResult);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				jRes.addAttribute("msg", "nullParam");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}
		return jRes;
	}
	
	@RequestMapping(value = "/getRealtimeSetting", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Map<String,String> getRealtimeSetting(HttpServletRequest request, Locale locale, Model model) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		Map<String,String> result = new HashMap<String,String>();
		
		if (userInfo != null) {
				List<SystemRealtimeInfo> sysRealtimeInfoResult = monitoringInfoService.selectSystemRealtimeInfo(new SystemRealtimeInfo());
				
				for(int i = 0; i < sysRealtimeInfoResult.size(); i++) {
					SystemRealtimeInfo sysRealtimeInfo = sysRealtimeInfoResult.get(i);
					result.put(sysRealtimeInfo.getrSysIp().replaceAll("\\.", ""),sysRealtimeInfo.getrSysName());
				}
		}
		return result;
	}
	
/*	String showMemory() {
		Runtime r = Runtime.getRuntime();
		long max = r.maxMemory();
		long total = r.totalMemory();
		//long free = r.freeMemory();


		String use=getFileSize(total);
		String notUse=getFileSize(max-total);

		String result= use+"/"+notUse;
		return result;
	}*/
	/*
	==============================================
	JSONARRAY to MAP
	==============================================
	*/
	public static List<Map<String,Object>> getListMapFromJsonArray(JSONArray jsonArray){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		if(jsonArray != null) {
			int jsonSize = jsonArray.size();
			for(int i = 0;i<jsonSize;i++) {
				Map<String,Object> map = getMapFromJsonObject((JSONObject)jsonArray.get(i));
				list.add(map);
			}
		}

		return list;
	}

	/*
	==============================================
	JSONObject to MAP
	==============================================
	*/

	@SuppressWarnings("unchecked")
	public static Map<String,Object> getMapFromJsonObject(JSONObject jsonObj){
		if (jsonObj == null)
			return null;

		Map<String,Object> map = null;

		try {
			map = new ObjectMapper().readValue(jsonObj.toJSONString(),Map.class);
		} catch (JsonParseException e) {
			logger.error("",e);
			logger.error("error",e);
        } catch (JsonMappingException e) {
			logger.error("",e);
			logger.error("error",e);
        } catch (IOException e) {
			logger.error("",e);
			logger.error("error",e);
        } catch(NullPointerException e) {
			logger.error("",e);
			logger.error("error",e);
		} catch(Exception e) {
			logger.error("",e);
			logger.error("error",e);
		}
		return map;
	}


/*	String getFileSize(long l) {

			if (l < 1024)
				return l+"B";
			else if( (1024*1024 > l ) && (l >= 1024))
				return l/1024+"KB";
			else if ( (1024*1024*1024 > l ) && (l >= 1024*1024))
				return l/1024/1024+"MB";
			else
				return l/1024/1024/1024+"GB";
	}

	String mathItem(String item_index) {
		String result="";
		switch (item_index) {
		case "1":
			result="#cpuWrap";
			break;
		case "2":
			result="#memoryWrap";
			break;
		case "3":
			result="#hddWrap";
			break;
		case "4":
			result="#recWrapParent";
			break;
		case "5":
			result="#wasWrap";
			break;
		case "7":
			result="#dbWrapParent";
			break;
		case "8":
			result="#dbWrapParent";
			break;
		}
		return result;
	}
*/
	
	
	
	
	// 시스템 모니터링 임계치 설정
	@RequestMapping(value="/setLimitValue", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO setLimitValue(HttpServletRequest request, Locale locale, Model model) throws Exception{

		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {
			String configValue = "";
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("SYS_LIMIT_VALUE");
			if(!StringUtil.isNull(request.getParameter("cpu"))) {
				configValue = request.getParameter("cpu");
			} else {
				configValue = "0.0";
			}
			
			if(!StringUtil.isNull(request.getParameter("memory"))) {
				configValue += "," + request.getParameter("memory");
			} else {
				configValue += ",0.0";
			}
			

			if(!StringUtil.isNull(request.getParameter("disk"))) {
				configValue += "," + request.getParameter("disk");
			} else {
				configValue += ",0.0";
			}

			try {
				etcConfigInfo.setConfigValue(configValue);
				etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			}
			
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 도면 SELECT
		@RequestMapping(value="/bluePrintPaint.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
		public @ResponseBody AJaxResVO selectbluePrintPaint(HttpServletRequest request, Locale locale, Model model) throws Exception{

			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			if (userInfo != null) {
				BluePrintInfo bluePrintInfo = new BluePrintInfo();
				bluePrintInfo.setrBlueprintSeq(request.getParameter("Seq"));
				
				List<BluePrintInfo> selectBluePrint = monitoringInfoService.selectBluePrint(bluePrintInfo);
				
				if(selectBluePrint.size() > 0 ) {
					jRes.addAttribute("bluePrintPaint" , selectBluePrint.get(0).getrBlueprintPaint());
				}
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not LoginInfo");
			}
			return jRes;
		}
		
		

		// 도면 모니터링 추가
		@RequestMapping(value="/createBluePrint.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
		public @ResponseBody AJaxResVO createBluePrint(HttpServletRequest request, Locale locale, Model model) throws Exception{

			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			if (userInfo != null) {
				BluePrintInfo bluePrintInfo = new BluePrintInfo();
				if(!StringUtil.isNull(request.getParameter("blueprintName"))) {
					bluePrintInfo.setrBlueprintname(request.getParameter("blueprintName"));
					bluePrintInfo.setrBlueprintjoinId(userInfo.getUserId());
					
					int res = monitoringInfoService.createBluePrint(bluePrintInfo);
					int seq = monitoringInfoService.selectLastSeq();
					
					if(res>0) {
						jRes.addAttribute("seq",seq);
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("query Fail");
					}
				}

			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not LoginInfo");
			}

			return jRes;
		}
		
		// 도면 모니터링 추가
		@RequestMapping(value="/saveBluePrint.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
		public @ResponseBody AJaxResVO saveBluePrint(HttpServletRequest request, Locale locale, Model model) throws Exception{

			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			if (userInfo != null) {
				BluePrintInfo bluePrintInfo = new BluePrintInfo();
				bluePrintInfo.setrBlueprintSeq(request.getParameter("seq"));
				bluePrintInfo.setrBlueprintPaint(request.getParameter("blueprintpaint"));
				
				
				int res = monitoringInfoService.insertBluePrintPaint(bluePrintInfo);
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not LoginInfo");
			}
			return jRes;
		}
		
		// 오늘 콜 갯수 확인
		@RequestMapping(value="/countRecFileAndCallTime.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
		public @ResponseBody AJaxResVO countRecFile(HttpServletRequest request, Locale locale, Model model) throws Exception{

			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			if (userInfo != null) {
				SearchListInfo searchListInfo = new SearchListInfo();
				searchListInfo.setExtNum(request.getParameter("ext"));
				
				List<SearchListInfo> recDetail = monitoringInfoService.selectRecCount(searchListInfo);
				
				if(recDetail.size() > 0) {
					jRes.addAttribute("count",recDetail.get(0).getrCallCount());
					jRes.addAttribute("callTtime" , recDetail.get(0).getrCallTtime());
				}else {
					jRes.addAttribute("count","0");
					jRes.addAttribute("callTtime" , "0");
				}
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not LoginInfo");
			}
			return jRes;
		}
		
		// 모니터링 공유 id 넣어주기
		@RequestMapping(value="/insertBluePrintShare.do", produces = "text/plain;charset=UTF-8" ,method = {RequestMethod.GET, RequestMethod.POST})
		public @ResponseBody AJaxResVO insertBluePrintShare(HttpServletRequest request, Locale locale, Model model) throws Exception{

			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			if (userInfo != null) {
				String[] userIdArr = request.getParameter("userId").split(",");
				
				BluePrintInfo bluePrintInfo = new BluePrintInfo();
				bluePrintInfo.setrBlueprintSeq(request.getParameter("seq"));
				
				for(int i = 0 ; i < userIdArr.length; i++) {
					bluePrintInfo.setrBlueprintshareId(userIdArr[i]);
					Integer ins = monitoringInfoService.insertBluePrintShare(bluePrintInfo);
				}
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not LoginInfo");
			}
			return jRes;
		}
}//end class
