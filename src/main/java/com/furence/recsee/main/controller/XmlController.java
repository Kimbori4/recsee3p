//package com.furence.recsee.main.controller;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.w3c.dom.Element;
//
//import com.furence.recsee.common.model.EtcConfigInfo;
//import com.furence.recsee.common.model.LoginVO;
//import com.furence.recsee.common.model.MMenuAccessInfo;
//import com.furence.recsee.common.model.OrganizationInfo;
//import com.furence.recsee.common.service.EtcConfigInfoService;
//import com.furence.recsee.common.service.MenuAccessInfoService;
//import com.furence.recsee.common.service.OrganizationInfoService;
//import com.furence.recsee.common.util.ConvertUtil;
//import com.furence.recsee.common.util.ExcelView;
//import com.furence.recsee.common.util.FindOrganizationUtil;
//import com.furence.recsee.common.util.RecSeeUtil;
//import com.furence.recsee.common.util.SessionManager;
//import com.furence.recsee.common.util.XmlUtil;
//import com.furence.recsee.main.model.SearchItemRecord;
//import com.furence.recsee.main.model.SearchItemRecordElement;
//import com.furence.recsee.main.model.SearchListColumnElement;
//import com.furence.recsee.main.model.SearchListInfo;
//import com.furence.recsee.main.model.SearchListRecord;
//import com.furence.recsee.main.model.SearchListRecordElement;
//import com.furence.recsee.main.model.dhtmlXGridCombo;
//import com.furence.recsee.main.model.dhtmlXGridComboOption;
//import com.furence.recsee.main.model.dhtmlXGridHead;
//import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
//import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
//import com.furence.recsee.main.model.dhtmlXGridRow;
//import com.furence.recsee.main.model.dhtmlXGridRowCell;
//import com.furence.recsee.main.model.dhtmlXGridXml;
//import com.furence.recsee.main.service.SearchListInfoService;
//import com.furence.recsee.management.model.CustomizeItemInfo;
//import com.furence.recsee.management.model.CustomizeListInfo;
//import com.furence.recsee.management.model.MultiPartInfo;
//import com.furence.recsee.management.model.RUserInfo;
//import com.furence.recsee.management.service.CustomizeInfoService;
//import com.furence.recsee.management.service.LogInfoService;
//import com.furence.recsee.management.service.RUserInfoService;
//
//@Controller
//@RequestMapping("/search")
//public class XmlController {
//
//	@Autowired
//	private SearchListInfoService searchListInfoService;
//
//	@Autowired
//	private CustomizeInfoService customizeInfoService;
//
//	@Autowired
//	private RUserInfoService ruserInfoService;
//
//	@Autowired
//	private MenuAccessInfoService menuAccessInfoService;
//
//	@Autowired
//	private EtcConfigInfoService etcConfigInfoService;
//
//	@Autowired
//	private OrganizationInfoService organizatinoInfoService;
//
//	@Autowired
//	private LogInfoService logInfoService;
//
//	@Autowired
//	private MessageSource messageSource;
//
//	// 조회 검색 항목
//	@RequestMapping(value="/search_item.xml", method=RequestMethod.GET, produces="application/xml")
//	public @ResponseBody SearchItemRecord search_item(HttpServletRequest request) {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		SearchItemRecord xmls = new SearchItemRecord();
//
//		if(userInfo != null) {
//			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("EXCEPT");
//			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
//			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String consentNoRecodingUse = "N";
//			if(etcConfigResult.size() > 0) {
//				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
//			}
//
//			CustomizeItemInfo customizeItemInfo = new CustomizeItemInfo();
//			if (userInfo != null)
//				customizeItemInfo.setrUserId(userInfo.getUserLevel());
//
//			List<CustomizeItemInfo> searchSettingInfo = customizeInfoService.selectCustomizeItemInfo(customizeItemInfo);
//
//			Integer settingInfoTotal = searchSettingInfo.size();
//			if(settingInfoTotal < 1) {
//				customizeItemInfo.setrUserId("default");
//
//				searchSettingInfo = customizeInfoService.selectCustomizeItemInfo(customizeItemInfo);
//
//				settingInfoTotal = searchSettingInfo.size();
//			}
//
//			if(settingInfoTotal > 0) {
//
//				xmls.setItemElements(new ArrayList<SearchItemRecordElement>());
//
//				/**
//				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
//				 */
//				Map<String, String> searchTemplItem = null;
//				if (settingInfoTotal > 1) {
//					for(int i = 0; i < settingInfoTotal; i++) {
//						if(userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
//							searchTemplItem = searchSettingInfo.get(i).getAllItem();
//					}
//				} else {
//					searchTemplItem = searchSettingInfo.get(0).getAllItem();
//				}
//
//				Iterator<String> keys = searchTemplItem.keySet().iterator();
//
//				Calendar cal = Calendar.getInstance();
//				String currentDate = String.valueOf(cal.get(Calendar.YEAR));
//				currentDate += "-" + ( ( cal.get(Calendar.MONTH) + 1 ) < 10 ? "0" : "" ) + String.valueOf( cal.get(Calendar.MONTH) + 1 );
//				currentDate += "-" + ( cal.get(Calendar.DATE) < 10 ? "0" : "" ) + String.valueOf(cal.get(Calendar.DATE));
//
//				while(keys.hasNext()) {
//					String key = keys.next();
//
//					if(key.equals("rUserId")) continue;
//
//					if (searchTemplItem.get(key) != null) {
//						SearchItemRecordElement item = null;
//
//						switch(searchTemplItem.get(key)) {
//						case "r_rec_date" :
//							item = new SearchItemRecordElement();
//							item.setType("calendar");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_REC_DATE", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(50);
//							item.setName("sDate");
//							item.setClassName("inputLabelIcon inputCalIcon");
//							item.setReadonly(true);
//							item.setInputAlign("center");
//							item.setInputWidth(100);
//							item.setValue(currentDate);
//							item.setPosition("center");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("calendar");
//							item.setName("eDate");
//							item.setClassName("inputCalIcon");
//							item.setLabel("&nbsp;");
//							item.setReadonly(true);
//							item.setInputAlign("center");
//							item.setInputWidth(100);
//							item.setValue(currentDate);
//							item.setPosition("center");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_rec_time" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_REC_TIME", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(50);
//							item.setName("sTime");
//							item.setInputWidth(90);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=Time&comboType2=s&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("eTime");
//							item.setClassName("inputLabelIcon");
//							item.setInputWidth(90);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=Time&comboType2=e&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_rec_rtime" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_REC_RTIME", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(50);
//							item.setName("sRtime");
//							item.setInputWidth(80);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=rTime&comboType2=s&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("eRtime");
//							item.setClassName("inputLabelIcon");
//							item.setInputWidth(80);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=rTime&comboType2=e&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_ext_num" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("extNum");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_EXT_NUM", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(75);
//							item.setMaxLength(15);
//							item.setInputWidth(80);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//
//						case "r_cust_phone1" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("custPhone1");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_CUST_PHONE1", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(130);
//							item.setMaxLength(20);
//							item.setInputWidth(120);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_cust_phone2" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("custPhone2");
//							item.setClassName("inputLabelIcon");
//							item.setMaxLength(20);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_cust_phone3" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("custPhone3");
//							item.setClassName("inputLabelIcon");
//							item.setMaxLength(20);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_call_kind1" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("callKind1");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_CALL_KIND1", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(75);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=callType&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_call_kind2" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("callKind2");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_CALL_KIND2", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(75);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=callType&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_bg_code" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("bgCode");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(75);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/organization_combo_option.xml?comboType=bgCode&subOpt=ALL&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_mg_code" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("mgCode");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(75);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/organization_combo_option.xml?comboType=mgCode&subOpt=ALL&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_sg_code" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("sgCode");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(75);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/organization_combo_option.xml?comboType=sgCode&subOpt=ALL&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_user_name" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("userName");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_USER_NAME", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(80);
//							item.setInputWidth(60);
//							item.setMaxLength(10);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_user_id" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("userId");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_USER_ID", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(70);
//							item.setInputWidth(70);
//							item.setMaxLength(20);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_cust_name" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("custName");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_CUST_NAME", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(110);
//							item.setInputWidth(100);
//							item.setMaxLength(10);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_cust_social_num" :
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName("custSocialNum");
//							item.setClassName("inputLabelIcon");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_CUST_SOCIAL_NUM", null,Locale.getDefault()));
//							item.setLabelAlign("center");
//							item.setLabelWidth(120);
//							item.setInputWidth(120);
//							item.setMaxLength(6);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_buffer1" :
//							item = new SearchItemRecordElement();
//							item.setName("buffer1");
//							if(consentNoRecodingUse.equals("Y")) {
//								item.setType("combo");
//								item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=YN&selectedIdx=0");
//								item.setReadonly(true);
//							} else {
//								item.setType("input");
//							}
//
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_BUFFER1", null,Locale.getDefault()));
//							item.setClassName("inputLabelIcon");
//							item.setLabelAlign("center");
//							item.setLabelWidth(70);
//							item.setInputWidth(70);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_buffer2" :
//						case "r_buffer3" :
//						case "r_buffer4" :
//						case "r_buffer5" :
//						case "r_buffer6" :
//						case "r_buffer7" :
//						case "r_buffer8" :
//						case "r_buffer9" :
//						case "r_buffer10" :
//						case "r_buffer11" :
//						case "r_buffer12" :
//						case "r_buffer13" :
//						case "r_buffer14" :
//						case "r_buffer15" :
//						case "r_buffer16" :
//						case "r_buffer17" :
//						case "r_buffer18" :
//						case "r_buffer19" :
//						case "r_buffer20" :
//
//							item = new SearchItemRecordElement();
//							item.setType("input");
//							item.setName(searchTemplItem.get(key).substring(2));
//							item.setLabel(messageSource.getMessage("views.search.grid.head." + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault()));
//							item.setClassName("inputLabelIcon");
//							item.setLabelAlign("center");
//							item.setLabelWidth(70);
//							item.setInputWidth(100);
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_call_ttime" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setLabel(messageSource.getMessage("views.search.grid.head.R_CALL_TTIME", null,Locale.getDefault()));
//							item.setClassName("inputLabelIcon");
//							item.setLabelAlign("center");
//							item.setLabelWidth(90);
//							item.setName("sTtime");
//							item.setInputWidth(80);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=tTime&comboType2=s&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName("eTtime");
//							item.setInputWidth(80);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=tTime&comboType2=e&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_queue_no1" :
//						case "r_queue_no2" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName(ConvertUtil.convert2CamelCase(searchTemplItem.get(key)));
//							item.setLabel(messageSource.getMessage("views.search.grid.head." + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault()));
//							item.setClassName("inputLabelIcon");
//							item.setLabelAlign("center");
//							item.setLabelWidth(70);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/queue_combo_option.xml?type=ALL&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_selfdis_yn" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName(ConvertUtil.convert2CamelCase(searchTemplItem.get(key)));
//							item.setLabel(messageSource.getMessage("views.search.grid.head." + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault()));
//							item.setClassName("inputLabelIcon");
//							item.setLabelAlign("center");
//							item.setLabelWidth(100);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=YN&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//						case "r_rec_start_type" :
//							item = new SearchItemRecordElement();
//							item.setType("combo");
//							item.setName(ConvertUtil.convert2CamelCase(searchTemplItem.get(key)));
//							item.setLabel(messageSource.getMessage("views.search.grid.head." + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault()));
//							item.setClassName("inputLabelIcon");
//							item.setLabelAlign("center");
//							item.setLabelWidth(100);
//							item.setInputWidth(100);
//							item.setReadonly(true);
//							item.setConnector(request.getContextPath()+"/opt/combo_option.xml?comboType=startType&selectedIdx=0");
//							xmls.getItemElements().add(item);
//
//							item = null;
//
//							item = new SearchItemRecordElement();
//							item.setType("newcolumn");
//							xmls.getItemElements().add(item);
//
//							item = null;
//							break;
//
//						}
//					}
//				}
//				SearchItemRecordElement item = new SearchItemRecordElement();
//				item.setType("button");
//				item.setName("btnSearch");
//				item.setValue(messageSource.getMessage("views.search.button.search", null,Locale.getDefault()));
//				xmls.getItemElements().add(item);
//			}
//		}
//		return xmls;
//	}
//	// 조회 목록
//	@RequestMapping(value="/search_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
//	public @ResponseBody SearchListRecord search_list(HttpServletRequest request) throws MalformedURLException {
//
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
//		SearchListRecord xmls = new SearchListRecord();
//
//		if(userInfo != null) {
//
//			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("PLAYER");
//			etcConfigInfo.setConfigKey("MODE");
//			List<EtcConfigInfo> playerModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String playerMode = "AUDIO";
//			if(playerModeResult.size() > 0) {
//				playerMode = playerModeResult.get(0).getConfigValue();
//			}
//
//			etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("EXCEPT");
//			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
//			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String consentNoRecodingUse = "N";
//			if(etcConfigResult.size() > 0) {
//				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
//			}
//
//			OrganizationInfo organizationInfo = new OrganizationInfo();
//			List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
//			List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
//			List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);
//
//			CustomizeListInfo customizeListInfo = new CustomizeListInfo();
//			if (request.getParameter("mode") != null && !request.getParameter("mode").trim().isEmpty() && request.getParameter("mode").equals("trace")) {
//				customizeListInfo.setrUserId("traceWins".toString());
//			} else {
//				if (userInfo != null) {
//					customizeListInfo.setrUserId(userInfo.getUserLevel());
//				}
//			}
//
//			List<CustomizeListInfo> searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);
//
//			Integer settingInfoTotal = searchSettingInfo.size();
//			if(settingInfoTotal < 1) {
//				customizeListInfo.setrUserId("default");
//
//				searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);
//
//				settingInfoTotal = searchSettingInfo.size();
//			}
//			if(settingInfoTotal > 0) {
//				/**
//				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
//				 */
//				Map<String, String> searchTemplItem = null;
//				if (settingInfoTotal > 1) {
//					for(int i = 0; i < settingInfoTotal; i++) {
//						if(userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
//							searchTemplItem = searchSettingInfo.get(i).getAllItem();
//					}
//				} else {
//					searchTemplItem = searchSettingInfo.get(0).getAllItem();
//				}
//
//				if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
//					String titleBaseName = "views.search.grid.head.";
//
//					xmls.setColumnElements(new ArrayList<SearchListColumnElement>());
//
//					/**
//					 * player를 위한 태그를 sub-row를 이용하여 추가
//					 */
//					SearchListColumnElement column = new SearchListColumnElement();
//					if (request.getParameter("mode") == null || request.getParameter("mode").trim().isEmpty() || !request.getParameter("mode").equals("trace")) {
//						column.setWidth("5");
//						column.setType("sub_row");
//						column.setAlign("center");
//						column.setSort("na");
//						column.setValue("");
//						xmls.getColumnElements().add(column);
//					}
//
//					Iterator<String> keys = searchTemplItem.keySet().iterator();
//					while(keys.hasNext()) {
//						String key = keys.next();
//
//						if(key.equals("rUserId")) continue;
//
//						if (searchTemplItem.get(key) != null) {
//							column = new SearchListColumnElement();
//
//							switch(searchTemplItem.get(key)) {
//							case "r_check_box" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("30");
//								column.setType("ch");
//								column.setAlign("center");
//								column.setSort("na");
//								column.setValue("<div id='allcheck' style=\"text-align:center;cursor:pointer;margin: 0px 0px 0px -10px;\"><img src='"+request.getContextPath()+"/resources/common/images/dhxgrid_web/item_chk0.gif' width='18' height='18'/></div>");
//								break;
//							case "r_cust_phone1" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("150");
//								column.setType("ro");
//								column.setAlign("center");
//								column.setSort("server");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "r_bg_code" :
//							case "r_mg_code" :
//							case "r_sg_code" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("100");
//								column.setType("ro");
//								column.setAlign("center");
//								column.setSort("server");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "r_bg_name" :
//							case "r_mg_name" :
//							case "r_sg_name" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("150");
//								column.setType("ro");
//								column.setAlign("center");
//								column.setSort("server");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "screen" :
//							case "evaluation" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("80");
//								column.setType("txt");
//								column.setAlign("center");
//								column.setSort("na");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "r_queue_no1" :
//							case "r_queue_no2" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("100");
//								column.setType("combo");
//								column.setAlign("center");
//								column.setSource(request.getContextPath() + "/opt/queue_combo_option.xml");
//								column.setSort("na");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "r_selfdis_yn" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("100");
//								column.setType("combo");
//								column.setAlign("center");
//								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN2");
//								column.setSort("na");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "r_rec_start_type" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("100");
//								column.setType("combo");
//								column.setAlign("center");
//								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=startType");
//								column.setSort("na");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							case "trace" :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("100");
//								column.setType("ro");
//								column.setAlign("center");
//								column.setSort("string");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							default :
//								column.setId(searchTemplItem.get(key));
//								column.setWidth("100");
//								column.setType("ro");
//								column.setAlign("center");
//								column.setSort("server");
//								column.setValue("<div style=\"text-align:center;padding-right:10px;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
//								break;
//							}
//							xmls.getColumnElements().add(column);
//						}
//					}
//
//					if(playerMode.equals("SCREEN")) {
//						column = new SearchListColumnElement();
//						column.setId("screen_url");
//						column.setWidth("0");
//						column.setType("ro");
//						column.setSort("na");
//						column.setAlign("center");
//						column.setValue("screen_url");
//						column.setHidden("true");
//
//						xmls.getColumnElements().add(column);
//					}
//					/**
//					 * 녹취 재생 url을 hidden으로 추가 해야 함.
//					 */
//					column = new SearchListColumnElement();
//					column.setId("rec_url");
//					column.setWidth("0");
//					column.setType("ro");
//					column.setSort("na");
//					column.setAlign("center");
//					column.setValue("rec_url");
//					column.setHidden("true");
//
//					xmls.getColumnElements().add(column);
//				}
//
//				xmls.setRowElements(new ArrayList<SearchListRecordElement>());
//
//				SearchListInfo searchListInfo = new SearchListInfo();
//				List<SearchListInfo> searchListResult = null;
//
//				if (request.getParameter("mode") != null && !request.getParameter("mode").trim().isEmpty() && request.getParameter("mode").equals("trace")) {
//
//					if(request.getParameter("file") != null && !request.getParameter("file").trim().isEmpty()) {
//						URL fileUrl = new URL(request.getParameter("file").toString());
//						String fileName = FilenameUtils.getName(fileUrl.getPath());
//
//						searchListInfo.setsDate(fileName.substring(0, 8));
//						searchListInfo.seteDate(fileName.substring(0, 8));
//						searchListInfo.setsTime(fileName.substring(8, 14));
//						searchListInfo.seteTime(fileName.substring(8, 14));
//						searchListInfo.setListenUrl(request.getParameter("file").toString());
//
//						searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
//						if(searchListResult.size() == 1 &&
//							( searchListResult.get(0).getCallId1() != null || searchListResult.get(0).getCallId2() != null || searchListResult.get(0).getCallId3() != null ) ) {
//
//							searchListInfo = new SearchListInfo();
//
//							if(searchListResult.get(0).getCallId1() != null) {
//								searchListInfo.setCallId1(searchListResult.get(0).getCallId1());
//							}
//							if(searchListResult.get(0).getCallId2() != null) {
//								searchListInfo.setCallId2(searchListResult.get(0).getCallId2());
//							}
//							if(searchListResult.get(0).getCallId3() != null) {
//								searchListInfo.setCallId3(searchListResult.get(0).getCallId3());
//							}
//
//							searchListResult = searchListInfoService.selectTraceSearchListInfo(searchListInfo);
//						} else {
//							searchListResult = null;
//						}
//					}
//				} else {
//					searchListInfo.setParamMap(request, consentNoRecodingUse, "Y");
//
//					MMenuAccessInfo accessInfo = new MMenuAccessInfo();
//					accessInfo.setLevelCode(userInfo.getUserLevel());
//					accessInfo.setProgramCode("P10002");
//					List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
//
//					List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
//					if(!accessResult.get(0).getAccessLevel().equals("A")) {
//						HashMap<String, String> item = new HashMap<String, String>();
//
//						item.put("bgcode", userInfo.getBgCode());
//
//						if(!accessResult.get(0).getAccessLevel().equals("B")) {
//							item.put("mgcode", userInfo.getMgCode());
//						}
//						if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M")) {
//							item.put("sgcode", userInfo.getSgCode());
//						}
//						if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M") && !accessResult.get(0).getAccessLevel().equals("S")) {
//							item.put("user", userInfo.getUserId());
//						}
//
//						authyInfo.add(item);
//					}
//
//					MultiPartInfo multiPartInfo = new MultiPartInfo();
//					multiPartInfo.setrTarget(userInfo.getUserId());
//					List<MultiPartInfo> multiPartResult = ruserInfoService.selectMultiPartInfo(multiPartInfo);
//					if(multiPartResult.size() > 0) {
//						for(int j=0; j<multiPartResult.size(); j++) {
//							MultiPartInfo multiPartItem = multiPartResult.get(j);
//
//							HashMap<String, String> item = new HashMap<String, String>();
//
//							if(multiPartItem.getrBgCode() != null && !multiPartItem.getrBgCode().isEmpty()) {
//								item.put("bgcode", multiPartItem.getrBgCode());
//							}
//							if(multiPartItem.getrMgCode() != null && !multiPartItem.getrMgCode().isEmpty()) {
//								item.put("mgcode", multiPartItem.getrMgCode());
//							}
//							if(multiPartItem.getrSgCode() != null && !multiPartItem.getrSgCode().isEmpty()) {
//								item.put("sgcode", multiPartItem.getrSgCode());
//							}
//
//							authyInfo.add(item);
//						}
//					}
//
//					if(authyInfo != null && authyInfo.size() > 0) {
//						searchListInfo.setAuthyInfo(authyInfo);
//					}
//
//					searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
//				}
//				Integer searchListResultTotal = searchListResult.size();
//
//				String tmpCallKind = "";
//
//				Integer posStart = 0;
//				if(request.getParameter("posStart") != null) {
//					posStart = Integer.parseInt(request.getParameter("posStart"));
//				}
//
//				for( int i = 0; i < searchListResultTotal; i++) {
//					SearchListRecordElement xml = new SearchListRecordElement();
//
//					xml.setId(searchListResult.get(i).getRecDate().replace("-", "") + searchListResult.get(i).getRecTime().replace(":", "") + searchListResult.get(i).getExtNum());
//
//					String listenUrl = searchListResult.get(i).getListenUrl();
//					if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y") && searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##")) {
//						listenUrl = "";
//					}
//
//					if (request.getParameter("mode") == null || request.getParameter("mode").trim().isEmpty() || !request.getParameter("mode").equals("trace")) {
//						String audioTag = "";
//						if(!listenUrl.isEmpty()) {
//							//audioTag = String.format("<div class='simplePlayer'><audio id='player_%s%s%s' controls><source src='%s' type='audio/mpeg'><source src='%s' type='audio/wav'></audio><div class='icon_detailPlayer'></div></div>"
//							//, searchListResult.get(i).getRecDate().replace("-", ""), searchListResult.get(i).getRecTime().replace(":", ""), searchListResult.get(i).getExtNum(), listenUrl, listenUrl);
//							audioTag = String.format("<div class='simplePlayer'><audio id='player_%s%s%s' controls><source src='%s' type='audio/mpeg'></audio><div class='icon_detailPlayer'></div></div>"
//									, searchListResult.get(i).getRecDate().replace("-", ""), searchListResult.get(i).getRecTime().replace(":", ""), searchListResult.get(i).getExtNum(), listenUrl);
//
//						}
//						xml.getSearchListRecordElementCell().add(audioTag);
//					}
//
//					Iterator<String> columnList = searchTemplItem.keySet().iterator();
//
//					String tempStrValue = null;
//
//					while(columnList.hasNext()) {
//						String columnName = columnList.next();
//
//						if(searchTemplItem.get(columnName) == null) continue;
//
//						switch(searchTemplItem.get(columnName)) {
//						case "r_check_box" :
//							xml.getSearchListRecordElementCell().add("0");
//							break;
//						case "r_rec_date" :
//							String orgDate = searchListResult.get(i).getRecDate();
//							if (orgDate == null ) orgDate = "";
//							xml.getSearchListRecordElementCell().add(orgDate);
//							break;
//						case "r_rec_rtime" :
//							if(searchListResult.get(i).getRecRtime() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getRecRtime();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_rec_time" :
//							String orgTime = searchListResult.get(i).getRecTime();
//							if(orgTime == null) orgTime = "";
//							xml.getSearchListRecordElementCell().add(orgTime);
//							break;
//						case "r_bg_code" :
//							if(searchListResult.get(i).getBgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBgCode();
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_mg_code" :
//							if(searchListResult.get(i).getMgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMgCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_sg_code" :
//							if(searchListResult.get(i).getSgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getSgCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_user_id" :
//							if(searchListResult.get(i).getUserId() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getUserId();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_ch_num" :
//							if(searchListResult.get(i).getChNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getChNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_user_name" :
//							if(searchListResult.get(i).getUserName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getUserName();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_call_id1" :
//							if(searchListResult.get(i).getCallId1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallId1();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_call_id2" :
//							if(searchListResult.get(i).getCallId2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallId2();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_call_id3" :
//							if(searchListResult.get(i).getCallId3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallId3();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_ext_num" :
//							if(searchListResult.get(i).getExtNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getExtNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_name" :
//							if(searchListResult.get(i).getCustName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustName();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_phone1" :
//							if(searchListResult.get(i).getCustPhone1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustPhone1();
//
//							tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_phone2" :
//							if(searchListResult.get(i).getCustPhone2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustPhone2();
//
//							tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_phone3" :
//							if(searchListResult.get(i).getCustPhone3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustPhone3();
//
//							tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_call_kind1" :
//							tmpCallKind = "";
//							if(searchListResult.get(i).getCallKind1() != null && !searchListResult.get(i).getCallKind1().equals(""))
//								tmpCallKind = messageSource.getMessage("call.type."+searchListResult.get(i).getCallKind1(), null,Locale.getDefault());
//
//							xml.getSearchListRecordElementCell().add(tmpCallKind);
//							break;
//						case "r_call_kind2" :
//							tmpCallKind = "";
//							if(searchListResult.get(i).getCallKind2() != null && !searchListResult.get(i).getCallKind2().equals(""))
//								tmpCallKind = messageSource.getMessage("call.type."+searchListResult.get(i).getCallKind2(), null,Locale.getDefault());
//
//							xml.getSearchListRecordElementCell().add(tmpCallKind);
//							break;
//						case "r_call_stime" :
//							if(searchListResult.get(i).getCallStime() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallStime();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_call_etime" :
//							if(searchListResult.get(i).getCallEtime() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallEtime();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_call_ttime" :
//							String tmpTtime = "00:00:00";
//							if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y") && searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##")) {
//								tmpTtime = "00:00:00";
//							} else {
//								if(searchListResult.get(i).getCallTtime() != null && searchListResult.get(i).getCallTtime().length() > 0 && searchListResult.get(i).getCallTtime().length() <= 7)
//									tmpTtime = new RecSeeUtil().getSecToTime(Integer.parseInt(searchListResult.get(i).getCallTtime()));
//							}
//							xml.getSearchListRecordElementCell().add(tmpTtime);
//							break;
//						case "r_selfdis_yn" :
//							if(searchListResult.get(i).getSelfDisYn() == null) {
//								tempStrValue = "N";
//							}else if(searchListResult.get(i).getSelfDisYn().equals("Y")) {
//								tempStrValue = "Y";
//							} else {
//								tempStrValue = "N";
//							}
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_v_sys_code" :
//							if(searchListResult.get(i).getvSysCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getvSysCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_v_hdd_flag" :
//							if(searchListResult.get(i).getvHddFlag() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getvHddFlag();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_listen_url" :
//							if(searchListResult.get(i).getListenUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getListenUrl();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_v_filename" :
//							if(searchListResult.get(i).getvFileName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getvFileName();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_s_sys_code" :
//							if(searchListResult.get(i).getsSysCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsSysCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_s_hdd_flag" :
//							if(searchListResult.get(i).getsHddFlag() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsHddFlag();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_screen_url" :
//							if(searchListResult.get(i).getScreenUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getScreenUrl();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_rec_visible" :
//							if(searchListResult.get(i).getRecVisible() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getRecVisible();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_s_filename" :
//							if(searchListResult.get(i).getsFileName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsFileName();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_s_upload_yn" :
//							if(searchListResult.get(i).getsUploadYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsUploadYn();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_t_sys_code" :
//							if(searchListResult.get(i).gettSysCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettSysCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_eval_yn" :
//							if(searchListResult.get(i).getEvalYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getEvalYn();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_text_url" :
//							if(searchListResult.get(i).getTextUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getTextUrl();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_listen_yn" :
//							if(searchListResult.get(i).getListenYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getListenYn();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_t_filename" :
//							if(searchListResult.get(i).gettFileName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettFileName();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_t_upload_yn" :
//							if(searchListResult.get(i).gettUploadYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettUploadYn();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_part_start" :
//							if(searchListResult.get(i).getPartStart() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getPartStart();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_part_end" :
//							if(searchListResult.get(i).getPartEnd() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getPartEnd();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_marking1" :
//							if(searchListResult.get(i).getMarking1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking1();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_marking2" :
//							if(searchListResult.get(i).getMarking2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking2();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_marking3" :
//							if(searchListResult.get(i).getMarking3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking3();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_marking4" :
//							if(searchListResult.get(i).getMarking4() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking4();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_social_num" :
//							if(searchListResult.get(i).getCustSocialNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustSocialNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_contract_num" :
//							if(searchListResult.get(i).getContractNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getContractNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_counsel_code" :
//							if(searchListResult.get(i).getCounselCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCounselCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_counsel_content" :
//							if(searchListResult.get(i).getCounselContent() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCounselContent();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_address" :
//							if(searchListResult.get(i).getCustAddress() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustAddress();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_player_kind" :
//							if(searchListResult.get(i).getPlayerKind() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getPlayerKind();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_t_contents" :
//							if(searchListResult.get(i).gettContents() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettContents();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_receipt_num" :
//							if(searchListResult.get(i).getReceiptNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getReceiptNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer1" :
//							if(searchListResult.get(i).getBuffer1() == null) {
//								if(consentNoRecodingUse.equals("Y")) {
//									tempStrValue = messageSource.getMessage("use.yes", null,Locale.getDefault());
//								} else {
//									tempStrValue = "";
//								}
//							} else {
//								if(consentNoRecodingUse.equals("Y")) {
//									if(searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##")) {
//										tempStrValue = messageSource.getMessage("use.no", null,Locale.getDefault());
//									} else {
//										tempStrValue = messageSource.getMessage("use.yes", null,Locale.getDefault());
//									}
//								} else {
//									tempStrValue = searchListResult.get(i).getBuffer1();
//								}
//							}
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer2" :
//							if(searchListResult.get(i).getBuffer2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer2();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer3" :
//							if(searchListResult.get(i).getBuffer3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer3();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_bw_yn" :
//							if(searchListResult.get(i).getBwYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBwYn();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_bw_bg_code" :
//							if(searchListResult.get(i).getBwBgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBwBgCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_bw_sg_code" :
//							if(searchListResult.get(i).getBwSgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBwSgCode();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_screen_dual_url" :
//							if(searchListResult.get(i).getScreenDualUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getScreenDualUrl();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer4" :
//							if(searchListResult.get(i).getBuffer4() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer4();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer5" :
//							if(searchListResult.get(i).getBuffer5() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer5();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer6" :
//							if(searchListResult.get(i).getBuffer6() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer6();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer7" :
//							if(searchListResult.get(i).getBuffer7() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer7();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer8" :
//							if(searchListResult.get(i).getBuffer8() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer8();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer9" :
//							if(searchListResult.get(i).getBuffer9() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer9();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer10" :
//							if(searchListResult.get(i).getBuffer10() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer10();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer11" :
//							if(searchListResult.get(i).getBuffer11() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer11();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer12" :
//							if(searchListResult.get(i).getBuffer12() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer12();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer13" :
//							if(searchListResult.get(i).getBuffer13() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer13();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer14" :
//							if(searchListResult.get(i).getBuffer14() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer14();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer15" :
//							if(searchListResult.get(i).getBuffer15() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer15();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer16" :
//							if(searchListResult.get(i).getBuffer16() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer16();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer17" :
//							if(searchListResult.get(i).getBuffer17() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer17();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer18" :
//							if(searchListResult.get(i).getBuffer18() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer18();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer19" :
//							if(searchListResult.get(i).getBuffer19() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer19();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_buffer20" :
//							if(searchListResult.get(i).getBuffer20() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer20();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cnid" :
//							if(searchListResult.get(i).getCnId() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCnId();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_dest_ip" :
//							if(searchListResult.get(i).getDestIp() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getDestIp();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_bg_name" :
//							if(searchListResult.get(i).getBgCode() == null) tempStrValue = "";
//							else tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), organizationBgInfo);
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_mg_name" :
//							if(searchListResult.get(i).getMgCode() == null) tempStrValue = "";
//							else tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(), organizationMgInfo);
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_sg_name" :
//							if(searchListResult.get(i).getSgCode() == null) tempStrValue = "";
//							else tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(), searchListResult.get(i).getSgCode(), organizationSgInfo);
//
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "evaluation" :
//							tempStrValue = "<img src='"+request.getContextPath() + "/resources/common/images/"+systemTemplates.toLowerCase()+"_evaluation.png' class='btn_evaluation'>";
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "screen" :
//							if(searchListResult.get(i).getScreenUrl() != null && !searchListResult.get(i).getScreenUrl().isEmpty()
//							&& searchListResult.get(i).getsUploadYn() != null && !searchListResult.get(i).getsUploadYn().isEmpty() && searchListResult.get(i).getsUploadYn().equals("Y")) {
//								tempStrValue = "<img src='"+request.getContextPath() + "/resources/common/images/"+systemTemplates+"_screen.png' class='btn_screen'>";
//							} else {
//								tempStrValue = "";
//							}
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_vdo_url" :
//							if(searchListResult.get(i).getVdoUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getVdoUrl();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_cust_id" :
//							if(searchListResult.get(i).getCustId() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustId();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_regi_date" :
//							if(searchListResult.get(i).getRegiDate() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getRegiDate();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_manager" :
//							if(searchListResult.get(i).getManager() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getManager();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_srtp" :
//							if(searchListResult.get(i).getsRtp() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsRtp();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "f_key" :
//							if(searchListResult.get(i).getfKey() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getfKey();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_ssrc" :
//							if(searchListResult.get(i).getsSrc() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsSrc();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_key" :
//							if(searchListResult.get(i).getrKey() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getrKey();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_co_num" :
//							if(searchListResult.get(i).getCoNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCoNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_did_num" :
//							if(searchListResult.get(i).getDidNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getDidNum();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_uei_data" :
//							if(searchListResult.get(i).getUeiData() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getUeiData();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_queue_no1" :
//							if(searchListResult.get(i).getQueueNo1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getQueueNo1();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_queue_no2" :
//							if(searchListResult.get(i).getQueueNo2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getQueueNo2();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "r_rec_start_type" :
//							if(searchListResult.get(i).getRecStartType() == null) tempStrValue = "T";
//							else tempStrValue = searchListResult.get(i).getRecStartType();
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						case "trace" :
//							if(searchListResult.get(i).getCallId1() != null && searchListResult.get(i).getCallId2() != null
//							&& !searchListResult.get(i).getCallId1().trim().isEmpty() && !searchListResult.get(i).getCallId2().trim().isEmpty()
//							&& ( searchListResult.get(i).getCallKind1().equals("T") || searchListResult.get(i).getCallKind2().equals("T")
//									|| searchListResult.get(i).getCallKind1().equals("C") || searchListResult.get(i).getCallKind2().equals("C") )) {
//								tempStrValue = "<img src='"+request.getContextPath() + "/resources/common/images/trace.png' class='btn_trace'>";
//							} else {
//								tempStrValue = "";
//							}
//							xml.getSearchListRecordElementCell().add(tempStrValue);
//							break;
//						}
//					}
//
//					if(playerMode.equals("SCREEN")) {
//						String screenUrl = "";
//						if(searchListResult.get(i).getScreenUrl() != null && !searchListResult.get(i).getScreenUrl().isEmpty()) {
//							screenUrl = searchListResult.get(i).getScreenUrl();
//						}
//						xml.getSearchListRecordElementCell().add(screenUrl);
//					}
//
//					xml.getSearchListRecordElementCell().add(listenUrl);
//					//xml.getSearchListRecordElementCell().add("http://192.168.0.64:8080/recsee/resources/common/sample_data/data/2015-04-13--13-46__FROM_14087964706_(CAMPBELL____CA)__TO_14085507200_().mp3"); //
//
//					xmls.getRowElements().add(xml);
//
//					xml = null;
//				}
//
//				Integer totalListResult = searchListInfoService.totalSearchListInfo(searchListInfo);
//				if(totalListResult > 0 && (request.getParameter("posStart") == null || request.getParameter("posStart").equals("0"))) {
//					xmls.setTotal_count(totalListResult.toString());
//				} else {
//					xmls.setTotal_count("");
//				}
//				if(request.getParameter("posStart") != null) {
//					xmls.setPos(request.getParameter("posStart"));
//				} else {
//					xmls.setPos("0");
//				}
//			}
//		}
//		return xmls;
//	}
//
//	@RequestMapping(value="/searchExcel.do")
//	public void searchExcel(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
//
//		List<String[]> contents = new ArrayList<String[]>();
//
//		String[] row = null;
//		int colPos = 0;
//
//		if(userInfo != null) {
//			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//			etcConfigInfo.setGroupKey("EXCEPT");
//			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
//			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//			String consentNoRecodingUse = "N";
//			if(etcConfigResult.size() > 0) {
//				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
//			}
//
//			OrganizationInfo organizationInfo = new OrganizationInfo();
//			List<OrganizationInfo> organizationBgInfo = organizatinoInfoService.selectOrganizationBgInfo(organizationInfo);
//			List<OrganizationInfo> organizationMgInfo = organizatinoInfoService.selectOrganizationMgInfo(organizationInfo);
//			List<OrganizationInfo> organizationSgInfo = organizatinoInfoService.selectOrganizationSgInfo(organizationInfo);
//
//			URL xmlUrl = new URL(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/opt/queue_combo_option.xml?exception=internal");
//			Element queueComboXml = XmlUtil.getDocumentElements(xmlUrl);
//			Map<String, String> queueList = XmlUtil.getXmlToHashMap(queueComboXml, "option");
//
//			xmlUrl = new URL(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/opt/combo_option.xml?comboType=YN&exception=internal");
//			Element selfDisYnComboXml = XmlUtil.getDocumentElements(xmlUrl);
//			Map<String, String> selfDisYnList = XmlUtil.getXmlToHashMap(selfDisYnComboXml, "option");
//
//			CustomizeListInfo customizeListInfo = new CustomizeListInfo();
//			if (userInfo != null)
//				customizeListInfo.setrUserId(userInfo.getUserLevel());
//
//			List<CustomizeListInfo> searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);
//
//			Integer settingInfoTotal = searchSettingInfo.size();
//			if(settingInfoTotal < 1) {
//				customizeListInfo.setrUserId("default");
//
//				searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);
//
//				settingInfoTotal = searchSettingInfo.size();
//			}
//			if(settingInfoTotal > 0) {
//				/**
//				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
//				 */
//				Map<String, String> searchTemplItem = null;
//				if (settingInfoTotal > 1) {
//					for(int i = 0; i < settingInfoTotal; i++) {
//						if(userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
//							searchTemplItem = searchSettingInfo.get(i).getAllItem();
//					}
//				} else {
//					searchTemplItem = searchSettingInfo.get(0).getAllItem();
//				}
//
//				String titleBaseName = "views.search.grid.head.";
//
//				Integer colNum = searchTemplItem.size();
//
//				row = new String[colNum];
//				colPos = 0;
//
//				Iterator<String> keys = searchTemplItem.keySet().iterator();
//				while(keys.hasNext()) {
//					String key = keys.next();
//
//					if(key.equals("rUserId")) continue;
//
//					if (searchTemplItem.get(key) != null && !searchTemplItem.get(key).isEmpty()) {
//
//						switch(searchTemplItem.get(key)) {
//						case "r_check_box" :
//						case "screen" :
//						case "evaluation" :
//						case "trace":
//							break;
//						default :
//							row[colPos++] = messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault());
//							break;
//						}
//					}
//				}
//				contents.add(row);
//
//				SearchListInfo searchListInfo = new SearchListInfo();
//
//				searchListInfo.setParamMap(request, consentNoRecodingUse, "N");
//
//				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
//				accessInfo.setLevelCode(userInfo.getUserLevel());
//				accessInfo.setProgramCode("P10002");
//				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
//
//				List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
//				if(!accessResult.get(0).getAccessLevel().equals("A")) {
//					HashMap<String, String> item = new HashMap<String, String>();
//
//					item.put("bgcode", userInfo.getBgCode());
//
//					if(!accessResult.get(0).getAccessLevel().equals("B")) {
//						item.put("mgcode", userInfo.getMgCode());
//					}
//					if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M")) {
//						item.put("sgcode", userInfo.getSgCode());
//					}
//					if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M") && !accessResult.get(0).getAccessLevel().equals("S")) {
//						item.put("user", userInfo.getUserId());
//					}
//
//					authyInfo.add(item);
//				}
//
//				MultiPartInfo multiPartInfo = new MultiPartInfo();
//				multiPartInfo.setrTarget(userInfo.getUserId());
//				List<MultiPartInfo> multiPartResult = ruserInfoService.selectMultiPartInfo(multiPartInfo);
//				if(multiPartResult.size() > 0) {
//					for(int j=0; j<multiPartResult.size(); j++) {
//						MultiPartInfo multiPartItem = multiPartResult.get(j);
//
//						HashMap<String, String> item = new HashMap<String, String>();
//
//						if(multiPartItem.getrBgCode() != null && !multiPartItem.getrBgCode().isEmpty()) {
//							item.put("bgcode", multiPartItem.getrBgCode());
//						}
//						if(multiPartItem.getrMgCode() != null && !multiPartItem.getrMgCode().isEmpty()) {
//							item.put("mgcode", multiPartItem.getrMgCode());
//						}
//						if(multiPartItem.getrSgCode() != null && !multiPartItem.getrSgCode().isEmpty()) {
//							item.put("sgcode", multiPartItem.getrSgCode());
//						}
//
//						authyInfo.add(item);
//					}
//				}
//
//				if(authyInfo != null && authyInfo.size() > 0) {
//					searchListInfo.setAuthyInfo(authyInfo);
//				}
//
//				List<SearchListInfo> searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
//
//				Integer searchListResultTotal = searchListResult.size();
//
//				for( int i = 0; i < searchListResultTotal; i++) {
//					Iterator<String> columnList = searchTemplItem.keySet().iterator();
//
//					String tempStrValue = null;
//
//					row = new String[colNum];
//
//					colPos = 0;
//
//					while(columnList.hasNext()) {
//						String columnName = columnList.next();
//
//						if(searchTemplItem.get(columnName) == null) continue;
//
//						switch(searchTemplItem.get(columnName)) {
//						case "r_check_box" :
//							break;
//						case "r_rec_date" :
//							String orgDate = searchListResult.get(i).getRecDate();
//							if (orgDate == null ) orgDate = "";
//							row[colPos++] = orgDate;
//							break;
//						case "r_rec_rtime" :
//							if(searchListResult.get(i).getRecRtime() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getRecRtime();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_rec_time" :
//							String orgTime = searchListResult.get(i).getRecTime();
//							if(orgTime == null) orgTime = "";
//							row[colPos++] = orgTime;
//							break;
//						case "r_bg_code" :
//							if(searchListResult.get(i).getBgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBgCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_mg_code" :
//							if(searchListResult.get(i).getMgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMgCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_sg_code" :
//							if(searchListResult.get(i).getSgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getSgCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_user_id" :
//							if(searchListResult.get(i).getUserId() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getUserId();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_ch_num" :
//							if(searchListResult.get(i).getChNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getChNum();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_user_name" :
//							if(searchListResult.get(i).getUserName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getUserName();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_id1" :
//							if(searchListResult.get(i).getCallId1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallId1();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_id2" :
//							if(searchListResult.get(i).getCallId2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallId2();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_id3" :
//							if(searchListResult.get(i).getCallId3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallId3();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_ext_num" :
//							if(searchListResult.get(i).getExtNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getExtNum();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cust_name" :
//							if(searchListResult.get(i).getCustName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustName();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cust_phone1" :
//							if(searchListResult.get(i).getCustPhone1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustPhone1();
//
//							tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cust_phone2" :
//							if(searchListResult.get(i).getCustPhone2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustPhone2();
//
//							tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cust_phone3" :
//							if(searchListResult.get(i).getCustPhone3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustPhone3();
//
//							tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_kind1" :
//							tempStrValue = "";
//							if(searchListResult.get(i).getCallKind1() != null && !searchListResult.get(i).getCallKind1().equals(""))
//								tempStrValue = messageSource.getMessage("call.type."+searchListResult.get(i).getCallKind1(), null,Locale.getDefault());
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_kind2" :
//							tempStrValue = "";
//							if(searchListResult.get(i).getCallKind2() != null && !searchListResult.get(i).getCallKind2().equals(""))
//								tempStrValue = messageSource.getMessage("call.type."+searchListResult.get(i).getCallKind2(), null,Locale.getDefault());
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_stime" :
//							if(searchListResult.get(i).getCallStime() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallStime();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_etime" :
//							if(searchListResult.get(i).getCallEtime() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCallEtime();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_call_ttime" :
//							tempStrValue = "00:00:00";
//							if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y") && searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##")) {
//								tempStrValue = "00:00:00";
//							} else {
//								if(searchListResult.get(i).getCallTtime() != null && searchListResult.get(i).getCallTtime().length() > 0 && searchListResult.get(i).getCallTtime().length() <= 7)
//									tempStrValue = new RecSeeUtil().getSecToTime(Integer.parseInt(searchListResult.get(i).getCallTtime()));
//							}
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_selfdis_yn" :
//							if(searchListResult.get(i).getSelfDisYn() == null) {
//								tempStrValue = "N";
//							}else if(searchListResult.get(i).getSelfDisYn().equals("Y")) {
//								tempStrValue = "Y";
//							} else {
//								tempStrValue = "N";
//							}
//
//							tempStrValue =  selfDisYnList.get(tempStrValue);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_v_sys_code" :
//							if(searchListResult.get(i).getvSysCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getvSysCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_v_hdd_flag" :
//							if(searchListResult.get(i).getvHddFlag() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getvHddFlag();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_listen_url" :
//							if(searchListResult.get(i).getListenUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getListenUrl();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_v_filename" :
//							if(searchListResult.get(i).getvFileName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getvFileName();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_s_sys_code" :
//							if(searchListResult.get(i).getsSysCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsSysCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_s_hdd_flag" :
//							if(searchListResult.get(i).getsHddFlag() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsHddFlag();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_screen_url" :
//							if(searchListResult.get(i).getScreenUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getScreenUrl();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_rec_visible" :
//							if(searchListResult.get(i).getRecVisible() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getRecVisible();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_s_filename" :
//							if(searchListResult.get(i).getsFileName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsFileName();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_s_upload_yn" :
//							if(searchListResult.get(i).getsUploadYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getsUploadYn();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_t_sys_code" :
//							if(searchListResult.get(i).gettSysCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettSysCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_eval_yn" :
//							if(searchListResult.get(i).getEvalYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getEvalYn();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_text_url" :
//							if(searchListResult.get(i).getTextUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getTextUrl();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_listen_yn" :
//							if(searchListResult.get(i).getListenYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getListenYn();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_t_filename" :
//							if(searchListResult.get(i).gettFileName() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettFileName();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_t_upload_yn" :
//							if(searchListResult.get(i).gettUploadYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettUploadYn();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_part_start" :
//							if(searchListResult.get(i).getPartStart() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getPartStart();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_part_end" :
//							if(searchListResult.get(i).getPartEnd() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getPartEnd();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_marking1" :
//							if(searchListResult.get(i).getMarking1() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking1();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_marking2" :
//							if(searchListResult.get(i).getMarking2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking2();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_marking3" :
//							if(searchListResult.get(i).getMarking3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking3();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_marking4" :
//							if(searchListResult.get(i).getMarking4() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getMarking4();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cust_social_num" :
//							if(searchListResult.get(i).getCustSocialNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustSocialNum();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_contract_num" :
//							if(searchListResult.get(i).getContractNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getContractNum();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_counsel_code" :
//							if(searchListResult.get(i).getCounselCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCounselCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_counsel_content" :
//							if(searchListResult.get(i).getCounselContent() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCounselContent();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cust_address" :
//							if(searchListResult.get(i).getCustAddress() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCustAddress();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_player_kind" :
//							if(searchListResult.get(i).getPlayerKind() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getPlayerKind();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_t_contents" :
//							if(searchListResult.get(i).gettContents() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).gettContents();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_receipt_num" :
//							if(searchListResult.get(i).getReceiptNum() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getReceiptNum();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer1" :
//							if(searchListResult.get(i).getBuffer1() == null) {
//								if(consentNoRecodingUse.equals("Y")) {
//									tempStrValue = messageSource.getMessage("use.yes", null,Locale.getDefault());
//								} else {
//									tempStrValue = "";
//								}
//							} else {
//								if(consentNoRecodingUse.equals("Y")) {
//									if(searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##")) {
//										tempStrValue = messageSource.getMessage("use.no", null,Locale.getDefault());
//									} else {
//										tempStrValue = messageSource.getMessage("use.yes", null,Locale.getDefault());
//									}
//								} else {
//									tempStrValue = searchListResult.get(i).getBuffer1();
//								}
//							}
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer2" :
//							if(searchListResult.get(i).getBuffer2() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer2();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer3" :
//							if(searchListResult.get(i).getBuffer3() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer3();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_bw_yn" :
//							if(searchListResult.get(i).getBwYn() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBwYn();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_bw_bg_code" :
//							if(searchListResult.get(i).getBwBgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBwBgCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_bw_sg_code" :
//							if(searchListResult.get(i).getBwSgCode() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBwSgCode();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_screen_dual_url" :
//							if(searchListResult.get(i).getScreenDualUrl() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getScreenDualUrl();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer4" :
//							if(searchListResult.get(i).getBuffer4() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer4();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer5" :
//							if(searchListResult.get(i).getBuffer5() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer5();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer6" :
//							if(searchListResult.get(i).getBuffer6() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer6();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer7" :
//							if(searchListResult.get(i).getBuffer7() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer7();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer8" :
//							if(searchListResult.get(i).getBuffer8() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer8();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer9" :
//							if(searchListResult.get(i).getBuffer9() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer9();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer10" :
//							if(searchListResult.get(i).getBuffer10() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer10();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer11" :
//							if(searchListResult.get(i).getBuffer11() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer11();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer12" :
//							if(searchListResult.get(i).getBuffer12() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer12();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer13" :
//							if(searchListResult.get(i).getBuffer13() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer13();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer14" :
//							if(searchListResult.get(i).getBuffer14() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer14();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer15" :
//							if(searchListResult.get(i).getBuffer15() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer15();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer16" :
//							if(searchListResult.get(i).getBuffer16() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer16();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer17" :
//							if(searchListResult.get(i).getBuffer17() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer17();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer18" :
//							if(searchListResult.get(i).getBuffer18() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer18();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer19" :
//							if(searchListResult.get(i).getBuffer19() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer19();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_buffer20" :
//							if(searchListResult.get(i).getBuffer20() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getBuffer20();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_cnid" :
//							if(searchListResult.get(i).getCnId() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getCnId();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_dest_ip" :
//							if(searchListResult.get(i).getDestIp() == null) tempStrValue = "";
//							else tempStrValue = searchListResult.get(i).getDestIp();
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_bg_name" :
//							if(searchListResult.get(i).getBgCode() == null) tempStrValue = "";
//							else tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), organizationBgInfo);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_mg_name" :
//							if(searchListResult.get(i).getMgCode() == null) tempStrValue = "";
//							else tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(), organizationMgInfo);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_sg_name" :
//							if(searchListResult.get(i).getSgCode() == null) tempStrValue = "";
//							else tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(), searchListResult.get(i).getSgCode(), organizationSgInfo);
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "evaluation" :
//						case "screen" :
//							break;
//						case "r_queue_no1" :
//							if(searchListResult.get(i).getQueueNo1() == null) tempStrValue = "";
//							else tempStrValue = queueList.get(searchListResult.get(i).getQueueNo1());
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_queue_no2" :
//							if(searchListResult.get(i).getQueueNo2() == null) tempStrValue = "";
//							else tempStrValue = queueList.get(searchListResult.get(i).getQueueNo2());
//
//							row[colPos++] = tempStrValue;
//							break;
//						case "r_rec_start_type" :
//							if(searchListResult.get(i).getRecStartType() == null) tempStrValue = "T";
//							else tempStrValue = queueList.get(searchListResult.get(i).getRecStartType());
//
//							row[colPos++] = tempStrValue;
//							break;
//						}
//					}
//					contents.add(row);
//				}
//
//				ModelMap.put("excelList", contents);
//				ModelMap.put("target", request.getParameter("fileName"));
//			}
//		}
//		String realPath = request.getSession().getServletContext().getRealPath("/search");
//		ExcelView.createXlsx(ModelMap, realPath, response);
//
//		logInfoService.writeLog(request, "Search - Excel download", null, userInfo.getUserId());
//	}
//
//	@RequestMapping(value="/player_user_info.xml", method=RequestMethod.GET, produces="application/xml")
//	public @ResponseBody dhtmlXGridXml player_user_info(HttpServletRequest request) {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		dhtmlXGridXml xmls = null;
//
//		if(userInfo != null && request.getParameter("userId") != null) {
//			String titleBaseName = "views.search.grid.user.title.";
//
//			xmls = new dhtmlXGridXml();
//			xmls.setHeadElement(new dhtmlXGridHead());
//
//			dhtmlXGridHead head = new dhtmlXGridHead();
//			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
//
//			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
//
//			column.setAlign("center");
//			column.setType("ro");
//			column.setWidth("80");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("<div style=\"padding-right:10px;\"><img src='"+request.getContextPath()+"/resources/common/images/player/icon_user_info.png'>&nbsp;&nbsp;<b>"+messageSource.getMessage(titleBaseName + "operationInfo", null,Locale.getDefault())+"</b></div>");
//
//			head.getColumnElement().add(column);
//
//			column = new dhtmlXGridHeadColumn();;
//
//			column.setAlign("left");
//			column.setType("ro");
//			column.setWidth("192");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("#cspan");
//
//			head.getColumnElement().add(column);
//
//			head.setAfterElement(new dhtmlXGridHeadAfterInit());
//			xmls.setHeadElement(head);
//
//			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
//			dhtmlXGridRowCell cellInfo = null;
//
//			RUserInfo ruserInfo = new RUserInfo();
//			ruserInfo.setUserId(request.getParameter("userId"));
//			List<RUserInfo> ruserInfoResult = ruserInfoService.selectRUserInfo(ruserInfo);
//			Integer ruserInfoResultTotal = ruserInfoResult.size();
//
//			if(ruserInfoResultTotal > 0) {
//
//				RUserInfo ruserItem = ruserInfoResult.get(0);
//
//				dhtmlXGridRow rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(1));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<img src='"+request.getContextPath()+"/resources/common/images/icon_user.svg' style='margin-left:10px;width:50px;height:50px;display:block;'>");
//				rowItem.getCellElements().add(cellInfo);
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "name", null,Locale.getDefault())+"</b></div><div style='display:inline-block;width:120px;text-align:left; float:left;'>"+ruserItem.getUserName()+"</div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//
//				rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(2));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("");
//				rowItem.getCellElements().add(cellInfo);
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "id", null,Locale.getDefault())+"</b></div><div style='display:inline-block;width:120px;text-align:left; float:left;'>"+ruserItem.getUserId()+"</div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//
//				rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(3));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("");
//				rowItem.getCellElements().add(cellInfo);
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "ext", null,Locale.getDefault())+"</b></div><div style='display:inline-block;width:120px;text-align:left; float:left;'>"+ruserItem.getExtNo()+"</div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//			}
//		}
//
//		return xmls;
//	}
//
//	@RequestMapping(value="/player_call_info.xml", method=RequestMethod.GET, produces="application/xml")
//	public @ResponseBody dhtmlXGridXml player_call_info(HttpServletRequest request) {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		dhtmlXGridXml xmls = null;
//
//		if(userInfo != null && request.getParameter("date") != null &&
//			request.getParameter("time") != null && request.getParameter("fileName") != null) {
//			String titleBaseName = "views.search.grid.call.title.";
//
//			xmls = new dhtmlXGridXml();
//			xmls.setHeadElement(new dhtmlXGridHead());
//
//			dhtmlXGridHead head = new dhtmlXGridHead();
//			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
//
//			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
//
//			column.setAlign("center");
//			column.setType("ro");
//			column.setWidth("80");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("<div style=\"padding-right:10px;\"><img src='"+request.getContextPath()+"/resources/common/images/player/icon_call_info.png'>&nbsp;&nbsp;<b>"+messageSource.getMessage(titleBaseName + "callInfo", null,Locale.getDefault())+"</b></div>");
//
//			head.getColumnElement().add(column);
//
//
//			column = new dhtmlXGridHeadColumn();;
//
//			column.setAlign("left");
//			column.setType("ro");
//			column.setWidth("172");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("#cspan");
//
//			head.getColumnElement().add(column);
//
//			head.setAfterElement(new dhtmlXGridHeadAfterInit());
//			xmls.setHeadElement(head);
//
//			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
//
//			dhtmlXGridRowCell cellInfo = null;
//
//			SearchListInfo searchListInfo = new SearchListInfo();
//
//			searchListInfo.setsDate(request.getParameter("date"));
//			searchListInfo.seteDate(request.getParameter("date"));
//			searchListInfo.setsTime(request.getParameter("time"));
//			searchListInfo.seteTime(request.getParameter("time"));
//			searchListInfo.setvFileName(request.getParameter("fileName"));
//
//			List<SearchListInfo> searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
//			Integer searchListResultTotal = searchListResult.size();
//
//			if(searchListResultTotal == 1) {
//				SearchListInfo item = searchListResult.get(0);
//
//				dhtmlXGridRow rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(1));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "date", null,Locale.getDefault())+"</b></div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(item.getRecDate());
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//
//				rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(2));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "time", null,Locale.getDefault())+"</b></div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(item.getRecTime());
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//
//				rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(3));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "callTime", null,Locale.getDefault())+"</b></div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				String tmpTtime = "00:00:00";
//				if(item.getCallTtime() != null && item.getCallTtime().length() > 0 )
//					tmpTtime = new RecSeeUtil().getSecToTime(Integer.parseInt(item.getCallTtime()));
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(tmpTtime);
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//
//				rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(4));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "phoneNo", null,Locale.getDefault())+"</b></div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				String tempStrValue = "";
//				if(item.getCustPhone1() == null) tempStrValue = "";
//				else tempStrValue = item.getCustPhone1();
//
//				tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(tempStrValue);
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//
//				rowItem = new dhtmlXGridRow();
//				rowItem.setId(String.valueOf(5));
//				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue("<div style='display:inline-block;width:50px;vertical-align:baseline;text-align:left; float: left;'><b>"+messageSource.getMessage(titleBaseName + "callType", null,Locale.getDefault())+"</b></div>");
//				rowItem.getCellElements().add(cellInfo);
//
//				String tmpCallKind = "";
//				if(item.getCallKind1() != null && !item.getCallKind1().equals(""))
//					tmpCallKind = messageSource.getMessage("call.type."+item.getCallKind1(), null,Locale.getDefault());
//
//				cellInfo = new dhtmlXGridRowCell();
//				cellInfo.setValue(tmpCallKind);
//				rowItem.getCellElements().add(cellInfo);
//
//				xmls.getRowElements().add(rowItem);
//			}
//
//		}
//		return xmls;
//	}
//
//	@RequestMapping(value="/player_call_log.xml", method=RequestMethod.GET, produces="application/xml")
//	public @ResponseBody dhtmlXGridXml player_call_log(HttpServletRequest request) {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		dhtmlXGridXml xmls = null;
//
//		if(userInfo != null && request.getParameter("date") != null && request.getParameter("phoneNo") != null && request.getParameter("topCount") != null) {
//			String titleBaseName = "views.search.grid.log.title.";
//
//			xmls = new dhtmlXGridXml();
//			xmls.setHeadElement(new dhtmlXGridHead());
//
//			dhtmlXGridHead head = new dhtmlXGridHead();
//			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
//
//			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
//
//			column.setAlign("center");
//			column.setType("ro");
//			column.setWidth("85");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("<div style=\"padding-right:10px;\"><img src='"+request.getContextPath()+"/resources/common/images/player/icon_call_log.svg'>&nbsp;&nbsp;<b>"+messageSource.getMessage(titleBaseName + "callLog", null,Locale.getDefault())+"</b></div>");
//
//			head.getColumnElement().add(column);
//
//			column = new dhtmlXGridHeadColumn();;
//
//			column.setAlign("center");
//			column.setType("ro");
//			column.setWidth("85");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("#cspan");
//
//			head.getColumnElement().add(column);
//
//			column = new dhtmlXGridHeadColumn();;
//
//			column.setAlign("center");
//			column.setType("ro");
//			column.setWidth("83");
//			column.setFiltering("0");
//			column.setEditable("0");
//			column.setCache("1");
//			column.setSort("na");
//			column.setValue("#cspan");
//
//			head.getColumnElement().add(column);
//
//			head.setAfterElement(new dhtmlXGridHeadAfterInit());
//			xmls.setHeadElement(head);
//
//			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
//
//			dhtmlXGridRowCell cellInfo = null;
//
//			SearchListInfo searchListInfo = new SearchListInfo();
//
//			searchListInfo.setsDate("19000101");
//			searchListInfo.seteDate(request.getParameter("date"));
//			searchListInfo.setCustPhone1(request.getParameter("phoneNo"));
//			searchListInfo.setTopCount("5");
//
//			List<SearchListInfo> searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
//			Integer searchListResultTotal = searchListResult.size();
//
//			if(searchListResultTotal > 0) {
//				for(int i=0; i<searchListResultTotal; i++) {
//					SearchListInfo item = searchListResult.get(i);
//
//					dhtmlXGridRow rowItem = new dhtmlXGridRow();
//					rowItem.setId(String.valueOf(i+1));
//					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getRecDate());
//					rowItem.getCellElements().add(cellInfo);
//
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getRecTime());
//					rowItem.getCellElements().add(cellInfo);
//
//					String tmpTtime = "00:00:00";
//					if(item.getCallTtime() != null && item.getCallTtime().length() > 0 )
//						tmpTtime = new RecSeeUtil().getSecToTime(Integer.parseInt(item.getCallTtime()));
//
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(tmpTtime);
//					rowItem.getCellElements().add(cellInfo);
//
//					xmls.getRowElements().add(rowItem);
//				}
//			}
//
//		}
//		return xmls;
//	}
//
//	@RequestMapping(value="/search_expand_combo_option.xml", method=RequestMethod.GET, produces="application/xml")
//	public @ResponseBody dhtmlXGridCombo search_expand_combo_option(HttpServletRequest request) {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		dhtmlXGridCombo xmls = new dhtmlXGridCombo();
//
//		if(userInfo != null) {
//			CustomizeItemInfo customizeItemInfo = new CustomizeItemInfo();
//			if (userInfo != null)
//				customizeItemInfo.setrUserId(userInfo.getUserId());
//
//			xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
//
//			dhtmlXGridComboOption optionItem = new dhtmlXGridComboOption();
//			optionItem.setValue("");
//			optionItem.setValueElement(messageSource.getMessage("message.combo.select", null,Locale.getDefault()));
//			optionItem.setSelected("true");
//
//			xmls.getValueAttr().add(optionItem);
//
//			List<CustomizeItemInfo> searchSettingInfo = customizeInfoService.selectCustomizeItemInfo(customizeItemInfo);
//
//			Integer settingInfoTotal = searchSettingInfo.size();
//
//			if(settingInfoTotal > 0) {
//				String titleBaseName = "views.search.grid.head.";
//
//				//xmls.setValueAttr(new ArrayList<dhtmlXGridComboOption>());
//
//				/**
//				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
//				 */
//				Map<String, String> searchTemplItem = null;
//				if (settingInfoTotal > 1) {
//					for(int i = 0; i < settingInfoTotal; i++) {
//						if(userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
//							searchTemplItem = searchSettingInfo.get(i).getAllItem();
//					}
//				} else {
//					searchTemplItem = searchSettingInfo.get(0).getAllItem();
//				}
//
//				Iterator<String> keys = searchTemplItem.keySet().iterator();
//				while(keys.hasNext()) {
//					String key = keys.next();
//
//					if(key.equals("rUserId")) continue;
//
//					if(searchTemplItem.get(key) != null) {
//						optionItem = new dhtmlXGridComboOption();
//						optionItem.setValue(searchTemplItem.get(key));
//						optionItem.setValueElement(messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault()));
//
//						xmls.getValueAttr().add(optionItem);
//					}
//				}
//			}
//		}
//
//		return xmls;
//	}
//}