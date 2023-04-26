package com.furence.recsee.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.admin.model.DBManage;
import com.furence.recsee.admin.model.DBSQLInfo;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.admin.service.DBInfoService;
import com.furence.recsee.admin.service.DBManageService;
import com.furence.recsee.admin.service.DBSQLInfoService;
import com.furence.recsee.admin.service.JobManageService;
import com.furence.recsee.admin.service.SQLManageService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.initech.shttp.server.Logger;

/**
 * 계정관리 (관리자 전용) 페이지 컨트롤러
 * 
 * @author bella
 * @since 2021.01.19
 * @version v3.022
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private DBSQLInfoService dbSQLInfoService;
	
	@Autowired
	DBManageService dbManageService;
	
	@Autowired
	SQLManageService sqlManageService;
	
	@Autowired
	JobManageService jobManageService;

	/**
	 * Move to channel manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/channel_manage")
	public ModelAndView channelManage(HttpServletRequest request, Locale local, Model model) {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("LICENCE");
		etcConfigInfo.setConfigKey("DEFAULT_LICENCE_USE");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if(etcConfigResult.isEmpty()) {
			etcConfigInfo.setConfigValue("Y");
			etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			etcConfigInfo.setConfigValue(null);
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		}
		String defaultLicence = etcConfigResult.get(0).getConfigValue().toUpperCase();
		model.addAttribute("defaultLicence", defaultLicence);
		
		return setModelAndView("/admin/channel_manage", request, local, model, "systemOption.channel", null);
	}
	
	/**
	 * Move to channel monitoring manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/channelMonitoring_manage")
	public ModelAndView channelMonitoringManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/channelMonitoring_manage", request, local, model, "systemOption.channelMonitoring", null);
	}
	
	/**
	 * Move to server manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/server_manage")
	public ModelAndView serverManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/server_manage", request, local, model, "systemOption.server", null);
	}
	
	/**
	 * Move to switch manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/switch_manage")
	public ModelAndView switchManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/switch_manage", request, local, model, "systemOption.switchboard", null);
	}
	
	// 20200417 jbs 자번호 관리 페이지 이동 컨트롤러 정의
	/**
	 * Move to subNumber manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/subNumber_manage")
	public ModelAndView subNumberManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/subNumber_manage", request, local, model, "systemOption.subNumber", null);
	}
	
	// 20200724 justin 로고 관리 페이지 정의
	/**
	 * Move to logo manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/logo_manage")
	public ModelAndView logoManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/logo_manage", request, local, model, "systemOption.logoSetting", null);
	}
	
	// 20200818 justin 패킷 에러 세팅 페이지 정의
	/**
	 * Move to packet manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/packet_manage")
	public ModelAndView packetManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/packet_manage", request, local, model, "systemOption.packetSetting", null);
	}
	
	// 20200825 bella 패킷 에러 로그 페이지 정의
	/**
	 * Move to packet log manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/packet_log_manage")
	public ModelAndView packetLogManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/packet_log_manage", request, local, model, "systemManage.packetLogManage", null);
	}
	
	// 20200804 brandon stt 서버관리 페이지 정의
	/**
	 * Move to stt server manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/stt_server_manage")
	public ModelAndView sttServerManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/stt_server_manage", request, local, model, "systemOption.sttServer", null);
	}
	
	// 20200420 jbs 전화번호 맵핑 페이지 이동 컨트롤러 정의
	/**
	 * Move to phone mapping manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/phone_mapping")
	public ModelAndView phone_mapping(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/phone_mapping", request, local, model, "systemOption.phoneMapping", null);
	}
	
	// 20200616 jbs 내부망 관리 페이지 이동 컨트롤러 정의
	/**
	 * Move to public ip manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/publicIp_manage")
	public ModelAndView intranet_manage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/publicIp_manage", request, local, model, "systemOption.publicIp", null);
	}

	/**
	 * Move to log manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/log_manage")
	public ModelAndView logManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/log_manage", request, local, model, "systemManage.log", null);
	}

	/**
	 * Move to que manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/que_manage")
	public ModelAndView queManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/que_manage", request, local, model, "systemManage.queue", null);
	}

	/**
	 * Move to delete recfile manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/delete_recfile_manage")
	public ModelAndView deleteRecfileManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/delete_recfile_manage", request, local, model, "schedulerManage.deleteRecfile", null);
	}

	/**
	 * Move to backup recfile manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/backup_recfile_manage")
	public ModelAndView backupRecfileManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/backup_recfile_manage", request, local, model, "schedulerManage.backupRecfile", null);
	}

	// 20210208 bella 3차 백업 관리 페이지
	/**
	 * Move to file recover manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/file_recover_manage")
	public ModelAndView fileRecoverManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/file_recover_manage", request, local, model, "systemManage.fileRecoverManage", null);
	}
	
	/**
	 * Move to db manage page
	 * 
	 * @return dbResultList - 등록된 db 정보 리스트, sqlResultList - 등록된 쿼리 정보 리스트, jobResultList - 등록된 db동기화 스케줄 정보 리스트 
	 */
	@RequestMapping(value = "/db_manage")
	public ModelAndView dbmanage(HttpServletRequest request, Locale local, Model model) {
		DBManage dbManage = new DBManage();
		List<DBManage> dbManageResult = dbManageService.selectDBManage(dbManage);
		SQLManage sqlManage = new SQLManage();
		List<SQLManage> sqlManageResult = sqlManageService.selectSQLManage(sqlManage);
		JobManage jobManage = new JobManage();
		List<JobManage> jobManageResult = jobManageService.selectJobManage(jobManage);
		model.addAttribute("tab", request.getParameter("tab"));
		model.addAttribute("dbResultList", dbManageResult);
		model.addAttribute("sqlResultList", sqlManageResult);
		model.addAttribute("jobResultList", jobManageResult);
		return setModelAndView("/admin/db_manage", request, local, model, "schedulerManage.dbManage", null);
	}

	/**
	 * Move to user db manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/userDBInterface")
	public ModelAndView userDBInterface(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/userDBInterface", request, local, model, "schedulerManage.userDBInterface", null);
	}

	/**
	 * Move to group manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/group_manage")
	public ModelAndView groupManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/group_manage", request, local, model, "userManage", null);
	}

	/**
	 * Move to detail manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/detail_manage")
	public ModelAndView detailManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/detail_manage", request, local, model, "systemManage.details", null);
	}

	/**
	 * Move to user manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/user_manage")
	public ModelAndView userManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/user_manage", request, local, model, "userManage.userManage", null);
	}

	/**
	 * Move to user manage rec page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/user_manage_rec")
	public ModelAndView userManageRec(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/user_manage_rec", request, local, model, "userManage.userManageRec", null);
	}

	/**
	 * Move to authy manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/authy_manage")
	public ModelAndView authyManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/authy_manage", request, local, model, "userManage.authyManage", null);
	}

	/**
	 * Move to allowable range manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/allowableRange_manage")
	public ModelAndView allowableRangeManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/allowableRange_manage", request, local, model, "userManage.allowableRangeManage", null);
	}
	
	// 20200818 모바일 관리 메뉴
	/**
	 * Move to mobile manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/mobile_manage")
	public ModelAndView mobileManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/mobile_manage", request, local, model, "userManage.mobileManage", null);
	}
	
	/**
	 * Move to screen manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/screen_manage")
	public ModelAndView screenManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/screen_manage", request, local, model, "userManage.usageScreen", null);
	}
	
	/**
	 * Move to search manage page
	 * 
	 * @return dataCopy - 데이터 클립보드 복사 기능 사용 여부, dataCopyAll - 데이터 클립보드 전체 컬럼 복사 허용 옵션 사용 여부
	 */
	@RequestMapping(value = "/search_manage")
	public ModelAndView searchManage(HttpServletRequest request, Locale local, Model model) {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SEARCH");
		etcConfigInfo.setConfigKey("DATA_COPY");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if(etcConfigResult.isEmpty()) {
			etcConfigInfo.setConfigValue("N");
			etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			etcConfigInfo.setConfigValue(null);
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		}
		String dataCopy = etcConfigResult.get(0).getConfigValue().toUpperCase();
		model.addAttribute("dataCopy", dataCopy);

		etcConfigInfo.setGroupKey("SEARCH");
		etcConfigInfo.setConfigKey("DATA_COPY_ALL");
		etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if(etcConfigResult.isEmpty()) {
			etcConfigInfo.setConfigValue("N");
			etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			etcConfigInfo.setConfigValue(null);
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		}
		String dataCopyAll = etcConfigResult.get(0).getConfigValue().toUpperCase();
		model.addAttribute("dataCopyAll", dataCopyAll);
		
		return setModelAndView("/admin/search_manage", request, local, model, "userManage.userDefinition", null);
	}
	
	// 20200819 bella 상세 통계 사용자 정의 - 우선 모바일 통계 컬럼 정의 후 통계 범위 늘리기
	/**
	 * Move to statistics manage page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/statistics_manage")
	public ModelAndView statisticsManage(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/statistics_manage", request, local, model, "userManage.statisticsUserDefinition", null);
	}
	
//	@RequestMapping(value = "/approve_List")
//	public ModelAndView approveList(HttpServletRequest request, Locale local, Model model) {
//		return setModelAndView("/admin/approve_List", request, local, model, "userManage.approveList", null);
//	}
	/**
	 * Move to combo list page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/combo_List")
	public ModelAndView combo_List(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/combo_List", request, local, model, "userManage.comboList", null);
	}
	
	/**
	 * Move to log view page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/log_view")
	public ModelAndView log_view(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/log_view", request, local, model, "systemManage.logView", null);
	}
	
	/**
	 * Move to user interface page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/userInterface")
	public ModelAndView userInterface(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/userInterface", request, local, model, "systemManage.userInterface",null);
	}
	
	/**
	 * Move to monitoring setting page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/monitoring_setting")
	public ModelAndView monitoring_setting(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/monitoring_setting", request, local, model, "systemMonitoring.targetSetting",null);
	}

	/**
	 * Move to monitoring item page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/monitoring_item")
	public ModelAndView monitoring_item(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/monitoring_item", request, local, model, "systemMonitoring.targetItemSetting",null);
	}

	/**
	 * Move to monitoring attr page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/monitoring_attr")
	public ModelAndView monitoring_attr(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/attr_setting", request, local, model, "systemMonitoring.attrSetting",null);
	}

	/**
	 * Move to monitoring UI page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/monitoring_UI")
	public ModelAndView monitoring_UI(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/monitoring_UI", request, local, model, "systemMonitoring.UISetting",null);
	}

	/**
	 * Move to monitoring alert page
	 * 
	 * @return Model and View 
	 */
	@RequestMapping(value = "/monitoring_alert")
	public ModelAndView monitoring_alert(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/alert_list", request, local, model, "systemMonitoring.alertList",null);
	}

	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath, Map<String,Object> addAttribute) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		String evalThema = (String)request.getSession().getAttribute("evalThema");
		String clientIpChk = (String)request.getSession().getAttribute("clientIpChk");
		String ipChkDup ="";
		
		if("Y".equals(clientIpChk)) {
			ipChkDup = (String)request.getSession().getAttribute("ipChkDup");
		}
		
		if(userInfo != null) {
			if (addAttribute != null) {
				for(String key : addAttribute.keySet()) {
					model.addAttribute(key, addAttribute.get(key));
				}
			}
			model.addAttribute("userInfo", userInfo);
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("USER");
			etcConfigInfo.setConfigKey("hidden_rUserInfo");
			List<EtcConfigInfo> resultInfo = null;
			try {
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				logger.error("error {}",e);
			}
			String hidden_rUserInfo = "";
			if (resultInfo.size() > 0) {
				hidden_rUserInfo = resultInfo.get(0).getConfigValue();
			}else {
				etcConfigInfo.setGroupKey("USER");
				etcConfigInfo.setConfigKey("hidden_rUserInfo");
				etcConfigInfo.setConfigValue("");
				
				Integer insertResult = etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			}

			etcConfigInfo.setGroupKey("USER");
			etcConfigInfo.setConfigKey("hidden_aUserInfo");
			try {
				resultInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			} catch(Exception e) {
				logger.error("error {}",e);
			}
			String hidden_aUserInfo = "";
			if (resultInfo.size() > 0) {
				hidden_aUserInfo = resultInfo.get(0).getConfigValue();
			}else {
				etcConfigInfo.setGroupKey("USER");
				etcConfigInfo.setConfigKey("hidden_aUserInfo");
				etcConfigInfo.setConfigValue("");
				
				Integer insertResult = etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			model.addAttribute("evalThema", evalThema);
			model.addAttribute("clientIpChk", clientIpChk);
			model.addAttribute("hidden_rUserInfo", hidden_rUserInfo);
			model.addAttribute("hidden_aUserInfo", hidden_aUserInfo);
			if("Y".equals(clientIpChk)) {
				model.addAttribute("ipChkDup", ipChkDup);
			}
			
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);
			
			ModelAndView result = new ModelAndView();
			if( nowAccessInfo.getAccessLevel() != null ){
				model.addAttribute("nowAccessInfo", nowAccessInfo);
				result.setViewName(viewName);
				return result;
			}else{
				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
				rv.setExposeModelAttributes(false);
				
				return new ModelAndView(rv);
			}
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}
	
	// etc config ���� 
	// admin ������ ���� ������
	@RequestMapping(value = "/etc_config")
	public ModelAndView t(HttpServletRequest request, Locale local, Model model) {
		ModelAndView result = new ModelAndView();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo != null) {
			if ("admin".equals(userInfo.getUserId())) { 
				result.setViewName("/admin/etc_config");
				return result;	
			}else {
				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
				rv.setExposeModelAttributes(false);
				return new ModelAndView(rv);
			}
		}else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);
			return new ModelAndView(rv);
		}
	}
	
	@RequestMapping(value = "/script_variable")
	public ModelAndView script_variable(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/admin/script_variable", request, local, model, "systemMonitoring.script_variable", null);
	}
}
