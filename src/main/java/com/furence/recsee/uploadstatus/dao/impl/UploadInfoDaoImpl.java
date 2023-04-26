package com.furence.recsee.uploadstatus.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.uploadstatus.dao.UploadInfoDao;
import com.furence.recsee.uploadstatus.model.UploadInfo;

@Repository("uploadInfoDao")
public class UploadInfoDaoImpl implements UploadInfoDao {
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
	public List<UploadInfo> selectUploadInfo(UploadInfo uploadInfo) {
		UploadInfoDao uploadInfoMapper = sqlSession.getMapper(UploadInfoDao.class);
		return uploadInfoMapper.selectUploadInfo(uploadInfo);
	}
	@Override
	public List<UploadInfo> selectUploadGroupInfo(UploadInfo uploadInfo) {
		UploadInfoDao uploadInfoMapper = sqlSession.getMapper(UploadInfoDao.class);
		return uploadInfoMapper.selectUploadGroupInfo(uploadInfo);
	}

	@Override
	public List<UploadInfo> selectServerCount(UploadInfo uploadInfo) {
		UploadInfoDao uploadInfoMapper = sqlSession.getMapper(UploadInfoDao.class);
		return uploadInfoMapper.selectServerCount(uploadInfo);
	}

	@Override
	public Integer CountUploadSelect(UploadInfo uploadInfo) {
		UploadInfoDao uploadInfoMapper = sqlSession.getMapper(UploadInfoDao.class);
		return uploadInfoMapper.CountUploadSelect(uploadInfo);
	}

	@Override
	public Integer insertUpload(UploadInfo uploadInfo) {
		UploadInfoDao uploadInfoMapper = sqlSession.getMapper(UploadInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertUpload").getBoundSql(uploadInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return uploadInfoMapper.insertUpload(uploadInfo);
	}
	
}
