package com.furence.recsee.login.dao.impl;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.login.dao.LoginDao;

@Repository("loginDao")
public class LoginDaoImpl implements LoginDao {
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
	public LoginVO selectUserInfo(LoginVO bean){
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		return LoginMapper.selectUserInfo(bean);
	}
	@Override
	public LoginVO selectUser(LoginVO bean){
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		return LoginMapper.selectUserInfo(bean);
	}
	@Override
	public Integer updateLastLoginDate(LoginVO bean){
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateLastLoginDate").getBoundSql(bean);
		synchronizationUtil.SynchronizationInsert(query);
		
		return LoginMapper.updateLastLoginDate(bean);
	}
	@Override
	public Integer updateTryCount(LoginVO bean){
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTryCount").getBoundSql(bean);
		synchronizationUtil.SynchronizationInsert(query);
		
		return LoginMapper.updateTryCount(bean);
	}
	@Override
	public Integer updateUserLock(LoginVO bean){
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUserLock").getBoundSql(bean);
		synchronizationUtil.SynchronizationInsert(query);
		
		return LoginMapper.updateUserLock(bean);
	}
	@Override
	public LoginVO selectAuserInfo(LoginVO bean) {
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		return LoginMapper.selectAuserInfo(bean);
	}
	@Override
	public Integer updateLastLoginDateAuser(LoginVO bean) {
		LoginDao LoginMapper = sqlSession.getMapper(LoginDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateLastLoginDateAuser").getBoundSql(bean);
		synchronizationUtil.SynchronizationInsert(query);
		
		return LoginMapper.updateLastLoginDateAuser(bean);
	}
}
