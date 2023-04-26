package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.furence.recsee.admin.dao.DBManageDao;
import com.furence.recsee.admin.model.DBManage;

public class DBManageDaoImpl implements DBManageDao {
	
	@Autowired
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<DBManage> selectDBManage(DBManage dbManage) {
		DBManageDao dbManageMapper = sqlSession.getMapper(DBManageDao.class);
		return dbManageMapper.selectDBManage(dbManage);
	}
	
	@Override
	public DBManage selectOneDBManage(DBManage dbManage) {
		DBManageDao dbManageMapper = sqlSession.getMapper(DBManageDao.class);
		return dbManageMapper.selectOneDBManage(dbManage);
	}
	
	@Override
	public Integer insertDBManage(DBManage dbManage) {
		DBManageDao dbManageMapper = sqlSession.getMapper(DBManageDao.class);
		return dbManageMapper.insertDBManage(dbManage);
	}

	@Override
	public Integer updateDBManage(DBManage dbManage) {
		DBManageDao dbManageMapper = sqlSession.getMapper(DBManageDao.class);
		
		return dbManageMapper.updateDBManage(dbManage);
	}

	@Override
	public Integer deleteDBManage(DBManage dbManage) {
		DBManageDao dbManageMapper = sqlSession.getMapper(DBManageDao.class);
		return dbManageMapper.deleteDBManage(dbManage);
	}

}
