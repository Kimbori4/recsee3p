package com.furence.recsee.statistics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
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
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.SearchListRecord;
import com.furence.recsee.main.model.SearchListRecordElement;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.statistics.model.StatisticsInfo;
import com.furence.recsee.statistics.service.StatisticsInfoService;

@Controller
@RequestMapping("/statistics")
public class XmlStatisticsController {
	private static final Logger logger = LoggerFactory.getLogger(XmlStatisticsController.class);
	@Autowired
	private StatisticsInfoService statisticsInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private OrganizationInfoService organizatinoInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private RUserInfoService ruserInfoService;
	
	// 콜 통계 목록
	@RequestMapping(value="/call_statistics_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml call_statistics_list(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		int allCalls = 0, allTime = 0, inboundCalls = 0, inboundTime = 0;
		int outboundCalls = 0, outboundTime = 0, internalCalls = 0, internalTime = 0;
		int transferCalls = 0, transferTime = 0, conferenceCalls = 0, conferenceTime = 0;
		int realCalls =0, realCallTime=0;

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("EXCEPT");
			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String consentNoRecodingUse = "N";
			if(etcConfigResult.size() > 0) {
				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
			}
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("STATISTICS");
			etcConfigInfo.setConfigKey("USE_INTERNALCALLS");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String statisticsUseInternalCalls = "N";
			if(etcConfigResult.size() > 0) {
				statisticsUseInternalCalls = etcConfigResult.get(0).getConfigValue();
			}
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("STATISTICS");
			etcConfigInfo.setConfigKey("USE_REALCALLS");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String statisticsUseRealCalls = "N";
			if(etcConfigResult.size() > 0) {
				statisticsUseRealCalls = etcConfigResult.get(0).getConfigValue();
			}

			xmls = new dhtmlXGridXml();

		//	if(!StringUtil.isNull(request.getParameter("init")) && request.getParameter("init").equals("true")){
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			String titleBaseName = "statistics.callStatistics.grid.title.";
			String hideTranser=etcConfigInfoService.selectHideTransfer();
			String hideConference=etcConfigInfoService.selectHideConference();

			dhtmlXGridHeadColumn column = null;
			for(int j = 0; j < 12; j++) {
				column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setWidth("130");
				switch(j) {
				case 0:

					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").equals("timeBy")) {
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "time", null,Locale.getDefault())+"</div>");
					}else {
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "date", null,Locale.getDefault())+"</div>");
					}
					break;
				case 1:
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "group", null,Locale.getDefault())+"</div>");
					if((request.getParameter("bgCode")!=null&& !request.getParameter("bgCode").equals("")) ||(request.getParameter("mgCode")!=null&&!request.getParameter("mgCode").equals("")) || (request.getParameter("sgCode")!=null&&!request.getParameter("sgCode").equals(""))) {
						column.setHidden("0");
					}else {
						column.setHidden("1");
					}
					break;
				case 2:
					column.setType("ron");
					column.setFormat("0,000");
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "calls", null,Locale.getDefault())+"</div>");
					break;
				case 3:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "talkTime", null,Locale.getDefault())+"</div>");
					break;
				case 4:
					column.setType("ron");
					column.setFormat("0,000");
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inbound", null,Locale.getDefault())+"</div>");
					break;
				case 5:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inboundTime", null,Locale.getDefault())+"</div>");
					break;
				case 6:
					column.setType("ron");
					column.setFormat("0,000");
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "outbound", null,Locale.getDefault())+"</div>");
					break;
				case 7:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "outboundTime", null,Locale.getDefault())+"</div>");
					break;
				case 8:
					column.setType("ron");
					column.setFormat("0,000");
					column.setSort("int");
					if(hideTranser.equals("1"))
						column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "transfer", null,Locale.getDefault())+"</div>");
					break;
				case 9:
					if(hideTranser.equals("1"))
						column.setHidden("1");
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "transferTime", null,Locale.getDefault())+"</div>");
					break;
				case 10:
					column.setType("ron");
					column.setFormat("0,000");
					column.setSort("int");
					if(hideConference.equals("1"))
						column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "conference", null,Locale.getDefault())+"</div>");
					break;
				case 11:
					if(hideConference.equals("1"))
						column.setHidden("1");
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "conferenceTime", null,Locale.getDefault())+"</div>");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			if(statisticsUseInternalCalls != null && statisticsUseInternalCalls.equals("Y")) {
				column = new dhtmlXGridHeadColumn();
				column.setSort("int");
				column.setAlign("center");
				column.setWidth("130");
				column.setType("ron");
				column.setFormat("0,000");
				column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "internal", null,Locale.getDefault())+"</div>");
				head.getColumnElement().add(column);

				column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("int");
				column.setAlign("center");
				column.setWidth("130");
				column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "internalTime", null,Locale.getDefault())+"</div>");
				head.getColumnElement().add(column);
				
			}
			if(statisticsUseRealCalls != null && statisticsUseRealCalls.equals("Y")) {
				
				column = new dhtmlXGridHeadColumn();
				column.setSort("int");
				column.setAlign("center");
				column.setWidth("130");
				column.setType("ron");
				column.setFormat("0,000");
				column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "realCalls", null,Locale.getDefault())+"</div>");
				head.getColumnElement().add(column);

				column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("int");
				column.setAlign("center");
				column.setWidth("130");
				column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "realCallTime", null,Locale.getDefault())+"</div>");
				head.getColumnElement().add(column);
			}


			if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
				column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setWidth("130");
				column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "noRecodingCount", null,Locale.getDefault())+"</div>");
				head.getColumnElement().add(column);
			}

			xmls.setHeadElement(head);
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10004");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

			StatisticsInfo statisticsInfo = new StatisticsInfo();
			String sDate = "";
			String eDate = "";

			if(request.getParameter("sDate") != null) 
				sDate = request.getParameter("sDate");
			else 
				return xmls;
			if(request.getParameter("sDate") != null) 
				eDate = request.getParameter("eDate");
			else 
				return xmls;
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
			if(!StringUtil.isNull(request.getParameter("dayTimeBy"),true))
				statisticsInfo.setDayTimeBy(request.getParameter("dayTimeBy"));
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
				etcConfigAuthBeforeGroupYN.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigAuthBeforeGroupYN);
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
			
			if(statisticsUseInternalCalls != null && statisticsUseInternalCalls.equals("Y")) {
				statisticsInfo.setStatisticsUseInternalCalls(statisticsUseInternalCalls);
			}

			List<StatisticsInfo> statisticsInfoResult = statisticsInfoService.selectCallsStatistics(statisticsInfo);
			Integer statisticsInfoResultTotal = statisticsInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			dhtmlXGridRowUserdata userdataInfo = null;

			for(int i = 0; i < statisticsInfoResultTotal; i++) {
				StatisticsInfo statisticsItem = statisticsInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());


				cellInfo = new dhtmlXGridRowCell();
				if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").equals("timeBy")) {
					cellInfo.setValue(statisticsItem.getRecRTime());
				}else if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").equals("weekBy")) {
					cellInfo.setValue(statisticsItem.getRecDate());
				}else if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").equals("dayWeekBy")) {
					cellInfo.setValue(messageSource.getMessage("statistics.title.dayOfWeek." + statisticsItem.getDayOfWeek(), null,Locale.getDefault()));
				}else {
					cellInfo.setValue(statisticsItem.getRecDate());
				}
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getSgName()!=null) {
					cellInfo.setValue(statisticsItem.getSgName());
				}else if(statisticsItem.getMgName()!=null) {
					cellInfo.setValue(statisticsItem.getMgName());
				}else if(statisticsItem.getBgName()!=null){
					cellInfo.setValue(statisticsItem.getBgName());
				}else {
					cellInfo.setValue(" ");
				}

				rowItem.getCellElements().add(cellInfo);


				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getTotalCalls()!=null) {
					cellInfo.setValue(statisticsItem.getTotalCalls().toString());
					allCalls+=statisticsItem.getTotalCalls();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getTotalTime()!=null) {
					cellInfo.setValue(seconds2time(statisticsItem.getTotalTime()));
					allTime+=statisticsItem.getTotalTime();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getInboundCalls()!=null) {
					cellInfo.setValue(statisticsItem.getInboundCalls().toString());
					inboundCalls+=statisticsItem.getInboundCalls();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getInboundTime()!=null) {
					cellInfo.setValue(seconds2time(statisticsItem.getInboundTime()));
					inboundTime+=statisticsItem.getInboundTime();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getOutboundCalls()!=null) {
					cellInfo.setValue(statisticsItem.getOutboundCalls().toString());
					outboundCalls +=statisticsItem.getOutboundCalls();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getOutboundTime()!=null) {
					cellInfo.setValue(seconds2time(statisticsItem.getOutboundTime()));
					outboundTime  +=statisticsItem.getOutboundTime();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getTransferCalls()!=null) {
					cellInfo.setValue(statisticsItem.getTransferCalls().toString());
					transferCalls    +=statisticsItem.getTransferCalls();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getTransferTime()!=null) {
					cellInfo.setValue(seconds2time(statisticsItem.getTransferTime()));
					transferTime +=statisticsItem.getTransferTime();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getConferenceCalls()!=null) {
					cellInfo.setValue(statisticsItem.getConferenceCalls().toString());
					conferenceCalls+=statisticsItem.getConferenceCalls();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getConferenceTime()!=null) {
					cellInfo.setValue(seconds2time(statisticsItem.getConferenceTime()));
					conferenceTime+=statisticsItem.getConferenceTime();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getInternalCalls()!=null) {
					cellInfo.setValue(statisticsItem.getInternalCalls().toString());
					internalCalls+=statisticsItem.getInternalCalls();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getInternalTime()!=null) {
					cellInfo.setValue(seconds2time(statisticsItem.getInternalTime()));
					internalTime+=statisticsItem.getInternalTime();
				}else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(statisticsItem.getRealCalls().toString());
				realCalls+=statisticsItem.getRealCalls();
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(seconds2time(statisticsItem.getRealCallTime()));
				realCallTime+=statisticsItem.getRealCallTime();
				rowItem.getCellElements().add(cellInfo);

				if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(statisticsItem.getNoRecodingCount().toString());
				rowItem.getCellElements().add(cellInfo);
				}


				if(i+1==statisticsInfoResultTotal) {
					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("allCalls");
					userdataInfo.setValue(String.valueOf(allCalls));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("allTime");
					userdataInfo.setValue(seconds2time(allTime));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("inboundCalls");
					userdataInfo.setValue(String.valueOf(inboundCalls));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("inboundTime");
					userdataInfo.setValue(seconds2time(inboundTime));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("outboundCalls");
					userdataInfo.setValue(String.valueOf(outboundCalls));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("outboundTime");
					userdataInfo.setValue(seconds2time(outboundTime));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("transferCalls");
					userdataInfo.setValue(String.valueOf(transferCalls));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("transferTime");
					userdataInfo.setValue(seconds2time(transferTime));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("conferenceCalls");
					userdataInfo.setValue(String.valueOf(conferenceCalls));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("conferenceTime");
					userdataInfo.setValue(seconds2time(conferenceTime));
					rowItem.getUserdataElements().add(userdataInfo);

					if(statisticsUseRealCalls != null && statisticsUseRealCalls.equals("Y")) {
						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("internalCalls");
						userdataInfo.setValue(String.valueOf(internalCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("internalTime");
						userdataInfo.setValue(seconds2time(internalTime));
						rowItem.getUserdataElements().add(userdataInfo);
					}

					if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("realCalls");
						userdataInfo.setValue(String.valueOf(realCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("realCallTime");
						userdataInfo.setValue(seconds2time(realCallTime));
						rowItem.getUserdataElements().add(userdataInfo);
					}
				}

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}else{
			xmls = null;
		}
		return xmls;
	}

	// 레포트 목록
	@RequestMapping(value="/report_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody SearchListRecord report_list(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		SearchListRecord xmls = null;

		if(userInfo != null) {

			xmls = new SearchListRecord();
			xmls.setRowElements(new ArrayList<SearchListRecordElement>());

			for( int i = 1; i <= 50; i++) {
				String tmpI = Integer.toString(i);

				SearchListRecordElement xml = new SearchListRecordElement();

				xml.setId(tmpI);

				for( int j = 0; j < 8; j++ ) {
					String tmpJ = Integer.toString(j);

					String tmpVal = "";
					switch(j) {
					case 0:
						tmpVal = "0";
						break;
					case 1:
						tmpVal = "./icon_favorite_on.svg";
						break;
					default :
						tmpVal = "test" + tmpI + "-" + tmpJ;
					}
					xml.getSearchListRecordElementCell().add(tmpVal);
				}

				xmls.getRowElements().add(xml);

				xml = null;
			}
		}
		return xmls;
	}

	// 사용자 통계 목록
	@RequestMapping(value="/user_statistics_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml user_statistics_list(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("EXCEPT");
			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String consentNoRecodingUse = "N";
			if(etcConfigResult.size() > 0) {
				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
			}
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("STATISTICS");
			etcConfigInfo.setConfigKey("USE_INTERNALCALLS");
			//etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			/*String hideTranser=etcConfigInfoService.selectHideTransfer();
			String hideConference=etcConfigInfoService.selectHideConference();*/
			String statisticsUseInternalCalls = "N";
			if(etcConfigResult.size() > 0) {
				statisticsUseInternalCalls = etcConfigResult.get(0).getConfigValue();
			}
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("STATISTICS");
			etcConfigInfo.setConfigKey("USE_REALCALLS");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String statisticsUseRealCalls = "N";
			if(etcConfigResult.size() > 0) {
				statisticsUseRealCalls = etcConfigResult.get(0).getConfigValue();
			}

			xmls = new dhtmlXGridXml();

			if(request.getParameter("header") != null && request.getParameter("header").equals("true")){

				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				String titleBaseName = "statistics.userStatistics.grid.title.";
				
				dhtmlXGridHeadColumn column = null;
				for(int j = 0; j < 34; j++) {
					column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("server");
					column.setAlign("center");
					column.setWidth("130");

					switch(j) {
					case 0:
						column.setId("R_REC_RTIME");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "time", null,Locale.getDefault())+"</div>");
						break;
					case 1:
						column.setId("dayOfWeek");
						column.setValue("<div style=\"text-align:center;\">요일</div>");
						break;
					case 2:
						column.setId("r_rec_date");
						column.setValue("<div style=\"text-align:center;\">날짜</div>");
						break;
					case 3:
						column.setId("r_user_name");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "userName", null,Locale.getDefault())+"</div>");
						break;
					case 4:
						column.setId("r_bg_code");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rBgCode", null,Locale.getDefault())+"</div>");
						break;
					case 5:
						column.setId("r_mg_code");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rMgCode", null,Locale.getDefault())+"</div>");
						break;
					case 6:
						column.setId("r_sg_code");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rSgCode", null,Locale.getDefault())+"</div>");
						break;
					case 7:
						column.setId("r_user_id");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "userId", null,Locale.getDefault())+"</div>");
						break;
					case 8:
						column.setId("r_ext_no");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "extNo", null,Locale.getDefault())+"</div>");
						break;
					case 9:
						column.setId("total_calls");
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "calls", null,Locale.getDefault())+"</div>");
						break;
					case 10:
						column.setId("total_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "talkTime", null,Locale.getDefault())+"</div>");
						break;
					case 11:
						column.setId("inbound_calls");
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inbound", null,Locale.getDefault())+"</div>");
						break;
					case 12:
						column.setId("inbound_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inboundTime", null,Locale.getDefault())+"</div>");
						break;
					case 13:
						column.setId("outbound_calls");
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.callStatistics.grid.title.outbound", null,Locale.getDefault())+"</div>");
						break;
					case 14:
						column.setId("outbound_time");
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.callStatistics.grid.title.outboundTime", null,Locale.getDefault())+"</div>");
						break;
					case 15:
						column.setId("transfer_calls");
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "transfer", null,Locale.getDefault())+"</div>");
						break;
					case 16:
						column.setId("transfer_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "transferTime", null,Locale.getDefault())+"</div>");
						break;
					case 17:
						column.setId("conference_calls");
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "conference", null,Locale.getDefault())+"</div>");
						break;
					case 18:
						column.setId("conference_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "conferenceTime", null,Locale.getDefault())+"</div>");
						break;
					case 19:
						column.setId("internal_calls");
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.callStatistics.grid.title.internal", null,Locale.getDefault())+"</div>");
						break;
					case 20:
						column.setId("internal_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.callStatistics.grid.title.internalTime", null,Locale.getDefault())+"</div>");
						break;
					case 21:
						column.setValue("<div style=\"text-align:center;\">최소통화시간</div>");
						column.setHidden("1");
						break;
					case 22:
						column.setValue("<div style=\"text-align:center;\">최대통화시간</div>");
						column.setHidden("1");
						break;
					case 23:
						column.setValue("<div style=\"text-align:center;\">평균통화시간</div>");
						column.setHidden("1");
						break;
					case 24:
						column.setId("totalCallbytime");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.callStatistics.grid.title.Total", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 25:
						column.setId("sec30");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec30", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 26:
						column.setId("sec60");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec60", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 27:
						column.setId("sec120");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec120", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 28:
						column.setId("sec180");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec180", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 29:
						column.setId("sec240");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec240", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 30:
						column.setId("sec300");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec300", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 31:
						column.setId("sec420");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec420", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 32:
						column.setId("sec600");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.sec600", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					case 33:
						column.setId("moresec600");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("statistics.userStatistics.excel.title.moresec600", null,Locale.getDefault())+"</div>");
						column.setFormat("0,000");
						break;
					}

					head.getColumnElement().add(column);
					column = null;
				}
				

				if(statisticsUseRealCalls != null && statisticsUseRealCalls.equals("Y")) {
					
					column = new dhtmlXGridHeadColumn();
					column.setType("ro");
					column.setSort("server");
					column.setAlign("center");
					column.setWidth("130");
					column.setId("real_calls");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "realCalls", null,Locale.getDefault())+"</div>");
					head.getColumnElement().add(column);

					column = new dhtmlXGridHeadColumn();
					column.setType("ro");
					column.setSort("server");
					column.setAlign("center");
					column.setWidth("130");
					column.setId("real_time");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "realCallTime", null,Locale.getDefault())+"</div>");
					head.getColumnElement().add(column);
				}
				
				if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
					column = new dhtmlXGridHeadColumn();
					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setWidth("130");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "noRecodingCount", null,Locale.getDefault())+"</div>");
					head.getColumnElement().add(column);
				}

				xmls.setHeadElement(head);
			}else {
				OrganizationInfo organizationInfo = new OrganizationInfo();
				List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
				List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
				List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);

				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P10005");
				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

				StatisticsInfo statisticsInfo = new StatisticsInfo();
				String sDate=null;
				String eDate=null;
				
				if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
				if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
				if(sDate != null) {
					statisticsInfo.setsDate(sDate.replace("-", ""));
				}
				if( eDate !=null ) {
					statisticsInfo.seteDate(eDate.replace("-", ""));
				}

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
				if(!StringUtil.isNull(request.getParameter("dayTimeBy"),true))
					statisticsInfo.setDayTimeBy(request.getParameter("dayTimeBy"));
				if(!StringUtil.isNull(request.getParameter("sysCode"),true))
					statisticsInfo.setvSysCode(request.getParameter("sysCode"));
				if(!StringUtil.isNull(request.getParameter("col"),true))
					statisticsInfo.setCol(request.getParameter("col"));
				if(!StringUtil.isNull(request.getParameter("order"),true))
					statisticsInfo.setOrder(request.getParameter("order"));

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
				
				if(statisticsUseInternalCalls != null && statisticsUseInternalCalls.equals("Y")) {
					statisticsInfo.setStatisticsUseInternalCalls(statisticsUseInternalCalls);
				}

				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					statisticsInfo.setPosStart(posStart);
				}
				Integer count = 100;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("count"));
					statisticsInfo.setCount(count);
				}

				List<StatisticsInfo> statisticsInfoResult=null;

				if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {

					statisticsInfoResult = statisticsInfoService.callCountByCallTimeUser(statisticsInfo);
				}
				else {
					statisticsInfoResult = statisticsInfoService.selectUserStatistics(statisticsInfo);
				}

				Integer statisticsInfoResultTotal = statisticsInfoResult.size();

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				dhtmlXGridRowUserdata userdataInfo = null;

				for(int i = 0; i < statisticsInfoResultTotal; i++) {
					StatisticsInfo statisticsItem = statisticsInfoResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getRecRTime(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(statisticsItem.getRecRTime());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getDayOfWeek(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(messageSource.getMessage("statistics.title.dayOfWeek." + statisticsItem.getDayOfWeek(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getRecDate(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(statisticsItem.getRecDate());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getUserName());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getBgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), organizationBgInfo) );
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getMgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), organizationMgInfo) );
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getSgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), statisticsItem.getSgCode(), organizationSgInfo) );
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getUserId());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getExtNo());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getTotalCalls()!=null) {
						cellInfo.setValue(statisticsItem.getTotalCalls().toString());
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getTotalTime()!=null) {
						cellInfo.setValue(seconds2time(statisticsItem.getTotalTime()));
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getInboundCalls()!=null) {
						cellInfo.setValue(statisticsItem.getInboundCalls().toString());
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getInboundTime()!=null) {
						cellInfo.setValue(seconds2time(statisticsItem.getInboundTime()));
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getOutboundCalls()!=null) {
						cellInfo.setValue(statisticsItem.getOutboundCalls().toString());
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getOutboundTime()!=null) {
						cellInfo.setValue(seconds2time(statisticsItem.getOutboundTime()));
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getTransferCalls()!=null) {
						cellInfo.setValue(statisticsItem.getTransferCalls().toString());
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getTransferTime()!=null) {
						cellInfo.setValue(seconds2time(statisticsItem.getTransferTime()));
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getConferenceCalls()!=null) {
						cellInfo.setValue(statisticsItem.getConferenceCalls().toString());
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getConferenceTime()!=null) {
						cellInfo.setValue(seconds2time(statisticsItem.getConferenceTime()));
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getInternalCalls()!=null) {
						cellInfo.setValue(statisticsItem.getInternalCalls().toString());
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(statisticsItem.getInternalTime()!=null) {
						cellInfo.setValue(seconds2time(statisticsItem.getInternalTime()));
					}else
						cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (statisticsItem.getMinCallTime()==null)
						cellInfo.setValue("");
					else
						cellInfo.setValue(statisticsItem.getMinCallTime().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (statisticsItem.getMaxCallTime()==null)
						cellInfo.setValue("");
					else
						cellInfo.setValue(statisticsItem.getMaxCallTime().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (statisticsItem.getAvgCallTime()==null)
						cellInfo.setValue("");
					else
						cellInfo.setValue(statisticsItem.getAvgCallTime().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getTotalCallbytime());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec30());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec60());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec120());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec180());
					}else  {
						cellInfo.setValue("");
					}//26
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec240());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec300());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec420());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getSec600());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
						cellInfo.setValue(statisticsItem.getMoresec600());
					}else  {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);
					if(statisticsUseRealCalls != null && statisticsUseRealCalls.equals("Y")) {
						cellInfo = new dhtmlXGridRowCell();
						if(statisticsItem.getRealCalls()!=null) {
							cellInfo.setValue(statisticsItem.getRealCalls().toString());
						}else
							cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						cellInfo = new dhtmlXGridRowCell();
						if(statisticsItem.getRealCallTime()!=null) {
							cellInfo.setValue(seconds2time(statisticsItem.getRealCallTime()));
						}else
							cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);
					}

					if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(statisticsItem.getNoRecodingCount().toString());
						rowItem.getCellElements().add(cellInfo);
					}

					/*if(i+1==statisticsInfoResultTotal) {
						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("allCalls");
						userdataInfo.setValue(String.valueOf(allCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("allTime");
						userdataInfo.setValue(seconds2time(allTime));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("inboundCalls");
						userdataInfo.setValue(String.valueOf(inboundCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("inboundTime");
						userdataInfo.setValue(seconds2time(inboundTime));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("outboundCalls");
						userdataInfo.setValue(String.valueOf(outboundCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("outboundTime");
						userdataInfo.setValue(seconds2time(outboundTime));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("transferCalls");
						userdataInfo.setValue(String.valueOf(transferCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("transferTime");
						userdataInfo.setValue(seconds2time(transferTime));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("conferenceCalls");
						userdataInfo.setValue(String.valueOf(conferenceCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("conferenceTime");
						userdataInfo.setValue(seconds2time(conferenceTime));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("internalCalls");
						userdataInfo.setValue(String.valueOf(internalCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("internalTime");
						userdataInfo.setValue(seconds2time(internalTime));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("realCalls");
						userdataInfo.setValue(String.valueOf(realCalls));
						rowItem.getUserdataElements().add(userdataInfo);

						userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("realCallTime");
						userdataInfo.setValue(seconds2time(realCallTime));
						rowItem.getUserdataElements().add(userdataInfo);

						if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec1");
							userdataInfo.setValue(String.valueOf(sec30));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec2");
							userdataInfo.setValue(String.valueOf(sec60));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec3");
							userdataInfo.setValue(String.valueOf(sec120));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec4");
							userdataInfo.setValue(String.valueOf(sec180));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec5");
							userdataInfo.setValue(String.valueOf(sec240));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec6");
							userdataInfo.setValue(String.valueOf(sec300));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec7");
							userdataInfo.setValue(String.valueOf(sec420));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec8");
							userdataInfo.setValue(String.valueOf(sec600));
							rowItem.getUserdataElements().add(userdataInfo);

							userdataInfo = new dhtmlXGridRowUserdata();
							userdataInfo.setName("sec9");
							userdataInfo.setValue(String.valueOf(moresec600));
							rowItem.getUserdataElements().add(userdataInfo);
						}


					}*/

					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
				Integer statisticsInfoTotal =null;
				if(request.getParameter("dayTimeBy")!=null &&request.getParameter("dayTimeBy").contains("callTimeBy")) {
					statisticsInfoTotal = statisticsInfoService.totalCallCountByCallTimeUser(statisticsInfo);
				}
				else {
					statisticsInfoTotal = statisticsInfoService.totalSelectUserStatistics(statisticsInfo);
				}

				if( statisticsInfoTotal > 0 &&  (request.getParameter("posStart")==null||"0".equals(request.getParameter("posStart")))) {
					xmls.setTotal_count(statisticsInfoTotal.toString());
				} else {
					xmls.setTotal_count("");
				}
				if(request.getParameter("posStart") != null) {
					xmls.setPos(request.getParameter("posStart"));
				} else {
					xmls.setPos("0");
				}
			}
		}
		return xmls;
	}

	// 일자 시간 사용자 통계 목록
	@RequestMapping(value="/day_time_user_statistics_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml day_time_user_statistics_list(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("EXCEPT");
			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String consentNoRecodingUse = "N";
			if(etcConfigResult.size() > 0) {
				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
			}
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("STATISTICS");
			etcConfigInfo.setConfigKey("USE_INTERNALCALLS");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String statisticsUseInternalCalls = "N";
			if(etcConfigResult.size() > 0) {
				statisticsUseInternalCalls = etcConfigResult.get(0).getConfigValue();
			}

			xmls = new dhtmlXGridXml();

			if(!StringUtil.isNull(request.getParameter("init")) && request.getParameter("init").equals("true")){

				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				String titleBaseName = "statistics.userStatistics.grid.title.";

				dhtmlXGridHeadColumn column = null;
				for(int j = 0; j < 14; j++) {
					column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setWidth("130");

					switch(j) {
					case 0:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rRecDate", null,Locale.getDefault())+"</div>");
						break;
					case 1:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rRecRtime", null,Locale.getDefault())+"</div>");
						break;
					case 2:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "userName", null,Locale.getDefault())+"</div>");
						break;
					case 3:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rBgCode", null,Locale.getDefault())+"</div>");
						break;
					case 4:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rMgCode", null,Locale.getDefault())+"</div>");
						break;
					case 5:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "rSgCode", null,Locale.getDefault())+"</div>");
						break;
					case 6:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "userId", null,Locale.getDefault())+"</div>");
						break;
					case 7:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "extNo", null,Locale.getDefault())+"</div>");
						break;
					case 8:
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "calls", null,Locale.getDefault())+"</div>");
						break;
					case 9:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "talkTime", null,Locale.getDefault())+"</div>");
						break;
					case 10:
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inbound", null,Locale.getDefault())+"</div>");
						break;
					case 11:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inboundTime", null,Locale.getDefault())+"</div>");
						break;
					case 12:
						column.setType("ron");
						column.setFormat("0,000");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "outbound", null,Locale.getDefault())+"</div>");
						break;
					case 13:
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "outboundTime", null,Locale.getDefault())+"</div>");
						break;
					}
					head.getColumnElement().add(column);
					column = null;
				}
				if(statisticsUseInternalCalls != null && statisticsUseInternalCalls.equals("Y")) {
					column = new dhtmlXGridHeadColumn();

					column.setSort("str");
					column.setAlign("center");
					column.setWidth("130");

					column.setType("ron");
					column.setFormat("0,000");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "internal", null,Locale.getDefault())+"</div>");
					head.getColumnElement().add(column);

					column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setWidth("130");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "internalTime", null,Locale.getDefault())+"</div>");
					head.getColumnElement().add(column);
				}
				if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
					column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setWidth("130");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "noRecodingCount", null,Locale.getDefault())+"</div>");
					head.getColumnElement().add(column);
				}

				xmls.setHeadElement(head);
			}else {
				OrganizationInfo organizationInfo = new OrganizationInfo();
				List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
				List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
				List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);

				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P10005");
				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

				StatisticsInfo statisticsInfo = new StatisticsInfo();
				String sDate = "2015-10-01";
				String eDate = "2020-10-02";

				if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
				if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
				statisticsInfo.setsDate(sDate.replace("-", ""));
				statisticsInfo.seteDate(eDate.replace("-", ""));

				if(!StringUtil.isNull(request.getParameter("bgCode"),true))
					statisticsInfo.setBgCode(request.getParameter("bgCode"));
				if(!StringUtil.isNull(request.getParameter("mgCode"),true))
					statisticsInfo.setMgCode(request.getParameter("mgCode"));
				if(!StringUtil.isNull(request.getParameter("sgCode"),true))
					statisticsInfo.setSgCode(request.getParameter("sgCode"));

				if(!accessResult.get(0).getAccessLevel().equals("A")) {

					if(!StringUtil.isNull(statisticsInfo.getBgCode(),true) )
						statisticsInfo.setBgCode(statisticsInfo.getBgCode()+",\'"+userInfo.getBgCode()+"\'");
					else
						statisticsInfo.setBgCode("\'"+userInfo.getBgCode()+"\'");

					if(!accessResult.get(0).getAccessLevel().equals("B")) {

						if(!StringUtil.isNull(statisticsInfo.getMgCode(),true) )
							statisticsInfo.setMgCode(statisticsInfo.getMgCode()+",\'"+userInfo.getMgCode()+"\'");
						else
							statisticsInfo.setMgCode("\'"+userInfo.getMgCode()+"\'");
					}
					if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M")) {

						if(!StringUtil.isNull(statisticsInfo.getSgCode(),true) )
							statisticsInfo.setSgCode(statisticsInfo.getSgCode()+",\'"+userInfo.getSgCode()+"\'");
						else
							statisticsInfo.setSgCode("\'"+userInfo.getSgCode()+"\'");
					}

					if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M") && !accessResult.get(0).getAccessLevel().equals("S")) {
						statisticsInfo.setUserId(userInfo.getUserId());
					}
				}
				if(statisticsUseInternalCalls != null && statisticsUseInternalCalls.equals("Y")) {
					statisticsInfo.setStatisticsUseInternalCalls(statisticsUseInternalCalls);
				}

				List<StatisticsInfo> statisticsInfoResult = statisticsInfoService.selectDayTimeUserStatistics(statisticsInfo);
				Integer statisticsInfoResultTotal = statisticsInfoResult.size();

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for(int i = 0; i < statisticsInfoResultTotal; i++) {
					StatisticsInfo statisticsItem = statisticsInfoResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getRecDate());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getRecRTime());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getUserName());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getBgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), organizationBgInfo) );
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getMgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), organizationMgInfo) );
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if (StringUtil.isNull(statisticsItem.getSgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), statisticsItem.getSgCode(), organizationSgInfo) );
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getUserId());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getExtNo());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getTotalCalls().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getTotalTime().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getInboundCalls().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getInboundTime().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getOutboundCalls().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getOutboundTime().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getInternalCalls().toString());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getInternalTime().toString());
					rowItem.getCellElements().add(cellInfo);

					if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")) {
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(statisticsItem.getNoRecodingCount().toString());
						rowItem.getCellElements().add(cellInfo);
					}

					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}else{
			xmls = null;
		}

		return xmls;
	}
	
	//********************************************************************************************************************
	// 모바일 콜 통계(시간, 일, 월별 통계)
	@RequestMapping(value="/call_mobile_statistics.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml call_mobile_statistics(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		if(userInfo != null) {
			xmls = new dhtmlXGridXml();

			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			String titleBaseName = "statistics.callStatistics.grid.title.";

			Integer totalCalls = 0, success_connect = 0, giveup_call = 0, no_answer = 0, busy_call = 0;
			
			dhtmlXGridHeadColumn column = null;
			for(int j = 0; j < 10; j++) {
				column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setWidth("130");
				switch(j) {
				//구분
				case 0:
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "division", null,Locale.getDefault())+"</div>");
					column.setColumnMinWidth("130");
					break;
				//연결시도
				case 1:
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "connTry", null,Locale.getDefault())+"</div>");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//연결성공
				case 2:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "connSuccess", null,Locale.getDefault())+"</div>");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//연결성공
				case 3:
					column.setValue("#cspan");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//연결포기
				case 4:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "connFail", null,Locale.getDefault())+"</div>");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//연결포기
				case 5:
					column.setValue("#cspan");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//부재중
				case 6:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "connMiss", null,Locale.getDefault())+"</div>");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//부재중
				case 7:
					column.setValue("#cspan");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//통화중
				case 8:
					column.setSort("int");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "calling", null,Locale.getDefault())+"</div>");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				//통화중
				case 9:
					column.setValue("#cspan");
					column.setColumnMinWidth("170");
					column.setWidth("*");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}

			xmls.setHeadElement(head);
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10059");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

			StatisticsInfo statisticsInfo = new StatisticsInfo();
			String sDate = "";
			String eDate = "";

			if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
			else return xmls;
			if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
			else return xmls;
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
				etcConfigAuthBeforeGroupYN.setConfigValue("N");
				etcConfigInfoService.insertEtcConfigInfo(etcConfigAuthBeforeGroupYN);
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
			
			List<StatisticsInfo> statisticsInfoResult = statisticsInfoService.selectCallsMobileStatistics(statisticsInfo);
			Integer statisticsInfoResultTotal = statisticsInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			dhtmlXGridRowUserdata userdataInfo = null;

			for(int i = 0; i < statisticsInfoResultTotal; i++) {
				StatisticsInfo statisticsItem = statisticsInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());

				//구분
				cellInfo = new dhtmlXGridRowCell();
				if(!StringUtil.isNull(request.getParameter("divisionBy"),true)) {
					switch(request.getParameter("divisionBy")) {
					case "timeBy":
						if(!StringUtil.isNull(statisticsItem.getRecRTime())) {
							cellInfo.setValue(statisticsItem.getRecRTime());
						}else {
							cellInfo.setValue("0");
						}
						break;
						
					case "dateBy":
						if(!StringUtil.isNull(statisticsItem.getRecDate())) {
							cellInfo.setValue(statisticsItem.getRecDate());
						}else {
							cellInfo.setValue("0");
						}
						break;
						
					case "monthBy":						
						if(!StringUtil.isNull(statisticsItem.getR_month())) {
							cellInfo.setValue(statisticsItem.getR_month());
						}else {
							cellInfo.setValue("0");
						}
						
						break;
					}
				}			
				
				rowItem.getCellElements().add(cellInfo);

				//연결시도
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getTotalCalls() != null) {
					cellInfo.setValue(statisticsItem.getTotalCalls().toString());
					totalCalls = totalCalls+statisticsItem.getTotalCalls();
				}else {
					cellInfo.setValue("0");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//연결성공
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getSuccess_connect() != null) {
					cellInfo.setValue(statisticsItem.getSuccess_connect().toString());
					success_connect = success_connect + statisticsItem.getSuccess_connect();
				}else {
					cellInfo.setValue("0");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//연결성공(%)
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getSuccess_connect() != null) {
					cellInfo.setValue(IntegerToPer(statisticsItem.getSuccess_connect(), statisticsItem.getTotalCalls()));
				}else {
					cellInfo.setValue("0%");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//강제포기
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getGiveup_call() != null) {
					cellInfo.setValue(statisticsItem.getGiveup_call().toString());
					giveup_call = giveup_call + statisticsItem.getGiveup_call();
				}else {
					cellInfo.setValue("0");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//강제포기(%)
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getGiveup_call() != null) {
					cellInfo.setValue(IntegerToPer(statisticsItem.getGiveup_call(), statisticsItem.getTotalCalls()));
				}else {
					cellInfo.setValue("0%");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//부재중
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getNo_answer() != null) {
					cellInfo.setValue(statisticsItem.getNo_answer().toString());
					no_answer = no_answer + statisticsItem.getNo_answer();
				}else {
					cellInfo.setValue("0");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//부재중(%)
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getNo_answer() != null) {
					cellInfo.setValue(IntegerToPer(statisticsItem.getNo_answer(), statisticsItem.getTotalCalls()));
				}else {
					cellInfo.setValue("0%");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//통화중
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getBusy_call() != null) {
					cellInfo.setValue(statisticsItem.getBusy_call().toString());
					busy_call = busy_call + statisticsItem.getBusy_call();
				}else {
					cellInfo.setValue("0");
				}
				rowItem.getCellElements().add(cellInfo);
				
				//통화중(%)
				cellInfo = new dhtmlXGridRowCell();
				if(statisticsItem.getBusy_call() != null) {
					cellInfo.setValue(IntegerToPer(statisticsItem.getBusy_call(), statisticsItem.getTotalCalls()));
				}else {
					cellInfo.setValue("0%");
				}
				rowItem.getCellElements().add(cellInfo);

				
				if(i+1==statisticsInfoResultTotal) {
					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("allCalls");
					userdataInfo.setValue(String.valueOf(totalCalls));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("success_connect");
					userdataInfo.setValue(String.valueOf(success_connect));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("giveup_call");
					userdataInfo.setValue(String.valueOf(giveup_call));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("no_answer");
					userdataInfo.setValue(String.valueOf(no_answer));
					rowItem.getUserdataElements().add(userdataInfo);

					userdataInfo = new dhtmlXGridRowUserdata();
					userdataInfo.setName("busy_call");
					userdataInfo.setValue(String.valueOf(busy_call));
					rowItem.getUserdataElements().add(userdataInfo);					
				}
				
				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}else{
			xmls = null;
		}
		return xmls;
	}
	
	
	// 모바일 콜 통계(그룹별, 사용자별)
	@RequestMapping(value="/group_user_call_mobile_statistics.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml group_user_call_mobile_statistics(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		String divisionBy = "";
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		if(userInfo != null) {
			if(!StringUtil.isNull(request.getParameter("divisionBy"),true)) {
				divisionBy = request.getParameter("divisionBy");
			}
			xmls = new dhtmlXGridXml();
			if(request.getParameter("init") != null && request.getParameter("init").equals("true")
					&& StringUtil.isNull(request.getParameter("dhx_no_header"))) {
				xmls.setHeadElement(new dhtmlXGridHead());
	
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				String titleBaseName = "statistics.callStatistics.grid.title.";
	
				Integer totalCalls = 0, success_connect = 0, total_times = 0, total_dials = 0, user_count = 0;
				Double avg_times = 0.0;
				dhtmlXGridHeadColumn column = null;
				for(int j = 0; j < 11; j++) {
					column = new dhtmlXGridHeadColumn();
	
					column.setType("ro");
					column.setSort("server");
					column.setAlign("center");
					column.setWidth("130");
					switch(j) {
					//대분류명
					case 0:
						column.setId("r_bg_code");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "bgName", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("200");
						column.setWidth("*");
						break;
					//중분류명
					case 1:
						column.setId("r_mg_code");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "mgName", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("200");
						column.setWidth("*");
						break;
					//소분류명
					case 2:
						column.setId("r_sg_code");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "sgName", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("200");
						column.setWidth("*");
						break;
					//아이디
					case 3:
						column.setId("r_user_id");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "id", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						if(divisionBy.equals("groupBy")) {
							column.setHidden("1");
						}
						break;
					//사용자
					case 4:
						column.setId("r_user_name");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "name", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						if(divisionBy.equals("groupBy")) {
							column.setHidden("1");
						}
						break;
					//시도건수
					case 5:
						column.setId("total_calls");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "totalcalls", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						break;
					//통화건수
					case 6:
						column.setId("success_connect");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "realcalling", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						break;
					//총다이얼 - 기존 천재교과서 존재하던 컬럼이지만 불필요 판단되어 숨김처리
					case 7:
						column.setId("total_dial");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "totalDial", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						column.setHidden("1");
	//					if(divisionBy.equals("groupBy")) {
	//						column.setHidden("1");
	//					}
						break;
					//총통화
					case 8:
						column.setId("total_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "totalTimeCalls", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						break;
					//평균통화
					case 9:
						column.setId("avg_call_time");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "avgTimeCalls", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						break;
					//사용자수
					case 10:
						column.setId("total_user");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "groupUsers", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("150");
						column.setWidth("*");
						if(divisionBy.equals("userBy")) {
							column.setHidden("1");
						}
						break;
					}
					head.getColumnElement().add(column);
					column = null;
				}
	
				xmls.setHeadElement(head);
			}else {
				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P10059");
				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
	
				OrganizationInfo organizationInfo = new OrganizationInfo();
				List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
				List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
				List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);
	
				
				StatisticsInfo statisticsInfo = new StatisticsInfo();
				String sDate = "";
				String eDate = "";
				if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
				else return xmls;
				if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
				else return xmls;
				statisticsInfo.setsDate(sDate.replace("-", ""));
				statisticsInfo.seteDate(eDate.replace("-", ""));
	
				if(!StringUtil.isNull(request.getParameter("init"),true) && StringUtil.isNull(request.getParameter("dhx_no_header")))
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
				
				//페이징 처리
				Integer posStart = 0, count = 20;
				String col, order;
				
				if(!StringUtil.isNull(request.getParameter("posStart"))) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
				}
				
				if(!StringUtil.isNull(request.getParameter("count"))) {
					count = Integer.parseInt(request.getParameter("count"));
				}
				
				if(!StringUtil.isNull(request.getParameter("columnId"))) {
					col = request.getParameter("columnId");
					statisticsInfo.setCol(col);
				}
				
				if(!StringUtil.isNull(request.getParameter("direction"))) {
					order = request.getParameter("direction");
					statisticsInfo.setOrder(order);
				}
				
				statisticsInfo.setPosStart(posStart);
				statisticsInfo.setCount(count);
	
				List<StatisticsInfo> statisticsInfoResult = statisticsInfoService.selectUsersCallsMobileStatistics(statisticsInfo);
				Integer statisticsInfoResultTotal = statisticsInfoResult.size();
	
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				dhtmlXGridRowUserdata userdataInfo = null;
	
				for(int i = 0; i < statisticsInfoResultTotal; i++) {
					StatisticsInfo statisticsItem = statisticsInfoResult.get(i);
	
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
	
					//대분류명
					cellInfo = new dhtmlXGridRowCell();
					if (!StringUtil.isNull(statisticsItem.getBgCode(),true)) {
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), organizationBgInfo) );
					}else {
						cellInfo.setValue("-");
					}
					rowItem.getCellElements().add(cellInfo);
	
					//중분류명
					cellInfo = new dhtmlXGridRowCell();
					if (!StringUtil.isNull(statisticsItem.getMgCode(),true)) {
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), organizationMgInfo) );
					}else {
						cellInfo.setValue("-");
					}
					rowItem.getCellElements().add(cellInfo);
					
					//소분류명
					cellInfo = new dhtmlXGridRowCell();
					if (!StringUtil.isNull(statisticsItem.getSgCode(),true)) {
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), statisticsItem.getSgCode(), organizationSgInfo) );
					}else {
						cellInfo.setValue("-");
					}
					rowItem.getCellElements().add(cellInfo);
					
					//아이디
					cellInfo = new dhtmlXGridRowCell();
					if (!StringUtil.isNull(statisticsItem.getUserId(),true)) {
						cellInfo.setValue(statisticsItem.getUserId());
					}else {
						cellInfo.setValue("-");
					}
					rowItem.getCellElements().add(cellInfo);
					
					//사용자
					cellInfo = new dhtmlXGridRowCell();
					if (!StringUtil.isNull(statisticsItem.getUserName(),true)) {
						cellInfo.setValue(statisticsItem.getUserName());
					}else {
						cellInfo.setValue("-");
					}
					rowItem.getCellElements().add(cellInfo);
					
					//시도건수
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getTotalCalls().toString());
	//				totalCalls = totalCalls + statisticsItem.getTotalCalls();
					rowItem.getCellElements().add(cellInfo);
					
					//통화건수
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getSuccess_connect().toString());
	//				success_connect = success_connect + statisticsItem.getSuccess_connect();
					rowItem.getCellElements().add(cellInfo);
					
					//총다이얼 - 기존 천재교과서 존재하던 컬럼이지만 불필요 판단되어 숨김처리
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
	//				total_dials = total_dials + ;
					rowItem.getCellElements().add(cellInfo);
					
					//총통화
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(Integer.toString(statisticsItem.getTotalTime())));
	//				total_times = total_times + statisticsItem.getTotalTime();
					rowItem.getCellElements().add(cellInfo);
					
					//평균통화
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getAvgCallTime()));
	//				avg_times = avg_times +  (int) Double.parseDouble(statisticsItem.getAvgCallTime());
					rowItem.getCellElements().add(cellInfo);
					
					//사용자수
					cellInfo = new dhtmlXGridRowCell();
					if(!StringUtil.isNull(statisticsItem.getTotal_user())) {
						cellInfo.setValue(statisticsItem.getTotal_user());
	//					user_count = user_count + Integer.parseInt(statisticsItem.getTotal_user());					
					}else {
						cellInfo.setValue("0");
					}
					rowItem.getCellElements().add(cellInfo);
					
	//				if(i+1==statisticsInfoResultTotal) {
	//					userdataInfo = new dhtmlXGridRowUserdata();
	//					userdataInfo.setName("allCalls");
	//					userdataInfo.setValue(String.valueOf(totalCalls));
	//					rowItem.getUserdataElements().add(userdataInfo);
	//
	//					userdataInfo = new dhtmlXGridRowUserdata();
	//					userdataInfo.setName("success_connect");
	//					userdataInfo.setValue(String.valueOf(success_connect));
	//					rowItem.getUserdataElements().add(userdataInfo);
	//
	//					userdataInfo = new dhtmlXGridRowUserdata();
	//					userdataInfo.setName("total_times");
	//					userdataInfo.setValue(secToHHMMSS(String.valueOf(total_times)));
	//					rowItem.getUserdataElements().add(userdataInfo);
	//
	//					userdataInfo = new dhtmlXGridRowUserdata();
	//					userdataInfo.setName("avg_times");
	//					userdataInfo.setValue(secToHHMMSS(String.valueOf(avg_times)));
	//					rowItem.getUserdataElements().add(userdataInfo);
	//
	//					userdataInfo = new dhtmlXGridRowUserdata();
	//					userdataInfo.setName("total_dials");
	//					userdataInfo.setValue(secToHHMMSS(String.valueOf(total_dials)));
	//					rowItem.getUserdataElements().add(userdataInfo);					
	//					
	//					userdataInfo = new dhtmlXGridRowUserdata();
	//					userdataInfo.setName("user_count");
	//					userdataInfo.setValue(String.valueOf(user_count));
	//					rowItem.getUserdataElements().add(userdataInfo);
	//				}
					
					xmls.getRowElements().add(rowItem);
					
					rowItem = null;				
				}
				
				List<StatisticsInfo> totalListResult = statisticsInfoService.totalUsersCallsMobileStatistics(statisticsInfo);
				if(totalListResult.size() > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {					
					if(totalListResult.get(0).getTotal_count() != null) {
						xmls.setTotal_count(Integer.toString(totalListResult.get(0).getTotal_count()));
					}else {
						xmls.setTotal_count("");
					}
					
				} else {
					xmls.setTotal_count("");
				}
				if(request.getParameter("posStart") != null) {
					xmls.setPos(request.getParameter("posStart"));
				} else {
					xmls.setPos("0");
				}
			}
		}else{
			xmls = null;
		}
		return xmls;
	}
	
	// 모바일 콜 통계(상세통계)
	@RequestMapping(value="/detail_call_mobile_statistics.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml detail_call_mobile_statistics(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		String divisionBy = "";
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		if(userInfo != null) {
			if(!StringUtil.isNull(request.getParameter("divisionBy"),true)) {
				divisionBy = request.getParameter("divisionBy");
			}			
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("STATISTICS");
			etcConfigInfo.setConfigKey("USE_USERCOLUMN");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String statisticsUseUserColumn = "Y";
			if(etcConfigResult.size() > 0) {
				statisticsUseUserColumn = etcConfigResult.get(0).getConfigValue();
			}
			
			
			xmls = new dhtmlXGridXml();
			if(request.getParameter("head") != null && request.getParameter("head").equals("true")
					&& StringUtil.isNull(request.getParameter("dhx_no_header"))) {
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				String titleBaseName = "statistics.callStatistics.grid.title.";

//				Integer totalCalls = 0, success_connect = 0, giveup_call = 0, no_answer = 0, busy_call = 0;
				
				dhtmlXGridHeadColumn column = null;
				for(int j = 0; j < 35; j++) {
					column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("server");
					column.setAlign("center");
					column.setWidth("60");
					column.setColumnMinWidth("60");
					switch(j) {
					//No
					case 0:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "no", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("80");
						column.setId("rownumber");
						column.setWidth("80");						
						break;
					//그룹명
					case 1:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "groupName", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("120");
						column.setId("r_sg_code");
						column.setWidth("120");
						break;
					//성명(아이디)
					case 2:
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "nameAndId", null,Locale.getDefault())+"</div>");
						column.setColumnMinWidth("120");
						column.setId("r_user_id");
						column.setWidth("120");
						break;
						//발신 건수
					case 3:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "outbound", null,Locale.getDefault())+"</div>");
						column.setId("outbound");
						break;
					case 4:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
						
					//수신 건수
					case 5:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "inbound", null,Locale.getDefault())+"</div>");
						column.setId("inbound");
						break;
					case 6:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//총통화-시도
					case 7:
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "totalTimeCalls", null,Locale.getDefault())+"</div>");
						column.setId("total_calls");
						break;
					//총통화-연결
					case 8:
						column.setValue("#cspan");
						break;
					//무료전회원-시도
					case 9:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "freeBeforeCust", null,Locale.getDefault())+"</div>");
						column.setId("CL03_try");
						break;
					//무료전회원-연결
					case 10:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//무료회원-시도
					case 11:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "freeCust", null,Locale.getDefault())+"</div>");
						column.setId("CL02_try");
						break;
					//무료회원-연결
					case 12:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//만료회원-시도
					case 13:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "expirationCust", null,Locale.getDefault())+"</div>");
						column.setId("CL07_try");
						break;
					//만료회원-연결
					case 14:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//정회원-시도
					case 15:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "RegularCust", null,Locale.getDefault())+"</div>");
						column.setId("CL01_try");
						break;
					//정회원-연결
					case 16:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//휴강회원-시도
					case 17:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "CancelMember", null,Locale.getDefault())+"</div>");
						column.setId("CL04_try");
						break;
					//휴강회원-연결
					case 18:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//휴회회원-시도
					case 19:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "AdjournedMember", null,Locale.getDefault())+"</div>");
						column.setId("CL05_try");
						break;
					//휴회회원-연결
					case 20:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//휴회예정-시도
					case 21:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "AdjournedExpectedMember", null,Locale.getDefault())+"</div>");
						column.setId("CL06_try");
						break;
					//휴회예정-연결
					case 22:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//기타회원-시도
					case 23:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "OtherMembers", null,Locale.getDefault())+"</div>");
						column.setId("CL08_try");
						break;
					//기타회원-연결
					case 24:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						break;
					//발신 총 통화시간
					case 25:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">발신 "+messageSource.getMessage(titleBaseName + "totalCallsTime", null,Locale.getDefault())+"</div>");
						column.setId("OUTBOUND_TOTAL_TIME");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//수신 총 통화시간
					case 26:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">수신 "+messageSource.getMessage(titleBaseName + "totalCallsTime", null,Locale.getDefault())+"</div>");
						column.setId("INBOUND_TOTAL_TIME");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//총통화시간
					case 27:
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "totalCallsTime", null,Locale.getDefault())+"</div>");
						column.setId("TOTAL_TIME");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//발신 평균통화
					case 28:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">발신 "+messageSource.getMessage(titleBaseName + "avgTimeCalls", null,Locale.getDefault())+"</div>");
						column.setId("outbound_avg_call_time");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//수신 평균통화
					case 29:
						if("Y".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">수신 "+messageSource.getMessage(titleBaseName + "avgTimeCalls", null,Locale.getDefault())+"</div>");
						column.setId("inbound_avg_call_time");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//총 평균통화
					case 30:
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">총 "+messageSource.getMessage(titleBaseName + "avgTimeCalls", null,Locale.getDefault())+"</div>");
						column.setId("avg_call_time");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//정회원+휴강회원-연결합계
					case 31:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "RegularCust", null,Locale.getDefault())+"+"+messageSource.getMessage(titleBaseName + "CancelMember", null,Locale.getDefault())+"</div>");
						column.setId("CL01CL04_sum_conn");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//정회원+휴강회원-시간합산
					case 32:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//통화시간-정회원
					case 33:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
