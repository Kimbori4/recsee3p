<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/audioCalibration/audio_calibration.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/audioCalibration/audio_calibration.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/wavesurfer/wavesurfer.min.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/wavesurfer/wavesurfer.cursor.min.js"></script>
	
	<style>
		.main_contents {
			width:100%;
			background-color:#efefef;
		}
		.audio_calibration_main_pannel{
			float: left;
			clear: both;
			width: 100%;
		}
		
		wave {
			border-right:none !important;
		}
		
		div.gridbox.gridbox_dhx_web table.obj tr td {
			height:50px !important;
		}
		
		/*search Form*/
		.search_fieldset{
			display:block;
			float:left;
			border: 1px solid #2d71c4 !important;
			position : relative;
			transition : transform 1s;
		}
		
		.search_fieldset legend{
			font-size : 12px;
			margin-left : 5px;
			color: #3da0e3;
		}
		
		#searchBtn {
		    float: left;
		    height: 35px;
		    margin-top: 10px;
		}
		
	</style>
	<script>
		//var maskingYn = "${maskingAccessInfo.getReadYn()}";
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();
		    	var documentWidth  = $(window).width();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridAudioCalibration").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				
				$("#gridAudioCalibration").css({"height": + (gridResultHeight - 2) + "px"})
		    }).resize();
		})
	</script>
</head>
<body>
	<canvas id="canvas" width="1000" height="325" style="display:none;"></canvas>
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
	        	<div class="audio_calibration_main_pannel">
					<div class="ui_article">
						<div class="ui_pannel_row">
							<div class="ui_float_left">
								<div class="main_form">
								</div>
							</div>
						</div>
					</div>
					<div class="gridWrap">
						<div id="gridAudioCalibration"></div>
						<div id="pagingAudioCalibration"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
