<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/combo_manage.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/combo_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 메뉴 위의 높이 값
				var calcHeight = $('.admin_lnb').offset().top;
				var resultHeight = (documentHeight - calcHeight);

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#codeManageGrid").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#codeManageGrid").css({"height": + (gridResultHeight-5) + "px"})
				$("#detailManageGrid").css({"height": + (gridResultHeight-5) + "px"})

		    }).resize();
		})
	</script>

	<style>
		.gridWrap{
			float:left;
			clear:none !important;
			width:50%;
		}

        #modifyConfig{
        	width: 400px;
        }
        #InsertConfig{
        	width: 400px;
        }
        .ui_label_essential{
       	    width: 80px !important;
        }
		.modifyBox{
			width: calc(100% - 0px) !important;
		}    
		.dateClass{
			display: none;
		}
		.insertBox{
			width: calc(100% - 47px) !important;
		}
		#insertSearch{
			background-repeat: no-repeat;
		    background-size: 18px;
		    background-position: 8px center;
		    padding-left: 26px !important;
		    margin: 0;
		    margin-left: 5px;
		    height: 33px;
		}
		.insertSelectClass{
			width: calc(100%) !important;
		}
		.productDelete{
			background-color: rgb(96, 179, 220) !important;
		    border-color: rgb(96, 179, 220) !important;
		    color: rgb(255, 255, 255) !important;
		    cursor: pointer !important;
		    width: 63px !important;
		    transition: all 0.3s ease 0s !important;
		    height: 31px !important;
		    text-align: center !important;
		    padding: 0px 0px 0px 0px !important;
		}
	</style>
