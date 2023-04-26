<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	response.addHeader("P3P","CP='IDC DSP COR ADM DEViTali CAO PSA PSD IVDi CONi HIS IND CNT CONIOTR OUR DEM ONL'");
%>

<!DOCTYPE html>
<html>
<head>

	
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/recsee_player/faceRecsee_player.js?ver=1234"></script>
<%-- 	<script type="text/javascript" src="${compoResourcePath }/recsee_player/recsee_player.js?ver=1238"></script> --%>
	
	
	<script>
	
	var playSection 	= 	("${menu.getWriteYn()}"		=="Y"?true:false);
	var timeSection 	= 	("${menu.getModiYn()}"		=="Y"?true:false);
	var mouseSection 	= 	("${menu.getDelYn()}"		=="Y"?true:false);
	var memoDel 		=	("${menu.getListenYn()}"	=="Y"?true:false);
	var upFile 			=	("${menu.getDownloadYn()}"	=="Y"?true:false);
	var downFile 		=	("${menu.getExcelYn()}"		=="Y"?true:false);
	var downPlayer 		=	("${menu.getPrereciptYn()}"	=="Y"?true:false);
	
	var memo	=	("${sectionMenu.getWriteYn()}"		=="Y"?true:false);
	var replay	=	("${sectionMenu.getModiYn()}"		=="Y"?true:false);
	var down	=	("${sectionMenu.getDelYn()}"		=="Y"?true:false);
	var mute	=	("${sectionMenu.getListenYn()}"		=="Y"?true:false);
	var del 	=	("${sectionMenu.getDownloadYn()}"	=="Y"?true:false);
	
	/* var rc_player; 
	var contextPath = '<c:out value="${contextPath}"/>';
	var playerPassword = '<c:out value="${playerPassword}"/>';
	var playerPeriod = '<c:out value="${playerPeriod}"/>'; */
	
    $(function() {
    	
    	//우클릭 막기
	 	document.oncontextmenu = function (e) {
		   return false;
		}
    	
    	rc_player = new RecseePlayer({
	            "target" 			: "#playerObj"		// 플레이어를 표출 할 target 
	    	,	"btnDownPlayer" 	: downPlayer		// 플레이어 다운로드 버튼 사용 여부  
			,	"btnDownFile" 		: downFile			// 전체파일 다운로드 버튼 사용 여부 (다운로드 시 암호화 된 파일 다운로드)  
			,	"btnUpFile" 		: upFile			// 파일 업로드 청취 버튼 사용 여부 (플레이어에서 다운로드 한 파일만 청취 가능)
			,	"btnPlaySection"	: playSection		// 재생 구간 설정 버튼 사용 여부
			,	"btnTimeSection"	: timeSection		// 사용자 정의 구간 설정 버튼 사용 여부
			,	"btnMouseSection"	: mouseSection		// 마우스로 구간 설정 버튼 사용 여부
			,	"btnMemoDel"		: memoDel			// 메모 일괄 삭제 버튼 사용 여부
			,	"wave"   			: true				// 웨이브 표출여부 (비 활성시 구간 지정 기능 사용 불가)
			,	"btnDown"			: down				// 구간 설정 시 구간에 대한 다운로드 기능 (다운로드 시 암호화 된 파일 다운로드)
			,	"btnMute"			: mute				// 구간 설정 시 묵음처리 기능
			,	"btnDel" 			: del				// 구간 설정 시 제외시키기 기능
			,	"btnReplay"			: replay			// 구간 반복재생 메뉴 표출여부
			,	"moveTime"			: 5					// 플레이어 좌우 이동시 증감할 시간; 기본 5초
			,	"list"				: true				// 플레이 리스트 사용 유무
			,	"dual"				: false	 			// 화자분리 플레이어 사용 유무
			,	"memo"				: memo				// 메모 사용 유무 
			,	"log"				: true				// 다운로드 로그 사용 유무
			,	"userTypeMenu"				: true				// 유저 타입 메뉴 설정
			,	"playListSave"				: true				// 플레이리스트 저장 여부 
			,	"barCount"			: 200
			,	"requestIp"			: $("#ip").val()	// 통신 IP
			,	"requestPort"		: $("#port").val()	// 통신 Port
			,	"HTTP"		: $("#HTTP").val()	// http, htttps 유무
    	});
    	
    	dual_rc_player = new RecseePlayer({
	            "target" 			: "#dualPlayerObj"	// 플레이어를 표출 할 target 
			,	"btnDownFile" 		: false				// 전체파일 다운로드 버튼 사용 여부 (다운로드 시 암호화 된 파일 다운로드)
			,	"btnUpFile" 		: false				// 파일 업로드 청취 버튼 사용 여부 (플레이어에서 다운로드 한 파일만 청취 가능)
			,	"btnPlaySection"	: false				// 재생 구간 설정 버튼 사용 여부
			,	"btnTimeSection"	: false				// 사용자 정의 구간 설정 버튼 사용 여부
			,	"btnMouseSection"	: false				// 마우스로 구간 설정 버튼 사용 여부
			,	"btnMemoDel"		: true				// 메모 일괄 삭제 버튼 사용 여부
			,	"wave"   			: true				// 웨이브 표출여부 (비 활성시 구간 지정 기능 사용 불가)
    		,	"replay"			: false				// 이어듣기 표출 여부
			,	"btnDown"			: false				// 구간 설정 시 구간에 대한 다운로드 기능 (다운로드 시 암호화 된 파일 다운로드)
			,	"btnMute"			: false				// 구간 설정 시 묵음처리 기능
			,	"btnDel" 			: false				// 구간 설정 시 제외시키기 기능
			,	"moveTime"			: 5					// 플레이어 좌우 이동시 증감할 시간; 기본 5초
			,	"list"				: false				// 플레이 리스트 사용 유무
			,	"dual"				: true	 			// 화자분리 플레이어 사용 유무
			,	"memo"				: false				// 메모 사용 유무 
			,	"log"				: false				// 다운로드 로그 사용 유무 
			,	"barCount"			: 200
			,	"requestIp"			: $("#ip").val()	// 통신 IP
			,	"requestPort"		: $("#port").val()	// 통신 Port
			,	"HTTP"		: $("#HTTP").val()	// http, htttps 유무
		});
    	
    	$("#playerObj").hide();

    	top.rc = rc_player;
    	top.nowRc = rc_player;
    	top.dual_rc = dual_rc_player;
    	
//     	새로고침 이벤트 
//     	top.keyDownEvent(document);
    	
    	//크롬에선 loadstart 이벤트 타면서 프로세스바 생성 및 파형 색상 표현되므로 초기화 추가 처리
    	setTimeout(function(){
    		$(".waveObj").css("opacity","0");
    		progress.off();
    	},1000)
    	
    });
    
    // 싱글플레이어 판단
