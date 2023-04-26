
package com.furence.recsee.admin.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.DelRecfileInfo;
import com.furence.recsee.admin.model.FileRecoverInfo;
import com.furence.recsee.admin.model.QueueInfo;
import com.furence.recsee.admin.service.DelRecfileInfoService;
import com.furence.recsee.admin.service.FileRecoverService;
import com.furence.recsee.admin.service.QueueInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.Log;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.PacketVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.sqlFilterUtil;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.initech.core.util.URLDecoder;
import com.initech.shttp.server.Logger;


@Controller
public class XmlSystemManageController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XmlSystemManageController.class);
	@Autowired
	private DelRecfileInfoService delRecfileInfoService;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private LogService logService;

	@Autowired
	private QueueInfoService queueInfoService;

	@Autowired
	private SubNumberInfoService subNumberInfoService;

	@Autowired
	private FileRecoverService fileRecoverService;
	
	@Autowired
	private MessageSource messageSource;

	// 로그  목록 그리드
	@RequestMapping(value="/log_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml log_list(HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {

			boolean nowAcc = nowAccessChk(request,"systemManage.log");

			if(!nowAcc) {
				return xmls;
			}

			xmls = new dhtmlXGridXml();

			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
				xmls.setHeadElement(new dhtmlXGridHead());
				
				// 자번호 닉네임 표시 옵션 사용 여부
				EtcConfigInfo company_telno = new EtcConfigInfo();
				company_telno.setGroupKey("SEARCH");
				company_telno.setConfigKey("company_telno");
				EtcConfigInfo useExtNickYN = null;
				String extNickYN = "N";
				try {
					useExtNickYN = etcConfigInfoService.selectOptionYN(company_telno);
					
					if (useExtNickYN != null) {
						extNickYN = useExtNickYN.getConfigValue();
					}
				} catch(Exception e) {
					company_telno.setConfigValue("N");
					int insertEtcConfigInfoResult = etcConfigInfoService.insertEtcConfigInfo(company_telno);
					
					logger.error("error",e);
				}
					 
				

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

				for( int j = 0; j < 9; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setWidth("150");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");

					switch(j) {

					case 0:
						column.setWidth("30");
						column.setType("ch");
						column.setSort("na");
						column.setHidden("1");
						column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
						break;
					case 1:
						column.setId("R_LOG_DATE");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.grid.date", null,Locale.getDefault()));
						break;
					case 2:
						column.setId("R_LOG_TIME");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.grid.time", null,Locale.getDefault()));
						break;
					case 3:
						column.setId("R_LOG_IP");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.grid.logIp", null,Locale.getDefault()));
						if("Y".equals(extNickYN)) {
							column.setHidden("1");
						}
						break;
					case 4:
						column.setId("R_LOG_SERVER_IP");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.grid.serverIp", null,Locale.getDefault()));
						if("Y".equals(extNickYN)) {
							column.setHidden("1");
						}
						break;
					case 5:
						column.setId("R_LOG_USER_ID");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.grid.userId", null,Locale.getDefault()));
						break;
					case 6:
						column.setId("R_LOG_NAME");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.grid.logType", null,Locale.getDefault()));
						break;
					case 7:
						column.setId("R_LOG_CONTENTS");
						column.setWidth("200");
						column.setValue(messageSource.getMessage("admin.grid.logContent", null,Locale.getDefault()));
						break;
					case 8:
						column.setId("R_LOG_ETC");
						column.setWidth("400");
						column.setAlign("left");
						column.setValue(messageSource.getMessage("admin.grid.contents", null,Locale.getDefault()));
						break;
					}

					head.getColumnElement().add(column);
					column = null;
				}
				xmls.setHeadElement(head);
			}else{

				Log log = new Log();

				if(!StringUtil.isNull(request.getParameter("sDate"),true) && !StringUtil.isNull(request.getParameter("eDate"),true)) {
					log.setsDate(sqlFilterUtil.sqlFilter(request.getParameter("sDate").replace("-", "").toString()));
					log.seteDate(sqlFilterUtil.sqlFilter(request.getParameter("eDate").replace("-", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("sTime"),true) && !StringUtil.isNull(request.getParameter("sTime"),true)) {
					log.setsTime(sqlFilterUtil.sqlFilter(request.getParameter("sTime").replace(":", "").toString()));
					log.seteTime(sqlFilterUtil.sqlFilter(request.getParameter("eTime").replace(":", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("rLogIp"),true)) {
					log.setrLogIp(sqlFilterUtil.sqlFilter(request.getParameter("rLogIp")));
				}
				if(!StringUtil.isNull(request.getParameter("rLogServerIp"),true)) {
					log.setrLogServerIp(sqlFilterUtil.sqlFilter(request.getParameter("rLogServerIp")));
				}
				if(!StringUtil.isNull(request.getParameter("rLogUserId"),true)) {
					log.setrLogUserId(sqlFilterUtil.sqlFilter(request.getParameter("rLogUserId")));
				}
				if(!StringUtil.isNull(request.getParameter("rLogContents"),true)) {
					log.setrLogDetailCode(sqlFilterUtil.sqlFilter(request.getParameter("rLogContents")));
				}
				if(!StringUtil.isNull(request.getParameter("rLogEtc"),true)) {
					try {
						log.setrLogEtc(sqlFilterUtil.sqlFilter(URLDecoder.decode(request.getParameter("rLogEtc"), "UTF-8")));
					} catch (UnsupportedEncodingException e) {
						Logger.error("", "", "", e.toString());
					}
				}

				if(!StringUtil.isNull(request.getParameter("rLogCode"),true)) {
					log.setrLogCode(sqlFilterUtil.sqlFilter(request.getParameter("rLogCode")));
				}
				if(!StringUtil.isNull(request.getParameter("rLogDetailCode"),true)) {
					log.setrLogDetailCode(sqlFilterUtil.sqlFilter(request.getParameter("rLogDetailCode")));
				}

				if(!StringUtil.isNull(request.getParameter("orderBy"),true) && !StringUtil.isNull(request.getParameter("direction"),true)) {
					log.setOrderBy(sqlFilterUtil.sqlFilter(request.getParameter("orderBy")));
					log.setDirection(sqlFilterUtil.sqlFilter(request.getParameter("direction")));
				}

				if(!StringUtil.isNull(request.getParameter("limitUse"),true)) {
					log.setLimitUse(request.getParameter("limitUse").toUpperCase());
				} else {
					log.setLimitUse("N");
				}
				
				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					log.setPosStart(posStart);
				}
				
				Integer count = 0;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("posStart"));
					log.setCount(count);
				}
				
				
				List<Log> logResult = logService.selectLog(log);
				Integer logResultTotal = logResult.size();

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for(int i = 0; i < logResultTotal; i++) {
					Log logItem = logResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(posStart+i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 체크 박스
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(null);
					rowItem.getCellElements().add(cellInfo);

					// 로그 날짜
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(logItem.getrLogDate());
					rowItem.getCellElements().add(cellInfo);

					// 로그 시간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(logItem.getrLogTime());
					rowItem.getCellElements().add(cellInfo);

					// 로그 아이피
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(logItem.getrLogIp());
					rowItem.getCellElements().add(cellInfo);

					// 서버 아이피
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(logItem.getrLogServerIp());
					rowItem.getCellElements().add(cellInfo);

					// 사용자 아이디
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(logItem.getrLogUserId());
					rowItem.getCellElements().add(cellInfo);

					// 로그 분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.log.name."+logItem.getrLogName(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);

					// 로그 내용
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.log.contents."+logItem.getrLogContents(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);

					// 기타
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(logItem.getrLogEtc());
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
				Integer totalLogResult = logService.totalLog(log);
				logService.writeLog(request, "LOG", "SEARCH", log.toLogString(messageSource));
				if(totalLogResult > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {
					xmls.setTotal_count(totalLogResult.toString());
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


	// 큐 관리
	@RequestMapping(value="/queue_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml queue_list(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
				
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.queue.form.title.";
			for(int i=0; i<4; i++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setFiltering("0");
				column.setEditable("false");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("190");
				column.setValue("#cspan");
				column.setSort("na");

				switch(i) {
				case 0:
					column.setType("ch");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					column.setWidth("30");
					break;
				case 1:
					column.setType("ro");
					column.setWidth("120");
					column.setId("queNum");
					column.setValue(messageSource.getMessage("admin.label.queNum", null,Locale.getDefault()));
					break;
				case 2:
					column.setType("ed");
					column.setWidth("230");
					column.setId("queName");
					column.setValue(messageSource.getMessage("admin.label.queName", null,Locale.getDefault()));
					break;
				case 3:
					column.setWidth("*");
					column.setValue("");
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
			headCall.getParamElement().add("#rspan,#text_filter,#text_filter");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			QueueInfo queueInfo = new QueueInfo();
			List<QueueInfo> queueInfoResult = queueInfoService.selectQueueInfo(queueInfo);
			Integer queueInfoResultTotal = queueInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < queueInfoResultTotal; i++) {
				QueueInfo queueItem = queueInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(queueItem.getrQueueNum() != null && !queueItem.getrQueueNum().isEmpty()) {
					cellInfo.setValue(queueItem.getrQueueNum());
				} else {
					cellInfo.setValue("");
				}
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(queueItem.getrQueueName() != null && !queueItem.getrQueueName().isEmpty()) {
					cellInfo.setValue(queueItem.getrQueueName());
				} else {
					cellInfo.setValue("");
				}
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}else{
			xmls=null;
		}
		return xmls;
	}

	// 녹취 삭제 관리 목록
	@RequestMapping(value = "/delMenuInfo_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml selectDelMenuInfoList(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {

			String recsee_mobile = "N";
			
			if(request.getSession().getAttribute("recsee_mobile")!=null) {
				recsee_mobile = (String)request.getSession().getAttribute("recsee_mobile");	
			}
			
			
			String mody="";
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "userManage.userManageRec");
			if(!StringUtil.isNull(nowAccessInfo.getModiYn(),true))
				mody=nowAccessInfo.getModiYn();
		
			String loginIpChk="N";
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LOGIN");
			etcConfigInfo.setConfigKey("LOGIN_IP_CHK");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			if(etcConfigResult.size()>0&&"Y".equals(etcConfigResult.get(0).getConfigValue())) {
				loginIpChk="Y";
			}
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			// if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
			if(userInfo != null) {
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				for(int j = 0; j < 36; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
	
					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setFiltering("1");
					column.setEditable("0");
					column.setCache("1");
					column.setHidden("0");
	
					switch (j) {
	
					// 체크
					case 0:
						column.setWidth("50");
						column.setType("ch");
						column.setAlign("center");
						column.setSort("na");
						column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img id='r_check_box' src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
						break;
						// 스케줄 이름
					case 1:
	//					column.setWidth("100");
						column.setWidth("180");
						column.setId("delScheduleName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
						column.setValue("<div style=\"text-align:center;\">삭제 스케줄 이름</div>");
						break;
						// 삭제 유형
					case 2:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delType", null,Locale.getDefault())+"</div>");
						break;
						// 삭제 유형 실제 DB 값
					case 3:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delTypeValue");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delType", null,Locale.getDefault())+" DB data </div>");
						break;
						
						// 삭제 파일 유형
					case 4:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delFileType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delFileType", null,Locale.getDefault())+"</div>");
						break;
						
						// 삭제  파일 유형 실제 DB 값
					case 5:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delFileTypeValue");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delFileType", null,Locale.getDefault())+" DB data </div>");
						break;
						
						// 삭제 파일 위치
					case 6:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delFilePathType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delFilePathType", null,Locale.getDefault())+"</div>");
						break;
						
					// 삭제  파일 위치 실제 DB 값
					case 7:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delFilePathTypeValue");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delFilePathType", null,Locale.getDefault())+" DB data </div>");
						break;
						
						// 삭제 기준
					case 8:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delSearchType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delSearchType", null,Locale.getDefault())+"</div>");
						break;
					// 삭제 기준 실제 DB 값
					case 9:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delSearchTypeValue");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delSearchType", null,Locale.getDefault())+" DB data </div>");
						break;
						
						// 삭제 경로
					case 10:
						column.setWidth("250");
						column.setId("delPath");
						column.setValue("<div style=\"text-align:center;\">삭제 경로</div>");
						break;
						
						// 기간
					case 11:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delPeriod");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delPeriod", null,Locale.getDefault())+"</div>");
						break;		
						
					case 12:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delPeriodOffset");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.delPeriodOffset", null,Locale.getDefault())+"</div>");
						break;
						
						// 대분류
					case 13:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("bgName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 중분류
					case 14:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("mgName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 소분류
					case 15:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("sgName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
					case 16:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupSchedul");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupSchedule", null,Locale.getDefault())+"</div>");
						break;	
					case 17:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupSchedulDb");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupSchedule", null,Locale.getDefault())+"</div>");
						break;	
						// 기간
					case 18:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("week");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.week", null,Locale.getDefault())+"</div>");
						break;
					case 19:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("weekDb");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.week", null,Locale.getDefault())+"</div>");
						break;
						// 백업 일
					case 20:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("day");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.day", null,Locale.getDefault())+"</div>");
						break;		
					case 21:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("time");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.time", null,Locale.getDefault())+"</div>");
						break;	
					case 22:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("min");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.min", null,Locale.getDefault())+"</div>");
						break;
						// 년 
					case 23:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delYear");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delYear", null,Locale.getDefault())+"</div>");
						break;							
						// 개월
					case 24:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delMonth");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delMonth", null,Locale.getDefault())+"</div>");
						break;
						// 개월
					case 25:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delDay");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delDay", null,Locale.getDefault())+"</div>");
						break;
					case 26:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delYearOffset");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delYear", null,Locale.getDefault())+"</div>");
						break;	
						// 개월
					case 27:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delMonthOffset");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delMonth", null,Locale.getDefault())+"</div>");
						break;
						// 개월
					case 28:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delDayOffset");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delMonth", null,Locale.getDefault())+"</div>");
						break;
						// 삭제 방식 선택
					case 29:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("delSelect");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delSelect", null,Locale.getDefault())+"</div>");
						break;
					// 삭제  방식 선택 실제 DB 값
					case 30:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delSelectValue");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.select.title.delSelect", null,Locale.getDefault())+" DB data </div>");
						break;
					// 로그 유형
					case 31:
	//					column.setWidth("100");
						column.setWidth("130");
						column.setId("logType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.logType", null,Locale.getDefault())+"</div>");
						break;
					// 로그 유형 실제 DB 값
					case 32:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("logTypeValue");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.logType", null,Locale.getDefault())+" DB data</div>");
						break;
						// 스토리지 전송 체크
					case 33:
	//					column.setWidth("100");
						column.setWidth("200");
						column.setId("storageSendChk");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">스토리지 전송 체크</div>");
						break;
					case 34:
	//					column.setWidth("100");
						column.setWidth("200");
						column.setHidden("1");
						column.setId("storageSendChkValue");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">스토리지 전송 체크</div>");
						break;
					case 35:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("delSeq");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\"></div>");
						break;
					}
					head.getColumnElement().add(column);
					column = null;
	
				}
				xmls.setHeadElement(head);
				
				
				List<DelRecfileInfo> delRecfileList = null;
				int delRecfileListTotal = 0;
				
				DelRecfileInfo delRecfileInfoChk = new DelRecfileInfo();
				delRecfileList = delRecfileInfoService.selectDelRecfileInfo(delRecfileInfoChk);
				
	        	delRecfileListTotal = delRecfileList.size();
		        
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
	
				for(int i = 0; i < delRecfileListTotal; i++) {
					DelRecfileInfo deleteRecfileItem = delRecfileList.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					if ("".equals(deleteRecfileItem.getR_seq()) || StringUtil.isNull(deleteRecfileItem.getR_seq(),true)) {
						rowItem.setId(deleteRecfileItem.getR_seq());
					}
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
	
					// 삭제 스케줄 이름
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_schedule_name());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 유형
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.delType." + deleteRecfileItem.getR_del_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 유형 db data hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 파일 유형
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.delFileType." + deleteRecfileItem.getR_del_file_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 파일 유형 db data hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_file_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 파일 위치
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.delFilePathType." + deleteRecfileItem.getR_del_file_path_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 파일 위치 db data hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_file_path_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 기준
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.delSearchType." + deleteRecfileItem.getR_del_search_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 기준 db data hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_search_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 경로
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_path());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 기간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_year() 
							+ messageSource.getMessage("admin.select.title.delYear", null,Locale.getDefault()) 
							+ deleteRecfileItem.getR_del_month() 
							+ messageSource.getMessage("admin.select.title.delMonth", null,Locale.getDefault())
							+ deleteRecfileItem.getR_del_day() 
							+ messageSource.getMessage("admin.select.title.delDay", null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 기간 제한
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_year_offset() 
							+ messageSource.getMessage("admin.select.title.delYear", null,Locale.getDefault()) 
							+ deleteRecfileItem.getR_del_month_offset() 
							+ messageSource.getMessage("admin.select.title.delMonth", null,Locale.getDefault())
							+ deleteRecfileItem.getR_del_day_offset() 
							+ messageSource.getMessage("admin.select.title.delDay", null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
	
					
					// 대분류
					cellInfo = new dhtmlXGridRowCell();

					if(!StringUtil.isNull(deleteRecfileItem.getR_bg_code()) && "".equals(deleteRecfileItem.getR_bg_code())) {
						cellInfo.setValue("*");
					} else {
						cellInfo.setValue(deleteRecfileItem.getR_bg_name());
					}
					rowItem.getCellElements().add(cellInfo);
	
					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if (deleteRecfileItem.getR_bg_code() != null && "*".equals(deleteRecfileItem.getR_bg_code())) {
						cellInfo.setValue("*");
					} else {
						cellInfo.setValue(deleteRecfileItem.getR_mg_name());
					}
					rowItem.getCellElements().add(cellInfo);
	
					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if (deleteRecfileItem.getR_bg_code() != null && "*".equals(deleteRecfileItem.getR_bg_code())) {
						cellInfo.setValue("*");
					} else {
						cellInfo.setValue(deleteRecfileItem.getR_sg_name());
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 스케줄러						
					cellInfo = new dhtmlXGridRowCell();
					if (deleteRecfileItem.getR_scheduler_select() != null) {
						cellInfo.setValue(messageSource.getMessage("admin.detail.view." + deleteRecfileItem.getR_scheduler_select(), null,Locale.getDefault()));
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 스케줄러 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_select());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 주
					cellInfo = new dhtmlXGridRowCell();
					if(!"*".equals(deleteRecfileItem.getR_scheduler_week())) {							
					cellInfo.setValue(messageSource.getMessage("admin.detail.view.week." + deleteRecfileItem.getR_scheduler_week(), null,Locale.getDefault()));
					}else {
						cellInfo.setValue(deleteRecfileItem.getR_scheduler_week());
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 주 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_week());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_day());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 시간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_hour());
					rowItem.getCellElements().add(cellInfo);

					// 백업 분
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_min());
					rowItem.getCellElements().add(cellInfo);
					
					// 년 hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_year());
					rowItem.getCellElements().add(cellInfo);
					
					// 개월 hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_month());
					rowItem.getCellElements().add(cellInfo);

					// 일 hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_day());
					rowItem.getCellElements().add(cellInfo);
					
					// 년 hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_year_offset());
					rowItem.getCellElements().add(cellInfo);
					
					// 개월 hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_month_offset());
					rowItem.getCellElements().add(cellInfo);
					
					// 일 hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_day_offset());
					rowItem.getCellElements().add(cellInfo);
					
					// 삭제 방식 선택
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.delSelect." + deleteRecfileItem.getR_del_select(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					//삭제 방식 선택 db data hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_del_select());
					rowItem.getCellElements().add(cellInfo);					
					
					// 로그 유형
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.logType." + deleteRecfileItem.getR_log_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 로그 유형 db data hidden
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_log_type());
					rowItem.getCellElements().add(cellInfo);

					// 스토리지 전송 체크
					cellInfo = new dhtmlXGridRowCell();
					String val = deleteRecfileItem.getR_storage_send_chk_yn();
					if ("Y".equals(val)) {
						val = messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault());
					} else {
						val = messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault());
					}
					cellInfo.setValue(val);
					rowItem.getCellElements().add(cellInfo);
					
					// 스토리지 전송 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_storage_send_chk_yn());
					rowItem.getCellElements().add(cellInfo);
					
					// 시퀀스
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_seq());
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
	
					rowItem = null;
				}
			}
		}
		return xmls;
	}
		
	// 녹취 삭제 관리 목록 asd
	@RequestMapping(value = "/bakMenuInfo_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml selectBakMenuInfoList(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {							
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			// if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
			if(userInfo != null) {
				
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("SEARCH");
				etcConfigInfo.setConfigKey("company_telno");
				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				for(int j = 0; j < 35; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
	
					column.setType("ro");
					column.setSort("str");
					column.setAlign("center");
					column.setFiltering("1");
					column.setEditable("0");
					column.setCache("1");
					column.setHidden("0");
	
					switch (j) {
	
					// 체크
					case 0:
						column.setWidth("50");
						column.setType("ch");
						column.setAlign("center");
						column.setSort("na");
						
						column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img id='r_check_box' src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
						break;
	
						// 대분류
					case 1:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("bgName");
						if(etcConfigResult.size() > 0 && etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 중분류
					case 2:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("mgName");
						if(etcConfigResult.size() > 0 && etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 소분류
					case 3:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("sgName");
						if(etcConfigResult.size() > 0 && etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 기간
					case 4:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupSchedul");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupSchedule", null,Locale.getDefault())+"</div>");
						break;	
					case 5:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupSchedulDb");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupSchedule", null,Locale.getDefault())+"</div>");
						break;	
						// 기간
					case 6:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("week");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.week", null,Locale.getDefault())+"</div>");
						break;
					case 7:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("weekDb");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.week", null,Locale.getDefault())+"</div>");
						break;
						// 백업 일
					case 8:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("day");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.day", null,Locale.getDefault())+"</div>");
						break;		
					case 9:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("time");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.time", null,Locale.getDefault())+"</div>");
						break;	
						// 삭제 유형
					case 10:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.backType", null,Locale.getDefault())+"</div>");
						break;		
					case 11:
	//					column.setWidth("100");
						column.setWidth("110");
						column.setId("backTypeDb");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.backType", null,Locale.getDefault())+"</div>");
						break;	
						// 로그 유형
					case 12:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("logType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.logType", null,Locale.getDefault())+"</div>");
						break;							
						// 로그 유형
					case 13:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("logTypeDB");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.logType", null,Locale.getDefault())+"</div>");
						break;	
					case 14:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("decType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.decType", null,Locale.getDefault())+"</div>");
						break;		
					case 15:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("decTypeDB");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.decType", null,Locale.getDefault())+"</div>");
						break;		
					case 16:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("urlUpdateType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.urlUpdateType", null,Locale.getDefault())+"</div>");
						break;								
					case 17:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("urlUpdateTypeDB");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.urlUpdateType", null,Locale.getDefault())+"</div>");
						break;								
					case 18:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("overWriteType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.overWriteType", null,Locale.getDefault())+"</div>");
						break;								
					case 19:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("overWriteTypeDB");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.overWriteType", null,Locale.getDefault())+"</div>");
						break;	
					case 20:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("conformityType");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.conformityType", null,Locale.getDefault())+"</div>");
						break;								
					case 21:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("conformityTypeDB");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.conformityType", null,Locale.getDefault())+"</div>");
						break;		
					case 22:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("dualBackupType");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.dualBackupType", null,Locale.getDefault())+"</div>");
						break;									
					case 23:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("dualBackupTypeDB");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.dualBackupType", null,Locale.getDefault())+"</div>");
						break;							
					case 24:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupSelect");
						//column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupSelect", null,Locale.getDefault())+"</div>");
						break;
					case 25:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupSelectDb");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupSelect", null,Locale.getDefault())+"</div>");
						break;
					case 26:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupData");
						//column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupData", null,Locale.getDefault())+"</div>");
						break;
					case 27:
	//					column.setWidth("100");
						column.setWidth("140");
						column.setId("backupPath");
						//column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.backPath", null,Locale.getDefault())+"</div>");
						break;								
					case 28:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("seq");
						column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\"></div>");
						break;
					case 29:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("nowStart");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.backNowStart", null,Locale.getDefault())+"</div>");
						break;
					case 30:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("nowDown");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.title.backDown", null,Locale.getDefault())+"</div>");
						break;
					case 31:
	//					column.setWidth("100");
						column.setWidth("160");
						column.setId("backupState");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupState", null,Locale.getDefault())+"</div>");
						break;
					
					case 32:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupStartTime");
						//column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupStartTime", null,Locale.getDefault())+"</div>");
						break;
					case 33:
	//					column.setWidth("100");
						column.setWidth("100");
						column.setId("backupRunTime");
						//column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupRunTime", null,Locale.getDefault())+"</div>");
						break;
					case 34:
	//					column.setWidth("100");
						column.setWidth("100"); 
						column.setId("backupEndTime");
						//column.setHidden("1");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.backup.label.backupEndTime", null,Locale.getDefault())+"</div>");
						break;
					}
					head.getColumnElement().add(column);
					column = null;
	
				}
				xmls.setHeadElement(head);
				
				
				List<DelRecfileInfo> delRecfileList = null;
				int delRecfileListTotal = 0;
				
				delRecfileList = delRecfileInfoService.selectBackRecfileInfo();
				
	        	delRecfileListTotal = delRecfileList.size();
		        
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
	
				for(int i = 0; i < delRecfileListTotal; i++) {
					DelRecfileInfo deleteRecfileItem = delRecfileList.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					if ("".equals(deleteRecfileItem.getR_seq()) || StringUtil.isNull(deleteRecfileItem.getR_seq(),true)) {
						rowItem.setId(deleteRecfileItem.getR_seq());
					}
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
	
					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_bg_name());
					rowItem.getCellElements().add(cellInfo);
	
					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_mg_name());
					rowItem.getCellElements().add(cellInfo);
	
					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_sg_name());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 스케줄러						
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.detail.view." + deleteRecfileItem.getR_scheduler_select(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 스케줄러 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_select());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 주
					cellInfo = new dhtmlXGridRowCell();
					if(!"*".equals(deleteRecfileItem.getR_scheduler_week())) {							
					cellInfo.setValue(messageSource.getMessage("admin.detail.view.week." + deleteRecfileItem.getR_scheduler_week(), null,Locale.getDefault()));
					}else {
						cellInfo.setValue(deleteRecfileItem.getR_scheduler_week());
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 주 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_week());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_day());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 시간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_scheduler_hour());
					rowItem.getCellElements().add(cellInfo);
	
					// 백업 유형
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.delType." + deleteRecfileItem.getR_back_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 유형 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_back_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 로그 유형
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.select.value.logType." + deleteRecfileItem.getR_log_type(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);	
					
					// 로그 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_log_type());
					rowItem.getCellElements().add(cellInfo);	
					
					// 복호하 유형
					cellInfo = new dhtmlXGridRowCell();
					
					String tempValue = deleteRecfileItem.getR_decode_type();
					
					if("Y".equals(tempValue)) {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault()));
					}else {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault()));
					}						
					rowItem.getCellElements().add(cellInfo);	
					
					// 복호하 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_decode_type());
					rowItem.getCellElements().add(cellInfo);	
					
					// 파일 경로 수정 유형
					cellInfo = new dhtmlXGridRowCell();
					tempValue = deleteRecfileItem.getR_url_update_type();						
					if("Y".equals(tempValue)) {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault()));
					}else {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault()));
					}	
					rowItem.getCellElements().add(cellInfo);	
					
					// 파일 경로 수정 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_url_update_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 덮어쓰기 유형
					cellInfo = new dhtmlXGridRowCell();
					tempValue = deleteRecfileItem.getR_overwrite_type();						
					if("Y".equals(tempValue)) {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault()));
					}else {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault()));
					}
					rowItem.getCellElements().add(cellInfo);	
					
					// 덮어쓰기 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_overwrite_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 적합성 검사 유형
					cellInfo = new dhtmlXGridRowCell();						
					tempValue = deleteRecfileItem.getR_conformity_type();						
					if("Y".equals(tempValue)) {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault()));
					}else {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault()));
					}
					rowItem.getCellElements().add(cellInfo);	
					
					// 적합성 검사 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_conformity_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 듀얼 녹취 백업 유형
					cellInfo = new dhtmlXGridRowCell();						
					tempValue = deleteRecfileItem.getR_dual_backup_type();						
					if("Y".equals(tempValue)) {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.use", null,Locale.getDefault()));
					}else {
						cellInfo.setValue(messageSource.getMessage("admin.detail.option.notUse", null,Locale.getDefault()));
					}
					rowItem.getCellElements().add(cellInfo);	
					
					// 듀얼 녹취 백업 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_dual_backup_type());
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 날짜			
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(messageSource.getMessage("admin.backup.label." + deleteRecfileItem.getR_back_select(), null,Locale.getDefault()));
					rowItem.getCellElements().add(cellInfo);
					
					// 백업 날짜 DB
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_back_select());
					rowItem.getCellElements().add(cellInfo);	
					
					// 백업 데이터
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_back_date());
					rowItem.getCellElements().add(cellInfo);	
					
					// 백업 경로
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_back_path());
					rowItem.getCellElements().add(cellInfo);	
					
					// 시퀀스
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(deleteRecfileItem.getR_seq());
					rowItem.getCellElements().add(cellInfo);
					
					// 지금 백업 시작
					cellInfo = new dhtmlXGridRowCell();						
					cellInfo.setValue("<button class=\"nowBackStart"+(deleteRecfileItem.getR_seq())+" ui_btn_white \" onclick=\"backNowStart("+deleteRecfileItem.getR_seq()+")\">" + messageSource.getMessage("admin.title.backNowStart", null,Locale.getDefault()) + "</button>");
					rowItem.getCellElements().add(cellInfo);
					
					// 다운로드
					cellInfo = new dhtmlXGridRowCell();						
					cellInfo.setValue("<button class=\"nowDownStart"+(deleteRecfileItem.getR_seq())+" ui_btn_white \" onclick=\"FileDownload('"+deleteRecfileItem.getR_seq()+"')\">" + messageSource.getMessage("admin.title.backDown", null,Locale.getDefault()) + "</button>");
					rowItem.getCellElements().add(cellInfo);
											
					// 백업 상태
					cellInfo = new dhtmlXGridRowCell();
					tempValue = deleteRecfileItem.getR_back_state();		
					if(StringUtil.isNull(tempValue)) {
						tempValue = "-";
					}
					cellInfo.setValue("<div class='backupState"+(deleteRecfileItem.getR_seq())+"'>"+tempValue+"</div>");						
					rowItem.getCellElements().add(cellInfo);
							
					
					
					// 시작시간
					cellInfo = new dhtmlXGridRowCell();
					tempValue = deleteRecfileItem.getR_start_time();
					
					if(!StringUtil.isNull(tempValue)) {
						if(tempValue.length() == 6) {
							tempValue = tempValue.substring(0, 2)+":"+tempValue.substring(2, 4)+":"+tempValue.substring(4, 6);
						}else {
							tempValue = "-";
						}
					}else {
						tempValue = "-";
					}								
					cellInfo.setValue("<div class='backupStartTime"+(deleteRecfileItem.getR_seq())+"'>"+tempValue+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 실행시간
					cellInfo = new dhtmlXGridRowCell();
					tempValue = deleteRecfileItem.getR_run_time();
					
					if(!StringUtil.isNull(tempValue)) {
						if(tempValue.length() == 6) {
							tempValue = "<div class='backrunTime"+(deleteRecfileItem.getR_seq())+"'>"+tempValue.substring(0, 2)+":"+tempValue.substring(2, 4)+":"+tempValue.substring(4, 6)+"</div>";
						}else {
							tempValue = "<div class='backrunTime"+(deleteRecfileItem.getR_seq())+"'>-</div>";
						}							
					}else {
						tempValue = "<div class='backrunTime"+(deleteRecfileItem.getR_seq())+"'>-</div>";
					}
					cellInfo.setValue(tempValue);
					rowItem.getCellElements().add(cellInfo);
					
					// 끝시간
					cellInfo = new dhtmlXGridRowCell();
					tempValue = deleteRecfileItem.getR_end_time();						
					if(!StringUtil.isNull(tempValue)) {
						if(tempValue.length() == 6) {
							tempValue = tempValue.substring(0, 2)+":"+tempValue.substring(2, 4)+":"+tempValue.substring(4, 6);
						}else {
							tempValue = "-";
						}							
					}else {
						tempValue = "-";
					}
					cellInfo.setValue("<div class='backupEndTime"+(deleteRecfileItem.getR_seq())+"'>"+tempValue+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
		}
		return xmls;
	}	

	// 20200826 bella 패킷 에러 로그
	@RequestMapping(value = "/packet_log_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml packetLogManageGrid(HttpServletRequest request, HttpServletResponse response){

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {
			boolean nowAcc = nowAccessChk(request,"systemManage.packetLogManage");

			if(!nowAcc) {
				return xmls;
			}

			xmls = new dhtmlXGridXml();
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {

				xmls.setHeadElement(new dhtmlXGridHead());
	
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				//String titleBaseName = "management.subNumber.title.";
				for( int j = 0; j < 11; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					
					column.setAlign("center");				
					if (j != 0) {
						column.setSort("str");
						column.setCache("1");					
						column.setType("ro");	
						
					}
					
					switch(j) {
					
					case 0:
						column.setType("ch");	
						column.setFiltering("0");
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("50");
						column.setHidden("1");
						column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
						break;				
					case 1:			
						column.setId("nortpDate");
						column.setWidth("100");
						column.setValue("날짜");
						break;
					case 2:
						column.setId("nortpTime");
						column.setWidth("100");
						column.setValue("시간");
						break;
					case 3:
						column.setId("nortpExt");
						column.setWidth("100");
						column.setValue("내선");
						break;
					case 4:
						column.setId("sysCode");
						column.setWidth("100");
						column.setHidden("1");
						column.setValue("시스템 코드");
						break;
					case 5:
						column.setId("sysName");
						column.setWidth("100");
						column.setValue("시스템 명");
						break;
					case 6:
						column.setId("callId");
						column.setWidth("150");
						column.setValue("콜 아이디");
						break;
					case 7:
						column.setId("custPhone");
						column.setWidth("150");
						column.setValue("고객 전화번호");
						break;
					case 8:
						column.setId("custCode");
						column.setWidth("100");
						column.setValue("고객 코드");
						break;
					case 9:
						column.setId("returnUrl");
						column.setWidth("300");
						column.setValue("전송 URL");
						column.setHidden("1");
						break;
					case 10:
						column.setId("sendResult");
						column.setWidth("*");
						column.setValue("전송 결과");
						column.setHidden("1");
						break;
					}
					head.getColumnElement().add(column);
					column = null;
				}
				xmls.setHeadElement(head);
			} else {
				
				/* 20200128 김다빈 추가 */
				// prefix 제거 옵션 사용 여부
				EtcConfigInfo etcConfigPrefixYN = new EtcConfigInfo();
				etcConfigPrefixYN.setGroupKey("Prefix");
				etcConfigPrefixYN.setConfigKey("PrefixYN");
				EtcConfigInfo PrefixYN = etcConfigInfoService.selectOptionYN(etcConfigPrefixYN);
				String PrefixYNVal = "N";
				if (PrefixYN != null)
					PrefixYNVal = PrefixYN.getConfigValue();

				String[] arrPrefixInfo = null;
				if ("Y".equals(PrefixYNVal)) {
					// 전화번호 prefix 제거 처리
					EtcConfigInfo etcConfigPrefixNumber = new EtcConfigInfo();
					etcConfigPrefixNumber.setGroupKey("Prefix");
					etcConfigPrefixNumber.setConfigKey("Prefix");
					List<EtcConfigInfo> PrefixNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigPrefixNumber);
					if(PrefixNumberInfo.size() > 0) {
						arrPrefixInfo = PrefixNumberInfo.get(0).getConfigValue().split(",");
					}
				}
				
				// 전화번호 마스킹 여부
				EtcConfigInfo etcConfigMaskingYN = new EtcConfigInfo();
				etcConfigMaskingYN.setGroupKey("masking");
				etcConfigMaskingYN.setConfigKey("maskingYN");
				EtcConfigInfo maskingYN = etcConfigInfoService.selectOptionYN(etcConfigMaskingYN);
				String maskingYNVal = "N"; 
				if (maskingYN != null)
					maskingYNVal = maskingYN.getConfigValue();

				int startIdx = 0, ea = 0;
				if ("Y".equals(maskingYNVal)) {
					// 전화번호 마스킹 처리
					EtcConfigInfo etcConfigMaskingNumber = new EtcConfigInfo();
					etcConfigMaskingNumber.setGroupKey("masking");
					etcConfigMaskingNumber.setConfigKey("masking");
					List<EtcConfigInfo> maskingNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigMaskingNumber);
					String[] arrMaskingInfo;
					if(maskingNumberInfo.size() > 0) {
						arrMaskingInfo = maskingNumberInfo.get(0).getConfigValue().split(",");
						startIdx = Integer.parseInt(arrMaskingInfo[0]);
						ea = Integer.parseInt(arrMaskingInfo[1]);
					}
				}
				
				
				// 전화번호 표기옵션 여부
				EtcConfigInfo etcConfigHyphenYN = new EtcConfigInfo();
				etcConfigHyphenYN.setGroupKey("hyphen");
				etcConfigHyphenYN.setConfigKey("hyphenYN");
				EtcConfigInfo hyphenYN = etcConfigInfoService.selectOptionYN(etcConfigHyphenYN);
				String hyphenYNVal = "N";
				if (hyphenYN != null) 
					hyphenYNVal = hyphenYN.getConfigValue();

				String h1 = "", h2 = "";
				if ("Y".equals(hyphenYNVal)) {
					//전화번호 표기옵션 적용
					EtcConfigInfo etcConfigSetHyphen= new EtcConfigInfo();
					etcConfigSetHyphen.setGroupKey("hyphen");
					etcConfigSetHyphen.setConfigKey("hyphen");
					List<EtcConfigInfo> setHyphenInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigSetHyphen);
					String[] arrHyphenInfo;
					if(setHyphenInfo.size() > 0) {
						arrHyphenInfo = setHyphenInfo.get(0).getConfigValue().split(",");
						h1 = arrHyphenInfo[0];
						h2 = arrHyphenInfo[1];
					}
				}
				
				
				PacketVO packetVO = new PacketVO();
				// 검색 시 rs_recfile이랑 비교 후 있으면 삭제, 그 다음 select
				subNumberInfoService.deletePacketLogInfo(packetVO);
				
				List<PacketVO> packetVOInfoResult = null;
				
				if(!StringUtil.isNull(request.getParameter("sDate"),true) && !StringUtil.isNull(request.getParameter("eDate"),true)) {
					packetVO.setsDate(sqlFilterUtil.sqlFilter(request.getParameter("sDate").replace("-", "").toString()));
					packetVO.seteDate(sqlFilterUtil.sqlFilter(request.getParameter("eDate").replace("-", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("sTime"),true) && !StringUtil.isNull(request.getParameter("sTime"),true)) {
					packetVO.setsTime(sqlFilterUtil.sqlFilter(request.getParameter("sTime").replace(":", "").toString()));
					packetVO.seteTime(sqlFilterUtil.sqlFilter(request.getParameter("eTime").replace(":", "").toString()));
				}
	
				if(!StringUtil.isNull(request.getParameter("sysCode"),true)) {
					packetVO.setSysCode(sqlFilterUtil.sqlFilter(request.getParameter("sysCode")));
				}
				if(!StringUtil.isNull(request.getParameter("extNum"),true)) {
					packetVO.setNortpExt(sqlFilterUtil.sqlFilter(request.getParameter("extNum")));
				}
				if(!StringUtil.isNull(request.getParameter("callId"),true)) {
					packetVO.setCallId(sqlFilterUtil.sqlFilter(request.getParameter("callId")));
				}
				if(!StringUtil.isNull(request.getParameter("custPhone"),true)) {
					packetVO.setCustPhone(sqlFilterUtil.sqlFilter(request.getParameter("custPhone")));
				}
				
				if(!StringUtil.isNull(request.getParameter("rCustCode"),true)) {
					packetVO.setCustCode(sqlFilterUtil.sqlFilter(request.getParameter("rCustCode")));
				}

				if(!StringUtil.isNull(request.getParameter("limitUse"),true)) {
					packetVO.setLimitUse(request.getParameter("limitUse").toUpperCase());
				} else {
					packetVO.setLimitUse("N");
				}
				
				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					packetVO.setPosStart(posStart);
				}
				
				Integer count = 0;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("posStart"));
					packetVO.setCount(count);
				}
				
				try {
					packetVOInfoResult = subNumberInfoService.selectPacketLogInfo(packetVO);
				} catch(Exception e) {
					logger.error("error",e);
				}
				Integer logoInfoResultTotal = packetVOInfoResult.size();
				
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
	
				for(int i = 0; i < logoInfoResultTotal; i++) {
					PacketVO packetVOReurn = packetVOInfoResult.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getNortpDate());
					rowItem.getCellElements().add(cellInfo);
	
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getNortpTime());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getNortpExt() != null ? packetVOReurn.getNortpExt() : "");
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getSysCode());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getSysName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getCallId());
					rowItem.getCellElements().add(cellInfo);
					
					// 고객 전화번호 표기옵션 적용
					cellInfo = new dhtmlXGridRowCell();

					String tempStrValue = packetVOReurn.getCustPhone();
					
					try {
						if(StringUtil.isNull(tempStrValue,true)) {
							tempStrValue = "-";
						} 
						
						if (PrefixYNVal.equals("Y")) {
							tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
						}
						if (maskingYNVal.equals("Y")) {
							// 메리츠에서는 maskingPhoneNumLast 함수 사용 (전화번호가 뒤에서부터 들어와서 거꾸로 마스킹)
							tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
						}
						if (hyphenYNVal.equals("Y")) {
							tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
						}
					}catch (Exception e) {
						tempStrValue = packetVOReurn.getCustPhone();
					}
					
					cellInfo.setValue(tempStrValue);
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getCustCode());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getReturnUrl());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(packetVOReurn.getSendResult());
					rowItem.getCellElements().add(cellInfo);
	
					xmls.getRowElements().add(rowItem);
	
					rowItem = null;
				}
				Integer totalLogResult = subNumberInfoService.selectTotalPacketLog(packetVO);
				if(totalLogResult > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {
					xmls.setTotal_count(totalLogResult.toString());
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
	
	// 20210208 bella 3차 백업 관리
	@RequestMapping(value = "/file_recover_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml fileRecoverManageGrid(HttpServletRequest request, HttpServletResponse response){

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {
			boolean nowAcc = nowAccessChk(request,"systemManage.fileRecoverManage");
			
			if(!nowAcc) {
				return xmls;
			}

			String write="";
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemManage.fileRecoverManage");
			if(!StringUtil.isNull(nowAccessInfo.getWriteYn(),true))
				write=nowAccessInfo.getWriteYn();
			
			xmls = new dhtmlXGridXml();
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {

				xmls.setHeadElement(new dhtmlXGridHead());
	
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				//String titleBaseName = "management.subNumber.title.";
				for( int j = 0; j < 17; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					
					column.setAlign("center");				
					if (j != 0) {
						column.setSort("str");
						column.setCache("1");					
						column.setType("ro");	
					}
					
					switch(j) {
					
					case 0:
						column.setId("ckeckbox");
						column.setType("ch");	
						column.setFiltering("0");
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("50");
						column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
						break;				
					case 1:			
						column.setId("fileDate");
						column.setWidth("100");
						column.setValue("날짜");
						break;
					case 2:
						column.setId("fileTime");
						column.setWidth("100");
						column.setValue("시간");
						break;
					case 3:
						column.setId("sysCode");
						column.setWidth("100");
						column.setType("combo");
						column.setValue("시스템 코드");
						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=system");
						break;
					case 4:
						column.setId("extNum");
						column.setWidth("100");
						column.setValue("내선 번호");
						break;
					case 5:
						column.setId("extIp");
						column.setWidth("150");
						column.setValue("내선 IP");
						break;
					case 6:
						column.setId("callId");
						column.setWidth("150");
						column.setValue("콜 아이디");
						break;
					case 7:
						column.setId("callTtime");
						column.setWidth("150");
						column.setValue("통화 시간");
						break;
					case 8:
						column.setId("custPhone");
						column.setWidth("150");
						column.setValue("고객 전화번호");
						break;
					case 9:
						column.setId("callKind");
						column.setWidth("100");
						column.setType("combo");
						column.setValue("콜 타입");
						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=callType");
						break;
					case 10:
						column.setId("fileName");
						column.setWidth("200");
						column.setValue("파일 이름");
						break;
					case 11:
						column.setId("fileState");
						column.setWidth("100");
						column.setValue("파일 상태");
						break;
					case 12:
						column.setId("recoverYN");
						column.setWidth("150");
						column.setValue("파일 복구");
						if ("N".equals(write)) {
							column.setHidden("1");
						}
						break;
					case 13:
						column.setId("listen");
						column.setWidth("150");
						column.setValue("수정");
						if ("genesys".equals(request.getParameter("listType"))) {
							column.setHidden("1");
						}
						break;
					case 14:
						column.setId("fileRecoverIp");
						column.setWidth("*");
						column.setHidden("1");
						column.setValue("복구 서버 IP");
						break;
					case 15:
						column.setId("nortp");
						column.setWidth("*");
						column.setHidden("1");
						column.setValue("No RTP 여부");
						break;
					case 16:
						column.setId("notRecording");
						column.setWidth("*");
						column.setHidden("1");
						column.setValue("복구 필요");
						break;
					}
					head.getColumnElement().add(column);
					column = null;
				}
				xmls.setHeadElement(head);
			} else {
				
				/* 20200128 김다빈 추가 */
				// prefix 제거 옵션 사용 여부
				EtcConfigInfo etcConfigPrefixYN = new EtcConfigInfo();
				etcConfigPrefixYN.setGroupKey("Prefix");
				etcConfigPrefixYN.setConfigKey("PrefixYN");
				EtcConfigInfo PrefixYN = etcConfigInfoService.selectOptionYN(etcConfigPrefixYN);
				String PrefixYNVal = "N";
				if (PrefixYN != null)
					PrefixYNVal = PrefixYN.getConfigValue();
				
				// 전화번호 prefix 제거 처리
				EtcConfigInfo etcConfigPrefixNumber = new EtcConfigInfo();
				etcConfigPrefixNumber.setGroupKey("Prefix");
				etcConfigPrefixNumber.setConfigKey("Prefix");
				List<EtcConfigInfo> PrefixNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigPrefixNumber);
				String[] arrPrefixInfo = null;
				if(PrefixNumberInfo.size() > 0) {
					arrPrefixInfo = PrefixNumberInfo.get(0).getConfigValue().split(",");
				}
				
				// 전화번호 마스킹 여부
				EtcConfigInfo etcConfigMaskingYN = new EtcConfigInfo();
				etcConfigMaskingYN.setGroupKey("masking");
				etcConfigMaskingYN.setConfigKey("maskingYN");
				EtcConfigInfo maskingYN = etcConfigInfoService.selectOptionYN(etcConfigMaskingYN);
				String maskingYNVal = "N"; 
				if (maskingYN != null)
					maskingYNVal = maskingYN.getConfigValue();
				
				// 전화번호 마스킹 처리
				EtcConfigInfo etcConfigMaskingNumber = new EtcConfigInfo();
				etcConfigMaskingNumber.setGroupKey("masking");
				etcConfigMaskingNumber.setConfigKey("masking");
				List<EtcConfigInfo> maskingNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigMaskingNumber);
				String[] arrMaskingInfo;
				int startIdx = 0, ea = 0;
				if(maskingNumberInfo.size() > 0) {
					arrMaskingInfo = maskingNumberInfo.get(0).getConfigValue().split(",");
					startIdx = Integer.parseInt(arrMaskingInfo[0]);
					ea = Integer.parseInt(arrMaskingInfo[1]);
				}
				
				// 전화번호 표기옵션 여부
				EtcConfigInfo etcConfigHyphenYN = new EtcConfigInfo();
				etcConfigHyphenYN.setGroupKey("hyphen");
				etcConfigHyphenYN.setConfigKey("hyphenYN");
				EtcConfigInfo hyphenYN = etcConfigInfoService.selectOptionYN(etcConfigHyphenYN);
				String hyphenYNVal = "N";
				if (hyphenYN != null) 
					hyphenYNVal = hyphenYN.getConfigValue();
				
				//전화번호 표기옵션 적용
				EtcConfigInfo etcConfigSetHyphen= new EtcConfigInfo();
				etcConfigSetHyphen.setGroupKey("hyphen");
				etcConfigSetHyphen.setConfigKey("hyphen");
				List<EtcConfigInfo> setHyphenInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigSetHyphen);
				String[] arrHyphenInfo;
				String h1 = "", h2 = "";
				if(setHyphenInfo.size() > 0) {
					arrHyphenInfo = setHyphenInfo.get(0).getConfigValue().split(",");
					h1 = arrHyphenInfo[0];
					h2 = arrHyphenInfo[1];
				}
				
				// Postgres 암호화 사용여부
				String postgresColumn = "";
				EtcConfigInfo etcConfigInfo= new EtcConfigInfo();
				etcConfigInfo.setGroupKey("ENCRYPT");
				etcConfigInfo.setConfigKey("POSTGRES");
				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if(etcConfigResult.size() > 0) {
					if("Y".equals(etcConfigResult.get(0).getConfigValue())){
						etcConfigInfo.setGroupKey("ENCRYPT");
						etcConfigInfo.setConfigKey("COLUMN");
						
						etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
						
						postgresColumn = etcConfigResult.get(0).getConfigValue();
					}
				}else {
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);				
					etcConfigInfo.setGroupKey("ENCRYPT");
					etcConfigInfo.setConfigKey("COLUMN");
					etcConfigInfo.setConfigValue("N");				
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);				
				}
				
				FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();
				
				if(!StringUtil.isNull(request.getParameter("sDate"),true) && !StringUtil.isNull(request.getParameter("eDate"),true)) {
					fileRecoverInfo.setsDate(sqlFilterUtil.sqlFilter(request.getParameter("sDate").replace("-", "").toString()));
					fileRecoverInfo.seteDate(sqlFilterUtil.sqlFilter(request.getParameter("eDate").replace("-", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("sTime"),true) && !StringUtil.isNull(request.getParameter("eTime"),true)) {
					fileRecoverInfo.setsTime(sqlFilterUtil.sqlFilter(request.getParameter("sTime").replace(":", "").toString()));
					fileRecoverInfo.seteTime(sqlFilterUtil.sqlFilter(request.getParameter("eTime").replace(":", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("sTtime"),true)) {
					if (request.getParameter("sTtime").contains(":")) {
						fileRecoverInfo.setsTtime(sqlFilterUtil.sqlFilter(getTimetoLong(request.getParameter("sTtime"))));
					} else {
						fileRecoverInfo.setsTtime(sqlFilterUtil.sqlFilter(request.getParameter("sTtime")));
					}
				}
				if(!StringUtil.isNull(request.getParameter("eTtime"),true)) {
					if (request.getParameter("eTtime").contains(":")) {
						fileRecoverInfo.seteTtime(sqlFilterUtil.sqlFilter(getTimetoLong(request.getParameter("eTtime"))));
					} else {
						fileRecoverInfo.seteTtime(sqlFilterUtil.sqlFilter(request.getParameter("eTtime")));
					}
				}
				
				if(!StringUtil.isNull(request.getParameter("sysCode"),true)) {
					fileRecoverInfo.setSysCode(request.getParameter("sysCode"));
				}
				if(!StringUtil.isNull(request.getParameter("extNum"),true)) {
					fileRecoverInfo.setExtNum(request.getParameter("extNum"));
				}
				if(!StringUtil.isNull(request.getParameter("extIp"),true)) {
					fileRecoverInfo.setExtIp(request.getParameter("extIp"));
				}
				if(!StringUtil.isNull(request.getParameter("callId"),true)) {
					fileRecoverInfo.setCallId(request.getParameter("callId"));
				}
				if(!StringUtil.isNull(request.getParameter("callTtime"),true)) {
					fileRecoverInfo.setCallTtime(request.getParameter("callTtime"));
				}
				if(!StringUtil.isNull(request.getParameter("custPhone"),true)) {
					fileRecoverInfo.setCustPhone(request.getParameter("custPhone"));
				}
				if(!StringUtil.isNull(request.getParameter("callKind"),true)) {
					fileRecoverInfo.setCallKind(request.getParameter("callKind"));
				}
				if(!StringUtil.isNull(request.getParameter("fileName"),true)) {
					fileRecoverInfo.setFileName(request.getParameter("fileName"));
				}
				if(!StringUtil.isNull(request.getParameter("fileState"),true)) {
					fileRecoverInfo.setFileState(request.getParameter("fileState"));
				}
				if(!StringUtil.isNull(request.getParameter("recfileExists"),true)) {
					fileRecoverInfo.setRecfileExists(request.getParameter("recfileExists"));
				}
				if(!StringUtil.isNull(request.getParameter("limitUse"),true)) {
					fileRecoverInfo.setLimitUse(request.getParameter("limitUse").toUpperCase());
				} else {
					fileRecoverInfo.setLimitUse("N");
				}
				
				if(!"".equals(postgresColumn)) {
					if(postgresColumn.contains("r_cust_phone1")) {
						fileRecoverInfo.setCustPhoneIsEncrypt("Y");
					}
				}
				
				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					fileRecoverInfo.setPosStart(posStart);
				}
				
				Integer count = 0;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("posStart"));
					fileRecoverInfo.setCount(count);
				}

				List<FileRecoverInfo> fileRecoverInfoResult = null;
				// 제네시스 리스트 선택 시 rs_genesys_info 리스트 보여줌
				if ("genesys".equals(request.getParameter("listType"))) {
					try {
						fileRecoverInfoResult = fileRecoverService.selectGenesysInfo(fileRecoverInfo);
					} catch(Exception e) {
						logger.error("error",e);
					}
					Integer resultTotal = fileRecoverInfoResult.size();
					
					xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
					dhtmlXGridRowCell cellInfo = null;
					for(int i = 0; i < resultTotal; i++) {
						FileRecoverInfo fileRecoverInfoReturn = fileRecoverInfoResult.get(i);
						
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(String.valueOf(i+1));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
						
						// 체크박스
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("0");
						rowItem.getCellElements().add(cellInfo);
						
						// 날짜
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getFileDate());
						cellInfo.setValue(String.format("%s-%s-%s", fileRecoverInfoReturn.getRecDate().substring(0, 4), fileRecoverInfoReturn.getRecDate().substring(4,  6), fileRecoverInfoReturn.getRecDate().substring(6, 8)));
						rowItem.getCellElements().add(cellInfo);
		
						// 시간
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getFileTime());
						cellInfo.setValue(String.format("%s:%s:%s", fileRecoverInfoReturn.getRecTime().substring(0, 2), fileRecoverInfoReturn.getRecTime().substring(2,  4), fileRecoverInfoReturn.getRecTime().substring(4, 6)));
						rowItem.getCellElements().add(cellInfo);
	
						// 시스템코드
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getSysCode());
						if(StringUtil.isNull(fileRecoverInfoReturn.getSysCode(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getSysCode().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 내선번호
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getExtNum());
						if(StringUtil.isNull(fileRecoverInfoReturn.getExtNum(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getExtNum().trim());
						}
						rowItem.getCellElements().add(cellInfo);
	
						// 내선IP
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getExtIp());
						if(StringUtil.isNull(fileRecoverInfoReturn.getExtIp(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getExtIp().trim());
						}
						rowItem.getCellElements().add(cellInfo);
	
						// 콜아이디
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getExtIp());
						if(StringUtil.isNull(fileRecoverInfoReturn.getCallId(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getCallId().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 통화시간
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getCallTtime());
						if(StringUtil.isNull(fileRecoverInfoReturn.getCallTtime(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getCallTtime().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 고객 전화번호 표기옵션 적용
						cellInfo = new dhtmlXGridRowCell();
						String tempStrValue = fileRecoverInfoReturn.getCustPhone();
						try {
							if(StringUtil.isNull(tempStrValue,true)) {
								tempStrValue = "-";
							} else {
								tempStrValue = tempStrValue.trim();
								if (PrefixYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
								}
								if (maskingYNVal.equals("Y")) {
									// 메리츠에서는 maskingPhoneNumLast 함수 사용 (전화번호가 뒤에서부터 들어와서 거꾸로 마스킹)
									tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
								}
								if (hyphenYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
								}
							}
						}catch (Exception e) {
							tempStrValue = fileRecoverInfoReturn.getCustPhone().trim();
						}
						cellInfo.setValue(tempStrValue);
						rowItem.getCellElements().add(cellInfo);
	
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getCallKind());
						if(StringUtil.isNull(fileRecoverInfoReturn.getCallKind(),true))
							cellInfo.setValue("-");
						else {		
							cellInfo.setValue(fileRecoverInfoReturn.getCallKind().trim());
						}
						rowItem.getCellElements().add(cellInfo);
	
						// 파일명
						cellInfo = new dhtmlXGridRowCell();
						if(StringUtil.isNull(fileRecoverInfoReturn.getFileName(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getFileName().trim());
						}
						rowItem.getCellElements().add(cellInfo);
	
						// 파일 상태
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getFileState());
						if(StringUtil.isNull(fileRecoverInfoReturn.getFileState(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(messageSource.getMessage("admin.fileRecover.state."+fileRecoverInfoReturn.getFileState().trim(), null,Locale.getDefault()));
						}
						rowItem.getCellElements().add(cellInfo);
	
						// 수동 복구 요청
						cellInfo = new dhtmlXGridRowCell();
						if (StringUtil.isNull(fileRecoverInfoReturn.getFileState(),true) 
								|| "MS".equals(fileRecoverInfoReturn.getFileState()) 
								|| "AS".equals(fileRecoverInfoReturn.getFileState())){
							cellInfo.setValue("");
						} else if (StringUtil.isNull(fileRecoverInfoReturn.getFileState(),true) 
								|| "MW".equals(fileRecoverInfoReturn.getFileState()) 
								|| "AW".equals(fileRecoverInfoReturn.getFileState()) 
								|| "MF".equals(fileRecoverInfoReturn.getFileState()) 
								|| "AF".equals(fileRecoverInfoReturn.getFileState())) {
							cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='fileRecoverBtn' onClick='requestFileRecover(\""+String.valueOf(i+1)+"\");'>재복구</button>");
						} else {
							cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='fileRecoverBtn' onClick='requestFileRecover(\""+String.valueOf(i+1)+"\");'>복구</button>");
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 수정 버튼
						// cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='listenRecoverFileBtn' onClick='listenRecoverFile(\""+String.valueOf(i+1)+"\");'>청취</button>");
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat icon_btn_pen_white userModiBtn' onclick='modifyFileInfo("+String.valueOf(i+1)+");'>"+messageSource.getMessage("evaluation.campaign.grid.modification", null,Locale.getDefault()) +"</button>");
						rowItem.getCellElements().add(cellInfo);
						
						
						cellInfo = new dhtmlXGridRowCell();
						if(StringUtil.isNull(fileRecoverInfoReturn.getRecoverServerIp(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getRecoverServerIp());
						}
						rowItem.getCellElements().add(cellInfo);

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(fileRecoverInfoReturn.getNortp());
						rowItem.getCellElements().add(cellInfo);
						
						// 파일명
						cellInfo = new dhtmlXGridRowCell();
						if(StringUtil.isNull(fileRecoverInfoReturn.getRecfileExists(),true)) {
							cellInfo.setValue("N");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getRecfileExists().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						xmls.getRowElements().add(rowItem);
		
						rowItem = null;
					}
	
					Integer fileRecoverInfoTotal = fileRecoverService.selectGenesysInfoTotal(fileRecoverInfo);
					if(fileRecoverInfoTotal > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {
						xmls.setTotal_count(fileRecoverInfoTotal.toString());
					} else {
						xmls.setTotal_count("");
					}
					if(request.getParameter("posStart") != null) {
						xmls.setPos(request.getParameter("posStart"));
					} else {
						xmls.setPos("0");
					}
				} else {
					// RTP 선택 시 rs_file_recover 리스트 보여줌
					try {
						fileRecoverInfoResult = fileRecoverService.selectFileRecoverInfo(fileRecoverInfo);
					} catch(Exception e) {
						logger.error("error",e);
					}
					Integer resultTotal = fileRecoverInfoResult.size();
					
					xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
					dhtmlXGridRowCell cellInfo = null;
		
					for(int i = 0; i < resultTotal; i++) {
						FileRecoverInfo fileRecoverInfoReturn = fileRecoverInfoResult.get(i);
						
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(String.valueOf(i+1));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
		
						// 체크박스
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("0");
						rowItem.getCellElements().add(cellInfo);
						
						// 날짜
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getFileDate());
						cellInfo.setValue(String.format("%s-%s-%s", fileRecoverInfoReturn.getRecDate().substring(0, 4), fileRecoverInfoReturn.getRecDate().substring(4,  6), fileRecoverInfoReturn.getRecDate().substring(6, 8)));
						rowItem.getCellElements().add(cellInfo);
		
						// 시간
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getFileTime());
						cellInfo.setValue(String.format("%s:%s:%s", fileRecoverInfoReturn.getRecTime().substring(0, 2), fileRecoverInfoReturn.getRecTime().substring(2,  4), fileRecoverInfoReturn.getRecTime().substring(4, 6)));
						rowItem.getCellElements().add(cellInfo);

						// 시스템코드
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getSysCode());
						if(StringUtil.isNull(fileRecoverInfoReturn.getSysCode(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getSysCode().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 내선번호
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getExtNum());
						if(StringUtil.isNull(fileRecoverInfoReturn.getExtNum(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getExtNum().trim());
						}
						rowItem.getCellElements().add(cellInfo);

						// 내선IP
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getExtIp());
						if(StringUtil.isNull(fileRecoverInfoReturn.getExtIp(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getExtIp().trim());
						}
						rowItem.getCellElements().add(cellInfo);

						// 콜아이디
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getExtIp());
						if(StringUtil.isNull(fileRecoverInfoReturn.getCallId(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getCallId().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 통화시간
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getCallTtime());
						if(StringUtil.isNull(fileRecoverInfoReturn.getCallTtime(),true)) {
							cellInfo.setValue("-");
						} else {
							cellInfo.setValue(fileRecoverInfoReturn.getCallTtime().trim());
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 고객 전화번호 표기옵션 적용
						cellInfo = new dhtmlXGridRowCell();
						String tempStrValue = fileRecoverInfoReturn.getCustPhone();
						try {
							if(StringUtil.isNull(tempStrValue,true)) {
								tempStrValue = "-";
							} else {
								tempStrValue = tempStrValue.trim();
								if (PrefixYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
								}
								if (maskingYNVal.equals("Y")) {
									// 메리츠에서는 maskingPhoneNumLast 함수 사용 (전화번호가 뒤에서부터 들어와서 거꾸로 마스킹)
									tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
								}
								if (hyphenYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
								}
							}
						}catch (Exception e) {
							tempStrValue = fileRecoverInfoReturn.getCustPhone().trim();
						}
						cellInfo.setValue(tempStrValue);
						rowItem.getCellElements().add(cellInfo);

						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getCallKind());
						if(StringUtil.isNull(fileRecoverInfoReturn.getCallKind(),true))
							cellInfo.setValue("-");
						else {		
							cellInfo.setValue(fileRecoverInfoReturn.getCallKind().trim());
						}
						rowItem.getCellElements().add(cellInfo);

						// 파일명
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(fileRecoverInfoReturn.getFileName().trim());
						rowItem.getCellElements().add(cellInfo);

						// 파일 상태
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue(fileRecoverInfoReturn.getFileState());
						cellInfo.setValue(messageSource.getMessage("admin.fileRecover.state."+fileRecoverInfoReturn.getFileState().trim(), null,Locale.getDefault()));
						rowItem.getCellElements().add(cellInfo);

						// 수동 복구 요청
						cellInfo = new dhtmlXGridRowCell();
						String fileState = fileRecoverInfoReturn.getFileState().trim();
						
						if (StringUtil.isNull(fileRecoverInfoReturn.getFileState(),true) 
								|| "MS".equals(fileRecoverInfoReturn.getFileState()) 
								|| "AS".equals(fileRecoverInfoReturn.getFileState())){
							cellInfo.setValue("");
						} else if (StringUtil.isNull(fileRecoverInfoReturn.getFileState(),true) 
								|| "MW".equals(fileRecoverInfoReturn.getFileState()) 
								|| "AW".equals(fileRecoverInfoReturn.getFileState()) 
								|| "MF".equals(fileRecoverInfoReturn.getFileState()) 
								|| "AF".equals(fileRecoverInfoReturn.getFileState())) {
							cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='fileRecoverBtn' onClick='requestFileRecover(\""+String.valueOf(i+1)+"\");'>재복구</button>");
						} else {
							cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='fileRecoverBtn' onClick='requestFileRecover(\""+String.valueOf(i+1)+"\");'>복구</button>");
						}
						rowItem.getCellElements().add(cellInfo);
						
						// 수정 버튼
						// cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='listenRecoverFileBtn' onClick='listenRecoverFile(\""+String.valueOf(i+1)+"\");'>청취</button>");
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat icon_btn_pen_white userModiBtn' onclick='modifyFileInfo("+String.valueOf(i+1)+");'>"+messageSource.getMessage("evaluation.campaign.grid.modification", null,Locale.getDefault()) +"</button>");
						rowItem.getCellElements().add(cellInfo);
						
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(fileRecoverInfoReturn.getRecoverServerIp());
						rowItem.getCellElements().add(cellInfo);

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(fileRecoverInfoReturn.getNortp());
						rowItem.getCellElements().add(cellInfo);
						
						xmls.getRowElements().add(rowItem);
		
						rowItem = null;
					}

					Integer fileRecoverInfoTotal = fileRecoverService.selectFileRecoverInfoTotal(fileRecoverInfo);
					if(fileRecoverInfoTotal > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {
						xmls.setTotal_count(fileRecoverInfoTotal.toString());
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
		}
		return xmls;
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
	public String getTimetoLong(String t) {
		if(t.indexOf(":")<0) {
			return String.valueOf(t);
		}

		String[] k = t.split(":");
		int df = Integer.parseInt(k[0]) * 3600 + Integer.parseInt(k[1]) * 60 + Integer.parseInt(k[2]);

		return String.valueOf(df);
	}
}
