package com.furence.recsee.admin.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.RUserInfoDao;
import com.furence.recsee.admin.model.MultiPartInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.admin.service.RUserInfoService;

@Service("ruserInfoService")
public class RUserInfoServiceImpl implements RUserInfoService {
	@Autowired
	RUserInfoDao ruserInfoMapper;

	@Override
	public List<RUserInfo> selectRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.selectRUserInfo(ruserInfo);
	}

	@Override
	public List<RUserInfo> selectTreeViewRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.selectTreeViewRUserInfo(ruserInfo);
	}
	@Override
	public Integer insertRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.insertRUserInfo(ruserInfo);
	}

	@Override
	public Integer updateRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.updateRUserInfo(ruserInfo);
	}

	@Override
	public Integer deleteRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.deleteRUserInfo(ruserInfo);
	}

	@Override
	public Integer userLevelEmpty(RUserInfo ruserInfo) {
		return ruserInfoMapper.userLevelEmpty(ruserInfo);
	}

	@Override
	public List<MultiPartInfo> selectMultiPartInfo(MultiPartInfo multiPartInfo) {
		return ruserInfoMapper.selectMultiPartInfo(multiPartInfo);
	}

	@Override
	public List<HashMap<String, String>> countMultiPartInfo(MultiPartInfo multiPartInfo) {
		return ruserInfoMapper.countMultiPartInfo(multiPartInfo);
	}

	@Override
	public Integer insertMultiPartInfo(MultiPartInfo multiPartInfo) {
		return ruserInfoMapper.insertMultiPartInfo(multiPartInfo);
	}

	@Override
	public Integer deleteMultiPartInfo(MultiPartInfo multiPartInfo) {
		return ruserInfoMapper.deleteMultiPartInfo(multiPartInfo);
	}

	@Override
	public List<RUserInfo> selectPeople(RUserInfo ruserInfo) {
		return ruserInfoMapper.selectPeople(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminUserManageSelect(RUserInfo ruserInfo) {
		return ruserInfoMapper.adminUserManageSelect(ruserInfo);
	}

	@Override
	public Integer CountadminUserManageSelect(RUserInfo ruserInfo) {
		return ruserInfoMapper.CountadminUserManageSelect(ruserInfo);
	}

	@Override
	public Integer CountadminAUserManageSelect(RUserInfo ruserInfo) {
		return ruserInfoMapper.CountadminAUserManageSelect(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminAUserManageSelect(RUserInfo ruserInfo) {
		return ruserInfoMapper.adminAUserManageSelect(ruserInfo);
	}

	@Override
	public Integer checkId(RUserInfo ruserInfo) {
		return ruserInfoMapper.checkId(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminAUserManageSelectTree(RUserInfo rUserInfo) {
		return ruserInfoMapper.adminAUserManageSelectTree(rUserInfo);
	}
	
	@Override
	public List<RUserInfo> adminUserManageSelectExcel(RUserInfo rUserInfo) {
		return ruserInfoMapper.adminUserManageSelectExcel(rUserInfo);
	}
	
	@Override
	public Integer multiGroupModify(RUserInfo ruserInfo) {
		return ruserInfoMapper.multiGroupModify(ruserInfo);
	}
	
	@Override
	public Integer checkPhone(String phoneNumber) {
		return ruserInfoMapper.checkPhone(phoneNumber);
	}
	
	@Override
	public Integer deleteMgRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.deleteMgRUserInfo(ruserInfo);
	}
	
	@Override
	public Integer upsertRUserInfo(RUserInfo ruserInfo) {
		return ruserInfoMapper.upsertRUserInfo(ruserInfo);
	}
	
	@Override
	public Integer mobileUsage() {
		return ruserInfoMapper.mobileUsage();
	}

	@Override
	public Integer checkIP(RUserInfo ruserInfo) {
		return ruserInfoMapper.checkIP(ruserInfo);
	}

	@Override
	public Integer unlockUser(RUserInfo ruserInfo) {
		return ruserInfoMapper.unlockUser(ruserInfo);
	}
	
	@Override
	public List<RUserInfo> mobileManageSelect(RUserInfo ruserInfo) {
		return ruserInfoMapper.mobileManageSelect(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminAUserManageRealTimeSelect(RUserInfo ruserInfo) {
		return ruserInfoMapper.adminAUserManageRealTimeSelect(ruserInfo);
	}
}
