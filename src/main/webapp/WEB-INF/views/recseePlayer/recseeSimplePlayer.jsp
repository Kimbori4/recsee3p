<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<title>Recsee Player</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/recsee_player_mini/recsee_player.js?ver=1234"></script>	
	<script>
	
	//새로고침 막음
	function noEvent() {
		if (event.keyCode == 116) { //f5
			event.keyCode= 2;
			rc_player.setFile("audio", decodeURI('${listenUrl.replaceAll("\\\\","/")}'));
			return false;
		}
			else if(event.ctrlKey && event.keyCode == 82 & event.keyCode == 16) //컨트롤 시프트 r
		{	rc_player.setFile("audio", decodeURI('${listenUrl.replaceAll("\\\\","/")}'));
			return false;
		}
			else if(event.ctrlKey && event.keyCode == 82 ) //컨트롤 r
			{	rc_player.setFile("audio", decodeURI('${listenUrl.replaceAll("\\\\","/")}'));
				return false;
			}
	}
	
    $(function() {
    	
    	document.onkeydown = noEvent;
    	
    	//history.replaceState({}, null, location.pathname);
    	
    	

    	//우클릭 막기
   	 	document.oncontextmenu = function (e) {
   		   return false;
   		}
   	 	//f12 막기
   		 document.onkeydown = function (e){
			if (e.which === 123)
				return false;
		}; 
		
    	
    	var ip = "${listenUrl}".split("/")[2].split(":")[0];
    	var port = "${listenUrl}".split("/")[2].split(":")[1];
    	rc_player = new RecseePlayer({
	            "target" 			: "#playerObj"		// 플레이어를 표출 할 target 
           	,	"requestIp"			: ip	// 통신 IP
   			,	"requestPort"		: port	// 통신 Port
    	});
    	
    	//console.log("${listenUrl}");
    	rc_player.setFile("audio", decodeURI('${listenUrl.replaceAll("\\\\","/")}'));
    	
    	$("#ip").val(ip);
    	$("#port").val(port);
    });
    
</script>
	
</head>

<body>
	<div id="playerObj" class="togglePlayer"></div>
	<input type="hidden" id ="ip" value="">
    <input type="hidden" id ="port" value="">
</body>