package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.RecStatusInfoDao;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.monitoring.model.DashboardInfo;

@Repository("recStatusInfoDao")
public class RecStatusInfoDaoImpl implements RecStatusInfoDao {

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
	public List<DashboardInfo> selectFirstRecfileInfo(DashboardInfo dashboardInfo){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		return RecStatusInfoMapper.selectFirstRecfileInfo(dashboardInfo);
	}
	@Override
	public Integer insertRecStatus(DashboardInfo dashboardInfo){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertRecStatus").getBoundSql(dashboardInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return RecStatusInfoMapper.insertRecStatus(dashboardInfo);
	}
	@Override
	public Integer updateRecStatus(DashboardInfo dashboardInfo){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRecStatus").getBoundSql(dashboardInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return RecStatusInfoMapper.updateRecStatus(dashboardInfo);
	}
	@Override
	public List<DashboardInfo> selectRecfileInfo(DashboardInfo dashboardInfo){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		return RecStatusInfoMapper.selectRecfileInfo(dashboardInfo);
	}
	@Override
	public Integer updateEtcConfigTime(String dateTime){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEtcConfigTime").getBoundSql(dateTime);
		synchronizationUtil.SynchronizationInsert(query);
		
		return RecStatusInfoMapper.updateEtcConfigTime(dateTime);
	}
	@Override
	public Integer selectRecStatus(){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		return RecStatusInfoMapper.selectRecStatus();
	}
	@Override
	public String selectEtcConfigTime(){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		return RecStatusInfoMapper.selectEtcConfigTime();
	}
	@Override
	public DashboardInfo selectCompRecData(DashboardInfo dashboardInfo){
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		return RecStatusInfoMapper.selectCompRecData(dashboardInfo);
	}

	@Override
	public Integer insertSttList(String sysCode) {
		RecStatusInfoDao RecStatusInfoMapper =  sqlSession.getMapper(RecStatusInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSttList").getBoundSql(sysCode);
		synchronizationUtil.SynchronizationInsert(query);
		
		return RecStatusInfoMapper.insertSttList(sysCode);
	}
}
