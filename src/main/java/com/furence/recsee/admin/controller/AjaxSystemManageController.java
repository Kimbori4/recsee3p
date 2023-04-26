package com.furence.recsee.admin.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.model.DelRecfileInfo;
import com.furence.recsee.admin.model.FileRecoverInfo;
import com.furence.recsee.admin.service.ChannelInfoService;
import com.furence.recsee.admin.service.DelRecfileInfoService;
import com.furence.recsee.admin.service.FileRecoverService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.Log;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.CompreasZip;
import com.furence.recsee.common.util.ExcelView;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;
import com.furence.recsee.common.util.sqlFilterUtil;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;
import com.initech.core.util.URLEncoder;
import com.initech.inisafenet.iniplugin.log.Logger;


/**
 * 시스템 관리 페이지의 요청 사항 처리 컨트롤러
 *  
 */
@Controller
public class AjaxSystemManageController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AjaxSystemManageController.class);
	@Autowired
	private DelRecfileInfoService delRecfileInfoService;

	@Autowired
	private LogService logService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private FileRecoverService fileRecoverService;

	@Autowired
	private ChannelInfoService channelInfoService;

	@Autowired
	private SearchListInfoService searchListInfoService;
	
	/**
	 * 로그 관련 요청 처리 메소드
	 *  
	 *  @param request 요청 처리 조건이 담긴 HttpServletRequest 파라미터 
	 *  (proc - 로그 처리 타입 (delete), 
	 *  sDate - 로그 시작 날짜,
	 *  eDate - 로그 종료 날짜,
	 *  sTime - 로그 시작 시간,
	 *  eTime - 로그 종료 시간,
	 *  rLogIp - 클라이언트 IP,
	 *  rLogServerIp - 로그 서버 IP,
	 *  rLogUserId - 사용자 ID,
	 *  rLogContents - 로그 내용,
	 *  rLogEtc - 로그 설명)
	 *  @return jRes 요청 처리 성공 여부 및 실패 이유 리턴 
	 */
	@RequestMapping(value = "/log_proc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO log_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {
			// try {
			if (request.getParameter("proc") != null) {
				if (request.getParameter("proc").equals("delete")) {
					Log log = new Log();

					if (request.getParameter("sDate") != null && !request.getParameter("sDate").trim().isEmpty()
							&& request.getParameter("eDate") != null
							&& !request.getParameter("eDate").trim().isEmpty()) {
						log.setsDate(request.getParameter("sDate").replace("-", "").toString());
						log.seteDate(request.getParameter("eDate").replace("-", "").toString());
					}
					if (request.getParameter("sTime") != null && !request.getParameter("sTime").trim().isEmpty()
							&& request.getParameter("eTime") != null
							&& !request.getParameter("eTime").trim().isEmpty()) {
						log.setsTime(request.getParameter("sTime").replace(":", "").toString());
						log.seteTime(request.getParameter("eTime").replace(":", "").toString());
					}
					if (request.getParameter("rLogIp") != null && !request.getParameter("rLogIp").trim().isEmpty()) {
						log.setrLogIp(request.getParameter("rLogIp"));
					}
					if (request.getParameter("rLogServerIp") != null
							&& !request.getParameter("rLogServerIp").trim().isEmpty()) {
						log.setrLogServerIp(request.getParameter("rLogServerIp"));
					}
					if (request.getParameter("rLogUserId") != null
							&& !request.getParameter("rLogUserId").trim().isEmpty()) {
						log.setrLogUserId(request.getParameter("rLogUserId"));
					}
					if (request.getParameter("rLogContents") != null
							&& !request.getParameter("rLogContents").trim().isEmpty()) {
						log.setrLogContents(request.getParameter("rLogContents"));
					}
					if (request.getParameter("rLogEtc") != null && !request.getParameter("rLogEtc").trim().isEmpty()) {
						log.setrLogEtc(request.getParameter("rLogEtc"));
					}

					Integer logResult = logService.deleteLog(log);
					if (logResult > 0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
						jRes.addAttribute("msg", "log delete success");

						logService.writeLog(request, "LOG", "DELETE-SUCCESS", log.toLogString(messageSource));
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "log delete fail");

						logService.writeLog(request, "LOG", "DELETE-FAIL", log.toLogString(messageSource));
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "log proc command fail");

					logService.writeLog(request, "LOG", "COMMAND-FAIL", "�� �������� ��û�Դϴ�.");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "proc fail");

				logService.writeLog(request, "LOG", "COMMAND-FAIL", "�� �������� ��û�Դϴ�.");
			}
			/*
			 * }catch (Exception e){
			 * 
			 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
			 * jRes.addAttribute("msg", "proc fail"); logService.writeLog(request, "LOG",
			 * "EXCEPTION", e.getMessage()); }
			 */
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
			logService.writeLog(request, "LOG", "COMMAND-FAIL", "������ ������ϴ�.");
		}
		return jRes;
	}


	/**
	 * 이력관리 페이지 엑셀 다운로드 처리 메소드
	 *  
	 *  @param request 다운로드 조건이 담긴 HttpServletRequest 파라미터 
	 *  (proc - 로그 처리 타입 (delete), 
	 *  sDate - 로그 시작 날짜,
	 *  eDate - 로그 종료 날짜,
	 *  sTime - 로그 시작 시간,
	 *  eTime - 로그 종료 시간,
	 *  rLogIp - 클라이언트 IP,
	 *  rLogServerIp - 로그 서버 IP,
	 *  rLogUserId - 사용자 ID,
	 *  rLogContents - 로그 내용,
	 *  rLogEtc - 로그 설명,
	 *  fileName - 다운로드 파일명) 
	 */
	@RequestMapping(value = "/logExcel.do")
	public void logExcel(HttpServletRequest request, Map<String, Object> ModelMap, HttpServletResponse response)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		LoginVO userInfo = SessionManager.getUserInfo(request);

		List<String[]> contents = new ArrayList<String[]>();

		String[] row = new String[8];
		int colPos = 0;

		if (userInfo != null) {

			boolean nowAcc = nowAccessChk(request, "systemManage.log");

			if (!nowAcc) {
				return;
			}

			for (int i = 0; i < 8; i++) {
				switch (i) {
				case 0:
					row[i] = messageSource.getMessage("admin.grid.date", null, Locale.getDefault())/* "날짜" */;
					break;
				case 1:
					row[i] = messageSource.getMessage("admin.grid.time", null, Locale.getDefault())/* "시간" */;
					break;
				case 2:
					row[i] = messageSource.getMessage("admin.grid.logIp", null, Locale.getDefault())/* "로그IP" */;
					break;
				case 3:
					row[i] = messageSource.getMessage("admin.grid.serverIp", null, Locale.getDefault())/* "서버IP" */;
					break;
				case 4:
					row[i] = messageSource.getMessage("admin.grid.userId", null, Locale.getDefault())/* "사용자ID" */;
					break;
				case 5:
					row[i] = messageSource.getMessage("admin.grid.logType", null, Locale.getDefault())/* "로그 분류" */;
					break;
				case 6:
					row[i] = messageSource.getMessage("admin.grid.logContent", null, Locale.getDefault())/* "로그 내용" */;
					break;
				case 7:
					row[i] = messageSource.getMessage("admin.grid.contents", null, Locale.getDefault())/* "상세 내용" */;
					break;
				}
			}
			contents.add(row);

			Log log = new Log();

			if (!StringUtil.isNull(request.getParameter("sDate"), true)
					&& !StringUtil.isNull(request.getParameter("eDate"), true)) {
				log.setsDate(sqlFilterUtil.sqlFilter(request.getParameter("sDate").replace("-", "").toString()));
				log.seteDate(sqlFilterUtil.sqlFilter(request.getParameter("eDate").replace("-", "").toString()));
			}
			if (!StringUtil.isNull(request.getParameter("sTime"), true)
					&& !StringUtil.isNull(request.getParameter("sTime"), true)) {
				log.setsTime(sqlFilterUtil.sqlFilter(request.getParameter("sTime").replace(":", "").toString()));
				log.seteTime(sqlFilterUtil.sqlFilter(request.getParameter("eTime").replace(":", "").toString()));
			}
			if (!StringUtil.isNull(request.getParameter("rLogIp"), true)) {
				log.setrLogIp(sqlFilterUtil.sqlFilter(request.getParameter("rLogIp")));
			}
			if (!StringUtil.isNull(request.getParameter("rLogServerIp"), true)) {
				log.setrLogServerIp(sqlFilterUtil.sqlFilter(request.getParameter("rLogServerIp")));
			}
			if (!StringUtil.isNull(request.getParameter("rLogUserId"), true)) {
				log.setrLogUserId(sqlFilterUtil.sqlFilter(request.getParameter("rLogUserId")));
			}
			if (!StringUtil.isNull(request.getParameter("rLogEtc"), true)) {
				log.setrLogEtc(sqlFilterUtil.sqlFilter(request.getParameter("rLogEtc")));
			}
			if (!StringUtil.isNull(request.getParameter("rLogCode"), true)) {
				log.setrLogCode(sqlFilterUtil.sqlFilter(request.getParameter("rLogCode")));
			}
			if (!StringUtil.isNull(request.getParameter("rLogDetailCode"), true)) {
				log.setrLogDetailCode(sqlFilterUtil.sqlFilter(request.getParameter("rLogDetailCode")));
			}

			List<Log> logResult = logService.selectLog(log);
			Integer logResultTotal = logResult.size();

			for (int i = 0; i < logResultTotal; i++) {
				Log logItem = logResult.get(i);

				row = new String[row.length];

				colPos = 0;

				setValue(row, logItem.getrLogDate(), colPos++);
				setValue(row, logItem.getrLogTime(), colPos++);
				setValue(row, logItem.getrLogIp(), colPos++);
				setValue(row, logItem.getrLogServerIp(), colPos++);
				setValue(row, logItem.getrLogUserId(), colPos++);
				setValue(row, logItem.getrLogName(), colPos++);
				setValue(row, logItem.getrLogContents(), colPos++);
				setValue(row, logItem.getrLogEtc(), colPos++);

				contents.add(row);
			}

			ModelMap.put("excelList", contents);
			ModelMap.put("target", request.getParameter("fileName"));

			String realPath = request.getSession().getServletContext().getRealPath("/search");
			ExcelView.createXlsx(ModelMap, realPath, response);
			row = null;
			contents = null;
			logService.writeLog(request, "EXCELDOWN", "DO", log.toLogString(messageSource));
		}
	}

	/**
	 * 로그뷰 페이지 로그 리스트를 rsfft에 요청하는 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (path - 로그 경로) 
	 */
	@RequestMapping(value = "/logList.do", produces = "application/json;charset=UTF-8")
	public @ResponseBody String logList(HttpServletRequest request, Locale local, Model model) throws IOException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String goPath = "";
		String response = "";

		if (userInfo != null) {
			boolean nowAcc = nowAccessChk2(request, "systemManage.logView");
			// System.out.println(nowAcc);
			if (!nowAcc) {
				return response;
			}
			
			EtcConfigInfo logInfo = new EtcConfigInfo();
			logInfo.setGroupKey("LOG");
			logInfo.setConfigKey("LOG_URL");
			List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(logInfo);

			String ip = result.get(0).getConfigValue();
			URL url = new URL(ip + "/list?path=" + XssFilterUtil.XssFilter(request.getParameter("path")));
			// System.out.println(request.getParameter("path"));
			// if(!StringUtil.isNull(request.getParameter("path"))) {
			// goPath = request.getParameter("path");
			// url = new URL(ip+"/list?path="+goPath);
			// }else {
			// url = new URL(ip+"/list?path="+path);
			// }
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(3000);

			InputStream in = conn.getInputStream();

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte[] buf = new byte[1024 * 8];
			int length = 0;


			try {
				while ((length = in.read(buf)) != -1) {
					out.write(buf, 0, length);
				}
				in.close();
				out.close();
				conn.disconnect();
			} catch (IOException e) {
				logger.error("error"+e);
			}finally {
				if(in != null) {
					try {
						in.close();
					}catch(Exception e) {
						logger.error("error"+e.getMessage());
					}
				}
				if(out !=null) {
					try {
						out.close();
					}catch(Exception e) {
						logger.error("error"+e.getMessage());
					}
				}
				if(conn != null) {
					try {
					conn.disconnect();
					}catch(Exception e) {
						logger.error("error"+e.getMessage());
					}
				}
			}
			response = new String(out.toByteArray(), "UTF-8");

		}
		return XssFilterUtil.XssFilter(response);
	}

	/**
	 * 로그뷰 페이지 선택한 로그를 rsfft에 요청해서 불러오는 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (path - 로그 경로,
	 *  page - 로그뷰 페이지 페이징 수(요청 바이트 사이즈 계산 시 사용),
	 *  size - 사이즈(요청 바이트 사이즈 계산 시 사용))
	 *  @return response - 로그 내용 리턴 
	 */
	@RequestMapping(value = "/getLog.do", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getLog(HttpServletRequest request, Locale local, Model model) throws IOException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String response = "";

		if (userInfo != null) {
			boolean nowAcc = nowAccessChk2(request, "systemManage.logView");
			if (!nowAcc) {
				return response;
			}
			EtcConfigInfo logInfo = new EtcConfigInfo();
			logInfo.setGroupKey("LOG");
			logInfo.setConfigKey("LOG_PATH");
			List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(logInfo);
			String path = result.get(0).getConfigValue();
			logInfo.setConfigKey("LOG_URL");
			result = etcConfigInfoService.selectEtcConfigInfo(logInfo);
			String ip = result.get(0).getConfigValue();

			URL url = new URL(ip + "/log?path=" + path + XssFilterUtil.XssFilter(request.getParameter("url")) + "&page="
					+ request.getParameter("page") + "&size=" + XssFilterUtil.XssFilter(request.getParameter("size")));
			// System.out.println(url);
			// URL url = new URL(request.getParameter("url"));

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(3000);
			InputStream in = null;
			ByteArrayOutputStream out = null;
			


			byte[] buf = new byte[1024 * 8];
			int length = 0;
			
			try {
				out = new ByteArrayOutputStream();
				if(conn.getInputStream() != null) {
					in = conn.getInputStream();
				};
				if(in != null) {
					while ((length = in.read(buf)) != -1) {
						if(out!= null) {
							out.write(buf, 0, length);
						}else {
							continue;
						}
					}
				}
				if(in!=null) {
					in.close();
				}
				if(out!=null) {
					out.close();
				}
				if(conn!=null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				logger.error("error"+e);
			}finally {
				if(in!=null) {
					try {
						in.close();
					}catch(Exception e) {
						logger.error("error"+e.getMessage());
					}
				}
				if(out!=null) {
					try {
						out.close();
					}catch(Exception e) {
						logger.error("error"+e.getMessage());
					}
				}
				if(conn!=null) {
					try {
						conn.disconnect();
					}catch (Exception e) {
					logger.error("e : "+e.getMessage());
					}
				}
			}

			response = new String(out.toByteArray(), "UTF-8");
		}
		return response;
	}

	/**
	 * 현재 접근 및 다운로드 권한 체크 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (AccessInfo - 현재 사용자 권한,
	 *  string - nowMenu)
	 *  @return readYn 접근 및 다운로드 가능 유무 
	 */
	private boolean nowAccessChk(HttpServletRequest request, String string) {
		boolean readYn = false;

		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, string);

		if (nowAccessInfo.getReadYn() != null && nowAccessInfo.getReadYn().equals("Y")
				&& nowAccessInfo.getExcelYn().equals("Y")) {
			readYn = true;
		}
		return readYn;
	}

	/**
	 * 현재 접근 및 다운로드 권한 체크 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (AccessInfo - 현재 사용자 권한,
	 *  string - nowMenu)
	 *  @return readYn 접근 및 다운로드 가능 유무 
	 */
	private boolean nowAccessChk2(HttpServletRequest request, String string) {
		boolean readYn = false;

		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, string);

		if (nowAccessInfo.getReadYn() != null && ("Y").equals(nowAccessInfo.getReadYn())) {
			readYn = true;
		}
		return readYn;
	}

	/**
	 * String 배열의 특정 위치에 value를 할당하는 메소드
	 *  
	 *  @param arr String 배열
	 *  @param value 배열에 담을 값
	 *  @param pos value를 담을 위치 
	 */
	private void setValue(String[] arr, String value, Integer pos) {
		if (!StringUtil.isNull(value))
			arr[pos] = value;
		else
			arr[pos] = "";
	}

	/**
	 * 녹취 삭제 관리 스케줄을 등록하는 메소드 
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (bgCode - 대분류 코드,
	 *  mgCode - 중분류 코드, 
	 *  sgCode - 소분류 코드,
	 *  delType - 삭제 유형,
	 *  delFileType - 파일 유형,
	 *  logType - 로그 유형,
	 *  delFilePathType - 삭제 파일 위치,
	 *  delYear - 삭제 스케줄(년),
	 *  delMonth - 삭제 스케줄(월))
	 *  @return jRes 스케줄 등록 성공 여부 및 실패 이유 리턴
	 */
	@RequestMapping(value = "/delRecfileInfoSave.do")
	public @ResponseBody AJaxResVO delRecfileInfoSave(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();

		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
				if (request.getParameter("delScheduleName") != null)
					delRecfileInfo.setR_del_schedule_name(request.getParameter("delScheduleName"));
				
				if (request.getParameter("bgCode") != null)
					delRecfileInfo.setR_bg_code(request.getParameter("bgCode"));

				if (request.getParameter("mgCode") != null)
					delRecfileInfo.setR_mg_code(request.getParameter("mgCode"));

				if (request.getParameter("sgCode") != null)
					delRecfileInfo.setR_sg_code(request.getParameter("sgCode"));

				if (request.getParameter("delType") != null)
					delRecfileInfo.setR_del_type(request.getParameter("delType"));

				if (request.getParameter("delFileType") != null)
					delRecfileInfo.setR_del_file_type(request.getParameter("delFileType"));

				if (request.getParameter("logType") != null)
					delRecfileInfo.setR_log_type(request.getParameter("logType"));

				if (request.getParameter("storageSendChk") != null)
					delRecfileInfo.setR_storage_send_chk_yn(request.getParameter("storageSendChk"));

				if (request.getParameter("delFilePathType") != null)
					delRecfileInfo.setR_del_file_path_type(request.getParameter("delFilePathType"));

				if (request.getParameter("scheCheckType") != null)
					delRecfileInfo.setR_scheduler_select(request.getParameter("scheCheckType"));

				if (request.getParameter("backWeek") != null)
					delRecfileInfo.setR_scheduler_week(request.getParameter("backWeek"));

				if (request.getParameter("backScheduler") != null)
					delRecfileInfo.setR_scheduler_day(request.getParameter("backScheduler"));

				if (request.getParameter("scheTime") != null)
					delRecfileInfo.setR_scheduler_hour(request.getParameter("scheTime"));
				
				if (request.getParameter("scheTimeMin") != null)
					delRecfileInfo.setR_scheduler_min(request.getParameter("scheTimeMin"));
				
				if (request.getParameter("delYear") != null)
					delRecfileInfo.setR_del_year(request.getParameter("delYear"));

				if (request.getParameter("delMonth") != null)
					delRecfileInfo.setR_del_month(request.getParameter("delMonth"));

				if (request.getParameter("delDay") != null)
					delRecfileInfo.setR_del_day(request.getParameter("delDay"));
					
				if (request.getParameter("delYearOffset") != null)
					delRecfileInfo.setR_del_year_offset(request.getParameter("delYearOffset"));
				
				if (request.getParameter("delMonthOffset") != null)
					delRecfileInfo.setR_del_month_offset(request.getParameter("delMonthOffset"));
				
				if (request.getParameter("delDayOffset") != null)
					delRecfileInfo.setR_del_day_offset(request.getParameter("delDayOffset"));

				if (request.getParameter("delSearchType") != null)
					delRecfileInfo.setR_del_search_type(request.getParameter("delSearchType"));
				
				if (request.getParameter("path") != null)
					delRecfileInfo.setR_del_path(request.getParameter("path"));

				delRecfileInfo.setR_del_select("D");
				
				List<DelRecfileInfo> dupSchedule = delRecfileInfoService.selectDelRecfileInfo(delRecfileInfo);
				int insertDelRecfileInfoResult = 0;
				if (dupSchedule.size() > 0) { // 중복 key
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "duplicate key");
				} else {
					insertDelRecfileInfoResult = delRecfileInfoService.insertDelRecfileInfo(delRecfileInfo);
					
					// 삭제 스케줄 적용 할 IP, PORT 조회
					EtcConfigInfo fileDeleteScheduleConfig= new EtcConfigInfo();
					fileDeleteScheduleConfig.setGroupKey("SYSTEM");
					fileDeleteScheduleConfig.setConfigKey("FILE_DELETE_SCHEDULE_IP");
					List<EtcConfigInfo> fileDeleteScheduleIpArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
					if(fileDeleteScheduleIpArr == null || fileDeleteScheduleIpArr.size() == 0) {
						fileDeleteScheduleConfig.setDesc("FILE_DELETE_SCHEDULE_IP Setting");
						fileDeleteScheduleConfig.setConfigValue("localhost");
						etcConfigInfoService.insertEtcConfigInfo(fileDeleteScheduleConfig);
						
						fileDeleteScheduleIpArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
					}
					String[] fileDeleteScheduleIp = fileDeleteScheduleIpArr.get(0).getConfigValue().split(",");
					
					fileDeleteScheduleConfig.setGroupKey("SYSTEM");
					fileDeleteScheduleConfig.setConfigKey("FILE_DELETE_SCHEDULE_PORT");
					List<EtcConfigInfo> fileDeleteSchedulePortArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
					int fileDeleteSchedulePort = 28885;
					if (fileDeleteSchedulePortArr.size() > 0) {
						fileDeleteSchedulePort = Integer.parseInt(fileDeleteSchedulePortArr.get(0).getConfigValue());
					}
					
					for (int i = 0; i < fileDeleteScheduleIp.length; i++) {
						// 시스템 리스트 가져오기
						httpConnection("HTTP://"+fileDeleteScheduleIp[i]+":"+fileDeleteSchedulePort+"/addSchedule?");
					}
					
					if (insertDelRecfileInfoResult > 0) {
						// 삭제 스케줄 등록 성공 이력 남기기
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						String logContents = messageSource.getMessage("admin.log.contents.DELETE-SCHEDULER-INSERT-SUCCESS", null,Locale.getDefault()) + " [ "+delRecfileInfo.toString(messageSource)+" ]";
						logService.writeLog(request, "REC_DELETE", "INSERT-SUCCESS", logContents);
					} else {
						// 삭제 스케줄 등록 실패 이력 남기기
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "insert fail"); 
						logService.writeLog(request, "REC_DELETE", "INSERT-FAIL", messageSource.getMessage("admin.log.contents.DELETE-SCHEDULER-INSERT-FAIL", null,Locale.getDefault()));
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 녹취 삭제 관리 스케줄을 수정하는 메소드 
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (del_type - 삭제 유형,
	 *  del_file_type - 파일 유형,
	 *  log_type - 로그 유형,
	 *  del_file_path_type - 삭제 파일 위치,
	 *  del_year - 삭제 스케줄(년),
	 *  del_month - 삭제 스케줄(월),
	 *  delRecfileInfoSeq - 스케줄 시퀀스)
	 *  @return jRes 스케줄 수정 성공 여부 및 실패 이유 리턴
	 */
	@RequestMapping(value = "/updateDelRecfileInfo.do")
	public @ResponseBody AJaxResVO updateDelRecfileInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
				if (request.getParameter("del_schedule_name") != null)
					delRecfileInfo.setR_del_schedule_name(request.getParameter("del_schedule_name"));
				
				if (request.getParameter("del_type") != null)
					delRecfileInfo.setR_del_type(request.getParameter("del_type"));

				if (request.getParameter("del_file_type") != null)
					delRecfileInfo.setR_del_file_type(request.getParameter("del_file_type"));
				
				if (request.getParameter("log_type") != null)
					delRecfileInfo.setR_log_type(request.getParameter("log_type"));

				if (request.getParameter("storage_send_chk") != null)
					delRecfileInfo.setR_storage_send_chk_yn(request.getParameter("storage_send_chk"));
				
				if (request.getParameter("del_file_path_type") != null)
					delRecfileInfo.setR_del_file_path_type(request.getParameter("del_file_path_type"));

				if (request.getParameter("scheCheck_Type") != null)
					delRecfileInfo.setR_scheduler_select(request.getParameter("scheCheck_Type"));

				if (request.getParameter("back_Week") != null)
					delRecfileInfo.setR_scheduler_week(request.getParameter("back_Week"));

				if (request.getParameter("back_Scheduler") != null)
					delRecfileInfo.setR_scheduler_day(request.getParameter("back_Scheduler"));

				if (request.getParameter("sche_Time") != null)
					delRecfileInfo.setR_scheduler_hour(request.getParameter("sche_Time"));
				
				if (request.getParameter("sche_Time_Min") != null)
					delRecfileInfo.setR_scheduler_min(request.getParameter("sche_Time_Min"));
				
				if (request.getParameter("del_year") != null)
					delRecfileInfo.setR_del_year(request.getParameter("del_year"));

				if (request.getParameter("del_month") != null)
					delRecfileInfo.setR_del_month(request.getParameter("del_month"));

				if (request.getParameter("del_day") != null)
					delRecfileInfo.setR_del_day(request.getParameter("del_day"));

				if (request.getParameter("del_year_offset") != null)
					delRecfileInfo.setR_del_year_offset(request.getParameter("del_year_offset"));
				
				if (request.getParameter("del_month_offset") != null)
					delRecfileInfo.setR_del_month_offset(request.getParameter("del_month_offset"));
				
				if (request.getParameter("del_day_offset") != null)
					delRecfileInfo.setR_del_day_offset(request.getParameter("del_day_offset"));

				if (request.getParameter("del_search_type") != null)
					delRecfileInfo.setR_del_search_type(request.getParameter("del_search_type"));
				
				if (request.getParameter("path") != null)
					delRecfileInfo.setR_del_path(request.getParameter("path"));
				
				if (request.getParameter("delRecfileInfoSeq") != null)
					delRecfileInfo.setR_seq(request.getParameter("delRecfileInfoSeq"));

				int updateDelRecfileInfoResult = delRecfileInfoService.updateDelRecfileInfo(delRecfileInfo);
				
				// 삭제 스케줄 적용 할 IP, PORT 조회
				EtcConfigInfo fileDeleteScheduleConfig= new EtcConfigInfo();
				fileDeleteScheduleConfig.setGroupKey("SYSTEM");
				fileDeleteScheduleConfig.setConfigKey("FILE_DELETE_SCHEDULE_IP");
				List<EtcConfigInfo> fileDeleteScheduleIpArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
				if(fileDeleteScheduleIpArr == null || fileDeleteScheduleIpArr.size() == 0) {
					fileDeleteScheduleConfig.setDesc("FILE_DELETE_SCHEDULE_IP Setting");
					fileDeleteScheduleConfig.setConfigValue("localhost");
					etcConfigInfoService.insertEtcConfigInfo(fileDeleteScheduleConfig);
					
					fileDeleteScheduleIpArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
				}
				String[] fileDeleteScheduleIp = fileDeleteScheduleIpArr.get(0).getConfigValue().split(",");
				
				fileDeleteScheduleConfig.setGroupKey("SYSTEM");
				fileDeleteScheduleConfig.setConfigKey("FILE_DELETE_SCHEDULE_PORT");
				List<EtcConfigInfo> fileDeleteSchedulePortArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
				int fileDeleteSchedulePort = 28885;
				if (fileDeleteSchedulePortArr.size() > 0) {
					fileDeleteSchedulePort = Integer.parseInt(fileDeleteSchedulePortArr.get(0).getConfigValue());
				}
				
				for (int i = 0; i < fileDeleteScheduleIp.length; i++) {
					// 시스템 리스트 가져오기
					httpConnection("HTTP://"+fileDeleteScheduleIp[i]+":"+fileDeleteSchedulePort+"/reSchedule?seq="+request.getParameter("delRecfileInfoSeq"));
				}
				
				if (updateDelRecfileInfoResult > 0) {
					// 삭제 스케줄 수정 성공 이력 남기기
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					String logContents = messageSource.getMessage("admin.log.contents.DELETE-SCHEDULER-UPDATE-SUCCESS", null,Locale.getDefault()) + " [ "+delRecfileInfo.toString(messageSource)+" ]";
					logService.writeLog(request, "REC_DELETE", "UPDATE-SUCCESS", logContents);
				} else {
					// 삭제 스케줄 수정 실패 이력 남기기
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "insert fail");
					logService.writeLog(request, "REC_DELETE", "UPDATE-FAIL", messageSource.getMessage("admin.log.contents.DELETE-SCHEDULER-UPDATE-FAIL", null,Locale.getDefault()));
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 녹취 삭제 관리 스케줄을 삭제하는 메소드 
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (idx - 삭제 스케줄 시퀀스 String(','로 구분))
	 *  @return jRes 스케줄 삭제 성공 여부 및 실패 이유 리턴
	 */
	@RequestMapping(value = "/deleteDelRecfileInfo.do")
	public @ResponseBody AJaxResVO deleteDelRecfileInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				if (request.getParameter("idx") != null) {
					int deleteDelRecfileInfoResult = 0;
					String[] arrIdx = request.getParameter("idx").split(",");

					for (int i = 0; i < arrIdx.length; i++) {
						DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
						delRecfileInfo.setR_seq(arrIdx[i]);
						deleteDelRecfileInfoResult += delRecfileInfoService.deleteDelRecfileInfo(delRecfileInfo);
					}

					if (deleteDelRecfileInfoResult == arrIdx.length) {
						// 삭제 스케줄 적용 할 IP, PORT 조회
						EtcConfigInfo fileDeleteScheduleConfig= new EtcConfigInfo();
						fileDeleteScheduleConfig.setGroupKey("SYSTEM");
						fileDeleteScheduleConfig.setConfigKey("FILE_DELETE_SCHEDULE_IP");
						List<EtcConfigInfo> fileDeleteScheduleIpArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
						if(fileDeleteScheduleIpArr == null || fileDeleteScheduleIpArr.size() == 0) {
							fileDeleteScheduleConfig.setDesc("FILE_DELETE_SCHEDULE_IP Setting");
							fileDeleteScheduleConfig.setConfigValue("localhost");
							etcConfigInfoService.insertEtcConfigInfo(fileDeleteScheduleConfig);
							
							fileDeleteScheduleIpArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
						}
						String[] fileDeleteScheduleIp = fileDeleteScheduleIpArr.get(0).getConfigValue().split(",");
						
						fileDeleteScheduleConfig.setGroupKey("SYSTEM");
						fileDeleteScheduleConfig.setConfigKey("FILE_DELETE_SCHEDULE_PORT");
						List<EtcConfigInfo> fileDeleteSchedulePortArr = etcConfigInfoService.selectEtcConfigInfo(fileDeleteScheduleConfig);
						int fileDeleteSchedulePort = 28885;
						if (fileDeleteSchedulePortArr.size() > 0) {
							fileDeleteSchedulePort = Integer.parseInt(fileDeleteSchedulePortArr.get(0).getConfigValue());
						}
						
						for (int i = 0; i < fileDeleteScheduleIp.length; i++) {
							// 시스템 리스트 가져오기
							httpConnection("HTTP://"+fileDeleteScheduleIp[i]+":"+fileDeleteSchedulePort+"/delSchedule?seq="+request.getParameter("idx"));
						}
						
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						// 삭제 스케줄 삭제 성공 이력 남기기
						String logContents = messageSource.getMessage("admin.log.contents.DELETE-SCHEDULER-DELETE-SUCCESS", null,Locale.getDefault()) + " [ seq : "+request.getParameter("idx")+" ]";
						logService.writeLog(request, "REC_DELETE", "DELETE-SUCCESS", logContents);
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						// 삭제 스케줄 삭제 실패 이력 남기기
						jRes.addAttribute("msg", "delete fail");
						logService.writeLog(request, "REC_DELETE", "DELETE-FAIL", messageSource.getMessage("admin.log.contents.DELETE-SCHEDULER-DELETE-FAIL", null,Locale.getDefault()));
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 녹취 백업 관리 스케줄을 등록하는 메소드 
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (bgCode - 대분류 코드,
	 *  mgCode - 중분류 코드,
	 *  sgCode - 소분류 코드,
	 *  backType - 백업 유형(백업, 이동),
	 *  logType - 로그 유형,
	 *  decType - 복호화 유형,
	 *  urlUpdateType - listen url 수정 여부 옵션,
	 *  overWriteType - 파일 덮어쓰기 여부 옵션,
	 *  conformityType - 유효성 검사 여부 옵션,
	 *  dualBackupType - 듀얼 녹취 백업 여부 옵션,
	 *  scheCheckType - 스케줄 타입,
	 *  backWeek - 백업 주,
	 *  backScheduler - 백업 일,
	 *  scheTime - 백업 시,
	 *  PreviousDataThird - 백업 데이터 기간 타입,
	 *  PreviousDataMonth - 백업 데이터 기간 월, 
	 *  PreviousDataDay - 백업 데이터 기간 일, 
	 *  PreviousDataTime - 백업 데이터 기간 시,
	 *  backupPath - 백업 경로)
	 *  )
	 *  @return jRes 백업 스케줄 등록 성공 여부 및 실패 이유 리턴
	 */
	@RequestMapping(value = "/backupRecfileInfoSave.do")
	public @ResponseBody AJaxResVO backupRecfileInfoSave(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();

		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
				String previousData = "";

				if (request.getParameter("bgCode") != null)
					delRecfileInfo.setR_bg_code(request.getParameter("bgCode"));

				if (request.getParameter("mgCode") != null)
					delRecfileInfo.setR_mg_code(request.getParameter("mgCode"));

				if (request.getParameter("sgCode") != null)
					delRecfileInfo.setR_sg_code(request.getParameter("sgCode"));

				if (request.getParameter("backType") != null)
					delRecfileInfo.setR_back_type(request.getParameter("backType"));

				if (request.getParameter("logType") != null)
					delRecfileInfo.setR_log_type(request.getParameter("logType"));

				if (request.getParameter("decType") != null)
					delRecfileInfo.setR_decode_type(request.getParameter("decType"));
					
				if (request.getParameter("urlUpdateType") != null)
					delRecfileInfo.setR_url_update_type(request.getParameter("urlUpdateType"));
					
				if (request.getParameter("overWriteType") != null)
					delRecfileInfo.setR_overwrite_type(request.getParameter("overWriteType"));	
					
				if (request.getParameter("conformityType") != null)
					delRecfileInfo.setR_conformity_type(request.getParameter("conformityType"));
				
				if (request.getParameter("dualBackupType") != null)
					delRecfileInfo.setR_dual_backup_type(request.getParameter("dualBackupType"));
				
				if (request.getParameter("scheCheckType") != null)
					delRecfileInfo.setR_scheduler_select(request.getParameter("scheCheckType"));

				if (request.getParameter("backWeek") != null)
					delRecfileInfo.setR_scheduler_week(request.getParameter("backWeek"));

				if (request.getParameter("backScheduler") != null)
					delRecfileInfo.setR_scheduler_day(request.getParameter("backScheduler"));

				if (request.getParameter("scheTime") != null)
					delRecfileInfo.setR_scheduler_hour(request.getParameter("scheTime"));

				if (request.getParameter("PreviousDataThird") != null)
					delRecfileInfo.setR_back_select(request.getParameter("PreviousDataThird"));

				if (request.getParameter("PreviousDataMonth") != null)
					if (!"".equals(request.getParameter("PreviousDataMonth")))
						previousData = request.getParameter("PreviousDataMonth");

				if (request.getParameter("PreviousDataDay") != null)
					if (!"".equals(request.getParameter("PreviousDataDay")))
						previousData = request.getParameter("PreviousDataDay");

				if (request.getParameter("PreviousDataTime") != null)
					if (!"".equals(request.getParameter("PreviousDataTime")))
						previousData = request.getParameter("PreviousDataTime");

				if ("".equals(previousData)) {
					previousData = request.getParameter("sdatebackup") + ":" + request.getParameter("edatebackup");
				}

				if (request.getParameter("backupPath") != null)
					delRecfileInfo.setR_back_path(request.getParameter("backupPath"));

				delRecfileInfo.setR_back_date(previousData);

				int insertDelRecfileInfoResult = delRecfileInfoService.insertBackRecfileInfo(delRecfileInfo);

				if (insertDelRecfileInfoResult > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "insert fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 녹취 백업 관리 스케줄을 수정하는 메소드 
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (back_type - 백업 유형(백업, 이동),
	 *  log_type - 로그 유형,
	 *  dec_Type - 복호화 유형,
	 *  urlUpdate_Type - listen url 수정 여부 옵션,
	 *  overWrite_Type - 파일 덮어쓰기 여부 옵션,
	 *  conformity_Type - 유효성 검사 여부 옵션,
	 *  dualBackup_Type - 듀얼 녹취 백업 여부 옵션,
	 *  backRecfileInfoSeq - 백업 스케줄 시퀀스,
	 *  scheCheck_Type - 스케줄 타입,
	 *  back_Week - 백업 주,
	 *  back_Scheduler - 백업 일,
	 *  sche_Time - 백업 시,
	 *  PreviousData_Third - 백업 데이터 기간 타입,
	 *  PreviousData_Month - 백업 데이터 기간 월, 
	 *  PreviousData_Day - 백업 데이터 기간 일, 
	 *  PreviousData_Time - 백업 데이터 기간 시,
	 *  backup_Path - 백업 경로)
	 *  )
	 *  @return jRes 백업 스케줄 수정 성공 여부 및 실패 이유 리턴
	 */
	@RequestMapping(value = "/updateBackupRecfileInfo.do")
	public @ResponseBody AJaxResVO updateBackupRecfileInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			String previousData = "";
			if (userInfo != null) {
				DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
				if (request.getParameter("back_type") != null)
					delRecfileInfo.setR_back_type(request.getParameter("back_type"));

				if (request.getParameter("log_type") != null)
					delRecfileInfo.setR_log_type(request.getParameter("log_type"));
				
				if (request.getParameter("dec_Type") != null)
					delRecfileInfo.setR_decode_type(request.getParameter("dec_Type"));
					
				if (request.getParameter("urlUpdate_Type") != null)
					delRecfileInfo.setR_url_update_type(request.getParameter("urlUpdate_Type"));
					
				if (request.getParameter("overWrite_Type") != null)
					delRecfileInfo.setR_overwrite_type(request.getParameter("overWrite_Type"));	
					
				if (request.getParameter("conformity_Type") != null)
					delRecfileInfo.setR_conformity_type(request.getParameter("conformity_Type"));
				
				if (request.getParameter("dualBackup_Type") != null)
					delRecfileInfo.setR_dual_backup_type(request.getParameter("dualBackup_Type"));
				
				if (request.getParameter("backRecfileInfoSeq") != null)
					delRecfileInfo.setR_seq(request.getParameter("backRecfileInfoSeq"));

				if (request.getParameter("scheCheck_Type") != null)
					delRecfileInfo.setR_scheduler_select(request.getParameter("scheCheck_Type"));

				if (request.getParameter("back_Week") != null)
					delRecfileInfo.setR_scheduler_week(request.getParameter("back_Week"));

				if (request.getParameter("back_Scheduler") != null)
					delRecfileInfo.setR_scheduler_day(request.getParameter("back_Scheduler"));

				if (request.getParameter("sche_Time") != null)
					delRecfileInfo.setR_scheduler_hour(request.getParameter("sche_Time"));
				
				if (request.getParameter("PreviousData_Third") != null)
					delRecfileInfo.setR_back_select(request.getParameter("PreviousData_Third"));

				if (request.getParameter("PreviousData_Month") != null)
					if (!"".equals(request.getParameter("PreviousData_Month")))
						previousData = request.getParameter("PreviousData_Month");

				if (request.getParameter("PreviousData_Day") != null)
					if (!"".equals(request.getParameter("PreviousData_Day")))
						previousData = request.getParameter("PreviousData_Day");

				if (request.getParameter("PreviousData_Time") != null)
					if (!"".equals(request.getParameter("PreviousData_Time")))
						previousData = request.getParameter("PreviousData_Time");
				
				if ("".equals(previousData)) {
					previousData = request.getParameter("sdatebackup") + ":" + request.getParameter("edatebackup");
				}

				delRecfileInfo.setR_back_date(previousData);

				if (request.getParameter("backup_Path") != null)
					delRecfileInfo.setR_back_path(request.getParameter("backup_Path"));				
				

				int updateDelRecfileInfoResult = delRecfileInfoService.updateBackRecfileInfo(delRecfileInfo);

				if (updateDelRecfileInfoResult > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "insert fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 녹취 백업 관리 스케줄을 삭제하는 메소드 
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (idx - 백업 스케줄 시퀀스 String(','로 구분))
	 *  @return jRes 백업 스케줄 삭제 성공 여부 및 실패 이유 리턴
	 */
	@RequestMapping(value = "/deleteBackupRecfileInfo.do")
	public @ResponseBody AJaxResVO deleteBackupRecfileInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				if (request.getParameter("idx") != null) {
					int deleteDelRecfileInfoResult = 0;
					String[] arrIdx = request.getParameter("idx").split(",");

					for (int i = 0; i < arrIdx.length; i++) {
						DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
						delRecfileInfo.setR_seq(arrIdx[i]);
						deleteDelRecfileInfoResult += delRecfileInfoService.deleteBackRecfileInfo(delRecfileInfo);
					}

					if (deleteDelRecfileInfoResult == arrIdx.length) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "delete fail");
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 즉시 백업 시작 요청된 스케줄 정보 조회하는 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (seq - 백업 스케줄 시퀀스)
	 */
	@RequestMapping(value = "/sendBackupRecfileNowStart.do")
	public @ResponseBody AJaxResVO sendBackupRecfileNowStart(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				if (request.getParameter("seq") != null) {

					DelRecfileInfo delRecfileInfoChk = new DelRecfileInfo();
					delRecfileInfoChk.setR_seq(request.getParameter("seq"));
					List<DelRecfileInfo> delRecfileList = delRecfileInfoService.selectBackRecfileInfo();

				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 백업 경로 유효성 검사하는 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (path - 백업 경로)
	 *  @return jRes 백업 경로 존재 유무 리턴
	 */
//	@RequestMapping(value = "/sendBackupPathCheck.do")
//	public @ResponseBody AJaxResVO sendBackupPathCheck(HttpServletRequest request, Locale local, Model model) {
//		AJaxResVO jRes = new AJaxResVO();
//		try {
//			if (request.getParameter("path") != null) {
//
//				String path = request.getParameter("path");
//
//				File file = new File(path);
//
//				if (file.exists()) {
//					jRes.setResult("0");
//				} else {
//					jRes.setResult("1");
//				}
//				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//			}
//		} catch (Exception e) {
//			jRes.setSuccess(AJaxResVO.SUCCESS_N);
//			jRes.setResult("3");
//			jRes.addAttribute("msg", "exception");
//		}
//		return jRes;
//	}

	/**
	 * 백업 진행 상태 업데이트 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (path - 백업 경로)
	 *  @return jRes - 백업 진행 상태 업데이트 성공 여부, backSateCheck - 백업 진행 상태 정보 배열 리턴
	 */
	@RequestMapping(value = "/sendBackupState.do")
	public @ResponseBody AJaxResVO sendBackupState(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			
			DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
			List<DelRecfileInfo> delRecfileList = delRecfileInfoService.selectNowBackRecfileInfo(delRecfileInfo);
			String[][] backSateCheck = new String[delRecfileList.size()][6];
			for(int i=0; i< delRecfileList.size(); i++) {
				backSateCheck[i][0] = delRecfileList.get(i).getR_seq();
				
				boolean ThreadChk = ThreadCheck("RecseeBackUp"+delRecfileList.get(i).getR_seq());
							
				if((delRecfileList.get(i).getR_start_time() != null && !"".equals(delRecfileList.get(i).getR_start_time()) || ThreadChk == true)) {
					backSateCheck[i][1] = "N";
					
					String startTime = delRecfileList.get(i).getR_start_time();
					
					
					SimpleDateFormat TempDate = new SimpleDateFormat("yyyy-MM-dd");					
					Date TempDate1 = new Date();	
					String TempDate2 = TempDate.format(TempDate1);						
										
					SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");					
					Date Datetime = new Date();	
					String time1 = format1.format(Datetime);					
					DelRecfileInfo StartTimeUpdate = new DelRecfileInfo();
					StartTimeUpdate.setR_seq(delRecfileList.get(i).getR_seq());
					if(ThreadChk == true) {
						StartTimeUpdate.setR_back_state("BackUpStarting");
					}
					StartTimeUpdate.setR_run_time(time1);
					delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
					
					time1 = time1.substring(0, 2)+":"+time1.substring(2, 4)+":"+time1.substring(4, 6);
									
					
					String start = TempDate2 +" " + startTime.substring(0, 2)+":"+startTime.substring(2, 4)+":"+startTime.substring(4, 6);
					String end = TempDate2 +" " + time1;
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date beginDate = formatter.parse(start);
					Date endDate = formatter.parse(end);
					
					long diff = endDate.getTime() - beginDate.getTime();
					long timetemp = diff / (1000);			
					long min = timetemp/60;
					long hour = min / 60;
					long sec = timetemp % 60;
					     min = min%60;
						
					
					String time = String.format("%02d", hour)+":"+String.format("%02d", min)+":"+String.format("%02d", sec);
					
					backSateCheck[i][2] = time;
				}else {
					backSateCheck[i][1] = "Y";
					backSateCheck[i][2] = "-";
				}		
				
				backSateCheck[i][3] =  delRecfileList.get(i).getR_back_state();
				
				String startTime = delRecfileList.get(i).getR_start_time() == null ? "-" : delRecfileList.get(i).getR_start_time();
				if(startTime.length() < 6) {
					backSateCheck[i][4] =  "-";
				}else {
					backSateCheck[i][4] =  startTime.substring(0, 2)+":"+startTime.substring(2, 4)+":"+startTime.substring(4, 6);
				}
				
				String endTime = delRecfileList.get(i).getR_end_time() == null ? "-" : delRecfileList.get(i).getR_end_time();
				if(endTime.length() < 6) {
					backSateCheck[i][5] =  "-";
				}else {
					backSateCheck[i][5] =  endTime.substring(0, 2)+":"+endTime.substring(2, 4)+":"+endTime.substring(4, 6);
				}
			}

			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("backSateCheck",backSateCheck);
			jRes.setResult("0");

		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("3");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	/**
	 * 백업 스케줄 즉시 시작 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (seq - 백업 스케줄 시퀀스)
	 *  @return jRes - 백업 스케줄 시작 성공 여부 리턴
	 */
//	@RequestMapping(value = "/NowBackUpStart.do")
//	public @ResponseBody AJaxResVO NowBackUpStart(HttpServletRequest request, Locale local, Model model) {
//		AJaxResVO jRes = new AJaxResVO();
//		try {
//			if (request.getParameter("seq") != null) {
//
//				final String seqData = request.getParameter("seq");
//
//				String returnVal = "0";
//				final Calendar cal = Calendar.getInstance();
//				final String currentTime = String.valueOf(cal.get(Calendar.YEAR))
//						+ String.format("%02d", cal.get(Calendar.MONTH) + 1)
//						+ String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
//				final String fileName = (new SimpleDateFormat("yyyymmddhhmmss")).format(new Date());
//				final String LINE_SEPARATOR = System.getProperty("line.separator");
//
//				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
//				etcConfigInfo.setGroupKey("SYSTEM");
//				etcConfigInfo.setConfigKey("HTTPS");
//				List<EtcConfigInfo> httpsConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
//
//				String tempValue = "http";
//
//				if (httpsConfigResult != null && "https".equals(httpsConfigResult.get(0).getConfigValue())) {
//					tempValue = "https";
//				}
//
//				final String http = tempValue;
//
//				Runnable r = new Runnable() {
//
//					@Override
//					public void run() {
//
//						try {
//							String ThreadTime = "0";
//							DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
//							if (seqData != null) {
//								delRecfileInfo.setR_seq(seqData);
//							}
//
//							if ("0".equals(ThreadTime)) {
//								ThreadTime = currentTime;
//							} else if (!ThreadTime.equals(currentTime)) {
//								delRecfileInfo.setR_start_check("N");
//								int resultNum = delRecfileInfoService.updatecRsBackFileFlag(delRecfileInfo);
//								if (resultNum > 0) {
//									ThreadTime = currentTime;
//								}
//							}
//
//							List<DelRecfileInfo> delRecfileList = delRecfileInfoService
//									.selectNowBackRecfileInfo(delRecfileInfo);
//
//							if (delRecfileList != null && delRecfileList.size() > 0) {
//
//								for (int i = 0; i < delRecfileList.size(); i++) {
//
//									String bgCode = delRecfileList.get(i).getR_bg_code();
//									String mgCode = delRecfileList.get(i).getR_mg_code();
//									String sgCode = delRecfileList.get(i).getR_sg_code();
//
//									// 백업 타입
//									// B : 파일 배업
//									// M : 파일 이동
//									String backType = delRecfileList.get(i).getR_back_type();
//
//									// 로그 타입
//									// Y : 인덱스 생성
//									// N : 인덱스 미생성
//									String logType = delRecfileList.get(i).getR_log_type();
//									
//									// 복호화 여부
//									// Y : 복호화
//									// N : 복호화 X
//									String decodeType = delRecfileList.get(i).getR_decode_type();
//									
//									// Listen Url 
//									// Y : 업데이트
//									// N : 업데이트 X								
//									String urlUpdateType = delRecfileList.get(i).getR_url_update_type();
//
//									// 적합성 검사
//									// Y : 사용
//									// N : 미사용
//									String conformityType = delRecfileList.get(i).getR_conformity_type();
//									
//									//덮어쓰기 여부
//									// Y : 덮어쓰기
//									// N : 덮어쓰기 X
//									String overwriteType = delRecfileList.get(i).getR_overwrite_type();
//
//									//듀얼 서버 백업 여부
//									// Y : 백업 포함
//									// N : 백업 미포함
//									String dualBackupType = delRecfileList.get(i).getR_dual_backup_type();
//									
//									// 스케줄러 종류
//									String schedulerSelect = delRecfileList.get(i).getR_scheduler_select();
//									// 스케줄러 주
//									String schedulerWeek = delRecfileList.get(i).getR_scheduler_week();
//									// 스케줄러 일
//									String schedulerDay = delRecfileList.get(i).getR_scheduler_day();
//									// 스케줄러 시간
//									String schedulerHour = delRecfileList.get(i).getR_scheduler_hour();
//									// 백업 종류(월,일,시)
//									String backSelect = delRecfileList.get(i).getR_back_select();
//									// 백업 실행 데이터
//									String backdate = delRecfileList.get(i).getR_back_date();
//									// 백업 경로
//									String backPath = delRecfileList.get(i).getR_back_path();
//									// 시퀀스
//									String seq = delRecfileList.get(i).getR_seq();
//									// 실행 여부
//									String startCheck = delRecfileList.get(i).getR_start_check();
//									// 실행 시간
//									String startTime = delRecfileList.get(i).getR_start_time();
//									
//									if (!StringUtil.isNull(startTime) && !"".equals(startTime)) {
//										continue;
//									}
//
//
//									File txtFile = null;
//									FileWriter fw = null;
//
//									if (seqData == null) {
//
//										if ("Y".equals(startCheck)) {
//											continue;
//										}
//
//										// 백업 스케줄러가 주일때
//										if ("W".equals(schedulerSelect)) {
//
//											// 1 : 일, 2 : 월, 3 : 화, 4 : 수, 5 : 목, 6 : 금, 7 : 토
//											String calWeek = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
//
//											// 스케줄러 주 와 현재 주가 동일할때
//											if (calWeek.equals(schedulerWeek)) {
//												String calHour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
//												// 스케줄로 시간과 현재 시간이 동일하지 않을떄
//												if (!calHour.equals(schedulerHour)) {
//													continue;
//												}
//											} else {
//												continue;
//											}
//
//										}
//										// 백업 스케줄러가 월일대
//										else if ("M".equals(schedulerSelect)) {
//
//											String calDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
//
//											// 스케줄러 일 과 현주 일이 동일할때
//											if (calDay.equals(schedulerDay)) {
//												String calHour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
//												// 스케줄로 시간과 현재 시간이 동일하지 않을떄
//												if (!calHour.equals(schedulerHour)) {
//													continue;
//												}
//											} else {
//												continue;
//											}
//										}
//										// 백업 스케줄러가 일일때
//										// 백업 스케줄러가 월일대
//										else if ("D".equals(schedulerSelect)) {
//											String calHour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
//											// 스케줄로 시간과 현재 시간이 동일하지 않을떄
//											if (!calHour.equals(schedulerHour)) {
//												continue;
//											}
//										}
//										// 백업 스케줄러를 사용하지 않을때
//										else if ("N".equals(schedulerSelect)) {
//											continue;
//										} else {
//											continue;
//										}
//									}
//
//									SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
//									Date time = new Date();
//									String time1 = format1.format(time);
//
//									DelRecfileInfo StartTimeUpdate = new DelRecfileInfo();
//									StartTimeUpdate.setR_seq(delRecfileList.get(i).getR_seq());
//									StartTimeUpdate.setR_start_time(time1);
//									StartTimeUpdate.setR_run_time("");
//									StartTimeUpdate.setR_end_time("");
//									StartTimeUpdate.setR_back_state("BackUpStarting");
//									delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//									
//									if ("Y".equals(logType)) {
//
//										File indexFolder = new File(backPath);
//
//										if (!indexFolder.exists()) {
//											indexFolder.mkdirs();
//										}
//
//										txtFile = new File(backPath + "/" + fileName + ".txt");
//										fw = new FileWriter(txtFile, true);
//									}
//									DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//
//									Date date = dateFormat.parse(currentTime);
//									cal.setTime(date);
//
//									String backHour = "235959";
//
//									switch (backSelect) {
//									case "day":
//										cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(backdate) * -1);
//										break;
//									case "mon":
//										cal.add(Calendar.MONTH, Integer.parseInt(backdate) * -1);
//										break;
//									case "hour":
//										backHour = String.format("%02d", Integer.parseInt(backdate)) + "0000";
//										break;
//									case "fromTo":
//										delRecfileList.get(i).setR_sdate_backup(backdate.split(":")[0]);
//										delRecfileList.get(i).setR_edate_backup(backdate.split(":")[1]);
//										break;
//									}
//
//									String backTime = dateFormat.format(cal.getTime());
//
//									delRecfileList.get(i).setBackTime(backTime);
//									/*int FileSize = 0;
//									try {
//									FileSize = delRecfileInfoService
//											.FileSizeRecfileFileBackup(delRecfileList.get(i));
//									}catch (Exception e) {
//										FileSize = 0;
//									}
//
//									File[] roots = File.listRoots();
//
//									String drive;
//									boolean backupStartcheck = false;
//
//									String filePath = backPath.replaceAll("/", "\\\\");
//
//									for (File root : roots) {
//										drive = root.getAbsolutePath();
//										if (filePath.contains(drive)) {
//											int useSize = (int) (root.getUsableSpace() / Math.pow(1024, 3)) + 1;
//
//											if (FileSize < useSize) {
//												backupStartcheck = true;
//											}
//										}
//									}
//
//									if (backupStartcheck == false) {
//										StartTimeUpdate = new DelRecfileInfo();
//										StartTimeUpdate.setR_seq(delRecfileList.get(i).getR_seq());
//										StartTimeUpdate.setR_start_time("");
//										StartTimeUpdate.setR_run_time("");
//										StartTimeUpdate.setR_end_time("");
//										StartTimeUpdate.setR_back_state("HardWare Size Error");
//										delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//										continue;
//									}*/
//
//
//									// 듀얼 녹취 백업 유무 - N 일경우 듀얼은 백업 조건에서 제외
//									if("N".equals(dualBackupType)) {
//										delRecfileList.get(i).setR_dual_backup_type(dualBackupType);
//									}
//									// 백업 시킬 데이터의 count
//									int dataCnt = delRecfileInfoService.countRecfileFileBackup(delRecfileList.get(i));
//
//									int cycle = (dataCnt / 500) + 1;
//
//									for (int currentcycle = 1; currentcycle <= cycle; currentcycle++) {
//										delRecfileList.get(i).setLimit(String.valueOf(currentcycle * 500));
//										delRecfileList.get(i).setOffset(String.valueOf((currentcycle * 500) - 500));
//
//										List<SearchListInfo> delRecfileListDown = delRecfileInfoService
//												.selectRecfileFileBackup(delRecfileList.get(i));
//										for (int j = 0; j < delRecfileListDown.size(); j++) {
//											// 파일 날짜
//											String RecDate = delRecfileListDown.get(j).getRecDate();
//											// 파일 명
//											String vFileName = delRecfileListDown.get(j).getvFileName();
//											// 녹취 서버 파일 IP
//											String vRecIp = delRecfileListDown.get(j).getvRecIp();
//											// 녹취 서버 파일 경로
//											String vRecFullpath = delRecfileListDown.get(j).getvRecFullpath();
//											// 스토리지 서버 IP
//											String vReadIp = delRecfileListDown.get(j).getvReadIp();
//											// 스토리지 서버 경로
//											String vReadFullpath = delRecfileListDown.get(j).getvRecFullpath();
//											// 콜 아이디
//											String callkeyAp = delRecfileListDown.get(j).getCallKeyAp();
//
//											String listenUrl = delRecfileListDown.get(j).getListenUrl();
//
//											String vSysCode = delRecfileListDown.get(j).getvSysCode();
//											
//											if ("https".equals(http)) {
//												listenUrl = listenUrl.replace("HTTP:", "HTTPS:");
//											}
//
//											String rsfftUrl = listenUrl.substring(0, listenUrl.indexOf("/listen"));
//											listenUrl = listenUrl.split("/listen?")[1].replace("?url=", "");
//											
//											String encYn = "false";
//											
//											// 복호화 여부
//											if("N".equals(decodeType)) {
//												encYn = "true";
//											}
//											
//											URL url = new URL(rsfftUrl + "/down?url=" + URLEncoder.encode(listenUrl, "UTF-8") + "&encYn="+encYn);
//											FileOutputStream fos = null;
//											try {
//												if ("Y".equals(dualBackupType) && vSysCode.startsWith("B")) {
//													File FileOverWriteCheck = new File(backPath + "/" + RecDate + "/DUAL/"+ listenUrl.substring(listenUrl.lastIndexOf("/") + 1));
//													
//													if(!FileOverWriteCheck.exists() || "Y".equals(overwriteType)) {						
//														ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//														File mkdirFoleder = new File(backPath + "/" + RecDate + "/DUAL");
//														if (!mkdirFoleder.isDirectory()) {
//															mkdirFoleder.mkdirs();
//														}
//														
//														
//														fos = new FileOutputStream(backPath + "/" + RecDate + "/DUAL/"
//																+ listenUrl.substring(listenUrl.lastIndexOf("/") + 1));
//														fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//																									
//													}else {
//														if (fos != null) fos.close();
//													}
//												} else {
//													File FileOverWriteCheck = new File(backPath + "/" + RecDate + "/"+ listenUrl.substring(listenUrl.lastIndexOf("/") + 1));
//													
//													if(!FileOverWriteCheck.exists() || "Y".equals(overwriteType)) {						
//														ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//														File mkdirFoleder = new File(backPath + "/" + RecDate);
//														if (!mkdirFoleder.isDirectory()) {
//															mkdirFoleder.mkdirs();
//														}
//														
//														fos = new FileOutputStream(backPath + "/" + RecDate + "/"
//																+ listenUrl.substring(listenUrl.lastIndexOf("/") + 1));
//														fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//																									
//													}else {
//														if (fos != null) fos.close();
//													}
//												}
//												
//												// 적합성 검사
//												if("Y".equals(conformityType )) {
//													
//													String updateSavePath = backPath + "/" + RecDate + "/"+listenUrl.substring(listenUrl.lastIndexOf("/") + 1);	
//													
//													File isExistsFile = new File(updateSavePath);
//													
//													if(!isExistsFile.isFile()) {
//														// 파일없으면 너 문제 있음
//													}else {
//														String fileSize = String.valueOf(isExistsFile.length());
//																												
//														URL url2 = new URL(rsfftUrl + "/fileSize?url=" + URLEncoder.encode(listenUrl, "UTF-8"));
//														BufferedReader br = new BufferedReader(new InputStreamReader(url2.openStream()));
//														//connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//
//											            String result = br.readLine().trim();										
//														
//											            System.out.println("fileSize : " + fileSize + " | result : " + result);
//											            if(!fileSize.equals(result)) {
//											            }
//											            
//													}
//													
//												}
//												
//												
//												// Listen Url Update 여부											
//												if("Y".equals(urlUpdateType)) {
//																																							
//													String ip = null;
//												    try {
//												        ip = InetAddress.getLocalHost().getHostAddress();
//												        
//												        SearchListInfo searchListInfo = new SearchListInfo();
//												        
//												        String updateSavePath = backPath + "/" + RecDate + "/"+listenUrl.substring(listenUrl.lastIndexOf("/") + 1);											        
//												        String updateListenUrl = "HTTP://"+ip+":28881/listen?url="+updateSavePath;
//												        String vFileFlag = "Y";
//												        
//												        if ("https".equals(http)) {
//												        	updateListenUrl = updateListenUrl.replace("HTTP:", "HTTPS:");
//														}
//												        
//												        
//												        if(!StringUtil.isNull(delRecfileListDown.get(j).getRecDate(),true)) 
//												        	searchListInfo.setRecDate(delRecfileListDown.get(j).getRecDate());
//												        
//												        if(!StringUtil.isNull(delRecfileListDown.get(j).getRecTime(),true)) 
//												        	searchListInfo.setRecTime(delRecfileListDown.get(j).getRecTime());
//												        
//												        if(!StringUtil.isNull(delRecfileListDown.get(j).getRecRtime(),true)) 
//												        	searchListInfo.setRecRtime(delRecfileListDown.get(j).getRecRtime());
//												        
//												        if(!StringUtil.isNull(delRecfileListDown.get(j).getvSysCode(),true)) 
//												        	searchListInfo.setvSysCode(delRecfileListDown.get(j).getvSysCode());
//												        
//												        if(!StringUtil.isNull(delRecfileListDown.get(j).getRecStartType(),true)) 
//												        	searchListInfo.setRecStartType(delRecfileListDown.get(j).getRecStartType());
//												        
//												        searchListInfo.setvReadIp(ip);
//												        searchListInfo.setvReadFullpath(updateSavePath);
//												        searchListInfo.setListenUrl(updateListenUrl);
//												        searchListInfo.setVsendFileFlag(vFileFlag);
//												        
//												        // Listen Url Update
//												        delRecfileInfoService.updateRsRecfileListenUrlUpdate(searchListInfo);
//												        
//												        
//												    } catch (Exception e) {
//												    	e.printStackTrace();
//												    }
//												}
//											} catch (Exception e) {
//												e.printStackTrace();
//												StartTimeUpdate = new DelRecfileInfo();
//												StartTimeUpdate.setR_seq(delRecfileList.get(i).getR_seq());
//												/*StartTimeUpdate.setR_start_time("");*/
//												StartTimeUpdate.setR_run_time("");
//												StartTimeUpdate.setR_end_time("");
//												StartTimeUpdate.setR_back_state("BackUp Error");
//												delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//												continue;
//											} finally {
//												if (fos != null)
//													fos.close();
//											}
//
//											if ("Y".equals(logType)) {
//												String strTemp = delRecfileListDown.get(j).getRecDate() + "|"
//														+ delRecfileListDown.get(j).getRecTime() + "|"
//														+ delRecfileListDown.get(j).getExtNum() + "|"
//														+ delRecfileListDown.get(j).getCustPhone1() + "|"
//														+ delRecfileListDown.get(j).getvFileName();
//												if(fw!=null) {
//													fw.write(strTemp);
//													fw.write(LINE_SEPARATOR);
//												}
//											}
//
//											String current = (new SimpleDateFormat("YYYYMMddHHMMSS"))
//													.format(new Date());
//											delRecfileListDown.get(j).setQueueNo2(current + "/" + backType);
//
//											delRecfileInfoService.updateRsRecfileBackCheck(delRecfileListDown.get(j));
//
//											// 파일 이동일떄 rsfft를 통하여 파일 삭제
//											if ("M".equals(backType)) {
//												URL urldelete = new URL(
//														rsfftUrl + "/fileDelete?fullpath=" + vRecFullpath);
//												urldelete.openStream();
//											}
//
//											// 파일 이동일떄 조회 및 청취에서 청취가 불가능하게 flag update
//											if ("M".equals(backType)) {
//												delRecfileInfoService.updateRsRecfileMove(delRecfileListDown.get(j));
//											}
//
//										}
//
//									}
//									if (seqData == null) {
//										delRecfileInfo.setR_start_check("Y");
//										delRecfileInfoService.updatecRsBackFileFlag(delRecfileInfo);
//									}
//
//									if (fw != null)
//										fw.close();
//
//									format1 = new SimpleDateFormat("HHmmss");
//									time = new Date();
//									time1 = format1.format(time);
//
//									StartTimeUpdate = new DelRecfileInfo();
//									StartTimeUpdate.setR_seq(delRecfileList.get(i).getR_seq());
//									StartTimeUpdate.setR_start_time("");
//									StartTimeUpdate.setR_run_time("");
//									StartTimeUpdate.setR_end_time(time1);
//									StartTimeUpdate.setR_back_state("BackUpCompliete");
//									delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				};
//				// jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//
//				Thread thread = new Thread(r, "RecseeBackUp" + seqData);
//
//				
//				Thread currentThread = Thread.currentThread();
//				ThreadGroup threadGroup = currentThread.getThreadGroup();
//				while (threadGroup.getParent() != null) {
//					threadGroup = threadGroup.getParent();
//				}
//				int activeCount = threadGroup.activeCount();
//				Thread[] activeThreads = new Thread[activeCount + 5];
//				int enumeratedCount = threadGroup.enumerate(activeThreads);
//				Thread finalizerThread = null;
//				for (int i = 0; i < enumeratedCount; i++) {
//					if (activeThreads[i].getName().equals("RecseeBackUp" + seqData)) {
//						finalizerThread = activeThreads[i];
//						break;
//					}
//				}
//
//				 if (finalizerThread == null) {
//					 logService.writeLog(request, "REC_BACKUP", "BACKUP-SUCCESS", "Now Backup Start");
//					 thread.start();
//
//					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//					jRes.setResult("0");
//				 } else {
//					 logService.writeLog(request, "REC_BACKUP", "BACKUP-SUCCESS", "Already Thread Start");
//					 jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//					 jRes.setResult("1");
//				 }
//			}
//		} catch (Exception e) {
//			 logService.writeLog(request, "REC_BACKUP", "BACKUP-FAIL", "ERROR");
//			jRes.setSuccess(AJaxResVO.SUCCESS_N);
//			jRes.setResult("2");
//			// jRes.addAttribute("msg", "exception");
//		}
//		return jRes;
//	}

	/**
	 * 백업 파일 다운로드 메소드
	 *  
	 *  @param request HttpServletRequest 파라미터 
	 *  (seq - 백업 스케줄 시퀀스)
	 *  @return jRes - 백업 파일 다운로드 성공 여부
	 */
//	@RequestMapping(value = "/BackUpFileDown.do")
//	public @ResponseBody AJaxResVO BackUpFileDown(HttpServletRequest request, HttpServletResponse response,
//			Locale local, Model model) {
//		AJaxResVO jRes = new AJaxResVO();
//		HttpSession session = request.getSession();
//		try {
//			if (request.getParameter("seq") != null) {
//
//				String seqData = request.getParameter("seq");
//
//				DelRecfileInfo delRecfileInfo = new DelRecfileInfo();
//				delRecfileInfo.setR_seq(seqData);
//				List<DelRecfileInfo> delRecfileList = delRecfileInfoService.selectNowBackRecfileInfo(delRecfileInfo);
//				
//				String startState = delRecfileList.get(0).getR_start_time();
//				
//				if(!StringUtil.isNull(startState) && !"".equals(startState)) {
//					response.setContentType("text/html; charset=UTF-8");
//
//					ServletOutputStream out = response.getOutputStream();
//
//					out.println("<script>alert('Aready Download Running'); self.close();</script>");
//
//					out.flush();
//				}else {				
//				
//					String path = delRecfileList.get(0).getR_back_path();
//	
//					SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
//	
//					Date time = new Date();
//	
//					String time1 = format1.format(time);
//					
//					DelRecfileInfo StartTimeUpdate = new DelRecfileInfo();
//					StartTimeUpdate.setR_seq(seqData);
//					StartTimeUpdate.setR_start_time(time1);
//					StartTimeUpdate.setR_run_time("");
//					StartTimeUpdate.setR_end_time("");
//					StartTimeUpdate.setR_back_state("DownLoading");
//					delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//	
//					String pathName = time1;
//					try {
//						CompreasZip.createZipFile(path, path, pathName);
//	
//					} catch (Throwable e) {
//						e.printStackTrace();
//						StartTimeUpdate = new DelRecfileInfo();
//						StartTimeUpdate.setR_seq(seqData);
//						StartTimeUpdate.setR_start_time("");
//						StartTimeUpdate.setR_run_time("");
//						StartTimeUpdate.setR_end_time("");
//						StartTimeUpdate.setR_back_state("DOWNLOAD Error");
//						delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//					}
//	
//					File outputFile = new File(path + "/" + pathName);
//	
//					response.setContentType("application/octet-stream");
//					response.setHeader("Content-Disposition", "attachment;filename=\"" + pathName + ".zip\";");
//	
//					FileInputStream fileInputStream = new FileInputStream(outputFile);
//					ServletOutputStream servletOutputStream = response.getOutputStream();
//	
//					byte b[] = new byte[1024];
//					int data = 0;
//	
//					while ((data = (fileInputStream.read(b, 0, b.length))) != -1) {
//						servletOutputStream.write(b, 0, data);
//					}
//	
//					servletOutputStream.flush();
//					servletOutputStream.close();
//					fileInputStream.close();
//	
//					if (outputFile.exists()) {
//						outputFile.delete();
//					}
//					
//					format1 = new SimpleDateFormat("HHmmss");
//					
//					time = new Date();
//	
//					time1 = format1.format(time);
//					
//					StartTimeUpdate = new DelRecfileInfo();
//					StartTimeUpdate.setR_seq(seqData);
//					StartTimeUpdate.setR_start_time("");
//					StartTimeUpdate.setR_run_time("");
//					StartTimeUpdate.setR_end_time(time1);
//					StartTimeUpdate.setR_back_state("DOWNLOAD Complete");
//					delRecfileInfoService.updateBackRecfileInfo(StartTimeUpdate);
//				}
//
//			} else {
//				response.setContentType("text/html; charset=UTF-8");
//
//				ServletOutputStream out = response.getOutputStream();
//
//				out.println("<script>alert('Path Error Download'); self.close();</script>");
//
//				out.flush();
//			}
//		} catch (Exception e) {
//			jRes.setSuccess(AJaxResVO.SUCCESS_N);
//			jRes.setResult("2");
//			// jRes.addAttribute("msg", "exception");
//		}
//		return jRes;
//	}
	


	/**
	 * 현재 스레드 상태 체크하는 메소드
	 *  
	 *  @param ThreadName 스레드 이름
	 *  @return OnOff - 스레드 동작 여부 리턴
	 */
	public boolean ThreadCheck(String ThreadName) {
		
		boolean OnOff = false;
		

		Thread currentThread = Thread.currentThread();
		ThreadGroup threadGroup = currentThread.getThreadGroup();
		while (threadGroup.getParent() != null) {
			threadGroup = threadGroup.getParent();
		}
		int activeCount = threadGroup.activeCount();
		Thread[] activeThreads = new Thread[activeCount + 5];
		int enumeratedCount = threadGroup.enumerate(activeThreads);
		Thread finalizerThread = null;
		for (int i = 0; i < enumeratedCount; i++) {
			if (activeThreads[i].getName().equals(ThreadName)) {
				finalizerThread = activeThreads[i];
				break;
			}
		}

		 if (finalizerThread != null) {
			 OnOff = true;
		 }
		
		
		
		return OnOff;
	}
	
	public String httpConnection(String targetUrl) {
	    URL url = null;
	    HttpURLConnection conn = null;
	    String jsonData = "";
	    BufferedReader br = null;
	    StringBuffer sb = null;
	    String returnText = "";
	 
	    try {
	        url = new URL(targetUrl);
	 
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000);
	        conn.connect();
	 
	        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	 
	        sb = new StringBuffer();
	 
	        while ((jsonData = br.readLine()) != null) {
	            sb.append(jsonData);
	        }
	 
	        returnText = sb.toString();
	 
	    } catch (IOException e) {
	        logger.error("error",e);
	    } finally {
	        try {
	            if (br != null) br.close();
	        } catch (IOException e) {
	            logger.error("error",e);
	        }
	    }
	 
	    return returnText;
	}
	
	
	@RequestMapping(value = "/updateFileRecoverInfo.do")
	public @ResponseBody AJaxResVO updateFileRecoverInfo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

				if(request.getParameter("sysCode") != null) 
					fileRecoverInfo.setSysCode(request.getParameter("sysCode"));

				if(request.getParameter("extNum") != null) 
					fileRecoverInfo.setExtNum(request.getParameter("extNum"));

				if(request.getParameter("extIp") != null) 
					fileRecoverInfo.setExtIp(request.getParameter("extIp"));

				if(request.getParameter("callId") != null) 
					fileRecoverInfo.setCallId(request.getParameter("callId"));

				if(request.getParameter("callTtime") != null) 
					fileRecoverInfo.setCallTtime(request.getParameter("callTtime"));

				if(request.getParameter("custPhone") != null) {
					if (request.getParameter("custPhoneChange") != null && "Y".equals(request.getParameter("custPhoneChange"))) {
						fileRecoverInfo.setCustPhone(request.getParameter("custPhone"));
					}
				}

				if(request.getParameter("callKind") != null) 
					fileRecoverInfo.setCallKind(request.getParameter("callKind"));
				
				if(!StringUtil.isNull(request.getParameter("fileDate"),true)) 
					fileRecoverInfo.setRecDate(request.getParameter("fileDate").replaceAll("-", ""));

				if(!StringUtil.isNull(request.getParameter("fileTime"),true)) 
					fileRecoverInfo.setRecTime(request.getParameter("fileTime").replaceAll(":", ""));

				if(!StringUtil.isNull(request.getParameter("fileName"),true)) 
					fileRecoverInfo.setFileName(request.getParameter("fileName"));

				if(!StringUtil.isNull(request.getParameter("fileState"),true)) 
					fileRecoverInfo.setFileState(request.getParameter("fileState"));
				
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
						fileRecoverInfo.setCustPhone1IsEncrypt("Y");
					}
				}

				String nowListType = "rtp";
				if(!StringUtil.isNull(request.getParameter("nowListType"),true)) 
					nowListType = request.getParameter("nowListType");
				int updateFileRecoverInfoResult = 0;
				if ("genesys".equals(nowListType)) {
					if(!(StringUtil.isNull(request.getParameter("fileDate"),true) && StringUtil.isNull(request.getParameter("fileTime"),true) && StringUtil.isNull(request.getParameter("extNum"),true) && StringUtil.isNull(request.getParameter("callId"),true))) 
						updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(fileRecoverInfo);
				} else {
					// 업데이트 조건이 다 있으면 업데이트
					// 만약 조건 없이 업데이트 되면 안됨
					if(!(StringUtil.isNull(request.getParameter("fileDate"),true) && StringUtil.isNull(request.getParameter("fileTime"),true) && StringUtil.isNull(request.getParameter("fileName"),true))) 
						updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(fileRecoverInfo);
				}
				
				if (updateFileRecoverInfoResult > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "insert fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}

	@RequestMapping(value = "/listenRecoverFile.do")
	public @ResponseBody AJaxResVO listenRecoverFile(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				
				String listType = "rtp";
				
				// listType 은 rtp 또는 genesys, 둘이 보는 테이블, 요청하는 파라미터가 다름
				if(!StringUtil.isNull(request.getParameter("listType"),true)) {
					listType = request.getParameter("listType");
				} 
				
				// rs_etc_config에 파일 복구시 사용할 IP를 등록
				// RTP 리스트에 복구 서버 IP가 들어가 있으나, 나중에 추가된 제네시스 리스트에는 없어서 옵션으로 추가함
				// 복구 요청할때 복구할 콜 sysCode로 등록된 복구서버에 먼저 요청한 후 실패시 다른서버에 요청
				EtcConfigInfo fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_IP_");
				List<EtcConfigInfo> fileRecoverIpList = etcConfigInfoService.selectEtcConfigInfoLike(fileRecoverConfig);
				if (fileRecoverIpList == null || fileRecoverIpList.size() == 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "noConfigServerIp");
				}
				
				// 복구 서버 포트는 공용으로 사용
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_PORT");
				List<EtcConfigInfo> fileRecoverPortArr = etcConfigInfoService.selectEtcConfigInfo(fileRecoverConfig);
				int fileRecoverPort = 28886;
				if (fileRecoverPortArr.size() > 0) {
					fileRecoverPort = Integer.parseInt(fileRecoverPortArr.get(0).getConfigValue());
				}
				
				// 제네시스일때 R2M에 날짜 시간 내선 콜아이디로 복구 요청
				if ("genesys".equals(listType)) {
					String listenUrl =  "";
					FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();
					fileRecoverInfo.setRecDate(request.getParameter("fileDate"));
					fileRecoverInfo.setRecTime(request.getParameter("fileTime"));
					fileRecoverInfo.setExtNum(request.getParameter("extNum"));
					fileRecoverInfo.setCallId(request.getParameter("callId"));
					List<FileRecoverInfo> genesysInfoResult = fileRecoverService.selectGenesysInfo(fileRecoverInfo);
					if (genesysInfoResult.size() > 0 && "Y".equals(genesysInfoResult.get(0).getRecoverYn())) {
						// 복구 완료된 파일일 경우 rs_recfile에서 청취 url 받아옴
						SearchListInfo searchListInfo = new SearchListInfo();
						searchListInfo.setRecDate(request.getParameter("fileDate"));
						searchListInfo.setRecTime(request.getParameter("fileTime"));
						searchListInfo.setvSysCode(request.getParameter("sysCode"));
						searchListInfo.setExtNum(request.getParameter("extNum"));
						searchListInfo.setCallId1(request.getParameter("callId"));
						List<SearchListInfo> searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
						
						if (searchListResult.size() > 0) {
							listenUrl = searchListResult.get(0).getListenUrl();
						}
						if (!"".equals(listenUrl)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.addAttribute("listenUrl", listenUrl);
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
						}
					} else {
						// 복구 필요한 경우
						// 등록된 내선인지 먼저 확인
						String extNum = request.getParameter("extNum");
						String sysCode = request.getParameter("sysCode");
						ChannelInfo isExistExtIp = new ChannelInfo();
						isExistExtIp.setSysCode(sysCode);
						isExistExtIp.setExtNum(extNum);
						int isExistExtIpResult = channelInfoService.checkIpOverlap(isExistExtIp);
						
						if (isExistExtIpResult > 0) {
							// 자기 system code 먼저 확인
							for (int i = 0; i < fileRecoverIpList.size(); i++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
								if (ipConfig.getConfigKey().endsWith(sysCode)) {
									listenUrl =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/listen?listType=" + listType + "&recDate="+request.getParameter("fileDate") + "&recTime="+request.getParameter("fileTime") + "&extNum=" + request.getParameter("extNum") + "&callId=" +  request.getParameter("callId"));
									break;
								}
							}

							if ("98".equals(listenUrl)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", "working");
							} else if (!"".equals(listenUrl)) {
								// listenUrl 값이 있으면 복구 성공한거 listenUrl 리턴
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.addAttribute("listenUrl", listenUrl);
							} else {
								// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
								for (int i = 0; i < fileRecoverIpList.size(); i++) {
									EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
									if (!ipConfig.getConfigKey().endsWith(sysCode)) {
										listenUrl =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/listen?listType=" + listType + "&recDate="+request.getParameter("fileDate") + "&recTime="+request.getParameter("fileTime") + "&extNum=" + request.getParameter("extNum") + "&callId=" +  request.getParameter("callId"));
										if ("98".equals(listenUrl)) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.addAttribute("msg", "working");
										} else if (!"".equals(listenUrl)) {
											jRes.setSuccess(AJaxResVO.SUCCESS_Y);
											jRes.addAttribute("listenUrl", listenUrl);
											break;
										}
									}
								}
								if ("".equals(listenUrl)) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
								}
							}
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "notRegistered");
						}
					}
				} else {
					// RTP 리스트에서 복구 요청한 경우 파일명을 R2M에 보냄
					String fileName = "";
					if(!StringUtil.isNull(request.getParameter("fileName"),true)) {
						fileName = request.getParameter("fileName");
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "nullParam");
						return jRes;
					}
					String listenUrl =  "";
					FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();
					fileRecoverInfo.setFileName(fileName);
					List<FileRecoverInfo> fileRecoverResult = fileRecoverService.selectFileRecoverInfo(fileRecoverInfo);
					if (fileRecoverResult.size() > 0 && "Y".equals(fileRecoverResult.get(0).getRecoverYn())) {
						// 복구 완료된 파일일 경우 rs_recfile에서 청취 url 받아옴
						SearchListInfo searchListInfo = new SearchListInfo();
						searchListInfo.setRecDate(fileRecoverResult.get(0).getRecDate());
						searchListInfo.setRecTime(fileRecoverResult.get(0).getRecTime());
						searchListInfo.setvSysCode(fileRecoverResult.get(0).getSysCode());
						searchListInfo.setExtNum(fileRecoverResult.get(0).getExtNum());
						searchListInfo.setCallId1(fileRecoverResult.get(0).getFileName().replace(".mp3", ""));
						List<SearchListInfo> searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
						
						if (searchListResult.size() > 0) {
							listenUrl = searchListResult.get(0).getListenUrl();
						}
						
						if ("98".equals(listenUrl)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "working");
						} else if (!"".equals(listenUrl)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.addAttribute("listenUrl", listenUrl);
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
						}
					} else {
						// 복구 안된 경우 복구 요청
						// 등록된 내선인지 먼저 확인
						String extIp = request.getParameter("extIp");
						String sysCode = request.getParameter("sysCode");
						ChannelInfo isExistExtIp = new ChannelInfo();
						isExistExtIp.setSysCode(sysCode);
						isExistExtIp.setExtIp(extIp);
						int isExistExtIpResult = channelInfoService.checkIpOverlap(isExistExtIp);
						
						if (isExistExtIpResult > 0) {
							for (int i = 0; i < fileRecoverIpList.size(); i++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
								if (ipConfig.getConfigKey().endsWith(sysCode)) {
									listenUrl =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/listen?listType=" + listType + "&filename=" + fileName);
									break;
								}
							}
							
							if ("98".equals(listenUrl)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", "working");
							} else if (!"".equals(listenUrl)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_Y);
								jRes.addAttribute("listenUrl", listenUrl);
							}  else {
								// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
								for (int i = 0; i < fileRecoverIpList.size(); i++) {
									EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
									if (!ipConfig.getConfigKey().endsWith(sysCode)) {
										listenUrl =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/listen?listType=" + listType + "&filename=" + fileName);
										if (!"".equals(listenUrl)) {
											jRes.setSuccess(AJaxResVO.SUCCESS_Y);
											jRes.addAttribute("listenUrl", listenUrl);
											break;
										}
									}
								}
								if ("".equals(listenUrl)) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
								}
							}
						} else {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "notRegistered");
						}
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}
	
	@RequestMapping(value = "/requestFileRecover.do")
	public @ResponseBody AJaxResVO requestFileRecover(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				// 리스트 타입은 rtp 또는 genesys
				String listType = "rtp";
				if(!StringUtil.isNull(request.getParameter("listType"),true)) 
					listType = request.getParameter("listType");
				
				EtcConfigInfo fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_PORT");
				List<EtcConfigInfo> fileRecoverPortArr = etcConfigInfoService.selectEtcConfigInfo(fileRecoverConfig);
				int fileRecoverPort = 28886;
				if (fileRecoverPortArr.size() > 0) {
					fileRecoverPort = Integer.parseInt(fileRecoverPortArr.get(0).getConfigValue());
				}
				
				fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_IP_");
				List<EtcConfigInfo> fileRecoverIpList = etcConfigInfoService.selectEtcConfigInfoLike(fileRecoverConfig);
				if (fileRecoverIpList == null || fileRecoverIpList.size() == 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "noConfigServerIp");
				}
				
				// 복구시 등록된 내선인지 먼저 확인
				boolean existsExtIp = false;
				if ("rtp".equals(listType)) {
					String extIp = request.getParameter("extIp");
					String sysCode = request.getParameter("sysCode");
					ChannelInfo isExistExtIp = new ChannelInfo();
					isExistExtIp.setSysCode(sysCode);
					isExistExtIp.setExtIp(extIp);
					int isExistExtIpResult = channelInfoService.checkIpOverlap(isExistExtIp);

					if (isExistExtIpResult > 0) {
						existsExtIp = true;
					}
				} else {
					existsExtIp = true;
				}
				
				if (existsExtIp) {
					if("genesys".equals(listType)) {
						String result = "";
						
						// 자기 system code 먼저 확인
						for (int i = 0; i < fileRecoverIpList.size(); i++) {
							EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
							if (ipConfig.getConfigKey().endsWith(request.getParameter("sysCode"))) {
								result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+request.getParameter("fileDate") + "&recTime="+request.getParameter("fileTime") + "&extNum=" + request.getParameter("extNum") + "&callId=" +  request.getParameter("callId"));
								break;
							}
						}
						
						if ("98".equals(result)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "working");
						} else if ("1".equals(result)) {
							FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

							if(!StringUtil.isNull(request.getParameter("fileDate"),true)) 
								fileRecoverInfo.setRecDate(request.getParameter("fileDate").replaceAll("-", ""));

							if(!StringUtil.isNull(request.getParameter("fileTime"),true)) 
								fileRecoverInfo.setRecTime(request.getParameter("fileTime").replaceAll(":", ""));

							if(!StringUtil.isNull(request.getParameter("extNum"),true)) 
								fileRecoverInfo.setExtNum(request.getParameter("extNum"));

							if(!StringUtil.isNull(request.getParameter("callId"),true)) 
								fileRecoverInfo.setCallId(request.getParameter("callId"));

							fileRecoverInfo.setFileState("MW");

							int updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(fileRecoverInfo);
							
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.addAttribute("msg", result);
						} else {
							// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
							for (int i = 0; i < fileRecoverIpList.size(); i++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
								if (!ipConfig.getConfigKey().endsWith(request.getParameter("sysCode"))) {
									result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+request.getParameter("fileDate") + "&recTime="+request.getParameter("fileTime") + "&extNum=" + request.getParameter("extNum") + "&callId=" +  request.getParameter("callId"));
									if ("1".equals(result)) {
										FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

										if(!StringUtil.isNull(request.getParameter("fileDate"),true)) 
											fileRecoverInfo.setRecDate(request.getParameter("fileDate").replaceAll("-", ""));

										if(!StringUtil.isNull(request.getParameter("fileTime"),true)) 
											fileRecoverInfo.setRecTime(request.getParameter("fileTime").replaceAll(":", ""));

										if(!StringUtil.isNull(request.getParameter("extNum"),true)) 
											fileRecoverInfo.setExtNum(request.getParameter("extNum"));

										if(!StringUtil.isNull(request.getParameter("callId"),true)) 
											fileRecoverInfo.setCallId(request.getParameter("callId"));

										fileRecoverInfo.setFileState("MW");

										int updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(fileRecoverInfo);
										
										jRes.setSuccess(AJaxResVO.SUCCESS_Y);
										jRes.addAttribute("msg", result);
										break;
									}
								}
							}
							
							if ("98".equals(result)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", "working");
							} else if (!"1".equals(result)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", result);
							}
						}
					} else {
						String result = "";
						
						// 자기 system code 먼저 확인
						for (int i = 0; i < fileRecoverIpList.size(); i++) {
							EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
							if (ipConfig.getConfigKey().endsWith(request.getParameter("sysCode"))) {
								result = httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?filename=" + request.getParameter("fileName").trim());
								break;
							}
						}
						
						if ("98".equals(result)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "working");
						} else if ("1".equals(result)) {
							FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

							if(!StringUtil.isNull(request.getParameter("fileDate"),true)) 
								fileRecoverInfo.setRecDate(request.getParameter("fileDate").replaceAll("-", ""));

							if(!StringUtil.isNull(request.getParameter("fileTime"),true)) 
								fileRecoverInfo.setRecTime(request.getParameter("fileTime").replaceAll(":", ""));

							if(!StringUtil.isNull(request.getParameter("fileName"),true)) 
								fileRecoverInfo.setFileName(request.getParameter("fileName"));

							fileRecoverInfo.setFileState("MW");

							int updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(fileRecoverInfo);
							
							jRes.setSuccess(AJaxResVO.SUCCESS_Y);
							jRes.addAttribute("msg", result);
						} else {
							// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
							for (int i = 0; i < fileRecoverIpList.size(); i++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
								if (!ipConfig.getConfigKey().endsWith(request.getParameter("sysCode"))) {
									result = httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?filename=" + request.getParameter("fileName").trim());
									
									if ("98".equals(result)) {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
										jRes.addAttribute("msg", "working");
									} else if ("1".equals(result)) {
										FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

										if(!StringUtil.isNull(request.getParameter("fileDate"),true)) 
											fileRecoverInfo.setRecDate(request.getParameter("fileDate").replaceAll("-", ""));

										if(!StringUtil.isNull(request.getParameter("fileTime"),true)) 
											fileRecoverInfo.setRecTime(request.getParameter("fileTime").replaceAll(":", ""));

										if(!StringUtil.isNull(request.getParameter("fileName"),true)) 
											fileRecoverInfo.setFileName(request.getParameter("fileName"));

										fileRecoverInfo.setFileState("MW");

										int updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(fileRecoverInfo);
										
										jRes.setSuccess(AJaxResVO.SUCCESS_Y);
										jRes.addAttribute("msg", result);
										break;
									}
								}
							}
							if (!"1".equals(result)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", result);
							}
						}
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "notRegistered");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}
	
	@RequestMapping(value = "/requestMultiFileRecover.do")
	public @ResponseBody AJaxResVO requestMultiFileRecover(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				int succCnt = 0;
				int failCnt = 0;

				String listType = "rtp";
				if(!StringUtil.isNull(request.getParameter("listType"),true)) {
					listType = request.getParameter("listType");
				}
				List<String> requestFileDateArr = new ArrayList<String>();
				List<String> requestFileTimeArr = new ArrayList<String>();
				List<String> requestFileNameArr = new ArrayList<String>();
				List<String> requestFileRecoverIpArr = new ArrayList<String>();
				List<String> requestSysCodeArrArr = new ArrayList<String>();
				List<String> requestExtIpArrArr = new ArrayList<String>();
				List<String> requestCallIdArr = new ArrayList<String>();
				List<String> requestExtNumArr = new ArrayList<String>();

				for(String s : params.get("fileDateArr").split(",")) { requestFileDateArr.add(s); }
				for(String s : params.get("fileTimeArr").split(",")) { requestFileTimeArr.add(s); }
				for(String s : params.get("sysCodeArr").split(",")) { requestSysCodeArrArr.add(s); }
				if ("genesys".equals(listType)) {
					for(String s : params.get("callIdArr").split(",")) { requestCallIdArr.add(s); }
					for(String s : params.get("extNumArr").split(",")) { requestExtNumArr.add(s); }
				} else {
					for(String s : params.get("fileNameArr").split(",")) { requestFileNameArr.add(s); }
					for(String s : params.get("fileRecoverIpArr").split(",")) { requestFileRecoverIpArr.add(s); }
					for(String s : params.get("extIpArr").split(",")) { requestExtIpArrArr.add(s); }
				}
				
				
				// 복구 요청할 IP, PORT 조회
				EtcConfigInfo fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_PORT");
				List<EtcConfigInfo> fileRecoverPortArr = etcConfigInfoService.selectEtcConfigInfo(fileRecoverConfig);
				int fileRecoverPort = 28886;
				if (fileRecoverPortArr.size() > 0) {
					fileRecoverPort = Integer.parseInt(fileRecoverPortArr.get(0).getConfigValue());
				}
				
				fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_IP_");
				List<EtcConfigInfo> fileRecoverIpList = etcConfigInfoService.selectEtcConfigInfoLike(fileRecoverConfig);
				if (fileRecoverIpList == null || fileRecoverIpList.size() == 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "noConfigServerIp");
				}
				
				String result = "";
				for (int j = 0; j < requestFileDateArr.size(); j++) {
					if ("rtp".equals(listType)) {
						// 등록된 ip인지 먼저 확인
						String sysCode = requestSysCodeArrArr.get(j);
						String extIp = requestExtIpArrArr.get(j);
						ChannelInfo isExistExtIp = new ChannelInfo();
						isExistExtIp.setSysCode(sysCode);
						isExistExtIp.setExtIp(extIp);
						int isExistExtIpResult = channelInfoService.checkIpOverlap(isExistExtIp);
						
						if (isExistExtIpResult == 0) {
							failCnt++;
							continue;
						}
					}
					
					if ("genesys".equals(listType)) {
						// 자기 system code 먼저 확인
						for (int i = 0; i < fileRecoverIpList.size(); i++) {
							EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
							if (ipConfig.getConfigKey().endsWith(requestSysCodeArrArr.get(j).trim())) {
								result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+requestFileDateArr.get(j).replaceAll("-", "").trim() + "&recTime="+requestFileTimeArr.get(j).replaceAll(":", "").trim() + "&extNum=" + requestExtNumArr.get(j).trim() + "&callId=" +  requestCallIdArr.get(j).trim());
								break;
							}
						}
						
						if ("98".equals(result)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "working");
						} else if ("1".equals(result)) {
							FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

							if(!StringUtil.isNull(requestFileDateArr.get(j),true)) 
								fileRecoverInfo.setRecDate(requestFileDateArr.get(j).replaceAll("-", "").trim());

							if(!StringUtil.isNull(requestFileTimeArr.get(j),true)) 
								fileRecoverInfo.setRecTime(requestFileTimeArr.get(j).replaceAll(":", "").trim());
							
							if(!StringUtil.isNull(requestExtNumArr.get(j),true)) 
								fileRecoverInfo.setExtNum(requestExtNumArr.get(j).trim());

							if(!StringUtil.isNull(requestCallIdArr.get(j),true)) 
								fileRecoverInfo.setCallId(requestCallIdArr.get(j).trim());
							
							fileRecoverInfo.setFileState("MW");

							int updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(fileRecoverInfo);

							succCnt++;
						} else {
							// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
							for (int i = 0; i < fileRecoverIpList.size(); i++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
								if (!ipConfig.getConfigKey().endsWith(requestSysCodeArrArr.get(j).trim())) {
									result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+requestFileDateArr.get(j).replaceAll("-", "").trim() + "&recTime="+requestFileTimeArr.get(j).replaceAll(":", "").trim() + "&extNum=" + requestExtNumArr.get(j).trim() + "&callId=" +  requestCallIdArr.get(j).trim());
									
									if ("98".equals(result)) {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
										jRes.addAttribute("msg", "working");
									} else if ("1".equals(result)) {
										FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

										if(!StringUtil.isNull(requestFileDateArr.get(j),true)) 
											fileRecoverInfo.setRecDate(requestFileDateArr.get(j).replaceAll("-", "").trim());

										if(!StringUtil.isNull(requestFileTimeArr.get(j),true)) 
											fileRecoverInfo.setRecTime(requestFileTimeArr.get(j).replaceAll(":", "").trim());
										
										if(!StringUtil.isNull(requestExtNumArr.get(j),true)) 
											fileRecoverInfo.setExtNum(requestExtNumArr.get(j).trim());

										if(!StringUtil.isNull(requestCallIdArr.get(j),true)) 
											fileRecoverInfo.setCallId(requestCallIdArr.get(j).trim());
										
										fileRecoverInfo.setFileState("MW");

										int updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(fileRecoverInfo);

										succCnt++;
										break;
									}
								}
							}
							if (!"1".equals(result)) {
								failCnt++;
							}
						}
					} else {
						// 자기 system code 먼저 확인
						for (int i = 0; i < fileRecoverIpList.size(); i++) {
							EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
							if (ipConfig.getConfigKey().endsWith(requestSysCodeArrArr.get(j).trim())) {
								result = httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?filename=" + requestFileNameArr.get(j).trim());
								break;
							}
						}
						
						if ("98".equals(result)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "working");
						} else if ("1".equals(result)) {
							FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

							if(!StringUtil.isNull(requestFileDateArr.get(j),true)) 
								fileRecoverInfo.setRecDate(requestFileDateArr.get(j).replaceAll("-", "").trim());

							if(!StringUtil.isNull(requestFileTimeArr.get(j),true)) 
								fileRecoverInfo.setRecTime(requestFileTimeArr.get(j).replaceAll(":", "").trim());

							if(!StringUtil.isNull(requestFileNameArr.get(j),true)) 
								fileRecoverInfo.setFileName(requestFileNameArr.get(j).trim());

							fileRecoverInfo.setFileState("MW");

							int updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(fileRecoverInfo);

							succCnt++;
						} else {
							// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
							for (int i = 0; i < fileRecoverIpList.size(); i++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(i);
								if (!ipConfig.getConfigKey().endsWith(requestSysCodeArrArr.get(j).trim())) {
									result = httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?filename=" + requestFileNameArr.get(j).trim());
									
									if ("98".equals(result)) {
										jRes.setSuccess(AJaxResVO.SUCCESS_N);
										jRes.addAttribute("msg", "working");
									} else if ("1".equals(result)) {
										FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();

										if(!StringUtil.isNull(requestFileDateArr.get(j),true)) 
											fileRecoverInfo.setRecDate(requestFileDateArr.get(j).replaceAll("-", "").trim());

										if(!StringUtil.isNull(requestFileTimeArr.get(j),true)) 
											fileRecoverInfo.setRecTime(requestFileTimeArr.get(j).replaceAll(":", "").trim());

										if(!StringUtil.isNull(requestFileNameArr.get(j),true)) 
											fileRecoverInfo.setFileName(requestFileNameArr.get(j).trim());

										fileRecoverInfo.setFileState("MW");

										int updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(fileRecoverInfo);

										succCnt++;
										break;
									}
								}
							}
							if (!"1".equals(result)) {
								failCnt++;
							}
						}
					}
				}
				if (requestFileDateArr.size() == succCnt && failCnt == 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if (succCnt > 0 && failCnt > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "requestRecoverFail");
					jRes.addAttribute("succCnt", succCnt);
					jRes.addAttribute("failCnt", failCnt);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("succCnt", succCnt);
					jRes.addAttribute("failCnt", failCnt);
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}
	
	@RequestMapping(value = "/requestAllFileRecover.do")
	public @ResponseBody AJaxResVO requestAllFileRecover(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				String listType = "rtp";
				if(!StringUtil.isNull(request.getParameter("listType"),true)) {
					listType = request.getParameter("listType");
				}
				int succCnt = 0;
				int failCnt = 0;
				
				FileRecoverInfo fileRecoverInfo = new FileRecoverInfo();
				fileRecoverInfo.setAllRecover("Y");
				
				if(!StringUtil.isNull(request.getParameter("sDate"),true) && !StringUtil.isNull(request.getParameter("eDate"),true)) {
					fileRecoverInfo.setsDate(sqlFilterUtil.sqlFilter(request.getParameter("sDate").replace("-", "").toString()));
					fileRecoverInfo.seteDate(sqlFilterUtil.sqlFilter(request.getParameter("eDate").replace("-", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("sTime"),true) && !StringUtil.isNull(request.getParameter("eTime"),true)) {
					fileRecoverInfo.setsTime(sqlFilterUtil.sqlFilter(request.getParameter("sTime").replace(":", "").toString()));
					fileRecoverInfo.seteTime(sqlFilterUtil.sqlFilter(request.getParameter("eTime").replace(":", "").toString()));
				}
				if(!StringUtil.isNull(request.getParameter("sTtime"),true)) {
					if (request.getParameter("sTtime").contains(":")) {
						fileRecoverInfo.setsTtime(sqlFilterUtil.sqlFilter(getTimetoLong(request.getParameter("sTtime"))));
					} else {
						fileRecoverInfo.setsTtime(sqlFilterUtil.sqlFilter(request.getParameter("sTtime")));
					}
				}
				if(!StringUtil.isNull(request.getParameter("eTtime"),true)) {
					if (request.getParameter("eTtime").contains(":")) {
						fileRecoverInfo.seteTtime(sqlFilterUtil.sqlFilter(getTimetoLong(request.getParameter("eTtime"))));
					} else {
						fileRecoverInfo.seteTtime(sqlFilterUtil.sqlFilter(request.getParameter("eTtime")));
					}
				}
				
				if(!StringUtil.isNull(request.getParameter("sysCode"),true)) {
					fileRecoverInfo.setSysCode(request.getParameter("sysCode"));
				}
				if(!StringUtil.isNull(request.getParameter("extNum"),true)) {
					fileRecoverInfo.setExtNum(request.getParameter("extNum"));
				}
				if(!StringUtil.isNull(request.getParameter("extIp"),true)) {
					fileRecoverInfo.setExtIp(request.getParameter("extIp"));
				}
				if(!StringUtil.isNull(request.getParameter("callId"),true)) {
					fileRecoverInfo.setCallId(request.getParameter("callId"));
				}
				if(!StringUtil.isNull(request.getParameter("callTtime"),true)) {
					fileRecoverInfo.setCallTtime(request.getParameter("callTtime"));
				}
				if(!StringUtil.isNull(request.getParameter("custPhone"),true)) {
					fileRecoverInfo.setCustPhone(request.getParameter("custPhone"));
				}
				if(!StringUtil.isNull(request.getParameter("callKind"),true)) {
					fileRecoverInfo.setCallKind(request.getParameter("callKind"));
				}
				if(!StringUtil.isNull(request.getParameter("fileName"),true)) {
					fileRecoverInfo.setFileName(request.getParameter("fileName"));
				}
				if(!StringUtil.isNull(request.getParameter("fileState"),true)) {
					fileRecoverInfo.setFileState(request.getParameter("fileState"));
				}
				if(!StringUtil.isNull(request.getParameter("recfileExists"),true)) {
					fileRecoverInfo.setRecfileExists(request.getParameter("recfileExists"));
				}
				
				List<FileRecoverInfo> fileRecoverInfoResult = new ArrayList<>();
				if ("genesys".equals(listType)) {
					fileRecoverInfoResult = fileRecoverService.selectGenesysInfo(fileRecoverInfo);
				} else {
					fileRecoverInfoResult = fileRecoverService.selectFileRecoverInfo(fileRecoverInfo);
				}
				
				int total = fileRecoverInfoResult.size();
				if (total > 0) {
					// 복구 요청할 IP, PORT 조회
					EtcConfigInfo fileRecoverConfig= new EtcConfigInfo();
					fileRecoverConfig.setGroupKey("SYSTEM");
					fileRecoverConfig.setConfigKey("FILE_RECOVER_PORT");
					List<EtcConfigInfo> fileRecoverPortArr = etcConfigInfoService.selectEtcConfigInfo(fileRecoverConfig);
					int fileRecoverPort = 28886;
					if (fileRecoverPortArr.size() > 0) {
						fileRecoverPort = Integer.parseInt(fileRecoverPortArr.get(0).getConfigValue());
					}
					
					fileRecoverConfig= new EtcConfigInfo();
					fileRecoverConfig.setGroupKey("SYSTEM");
					fileRecoverConfig.setConfigKey("FILE_RECOVER_IP_");
					List<EtcConfigInfo> fileRecoverIpList = etcConfigInfoService.selectEtcConfigInfoLike(fileRecoverConfig);
					if (fileRecoverIpList == null || fileRecoverIpList.size() == 0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "noConfigServerIp");
					}

					String result = "";
					for(int i = 0; i < fileRecoverInfoResult.size(); i++) {
						FileRecoverInfo updateFileRecoverInfo = new FileRecoverInfo();
						
						if ("rtp".equals(listType)) {
							// 등록된 ip인지 먼저 확인
							String sysCode = fileRecoverInfoResult.get(i).getSysCode();
							String extIp = fileRecoverInfoResult.get(i).getExtIp();
							ChannelInfo isExistExtIp = new ChannelInfo();
							isExistExtIp.setSysCode(sysCode);
							isExistExtIp.setExtIp(extIp);
							int isExistExtIpResult = channelInfoService.checkIpOverlap(isExistExtIp);
							
							if (isExistExtIpResult == 0) {
								failCnt++;
								continue;
							}
						}
						
						if ("genesys".equals(listType)) {
							// 자기 system code 먼저 확인
							for (int j = 0; j < fileRecoverIpList.size(); j++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(j);
								if (ipConfig.getConfigKey().endsWith(fileRecoverInfoResult.get(i).getSysCode())) {
									result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "") + "&recTime="+fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "") + "&extNum=" + fileRecoverInfoResult.get(i).getExtNum() + "&callId=" +  fileRecoverInfoResult.get(i).getCallId());
									break;
								}
							}
							
							if ("98".equals(result)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", "working");
								break;
							} else if ("1".equals(result)) {
								updateFileRecoverInfo.setRecDate(fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "").trim());
								updateFileRecoverInfo.setRecTime(fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "").trim());
								updateFileRecoverInfo.setExtNum(fileRecoverInfoResult.get(i).getExtNum());
								updateFileRecoverInfo.setCallId(fileRecoverInfoResult.get(i).getCallId());
								
								updateFileRecoverInfo.setFileState("MW");
	
								int updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(updateFileRecoverInfo);
	
								succCnt++;
							} else {
								// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
								for (int j = 0; j < fileRecoverIpList.size(); j++) {
									EtcConfigInfo ipConfig = fileRecoverIpList.get(j);
									if (!ipConfig.getConfigKey().endsWith(fileRecoverInfoResult.get(i).getSysCode())) {
										result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "") + "&recTime="+fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "") + "&extNum=" + fileRecoverInfoResult.get(i).getExtNum() + "&callId=" +  fileRecoverInfoResult.get(i).getCallId());
										
										if ("98".equals(result)) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.addAttribute("msg", "working");
											break;
										} else if ("1".equals(result)) {
											updateFileRecoverInfo.setRecDate(fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "").trim());
											updateFileRecoverInfo.setRecTime(fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "").trim());
											updateFileRecoverInfo.setExtNum(fileRecoverInfoResult.get(i).getExtNum());
											updateFileRecoverInfo.setCallId(fileRecoverInfoResult.get(i).getCallId());
											
											updateFileRecoverInfo.setFileState("MW");
				
											int updateFileRecoverInfoResult = fileRecoverService.updateGenesysInfo(updateFileRecoverInfo);
				
											succCnt++;
											break;
										}
									}
								}
								if (!"1".equals(result)) {
									failCnt++;
								}
							}
						} else {
							for (int j = 0; j < fileRecoverIpList.size(); j++) {
								EtcConfigInfo ipConfig = fileRecoverIpList.get(j);
								if (ipConfig.getConfigKey().endsWith(fileRecoverInfoResult.get(i).getSysCode())) {
									result = httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?filename=" + fileRecoverInfoResult.get(i).getFileName().trim());
								}
							}
							
							if ("98".equals(result)) {
								jRes.setSuccess(AJaxResVO.SUCCESS_N);
								jRes.addAttribute("msg", "working");
								break;
							} else if ("1".equals(result)) {
								updateFileRecoverInfo.setRecDate(fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "").trim());
								updateFileRecoverInfo.setRecTime(fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "").trim());
								updateFileRecoverInfo.setFileName(fileRecoverInfoResult.get(i).getFileName().trim());
	
								updateFileRecoverInfo.setFileState("MW");
	
								int updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(updateFileRecoverInfo);
	
								succCnt++;
							} else {
								// 자기 서버랑 연결 안되면 or 결과가 없으면 다른곳 확인
								for (int j = 0; j < fileRecoverIpList.size(); j++) {
									EtcConfigInfo ipConfig = fileRecoverIpList.get(j);
									if (!ipConfig.getConfigKey().endsWith(fileRecoverInfoResult.get(i).getSysCode())) {
										result =  httpConnection("HTTP://"+ipConfig.getConfigValue()+":"+fileRecoverPort+"/Recover?listType=" + listType + "&recDate="+fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "") + "&recTime="+fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "") + "&extNum=" + fileRecoverInfoResult.get(i).getExtNum() + "&callId=" +  fileRecoverInfoResult.get(i).getCallId());
										
										if ("98".equals(result)) {
											jRes.setSuccess(AJaxResVO.SUCCESS_N);
											jRes.addAttribute("msg", "working");
											break;
										} else if ("1".equals(result)) {
											updateFileRecoverInfo.setRecDate(fileRecoverInfoResult.get(i).getRecDate().replaceAll("-", "").trim());
											updateFileRecoverInfo.setRecTime(fileRecoverInfoResult.get(i).getRecTime().replaceAll(":", "").trim());
											updateFileRecoverInfo.setFileName(fileRecoverInfoResult.get(i).getFileName().trim());
				
											updateFileRecoverInfo.setFileState("MW");
				
											int updateFileRecoverInfoResult = fileRecoverService.updateFileRecoverInfo(updateFileRecoverInfo);
				
											succCnt++;
											break;
										}
									}
								}
								if (!"1".equals(result)) {
									failCnt++;
								}
							}
						}
					}
					
					if (total == succCnt && failCnt == 0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					} else if (succCnt > 0 && failCnt > 0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "requestRecoverFail");
						
						jRes.addAttribute("total", total);
						jRes.addAttribute("succCnt", succCnt);
						jRes.addAttribute("failCnt", failCnt);
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("total", total);
						jRes.addAttribute("succCnt", succCnt);
						jRes.addAttribute("failCnt", failCnt);
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}
	
	@RequestMapping(value = "/requestListReload.do")
	public @ResponseBody AJaxResVO requestListReload(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if (userInfo != null) {
				String sDate = "";
				String eDate = "";
				String sTime = "";
				String eTime = "";
				String listType = "rtp";
				
				if(!StringUtil.isNull(request.getParameter("sDate"),true)) 
					sDate = request.getParameter("sDate").replaceAll("-", "");
				if(!StringUtil.isNull(request.getParameter("eDate"),true)) 
					eDate = request.getParameter("eDate").replaceAll("-", "");
				if(!StringUtil.isNull(request.getParameter("sTime"),true)) 
					sTime = request.getParameter("sTime").replaceAll(":", "");
				if(!StringUtil.isNull(request.getParameter("eTime"),true)) 
					eTime = request.getParameter("eTime").replaceAll(":", "");

				if(!StringUtil.isNull(request.getParameter("listType"),true)) 
					listType = request.getParameter("listType");
				
				Date sD = new SimpleDateFormat("yyyyMMddHHmmss").parse(sDate+sTime);	
				Date eD = new SimpleDateFormat("yyyyMMddHHmmss").parse(eDate+eTime);	
				
				String startTime = new SimpleDateFormat("yyyyMMddHHmmss").format(sD);
				String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(eD);
				
				EtcConfigInfo fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_IP");
				List<EtcConfigInfo> fileRecoverIpArr = etcConfigInfoService.selectEtcConfigInfo(fileRecoverConfig);
				String fileRecoverIp = "";
				if (fileRecoverIpArr.size() > 0) {
					fileRecoverIp = fileRecoverIpArr.get(0).getConfigValue();
				}
				
				fileRecoverConfig= new EtcConfigInfo();
				fileRecoverConfig.setGroupKey("SYSTEM");
				fileRecoverConfig.setConfigKey("FILE_RECOVER_PORT");
				List<EtcConfigInfo> fileRecoverPortArr = etcConfigInfoService.selectEtcConfigInfo(fileRecoverConfig);
				int fileRecoverPort = 28886;
				if (fileRecoverPortArr.size() > 0) {
					fileRecoverPort = Integer.parseInt(fileRecoverPortArr.get(0).getConfigValue());
				}
				
				
				String[] ipArr = fileRecoverIp.split(",");
				String param = "?startTime="+startTime+"&endTime="+endTime+"&listType="+listType;
				int result = 0;
				for (int i = 0; i < ipArr.length; i++) {
					try {
						String resultStr = httpConnection("HTTP://"+ipArr[i]+":"+fileRecoverPort+"/listReload"+param);
						
						if ("98".equals(resultStr)) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.addAttribute("msg", "working");
							break;
						} else if (!"".equals(resultStr)) {
							result++;
						}
					} catch(Exception e) {
						
					}
				}
				if (result == ipArr.length) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "user fail");
			}
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "exception");
		}
		return jRes;
	}
	
	public String getTimetoLong(String t) {
		if(t.indexOf(":")<0) {
			return String.valueOf(t);
		}

		String[] k = t.split(":");
		int df = Integer.parseInt(k[0]) * 3600 + Integer.parseInt(k[1]) * 60 + Integer.parseInt(k[2]);

		return String.valueOf(df);
	}
	
	public static void safeCloseinput(FileInputStream fis) {
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}
	public static void safeCloseOutput(ByteArrayOutputStream fos) {
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}
}
