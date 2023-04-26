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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/user_manage.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/authy_manage.js"></script>
	<script>
		var locale = "${locale}"
	</script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#authyGrid").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = documentHeight;

				// 사이드 권한리스트 및 그리드 리사이즈
				if(tabMode=="Y"){
					$("#authyGrid").css({"height": + (gridResultHeight - 50) + "px"});
					$(".group_list_wrap").css({"height": + (gridResultHeight - 89) + "px"});
				}else{
					$("#authyGrid").css({"height": + (gridResultHeight - 138) + "px"});
					$(".group_list_wrap").css({"height": + (gridResultHeight - 174) + "px"});
				}
				
				
		    }).resize();
		})
	</script>

	<style>
        #authyGrid{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
	</style>
</head>
<body onload="userManageLoad()">
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
			<div class="ui_acrticle group_manage_pannel">
			    <div class="ui_pannel_row">
			        <div class="ui_float_left">
						<button id="authyAddBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.group.add"/></button>
						<button id="authyModifyBtn" class="ui_btn_white icon_btn_pen_gray"><spring:message code="admin.button.addModi"/></button>
						<button id="authyDeleteBtn" class="ui_btn_white icon_btn_trash_gray"></button>
			        </div>
			    </div>
			    <div class="group_list_wrap ui_sub_bg_flat">
			    	<div class="group_list">
			    		<ul class="group_name"></ul>
			    	</div>
			    </div>
			</div>

			<div class="ui_acrticle juri_manage_pannel">
			    <div class="ui_pannel_row">
			        <div class="ui_float_left">
			            </div>
			            <div class="ui_float_right">
			            </div>
			        </div>
				<div class="gridWrap">
					<div id="authyGrid"></div>
				</div>
			</div>
        </div>
    </div>

	<div class="message_area">
	</div>
</body>