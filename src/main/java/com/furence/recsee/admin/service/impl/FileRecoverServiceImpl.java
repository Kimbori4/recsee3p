package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.FileRecoverDao;
import com.furence.recsee.admin.model.FileRecoverInfo;
import com.furence.recsee.admin.service.FileRecoverService;

@Service("fileRecoverService")
public class FileRecoverServiceImpl implements FileRecoverService {
	
	@Autowired
	FileRecoverDao fileRecoverMapper;

	@Override
	public List<FileRecoverInfo> selectFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.selectFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer insertFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.insertFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer updateFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.updateFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer deleteFileRecoverInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.deleteFileRecoverInfo(fileRecoverInfo);
	}

	@Override
	public Integer selectFileRecoverInfoTotal(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.selectFileRecoverInfoTotal(fileRecoverInfo);
	}

	@Override
	public List<FileRecoverInfo> selectGenesysInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.selectGenesysInfo(fileRecoverInfo);
	}

	@Override
	public Integer selectGenesysInfoTotal(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.selectGenesysInfoTotal(fileRecoverInfo);
	}

	@Override
	public Integer updateGenesysInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.updateGenesysInfo(fileRecoverInfo);
	}

	@Override
	public Integer deleteGenesysInfo(FileRecoverInfo fileRecoverInfo) {
		return fileRecoverMapper.deleteGenesysInfo(fileRecoverInfo);
	}
}
