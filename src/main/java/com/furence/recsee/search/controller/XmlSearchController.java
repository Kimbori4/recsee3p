package com.furence.recsee.search.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;
import com.furence.recsee.admin.model.MultiPartInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.PhoneMappingInfo;
import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.common.model.SubNumberInfo;
import com.furence.recsee.common.service.CommonCodeService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.PhoneMappingInfoService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.util.AesUtil;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.main.model.dhtmlXTree;
import com.furence.recsee.main.model.dhtmlXTreeItem;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecordingInfo;
import com.furence.recsee.wooribank.facerecording.service.FaceRecordingService;



/**
 * 조회페이지 그리드 그리는 클래스
 * 
 * @author Hyunki Kim
 * @since 2000.01.01
 * @version v3.000
 *
 */
@Controller
public class XmlSearchController {
	private static final Logger logger = LoggerFactory.getLogger(XmlSearchController.class);
	
	@Autowired
	private SearchListInfoService searchListInfoService;
	
	@Autowired
	private FaceRecordingService faceRecordingService;
	
	@Autowired
	private CustomizeInfoService customizeInfoService;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@Autowired
	private AllowableRangeInfoService allowableRangeInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private LogService logService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private OrganizationInfoService organizationInfoService;
	
	
	@Autowired
	private CommonCodeService commonCodeService;
	
	@Autowired
	private SubNumberInfoService subNumberInfoService;

	@Autowired
	private PhoneMappingInfoService phoneMappingInfoService;
	
	/**
	 * 트레이스 결과값 가져오는 메소드
	 * 
	 * @author Hyunki Kim
	 * @since 2000.01.01
	 * @version v3.000
	 * @param map 결과를 담을 맵 매개변수
	 * @return 결과 맵
	 *
	 */
	public HashMap traceResult(HashMap map, SearchListInfo pram, String postgresColumn) {

		SearchListInfo searchListInfo = new SearchListInfo();
		searchListInfo.setsDate(pram.getRecDate().replace("-", ""));
		searchListInfo.seteDate(pram.getRecDate().replace("-", ""));

		if(!"".equals(postgresColumn)) {
			if(postgresColumn.contains("r_cust_phone1")) {
				searchListInfo.setCustPhone1IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_cust_phone2")) {
				searchListInfo.setCustPhone2IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_cust_phone3")) {
				searchListInfo.setCustPhone3IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_cust_social_num")) {
				searchListInfo.setCustSocailNumIsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_cust_phone_ap")) {
				searchListInfo.setCustPhoneApIsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_cust_name")) {
				searchListInfo.setCustNameIsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer1")) {
				searchListInfo.setBuffer1IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer2")) {
				searchListInfo.setBuffer2IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer3")) {
				searchListInfo.setBuffer3IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer4")) {
				searchListInfo.setBuffer4IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer5")) {
				searchListInfo.setBuffer5IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer6")) {
				searchListInfo.setBuffer6IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer7")) {
				searchListInfo.setBuffer7IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer8")) {
				searchListInfo.setBuffer8IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer9")) {
				searchListInfo.setBuffer9IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer10")) {
				searchListInfo.setBuffer10IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer11")) {
				searchListInfo.setBuffer11IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer12")) {
				searchListInfo.setBuffer12IsEncrypt("Y");
			}
			
			if(postgresColumn.contains("r_buffer13")) {
				searchListInfo.setBuffer13IsEncrypt("Y");
			}							
			if(postgresColumn.contains("r_buffer1")) {
				searchListInfo.setBuffer14IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer15")) {
				searchListInfo.setBuffer15IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer16")) {
				searchListInfo.setBuffer16IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer17")) {
				searchListInfo.setBuffer17IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer18")) {
				searchListInfo.setBuffer18IsEncrypt("Y");
			}
			if(postgresColumn.contains("r_buffer19")) {
				searchListInfo.setBuffer19IsEncrypt("Y");
			}							
			if(postgresColumn.contains("r_buffer20")) {
				searchListInfo.setBuffer20IsEncrypt("Y");
			}
		}
		
		if(!StringUtil.isNull(pram.getRecTime(),true))
			searchListInfo.setRecTimeRaw(pram.getRecTime());
		else
			searchListInfo.setRecTimeRaw("");
		if(!StringUtil.isNull(pram.getCallId1(),true))
			searchListInfo.setCallId1(pram.getCallId1());
		else
			searchListInfo.setCallId1("");
		if(!StringUtil.isNull(pram.getCallId2(),true))
			searchListInfo.setCallId2(pram.getCallId2());
		else
			searchListInfo.setCallId2("");
		if(!StringUtil.isNull(pram.getCallId3(),true))
			searchListInfo.setCallId3(pram.getCallId3());
		else
			searchListInfo.setCallId3("");
		if(!StringUtil.isNull(pram.getBuffer18(),true))
			if("Y".equals(pram.getBuffer18()))
				searchListInfo.setBuffer18(pram.getBuffer18());
		
		List<SearchListInfo> searchListResult = searchListInfoService.selectTraceSearchListInfo(searchListInfo);

		if (searchListResult.size() > 1) {
			for (int j = 0; j < searchListResult.size() ; j++) {
				SearchListInfo item = searchListResult.get(j);

				if (map.get(item.getvFileName()) == null) {
					map.put(item.getvFileName(), item);
					traceResult(map,item,postgresColumn);
				}
			}
		}

		return map;
	}

