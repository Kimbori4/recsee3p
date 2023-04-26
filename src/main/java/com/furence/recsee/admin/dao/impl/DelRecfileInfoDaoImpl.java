package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.DelRecfileInfoDao;
import com.furence.recsee.admin.model.DelRecfileInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.main.model.SearchListInfo;

@Repository("delRecfileInfoDao")
public class DelRecfileInfoDaoImpl implements DelRecfileInfoDao {
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
	public List<DelRecfileInfo> selectDelRecfileInfo() {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.selectDelRecfileInfo();
	}
	
	@Override
	public Integer insertDelRecfileInfo(DelRecfileInfo delRecfileInfo) {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.insertDelRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public Integer updateDelRecfileInfo(DelRecfileInfo delRecfileInfo) {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.updateDelRecfileInfo(delRecfileInfo);
	}

	@Override
	public Integer deleteDelRecfileInfo(DelRecfileInfo delRecfileInfo) {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.deleteDelRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public Integer updatecRsBackFileFlag(DelRecfileInfo delRecfileInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.updatecRsBackFileFlag(delRecfileInfo);
	}
	
	@Override
	public List<DelRecfileInfo> selectBackRecfileInfo() {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.selectBackRecfileInfo();
	}
	
	@Override
	public Integer insertBackRecfileInfo(DelRecfileInfo delRecfileInfo) {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.insertBackRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public Integer updateBackRecfileInfo(DelRecfileInfo delRecfileInfo) {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.updateBackRecfileInfo(delRecfileInfo);
	}

	@Override
	public Integer deleteBackRecfileInfo(DelRecfileInfo delRecfileInfo) {

		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.deleteBackRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public List<DelRecfileInfo> selectNowBackRecfileInfo(DelRecfileInfo delRecfileInfo) {
		
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.selectNowBackRecfileInfo(delRecfileInfo);
	}
	@Override
	public List<DelRecfileInfo> selectNowBackRecfilePathCheck(DelRecfileInfo delRecfileInfo) {
		
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.selectNowBackRecfilePathCheck(delRecfileInfo);
	}
	
	@Override
	public Integer countRecfileFileBackup(DelRecfileInfo delRecfileInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.countRecfileFileBackup(delRecfileInfo);
	}
	
	@Override
	public List<SearchListInfo> selectRecfileFileBackup(DelRecfileInfo delRecfileInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.selectRecfileFileBackup(delRecfileInfo);
	}
	
	@Override
	public Integer updateRsRecfileBackCheck(SearchListInfo searchListInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.updateRsRecfileBackCheck(searchListInfo);
	}
	
	@Override
	public Integer updateRsRecfileListenUrlUpdate(SearchListInfo searchListInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.updateRsRecfileListenUrlUpdate(searchListInfo);
	}
	
	@Override
	public Integer updateRsRecfileMove(SearchListInfo searchListInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);

		return delRecfileInfoMapper.updateRsRecfileMove(searchListInfo);
	}
	
	@Override
	public Integer FileSizeRecfileFileBackup(DelRecfileInfo delRecfileInfo) {
		DelRecfileInfoDao delRecfileInfoMapper = sqlSession.getMapper(DelRecfileInfoDao.class);
		
		return delRecfileInfoMapper.FileSizeRecfileFileBackup(delRecfileInfo);
	}
}
