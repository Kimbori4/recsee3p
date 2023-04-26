package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.CustomizeInfoDao;
import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.model.CustomizeItemInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;
import com.furence.recsee.admin.service.CustomizeInfoService;


@Service("customizeInfoService")
public class CustomizeInfoServiceImpl implements CustomizeInfoService {
	@Autowired
	CustomizeInfoDao customizeInfoMapper;

	@Override
	public List<CustomizeListInfo> selectCustomizeListInfo(CustomizeListInfo customizeListInfo) {

		return customizeInfoMapper.selectCustomizeListInfo(customizeListInfo);
	}
	@Override
	public Integer insertCustomizeListInfo(CustomizeListInfo customizeListInfo) {

		return customizeInfoMapper.insertCustomizeListInfo(customizeListInfo);
	}

	@Override
	public Integer updateCustomizeListInfo(CustomizeListInfo customizeListInfo) {

		return customizeInfoMapper.updateCustomizeListInfo(customizeListInfo);
	}

	@Override
	public Integer deleteCustomizeListInfo(CustomizeListInfo customizeListInfo) {

		return customizeInfoMapper.deleteCustomizeListInfo(customizeListInfo);
	}

	@Override
	public List<CustomizeItemInfo> selectCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {

		return customizeInfoMapper.selectCustomizeItemInfo(customizeItemInfo);
	}
	@Override
	public Integer insertCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {

		return customizeInfoMapper.insertCustomizeItemInfo(customizeItemInfo);
	}

	@Override
	public Integer updateCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {

		return customizeInfoMapper.updateCustomizeItemInfo(customizeItemInfo);
	}

	@Override
	public Integer deleteCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {

		return customizeInfoMapper.deleteCustomizeItemInfo(customizeItemInfo);
	}


	@Override
	public List<CustomizeCopyListInfo> selectCustomizeCopyListInfo(CustomizeCopyListInfo customizeCopyListInfo) {

		return customizeInfoMapper.selectCustomizeCopyListInfo(customizeCopyListInfo);
	}
	@Override
	public Integer insertCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {

		return customizeInfoMapper.insertCustomizeCopyListInfo(customizeListInfo);
	}

	@Override
	public Integer updateCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {

		return customizeInfoMapper.updateCustomizeCopyListInfo(customizeListInfo);
	}

	@Override
	public Integer deleteCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {

		return customizeInfoMapper.deleteCustomizeCopyListInfo(customizeListInfo);
	}

}
