package com.furence.recsee.wooribank.facerecording.service.impl;

import java.util.Arrays;
import java.util.List;

import com.furence.recsee.wooribank.facerecording.model.FaceRecMoreProductCodeDto;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;

public class FaceRecMoreProductCodeDtoSample extends FaceRecMoreProductCodeDto {
	
	
	private String prdCd;
	
	private String callKey;
	
	public static Builder builder(String prdCd, String callKey) {
		return new Builder(prdCd, callKey);
	}
	
	public static class Builder {
		
		private String prdCd;
		
		private String callKey;
		
		private String prdCd2;
		
		private String prdCd3;
		
		private String prdCd4;
		
		public Builder(String prdCd, String callKey) {
			this.prdCd = prdCd;
			this.callKey = callKey;
		}
		
		public Builder prdCd2(String prdCd2) {
			this.prdCd2 = prdCd2;
			return this;
		}
		
		public Builder prdCd3(String prdCd3) {
			this.prdCd3 = prdCd3;
			return this;
		}
		
		public Builder prdCd4(String prdCd4) {
			this.prdCd4 = prdCd4;
			return this;
		}
		
		public FaceRecMoreProductCodeDtoSample build() {
			
			FaceRecMoreProductCodeDtoSample sample = new FaceRecMoreProductCodeDtoSample();
			sample.setPrdCd(this.prdCd);
			sample.setCallKey(this.callKey);
			return sample;
		}

		public String getPrdCd() {
			return prdCd;
		}

		public void setPrdCd(String prdCd) {
			this.prdCd = prdCd;
		}

		public String getCallKey() {
			return callKey;
		}

		public void setCallKey(String callKey) {
			this.callKey = callKey;
		}
		
		
	}
	
	
}
