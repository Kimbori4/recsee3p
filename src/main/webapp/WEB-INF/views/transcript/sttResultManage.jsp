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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/search/search.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/transcript/sttResultManage.js"></script>
	<script>
	$(function() {
	    $(window).resize(function() {
	    	// 현재 document 높이
			var documentHeight = $(window).height();
	    	var documentWidth  = $(window).width();

			// 그리드 위의 높이 값
			var gridCalcHeight = $("#gridSttResult").offset().top;
			
			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight - 2);
			
			$("#gridSttResult").css({"height": + gridResultHeight + "px"});
	    }).resize();
	})
	</script>
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
	    <div class="main_contents">
        <div class="ui_layout_pannel">
        	<div class="main_pannel">
				<div class="ui_article">
					<div class="ui_pannel_row">
						<div class="ui_float_left">
							<div class="main_form">
								
							</div>
						</div>
					</div>
				</div>
				<div class="gridWrap">
					<div id="gridSttResult" style="width:100%"></div>
					<div id="pagingSttResult"></div>
				</div>
			</div>
		</div>
    	</div>
    </div>
	<input type="hidden" id ="ip" value="${ip}">
    <input type="hidden" id ="port" value="${port}">
    <input type="hidden" id ="delYn" value="${delYn}">
</body>
