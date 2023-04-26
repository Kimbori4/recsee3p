package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.MAccessLevelInfo;

public interface MAccessLevelInfoDao {
	List<MAccessLevelInfo> selectAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	MAccessLevelInfo checkAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	Integer insertAccessLevelInfo(MAccessLevelInfo accessLevelInfO);
	Integer updateAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	Integer deleteAccessLevelInfo(MAccessLevelInfo accessLevelInfo);

	
	
	List<MAccessLevelInfo> selectAllowableInfo(MAccessLevelInfo accessLevelInfo);
	MAccessLevelInfo checkAllowableInfo(MAccessLevelInfo accessLevelInfo);
	Integer insertAllowableInfo(MAccessLevelInfo accessLevelInfO);
	Integer updateAllowableInfo(MAccessLevelInfo accessLevelInfo);
	Integer deleteAllowableInfo(MAccessLevelInfo accessLevelInfo);
}
