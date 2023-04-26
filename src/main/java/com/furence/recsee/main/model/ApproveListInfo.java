package com.furence.recsee.main.model;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.furence.recsee.common.util.StringUtil;
import com.initech.safedb.SimpleSafeDB;
import com.initech.safedb.sdk.exception.SafeDBSDKException;

public class ApproveListInfo {
	
	private String reqDate;
	private String reqTime;
	private String userId;
	private String userName;
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String sgName;
	private String approveType;
	private String approveReason;
	private String approveDay;
	private String fileName;
	private String recExt;
	private String recDate;
	private String recTime;
	private String approveState;
	private String prereciptDate;
	private String prereciptTime;
	private String prereciptId;
	private String reciptDate;
	private String reciptTime;
	private String reciptId;
	private String approveDate;
	private String approveTime;
	private String approveId;
	
	private String reqsDate;
	private String reqeDate;
	private String reqsTime;
	private String reqeTime;
	
	
	private String reciptYn;
	private String approveYn;
	private String listenUrl;
	private String preReciptYn;
	
	private String custName;
	private String custPhone1;
	private String extNum;
	private String callKind1;
	private String stockNo;
	private String userIdCall;
	private String userNameCall;
	
	private String fileColumnValue;
	private String fileColumnKey;
	
	public String getUserIdCall() {
		return userIdCall;
	}
	public void setUserIdCall(String userIdCall) {
		this.userIdCall = userIdCall;
	}
	public String getUserNameCall() {
		return userNameCall;
	}
	public void setUserNameCall(String userNameCall) {
		this.userNameCall = userNameCall;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustPhone1() {
		return custPhone1;
	}
	public void setCustPhone1(String custPhone1) {
		this.custPhone1 = custPhone1;
	}
	public String getExtNum() {
		return extNum;
	}
	public void setExtNum(String extNum) {
		this.extNum = extNum;
	}
	public String getCallKind1() {
		return callKind1;
	}
	public void setCallKind1(String callKind1) {
		this.callKind1 = callKind1;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getListenUrl() {
		return listenUrl;
	}
	public void setListenUrl(String listenUrl) {
		this.listenUrl = listenUrl;
	}
	public String getPrereciptDate() {
		String tmpDate = null;
		if(prereciptDate != null && prereciptDate.length() == 8)	
			tmpDate = String.format("%s-%s-%s", prereciptDate.substring(0, 4), prereciptDate.substring(4,  6), prereciptDate.substring(6, 8));
		return tmpDate;
	}
	public void setPrereciptDate(String prereciptDate) {
		this.prereciptDate = prereciptDate;
	}
	public String getPreReciptYn() {
		return preReciptYn;
	}
	public void setPreReciptYn(String preReciptYn) {
		this.preReciptYn = preReciptYn;
	}
	public String getPrereciptTime() {
		String tmpTime = null;
		if(prereciptTime != null  && prereciptTime.length() == 6)
			tmpTime = String.format("%s:%s:%s", prereciptTime.substring(0, 2), prereciptTime.substring(2,  4), prereciptTime.substring(4, 6));
		return tmpTime;
	}
	public void setPrereciptTime(String prereciptTime) {
		this.prereciptTime = prereciptTime;
	}
	public String getPrereciptId() {
		return prereciptId;
	}
	public void setPrereciptId(String prereciptId) {
		this.prereciptId = prereciptId;
	}
	private String checkYn;

	public String getvRecIp() {
		return vRecIp;
	}
	public void setvRecIp(String vRecIp) {
		this.vRecIp = vRecIp;
	}
	public String getvRecFullpath() {
		return vRecFullpath;
	}
	public void setvRecFullpath(String vRecFullpath) {
		this.vRecFullpath = vRecFullpath;
	}
	private String vRecIp;
	private String vRecFullpath;
	
	private List<HashMap<String, String>> authyInfo;
	
	public String getReqDate() {
		String tmpDate = null;
		if(reqDate != null && reqDate.length() == 8)	
			tmpDate = String.format("%s-%s-%s", reqDate.substring(0, 4), reqDate.substring(4,  6), reqDate.substring(6, 8));
		return tmpDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getReqTime() {
		String tmpTime = null;
		if(reqTime != null  && reqTime.length() == 6)
			tmpTime = String.format("%s:%s:%s", reqTime.substring(0, 2), reqTime.substring(2,  4), reqTime.substring(4, 6));
		return tmpTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
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
	public String getSgCode() {
		return sgCode;
	}
	public void setSgCode(String sgCode) {
		this.sgCode = sgCode;
	}
	public String getSgName() {
		return sgName;
	}
	public void setSgName(String sgName) {
		this.sgName = sgName;
	}
	public String getApproveType() {
		return approveType;
	}
	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}
	public String getApproveReason() {
		return approveReason;
	}
	public void setApproveReason(String approveReason) {
		this.approveReason = approveReason;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRecExt() {
		return recExt;
	}
	public void setRecExt(String recExt) {
		this.recExt = recExt;
	}
	public String getRecDate() {
		String tmpDate = null;
		if(recDate != null && recDate.length() == 8)	
			tmpDate = String.format("%s-%s-%s", recDate.substring(0, 4), recDate.substring(4,  6), recDate.substring(6, 8));
		return tmpDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecTime() {
		String tmpTime = null;
		if(recTime != null  && recTime.length() == 6)
			tmpTime = String.format("%s:%s:%s", recTime.substring(0, 2), recTime.substring(2,  4), recTime.substring(4, 6));
		return tmpTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public String getApproveState() {
		return approveState;
	}
	public void setApproveState(String approveState) {
		this.approveState = approveState;
	}
	public String getReciptDate() {
		String tmpDate = null;
		if(reciptDate != null && reciptDate.length() == 8)	
			tmpDate = String.format("%s-%s-%s", reciptDate.substring(0, 4), reciptDate.substring(4,  6), reciptDate.substring(6, 8));
		return tmpDate;
	}
	public void setReciptDate(String reciptDate) {
		this.reciptDate = reciptDate;
	}
	public String getReciptTime() {
		String tmpTime = null;
		if(reciptTime != null  && reciptTime.length() == 6)
			tmpTime = String.format("%s:%s:%s", reciptTime.substring(0, 2), reciptTime.substring(2,  4), reciptTime.substring(4, 6));
		return tmpTime;
	}
	public void setReciptTime(String reciptTime) {
		this.reciptTime = reciptTime;
	}
	public String getReciptId() {
		return reciptId;
	}
	public void setReciptId(String reciptId) {
		this.reciptId = reciptId;
	}
	public String getApproveDate() {
		String tmpDate = null;
		if(approveDate != null && approveDate.length() == 8)	
			tmpDate = String.format("%s-%s-%s", approveDate.substring(0, 4), approveDate.substring(4,  6), approveDate.substring(6, 8));
		return tmpDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getApproveTime() {
		String tmpTime = null;
		if(approveTime != null  && approveTime.length() == 6)
			tmpTime = String.format("%s:%s:%s", approveTime.substring(0, 2), approveTime.substring(2,  4), approveTime.substring(4, 6));
		return tmpTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	public String getApproveId() {
		return approveId;
	}
	public void setApproveId(String approveId) {
		this.approveId = approveId;
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
	public String getReqsDate() {
		return reqsDate;
	}
	public void setReqsDate(String reqsDate) {
		this.reqsDate = reqsDate;
	}
	public String getReqeDate() {
		return reqeDate;
	}
	public void setReqeDate(String reqeDate) {
		this.reqeDate = reqeDate;
	}
	public String getReqsTime() {
		return reqsTime;
	}
	public void setReqsTime(String reqsTime) {
		this.reqsTime = reqsTime;
	}
	public String getReqeTime() {
		return reqeTime;
	}
	public void setReqeTime(String reqeTime) {
		this.reqeTime = reqeTime;
	}
	public String getApproveDay() {
		return approveDay;
	}
	public void setApproveDay(String approveDay) {
		this.approveDay = approveDay;
	}
	public String getReciptYn() {
		return reciptYn;
	}
	public void setReciptYn(String reciptYn) {
		this.reciptYn = reciptYn;
	}
	public String getApproveYn() {
		return approveYn;
	}
	public void setApproveYn(String approveYn) {
		this.approveYn = approveYn;
	}
	public List<HashMap<String, String>> getAuthyInfo() {
		return authyInfo;
	}
	public void setAuthyInfo(List<HashMap<String, String>> authyInfo) {
		this.authyInfo = authyInfo;
	}
	public String getCheckYn() {
		return checkYn;
	}
	public void setCheckYn(String checkYn) {
		this.checkYn = checkYn;
	}		
	public String getFileColumnValue() {
		return fileColumnValue;
	}
	public void setFileColumnValue(String fileColumnValue) {
		this.fileColumnValue = fileColumnValue;
	}
	public String getFileColumnKey() {
		return fileColumnKey;
	}
	public void setFileColumnKey(String fileColumnKey) {
		this.fileColumnKey = fileColumnKey;
	}
	public String SafeDBGetter(String txt) {
		SimpleSafeDB ssdb = SimpleSafeDB.getInstance();
		Logger logger = Logger.getLogger(getClass());
		String tempStrValue = "";
		
		String safedbuserid = "shlsafe";
		//String tablename = "SHLOWN.BC_ASPRMDRG";
		String tablename = "CALL.ENC_POLICY";
		//String columnname = "AGNT_RDRE_NO";
		String columnname = "RDRE_NO";
		
		try {
			if(ssdb.login()) {
				
				if(!StringUtil.isNull(txt,true)) {
					byte res[] = ssdb.decrypt(safedbuserid,tablename,columnname, txt.getBytes());
					logger.info("LOG4 SSDB DECRYPT  :  "+new String(res));
					tempStrValue = new String(res);
				}
			}else {
				tempStrValue = "";
			}
		} catch (SafeDBSDKException e) {
			logger.info("LOG4 erororor  :  "+e);
		}
		return tempStrValue;
	}
}
