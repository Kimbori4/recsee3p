package com.furence.recsee.wooribank.facerecording.model;

public class ProductListSearchVo {
	
	private String rSearchWord;
	
	private String rSearchType;
	
	private String orderBy;
	
	private String direction;
	
	private String rProductListPk;
	
	private String rProductType;
	
	private String offerProductFlag;
	
	private String sysType;
	
	private Boolean trFlag = true;

	public Boolean getTrFlag() {
		return trFlag;
	}

	public void setTrFlag(Boolean trFlag) {
		this.trFlag = trFlag;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public String getrSearchWord() {
		return rSearchWord;
	}

	public void setrSearchWord(String rSearchWord) {
		this.rSearchWord = rSearchWord;
	}

	public String getrSearchType() {
		return rSearchType;
	}

	public void setrSearchType(String rSearchType) {
		this.rSearchType = rSearchType;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
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

	@Override
	public String toString() {
		return "ProductListSearchVo [rSearchWord=" + rSearchWord + ", rSearchType=" + rSearchType + ", orderBy="
				+ orderBy + ", direction=" + direction + ", rProductListPk=" + rProductListPk + "]";
	}
	
	public void copyProductListVo(ProductListVo vo) {
		try {
			this.rProductListPk = vo.getrProductListPk();
			this.rProductType = vo.getrProductType();
			this.rSearchWord = vo.getrProductCode();
			this.sysType = vo.getrSysType();
		}catch (Exception e) {
			throw new NullPointerException("상품기본정보 누락");
		}
	}
	
	
	

}
