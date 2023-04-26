package com.furence.recsee.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hyperic.sigar.SigarException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.db.DBInfoList;
import com.db.Generator;
import com.furence.recsee.admin.model.DBManage;
import com.furence.recsee.admin.model.ExecuteManage;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.admin.model.UserDBInterface;
import com.furence.recsee.admin.service.DBManageService;
import com.furence.recsee.admin.service.DelRecfileInfoService;
import com.furence.recsee.admin.service.ExecuteManageService;
import com.furence.recsee.admin.service.JobManageService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.admin.service.SQLManageService;
import com.furence.recsee.admin.service.UserDBInterfaceService;
import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CustConfigInfo;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.MeritUser;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.ChannInfoService;
import com.furence.recsee.common.service.CustConfigInfoService;
import com.furence.recsee.common.service.CustomerInfoService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.OraGetDbService;
import com.furence.recsee.common.service.RecStatusInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;
import com.furence.recsee.monitoring.model.DashboardInfo;
import com.furence.recsee.monitoring.model.MonitoringInfo;
import com.furence.recsee.monitoring.model.SystemMonitoringProc;
import com.furence.recsee.monitoring.service.MonitoringInfoService;

@Component
public class ScheduledTaskLauncher {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskLauncher.class);
	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private MonitoringInfoService monitoringInfoService;

	@Autowired
	private OraGetDbService oraGetDbService;

	@Autowired
	private ChannInfoService channelInfoService;

	@Autowired
	private CustomerInfoService customerInfoService;

	@Autowired
	private SearchListInfoService searchListInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private RecStatusInfoService recStatusInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ExecuteManageService executeManageService;

	@Autowired
	private JobManageService jobManageService;

	@Autowired
	private DBManageService dbManageService;

	@Autowired
	private SQLManageService sqlManageService;

	@Autowired
	private UserDBInterfaceService userDBInterfaceService;

	@Autowired
	private LogService logService;

	@Autowired
	private CustConfigInfoService custConfigInfoService;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private DelRecfileInfoService delRecfileInfoService;
	
	
	public void checkSystemMonitoring() throws SigarException {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("CRONTAB");
		etcConfigInfo.setConfigKey("SYSTEM_MONITORING");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if (etcConfigResult.size() > 0 && etcConfigResult.get(0).getConfigValue().equals("Y")) {

			// System.out.println("System Monitoring Processing!");

			RecSeeUtil recseeUtil = new RecSeeUtil();

			String nowServerIp = recseeUtil.getLocalServerIp();
			// System.out.println("Now server ip : " + nowServerIp );

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			Date currentDateTime = new Date();
			String dateTime = formatter.format(currentDateTime);
			String currentDate = dateTime.substring(0, 8);
			String currentTime = dateTime.substring(8, 14);

			SystemInfo systemInfo = new SystemInfo();

			systemInfo.setSysId(null);
			systemInfo.setSysIp(nowServerIp);

			List<SystemInfo> systemInfoList = systemInfoService.selectSystemInfo(systemInfo);

			int systemInfoTotal = systemInfoList.size();

			if (systemInfoTotal > 0) {
				SystemInfo systemInfoItem = systemInfoList.get(0);

				String[] hddPath = systemInfoItem.getStoragePath().split(",");

				SystemMonitoringProc systemMonitoringProc = new SystemMonitoringProc();

				String realTimeResult = systemMonitoringProc.systemMonitoringProc(systemInfoList).toString();

				// System.out.println(realTimeResult);

				MonitoringInfo monitoringInfo = new MonitoringInfo();

				JSONParser jsonParser = new JSONParser();
				try {
					JSONObject realTimeJson = (JSONObject) jsonParser.parse(realTimeResult);

					if (realTimeJson.get("cpu") != null) {
						monitoringInfo.setSysDate(currentDate);
						monitoringInfo.setSysTime(currentTime);
						monitoringInfo.setSysId(systemInfoItem.getSysId());
						monitoringInfo.setSysName(systemInfoItem.getSysName());
						monitoringInfo.setMonitoringType("CPU");
						monitoringInfo.setTarget("CPU");
						monitoringInfo.setTotal("100");
						monitoringInfo.setUse(realTimeJson.get("cpu").toString());
						monitoringInfo.setUsePercent(realTimeJson.get("cpu").toString());

						monitoringInfoService.insertMonitoringInfo(monitoringInfo);
					}

					JSONObject memory = (JSONObject) realTimeJson.get("memory");

					if (memory != null) {
						monitoringInfo.setSysDate(currentDate);
						monitoringInfo.setSysTime(currentTime);
						monitoringInfo.setSysId(systemInfoItem.getSysId());
						monitoringInfo.setSysName(systemInfoItem.getSysName());
						monitoringInfo.setMonitoringType("Memory");
						monitoringInfo.setTarget("Memory");
						monitoringInfo.setTotal(memory.get("total").toString());
						monitoringInfo.setUse(memory.get("use").toString());
						monitoringInfo.setUsePercent(memory.get("use_percent").toString());

						monitoringInfoService.insertMonitoringInfo(monitoringInfo);
					}

					JSONArray hdd = (JSONArray) realTimeJson.get("hdd");
					if (hdd.size() > 0) {
						@SuppressWarnings("rawtypes")
						Iterator hddItem = hdd.iterator();

						while (hddItem.hasNext()) {
							JSONObject hddObj = (JSONObject) hddItem.next();

							monitoringInfo.setSysDate(currentDate);
							monitoringInfo.setSysTime(currentTime);
							monitoringInfo.setSysId(systemInfoItem.getSysId());
							monitoringInfo.setSysName(systemInfoItem.getSysName());
							monitoringInfo.setMonitoringType("HDD");
							String targetNm = "None";
							for (int i = 0; i < hddPath.length; i++) {
								hddPath[i] = hddPath[i].replace("\\", "");

								if (hddPath[i].toString().toUpperCase().equals(hddObj.get("path"))) {
									targetNm = hddPath[i];
								}
							}
							monitoringInfo.setTarget(targetNm);
							monitoringInfo.setTotal(hddObj.get("total").toString());
							monitoringInfo.setUse(hddObj.get("use").toString());
							monitoringInfo.setUsePercent(hddObj.get("use_percent").toString());

							monitoringInfoService.insertMonitoringInfo(monitoringInfo);
						}
					}
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
			}
		}
	}

	public void checkCustomerInfoChange() {}

	public void checkChannelInfoChange() {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("CRONTAB");
		etcConfigInfo.setConfigKey("CHANNEL_INFO_CHANGE");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if (etcConfigResult.size() > 0 && etcConfigResult.get(0).getConfigValue().equals("Y")) {

			List<ChannInfo> selectChSysCode = channelInfoService.selectChSysCode(); // sysCode에 있는 값들이 매핑이되는지 (없는 값이면 해당
																					// sysCode 데이터 삭제)
			for (int n = 0; n < selectChSysCode.size(); n++) {
				Integer compCnt = channelInfoService.selectCompSysCode(selectChSysCode.get(n).getrSysCode()); // sysCode
																												// 0이면
																												// 삭제
																												// 1이면
																												// 작업진행
				if (compCnt == 0) {
					ChannInfo tmpSysCode = new ChannInfo();
					tmpSysCode.setrSysCode(selectChSysCode.get(n).getrSysCode());
					channelInfoService.deleteChInfo(tmpSysCode);
				}
			}

			List<ChannInfo> selectSyscode = channelInfoService.selectSysCode();
			for (int k = 0; k < selectSyscode.size(); k++) { // syscode별로 확인
				String sysCode = selectSyscode.get(k).getrSysCode();

				List<ChannInfo> mariaChannelList = oraGetDbService.selectChannelInfo();
				Integer mariaChannelSize = mariaChannelList.size();

				if (mariaChannelSize > 0) { // maria에 있는 channel List
					List<ChannInfo> postChannelList = channelInfoService.selectChennelInfo(sysCode);
					Integer postChannelSize = postChannelList.size();

					if (postChannelSize > 0) { // postgres에 있는 channel List
						for (int i = 0; i < mariaChannelSize; i++) {
							for (int j = 0; j < postChannelSize; j++) {
								if (mariaChannelList.get(i).getrChNum().toString()
										.equals(postChannelList.get(j).getrExtNum())) {
									mariaChannelList.get(i).setrSysCode(sysCode);
									if (mariaChannelList.get(i).getrRecYn().equals("1")) {
										mariaChannelList.get(i).setrRecYn("Y");
									} else {
										mariaChannelList.get(i).setrRecYn("N");
									}
									int result = channelInfoService.updateChInfo(mariaChannelList.get(i));
									if (result > 0) {
										break;
									}
								} else { // chnum 다르다면
									Integer infoCheckNum = 0;
									mariaChannelList.get(i).setrSysCode(sysCode);
									infoCheckNum = channelInfoService.checkChInfo(mariaChannelList.get(i)); // 해당
																											// ch_num을
																											// postDB에서
																											// 찾는다.
									if (infoCheckNum > 0) {
										continue;
									} else {
										// if(mariaChannelList.get(i).getrChNum().toString().equals(postChannelList.get(j).getrExtNum())){
										if (mariaChannelList.get(i).getrRecYn().equals("1")) {
											mariaChannelList.get(i).setrRecYn("Y");
										} else {
											mariaChannelList.get(i).setrRecYn("N");
										}
										/*
										 * List<ChannInfo> selectSysCode=channelInfoService.selectSysCode(); for(int k=0
										 * ; k< selectSysCode.size(); k++){
										 */
										mariaChannelList.get(i).setrSysCode(sysCode);
										channelInfoService.insertChInfo(mariaChannelList.get(i)); // insert
										// }
										// }
									}
								}
							}
						} // for문 끝

						mariaChannelList = oraGetDbService.selectChannelInfo();
						mariaChannelSize = mariaChannelList.size();

						postChannelList = channelInfoService.selectChennelInfo(sysCode);
						postChannelSize = postChannelList.size();

						if (postChannelSize > mariaChannelSize) { // 삭제해야할 사항이 생긴 것.
							for (int i = 0; i < postChannelSize; i++) {
								Integer infoCheckNum = 0;
								infoCheckNum = oraGetDbService.selectMChannCnt(postChannelList.get(i)); // 해당 ch_num을
																										// postDB에서 찾는다.

								if (infoCheckNum == 0) {
									// post에서 해당 ch num삭제
									postChannelList.get(i).setrSysCode(sysCode);
									int result = channelInfoService.deleteChInfo(postChannelList.get(i));
									if (result > 0) {

									}
								}
							}
						}
					} else {
						// channel_postData가 비어있으면 channel info insert!!
						for (int i = 0; i < mariaChannelSize; i++) {
							if (mariaChannelList.get(i).getrRecYn().equals("1")) {
								mariaChannelList.get(i).setrRecYn("Y");
							} else {
								mariaChannelList.get(i).setrRecYn("N");
							}
							/*
							 * List<ChannInfo> selectSyscode=channelInfoService.selectSysCode(); for(int k=0
							 * ; k<selectSyscode.size() ; k++){
							 */
							mariaChannelList.get(i).setrSysCode(sysCode);
							channelInfoService.insertChInfo(mariaChannelList.get(i)); // insert
							// }
						}
					}

				}
			}
		}
	}

	public void checkCustomerInfoToRecsee() {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("CRONTAB");
		etcConfigInfo.setConfigKey("CUSTOMER_INFO_TO_RECSEE");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if (etcConfigResult.size() > 0 && etcConfigResult.get(0).getConfigValue().equals("Y")) {

			SearchListInfo searchListInfo = new SearchListInfo();

			Calendar prevYear = Calendar.getInstance();
			String eDate = String.format("%d%02d%02d", prevYear.get(Calendar.YEAR), prevYear.get(Calendar.MONTH) + 1,
					prevYear.get(Calendar.DATE));
			String eTime = String.format("%02d%02d%02d", prevYear.get(Calendar.HOUR_OF_DAY),
					prevYear.get(Calendar.MINUTE), prevYear.get(Calendar.SECOND));

			prevYear.add(Calendar.HOUR_OF_DAY, -6);
			String sDate = String.format("%d%02d%02d", prevYear.get(Calendar.YEAR), prevYear.get(Calendar.MONTH) + 1,
					prevYear.get(Calendar.DATE));
			String sTime = String.format("%02d%02d%02d", prevYear.get(Calendar.HOUR_OF_DAY),
					prevYear.get(Calendar.MINUTE), prevYear.get(Calendar.SECOND));

			searchListInfo.setsDate(sDate);
			searchListInfo.seteTime(sTime);

			searchListInfo.seteDate(eDate);
			searchListInfo.seteTime(eTime);

			List<SearchListInfo> searchListInfoResult = searchListInfoService.selectCustToRecSeeInfo(searchListInfo);
			Integer searchListInfoTotal = searchListInfoResult.size();

			if (searchListInfoTotal > 0) {

				HashMap<String, Object> Info = null;

				for (int i = 0; i < searchListInfoTotal; i++) {
					SearchListInfo rowItem = searchListInfoResult.get(i);

					Info = new HashMap<String, Object>();
					Info.put("P_CUST_PHONE_NO", rowItem.getCustPhone1());
					Info.put("P_INFO_YN", "");
					Info.put("P_CUST_NM", "");
					Info.put("P_JUMIN_NO", "");

					oraGetDbService.selectCustInfo(Info);

					String pInfoYn = Info.get("P_INFO_YN").toString();

					if (pInfoYn.equals("Y")) {
						searchListInfo = new SearchListInfo();
						searchListInfo.setsDate(sDate);
						searchListInfo.seteTime(sTime);

						searchListInfo.seteDate(eDate);
						searchListInfo.seteTime(eTime);

						searchListInfo.setCustPhone1(rowItem.getCustPhone1());

						if (Info.get("P_CUST_NM") != null && !Info.get("P_CUST_NM").toString().isEmpty()) {
							searchListInfo.setCustName(Info.get("P_CUST_NM").toString());
							if (Info.get("P_JUMIN_NO") != null && !Info.get("P_JUMIN_NO").toString().isEmpty()) {
								searchListInfo.setCustSocialNum(Info.get("P_JUMIN_NO").toString());
							}
						}
						searchListInfoService.updateCustToRecSeeInfo(searchListInfo);
					} else {

					}
				}
			}
		}
	}

	public void stackRecStatus() {
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("CRONTAB");
		etcConfigInfo.setConfigKey("REC_STATUS_INFO"); // REC 통계테이블에 데이터를 밀어넣는 CronTab
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if (etcConfigResult.size() > 0 && etcConfigResult.get(0).getConfigValue().equals("Y")) {
			Integer recStatusListCnt = recStatusInfoService.selectRecStatus();

			Calendar calendar = Calendar.getInstance();

			if (recStatusListCnt == 0) { // recStatus 테이블에 데이터가 하나도 없다면
				// System.out.println("데이터가 없다.");

				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, -1); // 1초전
				DateFormat tmpDate = new SimpleDateFormat("yyyyMMdd HHmmss");
				String recTime = tmpDate.format(cal.getTime());

				String rRecDate = recTime.substring(0, 8); // 1초전
				String rRecHh24 = recTime.substring(9, 11); // 1초전
				String rRecMi = recTime.substring(11, 13); // 1초전
				String rRecSs = recTime.substring(13, 15); // 1초전

				DashboardInfo selectRecElement = new DashboardInfo();
				selectRecElement.setrRecDate(rRecDate);
				selectRecElement.setrRecHh24(rRecHh24);
				selectRecElement.setrRecMi(rRecMi);
				selectRecElement.setrRecSs(rRecSs);

				Calendar calOri = Calendar.getInstance();
				DateFormat tmpDateOri = new SimpleDateFormat("yyyyMMdd HHmmss"); // 1초 안뺀 시간
				String recTimeOri = tmpDateOri.format(calOri.getTime());

				recStatusInfoService.updateEtcConfigTime(recTimeOri); // etc config -Monitoring의 시간 저장

				List<DashboardInfo> resultList = recStatusInfoService.selectFirstRecfileInfo(selectRecElement);
				for (int i = 0; resultList.size() > i; i++) {
					DashboardInfo insertRecElement = new DashboardInfo();
					insertRecElement.setrExtNum(resultList.get(i).getrExtNum());
					insertRecElement.setrRecDate(resultList.get(i).getrRecDate());
					insertRecElement.setrRecHh24(resultList.get(i).getrRecHh24());
					insertRecElement.setrRecMi(resultList.get(i).getrRecMi());
					insertRecElement.setrRecCnt(resultList.get(i).getrRecCnt());
					recStatusInfoService.insertRecStatus(insertRecElement); // 테이블에 insert LATELY_DATETIME에
																			// yyyymmddhh24miss 형식으로 입력
				}
			} else { //// recStatus 테이블에 데이터가 있다면
				// System.out.println("데이터가 있다.");
				DashboardInfo selectRecElement = new DashboardInfo();
				String recordTime = recStatusInfoService.selectEtcConfigTime(); // 마지막으로 업데이트 된 날짜와 시간 yyyymmddhh24miss
																				// 로 저장되어있음.

				DashboardInfo selectElement = new DashboardInfo();
				selectElement.setrRecDate(recordTime.substring(0, 8)); // etc_config에 저장된 날짜
				selectElement.setrRecHh24(recordTime.substring(9, 11));
				selectElement.setrRecMi(recordTime.substring(11, 13));
				selectElement.setrRecSs(recordTime.substring(13, 15));

				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, -1); // 1초전
				DateFormat tmpDate = new SimpleDateFormat("yyyyMMdd HHmmss");
				String recTime = tmpDate.format(cal.getTime());

				selectElement.setrCallEtime(recTime);
				// System.out.println("1초전 시간"+recTime);

				Calendar calOri = Calendar.getInstance();
				DateFormat tmpDateOri = new SimpleDateFormat("yyyyMMdd HHmmss"); // 1초 안뺀 시간
				String recTimeOri = tmpDateOri.format(calOri.getTime());

				recStatusInfoService.updateEtcConfigTime(recTimeOri); // etc config -Monitoring의 시간 저장

				List<DashboardInfo> resultList = recStatusInfoService.selectRecfileInfo(selectElement);

				for (int i = 0; resultList.size() > i; i++) {
					DashboardInfo insertRecElement = new DashboardInfo();
					insertRecElement.setrExtNum(resultList.get(i).getrExtNum());
					insertRecElement.setrRecDate(resultList.get(i).getrRecDate());
					insertRecElement.setrRecHh24(resultList.get(i).getrRecHh24());
					insertRecElement.setrRecMi(resultList.get(i).getrRecMi());
					insertRecElement.setrRecCnt(resultList.get(i).getrRecCnt());

					DashboardInfo compResult = recStatusInfoService.selectCompRecData(insertRecElement); // 비교하여 같은 날짜와
																											// 시분이 존재여부

					if (compResult == null) { // 같은 날짜 시분의 데이터가 없으면 insert
						recStatusInfoService.insertRecStatus(insertRecElement); // 테이블에 insert LATELY_DATETIME에
																				// yyyymmddhh24miss 형식으로 입력
					} else { // 있으면 rec cnt 더하여 update
						recStatusInfoService.updateRecStatus(compResult);
					}
				}

			}
		}
	}

	// stt프로시져돌리기
	public void addSttList() {

		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("PLAYER");
		etcConfigInfo.setConfigKey("STT_PLAYER");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("STT_TARGET_SERVER");
		List<EtcConfigInfo> etcConfigResult2 = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		if ((etcConfigResult.size() > 0 && etcConfigResult.get(0).getConfigValue().equals("Y"))
				&& (etcConfigResult2.size() > 0
						&& !StringUtil.isNull(etcConfigResult2.get(0).getConfigValue(), true))) {
			String[] targetServer = etcConfigResult2.get(0).getConfigValue().split(",");

			for (int i = 0; i < targetServer.length; i++) {
				recStatusInfoService.insertSttList(targetServer[i].trim());
			}
		}
	}
	
	// stt프로시져돌리기
	public void meritzUserMapping() {

		MeritUser meritUser = new MeritUser();
		
		meritUser.setUse_yn("Y");
		
		customerInfoService.meritzUserDelete(meritUser);
		
		List<MeritUser> meritUserResult =  customerInfoService.meritzUserMapping(meritUser);
		
		for(int i=0; i < meritUserResult.size();i++) {
			customerInfoService.meritzUserInsert(meritUserResult.get(i));			
		}
		
		customerInfoService.meritzRUserInsert();
	}

	public void dbScheduler() {
		ExecuteManage executeManage = new ExecuteManage();
		List<ExecuteManage> executeManageList = executeManageService.selectExecuteManage(executeManage);

		// 스케줄에 등록된 내역이 없으면 실행x
		if (executeManageList.size() > 0) {

			// 객체 생성
			JobManage jobManage = new JobManage();
			DBManage dbManage = new DBManage();
			SQLManage sqlManage = new SQLManage();

			// 날짜 시간 요일
			Calendar cal = Calendar.getInstance();
			DateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
			String calNow = format.format(cal.getTime());
			String weekOfDay = Integer.toString(cal.get(Calendar.DAY_OF_WEEK));

			for (int i = 0; i < executeManageList.size(); i++) {

				// flag N인경우만 실행
				if (executeManageList.get(i).getrExecuteFlag().equals("N")) {

					// daily, weekly, monthly에 따른 switch case문
					switch (executeManageList.get(i).getrSchedulerSelect()) {
					case "d":
						if (calNow.substring(9, 11).equals(executeManageList.get(i).getrSchedulerHour())) {
							jobManage.setJobName(executeManageList.get(i).getJobName());
							jobManage = jobManageService.selectOneJobManage(jobManage);

							// 작업 내 속한 db&sql set 리스트가 각각 컬럼에 "/"를 기준으로 나눠져 있기 때문에 스플릿
							String[] dbList = jobManage.getDbName().split("/");
							String[] sqlList = jobManage.getSqlName().split("/");

							Generator gen = new Generator();

							// 작업그룹에 속해있는 db&sql set 개수 만큼 정보 가져오기
							for (int j = 0; j < dbList.length; j++) {
								// db 정보 가져오기
								dbManage.setDbName(dbList[j]);
								dbManage = dbManageService.selectOneDBManage(dbManage);

								// 쿼리정보 가져오기
								sqlManage.setSqlName(sqlList[j]);
								sqlManage = sqlManageService.selectOneSQLManage(sqlManage);

								// generator에 db 및 쿼리 정보 입력하기
								DBInfoList.DBInfo dbinfo = new DBInfoList.DBInfo(dbManage.getDbServer(),
										dbManage.getUrl(), dbManage.getId(), dbManage.getPw(), dbManage.getTimeout());
								gen.add(dbinfo, sqlManage.getSql());
							}

							try {
								gen.execute();
								executeManage.setExecuteName(executeManageList.get(i).getExecuteName());
								executeManage.setrExecuteFlag("Y");
								executeManage.setrExecuteDate(calNow.substring(0, 8));
								executeManage.setrExecuteTime(calNow.substring(9, 15));
								executeManageService.updateStatusExecuteManage(executeManage);
							} catch (Exception e) {
								logger.error("error",e);
							}
						}
						break;
					case "w":
						if (calNow.substring(9, 11).equals(executeManageList.get(i).getrSchedulerHour())
								&& weekOfDay.equals(executeManageList.get(i).getrSchedulerWeek())) {
							jobManage.setJobName(executeManageList.get(i).getJobName());
							jobManage = jobManageService.selectOneJobManage(jobManage);

							String[] dbList = jobManage.getDbName().split("/");
							String[] sqlList = jobManage.getSqlName().split("/");

							Generator gen = new Generator();

							// 작업그룹에 속해있는 db&sql set 개수 만큼 정보 가져오기
							for (int j = 0; j < dbList.length; j++) {
								// db 정보 가져오기
								dbManage.setDbName(dbList[j]);
								dbManage = dbManageService.selectOneDBManage(dbManage);

								// 쿼리정보 가져오기
								sqlManage.setSqlName(sqlList[j]);
								sqlManage = sqlManageService.selectOneSQLManage(sqlManage);

								// generator에 db 및 쿼리 정보 입력하기
								DBInfoList.DBInfo dbinfo = new DBInfoList.DBInfo(dbManage.getDbServer(),
										dbManage.getUrl(), dbManage.getId(), dbManage.getPw(), dbManage.getTimeout());
								gen.add(dbinfo, sqlManage.getSql());
							}

							try {
								gen.execute();
								executeManage.setExecuteName(executeManageList.get(i).getExecuteName());
								executeManage.setrExecuteFlag("Y");
								executeManage.setrExecuteDate(calNow.substring(0, 8));
								executeManage.setrExecuteTime(calNow.substring(9, 15));
								executeManageService.updateStatusExecuteManage(executeManage);
							} catch (Exception e) {
								logger.error("error",e);
							}
						}
						break;
					case "m":
						if (calNow.substring(9, 11).equals(executeManageList.get(i).getrSchedulerHour())
								&& calNow.substring(6, 8).equals(executeManageList.get(i).getrSchedulerDay())) {
							jobManage.setJobName(executeManageList.get(i).getJobName());
							jobManage = jobManageService.selectOneJobManage(jobManage);

							String[] dbList = jobManage.getDbName().split("/");
							String[] sqlList = jobManage.getSqlName().split("/");

							Generator gen = new Generator();

							// 작업그룹에 속해있는 db&sql set 개수 만큼 정보 가져오기
							for (int j = 0; j < dbList.length; j++) {
								// db 정보 가져오기
								dbManage.setDbName(dbList[j]);
								dbManage = dbManageService.selectOneDBManage(dbManage);

								// 쿼리정보 가져오기
								sqlManage.setSqlName(sqlList[j]);
								sqlManage = sqlManageService.selectOneSQLManage(sqlManage);

								// generator에 db 및 쿼리 정보 입력하기
								DBInfoList.DBInfo dbinfo = new DBInfoList.DBInfo(dbManage.getDbServer(),
										dbManage.getUrl(), dbManage.getId(), dbManage.getPw(), dbManage.getTimeout());
								gen.add(dbinfo, sqlManage.getSql());
							}

							try {
								gen.execute();
								executeManage.setExecuteName(executeManageList.get(i).getExecuteName());
								executeManage.setrExecuteFlag("Y");
								executeManage.setrExecuteDate(calNow.substring(0, 8));
								executeManage.setrExecuteTime(calNow.substring(9, 15));
								executeManageService.updateStatusExecuteManage(executeManage);
							} catch (Exception e) {
								logger.error("error",e);
							}
						}
						break;

					}

				} else {
					// flag N일떄
					if (!executeManageList.get(i).getrExecuteDate().equals(calNow.substring(0, 8))) {
						executeManage.setExecuteName(executeManageList.get(i).getExecuteName());
						executeManage.setrExecuteFlag("N");
						executeManageService.updateFlagExecuteManage(executeManage);
					}

				} // flag Y 처리
			} // for 끝
		} else {
			return;
		}
	}


	public void BackupScheStart() {}

}// 클래스 끝
