package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.SttServerInfo;

public interface SttServerInfoService {
	List<SttServerInfo> selectSttServerInfo(SttServerInfo sttServerInfo);
	Integer insertSttServerInfo(SttServerInfo sttServerInfo);
	Integer updateSttServerInfo(SttServerInfo sttServerInfo);
	Integer deleteSttServerInfo(SttServerInfo sttServerInfo);
}
