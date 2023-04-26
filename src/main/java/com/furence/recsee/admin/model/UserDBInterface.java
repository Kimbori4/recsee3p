package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class UserDBInterface {
	
	private String seq;
	private String rDbIp;
	private String rDbUser;
	private String rDbPwd;
	private String rDbPort;
	private String rDbName;
	private String rTeamCode;
	private String rTeamName;
	private String rHh;
	private String rFlag;
	private String rFlagDate;
	private String rCompleteDate;
	private String rCompleteTime;

	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getrDbIp() {
		return rDbIp;
	}
	public String getrDbUser() {
		return rDbUser;
	}
	public String getrDbPwd() {
		return rDbPwd;
	}
	public String getrDbPort() {
		return rDbPort;
	}
	public String getrDbName() {
		return rDbName;
	}
	public String getrTeamCode() {
		return rTeamCode;
	}
	public String getrTeamName() {
		return rTeamName;
	}
	public String getrHh() {
		return rHh;
	}
	public String getrFlag() {
		return rFlag;
	}
	public void setrFlag(String rFlag) {
		this.rFlag = rFlag;
	}
	public void setrDbIp(String rDbIp) {
		this.rDbIp = rDbIp;
	}
	public void setrDbUser(String rDbUser) {
		this.rDbUser = rDbUser;
	}
	public void setrDbPwd(String rDbPwd) {
		this.rDbPwd = rDbPwd;
	}
	public void setrDbPort(String rDbPort) {
		this.rDbPort = rDbPort;
	}
	public void setrDbName(String rDbName) {
		this.rDbName = rDbName;
	}
	public void setrTeamCode(String rTeamCode) {
		this.rTeamCode = rTeamCode;
	}
	public void setrTeamName(String rTeamName) {
		this.rTeamName = rTeamName;
	}
	public void setrHh(String rHh) {
		this.rHh = rHh;
	}
	public String getrFlagDate() {
		return rFlagDate;
	}
	public void setrFlagDate(String rFlagDate) {
		this.rFlagDate = rFlagDate;
	}
	public String getrCompleteDate() {
		return rCompleteDate;
	}
	public String getrCompleteTime() {
		return rCompleteTime;
	}
	public void setrCompleteDate(String rCompleteDate) {
		this.rCompleteDate = rCompleteDate;
	}
	public void setrCompleteTime(String rCompleteTime) {
		this.rCompleteTime = rCompleteTime;
	}
	
	@Override
	public String toString() {
		return "UserDBInterface [seq=" + seq + ",rDbIp=" + rDbIp + ", rDbUser=" + rDbUser
				+ ", rDbPwd=" + rDbPwd + ", rDbPort=" + rDbPort + ", rDbName=" + rDbName
				+ ", rTeamCode=" + rTeamCode + ", rTeamName=" + rTeamName
				+ ", rHh=" + rHh + ", rFlag=" + rFlag + ", rFlagDate=" + rFlagDate
				+ ", rCompleteDate=" + rCompleteDate + ", rCompleteTime=" + rCompleteTime + "]";
	}
	public String toLogString(MessageSource messageSource) {
		
		return 	messageSource.getMessage("log.UserDBInterface.UserDBInterface",null,Locale.getDefault())+" [ "  
				+ (seq != 				null ? " "+messageSource.getMessage("log.UserDBInterface.seq",null,Locale.getDefault())+"=" 			+seq+" "			: "")
				+ (rDbIp != 			null ? " "+messageSource.getMessage("log.UserDBInterface.rDbIp",null,Locale.getDefault())+"=" 			+rDbIp+" "			: "")
				+ (rDbUser != 			null ? " "+messageSource.getMessage("log.UserDBInterface.rDbUser",null,Locale.getDefault())+"=" 		+rDbUser+" " 		: "") 
				+ (rDbPwd != 			null ? " "+messageSource.getMessage("log.UserDBInterface.rDbPwd",null,Locale.getDefault())+"=" 			+rDbPwd+" " 		: "")
				+ (rDbPort != 			null ? " "+messageSource.getMessage("log.UserDBInterface.rDbPort",null,Locale.getDefault())+"=" 		+rDbPort+" " 		: "")
				+ (rDbName !=			null ? " "+messageSource.getMessage("log.UserDBInterface.rDbName",null,Locale.getDefault())+"="			+rDbName+ " " 		: "")
				+ (rTeamCode !=			null ? " "+messageSource.getMessage("log.UserDBInterface.rTeamCode",null,Locale.getDefault())+"="		+rTeamCode+ " " 	: "")
				+ (rTeamName !=			null ? " "+messageSource.getMessage("log.UserDBInterface.rTeamName",null,Locale.getDefault())+"="		+rTeamName+ " " 	: "")
				+ (rHh !=				null ? " "+messageSource.getMessage("log.UserDBInterface.rHh",null,Locale.getDefault())+"="				+rHh+ " " 			: "")
				+ (rFlag !=				null ? " "+messageSource.getMessage("log.UserDBInterface.rFlag",null,Locale.getDefault())+"="			+rFlag+ " " 		: "")
				+ (rFlagDate !=			null ? " "+messageSource.getMessage("log.UserDBInterface.rDate",null,Locale.getDefault())+"="			+rFlagDate+ " " 	: "")
				+ (rCompleteDate !=		null ? " "+messageSource.getMessage("log.UserDBInterface.rCompleteDate",null,Locale.getDefault())+"="			+rCompleteDate+ " " : "")
				+ (rCompleteTime !=		null ? " "+messageSource.getMessage("log.UserDBInterface.rCompleteTime",null,Locale.getDefault())+"="			+rCompleteTime+ " " : "")
				+ "]";																								
	}
	
}
