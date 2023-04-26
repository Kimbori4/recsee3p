package com.furence.recsee.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.initech.shttp.server.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedisService.class);
/*	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valuesOps;
	
	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOps;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String,String, String> hshOps;*/
	
	public JSONArray getExtStatusTEST(String ip, String key, int port,String masking) {
		
		StringBuilder sB = new StringBuilder();
		JSONParser jp = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		
		
		Jedis jedis = new Jedis(ip ,port);
		jedis.connect();
		
		Set<String> ExtStatus = jedis.hkeys(key);
		
		Iterator<String> extIt = ExtStatus.iterator();			//Ext Iterator
		
		Map<String,String> getAll =jedis.hgetAll(key);
		
		while(extIt.hasNext()) {
			String data = extIt.next();
			//System.out.println(data);
			
			String a = new String(getAll.get(data));
			//System.out.println(a);
			
			JSONObject obj = null;
			try {
				obj= (JSONObject) jp.parse(a);
				if(masking.equals("Y"))
				{
					String cust = new RecSeeUtil().makePhoneNumber((String) obj.get("CUSTNUM"));
					cust = new RecSeeUtil().maskingNumber(cust);
					obj.put("CUSTNUM",cust);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//logger.error("error",e);
				Logger.error("", "", "", e.toString());
			}
			jsonArr.add(obj);	
		}
		
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		
//		//Redis get Connection
//		RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
//		
//		Set<byte[]> ExtStatus = redisConnection.hKeys("RECSEE_EXTSTATUS".getBytes());
//		
//		Iterator<byte[]> extIt = ExtStatus.iterator();			//Ext Iterator
//		Map<byte[],byte[]> getAll =  redisConnection.hGetAll("RECSEE_EXTSTATUS".getBytes());
//		
//		while(extIt.hasNext()) {
//			byte[] data = (byte[])extIt.next();
//				String a = new String(getAll.get(data),0,getAll.get(data).length);
//				JSONObject obj = null;
//				try {
//					obj= (JSONObject) jp.parse(a);
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					logger.error("error",e);
//				}
//				jsonArr.add(obj);	
//		}
//		
//		redisConnection.close();
		
		/*
		 *  	�젅�뵒�뒪 援щ룆 �븯湲� 硫붿꽌�뱶
		 * 
		 */
		
		//redisConnection.pSubscribe(arg0, arg1);
		
		return jsonArr;
	}
	
	public String setExtStatus() {
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisPool pool = new JedisPool(jedisPoolConfig, "172.18.108.45" , 3690, 0);			//172.18.108.45
		
		Jedis jedis = pool.getResource();
		
	/*	for(int i=2200;i<2200;i++) {
			jedis.hset("RECSEE_EXTSTATUS", String.valueOf(i) ,"\r\n" + 
					"{\"EXT\":\""+String.valueOf(i)+"\",\"AGENTID\":\"\",\"AGENTNAME\":\"\",\"BGCODE\":\"\""
					+ ",\"MGCODE\":\"\",\"SGCODE\":\"\",\"STATUS\":\"NOTRECORDING\",\"RTP\":\"1\",\"CUSTNUM\":\"O\",\"CALLTYPE\":\"91\",\"CID\":\"\",\"STIME\":\"090810\",\"ETIME\":\"\",\"CTI\":\"CALLING\",\"UPDATETIME\":\"090848\"}");		
		}*/
		jedis.hset("RECSEE_SYSTEMSTATUS", "172.18.28.155" ,"{\"CPU_USAGE\":\"0.33\",\"MEM_TOTAL\":\"32761760\",\"MEM_USED\":\"3855092\",\"MEM_FREE\":\"27275040\",\"HDD_TOTAL\":\"745074240\",\"HDD_USED\":\"2551452\",\"HDD_FREE\":\"704668480\",\"POSTGRES\":\"off\",\"POSTGRES_CON\":\"0\",\"RECSEE\":\"on\",\"UPDATETIME\":\"20180116172542\"}");
	
		
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
	
		return "�꽦怨�";
	}
	
	public JSONArray getExtStatus(String ip, String key, int port) {
		StringBuilder sB = new StringBuilder();
		JSONParser jp = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		
		Jedis jedis = new Jedis(ip ,port);
		try {
			jedis.connect();
		}catch(Exception e) {
			return null;
		}
				
		Long befor = System.currentTimeMillis();
		
		Set<String> ExtStatus = jedis.hkeys(key);

		Iterator<String> extIt = ExtStatus.iterator();			//Ext Iterator
		
		Map<String,String> getAll =jedis.hgetAll(key);
		
		while(extIt.hasNext()) {
			String data = extIt.next();
//			System.out.println(data);
			
			String a = new String(getAll.get(data));
			
			JSONObject obj = null;
			try {
				obj= (JSONObject) jp.parse(a);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//logger.error("error",e);
				Logger.error("", "", "", e.toString());
			}
			jsonArr.add(obj);	
		}
			
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		
		return jsonArr;
	}
	
	public JSONArray getExtStatus(String ip, String key, int port, String selectCode, String selectArr) {
		StringBuilder sB = new StringBuilder();
		JSONParser jp = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		
		Jedis jedis = new Jedis(ip ,port);
		jedis.connect();
		
		Long befor = System.currentTimeMillis();
		
		Set<String> ExtStatus = jedis.hkeys(key);

		Iterator<String> extIt = ExtStatus.iterator();			//Ext Iterator
		
		Map<String,String> getAll =jedis.hgetAll(key);
		
		while(extIt.hasNext()) {
			String data = extIt.next();
			//System.out.println(data);
			
			String a = new String(getAll.get(data));
			//System.out.println(a);
			
			JSONObject obj = null;
			try {
				obj= (JSONObject) jp.parse(a);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//logger.error("error",e);
				Logger.error("", "", "", e.toString());
			}
			String [] selectedValues = selectArr.split(",");
			for(int i=0; i<selectedValues.length; i++) {
				String bgCode = "", mgCode = "", sgCode = "";
				String [] selectedValuesTemp = selectedValues[i].split("\\|");
				bgCode = selectedValuesTemp[0];
				try {
					mgCode = selectedValuesTemp[1];
				} catch(Exception e) {
					logger.error("error",e);
				}
				try { 
					sgCode = selectedValuesTemp[2];
				} catch(Exception e) {
					logger.error("error",e);
				}
				bgCode = Optional.ofNullable(bgCode).orElse("");
				mgCode = Optional.ofNullable(mgCode).orElse("");
				sgCode = Optional.ofNullable(sgCode).orElse("");
				
				String MGCODE = "";
				String SGCODE = "";
				String BGCODE ="";
				String AGENTNAME = "";
				String AGENTID = "";
				String EXT = "";
				if(obj != null) {
					MGCODE = (String) Optional.ofNullable(obj.get("MGCODE")).orElse("");
					SGCODE = (String) Optional.ofNullable(obj.get("SGCODE")).orElse("");
					BGCODE = (String) Optional.ofNullable(obj.get("BGCODE")).orElse("");
					AGENTNAME = (String) Optional.ofNullable(obj.get("AGENTNAME")).orElse("");
					AGENTID = (String) Optional.ofNullable(obj.get("AGENTID")).orElse("");
					EXT = (String) Optional.ofNullable(obj.get("EXT")).orElse("");
				}
				
				if(
					"BG".equals(selectCode) && BGCODE.equals(bgCode)
					|| ("MG".equals(selectCode) && BGCODE.equals(bgCode) && MGCODE.equals(mgCode))
					|| ("SG".equals(selectCode) && BGCODE.equals(bgCode) && MGCODE.equals(mgCode) && SGCODE.equals(sgCode))
					|| "NAME".equals(selectCode) && AGENTNAME.equals(Optional.ofNullable(selectedValues[i]).orElse(""))
					|| "NUM".equals(selectCode) && AGENTID.equals(Optional.ofNullable(selectedValues[i]).orElse(""))
					|| "EXT".equals(selectCode) && EXT.equals(Optional.ofNullable(selectedValues[i]).orElse(""))
				) {
					jsonArr.add(obj);
				}
			}
		}
			
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		
		return jsonArr;
	}
	
	public ArrayList<String> getRecSeeRTPTitle(String ip, String key, int port) {
		ArrayList<String> jsonArray = new ArrayList<>();
		
		Jedis jedis = new Jedis(ip ,port);
		jedis.connect();
				
		Map<String,String> getAll =jedis.hgetAll(key);
		
		String a = new String(getAll.get("Title"));
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) jsonParser.parse(a);
			Set<String> set = jsonObject.keySet();
			List sortList = new ArrayList<>(set);			
			Collections.sort(sortList);
			set = new HashSet<>(sortList);
			Iterator<String> iterator = set.iterator();
			
			while(iterator.hasNext()) {
				String data = iterator.next();
				
				jsonArray.add((String)jsonObject.get(data));
			}	
		} catch (ParseException e) {
			logger.error("error",e);
		}
		
			
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		
		return jsonArray;
	}
	
	public JSONArray getRecSeeRTPMessage(String ip, String key, int port) {
		JSONArray jsonArr = new JSONArray();
		JSONArray sortedJsonArray = new JSONArray();
		Jedis jedis = new Jedis(ip ,port);
		jedis.connect();
		
		Set<String> Status = jedis.hkeys(key);

		Iterator<String> It = Status.iterator();			//Ext Iterator
		
		Map<String,String> getAll =jedis.hgetAll(key);
		
		while(It.hasNext()) {
			String data = It.next();
			
			if(data.equals("Title")) {
				continue;
			}
			
			String a = new String(getAll.get(data));
			JSONParser jsonParser = new JSONParser();
			JSONObject obj = null;
			try {
				obj = (JSONObject) jsonParser.parse(a);
				jsonArr.add(obj);
			} catch (ParseException e) {
				logger.error("error",e);
			}		
			
		}
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		
	    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	    for (int i = 0; i < jsonArr.size(); i++) {
	        jsonValues.add((JSONObject) jsonArr.get(i));
	    }
	    
	    Collections.sort(jsonValues, new Comparator<JSONObject>() {
	    	private static final String KEY_NAME = "CH";
	    	 
			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();
				
				try {
					String tempvalA = (String)a.get(KEY_NAME);
					String tempvalB = (String)b.get(KEY_NAME);					
					
					
					
					valA = String.format("%03d", Integer.parseInt(tempvalA));
					valB = String.format("%03d", Integer.parseInt(tempvalB));
				}catch (Exception e) {
					logger.error("error",e);
				}

				
				return valA.compareTo(valB);
			}});
	    
	    for(int i= 0; i< jsonArr.size(); i++) {
	    	sortedJsonArray.add(jsonValues.get(i));
	    }
	    
		return sortedJsonArray;
	}
	
	public JSONArray getRecSeeLog(String ip, String key, int port) {
		JSONArray jsonArr = new JSONArray();
		Jedis jedis = new Jedis(ip ,port);
		jedis.connect();
		
		Set<String> Status = jedis.hkeys(key);
		
		Status.remove("LogInfo");
				
		List sortList = new ArrayList<>(Status);
				
		 Collections.sort(sortList, new Comparator<String>() {
			 public int compare(String o1, String o2) {
				 
				 int value1 = Integer.parseInt(o1);
				 int value2 = Integer.parseInt(o2);
				 
				 return String.format("%05d", value1).compareTo(String.format("%05d", value2));
			 }		 
		 });

		Iterator<String> It = sortList.iterator();			//Ext Iterator
		
		Map<String,String> getAll =jedis.hgetAll(key);
		
		while(It.hasNext()) {
			String data = It.next();
			
			String a = new String(getAll.get(data));
			jsonArr.add(a);	
			
		}
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}		
	    
		return jsonArr;
	}
	
	// 蹂꾨룄�쓽 �뒪�깭�떛 �븿�닔濡� 援ы쁽

	public static List sortByValue(final Map map) {
		List<String> list = new ArrayList();
		list.addAll(map.keySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Object v1 = map.get(o1);
				Object v2 = map.get(o2);
				return ((Comparable) v2).compareTo(v1);
			}
		});
		Collections.reverse(list); // 二쇱꽍�떆 �삤由꾩감�닚
		return list;
	}
	
	public JSONArray getSystem(String ip, int port, int timeout, String key, String pw) {
		JSONParser jp = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisPool pool = null;
		
		// �끃痍⑥꽌踰꾩� �쎒�꽌踰꾧� 媛숈� 寃쎌슦
		if("0".equals(pw.trim())) {
			pool = new JedisPool(jedisPoolConfig, ip , port);
		}else {
			pool = new JedisPool(jedisPoolConfig, ip , port, timeout, pw);
		}

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
		}catch(Exception e) {
			return null;
		}
		
		Set<String> sysKey = jedis.hkeys(key);
		Iterator<String> keyIterator = sysKey.iterator();	
		Map<String,String> getAll = jedis.hgetAll(key);
		Set<String> sysIp = new HashSet();
		
		ArrayList<JSONObject> temp = new ArrayList<>();
		while(keyIterator.hasNext()) {
			String data = keyIterator.next();
			sysIp.add(data.split("_")[0]); //
			String a = getAll.get(data);
			JSONObject obj = null;
			try {
				obj = (JSONObject) jp.parse(a);
			} catch (Exception e) {
				logger.error("error",e);
				Logger.error("", "", "", e.toString());
			}
			temp.add(obj);
		}
		
		Iterator<String> ipIterator = sysIp.iterator();	
		while(ipIterator.hasNext()) {
			String data = ipIterator.next();
			JSONArray jsonArrI = new JSONArray();
			for (int i = 0; i < temp.size(); i++) {
				if(data.equals(temp.get(i).get("sysIp"))) {
					jsonArrI.add(temp.get(i));
				}
			}
			
			/////// �젙�젹
			JSONArray sortedJsonArray = new JSONArray();

		    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		    for (int i = 0; i < jsonArrI.size(); i++) {
		        jsonValues.add((JSONObject) jsonArrI.get(i));
		    }
		    Collections.sort( jsonValues, new Comparator<JSONObject>() {
		        
		        private static final String KEY_NAME1 = "sysDate";
		        private static final String KEY_NAME2 = "sysTime";

		        @Override
		        public int compare(JSONObject a, JSONObject b) {
		            String valA = new String();
		            String valB = new String();

		            try {
		                valA = (String) a.get(KEY_NAME1)+a.get(KEY_NAME2);
		                valB = (String) b.get(KEY_NAME1)+b.get(KEY_NAME2);
		            } 
		            catch (Exception e) {
		            	logger.error("error",e);
		            }

		            return valA.compareTo(valB);
		        }
		    });

		    for (int i = 0; i < jsonArrI.size(); i++) {
		        sortedJsonArray.add(jsonValues.get(i));
		    }
			/////////////////
			
			jsonArr.add(sortedJsonArray);
		}
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		pool.destroy();
		
		return jsonArr;
	}

	public JSONArray getExtStatusBluePrint(String ip, String key, int port) {
		StringBuilder sB = new StringBuilder();
		JSONParser jp = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		
		Jedis jedis = new Jedis(ip ,port);
		try {
			jedis.connect();
		}catch(Exception e) {
			return null;
		}
				
		Long befor = System.currentTimeMillis();
		
		Set<String> ExtStatus = jedis.hkeys(key);

		Iterator<String> extIt = ExtStatus.iterator();			//Ext Iterator
		
		Map<String,String> getAll =jedis.hgetAll(key);
		
		while(extIt.hasNext()) {
			String data = extIt.next();
//			System.out.println(data);
			
			String a = new String(getAll.get(data));
			
			JSONObject obj = null;
			try {
				obj= (JSONObject) jp.parse(a);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//logger.error("error",e);
				//logger.error(e.toString());
				logger.error("error",e);
			}
			jsonArr.add(obj);	
		}
			
		
		//	�젅�뵒�뒪 �뿰寃� �썑 �걡�뼱二쇨린 ...
		jedis.disconnect();
		if(jedis.isConnected()==true) {
			jedis.disconnect();
		}
		
		return jsonArr;
	}
}
