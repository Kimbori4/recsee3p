package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.DelRecfileInfoDao;
import com.furence.recsee.admin.model.DelRecfileInfo;
import com.furence.recsee.admin.dao.AllowableRangeInfoDao;
import com.furence.recsee.admin.dao.DelRecfileInfoDao;
import com.furence.recsee.admin.service.DelRecfileInfoService;
import com.furence.recsee.main.model.SearchListInfo;

@Service("DelRecfileInfoService")
public class DelRecfileInfoServiceImpl implements DelRecfileInfoService {
	
	@Autowired
	DelRecfileInfoDao delRecfileInfoMapper;
	
	@Override
	public List<DelRecfileInfo> selectDelRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.selectDelRecfileInfo();
	}

	@Override
	public Integer insertDelRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.insertDelRecfileInfo(delRecfileInfo);
	}

	@Override
	public Integer updateDelRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.updateDelRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public Integer deleteDelRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.deleteDelRecfileInfo(delRecfileInfo);
	}
	
	
	@Override
	public Integer updateRsRecfileBackCheck(SearchListInfo searchListInfo) {
		return delRecfileInfoMapper.updateRsRecfileBackCheck(searchListInfo);
	}
	
	@Override
	public Integer updateRsRecfileListenUrlUpdate(SearchListInfo searchListInfo) {
		return delRecfileInfoMapper.updateRsRecfileListenUrlUpdate(searchListInfo);
	}
	
	@Override
	public Integer updatecRsBackFileFlag(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.updatecRsBackFileFlag(delRecfileInfo);
	}

	@Override
	public List<DelRecfileInfo> selectBackRecfileInfo() {
		return delRecfileInfoMapper.selectBackRecfileInfo();
	}
	

	@Override
	public List<DelRecfileInfo> selectNowBackRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.selectNowBackRecfileInfo(delRecfileInfo);
	}
	

	@Override
	public List<DelRecfileInfo> selectNowBackRecfilePathCheck(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.selectNowBackRecfilePathCheck(delRecfileInfo);
	}

	@Override
	public Integer insertBackRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.insertBackRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public Integer updateBackRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.updateBackRecfileInfo(delRecfileInfo);
	}
	
	@Override
	public Integer deleteBackRecfileInfo(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.deleteBackRecfileInfo(delRecfileInfo);
	}
	@Override
	public Integer countRecfileFileBackup(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.countRecfileFileBackup(delRecfileInfo);
	}
	
	@Override
	public List<SearchListInfo> selectRecfileFileBackup(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.selectRecfileFileBackup(delRecfileInfo);
	}
	
	@Override
	public Integer updateRsRecfileMove(SearchListInfo searchListInfo) {
		return delRecfileInfoMapper.updateRsRecfileMove(searchListInfo);
	}
	
	@Override
	public Integer FileSizeRecfileFileBackup(DelRecfileInfo delRecfileInfo) {
		return delRecfileInfoMapper.FileSizeRecfileFileBackup(delRecfileInfo);
	}
}
	