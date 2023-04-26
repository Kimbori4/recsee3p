package com.furence.recsee.monitoring.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
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

@Repository("monitoringInfoDao")
public class MonitoringInfoDaoImpl implements MonitoringInfoDao {

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SynchronizationService synchronizationService;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<MonitoringInfo> selectMonitoringInfo(MonitoringInfo monitoringInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		return monitoringInfoMapper.selectMonitoringInfo(monitoringInfo);
	}

	@Override
	public Integer insertMonitoringInfo(MonitoringInfo monitoringInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMonitoringInfo").getBoundSql(monitoringInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.insertMonitoringInfo(monitoringInfo);
	}

	@Override
	public Integer deleteMonitoringInfo(MonitoringInfo monitoringInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMonitoringInfo").getBoundSql(monitoringInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteMonitoringInfo(monitoringInfo);
	}

	@Override
	public List<DashboardInfo> channelInDashboard(DashboardInfo dashboardInfo){
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		return monitoringInfoMapper.channelInDashboard(dashboardInfo);
	}

	@Override
	public 	List<DashboardInfo> channelInfoNum(DashboardInfo dashboardInfo){
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		return monitoringInfoMapper.channelInfoNum(dashboardInfo);
	}

	@Override
	public List<DashboardInfo> diffDateList(DashboardInfo dashboardInfo){
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		return monitoringInfoMapper.diffDateList(dashboardInfo);
	}

