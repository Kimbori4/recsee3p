package com.furence.recsee.wooribank.script.util.file.pdf;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PDFHtmlConfig {
	
	
	/**
	 * 문서 여백 정보
	 */
	@Getter
	@Setter
	@AllArgsConstructor
	static class Padding {
		
		private float left;
		
		private float top;
		
		private float right;
		
		private float bottom;
	}
	
	/**
	 * 문서의 여백정보
	 */
	@Default
	private Padding padding = new Padding(50,50,50,50);
	
	/**
	 * css 파일 경로
	 */
	private String cssPath;
	
	/**
	 * Font 파일 경로
	 */
	
	private String fontPath;
	
}
