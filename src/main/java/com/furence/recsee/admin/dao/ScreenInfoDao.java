package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.ScreenInfo;

public interface ScreenInfoDao {
	List<ScreenInfo> selectUserInfo(ScreenInfo screenInfo);
	List<ScreenInfo> selectScreenUserInfo(ScreenInfo screenInfo);
	Integer updateScreenUser(ScreenInfo screenInfo);
}
