package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.AllowableRangeInfoDao;
import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("allowableRangeInfoDao")
public class AllowableRangeInfoDaoImpl implements AllowableRangeInfoDao {

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
	public List<AllowableRangeInfo> selectAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {

		AllowableRangeInfoDao allowableRangeInfoMapper = sqlSession.getMapper(AllowableRangeInfoDao.class);

		return allowableRangeInfoMapper.selectAllowableRangeInfo(allowableRangeInfo);
	}
	
	@Override
	public AllowableRangeInfo checkAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {

		AllowableRangeInfoDao allowableRangeInfoMapper = sqlSession.getMapper(AllowableRangeInfoDao.class);

		return allowableRangeInfoMapper.checkAllowableRangeInfo(allowableRangeInfo);
	}
	
	@Override
	public Integer deleteAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {
		
		AllowableRangeInfoDao allowableRangeInfoMapper = sqlSession.getMapper(AllowableRangeInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteAllowableRangeInfo").getBoundSql(allowableRangeInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return allowableRangeInfoMapper.deleteAllowableRangeInfo(allowableRangeInfo);
	}
	
	@Override
	public Integer insertAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {
		
		AllowableRangeInfoDao allowableRangeInfoMapper = sqlSession.getMapper(AllowableRangeInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAllowableRangeInfo").getBoundSql(allowableRangeInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return allowableRangeInfoMapper.insertAllowableRangeInfo(allowableRangeInfo);
	}
}
