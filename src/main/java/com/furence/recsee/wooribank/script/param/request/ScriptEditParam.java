package com.furence.recsee.wooribank.script.param.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.wooribank.script.annotation.Translate;
import com.furence.recsee.wooribank.script.annotation.Translate.TranslateTypes;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.base.EditDtoType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ScriptEditParam {
	
	@Getter
	@Setter
	@ToString(callSuper = true)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class History {

		/**
		 * 상품 테이블 PK
		 */
		private String productPk;
		
		/**
		 * 상품 스크립트 버전
		 */
		private String scriptVersion;
		
	}	
	
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class Common implements EditDtoType{

		/**
		 * 공통 스크립트 수정테이블 pk
		 */
		@JsonProperty("rsScriptEditId")
		private String commonScriptEditId;
		
		/**
		 * 수정타입
		 */
		@JsonIgnore
		private EditTypes editType;
		
		/**
		 * 공통 스크립트 pk
		 */
		@JsonProperty("rsScriptCommonPk")
		private String commonScriptPk;

		/**
		 * 공통 스크립트 pk
		 */
		@JsonProperty("rsScriptCommonType")
		private String commonScriptType;

		/**
		 * 공통 스크립트 설명
		 */
		@JsonProperty("rsScriptCommonDesc")
		private String commonScriptDesc;

		/**
		 * 공통 스크립트 이름
		 */
		@JsonProperty("rsScriptCommonName")
		private String commonScriptName;

		/**
		 * 공통 스크립트 내용'
		 */
		@JsonProperty("rsScriptCommonText")
		@Translate(value = { TranslateTypes.HtmlEsacpe, TranslateTypes.WhiteSpaceRemove })
		private String commonScriptText;

		/**
		 * 공통 스크립트 실시간TTS요청 여부
		 */
		@JsonProperty("rsScriptCommonRealtimeTTS")
		private String commonScriptRealtimeTTS;
		
		/**
		 * 스크립트 편집자
		 */
		@JsonProperty("rsScriptCommonEditUser")
		private String commonScriptEditUser;
		
		/**
		 * 적용요청일자
		 */
		@JsonProperty("rsScriptCommonApplyDate")
		private String commonScriptApplyDate;
		
		/**
		 * 적용타입
		 */
		@JsonProperty("rsScriptCommonApplyType")
		private Const.Apply commonScriptApplyType;
		
		
		@Override
		public String getScriptKey() {
			return this.commonScriptPk;
		}
		
	}
	
	
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class Step extends ScriptEditBase implements EditDtoType {
		
		/**
		 * 공통 스크립트 수정테이블 pk
		 */
		@JsonProperty("rsScriptEditId")
		private String scriptEditId;
		
			
		/**
		 * 해당 스크립트가 속한 상품의 고유키
		 */
		@JsonProperty("rsScriptStepFk")
		private String productPk;
		
		/**
		 * 스크립트 스텝의 pk
		 */
		@JsonProperty("rsScriptStepPk")
		private String scriptStepPk;
		
		/**
		 * 스크립트 스텝의 pk
		 */
		@JsonProperty("rsScriptStepTempPk")
		private String scriptStepTempPk;
		
		/**
		 * 상위 스크립트 스텝의 pk
		 */
		@JsonProperty("rsScriptStepParent")
		private String scriptStepParentPk;
		
		/**
		 * 스크립트 스텝 타입
		 */
		@JsonProperty("rsScriptStepType")
		private String scriptStepType;
		
		/**
		 * 표시이름
		 */
		@Translate(TranslateTypes.HtmlEsacpe)
		@JsonProperty("rsScriptStepName")
		private String scriptStepName;
		
		
		@Override
		public String getScriptKey() {		
			return this.scriptStepPk;
		}
		
	}
	
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class Detail extends ScriptEditBase implements EditDtoType{
		
		/**
		 * 상품코드
		 */
		@JsonProperty("rsProductCode")
		private String productCode;
		
		/**
		 * 공통 스크립트 수정테이블 pk
		 */
		@JsonProperty("rsScriptEditId")
		private String scriptEditId;
		
		/**
		 * 스텝 디테일 PK, 새 디테일 추가시는 NULL
		 */
		@JsonProperty("rsScriptStepDetailPk")
		private String scriptStepDetailPk;
			
		/**
		 * 스텝 디테일 임시 PK(수정,삭제는 원본키와 동일함)
		 */
		@JsonProperty("rsScriptStepDetailTempPk")
		private String scriptStepDetailTempPk;
			
		/**
		 * 스텝 PK, 새스텝 추가시는 NULL
		 */
		@JsonProperty("rsScriptStepPk")
		private String scriptStepPk;
		
		/**
		 * T:TTS리팅 G:직원리딩 S:상담가이드 R:적합성보고서 수정,삭제는 NULL 
		 */
		@JsonProperty("rsScriptDetailType")
		private String scriptDetailType;

		/**
		 * 스크립트가 적용된 상품 PK
		 */
		@JsonProperty("rsScriptStepFk")
		private String productPk;

		/**
		 * 스크립트 내용,삭제시에는 NUL
		 */
		@Translate(TranslateTypes.HtmlEsacpe)
		@JsonProperty("rsScriptDetailText")
		private String scriptDetailText;

		/**
		 * 조견유형,삭제시에는 NULL
		 */
		@JsonProperty("rsScriptDetailIfCase")
		private Const.Bool varibaleWordYN;

		/**
		 * 조견유형 코드,삭제시에는 NULL
		 */
		@JsonProperty("rsScriptDetailIfCaseCode")
		private String variableWordType;

		/**
		 * 조건유형상세
		 */
		@JsonProperty("rsScriptDetailIfCaseDetail")
		private String variableWord;

		/**
		 * 조견유형상세 코드,삭제시에는 NULL
		 */
		@JsonProperty("rsScriptDetailIfCaseDetailCode")
		private String variableWordCode;

		/**
		 * 실시간 TTS Y/N
		 */
		@JsonProperty("rsScriptDetailRealtimeTTS")
		private Const.Bool realtimeTtsYN;
		
		/**
		 * 공통스크립트 사용 여부
		 */
		@JsonProperty("rsScriptDetailComKind")
		private Const.Bool commonScriptYN;
			
		/**
		 * 공통스크립트 pk 
		 */
		@JsonProperty("rsScriptDetailComFk")
		private String commonScriptPk;
		
		/**
		 * 상품코드
		 */
		@JsonProperty("rUseYn")
		private String useYn;
		
		@JsonProperty("editMode")
		private String editMode =  "N";

		
		@Override
		public String getScriptKey() {		
			return this.scriptStepPk;
		}

	}
	
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class Transaction extends ScriptEditBase implements EditDtoType{

		@JsonProperty("rsProductListPk")
		private String  productPk;
		
		@JsonProperty("endType")
		private Const.Transaction endType;
		
		@JsonProperty("applyType")
		private Const.Apply applyType;
		
		@JsonProperty("applyDate")
        private String applyDate;
		
		@Override
		public String getScriptKey() {
			return this.productPk;
		}
		
		@JsonProperty("oldTransactionId")
		private String oldTransactionId;
	}
	
}
