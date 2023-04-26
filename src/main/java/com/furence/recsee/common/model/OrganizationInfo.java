package com.furence.recsee.common.model;

import java.util.HashMap;
import java.util.List;

public class OrganizationInfo {
	String rBgCode;
	String rBgName;
	String rMgCode;
	String rMgName;
	String rSgCode;
	String rSgName;
	String rSys;
	String rEtc;
	String rTable;
	String groupCode;
	String sameCount;
	HashMap<String, String> selectValue;
	
	String notAdmin;
	
	String oBgCode;
	String oMgCode;
	String oSgCode;
	
	String useYn;
	String aUser;
	
	String state;
	
	String type;
	
	String notIvr;
	
	List<String> list;
	
	
	private List<HashMap<String, String>> authyInfo;
	
	public List<HashMap<String, String>> getAuthyInfo() {
		return authyInfo;
	}
	public void setAuthyInfo(List<HashMap<String, String>> authyInfo) {
		this.authyInfo = authyInfo;
	}

	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public String getNotIvr() {
		return notIvr;
	}
	public void setNotIvr(String notIvr) {
		this.notIvr = notIvr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getaUser() {
		return aUser;
	}
	public void setaUser(String aUser) {
		this.aUser = aUser;
	}
	
	public String getrBgCode() {
		return rBgCode;
	}
	public void setrBgCode(String rBgCode) {
		this.rBgCode = rBgCode;
	}
	public String getrBgName() {
		return rBgName;
	}
	public void setrBgName(String rBgName) {
		this.rBgName = rBgName;
	}
	public String getrMgCode() {
		return rMgCode;
	}
	public void setrMgCode(String rMgCode) {
		this.rMgCode = rMgCode;
	}
	public String getrMgName() {
		return rMgName;
	}
	public void setrMgName(String rMgName) {
		this.rMgName = rMgName;
	}
	public String getrSgCode() {
		return rSgCode;
	}
	public void setrSgCode(String rSgCode) {
		this.rSgCode = rSgCode;
	}
	public String getrSgName() {
		return rSgName;
	}
	public void setrSgName(String rSgName) {
		this.rSgName = rSgName;
	}
	public String getrSys() {
		return rSys;
	}
	public void setrSys(String rSys) {
		this.rSys = rSys;
	}
	public String getrEtc() {
		return rEtc;
	}
	public void setrEtc(String rEtc) {
		this.rEtc = rEtc;
	}
	public String getNotAdmin() {
		return notAdmin;
	}
	public void setNotAdmin(String notAdmin) {
		this.notAdmin = notAdmin;
	}
	public String getrTable() {
		return rTable;
	}
	public void setrTable(String rTable) {
		this.rTable = rTable;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getSameCount() {
		return sameCount;
	}
	public void setSameCount(String sameCount) {
		this.sameCount = sameCount;
	}
	public HashMap<String, String> getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(HashMap<String, String> selectValue) {	
		this.selectValue = selectValue;
	}
	public String getoBgCode() {
		return oBgCode;
	}
	public void setoBgCode(String oBgCode) {
		this.oBgCode = oBgCode;
	}
	public String getoMgCode() {
		return oMgCode;
	}
	public void setoMgCode(String oMgCode) {
		this.oMgCode = oMgCode;
	}
	public String getoSgCode() {
		return oSgCode;
	}
	public void setoSgCode(String oSgCode) {
		this.oSgCode = oSgCode;
	}
	
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	@Override
	public String toString() {
		return "OrganizationInfo [rBgCode=" + rBgCode + ", rBgName=" + rBgName
				+ ", rMgCode=" + rMgCode + ", rMgName=" + rMgName
				+ ", rSgCode=" + rSgCode + ", rSgName=" + rSgName + ", rSys="
				+ rSys + ", rEtc=" + rEtc + ", rTable=" + rTable
				+ ", groupCode=" + groupCode + ", sameCount=" + sameCount
				+ ", selectValue=" + selectValue + ", notAdmin=" + notAdmin
				+ ", oBgCode=" + oBgCode + ", oMgCode=" + oMgCode
				+ ", oSgCode=" + oSgCode + "]";
	}
}
