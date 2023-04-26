package com.furence.recsee.uploadstatus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.uploadstatus.model.UploadInfo;
import com.furence.recsee.uploadstatus.service.UploadInfoService;

@Controller
public class XmlUploadStatusController {
	
	@Autowired
	private UploadInfoService uploadInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;
	
	@Autowired
	private AllowableRangeInfoService allowableRangeInfoService;


	// 	개별 업로드 현황
	@RequestMapping(value="/individualUploadstatus_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml individualUpload_list(HttpServletRequest request ,@RequestParam Map<String,String> params) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			if(request.getParameter("header") != null && request.getParameter("header").equals("true")) {
				xmls.setHeadElement(new dhtmlXGridHead());
	
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
				//String titleBaseName = "management.channel.title.";
				for( int j = 0; j < 14; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					column.setFiltering("1");
					column.setEditable("1");
					column.setCache("1");
					column.setAlign("center");
					column.setWidth("200");
					column.setSort("na");
	
					switch(j) {
					case 0:
						column.setType("ro");
						column.setSort("int");
						column.setFiltering("0");
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("40");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.num", null,Locale.getDefault()) + "</div>");/*순번*/
						break;
					case 1:
						column.setType("ro");
						column.setFiltering("0");
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.recDate", null,Locale.getDefault()) + "</div>");/*"녹취일"*/
						column.setId("recDate");
						break;
					case 2:
						column.setType("ro");
						column.setFiltering("0");
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.recTime", null,Locale.getDefault()) + "</div>");/*"녹취시간"*/
						column.setId("recTime");
						break;
					case 3:
						column.setSort("int");
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.extNum", null,Locale.getDefault()) + "</div>");/*"내선번호"*/
						column.setId("extNum");
						column.setType("ro");
						break;
					case 4:
						column.setSort("int");
						column.setWidth("100");
						column.setValue("<div style=\"text-align:center;\">고객 전화번호</div>");/*"고객 전화번호"*/
						column.setId("custPhone1");
						column.setType("ro");
						break;
					case 5:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.workType", null,Locale.getDefault()) + "</div>");/*"작업종류"*/
						column.setId("procType");
						break;
					case 6:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("100");
						column.setId("result");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.result", null,Locale.getDefault()) + "</div>");/*"처리결과"*/
						break;
						
					case 7:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setId("FTryDatetime");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.firstTrySTime", null,Locale.getDefault()) + "</div>");/*"1차시도(시작일시)"*/
						break;
					case 8:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setId("FTryFinishDatetime");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.firstTryFTime", null,Locale.getDefault()) + "</div>");/*"1차시도(종료일시)"*/
						break;
					case 9:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setId("STryDatetime");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.secondTrySTime", null,Locale.getDefault()) + "</div>");/*"2차시도(시작일시)"*/
						break;
					case 10:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setId("STryFinishDatetime");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.secondTryFTime", null,Locale.getDefault()) + "</div>");/*"2차시도(종료일시)"*/
						break;
					case 11:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setId("TTryDatetime");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.thirdTrySTime", null,Locale.getDefault()) + "</div>");/*"3차시도(시작일시)"*/
						break;
					case 12:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("150");
						column.setId("TTryFinishDatetime");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.thirdTryFTime", null,Locale.getDefault()) + "</div>");/*"3차시도(종료일시)"*/
						break;
					case 13:
						column.setType("ro");
						column.setEditable("0");
						column.setWidth("120");
						column.setId("reTry");
						column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("uploadstatus.grid.directTrans", null,Locale.getDefault()) + "</div>");/*"즉시전송"*/
						break;
					}
					head.getColumnElement().add(column);
					column = null;
				}
				
