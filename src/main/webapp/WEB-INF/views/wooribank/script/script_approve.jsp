<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
	<head>
	<%@ include file="/WEB-INF/views/common/include/commonVar.jsp"%>
	<script type="text/javascript">
	
	var detailCase = JSON.parse('${codeMap.SCRT}');
	var recValue = JSON.parse('${codeMap.SSDT}');
	
	</script>
	
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/dhtmlxSuite/codebase/dhtmlx.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/dhtmlxSuite/skins/terrace/dhtmlx.css"/>
	
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_approve_main.css?ver=20220331" />
	<link rel="stylesheet" media="screen and (min-width: 1800px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_approve_main_big.css?ver=20220331" />
	<link rel="stylesheet" media="screen and (min-width: 1281px) and (max-width: 1799px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_approve_main_mid.css?ver=20220331" />
	<link rel="stylesheet" media="screen and (max-width: 1280px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_approve_main_mini.css?ver=20220331" />
	
	<link rel="stylesheet" media="screen and (min-width: 1800px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main_big.css?ver=20220331" />
	<link rel="stylesheet" media="screen and (min-width: 1281px) and (max-width: 1799px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main_mid.css?ver=20220331" />
	<link rel="stylesheet" media="screen and (max-width: 1280px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main_mini.css?ver=20220331" />
	
	
	<%-- js page --%>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/jquery-sortable.js"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_var.js?ver=20220331"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_api.js?ver=20220331"></script>
	<script type="text/javascript" src="${compoResourcePath}/dhtmlxSuite/codebase/dhtmlx.js"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_approve.js?ver=20220331"></script>
	
	<style>

	</style>
	</head>
	<body >
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
			<div id="search_box" class="search_box enable">
				<div id="script_info_box" class="script_menu_class float_left">
					<section id="filter" class="filter" >
						<div style="height: 25px; font-weight: bold; margin: 10px 0 10px 6px; color : white">결재목록</div>
						<div id="fixed_div">
							<ul style="display:flex; align-items: center">
								<div id='filterScriptValue'class='filterScriptVal'style="font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 스크립트종류</div>
								<select id="scriptType" style="margin:4px !important" class="group_script_contents">
									<option value="A" selected>전체</option>
									<option value="C">공용</option>
									<option value="P">상품</option>
								</select>
								<div id='filterScriptState'class='filterScriptVal' style="font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 결재상태</div>
								<select id="approveType" style="margin:4px !important" class="group_script_contents">
									<option value="request" selected>결재의뢰</option>
									<option value="approve">결재완료</option>
									<option value="reject">반려</option>
								</select>
								<div id='filterScriptApproveDate'class='filterScriptVal' style="font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 결재의뢰일</div>
								<input autocomplete="off" type="text" id="sDate" class="icon_input_cal inputDate" style="cursor:pointer" onclick="setStartCalendar()">
								<span style="color:white">~</span>
								<input autocomplete="off" type="text" id="eDate" class="icon_input_cal inputDate" style="cursor:pointer" onclick="setEndCalendar()">
								<div id="searchBox" style="position:relative">
									<input autocomplete="off" type="text" id="searchKeyword" class="search_script_contents" placeholder="검색어를 입력하세요." style="ime-mode:active" />
									<span id="keywordRemoveImg" onclick="removeKeyword()" style="display:none"></span>
								</div>
								<button id="searchBtn" class="search_btn" style="margin:0px !important;">조회</button>
							</ul>
						</div>
					</section>
					<div id="scriptApproveListWrap" class="script_approve_list_wrap">		
					<div id="scriptApproveListGrid" class="script_approve_list_grid"></div>
					<div id="ApprovalPaging"></div>
				</div>		
				<div id="scriptApproveBeforeAfter" class="script_approve_Before_After " style="display: none; width : 100%; height : auto;">
					<div id="productName" style="display:none"></div>
					<button id="scriptApprovalList">결재목록 보기</button>
					<button id="scriptReturning">반려</button>
					<button id="scriptApproval">승인</button>
					<button id="cancelApproval">승인취소</button>
					<div id="applyTitle" class="apply_itle" style="width :100%; height : 30px; margin-top:5px;margin-bottom:5px; display :inline-flex;">
						<div id="approveLeft" class="approve_left">
							<div id="scriptApproveBeforeTitle" class="script_approve_Before_title">변경 전</div>
						</div>
						<div id="approveRight" class="approve_right">
							<div id="scriptApproveAfterTitle" class="script_approve_After_title">변경 후</div>
						</div>
					</div>
					<div id="scriptApproveBeforeAfterWrap" class="script_approve_Before_After_Wrap " >	
						<div id="scriptApproveBeforeGridWrap" class="script_approve_Before_grid_wrap">
							<div id="scriptApproveBeforeGridContent" class="script_approve_Before_grid_contet">
								<div id="scriptApproveBeforeGrid" class="script_approve_Before_grid">
								</div>
							</div>
						</div>
						<div id="scriptApproveAfterGridWrap" class="script_approve_After_grid_wrap">
							<div id="scriptApproveAfterGridContent" class="script_approve_After_grid_contet">
								<div id="scriptApproveAfterGrid" class="script_approve_After_grid">
								</div>
							</div>
						</div>
					</div>	
				</div>
			</div>
			
			<div id="dateBox"></div>
	
	</body>