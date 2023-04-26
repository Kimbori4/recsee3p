package com.furence.recsee.monitoring.service;

import java.util.List;

import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.monitoring.model.AlertList;
import com.furence.recsee.monitoring.model.BluePrintInfo;
import com.furence.recsee.monitoring.model.ControlTargetVO;
import com.furence.recsee.monitoring.model.DashboardInfo;
import com.furence.recsee.monitoring.model.ItemVo;
import com.furence.recsee.monitoring.model.MonitoringInfo;
import com.furence.recsee.monitoring.model.OfficeMonitoringRoomVO;
import com.furence.recsee.monitoring.model.OfficeMonitoringVO;
import com.furence.recsee.monitoring.model.RealTimeSettingInfo;
import com.furence.recsee.monitoring.model.SystemRealtimeInfo;
import com.furence.recsee.monitoring.model.UiInfoVO;

public interface MonitoringInfoService {
	List<MonitoringInfo> selectMonitoringInfo(MonitoringInfo monitoringInfo);
	Integer insertMonitoringInfo(MonitoringInfo monitoringInfo);
	Integer deleteMonitoringInfo(MonitoringInfo monitoringInfo);
	List<DashboardInfo> channelInDashboard(DashboardInfo dashboardInfo);
	List<DashboardInfo> channelInfoNum(DashboardInfo dashboardInfo);
	List<DashboardInfo> diffDateList(DashboardInfo dashboardInfo);
	List<ControlTargetVO> selectTargetList();
	int updateTarget(ControlTargetVO controlTargetVO);
	int insertTarget(ControlTargetVO controlTargetVO);
	int deleteTarget(ControlTargetVO controlTargetVO);
	int updateOrder(ControlTargetVO controlTargetVO);
	List<AlertList> selectAlertList(AlertList alertList);
	List<ItemVo> selectTargetItemList();
	List<ControlTargetVO> selectUsingTargetItemList();
	List<ControlTargetVO> selectTargetUIList();
	Integer countItemList(ControlTargetVO setTargetInfo);
	List<ItemVo> targetItemCombo(ControlTargetVO controlTargetVO);
	Integer deleteItemList(ControlTargetVO setTargetInfo);
	List<OfficeMonitoringRoomVO> officeMonitoringSelect(OfficeMonitoringRoomVO officeMonitoringVo);
	List<OfficeMonitoringVO> officeMonitoringAgentSelect(OfficeMonitoringVO officeMonitoringVo);
	int addUseItem(ItemVo itemVO);
	int updateItem(ItemVo itemVO);
	int addItemList(ItemVo setItemInfo);
	int updateItemList(ItemVo setItemInfo);
	int deleteItemAttrList(ItemVo setItemInfo);
	void deleteItemAttrList2(ItemVo setItemInfo);
	int insertTargetOrder(ControlTargetVO setTargetInfo);
	int updateTargetOrderbyDelete(ControlTargetVO setTargetInfo);
	int deleteTargetUi(ControlTargetVO setTargetInfo);
	List<UiInfoVO> selectMaplist();
	List<UiInfoVO> selectIconList();
	List<UiInfoVO> selectIconListCombo();
	int updateTargetUi(ControlTargetVO controlTargetVO);
	int updateUisetting(ControlTargetVO controlTargetVO);
	int updateUseMap(UiInfoVO uiInfoVO);
	int updateMapName(UiInfoVO uiInfoVO);
	int updateUseMapDefault();
	int updateTargetPosition(ControlTargetVO controlTargetVO);
	List<ItemVo> selecMonitoringItemOfTarget(ItemVo itemVO);
	List<ItemVo> selectLimit();
	ControlTargetVO selectTargetNameByIp(ControlTargetVO controlTargetVO);
	int insertAlertList(AlertList alertList);
	int selectAlertListByCompare(AlertList alertList);
	int updateAlertList(AlertList alertList);
	UiInfoVO selectUseMap();
	List<ControlTargetVO> selectMonitoringUseingItemList();
	int updateAlertListBySolve(AlertList alertList);
	Integer deleteAlert(AlertList alertList);
	String selectAlertYn(AlertList alertList);
	Integer deleteUseItem(ControlTargetVO setTargetInfo);
	Integer officeMoitoringInsert(OfficeMonitoringVO officeMonitoringVo);
	Integer officeMoitoringRoomInsert(OfficeMonitoringRoomVO officeMonitoringRoomVo);
	List<RealTimeSettingInfo> realTimeSettingSelect(RealTimeSettingInfo realTimeSettingInfo);
	Integer realTimeSettingInsert(RealTimeSettingInfo realTimeSettingInfo);
	
	
	//	도면 모니터링
	int createBluePrint(BluePrintInfo bluePrintInfo);
	List<BluePrintInfo> selectBluePrint(BluePrintInfo bluePrintInfo);
	int insertBluePrintPaint(BluePrintInfo bluePrintInfo);
	int selectLastSeq();
	List<SearchListInfo> selectRecCount(SearchListInfo searchListInfo);
	Integer insertBluePrintShare(BluePrintInfo bluePrintInfo);
	
	// 시스템 모니터링
	List<SystemRealtimeInfo> selectSystemRealtimeInfo(SystemRealtimeInfo sysRealtimeInfo);
}
