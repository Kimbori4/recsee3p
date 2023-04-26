
package com.furence.recsee.common.dao;

import java.util.List;
import com.furence.recsee.monitoring.model.DashboardInfo;


public interface RecStatusInfoDao {

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