	@RequestMapping(value="/recSection_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml recSection_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		
		String callid1 = request.getParameter("callid1") != null ? request.getParameter("callid1") : "";
		String buffer3 = request.getParameter("buffer3") != null ? request.getParameter("buffer3") : "";
		String type = request.getParameter("type") != null ? request.getParameter("type").trim(): "";
		String date = request.getParameter("date") != null ? request.getParameter("date").trim() : ""; 
		boolean header = request.getParameter("header").equals("true")? true : false;
		
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		String titleBaseName = "views.search.grid.head.";

		xmls.setHeadElement(new dhtmlXGridHead());
		dhtmlXGridHead head = new dhtmlXGridHead();
		head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
		
		dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
		
		column.setId("r_v_sys_code");
		column.setWidth("100");
		column.setType("combo");
		column.setAlign("center");
		column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=system");
		//column.setSort("server");
		column.setValue("<div id=r_v_sys_code style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_v_sys_code".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();		
		column.setId("r_rec_date");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_rec_date style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_rec_date".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_rec_time");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_rec_time style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_rec_time".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_call_ttime");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_call_ttime style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_call_ttime".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_cust_phone1");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_cust_phone1 style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_cust_phone1".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_user_name");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_user_name style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_user_name".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_ext_num");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_ext_num style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_ext_num".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_call_id1");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_call_id1 style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_call_id1".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		head.getColumnElement().add(column);		
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_call_id2");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_call_id2 style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_call_id2".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_rec_start_type");
		column.setHidden("true");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		//column.setSort("server");
		column.setValue("<div id=r_rec_start_type style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "r_rec_start_type".toUpperCase(), null,Locale.getDefault())+"</div>");
		
		
		head.getColumnElement().add(column);
		column = null;
		
		xmls.setHeadElement(head);
		if(header == true) {
			return xmls;
		}
		
		SearchListInfo info = new SearchListInfo();
		if(!StringUtil.isNull(callid1, true)) {
			info.setCallId1(callid1);	
		}else if(!StringUtil.isNull(buffer3, true)) {	
			info.setsDate(date);
			if("callkey".equals(type)){
				info.setBuffer17(buffer3);
			}else if("buffer3".equals(type)){
				info.setBuffer3(buffer3);
			}else {
				return xmls;
			}
				
		}else {
			return xmls;
		}
		
		//전화번호, 이름 마스킹 처리 여부 확인
		EtcConfigInfo etcConfigMasking = new EtcConfigInfo();
		etcConfigMasking.setGroupKey("SEARCH");
		etcConfigMasking.setConfigKey("MASKING_INFO");
		List<EtcConfigInfo> maskingModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigMasking);
		String[] maskingYn= null;
		List<String> maskingInfo = null;
		if(maskingModeResult.size()>0){
			String temp = maskingModeResult.get(0).getConfigValue();
			if (!StringUtil.isNull(temp,true) && temp.split(",").length > 0 ) {
				maskingInfo = Arrays.asList(temp.split(","));
			}
		}
		
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
		
		// 주민번호 마스킹 처리
		etcConfigMaskingNumber = new EtcConfigInfo();
		etcConfigMaskingNumber.setGroupKey("masking");
		etcConfigMaskingNumber.setConfigKey("socialNum");
		maskingNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigMaskingNumber);
		int socialStartIdx = 0, socialEa = 0;
		if(maskingNumberInfo.size() > 0) {
			arrMaskingInfo = maskingNumberInfo.get(0).getConfigValue().split(",");
			socialStartIdx = Integer.parseInt(arrMaskingInfo[0]);
			socialEa = Integer.parseInt(arrMaskingInfo[1]);
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
		
		
		List<SearchListInfo> searchListResult = searchListInfoService.selectRecSectionListInfo(info);
		
		if(searchListResult.size()>0) {
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i=0; i< searchListResult.size();i++) {
				SearchListInfo item = searchListResult.get(i);
		
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//시스템코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getvSysCode());
				rowItem.getCellElements().add(cellInfo);
				
				//날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getRecDate());
				rowItem.getCellElements().add(cellInfo);
				
				// 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getRecTime());
				rowItem.getCellElements().add(cellInfo);
				
				// 초
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getCallTtime());
				rowItem.getCellElements().add(cellInfo);
				
				// 고객 전화번호 asd
				cellInfo = new dhtmlXGridRowCell();
				//cellInfo.setValue(item.getCustPhone1());
				
				String tempStrValue = searchListResult.get(i).getCustPhone1();
				
				if(StringUtil.isNull(tempStrValue,true)) {
					tempStrValue = "-";
				} 
				
				if (maskingInfo!=null && maskingInfo.contains("r_cust_phone1")) {
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
				
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				
				// 상담사 명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getUserName());
				rowItem.getCellElements().add(cellInfo);
				
				// 내선번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getExtNum());
				rowItem.getCellElements().add(cellInfo);
				
				// 녹취 아이디1
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getCallId1());
				rowItem.getCellElements().add(cellInfo);
				
				// 녹취 아이디2
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getCallId2());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getRecStartType());
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
				}		
			}
		return xmls;
	}
	
	
	// 조회 목록
	@RequestMapping(value="/search_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml search_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
//		Locale setLocale = null;
//		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//		Cookie[] cookie = request.getCookies();
//		for(int c=0; c<cookie.length; c++) {
//			if(cookie[c].getName().contains("unqLang")) {
//				setLocale = new Locale(cookie[c].getValue());
//				localeResolver.setLocale(request,response,setLocale);
//				java.util.Locale.setDefault(setLocale);
//			}
//		}

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		if(request.getQueryString().indexOf("&&") >-1||request.getQueryString().indexOf("%&") >-1||request.getQueryString().indexOf("+&") >-1)
		{
			xmls = new dhtmlXGridXml();
			return xmls;
		}

		Enumeration params = request.getParameterNames();

		while(params.hasMoreElements()) {
			String name = (String)params.nextElement();

			String regex = "(union|select|from|where|delete|update|insert|cmdshell|drop)";

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request.getParameter(name));
			
			if(matcher.find()) {
				return null;
			}
		}

		if(userInfo != null) {
			
			String recsee_mobile = "N";
			if(!StringUtil.isNull(request.getParameter("recsee_mobile"))) {
				recsee_mobile = request.getParameter("recsee_mobile");
			}
			// 청취한 이력 색깔 표시
			String listenColor = "N";
			if(!StringUtil.isNull(request.getParameter("listenColor"))) {
				listenColor = request.getParameter("listenColor");
			}
			
			//SAFE DB 확인
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();

			etcConfigInfo.setGroupKey("EXCEPT");
			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String consentNoRecodingUse = "N";
			if(etcConfigResult.size() > 0) {
				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
			}
			
			//STT사용유무 확인
			etcConfigInfo = new EtcConfigInfo();

			etcConfigInfo.setGroupKey("PLAYER");
			etcConfigInfo.setConfigKey("STT_PLAYER");
		    etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String sttPlayerYN = "N";
			if(etcConfigResult.size() > 0) {
				sttPlayerYN = etcConfigResult.get(0).getConfigValue();
			}
			
			etcConfigInfo.setGroupKey("Evaluation");
			etcConfigInfo.setConfigKey("COMPLETION VIEW");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String CompletionView = "N";
			if(etcConfigResult.size() > 0) {
				CompletionView = etcConfigResult.get(0).getConfigValue();
			}
			
			// Postgres 암호화 사용여부
			String postgresColumn = "";
			etcConfigInfo.setGroupKey("ENCRYPT");
			etcConfigInfo.setConfigKey("POSTGRES");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
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
			
			// 자번호 닉네임 표시 옵션 사용 여부
			EtcConfigInfo company_telno = new EtcConfigInfo();
			company_telno.setGroupKey("SEARCH");
			company_telno.setConfigKey("company_telno");
			EtcConfigInfo useExtNickYN = etcConfigInfoService.selectOptionYN(company_telno);
			String extNickYN = "N"; 
			if (useExtNickYN != null )
				extNickYN = useExtNickYN.getConfigValue();

			// 자번호 리스트 조회 - 내선번호
			SubNumberInfo subNumberInfo = new SubNumberInfo();
			List<SubNumberInfo> subNumberInfoResult = null;
			try{
				subNumberInfoResult = subNumberInfoService.selectSubNumberInfo(subNumberInfo);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			// 전화번호 대체문자 표시 옵션 사용 여부
			EtcConfigInfo phone_mapping = new EtcConfigInfo();
			phone_mapping.setGroupKey("SEARCH");
			phone_mapping.setConfigKey("phone_mapping");
			EtcConfigInfo useCustphoneNickYN = etcConfigInfoService.selectOptionYN(phone_mapping);
			String custphoneNickYN = "N"; 
			if (useCustphoneNickYN != null) 
				useCustphoneNickYN.getConfigValue();
			
			// 폰 맵핑 리스트 조회 - 고객 전화번호
			PhoneMappingInfo phoneMappingInfo = new PhoneMappingInfo();
			List<PhoneMappingInfo> phoneMappingInfoResult = null;
			try {
				phoneMappingInfoResult = phoneMappingInfoService.selectPhoneMappingInfo(phoneMappingInfo);
			} catch(Exception e) {
				logger.error("error",e);
			}
					

			OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizationInfoService.selectOrganizationSgInfo(organizationInfo);


			CustomizeListInfo customizeListInfo = new CustomizeListInfo();
			if (request.getParameter("mode") != null && !request.getParameter("mode").trim().isEmpty() && request.getParameter("mode").equals("trace")) {
				customizeListInfo.setrUserId("traceWins".toString());
			} else {
				if (userInfo != null) {
					customizeListInfo.setrUserId(userInfo.getUserLevel());
				}
			}

			List<CustomizeListInfo> searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);

			Integer settingInfoTotal = searchSettingInfo.size();
			if(settingInfoTotal < 1) {
				customizeListInfo.setrUserId("default");

				searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);

				settingInfoTotal = searchSettingInfo.size();
			}
			if(settingInfoTotal > 0) {
				/**
				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
				 */
				Map<String, String> searchTemplItem = null;
				if (settingInfoTotal > 1) {
					for(int i = 0; i < settingInfoTotal; i++) {
						if(userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
							searchTemplItem = searchSettingInfo.get(i).getAllItem();
					}
				} else {
					searchTemplItem = searchSettingInfo.get(0).getAllItem();
				}

//				searchTemplItem.put("errorYn", "errorYn");
				xmls = new dhtmlXGridXml();
				if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
					String titleBaseName = "views.search.grid.head.";

					xmls.setHeadElement(new dhtmlXGridHead());
					dhtmlXGridHead head = new dhtmlXGridHead();
					head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

					Iterator<String> keys = (searchTemplItem!=null && searchTemplItem.keySet()!=null) ? searchTemplItem.keySet().iterator() : null;

					// 아래 6개는 메모 입력시 필수 조건이므로,
					// 만약에 사용자 설정으로 그리드에서 빠지게 된다면, 숨김처리 하여 따로 보여 줘야 한다..
					// 그래야 메모 저장 시 값을 들고가서 저장이 가능하다.
					Boolean rownumber = true;
					Boolean recDate = true;
					Boolean recTime = true;
					Boolean extNum = true;
					Boolean vFilename = true;
					Boolean custPhone1 = true;
					Boolean recUserName = true;
					Boolean recCustName =true;
					Boolean recStartType =true;
					Boolean vSysCode = true;
					
					while(keys!= null && keys.hasNext()) {
						String key = keys.next();

						if(key.equals("rUserId")) continue;

						if (searchTemplItem.get(key) != null) {
							dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
							switch(searchTemplItem.get(key)) {
							/*
							case"errorYn":
								column.setId("errorYn");
								column.setWidth("40");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setHidden("1");
								column.setValue("<div id="+searchTemplItem.get(key)+"  style=\"text-align:center;\">재녹취여부</div>");
								break;
								*/
							case"rownumber" :
								rownumber = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("40");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setHidden("1");
								column.setValue("<div id="+searchTemplItem.get(key)+"  style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case"r_rec_date" :
								recDate = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case"r_rec_time" :
								recTime = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case"callStimeConnect" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case"r_ext_num" :
								extNum = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								if(recsee_mobile.equals("Y")) {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "R_EXT_NUM_MOBILE".toUpperCase(), null,Locale.getDefault())+"</div>");
								}else {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}
								
								break;
							case"r_v_filename" :
								vFilename = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case"r_user_name" :
								recUserName = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_check_box" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("30");
								column.setType("ch");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img id="+searchTemplItem.get(key)+" src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
								break;
							case"r_cust_name" :
								recCustName = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_cust_phone1" :
								custPhone1 = false;
							case "r_cust_phone2" :
							case "r_cust_phone_ap" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_cust_phone3" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_bg_code" :
							case "r_mg_code" :
							case "r_sg_code" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_bg_name" :
							case "r_mg_name" :
							case "r_sg_name" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setAlign("center");
								column.setType("ro");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;

							case "r_counsel_result_bgcode":
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setType("combo");
								column.setAlign("center");
								column.setSort("server");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=counsel");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_counsel_result_mgcode":
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setType("combo");
								column.setAlign("center");
								column.setSort("server");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=mgCode");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_counsel_result_sgcode":
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setType("combo");
								column.setAlign("center");
								column.setSort("server");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=sgCode");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;

							case "screen" :
							case "evaluation" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("80");
								column.setType("txt");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_list_add" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("150");
								column.setType("txt");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div  id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_queue_no1" :
							case "r_queue_no2" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("combo");
								column.setAlign("center");
								column.setSource(request.getContextPath() + "/opt/queue_combo_option.xml");
								column.setSort("na");
								column.setValue("<div  id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_selfdis_yn" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("120");
								column.setType("combo");
								column.setAlign("center");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN2");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_rec_start_type" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("combo");
								column.setAlign("center");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=startType");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								recStartType = false;
								break;
							case "trace" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_v_sys_code" :
								vSysCode = false;
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("combo");
								column.setAlign("center");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=system");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							/*case "r_memo_info" :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("string");
								column.setHidden("1");
								column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;*/
							case "r_memo":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_division":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_phone_mapping":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_company_telno":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_company_telno_nick":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_product_type":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_buffer1":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setSort("na");
								column.setAlign("center");
								if(recsee_mobile.equals("Y")) {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "mobile" + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}else {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}
								break;
							case "r_buffer2":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setSort("na");
								column.setAlign("center");
								if(recsee_mobile.equals("Y")) {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "mobile" + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}else {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}
								break;
							case "r_buffer3":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setSort("na");
								column.setAlign("center");
								if(recsee_mobile.equals("Y")) {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "mobile" + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}else {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}
								break;
							case "r_buffer4":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("combo");
								column.setAlign("center");
								column.setSort("server");
								column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=rbuffer4");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_buffer5":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setSort("na");
								column.setAlign("center");
								if(recsee_mobile.equals("Y")) {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "mobile" + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}else {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}
								break;
							case "r_buffer6":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setSort("na");
								column.setAlign("center");
								if(recsee_mobile.equals("Y")) {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "mobile" + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}else {
									column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								}
								break;
							case "r_buffer16":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_buffer17":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">상품위험등급</div>");
								break;
							case "r_buffer18":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">고객성향등급</div>");
								break;
							case "r_buffer14":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;									
							case "r_stt_player":
								column.setId(searchTemplItem.get(key));
								column.setWidth("80");
								column.setType("txt");
								column.setAlign("center");
								column.setSort("na");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								if(!"Y".equals(sttPlayerYN)) {
									column.setHidden("1");
								}
								break;
							case "r_rec_summary":
								column.setId(searchTemplItem.get(key));
								column.setWidth("500");
								column.setType("txt");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_log_listen":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_call_key_ap":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_rec_visible":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								column.setHidden("1");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\"></div>");
								break;
							case "r_stt_result":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							case "r_rec_volume":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								//column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=volume");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage("views.search.column.title.volume", null,Locale.getDefault())+"</div>");
								head.getColumnElement().add(column);
								column = null;

								// volume 값 숨김처리하여 저장 -> 청취, 다운, 플레이리스트 추가시 hidden값 가져다가 처리
								column = new dhtmlXGridHeadColumn();
								column.setId(searchTemplItem.get(key)+"_value");
								column.setWidth("100");
								column.setType("ro");
								//column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=volume");
								column.setAlign("center");
								column.setSort("server");
								column.setHidden("1");
								column.setValue("");
								break;
							case "r_pdt_nm":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">상품명</div>");
								break;
							case "r_cust_info1":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">고객성향등급</div>");
								break;
							case "r_cust_info2":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">상품위험등급</div>");
								break;
							case "r_cust_info3":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">상품설명유무</div>");
								break;
							case "r_retry_rec":
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("str");
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">재녹취</div>");
								break;
							default :
								column.setId(searchTemplItem.get(key));
								column.setWidth("100");
								column.setType("ro");
								column.setAlign("center");
								column.setSort("server");
								if(searchTemplItem.get(key).equals("r_buffer10")) {
									column.setHidden("1");
								}
								column.setValue("<div id="+searchTemplItem.get(key)+" style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + searchTemplItem.get(key).toUpperCase(), null,Locale.getDefault())+"</div>");
								break;
							}
							head.getColumnElement().add(column);
							column = null;
						}
					}
					
					// 아래 6개는 메모 입력시 필수 조건이므로,
					// 만약에 사용자 설정으로 그리드에서 빠지게 된다면, 숨김처리 하여 따로 보여 줘야 한다..
					// 그래야 메모 저장 시 값을 들고가서 저장이 가능하다.
					if(rownumber) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("rownumber");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("rownumber");
						head.getColumnElement().add(column);
						column = null;
					}

					if(recDate) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_rec_date");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("r_rec_date");
						head.getColumnElement().add(column);
						column = null;
					}

					if(recTime) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_rec_time");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("r_rec_time");
						head.getColumnElement().add(column);
						column = null;
					}

					if(extNum) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_ext_num");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("r_ext_num");
						head.getColumnElement().add(column);
						column = null;
					}

					if(vFilename) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_v_filename");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("r_v_filename");
						head.getColumnElement().add(column);
						column = null;
					}


					if(custPhone1) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_cust_phone1");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("r_cust_phone1");
						head.getColumnElement().add(column);
						column = null;
					}

					if(recUserName) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_user_name");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						column.setValue("r_user_name");
						head.getColumnElement().add(column);
						column = null;
					}
					
					if(recCustName) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_cust_name");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						head.getColumnElement().add(column);
						column = null;
					}
					if(recStartType) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_rec_start_type");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						head.getColumnElement().add(column);
						column = null;
					}
					
					
					if(vSysCode) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
						column.setHidden("true");
						column.setId("r_v_sys_code");
						column.setWidth("0");
						column.setType("ro");
						column.setSort("na");
						column.setAlign("center");
						head.getColumnElement().add(column);
						column = null;
					}

					/**
					 * 메모를 hidden으로 추가 해야 됨..
					 *
					 * */
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					column.setId("r_memo_info");
					column.setWidth("100");
					column.setType("ro");
					column.setAlign("center");
					column.setSort("na");
					column.setHidden("1");
					column.setValue("r_memo_info");
					head.getColumnElement().add(column);
					column = null;

					/**
					 * 녹취 재생 url을 hidden으로 추가 해야 함.
					 */
					column = new dhtmlXGridHeadColumn();
					column.setId("rec_url");
					column.setWidth("0");
					column.setType("ro");
					column.setSort("na");
					column.setAlign("center");
					column.setValue("rec_url");
					column.setHidden("true");

					head.getColumnElement().add(column);
					column = null;

					column = new dhtmlXGridHeadColumn();
					column.setId("rec_url2");
					column.setWidth("0");
					column.setType("ro");
					column.setSort("na");
					column.setAlign("center");
					column.setValue("rec_url2");
					column.setHidden("true");

					head.getColumnElement().add(column);
					column = null;
					
					if(!StringUtil.isNull(request.getParameter("splitAt"), true)) {
						head.setAfterElement(new dhtmlXGridHeadAfterInit());
						dhtmlXGridHeadAfterInit afterInit = new dhtmlXGridHeadAfterInit();

						afterInit.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

						dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
						headCall.setParamElement(new ArrayList<String>());
						headCall.setCommand("splitAt");
						headCall.getParamElement().add(request.getParameter("splitAt"));

						afterInit.getCallElement().add(headCall);

						head.setAfterElement(afterInit);
					}
					xmls.setHeadElement(head);

				} else {
					String Eval_Thema = "recsee";
					if(!StringUtil.isNull(request.getParameter("Eval_Thema"))) {
						Eval_Thema = request.getParameter("Eval_Thema");
					}
					
					xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
					dhtmlXGridRowCell cellInfo = null;
					dhtmlXGridRowUserdata userdataInfo = null;

					SearchListInfo searchListInfo = new SearchListInfo();

					searchListInfo.setLoginUserId(userInfo.getUserId());

					List<SearchListInfo> searchListResult = null;
					
					if (!StringUtil.isNull(request.getParameter("mode"),true) && request.getParameter("mode").equals("trace")) {

						if(!StringUtil.isNull(request.getParameter("file"),true)) {
							URL fileUrl = new URL(request.getParameter("file"));
							String tempFileName="";
							if(fileUrl.getQuery()!=null && !"".equals(fileUrl.getQuery()))
								tempFileName = fileUrl.getQuery();
							String fileName = FilenameUtils.getName(fileUrl.getQuery());
							searchListInfo.setsDate(fileName.substring(0, 8));
							searchListInfo.seteDate(fileName.substring(0, 8));
							searchListInfo.setsTime(fileName.substring(8, 14));
							searchListInfo.seteTime(fileName.substring(8, 14));
							searchListInfo.setvFileName(fileName);

							//녹취 기간 제한 설정
							MMenuAccessInfo accessInfo = new MMenuAccessInfo();
							accessInfo.setLevelCode(userInfo.getUserLevel());
							accessInfo.setProgramCode("P20003");
							try {
								List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
								
								if("Y".equals(accessResult.get(0).getReadYn())) {
									int recDate = accessResult.get(0).getTopPriority();
									
									searchListInfo.setRecDateLimit(String.valueOf(recDate));
									
								}
							}catch (Exception e) {
								logger.error("error",e);
							}		
							
							searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);

							if(searchListResult.size() == 1 && !StringUtil.isNull(searchListResult.get(0).getCallId1(),true)) {

								searchListInfo = new SearchListInfo();
								searchListInfo.setsDate(fileName.substring(0, 8));
								searchListInfo.seteDate(fileName.substring(0, 8));
								
								if(!"".equals(postgresColumn)) {
									if(postgresColumn.contains("r_cust_phone1")) {
										searchListInfo.setCustPhone1IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_cust_phone2")) {
										searchListInfo.setCustPhone2IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_cust_phone3")) {
										searchListInfo.setCustPhone3IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_cust_social_num")) {
										searchListInfo.setCustSocailNumIsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_cust_phone_ap")) {
										searchListInfo.setCustPhoneApIsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_cust_name")) {
										searchListInfo.setCustNameIsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer1")) {
										searchListInfo.setBuffer1IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer2")) {
										searchListInfo.setBuffer2IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer3")) {
										searchListInfo.setBuffer3IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer4")) {
										searchListInfo.setBuffer4IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer5")) {
										searchListInfo.setBuffer5IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer6")) {
										searchListInfo.setBuffer6IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer7")) {
										searchListInfo.setBuffer7IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer8")) {
										searchListInfo.setBuffer8IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer9")) {
										searchListInfo.setBuffer9IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer10")) {
										searchListInfo.setBuffer10IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer11")) {
										searchListInfo.setBuffer11IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer12")) {
										searchListInfo.setBuffer12IsEncrypt("Y");
									}
									
									if(postgresColumn.contains("r_buffer13")) {
										searchListInfo.setBuffer13IsEncrypt("Y");
									}							
									if(postgresColumn.contains("r_buffer1")) {
										searchListInfo.setBuffer14IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer15")) {
										searchListInfo.setBuffer15IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer16")) {
										searchListInfo.setBuffer16IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer17")) {
										searchListInfo.setBuffer17IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer18")) {
										searchListInfo.setBuffer18IsEncrypt("Y");
									}
									if(postgresColumn.contains("r_buffer19")) {
										searchListInfo.setBuffer19IsEncrypt("Y");
									}							
									if(postgresColumn.contains("r_buffer20")) {
										searchListInfo.setBuffer20IsEncrypt("Y");
									}
								}
								
								if (!StringUtil.isNull(request.getParameter("callKeyAp"),true)) {
									searchListInfo.setCallKeyAp(request.getParameter("callKeyAp"));
									
									searchListResult = searchListInfoService.selectTraceSearchListInfo(searchListInfo);
								}else {
									if(!StringUtil.isNull(searchListResult.get(0).getCallId1(),true))
										searchListInfo.setCallId1(searchListResult.get(0).getCallId1());
									else
										searchListInfo.setCallId1("");
									if(!StringUtil.isNull(searchListResult.get(0).getCallId2(),true))
										searchListInfo.setCallId2(searchListResult.get(0).getCallId2());
									else
										searchListInfo.setCallId2("");
									if(!StringUtil.isNull(searchListResult.get(0).getCallId3(),true))
										searchListInfo.setCallId3(searchListResult.get(0).getCallId3());
									else
										searchListInfo.setCallId3("");
									if(!StringUtil.isNull(searchListResult.get(0).getRecTime(),true))
										searchListInfo.setRecTimeRaw(searchListResult.get(0).getRecTime());
									else
										searchListInfo.setRecTimeRaw("");

									searchListResult = searchListInfoService.selectTraceSearchListInfo(searchListInfo);

									HashMap <String, SearchListInfo> traceReuslt = new HashMap <String, SearchListInfo>();

									for (int j = 0; j < searchListResult.size() ; j++) {
										SearchListInfo item = searchListResult.get(j);
										if (traceReuslt.get(item.getvFileName()) == null) {
											traceReuslt.put(item.getvFileName(), item);
											traceResult(traceReuslt,item,postgresColumn);
										}
									}

									ArrayList temp = new ArrayList();

									for (SearchListInfo item : traceReuslt.values()) {
										temp.add(item);
									}

									searchListResult = temp;

								}
							} else {
								searchListResult = null;
							}
						}
					} else {
						searchListInfo.setParamMap(request, consentNoRecodingUse, "Y");
						
						// 텍스트 검색 관련 변수들 - 현재 카테고리검색 부분은 작업이 안됨...
						if(!StringUtil.isNull(request.getParameter( "recCategory"),true)) {
							searchListInfo.setCategory( new ArrayList<String> (Arrays.asList(request.getParameter( "recCategory").split(","))));
						}
						
						if(!StringUtil.isNull(request.getParameter( "recKeyword"),true)) {
							searchListInfo.setRecKeyword( new ArrayList<String> (Arrays.asList(request.getParameter( "recKeyword").split(","))));
						}
						
						if(!StringUtil.isNull(request.getParameter( "recText"),true)) {
							searchListInfo.setRecText(request.getParameter("recText"));
						}
						
						if(!StringUtil.isNull(request.getParameter( "sgCode"),true))
							searchListInfo.setSgCodeArray( new ArrayList<String> (Arrays.asList(request.getParameter( "sgCode").split(","))));

						if(!StringUtil.isNull(request.getParameter("buffer12"),true))
							searchListInfo.setBuffer12Array(new ArrayList<String> (Arrays.asList(request.getParameter("buffer12").split(","))));

						if(!StringUtil.isNull(request.getParameter("buffer13"),true))
							searchListInfo.setBuffer13Array(new ArrayList<String> (Arrays.asList(request.getParameter("buffer13").split(","))));

						if(!StringUtil.isNull(request.getParameter("productType"),true)) {
							String productType = request.getParameter("productType");
							if (!"".equals(productType)) {
								searchListInfo.setrProductType(productType);
							}
						}
							
						MMenuAccessInfo accessInfo = new MMenuAccessInfo();
						accessInfo.setLevelCode(userInfo.getUserLevel());
						accessInfo.setProgramCode("P10002");
						List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

						List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
						
						EtcConfigInfo etcConfigAuthGroupYN = new EtcConfigInfo();
						etcConfigAuthGroupYN.setGroupKey("AUTHORITY");
						etcConfigAuthGroupYN.setConfigKey("USE_BEFORE_GROUP_SEARCH");
						String AuthGroupYNVal = "N"; // 기존 권한 - 현재 그룹만 조회 허용
						EtcConfigInfo AuthGroupYN = null;
						try { 
							AuthGroupYN = etcConfigInfoService.selectOptionYN(etcConfigAuthGroupYN);
						} catch(Exception e) {
							logger.error("error",e);
						} 
						if (AuthGroupYN != null) 
							AuthGroupYNVal = AuthGroupYN.getConfigValue();
						
						//이전 그룹 조회 허용
						if ("Y".equals(AuthGroupYNVal)) {
							if(accessResult.get(0).getAccessLevel().equals("A")) {
								searchListInfo.setPlusType("A");
							}else if(accessResult.get(0).getAccessLevel().equals("B")) {
								searchListInfo.setPlusType("B");
								searchListInfo.setUserIdPlus(userInfo.getBgCode());
							}else if(accessResult.get(0).getAccessLevel().equals("M")) {
								searchListInfo.setPlusType("M");
								searchListInfo.setUserIdPlus(userInfo.getMgCode());
							}else if(accessResult.get(0).getAccessLevel().equals("S")) {
								searchListInfo.setPlusType("S");
								searchListInfo.setUserIdPlus(userInfo.getSgCode());
							}else if(accessResult.get(0).getAccessLevel().equals("U")) {
								searchListInfo.setPlusType("U");
								searchListInfo.setUserIdPlus(userInfo.getUserId());
							}
						}
						if(accessResult.get(0).getAccessLevel().substring(0,1).equals("R")){
							List<AllowableRangeInfo> allowableList = null;
							
							AllowableRangeInfo allowableRangeInfoChk = new AllowableRangeInfo();
				        	allowableRangeInfoChk.setrAllowableCode(accessResult.get(0).getAccessLevel());
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
							if(!accessResult.get(0).getAccessLevel().equals("A")) {
								HashMap<String, String> item = new HashMap<String, String>();
								item.put("bgcode", userInfo.getBgCode());
								item.put("user", userInfo.getUserId());
								if(!accessResult.get(0).getAccessLevel().equals("B")) {
									item.put("mgcode", userInfo.getMgCode());
								}
								if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M")) {
									item.put("sgcode", userInfo.getSgCode());
								}
								/*
								if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M") && !accessResult.get(0).getAccessLevel().equals("S")) {
									item.put("user", userInfo.getUserId());
								}
								*/

								authyInfo.add(item);
							}
						}
													
						//메모 소속별 필터
						accessInfo.setLevelCode(userInfo.getUserLevel());
						accessInfo.setProgramCode("P20001");
						accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
						if(accessResult.get(0).getAccessLevel().equals("B")) {
							searchListInfo.setBgCodeM(userInfo.getBgCode());
						}
						else if(accessResult.get(0).getAccessLevel().equals("M")) {
							searchListInfo.setMgCodeM(userInfo.getMgCode());
						}
						else if(accessResult.get(0).getAccessLevel().equals("S") && !accessResult.get(0).getAccessLevel().equals("M")) {
							searchListInfo.setSgCodeM(userInfo.getSgCode());
						}
						else if(accessResult.get(0).getAccessLevel().equals("U")){
							searchListInfo.setUserIdM(userInfo.getUserId());
						}
											

						MultiPartInfo multiPartInfo = new MultiPartInfo();
						multiPartInfo.setrTarget(userInfo.getUserId());
						List<MultiPartInfo> multiPartResult = ruserInfoService.selectMultiPartInfo(multiPartInfo);
						if(multiPartResult.size() > 0) {
							for(int j=0; j<multiPartResult.size(); j++) {
								MultiPartInfo multiPartItem = multiPartResult.get(j);

								HashMap<String, String> item = new HashMap<String, String>();

								if(multiPartItem.getrBgCode() != null && !multiPartItem.getrBgCode().isEmpty()) {
									item.put("bgcode", multiPartItem.getrBgCode());
								}
								if(multiPartItem.getrMgCode() != null && !multiPartItem.getrMgCode().isEmpty()) {
									item.put("mgcode", multiPartItem.getrMgCode());
								}
								if(multiPartItem.getrSgCode() != null && !multiPartItem.getrSgCode().isEmpty()) {
									item.put("sgcode", multiPartItem.getrSgCode());
								}

								authyInfo.add(item);
							}
						}

						if(authyInfo != null && authyInfo.size() > 0) {
							searchListInfo.setAuthyInfo(authyInfo);
						}
						// logService.writeLog(request, "RECSEARCH", "DO", searchListInfo.toLogString());
						if(safeDbResult.size() > 0) {
							if("Y".equals(safeDbResult.get(0).getConfigValue())) {
								if(!StringUtil.isNull(searchListInfo.getCustName(),true))
									searchListInfo.setCustName(searchListInfo.SafeDBSetter(searchListInfo.getCustName()));
							}
						}
						
						// VOC 구분[천재]
						if(!StringUtil.isNull(request.getParameter("r_buffer10"),true)) {
							searchListInfo.setBuffer11(request.getParameter("r_buffer10"));
						}
						
						// 상담유형(대)[천재]
						if(!StringUtil.isNull(request.getParameter("r_buffer11"),true)) {
							searchListInfo.setBuffer11(request.getParameter("r_buffer11"));
						}
						
						// 상담유형(중)[천재]
						if(!StringUtil.isNull(request.getParameter("r_buffer12"),true)) {
							searchListInfo.setBuffer12(request.getParameter("r_buffer12"));
						}
						
						// 상담유형(소)[천재]
						if(!StringUtil.isNull(request.getParameter("r_buffer13"),true)) {
							searchListInfo.setBuffer13(request.getParameter("r_buffer13"));
						}
						
						// 서비스 구분[천재]
						if(!StringUtil.isNull(request.getParameter("buffer15"),true)) {
							searchListInfo.setBuffer15(request.getParameter("buffer15"));
						}
						
						// 접수 구분[천재]
						if(!StringUtil.isNull(request.getParameter("buffer16"),true)) {
							searchListInfo.setBuffer16(request.getParameter("buffer16"));
						}
						
						// 고객전화번호 암호화 시작한 날짜
						if(!StringUtil.isNull(request.getParameter("custEncryptDate"), true)) {
							searchListInfo.setCustEncryptDate(request.getParameter("custEncryptDate"));
						}
						
						if (!StringUtil.isNull(request.getParameter("custName"), true) && "Y".equals(custphoneNickYN)) {
				              String companyTelname = request.getParameter("custName");
				              ArrayList<String> custPhoneTel = new ArrayList<>();
				              for (int s = 0; s < phoneMappingInfoResult.size(); s++) {
				                String custnickName = ((PhoneMappingInfo)phoneMappingInfoResult.get(s)).getCustNickName();
				                String custPhoneNum = ((PhoneMappingInfo)phoneMappingInfoResult.get(s)).getCustPhone();
				                if ("Y".equals(((PhoneMappingInfo)phoneMappingInfoResult.get(s)).getUseNickName()) && custnickName.contains(companyTelname))
				                  custPhoneTel.add(custPhoneNum); 
				              } 
				              if (custPhoneTel.size() == 0)
				                custPhoneTel.add("-"); 
				              searchListInfo.setCustPhoneTelNo(custPhoneTel);
				              searchListInfo.setCustName(null);
				            } 
							
						accessInfo = new MMenuAccessInfo();
						//콜 타입 조회 권한
						accessInfo.setLevelCode(userInfo.getUserLevel());
						accessInfo.setProgramCode("P20002");
						
						try {
						
							accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
							// 수신, 발신, 전환수신, 전환송신, 내선
							String[] Calltype = {"Y","Y","Y","Y","Y","Y"};
							
							if(accessResult != null && accessResult.size() > 0) {
								// 사용유무
								if(accessResult.get(0).getReadYn().equals("N")) {
									Calltype[0] = "N";
								}	
								// 수신
								if(accessResult.get(0).getWriteYn().equals("N")) {
									Calltype[1] = "N";
								}
								// 발신
								if(accessResult.get(0).getModiYn().equals("N")) {
									Calltype[2] = "N";
								}
								// 전환수신
								if(accessResult.get(0).getDelYn().equals("N")) {
									Calltype[3] = "N";
								}
								// 전환송신
								if(accessResult.get(0).getListenYn().equals("N")) {
									Calltype[4] = "N";
								}
								// 내선
								if(accessResult.get(0).getDownloadYn().equals("N")) {
									Calltype[5] = "N";
								}
							}
													
							
							if ("Y".equals(Calltype[0])) {
								ArrayList<String> callType = new ArrayList<>();
								if ("Y".equals(Calltype[1])) {
									callType.add("I");
								}
								if ("Y".equals(Calltype[2])) {
									callType.add("O");
								}
								if ("Y".equals(Calltype[3])) {
									callType.add("TR");
								}							
								if ("Y".equals(Calltype[4])) {
									callType.add("TS");
								}
								if ("Y".equals(Calltype[5])) {
									callType.add("Z");
								}
								
								if(callType.size() > 0) {
									searchListInfo.setRecCallType(callType);
								}
							}
						}catch (Exception e) {
							logger.error("error",e);
						}
						
						//녹취 기간 제한 설정
						accessInfo = new MMenuAccessInfo();
						accessInfo.setLevelCode(userInfo.getUserLevel());
						accessInfo.setProgramCode("P20003");
						try {
							accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
							
							if("Y".equals(accessResult.get(0).getReadYn())) {
								int recDate = accessResult.get(0).getTopPriority();
								
								searchListInfo.setRecDateLimit(String.valueOf(recDate));
								
							}
						}catch (Exception e) {
							logger.error("error",e);
						}
						
						if(!"".equals(postgresColumn)) {
							if(postgresColumn.contains("r_cust_phone1")) {
								searchListInfo.setCustPhone1IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_cust_phone2")) {
								searchListInfo.setCustPhone2IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_cust_phone3")) {
								searchListInfo.setCustPhone3IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_cust_social_num")) {
								searchListInfo.setCustSocailNumIsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_cust_phone_ap")) {
								searchListInfo.setCustPhoneApIsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_cust_name")) {
								searchListInfo.setCustNameIsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer1")) {
								searchListInfo.setBuffer1IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer2")) {
								searchListInfo.setBuffer2IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer3")) {
								searchListInfo.setBuffer3IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer4")) {
								searchListInfo.setBuffer4IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer5")) {
								searchListInfo.setBuffer5IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer6")) {
								searchListInfo.setBuffer6IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer7")) {
								searchListInfo.setBuffer7IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer8")) {
								searchListInfo.setBuffer8IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer9")) {
								searchListInfo.setBuffer9IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer10")) {
								searchListInfo.setBuffer10IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer11")) {
								searchListInfo.setBuffer11IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer12")) {
								searchListInfo.setBuffer12IsEncrypt("Y");
							}
							
							if(postgresColumn.contains("r_buffer13")) {
								searchListInfo.setBuffer13IsEncrypt("Y");
							}							
							if(postgresColumn.contains("r_buffer1")) {
								searchListInfo.setBuffer14IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer15")) {
								searchListInfo.setBuffer15IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer16")) {
								searchListInfo.setBuffer16IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer17")) {
								searchListInfo.setBuffer17IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer18")) {
								searchListInfo.setBuffer18IsEncrypt("Y");
							}
							if(postgresColumn.contains("r_buffer19")) {
								searchListInfo.setBuffer19IsEncrypt("Y");
							}							
							if(postgresColumn.contains("r_buffer20")) {
								searchListInfo.setBuffer20IsEncrypt("Y");
							}
						}
						
						// ListenUrl 암호화 처리 여부
						EtcConfigInfo urlEncYn = new EtcConfigInfo();
						urlEncYn.setGroupKey("LISTEN");
						urlEncYn.setConfigKey("URL_ENC_YN");
						urlEncYn = etcConfigInfoService.selectOptionYN(urlEncYn);
						if(urlEncYn != null) {
							searchListInfo.setUrlEncYn(urlEncYn.getConfigValue());
						}
						searchListInfo.setPartRecYn("N");  //병합인지 아닌지 구분 Y = 부분 N 병합
						searchListInfo.setErrorYn("N");  //병합녹취 이지만 최근에 재녹취한 이력이 있어서 숨김처리한 이력

						
