package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.QueueInfo;

public interface QueueInfoService {
	List<QueueInfo> selectQueueInfo(QueueInfo queueInfo);
	Integer insertQueueInfo(QueueInfo queueInfo);
	Integer updateQueueInfo(QueueInfo queueInfo);
	Integer deleteQueueInfo(QueueInfo queueInfo);
}
