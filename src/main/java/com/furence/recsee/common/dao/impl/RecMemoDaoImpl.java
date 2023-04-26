package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.RecMemoDao;
import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("recMemoDao")
public class RecMemoDaoImpl implements RecMemoDao {

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
	public List<RecMemo> selectRecMemo(RecMemo recMemo){
		RecMemoDao recMemoDaoMapper = sqlSession.getMapper(RecMemoDao.class);
		return recMemoDaoMapper.selectRecMemo(recMemo);
	}
	@Override
	public Integer insertRecMemo(RecMemo recMemo){
		RecMemoDao recMemoDaoMapper = sqlSession.getMapper(RecMemoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertRecMemo").getBoundSql(recMemo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return recMemoDaoMapper.insertRecMemo(recMemo);
	}
	@Override
	public Integer	updateRecMemo(RecMemo recMemo){
		RecMemoDao recMemoDaoMapper = sqlSession.getMapper(RecMemoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRecMemo").getBoundSql(recMemo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return recMemoDaoMapper.updateRecMemo(recMemo);
	}
	@Override
	public Integer	deleteRecMemo(RecMemo recMemo){
		RecMemoDao recMemoDaoMapper = sqlSession.getMapper(RecMemoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteRecMemo").getBoundSql(recMemo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return recMemoDaoMapper.deleteRecMemo(recMemo);
	}

	@Override
	public Integer upsertRecMemo(RecMemo recMemo) {
		RecMemoDao recMemoDaoMapper = sqlSession.getMapper(RecMemoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upsertRecMemo").getBoundSql(recMemo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return recMemoDaoMapper.deleteRecMemo(recMemo);
	}
	
	@Override
	public Integer selectTagCheck(RecMemo recMemo) {
		RecMemoDao recMemoDaoMapper = sqlSession.getMapper(RecMemoDao.class);
		return recMemoDaoMapper.selectTagCheck(recMemo);
	}
}