				xmls.setHeadElement(head);

			} else {
				UploadInfo uploadInfo = new UploadInfo();
				
				
				if(!StringUtil.isNull(params.get("sDate"), true) && !StringUtil.isNull(params.get("eDate"), true)) {
					//	DATE
					uploadInfo.setSrecDate(params.get("sDate").trim());
					uploadInfo.setErecDate(params.get("eDate").trim());
					//	 TIME
					if(!StringUtil.isNull(params.get("sTime"), true) && !StringUtil.isNull(params.get("eTime"), true)) {
						uploadInfo.setSrecTime(params.get("sTime").trim());
						uploadInfo.setErecTime(params.get("eTime").trim());					
					}
				}
			
				
				
				if(!StringUtil.isNull(params.get("extNum"), true)) {
					uploadInfo.setExtNum(params.get("extNum"));
				};
				
				if(!StringUtil.isNull(params.get("custPhone1"), true)) {
					uploadInfo.setCustPhone1(params.get("custPhone1"));
				};
				
				if(!StringUtil.isNull(params.get("workType"), true)) {
					uploadInfo.setWorkType(params.get("workType"));
				};
				
				if(!StringUtil.isNull(params.get("result"), true)) {
					uploadInfo.setResult(params.get("result"));
				};
				
				Integer posStart = 0;
				if(request.getParameter("posStart") != null) {
					posStart = Integer.parseInt(request.getParameter("posStart"));
					uploadInfo.setPosStart(posStart);
				}
				Integer count = 50;
				if(request.getParameter("count") != null) {
					count = Integer.parseInt(request.getParameter("count"));
					uploadInfo.setCount(count);
				}
				
				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P10045");
				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

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
				if(authyInfo != null && authyInfo.size() > 0) {
					uploadInfo.setAuthyInfo(authyInfo);
				}
				
				List<UploadInfo> uploadInfoResult = uploadInfoService.selectUploadInfo(uploadInfo);
				Integer uploadInfoResultTotal = uploadInfoResult.size();
	
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				
				String tryDate	= null;
				
				for(int i = 0; i < uploadInfoResultTotal; i++) {
					UploadInfo uploadItem = uploadInfoResult.get(i);
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setStyle("height:45px");
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
					//번호
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(rowItem.getId());
					rowItem.getCellElements().add(cellInfo);
					
					//녹취 일
					cellInfo = new dhtmlXGridRowCell();
					String tmpDate = null;
					if(uploadItem.getRecDate() != null && uploadItem.getRecDate().length()==8)	tmpDate = String.format("%s-%s-%s", uploadItem.getRecDate().substring(0, 4), uploadItem.getRecDate().substring(4,  6), uploadItem.getRecDate().substring(6, 8));
					cellInfo.setValue(tmpDate);
					rowItem.getCellElements().add(cellInfo);
					
					// 녹취 시간
					cellInfo = new dhtmlXGridRowCell();
					String tmpTime = null;
					if(uploadItem.getRecTime() != null && uploadItem.getRecTime().length()==6)	tmpTime = String.format("%s:%s:%s", uploadItem.getRecTime().substring(0, 2), uploadItem.getRecTime().substring(2,  4), uploadItem.getRecTime().substring(4, 6));
					cellInfo.setValue(tmpTime);
					rowItem.getCellElements().add(cellInfo);
					
				
					// 내선 버노
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(uploadItem.getExtNum().trim());
					cellInfo.setCellClass("inputFilter numberFilter");
					rowItem.getCellElements().add(cellInfo);
					
				
					// 고객 전화번호
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(uploadItem.getCustPhone1().trim());
					cellInfo.setCellClass("inputFilter numberFilter");
					rowItem.getCellElements().add(cellInfo);
					
					
					// 작업 종류
					cellInfo = new dhtmlXGridRowCell();
					if(!StringUtil.isNull(uploadItem.getResult(), true) &&
							(uploadItem.getResult().equals("11")
							|| uploadItem.getResult().equals("16")
							|| uploadItem.getResult().equals("17")
							|| uploadItem.getResult().equals("18")
							|| uploadItem.getResult().equals("19"))) {
						cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.direct", null,Locale.getDefault()));/*"즉시"*/
					}else {
						cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.batch", null,Locale.getDefault()));/*"배치"*/
					}
					rowItem.getCellElements().add(cellInfo);
	
					
					// 처리 결과
					cellInfo = new dhtmlXGridRowCell();
					if(!StringUtil.isNull(uploadItem.getResult(), true)) {
						if(uploadItem.getResult().equals("1")
						|| uploadItem.getResult().equals("11")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.transCompl", null,Locale.getDefault()));/*"전송완료"*/
						} else if(uploadItem.getResult().equals("9")
								|| uploadItem.getResult().equals("19")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.noRec", null,Locale.getDefault()));/*"녹취파일 없음"*/
						} else if(uploadItem.getResult().equals("2")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.retry1", null,Locale.getDefault()));/*"재시도(1차)"*/
						} else if(uploadItem.getResult().equals("3")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.retry1", null,Locale.getDefault()));/*"재시도(1차)"*/
						} else if(uploadItem.getResult().equals("4")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.retry2", null,Locale.getDefault()));/*"재시도(2차)"*/
						} else if(uploadItem.getResult().equals("5")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.retry2", null,Locale.getDefault()));/*"재시도(2차)"*/
						} else if(uploadItem.getResult().equals("6")
								|| uploadItem.getResult().equals("16")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.transFailInfo", null,Locale.getDefault()));/*"전송실패(녹취정보)"*/
						} else if(uploadItem.getResult().equals("7")
								|| uploadItem.getResult().equals("17")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.transFailFile", null,Locale.getDefault()));/*"전송실패(녹취파일)"*/
						} else if(uploadItem.getResult().equals("8")
								|| uploadItem.getResult().equals("18")) {
							cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.waitTrans", null,Locale.getDefault()));/*"즉시전송대기"*/
						}
					} else {
						cellInfo.setValue(messageSource.getMessage("uploadstatus.grid.notSend", null,Locale.getDefault()));/*"미전송"*/
					}
					rowItem.getCellElements().add(cellInfo);
					
					// 1차시도(시작일시)
					if(!StringUtil.isNull(uploadItem.getFTryDatetime(), true)) {
						StringBuffer tryDateInfo = new StringBuffer();
						tryDateInfo.append(uploadItem.getFTryDatetime().substring(0, 14));
						tryDateInfo.insert(4, "-");
						tryDateInfo.insert(7, "-");
						tryDateInfo.insert(10, " ");
						tryDateInfo.insert(13, ":");
						tryDateInfo.insert(16, ":");
						tryDate = tryDateInfo.toString();
					}else {
						tryDate = "";
					}
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(tryDate);
					rowItem.getCellElements().add(cellInfo);
					
					// 1차시도(종료일시)
					if(!StringUtil.isNull(uploadItem.getFTryDatetime(), true) && uploadItem.getFTryDatetime().length()==29) {
						StringBuffer tryDateInfo = new StringBuffer();
						tryDateInfo.append(uploadItem.getFTryDatetime().substring(15, 29));
						tryDateInfo.insert(4, "-");
						tryDateInfo.insert(7, "-");
						tryDateInfo.insert(10, " ");
						tryDateInfo.insert(13, ":");
						tryDateInfo.insert(16, ":");
						tryDate = tryDateInfo.toString();
					}else {
						tryDate = "";
					}
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(tryDate);
					rowItem.getCellElements().add(cellInfo);
					
					// 2차시도(시작일시)
					if(!StringUtil.isNull(uploadItem.getSTryDatetime(), true)) {
						StringBuffer tryDateInfo = new StringBuffer();
						tryDateInfo.append(uploadItem.getSTryDatetime().substring(0, 14));
						tryDateInfo.insert(4, "-");
						tryDateInfo.insert(7, "-");
						tryDateInfo.insert(10, " ");
						tryDateInfo.insert(13, ":");
						tryDateInfo.insert(16, ":");
						tryDate = tryDateInfo.toString();
					}else {
						tryDate = "";
					}
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(tryDate);
					rowItem.getCellElements().add(cellInfo);
					
					// 2차시도(종료일시)
					if(!StringUtil.isNull(uploadItem.getSTryDatetime(), true) && uploadItem.getSTryDatetime().length()==29) {
						StringBuffer tryDateInfo = new StringBuffer();
						tryDateInfo.append(uploadItem.getSTryDatetime().substring(15, 29));
						tryDateInfo.insert(4, "-");
						tryDateInfo.insert(7, "-");
						tryDateInfo.insert(10, " ");
						tryDateInfo.insert(13, ":");
						tryDateInfo.insert(16, ":");
						tryDate = tryDateInfo.toString();
					}else {
						tryDate = "";
					}
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(tryDate);
					rowItem.getCellElements().add(cellInfo);
					
					// 3차시도(시작일시)
					if(!StringUtil.isNull(uploadItem.getTTryDatetime(), true)) {
						StringBuffer tryDateInfo = new StringBuffer();
						tryDateInfo.append(uploadItem.getTTryDatetime().substring(0, 14));
						tryDateInfo.insert(4, "-");
						tryDateInfo.insert(7, "-");
						tryDateInfo.insert(10, " ");
						tryDateInfo.insert(13, ":");
						tryDateInfo.insert(16, ":");
						tryDate = tryDateInfo.toString();
					}else {
						tryDate = "";
					}
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(tryDate);
					rowItem.getCellElements().add(cellInfo);
					
					// 3차시도(종료일시)
					if(!StringUtil.isNull(uploadItem.getTTryDatetime(), true) && uploadItem.getTTryDatetime().length()==29) {
						StringBuffer tryDateInfo = new StringBuffer();
						tryDateInfo.append(uploadItem.getTTryDatetime().substring(15, 29));
						tryDateInfo.insert(4, "-");
						tryDateInfo.insert(7, "-");
						tryDateInfo.insert(10, " ");
						tryDateInfo.insert(13, ":");
						tryDateInfo.insert(16, ":");
						tryDate = tryDateInfo.toString();
					}else {
						tryDate = "";
					}
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(tryDate);
					rowItem.getCellElements().add(cellInfo);
					
					
					// 전송 요청
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.isNull(uploadItem.getResult(), true) || (uploadItem.getResult().equals("6") || uploadItem.getResult().equals("7"))) {
						cellInfo.setValue("<button style=\"float:none;\"  onclick=\"directUpload("+String.valueOf(i+1)+")\">"+messageSource.getMessage("uploadstatus.grid.transfer", null,Locale.getDefault())+"</button>");/*전송*/
					}
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
				Integer totalListResult = uploadInfoService.CountUploadSelect(uploadInfo);
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
			
}