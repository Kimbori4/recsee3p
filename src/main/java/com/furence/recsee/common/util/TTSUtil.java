package com.furence.recsee.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;






public class TTSUtil implements byteCode {
	
	
	private static final Logger logger = LoggerFactory.getLogger(TTSUtil.class);
	//TTS 생성
	public static HashMap<String,String> createTTS(String text,String userId) {
		HashMap<String,String> result = new HashMap<>();
		HttpURLConnection conn = null;
		ByteArrayOutputStream byteArrayOutputStream = null;

		URL url = null;
		String resultCode = null;                // 요청한 getAcceVal에 대한 응답코드를 저장하기 위한 변수
		String resultMessage = null;           // 요청한 getAcceVal에 대한 응답메시지를 저장하기 위한 변수
//		String acceVal = null;                    // 요청한 getAcceVal에 대한 인증키를 저장하기 위한 변수
//		String requestDateTime = null;        // 요청한 getAcceVal에 대한 서버시간을 저장하기 위한 변수(yyyyMMddHHmmssSSS)
		byte[] wav = null;                              		// 요청한 getTTS에 대한 WAV를 저장하기 위한 변수
		String crypto = null;  
		String encQueryString = null;
		Encoder encoder = Base64.getEncoder();
		
		
		String deviceId = "9801";
		String macAddr = MacAddr();
//		environment.
		String filePath = "";
		
		HashMap<String,String> ttsKeyMap = TTSKey(deviceId,macAddr);
		
		
//		logger.info(ttsKeyMap.get("acceVal"));
//		logger.info(ttsKeyMap.get("requestDateTime"));
//		logger.info(ttsKeyMap.get("resultCode"));
		
		if(!"0".equals(ttsKeyMap.get("resultCode"))) {
			result.put("result", "fail");
			return result;
		}
		
		
		Date requestDate = null;
		String fileName = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA); 
		try {
		    requestDate = sdf.parse(ttsKeyMap.get("requestDateTime"));    // 1단계에서 서버로부터 응답받은 String타입의 서버요청시간을 입력
		    fileName = "userId"+sdf.format(date)+".wav";
		} catch (java.text.ParseException e) {
		    // TODO Exception 처리
			logger.error("error",e);
		}
		
		if(requestDate == null) {
			throw new NullPointerException();
		}

		// 서버로부터 전달받은 요청시간값에 10초를 추가
		long timestamp = requestDate.getTime() + 10000;    // 요청시간값 + 10초                    
		requestDate.setTime(timestamp);

		String source = String.valueOf(deviceId)            	// 공통코드의 디바이스ID 정의 참조
		        .concat(macAddr)                   	// 클라이언트의 12자리 맥어드레스
		        .concat(sdf.format(requestDate))          	// getAcceVal을 통해 응받받은 서버요청시간 + 10초를 "yyyyMMddHHmmssSSS"로 변환한 값
		        .concat(ttsKeyMap.get("acceVal"));                         		// getAccesVal을 통해 응답받은 인증키값 

		
		StringBuffer hash = new StringBuffer();
		int byteSize = 32;                                             
		byte digest[] = new byte[byteSize];                                            

		Hash lsh = Hash.getInstance(Hash.Algorithm.LSH256_256);
		lsh.update(source.getBytes());
		digest = lsh.doFinal();

		for (int i = 0; i < byteSize; i++) {
		    hash.append(String.format("%02x", 0xff&digest[i]));
		}

		// 뒤에서 16자리를 인증 값으로 사용
		crypto = hash.toString().substring(hash.toString().length() - 16, hash.toString().length());    
		hash.setLength(0);
		hash = null;
		
