package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.FileRecoverInfo;

public interface FileRecoverDao {
	List<FileRecoverInfo> selectFileRecoverInfo(FileRecoverInfo fileRecoverInfo);
	Integer selectFileRecoverInfoTotal(FileRecoverInfo fileRecoverInfo);
	Integer insertFileRecoverInfo(FileRecoverInfo fileRecoverInfo);
	Integer updateFileRecoverInfo(FileRecoverInfo fileRecoverInfo);
	Integer deleteFileRecoverInfo(FileRecoverInfo fileRecoverInfo);

	List<FileRecoverInfo> selectGenesysInfo(FileRecoverInfo fileRecoverInfo);
	Integer selectGenesysInfoTotal(FileRecoverInfo fileRecoverInfo);
	Integer updateGenesysInfo(FileRecoverInfo fileRecoverInfo);
	Integer deleteGenesysInfo(FileRecoverInfo fileRecoverInfo);
}
