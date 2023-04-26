package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.MAccessLevelInfoDao;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.service.MAccessLevelInfoService;

@Service("accessLevelInfoService")
public class MAccessLevelInfoServiceImpl implements MAccessLevelInfoService {
	@Autowired
	MAccessLevelInfoDao accessLevelInfoMapper;

	@Override
	public List<MAccessLevelInfo> selectAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.selectAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public Integer insertAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.insertAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public Integer updateAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.updateAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public Integer deleteAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.deleteAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public MAccessLevelInfo checkAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		return accessLevelInfoMapper.checkAccessLevelInfo(accessLevelInfo);
	}
	
	
	
	
	
	
	@Override
	public List<MAccessLevelInfo> selectAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.selectAllowableInfo(accessLevelInfo);
	}

	@Override
	public Integer insertAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.insertAllowableInfo(accessLevelInfo);
	}

	@Override
	public Integer updateAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.updateAllowableInfo(accessLevelInfo);
	}

	@Override
	public Integer deleteAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		return accessLevelInfoMapper.deleteAllowableInfo(accessLevelInfo);
	}
	
	@Override
	public MAccessLevelInfo checkAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		return accessLevelInfoMapper.checkAllowableInfo(accessLevelInfo);
	}

}
