package com.furence.recsee.admin.service;

import java.util.HashMap;
import java.util.List;

import com.furence.recsee.admin.model.MultiPartInfo;
import com.furence.recsee.admin.model.RUserInfo;

public interface RUserInfoService {
	List<RUserInfo> selectRUserInfo(RUserInfo ruserInfo);
	List<RUserInfo> selectTreeViewRUserInfo(RUserInfo ruserInfo);
	Integer insertRUserInfo(RUserInfo ruserInfo);
	Integer updateRUserInfo(RUserInfo ruserInfo);
	Integer deleteRUserInfo(RUserInfo ruserInfo);
	Integer userLevelEmpty(RUserInfo ruserInfo);
	List<MultiPartInfo> selectMultiPartInfo(MultiPartInfo multiPartInfo);
	List<HashMap<String, String>> countMultiPartInfo(MultiPartInfo multiPartInfo);
	Integer insertMultiPartInfo(MultiPartInfo multiPartInfo);
	Integer deleteMultiPartInfo(MultiPartInfo multiPartInfo);
	List<RUserInfo> selectPeople(RUserInfo ruserInfo);
	List<RUserInfo> adminUserManageSelect(RUserInfo ruserInfo);
	List<RUserInfo> adminUserManageSelectExcel(RUserInfo rUserInfo);
	Integer CountadminUserManageSelect(RUserInfo ruserInfo);
	Integer CountadminAUserManageSelect(RUserInfo ruserInfo);
	List<RUserInfo> adminAUserManageSelect(RUserInfo ruserInfo);
	Integer checkId(RUserInfo ruserInfo);
	List<RUserInfo> adminAUserManageSelectTree(RUserInfo rUserInfo);
	Integer multiGroupModify(RUserInfo ruserInfo);
	Integer deleteMgRUserInfo(RUserInfo ruserInfo);
	Integer upsertRUserInfo(RUserInfo ruserInfo);
	
	Integer checkPhone(String phoneNumber);
	Integer mobileUsage();
	Integer checkIP(RUserInfo ruserInfo);
	Integer unlockUser(RUserInfo ruserInfo);
	
	List<RUserInfo> mobileManageSelect(RUserInfo ruserInfo);

	List<RUserInfo> adminAUserManageRealTimeSelect(RUserInfo ruserInfo);
}
