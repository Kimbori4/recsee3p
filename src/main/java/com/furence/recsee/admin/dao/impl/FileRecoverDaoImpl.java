package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.FileRecoverDao;
import com.furence.recsee.admin.model.FileRecoverInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("fileRecoverDao")
public class FileRecoverDaoImpl implements FileRecoverDao {
	
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
	public List<FileRecoverInfo> selectFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		return fileRecoverMapper.selectFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer insertFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertFileRecoverInfo").getBoundSql(fileRecoverInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return fileRecoverMapper.insertFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer updateFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateFileRecoverInfo").getBoundSql(fileRecoverInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return fileRecoverMapper.updateFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer deleteFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteFileRecoverInfo").getBoundSql(fileRecoverInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return fileRecoverMapper.deleteFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer selectFileRecoverInfoTotal(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		return fileRecoverMapper.selectFileRecoverInfoTotal(fileRecoverInfo);
	}

	@Override
	public List<FileRecoverInfo> selectGenesysInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		return fileRecoverMapper.selectGenesysInfo(fileRecoverInfo);
	}

	@Override
	public Integer selectGenesysInfoTotal(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		return fileRecoverMapper.selectGenesysInfoTotal(fileRecoverInfo);
	}

	@Override
	public Integer updateGenesysInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateGenesysInfo").getBoundSql(fileRecoverInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return fileRecoverMapper.updateGenesysInfo(fileRecoverInfo);
	}

	@Override
	public Integer deleteGenesysInfo(FileRecoverInfo fileRecoverInfo) {
		FileRecoverDao fileRecoverMapper = sqlSession.getMapper(FileRecoverDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteGenesysInfo").getBoundSql(fileRecoverInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return fileRecoverMapper.deleteGenesysInfo(fileRecoverInfo);
	}
}
