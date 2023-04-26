package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.JobManageDao;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.admin.service.JobManageService;

@Service("jobManageService")
public class JobManageServiceImpl implements JobManageService {
	
	@Autowired
	JobManageDao jobManageMapper;

	@Override
	public List<JobManage> selectJobManage(JobManage jobManage) {
		return jobManageMapper.selectJobManage(jobManage);
	}
	
	@Override
	public JobManage selectOneJobManage(JobManage jobManage) {
		return jobManageMapper.selectOneJobManage(jobManage);
	}

	@Override
	public Integer insertJobManage(JobManage jobManage) {
		return jobManageMapper.insertJobManage(jobManage);
	}

	@Override
	public Integer updateJobManage(JobManage jobManage) {
		return jobManageMapper.updateJobManage(jobManage);
	}

	@Override
	public Integer deleteJobManage(JobManage jobManage) {
		return jobManageMapper.deleteJobManage(jobManage);
		
	}
	
}
