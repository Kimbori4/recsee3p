package com.furence.recsee.wooribank.script.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.repository.transform.DhtmlRowTransformable;

public enum DhtmlGridDataProvider {
	
	/**
	 * 메인 상품조회 그리드
	 */
	ProductGrid {
		@Override
		public dhtmlXGridXml getHeaders(String[] args) {
			
			if(null == args || 0 == args.length) return null;
			
			String contextPath = args[0];
			
			// 권한별 칼럼 숨김(1) - args로 properties 값 받아오기
			String hiddenColumns = null; // properties에 hidden-columns로 정의된 칼럼들
			String[] hiddenColumnsArray = null; // 칼럼 배열
			if(args.length > 1) {
				if(args[1] != null) {
					hiddenColumns = args[1];
					hiddenColumnsArray = hiddenColumns.split(",");
				}
			}
			
			dhtmlXGridXml xmls = new dhtmlXGridXml();
			// Grid Header 만드는 부분
			xmls.setHeadElement(new dhtmlXGridHead());
		
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
		
			for (int j = 0; j < 21; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");

				switch (j) {
				case 0:
					column.setWidth("40");
					column.setType("ch");
					column.setCache("0");
					column.setSort("na");
					column.setHidden("1");
					column.setFiltering("0");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"
									+ contextPath
									+ "/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");		
					break;
					
				case 1:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsScriptStepFk");
					break;
					
				case 2:
					column.setWidth("40");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "번호" + "</div>");
					break;
					
				case 3:
					column.setWidth("80");
					column.setCache("0");
					column.setSort("server");
					column.setId("rsUseYn");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "판매상태" 
							+ "<span id=\"useHelpMessage\" class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
							+ "<input type=\"hidden\" value=\"판매가능 상품여부 표시(전산정보와 연동)\"></div>");
					break;
					
				case 4:
					column.setWidth("80");
					column.setCache("0");
					column.setSort("str");
					column.setFiltering("0");
					column.setId("rsScriptTypeName");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "상품유형" + "</div>");
					break;
					
