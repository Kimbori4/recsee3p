package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.SubNumberInfoDao;
import com.furence.recsee.common.model.LogoVO;
import com.furence.recsee.common.model.PacketVO;
import com.furence.recsee.common.model.SubNumberInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("subNumberInfoDao")
public class SubNumberInfoDaoImpl implements SubNumberInfoDao {

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
	public List<SubNumberInfo> selectSubNumberInfo(SubNumberInfo subNumberInfo) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		return subNumberInfoMapper.selectSubNumberInfo(subNumberInfo);
	}

	@Override
	public Integer insertSubNumberInfo(SubNumberInfo subNumberInfo) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSubNumberInfo").getBoundSql(subNumberInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.insertSubNumberInfo(subNumberInfo);
	}

	@Override
	public Integer updateSubNumberInfo(SubNumberInfo subNumberInfo) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSubNumberInfo").getBoundSql(subNumberInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.updateSubNumberInfo(subNumberInfo);
	}

	@Override
	public Integer deleteSubNumberInfo(SubNumberInfo subNumberInfo) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSubNumberInfo").getBoundSql(subNumberInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.deleteSubNumberInfo(subNumberInfo);
	}
	
	@Override
	public List<LogoVO> selectLogoInfo(LogoVO logoVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		return subNumberInfoMapper.selectLogoInfo(logoVO);
	}
	
	@Override
	public Integer updateLogoInfo(LogoVO logoVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateLogoInfo").getBoundSql(logoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.updateLogoInfo(logoVO);
	}
	
	@Override
	public List<PacketVO> selectPacketSettingInfo(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		return subNumberInfoMapper.selectPacketSettingInfo(packetVO);
	}
	
	@Override
	public List<PacketVO> selectPacketLogInfo(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		return subNumberInfoMapper.selectPacketLogInfo(packetVO);
	}
	
	@Override
	public Integer selectTotalPacketLog(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		return subNumberInfoMapper.selectTotalPacketLog(packetVO);
	}

	@Override
	public Integer insertPacketSettingInfo(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPacketSettingInfo").getBoundSql(packetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.insertPacketSettingInfo(packetVO);
	}
	
	@Override
	public Integer updatePacketSettingInfo(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updatePacketSettingInfo").getBoundSql(packetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.updatePacketSettingInfo(packetVO);
	}
	
	@Override
	public Integer deletePacketSettingInfo(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deletePacketSettingInfo").getBoundSql(packetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.deletePacketSettingInfo(packetVO);
	}

	@Override
	public Integer deletePacketLogInfo(PacketVO packetVO) {
		SubNumberInfoDao subNumberInfoMapper = sqlSession.getMapper(SubNumberInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deletePacketLogInfo").getBoundSql(packetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return subNumberInfoMapper.deletePacketLogInfo(packetVO);
	}

}
