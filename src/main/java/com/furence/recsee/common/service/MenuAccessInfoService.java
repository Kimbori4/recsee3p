package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;

public interface MenuAccessInfoService {

	List<MMenuAccessInfo> selectMenuAccessInfo(MMenuAccessInfo menuAccessInfo);
	List<MMenuAccessInfo> checkAccessInfo(MMenuAccessInfo menuAccessInfo);
	List<MMenuAccessInfo> selectSubMenuAccessInfo(MMenuAccessInfo menuAccessInfo);
	Integer updateMenuAccessInfo(MMenuAccessInfo menuAccessInfo);
	Integer updatecAccessInfo(MMenuAccessInfo menuAccessInfo);	
	Integer deleteMenuAccessInfo(MMenuAccessInfo menuAccessInfo);
	Integer insertMenuAccessInfo(MMenuAccessInfo menuAccessInfo);
	Integer accessLevelEmpty(MMenuAccessInfo menuAccessInfo);
	List<MMenuAccessInfo> selectHideMenuAccessInfo();
}
