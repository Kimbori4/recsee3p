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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/recording/recording.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/recording/recording.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/format_reader.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/mpeg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/ogg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/wav.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/Queue.js"></script>
	
	<style>
		.main_contents {
			width:65%;
		}
		
		.log_contents {
			width:30%;
			padding:1%;
			float:left;
   			border-left: 1px solid #dedede;
   			height: 713px;
   			overflow: auto;
		}
		.log_contents span{
			display:block;
			margin-bottom:2px;
			line-height: 1.5;
		}
		.stt {
			color:blue;
		}
		.ta {
			color:red;
		}
		#recordingTime {
			width: 100%;
			font-family: NotoSansKR;
			font-size: 48px;
			font-weight: 300;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			color: #000000;
			text-align: center;
			margin: 40px auto;
		}

		#recordingWave {
			width:100%;
			height:200px;
			margin: 53px 0 1px;
  			background-color: #f2f2f2;
  			position:relative;
		}
		
		#rec_title {
			font-family: NotoSansKR;
			font-size: 18px;
			font-weight: bold;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			text-align: center;
			color: #000000;
			margin-top: 34px;
		}
		
		.rec_button_box {
			width:540px;
			height:303px;
			margin:30px auto;
		}
		
		#rec_start {
			width: 220px;
			height: 303px;
			border: solid 2px #e5e5e5;
			background-color: #ffffff;
			float:left;
		}
		
		#rec_start:hover {
			background-color: #deedff;
		}
		
		#rec_end {
			width: 220px;
			height: 303px;
			background-color: #e5e5e5;
			float:right;
		}
		
		.rec_button_box div {
			text-align:center;
			cursor:pointer;
		}
		
		.rec_button_box div img {
			margin-top:47px;
		}
		
		.rec_button_box div span {
			display:block;
			font-family: NotoSansKR;
			font-size: 24px;
			font-weight: bold;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			text-align: left;
			color: #000000;
		    width: 110px;
    		margin: 31px auto;
		}
	</style>
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
    <div class="main_contents">
		<div id="recordingTime">00:00:00</div>
		<div id="recordingWave" class="waveCanvasObj"></div>
		<div id="recording">
			<div id="rec_title">시작 버튼을 눌러 녹취를 시작하세요</div>
			<div class="rec_button_box">
				<div id="rec_start">
					<img class="" src="${commResourcePath}/recsee/images/project/icon/rec_start.png">
					<span>녹취 시작</span>
				</div>
				<div id="rec_end">
					<img class="" src="${commResourcePath}/recsee/images/project/icon/rec_end.png">
					<span>녹취 종료</span>
				</div>
			</div>
		</div>
	</div>
	
	<div class="log_contents">
	</div>
</body>
