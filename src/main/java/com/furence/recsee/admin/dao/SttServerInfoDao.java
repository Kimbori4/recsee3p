package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.SttServerInfo;

public interface SttServerInfoDao {
	List<SttServerInfo> selectSttServerInfo(SttServerInfo sttServerInfo);
	Integer insertSttServerInfo(SttServerInfo sttServerInfo);
	Integer updateSttServerInfo(SttServerInfo sttServerInfo);
	Integer deleteSttServerInfo(SttServerInfo sttServerInfo);
}
