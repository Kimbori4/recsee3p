package com.furence.recsee.wooribank.script.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

final public class Const {
	
	public static final String SCRIPT_TRANSACTION_ENABLED = "transactionEnableed";
	
	public static final String SCRIPT_TRANSACTION_KEY = "transactionId";
	
	public enum ScriptKind {
		
		/**
		 * 공용 스크립트
		 */
		Common("C"),
		/**
		 * 상품 스크립트
		 */
		Product("P");
		
		private String value = "";
		
		ScriptKind(String s){
			this.value = s;
		}
		
		@JsonValue
		public String getValue() {
			return this.value;
		}
		
		@JsonCreator
		public static ScriptKind create(String value) {
			
			if(value == null) return null;
			
			String upperValue = value.toUpperCase();
			for(ScriptKind type: ScriptKind.values()) {
				if(type.getValue().equals(upperValue) || type.name().equals(upperValue) ) {
					return type;
				}
			}
			return null;
		}
	}
	
	public enum Bool{
		@JsonProperty(value = "Y")
		YES("Y"),
		@JsonProperty(value = "N")
		NO("N");
		
		private String value = "";
		
		Bool(String s){
			this.value = s;
		}
		
		@JsonValue
		public String getValue() {
			return this.value;
		}
		
		@JsonCreator
		public static Bool create(String value) {
			
			if(value == null) return null;
			
			String upperValue = value.toUpperCase();
			for(Bool type: Bool.values()) {
				if(type.getValue().equals(upperValue) || type.name().equals(upperValue) ) {
					return type;
				}
			}
			return null;
		}
		
	}
	
	public enum ScriptStepType{
		CONTAINS("P"),
		NOT_CONTAIN("N");
		
		private String value = "";
		
		ScriptStepType(String s){
			this.value = s;
		}
		
		@JsonValue
		public String getValue() {
			return this.value;
		}
		
		@JsonCreator
		public static ScriptStepType create(String value) {
			
			if(value == null) return null;
			
			String upperValue = value.toUpperCase();
			for(ScriptStepType type: ScriptStepType.values()) {
				if(type.getValue().equals(upperValue) || type.name().equals(upperValue) ) {
					return type;
				}
			}
			return null;
		}
		
		public static ScriptStepType contains(String str) {
			if(str == null) return null;
			return str.contentEquals("설명") ? CONTAINS : NOT_CONTAIN;
		}
	}
	
	public enum Approve{
		REQUEST("APRV01"),
		CANCEL("APRV02"),
		REJECT("APRV03"),
		APPROVE("APRV04");
		
		private String value = "";
		
		Approve(String s){
			this.value = s;
		}
		
		@JsonValue
		public String getValue() {
			return this.value;
		}
		
		@JsonCreator
		public static Approve create(String name) {
			
			if(name == null) return null;
			
			String upperValue = name.toUpperCase();
			for(Approve type: Approve.values()) {
				if(type.getValue().equals(upperValue) || type.name().equals(upperValue) ) {
					return type;
				}
			}
			return null;
		}
		
		public String getKorName() {
			switch (this) {
			case REQUEST:
				return "결재의뢰";
			case APPROVE:
				return "결재완료";
			case CANCEL:
				return "취소";
			case REJECT:
				return "반려";
			default:
				return "";
			}
		}
		
	}
	
	public enum Transaction{
		COMMIT,
		ROLLBACK;
		
		@JsonCreator
		public static Transaction create(String name) {
			
			if(name == null) return null;	
			
			String upperValue = name.toUpperCase();
			for(Transaction type: Transaction.values()) {
				if(type.name().equals(upperValue) ) {
					return type;
				}
			}
			return null;
		}
		
	}
	
	
	public enum Apply{
		DAYAFTER("APTP01"),
		IMMEDIATELY("APTP02"),
		RESERVED("APTP03");
		
		private String value = "";
		
		Apply(String s){
			this.value = s;
		}
		
		@JsonValue
		public String getValue() {
			return this.value;
		}
		
		@JsonCreator
		public static Apply create(String name) {
			
			if(name == null) return null;
			
			String upperValue = name.toUpperCase();
			for(Apply type: Apply.values()) {
				if(type.getValue().equals(upperValue) || type.name().equals(upperValue) ) {
					return type;
				}
			}
			return null;
		}
		
	}
}
