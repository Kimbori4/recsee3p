package com.furence.recsee.common.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.LogInfoDao;
import com.furence.recsee.common.model.LogInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;

@Service("logInfoService")
public class LogInfoServiceImpl implements LogInfoService {
	@Autowired
	LogInfoDao logInfoMapper;

	@Override
	public List<LogInfo> selectLogInfo(LogInfo logInfo) {
		return logInfoMapper.selectLogInfo(logInfo);
	}

	@Override
	public Integer totalLogInfo(LogInfo logInfo) {
		return logInfoMapper.totalLogInfo(logInfo);
	}

	@Override
	public Integer insertLogInfo(LogInfo logInfo) {
		return logInfoMapper.insertLogInfo(logInfo);
	}

	@Override
	public Integer deleteLogInfo(LogInfo logInfo) {
		return logInfoMapper.deleteLogInfo(logInfo);
	}	
	

	private Boolean procLog(HttpServletRequest request, String contentsMsg, String etcMsg, String prevUserId) {
		String serverIp = new RecSeeUtil().getLocalServerIp();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		LogInfo logInfo = new LogInfo();

		String ip = new RecSeeUtil().getClientIp(request);
		
		logInfo.setrLogIp(ip);
		logInfo.setrLogServerIp(serverIp);
		if(userInfo != null && !StringUtil.isNull(userInfo.getUserId(),true) && !userInfo.getUserId().trim().isEmpty()) {
			logInfo.setrLogUserId(userInfo.getUserId().trim());
		}
		if(userInfo != null && !StringUtil.isNull(userInfo.getExtNo(),true) && !userInfo.getExtNo().trim().isEmpty()) {
			logInfo.setrLogUserId(userInfo.getExtNo().trim());
		} else if (prevUserId != null && !prevUserId.trim().isEmpty()) {
			logInfo.setrLogUserId(prevUserId.trim());
		}
		logInfo.setrLogContents(contentsMsg);
		if(etcMsg != null && !etcMsg.trim().isEmpty()) {
			logInfo.setrLogEtc(etcMsg.trim());
		}

		Integer result = logInfoMapper.insertLogInfo(logInfo);

		if(result > 0)
			return true;
		else
			return false;
	}

	@Override
	public Boolean writeLog(HttpServletRequest request, String contentsMsg) {
		return procLog(request, contentsMsg, null, null);
	}
	@Override
	public Boolean writeLog(HttpServletRequest request, String contentsMsg, String etcMsg) {
		return procLog(request, contentsMsg, etcMsg, null);
	}
	@Override
	public Boolean writeLog(HttpServletRequest request, String contentsMsg, String etcMsg, String prevUserId) {
		return procLog(request, contentsMsg, etcMsg, prevUserId);
	}
}
