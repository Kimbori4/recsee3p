package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.dao.AllowableRangeInfoDao;
import com.furence.recsee.admin.service.AllowableRangeInfoService;

@Service("allowableRangeInfoService")
public class AllowableRangeInfoServiceImpl implements AllowableRangeInfoService {

	@Autowired
	AllowableRangeInfoDao allowableRangeInfoMapper;

	@Override
	public List<AllowableRangeInfo> selectAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {
		return allowableRangeInfoMapper.selectAllowableRangeInfo(allowableRangeInfo);
	}

	@Override
	public AllowableRangeInfo checkAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {
		return allowableRangeInfoMapper.checkAllowableRangeInfo(allowableRangeInfo);
	}
	
	@Override
	public Integer deleteAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {
		return allowableRangeInfoMapper.deleteAllowableRangeInfo(allowableRangeInfo);
	}
	
	@Override
	public Integer insertAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo) {
		return allowableRangeInfoMapper.insertAllowableRangeInfo(allowableRangeInfo);
	}
}
