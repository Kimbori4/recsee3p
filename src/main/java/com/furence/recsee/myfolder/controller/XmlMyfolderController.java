package com.furence.recsee.myfolder.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
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
import com.furence.recsee.myfolder.model.MyFolderListinfo;
import com.furence.recsee.myfolder.service.MyFolderService;

@Controller
public class XmlMyfolderController {

	@Autowired
	MyFolderService myFolderService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private MessageSource messageSource;

	// 사용자 관리 그리드
	@RequestMapping(value = "/myfolderGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml myfolderGrid(HttpServletRequest request) throws UnsupportedEncodingException{

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
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;

				case 1:
					column.setWidth("100");
					column.setCache("0");
					column.setId("r_rec_date");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.label.recDate", null,Locale.getDefault())+"</div>");
					break;

				case 2:
					column.setWidth("80");
					column.setCache("0");
					column.setId("r_rec_time");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.rectime", null,Locale.getDefault())+"</div>");
					break;

				case 3:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;

				case 4:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;

				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;

				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("admin.grid.emp", null,Locale.getDefault())+"</div>");
					break;
				case 7:
					column.setWidth("80");
					column.setCache("0");
					column.setId("r_user_name");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.head.R_USER_NAME", null,Locale.getDefault())+"</div>");
					break;
				case 8:
					column.setWidth("80");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_ext_num");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("views.search.grid.user.title.ext", null,Locale.getDefault())+"</div>");
					break;
				case 9:
					column.setWidth("*");
					column.setCache("0");
					column.setFiltering("0");
					column.setId("r_cust_name");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("search.option.custName", null,Locale.getDefault())+"</div>");
					break;
				case 10:
					column.setWidth("*");
					column.setCache("0");
					column.setId("r_cust_phone1");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("search.option.custNo", null,Locale.getDefault())+"</div>");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			xmls.setHeadElement(head);

			//	마이폴더 그리드 표출
			MyFolderListinfo myfolderListinfo = new MyFolderListinfo();

			myfolderListinfo.setrUserId(userInfo.getUserId());
			myfolderListinfo.setrFolderName(URLDecoder.decode(request.getParameter("folderName"),"utf-8"));

			// Postgres 암호화 사용여부
			String postgresColumn = "";
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("ENCRYPT");
			etcConfigInfo.setConfigKey("POSTGRES");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
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
			
			if(!"".equals(postgresColumn)) {
				if(postgresColumn.contains("r_cust_phone1")) {
					myfolderListinfo.setCustPhone1IsEncrypt("Y");
				}
				
				if(postgresColumn.contains("r_cust_name")) {
					myfolderListinfo.setCustNameIsEncrypt("Y");
				}
			}
			
			
			// 마이폴더 조회
			List<MyFolderListinfo> listResult = myFolderService.selectMyfolderListInfo(myfolderListinfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			/* 20200129 김다빈 추가 */
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
			
			for(int i = 0; i < listResult.size()  ; i++) {
				MyFolderListinfo list = listResult.get(i);
				String tempStrValue = "";

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(list.getrItemSerial());
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

				/* 20200129 김다빈 추가 */
				// 고객번호
				cellInfo = new dhtmlXGridRowCell();
				tempStrValue = list.getrCustPhone();
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
				
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
				
				//	고객번호
				/*cellInfo = new dhtmlXGridRowCell();
				tempStrValue=new RecSeeUtil().makePhoneNumber(list.getrCustPhone());
				tempStrValue = new RecSeeUtil().maskingNumber(tempStrValue);
				cellInfo.setValue(tempStrValue);
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;*/
			}

		}

		return xmls;
	}
}
