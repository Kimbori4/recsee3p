package com.furence.recsee.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.PwHistoryDao;
import com.furence.recsee.admin.model.PwHistory;
import com.furence.recsee.admin.service.PwHistoryService;

@Service("pwHistoryService")
public class PwHistoryServiceImpl implements PwHistoryService {
	@Autowired
	PwHistoryDao pwHistoryMapper;

	@Override
	public Integer selectPwHistory(PwHistory pwHistory){
		return pwHistoryMapper.selectPwHistory(pwHistory);
	}

	@Override
	public Integer insertPwHistory(PwHistory pwHistory){
		return pwHistoryMapper.insertPwHistory(pwHistory);
	}
}
