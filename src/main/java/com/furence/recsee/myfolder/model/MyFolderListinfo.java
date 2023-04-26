package com.furence.recsee.myfolder.model;

import java.util.List;

public class MyFolderListinfo {
	private String rUserId;
	private String rFolderName;
	private String rRecDate;
	private String rRecTime;
	private String rBgCode;
	private String rMgCode;
	private String rSgCode;
	private String rRecId;
	private String rRecName;
	private String rExtNum;
	private String rCustName;
	private String rCustPhone;
	private String rItemSerial;
	
	private List<String> recDateArr;
	private List<String> recTimeArr;
	private List<String> recExtArr;
	private List<String> rItemSerialArr;
	
	private String custPhone1IsEncrypt;
	private String custNameIsEncrypt;
	
	public String getCustNameIsEncrypt() {
		return custNameIsEncrypt;
	}
	public void setCustNameIsEncrypt(String custNameIsEncrypt) {
		this.custNameIsEncrypt = custNameIsEncrypt;
	}
	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrFolderName() {
		return rFolderName;
	}
	public void setrFolderName(String rFolderName) {
		this.rFolderName = rFolderName;
	}
	public String getrRecDate() {
		String tmpDate = null;
		if(rRecDate != null)	tmpDate = String.format("%s-%s-%s", rRecDate.substring(0, 4), rRecDate.substring(4,  6), rRecDate.substring(6, 8));
		return tmpDate;
	}
	public void setrRecDate(String rRecDate) {
		this.rRecDate = rRecDate;
	}
	public String getrRecTime() {
		String tmpTime = null;
		if(rRecTime != null) tmpTime = String.format("%s:%s:%s", rRecTime.substring(0, 2), rRecTime.substring(2,  4), rRecTime.substring(4, 6));
		return tmpTime;
	}
	public void setrRecTime(String rRecTime) {
		this.rRecTime = rRecTime;
	}
	public String getrBgCode() {
		return rBgCode;
	}
	public void setrBgCode(String rBgCode) {
		this.rBgCode = rBgCode;
	}
	public String getrMgCode() {
		return rMgCode;
	}
	public void setrMgCode(String rMgCode) {
		this.rMgCode = rMgCode;
	}
	public String getrSgCode() {
		return rSgCode;
	}
	public void setrSgCode(String rSgCode) {
		this.rSgCode = rSgCode;
	}
	public String getrRecId() {
		return rRecId;
	}
	public void setrRecId(String rRecId) {
		this.rRecId = rRecId;
	}
	public String getrRecName() {
		return rRecName;
	}
	public void setrRecName(String rRecName) {
		this.rRecName = rRecName;
	}
	public String getrExtNum() {
		return rExtNum;
	}
	public void setrExtNum(String rExtNum) {
		this.rExtNum = rExtNum;
	}
	public String getrCustName() {
		return rCustName;
	}
	public void setrCustName(String rCustName) {
		this.rCustName = rCustName;
	}
	public String getrCustPhone() {
		return rCustPhone;
	}
	public void setrCustPhone(String rCustPhone) {
		this.rCustPhone = rCustPhone;
	}
	public List<String> getRecDateArr() {
		return recDateArr;
	}
	public void setRecDateArr(List<String> recDateArr) {
		this.recDateArr = recDateArr;
	}
	public List<String> getRecTimeArr() {
		return recTimeArr;
	}
	public void setRecTimeArr(List<String> recTimeArr) {
		this.recTimeArr = recTimeArr;
	}
	public List<String> getRecExtArr() {
		return recExtArr;
	}
	public void setRecExtArr(List<String> recExtArr) {
		this.recExtArr = recExtArr;
	}
	public String getrItemSerial() {
		return rItemSerial;
	}
	public void setrItemSerial(String rItemSerial) {
		this.rItemSerial = rItemSerial;
	}
	public List<String> getrItemSerialArr() {
		return rItemSerialArr;
	}
	public void setrItemSerialArr(List<String> rItemSerialArr) {
		this.rItemSerialArr = rItemSerialArr;
	}
	public String getCustPhone1IsEncrypt() {
		return custPhone1IsEncrypt;
	}
	public void setCustPhone1IsEncrypt(String custPhone1IsEncrypt) {
		this.custPhone1IsEncrypt = custPhone1IsEncrypt;
	}
	
}
