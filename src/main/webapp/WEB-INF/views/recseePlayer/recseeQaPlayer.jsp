<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>

	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/recsee_player/recsee_player.js"></script>
	
	<script>	 
	var rc_player; 
	
	$(function() {
    	rc_player = new RecseePlayer({
	            "target" 			: "#playerObj"	// 플레이어를 표출 할 target 
			,	"btnDownFile" 		: true			// 전체파일 다운로드 버튼 사용 여부 (다운로드 시 암호화 된 파일 다운로드)
			,	"btnUpFile" 		: true			// 파일 업로드 청취 버튼 사용 여부 (플레이어에서 다운로드 한 파일만 청취 가능)
    		,	"btnPlaySection"	: true			// 재생 구간 설정 버튼 사용 여부
    		,	"btnTimeSection"	: true			// 사용자 정의 구간 설정 버튼 사용 여부
    		,	"btnMouseSection"	: true			// 마우스로 구간 설정 버튼 사용 여부
			,	"wave"   			: true			// 웨이브 표출여부 (비 활성시 구간 지정 기능 사용 불가)
			,	"btnDown"			: true			// 구간 설정 시 구간에 대한 다운로드 기능 (다운로드 시 암호화 된 파일 다운로드)
			,	"btnMute"			: true			// 구간 설정 시 묵음처리 기능
			,	"btnDel" 			: true			// 구간 설정 시 제외시키기 기능
			,	"moveTime"			: 5				// 플레이어 좌우 이동시 증감할 시간; 기본 5초
			,	"menuClickRight"	: true			// 구간 메뉴 우클릭 사용 여부
	    	,	"menuClickLeft"		: false			// 구간 메뉴 좌클릭 사용 여부
			
        }); 
    	// 플레이어에 재생                     파일의 실제 경로로 호출
    	rc_player.setFile("audio", "?url=C:\\Spring\\sample\\50.wav");
    	
    	// 플레이어 현재 재생중인 파일에 파일명 표출 시(url= 부분이 가장 마지막에 오면 됩니다.)
    	//rc_player.setFile("audio", "?url=C:/Spring/sample/wave.wav");
    	
    	// 플레이어의 현재 재생중인 파일에 파일명 이외의 정보 표출 시 (음성 요청 주소 제일 마지막 부분에 쿼리스트링 아무 키값이나 추가 후, =/ 다음 표출 하고 싶은 정보를 적어 주면 됩니다.)
    	// ex 
    	// Ext=/1004
    	// name=/agent
    	// phone=/02-123-123
    	// rc_player.setFile("audio", "?url=C:/Spring/sample/wave.wav&callId=/콜아이디");
    	
    	// 플레이어 현재 재생 시간 가져오기
    	// rc_player.currentTime()
    });
	
	</script>
	
</head>

<body>
	<div id="playerObj"></div>
</body>