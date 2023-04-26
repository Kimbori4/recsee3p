package com.furence.recsee.wooribank.script.repository.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductGroup {
	
	/**
	 * 상품 PK
	 */
	@JsonProperty("rsProductListPk")
	private String productPk;
	
	/**
	 * 상품타입
	 */
	@JsonProperty("rsProductType")
	private String productType;
	
	/**
	 * 상품타입이름
	 */
	@JsonProperty("rsProductTypeName")
	private String productTypeName;
	
	/**
	 * 상품코드
	 */
	@JsonProperty("rsProductCode")
	private String productCode;
	
	/**
	 * 상품명
	 */
	@JsonProperty("rsProductName")
	private String productName;
	
	/**
	 * 그룹코드
	 */
	@JsonProperty("rsGroupCode")
	private String groupCode;
		
	/**
	 * 상품이름
	 */
	@JsonProperty("rsGroupName")
	private String groupName;
	
	/**
	 * 스크립트 참조용 pk
	 */
	@JsonProperty("rsScriptStepFk")
	private String scriptStepFk;
	
	/**
	 * ELT 상품 여부
	 */
	@JsonProperty("rsEltYN")
	private String eltYN;
	
	/**
	 * 그룹상품목록
	 */
	@JsonProperty("rsGroupProductList")
	private List<GroupSimpleInfo> groupProductList;

	
	@Getter
	@Setter
	@ToString
	public static class GroupSimpleInfo{
		
		/**
		 * 상품 코드
		 */
		@JsonInclude(Include.NON_EMPTY)
		@JsonProperty("rsGroupProductList")
		private String productCode;
		
		/**
		 * 상품 이름
		 */
		@JsonInclude(Include.NON_EMPTY)
		@JsonProperty("rsGroupProductList")
		private String productName;
	}
}
