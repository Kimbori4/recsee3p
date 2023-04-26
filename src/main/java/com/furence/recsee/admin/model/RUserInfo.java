package com.furence.recsee.admin.model;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

public class RUserInfo {
	
	private String userId;
	private String userName;
	private String password;
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String extNo;
	private String userLevel;
	private String empId;
	private String userEmail;
	private String userPhone;
	private String ctiId;
	private String clientKind;
	private String userSex;
	private String lockCount;
	private String lockYn;
	private String longRange;
	private String middleRange;
	private String shortRange;
	private String skinCode;
	private String pwEditDate;
	private String regiDate;
	private String bgName;
	private String mgName;
	private String sgName;
	private String rUseYn;
	private String rMonitoring;
	private Integer count;
	private Integer posStart;

	private String aUser;
	private String mobileYN;

	private List<String> list;
	
	private String clientIp;
	
	private String levelName;

	private String orderBy;
	private String direction;
	
	private String selectAll;
	
	private String logContents;
	
	private String rShareName;
	private String rShareYn;

	private String userLevelName;
	
	private List<HashMap<String, String>> authyInfo;
	
	public List<HashMap<String, String>> getAuthyInfo() {
		return authyInfo;
	}
	public void setAuthyInfo(List<HashMap<String, String>> authyInfo) {
		this.authyInfo = authyInfo;
	}
	
	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	public String getrShareYn() {
		return rShareYn;
	}
	public void setrShareYn(String rShareYn) {
		this.rShareYn = rShareYn;
	}
	public String getrShareName() {
		return rShareName;
	}
	public void setrShareName(String rShareName) {
		this.rShareName = rShareName;
	}
	public String getSelectAll() {
		return selectAll;
	}
	public void setSelectAll(String selectAll) {
		this.selectAll = selectAll;
	}
	public String getLockYn() {
		return lockYn;
	}
	public void setLockYn(String lockYn) {
		this.lockYn = lockYn;
	}
	public String getLogContents() {
		return logContents;
	}
	public void setLogContents(String logContents) {
		this.logContents = logContents;
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
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public String getMobileYN() {
		return mobileYN;
	}
	public void setMobileYN(String mobileYN) {
		this.mobileYN = mobileYN;
	}
	public String getaUser() {
		return aUser;
	}
	public void setaUser(String aUser) {
		this.aUser = aUser;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getPosStart() {
		return posStart;
	}
	public void setPosStart(Integer posStart) {
		this.posStart = posStart;
	}
	private List<Map<String, String>> group;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBgCode() {
		return bgCode;
	}
	public void setBgCode(String bgCode) {
		this.bgCode = bgCode;
	}
	public String getMgCode() {
		return mgCode;
	}
	public void setMgCode(String mgCode) {
		this.mgCode = mgCode;
	}
	public String getSgCode() {
		return sgCode;
	}
	public void setSgCode(String sgCode) {
		this.sgCode = sgCode;
	}
	public String getExtNo() {
		return extNo;
	}
	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCtiId() {
		return ctiId;
	}
	public void setCtiId(String ctiId) {
		this.ctiId = ctiId;
	}
	public String getClientKind() {
		return clientKind;
	}
	public void setClientKind(String clientKind) {
		this.clientKind = clientKind;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getLockCount() {
		return lockCount;
	}
	public void setLockCount(String lockCount) {
		this.lockCount = lockCount;
	}
	public String getLongRange() {
		return longRange;
	}
	public void setLongRange(String longRange) {
		this.longRange = longRange;
	}
	public String getMiddleRange() {
		return middleRange;
	}
	public void setMiddleRange(String middleRange) {
		this.middleRange = middleRange;
	}
	public String getShortRange() {
		return shortRange;
	}
	public void setShortRange(String shortRange) {
		this.shortRange = shortRange;
	}
	public String getSkinCode() {
		return skinCode;
	}
	public void setSkinCode(String skinCode) {
		this.skinCode = skinCode;
	}
	public String getPwEditDate() {
		return pwEditDate;
	}
	public void setPwEditDate(String pwEditDate) {
		this.pwEditDate = pwEditDate;
	}
	public String getRegiDate() {
		return regiDate;
	}
	public void setRegiDate(String regiDate) {
		this.regiDate = regiDate;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public List<Map<String, String>> getGroup() {
		return group;
	}
	public void setGroup(List<Map<String, String>> group) {
		this.group = group;
	}

	public String getBgName() {
		return bgName;
	}
	public void setBgName(String bgName) {
		this.bgName = bgName;
	}
	public String getMgName() {
		return mgName;
	}
	public void setMgName(String mgName) {
		this.mgName = mgName;
	}
	public String getSgName() {
		return sgName;
	}
	public void setSgName(String sgName) {
		this.sgName = sgName;
	}

	public String getrUseYn() {
		return rUseYn;
	}
	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}

	public String getrMonitoring() {
		return rMonitoring;
	}
	public void setrMonitoring(String rMonitoring) {
		this.rMonitoring = rMonitoring;
	}
	
	public String toString(MessageSource messageSource) {
		return messageSource.getMessage("log.RUserInfo.RUserInfo",null,Locale.getDefault())
				+ " ["
				+ (logContents != null && !"".equals(logContents)? messageSource.getMessage("log.RUserInfo.logContents",null,Locale.getDefault())+"=" + logContents : "")
				+ (userId != null && !"".equals(userId)? ", "+messageSource.getMessage("log.RUserInfo.userId",null,Locale.getDefault())+"=" + userId : "")
				+ (userName != null && !"".equals(userName)? ", "+messageSource.getMessage("log.RUserInfo.userName",null,Locale.getDefault())+"=" + userName : "")
				+ (bgCode != null && !"".equals(bgCode)? ", "+messageSource.getMessage("log.RUserInfo.bgCode",null,Locale.getDefault())+"=" + (bgName!= null ? bgName + "(" + bgCode + ")" : bgCode) : "")
				+ (mgCode != null && !"".equals(mgCode)? ", "+messageSource.getMessage("log.RUserInfo.mgCode",null,Locale.getDefault())+"=" + (mgName!= null ? mgName + "(" + mgCode + ")" : mgCode) : "")
				+ (sgCode != null && !"".equals(sgCode)? ", "+messageSource.getMessage("log.RUserInfo.sgCode",null,Locale.getDefault())+"=" + (sgName!= null ? sgName + "(" + sgCode + ")" : sgCode) : "")
				+ (extNo != null ? ", "+messageSource.getMessage("log.RUserInfo.extNo",null,Locale.getDefault())+"=" + extNo : "")
				+ (userLevel != null && !"".equals(userLevel)? ", "+messageSource.getMessage("log.RUserInfo.userLevel",null,Locale.getDefault())+"=" + userLevelName + "(" + userLevel +")" : "")
				+ (userEmail != null ? ", "+messageSource.getMessage("log.RUserInfo.userEmail",null,Locale.getDefault())+"=" + userEmail : "")
				+ (userPhone != null ? ", "+messageSource.getMessage("log.RUserInfo.userPhone",null,Locale.getDefault())+"=" + userPhone : "")
				+ (userSex != null && !"".equals(userSex)? ", "+messageSource.getMessage("log.RUserInfo.userSex",null,Locale.getDefault())+"=" + userSex : "")
				+ "]";
	}
	
	public String toLogString(MessageSource messageSource) {
		return 	messageSource.getMessage("log.RUserInfo.RUserInfo",null,Locale.getDefault())+" [ "  
				+ (userName!= 		null && !"".equals(userName)? messageSource.getMessage("log.RUserInfo.userName",null,Locale.getDefault())+"=" 		+ userName + " " : "")
				+ (userId!= 		null && !"".equals(userId)? messageSource.getMessage("log.RUserInfo.userId",null,Locale.getDefault())+"=" 			+ userId + " " : "") 
				+ (userPhone!= 		null && !"".equals(userPhone)? messageSource.getMessage("log.RUserInfo.userPhone",null,Locale.getDefault())+"=" 		+ userPhone + " " : "") 
				+ (userLevel!= 		null && !"".equals(userLevel)? messageSource.getMessage("log.RUserInfo.userLevel",null,Locale.getDefault())+"=" 		+ userLevel + " " : "") 
				+ (bgName!= 		null && !"".equals(bgName)? messageSource.getMessage("log.RUserInfo.bgName",null,Locale.getDefault())+"=" 			+ bgName + " " : "") 
				+ (mgName!= 		null && !"".equals(mgName)? messageSource.getMessage("log.RUserInfo.mgName",null,Locale.getDefault())+"=" 			+ mgName + " " : "") 
				+ (sgName!= 		null && !"".equals(sgName)? messageSource.getMessage("log.RUserInfo.sgName",null,Locale.getDefault())+"=" 			+ sgName + " " : "") 
				+ "]";																						
		
	}
}
