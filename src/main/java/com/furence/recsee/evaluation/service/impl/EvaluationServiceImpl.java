package com.furence.recsee.evaluation.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.evaluation.dao.EvaluationDao;
import com.furence.recsee.evaluation.model.SheetInfo;
import com.furence.recsee.evaluation.service.EvaluationService;

@Service("evaluationService")
public class EvaluationServiceImpl implements EvaluationService{

	@Autowired
	EvaluationDao evaluationMapper;


	//selectStatistisList
	@Override
	public List<SheetInfo> selectStatistisList(SheetInfo sheetInfo) {
		return evaluationMapper.selectStatistisList(sheetInfo);
	}
	
	@Override
	public Integer insertSheetMemoInfo(SheetInfo sheetInfo) {
		return evaluationMapper.insertSheetMemoInfo(sheetInfo);
	}

	@Override
	public Integer updateSheetMemoInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateSheetMemoInfo(sheetInfo);
	}
	
	//selectIndividualStatistisList
	@Override
	public List<SheetInfo> selectIndividualStatistisList(SheetInfo sheetInfo) {
		return evaluationMapper.selectStatistisList(sheetInfo);
	}

	@Override
	public List<SheetInfo> insertSheetInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertSheetInfo(sheetInfo);
	}

	@Override
	public Integer updateSubInfo(SheetInfo sheetInfo){
		return evaluationMapper.updateSubInfo(sheetInfo);
	}
	@Override
	public Integer updateSubMemoInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateSubMemoInfo(sheetInfo);
	}
	@Override
	public Integer deleteSubInfo(SheetInfo sheetInfo){
		return evaluationMapper.deleteSubInfo(sheetInfo);
	}
	@Override
	public Integer deleteSubMemoInfo(SheetInfo sheetInfo) {
		return evaluationMapper.deleteSubMemoInfo(sheetInfo);
	}
	@Override
	public Integer updateItemMark(SheetInfo sheetInfo){
		return evaluationMapper.updateItemMark(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSheetList(SheetInfo sheetInfo){
		return evaluationMapper.selectSheetList(sheetInfo);
	}

	@Override
	public List<Map<String, Object>> selectSheetScore(Map<String, Object> param) {
		return evaluationMapper.selectSheetScore(param);
	}

	@Override
	public 	List<SheetInfo> itemList(String sheetCode){
		return evaluationMapper.itemList(sheetCode);
	}

	@Override
	public 	List<SheetInfo> sbgList(String sheetCode){
		return evaluationMapper.sbgList(sheetCode);
	}

	@Override
	public 	List<SheetInfo> smgList(String sheetCode){
		return evaluationMapper.smgList(sheetCode);
	}

	@Override
	public 	List<SheetInfo> ssgList(String sheetCode){
		return evaluationMapper.ssgList(sheetCode);
	}

	@Override
	public 	List<SheetInfo> subItemList(String itemCode){
		return evaluationMapper.subItemList(itemCode);
	}


	@Override
	public Integer updateBgCate(SheetInfo sheetInfo){
		return evaluationMapper.updateBgCate(sheetInfo);
	}

	@Override
	public Integer updateMgCate(SheetInfo sheetInfo){
		return evaluationMapper.updateMgCate(sheetInfo);
	}

	@Override
	public Integer updateSgCate(SheetInfo sheetInfo){
		return evaluationMapper.updateSgCate(sheetInfo);
	}

	@Override
	public Integer insertBgInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertBgInfo(sheetInfo);
	}

	@Override
	public Integer insertMgInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertMgInfo(sheetInfo);
	}

	@Override
	public Integer insertSgInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertSgInfo(sheetInfo);
	}

	@Override
	public Integer insertsubInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertsubInfo(sheetInfo);
	}

	@Override
	public String selectBgLastNum(SheetInfo sheetInfo){
		return evaluationMapper.selectBgLastNum(sheetInfo);
	}

	@Override
	public Integer insertBgCate(SheetInfo sheetInfo){
		return evaluationMapper.insertBgCate(sheetInfo);
	}

	@Override
	public Integer insertMgCate(SheetInfo sheetInfo){
		return evaluationMapper.insertMgCate(sheetInfo);
	}

	@Override
	public Integer insertSgCate(SheetInfo sheetInfo){
		return evaluationMapper.insertSgCate(sheetInfo);
	}

	@Override
	public String selectRegBgLastNum(){
		return evaluationMapper.selectRegBgLastNum();
	}

	@Override
	public String selectMgLastNum(SheetInfo sheetInfo){
		return evaluationMapper.selectMgLastNum(sheetInfo);
	}

	@Override
	public String selectRegMgLastNum(){
		return evaluationMapper.selectRegMgLastNum();
	}
	@Override
	public String selectSgLastNum(SheetInfo sheetInfo){
		return evaluationMapper.selectSgLastNum(sheetInfo);
	}
	@Override
	public String selectRegSgLastNum(){
		return evaluationMapper.selectRegSgLastNum();
	}

	@Override
	public Integer updateItemCate(SheetInfo sheetInfo){
		return evaluationMapper.updateItemCate(sheetInfo);
	}

	@Override
	public Integer insertItemInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertItemInfo(sheetInfo);
	}

	@Override
	public Integer insertItemCate(SheetInfo sheetInfo){
		return evaluationMapper.insertItemCate(sheetInfo);
	}

	@Override
	public String selectItemLastNum(SheetInfo sheetInfo){
		return evaluationMapper.selectItemLastNum(sheetInfo);
	}

	@Override
	public String selectRegItemLastNum(){
		return evaluationMapper.selectRegItemLastNum();
	}

	@Override
	public List<SheetInfo> selectSheetView(SheetInfo sheetInfo){
		return evaluationMapper.selectSheetView(sheetInfo);
	}

	@Override
	public SheetInfo selectRegBCode(SheetInfo sheetInfo){
		return evaluationMapper.selectRegBCode(sheetInfo);
	}

	@Override
	public SheetInfo selectAllRegCode(SheetInfo sheetInfo){
		return evaluationMapper.selectAllRegCode(sheetInfo);
	}

	@Override
	public Integer deleteRegBCode(SheetInfo sheetInfo){
		return evaluationMapper.deleteRegBCode(sheetInfo);
	}

	@Override
	public Integer deleteRegMCode(SheetInfo sheetInfo){
		return evaluationMapper.deleteRegMCode(sheetInfo);
	}

	@Override
	public Integer deleteRegSCode(SheetInfo sheetInfo){
		return evaluationMapper.deleteRegSCode(sheetInfo);
	}

	@Override
	public Integer deleteRegICode(SheetInfo sheetInfo){
		return evaluationMapper.deleteRegICode(sheetInfo);
	}

	@Override
	public Integer deleteSheetInfo(SheetInfo sheetInfo){
		return evaluationMapper.deleteSheetInfo(sheetInfo);
	}

	@Override
	public Integer deleteSheetBgCode(SheetInfo sheetInfo){
		return evaluationMapper.deleteSheetBgCode(sheetInfo);
	}

	@Override
	public Integer deleteSheetMgCode(SheetInfo sheetInfo){
		return evaluationMapper.deleteSheetMgCode(sheetInfo);
	}

	@Override
	public Integer deleteSheetSgCode(SheetInfo sheetInfo){
		return evaluationMapper.deleteSheetSgCode(sheetInfo);
	}

	@Override
	public Integer deleteSheetICode(SheetInfo sheetInfo){
		return evaluationMapper.deleteSheetICode(sheetInfo);
	}

	@Override
	public String selectBCodeInfo(){
		return evaluationMapper.selectBCodeInfo();
	}

	@Override
	public String selectMCodeInfo(){
		return evaluationMapper.selectMCodeInfo();
	}

	@Override
	public String selectSCodeInfo(){
		return evaluationMapper.selectSCodeInfo();
	}

	@Override
	public String selectSubItemLastNum(){
		return evaluationMapper.selectSubItemLastNum();
	}

	@Override
	public Integer insertSICate(SheetInfo sheetInfo){
		return evaluationMapper.insertSICate(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectSubItem(SheetInfo sheetInfo){
		return evaluationMapper.selectSubItem(sheetInfo);
	}

	@Override
	public 	Integer updateSubMark(SheetInfo sheetInfo){
		return evaluationMapper.updateSubMark(sheetInfo);
	}

	@Override
	public Integer updateMark(SheetInfo sheetInfo){
		return evaluationMapper.updateMark(sheetInfo);
	}

	@Override
	public String selectMarkEle(SheetInfo sheetInfo){
		return evaluationMapper.selectMarkEle(sheetInfo);
	}

	@Override
	public List<SheetInfo> searchSubMarkEle(SheetInfo sheetInfo){
		return evaluationMapper.searchSubMarkEle(sheetInfo);
	}

	@Override
	public SheetInfo searchSubMark(SheetInfo sheetInfo){
		return evaluationMapper.searchSubMark(sheetInfo);
	}

	@Override
	public List<SheetInfo> searchMarkEle(SheetInfo sheetInfo){
		return evaluationMapper.searchMarkEle(sheetInfo);
	}

	@Override
	public String searchMark(SheetInfo sheetInfo){
		return evaluationMapper.searchMark(sheetInfo);
	}

	@Override
	public Integer deleteSubItem(SheetInfo sheetInfo){
		return evaluationMapper.deleteSubItem(sheetInfo);
	}

	@Override
	public String maxSubItemMark(SheetInfo sheetInfo){
		return evaluationMapper.maxSubItemMark(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectBgTotMark(String sheetInfo){
		return evaluationMapper.selectBgTotMark(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectMgTotMark(String sheetInfo){
		return evaluationMapper.selectMgTotMark(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSgTotMark(String sheetInfo){
		return evaluationMapper.selectSgTotMark(sheetInfo);
	}
	@Override
	public String selectEleRegCode(SheetInfo sheetInfo){
		return evaluationMapper.selectEleRegCode(sheetInfo);
	}
	@Override
	public Integer updateBMark(SheetInfo sheetInfo){
		return evaluationMapper.updateBMark(sheetInfo);
	}
	@Override
	public Integer updateMMark(SheetInfo sheetInfo){
		return evaluationMapper.updateMMark(sheetInfo);
	}
	@Override
	public Integer updateSMark(SheetInfo sheetInfo){
		return evaluationMapper.updateSMark(sheetInfo);
	}
	@Override
	public Integer updateSubItem(SheetInfo sheetInfo){
		return evaluationMapper.updateSubItem(sheetInfo);
	}

	@Override
	public Integer insertCampInfo(SheetInfo sheetInfo){
		return evaluationMapper.insertCampInfo(sheetInfo);
	}
	@Override
	public Integer deleteCampInfo(SheetInfo sheetInfo){
		return evaluationMapper.deleteCampInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectCampList(){
		return evaluationMapper.selectCampList();
	}

	@Override
	public SheetInfo selectEvalResult(SheetInfo sheetInfo){
		return evaluationMapper.selectEvalResult(sheetInfo);
	}
	@Override
	public Integer updateEvalResult(SheetInfo sheetInfo){
		return evaluationMapper.updateEvalResult(sheetInfo);
	}
	@Override
	public Integer insertEvalResult(SheetInfo sheetInfo){
		return evaluationMapper.insertEvalResult(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectEvalGrid(SheetInfo sheetInfo){
		return evaluationMapper.selectEvalGrid(sheetInfo);
	}
	@Override
	public SheetInfo selectRecFile(SheetInfo sheetInfo){
		return evaluationMapper.selectRecFile(sheetInfo);
	}
	@Override
	public Integer updateEvalYn(SheetInfo sheetInfo){
		return evaluationMapper.updateEvalYn(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectEvalUseCheck(String sheetCode){
		return evaluationMapper.selectEvalUseCheck(sheetCode);
	}
	@Override
	public Integer updateCampInfo(SheetInfo campInfo){
		return evaluationMapper.updateCampInfo(campInfo);
	}
	//question_manage 대분류 select
	@Override
	public List<SheetInfo> selectBgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectBgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectCampBgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectCampBgInfo(sheetInfo);
	}
	//question_manage 중분류 select
	@Override
	public List<SheetInfo> selectMgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectMgInfo(sheetInfo);
	}
	//question_manage 소분류 select
	@Override
	public List<SheetInfo> selectSgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectSgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectCampSgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectCampSgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectCampIgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectCampIgInfo(sheetInfo);
	}
	//question_manage 대분류 delete
	@Override
	public Integer deleteBgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.deleteBgInfo(sheetInfo);
	}
	@Override
	public Integer deleteMgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.deleteMgInfo(sheetInfo);
	}
	@Override
	public Integer deleteSgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.deleteSgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectboxSheet(SheetInfo sheetInfo) {
		return evaluationMapper.selectboxSheet(sheetInfo);
	}

	@Override
	public Integer insertSheet(SheetInfo sheetInfo) {
		return evaluationMapper.insertSheet(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectSheet(SheetInfo sheetInfo) {
		return evaluationMapper.selectSheet(sheetInfo);
	}

	@Override
	public Integer updateBgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateBgInfo(sheetInfo);
	}

	@Override
	public Integer updateMgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateMgInfo(sheetInfo);
	}

	@Override
	public Integer updateSgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateSgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectBgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectBgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectMgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectMgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectSgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectSgInfo(sheetInfo);
	}
	@Override
	public Integer insertBfbg(SheetInfo sheetInfo) {
		return evaluationMapper.insertBfbg(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectBfbg(SheetInfo sheetInfo) {
		return evaluationMapper.selectBfbg(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectSheet(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectSheet(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectCode(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectCode(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectBgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectBgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectMgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectMgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectSgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectSgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectIgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectIgInfo(sheetInfo);
	}

	@Override
	public Integer updateIgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateIgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectIgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectIgInfo(sheetInfo);
	}
	@Override
	public 	List<SheetInfo> upSelectSubIgInfo(SheetInfo sheetInfo){
		return evaluationMapper.upSelectSubIgInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> upSelectSubIgMemoInfo(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectSubIgMemoInfo(sheetInfo);
	}
	@Override
	public 	List<SheetInfo> selectSubItemInfo(SheetInfo sheetInfo){
		return evaluationMapper.selectSubItemInfo(sheetInfo);
	}
	// 수정
	@Override
	public 	List<SheetInfo> selectSubReportInfo(SheetInfo sheetInfo){
		return evaluationMapper.selectSubReportInfo(sheetInfo);
	}
	@Override
	public List<SheetInfo> selectSubMemoInfo(SheetInfo sheetInfo) {
		return evaluationMapper.selectSubMemoInfo(sheetInfo);
	}
	@Override
	public Integer deleteIgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.deleteIgInfo(sheetInfo);
	}

	@Override
	public Integer insertIgInfo(SheetInfo sheetInfo) {
		return evaluationMapper.insertIgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectIgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectIgInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectCate(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> upSelectMemo(SheetInfo sheetInfo) {
		return evaluationMapper.upSelectMemo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectSheetMemo(SheetInfo sheetInfo) {
		return evaluationMapper.selectSheetMemo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> selectResultMemo(SheetInfo sheetInfo) {
		return evaluationMapper.selectResultMemo(sheetInfo);
	}
	
	@Override
	public Integer updateSubMemo(SheetInfo sheetInfo) {
		return evaluationMapper.updateSubMemo(sheetInfo);
	}
	
	@Override
	public Integer deleteSubMemo(SheetInfo sheetInfo) {
		return evaluationMapper.deleteSubMemo(sheetInfo);
	}
	
	@Override
	public Integer InsertSubMemo(SheetInfo sheetInfo) {
		return evaluationMapper.InsertSubMemo(sheetInfo);
	}
	
	@Override
	public List<SheetInfo> excelDownSheetInfo(SheetInfo sheetInfo) {
		return evaluationMapper.excelDownSheetInfo(sheetInfo);
	}

	@Override
	public List<SheetInfo> excelDownSheetInfoResult(SheetInfo sheetInfo) {
		return evaluationMapper.excelDownSheetInfoResult(sheetInfo);
	}

	@Override
	public List<SheetInfo> excelDownSheetAllInfoResult(SheetInfo sheetInfo) {
		return evaluationMapper.excelDownSheetAllInfoResult(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectItemNameList(SheetInfo sheetInfo) {
		return evaluationMapper.selectItemNameList(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectItemTotalAvgList(SheetInfo sheetInfo) {
		return evaluationMapper.selectItemTotalAvgList(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectItemMarkList(SheetInfo sheetInfo) {
		return evaluationMapper.selectItemMarkList(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectSectItemList(SheetInfo sheetInfo) {
		return evaluationMapper.selectSectItemList(sheetInfo);
	}

	@Override
	public Integer updateSheetInfo(SheetInfo sheetInfo) {
		return evaluationMapper.updateSheetInfo(sheetInfo);
	}

	@Override
	public Integer upInsertBgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upInsertBgCate(sheetInfo);
	}

	@Override
	public Integer upInsertMgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upInsertMgCate(sheetInfo);
	}

	@Override
	public Integer upInsertSgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upInsertSgCate(sheetInfo);
	}

	@Override
	public Integer upInsertIgCate(SheetInfo sheetInfo) {
		return evaluationMapper.upInsertIgCate(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectsSgName(SheetInfo sheetInfo) {
		return evaluationMapper.selectsSgName(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectFeedback(SheetInfo sheetInfo) {
		return evaluationMapper.selectFeedback(sheetInfo);
	}

	@Override
	public List<SheetInfo> selectEvalStatus(SheetInfo sheetInfo) {
		return evaluationMapper.selectEvalStatus(sheetInfo);
	}
	@Override
	public 	List<SheetInfo> selectSheetUseBg(SheetInfo sheetInfo){
		return evaluationMapper.selectSheetUseBg(sheetInfo);
	}
	@Override
	public 	List<SheetInfo> selectSheetUseMg(SheetInfo sheetInfo){
		return evaluationMapper.selectSheetUseMg(sheetInfo);
	}
	@Override
	public 	List<SheetInfo> selectSheetUseSg(SheetInfo sheetInfo){
		return evaluationMapper.selectSheetUseSg(sheetInfo);
	}
	@Override
	public 	List<SheetInfo> selectSheetUseIg(SheetInfo sheetInfo){
		return evaluationMapper.selectSheetUseIg(sheetInfo);
	}
	
	@Override
	public Integer insertInputsubInfo(SheetInfo sheetInfo) {
		return evaluationMapper.insertInputsubInfo(sheetInfo);
	}
	
	@Override
	public Integer insertMemosubInfo(SheetInfo sheetInfo) {
		return evaluationMapper.insertMemosubInfo(sheetInfo);
	}
}
