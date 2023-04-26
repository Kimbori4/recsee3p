package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.PbxInfoDao;
import com.furence.recsee.admin.model.PbxInfo;
import com.furence.recsee.admin.service.PbxInfoService;

@Service("pbxInfoService")
public class PbxInfoServiceImpl implements PbxInfoService {
	@Autowired
	PbxInfoDao pbxInfoMapper;

	@Override
	public List<PbxInfo> selectPbxInfo(PbxInfo pbxInfo) {
		return pbxInfoMapper.selectPbxInfo(pbxInfo);
	}

	@Override
	public Integer insertPbxInfo(PbxInfo pbxInfo) {
		return pbxInfoMapper.insertPbxInfo(pbxInfo);
	}

	@Override
	public Integer updatePbxInfo(PbxInfo pbxInfo) {
		return pbxInfoMapper.updatePbxInfo(pbxInfo);
	}

	@Override
	public Integer deletePbxInfo(PbxInfo pbxInfo) {
		return pbxInfoMapper.deletePbxInfo(pbxInfo);
	}
}
