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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecording/face_recording.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording.js"></script>
	
	<style>
		.main_contents {
			width:100%;
			background-color:#efefef;
		}
		
		#script_info_box {
			width:calc(100% - 22px);
			height:calc(100% - 20px);
			background-color:white;
			padding:10px;
			font-weight: bold;
			border-top: 1px solid #efefef;
		}
		#wave_box {
			width:100%;
			height:50px;
			background-color:lightgreen;
			margin-top:10px;
		}
		
		.float_left {
			float:left;
		}
		
		.display_none {
			display:none;
		}
		
		.rec_script {
			width: calc(100% - 30px);
		    max-height: calc(100% - 65px);
		    background-color: #f7f7f7;
		    margin-top: 15px;
		    padding: 15px;
		    font-weight: normal; 
		    font-size: 15px;
		    line-height: 25px;
			position: relative;
			overflow:auto;
		}
		
		.speaker_btn {
			width:50px;
			height:50px;
			border-radius:50%;
			border:1px solid #efefef;
			background-color:white;
			position: absolute;
		    bottom: 10px;
		    right: 10px;
		    cursor:pointer;
		}
		button {
			width:100px;
			height:40px;
			background-color:#449ed7;
			color:white;
			float:right;
		    border:0;
		    margin:10px;
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
		<div id="script_info_box" class="float_left">
			<div style="width:5px; height:25px; background-color:#449ed7; margin-right:5px;" class="float_left"></div>
			<div style="height:25px; font-weight: bold; margin-top:2px;">스크립트</div>
			<textarea class="rec_script" id = "script1" style="min-height: calc(38% - 30px);">만 70세 이상인 고령투자자 또는 부적합투자자의 경우 법에 따라 중요한 내용을 빠짐없이 설명해드리기 위해 판매 전 과정을 녹취해야 합니다. 그리고 만약 고객님께서 원하실 경우 녹취파일은 제공이 가능합니다. 조금 불편하시더라도 양해 부탁 드립니다. 그럼 녹취를 시작하겠습니다. 동의하시겠습니까?
			</textarea>
			<button onclick="ttsSetting()">TTS 설정</button>
			<button onclick="ttsPlay()">재생</button>
			<div class="speaker_btn display_none" onclick="tts('script1')"></div>
			<input type="file"/>
			<div id="wave_box" class="float_left"> </div>
		</div>
	</div>
	<audio id="audioPlayer" src="" controls></audio>
</body>
