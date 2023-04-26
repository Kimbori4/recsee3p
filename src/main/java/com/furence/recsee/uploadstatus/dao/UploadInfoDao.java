package com.furence.recsee.uploadstatus.dao;

import java.util.List;

import com.furence.recsee.uploadstatus.model.UploadInfo;

public interface UploadInfoDao {
	List<UploadInfo> selectUploadInfo(UploadInfo uploadInfo);
	List<UploadInfo> selectUploadGroupInfo(UploadInfo uploadInfo);
	List<UploadInfo> selectServerCount(UploadInfo uploadInfo);

	Integer CountUploadSelect(UploadInfo uploadInfo);
	Integer insertUpload(UploadInfo uploadInfo);
}
