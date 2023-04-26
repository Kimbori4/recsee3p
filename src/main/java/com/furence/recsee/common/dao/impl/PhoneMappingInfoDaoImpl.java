package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.PhoneMappingInfoDao;
import com.furence.recsee.common.model.PhoneMappingInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("phoneMappingInfoDao")
public class PhoneMappingInfoDaoImpl implements PhoneMappingInfoDao {

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
	public List<PhoneMappingInfo> selectPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		PhoneMappingInfoDao phoneMappingInfoMapper = sqlSession.getMapper(PhoneMappingInfoDao.class);
		return phoneMappingInfoMapper.selectPhoneMappingInfo(phoneMappingInfo);
	}

	@Override
	public Integer insertPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		PhoneMappingInfoDao phoneMappingInfoMapper = sqlSession.getMapper(PhoneMappingInfoDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPhoneMappingInfo").getBoundSql(phoneMappingInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return phoneMappingInfoMapper.insertPhoneMappingInfo(phoneMappingInfo);
	}

	@Override
	public Integer updatePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		PhoneMappingInfoDao phoneMappingInfoMapper = sqlSession.getMapper(PhoneMappingInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updatePhoneMappingInfo").getBoundSql(phoneMappingInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return phoneMappingInfoMapper.updatePhoneMappingInfo(phoneMappingInfo);
	}

	@Override
	public Integer deletePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		PhoneMappingInfoDao phoneMappingInfoMapper = sqlSession.getMapper(PhoneMappingInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deletePhoneMappingInfo").getBoundSql(phoneMappingInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return phoneMappingInfoMapper.deletePhoneMappingInfo(phoneMappingInfo);
	}

}
