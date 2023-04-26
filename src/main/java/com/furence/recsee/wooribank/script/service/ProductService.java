package com.furence.recsee.wooribank.script.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.annotation.ColumnMapUtil;
import com.furence.recsee.wooribank.script.config.ScriptManageConfig;
import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.response.ScriptSnapshot;
import com.furence.recsee.wooribank.script.repository.dao.ProductDao;
import com.furence.recsee.wooribank.script.repository.entity.Product;
import com.furence.recsee.wooribank.script.repository.entity.ProductGroup;
import com.furence.recsee.wooribank.script.repository.entity.ProductHistory;
import com.furence.recsee.wooribank.script.repository.entity.ScriptCopy;
import com.furence.recsee.wooribank.script.util.DhtmlGridDataProvider;

@Service
public class ProductService  {

	private ProductDao productDao;
	
	@Value("#{scriptManageProperties['grid.productgrid.limited-levels']}")
	private String[] limitedLevels;
	
	@Value("#{scriptManageProperties['grid.productgrid.limited-levels.hidden-columns']}")
	private String hiddenColumns;
	
	private ScriptManageConfig config; 
	
	@Autowired
	public ProductService(ProductDao productDao, ScriptManageConfig config) {
		this.productDao = productDao;
		this.config = config;
	}
	
	/**
	 * 상품 조회
	 * @param searchDTO
	 * @return
	 */
	private List<Product> getListOfProduct(ProductParam.Search searchDTO) {
		
		return this.productDao.selectProductList(searchDTO);	
	}
	
	/**
	 * 상품 조회 결과 카운트
	 * @param searchDTO
	 * @return
	 */
	private int getCountOfProductList(ProductParam.Search searchDTO) {
		
		return this.productDao.selectProductListCount(searchDTO);	
	}
	
	/**
	 * 상품그룹 정보 조회
	 * @param productListPk
	 * @return
	 */
	private List<ProductGroup> getListOfProductGroup(String productListPk) {
		
		return this.productDao.selectProductGroupInfo(productListPk);	
	}
	
	/**
	 * 상품 스크립틔 변경 이력 조회
	 * @param productListPk
	 * @return
	 */
	private List<ProductHistory> getListOfProductHistory(String productListPk) {
		
		return this.productDao.selectProductHistory(productListPk);
	}
	
	/**
	 * 상품 스크립틔 변경 이력 카운트 조회
	 * @param productListPk
	 * @return
	 */
	private int getCountOfProductHistory(String productListPk) {
		
		return this.productDao.selectCountProductHistory(productListPk);
	}
	
	/**
	 * 상품 스크립트의 버전 데이터 조회
	 * @param history
	 * @return
	 */
	private List<ScriptSnapshot> getListOfVersion(ScriptEditParam.History history) {
		
		return this.productDao.selectVersionDetail(history);
	}
	
	/**
	 * 스크립트 미등록 상품 리스트 조회
	 * @param copyParam
	 * @return
	 */
	private List<ScriptCopy> getProductCopyList(ProductParam.NotRegistered copyParam) {
		
		return this.productDao.selectProductCopyList(copyParam);	
	}
	
	/**
	 * 스크립트 미등록 상품 리스트 카운트
	 * @param copyParam
	 * @return
	 */
	private int getCountOfProductCopyList(ProductParam.NotRegistered copyParam) {
		
		return this.productDao.selectProductCopyListCount(copyParam);	
	}
	
	/**
	 * 상품 정보 그리드 헤더 
	 * @param searchDTO
	 * @param sessAccessList
	 * @param args
	 * @return
	 */
	public dhtmlXGridXml getDhtmlxHeaderOfProductList(
			ProductParam.Search searchDTO, List<MMenuAccessInfo> sessAccessList, String[] args) {
		
		// 접속자 계정 권한 체크 - 일부 칼럼 hidden 처리용
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "scriptRegistration");
		String nowLevelCode = nowAccessInfo.getLevelCode();
		
		if(Arrays.asList(limitedLevels).contains(nowLevelCode)) {
			args[1] = this.config.getProperty("grid.productgrid.limited-levels."+nowLevelCode+".hidden-columns") ;//this.hiddenColumns;
		}
		
