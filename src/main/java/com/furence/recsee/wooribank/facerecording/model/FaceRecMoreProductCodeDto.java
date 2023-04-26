package com.furence.recsee.wooribank.facerecording.model;

import java.util.ArrayList;
import java.util.List;

public class FaceRecMoreProductCodeDto {
	
	private ScriptStepDetailVo detailVo;
	
	private ScriptStepVo stepVo;
	
	private List<String> productCodeList;
	
	private List<ProductListVo> productList;
	
	private String prdCd;
	
	private String callKey;
	
	private String productName;
	
	private String[] regAmArr;
	
	
	

	public String[] getRegAmArr() {
		return regAmArr;
	}

	public void setRegAmArr(String[] regAmArr) {
		this.regAmArr = regAmArr;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * morProductType
	 * comment : (int) 0 = 일반 상품 (다계좌X,양수도X)
	 * 				   1 = 다계좌 
	 * 				   2 = 양수도
	 * */
	private int moreProductType;
	

	public int getMoreProductType() {
		return moreProductType;
	}

	public void setMoreProductType(int moreProductType) {
		this.moreProductType = moreProductType;
	}

	public String getCallKey() {
		return callKey;
	}

	public void setCallKey(String callKey) {
		this.callKey = callKey;
	}

	public String getPrdCd() {
		return prdCd;
	}

	public void setPrdCd(String prdCd) {
		this.prdCd = prdCd;
	}

	public FaceRecMoreProductCodeDto() {
		this.detailVo = new ScriptStepDetailVo();
		this.stepVo = new ScriptStepVo();
		this.productList = new ArrayList<ProductListVo>();
		this.productCodeList = new ArrayList<String>();
	}
	
	public void addProductList(ProductListVo vo) {
		this.productList.add(vo);
	}
	
	public void addProductCodeList(String code) {
		this.productCodeList.add(code);
	}

	public ScriptStepDetailVo getDetailVo() {
		return detailVo;
	}

	public void setDetailVo(ScriptStepDetailVo detailVo) {
		this.detailVo = detailVo;
	}

	public ScriptStepVo getStepVo() {
		return stepVo;
	}

	public void setStepVo(ScriptStepVo stepVo) {
		this.stepVo = stepVo;
	}

	public List<String> getProductCodeList() {
		return productCodeList;
	}

	public List<ProductListVo> getProductList() {
		return productList;
	}
	
	

}
