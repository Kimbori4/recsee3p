package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.UserDBInterfaceDao;
import com.furence.recsee.admin.model.UserDBInterface;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("userDBInterfaceDao")
public class UserDBInterfaceDaoImpl implements UserDBInterfaceDao {
	
	@Autowired
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SynchronizationService synchronizationService;

	@Override
	public List<UserDBInterface> selectUserDBInterface(UserDBInterface userDBInterface) {
		UserDBInterfaceDao userDBInterfaceMapper = sqlSession.getMapper(UserDBInterfaceDao.class);
		return userDBInterfaceMapper.selectUserDBInterface(userDBInterface);
	}
	
	@Override
	public UserDBInterface selectOneUserDBInterface(UserDBInterface userDBInterface) {
		UserDBInterfaceDao userDBInterfaceMapper = sqlSession.getMapper(UserDBInterfaceDao.class);
		return userDBInterfaceMapper.selectOneUserDBInterface(userDBInterface);
	}

	@Override
	public Integer insertUserDBInterface(UserDBInterface userDBInterface) {
		UserDBInterfaceDao userDBInterfaceMapper = sqlSession.getMapper(UserDBInterfaceDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertUserDBInterface").getBoundSql(userDBInterface);
		synchronizationUtil.SynchronizationInsert(query);
		
		return userDBInterfaceMapper.insertUserDBInterface(userDBInterface);
	}

	@Override
	public Integer updateUserDBInterface(UserDBInterface userDBInterface) {
		UserDBInterfaceDao userDBInterfaceMapper = sqlSession.getMapper(UserDBInterfaceDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUserDBInterface").getBoundSql(userDBInterface);
		synchronizationUtil.SynchronizationInsert(query);
		
		return userDBInterfaceMapper.updateUserDBInterface(userDBInterface);
	}

	@Override
	public Integer deleteUserDBInterface(UserDBInterface userDBInterface) {
		UserDBInterfaceDao userDBInterfaceMapper = sqlSession.getMapper(UserDBInterfaceDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteUserDBInterface").getBoundSql(userDBInterface);
		synchronizationUtil.SynchronizationInsert(query);
		
		return userDBInterfaceMapper.deleteUserDBInterface(userDBInterface);
	}

}
