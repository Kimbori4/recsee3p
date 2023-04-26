package com.furence.recsee.player.dao.Impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.furence.recsee.player.dao.PlayerDao;
import com.furence.recsee.player.model.PlayerInfo;

public class PlayerDaoImpl implements PlayerDao{
	
	@Autowired
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public List<PlayerInfo> selectListenUrl(PlayerInfo PlayerInfo){
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectListenUrl(PlayerInfo);
	}
	
	@Override
	public List<PlayerInfo> selectListType1(PlayerInfo playerInfo) {
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectListType1(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectListType2(PlayerInfo playerInfo) {
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectListType2(playerInfo);
	}
	
	@Override
	public List<PlayerInfo> selectListType3(PlayerInfo playerInfo) {
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectListType3(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectFileInfo(PlayerInfo playerInfo) {
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectFileInfo(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectSttList(PlayerInfo playerInfo) {
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectSttList(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectListenUrl2(PlayerInfo playerInfo) {
		PlayerDao playerInfoMapper = sqlSession.getMapper(PlayerDao.class);
		
		return playerInfoMapper.selectListenUrl2(playerInfo);
	}
}
