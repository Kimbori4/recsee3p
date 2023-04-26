package com.furence.recsee.monitoring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.monitoring.dao.MonitoringInfoDao;
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
import com.furence.recsee.monitoring.service.MonitoringInfoService;

@Service("monitoringInfoService")
public class MonitoringInfoServiceImpl implements MonitoringInfoService {
	@Autowired
	MonitoringInfoDao monitoringInfoMapper;

	@Override
	public List<MonitoringInfo> selectMonitoringInfo(MonitoringInfo monitoringInfo) {
		return monitoringInfoMapper.selectMonitoringInfo(monitoringInfo);
	}
	@Override
	public Integer insertMonitoringInfo(MonitoringInfo monitoringInfo) {
		return monitoringInfoMapper.insertMonitoringInfo(monitoringInfo);
	}
	@Override
	public Integer deleteMonitoringInfo(MonitoringInfo monitoringInfo) {
		return monitoringInfoMapper.deleteMonitoringInfo(monitoringInfo);
	}
	@Override
	public List<DashboardInfo> channelInDashboard(DashboardInfo dashboardInfo){
		return monitoringInfoMapper.channelInDashboard(dashboardInfo);
	}
	@Override
	public 	List<DashboardInfo> channelInfoNum(DashboardInfo dashboardInfo){
		return monitoringInfoMapper.channelInfoNum(dashboardInfo);
	}
	@Override
	public List<DashboardInfo> diffDateList(DashboardInfo dashboardInfo){
		return monitoringInfoMapper.diffDateList(dashboardInfo);
	}
	@Override
	public List<ControlTargetVO> selectTargetList() {
		return monitoringInfoMapper.selectTargetList();
	}
	@Override
	public int updateTarget(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.updateTarget(controlTargetVO);
	}
	@Override
	public int insertTarget(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.insertTarget(controlTargetVO);
	}
	@Override
	public int deleteTarget(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.deleteTarget(controlTargetVO);
	}
	
