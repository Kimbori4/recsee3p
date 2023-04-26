package com.furence.recsee.wooribank.facerecording.model;

public class ProductListVo {
	
	
		private String rProductListPk;
		private String rProductType;
		private String rProductCode;
		private String rProductName;
		private String rUseYn;
		private String rProductGroupCode; 
		private String rProductGroupYn;
		
		private String rProductAttributes;
		
		private String rSysType;
		
		private int rGroupPk;
		
		
		
		
		
		
		public int getrGroupPk() {
			return rGroupPk;
		}
		public void setrGroupPk(int rGroupPk) {
			this.rGroupPk = rGroupPk;
		}
		public String getrSysType() {
			return rSysType;
		}
		public void setrSysType(String rSysType) {
			this.rSysType = rSysType;
		}
		public String getrProductAttributes() {
			return rProductAttributes;
		}
		public void setrProductAttributes(String rProductAttributes) {
			this.rProductAttributes = rProductAttributes;
		}
		public String getrProductListPk() {
			return rProductListPk;
		}
		public void setrProductListPk(String rProductListPk) {
			this.rProductListPk = rProductListPk;
		}
		public String getrProductType() {
			return rProductType;
		}
		public void setrProductType(String rProductType) {
			this.rProductType = rProductType;
		}
		public String getrProductCode() {
			return rProductCode;
		}
		public void setrProductCode(String rProductCode) {
			this.rProductCode = rProductCode;
		}
		public String getrProductName() {
			return rProductName;
		}
		public void setrProductName(String rProductName) {
			this.rProductName = rProductName;
		}
		public String getrUseYn() {
			return rUseYn;
		}
		public void setrUseYn(String rUseYn) {
			this.rUseYn = rUseYn;
		}
		public String getrProductGroupCode() {
			return rProductGroupCode;
		}
		public void setrProductGroupCode(String rProductGroupCode) {
			this.rProductGroupCode = rProductGroupCode;
		}
		public String getrProductGroupYn() {
			return rProductGroupYn;
		}
		public void setrProductGroupYn(String rProductGroupYn) {
			this.rProductGroupYn = rProductGroupYn;
		}
		@Override
		public String toString() {
			return "ProductListVo [rProductListPk=" + rProductListPk + ", rProductType=" + rProductType
					+ ", rProductCode=" + rProductCode + ", rProductName=" + rProductName + ", rUseYn=" + rUseYn
					+ ", rProductGroupCode=" + rProductGroupCode + ", rProductGroupYn=" + rProductGroupYn
					+ ", rProductAttributes=" + rProductAttributes + "]";
		}
		
		
}