		// TTS JSON 세팅 
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("deviceID", deviceId);                    	// 공통코드의 디바이스ID 정의 참조
		jsonObj.put("macAddress", macAddr);          	// 요청하는 클라이언트의 12자리 맥어드레스
		jsonObj.put("requestDateTime", ttsKeyMap.get("requestDateTime"));    	// getAccesVal 요청을 통해 응답받은 서버요청시간
		jsonObj.put("crypto", crypto);                      	// getAccesVal 요청을 통해 응답받은 값을 이용하여 생성한 인증 값
		jsonObj.put("text", text);                    	// WAV로 변환을 요청하는 문자열 
		jsonObj.put("language", 0);                         	// 공통코드의 언어코드 정의 참조    
		jsonObj.put("speaker", 2023);                      	 // 공통코드의 Speaker 타입코드 참조
		jsonObj.put("format", -1);                          	// will be defined    
		jsonObj.put("pitch", -1);                           	// 합성된 음성의 pitch(default: -1, 유효범위: 50~150)
		jsonObj.put("speed", -1);                           	// 합성된 음성의 속도(default: -1, 유효범위: 50~150)
		jsonObj.put("volume", -1);                          	// 합성된 음성의 볼륨(default: -1, 유효범위: 0~200)
		jsonObj.put("charSet", 0);                          	// 공통코드의 charSet 코드 참조
		jsonObj.put("appDomain", "mc.None");                	// Application 이름 + ‘.’ + Domain 코드 예. “mc.None”
		jsonObj.put("fileName", fileName);                        	// 전달 요청하는 파일이름. 예. yana_1803_001.wav, 한글불가
		jsonObj.put("localLog", "NoLog");                   	// 단말자체 처리한 내역 전송. 보낼 로그가 없을 경우 NoLog로 전송
		jsonObj.put("cacheYN", "Y");			// TTS요청에 대한 문장 캐싱 여부
		jsonObj.put("wavReturnYN", "Y");		// TTS요청에 대한 WAV 반환 여부
		jsonObj.put("requestSampleRate", "16000");		// 반환될 wav의 SampleRate 값
		jsonObj.put("mulawYn", "N");			//  반환될 wav의 인코딩을 mu-law로 요청할지에 대한 여부(Y/N)

		// 생성한 요청 파라메터를 Base64로 인코딩
		encQueryString = null;
		encoder = Base64.getEncoder();        
		encQueryString = new String(encoder.encode(jsonObj.toJSONString().getBytes(StandardCharsets.UTF_8)));
		jsonObj.clear();
		jsonObj = null;

		/**
		 * [3단계] TTS 요청 및 WAV 수신
		 */
		conn = null;
		OutputStreamWriter outputStream = null;    
		InputStream is = null;
		FileOutputStream out = null;
		try {
			//test
//		    url = new URL("http://10.214.121.39:6791/tts/getTTS");
//		    url = new URL("http://10.190.234.111:6791/tts/getTTS");
			//vip
		    url = new URL("http://10.190.200.112:6791/tts/getTTS");
		    
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setDoOutput(true);
		    conn.setDoInput(true);            
		    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		    conn.setRequestProperty("Content-Length", String.valueOf(("query=" + encQueryString).getBytes().length));
		    conn.setRequestMethod("POST");
		                    
		    outputStream = new OutputStreamWriter(conn.getOutputStream());
		    outputStream.write("query=" + encQueryString);    		// Base64로 인코딩된 json형태의 요청 정보를 전송
		    outputStream.flush();     
		    
		    if(outputStream!=null) {
		    	try {
		    		 outputStream.close();
				} catch (IOException e) {
					logger.error("error",e);
				}
		    	 outputStream = null;
		    }
		    int status = conn.getResponseCode();
		    if (status == HttpURLConnection.HTTP_OK) {
		        
		        // 요청한 TTS에 대한 WAV가 http protocol의 body를 통해 전달되기 때문에 
		        // 응답결과코드는 http header를 통하여 전달된다. 
		        resultCode = conn.getHeaderField("resultCode");
		        resultMessage = conn.getHeaderField("resultMessage");
		        // 결과가 성공인 경우 banary로 wav파일을 수신한다.
		        if ("0".equals(resultCode)) {        
		            byteArrayOutputStream = new ByteArrayOutputStream(); 
		            byte[] buf = new byte[1014];
		            int size;
		            is = conn.getInputStream();
		            
		            while ((size = is.read(buf)) != -1) {
		                byteArrayOutputStream.write(buf, 0, size);
		            }            
		            wav = byteArrayOutputStream.toByteArray();     // TTS요청에 대한  wav 수신 완료
		            
		            if(is!=null) {
		            	try {
		            		is.close();
						} catch (IOException e) {
							logger.error("error",e);
							throw new IOException();
						}
		            }
		        }
		    }                
		    File outFile = new File(filePath+fileName);
		    out = new FileOutputStream(outFile);
		    out.write(wav);
		    ttsKeyMap.put("fileName",fileName);
		    ttsKeyMap.put("filePath",filePath);
		
		} catch(MalformedURLException e1) {
			logger.error("error",e1);
		} catch (IOException e2) {
			logger.error("error",e2);
		} finally {
		    if (conn != null) {
		     conn.disconnect();
		    }
		    if(outputStream!=null) {
		    	try {
		    		 outputStream.close();
				} catch (IOException e) {
					logger.error("error",e.getMessage());
				}
		    	outputStream = null;
		    }
            if(is!=null) {
            	try {
            		is.close();
				} catch (IOException e) {
					logger.error("error",e.getMessage());
				}
            }
            if(out!=null) {
            	try {
            		out.close();
            	} catch (IOException e) {
            		logger.error("error",e.getMessage());
            	}
            }
		    
		    
		    conn = null;    
		}
		encQueryString = null;    
		url = null;
		
