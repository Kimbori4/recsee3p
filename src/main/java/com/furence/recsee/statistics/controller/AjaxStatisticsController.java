package com.furence.recsee.statistics.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.RecMemoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.ExcelView;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.statistics.model.StatisticsInfo;
import com.furence.recsee.statistics.service.StatisticsInfoService;



@Controller
public class AjaxStatisticsController {
	private static final Logger logger = LoggerFactory.getLogger(AjaxStatisticsController.class);
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private StatisticsInfoService statisticsInfoService;
	
	@Autowired
	private CustomizeInfoService customizeInfoService;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private SearchListInfoService searchListInfoService;
	
	@Autowired
	private OrganizationInfoService organizatinoInfoService;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@Autowired
	private RUserInfoService ruserInfoService;
	
	@Autowired
	private RecMemoService recMemoService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LogService logService;


	@RequestMapping(value="/staticExcel.do")
	public void staticExcel(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");

		List<String[]> contents = new ArrayList<String[]>();

		String[] row = null;
		int colPos = 0;
		int allCalls = 0, allTime = 0, inboundCalls = 0, inboundTime = 0;
		int outboundCalls = 0, outboundTime = 0, internalCalls = 0, internalTime = 0;
		int transferCalls = 0, transferTime = 0, conferenceCalls = 0, conferenceTime = 0;
		int realCalls=0, realTime=0;
		int sec30=0,sec60=0,sec120=0;
		int sec180=0,sec240=0,sec300=0;
		int sec420=0,sec600=0,moresec600=0;
		int total=0;

		String hideTranser=etcConfigInfoService.selectHideTransfer();
		String hideConference=etcConfigInfoService.selectHideConference();
		
		Map<String, String> searchTemplItem = new LinkedHashMap<String,String>();
		if(!StringUtil.isNull(request.getParameter("dayTimeBy"), true)) {
			if("dateBy".indexOf(request.getParameter("dayTimeBy"))>-1) {
				searchTemplItem.put("r_user_name","r_user_name" );
				searchTemplItem.put("r_bg_code","r_bg_code" );
				searchTemplItem.put("r_mg_code","r_mg_code" );
				searchTemplItem.put("r_sg_code","r_sg_code" );
				searchTemplItem.put("r_user_id","r_user_id" );
				searchTemplItem.put("r_ext_no","r_ext_no" );
				searchTemplItem.put("total_calls","total_calls" );
				searchTemplItem.put("total_time","total_time" );
				searchTemplItem.put("inbound_calls","inbound_calls" );
				searchTemplItem.put("inbound_time","inbound_time" );
				searchTemplItem.put("outbound_calls","outbound_calls" );
				searchTemplItem.put("outbound_time","outbound_time" );
				if(("0".equals(hideTranser))) {
					searchTemplItem.put("transfer_calls","transfer_calls" );
					searchTemplItem.put("transfer_time","transfer_time" );
				}
				if(("0".equals(hideConference))) {
					searchTemplItem.put("conference_calls","conference_calls" );
					searchTemplItem.put("conference_time","conference_time" );
				}
				searchTemplItem.put("internal_calls","internal_calls" );
				searchTemplItem.put("internal_time","internal_time" );
				searchTemplItem.put("real_calls","real_calls" );
				searchTemplItem.put("real_time","real_time" );
			}else if("timeBy".indexOf(request.getParameter("dayTimeBy"))>-1) {
				searchTemplItem.put("R_REC_RTIME","R_REC_RTIME" );
				searchTemplItem.put("r_user_name","r_user_name" );
				searchTemplItem.put("r_bg_code","r_bg_code" );
				searchTemplItem.put("r_mg_code","r_mg_code" );
				searchTemplItem.put("r_sg_code","r_sg_code" );
				searchTemplItem.put("r_user_id","r_user_id" );
				searchTemplItem.put("r_ext_no","r_ext_no" );
				searchTemplItem.put("total_calls","total_calls" );
				searchTemplItem.put("total_time","total_time" );
				searchTemplItem.put("inbound_calls","inbound_calls" );
				searchTemplItem.put("inbound_time","inbound_time" );
				searchTemplItem.put("outbound_calls","outbound_calls" );
				searchTemplItem.put("outbound_time","outbound_time" );
				if(("0".equals(hideTranser))) {
					searchTemplItem.put("transfer_calls","transfer_calls" );
					searchTemplItem.put("transfer_time","transfer_time" );
				}
				if(("0".equals(hideConference))) {
					searchTemplItem.put("conference_calls","conference_calls" );
					searchTemplItem.put("conference_time","conference_time" );
				}
				searchTemplItem.put("internal_calls","internal_calls" );
				searchTemplItem.put("internal_time","internal_time" );
				searchTemplItem.put("real_calls","real_calls" );
				searchTemplItem.put("real_time","real_time" );
			}else if("dayWeekBy".indexOf(request.getParameter("dayTimeBy"))>-1) {
				searchTemplItem.put("dayOfWeek","dayOfWeek" );
				searchTemplItem.put("r_user_name","r_user_name" );
				searchTemplItem.put("r_bg_code","r_bg_code" );
				searchTemplItem.put("r_mg_code","r_mg_code" );
				searchTemplItem.put("r_sg_code","r_sg_code" );
				searchTemplItem.put("r_user_id","r_user_id" );
				searchTemplItem.put("r_ext_no","r_ext_no" );
				searchTemplItem.put("total_calls","total_calls" );
				searchTemplItem.put("total_time","total_time" );
				searchTemplItem.put("inbound_calls","inbound_calls" );
				searchTemplItem.put("inbound_time","inbound_time" );
				searchTemplItem.put("outbound_calls","outbound_calls" );
				searchTemplItem.put("outbound_time","outbound_time" );
				if(("0".equals(hideTranser))) {
					searchTemplItem.put("transfer_calls","transfer_calls" );
					searchTemplItem.put("transfer_time","transfer_time" );
				}
				if(("0".equals(hideConference))) {
					searchTemplItem.put("conference_calls","conference_calls" );
					searchTemplItem.put("conference_time","conference_time" );
				}
				searchTemplItem.put("internal_calls","internal_calls" );
				searchTemplItem.put("internal_time","internal_time" );
				searchTemplItem.put("real_calls","real_calls" );
				searchTemplItem.put("real_time","real_time" );
			}else if("monthBy".indexOf(request.getParameter("dayTimeBy"))>-1 || "weekBy".indexOf(request.getParameter("dayTimeBy"))>-1 ) {
				searchTemplItem.put("R_REC_DATE","R_REC_DATE" );
				searchTemplItem.put("r_user_name","r_user_name" );
				searchTemplItem.put("r_bg_code","r_bg_code" );
				searchTemplItem.put("r_mg_code","r_mg_code" );
				searchTemplItem.put("r_sg_code","r_sg_code" );
				searchTemplItem.put("r_user_id","r_user_id" );
				searchTemplItem.put("r_ext_no","r_ext_no" );
				searchTemplItem.put("total_calls","total_calls" );
				searchTemplItem.put("total_time","total_time" );
				searchTemplItem.put("inbound_calls","inbound_calls" );
				searchTemplItem.put("inbound_time","inbound_time" );
				searchTemplItem.put("outbound_calls","outbound_calls" );
				searchTemplItem.put("outbound_time","outbound_time" );
				if(("0".equals(hideTranser))) {
					searchTemplItem.put("transfer_calls","transfer_calls" );
					searchTemplItem.put("transfer_time","transfer_time" );
				}
				if(("0".equals(hideConference))) {
					searchTemplItem.put("conference_calls","conference_calls" );
					searchTemplItem.put("conference_time","conference_time" );
				}
				searchTemplItem.put("internal_calls","internal_calls" );
				searchTemplItem.put("internal_time","internal_time" );
				searchTemplItem.put("real_calls","real_calls" );
				searchTemplItem.put("real_time","real_time" );
			}else {
				searchTemplItem.put("r_user_name","r_user_name" );
				searchTemplItem.put("r_bg_code","r_bg_code" );
				searchTemplItem.put("r_mg_code","r_mg_code" );
				searchTemplItem.put("r_sg_code","r_sg_code" );
				searchTemplItem.put("r_user_id","r_user_id" );
				searchTemplItem.put("r_ext_no","r_ext_no" );
				searchTemplItem.put("totalCallbytime","totalCallbytime" );
				searchTemplItem.put("sec30","sec30" );
				searchTemplItem.put("sec60","sec60" );
				searchTemplItem.put("sec120","sec120" );
				searchTemplItem.put("sec180","sec180" );
				searchTemplItem.put("sec240","sec240" );
				searchTemplItem.put("sec300","sec300" );
				searchTemplItem.put("sec420","sec420" );
				searchTemplItem.put("sec600","sec600" );
				searchTemplItem.put("moresec600","moresec600" );
			}
		}
		
		String titleBaseName =  "statistics.userStatistics.excel.title.";

		Integer colNum = searchTemplItem.size();

		row = new String[colNum];
		colPos = 0;
		Iterator<String> keys = searchTemplItem.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			if (searchTemplItem.get(key) != null && !searchTemplItem.get(key).isEmpty()) {

				switch(searchTemplItem.get(key)) {
				default :
					row[colPos++] = messageSource.getMessage(titleBaseName + searchTemplItem.get(key), null,Locale.getDefault());
					break;
				}
			}
		}
		contents.add(row);