//						rs_search_info - E1001  
						
						
						// csv 조회
						if(!StringUtil.isNull(request.getParameter("csv"),true)) { // csv 파라메터 있는 경우만 아래 방식으로 select
							searchListResult = new ArrayList(); // 위에서 null로 선언하여 ArrayList로 선언 (rs를 add하기 위함)
							String[] colName = request.getParameter("csvColName").split(","); // 컬럼명 , 기준으로 스플릿하기
							String[] csvList = request.getParameter("csvList").split("!e!"); // 유효데이터 enter 기준으로 스플릿
							for (int i = 0; i < csvList.length; i++) { // 유효데이터 개수 만큼 반복
								SearchListInfo csvSearchInfo = new SearchListInfo(); // select할 dto 선언 
								for (int j = 0; j < colName.length; j++) { // 컬럼명 기준으로 반복(데이터 순서 상관없이 컬럼명 기준으로 dto에 set하기 위함)
									switch (colName[j].trim()) {
									case "date":
										csvSearchInfo.setRecDate(csvList[i].split(",")[j].trim());
										break;
									case "callKey":
										csvSearchInfo.setCallKeyAp(csvList[i].split(",")[j].trim());
										break;
									// csv 양식 내 조건 컬럼 추가 시 case문 추가 필요(mapper에서도 추가 필요)
									default:
										break;
									}
								}
								List<SearchListInfo> csvSearchListInfo = searchListInfoService.selectValidation(csvSearchInfo); // csv 한개 레코드 조건으로 select
								for (int j = 0; j < csvSearchListInfo.size(); j++) { // rs 크기만큼 반복
									searchListResult.add(csvSearchListInfo.get(j)); // 조회 및 청취 페이지로 넘길 List 변수에 rs값 add
								}
							}
							
							/*ArrayList<String> callKeyApList = new ArrayList<>();
							for (int i = 0; i < csvList.length; i++) {
								callKeyApList.add(csvList[i].split(",")[0]);
							}
							searchListInfo.setCallKeyApList(callKeyApList);
							searchListResult = searchListInfoService.selectCSV(searchListInfo);*/
						}else {
							if("Y".equals(sttPlayerYN)) {
								searchListResult = searchListInfoService.selectSearchListInfoSTT(searchListInfo);
							}else {
								try {
									searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
								} catch (Exception e) {
									searchListInfo.setCustEncryptDate("N");
									searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
								}
							}
						}
					}

					Map<String, Object> map = new HashMap<String, Object>();

					//평가를 위한 데이터 처리
					//평가 대상자로 사용할 본인 아이디
					String ConnAuthy = userInfo.getUserLevel();
					if(ConnAuthy.trim().equals("E1006")) {
						
					}
					Integer searchListResultTotal = ( searchListResult == null ? 0 : searchListResult.size());
					
					String tmpCallKind = "";

					Integer posStart = 0;
					if(request.getParameter("posStart") != null) {
						posStart = Integer.parseInt(request.getParameter("posStart"));
					}

					//전화번호, 이름 마스킹 처리 여부 확인
					EtcConfigInfo etcConfigMasking = new EtcConfigInfo();
					etcConfigMasking.setGroupKey("SEARCH");
					etcConfigMasking.setConfigKey("MASKING_INFO");
					List<EtcConfigInfo> maskingModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigMasking);
					String[] maskingYn= null;
					List<String> maskingInfo = null;
					if(maskingModeResult.size()>0){
						String temp = maskingModeResult.get(0).getConfigValue();
						if (!StringUtil.isNull(temp,true) && temp.split(",").length > 0 ) {
							maskingInfo = Arrays.asList(temp.split(","));
						}
					}

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
					
					// 주민번호 마스킹 처리
					etcConfigMaskingNumber = new EtcConfigInfo();
					etcConfigMaskingNumber.setGroupKey("masking");
					etcConfigMaskingNumber.setConfigKey("socialNum");
					maskingNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigMaskingNumber);
					int socialStartIdx = 0, socialEa = 0;
					if(maskingNumberInfo.size() > 0) {
						arrMaskingInfo = maskingNumberInfo.get(0).getConfigValue().split(",");
						socialStartIdx = Integer.parseInt(arrMaskingInfo[0]);
						socialEa = Integer.parseInt(arrMaskingInfo[1]);
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
					
					


					AesUtil util = new AesUtil();
					boolean check=true;
				
					for( int i = 0; i < searchListResultTotal; i++) {
						
						/*// 녹취 파일 사용자 분배 여부
						String division = "-";
						for (int j = 0; j < transcriptList.size(); j++) {
							System.out.println("division index::: " + j);
							if (searchListResult.get(i).getRecDate().replace("-", "").equals(transcriptList.get(j).getrRecDate()) && searchListResult.get(i).getRecTime().replace(":", "").equals(transcriptList.get(j).getrRecTime()) && searchListResult.get(i).getExtNum().equals(transcriptList.get(j).getrExtNum())) {
								division = transcriptList.get(j).getrTranscriptStatus();
								System.out.println("division ::: " + division);
								break;
							}
						}*/
//					searchListResult.get(i).setRownumber(String.valueOf(i));
						
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(Optional.ofNullable(searchListResult.get(i).getRecDate()).orElse("").replace("-", "") + Optional.ofNullable(searchListResult.get(i).getRecTime()).orElse("").replace(":", "") + Optional.ofNullable(searchListResult.get(i).getExtNum()).orElse("")+Optional.ofNullable(searchListResult.get(i).getvSysCode()).orElse("")+Optional.ofNullable(searchListResult.get(i).getRecStartType()).orElse(""));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
						rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());

//						String listenUrl = "";
						String screenUrl = "";
						// 녹취 url 암호화
//						if (!StringUtil.isNull(searchListResult.get(i).getListenUrl(),true)){
//							listenUrl =  util.encrypt(searchListResult.get(i).getListenUrl());
//						}

						// 녹취 파일이 로컬(서버기준)에 남아 있을 경우
						// 우선 열람 서버 무시하고, 로컬에 있을 경우만 가정한다.

//						if("N".equals(searchListResult.get(i).getVsendFileFlag()))
						//요 밑에줄만 진짜임여
//						listenUrl = "http://"+searchListResult.get(i).getvRecIp()+":28881/listen?url="+searchListResult.get(i).getvRecFullpath();
						//listenUrl="getlisten.do?recDate="+searchListResult.get(i).getRecDate().replaceAll("-", "")+"&recTime="+searchListResult.get(i).getRecTime().replaceAll(":", "")+"&extNum="+searchListResult.get(i).getExtNum();


//						else
//							listenUrl = "http://"+searchListResult.get(i).getvReadIp()+":28881/listen?url="+searchListResult.get(i).getvReadFullpath().replaceAll("\\\\", "/");

//						if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y") && searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##")) {
//							listenUrl = "";
//						}


						//Iterator<String> columnList = searchTemplItem.keySet().iterator();
						Iterator<String> columnList = (searchTemplItem!=null && searchTemplItem.keySet()!=null) ? searchTemplItem.keySet().iterator() : null;
						String tempStrValue = null;

						// 아래 6개는 메모 입력시 필수 조건이므로,
						// 만약에 사용자 설정으로 그리드에서 빠지게 된다면, 숨김처리 하여 따로 보여 줘야 한다..
						// 그래야 메모 저장 시 값을 들고가서 저장이 가능하다.
						Boolean rownumber = true;
						Boolean recDate = true;
						Boolean recTime = true;
						Boolean extNum = true;
						Boolean vFilename = true;
						Boolean custPhone1 = true;
						Boolean recUserName = true;
						Boolean recCustName = true;
						Boolean recStartType = true;
						Boolean vSysCode = true;

						Integer count = 0;
						
						while(columnList!=null && columnList.hasNext()) {
							String columnName = columnList.next();
							columnName = Optional.ofNullable(columnName).orElse("");
							
							if(searchTemplItem.get(columnName) == null) continue;
							switch(searchTemplItem.get(columnName)) {

							case "rownumber" :
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(searchListResult.get(i).getRownumber());
								rowItem.getCellElements().add(cellInfo);
								rownumber = false;
								break;
							case "r_check_box" :
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue("0");
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_rec_date" :
								String orgDate = searchListResult.get(i).getRecDate();
								if (StringUtil.isNull(orgDate,true))
									orgDate = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(orgDate);
								rowItem.getCellElements().add(cellInfo);
								recDate = false;
								break;
							case "r_rec_rtime" :
								tempStrValue = searchListResult.get(i).getRecRtime();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_rec_time" :
								String orgTime = searchListResult.get(i).getRecTime();
								if(StringUtil.isNull(orgTime,true))
									orgTime = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(orgTime);
								rowItem.getCellElements().add(cellInfo);
								recTime= false;
								break;
							case "r_call_stime_connect" :
								String orgTimeConnect = searchListResult.get(i).getCallStimeConnect();
								if(StringUtil.isNull(orgTimeConnect,true))
									orgTimeConnect = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(orgTimeConnect);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_bg_code" :
								tempStrValue = searchListResult.get(i).getBgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_mg_code" :
								tempStrValue = searchListResult.get(i).getMgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_sg_code" :
								tempStrValue = searchListResult.get(i).getSgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_user_id" :
								tempStrValue = searchListResult.get(i).getUserId();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_ch_num" :
								tempStrValue = searchListResult.get(i).getChNum();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_user_name" :
								recUserName = false;
 								tempStrValue = searchListResult.get(i).getUserName();
								if(searchListResult.get(i).getUserName() == null) {
									tempStrValue = "";
								}
								/*userdataInfo = new dhtmlXGridRowUserdata();
								userdataInfo.setName(searchTemplItem.get(columnName));
								userdataInfo.setValue(tempStrValue);
								rowItem.getUserdataElements().add(userdataInfo);*/

								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName))) {
									tempStrValue=new RecSeeUtil().makingName(tempStrValue);
								}

								if(StringUtil.isNull(tempStrValue) || tempStrValue.equals("")) {
									tempStrValue = "-";
								}
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);

								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_call_id1" :
								tempStrValue = searchListResult.get(i).getCallId1();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_call_id2" :
								tempStrValue = searchListResult.get(i).getCallId2();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_call_id3" :
								tempStrValue = searchListResult.get(i).getCallId3();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_ext_num" :
								tempStrValue = searchListResult.get(i).getExtNum();		
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								extNum = false;
								break;
							case "r_cust_name" :
								recCustName=false;
								tempStrValue = searchListResult.get(i).getCustName();
								
								if ("N".equals(custphoneNickYN) && StringUtil.isNull(tempStrValue, true)) {
				                    tempStrValue = "-";
				                  } else if ("Y".equals(custphoneNickYN)) {
				                    tempStrValue = ((SearchListInfo)searchListResult.get(i)).getCustPhone1();
				                    for (int s = 0; s < phoneMappingInfoResult.size(); s++) {
				                      String str = ((PhoneMappingInfo)phoneMappingInfoResult.get(s)).getCustPhone();
				                      if ("Y".equals(((PhoneMappingInfo)phoneMappingInfoResult.get(s)).getUseNickName()) && tempStrValue.equals(str)) {
				                        tempStrValue = ((PhoneMappingInfo)phoneMappingInfoResult.get(s)).getCustNickName();
				                        break;
				                      } 
				                      if (s == phoneMappingInfoResult.size() - 1)
				                        tempStrValue = "-"; 
				                    } 
				                    if (phoneMappingInfoResult.size() == 0)
				                      tempStrValue = "-"; 
								
				                  }
								else {
									if(safeDbResult.size() > 0) {
										if("Y".equals(safeDbResult.get(0).getConfigValue()))
											tempStrValue = searchListResult.get(i).SafeDBGetter(searchListResult.get(i).getCustName());
									}
								}
								/*userdataInfo = new dhtmlXGridRowUserdata();
								userdataInfo.setName(searchTemplItem.get(columnName));
								userdataInfo.setValue(tempStrValue);
								rowItem.getUserdataElements().add(userdataInfo);*/

								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName)))
									tempStrValue=new RecSeeUtil().makingName(tempStrValue);

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_phone_ap" :
								//custPhone1 = false;

								tempStrValue = searchListResult.get(i).getrCustPhoneAp();
								if(StringUtil.isNull(tempStrValue,true)) {
									tempStrValue = searchListResult.get(i).getCustPhone2();
									if(StringUtil.isNull(tempStrValue,true)) {
										tempStrValue = searchListResult.get(i).getCustPhone1();
										if(StringUtil.isNull(tempStrValue,true)) {
											tempStrValue = "-";
										}
									}
								}
								else {
									
									if(tempStrValue.substring(0,1).equals("9")) {
										tempStrValue = tempStrValue.substring(1);
									}
									
									if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName))) {
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
								}
