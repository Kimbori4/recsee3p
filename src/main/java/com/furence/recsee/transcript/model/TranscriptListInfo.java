package com.furence.recsee.transcript.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TranscriptListInfo {
	private String rUserId;
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
	private String rTranscriptYn;
	private String rTranscriptStatus;
	private String rTranscriptSerial;
	private String rLocalFile;
	
	private String sDate;
	private String eDate;
	private String sTime;
	private String eTime;
	private String sTimeS;
	private String eTimeS;	
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String userId;
	private String extNum;
	private String userName;
	private String transcript;
	private String sgCodeArray;
	
	
	private String rLearningYn;
	private String rText;
	private String rLearningPer;

	private String listenUrl;
	private String rFilename;
	
	private String transcriber;
	private String fileAudioType;

	private String rSysCode;
	
	private List<String> rTranscriptSerialList;
	
	
	public String getFileAudioType() {
		return fileAudioType;
	}
	public void setFileAudioType(String fileAudioType) {
		this.fileAudioType = fileAudioType;
	}
	public String getTranscriber() {
		return transcriber;
	}
	public void setTranscriber(String transcriber) {
		this.transcriber = transcriber;
	}
	public String getrFilename() {
		return rFilename;
	}
	public void setrFilename(String rFilename) {
		this.rFilename = rFilename;
	}
	public String getListenUrl() {
		return listenUrl;
	}
	public void setListenUrl(String listenUrl) {
		this.listenUrl = listenUrl;
	}
	public String getSgCodeArray() {
		return sgCodeArray;
	}
	public void setSgCodeArray(String sgCodeArray) {
		this.sgCodeArray = sgCodeArray;
	}
	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrRecDate() {
		return rRecDate;
	}
	public void setrRecDate(String rRecDate) {
		this.rRecDate = rRecDate;
	}
	public String getrRecTime() {
		return rRecTime;
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
	public String getrTranscriptYn() {
		return rTranscriptYn;
	}
	public void setrTranscriptYn(String rTranscriptYn) {
		this.rTranscriptYn = rTranscriptYn;
	}
	public String getrTranscriptStatus() {
		return rTranscriptStatus;
	}
	public void setrTranscriptStatus(String rTranscriptStatus) {
		this.rTranscriptStatus = rTranscriptStatus;
	}
	public String getrTranscriptSerial() {
		return rTranscriptSerial;
	}
	public void setrTranscriptSerial(String rTranscriptSerial) {
		this.rTranscriptSerial = rTranscriptSerial;
	}
	public String getrLocalFile() {
		return rLocalFile;
	}
	public void setrLocalFile(String rLocalFile) {
		this.rLocalFile = rLocalFile;
	}
	
	
	public String getTranscript() {
		return transcript;
	}
	public void setTranscript(String transcript) {
		this.transcript = transcript;
	}
	public String getsTimeS() {
		return sTimeS;
	}
	public void setsTimeS(String sTimeS) {
		this.sTimeS = sTimeS;
	}
	public String geteTimeS() {
		return eTimeS;
	}
	public void seteTimeS(String eTimeS) {
		this.eTimeS = eTimeS;
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
	public String getExtNum() {
		return extNum;
	}
	public void setExtNum(String extNum) {
		this.extNum = extNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public String getrLearningYn() {
		return rLearningYn;
	}
	public void setrLearningYn(String rLearningYn) {
		this.rLearningYn = rLearningYn;
	}
	public String getrText() {
		return rText;
	}
	public void setrText(String rText) {
		this.rText = rText;
	}
	public String getrLearningPer() {
		return rLearningPer;
	}
	public void setrLearningPer(String rLearningPer) {
		this.rLearningPer = rLearningPer;
	}
	public List<String> getrTranscriptSerialList() {
		return rTranscriptSerialList;
	}
	public void setrTranscriptSerialList(List<String> rTranscriptSerialList) {
		this.rTranscriptSerialList = rTranscriptSerialList;
	}
	public String getrSysCode() {
		return rSysCode;
	}
	public void setrSysCode(String rSysCode) {
		this.rSysCode = rSysCode;
	}
	
	public HashMap<String,String> getAllItem() {
		HashMap<String,String> allItem = new LinkedHashMap<String,String>();

		allItem.put("rUserId", this.rUserId);
		if(this.rRecDate != null && !this.rRecDate.trim().isEmpty()) allItem.put("rRecDate", this.rRecDate);
		if(this.rRecTime != null && !this.rRecTime.trim().isEmpty()) allItem.put("rRecTime", this.rRecTime);
		if(this.rBgCode != null && !this.rBgCode.trim().isEmpty()) allItem.put("rBgCode", this.rBgCode);
		if(this.rMgCode != null && !this.rMgCode.trim().isEmpty()) allItem.put("rMgCode", this.rMgCode);
		if(this.rSgCode != null && !this.rSgCode.trim().isEmpty()) allItem.put("rSgCode", this.rSgCode);
		if(this.rRecId != null && !this.rRecId.trim().isEmpty()) allItem.put("rRecId", this.rRecId);
		if(this.rRecName != null && !this.rRecName.trim().isEmpty()) allItem.put("rRecName", this.rRecName);
		if(this.rExtNum != null && !this.rExtNum.trim().isEmpty()) allItem.put("rExtNum", this.rExtNum);
		if(this.rCustName != null && !this.rCustName.trim().isEmpty()) allItem.put("rCustName", this.rCustName);
		if(this.rCustPhone != null && !this.rCustPhone.trim().isEmpty()) allItem.put("rCustPhone", this.rCustPhone);
		if(this.rTranscriptYn != null && !this.rTranscriptYn.trim().isEmpty()) allItem.put("rTranscriptYn", this.rTranscriptYn);
		if(this.rTranscriptStatus != null && !this.rTranscriptStatus.trim().isEmpty()) allItem.put("rTranscriptStatus", this.rTranscriptStatus);
		if(this.rTranscriptSerial != null && !this.rTranscriptSerial.trim().isEmpty()) allItem.put("rTranscriptSerial", this.rTranscriptSerial);
		if(this.rLocalFile != null && !this.rLocalFile.trim().isEmpty()) allItem.put("rLocalFile", this.rLocalFile);
		if(this.rSysCode != null && !this.rSysCode.trim().isEmpty()) allItem.put("rSysCode", this.rSysCode);
		return allItem;
	}

	public void setAllItem(String[] setValues) {

		Integer i = 0;
		Integer maxSetValues = setValues.length;
		
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecDate = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecTime = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rBgCode = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rMgCode = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rSgCode = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecId = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rRecName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rExtNum = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rCustName = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rCustPhone = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rTranscriptYn = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rTranscriptStatus = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rTranscriptSerial = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rLocalFile = setValues[i++]; }
		if(maxSetValues > i && setValues[i] != null && !setValues[i].isEmpty()) { this.rSysCode = setValues[i++]; }
	}
	
	
	@Override
	public String toString() {
		return "TranscriptListInfo [rUserId=" + rUserId + ", rRecDate=" + rRecDate
				+ ", rRecTime=" + rRecTime + ", rBgCode=" + rBgCode + ", rMgCode="
				+ rMgCode + ", rSgCode=" + rSgCode + ", rRecId=" + rRecId
				+ ", rRecName=" + rRecName + ", rExtNum=" + rExtNum + ", rCustName="
				+ rCustName + ", rCustPhone=" + rCustPhone + ", rTranscriptYn=" + rTranscriptYn
				+ ", rTranscriptStatus=" + rTranscriptStatus + ", rTranscriptSerial=" + rTranscriptSerial
				+ ", rLocalFile=" + rLocalFile + ", rSysCode=" + rSysCode + "]";
	}
	
	
	
	
}
