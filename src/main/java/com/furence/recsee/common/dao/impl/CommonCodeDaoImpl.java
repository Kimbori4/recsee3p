package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.CommonCodeDao;
import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("commonCodeDao")
public class CommonCodeDaoImpl implements CommonCodeDao {

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
	public 	List<CommonCodeVO> selectCommonCode(CommonCodeVO commonCode){
		CommonCodeDao commonCodeMapper = sqlSession.getMapper(CommonCodeDao.class);
		return commonCodeMapper.selectCommonCode(commonCode);
	}
	
	@Override
	public CommonCodeVO selectCommonName(CommonCodeVO commonCode) {
		CommonCodeDao commonCodeMapper = sqlSession.getMapper(CommonCodeDao.class);
		return commonCodeMapper.selectCommonName(commonCode);
	}

	@Override
	public Integer updateCommonCode(CommonCodeVO commonCode) {
		CommonCodeDao commonCodeMapper = sqlSession.getMapper(CommonCodeDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCommonCode").getBoundSql(commonCode);
		synchronizationUtil.SynchronizationInsert(query);
		
		return commonCodeMapper.updateCommonCode(commonCode);
	}

}
