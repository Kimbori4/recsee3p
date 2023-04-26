package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.furence.recsee.common.dao.CustConfigInfoDao;
import com.furence.recsee.common.model.CustConfigInfo;

public class CustConfigInfoDaoImpl implements CustConfigInfoDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<CustConfigInfo> selectCustConfigInfo(CustConfigInfo custConfigInfo) {
		CustConfigInfoDao custConfigInfoMapper = sqlSession.getMapper(CustConfigInfoDao.class);
		return custConfigInfoMapper.selectCustConfigInfo(custConfigInfo);
	}

	@Override
	public List<CustConfigInfo> selectMenuYCustConfigInfo(CustConfigInfo custConfigInfo) {
		CustConfigInfoDao custConfigInfoMapper = sqlSession.getMapper(CustConfigInfoDao.class);
		return custConfigInfoMapper.selectMenuYCustConfigInfo(custConfigInfo);
	}

	@Override
	public List<CustConfigInfo> selectMenuNCustConfigInfo(CustConfigInfo custConfigInfo) {
		CustConfigInfoDao custConfigInfoMapper = sqlSession.getMapper(CustConfigInfoDao.class);
		return custConfigInfoMapper.selectMenuNCustConfigInfo(custConfigInfo);
	}

	@Override
	public Integer insertCustConfigInfo(CustConfigInfo custConfigInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updateCustConfigInfo(CustConfigInfo custConfigInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteCustConfigInfo(CustConfigInfo custConfigInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
