package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.ExecuteManage;

public interface ExecuteManageService {
	List<ExecuteManage> selectExecuteManage(ExecuteManage executeManage);
	ExecuteManage selectOneExecuteManage(ExecuteManage executeManage);
	Integer insertExecuteManage(ExecuteManage executeManage);
	Integer updateExecuteManage(ExecuteManage executeManage);
	Integer updateFlagExecuteManage(ExecuteManage executeManage);
	Integer updateStatusExecuteManage(ExecuteManage executeManage);
	Integer deleteExecuteManage(ExecuteManage executeManage);
	Integer deleteContainExecuteManage(ExecuteManage executeManage);
}
