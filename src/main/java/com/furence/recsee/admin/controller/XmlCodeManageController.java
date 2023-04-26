package com.furence.recsee.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.CommonCodeService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;

@Controller
public class XmlCodeManageController {


	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private MessageSource messageSource;

	// 코드 관리 그리드
	@RequestMapping(value = "/codeManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml codeManage(HttpServletRequest request, HttpServletResponse response){
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 5; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setEditable("0");
				column.setCache("0");
				column.setAlign("center");

				switch(j) {
				case 0:
					column.setWidth("200");
					column.setValue(messageSource.getMessage("admin.label.groupKey", null,Locale.getDefault()));
					break;
				case 1:
					column.setWidth("200");
					column.setValue(messageSource.getMessage("admin.label.etcKey", null,Locale.getDefault()));
					break;
				case 2:
					column.setWidth("*");
					column.setValue(messageSource.getMessage("admin.label.etcValue", null,Locale.getDefault()));
					break;
				case 3:
					column.setWidth("*");
					column.setValue("설명");
					break;
				case 4:
					column.setHidden("1");
					column.setWidth("*");
					column.setValue("옵션");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			dhtmlXGridRow rowItem = new dhtmlXGridRow();

			rowItem = new dhtmlXGridRow();
			// 코드명
			rowItem.setId("4");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("EXPIRATION PERIOD");
			rowItem.getCellElements().add(cellInfo);

			// 코드 이름
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(messageSource.getMessage("admin.grid.listenDownloadEnd", null,Locale.getDefault())/*"청취 다운로드 요청 만료기간"*/);
			rowItem.getCellElements().add(cellInfo);

			// 코드
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("expirationDay");
			rowItem.getCellElements().add(cellInfo);
			xmls.getRowElements().add(rowItem);
		}
		return xmls;
	}

