package com.furence.recsee.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.model.CustomizeItemInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;
import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.model.ScreenInfo;
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.admin.service.CustomizeInfoService;
// import com.furence.recsee.admin.service.DelRecfileInfoService;
import com.furence.recsee.admin.service.PasswordPolicyService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.admin.service.ScreenInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.ConvertUtil;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadBeforeInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.main.service.SearchListInfoService;
import com.initech.shttp.server.Logger;


@Controller
public class XmlUserManageController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XmlUserManageController.class);
	/*@Autowired
	private DelRecfileInfoService delRecfileInfoService;*/
	
	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private ScreenInfoService screenInfoService;

	@Autowired
	private CustomizeInfoService customizeInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@Autowired
	private AllowableRangeInfoService allowableRangeInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private PasswordPolicyService rPasswordPolicyService;
	
	@Autowired
	private SearchListInfoService searchListInfoService;

	// 사용자 관리 그리드asd
	/**
	 * 비 녹취 사용자 정보 grid를 그려주는 함수 
	 * 
	 * @param request = 비 녹취 사용자 정보 그리드 검색 조건에 사용될 정보를 담고있는 request 파라미터
	 * @author bella
	 * @return return xml grid about aUserInfo for userManageGrid
	 */
	@RequestMapping(value = "/userManageGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml userManageGrid(HttpServletRequest request, HttpServletResponse response){

		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("company_telno");
			List<EtcConfigInfo> etcConfigResult = null;
			try {
				etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				logger.error("error",e);
			}
			

			xmls = new dhtmlXGridXml();
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

				for(int j = 0; j < 20; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setSort("na");
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
						column.setCache("0");
						column.setFiltering("0");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault()) + "</div>");
						break;
						// 순번
					case 1:
						column.setWidth("50");
						column.setCache("0");
						column.setFiltering("0");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault()) + "</div>");
						break;

						// 사용자 이름
					case 2:
						column.setId("userName");
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.userName", null,Locale.getDefault()) + "</div>");
						break;

						// 사용자 ID
					case 3:
						column.setId("userId");
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.userId"/*"header.popup.modifyUser.label.userEmp"*/, null,Locale.getDefault()) + "</div>");
						break;

						// 사용자 PW
					case 4:
						column.setWidth("150");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.password", null,Locale.getDefault()) + "</div>");
						break;

						// 내선번호
					case 5:
						column.setId("extNo");
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.ext", null,Locale.getDefault()) + "</div>");
						column.setHidden("1");
						break;

						// 연락처
					case 6:
						column.setId("userPhone");
						if(etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.phoneNo", null,Locale.getDefault()) + "</div>");
						break;

						// 성별
					case 7:
						column.setId("userSex");
						if(etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.sex", null,Locale.getDefault()) + "</div>");
						break;

						// 권한등급
					case 8:
						column.setId("userLevel");
						column.setWidth("150");
						column.setType("combo");
						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=authy");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.userLevel", null,Locale.getDefault()) + "</div>");
						break;

						// 대분류
					case 9:
						column.setId("bgName");
						column.setWidth("100");
						column.setType("combo");
						if(etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.level1", null,Locale.getDefault()) + "</div>");
						break;

						// 중분류
					case 10:
						column.setId("mgName");
						column.setWidth("100");
						column.setType("combo");
						if(etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.level2", null,Locale.getDefault()) + "</div>");
						break;

						// 소분류
					case 11:
						column.setId("sgName");
						column.setWidth("100");
						column.setType("combo");
						if(etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
							column.setHidden("1");
						}
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.level3", null,Locale.getDefault()) + "</div>");
						break;

						// 사원번호
					case 12:
						column.setWidth("180");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.empNo", null,Locale.getDefault()) + "</div>");
						column.setHidden("1");
						break;

						// 이메일
					case 13:
						column.setId("userEmail");
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.email", null,Locale.getDefault()) + "</div>");
						column.setHidden("1");
						break;

						// CTI ID
					case 14:
						column.setId("ctiId");
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">CTI ID</div>");
						column.setHidden("1");
						break;

						// 수정
					case 15:
						column.setWidth("120");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.campaign.grid.modification", null,Locale.getDefault()) + "</div>");
						break;
						//  대분류 코드
					case 16:
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.label.bgCode", null,Locale.getDefault()) + "</div>");
						break;
						// 중분 류 코드
					case 17:
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.label.mgCode", null,Locale.getDefault()) + "</div>");
						break;
						// 소분류 코드
					case 18:
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.label.sgCode", null,Locale.getDefault()) + "</div>");
						break;
					// 로그인 정보 초기화
					case 19:
						column.setWidth("200");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.grid.logInfoReset", null,Locale.getDefault()) + "</div>");
						break;
					}

					head.getColumnElement().add(column);
					column = null;

				}
				/*head.setAfterElement(new dhtmlXGridHeadAfterInit());
				dhtmlXGridHeadAfterInit afterInit = new dhtmlXGridHeadAfterInit();

				afterInit.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

				dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
				headCall.setParamElement(new ArrayList<String>());
				headCall.setCommand("attachHeader");
				headCall.getParamElement().add(
						"#rspan,"				// 체크
						+ "#rspan,"				// 순번
						+ "#text_filter,"		// 사용자 이름
						+ "#text_filter,"		// 사용자 ID
						+ "#rspan,"				// 비밀번호
						+ "#text_filter,"		// 내선번호
						+ "#text_filter,"		// 연락처
						+ "#select_filter,"		// 성별
						+ "#select_filter,"		// 권한 등급
						+ "#select_filter,"		// 대분류
						+ "#select_filter,"		// 중분류
						+ "#select_filter,"		// 소분류
						+ "#text_filter,"		// 사원번호
						+ "#text_filter,"		// 이메일
						+ "#text_filter,"		// CID
						+ "#rspan");			// 수정 버튼

				afterInit.getCallElement().add(headCall);
				head.setAfterElement(afterInit);*/
				xmls.setHeadElement(head);
			}else {
				RUserInfo ruserInfo = new RUserInfo();

				List<RUserInfo> ruserInfoResult  = null;

				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					ruserInfo.setPosStart(posStart);
				}
				Integer count = 100;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("count"));
					ruserInfo.setCount(count);
				}

				if(request.getParameter("userName") != null) {
					ruserInfo.setUserName(request.getParameter("userName"));
				}
				if(request.getParameter("userId") != null) {
					ruserInfo.setUserId(request.getParameter("userId"));
				}

				if(request.getParameter("bgCode") != null) {
					ruserInfo.setBgCode(request.getParameter("bgCode"));
				}

				if(request.getParameter("mgCode") != null) {
					ruserInfo.setMgCode(request.getParameter("mgCode"));
				}

				if(request.getParameter("sgCode") != null) {
					ruserInfo.setSgCode(request.getParameter("sgCode"));
				}

				etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("CALLCENTER");
				etcConfigInfo.setConfigKey("CALLCENTER");
				etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

				String[] lista = etcConfigResult.get(0).getConfigValue().split(",");
				List<String> list = new ArrayList<String>();

				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}

				ruserInfo.setList(list);


				ruserInfoResult = ruserInfoService.adminAUserManageSelect(ruserInfo);

				Integer ruserInfoResultTotal = ruserInfoResult.size();

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for(int i = 0; i < ruserInfoResultTotal; i++) {
					RUserInfo ruserItem = ruserInfoResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(posStart+i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);

					// 순번
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(String.valueOf(posStart+i+1));
					rowItem.getCellElements().add(cellInfo);

					// 사용자 이름
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(ruserItem.getUserName().trim());
					rowItem.getCellElements().add(cellInfo);

					// 사용자 아이디
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(ruserItem.getUserId().toString());
					rowItem.getCellElements().add(cellInfo);

					// 비밀번호
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getPassword(),true)) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(ruserItem.getPassword().trim());
					}
					rowItem.getCellElements().add(cellInfo);

					// 내선번호
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getExtNo(),true))
						cellInfo.setValue(" ");
					else
						cellInfo.setValue(ruserItem.getExtNo().trim());
					rowItem.getCellElements().add(cellInfo);

					// 연락처
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getUserPhone(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getUserPhone().trim());
					rowItem.getCellElements().add(cellInfo);

					// 성별
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getUserSex(),true))
						cellInfo.setValue("");
					else
						if(ruserItem.getUserSex().equals("m"))
							cellInfo.setValue(messageSource.getMessage("admin.label.men", null,Locale.getDefault())/*"남자"*/);
						else
							cellInfo.setValue(messageSource.getMessage("admin.label.women", null,Locale.getDefault())/*"여자"*/);
					rowItem.getCellElements().add(cellInfo);

					// 권한등급
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getUserLevel(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getUserLevel().trim());
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getBgName(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getBgName().trim());
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getMgName(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getMgName().trim());
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getSgName(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getSgName().trim());
					rowItem.getCellElements().add(cellInfo);

					// 사원번호
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getEmpId(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getEmpId().trim());
					rowItem.getCellElements().add(cellInfo);

					// 이메일
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getUserEmail(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getUserEmail().trim());
					rowItem.getCellElements().add(cellInfo);

					// CID
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getCtiId(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getCtiId().trim());
					rowItem.getCellElements().add(cellInfo);

					// 수정 버튼
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class=\"ui_btn_white icon_btn_pen_gray userModiBtn\" onclick=\"modifyUser("+String.valueOf(posStart+i+1)+")\">" + messageSource.getMessage("evaluation.campaign.grid.modification", null,Locale.getDefault()) + "</button>");
					rowItem.getCellElements().add(cellInfo);

					// 대분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getBgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getBgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					// 중분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getMgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getMgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					// 소분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getSgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getSgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					// 로그인 초기화
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class=\"ui_btn_white \" onclick=\"sessionDestroyUser("+String.valueOf(i+1)+")\">" + messageSource.getMessage("admin.grid.logInfoReset", null,Locale.getDefault()) + "</button>");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);

					rowItem = null;

				}
					Integer totalListResult = ruserInfoService.CountadminAUserManageSelect(ruserInfo);
					if( totalListResult > 0 &&  (request.getParameter("posStart")==null||"0".equals(request.getParameter("posStart")))) {
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
		}
		return xmls;
	}

	// 녹취 사용자 관리 그리드
	@RequestMapping(value = "/userManageRecGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml userManageRecGrid(HttpServletRequest request, HttpServletResponse response){

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
			
			PasswordPolicyInfo rPasswordPolicyInfo = new PasswordPolicyInfo();
			List<PasswordPolicyInfo> list = rPasswordPolicyService.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 25; j++){
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
					/*column.setSort("na");
					column.setType("ch");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault()) + "</div>");
*/					column.setWidth("30");
					column.setType("ch");
					column.setAlign("center");
					column.setSort("na");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img id='r_check_box' src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
					// 순번
				case 1:
					column.setWidth("50");
					column.setCache("0");
					column.setFiltering("0");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault()) + "</div>");
					break;

					// 사용자 이름
				case 2:
