package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.model.PlayListInfo;
import com.furence.recsee.common.dao.PlayListInfoDao;
import com.furence.recsee.common.service.PlayListInfoService;


@Service("playListInfoService")
public class PlayListInfoServiceImpl implements PlayListInfoService {
	
	@Autowired
	PlayListInfoDao playListInfoMapper;

	@Override
	public List<PlayListInfo> selectUsePlayList(PlayListInfo playListInfo) {
		return playListInfoMapper.selectUsePlayList(playListInfo);
	}

	@Override
	public List<PlayListInfo> playListSelect(PlayListInfo rsPlayListInfo) {
		return playListInfoMapper.playListSelect(rsPlayListInfo);
	}

	@Override
	public Integer playListInsert(PlayListInfo rsPlayListInfo) {
		return playListInfoMapper.playListInsert(rsPlayListInfo);
	}

	@Override
	public Integer updateUsePlayList(PlayListInfo rsPlayListInfo) {
		return playListInfoMapper.updateUsePlayList(rsPlayListInfo);
	}

	@Override
	public Integer playListDelete(PlayListInfo rsPlayListInfo) {
		return playListInfoMapper.playListDelete(rsPlayListInfo);
	}

	@Override
	public Integer playListUpdateNum(PlayListInfo rsPlayNumInto) {
		return playListInfoMapper.playListUpdateNum(rsPlayNumInto);
	}

	@Override
	public Integer insertUsePlayList(PlayListInfo rsPlayListInfo) {
		return playListInfoMapper.insertUsePlayList(rsPlayListInfo);
	}

}
