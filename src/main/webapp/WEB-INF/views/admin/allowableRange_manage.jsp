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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/allowableRange_manage.js"></script>
	<script>
	$(function() {
	    $(window).resize(function() {
	    	// 현재 document 높이
			var documentHeight = $(window).height();

			// 그리드 위의 높이 값
			var gridCalcHeight = $("#userManageGrid").offset().top;

			// 페이징이 있음 페이징 만큼 뺴주깅
			var gridResultHeight = documentHeight;

			// 탭 전환 등의 시점 때문에 임의로 계산
			if(tabMode=="Y"){
				$(".group_list_wrap").css({"height": + (gridResultHeight - 52) + "px"});
				$("#userManageGrid").css({"height": + (gridResultHeight - 53) + "px"});
				$("#treeViewAgent").css({"height": + (gridResultHeight - 53) + "px"});
			} else{
				$(".group_list_wrap").css({"height": + (gridResultHeight - 141) + "px"});
				$("#userManageGrid").css({"height": + (gridResultHeight - 142) + "px"});
				$("#treeViewAgent").css({"height": + (gridResultHeight - 142) + "px"});
			}
			
	    }).resize();
	})
	</script>


	<style>
	
		.group_tree_pannel{
			border-left: 1px solid #bbbbbb;
		}
		.tree_view_wrap{
			margin-top: 48px;
		}
        #authyGrid{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
	</style>
</head>
<body onload="allowManageLoad()">
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
						<button id="allowableAddBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.allowableRange.button.rangeAdd"/></button>
						<button id="allowableModifyBtn" class="ui_btn_white icon_btn_pen_gray"><spring:message code="admin.allowableRange.button.rangeModi"/></button>
						<button id="allowableDeleteBtn" class="ui_btn_white icon_btn_trash_gray"></button>
			        </div>
			    </div>
			    <div class="group_list_wrap ui_sub_bg_flat">
			    	<div class="group_list">
			    		<ul class="allowable_name">
			    		</ul>
			    	</div>
			    </div>
			</div>
			<div class="ui_acrticle group_tree_pannel">
				<div class="ui_pannel_row">
			        <div class="ui_float_left">
						<button id= "rangeAddBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.allowableRange.button.groupAdd"/></button>
					</div>
		            <div class="ui_float_right">
		            </div>
		        </div>
	       		<div class="tree_view_wrap">
		            <div class="tree_agent_view" id="treeViewAgent"></div>
				</div>
			</div>
			<div class="ui_acrticle allowable_manage_pannel">
			    <div class="ui_pannel_row">
			        <div class="ui_float_left">
						<button id="rangeSaveBtn" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="admin.button.save"/></button>
						<button id="checkedRangeDeleteBtn" class="ui_btn_white icon_btn_trash_gray"></button>
					</div>
		            <div class="ui_float_right">
		            </div>
		        </div>
				<div class="gridWrap">
					<div id="userManageGrid"></div>
				</div>
			</div>
        </div>
    </div>

	<div class="message_area">
	</div>
</body>