//					column.setWidth("100");
					column.setWidth("*");
					column.setId("userName");
					column.setSort("server");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.userName", null,Locale.getDefault()) + "</div>");
					break;

					// 사용자 ID
				case 3:
//					column.setWidth("150");
					column.setWidth("*");
					column.setId("userId");
					column.setSort("server");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.userId"/*"admin.grid.emp"*/, null,Locale.getDefault()) + "</div>");
					break;
					// CTI ID
				case 4:
					column.setId("ctiId");
					column.setWidth("100");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">CTI ID</div>");
					break;
					// 사용자 PW
				case 5:
					column.setWidth("150");
					column.setHidden("1");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.password", null,Locale.getDefault()) + "</div>");
					break;

					// 내선번호
				case 6:
					column.setWidth("100");
					column.setId("extNo");
					column.setSort("server");
					// 우리은행은 내선번호 미사용으로 숨김
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.ext", null,Locale.getDefault()) + "</div>");
					if(recsee_mobile.equals("Y")) {//렉시 모바일이면 내선번호 사용안함으로 hidden 처리
						column.setHidden("1");
					}
					break;

					// 연락처
				case 7:
//					column.setWidth("100");
					column.setWidth("*");
					column.setId("userPhone");
					column.setSort("server");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.phoneNo", null,Locale.getDefault()) + "</div>");
					break;

					// 성별
				case 8:
					column.setWidth("100");
					column.setId("userSex");
					column.setSort("server");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.sex", null,Locale.getDefault()) + "</div>");
					if(recsee_mobile.equals("Y")) {//렉시 모바일이면  성별 사용안함으로 hidden 처리
						column.setHidden("1");
					}
					break;

					// 권한등급
				case 9:
//					column.setWidth("150");
					column.setWidth("*");
					column.setType("combo");
					column.setSort("server");
					column.setId("userLevel");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=authy");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.userLevel", null,Locale.getDefault()) + "</div>");
					break;

					// 대분류
				case 10:
//					column.setWidth("100");
					column.setWidth("80");
					column.setType("combo");
					column.setId("bgName");
					//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault())+"</div>");
					break;

					// 중분류
				case 11:
//					column.setWidth("100");
					column.setWidth("100");
					column.setType("combo");
					column.setId("mgName");
					//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault())+"</div>");
					break;

					// 소분류
				case 12:
