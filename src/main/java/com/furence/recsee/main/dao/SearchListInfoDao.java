package com.furence.recsee.main.dao;

import java.util.List;

import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.main.model.ApproveListInfo;
import com.furence.recsee.main.model.SearchListInfo;

public interface SearchListInfoDao {
	List<SearchListInfo> selectSearchListInfo(SearchListInfo searchListInfo);
	List<SearchListInfo> selectCustToRecSeeInfo(SearchListInfo searchListInfo);
	List<SearchListInfo> selectTraceSearchListInfo(SearchListInfo searchListInfo);
	Integer updateCustToRecSeeInfo(SearchListInfo searchListInfo);
	Integer updateEvalCheck(SearchListInfo searchListInfo);
	Integer totalSearchListInfo(SearchListInfo searchListInfo);
	Integer insertApproveInfo(ApproveListInfo approveListInfo);
	List<SearchListInfo> selectRecFileInfo(SearchListInfo searchListInfo);
	List<ApproveListInfo> selectApproveInfo(ApproveListInfo approveListInfo);
	Integer deleteApproveInfo(ApproveListInfo approveListInfo);
	Integer updateApproveInfo(ApproveListInfo approveListInfo);
	List<ApproveListInfo> selectApproveInfoByFileName(SearchListInfo searchListInfo);
	List<SearchListInfo> selectFullPath(SearchListInfo searchListInfo);
	List<RecMemo> selectRecMemo(SearchListInfo searchListInfo);
	List<SearchListInfo> selectSearchListInfoSelect(SearchListInfo searchListInfo);
	List<SearchListInfo> selectURL(SearchListInfo searchListInfo);
	List<SearchListInfo> selectRsRecfileInfo(SearchListInfo searchListInfo);	
	List<SearchListInfo> selectSearchListInfoSTT(SearchListInfo searchListInfo);
	List<SearchListInfo> selectSearchScriptStepHistory(SearchListInfo searchListInfo);
	
	String selectWhere(SearchListInfo searchListInfo);
	List<SearchListInfo> selectRecIp(SearchListInfo searchListInfo);
	List<SearchListInfo> selectRecIp2(SearchListInfo searchListInfo);
	List<SearchListInfo> selectRecIp3(SearchListInfo searchListInfo);
	List<SearchListInfo> selectRecIp4(SearchListInfo searchListInfo);
	Integer selectDownRecIpCount(SearchListInfo searchListInfo);
	List<SearchListInfo> selectDownRecIp(SearchListInfo searchListInfo);
	List<SearchListInfo> selectDownRecIp2(SearchListInfo searchListInfo);
	Integer selectTotalTime(SearchListInfo searchListInfo);
	List<SearchListInfo> selectSearchListInfoKeyword(SearchListInfo searchListInfo);
	String selectRecParamHistory(SearchListInfo searchListInfo);
	String selectRectryOneMonth(SearchListInfo searchListInfo);
	
	Integer totalSearchListInfoKeyword(SearchListInfo searchListInfo);
	List<SearchListInfo> selectLogListen(SearchListInfo searchListInfo);
	Integer insertLogListen(SearchListInfo searchListInfo);
	Integer updateGroupInfo(SearchListInfo searchListInfo);
	Integer updateInActive(SearchListInfo searchListInfo);
	List<SearchListInfo> selectCSV(SearchListInfo searchListInfo);
	List<SearchListInfo> selectValidation(SearchListInfo searchListInfo);
	List<SearchListInfo> selectFileUploadListInfo(SearchListInfo searchListInfo);
	List<SearchListInfo> selectRecSectionListInfo(SearchListInfo searchListInfo);
	Integer selectBuffer3Count(SearchListInfo searchListInfo);
	
	String selectPgDecoding(String temp);
	Integer updateRsRecfileInfo(SearchListInfo searchListInfo);
	Integer updateRsRecfile2Info(SearchListInfo searchListInfo);
	
	String selectUrlDecrypt(String url);
	
	List<SearchListInfo> selectApiListenUrl(SearchListInfo searchListInfo);
	Integer insertRsRecfileInfo(SearchListInfo searchListInfo);
	SearchListInfo selectttime(SearchListInfo searchListInfo);
	List<SearchListInfo> selectMergeFileToCallKey(String callKey);
}
