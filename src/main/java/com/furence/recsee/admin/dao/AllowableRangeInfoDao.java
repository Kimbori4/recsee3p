package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.AllowableRangeInfo;

public interface AllowableRangeInfoDao {

	List<AllowableRangeInfo> selectAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	AllowableRangeInfo checkAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	Integer deleteAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	Integer insertAllowableRangeInfo(AllowableRangeInfo allowableRangeInfo);
	
}
