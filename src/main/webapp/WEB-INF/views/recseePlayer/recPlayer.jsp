<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>	
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<%! 
		public String XssFilter(String value) {
			if (value == null) {
				return value;
			}
			value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		    value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		    value = value.replaceAll("'", "&#39;");
		    value = value.replaceAll("eval\\((.*)\\)", "");
		    value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		    value = value.replaceAll("<script", "");
		    value = value.replaceAll("`", "");
		    return value;
		}
	%>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<script type="text/javascript" src="${compoResourcePath }/recsee_stt_player/component/jquery/jquery-3.3.1.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/search/search.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/recseePlayer/recseePlayer.css" />
<%-- 	<script type="text/javascript" src="${recseeResourcePath }/js/page/search/search.js?version=20220212"></script> --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/recseePlayer/recseePlayer.js?version=20220416"></script>
	<script>
		var contextPath = "${contextPath}";
		var resourcePath = "${resourcePath}";
		var compoResourcePath = "${compoResourcePath}";
		var HTTP = "${HTTP}";
		var ip = "${ip}";
		var port = "${port}";
		var callKey = "${callKey}";
		var locale = 'ko';
		var listenYn = 'Y';
	</script>
	
</head>


<body>
	    <div id="faceRecordingPlayer" class="popup_obj" stlye="display:block">
    	<div id="listen_popup_header" class="popup_header">
    		<div id="listen_popup_title">청취</div>
    		<!-- <div id="listen_popup_close" onclick="layer_popup_close('#faceRecordingPlayer')">X</div> -->
    		<div class="ui_float_right">
			</div>
    	</div>
    	<div id="listen_popup_info">
    		<div class="cust_info_box_title">고객 정보</div>
    		<div id="cust_info_box">
	    		<div class="cust_info_box">
    			<div class="cust_info">고객명</div>
    			<div class="cust_info_value" id="cust_name_value"></div>
	    		</div>
				<div class="cust_info_box">
    			<div class="cust_info">고객성향등급</div>
    			<div class="cust_info_value" id="cust_level_value"></div>
	    		</div>
				<div class="cust_info_box">
    			<div class="cust_info">상품명</div>
    			<div class="cust_info_value" id="product_name_value"></div>
	    		</div>
	    		<div class="cust_info_box">
    			<div class="cust_info">상품위험등급</div>
    			<div class="cust_info_value" id="product_level_value"></div>
	    		</div>
	    		<div class="cust_info_box">
    			<div class="cust_info">녹취일에 사용된 스크립트</div>
    			<button id="script_pdf_down" onclick="scriptPdfDown()">PDF</button>
	    		</div>
			</div>
    	</div>
    	<div class="cust_info_box_title">녹취 목록</div>
    	<div id="listen_popup_grid" class=""></div>
	    <!-- Audio place -->
    	<div class="cust_info_box_title">플레이어<span id="now_playing_file_name">현재 재생 중인 파일 : <p></p></span></div>
		<iframe name="playerFrame" id="playerFrame"  class="mainFrame" style="width:98%; height: 181px; border:none;" src="${contextPath}/Player2"></iframe>
		<button id="playNext">다음 녹취 듣기</button>
<!-- 		<button id="retryRec">재녹취</button> -->
		<input type="hidden" id="rowId" value=""/>
    </div>
<!--     <iframe name="playerFrame" id="playerFrame"  class="player_pannel mainFrame" src="Player?api=true" style="display:none;"></iframe> -->
    
	<script>
	</script>
</body>

