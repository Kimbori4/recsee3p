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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<%@ include file="../common/include/commonVar.jsp" %>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>

	<link rel="stylesheet" type="text/css" href="<c:out value="${siteResourcePath}"/>/css/page/header.css" />

	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/audioCalibration/audio_calibration.css" />

	<%-- js page --%>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/audioCalibration/audio_calibration_popup.js"></script>
	
	<style>
		.audioCalibrationPopup {
			width:100%;
		    height: 100%;
		}
		.audio_calibration_main_pannel{
			width: 90%;
		    height: 100%;
		    margin: 0 auto;
		}
		
		#audio_calibration_option_box {
			width: 100%;
		    height: 13%;
		    border: 2px solid #0067ac;
		    margin-top: 20px;
		}
		.playFileName {
			margin-top: 60px;
		    font-family: NotoSansKR;
		    font-size: 14px;
		    font-weight: 500;
		    font-stretch: normal;
		    font-style: normal;
		    line-height: normal;
		    letter-spacing: normal;
		    text-align: left;
		    color: #000000;
		}
		.calibrationBox {
			margin-top: 35px;
			width: 100%;
			height: 55%;
		}
		.calibrationPlayer {
			width: calc(44% - 1px);
		    height: 90%;
		    float: left;
		    background-color:#f2f2f2;
		}
		#calibrationBtn {
			width:11%;
			height:90%;
			border-radius: 25px;
			box-shadow: 0 4px 10px 0 rgba(0, 0, 0, 0.25);
			background-color: #b9e3b2;
			text-align:center;
			float:left;
			cursor:pointer;
			margin: 0 5px;
		}
		
		#calibrationBtn span {
			display: block;
			margin-top: 10px;
			font-family: NotoSansKR;
			font-size: 18px;
			font-weight: bold;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			color: #000000;
		}
		
		#calibrationBtn img {
			width: 80px;
    		margin-top: 100%;
		}
		
		.playerWave {
			width: 100%;
		    height: 60%;
		    background-color: white;
		    margin-top: 40px;
		    position:relative;
		}
		
		.playController {
			width:150px;
			height:70px;
			border-radius: 35px;
  			box-shadow: 0 4px 15px 0 rgba(0, 0, 0, 0.1);
  			background-color: #ffffff;
			margin: 20px auto;
		}
		
		.contolBtn {
			width: 40px;
		    height: 40px;
		    border-radius: 2px;
		    float: left;
		    margin: 18px 0 0 26px;
			cursor:pointer;
		}
		.caliOptionWrap {
			width: 93%;
		    height: 40%;
		    margin: 25px auto;
		}
		
		.caliOption {
			width: 25%;
		    height: 50px;
		    float: left;
		    margin-top: 1%;
		}
		
		.caliOption input {
			width: 10%;
   			height: 50%;
		    margin: 4px 5px 0 0;
		    display: block;
		    float: left;
		}
		
		.caliOption span {
			display: block;
			float: left;
			margin-top: 7px;
			margin-right: 15px;
		}
		
		.caliOption select {
			width: 47%;
			height: 70%;
		}
		#audioCalibrationSaveBtn {
		    width: 80px;
		    height: 30px;
		    background-color: #449ed7;
		    color: white;
		    float: right;
		    border: 0;
		    text-align: center;
		    padding-top: 10px;
		}
	</style>
	<script>
		var audioFilePath = "${audioPath}";
		//var maskingYn = "${maskingAccessInfo.getReadYn()}";
		$(function() {
		    $(window).resize(function() {
		    	
		    }).resize();
		})
	</script>
</head>
<body>
    <div class="audioCalibrationPopup">
    	<div class="audio_calibration_main_pannel">
    		<div style="margin-top:60px;">아래 옵션을 선택하여 음질을 보정하세요.</div>
    		<div id="audio_calibration_option_box">
    			<ul class="caliOptionWrap">
    				<li class="caliOption" id="noise">
    					<input type="checkBox" name="noiseChk"/>
    					<span>노이즈 제거</span>
    					<select id="noiseSelect">
    						<option value="0.21">1</option>
    						<option value="0.23">3</option>
    						<option value="0.25">5</option>
    						<option value="0.27">7</option>
    						<option value="0.29">9</option>
    					</select>
    				</li>

    				<li class="caliOption" id="volumeUp">
    					<input type="checkBox" name="volumeUpChk"/>
    					<span>음질 증폭</span>
    					<select id="volumeUpSelect">
    						<option value="1">1</option>
    						<option value="2">2</option>
    						<option value="3">3</option>
    						<option value="4">4</option>
    						<option value="5">5</option>
    						<option value="6">6</option>
    						<option value="7">7</option>
    						<option value="8">8</option>
    						<option value="9">9</option>
    						<option value="10">10</option>
    					</select>
    				</li>
    				<li class="caliOption" id="highFrequency">
    					<input type="checkBox" name="highFrequencyChk"/>
    					<span>높은 주파수 설정</span>
    					<select id="highFrequencySelect">
    						<option value="200">200</option>
    						<option value="300">300</option>
    						<option value="400">400</option>
    					</select>
    				</li>

    				<li class="caliOption" id="lowFrequency">
    					<input type="checkBox" name="lowFrequencyChk"/>
    					<span>낮은 주파수 설정</span>
    					<select id="lowFrequencySelect">
    						<option value="2000">2000</option>
    						<option value="3000">3000</option>
    						<option value="4000">4000</option>
    					</select>
    				</li>
    			</ul>
    		</div>
    		<div class="playFileName">현재 선택된 파일 : </div>
		    <div class="calibrationBox">
			    <div id="beforeCali" class="calibrationPlayer">
			    	<div class="playerWave" id="beforePlayerWave"></div>
			    	<div class="playController" id="beforePlayerController">
			    		<div class="contolBtn play" id="beforePlay"><img class="" src="<c:out value="${commResourcePath}"/>/recsee/images/project/icon/startBtn.png"></div>
			    		<div class="contolBtn stop" id="beforeStop"><img class="" src="<c:out value="${commResourcePath}"/>/recsee/images/project/icon/stopBtn.png"></div>
			    	</div>
			    	<audio id="beforePlayer">
						<source id="beforePlayerSrc" src="" type="audio/wav">
					</audio>
			    </div>
			    <div id="calibrationBtn">
			    	<img class="" src="<c:out value="${commResourcePath}"/>/recsee/images/project/icon/calibration.png">
			    	<span>보정 실행</span>
			    </div>
			    <div id="afterCali" class="calibrationPlayer">
			   		<div class="playerWave" id="afterPlayerWave"></div>
			   		<div class="playController" id="beforePlayerController">
			   			<div class="contolBtn play" id="afterPlay"><img class="" src="<c:out value="${commResourcePath}"/>/recsee/images/project/icon/startBtn.png"></div>
			    		<div class="contolBtn stop" id="afterStop"><img class="" src="<c:out value="${commResourcePath}"/>/recsee/images/project/icon/stopBtn.png"></div>
			   		</div>
					<audio id="afterPlayer">
						<source id="afterPlayerSrc" src="" type="audio/wav">
					</audio>
			    </div>
			</div>
			<div id="audioCalibrationSaveBtn">저장</div>
    	</div>
    </div>
</body>
