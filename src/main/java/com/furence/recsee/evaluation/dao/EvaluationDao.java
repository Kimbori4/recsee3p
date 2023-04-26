package com.furence.recsee.evaluation.dao;

import java.util.List;
import java.util.Map;

import com.furence.recsee.evaluation.model.SheetInfo;

public interface EvaluationDao {
	List<SheetInfo> insertSheetInfo(SheetInfo sheetInfo);	
	List<SheetInfo> selectSheetList(SheetInfo sheetInfo);
	List<Map<String, Object>> selectSheetScore(Map<String, Object> param);
	List<SheetInfo> itemList(String SheetCode);
	List<SheetInfo> sbgList(String SheetCode);
	List<SheetInfo> smgList(String SheetCode);
	List<SheetInfo> ssgList(String SheetCode);
	List<SheetInfo> subItemList(String itemCode);

	//evaluationStatistics Select Part
	List<SheetInfo> selectStatistisList(SheetInfo sheetInfo);

	//question_manage 대분류 select
	List<SheetInfo> selectBgInfo(SheetInfo sheetInfo);
	List<SheetInfo> selectCampBgInfo(SheetInfo sheetInfo);
	//question_manage 중분류 select
	List<SheetInfo> selectMgInfo(SheetInfo sheetInfo);
	//question_manage 소분류 select
	List<SheetInfo> selectSgInfo(SheetInfo sheetInfo);
	List<SheetInfo> selectCampSgInfo(SheetInfo sheetInfo);
	List<SheetInfo> selectCampIgInfo(SheetInfo sheetInfo);
	//question_manage 세부사항 select
	List<SheetInfo> selectIgInfo(SheetInfo sheetInfo);
	List<SheetInfo> selectSubIgInfo(SheetInfo sheetInfo);
	List<SheetInfo> selectSubMemoInfo(SheetInfo sheetInfo);	
	//question_manage 대분류 update
	Integer updateBgInfo(SheetInfo sheetInfo);
	//question_manage 중분류 update
	Integer updateMgInfo(SheetInfo sheetInfo);
	//question_manage 소분류 update
	Integer updateSgInfo(SheetInfo sheetInfo);
	//question_manage 세부사항 update
	Integer updateIgInfo(SheetInfo sheetInfo);
	Integer updateSubInfo(SheetInfo sheetInfo);
	Integer updateSubMemoInfo(SheetInfo sheetInfo);	
	Integer deleteSubInfo(SheetInfo sheetInfo);
	Integer deleteSubMemoInfo(SheetInfo sheetInfo);
	Integer updateSubMemo(SheetInfo sheetInfo);	
	Integer InsertSubMemo(SheetInfo sheetInfo);	
	Integer deleteSubMemo(SheetInfo sheetInfo);	
	Integer insertSheetMemoInfo(SheetInfo sheetInfo);
	Integer updateSheetMemoInfo(SheetInfo sheetInfo);
	
