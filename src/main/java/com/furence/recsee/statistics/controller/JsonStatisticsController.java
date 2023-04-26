package com.furence.recsee.statistics.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.statistics.model.RecFileCount;
import com.furence.recsee.statistics.model.StatisticsInfo;
import com.furence.recsee.statistics.service.StatisticsInfoService;
import com.initech.shttp.server.Logger;

@Controller
@RequestMapping("/statistics")
public class JsonStatisticsController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JsonStatisticsController.class);
	@Autowired
	private StatisticsInfoService statisticsInfoService;

	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private RUserInfoService ruserInfoService;
	private static String OS = System.getProperty("os.name").toLowerCase();

	// 통계 - 대시보드 차트 데이터(일간)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dashboard_data.json", method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody void dashboard_data(HttpServletRequest request, HttpServletResponse response) throws IOException {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		if(userInfo != null) {
			StatisticsInfo statisticsInfo = new StatisticsInfo();

			String hideTranser=etcConfigInfoService.selectHideTransfer();
			String hideConference=etcConfigInfoService.selectHideConference();
			
			JSONObject jsonData = new JSONObject();

			List<StatisticsInfo> dashboardDailyResult = statisticsInfoService.dashboardDaily(statisticsInfo);
			Integer dashboardDailyTotal = dashboardDailyResult.size();

			JSONArray jsonDaily = new JSONArray();

			Integer totalCalls = 0;
			Integer totalTime = 0;

			if(dashboardDailyTotal > 0) {

				for(int i=0; i<dashboardDailyTotal; i++) {
					StatisticsInfo dailyItem = dashboardDailyResult.get(i);

					totalCalls += dailyItem.getTotalCalls();
					totalTime += dailyItem.getTotalTime();

					JSONObject jsonDailyItem = new JSONObject();

					jsonDailyItem.put("group", dailyItem.getRecRTime());
					jsonDailyItem.put("inbound", dailyItem.getInboundCalls());
					jsonDailyItem.put("outbound", dailyItem.getOutboundCalls());
					if(!hideTranser.equals("1"))
						jsonDailyItem.put("transfer", dailyItem.getTransferCalls());
					if(!hideConference.equals("1"))
						jsonDailyItem.put("conference", dailyItem.getConferenceCalls());
					jsonDailyItem.put("internal", dailyItem.getInternalCalls());

					jsonDaily.add(jsonDailyItem);
				}
			}

			Calendar cal = Calendar.getInstance();
			String eDate = String.valueOf(cal.get(Calendar.YEAR));
			eDate += ( ( cal.get(Calendar.MONTH) + 1 ) < 10 ? "0" : "" ) + String.valueOf( cal.get(Calendar.MONTH) + 1 );
			eDate += ( cal.get(Calendar.DATE) < 10 ? "0" : "" ) + String.valueOf(cal.get(Calendar.DATE));

			cal.add(Calendar.DATE, -7);

			statisticsInfo = new StatisticsInfo();
			List<StatisticsInfo> dashboardWeeklyResult = statisticsInfoService.dashboard7DayStatistics(statisticsInfo);
			Integer dashboardWeeklyTotal = dashboardWeeklyResult.size();

			JSONArray jsonWeekly = new JSONArray();

			if(dashboardWeeklyTotal > 0) {
				for(int i=0; i<dashboardWeeklyTotal; i++) {
					StatisticsInfo weeklyItem = dashboardWeeklyResult.get(i);

					JSONObject jsonWeeklyItem = new JSONObject();
					jsonWeeklyItem.put("group", weeklyItem.getRecDate());
					jsonWeeklyItem.put("inbound", weeklyItem.getInboundCalls());
					jsonWeeklyItem.put("outbound", weeklyItem.getOutboundCalls());
					if(!hideTranser.equals("1"))
						jsonWeeklyItem.put("transfer", weeklyItem.getTransferCalls());
					if(!hideConference.equals("1"))
						jsonWeeklyItem.put("conference", weeklyItem.getConferenceCalls());
					jsonWeeklyItem.put("internal", weeklyItem.getInternalCalls());

					jsonWeekly.add(jsonWeeklyItem);
				}
			}

			JSONArray jsonCallByFile = new JSONArray();

			if(dashboardDailyTotal > 0) {
				RecSeeUtil recseeUtil = new RecSeeUtil();
				String nowServerIp = recseeUtil.getLocalServerIp();

				SystemInfo systemInfo = new SystemInfo();

		        systemInfo.setSysId(null);
		        systemInfo.setSysIp(nowServerIp);

				List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);

				int systemInfoTotal = systemInfoList.size();

				String baseDir = "";
				String separator = "/";
				if ( OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ) {
				} else {
					separator = "\\";
				}

				RecFileCount recfileObject = null;
				for(int i=0; i<dashboardDailyTotal; i++) {
					StatisticsInfo dailyItem = dashboardDailyResult.get(i);
					Integer recfileCount = 0;

					if(systemInfoTotal > 0) {
						SystemInfo systemInfoItem = systemInfoList.get(0);
						String[] diskList = null;

						if(systemInfoItem.getStoragePath() != null){
							diskList = systemInfoItem.getStoragePath().split(",");
						}
						if(diskList!=null) {
							for(int d=0; d<diskList.length; d++) {
								baseDir = diskList[d];

								String[] param = {baseDir+separator+eDate+separator+"MP3"+separator+dailyItem.getRecRTime()};
								try {
									recfileObject = new RecFileCount(param);
									recfileCount += recfileObject.getTotalFiles();
									recfileObject.initTotalFiles();
								}  catch (NullPointerException e) {
									Logger.error("", "", "", e.toString());
								}	catch (Exception e) {
									Logger.error("", "", "", e.toString());
								}
								recfileObject = null;
							}
						}
					}

					JSONObject jsonCallByFileItem = new JSONObject();

					jsonCallByFileItem.put("group", dailyItem.getRecRTime());
					jsonCallByFileItem.put("calls", dailyItem.getTotalCalls());
					//jsonCallByFileItem.put("recfile", recfileCount);

					jsonCallByFile.add(jsonCallByFileItem);
					recfileCount = 0;
				}
			}

			/*SystemInfo systemInfo = new SystemInfo();
			List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);
			int systemInfoTotal = systemInfoList.size();

			statisticsInfo = new StatisticsInfo();
			List<StatisticsInfo> dashboardSystemResult = statisticsInfoService.dashboardSystem(statisticsInfo);
			Integer dashboardSystemTotal = dashboardSystemResult.size();

			JSONArray jsonSystem = new JSONArray();

			if(dashboardSystemTotal > 0) {


				for(int i=0; i<dashboardSystemTotal; i++) {
					StatisticsInfo systemItem = dashboardSystemResult.get(i);
					String systemName = "";

					for(int j=0; j<systemInfoTotal;j++){
						SystemInfo systemInfoItem = systemInfoList.get(j);

						if(systemInfoItem.getSysId().equals(systemItem.getvSysCode())) {
							systemName = systemInfoItem.getSysName();
						}
					}

					JSONObject jsonSystemItem = new JSONObject();
					jsonSystemItem.put("group", systemName);
					jsonSystemItem.put("calltime", (systemItem.getTotalTime()/3600));

					jsonSystem.add(jsonSystemItem);
				}
			}*/

			jsonData.put("totalCalls", totalCalls);
			jsonData.put("totalTime", totalTime);
			jsonData.put("daily", jsonDaily);
			jsonData.put("weekly", jsonWeekly);
			jsonData.put("callByfile", jsonCallByFile);
			//jsonData.put("system", jsonSystem);
			jsonData.put("hideTranser", hideTranser);
			jsonData.put("hideConference", hideConference);

			response.setContentType("application/json");
			response.getWriter().write(jsonData.toString());
		}
	}

	//개인별 대시보드
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dashboard_data_user.json", method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody void dashboard_data_user(HttpServletRequest request, HttpServletResponse response) throws IOException {	
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10006");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			
			StatisticsInfo statisticsInfo = new StatisticsInfo();
			if(!StringUtil.isNull(request.getParameter("bgCode"),true)) {
				String[] lista = request.getParameter("bgCode").replaceAll("'", "").split(",");
				List<String> list = new ArrayList<String>();
				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}
				statisticsInfo.setBgCodeL(list);
			}
			if(!StringUtil.isNull(request.getParameter("mgCode"),true)) {
				String[] lista = request.getParameter("mgCode").replaceAll("'", "").split(",");
				List<String> list = new ArrayList<String>();
				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}
				statisticsInfo.setMgCodeL(list);
				
			}
			if(!StringUtil.isNull(request.getParameter("sgCode"),true)) {
				String[] lista = request.getParameter("sgCode").replaceAll("'", "").split(",");
				List<String> list = new ArrayList<String>();
				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}
				statisticsInfo.setSgCodeL(list);
				
			}
			if(!StringUtil.isNull(request.getParameter("userId"),true)) {
				String[] lista = request.getParameter("userId").replaceAll("'", "").split(",");
				List<String> list = new ArrayList<String>();
				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}
				statisticsInfo.setUserIdL(list);
				
			}
			if(!StringUtil.isNull(request.getParameter("sysCode"),true))
				statisticsInfo.setvSysCode(request.getParameter("sysCode"));
			
			if(accessResult.get(0).getAccessLevel().equals("B")) {
				List<String> list = new ArrayList<String>();
				list.add(userInfo.getBgCode());
				statisticsInfo.setBgCodeL(list);
			}else if(accessResult.get(0).getAccessLevel().equals("M")) {
				List<String> list = new ArrayList<String>();
				list.add(userInfo.getBgCode());
				List<String> list1 = new ArrayList<String>();
				list1.add(userInfo.getMgCode());
				statisticsInfo.setBgCodeL(list);
				statisticsInfo.setMgCodeL(list1);
			}else if(accessResult.get(0).getAccessLevel().equals("S")) {
				List<String> list = new ArrayList<String>();
				list.add(userInfo.getBgCode());
				List<String> list1 = new ArrayList<String>();
				list1.add(userInfo.getMgCode());
				List<String> list2 = new ArrayList<String>();
				list2.add(userInfo.getSgCode());
				statisticsInfo.setBgCodeL(list);
				statisticsInfo.setMgCodeL(list1);
				statisticsInfo.setSgCodeL(list2);
			}else if(accessResult.get(0).getAccessLevel().equals("U")) {
				List<String> list = new ArrayList<String>();
				list.add(userInfo.getUserId());
				statisticsInfo.setUserIdL(list);
			}
		
			EtcConfigInfo etcConfigAuthBeforeGroupYN = new EtcConfigInfo();
			etcConfigAuthBeforeGroupYN.setGroupKey("AUTHORITY");
			etcConfigAuthBeforeGroupYN.setConfigKey("USE_BEFORE_GROUP_SEARCH");
			String AuthBeforeGroupYNVal = "N"; // 기존 권한 - 현재 그룹만 조회 허용
			EtcConfigInfo AuthBeforeGroupYN = null;
			try { 
				AuthBeforeGroupYN = etcConfigInfoService.selectOptionYN(etcConfigAuthBeforeGroupYN);
			} catch(Exception e) {
				logger.error("error",e);
			} 
			if (AuthBeforeGroupYN != null) {
				AuthBeforeGroupYNVal = AuthBeforeGroupYN.getConfigValue();
			}

			if ("Y".equals(AuthBeforeGroupYNVal) 
					&& StringUtil.isNull(request.getParameter("bgCode"),true)
					&& StringUtil.isNull(request.getParameter("mgCode"),true)
					&& StringUtil.isNull(request.getParameter("sgCode"),true)
					&& StringUtil.isNull(request.getParameter("userId"),true)) {
				RUserInfo ruserInfo = new RUserInfo();
				if(accessResult.get(0).getAccessLevel().equals("B")) {
					ruserInfo.setBgCode(userInfo.getBgCode());
				} else if(accessResult.get(0).getAccessLevel().equals("M")) {
					ruserInfo.setMgCode(userInfo.getMgCode());
				} else if(accessResult.get(0).getAccessLevel().equals("S")) {
					ruserInfo.setSgCode(userInfo.getSgCode());
				} else if(accessResult.get(0).getAccessLevel().equals("U")) {
					ruserInfo.setUserId(userInfo.getUserId());
				}
				
				List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
				List<String> list = new ArrayList<String>();
				if (ruserInfoResult.size() > 0) {
					for (int i = 0; i < ruserInfoResult.size(); i++) {
						list.add(ruserInfoResult.get(i).getUserId());
					}
					statisticsInfo.setAuthBeforeGroup(AuthBeforeGroupYNVal);
					statisticsInfo.setUserIdL(list);
				}
			}
			
			String hideTranser=etcConfigInfoService.selectHideTransfer();
			String hideConference=etcConfigInfoService.selectHideConference();
			
			JSONObject jsonData = new JSONObject();

			StatisticsInfo callSummaryResult = statisticsInfoService.callSummary(statisticsInfo);
			int totalCalls;
			int inCalls;
			int outCalls;
			int totalTime;
			if(callSummaryResult!=null&&callSummaryResult.getTotalCalls()!=null)
				totalCalls=callSummaryResult.getTotalCalls();
			else 
				totalCalls=0;
			if(callSummaryResult!=null&&callSummaryResult.getInboundCalls()!=null)
				inCalls=callSummaryResult.getInboundCalls();  
			else 
				inCalls=0;
			if(callSummaryResult!=null&&callSummaryResult.getOutboundCalls()!=null)
				outCalls=callSummaryResult.getOutboundCalls();
			else 
				outCalls=0;
			if(callSummaryResult!=null&&callSummaryResult.getTotalTime()!=null)
				totalTime=callSummaryResult.getTotalTime();
			else 
				totalTime=0;
			
			StatisticsInfo callCountbyTypeResult = statisticsInfoService.callCountbyType(statisticsInfo);
			JSONArray jsonTypeData = new JSONArray();
			
			JSONObject jsonType = new JSONObject();
			jsonType.put("callType", messageSource.getMessage("call.type.I", null,Locale.getDefault()));
			if(callCountbyTypeResult==null || callCountbyTypeResult.getInboundCalls()==null) {
				jsonType.put("val", 0);
			}else
				jsonType.put("val", callCountbyTypeResult.getInboundCalls());
			jsonTypeData.add(jsonType);
			
			jsonType = new JSONObject();
			jsonType.put("callType",  messageSource.getMessage("call.type.O", null,Locale.getDefault()));
			if(callCountbyTypeResult==null ||callCountbyTypeResult.getOutboundCalls()==null) {
				jsonType.put("val", 0);
			}else
				jsonType.put("val", callCountbyTypeResult.getOutboundCalls());
			jsonTypeData.add(jsonType);
			
			if(!hideTranser.equals("1")) {
				jsonType = new JSONObject();
				jsonType.put("callType",  messageSource.getMessage("call.type.T", null,Locale.getDefault()));
				if(callCountbyTypeResult==null ||callCountbyTypeResult.getTransferCalls()==null) {
					jsonType.put("val", 0);
				}else
					jsonType.put("val", callCountbyTypeResult.getTransferCalls());
				jsonTypeData.add(jsonType);
			}
			
			if(!hideConference.equals("1")) {
				jsonType = new JSONObject();
				jsonType.put("callType",  messageSource.getMessage("call.type.C", null,Locale.getDefault()));
				if(callCountbyTypeResult==null ||callCountbyTypeResult.getConferenceCalls()==null) {
					jsonType.put("val", 0);
				}else
					jsonType.put("val", callCountbyTypeResult.getConferenceCalls());
				jsonTypeData.add(jsonType);
			}
			
			jsonType = new JSONObject();
			jsonType.put("callType",  messageSource.getMessage("call.type.Z", null,Locale.getDefault()));
			if(callCountbyTypeResult==null ||callCountbyTypeResult.getInternalCalls()==null) {
				jsonType.put("val", 0);
			}else
				jsonType.put("val", callCountbyTypeResult.getInternalCalls());
			jsonTypeData.add(jsonType);
			
			
			StatisticsInfo callCountbyTimeResult = statisticsInfoService.callCountbyTime(statisticsInfo);
			JSONArray jsonTimeData = new JSONArray();
			
			JSONObject jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec30", null,Locale.getDefault()));
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec30()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  Integer.parseInt(callCountbyTimeResult.getSec30()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec60", null,Locale.getDefault()) /* "1분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec60()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  Integer.parseInt(callCountbyTimeResult.getSec60()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec120", null,Locale.getDefault()) /* "2분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec120()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  Integer.parseInt(callCountbyTimeResult.getSec120()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec180", null,Locale.getDefault()) /* "3분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec180()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  Integer.parseInt(callCountbyTimeResult.getSec180()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec240", null,Locale.getDefault()) /* "4분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec240()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  Integer.parseInt(callCountbyTimeResult.getSec240()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec300", null,Locale.getDefault()) /* "5분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec300()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  Integer.parseInt(callCountbyTimeResult.getSec300()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec420", null,Locale.getDefault()) /* "7분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec420()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count", Integer.parseInt(callCountbyTimeResult.getSec420()));
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.sec600", null,Locale.getDefault()) /* "10분 미만" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getSec600()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  callCountbyTimeResult.getSec600());
			jsonTimeData.add(jsonTime);
			
			jsonTime = new JSONObject();
			jsonTime.put("time", messageSource.getMessage("statistics.userStatistics.excel.title.moresec600", null,Locale.getDefault()) /* "10분 이상" */);
			if(callCountbyTimeResult==null || callCountbyTimeResult.getMoresec600()==null) {
				jsonTime.put("count",  0);
			}else
				jsonTime.put("count",  callCountbyTimeResult.getMoresec600());
			jsonTimeData.add(jsonTime);
			
			
			List<StatisticsInfo> callCountbyDayResult = statisticsInfoService.callCountbyDay(statisticsInfo);
			
			JSONArray jsonDayData = new JSONArray();

			for(int i=0; i<callCountbyDayResult.size(); i++) {
				StatisticsInfo dayItem = callCountbyDayResult.get(i);
				JSONObject jsonDayItem = new JSONObject();
				jsonDayItem.put("Date", dayItem.getRecDate());
				jsonDayItem.put("callCount", dayItem.getTotalCalls());
				jsonDayData.add(jsonDayItem);
			}
			
			jsonData.put("totalCalls", totalCalls);
			jsonData.put("totalTime", totalTime);
			jsonData.put("inCalls", inCalls);
			jsonData.put("outCalls", outCalls);
			jsonData.put("jsonTypeData", jsonTypeData);
			jsonData.put("jsonTimeData", jsonTimeData);
			jsonData.put("jsonDayData", jsonDayData);

			response.setContentType("application/json");
			response.getWriter().write(jsonData.toString());
		}
	}
}
