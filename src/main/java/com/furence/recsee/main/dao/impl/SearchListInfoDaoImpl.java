package com.furence.recsee.main.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.main.dao.SearchListInfoDao;
import com.furence.recsee.main.model.ApproveListInfo;
import com.furence.recsee.main.model.SearchListInfo;

@Repository("searchListInfoDao")
public class SearchListInfoDaoImpl implements SearchListInfoDao {
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SynchronizationService synchronizationService;	
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<SearchListInfo> selectSearchListInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectSearchListInfo(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectCustToRecSeeInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectCustToRecSeeInfo(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectTraceSearchListInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		//return searchListInfoMapper.selectSearchListInfo(searchListInfo);
		return searchListInfoMapper.selectTraceSearchListInfo(searchListInfo);
	}
	@Override
	public Integer updateCustToRecSeeInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCustToRecSeeInfo").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateCustToRecSeeInfo(searchListInfo);
	}
	
	@Override
	public Integer updateEvalCheck(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEvalCheck").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateEvalCheck(searchListInfo);
	}
	
	@Override
	public Integer totalSearchListInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.totalSearchListInfo(searchListInfo);
	}

	@Override
	public Integer insertApproveInfo(ApproveListInfo approveListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertApproveInfo").getBoundSql(approveListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.insertApproveInfo(approveListInfo);
	}

	@Override
	public List<SearchListInfo> selectRecFileInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRecFileInfo(searchListInfo);
	}

	@Override
	public List<ApproveListInfo> selectApproveInfo(ApproveListInfo approveListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectApproveInfo(approveListInfo);
	}

	@Override
	public Integer deleteApproveInfo(ApproveListInfo approveListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteApproveInfo").getBoundSql(approveListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.deleteApproveInfo(approveListInfo);
	}
	@Override
	public Integer updateApproveInfo(ApproveListInfo approveListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateApproveInfo").getBoundSql(approveListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateApproveInfo(approveListInfo);
	}

	@Override
	public List<ApproveListInfo> selectApproveInfoByFileName(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectApproveInfoByFileName(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectFullPath(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectFullPath(searchListInfo);
	}

	@Override
	public List<RecMemo> selectRecMemo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRecMemo(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectSearchListInfoSelect(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectSearchListInfoSelect(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectURL(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectURL(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectRsRecfileInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRsRecfileInfo(searchListInfo);
	}
	
	@Override
	public List<SearchListInfo> selectSearchListInfoSTT(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectSearchListInfoSTT(searchListInfo);
	}
	
	@Override
	public List<SearchListInfo> selectSearchScriptStepHistory(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectSearchScriptStepHistory(searchListInfo);
	}

	@Override
	public String selectWhere(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectWhere(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectRecIp(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRecIp(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectRecIp2(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRecIp2(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectRecIp3(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectRecIp3(searchListInfo);
	}
	
	@Override
	public List<SearchListInfo> selectRecIp4(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectRecIp4(searchListInfo);
	}

	@Override
	public Integer selectDownRecIpCount(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectDownRecIpCount(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectDownRecIp(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectDownRecIp(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectDownRecIp2(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectDownRecIp2(searchListInfo);
	}

	@Override
	public Integer selectTotalTime(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectTotalTime(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectSearchListInfoKeyword(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectSearchListInfoKeyword(searchListInfo);
	}

	@Override
	public String selectRecParamHistory(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRecParamHistory(searchListInfo);
	}

	@Override
	public String selectRectryOneMonth(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectRectryOneMonth(searchListInfo);
	}
	
	@Override
	public Integer totalSearchListInfoKeyword(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.totalSearchListInfoKeyword(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectLogListen(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectLogListen(searchListInfo);
	}

	@Override
	public Integer insertLogListen(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertLogListen").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.insertLogListen(searchListInfo);
	}
	
	@Override
	public Integer updateGroupInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateGroupInfo").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateGroupInfo(searchListInfo);
	}
	@Override
	public Integer updateInActive(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateInActive").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateInActive(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectCSV(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectCSV(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectValidation(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectValidation(searchListInfo);
	}
	@Override
	public List<SearchListInfo> selectFileUploadListInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectFileUploadListInfo(searchListInfo);
	}
	
	@Override
	public List<SearchListInfo> selectRecSectionListInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectRecSectionListInfo(searchListInfo);
	}
	
	@Override
	public Integer selectBuffer3Count(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectBuffer3Count(searchListInfo);
	}
	
	@Override
	public String selectPgDecoding(String temp) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectPgDecoding(temp);
	}

	@Override
	public Integer updateRsRecfileInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRsRecfileInfo").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateRsRecfileInfo(searchListInfo);
	}

	@Override
	public Integer updateRsRecfile2Info(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRsRecfile2Info").getBoundSql(searchListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return searchListInfoMapper.updateRsRecfile2Info(searchListInfo);
	}


	@Override
	public String selectUrlDecrypt(String url) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectUrlDecrypt(url);
	}

	@Override
	public List<SearchListInfo> selectApiListenUrl(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		
		return searchListInfoMapper.selectApiListenUrl(searchListInfo);
	}

	@Override
	public Integer insertRsRecfileInfo(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.insertRsRecfileInfo(searchListInfo);
	}

	@Override
	public SearchListInfo selectttime(SearchListInfo searchListInfo) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);

		return searchListInfoMapper.selectttime(searchListInfo);
	}

	@Override
	public List<SearchListInfo> selectMergeFileToCallKey(String callKey) {
		SearchListInfoDao searchListInfoMapper = (SearchListInfoDao)sqlSession.getMapper(SearchListInfoDao.class);
		return searchListInfoMapper.selectMergeFileToCallKey(callKey);
	}
}