//								else {
//									if(safeDbResult.size() > 0) {
//										if("Y".equals(safeDbResult.get(0).getConfigValue()))
//											tempStrValue = searchListResult.get(i).SafeDBGetter(searchListResult.get(i).getCustPhone1());
//									}aa
//								}

//								tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
								/*userdataInfo = new dhtmlXGridRowUserdata();
								userdataInfo.setName(searchTemplItem.get(columnName));
								userdataInfo.setValue(tempStrValue);
								rowItem.getUserdataElements().add(userdataInfo);*/

//								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName)))
//									tempStrValue = new RecSeeUtil().maskingNumber(tempStrValue);
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;									
							case "r_cust_phone1" :
								custPhone1 = false;

								tempStrValue = searchListResult.get(i).getCustPhone1();
								
								try {
									if(StringUtil.isNull(tempStrValue,true)) {
										tempStrValue = "-";
									} 
									
									if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName))) {
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
									tempStrValue = searchListResult.get(i).getCustPhone1();
								}
								
//								else {
//									if(safeDbResult.size() > 0) {
//										if("Y".equals(safeDbResult.get(0).getConfigValue()))
//											tempStrValue = searchListResult.get(i).SafeDBGetter(searchListResult.get(i).getCustPhone1());
//									}
//								}

//								tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
								/*userdataInfo = new dhtmlXGridRowUserdata();
								userdataInfo.setName(searchTemplItem.get(columnName));
								userdataInfo.setValue(tempStrValue);
								rowItem.getUserdataElements().add(userdataInfo);*/

//								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName)))
//									tempStrValue = new RecSeeUtil().maskingNumber(tempStrValue);
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_phone2" :
								tempStrValue = searchListResult.get(i).getCustPhone2();
								
								try {
									if(StringUtil.isNull(tempStrValue,true)) {
										tempStrValue = "-";
									} 
									
									if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName))) {
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
									tempStrValue = searchListResult.get(i).getCustPhone2();
								}
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_cust_phone3" :
								tempStrValue = searchListResult.get(i).getCustPhone3();	
								try {
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";								
								
								tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);

								/*userdataInfo = new dhtmlXGridRowUserdata();
								userdataInfo.setName(searchTemplItem.get(columnName));
								userdataInfo.setValue(tempStrValue);
								rowItem.getUserdataElements().add(userdataInfo);*/


								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName)))
									tempStrValue = new RecSeeUtil().maskingNumber(tempStrValue);								
								}catch (Exception e) {
									tempStrValue = searchListResult.get(i).getCustPhone3();	
								}
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_call_kind1" :
								tempStrValue = searchListResult.get(i).getCallKind1();
								
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else {		
									tempStrValue = messageSource.getMessage("call.type."+tempStrValue.trim(), null,Locale.getDefault());
								}

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_call_kind2" :
								tempStrValue = searchListResult.get(i).getCallKind2();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else
									tempStrValue = messageSource.getMessage("call.type."+tempStrValue.trim(), null,Locale.getDefault());

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_call_stime" :
								tempStrValue = searchListResult.get(i).getCallStime();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_call_etime" :
								tempStrValue = searchListResult.get(i).getCallEtime();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_call_ttime" :
								tempStrValue = searchListResult.get(i).getCallTtime();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "00:00:00";

								if(consentNoRecodingUse != null && consentNoRecodingUse.equals("Y") && searchListResult.get(i).getBuffer1() != null && searchListResult.get(i).getBuffer1().equals("##"))
									tempStrValue = "00:00:00";
//								else
//									if (!StringUtil.isNull(tempStrValue,true) && tempStrValue.length() <= 7)
//										tempStrValue = new RecSeeUtil().getSecToTime(Integer.parseInt(searchListResult.get(i).getCallTtime()));

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_call_ttime_connect" :
								tempStrValue = searchListResult.get(i).getCallTtimeConnect();
								if(StringUtil.isNull(searchListResult.get(i).getCallStimeConnect(),true)||"0".equals(searchListResult.get(i).getCallStimeConnect()))
									tempStrValue = "-";
								else {
									if(StringUtil.isNull(tempStrValue,true)||"".equals(tempStrValue))
										tempStrValue="00:00:00";
									else
										tempStrValue = new RecSeeUtil().getSecToTime(Integer.parseInt(searchListResult.get(i).getCallTtimeConnect()));
								}

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_selfdis_yn" :
								tempStrValue = searchListResult.get(i).getSelfDisYn();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "N";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_v_sys_code" :
								vSysCode = false;
								tempStrValue = searchListResult.get(i).getvSysCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_v_hdd_flag" :
								tempStrValue = searchListResult.get(i).getvHddFlag();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_listen_url" :

								tempStrValue = searchListResult.get(i).getListenUrl();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_v_filename" :
								tempStrValue = searchListResult.get(i).getvFileName();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								vFilename=false;
								break;
							case "r_s_sys_code" :

								tempStrValue = searchListResult.get(i).getsSysCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_s_hdd_flag" :

								tempStrValue = searchListResult.get(i).getsHddFlag();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_screen_url" :

								if(StringUtil.isNull(screenUrl,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(screenUrl);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_rec_visible" :

								tempStrValue = searchListResult.get(i).getRecVisible();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_stt_result":
								tempStrValue = searchListResult.get(i).getSttPlayer();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else 
									tempStrValue = messageSource.getMessage("views.sttResult.type."+tempStrValue.toUpperCase(), null,Locale.getDefault());

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_rec_volume" :
								//tempStrValue = searchListResult.get(i).getRecVisible();
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(messageSource.getMessage("views.search.column.value.volume", null,Locale.getDefault()));
								rowItem.getCellElements().add(cellInfo);
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue("1");
								rowItem.getCellElements().add(cellInfo);
								break;

							case "r_pdt_nm":
								// 상품명
								tempStrValue = searchListResult.get(i).getrPdtNm();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_info1":
								// 고객 성향 등급
								tempStrValue = searchListResult.get(i).getrCustInfo1();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_info2":
								//상품 위험 등급
								tempStrValue = searchListResult.get(i).getrCustInfo2();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_info3":
								//상품 설명 유무
								tempStrValue = searchListResult.get(i).getrCustInfo3();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_retry_rec":
								//상품 설명 유무
								tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat' onClick='retryRec(\""+searchListResult.get(i).getRecDate().replace("-", "") + searchListResult.get(i).getRecTime().replace(":", "") + searchListResult.get(i).getExtNum()+searchListResult.get(i).getvSysCode()+searchListResult.get(i).getRecStartType()+"\");'>재녹취</button>";
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_s_filename" :

								tempStrValue = searchListResult.get(i).getsFileName();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_s_upload_yn" :

								tempStrValue = searchListResult.get(i).getsUploadYn();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_t_sys_code" :

								tempStrValue = searchListResult.get(i).gettSysCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_eval_yn" :

								tempStrValue = searchListResult.get(i).getEvalYn();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_text_url" :

								tempStrValue = searchListResult.get(i).getTextUrl();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_listen_yn" :

								tempStrValue = searchListResult.get(i).getListenYn();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_t_filename" :

								tempStrValue = searchListResult.get(i).gettFileName();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_t_upload_yn" :

								tempStrValue = searchListResult.get(i).gettUploadYn();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_part_start" :

								tempStrValue = searchListResult.get(i).getPartStart();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_part_end" :

								tempStrValue = searchListResult.get(i).getPartEnd();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_marking1" :

								tempStrValue = searchListResult.get(i).getMarking1();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_marking2" :

								tempStrValue = searchListResult.get(i).getMarking2();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_marking3" :

								tempStrValue = searchListResult.get(i).getMarking3();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_marking4" :

								tempStrValue = searchListResult.get(i).getMarking4();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_social_num" :

								tempStrValue = searchListResult.get(i).getCustSocialNum();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else {
									if(safeDbResult.size() > 0) {
										if("Y".equals(safeDbResult.get(0).getConfigValue()))
											tempStrValue = searchListResult.get(i).SafeDBGetter(searchListResult.get(i).getCustSocialNum());
									}
								}
								
								//asd
								
								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName))) {
											
									if (maskingYNVal.equals("Y")) {
										// 메리츠에서는 maskingPhoneNumLast 함수 사용 (전화번호가 뒤에서부터 들어와서 거꾸로 마스킹)
										tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, socialStartIdx, socialEa);
									}
								}
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_contract_num" :

								tempStrValue = searchListResult.get(i).getContractNum();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_counsel_code" :

								tempStrValue = searchListResult.get(i).getCounselCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_counsel_result_bgcode" :
								tempStrValue = searchListResult.get(i).getCounselResultBgcode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_counsel_result_mgcode" :

								tempStrValue = searchListResult.get(i).getCounselResultMgcode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_counsel_result_sgcode" :

								tempStrValue = searchListResult.get(i).getCounselResultSgcode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_counsel_content" :

								tempStrValue = searchListResult.get(i).getCounselContent();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_address" :

								tempStrValue = searchListResult.get(i).getCustAddress();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_player_kind" :

								tempStrValue = searchListResult.get(i).getPlayerKind();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_t_contents" :

								tempStrValue = searchListResult.get(i).gettContents();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_receipt_num" :
								tempStrValue = searchListResult.get(i).getReceiptNum();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer1" :
								tempStrValue = searchListResult.get(i).getBuffer1();
								if(StringUtil.isNull(tempStrValue,true)) {
									if(consentNoRecodingUse.equals("Y")) {
										tempStrValue = messageSource.getMessage("use.yes", null,Locale.getDefault());
									} else {
										tempStrValue = "-";
									}
								} else {
									if(consentNoRecodingUse.equals("Y")) {
										if(!StringUtil.isNull(tempStrValue,true) && tempStrValue.equals("##")) {
											tempStrValue = messageSource.getMessage("use.no", null,Locale.getDefault());
										} else {
											tempStrValue = messageSource.getMessage("use.yes", null,Locale.getDefault());
										}
									}
								}
								
								if(recsee_mobile.equals("Y")) {
									orgTimeConnect = searchListResult.get(i).getCallStimeConnect();
									
									if("-".equals(orgTimeConnect)) {
										orgTimeConnect = messageSource.getMessage("views.search.grid.mobile.dropreason.fail", null,Locale.getDefault());
										
									}else {
										orgTimeConnect = messageSource.getMessage("views.search.grid.mobile.dropreason.success", null,Locale.getDefault());
																	
									}
									cellInfo = new dhtmlXGridRowCell();
									cellInfo.setValue(orgTimeConnect);	
									rowItem.getCellElements().add(cellInfo);
								}
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(searchListResult.get(i).getBuffer1());	
								rowItem.getCellElements().add(cellInfo);
								
								//렉시 모바일 사용중이면 drop reason 언어팩 적용
