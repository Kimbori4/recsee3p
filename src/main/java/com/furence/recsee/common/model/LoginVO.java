package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class LoginVO {
	
	private String userId;
	private String userName;
	private String userPass;
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String bgCodeName;
	private String mgCodeName;
	private String sgCodeName;
	private String userLevel;
	private String skinCode;
	private String regiDate;
	private String pwEditDate;
	private String shortRange;
	private String middleRange;
	private String longRange;
	private String userEmail;
	private String extNo;
	private String tryCount;
	private String lastLoginDate;
	private String lockYn;
	
	private String empId;
	private String userPhone;
	private String ctiId;
	private String userSex;
	
	private String clientIp;

	private String failReason;
	
	private String userLevelName;
	
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getBgCodeName() {
		return bgCodeName;
	}
	public void setBgCodeName(String bgCodeName) {
		this.bgCodeName = bgCodeName;
	}
	public String getMgCodeName() {
		return mgCodeName;
	}
	public void setMgCodeName(String mgCodeName) {
		this.mgCodeName = mgCodeName;
	}
	public String getSgCodeName() {
		return sgCodeName;
	}
	public void setSgCodeName(String sgCodeName) {
		this.sgCodeName = sgCodeName;
	}
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
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
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
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getSkinCode() {
		return skinCode;
	}
	public void setSkinCode(String skinCode) {
		this.skinCode = skinCode;
	}
	public String getRegiDate() {
		return regiDate;
	}
	public void setRegiDate(String regiDate) {
		this.regiDate = regiDate;
	}
	public String getPwEditDate() {
		return pwEditDate;
	}
	public void setPwEditDate(String pwEditDate) {
		this.pwEditDate = pwEditDate;
	}
	public String getShortRange() {
		return shortRange;
	}
	public void setShortRange(String shortRange) {
		this.shortRange = shortRange;
	}
	public String getMiddleRange() {
		return middleRange;
	}
	public void setMiddleRange(String middleRange) {
		this.middleRange = middleRange;
	}
	public String getLongRange() {
		return longRange;
	}
	public void setLongRange(String logRange) {
		this.longRange = logRange;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getExtNo() {
		return extNo;
	}
	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}
	public String getTryCount() {
		return tryCount;
	}
	public void setTryCount(String tryCount) {
		this.tryCount = tryCount;
	}
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getLockYn() {
		return lockYn;
	}
	public void setLockYn(String lockYn) {
		this.lockYn = lockYn;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getCtiId() {
		return ctiId;
	}
	public void setCtiId(String ctiId) {
		this.ctiId = ctiId;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	
	public String toString(MessageSource messageSource) {
		return messageSource.getMessage("log.LoginVO.LoginVO",null,Locale.getDefault())
				+ " ["
				+messageSource.getMessage("log.LoginVO.failReason",null,Locale.getDefault())+"="+failReason
				+ ", "+messageSource.getMessage("log.LoginVO.userId",null,Locale.getDefault())+"=" + userId
				+ ", "+messageSource.getMessage("log.LoginVO.clientIp",null,Locale.getDefault())+"=" + clientIp
				+ ", "+messageSource.getMessage("log.LoginVO.userLevel",null,Locale.getDefault())+"=" + userLevel
				+ ", "+messageSource.getMessage("log.LoginVO.userLevelName",null,Locale.getDefault())+"=" + userLevelName
				+ ", "+messageSource.getMessage("log.LoginVO.pwEditDate",null,Locale.getDefault())+"=" + pwEditDate
				+ ", "+messageSource.getMessage("log.LoginVO.tryCount",null,Locale.getDefault())+"=" + tryCount
				+ ", "+messageSource.getMessage("log.LoginVO.lastLoginDate",null,Locale.getDefault())+"=" + lastLoginDate
				+ ", "+messageSource.getMessage("log.LoginVO.lockYn",null,Locale.getDefault())+"=" + lockYn
				+ "]";
	}

}