		return DhtmlGridDataProvider.ProductGrid.getHeaders(args);
	}
	
	/**
	 * 조회조건에 해당하는 상품정보를 dhtmlx 용  xml 형태로 반환
	 * @param searchDTO - 조회조건
	 * @return
	 */
	public dhtmlXGridXml getDhtmlxRowOfProductList(
			ProductParam.Search searchDTO, List<MMenuAccessInfo> sessAccessList, String[] args) {
		
		// 접속자 계정 권한 체크 - 일부 칼럼 hidden 처리용
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "scriptRegistration");
		String nowLevelCode = nowAccessInfo.getLevelCode();
		
		if(Arrays.asList(limitedLevels).contains(nowLevelCode)) {
			args[1] = this.config.getProperty("grid.productgrid.limited-levels."+nowLevelCode+".hidden-columns") ;//this.hiddenColumns;
		}
		
		if(searchDTO.getOrderBy() != null) {
			String columnName = 
				ColumnMapUtil.getColumnNameByJsonProperty(Product.class, searchDTO.getOrderBy());
			searchDTO.setOrderBy(columnName);
		}
		
		List<Product> result = this.getListOfProduct(searchDTO);
		Integer totalCount = this.getCountOfProductList(searchDTO);
		
		dhtmlXGridXml xmls = DhtmlGridDataProvider.ProductGrid.getRows(result, args);
		xmls.setTotal_count( totalCount != null ? totalCount.toString() : "0");
		xmls.setPos(searchDTO.getOffset()+"");
		
		return xmls;
	}
	
	/**
	 * 상품의 이름,상품코드,그룹이름,그롭코드,같은 그룹의 상품코드목록 반환
	 * @param productListPk
	 * @return
	 */
	public ProductGroup getProductById(String productListPk) throws Exception{	
		
		List<ProductGroup> list = this.getListOfProductGroup(productListPk);		
		list = Optional.ofNullable(list).orElse(Collections.emptyList());		
		return list.isEmpty() ? null : list.get(0);
	}
	
	/**
	 * 상품스크립트 변경이력 존재여부 확인
	 * @param productListPk
	 * @return
	 */
	public boolean isExistHistory(String productListPk) {		
		return this.getCountOfProductHistory(productListPk) > 0;
	}

	/**
	 * 상품스크립트 수정일자와 버전 목록 반환
	 * @param productListPk
	 * @return
	 */
	public List<ProductHistory> getHistoryById(String productListPk) {		
		return this.getListOfProductHistory(productListPk);
	}

	/**
	 * 상품스크립트 버전 상세 반환
	 * @param productHistory
	 * @return
	 */
	public ScriptSnapshot getVersionSnapshot(ScriptEditParam.History history) {
		List<ScriptSnapshot> list = this.getListOfVersion(history);
		list = Optional.ofNullable(list).orElse(Collections.emptyList());		
		return list.isEmpty() ? null : list.get(0);
	}
	
	/**
	 * 스크립트 미등록 상품 조회
	 * @param copyParam
	 * @return
	 * @throws Exception
	 */
	public dhtmlXGridXml getDhtmlxRowOfCopyProductList(ProductParam.NotRegistered copyParam) throws Exception{
		
		if(copyParam.getOrderBy() != null) {
			String columnName = 
				ColumnMapUtil.getColumnNameByJsonProperty(ScriptCopy.class, copyParam.getOrderBy());
			copyParam.setOrderBy(columnName);
		}

		List<ScriptCopy> result = this.getProductCopyList(copyParam);
		Integer totalCount = this.getCountOfProductCopyList(copyParam);
		
		dhtmlXGridXml xmls = DhtmlGridDataProvider.ScriptCopyGrid.getRows(result, null);
		xmls.setTotal_count( totalCount != null ? totalCount.toString() : "0");
		xmls.setPos(copyParam.getOffset()+"");
		
		return xmls;
	}

	public String isEltProductCheck(String productListPk) throws Exception {
		
		String result = Optional.ofNullable(productDao.isEltProductCheck(productListPk)).orElse("N");
		
		return result;
	}

}
