package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.model.CustomizeItemInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;

public interface CustomizeInfoService {
	List<CustomizeListInfo> selectCustomizeListInfo(CustomizeListInfo customizeListInfo);
	Integer insertCustomizeListInfo(CustomizeListInfo customizeListInfo);
	Integer updateCustomizeListInfo(CustomizeListInfo customizeListInfo);
	Integer deleteCustomizeListInfo(CustomizeListInfo customizeListInfo);

	List<CustomizeItemInfo> selectCustomizeItemInfo(CustomizeItemInfo customizeItemInfo);
	Integer insertCustomizeItemInfo(CustomizeItemInfo customizeItemInfo);
	Integer updateCustomizeItemInfo(CustomizeItemInfo customizeItemInfo);
	Integer deleteCustomizeItemInfo(CustomizeItemInfo customizeItemInfo);

	List<CustomizeCopyListInfo> selectCustomizeCopyListInfo(CustomizeCopyListInfo customizeCopyListInfo);
	Integer insertCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo);
	Integer updateCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo);
	Integer deleteCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo);
}
