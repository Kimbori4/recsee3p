package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.EtcConfigInfoDao;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.TemplateKeyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("etcConfigInfoDao")
public class EtcConfigInfoDaoImpl implements EtcConfigInfoDao {

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
	public List<EtcConfigInfo> selectEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		return etcConfigInfoMapper.selectEtcConfigInfo(etcConfigInfo);
	}

	@Override
	public List<EtcConfigInfo> selectEtcConfigInfoLike(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		return etcConfigInfoMapper.selectEtcConfigInfoLike(etcConfigInfo);
	}
	
	@Override
	public List<EtcConfigInfo> selectEtcConfigKey(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		return etcConfigInfoMapper.selectEtcConfigKey(etcConfigInfo);
	}
	
	@Override
	public Integer insertEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertEtcConfigInfo").getBoundSql(etcConfigInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return etcConfigInfoMapper.insertEtcConfigInfo(etcConfigInfo);
	}

	@Override
	public Integer updateEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEtcConfigInfo").getBoundSql(etcConfigInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return etcConfigInfoMapper.updateEtcConfigInfo(etcConfigInfo);
	}
	
	@Override
	public Integer deleteEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteEtcConfigInfo").getBoundSql(etcConfigInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return etcConfigInfoMapper.deleteEtcConfigInfo(etcConfigInfo);
	}
	
	@Override
	public List<TemplateKeyInfo> selectTemplateKeyInfo(TemplateKeyInfo templateKeyInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);
		
		return etcConfigInfoMapper.selectTemplateKeyInfo(templateKeyInfo);
	}

	@Override
	public String selectHideConference() {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);
		
		return etcConfigInfoMapper.selectHideConference();
	}

	@Override
	public String selectHideTransfer() {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);
		
		return etcConfigInfoMapper.selectHideConference();
	}
	
	/* 20200120 ��ٺ� ���� */
	@Override
	public EtcConfigInfo selectOptionInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		return etcConfigInfoMapper.selectOptionInfo(etcConfigInfo);
	}
	@Override
	public Integer insertPrefixInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPrefixInfo").getBoundSql(etcConfigInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return etcConfigInfoMapper.insertEtcConfigInfo(etcConfigInfo);
	}

	@Override
	public Integer updatePrefixInfo(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updatePrefixInfo").getBoundSql(etcConfigInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return etcConfigInfoMapper.updatePrefixInfo(etcConfigInfo);
	}
	
	@Override
	public Integer updateOptionValue(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateOptionValue").getBoundSql(etcConfigInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return etcConfigInfoMapper.updateOptionValue(etcConfigInfo);
	}	
	
	@Override
	public EtcConfigInfo selectOptionYN(EtcConfigInfo etcConfigInfo) {
		EtcConfigInfoDao etcConfigInfoMapper = sqlSession.getMapper(EtcConfigInfoDao.class);

		return etcConfigInfoMapper.selectOptionYN(etcConfigInfo);
	}
}
