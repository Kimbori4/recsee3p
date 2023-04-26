package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.AllowableRangeInfo;

public interface AllowableRangeInfoService {

	List<AllowableRangeInfo> selectAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	AllowableRangeInfo checkAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	Integer deleteAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	Integer insertAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
}
