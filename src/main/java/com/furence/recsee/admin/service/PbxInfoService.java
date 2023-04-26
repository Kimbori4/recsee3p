package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.PbxInfo;

public interface PbxInfoService {
	List<PbxInfo> selectPbxInfo(PbxInfo pbxInfo);
	Integer insertPbxInfo(PbxInfo pbxInfo);
	Integer updatePbxInfo(PbxInfo pbxInfo);
	Integer deletePbxInfo(PbxInfo pbxInfo);
}
