package com.furence.recsee.monitoring.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.PhoneMappingInfoService;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.service.SystemInfoService;
import com.furence.recsee.common.util.RedisService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.monitoring.service.MonitoringInfoService;

@Controller
@RequestMapping("/monitoring")
public class XmlMontoringController {
	private static final Logger logger = LoggerFactory.getLogger(XmlMontoringController.class);
	@Autowired
	private SystemInfoService systemInfoService;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private RUserInfoService ruserInfoService;
	
	@Autowired
	private MonitoringInfoService monitoringInfoService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private SubNumberInfoService subNumberInfoService;
	
	@Autowired
	private PhoneMappingInfoService phoneMappingInfoService;
	
	@Autowired
	private MessageSource messageSource;
	
	// 채널 모니터링
	@RequestMapping(value="/channelMonitoring.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml channelMonitoring(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			
			
			String systemIp = request.getParameter("serverip");
			String redisTable = request.getParameter("redisTable");
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_PORT");
			List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);


			ArrayList<String> selectTitle = new ArrayList<>();
			try {
				
				// 레디스 데이터에서 먼저 Tilte 값을 가져온다.
						
				selectTitle = redisService.getRecSeeRTPTitle(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));
					
			
			}catch(NullPointerException e) {
				logger.error("error",e);
			}
			catch(Exception e) {
				logger.error("error",e);
			}
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			for(int j = 0; j < selectTitle.size(); j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setHidden("0");
				column.setWidth("*");
				column.setCache("0");
				column.setId(selectTitle.get(j));
				column.setFiltering("0");	
				column.setValue("<div style=\"text-align:center;\">"+selectTitle.get(j)+"</div>");				
				head.getColumnElement().add(column);
				
				column = null;
			}
			xmls.setHeadElement(head);
			
		
			JSONArray selectData = redisService.getRecSeeRTPMessage(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));		
			
			
			
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			for(int i = 0; i < selectData.size() ; i++) {
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
				JSONObject jsonObject = new JSONObject();
				
				jsonObject = (JSONObject) selectData.get(i);
				
				for(int k=0;k<selectTitle.size();k++) {
					String KeyValue = selectTitle.get(k);
					
					cellInfo = new dhtmlXGridRowCell();
					
					if("cti".equals(KeyValue.toLowerCase())) {
						
						String status = ((String)jsonObject.get(KeyValue)).toLowerCase();
						String statusTxt = ((String)jsonObject.get(KeyValue)).toLowerCase();			
						if (statusTxt.equals("login")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg8", null,Locale.getDefault());  /*"로그인"*/;
						} else if (statusTxt.equals("logout")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg9", null,Locale.getDefault());  /*"로그아웃"*/;
						} else if (statusTxt.equals("calling")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg10", null,Locale.getDefault());  /*"전화중"*/;
						} else if (statusTxt.equals("aftercallwork")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg11", null,Locale.getDefault());  /*"후처리"*/;
						} else if (statusTxt.equals( "ready")) {
							statusTxt = messageSource.getMessage("monitoring.alert.msg12", null,Locale.getDefault());  /*"준비"*/;
						} else if (statusTxt.equals("ringing")){
							statusTxt = messageSource.getMessage("monitoring.alert.msg13", null,Locale.getDefault());  /*"연결중"*/;
						}else if(statusTxt.equals("offering")){
							statusTxt = messageSource.getMessage("monitoring.alert.msg9", null,Locale.getDefault());  /*"로그아웃"*/;
						} else {
							statusTxt = messageSource.getMessage("monitoring.alert.msg14", null,Locale.getDefault());  /*"이석"*/;
						}
						
						if(jsonObject.get("Status") != null){
							if(jsonObject.get("Status").equals("RECORDING")) {
								statusTxt = messageSource.getMessage("monitoring.alert.msg10", null,Locale.getDefault());  /*"전화중"*/;
								status = "calling";
							}
						}

						String statusString = "<div class='grid_status grid_status_login'>로그인</div>".replace("login", status);
						statusString = statusString.replace("로그인", statusTxt);
						
						cellInfo.setValue(statusString);
					}else if("rtp".equals(KeyValue.toLowerCase())) {
						String rtpValue = ((String)jsonObject.get(KeyValue));
						String ctiValue = ((String)jsonObject.get("CTI"));
						String stayListen = "";
						if(("1".equals(rtpValue) && "calling".toUpperCase().equals(ctiValue)) || "1".equals(rtpValue) && jsonObject.get("Status").equals("RECORDING")) {
							stayListen = "<div class='recodeButton card_record_press'></div>";
							
						}else {
							stayListen = "<div class='recodeButton card_record_acw'></div>";
						}
						
						cellInfo.setValue(stayListen);
					}else if ("calltype".equals(KeyValue.toLowerCase())) {
						String callTypeValue = ((String)jsonObject.get(KeyValue));
						if(callTypeValue.length() > 0) {
							String statusTxt = messageSource.getMessage("call.type."+callTypeValue, null,Locale.getDefault());  /*"전화중"*/;
							cellInfo.setValue(statusTxt);
						}else {
							cellInfo.setValue((String)jsonObject.get(KeyValue));
						}
					}else {		
						cellInfo.setValue((String)jsonObject.get(KeyValue));
					}
					rowItem.getCellElements().add(cellInfo);
				}
				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
				
			}
		}
		return xmls;
	}
	
	// 로그 모니터링
	@RequestMapping(value="/logMonitoring.xml", method = { RequestMethod.GET, RequestMethod.POST }, produces="application/xml")
	public @ResponseBody dhtmlXGridXml logMonitoring(HttpServletRequest request, HttpServletResponse response, Locale locale) throws MalformedURLException, UnsupportedEncodingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		if (userInfo != null) {
			
			
			String systemIp = request.getParameter("serverip");
			String redisTable = request.getParameter("redisTable");
			
			try {
			redisTable = redisTable.split("_")[0]+"_LOG";
			}catch (Exception e) {
				redisTable = "RECSEE_LOG";
			}
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_PORT");
			List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		
			
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
			column.setType("ro");
			column.setSort("str");
			column.setAlign("center");
			column.setFiltering("1");
			column.setEditable("0");	
			column.setHidden("0");
			column.setWidth("*");
			column.setCache("0");
			column.setAlign("left");
			column.setId("LOG");
			column.setFiltering("0");	
			column.setValue("<div style=\"text-align:center;\">LOG</div>");				
			head.getColumnElement().add(column);
			column = null;
			xmls.setHeadElement(head);
			
		
			JSONArray selectData = redisService.getRecSeeLog(systemIp,redisTable,Integer.parseInt(redisPort.get(0).getConfigValue()));		
							
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			for(int i = 0; i < selectData.size() ; i++) {
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());				
				
				String value = (String)selectData.get(i);
					
				cellInfo = new dhtmlXGridRowCell();			
					
				cellInfo.setValue("<div>&nbsp&nbsp&nbsp&nbsp&nbsp"+value+"</div>");
				rowItem.getCellElements().add(cellInfo);				
				xmls.getRowElements().add(rowItem);
				rowItem = null;
				
			}
		}
		return xmls;
	}

}
