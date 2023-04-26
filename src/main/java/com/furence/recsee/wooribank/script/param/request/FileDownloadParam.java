package com.furence.recsee.wooribank.script.param.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.wooribank.script.param.request.base.FileParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class FileDownloadParam {
	
	/**
	 * 스크립트 버전별 가져오기 
	 * @author woori
	 *
	 */
	@Setter
	@Getter
	@ToString(callSuper = true)
	public static class VersionSnapshot extends FileParam{
		
		/**
		 * 상품 테이블 PK
		 */
		@JsonProperty("productPk")
		private String productPk;
		
		/**
		 * 상품 스크립트 버전
		 */
		@JsonProperty("scriptVersion")
		private String scriptVersion;		
		
	}
	
	
	/**
	 * 현재 상품의 스크립트 다운로드 요청 파라미터
	 * @author woori
	 *
	 */
	@Setter
	@Getter
	@ToString(callSuper = true)
	public static class ScriptCurrentInfo extends FileParam {
		
		/**
		 * 상품 테이블 PK
		 */
		@JsonProperty("productPk")
		private String productPk;
		
	}
	
	/**
	 * 녹취 파일의 스크립트 다운로드 요청 파라미터
	 * @author woori
	 *
	 */
	@Setter
	@Getter
	@ToString(callSuper = true)
	public static class ScriptCallInfo extends FileParam {
		
		/**
		 * 녹취 key
		 */
		@JsonProperty("callKey")
		private String callKey;
		/**
		 * 
		 */
		@JsonProperty("type")
		private int type;
		
		@JsonProperty("recType")
		private int recType = 1;
		
	}
}
