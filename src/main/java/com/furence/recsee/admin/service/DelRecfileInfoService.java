package com.furence.recsee.admin.service;

import java.util.List;
import com.furence.recsee.admin.model.DelRecfileInfo;
import com.furence.recsee.main.model.SearchListInfo;

public interface DelRecfileInfoService {
	List<DelRecfileInfo> selectDelRecfileInfo(DelRecfileInfo delRecfileInfo);
	Integer insertDelRecfileInfo(DelRecfileInfo delRecfileInfo);
	Integer updateDelRecfileInfo(DelRecfileInfo delRecfileInfo);	
	Integer deleteDelRecfileInfo(DelRecfileInfo delRecfileInfo);
	Integer updatecRsBackFileFlag(DelRecfileInfo delRecfileInfo);
	
	Integer updateRsRecfileBackCheck(SearchListInfo searchListInfo);	
	Integer updateRsRecfileListenUrlUpdate(SearchListInfo searchListInfo);	
	Integer updateRsRecfileMove(SearchListInfo searchListInfo);
	
	
	List<DelRecfileInfo> selectBackRecfileInfo();
	List<DelRecfileInfo> selectNowBackRecfileInfo(DelRecfileInfo delRecfileInfo);
	List<DelRecfileInfo> selectNowBackRecfilePathCheck(DelRecfileInfo delRecfileInfo);	
	List<SearchListInfo> selectRecfileFileBackup(DelRecfileInfo delRecfileInfo);		
	Integer insertBackRecfileInfo(DelRecfileInfo delRecfileInfo);
	Integer updateBackRecfileInfo(DelRecfileInfo delRecfileInfo);	
	Integer deleteBackRecfileInfo(DelRecfileInfo delRecfileInfo);
	Integer countRecfileFileBackup(DelRecfileInfo delRecfileInfo);
	Integer FileSizeRecfileFileBackup(DelRecfileInfo delRecfileInfo);
	
	
}
