package com.furence.recsee.main.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.furence.recsee.common.util.ConvertUtil;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.sqlFilterUtil;
import com.initech.safedb.SimpleSafeDB;
import com.initech.safedb.sdk.exception.SafeDBSDKException;

public class SearchListInfo {

	private String recDate;
	private String recRtime;
	private String recTime;
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String userId;
	private String chNum;
	private String userName;
	private String callId1;
	private String callId2;
	private String callId3;
	private String extNum;
	private String custName;
	private String custPhone1;
	private String custPhone2;
	private String custPhone3;
	private String callKind1;
	private String callKind2;
	private String callStime;
	private String callEtime;
	private String callTtime;
	private String selfDisYn;
	private String vSysCode;
	private String vHddFlag;
	private String listenUrl;
	private String vFileName;
	private String sSysCode;
	private String sHddFlag;
	private String screenUrl;
	private String recVisible;
	private String sFileName;
	private String sUploadYn;
	private String tSysCode;
	private String recMemo;
	private String tHddFlag;
	private String evalYn;
	private String textUrl;
	private String listenYn;
	private String tFileName;
	private String tUploadYn;
	private String partStart;
	private String partEnd;
	private String marking1;
	private String marking2;
	private String marking3;
	private String marking4;
	private String custSocialNum;
	private String contractNum;
	private String counselCode;
	private String counselContent;
	private String custAddress;
	private String playerKind;
	private String tContents;
	private String receiptNum;
	private String buffer1;
	private String buffer2;
	private String buffer3;
	private String bwYn;
	private String bwBgCode;
	private String bwSgCode;
	private String screenDualUrl;
	private String buffer4;
	private String buffer5;
	private String buffer6;
	private String buffer7;
	private String buffer8;
	private String buffer9;
	private String buffer10;
	private String buffer11;
	private String buffer12;
	private String buffer13;
	private String buffer14;
	private String buffer15;
	private String buffer16;
	private String buffer17;
	private String buffer18;
	private String buffer19;
	private String buffer20;
	private String cnId;
	private String destIp;
	private String vdoUrl;
	private String custId;
	private String regiDate;
	private String manager;
	private String sRtp;
	private String fKey;
	private String sSrc;
	private String rKey;
	private String coNum;
	private String didNum;
	private String ueiData;
	private String queueNo1;
	private String queueNo2;
	private String recStartType;

	private String vsendFileFlag;
	private String ssendFileFlag;

	private String stockNo;

	private String vRecIp;
	private String vRecFullpath;
	private String vReadIp;
	private String vReadFullpath;
	private String sRecIp;
	private String sRecFullpath;
	private String sReadIp;
	private String sReadFullpath;

	private String counselResultBgcode;
	private String counselResultMgcode;
	private String counselResultSgcode;

	private String sDate;
	private String eDate;
	private String sRtime;
	private String eRtime;
	private String sTime;
	private String eTime;
	private String sTimeConnect;
	private String eTimeConnect;
	private String sStime;
	private String eStime;
	private String sEtime;
	private String eEtime;
	private String sTtime;
	private String eTtime;
	private String sTtimeConnect;
	private String eTtimeConnect;
	private String loginUserId;
	
	private String sttPlayer;

	private String bgCodeM;
	private String mgCodeM;
	private String sgCodeM;
	private String userIdM;

	private String ivrChk;
	private String affilicateName;

	private List<String> recDateArr;
	private List<String> recTimeArr;
	private List<String> recExtArr;
	
	private String url;
	private String cmd;
	private String mStartTime;
	private String mEndTime;
	private String cStartTime;
	private String cEndTime;
	private String totalLength;
	private String encYn;

	private ArrayList<String> recKeyword;
	private ArrayList<String> recCategory;
	private ArrayList<String> recCallType;
	private String recText;
	private String recSummary;
	private String keywordName;
	
	private String logListen;
	private String listenDate;
	private String listenTime;

	private String custEncryptDate;
	private String screenDaulUrl;
	
	private String recDivision;

	private String rCustPhoneAp;
	
	private String companyTelno;
	private String companyTelnoNick;
	
	private String sttResult;
	
	private ArrayList<String> custPhoneTelNo;
	private ArrayList<String> listenUrlList;
	private ArrayList<String> callKeyApList;
	
	private String recDateLimit;
	

	private String rCallCount;
	private String rCallTtime;

	private String plusType;
	private String userIdPlus;
	
	private String fileNameChange;
	
	private String item;
	
	private String urlEncYn;
	
	private String errorYn;
	private String partRecYn;
	private String rPdtNm;
	private String rCustInfo1;
	private String rCustInfo2;
	private String rCustInfo3;
	private String scriptStepName;
	private String scriptRecState;
	private String retryReason;
	private String retryReasonDetail;
	private String retryCount;
	
	private String rRecKey;
	private String rProductType;
	
	private String rectryOm;
	
	private int tDepth;
	private int tKey;
	private int tParent;
	
	private String riskNm;
	
	private int moreProductYn; 
	
	private String server;
	

	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getMoreProductYn() {
		return moreProductYn;
	}
	public void setMoreProductYn(int moreProductYn) {
		this.moreProductYn = moreProductYn;
	}
	public String getRiskNm() {
		return riskNm;
	}
	public void setRiskNm(String riskNm) {
		this.riskNm = riskNm;
	}
	public int gettKey() {
		return tKey;
	}
	public void settKey(int tKey) {
		this.tKey = tKey;
	}
	public int gettParent() {
		return tParent;
	}
	public void settParent(int tParent) {
		this.tParent = tParent;
	}
	public int gettDepth() {
		return tDepth;
	}
	public void settDepth(int tDepth) {
		this.tDepth = tDepth;
	}
	public String getRectryOm() {
		return rectryOm;
	}
	public void setRectryOm(String rectryOm) {
		this.rectryOm = rectryOm;
	}
	public String getrProductType() {
		return rProductType;
	}
	public void setrProductType(String rProductType) {
		this.rProductType = rProductType;
	}
	public String getrRecKey() {
		return rRecKey;
	}
	public void setrRecKey(String rRecKey) {
		this.rRecKey = rRecKey;
	}
	public String getErrorYn() {
		return errorYn;
	}
	public void setErrorYn(String errorYn) {
		this.errorYn = errorYn;
	}
	public String getPartRecYn() {
		return partRecYn;
	}
	public void setPartRecYn(String partRecYn) {
		this.partRecYn = partRecYn;
	}
	
	public String getrPdtNm() {
		return rPdtNm;
	}
	public void setrPdtNm(String rPdtNm) {
		this.rPdtNm = rPdtNm;
	}
	public String getrCustInfo1() {
		return rCustInfo1;
	}
	public void setrCustInfo1(String rCustInfo1) {
		this.rCustInfo1 = rCustInfo1;
	}
	public String getrCustInfo2() {
		return rCustInfo2;
	}
	public void setrCustInfo2(String rCustInfo2) {
		this.rCustInfo2 = rCustInfo2;
	}
	public String getrCustInfo3() {
		return rCustInfo3;
	}
	public void setrCustInfo3(String rCustInfo3) {
		this.rCustInfo3 = rCustInfo3;
	}
	public String getScriptStepName() {
		return scriptStepName;
	}
	public void setScriptStepName(String scriptStepName) {
		this.scriptStepName = scriptStepName;
	}
	public String getScriptRecState() {
		return scriptRecState;
	}
	public void setScriptRecState(String scriptRecState) {
		this.scriptRecState = scriptRecState;
	}
	public String getRetryReason() {
		return retryReason;
	}
	public void setRetryReason(String retryReason) {
		this.retryReason = retryReason;
	}
	public String getRetryReasonDetail() {
		return retryReasonDetail;
	}
	public void setRetryReasonDetail(String retryReasonDetail) {
		this.retryReasonDetail = retryReasonDetail;
	}
	
