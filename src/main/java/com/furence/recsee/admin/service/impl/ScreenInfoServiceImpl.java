package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.ScreenInfoDao;
import com.furence.recsee.admin.model.ScreenInfo;
import com.furence.recsee.admin.service.ScreenInfoService;

@Service("screenInfoService")
public class ScreenInfoServiceImpl implements ScreenInfoService {
	@Autowired
	ScreenInfoDao screenInfoMapper;

	@Override
	public List<ScreenInfo> selectUserInfo(ScreenInfo screenInfo) {

		return screenInfoMapper.selectUserInfo(screenInfo);
	}

	@Override
	public List<ScreenInfo> selectScreenUserInfo(ScreenInfo screenInfo) {

		return screenInfoMapper.selectScreenUserInfo(screenInfo);
	}

	@Override
	public Integer updateScreenUser(ScreenInfo screenInfo) {
		return screenInfoMapper.updateScreenUser(screenInfo);
	}
}
