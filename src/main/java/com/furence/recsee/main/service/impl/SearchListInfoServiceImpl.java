package com.furence.recsee.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.main.dao.SearchListInfoDao;
import com.furence.recsee.main.model.ApproveListInfo;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.service.SearchListInfoService;

@Service("searchListInfoService")
public class SearchListInfoServiceImpl implements SearchListInfoService {
	@Autowired
	SearchListInfoDao searchListInfoMapper;

	@Override
	public List<SearchListInfo> selectSearchListInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectSearchListInfo(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectCustToRecSeeInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectCustToRecSeeInfo(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectTraceSearchListInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectTraceSearchListInfo(searchListInfo);
	}
	@Override
	public Integer updateCustToRecSeeInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.updateCustToRecSeeInfo(searchListInfo);
	}
	@Override
	public Integer updateEvalCheck(SearchListInfo searchListInfo) {
		return searchListInfoMapper.updateEvalCheck(searchListInfo);
	}	
	@Override
	public Integer totalSearchListInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.totalSearchListInfo(searchListInfo);
	}
	@Override
	public Integer insertApproveInfo(ApproveListInfo approveListInfo) {
		return searchListInfoMapper.insertApproveInfo(approveListInfo);
	}
	@Override
	public List<SearchListInfo> selectRecFileInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecFileInfo(searchListInfo);
	}
	@Override
	public List<ApproveListInfo> selectApproveInfo(ApproveListInfo approveListInfo) {
		return searchListInfoMapper.selectApproveInfo(approveListInfo);
	}
	@Override
	public Integer deleteApproveInfo(ApproveListInfo approveListInfo) {
		return searchListInfoMapper.deleteApproveInfo(approveListInfo);
	}
	@Override
	public Integer updateApproveInfo(ApproveListInfo approveListInfo) {
		return searchListInfoMapper.updateApproveInfo(approveListInfo);
	}
	@Override
	public List<ApproveListInfo> selectApproveInfoByFileName(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectApproveInfoByFileName(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectFullPath(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectFullPath(searchListInfo);
	}
	
	@Override
	public List<RecMemo> selectRecMemo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecMemo(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectSearchListInfoSelect(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectSearchListInfoSelect(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectURL(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectURL(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectRsRecfileInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRsRecfileInfo(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectSearchListInfoSTT(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectSearchListInfoSTT(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectSearchScriptStepHistory(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectSearchScriptStepHistory(searchListInfo);
	}
	@Override
	public String selectWhere(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectWhere(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectRecIp(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecIp(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectRecIp2(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecIp2(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectRecIp3(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecIp3(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectRecIp4(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecIp4(searchListInfo);
	}
	@Override
	public Integer selectDownRecIpCount(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectDownRecIpCount(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectDownRecIp(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectDownRecIp(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectDownRecIp2(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectDownRecIp2(searchListInfo);
	}
	@Override
	public Integer selectTotalTime(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectTotalTime(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectSearchListInfoKeyword(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectSearchListInfoKeyword(searchListInfo);
	}
	@Override
	public String selectRecParamHistory(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecParamHistory(searchListInfo);
	}

	@Override
	public String selectRectryOneMonth(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRectryOneMonth(searchListInfo);
	}
	
	@Override
	public Integer totalSearchListInfoKeyword(SearchListInfo searchListInfo) {
		return searchListInfoMapper.totalSearchListInfoKeyword(searchListInfo);
	}
	
	@Override
	public List<SearchListInfo> selectLogListen(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectLogListen(searchListInfo);
	}
	@Override
	public Integer insertLogListen(SearchListInfo searchListInfo) {
		return searchListInfoMapper.insertLogListen(searchListInfo);
	}
	@Override
	public Integer updateGroupInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.updateGroupInfo(searchListInfo);
	}
	@Override
	public Integer updateInActive(SearchListInfo searchListInfo) {
		return searchListInfoMapper.updateInActive(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectCSV(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectCSV(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectValidation(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectValidation(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectRecSectionListInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectRecSectionListInfo(searchListInfo);
	}
	
	@Override
	public Integer selectBuffer3Count(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectBuffer3Count(searchListInfo);
	}
	
	@Override
	public String selectPgDecoding(String temp) {
		return searchListInfoMapper.selectPgDecoding(temp);
	}
	@Override
	public List<SearchListInfo> selectFileUploadListInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectFileUploadListInfo(searchListInfo);
	}
	@Override
	public Integer updateRsRecfileInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.updateRsRecfileInfo(searchListInfo);
	}
	@Override
	public Integer updateRsRecfile2Info(SearchListInfo searchListInfo) {
		return searchListInfoMapper.updateRsRecfile2Info(searchListInfo);
	}
	@Override
	public String selectUrlDecrypt(String url) {
		return searchListInfoMapper.selectUrlDecrypt(url);
	}
	@Override
	public List<SearchListInfo> selectApiListenUrl(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectApiListenUrl(searchListInfo);
	}
	@Override
	public Integer insertRsRecfileInfo(SearchListInfo searchListInfo) {
		return searchListInfoMapper.insertRsRecfileInfo(searchListInfo);
	}
	@Override
	public SearchListInfo selectttime(SearchListInfo searchListInfo) {
		return searchListInfoMapper.selectttime(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectMergeFileToCallKey(String callKey) {
		return searchListInfoMapper.selectMergeFileToCallKey(callKey);
	}
}
