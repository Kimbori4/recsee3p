package com.furence.recsee.common.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.furence.recsee.common.model.Log;

public interface LogService {
	List<Log> selectLog(Log log);
	List<Log> selectLogName(Log log);
	List<Log> selectLogContents(Log log);
	Integer totalLog(Log log);
	Integer insertLog(Log log);
	Integer deleteLog(Log log);
	Boolean writeLog(HttpServletRequest request, String code, String detailCode, String etcMsg);
	Boolean writeLog(HttpServletRequest request, String code, String detailCode, String etcMsg, String prevUserId);
}
