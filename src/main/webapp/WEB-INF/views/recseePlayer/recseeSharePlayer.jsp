<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>

	<%@ include file="../common/include/commonVar.jsp" %>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/recsee_player_share/recsee_player.js?ver=1234"></script>	
	<script>
	var windowPopup = "Y"
	var rc_player; 
	var contextPath = '<c:out value="${contextPath}"/>';
	
    $(function() {
    	$("#ip").val("${listenUrl}".split("/")[2].split(":")[0]);
    	$("#port").val("${listenUrl}".split("/")[2].split(":")[1]);
    	$("#userName").val("${userName}");
    	$("#userId").val("${userId}");
    	$("#bgCode").val("${bgCode}");
    	$("#mgCode").val("${mgCode}");
    	$("#sgCode").val("${sgCode}");
    	$("#ip2").val("${ip}");
    	console.log("windowPopup   jsp     "+windowPopup)
    	rc_player = new RecseePlayer({
	            "target" 			: "#playerObj"		// 플레이어를 표출 할 target 
			,	"btnDownFile" 		: false			// 전체파일 다운로드 버튼 사용 여부 (다운로드 시 암호화 된 파일 다운로드)  
			,	"btnUpFile" 		: false			// 파일 업로드 청취 버튼 사용 여부 (플레이어에서 다운로드 한 파일만 청취 가능)
			,	"btnPlaySection"	: true		// 재생 구간 설정 버튼 사용 여부
			,	"btnTimeSection"	: false		// 사용자 정의 구간 설정 버튼 사용 여부
			,	"btnMouseSection"	: true		// 마우스로 구간 설정 버튼 사용 여부
			,	"btnMemoDel"		: false			// 메모 일괄 삭제 버튼 사용 여부
			,	"wave"   			: true				// 웨이브 표출여부 (비 활성시 구간 지정 기능 사용 불가)
			,	"btnDown"			: false				// 구간 설정 시 구간에 대한 다운로드 기능 (다운로드 시 암호화 된 파일 다운로드)
			,	"btnMute"			: true				// 구간 설정 시 묵음처리 기능
			,	"btnDel" 			: true				// 구간 설정 시 제외시키기 기능
			,	"btnReplay"			: false			// 구간 반복재생 메뉴 표출여부
			,	"moveTime"			: 5					// 플레이어 좌우 이동시 증감할 시간; 기본 5초
			,	"list"				: true				// 플레이 리스트 사용 유무
			,	"dual"				: false	 			// 화자분리 플레이어 사용 유무
			,	"memo"				: false				// 메모 사용 유무 
			,	"log"				: true				// 다운로드 로그 사용 유무
			,	"userTypeMenu"				: true				// 유저 타입 메뉴 설정
			,	"playListSave"				: true				// 플레이리스트 저장 여부 
			,	"barCount"			: 200
			,	"requestIp"			: $("#ip").val()	// 통신 IP
			,	"requestPort"		: $("#port").val()	// 통신 Port
    	});
    	$("#dualPlayerObj").hide();
    	rc_player.setFile("audio", '${listenUrl.replaceAll("\\\\","/")}');
    	
    });
    
    // 싱글플레이어 판단
    function isSingle(){
		return $("#playerObj").is(":visible");
	}
    
    // 플레이어 토글링 
    function playerToggle(){
	}
    
    // 활성화된 플레이어 높이 
	function playerHeight(){
		if($("#playerObj").is(":visible"))
			return ("#playerObj .rplayer").outerHeight();
		else
			return ("#dualPlayerObj .rplayer").outerHeight();
	}
    
	</script>
	
</head>

<body>
	<div id="playerObj" class="togglePlayer"></div>
	<div id="dualPlayerObj" class="togglePlayer"></div>
	<input type="hidden" id ="userId" value="">
	<input type="hidden" id ="userName" value="">
	<input type="hidden" id ="ip" value="">
	<input type="hidden" id ="ip2" value="">
    <input type="hidden" id ="port" value="">
    <input type="hidden" id ="bgCode" value="">
    <input type="hidden" id ="mgCode" value="">
    <input type="hidden" id ="sgCode" value="">
</body>