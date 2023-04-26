package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.QueueInfo;

public interface QueueInfoDao {
	List<QueueInfo> selectQueueInfo(QueueInfo queueInfo);
	Integer insertQueueInfo(QueueInfo queueInfo);
	Integer updateQueueInfo(QueueInfo queueInfo);
	Integer deleteQueueInfo(QueueInfo queueInfo);
}