//								if(recsee_mobile.equals("Y")) {
//									if(tempStrValue != null) {
//										switch(tempStrValue) {
//											case "busy":
//											case "noAnswer":
//											case "notFound":
//											case "endCall":
//											case "phone":
//											case "custDrop":
//												tempStrValue = messageSource.getMessage("views.search.grid.mobile.dropreason." + tempStrValue, null,Locale.getDefault());
//												break;
//											default:
//												tempStrValue = "-";
//												break;
//										}
//									}
//								}
//								cellInfo = new dhtmlXGridRowCell();
//								cellInfo.setValue(tempStrValue);
//								rowItem.getCellElements().add(cellInfo);
								
								break;
							case "r_buffer2" :
								tempStrValue = searchListResult.get(i).getBuffer2();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer3" :
								//asd
								cellInfo = new dhtmlXGridRowCell();
								tempStrValue = searchListResult.get(i).getBuffer17();								
								if(!"".equals(tempStrValue) && tempStrValue != null) {	
									
									String startDate = String.valueOf(searchListResult.get(i).getRecDate().replace("-", ""));
									String tempValue = searchListResult.get(i).getBuffer17();
									
									
									SearchListInfo buffer3Count = new SearchListInfo();
									buffer3Count.setsDate(startDate);
									//buffer3Count.setBuffer3(tempStrValue);
									buffer3Count.setBuffer17(tempValue);
									
									int buffer3Chk = searchListInfoService.selectBuffer3Count(buffer3Count);
									
									if(buffer3Chk > 1) {										
										tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_trace_white' onClick='openReferencePop(\""+tempValue+"\",\"callkey\",\""+startDate+"\");'></button>";
									}else if(!StringUtil.isNull(searchListResult.get(i).getBuffer3(),true)){
										
										buffer3Count = new SearchListInfo();
										buffer3Count.setsDate(startDate);
										tempValue = searchListResult.get(i).getBuffer3();
										
										buffer3Count.setBuffer3(tempValue);
										//buffer3Count.setCallKeyAp(tempValue);
										
										buffer3Chk = searchListInfoService.selectBuffer3Count(buffer3Count);
										
										if(buffer3Chk > 1) {
											tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_trace_white' onClick='openReferencePop(\""+tempValue+"\",\"buffer3\",\""+startDate+"\");'></button>";
										}else {
											tempStrValue = "-";
										}	
									}else {
										tempStrValue = "-";
									}
									cellInfo.setValue(tempStrValue);
								}else {
									cellInfo.setValue("-");
								}					
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_bw_yn" :

								tempStrValue = searchListResult.get(i).getBwYn();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_bw_bg_code" :

								tempStrValue = searchListResult.get(i).getBwBgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_bw_sg_code" :

								tempStrValue = searchListResult.get(i).getBwSgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_screen_dual_url" :

								tempStrValue = searchListResult.get(i).getScreenDualUrl();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer4" :

								tempStrValue = searchListResult.get(i).getBuffer4();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer5" :

								tempStrValue = searchListResult.get(i).getBuffer5();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer6" :

								tempStrValue = searchListResult.get(i).getBuffer6();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer7" :

								tempStrValue = searchListResult.get(i).getBuffer7();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer8" :

								tempStrValue = searchListResult.get(i).getBuffer8();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer9" :

								tempStrValue = searchListResult.get(i).getBuffer9();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer10" :

								tempStrValue = searchListResult.get(i).getBuffer10();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								CommonCodeVO commCodeVoc = new CommonCodeVO();
								commCodeVoc.setParentCode("VOC");
								commCodeVoc.setCodeValue(tempStrValue);
								
								CommonCodeVO commCodeVocValue = commonCodeService.selectCommonName(commCodeVoc);
								
								if(commCodeVocValue != null)
										tempStrValue = commCodeVocValue.getCodeName();
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer11" :

								tempStrValue = searchListResult.get(i).getBuffer11();
								if(StringUtil.isNull(tempStrValue,true) || "null".equals(tempStrValue))
									tempStrValue = "";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer12" :

								tempStrValue = searchListResult.get(i).getBuffer12();
								if(StringUtil.isNull(tempStrValue,true) || "null".equals(tempStrValue))
									tempStrValue = "";
								else if(Eval_Thema.equals("master")){
									if(StringUtil.isNull(searchListResult.get(i).getAffilicateName(), true)){
//										tempStrValue = messageSource.getMessage("call.buffer12."+tempStrValue.trim(), null,Locale.getDefault());
										tempStrValue = "";
									}else{
										tempStrValue = searchListResult.get(i).getAffilicateName();
									}
								}
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer13" :

								tempStrValue = searchListResult.get(i).getBuffer13();
								if(StringUtil.isNull(tempStrValue,true) || "null".equals(tempStrValue))
									tempStrValue = "";
								else if(Eval_Thema.equals("master"))
										tempStrValue = messageSource.getMessage("call.buffer13."+tempStrValue.trim(), null,Locale.getDefault());
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer14" :
								tempStrValue = searchListResult.get(i).getBuffer14();
								
								if((!StringUtil.isNull(tempStrValue,true) && ("04".equals(tempStrValue) || "05".equals(tempStrValue))) || (!StringUtil.isNull(searchListResult.get(i).getRecStartType(),true) && ("P".equals(searchListResult.get(i).getRecStartType()))))
									tempStrValue = "영구";
								else 
									tempStrValue = "일반";
								
								if (maskingInfo!=null && maskingInfo.contains(searchTemplItem.get(columnName)))
									tempStrValue = new RecSeeUtil().makingName(tempStrValue);								
								
								// 천재 교과서 고객 ID 추가로 인한 r_buffer14 사용 20200107
								/*if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else if(Eval_Thema.equals("master")){
									tempStrValue = messageSource.getMessage("call.buffer14."+tempStrValue.trim(), null,Locale.getDefault());
								}*/
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer15" :

								tempStrValue = searchListResult.get(i).getBuffer15();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								
								CommonCodeVO commCodeService = new CommonCodeVO();
								commCodeService.setParentCode("SERVICE");
								commCodeService.setCodeValue(tempStrValue);
								
								CommonCodeVO commCodeServiceValue = commonCodeService.selectCommonName(commCodeService);
								
								if(commCodeServiceValue != null)
										tempStrValue = commCodeServiceValue.getCodeName();

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer16" :

								tempStrValue = searchListResult.get(i).getBuffer16();
								if(StringUtil.isNull(tempStrValue,true) || "null".equals(tempStrValue))
									tempStrValue = "";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer17" :
								tempStrValue = searchListResult.get(i).getBuffer17();
								if(StringUtil.isNull(tempStrValue,true) || "null".equals(tempStrValue))
									tempStrValue = "";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_buffer18" :
								tempStrValue = searchListResult.get(i).getBuffer18();
								if(StringUtil.isNull(tempStrValue,true) || "null".equals(tempStrValue))
									tempStrValue = "";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_buffer19" :

								tempStrValue = searchListResult.get(i).getBuffer19();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else {
									tempStrValue = URLDecoder.decode(tempStrValue);
								}
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_buffer20" :

								tempStrValue = searchListResult.get(i).getBuffer20();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cnid" :

								tempStrValue = searchListResult.get(i).getCnId();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_dest_ip" :

								tempStrValue = searchListResult.get(i).getDestIp();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_bg_name" :

								tempStrValue = searchListResult.get(i).getBgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else
									tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), organizationBgInfo);

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_mg_name" :

								tempStrValue = searchListResult.get(i).getMgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else
									tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(), organizationMgInfo);

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_sg_name" :

								tempStrValue = searchListResult.get(i).getSgCode();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";
								else
									tempStrValue = new FindOrganizationUtil().getOrganizationName(searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(), searchListResult.get(i).getSgCode(), organizationSgInfo);

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "evaluation" :
								if(Eval_Thema.equals("master")) { //마스타 자동차 평가
									String evalCheck = searchListResult.get(i).getBuffer20();
									if("Y".equals(evalCheck)) {
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue("");
										rowItem.getCellElements().add(cellInfo);
									}else {
										String callId = "";
										String agentName = "";
										String callRecDate = "";
										String callRecTime = "";
										String custName = "";
										String custPhone = "";
										String userType = "";
										String agentBgCode = "";
										String agentMgCode = "";
										String agentSgCode = "";
	
										if(!StringUtil.isNull(searchListResult.get(i).getUserId())) {
											callId = searchListResult.get(i).getUserId();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getUserName())) {
											agentName = searchListResult.get(i).getUserName();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getRecDate())) {
											callRecDate = searchListResult.get(i).getRecDate();
											callRecDate = callRecDate.replaceAll("-", "");
										}
										if(!StringUtil.isNull(searchListResult.get(i).getRecTime())) {
											callRecTime = searchListResult.get(i).getRecTime();
											callRecTime = callRecTime.replaceAll(":", "");
										}
										if(!StringUtil.isNull(searchListResult.get(i).getBgCode())) {
											agentBgCode = searchListResult.get(i).getBgCode();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getMgCode())) {
											agentMgCode = searchListResult.get(i).getMgCode();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getSgCode())) {
											agentSgCode = searchListResult.get(i).getSgCode();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getCustName())) {
											custName = searchListResult.get(i).getCustName();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getCustPhone1())) {
											custPhone = searchListResult.get(i).getCustPhone1();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getBuffer15())) {
											/*
											 * Master Car buffer15
											 * buffer15 Value Define(통계 용도)
											 * 1: 기타
											 * 2: 신입
											 * 3: 주간
											 * 4: 야간
											 * */
											userType = searchListResult.get(i).getBuffer15();
										}
	
										tempStrValue = "<button class='on_evaluation_open ui_btn_white ui_sub_btn_flat icon_btn_evaluation_white' onClick='openEvalPop(\""+searchListResult.get(i).getListenUrl()+"\",\""+callId+"\",\""+agentName+"\",\""+callRecDate+"\",\""+callRecTime+"\","
												+ "\""+custName+"\",\""+custPhone+"\",\""+userType+"\",\""+agentBgCode+"\",\""+agentMgCode+"\",\""+agentSgCode+"\")'></button>";
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue(tempStrValue);
										rowItem.getCellElements().add(cellInfo);
									}
									
								}else {//렉시 평가일경우(recsee)
									cellInfo = new dhtmlXGridRowCell();
									String callId = "", agentName = "", callRecDate = "", callRecTime = "", custName = "", custPhone = "";
									String userType = "", agentBgCode = "", agentMgCode = "", agentSgCode = "";
									String evalCheck = searchListResult.get(i).getBuffer20(); //Y이면 평가한거 빈값은 N이면 삭제 (Buffer20에서 16으로 바뀜)
									String evalBtnClass = "icon_btn_evaluation_white";
									
									// 평가완료이고 완료된 평가 보여주지 않기 상태일떄
									if("N".equals(CompletionView) && "Y".equals(evalCheck)) {
										cellInfo = new dhtmlXGridRowCell();
										cellInfo.setValue("");
										rowItem.getCellElements().add(cellInfo);
									}else {
										
										if("Y".equals(evalCheck)) {								
											evalBtnClass = "icon_btn_evaluation_gray";
										}
										
										if(!StringUtil.isNull(searchListResult.get(i).getUserId())) {
											callId = searchListResult.get(i).getUserId();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getUserName())) {
											agentName = searchListResult.get(i).getUserName();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getRecDate())) {
											callRecDate = searchListResult.get(i).getRecDate();
											callRecDate = callRecDate.replaceAll("-", "");
										}
										if(!StringUtil.isNull(searchListResult.get(i).getRecTime())) {
											callRecTime = searchListResult.get(i).getRecTime();
											callRecTime = callRecTime.replaceAll(":", "");
										}
										if(!StringUtil.isNull(searchListResult.get(i).getBgCode())) {
											agentBgCode = searchListResult.get(i).getBgCode();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getMgCode())) {
											agentMgCode = searchListResult.get(i).getMgCode();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getSgCode())) {
											agentSgCode = searchListResult.get(i).getSgCode();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getCustName())) {
											custName = searchListResult.get(i).getCustName();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getCustPhone1())) {
											custPhone = searchListResult.get(i).getCustPhone1();
										}
										if(!StringUtil.isNull(searchListResult.get(i).getBuffer15())) {
											/*
											 * Master Car buffer15
											 * buffer15 Value Define(통계 용도)
											 * 1: 기타
											 * 2: 신입
											 * 3: 주간
											 * 4: 야간
											 * */
											userType = searchListResult.get(i).getBuffer15();
										}
	
										tempStrValue = "<button class='on_evaluation_open ui_btn_white ui_sub_btn_flat "+evalBtnClass+"' onClick='openEvalPop(\""+searchListResult.get(i).getListenUrl()+"\",\""+callId+"\",\""+agentName+"\",\""+callRecDate+"\",\""+callRecTime+"\","
												+ "\""+custName+"\",\""+custPhone+"\",\""+userType+"\",\""+agentBgCode+"\",\""+agentMgCode+"\",\""+agentSgCode+"\",\""+evalCheck+"\")'></button>";
										
										cellInfo.setValue(tempStrValue);
										rowItem.getCellElements().add(cellInfo);
									}
								}
								break;
							case "screen" :

								if(!StringUtil.isNull(searchListResult.get(i).getsUploadYn(),true) && "Y".equals(searchListResult.get(i).getsUploadYn())) {

									JSONObject obj = new JSONObject();
									obj.put("listenUrl", 	(StringUtil.isNull(searchListResult.get(i).getListenUrl(),true) ? "" : searchListResult.get(i).getListenUrl()) );
									obj.put("recDate", 		(StringUtil.isNull(searchListResult.get(i).getRecDate(),true) ? "" : searchListResult.get(i).getRecDate()) );
									obj.put("recTime",  	(StringUtil.isNull(searchListResult.get(i).getRecTime(),true) ? "" : searchListResult.get(i).getRecTime()) );
									obj.put("recExt",  		(StringUtil.isNull(searchListResult.get(i).getExtNum(),true) ? "" : searchListResult.get(i).getExtNum()) );
									obj.put("recCustPhone", (StringUtil.isNull(searchListResult.get(i).getCustPhone1(),true) ? "" : searchListResult.get(i).getCustPhone1()) );
									obj.put("recUserName",  (StringUtil.isNull(searchListResult.get(i).getUserName(),true) ? "" : searchListResult.get(i).getUserName()) );

									// 우선 열람서버 무시하고,
									// 로컬에 있는 경로만 바라보게 처리 한다.

									if("N".equals(searchListResult.get(i).getSsendFileFlag())) 
										screenUrl = "http://"+searchListResult.get(i).getsRecIp()+":28881/view?url="+searchListResult.get(i).getsRecFullpath();
									else
										screenUrl = "http://"+searchListResult.get(i).getsReadIp()+":28881/view?url="+searchListResult.get(i).getsReadFullpath();

									obj.put("screenUrl",  screenUrl);

									tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_view_white' onClick='openScreenPop("+obj+")'></button>";
								} else if(!StringUtil.isNull(searchListResult.get(i).getsUploadYn(),true) && "D".equals(searchListResult.get(i).getsUploadYn())) {
									tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_view_white' onClick='alert(\"녹취파일이 삭제되었습니다\")'></button>;";
								}else {
									tempStrValue = "";
								}
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_stt_player" :
								if("Y".equals(sttPlayerYN)) {
									cellInfo = new dhtmlXGridRowCell();
									
									tempStrValue = searchListResult.get(i).getSttPlayer();
									if("S".equals(tempStrValue)) {
										tempStrValue="<button class='ui_btn_white ui_sub_btn_flat icon_btn_historySearch_white2' onClick='sttPlayerPopup(\""+searchListResult.get(i).getRecDate().replace("-", "") + searchListResult.get(i).getRecTime().replace(":", "") + searchListResult.get(i).getExtNum()+searchListResult.get(i).getvSysCode()+"\")'></button>";
										cellInfo.setValue(tempStrValue);
									}else {
										cellInfo.setValue("-");
									}
									rowItem.getCellElements().add(cellInfo);
								}else {
									cellInfo = new dhtmlXGridRowCell();
									tempStrValue="";
									cellInfo.setValue(tempStrValue);
									rowItem.getCellElements().add(cellInfo);
								}
								break;
							case "r_vdo_url" :

								tempStrValue = searchListResult.get(i).getVdoUrl();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_cust_id" :

								tempStrValue = searchListResult.get(i).getCustId();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_regi_date" :

								tempStrValue = searchListResult.get(i).getRegiDate();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_manager" :

								tempStrValue = searchListResult.get(i).getManager();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_srtp" :

								tempStrValue = searchListResult.get(i).getsRtp();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "f_key" :

								tempStrValue = searchListResult.get(i).getfKey();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_ssrc" :

								tempStrValue = searchListResult.get(i).getsSrc();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_key" :

								tempStrValue = searchListResult.get(i).getrKey();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_co_num" :

								tempStrValue = searchListResult.get(i).getCoNum();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_did_num" :

								tempStrValue = searchListResult.get(i).getDidNum();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_uei_data" :

								tempStrValue = searchListResult.get(i).getUeiData();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_queue_no1" :
								
								tempStrValue = searchListResult.get(i).getQueueNo1();
								if(StringUtil.isNull(tempStrValue,true)) 
									tempStrValue = "-";
//								else {
//									QueueInfo queueInfo = new QueueInfo();
//									queueInfo.setrQueueNum(tempStrValue);
//									List<QueueInfo> resultQueData = queueInfoService.selectQueueInfo(queueInfo);
//									if(resultQueData.size()>0) {
//										tempStrValue = resultQueData.get(0).getrQueueName();
//									}
//								}
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_queue_no2" :

								tempStrValue = searchListResult.get(i).getQueueNo2();
								if(StringUtil.isNull(tempStrValue,true)) 
									tempStrValue = "-";
//								else {
//									QueueInfo queueInfo = new QueueInfo();
//									queueInfo.setrQueueNum(tempStrValue);
//									List<QueueInfo> resultQueData = queueInfoService.selectQueueInfo(queueInfo);
//									if(resultQueData.size()>0) {
//										tempStrValue = resultQueData.get(0).getrQueueName();
//									}
//								}
								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_rec_start_type" :
								recStartType = false;
								tempStrValue = searchListResult.get(i).getRecStartType();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "trace" :

								if(
										!StringUtil.isNull(searchListResult.get(i).getCallId1(),true) &&
										/*searchListResult.get(i).getCallId2() != null && */
										/*!searchListResult.get(i).getCallId1().trim().isEmpty() &&*/
										/*!searchListResult.get(i).getCallId2().trim().isEmpty() && */
										// 호전환 수신,송신,회의통화 일경우 트레이스 표출
										(
											"TR".equals (searchListResult.get(i).getCallKind1())||
											"TR".equals (searchListResult.get(i).getCallKind2())||
											"TS".equals (searchListResult.get(i).getCallKind1())||
											"TS".equals (searchListResult.get(i).getCallKind2())||
											"C".equals (searchListResult.get(i).getCallKind1())||
											"C".equals (searchListResult.get(i).getCallKind2())
										) 
										&&
										("A".equals(searchListResult.get(i).getRecStartType()))
										&&
										(!"".equals(searchListResult.get(i).getCallId2()) && searchListResult.get(i).getCallId2() != null)
									)
								{

									JSONObject obj = new JSONObject();

									obj.put("listenUrl", 	(StringUtil.isNull(searchListResult.get(i).getListenUrl(),true) ? "" : searchListResult.get(i).getListenUrl()) );
									obj.put("recDate", 		(StringUtil.isNull(searchListResult.get(i).getRecDate(),true) ? "" : searchListResult.get(i).getRecDate()) );
									obj.put("recTime",  	(StringUtil.isNull(searchListResult.get(i).getRecTime(),true) ? "" : searchListResult.get(i).getRecTime()) );
									obj.put("recExt",  		(StringUtil.isNull(searchListResult.get(i).getExtNum(),true) ? "" : searchListResult.get(i).getExtNum()) );
									obj.put("recCustPhone", (StringUtil.isNull(searchListResult.get(i).getCustPhone1(),true) ? "" : new RecSeeUtil().maskingNumber(searchListResult.get(i).getCustPhone1())) );
									obj.put("recUserName",  (StringUtil.isNull(searchListResult.get(i).getUserName(),true) ? "" : searchListResult.get(i).getUserName()) );
									obj.put("recvFileName",	(StringUtil.isNull(searchListResult.get(i).getvFileName(),true) ? "" : searchListResult.get(i).getvFileName()) );
									obj.put("recMemo",  	(StringUtil.isNull(searchListResult.get(i).getMemoInfo(),true) ? "" : searchListResult.get(i).getMemoInfo()) );
									obj.put("callKeyAp",  	(StringUtil.isNull(searchListResult.get(i).getCallKeyAp(),true) ? "" : searchListResult.get(i).getCallKeyAp()) );
									obj.put("callId1",  	(StringUtil.isNull(searchListResult.get(i).getCallId1(),true) ? "" : searchListResult.get(i).getCallId1()) );

									//tempStrValue = "<img src='"+request.getContextPath() + "/resources/common/images/trace.png' class='btn_trace'>";
									tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_trace_white' onClick='openTracePop("+obj+");'></button>";
								} else {
									tempStrValue = "-";
								}
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_list_add" :

								String custPhone = "";
								if (!StringUtil.isNull(searchListResult.get(i).getCustPhone1(),true)) {
									custPhone = searchListResult.get(i).getCustPhone1();
									
									try {
										if (maskingInfo!=null && maskingInfo.contains("r_cust_phone1")) {
											if (PrefixYNVal.equals("Y")) {
												custPhone = new RecSeeUtil().prefixPhoneNum(custPhone, arrPrefixInfo);
											}
											if (maskingYNVal.equals("Y")) {
												// 메리츠에서는 maskingPhoneNumLast 함수 사용 (전화번호가 뒤에서부터 들어와서 거꾸로 마스킹)
												custPhone = new RecSeeUtil().maskingPhoneNum(custPhone, startIdx, ea);
											}
											if (hyphenYNVal.equals("Y")) {
												custPhone = new RecSeeUtil().setHyphenNum(custPhone, h1, h2);
											}
										}
									}catch (Exception e) {
										custPhone = searchListResult.get(i).getCustPhone1();
									}
								} 
								
								
								JSONObject obj = new JSONObject();

								obj.put("listenUrl", 	(StringUtil.isNull(searchListResult.get(i).getListenUrl(),true) ? "" : searchListResult.get(i).getListenUrl()) );
								obj.put("recDate", 		(StringUtil.isNull(searchListResult.get(i).getRecDate(),true) ? "" : searchListResult.get(i).getRecDate()).replaceAll("-", "") );
								obj.put("recTime",  	(StringUtil.isNull(searchListResult.get(i).getRecTime(),true) ? "" : searchListResult.get(i).getRecTime()) );
								obj.put("recExt",  		(StringUtil.isNull(searchListResult.get(i).getExtNum(),true) ? "" : searchListResult.get(i).getExtNum()) );
//								obj.put("recCustPhone", (StringUtil.isNull(searchListResult.get(i).getCustPhone1(),true) ? "" :new RecSeeUtil().maskingNumber(searchListResult.get(i).getCustPhone1().replaceAll("-", ""))) );
								obj.put("recCustPhone", custPhone);
								obj.put("recUserName",  (StringUtil.isNull(searchListResult.get(i).getUserName(),true) ? "" : searchListResult.get(i).getUserName()) );
								obj.put("recvFileName",	(StringUtil.isNull(searchListResult.get(i).getvFileName(),true) ? "" : searchListResult.get(i).getvFileName()) );
								obj.put("recMemo",  	(StringUtil.isNull(searchListResult.get(i).getMemoInfo(),true) ? "" : searchListResult.get(i).getMemoInfo()) );
								tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_addBtn_white' onClick='addPlayList("+obj+")'></button>";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_memo" :

								JSONObject jsonObj = new JSONObject();
								jsonObj.put("recDate", searchListResult.get(i).getRecDate());
								jsonObj.put("recTime", searchListResult.get(i).getRecTime());
								jsonObj.put("extNum", searchListResult.get(i).getExtNum());
								jsonObj.put("rowId", searchListResult.get(i).getRecDate().replace("-", "") + searchListResult.get(i).getRecTime().replace(":", "") + searchListResult.get(i).getExtNum()+searchListResult.get(i).getvSysCode());

								if (StringUtil.isNull(searchListResult.get(i).getTag(),true) || searchListResult.get(i).getTag().equals("0") ) {
									jsonObj.put("save", "insert");
									jsonObj.put("tag", "");
									tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_pen_white' onClick='tagPopupGrid("+jsonObj+")'></button>";
								}else {
									jsonObj.put("save", "modify");
									jsonObj.put("tag", searchListResult.get(i).getTag());
									tempStrValue = "<button class='ui_btn_white ui_main_btn_flat btn_icon_tag_white' onClick='tagPopupGrid("+jsonObj+")'></button>";
								}

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_stock_no" :

								tempStrValue = searchListResult.get(i).getStockNo();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_rec_summary" :
								
								tempStrValue = "";
								String recSum = searchListResult.get(i).getRecSummary();	// 전체 텍스트
								if(StringUtil.isNull(recSum,true)) {
									tempStrValue = "-";
								}else {
									String containKeyword = null;	// 키워드 - 현재는 키워드 기준으로 검색할때만 통화내용요약 보여주게 되어있음, 통화내용에 텍스트쳐서 검색하는건 문장입력하는 경우까지 생각해야되서 일단은 미뤄둠... 죄송ㅜㅜ
									// 검색해서 나온 전체 텍스트에 첫번째키워드가 있으면 그게 기준이고 없으면 그 다음 순 키워드가 기준임... 근데 키워드가 아무것도 없으면 문장요약 패스~
									for(int k=0; k<searchListInfo.getRecKeyword().size(); k++) {
										if(recSum.contains(searchListInfo.getRecKeyword().get(k))) {
											containKeyword = searchListInfo.getRecKeyword().get(k);
											break;
										}
									}
									if(StringUtil.isNull(containKeyword,true)) {
										tempStrValue = "-";
									}else {
										// 키워드 기준으로 split했을때 뒤에 길이가 50자 이하면 나머지를 더 붙여줌
										if(recSum.split(containKeyword)[1].length()<50) {
											for(int j=1; j<recSum.split(containKeyword).length; j++) {
												tempStrValue += recSum.split(containKeyword)[j];
											}
										}else {
											tempStrValue = recSum.split(containKeyword)[1];
										}
										// 키워드 뒤에 나올 글자는 최대 50자까지만 보여줌 -> 원래 키워드 기준으로 앞문장, 뒷문장 붙여주려고 했는데 일단은 이렇게만 구현함...
										if(tempStrValue.length()<50) {
											tempStrValue = "<b style='color:red;'>"+containKeyword+"</b>"+tempStrValue;
										}else {
											tempStrValue = "<b style='color:red;'>"+containKeyword+"</b>"+tempStrValue.substring(0, 50);
										}
									}
								}

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_log_listen" :

								jsonObj = new JSONObject();
								jsonObj.put("recDate", searchListResult.get(i).getRecDate());
								jsonObj.put("recTime", searchListResult.get(i).getRecTime());
								jsonObj.put("extNum", searchListResult.get(i).getExtNum());
								
								if (!StringUtil.isNull(searchListResult.get(i).getLogListen(),true) && Integer.parseInt(searchListResult.get(i).getLogListen())>0 ) {
									tempStrValue = "<button class='ui_btn_white ui_sub_btn_flat icon_btn_historySearch_white' onClick='logPopupGrid("+jsonObj+")'></button>";
								}else {
									tempStrValue = "-";
									// 청취이력 색깔 표시
									if(listenColor.equals("Y")) {
										rowItem.setStyle("color:rgb(96, 179, 220);");
									}
								}

								
								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
								break;
							case "r_call_key_ap" :

								tempStrValue = searchListResult.get(i).getCallKeyAp();
								if(StringUtil.isNull(tempStrValue,true))
									tempStrValue = "-";

								cellInfo = new dhtmlXGridRowCell();
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_division" :
								
								cellInfo = new dhtmlXGridRowCell();
								tempStrValue = searchListResult.get(i).getrTranscriptStatus();
								if (!StringUtil.isNull(tempStrValue,true)) {
									cellInfo.setValue(messageSource.getMessage("views.search.title.rDivision", null,Locale.getDefault()));									
								} else {
									cellInfo.setValue("-");	
								}
								rowItem.getCellElements().add(cellInfo);

								break;
							case "r_phone_mapping":
								cellInfo = new dhtmlXGridRowCell();
								
								tempStrValue = searchListResult.get(i).getCompanyTelno();
								if (StringUtil.isNull(tempStrValue,true)) {
									cellInfo.setValue("-");
								}else {
									cellInfo.setValue(tempStrValue);
								}
								rowItem.getCellElements().add(cellInfo);
							
								break;
							case "r_company_telno" :
		                        cellInfo = new dhtmlXGridRowCell();
		                        
		                        tempStrValue = searchListResult.get(i).getCompanyTelno();
		                        if (StringUtil.isNull(tempStrValue,true)) {
		                           cellInfo.setValue("-");
		                        }else {
		                           cellInfo.setValue(tempStrValue);
		                        }
		                        rowItem.getCellElements().add(cellInfo);
		                     
		                        break;
		                     case "r_company_telno_nick" :

								cellInfo = new dhtmlXGridRowCell();
								tempStrValue = "-";
								
								if ("Y".equals(extNickYN) && subNumberInfoResult != null && subNumberInfoResult.size() > 0){
									for (int s = 0; s < subNumberInfoResult.size(); s++) {
										String subNumber = subNumberInfoResult.get(s).getTelNo();
										if (subNumber.equals(searchListResult.get(i).getCompanyTelno())) {
											tempStrValue = subNumberInfoResult.get(s).getNickName();
											break;
										}
									}
								}
								cellInfo.setValue(tempStrValue);
								rowItem.getCellElements().add(cellInfo);
							
								break;
		                     case "r_product_type" :
		                    	cellInfo = new dhtmlXGridRowCell();
								tempStrValue = searchListResult.get(i).getBuffer7();
								if (StringUtil.isNull(tempStrValue,true)) {
								   cellInfo.setValue("-");
								}else {
									if(tempStrValue.startsWith("T")) {
										cellInfo.setValue("투자 성향 분석");
									} else {
										cellInfo.setValue("상품 설명");
									}
								}
								rowItem.getCellElements().add(cellInfo);
								break;
								
							}
						}
						// 아래 6개는 메모 입력시 필수 조건이므로,
						// 만약에 사용자 설정으로 그리드에서 빠지게 된다면, 숨김처리 하여 따로 보여 줘야 한다..
						// 그래야 메모 저장 시 값을 들고가서 저장이 가능하다.
						if(rownumber) {
							
							tempStrValue = searchListResult.get(i).getRownumber();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);
						}
						
						if(recDate) {
							
							tempStrValue = searchListResult.get(i).getRecDate();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);

						}

						if(recTime) {
							tempStrValue = searchListResult.get(i).getRecTime();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);

						}

						if(extNum) {
							tempStrValue = searchListResult.get(i).getExtNum();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);

						}

						if(vFilename) {
							tempStrValue = searchListResult.get(i).getvFileName();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);

						}

						if(custPhone1) {

							tempStrValue = searchListResult.get(i).getCustPhone1();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);

						}

						if(recUserName) {
							tempStrValue = searchListResult.get(i).getUserName();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);

						}

						if(recCustName) {
							tempStrValue = searchListResult.get(i).getCustName();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";
							else {
								if(safeDbResult.size() > 0) {
									if("Y".equals(safeDbResult.get(0).getConfigValue()))
										tempStrValue = searchListResult.get(i).SafeDBGetter(searchListResult.get(i).getCustName());
								}
							}
							if (maskingInfo!=null && maskingInfo.contains("r_cust_name"))
								tempStrValue=new RecSeeUtil().makingName(tempStrValue);

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);
						}
						
						if(recStartType) {
							tempStrValue = searchListResult.get(i).getRecStartType();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "-";

							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);
						}

						if(vSysCode) {
							// 듀얼렉시땜시 시스템 코드 필요 hidden으로 가지고있기
							tempStrValue = searchListResult.get(i).getvSysCode();
							if(StringUtil.isNull(tempStrValue,true))
								tempStrValue = "";
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(tempStrValue);
							rowItem.getCellElements().add(cellInfo);
						}
						
						// 메모
						tempStrValue = searchListResult.get(i).getMemoInfo();
						if(StringUtil.isNull(tempStrValue,true))
							tempStrValue = "-";

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(tempStrValue);
						rowItem.getCellElements().add(cellInfo);

						// 리슨 유알엘
						tempStrValue = searchListResult.get(i).getListenUrl();
						if(StringUtil.isNull(tempStrValue,true))
							tempStrValue = "1";

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(tempStrValue);
						rowItem.getCellElements().add(cellInfo);
						
						
						// 리슨 유알엘2
						tempStrValue = searchListResult.get(i).getListenUrl();
						if(StringUtil.isNull(tempStrValue,true))
							tempStrValue = "-";

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(tempStrValue);
						rowItem.getCellElements().add(cellInfo);
						
						xmls.getRowElements().add(rowItem);


						rowItem = null;
					}

					if (StringUtil.isNull(request.getParameter("mode")) || !"trace".equals(request.getParameter("mode"))) {
						Integer totalListResult = 0;
						if(!StringUtil.isNull(request.getParameter("csv"),true)) {
							totalListResult = searchListResult!=null ?searchListResult.size() : 0;
						}else {
							totalListResult = searchListInfoService.totalSearchListInfo(searchListInfo);
						}

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
						logService.writeLog(request, "RECSEARCH", "DO", searchListInfo.toLogString().replace("TotalCount", "Total Count="+totalListResult));
					}
				}
			}
		}else{
			xmls=null;
		}
		return xmls;
	}

	//	조회 및 청취 메모 그리드
	@RequestMapping(value="/recMemo_list.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml recMemoListGrid(HttpServletRequest request, HttpServletResponse response) {

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
			// logger.error("error",e);
		}
		
		if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
			// 처음 헤더 xml
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
			column.setId("recDate");
			column.setWidth("100");
			column.setType("ro");
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
			column.setHidden("1");
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
			column.setHidden("1");
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

			//	대분류
			
			column = new dhtmlXGridHeadColumn();
			if(etcConfigResult.size() > 0 && etcConfigResult.get(0) != null && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
				column.setHidden("1");
			}
			column.setId("bgCode");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_BG_CODE", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			//	중분류
			column = new dhtmlXGridHeadColumn();
			if(etcConfigResult.size() > 0 && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
				column.setHidden("1");
			}
			column.setId("mgCode");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_MG_CODE", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			//	소분류
			column = new dhtmlXGridHeadColumn();
			if(etcConfigResult.size() > 0 && "Y".equals(etcConfigResult.get(0).getConfigValue())) {
				column.setHidden("1");
			}
			column.setId("sgCode");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_SG_CODE", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			//	메모
			column = new dhtmlXGridHeadColumn();
			column.setId("recMemo");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='recDate' style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_REC_MEMO", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			xmls.setHeadElement(head);

		}else {
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			SearchListInfo searchListInfo = new SearchListInfo();
			if(!StringUtil.isNull(request.getParameter("date"),true))
				searchListInfo.setRecDate(request.getParameter("date").replaceAll("-",""));
			if(!StringUtil.isNull(request.getParameter("time"),true))
				searchListInfo.setRecTime(request.getParameter("time").replaceAll(":", ""));
			if(!StringUtil.isNull(request.getParameter("ext"), true))
				searchListInfo.setExtNum(request.getParameter("ext"));

			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P20001");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

			List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
			if(!accessResult.get(0).getAccessLevel().equals("A")) {
				HashMap<String, String> item = new HashMap<String, String>();

				searchListInfo.setBgCode(userInfo.getBgCode());

				if(!accessResult.get(0).getAccessLevel().equals("B")) {
					searchListInfo.setMgCode(userInfo.getMgCode());
				}
				if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M")) {
					searchListInfo.setSgCode(userInfo.getSgCode());
				}
				if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M") && !accessResult.get(0).getAccessLevel().equals("S")) {
					searchListInfo.setUserId(userInfo.getUserId());
				}
			}


			List<RecMemo> resultMemo  = searchListInfoService.selectRecMemo(searchListInfo);

			for(int i=0; i < resultMemo.size(); i++) {
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());

				// recDate
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.format("%s-%s-%s", resultMemo.get(i).getRecDate().substring(0, 4), resultMemo.get(i).getRecDate().substring(4,  6), resultMemo.get(i).getRecDate().substring(6, 8)));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//recTime
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.format("%s:%s:%s", resultMemo.get(i).getRecTime().substring(0, 2), resultMemo.get(i).getRecTime().substring(2,  4), resultMemo.get(i).getRecTime().substring(4, 6)));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//ext
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultMemo.get(i).getExtNum());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//userID
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultMemo.get(i).getUserId());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//userName
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultMemo.get(i).getUserName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//bgname
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultMemo.get(i).getBgName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//mgname
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultMemo.get(i).getMgName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//sgname
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultMemo.get(i).getSgName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;


				//memo
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("memoIdx", resultMemo.get(i).getMemoIdx());
				jsonObj.put("recDate", resultMemo.get(i).getRecDate());
				jsonObj.put("recTime", resultMemo.get(i).getRecTime());
				jsonObj.put("extNum", resultMemo.get(i).getExtNum());
				jsonObj.put("tag", resultMemo.get(i).getMemo());
				jsonObj.put("save", "modify");
				jsonObj.put("userId",resultMemo.get(i).getUserId());
				jsonObj.put("rowId", String.valueOf(i+1));

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='ui_btn_white ui_main_btn_flat btn_icon_tag_white' style='vertical-align : middle; height:25px;' onClick='tag("+jsonObj+")'></button>");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}

		return xmls;
	}

	//	조회 및 청취 청취 이력 그리드
	@RequestMapping(value="/logListen_list.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml logListenListGrid(HttpServletRequest request, HttpServletResponse response) {

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		xmls = new dhtmlXGridXml();
		if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
			String titleBaseName = "views.search.grid.head.";
			// 처음 헤더 xml
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
			column.setId("listenDate");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='listenDate' style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "listenDate", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			column = new dhtmlXGridHeadColumn();
			column.setId("listenTime");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='listenTime' style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "listenTime", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			// 사번
			column = new dhtmlXGridHeadColumn();
			column.setId("listenUserId");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='listenUserId' style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "listenUserId", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			//	상담사명
			column = new dhtmlXGridHeadColumn();
			column.setId("listenUserName");
			column.setWidth("80");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("<div id='listenUserName' style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "listenUserName", null,Locale.getDefault())+"</div>");
			head.getColumnElement().add(column);
			column = null;

			xmls.setHeadElement(head);

		}else {
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			SearchListInfo searchListInfo = new SearchListInfo();
			if(!StringUtil.isNull(request.getParameter("date"),true))
				searchListInfo.setRecDate(request.getParameter("date").replaceAll("-",""));
			if(!StringUtil.isNull(request.getParameter("time"),true))
				searchListInfo.setRecTime(request.getParameter("time").replaceAll(":", ""));
			if(!StringUtil.isNull(request.getParameter("ext"), true))
				searchListInfo.setExtNum(request.getParameter("ext"));

			List<SearchListInfo> resultLog  = searchListInfoService.selectLogListen(searchListInfo);

			for(int i=0; i < resultLog.size(); i++) {
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());

				// listenDate
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.format("%s-%s-%s", resultLog.get(i).getListenDate().substring(0, 4), resultLog.get(i).getListenDate().substring(4,  6), resultLog.get(i).getListenDate().substring(6, 8)));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				// listenTime
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.format("%s:%s:%s", resultLog.get(i).getListenTime().substring(0, 2), resultLog.get(i).getListenTime().substring(2,  4), resultLog.get(i).getListenTime().substring(4, 6)));
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//userID
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultLog.get(i).getUserId());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				//userName
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(resultLog.get(i).getUserName());
				rowItem.getCellElements().add(cellInfo);
				cellInfo = null;

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}

		return xmls;
	}
	
	
	// 다운로드 요청 관리 그리드
