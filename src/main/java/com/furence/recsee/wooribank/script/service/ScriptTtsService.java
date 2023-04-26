package com.furence.recsee.wooribank.script.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.util.DateUtil;
import com.furence.recsee.common.util.DirectoryUtil;
import com.furence.recsee.common.util.OsUtil;
import com.furence.recsee.common.util.TTSUtil;
import com.furence.recsee.wooribank.script.param.request.MakeTTSParam;
import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.param.request.TTSParam;
import com.furence.recsee.wooribank.script.repository.dao.ProductDao;
import com.furence.recsee.wooribank.script.repository.entity.ProductScriptVariable;
import com.furence.recsee.wooribank.script.util.NetworkUtil;
import com.furence.recsee.wooribank.script.util.webclient.WebClient;

@Service
public class ScriptTtsService {

	private static final Logger logger = LoggerFactory.getLogger(ScriptTtsService.class);
	
	@Value("#{scriptManageProperties['tts.server.url']}")
	private String[] ttsServerUrl;
	
	@Value("#{scriptManageProperties['tts.server.url.path.make']}")
	private String ttsServerMakePath;
	
	@Value("#{scriptManageProperties['tts.server.url.path.ping']}")
	private String ttsServerPingPath;
	
	@Value("#{scriptManageProperties['tts.prelisten.url']}")
	private String ttsPrelistenUrl;
	
	@Value("#{ttsProperties['server.ip']}")
	private String ttsServerIp;
	
	@Value("#{ttsProperties['path.windowHistoryPath']}")
	private String windowHistoryPath;

	@Value("#{ttsProperties['path.linuxHistoryPath']}")
	private String linuxHistoryPath;

	@Value("#{ttsProperties['path.windowRealTimeTTSPath']}")
	private String windowRealTTSPath;

	@Value("#{ttsProperties['path.linuxRealTimeTTSPath']}")
	private String linuxRealTTSPath;
	
	private final static String TTS_EXT = "wav";
	
	private ProductDao productDao;
	
	@Autowired
	public ScriptTtsService(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	/**
	 * TTS 실시간 미리듣기 처리
	 * @param param
	 * @return
	 */
	public String requestRealtiemTTS(TTSParam.Prelisten param) {
		
		String ttsText = generateTextForTTS(param);		
		String tempPath = downloadPath();		
		DirectoryUtil.mkdirDirectory(new File(tempPath));		
		String fileName = getTempFilePath(tempPath);
		
		String ttsUrl = null;
		// TTS 생성 요청
		try {
			
			String localPath = makeAudioFile( ttsText, fileName, tempPath);
			ttsUrl = "https://" + ttsServerIp + ":28881/listen?url=" + localPath;
		} catch (Exception e) {
			logger.error("error",e);
		}
		
		return ttsUrl;
	}
	
	/*
	 *
	 */
	private String getTempFilePath(String tempPath) {
		String dateFormat = DateUtil.toString(new Date());
		String fileName = dateFormat + "_temp";
		
		String resultTempPath = "http://"+NetworkUtil.getIpAddress()+":28881/"
				+ "listen?url="+tempPath + fileName + ".wav";
		logger.info("TTS Path:" + resultTempPath);
		return fileName;
	}
	
	/**
	 * TTS용 텍스트
	 * @param param
	 * @return
	 */
	private String generateTextForTTS(TTSParam.Prelisten param) {
		List<ProductScriptVariable> varList = getTTSFileWithScriptDetail(param);
		varList = Optional.ofNullable(varList).orElse(Collections.emptyList());
		
		String ttsText = generateTTSSource(param.getText(), varList);
		return ttsText;
	}

	/**
	 * OS별 다운로드 경로 
	 * @return
	 */
	private String downloadPath() {
		String tempPath = OsUtil.getInstance().OsCheck().contains("win") 
				? this.windowRealTTSPath 
				: this.linuxRealTTSPath;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);		
		String format = sdf.format(new Date()) + "/";
		
		return tempPath += format;
	}
	
	/**
	 * TTS 생성 요청전 health 체크
	 * @return
	 */
	public boolean healthCheckTTSServer() {
		
		int succCount = 0;
		for(String url : this.ttsServerUrl) {
			String pingurl = url + this.ttsServerPingPath;
			
			try {
				String pingResult = WebClient.<Object,String>builder(pingurl, HttpMethod.GET)
						.reseponseType(String.class)
						.build()
						.reqeust();
				if( pingResult.equals("OK") ) succCount++;
			} catch (Exception e) {
				logger.error("error",e);
			}
		}		
		return succCount > 0 ;
	}
	
