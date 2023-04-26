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
	                        <select id="groupKey" required>
	                        	<option value='all'>전체</option>
	                        	<option value='2'>펀드</option>
	                        	<option value='1'>신탁</option>
	                        </select>
	                        <select id="configKey" required style="width:250px !important;">
	                        	<option value='all'>전체</option>
	                        	<option value='p_name'>상품명</option>
	                        	<option value='p_code'>상품코드</option>
	                        </select>
	                        <input id="configValue" type="text" style="width:300px !important;" placeholder=""/>
	                       	<button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white" onclick="searchValue()"><spring:message code="admin.label.search"/></button>

	                    	<%-- <button id="addPopBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.add"/></button> --%>
	                    </div>
	                </div>
                   	<div style="float: right;">
                       	<button id="modifyBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="VariableInsertPopUp()" style="float:right;margin-right: 21px">추가</button>
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
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<input type="hidden" id="valuePk" value="">
                    	<label class="ui_label_essential">상품코드</label>
                        	<input class="modifyBox" id="productCode" type="text" readonly="readonly" disabled="disabled"/>
                        	<label class="ui_label_essential">타입</label>
                        	<input class="modifyBox" id="productType" type="text" readonly="readonly" disabled="disabled"/>
                        	<label class="ui_label_essential">상품명</label>
                        	<input class="modifyBox" id="productName" type="text"/>
                    	<label class="ui_label_essential">가변코드</label>
                        	<input class="modifyBox" id="valueCode" type="text" readonly="readonly" disabled="disabled" />
                    	<label class="ui_label_essential">가변명</label>
                        	<input class="modifyBox" id="valueName" type="text"/>
                    	<label class="ui_label_essential">가변값</label>
                        	<input class="modifyBox" id="valueVal" type="text"/>
                    	<label class="ui_label_essential">반영종류</label>
                    		<select id="modifyType" class="modifyBox" onchange="typeChange()">
                    			<option value="1">즉시반영</option>
                    			<option value="2">예약반영</option>
                    		</select>
                        <div class="dateClass">
                    	<label class="ui_label_essential">반영날짜</label>
                        	<input class="modifyBox" id="reserveDate" type="date" onchange="dateChange(this)"/>
                        </div>
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
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" id="insertExit" onclick="inputClear()"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<input type="hidden" id="valuePk" value="">
                       	<label class="ui_label_essential">타입</label>
                       	<select class="insertSelectClass" id="insertSelectOption">
                       		<option value="1">신탁</option>
                       		<option value="2">펀드</option>
                       		<option value="3">투자성향분석</option>
                       	</select>
                    	<label class="ui_label_essential">상품코드</label>
                        	<input class="insertBox" id="insertProductCode" type="text">
                        	<button id="insertSearch" class="ui_main_btn_flat icon_btn_search_white" onclick="searchProductCode()"></button>
                        	<label class="ui_label_essential">상품명</label>
                        	<input class="modifyBox" id="insertProductName" type="text" readonly="readonly" disabled="disabled"/>
                    	<label class="ui_label_essential">가변코드</label>
                        	<input class="modifyBox" id="insertValueCode" type="text" disabled="disabled"/>
                    	<label class="ui_label_essential">가변명</label>
                        	<input class="modifyBox" id="insertValueName" type="text" disabled="disabled"/>
                    	<label class="ui_label_essential">가변값</label>
                        	<input class="modifyBox" id="insertValueVal" type="text" disabled="disabled"/>
                    	<label class="ui_label_essential">실시간</label>
                        	<select class="insertSelectClass" id="insertrealTime" disabled="disabled">
                       			<option value="Y">실시간</option>
                       			<option value="N">비 실시간</option>
                       	</select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="insertBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="VariableInsert()" style="opacity : 0.4;pointer-events : none;" >추가</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>