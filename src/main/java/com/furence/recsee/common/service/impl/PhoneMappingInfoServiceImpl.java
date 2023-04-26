package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.PhoneMappingInfoDao;
import com.furence.recsee.common.model.PhoneMappingInfo;
import com.furence.recsee.common.service.PhoneMappingInfoService;

@Service("phoneMappingInfoService")
public class PhoneMappingInfoServiceImpl implements PhoneMappingInfoService {
	@Autowired
	PhoneMappingInfoDao phoneMappingInfoMapper;

	@Override
	public List<PhoneMappingInfo> selectPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		return phoneMappingInfoMapper.selectPhoneMappingInfo(phoneMappingInfo);
	}
	@Override
	public Integer insertPhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		return phoneMappingInfoMapper.insertPhoneMappingInfo(phoneMappingInfo);
	}
	@Override
	public Integer updatePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		return phoneMappingInfoMapper.updatePhoneMappingInfo(phoneMappingInfo);
	}
	@Override
	public Integer deletePhoneMappingInfo(PhoneMappingInfo phoneMappingInfo) {
		return phoneMappingInfoMapper.deletePhoneMappingInfo(phoneMappingInfo);
	}
}
