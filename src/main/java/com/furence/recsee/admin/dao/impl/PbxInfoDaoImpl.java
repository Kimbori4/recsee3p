package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.PbxInfoDao;
import com.furence.recsee.admin.model.PbxInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("pbxInfoDao")
public class PbxInfoDaoImpl implements PbxInfoDao {
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
	public List<PbxInfo> selectPbxInfo(PbxInfo pbxInfo) {
		PbxInfoDao pbxInfoMapper = (PbxInfoDao)sqlSession.getMapper(PbxInfoDao.class);
		return pbxInfoMapper.selectPbxInfo(pbxInfo);
	}

	@Override
	public Integer insertPbxInfo(PbxInfo pbxInfo) {
		PbxInfoDao pbxInfoMapper = (PbxInfoDao)sqlSession.getMapper(PbxInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPbxInfo").getBoundSql(pbxInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return pbxInfoMapper.insertPbxInfo(pbxInfo);
	}

	@Override
	public Integer updatePbxInfo(PbxInfo pbxInfo) {
		PbxInfoDao pbxInfoMapper = (PbxInfoDao)sqlSession.getMapper(PbxInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updatePbxInfo").getBoundSql(pbxInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return pbxInfoMapper.updatePbxInfo(pbxInfo);
	}

	@Override
	public Integer deletePbxInfo(PbxInfo pbxInfo) {
		PbxInfoDao pbxInfoMapper = (PbxInfoDao)sqlSession.getMapper(PbxInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deletePbxInfo").getBoundSql(pbxInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return pbxInfoMapper.deletePbxInfo(pbxInfo);
	}
}