</head>
<body onload="comboLoad();">
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
	<div class="ui_layout_pannel">
		<%@ include file="./admin_menu.jsp" %>
		<div class="main_contents admin_body">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <div class="main_form">
	                        <select id="groupKey" required style="display:none;">
	                        	<option value='all'>전체</option>
	                        	<option value='2'>펀드</option>
	                        	<option value='1'>신탁</option>
	                        </select>
	                        <select id="configKey" required style="width:250px !important;">
	                        	<option value='p_name' selected>상품명</option>
	                        	<option value='p_code'>상품코드</option>
	                        </select>
	                        <input id="configValue" type="text" style="width:300px !important;" placeholder=""/>
	                       	<button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white" onclick="searchValue()"><spring:message code="admin.label.search"/></button>

	                    	<%-- <button id="addPopBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.add"/></button> --%>
	                    </div>
	                </div>
                   	<div style="float: right;">
                       	<button id="modifyBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="VariableInsertPopUp()" style="float:right;margin-right: 21px">추가</button>
                       	<button id="modifyBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="SinkInsert()" style="float:right;margin-right: 21px; display:none;">싱크추가</button>
                   	</div>
	            </div>
			<div class="gridWrap" style="width: 100%">
				<div id="codeManageGrid" style="width: 100%"></div>
				<div id="codeManagePaging"></div>
			</div>
		</div>
	</div>
	<div id="modifyConfig" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" onclick="inputClear2()"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap" style="overflow:auto;">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<input type="hidden" id="valuePk" value="">
                    	<label class="ui_label_essential">스텝 구간명</label>
                        	<select class="insertSelectClass" id="rs_script_step_name" style="width: 88% !important;">
     	                  	</select>
                        	<button id="insertSearch" class="ui_main_btn_flat icon_btn_search_white" onclick="searchScriptStep()"></button>
                    	<label class="ui_label_essential">스크립트 내용 선택</label>
                        	<select class="insertSelectClass" id="rs_script_step_detail_pk" onchange= "detailListChange()" disabled="disabled">
     	                  	</select>
     	                <label class="ui_label_essential">스크립트 내용</label>
     	                <textarea id="rs_script_step_detail_text" style="height:34.09px; width:380px;" readonly="readonly"></textarea>
                    	<label class="ui_label_essential">발화타입</label>
                        	<select class="insertSelectClass" id="rs_script_step_detail_type" disabled="disabled">
	                       		<option value="T">TTS</option>
	                       		<option value="S">상담사</option>
	                       		<option value="R">투자성향 선택지</option>
	                       		<option value="G">고객</option>
     	                  	</select>
                        	<label class="ui_label_essential">조건 사용여부</label>
	                        	<select class="insertSelectClass" id="rs_script_step_detail_if_case" onchange="detailIfCaseChange()" disabled="disabled">
		                       		<option value="Y">Y</option>
		                       		<option value="N">N</option>
	     	                  	</select>
                        	<label class="ui_label_essential">조건 내용</label>
                        	<input class="modifyBox" id="rs_script_step_detail_if_case_detail" type="text" disabled="disabled"/>
                    	<label class="ui_label_essential">사용여부(rs_use_yn)</label>
                        	<select class="insertSelectClass" id="rs_use_yn2" disabled="disabled">
	                       		<option value="Y">Y</option>
	                       		<option value="N">N</option>
     	                  	</select>
                    	<label class="ui_label_essential">조건코드</label>
                    		<input class="modifyBox" id="rs_script_step_detail_if_case_code" type="text" disabled="disabled" placeholder="if_case_code"/>
                    	<label class="ui_label_essential">조건코드상세</label>
                        	<input class="modifyBox" id="rs_script_step_detail_if_case_detail_code" type="text" disabled="disabled" placeholder="if_case_detail_code"/>
               	         <label class="ui_label_essential">ELT조건</label>
                        	<input class="modifyBox" id="rs_script_step_detail_elt_case" type="text" disabled="disabled" placeholder="detail_elt_case"/>
                        	
                       	<label class="ui_label_essential">상품속성</label>
                        	<input class="modifyBox" id="rs_product_attributes2" type="text" disabled="disabled" placeholder="product_attributes (JSON)"/>
                       	<label class="ui_label_essential">상품속성 확장</label>
                        	<input class="modifyBox" id="rs_product_attributes_ext" type="text" disabled="disabled"  placeholder="product_attributes_ext (JSON)"/>
                       	<label class="ui_label_essential">상품조건속성</label>
                        	<input class="modifyBox" id="rs_script_step_detail_case_attributes" type="text" disabled="disabled" placeholder="detail_case_attributes (JSON)"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<button id="addBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.save"/></button>
                        <button id="modifyBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="VariableUpdate()"><spring:message code="message.btn.modify"/></button>
                        <button id="delBtn" class="ui_btn_white icon_btn_trash_gray"></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div id="InsertConfig" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" id="insertExit" onclick="inputClear3()"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<input type="hidden" id="valuePk" value="">
                       	<label class="ui_label_essential">타입</label>
                       	<select class="insertSelectClass" id="rs_product_type">
                       		<option value="1">신탁</option>
                       		<option value="2">펀드</option>
                       		<option value="3">공용</option>
                       		<option value="4">방카</option>
                       		<option value="5">퇴직연금</option>                       		
                       	</select>
                    	<label class="ui_label_essential">상품코드</label>
                        	<input class="insertBox" id="rs_product_code" type="text">
                        	<label class="ui_label_essential">상품명</label>
                        	<input class="modifyBox" id="rs_product_name" type="text">
                    	<label class="ui_label_essential">사용 여부(rs_use_yn)</label>
                        	<select class="insertSelectClass" id="rs_use_yn">
	                       		<option value="Y">Y</option>
	                       		<option value="N">N</option>
     	                  	</select>
                    	<label class="ui_label_essential">그룹 여부(rs_group_yn)</label>
                        	<select class="insertSelectClass" id="rs_group_yn">
	                       		<option value="Y">Y</option>
	                       		<option value="N">N</option>
     	                  	</select>
                    	<label class="ui_label_essential">대표 코드(rs_group_code)</label>
                        	<input class="modifyBox" id="rs_group_code" type="text"/>
                    	<label class="ui_label_essential">(rs_sysdis_type)</label>
                    		<select class="insertSelectClass" id="rs_sysdis_type">
	                       		<option value="WMS">WMS</option>
	                       		<option value="TOP">TOP</option>
	                       		<option value="SHT">SHT</option>
	                       		<option value="BK2">BK2</option>
     	                  	</select>
                    	<label class="ui_label_essential">(rs_product_attributes)</label>
                    		<input class="modifyBox" id="rs_product_attributes" type="text"/>	
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                           <button id="insertBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="adminProductInsert()">추가</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div id="InsertSink" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" id="insertExit" onclick="inputClear3()"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<input type="hidden" id="valuePk" value="">
                       	<label class="ui_label_essential">타입</label>
                       	<select class="insertSelectClass" id="sink_type">
                       		<option value="1">신탁</option>
                       		<option value="2">펀드</option>
                       		<option value="3">공용</option>
                       		<option value="4">방카</option>
                       		<option value="5">퇴직연금</option>                       		
                       	</select>
                    	<label class="ui_label_essential">가변코드</label>
                        	<input class="insertBox" id="r_col_cd" type="text">
                        	<label class="ui_label_essential">가변명</label>
                        	<input class="modifyBox" id="r_col_nm" type="text">
                    	<label class="ui_label_essential">순서</label>
                        	<input class="modifyBox" id="r_order" type="text"/>
                    	<label class="ui_label_essential">상품속성</label>
                    		<select class="insertSelectClass" id="r_prd_attr_yn">
	                       		<option value="Y">Y</option>
	                       		<option value="N">N</option>
	                       		<option value="F">F</option>
	                       		<option value="B">B</option>
     	                  	</select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="insertBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="SinkInsertAction()" style="" >추가</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>