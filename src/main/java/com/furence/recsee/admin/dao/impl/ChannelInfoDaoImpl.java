package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.ChannelInfoDao;
import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("channelInfoDao")
public class ChannelInfoDaoImpl implements ChannelInfoDao {
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
	public List<ChannelInfo> selectChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		return channelInfoMapper.selectChannelInfo(channelInfo);
	}

	@Override
	public List<ChannelInfo> groupChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		return channelInfoMapper.groupChannelInfo(channelInfo);
	}

	@Override
	public Integer insertChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertChannelInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return channelInfoMapper.insertChannelInfo(channelInfo);
	}

	@Override
	public Integer updateChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateChannelInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return channelInfoMapper.updateChannelInfo(channelInfo);
	}

	@Override
	public Integer deleteChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteChannelInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return channelInfoMapper.deleteChannelInfo(channelInfo);
	}

	@Override
	public List<ChannelInfo> selectChannelInfoGet() {
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		return channelInfoMapper.selectChannelInfoGet();
	}

	@Override
	public List<ChannelInfo> selectChannelMonitoringInfo(ChannelInfo channelInfo) {
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		return channelInfoMapper.selectChannelMonitoringInfo(channelInfo);
	}

	@Override
	public List<ChannelInfo> selectChannelMonitoringExtInfo(ChannelInfo channelInfo) {
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		return channelInfoMapper.selectChannelMonitoringExtInfo(channelInfo);
	}

	@Override
	public Integer checkIpOverlap(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("checkIpOverlap").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return channelInfoMapper.checkIpOverlap(channelInfo);
	}



	@Override
	public Integer checkExtOverlap(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		return channelInfoMapper.checkExtOverlap(channelInfo);
	}

	@Override
	public Integer updateSwitchChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSwitchChannelInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return channelInfoMapper.updateSwitchChannelInfo(channelInfo);
	}

	@Override
	public Integer updateReturningSwitchChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		ChannelInfoDao channelInfoMapper = sqlSession.getMapper(ChannelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateReturningSwitchChannelInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return channelInfoMapper.updateReturningSwitchChannelInfo(channelInfo);
	}
}
