package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.MMenuAccessInfoDao;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.MenuAccessInfoService;

@Service("menuAccessInfoService")
public class MenuAccessInfoServiceImpl implements MenuAccessInfoService {

	@Autowired
	MMenuAccessInfoDao menuAccessInfoMapper;

	@Override
	public List<MMenuAccessInfo> selectMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.selectMenuAccessInfo(menuAccessInfo);
	}
	@Override
	public List<MMenuAccessInfo> checkAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.checkAccessInfo(menuAccessInfo);
	}
	@Override
	public List<MMenuAccessInfo> selectSubMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.selectSubMenuAccessInfo(menuAccessInfo);
	}
	@Override
	public Integer updateMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.updateMenuAccessInfo(menuAccessInfo);
	}	
	@Override
	public Integer updatecAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.updatecAccessInfo(menuAccessInfo);
	}
	@Override
	public Integer deleteMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.deleteMenuAccessInfo(menuAccessInfo);
	}
	@Override
	public Integer insertMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.insertMenuAccessInfo(menuAccessInfo);
	}
	@Override
	public Integer accessLevelEmpty(MMenuAccessInfo menuAccessInfo) {
		return menuAccessInfoMapper.accessLevelEmpty(menuAccessInfo);
	}
	@Override
	public List<MMenuAccessInfo> selectHideMenuAccessInfo() {
		return menuAccessInfoMapper.selectHideMenuAccessInfo();
	}
}
