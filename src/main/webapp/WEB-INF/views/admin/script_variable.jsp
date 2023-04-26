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
		#codePopup{
		width:400px;
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
                <div class="ui_float_right">
                </div>
          </div>
    	<div class="gridWrap">
		    <div id="codeManageGrid"></div>
    	</div>
    	<div class="gridWrap">
    		<div id="detailManageGrid"></div>
	    </div>
	   </div>
     </div>
     <div id="codePopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.addCode"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.label.code"/></label>
	                        <input maxlength='7' class="inputFilter numberFilter" id="codeInput" type="text"/>
						<label class="ui_label_essential"><spring:message code="admin.label.codeName"/></label>
	                        <input class="inputFilter korFilter engFilter" id="codeNameInput"  type="text"/>
						<label class="ui_label_essential"><spring:message code="admin.label.useYn"/></label>
	                   	  <select id="useYN">
	                   	  	<option value="Y"><spring:message code="admin.detail.option.use"/></option>
	                   	  	<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                   	  </select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addBtn" class="ui_main_btn_flat"><spring:message code="admin.button.approve"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#codePopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>