//	@RequestMapping(value="/approve_list.xml", method=RequestMethod.GET, produces = "application/xml")
//	public @ResponseBody dhtmlXGridXml approveListGrid(HttpServletRequest request, HttpServletResponse response) {
//
//		CookieSetToLang cls = new CookieSetToLang();
//		cls.langSetFunc(request, response);
//
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		dhtmlXGridXml xmls = null;
//
//		if (userInfo != null) {
//
//			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
//			safeDBetcConfigInfo.setGroupKey("SEARCH");
//			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
//			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);
//
//			xmls = new dhtmlXGridXml();
//
//			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
//				xmls.setHeadElement(new dhtmlXGridHead());
//
//				dhtmlXGridHead head = new dhtmlXGridHead();
//
//				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
//
//				for(int i=0;i<33;i++) {
//					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
//
//					column.setType("ro");
//					column.setSort("str");
//					column.setAlign("center");
//					column.setFiltering("1");
//					column.setEditable("0");
//					column.setCache("1");
//					column.setHidden("0");
//
//					switch(i) {
//					case 0:
//						/*column.setType("ch");
//						column.setFiltering("0");
//						column.setEditable("0");
//						column.setCache("0");
//						column.setWidth("50");
//						column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
//						break;*/
//						column.setSort("int");
//						column.setFiltering("0");
//						column.setEditable("0");
//						column.setCache("0");
//						column.setWidth("50");
//						column.setValue("번호");
//						break;
//					case 1://
//						column.setWidth("90");
//						column.setValue("요청 일자");
//						break;
//					case 2://
//						column.setWidth("80");
//						column.setValue("요청 시간");
//						break;
//					case 3://
//						column.setWidth("100");
//						column.setValue("요청자 사번");
//						break;
//					case 4://
//						column.setWidth("100");
//						column.setValue("요청자 이름");
//						break;
//					case 5://
//						column.setWidth("120");
//						column.setValue("요청자 소속");
//						break;
//					case 6://
//						column.setWidth("80");
//						column.setType("combo");
//						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=approveType");
//						column.setValue("요청 유형");
//						break;
//					case 7://
//						column.setWidth("120");
//						column.setType("combo");
//						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=approveReason");
//						column.setValue("요청 사유");
//						break;
//					case 8://
//						column.setWidth("150");
//						column.setType("combo");
//						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=approveState");
//						column.setValue("진행 상태");
//						break;
//					case 9://
//						column.setWidth("200");
//						column.setValue("파일명");
//						break;
//					case 10://
//						column.setWidth("90");
//						column.setValue("녹취 일자");
//						break;
//					case 11://
//						column.setWidth("80");
//						column.setValue("녹취 시간");
//						break;
//					case 12://
//						column.setWidth("80");
//						column.setValue("녹취 내선");
//						break;
//					case 13://
//						column.setWidth("80");
//						column.setValue("녹취자 사번");
//						break;
//					case 14://
//						column.setWidth("80");
//						column.setValue("녹취자 이름");
//						break;
//					case 15://
//						column.setWidth("80");
//						column.setValue("녹취 고객명");
//						break;
//					case 16://
//						column.setWidth("120");
//						column.setValue("녹취 고객 전화번호");
//						break;
//					case 17://
//						column.setWidth("100");
//						column.setValue("녹취 증권번호");
//						break;
//					case 18://
//						column.setWidth("80");
//						column.setValue("녹취 콜타입");
//						break;
//					case 19://
//						column.setWidth("90");
//						column.setValue("지점 접수 날짜");
//						break;
//					case 20://
//						column.setWidth("90");
//						column.setValue("지점 접수 시간");
//						break;
//					case 21://
//						column.setWidth("102");
//						column.setValue("지점 접수자 사번");
//						break;
//					case 22://
//						column.setWidth("90");
//						column.setValue("접수 처리 날짜");
//						break;
//					case 23://
//						column.setWidth("90");
//						column.setValue("접수 처리 시간");
//						break;
//					case 24://
//						column.setWidth("100");
//						column.setValue("접수자 사번");
//						break;
//					case 25://
//						column.setWidth("90");
//						column.setValue("승인 처리 날짜");
//						break;
//					case 26://
//						column.setWidth("90");
//						column.setValue("승인 처리 시간");
//						break;
//					case 27://
//						column.setWidth("100");
//						column.setValue("승인자 사번");
//						break;
//					case 28://
//						column.setWidth("100");
//						column.setValue("요청 기간");
//						break;
//					case 29://
//						column.setWidth("100");
//						column.setValue("ip");
//						column.setHidden("1");
//						break;
//					case 30://대그룹코드
//						column.setWidth("100");
//						column.setValue("bgcode");
//						column.setHidden("1");
//						break;
//					case 31://중그룹코드
//						column.setWidth("100");
//						column.setValue("mgcode");
//						column.setHidden("1");
//						break;
//					case 32://소그룹코드
//						column.setWidth("100");
//						column.setValue("sgcode");
//						column.setHidden("1");
//						break;
//					}
//					head.getColumnElement().add(column);
//					column = null;
//				}
//
//				xmls.setHeadElement(head);
//
//			}else {
//				ApproveListInfo approveListInfo = new ApproveListInfo();
//
//				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
//				accessInfo.setLevelCode(userInfo.getUserLevel());
//				accessInfo.setProgramCode("P10027");
//				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
//
//				// 그룹 조회 권한
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
//				// 접수, 승인 권한
//				approveListInfo.setApproveYn(accessResult.get(0).getApproveYn());
//				approveListInfo.setReciptYn(accessResult.get(0).getReciptYn());
//				approveListInfo.setPrereciptDate(accessResult.get(0).getPrereciptYn());
//
//				if(authyInfo != null && authyInfo.size() > 0) {
//					approveListInfo.setAuthyInfo(authyInfo);
//				}
//
//				if(!StringUtil.isNull(request.getParameter("reqsDate"),true))
//					approveListInfo.setReqsDate(request.getParameter("reqsDate"));
//
//				if(!StringUtil.isNull(request.getParameter("reqeDate"),true))
//					approveListInfo.setReqeDate(request.getParameter("reqeDate"));
//
//				if(!StringUtil.isNull(request.getParameter("reqsTime"),true))
//					approveListInfo.setReqsTime(request.getParameter("reqsTime"));
//
//				if(!StringUtil.isNull(request.getParameter("reqeTime"),true))
//					approveListInfo.setReqeTime(request.getParameter("reqeTime"));
//
//				if(!StringUtil.isNull(request.getParameter("aUserId"),true))
//					approveListInfo.setUserId(request.getParameter("aUserId"));
//
//				if(!StringUtil.isNull(request.getParameter("aUserName"),true))
//					approveListInfo.setUserName(request.getParameter("aUserName"));
//
//				if(!StringUtil.isNull(request.getParameter("approveType"),true))
//					approveListInfo.setApproveType(request.getParameter("approveType"));
//
//				if(!StringUtil.isNull(request.getParameter("approveReason"),true))
//					approveListInfo.setApproveReason(request.getParameter("approveReason"));
//
//				if(!StringUtil.isNull(request.getParameter("approveState"),true))
//					approveListInfo.setApproveState(request.getParameter("approveState"));
//
//				List<ApproveListInfo> approveListResult = searchListInfoService.selectApproveInfo(approveListInfo);
//				int approveListCnt = approveListResult.size();
//
//				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
//				dhtmlXGridRowCell cellInfo = null;
//				dhtmlXGridRowUserdata userdataInfo = null;
//
//				//전화번호, 이름 마스킹 처리 여부 확인
//				EtcConfigInfo etcConfigMasking = new EtcConfigInfo();
//				etcConfigMasking.setGroupKey("SEARCH");
//				etcConfigMasking.setConfigKey("MASKING_INFO");
//				List<EtcConfigInfo> maskingModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigMasking);
//				String[] maskingYn= null;
//				List<String> maskingInfo = null;
//				if(maskingModeResult.size()>0){
//					String temp = maskingModeResult.get(0).getConfigValue();
//					if (!StringUtil.isNull(temp,true) && temp.split(",").length > 0 ) {
//						maskingInfo = Arrays.asList(temp.split(","));
//					}
//				}
//
//				for (int i=0; i<approveListCnt; i++) {
//					ApproveListInfo item = approveListResult.get(i);
//					dhtmlXGridRow rowItem = new dhtmlXGridRow();
//					rowItem.setId(String.valueOf(i+1));
//					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//					rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
//
//					// 체크박스
//				/*	cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue("0");
//					rowItem.getCellElements().add(cellInfo);*/
//					//번호
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(rowItem.getId());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 요청일자
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getReqDate());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 요청 시간
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getReqTime());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 사용자 아이디
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getUserId());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 사용자 이름
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getUserName());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 소속 그룹
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getSgName());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 요청 유형
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getApproveType());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 요청 사유
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getApproveReason());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 진행 상태
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getApproveState());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 파일명
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getFileName());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취 일자
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getRecDate());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취 시간
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getRecTime());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취 내선
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getRecExt());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취자 사번
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getUserId());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취자 이름
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getUserNameCall());
//					rowItem.getCellElements().add(cellInfo);
//
//
//					// 녹취자 고객명
//					/*userdataInfo = new dhtmlXGridRowUserdata();
//					userdataInfo.setName("r_cust_name");
//					userdataInfo.setValue(item.getCustName());
//					rowItem.getUserdataElements().add(userdataInfo);*/
//
//					cellInfo = new dhtmlXGridRowCell();
//
//					boolean recCustName=false;
//					String tempStr = item.getCustName();
//
//					if(StringUtil.isNull(tempStr,true))
//						tempStr= "-";
//					else {
//						if(safeDbResult.size() > 0) {
//							if("Y".equals(safeDbResult.get(0).getConfigValue()))
//								tempStr = item.SafeDBGetter(item.getCustName());
//						}
//					}
//					cellInfo.setValue(tempStr);
//					if (maskingInfo.contains("r_cust_name")){
//						cellInfo.setValue(new RecSeeUtil().makingName(tempStr));
//					}else {
//						cellInfo.setValue(tempStr);
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취자 고객전화번호
//					String tempStrValue = new RecSeeUtil().makePhoneNumber(item.getCustPhone1());
//					/*userdataInfo = new dhtmlXGridRowUserdata();
//					userdataInfo.setName("r_cust_phone1");
//					userdataInfo.setValue(tempStrValue);
//					rowItem.getUserdataElements().add(userdataInfo);*/
//
//					cellInfo = new dhtmlXGridRowCell();
//					if (maskingInfo.contains("r_cust_phone1")){
//						cellInfo.setValue(new RecSeeUtil().maskingNumber(tempStrValue));
//					}else {
//						cellInfo.setValue(tempStrValue);
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취자 증권번호
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getStockNo());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 녹취자 콜타입
//					cellInfo = new dhtmlXGridRowCell();
//					String temp="";
//					switch (item.getCallKind1()) {
//					case "I":
//						temp="수신";
//						break;
//					case "O":
//						temp="발신";
//						break;
//					case "T":
//						temp="전환통화";
//						break;
//					case "C":
//						temp="회의통화";
//						break;
//					case "Z":
//						temp="내선통화";
//						break;
//					}
//					cellInfo.setValue(temp);
//					rowItem.getCellElements().add(cellInfo);
//
//
//
//					// 지점 신청일
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getPrereciptDate());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 지점 신청시
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getPrereciptTime());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 지점 신청자
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getPrereciptId());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 접수일
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getReciptDate());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 접수시
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getReciptTime());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 접수자
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getReciptId());
//					rowItem.getCellElements().add(cellInfo);
//
//
//					// 승인일
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getApproveDate());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 승인시
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getApproveTime());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 승인자
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getApproveId());
//					rowItem.getCellElements().add(cellInfo);
//
//					// 요청 제한일
//					cellInfo = new dhtmlXGridRowCell();
//					if (StringUtil.isNull(item.getApproveDay(),true))
//						cellInfo.setValue("");
//					else
//						cellInfo.setValue(item.getApproveDay() + " 일 동안");
//					rowItem.getCellElements().add(cellInfo);
//
//					/*//url
//					cellInfo = new dhtmlXGridRowCell();
//					//cellInfo.setValue("http://"+item.getvRecIp()+":28881/listen?url="+item.getvRecFullpath());
//					cellInfo.setValue(item.getListenUrl());
//					rowItem.getCellElements().add(cellInfo);
//*/
//					// ip
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getvRecIp());
//					rowItem.getCellElements().add(cellInfo);
//
//				/*	// fullpath
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getvRecFullpath());
//					rowItem.getCellElements().add(cellInfo);*/
//
//					// 대그룹코드
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getBgCode());
//					rowItem.getCellElements().add(cellInfo);
//
//
//					// 중그룹코드
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getMgCode());
//					rowItem.getCellElements().add(cellInfo);
//
//
//					// 소그룹코드
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue(item.getSgCode());
//					rowItem.getCellElements().add(cellInfo);
//
//
//					xmls.getRowElements().add(rowItem);
//					rowItem = null;
//
//					//
//				}
//			}
//		}else {
//			xmls = null;
//		}
//		return xmls;
//	}


	
	@RequestMapping(value="/AgentTreeView.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXTree AgentTreeView(HttpServletRequest request) {

		dhtmlXTree xmls = null;
		LoginVO userInfo = SessionManager.getUserInfo(request);
//		if(userInfo == null) {
			xmls = new dhtmlXTree();
			xmls.setId("0");

			OrganizationInfo organizationInfo = new OrganizationInfo();
			RUserInfo rUserInfo = new RUserInfo();
			if(request.getParameter("aUser")!=null&&!request.getParameter("aUser").equals("")) {
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("CALLCENTER");
				etcConfigInfo.setConfigKey("CALLCENTER");
				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

				String[] lista = etcConfigResult.get(0).getConfigValue().split(",");
				List<String> list = new ArrayList<String>();

				for(int i=0;i<lista.length;i++) {
					list.add(lista[i]);
				}

				organizationInfo.setaUser(request.getParameter("aUser"));
				organizationInfo.setList(list);
			}

			organizationInfo.setNotIvr("Y");
			organizationInfo.setType("all"); // use_yn 체크 안함
			
			String pageName = "";
			if(request.getParameter("pageName") != null && !"".equals(request.getParameter("pageName"))) {
				pageName = request.getParameter("pageName");
			}
			
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo();
			if ("userManageRec".equals(pageName)) {
				// 유저관리에서 온거면
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "userManage.userManageRec");
				
				List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
				// 허용범위 적용
				if("R".equals(nowAccessInfo.getAccessLevel().substring(0,1))){
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
					if(!"A".equals(nowAccessInfo.getAccessLevel())) {
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("bgcode", userInfo.getBgCode());
						if(!"B".equals(nowAccessInfo.getAccessLevel())) {
							item.put("mgcode", userInfo.getMgCode());
						}
						if(!"B".equals(nowAccessInfo.getAccessLevel()) && !"M".equals(nowAccessInfo.getAccessLevel())) {
							item.put("sgcode", userInfo.getSgCode());
						}
						if(!"B".equals(nowAccessInfo.getAccessLevel()) && !"M".equals(nowAccessInfo.getAccessLevel()) && !"S".equals(nowAccessInfo.getAccessLevel())) {
							item.put("user", userInfo.getUserId());
						}
						authyInfo.add(item);
					}
				}
				
				if(authyInfo != null && authyInfo.size() > 0) {
					organizationInfo.setAuthyInfo(authyInfo);
				}
			} 
			
			List<OrganizationInfo> bgList = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> mgList = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> sgList = organizationInfoService.selectOrganizationSgInfo(organizationInfo);

			xmls.setItemElements(new ArrayList<dhtmlXTreeItem>());
			dhtmlXTreeItem all = null;
			dhtmlXTreeItem treeBg = null;
			dhtmlXTreeItem treeMg = null;
			dhtmlXTreeItem treeSg = null;
			dhtmlXTreeItem treeRUser = null;
			if(request.getParameter("total")==null) {
				//전체
				all = new dhtmlXTreeItem();
				all.setId("all");
				all.setText(messageSource.getMessage("combo.ALL", null,Locale.getDefault())/*"전체"*/);
				xmls.getItemElements().add(all);
				all.setItemElements(new ArrayList<dhtmlXTreeItem>());
			}
			//대분류
			for(int i=0;i<bgList.size();i++) {
				treeBg = new dhtmlXTreeItem();
				treeBg.setId(bgList.get(i).getrBgCode());
				treeBg.setText(bgList.get(i).getrBgName());
				treeBg.setToolTip(bgList.get(i).getUseYn());
				if(request.getParameter("total")!=null)
					xmls.getItemElements().add(treeBg);
				treeBg.setItemElements(new ArrayList<dhtmlXTreeItem>());
				//중분류
				for(int j=0;j<mgList.size();j++) {
					treeMg = new dhtmlXTreeItem();
					treeMg.setItemElements(new ArrayList<dhtmlXTreeItem>());

					if(bgList.get(i).getrBgCode().equals(mgList.get(j).getrBgCode())) {
						treeMg.setId(mgList.get(j).getrMgCode());
						treeMg.setText(mgList.get(j).getrMgName());
						treeMg.setToolTip(mgList.get(j).getUseYn());
						treeBg.getItemElements().add(treeMg);
					}
					//소
					for(int k=0;k<sgList.size();k++) {
						treeSg = new dhtmlXTreeItem();
						treeSg.setItemElements(new ArrayList<dhtmlXTreeItem>());

						if(bgList.get(i).getrBgCode().equals(sgList.get(k).getrBgCode()) && mgList.get(j).getrMgCode().equals(sgList.get(k).getrMgCode()) ) {
							treeSg.setId(sgList.get(k).getrSgCode());
							treeSg.setText(sgList.get(k).getrSgName());
							treeSg.setToolTip(sgList.get(k).getUseYn());
							treeMg.getItemElements().add(treeSg);
						}
						treeSg = null;
					}
					treeMg = null;
				}
				if(request.getParameter("total")==null) {
					all.getItemElements().add(treeBg);
				}
				treeBg = null;
			}
