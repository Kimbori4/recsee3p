package com.furence.recsee.wooribank.facerecording.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.facerecording.model.ScriptRegistrationInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.service.FaceRecordingService;

@Controller
public class XmlFaceRecordingController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private FaceRecordingService faceRecordingService;

	//	조회 및 청취 메모 그리드
	@RequestMapping(value="/scriptGrid2.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml scriptGrid(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		xmls = new dhtmlXGridXml();
		
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SEARCH");
		etcConfigInfo.setConfigKey("company_telno");
		List<EtcConfigInfo> etcConfigResult = null;
		try {
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		} catch(Exception e) {
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("company_telno");
			etcConfigInfo.setConfigValue("N");
			int insertEtcConfigInfoResult = etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);

			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			// e.printStackTrace();
		}
		
//		if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
			// 처음 헤더 xml
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
			column.setId("recDate");
			column.setWidth("100");
			column.setType("tree");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.call.title.date", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			column = new dhtmlXGridHeadColumn();
			column.setId("recTime");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.call.title.time", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;
			
			//	내선
			column = new dhtmlXGridHeadColumn();
			column.setId("recExt");
			column.setWidth("70");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
//			column.setHidden("1");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.user.title.ext", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			// 사번
			column = new dhtmlXGridHeadColumn();
			column.setId("userId");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
//			column.setHidden("1");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("search.option.empId", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			//	상담사명
			column = new dhtmlXGridHeadColumn();
			column.setId("userName");
			column.setWidth("80");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_USER_NAME", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			xmls.setHeadElement(head);

//		}else {
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			SearchListInfo searchListInfo = new SearchListInfo();
			if(!StringUtil.isNull(request.getParameter("date"),true))
				searchListInfo.setRecDate(request.getParameter("date").replaceAll("-",""));
			if(!StringUtil.isNull(request.getParameter("time"),true))
				searchListInfo.setRecTime(request.getParameter("time").replaceAll(":", ""));
			if(!StringUtil.isNull(request.getParameter("ext"), true))
				searchListInfo.setExtNum(request.getParameter("ext"));

			
			
			
//			 recDate
			for(int i = 0; i< 5; i++) {
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				dhtmlXGridRow rowItem2 = new dhtmlXGridRow();
				dhtmlXGridRow rowItem3 = new dhtmlXGridRow();
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
				
				rowItem.setId(String.valueOf(i+3));
				if(i == 0) {
					rowItem.setXmlkids("1");
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+1 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+2 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+3 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+4 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
//					=================================================
					
					rowItem3.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem3.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+100 +" 번");
					rowItem3.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+200 +" 번");
					rowItem3.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+300 +" 번");
					rowItem3.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+400 +" 번");
					rowItem3.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+500 +" 번");
					rowItem3.getCellElements().add(cellInfo);
					cellInfo = null;
					
					
					rowItem2.setRowElements(new ArrayList<dhtmlXGridRow>());
					rowItem2.getRowElements().add(rowItem3);
					
					
					rowItem2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem2.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+10 +" 번");
					rowItem2.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+20 +" 번");
					rowItem2.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+30 +" 번");
					rowItem2.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+40 +" 번");
					rowItem2.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+50 +" 번");
					rowItem2.getCellElements().add(cellInfo);
					cellInfo = null;
					
					
					rowItem.setRowElements(new ArrayList<dhtmlXGridRow>());
					rowItem.getRowElements().add(rowItem2);
					
//					rowItem.setRowElements(rowItem2);
//					xmls.getRowElements().add(rowItem);
//					rowItem.
					
					
				}else {
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+1 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+2 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+3 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+4 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(i+5 +" 번");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					
					
				}
			
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
			

			
			
			
			
			

//			List<RecMemo> resultMemo  = searchListInfoService.selectRecMemo(searchListInfo);

