package com.furence.recsee.admin.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.db.DBInfoList;
import com.db.Generator;
import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBManage;
import com.furence.recsee.admin.model.DBSQLInfo;
import com.furence.recsee.admin.model.ExecuteManage;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.admin.model.PbxInfo;
import com.furence.recsee.admin.model.PublicIpInfo;
import com.furence.recsee.admin.model.QueueInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.admin.model.SttServerInfo;
import com.furence.recsee.admin.model.UserDBInterface;
import com.furence.recsee.admin.service.ChannelInfoService;
import com.furence.recsee.admin.service.DBInfoService;
import com.furence.recsee.admin.service.DBManageService;
import com.furence.recsee.admin.service.DBSQLInfoService;
import com.furence.recsee.admin.service.ExecuteManageService;
import com.furence.recsee.admin.service.JobManageService;
import com.furence.recsee.admin.service.PbxInfoService;
import com.furence.recsee.admin.service.PublicIpInfoService;
import com.furence.recsee.admin.service.QueueInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.admin.service.SQLManageService;
import com.furence.recsee.admin.service.SttServerInfoService;
import com.furence.recsee.admin.service.UserDBInterfaceService;
import com.furence.recsee.admin.service.dbsync.DBSyncDAO;
import com.furence.recsee.admin.service.dbsync.PostgresqlQuery;
import com.furence.recsee.admin.service.dbsync.SchemaInfo;
import com.furence.recsee.admin.service.dbsync.SelectQueryInfo;
import com.furence.recsee.admin.service.rsDBGenerator.DBConnectionMgr;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.CustConfigInfo;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.LogoVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.PacketVO;
import com.furence.recsee.common.model.PhoneMappingInfo;
import com.furence.recsee.common.model.SubNumberInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.CustConfigInfoService;
import com.furence.recsee.common.service.CustomerInfoService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.PhoneMappingInfoService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;

@Controller
public class AjaxSystemSetupController {

	private static final Logger logger = LoggerFactory.getLogger(AjaxSystemSetupController.class);

	@Autowired
	private ChannelInfoService channelInfoService;

	@Autowired
	private LogService logService;

	@Autowired
	private LogInfoService logInfoService;

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
	private DBSQLInfoService dbSQLInfoService;

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
	private PbxInfoService pbxInfoService;

	@Autowired
	private QueueInfoService queueInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private CustConfigInfoService custConfigInfoService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private RUserInfoService ruserInfoService;
	
	@Autowired
	private CustomerInfoService customerInfoService;

	@RequestMapping(value = "/channel_generation_proc.do")
	public @ResponseBody AJaxResVO channelGeneration(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ChannelInfo setChannelInfo = new ChannelInfo();

		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);

			if(userInfo != null) {
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.channel");

				if(!request.getParameter("method").equals("modify") && !request.getParameter("method").equals("delete")) { // 채널 추가
					if(request.getParameter("chDelete") != null && request.getParameter("chDelete").equals("1")) {
						Integer deleteChannelResult = channelInfoService.deleteChannelInfo(setChannelInfo);
						if(deleteChannelResult == 1) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							logService.writeLog(request, "CHANNEL", "INIT", setChannelInfo.toLogString(messageSource));
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "delete fail");
							logService.writeLog(request, "CHANNEL", "INIT-FAIL", setChannelInfo.toLogString(messageSource));
						}
					}

					String sExtNum = "";
					String eExtNum = "";
					
					if(!request.getParameter("method").equals("1")) {
						sExtNum = request.getParameter("sExtNum").trim();
						eExtNum = request.getParameter("sExtNum").trim();
					} else {
						sExtNum = request.getParameter("sExtNum").trim();
						eExtNum = request.getParameter("eExtNum").trim();
					}

					String extNum = request.getParameter("sExtNum").trim();
					String sysCode = request.getParameter("systemCode").trim();
					String chIp = request.getParameter("chIp").trim();
					String chAgtIp = request.getParameter("chAgtIp").trim();
					String recYn = request.getParameter("recordingUse").trim();
					String recType = request.getParameter("recordingType").trim();
					String recMethod = request.getParameter("recordingMethod").trim();
					String screenYn = request.getParameter("screenYn").trim();

					SystemInfo systemInfo2 = new SystemInfo();
					systemInfo2.setSysId(sysCode);
					List<SystemInfo> selectSysWorking = systemInfoService.selectSystemInfo(systemInfo2);
					
					// 현재 해당 sysId 의 flag가 0이 아니면 돌려보내기
					if (selectSysWorking.size() > 0 && selectSysWorking.get(0).getChannelUpdateFlag() != null && !"0".equals(selectSysWorking.get(0).getChannelUpdateFlag())) {
						// 돌아가. (단호)
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "server is working");

						logService.writeLog(request, "CHANNEL", "INSERT-FAIL", setChannelInfo.toLogString(messageSource));
						
