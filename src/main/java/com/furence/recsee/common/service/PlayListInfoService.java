package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.PlayListInfo;

public interface PlayListInfoService {
	List<PlayListInfo> selectUsePlayList(PlayListInfo playListInfo);

	List<PlayListInfo> playListSelect(PlayListInfo rsPlayListInfo);

	Integer playListInsert(PlayListInfo rsPlayListInfo);

	Integer updateUsePlayList(PlayListInfo rsPlayListInfo);

	Integer playListDelete(PlayListInfo rsPlayListInfo);

	Integer playListUpdateNum(PlayListInfo rsPlayNumInto);

	Integer insertUsePlayList(PlayListInfo rsPlayListInfo);

}
