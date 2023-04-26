package com.furence.recsee.wooribank.script.param.response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridRowUserdata;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.repository.transform.DhtmlRowTransformable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MergedApprove extends DhtmlRowTransformable{
	
	private int rowNumber;
	
	/*
	 * 트랜잭션 아이디
	 */
	private String trasactionId;
	
	/*
	 * productPk
	 */
	private String productPk;
	
	/*
	 * 공통스크립트 여부
	 */
	@JsonProperty("rsScriptCommonYN")
	private Const.Bool commonYN;
	
	/*
	 * 공용문구 이름
	 */
	@JsonProperty("rsScriptCommonName")
	private String scriptCommonName;
	
	/*
	 * 공통:스크립트 설명 / 상품:상품명
	 */
	@JsonProperty("rsScriptDesc")
	private String scriptDesc;
	
	/*
	 * 스크립트 타입(코드)
	 */
	@JsonProperty("rsScriptType")
	private String scriptType;
	
	/*
	 * 스크립트 타입(이름)
	 */
	@JsonProperty("rsScriptTypeName")
	private String scriptTypeName;
	
	/*
	 * 등록일(상신일)
	 */
	@JsonProperty("rsEditDate")
	@JsonFormat(shape = Shape.STRING,
				pattern = "yyyy-MM-dd" ,
				timezone = "Asia/Seoul")
	private Date registDate;
	
	/*
	 * 등록자(상신자)
	 */
	@JsonProperty("rsEditUser")
	private String editUser;
	
	/*
	 * 결재상태
	 */
	@JsonProperty("rsApproveStatus")
	private Const.Approve approveStatus;
	
	/*
	 * 결재자
	 */
	@JsonProperty("rsApproveUser")
	private String approveUser;
	
	/*
	 * 결재일
	 */
	@JsonProperty("rsApproveDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date approveDate;
	
	/*
	 * 적용예정일
	 */
	@JsonProperty("rsApplyDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date applyDate;
	
	/*
	 * 적용타입
	 */
	@JsonProperty("rsApplyType")
	private String applyType;
	
	/*
	 * 적용타입
	 */
	@JsonProperty("rsApplyName")
	private String applyName;

	/*
	 * 현재 버전의 수정건인지 여부
	 */
	@JsonProperty("rsIsLast")
	private Const.Bool lastYN;
	
	@Override
	public dhtmlXGridRow transform(String[] args) {
		
		boolean isApprover = false;
		if ( args != null && args.length > 0 ) {
			isApprover = Boolean.valueOf(args[0]);
		}
		
		SimpleDateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		
		dhtmlXGridRow rowItem = new dhtmlXGridRow();
		
		rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
		rowItem.setUserdataElements(new ArrayList<dhtmlXGridRowUserdata>());
		rowItem.setRowClass("searchApproveGrid");
		rowItem.setId( this.trasactionId );
		
		for (int i = 0; i < 15; i++) {
			
			dhtmlXGridRowCell cellInfo = new dhtmlXGridRowCell();
			
			switch (i) {
			case 0: // transactionID
				cellInfo.setName("transactionId");
				cellInfo.setValue("" + this.trasactionId);
				break;
			case 1: // 번호
				cellInfo.setName("index");
				cellInfo.setValue(this.rowNumber + "");
				break;
			case 2: // 공용스크립트 여부
				cellInfo.setName("rsScriptCommonYN");
				cellInfo.setValue(this.commonYN == Const.Bool.YES ? "공용" : "상품");
				break;
			case 3: // 대표상품명 or 공용스크립트이름+설명
				cellInfo.setName("rsScriptTypeDesc");
				cellInfo.setValue(this.commonYN == Const.Bool.YES ? "[" + this.scriptCommonName + "] " + this.scriptDesc : this.scriptDesc);
				break;
			case 4: // 스크립트유형(코드)
				cellInfo.setName("rsScriptType");
				cellInfo.setValue(this.scriptType);	
				break;
			case 5: // 스크립트유형(이름)
				cellInfo.setName("rsScriptTypeName");
				cellInfo.setValue(this.scriptTypeName);
				break;
			case 6: // 결재의뢰일
				cellInfo.setName("rsEditDate");
				cellInfo.setValue( this.registDate == null ? "" : dfmt.format(this.registDate));
				break;
			case 7: // 결재상태
				cellInfo.setName("rsConfirmYN");
				String approveName = null == this.approveStatus ? "-" : this.approveStatus.getKorName();
				if(approveName.equals("결재완료")) {
					cellInfo.setValue("<span style='color:blue'>"+approveName+"</span>");
				}
				if(approveName.equals("반려")) {
					cellInfo.setValue("<span style='color:red'>"+approveName+"</span>");
				}
				if(approveName.equals("취소")) {
					cellInfo.setValue("<span style='color:grey'>"+approveName+"</span>");
				}
				if(approveName.equals("결재의뢰")) {
					cellInfo.setValue(approveName);
				}
				break;
			case 8: // 결재의뢰자
				cellInfo.setName("rsEditUser");
				cellInfo.setValue(this.editUser);
				break;
			case 9: // 결재자
				cellInfo.setName("rsConfirmer");
				cellInfo.setValue(this.approveUser);
				break;
			case 10: // 결재일
				cellInfo.setName("rsApproveDate");
				cellInfo.setValue( this.approveDate == null ? "" : dfmt.format(this.approveDate));
				break;
			case 11: // 적용예정일
				cellInfo.setName("rsApplyDate");
				cellInfo.setValue( this.applyDate == null ? "즉시적용" : dfmt.format(this.applyDate));
				break;
			case 12: // 공용스크립트여부
				cellInfo.setName("rsCommonKind");
				cellInfo.setValue( this.commonYN.getValue());
				break;
			case 13:
				if(isApprover) { // 익일적용 or 즉시적용 or 예약일적용
					cellInfo.setName("rsApplyName");
					cellInfo.setValue( this.applyName );
				}else { // prodcutPk
					cellInfo.setName("productPk");
					cellInfo.setValue( this.productPk );
				}
				break;
			case 14:
				if(isApprover) { // productPk
					cellInfo.setName("productPk");
					cellInfo.setValue( this.productPk );
				}else { // 재상신 버튼
					cellInfo.setName("approveRestart");
					if(this.commonYN == Const.Bool.NO && this.lastYN == Const.Bool.YES ) {
						cellInfo.setValue("<button class='restartBtn' onclick='restartApproval("+"\""+this.getProductPk()+"\""+","+"\""+this.getTrasactionId()+"\""+")'>재의뢰</button>");
					}
				}
				break;
			}
			
			rowItem.getCellElements().add(cellInfo);
			
			if(args[1]!=null && cellInfo.getName()!=null) {
				// 결재의뢰 상태에서는 결재자, 결재일 cell 지우기
				if(args[1].equals("request")
						&& ( cellInfo.getName().equals("rsConfirmer") || cellInfo.getName().equals("rsApproveDate")) ) {
					rowItem.getCellElements().remove(cellInfo);
				}
				// 상신목록 - 취소,반려 상태에서는 적용예정일 지우기
				if(!isApprover
						&& (args[1].equals("reject") || args[1].equals("cancel")) 
						&& cellInfo.getName().equals("rsApplyDate")) {
					rowItem.getCellElements().remove(cellInfo);
				}
			}
			
		}

		return rowItem;
		
	}
}
