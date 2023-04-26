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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/search_manage.js"></script>

	<script>
		var dataCopy = "${dataCopy}";
		var dataCopyAll = "${dataCopyAll}";
		
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#searchItemGrid").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				
				$('.hdr').css("width","105%");

				$(".searchGridObj").css({"height": + (gridResultHeight - 5) + "px"})
				$('.group_list_wrap').css({"height": + (gridResultHeight - 1) + "px"})
		    }).resize();
		})
	</script>

	<style>
		.searchGridObj{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 810px; */
        }
        /* 권한 그룹 레이어 */
		.group_list_pannel{
			float: left !important;
			clear: none !important;
			width: 255px !important;
			background-color: #ffffff !important;
		}
		/* 권한 관리 레이어 */
		.search_manage_pannel{
			float: left !important;
		    width: calc(100% - 257px) !important;
			clear: none !important;
		    border-left: 1px solid #bbbbbb !important;
		}
		.halfGrid {
			float:left;
			width: 50% !important;
		}
		.halfGridRight {
			float:left;
			width: 33% !important;
		}
		.halfGridMaster{
			width: 40% !important;
			border-right: 2px solid #bbb;
		}
		.halfGridMaster2{
			width: 58.8% !important;
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
            <div class="ui_acrticle group_list_pannel">
                <div class="ui_pannel_row">
                    <div class="ui_float_left">
                       	<p class="ui_pannel_tit ui_sub_bg_flat"><spring:message code="admin.label.authyList"/></p>
                    </div>
                </div>
                <div class="group_list_wrap ui_sub_bg_flat">
                	<div class="group_list">
                		<ul class="group_name"></ul>
                	</div>
                </div>
            </div>
		    <div class="ui_acrticle search_manage_pannel">
	        	<div class="ui_pannel_row">
	            	<div class="ui_float_left">
		                <button id="searchSave" class="ui_sub_btn_flat icon_btn_save_white"><spring:message code="admin.label.saveModify"/></button>
		            </div>
	        	</div>
		        <div class="gridWrap">
			        <div class="halfGrid halfGridMaster">
			        	<div class="halfGrid">
				        	<div id="searchItemBaseGrid" class="searchGridObj"></div>
				        </div>
				        <div class="halfGrid">
				        	<div id="searchItemGrid" class="searchGridObj"></div>
				        </div>
			        </div>
			        <div class="halfGrid halfGridMaster2">
			        	<div class="halfGrid halfGridRight">
			        		<div id="searchListBaseGrid" class="searchGridObj"></div>
			        	</div>
			        	<div class="halfGrid halfGridRight">
			        		<div id="searchListGrid" class="searchGridObj"></div>
			        	</div>
			        	<div class="halfGrid halfGridRight" id="copyListBox">
			        		<div id="searchCopyListGrid" class="searchGridObj"></div>
			        	</div>
			        </div>
		        </div>
	        </div>
	    </div>
    </div>

	<div class="message_area">
	</div>
</body>