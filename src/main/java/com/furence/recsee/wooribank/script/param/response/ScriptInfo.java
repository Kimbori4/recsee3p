package com.furence.recsee.wooribank.script.param.response;

import java.util.List;
import java.util.Map;

import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.wooribank.script.util.JsonUtil;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptInfo {
	
	/**
	 * 상품 PK
	 */
	@JsonProperty("rsProductPk")
	private String productPk;
	
	/**
	 * 상품 이름
	 */
	@JsonProperty("rsProductName")
	private String productName;
	
	/**
	 * 상품코드
	 */
	@JsonProperty("rsProductCode")
	private String productCode;
	
	
	/**
	 * 상품종류 1:신탁 2:펀드 4:방카 5:퇴직연금
	 */
	@JsonProperty("rsProductType")
	private String productType;
	
	
	/**
	 * 그룹여부
	 */
	@JsonInclude(Include.NON_NULL)
	private String groupYN;
	
	/**
	 * 수정자
	 */
	@JsonProperty("rsUpdateUser")
	private String updateUser;
	
	/**
	 * 결재자
	 */
	@JsonProperty("rsApproveUser")
	private String approveUser;
	
	/**
	 * 스크립트 스텝 리스트
	 */
	private List<Step> stepList;
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Step {
		
		/**
		 * 상품 PK
		 */		
		@JsonInclude(Include.NON_NULL)
		private String prodcutPk;
		
		/**
		 * 상위 스텝 PK
		 */
		@JsonProperty("rsScriptStepPk")
		private String stepPk;
		
		/**
		 * 상위 스텝 PK
		 */
		@JsonProperty("rsScriptStepParent")
		@JsonInclude(Include.NON_NULL)
		private String stepParent;
		
		/**
		 * 스텝 이름
		 */
		@JsonProperty("rsScriptStepName")
		private String stepName;
		
		/**
		 * 스텝 단계 
		 */
		@JsonInclude(Include.NON_NULL)
		private String stepDepth;
		
		/*
		 * 스텝 정렬
		 */
		@JsonInclude(Include.NON_NULL)
		private String stepOrder;
		
		/**
		 * 수정타입
		 */		
		@JsonProperty("rsScriptStepEditType")
		private String editType;
		
		/**
		 * 디테일 리스트
		 */
		private List<Detail> detailList;

	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Detail {
		
		/**
		 * 디테일 정렬
		 */
		@JsonProperty("rsScriptStepDetailOrder")
		private String detailOrder;
		
		@JsonProperty("rsScriptStepDetailEditType")
		private String editType;
		
		/**
		 * 디테일 pk
		 */
		@JsonProperty("rsScriptStepDetailPk")
		private String detailPk;
		
		/*
		 * 디테일 타입 코드
		 */
		@JsonProperty("rsScriptStepDetailType")
		private String detailType;
		
		/**
		 * 디테일 타입 이름(TTS리딩,직원리딩,상담가이드,적합성보고서)
		 */
		@JsonProperty("rsScriptStepDetailTypeName")
		private String detailTypeName;
		
		/**
		 * 디테일 스크립트텍스트
		 */
		@JsonProperty("rsScriptDetailText")
		private String detailText;
		
		/*
		 * 공용스크립트 여부
		 */
		@JsonProperty("rsScriptDetailComKind")
		private String commonYn;
		
		/**
		 * 조견유형,삭제시에는 NULL
		 */
		@JsonInclude(Include.NON_NULL)
		@JsonProperty("rsScriptDetailIfCase")
		private String varibaleWordYn;

		/**
		 * 조견유형 코드,삭제시에는 NULL
		 */
		@JsonInclude(Include.NON_NULL)
		@JsonProperty("rsScriptDetailIfCaseCode")
		private String variableWordType;

		/**
		 * 조건유형상세
		 */
		@JsonInclude(Include.NON_NULL)
		@JsonProperty("rsScriptDetailIfCaseDetail")
		private String variableWord;

		/**
		 * 조견유형상세 코드,삭제시에는 NULL
		 */
		@JsonInclude(Include.NON_NULL)
		@JsonProperty("rsScriptDetailIfCaseDetailCode")
		private String variableWordCode;
		
		/**
		 * 실시간 TTS Y/N
		 */
		@JsonInclude(Include.NON_NULL)
		@JsonProperty("rsScriptDetailRealtimeTTS")
		private String realtimeTtsYn;
		
		
		/**
		 * 가변값 구분 코드
		 */
		@JsonProperty("rsScriptDetailCaseCode")
		private String ifCaseCode;
		/**
		 * 가변값 구분명
		 */
		@JsonProperty("rsScriptDetailCaseValue")
		private String ifCaseValue;
		/**
		 * 가변값 코드
		 */
		@JsonProperty("rsScriptDetailCaseDetailCode")
		private String ifCaseDetailCode;
		
		/**
		 * 가변값 
		 */
		@JsonProperty("rsScriptDetailCaseDetailValue")
		private String ifCaseDetailValue;
		
		/**
		 * elt 상품 디테일 속성 
		 */
		@JsonProperty("rsProductAttributes")
		private Map<String, String> productAttributes;
		
		/**
		 * elt 상품 디테일 속성 문자열 변환 리스트
		 */
		@JsonProperty("rsProductAttributesText")
		private List<String> productAttributesText;
		
		/**
		 * elt 상품 디테일 속성 
		 */
		@JsonProperty("rsProductAttributesExt")
		private Map<String, String> productAttributesExt;
		
		/**
		 * elt 상품 디테일 속성 
		 */
		@JsonProperty("rsScriptEltCase")
		private String eltCase;
		
		/**
		 * elt 상품 디테일 속성 
		 */
		@JsonProperty("rsScriptDetailCaseAttributes")
		private Map<String, String> variableAttributes;
		
		public String getUnescapedDetailText() {
			return HtmlUtils.htmlUnescape(this.detailText);
		}
		
		@JsonIgnoreProperties
		public String getEscapedDetailText() {
			return HtmlUtils.htmlEscape(this.detailText);
		}
	}
	
	public static ScriptInfo from(String jsonStr) throws Exception {
		return JsonUtil.strToObject(jsonStr, ScriptInfo.class);
	}
}