	/**
	 * TTS 생성 요청
	 */
	/**
	 * async http 요청
	 * @param url
	 * @param ttsParam
	 * @throws Exception
	 */	
	public void requestMakeTTS(MakeTTSParam  ttsParam) throws Exception {
		
		for(String url : this.ttsServerUrl) {
			String ttsUrl = url + this.ttsServerMakePath;
			WebClient.<String, String>builder(ttsUrl, HttpMethod.POST)
				.payload(ttsParam.toJsonString())
				.contentType("application/json;charset=UTF-8")
				.reseponseType(String.class)
				.succesCallback( (s) -> {
					AJaxResVO resVo;
					try {
						ObjectMapper mapper = new ObjectMapper();
						resVo = mapper.readValue(s, AJaxResVO.class);
						logger.info("MakeTTS result:{}", resVo.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}				
					
				})
				.failCallback( (status) -> { 
					logger.info("MakeTTS result:{}", status); 
				})
				.build()
				.reqeust();
		}
	}
	
	/**
	 * 상품별 스크립트용 가변데이터 조회
	 * @return
	 */
	private List<ProductScriptVariable> getScriptVariableOfProduct(ProductParam.ScriptVariable param) {
		return this.productDao.selectProductVariables(param);
	}
	
	/**
	 * 
	 * @param content - TTS 미리듣기할 내용
	 * @param fileName - 파일이름
	 * @param localPath - 로컬 저장경로
	 */
	private List<ProductScriptVariable> getTTSFileWithScriptDetail(TTSParam.Prelisten param)  {
		
		ProductParam.ScriptVariable varParam = 
				new ProductParam.ScriptVariable(param.getProductPk(), param.getProductType());	
		
		return Optional.ofNullable(getScriptVariableOfProduct(varParam))
				.orElse(Collections.emptyList());
	}
	
	/**
	 * 가변텍스트 치환 함수
	 * @param script
	 * @param findPrefix
	 * @param findSuffix
	 * @param map
	 * @return
	 */
	private String translateText(
			String script,	
			String findPrefix,
			String findSuffix,
			Map<String, String> codeValueMap ) {
		
		
		Iterator<Entry<String, String>> iter = 
				codeValueMap.entrySet().iterator();
		
		while(iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			script = script.replace(
					findPrefix+entry.getKey() +findSuffix, 
					entry.getValue());
		}
		
		return script;
	}
	
	/**
	 * 조회한 상품 가변값 정보를 기준으로 현재 스크립트의 가변값을 치환
	 * @param text
	 * @param varList
	 * @return
	 */
	private String generateTTSSource(
			String text, List<ProductScriptVariable> varList) {
		
		Map<String, String> map = 
			varList.stream()
			.collect(
				Collectors.toMap(
					ProductScriptVariable::getVariableCode,
					ProductScriptVariable::getVariableValue,
					(x,y) -> y,
					LinkedHashMap::new
				)
			);
		
		return generateTTSSource (translateText(text, "{" , "}" , map));
		
	}
	
	/**
	 * TTS가 처리할 수 있도록 전처리작
	 * @param text
	 * @return
	 */
	private String generateTTSSource(String text) {
		
		String returnText = TTSUtil.specialTextParse(text);
		returnText = TTSUtil.dotChangeUtil(returnText);
		returnText = TTSUtil.textOneChange(returnText);		
		return returnText;
	}
	
	
	
	/**
	 * TTS서버에 음성합성 요청한후 , 성공시 TTS wave 파일 저장 
	 * @param script
	 * @param fileName
	 * @param tempPath
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private String makeAudioFile(String script, String fileName, String tempPath) 
			throws JsonMappingException, IOException {

		String deviceId = "9801";
		String macAddr = NetworkUtil.getMacAddress();
		String crypto = "123456789absdfgh";
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
		
		TTSParam.Make makeParam = TTSParam.Make.builder()
				.deviceID(deviceId)
				.macAddress(macAddr)
				.requestDateTime(sdf.format(date))
				.crypto(crypto)
				.text(script)
				.appDomain("wooribank.script.recsee.com")
				.fileName( fileName + "."+ TTS_EXT) 
				.requestSampleRate("16000")
				.build();
		
		String queryString = makeParam.base64encode();
		String url = this.ttsPrelistenUrl;
		String contentType = "application/x-www-form-urlencoded;charset=UTF-8";
		
		File file = ttsFileDownload(url, contentType, "query="+queryString);
		return file == null ? null : file.getPath();
	}
	
	/**
	 * 
	 * @param url
	 * @param contentType
	 * @param payload
	 * @return
	 */
	private File ttsFileDownload(String url, String contentType ,String payload) {
		
		File file = null;
		try {
			file = WebClient.builder(url, HttpMethod.POST)
				.contentType(contentType)
				.payload(payload)
				.build()
				.download();
		} catch (Exception e) {
			logger.error("error",e);
		}
		
		return file;
	}

}
