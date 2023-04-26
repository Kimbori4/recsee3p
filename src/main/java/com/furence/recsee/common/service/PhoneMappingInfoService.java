package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.PhoneMappingInfo;

public interface PhoneMappingInfoService {
	List<PhoneMappingInfo> selectPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
	Integer insertPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
	Integer updatePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
	Integer deletePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo);
}