		return ttsKeyMap;
		
	}
	
	
	
	
	//TTS 인증키 얻어오기
	public static HashMap<String,String> TTSKey(String deviceId, String macAddr){
		return new HashMap<String, String>();
	}

	//나의 MacAddress 주소 얻어오기 
	public static String MacAddr() {
		String macAddr = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			for(int i = 0; i< mac.length; i++) {
				macAddr += String.format("%02X%s", mac[i],(i<mac.length -1)?"-":"");
			}
			macAddr = macAddr.replaceAll("-", "");
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("error",e);
		}
		return macAddr;
	}
	
	public static List<String> ttsTextList(String ttsText){
		ttsText = ttsText.replaceAll("\"", "");
		try {
			ttsText = ttsText.replaceAll("(^\\p{Z}+|\\p{Z}+$)", " ");
		}catch (Exception e) {
			logger.error("error",e);
		}
		String tmpTextArr [] = null; // . 와 ? 로 짜른 텍스트 배열 
		List<String> returnTxtTArr = new ArrayList<>();	// 텍스트를 다시 담은 배열 
		List<String> textArr = new ArrayList<>();	// 텍스트를 다시 담은 배열 
		String checkText = ""; // 자른 텍스트를 담아둘 변수 
		String checked = "";	// 구분값 체크 변수 
		String tmpText = "";	// 자른구분값을 리턴할 리스트에 담을 변수 
		String nextA = "";		// 다음리스트 구분값 체크 변수 
		String nextB = "";		// 다음리스트 구분값 체크 변수2 

		tmpTextArr = ttsText.split("\\.|\\?");
		if(ttsText.length() < 30) {							//텍스트 길이가 자를필요없을경우 
			returnTxtTArr.add(ttsText);
		}else {
			for(int i = 0 ; i< tmpTextArr.length; i++) {	//스플릿으로 자른 수만큼 반복문 
				checkText += tmpTextArr[i];					// 구분값을 구하기 위한 변수
				tmpText += tmpTextArr[i];	
				int t = checkText.length();
				if(t == ttsText.length()) {
					t = checkText.length();
					
				}else {
					t = checkText.length() +1;
				}
				checked = ttsText.substring(checkText.length(),t); // 맨마지막 구분값 	
				// 텍스트를 담기위한 변수 
//					checked = ttsText.substring(checkText.length(),checkText.length()+1); // 맨마지막 구분값 					

				if(checked.equals(".")) {					//마지막 구분값에 따라 뒤에 붙혀줌 
					checkText += ".";
					tmpText += ".";
				}else if(checked.equals("?")){
					checkText += "?";
					tmpText += "?";
				}
				if(tmpText.trim().length() > 0) {					//길이가 넘으면 리스트에 담아줌 
					textArr.add(tmpText);
					tmpText = "";
				}
			}
			
//			logger.info("중간과정 사이즈 : "+textArr.size());
			String realText = "";		//텍스트를 리스트에 담을 변수 
			for(int j = 0; j < textArr.size(); j++) {
				String string = textArr.get(j);
				if(string.equals(".") || string.equals("?")) {
					continue;
				}
				try {
					nextA = textArr.get(j).substring(textArr.get(j).length()-2,textArr.get(j).length()-1);
				}catch (Exception e) {
					logger.error("error",e);
				}
				if(nextA.matches(".*[0-9].*") || nextA.matches("^[a-zA-z]$")) {	//다음자리가 숫자인지 판단하여 뒤에 붙힐지 말지 결정 
					try {
						String string2 = textArr.get(j+1);
					}catch (IndexOutOfBoundsException e) {
						realText = textArr.get(j);
						returnTxtTArr.add(realText);
						continue;
					}
					realText = textArr.get(j) + textArr.get(j+1);
					try {
						nextB = textArr.get(j+1).substring(textArr.get(j+1).length()-2,textArr.get(j+1).length()-1);
					}catch (Exception e) {
						logger.error("error",e);
					}
					boolean flag = true;
					if(nextB.matches(".*[0-9].*") || nextB.matches("^[a-zA-z]$")) {flag = true;}else {flag =false;}
					while(flag) {
						try {
							nextB = textArr.get(j+1).substring(textArr.get(j+1).length()-2,textArr.get(j+1).length()-1);
						}catch (Exception e) {
							logger.error("error",e);
						}
						if(nextB.matches(".*[0-9].*") || nextB.matches("^[a-zA-z]$")) {	//다음자리가 숫자인지 판단하여 뒤에 붙힐지 말지 결정 
							if(j+2 >textArr.size()-1) {
								realText += textArr.get(j+1);							
								j++;
							}else {
								realText += textArr.get(j+2);
								j++;
							}
						}else {flag = false;}
					}
					j++;
					//현재 j=4 이고 textArr의 j+2 = 6은 존재하지않음.
				}else {
					realText = textArr.get(j);
				}
				returnTxtTArr.add(realText.strip());
			}
		}
		return returnTxtTArr;
	}
	public static boolean createTTS2(String text,String fileName,String windowFilePath) throws Exception {
//		logger.info(windowFilePath);
		HashMap<String,String> result = new HashMap<>();
		HttpURLConnection conn = null;
		ByteArrayOutputStream byteArrayOutputStream = null;

		URL url = null;
		String resultCode = null;                // 요청한 getAcceVal에 대한 응답코드를 저장하기 위한 변수
		String resultMessage = null;           // 요청한 getAcceVal에 대한 응답메시지를 저장하기 위한 변수
//		String acceVal = null;                    // 요청한 getAcceVal에 대한 인증키를 저장하기 위한 변수
//		String requestDateTime = null;        // 요청한 getAcceVal에 대한 서버시간을 저장하기 위한 변수(yyyyMMddHHmmssSSS)
		byte[] wav = null;                              		// 요청한 getTTS에 대한 WAV를 저장하기 위한 변수
		String crypto = null;  
		String encQueryString = null;
		Encoder encoder = Base64.getEncoder();
		
		
		String deviceId = "9801";
		String macAddr = MacAddr();
		String filePath = windowFilePath;
		
//		HashMap<String,String> ttsKeyMap = TTSKey(deviceId,macAddr);
		HashMap<String,String> ttsKeyMap = new HashMap<String, String>();
		
		
//		logger.info(ttsKeyMap.get("acceVal"));
//		logger.info(ttsKeyMap.get("requestDateTime"));
//		logger.info(ttsKeyMap.get("resultCode"));
//		
//		if(!"0".equals(ttsKeyMap.get("resultCode"))) {
//			result.put("result", "fail");
//			return false;	//2021 11 03 14:51 장진호 수정
//		}
		
		
		Date requestDate = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA); 
		try {
//		    requestDate = sdf.parse(ttsKeyMap.get("requestDateTime"));    // 1단계에서 서버로부터 응답받은 String타입의 서버요청시간을 입력
		    fileName+=".wav";
		} catch (Exception e) {
			logger.error("error",e);
			logger.info(e.getMessage());
		}

		
		crypto = "123456789absdfgh";
		// TTS JSON 세팅 
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("deviceID", deviceId);                    	// 공통코드의 디바이스ID 정의 참조
		jsonObj.put("macAddress", macAddr);          	// 요청하는 클라이언트의 12자리 맥어드레스
		jsonObj.put("requestDateTime", sdf.format(date));    	// getAccesVal 요청을 통해 응답받은 서버요청시간
		jsonObj.put("crypto", crypto);                      	// getAccesVal 요청을 통해 응답받은 값을 이용하여 생성한 인증 값
		jsonObj.put("text", text);                    	// WAV로 변환을 요청하는 문자열 
		jsonObj.put("language", 0);                         	// 공통코드의 언어코드 정의 참조    
		jsonObj.put("speaker", 2023);                      	 // 공통코드의 Speaker 타입코드 참조
		jsonObj.put("format", -1);                          	// will be defined    
		jsonObj.put("pitch", -1);                           	// 합성된 음성의 pitch(default: -1, 유효범위: 50~150)
		jsonObj.put("speed", -1);                           	// 합성된 음성의 속도(default: -1, 유효범위: 50~150)
		jsonObj.put("volume", -1);                          	// 합성된 음성의 볼륨(default: -1, 유효범위: 0~200)
		jsonObj.put("charSet", 0);                          	// 공통코드의 charSet 코드 참조
		jsonObj.put("appDomain", "mc.None");                	// Application 이름 + ‘.’ + Domain 코드 예. “mc.None”
		jsonObj.put("fileName", fileName);                        	// 전달 요청하는 파일이름. 예. yana_1803_001.wav, 한글불가
		jsonObj.put("localLog", "NoLog");                   	// 단말자체 처리한 내역 전송. 보낼 로그가 없을 경우 NoLog로 전송
		jsonObj.put("cacheYN", "Y");			// TTS요청에 대한 문장 캐싱 여부
		jsonObj.put("wavReturnYN", "Y");		// TTS요청에 대한 WAV 반환 여부
		jsonObj.put("requestSampleRate", "16000");		// 반환될 wav의 SampleRate 값
		jsonObj.put("mulawYn", "N");			//  반환될 wav의 인코딩을 mu-law로 요청할지에 대한 여부(Y/N)

		// 생성한 요청 파라메터를 Base64로 인코딩
		encQueryString = null;
		encoder = Base64.getEncoder();        
		encQueryString = new String(encoder.encode(jsonObj.toJSONString().getBytes(StandardCharsets.UTF_8)));
		jsonObj.clear();
		jsonObj = null;

		/**
		 * [3단계] TTS 요청 및 WAV 수신
		 */
		conn = null;
		OutputStreamWriter outputStream = null;    
		FileOutputStream out = null;
		InputStream in = null;
		try {
			//테스트
//		    url = new URL("http://10.214.121.39:6791/tts/getTTS");
//			url = new URL("http://10.190.234.111:6791/tts/getTTS");	
//			운영VIP
		    url = new URL("http://10.190.200.112:6791/tts/getTTS");
		    System.out.println("tts request URL : "+url.toString());
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setDoOutput(true);
		    conn.setDoInput(true);            
		    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		    conn.setRequestProperty("Content-Length", String.valueOf(("query=" + encQueryString).getBytes().length));
		    conn.setRequestMethod("POST");
		    outputStream = new OutputStreamWriter(conn.getOutputStream());
		    outputStream.write("query=" + encQueryString);    		// Base64로 인코딩된 json형태의 요청 정보를 전송
		    outputStream.flush();           
		        
		    int status = conn.getResponseCode();
		    if (status == HttpURLConnection.HTTP_OK) {
		        // 요청한 TTS에 대한 WAV가 http protocol의 body를 통해 전달되기 때문에 
		        // 응답결과코드는 http header를 통하여 전달된다. 
		        resultCode = conn.getHeaderField("resultCode");
		        resultMessage = conn.getHeaderField("resultMessage");
		        logger.info("resultCode : "+resultCode);
		        logger.info("resultMessage : "+resultMessage);

		        in = conn.getInputStream();
		        
		        // 결과가 성공인 경우 banary로 wav파일을 수신한다.
		        if ("0".equals(resultCode)) {        
		            byteArrayOutputStream = new ByteArrayOutputStream(); 
		            byte[] buf = new byte[1014];
		            int size;
		            while ((size = in.read(buf)) != -1) {
		                byteArrayOutputStream.write(buf, 0, size);
		            }
		            	            
		            wav = byteArrayOutputStream.toByteArray();// TTS요청에 대한  wav 수신 완료
		            byteArrayOutputStream.flush();
		        }		
		    }       
		
		    String resultPath = TTSUtil.getrValidPath(filePath+fileName);
		    
		    File outFile = new File(resultPath);
		    out = new FileOutputStream(outFile);
		    out.write(wav);
		    out.flush();
		    ttsKeyMap.put("fileName",fileName);
		    ttsKeyMap.put("filePath",filePath);
		
		} catch(MalformedURLException e1) {
			logger.info("error : " + e1.getMessage());
		} catch (IOException e2) {
			logger.info("error : " + e2.getMessage());
		} finally {
			if(conn!=null) {
				try {
					conn.disconnect();
				}catch (Exception e) {
					logger.error("error",e.getMessage());
				}
			}
	    	if(outputStream != null) {
	    		try {
	    			outputStream.close();
	    		}catch(Exception e) {
	    			logger.error("error",e.getMessage());
	    		}
	    	}
	    	if(byteArrayOutputStream != null) {
	    		try {
	    			byteArrayOutputStream.close();
	    		}catch(Exception e) {
	    			logger.error("error",e.getMessage());
	    		}
	    	}
	    	if(out != null) {
	    		try {
	    			out.close();
	    		}catch(Exception e) {
	    			logger.error("error",e.getMessage());
	    		}
	    	}
	    	if(in != null) {
	    		try {
	    			in.close();
	    		}catch (Exception e) {
	    			logger.error("error",e.getMessage());
				}
	    	}
	    	
		}
		encQueryString = null;    
		url = null;
		
		return true;
		
	}
	
	public static boolean createTTSFileDirectoryFromProductCode(String createProductTTSFilePath) {
		File directory = new File(createProductTTSFilePath);
		
		if(directory.mkdir()) {
			return true;
		}else {
			return false;			
		}
	}
	
	public static String replaceUtil(String prefix , String sufix , String text) {
		String replace = text.replace(prefix, "");
		String replace2 = replace.replace(sufix, "");
		return replace2;
	}
	
	//대면녹취에서사용
	public static String randomRemove(String text,List<scriptProductValueInfo> asList ) {
		String tt = text;
		List<scriptProductValueInfo> list = new ArrayList<scriptProductValueInfo>();
		for (scriptProductValueInfo r : asList) {	
			if(tt.contains(r.getRsProductValueCode())) {
				list.add(r);
			}
		}
		String ttt = tt;
		for (scriptProductValueInfo info : list) {
			int strLeng = info.getRsProductValueCode().length();
			int indexOf = text.indexOf(info.getRsProductValueCode());
			String prefix = ""+text.charAt(indexOf-1);
			String sufix = ""+text.charAt(indexOf+strLeng);
			ttt = ttt.replace("{"+info.getRsProductValueCode()+"}", info.getRsProductValueVal()+" ");
		}


		return ttt;
	}
	
	
	public static String randomRemove3(String text,List<scriptProductValueInfo> asList ) {
		String tt = text;
		List<scriptProductValueInfo> list = new ArrayList<scriptProductValueInfo>();
		for (scriptProductValueInfo r : asList) {	
			String compare ="{"+r.getRsProductValueName()+"}";
			if(tt.contains(compare)) {
				list.add(r);
			}
		}
		String ttt = tt;
		for (scriptProductValueInfo info : list) {
			String replaceText = ttt.replace("{"+info.getRsProductValueName()+"}", info.getRsProductValueVal());
			ttt = replaceText;
		}

		return ttt;
	}
	
	
	public static String randomRemove2(String text,List<scriptProductValueInfo> asList ) {
		String tt = text;
		List<scriptProductValueInfo> list = new ArrayList<scriptProductValueInfo>();
		for (scriptProductValueInfo r : asList) {	
			if(tt.contains(r.getRsProductValueCode())) {
				list.add(r);
			}
		}
		String ttt = tt;
		try {
			for (scriptProductValueInfo info : list) {
				int strLeng = info.getRsProductValueCode().length();
				int indexOf = text.indexOf(info.getRsProductValueCode());
				String prefix = ""+text.charAt(indexOf-1);
				String sufix = ""+text.charAt(indexOf+strLeng);
				String replaceText = ttt.replaceAll(info.getRsProductValueCode(),info.getRsProductValueName());
				ttt = replaceText;
			}
		}catch (NullPointerException e) {
			logger.info("no variable retun : "+ttt);
		}
		
		return ttt;
	}
	

	public static String specialTextParse(String text) {
		String resultText = text;
		StringBuffer buf = new StringBuffer();
		List<String> strList = new ArrayList<String>();
		List<Integer> intList = new ArrayList<Integer>();
			for (int j = 0; j < code.length; j++) {
   				String str = String.valueOf((char)code[j]);
 				if(text.contains(str)) {
					if(str.equals("-")) {
						int size = text.trim().split(str).length;
						//size = 2  -> 1개 들어감   size = 3 --> 2개들어감
						while(size>1) {
							int indexOftrim = text.replaceAll(" ", "").indexOf(str);
							int indexOf = text.indexOf(str);
							
							String prefix = String.valueOf(text.replaceAll(" ", "").charAt(indexOftrim-1));
							String sufix = String.valueOf(text.replaceAll(" ", "").charAt(indexOftrim+1));
							if(!prefix.matches(".*[0-9].*") && !prefix.matches("^[a-zA-z]$") ) {
								if(!sufix.matches(".*[0-9].*") && !sufix.matches("^[a-zA-z]$") ) {
									String substring = text.substring(0,indexOf);
									String end = text.substring(indexOf+1);
									text = substring+value[j]+end;
									size--;
								}else {
									buf.append(text.subSequence(0, indexOf+1));
									text = text.substring(indexOf+1);
									size--;
								}
							}else {
								buf.append(text.subSequence(0, indexOf+1));
								text = text.substring(indexOf+1);
								size--;
								
							}
						}
						continue;
					}	
					strList.add(str);
					intList.add(j);
				}
				
			}
			text = buf.toString()+text;
			if(strList.size() == 0 && intList.size() == 0) {
				return text;
			}
			for(int z=0; z<intList.size();z++) {
				Integer integer = intList.get(z);
				text = text.replace(strList.get(z), value[integer]);
			}
	
		return text;
	}
	public static String textOneChange(String text) {
		if(text.contains("bp")) {
			text = text.replace("bp", " bp ");
		}		
		return text;
	}
	
	
	public static String dotChangeUtil(String text) {
		boolean flag =false;
		StringBuffer buf = new StringBuffer();
		int count = dotCount(text);
		if(count != 0) {				
			for (int i = 0; i < count; i++) {
				if(!dotCheck(text)) {
					buf.append(text);
					break;
				}
				int idx = dotIndexCheck(text);
				String subStr = "";
				if(idx != -1) {
					if(idx==text.length()-1) {
						subStr = text;
						flag = true;
					}else {
						subStr = text.substring(0,idx+1);					
					}
				}
				String frontSS = "";
				String frontS = "";
				frontS = String.valueOf(text.charAt(idx-1));
				try {
					frontSS = String.valueOf(text.charAt(idx-2));
				}catch(StringIndexOutOfBoundsException e) {
					frontSS = "";
				}
				String backS = "";
				if(flag) {
					backS = String.valueOf(text.charAt(idx));
				}else {
					backS = String.valueOf(text.charAt(idx+1));				
				}
				
				if(frontS.matches(".*[0-9].*")) {
					if(backS.matches(".*[0-9].*")) {
						buf.append(subStr);
						text = text.substring(idx+1); //추후 해야할것
						
					}
					if(backS.equals(" ") || backS.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣].*") || backS.matches(".*[a-zA-Z].*") ) {
						if(frontSS != null) {
							if(frontSS.matches(".*[0-9].*")) {
								subStr = subStr.replace(frontS+".", frontS+". ");							
							}else {
								subStr = subStr.replace(frontS+".", "<pause='300'>"+frontS+"<pause='600'>");							
							}
						}
						if(frontSS == null) {
							subStr = subStr.replace(frontS+".", frontS+"<pause='600'>");		
						}
						buf.append(subStr);
						text = text.substring(idx+1); //추후 해야할것				
					}
				}else {
					buf.append(subStr);
					text = text.substring(idx+1); //추후 해야할것
					
				}
			}
		}else {
			buf.append(text);
		}
		return buf.toString();

	}
	
	public static boolean dotCheck(String text) {
		if(text.contains(".")) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public static int dotIndexCheck(String text) {
		boolean b = dotCheck(text);
		if(b) {
			int idx = text.indexOf(".");
			return idx;
		}
		return -1;
	}
	
	public static int dotCount(String text) {
		String[] split = text.split("\\.");
		if(split.length == 1) {
			return 0;
		}
		return split.length;
		}
	
	
	public static String ad047Check(String ad047) throws Exception {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		Date ad047Date = null;
		try {
			ad047Date = sdf.parse(ad047);
		} catch (ParseException e) {
			logger.info("date parsing error");
		}
		long baseDay = 24 * 60 * 60 *1000;
		long baseMonth = baseDay * 30;
		long baseYear = baseMonth * 12;
		if(ad047Date == null) {
			throw new Exception();
		}
		long calDate = date.getTime() - ad047Date.getTime();
		//차이년
		long diffYear = calDate / baseYear;

		if(diffYear>3) {
			logger.info("3년 초과");
			result = "1";
//			scriptRegistrationInfo.setAd047("1");
		}else if(diffYear<=3) {
			logger.info("3년 이내");
			result = "0";
//			scriptRegistrationInfo.setAd047("0");
		}	
		return result;
	}
	
	public static String StringToNumberLine(int num) {
		switch (num) {
		case 1:
			return "첫번째";
		case 2:
			return "두번째";
		case 3:
			return "세번째";
		}
		return "마지막으로";
		
	}




	public static List<scriptProductValueInfo> parseValue(List<scriptProductValueInfo> valueList) {
		for (scriptProductValueInfo info : valueList) {
			String valueName = info.getRsProductValueCode();
			try {
				String percentValue = parsingValueCheck(valueName,(info.getRsProductValueVal()));
				info.setRsProductValueVal(percentValue);
			}catch (Exception e) {
				logger.error("error",e);
			}
			
		}
		
		
		return valueList;
	}
	
	private static String percentParse(String value) throws Exception {
		DecimalFormat dcl = new DecimalFormat("#.####"); 
		return dcl.format(Double.parseDouble(value))+"%";
	}
	
	public static String parsingValueCheck(String valueName,String value) throws Exception {
		switch (valueName) {
		case "ISA_FEES":
		case "ASSETS_RATIO":
		case "FD_RATIO":
		case "BNK_COM":
		case "FND_POCP_FEE_RT":
		case "TOT_PFE_RT":
		case "SUM_PFE_RT":
		case "FNDR_FEE_RT_1":
		case "FNDR_FEE_RT_2":
		case "POCP_TRTPY_RT":
		case "DFAN_TRTPY_RT":
		case "YTM1":
		case "ELF_TOT_REMUN_RT":
			value = percentParse(value);
			break;
		case "LATT_APL_TM":
			value = timeParse(value);
			break;
		case "AN013":
		case "AN017":
		case "FNDBP_APL_DCNT_1" :
		case "FNDBP_APL_DCNT_2" :
			value = intParse(value);
			break;
		case "AD047":
			value = dateParse(value);
			break;
//		case "LZR_SQC_CN":
//			value = lzrParse(value);
		default:
			break;
		}
		return value;
	}

	//리자드차수 P
	private static String lzrParse(String value) throws Exception {
		String forText = "차";
		String[] splitValue = value.split("\\^");
		
		List<String> valueSplit = Arrays.asList(splitValue);
		
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<valueSplit.size(); i++) {
			String t = valueSplit.get(i);
			if(i == valueSplit.size()-1) {
				buf.append(t+forText);
				continue;
			}
			buf.append(t+forText+",");
		}
		return buf.toString();
	}




	private static String dateParse(String value) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일");
		Date date = null;
		date = sdf.parse(value);
		return sdf2.format(date);
	}
	
	private static String timeParse(String value) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date parseDate = sdf.parse(value);
		
		String formatter = "";
		if(parseDate.getMinutes() == 0) {
			formatter= "a h시";
		}else {
			formatter= "a h시mm분";
		}
		String time = new SimpleDateFormat(formatter).format(parseDate);
		return time;
	}
	
	private static String intParse(String value) {
		try {
			value = String.valueOf(Integer.parseInt(value));
		}catch (Exception e) {
			return value;
		}
		return value;
	}
	
	public static String getrValidPath(String path) throws Exception {
		if(path == null || "".equals(path)) {
			throw new Exception("path is null");
		}
		
		if(path.indexOf("..") > -1) {
			throw new Exception("path contains ..");
		}
		if(path.indexOf("//") > -1) {
			throw new Exception("path contains //");
		}
		return path;
	}
}
