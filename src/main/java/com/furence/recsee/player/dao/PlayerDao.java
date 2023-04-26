package com.furence.recsee.player.dao;

import java.util.List;

import com.furence.recsee.player.model.PlayerInfo;

public interface PlayerDao {
	List<PlayerInfo> selectListenUrl(PlayerInfo playerInfo);

	List<PlayerInfo> selectListType1(PlayerInfo playerInfo);
	List<PlayerInfo> selectListType2(PlayerInfo playerInfo); 
	List<PlayerInfo> selectListType3(PlayerInfo playerInfo);

	List<PlayerInfo> selectFileInfo(PlayerInfo playerInfo);

	List<PlayerInfo> selectSttList(PlayerInfo playerInfo);

	List<PlayerInfo> selectListenUrl2(PlayerInfo playerInfo);
}
