package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.PasswordPolicyDao;
import com.furence.recsee.admin.model.EnWord;
import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("passwordPolicyDao")
public class PasswordPolicyDaoImpl implements PasswordPolicyDao {
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
	public List<PasswordPolicyInfo> selectRPasswordPolicyInfo(PasswordPolicyInfo rPasswordPolicyInfo){
		PasswordPolicyDao rPasswordPolicyDao = sqlSession.getMapper(PasswordPolicyDao.class);
		return rPasswordPolicyDao.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
	}
	@Override
	public Integer updateRPasswordPolicyInfo(PasswordPolicyInfo rPasswordPolicyInfo){
		PasswordPolicyDao rPasswordPolicyDao = sqlSession.getMapper(PasswordPolicyDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRPasswordPolicyInfo").getBoundSql(rPasswordPolicyInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return rPasswordPolicyDao.updateRPasswordPolicyInfo(rPasswordPolicyInfo);
	}

	@Override
	public Integer selectEnWord (EnWord enWord){
		PasswordPolicyDao rPasswordPolicyDao = sqlSession.getMapper(PasswordPolicyDao.class);
		return rPasswordPolicyDao.selectEnWord(enWord);
	}
}