//			for(int i=0; i < resultMemo.size(); i++) {
//				dhtmlXGridRow rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(i+1));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//				rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
//
//				// recDate
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(String.format("%s-%s-%s", resultMemo.get(i).getRecDate().substring(0, 4), resultMemo.get(i).getRecDate().substring(4,  6), resultMemo.get(i).getRecDate().substring(6, 8)));
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//recTime
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(String.format("%s:%s:%s", resultMemo.get(i).getRecTime().substring(0, 2), resultMemo.get(i).getRecTime().substring(2,  4), resultMemo.get(i).getRecTime().substring(4, 6)));
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//ext
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(resultMemo.get(i).getExtNum());
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//userID
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(resultMemo.get(i).getUserId());
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//userName
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(resultMemo.get(i).getUserName());
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//bgname
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(resultMemo.get(i).getBgName());
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//mgname
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(resultMemo.get(i).getMgName());
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//				//sgname
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(resultMemo.get(i).getSgName());
//				rowItem.getCellElements().add(cellInfo);
//				cellInfo = null;
//
//
//				//memo
//				JSONObject jsonObj = new JSONObject();
//				jsonObj.put("memoIdx", resultMemo.get(i).getMemoIdx());
//				jsonObj.put("recDate", resultMemo.get(i).getRecDate());
//				jsonObj.put("recTime", resultMemo.get(i).getRecTime());
//				jsonObj.put("extNum", resultMemo.get(i).getExtNum());
//				jsonObj.put("tag", resultMemo.get(i).getMemo());
//				jsonObj.put("save", "modify");
//				jsonObj.put("userId",resultMemo.get(i).getUserId());
//				jsonObj.put("rowId", String.valueOf(i+1));
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<button class='ui_btn_white ui_main_btn_flat btn_icon_tag_white' style='vertical-align : middle; height:25px;' onClick='tag("+jsonObj+")'></button>");
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//				rowItem = null;
//			}
//		}

		return xmls;
	}
	
	/**
	 * 스크립트 상품안내 윈도우팝업 그리드
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/productInfoScriptGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml productInfoScriptGrid(HttpServletRequest request, HttpServletResponse response) {
		boolean threeDepthFlag = true;
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		xmls = new dhtmlXGridXml();

		// 처음 헤더 xml
		xmls.setHeadElement(new dhtmlXGridHead());
		dhtmlXGridHead head = new dhtmlXGridHead();
		head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
		dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

		column.setId("name");
		column.setWidth("*");
		column.setType("tree");
		column.setAlign("left");
		column.setSort("server");
		column.setValue("단계");
//		column.setValue("<div style='background:#00FF00'>제목</div>");

		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("id");
		column.setWidth("");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setValue("");
		column.setHidden("1");

		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("state");
		column.setWidth("55");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("상태");
//		column.setValue("<div style='background:#00FF00'>상태</div>");

		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("rec");
		column.setWidth("40");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("청취");
//		column.setValue("<div style='background:#00FF00'>청취</div>");

		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("complete");
		column.setWidth("65");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
//		column.setValue("불완전판매");
		column.setValue("분석결과");
//		column.setValue("<div style='background:#00FF00'>비고</div>");
		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("retryRec");
		column.setWidth("60");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
//		column.setValue("불완전판매");
		column.setValue("재녹취");
//		column.setValue("<div style='background:#00FF00'>재녹취</div>");

		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("recCallKey");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");
		column.setValue("콜키");
		head.getColumnElement().add(column);
		column = null;

		column = new dhtmlXGridHeadColumn();
		column.setId("recState2");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");
		
		column = new dhtmlXGridHeadColumn();
		column.setId("moreProductType");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");

		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("taResult");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		
		column = null;
		column = new dhtmlXGridHeadColumn();
		column.setId("taResult");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		//상품번호 idx : 10 
		column = null;
		column = new dhtmlXGridHeadColumn();
		column.setId("rProductListPk");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		//상품번호 idx : 10 
		column = null;
		column = new dhtmlXGridHeadColumn();
		column.setId("rTaReason");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;

		xmls.setHeadElement(head);

		xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
		dhtmlXGridRowCell cellInfo = null;

//		MMenuAccessInfo accessInfo = new MMenuAccessInfo();
//		accessInfo.setLevelCode(userInfo.getUserLevel());
//		accessInfo.setProgramCode("P20001");//TODO
////			
//			

		// 여기수정함
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
		if (!StringUtil.isNull(request.getParameter("rs_script_step_fk"), true))
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rs_script_step_fk")));

		if (!StringUtil.isNull(request.getParameter("callKey"), true))
			scriptRegistrationInfo.setrScriptCallKey(request.getParameter("callKey"));
		
			

		Integer getrProductListPk = scriptRegistrationInfo.getrScriptStepFk();
		
		ProductListVo vo = new ProductListVo();
		if (!StringUtil.isNull(request.getParameter("bizDis"), true))
			vo.setrProductType(request.getParameter("bizDis"));
		if (!StringUtil.isNull(request.getParameter("sysDis"), true))
			vo.setrSysType(request.getParameter("sysDis"));
		if(scriptRegistrationInfo.getrScriptStepFk() != null) {
			vo.setrProductListPk(""+getrProductListPk);
		}
		
		boolean sttTaFlag = true;
		try {
			if (!StringUtil.isNull(request.getParameter("sttTa"), true))
				sttTaFlag = Boolean.parseBoolean(request.getParameter("sttTa"));
		}catch (Exception e) {
			sttTaFlag = true;
		}
		
		List<ScriptRegistrationInfo> buffer = faceRecordingService
				.selectScriptStepHistoryTree(scriptRegistrationInfo);

		for (int i = 0; i < buffer.size(); i++) {
			boolean flag = true;

			ScriptRegistrationInfo item = buffer.get(i);
			dhtmlXGridRow rowItem = new dhtmlXGridRow();
			rowItem.setId(String.valueOf("L1_" + i));
			rowItem.setStyle("");

			String gettOrder = item.gettOrder();
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
			rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
			if (item.gettDepth() == 1) {
				rowItem.setXmlkids("1");

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.gettName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettKey()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				rowItem.setRowElements(new ArrayList<dhtmlXGridRow>());
				for (int j = 1; j < buffer.size(); j++) {
					ScriptRegistrationInfo item2 = buffer.get(j);
					dhtmlXGridRow rowItem2 = new dhtmlXGridRow();
					rowItem2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					if(item2.gettDepth() == 2) {

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item2.gettName());
						rowItem2.getCellElements().add(cellInfo);
						cellInfo = null;

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(String.valueOf(item2.gettKey()));
						rowItem2.getCellElements().add(cellInfo);
						cellInfo = null;
						
						String[] split = item2.gettOrder().split(",");
						if (gettOrder.equals(split[0])) {				//03,3     ,1 -> 03이 맞는지
							rowItem2.setRowElements(new ArrayList<dhtmlXGridRow>());
							for (int k = 2; k < buffer.size(); k++) {
								ScriptRegistrationInfo item3 = buffer.get(k);
								Integer item3Depth = item3.gettDepth();
								if(item3.gettDepth() == 3) {
									String[] split2 = item3.gettOrder().split(",");
									
									if(gettOrder.equals(split2[0]) && split[1].equals(split2[1])) { 		//3뎁스 조건만족
										rowItem.setStyle("color: #060606;background: rgb(255 255 140);font-weight: bold; 2px solid #488939;");
										threeDepthFlag = true;
										dhtmlXGridRow rowItem3 = new dhtmlXGridRow();
										rowItem3.setCellElements(new ArrayList<dhtmlXGridRowCell>());
										rowItem3.setId(String.valueOf("L3_" + k));
										
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(item3.gettName());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(String.valueOf(item3.gettKey()));
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										cellInfo = new dhtmlXGridRowCell();
										if ("Y".equals(item3.getrScriptRecState())) {
											cellInfo.setValue(
													"<div id='state'  style=\"cursor: pointer; position: relative; top: 2px\">녹취완료 </div>");
										} else if ("N".equals(item3.getrScriptRecState())) {
											cellInfo.setValue(
													"<div id='state'  style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
															+ request.getContextPath()
															+ "/resources/common/recsee/images/project/icon/loading.png' /></div>");
										}
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(
												"<div id='recPlay' onClick='recFilePlay(this)' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
														+ request.getContextPath()
														+ "/resources/common/recsee/images/project/icon/speakerBlack.png' style='width:16px;' /></div>");
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										// 불완전판매
										cellInfo = new dhtmlXGridRowCell();
										if(sttTaFlag) {
											cellInfo.setValue(caseTaResult(item3.getrScriptTaState(),request.getContextPath(), String.valueOf("L3_" + k)));
										}else {
											cellInfo.setValue("");
										}
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;

										// 재녹취
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue("<button class='ui_btn_white' id='retryRecFile' onClick='retryRecFile(\""
												+ String.valueOf("L3_" + k)
												+ "\");' style='font-size:12px !important; width: 40px!important ;padding: 2px 3px !important;'>재녹취</button>");
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;

										// 콜키
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(item3.getrSccriptStepCallKey());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;

										// 상태값
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(item3.getrScriptRecState());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										// 상태값
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(""+item3.getMoreProductType());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										// TA결과
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(item3.getrScriptTaState());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										// TA결과
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(""+item3.getrProductListPk());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										// TA사유
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(""+item3.getTaReason());
										rowItem3.getCellElements().add(cellInfo);
										cellInfo = null;
										
										
										rowItem2.getRowElements().add(rowItem3);
										rowItem3 = null;

										
									}
								}
								
								
							}
							
							flag = false;
							rowItem2.setId(String.valueOf("L2_" + j));
							if(rowItem2.getRowElements().size() == 0 ) {
								// 상태값
								cellInfo = new dhtmlXGridRowCell();
								if ("Y".equals(item2.getrScriptRecState())) {
									cellInfo.setValue(
											"<div id='state'  style=\"cursor: pointer; position: relative; top: 2px\">녹취완료 </div>");
								} else if ("N".equals(item2.getrScriptRecState())) {
									cellInfo.setValue(
											"<div id='state'  style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
													+ request.getContextPath()
													+ "/resources/common/recsee/images/project/icon/loading.png' /></div>");
								}
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
	
								// 청취
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(
										"<div id='recPlay' onClick='recFilePlay(this)' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
												+ request.getContextPath()
												+ "/resources/common/recsee/images/project/icon/speakerBlack.png' style='width:16px;' /></div>");
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
	
								// 불완전판매
								cellInfo = new dhtmlXGridRowCell();
								if(sttTaFlag) {
									cellInfo.setValue(caseTaResult(item2.getrScriptTaState(),request.getContextPath(), String.valueOf("L2_" + j)));
								}else {
									cellInfo.setValue("");
								}
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
	
								// 재녹취
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue("<button class='ui_btn_white' id='retryRecFile' onClick='retryRecFile(\""
										+ String.valueOf("L2_" + j)
										+ "\");' style='font-size:12px !important; width: 40px!important ;padding: 2px 3px !important;'>재녹취</button>");
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
	
								// 콜키
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(item2.getrSccriptStepCallKey());
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
	
								// 상태값
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(item2.getrScriptRecState());
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(""+item2.getMoreProductType());
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
								
								// TA결과
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(item2.getrScriptTaState());
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
								
								// productListPk
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(""+item2.getrProductListPk());
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
								
								// taReason
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(""+item2.getTaReason());
								rowItem2.getCellElements().add(cellInfo);
								cellInfo = null;
								
							}

//                          

							rowItem.getRowElements().add(rowItem2);
							rowItem2 = null;
							threeDepthFlag = true;

						}
					}
				}
				if (flag) {
					// 상태값
					cellInfo = new dhtmlXGridRowCell();
					if ("Y".equals(item.getrScriptRecState())) {
						cellInfo.setValue(
								"<div id='state'  style=\"cursor: pointer; position: relative; top: 2px\">녹취완료 </div>");
					} else if ("N".equals(item.getrScriptRecState())) {
						cellInfo.setValue(
								"<div id='state'  style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
										+ request.getContextPath()
										+ "/resources/common/recsee/images/project/icon/loading.png' /></div>");
					}
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;

					// 청취
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(
							"<div id='recPlay' onClick='recFilePlay(this)' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
									+ request.getContextPath()
									+ "/resources/common/recsee/images/project/icon/speakerBlack.png' style='width:16px;' /></div>");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;

					// 불완전판매
					cellInfo = new dhtmlXGridRowCell();
					if(sttTaFlag) {
						cellInfo.setValue(caseTaResult(item.getrScriptTaState(),request.getContextPath(), String.valueOf(("L1_" + i))));
					}else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;

					// 재녹취
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class='ui_btn_white' onClick='retryRecFile(\""
							+ String.valueOf("L1_" + i)
							+ "\");' style='font-size:12px !important;width: 40px!important ;padding: 2px 3px !important;'>재녹취</button>");
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;

					// 콜키
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(item.getrSccriptStepCallKey());
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;

					// 상태값
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(item.getrScriptRecState());
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(""+item.getMoreProductType());
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					
					// TA결과
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(item.getrScriptTaState());
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					// ProductListPk
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(""+item.getrProductListPk());
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					
					// ProductListPk
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(""+item.getTaReason());
					rowItem.getCellElements().add(cellInfo);
					cellInfo = null;
					

				}

//            		if()

				xmls.getRowElements().add(rowItem);
				rowItem = null;

			}
		}
		return xmls;

	}
	
	
	private String caseTaResult(String taResult , String path , String colId) {
		String color = "Gray";
		String img = "";
		
		switch (taResult) {
		case "Y":
			color = "Blue";
			img = "<div id='recPlay' onClick='' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
			+ path
			+ "/resources/common/recsee/images/project/icon/Circle"+color+".png' style='width:16px;' /></div>";
			break;
		case "N":
			color = "Yellow";
			img = "<div id='"+colId+"' onClick='taErrorPopUp(this)' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
					+ path
					+ "/resources/common/recsee/images/project/icon/Circle"+color+".png' style='width:16px;' /></div>";
			break;
		default:
			color = "Gray";
			img = "<div id='recPlay' onClick='' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
					+ path
					+ "/resources/common/recsee/images/project/icon/Circle"+color+".png' style='width:16px;' /></div>";
			break;
		}
		return img;
	}
}
