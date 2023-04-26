package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.MAccessLevelInfo;

public interface MAccessLevelInfoService {
	List<MAccessLevelInfo> selectAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	MAccessLevelInfo checkAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	Integer insertAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	Integer updateAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	Integer deleteAccessLevelInfo(MAccessLevelInfo accessLevelInfo);
	
	
	List<MAccessLevelInfo> selectAllowableInfo(MAccessLevelInfo accessLevelInfo);
	MAccessLevelInfo checkAllowableInfo(MAccessLevelInfo accessLevelInfo);
	Integer insertAllowableInfo(MAccessLevelInfo accessLevelInfo);
	Integer updateAllowableInfo(MAccessLevelInfo accessLevelInfo);
	Integer deleteAllowableInfo(MAccessLevelInfo accessLevelInfo);
}