				case 5:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setAlign("left");
					column.setId("rsProductName");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "상품명" + "</div>");
					break;
					
				case 6:
					column.setWidth("80");
					column.setCache("0");
					column.setSort("server");
					column.setId("rsGroupYn");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "그룹유무"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"대표코드 아래 개별상품코드의 매핑 유무 표시\"></div>");
					break;
					
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setFiltering("0");
					column.setId("rsGroupCode");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "그룹코드"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"개별상품코드의 대표가 되는 코드(예: 펀드 그룹코드 - G코드)\"></div>");
					break;
					
				case 8:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setFiltering("0");
					column.setId("rsProductCode");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "상품코드" + "</div>");
					break;
					
				case 9:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setFiltering("0");
					column.setId("registeredYN");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "스크립트유무"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"스크립트 등록/미등록에 대한 표시\"></div>");
					break;
	
				case 10:			
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setId("rsTtsUpdateDate");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "TTS 적용일"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"TTS가 최종적으로 만들어진 일자\"></div>");

					break;

				case 11:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setId("rsReservedUpdateDate");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "변경 예정일"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"스크립트 변경 예약이 걸려 있어서 향후 스크립트가 변경되는 날\"></div>");
					break;
					
					
				case 12:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "확인 유무" + "</div>");
					break;
					
				case 13:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("date");
					column.setId("rsLastUpdateDate");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "최종 수정일"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"스크립트가 마지막으로 변경된 날\"></div>");
					break;
					
					
				case 14:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setId("rsLastUpdateUser");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "최종 수정자"
									+ "<span class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"스크립트를 마지막으로 수정한 직원\"></div>");
					break;
					
				case 15:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("str");
					column.setId("rsLastApproveUser");
					column.setFiltering("0");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "결재자"
									+ "<span id=\"approverHelpMessage\" class=\"questionMark\" onmouseover=\"showHeaderHelp(this)\" onmouseout=\"hideHeaderHelp()\"></span>"
									+ "<input type=\"hidden\" value=\"마지막으로 변경된 스크립트에 대해 결재를 한 직원\"></div>");
					break;
					
				case 16:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setId("scriptHistory");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "변경이력보기" + "</div>");
					break;
					
				case 17:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "FK" + "</div>");
					break;
					
				case 18:
					column.setWidth("0");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setId("rsScriptType");
					column.setHidden("1");					
					break;
				
				case 19:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsProductPk");
					break;
					
				case 20:
					column.setWidth("50");
					column.setCache("0");
					column.setId("nowPdfDownload");
					column.setValue("<div style=\"text-align:center; font-weight:bolder;\">" + "PDF" + "</div>");
					break;
				}
				
				head.getColumnElement().add(column);
				
				// 권한별 칼럼 숨김 (2) - column Id가 properties에 hidden-columns로 정의되어 있으면 remove
				if(args.length > 1) {
					if(args[1] != null) {
						if(Arrays.asList(hiddenColumnsArray).contains(column.getId())) {
							head.getColumnElement().remove(column);
						}
					}
				}
				
				column = null;
			}
			
			xmls.setHeadElement(head);
			
			return xmls;
		}
		
	},
	
	/**
	 * 상품별 스크립트 스텝 그리드
	 */
	ScriptStepGrid {
		@Override
		public dhtmlXGridXml getHeaders(String[] args) {
			dhtmlXGridXml xmls = new dhtmlXGridXml();
			// Grid Header 만드는 부분
			xmls.setHeadElement(new dhtmlXGridHead());
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
			
			for (int j = 0; j < 6; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
				column.setWidth("*");
				column.setAlign("left");
				column.setType("ro");
				column.setSort("server");
				column.setHidden("1");

				switch (j) {
				case 0:
					column.setType("tree");
					column.setWidth("270");
					column.setId("scriptName");
					column.setHidden("0");
					break;
				case 1:
					column.setId("rScriptStepPk");
					break;
				case 2:
					column.setId("rScriptStepParent");
					break;
				case 3:
					column.setId("rScriptStepFk");
					break;
				case 4:
					column.setId("changeScriptName");
					break;
				case 5:
					column.setId("addScriptStepRowLank");
					column.setWidth("120");
					column.setHidden("0");
					break;


				}
				head.getColumnElement().add(column);
			}
			
			xmls.setHeadElement(head);
			
			return xmls;
		}
		
	},
	
	/**
	 * 공통 스크립트 그리드
	 */
	CommonScriptGrid {
		@Override
		public dhtmlXGridXml getHeaders(String[] args) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	
	/**s
	 * 스크립트 결재 그리드
	 */
	ScriptApprovalGrid {
		@Override
		public dhtmlXGridXml getHeaders(String[] args) {
			
			if(null == args || 0 == args.length) return null;
				
			dhtmlXGridXml xmls = new dhtmlXGridXml();
			// Grid Header 만드는 부분
			xmls.setHeadElement(new dhtmlXGridHead());
		
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
		
			for (int j = 0; j < 15; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
		
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
		
				switch (j) {
				case 0:
					column.setCache("0");
					column.setHidden("1");
					column.setId("transactionId");
					column.setValue("transactionId");
					break;
				case 1:
					column.setWidth("40");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("index");
					column.setValue("번호");
					break;
				case 2:
					column.setWidth("150");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptCommonYN");
					column.setValue("스크립트종류");
					break;
				case 3:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setAlign("left");
					column.setId("rsScriptTypeDesc");
					column.setValue("대표상품명/스크립트설명");
					break;
				case 4:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsScriptType");
					column.setValue("스크립트타입");
					break;
				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptTypeName");
					column.setValue("스크립트유형");
					break;
				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsEditDate");
					column.setValue("결재의뢰일");
					break;
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsConfirmYN");
					column.setValue("결재상태");
					break;
				case 8:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsEditUser");
					column.setValue("결재의뢰자");
					break;
				case 9:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsConfirmer");
					column.setValue("결재자");
					break;
				case 10:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsApproveDate");
					column.setValue("결재일");
					break;
				case 11:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsApplyDate");
					column.setValue("적용예정일");
					break;
				case 12:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsCommonKind");
					column.setValue("공용스크립트여부");
					break;
				case 13:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsApplyName");
					column.setValue("즉시적용여부");
					break;
				case 14:
					column.setCache("0");
					column.setHidden("1");
					column.setId("productPk");
					column.setValue("productPk");
					break;
				}
				head.getColumnElement().add(column);
				
				if(args[1]!=null && column.getId()!=null) {
					
					// 결재의뢰 상태에서 결재일,결재자 칼럼 표출x
					if(args[1].equals("request") 
							&& (column.getId().equals("rsConfirmer") || column.getId().equals("rsApproveDate"))) {
						head.getColumnElement().remove(column);
					}else if(args[1].equals("reject")
							&& column.getId().equals("rsApplyDate")) { // 반려 상태에서 적용일 칼럼 표출 x
						column.setHidden("1");
					}
				}
				
				column = null;
			}
			
			xmls.setHeadElement(head);
			
			return xmls;
		}
		
	},
	
	/**s
	 * 스크립트 상신목록 조회 그리드
	 */
	ScriptApprovalResultGrid {
		@Override
		public dhtmlXGridXml getHeaders(String[] args) {
			
			if(null == args || 0 == args.length) return null;
				
			dhtmlXGridXml xmls = new dhtmlXGridXml();
			// Grid Header 만드는 부분
			xmls.setHeadElement(new dhtmlXGridHead());
		
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
		
			for (int j = 0; j < 15; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
		
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
		
				switch (j) {
				case 0:
					column.setCache("0");
					column.setHidden("1");
					column.setId("transactionId");
					break;
				case 1:
					column.setWidth("40");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("index");
					column.setValue("번호");
					break;
				case 2:
					column.setWidth("150");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptCommonYN");
					column.setValue("스크립트종류");
					break;
				case 3:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setAlign("left");
					column.setId("rsScriptTypeDesc");
					column.setValue("대표상품명/스크립트설명");
					break;
				case 4:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsScriptType");
					break;
				case 5:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptTypeName");
					column.setValue("스크립트유형");
					break;
				case 6:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsEditDate");
					column.setValue("결재의뢰일");
					break;	
				case 7:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsConfirmYN");
					column.setValue("결재상태");
					break;
				case 8:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsEditUser");
					column.setValue("결재의뢰자");
					break;
				case 9:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsConfirmer");
					column.setValue("결재자");
					break;
				case 10:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsApproveDate");
					column.setValue("결재일");
					break;
				case 11:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsApplyDate");
					column.setValue("적용(예약)일");
					break;
				case 12:
					column.setCache("0");
					column.setHidden("1");
					column.setId("rsCommonKind");
					break;
				case 13:
					column.setCache("0");
					column.setHidden("1");
					column.setId("productPk");
					break;
				case 14:
					column.setWidth("100");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("approveRestart");
					column.setValue("재의뢰");
					break;
				}
				head.getColumnElement().add(column);
				
				if(args[1]!=null && column.getId()!=null) {
					// 결재의뢰 상태에서 결재일,결재자 칼럼 표출x
					if(args[1].equals("request") 
							&& (column.getId().equals("rsConfirmer") || column.getId().equals("rsApproveDate"))) {
						head.getColumnElement().remove(column);
					}
					
					// 결재의뢰 or 결재완료 상태에서 재상신 칼럼 표출x
					if((args[1].equals("request") || args[1].equals("approve")) 
							&& column.getId().equals("approveRestart")) {
						head.getColumnElement().remove(column);
					}
					
					// 취소 or 반려 상태에서 적용일 칼럼 표출 x
					if((args[1].equals("reject") || args[1].equals("cancel")) 
							&& column.getId().equals("rsApplyDate")) {
						head.getColumnElement().remove(column);
					}
				}
				
				column = null;
			}
			
			xmls.setHeadElement(head);
			
			return xmls;
		}
		
	},
	/**
	 * 스크립트 상품 복사 그리드
	 */
	ScriptCopyGrid {
		@Override
		public dhtmlXGridXml getHeaders(String[] args) {
			
			if(null == args || 0 == args.length) return null;
			
			String contextPath = args[0];
				
			dhtmlXGridXml xmls = new dhtmlXGridXml();
			// Grid Header 만드는 부분
			xmls.setHeadElement(new dhtmlXGridHead());
		
			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());
		
			for (int j = 0; j < 6; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();
		
				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("1");
				column.setEditable("0");
				column.setCache("1");
		
				switch (j) {
				case 0:
					column.setWidth("40");
					column.setType("ch");
					column.setCache("0");
					column.setSort("na");
					column.setFiltering("0");
					column.setValue("<div id='allcheck' style=\"cursor: pointer; position: relative; top: 2px\"><img src='"
									+ contextPath
									+ "/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif' /></div>");
					break;
				case 1:
					column.setWidth("40");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setValue("번호");
					break;
				case 2:
					column.setWidth("400");
					column.setCache("0");
					column.setSort("str");
					column.setFiltering("0");
					column.setAlign("left");
					column.setId("rsProductName");
					column.setValue("상품명");
					break;
				case 3:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptTypeName");
					column.setValue("상품유형");
					break;
				case 4:
					column.setWidth("*");
					column.setCache("0");
					column.setSort("server");
					column.setFiltering("0");
					column.setId("rsScriptGroupName");
					column.setValue("대표상품코드");
					break;
				case 5:
					column.setWidth("*");
					column.setCache("0");
					column.setFiltering("0");
					column.setSort("server");
					column.setHidden("1");
					column.setId("rsProductListPk");
					column.setValue("rsProductListPk");
					break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			
			xmls.setHeadElement(head);
			
			return xmls;
		}

		
	};
	
	
	public abstract dhtmlXGridXml getHeaders(String[] args);	
	
	public dhtmlXGridXml getRows(Collection<? extends DhtmlRowTransformable> list , String[] args) {
		dhtmlXGridXml xmls = new dhtmlXGridXml();
		List<dhtmlXGridRow> rows = list.stream().map(vo->vo.transform(args)).collect(Collectors.toList());
		xmls.setRowElements(rows);			
		return xmls;
	}
}