	//question_manage 대분류 수정버튼 클릭 시 select
	List<SheetInfo> upSelectBgInfo(SheetInfo sheetInfo);
	//question_manage 중분류 수정버튼 클릭 시 select
	List<SheetInfo> upSelectMgInfo(SheetInfo sheetInfo);
	//question_manage 소분류 수정버튼 클릭 시 select
	List<SheetInfo> upSelectSgInfo(SheetInfo sheetInfo);
	//question_manage 세부사항 수정버튼 클릭시 select
	List<SheetInfo> upSelectIgInfo(SheetInfo sheetInfo);
	List<SheetInfo> upSelectSubIgInfo(SheetInfo sheetInfo);
	List<SheetInfo> upSelectSubIgMemoInfo(SheetInfo sheetInfo);	
	List<SheetInfo> selectSubItemInfo(SheetInfo sheetInfo);
	List<SheetInfo> selectSubReportInfo(SheetInfo sheetInfo);	// 수정
	//evaluation manage 평가지 만들기 대분류 추가
	Integer insertBfbg(SheetInfo sheetInfo);
	//evaluation manage 평가지 만들기 대분류 추가 후 뿌려주기
	List<SheetInfo> selectBfbg(SheetInfo sheetInfo);
	//evaluation manage에서 수정 눌렀을 때 그 값 팝업창에 뿌려주기
	List<SheetInfo> upSelectSheet(SheetInfo sheetInfo);
	//evaluation manage에서 수정 눌렀을 때 그 값에 대한 evaluation magician에 뿌려주기
	List<SheetInfo> upSelectCode(SheetInfo sheetInfo);
	//evaluation manage에서 수정 눌렀을 때 대분류 뽑아오기
	List<SheetInfo> upSelectBgCate(SheetInfo sheetInfo);
	//evaluation manage에서 수정 눌렀을 때 중분류 뽑아오기
	List<SheetInfo> upSelectMgCate(SheetInfo sheetInfo);
	//evaluation manage에서 수정 눌렀을 때 소분류 뽑아오기
	List<SheetInfo> upSelectSgCate(SheetInfo sheetInfo);
	//evaluation manage에서 수정 => 세부사항
	List<SheetInfo> upSelectIgCate(SheetInfo sheetInfo);
	//evaluation magician에서 새로운 대 분류 추가
	Integer upInsertBgCate(SheetInfo sheetInfo);
	//evaluation magician에서 새로운 중 분류 추가
	Integer upInsertMgCate(SheetInfo sheetInfo);
	//evaluation magician에서 새로운 소 분류 추가
	Integer upInsertSgCate(SheetInfo sheetInfo);
	//evaluation magician에서 새로운 세부 분류 추가
	Integer upInsertIgCate(SheetInfo sheetInfo);


	Integer updateItemMark(SheetInfo sheetInfo);

	//evaluation manage에서 수정->모든 분류 다 뽑아오기
	List<SheetInfo> upSelectCate(SheetInfo sheetInfo);
	
	List<SheetInfo> upSelectMemo(SheetInfo sheetInfo);
	List<SheetInfo> selectSheetMemo(SheetInfo sheetInfo);
	
	List<SheetInfo> selectResultMemo(SheetInfo sheetInfo);
	
	//엑셀 다운로드 정보 가져오기
	List<SheetInfo> excelDownSheetInfo(SheetInfo sheetInfo);
	List<SheetInfo> excelDownSheetInfoResult(SheetInfo sheetInfo);
	List<SheetInfo> excelDownSheetAllInfoResult(SheetInfo sheetInfo);
	List<SheetInfo> selectItemNameList(SheetInfo sheetInfo);
	List<SheetInfo> selectSectItemList(SheetInfo sheetInfo);
	List<SheetInfo> selectItemTotalAvgList(SheetInfo sheetInfo);
	
	List<SheetInfo> selectItemMarkList(SheetInfo sheetInfo);

	//시트 업데이트
	Integer updateSheetInfo(SheetInfo sheetInfo);

	Integer updateBgCate(SheetInfo sheetInfo);
	Integer updateMgCate(SheetInfo sheetInfo);
	Integer updateSgCate(SheetInfo sheetInfo);
	Integer insertBgInfo(SheetInfo sheetInfo);
	Integer insertMgInfo(SheetInfo sheetInfo);
	Integer insertSgInfo(SheetInfo sheetInfo);
	Integer insertIgInfo(SheetInfo sheetInfo);
	Integer insertsubInfo(SheetInfo sheetInfo);
	String selectBgLastNum(SheetInfo sheetInfo);
	Integer insertBgCate(SheetInfo sheetInfo);
	Integer insertMgCate(SheetInfo sheetInfo);
	Integer insertSgCate(SheetInfo sheetInfo);
	String selectRegBgLastNum();
	String selectMgLastNum(SheetInfo sheetInfo);
	String selectRegMgLastNum();
	String selectSgLastNum(SheetInfo sheetInfo);
	String selectRegSgLastNum();

	Integer updateItemCate(SheetInfo sheetInfo);
	Integer insertItemInfo(SheetInfo sheetInfo);
	Integer insertItemCate(SheetInfo sheetInfo);
	String selectItemLastNum(SheetInfo sheetInfo);
	String selectRegItemLastNum();
	List<SheetInfo> selectSheetView(SheetInfo sheetInfo);
	SheetInfo selectRegBCode(SheetInfo sheetInfo);

