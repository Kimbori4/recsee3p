package com.furence.recsee.transcript.controller;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.admin.service.QueueInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.CommonCodeService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.main.model.dhtmlXTree;
import com.furence.recsee.main.model.dhtmlXTreeItem;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.transcript.model.SttDataset;
import com.furence.recsee.transcript.model.SttEnginState;
import com.furence.recsee.transcript.model.SttModel;
import com.furence.recsee.transcript.model.SttResult;
import com.furence.recsee.transcript.model.TranscriptListInfo;
import com.furence.recsee.transcript.model.TranscriptMap;
import com.furence.recsee.transcript.service.TranscriptService;
@Controller
public class XmlTranscriptController {
	private static final Logger logger = LoggerFactory.getLogger(XmlTranscriptController.class);
	@Autowired
	private TranscriptService transcriptService;
	@Autowired
	private SearchListInfoService searchListInfoService;
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
	private QueueInfoService queueInfoService;
	@Autowired
	private CommonCodeService commonCodeService;
	// 조회 목록
	@RequestMapping(value="/search_transcript_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml search_transcript_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			//SAFE DB 확인
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);
			OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			for(int j = 0; j < 17; j++){
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
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_rec_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.label.recDate", null,Locale.getDefault())+"</div>");
					break;
				// 녹취 시간
				case 2:
					column.setWidth("80");
					column.setCache("0");
					column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.rectime", null,Locale.getDefault())+"</div>");
					break;
				// 대분류
				case 3:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;
				// 중분류
				case 4:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;
				// 소분류
				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;
				// 상담사ID
				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_USER_ID", null,Locale.getDefault())+"</div>");
					break;
				// 상담사명
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_user_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_USER_NAME", null,Locale.getDefault())+"</div>");
					break;
				// 내선번호
				case 8:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_ext_num");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.user.title.ext", null,Locale.getDefault())+"</div>");
					break;
				// 시스템 코드
				case 9:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_sys_code");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_V_SYS_CODE", null,Locale.getDefault())+"</div>");
					break;
				// 고객명
				case 10:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_cust_name");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("search.option.custName", null,Locale.getDefault())+"</div>");
					break;
				// 고객 번호
				case 11:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_cust_phone1");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("search.option.custNo", null,Locale.getDefault())+"</div>");
					break;
				// 전사자
				case 12:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_user_id");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.title.transcriber", null,Locale.getDefault())+"</div>");
					break;
				// 진행 상태
				case 13:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_transcript_status");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.approve.label.status", null,Locale.getDefault())+"</div>");
					break;
				// 검색용 상태 숨김처리
				case 14:
					column.setWidth("100");
					column.setCache("0");
					column.setId("hiddenStatus");
					column.setHidden("1");
					column.setValue("");
					break;
				// 전사 버튼
				case 15:
					column.setWidth("50");
					column.setCache("0");
					column.setId("r_transcript_btn");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.title.transcript", null,Locale.getDefault())+"</div>");
					break;
				// 시리얼
				case 16:
					column.setWidth("*");
					column.setCache("0");
					column.setId("r_transcript_serial");
					column.setHidden("1");
					column.setValue("");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			// 전사 관리 - 파일 리스트 
			TranscriptListInfo transcriptListInfo = new TranscriptListInfo();
			if(!StringUtil.isNull(request.getParameter("sDate"),true)) {
				transcriptListInfo.setsDate(request.getParameter("sDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eDate"),true)) {
				transcriptListInfo.seteDate(request.getParameter("eDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("sTime"),true)) {
				transcriptListInfo.setsTime(request.getParameter("sTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eTime"),true)) {
				transcriptListInfo.seteTime(request.getParameter("eTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("userId"),true)) {
				transcriptListInfo.setUserId(request.getParameter("userId"));
			}
			if(!StringUtil.isNull(request.getParameter("extNum"),true)) {
				transcriptListInfo.setExtNum(request.getParameter("extNum"));
			}
			if(!StringUtil.isNull(request.getParameter("userName"),true)) {
				transcriptListInfo.setUserName(request.getParameter("userName"));
			}
			if(!StringUtil.isNull(request.getParameter("sgCode"),true)) {
				transcriptListInfo.setSgCode(request.getParameter("sgCode"));
			}
			if(!StringUtil.isNull(request.getParameter("mgCode"),true)) {
				transcriptListInfo.setMgCode(request.getParameter("mgCode"));
			}
			if(!StringUtil.isNull(request.getParameter("bgCode"),true)) {
				transcriptListInfo.setBgCode(request.getParameter("bgCode"));
			}
			if(!StringUtil.isNull(request.getParameter("transcriber"),true)) {
				transcriptListInfo.setTranscriber(request.getParameter("transcriber"));
			}
			if(!StringUtil.isNull(request.getParameter("transcript"),true)) {
				transcriptListInfo.setTranscript(request.getParameter("transcript"));
			}
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10064");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			if(!"admin".equals(userInfo.getUserId()) && accessResult.size() > 0){
				if(accessResult.get(0).getAccessLevel().equals("B")) {
					transcriptListInfo.setBgCode(userInfo.getBgCode());
				}
				else if(accessResult.get(0).getAccessLevel().equals("M")) {
					transcriptListInfo.setMgCode(userInfo.getMgCode());
				}
				else if(accessResult.get(0).getAccessLevel().equals("S") && !accessResult.get(0).getAccessLevel().equals("M")) {
					transcriptListInfo.setSgCode(userInfo.getSgCode());
				}
				else if(accessResult.get(0).getAccessLevel().equals("U")){
					transcriptListInfo.setTranscriber(userInfo.getUserId());
				}
			}
			List<TranscriptListInfo> listResult = transcriptService.selectTranscriptList(transcriptListInfo);
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			// prefix 제거 옵션 사용 여부
			EtcConfigInfo etcConfigPrefixYN = new EtcConfigInfo();
			etcConfigPrefixYN.setGroupKey("Prefix");
			etcConfigPrefixYN.setConfigKey("PrefixYN");
			EtcConfigInfo PrefixYN = etcConfigInfoService.selectOptionYN(etcConfigPrefixYN);
			String PrefixYNVal = PrefixYN.getConfigValue();
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
			String maskingYNVal = maskingYN.getConfigValue();
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
			String hyphenYNVal = hyphenYN.getConfigValue();
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
			String transcriptBtnClass;
			for(int i = 0; i < listResult.size()  ; i++) {
				TranscriptListInfo list = listResult.get(i);
				String tempStrValue = "";
				transcriptBtnClass = "icon_btn_transcript_white";
				
				if("Y".equals(list.getrTranscriptYn())) {								
					transcriptBtnClass = "icon_btn_transcript_gray";
				}
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getrTranscriptSerial());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				// 췤췤 체크박스
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				//	녹취 날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecDate());
				rowItem.getCellElements().add(cellInfo);
				//	녹취 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecTime());
				rowItem.getCellElements().add(cellInfo);
				//	대분류
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrBgCode();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else
					tempStrValue = new FindOrganizationUtil().getOrganizationName(list.getrBgCode(), organizationBgInfo);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				//	중분류
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrMgCode();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else
					tempStrValue = new FindOrganizationUtil().getOrganizationName(list.getrBgCode(), list.getrMgCode(), organizationMgInfo);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				//	소분류
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrSgCode();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else
					tempStrValue = new FindOrganizationUtil().getOrganizationName(list.getrBgCode(), list.getrMgCode(), list.getrSgCode(), organizationSgInfo);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				//	사번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecId());
				rowItem.getCellElements().add(cellInfo);
				//	상담사명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecName());
				rowItem.getCellElements().add(cellInfo);
				//	내선번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrExtNum());
				rowItem.getCellElements().add(cellInfo);
				//	시스템 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrSysCode());
				rowItem.getCellElements().add(cellInfo);
				//	고객이름
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrCustName();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else {
					if(safeDbResult.size() > 0) {
						if("Y".equals(safeDbResult.get(0).getConfigValue())) {
							SearchListInfo searchList = new SearchListInfo();
							tempStrValue = searchList.SafeDBGetter(list.getrCustName());
						}
					}
				}
				tempStrValue=new RecSeeUtil().makingName(tempStrValue);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				// 고객번호
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrCustPhone();
				if (PrefixYNVal.equals("Y")) {
					tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
				}
				if (maskingYNVal.equals("Y")) {
					tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
				}
				if (hyphenYNVal.equals("Y")) {
					tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
				}
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				// 전사자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrUserId());
				rowItem.getCellElements().add(cellInfo);
				// 진행 상황
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrTranscriptStatus();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "-";
				cellInfo.setValue(messageSource.getMessage("views.search.title."+tempStrValue.trim(), null,Locale.getDefault()));
				rowItem.getCellElements().add(cellInfo);
				// 진행 상황 (hidden) -> 학습 요청시 전사안된 파일 있으면 return false 하기위한 검사용 컬럼
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrTranscriptStatus();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "-";
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				// 전사 버튼
				tempStrValue = "<button class='on_evaluation_open ui_btn_white ui_sub_btn_flat "+transcriptBtnClass+"' onClick='openTranscriptPop(\""+list.getListenUrl()+"\",\""+userInfo.getUserId()+"\",\""+list.getrRecId()+"\",\""+list.getrRecDate()+"\",\""+list.getrRecTime()+"\",\""+list.getrExtNum()+"\",\""+list.getrTranscriptYn()+"\",\""+list.getrTranscriptSerial()+"\",\""+list.getrLocalFile()+"\")'></button>";
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);				
				// 시리얼 넘버 -> idx
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrTranscriptSerial());
				rowItem.getCellElements().add(cellInfo);				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
	
	// 데이터셋 조회 목록
	@RequestMapping(value="/search_sttDataset_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml search_transcriptModel_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			/*//SAFE DB 확인
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);*/
			/*OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizationInfoService.selectOrganizationSgInfo(organizationInfo);*/
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
				
				// 데이터셋명
				case 0:
					column.setWidth("200");
					column.setCache("0");
					column.setId("r_dataset_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttDataset.label.rDatasetName", null,Locale.getDefault())+"</div>");
					break;

				// 데이터셋 생성일자
				case 1:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_dataset_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttDataset.label.rDatasetDate", null,Locale.getDefault())+"</div>");
					break;
					
				// 데이터셋 생성시간
				case 2:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_dataset_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttDataset.label.rDatasetTime", null,Locale.getDefault())+"</div>");
					break;
					
				// 데이터 cnt
				case 3:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_dataset_size");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttDataset.label.rDatasetSize", null,Locale.getDefault())+"</div>");
					break;
				// 학습버튼
				case 4:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_learn_btn");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttDataset.button.learn", null,Locale.getDefault())+"</div>");
					break;
					
				// 삭제버튼 
				case 5:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_delete_btn");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttDataset.button.delete", null,Locale.getDefault())+"</div>");
					break;
					
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			// 데이터셋 관리 
			SttDataset sttDataset = new SttDataset();
			
			if(!StringUtil.isNull(request.getParameter("sDate"),true)) {
				sttDataset.setsDate(request.getParameter("sDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eDate"),true)) {
				sttDataset.seteDate(request.getParameter("eDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("sTime"),true)) {
				sttDataset.setsTime(request.getParameter("sTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eTime"),true)) {
				sttDataset.seteTime(request.getParameter("eTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("rDatasetName"),true)) {
				sttDataset.setrDatasetName(request.getParameter("rDatasetName"));
			}
			
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			List<SttDataset> sttDatasetResult = transcriptService.selectSttDataset(sttDataset);
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			for(int i = 0; i < sttDatasetResult.size()  ; i++) {
				SttDataset list = sttDatasetResult.get(i);
				String tempStrValue = "";
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getrDatasetName());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				// 데이터셋명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrDatasetName());
				rowItem.getCellElements().add(cellInfo);
				// 데이터셋 생성일자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrDatasetDate());
				rowItem.getCellElements().add(cellInfo);
				// 데이터셋 생성시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrDatasetTime());
				rowItem.getCellElements().add(cellInfo);
				//	데이터 개수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrDatasetSize());
				rowItem.getCellElements().add(cellInfo);

				// 학습 버튼
				tempStrValue = "<button class='on_evaluation_open ui_btn_white ui_sub_btn_flat' onClick='onLearnPop(\""+list.getrDatasetName()+"\")'>"+messageSource.getMessage("views.sttDataset.button.learn", null,Locale.getDefault())+"</button>";
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				// 삭제 버튼
				tempStrValue = "<button class='ui_btn_white icon_btn_trash_gray' onClick='onDelDataset(\""+list.getrDatasetName()+"\")'></button>";
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
	
	// stt모델 조회 목록
	@SuppressWarnings("null")
	@RequestMapping(value="/search_sttModel_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml search_sttModel_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			/*//SAFE DB 확인
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);*/
			/*OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizationInfoService.selectOrganizationSgInfo(organizationInfo);*/
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			for(int j = 0; j < 18; j++){
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
					column.setHidden("1");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
				// 학습요청날짜
				case 1:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_create_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rCteateDate", null,Locale.getDefault())+"</div>");
					break;
				// 학습요청시간
				case 2:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_create_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rCteateTime", null,Locale.getDefault())+"</div>");
					break;
					
				// 학습시작날짜
				case 3:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_start_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rStartDate", null,Locale.getDefault())+"</div>");
					break;
					
				// 학습시작시간
				case 4:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_start_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rStartTime", null,Locale.getDefault())+"</div>");
					break;

				// 학습종료날짜
				case 5:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_end_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rEndDate", null,Locale.getDefault())+"</div>");
					break;

				// 학습종료시간
				case 6:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_end_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rEndTime", null,Locale.getDefault())+"</div>");
					break;
					
				// 모델 타입
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_type");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rType", null,Locale.getDefault())+"</div>");
					break;
				// 모델명
				case 8:
					column.setWidth("200");
					column.setCache("0");
					column.setId("r_model_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rModelName", null,Locale.getDefault())+"</div>");
					break;
				// 베이스 모델
				case 9:
					column.setWidth("200");
					column.setCache("0");
					column.setId("r_base_model");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rBaseModel", null,Locale.getDefault())+"</div>");
					break;
				// 데이터 개수
				case 10:
					column.setWidth("90");
					column.setCache("0");
					column.setId("r_data_total_count");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rDataTotalCount", null,Locale.getDefault())+"</div>");
					break;
				// 데이터 총 시간
				case 11:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_data_total_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rDataTotalTime", null,Locale.getDefault())+"</div>");
					break;
				// 학습 상태
				case 12:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_work_status");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rWorkStatus", null,Locale.getDefault())+"</div>");
					break;
				// 학습상태 메시지
				case 13:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_work_status_msg");
					column.setHidden("1");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rWorkStatusMsg", null,Locale.getDefault())+"</div>");
					break;
				// 인식률
				case 14:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_recog_rate");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rRecogRate", null,Locale.getDefault())+"</div>");
					break;
				// 인식률 메시지
				case 15:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_recog_rate_msg");
					column.setHidden("1");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rRecogRateMsg", null,Locale.getDefault())+"</div>");
					break;
				// 데이터셋 이름
				case 16:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_dataset_name");
					column.setHidden("1");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rDatasetName", null,Locale.getDefault())+"</div>");
					break;
				// 모델 적용 버튼
				case 17:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_apply");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttModel.label.rApply", null,Locale.getDefault())+"</div>");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			// stt모델 리스트 VO(DTO) 객체 생성 
			SttModel sttModel = new SttModel();
			
			if(!StringUtil.isNull(request.getParameter("sDate"),true)) {
				sttModel.setsDate(request.getParameter("sDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eDate"),true)) {
				sttModel.seteDate(request.getParameter("eDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("sTime"),true)) {
				sttModel.setsTime(request.getParameter("sTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eTime"),true)) {
				sttModel.seteTime(request.getParameter("eTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("rModelName"),true)) {
				sttModel.setrModelName(request.getParameter("rModelName"));
			}
			if(!StringUtil.isNull(request.getParameter("rType"),true)) {
				sttModel.setrType(request.getParameter("rType"));
			}
			if(!StringUtil.isNull(request.getParameter("rWorkStatus"),true)) {
				sttModel.setrWorkStatus(request.getParameter("rWorkStatus"));
			}
			
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			List<SttModel> sttModelResult = transcriptService.selectSttModel(sttModel);
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			for(int i = 0; i < sttModelResult.size()  ; i++) {
				SttModel list = sttModelResult.get(i);
				String tempStrValue = "";
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getrModelName()+","+list.getrType());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				// 췤췤 체크박스
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				// 학습요청날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrCreateDate());
				rowItem.getCellElements().add(cellInfo);
				// 학습요청시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrCreateTime());
				rowItem.getCellElements().add(cellInfo);
				// 학습시작날짜
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrStartDate()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrStartDate());
				}
				rowItem.getCellElements().add(cellInfo);
				// 학습시작시간
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrStartTime()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrStartTime());
				}
				rowItem.getCellElements().add(cellInfo);
				// 학습종료날짜
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrEndDate()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrEndDate());
				}
				rowItem.getCellElements().add(cellInfo);
				// 학습종료시간
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrEndTime()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrEndTime());
				}
				rowItem.getCellElements().add(cellInfo);
				// 모델 타입
				cellInfo = new dhtmlXGridRowCell();
				if("am".equals(list.getrType())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.am", null,Locale.getDefault()));
				}else if("lm".equals(list.getrType())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.lm", null,Locale.getDefault()));
				}else if("recog".equals(list.getrType())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.recog", null,Locale.getDefault()));
				}else {
					cellInfo.setValue("-");
				}
				rowItem.getCellElements().add(cellInfo);
				// 모델명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrModelName());
				rowItem.getCellElements().add(cellInfo);
				// 베이스모델
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrBaseModel()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrBaseModel());
				}
				rowItem.getCellElements().add(cellInfo);
				// 학습 데이터 개수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrDataTotalCount());
				rowItem.getCellElements().add(cellInfo);
				// 학습 데이터 총 시간
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrDataTotalTime()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrDataTotalTime());
				}
				rowItem.getCellElements().add(cellInfo);
				// 작업상태
				cellInfo = new dhtmlXGridRowCell();
				
				if("f".equals(list.getrWorkStatus())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.f", null,Locale.getDefault()));
				}else if("w".equals(list.getrWorkStatus())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.w", null,Locale.getDefault()));
				}else if("e".equals(list.getrWorkStatus())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.e", null,Locale.getDefault()));
				}else if("r".equals(list.getrWorkStatus())) {
					cellInfo.setValue(messageSource.getMessage("views.sttModel.label.r", null,Locale.getDefault()));
				}else {
					cellInfo.setValue("-");
				}
				rowItem.getCellElements().add(cellInfo);
				// 작업상태 메시지
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrWorkStatusMsg());
				rowItem.getCellElements().add(cellInfo);
				// 인식률
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrRecogRate()==null) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrRecogRate());
				}
				rowItem.getCellElements().add(cellInfo);
				// 인식률 메시지
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecogRateMsg());
				rowItem.getCellElements().add(cellInfo);
				// 데이터셋 이름
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrDatasetName());
				rowItem.getCellElements().add(cellInfo);
				xmls.getRowElements().add(rowItem);
				// 모델적용 버튼
				if("recog".equals(list.getrType())) {
					tempStrValue = "";
				}else if("f".equals(list.getrWorkStatus())) {
					if("Y".equals(list.getrApplyYn())) {
						tempStrValue = messageSource.getMessage("views.sttModel.label.rApplyY", null,Locale.getDefault());
					}else {
						tempStrValue = "<button class='ui_btn_white' onClick='onApply(\""+list.getrModelName()+"\", \""+list.getrType()+"\")'>"+messageSource.getMessage("views.sttModel.label.rApplyN", null,Locale.getDefault())+"</button>";
					}
				}else {
					tempStrValue = "";
				}
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				rowItem = null;
			}
		}
		return xmls;
	}
	
	@RequestMapping(value="/SearchAgentTreeView.xml", method=RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXTree SearchAgentTreeView(HttpServletRequest request) {

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
			List<OrganizationInfo> bgList = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> mgList = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> sgList = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
			List<RUserInfo> rUserList = ruserInfoService.selectTreeViewRUserInfo(rUserInfo);

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
				if(request.getParameter("total")!=null)
					xmls.getItemElements().add(treeBg);
				treeBg.setItemElements(new ArrayList<dhtmlXTreeItem>());
				
				// 유저정보
				for(int l=0;l<rUserList.size();l++) {
					treeRUser = new dhtmlXTreeItem();
					treeRUser.setItemElements(new ArrayList<dhtmlXTreeItem>());

					if(bgList.get(i).getrBgCode().equals(rUserList.get(l).getBgCode()) && 
							(rUserList.get(l).getMgCode() == null || "".equals(rUserList.get(l).getMgCode())) && 
									(rUserList.get(l).getSgCode() == null || "".equals(rUserList.get(l).getSgCode()))) {
						treeRUser.setId("user,"+rUserList.get(l).getUserId());
						treeRUser.setText(rUserList.get(l).getUserName());
						treeBg.getItemElements().add(treeRUser);
					}
					treeRUser = null;
				}
				
				
				//중분류
				for(int j=0;j<mgList.size();j++) {
					treeMg = new dhtmlXTreeItem();
					treeMg.setItemElements(new ArrayList<dhtmlXTreeItem>());

					if(bgList.get(i).getrBgCode().equals(mgList.get(j).getrBgCode())) {
						treeMg.setId(mgList.get(j).getrMgCode());
						treeMg.setText(mgList.get(j).getrMgName());
						treeBg.getItemElements().add(treeMg);
					}
					

					// 유저정보
					for(int l=0;l<rUserList.size();l++) {
						treeRUser = new dhtmlXTreeItem();
						treeRUser.setItemElements(new ArrayList<dhtmlXTreeItem>());

						if(bgList.get(i).getrBgCode().equals(rUserList.get(l).getBgCode()) && 
							mgList.get(j).getrMgCode().equals(rUserList.get(l).getMgCode()) && 
							(rUserList.get(l).getSgCode() == null || "".equals(rUserList.get(l).getSgCode()))) {
							treeRUser.setId("user,"+rUserList.get(l).getUserId());
							treeRUser.setText(rUserList.get(l).getUserName());
							treeMg.getItemElements().add(treeRUser);
						}
						treeRUser = null;
					}
					
					//소
					for(int k=0;k<sgList.size();k++) {
						treeSg = new dhtmlXTreeItem();
						treeSg.setItemElements(new ArrayList<dhtmlXTreeItem>());

						if(bgList.get(i).getrBgCode().equals(sgList.get(k).getrBgCode()) && mgList.get(j).getrMgCode().equals(sgList.get(k).getrMgCode()) ) {
							treeSg.setId(sgList.get(k).getrSgCode());
							treeSg.setText(sgList.get(k).getrSgName());
							treeMg.getItemElements().add(treeSg);
						}
						// 유저정보
						for(int l=0;l<rUserList.size();l++) {
							treeRUser = new dhtmlXTreeItem();
							treeRUser.setItemElements(new ArrayList<dhtmlXTreeItem>());

							if(bgList.get(i).getrBgCode().equals(rUserList.get(l).getBgCode()) && 
								mgList.get(j).getrMgCode().equals(rUserList.get(l).getMgCode()) && 
								sgList.get(k).getrSgCode().equals(rUserList.get(l).getSgCode()) ) {
								treeRUser.setId("user,"+rUserList.get(l).getUserId());
								treeRUser.setText(rUserList.get(l).getUserName());
								treeSg.getItemElements().add(treeRUser);
							}
							treeRUser = null;
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
	
	@ResponseBody
	@RequestMapping("transciprtListPop")
	public dhtmlXGridXml transciprtListPop(HttpServletRequest request) {
		TranscriptMap transcriptMap = new TranscriptMap();
		transcriptMap.setrDatasetName(request.getParameter("rDatasetName"));
		List<TranscriptMap> transcriptMapResult  = transcriptService.selectTranscriptMap(transcriptMap);
		
		List<String> rTranscriptSerial = new ArrayList();
		for (int i = 0; i < transcriptMapResult.size(); i++) {
			rTranscriptSerial.add(transcriptMapResult.get(i).getrTranscriptSerial());
		}
		
		TranscriptListInfo transcriptListInfo = new TranscriptListInfo();
		transcriptListInfo.setrTranscriptSerialList(rTranscriptSerial);
		
		List<TranscriptListInfo> listResult = transcriptService.selectTranscriptListPop(transcriptListInfo);
		
		for (int i = 0; i < listResult.size(); i++) {
			logger.info(listResult.get(i).getrTranscriptSerial());
		}
		
		
		/*LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			//SAFE DB 확인
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);
			OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			for(int j = 0; j < 13; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");
				switch (j) {
				// 녹취 일자
				case 0:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_rec_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.label.recDate", null,Locale.getDefault())+"</div>");
					break;
				// 녹취 시간
				case 1:
					column.setWidth("80");
					column.setCache("0");
					column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.rectime", null,Locale.getDefault())+"</div>");
					break;
				// 대분류
				case 2:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;
				// 중분류
				case 3:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;
				// 소분류
				case 4:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;
				// 상담사ID
				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_USER_ID", null,Locale.getDefault())+"</div>");
					break;
				// 상담사명
				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_user_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_USER_NAME", null,Locale.getDefault())+"</div>");
					break;
				// 내선번호
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_ext_num");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.user.title.ext", null,Locale.getDefault())+"</div>");
					break;
				// 고객명
				case 8:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_cust_name");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("search.option.custName", null,Locale.getDefault())+"</div>");
					break;
				// 고객 번호
				case 9:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_cust_phone1");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("search.option.custNo", null,Locale.getDefault())+"</div>");
					break;
				// 전사자
				case 10:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_user_id");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.title.transcriber", null,Locale.getDefault())+"</div>");
					break;
				// 전사 버튼
				case 11:
					column.setWidth("50");
					column.setCache("0");
					column.setId("r_transcript_btn");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.title.transcript", null,Locale.getDefault())+"</div>");
					break;
				// 시리얼
				case 12:
					column.setWidth("*");
					column.setCache("0");
					column.setId("r_transcript_serial");
					column.setHidden("1");
					column.setValue("");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			// 전사 관리 - 파일 리스트 
			TranscriptListInfo transcriptListInfo = new TranscriptListInfo();
			if(!StringUtil.isNull(request.getParameter("rTranscriptSerial"),true)) {
				transcriptListInfo.setTranscript(request.getParameter("rTranscriptSerial"));
			}
			List<TranscriptListInfo> listResult = transcriptService.selectTranscriptListPop(transcriptListInfo);
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			// prefix 제거 옵션 사용 여부
			EtcConfigInfo etcConfigPrefixYN = new EtcConfigInfo();
			etcConfigPrefixYN.setGroupKey("Prefix");
			etcConfigPrefixYN.setConfigKey("PrefixYN");
			EtcConfigInfo PrefixYN = etcConfigInfoService.selectOptionYN(etcConfigPrefixYN);
			String PrefixYNVal = PrefixYN.getConfigValue();
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
			String maskingYNVal = maskingYN.getConfigValue();
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
			String hyphenYNVal = hyphenYN.getConfigValue();
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
			String transcriptBtnClass;
			for(int i = 0; i < listResult.size()  ; i++) {
				TranscriptListInfo list = listResult.get(i);
				String tempStrValue = "";
				if("Y".equals(list.getrTranscriptYn())) {								
					transcriptBtnClass = "icon_btn_transcript_gray";
				} else {
					transcriptBtnClass = "icon_btn_transcript_white";
				}
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getrTranscriptSerial());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				//	녹취 날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecDate());
				rowItem.getCellElements().add(cellInfo);
				//	녹취 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecTime());
				rowItem.getCellElements().add(cellInfo);
				//	대분류
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrBgCode();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else
					tempStrValue = new FindOrganizationUtil().getOrganizationName(list.getrBgCode(), organizationBgInfo);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				//	중분류
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrMgCode();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else
					tempStrValue = new FindOrganizationUtil().getOrganizationName(list.getrBgCode(), list.getrMgCode(), organizationMgInfo);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				//	소분류
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrSgCode();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else
					tempStrValue = new FindOrganizationUtil().getOrganizationName(list.getrBgCode(), list.getrMgCode(), list.getrSgCode(), organizationSgInfo);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				//	사번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecId());
				rowItem.getCellElements().add(cellInfo);
				//	상담사명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecName());
				rowItem.getCellElements().add(cellInfo);
				//	내선번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrExtNum());
				rowItem.getCellElements().add(cellInfo);
				//	고객이름
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrCustName();
				if(StringUtil.isNull(tempStrValue,true))
					tempStrValue = "";
				else {
					if(safeDbResult.size() > 0) {
						if("Y".equals(safeDbResult.get(0).getConfigValue())) {
							SearchListInfo searchList = new SearchListInfo();
							tempStrValue = searchList.SafeDBGetter(list.getrCustName());
						}
					}
				}
				tempStrValue=new RecSeeUtil().makingName(tempStrValue);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				// 고객번호
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrCustPhone();
				if (PrefixYNVal.equals("Y")) {
					tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
				}
				if (maskingYNVal.equals("Y")) {
					tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
				}
				if (hyphenYNVal.equals("Y")) {
					tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
				}
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);
				// 전사자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrUserId());
				rowItem.getCellElements().add(cellInfo);
				// 전사 버튼
				tempStrValue = "<button class='on_evaluation_open ui_btn_white ui_sub_btn_flat "+transcriptBtnClass+"' onClick='openTranscriptPop(\""+list.getListenUrl()+"\",\""+userInfo.getUserId()+"\",\""+list.getrRecId()+"\",\""+list.getrRecDate()+"\",\""+list.getrRecTime()+"\",\""+list.getrExtNum()+"\",\""+list.getrTranscriptYn()+"\",\""+list.getrTranscriptSerial()+"\")'></button>";
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);			
				// 시리얼 넘버 -> idx
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrTranscriptSerial());
				rowItem.getCellElements().add(cellInfo);				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}*/
		/*return xmls;*/
		return null;
	}
	
	// stt적용모델 그리드
	@SuppressWarnings("null")
	@RequestMapping(value="/search_sttEnginState_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml search_sttEnginState_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			for(int j = 0; j < 11; j++){
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
					column.setHidden("1");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
				// 시스템코드
				case 1:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_sys_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rSysCode", null,Locale.getDefault())+"</div>");
					break;
				// 시스템명
				case 2:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_sys_ip");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rSysName", null,Locale.getDefault())+"</div>");
					break;
				// 시스템IP
				case 3:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_sys_code");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rSysIp", null,Locale.getDefault())+"</div>");
					break;
				// 모델적용 요청일자
				case 4:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_update_req_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rUpdateReqDate", null,Locale.getDefault())+"</div>");
					break;
					
				// 모델적용 요청시간
				case 5:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_update_req_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rUpdateReqTime", null,Locale.getDefault())+"</div>");
					break;
					
				// 적용날짜
				case 6:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_update_proc_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rUpdateProcDate", null,Locale.getDefault())+"</div>");
					break;

				// 적용시간
				case 7:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_update_proc_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rUpdateProcTime", null,Locale.getDefault())+"</div>");
					break;
					
				// 모델명
				case 8:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_model_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rModelName", null,Locale.getDefault())+"</div>");
					break;

				// 모델타입
				case 9:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_type");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rType", null,Locale.getDefault())+"</div>");
					break;
					
				// 상태
				case 10:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_work_state");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttEnginState.label.rWorkState", null,Locale.getDefault())+"</div>");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);
			
			// stt 적용모델 리스트 VO(DTO) 객체 생성 
			SttEnginState sttEnginState = new SttEnginState();
			
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			List<SttEnginState> sttEnginStateResult = transcriptService.selectSttEnginState(sttEnginState);
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			for(int i = 0; i < sttEnginStateResult.size() ; i++) {
				SttEnginState list = sttEnginStateResult.get(i);
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getrSysCode()+","+list.getrType());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				// 췤췤 체크박스
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				// 시스템코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrSysCode());
				rowItem.getCellElements().add(cellInfo);
				// 시스템명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrSysName());
				rowItem.getCellElements().add(cellInfo);
				// IP
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrSysIp());
				rowItem.getCellElements().add(cellInfo);
				// 적용요청 날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrUpdateReqTime().substring(0, 8));
				rowItem.getCellElements().add(cellInfo);
				// 적용요청 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrUpdateReqTime().substring(8, list.getrUpdateReqTime().length()));
				rowItem.getCellElements().add(cellInfo);
				// 적용날짜
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrUpdateProcTime()==null || "".equals(list.getrUpdateProcTime())) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrUpdateProcTime().substring(0, 8));
				}
				rowItem.getCellElements().add(cellInfo);
				// 적용시간
				cellInfo = new dhtmlXGridRowCell();
				if(list.getrUpdateProcTime()==null || "".equals(list.getrUpdateProcTime())) {
					cellInfo.setValue("-");
				}else {
					cellInfo.setValue(list.getrUpdateProcTime().substring(8, list.getrUpdateProcTime().length()));
				}
				rowItem.getCellElements().add(cellInfo);
				// 모델명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrModelName());
				rowItem.getCellElements().add(cellInfo);
				// 모델타입
				cellInfo = new dhtmlXGridRowCell();
				if("am".equals(list.getrType())) {
					cellInfo.setValue(messageSource.getMessage("views.sttEnginState.label.am", null,Locale.getDefault()));
				}else if("lm".equals(list.getrType())) {
					cellInfo.setValue(messageSource.getMessage("views.sttEnginState.label.lm", null,Locale.getDefault()));
				}
				rowItem.getCellElements().add(cellInfo);
				// 상태
				cellInfo = new dhtmlXGridRowCell();
				if("r".equals(list.getrWorkState())) {
					cellInfo.setValue(messageSource.getMessage("views.sttEnginState.label.r", null,Locale.getDefault()));
				}else if("u".equals(list.getrWorkState())) {
					cellInfo.setValue(messageSource.getMessage("views.sttEnginState.label.u", null,Locale.getDefault()));
				}else if("e".equals(list.getrWorkState())) {
					cellInfo.setValue(messageSource.getMessage("views.sttEnginState.label.e", null,Locale.getDefault()));
				}
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
	
	
	// 데이터셋 조회 목록
	@RequestMapping(value="/search_sttResult_list.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml search_sttResult_list(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			/*//SAFE DB 확인
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);*/
			/*OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizationInfoService.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizationInfoService.selectOrganizationSgInfo(organizationInfo);*/
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			for(int j = 0; j < 10; j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
				column.setHidden("0");
				switch (j) {
				
				// 녹취 날짜
				case 0:
					column.setWidth("200");
					column.setCache("0");
					column.setId("r_rec_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rRecDate", null,Locale.getDefault())+"</div>");
					break;

				// 녹취 시간 
				case 1:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rRecTime", null,Locale.getDefault())+"</div>");
					break;
					
				// 내선번호
				case 2:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_ext_num");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rExtNum", null,Locale.getDefault())+"</div>");
					break;
					
				// 시스템 코드
				case 3:
					column.setId("r_v_sys_code");
					column.setWidth("100");
					column.setType("combo");
					column.setAlign("center");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=system");
					column.setSort("str");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rVSysCode", null,Locale.getDefault())+"</div>");
					break;

				// STT 시작 시간 
				case 4:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_reg_datetime");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rRegDatetime", null,Locale.getDefault())+"</div>");
					break;

				// STT 시작 시간 
				case 5:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_proc_start_datetime");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rProcStartDatetime", null,Locale.getDefault())+"</div>");
					break;


					// STT 종료 시간 
				case 6:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_proc_end_datetime");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rProcEndDatetime", null,Locale.getDefault())+"</div>");
					break;
					
				// STT 결과 
				case 7:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_result");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rResult", null,Locale.getDefault())+"</div>");
					break;
					
				// STT 결과  검색 위한 Hidden value
				case 8:
					column.setWidth("0");
					column.setCache("0");
					column.setId("r_result_value");
					break;

					// 실패 사유 
				case 9:
					column.setWidth("150");
					column.setCache("0");
					column.setId("r_result_reason");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.sttResult.label.rResultReason", null,Locale.getDefault())+"</div>");
					break;

				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			// 데이터셋 관리 
			SttResult sttResult = new SttResult();
			
			if(!StringUtil.isNull(request.getParameter("sDate"),true)) {
				sttResult.setsDate(request.getParameter("sDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eDate"),true)) {
				sttResult.seteDate(request.getParameter("eDate").replace("-", ""));
			}
			if(!StringUtil.isNull(request.getParameter("sTime"),true)) {
				sttResult.setsTime(request.getParameter("sTime").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("eTime"),true)) {
				sttResult.seteTime(request.getParameter("eTime").replace(":", ""));
			}

			if(!StringUtil.isNull(request.getParameter("rExtNum"),true)) {
				sttResult.setrExtNum(request.getParameter("rExtNum").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("rVSysCode"),true)) {
				sttResult.setrVSysCode(request.getParameter("rVSysCode").replace(":", ""));
			}
			if(!StringUtil.isNull(request.getParameter("rResult"),true)) {
				sttResult.setrResult(request.getParameter("rResult").replace(":", ""));
			}
			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			List<SttResult> sttResultList = transcriptService.selectSttResult(sttResult);
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			for(int i = 0; i < sttResultList.size()  ; i++) {
				SttResult list = sttResultList.get(i);
				String tempStrValue = "";
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				
				//rowItem.setId(list.getrDatasetName());
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				// 녹취 날짜 
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecDate());
				rowItem.getCellElements().add(cellInfo);
				// 녹취 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRecTime());
				rowItem.getCellElements().add(cellInfo);
				// 내선번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrExtNum());
				rowItem.getCellElements().add(cellInfo);
				//	시스템 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrVSysCode());
				rowItem.getCellElements().add(cellInfo);

				// STT 추가 시각
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrRegDatetime());
				rowItem.getCellElements().add(cellInfo);
				
				// STT 실행 시각
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrProcStartDatetime());
				rowItem.getCellElements().add(cellInfo);
				
				// STT 종료 시각
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrProcEndDatetime());
				rowItem.getCellElements().add(cellInfo);
				
				// STT 결과
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(messageSource.getMessage("views.sttResult.type."+list.getrResult(), null,Locale.getDefault()));
				rowItem.getCellElements().add(cellInfo);
				
				// STT 결과 검색용 hidden Value
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrResult());
				rowItem.getCellElements().add(cellInfo);
				
				// STT 실패 이유
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(list.getrResultReason());
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
}
