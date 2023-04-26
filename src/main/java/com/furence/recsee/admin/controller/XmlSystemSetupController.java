package com.furence.recsee.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBManage;
import com.furence.recsee.admin.model.ExecuteManage;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.admin.model.PbxInfo;
import com.furence.recsee.admin.model.PublicIpInfo;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.admin.model.SttServerInfo;
import com.furence.recsee.admin.model.UserDBInterface;
import com.furence.recsee.admin.service.ChannelInfoService;
import com.furence.recsee.admin.service.DBInfoService;
import com.furence.recsee.admin.service.DBManageService;
import com.furence.recsee.admin.service.ExecuteManageService;
import com.furence.recsee.admin.service.JobManageService;
import com.furence.recsee.admin.service.PbxInfoService;
import com.furence.recsee.admin.service.PublicIpInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.admin.service.SQLManageService;
import com.furence.recsee.admin.service.SttServerInfoService;
import com.furence.recsee.admin.service.UserDBInterfaceService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.LogoVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.PacketVO;
import com.furence.recsee.common.model.PhoneMappingInfo;
import com.furence.recsee.common.model.SubNumberInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.PhoneMappingInfoService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadAfterInit;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;

@Controller
public class XmlSystemSetupController {
	private static final Logger logger = LoggerFactory.getLogger(XmlSystemSetupController.class);

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private SystemInfoService systemInfoService;
	
	@Autowired
	private SttServerInfoService sttServerInfoService;

	@Autowired
	private SubNumberInfoService subNumberInfoService;
	
	@Autowired
	private PhoneMappingInfoService phoneMappingInfoService;
	
	@Autowired
	private PublicIpInfoService publicIpInfoService;
	
	@Autowired
	private DBInfoService dbInfoService;

	@Autowired
	private DBManageService dbManageService;
	
	@Autowired
	private SQLManageService sqlManageService;
	
	@Autowired
	private JobManageService jobManageService;
	
	@Autowired
	private ExecuteManageService executeManageService;
	
	@Autowired
	private UserDBInterfaceService userDBInterfaceService;
	
	@Autowired
	private ChannelInfoService channelInfoService;

	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private PbxInfoService pbxInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private MessageSource messageSource;

	// 채널 관리 그리드
	@RequestMapping(value="/channel_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml channel_list(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.channel");

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("SHOW_GENESYS_REGISTER_YN");
			List<EtcConfigInfo> showGenesysRegister = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			String showGenesysRegisterYn = "N";
			if (showGenesysRegister != null && showGenesysRegister.size() > 0) {
				showGenesysRegisterYn = showGenesysRegister.get(0).getConfigValue();
			}
			etcConfigInfo.setConfigKey("SHOW_BACKUP_IP_INFO");
			List<EtcConfigInfo> showBackupIpInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			String showBackupIpInfoYn = "N";
			if (showBackupIpInfo != null && showBackupIpInfo.size() > 0) {
				showGenesysRegisterYn = showBackupIpInfo.get(0).getConfigValue();
			}
			
			//String titleBaseName = "management.channel.title.";
			for( int j = 0; j < 15; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("100");

				switch(j) {
				case 0:
					column.setType("ch");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				case 1:
					column.setType("ro");
					column.setSort("int");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("No");
					column.setId("idx");
					break;
				case 2:
					column.setType("ro");
					column.setSort("int");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.channel.label.channelNo", null,Locale.getDefault()));
					column.setId("chNum");
					break;
				case 3:
					//column.setType("combo");
					column.setSort("str");
					column.setType("ro");
					//column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=system");
					column.setEditable("0");
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.channel.label.system", null,Locale.getDefault()));
					column.setId("systemCode");
					break;
				case 4:
					column.setSort("int");
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.channel.label.ext", null,Locale.getDefault()));
					column.setId("extNum");
					column.setType("ro");
					/*if(!nowAccessInfo.getModiYn().equals("Y"))
						column.setType("ro");
					else
						column.setType("edn");*/
					break;
				case 5:
					column.setWidth("150");
					column.setValue("IP");
					column.setId("chIp");
					column.setSort("str");
					if(!nowAccessInfo.getModiYn().equals("Y"))
						column.setType("ro");
					else
						column.setType("ed");
					break;
				case 6:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.channel.label.backupIP", null,Locale.getDefault()));
					column.setSort("str");
					column.setId("chAgtIp");
					column.setFiltering("0");
					if(!nowAccessInfo.getModiYn().equals("Y"))
						column.setType("ro");
					else
						column.setType("ed");
					break;
				case 7:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.channel.label.backupIPTime", null,Locale.getDefault()));
					column.setSort("str");
					column.setId("agtIpUpdateTime");
					column.setFiltering("0");
					column.setType("ro");
					break;
				case 8:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN2");
					column.setEditable("0");
					column.setWidth("100");
					column.setId("recYn");
					column.setValue(messageSource.getMessage("admin.channel.label.usageRec", null,Locale.getDefault()));
					break;
				case 9:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN5");
					column.setEditable("0");
					column.setWidth("100");
					column.setId("genesysRegisterYn");
					column.setValue(messageSource.getMessage("admin.channel.label.ctiConnect", null,Locale.getDefault()));
					if("N".equals(showGenesysRegisterYn)) {
						column.setHidden("1");
					}
					break;
				case 10:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=callType");
					column.setHidden("1");
					column.setEditable("0");
					column.setWidth("100");
					column.setId("recType");
					column.setValue(messageSource.getMessage("admin.channel.label.recKind", null,Locale.getDefault()));
					break;
				case 11:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=callKind");
					column.setEditable("0");
					column.setWidth("150");
					column.setId("recKind");
					column.setValue(messageSource.getMessage("admin.channel.label.recType", null,Locale.getDefault()));
					break;
				case 12:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN2");
					column.setHidden("1");
					column.setEditable("0");
					column.setWidth("100");
					column.setId("screenYn");
					column.setValue(messageSource.getMessage("admin.channel.label.screen", null,Locale.getDefault()));
					break;
				case 13:
					column.setType("ro");
					column.setSort("na");
					column.setEditable("0");
					column.setWidth("70");
					column.setValue(messageSource.getMessage("admin.channel.label.removeChannel2", null,Locale.getDefault()));
					break;
				case 14:
					column.setSort("na");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("*");
					column.setValue("");
					break;
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
			headCall.getParamElement().add(
					"#rspan,"
					+ "#rspan,"
					+ "<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,"
					+ "#select_filter,"
					+ "#text_filter,"
					+ "#text_filter,"
					+ "#text_filter,"
					+ "#text_filter,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "#select_filter,"
					+ "<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/icon/btn_icon/icon_btn_trash_gray.png' width='18' height='18' /></div>,"
			);

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			ChannelInfo channelInfo = new ChannelInfo();
			SystemInfo systemInfo = new SystemInfo();

			List<SystemInfo> systemInfoResult = systemInfoService.selectSystemInfo(systemInfo);

			List<ChannelInfo> channelInfoResult = channelInfoService.selectChannelInfo(channelInfo);
			Integer channelInfoResultTotal = channelInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			Map<String,Integer> rowNumMap = new HashMap<String,Integer>();
			for(int i = 0; i < channelInfoResultTotal; i++) {
				ChannelInfo channelItem = channelInfoResult.get(i);
				if (rowNumMap.get(channelItem.getSysCode()) == null) {
					rowNumMap.put(channelItem.getSysCode(), 1);
				} else {
					rowNumMap.put(channelItem.getSysCode(), rowNumMap.get(channelItem.getSysCode())+1);
				}
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// 췤췤 체크박스
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				// 채널 버노
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(rowNumMap.get(channelItem.getSysCode())));
				rowItem.getCellElements().add(cellInfo);
				
				// 채널 버노
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getChNum().toString());
				rowItem.getCellElements().add(cellInfo);

				// 시스테므 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult));
				rowItem.getCellElements().add(cellInfo);

				// 내선 버노
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getExtNum().trim());
				cellInfo.setCellClass("inputFilter numberFilter");
				rowItem.getCellElements().add(cellInfo);

