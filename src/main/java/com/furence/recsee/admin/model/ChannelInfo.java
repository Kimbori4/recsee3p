package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class ChannelInfo {
	
	private Integer chNum;
	private String sysCode;
	private String extNum;
	private String extKind;
	private String extIp;
	private String extMac;
	private String rxAmp;
	private String extKey;
	private String extAgtIp;
	private String txAmp;
	private String volume;
	private String agc;
	private String recYn;
	private String logYn;
	private String screenYn;
	private String chTenant;
	private String recType;
	private String sChNum;
	private String eChNum;
	private String oldSysCode;
	private String grpFlag;
	private String recSum;
	private String recSize;
	private String recCount;
	private String recDate;
	private String recApkey;
	private String recApkeySize;
	
	private String userId;
	private String userName;
	private String mgCode;
	private String sgCode;
	
	private String switchChNum;
	
	public String getSwitchChNum() {
		return switchChNum;
	}
	public void setSwitchChNum(String switchChNum) {
		this.switchChNum = switchChNum;
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
	public String getRecApkeySize() {
		return recApkeySize;
	}
	public void setRecApkeySize(String recApkeySize) {
		this.recApkeySize = recApkeySize;
	}
	public String getRecApkey() {
		return recApkey;
	}
	public void setRecApkey(String recApkey) {
		this.recApkey = recApkey;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecSum() {
		return recSum;
	}
	public void setRecSum(String recSum) {
		this.recSum = recSum;
	}
	public String getRecSize() {
		return recSize;
	}
	public void setRecSize(String recSize) {
		this.recSize = recSize;
	}
	public String getRecCount() {
		return recCount;
	}
	public void setRecCount(String recCount) {
		this.recCount = recCount;
	}
	public Integer getChNum() {
		return chNum;
	}
	public void setChNum(Integer chNum) {
		this.chNum = chNum;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getExtNum() {
		return extNum;
	}
	public void setExtNum(String extNum) {
		this.extNum = extNum;
	}
	public String getExtKind() {
		return extKind;
	}
	public void setExtKind(String extKind) {
		this.extKind = extKind;
	}
	public String getExtIp() {
		return extIp;
	}
	public void setExtIp(String extIp) {
		this.extIp = extIp;
	}
	public String getExtMac() {
		return extMac;
	}
	public void setExtMac(String extMac) {
		this.extMac = extMac;
	}
	public String getRxAmp() {
		return rxAmp;
	}
	public void setRxAmp(String rxAmp) {
		this.rxAmp = rxAmp;
	}
	public String getExtKey() {
		return extKey;
	}
	public void setExtKey(String extKey) {
		this.extKey = extKey;
	}
	public String getExtAgtIp() {
		return extAgtIp;
	}
	public void setExtAgtIp(String extAgtIp) {
		this.extAgtIp = extAgtIp;
	}
	public String getTxAmp() {
		return txAmp;
	}
	public void setTxAmp(String txAmp) {
		this.txAmp = txAmp;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getAgc() {
		return agc;
	}
	public void setAgc(String agc) {
		this.agc = agc;
	}
	public String getRecYn() {
		return recYn;
	}
	public void setRecYn(String recYn) {
		this.recYn = recYn;
	}
	public String getLogYn() {
		return logYn;
	}
	public void setLogYn(String logYn) {
		this.logYn = logYn;
	}
	public String getScreenYn() {
		return screenYn;
	}
	public void setScreenYn(String screenYn) {
		this.screenYn = screenYn;
	}
	public String getChTenant() {
		return chTenant;
	}
	public void setChTenant(String chTenant) {
		this.chTenant = chTenant;
	}
	public String getRecType() {
		return recType;
	}
	public void setRecType(String recType) {
		this.recType = recType;
	}
	public String getsChNum() {
		return sChNum;
	}
	public void setsChNum(String sChNum) {
		this.sChNum = sChNum;
	}
	public String geteChNum() {
		return eChNum;
	}
	public void seteChNum(String eChNum) {
		this.eChNum = eChNum;
	}

	public String getOldSysCode() {
		return oldSysCode;
	}
	public void setOldSysCode(String oldSysCode) {
		this.oldSysCode = oldSysCode;
	}

	public String getGrpFlag() {
		return grpFlag;
	}
	public void setGrpFlag(String grpFlag) {
		this.grpFlag = grpFlag;
	}

	
	@Override
	public String toString() {
		return "ChannelInfo [chNum=" + chNum + ", sysCode=" + sysCode + ", extNum=" + extNum + ", extKind=" + extKind
				+ ", extIp=" + extIp + ", extMac=" + extMac + ", rxAmp=" + rxAmp + ", extKey=" + extKey + ", extAgtIp="
				+ extAgtIp + ", txAmp=" + txAmp + ", volume=" + volume + ", agc=" + agc + ", recYn=" + recYn
				+ ", logYn=" + logYn + ", screenYn=" + screenYn + ", chTenant=" + chTenant + ", recType=" + recType
				+ ", sChNum=" + sChNum + ", eChNum=" + eChNum + ", oldSysCode=" + oldSysCode + ", grpFlag=" + grpFlag
				+ ", recSum=" + recSum + ", recSize=" + recSize + ", recCount=" + recCount + ", recDate=" + recDate
				+ ", recApkey=" + recApkey + ", recApkeySize=" + recApkeySize + "]";
	}
	public String toLogString(MessageSource messageSource) {
		
		return 	messageSource.getMessage("log.ChannelInfo.ChannelInfo",null,Locale.getDefault()) + " [ "  
				+ (" "+messageSource.getMessage("log.ChannelInfo.chNum",null,Locale.getDefault())+"=" +chNum+ " ") 
				+ (oldSysCode != 		null ? " "+messageSource.getMessage("log.ChannelInfo.oldSysCode",null,Locale.getDefault())+"=" 				+oldSysCode+" "		: "")
				+ (sysCode != 			null ? " "+messageSource.getMessage("log.ChannelInfo.sysCode",null,Locale.getDefault())+"=" 				+sysCode+" " 		: "") 
				+ (extNum != 			null ? " "+messageSource.getMessage("log.ChannelInfo.extNum",null,Locale.getDefault())+"=" 					+extNum+" " 		: "")
				+ (extIp != 			null ? " "+messageSource.getMessage("log.ChannelInfo.extIp",null,Locale.getDefault())+"=" 					+extIp+" " 			: "")
				+ (recYn != 			null ? " "+messageSource.getMessage("log.ChannelInfo.recYn",null,Locale.getDefault())+"=" 					+recYn+" " 			: "") 	
				+ (extKind != 			null ? " "+messageSource.getMessage("log.ChannelInfo.extKind",null,Locale.getDefault())+"=" 				+extKind+" " 		: "")
				+ (recType != 			null ? " "+messageSource.getMessage("log.ChannelInfo.recType",null,Locale.getDefault())+"=" 				+recType+" " 		: "")
				+ (screenYn !=			null ? " "+messageSource.getMessage("log.ChannelInfo.screenYn",null,Locale.getDefault())+"="				+screenYn+ " " 		: "")
				+ "]";																								
	}
	
}
