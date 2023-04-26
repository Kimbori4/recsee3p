package com.furence.recsee.statistics.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class KeywordStatisticInfo {
	
	private String allCnt;
	private String rowId;
	private String rType;
	private String sDate;
	private String eDate;
	
	private Integer rKeywordId;
	private Integer rCategoryId;
	
	private String rKeywordCode;
	private String rKeywordName;
	private String rUseYn;
	
	private String rDate;
	private String rCategoryName;
	private String rWord;
	private String rWordCount;
	
	private List<String> rKeyWords;
	
	private String division;
	private Integer lastweekcnt;
	private Integer toweekcnt;
	private Double Increase;
	private Date rCreatedAt;
	
	public String getAllCnt() {
		return allCnt;
	}
	public void setAllCnt(String allCnt) {
		this.allCnt = allCnt;
	}
	
	public String getrCategoryName() {
		return rCategoryName;
	}
	public void setrCategoryName(String rCategoryName) {
		this.rCategoryName = rCategoryName;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getrType() {
		return rType;
	}
	public void setrType(String rType) {
		this.rType = rType;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	
	public String getrKeywordCode() {
		return rKeywordCode;
	}
	public void setrKeywordCode(String rKeywordCode) {
		this.rKeywordCode = rKeywordCode;
	}
	public String getrKeywordName() {
		return rKeywordName;
	}
	public void setrKeywordName(String rKeywordName) {
		this.rKeywordName = rKeywordName;
	}
	public String getrUseYn() {
		return rUseYn;
	}
	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}
	public String getrDate() {
		return rDate;
	}
	public void setrDate(String rDate) {
		this.rDate = rDate;
	}
	public String getrCategory() {
		return rCategoryName;
	}
	public void setrCategory(String rCategoryName) {
		this.rCategoryName = rCategoryName;
	}
	public String getrWord() {
		return rWord;
	}
	public void setrWord(String rWord) {
		this.rWord = rWord;
	}
	public String getrWordCount() {
		return rWordCount;
	}
	public void setrWordCount(String rWordCount) {
		this.rWordCount = rWordCount;
	}
	public List<String> getrKeyWords() {
		return rKeyWords;
	}
	public void setrKeyWords(List<String> rKeyWords) {
		this.rKeyWords = rKeyWords;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public Integer getLastweekcnt() {
		return lastweekcnt;
	}
	public void setLastweekcnt(Integer lastweekcnt) {
		this.lastweekcnt = lastweekcnt;
	}
	public Integer getToweekcnt() {
		return toweekcnt;
	}
	public void setToweekcnt(Integer toweekcnt) {
		this.toweekcnt = toweekcnt;
	}
	public Double getIncrease() {
		return Increase;
	}
	public void setIncrease(Double increase) {
		Increase = increase;
	}
	public Date getrCreatedAt() {
		return rCreatedAt;
	}
	public void setrCreatedAt(Date rCreatedAt) {
		this.rCreatedAt = rCreatedAt;
	}
	public Integer getrKeywordId() {
		return rKeywordId;
	}
	public void setrKeywordId(Integer rKeywordId) {
		this.rKeywordId = rKeywordId;
	}
	public Integer getrCategoryId() {
		return rCategoryId;
	}
	public void setrCategoryId(Integer rCategoryId) {
		this.rCategoryId = rCategoryId;
	}
	
	
	
}