	public String getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(String retryCount) {
		this.retryCount = retryCount;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getPlusType() {
		return plusType;
	}
	public void setPlusType(String plusType) {
		this.plusType = plusType;
	}
	public String getUserIdPlus() {
		return userIdPlus;
	}
	public void setUserIdPlus(String userIdPlus) {
		this.userIdPlus = userIdPlus;
	}
	public String getrCallCount() {
		return rCallCount;
	}
	public void setrCallCount(String rCallCount) {
		this.rCallCount = rCallCount;
	}
	public String getrCallTtime() {
		return rCallTtime;
	}
	public void setrCallTtime(String rCallTtime) {
		this.rCallTtime = rCallTtime;
	}
	public String getSttResult() {
		return sttResult;
	}
	public void setSttResult(String sttResult) {
		this.sttResult = sttResult;
	}
	
	private String custPhone1IsEncrypt;
	private String custPhone2IsEncrypt;
	private String custPhone3IsEncrypt;
	private String custPhoneApIsEncrypt;
	private String custSocailNumIsEncrypt;
	private String custNameIsEncrypt;
	private String buffer1IsEncrypt;
	private String buffer2IsEncrypt;
	private String buffer3IsEncrypt;
	private String buffer4IsEncrypt;
	private String buffer5IsEncrypt;
	private String buffer6IsEncrypt;
	private String buffer7IsEncrypt;
	private String buffer8IsEncrypt;
	private String buffer9IsEncrypt;
	private String buffer10IsEncrypt;
	private String buffer11IsEncrypt;
	private String buffer12IsEncrypt;
	private String buffer13IsEncrypt;
	private String buffer14IsEncrypt;
	private String buffer15IsEncrypt;
	
	private String buffer16IsEncrypt;
	private String buffer17IsEncrypt;
	private String buffer18IsEncrypt;
	private String buffer19IsEncrypt;
	private String buffer20IsEncrypt;
	
	private ArrayList<String> updateColumn;
	public String getRecDivision() {
		return recDivision;
	}
	public void setRecDivision(String recDivision) {
		this.recDivision = recDivision;
	}
	public String getScreenDaulUrl() {
		return screenDaulUrl;
	}
	public void setScreenDaulUrl(String screenDaulUrl) {
		this.screenDaulUrl = screenDaulUrl;
	}
	public void setRecCategory(ArrayList<String> recCategory) {
		this.recCategory = recCategory;
	}
	public String getCustEncryptDate() {
		return custEncryptDate;
	}
	public void setCustEncryptDate(String custEncryptDate) {
		this.custEncryptDate = custEncryptDate;
	}
	public String getRecText() {
		return recText;
	}
	public void setRecText(String recText) {
		this.recText = recText;
	}
	public String getListenDate() {
		return listenDate;
	}
	public void setListenDate(String listenDate) {
		this.listenDate = listenDate;
	}
	public String getListenTime() {
		return listenTime;
	}
	public void setListenTime(String listenTime) {
		this.listenTime = listenTime;
	}
	public String getLogListen() {
		return logListen;
	}
	public void setLogListen(String logListen) {
		this.logListen = logListen;
	}
	public String getKeywordName() {
		return keywordName;
	}
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}
	public String getRecSummary() {
		return recSummary;
	}
	public void setRecSummary(String recSummary) {
		this.recSummary = recSummary;
	}
	public ArrayList<String> getRecKeyword() {
		return recKeyword;
	}
	public void setRecKeyword(ArrayList<String> recKeyword) {
		this.recKeyword = recKeyword;
	}
	public ArrayList<String> getRecCategory() {
		return recCategory;
	}
	public void setCategory(ArrayList<String> recCategory) {
		this.recCategory = recCategory;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getmStartTime() {
		return mStartTime;
	}
	public void setmStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}
	public String getmEndTime() {
		return mEndTime;
	}
	public void setmEndTime(String mEndTime) {
		this.mEndTime = mEndTime;
	}
	public String getcStartTime() {
		return cStartTime;
	}
	public void setcStartTime(String cStartTime) {
		this.cStartTime = cStartTime;
	}
	public String getcEndTime() {
		return cEndTime;
	}
	public void setcEndTime(String cEndTime) {
		this.cEndTime = cEndTime;
	}
	public String getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(String totalLength) {
		this.totalLength = totalLength;
	}
	public String getEncYn() {
		return encYn;
	}
	public void setEncYn(String encYn) {
		this.encYn = encYn;
	}
	public String getSttPlayer() {
		return sttPlayer;
	}
	public void setSttPlayer(String sttPlayer) {
		this.sttPlayer = sttPlayer;
	}
	public String getAffilicateName() {
		return affilicateName;
	}
	public void setAffilicateName(String affilicateName) {
		this.affilicateName = affilicateName;
	}
	public String getBgCodeM() {
		return bgCodeM;
	}
	public void setBgCodeM(String bgCodeM) {
		this.bgCodeM = bgCodeM;
	}
	public String getMgCodeM() {
		return mgCodeM;
	}
	public void setMgCodeM(String mgCodeM) {
		this.mgCodeM = mgCodeM;
	}
	public String getSgCodeM() {
		return sgCodeM;
	}
	public void setSgCodeM(String sgCodeM) {
		this.sgCodeM = sgCodeM;
	}
	public String getUserIdM() {
		return userIdM;
	}
	public void setUserIdM(String userIdM) {
		this.userIdM = userIdM;
	}

	private List<HashMap<String, String>> authyInfo;
	private ArrayList<String> sgCodeArray;

	private ArrayList<String> buffer12Array;
	private ArrayList<String> buffer13Array;

	private String topCount;

	private String consentNoRecodingUse;
	private String limitUse;

	private Integer count;
	private Integer posStart;

	private String orderBy;
	private String direction;

	private String dateOrderBy;

	private String timeOrderBy;

	private String memoInfo;
	public String getsTimeConnect() {
		return sTimeConnect;
	}
	public void setsTimeConnect(String sTimeConnect) {
		this.sTimeConnect = sTimeConnect;
	}
	public String geteTimeConnect() {
		return eTimeConnect;
	}
	public void seteTimeConnect(String eTimeConnect) {
		this.eTimeConnect = eTimeConnect;
	}
	public String getsTtimeConnect() {
		return sTtimeConnect;
	}
	public void setsTtimeConnect(String sTtimeConnect) {
		this.sTtimeConnect = sTtimeConnect;
	}
	public String geteTtimeConnect() {
		return eTtimeConnect;
	}
	public void seteTtimeConnect(String eTtimeConnect) {
		this.eTtimeConnect = eTtimeConnect;
	}

	private String tag;

	private String totalCount;

	private String callKeyAp;

	private String rownumber;


	private String recTimeRaw;
	private String recDateRaw;

	private String callStimeConnect;
	private String callTtimeConnect;

	private String rTranscriptStatus;
	
	
	
