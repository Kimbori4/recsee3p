package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.SystemInfoDao;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.SystemInfoService;

@Service("systemInfoService")
public class SystemInfoServiceImpl implements SystemInfoService {
	@Autowired
	SystemInfoDao systemInfoMapper;

	@Override
	public List<SystemInfo> selectSystemInfo(SystemInfo systemInfo) {
		return systemInfoMapper.selectSystemInfo(systemInfo);
	}
	@Override
	public Integer insertSystemInfo(SystemInfo systemInfo) {
		return systemInfoMapper.insertSystemInfo(systemInfo);
	}
	@Override
	public Integer updateSystemInfo(SystemInfo systemInfo) {
		return systemInfoMapper.updateSystemInfo(systemInfo);
	}
	@Override
	public Integer deleteSystemInfo(SystemInfo systemInfo) {
		return systemInfoMapper.deleteSystemInfo(systemInfo);
	}
	@Override
	public List<SystemInfo> selectLicenceInfo(SystemInfo systemInfo) {
		return systemInfoMapper.selectLicenceInfo(systemInfo);
	}
	@Override
	public Integer insertLicenceInfo(SystemInfo systemInfo) {
		return systemInfoMapper.insertLicenceInfo(systemInfo);
	}
	@Override
	public Integer updateLicenceInfo(SystemInfo systemInfo) {
		return systemInfoMapper.updateLicenceInfo(systemInfo);
	}
	@Override
	public Integer deleteLicenceInfo(SystemInfo systemInfo) {
		return systemInfoMapper.deleteLicenceInfo(systemInfo);
	}
	@Override
	public Integer insertSysDeleteInfo(SystemInfo systemInfo) {
		return systemInfoMapper.insertSysDeleteInfo(systemInfo);
	}
	@Override
	public Integer deleteSysDeleteInfo(SystemInfo systemInfo) {
		return systemInfoMapper.deleteSysDeleteInfo(systemInfo);
	}
}