//     function isSingle(){
// 		return $("#playerObj").is(":visible");
// 	}
    
    // 플레이어 토글링 
//     function playerToggle(){
//     	top.nowRc.pause();
// 		$(".togglePlayer").toggle();
		
// 		top.dualSetting(!$("#playerObj").is(":visible"));
// 		if($("#playerObj").is(":visible")){
// 			//	 청취 중 플레이어 변경 될 경우 url 변경하여 바뀌는 플레이어에서 재생 될 수 있게 수정... @David
// 			var url = top.nowRc.listenUrl;
// 			var data = top.nowRc.recFileData;
// 			top.nowRc = rc_player;
			
// 			$(".player_tx .procTime").removeClass("dual");
// 	       	$(".player_rx .procTime").removeClass("dual");
	       	
// 	       	if(url != "" || url == undefined ){
// 	       		var $player =  top.nowRc.player;
//                 var rc = top.rc;
                
//                 top.nowRc.recFileData = data;
                
// 	       		top.nowRc.setFile("audio", url , undefined , true);
// 	       	}
	       	
// 		}else{
// 			//	 청취 중 플레이어 변경 될 경우 url 변경하여 바뀌는 플레이어에서 재생 될 수 있게 수정... @David
// 			var url = top.nowRc.listenUrl;
// 			var data = top.nowRc.recFileData;
// 			top.nowRc = dual_rc_player;
// 			$(".player_tx .procTime").addClass("dual");
// 	       	$(".player_rx .procTime").addClass("dual");
	       	
// 	       	if(url != ""  || url == undefined){
	       		
// 	       		var $player =  top.nowRc.player;
	       		
//                 var rc = top.rc;
                
//                 top.nowRc.recFileData = data;
                
//                 if(/_IVR*?/.test(url)){
//                 	alert(lang.common.alert.PartialRec);	/* 부분녹취는 듀얼 플레이어로 청취 하실 수 없습니다. */
//                 	top.nowRc.pause();
//             		$(".togglePlayer").toggle();
//             		top.dualSetting(!$("#playerObj").is(":visible"));
//                 	return false;
//                 }
                
// 	       		top.nowRc.setFile("audio", url , undefined , true);
// 	       	}
// 		}	
// 	}
    
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
	<input type="hidden" id ="ip" value="${ip}">
    <input type="hidden" id ="port" value="${port}">
    <input type="hidden" id ="HTTP" value="${HTTP}">
</body>