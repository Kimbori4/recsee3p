package com.furence.recsee.evaluation.model;

public class EvaluationResultInfo {

	String rItemMark;
	Integer rEvalCode;
	String rEvalDate;
	String rSheetCode;
	String rEvalTime;
	String rRecFilename;
	String rEcampCode;
	String rEcampName;
	String rEvalatorId;
	String rBpartCode;
	String rMpartCode;
	String rSpartCode;
	String rEvalatorName;
	String rRecDate;
	String rRecTime;
	String rRecId;
	String rRecName;
	String rUserName;
	String rScreenFilename;
	String rTextFilename;
	String rSectitem;
	String rEvalTotalMark;
	String rEvalStatus;
	Integer rEvalNumber;
	String rEvalatorFeedback;
	String rEvalatorFeedback2nd;
	String rAgentFeedback;
	String rBestWorstYn;
	String rMockYn;
	String rAuditYn;
	String rCompleteStatus;
	String rReportYn;
	String rEvalDegree;
	String rEvalTotalScore;
//	String itemName;

	String rCustPhone1;

	String sEvalDate;
	String eEvalDate;
	String sRecDate;
	String eRecDate;
	String sRecTime;
	String eRecTime;
	String searchWord;
	String searchType;
	String recRtime;
	String listenUrl;
	String userType;
	String rRecCustName;
	String rRecCustPhone;
	String evaluationCount;
	String evaluationTotal;
	String assignmentCode;
	String rBigDataMark;//브라질 추가
	String rBgName;
	String rMgName;
	String rSgName;
	String degree;
	String callKind1;
	String agreeStatus;
	String lastDate;
	String feedbackSec;
	String feedbackSecId;
	String rejectStatus;
	String evalThema;
	String answerTypeVal;	// 수정
	
	String agentSearchFlag;
	
	public String getrEvalatorFeedback2nd() {
		return rEvalatorFeedback2nd;
	}
	public void setrEvalatorFeedback2nd(String rEvalatorFeedback2nd) {
		this.rEvalatorFeedback2nd = rEvalatorFeedback2nd;
	}
	public void setAnswerTypeVal(String answerTypeVal) {
		this.answerTypeVal = answerTypeVal;
	}
	//페이징 관련 추가
	private String topCount;
	private String limitUse;

	private Integer count;
	private Integer posStart;

