package com.furence.recsee.wooribank.script.repository.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.response.ScriptSnapshot;
import com.furence.recsee.wooribank.script.repository.entity.Product;
import com.furence.recsee.wooribank.script.repository.entity.ProductGroup;
import com.furence.recsee.wooribank.script.repository.entity.ProductHistory;
import com.furence.recsee.wooribank.script.repository.entity.ProductScriptVariable;
import com.furence.recsee.wooribank.script.repository.entity.ScriptCopy;

@Repository
public interface ProductDao {
	
	/**
	 * 상품 정보 조회
	 * @param searConditionDTO
	 * @return
	 */
	public List<Product> selectProductList(ProductParam.Search product);
	
	/**
	 * 전체 상품 카운트 조회
	 * @param searConditionDTO
	 * @return
	 */
	public Integer selectProductListCount(ProductParam.Search product);
	
	
	/**
	 * 상품의 소속그룹 정보 조회
	 * @param productListPk
	 * @return
	 */
	public List<ProductGroup> selectProductGroupInfo(String productListPk);
	
	/**
	 * 상품스크립트 변경이력 카운트 조회
	 * @param productListPk
	 * @return
	 */
	public int selectCountProductHistory(String productListPk);
	
	
	/**
	 * 상품스크립트 변경이력 조회
	 * @param productListPk
	 * @return
	 */
	public List<ProductHistory> selectProductHistory(String productListPk);
	
	
	/**
	 * 상품스크립트 변경이력 상세 조회
	 * @param productHistoryDto
	 * @return
	 */
	public List<ScriptSnapshot> selectVersionDetail(ScriptEditParam.History history);
	
	
	/**
	 * 상품스크립트 복사할 상세 조회
	 * @param scriptCopyGridDto
	 * @return
	 */

	public List<ScriptCopy> selectProductCopyList(ProductParam.NotRegistered copyParam);
	
	/**
	 * 상품스크립트 복사할  상품 카운트 조회
	 * @param searConditionDTO
	 * @return
	 */
	public Integer selectProductCopyListCount(ProductParam.NotRegistered copyParam);


	/**
	 * 상품 스크립트용 가변값 조회
	 * @param param
	 * @return
	 */
	public List<ProductScriptVariable> selectProductVariables(ProductParam.ScriptVariable param);

	
	
	
	public String isEltProductCheck(String productListPk);
	
	
	
}
