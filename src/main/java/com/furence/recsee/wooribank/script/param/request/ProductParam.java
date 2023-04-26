package com.furence.recsee.wooribank.script.param.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.wooribank.script.annotation.Translate;
import com.furence.recsee.wooribank.script.annotation.Translate.TranslateTypes;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.param.request.base.Paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

public class ProductParam {
	
	@Getter
	@Setter
	@ToString(callSuper = true)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class Search extends Paging{

		
		/*
		 * 상품 타입 
		 * 1:펀드 2:신탁 3:투자성향조사 
		 */		
		private String productType;
		
		/**
		 * 검색 타입
		 * 1:전체 2:상품명 3:상품코드 4:그룹코드
		 */
		private String searchType;
			
		/**
		 * 검색어
		 */
		@Translate(TranslateTypes.HtmlEsacpe)
		private String keyword;
		
		/**
		 * 상품 테이블 PK
		 */
		private String productListPk;
		
		/**
		 * 사용유무
		 */
		private String useYN;
		
		/**
		 * 스크립트유무
		 */
		private String registeredYN;

		@Override
		public void setCount(int count) {
			super.setLimit(count);
		}
		
		@Override
		public void setPosStart(int posStart) {		
			super.setOffset(posStart);
		}
		
		@Override
		public int getCount() {
			return super.getLimit();
		}
		
		@Override
		public int getPosStart() {
			return getOffset();
		}
			
	}
	
	
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ScriptVariable {
		
		/**
		 * 상품 가변값 PK
		 */
		@JsonProperty("rsProductPk")
		private String productPk;

		/**
		 * 상품타입
		 */
		@JsonProperty("rsProductType")
		private String productType;
		
	}

	@Getter
	@Setter
	@ToString(callSuper = true)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class NotRegistered extends Paging{

		
		/*
		 * 상품 타입 
		 * 1:펀드 2:신탁 3:투자성향조사 
		 */
		private String productType;
		
		/**
		 * 검색 타입
		 * 1:전체 2:상품명 3:상품코드 4:그룹코드
		 */
		private String searchType = "1";
			
		/**
		 * 검색어
		 */
		private String keyword;
		
		/**
		 * 상품 테이블 PK
		 */
		private String productListPk;
		
		
		/**
		 * 상품 유형 이름
		 */
		@JsonProperty("rsProductTypeName")
		private String typeName;
		
		
		
		@Override
		public void setCount(int count) {
			super.setLimit(count);
		}
		
		@Override
		public void setPosStart(int posStart) {		
			super.setOffset(posStart);
		}
		
		@Override
		public int getCount() {
			return super.getLimit();
		}
		
		@Override
		public int getPosStart() {
			return getOffset();
		}
	}
	
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class Copy {

		@JsonProperty("rsSrcProductPk")
		private String  srcProductPk;
		
		@NonNull
		@Singular(ignoreNullCollections = true)
		private List<Integer> targetList;
	}
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class CopyApprove {

		@JsonProperty("srcProductPk")
		private String  srcProductPk;
		
		@NonNull
		@Singular(ignoreNullCollections = true)
		private List<Integer> targetList;
		
		/**
		 * 적용타입
		 */
		@JsonProperty("applyType")
		private Const.Apply applyType;
		
		/**
		 * 적용예정일
		 */
		@JsonProperty("applyDate")
		@JsonFormat(pattern = "yyyy-MM-dd")
		private String applyDate;
		
		/**
		 * 상신유저
		 */
		@JsonIgnore
		private String approveUser;		
	}
}