	@Override
	public List<ControlTargetVO> selectTargetList() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		return monitoringInfoMapper.selectTargetList();
	}

	@Override
	public int updateTarget(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTarget").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateTarget(controlTargetVO);
	}

	@Override
	public int insertTarget(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertTarget").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.insertTarget(controlTargetVO);
	}

	@Override
	public int deleteTarget(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteTarget").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteTarget(controlTargetVO);
	}
	@Override
	public List<OfficeMonitoringRoomVO> officeMonitoringSelect(OfficeMonitoringRoomVO officeMonitoringVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.officeMonitoringSelect(officeMonitoringVO);
	}

	@Override
	public int updateOrder(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateOrder").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateOrder(controlTargetVO);
	}

	@Override
	public List<AlertList> selectAlertList(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.selectAlertList(alertList);
	}

	@Override
	public List<ItemVo> selectTargetItemList() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.selectTargetItemList();
	}

	@Override
	public List<ControlTargetVO> selectUsingTargetItemList() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.selectUsingTargetItemList();
	}

	@Override
	public List<ControlTargetVO> selectTargetUIList() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.selectTargetUIList();
	}

	@Override
	public Integer countItemList(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.countItemList(controlTargetVO);
	}

	@Override
	public List<ItemVo> targetItemCombo(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.targetItemCombo(controlTargetVO);
	}

	@Override
	public Integer deleteItemList(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteItemList").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteItemList(controlTargetVO);
	}

	@Override
	public int addUseItem(ItemVo itemVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("addUseItem").getBoundSql(itemVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.addUseItem(itemVO);
	}

	@Override
	public int updateItem(ItemVo itemVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateItem").getBoundSql(itemVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateItem(itemVO);
	}

	@Override
	public int addItemList(ItemVo setItemInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("addItemList").getBoundSql(setItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.addItemList(setItemInfo);
	}

	@Override
	public int updateItemList(ItemVo setItemInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateItemList").getBoundSql(setItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateItemList(setItemInfo);
	}

	@Override
	public int deleteItemAttrList(ItemVo setItemInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteItemAttrList").getBoundSql(setItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteItemAttrList(setItemInfo);
	}

	@Override
	public void deleteItemAttrList2(ItemVo setItemInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteItemAttrList2").getBoundSql(setItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		monitoringInfoMapper.deleteItemAttrList2(setItemInfo);
	}

	@Override
	public int insertTargetOrder(ControlTargetVO setTargetInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertTargetOrder").getBoundSql(setTargetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.insertTargetOrder(setTargetInfo);
	}

	@Override
	public int updateTargetOrderbyDelete(ControlTargetVO setTargetInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTargetOrderbyDelete").getBoundSql(setTargetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateTargetOrderbyDelete(setTargetInfo);
	}

	@Override
	public int deleteTargetUi(ControlTargetVO setTargetInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteTargetUi").getBoundSql(setTargetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteTargetUi(setTargetInfo);
	}

	@Override
	public List<UiInfoVO> selectMaplist() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectMaplist();
	}

	@Override
	public List<UiInfoVO> selectIconList() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectIconList();
	}

	@Override
	public List<UiInfoVO> selectIconListCombo() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectIconListCombo();
	}

	@Override
	public int updateTargetUi(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTargetUi").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateTargetUi(controlTargetVO);
	}

	@Override
	public int updateUisetting(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUisetting").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateUisetting(controlTargetVO);
	}

	@Override
	public int updateMapName(UiInfoVO uiInfoVO){
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMapName").getBoundSql(uiInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateMapName(uiInfoVO);
	}

	@Override
	public int updateUseMap(UiInfoVO uiInfoVO){
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUseMap").getBoundSql(uiInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateUseMap(uiInfoVO);
	}

	@Override
	public int updateUseMapDefault() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUseMapDefault").getBoundSql(new Object());
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateUseMapDefault();
	}

	@Override
	public int updateTargetPosition(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTargetPosition").getBoundSql(controlTargetVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateTargetPosition(controlTargetVO);
	}

	@Override
	public List<ItemVo> selecMonitoringItemOfTarget(ItemVo itemVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selecMonitoringItemOfTarget(itemVO);
	}

	@Override
	public List<ItemVo> selectLimit() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectLimit();
	}

	@Override
	public ControlTargetVO selectTargetNameByIp(ControlTargetVO controlTargetVO) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectTargetNameByIp(controlTargetVO);
	}

	@Override
	public int insertAlertList(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAlertList").getBoundSql(alertList);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.insertAlertList(alertList);
	}

	@Override
	public int selectAlertListByCompare(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectAlertListByCompare(alertList);
	}

	@Override
	public int updateAlertList(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateAlertList").getBoundSql(alertList);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateAlertList(alertList);
	}

	@Override
	public UiInfoVO selectUseMap() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectUseMap();
	}

	@Override
	public List<ControlTargetVO> selectMonitoringUseingItemList() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectMonitoringUseingItemList();
	}

	@Override
	public int updateAlertListBySolve(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateAlertListBySolve").getBoundSql(alertList);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.updateAlertListBySolve(alertList);
	}

	@Override
	public Integer deleteAlert(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteAlert").getBoundSql(alertList);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteAlert(alertList);
	}

	@Override
	public String selectAlertYn(AlertList alertList) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectAlertYn(alertList);
	}

	@Override
	public Integer deleteUseItem(ControlTargetVO setTargetInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteUseItem").getBoundSql(setTargetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.deleteUseItem(setTargetInfo);
	}
	@Override
	public List<OfficeMonitoringVO> officeMonitoringAgentSelect(OfficeMonitoringVO officeMonitoringVo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		return monitoringInfoMapper.officeMonitoringAgentSelect(officeMonitoringVo);
	}

	@Override
	public Integer officeMoitoringInsert(OfficeMonitoringVO officeMonitoringVo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("officeMoitoringInsert").getBoundSql(officeMonitoringVo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.officeMoitoringInsert(officeMonitoringVo);
	}

	@Override
	public Integer officeMoitoringRoomInsert(OfficeMonitoringRoomVO officeMonitoringRoomVo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("officeMoitoringRoomInsert").getBoundSql(officeMonitoringRoomVo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.officeMoitoringRoomInsert(officeMonitoringRoomVo);
	}

	@Override
	public List<RealTimeSettingInfo> realTimeSettingSelect(RealTimeSettingInfo realTimeSettingInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.realTimeSettingSelect(realTimeSettingInfo);
	}

	@Override
	public Integer realTimeSettingInsert(RealTimeSettingInfo realTimeSettingInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.realTimeSettingInsert(realTimeSettingInfo);
	}

	@Override
	public int createBluePrint(BluePrintInfo bluePrintInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.createBluePrint(bluePrintInfo);
	}

	@Override
	public List<BluePrintInfo> selectBluePrint(BluePrintInfo bluePrintInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectBluePrint(bluePrintInfo);
	}

	@Override
	public int insertBluePrintPaint(BluePrintInfo bluePrintInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBluePrintPaint").getBoundSql(bluePrintInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.insertBluePrintPaint(bluePrintInfo);
	}

	@Override
	public int selectLastSeq() {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectLastSeq();
	}

	@Override
	public List<SearchListInfo> selectRecCount(SearchListInfo searchListInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectRecCount(searchListInfo);
	}

	@Override
	public Integer insertBluePrintShare(BluePrintInfo bluePrintInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBluePrintShare").getBoundSql(bluePrintInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return monitoringInfoMapper.insertBluePrintShare(bluePrintInfo);
	}

	@Override
	public List<SystemRealtimeInfo> selectSystemRealtimeInfo(SystemRealtimeInfo sysRealtimeInfo) {
		MonitoringInfoDao monitoringInfoMapper = sqlSession.getMapper(MonitoringInfoDao.class);
		return monitoringInfoMapper.selectSystemRealtimeInfo(sysRealtimeInfo);
	}
}
