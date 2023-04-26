package com.furence.recsee.scriptRegistration.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.service.ScriptRegistrationService;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;


@Controller
public class XmlScriptRegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(XmlScriptRegistrationController.class);
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ScriptRegistrationService scriptRegistrationService;

	@Autowired
	private LogInfoService logInfoService;

	// 스크립트 목록
	@RequestMapping(value = "/scriptGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml scriptGrid(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		// 미로그인 리턴
		if (userInfo == null) {
			logInfoService.writeLog(request, "Etc - Logout");
			return xmls;
		}

		xmls = new dhtmlXGridXml();

		// Grid Header 만드는 부분
		xmls.setHeadElement(new dhtmlXGridHead());
		dhtmlXGridHead head = new dhtmlXGridHead();
		head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

		for (int j = 0; j < 6; j++) {
			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
			column.setWidth("*");
			column.setAlign("left");
			column.setType("ro");
			column.setSort("server");
			column.setHidden("1");

			switch (j) {
			case 0:
				column.setType("tree");
				column.setWidth("270");
				column.setId("scriptName");
				column.setHidden("0");
				break;
			case 1:
				column.setId("rScriptStepPk");
				break;
			case 2:
				column.setId("rScriptStepParent");
				break;
			case 3:
				column.setId("rScriptStepFk");
				break;
			case 4:
				column.setId("changeScriptName");
				break;
			case 5:
				column.setId("addScriptStepRowLank");
				column.setWidth("120");
				column.setHidden("0");
				break;


			}
			head.getColumnElement().add(column);
		}
		
		xmls.setHeadElement(head);
		
		// Grid Body 만드는 부분
		xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
		dhtmlXGridRowCell cellInfo = null;

		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
		if (!StringUtil.isNull(request.getParameter("rs_script_step_fk"), true)) {
			scriptRegistrationInfo.setRsScriptStepFk(Integer.parseInt(request.getParameter("rs_script_step_fk")));
		}
		if (!StringUtil.isNull(request.getParameter("rs_script_step_parent"), true)) {
			scriptRegistrationInfo
					.setrScriptStepParent(Integer.parseInt(request.getParameter("rs_script_step_parent")));
		}
		if (!StringUtil.isNull(request.getParameter("rs_script_step_Pk"), true)) {
			scriptRegistrationInfo.setrScriptStepPk((request.getParameter("rs_script_step_Pk")));
		}
		if (!StringUtil.isNull(request.getParameter("t_key"), true)) {
			scriptRegistrationInfo.settKey(Integer.parseInt(request.getParameter("t_key")));
		}

		// 11 20 장진호 매니저 xml 수정
		Integer pk = scriptRegistrationService.selectProductListGroup(scriptRegistrationInfo);
		if (pk == null) {

		} else {
			scriptRegistrationInfo.setRsScriptStepFk(pk);
		}
		List<ScriptRegistrationInfo> buffer = scriptRegistrationService.selectScriptStepList(scriptRegistrationInfo);

		for (int i = 0; i < buffer.size(); i++) {
			ScriptRegistrationInfo item = buffer.get(i);
			dhtmlXGridRow rowItem = new dhtmlXGridRow();

			rowItem.setId((i + 1) + "");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
			rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
			if (item.gettDepth() == 1) {
				rowItem.setXmlkids("1");
				// 0 ScriptStepText
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.gettName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 1 ScriptStepPK
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettKey()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 2 ScriptStepParent
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettParent()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 3 ScriptStepFk
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.getRsScriptStepFk()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 4 ScriptStepText
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettName()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;
				
				// 5 addScriptStepRowLank
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(
						"<div class='stepDivWrap'>"
					+"<div id='sc_gird_step_one_de"+i+"' class='scGirdStepOneDe stepAddDeleteBtn' style='visibility: hidden'>"
	                +"<div id='sc_gird_add"+i+"' class='scGirdAdd' onClick='addRowRank("+(i + 1)+")' style='display : inline-block ;margin-right : 10px; cursor: pointer; width : 20px; text-align :center;  position: relative; top: 5px'>"
	                        +"<img id src='"+ request.getContextPath()
	                        + "/resources/common/recsee/images/project/icon/plus.png ' style='width:20px; margin : auto;'/></div>"
	                +"<div id='sc_gird_delete"+i+"' class='scGirdDelete' onClick='deleteRowRank("+(i + 1)+")' style='cursor: pointer; display : inline-block ;margin-right : 10px;text-align :center; width : 20px; position: relative; top: 5px'>"
	                        +"<img id src='"+ request.getContextPath()
	                        + "/resources/common/recsee/images/project/icon/wooribank/ic_trash.svg ' style='width:20px; margin : auto;'/></div>"
	                +"</div>"
	                +"<div class='showIfEdited' style='display:none; color:red;'>[수정]</div></div>");
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				String gettOrder = item.gettOrder();
				rowItem.setRowElements(new ArrayList<dhtmlXGridRow>());
				for (int j = 1; j < buffer.size(); j++) {
					ScriptRegistrationInfo item2 = buffer.get(j);
					dhtmlXGridRow rowItem2 = new dhtmlXGridRow();
					rowItem2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					dhtmlXGridRow rowItem3 = new dhtmlXGridRow();
					rowItem3.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					if (item2.gettOrder().split(",").length == 2) {
						String[] split = item2.gettOrder().split(",");
						if (gettOrder.equals(split[0])) {
							rowItem2.setId(String.valueOf(item2.gettKey() + 100));

							// 0 ScriptStepText
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(item2.gettName());
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 1 ScriptStepPK
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.gettKey()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 2 ScriptStepParent
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.gettParent()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;


							// 3 ScriptStepFk
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.getRsScriptStepFk()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 4 ScriptStepText
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.gettName()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;
							
							
							// 5 addScriptStepRowLank
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(
									"<div class='stepDivWrap'>"
								+"<div class='placeholderDiv' style='width:30px'></div>"
								+"<div id='sc_gird_step_two_de"+j+"' class ='scGirdStepTwoDe stepAddDeleteBtn' style='visibility: hidden'>"
                                +"<div id='sc_gird_delete_rowLank"+j+"' class='scGirddeleteRowLank' onClick='deleteRowRank("+String.valueOf(item2.gettKey() + 100)+")' style='cursor: pointer; width : 20px;text-align :center; display : inline-block ;margin-right : 10px; position: relative; top: 5px\'>"
                                         +"<img id src='"+ request.getContextPath()
                                        + "/resources/common/recsee/images/project/icon/wooribank/ic_trash.svg ' style='width:20px; margin : auto;'/></div>"
                                +"</div>"
                                +"<div class='showIfEdited' style='display:none; color:red;'>[수정]</div></div>");
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							rowItem.getRowElements().add(rowItem2);
							rowItem2 = null;
						}
					}
				}

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}

	

	// 스크립트 검색
	@RequestMapping(value = "/scriptSearchGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml scriptSearchGrid(HttpServletRequest request) throws UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		String header = "";
		if (!StringUtil.isNull(request.getParameter("header"))) {
			header = request.getParameter("header");
		}

		// 미로그인 리턴
		if (userInfo == null) {
			logInfoService.writeLog(request, "Etc - Logout");
			xmls = null;
			return xmls;
		}

		if (header != null && "true".equals(header)) {
			// Grid Header 만드는 부분
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 16; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");

				switch (j) {
				case 0:
					column.setWidth("40");
					column.setType("ch");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"
									+ request.getContextPath()
									+ "/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
				case 1:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsScriptStepFk");
					break;
				case 2:
					column.setWidth("40");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("번호");
					break;
				case 3:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setAlign("left");
					column.setId("rsScriptName");
					column.setValue("<div style=\"text-align:center;\">" + "상품명" + "</div>");
					break;
				case 4:
					column.setWidth("80");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptType");
					column.setValue("<div style=\"text-align:center;\">" + "상품유형" + "</div>");
					break;
				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsProductCode");
					column.setValue("<div style=\"text-align:center;\">" + "상품코드" + "</div>");
					break;
				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsProductCode");
					column.setValue("<div style=\"text-align:center;\">" + "스크립트유무" + "</div>");
					break;	
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "그룹유무" + "</div>");
					break;
				case 8:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "TTS 적용일" + "</div>");
					break;
				case 9:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "예약 적용일" + "</div>");
					break;
				case 10:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "확인 유무" + "</div>");
					break;
				case 11:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "최종 수정일" + "</div>");
					break;
				case 12:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "최종 수정자" + "</div>");
					break;
				case 13:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "확인자" + "</div>");
					break;
				case 14:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue(
							"<div class=\"ScriptChangeCaree;\" style=\"text-align:center;\">" + "변경이력보기" + "</div>");
					break;
				case 15:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "FK" + "</div>");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			
			xmls.setHeadElement(head);
		} else {
			// Grid Body 만드는 부분
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			// Get Parameter
			if (!StringUtil.isNull(request.getParameter("keyword"), true)) {
				scriptRegistrationInfo.setrSearchWord(request.getParameter("keyword"));
			}
			
			if (!StringUtil.isNull(request.getParameter("action"), true)) {
				scriptRegistrationInfo.setrSearchType(request.getParameter("action"));
			}
			
			if (!StringUtil.isNull(request.getParameter("productType"), true)) {
				scriptRegistrationInfo.setrProductType(request.getParameter("productType"));
			}
			
			if (!StringUtil.isNull(request.getParameter("orderBy"), true)) {
				scriptRegistrationInfo.setOrderBy(request.getParameter("orderBy"));
			}
			
			if (!StringUtil.isNull(request.getParameter("direction"), true)) {
				scriptRegistrationInfo.setDirection(request.getParameter("direction"));
			}

			int offset = !StringUtil.isNull(request.getParameter("posStart"), true) ? Integer.parseInt(request.getParameter("posStart")) : 0;
			scriptRegistrationInfo.setOffset(offset);

			int limit = !StringUtil.isNull(request.getParameter("count"), true) ? Integer.parseInt(request.getParameter("count")) : 100;
			scriptRegistrationInfo.setLimit( Math.max(limit,1) );
			
			List<ScriptRegistrationInfo> selectProductList = scriptRegistrationService.selectProductList(scriptRegistrationInfo);

			if (selectProductList != null && selectProductList.size() > 0) {
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for (int i = 0; i < selectProductList.size(); i++) {
					
					ScriptRegistrationInfo list = selectProductList.get(i);
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					rowItem.setRowClass("searchResultScript");
					rowItem.setId("" + list.getrProductListPk());

					// ch
					cellInfo = new dhtmlXGridRowCell();
					rowItem.setId("Ch");
					rowItem.getCellElements().add(cellInfo);

					// pk
					cellInfo = new dhtmlXGridRowCell();
					rowItem.setId("Pk");
					cellInfo.setValue("" + list.getrProductListPk());
					rowItem.getCellElements().add(cellInfo);

					// 번호
					cellInfo = new dhtmlXGridRowCell();
					rowItem.setId("num");
					cellInfo.setValue(list.getRowNumber() + "");
					rowItem.getCellElements().add(cellInfo);

					// 상품명
					cellInfo = new dhtmlXGridRowCell();
					rowItem.setId("productName");
					cellInfo.setStyle("text-align:left");
					cellInfo.setValue(list.getrProductName());
					rowItem.getCellElements().add(cellInfo);

					// 상품유형
					cellInfo = new dhtmlXGridRowCell();
					rowItem.setId("productType");
					if ("1".equals(list.getrProductType())) {
						cellInfo.setValue("신탁");
					} else if ("2".equals(list.getrProductType())) {
						cellInfo.setValue("펀드");
					}
					rowItem.getCellElements().add(cellInfo);

					// 상품코드
					cellInfo = new dhtmlXGridRowCell();
					rowItem.setId("productCode");
					cellInfo.setValue(list.getrProductCode());
					rowItem.getCellElements().add(cellInfo);
					
					// 스크립트유무
					boolean registered = list.getRegisteredYN().equals("Y");
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue( registered ? "-" : "미등록" );
					if(!registered ) {
						cellInfo.setStyle( "color:red");
					}
					rowItem.setId("scriptYN");
					rowItem.getCellElements().add(cellInfo);

					// 그룹유무
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(list.getrProductGroupYn());
					rowItem.setId("groupYN");
					rowItem.getCellElements().add(cellInfo);

					// TTS 적용일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("-");
					rowItem.setId("TTSdate");
					rowItem.getCellElements().add(cellInfo);

					// 예약적용일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("-");
					rowItem.setId("reserasionDate");
					rowItem.getCellElements().add(cellInfo);

					// 확인 유무
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("Y");
					rowItem.getCellElements().add(cellInfo);

					// 최종 수정일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("-");
					rowItem.setId("modifyDate");
					rowItem.getCellElements().add(cellInfo);

					// 최종 수정자
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("admin");
					rowItem.setId("editor");
					rowItem.getCellElements().add(cellInfo);

					// 확인자
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("s");
					rowItem.setId("confirmor");
					rowItem.getCellElements().add(cellInfo);

					// 변경이력보기
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button id='changeRecordBtn' onClick='createChangeRecord("+(i + 1)+")' style=' cursor: pointer; width : 20px; text-align :center;  position: relative; top: 2px'>조회</button>");
					rowItem.setId("changeRecord");
					rowItem.getCellElements().add(cellInfo);
					
					// 스크립트 조회용 키
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(list.getrScriptStepFk()+"");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
			// 전체 카운트
			Integer totalCount = scriptRegistrationService.selectProductListCount(scriptRegistrationInfo);
			xmls.setTotal_count( totalCount != null ? totalCount.toString() : "0");
			xmls.setPos(request.getParameter("posStart") != null ? request.getParameter("posStart") : "0");
		}
		
		return xmls;
	}

	// 변경이력 검색
	@RequestMapping(value = "/PopUPcareerGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml popUPcareerGrid(HttpServletRequest request) throws UnsupportedEncodingException {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 5; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
//					column.setHidden("0");

				switch (j) {
				case 0:
					column.setWidth("30");
					column.setType("ch");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setValue("no");
					break;

				case 1:
					column.setWidth("100");
					column.setCache("0");
//						column.setId("r_rec_date");
					column.setFiltering("0");
					// column.setValue("2번");
					column.setValue("<div style=\"text-align:center;\">" + "내용" + "</div>");
					break;

				case 2:
					column.setWidth("80");
					column.setCache("0");
//						column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "변경날짜" + "</div>");
					break;

				case 3:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "변경 담당자" + "</div>");
					break;

				case 4:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "최종 결재자" + "</div>");
					break;

				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

////				//	상품 검색 그리드 표출
//				ScriptRegistrationInfo ScriptRegistrationInfo = new ScriptRegistrationInfo();
			//
//				ScriptRegistrationInfo.setrUseYn(userInfo.getUserId());
//				ScriptRegistrationInfo.setrFolderName(URLDecoder.decode(request.getParameter("folderName"),"utf-8"));
			//
//				// Postgres 암호화 사용여부
//				String postgresColumn = "";
//				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//				etcConfigInfo.setGroupKey("ENCRYPT");
//				etcConfigInfo.setConfigKey("POSTGRES");
//				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//				if(etcConfigResult.size() > 0) {
//					if("Y".equals(etcConfigResult.get(0).getConfigValue())){
//						etcConfigInfo.setGroupKey("ENCRYPT");
//						etcConfigInfo.setConfigKey("COLUMN");
//						
//						etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//						
//						postgresColumn = etcConfigResult.get(0).getConfigValue();
//					}
//				}else {
//					etcConfigInfo.setConfigValue("N");
//					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);				
//					etcConfigInfo.setGroupKey("ENCRYPT");
//					etcConfigInfo.setConfigKey("COLUMN");
//					etcConfigInfo.setConfigValue("N");				
//					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);				
//				}
//				
//				if(!"".equals(postgresColumn)) {
//					if(postgresColumn.contains("r_cust_phone1")) {
//						myfolderListinfo.setCustPhone1IsEncrypt("Y");
//					}
//					
//					if(postgresColumn.contains("r_cust_name")) {
//						myfolderListinfo.setCustNameIsEncrypt("Y");
//					}
//				}

//				
//				// 스크립트 검색 
//				ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
//				  List<ScriptRegistrationInfo> scriptDetailList = scriptRegistrationService.selectProductList(scriptRegistrationInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for (int i = 0; i < 5; i++) {

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
//					rowItem.setId(list.getrItemSerial());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// no
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
//					rowItem.getCellElements().add(cellInfo);

				// 내용
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(i + 1 + " 번");
//					cellInfo.setValue(list.getrProductName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 변경날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("2021.10.22");
//					cellInfo.setValue(list.getrProductType());
				rowItem.getCellElements().add(cellInfo);

				// 변경 담당자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("관리자");
//					cellInfo.setValue(list.getrProductCode());
				rowItem.getCellElements().add(cellInfo);

				// 최종 결재자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("관리자");
//					cellInfo.setValue(list.getrProductCode());
				rowItem.getCellElements().add(cellInfo);

//					tempStrValue=new RecSeeUtil().makingName(tempStrValue);

//					cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);

//					cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;

			}

		}

		return xmls;
	}
	
	/* 공용 스크립트 팝업 그리드 생성 */
	@RequestMapping(value = "/scriptCommonPopupGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml scriptCommonPopupGrid(HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		if (userInfo != null) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			for (int j = 0; j < 9; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");

				switch (j) {

				case 0:
					column.setWidth("150");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("이름");
					break;

				case 1:
					column.setWidth("200");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("설명");
					break;

				case 2:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("유형");
					break;
					
				case 3:
					column.setWidth("200");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("내용");
					break;
					
				case 4:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("최종수정일");
					break;
					
				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("변경예정일");
					break;
					
				case 6:
					column.setWidth("80");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("최종수정자");
					break;
					
				case 7:
					column.setWidth("80");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("확인자");
					break;
					
				case 8:
					column.setHidden("1");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("detailType");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			
			
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			if (!StringUtil.isNull(request.getParameter("keyword"), true)) {
				scriptRegistrationInfo.setrSearchWord(request.getParameter("keyword"));
			}
			if (!StringUtil.isNull(request.getParameter("action"), true)) {
				scriptRegistrationInfo.setrSearchType(request.getParameter("action"));
			}
			if (!StringUtil.isNull(request.getParameter("rsScriptCommonPk"), true)) {
				scriptRegistrationInfo.setrScriptCommonPk((request.getParameter("rsScriptCommonPk")));
			}
			if (!StringUtil.isNull(request.getParameter("scriptType"), true)) {
				scriptRegistrationInfo.setScriptType(request.getParameter("scriptType"));
			}	
			
			List<ScriptRegistrationInfo> searchCommonScriptDetail = null;
			logger.info("request.getParameter(\"flag\")="+request.getParameter("flag"));
			if ("2".equals(request.getParameter("flag"))) {
				searchCommonScriptDetail = scriptRegistrationService.searchCommonScriptDetail(scriptRegistrationInfo);
			}
			
			
			// head 끝
			if (searchCommonScriptDetail != null && searchCommonScriptDetail.size() > 0) {
				// row 시작
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for (int i = 0; i < searchCommonScriptDetail.size(); i++) {
					ScriptRegistrationInfo item = searchCommonScriptDetail.get(i);
					if(item.getRsScriptCommonCreateDate() == null) {
						continue;
					}
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();

					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					
					rowItem.setRowClass("searchCommonScript");
					rowItem.setId("" + item.getRsScriptCommonPk());

					// name(이름)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue("<div style=\"text-align:left;padding:5px;\">"+item.getRsScriptCommonName()+"</div>");
					rowItem.getCellElements().add(cellInfo);

					// desc(설명)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue(item.getRsScriptCommonDesc());
					cellInfo.setValue("<div style=\"text-align:left;padding:5px;\">"+item.getRsScriptCommonDesc()+"</div>");
					rowItem.getCellElements().add(cellInfo);

					// type(유형)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					if ("T".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>TTS 리딩</div>");
					} else if ("S".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>직원직접리딩</div>");
					}else if ("G".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>고객 답변</div>");
					}else if ("R".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>적합성보고서</div>");
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 내용
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue("<div style=\"text-align:left;padding:5px;\">"+item.getRsScriptCommonText()+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 최종수정일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					String updateDate = item.getRsScriptCommonUpdateDate() == null? sdf.format(item.getRsScriptCommonCreateDate()) : sdf.format(item.getRsScriptCommonUpdateDate());
					cellInfo.setValue("<div>"+updateDate+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 예약적용일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					String reservedDate = item.getRsScriptCommonReservDate() == null? "-" : sdf.format(item.getRsScriptCommonReservDate());
					cellInfo.setValue("<div>"+reservedDate+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 최종수정자
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					String updateUser = item.getRsScriptCommonUpdateUser() == null? item.getRsScriptCommonCreateUser() : item.getRsScriptCommonUpdateUser();
					cellInfo.setValue("<div>"+updateUser+"</div>");
					rowItem.getCellElements().add(cellInfo);

					// 확인자
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue("<div>"+item.getRsScriptCommonConfirmUser()+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// detailType (code)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(item.getRsScriptCommonType());
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
		}
		return xmls;
	}
	
	/* 공용 스크립트 페이지 그리드 생성 */
	@RequestMapping(value = "/scriptCommonMainGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml scriptCommonMainGrid(HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		if (userInfo != null) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			for (int j = 0; j < 9; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");

				switch (j) {

				case 0:
					column.setWidth("150");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("이름");
					break;

				case 1:
					column.setWidth("200");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("설명");
					break;

				case 2:
					column.setWidth("150");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("유형");
					break;
					
				case 3:
					column.setWidth("*");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("내용");
					break;
					
				case 4:
					column.setWidth("150");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("최종수정일");
					break;
					
				case 5:
					column.setWidth("150");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("변경예정일");
					break;
					
				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("최종수정자");
					break;
					
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("결재자");
					break;
					
				case 8:
					column.setHidden("1");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("detailType");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			
			
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			if (!StringUtil.isNull(request.getParameter("keyword"), true)) {
				scriptRegistrationInfo.setrSearchWord(request.getParameter("keyword"));
			}
			if (!StringUtil.isNull(request.getParameter("action"), true)) {
				scriptRegistrationInfo.setrSearchType(request.getParameter("action"));
			}
			if (!StringUtil.isNull(request.getParameter("rsScriptCommonPk"), true)) {
				scriptRegistrationInfo.setrScriptCommonPk((request.getParameter("rsScriptCommonPk")));
			}
			if (!StringUtil.isNull(request.getParameter("scriptType"), true)) {
				scriptRegistrationInfo.setScriptType(request.getParameter("scriptType"));
			}	
			
			List<ScriptRegistrationInfo> searchCommonScriptDetail = null;
			logger.info("request.getParameter(\"flag\")="+request.getParameter("flag"));
			if ("2".equals(request.getParameter("flag"))) {
				searchCommonScriptDetail = scriptRegistrationService.searchCommonScriptDetail(scriptRegistrationInfo);
			}
			
			
			// head 끝
			if (searchCommonScriptDetail != null && searchCommonScriptDetail.size() > 0) {
				// row 시작
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for (int i = 0; i < searchCommonScriptDetail.size(); i++) {
					ScriptRegistrationInfo item = searchCommonScriptDetail.get(i);
					if(item.getRsScriptCommonCreateDate() == null) {
						continue;
					}
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();

					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					
					rowItem.setRowClass("searchCommonScript");
					rowItem.setId("" + item.getRsScriptCommonPk());

					// name(이름)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue("<div style=\"text-align:left;padding:5px;\">"+item.getRsScriptCommonName()+"</div>");
					rowItem.getCellElements().add(cellInfo);

					// desc(설명)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue(item.getRsScriptCommonDesc());
					cellInfo.setValue("<div style=\"text-align:left;padding:5px;\">"+item.getRsScriptCommonDesc()+"</div>");
					rowItem.getCellElements().add(cellInfo);

					// type(유형)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					if ("T".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>TTS 리딩</div>");
					} else if ("S".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>직원직접리딩</div>");
					}else if ("G".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>고객 답변</div>");
					}else if ("R".equals(item.getRsScriptCommonType())) {
						cellInfo.setValue("<div>적합성보고서</div>");
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 내용
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue("<div style=\"text-align:left;padding:5px;\">"+item.getRsScriptCommonText()+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 최종수정일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					String updateDate = item.getRsScriptCommonUpdateDate() == null? sdf.format(item.getRsScriptCommonCreateDate()) : sdf.format(item.getRsScriptCommonUpdateDate());
					cellInfo.setValue("<div>"+updateDate+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 예약적용일
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					String reservedDate = item.getRsScriptCommonReservDate() == null? "-" : sdf.format(item.getRsScriptCommonReservDate());
					cellInfo.setValue("<div>"+reservedDate+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// 최종수정자
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					String updateUser = item.getRsScriptCommonUpdateUser() == null? item.getRsScriptCommonCreateUser() : item.getRsScriptCommonUpdateUser();
					cellInfo.setValue("<div>"+updateUser+"</div>");
					rowItem.getCellElements().add(cellInfo);

					// 확인자
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setCellClass("searchCommonScript");
					cellInfo.setValue("<div>"+item.getRsScriptCommonConfirmUser()+"</div>");
					rowItem.getCellElements().add(cellInfo);
					
					// detailType (code)
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(item.getRsScriptCommonType());
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
		}
		return xmls;
	}

	@RequestMapping(value = "/popUpScriptGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml popUpScriptGrid(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		dhtmlXGridXml xmls = null;

		xmls = new dhtmlXGridXml();

		// 처음 헤더 xml
		xmls.setHeadElement(new dhtmlXGridHead());
		dhtmlXGridHead head = new dhtmlXGridHead();
		head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

		dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
		// 0
		column.setId("name");
		column.setWidth("*");
		column.setType("tree");
		column.setAlign("left");
		column.setSort("server");
		column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.call.title.date", null,Locale.getDefault())+"</div>");
//				column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.call.title.date", null,Locale.getDefault())+"</div>");
		head.getColumnElement().add(column);
		column = null;

		// 1
		column = new dhtmlXGridHeadColumn();
		column.setId("id");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setHidden("1");
		head.getColumnElement().add(column);
		column = null;

		// 2
		column = new dhtmlXGridHeadColumn();
		column.setId("id");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setHidden("1");
		head.getColumnElement().add(column);
		column = null;

		// 3
		column = new dhtmlXGridHeadColumn();
		column.setId("id444");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setHidden("1");
		head.getColumnElement().add(column);
		column = null;

		// 4
		column = new dhtmlXGridHeadColumn();
		column.setId("id5");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setHidden("1");
		head.getColumnElement().add(column);
		column = null;

		// 5
		column = new dhtmlXGridHeadColumn();
		column.setId("id6");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setHidden("1");
		head.getColumnElement().add(column);
		column = null;

		// 6
		column = new dhtmlXGridHeadColumn();
		column.setId("name");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setHidden("1");
		head.getColumnElement().add(column);
		column = null;

		xmls.setHeadElement(head);

		xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
		dhtmlXGridRowCell cellInfo = null;

		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		if (!StringUtil.isNull(request.getParameter("rs_script_step_fk"), true)) {
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rs_script_step_fk")));
			scriptRegistrationInfo.setRsScriptStepFk(Integer.parseInt(request.getParameter("rs_script_step_fk")));
		}
		if (!StringUtil.isNull(request.getParameter("rs_script_step_parent"), true)) {
			scriptRegistrationInfo
					.setrScriptStepParent(Integer.parseInt(request.getParameter("rs_script_step_parent")));
		}
		if (!StringUtil.isNull(request.getParameter("rs_script_step_Pk"), true)) {
			scriptRegistrationInfo.setrScriptStepPk((request.getParameter("rs_script_step_Pk")));
		}
		if (!StringUtil.isNull(request.getParameter("t_key"), true)) {
			scriptRegistrationInfo.settKey(Integer.parseInt(request.getParameter("t_key")));
		}

		Integer pk = scriptRegistrationService.selectProductListGroup(scriptRegistrationInfo);
		if (pk == null) {

		} else {
			scriptRegistrationInfo.setRsScriptStepFk(pk);

		}
		List<ScriptRegistrationInfo> buffer = scriptRegistrationService.selectScriptStepList(scriptRegistrationInfo);

		logger.info("buffer size: "+ buffer.size());

		for (int i = 0; i < buffer.size(); i++) {
			ScriptRegistrationInfo item = buffer.get(i);
			dhtmlXGridRow rowItem = new dhtmlXGridRow();

			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
			rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
			if (item.gettDepth() == 1) {
				rowItem.setXmlkids("1");

				// 0
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.gettName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 1pk 무시하셔도됩니다.
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettKey()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 2
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettParent()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 3
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.gettKey()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;
				item.gettKey();

				// 4
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.getRsScriptStepFk()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 5
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(item.getrScriptStepPk()));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// 06
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.gettName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				String gettOrder = item.gettOrder();
				rowItem.setRowElements(new ArrayList<dhtmlXGridRow>());

				for (int j = 1; j < buffer.size(); j++) {
					ScriptRegistrationInfo item2 = buffer.get(j);
					dhtmlXGridRow rowItem2 = new dhtmlXGridRow();
					rowItem2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					dhtmlXGridRow rowItem3 = new dhtmlXGridRow();
					rowItem3.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					if (item2.gettOrder().split(",").length == 2) {
						String[] split = item2.gettOrder().split(",");
						if (gettOrder.equals(split[0])) {
							rowItem2.setId(String.valueOf(item2.gettKey() + 100));

							// 0
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(item2.gettName());
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 1
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
							cellInfo.setValue(String.valueOf(item2.gettKey()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;
////	                          
							// 2 parentCode 뽑아뢋
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.gettParent()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 3
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.gettKey()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 4
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.getRsScriptStepFk()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 5
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(String.valueOf(item2.getrScriptStepPk()));
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							// 6 teetete
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(item2.gettName());
							rowItem2.getCellElements().add(cellInfo);
							cellInfo = null;

							rowItem.getRowElements().add(rowItem2);
							rowItem2 = null;

						}
					}
				}

				xmls.getRowElements().add(rowItem);
				rowItem = null;

			}
		}

		return xmls;

	}

	// 그룹화된 목록 표출되는 grid
	@RequestMapping(value = "/scriptGroupGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml scriptGroupGrid(HttpServletRequest request) throws UnsupportedEncodingException {
		List<ScriptRegistrationInfo> selectGroupList = null;
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		AJaxResVO jRes = new AJaxResVO();
		
		String flag = request.getParameter("flag");
		
		if (flag.trim().equals("2")) {
			if (userInfo != null) {
				ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

				if (!StringUtil.isNull(request.getParameter("keyword"), true)) {
					scriptRegistrationInfo.setrSearchWord(request.getParameter("keyword"));
					logger.info("keyword:" + request.getParameter("keyword"));
				}
				if (!StringUtil.isNull(request.getParameter("action"), true)) {
					scriptRegistrationInfo.setrSearchType(request.getParameter("action"));
					logger.info("searchType:" + request.getParameter("action"));
				}
				if (!StringUtil.isNull(request.getParameter("productType"), true)) {
					scriptRegistrationInfo.setrProductType(request.getParameter("productType"));
					logger.info("productType:" + request.getParameter("productType"));
				}
				List<ScriptRegistrationInfo> selectGRProductList = scriptRegistrationService
						.selectGRProductList(scriptRegistrationInfo);

				if (selectGRProductList != null && selectGRProductList.size() > 0) {
					// 새로운 변수값에 정보 넣어주기
					selectGroupList = selectGRProductList;
				}
			} else {
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				logInfoService.writeLog(request, "Etc - Logout");

			}
		}

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 6; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");

//					column.setHidden("0");

				switch (j) {
				case 0:
					column.setWidth("30");
					column.setType("ch");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setValue(
							"<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"
									+ request.getContextPath()
									+ "/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;

				case 1:
					column.setWidth("*");
					column.setCache("0");
//						column.setSort("na");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("번호");
					break;

				case 2:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
//						column.setId("r_rec_date");
					column.setFiltering("0");
					// column.setValue("2번");
					column.setValue("<div style=\"text-align:center;\">" + "상품명" + "</div>");
					break;

				case 3:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
//						column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "상품유형" + "</div>");
					break;

				case 4:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "상품코드" + "</div>");
					break;

				case 5:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + "그룹유무" + "</div>");
					break;

				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			// 로우 컬럼 그리기
			if (selectGroupList != null) {
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for (int i = 0; i < selectGroupList.size(); i++) {
					ScriptRegistrationInfo list = selectGroupList.get(i);
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
					rowItem.setRowClass("searchResultScript");
					rowItem.setId("" + list.getrProductListPk());

					// ch
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					// no
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("" + list.getrProductListPk());
					rowItem.getCellElements().add(cellInfo);

					// 상품명
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(list.getrProductName());
					rowItem.getCellElements().add(cellInfo);

					// 상품유형
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(list.getrProductType());
					rowItem.getCellElements().add(cellInfo);

					// 상품코드
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(list.getrProductCode());
					rowItem.getCellElements().add(cellInfo);

					// 그룹유무
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("-");
					rowItem.getCellElements().add(cellInfo);

					rowItem.getCellElements().add(cellInfo);
					xmls.getRowElements().add(rowItem);
					rowItem = null;

				}

			}
		}

		return xmls;
	}
	
	// etc config 리스트
	@RequestMapping(value="/script_variable.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml etc_config(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		// 어드민 계정만 관리 가능
		if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for( int j = 0; j < 8; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setEditable("0");
				column.setCache("0");
				column.setAlign("center");

				switch(j) {
				case 0:
					column.setWidth("100");
					column.setValue("번호");
					break;
				case 1:
					column.setWidth("130");
					column.setValue("상품코드");
					break;
				case 2:
					column.setWidth("100");
					column.setValue("타입");
					break;
				case 3:
					column.setWidth("400");
					column.setValue("상품명");
					break;
				case 4:
//					column.setHidden("1");
					column.setWidth("250");
					column.setValue("가변명");
					break;
				case 5:
					column.setWidth("150");
					column.setValue("가변코드");
					break;
				case 6:
					column.setWidth("*");
					column.setValue("가변값");
					break;
				case 7:
					column.setWidth("*");
					column.setValue("예약일자");
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

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			ScriptRegistrationInfo info = new ScriptRegistrationInfo();
			if (!StringUtil.isNull(request.getParameter("groupKey"),true))
				info.setrSearchType(request.getParameter("groupKey"));
			if (!StringUtil.isNull(request.getParameter("configKey"),true))
				info.setScriptType(request.getParameter("configKey"));
			if (!StringUtil.isNull(request.getParameter("configValue"),true))
				info.setrSearchWord(request.getParameter("configValue"));
			if (StringUtil.isNull(request.getParameter("configValue"),true))
				info.setrSearchWord("");

			logger.info("searchType : "+info.getrSearchType());
			logger.info("scriptType : "+info.getScriptType());
			logger.info("searchWord : "+info.getrSearchWord());
			
			List<ScriptRegistrationInfo> valueList = scriptRegistrationService.selectProductValueJoinProductList(info);
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < valueList.size(); i++) {
				ScriptRegistrationInfo item = valueList.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//value Pk
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(""+item.getrProductValuePk());
				rowItem.getCellElements().add(cellInfo);

				//상품코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getrProductCode());
				rowItem.getCellElements().add(cellInfo);

				//타입
				
				cellInfo = new dhtmlXGridRowCell();
				if(item.getrProductType().equals("1")) {
					cellInfo.setValue("신탁");					
				}
				if(item.getrProductType().equals("2")) {
					cellInfo.setValue("펀드");										
				}
				rowItem.getCellElements().add(cellInfo);
				
				//상품명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getrProductName());
				rowItem.getCellElements().add(cellInfo);
				
				//가변명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getrProductValueName());
				rowItem.getCellElements().add(cellInfo);
				
				//가변코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getrProductValueCode());
				rowItem.getCellElements().add(cellInfo);
				
				//가변값
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getrProductValueVal());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if(item.getrProductReserveDate() == null) {
					cellInfo.setValue("예약일자 없음");					
				}else {
					cellInfo.setValue(item.getrProductReserveDate().substring(0,10));					
				}
				rowItem.getCellElements().add(cellInfo);
				
				if(!StringUtil.isNull(""+item.getrProductListPk(),true)) {
					xmls.getRowElements().add(rowItem);
				}

				rowItem = null;
			}
		}

		return xmls;
	}	
	// etc config 리스트
	@RequestMapping(value="/admin_product_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml adminProductList(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		// 어드민 계정만 관리 가능
		if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			for( int j = 0; j < 8; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				
				column.setType("ro");
				column.setSort("str");
				column.setEditable("0");
				column.setCache("0");
				column.setAlign("center");
				
				switch(j) {
				case 0:
					column.setWidth("100");
					column.setValue("번호");
					break;
				case 1:
					column.setWidth("130");
					column.setValue("상품코드");
					break;
				case 2:
					column.setWidth("100");
					column.setValue("타입");
					break;
				case 3:
					column.setWidth("400");
					column.setValue("상품명");
					break;
				case 4:
//					column.setHidden("1");
					column.setWidth("250");
					column.setValue("파생타입");
					break;
				case 5:
					column.setWidth("150");
					column.setValue("상품속성");
					break;
				case 6:
					column.setWidth("150");
					column.setValue("-");
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
			
			afterInit.getCallElement().add(headCall);
			
			head.setAfterElement(afterInit);
			
			xmls.setHeadElement(head);
			
			ScriptRegistrationInfo info = new ScriptRegistrationInfo();
			
			if (!StringUtil.isNull(request.getParameter("groupKey"),true))
				info.setrSearchType(request.getParameter("groupKey"));
			if (!StringUtil.isNull(request.getParameter("configKey"),true))
				info.setScriptType(request.getParameter("configKey"));
			if (!StringUtil.isNull(request.getParameter("configValue"),true))
				info.setrSearchWord(request.getParameter("configValue"));
			if (StringUtil.isNull(request.getParameter("configValue"),true))
				info.setrSearchWord("");
			
			//상품타입
			logger.info("searchType : "+info.getrSearchType());
			//상품명상품코드
			logger.info("scriptType : "+info.getScriptType());
			//단어
			logger.info("searchWord : "+info.getrSearchWord());
			
//			List<ScriptRegistrationInfo> valueList = scriptRegistrationService.selectProductValueJoinProductList(info);
			
			List<ProductListVo> productList  = scriptRegistrationService.adminSelectProductList(info);
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			for(int i = 0; i < productList.size(); i++) {
				ProductListVo productListVo = productList.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(productListVo.getrProductListPk());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				//value Pk
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(""+productListVo.getrProductListPk());
				rowItem.getCellElements().add(cellInfo);
				
				//상품코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(productListVo.getrProductCode());
				rowItem.getCellElements().add(cellInfo);
				
				//타입
				cellInfo = new dhtmlXGridRowCell();
				String type = "";
				if(productListVo.getrProductType().equals("1")) {
					type = "신탁";
				}
				if(productListVo.getrProductType().equals("2")) {
					type = "펀드";
				}
				if(productListVo.getrProductType().equals("3")) {
					type = "일반";
				}
				if(productListVo.getrProductType().equals("4")) {
					type = "방카";
				}
				if(productListVo.getrProductType().equals("5")) {
					type = "퇴직연금";
				}
				cellInfo.setValue(type);					
				rowItem.getCellElements().add(cellInfo);
				
				//상품명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(productListVo.getrProductName());
				rowItem.getCellElements().add(cellInfo);
				
				//파생타입
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(productListVo.getrSysType());
				rowItem.getCellElements().add(cellInfo);
				
				//상품속성
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(productListVo.getrProductAttributes());
				rowItem.getCellElements().add(cellInfo);
				
				String div = "<button class='ui_main_btn_flat productDelete' onclick='productDelete("+productListVo.getrProductListPk()+")'>상품삭제</button>";
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(div);
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				
				rowItem = null;
			}
		}
		
		return xmls;
	}	
	
	
}
