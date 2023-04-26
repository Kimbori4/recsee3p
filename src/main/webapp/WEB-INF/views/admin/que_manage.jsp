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

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/que_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridQueManage").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#gridQueManage").css({"height": + (gridResultHeight - 4) + "px"})
		    }).resize();
		})
	</script>

	<style>
		#gridQueManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
        /* layer popup */
        #addQue{
            width: 400px;
        }
        #exelUpload{
            width: 400px;
        }
	</style>
</head>
<body>
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

	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
		                <button class="ui_main_btn_flat icon_btn_traceTxt_white" onclick="layer_popup('#addQue');"><spring:message code="admin.label.addQue"/></button>
		                <button id="rQueueDel" class="ui_btn_white icon_btn_trash_gray"></button>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridQueManage"></div>
			        <div id="pagingQueManage"></div>
		        </div>
	        </div>

        </div>
    </div>


    <div id="addQue" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.addQue"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.label.queNum"/></label>
                        	<input class="" id="rQueueNum" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.label.queName"/></label>
                        	<input class="" id="rQueueName" value="" type="text"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button class="ui_main_btn_flat icon_btn_traceTxt_white" id="rQueueAdd"><spring:message code="admin.label.addQue"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div class="message_area">
	</div>
</body>