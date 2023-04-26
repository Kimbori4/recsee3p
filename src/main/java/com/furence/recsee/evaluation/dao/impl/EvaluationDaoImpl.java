package com.furence.recsee.evaluation.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.evaluation.dao.EvaluationDao;
import com.furence.recsee.evaluation.model.SheetInfo;

@Repository("evaluationDao")
public class EvaluationDaoImpl implements EvaluationDao {
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
	public Integer updateItemMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateItemMark").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateItemMark(sheetInfo);
	}

	@Override
	public Integer insertSheetMemoInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSheetMemoInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertSheetMemoInfo(sheetInfo);
	}
	
	@Override
	public Integer updateSheetMemoInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSheetMemoInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSheetMemoInfo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectStatistisList(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectStatistisList(sheetInfo);
	}

	@Override
	public List<SheetInfo> insertSheetInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSheetInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertSheetInfo(sheetInfo);
	}

	@Override
	public Integer updateSubInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSubInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSubInfo(sheetInfo);
	}

	@Override
	public Integer deleteSubInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSubInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSubInfo(sheetInfo);
	}

	@Override
	public Integer deleteSubMemoInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSubMemoInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSubMemoInfo(sheetInfo);
	}
	
	@Override
	public Integer updateSubMemo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSubMemo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSubMemo(sheetInfo);
	}
	
	@Override
	public Integer deleteSubMemo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSubMemo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSubMemo(sheetInfo);
	}
	
	@Override
	public Integer InsertSubMemo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("InsertSubMemo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.InsertSubMemo(sheetInfo);
	}
	
	@Override
	public Integer updateSubMemoInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSubMemoInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSubMemoInfo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectSheetList(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetList(sheetInfo);
	}
	@Override
	public List<Map<String, Object>> selectSheetScore(Map<String, Object> param) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetScore(param);
	}

	@Override
	public List<SheetInfo> itemList(String sheetCode){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.itemList(sheetCode);
	}

	@Override
	public List<SheetInfo> sbgList(String sheetCode){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.sbgList(sheetCode);
	}

	@Override
	public List<SheetInfo> smgList(String sheetCode){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.smgList(sheetCode);
	}

	@Override
	public List<SheetInfo> ssgList(String sheetCode){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.ssgList(sheetCode);
	}

	@Override
	public List<SheetInfo> subItemList(String itemCode){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.subItemList(itemCode);
	}

	@Override
	public Integer updateBgCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateBgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateBgCate(sheetInfo);
	}

	@Override
	public Integer updateMgCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateMgCate(sheetInfo);
	}

	@Override
	public Integer updateSgCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSgCate(sheetInfo);
	}

	@Override
	public Integer insertBgInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertBgInfo(sheetInfo);
	}

	@Override
	public Integer insertMgInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertMgInfo(sheetInfo);
	}

	@Override
	public Integer insertSgInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertSgInfo(sheetInfo);
	}

	@Override
	public Integer insertsubInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertsubInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertsubInfo(sheetInfo);
	}

	@Override
	public String selectBgLastNum(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectBgLastNum(sheetInfo);
	}

	@Override
	public Integer insertBgCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertBgCate(sheetInfo);
	}

	@Override
	public Integer insertMgCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertMgCate(sheetInfo);
	}

	@Override
	public Integer insertSgCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertSgCate(sheetInfo);
	}

	@Override
	public String selectRegBgLastNum(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectRegBgLastNum();
	}

	@Override
	public String selectMgLastNum(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectMgLastNum(sheetInfo);
	}

	@Override
	public String selectRegMgLastNum(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectRegMgLastNum();
	}

	@Override
	public String selectSgLastNum(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSgLastNum(sheetInfo);
	}

	@Override
	public String selectRegSgLastNum(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectRegSgLastNum();
	}

	@Override
	public Integer updateItemCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateItemCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateItemCate(sheetInfo);
	}

	@Override
	public Integer insertItemInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertItemInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertItemInfo(sheetInfo);
	}

	@Override
	public Integer insertItemCate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertItemCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertItemCate(sheetInfo);
	}

	@Override
	public String selectItemLastNum(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectItemLastNum(sheetInfo);
	}

	@Override
	public String selectRegItemLastNum(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectRegItemLastNum();
	}

	@Override
	public 	List<SheetInfo> selectSheetView(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetView(sheetInfo);
	}

	@Override
	public SheetInfo selectRegBCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectRegBCode(sheetInfo);
	}

	@Override
	public SheetInfo selectAllRegCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectAllRegCode(sheetInfo);
	}

	@Override
	public Integer deleteRegBCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("evaluationMapper").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteRegBCode(sheetInfo);
	}

	@Override
	public Integer deleteRegMCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteRegMCode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteRegMCode(sheetInfo);
	}

	@Override
	public Integer deleteRegSCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteRegSCode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteRegSCode(sheetInfo);
	}

	@Override
	public Integer deleteRegICode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteRegICode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteRegICode(sheetInfo);
	}

	@Override
	public Integer deleteSheetInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSheetInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSheetInfo(sheetInfo);
	}

	@Override
	public Integer deleteSheetBgCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSheetBgCode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSheetBgCode(sheetInfo);
	}

	@Override
	public Integer deleteSheetMgCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSheetMgCode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSheetMgCode(sheetInfo);
	}

	@Override
	public Integer deleteSheetSgCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSheetSgCode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSheetSgCode(sheetInfo);
	}

	@Override
	public Integer deleteSheetICode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSheetICode").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSheetICode(sheetInfo);
	}

	@Override
	public String selectBCodeInfo(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectBCodeInfo();
	}

	@Override
	public String selectMCodeInfo(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectMCodeInfo();
	}

	@Override
	public String selectSCodeInfo(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSCodeInfo();
	}

	@Override
	public String selectSubItemLastNum(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSubItemLastNum();
	}

	@Override
	public Integer insertSICate(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSICate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertSICate(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectSubItem(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSubItem(sheetInfo);
	}

	@Override
	public 	Integer updateSubMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSubMark").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSubMark(sheetInfo);
	}

	@Override
	public Integer updateMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMark").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateMark(sheetInfo);
	}

	@Override
	public String selectMarkEle(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectMarkEle(sheetInfo);
	}

	@Override
	public List<SheetInfo> searchSubMarkEle(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.searchSubMarkEle(sheetInfo);
	}

	@Override
	public SheetInfo searchSubMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.searchSubMark(sheetInfo);
	}

	@Override
	public List<SheetInfo> searchMarkEle(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.searchMarkEle(sheetInfo);
	}

	@Override
	public String searchMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.searchMark(sheetInfo);
	}

	@Override
	public Integer deleteSubItem(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.deleteSubItem(sheetInfo);
	}

	@Override
	public String maxSubItemMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.maxSubItemMark(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectBgTotMark(String sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectBgTotMark(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectMgTotMark(String sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectMgTotMark(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSgTotMark(String sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSgTotMark(sheetInfo);
	}
	@Override
	public String selectEleRegCode(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectEleRegCode(sheetInfo);
	}
	@Override
	public Integer updateBMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateBMark").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateBMark(sheetInfo);
	}
	@Override
	public Integer updateMMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMMark").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateMMark(sheetInfo);
	}
	@Override
	public Integer updateSMark(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSMark").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSMark(sheetInfo);
	}
	@Override
	public Integer updateSubItem(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSubItem").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSubItem(sheetInfo);
	}
	@Override
	public Integer insertCampInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertCampInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertCampInfo(sheetInfo);
	}
	@Override
	public Integer deleteCampInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteCampInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteCampInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectCampList(){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectCampList();
	}

	@Override
	public 	SheetInfo selectEvalResult(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectEvalResult(sheetInfo);
	}

	@Override
	public Integer updateEvalResult(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEvalResult").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateEvalResult(sheetInfo);
	}
	@Override
	public Integer insertEvalResult(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertEvalResult").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertEvalResult(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectEvalGrid(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectEvalGrid(sheetInfo);
	}
	@Override
	public SheetInfo selectRecFile(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectRecFile(sheetInfo);
	}
	@Override
	public 	Integer updateEvalYn(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEvalYn").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateEvalYn(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectEvalUseCheck(String sheetCode){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectEvalUseCheck(sheetCode);
	}
	@Override
	public Integer updateCampInfo(SheetInfo campInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("evaluationMapper").getBoundSql(campInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateCampInfo(campInfo);
	}
	//question_manage 대분류 select
	@Override
	public List<SheetInfo> selectBgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectBgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectCampBgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectCampBgInfo(sheetInfo);
	}
	//question_manage 중분류 select
	@Override
	public List<SheetInfo> selectMgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectMgInfo(sheetInfo);
	}
	//question_manage 소분류 select
	@Override
	public List<SheetInfo> selectSgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectCampSgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectCampSgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectCampIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectCampIgInfo(sheetInfo);
	}
	//question_manage 대분류 delete
	@Override
	public Integer deleteBgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteBgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteBgInfo(sheetInfo);
	}
	@Override
	public Integer deleteMgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteMgInfo(sheetInfo);
	}
	@Override
	public Integer deleteSgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteSgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectboxSheet(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectboxSheet(sheetInfo);
	}

	@Override
	public Integer insertSheet(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSheet").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertSheet(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectSheet(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheet(sheetInfo);
	}

	@Override
	public Integer updateBgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateBgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateBgInfo(sheetInfo);
	}

	@Override
	public Integer updateMgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateMgInfo(sheetInfo);
	}

	@Override
	public Integer updateSgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectBgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);		
		return evaluationMapper.upSelectBgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectMgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);		
		return evaluationMapper.upSelectMgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectSgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);		
		return evaluationMapper.upSelectSgInfo(sheetInfo);
	}

	@Override
	public Integer insertBfbg(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBfbg").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertBfbg(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectBfbg(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectBfbg(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectSheet(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectSheet(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectCode(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectCode(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectBgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectBgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectMgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectMgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectSgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectSgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectIgInfo(sheetInfo);
	}

	@Override
	public Integer updateIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateIgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateIgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectIgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> upSelectSubIgInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectSubIgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> upSelectSubIgMemoInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectSubIgMemoInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSubItemInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSubItemInfo(sheetInfo);
	}
	
	// 수정
	@Override
	public List<SheetInfo> selectSubReportInfo(SheetInfo sheetInfo){
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSubReportInfo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectSubMemoInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSubMemoInfo(sheetInfo);
	}
	@Override
	public Integer insertIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertIgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertIgInfo(sheetInfo);
	}

	@Override
	public Integer deleteIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteIgInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.deleteIgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectIgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectIgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectCate(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> upSelectMemo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.upSelectMemo(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectSheetMemo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetMemo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectResultMemo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectResultMemo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> excelDownSheetInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.excelDownSheetInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> excelDownSheetInfoResult(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.excelDownSheetInfoResult(sheetInfo);
	}

	@Override
	public List<SheetInfo> excelDownSheetAllInfoResult(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.excelDownSheetAllInfoResult(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectItemNameList(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectItemNameList(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectItemTotalAvgList(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectItemTotalAvgList(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectSectItemList(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSectItemList(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectItemMarkList(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectItemMarkList(sheetInfo);
	}

	@Override
	public Integer updateSheetInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSheetInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.updateSheetInfo(sheetInfo);
	}

	@Override
	public Integer upInsertBgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upInsertBgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.upInsertBgCate(sheetInfo);
	}

	@Override
	public Integer upInsertMgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upInsertMgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.upInsertMgCate(sheetInfo);
	}

	@Override
	public Integer upInsertSgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upInsertSgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.upInsertSgCate(sheetInfo);
	}

	@Override
	public Integer upInsertIgCate(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upInsertIgCate").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.upInsertIgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectsSgName(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectsSgName(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectFeedback(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectFeedback(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectEvalStatus(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectFeedback(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSheetUseBg(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetUseBg(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSheetUseMg(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetUseMg(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSheetUseSg(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetUseSg(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSheetUseIg(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSheetUseIg(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSubIgInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		return evaluationMapper.selectSubIgInfo(sheetInfo);
	}

	@Override
	public Integer insertInputsubInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertInputsubInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertInputsubInfo(sheetInfo);
	}
	
	@Override
	public Integer insertMemosubInfo(SheetInfo sheetInfo) {
		EvaluationDao evaluationMapper = sqlSession.getMapper(EvaluationDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMemosubInfo").getBoundSql(sheetInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationMapper.insertMemosubInfo(sheetInfo);
	}

}