//						column.setSort("int");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "callingTime", null,Locale.getDefault())+"</div>");
						column.setId("CL01_call_time");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					//통화시간-휴강회원
					case 34:
						if("N".equals(statisticsUseUserColumn)) 
							column.setHidden("1");
						column.setValue("#cspan");
						column.setColumnMinWidth("100");
						column.setWidth("100");
						break;
					}
					head.getColumnElement().add(column);
					column = null;
				}
				head.setAfterElement(new dhtmlXGridHeadAfterInit());
				dhtmlXGridHeadAfterInit afterInit = new dhtmlXGridHeadAfterInit();

				afterInit.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

				dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
				headCall.setParamElement(new ArrayList<String>());
				headCall.setCommand("attachHeader");
				headCall.getParamElement().add("#rspan,#rspan,#rspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.TryAndConn", null,Locale.getDefault())+","/*"(시도 | 연결),"*/
						+ "#cspan,"
						+ "#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.sumConnAndsumTime", null,Locale.getDefault())+","/*"(연결합계 | 시간합산),"*/
						+ "#cspan,"
						+ messageSource.getMessage("statistics.callStatistics.grid.title.regularMemAndCancelMem", null,Locale.getDefault())+","/*"(정회원 | 휴강회원),"*/
						+ "#cspan,"
						);
						
				
				
				
				afterInit.getCallElement().add(headCall);

				head.setAfterElement(afterInit);
				
				xmls.setHeadElement(head);
			}else {
				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P10059");
				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

				OrganizationInfo organizationInfo = new OrganizationInfo();
//				List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
//				List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
				List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);

				
				StatisticsInfo statisticsInfo = new StatisticsInfo();
				String sDate = "";
				String eDate = "";

				if(request.getParameter("sDate") != null) sDate = request.getParameter("sDate");
				else return xmls;
				if(request.getParameter("sDate") != null) eDate = request.getParameter("eDate");
				else return xmls;
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
				
				//페이징 처리
				Integer posStart = 0, count = 20;
				String col, order;
				
				if(!StringUtil.isNull(request.getParameter("posStart"))) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
				}
				
				if(!StringUtil.isNull(request.getParameter("count"))) {
					count = Integer.parseInt(request.getParameter("count"));
				}
				
				if(!StringUtil.isNull(request.getParameter("columnId"))) {
					col = request.getParameter("columnId");
					statisticsInfo.setCol(col);
				}
				
				if(!StringUtil.isNull(request.getParameter("direction"))) {
					order = request.getParameter("direction");
					statisticsInfo.setOrder(order);
				}
				
				statisticsInfo.setPosStart(posStart);
				statisticsInfo.setCount(count);
				List<StatisticsInfo> statisticsInfoResult = statisticsInfoService.selectDetailsCallsMobileStatistics(statisticsInfo);
				Integer statisticsInfoResultTotal = statisticsInfoResult.size();

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				dhtmlXGridRowUserdata userdataInfo = null;

				for(int i = 0; i < statisticsInfoResultTotal; i++) {
					StatisticsInfo statisticsItem = statisticsInfoResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					
					//No
					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(String.valueOf(i+1));
					cellInfo.setValue(statisticsItem.getRownumber());
					rowItem.getCellElements().add(cellInfo);
					
					//그룹명
					cellInfo = new dhtmlXGridRowCell();
					if (!StringUtil.isNull(statisticsItem.getSgCode(),true)) {
						cellInfo.setValue( new FindOrganizationUtil().getOrganizationName(statisticsItem.getBgCode(), statisticsItem.getMgCode(), statisticsItem.getSgCode(), organizationSgInfo) );
					}else {
						cellInfo.setValue("-");
					}
					rowItem.getCellElements().add(cellInfo);
					
					//성명(아이디)
					cellInfo = new dhtmlXGridRowCell();
					String UserName = "";
					String UserId   = "";
					if (!StringUtil.isNull(statisticsItem.getUserId(),true)) {
						UserId = statisticsItem.getUserId();
					}				
					if (!StringUtil.isNull(statisticsItem.getUserName(),true)) {
						UserName = statisticsItem.getUserName();
					}
					cellInfo.setValue(UserName+" ("+UserId+")");
					rowItem.getCellElements().add(cellInfo);
					
					//발신 건수-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getOUTBOUND_CALLS_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);

					//발신 건수-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getOUTBOUND_CALLS_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//수신 건수-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getINBOUND_CALLS_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);

					//수신 건수-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getINBOUND_CALLS_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//총통화-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getTotalCalls().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//총통화-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getSuccess_connect().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//무료전회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL03_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//무료전회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL03_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//무료회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL02_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//무료회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL02_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//만료회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL07_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//만료회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL07_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//정회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL01_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//정회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL01_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//휴강회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL04_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//휴강회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL04_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//휴회회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL05_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//휴회회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL05_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//휴회예정-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL06_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//휴회예정-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL06_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//기타회원-시도
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL08_try().toString());
					cellInfo.setStyle("color:#f90000;");
					rowItem.getCellElements().add(cellInfo);
					
					//기타회원-연결
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(statisticsItem.getCL08_conn().toString());
					cellInfo.setStyle("color:#003cf2;");
					rowItem.getCellElements().add(cellInfo);
					
					//발신 총 통화시간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(Integer.toString(statisticsItem.getOutboundTime())));
					rowItem.getCellElements().add(cellInfo);
					
					//수신 총 통화시간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(Integer.toString(statisticsItem.getInboundTime())));
					rowItem.getCellElements().add(cellInfo);
					
					//총통화시간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(Integer.toString(statisticsItem.getTotalTime())));
					rowItem.getCellElements().add(cellInfo);
					
					//발신 평균통화
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getOUTBOUND_AVG_CALL_TIME()));
					rowItem.getCellElements().add(cellInfo);
					
					//수신 평균통화
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getINBOUND_AVG_CALL_TIME()));
					rowItem.getCellElements().add(cellInfo);
					
					//총 평균통화
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getAvgCallTime()));
					rowItem.getCellElements().add(cellInfo);
					
					//정회원+휴강회원-연결합계
					cellInfo = new dhtmlXGridRowCell();
