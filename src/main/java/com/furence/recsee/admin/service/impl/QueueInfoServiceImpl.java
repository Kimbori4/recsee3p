package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.QueueInfoDao;
import com.furence.recsee.admin.model.QueueInfo;
import com.furence.recsee.admin.service.QueueInfoService;

@Service("queueInfoService")
public class QueueInfoServiceImpl implements QueueInfoService {
	@Autowired
	QueueInfoDao	queueInfoMapper;

	@Override
	public List<QueueInfo> selectQueueInfo(QueueInfo queueInfo) {
		return queueInfoMapper.selectQueueInfo(queueInfo);
	}

	@Override
	public Integer insertQueueInfo(QueueInfo queueInfo) {
		return queueInfoMapper.insertQueueInfo(queueInfo);
	}

	@Override
	public Integer updateQueueInfo(QueueInfo queueInfo) {
		return queueInfoMapper.updateQueueInfo(queueInfo);
	}

	@Override
	public Integer deleteQueueInfo(QueueInfo queueInfo) {
		return queueInfoMapper.deleteQueueInfo(queueInfo);
	}
}