	//정렬관련 추가
	private String orderBy;
	private String direction;
	
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
	public String getRejectStatus() {
		return rejectStatus;
	}
	public void setRejectStatus(String rejectStatus) {
		this.rejectStatus = rejectStatus;
	}
	public String getFeedbackSec() {
		return feedbackSec;
	}
	public void setFeedbackSec(String feedbackSec) {
		this.feedbackSec = feedbackSec;
	}
	public String getFeedbackSecId() {
		return feedbackSecId;
	}
	public void setFeedbackSecId(String feedbackSecId) {
		this.feedbackSecId = feedbackSecId;
	}
	public String getAgreeStatus() {
		return agreeStatus;
	}
	public void setAgreeStatus(String agreeStatus) {
		this.agreeStatus = agreeStatus;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getCallKind1() {
		return callKind1;
	}
	public void setCallKind1(String callKind1) {
		this.callKind1 = callKind1;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getrBgName() {
		return rBgName;
	}
	public void setrBgName(String rBgName) {
		this.rBgName = rBgName;
	}
	public String getrMgName() {
		return rMgName;
	}
	public void setrMgName(String rMgName) {
		this.rMgName = rMgName;
	}
	public String getrSgName() {
		return rSgName;
	}
	public void setrSgName(String rSgName) {
		this.rSgName = rSgName;
	}	
	public String getTopCount() {
		return topCount;
	}
	public void setTopCount(String topCount) {
		this.topCount = topCount;
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
	private String groupType;
	private int indexNum;

	public String getAssignmentCode() {
		return assignmentCode;
	}
	public void setAssignmentCode(String assignmentCode) {
		this.assignmentCode = assignmentCode;
	}
	public String getEvaluationTotal() {
		return evaluationTotal;
	}
	public void setEvaluationTotal(String evaluationTotal) {
		this.evaluationTotal = evaluationTotal;
	}
	public String getEvaluationCount() {
		return evaluationCount;
	}
	public void setEvaluationCount(String evaluationCount) {
		this.evaluationCount = evaluationCount;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public int getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}
	public String getListenUrl() {
		return listenUrl;
	}
	public void setListenUrl(String listenUrl) {
		this.listenUrl = listenUrl;
	}
	public String getRecRtime() {
		return recRtime;
	}
	public void setRecRtime(String recRtime) {
		this.recRtime = recRtime;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public Integer getrEvalCode() {
		return rEvalCode;
	}
	public void setrEvalCode(Integer rEvalCode) {
		this.rEvalCode = rEvalCode;
	}
	public String getrEcampName() {
		return rEcampName;
	}
	public void setrEcampName(String rEcampName) {
		this.rEcampName = rEcampName;
	}
	public String getrEvalDate() {
		String tmpDate = null;
		if(rEvalDate != null)	tmpDate = String.format("%s-%s-%s", rEvalDate.substring(0, 4), rEvalDate.substring(4,  6), rEvalDate.substring(6, 8));

		return tmpDate;
	}
	public void setrEvalDate(String rEvalDate) {
		this.rEvalDate = rEvalDate;
	}
	public String getrSheetCode() {
		return rSheetCode;
	}
	public void setrSheetCode(String rSheetCode) {
		this.rSheetCode = rSheetCode;
	}
	public String getrEvalTime() {
		String tmpTime = null;
		if(rEvalTime != null) tmpTime = String.format("%s:%s:%s", rEvalTime.substring(0, 2), rEvalTime.substring(2,  4), rEvalTime.substring(4, 6));

		return tmpTime;
	}
	public void setrEvalTime(String rEvalTime) {
		this.rEvalTime = rEvalTime;
	}
	public String getrRecFilename() {
		return rRecFilename;
	}
	public void setrRecFilename(String rRecFilename) {
		this.rRecFilename = rRecFilename;
	}
	public String getrEcampCode() {
		return rEcampCode;
	}
	public void setrEcampCode(String rEcampCode) {
		this.rEcampCode = rEcampCode;
	}
	public String getrEvalatorId() {
		return rEvalatorId;
	}
	public void setrEvalatorId(String rEvalatorId) {
		this.rEvalatorId = rEvalatorId;
	}
	public String getrBpartCode() {
		return rBpartCode;
	}
	public void setrBpartCode(String rBpartCode) {
		this.rBpartCode = rBpartCode;
	}
	public String getrMpartCode() {
		return rMpartCode;
	}
	public void setrMpartCode(String rMpartCode) {
		this.rMpartCode = rMpartCode;
	}
	public String getrSpartCode() {
		return rSpartCode;
	}
	public void setrSpartCode(String rSpartCode) {
		this.rSpartCode = rSpartCode;
	}
	public String getrEvalatorName() {
		return rEvalatorName;
	}
	public void setrEvalatorName(String rEvalatorName) {
		this.rEvalatorName = rEvalatorName;
	}
	public String getrRecDate() {
//		String tmpDate = null;
		//if(rRecDate != null)	tmpDate = String.format("%s-%s-%s", rRecDate.substring(0, 4), rRecDate.substring(4,  6), rRecDate.substring(6, 8));

		return rRecDate;
	}
	public void setrRecDate(String rRecDate) {
		this.rRecDate = rRecDate;
	}
	public String getrRecTime() {
//		String tmpTime = null;
//		if(rRecTime != null) tmpTime = String.format("%s:%s:%s", rRecTime.substring(0, 2), rRecTime.substring(2,  4), rRecTime.substring(4, 6));
		return rRecTime;
	}
	public void setrRecTime(String rRecTime) {
		this.rRecTime = rRecTime;
	}
	public String getrRecId() {
		return rRecId;
	}
	public void setrRecId(String rRecId) {
		this.rRecId = rRecId;
	}
	public String getrUserName() {
		return rUserName;
	}
	public void setrUserName(String rUserName) {
		this.rUserName = rUserName;
	}
	public String getrRecName() {
		return rRecName;
	}
	public void setrRecName(String rRecName) {
		this.rRecName = rRecName;
	}
	public String getrScreenFilename() {
		return rScreenFilename;
	}
	public void setrScreenFilename(String rScreenFilename) {
		this.rScreenFilename = rScreenFilename;
	}
	public String getrTextFilename() {
		return rTextFilename;
	}
	public void setrTextFilename(String rTextFilename) {
		this.rTextFilename = rTextFilename;
	}
	public String getrSectitem() {
		return rSectitem;
	}
	public void setrSectitem(String rSectitem) {
		this.rSectitem = rSectitem;
	}
	public String getrEvalTotalMark() {
		return rEvalTotalMark;
	}
	public void setrEvalTotalMark(String rEvalTotalMark) {
		this.rEvalTotalMark = rEvalTotalMark;
	}
	public String getrEvalStatus() {
		return rEvalStatus;
	}
	public void setrEvalStatus(String rEvalStatus) {
		this.rEvalStatus = rEvalStatus;
	}
	public Integer getrEvalNumber() {
		return rEvalNumber;
	}
	public void setrEvalNumber(Integer rEvalNumber) {
		this.rEvalNumber = rEvalNumber;
	}
	public String getrEvalatorFeedback() {
		return rEvalatorFeedback;
	}
	public void setrEvalatorFeedback(String rEvalatorFeedback) {
		this.rEvalatorFeedback = rEvalatorFeedback;
	}
	public String getrAgentFeedback() {
		return rAgentFeedback;
	}
	public void setrAgentFeedback(String rAgentFeedback) {
		this.rAgentFeedback = rAgentFeedback;
	}
	public String getrBestWorstYn() {
		return rBestWorstYn;
	}
	public void setrBestWorstYn(String rBestWorstYn) {
		this.rBestWorstYn = rBestWorstYn;
	}
	public String getrMockYn() {
		return rMockYn;
	}
	public void setrMockYn(String rMockYn) {
		this.rMockYn = rMockYn;
	}
	public String getrAuditYn() {
		return rAuditYn;
	}
	public void setrAuditYn(String rAuditYn) {
		this.rAuditYn = rAuditYn;
	}
	public String getrCompleteStatus() {
		return rCompleteStatus;
	}
	public void setrCompleteStatus(String rCompleteStatus) {
		this.rCompleteStatus = rCompleteStatus;
	}
	public String getrReportYn() {
		return rReportYn;
	}
	public void setrReportYn(String rReportYn) {
		this.rReportYn = rReportYn;
	}
	public String getrEvalDegree() {
		return rEvalDegree;
	}
	public void setrEvalDegree(String rEvalDegree) {
		this.rEvalDegree = rEvalDegree;
	}
	public String getrEvalTotalScore() {
		return rEvalTotalScore;
	}
	public void setrEvalTotalScore(String rEvalTotalScore) {
		this.rEvalTotalScore = rEvalTotalScore;
	}
	public String getrCustPhone1() {
		return rCustPhone1;
	}
	public void setrCustPhone1(String rCustPhone1) {
		this.rCustPhone1 = rCustPhone1;
	}
	public String getsEvalDate() {
		return sEvalDate;
	}
	public void setsEvalDate(String sEvalDate) {
		this.sEvalDate = sEvalDate;
	}
	public String geteEvalDate() {
		return eEvalDate;
	}
	public void seteEvalDate(String eEvalDate) {
		this.eEvalDate = eEvalDate;
	}
	public String getsRecDate() {
		return sRecDate;
	}
	public void setsRecDate(String sRecDate) {
		this.sRecDate = sRecDate;
	}
	public String geteRecDate() {
		return eRecDate;
	}
	public void seteRecDate(String eRecDate) {
		this.eRecDate = eRecDate;
	}
	public String getsRecTime() {
		return sRecTime;
	}
	public void setsRecTime(String sRecTime) {
		this.sRecTime = sRecTime;
	}
	public String geteRecTime() {
		return eRecTime;
	}
	public void seteRecTime(String eRecTime) {
		this.eRecTime = eRecTime;
	}
	public String getrItemMark() {
		return rItemMark;
	}
	public void setrItemMark(String rItemMark) {
		this.rItemMark = rItemMark;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getrRecCustName() {
		return rRecCustName;
	}
	public void setrRecCustName(String rRecCustName) {
		this.rRecCustName = rRecCustName;
	}
	public String getrRecCustPhone() {
		return rRecCustPhone;
	}
	public void setrRecCustPhone(String rRecCustPhone) {
		this.rRecCustPhone = rRecCustPhone;
	}
	public String getrBigDataMark() {
		return rBigDataMark;
	}
	public void setrBigDataMark(String rBigDataMark) {
		this.rBigDataMark = rBigDataMark;
	}
	public String getEvalThema() {
		return evalThema;
	}
	public void setEvalThema(String evalThema) {
		this.evalThema = evalThema;
	}
	@Override
	public String toString() {
		return "EvaluationResultInfo [rEvalCode=" + rEvalCode + ", rEvalDate=" + rEvalDate + ", rSheetCode=" + rSheetCode + ", rEvalTime=" + rEvalTime + ", rRecFilename=" + rRecFilename + ", rEcampCode="
				+ rEcampCode + ", rEvalatorId=" + rEvalatorId + ", rBpartCode=" + rBpartCode + ", rMpartCode=" + rMpartCode + ", rSpartCode=" + rSpartCode + ", rEvalatorName=" + rEvalatorName + ", rRecDate="
				+ rRecDate + ", rRecTime=" + rRecTime + ", rRecId=" + rRecId + ", rRecName=" + rRecName + ", rScreenFilename=" + rScreenFilename + ", rTextFilename=" + rTextFilename + ", rSetitem=" + rSectitem
				+ ", rEvalTotalMark=" + rEvalTotalMark + ", rEvalStatus=" + rEvalStatus + ", rEvalNumber=" + rEvalNumber + ", rEvalatorFeedback=" + rEvalatorFeedback + ", rAgentFeedback=" + rAgentFeedback
				+ ", rBestWorstYn=" + rBestWorstYn + ", rMockYn=" + rMockYn + ", rAuditYn=" + rAuditYn + ", rCompleteStatus=" + rCompleteStatus + ", rReportYn=" + rReportYn + ", rEvalDegree=" + rEvalDegree
				+ ", rEvalTotalScore=" + rEvalTotalScore + ", rCustPhone1=" + rCustPhone1 + ", sEvalDate=" + sEvalDate + ", eEvalDate=" + eEvalDate + ", sRecDate=" + sRecDate + ", eRecDate=" + eRecDate
				+ ", sRecTime=" + sRecTime + ", eRecTime=" + eRecTime + ", searchWord=" + searchWord + ", searchType=" + searchType + "]";
	}
	// 수정
	public String getAnswerTypeVal() {
		return answerTypeVal;
	}
	public void setanswerTypeVal(String answerTypeVal) {
		this.answerTypeVal = answerTypeVal;
	}
	public String getAgentSearchFlag() {
		return agentSearchFlag;
	}
	public void setAgentSearchFlag(String agentSearchFlag) {
		this.agentSearchFlag = agentSearchFlag;
	}
	
	
}
