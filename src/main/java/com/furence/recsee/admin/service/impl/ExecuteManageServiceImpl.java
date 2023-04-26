package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.ExecuteManageDao;
import com.furence.recsee.admin.model.ExecuteManage;
import com.furence.recsee.admin.service.ExecuteManageService;

@Service("executeManageService")
public class ExecuteManageServiceImpl implements ExecuteManageService {
	
	@Autowired
	ExecuteManageDao executeManageMapper;

	@Override
	public List<ExecuteManage> selectExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.selectExecuteManage(executeManage);
	}

	@Override
	public ExecuteManage selectOneExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.selectOneExecuteManage(executeManage);
	}
	
	@Override
	public Integer insertExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.insertExecuteManage(executeManage);
	}

	@Override
	public Integer updateExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.updateExecuteManage(executeManage);
	}
	
	@Override
	public Integer updateFlagExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.updateFlagExecuteManage(executeManage);
	}
	
	@Override
	public Integer updateStatusExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.updateStatusExecuteManage(executeManage);
	}

	@Override
	public Integer deleteExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.deleteExecuteManage(executeManage);
	}
	
	@Override
	public Integer deleteContainExecuteManage(ExecuteManage executeManage) {
		return executeManageMapper.deleteContainExecuteManage(executeManage);
	}
	
}