	@Override
	public int updateOrder(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.updateOrder(controlTargetVO);
	}
	@Override
	public List<AlertList> selectAlertList(AlertList alertList) {
		return monitoringInfoMapper.selectAlertList(alertList);
	}
	@Override
	public List<ItemVo> selectTargetItemList() {
		return monitoringInfoMapper.selectTargetItemList();
	}
	@Override
	public List<ControlTargetVO> selectUsingTargetItemList() {
		return monitoringInfoMapper.selectUsingTargetItemList();
	}
	@Override
	public List<ControlTargetVO> selectTargetUIList() {
		return monitoringInfoMapper.selectTargetUIList();
	}
	@Override
	public Integer countItemList(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.countItemList(controlTargetVO);
	}
	@Override
	public List<ItemVo> targetItemCombo(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.targetItemCombo(controlTargetVO);
	}
	@Override
	public Integer deleteItemList(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.deleteItemList(controlTargetVO);
	}
	@Override
	public List<OfficeMonitoringRoomVO> officeMonitoringSelect(OfficeMonitoringRoomVO officeMonitoringVO) {
		return monitoringInfoMapper.officeMonitoringSelect(officeMonitoringVO);
	}
	@Override
	public int addUseItem(ItemVo itemVO) {
		return monitoringInfoMapper.addUseItem(itemVO);
	}
	@Override
	public int updateItem(ItemVo itemVO) {
		return monitoringInfoMapper.updateItem(itemVO);
	}
	@Override
	public int addItemList(ItemVo setItemInfo) {
		return monitoringInfoMapper.addItemList(setItemInfo);
	}
	@Override
	public int updateItemList(ItemVo setItemInfo) {
		return monitoringInfoMapper.updateItemList(setItemInfo);
	}
	@Override
	public int deleteItemAttrList(ItemVo setItemInfo) {
		return monitoringInfoMapper.deleteItemAttrList(setItemInfo);
	}
	@Override
	public void deleteItemAttrList2(ItemVo setItemInfo) {
		monitoringInfoMapper.deleteItemAttrList2(setItemInfo);
	}
	@Override
	public int insertTargetOrder(ControlTargetVO setTargetInfo) {
		return monitoringInfoMapper.insertTargetOrder(setTargetInfo);
	}
	@Override
	public int updateTargetOrderbyDelete(ControlTargetVO setTargetInfo) {
		return monitoringInfoMapper.updateTargetOrderbyDelete(setTargetInfo);
	}
	@Override
	public int deleteTargetUi(ControlTargetVO setTargetInfo) {
		return monitoringInfoMapper.deleteTargetUi(setTargetInfo);
	}
	@Override
	public List<UiInfoVO> selectMaplist() {
		return monitoringInfoMapper.selectMaplist();
	}
	@Override
	public List<UiInfoVO> selectIconList() {
		return monitoringInfoMapper.selectIconList();
	}
	@Override
	public List<UiInfoVO> selectIconListCombo() {
		return monitoringInfoMapper.selectIconListCombo();
	}
	@Override
	public int updateTargetUi(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.updateTargetUi(controlTargetVO);
	}
	@Override
	public int updateUisetting(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.updateUisetting(controlTargetVO);
	}
	@Override
	public int updateMapName(UiInfoVO uiInfoVO) {
		return monitoringInfoMapper.updateMapName(uiInfoVO);
	}
	@Override
	public int updateUseMap(UiInfoVO uiInfoVO) {
		return monitoringInfoMapper.updateUseMap(uiInfoVO);
	}
	@Override
	public int updateUseMapDefault() {
		return monitoringInfoMapper.updateUseMapDefault();
	}
	@Override
	public int updateTargetPosition(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.updateTargetPosition(controlTargetVO);
	}
	@Override
	public List<ItemVo> selecMonitoringItemOfTarget(ItemVo itemVO) {
		return monitoringInfoMapper.selecMonitoringItemOfTarget(itemVO);
	}
	@Override
	public List<ItemVo> selectLimit() {
		return monitoringInfoMapper.selectLimit();
	}
	@Override
	public ControlTargetVO selectTargetNameByIp(ControlTargetVO controlTargetVO) {
		return monitoringInfoMapper.selectTargetNameByIp(controlTargetVO);
	}
	@Override
	public int insertAlertList(AlertList alertList) {
		return monitoringInfoMapper.insertAlertList(alertList);
	}
	@Override
	public int selectAlertListByCompare(AlertList alertList) {
		return monitoringInfoMapper.selectAlertListByCompare(alertList);
	}
	@Override
	public int updateAlertList(AlertList alertList) {
		return monitoringInfoMapper.updateAlertList(alertList);
	}
	@Override
	public UiInfoVO selectUseMap() {
		return monitoringInfoMapper.selectUseMap();
	}
	@Override
	public List<ControlTargetVO> selectMonitoringUseingItemList() {
		return monitoringInfoMapper.selectMonitoringUseingItemList();
	}
	@Override
	public int updateAlertListBySolve(AlertList alertList) {
		return monitoringInfoMapper.updateAlertListBySolve(alertList);
	}
	@Override
	public Integer deleteAlert(AlertList alertList) {
		return monitoringInfoMapper.deleteAlert(alertList);
	}
	@Override
	public String selectAlertYn(AlertList alertList) {
		return monitoringInfoMapper.selectAlertYn(alertList);
	}
	@Override
	public Integer deleteUseItem(ControlTargetVO setTargetInfo) {
		return monitoringInfoMapper.deleteUseItem(setTargetInfo);
	}
	@Override
	public List<OfficeMonitoringVO> officeMonitoringAgentSelect(OfficeMonitoringVO officeMonitoringVo) {
		return monitoringInfoMapper.officeMonitoringAgentSelect(officeMonitoringVo);
	}
	@Override
	public Integer officeMoitoringInsert(OfficeMonitoringVO officeMonitoringVo) {
		return monitoringInfoMapper.officeMoitoringInsert(officeMonitoringVo);
	}
	@Override
	public Integer officeMoitoringRoomInsert(OfficeMonitoringRoomVO officeMonitoringRoomVo) {
		return monitoringInfoMapper.officeMoitoringRoomInsert(officeMonitoringRoomVo);
	}
	@Override
	public List<RealTimeSettingInfo> realTimeSettingSelect(RealTimeSettingInfo realTimeSettingInfo) {
		return monitoringInfoMapper.realTimeSettingSelect(realTimeSettingInfo);
	}
	@Override
	public Integer realTimeSettingInsert(RealTimeSettingInfo realTimeSettingInfo) {
		return monitoringInfoMapper.realTimeSettingInsert(realTimeSettingInfo);
	}
	@Override
	public int createBluePrint(BluePrintInfo bluePrintInfo) {
		return monitoringInfoMapper.createBluePrint(bluePrintInfo);
	}
	@Override
	public List<BluePrintInfo> selectBluePrint(BluePrintInfo bluePrintInfo) {
		return monitoringInfoMapper.selectBluePrint(bluePrintInfo);
	}
	@Override
	public int insertBluePrintPaint(BluePrintInfo bluePrintInfo) {
		return monitoringInfoMapper.insertBluePrintPaint(bluePrintInfo);
	}
	@Override
	public int selectLastSeq() {
		return monitoringInfoMapper.selectLastSeq();
	}
	@Override
	public List<SearchListInfo> selectRecCount(SearchListInfo searchListInfo) {
		return monitoringInfoMapper.selectRecCount(searchListInfo);
	}
	
	@Override
	public Integer insertBluePrintShare(BluePrintInfo bluePrintInfo) {
		return monitoringInfoMapper.insertBluePrintShare(bluePrintInfo);
	}
	@Override
	public List<SystemRealtimeInfo> selectSystemRealtimeInfo(SystemRealtimeInfo sysRealtimeInfo) {
		return monitoringInfoMapper.selectSystemRealtimeInfo(sysRealtimeInfo);
	}
}
