package com.furence.recsee.wooribank.script.repository.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.wooribank.script.repository.transform.DhtmlRowTransformable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
	
@Getter
@Setter
@ToString
public class ScriptStep extends DhtmlRowTransformable implements ScriptEntity {
	
	/**
	 * 조회 결과 로우넘버
	 */
	private int rowNumber;
	
	/**
	 * 상하위 스크립트 단계
	 */
	private int depth;
	
	/**
	 * 스크립트 고유키
	 */
	@JsonProperty("rsScriptStepPk")
	private String stepPk;
	
	/**
	 * 스크립트가 속한 상품 키
	 */
	@JsonProperty("rsScriptStepFk")
	private String productPk;
	
	/**
	 * 스크립트 스텝의 임시 pk
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsScriptStepTempPk")
	private String scriptStepTempPk;
	
	/**
	 * 스크립트가 속한 부모의 스크립트 키
	 */
	@JsonProperty("rsScriptStepParent")
	private String stepParentPk;
	
	/**
	 * 스크립트 스텝 이름
	 */
	@JsonProperty("rsScriptStepName")
	private String stepName;
	
	/**
	 * 스크립트 스텝 종류(P:스크립트 이름에 '설명'이 포함된 경우 N:그 외)
	 */
	@JsonProperty("rsScriptStepType")
	private String stepType;
	
	/**
	 * 디비에 입력된 각 뎁스별 오더
	 */
	@JsonProperty("rsScriptStepOrder")
	private int stepOrder;
	
	/**
	 * 스크립트 스텝 flat 정렬순서
	 */
	@JsonInclude(Include.NON_NULL)
	private String flatOrder;
	
	/**
	 * 스크립트 스텝 정렬순서
	 */
	@JsonProperty("rsUseYn")
	private String useYn;
	
	/**
	 * 스크립트 version
	 */
	@JsonProperty("rsScriptVersion")
	private String scriptVersion;
	
	/**
	 * cell 에 들어갈 이미지등 리소스경로시 사용할 context path
	 */
	@JsonIgnore
	private String resourcePath;
	
	/**
	 * 스크립트 하위스텝 리스트
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("subStepList")
	private List<ScriptStep> subStepList;
	
	/**
	 * 스크립트 디테일 리스트
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("scriptDetailList")
	private List<ScriptDetail> detailList;


	@Override
	public dhtmlXGridRow transform(String[] args) {
		
		dhtmlXGridRow rowItem = new dhtmlXGridRow();
		
		rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
		rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
		rowItem.setId(this.stepPk);
		
		// ch
		dhtmlXGridRowCell cellInfo = new dhtmlXGridRowCell();
		
		// 0 ScriptStepText
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.stepName);
		rowItem.getCellElements().add(cellInfo);
		

		// 1 ScriptStepPK
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(String.valueOf(this.stepPk));
		rowItem.getCellElements().add(cellInfo);
		

		// 2 ScriptStepParent
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(String.valueOf(this.stepParentPk));
		rowItem.getCellElements().add(cellInfo);
		

		// 3 ScriptStepFk
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(String.valueOf(this.productPk));
		rowItem.getCellElements().add(cellInfo);
		cellInfo = null;

		// 4 ScriptStepName
		cellInfo = new dhtmlXGridRowCell();
		cellInfo.setValue(this.stepName);
		rowItem.getCellElements().add(cellInfo);
		
		
		
		// 5 addScriptStepRowLank
		cellInfo = new dhtmlXGridRowCell();
		StringBuilder htmlCotent = new StringBuilder();
		
		int i = this.rowNumber;
		
		if( this.getStepParentPk().equals("0")) {
			
			rowItem.setXmlkids("1");
			
			htmlCotent.append("<div class='stepDivWrap'>");
			htmlCotent.append("	   <div id='sc_gird_step_one_de"+ this.stepPk +"' class='scGirdStepOneDe stepAddDeleteBtn' style='visibility: hidden'>");
			htmlCotent.append("        <div id='sc_gird_add"+i+"' class='scGirdAdd' onClick='addRowRank(\""+ this.stepPk +"\")' style='display : inline-block ;margin-right : 10px; cursor: pointer; width : 20px; text-align :center;  position: relative; top: 5px'>");
			htmlCotent.append("	           <img id src='"+ args[0] +"/resources/common/recsee/images/project/icon/plus.png ' style='width:20px; margin : auto;'/>");
			htmlCotent.append("        </div>");
            htmlCotent.append("    <div id='sc_gird_delete"+ this.stepPk +"' class='scGirdDelete' onClick='deleteRowRank(\""+ this.stepPk +"\")' style='cursor: pointer; display : inline-block ;margin-right : 10px;text-align :center; width : 20px; position: relative; top: 5px'>");
            htmlCotent.append("        <img id src='"+ args[0] + "/resources/common/recsee/images/project/icon/wooribank/ic_trash.svg ' style='width:20px; margin : auto;'/></div>");
            htmlCotent.append("    </div>");
            htmlCotent.append("    <div class='showIfEdited' style='display:none; color:red;'>[수정]</div>");
            htmlCotent.append("</div>");
			
		} else {
			
			htmlCotent.append("<div class='stepDivWrap'>");
			htmlCotent.append("    <div class='placeholderDiv' style='width:30px'></div>");
			htmlCotent.append("    <div id='sc_gird_step_two_de" + this.stepPk +"' class ='scGirdStepTwoDe stepAddDeleteBtn' style='visibility: hidden'>");
			htmlCotent.append("    <div id='sc_gird_delete_rowLank" + this.stepPk +"' class='scGirddeleteRowLank' onClick='deleteRowRank(\""+ this.stepPk +"\")' style='cursor: pointer; width : 20px;text-align :center; display : inline-block ;margin-right : 10px; position: relative; top: 5px\'>");
			htmlCotent.append("        <img id src='"+ args[0] + "/resources/common/recsee/images/project/icon/wooribank/ic_trash.svg ' style='width:20px; margin : auto;'/></div>");
			htmlCotent.append("    </div>");
			htmlCotent.append("    <div class='showIfEdited' style='display:none; color:red;'>[수정]</div>");
			htmlCotent.append("</div>");
		}
		
		cellInfo.setValue(htmlCotent.toString());
		rowItem.getCellElements().add(cellInfo);
		
		Optional.ofNullable(this.subStepList)
			.orElse(Collections.emptyList())
			.stream()
			.map(step -> step.transform(args))
			.collect(Collectors.toList())
			.forEach(c -> {				
				if( null == rowItem.getRowElements()) {
					rowItem.setRowElements(new ArrayList<dhtmlXGridRow>());
				}
				rowItem.getRowElements().add(c);
			});
		
		
		return rowItem;
			
	}
}
