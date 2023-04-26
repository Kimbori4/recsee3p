<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../common/include/commonVar.jsp" %>

<!DOCTYPE html>
<html>
<head>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/statistics/statistics.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/statistics/font-awsome.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/statistics/dashboard.js"></script>

	<script>
		$(function(){
			top.playerVisible(false);
			ui_controller();
			 $(window).resize(function() {
		          // 현재 document 높이
		          var documentHeight = $(window).height();
		          // 그리드 위의 높이 값
		          var calcHeight = $('#checkHeight').offset().top;

		          var resultHeight = (documentHeight - calcHeight-100);

		          // height 값 문서 크기에 맞추기
		          $('.chartWrap').css({"height": + (resultHeight/2) + "px"});
		    }).resize();
			
		})
	</script>

    <style>
    	#chartDaily, #chartRecfile, #chartWeekly, #chartStorage{
    		width: 100%;
    		height: 100%;
    		float: left;
    	}
    	.chartWrap{
    	    border-bottom: 1px solid #dddddd;
    	}
    	.chartWrap_harf{
		    float: left;
		    width: 45%;
		    height: 100%;
		    margin-left: 3%;
		    margin-bottom: 25px;
    	}
    	.chartWrap_harf:last-child{
    		border-right: 0px;
    	}
    	.chartWrap svg {
			width:100%;
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
    <div class="main_contents">
        <div class="ui_layout_pannel">
        	<div class="ui_article">
	            <div class="recordInfoWrap ui_main_bg_flat">
	            	<div class="report_txt_wrap">
						<p class="icon_record_val recordInfo_tit"><spring:message code="statistics.title.allCount" /></p>
						<p class="icon_record_time recordInfo_txt" id="allCalls"></p>
					</div>
					<div class="report_txt_wrap">
						<p class="recordInfo_tit"><spring:message code="statistics.title.allHour" /></p>
						<p class="recordInfo_txt" id="allTime"></p>
					</div>
	            </div>
				<div class="chartWrap" id='checkHeight'>
					<div class="chartWrap_harf">
						<div id="chartDaily"></div>
					</div>
					<div class="chartWrap_harf">
						<div id="chartWeekly"></div>
					</div>
				</div>
				<div class="chartWrap">
					<div class="chartWrap">
						<div id="chartRecfile"></div>
					</div>
				</div>
			</div>
        </div>
    </div>
</body>
</html>
