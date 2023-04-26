package com.furence.recsee.statistics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.sqlFilterUtil;

public class StatisticsInfo {
	
	private String userId;
	private String userArray;
	private String userName;
	private String extNo;
	private String bgCode;
	private String mgCode;
	private String sgCode;
	private String recDate;
	private String recRTime;
	private String recSDate;
	private String recEDate;
	private String dayOfWeek;
	private String vSysCode;
	private Integer totalCalls;
	private Integer totalTime;
	private Integer inboundCalls;
	private Integer inboundTime;
	private Integer outboundCalls;
	private Integer outboundTime;
	private Integer transferCalls;
	private Integer transferTime;
	private Integer conferenceCalls;
	private Integer conferenceTime;
	private Integer internalCalls;
	private Integer internalTime;
	private Integer noRecodingCount;
	private String sDate;
	private String eDate;
	private String statisticsUseInternalCalls;
	private String dayTimeBy;
	private String divisionBy;
	private String init;
	private String bgName;
	private String mgName;
	private String sgName;
	private String minCallTime;
	private String maxCallTime;
	private String avgCallTime;
	private String totalCallbytime;
	private String sec30;
	private String sec60;
	private String sec120;
	private String sec180;
	private String sec240;
	private String sec300;
	private String sec420;
	
	private Integer realCalls;
	private Integer realCallTime;
	
	private String b;
	private String inline_no;
	private String grp_g_cd;
	private String grp_p_cd;
	private String name;
	
	private Integer count;
	private Integer posStart;
	
	private List<String> bgCodeL;
	private List<String> mgCodeL;
	private List<String> sgCodeL;
	private List<String> userIdL;
	
	private String col;
	private String order;
	
	private Integer success_connect;
	private Integer giveup_call;
	private Integer no_answer;
	private Integer busy_call;
	private String r_month;
	private String total_user;
	private Integer CL01_try;
	private Integer CL01_conn;
	private Integer CL02_try;
	private Integer CL02_conn;
	private Integer CL03_try;
	private Integer CL03_conn;
	private Integer CL04_try;
	private Integer CL04_conn;
	private Integer CL05_try;
	private Integer CL05_conn;
	private Integer CL06_try;
	private Integer CL06_conn;
	private Integer CL07_try;
	private Integer CL07_conn;
	private Integer CL08_try;
	private Integer CL08_conn;
	private Integer CL01CL04_sum_conn;
	private Integer CL01CL04_call_time;
	private Integer CL01_call_time;
	private Integer CL04_call_time;
	private Integer total_count;
	private String rownumber;
	private String excelDownload;
	
	private String keywordName;
	private Integer keywordSum;
	private Integer rowNumber;
	private String keywordCode;
	private String useYn;
	private String categoryCode;
	private String categoryName;
	private String keywordCount;

	private String OUTBOUND_AVG_CALL_TIME;
	private String INBOUND_AVG_CALL_TIME;
	
	private Integer OUTBOUND_CALLS_try;
	private Integer OUTBOUND_CALLS_conn;
	private Integer INBOUND_CALLS_try;
	private Integer INBOUND_CALLS_conn;
	