				// 아이피
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getExtIp().trim());
				cellInfo.setCellClass("inputFilter ipFilter");
				rowItem.getCellElements().add(cellInfo);

				// 백업 아이피
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getExtAgtIp() == null) cellInfo.setValue("");
				else cellInfo.setValue(channelItem.getExtAgtIp().trim());
				cellInfo.setCellClass("inputFilter ipFilter");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				String tmpStr = "";
				if (channelItem.getExtKey() != null && channelItem.getExtKey().length() == 14) {
					tmpStr = channelItem.getExtKey();
					tmpStr = tmpStr.substring(0,4) + "-" +tmpStr.substring(4,6) + "-" + tmpStr.substring(6,8) + " " + tmpStr.substring(8,10) + ":" + tmpStr.substring(10,12) + ":" + tmpStr.substring(12,14); 
				}
				cellInfo.setValue(tmpStr);
				rowItem.getCellElements().add(cellInfo);
				
				// 녹취 사용 유무
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getRecYn().trim());
				rowItem.getCellElements().add(cellInfo);

				// 제네시스 등록 여부
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getChTenant() == null) cellInfo.setValue("N");
				else cellInfo.setValue(channelItem.getChTenant().trim());
				rowItem.getCellElements().add(cellInfo);

				// 녹취 종류(숨김처뤼)
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getRecType() == null) cellInfo.setValue("");
				else cellInfo.setValue(channelItem.getRecType().trim());
				rowItem.getCellElements().add(cellInfo);

				// 녹취 유형
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getExtKind() == null) cellInfo.setValue("A");
				else cellInfo.setValue(channelItem.getExtKind().trim());
				rowItem.getCellElements().add(cellInfo);

				// 스크린 사용 유무
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getScreenYn() == null) cellInfo.setValue("");
				else cellInfo.setValue(channelItem.getScreenYn().trim());
				rowItem.getCellElements().add(cellInfo);

				// 삭제
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat icon_btn_trash_gray' onClick='deleteChannel(\""+String.valueOf(i+1)+"\")'></button>");
				rowItem.getCellElements().add(cellInfo);

				//빈칸 이쁘다
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}

	// 채널별 모니터링 그리드 (콜별)
	@RequestMapping(value="/channelMonitoring_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml channelMonitoring_list(HttpServletRequest request, HttpServletResponse response) {
	
	CookieSetToLang cls = new CookieSetToLang();
	cls.langSetFunc(request, response);
		
	LoginVO userInfo = SessionManager.getUserInfo(request);
	dhtmlXGridXml xmls = null;

	@SuppressWarnings("unchecked")
	List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
	MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.channelMonitoring");
	
	if(userInfo != null) {
	
		xmls = new dhtmlXGridXml();
		xmls.setHeadElement(new dhtmlXGridHead());
	
		dhtmlXGridHead head = new dhtmlXGridHead();
		head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
	
		//String titleBaseName = "management.channel.title.";
		for( int j = 0; j < 12; j++ ) {
			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
	
			column.setFiltering("1");
			column.setEditable("1");
			column.setCache("1");
			column.setAlign("center");
			column.setWidth("100");
	
			switch(j) {
			case 0:
				column.setType("ro");
				column.setSort("int");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setWidth("100");
				column.setValue(messageSource.getMessage("admin.grid.num", null,Locale.getDefault()));
				break;
			case 1:
				column.setSort("int");
				column.setWidth("130");
				column.setValue(messageSource.getMessage("header.popup.modifyUser.label.ext", null,Locale.getDefault()));
				column.setId("extNum");
				column.setType("ro");
				break;
			case 2:
				column.setSort("int");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setValue(messageSource.getMessage("header.popup.modifyUser.label.userName", null,Locale.getDefault()));
				column.setId("userId");
				break;
			case 3:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setValue(messageSource.getMessage("header.popup.modifyUser.label.userEmp", null,Locale.getDefault()));
				column.setId("userName");
				break;
			case 4:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setValue(messageSource.getMessage("admin.grid.center", null,Locale.getDefault()));
				column.setId("mgCode");
				break;
			case 5:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setValue(messageSource.getMessage("admin.grid.sg", null,Locale.getDefault()));
				column.setId("sgCode");
				break;
			case 6:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setValue(messageSource.getMessage("admin.grid.skill", null,Locale.getDefault()));
				column.setHidden("1");
				column.setId("skill");
				break;
			case 7:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("130");
				column.setValue(messageSource.getMessage("admin.channel.label.system", null,Locale.getDefault()));
				column.setId("systemCode");
				break;
			case 8:
				column.setType("ro");
				column.setSort("int");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setWidth("130");
				column.setValue(messageSource.getMessage("admin.channel.label.channelNo", null,Locale.getDefault()));
				column.setId("chNum");
				break;
			case 9:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setId("recYn");
				column.setValue(messageSource.getMessage("admin.channel.label.usageRec", null,Locale.getDefault()));
	
				break;
			case 10:
				column.setSort("str");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setId("recApkey");
				column.setValue(messageSource.getMessage("admin.grid.apYn", null,Locale.getDefault()));
				break;
			case 11:
				column.setSort("int");
				column.setType("ro");
				column.setEditable("0");
				column.setWidth("135");
				column.setId("recSize");
				column.setValue(messageSource.getMessage("admin.grid.fileSize", null,Locale.getDefault()));
				break;
			}
			if(!nowAccessInfo.getModiYn().equals("Y")) {
				column.setEditable("0");
			}
	
			head.getColumnElement().add(column);
			column = null;
		}
	
		xmls.setHeadElement(head);
	
		ChannelInfo channelInfo = new ChannelInfo();
		SystemInfo systemInfo = new SystemInfo();
		OrganizationInfo organizationInfo = new OrganizationInfo();
	
		if(!StringUtil.isNull(request.getParameter("chNum"), true)) {
			channelInfo.setChNum(Integer.parseInt(request.getParameter("chNum")));
		};
		if(!StringUtil.isNull(request.getParameter("sysCode"), true)) {
			channelInfo.setSysCode(request.getParameter("sysCode"));
		};
		if(!StringUtil.isNull(request.getParameter("extNum"), true)) {
			channelInfo.setExtNum(request.getParameter("extNum"));
		};
		if(!StringUtil.isNull(request.getParameter("sDate"), true)) {
			channelInfo.setRecDate(request.getParameter("sDate").trim());
		};
		if(!StringUtil.isNull(request.getParameter("userId"), true)) {
			channelInfo.setUserId(request.getParameter("userId").trim());
		};
		if(!StringUtil.isNull(request.getParameter("userName"), true)) {
			channelInfo.setUserName(request.getParameter("userName").trim());
		};
		if(!StringUtil.isNull(request.getParameter("mgCode"), true)) {
			channelInfo.setMgCode(request.getParameter("mgCode").trim());
		};
		if(!StringUtil.isNull(request.getParameter("sgCode"), true)) {
			channelInfo.setSgCode(request.getParameter("sgCode").trim());
		};
//		System.out.println(request.getParameter("userName"));
		List<SystemInfo> systemInfoResult = systemInfoService.selectSystemInfo(systemInfo);
		List<OrganizationInfo> organizationMInfoResult = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
		List<OrganizationInfo> organizationSInfoResult = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
		List<ChannelInfo> channelMonitoringInfoResult = channelInfoService.selectChannelMonitoringInfo(channelInfo);
	
		Integer channelInfoResultTotal = channelMonitoringInfoResult.size();
	
		xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
		dhtmlXGridRowCell cellInfo = null;
	
		for(int i = 0; i < channelInfoResultTotal; i++) {
			ChannelInfo channelItem = channelMonitoringInfoResult.get(i);
			dhtmlXGridRow rowItem = new dhtmlXGridRow();
			rowItem.setId(String.valueOf(i+1));
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
	
			//번호
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(rowItem.getId());
			rowItem.getCellElements().add(cellInfo);
	
			// 내선 버노
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(channelItem.getExtNum().trim());
			cellInfo.setCellClass("inputFilter numberFilter");
			rowItem.getCellElements().add(cellInfo);
	
			// 사용자 사번
			cellInfo = new dhtmlXGridRowCell();
			if(channelItem.getUserId() == null) {
				cellInfo.setValue("");
			} else {
				cellInfo.setValue(channelItem.getUserId().trim());
			}
			rowItem.getCellElements().add(cellInfo);
	
			// 사용자 이름
			cellInfo = new dhtmlXGridRowCell();
			if(channelItem.getUserName() == null) {
				cellInfo.setValue("");
			} else {
				cellInfo.setValue(channelItem.getUserName().trim());
			}
			rowItem.getCellElements().add(cellInfo);
	
			// 중분류 (센터)
			cellInfo = new dhtmlXGridRowCell();
			if(channelItem.getMgCode() == null) {
				cellInfo.setValue("");
			} else {
				cellInfo.setValue(new FindOrganizationUtil().getOrganizationMName(channelItem.getMgCode().trim(), organizationMInfoResult));
			}
			rowItem.getCellElements().add(cellInfo);
	
			// 소분류 (실)
			cellInfo = new dhtmlXGridRowCell();
			if(channelItem.getSgCode() == null) {
				cellInfo.setValue("");
			} else {
				cellInfo.setValue(new FindOrganizationUtil().getOrganizationSName(channelItem.getSgCode().trim(), organizationSInfoResult));
			}
			rowItem.getCellElements().add(cellInfo);
	
			// 스킬
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("");
			rowItem.getCellElements().add(cellInfo);
	
			// 시스테므 코드
			cellInfo = new dhtmlXGridRowCell();
			if(new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult) == null ||
				new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult).length() < 4) {
				cellInfo.setValue("");
			} else {
				cellInfo.setValue(new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult).substring(0,3));
			}
			rowItem.getCellElements().add(cellInfo);
	
			// 채널 버노
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(channelItem.getChNum().toString());
			rowItem.getCellElements().add(cellInfo);
	
			// 녹취 사용 유무
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(channelItem.getRecYn().trim());
			rowItem.getCellElements().add(cellInfo);
	
			// ap 유무
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(channelItem.getRecApkey());
			if(channelItem.getRecSize() != null) {
				cellInfo.setValue("Y");
			} else {
				cellInfo.setValue("N");
			}
			rowItem.getCellElements().add(cellInfo);
	
			// 파일 크기 유무
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getRecSize());
				rowItem.getCellElements().add(cellInfo);
	
				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}

	// 채널별 모니터링 그리드 (내선별)
	@RequestMapping(value="/channelMonitoringExt_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml channelMonitoringExt_list(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);	
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.channelMonitoring");

		if(userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.channel.title.";
			for( int j = 0; j < 13; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("100");

				switch(j) {
				case 0:
					column.setType("ro");
					column.setSort("int");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("85");
					column.setValue(messageSource.getMessage("admin.grid.num", null,Locale.getDefault()));
					break;
				case 1:
					column.setSort("int");
					column.setWidth("130");
					column.setValue(messageSource.getMessage("header.popup.modifyUser.label.ext", null,Locale.getDefault()));
					column.setId("extNum");
					column.setType("ro");
					break;
				case 2:
					column.setSort("int");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setValue(messageSource.getMessage("header.popup.modifyUser.label.userEmp", null,Locale.getDefault()));
					column.setId("userId");
					break;
				case 3:
					column.setSort("str");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setValue(messageSource.getMessage("header.popup.modifyUser.label.userName", null,Locale.getDefault()));
					column.setId("userName");
					break;
				case 4:
					column.setSort("str");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setValue(messageSource.getMessage("admin.grid.center", null,Locale.getDefault()));
					column.setId("mgCode");
					break;
				case 5:
					column.setSort("str");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setValue(messageSource.getMessage("admin.grid.sg", null,Locale.getDefault()));
					column.setId("sgCode");
					break;
				case 6:
					column.setSort("str");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setValue(messageSource.getMessage("admin.grid.skill", null,Locale.getDefault()));
					column.setHidden("1");
					column.setId("skill");
					break;
				case 7:
					column.setSort("str");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("130");
					column.setValue(messageSource.getMessage("admin.channel.label.system", null,Locale.getDefault()));
					column.setId("systemCode");
					break;
				case 8:
					column.setType("ro");
					column.setSort("int");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("130");
					column.setValue(messageSource.getMessage("admin.channel.label.channelNo", null,Locale.getDefault()));
					column.setId("chNum");
					break;
				case 9:
					column.setSort("str");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setId("recYn");
					column.setValue(messageSource.getMessage("admin.channel.label.usageRec", null,Locale.getDefault()));
					break;
				case 10:
					column.setSort("int");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setId("recApkeySize");
					column.setValue(messageSource.getMessage("admin.grid.recApkeySize", null,Locale.getDefault()));
					break;
				case 11:
					column.setSort("int");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setId("recCount");
					column.setValue(messageSource.getMessage("admin.grid.recCount", null,Locale.getDefault()));
					break;
				case 12:
					column.setSort("int");
					column.setType("ro");
					column.setEditable("0");
					column.setWidth("136");
					column.setId("recSum");
					column.setValue(messageSource.getMessage("admin.grid.fileSize", null,Locale.getDefault()));
					break;
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

			xmls.setHeadElement(head);

			ChannelInfo channelInfo = new ChannelInfo();
			SystemInfo systemInfo = new SystemInfo();
			OrganizationInfo organizationInfo = new OrganizationInfo();

			if(!StringUtil.isNull(request.getParameter("chNum"), true)) {
				channelInfo.setChNum(Integer.parseInt(request.getParameter("chNum")));
			};
			if(!StringUtil.isNull(request.getParameter("sysCode"), true)) {
				channelInfo.setSysCode(request.getParameter("sysCode"));
			};
			if(!StringUtil.isNull(request.getParameter("extNum"), true)) {
				channelInfo.setExtNum(request.getParameter("extNum"));
			};
			if(!StringUtil.isNull(request.getParameter("sDate"), true)) {
				channelInfo.setRecDate(request.getParameter("sDate").trim());
			};
			if(!StringUtil.isNull(request.getParameter("userId"), true)) {
				channelInfo.setUserId(request.getParameter("userId").trim());
			};
			if(!StringUtil.isNull(request.getParameter("userName"), true)) {
				channelInfo.setUserName(request.getParameter("userName").trim());
			};
			if(!StringUtil.isNull(request.getParameter("mgCode"), true)) {
				channelInfo.setMgCode(request.getParameter("mgCode").trim());
			};
			if(!StringUtil.isNull(request.getParameter("sgCode"), true)) {
				channelInfo.setSgCode(request.getParameter("sgCode").trim());
			};
			List<SystemInfo> systemInfoResult = systemInfoService.selectSystemInfo(systemInfo);
			List<OrganizationInfo> organizationMInfoResult = organizationInfoService.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSInfoResult = organizationInfoService.selectOrganizationSgInfo(organizationInfo);
			List<ChannelInfo> channelMonitoringInfoResult = channelInfoService.selectChannelMonitoringExtInfo(channelInfo);

			Integer channelInfoResultTotal = channelMonitoringInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < channelInfoResultTotal; i++) {
				ChannelInfo channelItem = channelMonitoringInfoResult.get(i);
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(rowItem.getId());
				rowItem.getCellElements().add(cellInfo);

				// 내선 버노
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getExtNum().trim());
				cellInfo.setCellClass("inputFilter numberFilter");
				rowItem.getCellElements().add(cellInfo);

				// 사용자 사번
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getUserId() == null) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(channelItem.getUserId().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 사용자 이름
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getUserName() == null) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(channelItem.getUserName().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류 (센터)
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getMgCode() == null) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(new FindOrganizationUtil().getOrganizationMName(channelItem.getMgCode().trim(), organizationMInfoResult));
				}
				rowItem.getCellElements().add(cellInfo);

				// 소분류 (실)
				cellInfo = new dhtmlXGridRowCell();
				if(channelItem.getSgCode() == null) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(new FindOrganizationUtil().getOrganizationSName(channelItem.getSgCode().trim(), organizationSInfoResult));
				}
				rowItem.getCellElements().add(cellInfo);

				// 스킬
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 시스테므 코드
				cellInfo = new dhtmlXGridRowCell();
				if(new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult) == null ||
					new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult).length() < 4) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(new FindOrganizationUtil().getSysCodeName(channelItem.getSysCode().trim(), systemInfoResult).substring(0,3));
				}
				rowItem.getCellElements().add(cellInfo);

				// 채널 버노
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getChNum().toString());
				rowItem.getCellElements().add(cellInfo);

				// 녹취 사용 유무
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getRecYn().trim());
				rowItem.getCellElements().add(cellInfo);

				// AP 등록개수

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getRecApkeySize());
				rowItem.getCellElements().add(cellInfo);

				// 녹취 파일 개수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getRecCount());
				rowItem.getCellElements().add(cellInfo);

				// 총 파일크기
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(channelItem.getRecSum());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		}
		return xmls;
	}
	// 시스템 관리 그리드
	@RequestMapping(value = "/system_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml channelManageGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		EtcConfigInfo etcConfig = new EtcConfigInfo();
		etcConfig.setGroupKey("SYSTEM");
		etcConfig.setConfigKey("SWITCH_SYSTEM_USE");
		List<EtcConfigInfo> switchSystemUse = null;
		String switchSystemUseYn = "N";
		try {
			switchSystemUse = etcConfigInfoService.selectEtcConfigInfo(etcConfig);
			if (switchSystemUse.size() > 0) {
				switchSystemUseYn = switchSystemUse.get(0).getConfigValue();
			}
		} catch(Exception e) {
			etcConfig.setConfigValue("N");
			etcConfigInfoService.insertEtcConfigInfo(etcConfig);
		}
		
		etcConfig = new EtcConfigInfo();
		etcConfig.setGroupKey("SYSTEM");
		etcConfig.setConfigKey("DELETE_FILE_SIZE");
		List<EtcConfigInfo> deleteFileSize= null;
		String deleteFileSizeYn = "N";
		try {
			deleteFileSize = etcConfigInfoService.selectEtcConfigInfo(etcConfig);
			if (deleteFileSize.size() > 0) {
				deleteFileSizeYn = deleteFileSize.get(0).getConfigValue();
			}
		} catch(Exception e) {
			etcConfig.setConfigValue("N");
			etcConfigInfoService.insertEtcConfigInfo(etcConfig);
		}
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.server");

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.system.title.";
			for( int j = 0; j < 13; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.label.systemId", null,Locale.getDefault()));
					break;
				case 2:
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.label.systemName", null,Locale.getDefault()));
					break;
				case 3:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.label.systemIp", null,Locale.getDefault()));
					break;
				case 4:
					column.setWidth("150");
					column.setValue("PBX ID");
					column.setHidden("1");
					break;
				case 5:
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.grid.saveLocation", null,Locale.getDefault()));
					column.setHidden("1");
					break;
				case 6:
					column.setWidth("100");
					column.setValue("수동 전환");
					column.setId("switch_system");
					if ("N".equals(switchSystemUseYn)) {
						column.setHidden("1");
					}
					break;
				case 7:
					column.setWidth("100");
					column.setValue("전환 시스템 ID");
					column.setHidden("1");
					break;
				case 8:
					column.setWidth("100");
					column.setValue("전환 시스템");
					if ("N".equals(switchSystemUseYn)) {
						column.setHidden("1");
					}
					break;
				case 9:
					column.setWidth("100");
					column.setValue("삭제 사용 여부");
					if ("N".equals(deleteFileSizeYn)) {
						column.setHidden("1");
					}
					break;
				case 10:
					column.setWidth("100");
					column.setValue("삭제 용량 체크");
					if ("N".equals(deleteFileSizeYn)) {
						column.setHidden("1");
					}
					break;
				case 11:
					column.setWidth("100");
					column.setValue("삭제 경로");
					if ("N".equals(deleteFileSizeYn)) {
						column.setHidden("1");
					}
					break;
					
				case 12:
					column.setWidth("*");
					column.setValue("");
					break;
				
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
			headCall.getParamElement().add("#rspan,<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,#text_filter,#text_filter,#text_filter,#text_filter");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			SystemInfo systemInfo = new SystemInfo();
			if ("Y".equals(switchSystemUseYn)) {
				systemInfo.setSysDeleteYN("Y");
			}
			
			List<SystemInfo> systemInfoResult = systemInfoService.selectSystemInfo(systemInfo);
			Integer systemInfoResultTotal = systemInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < systemInfoResultTotal; i++) {
				SystemInfo systemItem = systemInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSysId());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSysName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSysIp());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getPbxId());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getStoragePath());
				rowItem.getCellElements().add(cellInfo);

				// 수동 전환 버튼
				cellInfo = new dhtmlXGridRowCell();
				if ("Y".equals(systemItem.getSwitchSysYn())){
					cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='switchSystemBtn' onClick='returningSwitchSystemPop(\""+String.valueOf(i+1)+"\");'>복원</button>");
				} else {
					cellInfo.setValue("<button class='ui_btn_white ui_sub_btn_flat' id='returningSystemBtn' onClick='openSwitchSystemPop(\""+String.valueOf(i+1)+"\");'>전환</button>");
				}
				rowItem.getCellElements().add(cellInfo);
				
				// 전환 시스템 ID
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSwitchSysCode());
				rowItem.getCellElements().add(cellInfo);
				
				// 전환 시스템 이름
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSwitchSysName());
				rowItem.getCellElements().add(cellInfo);

				// 삭제 사용 여부
				// rs_sys_file_delete 테이블에 데이터가 있으면
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSysDeleteYN());
				rowItem.getCellElements().add(cellInfo);

				// 용량 체크
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSysDeleteSize());
				rowItem.getCellElements().add(cellInfo);
				
				// 체크 경로
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(systemItem.getSysDeletePath());
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	// 교환기 연동 정보 목록
	@RequestMapping(value="/pbx_list.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml pbx_list(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.switchboard");

		if(userInfo != null) {
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			String titleBaseName = "management.pbx.title.";
			for( int j = 0; j < 8; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
					column.setSort("str");
				}

				column.setFiltering("1");
				column.setEditable("1");
				column.setCache("1");
				column.setAlign("center");
				column.setWidth("200");


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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.label.pbxID", null,Locale.getDefault()));
					break;
				case 2:
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.label.pbxName", null,Locale.getDefault()));
					break;
				case 3:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.label.pbxIp", null,Locale.getDefault()));
					break;
				case 4:
					column.setWidth("80");
					column.setValue("라이선스");
					// 라이선스 의미 없으므로 숨겨~!
					column.setHidden("1");
					break;
				case 5:
					column.setWidth("100");
					column.setValue("SIP ID");
					break;
				case 6:
					column.setWidth("100");
					column.setType("Pasw");
					column.setValue("SIP PW");
					break;
				case 7:
					column.setWidth("*");
					column.setValue("");
					break;
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
			headCall.getParamElement().add("#rspan,<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,#text_filter,#text_filter,,#text_filter,");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			PbxInfo pbxInfo = new PbxInfo();

			List<PbxInfo> pbxInfoResult = pbxInfoService.selectPbxInfo(pbxInfo);
			Integer pbxInfoResultTotal = pbxInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < pbxInfoResultTotal; i++) {
				PbxInfo pbxItem = pbxInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(pbxItem.getrPbxId());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(pbxItem.getrPbxName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(pbxItem.getrPbxIp());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(pbxItem.getrPbxLicense() != null && !pbxItem.getrPbxLicense().trim().isEmpty()) {
					cellInfo.setValue(pbxItem.getrPbxLicense());
				} else {
					cellInfo.setValue("0");
				}
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(pbxItem.getrPbxSipId() != null && !pbxItem.getrPbxSipId().trim().isEmpty()) {
					cellInfo.setValue(pbxItem.getrPbxSipId());
				} else {
					cellInfo.setValue("");
				}
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				if(pbxItem.getrPbxSipPassword() != null && !pbxItem.getrPbxSipPassword().trim().isEmpty()) {
					cellInfo.setValue(pbxItem.getrPbxSipPassword());
				} else {
					cellInfo.setValue("");
				}
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}

		return xmls;
	}


	// etc config 리스트
	@RequestMapping(value="/etc_config.xml", method=RequestMethod.GET, produces="application/xml")
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

			for( int j = 0; j < 5; j++ ) {
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
			head.setAfterElement(new dhtmlXGridHeadAfterInit());
			dhtmlXGridHeadAfterInit afterInit = new dhtmlXGridHeadAfterInit();

			afterInit.setCallElement(new ArrayList<dhtmlXGridHeadCall>());

			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			EtcConfigInfo etcConfig = new EtcConfigInfo();

			if (!StringUtil.isNull(request.getParameter("groupKey"),true))
				etcConfig.setGroupKey(request.getParameter("groupKey"));
			if (!StringUtil.isNull(request.getParameter("configKey"),true))
				etcConfig.setConfigKey(request.getParameter("configKey"));
			if (!StringUtil.isNull(request.getParameter("configValue"),true))
				etcConfig.setConfigValue(request.getParameter("configValue"));

			List<EtcConfigInfo> etcConfigReuslt = etcConfigInfoService.selectEtcConfigInfo(etcConfig);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < etcConfigReuslt.size(); i++) {
				EtcConfigInfo item = etcConfigReuslt.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getGroupKey());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getConfigKey());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getConfigValue());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getDesc());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(item.getConfigOption());
				rowItem.getCellElements().add(cellInfo);
				
				if(!StringUtil.isNull(item.getDesc(),true)) {
					xmls.getRowElements().add(rowItem);
				}

				rowItem = null;
			}
		}

		return xmls;
	}
	
	@RequestMapping(value="/prefixList.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml prefixList(HttpServletRequest request, HttpServletResponse response, Locale locale) {		
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

			for( int j = 0; j < 2; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setEditable("0");
				column.setCache("0");
				column.setAlign("center");

				switch(j) {
					case 0:
						column.setType("ch");
						column.setFiltering("0");
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("50");
						column.setValue("<div id='allcheck' style=\"cursor: pointer; margin: 0px 0 0;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' width='18' height='18'/></div>");
					
						break;
					case 1:
						column.setWidth("200");
						column.setValue("NUM");
						break;
				}
				head.getColumnElement().add(column);
				column = null;	
			}

			xmls.setHeadElement(head);
			
			EtcConfigInfo etcConfig = new EtcConfigInfo();

			etcConfig.setGroupKey("Prefix");
			etcConfig.setConfigKey("Prefix");

			
			EtcConfigInfo prefixReuslt = etcConfigInfoService.selectOptionInfo(etcConfig);
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			if (prefixReuslt != null && !prefixReuslt.getConfigValue().equals("")) {
				String[] arrPrefixValue = prefixReuslt.getConfigValue().split(",");	// 구분자 ","로 저장된 값을 분해
						
				for(int i = 0; i < arrPrefixValue.length; i++) {
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);				
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(arrPrefixValue[i]);
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
	
					rowItem = null;
				}
			}
		}
		return xmls;
	}
	
	// 20200417 jbs 자번호 관리 그리드
	@RequestMapping(value = "/subNumber_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml subNumberManageGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.subNumber");
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.subNumber.title.";
			for( int j = 0; j < 6; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.subNumber.label.telNo", null,Locale.getDefault()));
					break;
				case 2:
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.subNumber.label.nickName", null,Locale.getDefault()));
					break;
				case 3:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN3");
					column.setWidth("200");
					column.setId("use");
					column.setHidden("1");
					column.setValue(messageSource.getMessage("admin.subNumber.label.use", null,Locale.getDefault()));
					break;
				case 4:
					column.setWidth("150");
					column.setValue("IDX");
					column.setHidden("1");
					break;
				case 5:
					column.setWidth("*");
					column.setValue("");
					break;
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
			headCall.getParamElement().add("#rspan,<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,#text_filter,#select_filter");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			SubNumberInfo subNumberInfo = new SubNumberInfo();

			List<SubNumberInfo> subNumberInfoResult = subNumberInfoService.selectSubNumberInfo(subNumberInfo);
			Integer subNumberInfoResultTotal = subNumberInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < subNumberInfoResultTotal; i++) {
				SubNumberInfo subNumberItem = subNumberInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(subNumberItem.getTelNo());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(subNumberItem.getNickName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(subNumberItem.getUse());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(subNumberItem.getIdx());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	
	// 20200724 justin 로그 관리
	@RequestMapping(value = "/logo_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml logoManageGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.subNumber");
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.subNumber.title.";
			for( int j = 0; j < 4; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("str");
				column.setCache("1");
				column.setAlign("center");

				switch(j) {
				case 0:
					column.setId("logoType");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.subNumber.label.logoType", null,Locale.getDefault()));
					break;
				case 1:
					column.setId("logoChangeUse");
					column.setWidth("200");
					column.setValue(messageSource.getMessage("admin.subNumber.label.logoChangeUse", null,Locale.getDefault()));
					break;
				case 2:
					column.setId("logoChangeBtn");
					column.setHidden("1");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.subNumber.label.logoChangeUse", null,Locale.getDefault()));
					break;
				case 3:
					column.setId("logoPath");
					column.setWidth("*");
					column.setValue(messageSource.getMessage("admin.subNumber.label.logoPath", null,Locale.getDefault()));
					break;
				}
				head.getColumnElement().add(column);
				column = null;

			}
			xmls.setHeadElement(head);

			LogoVO logoVO = new LogoVO();
			List<LogoVO> logoVOInfoResult = null;
			try {
				logoVOInfoResult = subNumberInfoService.selectLogoInfo(logoVO);
			} catch(Exception e) {
				logger.error("error",e);
			}
			Integer logoInfoResultTotal = logoVOInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < logoInfoResultTotal; i++) {
				LogoVO logoItem = logoVOInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(logoItem.getLogoType());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(logoItem.getLogoChangeUse());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("-");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				if ("N".equals(logoItem.getLogoChangeUse())) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(logoItem.getLogoPath());
				}
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	
	// 20200818 justin 패킷 에러 세팅
	@RequestMapping(value = "/Packet_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml packetManageGrid(HttpServletRequest request, HttpServletResponse response){

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.subNumber.title.";
			for( int j = 0; j < 5; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				
				column.setAlign("center");				
				
				switch(j) {
				
				case 0:
					column.setType("ch");	
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("50");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;				
				case 1:
					column.setSort("str");
					column.setCache("1");					
					column.setType("ro");				
					column.setId("custCode");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.systemOption.label.custCode", null,Locale.getDefault()));
					break;
				case 2:
					column.setSort("str");
					column.setCache("1");
					column.setAlign("center");
					column.setType("ro");
					column.setId("phoneSetting");
					column.setWidth("200");
					column.setValue(messageSource.getMessage("admin.systemOption.label.phoneSetting", null,Locale.getDefault()));
					break;
				case 3:
					column.setSort("str");
					column.setCache("1");
					column.setAlign("center");
					column.setType("ro");
					column.setId("returnMsg");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.systemOption.label.returnMsg", null,Locale.getDefault()));
					break;
				case 4:
					column.setSort("str");
					column.setCache("1");
					column.setAlign("center");
					column.setType("ro");
					column.setId("returnUrl");
					column.setWidth("*");
					column.setValue(messageSource.getMessage("admin.systemOption.label.returnUrl", null,Locale.getDefault()));
					break;
				}
				head.getColumnElement().add(column);
				column = null;

			}
			xmls.setHeadElement(head);

			PacketVO packetVO = new PacketVO();
			List<PacketVO> packetVOVOInfoResult = null;
			try {
				packetVOVOInfoResult = subNumberInfoService.selectPacketSettingInfo(packetVO);
			} catch(Exception e) {
				logger.error("error",e);
			}
			Integer logoInfoResultTotal = packetVOVOInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < logoInfoResultTotal; i++) {
				PacketVO packetVOReurn = packetVOVOInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(packetVOReurn.getCustCode());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(packetVOReurn.getPhoneSetting());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(packetVOReurn.getMsg());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(packetVOReurn.getReturnUrl());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	
	
	// 20200420 jbs 전화번호 맵핑 그리드
	@RequestMapping(value = "/phoneMapping_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml phoneMappingGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.phoneMapping");
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			//String titleBaseName = "management.phoneMapping.title.";
			for( int j = 0; j < 8; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.phoneMapping.label.custPhone", null,Locale.getDefault()));
					break;
				case 2:
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.phoneMapping.label.custNickName", null,Locale.getDefault()));
					break;
				case 3:
					column.setType("combo");
					column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=YN4");
					column.setWidth("200");
					column.setId("useNickName");
					column.setHidden("1");
					column.setValue(messageSource.getMessage("admin.phoneMapping.label.useNickName", null,Locale.getDefault()));
					break;
				case 4:
					column.setWidth("150");
					column.setValue("CUSTPHONE");
					column.setHidden("1");
					break;
				case 5:
					column.setWidth("150");
					column.setValue("PROCTYPE");
					column.setHidden("1");
					break;
				case 6:
					column.setWidth("150");
					column.setValue("PROCPOSITION");
					column.setHidden("1");
					break;
				case 7:
					column.setWidth("*");
					column.setValue("");
					break;
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
			headCall.getParamElement().add("#rspan,<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,#text_filter,#select_filter");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			PhoneMappingInfo phoneMappingInfo = new PhoneMappingInfo();

			List<PhoneMappingInfo> phoneMappingInfoResult = phoneMappingInfoService.selectPhoneMappingInfo(phoneMappingInfo);
			Integer phoneMappingInfoResultTotal = phoneMappingInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < phoneMappingInfoResultTotal; i++) {
				PhoneMappingInfo phoneMappingItem = phoneMappingInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(phoneMappingItem.getCustPhone());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(phoneMappingItem.getCustNickName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(phoneMappingItem.getUseNickName());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(phoneMappingItem.getCustPhone());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(phoneMappingItem.getProcType());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(phoneMappingItem.getProcPosition());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	
	// 20200616 jbs 내부망 관리 그리드
	@RequestMapping(value = "/publicIpInfo_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml intranetInfoGrid(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.publicIp");
		if (userInfo != null) {
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			//String titleBaseName = "management.publicIpInfo.title.";
			for( int j = 0; j < 2; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				
				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("300");
					column.setValue(messageSource.getMessage("admin.publicIp.label.rIntranetIp", null,Locale.getDefault()));
					break;
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
			headCall.getParamElement().add("#rspan,#text_filter");
			
			afterInit.getCallElement().add(headCall);
			
			head.setAfterElement(afterInit);
			
			xmls.setHeadElement(head);
			
			PublicIpInfo publicIpInfo = new PublicIpInfo();
			
			List<PublicIpInfo> publicIpInfoResult = publicIpInfoService.selectPublicIpInfo(publicIpInfo);
			Integer publicIpInfoResultTotal = publicIpInfoResult.size();
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			for(int i = 0; i < publicIpInfoResultTotal; i++) {
				PublicIpInfo publicIpInfoItem = publicIpInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(publicIpInfoItem.getrPublicIp());
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				
				rowItem = null;
			}
		}
		return xmls;
	}
	
	// db info grid
	@RequestMapping(value = "/dbInfo_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml dbInfoGrid(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.dbInfo");
		if (userInfo != null) {
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			//String titleBaseName = "management.phoneMapping.title.";
			for( int j = 0; j < 6; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				
				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("300");
					column.setValue("dbName"/*messageSource.getMessage("admin.phoneMapping.label.custPhone", null,Locale.getDefault())*/);
					break;
				case 2:
					column.setWidth("300");
					column.setValue("dbDriver"/*messageSource.getMessage("admin.phoneMapping.label.custNickName", null,Locale.getDefault())*/);
					break;
				case 3:
					column.setWidth("300");
					column.setValue("dbUrl"/*messageSource.getMessage("admin.phoneMapping.label.useNickName", null,Locale.getDefault())*/);
					break;
				case 4:
					column.setWidth("300");
					column.setValue("dbUser"/*messageSource.getMessage("admin.phoneMapping.label.useNickName", null,Locale.getDefault())*/);
					break;
				case 5:
					column.setWidth("300");
					column.setValue("dbPassword"/*messageSource.getMessage("admin.phoneMapping.label.useNickName", null,Locale.getDefault())*/);
					break;
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
			headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
			
			afterInit.getCallElement().add(headCall);
			
			head.setAfterElement(afterInit);
			
			xmls.setHeadElement(head);
			
			DBInfo dbInfo = new DBInfo();
			
			List<DBInfo> dbInfoResult = dbInfoService.selectDBInfo(dbInfo);
			Integer dbInfoResultTotal = dbInfoResult.size();
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			
			for(int i = 0; i < dbInfoResultTotal; i++) {
				DBInfo dbInfoItem = dbInfoResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(dbInfoItem.getDbName());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(dbInfoItem.getDbDriver());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(dbInfoItem.getDbUrl());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(dbInfoItem.getDbUser());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(dbInfoItem.getDbPassword());
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);
				
				rowItem = null;
			}
		}
		return xmls;
	}
	
	// dbManage grid
	@RequestMapping(value = "/dbManage_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml dbManageGrid(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		
		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
		if (userInfo != null) {
			if("db".equals(request.getParameter("tab"))) {
				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());
				
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				
				for( int j = 0; j < 8; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					
					if(j == 0 ) {
						column.setType("ch");
					} else if(j == 1) {
						column.setType("ro");
						column.setSort("str");
					} else {
						column.setType("ed");
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
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.dbManage.label.dbName", null,Locale.getDefault()));
						break;
					case 2:
						column.setWidth("150");
						column.setValue(messageSource.getMessage("admin.dbManage.label.dbServer", null,Locale.getDefault()));
						break;
					case 3:
						column.setWidth("250");
						column.setValue(messageSource.getMessage("admin.dbManage.label.url", null,Locale.getDefault()));
						break;
					case 4:
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.dbManage.label.id", null,Locale.getDefault()));
						break;
					case 5:
						column.setWidth("150");
						column.setValue(messageSource.getMessage("admin.dbManage.label.pw", null,Locale.getDefault()));
						break;
					case 6:
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.dbManage.label.timeout", null,Locale.getDefault()));
						break;
					case 7:
						column.setWidth("150");
						/*column.setValue(messageSource.getMessage("admin.dbManage.label.dbConnectionTest", null,Locale.getDefault()));*/
						break;
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
				headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,");
				
				afterInit.getCallElement().add(headCall);
				
				head.setAfterElement(afterInit);
				
				xmls.setHeadElement(head);
				
				DBManage dbManage = new DBManage();
				
				List<DBManage> dbManageResult = dbManageService.selectDBManage(dbManage);
				Integer dbManageResultTotal = dbManageResult.size();
				
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				
				for(int i = 0; i < dbManageResultTotal; i++) {
					DBManage dbManageItem = dbManageResult.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(dbManageItem.getDbName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(dbManageItem.getDbServer());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(dbManageItem.getUrl());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(dbManageItem.getId());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(dbManageItem.getPw());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(dbManageItem.getTimeout());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class='ui_main_btn_flat' onClick='dbConnectionTest(\""+String.valueOf(i+1)+"\")'>"+ messageSource.getMessage("admin.dbManage.label.dbConnectionTest", null,Locale.getDefault()) +"</button>");
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					
					rowItem = null;
				}
			}else if("query".equals(request.getParameter("tab"))) {
				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());
				
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				
				for( int j = 0; j < 3; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					
					if(j == 0 ) {
						column.setType("ch");
					} else if(j == 1) {
						column.setType("ro");
						column.setSort("str");
					} else {
						column.setType("ed");
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
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("150");
						column.setValue(messageSource.getMessage("admin.dbManage.label.sqlName", null,Locale.getDefault()));
						break;
					case 2:
						column.setWidth("600");
						column.setValue(messageSource.getMessage("admin.dbManage.label.sql", null,Locale.getDefault()));
						break;
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
				headCall.getParamElement().add("#rspan,#text_filter,#text_filter");
				
				afterInit.getCallElement().add(headCall);
				
				head.setAfterElement(afterInit);
				
				xmls.setHeadElement(head);
				
				SQLManage sqlManage = new SQLManage();
				
				List<SQLManage> sqlManageResult = sqlManageService.selectSQLManage(sqlManage);
				Integer sqlManageResultTotal = sqlManageResult.size();
				
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				
				for(int i = 0; i < sqlManageResultTotal; i++) {
					SQLManage sqlManageItem = sqlManageResult.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(sqlManageItem.getSqlName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(sqlManageItem.getSql());
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					
					rowItem = null;
				}
			}else if("job".equals(request.getParameter("tab"))) {
				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());
				
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				
				for( int j = 0; j < 4; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					
					if(j == 0 ) {
						column.setType("ch");
					} else if(j == 1) {
						column.setType("ro");
						column.setSort("str");
					} else {
						column.setType("ed");
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
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("150");
						column.setValue(messageSource.getMessage("admin.dbManage.label.jobName", null,Locale.getDefault()));
						break;
					case 2:
						column.setWidth("350");
						column.setValue(messageSource.getMessage("admin.dbManage.label.dbName", null,Locale.getDefault()));
						break;
					case 3:
						column.setWidth("350");
						column.setValue(messageSource.getMessage("admin.dbManage.label.sqlName", null,Locale.getDefault()));
						break;
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
				headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter");
				
				afterInit.getCallElement().add(headCall);
				
				head.setAfterElement(afterInit);
				
				xmls.setHeadElement(head);
				
				JobManage jobManage = new JobManage();
				
				List<JobManage> jobManageResult = jobManageService.selectJobManage(jobManage);
				Integer jobManageResultTotal = jobManageResult.size();
				
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				
				for(int i = 0; i < jobManageResultTotal; i++) {
					JobManage jobManageItem = jobManageResult.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(jobManageItem.getJobName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(jobManageItem.getDbName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(jobManageItem.getSqlName());
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					
					rowItem = null;
				}
			}else if("execute".equals(request.getParameter("tab"))) {
				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());
				
				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
				
				for( int j = 0; j < 13; j++ ) {
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
					
					if(j == 0 ) {
						column.setType("ch");
					} else if(j == 1) {
						column.setType("ro");
						column.setSort("str");
					} else {
						column.setType("ed");
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
						column.setEditable("0");
						column.setCache("0");
						column.setWidth("150");
						column.setValue(messageSource.getMessage("admin.dbManage.label.executeName", null,Locale.getDefault()));
						break;
					case 2:
						column.setWidth("150");
						column.setValue(messageSource.getMessage("admin.dbManage.label.jobName", null,Locale.getDefault()));
						break;
					case 3:
						column.setType("combo");
						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=schedule");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rSchedulerSelect", null,Locale.getDefault()));
						break;
					case 4:
						column.setType("combo");
						column.setSource(request.getContextPath() + "/opt/combo_option.xml?comboType=day");
						column.setWidth("100");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rSchedulerWeek", null,Locale.getDefault()));
						break;
					case 5:
						column.setWidth("50");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rSchedulerDay", null,Locale.getDefault()));
						break;
					case 6:
						column.setWidth("50");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rSchedulerHour", null,Locale.getDefault()));
						break;
					case 7:
						column.setWidth("120");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rExecuteDate", null,Locale.getDefault()));
						break;
					case 8:
						column.setWidth("120");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rExecuteTime", null,Locale.getDefault()));
						break;
					case 9:
						column.setWidth("120");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rCompleteDate", null,Locale.getDefault()));
						break;
					case 10:
						column.setWidth("120");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rCompleteTime", null,Locale.getDefault()));
						break;
					case 11:
						column.setWidth("120");
						column.setValue(messageSource.getMessage("admin.dbManage.label.rErrorMessage", null,Locale.getDefault()));
						column.setHidden("1");
						break;
					case 12:
						column.setWidth("120");
						/*column.setValue(messageSource.getMessage("admin.dbManage.label.executeQuery", null,Locale.getDefault()));*/
						break;
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
				headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,,,,,,");
				
				afterInit.getCallElement().add(headCall);
				
				head.setAfterElement(afterInit);
				
				xmls.setHeadElement(head);
				
				ExecuteManage executeManage = new ExecuteManage();
				
				List<ExecuteManage> executeManageResult = executeManageService.selectExecuteManage(executeManage);
				Integer executeManageResultTotal = executeManageResult.size();
				
				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				
				for(int i = 0; i < executeManageResultTotal; i++) {
					ExecuteManage executeManageItem = executeManageResult.get(i);
					
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("0");
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getExecuteName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getJobName());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrSchedulerSelect());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrSchedulerWeek());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrSchedulerDay());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrSchedulerHour());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrExecuteDate());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrExecuteTime());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrCompleteDate());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrCompleteTime());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(executeManageItem.getrErrorMessage());
					rowItem.getCellElements().add(cellInfo);
					
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class='ui_main_btn_flat' onClick='excuteQuery(\""+String.valueOf(i+1)+"\")'>"+messageSource.getMessage("admin.dbManage.label.executeQuery", null,Locale.getDefault())+"</button>");
					rowItem.getCellElements().add(cellInfo);
					
					xmls.getRowElements().add(rowItem);
					
					rowItem = null;
				}
			}
		}
		return xmls;
	}
	
	@RequestMapping(value = "/userDBInterface_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml userDBInterfaceGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.userDBInterface");
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for( int j = 0; j < 14; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setWidth("10");
					column.setValue("seq");
					column.setHidden("1");
					break;
				case 2:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rDbIp", null,Locale.getDefault()));
					break;
				case 3:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rDbUser", null,Locale.getDefault()));
					break;
				case 4:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rDbPwd", null,Locale.getDefault()));
					break;
				case 5:
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rDbPort", null,Locale.getDefault()));
					break;
				case 6:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rDbName", null,Locale.getDefault()));
					break;
				case 7:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rTeamCode", null,Locale.getDefault()));
					break;
				case 8:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rTeamName", null,Locale.getDefault()));
					break;
				case 9:
					column.setWidth("80");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rHh", null,Locale.getDefault()));
					break;
				case 10:
					column.setWidth("80");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rCompleteDate", null,Locale.getDefault()));
					break;
				case 11:
					column.setWidth("80");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rCompleteTime", null,Locale.getDefault()));
					break;
				case 12:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.userDBInterface.label.rQuickExcution", null,Locale.getDefault()));
					break;
				case 13:
					column.setWidth("*");
					column.setValue("");
					break;
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
			headCall.getParamElement().add("#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,,,");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			UserDBInterface userDBInterface = new UserDBInterface();

			List<UserDBInterface> userDBInterfaceResult = userDBInterfaceService.selectUserDBInterface(userDBInterface);
			Integer userDBInterfaceResultTotal = userDBInterfaceResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < userDBInterfaceResultTotal; i++) {
				UserDBInterface userDBInterfaceItem = userDBInterfaceResult.get(i);
				
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getSeq());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrDbIp());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrDbUser());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrDbPwd());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrDbPort());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrDbName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrTeamCode());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrTeamName());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrHh());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrCompleteDate());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(userDBInterfaceItem.getrCompleteTime());
				rowItem.getCellElements().add(cellInfo);
				
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='ui_main_btn_flat' onClick='quickExcution(\""+String.valueOf(i+1)+"\")'>"+messageSource.getMessage("admin.userDBInterface.button.rQuickExcution", null,Locale.getDefault())+"</button>");
				rowItem.getCellElements().add(cellInfo);
				
				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
	
	// 시스템 관리 그리드
	@RequestMapping(value = "/stt_server_list.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml sttServerManageGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.sttServer");

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for( int j = 0; j < 5; j++ ) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				if(j == 0 ) {
					column.setType("ch");
				} else if(j == 1) {
					column.setType("ro");
					column.setSort("str");
				} else {
					column.setType("ed");
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
					column.setEditable("0");
					column.setCache("0");
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.label.systemId", null,Locale.getDefault()));
					break;
				case 2:
					column.setWidth("100");
					column.setValue(messageSource.getMessage("admin.label.systemName", null,Locale.getDefault()));
					break;
				case 3:
					column.setWidth("150");
					column.setValue(messageSource.getMessage("admin.label.systemIp", null,Locale.getDefault()));
					break;
				case 4:
					column.setWidth("*");
					column.setValue("");
					break;
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
			headCall.getParamElement().add("#rspan,<div><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/icon_filter.svg' width='18' height='18' /></div>,#text_filter,#text_filter");

			afterInit.getCallElement().add(headCall);

			head.setAfterElement(afterInit);

			xmls.setHeadElement(head);

			SttServerInfo sttServerInfo = new SttServerInfo();

			List<SttServerInfo> sttServerInfoResult = sttServerInfoService.selectSttServerInfo(sttServerInfo);
			Integer sttServerInfoResultTotal = sttServerInfoResult.size();

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i = 0; i < sttServerInfoResultTotal; i++) {
				SttServerInfo sttServerItem = sttServerInfoResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(sttServerItem.getrSysCode());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(sttServerItem.getrSysName());
				rowItem.getCellElements().add(cellInfo);

				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(sttServerItem.getrSysIp());
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);

				rowItem = null;
			}
		}
		return xmls;
	}
		
}