//		}
		return  xmls;
	}
	
//	@RequestMapping(value="/authyTreeView.xml", method=RequestMethod.GET, produces = "application/xml")
//	public @ResponseBody dhtmlXTree authyTreeView(HttpServletRequest request) {
//
//		dhtmlXTree xmls = null;
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//			xmls = new dhtmlXTree();
//			xmls.setId("0");
//			
//			MMenuAccessInfo menuAccess = new MMenuAccessInfo();
//			menuAccess.setLevelCode(userInfo.getUserLevel());
////			menuAccess.setDisplayLevel(100);
//
//			List<MMenuAccessInfo> bList = menuAccessInfoService.authyTreeB(menuAccess);
//			List<MMenuAccessInfo> mList = menuAccessInfoService.authyTreeM(menuAccess);
//			int bListSize = bList.size();
//			
//			System.out.println(bListSize);
//			
//			for(int i = 0; i<bListSize; i++)
//			{
//				System.out.println(bList.get(i).getProgramName());
//				System.out.println(bList.get(i).getProgramCode());
//			}
////			List<OrganizationInfo> sgList = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
//
//			xmls.setItemElements(new ArrayList<dhtmlXTreeItem>());
////			dhtmlXTreeItem all = null;	
//			dhtmlXTreeItem treeBg = null;
//			dhtmlXTreeItem treeMg = null;
//			
////			dhtmlXTreeItem treeSg = null;
//
////			//대분류
//			for(int i=0;i<bList.size();i++) {
//				treeBg = new dhtmlXTreeItem();
//				treeBg.setId(bList.get(i).getProgramCode());
//				treeBg.setText(bList.get(i).getProgramName());
//				xmls.getItemElements().add(treeBg);
//				treeBg.setItemElements(new ArrayList<dhtmlXTreeItem>());
//				//중분류
//				for(int j=0;j<mList.size();j++) {
//					treeMg = new dhtmlXTreeItem();
//					treeMg.setItemElements(new ArrayList<dhtmlXTreeItem>());
//
//					if(bList.get(i).getProgramTop().equals(mList.get(j).getProgramTop())) {
//						treeMg.setId(mList.get(j).getProgramCode());
//						treeMg.setText(mList.get(j).getProgramName());
//						treeBg.getItemElements().add(treeMg);
//					}
////					//소
////					for(int k=0;k<sgList.size();k++) {
////						treeSg = new dhtmlXTreeItem();
////						treeSg.setItemElements(new ArrayList<dhtmlXTreeItem>());
////
////						if(bgList.get(i).getrBgCode().equals(sgList.get(k).getrBgCode()) && mgList.get(j).getrMgCode().equals(sgList.get(k).getrMgCode()) ) {
////							treeSg.setId(sgList.get(k).getrSgCode());
////							treeSg.setText(sgList.get(k).getrSgName());
////							treeSg.setToolTip(sgList.get(k).getUseYn());
////							treeMg.getItemElements().add(treeSg);
////						}
////						treeSg = null;
////					}
//					treeMg = null;
//				}
//				treeBg = null;
//			}
//		return  xmls;
//	}
	
	/**
	 * 청취할 녹취 정보
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listenPopupGrid.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml productInfoScriptGrid(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			
			// 처음 헤더 xml
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
			
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
			column.setId("checkbox");
			column.setWidth("30");
			column.setType("ch");
			column.setAlign("center");
			column.setSort("na");
			column.setValue("");
			column.setHidden("1");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("no");
			column.setWidth("92");
			column.setType("tree");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("NO.");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("title");
			column.setWidth("250");
			column.setType("ro");
			column.setAlign("left");
			column.setSort("server");
			column.setValue("제목");
			
			head.getColumnElement().add(column);
			column = null;
	
			column = new dhtmlXGridHeadColumn();
			column.setId("reRecordingYn");
			column.setWidth("60");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("재녹취 여부");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("r_rec_date");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("날짜");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("r_rec_time");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("시간");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("reRecordingReason");
			column.setWidth("*");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("재녹취 사유");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("r_ext_num");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("내선");
			column.setHidden("1");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("r_call_ttime");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("통화시간");
			column.setHidden("1");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("r_call_id1");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("그룹키");
			column.setHidden("1");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("moreProductYn");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("다계좌 여부");
			column.setHidden("1");
			
			head.getColumnElement().add(column);
			column = null;
			
			column = new dhtmlXGridHeadColumn();
			column.setId("taStateHidden");
			column.setWidth("100");
			column.setType("ro");
			column.setAlign("center");
			column.setSort("server");
			column.setValue("분석 결과");
//			column.setHidden("1");
			
			
			head.getColumnElement().add(column);
			column = null;
			
			xmls.setHeadElement(head);
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			
			SearchListInfo searchListInfo = new SearchListInfo();
			if(!StringUtil.isNull(request.getParameter("callId1"), true)) {
				searchListInfo.setCallId1(request.getParameter("callId1"));
			}
			List<SearchListInfo> info = searchListInfoService.selectSearchScriptStepHistory(searchListInfo);
			
			for(int i = 0; i< info.size(); i++) {

				boolean flag = true;
				
				
				SearchListInfo item = info.get(i);
				int key = item.gettKey();
				int depth = item.gettDepth();
				int parent = item.gettParent();
				
				if(depth > 1 && parent !=0) {
					continue;
				}
				dhtmlXGridRow row = new dhtmlXGridRow();
				row.setId(""+(i+1));
				row.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				row.setStyle("");
				row.setRowElements(new ArrayList<dhtmlXGridRow>());
				row.setXmlkids("1");
				for (int j = 1; j < info.size(); j++) {
					SearchListInfo item2 = info.get(j);
					dhtmlXGridRow row2 = new dhtmlXGridRow();
					row2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					
					int key2 = item2.gettKey();
					int depth2 = item2.gettDepth();
					int parent2 = item2.gettParent();
					row2.setId(String.valueOf(j+1));
					
					if(item2.gettDepth() == 2 && parent2 == key) {
						
						row2.setRowElements(new ArrayList<dhtmlXGridRow>());
						for (int k = 1; k < info.size(); k++) {
							SearchListInfo item3 = info.get(k);
							dhtmlXGridRow row3 = new dhtmlXGridRow();
							row3.setCellElements(new ArrayList<dhtmlXGridRowCell>());
							
							int key3 = item3.gettKey();
							int depth3 = item3.gettDepth();
							int parent3 = item3.gettParent();
							row3.setId(String.valueOf(k+1));
							if(depth3 == 3 && parent3==key2) {
								row3 = settingRow(row3,item3,cellInfo,k,request.getContextPath());
								row2.getRowElements().add(row3);
							}
						}
						if(row2.getRowElements().size() > 0 ) {
							row2 = settingRow(row2,item2,cellInfo,j,null);
						}else {
							row2 = settingRow(row2,item2,cellInfo,j,request.getContextPath());
						}
						row.getRowElements().add(row2);
					}
				}
				if(row.getRowElements().size() > 0) {
					row = settingRow(row, item, cellInfo, i,null);
				}else {
					row = settingRow(row, item, cellInfo, i,request.getContextPath());
				}
				xmls.getRowElements().add(row);
			
			} 
		}else{
			xmls=null;
		}
		return xmls;
	}
	@RequestMapping(value="/listenPopupGridNotLogin.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml productInfoScriptGridNotLogin(HttpServletRequest request, HttpServletResponse response) {
		
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
		column.setId("checkbox");
		column.setWidth("30");
		column.setType("ch");
		column.setAlign("center");
		column.setSort("na");
		column.setValue("");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("no");
		column.setWidth("92");
		column.setType("tree");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("NO.");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("title");
		column.setWidth("250");
		column.setType("ro");
		column.setAlign("left");
		column.setSort("server");
		column.setValue("제목");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("reRecordingYn");
		column.setWidth("60");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("재녹취 여부");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_rec_date");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("날짜");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_rec_time");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("시간");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("reRecordingReason");
		column.setWidth("*");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("재녹취 사유");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_ext_num");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("내선");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_call_ttime");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("통화시간");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("r_call_id1");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("그룹키");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("moreProductYn");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("다계좌 여부");
		column.setHidden("1");
		
		head.getColumnElement().add(column);
		column = null;
		
		column = new dhtmlXGridHeadColumn();
		column.setId("taStateHidden");
		column.setWidth("100");
		column.setType("ro");
		column.setAlign("center");
		column.setSort("server");
		column.setValue("분석 결과");
//			column.setHidden("1");
		
		
		head.getColumnElement().add(column);
		column = null;
		
		xmls.setHeadElement(head);
		
		xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
		dhtmlXGridRowCell cellInfo = null;
		
		
		SearchListInfo searchListInfo = new SearchListInfo();
		if(!StringUtil.isNull(request.getParameter("callId1"), true)) {
			searchListInfo.setCallId1(request.getParameter("callId1"));
		}
		List<SearchListInfo> info = searchListInfoService.selectSearchScriptStepHistory(searchListInfo);
		
		for(int i = 0; i< info.size(); i++) {
			boolean flag = true;
			
			
			SearchListInfo item = info.get(i);
			int key = item.gettKey();
			int depth = item.gettDepth();
			int parent = item.gettParent();
			
			if(depth > 1 && parent !=0) {
				continue;
			}
			dhtmlXGridRow row = new dhtmlXGridRow();
			row.setId(""+(i+1));
			row.setCellElements(new ArrayList<dhtmlXGridRowCell>());
			row.setStyle("");
			row.setRowElements(new ArrayList<dhtmlXGridRow>());
			row.setXmlkids("1");
			for (int j = 1; j < info.size(); j++) {
				SearchListInfo item2 = info.get(j);
				dhtmlXGridRow row2 = new dhtmlXGridRow();
				row2.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				int key2 = item2.gettKey();
				int depth2 = item2.gettDepth();
				int parent2 = item2.gettParent();
				row2.setId(String.valueOf(j+1));
				
				if(item2.gettDepth() == 2 && parent2 == key) {
					
					row2.setRowElements(new ArrayList<dhtmlXGridRow>());
					for (int k = 1; k < info.size(); k++) {
						SearchListInfo item3 = info.get(k);
						dhtmlXGridRow row3 = new dhtmlXGridRow();
						row3.setCellElements(new ArrayList<dhtmlXGridRowCell>());
						
						int key3 = item3.gettKey();
						int depth3 = item3.gettDepth();
						int parent3 = item3.gettParent();
						row3.setId(String.valueOf(k+1));
						if(depth3 == 3 && parent3==key2) {
							row3 = settingRow(row3,item3,cellInfo,k,request.getContextPath());
							row2.getRowElements().add(row3);
						}
					}
					if(row2.getRowElements().size() > 0 ) {
						row2 = settingRow(row2,item2,cellInfo,j,null);
					}else {
						row2 = settingRow(row2,item2,cellInfo,j,request.getContextPath());
					}
					row.getRowElements().add(row2);
				}
			}
			if(row.getRowElements().size() > 0) {
				row = settingRow(row, item, cellInfo, i,null);
			}else {
				row = settingRow(row, item, cellInfo, i,request.getContextPath());
			}
			xmls.getRowElements().add(row);
		} 
		return xmls;
	}
	
	private dhtmlXGridRow settingRow(dhtmlXGridRow row,SearchListInfo info , dhtmlXGridRowCell cellInfo , int i ,  String path) {
		
		// id
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(info.getCallKeyAp());
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		// checkbox
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue("0");
		cellInfo.setImage(null);
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		//no
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(String.valueOf(i+1));
		row.getCellElements().add(cellInfo);
		cellInfo = null;

		// 제목
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(info.getScriptStepName());
		if(info.getMoreProductYn() == 0) 
			cellInfo.setStyle("color:red; text-align:center; font-weight:bold");
		
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		// 재녹취 여부
		cellInfo = new dhtmlXGridRowCell();
		String callKeyAp = info.getCallKeyAp();
		if(callKeyAp == null) {
			cellInfo.setValue(null);
		}else {
			cellInfo.setValue(info.getRetryCount());
			
		}
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		// null체크
		// 날짜
		cellInfo = new dhtmlXGridRowCell();
		String tempStr = info.getRecDate();
		if (tempStr != null && !"".equals(tempStr) && tempStr.length() == 8) {
			tempStr = tempStr.substring(0,4) + "-" + tempStr.substring(4,6) + "-" + tempStr.substring(6,8);
		}
		cellInfo.setValue(tempStr);
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		//null 체크
		// 시간
		cellInfo = new dhtmlXGridRowCell();
		tempStr = info.getRecTime();
		if (tempStr != null && !"".equals(tempStr) && tempStr.length() == 6) {
			tempStr = tempStr.substring(0,2) + ":" + tempStr.substring(2,4) + ":" + tempStr.substring(4,6);
		}
		cellInfo.setValue(tempStr);
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		// 재녹취 사유
		cellInfo = new dhtmlXGridRowCell();
		tempStr = info.getRetryReasonDetail();
		if (tempStr == null || "".equals(tempStr)) {
			tempStr = "-";
		}
		cellInfo.setValue(info.getRetryReasonDetail());
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		//null체크
		// 내선
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(info.getExtNum());
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		//null체크
		// 통화시간
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(info.getCallTtime());
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		// 그룹키
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(info.getCallId1());
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		//다계좌 여부
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(String.valueOf(info.getMoreProductYn()));
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		
		//TA결과 여부
		cellInfo = new dhtmlXGridRowCell();
		if(path == null) {
			cellInfo.setValue("");
		}else {
			cellInfo.setValue(caseTaResult(String.valueOf(info.getTaState()),path));
		}
		row.getCellElements().add(cellInfo);
		cellInfo = null;
		return row;
	}
	
	private String caseTaResult(String taResult , String path) {
		String color = "Gray";
		String img = "";
		
		switch (taResult) {
		case "Y":
			color = "Blue";
			img = "<div id='' onClick='' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
			+ path
			+ "/resources/common/recsee/images/project/icon/Circle"+color+".png' style='width:16px;' /></div>";
			break;
		case "N":
			color = "Yellow";
			img = "<div id='' onClick='' style=\"cursor: pointer; position: relative; top: 2px\"><img id src='"
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