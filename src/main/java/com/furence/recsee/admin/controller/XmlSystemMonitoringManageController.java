/*package com.furence.recsee.admin.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.monitoring.model.AlertList;
import com.furence.recsee.monitoring.model.ControlTargetVO;
import com.furence.recsee.monitoring.model.ItemVo;
import com.furence.recsee.monitoring.service.MonitoringInfoService;


@Controller
public class XmlSystemMonitoringManageController {
	
	@Autowired
	private MonitoringInfoService monitoringInfoService;
	

	// 관제 대상 관리
	@RequestMapping(value="/SystemManage.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml System_manage(HttpServletRequest request) {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.channel");

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.channel.title.";
			for( int j = 0; j < 8; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("int");
				} else {
					if(!nowAccessInfo.getModiYn().equals("Y"))
						column.setType("ro");
					else
						column.setType("ed");
					column.setSort("str");
				}
				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("*");

				switch(j) {
				case 0:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				case 1:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue("id");
					column.setHidden("1");
					break;
				case 2:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue("순번");
					column.setSort("int");
					break;
				case 3:
					column.setEditable("0");
					column.setWidth("450");
					column.setAlign("left");
					column.setValue("이름");
					break;
				case 4:
					column.setWidth("100");
					column.setValue("종류");
					break;
				case 5:
					column.setWidth("100");
					column.setValue("IP");
					break;
				case 6:
					column.setWidth("200");
					column.setValue("설명");
					break;
				case 7:
					column.setWidth("80");
					column.setValue("관제 유무");
					break;
				}
				if(!nowAccessInfo.getModiYn().equals("Y")) {
					column.setEditable("0");
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
			headCall.getParamElement().add(
					"#rspan,"
					+ "<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,"
					+ "#select_filter,"
					+ "#text_filter,"
					+ "#text_filter,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "#select_filter"
			);

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			List<ControlTargetVO> controlTargetResult = monitoringInfoService.selectTargetList();
			Integer controlTargetResultTotal = controlTargetResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < controlTargetResultTotal; i++) {
				ControlTargetVO targetItem = controlTargetResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_id());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i+1));
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_name());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				
				if(targetItem.getRm_target_type()!=null) {
					String val=null;
					switch (targetItem.getRm_target_type()) {
					case "server":
						val="서버";
						break;
					case "job":
						val="업무";
						break;
					case "tel":
						val="전화기";
						break;
					default:
						val="기타";
						break;
					}
					cellInfo.setValue(val);
				}
			 	else
			  		cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_ip());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_descr());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_use());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}

	// 관제 지표 설정 관리
	@RequestMapping(value="/attrManage.xml", method=RequestMethod.GET, produces="application/xml;charset=UTF-8")
	public @ResponseBody dhtmlXGridXml attr_manage(HttpServletRequest request) {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for( int j = 0; j < 7; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("*");
				

				switch(j) {
				case 0:
					column.setHidden("1");
					column.setType("ch");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				case 1:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("300");
					column.setValue("id");
					column.setAlign("left");
					column.setHidden("1");
					break;
				case 2:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("300");
					column.setValue("관측 항목");
					column.setAlign("left");
					break;
				case 3:
					column.setEditable("0");
					column.setWidth("150");
					column.setValue("유형");
					column.setAlign("center");
					break;
				case 4:
					column.setEditable("0");
					column.setWidth("450");
					column.setValue("알림 임계치 정의");
					column.setAlign("center");
					break;
				case 5:
					column.setEditable("0");
					column.setWidth("450");
					column.setValue("단위");
					column.setAlign("center");
					column.setHidden("1");
					break;
				case 6:
					column.setEditable("0");
					column.setWidth("150");
					column.setValue("알림 표시 유무");
					column.setAlign("center");
					break;
				}

				head.getColumnElement().add(column);
				column = null;
			}

			xmls.setHeadElement(head);


			List<ItemVo> TargetItemListResult = monitoringInfoService.selectTargetItemList();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			for(int i = 0; i < TargetItemListResult.size(); i++) {
				ItemVo targetItem = TargetItemListResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_item_index());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<span class=name_title>"+targetItem.getRm_item_name()+"</span>");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				String type=null;
				if(targetItem.getRm_item_type()!=null) {
					switch (targetItem.getRm_item_type()) {
					case "server":
						type="서버";
						break;
					case "work":
						type="업무";
						break;
					default:
						type="기타";
						break;
					}
				}
				cellInfo.setValue(type);
				rowItem.getCellElements().add(cellInfo);
				

				cellInfo = new dhtmlXGridRowCell();
				if(targetItem.getRm_item_unit()!=null) {
					switch (targetItem.getRm_item_unit()) {
					case "on/off":
						if(targetItem.getRm_item_limit().equals("Off"))
							cellInfo.setValue("<input type='radio' style='width: 20px !important; margin: 13px 0;' name='inputVal"+(i+1)+"' value='On'>가동</input><input type='radio' style='width: 20px !important; margin-left:15px;' name='inputVal"+(i+1)+"' value='Off' checked='true'>비가동</input>");
						else
							cellInfo.setValue("<input type='radio' style='width: 20px !important; margin: 13px 0;' name='inputVal"+(i+1)+"' value='On' checked='true'>가동</input><input type='radio' style='width: 20px !important; margin-left:15px;'name='inputVal"+(i+1)+"' value='Off'>비가동</input>");
						break;
					case "input":
						cellInfo.setValue("<input class='input_input' id='inputVal"+(i+1)+"' value='"+targetItem.getRm_item_limit()+"' style='text-align:center; margin-right:13px; width: 122px !important; background-color:transparent; border: 1px solid #bbbbbb;'/>");
						break;
					default:
						cellInfo.setValue("<input class='slider_input' id='inputVal"+(i+1)+"' type='text' style='text-align:center; width: 122px !important; border: 1px solid #bbbbbb; background-color:transparent;'> % <div class='slider' id='sliderObj"+(i+1)+"' min='0' max='100' setp='1' value='"+targetItem.getRm_item_limit()+"' vertical='false' size='120'></div>");
						break;
					}
				}
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_item_unit());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if(targetItem.getRm_item_log_yn().equals("Off"))
					cellInfo.setValue("<input type='radio' style='width: 20px !important; margin: 13px 0;' name='inputValLog"+(i+1)+"' value='On'>켜기</input><input type='radio' style='width: 20px !important; margin-left:15px;' name='inputValLog"+(i+1)+"' value='Off' checked='true'>끄기</input>");
				else
					cellInfo.setValue("<input type='radio' style='width: 20px !important; margin: 13px 0;' name='inputValLog"+(i+1)+"' value='On' checked='true'>켜기</input><input type='radio' style='width: 20px !important; margin-left:15px;'name='inputValLog"+(i+1)+"' value='Off'>끄기</input>");
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
		
		// 알림 이력 항목 관리
	@RequestMapping(value="/alertManage.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml alert_manage(HttpServletRequest request) {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.channel");

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.channel.title.";
			for( int j = 0; j < 10; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else {
					column.setType("ro");
				}
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("*");
				column.setSort("str");
				

				switch(j) {
				case 0:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				case 1:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("순번");
					column.setSort("int");
					break;
				case 2:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("인덱스(pk)");
					column.setSort("int");
					column.setHidden("1");
					break;
				case 3:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue("발생대상코드");
					column.setHidden("1");
					break;
				case 4:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("180");
					column.setValue("발생 대상");
					break;
				case 5:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("150");
					column.setValue("발생 원인");
					break;
				case 6:
					column.setWidth("200");
					column.setValue("발생 일시");
					break;
				case 7:
					column.setWidth("200");
					column.setValue("해결 일시");
					break;
				case 8:
					column.setWidth("100");
					column.setValue("발생 당시 상태");
					break;
				case 9:
					column.setWidth("100");
					column.setValue("발생 당시 임계치");
					break;
				}
				
				if(!nowAccessInfo.getModiYn().equals("Y")) {
					column.setEditable("0");
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
			headCall.getParamElement().add(
					"#rspan,"
					+ "<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,"
					+ "#rspan,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "#text_filter,"
					+ "#text_filter,"
					+ "#rspan,"
					+ "#rspan"
			);

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			AlertList alertList = new AlertList();

			if(request.getParameter("startDate")!=null&&!request.getParameter("startDate").equals("")) {
				alertList.setStartDate(request.getParameter("startDate"));
			}
			if(request.getParameter("endDate")!=null&&!request.getParameter("endDate").equals("")) {
				alertList.setStartDate(request.getParameter("endDate"));
			}
			
			List<AlertList> alertListResult = monitoringInfoService.selectAlertList(alertList);
			Integer alertListResultTotal = alertListResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < alertListResultTotal; i++) {
				AlertList alertListItem = alertListResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i+1));
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getR_alert_index()!=null&&!"".equals(alertListItem.getR_alert_index()))
					cellInfo.setValue(alertListItem.getR_alert_index());
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getrAlertObject()!=null&&!"".equals(alertListItem.getrAlertObject()))
					cellInfo.setValue(alertListItem.getrAlertObject());
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getRm_target_name()!=null&&!"".equals(alertListItem.getRm_target_name()))
					cellInfo.setValue(alertListItem.getRm_target_name());
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getrAlertReasonObject()!=null&&!"".equals(alertListItem.getrAlertReasonObject()))
					cellInfo.setValue(alertListItem.getrAlertReasonObject());
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				
				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getrAlertStartDate()!=null&&!"".equals(alertListItem.getrAlertStartDate())) {
					if(alertListItem.getrAlertStartTime()!=null&&!"".equals(alertListItem.getrAlertStartTime())) {
						String tempDate=alertListItem.getrAlertStartDate().substring(0, 4)+"-"+alertListItem.getrAlertStartDate().substring(4, 6)+"-"+alertListItem.getrAlertStartDate().substring(6);
						String tempTime=alertListItem.getrAlertStartTime().substring(0, 2)+":"+alertListItem.getrAlertStartTime().substring(2, 4)+":"+alertListItem.getrAlertStartTime().substring(4);
						cellInfo.setValue(tempDate+" "+tempTime);
					}
				}
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				
				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getrAlertEndDate()!=null&&!"".equals(alertListItem.getrAlertEndDate())) {
					if(alertListItem.getrAlertEndTime()!=null&&!"".equals(alertListItem.getrAlertEndTime())) {
						String tempDate=alertListItem.getrAlertEndDate().substring(0, 4)+"-"+alertListItem.getrAlertEndDate().substring(4, 6)+"-"+alertListItem.getrAlertEndDate().substring(6);
						String tempTime=alertListItem.getrAlertEndTime().substring(0, 2)+":"+alertListItem.getrAlertEndTime().substring(2, 4)+":"+alertListItem.getrAlertEndTime().substring(4);
						cellInfo.setValue(tempDate+" "+tempTime);
					}
				}
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				

				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getrAlertState()!=null&&!"".equals(alertListItem.getrAlertState())) {
					if(alertListItem.getrAlertStateType()!=null&&!"".equals(alertListItem.getrAlertStateType())) {
						String temp=null;
						switch (alertListItem.getrAlertStateType()) {
						case "percent":
							temp=alertListItem.getrAlertState()+"%";
							break;
						default:
							temp=alertListItem.getrAlertState();
							break;
						}
						cellInfo.setValue(temp);
					}
				}
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if(alertListItem.getrAlertDefine()!=null&&!"".equals(alertListItem.getrAlertDefine())) {
					if(alertListItem.getrAlertStateType()!=null&&!"".equals(alertListItem.getrAlertStateType())) {
						String temp=null;
						switch (alertListItem.getrAlertStateType()) {
						case "percent":
							temp=alertListItem.getrAlertDefine()+"%";
							break;
						default:
							temp=alertListItem.getrAlertDefine();
							break;
						}
						cellInfo.setValue(temp);
					}
				}
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
				
	//관제 항목 관리
	@RequestMapping(value="/itemManage.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml itemManageGrid(HttpServletRequest request) {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for( int j = 0; j < 7; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j) {
				// 키
				case 0:
					column.setWidth("500");
					column.setType("tree");
					column.setAlign("left");
					column.setValue("<div style=\"text-align:center;\">관제 대상</div>");
					break;
				case 1:
					column.setWidth("*");
					column.setValue("<div style=\"text-align:center;\">관제 대상 타입</div>");
					column.setHidden("1");
					break;
				case 2:
					column.setWidth("150");
					column.setValue("<div style=\"text-align:center;\">관제 항목 부모pk</div>");
					column.setHidden("1");
					break;
				case 3:
					column.setWidth("150");
					column.setValue("<div style=\"text-align:center;\">관제 항목 리스트 pk</div>");
					column.setHidden("1");
					break;
				// 뎁스
				case 4:
					column.setWidth("400");
					column.setValue("<div style=\"text-align:center;\">관제 임계점</div>");
					break;
				case 5:
					column.setWidth("200");
					column.setValue("<div style=\"text-align:center;\">삭제</div>");
					break;
				case 6:
					column.setWidth("200");
					column.setValue("<div style=\"text-align:center;\">사용 관제 항목 pk</div>");
					column.setHidden("1");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			List<ControlTargetVO> selectTargetItemListResult = monitoringInfoService.selectUsingTargetItemList();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < selectTargetItemListResult.size(); i++) {
				ControlTargetVO item = selectTargetItemListResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				

				//관제 대상 트리
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getRm_target_name()+"<button class='ui_btn_white icon_btn_add_gray add_item'></button>");
				cellInfo.setImage("folder.gif");
				cellInfo.setEditable("0");
				rowItem.getCellElements().add(cellInfo);

				// 관제 대상 타입(히든)
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getRm_target_type());
				rowItem.getCellElements().add(cellInfo);
				
				// 관제 항목 PK
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getRm_target_id());
				rowItem.getCellElements().add(cellInfo);
				
				//관제 항목코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				
				//관제 임계점
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				//삭제
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				
				
				rowItem.setRowElements(new ArrayList<dhtmlXGridRow>());
				for(int k = 0; k < item.getItemList().size(); k++) {
					dhtmlXGridRowCell cellInfo2 = null;
					
					ItemVo item2 = item.getItemList().get(k);
					
					dhtmlXGridRow rowItem2 = new dhtmlXGridRow();
					rowItem2.setId(String.valueOf(i+1)+"child"+String.valueOf(k+1));
					rowItem2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					

					//관제 대상 트리
					cellInfo2 = new dhtmlXGridRowCell();
					cellInfo2.setValue(item2.getRm_item_name());
					cellInfo.setImage("folder.gif");
					rowItem2.getCellElements().add(cellInfo2);

					// 관제 대상 타입(히든)
					cellInfo2 = new dhtmlXGridRowCell();
					rowItem2.getCellElements().add(cellInfo2);
					
					// 관제 항목 부모 PK
					cellInfo2 = new dhtmlXGridRowCell();
					cellInfo2.setValue(item.getRm_target_id());
					rowItem2.getCellElements().add(cellInfo2);
					
					//관제 항목 pk
					cellInfo2 = new dhtmlXGridRowCell();
					cellInfo2.setValue(item2.getRm_item_index());
					rowItem2.getCellElements().add(cellInfo2);
					
					//관제 임계점
					cellInfo2 = new dhtmlXGridRowCell();
					String temp= "%";
					if(item2.getRm_item_unit()!=null&&item2.getRm_item_unit().equals("percent"))
						cellInfo2.setValue(item2.getRm_item_limit()+temp);
					else
						cellInfo2.setValue(item2.getRm_item_limit());
					rowItem2.getCellElements().add(cellInfo2);

					//삭제
					cellInfo2 = new dhtmlXGridRowCell();
					cellInfo2.setValue("<button class='ui_btn_white icon_btn_trash_gray'></button>");
					rowItem2.getCellElements().add(cellInfo2);
					
					cellInfo2 = new dhtmlXGridRowCell();
					cellInfo2.setValue(item2.getRm_use_monitoring_item_id());
					rowItem2.getCellElements().add(cellInfo2);
					
					rowItem.getRowElements().add(rowItem2);
					rowItem2 = null;
				}
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}

		}else{
				xmls = null;
		}
		return xmls;
	}		

	// 관제 UI 관리
	@RequestMapping(value="/UIManage.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml UI_manage(HttpServletRequest request) {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.channel.title.";
			for( int j = 0; j < 13; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("*");

				switch(j) {
				case 0:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue("id");
					column.setHidden("1");
					break;
				case 1:
					column.setHidden("1");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setType("ch");
					column.setSort("na");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				case 2:
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue("관측 순번");
					column.setSort("int");
					break;
				case 3:
					column.setEditable("0");
					column.setWidth("200");
					column.setAlign("left");
					column.setValue("관제 대상 이름");
					break;
				case 4:
					column.setWidth("100");
					column.setValue("종류");
					break;
				case 5:
					column.setEditable("0");
					column.setWidth("200");
					column.setAlign("left");
					column.setValue("관제 대상 설명");
					break;
				case 6:
					column.setWidth("70");
					column.setValue("관측 시간 (초)");
					break;
				case 7:
					column.setWidth("70");
					column.setValue("x축 좌표");
					break;
				case 8:
					column.setWidth("70");
					column.setValue("y축 좌표");
					break;
				case 9:
					column.setWidth("100");
					column.setValue("오브젝트 pk");
					column.setHidden("1");
					break;
				case 10:
					column.setWidth("100");
					column.setValue("오브젝트 경로");
					column.setHidden("1");
					break;
				case 11:
					column.setWidth("100");
					column.setValue("오브젝트 파일명");
					column.setHidden("1");
					break;
				case 12:
					column.setWidth("100");
					column.setValue("오브젝트");
					break;
				
				}

				head.getColumnElement().add(column);
				column = null;
			}

			xmls.setHeadElement(head);

			List<ControlTargetVO> controlTargetResult = monitoringInfoService.selectTargetUIList();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < controlTargetResult.size(); i++) {
				ControlTargetVO targetItem = controlTargetResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_id());
				rowItem.getCellElements().add(cellInfo);
				
				//체크박스
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_ui_order());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_name());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if(targetItem.getRm_target_type()!=null) {
					String val=null;
					switch (targetItem.getRm_target_type()) {
					case "server":
						val="서버";
						break;
					case "work":
						val="업무";
						break;
					default:
						val="기타";
						break;
					}
					cellInfo.setValue(val);
				}
			 	else
			  		cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_descr());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_ui_watch_time());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_ui_position_left());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_ui_position_top());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_target_ui_object_icon());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_icon_path());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(targetItem.getRm_icon_filename());
				rowItem.getCellElements().add(cellInfo);
				
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<div class='select_icon"+targetItem.getRm_target_id()+"' icon='"+targetItem.getRm_icon_filename()+"'"
						+ "style='width:100%;height:70px;background-repeat: no-repeat; background-position: center; background-size: contain; background-image: url("+request.getContextPath()+"/resources/common/recsee/images/project/map-view/"+targetItem.getRm_icon_filename()+");'></div>");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
}
*/