		StatisticsInfo statisticsItem = new StatisticsInfo();

		OrganizationInfo organizationInfo = new OrganizationInfo();
		List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
		List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
		List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);
		
		MMenuAccessInfo accessInfo = new MMenuAccessInfo();
		accessInfo.setLevelCode(userInfo.getUserLevel());
		accessInfo.setProgramCode("P10005");
		List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

		String sDate="";
		String eDate="";

		if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
		if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
		statisticsItem.setsDate(sDate.replace("-", ""));
		statisticsItem.seteDate(eDate.replace("-", ""));
		
		if(!StringUtil.isNull(request.getParameter("bgCode"),true)) {
			String[] lista = request.getParameter("bgCode").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setBgCodeL(list);
		}
		if(!StringUtil.isNull(request.getParameter("mgCode"),true)) {
			String[] lista = request.getParameter("mgCode").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setMgCodeL(list);
		}
		if(!StringUtil.isNull(request.getParameter("sgCode"),true)) {
			String[] lista = request.getParameter("sgCode").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setSgCodeL(list);
		}
		if(!StringUtil.isNull(request.getParameter("userId"),true)) {
			String[] lista = request.getParameter("userId").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setUserIdL(list);
		}
		if(!StringUtil.isNull(request.getParameter("dayTimeBy"),true))
			statisticsItem.setDayTimeBy(request.getParameter("dayTimeBy"));
		if(!StringUtil.isNull(request.getParameter("sysCode"),true))
			statisticsItem.setvSysCode(request.getParameter("sysCode"));
		if(!StringUtil.isNull(request.getParameter("col"),true))
			statisticsItem.setCol(request.getParameter("col"));
		if(!StringUtil.isNull(request.getParameter("order"),true))
			statisticsItem.setOrder(request.getParameter("order"));
		
		if(accessResult.get(0).getAccessLevel().equals("B")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getBgCode());
			statisticsItem.setBgCodeL(list);
		}else if(accessResult.get(0).getAccessLevel().equals("M")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getBgCode());
			List<String> list1 = new ArrayList<String>();
			list1.add(userInfo.getMgCode());
			statisticsItem.setBgCodeL(list);
			statisticsItem.setMgCodeL(list1);
		}else if(accessResult.get(0).getAccessLevel().equals("S")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getBgCode());
			List<String> list1 = new ArrayList<String>();
			list1.add(userInfo.getMgCode());
			List<String> list2 = new ArrayList<String>();
			list2.add(userInfo.getSgCode());
			statisticsItem.setBgCodeL(list);
			statisticsItem.setMgCodeL(list1);
			statisticsItem.setSgCodeL(list2);
		}else if(accessResult.get(0).getAccessLevel().equals("U")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getUserId());
			statisticsItem.setUserIdL(list);
		}
		statisticsItem.setCount(1);
		
		List<StatisticsInfo> statisticsItemResult=new ArrayList<StatisticsInfo>();
	
		if(!StringUtil.isNull(request.getParameter("dayTimeBy"), true)) {
			 if(request.getParameter("dayTimeBy").indexOf("callTimeBy")>-1) {
				 statisticsItemResult = statisticsInfoService.callCountByCallTimeUser(statisticsItem);	
			}else {
				statisticsItemResult = statisticsInfoService.selectUserStatistics(statisticsItem);
			}
		}
		
		Integer statisticsItemTotal = statisticsItemResult.size();
		
		for( int i = 0; i < statisticsItemTotal+1; i++) {
			Iterator<String> columnList = searchTemplItem.keySet().iterator();

			String tempStrValue = null;

			row = new String[colNum];

			colPos = 0;

			while(columnList.hasNext()) {
				
				String columnName = columnList.next();
				if(searchTemplItem.get(columnName) == null) continue;
				if(i<statisticsItemTotal) {
					switch(searchTemplItem.get(columnName)) {
					
					case "R_REC_RTIME" :
						if(statisticsItemResult.get(i).getRecRTime() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getRecRTime();
						row[colPos++] = tempStrValue;
						break;
					case "R_REC_DATE" :
						if(statisticsItemResult.get(i).getRecDate() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getRecDate();
						row[colPos++] = tempStrValue;
						break;
					case "dayOfWeek" :
						if(statisticsItemResult.get(i).getDayOfWeek() == null) tempStrValue = "";
						else tempStrValue = messageSource.getMessage("statistics.title.dayOfWeek." + statisticsItemResult.get(i).getDayOfWeek(), null,Locale.getDefault());
						row[colPos++] = tempStrValue;
						break;
					case "r_user_name" :
						if(statisticsItemResult.get(i).getUserName() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getUserName();
						row[colPos++] = tempStrValue;
						break;
					case "r_bg_code" :
						if(statisticsItemResult.get(i).getBgCode() == null) tempStrValue = "";
						else tempStrValue = new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), organizationBgInfo);
						row[colPos++] = tempStrValue;
						break;
					case "r_mg_code" :
						if(statisticsItemResult.get(i).getMgCode() == null) tempStrValue = "";
						else tempStrValue = new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), statisticsItemResult.get(i).getMgCode(), organizationMgInfo);
						row[colPos++] = tempStrValue;
						break;
					case "r_sg_code" :
						if(statisticsItemResult.get(i).getSgCode() == null) tempStrValue = "";
						else tempStrValue = new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), statisticsItemResult.get(i).getMgCode(), statisticsItemResult.get(i).getSgCode(), organizationSgInfo);
						row[colPos++] = tempStrValue;
						break;
					case "r_user_id" :
						if(statisticsItemResult.get(i).getUserId() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getUserId();
						row[colPos++] = tempStrValue;
						break;
					case "r_ext_no" :
						if(statisticsItemResult.get(i).getExtNo() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getExtNo();
						row[colPos++] = tempStrValue;
						break;
					case "total_calls" :
						if(statisticsItemResult.get(i).getTotalCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getTotalCalls().toString();
						row[colPos++] = tempStrValue;
						allCalls+=statisticsItemResult.get(i).getTotalCalls();
						break;
					case "total_time" :
						if(statisticsItemResult.get(i).getTotalTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getTotalTime());
						row[colPos++] = tempStrValue;
						allTime+=statisticsItemResult.get(i).getTotalTime();
						break;
					case "inbound_calls" :
						if(statisticsItemResult.get(i).getInboundCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getInboundCalls().toString();
						row[colPos++] = tempStrValue;
						allTime+=statisticsItemResult.get(i).getInboundCalls();
						break;
					case "inbound_time" :
						if(statisticsItemResult.get(i).getInboundTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getInboundTime());
						row[colPos++] = tempStrValue;
						inboundCalls+=statisticsItemResult.get(i).getInboundTime();
						break;
					case "outbound_calls" :
						if(statisticsItemResult.get(i).getOutboundCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getOutboundCalls().toString();
						row[colPos++] = tempStrValue;
						outboundCalls +=statisticsItemResult.get(i).getOutboundCalls();
						break;
					case "outbound_time" :
						if(statisticsItemResult.get(i).getOutboundTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getOutboundTime());
						row[colPos++] = tempStrValue;
						outboundTime  +=statisticsItemResult.get(i).getOutboundTime();
						break;
					case "transfer_calls" :
						if(statisticsItemResult.get(i).getTransferCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getTransferCalls().toString();
						row[colPos++] = tempStrValue;
						transferCalls+=statisticsItemResult.get(i).getTransferCalls();
						break;
					case "transfer_time" :
						if(statisticsItemResult.get(i).getTransferTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getTransferTime());
						row[colPos++] = tempStrValue;
						transferTime +=statisticsItemResult.get(i).getTransferTime();
						break;
					case "conference_calls" :
						if(statisticsItemResult.get(i).getConferenceCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getConferenceCalls().toString();
						row[colPos++] = tempStrValue;
						conferenceCalls+=statisticsItemResult.get(i).getConferenceCalls();
						break;
					case "conference_time" :
						if(statisticsItemResult.get(i).getConferenceTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getConferenceTime());
						row[colPos++] = tempStrValue;
						conferenceTime+=statisticsItemResult.get(i).getConferenceTime();
						break;
					case "internal_calls" :
						if(statisticsItemResult.get(i).getInternalCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getInternalCalls().toString();
						row[colPos++] = tempStrValue;
						internalCalls+=statisticsItemResult.get(i).getInternalCalls();
						break;
					case "internal_time" :
						if(statisticsItemResult.get(i).getInternalTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getInternalTime());
						row[colPos++] = tempStrValue;
						internalTime+=statisticsItemResult.get(i).getInternalTime();
						break;
					case "real_calls" :
						if(statisticsItemResult.get(i).getRealCalls() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getRealCalls().toString();
						row[colPos++] = tempStrValue;
						realCalls+=statisticsItemResult.get(i).getRealCalls();
						break;
					case "real_time" :
						if(statisticsItemResult.get(i).getRealCallTime() == null) tempStrValue = "";
						else tempStrValue = seconds2time(statisticsItemResult.get(i).getRealCallTime());
						row[colPos++] = tempStrValue;
						realTime+=statisticsItemResult.get(i).getRealCallTime();
						break;
						
					case "totalCallbytime" :
						if(statisticsItemResult.get(i).getTotalCallbytime() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getTotalCallbytime();
						row[colPos++] = tempStrValue;
						total+=Integer.parseInt(statisticsItemResult.get(i).getTotalCallbytime());
						break;
					case "sec30" :
						if(statisticsItemResult.get(i).getSec30() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec30();
						row[colPos++] = tempStrValue;
						sec30+=Integer.parseInt(statisticsItemResult.get(i).getSec30());
						break;
					case "sec60" :
						if(statisticsItemResult.get(i).getSec60() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec60();
						row[colPos++] = tempStrValue;
						sec60+=Integer.parseInt(statisticsItemResult.get(i).getSec60());
						break;
					case "sec120" :
						if(statisticsItemResult.get(i).getSec120() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec120();
						row[colPos++] = tempStrValue;
						sec120+=Integer.parseInt(statisticsItemResult.get(i).getSec120());
						break;
					case "sec180" :
						if(statisticsItemResult.get(i).getSec180() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec180();
						row[colPos++] = tempStrValue;
						sec180+=Integer.parseInt(statisticsItemResult.get(i).getSec180());
						break;
					case "sec240" :
						if(statisticsItemResult.get(i).getSec240() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec240();
						row[colPos++] = tempStrValue;
						sec240+=Integer.parseInt(statisticsItemResult.get(i).getSec240());
						break;
					case "sec300" :
						if(statisticsItemResult.get(i).getSec300() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec300();
						row[colPos++] = tempStrValue;
						sec300+=Integer.parseInt(statisticsItemResult.get(i).getSec300());
						break;
					case "sec420" :
						if(statisticsItemResult.get(i).getSec420() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec420();
						row[colPos++] = tempStrValue;
						sec420+=Integer.parseInt(statisticsItemResult.get(i).getSec420());
						break;
					case "sec600" :
						if(statisticsItemResult.get(i).getSec600() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getSec600();
						row[colPos++] = tempStrValue;
						sec600+=Integer.parseInt(statisticsItemResult.get(i).getSec600());
						break;
					case "moresec600" :
						if(statisticsItemResult.get(i).getMoresec600() == null) tempStrValue = "";
						else tempStrValue = statisticsItemResult.get(i).getMoresec600();
						row[colPos++] = tempStrValue;
						moresec600+=Integer.parseInt(statisticsItemResult.get(i).getMoresec600());
						break;
					}
				}else {
					switch(searchTemplItem.get(columnName)) {
					
					case "R_REC_RTIME" :
						row[colPos++] = "전체합계";
						break;
					case "r_user_name" :
						if(colPos==0)
							row[colPos++] = "전체합계";
						else
							row[colPos++] = "";
						break;
					case "R_REC_DATE" :
						if(colPos==0)
							row[colPos++] = "전체합계";
						else
							row[colPos++] = "";
						break;
					case "dayOfWeek" :
						if(colPos==0)
							row[colPos++] = "전체합계";
						else
							row[colPos++] = "";
						break;
					case "r_bg_code" :
						row[colPos++] = "";
						break;
					case "r_mg_code" :
						row[colPos++] = "";
						break;
					case "r_sg_code" :
						row[colPos++] = "";
						break;
					case "r_user_id" :
						row[colPos++] = "";
						break;
					case "r_ext_no" :
						row[colPos++] = "";
						break;
					case "total_calls" :
						row[colPos++] = String.valueOf(allCalls);
						break;
					case "total_time" :
						row[colPos++] = seconds2time(allTime);
						break;
					case "inbound_calls" :
						row[colPos++] = String.valueOf(inboundCalls);
						break;
					case "inbound_time" :
						row[colPos++] = seconds2time(inboundTime);
						break;
					case "outbound_calls" :
						row[colPos++] = String.valueOf(outboundCalls);
						break;
					case "outbound_time" :
						row[colPos++] = seconds2time(outboundTime);
						break;
					case "transfer_calls" :
						row[colPos++] = String.valueOf(transferCalls);
						break;
					case "transfer_time" :
						row[colPos++] = seconds2time(transferTime);
						break;
					case "conference_calls" :
						row[colPos++] = String.valueOf(conferenceCalls);
						break;
					case "conference_time" :
						row[colPos++] = seconds2time(conferenceTime);
						break;
					case "internal_calls" :
						row[colPos++] = String.valueOf(internalCalls);
						break;
					case "internal_time" :
						row[colPos++] = seconds2time(internalTime);
						break;
					case "real_calls" :
						row[colPos++] = String.valueOf(realCalls);
						break;
					case "real_time" :
						row[colPos++] = seconds2time(realTime);
						break;
					case "totalCallbytime" :
						row[colPos++] = String.valueOf(total);
						break;
					case "sec30" :
						row[colPos++] = String.valueOf(sec30);
						break;
					case "sec60" :
						row[colPos++] = String.valueOf(sec60);
						break;
					case "sec120" :
						row[colPos++] = String.valueOf(sec120);
						break;
					case "sec180" :
						row[colPos++] =	String.valueOf(sec180);
						break;
					case "sec240" :
						row[colPos++] = String.valueOf(sec240);
						break;
					case "sec300" :
						row[colPos++] = String.valueOf(sec300);
						break;
					case "sec420" :
						row[colPos++] = String.valueOf(sec420);
						break;
					case "sec600" :
						row[colPos++] = String.valueOf(sec600);
						break;
					case "moresec600" :
						row[colPos++] = String.valueOf(moresec600);
						break;
					}
				}
			}
			contents.add(row);
		}
		ModelMap.put("excelList", contents);
		ModelMap.put("target", request.getParameter("fileName"));
		if(!StringUtil.isNull(request.getParameter("dayTimeBy"), true)) {
			 if(request.getParameter("dayTimeBy").indexOf("callTimeBy")>-1) {
				 ModelMap.put("marge", 6);
			}else {
				ModelMap.put("marge", 5);
			}
		}
		
		logService.writeLog(request, "EXCELDOWN", "DO", statisticsItem.toString());

		String realPath = request.getSession().getServletContext().getRealPath("/search");
		ExcelView.createXlsx(ModelMap, realPath, response);
	}
	
	@RequestMapping(value="/mobile_staticExcel.do")
	public void mobile_staticExcel(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");

		List<String[]> contents = new ArrayList<String[]>();

		String[] row = null;
		int colPos = 0;
		String divisionBy = "";
		
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("STATISTICS");
		etcConfigInfo.setConfigKey("USE_USERCOLUMN");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		String statisticsUseUserColumn = "Y";
		if(etcConfigResult.size() > 0) {
			statisticsUseUserColumn = etcConfigResult.get(0).getConfigValue();
		}
		
		Map<String, String> searchTemplItem = new LinkedHashMap<String,String>();
		if(!StringUtil.isNull(request.getParameter("divisionBy"), true)) {
			divisionBy = request.getParameter("divisionBy");
			if(request.getParameter("divisionBy").equals("groupBy")) { //그룹별 통계 엑셀 다운로드
				searchTemplItem.put("bgName","bgName");
				searchTemplItem.put("mgName","mgName");
				searchTemplItem.put("sgName","sgName");
				searchTemplItem.put("totalcalls","totalcalls");
				searchTemplItem.put("realcalling","realcalling");
				searchTemplItem.put("totalTimeCalls","totalTimeCalls");
				searchTemplItem.put("avgTimeCalls","avgTimeCalls");
				searchTemplItem.put("groupUsers","groupUsers");				
			}else if(request.getParameter("divisionBy").equals("userBy")){ //사용자별 통계 엑셀 다운로드
				searchTemplItem.put("bgName","bgName");
				searchTemplItem.put("mgName","mgName");
				searchTemplItem.put("sgName","sgName");
				searchTemplItem.put("id","id");
				searchTemplItem.put("name","name");
				searchTemplItem.put("totalcalls","totalcalls");
				searchTemplItem.put("realcalling","realcalling");
				searchTemplItem.put("totalTimeCalls","totalTimeCalls");
				searchTemplItem.put("avgTimeCalls","avgTimeCalls");
			}else if(request.getParameter("divisionBy").equals("detailBy") && "N".equals(statisticsUseUserColumn)){ //상세통계 엑셀 다운로드
				searchTemplItem.put("no","no");
				searchTemplItem.put("groupName","groupName");
				searchTemplItem.put("nameAndId","nameAndId");
				
				searchTemplItem.put("outbound","outbound");
				searchTemplItem.put("outbound_blank","outbound_blank");
				searchTemplItem.put("inbound","inbound");
				searchTemplItem.put("inbound_blank","inbound_blank");

				searchTemplItem.put("totalTimeCalls","totalTimeCalls");
				searchTemplItem.put("totalTimeCalls_blank","totalTimeCalls_blank");
				
				searchTemplItem.put("outboundTotalCallsTime","outboundTotalCallsTime");
				searchTemplItem.put("inboundTotalCallsTime","inboundTotalCallsTime");
				searchTemplItem.put("totalCallsTime","totalCallsTime");
				searchTemplItem.put("outboundAvgTimeCalls","outboundAvgTimeCalls");
				searchTemplItem.put("inboundAvgTimeCalls","inboundAvgTimeCalls");
				searchTemplItem.put("avgTimeCalls","avgTimeCalls");
			}else { //상세통계 엑셀 다운로드
				searchTemplItem.put("no","no");
				searchTemplItem.put("groupName","groupName");
				searchTemplItem.put("nameAndId","nameAndId");
				searchTemplItem.put("totalTimeCalls","totalTimeCalls");
				searchTemplItem.put("totalTimeCalls_blank","totalTimeCalls_blank");
				searchTemplItem.put("freeBeforeCust","freeBeforeCust");
				searchTemplItem.put("freeBeforeCust_blank","freeBeforeCust_blank");
				searchTemplItem.put("freeCust","freeCust");
				searchTemplItem.put("freeCust_blank","freeCust_blank");
				searchTemplItem.put("expirationCust","expirationCust");
				searchTemplItem.put("expirationCust_blank","expirationCust_blank");
				searchTemplItem.put("RegularCust","RegularCust");
				searchTemplItem.put("RegularCust_blank","RegularCust_blank");
				searchTemplItem.put("CancelMember","CancelMember");
				searchTemplItem.put("CancelMember_blank","CancelMember_blank");
				searchTemplItem.put("AdjournedMember","AdjournedMember");
				searchTemplItem.put("AdjournedMember_blank","AdjournedMember_blank");
				searchTemplItem.put("AdjournedExpectedMember","AdjournedExpectedMember");
				searchTemplItem.put("AdjournedExpectedMember_blank","AdjournedExpectedMember_blank");
				searchTemplItem.put("OtherMembers","OtherMembers");
				searchTemplItem.put("OtherMembers_blank","OtherMembers_blank");
				searchTemplItem.put("totalCallsTime","totalCallsTime");
				searchTemplItem.put("avgTimeCalls","avgTimeCalls");
				searchTemplItem.put("RegularCust,CancelMember","RegularCust,CancelMember");
				searchTemplItem.put("RegularCust,CancelMember_blank","RegularCust,CancelMember_blank");				
				searchTemplItem.put("callingTime","callingTime");
				searchTemplItem.put("callingTime_blank","callingTime_blank");
			}
		}
		
		String titleBaseName = "statistics.callStatistics.grid.title.";

		Integer colNum = searchTemplItem.size();

		row = new String[colNum];
		colPos = 0;
		Iterator<String> keys = searchTemplItem.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			if (searchTemplItem.get(key) != null && !searchTemplItem.get(key).isEmpty()) {
				//특이사항이 없다면 default에서 기본 언어팩으로 컬럼 세팅
				switch(searchTemplItem.get(key)) {
				case "RegularCust,CancelMember":
					String str1 = messageSource.getMessage(titleBaseName + "RegularCust", null,Locale.getDefault());
					String str2 = messageSource.getMessage(titleBaseName + "CancelMember", null,Locale.getDefault());
					row[colPos++] = str1 + "+" + str2;
					break;
				default :
					if(searchTemplItem.get(key).contains("_blank")) {
						row[colPos++] = "";
					}else {
						row[colPos++] = messageSource.getMessage(titleBaseName + searchTemplItem.get(key), null,Locale.getDefault());
					}					
					break;
				}
								
			}
		}
		contents.add(row);
		
		//상세통계는 헤더가 2줄이므로
		if(divisionBy.equals("detailBy")) {
			row = new String[colNum];
			colPos = 0;
			keys = searchTemplItem.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				switch(searchTemplItem.get(key)) {
				case "no":
				case "groupName":
				case "outboundTotalCallsTime":
				case "inboundTotalCallsTime":
				case "totalCallsTime":
				case "outboundAvgTimeCalls":
				case "inboundAvgTimeCalls":
				case "avgTimeCalls":
				case "nameAndId":
					row[colPos++] = "";
					break;
				case "outbound":
				case "inbound":
				case "totalTimeCalls":
				case "freeBeforeCust":
				case "freeCust":
				case "expirationCust":
				case "RegularCust":
				case "CancelMember":
				case "AdjournedMember":
				case "AdjournedExpectedMember":
				case "OtherMembers":
					//시도 연결
					row[colPos++] = messageSource.getMessage(titleBaseName + "TryAndConn", null,Locale.getDefault());
					break;
				case "RegularCust,CancelMember":
					//연결합계 시간합산
					row[colPos++] = messageSource.getMessage(titleBaseName + "sumConnAndsumTime", null,Locale.getDefault());
					break;
				case "callingTime":
					//정회원 | 휴강회원
					row[colPos++] = messageSource.getMessage(titleBaseName + "regularMemAndCancelMem", null,Locale.getDefault());
					break;
				default :
					if(searchTemplItem.get(key).contains("_blank")) {
						row[colPos++] = "";
					}else {
						row[colPos++] = messageSource.getMessage(titleBaseName + searchTemplItem.get(key), null,Locale.getDefault());
					}					
					break;
				}				
			}
			contents.add(row);
		}
		

		StatisticsInfo statisticsItem = new StatisticsInfo();

		OrganizationInfo organizationInfo = new OrganizationInfo();
		List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
		List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
		List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);
		
		MMenuAccessInfo accessInfo = new MMenuAccessInfo();
		accessInfo.setLevelCode(userInfo.getUserLevel());
		accessInfo.setProgramCode("P10059");
		List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

		String sDate="";
		String eDate="";

		if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
		if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
		statisticsItem.setsDate(sDate.replace("-", ""));
		statisticsItem.seteDate(eDate.replace("-", ""));
		
		if(!StringUtil.isNull(request.getParameter("bgCode"),true)) {
			String[] lista = request.getParameter("bgCode").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setBgCodeL(list);
		}
		if(!StringUtil.isNull(request.getParameter("mgCode"),true)) {
			String[] lista = request.getParameter("mgCode").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setMgCodeL(list);
		}
		if(!StringUtil.isNull(request.getParameter("sgCode"),true)) {
			String[] lista = request.getParameter("sgCode").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setSgCodeL(list);
		}
		if(!StringUtil.isNull(request.getParameter("userId"),true)) {
			String[] lista = request.getParameter("userId").replaceAll("'", "").split(",");
			List<String> list = new ArrayList<String>();
			for(int i=0;i<lista.length;i++) {
				list.add(lista[i]);
			}
			statisticsItem.setUserIdL(list);
		}
		if(!StringUtil.isNull(request.getParameter("divisionBy"),true))
			statisticsItem.setDivisionBy(request.getParameter("divisionBy"));
		if(!StringUtil.isNull(request.getParameter("sysCode"),true))
			statisticsItem.setvSysCode(request.getParameter("sysCode"));
		
		if(accessResult.get(0).getAccessLevel().equals("B")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getBgCode());
			statisticsItem.setBgCodeL(list);
		}else if(accessResult.get(0).getAccessLevel().equals("M")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getBgCode());
			List<String> list1 = new ArrayList<String>();
			list1.add(userInfo.getMgCode());
			statisticsItem.setBgCodeL(list);
			statisticsItem.setMgCodeL(list1);
		}else if(accessResult.get(0).getAccessLevel().equals("S")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getBgCode());
			List<String> list1 = new ArrayList<String>();
			list1.add(userInfo.getMgCode());
			List<String> list2 = new ArrayList<String>();
			list2.add(userInfo.getSgCode());
			statisticsItem.setBgCodeL(list);
			statisticsItem.setMgCodeL(list1);
			statisticsItem.setSgCodeL(list2);
		}else if(accessResult.get(0).getAccessLevel().equals("U")) {
			List<String> list = new ArrayList<String>();
			list.add(userInfo.getUserId());
			statisticsItem.setUserIdL(list);
		}
		statisticsItem.setCount(1);
		
		statisticsItem.setExcelDownload("Y");
		List<StatisticsInfo> statisticsItemResult=new ArrayList<StatisticsInfo>();
		if(!StringUtil.isNull(request.getParameter("divisionBy"), true)) {
			if(request.getParameter("divisionBy").equals("groupBy") || request.getParameter("divisionBy").equals("userBy")) {
				statisticsItemResult = statisticsInfoService.selectUsersCallsMobileStatistics(statisticsItem);
			}else {
				statisticsItemResult = statisticsInfoService.selectDetailsCallsMobileStatistics(statisticsItem);
			}
		}
		
		if(statisticsItemResult.size()>0) {
			Integer statisticsItemTotal = statisticsItemResult.size();
			for(int i=0; i<statisticsItemTotal; i++) {
				Iterator<String> columnList = searchTemplItem.keySet().iterator();
				
				String tempStrValue = null;

				row = new String[colNum];

				colPos = 0;
				if(divisionBy.equals("detailBy")) {
					while(columnList.hasNext()) {
						String columnName = columnList.next();					
						if(searchTemplItem.get(columnName) == null) continue;
						if(i < statisticsItemTotal) {
							switch(searchTemplItem.get(columnName)) {
							case "no" :
								if(statisticsItemResult.get(i).getRownumber() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getRownumber();
								row[colPos++] = tempStrValue;
								break;
							case "groupName" :
								if(statisticsItemResult.get(i).getSgCode() == null) tempStrValue = "";
								else tempStrValue = new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), statisticsItemResult.get(i).getMgCode(), statisticsItemResult.get(i).getSgCode(), organizationSgInfo);
								row[colPos++] = tempStrValue;
								break;
							case "nameAndId":
								String UserName = "";
								String UserId = "";
								if(statisticsItemResult.get(i).getUserId() != null) UserId = statisticsItemResult.get(i).getUserId();
								if(statisticsItemResult.get(i).getUserName() != null) UserName = statisticsItemResult.get(i).getUserName();
								tempStrValue = UserName+"("+UserId+")";							
								row[colPos++] = tempStrValue;
								break;
							case "outbound":
								if(statisticsItemResult.get(i).getOUTBOUND_CALLS_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getOUTBOUND_CALLS_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "outbound_blank":
								if(statisticsItemResult.get(i).getOUTBOUND_CALLS_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getOUTBOUND_CALLS_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "inbound":
								if(statisticsItemResult.get(i).getINBOUND_CALLS_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getINBOUND_CALLS_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "inbound_blank":
								if(statisticsItemResult.get(i).getINBOUND_CALLS_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getINBOUND_CALLS_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "totalTimeCalls":
								if(statisticsItemResult.get(i).getTotalCalls() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getTotalCalls().toString();
								row[colPos++] = tempStrValue;
								break;
							case "totalTimeCalls_blank":
								if(statisticsItemResult.get(i).getSuccess_connect() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getSuccess_connect().toString();
								row[colPos++] = tempStrValue;
								break;
							case "freeBeforeCust":
								if(statisticsItemResult.get(i).getCL03_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL03_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "freeBeforeCust_blank":
								if(statisticsItemResult.get(i).getCL03_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL03_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "freeCust":
								if(statisticsItemResult.get(i).getCL02_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL02_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "freeCust_blank":
								if(statisticsItemResult.get(i).getCL02_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL02_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "expirationCust":
								if(statisticsItemResult.get(i).getCL07_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL07_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "expirationCust_blank":
								if(statisticsItemResult.get(i).getCL07_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL07_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "RegularCust":
								if(statisticsItemResult.get(i).getCL01_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL01_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "RegularCust_blank":
								if(statisticsItemResult.get(i).getCL01_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL01_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "CancelMember":
								if(statisticsItemResult.get(i).getCL04_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL04_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "CancelMember_blank":
								if(statisticsItemResult.get(i).getCL04_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL04_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "AdjournedMember":
								if(statisticsItemResult.get(i).getCL05_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL05_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "AdjournedMember_blank":
								if(statisticsItemResult.get(i).getCL05_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL05_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "AdjournedExpectedMember":
								if(statisticsItemResult.get(i).getCL06_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL06_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "AdjournedExpectedMember_blank":
								if(statisticsItemResult.get(i).getCL06_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL06_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "OtherMembers":
								if(statisticsItemResult.get(i).getCL08_try() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL08_try().toString();
								row[colPos++] = tempStrValue;
								break;
							case "OtherMembers_blank":
								if(statisticsItemResult.get(i).getCL08_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL08_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "outboundTotalCallsTime":
								if(statisticsItemResult.get(i).getOutboundTime() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(Integer.toString(statisticsItemResult.get(i).getOutboundTime()));
								row[colPos++] = tempStrValue;
								break;
							case "inboundTotalCallsTime":
								if(statisticsItemResult.get(i).getInboundTime() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(Integer.toString(statisticsItemResult.get(i).getInboundTime()));
								row[colPos++] = tempStrValue;
								break;
							case "totalCallsTime":
								if(statisticsItemResult.get(i).getTotalTime() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getTotalTime().toString());
								row[colPos++] = tempStrValue;
								break;
							case "outboundAvgTimeCalls":
								if(statisticsItemResult.get(i).getOUTBOUND_AVG_CALL_TIME() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getOUTBOUND_AVG_CALL_TIME().toString());
								row[colPos++] = tempStrValue;
								break;
							case "inboundAvgTimeCalls":
								if(statisticsItemResult.get(i).getINBOUND_AVG_CALL_TIME() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getINBOUND_AVG_CALL_TIME().toString());
								row[colPos++] = tempStrValue;
								break;
							case "avgTimeCalls":
								if(statisticsItemResult.get(i).getAvgCallTime() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getAvgCallTime().toString());
								row[colPos++] = tempStrValue;
								break;
							case "RegularCust,CancelMember":
								if(statisticsItemResult.get(i).getCL01CL04_sum_conn() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getCL01CL04_sum_conn().toString();
								row[colPos++] = tempStrValue;
								break;
							case "RegularCust,CancelMember_blank":
								if(statisticsItemResult.get(i).getCL01_call_time() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getCL01CL04_call_time().toString());
								row[colPos++] = tempStrValue;
								break;
							case "callingTime":
								if(statisticsItemResult.get(i).getCL01_call_time() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getCL01_call_time().toString());
								row[colPos++] = tempStrValue;
								break;
							case "callingTime_blank":
								if(statisticsItemResult.get(i).getCL04_call_time() == null) tempStrValue = "00:00:00";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getCL04_call_time().toString());
								row[colPos++] = tempStrValue;
								break;
							}
						}
					}
				}else {
					while(columnList.hasNext()) {
						String columnName = columnList.next();					
						if(searchTemplItem.get(columnName) == null) continue;
						if(i < statisticsItemTotal) {
							switch(searchTemplItem.get(columnName)) {
							case "bgName" :
								if(statisticsItemResult.get(i).getBgCode() == null) tempStrValue = "";
								else tempStrValue =  new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), organizationBgInfo);
								row[colPos++] = tempStrValue;
								break;
							case "mgName" :
								if(statisticsItemResult.get(i).getMgCode() == null) tempStrValue = "";
								else tempStrValue = new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), statisticsItemResult.get(i).getMgCode(), organizationMgInfo);
								row[colPos++] = tempStrValue;
								break;
							case "sgName" :
								if(statisticsItemResult.get(i).getSgCode() == null) tempStrValue = "";
								else tempStrValue = new FindOrganizationUtil().getOrganizationName(statisticsItemResult.get(i).getBgCode(), statisticsItemResult.get(i).getMgCode(), statisticsItemResult.get(i).getSgCode(), organizationSgInfo);
								row[colPos++] = tempStrValue;
								break;
							case "id" :
								if(statisticsItemResult.get(i).getUserId() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getUserId();
								row[colPos++] = tempStrValue;
								break;
							case "name" :
								if(statisticsItemResult.get(i).getUserName() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getUserName();
								row[colPos++] = tempStrValue;
								break;
							case "totalcalls" :
								if(statisticsItemResult.get(i).getTotalCalls() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getTotalCalls().toString();
								row[colPos++] = tempStrValue;
								break;
							case "realcalling" :
								if(statisticsItemResult.get(i).getSuccess_connect() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getSuccess_connect().toString();
								row[colPos++] = tempStrValue;
								break;
							case "totalDial" :
//								if(statisticsItemResult.get(i).get() == null) tempStrValue = "";
//								else tempStrValue = statisticsItemResult.get(i).get();
								row[colPos++] = "";
								break;
							case "totalTimeCalls" :
								if(statisticsItemResult.get(i).getTotalTime() == null) tempStrValue = "";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getTotalTime().toString());
								row[colPos++] = tempStrValue;
								break;
							case "avgTimeCalls" :
								if(statisticsItemResult.get(i).getAvgCallTime() == null) tempStrValue = "";
								else tempStrValue = secToHHMMSS(statisticsItemResult.get(i).getAvgCallTime());
								row[colPos++] = tempStrValue;
								break;
							case "groupUsers" :
								if(statisticsItemResult.get(i).getTotal_user() == null) tempStrValue = "";
								else tempStrValue = statisticsItemResult.get(i).getTotal_user();
								row[colPos++] = tempStrValue;
								break;
							}
						}
					}
				}				
			contents.add(row);
			}
			
			ModelMap.put("excelList", contents);
			ModelMap.put("target", request.getParameter("fileName"));
			
			logService.writeLog(request, "EXCELDOWN", "DO", statisticsItem.toString());
	
			String realPath = request.getSession().getServletContext().getRealPath("/search");
			if(divisionBy.equals("detailBy")) {
				ExcelView.createXlsx_mobile_detail_statistics(ModelMap, realPath, response, statisticsUseUserColumn);
			}else {
				ExcelView.createXlsx(ModelMap, realPath, response);
			}		
		}
	}

	
	//개인별 통계에 회의통화, 전환통화 표시여부
	@RequestMapping(value = "/useCallType.do",produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO useCallType(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo != null) {
			
			String hideTranser=etcConfigInfoService.selectHideTransfer();
			String hideConference=etcConfigInfoService.selectHideConference();
			
			if(hideTranser!=null && hideConference!=null) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);	
				jRes.addAttribute("hideTranser", hideTranser);
				jRes.addAttribute("hideConference", hideConference);
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);	
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
		
	}
	//토탈
	@RequestMapping(value = "/totalcount.do",produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO totalcount(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo != null) {

			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10005");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			
			StatisticsInfo statisticsInfo = new StatisticsInfo();
			String sDate="";
			String eDate="";

			if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
			if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
			statisticsInfo.setsDate(sDate.replace("-", ""));
			statisticsInfo.seteDate(eDate.replace("-", ""));
			
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
			//어떤 쿼리 날릴지 선택. 마이바티스에서 조건절로 쿼리 분리
			if(!StringUtil.isNull(request.getParameter("dayTimeBy"),true))
				statisticsInfo.setDayTimeBy(request.getParameter("dayTimeBy"));
			
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
			
			List<StatisticsInfo> statisticsItemResult= statisticsInfoService.selectUserTotalStatistics(statisticsInfo);	
			
			if(statisticsItemResult.size()>0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);	
				jRes.addAttribute("total", statisticsItemResult.get(0));
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);	
				jRes.setResult("Not result");
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
		
	}
	
	
	//모바일 유저 토탈
	@RequestMapping(value = "/mobileUserAndGroupTotalCount.do",produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO mobileUserAndGroupTotalCount(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo != null) {

			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10059");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			
			StatisticsInfo statisticsInfo = new StatisticsInfo();
			String sDate = null;
			String eDate = null;

			if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
			if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
			if( sDate != null ) {
				statisticsInfo.setsDate(sDate.replace("-", ""));
			}
			if( eDate != null ) {
				statisticsInfo.seteDate(eDate.replace("-", ""));
			}

			statisticsInfo.setsDate(sDate.replace("-", ""));
			statisticsInfo.seteDate(eDate.replace("-", ""));


			if(!StringUtil.isNull(request.getParameter("init"),true))
				statisticsInfo.setInit(request.getParameter("init"));
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
			if(!StringUtil.isNull(request.getParameter("divisionBy"),true))
				statisticsInfo.setDivisionBy(request.getParameter("divisionBy"));
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
					ruserInfo.setBgCode(userInfo.getMgCode());
				} else if(accessResult.get(0).getAccessLevel().equals("S")) {
					ruserInfo.setBgCode(userInfo.getSgCode());
				} else if(accessResult.get(0).getAccessLevel().equals("U")) {
					ruserInfo.setBgCode(userInfo.getUserId());
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
			
			List<StatisticsInfo> totalListResult = statisticsInfoService.totalUsersCallsMobileStatistics(statisticsInfo);
			
			if(totalListResult.size()>0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);	
				jRes.addAttribute("total", totalListResult.get(0));
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not result");
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
		
	}
	
	
	String seconds2time (int seconds) {
		int hours = 0;
	    String time = "";
	    boolean chkTime = true;
	
		hours   = seconds / 3600;
	
		seconds = seconds % 3600;
		int minutes =seconds / 60;
	    seconds = seconds%60;
	
		
			if (hours != 0) 
				time = (hours < 10) ? "0"+hours+":" : hours+":";
			else 
				time = "00:";
	
			if (!time.equals("")) 
				chkTime = true;
			else 
				chkTime = false;
	
	    if (minutes != 0 || chkTime )
		{
			time +=  (minutes < 10 && chkTime) ? "0"+minutes : String.valueOf(minutes);
	    } else {
			time += "00:";
		}
	
	    if (time.equals(""))
	    	time = ":"+String.valueOf(seconds);
	    else 
	    	time += (seconds < 10) ? ":0"+seconds : ":"+String.valueOf(seconds);
	
	    return time;
	}
	
	public static String secToHHMMSS(String param) {
		String hhmmss = "00:00:00";
		if(param.indexOf(".") > -1) 
			param = param.substring(0, param.indexOf("."));
		
		Integer sec = Integer.parseInt(param);
		if(sec > 0) {
			Integer hh  = sec/3600;
			Integer mm  = (sec%3600)/60;
			Integer ss  = (sec%3600)%60;
			
			hhmmss = (hh<10?"0"+Integer.toString(hh):Integer.toString(hh))
					+":"+ (mm<10?"0"+Integer.toString(mm):Integer.toString(mm))
					+":"+ (ss<10?"0"+Integer.toString(ss):Integer.toString(ss)); 
		}
		
		return hhmmss;
	}
	
}



