
package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.monitoring.model.DashboardInfo;


public interface RecStatusInfoService {

	List<DashboardInfo> selectFirstRecfileInfo(DashboardInfo dashboardInfo);
	Integer insertRecStatus(DashboardInfo dashboardInfo);
	Integer updateRecStatus(DashboardInfo dashboardInfo);
	List<DashboardInfo> selectRecfileInfo(DashboardInfo dashboardInfo);
	Integer updateEtcConfigTime(String dateTime);
	Integer selectRecStatus();
	String selectEtcConfigTime();
	DashboardInfo selectCompRecData(DashboardInfo dashboardInfo);
	Integer insertSttList(String sysCode);
}