//					Integer sum_cl01cl04 = statisticsItem.getCL01_conn()+statisticsItem.getCL04_conn();
					cellInfo.setValue(statisticsItem.getCL01CL04_sum_conn().toString());
					rowItem.getCellElements().add(cellInfo);
					
					//정회원+휴강회원-시간합산
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getCL01CL04_call_time().toString()));
					rowItem.getCellElements().add(cellInfo);
					
					//통화시간-정회원
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getCL01_call_time().toString()));
					rowItem.getCellElements().add(cellInfo);
					
					//통화시간-휴강회원
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(secToHHMMSS(statisticsItem.getCL04_call_time().toString()));
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}

				Integer totalListResult = statisticsInfoService.totalDetailsCallsMobileStatistics(statisticsInfo);
				if(totalListResult > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {
					xmls.setTotal_count(totalListResult.toString());
				} else {
					xmls.setTotal_count("");
				}
				if(request.getParameter("posStart") != null) {
					xmls.setPos(request.getParameter("posStart"));
				} else {
					xmls.setPos("0");
				}
			}
		}else{
			xmls = null;
		}
		return xmls;
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
	
	String IntegerToPer(Integer cmp, Integer std) {
		String res = "0.0%";
		if(std>0) {
			double cal = (double)cmp/(double)std * 100.0;		
			res = String.format("%.2f", cal)+"%";
		}
		
		return res;
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