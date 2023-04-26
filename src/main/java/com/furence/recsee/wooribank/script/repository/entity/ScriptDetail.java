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
public class ScriptDetail implements ScriptEntity {
	
	/**
	 * 스크립트 스텝PK
	 */
	@JsonProperty("rScriptStepPk")
	private String stepPk;

	/**
	 * 스크립트 상품 PK(그룹인 경우 그룹PK)
	 */
	@JsonProperty("rScriptStepFk")
	private String productPk;

	/**
	 * 스크립트 디테일 pk
	 */
	@JsonProperty("rScriptDetailPk")
	private String detailPk;

	/**
	 * 스크립트 디테일 코드
	 */
	@JsonProperty("rScriptDetailType")
	private String detailType;
	
	/**
	 * 스크립트 디테일 코드 이름
	 */
	@JsonProperty("rScriptDetailTypeName")
	private String detailTypeName;

	/**
	 * 공통스크립 여부
	 */
	@JsonProperty("rScriptDetailComKind")
	private String detailComYn;

	/**
	 * 스크립트 디테일 정렬
	 */
	@JsonProperty("rScriptDetailOrder")
	private String detailOrder;

	/**
	 * 스크립트 디테일 내용
	 */
	@JsonProperty("rScriptDetailText")
	private String detailText;

	/**
	 * 스크립트 디테일 가변값 분류
	 */
	@JsonProperty("rScriptDetailIfCase")
	private String useDetailOpionalYN;

	/**
	 * 스크립트 디테일 가변값
	 */
	@JsonProperty("rScriptDetailIfCaseDetail")
	private String detailOpionalValue;

	/**
	 * 실시간 TTS 여부
	 */
	@JsonProperty("rScriptDetailRealtimeTTS")
	private String detailRealtimeTTSYN;

	/**
	 * 최초 생성자
	 */
	@JsonProperty("rScriptDetailCreateDate")
	private String detailCreateUser;

	/**
	 * 최초 생성일
	 */
	@JsonProperty("rScriptDetailCreateUser")
	private String detailCreateDate;

	/**
	 * 최근 수정일
	 */
	@JsonProperty("rsScriptDetailUpateDate")
	private String detailUpateDate;

	/**
	 * 최근 수정자
	 */
	@JsonProperty("rScriptDetailUpdateUser")
	private String detailUpdateUser;

	/**
	 * 크립트 디테일 수정 결재여부
	 */
	@JsonProperty("rScriptDetailConfirm")
	private String detailConfirmYN;

	/**
	 * 스크립트 디테일 수정 결재일
	 */
	@JsonProperty("rScriptDetailConfirmDate")
	private String detailConfirmDate;

	/**
	 * 스크립트 디테일 수정 결재자
	 */
	@JsonProperty("rScriptDetailConfirmUser")
	private String detailConfirmUser;

	/**
	 * 공통스크립트 pk
	 */
	@JsonProperty("rScriptDetailComFk")
	private String detailComFk;

	/**
	 * 수정사항 적용 예약일
	 */
	@JsonProperty("rScriptDetailReservDate")
	private String detailReservDate;

	/**
	 * 공통스크립트 타입
	 */
	@JsonProperty("rScriptCommonType")
	private String commonType;
	
	/**
	 * 공통스크립트 타입
	 */
	@JsonProperty("rCommonConfirm")
	private String commonConfirmYn;

	/**
	 * 공통스크립트 타입 이름
	 */
	@JsonProperty("rScriptCommonTypeName")
	private String commonTypeNmae;
	
	/**
	 * 공통스크립트 pk
	 */
	@JsonProperty("rScriptCommonPk")
	private String commonPk;

	/**
	 * 공통스크립트 상세
	 */
	@JsonProperty("rScriptCommonDesc")
	private String commonDesc;

	/**
	 * 스크립트 디테일 가변값 분류코드
	 */
	@JsonProperty("rScriptDetailIfCaseCode")
	private String detailIfCaseCode;

	/**
	 * 스크립트 디테일 가변값 코드
	 */
	@JsonProperty("rScriptDetailIfCaseDetailCode")
	private String detailIfCaseDetailCode;

	/**
	 * 상품 class(c-p 클래스)
	 */
	@JsonProperty("rProductClass")
	private String productClass;

	/**
	 * 사용여부
	 */
	@JsonProperty("rUseYn")
	private String useYn;
	
	/**
	 * 스크립트 version
	 */
	@JsonProperty("rScriptVersion")
	private String scriptVersion;
	
	/**
	 * 그룹정보
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("groupInfo")
	private ProductGroup groupInfo;
	
	
	@JsonProperty("rProdutAttributes")
	private List<String> productAttributes;
	
}
