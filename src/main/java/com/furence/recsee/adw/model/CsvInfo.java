package com.furence.recsee.adw.model;

import java.util.Optional;

import lombok.Data;

@Data
public class CsvInfo {
	
	private String recKey;
	
	private String callKeyAp;
	
	private String custId;
	
	private String rcdFmltAutoYn;
	
	private String addRcdYn;
	
	private String addRcdMcd;
	
	private String addRcdRsn2Txt;
	
	private String recDate;
	
	private String recTime;
	
	private String callTtime;
	
	private String lstRcdYn;
	
	private String productName;
	
	private String advpeNo;
	
	private String oprNo;
	
	private String extNum;
	
	private String bizDis;
	
	private String rcdSysDscd;
	
	private String rcdSysDbChgDtm;
	
	private String rcdTrnKdcd;
	
	private String multPrdRcdYn;
	
	public void npeCheckData() {
		this.recKey = Optional.ofNullable(this.recKey).orElse("");
		this.callKeyAp = Optional.ofNullable(this.callKeyAp).orElse("");
		this.custId = Optional.ofNullable(this.custId).orElse("");
		this.rcdFmltAutoYn = Optional.ofNullable(this.rcdFmltAutoYn).orElse("");
		this.addRcdYn = Optional.ofNullable(this.addRcdYn).orElse("");
		this.addRcdMcd = Optional.ofNullable(this.addRcdMcd).orElse("");
		this.addRcdRsn2Txt = Optional.ofNullable(this.addRcdRsn2Txt).orElse("");
		this.recDate = Optional.ofNullable(this.recDate).orElse("");
		this.recTime = Optional.ofNullable(this.recTime).orElse("");
		this.callTtime = Optional.ofNullable(this.callTtime).orElse("");
		this.lstRcdYn = Optional.ofNullable(this.lstRcdYn).orElse("");
		this.productName = Optional.ofNullable(this.productName).orElse("");
		this.advpeNo = Optional.ofNullable(this.advpeNo).orElse("");
		this.oprNo = Optional.ofNullable(this.oprNo).orElse("");
		this.extNum = Optional.ofNullable(this.extNum).orElse("");
		this.bizDis = Optional.ofNullable(this.bizDis).orElse("");
		this.rcdSysDscd = Optional.ofNullable(this.rcdSysDscd).orElse("");
		this.rcdSysDbChgDtm = Optional.ofNullable(this.rcdSysDbChgDtm).orElse("");
		this.rcdTrnKdcd = Optional.ofNullable(this.rcdTrnKdcd).orElse("");
		this.multPrdRcdYn = Optional.ofNullable(this.multPrdRcdYn).orElse("");
	}
	
	
	public String csvTextExport() {
		StringBuffer buf = new StringBuffer();
		String delimeter = "|";
		
		buf.append(this.recKey+delimeter);
		buf.append(this.callKeyAp+delimeter);
		buf.append(this.custId+delimeter);
		buf.append(this.rcdFmltAutoYn+delimeter);
		buf.append(this.addRcdYn+delimeter);
		buf.append(this.addRcdMcd+delimeter);
		buf.append(this.addRcdRsn2Txt+delimeter);
		buf.append(this.recDate+delimeter);
		buf.append(this.recTime+delimeter);
		buf.append(this.callTtime+delimeter);
		buf.append(this.lstRcdYn+delimeter);
		buf.append(this.productName+delimeter);
		buf.append(this.advpeNo+delimeter);
		buf.append(this.oprNo+delimeter);
		buf.append(this.extNum+delimeter);
		buf.append(this.bizDis+delimeter);
		buf.append(this.rcdSysDscd+delimeter);
		buf.append(this.rcdSysDbChgDtm+delimeter);
		buf.append(this.rcdTrnKdcd+delimeter);
		buf.append(this.multPrdRcdYn);
		
		return buf.toString();
	}
	
	
	

}
