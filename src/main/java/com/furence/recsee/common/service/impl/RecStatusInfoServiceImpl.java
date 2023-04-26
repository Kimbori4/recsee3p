
package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.RecStatusInfoDao;
import com.furence.recsee.common.service.RecStatusInfoService;
import com.furence.recsee.monitoring.model.DashboardInfo;


@Service("recStatusInfoService")
public class RecStatusInfoServiceImpl implements RecStatusInfoService {

	@Autowired
	RecStatusInfoDao recStatusInfoMapper;

	@Override
	public List<DashboardInfo> selectFirstRecfileInfo(DashboardInfo dashboardInfo){
		return recStatusInfoMapper.selectFirstRecfileInfo(dashboardInfo);
	}
	@Override
	public Integer insertRecStatus(DashboardInfo dashboardInfo){
		return recStatusInfoMapper.insertRecStatus(dashboardInfo);
	}
	@Override
	public Integer updateRecStatus(DashboardInfo dashboardInfo){
		return recStatusInfoMapper.updateRecStatus(dashboardInfo);
	}
	@Override
	public List<DashboardInfo> selectRecfileInfo(DashboardInfo dashboardInfo){
		return recStatusInfoMapper.selectRecfileInfo(dashboardInfo);
	}
	@Override
	public Integer updateEtcConfigTime(String dateTime){
		return recStatusInfoMapper.updateEtcConfigTime(dateTime);
	}
	@Override
	public Integer selectRecStatus(){
		return recStatusInfoMapper.selectRecStatus();
	}
	@Override
	public String selectEtcConfigTime(){
		return recStatusInfoMapper.selectEtcConfigTime();
	}
	@Override
	public DashboardInfo selectCompRecData(DashboardInfo dashboardInfo){
		return recStatusInfoMapper.selectCompRecData(dashboardInfo);
	}
	@Override
	public Integer insertSttList(String sysCode) {
		return recStatusInfoMapper.insertSttList(sysCode);
	}
}