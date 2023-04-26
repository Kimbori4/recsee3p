package com.furence.recsee.common.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.furence.recsee.common.model.LogInfo;
import com.furence.recsee.common.model.MeritUser;

public interface LogInfoService {
	List<LogInfo> selectLogInfo(LogInfo logInfo);
	Integer totalLogInfo(LogInfo logInfo);
	Integer insertLogInfo(LogInfo logInfo);
	Integer deleteLogInfo(LogInfo logInfo);
	Boolean writeLog(HttpServletRequest request, String contentsMsg);
	Boolean writeLog(HttpServletRequest request, String contentsMsg, String etcMsg);
	Boolean writeLog(HttpServletRequest request, String contentsMsg, String etcMsg, String prevUserId);
	
}