	// 대분류 관리 그리드
	@RequestMapping(value = "/oraganizationManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml oraganizationManage(HttpServletRequest request, HttpServletResponse response){
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					// 플레이어 속성 값
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.bgCode", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.bgName", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 2:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.useYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			OrganizationInfo organizationInfo = new OrganizationInfo();
			organizationInfo.setType("Y");
			organizationInfo.setNotIvr("Y");
			List<OrganizationInfo> bgSelect = organizationInfoService.selectOrganizationBgInfo(organizationInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			if(bgSelect.size() > 0) {
				for(int i = 0; i < bgSelect.size(); i++) {

					OrganizationInfo bgColumn = bgSelect.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 대분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getrBgCode()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(bgColumn.getrBgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getrBgName()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(bgColumn.getrBgName().trim());
					rowItem.getCellElements().add(cellInfo);


					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getUseYn()) || bgColumn.getUseYn().equals("N") )
						cellInfo.setValue("0");
					else
						cellInfo.setValue("1");
					rowItem.getCellElements().add(cellInfo);

					// 로우 추가
					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}
		return xmls;
	}

	//콜센터 중분류
	@RequestMapping(value = "/callCenterMg.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml callCenterMg(HttpServletRequest request, HttpServletResponse response){
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					// 플레이어 속성 값
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.mgCode", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.mgName", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 2:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.useYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			OrganizationInfo organizationInfo = new OrganizationInfo();

			organizationInfo.setType("Y");
			List<OrganizationInfo> mgSelect = organizationInfoService.selectCallCenterMgInfo(organizationInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			if(mgSelect.size() > 0) {
				for(int i = 0; i < mgSelect.size(); i++) {

					OrganizationInfo mgColumn = mgSelect.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 대분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(mgColumn.getrMgCode()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(mgColumn.getrMgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(mgColumn.getrMgName()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(mgColumn.getrMgName().trim());
					rowItem.getCellElements().add(cellInfo);


					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(mgColumn.getUseYn()) || mgColumn.getUseYn().equals("N") )
						cellInfo.setValue("0");
					else
						cellInfo.setValue("1");
					rowItem.getCellElements().add(cellInfo);

					// 로우 추가
					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}
		return xmls;
	}



	// 플레이어 코드 관리 그리드
	@RequestMapping(value = "/playerCodeManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml playerCodeManage(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 4; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					// 플레이어 속성 값
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.menuName1", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.menuName2", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 2:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.menuName3", null,Locale.getDefault()));
						break;
//					// 플레이어 속성 값
//					case 3:
//						column.setWidth("*");
//						column.setValue("설명");
//						break;
					// 플레이어 속성 값
					case 3:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("message.btn.modify", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			CommonCodeVO commonCode = new CommonCodeVO();
			commonCode.setParentCode("player");
			List<CommonCodeVO> codeList = commonCodeService.selectCommonCode(commonCode);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			if(codeList.size() > 0) {
				for(int i = 0; i < codeList.size(); i++) {
					CommonCodeVO commonCodeItem = codeList.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 속성값
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(commonCodeItem.getCodeValue()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(commonCodeItem.getCodeValue().trim());
					rowItem.getCellElements().add(cellInfo);

					// 현재이름
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(commonCodeItem.getCodeName()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(commonCodeItem.getCodeName().trim());
					rowItem.getCellElements().add(cellInfo);

					// 변경이름
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<input id='changeCodeInput' type='text' maxlength='10' placeholder='" + messageSource.getMessage("admin.label.modiCode", null,Locale.getDefault())+ "'></input>");
					rowItem.getCellElements().add(cellInfo);

//					// 설명
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue("");
//					rowItem.getCellElements().add(cellInfo);

					// 버튼
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button id='changeItem' class='ui_btn_white ui_main_btn_flat'>"+messageSource.getMessage("message.btn.modify", null,Locale.getDefault()) + "</button>");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}
		return xmls;
	}



	//다운로드 청취 요청 기간 설정
	@RequestMapping(value = "/expirationDay.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml expirationDay(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.expirationDay1", null,Locale.getDefault()));
						break;
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.expirationDay2", null,Locale.getDefault()));
						break;
					case 2:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("message.btn.modify", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("APPROVE");
			etcConfigInfo.setConfigKey("DUE_DATE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			if(etcConfigResult.size() > 0) {
				for(int i = 0; i < etcConfigResult.size(); i++) {
					EtcConfigInfo commonCodeItem = etcConfigResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 현재기간
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(commonCodeItem.getConfigValue()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(commonCodeItem.getConfigValue().trim());
					rowItem.getCellElements().add(cellInfo);

					// 변경기간
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<input id='changeCodeInput' class='inputFilter numberFilter forday' type='text' maxlength='2' placeholder='" + messageSource.getMessage("admin.label.modiCode", null,Locale.getDefault()) + "'></input>");
					rowItem.getCellElements().add(cellInfo);


					// 버튼
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button id='changeItemForexpirationDay' class='ui_btn_white ui_main_btn_flat'>" + messageSource.getMessage("message.btn.modify", null,Locale.getDefault()) + "</button>");
//					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}
		return xmls;
	}

	//콜센터 선택
	@RequestMapping(value = "/callCenterCodeManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml callCenterCodeManage(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					// 플레이어 속성 값
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.bgCode", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.bgName", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 2:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.useCallcenterYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			OrganizationInfo organizationInfo = new OrganizationInfo();
			organizationInfo.setNotIvr("Y");
			List<OrganizationInfo> bgSelect = organizationInfoService.selectOrganizationBgInfo(organizationInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("CALLCENTER");
			etcConfigInfo.setConfigKey("CALLCENTER");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			String target = etcConfigResult.get(0).getConfigValue();


			if(bgSelect.size() > 0) {
				for(int i = 0; i < bgSelect.size(); i++) {

					OrganizationInfo bgColumn = bgSelect.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 대분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getrBgCode()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(bgColumn.getrBgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getrBgName()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(bgColumn.getrBgName().trim());
					rowItem.getCellElements().add(cellInfo);


					cellInfo = new dhtmlXGridRowCell();
					if(target.contains(bgColumn.getrBgCode()))
						cellInfo.setValue("1");
					else
						cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);

					// 로우 추가
					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}
		return xmls;
	}

	//채널 타입 선정
	@RequestMapping(value = "/channalTypeCodeManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml channalTypeCodeManage(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.recStartType", null,Locale.getDefault()));
						break;
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.recStartTypeCode", null,Locale.getDefault()));
						column.setHidden("1");
						break;
					case 2:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.useYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);


			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("START_TYPE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			String target = etcConfigResult.get(0).getConfigValue();


			dhtmlXGridRow rowItem = new dhtmlXGridRow();

			rowItem.setId("1");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("전수녹취");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("A");
			rowItem.getCellElements().add(cellInfo);


			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("A"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);


			rowItem = new dhtmlXGridRow();
			rowItem.setId("2");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("선택녹취");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("S");
			rowItem.getCellElements().add(cellInfo);


			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("S"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);

			rowItem = new dhtmlXGridRow();
			rowItem.setId("3");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("부분녹취");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("P");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("P"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);

			rowItem = new dhtmlXGridRow();
			rowItem.setId("4");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("전체");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("T");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("T"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);


			// 로우 추가
			xmls.getRowElements().add(rowItem);

			rowItem = null;
		}
		return xmls;
	}

	//마스킹 관리 선정
	@RequestMapping(value = "/maskingManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml MaskingManage(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.maskingColumn", null,Locale.getDefault()));
						break;
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.grid.columnCode", null,Locale.getDefault()));
						column.setHidden("1");
						break;
					case 2:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.useYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);


			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("MASKING_INFO");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			String target = etcConfigResult.get(0).getConfigValue();


			dhtmlXGridRow rowItem = new dhtmlXGridRow();

			rowItem.setId("1");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("고객명");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("r_cust_name");
			rowItem.getCellElements().add(cellInfo);


			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("r_cust_name"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);


			rowItem = new dhtmlXGridRow();
			rowItem.setId("2");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("고객 전화번호");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("r_cust_phone1");
			rowItem.getCellElements().add(cellInfo);


			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("r_cust_phone1"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);

			rowItem = new dhtmlXGridRow();
			rowItem.setId("3");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("전환번호");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("r_cust_phone2");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			if(target.contains("r_cust_phone2"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);

			rowItem = null;
		}
		return xmls;
	}

	//청취, 다운로드 요청 직접 신청 그룹관리
	@RequestMapping(value = "/directGroupManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml directGroupManage(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 3; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					// 플레이어 속성 값
					case 0:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.bgCode", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 1:
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.bgName", null,Locale.getDefault()));
						break;
					// 플레이어 속성 값
					case 2:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.useYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			OrganizationInfo organizationInfo = new OrganizationInfo();
			organizationInfo.setType("Y");
			organizationInfo.setNotIvr("Y");
			List<OrganizationInfo> bgSelect = organizationInfoService.selectOrganizationBgInfo(organizationInfo);

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("APPROVE");
			etcConfigInfo.setConfigKey("DIRECTGROUP");
			List<EtcConfigInfo> etcCallCenterDirect = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			if(bgSelect.size() > 0) {
				for(int i = 0; i < bgSelect.size(); i++) {

					OrganizationInfo bgColumn = bgSelect.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 대분류 코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getrBgCode()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(bgColumn.getrBgCode().trim());
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(bgColumn.getrBgName()))
						cellInfo.setValue("");
					else
						cellInfo.setValue(bgColumn.getrBgName().trim());
					rowItem.getCellElements().add(cellInfo);


					cellInfo = new dhtmlXGridRowCell();
					if(!StringUtil.isNull(bgColumn.getrBgCode()) && etcCallCenterDirect.get(0).getConfigValue().indexOf(bgColumn.getrBgCode())>-1 )
						cellInfo.setValue("1");
					else
						cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);

					// 로우 추가
					xmls.getRowElements().add(rowItem);

					rowItem = null;
				}
			}
		}
		return xmls;
	}
	
	//플레이어 다운로드 설정
	@RequestMapping(value = "/playerDownloadManage.xml", produces = "application/xml")
	public @ResponseBody dhtmlXGridXml playerDownloadManage(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j = 0; j < 2; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");

				switch (j) {
					case 0:
						column.setWidth("*");
						column.setValue("구분");
						break;
					case 1:
						column.setType("ch");
						column.setWidth("*");
						column.setValue(messageSource.getMessage("admin.label.useYn", null,Locale.getDefault()));
						break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);


			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("PLAYER_PERIOD");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			String period = etcConfigResult.get(0).getConfigValue();

			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("PLAYER_PASSWORD");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			String password = etcConfigResult.get(0).getConfigValue();

			dhtmlXGridRow rowItem = new dhtmlXGridRow();

			rowItem.setId("PLAYER_PERIOD");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("사용기간 설정");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			if(period.equals("Y"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);


			rowItem = new dhtmlXGridRow();
			rowItem.setId("PLAYER_PASSWORD");
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			// 속성값
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("2차 인증");
			rowItem.getCellElements().add(cellInfo);

			cellInfo = new dhtmlXGridRowCell();
			if(password.equals("Y"))
				cellInfo.setValue("1");
			else
				cellInfo.setValue("0");
			rowItem.getCellElements().add(cellInfo);

			// 로우 추가
			xmls.getRowElements().add(rowItem);

			rowItem = null;
		}
		return xmls;
	}
}
