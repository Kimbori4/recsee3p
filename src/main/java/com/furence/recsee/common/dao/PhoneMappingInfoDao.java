package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.PhoneMappingInfo;

public interface PhoneMappingInfoDao {
	List<PhoneMappingInfo> selectPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
	Integer insertPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
	Integer updatePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
	Integer deletePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
}