	private String authBeforeGroup;

	
	public String getAuthBeforeGroup() {
		return authBeforeGroup;
	}
	public void setAuthBeforeGroup(String authBeforeGroup) {
		this.authBeforeGroup = authBeforeGroup;
	}
	public String getOUTBOUND_AVG_CALL_TIME() {
		return OUTBOUND_AVG_CALL_TIME;
	}
	public void setOUTBOUND_AVG_CALL_TIME(String oUTBOUND_AVG_CALL_TIME) {
		OUTBOUND_AVG_CALL_TIME = oUTBOUND_AVG_CALL_TIME;
	}
	public String getINBOUND_AVG_CALL_TIME() {
		return INBOUND_AVG_CALL_TIME;
	}
	public void setINBOUND_AVG_CALL_TIME(String iNBOUND_AVG_CALL_TIME) {
		INBOUND_AVG_CALL_TIME = iNBOUND_AVG_CALL_TIME;
	}
	public Integer getOUTBOUND_CALLS_try() {
		return OUTBOUND_CALLS_try;
	}
	public void setOUTBOUND_CALLS_try(Integer oUTBOUND_CALLS_try) {
		OUTBOUND_CALLS_try = oUTBOUND_CALLS_try;
	}
	public Integer getOUTBOUND_CALLS_conn() {
		return OUTBOUND_CALLS_conn;
	}
	public void setOUTBOUND_CALLS_conn(Integer oUTBOUND_CALLS_conn) {
		OUTBOUND_CALLS_conn = oUTBOUND_CALLS_conn;
	}
	public Integer getINBOUND_CALLS_try() {
		return INBOUND_CALLS_try;
	}
	public void setINBOUND_CALLS_try(Integer iNBOUND_CALLS_try) {
		INBOUND_CALLS_try = iNBOUND_CALLS_try;
	}
	public Integer getINBOUND_CALLS_conn() {
		return INBOUND_CALLS_conn;
	}
	public void setINBOUND_CALLS_conn(Integer iNBOUND_CALLS_conn) {
		INBOUND_CALLS_conn = iNBOUND_CALLS_conn;
	}
	public String getRecSDate() {
		return recSDate;
	}
	public void setRecSDate(String recSDate) {
		this.recSDate = recSDate;
	}
	public String getRecEDate() {
		return recEDate;
	}
	public void setRecEDate(String recEDate) {
		this.recEDate = recEDate;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getKeywordCount() {
		return keywordCount;
	}
	public void setKeywordCount(String keywordCount) {
		this.keywordCount = keywordCount;
	}
	private ArrayList<String> categoryCodeArr;

	public ArrayList<String> getCategoryCodeArr() {
		return categoryCodeArr;
	}
	public void setCategoryCodeArr(ArrayList<String> categoryCodeArr) {
		this.categoryCodeArr = categoryCodeArr;
	}
	public String getKeywordCode() {
		return keywordCode;
	}
	public void setKeywordCode(String keywordCode) {
		this.keywordCode = keywordCode;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getKeywordName() {
		return keywordName;
	}
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}
	public Integer getKeywordSum() {
		return keywordSum;
	}
	public void setKeywordSum(Integer keywordSum) {
		this.keywordSum = keywordSum;
	}
	public Integer getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getR_month() {
		return r_month;
	}
	public void setR_month(String r_month) {
		this.r_month = r_month;
	}
	public String getDivisionBy() {
		return divisionBy;
	}
	public void setDivisionBy(String divisionBy) {
		this.divisionBy = divisionBy;
	}
	public String getTotalCallbytime() {
		return totalCallbytime;
	}
	public void setTotalCallbytime(String totalCallbytime) {
		this.totalCallbytime = totalCallbytime;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
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
	public Integer getRealCalls() {
		return realCalls;
	}
	public void setRealCalls(Integer realCalls) {
		this.realCalls = realCalls;
	}
	public Integer getRealCallTime() {
		return realCallTime;
	}
	public void setRealCallTime(Integer realCallTime) {
		this.realCallTime = realCallTime;
	}
	
	public List<String> getBgCodeL() {
		return bgCodeL;
	}
	public void setBgCodeL(List<String> bgCodeL) {
		this.bgCodeL = bgCodeL;
	}
	public List<String> getMgCodeL() {
		return mgCodeL;
	}
	public void setMgCodeL(List<String> mgCodeL) {
		this.mgCodeL = mgCodeL;
	}
	public List<String> getSgCodeL() {
		return sgCodeL;
	}
	public void setSgCodeL(List<String> sgCodeL) {
		this.sgCodeL = sgCodeL;
	}
	public List<String> getUserIdL() {
		return userIdL;
	}
	public void setUserIdL(List<String> userIdL) {
		this.userIdL = userIdL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getInline_no() {
		return inline_no;
	}
	public void setInline_no(String inline_no) {
		this.inline_no = inline_no;
	}
	public String getGrp_g_cd() {
		return grp_g_cd;
	}
	public void setGrp_g_cd(String grp_g_cd) {
		this.grp_g_cd = grp_g_cd;
	}
	public String getGrp_p_cd() {
		return grp_p_cd;
	}
	public void setGrp_p_cd(String grp_p_cd) {
		this.grp_p_cd = grp_p_cd;
	}
	public String getUserArray() {
		return userArray;
	}
	public void setUserArray(String userArray) {
		this.userArray = userArray;
	}
	public String getSec30() {
		return sec30;
	}
	public void setSec30(String sec30) {
		this.sec30 = sec30;
	}
	public String getSec60() {
		return sec60;
	}
	public void setSec60(String sec60) {
		this.sec60 = sec60;
	}
	public String getSec120() {
		return sec120;
	}
	public void setSec120(String sec120) {
		this.sec120 = sec120;
	}
	public String getSec180() {
		return sec180;
	}
	public void setSec180(String sec180) {
		this.sec180 = sec180;
	}
	public String getSec240() {
		return sec240;
	}
	public void setSec240(String sec240) {
		this.sec240 = sec240;
	}
	public String getSec300() {
		return sec300;
	}
	public void setSec300(String sec300) {
		this.sec300 = sec300;
	}
	public String getSec420() {
		return sec420;
	}
	public void setSec420(String sec420) {
		this.sec420 = sec420;
	}
	public String getSec600() {
		return sec600;
	}
	public void setSec600(String sec600) {
		this.sec600 = sec600;
	}
	public String getMoresec600() {
		return moresec600;
	}
	public void setMoresec600(String moresec600) {
		this.moresec600 = moresec600;
	}
	private String sec600;
	private String moresec600;
	
	
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
	public String getExtNo() {
		return extNo;
	}
	public void setExtNo(String extNo) {
		this.extNo = extNo;
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
	public String getRecDate() {
		String tmpDate = null;
		if(recDate != null) {
			if(recDate.length() == 6) {
				tmpDate = String.format("%s-%s", recDate.substring(0, 4), recDate.substring(4,  6));
			} else {
				tmpDate = String.format("%s-%s-%s", recDate.substring(0, 4), recDate.substring(4,  6), recDate.substring(6, 8));
			}	
		}
		return tmpDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecRTime() {
		return recRTime;
	}
	public void setRecRTime(String recRTime) {
		this.recRTime = recRTime;
	}
	public String getvSysCode() {
		return vSysCode;
	}
	public void setvSysCode(String vSysCode) {
		this.vSysCode = vSysCode;
	}
	public Integer getTotalCalls() {
		return totalCalls;
	}
	public void setTotalCalls(Integer totalCalls) {
		this.totalCalls = totalCalls;
	}
	public Integer getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}
	public Integer getInboundCalls() {
		return inboundCalls;
	}
	public void setInboundCalls(Integer inboundCalls) {
		this.inboundCalls = inboundCalls;
	}
	public Integer getInboundTime() {
		return inboundTime;
	}
	public void setInboundTime(Integer inboundTime) {
		this.inboundTime = inboundTime;
	}
	public Integer getOutboundCalls() {
		return outboundCalls;
	}
	public void setOutboundCalls(Integer outboundCalls) {
		this.outboundCalls = outboundCalls;
	}
	public Integer getOutboundTime() {
		return outboundTime;
	}
	public void setOutboundTime(Integer outboundTime) {
		this.outboundTime = outboundTime;
	}
	public Integer getInternalCalls() {
		return internalCalls;
	}
	public void setInternalCalls(Integer internalCalls) {
		this.internalCalls = internalCalls;
	}
	public Integer getTransferCalls() {
		return transferCalls;
	}
	public void setTransferCalls(Integer transferCalls) {
		this.transferCalls = transferCalls;
	}
	public Integer getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Integer transferTime) {
		this.transferTime = transferTime;
	}
	public Integer getConferenceCalls() {
		return conferenceCalls;
	}
	public void setConferenceCalls(Integer conferenceCalls) {
		this.conferenceCalls = conferenceCalls;
	}
	public Integer getConferenceTime() {
		return conferenceTime;
	}
	public void setConferenceTime(Integer conferenceTime) {
		this.conferenceTime = conferenceTime;
	}
	public Integer getInternalTime() {
		return internalTime;
	}
	public void setInternalTime(Integer internalTime) {
		this.internalTime = internalTime;
	}
	public Integer getNoRecodingCount() {
		return noRecodingCount;
	}
	public void setNoRecodingCount(Integer noRecodingCount) {
		this.noRecodingCount = noRecodingCount;
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
	public String getStatisticsUseInternalCalls() {
		return statisticsUseInternalCalls;
	}
	public void setStatisticsUseInternalCalls(String statisticsUseInternalCalls) {
		this.statisticsUseInternalCalls = statisticsUseInternalCalls;
	}
	public String getDayTimeBy() {
		return dayTimeBy;
	}
	public void setDayTimeBy(String dayTimeBy) {
		this.dayTimeBy = dayTimeBy;
	}
	
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
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
	
	public String getMinCallTime() {
		return minCallTime;
	}
	public void setMinCallTime(String minCallTime) {
		this.minCallTime = minCallTime;
	}
	public String getMaxCallTime() {
		return maxCallTime;
	}
	public void setMaxCallTime(String maxCallTime) {
		this.maxCallTime = maxCallTime;
	}
	public String getAvgCallTime() {
		return avgCallTime;
	}
	public void setAvgCallTime(String avgCallTime) {
		this.avgCallTime = avgCallTime;
	}
	public Integer getSuccess_connect() {
		return success_connect;
	}
	public void setSuccess_connect(Integer success_connect) {
		this.success_connect = success_connect;
	}
	public Integer getGiveup_call() {
		return giveup_call;
	}
	public void setGiveup_call(Integer giveup_call) {
		this.giveup_call = giveup_call;
	}
	public Integer getNo_answer() {
		return no_answer;
	}
	public void setNo_answer(Integer no_answer) {
		this.no_answer = no_answer;
	}
	public Integer getBusy_call() {
		return busy_call;
	}
	public void setBusy_call(Integer busy_call) {
		this.busy_call = busy_call;
	}
	public String getTotal_user() {
		return total_user;
	}
	public void setTotal_user(String total_user) {
		this.total_user = total_user;
	}
	public Integer getCL01_try() {
		return CL01_try;
	}
	public void setCL01_try(Integer cL01_try) {
		CL01_try = cL01_try;
	}
	public Integer getCL01_conn() {
		return CL01_conn;
	}
	public void setCL01_conn(Integer cL01_conn) {
		CL01_conn = cL01_conn;
	}
	public Integer getCL02_try() {
		return CL02_try;
	}
	public void setCL02_try(Integer cL02_try) {
		CL02_try = cL02_try;
	}
	public Integer getCL02_conn() {
		return CL02_conn;
	}
	public void setCL02_conn(Integer cL02_conn) {
		CL02_conn = cL02_conn;
	}
	public Integer getCL03_try() {
		return CL03_try;
	}
	public void setCL03_try(Integer cL03_try) {
		CL03_try = cL03_try;
	}
	public Integer getCL03_conn() {
		return CL03_conn;
	}
	public void setCL03_conn(Integer cL03_conn) {
		CL03_conn = cL03_conn;
	}
	public Integer getCL04_try() {
		return CL04_try;
	}
	public void setCL04_try(Integer cL04_try) {
		CL04_try = cL04_try;
	}
	public Integer getCL04_conn() {
		return CL04_conn;
	}
	public void setCL04_conn(Integer cL04_conn) {
		CL04_conn = cL04_conn;
	}
	public Integer getCL05_try() {
		return CL05_try;
	}
	public void setCL05_try(Integer cL05_try) {
		CL05_try = cL05_try;
	}
	public Integer getCL05_conn() {
		return CL05_conn;
	}
	public void setCL05_conn(Integer cL05_conn) {
		CL05_conn = cL05_conn;
	}
	public Integer getCL06_try() {
		return CL06_try;
	}
	public void setCL06_try(Integer cL06_try) {
		CL06_try = cL06_try;
	}
	public Integer getCL06_conn() {
		return CL06_conn;
	}
	public void setCL06_conn(Integer cL06_conn) {
		CL06_conn = cL06_conn;
	}
	public Integer getCL07_try() {
		return CL07_try;
	}
	public void setCL07_try(Integer cL07_try) {
		CL07_try = cL07_try;
	}
	public Integer getCL07_conn() {
		return CL07_conn;
	}
	public void setCL07_conn(Integer cL07_conn) {
		CL07_conn = cL07_conn;
	}
	public Integer getCL08_try() {
		return CL08_try;
	}
	public void setCL08_try(Integer cL08_try) {
		CL08_try = cL08_try;
	}
	public Integer getCL08_conn() {
		return CL08_conn;
	}
	public void setCL08_conn(Integer cL08_conn) {
		CL08_conn = cL08_conn;
	}
	public Integer getCL01CL04_sum_conn() {
		return CL01CL04_sum_conn;
	}
	public void setCL01CL04_sum_conn(Integer cL01CL04_sum_conn) {
		CL01CL04_sum_conn = cL01CL04_sum_conn;
	}
	public Integer getCL01CL04_call_time() {
		return CL01CL04_call_time;
	}
	public void setCL01CL04_call_time(Integer cL01CL04_call_time) {
		CL01CL04_call_time = cL01CL04_call_time;
	}
	public Integer getCL01_call_time() {
		return CL01_call_time;
	}
	public void setCL01_call_time(Integer cL01_call_time) {
		CL01_call_time = cL01_call_time;
	}
	public Integer getCL04_call_time() {
		return CL04_call_time;
	}
	public void setCL04_call_time(Integer cL04_call_time) {
		CL04_call_time = cL04_call_time;
	}
	public Integer getTotal_count() {
		return total_count;
	}
	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}
	public String getRownumber() {
		return rownumber;
	}
	public void setRownumber(String rownumber) {
		this.rownumber = rownumber;
	}
	public String getExcelDownload() {
		return excelDownload;
	}
	public void setExcelDownload(String excelDownload) {
		this.excelDownload = excelDownload;
	}
	@Override
	public String toString() {
		return "StatisticsInfo [userId=" + userId + ", userName=" + userName
				+ ", extNo=" + extNo + ", bgCode=" + bgCode + ", mgCode="
				+ mgCode + ", sgCode=" + sgCode + ", recDate=" + recDate
				+ ", recRTime=" + recRTime + ", vSysCode=" + vSysCode
				+ ", totalCalls=" + totalCalls + ", totalTime=" + totalTime
				+ ", inboundCalls=" + inboundCalls + ", inboundTime="
				+ inboundTime + ", outboundCalls=" + outboundCalls
				+ ", outboundTime=" + outboundTime + ", internalCalls="
				+ internalCalls + ", internalTime=" + internalTime
				+ ", noRecodingCount=" + noRecodingCount + ", sDate=" + sDate
				+ ", eDate=" + eDate + ", statisticsUseInternalCalls="
				+ statisticsUseInternalCalls + "]";
	}
	
}
