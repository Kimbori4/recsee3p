package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.SystemInfo;

public interface SystemInfoDao {
	List<SystemInfo> selectSystemInfo(SystemInfo systemInfo);
	Integer insertSystemInfo(SystemInfo systemInfo);
	Integer updateSystemInfo(SystemInfo systemInfo);
	Integer deleteSystemInfo(SystemInfo systemInfo);
	List<SystemInfo> selectLicenceInfo(SystemInfo systemInfo);
	Integer insertLicenceInfo(SystemInfo systemInfo);
	Integer updateLicenceInfo(SystemInfo systemInfo);
	Integer deleteLicenceInfo(SystemInfo systemInfo);
	Integer insertSysDeleteInfo(SystemInfo systemInfo);
	Integer deleteSysDeleteInfo(SystemInfo systemInfo);
}
