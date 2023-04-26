package com.furence.recsee.wooribank.script.repository.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.facerecording.model.BkProductAndScriptStepDetail;
import com.furence.recsee.wooribank.facerecording.model.ProductListDto;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.script.repository.entity.ProductCharacterDetail;
import com.furence.recsee.wooribank.script.repository.entity.ProductFundDetail;

@Repository
public interface ExcelUploadDao {

	
	int conflicProductList(List<ProductListVo> readSheetOne) throws Exception;

	int insertScriptStep(List<ScriptStepVo> i);

	int insertScriptStepUnder(List<ScriptStepVo> i);

	String selectDetailFk(HashMap<String, String> hash);

	String selectNonRefEltCheck(String tId);

	void updateNonRefEltAttr(String tId);

	int insertScriptStepDetail(BkProductAndScriptStepDetail vo);

	int insertUpdateProductCharacterDetail(HashMap<String, List<ProductCharacterDetail>> resultList);

	int insertUpdateProductFundDetail(HashMap<String, List<ProductFundDetail>> resultList);

	int upsertInsureList(HashMap<String, List<HashMap<String,String>>> hash) throws Exception;

	int stepCheck(ProductListVo productListVo);

	int checkNonRelEltStepDetail(ProductListDto dto);

	
	
	
	

}
