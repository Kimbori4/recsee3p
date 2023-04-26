package com.furence.recsee.monitoring.controller;


import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.codehaus.jackson.map.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.RedisService;

@Component
public class MonitoringWebSocket{
	
	private ServletContext sc;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private RedisService redisService;
	
	public MonitoringWebSocket(){
//		Runnable a= new serverRun();
//		Thread t = new Thread(a);
//		t.start();
	}
	
	public class serverRun implements Runnable {
		private final org.slf4j.Logger logger= LoggerFactory.getLogger(serverRun.class);
		
		WebSocketServer server;
		
		// websocket List 만들기
		List<WebSocket> wsList = new ArrayList<>();
		
		@Override
		public void run() {
			logger.info("=============== WebSocket Server Start ====================");
			server = new WebSocketServer(new InetSocketAddress(12969)) {
				
				@Override
				public void onOpen(WebSocket ws, ClientHandshake arg1) {
					//웹소켓 리스트 추가해주기
					wsList.add(ws);
				}
				
				@Override
				public void onMessage(WebSocket ws, String msg) {
					// TODO Auto-generated method stub
					
					if("getCurrentTime".equals(msg)) {
						ws.send("[{\"getTime\" : \""+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + " \"}]");	
					}
					
					if("getUserTime".equals(msg)) {
						ws.send("[{\"getUserTime\" : \""+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + " \"}]");	
					}
					
	/*				if(msg.indexOf("getTimeExt") > -1) {
						System.out.println(msg.split("=")[1]);
					}*/
				}
				
				@Override
				public void onError(WebSocket ws, Exception arg1) {
					// TODO Auto-generated method stub
					wsList.remove(ws);
				}
				
				@Override
				public void onClose(WebSocket ws, int arg1, String arg2, boolean arg3) {
					wsList.remove(ws);
				}
				
			};
			
			Runnable r = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true) {
						try {
							String res = monitoringExt().toJSONString();
							for(int i = 0 ; i < wsList.size() ; i ++ ) {
								wsList.get(i).send(res);
							}
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							logger.error("error",e);
						}
					}
				}
			};
			
			Thread t = new Thread(r);
			t.start();
			
			server.run();
			
		}
		
		
		public  JSONArray monitoringExt(){
			
			String[] sysId = null;
			String masking = "";
			JSONArray arr = new JSONArray();
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_IP");
			List<EtcConfigInfo> redisIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			sysId = redisIp.get(0).getConfigValue().split(",");

			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("REDIS_PORT");
			List<EtcConfigInfo> redisPort = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			etcConfigInfo.setGroupKey("MONITORING");
			etcConfigInfo.setConfigKey("Masking");
			List<EtcConfigInfo> maskingYn = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			masking = maskingYn.get(0).getConfigValue();

			Map<String, Object>  resultHMap = new HashMap<>();

			// 레디스 갯수
			int redisCnt = 0;

			//	현재시간
			SimpleDateFormat dayTime = new SimpleDateFormat("YYYYMMddHHmmss");

			boolean bAddFlag = false;
			for(int i=0;i<sysId.length;i++) {
				try {
					JSONArray selectAgent = redisService.getExtStatus(sysId[i],"RECSEE_EXTSTATUS",Integer.parseInt(redisPort.get(0).getConfigValue()));
					if(selectAgent==null)
						redisCnt=0;
					else 
						redisCnt = selectAgent.size();
					
					for(int j=0;j<redisCnt;j++) {

						Map<String,Object> currMap = getMapFromJsonObject((JSONObject)selectAgent.get(j));
						if(resultHMap.get(currMap.get("EXT")) != null) {
							continue;
						}
						Map<String,Object> resultMap = getMapFromJsonObject((JSONObject)resultHMap.get(currMap.get("EXT")));

						if(masking.equals("Y")) {
							String cust="";
							
							if(currMap.get("CUSTNUM")!=null)
								cust = new RecSeeUtil().makePhoneNumber((String) currMap.get("CUSTNUM"));
							
							cust = new RecSeeUtil().maskingNumber(cust);
							currMap.put("CUSTNUM", cust);
							JSONObject chageCust = (JSONObject)selectAgent.get(j);
							chageCust.put("CUSTNNUM", cust);
						}

						if (resultMap  == null)
						{  //최초
							resultHMap.put((String)currMap.get("EXT"), selectAgent.get(j));
						}else
						{
							//비교
							long curDateTime = dayTime.parse(currMap.get("UPDATETIME").toString()).getTime();
							long resultDateTime = dayTime.parse(resultMap.get("UPDATETIME").toString()).getTime();
							long minute = Math.abs((curDateTime - resultDateTime) / 60000 );

							if(minute <2) {
								if(currMap.get("CTI").equals("CALLING") && currMap.get("RTP").equals("1")) {
									//위에 플레그..
									if (bAddFlag == true)
									{
										resultHMap.put((String) currMap.get("EXT") , currMap);
										bAddFlag = false;
									}else
									{
										if(resultMap.get("CTI").equals("CALLING") && resultMap.get("RTP").equals("1")) {
											//버림.
										}else
										{
											resultHMap.put((String) currMap.get("EXT") , currMap);
										}
										bAddFlag = true;
									}

								}
							}else {
								if (curDateTime - resultDateTime > 0)
										resultHMap.put((String) currMap.get("EXT") , currMap);
							}
						}

					}
				}catch(NullPointerException e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
				catch(Exception e) {
					logger.error("",e);
					//만약 레디스 서버 연결 안될 경우 해당 아이피 표시
					//logger.error("REDIS CONNECTION ERR IP:  "+sysId[i]);
				}
			}


			Collection<Object> values = resultHMap.values();

			arr.add(0 , "ExtStatus");
			for(Object obj : values) {
				arr.add(obj);
			}
			
			return arr;
		}
		
		/*
		==============================================
		JSONObject to MAP
		==============================================
		*/

		@SuppressWarnings("unchecked")
		public Map<String,Object> getMapFromJsonObject(JSONObject jsonObj){
			if (jsonObj == null)
				return null;

			Map<String,Object> map = null;

			try {
				map = new ObjectMapper().readValue(jsonObj.toJSONString(),Map.class);
			}catch(NullPointerException e) {
				logger.error("",e);
			} catch(Exception e) {
				logger.error("",e);
			}
			return map;
		}
		
	}
}

