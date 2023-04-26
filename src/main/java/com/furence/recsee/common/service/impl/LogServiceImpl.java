package com.furence.recsee.common.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.LogDao;
import com.furence.recsee.common.model.Log;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;

@Service("logService")
public class LogServiceImpl implements LogService {
	private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
	@Autowired
	LogDao logMapper;

	@Override
	public List<Log> selectLog(Log log) {
		return logMapper.selectLog(log);
	}
	
	@Override
	public List<Log> selectLogName(Log log){
		return logMapper.selectLogName(log);
	}
	
	@Override
	public List<Log> selectLogContents(Log log){
		return logMapper.selectLogContents(log);
	}

	@Override
	public Integer totalLog(Log log) {
		return logMapper.totalLog(log);
	}

	@Override
	public Integer insertLog(Log log) {
		return logMapper.insertLog(log);
	}

	@Override
	public Integer deleteLog(Log log) {
		return logMapper.deleteLog(log);
	}

	private Boolean procLog(HttpServletRequest request, String code, String detailCode, String etcMsg, String prevUserId) {
		String serverIp = new RecSeeUtil().getLocalServerIp();
		String clientIp = ""; // 스케줄러에서 로그 남길때 request가 null 이기 때문에 예외처리...
		
		try {
			new RecSeeUtil();
			clientIp = RecSeeUtil.getClientIp(request);
		} catch(Exception e) {
			logger.error("error",e);
		}
		
		if("0:0:0:0:0:0:0:1".equals(clientIp)) {
			clientIp = "127.0.0.1";
		}
		
		LoginVO userInfo = new LoginVO();
		
		try { 
			userInfo = SessionManager.getUserInfo(request);
		} catch(Exception e) {
			logger.error("error",e);
		}

		Log log = new Log();
		
		log.setrLogIp(clientIp);
		log.setrLogServerIp(serverIp);
		if(userInfo != null && !StringUtil.isNull(userInfo.getUserId(),true)) {
			log.setrLogUserId(userInfo.getUserId().trim());
		} else if (prevUserId != null && !prevUserId.trim().isEmpty()) {
			log.setrLogUserId(prevUserId.trim());
		}
		
		if(!StringUtil.isNull(etcMsg,true)) {
			log.setrLogEtc(etcMsg.trim());
		}
		
		log.setrLogCode(code);
		log.setrLogDetailCode(detailCode);

		Integer result = logMapper.insertLog(log);

		if(result > 0)
			return true;
		else
			return false;

	}

	@Override
	public Boolean writeLog(HttpServletRequest request, String code, String detailCode, String etcMsg) {
		return procLog(request, code, detailCode, etcMsg, null);
	}
	@Override
	public Boolean writeLog(HttpServletRequest request, String code, String detailCode, String etcMsg, String prevUserId) {
		return procLog(request, code, detailCode, etcMsg, prevUserId);
	}
}