//					column.setWidth("100");
					column.setWidth("120");
					column.setType("combo");
					column.setId("sgName");
					//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault())+"</div>");
					break;

					// 사원번호
				case 13:
					column.setWidth("180");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.empNo", null,Locale.getDefault()) + "</div>");
					column.setHidden("1");
					break;

					// 이메일
				case 14:
					column.setWidth("*");
					column.setId("userEmail");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("header.popup.modifyUser.label.email", null,Locale.getDefault()) + "</div>");
					if(recsee_mobile.equals("Y")) {//렉시 모바일이면 이메일 사용안함으로 hidden 처리
						column.setHidden("1");
					}
					break;
					//  대분류 코드
				case 15:
					column.setWidth("0");
					column.setHidden("1");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.label.bgCode", null,Locale.getDefault()) + "</div>");
					break;
					// 중분 류 코드
				case 16:
					column.setWidth("0");
					column.setHidden("1");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.label.mgCode", null,Locale.getDefault()) + "</div>");
					break;
					// 소분류 코드
				case 17:
					column.setWidth("0");
					column.setHidden("1");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.label.sgCode", null,Locale.getDefault()) + "</div>");
					break;
				// 접속 아이피
				case 18:
					column.setWidth("200");
					if(!"Y".equals(loginIpChk)||("Y".equals(loginIpChk)&&!"Y".equals(mody)))
						column.setHidden("1");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.grid.clientIp", null,Locale.getDefault()) + "</div>");
					break;
				// 로그인 정보 초기화
				case 19:
					column.setWidth("200");
					column.setHidden("1");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.grid.logInfoReset", null,Locale.getDefault()) + "</div>");
					break;
					// 잠금 해제
				case 20:
					column.setWidth("*");
					column.setId("lockYn");
					column.setSort("server");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("admin.grid.unlockBtn", null,Locale.getDefault()) + "</div>");
					column.setHidden("1");
					// 잠금기능 사용하는경우
					if(list.size() > 0 && "Y".equals(list.get(0).getrPolicyUse()) && ("Y".equals(list.get(0).getrTryUse()) || "Y".equals(list.get(0).getrLockUse()))) {
						column.setHidden("0");
					}
					break;
					
				case 21:
					column.setWidth("*");
					column.setSort("na");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("views.search.grid.head.EmploymentCategory", null,Locale.getDefault()) + "</div>");
					break;
					
				case 22:
					column.setWidth("*");
					column.setSort("na");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("views.search.grid.head.EmploymentCategory", null,Locale.getDefault()) + "</div>");
					break;
				
				case 23:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN6");
					column.setEditable("0");
					column.setWidth("100");
					column.setSort("na");
					// column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("views.search.grid.head.RecseeUserYN", null,Locale.getDefault()) + "</div>");
					column.setValue("<div style=\"text-align:center;\">신규 녹취 사용</div>");
					break;
								
					// 수정
				case 24:
					column.setWidth("*");
					column.setSort("na");
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("message.btn.modify", null,Locale.getDefault()) + "</div>");
					break;
				}

				head.getColumnElement().add(column);
				column = null;

			}
			/*head.setAfterElement(new dhtmlXGridHeadAfterInit());
			dhtmlXGridHeadAfterInit afterInit = new dhtmlXGridHeadAfterInit();

			afterInit.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			headCall.getParamElement().add(
					"#rspan,"				// 체크
					+ "#rspan,"				// 순번
					+ "#text_filter,"		// 사용자 이름
					+ "#text_filter,"		// 사용자 ID
					+ "#rspan,"				// 비밀번호
					+ "#text_filter,"		// 내선번호
					+ "#text_filter,"		// 연락처
					+ "#select_filter,"		// 성별
					+ "#select_filter,"		// 권한 등급
					+ "#select_filter,"		// 대분류
					+ "#select_filter,"		// 중분류
					+ "#select_filter,"		// 소분류
					+ "#text_filter,"		// 사원번호
					+ "#text_filter,"		// 이메일
					+ "#text_filter,"		// CID
					+ "#rspan,"		// 대분류
					+ "#rspan,"		// 중분류
					+ "#rspan,"		// 소분류
					+ "#rspan,"		// 사용자 ip
					+ "#rspan,"	//로그인초기화
					+ "#rspan");			// 수정 버튼

			afterInit.getCallElement().add(headCall);
			head.setAfterElement(afterInit);*/
			xmls.setHeadElement(head);
			}else {
			RUserInfo ruserInfo = new RUserInfo();
		
			Integer posStart = 0;
			if(request.getParameter("posStart") != null) {
				posStart = Integer.parseInt(request.getParameter("posStart"));
				ruserInfo.setPosStart(posStart);
			}
			Integer count = 100;
			if(request.getParameter("count") != null) {
				count = Integer.parseInt(request.getParameter("count"));
				ruserInfo.setCount(count);
			}
			
			if(request.getParameter("userName") != null) {
				// 한글 파라미터라서 디코딩 필요함
				try {
					ruserInfo.setUserName(URLDecoder.decode(request.getParameter("userName"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error("error",e);
				}
			}
			
			if(request.getParameter("userId") != null) {
				ruserInfo.setUserId(request.getParameter("userId"));
			}
			if(request.getParameter("extNo") != null) {
				ruserInfo.setExtNo(request.getParameter("extNo"));
			}

			if(request.getParameter("Authy") != null) {
				ruserInfo.setUserLevel(request.getParameter("Authy"));
			}
			if(request.getParameter("bgCode") != null) {
				ruserInfo.setBgCode(request.getParameter("bgCode"));
			}

			if(request.getParameter("mgCode") != null) {
				ruserInfo.setMgCode(request.getParameter("mgCode"));
			}

			if(request.getParameter("sgCode") != null) {
				ruserInfo.setSgCode(request.getParameter("sgCode"));
			}
			
			if(request.getParameter("orderBy") != null) {
				ruserInfo.setOrderBy(request.getParameter("orderBy"));
			}
			
			if(request.getParameter("direction") != null) {
				ruserInfo.setDirection(request.getParameter("direction"));
			}
			
			if(request.getParameter("EmploymentSearch") != null) {
				ruserInfo.setrUseYn(request.getParameter("EmploymentSearch"));
			}
			
			if(request.getParameter("skinCode") != null && !"".equals(request.getParameter("skinCode"))) {
				ruserInfo.setSkinCode(request.getParameter("skinCode"));
			}
			
			List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
			// 허용범위 적용
			if(nowAccessInfo.getAccessLevel().substring(0,1).equals("R")){
				List<AllowableRangeInfo> allowableList = null;
				
				AllowableRangeInfo allowableRangeInfoChk = new AllowableRangeInfo();
	        	allowableRangeInfoChk.setrAllowableCode(nowAccessInfo.getAccessLevel());
	        	allowableList = allowableRangeInfoService.selectAllowableRangeInfo(allowableRangeInfoChk);
	        	if(allowableList.size()>0) {
		        	for(int i = 0; i < allowableList.size(); i++) {
						HashMap<String, String> item = new HashMap<String, String>();
		        		item.put("bgcode", allowableList.get(i).getrBgCode());
		        		item.put("mgcode", allowableList.get(i).getrMgCode());
		        		item.put("sgcode", allowableList.get(i).getrSgCode());
			        	authyInfo.add(item); 
		        	}
	        	} else {
	        		HashMap<String, String> item = new HashMap<String, String>();
	        		item.put("noneallowable", "noneallowable");
		        	authyInfo.add(item); 
	        	}
			}else {
				if(!nowAccessInfo.getAccessLevel().equals("A")) {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("bgcode", userInfo.getBgCode());
					if(!nowAccessInfo.getAccessLevel().equals("B")) {
						item.put("mgcode", userInfo.getMgCode());
					}
					if(!nowAccessInfo.getAccessLevel().equals("B") && !nowAccessInfo.getAccessLevel().equals("M")) {
						item.put("sgcode", userInfo.getSgCode());
					}
					if(!nowAccessInfo.getAccessLevel().equals("B") && !nowAccessInfo.getAccessLevel().equals("M") && !nowAccessInfo.getAccessLevel().equals("S")) {
						item.put("user", userInfo.getUserId());
					}
					authyInfo.add(item);
				}
			}
			
			if(authyInfo != null && authyInfo.size() > 0) {
				ruserInfo.setAuthyInfo(authyInfo);
			}
			
			List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
			Integer ruserInfoResultTotal = ruserInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < ruserInfoResultTotal; i++) {
				RUserInfo ruserItem = ruserInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(posStart+i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// 체크
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				// 순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(posStart+i+1));
				rowItem.getCellElements().add(cellInfo);

				// 사용자 이름
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getUserName(),true)){
					cellInfo.setValue("");
				}else{
					cellInfo.setValue(ruserItem.getUserName().trim());
				}

				rowItem.getCellElements().add(cellInfo);

				// 사용자 아이디
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(ruserItem.getUserId().toString());
				rowItem.getCellElements().add(cellInfo);

				// CID
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getCtiId(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getCtiId().trim());
				rowItem.getCellElements().add(cellInfo);
				
				// 비밀번호
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getPassword(),true)) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(ruserItem.getPassword().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 내선번호
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getExtNo(),true))
					cellInfo.setValue(" ");
				else
					cellInfo.setValue(ruserItem.getExtNo().trim());
				rowItem.getCellElements().add(cellInfo);

				// 연락처
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getUserPhone(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getUserPhone().trim());
				rowItem.getCellElements().add(cellInfo);

				// 성별
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getUserSex(),true))
					cellInfo.setValue("");
				else
					if(ruserItem.getUserSex().equals("m"))
						cellInfo.setValue(messageSource.getMessage("admin.label.men", null,Locale.getDefault())/*"남자"*/);
					else
						cellInfo.setValue(messageSource.getMessage("admin.label.women", null,Locale.getDefault())/*"여자"*/);
				rowItem.getCellElements().add(cellInfo);

				// 권한등급
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getUserLevel(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getUserLevel().trim());
				rowItem.getCellElements().add(cellInfo);

				// 대분류
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getBgName(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getBgName().trim());
				rowItem.getCellElements().add(cellInfo);

				// 중분류
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getMgName(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getMgName().trim());
				rowItem.getCellElements().add(cellInfo);

				// 소분류
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getSgName(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getSgName().trim());
				rowItem.getCellElements().add(cellInfo);

				// 사원번호
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getEmpId(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getEmpId().trim());
				rowItem.getCellElements().add(cellInfo);

				// 이메일
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getUserEmail(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getUserEmail().trim());
				rowItem.getCellElements().add(cellInfo);

				// 대분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getBgCode(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getBgCode().trim());
				rowItem.getCellElements().add(cellInfo);

				// 중분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getMgCode(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getMgCode().trim());
				rowItem.getCellElements().add(cellInfo);

				// 소분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getSgCode(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getSgCode().trim());
				rowItem.getCellElements().add(cellInfo);
				
				//사용자 ip
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getClientIp(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getClientIp().trim());
				rowItem.getCellElements().add(cellInfo);
				
				// 로그인 초기화
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_btn_white \" onclick=\"sessionDestroyUser("+String.valueOf(i+1)+")\">"+messageSource.getMessage("admin.grid.logInfoReset", null,Locale.getDefault())/*초기화*/+"</button>");
				rowItem.getCellElements().add(cellInfo);
				
				// 잠금 해제
				cellInfo = new dhtmlXGridRowCell();
				if("Y".equals(ruserItem.getLockYn()))
					cellInfo.setValue("<button class=\"ui_btn_white unlockUserBtn\" onclick=\"unlockUser("+String.valueOf(posStart+i+1)+")\"><i class=\"fas fa-lock\"></i></button>");
				else
					cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);
				
				// 재직자 구분
				cellInfo = new dhtmlXGridRowCell();
				if(ruserItem.getrUseYn().equals("Y")) {
					cellInfo.setValue(messageSource.getMessage("views.search.grid.head.Incumbent", null,Locale.getDefault()));
				}else {
					cellInfo.setValue(messageSource.getMessage("views.search.grid.head.Retiree", null,Locale.getDefault()));
				}			
				rowItem.getCellElements().add(cellInfo);
				
				// 재직자 값
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(ruserItem.getrUseYn());
				rowItem.getCellElements().add(cellInfo);
				
				// 신규 녹취 사용 구분
				cellInfo = new dhtmlXGridRowCell();
				if(ruserItem.getSkinCode() == null) cellInfo.setValue("Y");
				else cellInfo.setValue(ruserItem.getSkinCode().trim());
				rowItem.getCellElements().add(cellInfo);
				
				// 수정 버튼
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_btn_white icon_btn_pen_gray userModiBtn\" onclick=\"modifyUser("+String.valueOf(posStart+i+1)+")\">"+messageSource.getMessage("evaluation.campaign.grid.modification", null,Locale.getDefault()) +"</button>");
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
			
			Integer totalListResult = ruserInfoService.CountadminUserManageSelect(ruserInfo);
			if( totalListResult > 0 &&  (request.getParameter("posStart")==null||"0".equals(request.getParameter("posStart")))) {
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
		}
		return xmls;
	}

	@RequestMapping(value="/screen_user_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml screen_user_list(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int i=0; i<5; i++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(i == 0 ) {
					column.setType("ch");
				}

				column.setFiltering("0");
				column.setEditable("false");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("190");
				column.setValue("#cspan");
				column.setSort("na");

				switch(i) {
				case 0 :
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
				case 1:
					// 기존 : 추가할곳 추가할대상 두개 불러오돈것 한개만 불러오도록 변경
					// 추가는 팝업창에서 할것
					// 기존에 setType이 왜 콤보인지 모르겠음..
					//column.setType("combo");
					//column.setSource(request.getContextPath() + "/opt/user_combo_option.xml");
					/*if(request.getParameter("type") != null && request.getParameter("type").trim().equals("screen")) {
							column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage("management.screenUserListFieldSet.form.item.screenUserFieldset.title", null,Locale.getDefault())+"</div>");
						} else if(request.getParameter("type") != null && request.getParameter("type").trim().equals("user")) {
							column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage("management.screenUserListFieldSet.form.item.UserFieldset.title", null,Locale.getDefault())+"</div>");
						}*/
					column.setType("ro");
					column.setValue(messageSource.getMessage("admin.grid.user", null,Locale.getDefault()));
					break;
				case 2:
					column.setType("ro");
					column.setValue(messageSource.getMessage("header.popup.modifyUser.label.empNo", null,Locale.getDefault()));
					break;
				case 3:
					column.setType("ro");
					column.setValue(messageSource.getMessage("admin.label.status", null,Locale.getDefault()));
					break;
				case 4:
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
			//headCall.getParamElement().add("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage("management.user.title", null,Locale.getDefault())+"</div>,<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage("management.user.title.extNum", null,Locale.getDefault())+"</div>,<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage("message.title.status", null,Locale.getDefault())+"</div>");

			//afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			ScreenInfo screenInfo = new ScreenInfo();

			if(request.getParameter("bgCode") != null && !request.getParameter("bgCode").trim().isEmpty()) {
				screenInfo.setrBgCode(request.getParameter("bgCode"));
			}
			if(request.getParameter("mgCode") != null && !request.getParameter("mgCode").trim().isEmpty()) {
				screenInfo.setrMgCode(request.getParameter("mgCode"));
			}
			if(request.getParameter("sgCode") != null && !request.getParameter("sgCode").trim().isEmpty()) {
				screenInfo.setrSgCode(request.getParameter("sgCode"));
			}

			// 기존 (좌우 두개 불러오는 것)
			/*List<ScreenInfo> screenInfoResult = null;
			if(request.getParameter("type") != null && request.getParameter("type").trim().equals("screen")) {
				screenInfoResult = screenInfoService.selectScreenUserInfo(screenInfo);
			} else if(request.getParameter("type") != null && request.getParameter("type").trim().equals("user")) {
				screenInfoResult = screenInfoService.selectUserInfo(screenInfo);
			}*/

			// 한개만 불러오기 (사용자 추가를 팝업으로 변경하기 위함)
			List<ScreenInfo> screenInfoResult = screenInfoService.selectScreenUserInfo(screenInfo);
			Integer screenInfoResultTotal = screenInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < screenInfoResultTotal; i++) {
				ScreenInfo screenItem = screenInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				if(screenItem.getrExtNo() == null || ( screenItem.getrScreenStatus() != null && screenItem.getrScreenStatus().trim().equals("O"))) {
					rowItem.setBgColor("red");
				}

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(screenItem.getrUserId());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(screenItem.getrExtNo());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}

		return xmls;
	}

	// 검색 사용자 정의 떠온거
	@RequestMapping(value="/customize_info.xml", method=RequestMethod.GET, produces="application/xml;charset=UTF-8")
	public @ResponseBody dhtmlXGridXml customizeSearchInfo(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			String messageDefault = "management.searchCustomize.";
			String typeString = "list";

			if(request.getParameter("type") != null && !request.getParameter("type").isEmpty()) {
				typeString = request.getParameter("type");
			}
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();

			etcConfigInfo.setGroupKey("PLAYER");
			etcConfigInfo.setConfigKey("STT_PLAYER");
			List<EtcConfigInfo>  etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String sttPlayerYN = "N";
			if(etcConfigResult.size() > 0) {
				sttPlayerYN = etcConfigResult.get(0).getConfigValue();
			}

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();

			dhtmlXGridHeadBeforeInit beforeInitItem = new dhtmlXGridHeadBeforeInit();
			beforeInitItem.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

			dhtmlXGridHeadCall callItem = new dhtmlXGridHeadCall();
			callItem.setCommand("setStyle");
			List<String> paramList = new ArrayList<String>();
			paramList.add("text-align:center;");
			callItem.setParamElement(paramList);

			beforeInitItem.getCallElement().add(callItem);

			head.setBeforeElement(beforeInitItem);

			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

			column.setAlign("center");
			column.setType("ro");
			column.setWidth("130");
			column.setFiltering("0");
			column.setEditable("0");
			column.setCache("1");
			column.setSort("na");

			column.setValue(messageSource.getMessage(messageDefault + typeString + ".grid.title.name", null, Locale.getDefault()) + (StringUtil.isNull(request.getParameter("userId")) ? " (list)" : " (used)"));

			head.getColumnElement().add(column);

			column = new dhtmlXGridHeadColumn();

			column.setAlign("center");
			column.setType("ro");
			column.setWidth("0");
			column.setFiltering("0");
			column.setEditable("0");
			column.setCache("1");
			column.setSort("na");
			column.setHidden("1");
			column.setValue(messageSource.getMessage(messageDefault + typeString + ".grid.title.var", null, Locale.getDefault()));

			head.getColumnElement().add(column);

			column = new dhtmlXGridHeadColumn();

			column.setAlign("center");
			column.setType("ro");
			column.setWidth("0");
			column.setFiltering("0");
			column.setEditable("0");
			column.setCache("1");
			column.setSort("na");
			column.setHidden("1");
			column.setValue("index");

			head.getColumnElement().add(column);

			xmls.setHeadElement(head);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			List<HashMap<String, String>> customizeList = new ArrayList<HashMap<String, String>>();
			Integer customizeListTotal = 0;
			if (typeString.equals("item")) {

				CustomizeItemInfo  customizeInfo = new CustomizeItemInfo();
				if(request.getParameter("userId") != null && !request.getParameter("userId").isEmpty()) {
					customizeInfo.setrUserId(request.getParameter("userId"));
				} else {
					customizeInfo.setrUserId("init");
				}
				List<CustomizeItemInfo> tmpCustomizeList = customizeInfoService.selectCustomizeItemInfo(customizeInfo);
				customizeListTotal = tmpCustomizeList.size();
				if(customizeListTotal > 0) { customizeList.add(tmpCustomizeList.get(0).getAllItem()); }
			} else if(typeString.equals("copy")) {
				CustomizeCopyListInfo  customizeListInfo = new CustomizeCopyListInfo();
				if(request.getParameter("userId") != null && !request.getParameter("userId").isEmpty()) {
					customizeListInfo.setrUserId(request.getParameter("userId"));
				} else {
					customizeListInfo.setrUserId("init");
				}
				List<CustomizeCopyListInfo> tmpCustomizeList = customizeInfoService.selectCustomizeCopyListInfo(customizeListInfo);
				customizeListTotal = tmpCustomizeList.size();
				if(customizeListTotal > 0) { customizeList.add(tmpCustomizeList.get(0).getAllItem()); }
			} else {
				
				CustomizeListInfo  customizeInfo = new CustomizeListInfo();
				if(request.getParameter("userId") != null && !request.getParameter("userId").isEmpty()) {
					customizeInfo.setrUserId(request.getParameter("userId"));
				} else {
					customizeInfo.setrUserId("init");
				}
				List<CustomizeListInfo> tmpCustomizeList = customizeInfoService.selectCustomizeListInfo(customizeInfo);
				customizeListTotal = tmpCustomizeList.size();
				if(customizeListTotal > 0) { customizeList.add(tmpCustomizeList.get(0).getAllItem()); }
			}

			if(customizeListTotal == 1) {
				Iterator<String> columnList = customizeList.get(0).keySet().iterator();

				HashMap<String, String> customizeItem = customizeList.get(0);

				Integer nRowIdx = 1;
				while(columnList.hasNext()) {
					String columnName = columnList.next();
					if(columnName.equals("rUserId")) continue;
					
					if("N".equals(sttPlayerYN)&&customizeItem.get(columnName).toString().equals("r_stt_player")) continue;

					if(customizeItem.get(columnName) != null && !customizeItem.get(columnName).toString().isEmpty() ) {
						if(customizeItem.get(columnName).equals("r_rec_visible")) continue;
						
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(nRowIdx.toString());
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
						
						//System.out.println(messageDefault + ConvertUtil.convert2CamelCase(customizeItem.get(columnName).toString()));
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(messageSource.getMessage(messageDefault + ConvertUtil.convert2CamelCase(customizeItem.get(columnName).toString()), null, Locale.getDefault()));
						rowItem.getCellElements().add(cellInfo);

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(customizeItem.get(columnName).toString());
						rowItem.getCellElements().add(cellInfo);

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(nRowIdx.toString());
						rowItem.getCellElements().add(cellInfo);

						xmls.getRowElements().add(rowItem);

						nRowIdx++;
					}
				}
			}
		}
		return xmls;

	}

	/*기존 로직 가져오기 위해  주석처리...하ㅏ하하하핳하ㅏㅎ하ㅏ하하ㅏ 신한 생명 시바꺼..*/
	/*@RequestMapping(value="/customize_info.xml", method=RequestMethod.GET, produces="application/xml;charset=UTF-8")
	public @ResponseBody dhtmlXGridXml customizeSearchInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			String messageDefault = "management.searchCustomize.";
			String typeString = "list";

			if(request.getParameter("type") != null && !request.getParameter("type").isEmpty()) {
				typeString = request.getParameter("type");
			}

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();

			dhtmlXGridHeadBeforeInit beforeInitItem = new dhtmlXGridHeadBeforeInit();
			beforeInitItem.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

			dhtmlXGridHeadCall callItem = new dhtmlXGridHeadCall();
			callItem.setCommand("setStyle");
			List<String> paramList = new ArrayList<String>();
			paramList.add("text-align:center;");
			callItem.setParamElement(paramList);

			beforeInitItem.getCallElement().add(callItem);

			head.setBeforeElement(beforeInitItem);

			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

			for(int i = 0 ; i < 4 ; i++){

				column = new dhtmlXGridHeadColumn();
				column.setAlign("center");
				column.setType("ro");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("1");
				column.setSort("na");
				column.setHidden("0");

				switch(i){
				case 0:
					column.setHidden("1");
					column.setWidth("80");
					column.setValue("순번");
					break;
				case 1:
					column.setWidth("*");
					column.setValue(messageSource.getMessage("management.search_customize.search_"+typeString, null, Locale.getDefault()));
					break;
				case 2:
					column.setHidden("1");
					column.setValue("실 컬럼명");
					break;
				case 3:
					column.setType("ch");
					column.setWidth("80");
					column.setValue("사용 유무");
					break;
				}

				head.getColumnElement().add(column);
			}

			xmls.setHeadElement(head);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			List<HashMap<String, String>> customizeList = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> initList = new ArrayList<HashMap<String,String>>();

			if (typeString.equals("item")) {

				CustomizeItemInfo  customizeInfo = new CustomizeItemInfo();
				customizeInfo.setrUserId(request.getParameter("userId"));
				List<CustomizeItemInfo> tmpCustomizeList = customizeInfoService.selectCustomizeItemInfo(customizeInfo);
				customizeInfo.setrUserId("init");
				List<CustomizeItemInfo> tmpInitList = customizeInfoService.selectCustomizeItemInfo(customizeInfo);

				if(tmpCustomizeList.size() > 0)
					customizeList.add(tmpCustomizeList.get(0).getAllItem());
				if(tmpInitList.size() > 0)
					initList.add(tmpInitList.get(0).getAllItem());
			} else {

				CustomizeListInfo  customizeInfo = new CustomizeListInfo();

				customizeInfo.setrUserId(request.getParameter("userId"));
				List<CustomizeListInfo> tmpCustomizeList = customizeInfoService.selectCustomizeListInfo(customizeInfo);
				customizeInfo.setrUserId("init");
				List<CustomizeListInfo> tmpInitList = customizeInfoService.selectCustomizeListInfo(customizeInfo);

				if(tmpCustomizeList.size() > 0)
					customizeList.add(tmpCustomizeList.get(0).getAllItem());
				if(tmpInitList.size() > 0)
					initList.add(tmpInitList.get(0).getAllItem());
			}

			if(initList.size() == 1) {
				HashMap<String,String> columnList = new HashMap<String,String>();

				if (customizeList.size() > 0)
					columnList = customizeList.get(0);

				Iterator<String> initializeList = initList.get(0).keySet().iterator();

				HashMap<String, String> initializeItem = initList.get(0);

				Integer nRowIdx = 1;
				while(initializeList.hasNext()) {
					String columnName = initializeList.next();

					if(columnName.equals("rUserId")) continue;

					if(initializeItem.get(columnName) != null && !initializeItem.get(columnName).toString().isEmpty() ) {
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(nRowIdx.toString());
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

						// 순번
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(nRowIdx.toString());
						rowItem.getCellElements().add(cellInfo);

						// 컬럼명
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(messageSource.getMessage(messageDefault + ConvertUtil.convert2CamelCase(initializeItem.get(columnName).toString()), null, Locale.getDefault()));
						rowItem.getCellElements().add(cellInfo);

						// 실 컬럼명
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(initializeItem.get(columnName).toString());
						rowItem.getCellElements().add(cellInfo);

						// 체크 박스
						cellInfo = new dhtmlXGridRowCell();
						if (columnList.containsKey(columnName)) {
							cellInfo.setValue("1");
					    } else {
					    	cellInfo.setValue("0");
					    }
						rowItem.getCellElements().add(cellInfo);

						xmls.getRowElements().add(rowItem);

						nRowIdx++;
					}
				}
			}
		}
		return xmls;
	}*/


	// 권한 목록
	@RequestMapping(value="/authy_list.xml", method=RequestMethod.GET, produces="application/xml;charset=UTF-8")
	public @ResponseBody String authyList(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "userManage.authyManage");

        String rstValue = "";

		if(userInfo != null) {

	        List<MMenuAccessInfo> menuAccessList = null;
	        int menuAccessListTotal = 0;

	        if(request.getParameter("levelCode") != null && !request.getParameter("levelCode").equals("")) {
				MMenuAccessInfo menuAccessInfoChk = new MMenuAccessInfo();
				menuAccessInfoChk.setLevelCode(request.getParameter("levelCode"));
				menuAccessInfoChk.setDisplayLevel(5);
				menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccessInfoChk);
				menuAccessListTotal = menuAccessList.size();
	        }

			try {
				String formAuthyInit = "authy_init.xml";

				File file = new File(request.getSession().getServletContext().getRealPath("/") + "/resources/common/form/" + formAuthyInit);
				DocumentBuilderFactory authyFactory = DocumentBuilderFactory.newInstance();

		        DocumentBuilder authyBuilder = authyFactory.newDocumentBuilder();
				Document authyDoc = authyBuilder.parse(file);
				authyDoc.getDocumentElement().normalize();

				NodeList nList = authyDoc.getElementsByTagName("column");
				
				for(int i=0; i<nList.getLength(); i++) {
					Node nNode = nList.item(i);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.groupName", messageSource.getMessage("management.access.title.groupName", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.menuName", messageSource.getMessage("management.access.title.menuName", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.use", messageSource.getMessage("management.access.title.use", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.access", messageSource.getMessage("management.access.title.access", null,Locale.getDefault())));

						if(eElement.getAttribute("source") != null && !eElement.getAttribute("source").isEmpty()) {
							eElement.setAttribute("source", eElement.getAttribute("source").replace("contextPath", request.getContextPath()));
						}
					}
				}
				nList = authyDoc.getElementsByTagName("param");

				for(int i=0; i<nList.getLength(); i++) {
					Node nNode = nList.item(i);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.range", messageSource.getMessage("management.access.title.range", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.read", messageSource.getMessage("management.access.title.read", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.write", messageSource.getMessage("management.access.title.write", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.modi", messageSource.getMessage("management.access.title.modi", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.delete", messageSource.getMessage("management.access.title.delete", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.listen", messageSource.getMessage("management.access.title.listen", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.download", messageSource.getMessage("management.access.title.download", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.excel", messageSource.getMessage("management.access.title.excel", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.masking", messageSource.getMessage("management.access.title.masking", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.prerecipt", messageSource.getMessage("management.access.title.prerecipt", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.recipt", messageSource.getMessage("management.access.title.recipt", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.approve", messageSource.getMessage("management.access.title.approve", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("header.menu.label.downloadApproveInit", messageSource.getMessage("header.menu.label.downloadApproveInit", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.encyn", messageSource.getMessage("management.access.title.encyn", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.upload", messageSource.getMessage("management.access.title.upload", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.bestcall", messageSource.getMessage("management.access.title.bestcall", null,Locale.getDefault())));
						eElement.setTextContent(eElement.getTextContent().replace("management.access.title.feedback.modify", messageSource.getMessage("management.access.title.feedback.modify", null,Locale.getDefault())));
						
					}
				}

				NodeList nRowList = authyDoc.getElementsByTagName("row");
				for(int row=0; row<nRowList.getLength(); row++) {

					nRowList.item(row).normalize();
					nList = nRowList.item(row).getChildNodes();
					for(int i=1; i<nList.getLength(); i+=2) {
						Node nNode = nList.item(i);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							if(i==1 || i==3) {
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.searchNListen$", 					messageSource.getMessage("header.menu.label.searchNListen", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.searchNListenMemo$", 				messageSource.getMessage("header.menu.label.searchNListenMemo", null,Locale.getDefault())));

								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.searchNListenCallType$", 			messageSource.getMessage("header.menu.label.searchNListenCallType", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.searchNDate$", 					messageSource.getMessage("header.menu.label.searchNDate", null,Locale.getDefault())));
																
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.reportNDashboard$", 				messageSource.getMessage("header.menu.label.reportNDashboard", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.callAll$", 						messageSource.getMessage("header.menu.label.callAll", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.callUser$", 						messageSource.getMessage("header.menu.label.callUser", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.callDayTimeUser$", 				messageSource.getMessage("header.menu.label.callDayTimeUser", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.callMobile$", 						messageSource.getMessage("header.menu.label.callMobile", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.dashboard$", 						messageSource.getMessage("header.menu.label.dashboard", null,Locale.getDefault())));

								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.monitoring$", 						messageSource.getMessage("header.menu.label.monitoring", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.realtimeMonitoring$",				messageSource.getMessage("header.menu.label.realtimeMonitoring", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.realtimeMonitoringGrid$",			messageSource.getMessage("header.menu.label.realtimeMonitoringGrid", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.realtimeMonitoringOffice$",		messageSource.getMessage("header.menu.label.realtimeMonitoringOffice", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.systemMonitoring$", 				messageSource.getMessage("header.menu.label.systemMonitoring", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.BluePrintMonitoring$", 			messageSource.getMessage("header.menu.label.BluePrintMonitoring", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.serverMonitoring$",				messageSource.getMessage("header.menu.label.serverMonitoring", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.systemRealtimeMonitoring$",		messageSource.getMessage("header.menu.label.systemRealtimeMonitoring", null,Locale.getDefault())));
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.approveList$", 					messageSource.getMessage("header.menu.label.approveList", null,Locale.getDefault())));

								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.myFolderMenu$", 					messageSource.getMessage("header.menu.label.myFolderMenu", null,Locale.getDefault())));

								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.BestCall$", 						messageSource.getMessage("header.menu.label.BestCall", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.BestCallManage$", 					messageSource.getMessage("header.menu.label.BestCallManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.BestCallShare$", 					messageSource.getMessage("header.menu.label.BestCallShare", null,Locale.getDefault())));

								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.uploadStatus$", 					messageSource.getMessage("header.menu.label.uploadStatus", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.individualUploadstatus$", 			messageSource.getMessage("header.menu.label.individualUploadstatus", null,Locale.getDefault())));
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.recording$", 					messageSource.getMessage("header.menu.label.recording", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.faceRecording$", 					messageSource.getMessage("header.menu.label.faceRecording", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.audioCalibration$", 					messageSource.getMessage("header.menu.label.audioCalibration", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.scriptRegistration$", 					messageSource.getMessage("header.menu.label.scriptRegistration", null,Locale.getDefault())));
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.systemOption$", 							messageSource.getMessage("admin.menu.systemOption", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.channel$", 				messageSource.getMessage("admin.menu.li.systemOption.channel", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.channelMonitoring$", 		messageSource.getMessage("admin.menu.li.systemOption.channelMonitoring", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.server$", 				messageSource.getMessage("admin.menu.li.systemOption.server", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.switchboard$", 			messageSource.getMessage("admin.menu.li.systemOption.switchboard", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.subNumber$", 				messageSource.getMessage("admin.menu.li.systemOption.subNumber", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.phoneMapping$", 			messageSource.getMessage("admin.menu.li.systemOption.phoneMapping", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.publicIp$", 				messageSource.getMessage("admin.menu.li.systemOption.publicIp", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.logoSetting$", 			messageSource.getMessage("admin.menu.li.systemOption.logoSetting", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.packetSetting$", 			messageSource.getMessage("admin.menu.li.systemOption.packetSetting", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemOption.sttServer$", 				messageSource.getMessage("admin.menu.li.systemOption.sttServer", null,Locale.getDefault())));
								
								
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.systemManage$", 							messageSource.getMessage("admin.menu.systemManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.details$", 				messageSource.getMessage("admin.menu.li.systemManage.details", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.log$", 					messageSource.getMessage("admin.menu.li.systemManage.log", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.logView$", 				messageSource.getMessage("admin.menu.li.systemManage.logView", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.packetLogManage$", 				messageSource.getMessage("admin.menu.li.systemManage.packetLogManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.fileRecoverManage$", 				messageSource.getMessage("admin.menu.li.systemManage.fileRecoverManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.queue$", 					messageSource.getMessage("admin.menu.li.systemManage.queue", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.systemManage.backupSearch$", 			messageSource.getMessage("admin.menu.li.systemManage.backupSearch", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.schedulerManage.deleteRecfile$", 		messageSource.getMessage("admin.menu.li.schedulerManage.deleteRecfile", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.schedulerManage.backupRecfile$", 		messageSource.getMessage("admin.menu.li.schedulerManage.backupRecfile", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.schedulerManage.dbManage$", 			messageSource.getMessage("admin.menu.li.schedulerManage.dbManage", null,Locale.getDefault())));
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.userManage$", 							messageSource.getMessage("admin.menu.userManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.userManage$", 				messageSource.getMessage("admin.menu.li.userManage.userManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.userManageRec$", 			messageSource.getMessage("admin.menu.li.userManage.userManageRec", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.authyManage$", 				messageSource.getMessage("admin.menu.li.userManage.authyManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.allowableRangeManage$", 	messageSource.getMessage("admin.menu.li.userManage.allowableRangeManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.comboList$", 				messageSource.getMessage("admin.menu.li.userManage.comboList", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.playerSetting$", 					messageSource.getMessage("header.menu.label.playerSetting", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.playerUnderMenu$", 				messageSource.getMessage("header.menu.label.playerUnderMenu", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.playersectionMenu$", 				messageSource.getMessage("header.menu.label.playersectionMenu", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.downloadApproveInit$", 			messageSource.getMessage("header.menu.label.downloadApproveInit", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.usageScreen$", 				messageSource.getMessage("admin.menu.li.userManage.usageScreen", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.userDefinition$", 			messageSource.getMessage("admin.menu.li.userManage.userDefinition", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.li.userManage.approveList$", 				messageSource.getMessage("admin.menu.li.userManage.approveList", null,Locale.getDefault())));

								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.evaluation$", 						messageSource.getMessage("header.menu.label.evaluation", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.itemManage$", 						messageSource.getMessage("header.menu.label.itemManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.sheetManage$", 					messageSource.getMessage("header.menu.label.sheetManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.campaignManage$", 					messageSource.getMessage("header.menu.label.campaignManage", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.evaluating$", 						messageSource.getMessage("header.menu.label.evaluating", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.evaluationStatistics$", 			messageSource.getMessage("header.menu.label.evaluationStatistics", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.evaluationList$", 					messageSource.getMessage("header.menu.label.evaluationList", null,Locale.getDefault())));
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.transcript$", 						messageSource.getMessage("header.menu.label.transcript", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.learningTranscript$", 				messageSource.getMessage("header.menu.label.learningTranscript", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.sttModel$", 						messageSource.getMessage("header.menu.label.sttModel", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.sttEnginState$", 					messageSource.getMessage("header.menu.label.sttEnginState", null,Locale.getDefault())));
								eElement.setTextContent(eElement.getTextContent().replaceAll("^header.menu.label.sttResultManage$", 				messageSource.getMessage("header.menu.label.sttResultManage", null,Locale.getDefault())));
								
								eElement.setTextContent(eElement.getTextContent().replaceAll("^admin.menu.schedulerManage$", 						messageSource.getMessage("admin.menu.schedulerManage", null,Locale.getDefault())));
							}
						}
					}
					
					if (menuAccessList!=null && menuAccessListTotal>0){
						for(int seqMenu = 0; seqMenu < menuAccessListTotal; seqMenu++) {
							MMenuAccessInfo menuItem = menuAccessList.get(seqMenu);

							// authy_init.xml 제일 마지막 programeCode 위치
							// 그리드를 보면 colspan으로 위아래 2겹씩 묶어 놓았기 때문에, 순서가 5, 7, 9, ... 로 나간다.
							if(menuItem.getProgramCode().equals(((Element)nList.item(39)).getTextContent())) {
									
								if(nRowList.item(row).getAttributes().getNamedItem("range") != null && "1".equals(nRowList.item(row).getAttributes().getNamedItem("range").getNodeValue()))  {
									((Element)nList.item(5)).setTextContent(menuItem.getAccessLevel());
								} else {
									((Element)nList.item(5)).setTextContent(" ");
								}
								if(menuItem.getReadYn().equals("Y"))
									((Element)nList.item(7)).setTextContent("1");
								else
									((Element)nList.item(7)).setTextContent("0");

								if(menuItem.getWriteYn().equals("Y"))
									((Element)nList.item(9)).setTextContent("1");
								else
									((Element)nList.item(9)).setTextContent("0");

								if(menuItem.getModiYn().equals("Y"))
									((Element)nList.item(11)).setTextContent("1");
								else
									((Element)nList.item(11)).setTextContent("0");

								if(menuItem.getDelYn().equals("Y"))
									((Element)nList.item(13)).setTextContent("1");
								else
									((Element)nList.item(13)).setTextContent("0");

								if(menuItem.getListenYn().equals("Y"))
									((Element)nList.item(15)).setTextContent("1");
								else
									((Element)nList.item(15)).setTextContent("0");

								if(menuItem.getDownloadYn().equals("Y"))
									((Element)nList.item(17)).setTextContent("1");
								else
									((Element)nList.item(17)).setTextContent("0");

								if(menuItem.getExcelYn().equals("Y"))
									((Element)nList.item(19)).setTextContent("1");
								else
									((Element)nList.item(19)).setTextContent("0");

								if(menuItem.getMaskingYn().equals("Y"))
									((Element)nList.item(21)).setTextContent("1");
								else
									((Element)nList.item(21)).setTextContent("0");

								if(menuItem.getPrereciptYn().equals("Y"))
									((Element)nList.item(23)).setTextContent("1");
								else
									((Element)nList.item(23)).setTextContent("0");

								if(menuItem.getReciptYn().equals("Y"))
									((Element)nList.item(25)).setTextContent("1");
								else
									((Element)nList.item(25)).setTextContent("0");

								if(menuItem.getApproveYn().equals("Y"))
									((Element)nList.item(27)).setTextContent("1");
								else
									((Element)nList.item(27)).setTextContent("0");

								if(menuItem.getDownloadApprove().equals("Y"))
									((Element)nList.item(29)).setTextContent("1");
								else
									((Element)nList.item(29)).setTextContent("0");

								if(menuItem.getEncYn().equals("Y"))
									((Element)nList.item(31)).setTextContent("1");
								else
									((Element)nList.item(31)).setTextContent("0");
								
								if(menuItem.getUploadYn() != null && menuItem.getUploadYn().equals("Y"))
									((Element)nList.item(33)).setTextContent("1");
								else
									((Element)nList.item(33)).setTextContent("0");
								
								if(menuItem.getBestcallYn() != null && menuItem.getBestcallYn().equals("Y"))
									((Element)nList.item(35)).setTextContent("1");
								else
									((Element)nList.item(35)).setTextContent("0");
								
								if(menuItem.getFeedbackModifyYn() != null && menuItem.getFeedbackModifyYn().equals("Y"))
									((Element)nList.item(37)).setTextContent("1");
								else
									((Element)nList.item(37)).setTextContent("0");

							}
							if(!nowAccessInfo.getModiYn().equals("Y")) {
								((Element)nList.item(5)).setAttribute("disabled", "1");
								((Element)nList.item(7)).setAttribute("disabled", "1");
								((Element)nList.item(9)).setAttribute("disabled", "1");
								((Element)nList.item(11)).setAttribute("disabled", "1");
								((Element)nList.item(13)).setAttribute("disabled", "1");
								((Element)nList.item(15)).setAttribute("disabled", "1");
								((Element)nList.item(17)).setAttribute("disabled", "1");
								((Element)nList.item(19)).setAttribute("disabled", "1");
								((Element)nList.item(21)).setAttribute("disabled", "1");
								((Element)nList.item(23)).setAttribute("disabled", "1");
								((Element)nList.item(25)).setAttribute("disabled", "1");
								((Element)nList.item(27)).setAttribute("disabled", "1");
								((Element)nList.item(29)).setAttribute("disabled", "1");
								((Element)nList.item(31)).setAttribute("disabled", "1");
								((Element)nList.item(33)).setAttribute("disabled", "1");
								((Element)nList.item(35)).setAttribute("disabled", "1");
								((Element)nList.item(37)).setAttribute("disabled", "1");
							}
						}
					} else {
						if(!nowAccessInfo.getModiYn().equals("Y")) {
							((Element)nList.item(5)).setAttribute("disabled", "1");
							((Element)nList.item(7)).setAttribute("disabled", "1");
							((Element)nList.item(9)).setAttribute("disabled", "1");
							((Element)nList.item(11)).setAttribute("disabled", "1");
							((Element)nList.item(13)).setAttribute("disabled", "1");
							((Element)nList.item(15)).setAttribute("disabled", "1");
							((Element)nList.item(17)).setAttribute("disabled", "1");
							((Element)nList.item(19)).setAttribute("disabled", "1");
							((Element)nList.item(21)).setAttribute("disabled", "1");
							((Element)nList.item(23)).setAttribute("disabled", "1");
							((Element)nList.item(25)).setAttribute("disabled", "1");
							((Element)nList.item(27)).setAttribute("disabled", "1");
							((Element)nList.item(29)).setAttribute("disabled", "1");
							((Element)nList.item(31)).setAttribute("disabled", "1");
							((Element)nList.item(33)).setAttribute("disabled", "1");
							((Element)nList.item(35)).setAttribute("disabled", "1");
							((Element)nList.item(37)).setAttribute("disabled", "1");
						}
					}
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();

				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
				DOMSource source = new DOMSource(authyDoc);

				StringWriter sw = new StringWriter();
				StreamResult result = new StreamResult(sw);
				transformer.transform(source, result);

				rstValue = sw.toString();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				Logger.error("", "", "", e.toString());
				logger.error("error",e);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				Logger.error("", "", "", e.toString());
				logger.error("error",e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.error("", "", "", e.toString());
				logger.error("error",e);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				Logger.error("", "", "", e.toString());
				logger.error("error",e);
			}
		}
		return rstValue;
	}
	
	
	// 허용 범위 목록
	@RequestMapping(value = "/allowable_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml allowableRange(HttpServletRequest request, HttpServletResponse response){

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
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				for(int j = 0; j < 5; j++){
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
						column.setWidth("*");
						column.setId("bgName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 중분류
					case 2:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("mgName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 소분류
					case 3:
	//					column.setWidth("100");
						column.setWidth("*");
						column.setId("sgName");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault())+"</div>");
						break;
	
						// 삭제
					case 4:
						column.setWidth("*");
						column.setSort("na");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("message.btn.del", null,Locale.getDefault()) + "</div>");
						break;
					}
	
					head.getColumnElement().add(column);
					column = null;
	
				}
				xmls.setHeadElement(head);
			}else {
				
				List<AllowableRangeInfo> allowableList = null;
				int allowableListTotal = 0;
				
		        if(request.getParameter("allowableCode") != null && !request.getParameter("allowableCode").equals("")) {
		        	AllowableRangeInfo allowableRangeInfoChk = new AllowableRangeInfo();
		        	allowableRangeInfoChk.setrAllowableCode(request.getParameter("allowableCode"));
		        	allowableList = allowableRangeInfoService.selectAllowableRangeInfo(allowableRangeInfoChk);
		        	if(allowableList!=null)
		        		allowableListTotal = allowableList.size();
		        }
		        
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
	
				for(int i = 0; i < allowableListTotal; i++) {
					AllowableRangeInfo allowableRangeItem = allowableList.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					if ("".equals(allowableRangeItem.getrSgCode()) || StringUtil.isNull(allowableRangeItem.getrSgCode(),true)) {
						if ("".equals(allowableRangeItem.getrMgCode()) || StringUtil.isNull(allowableRangeItem.getrMgCode(),true)) {
							rowItem.setId(allowableRangeItem.getrBgCode());
						}else {
							rowItem.setId(allowableRangeItem.getrBgCode()+"_"+allowableRangeItem.getrMgCode());
						}
					}else {
						rowItem.setId(allowableRangeItem.getrBgCode()+"_"+allowableRangeItem.getrMgCode()+"_"+allowableRangeItem.getrSgCode());
					}
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
	
					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(allowableRangeItem.getrBgName());
					rowItem.getCellElements().add(cellInfo);
	
					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(allowableRangeItem.getrMgName());
					rowItem.getCellElements().add(cellInfo);
	
					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(allowableRangeItem.getrSgName());
					rowItem.getCellElements().add(cellInfo);
					
					// 수정 버튼
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class=\"ui_btn_white icon_btn_trash_gray\" onclick='rangeDelete(\""+rowItem.getId()+"\")'></button>");
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
	
					rowItem = null;
				}
			}
		}
		return xmls;
	}
	
	@RequestMapping(value = "/mobile_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml mobileManageGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "userManage.mobileManage");
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.subNumber.title.";
			for( int j = 0; j < 8; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else {
					column.setType("ro");
					column.setSort("str");
				}

				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("100");


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
					column.setCache("0");
					column.setWidth("200");
					column.setValue("id"/*messageSource.getMessage("admin.subNumber.label.telNo", null,Locale.getDefault())*/);
					break;
				case 2:
					column.setWidth("200");
					column.setValue("name"/*messageSource.getMessage("admin.subNumber.label.nickName", null,Locale.getDefault())*/);
					break;
				case 3:
					column.setWidth("200");
					column.setValue("phone"/*messageSource.getMessage("admin.subNumber.label.nickName", null,Locale.getDefault())*/);
					break;
				case 4:
					column.setWidth("200");
					column.setValue("로그파일요청");
					break;
				case 5:
					column.setWidth("200");
					column.setValue("로그레벨설정");
					break;
				case 6:
					column.setWidth("200");
					column.setValue("파일업로드요청");
					break;
				/*case 7:
					column.setWidth("*");
					column.setValue("");
					break;*/
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
			headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter,,,");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			RUserInfo ruserInfo = new RUserInfo();
			
			List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
			Integer ruserInfoResultTotal = ruserInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < ruserInfoResultTotal; i++) {
				RUserInfo rsuerInfoResultItem = ruserInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(rsuerInfoResultItem.getUserId());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(rsuerInfoResultItem.getUserName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(rsuerInfoResultItem.getUserPhone());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='ui_main_btn_flat' onClick='reqLogFile(\""+String.valueOf(i+1)+"\")'>로그파일요청</button>");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='ui_main_btn_flat' onClick='reqLogLevel(\""+String.valueOf(i+1)+"\")'>로그레벨설정</button>");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='ui_main_btn_flat' onClick='reqFileUpload(\""+String.valueOf(i+1)+"\")'>파일업로드요청</button>");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	
	@RequestMapping(value = "/mobileFileUpload_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml mobileFileUploadGrid(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "userManage.mobileManage");
		if (userInfo != null) {
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			//String titleBaseName = "management.subNumber.title.";
			for( int j = 0; j < 5; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				
				if(j == 0 ) {
					column.setType("ch");
				} else {
					column.setType("ro");
					column.setSort("str");
				}
				
				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("100");
				
				
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
					column.setCache("0");
					column.setWidth("200");
					column.setValue("date"/*messageSource.getMessage("admin.subNumber.label.telNo", null,Locale.getDefault())*/);
					break;
				case 2:
					column.setWidth("200");
					column.setValue("time"/*messageSource.getMessage("admin.subNumber.label.nickName", null,Locale.getDefault())*/);
					break;
				case 3:
					column.setWidth("200");
					column.setValue("custPhone"/*messageSource.getMessage("admin.subNumber.label.nickName", null,Locale.getDefault())*/);
					break;
				case 4:
					column.setWidth("200");
					column.setValue("userId");
					break;
					/*case 7:
					column.setWidth("*");
					column.setValue("");
					break;*/
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
			headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter,#text_filter");
			
			afterInit.getCallElement().add(headCall);
			
			head.setAfterElement(afterInit);
			
			xmls.setHeadElement(head);
			
			SearchListInfo searchListInfo = new SearchListInfo();
			searchListInfo.setUserId(request.getParameter("rUserId"));
			List<SearchListInfo> searchListInfoResult = searchListInfoService.selectFileUploadListInfo(searchListInfo);
			Integer searchListInfoResultTotal = searchListInfoResult.size();
			
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			for(int i = 0; i < searchListInfoResultTotal; i++) {
				SearchListInfo searchListInfoResultItem = searchListInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(searchListInfoResultItem.getRecDate());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(searchListInfoResultItem.getRecTime());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(searchListInfoResultItem.getCustPhone1());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(searchListInfoResultItem.getUserId());
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				
				rowItem = null;
			}
		}
		return xmls;
	}
	// 실감 도면 모니터링 전화기 사용자 조회 그리드
	@RequestMapping(value = "/userRealTimeRecManageGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml userRealTimeRecManageGrid(HttpServletRequest request){

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


					// 사용자 이름
				case 0:
					column.setWidth("100");
					column.setValue("<div style=\"text-align:center;\">성함</div>");
					break;

					// 사용자 ID
				case 1:
					column.setWidth("100");
					column.setValue("<div style=\"text-align:center;\">사번</div>");
					break;

				// 내선번호
				case 2:
					column.setWidth("100");
					column.setValue("<div style=\"text-align:center;\">내선번호</div>");
					break;


					// 대분류
				case 3:
					column.setWidth("100");
					column.setType("combo");
					//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault())+"</div>");
					break;

					// 중분류
				case 4:
					column.setWidth("100");
					column.setType("combo");
					//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault())+"</div>");
					break;

					// 소분류
				case 5:
					column.setWidth("100");
					column.setType("combo");
					//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault())+"</div>");
					break;
				}

				head.getColumnElement().add(column);
				column = null;

			}
			head.setAfterElement(new dhtmlXGridHeadAfterInit());
			dhtmlXGridHeadAfterInit afterInit = new dhtmlXGridHeadAfterInit();

			afterInit.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

			head.setAfterElement(afterInit);
			xmls.setHeadElement(head);

			RUserInfo ruserInfo = new RUserInfo();

			if(request.getParameter("userName") != null) {
				ruserInfo.setUserName(request.getParameter("userName"));
			}
			if(request.getParameter("userId") != null) {
				ruserInfo.setUserId(request.getParameter("userId"));
			}
			
			if(request.getParameter("bgCode") != null) {
				ruserInfo.setBgCode(request.getParameter("bgCode"));
			}
			
			if(request.getParameter("mgCode") != null) {
				ruserInfo.setMgCode(request.getParameter("mgCode"));
			}
			
			if(request.getParameter("sgCode") != null) {
				ruserInfo.setSgCode(request.getParameter("sgCode"));
			}
			
			List<RUserInfo> ruserInfoResult = ruserInfoService.adminUserManageSelect(ruserInfo);
			Integer ruserInfoResultTotal = ruserInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < ruserInfoResultTotal; i++) {
				RUserInfo ruserItem = ruserInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// 사용자 이름
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(ruserItem.getUserName().trim());
				rowItem.getCellElements().add(cellInfo);

				// 사용자 아이디
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(ruserItem.getUserId().toString());
				rowItem.getCellElements().add(cellInfo);

				// 내선번호
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getExtNo(),true))
					cellInfo.setValue(" ");
				else
					cellInfo.setValue(ruserItem.getExtNo().trim());
				rowItem.getCellElements().add(cellInfo);

				// 대분류
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getBgName(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getBgName().trim());
				rowItem.getCellElements().add(cellInfo);

				// 중분류
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getMgName(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getMgName().trim());
				rowItem.getCellElements().add(cellInfo);

				// 소분류
				cellInfo = new dhtmlXGridRowCell();
				if(StringUtil.isNull(ruserItem.getSgName(),true))
					cellInfo.setValue("");
				else
					cellInfo.setValue(ruserItem.getSgName().trim());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}


	// 실감 도면모니터링 유저 선택 부분 그리드  -   공유 기능
	@RequestMapping(value = "/userRealTimeManageGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml userRealTimeManageGrid(HttpServletRequest request){
	
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
	
		if (userInfo != null) {
	
			xmls = new dhtmlXGridXml();
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
				xmls.setHeadElement(new dhtmlXGridHead());
	
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				for(int j = 0; j < 11; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
	
					column.setType("ro");
					column.setSort("na");
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
						column.setCache("0");
						column.setFiltering("0");
						column.setValue("<div id='allcheck' style='cursor: pointer; position: relative; top: 2px'><img class='all_chk_btn_false'/></div>");
						break;
						// 사용자 이름
					case 1:
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">사용자 이름</div>");
						break;
	
						// 사용자 ID
					case 2:
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">사번</div>");
						break;
						
						// 내선번호
					case 3:
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">내선번호</div>");
						column.setHidden("1");
						break;
	
						// 대분류
					case 4:
						column.setWidth("100");
						column.setType("combo");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=bgCode");
						column.setValue("<div style=\"text-align:center;\">대분류</div>");
						break;
	
						// 중분류
					case 5:
						column.setWidth("100");
						column.setType("combo");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=mgCode");
						column.setValue("<div style=\"text-align:center;\">중분류</div>");
						break;
	
						// 소분류
					case 6:
						column.setWidth("100");
						column.setType("combo");
						//column.setSource(request.getContextPath() + "/opt/organization_combo_option.xml?comboType=sgCode");
						column.setValue("<div style=\"text-align:center;\">소분류</div>");
						break;
	
						// 사원번호
					case 7:
						column.setWidth("180");
						column.setValue("<div style=\"text-align:center;\">사원번호</div>");
						column.setHidden("1");
						break;
	
						//  대분류 코드
					case 8:
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
						break;
						// 중분 류 코드 
					case 9:
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");
						break;
						// 소분류 코드 
					case 10:
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">소분류 코드</div>");
						break;
					}
	
					head.getColumnElement().add(column);
					column = null;
	
				}
	
				xmls.setHeadElement(head);
			}else {
				RUserInfo ruserInfo = new RUserInfo();
				
				List<RUserInfo> ruserInfoResult  = null;
				
				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					ruserInfo.setPosStart(posStart);
				}
				Integer count = 100;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("count"));
					ruserInfo.setCount(count);
				}
				
				if(request.getParameter("userName") != null) {
					ruserInfo.setUserName(request.getParameter("userName"));
				}
				if(request.getParameter("userId") != null) {
					ruserInfo.setUserId(request.getParameter("userId"));
				}
				
				if(request.getParameter("bgCode") != null) {
					ruserInfo.setBgCode(request.getParameter("bgCode"));
				}
				
				if(request.getParameter("mgCode") != null) {
					ruserInfo.setMgCode(request.getParameter("mgCode"));
				}
				
				if(request.getParameter("sgCode") != null) {
					ruserInfo.setSgCode(request.getParameter("sgCode"));
				}
				
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("CALLCENTER");
				etcConfigInfo.setConfigKey("CALLCENTER");
				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				
				String[] lista = etcConfigResult.get(0).getConfigValue().split(",");
				List<String> list = new ArrayList<String>();
				
				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}
				
				ruserInfo.setList(list);
				ruserInfo.setrShareName(URLDecoder.decode(request.getParameter("shareName")));
				
				ruserInfoResult = ruserInfoService.adminAUserManageRealTimeSelect(ruserInfo);
			
				Integer ruserInfoResultTotal = ruserInfoResult.size();
	
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				
				
				for(int i = 0; i < ruserInfoResultTotal; i++) {
					RUserInfo ruserItem = ruserInfoResult.get(i);
	
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(posStart+i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
					// 체크
					cellInfo = new dhtmlXGridRowCell();
					if(ruserItem.getrShareYn().equals("N")) {
						cellInfo.setValue("0");
					}else {
						cellInfo.setValue("1");
					}
					rowItem.getCellElements().add(cellInfo);
	
					// 사용자 이름
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(ruserItem.getUserName().trim());
					rowItem.getCellElements().add(cellInfo);
	
					// 사용자 아이디
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(ruserItem.getUserId().toString());
					rowItem.getCellElements().add(cellInfo);
	
					// 내선번호
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getExtNo(),true))
						cellInfo.setValue(" ");
					else
						cellInfo.setValue(ruserItem.getExtNo().trim());
					rowItem.getCellElements().add(cellInfo);
	
					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getBgName(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getBgName().trim());
					rowItem.getCellElements().add(cellInfo);
	
					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getMgName(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getMgName().trim());
					rowItem.getCellElements().add(cellInfo);
	
					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getSgName(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getSgName().trim());
					rowItem.getCellElements().add(cellInfo);
	
					// 사원번호
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getEmpId(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getEmpId().trim());
					rowItem.getCellElements().add(cellInfo);
	
					// 대분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getBgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getBgCode().trim());
					rowItem.getCellElements().add(cellInfo);
					
					// 중분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getMgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getMgCode().trim());
					rowItem.getCellElements().add(cellInfo);
					
					// 소분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(ruserItem.getSgCode(),true))
						cellInfo.setValue("");
					else
						cellInfo.setValue(ruserItem.getSgCode().trim());
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
	
					rowItem = null;
					
				}	
					Integer totalListResult = ruserInfoService.CountadminAUserManageSelect(ruserInfo);
					if( totalListResult > 0 &&  (request.getParameter("posStart")==null||"0".equals(request.getParameter("posStart")))) {
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
		}
		return xmls;
	}
}// end