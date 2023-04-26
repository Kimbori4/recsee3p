package com.furence.recsee.audioCalibration.controller;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.facerecording.model.FaceRecordingInfo;
import com.furence.recsee.wooribank.facerecording.service.FaceRecordingService;

@Controller
public class XmlAudioCalibrationController {
	
	@Autowired
	private FaceRecordingService faceRecordingService;

	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value="/audio_calibration_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml audio_calibration_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			for(int j = 0; j < 6; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");
				switch (j) {
				case 0:
					column.setWidth("30");
					column.setType("ch");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
				// 녹취 일자
				case 1:
					column.setWidth("120");
					column.setCache("0");
					column.setId("r_rec_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.label.recDate", null,Locale.getDefault())+"</div>");
					break;
				// 녹취 시간
				case 2:
					column.setWidth("120");
					column.setCache("0");
					column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.rectime", null,Locale.getDefault())+"</div>");
					break;
				// 파일명
				case 3:
					column.setWidth("120");
					column.setCache("0");
					column.setId("r_rec_filename");
					column.setValue("파일명");
					break;
				// 파일 파형
				case 4:
					column.setWidth("*");
					column.setCache("0");
					column.setId("r_rec_wave");
					column.setFiltering("0");
					column.setValue("파형");
					break;
				// 보전 버튼
				case 5:
					column.setWidth("120");
					column.setCache("0");
					column.setId("audio_calibration_btn");
					column.setValue("보정");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			
			FaceRecordingInfo faceRecordingInfo = new FaceRecordingInfo();
			if(!StringUtil.isNull(request.getParameter("sDate"),true)) {
				faceRecordingInfo.setsDate(request.getParameter("sDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eDate"),true)) {
				faceRecordingInfo.seteDate(request.getParameter("eDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("sTime"),true)) {
				faceRecordingInfo.setsTime(request.getParameter("sTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eTime"),true)) {
				faceRecordingInfo.seteTime(request.getParameter("eTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("fileName"),true)) {
				faceRecordingInfo.setFaceRecFileName(request.getParameter("fileName"));
			}
			
			
			List<FaceRecordingInfo> listResult = faceRecordingService.selectFaceRecordingInfo(faceRecordingInfo);
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			for(int i = 0; i < listResult.size()  ; i++) {
				FaceRecordingInfo list = listResult.get(i);
				String tempStrValue = "";
	
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getFaceRecKey());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				// 췤췤 체크박스
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				
				//	녹취 날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getFaceRecDate());
				rowItem.getCellElements().add(cellInfo);
				
				//	녹취 시간2
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getFaceRecTime());
				rowItem.getCellElements().add(cellInfo);
				
				// 파일명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getFaceRecFileName());
				rowItem.getCellElements().add(cellInfo);
	
				// 파형
				cellInfo = new dhtmlXGridRowCell();
				String path = list.getFaceRecFilePath().replaceAll("\\\\", "/");
				cellInfo.setValue("<div style='width:100%; height:100%;' id='"+(String)list.getFaceRecKey()+"' filePath='"+path+"' ></div>");
				rowItem.getCellElements().add(cellInfo);
				
				// 보정 버튼
				tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat' style='height: 35px;' onClick='openAudioCalibrationPop(\""+path+"\")'>" + "<img src='"+request.getContextPath() + "/resources/common/recsee/images/project/icon/calibrationList.png'>" + "</button>";
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);				
	
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		
		return xmls;
	}
}