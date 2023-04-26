package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.LogInfo;
import com.furence.recsee.common.model.MeritUser;

public interface LogInfoDao {
	List<LogInfo> selectLogInfo(LogInfo logInfo);
	List<LogInfo> selectSystemLogInfo(LogInfo logInfo);
	Integer totalLogInfo(LogInfo logInfo);
	Integer insertLogInfo(LogInfo logInfo);
	Integer deleteLogInfo(LogInfo logInfo);
}