	SheetInfo selectAllRegCode(SheetInfo sheetInfo);
	Integer deleteRegBCode(SheetInfo sheetInfo);
	Integer deleteRegMCode(SheetInfo sheetInfo);
	Integer deleteRegSCode(SheetInfo sheetInfo);
	Integer deleteRegICode(SheetInfo sheetInfo);
	Integer deleteSheetInfo(SheetInfo sheetInfo);
	Integer deleteSheetBgCode(SheetInfo sheetInfo);
	Integer deleteSheetMgCode(SheetInfo sheetInfo);
	Integer deleteSheetSgCode(SheetInfo sheetInfo);
	Integer deleteSheetICode(SheetInfo sheetInfo);
	//question_manager 대분류 delete
	Integer deleteBgInfo(SheetInfo sheetInfo);
	//question_manage 중분류 delete
	Integer deleteMgInfo(SheetInfo sheetInfo);
	//question_manage 소분류 delete
	Integer deleteSgInfo(SheetInfo sheetInfo);
	//question_manage 세부사항 delete
	Integer deleteIgInfo(SheetInfo sheetInfo);

	//캠페인 만들기 selectbox sheetinfo 뽑기
	List<SheetInfo> selectboxSheet(SheetInfo sheetInfo);
	//캠페인 만들기 시트추가
	Integer insertSheet(SheetInfo sheetInfo);
	//캠페인 만들기 그리드 뽑기
	List<SheetInfo> selectSheet(SheetInfo sheetInfo);
	String selectBCodeInfo();
	String selectMCodeInfo();
	String selectSCodeInfo();

	String selectSubItemLastNum();
	Integer insertSICate(SheetInfo sheetInfo);
	List<SheetInfo> selectSubItem(SheetInfo sheetInfo);

	Integer updateSubMark(SheetInfo sheetInfo);
	Integer updateMark(SheetInfo sheetInfo);
	String selectMarkEle(SheetInfo sheetInfo);

	List<SheetInfo> searchSubMarkEle(SheetInfo sheetInfo);
	SheetInfo searchSubMark(SheetInfo sheetInfo);
	List<SheetInfo> searchMarkEle(SheetInfo sheetInfo);
	String searchMark(SheetInfo sheetInfo);

	Integer deleteSubItem(SheetInfo sheetInfo);
	String maxSubItemMark(SheetInfo sheetInfo);

	List<SheetInfo> selectBgTotMark(String sheetInfo);
	List<SheetInfo> selectMgTotMark(String sheetInfo);
	List<SheetInfo> selectSgTotMark(String sheetInfo);
	String selectEleRegCode(SheetInfo sheetInfo);
	Integer updateBMark(SheetInfo sheetInfo);
	Integer updateMMark(SheetInfo sheetInfo);
	Integer updateSMark(SheetInfo sheetInfo);
	Integer updateSubItem(SheetInfo sheetInfo);

	Integer insertCampInfo(SheetInfo sheetInfo);
	Integer deleteCampInfo(SheetInfo sheetInfo);

	List<SheetInfo> selectCampList();
	SheetInfo selectEvalResult(SheetInfo sheetInfo);
	Integer updateEvalResult(SheetInfo sheetInfo);
	Integer insertEvalResult(SheetInfo sheetInfo);

	List<SheetInfo> selectEvalGrid(SheetInfo sheetInfo);
	SheetInfo selectRecFile(SheetInfo sheetInfo);
	Integer updateEvalYn(SheetInfo sheetInfo);
	List<SheetInfo> selectEvalUseCheck(String sheetCode);
	Integer updateCampInfo(SheetInfo campInfo);

	List<SheetInfo> selectsSgName(SheetInfo sheetInfo);

	//피드백 추가하면서 추가함
	List<SheetInfo> selectFeedback(SheetInfo sheetInfo);
	List<SheetInfo> selectEvalStatus(SheetInfo sheetInfo);

	List<SheetInfo> selectSheetUseBg(SheetInfo sheetInfo);
	List<SheetInfo> selectSheetUseMg(SheetInfo sheetInfo);
	List<SheetInfo> selectSheetUseSg(SheetInfo sheetInfo);
	List<SheetInfo> selectSheetUseIg(SheetInfo sheetInfo);
	
	Integer insertInputsubInfo(SheetInfo sheetInfo);	
	Integer insertMemosubInfo(SheetInfo sheetInfo);	

}