	public String getrTranscriptStatus() {
		return rTranscriptStatus;
	}
	public void setrTranscriptStatus(String rTranscriptStatus) {
		this.rTranscriptStatus = rTranscriptStatus;
	}
	public String getCallStimeConnect() {
//		String tmpTime = null;
//		if(callStimeConnect != null && callStimeConnect.length()==6) tmpTime = String.format("%s:%s:%s", callStimeConnect.substring(0, 2), callStimeConnect.substring(2,  4), callStimeConnect.substring(4, 6));
//		else
//			tmpTime="";
//		return tmpTime;
		return callStimeConnect;
	}
	public void setCallStimeConnect(String callStimeConnect) {
		this.callStimeConnect = callStimeConnect;
	}
	public String getCallTtimeConnect() {
		return callTtimeConnect;
	}
	public void setCallTtimeConnect(String callTtimeConnect) {
		this.callTtimeConnect = callTtimeConnect;
	}
	public String getRecTimeRaw() {
		return recTimeRaw;
	}
	public void setRecTimeRaw(String recTimeRaw) {
		this.recTimeRaw = recTimeRaw;
	}
	public String getRecDateRaw() {
		return recDateRaw;
	}
	public void setRecDateRaw(String recDateRaw) {
		this.recDateRaw = recDateRaw;
	}
	public String getRownumber() {
		return rownumber;
	}
	public void setRownumber(String rownumber) {
		this.rownumber = rownumber;
	}
	public String getRecDate() {
//		String tmpDate = null;
//		if(recDate != null)	tmpDate = String.format("%s-%s-%s", recDate.substring(0, 4), recDate.substring(4,  6), recDate.substring(6, 8));
//
//		return tmpDate;
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecRtime() {
		return recRtime;
	}
	public void setRecRtime(String recRtime) {
		this.recRtime = recRtime;
	}
	public String getRecTime() {
//		String tmpTime = null;
//		if(recTime != null) tmpTime = String.format("%s:%s:%s", recTime.substring(0, 2), recTime.substring(2,  4), recTime.substring(4, 6));
//
//		return tmpTime;
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChNum() {
		return chNum;
	}
	public void setChNum(String chNum) {
		this.chNum = chNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCallId1() {
		return callId1;
	}
	public void setCallId1(String callId1) {
		this.callId1 = callId1;
	}
	public String getCallId2() {
		return callId2;
	}
	public void setCallId2(String callId2) {
		this.callId2 = callId2;
	}
	public String getCallId3() {
		return callId3;
	}
	public void setCallId3(String callId3) {
		this.callId3 = callId3;
	}
	public String getExtNum() {
		return extNum;
	}
	public void setExtNum(String extNum) {
		this.extNum = extNum;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustPhone1() {
		return this.custPhone1;
	}
	public void setCustPhone1(String custPhone1) {
		this.custPhone1 = custPhone1;
	}
	public String getCustPhone2() {
		return custPhone2;
	}
	public void setCustPhone2(String custPhone2) {
		this.custPhone2 = custPhone2;
	}
	public String getCustPhone3() {
		return custPhone3;
	}
	public void setCustPhone3(String custPhone3) {
		this.custPhone3 = custPhone3;
	}
	public String getCallKind1() {
		return callKind1;
	}
	public void setCallKind1(String callKind1) {
		this.callKind1 = callKind1;
	}
	public String getCallKind2() {
		return callKind2;
	}
	public void setCallKind2(String callKind2) {
		this.callKind2 = callKind2;
	}
	public String getCallStime() {
		return callStime;
	}
	public void setCallStime(String callStime) {
		this.callStime = callStime;
	}
	public String getCallEtime() {
		return callEtime;
	}
	public void setCallEtime(String callEtime) {
		this.callEtime = callEtime;
	}
	public String getCallTtime() {
		return callTtime;
	}
	public void setCallTtime(String callTtime) {
		this.callTtime = callTtime;
	}
	public String getSelfDisYn() {
		return selfDisYn;
	}
	public void setSelfDisYn(String selfDisYn) {
		this.selfDisYn = selfDisYn;
	}
	public String getvSysCode() {
		return vSysCode;
	}
	public void setvSysCode(String vSysCode) {
		this.vSysCode = vSysCode;
	}
	public String getvHddFlag() {
		return vHddFlag;
	}
	public void setvHddFlag(String vHddFlag) {
		this.vHddFlag = vHddFlag;
	}
	public String getListenUrl() {
		return listenUrl;
	}
	public void setListenUrl(String listenUrl) {
		this.listenUrl = listenUrl;
	}
	public String getvFileName() {
		return vFileName;
	}
	public void setvFileName(String vFileName) {
		this.vFileName = vFileName;
	}
	public String getsSysCode() {
		return sSysCode;
	}
	public void setsSysCode(String sSysCode) {
		this.sSysCode = sSysCode;
	}
	public String getsHddFlag() {
		return sHddFlag;
	}
	public void setsHddFlag(String sHddFlag) {
		this.sHddFlag = sHddFlag;
	}
	public String getScreenUrl() {
		return screenUrl;
	}
	public void setScreenUrl(String screenUrl) {
		this.screenUrl = screenUrl;
	}
	public String getRecVisible() {
		return recVisible;
	}
	public void setRecVisible(String recVisible) {
		this.recVisible = recVisible;
	}
	public String getsFileName() {
		return sFileName;
	}
	public void setsFileName(String sFileName) {
		this.sFileName = sFileName;
	}
	public String getsUploadYn() {
		return sUploadYn;
	}
	public void setsUploadYn(String sUploadYn) {
		this.sUploadYn = sUploadYn;
	}
	public String gettSysCode() {
		return tSysCode;
	}
	public void settSysCode(String tSysCode) {
		this.tSysCode = tSysCode;
	}
	public String getRecMemo() {
		return recMemo;
	}
	public void setRecMemo(String recMemo) {
		this.recMemo = recMemo;
	}
	public String gettHddFlag() {
		return tHddFlag;
	}
	public void settHddFlag(String tHddFlag) {
		this.tHddFlag = tHddFlag;
	}
	public String getEvalYn() {
		return evalYn;
	}
	public void setEvalYn(String evalYn) {
		this.evalYn = evalYn;
	}
	public String getTextUrl() {
		return textUrl;
	}
	public void setTextUrl(String textUrl) {
		this.textUrl = textUrl;
	}
	public String getListenYn() {
		return listenYn;
	}
	public void setListenYn(String listenYn) {
		this.listenYn = listenYn;
	}
	public String gettFileName() {
		return tFileName;
	}
	public void settFileName(String tFileName) {
		this.tFileName = tFileName;
	}
	public String gettUploadYn() {
		return tUploadYn;
	}
	public void settUploadYn(String tUploadYn) {
		this.tUploadYn = tUploadYn;
	}
	public String getPartStart() {
		return partStart;
	}
	public void setPartStart(String partStart) {
		this.partStart = partStart;
	}
	public String getPartEnd() {
		return partEnd;
	}
	public void setPartEnd(String partEnd) {
		this.partEnd = partEnd;
	}
	public String getMarking1() {
		return marking1;
	}
	public void setMarking1(String marking1) {
		this.marking1 = marking1;
	}
	public String getMarking2() {
		return marking2;
	}
	public void setMarking2(String marking2) {
		this.marking2 = marking2;
	}
	public String getMarking3() {
		return marking3;
	}
	public void setMarking3(String marking3) {
		this.marking3 = marking3;
	}
	public String getMarking4() {
		return marking4;
	}
	public void setMarking4(String marking4) {
		this.marking4 = marking4;
	}
	public String getCustSocialNum() {
		return custSocialNum;
	}
	public void setCustSocialNum(String custSocialNum) {
		this.custSocialNum = custSocialNum;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getCounselCode() {
		return counselCode;
	}
	public void setCounselCode(String counselCode) {
		this.counselCode = counselCode;
	}
	public String getCounselContent() {
		return counselContent;
	}
	public void setCounselContent(String counselContent) {
		this.counselContent = counselContent;
	}
	public String getCustAddress() {
		return custAddress;
	}
	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}
	public String getPlayerKind() {
		return playerKind;
	}
	public void setPlayerKind(String playerKind) {
		this.playerKind = playerKind;
	}
	public String gettContents() {
		return tContents;
	}
	public void settContents(String tContents) {
		this.tContents = tContents;
	}
	public String getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getBuffer1() {
		return buffer1;
	}
	public void setBuffer1(String buffer1) {
		this.buffer1 = buffer1;
	}
	public String getBuffer2() {
//		String holdTime = null;
//		if(buffer2 != null) holdTime = String.format("%s:%s:%s", buffer2.substring(0, 2), buffer2.substring(2,  4), buffer2.substring(4, 6));		
//		
//		return holdTime;
		return buffer2;
	}
	public void setBuffer2(String buffer2) {
		this.buffer2 = buffer2;
	}
	public String getBuffer3() {
		return buffer3;
	}
	public void setBuffer3(String buffer3) {
		this.buffer3 = buffer3;
	}
	public String getBwYn() {
		return bwYn;
	}
	public void setBwYn(String bwYn) {
		this.bwYn = bwYn;
	}
	public String getBwBgCode() {
		return bwBgCode;
	}
	public void setBwBgCode(String bwBgCode) {
		this.bwBgCode = bwBgCode;
	}
	public String getBwSgCode() {
		return bwSgCode;
	}
	public void setBwSgCode(String bwSgCode) {
		this.bwSgCode = bwSgCode;
	}
	public String getScreenDualUrl() {
		return screenDualUrl;
	}
	public void setScreenDualUrl(String screenDualUrl) {
		this.screenDualUrl = screenDualUrl;
	}
	public String getBuffer4() {
		return buffer4;
	}
	public void setBuffer4(String buffer4) {
		this.buffer4 = buffer4;
	}
	public String getBuffer5() {
		return buffer5;
	}
	public void setBuffer5(String buffer5) {
		this.buffer5 = buffer5;
	}
	public String getBuffer6() {
		return buffer6;
	}
	public void setBuffer6(String buffer6) {
		this.buffer6 = buffer6;
	}
	public String getBuffer7() {
		return buffer7;
	}
	public void setBuffer7(String buffer7) {
		this.buffer7 = buffer7;
	}
	public String getBuffer8() {
		return buffer8;
	}
	public void setBuffer8(String buffer8) {
		this.buffer8 = buffer8;
	}
	public String getBuffer9() {
		return buffer9;
	}
	public void setBuffer9(String buffer9) {
		this.buffer9 = buffer9;
	}
	public String getBuffer10() {
		return buffer10;
	}
	public void setBuffer10(String buffer10) {
		this.buffer10 = buffer10;
	}
	public String getBuffer11() {
		return buffer11;
	}
	public void setBuffer11(String buffer11) {
		this.buffer11 = buffer11;
	}
	public String getBuffer12() {
		return buffer12;
	}
	public void setBuffer12(String buffer12) {
		this.buffer12 = buffer12;
	}
	public String getBuffer13() {
		return buffer13;
	}
	public void setBuffer13(String buffer13) {
		this.buffer13 = buffer13;
	}
	public String getBuffer14() {
		return buffer14;
	}
	public void setBuffer14(String buffer14) {
		this.buffer14 = buffer14;
	}
	public String getBuffer15() {
		return buffer15;
	}
	public void setBuffer15(String buffer15) {
		this.buffer15 = buffer15;
	}
	public String getBuffer16() {
		return buffer16;
	}
	public void setBuffer16(String buffer16) {
		this.buffer16 = buffer16;
	}
	public String getBuffer17() {
		return buffer17;
	}
	public void setBuffer17(String buffer17) {
		this.buffer17 = buffer17;
	}
	public String getBuffer18() {
		return buffer18;
	}
	public void setBuffer18(String buffer18) {
		this.buffer18 = buffer18;
	}
	public String getBuffer19() {
		return buffer19;
	}
	public void setBuffer19(String buffer19) {
		this.buffer19 = buffer19;
	}
	public String getBuffer20() {
		return buffer20;
	}
	public void setBuffer20(String buffer20) {
		this.buffer20 = buffer20;
	}
	public String getCnId() {
		return cnId;
	}
	public void setCnId(String cnId) {
		this.cnId = cnId;
	}
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	public String getVdoUrl() {
		return vdoUrl;
	}
	public void setVdoUrl(String vdoUrl) {
		this.vdoUrl = vdoUrl;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getRegiDate() {
		return regiDate;
	}
	public void setRegiDate(String regiDate) {
		this.regiDate = regiDate;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getsRtp() {
		return sRtp;
	}
	public void setsRtp(String sRtp) {
		this.sRtp = sRtp;
	}
	public String getfKey() {
		return fKey;
	}
	public void setfKey(String fKey) {
		this.fKey = fKey;
	}
	public String getsSrc() {
		return sSrc;
	}
	public void setsSrc(String sSrc) {
		this.sSrc = sSrc;
	}
	public String getrKey() {
		return rKey;
	}
	public void setrKey(String rKey) {
		this.rKey = rKey;
	}
	public String getCoNum() {
		return coNum;
	}
	public void setCoNum(String coNum) {
		this.coNum = coNum;
	}
	public String getDidNum() {
		return didNum;
	}
	public void setDidNum(String didNum) {
		this.didNum = didNum;
	}
	public String getUeiData() {
		return ueiData;
	}
	public void setUeiData(String ueiData) {
		this.ueiData = ueiData;
	}
	public String getQueueNo1() {
		return queueNo1;
	}
	public void setQueueNo1(String queueNo1) {
		this.queueNo1 = queueNo1;
	}
	public String getQueueNo2() {
		return queueNo2;
	}
	public void setQueueNo2(String queueNo2) {
		this.queueNo2 = queueNo2;
	}
	public String getRecStartType() {
		return recStartType;
	}
	public void setRecStartType(String recStartType) {
		this.recStartType = recStartType;
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
	public String getsRtime() {
		return sRtime;
	}
	public void setsRtime(String sRtime) {
		this.sRtime = sRtime;
	}
	public String geteRtime() {
		return eRtime;
	}
	public void seteRtime(String eRtime) {
		this.eRtime = eRtime;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getsStime() {
		return sStime;
	}
	public void setsStime(String sStime) {
		this.sStime = sStime;
	}
	public String geteStime() {
		return eStime;
	}
	public void seteStime(String eStime) {
		this.eStime = eStime;
	}
	public String getsEtime() {
		return sEtime;
	}
	public void setsEtime(String sEtime) {
		this.sEtime = sEtime;
	}
	public String geteEtime() {
		return eEtime;
	}
	public void seteEtime(String eEtime) {
		this.eEtime = eEtime;
	}
	public String getsTtime() {
		return sTtime;
	}
	public void setsTtime(String sTtime) {
		this.sTtime = sTtime;
	}
	public String geteTtime() {
		return eTtime;
	}
	public void seteTtime(String eTtime) {
		this.eTtime = eTtime;
	}
	public List<HashMap<String, String>> getAuthyInfo() {
		return authyInfo;
	}
	public void setAuthyInfo(List<HashMap<String, String>> authyInfo) {
		this.authyInfo = authyInfo;
	}
	public String getTopCount() {
		return topCount;
	}
	public void setTopCount(String topCount) {
		this.topCount = topCount;
	}
	public String getConsentNoRecodingUse() {
		return consentNoRecodingUse;
	}
	public void setConsentNoRecodingUse(String consentNoRecodingUse) {
		this.consentNoRecodingUse = consentNoRecodingUse;
	}
	public String getLimitUse() {
		return limitUse;
	}
	public void setLimitUse(String limitUse) {
		this.limitUse = limitUse;
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
	public String getMemoInfo() {
		return memoInfo;
	}
	public void setMemoInfo(String memoInfo) {
		this.memoInfo = memoInfo;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public ArrayList<String> getSgCodeArray() {
		return sgCodeArray;
	}
	public void setSgCodeArray(ArrayList<String> sgCodeArray) {
		this.sgCodeArray = sgCodeArray;
	}
	public ArrayList<String> getBuffer12Array() {
		return buffer12Array;
	}
	public void setBuffer12Array(ArrayList<String> buffer12Array) {
		this.buffer12Array = buffer12Array;
	}
	public ArrayList<String> getBuffer13Array() {
		return buffer13Array;
	}
	public void setBuffer13Array(ArrayList<String> buffer13Array) {
		this.buffer13Array = buffer13Array;
	}
	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getVsendFileFlag() {
		return vsendFileFlag;
	}
	public void setVsendFileFlag(String vsendFileFlag) {
		this.vsendFileFlag = vsendFileFlag;
	}
	public String getSsendFileFlag() {
		return ssendFileFlag;
	}
	public void setSsendFileFlag(String ssendFileFlag) {
		this.ssendFileFlag = ssendFileFlag;
	}
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
	public String getvReadIp() {
		return vReadIp;
	}
	public void setvReadIp(String vReadIp) {
		this.vReadIp = vReadIp;
	}
	public String getvReadFullpath() {
		return vReadFullpath;
	}
	public void setvReadFullpath(String vReadFullpath) {
		this.vReadFullpath = vReadFullpath;
	}
	public String getsRecIp() {
		return sRecIp;
	}
	public void setsRecIp(String sRecIp) {
		this.sRecIp = sRecIp;
	}
	public String getsRecFullpath() {
		return sRecFullpath;
	}
	public void setsRecFullpath(String sRecFullpath) {
		this.sRecFullpath = sRecFullpath;
	}
	public String getsReadIp() {
		return sReadIp;
	}
	public void setsReadIp(String sReadIp) {
		this.sReadIp = sReadIp;
	}
	public String getsReadFullpath() {
		return sReadFullpath;
	}
	public void setsReadFullpath(String sReadFullpath) {
		this.sReadFullpath = sReadFullpath;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getCounselResultBgcode() {
		return counselResultBgcode;
	}
	public void setCounselResultBgcode(String counselResultBgcode) {
		this.counselResultBgcode = counselResultBgcode;
	}
	public String getCounselResultMgcode() {
		return counselResultMgcode;
	}
	public void setCounselResultMgcode(String counselResultMgcode) {
		this.counselResultMgcode = counselResultMgcode;
	}
	public String getCounselResultSgcode() {
		return counselResultSgcode;
	}
	public void setCounselResultSgcode(String counselResultSgcode) {
		this.counselResultSgcode = counselResultSgcode;
	}
	public String getDateOrderBy() {
		return dateOrderBy;
	}
	public void setDateOrderBy(String dateOrderBy) {
		this.dateOrderBy = dateOrderBy;
	}
	public String getTimeOrderBy() {
		return timeOrderBy;
	}
	public void setTimeOrderBy(String timeOrderBy) {
		this.timeOrderBy = timeOrderBy;
	}
	public String getCallKeyAp() {
		return callKeyAp;
	}
	public void setCallKeyAp(String callKeyAp) {
		this.callKeyAp = callKeyAp;
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
	public String getIvrChk() {
		return ivrChk;
	}
	public void setIvrChk(String ivrChk) {
		this.ivrChk = ivrChk;
	}
	public String getCompanyTelno() {
		return companyTelno;
	}
	public void setCompanyTelno(String companyTelno) {
		this.companyTelno = companyTelno;
	}
	public String getCompanyTelnoNick() {
		return companyTelnoNick;
	}
	public void setCompanyTelnoNick(String companyTelnoNick) {
		this.companyTelnoNick = companyTelnoNick;
	}
	public ArrayList<String> getListenUrlList() {
		return listenUrlList;
	}
	public void setListenUrlList(ArrayList<String> listenUrlList) {
		this.listenUrlList = listenUrlList;
	}
	public ArrayList<String> getRecCallType() {
		return recCallType;
	}
	public void setRecCallType(ArrayList<String> recCallType) {
		this.recCallType = recCallType;
	}
	public ArrayList<String> getCustPhoneTelNo() {
		return custPhoneTelNo;
	}
	public void setCustPhoneTelNo(ArrayList<String> custPhoneTelNo) {
		this.custPhoneTelNo = custPhoneTelNo;
	}
	public ArrayList<String> getCallKeyApList() {
		return callKeyApList;
	}
	public void setCallKeyApList(ArrayList<String> callKeyApList) {
		this.callKeyApList = callKeyApList;
	}	
	
	public String getRecDateLimit() {
		return recDateLimit;
	}
	public void setRecDateLimit(String recDateLimit) {
		this.recDateLimit = recDateLimit;
	}
	
	public String getFileNameChange() {
		return fileNameChange;
	}
	public void setFileNameChange(String fileNameChange) {
		this.fileNameChange = fileNameChange;
	}
	@SuppressWarnings("unchecked")
	public void setParamMap(HttpServletRequest param, String consentNoRecodingUse, String limitUse) {

		if(consentNoRecodingUse != null) this.consentNoRecodingUse = consentNoRecodingUse;
		if(limitUse != null) {
			this.limitUse = limitUse;
		} else {
			this.limitUse = "Y";
		}
		if(param.getParameterMap().size() > 0) {
			
			for (String key : (ArrayList<String>)Collections.list(param.getParameterNames())) {
			
				String value = param.getParameter(key);
				if(StringUtil.isNull(value,true)) continue;

				if(!key.equals("custName"))
					value = sqlFilterUtil.sqlFilter(value);
				else
					value = sqlFilterUtil.sqlFilter2(value);
				switch(ConvertUtil.convert2CamelCase(key)) {
				case "recDate" : this.recDate = value.replace("-", ""); break;
				case "recRtime" : this.recRtime = value.replace(":",  ""); break;
				case "recTime" : this.recTime = value.replace(":", ""); break;
				case "bgCode" : this.bgCode = value; break;
				case "mgCode" : this.mgCode = value; break;
				case "sgCode" : this.sgCode = value; break;
				case "userId" : this.userId = value; break;
				case "chNum" : this.chNum = value; break;
				case "userName" : this.userName = value; break;
				case "callId1" : this.callId1 = value; break;
				case "callId2" : this.callId2 = value; break;
				case "callId3" : this.callId3 = value; break;
				case "extNum" : this.extNum = value; break;
				case "custName" : this.custName = value.replaceAll("%2B", "+").replaceAll("%26","&");
				break;
				case "custPhone1" : this.custPhone1 = value.replace("-", ""); break;
				case "custPhone2" : this.custPhone2 = value.replace("-", ""); break;
				case "custPhone3" : this.custPhone3 = value.replace("-", ""); break;
				case "custPhoneAp" : this.rCustPhoneAp = value.replace("-", ""); break;
				case "callKind1" :
					switch(value) {
					case "T2" :
					case "C2" :
					case "Z2" :
						this.callKind2 = value.substring(0,  1);
						break;
					default :
						this.callKind1 = value;
						break;
					}
					break;
				case "callKind2" : this.callKind2 = value; break;
				case "callStime" : this.callStime = value; break;
				case "callEtime" : this.callEtime = value; break;
				case "callTtime" : this.callTtime = value; break;
				case "selfDisYn" : this.selfDisYn = value; break;
				case "vSysCode" : this.vSysCode = value; break;
				case "vHddFlag" : this.vHddFlag = value; break;
				case "listenUrl" : this.listenUrl = value; break;
				case "vFileName" : this.vFileName = value; break;
				case "sSysCode" : this.sSysCode = value; break;
				case "sHddFlag" : this.sHddFlag = value; break;
				case "screenUrl" : this.screenUrl = value; break;
				case "recVisible" : this.recVisible = value; break;
				case "sFileName" : this.sFileName = value; break;
				case "sUploadYn" : this.sUploadYn = value; break;
				case "tSysCode" : this.tSysCode = value; break;
				case "recMemo" : this.recMemo = value; break;
				case "tHddFlag" : this.tHddFlag = value; break;
				case "evalYn" : this.evalYn = value; break;
				case "textUrl" : this.textUrl = value; break;
				case "listenYn" : this.listenYn = value; break;
				case "tFileName" : this.tFileName = value; break;
				case "tUploadYn" : this.tUploadYn = value; break;
				case "partStart" : this.partStart = value; break;
				case "partEnd" : this.partEnd = value; break;
				case "marking1" : this.marking1 = value; break;
				case "marking2" : this.marking2 = value; break;
				case "marking3" : this.marking3 = value; break;
				case "marking4" : this.marking4 = value; break;
				case "custSocialNum" : this.custSocialNum = value; break;
				case "contractNum" : this.contractNum = value; break;
				case "counselCode" : this.counselCode = value; break;
				case "counselContent" : this.counselContent = value; break;
				case "custAddress" : this.custAddress = value; break;
				case "playerKind" : this.playerKind = value; break;
				case "tContents" : this.tContents = value; break;
				case "receiptNum" : this.receiptNum = value; break;
				case "buffer1" : this.buffer1 = value; break;
				case "buffer2" : this.buffer2 = value; break;
				case "buffer3" : this.buffer3 = value; break;
				case "bwYn" : this.bwYn = value; break;
				case "bwBgCode" : this.bwBgCode = value; break;
				case "bwSgCode" : this.bwSgCode = value; break;
				case "screenDualUrl" : this.screenDualUrl = value; break;
				case "buffer4" : this.buffer4 = value; break;
				case "buffer5" : this.buffer5 = value; break;
				case "buffer6" : this.buffer6 = value; break;
				case "buffer7" : this.buffer7 = value; break;
				case "buffer8" : this.buffer8 = value; break;
				case "buffer9" : this.buffer9 = value; break;
				case "buffer10" : this.buffer10 = value; break;
				case "buffer11" : this.buffer11 = value; break;
				case "buffer12" : this.buffer12 = value; break;
				case "buffer13" : this.buffer13 = value; break;
				case "buffer14" : this.buffer14 = value; break;
				case "buffer15" : this.buffer15 = value; break;
				case "buffer16" : this.buffer16 = value; break;
				case "buffer17" : this.buffer17 = value; break;
				case "buffer18" : this.buffer18 = value; break;
				case "buffer19" : this.buffer19 = value; break;
				case "buffer20" : this.buffer20 = value; break;
				case "cnId" : this.cnId = value; break;
				case "destIp" : this.destIp = value; break;
				case "vdoUrl" : this.vdoUrl = value; break;
				case "custId" : this.custId = value; break;
				case "regiDate" : this.regiDate = value; break;
				case "manager" : this.manager = value; break;
				case "sRtp" : this.sRtp = value; break;
				case "fKey" : this.fKey = value; break;
				case "sSrc" : this.sSrc = value; break;
				case "rKey" : this.rKey = value; break;
				case "coNum" : this.coNum = value; break;
				case "didNum" : this.didNum = value; break;
				case "ueiData" : this.ueiData = value; break;
				case "rQueueNo1" : this.queueNo1 = value; break;
				case "rQueueNo2" : this.queueNo2 = value; break;
				case "recStartType" : this.recStartType = value; break;
				case "sDate" :this.sDate = value.replace("-", ""); break;
				case "eDate" : this.eDate = value.replace("-",  ""); break;
				case "sRtime" : this.sRtime = value.replace(":", ""); break;
				case "eRtime" : this.eRtime = value.replace(":",  ""); break;
				case "sTime" : this.sTime = value.replace(":",  ""); break;
				case "eTime" : this.eTime = value.replace(":",  ""); break;
				case "sStime" : this.sStime = value.replace(":",  ""); break;
				case "eStime" : this.eStime = value.replace(":",  ""); break;
				case "sEtime" : this.sEtime = value.replace(":",  ""); break;
				case "eEtime" : this.eEtime = value.replace(":",  ""); break;
				case "sTtime" : this.sTtime = getTimetoLong(value); break;
				case "eTtime" : this.eTtime = getTimetoLong(value); break;
				case "topCount" : this.topCount = value; break;
				case "count" : this.count = Integer.parseInt(value); break;
				case "posStart" : this.posStart = Integer.parseInt(value); break;
				case "orderBy" : this.orderBy = value; break;
				case "dateOrderBy" : this.dateOrderBy = value; break;
				case "timeOrderBy" : this.timeOrderBy = value; break;
				case "direction" : this.direction = value; break;
				case "memoInfo" : this.memoInfo = value; break;
				case "memo" : this.tag = value; break;
				case "stockNo" : this.stockNo = value; break;
				case "counselResultBgcode" : this.counselResultBgcode = value; break;
				case "counselResultMgcode" : this.counselResultMgcode = value; break;
				case "counselResultSgcode" : this.counselResultSgcode = value; break;
				case "callKeyAp":this.callKeyAp = value ;break;
				case "callStimeConnect;":this.callStimeConnect = value ;break;
				case "callTtimeConnect;":this.callTtimeConnect = value ;break;
				case "sTimeConnect" : this.sTimeConnect = value.replace(":",  ""); break;
				case "eTimeConnect":this.eTimeConnect = value.replace(":",  "") ; break;
				case "sTtimeConnect":this.sTtimeConnect = getTimetoLong(value) ; break;
				case "eTtimeConnect":this.eTtimeConnect = getTimetoLong(value) ; break;
				case "companyTelno" : this.companyTelno = value; break;
				case "companyTelnoNick" : this.companyTelnoNick = value; break;
				case "sttResult" : this.sttResult = value; break;
				}
			}
		}
	}

	public String toLogString() {
		return 	"Search Condition Info [ "
				+ "TotalCount"
				+ (recDate != 			null ? " recDate=" 				+recDate+" " 		: "")
				+ (recRtime != 			null ? " recRtime=" 				+recRtime+" " 		: "")
				+ (recTime != 			null ? " recTime=" 				+recTime+" " 		: "")
				+ (bgCode != 			null ? " bgCode=" 				+bgCode+" " 		: "")
				+ (mgCode != 			null ? " mgCode=" 				+mgCode+" " 		: "")
				+ (sgCode != 			null ? " sgCode=" 				+sgCode+" " 		: "")
				+ (userId != 			null ? " userId=" 			+userId+" " 		: "")
				+ (chNum != 			null ? " chNum=" 				+chNum+" " 			: "")
				+ (userName !=			null ? " userName=" 				+userName+" " 		: "")
				+ (callId1 != 			null ? " callId1=" 				+callId1+" " 		: "")
				+ (callId2 != 			null ? " callId2=" 				+callId2+" " 		: "")
				+ (callId3 != 			null ? " callId3=" 				+callId3+" " 		: "")
				+ (extNum != 			null ? " extNum=" 				+extNum+" " 		: "")
				+ (custName != 			null ? " custName" 				+custName+" " 		: "")
				+ (custPhone1 != 		null ? " custPhone1=" 				+custPhone1+" " 	: "")
				+ (custPhone2 != 		null ? " custPhone2=" 				+custPhone2+" " 	: "")
				+ (custPhone3 !=	 	null ? " custPhone3=" 				+custPhone3+" " 	: "")
				+ (callKind1 != 		null ? " callKind1=" 				+callKind1+" " 		: "")
				+ (callKind2 != 		null ? " callKind2=" 				+callKind2+" " 		: "")
				+ (callStime != 		null ? " callStime=" 			+callStime+" " 		: "")
				+ (callEtime != 		null ? " callEtime="				+callEtime+" " 		: "")
				+ (callTtime != 		null ? " callTtime=" 				+callTtime+" " 		: "")
				+ (selfDisYn != 		null ? " selfDisYn="			+selfDisYn+" " 		: "")
				+ (vSysCode != 			null ? " vSysCode="				+vSysCode+" " 		: "")
				+ (vHddFlag != 			null ? " vHddFlag=" 			+vHddFlag+" " 		: "")
				+ (listenUrl != 		null ? " listenUrl=" 			+listenUrl+" " 		: "")
				+ (vFileName != 		null ? " vFileName="				+vFileName+" " 		: "")
				+ (sSysCode != 			null ? " sSysCode=" 		+sSysCode+" " 		: "")
				+ (sHddFlag != 			null ? " sHddFlag="		+sHddFlag+" " 		: "")
				+ (screenUrl != 		null ? " screenUrl="			+screenUrl+" " 		: "")
/*				+ (recVisible != 		null ? ", 녹취일=" 				+recVisible+" " 	: "") */
				+ (sFileName != 		null ? " sFileName=" 			+sFileName+" " 		: "")
				+ (sUploadYn != 		null ? " sUploadYn=" 		+sUploadYn+" " 		: "")
				+ (tSysCode != 			null ? " tSysCode="		+tSysCode+" " 		: "")
				+ (memoInfo != 			null ? " memoInfo=" 				+memoInfo+" " 		: "")
				+ (tag != 				null ? " tag=" 					+tag+" " 			: "")
				+ (tHddFlag != 			null ? " tHddFlag=" 		+tHddFlag+" " 		: "")
				/*+ (evalYn != 			null ? ", 녹취일=" 				+recDate+" " 		: "") */
				+ (textUrl != 			null ? " textUrl=" 			+textUrl+" " 		: "")
/*				+ (listenYn != 			null ? ", 녹취일=" 				+listenYn+" " 		: "") */
				+ (tFileName != 		null ? " tFileName=" 			+tFileName+" " 		: "")
				+ (tUploadYn != 		null ? " tUploadYn=" 		+tUploadYn+" " 		: "")
				+ (partStart != 		null ? " partStart=" 		+partStart+" " 		: "")
				+ (partEnd != 			null ? " partEnd=" 		+partEnd+" " 		: "")
				+ (marking1 != 			null ? " marking1=" 					+marking1+" " 		: "")
				+ (marking2 != 			null ? " marking2=" 					+marking2+" " 		: "")
				+ (marking3 != 			null ? " marking3=" 					+marking3+" " 		: "")
				+ (marking4 != 			null ? " marking4=" 					+marking4+" " 		: "")
				+ (custSocialNum != 	null ? " custSocialNum=" 			+custSocialNum+" " : "")
				+ (contractNum != 		null ? " contractNum=" 				+contractNum+" " 	: "")
				+ (counselCode != 		null ? " counselCode=" 				+counselCode+" " 	: "")
				+ (counselContent !=	null ? " counselContent=" 				+counselContent+" " : "")
				+ (custAddress != 		null ? " custAddress=" 				+custAddress+" " 	: "")
				+ (playerKind != 		null ? " playerKind=" 			+playerKind+" " 	: "")
				/*+ (tContents != 		null ? ", 녹취일=" 				+recDate+" " 		: "") */
				+ (receiptNum != 		null ? " receiptNum=" 				+receiptNum+" " 	: "")
				+ (buffer1 != 			null ? " buffer1=" 					+buffer1+" " 		: "")
				+ (buffer2 != 			null ? " buffer2=" 					+buffer2+" " 		: "")
				+ (buffer3 != 			null ? " buffer3=" 					+buffer3+" " 		: "")
				+ (bwYn != 				null ? " 베스트&워스트 유무=" 		+bwYn+" " 			: "")
				+ (bwBgCode != 			null ? " 베스트&워스트 대분류 코드=" 	+bwBgCode+" " 		: "")
				+ (bwSgCode != 			null ? " 베스트&워스트 소분류 코드="	+bwSgCode+" " 		: "")
				+ (screenDualUrl != 	null ? " 스크린 듀얼 URL=" 			+screenDualUrl+" " 	: "")
				+ (buffer4 != 			null ? " 예비4=" 					+buffer4+" " 		: "")
				+ (buffer5 != 			null ? " 예비5=" 					+buffer5+" " 		: "")
				+ (buffer6 != 			null ? " 예비6=" 					+buffer6+" " 		: "")
				+ (buffer7 != 			null ? " 예비7=" 					+buffer7+" " 		: "")
				+ (buffer8 != 			null ? " 예비8=" 					+buffer8+" " 		: "")
				+ (buffer9 != 			null ? " 예비9=" 					+buffer9+" " 		: "")
				+ (buffer10 != 			null ? " 예비10=" 				+buffer10+" "		: "")
				+ (buffer11 != 			null ? " 예비11=" 				+buffer11+" " 		: "")
				+ (buffer12 != 			null ? " 예비12=" 				+buffer12+" " 		: "")
				+ (buffer13 != 			null ? " 예비13=" 				+buffer13+" " 		: "")
				+ (buffer14 != 			null ? " 예비14=" 				+buffer14+" " 		: "")
				+ (buffer15 != 			null ? " 예비15=" 				+buffer15+" " 		: "")
				+ (buffer16 != 			null ? " 예비16=" 				+buffer16+" " 		: "")
				+ (buffer17 != 			null ? " 예비17=" 				+buffer17+" " 		: "")
				+ (buffer18 != 			null ? " 예비18=" 				+buffer18+" " 		: "")
				+ (buffer19 != 			null ? " 예비19=" 				+buffer19+" " 		: "")
				+ (buffer20 != 			null ? " 예비20=" 				+buffer20+" " 		: "")
				+ (cnId != 				null ? " CNID=" 				+cnId+" " 			: "")
				+ (destIp != 			null ? " 목적지 IP=" 				+destIp+" " 		: "")
				+ (vdoUrl != 			null ? " VDO 링크=" 				+vdoUrl+" " 		: "")
				+ (custId != 			null ? " 고객 ID=" 				+custId+" " 		: "")
				+ (regiDate != 			null ? " 등록일=" 				+regiDate+" " 		: "")
				+ (manager != 			null ? " 관리자=" 				+manager+" " 		: "")
				+ (sRtp != 				null ? " SRTP 유무=" 				+sRtp+" " 			: "")
				+ (fKey != 				null ? " F 키=" 					+fKey+" " 			: "")
				+ (sSrc != 				null ? " SSRC=" 				+sSrc+" " 			: "")
				+ (rKey != 				null ? " 키=" 					+rKey+" " 			: "")
				+ (coNum != 			null ? " CO 번호=" 				+coNum+" " 			: "")
				+ (didNum != 			null ? " DID 번호=" 				+didNum+" " 		: "")
				+ (ueiData != 			null ? " UEI=" 					+ueiData+" " 		: "")
				+ (queueNo1 != 			null ? " 큐1=" 					+queueNo1+" " 		: "")
				+ (queueNo2 != 			null ? " 큐2=" 					+queueNo2+" " 		: "")
				+ (recStartType !=		null ? " 녹취타입=" 				+recStartType+" " 	: "")
				+ (stockNo !=			null ? " 증권번호=" 				+stockNo+" " 		: "")
				+ (counselResultBgcode !=	null ? " 상담 결과 코드 대=" 				+counselResultBgcode+" " 		: "")
				+ (counselResultMgcode !=	null ? " 상담 결과 코드 중=" 				+counselResultMgcode+" " 		: "")
				+ (counselResultSgcode !=	null ? " 상담 결과 코드 소=" 				+counselResultSgcode+" " 		: "")



				+ (sDate !=				null ? " Date[start]=" 			+sDate+" " 			: "")
				+ (eDate !=				null ? " Date[end]=" 				+eDate+" " 			: "")
				+ (sRtime !=			null ? " Time[start]=" 			+sRtime+" "	 		: "")
				+ (eRtime !=			null ? " Time[end]=" 			+eRtime+" " 		: "")
				+ (sTime !=				null ? " Time[start]=" 			+sTime+" " 			: "")
				+ (eTime !=				null ? " Time[end]=" 			+eTime+" " 			: "")
				+ (sStime !=			null ? " 녹취시작시간[시작]=" 		+sStime+" " 		: "")
				+ (eStime !=			null ? " 녹취종료시간[끝]=" 			+eStime+" " 		: "")
				+ (sEtime !=			null ? " 녹취종료시간[시작]=" 		+sEtime+" " 		: "")
				+ (eEtime !=			null ? " 녹취종료시간[끝]=" 			+eEtime+" " 		: "")
				+ (sTtime !=			null ? " 통화시간[시작]=" 			+sTtime+" " 		: "")
				+ (eTtime !=			null ? " 통화시간[끝]=" 			+eTtime+" " 		: "")
				+ (sTimeConnect !=			null ? " 유효녹취시작[시작]=" 		+sEtime+" " 		: "")
				+ (eTimeConnect !=			null ? " 유효녹취종료시간[끝]=" 			+eEtime+" " 		: "")
				+ (sTtimeConnect !=			null ? " 유효통화시간[시작]=" 			+sTtime+" " 		: "")
				+ (eTtimeConnect !=			null ? " 유효통화시간[끝]=" 			+eTtime+" " 		: "")
				+ (companyTelno != 			null ? " companyTelno=" 					+companyTelno+" " 		: "")
				+ (companyTelnoNick != 			null ? " companyTelnoNick=" 					+companyTelnoNick+" " 		: "")
				+ (sttResult != 			null ? " sttResult=" 					+sttResult+" " 		: "")
				+ "]";

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

		logger.info("LOG4 SSDBLOGIN  :  "+ssdb.login());
		logger.info("LOG4 custPhone1  :  "+txt);
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

	public String SafeDBSetter(String txt) {
		SimpleSafeDB ssdb = SimpleSafeDB.getInstance();
		Logger logger = Logger.getLogger(getClass());
		String tempStrValue = "";

		String safedbuserid = "shlsafe";
		String tablename = "CALL.ENC_POLICY";
		String columnname = "RDRE_NO";

		try {
			if(ssdb.login()) {

				if(!StringUtil.isNull(txt,true)) {
					byte res[] = ssdb.encrypt(safedbuserid,tablename,columnname, txt.getBytes());
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

	public String getTimetoLong(String t) {
		if(t.indexOf(":")<0) {
			return String.valueOf(t);
		}

		String[] k = t.split(":");
		int df = Integer.parseInt(k[0]) * 3600 + Integer.parseInt(k[1]) * 60 + Integer.parseInt(k[2]);

		return String.valueOf(df);
	}
	public String getrCustPhoneAp() {
		return rCustPhoneAp;
	}
	public void setrCustPhoneAp(String rCustPhoneAp) {
		this.rCustPhoneAp = rCustPhoneAp;
	}
	public String getCustPhone1IsEncrypt() {
		return custPhone1IsEncrypt;
	}
	public void setCustPhone1IsEncrypt(String custPhone1IsEncrypt) {
		this.custPhone1IsEncrypt = custPhone1IsEncrypt;
	}
	public String getCustPhone2IsEncrypt() {
		return custPhone2IsEncrypt;
	}
	public void setCustPhone2IsEncrypt(String custPhone2IsEncrypt) {
		this.custPhone2IsEncrypt = custPhone2IsEncrypt;
	}
	public String getCustPhone3IsEncrypt() {
		return custPhone3IsEncrypt;
	}
	public void setCustPhone3IsEncrypt(String custPhone3IsEncrypt) {
		this.custPhone3IsEncrypt = custPhone3IsEncrypt;
	}
	public String getCustPhoneApIsEncrypt() {
		return custPhoneApIsEncrypt;
	}
	public void setCustPhoneApIsEncrypt(String custPhoneApIsEncrypt) {
		this.custPhoneApIsEncrypt = custPhoneApIsEncrypt;
	}
	public String getCustSocailNumIsEncrypt() {
		return custSocailNumIsEncrypt;
	}
	public void setCustSocailNumIsEncrypt(String custSocailNumIsEncrypt) {
		this.custSocailNumIsEncrypt = custSocailNumIsEncrypt;
	}
	public String getCustNameIsEncrypt() {
		return custNameIsEncrypt;
	}
	public void setCustNameIsEncrypt(String custNameIsEncrypt) {
		this.custNameIsEncrypt = custNameIsEncrypt;
	}
	public String getBuffer1IsEncrypt() {
		return buffer1IsEncrypt;
	}
	public void setBuffer1IsEncrypt(String buffer1IsEncrypt) {
		this.buffer1IsEncrypt = buffer1IsEncrypt;
	}
	public String getBuffer2IsEncrypt() {
		return buffer2IsEncrypt;
	}
	public void setBuffer2IsEncrypt(String buffer2IsEncrypt) {
		this.buffer2IsEncrypt = buffer2IsEncrypt;
	}
	public String getBuffer3IsEncrypt() {
		return buffer3IsEncrypt;
	}
	public void setBuffer3IsEncrypt(String buffer3IsEncrypt) {
		this.buffer3IsEncrypt = buffer3IsEncrypt;
	}
	public String getBuffer4IsEncrypt() {
		return buffer4IsEncrypt;
	}
	public void setBuffer4IsEncrypt(String buffer4IsEncrypt) {
		this.buffer4IsEncrypt = buffer4IsEncrypt;
	}
	public String getBuffer5IsEncrypt() {
		return buffer5IsEncrypt;
	}
	public void setBuffer5IsEncrypt(String buffer5IsEncrypt) {
		this.buffer5IsEncrypt = buffer5IsEncrypt;
	}
	public String getBuffer6IsEncrypt() {
		return buffer6IsEncrypt;
	}
	public void setBuffer6IsEncrypt(String buffer6IsEncrypt) {
		this.buffer6IsEncrypt = buffer6IsEncrypt;
	}
	public String getBuffer7IsEncrypt() {
		return buffer7IsEncrypt;
	}
	public void setBuffer7IsEncrypt(String buffer7IsEncrypt) {
		this.buffer7IsEncrypt = buffer7IsEncrypt;
	}
	public String getBuffer8IsEncrypt() {
		return buffer8IsEncrypt;
	}
	public void setBuffer8IsEncrypt(String buffer8IsEncrypt) {
		this.buffer8IsEncrypt = buffer8IsEncrypt;
	}
	public String getBuffer9IsEncrypt() {
		return buffer9IsEncrypt;
	}
	public void setBuffer9IsEncrypt(String buffer9IsEncrypt) {
		this.buffer9IsEncrypt = buffer9IsEncrypt;
	}
	public String getBuffer10IsEncrypt() {
		return buffer10IsEncrypt;
	}
	public void setBuffer10IsEncrypt(String buffer10IsEncrypt) {
		this.buffer10IsEncrypt = buffer10IsEncrypt;
	}
	public String getBuffer11IsEncrypt() {
		return buffer11IsEncrypt;
	}
	public void setBuffer11IsEncrypt(String buffer11IsEncrypt) {
		this.buffer11IsEncrypt = buffer11IsEncrypt;
	}
	public String getBuffer12IsEncrypt() {
		return buffer12IsEncrypt;
	}
	public void setBuffer12IsEncrypt(String buffer12IsEncrypt) {
		this.buffer12IsEncrypt = buffer12IsEncrypt;
	}
	public String getBuffer13IsEncrypt() {
		return buffer13IsEncrypt;
	}
	public void setBuffer13IsEncrypt(String buffer13IsEncrypt) {
		this.buffer13IsEncrypt = buffer13IsEncrypt;
	}
	public String getBuffer14IsEncrypt() {
		return buffer14IsEncrypt;
	}
	public void setBuffer14IsEncrypt(String buffer14IsEncrypt) {
		this.buffer14IsEncrypt = buffer14IsEncrypt;
	}
	public String getBuffer15IsEncrypt() {
		return buffer15IsEncrypt;
	}
	public void setBuffer15IsEncrypt(String buffer15IsEncrypt) {
		this.buffer15IsEncrypt = buffer15IsEncrypt;
	}
	public String getBuffer16IsEncrypt() {
		return buffer16IsEncrypt;
	}
	public void setBuffer16IsEncrypt(String buffer16IsEncrypt) {
		this.buffer16IsEncrypt = buffer16IsEncrypt;
	}
	public String getBuffer17IsEncrypt() {
		return buffer17IsEncrypt;
	}
	public void setBuffer17IsEncrypt(String buffer17IsEncrypt) {
		this.buffer17IsEncrypt = buffer17IsEncrypt;
	}
	public String getBuffer18IsEncrypt() {
		return buffer18IsEncrypt;
	}
	public void setBuffer18IsEncrypt(String buffer18IsEncrypt) {
		this.buffer18IsEncrypt = buffer18IsEncrypt;
	}
	public String getBuffer19IsEncrypt() {
		return buffer19IsEncrypt;
	}
	public void setBuffer19IsEncrypt(String buffer19IsEncrypt) {
		this.buffer19IsEncrypt = buffer19IsEncrypt;
	}
	public String getBuffer20IsEncrypt() {
		return buffer20IsEncrypt;
	}
	public void setBuffer20IsEncrypt(String buffer20IsEncrypt) {
		this.buffer20IsEncrypt = buffer20IsEncrypt;
	}	
	
	public ArrayList<String> getUpdateColumn() {
		return updateColumn;
	}
	public void setUpdateColumn(ArrayList<String> updateColumn) {
		this.updateColumn = updateColumn;
	}
	public String getUrlEncYn() {
		return urlEncYn;
	}
	public void setUrlEncYn(String urlEncYn) {
		this.urlEncYn = urlEncYn;
	}
	
	private boolean freeRecFlag;
	private String taState;
	
	

	public String getTaState() {
		return taState;
	}
	public void setTaState(String taState) {
		this.taState = taState;
	}
	public boolean isFreeRecFlag() {
		return freeRecFlag;
	}
	public void setFreeRecFlag(boolean freeRecFlag) {
		this.freeRecFlag = freeRecFlag;
	}
	
	
}
