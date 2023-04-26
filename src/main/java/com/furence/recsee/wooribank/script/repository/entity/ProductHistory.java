package com.furence.recsee.wooribank.script.repository.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductHistory {
	
	/**
	 * 그룹 또는 개별상품의 rs_product_list_pk
	 */
	@JsonProperty("rsProductPk")
	private String productPk;
	
	/**
	 * 상품스크립트 수정일자
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("rsUpdateDate")
	private Date updateDate;
	
	/**
	 * 상품스크립트 버전
	 */
	@JsonProperty("rsScriptVersion")
	private String scriptVersion;
	
	/**
	 * 상품스크립트 이전 버전
	 */
	private String earlierVersion;
	
	/**
	 * 상품스크립트 수정자
	 */
	@JsonProperty("rsUpdateUser")
	private String updateUser;
	
	/**
	 * 상품스크립트 결재자
	 */
	@JsonProperty("rsApproveUser")
	private String approveUser;
	
	
}
