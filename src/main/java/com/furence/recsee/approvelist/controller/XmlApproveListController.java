package com.furence.recsee.approvelist.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.ApproveListInfo;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.main.service.SearchListInfoService;

@Controller

public class XmlApproveListController {
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@Autowired
	private AllowableRangeInfoService allowableRangeInfoService;
	
	@Autowired
	private SearchListInfoService searchListInfoService;
	
	@Autowired
	private MessageSource messageSource;
	
	
	// 다운로드 요청 관리 그리드
		@RequestMapping(value="/approve_list.xml", method=RequestMethod.GET, produces = "application/xml")
		public @ResponseBody dhtmlXGridXml approveListGrid(HttpServletRequest request, HttpServletResponse response) {

			CookieSetToLang cls = new CookieSetToLang();
			cls.langSetFunc(request, response);

			LoginVO userInfo = SessionManager.getUserInfo(request);
			dhtmlXGridXml xmls = null;
			boolean FileSettingboolean = false;
			int columnSize = 35;
			int approvealCnt = 3;

			if (userInfo != null) {
				
				EtcConfigInfo APPROVE = new EtcConfigInfo();
				APPROVE.setGroupKey("APPROVE");
				APPROVE.setConfigKey("APPROVEALCNT");
				List<EtcConfigInfo> APPROVEResult = etcConfigInfoService.selectEtcConfigInfo(APPROVE);
				
				if(APPROVEResult.size() > 0) {
					
					approvealCnt  = Integer.parseInt(APPROVEResult.get(0).getConfigValue());
					
				}else {
					APPROVE.setConfigValue("3");
					APPROVE.setDesc("청취 및 파일 다운로드 승인 권한 카운터");
					APPROVE.setConfigOption("1/2/3");
					
					etcConfigInfoService.insertEtcConfigInfo(APPROVE);
				}

				EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
				safeDBetcConfigInfo.setGroupKey("SEARCH");
				safeDBetcConfigInfo.setConfigKey("SAFE_DB");
				List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);
				
				// 파일다운로드 요청시 파일이름 변경 기능을 사용할지 여부
				EtcConfigInfo FileNameSettingYn = new EtcConfigInfo();
				FileNameSettingYn.setGroupKey("FILE_NAME_SETTING");
				FileNameSettingYn.setConfigKey("USE_YN");
				List<EtcConfigInfo> FileNameSettingYn_result = etcConfigInfoService.selectEtcConfigInfo(FileNameSettingYn);
				
				if(FileNameSettingYn_result.size() > 0) {
					if(FileNameSettingYn_result.get(0).getConfigValue().equals("Y")) {
						FileSettingboolean = true;
						columnSize = 37;
					}
					
				}else {
					FileNameSettingYn.setDesc("FileName Setting Use YN");
					FileNameSettingYn.setConfigValue("N");
					FileNameSettingYn.setConfigOption("Y/N");
					etcConfigInfoService.insertEtcConfigInfo(FileNameSettingYn);					
				}


				xmls = new dhtmlXGridXml();

				if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
					xmls.setHeadElement(new dhtmlXGridHead());

					dhtmlXGridHead head = new dhtmlXGridHead();

					head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

					for(int i=0;i<columnSize;i++) {
						dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

						column.setType("ro");
						column.setSort("str");
						column.setAlign("center");
						column.setFiltering("1");
						column.setEditable("0");
						column.setCache("1");
						column.setHidden("0");

						switch(i) {
						case 0:
							column.setType("ch");
							column.setFiltering("0");
							column.setEditable("0");
							column.setCache("0");
							column.setWidth("50");
							column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
							break;						
						case 1:							
							column.setSort("int");
							column.setFiltering("0");
							column.setEditable("0");
							column.setCache("0");
							column.setWidth("50");
							column.setValue(messageSource.getMessage("views.approveList.grid.number", null,Locale.getDefault()));/*번호*/
							break;
						case 2://
							column.setWidth("90");
							column.setId("reqDate");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqDate", null,Locale.getDefault()));/*"요청 일자"*/
							break;
						case 3://
							column.setWidth("80");
							column.setId("reqTime");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqTime", null,Locale.getDefault()));/*"요청 시간"*/
							break;
						case 4://
							column.setWidth("100");
							column.setId("reqId");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqId", null,Locale.getDefault()));/*"요청자 아이디"*/
							break;
						case 5://
							column.setWidth("100");
							column.setId("reqName");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqName", null,Locale.getDefault()));/*"요청자 이름"*/
							break;
						case 6://
							column.setWidth("120");
							column.setId("reqGroup");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqGroup", null,Locale.getDefault()));/*"요청자 소속"*/
							break;
						case 7://
							column.setWidth("80");
							column.setType("combo");
							column.setId("approveType");
							column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=approveType");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqType", null,Locale.getDefault()));/*"요청 유형"*/
							break;
						case 8://
							column.setWidth("120");
							column.setType("combo");
							column.setId("approveReason");
							column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=approveReason");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqReason", null,Locale.getDefault()));/*"요청 사유"*/
							break;
						case 9://
							column.setWidth("150");
							column.setType("combo");
							column.setId("approveState");
							column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=common&comboType2=approveState");
							column.setValue(messageSource.getMessage("views.approveList.grid.state", null,Locale.getDefault()));/*"진행 상태"*/
							break;
						case 10://
							column.setWidth("200");
							column.setId("fileName");
							column.setValue(messageSource.getMessage("views.approveList.grid.fileName", null,Locale.getDefault()));/*"파일명"*/
							break;
						case 11://
							column.setWidth("90");
							column.setId("recDate");
							column.setValue(messageSource.getMessage("views.approveList.grid.recDate", null,Locale.getDefault()));/*"녹취 일자"*/
							break;
						case 12://
							column.setWidth("80");
							column.setId("recTime");
							column.setValue(messageSource.getMessage("views.approveList.grid.recTime", null,Locale.getDefault()));/*"녹취 시간"*/
							break;
						case 13://
							column.setWidth("80");
							column.setId("recExtNum");
							column.setValue(messageSource.getMessage("views.approveList.grid.recExtNum", null,Locale.getDefault()));/*"녹취 내선"*/
							break;
						case 14://
							column.setWidth("100");
							column.setId("recId");
							column.setValue(messageSource.getMessage("views.approveList.grid.recId", null,Locale.getDefault()));/*"녹취자 아이디"*/
							break;
						case 15://
							column.setWidth("80");
							column.setId("recName");
							column.setValue(messageSource.getMessage("views.approveList.grid.recName", null,Locale.getDefault()));/*"녹취자 이름"*/
							break;
						case 16://
							column.setWidth("80");
							column.setId("custName");
							column.setValue(messageSource.getMessage("views.approveList.grid.custName", null,Locale.getDefault()));/*"녹취 고객명"*/
							break;
						case 17://
							column.setWidth("120");
							column.setId("custNum");
							column.setValue(messageSource.getMessage("views.approveList.grid.custNum", null,Locale.getDefault()));/*"녹취 고객 전화번호"*/
							break;
						case 18://
							column.setWidth("100");
							column.setId("stockNum");
							column.setValue(messageSource.getMessage("views.approveList.grid.stockNum", null,Locale.getDefault()));/*"녹취 증권번호"*/
							column.setHidden("1");
							break;
						case 19://
							column.setWidth("80");
							column.setId("callType");
							column.setValue(messageSource.getMessage("views.approveList.grid.callType", null,Locale.getDefault()));/*"녹취 콜타입"*/
							break;
						case 20://
							column.setWidth("90");
							column.setValue(messageSource.getMessage("views.approveList.grid.firstApprovalDate", null,Locale.getDefault()));/*"1차 승인 날짜"*/
							if(approvealCnt < 2) {
								column.setHidden("1");
							}
							break;
						case 21://
							column.setWidth("90");
							column.setValue(messageSource.getMessage("views.approveList.grid.firstApprovalTime", null,Locale.getDefault()));/*"1차 승인 시간"*/
							if(approvealCnt < 2) {
								column.setHidden("1");
							}
							break;
						case 22://
							column.setWidth("120");
							column.setValue(messageSource.getMessage("views.approveList.grid.primaryApprover", null,Locale.getDefault()));/*"1차 승인자 아이디"*/
							if(approvealCnt < 2) {
								column.setHidden("1");
							}
							break;
						case 23://
							column.setWidth("90");
							column.setValue(messageSource.getMessage("views.approveList.grid.secondApprovalDate", null,Locale.getDefault()));/*"2차 승인 날짜"*/
							if(approvealCnt < 3) {
								column.setHidden("1");
							}
							break;
						case 24://
							column.setWidth("90");
							column.setValue(messageSource.getMessage("views.approveList.grid.secondApprovalTime", null,Locale.getDefault()));/*"2차 승인 시간"*/
							if(approvealCnt < 3) {
								column.setHidden("1");
							}
							break;
						case 25://
							column.setWidth("120");
							column.setValue(messageSource.getMessage("views.approveList.grid.secondApprover", null,Locale.getDefault()));/*"2차 승인자 아이디"*/
							if(approvealCnt < 3) {
								column.setHidden("1");
							}
							break;
						case 26://
							column.setWidth("90");
							column.setId("finalApprovalDate");
							column.setValue(messageSource.getMessage("views.approveList.grid.finalApprovalDate", null,Locale.getDefault()));/*"최종 승인 날짜"*/
							break;
						case 27://
							column.setWidth("90");
							column.setId("finalApprovalTime");
							column.setValue(messageSource.getMessage("views.approveList.grid.finalApprovalTime", null,Locale.getDefault()));/*"최종 승인 시간"*/
							break;
						case 28://
							column.setWidth("120");
							column.setId("finalApprover");
							column.setValue(messageSource.getMessage("views.approveList.grid.finalApprover", null,Locale.getDefault()));/*"최종 승인자 아이디"*/
							break;
						case 29://
							column.setWidth("100");
							column.setId("reqPeriod");
							column.setValue(messageSource.getMessage("views.approveList.grid.reqPeriod", null,Locale.getDefault()));/*"요청 기간"*/
							break;
						case 30://
							column.setWidth("100");
							column.setValue("ip");
							column.setHidden("1");
							break;
						case 31://대그룹코드
							column.setWidth("100");
							column.setValue("bgcode");
							column.setHidden("1");
							break;
						case 32://중그룹코드
							column.setWidth("100");
							column.setValue("mgcode");
							column.setHidden("1");
							break;
						case 33://소그룹코드
							column.setWidth("100");
							column.setValue("sgcode");
							column.setHidden("1");
							break;
						case 34://
							column.setWidth("100");
							column.setId("rec_url");
							column.setValue("url");
							column.setHidden("1");
							break;
						case 35://파일형식 키
							column.setWidth("200");
							column.setValue(messageSource.getMessage("views.approveList.grid.fileColumnKey", null,Locale.getDefault()));/*"파일명 형식"*/
							column.setId("fileColumnKey");
							column.setHidden("1");
							break;		
						case 36://파일형식 값
							column.setWidth("100");
							column.setValue("fileColumnValue");
							column.setId("fileColumnValue");
							column.setHidden("1");
							break;
						}
						head.getColumnElement().add(column);
						column = null;
					}

					xmls.setHeadElement(head);

				}else {
					ApproveListInfo approveListInfo = new ApproveListInfo();

					MMenuAccessInfo accessInfo = new MMenuAccessInfo();
					accessInfo.setLevelCode(userInfo.getUserLevel());
					accessInfo.setProgramCode("P10027");
					List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

					// 그룹 조회 권한
					List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
					
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

							if(!accessResult.get(0).getAccessLevel().equals("B")) {
								item.put("mgcode", userInfo.getMgCode());
							}
							if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M")) {
								item.put("sgcode", userInfo.getSgCode());
							}
							if(!accessResult.get(0).getAccessLevel().equals("B") && !accessResult.get(0).getAccessLevel().equals("M") && !accessResult.get(0).getAccessLevel().equals("S")) {
								item.put("user", userInfo.getUserId());
							}

							authyInfo.add(item);
						}
					}
					

					// 접수, 승인 권한
					approveListInfo.setApproveYn(accessResult.get(0).getApproveYn());
					approveListInfo.setReciptYn(accessResult.get(0).getReciptYn());
					approveListInfo.setPrereciptDate(accessResult.get(0).getPrereciptYn());

					if(authyInfo != null && authyInfo.size() > 0) {
						approveListInfo.setAuthyInfo(authyInfo);
					}

					if(!StringUtil.isNull(request.getParameter("reqsDate"),true))
						approveListInfo.setReqsDate(request.getParameter("reqsDate"));

					if(!StringUtil.isNull(request.getParameter("reqeDate"),true))
						approveListInfo.setReqeDate(request.getParameter("reqeDate"));

					if(!StringUtil.isNull(request.getParameter("reqsTime"),true))
						approveListInfo.setReqsTime(request.getParameter("reqsTime"));

					if(!StringUtil.isNull(request.getParameter("reqeTime"),true))
						approveListInfo.setReqeTime(request.getParameter("reqeTime"));

					if(!StringUtil.isNull(request.getParameter("aUserId"),true))
						approveListInfo.setUserId(request.getParameter("aUserId"));

					if(!StringUtil.isNull(request.getParameter("aUserName"),true))
						approveListInfo.setUserName(request.getParameter("aUserName"));

					if(!StringUtil.isNull(request.getParameter("approveType"),true))
						approveListInfo.setApproveType(request.getParameter("approveType"));

					if(!StringUtil.isNull(request.getParameter("approveReason"),true))
						approveListInfo.setApproveReason(request.getParameter("approveReason"));

					if(!StringUtil.isNull(request.getParameter("approveState"),true))
						approveListInfo.setApproveState(request.getParameter("approveState"));

					List<ApproveListInfo> approveListResult = searchListInfoService.selectApproveInfo(approveListInfo);
					int approveListCnt = approveListResult.size();

					xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
					dhtmlXGridRowCell cellInfo = null;
					dhtmlXGridRowUserdata userdataInfo = null;

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

					for (int i=0; i<approveListCnt; i++) {
						ApproveListInfo item = approveListResult.get(i);
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(String.valueOf(i+1));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
						rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());

						// 체크박스
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("0");
						rowItem.getCellElements().add(cellInfo);
						
						//번호
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(rowItem.getId());
						rowItem.getCellElements().add(cellInfo);

						// 요청일자
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getReqDate());
						rowItem.getCellElements().add(cellInfo);

						// 요청 시간
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getReqTime());
						rowItem.getCellElements().add(cellInfo);

						// 사용자 아이디
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getUserId());
						rowItem.getCellElements().add(cellInfo);

						// 사용자 이름
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getUserName());
						rowItem.getCellElements().add(cellInfo);

						// 소속 그룹
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getSgName());
						rowItem.getCellElements().add(cellInfo);

						// 요청 유형
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getApproveType());
						rowItem.getCellElements().add(cellInfo);

						// 요청 사유
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getApproveReason());
						rowItem.getCellElements().add(cellInfo);

						// 진행 상태
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getApproveState());
						rowItem.getCellElements().add(cellInfo);

						// 파일명
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getFileName());
						rowItem.getCellElements().add(cellInfo);

						// 녹취 일자
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getRecDate());
						rowItem.getCellElements().add(cellInfo);

						// 녹취 시간
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getRecTime());
						rowItem.getCellElements().add(cellInfo);

						// 녹취 내선
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getRecExt());
						rowItem.getCellElements().add(cellInfo);

						// 녹취자 사번
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getUserId());
						rowItem.getCellElements().add(cellInfo);

						// 녹취자 이름
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getUserNameCall());
						rowItem.getCellElements().add(cellInfo);


						// 녹취자 고객명
						/*userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("r_cust_name");
						userdataInfo.setValue(item.getCustName());
						rowItem.getUserdataElements().add(userdataInfo);*/

						cellInfo = new dhtmlXGridRowCell();

						boolean recCustName=false;
						String tempStr = item.getCustName();

						if(StringUtil.isNull(tempStr,true))
							tempStr= "-";
						else {
							if(safeDbResult.size() > 0) {
								if("Y".equals(safeDbResult.get(0).getConfigValue()))
									tempStr = item.SafeDBGetter(item.getCustName());
							}
						}
						cellInfo.setValue(tempStr);
						if (maskingInfo!=null && maskingInfo.contains("r_cust_name")){
							cellInfo.setValue(new RecSeeUtil().makingName(tempStr));
						}else {
							cellInfo.setValue(tempStr);
						}
						rowItem.getCellElements().add(cellInfo);

						// 녹취자 고객전화번호						
						
						String tempStrValue = new RecSeeUtil().makePhoneNumber(item.getCustPhone1());
						/*userdataInfo = new dhtmlXGridRowUserdata();
						userdataInfo.setName("r_cust_phone1");
						userdataInfo.setValue(tempStrValue);
						rowItem.getUserdataElements().add(userdataInfo);*/

						cellInfo = new dhtmlXGridRowCell();
						if (maskingInfo!=null && maskingInfo.contains("r_cust_phone1")){
							cellInfo.setValue(new RecSeeUtil().maskingNumber(tempStrValue));
						}else {
							cellInfo.setValue(tempStrValue);
						}
						rowItem.getCellElements().add(cellInfo);

						// 녹취자 증권번호
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getStockNo());
						rowItem.getCellElements().add(cellInfo);

						// 녹취자 콜타입
						cellInfo = new dhtmlXGridRowCell();
						String temp="";
						switch (item.getCallKind1()) {
						case "I":
							temp="수신";
							break;
						case "O":
							temp="발신";
							break;
						case "T":
							temp="전환통화";
							break;
						case "C":
							temp="회의통화";
							break;
						case "Z":
							temp="내선통화";
							break;
						}
						cellInfo.setValue(temp);
						rowItem.getCellElements().add(cellInfo);



						// 지점 신청일
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getPrereciptDate());
						rowItem.getCellElements().add(cellInfo);

						// 지점 신청시
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getPrereciptTime());
						rowItem.getCellElements().add(cellInfo);

						// 지점 신청자
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getPrereciptId());
						rowItem.getCellElements().add(cellInfo);

						// 접수일
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getReciptDate());
						rowItem.getCellElements().add(cellInfo);

						// 접수시
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getReciptTime());
						rowItem.getCellElements().add(cellInfo);

						// 접수자
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getReciptId());
						rowItem.getCellElements().add(cellInfo);


						// 승인일
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getApproveDate());
						rowItem.getCellElements().add(cellInfo);

						// 승인시
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getApproveTime());
						rowItem.getCellElements().add(cellInfo);

						// 승인자
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getApproveId());
						rowItem.getCellElements().add(cellInfo);

						// 요청 제한일
						cellInfo = new dhtmlXGridRowCell();
						if (StringUtil.isNull(item.getApproveDay(),true))
							cellInfo.setValue("");
						else
							cellInfo.setValue(item.getApproveDay() + " 일 동안");
						rowItem.getCellElements().add(cellInfo);
	
						// ip
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getvRecIp());
						rowItem.getCellElements().add(cellInfo);

					/*	// fullpath
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getvRecFullpath());
						rowItem.getCellElements().add(cellInfo);*/

						// 대그룹코드
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getBgCode());
						rowItem.getCellElements().add(cellInfo);


						// 중그룹코드
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getMgCode());
						rowItem.getCellElements().add(cellInfo);


						// 소그룹코드
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue(item.getSgCode());
						rowItem.getCellElements().add(cellInfo);

						//url
						cellInfo = new dhtmlXGridRowCell();
						//cellInfo.setValue("http://"+item.getvRecIp()+":28881/listen?url="+item.getvRecFullpath());
						cellInfo.setValue(item.getListenUrl());
						rowItem.getCellElements().add(cellInfo);

						
						if(FileSettingboolean == true) {
							// 소그룹코드
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(item.getFileColumnKey());
							rowItem.getCellElements().add(cellInfo);
							
							// 소그룹코드
							cellInfo = new dhtmlXGridRowCell();
							cellInfo.setValue(item.getFileColumnValue());
							rowItem.getCellElements().add(cellInfo);
						}

						xmls.getRowElements().add(rowItem);
						rowItem = null;

						//
					}
				}
			}else {
				xmls = null;
			}
			return xmls;
		}
}