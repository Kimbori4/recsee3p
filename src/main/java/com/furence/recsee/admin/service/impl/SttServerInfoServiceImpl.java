package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.SttServerInfoDao;
import com.furence.recsee.admin.model.SttServerInfo;
import com.furence.recsee.admin.service.SttServerInfoService;
import com.furence.recsee.common.dao.SystemInfoDao;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.SystemInfoService;

@Service("sttServerInfoService")
public class SttServerInfoServiceImpl implements SttServerInfoService {
	@Autowired
	SttServerInfoDao sttServerInfoMapper;

	@Override
	public List<SttServerInfo> selectSttServerInfo(SttServerInfo sttServerInfo) {
		return sttServerInfoMapper.selectSttServerInfo(sttServerInfo);
	}
	@Override
	public Integer insertSttServerInfo(SttServerInfo sttServerInfo) {
		return sttServerInfoMapper.insertSttServerInfo(sttServerInfo);
	}
	@Override
	public Integer updateSttServerInfo(SttServerInfo sttServerInfo) {
		return sttServerInfoMapper.updateSttServerInfo(sttServerInfo);
	}
	@Override
	public Integer deleteSttServerInfo(SttServerInfo sttServerInfo) {
		return sttServerInfoMapper.deleteSttServerInfo(sttServerInfo);
	}
}
