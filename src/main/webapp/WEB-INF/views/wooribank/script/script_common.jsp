<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
	<head>
		<%@ include file="/WEB-INF/views/common/include/commonVar.jsp"%>
		<script type="text/javascript">
		var detailCase = JSON.parse('${codeMap.SCRT}');
		var recValue = JSON.parse('${codeMap.SSDT}');
		var excelYn = "${nowAccessInfo.getExcelYn()}";
		var writeYn = "${nowAccessInfo.getWriteYn()}";
		var modiYn 	= "${nowAccessInfo.getModiYn()}";
		var delYn 	= "${nowAccessInfo.getDelYn()}";
		//[공용문구] 
		var writeCommonText;
		//TTS 리딩
		var recValue;
		//스트립트 유형 
		var DetailCase;
		var listenIp = "${listenIp}";
		var scriptId = "";
		//프로그레스 처리
		$(function() {
			var PopupCheck = <%=request.getAttribute("PopupCheck")%>;
			
			if (PopupCheck == true) {
				$(".main_lnb").hide();
				$(".main_header").hide();
			}
	
			$(window).resize( function() {
				// 현재 document 높이
				var documentHeight = $(window).height();
	
				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0
						: $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - pagingHeight);
	
	 		 	$(".main_contents").css({"height": + (gridResultHeight - 92)+ "px"}) 
			}).resize();
	
		});
		</script>
		<%-- css --%>
		<link rel="stylesheet" type="text/css" href="${compoResourcePath}/dhtmlxSuite/codebase/dhtmlx.css"/>
		<link rel="stylesheet" type="text/css" href="${compoResourcePath}/dhtmlxSuite/skins/terrace/dhtmlx.css"/>
		<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main.css?ver=20220331" />
		
		<link rel="stylesheet" media="screen and (min-width: 1800px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_common_big.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (min-width: 1281px) and (max-width: 1799px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_common_mid.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (max-width: 1280px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_common_mini.css?ver=20220331" />
		<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_common_main.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (min-width: 1800px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main_big.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (min-width: 1281px) and (max-width: 1799px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main_mid.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (max-width: 1280px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main_mini.css?ver=20220331" />
		
		<%-- js --%>
		<%-- compo --%>
		<script type="text/javascript" src="${compoResourcePath}/streaming/js/jquery-sortable.js"></script>
		<script type="text/javascript" src="${compoResourcePath}/dhtmlxSuite/codebase/dhtmlx.js"></script>
		<%-- recsee --%>
		<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_var.js?ver=20220331"></script>
		<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_api.js?ver=20220331"></script>
		<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_common_main.js?ver=20220331"></script>

	</head>
	<body style="overflow-x:hidden; overflow-y:hidden;">
		<canvas id="canvas" width="1000" height="325" style="display: none;"></canvas>
		<c:choose>
			<c:when test="${tabMode eq 'Y'}">
				<%@ include file="/WEB-INF/views/common/headerTab.jsp"%>
			</c:when>
			<c:otherwise>
				<%@ include file="/WEB-INF/views/common/header.jsp"%>
			</c:otherwise>
		
		</c:choose>
		<div class="main_Registration_contents" id="main_script_Registration_contents" style="display: block; width : 100%; height : auto;">
			<!-- 결재관리 첫 페이지 -->
			<div id="search_box" class="search_box enable">
				<div id="script_info_box" class="script_menu_class float_left">
					<!-- 검색 -->
					<section id="filter" class="filter" >
						<div style="height: 25px; font-weight: bold; margin: 10px 0 10px 6px; color : white">공용스크립트</div>
						<div id="fixed_div">
							<ul style="display:flex; align-items: center">
								<div id="scriptTypeTitle" style="font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white; width : 7%;">ㆍ 스크립트유형</div>
								<select id="commonSearchTTS" style="margin:4px !important" class="group_script_contents ttsSelect">
									<option value="all">전체</option>
								</select>
								<div id="searchTypeTitle" style="font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;width : 5%;">ㆍ 검색유형</div>
								<select id="commonSearchBy" style="margin:4px !important" class="group_script_contents">
									<option value="all">전체</option>
									<option value="1">이름</option>
									<option value="2">설명</option>
									<option value="3">내용</option>
								</select>
								<div id="searchBox">
									<input autocomplete="off" type="text" id="searchCommon" class="search_script_contents" placeholder="검색어를 입력하세요." style="ime-mode:active"/>
									<span id="keywordRemoveImg" onclick="removeKeyword()" style="display:none"></span>
								</div>
								<button id="searchBtn" class="search_btn">조회</button> 							
							</ul>
						</div>
					</section>
				<div id="scriptCommonGrid" style="width:100% !important"></div>
				<div id="commonPaging"></div>
				<div id="commonEdit">
					<input type="hidden" id="common_pk" value="" />
					<div id="insertUpdate" style="visibility:hidden; width:90px; height:20px; line-height:20px; margin-left:30px; font-weight:bold; margin-top:50px; margin-bottom:5px; text-align:center"></div>
					<table class="commonInfoTable">
						<tr style="height:40px">
							<th>이름</th>  
							<td><input autocomplete="off" id="common_name" type="text" placeholder="스크립트 명을 입력하세요." class="readonly common_main_input" readonly="readonly"></td>
						</tr>
						<tr style="height:40px">
							<th>설명</th>
							<td><input autocomplete="off" id="common_desc" type="text" placeholder="용도를 입력하세요." class="readonly common_main_input" readonly="readonly"></td>
						</tr>
						<tr>
							<th>유형</th>
							<td class="ttsRadio"></td>
						</tr>
						<tr>
							<th>실시간</th>
							<td>
								<span id="realtimeRadioSpan"><input type="radio" class="commonRadio" id="realtime_Y" name="realtimeOption" value="Y" disabled><label for="realtime_Y">실시간</label></span>
								<span><input type="radio" class="commonRadio" id="realtime_N" name="realtimeOption" value="N" disabled><label for="realtime_N">비실시간</label></span>
							</td>
						</tr>
						<tr>
							<th id="common_text_title">내용</th>
							<td id="common_textarea">
								<div id="common_text"  class="readonly script_input" readonly="readonly">
									<pre id ="commonScriptDetailText" contenteditable="false"></pre>
								</div>
								<div id="list" class="panel panel-default hide autoList" autoList>
		 							<div class="list-group"></div>
								</div>
							</td>
						</tr>
					</table>
					<div id="commonButtons">
						<!-- 좌측 버튼 -->
						<div id="commonMainButtonLeft">
							<button id="commonInsertBtn" class="common-main-btn-primary">신규</button>
						</div>
						<!-- 우측 버튼 -->
						<div id="commonMainButtonRight" style="position :relative ">
							<button id="commonUpdateBtn" class="common-main-btn-primary">수정</button>
							<button id="commonDeleteBtn" class="common-main-btn-grey">삭제</button>
							<button id="commonUpdateSaveBtn" class="common-main-btn-primary disable">결재의뢰</button>
							<button id="commonInsertSaveBtn" class="common-main-btn-primary disable">결재의뢰</button>
							<button id="commonUpdateCancelBtn" class="common-main-btn-grey disable">취소</button>
							<button id="commonInsertCancelBtn" class="common-main-btn-grey disable">취소</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="dateBox"></div>
	</body>
</html>