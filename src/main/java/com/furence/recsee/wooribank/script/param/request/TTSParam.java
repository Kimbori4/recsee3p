package com.furence.recsee.wooribank.script.param.request;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class TTSParam{
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Prelisten {
		
		/**
		 * 상품 pk
		 */
		@JsonProperty("rsProductPk")
		private String productPk;
		
		/**
		 * 상품 타입
		 */
		@JsonProperty("rsProductType")
		private String productType;
		
		/**
		 * 스크립트 디테일 타입
		 */
		@JsonProperty("rsScriptDetailType")
		private String detailType;
		
		/**
		 * 스크립트 텍스트
		 */
		@JsonProperty("rsScriptDetailText")
		private String text;
		
	}
	
	
	@Builder
	@ToString
	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Make implements Serializable{
		
		private static final long serialVersionUID = 7389944693461940200L;
		
		private String deviceID;
		
		private String macAddress;
		
		private String requestDateTime;
		
		private String crypto;
		
		private String text;
		
		@Default
		private int language = 0 ;
		
		@Default
		private int speaker = 2023;
		
		@Default
		private int format = -1;
		
		@Default
		private int pitch = -1;
		
		@Default
		private int speed = -1;
		
		@Default
		private int volume = -1;
		
		@Default
		private int charSet = 0;	
		
		private String appDomain;
		
		private String fileName;
		
		@Default
		private String localLog = "NoLog";
		
		@Default
		private String cacheYN = "Y";
		
		@Default
		private String wavReturnYN = "Y";
		
		@Default
		private String requestSampleRate = "8000";
		
		@Default
		private String mulawYn = "N";
		
		public String base64encode() throws IOException, JsonMappingException {
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(this);
			return new String(Base64.getEncoder()
						.encode(jsonStr.getBytes(StandardCharsets.UTF_8)));			
		}
	}

	
}