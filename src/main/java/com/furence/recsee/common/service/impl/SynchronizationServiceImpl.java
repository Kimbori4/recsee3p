package com.furence.recsee.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.SynchronizationDao;
import com.furence.recsee.common.model.SynchronizationVO;
import com.furence.recsee.common.service.SynchronizationService;

@Service("synchronizationService")
public class SynchronizationServiceImpl implements  SynchronizationService{
	
	@Autowired 
	SynchronizationDao synchronizationMapper;
	
	@Override
	public Integer insertSynchronizationInfo(SynchronizationVO synchronizationVO) {
		return synchronizationMapper.insertSynchronizationInfo(synchronizationVO);
	}
}
