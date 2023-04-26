package com.furence.recsee.wooribank.script.repository.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Product extends DhtmlRowTransformable{
	
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
	
	
	/**
	 * 상품 타입 코드
	 */
	@ColumnMap("rs_product_type")
	@JsonProperty("rsScriptType")
	private String scriptTypeCode;
	
	/**
	 * 상품 타입 이름
	 */
	@ColumnMap("rs_product_type_name")
	@JsonProperty("rsScriptTypeName")
	private String sctipTypeName;
	
		
	/**
	 * 상품 세부 타입 코드
	 */	
	@JsonProperty("rsCategoryCdoe")
	private String ctegoryCdoe;
	
	/**
	 * 상품 세부 타입 이름
	 */
	@JsonProperty("rsCategoryName")
	private String categoryName;
	
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
	
	/**
	 * 상품 참조
	 */
	@ColumnMap("rs_script_step_fk")
	@JsonProperty("rsScriptStepFk")
	private String scriptStepFk;
	
	
	@ColumnMap("rs_script_tts_update_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("rsTtsUpdateDate")
	private Date ttsUpdateDate;
	
	@ColumnMap("rs_script_reserved_update_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("rsReservedUpdateDate")
	private Date reservedUpdateDate;
	
	@ColumnMap("rs_script_last_update_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("rsLastUpdateDate")
	private Date lastUpdateDate;
	
	@ColumnMap("rs_script_last_update_user")
	@JsonProperty("rsLastUpdateUser")
	private String lastUpdateUser;
	
	@ColumnMap("rs_script_last_update_approve_user")
	@JsonProperty("rsLastApproveUser")
	private String lastApproveUser;
	
	/**
	 * 스크립트 등록 여부
	 */
	@ColumnMap("registeredYN")
	@JsonProperty("registered")
	private String registeredYN;
	
	
	@JsonProperty("groupInfo")
	private ProductGroup productGroup;
	
	

	@Override
	public dhtmlXGridRow transform(String args[]) {
		
		if(null == args || 0 == args.length) return null;
		
		String contextPath = args[0];
		
		dhtmlXGridRow rowItem = new dhtmlXGridRow();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
		rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
		rowItem.setRowClass("searchResultScript");
		rowItem.setId("" + this.getProductPk());
		
		// 권한별 칼럼 숨김(1) - args로 properties 값 받아오기
		String hiddenColumns = null; // properties에 hidden-columns로 정의된 칼럼들
		String[] hiddenColumnsArray = null; // 칼럼 배열
		if(args.length > 1) {
			if(args[1] != null) {
				hiddenColumns = args[1];
				hiddenColumnsArray = hiddenColumns.split(",");
			}
		}
		
		for (int i = 0; i < 21; i++) {
			
			dhtmlXGridRowCell cellInfo = new dhtmlXGridRowCell();
			
			switch (i) {
			case 0: // 체크박스 : 현재 사용 o
//				cellInfo.setName("checkbox");
//				if(this.reservedUpdateDate == null ) {
//					cellInfo.setStyle("visibility:hidden");
//				} 

				break;
			case 1:
				cellInfo.setName("rsScriptStepFk");
				cellInfo.setValue("" + this.getProductPk());
				break;
			case 2:
				cellInfo.setValue(this.getRowNumber() + "");
				break;
			case 3:
				boolean used = this.getUseYn().equals("Y");
				cellInfo.setName("rsUseYn");
				cellInfo.setValue( used ? "판매" : "판매중지");
				break;
			case 4:
				cellInfo.setName("rsScriptTypeName");
				cellInfo.setValue(this.getTypeName());
				break;
			case 5:
				cellInfo.setName("rsProductName");
				cellInfo.setValue(this.getProductName());
				break;
			case 6:
				cellInfo.setName("rsGroupYn");
				cellInfo.setValue(this.getGroupYn());
				break;
			case 7:
				cellInfo.setName("rsGroupCode");
				cellInfo.setValue(this.getGroupCode() == null ? "-" : this.getGroupCode());
				break;
			case 8:
				cellInfo.setName("rsProductCode");
				cellInfo.setValue(this.getProductCode());
				break;
			case 9:
				boolean registered = this.getRegisteredYN().equals("Y");
				cellInfo.setName("registeredYN");
				cellInfo.setValue( registered ? "등록" : "미등록" );
				if(!registered ) {
					cellInfo.setStyle( "color:red");
				}
				break;
			case 10:
				cellInfo.setName("rsTtsUpdateDate");
				cellInfo.setValue(this.ttsUpdateDate == null ? "-" : sdf.format(this.ttsUpdateDate));
				break;
			case 11:
				cellInfo.setName("rsReservedUpdateDate");
				cellInfo.setValue(this.reservedUpdateDate == null ? "-" : sdf.format(this.reservedUpdateDate));
				break;
			case 12:
				cellInfo.setValue("Y");
				break;
			case 13:
				cellInfo.setName("rsLastUpdateDate");
				cellInfo.setValue(this.lastUpdateDate == null ? "-" : sdf.format(this.lastUpdateDate));
				break;
			case 14:
				cellInfo.setName("rsLastUpdateUser");
				cellInfo.setValue(this.lastUpdateUser);
				break;
			case 15:
				cellInfo.setName("rsLastApproveUser");
				cellInfo.setValue(this.lastApproveUser);
				break;
			case 16:
				cellInfo.setName("scriptHistory");
				cellInfo.setValue("<button class='changeRecordBtn' onclick='showHistory("+this.getProductPk()+")'>변경이력</button>");
				break;
			case 17:
				cellInfo.setValue(this.getScriptStepFk()+"");
				break;
			case 18:
				cellInfo.setName("rsScriptType");
				cellInfo.setValue(this.getTypeCode());
				break;
			case 19:
				cellInfo.setName("rsProductPk");
				cellInfo.setValue(this.getProductPk());
				break;
			case 20:
				cellInfo.setName("nowPdfDownload");
				cellInfo.setValue("<img src='"+ contextPath + "/resources/common/recsee/images/project/icon/wooribank/iconPDF.gif' style='cursor:pointer' onclick='nowPdfDownload("+this.getProductPk()+")'>");
				break;
			}
			
			rowItem.getCellElements().add(cellInfo);
			
			if(args.length > 1) {
				if(args[1] != null) {
					if(Arrays.asList(hiddenColumnsArray).contains(cellInfo.getName())) {
						rowItem.getCellElements().remove(cellInfo);
					}
				}
			}
		}

		
		return rowItem;
			
	}
}
