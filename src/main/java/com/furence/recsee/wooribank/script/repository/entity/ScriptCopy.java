package com.furence.recsee.wooribank.script.repository.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.wooribank.script.annotation.ColumnMap;
import com.furence.recsee.wooribank.script.repository.transform.DhtmlRowTransformable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScriptCopy extends DhtmlRowTransformable{
	
	
	private int rowNumber;
	
	/*
	 * 상품 PK
	 */
	@ColumnMap("rs_product_list_pk")
	@JsonProperty("rsProductListPk")
	private String productPk;
	
	/*
	 * 상품 타입 코드
	 */
	@ColumnMap("rs_product_type")
	@JsonProperty("rsProductType")
	private String typeCode;
	
	/**
	 * 상품 타입 이름
	 */
	@ColumnMap("rs_product_type_name")
	@JsonProperty("rsProductTypeName")
	private String typeName;
	
	
	/*
	 * 상품 타입 코드
	 */
	@ColumnMap("rs_product_type")
	@JsonProperty("rsScriptType")
	private String scriptTypeCode;

	/**
	 * 상품코드
	 */
	@ColumnMap("rs_product_code")
	@JsonProperty("rsProductCode")
	private String productCode;
	
	/**
	 * 상품이름
	 */
	@ColumnMap("rs_product_name")
	@JsonProperty("rsProductName")
	private String productName; 
	
	/**
	 * 스크립트이름
	 */
	@ColumnMap("rs_product_name")
	@JsonProperty("rsScriptName")
	private String scriptName; 
	
	/**
	 * 사용여부
	 */
	@ColumnMap("rs_use_yn")
	@JsonProperty("rsUseYn")
	private String useYn;
	
	/**
	 * 그룹여부
	 */
	@ColumnMap("rs_group_yn")
	@JsonProperty("rsGroupYn")
	private String groupYn;
	
	/**
	 * 그룹코드
	 */
	@ColumnMap("rs_group_code")
	@JsonProperty("rsGroupCode")
	private String groupCode;
	
	
	

	@Override
	public dhtmlXGridRow transform(String[] args) {
		
		dhtmlXGridRow rowItem = new dhtmlXGridRow();
		
		rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
		rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
		rowItem.setRowClass("searchProductForCopy");
		rowItem.setId("" + this.getRowNumber());

		// ch
		dhtmlXGridRowCell cellInfo = new dhtmlXGridRowCell();
		rowItem.getCellElements().add(cellInfo);

		// 번호
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.getRowNumber() + "");
//		cellInfo.setStyle( "color:	#8A93A4; font-weight:400;");
		rowItem.getCellElements().add(cellInfo);

		// 상품명
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.getProductName());
//		cellInfo.setStyle( "color : #1760F0; font-weight:400;");
		rowItem.getCellElements().add(cellInfo);

		// 상품유형이름
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.getTypeName());
//		cellInfo.setStyle( "color:	#8A93A4; font-weight:400;");	
		rowItem.getCellElements().add(cellInfo);
		
		//그룹코드
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.getGroupCode());
//		cellInfo.setStyle( "color:	#8A93A4; font-weight:400;");	
		rowItem.getCellElements().add(cellInfo);
		
		//그룹코드
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.getProductPk());
//		cellInfo.setStyle( "color:	#8A93A4; font-weight:400;");	
		rowItem.getCellElements().add(cellInfo);
		
		return rowItem;
		
	
	}
	
	
 
	
}

