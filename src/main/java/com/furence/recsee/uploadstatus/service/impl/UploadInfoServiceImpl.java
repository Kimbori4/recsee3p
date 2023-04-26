package com.furence.recsee.uploadstatus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.uploadstatus.dao.UploadInfoDao;
import com.furence.recsee.uploadstatus.model.UploadInfo;
import com.furence.recsee.uploadstatus.service.UploadInfoService;

@Service("uploadInfoService")
public class UploadInfoServiceImpl implements UploadInfoService {
	@Autowired
	UploadInfoDao uploadInfoMapper;

	@Override
	public List<UploadInfo> selectUploadInfo(UploadInfo uploadInfo) {
		// TODO Auto-generated method stub
		return uploadInfoMapper.selectUploadInfo(uploadInfo);
	}
	@Override
	public List<UploadInfo> selectUploadGroupInfo(UploadInfo uploadInfo) {
		// TODO Auto-generated method stub
		return uploadInfoMapper.selectUploadGroupInfo(uploadInfo);
	}
	@Override
	public List<UploadInfo> selectServerCount(UploadInfo uploadInfo) {
		// TODO Auto-generated method stub
		return uploadInfoMapper.selectServerCount(uploadInfo);
	}
	@Override
	public Integer CountUploadSelect(UploadInfo uploadInfo) {
		// TODO Auto-generated method stub
		return uploadInfoMapper.CountUploadSelect(uploadInfo);
	}
	@Override
	public Integer insertUpload(UploadInfo uploadInfo) {
		// TODO Auto-generated method stub
		return uploadInfoMapper.insertUpload(uploadInfo);
	}
}
