package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.PlayListInfoDao;
import com.furence.recsee.common.model.PlayListInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("playListInfoDao")
public class PlayListInfoDaoImpl implements PlayListInfoDao {

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
	public List<PlayListInfo> selectUsePlayList(PlayListInfo playListInfo) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);
		return playerListMapper.selectUsePlayList(playListInfo);
	}

	@Override
	public List<PlayListInfo> playListSelect(PlayListInfo playListInfo) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);
		return playerListMapper.selectUsePlayList(playListInfo);
	}

	@Override
	public Integer playListInsert(PlayListInfo playListInfo) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("playListInsert").getBoundSql(playListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return playerListMapper.playListInsert(playListInfo);
	}

	@Override
	public Integer updateUsePlayList(PlayListInfo playListInfo) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUsePlayList").getBoundSql(playListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return playerListMapper.updateUsePlayList(playListInfo);
	}

	@Override
	public Integer playListDelete(PlayListInfo rsPlayListInfo) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("playListDelete").getBoundSql(rsPlayListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return playerListMapper.playListDelete(rsPlayListInfo);
	}

	@Override
	public Integer playListUpdateNum(PlayListInfo rsPlayNumInto) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("playListUpdateNum").getBoundSql(rsPlayNumInto);
		synchronizationUtil.SynchronizationInsert(query);
		
		return playerListMapper.playListUpdateNum(rsPlayNumInto);
	}

	@Override
	public Integer insertUsePlayList(PlayListInfo rsPlayListInfo) {
		PlayListInfoDao playerListMapper = sqlSession.getMapper(PlayListInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertUsePlayList").getBoundSql(rsPlayListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return playerListMapper.insertUsePlayList(rsPlayListInfo);
	}

}
