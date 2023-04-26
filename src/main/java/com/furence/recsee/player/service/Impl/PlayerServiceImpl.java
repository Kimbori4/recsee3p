package com.furence.recsee.player.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.player.dao.PlayerDao;
import com.furence.recsee.player.model.PlayerInfo;
import com.furence.recsee.player.service.PlayerService;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService{
	@Autowired
	PlayerDao PlayerDaoMapper;
	
	@Override
	public List<PlayerInfo> selectListenUrl(PlayerInfo playerInfo){
		return PlayerDaoMapper.selectListenUrl(playerInfo);
	}
	
	@Override
	public List<PlayerInfo> selectListType1(PlayerInfo playerInfo) {
		return PlayerDaoMapper.selectListType1(playerInfo);
	}
	
	@Override
	public List<PlayerInfo> selectListType2(PlayerInfo playerInfo) {
		return PlayerDaoMapper.selectListType2(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectListType3(PlayerInfo playerInfo) {
		return PlayerDaoMapper.selectListType3(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectFileInfo(PlayerInfo playerInfo) {
		return PlayerDaoMapper.selectFileInfo(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectSttList(PlayerInfo playerInfo) {
		return PlayerDaoMapper.selectSttList(playerInfo);
	}

	@Override
	public List<PlayerInfo> selectListenUrl2(PlayerInfo playerInfo) {
		return PlayerDaoMapper.selectListenUrl2(playerInfo);
	}
}
