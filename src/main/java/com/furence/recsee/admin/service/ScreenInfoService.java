package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.ScreenInfo;

public interface ScreenInfoService {
	List<ScreenInfo> selectUserInfo(ScreenInfo screenInfo);
	List<ScreenInfo> selectScreenUserInfo(ScreenInfo screenInfo);
	Integer updateScreenUser(ScreenInfo screenInfo);
}
