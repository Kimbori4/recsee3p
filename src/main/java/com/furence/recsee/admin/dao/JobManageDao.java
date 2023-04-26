package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.JobManage;

public interface JobManageDao {
	List<JobManage> selectJobManage(JobManage jobManage);
	JobManage selectOneJobManage(JobManage jobManage);
	Integer insertJobManage(JobManage jobManage);
	Integer updateJobManage(JobManage jobManage);
	Integer deleteJobManage(JobManage jobManage);
}