						return jRes;
					}
					
					String[] arChIp = new String[4];
					String[] arChAgtIp = new String[4];

					int inc = 0;
					for(int i = Integer.parseInt(sExtNum); i <= Integer.parseInt(eExtNum); i++) {

						setChannelInfo.setExtNum(String.valueOf(Integer.parseInt(extNum)+inc));

						setChannelInfo.setSysCode(sysCode);

						arChIp = chIp.split("\\.");

						arChIp[3] = String.valueOf(Integer.parseInt(arChIp[3]) + inc);
						setChannelInfo.setExtIp(StringUtils.join(arChIp,"."));

						setChannelInfo.setExtAgtIp(chAgtIp);
						
						setChannelInfo.setRecYn(recYn);

						setChannelInfo.setRecType(recType);

						setChannelInfo.setExtKind(recMethod);

						setChannelInfo.setScreenYn(screenYn);

						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							Integer insertChannelResult = channelInfoService.insertChannelInfo(setChannelInfo);
							if(insertChannelResult == 0) {
								logService.writeLog(request, "CHANNEL", "INSERT-FAIL", setChannelInfo.toLogString(messageSource));
							} else {
								logService.writeLog(request, "CHANNEL", "INSERT-SUCCESS", setChannelInfo.toLogString(messageSource));
							}
							inc++;
						}else {
							logService.writeLog(request, "CHANNEL", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					}

					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("1");

					SystemInfo systemInfo = new SystemInfo();
					systemInfo.setChannelUpdateFlag("1");
					systemInfoService.updateSystemInfo(systemInfo);

				} else if(request.getParameter("method").equals("modify")){ // 채널 수정
					Enumeration<?> paramNames = request.getParameterNames();
					String sysFlag = "";
					while(paramNames.hasMoreElements()) {
						String names = (String)paramNames.nextElement();

						String val = request.getParameter(names).trim();
						if(val.equals("")) val = " ";

						switch(names) {
						case "chNum" :
							setChannelInfo.setChNum(Integer.parseInt(val));
							break;
						case "oldSysCode" :
							SystemInfo systemInfo3 = new SystemInfo();
							List<SystemInfo> systemInfoResult = systemInfoService.selectSystemInfo(systemInfo3);
							setChannelInfo.setOldSysCode(new FindOrganizationUtil().getSysCode(val,systemInfoResult));
							sysFlag = new FindOrganizationUtil().getSysFlag(val,systemInfoResult);
							break;
						case "systemCode" :
							SystemInfo systemInfo2 = new SystemInfo();
							List<SystemInfo> systemInfoResult2 = systemInfoService.selectSystemInfo(systemInfo2);
							setChannelInfo.setSysCode(new FindOrganizationUtil().getSysCode(val,systemInfoResult2));
							break;
						case "extNum" :
							setChannelInfo.setExtNum(val);
							break;
						case "chIp" :
							setChannelInfo.setExtIp(val);
							break;
						case "chAgtIp" :
							setChannelInfo.setExtAgtIp(val);
							break;
						case "recYn" :
							setChannelInfo.setRecYn(val);
							break;
						case "recType" :
							setChannelInfo.setRecType(val);
							break;
						case "recKind" :
							setChannelInfo.setExtKind(val);
							break;
						case "screenYn" :
							setChannelInfo.setScreenYn(val);
							break;
						case "genesysRegisterYn" :
							setChannelInfo.setChTenant(val);
							break;
						}
					}

					// 현재 해당 sysId 의 flag가 0이 아니면 돌려보내기
					if (!"".equals(sysFlag) && !"0".equals(sysFlag)) {
						// 돌아가. (단호)
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "server is working");

						logService.writeLog(request, "CHANNEL", "UPDATE-FAIL", setChannelInfo.toLogString(messageSource));
						
						return jRes;
					}
					
					if ("Y".equals(nowAccessInfo.getModiYn())) {
						Integer updateChannelResult = channelInfoService.updateChannelInfo(setChannelInfo);
						if(updateChannelResult == 0) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "channel update fail");

							logService.writeLog(request, "CHANNEL", "UPDATE-FAIL", setChannelInfo.toLogString(messageSource));
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");

							SystemInfo systemInfo = new SystemInfo();
							systemInfo.setChannelUpdateFlag("1");
							systemInfoService.updateSystemInfo(systemInfo);

							logService.writeLog(request, "CHANNEL", "UPDATE-SUCCESS", setChannelInfo.toLogString(messageSource));
						}
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "channel update fail");

						logService.writeLog(request, "CHANNEL", "UPDATE-FAIL", "권한이 없는 사용자의 요청입니다.");
					}


				} else { // 채널 삭제
					// 삭제할 서버 flag가 0이 아닐때 삭제 실패 -> 삭제 실패한 서버 리스트 => errorMsg에 ++++++ => return delete_fail;
					String errorMsg = "";
					
					String[] chList = request.getParameter("chList").split(",");
					String[] sysList = request.getParameter("sysList").split(",");

					for(int i = 0; i < chList.length; i++) {
						setChannelInfo.setChNum(Integer.parseInt(chList[i].toString()));
						String sysCode = sysList[i].toString();
						if(sysList[i].equals("") || sysList[i] == null) {
							sysCode = " ";
						}

						SystemInfo systemInfo2 = new SystemInfo();
						List<SystemInfo> selectSysWorking = systemInfoService.selectSystemInfo(systemInfo2);
						setChannelInfo.setSysCode(new FindOrganizationUtil().getSysCode(sysCode,selectSysWorking));
						
						String sysFlag = new FindOrganizationUtil().getSysFlag(sysCode,selectSysWorking);
						// 현재 해당 sysId 의 flag가 0이 아니면 돌려보내기
						if (!"".equals(sysFlag) && !"0".equals(sysFlag)) {
							logService.writeLog(request, "CHANNEL", "DELETE-FAIL", setChannelInfo.toLogString(messageSource));
							errorMsg += ", [ " + Integer.parseInt(chList[i].toString()) + " : " + sysList[i].toString() + " ]";
							continue;
						}
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							Integer deleteChannelResult = channelInfoService.deleteChannelInfo(setChannelInfo);

							if(deleteChannelResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");

								SystemInfo systemInfo = new SystemInfo();
								systemInfo.setChannelUpdateFlag("1");
								systemInfoService.updateSystemInfo(systemInfo);

								logService.writeLog(request, "CHANNEL", "DELETE-SUCCESS", setChannelInfo.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", "delete fail");
								jRes.setResult("0");

								logService.writeLog(request, "CHANNEL", "DELETE-FAIL", setChannelInfo.toLogString(messageSource));
							}
						}else {
							logService.writeLog(request, "CHANNEL", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					}
					if (!"".equals(errorMsg)) {
						errorMsg = errorMsg.substring(1); // 앞 구분자 "," 자름
						// 돌아가. (단호)
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "server is working");
						jRes.addAttribute("reason", errorMsg);
						
						return jRes;
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");

				logService.writeLog(request, "CHANNEL", "EXCEPTION", "세션이 끊겼습니다.");
			}
		} catch(NullPointerException e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			logger.error("error {}",e);

			logService.writeLog(request, "CHANNEL", "EXCEPTION", e.getMessage());
		}catch(Exception e) {
			logger.error("", e);
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");

			logger.error("error {}",e);
			logService.writeLog(request, "CHANNEL", "EXCEPTION", e.getMessage());
		}

		return jRes;
	}


	//시스템 관리 - 시스템 설정
	@RequestMapping(value = "/system_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO system_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		try {
			if(userInfo != null) {

				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.server");

				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("sysId") != null && request.getParameter("sysName") != null && request.getParameter("sysIp") != null) {

		               SystemInfo systemInfo = new SystemInfo();

		               systemInfo.setSysId(request.getParameter("sysId"));
		               systemInfo.setSysName(request.getParameter("sysName"));
		               systemInfo.setSysIp(request.getParameter("sysIp"));
		               if(request.getParameter("pbxId") != null) systemInfo.setPbxId(request.getParameter("pbxId"));
		               else systemInfo.setPbxId("");
		               if(request.getParameter("storagePath") != null) systemInfo.setStoragePath(request.getParameter("storagePath"));
		               else systemInfo.setStoragePath("");
		               
		               if(request.getParameter("sysDeleteYN") != null && "Y".equals(request.getParameter("sysDeleteYN"))) {
		            	   if (request.getParameter("sysDeleteSize") != null && request.getParameter("sysDeletePath") != null && !"".equals(request.getParameter("sysDeleteSize")) && !"".equals(request.getParameter("sysDeletePath"))) {
		            		   systemInfo.setSysDeleteSize(request.getParameter("sysDeleteSize"));
		            		   systemInfo.setSysDeletePath(request.getParameter("sysDeletePath"));
		            		   
		            		   Integer sysDeleteInfoResult = systemInfoService.insertSysDeleteInfo(systemInfo);
		            		   if (sysDeleteInfoResult != 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "sysDeleteInfo insert fail");
									return jRes;
		            		   }
		            	   } else {
		            		   // 삭제 옵션 사용 but 사이즈 또는 경로 설정 안함
		            		    jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "no param sysDeleteSize & sysDeletePath");
								return jRes;
		            	   }
		               } else {
		            	   // 삭제 옵션 미사용시 rs_sys_file_delete 테이블에서 삭제
		            	   Integer sysDeleteInfoResult = systemInfoService.deleteSysDeleteInfo(systemInfo);
		               }
		               
		               if ("Y".equals(nowAccessInfo.getWriteYn())) {
			               Integer systemInfoResult = systemInfoService.insertSystemInfo(systemInfo);
			               Integer licenceInfoResult = systemInfoService.insertLicenceInfo(systemInfo);
			               if(systemInfoResult == 1 && licenceInfoResult == 1) {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			                  jRes.setResult("1");
			                  jRes.addAttribute("msg", "system insert success");
			                  
			                  logService.writeLog(request, "SERVER", "INSERT-SUCCESS", systemInfo.toLogString(messageSource));
			               } else {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system insert fail");

			                  logService.writeLog(request, "SERVER", "INSERT-FAIL", systemInfo.toLogString(messageSource));
			               }
		               }else {
		            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system insert fail");

			                  logService.writeLog(request, "SERVER", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
		               }
		            } else if(request.getParameter("proc").equals("modify") && request.getParameter("sysId") != null) {
		               SystemInfo systemInfo = new SystemInfo();

		               systemInfo.setSysId(request.getParameter("sysId"));

		               if(request.getParameter("sysName")!= null) systemInfo.setSysName(request.getParameter("sysName"));
		               if(request.getParameter("sysIp") != null) systemInfo.setSysIp(request.getParameter("sysIp"));
		               if(request.getParameter("pbxId") != null) systemInfo.setPbxId(request.getParameter("pbxId"));
		               if(request.getParameter("storagePath") != null) systemInfo.setStoragePath(request.getParameter("storagePath"));

		               if(request.getParameter("sysDeleteYN") != null && "Y".equals(request.getParameter("sysDeleteYN"))) {
		            	   if (request.getParameter("sysDeleteSize") != null && request.getParameter("sysDeletePath") != null && !"".equals(request.getParameter("sysDeleteSize")) && !"".equals(request.getParameter("sysDeletePath"))) {
		            		   systemInfo.setSysDeleteSize(request.getParameter("sysDeleteSize"));
		            		   systemInfo.setSysDeletePath(request.getParameter("sysDeletePath"));
		            		   
		            		   Integer sysDeleteInfoResult = systemInfoService.insertSysDeleteInfo(systemInfo);
		            	   } else {
		            		   // 삭제 옵션 사용 but 사이즈 또는 경로 설정 안함
		            		    jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "no param sysDeleteSize & sysDeletePath");
								return jRes;
		            	   }
		               } else {
		            	   // 삭제 옵션 미사용시 rs_sys_file_delete 테이블에서 삭제
		            	   Integer sysDeleteInfoResult = systemInfoService.deleteSysDeleteInfo(systemInfo);
		               }
		               
		               if ("Y".equals(nowAccessInfo.getModiYn())) {
		            	   Integer systemInfoResult = systemInfoService.updateSystemInfo(systemInfo);
		            	   if(!StringUtil.isNull(systemInfo.getSysIp(), true)) {
		            		   Integer licenceInfoResult = systemInfoService.updateLicenceInfo(systemInfo);
		            	   }
			               if(systemInfoResult == 1) {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			                  jRes.setResult("1");
			                  jRes.addAttribute("msg", "system update success");

			                  logService.writeLog(request, "SERVER", "UPDATE-SUCCESS", systemInfo.toLogString(messageSource));
			               } else {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system update fail");

			                  logService.writeLog(request, "SERVER", "UPDATE-FAIL", systemInfo.toLogString(messageSource));
			               }
		               }else {
		            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system update fail");

			                  logService.writeLog(request, "SERVER", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
		               }
		            } else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
		               String[] chList = request.getParameter("chList").split(",");
		               SystemInfo systemInfo = new SystemInfo();

		               if ("Y".equals(nowAccessInfo.getDelYn())) {

			               for(int i = 0; i < chList.length; i++) {
			                  systemInfo.setSysId(chList[i]);

			                  Integer systemInfoResult = systemInfoService.deleteSystemInfo(systemInfo);
			                  Integer licenceInfoResult = systemInfoService.deleteLicenceInfo(systemInfo);
			                  Integer sysDeleteInfoResult = systemInfoService.deleteSysDeleteInfo(systemInfo);
			                  if(systemInfoResult == 1 && licenceInfoResult == 1) {
			                     jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			                     jRes.setResult("1");
			                     jRes.addAttribute("msg", "system delete success");

			                     logService.writeLog(request, "SERVER", "DELETE-SUCCESS", systemInfo.toLogString(messageSource));
			                  } else {
			                     jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                     jRes.setResult("0");
			                     jRes.addAttribute("msg", "system delete fail");

			                     logService.writeLog(request, "SERVER", "DELETE-FAIL", systemInfo.toLogString(messageSource));
			                  }
			               }
		               }else {
		            	     jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                     jRes.setResult("0");
		                     jRes.addAttribute("msg", "system delete fail");

		                     logService.writeLog(request, "SERVER", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
		               }

		            } else {
		               jRes.setSuccess(AJaxResVO.SUCCESS_N);
		               jRes.setResult("0");
		               jRes.addAttribute("msg", "system proc command fail");

		               String cmd = "저장";
		               if ("modify".equals(request.getParameter("proc")))
		            	   cmd = "수정";
		               if ("delete".equals(request.getParameter("proc")))
		            	   cmd = "삭제";

		               logService.writeLog(request, "SERVER", "COMMAND-FAIL", cmd + " 실패");
		            }
		         } else {
		            jRes.setSuccess(AJaxResVO.SUCCESS_N);
		            jRes.setResult("0");
		            jRes.addAttribute("msg", "proc fail");

		            logService.writeLog(request, "SERVER", "COMMAND-FAIL", "비 정상적인 요청입니다.");
		         }
		      } else {
		         jRes.setSuccess(AJaxResVO.SUCCESS_N);
		         jRes.setResult("0");
		         jRes.addAttribute("msg", "login fail");

		         logService.writeLog(request, "SERVER", "COMMAND-FAIL", "세션이 끊겼습니다.");
		      }
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "SERVER", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "SERVER", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}

	//시스템 관리 - 시스템 설정
	@RequestMapping(value = "/packet_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO packet_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		try {
			if(userInfo != null) {
				String cmd = request.getParameter("cmd");
				String custCode = request.getParameter("custCode");
				String phoneSetting = request.getParameter("phoneSetting");
				String returnMsg = request.getParameter("returnMsg");
				String returnUrl = request.getParameter("returnUrl");
				PacketVO packetVO = new PacketVO();
				
				
				packetVO.setPhoneSetting(phoneSetting);
				packetVO.setMsg(returnMsg);
				packetVO.setReturnUrl(returnUrl);
				Integer returnResult = 0;
				if("insert".equals(cmd)) {
					packetVO.setCustCode(custCode);
					returnResult =  subNumberInfoService.insertPacketSettingInfo(packetVO);
				}else if("update".equals(cmd)) {
					packetVO.setCustCode(custCode);
					returnResult =  subNumberInfoService.updatePacketSettingInfo(packetVO);
				}else if("delete".equals(cmd)) {
					String[] custCodeArray = custCode.split(",");
					
					if(custCodeArray.length > 1) {
						for(int i=0; i < custCodeArray.length;i++) {
							packetVO.setCustCode(custCodeArray[i]);
							returnResult +=  subNumberInfoService.deletePacketSettingInfo(packetVO);
						}
					}else {
						packetVO.setCustCode(custCode);
						returnResult =  subNumberInfoService.deletePacketSettingInfo(packetVO);
					}
					
					
				}
				
				if(returnResult > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                    jRes.setResult("1");
                    jRes.addAttribute("msg", "packetSetting "+cmd+" success");
				}else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
                    jRes.setResult("0");
                    jRes.addAttribute("msg", "packetSetting "+cmd+" fail");
				}
				
			}
		}catch (Exception e) {
			logger.error("error",e);
		}

		               
		return jRes;
	}
	

   // 시스템 관리 - 교환기 연동 정보
   @RequestMapping(value = "/pbx_proc.do" , method=RequestMethod.POST )
   public @ResponseBody AJaxResVO pbx_proc(HttpServletRequest request, Locale local, Model model) {
      AJaxResVO jRes = new AJaxResVO();
      LoginVO userInfo = SessionManager.getUserInfo(request);
      if(userInfo != null) {
         if(request.getParameter("proc") != null ) {
            if(request.getParameter("proc").equals("insert") &&
               request.getParameter("pbxId") != null && request.getParameter("pbxName") != null && request.getParameter("pbxIp") != null) {

               PbxInfo pbxInfo = new PbxInfo();

               pbxInfo.setrPbxId(request.getParameter("pbxId"));
               pbxInfo.setrPbxName(request.getParameter("pbxName"));
               pbxInfo.setrPbxIp(request.getParameter("pbxIp"));
               if(request.getParameter("pbxSipId") != null) pbxInfo.setrPbxSipId(request.getParameter("pbxSipId"));
               else pbxInfo.setrPbxSipId("");
               if(request.getParameter("pbxSipPassword") != null) pbxInfo.setrPbxSipPassword(request.getParameter("pbxSipPassword"));
               else pbxInfo.setrPbxSipPassword("");
               Integer pbxInfoResult = pbxInfoService.insertPbxInfo(pbxInfo);
               if(pbxInfoResult == 1) {
                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                  jRes.setResult("1");
                  jRes.addAttribute("msg", "pbx insert success");

                  logInfoService.writeLog(request, "Pbx - Insert success", pbxInfo.toString(), userInfo.getUserId());
               } else {
                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
                  jRes.setResult("0");
                  jRes.addAttribute("msg", "pbx insert fail");

                  logInfoService.writeLog(request, "Pbx - Insert fail", pbxInfo.toString(), userInfo.getUserId());
               }
            } else if(request.getParameter("proc").equals("modify") && request.getParameter("pbxId") != null) {
               PbxInfo pbxInfo = new PbxInfo();

               pbxInfo.setrPbxId(request.getParameter("pbxId"));

               if(request.getParameter("pbxName")!= null) pbxInfo.setrPbxName(request.getParameter("pbxName"));
               if(request.getParameter("pbxIp") != null) pbxInfo.setrPbxIp(request.getParameter("pbxIp"));
               if(request.getParameter("pbxSipId") != null) pbxInfo.setrPbxSipId(request.getParameter("pbxSipId"));
               if(request.getParameter("pbxSipPassword") != null) pbxInfo.setrPbxSipPassword(request.getParameter("pbxSipPassword"));

               Integer pbxInfoResult = pbxInfoService.updatePbxInfo(pbxInfo);
               if(pbxInfoResult == 1) {
                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                  jRes.setResult("1");
                  jRes.addAttribute("msg", "pbx update success");

                  logInfoService.writeLog(request, "Pbx - Update success", pbxInfo.toString(), userInfo.getUserId());
               } else {
                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
                  jRes.setResult("0");
                  jRes.addAttribute("msg", "pbx update fail");

                  logInfoService.writeLog(request, "Pbx - Update fail", pbxInfo.toString(), userInfo.getUserId());
               }

            } else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
               String[] chList = request.getParameter("chList").split(",");
               PbxInfo pbxInfo = new PbxInfo();

               for(int i = 0; i < chList.length; i++) {
                  pbxInfo.setrPbxId(chList[i]);
                  Integer pbxInfoResult = pbxInfoService.deletePbxInfo(pbxInfo);
                  if(pbxInfoResult == 1) {
                     jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                     jRes.setResult("1");
                     jRes.addAttribute("msg", "pbx delete success");

                     logInfoService.writeLog(request, "Pbx - Delete success", pbxInfo.toString(), userInfo.getUserId());
                  } else {
                     jRes.setSuccess(AJaxResVO.SUCCESS_N);
                     jRes.setResult("0");
                     jRes.addAttribute("msg", "pbx delete fail");

                     logInfoService.writeLog(request, "Pbx - Delete fail", pbxInfo.toString(), userInfo.getUserId());
                  }
               }

            } else {
               jRes.setSuccess(AJaxResVO.SUCCESS_N);
               jRes.setResult("0");
               jRes.addAttribute("msg", "pbx proc command fail");

               logInfoService.writeLog(request, "Pbx - Proc command fail", request.getParameter("proc"), userInfo.getUserId());
            }
         } else {
            jRes.setSuccess(AJaxResVO.SUCCESS_N);
            jRes.setResult("0");
            jRes.addAttribute("msg", "proc fail");

            logInfoService.writeLog(request, "Pbx - Abnormal request", null, userInfo.getUserId());
         }
      } else {
         jRes.setSuccess(AJaxResVO.SUCCESS_N);
         jRes.setResult("0");
         jRes.addAttribute("msg", "login fail");

         logInfoService.writeLog(request, "Pbx - Logout");
      }
      return jRes;
   }

   // 시스템 관리 - 큐 관리
   @RequestMapping(value = "/queue_proc.do")
   public @ResponseBody AJaxResVO queue_proc(HttpServletRequest request, Locale local, Model model) {
      AJaxResVO jRes = new AJaxResVO();

      try {
         LoginVO userInfo = SessionManager.getUserInfo(request);
         if(userInfo != null) {
            QueueInfo queueInfo = new QueueInfo();
            String proc = request.getParameter("proc");

            jRes.setSuccess(AJaxResVO.SUCCESS_N);
            jRes.setResult("0");
            jRes.addAttribute("msg", "queue proc fail");

            switch(proc)
            {
               case "insert" :
                  if((request.getParameter("rQueueNum") != null && !request.getParameter("rQueueNum").equals("")) &&
                     (request.getParameter("rQueueName") != null && !request.getParameter("rQueueName").equals(""))) {

                     queueInfo.setrQueueNum(request.getParameter("rQueueNum"));
                     queueInfo.setrQueueName(request.getParameter("rQueueName"));

                     Integer insertQueueResult = queueInfoService.insertQueueInfo(queueInfo);
                     if (insertQueueResult == 1) {
                        jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                        jRes.setResult("1");
                        jRes.addAttribute("msg", "queue insert success");

                        logInfoService.writeLog(request, "Queue - Insert success", queueInfo.toString(), userInfo.getUserId());
                     } else {
                        logInfoService.writeLog(request, "Queue - Insert fail", queueInfo.toString(), userInfo.getUserId());
                     }
                  }
                  break;
               case "update" :
                  if((request.getParameter("rQueueNum") != null && !request.getParameter("rQueueNum").equals("")) &&
                        (request.getParameter("rQueueName") != null && !request.getParameter("rQueueName").equals(""))) {

                     queueInfo.setrQueueNum(request.getParameter("rQueueNum"));
                     queueInfo.setrQueueName(request.getParameter("rQueueName"));

                     Integer updateQueueResult = queueInfoService.updateQueueInfo(queueInfo);
                     if (updateQueueResult == 1) {
                        jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                        jRes.setResult("1");
                        jRes.addAttribute("msg", "queue update success");

                        logInfoService.writeLog(request, "Queue - Update success", queueInfo.toString(), userInfo.getUserId());
                     } else {
                        logInfoService.writeLog(request, "Queue - Update fail", queueInfo.toString(), userInfo.getUserId());
                     }
                  }
                  break;
               case "delete" :
                  if(request.getParameter("queueList") != null && !request.getParameter("queueList").equals("")) {
                     String[] queueList = request.getParameter("queueList").split(",");

                     for(int i = 0; i < queueList.length; i++) {
                        queueInfo.setrQueueNum(queueList[i]);

                        Integer insertQueueResult = queueInfoService.deleteQueueInfo(queueInfo);
                        if (insertQueueResult == 1) {
                           jRes.setSuccess(AJaxResVO.SUCCESS_Y);
                           jRes.setResult("1");
                           jRes.addAttribute("msg", "queue delete success");

                           logInfoService.writeLog(request, "Queue - Delete success", queueInfo.toString(), userInfo.getUserId());
                        } else {
                           logInfoService.writeLog(request, "Queue - Delete fail", queueInfo.toString(), userInfo.getUserId());
                        }
                     }
                  }
                  break;
               default :
                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
                  jRes.setResult("0");
                  jRes.addAttribute("msg", "proc type fail");

                  logInfoService.writeLog(request, "Queue - Abnormal request", proc, userInfo.getUserId());
                  break;
            }
         } else {
            jRes.setSuccess(AJaxResVO.SUCCESS_N);
            jRes.setResult("0");
            jRes.addAttribute("msg", "queue fail");

            logInfoService.writeLog(request, "Queue - Logout");
         }
      } catch(NullPointerException e) {
          logger.error("", e);
			logger.error("error",e);
          jRes.setSuccess(AJaxResVO.SUCCESS_N);
          jRes.setResult("0");

          logInfoService.writeLog(request, "Queue - Proc exception", e.getMessage());
       } catch(Exception e) {
         logger.error("", e);
			logger.error("error",e);
         jRes.setSuccess(AJaxResVO.SUCCESS_N);
         jRes.setResult("0");

         logInfoService.writeLog(request, "Queue - Proc exception", e.getMessage());
      }
      return jRes;
   }

   // 시스템 관리 - 채널 중복 체크 + 라이센스초과여부 확인
	@RequestMapping(value = "/channelOverlap.do")
	public @ResponseBody AJaxResVO channelOverlap(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		ChannelInfo setChannelInfo = new ChannelInfo();
		SystemInfo systemInfo = new SystemInfo();
		
		if(userInfo != null) {
			setChannelInfo.setSysCode(request.getParameter("systemCode")); // A001
			// 먼저 라이센스 초과여부 확인
			Integer channelUsage = channelInfoService.checkIpOverlap(setChannelInfo);// 현재 사용중인 채널 개수	channelUsage : 8
			systemInfo.setSysId(request.getParameter("systemCode")); // A001
			
			List<SystemInfo> systemInfoResult = null;
			try {
				systemInfo.setLicEnc("Y");
				systemInfoResult = systemInfoService.selectLicenceInfo(systemInfo);
			} catch(Exception e) {
				logger.error("error",e);
				systemInfo.setLicEnc("N");
				systemInfoResult = systemInfoService.selectLicenceInfo(systemInfo);
			}
			
			int licenceSize = 0;	// 라이센스 개수
			if(systemInfoResult.size()>0 && !StringUtil.isNull(systemInfoResult.get(0).getSysLicence(), true)) {
				licenceSize = Integer.parseInt(systemInfoResult.get(0).getSysLicence());
			}
			// 라이센스 초과 아니면 중복여부 확인
			if(channelUsage<licenceSize) {
				setChannelInfo.setExtNum(request.getParameter("extNumber"));
				setChannelInfo.setExtIp(request.getParameter("IpNumber"));
				setChannelInfo.setSysCode(request.getParameter("systemCode"));
				
				// 내선번호는 무조건 중복 안되는걸로 체크
				Integer checkExtOverlapResult = channelInfoService.checkExtOverlap(setChannelInfo);
				// 아이피는 시스템 같을때만 중복 체크
				Integer checkIpOverlapResult = channelInfoService.checkIpOverlap(setChannelInfo);

				Integer checkAgtIpOverlapResult = 0;
				
				if(!StringUtil.isNull(request.getParameter("agtIpNumber"), true)) {
					setChannelInfo.setExtIp(null);
					setChannelInfo.setExtAgtIp(request.getParameter("agtIpNumber"));

					checkAgtIpOverlapResult = channelInfoService.checkIpOverlap(setChannelInfo);
				}
				
				if(checkExtOverlapResult==0 && checkIpOverlapResult==0 && checkAgtIpOverlapResult == 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else if(checkExtOverlapResult>0){
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("extnum is overlap");
				}else if(checkIpOverlapResult>0){
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("ip is overlap");
				}else if(checkAgtIpOverlapResult>0){
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("agt ip is overlap");
				}
			// 라이센스 초과면 채널 등록 안됨
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("licence over");
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	
   // 시스템 관리 - etc config 관리
   @RequestMapping(value = "/etc_config_proc.do")
   public @ResponseBody AJaxResVO etc_config_proc(HttpServletRequest request, Locale local, Model model) {
	   AJaxResVO jRes = new AJaxResVO();

	   try {
		   LoginVO userInfo = SessionManager.getUserInfo(request);
		   if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			   String proc = request.getParameter("proc");

			   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			   jRes.setResult("0");
			   jRes.addAttribute("msg", "etc_config_proc fail");

			   switch(proc){
			   		case "insert" :

			   			if (!StringUtil.isNull(request.getParameter("groupKey"),true))
			   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
			   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
			   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));
			   			if (!StringUtil.isNull(request.getParameter("configValue"),true))
			   				etcConfigInfo.setConfigValue(request.getParameter("configValue"));

			   			Integer insertEtcConfig = etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);

			   			if (insertEtcConfig == 1) {
			   				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			   				jRes.setResult("1");
			   				jRes.addAttribute("msg", "etc config insert success");
			   				logService.writeLog(request, "ETCCONFIG", "INSERT-SUCCESS", etcConfigInfo.toLogString(messageSource));
			   			} else {
			   				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			   				jRes.setResult("0");
			   				jRes.addAttribute("msg", "etc config insert fail");
			   				logService.writeLog(request, "ETCCONFIG", "INSERT-FAIL", etcConfigInfo.toLogString(messageSource));
			   			}

			   			break;
			   		case "update" :

			   			if (!StringUtil.isNull(request.getParameter("groupKey"),true))
			   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
			   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
			   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));

			   			if (!StringUtil.isNull(request.getParameter("mGroupKey"),true))
			   				etcConfigInfo.setmGroupKey(request.getParameter("mGroupKey"));
			   			if (!StringUtil.isNull(request.getParameter("mConfigKey"),true))
			   				etcConfigInfo.setmConfigKey(request.getParameter("mConfigKey"));

			   			if (!StringUtil.isNull(request.getParameter("configValue"),true))
			   				etcConfigInfo.setConfigValue(request.getParameter("configValue"));

			   			Integer updateEtcConfig = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);

			   			if (updateEtcConfig == 1) {
			   				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			   				jRes.setResult("1");
			   				jRes.addAttribute("msg", "etc config update success");
			   				logService.writeLog(request, "ETCCONFIG", "UPDATE-SUCCESS", etcConfigInfo.toLogString(messageSource));
			   			} else {

			   				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			   				jRes.setResult("0");
			   				jRes.addAttribute("msg", "etc config update fail");

			   				logService.writeLog(request, "ETCCONFIG", "UPDATE-FAIL", etcConfigInfo.toLogString(messageSource));

			   			}
			   			break;
			   		case "delete" :

			   			if (!StringUtil.isNull(request.getParameter("deleteList"),true)) {

			   				String[] list = request.getParameter("deleteList").split("\\|");

			   				for (int i =0 ; i < list.length ;i++) {
			   					String[] temp = list[i].split(",");
			   					if (temp.length == 2) {
			   						etcConfigInfo.setGroupKey(temp[0]);
			   						etcConfigInfo.setConfigKey(temp[1]);
			   						Integer deleteEtcConfig = etcConfigInfoService.deleteEtcConfigInfo(etcConfigInfo);

			   						if (deleteEtcConfig == 1) {
						   				logService.writeLog(request, "ETCCONFIG", "DELETE-SUCCESS", etcConfigInfo.toLogString(messageSource));
						   			} else {
						   				logService.writeLog(request, "ETCCONFIG", "DELETE-FAIL", etcConfigInfo.toLogString(messageSource));
						   			}
			   					}
			   				}

			   				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			   				jRes.setResult("1");
			   				jRes.addAttribute("msg", "etc config delete success");

			   			}else {
			   				if (!StringUtil.isNull(request.getParameter("groupKey"),true))
			   					etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
			   				if (!StringUtil.isNull(request.getParameter("configKey"),true))
			   					etcConfigInfo.setConfigKey(request.getParameter("configKey"));
	   						Integer deleteEtcConfig = etcConfigInfoService.deleteEtcConfigInfo(etcConfigInfo);

	   						if (deleteEtcConfig == 1) {

				   				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				   				jRes.setResult("1");
				   				jRes.addAttribute("msg", "etc config delete success");

				   				logService.writeLog(request, "ETCCONFIG", "DELETE-SUCCESS", etcConfigInfo.toLogString(messageSource));
				   			} else {

				   				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				   				jRes.setResult("0");
				   				jRes.addAttribute("msg", "etc config delete fail");

				   				logService.writeLog(request, "ETCCONFIG", "DELETE-FAIL", etcConfigInfo.toLogString(messageSource));
				   			}
			   			}

			   			break;
			   		default :
			   			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			   			jRes.setResult("0");
			   			jRes.addAttribute("msg", "proc type fail");

			   			logService.writeLog(request, "ETCCONFIG", "COMMAND-FAIL", "비 정상적인 요청입니다.");
			   			break;
			   }
		   } else {
			   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			   jRes.setResult("0");
			   jRes.addAttribute("msg", "etc config fail");

			   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		   }
	   } catch(NullPointerException e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
	   }catch(Exception e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
	   }
	   return jRes;
   }
   
	// 채널 라이센스 불러오기
	@RequestMapping(value = "/channelLicence.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO channelLicence( HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SystemInfo systemInfo = new SystemInfo();
		JSONObject systemObj = new JSONObject();
		try {
			if (userInfo != null) {
				List<SystemInfo> systemInfoResult = null;
				try {
					systemInfo.setLicEnc("Y");
					systemInfoResult = systemInfoService.selectLicenceInfo(systemInfo);
				} catch(Exception e) {
					systemInfo.setLicEnc("N");
					systemInfoResult = systemInfoService.selectLicenceInfo(systemInfo);
				}
				
				if (systemInfoResult.size() >= 0) {
					for(SystemInfo sysInfo : systemInfoResult) {
						systemObj = new JSONObject();
						systemObj.put("sysId", sysInfo.getSysId());
						systemObj.put("sysName", sysInfo.getSysName());
						// 라이센스가 아직 등록 안된경우에는 0인걸로
						if(StringUtil.isNull(sysInfo.getSysLicence(), true)) {
							systemObj.put("sysLicence", "0");
						}else {
							systemObj.put("sysLicence", sysInfo.getSysLicence());
						}
						jRes.addAttribute(sysInfo.getSysId(), systemObj);
					}
					jRes.setResult("SUCCESS");
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setResult("NODATA");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("NULL ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	   
	// 채널 사용현황 불러오기
	@RequestMapping(value = "/channelUsage.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO channelUsage( HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		ChannelInfo channelInfo = new ChannelInfo();
		try {
			if (userInfo != null) {
				
				if(!StringUtil.isNull(request.getParameter("sysCode"), true)) {
					channelInfo.setSysCode(request.getParameter("sysCode"));
				}else if(!StringUtil.isNull(request.getParameter("sysName"), true)) {						
					SystemInfo systemInfo2 = new SystemInfo();
					List<SystemInfo> systemInfoResult2 = systemInfoService.selectSystemInfo(systemInfo2);
					channelInfo.setSysCode(new FindOrganizationUtil().getSysCode(request.getParameter("sysName"),systemInfoResult2));
				}
				
				if(!StringUtil.isNull(channelInfo.getSysCode(), true)) {
					Integer channelInfoResult = channelInfoService.checkIpOverlap(channelInfo);
					jRes.addAttribute("sysCode", channelInfo.getSysCode());
					jRes.setResult(channelInfoResult.toString());
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else {
					jRes.setResult("SYSTEMCODE IS NULL");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
				
			} else {
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} catch (NullPointerException e) {
			jRes.setResult("NULL ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		} catch (Exception e) {
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 옵션 사용 여부 변경
	@RequestMapping(value = "/updateOptionValue.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO updateOptionValue( HttpServletRequest request) {
		AJaxResVO jRes = new AJaxResVO();
		
		try {
		   LoginVO userInfo = SessionManager.getUserInfo(request);
		   if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			   
			   	if (!StringUtil.isNull(request.getParameter("groupKey"),true))
	   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
	   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
	   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));
	   			if (!StringUtil.isNull(request.getParameter("configValue"),true))
	   				etcConfigInfo.setConfigValue(request.getParameter("configValue"));

				
				Integer updateOptionValue = etcConfigInfoService.updateOptionValue(etcConfigInfo);

	   			if (updateOptionValue == 1) {
	   				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
	   			} else {
	   				jRes.setSuccess(AJaxResVO.SUCCESS_N);
	   			}
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "LoginFail");
			}
		} catch(NullPointerException e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
	   } catch(Exception e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
	   }
		return jRes;
	}
	
	// 옵션 사용 여부 값 가져오기
	@RequestMapping(value = "/selectOptionYN.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO selectOptionYN(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		
		try {
		   LoginVO userInfo = SessionManager.getUserInfo(request);
		   if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			   
			   	if (!StringUtil.isNull(request.getParameter("groupKey"),true))
	   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
	   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
	   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));

				
				EtcConfigInfo selectOptionYN = etcConfigInfoService.selectOptionYN(etcConfigInfo);
				
			
				if(selectOptionYN != null) {
					String configValue = selectOptionYN.getConfigValue();
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("configValue", configValue);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "LoginFail");
			}
		} catch(NullPointerException e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		} catch(Exception e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		}
		return jRes;
	}
	
	//prefix
	@RequestMapping(value = "/addPrefixVal.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO addPrefixVal(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		
		try {
		   LoginVO userInfo = SessionManager.getUserInfo(request);
		   if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			   
			   	if (!StringUtil.isNull(request.getParameter("groupKey"),true))
	   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
	   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
	   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));
				
				EtcConfigInfo selectPrefixList = etcConfigInfoService.selectOptionInfo(etcConfigInfo);
				String prefixValue = selectPrefixList.getConfigValue();
				
				Integer prefixResult = 0;
				String tempConfigValue = "";
				boolean chkDupliValue = false;	// 입력받은 값이 이미 설정된 값인지 체크 - false이면 중복 없음
				
				if(selectPrefixList != null) {
					String[] arrConfigValue = selectPrefixList.getConfigValue().split(",");	// 구분자 ","로 저장된 값을 분해
					
					for (int i = 0; i < arrConfigValue.length; i++) {	// 입력받은 값과 하나씩 비교
						if (arrConfigValue[i].equals(request.getParameter("configValue"))) {
							chkDupliValue = true;	// 이미 저장된 값이면
							break;
						}
					}
					if (!chkDupliValue) { // chkDupliValue == false;
						tempConfigValue =  prefixValue + "," + request.getParameter("configValue");
						if(prefixValue.equals("")) {
							tempConfigValue = tempConfigValue.substring(1);
						}
		   				etcConfigInfo.setConfigValue(tempConfigValue);
						prefixResult = etcConfigInfoService.updatePrefixInfo(etcConfigInfo);
					}	
				}	
				if(prefixResult>0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					if (chkDupliValue) {
						jRes.addAttribute("msg", "duplicationValue");						
					}
				}
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "LoginFail");
			}
		} catch(NullPointerException e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		} catch(Exception e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		}
		return jRes;
	}
		
		
	//fault ?젙蹂? ?궘?젣
	@RequestMapping(value = "/delPrefixVal.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO delPrefixVal(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
	    EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
						
		if(userInfo != null) {
			String[] val = request.getParameter("idx").split(",");
			
			if (!StringUtil.isNull(request.getParameter("groupKey"),true))
   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));
			
			EtcConfigInfo selectPrefixList = etcConfigInfoService.selectOptionInfo(etcConfigInfo);
			String configValue = selectPrefixList.getConfigValue();
			
			String[] arrConfigValue;
			String tempConfigValue = "";
			
			if (selectPrefixList != null) {
				arrConfigValue =  configValue.split(",");	
				
				
				for (int i = 0; i < val.length; i++) {
					for (int j = 0; j < arrConfigValue.length; j++) {
						if (val[i].equals(arrConfigValue[j])) {
							arrConfigValue[j] = "";						
						}
					}
				}
				for (int i = 0; i < arrConfigValue.length; i++) {
					if(!arrConfigValue[i].equals("")) {
						tempConfigValue += "," + arrConfigValue[i];
					}
				}
			}
			if (!tempConfigValue.equals("")) {
				tempConfigValue = tempConfigValue.substring(1);
			}
			etcConfigInfo.setConfigValue(tempConfigValue);
			
			Integer prefixResult = etcConfigInfoService.updatePrefixInfo(etcConfigInfo);
			
			if(prefixResult>0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "LoginFail");
		}
		return jRes;
	}
	
	
	
	//select pNum Masking Value
	@RequestMapping(value = "/selectMaskingInfo.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO selectMaskingInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		
		try {
		   LoginVO userInfo = SessionManager.getUserInfo(request);
		   if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			   
			   	if (!StringUtil.isNull(request.getParameter("groupKey"),true))
	   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
	   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
	   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));

				
				EtcConfigInfo selectPrefixList = etcConfigInfoService.selectOptionInfo(etcConfigInfo);
			
				if(selectPrefixList != null) {
					String startIdx = selectPrefixList.getConfigValue().split(",")[0];
					String maskingEA = selectPrefixList.getConfigValue().split(",")[1];
					jRes.addAttribute("startIdx", startIdx);
					jRes.addAttribute("maskingEA", maskingEA);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "LoginFail");
			}
		} catch(NullPointerException e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		} catch(Exception e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		}
		return jRes;
	}
	
	
	
	
	
	
	
	//select pNum Masking Value
	@RequestMapping(value = "/selectHyphenInfo.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO selectHyphenInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		
		try {
		   LoginVO userInfo = SessionManager.getUserInfo(request);
		   if(userInfo != null && "admin".equals(userInfo.getUserId())) {
			   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			   
			   	if (!StringUtil.isNull(request.getParameter("groupKey"),true))
	   				etcConfigInfo.setGroupKey(request.getParameter("groupKey"));
	   			if (!StringUtil.isNull(request.getParameter("configKey"),true))
	   				etcConfigInfo.setConfigKey(request.getParameter("configKey"));

				
				EtcConfigInfo selectPrefixList = etcConfigInfoService.selectOptionInfo(etcConfigInfo);
			
				if(selectPrefixList != null) {
					String hyphen1 = selectPrefixList.getConfigValue().split(",")[0];
					String hyphen2 = selectPrefixList.getConfigValue().split(",")[1];
					jRes.addAttribute("hyphen1", hyphen1);
					jRes.addAttribute("hyphen2", hyphen2);
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "LoginFail");
			}
		} catch(NullPointerException e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		} catch(Exception e) {
		   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		   jRes.setResult("0");

		   logService.writeLog(request, "ETCCONFIG", "EXCEPTION", "세션이 끊겼습니다.");
		}
		return jRes;
	}
	
	// 20200417 jbs 자번호 관리 - 시스템 설정
		@RequestMapping(value = "/subNumber_proc.do", produces = "text/plain;charset=UTF-8")
		public @ResponseBody AJaxResVO subNumber_proc(HttpServletRequest request, Locale local, Model model) {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			try {
				if(userInfo != null) {

					// 세션에서 권한 정보 가져오기
					@SuppressWarnings("unchecked")
					List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
					MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.subNumber");

					if(request.getParameter("proc") != null ) {
						if(request.getParameter("proc").equals("insert") && request.getParameter("telNo") != null && request.getParameter("nickName") != null && request.getParameter("use") != null) {

			               SubNumberInfo subNumberInfo = new SubNumberInfo();

			               subNumberInfo.setTelNo(request.getParameter("telNo"));
			               subNumberInfo.setNickName(request.getParameter("nickName"));
			               subNumberInfo.setUse(request.getParameter("use"));

			               if ("Y".equals(nowAccessInfo.getWriteYn())) {

			               Integer subNumberInfoResult = subNumberInfoService.insertSubNumberInfo(subNumberInfo);
				               if(subNumberInfoResult == 1) {
				                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				                  jRes.setResult("1");
				                  jRes.addAttribute("msg", "subNumber insert success");

			                  logService.writeLog(request, "SUBNUMBER", "INSERT-SUCCESS", subNumberInfo.toLogString(messageSource));
			               } else {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "subNumber insert fail");

				                  logService.writeLog(request, "SUBNUMBER", "INSERT-FAIL", subNumberInfo.toLogString(messageSource));
				               }
			               }else {
			            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
				                  jRes.setResult("0");
				                  jRes.addAttribute("msg", "subNumber insert fail");

				                  logService.writeLog(request, "SUBNUMBER", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
			               }
			            } else if(request.getParameter("proc").equals("modify") && request.getParameter("telNo") != null && request.getParameter("nickName") != null && request.getParameter("use") != null && request.getParameter("idx") != null) {
			               SubNumberInfo subNumberInfo = new SubNumberInfo();
			               subNumberInfo.setIdx(request.getParameter("idx"));
			               if(request.getParameter("telNo")!= null) subNumberInfo.setTelNo(request.getParameter("telNo"));
			               if(request.getParameter("nickName") != null) subNumberInfo.setNickName(request.getParameter("nickName"));
			               if(request.getParameter("use") != null) subNumberInfo.setUse(request.getParameter("use"));
			               if ("Y".equals(nowAccessInfo.getModiYn())) {

			            	   Integer subNumberInfoResult = subNumberInfoService.updateSubNumberInfo(subNumberInfo);
				               if(subNumberInfoResult == 1) {
				                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				                  jRes.setResult("1");
				                  jRes.addAttribute("msg", "subNumber update success");

				                  logService.writeLog(request, "SUBNUMBER", "UPDATE-SUCCESS", subNumberInfo.toLogString(messageSource));
				               } else {
				                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
				                  jRes.setResult("0");
				                  jRes.addAttribute("msg", "subNumber update fail");

				                  logService.writeLog(request, "SUBNUMBER", "UPDATE-FAIL", subNumberInfo.toLogString(messageSource));
				               }
			               }else {
			            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
				                  jRes.setResult("0");
				                  jRes.addAttribute("msg", "subNumber update fail");

				                  logService.writeLog(request, "SUBNUMBER", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
			               }
			            } else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
			               String[] chList = request.getParameter("chList").split(",");
			               SubNumberInfo subNumberInfo = new SubNumberInfo();

			               if ("Y".equals(nowAccessInfo.getDelYn())) {

				               for(int i = 0; i < chList.length; i++) {
				            	   subNumberInfo.setIdx(chList[i]);

				                  Integer subNumberInfoResult = subNumberInfoService.deleteSubNumberInfo(subNumberInfo);
				                  if(subNumberInfoResult == 1) {
				                     jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				                     jRes.setResult("1");
				                     jRes.addAttribute("msg", "subNumber delete success");

				                     logService.writeLog(request, "SUBNUMBER", "DELETE-SUCCESS", subNumberInfo.toLogString(messageSource));
				                  } else {
				                     jRes.setSuccess(AJaxResVO.SUCCESS_N);
				                     jRes.setResult("0");
				                     jRes.addAttribute("msg", "subNumber delete fail");

				                     logService.writeLog(request, "SUBNUMBER", "DELETE-FAIL", subNumberInfo.toLogString(messageSource));
				                  }
				               }
			               }else {
			            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                     jRes.setResult("0");
			                     jRes.addAttribute("msg", "subNumber delete fail");

			                     logService.writeLog(request, "SUBNUMBER", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
			               }

			            } else {
			               jRes.setSuccess(AJaxResVO.SUCCESS_N);
			               jRes.setResult("0");
			               jRes.addAttribute("msg", "subNumber proc command fail");

			               String cmd = "저장";
			               if ("modify".equals(request.getParameter("proc")))
			            	   cmd = "수정";
			               if ("delete".equals(request.getParameter("proc")))
			            	   cmd = "삭제";

			               logService.writeLog(request, "SUBNUMBER", "COMMAND-FAIL", cmd + " 실패");
			            }
			         } else {
			            jRes.setSuccess(AJaxResVO.SUCCESS_N);
			            jRes.setResult("0");
			            jRes.addAttribute("msg", "proc fail");

			            logService.writeLog(request, "SUBNUMBER", "COMMAND-FAIL", "비 정상적인 요청입니다.");
			         }
			      } else {
			         jRes.setSuccess(AJaxResVO.SUCCESS_N);
			         jRes.setResult("0");
			         jRes.addAttribute("msg", "login fail");

			         logService.writeLog(request, "SUBNUMBER", "COMMAND-FAIL", "세션이 끊겼습니다.");
			      }
			}catch (NullPointerException e) {
				logger.error("error",e);
				logService.writeLog(request, "SUBNUMBER", "EXCEPTION", e.getMessage());
			}catch (Exception e) {
				logger.error("error",e);
				logService.writeLog(request, "SUBNUMBER", "EXCEPTION", e.getMessage());
			}
			return jRes;
		}
		
	// 20200417 jbs 자번호 관리 - 시스템 설정
	@RequestMapping(value = "/phoneMapping_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO phoneMapping_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.phoneMapping");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("custPhone") != null && request.getParameter("custNickName") != null && request.getParameter("useNickName") != null) {
						
						PhoneMappingInfo phoneMappingInfo = new PhoneMappingInfo();
						
						phoneMappingInfo.setCustPhone(request.getParameter("custPhone"));
						phoneMappingInfo.setCustNickName(request.getParameter("custNickName"));
						phoneMappingInfo.setUseNickName(request.getParameter("useNickName"));
						phoneMappingInfo.setProcType(request.getParameter("procType"));
						phoneMappingInfo.setProcPosition(request.getParameter("procPosition"));
						
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							
							Integer phoneMappingInfoResult = phoneMappingInfoService.insertPhoneMappingInfo(phoneMappingInfo);
							if(phoneMappingInfoResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "phoneMapping insert success");
								
								logService.writeLog(request, "PHONEMAPPING", "INSERT-SUCCESS", phoneMappingInfo.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "phoneMapping insert fail");
								
								logService.writeLog(request, "PHONEMAPPING", "INSERT-FAIL", phoneMappingInfo.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "phoneMapping insert fail");
							
							logService.writeLog(request, "PHONEMAPPING", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("custPhone") != null && request.getParameter("custNickName") != null && request.getParameter("useNickName") != null) {
						PhoneMappingInfo phoneMappingInfo = new PhoneMappingInfo();
						if(request.getParameter("custPhone")!= null) phoneMappingInfo.setCustPhone(request.getParameter("custPhone"));
						if(request.getParameter("custNickName") != null) phoneMappingInfo.setCustNickName(request.getParameter("custNickName"));
						if(request.getParameter("useNickName") != null) phoneMappingInfo.setUseNickName(request.getParameter("useNickName"));
						phoneMappingInfo.setCustPhoneKey(request.getParameter("custPhoneKey"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							
							Integer phoneMappingInfoResult = phoneMappingInfoService.updatePhoneMappingInfo(phoneMappingInfo);
							if(phoneMappingInfoResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "phoneMapping update success");
								
								logService.writeLog(request, "PHONEMAPPING", "UPDATE-SUCCESS", phoneMappingInfo.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "phoneMapping update fail");
								
								logService.writeLog(request, "PHONEMAPPING", "UPDATE-FAIL", phoneMappingInfo.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "phoneMapping update fail");
							
							logService.writeLog(request, "PHONEMAPPING", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						PhoneMappingInfo phoneMappingInfo = new PhoneMappingInfo();
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							
							for(int i = 0; i < chList.length; i++) {
								phoneMappingInfo.setCustPhone(chList[i]);
								
								Integer phoneMappingInfoResult = phoneMappingInfoService.deletePhoneMappingInfo(phoneMappingInfo);
								if(phoneMappingInfoResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "phoneMapping delete success");
									
									logService.writeLog(request, "PHONEMAPPING", "DELETE-SUCCESS", phoneMappingInfo.toLogString(messageSource));
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "phoneMapping delete fail");
									
									logService.writeLog(request, "PHONEMAPPING", "DELETE-FAIL", phoneMappingInfo.toLogString(messageSource));
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "phoneMapping delete fail");
							
							logService.writeLog(request, "PHONEMAPPING", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "phoneMapping proc command fail");
						
						String cmd = "저장";
						if ("modify".equals(request.getParameter("proc")))
							cmd = "수정";
						if ("delete".equals(request.getParameter("proc")))
							cmd = "삭제";
						
						logService.writeLog(request, "PHONEMAPPING", "COMMAND-FAIL", cmd + " 실패");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "PHONEMAPPING", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "PHONEMAPPING", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "SUBNUMBER", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "SUBNUMBER", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	// 20200616 jbs 내부망 관리 - 시스템 설정
	@RequestMapping(value = "/publicIp_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO publicIp_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.publicIp");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("rPublicIp") != null) {
						PublicIpInfo publicIpInfo = new PublicIpInfo();
						publicIpInfo.setrPublicIp(request.getParameter("rPublicIp"));
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							
							Integer publicIpInfoResult = publicIpInfoService.insertPublicIpInfo(publicIpInfo);
							if(publicIpInfoResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "publicIpInfo insert success");
								
								logService.writeLog(request, "PUBLICIP", "INSERT-SUCCESS", publicIpInfo.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "publicIpInfo insert fail");
								
								logService.writeLog(request, "PUBLICIP", "INSERT-FAIL", publicIpInfo.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "publicIpInfo insert fail");
							
							logService.writeLog(request, "PUBLICIP", "INSERT-FAIL", "No Authority");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("rPublicIp") != null && request.getParameter("rPublicIpOld") != null) {
						PublicIpInfo publicIpInfo = new PublicIpInfo();
						publicIpInfo.setrPublicIp(request.getParameter("rPublicIp"));
						publicIpInfo.setrPublicIpOld(request.getParameter("rPublicIpOld"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							
							Integer publicIpInfoResult = publicIpInfoService.updatePublicIpInfo(publicIpInfo);
							if(publicIpInfoResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "publicIpInfo update success");
								
								logService.writeLog(request, "PUBLICIP", "UPDATE-SUCCESS", publicIpInfo.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "publicIpInfo update fail");
								
								logService.writeLog(request, "PUBLICIP", "UPDATE-FAIL", publicIpInfo.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "publicIpInfo update fail");
							
							logService.writeLog(request, "PUBLICIP", "UPDATE-FAIL", "No Authority");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						PublicIpInfo publicIpInfo = new PublicIpInfo();
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							
							for(int i = 0; i < chList.length; i++) {
								publicIpInfo.setrPublicIp(chList[i]);
								Integer publicIpInfoResult = publicIpInfoService.deletePublicIpInfo(publicIpInfo);
								if(publicIpInfoResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "publicIpInfo delete success");
									
									logService.writeLog(request, "PUBLICIP", "DELETE-SUCCESS", publicIpInfo.toLogString(messageSource));
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "publicIpInfo delete fail");
									
									logService.writeLog(request, "PUBLICIP", "DELETE-FAIL", publicIpInfo.toLogString(messageSource));
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "publicIpInfo delete fail");
							
							logService.writeLog(request, "PUBLICIP", "DELETE-FAIL", "No Authority");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "publicIpInfo proc command fail");
						
						String cmd = "save";
						if ("modify".equals(request.getParameter("proc")))
							cmd = "modify";
						if ("delete".equals(request.getParameter("proc")))
							cmd = "delete";
						
						logService.writeLog(request, "PUBLICIP", "COMMAND-FAIL", cmd + " fail");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "PUBLICIP", "COMMAND-FAIL", "unusual request");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "PUBLICIP", "COMMAND-FAIL", "The session has been interrupted.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "PUBLICIP", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "PUBLICIP", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	// db 정보 insert, update, delete
	@RequestMapping(value = "/dbInfo_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO dbInfo_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.dbInfo");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("dbName") != null && request.getParameter("dbDriver") != null && request.getParameter("dbUrl") != null && request.getParameter("dbUser") != null && request.getParameter("dbPassword") != null) {
						DBInfo dbInfo = new DBInfo();
						dbInfo.setDbName(request.getParameter("dbName"));
						dbInfo.setDbDriver(request.getParameter("dbDriver"));
						dbInfo.setDbUrl(request.getParameter("dbUrl"));
						dbInfo.setDbUser(request.getParameter("dbUser"));
						dbInfo.setDbPassword(request.getParameter("dbPassword"));
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							Integer dbInfoResult = dbInfoService.insertDBInfo(dbInfo);
							if(dbInfoResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "dbInfo insert success");
								
								logService.writeLog(request, "DBINFO", "INSERT-SUCCESS", dbInfo.toLogString());
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "dbInfo insert fail");
								
								logService.writeLog(request, "DBINFO", "INSERT-FAIL", dbInfo.toLogString());
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "dbInfo insert fail");
							
							logService.writeLog(request, "DBINFO", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("dbName") != null && request.getParameter("dbDriver") != null && request.getParameter("dbUrl") != null && request.getParameter("dbUser") != null && request.getParameter("dbPassword") != null) {
						DBInfo dbInfo = new DBInfo();
						dbInfo.setDbName(request.getParameter("dbName"));
						dbInfo.setDbDriver(request.getParameter("dbDriver"));
						dbInfo.setDbUrl(request.getParameter("dbUrl"));
						dbInfo.setDbUser(request.getParameter("dbUser"));
						dbInfo.setDbPassword(request.getParameter("dbPassword"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							Integer dbInfoResult = dbInfoService.updateDBInfo(dbInfo);
							if(dbInfoResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "dbInfo update success");
								
								logService.writeLog(request, "DBINFO", "UPDATE-SUCCESS", dbInfo.toLogString());
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "dbInfo update fail");
								
								logService.writeLog(request, "DBINFO", "UPDATE-FAIL", dbInfo.toLogString());
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "dbInfo update fail");
							
							logService.writeLog(request, "DBINFO", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						DBInfo dbInfo = new DBInfo();
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							
							for(int i = 0; i < chList.length; i++) {
								dbInfo.setDbName(chList[i]);
								Integer dbInfoResult = dbInfoService.deleteDBInfo(dbInfo);
								if(dbInfoResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "dbInfo delete success");
									
									logService.writeLog(request, "DBINFO", "DELETE-SUCCESS", dbInfo.toLogString());
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "dbInfo delete fail");
									
									logService.writeLog(request, "DBINFO", "DELETE-FAIL", dbInfo.toLogString());
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "dbInfo delete fail");
							
							logService.writeLog(request, "DBINFO", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "dbInfo proc command fail");
						
						String cmd="";
						if ("insert".equals(request.getParameter("proc"))) {cmd = "저장";}
						if ("modify".equals(request.getParameter("proc"))) {cmd = "수정";}
						if ("delete".equals(request.getParameter("proc"))) {cmd = "삭제";}
						
						logService.writeLog(request, "DBINFO", "COMMAND-FAIL", cmd + " 실패");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "DBINFO", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "DBINFO", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "DBINFO", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "DBINFO", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	// DB Info 팝업
	/*
	 * @RequestMapping(value = "/dbInfoPopup") public String
	 * dbInfoPop(HttpServletRequest request, Locale local, Model model) {
	 * 
	 * // DB정보 가져오기 String dbName = request.getParameter("dbName"); DBInfo dbInfo =
	 * new DBInfo(); dbInfo.setDbName(dbName); List<DBInfo> dbInfoResult =
	 * dbInfoService.selectOneDBInfo(dbInfo);
	 * 
	 * // DBConnection String dbDriver = ""; String dbUrl = "";
	 * if(dbInfoResult.get(0).getDbDriver().equals("postgres")) { dbDriver =
	 * "org.postgresql.Driver"; dbUrl =
	 * "jdbc:postgresql://"+dbInfoResult.get(0).getDbUrl(); }// 오라클 mysql mssql 등 추가
	 * 필요 String dbUser = dbInfoResult.get(0).getDbUser(); String dbPassword =
	 * dbInfoResult.get(0).getDbPassword(); // 암호화 처리 추가 필요
	 * 
	 * DBConnectionMgr dbConnectionMgr = DBConnectionMgr.getInstance();
	 * dbConnectionMgr.set_driver(dbDriver); dbConnectionMgr.set_url(dbUrl);
	 * dbConnectionMgr.set_user(dbUser); dbConnectionMgr.set_password(dbPassword);
	 * 
	 * // 선택한 DB에서 테이블 정보 가져오기 DBSyncDAO dbSyncDAO = new DBSyncDAO();
	 * ArrayList<SchemaInfo> tableResult = new ArrayList(); try { tableResult =
	 * dbSyncDAO.selectTableName(dbConnectionMgr,
	 * dbInfoResult.get(0).getDbDriver()); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * DBSQLInfo dbSQLInfo = new DBSQLInfo(); dbSQLInfo.setDbName(dbName);
	 * 
	 * List<DBSQLInfo> queryResult = dbSQLInfoService.selectDBSQLInfo(dbSQLInfo);
	 * 
	 * model.addAttribute("dbName", dbName); model.addAttribute("tableResultList",
	 * tableResult); model.addAttribute("queryResultList", queryResult);
	 * 
	 * return "/admin/db_infoPopup"; }
	 */
	
	// column 정보 가져오기
//	@ResponseBody
//	@RequestMapping(value = "/selectColumnProc")
//	public ArrayList<SchemaInfo> selectColumnProc(HttpServletRequest request, Locale local, Model model) {
//		
//		// DB정보 가져오기
//		String dbName = request.getParameter("dbName");
//		DBInfo dbInfo = new DBInfo();
//		dbInfo.setDbName(dbName);
//		List<DBInfo> dbInfoResult = dbInfoService.selectOneDBInfo(dbInfo);
//		
//		// DBConnection		
//		String dbDriver = "";
//		String dbUrl = "";
//		if(dbInfoResult.get(0).getDbDriver().equals("postgres")) {
//			dbDriver = "org.postgresql.Driver";
//			dbUrl = "jdbc:postgresql://"+dbInfoResult.get(0).getDbUrl();
//		}// 오라클 mysql mssql 등 추가 필요
//		String dbUser = dbInfoResult.get(0).getDbUser();
//		String dbPassword = dbInfoResult.get(0).getDbPassword(); // 암호화 처리 추가 필요
//		
//		DBConnectionMgr dbConnectionMgr = DBConnectionMgr.getInstance();
//		dbConnectionMgr.set_driver(dbDriver);
//		dbConnectionMgr.set_url(dbUrl);
//		dbConnectionMgr.set_user(dbUser);
//		dbConnectionMgr.set_password(dbPassword);
//		
//		// 선택한 Table에서 column정보 가져오기
//		DBSyncDAO dbSyncDAO = new DBSyncDAO();
//		ArrayList<SchemaInfo> columnResult = new ArrayList();
//		try {
//			columnResult = dbSyncDAO.selectColumnName(dbConnectionMgr, dbInfoResult.get(0).getDbDriver(), request.getParameter("table_name"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return columnResult;
//	}
	
	// sql maker(select)
	@ResponseBody
	@RequestMapping("selectSQLContentProc")
	public String selectSQLContentProc(HttpServletRequest request, Locale local, Model model) {
		String dbName = request.getParameter("dbName");
		DBInfo dbInfo = new DBInfo();
		dbInfo.setDbName(dbName);
		List<DBInfo> dbInfoResult = dbInfoService.selectOneDBInfo(dbInfo);
		String dbDriver = dbInfoResult.get(0).getDbDriver();
		String sql = "";
		if(dbDriver.equals("postgres")) {
			PostgresqlQuery pq = new PostgresqlQuery();
			sql = pq.select(XssFilterUtil.XssFilter(request.getParameter("column_list")), XssFilterUtil.XssFilter(request.getParameter("table_name")));
		}//오라클 mssql mysql 추가해야함
		return sql;
	}
	// sql maker(upsert)
	@ResponseBody
	@RequestMapping("upsertSQLContentProc")
	public String upsertSQLContentProc(HttpServletRequest request, Locale local, Model model) {
		String dbName = request.getParameter("dbName");
		DBInfo dbInfo = new DBInfo();
		dbInfo.setDbName(dbName);
		List<DBInfo> dbInfoResult = dbInfoService.selectOneDBInfo(dbInfo);
		String dbDriver = dbInfoResult.get(0).getDbDriver();
		String sql = "";
		if(dbDriver.equals("postgres")) {
			PostgresqlQuery pq = new PostgresqlQuery();
			sql = pq.upsert(XssFilterUtil.XssFilter(request.getParameter("column_list_key")), XssFilterUtil.XssFilter(request.getParameter("column_list_normal")), XssFilterUtil.XssFilter(request.getParameter("table_name")));
		}//오라클 mssql mysql 추가해야함
		return sql;
	}
	
	// select SQL 정보 저장하기
	@ResponseBody
	@RequestMapping(value = "/selectSQLProc")
	public String selectSQLProc(HttpServletRequest request, Locale local, Model model) {
		
		PostgresqlQuery pq = new PostgresqlQuery();
		String sql = pq.select(request.getParameter("column_list"), request.getParameter("table_name"));
		String column_list = request.getParameter("column_list").replace("\"", "").replace("[", "").replace("]", "");
		DBSQLInfo dbSQLInfo = new DBSQLInfo();
		dbSQLInfo.setDbSQLName(request.getParameter("sqlName"));
		dbSQLInfo.setDbSQLDescription(request.getParameter("sqlDescription"));
		dbSQLInfo.setDbSQLContent(sql);
		dbSQLInfo.setDbSQLType("select");
		dbSQLInfo.setDbName(request.getParameter("dbName"));
		dbSQLInfo.setDbSQLColumn(column_list);
		
		dbSQLInfoService.insertDBSQLInfo(dbSQLInfo);
		
		return null;
	}
	
	// upsert SQL 정보 저장하기
	@ResponseBody
	@RequestMapping(value = "/upsertSQLProc")
	public String insertSQLProc(HttpServletRequest request, Locale local, Model model) {
		// 오라클 mysql mssql 등 추가 필요
		PostgresqlQuery pq = new PostgresqlQuery();
		String sql = pq.upsert(request.getParameter("column_list_key"), request.getParameter("column_list_normal"), request.getParameter("table_name"));
		String column_list = request.getParameter("column_list_key").replace("\"", "").replace("[", "").replace("]", "");
		if(!("[]".equals(request.getParameter("column_list_normal")))) {
			column_list +=  ","+request.getParameter("column_list_normal").replace("\"", "").replace("[", "").replace("]", "");
		}
		
		DBSQLInfo dbSQLInfo = new DBSQLInfo();
		dbSQLInfo.setDbSQLName(request.getParameter("sqlName"));
		dbSQLInfo.setDbSQLDescription(request.getParameter("sqlDescription"));
		dbSQLInfo.setDbSQLContent(sql);
		dbSQLInfo.setDbSQLType("upsert");
		dbSQLInfo.setDbName(request.getParameter("dbName"));
		dbSQLInfo.setDbSQLColumn(column_list);
		
		dbSQLInfoService.insertDBSQLInfo(dbSQLInfo);
		
		return null;
	}
	
	// query 리스트 가져오기
	@ResponseBody
	@RequestMapping("queryListProc")
	public HashMap<String, List> queryListProc(HttpServletRequest request, Locale local, Model model) {
		DBSQLInfo dbSQLInfo = new DBSQLInfo();
		List<DBSQLInfo> queryResult = dbSQLInfoService.selectDBSQLInfo(dbSQLInfo);
		List<DBSQLInfo> queryResult2 = dbSQLInfoService.selectDBSQLInfo2(dbSQLInfo);
		HashMap<String, List> result = new HashMap<>();
		result.put("select", queryResult);
		result.put("upsert", queryResult2);
		return result;
	}
	
	// select, upsert 상세정보 뿌리기
	@ResponseBody
	@RequestMapping(value = "/queryProc")
	public DBSQLInfo queryProc(HttpServletRequest request, Locale local, Model model) {
		
		// 오류 및 로그처리 추가해야함
		DBSQLInfo dbSQLInfo = new DBSQLInfo();
		dbSQLInfo.setDbSQLName(request.getParameter("dbSQLName"));
		DBSQLInfo dbSQLInfoResult = null;
		if(request.getParameter("proc") != null ) {
			if(request.getParameter("proc").equals("select") && request.getParameter("dbSQLName") != null) {
				dbSQLInfoResult = dbSQLInfoService.selectOneDBSQLInfo(dbSQLInfo);
			}else if(request.getParameter("proc").equals("upsert") && request.getParameter("dbSQLName") != null) {
				dbSQLInfoResult = dbSQLInfoService.selectOneDBSQLInfo(dbSQLInfo);
			}
		}
		
		return dbSQLInfoResult;
	}
	
	//@ResponseBody
	//@RequestMapping("excuteQueryProc")
//	public ArrayList<SelectQueryInfo> excuteQueryProc(HttpServletRequest request, Locale local, Model model) {
//		
//		// 각 쿼리 불러오기 (select, upsert)
//		DBSQLInfo dbSQLInfo = new DBSQLInfo();
//		dbSQLInfo.setDbSQLName(request.getParameter("selectQueryName"));
//		DBSQLInfo selectQuery = dbSQLInfoService.selectOneDBSQLInfo(dbSQLInfo);
//		dbSQLInfo.setDbSQLName(request.getParameter("upsertQueryName"));
//		DBSQLInfo upsertQuery = dbSQLInfoService.selectOneDBSQLInfo(dbSQLInfo);
//		
//		
//		// Select DB정보 가져오기
//		String selectDBName = selectQuery.getDbName();
//		DBInfo selectDBInfo = new DBInfo();
//		selectDBInfo.setDbName(selectDBName);
//		List<DBInfo> selectDBInfoResult = dbInfoService.selectOneDBInfo(selectDBInfo);
//		
//		// DBConnection		
//		String selectDBDriver = "";
//		String selectDBUrl = "";
//		if(selectDBInfoResult.get(0).getDbDriver().equals("postgres")) {
//			selectDBDriver = "org.postgresql.Driver";
//			selectDBUrl = "jdbc:postgresql://"+selectDBInfoResult.get(0).getDbUrl();
//		}// 오라클 mysql mssql 등 추가 필요
//		String selectDBUser = selectDBInfoResult.get(0).getDbUser();
//		String selectDBPassword = selectDBInfoResult.get(0).getDbPassword(); // 암호화 처리 추가 필요
//		
//		DBConnectionMgr selectDBConnectionMgr = DBConnectionMgr.getInstance();
//		selectDBConnectionMgr.set_driver(selectDBDriver);
//		selectDBConnectionMgr.set_url(selectDBUrl);
//		selectDBConnectionMgr.set_user(selectDBUser);
//		selectDBConnectionMgr.set_password(selectDBPassword); 
//		
//		// select 실행한 정보 가져오기
//		DBSyncDAO dbSyncDAO = new DBSyncDAO();
//		ArrayList<SelectQueryInfo> selectQueryResult = new ArrayList();
//		try {
//			selectQueryResult = dbSyncDAO.selectQueryResult(selectDBConnectionMgr, selectDBInfoResult.get(0).getDbDriver(), selectQuery.getDbSQLContent(), upsertQuery.getDbSQLColumn());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// Upsert DB정보 가져오기
//		String upsertDBName = upsertQuery.getDbName();
//		DBInfo upsertDBInfo = new DBInfo();
//		upsertDBInfo.setDbName(upsertDBName);
//		List<DBInfo> upsertDBInfoResult = dbInfoService.selectOneDBInfo(upsertDBInfo);
//		
//		// DBConnection		
//		String upsertDBDriver = "";
//		String upsertDBUrl = "";
//		if(upsertDBInfoResult.get(0).getDbDriver().equals("postgres")) {
//			upsertDBDriver = "org.postgresql.Driver";
//			upsertDBUrl = "jdbc:postgresql://"+upsertDBInfoResult.get(0).getDbUrl();
//		}// 오라클 mysql mssql 등 추가 필요
//		String upsertDBUser = upsertDBInfoResult.get(0).getDbUser();
//		String upsertDBPassword = upsertDBInfoResult.get(0).getDbPassword(); // 암호화 처리 추가 필요
//		
//		DBConnectionMgr upsertDBConnectionMgr = DBConnectionMgr.getInstance();
//		upsertDBConnectionMgr.set_driver(upsertDBDriver);
//		upsertDBConnectionMgr.set_url(upsertDBUrl);
//		upsertDBConnectionMgr.set_user(upsertDBUser);
//		upsertDBConnectionMgr.set_password(upsertDBPassword); 
//		
//		// upsert 실행하기
//		/*ArrayList<UpsertQueryInfo> upsertQueryResult = new ArrayList();*/
//		try {
//			dbSyncDAO.upsertQueryResult(upsertDBConnectionMgr, upsertDBInfoResult.get(0).getDbDriver(), upsertQuery.getDbSQLContent(), selectQueryResult);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		return null;
//	}
	
	// dbManage 정보 insert, update, delete
//	@RequestMapping(value = "/dbManage_proc.do", produces = "text/plain;charset=UTF-8")
//	public @ResponseBody AJaxResVO dbManage_proc(HttpServletRequest request, Locale local, Model model) {
//		AJaxResVO jRes = new AJaxResVO();
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		
//		try {
//			if(userInfo != null) {
//				
//				// 세션에서 권한 정보 가져오기
//				@SuppressWarnings("unchecked")
//				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
//				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
//				
//				if(request.getParameter("proc") != null ) {
//					if(request.getParameter("proc").equals("insert") && request.getParameter("dbName") != null && request.getParameter("dbServer") != null && request.getParameter("url") != null && request.getParameter("id") != null && request.getParameter("pw") != null && request.getParameter("timeout") != null) {
//						DBManage dbManage = new DBManage();
//						dbManage.setDbName(request.getParameter("dbName"));
//						dbManage.setDbServer(request.getParameter("dbServer"));
//						dbManage.setUrl(request.getParameter("url"));
//						dbManage.setId(request.getParameter("id"));
//						dbManage.setPw(request.getParameter("pw"));
//						dbManage.setTimeout(request.getParameter("timeout"));
//						if ("Y".equals(nowAccessInfo.getWriteYn())) {
//							Integer dbManageResult = dbManageService.insertDBManage(dbManage);
//							if(dbManageResult == 1) {
//								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//								jRes.setResult("1");
//								jRes.addAttribute("msg", "dbManage insert success");
//								
//								logService.writeLog(request, "DBMANAGE", "INSERT-SUCCESS", dbManage.toLogString(messageSource));
//							} else {
//								jRes.setSuccess(AJaxResVO.SUCCESS_N);
//								jRes.setResult("0");
//								jRes.addAttribute("msg", "dbManage insert fail");
//								
//								logService.writeLog(request, "DBMANAGE", "INSERT-FAIL", dbManage.toLogString(messageSource));
//							}
//						}else {
//							jRes.setSuccess(AJaxResVO.SUCCESS_N);
//							jRes.setResult("0");
//							jRes.addAttribute("msg", "dbManage insert fail");
//							
//							logService.writeLog(request, "DBMANAGE", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
//						}
//					} else if(request.getParameter("proc").equals("modify") && request.getParameter("dbName") != null && request.getParameter("dbServer") != null && request.getParameter("url") != null && request.getParameter("id") != null && request.getParameter("pw") != null && request.getParameter("timeout") != null) {
//						DBManage dbManage = new DBManage();
//						dbManage.setDbName(request.getParameter("dbName"));
//						dbManage.setDbServer(request.getParameter("dbServer"));
//						dbManage.setUrl(request.getParameter("url"));
//						dbManage.setId(request.getParameter("id"));
//						dbManage.setPw(request.getParameter("pw"));
//						dbManage.setTimeout(request.getParameter("timeout"));
//						if ("Y".equals(nowAccessInfo.getModiYn())) {
//							Integer dbManageResult = dbManageService.updateDBManage(dbManage);
//							if(dbManageResult == 1) {
//								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//								jRes.setResult("1");
//								jRes.addAttribute("msg", "dbManage update success");
//								
//								logService.writeLog(request, "DBMANAGE", "UPDATE-SUCCESS", dbManage.toLogString(messageSource));
//							} else {
//								jRes.setSuccess(AJaxResVO.SUCCESS_N);
//								jRes.setResult("0");
//								jRes.addAttribute("msg", "dbManage update fail");
//								
//								logService.writeLog(request, "DBMANAGE", "UPDATE-FAIL", dbManage.toLogString(messageSource));
//							}
//						}else {
//							jRes.setSuccess(AJaxResVO.SUCCESS_N);
//							jRes.setResult("0");
//							jRes.addAttribute("msg", "dbManage update fail");
//							
//							logService.writeLog(request, "DBMANAGE", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
//						}
//					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
//						String[] chList = request.getParameter("chList").split(",");
//						DBManage dbManage = new DBManage();
//						JobManage jobManage = new JobManage();
//						ExecuteManage executeManage = new ExecuteManage();
//						List<JobManage> jobList = jobManageService.selectJobManage(jobManage);
//						
//						if ("Y".equals(nowAccessInfo.getDelYn())) {
//							
//							for(int i = 0; i < chList.length; i++) {
//								ArrayList<String> jobNameList = new ArrayList<>();
//								
//								dbManage.setDbName(chList[i]);
//								Integer dbManageResult = dbManageService.deleteDBManage(dbManage);
//								
//								for (int j = 0; j < jobList.size(); j++) {
//									String[] dbList =  jobList.get(j).getDbName().split("/");
//									loop : for (int k = 0; k < dbList.length; k++) {
//										if(dbList[k].equals(chList[i])) {
//											jobNameList.add(jobList.get(j).getJobName());
//											break loop;
//										}
//									}
//								}
//								
//								for (int j = 0; j < jobNameList.size(); j++) {
//									jobManage.setJobName(jobNameList.get(j));
//									jobManageService.deleteJobManage(jobManage);
//									executeManage.setJobName(jobNameList.get(j));
//									executeManageService.deleteContainExecuteManage(executeManage);
//								}
//								
//								if(dbManageResult == 1) {
//									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//									jRes.setResult("1");
//									jRes.addAttribute("msg", "dbManage delete success");
//									
//									logService.writeLog(request, "DBMANAGE", "DELETE-SUCCESS", dbManage.toLogString(messageSource));
//								} else {
//									jRes.setSuccess(AJaxResVO.SUCCESS_N);
//									jRes.setResult("0");
//									jRes.addAttribute("msg", "dbManage delete fail");
//									
//									logService.writeLog(request, "DBMANAGE", "DELETE-FAIL", dbManage.toLogString(messageSource));
//								}
//							}
//						}else {
//							jRes.setSuccess(AJaxResVO.SUCCESS_N);
//							jRes.setResult("0");
//							jRes.addAttribute("msg", "dbManage delete fail");
//							
//							logService.writeLog(request, "DBMANAGE", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
//						}
//						
//					} else {
//						jRes.setSuccess(AJaxResVO.SUCCESS_N);
//						jRes.setResult("0");
//						jRes.addAttribute("msg", "dbManage proc command fail");
//						
//						String cmd="";
//						if ("insert".equals(request.getParameter("proc"))) {cmd = "저장";}
//						if ("modify".equals(request.getParameter("proc"))) {cmd = "수정";}
//						if ("delete".equals(request.getParameter("proc"))) {cmd = "삭제";}
//						
//						logService.writeLog(request, "DBMANAGE", "COMMAND-FAIL", cmd + " 실패");
//					}
//				} else {
//					jRes.setSuccess(AJaxResVO.SUCCESS_N);
//					jRes.setResult("0");
//					jRes.addAttribute("msg", "proc fail");
//					
//					logService.writeLog(request, "DBMANAGE", "COMMAND-FAIL", "비 정상적인 요청입니다.");
//				}
//			} else {
//				jRes.setSuccess(AJaxResVO.SUCCESS_N);
//				jRes.setResult("0");
//				jRes.addAttribute("msg", "login fail");
//				
//				logService.writeLog(request, "DBMANAGE", "COMMAND-FAIL", "세션이 끊겼습니다.");
//			}
//		}catch (NullPointerException e) {
//			e.printStackTrace();
//			logService.writeLog(request, "DBMANAGE", "EXCEPTION", e.getMessage());
//		}catch (Exception e) {
//			e.printStackTrace();
//			logService.writeLog(request, "DBMANAGE", "EXCEPTION", e.getMessage());
//		}
//		return jRes;
//	}
	
	// sqlManage 정보 insert, update, delete
	@RequestMapping(value = "/sqlManage_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO sqlManage_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("sqlName") != null && request.getParameter("sql") != null) {
						SQLManage sqlManage = new SQLManage();
						sqlManage.setSqlName(request.getParameter("sqlName"));
						sqlManage.setSql(request.getParameter("sql"));
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							Integer sqlManageResult = sqlManageService.insertSQLManage(sqlManage);
							if(sqlManageResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "sqlManage insert success");
								
								logService.writeLog(request, "SQLMANAGE", "INSERT-SUCCESS", sqlManage.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "sqlManage insert fail");
								
								logService.writeLog(request, "SQLMANAGE", "INSERT-FAIL", sqlManage.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "sqlManage insert fail");
							
							logService.writeLog(request, "SQLMANAGE", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("sqlName") != null && request.getParameter("sql") != null) {
						SQLManage sqlManage = new SQLManage();
						sqlManage.setSqlName(request.getParameter("sqlName"));
						sqlManage.setSql(request.getParameter("sql"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							Integer sqlManageResult = sqlManageService.updateSQLManage(sqlManage);
							if(sqlManageResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "sqlManage update success");
								
								logService.writeLog(request, "SQLMANAGE", "UPDATE-SUCCESS", sqlManage.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "sqlManage update fail");
								
								logService.writeLog(request, "SQLMANAGE", "UPDATE-FAIL", sqlManage.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "sqlManage update fail");
							
							logService.writeLog(request, "SQLMANAGE", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						SQLManage sqlManage = new SQLManage();
						JobManage jobManage = new JobManage();
						ExecuteManage executeManage = new ExecuteManage();
						List<JobManage> jobList = jobManageService.selectJobManage(jobManage);
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							
							for(int i = 0; i < chList.length; i++) {
								ArrayList<String> jobNameList = new ArrayList<>();
								
								sqlManage.setSqlName(chList[i]);
								Integer sqlManageResult = sqlManageService.deleteSQLManage(sqlManage);
								
								for (int j = 0; j < jobList.size(); j++) {
									String[] sqlList =  jobList.get(j).getSqlName().split("/");
									loop : for (int k = 0; k < sqlList.length; k++) {
										if(sqlList[k].equals(chList[i])) {
											jobNameList.add(jobList.get(j).getJobName());
											break loop;
										}
									}
								}
								
								for (int j = 0; j < jobNameList.size(); j++) {
									jobManage.setJobName(jobNameList.get(j));
									jobManageService.deleteJobManage(jobManage);
									executeManage.setJobName(jobNameList.get(j));
									executeManageService.deleteContainExecuteManage(executeManage);
								}
								
								if(sqlManageResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "sqlManage delete success");
									
									logService.writeLog(request, "SQLMANAGE", "DELETE-SUCCESS", sqlManage.toLogString(messageSource));
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "sqlManage delete fail");
									
									logService.writeLog(request, "SQLMANAGE", "DELETE-FAIL", sqlManage.toLogString(messageSource));
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "sqlManage delete fail");
							
							logService.writeLog(request, "SQLMANAGE", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "sqlManage proc command fail");
						
						String cmd="";
						if ("insert".equals(request.getParameter("proc"))) {cmd = "저장";}
						if ("modify".equals(request.getParameter("proc"))) {cmd = "수정";}
						if ("delete".equals(request.getParameter("proc"))) {cmd = "삭제";}
						
						logService.writeLog(request, "SQLMANAGE", "COMMAND-FAIL", cmd + " 실패");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "SQLMANAGE", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "SQLMANAGE", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "SQLMANAGE", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "SQLMANAGE", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	// jobManage 정보 insert, update, delete
	@RequestMapping(value = "/jobManage_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO jobManage_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("jobName") != null && request.getParameter("dbName") != null  && request.getParameter("sqlName") != null) {
						JobManage jobManage = new JobManage();
						jobManage.setJobName(request.getParameter("jobName"));
						jobManage.setDbName(request.getParameter("dbName").replace("\"", "").replace("[", "").replace("]", "").replace(",", "/"));
						jobManage.setSqlName(request.getParameter("sqlName").replace("\"", "").replace("[", "").replace("]", "").replace(",", "/"));
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							Integer jobManageResult = jobManageService.insertJobManage(jobManage);
							if(jobManageResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "jobManage insert success");
								
								logService.writeLog(request, "JOBMANAGE", "INSERT-SUCCESS", jobManage.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "jobManage insert fail");
								
								logService.writeLog(request, "JOBMANAGE", "INSERT-FAIL", jobManage.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "jobManage insert fail");
							
							logService.writeLog(request, "JOBMANAGE", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("jobName") != null && request.getParameter("dbName") != null  && request.getParameter("sqlName") != null) {
						JobManage jobManage = new JobManage();
						jobManage.setJobName(request.getParameter("jobName"));
						jobManage.setDbName(request.getParameter("dbName").replace("\"", "").replace("[", "").replace("]", "").replace(",", "/"));
						jobManage.setSqlName(request.getParameter("sqlName").replace("\"", "").replace("[", "").replace("]", "").replace(",", "/"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							Integer jobManageResult = jobManageService.updateJobManage(jobManage);
							if(jobManageResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "jobManage update success");
								
								logService.writeLog(request, "JOBMANAGE", "UPDATE-SUCCESS", jobManage.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "jobManage update fail");
								
								logService.writeLog(request, "JOBMANAGE", "UPDATE-FAIL", jobManage.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "jobManage update fail");
							
							logService.writeLog(request, "JOBMANAGE", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						JobManage jobManage = new JobManage();
						ExecuteManage executeManage = new ExecuteManage();
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							
							for(int i = 0; i < chList.length; i++) {
								jobManage.setJobName(chList[i]);
								Integer jobManageResult = jobManageService.deleteJobManage(jobManage);
								executeManage.setJobName(chList[i]);
								executeManageService.deleteContainExecuteManage(executeManage);
								if(jobManageResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "jobManage delete success");
									
									logService.writeLog(request, "JOBMANAGE", "DELETE-SUCCESS", jobManage.toLogString(messageSource));
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "jobManage delete fail");
									
									logService.writeLog(request, "JOBMANAGE", "DELETE-FAIL", jobManage.toLogString(messageSource));
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "jobManage delete fail");
							
							logService.writeLog(request, "JOBMANAGE", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "jobManage proc command fail");
						
						String cmd="";
						if ("insert".equals(request.getParameter("proc"))) {cmd = "저장";}
						if ("modify".equals(request.getParameter("proc"))) {cmd = "수정";}
						if ("delete".equals(request.getParameter("proc"))) {cmd = "삭제";}
						
						logService.writeLog(request, "JOBMANAGE", "COMMAND-FAIL", cmd + " 실패");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "JOBMANAGE", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "JOBMANAGE", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "JOBMANAGE", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "JOBMANAGE", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	// executeManage 정보 insert, update, delete
	@RequestMapping(value = "/executeManage_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO executeManage_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("executeName") != null && request.getParameter("jobName") != null  && request.getParameter("rSchedulerSelect") != null  && request.getParameter("rSchedulerWeek") != null  && request.getParameter("rSchedulerDay") != null  && request.getParameter("rSchedulerHour") != null) {
						ExecuteManage executeManage = new ExecuteManage();
						executeManage.setExecuteName(request.getParameter("executeName"));
						executeManage.setJobName(request.getParameter("jobName"));
						executeManage.setrSchedulerSelect(request.getParameter("rSchedulerSelect"));
						executeManage.setrSchedulerWeek(request.getParameter("rSchedulerWeek"));
						executeManage.setrSchedulerDay(request.getParameter("rSchedulerDay"));
						executeManage.setrSchedulerHour(request.getParameter("rSchedulerHour"));
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							Integer executeManageResult = executeManageService.insertExecuteManage(executeManage);
							if(executeManageResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "executeManage insert success");
								
								logService.writeLog(request, "EXECUTEMANAGE", "INSERT-SUCCESS", executeManage.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "executeManage insert fail");
								
								logService.writeLog(request, "EXECUTEMANAGE", "INSERT-FAIL", executeManage.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "executeManage insert fail");
							
							logService.writeLog(request, "EXECUTEMANAGE", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("executeName") != null && request.getParameter("jobName") != null  && request.getParameter("rSchedulerSelect") != null  && request.getParameter("rSchedulerWeek") != null  && request.getParameter("rSchedulerDay") != null  && request.getParameter("rSchedulerHour") != null) {
						ExecuteManage executeManage = new ExecuteManage();
						executeManage.setExecuteName(request.getParameter("executeName"));
						executeManage.setJobName(request.getParameter("jobName"));
						executeManage.setrSchedulerSelect(request.getParameter("rSchedulerSelect"));
						executeManage.setrSchedulerWeek(request.getParameter("rSchedulerWeek"));
						executeManage.setrSchedulerDay(request.getParameter("rSchedulerDay"));
						executeManage.setrSchedulerHour(request.getParameter("rSchedulerHour"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							Integer executeManageResult = executeManageService.updateExecuteManage(executeManage);
							if(executeManageResult == 1) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "executeManage update success");
								
								logService.writeLog(request, "EXECUTEMANAGE", "UPDATE-SUCCESS", executeManage.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "executeManage update fail");
								
								logService.writeLog(request, "EXECUTEMANAGE", "UPDATE-FAIL", executeManage.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "executeManage update fail");
							
							logService.writeLog(request, "EXECUTEMANAGE", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						ExecuteManage executeManage = new ExecuteManage();
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							
							for(int i = 0; i < chList.length; i++) {
								executeManage.setExecuteName(chList[i]);
								Integer executeManageResult = executeManageService.deleteExecuteManage(executeManage);
								if(executeManageResult == 1) {
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "executeManage delete success");
									
									logService.writeLog(request, "EXECUTEMANAGE", "DELETE-SUCCESS", executeManage.toLogString(messageSource));
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "executeManage delete fail");
									
									logService.writeLog(request, "EXECUTEMANAGE", "DELETE-FAIL", executeManage.toLogString(messageSource));
								}
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "executeManage delete fail");
							
							logService.writeLog(request, "EXECUTEMANAGE", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "executeManage proc command fail");
						
						String cmd="";
						if ("insert".equals(request.getParameter("proc"))) {cmd = "저장";}
						if ("modify".equals(request.getParameter("proc"))) {cmd = "수정";}
						if ("delete".equals(request.getParameter("proc"))) {cmd = "삭제";}
						
						logService.writeLog(request, "EXECUTEMANAGE", "COMMAND-FAIL", cmd + " 실패");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "EXECUTEMANAGE", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "EXECUTEMANAGE", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "EXECUTEMANAGE", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "EXECUTEMANAGE", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	// DB Connection Test
	@RequestMapping(value = "/dbConnectionTestProc", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO dbConnectionTestProc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
				
				if(request.getParameter("dbName") != null ) {
					
					DBManage dbManage = new DBManage();
					dbManage.setDbName(request.getParameter("dbName"));
					
					if ("Y".equals(nowAccessInfo.getWriteYn())) {
						
						dbManage = dbManageService.selectOneDBManage(dbManage);
						String dbServer = dbManage.getDbServer();
						String url = dbManage.getUrl();
						String dbDriver = "";
						String dbUrl = "";
						if("postgresql".equals(dbServer)) {
							dbDriver = "org.postgresql.Driver";
							dbUrl = "jdbc:postgresql://"+url; 
				    	}else if("sqlserver".equals(dbServer)) {
				    		dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				    		dbUrl = "jdbc:sqlserver://"+url; 
				    	}else if("oracle".equals(dbServer)) {
				    		dbDriver = "oracle.jdbc.driver.OracleDriver";
				    		dbUrl = "jdbc:oracle:thin:@"+url; 
				    	}
						
						DBConnectionMgr mgr = new DBConnectionMgr();
						mgr.set_driver(dbDriver);
						mgr.set_url(dbUrl);
						mgr.set_user(dbManage.getId());
						mgr.set_password(dbManage.getPw());
						
						try {
							Connection con = mgr.getConnection();
							con.close();
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "dbManage connection success");
							logService.writeLog(request, "DBMANAGE", "CONNECTION-SUCCESS", dbManage.toLogString(messageSource));
						} catch (Exception e) {
							logger.error("error",e);
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "dbManage connection fail");
							logService.writeLog(request, "DBMANAGE", "EXCEPTION", e.getMessage());
						}
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "dbManage connection fail");
						
						logService.writeLog(request, "DBMANAGE", "SELECT-FAIL", "권한이 없는 사용자의 요청입니다.");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "dbName fail");
					
					logService.writeLog(request, "DBMANAGE", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "DBMANAGE", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "DBMANAGE", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "DBMANAGE", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	// excuteQuery Test
	@RequestMapping(value = "/excuteQuery", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO excuteQuery(HttpServletRequest request, Locale local, Model model) {
		
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.dbManage");
				
				if(request.getParameter("executeName") != null ) {
					if ("Y".equals(nowAccessInfo.getWriteYn())) {
						// 날짜 시간
						Calendar cal = Calendar.getInstance();
						DateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
						String calNow = format.format(cal.getTime());
						
						// 스케줄 실행 등록 된 정보 가져오기
						ExecuteManage executeManage = new ExecuteManage();
						executeManage.setExecuteName(request.getParameter("executeName"));
						executeManage = executeManageService.selectOneExecuteManage(executeManage);
						
						// 작업 정보 가져오기 
						JobManage jobManage = new JobManage();
						jobManage.setJobName(executeManage.getJobName());
						jobManage = jobManageService.selectOneJobManage(jobManage);
						
						// 작업그룹에 속해있는 db 및 sql 정보 배열에 넣기
						String[] dbList = jobManage.getDbName().split("/");
						String[] sqlList = jobManage.getSqlName().split("/");
						
						// 객체 생성
						DBManage dbManage = new DBManage();
						SQLManage sqlManage = new SQLManage();
						
						// 객체생성
						Generator gen = new Generator();
						
						// 작업그룹에 속해있는 db&sql set 개수 만큼 정보 가져오기
						for (int i = 0; i < dbList.length; i++) {
							// db 정보 가져오기
							dbManage.setDbName(dbList[i]);
							dbManage = dbManageService.selectOneDBManage(dbManage);
							
							// 쿼리정보 가져오기
							sqlManage.setSqlName(sqlList[i]);
							sqlManage = sqlManageService.selectOneSQLManage(sqlManage);
							
							// generator에 db 및 쿼리 정보 입력하기
							DBInfoList.DBInfo dbinfo  = new DBInfoList.DBInfo(dbManage.getDbServer(), dbManage.getUrl(), dbManage.getId(), dbManage.getPw(), dbManage.getTimeout());
							gen.add(dbinfo, sqlManage.getSql());
						}
						
						try {
							// 실행
							gen.execute();
							
							executeManage.setExecuteName(executeManage.getExecuteName());
							executeManage.setrExecuteFlag("Y");
							executeManage.setrExecuteDate(calNow.substring(0, 8));
							executeManage.setrExecuteTime(calNow.substring(9, 15));
							executeManageService.updateStatusExecuteManage(executeManage);
							
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.setResult("1");
							jRes.addAttribute("msg", "excute query success");
							logService.writeLog(request, "EXECUTEQUERY", "EXECUTEQUERY-SUCCESS", executeManage.toLogString(messageSource));
						} catch (Exception e) {
							logger.error("error",e);
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "excute query fail");
							logService.writeLog(request, "EXECUTEQUERY", "EXCEPTION", e.getMessage());
						}
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "excute query fail");
						
						logService.writeLog(request, "EXECUTEQUERY", "EXECUTEQUERY-FAIL", "권한이 없는 사용자의 요청입니다.");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "executeName fail");
					
					logService.writeLog(request, "EXECUTEQUERY", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "EXECUTEQUERY", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "EXECUTEQUERY", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "EXECUTEQUERY", "EXCEPTION", e.getMessage());
		}
		return jRes;
		
	}
	
	// 사용자DB 인터페이스 insert update delete
	@RequestMapping(value = "/userDBInterface_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO userDBInterface_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		try {
			if(userInfo != null) {
				
				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "schedulerManage.userDBInterface");
				
				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("rDbIp") != null && request.getParameter("rDbUser") != null  && request.getParameter("rDbPwd") != null  && request.getParameter("rDbPort") != null  && request.getParameter("rDbName") != null  && request.getParameter("rTeamCode") != null && request.getParameter("rTeamName") != null && request.getParameter("rHh") != null) {
						UserDBInterface userDBInterface = new UserDBInterface();
						userDBInterface.setrDbIp(request.getParameter("rDbIp"));
						userDBInterface.setrDbUser(request.getParameter("rDbUser"));
						userDBInterface.setrDbPwd(request.getParameter("rDbPwd"));
						userDBInterface.setrDbPort(request.getParameter("rDbPort"));
						userDBInterface.setrDbName(request.getParameter("rDbName"));
						userDBInterface.setrTeamCode(request.getParameter("rTeamCode"));
						userDBInterface.setrTeamName(request.getParameter("rTeamName"));
						userDBInterface.setrHh(request.getParameter("rHh"));
						if ("Y".equals(nowAccessInfo.getWriteYn())) {
							Integer userDBInterfaceResult = userDBInterfaceService.insertUserDBInterface(userDBInterface);
							if(userDBInterfaceResult == 1) {
								
								/*// userdb_interface 전체목록 mg/sginfo로 upsert
								List<UserDBInterface> mgList = userDBInterfaceService.selectUserDBInterface(userDBInterface);
								CodeInfoVO codeInfoVO = new CodeInfoVO();
								for (int i = 0; i < mgList.size(); i++) {
									// uses bg코드
									codeInfoVO.setrBgCode("2000");
									codeInfoVO.setrMgCode(mgList.get(i).getrTeamCode());
									codeInfoVO.setrMgName(mgList.get(i).getrTeamName());
									codeInfoVO.setrSgCode(mgList.get(i).getrTeamCode());
									codeInfoVO.setrSgName(mgList.get(i).getrTeamName());
									customerInfoService.upsertMgInfo(codeInfoVO);
									customerInfoService.upsertSgInfo(codeInfoVO);
								}// upsert end*/
								
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "userDBInterface insert success");
								
								logService.writeLog(request, "USERDBINTERFACE", "INSERT-SUCCESS", userDBInterface.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "userDBInterface insert fail");
								
								logService.writeLog(request, "USERDBINTERFACE", "INSERT-FAIL", userDBInterface.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "userDBInterface insert fail");
							
							logService.writeLog(request, "USERDBINTERFACE", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
					} else if(request.getParameter("proc").equals("modify") && request.getParameter("rDbIp") != null && request.getParameter("rDbUser") != null  && request.getParameter("rDbPwd") != null  && request.getParameter("rDbPort") != null  && request.getParameter("rDbName") != null  && request.getParameter("rTeamCode") != null && request.getParameter("rTeamName") != null && request.getParameter("rHh") != null && request.getParameter("seq") != null) {
						UserDBInterface userDBInterface = new UserDBInterface();
						userDBInterface.setrDbIp(request.getParameter("rDbIp"));
						userDBInterface.setrDbUser(request.getParameter("rDbUser"));
						userDBInterface.setrDbPwd(request.getParameter("rDbPwd"));
						userDBInterface.setrDbPort(request.getParameter("rDbPort"));
						userDBInterface.setrDbName(request.getParameter("rDbName"));
						userDBInterface.setrTeamCode(request.getParameter("rTeamCode"));
						userDBInterface.setrTeamName(request.getParameter("rTeamName"));
						userDBInterface.setrHh(request.getParameter("rHh"));
						userDBInterface.setSeq(request.getParameter("seq"));
						if ("Y".equals(nowAccessInfo.getModiYn())) {
							Integer userDBInterfaceResult = userDBInterfaceService.updateUserDBInterface(userDBInterface);
							if(userDBInterfaceResult == 1) {
								
								/*// userdb_interface 전체목록 mg/sginfo로 upsert
								List<UserDBInterface> mgList = userDBInterfaceService.selectUserDBInterface(userDBInterface);
								CodeInfoVO codeInfoVO = new CodeInfoVO();
								for (int i = 0; i < mgList.size(); i++) {
									// uses bg코드
									codeInfoVO.setrBgCode("2000");
									codeInfoVO.setrMgCode(mgList.get(i).getrTeamCode());
									codeInfoVO.setrMgName(mgList.get(i).getrTeamName());
									codeInfoVO.setrSgCode(mgList.get(i).getrTeamCode());
									codeInfoVO.setrSgName(mgList.get(i).getrTeamName());
									customerInfoService.upsertMgInfo(codeInfoVO);
									customerInfoService.upsertSgInfo(codeInfoVO);
								}// upsert end*/
								
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.setResult("1");
								jRes.addAttribute("msg", "userDBInterface update success");
								
								logService.writeLog(request, "USERDBINTERFACE", "UPDATE-SUCCESS", userDBInterface.toLogString(messageSource));
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.setResult("0");
								jRes.addAttribute("msg", "userDBInterface update fail");
								
								logService.writeLog(request, "USERDBINTERFACE", "UPDATE-FAIL", userDBInterface.toLogString(messageSource));
							}
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "userDBInterface update fail");
							
							logService.writeLog(request, "USERDBINTERFACE", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
						}
					} else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
						String[] chList = request.getParameter("chList").split(",");
						UserDBInterface userDBInterface = new UserDBInterface();
						
						if ("Y".equals(nowAccessInfo.getDelYn())) {
							//CodeInfoVO codeInfoVO = new CodeInfoVO();
							for(int i = 0; i < chList.length; i++) {
								userDBInterface.setSeq(chList[i]);
								
								/*// mg/sginfo delete(userDB interface 정보가 삭제되면 데이터를 get하지 못해 미리 넣어줌)
								userDBInterface = userDBInterfaceService.selectOneUserDBInterface(userDBInterface);
								codeInfoVO.setrBgCode("2000");
								codeInfoVO.setrMgCode(userDBInterface.getrTeamCode());
								codeInfoVO.setrMgName(userDBInterface.getrTeamName());
								codeInfoVO.setrSgCode(userDBInterface.getrTeamCode());
								codeInfoVO.setrSgName(userDBInterface.getrTeamName());*/
								
								Integer userDBInterfaceResult = userDBInterfaceService.deleteUserDBInterface(userDBInterface);
								// delete end
								if(userDBInterfaceResult == 1) {
									
									/*// mg/sginfo delete
									customerInfoService.deleteMgInfo(codeInfoVO);
									customerInfoService.deleteSgInfo(codeInfoVO);
									// delete end*/
									
									jRes.setSuccess(AJaxResVO.SUCCESS_Y);
									jRes.setResult("1");
									jRes.addAttribute("msg", "userDBInterface delete success");
									
									logService.writeLog(request, "USERDBINTERFACE", "DELETE-SUCCESS", userDBInterface.toLogString(messageSource));
								} else {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.setResult("0");
									jRes.addAttribute("msg", "userDBInterface delete fail");
									
									logService.writeLog(request, "USERDBINTERFACE", "DELETE-FAIL", userDBInterface.toLogString(messageSource));
								}
							}
							
							/*// userdb_interface 전체목록 mg/sginfo로 upsert
							List<UserDBInterface> mgList = userDBInterfaceService.selectUserDBInterface(userDBInterface);
							for (int i = 0; i < mgList.size(); i++) {
								// uses bg코드
								codeInfoVO.setrBgCode("2000");
								codeInfoVO.setrMgCode(mgList.get(i).getrTeamCode());
								codeInfoVO.setrMgName(mgList.get(i).getrTeamName());
								codeInfoVO.setrSgCode(mgList.get(i).getrTeamCode());
								codeInfoVO.setrSgName(mgList.get(i).getrTeamName());
								customerInfoService.upsertMgInfo(codeInfoVO);
								customerInfoService.upsertSgInfo(codeInfoVO);
							}// upsert end*/
							
						}else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("0");
							jRes.addAttribute("msg", "userDBInterface delete fail");
							
							logService.writeLog(request, "USERDBINTERFACE", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
						}
						
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "userDBInterface proc command fail");
						
						String cmd="";
						if ("insert".equals(request.getParameter("proc"))) {cmd = "저장";}
						if ("modify".equals(request.getParameter("proc"))) {cmd = "수정";}
						if ("delete".equals(request.getParameter("proc"))) {cmd = "삭제";}
						
						logService.writeLog(request, "USERDBINTERFACE", "COMMAND-FAIL", cmd + " 실패");
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "proc fail");
					
					logService.writeLog(request, "USERDBINTERFACE", "COMMAND-FAIL", "비 정상적인 요청입니다.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				
				logService.writeLog(request, "USERDBINTERFACE", "COMMAND-FAIL", "세션이 끊겼습니다.");
			}
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "USERDBINTERFACE", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "USERDBINTERFACE", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	
	// 로고 정보 수정
	@RequestMapping(value = "/updateLogoInfo.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO updateLogoInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		LogoVO logoInfo = new LogoVO();

			if(userInfo != null) {
				if(!StringUtil.isNull(request.getParameter("logoType"),true)) 
					logoInfo.setLogoType(request.getParameter("logoType"));
				if(!StringUtil.isNull(request.getParameter("logoChangeUseYn"),true)) 
					logoInfo.setLogoChangeUse(request.getParameter("logoChangeUseYn"));
				if(!StringUtil.isNull(request.getParameter("logoName"),true)) 
					logoInfo.setLogoPath(request.getParameter("logoName"));

				Integer setLogoInfoResult = subNumberInfoService.updateLogoInfo(logoInfo);
				if(setLogoInfoResult>0)
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				else
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// 로고 파일 업로드
	@RequestMapping(value="/logoImgUpload.do", method=RequestMethod.POST)
	public @ResponseBody  AJaxResVO logoImgUpload(MultipartHttpServletRequest multi, HttpServletRequest request, HttpServletResponse response) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		try{
			if(userInfo != null){
				String logoType = ""; 
				if (request.getParameter("logoType") != null) {
					if ("login".equals(request.getParameter("logoType"))) {
						logoType = "login_logo";
					} else {
						logoType = "main_logo";
					}
				}
				
				
				
				//폴더 확인 후 생성
				File dir = new File(request.getSession().getServletContext().getRealPath("/resources/common/recsee/images/project/main/logo")); // "/usr/furence/IPCR3.0/conf/ ::: C:/usr/furence/IPCR3.0/conf/"
				//디렉토리가 없으면
				if(!dir.isDirectory())
					dir.mkdirs();

				
				//업로드 체크값
				boolean uploadSuccess = false;
				
				// 업로드 된 파일명들 가져옴
				Iterator<String> files = multi.getFileNames();
				while(files.hasNext()){
					String uploadImg = files.next();
					MultipartFile file = multi.getFile(uploadImg);
					String name = file.getName();
					name = XssFilterUtil.XssFilter(name);
					String ext = name.substring(name.lastIndexOf(".")+1);
					if(ext.equals("png") || ext.equals("jpg")) {
						if (file != null && !file.isEmpty()) {
							String origFileName = file.getOriginalFilename();
							String filename = logoType + ".png";
							File imageFile = new File(request.getSession().getServletContext().getRealPath("/resources/common/recsee/images/project/main/logo") + File.separator + filename);
							
							try {
								file.transferTo(imageFile);
								uploadSuccess = true;
							} catch (IllegalStateException | IOException e) {
								logger.error("error",e);
								uploadSuccess = false;
								jRes.setResult("UPLOADFAILED");
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
							}
						}
					}
				}
				// 업로드 실패여부 판단
				if(uploadSuccess) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("NOT_LOGIN");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// STT서버 관리 - 시스템 설정
	@RequestMapping(value = "/stt_server_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO stt_server_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		try {
			if(userInfo != null) {

				// 세션에서 권한 정보 가져오기
				@SuppressWarnings("unchecked")
				List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
				MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "systemOption.sttServer");

				if(request.getParameter("proc") != null ) {
					if(request.getParameter("proc").equals("insert") && request.getParameter("sysId") != null && request.getParameter("sysName") != null && request.getParameter("sysIp") != null) {

						SttServerInfo sttServerInfo = new SttServerInfo();

						sttServerInfo.setrSysCode(request.getParameter("sysId"));
						sttServerInfo.setrSysName(request.getParameter("sysName"));
						sttServerInfo.setrSysIp(request.getParameter("sysIp"));

		               if ("Y".equals(nowAccessInfo.getWriteYn())) {

		               Integer sttServerInfoResult = sttServerInfoService.insertSttServerInfo(sttServerInfo);
			               if(sttServerInfoResult == 1) {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			                  jRes.setResult("1");
			                  jRes.addAttribute("msg", "system insert success");

			                  logService.writeLog(request, "SERVER", "INSERT-SUCCESS", sttServerInfo.toLogString(messageSource));
			               } else {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system insert fail");

			                  logService.writeLog(request, "SERVER", "INSERT-FAIL", sttServerInfo.toLogString(messageSource));
			               }
		               }else {
		            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system insert fail");

			                  logService.writeLog(request, "SERVER", "INSERT-FAIL", "권한이 없는 사용자의 요청입니다.");
		               }
		            } else if(request.getParameter("proc").equals("modify") && request.getParameter("sysId") != null) {
		            	SttServerInfo sttServerInfo = new SttServerInfo();

		            	sttServerInfo.setrSysCode(request.getParameter("sysId"));

		               if(request.getParameter("sysName")!= null) sttServerInfo.setrSysName(request.getParameter("sysName"));
		               if(request.getParameter("sysIp") != null) sttServerInfo.setrSysIp(request.getParameter("sysIp"));

		               if ("Y".equals(nowAccessInfo.getModiYn())) {

		            	   Integer sttServerInfoResult = sttServerInfoService.updateSttServerInfo(sttServerInfo);
		            	   
			               if(sttServerInfoResult == 1) {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			                  jRes.setResult("1");
			                  jRes.addAttribute("msg", "system update success");

			                  logService.writeLog(request, "SERVER", "UPDATE-SUCCESS", sttServerInfo.toLogString(messageSource));
			               } else {
			                  jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system update fail");

			                  logService.writeLog(request, "SERVER", "UPDATE-FAIL", sttServerInfo.toLogString(messageSource));
			               }
		               }else {
		            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                  jRes.setResult("0");
			                  jRes.addAttribute("msg", "system update fail");

			                  logService.writeLog(request, "SERVER", "UPDATE-FAIL", "권한이 없는 사용자의 요청 입니다.");
		               }
		            } else if(request.getParameter("proc").equals("delete") && request.getParameter("chList") != null) {
		               String[] chList = request.getParameter("chList").split(",");
		               SttServerInfo sttServerInfo = new SttServerInfo();

		               if ("Y".equals(nowAccessInfo.getDelYn())) {

			               for(int i = 0; i < chList.length; i++) {
			            	   sttServerInfo.setrSysCode(chList[i]);

			                  Integer sttServerInfoResult = sttServerInfoService.deleteSttServerInfo(sttServerInfo);
			                  if(sttServerInfoResult == 1) {
			                     jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			                     jRes.setResult("1");
			                     jRes.addAttribute("msg", "system delete success");

			                     logService.writeLog(request, "SERVER", "DELETE-SUCCESS", sttServerInfo.toLogString(messageSource));
			                  } else {
			                     jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                     jRes.setResult("0");
			                     jRes.addAttribute("msg", "system delete fail");

			                     logService.writeLog(request, "SERVER", "DELETE-FAIL", sttServerInfo.toLogString(messageSource));
			                  }
			               }
		               }else {
		            	   jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                     jRes.setResult("0");
		                     jRes.addAttribute("msg", "system delete fail");

		                     logService.writeLog(request, "SERVER", "DELETE-FAIL", "권한이 없는 사용자의 요청입니다.");
		               }

		            } else {
		               jRes.setSuccess(AJaxResVO.SUCCESS_N);
		               jRes.setResult("0");
		               jRes.addAttribute("msg", "system proc command fail");

		               String cmd = "저장";
		               if ("modify".equals(request.getParameter("proc")))
		            	   cmd = "수정";
		               if ("delete".equals(request.getParameter("proc")))
		            	   cmd = "삭제";

		               logService.writeLog(request, "SERVER", "COMMAND-FAIL", cmd + " 실패");
		            }
		         } else {
		            jRes.setSuccess(AJaxResVO.SUCCESS_N);
		            jRes.setResult("0");
		            jRes.addAttribute("msg", "proc fail");

		            logService.writeLog(request, "SERVER", "COMMAND-FAIL", "비 정상적인 요청입니다.");
		         }
		      } else {
		         jRes.setSuccess(AJaxResVO.SUCCESS_N);
		         jRes.setResult("0");
		         jRes.addAttribute("msg", "login fail");

		         logService.writeLog(request, "SERVER", "COMMAND-FAIL", "세션이 끊겼습니다.");
		      }
		}catch (NullPointerException e) {
			logger.error("error",e);
			logService.writeLog(request, "SERVER", "EXCEPTION", e.getMessage());
		}catch (Exception e) {
			logger.error("error",e);
			logService.writeLog(request, "SERVER", "EXCEPTION", e.getMessage());
		}
		return jRes;
	}
	
	//시스템 관리 - 시스템 설정
	@RequestMapping(value = "/switchSystemProc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO switchSystemProc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		try {
			if(userInfo != null) {
				String type = request.getParameter("type");
				
				if (!(StringUtil.isNull(type))) {
					// 전환일경우
					if ("switch".equals(type)) {
						String sysId = request.getParameter("sysId");
						String sSysId = request.getParameter("sSysId");
						
						if (sysId == null) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                    jRes.addAttribute("msg", "시스템 전환 실패");
							return jRes;
						} else if (sSysId == null) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                    jRes.addAttribute("msg", "전환 할 시스템을 선택해주세요.");
							return jRes;
						} else if ("sysId".equals(sSysId)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                    jRes.addAttribute("msg", "현재 선택한 시스템과 동일한 시스템입니다.");
							return jRes;
						}
						
						// ch_info 업데이트
						ChannelInfo channelInfo = new ChannelInfo();
						channelInfo.setOldSysCode(sysId);
						channelInfo.setSysCode(sSysId);
						int chUpdateResult = 0;
						try {
							chUpdateResult = channelInfoService.updateSwitchChannelInfo(channelInfo);
							if(chUpdateResult > 0) {
								
								// sys_info 업데이트
								SystemInfo systemInfo = new SystemInfo();
								systemInfo.setSysId(sysId);
								systemInfo.setSwitchSysYn("Y");
								systemInfo.setSwitchSysCode(sSysId);
								systemInfo.setChannelUpdateFlag("1");
								int sysUpdateResult = 0;
								try {
									sysUpdateResult = systemInfoService.updateSystemInfo(systemInfo);
									if(sysUpdateResult > 0) {
										jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					                    jRes.addAttribute("msg", "시스템 전환 성공");
									}else {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
					                    jRes.addAttribute("msg", "업데이트 된 시스템 없음");
									}
								} catch(Exception e) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
				                    jRes.addAttribute("msg", "시스템 정보 업데이트 실패");
								}
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                    jRes.addAttribute("msg", "업데이트 된 채널 없음");
							}
						} catch(Exception e) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                    jRes.addAttribute("msg", "채널 정보 업데이트 실패");
						};
					} else if ("returning".equals(type)){
						String sysId = request.getParameter("sysId");
						String sSysId = request.getParameter("sSysId");
						
						// ch_info 업데이트
						ChannelInfo channelInfo = new ChannelInfo();
						channelInfo.setSysCode(sysId);
						channelInfo.setOldSysCode(sSysId);
						int chUpdateResult = 0;

						try {
							chUpdateResult = channelInfoService.updateReturningSwitchChannelInfo(channelInfo);
							if (chUpdateResult > 0) {

								// sys_info 업데이트
								SystemInfo systemInfo = new SystemInfo();
								systemInfo.setSysId(sysId);
								systemInfo.setSwitchSysYn("N");
								systemInfo.setSwitchSysCode("");
								systemInfo.setSwitchChNum("");
								systemInfo.setChannelUpdateFlag("1");
								int sysUpdateResult = 0;
								try {
									sysUpdateResult = systemInfoService.updateSystemInfo(systemInfo);
									if(sysUpdateResult > 0) {
										jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					                    jRes.addAttribute("msg", "시스템 전환 성공");
									}else {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
					                    jRes.addAttribute("msg", "업데이트 된 시스템 없음");
									}
								} catch(Exception e) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
				                    jRes.addAttribute("msg", "시스템 정보 업데이트 실패");
								}
							} else {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
			                    jRes.addAttribute("msg", "원복 된 채널 없음");
							}
						} catch(Exception e) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
		                    jRes.addAttribute("msg", "채널 정보 원복 실패");
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("error",e);
		}
		               
		return jRes;
	}
}
