<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/common/include/commonVar.jsp"%>
<script type="text/javascript">

/* DB에서 common code 가져오기 */
// 스크립트유형
var detailCase = JSON.parse('${codeMap.SCRT}');
// TTS리딩
var recValue = JSON.parse('${codeMap.SSDT}');
// 상품부서
var productDept = JSON.parse('${codeMap.r_biz_dis}');

var environment = '${environment}';

</script>
<%-- css page --%>
<link rel="stylesheet" type="text/css" href="${compoResourcePath}/dhtmlxSuite/codebase/dhtmlx.css"/>
<link rel="stylesheet" type="text/css" href="${compoResourcePath}/dhtmlxSuite/skins/terrace/dhtmlx.css"/>

<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_main.css?ver=20220331" />
<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_step.css?ver=20220331" />

<link rel="stylesheet" media="screen and (min-width: 1800px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_copy_big.css?ver=20220331" />
<link rel="stylesheet" media="screen and (min-width: 1281px) and (max-width: 1799px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_copy_mid.css?ver=20220331" />
<link rel="stylesheet" media="screen and (max-width: 1280px)" type="text/css" href="${recseeResourcePath}/css/page/wooribank/script/script_copy_mini.css?ver=20220331" />


<%-- js page --%>
<script type="text/javascript" src="${compoResourcePath}/streaming/js/jquery-sortable.js"></script>
<script type="text/javascript" src="${compoResourcePath}/dhtmlxSuite/codebase/dhtmlx.js"></script>

<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_step.js?ver=20220331"></script>
<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_copy.js?ver=20220331"></script>
<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_product.js?ver=20220331"></script>
<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_var.js?ver=20220331"></script>
<script type="text/javascript" src="${recseeResourcePath}/js/page/wooribank/script/script_api.js?ver=20220331"></script>

<script>
var accessLevel = "${nowAccessInfo.getAccessLevel()}";
var excelYn = "${nowAccessInfo.getExcelYn()}";
var writeYn = "${nowAccessInfo.getWriteYn()}";
var modiYn 	= "${nowAccessInfo.getModiYn()}";
var delYn 	= "${nowAccessInfo.getDelYn()}";
// [공용문구] 
var writeCommonText;

var listenIp = "${listenIp}";
var scriptId = "";

// 프로그레스 처리
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
<style>
/* css 파일에 추가 시 인식 안됨, css 정리 후 적용 */
/* 검색어 지우기 */
span#keywordRemoveImg {
	width: 30px;
	height: 15px;
	background-image: url(${recseeResourcePath}/images/project/icon/wooribank/icon_btn_exit_white.png);
	background-position: center;
	background-repeat: no-repeat;
	background-size: 10px;
	position: absolute;
	right: 15px;
	bottom: 5px;
	cursor: pointer;
}
/* 도움말 아이콘 */
.questionMark {
	width: 14px;
	height: 14px;
	background-image: url(${recseeResourcePath}/images/project/icon/wooribank/question-circle.svg);
	background-position: center;
	background-repeat: no-repeat;
	background-size: 12px;
	position: absolute;
	cursor: pointer;
}
.objbox .row20px {
	width: 400px !important;
}
/* MODAL - COMMON */
.modal {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 1000;
    width: 100vw;
    height: 100vh;
    background: rgba(85, 85, 85, 0.5);
    overflow: auto;
}
.modal.ac {
    display: block;
}

.modal-pop {
    width: 380px;
    position: absolute;
    top: 60px;
    left: 0;
    right: 0;
    /* transform: translateY(-50%); */
    margin: 0 auto;
    min-width: 320px;
    background-color: #fff;
    border-radius: 7px;
/*     border: 1px solid #266bb15e; */
}

/* MODAL - NEW REG */
.modal-pop.modal-newReg {
    /* width: 376px; */
}

.modal-header {
    height: 35px;
    background-color: #1760F0;
    padding: 0 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-radius: 5px 5px 0 0;
}
.modal-header .tit {
    color: #fff;
    font-size: 14px;
    font-weight: bold;
}
.modal-header .modal-close {
    display: block;
    width: 16px;
    height: 16px;
    background: url(${recseeResourcePath}/html_1209/img/modal_close.svg) center/cover no-repeat;
}
.modal-body {
    padding: 16px;
}

.modal-body .btn-wrap {
    margin-top: 9px;
}
.modal-body .btn-wrap button {
    margin-left: 4px;
    margin-right: 0;
}
.modal-newReg table,
.modal-table {
    table-layout: fixed;
    width: 100%;
    margin-top: -4px;
    margin-bottom: -4px;
}

.modal-newReg table th {
    width: 60px;
    text-align: left;
    font-weight: 400;
    vertical-align: middle;
}
.modal-table th {
    width: 90px;
    text-align: left;
    font-weight: 400;
    vertical-align: middle;
}
.modal-newReg table input,
.modal-newReg table select,
.modal-table input,
.modal-table select {
    width: 100%;
}

.modal-newReg table th,
.modal-newReg table td,
.modal-table th,
.modal-table td {
    padding: 4px 0;
}

.modal .modal-body .text {
    color: #111418;
    font-size: 12px;
    line-height: 0.8;
}


</style>
</head>
<body style="overflow:hidden;">
	<canvas id="canvas" width="1000" height="325" style="display: none;"></canvas>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="/WEB-INF/views/common/headerTab.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/views/common/header.jsp"%>
		</c:otherwise>
	</c:choose>
	<div class="main_Registration_contents" id="main_script_Registration_contents" style="display: block; width : 100%; height : 100%;">
		<div id="search_box" class="search_box enable">
			<div id="script_info_box" class="script_menu_class float_left">
			
			<section id="filter" class="filter" >
				<div style="height: 25px; font-weight: bold; margin: 10px 0 10px 6px; color : white">스크립트조회</div>
				<div id="fixed_div">
					<ul style="display:flex; align-items: center; justify-content : flex-start;">
						<div id="proType" class="productSearchTitle" style="  font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 상품유형</div>
						<select id="scriptType" style="margin:4px !important" class="group_script_contents">
						</select>
						<div id="searchType" class="productSearchTitle" style="font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 검색유형</div>
						<select id="scripSearchtType" style="margin:4px !important" class="group_script_contents">
							<option class="script_Type" value="1" selected>전체</option>
							<option class="script_Type" value="2">상품명</option>
							<option class="script_Type" value="3">상품코드</option>
							<option class="script_Type" value="4">그룹코드</option>
						</select> 
						<div id="saleState"class="productSearchTitle" style=" font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 판매상태</div>
						<select id="scriptUseType" style="margin:4px !important" class="group_script_contents">
							<option class="script_Type" value="all" selected>전체</option>
							<option class="script_Type" value="Y" selected="selected">판매</option>
							<option class="script_Type" value="N">판매중지</option>
						</select>
						<div  id="scriptExistYN" class="productSearchTitle" style=" font-size:13px;margin:0px 3px 2px 5px;font-weight:700;color:white;">ㆍ 스크립트유무</div>
						<select id="scriptRegistType" style="margin:4px !important" class="group_script_contents">
							<option class="script_Type" value="all" selected>전체</option>
							<option class="script_Type" value="Y" selected="selected">등록</option>
							<option class="script_Type" value="N">미등록</option>
						</select>
						<div id="searchBox" style="position:relative">
							<input autocomplete="off" type="text" id="searchKeyword" class="search_script_contents" placeholder="검색어를 입력하세요." style="ime-mode:active" />
							<span id="keywordRemoveImg" onclick="removeKeyword()" style="display:none"></span>
						</div>
						<button id="searchBtn" class="search_btn" style="position:static !important">조회</button> 							
					</ul>
				</div>
		 	</section>
		
				<div class="search_script_edit">
					<div id="searchGridBtnWrap" class="search_grid_btn_wrap" style="display:none;">
						<button id="addNewScript" class="searchGridBtnLine" style="float:left;">신규등록</button>
						<button id="grpManage" class="searchGridBtnLine" style="float:left;">그룹관리</button>
						<button id="sysRelflect" class="searchGridBtnLine" style="float:right;">시스템반영</button>
					</div>
					<div id="search_script_grid" class="search_script_edit_title1"></div>
					<div id="search_script_edit_content" class="search_script_edit_content"></div>
				</div>
				<div id="changeRecordBox"></div>
				<div class="right_bottom">
					<div class="right_paging" id="right_paging"></div>
					<div class="right_bottom_btn">
						<input type="hidden" id="scriptCode" />
					</div>
				</div>
			</div>
		</div>
		
	
	<section class="contents wrap disable">
        <div class="script_wrap">
            <div class="product_info">
            	<div class="real_product_info">
	            	<div id ="productName" class ="productInfoVal">
	            		<div id="product_name_title" class="productInfoName">ㆍ 상품명</div>
	            		<div id="product_name_value" class="productInfoText"></div>
	            	</div>
	            	<div id="productType" class ="productInfoVal">
	            		<div id="product_type_title" class="productInfoName">ㆍ 상품유형</div>
	            		<div id="product_type_value" class="productInfoText"></div>            	
	            	</div>
	            	<div id="productGroupCode" class ="productInfoVal">
	            		<div id="repre_product_code" class="productInfoName">ㆍ 그룹코드</div>
	            		<div id="repre_product_code_value" class="productInfoText"></div>            	
	            	</div>            	
	            	<div id ="productCode" class ="productInfoVal">
	            		<div id="product_code_title" class="productInfoName">ㆍ 상품코드</div>
	            		<div id="product_code_value" class="productInfoText"></div>            	
	            	</div>            	
	            	<div id="productDataInfo" class ="productInfoVal">
	            		<div id="winiValue"></div>		
	     		    </div>            	
     		    </div>            	
            </div>
            <div class="scriptWrap">           
                <div class="script_step_wrap">
                	<div class ="script_step_area" >
	                    <div class="script_step_header">스크립트 목록</div>
						<div id="scriptTreeGrid" class ="scripTreeGrid">
							<div id="scriptGrid" ></div>
						</div>
					</div>	
                </div>
                
                <div class="script_detail_wrap">
                	<div class="script_detail_area">
	                    <div class="script_detail_header">
	                    	<div id="productInfo" style ="display : none;" stepPk="" ; stepFk=""; productCode=""; productPk="";></div>
	                    	
							<div class="script_step_title">제목
								<span id="titleHelpMessage" class="questionMark" onmouseover="showTitleHelp(this)" onmouseout="hideTitleHelp()"></span>							
							</div>
							<div id="script_edit_title_area" class="script_step_title_text"></div>
							<div class="script_detail_btn_Line">
								<button id="excel_download_btn" class="script_detail_btn downloadBtn" onclick="downloadExcel()"><img src="${recseeResourcePath}/images/project/icon/wooribank/iconXLS.gif" style="margin-right:5px"/>다운로드</button>
								<button id="pdf_download_btn" class="script_detail_btn downloadBtn" onclick="downloadPdf()"><img src="${recseeResourcePath}/images/project/icon/wooribank/iconPDF.gif" style="margin-right:5px"/>다운로드</button>
								<button id="copy_script_btn" class="script_detail_btn">복사하기</button>
								<button id="edit_script_btn" class="script_detail_btn">스크립트 수정</button>
								<button id="back_script_btn" class="script_detail_btn">목록보기</button>
							</div>
	                    </div>
	                    <div class="script_detail_content">
	                    	<div class="script_detail_content_info">
								<div class="script_detail_content_title">스크립트 구성</div>
							</div>
							<div id="scriptDetailContentValue" class ="script_detail_content_value">
								<div id="realScriptDetailContentValue" class ="real_script_detail_content_value"></div>																	
							</div>
	                    </div>
	                    <div id="playZone" class="play_Zone">
 							<audio id="audioPlayer" controls="" src=""></audio> 
						</div> 
                	</div>
                </div>
            </div>
        </div>
    </section>
		
	<div id="groupListBox"></div>
		
	<div id="dateBox"></div>

		<div class="modal ac" id="bkExcelPop" style="display:none;">
			<div class="modal-pop" style="">
				<div class="modal-header">
					<span class="tit">TA 도움</span> <a href="#" class="modal-close"></a>
				</div>
				<div class="modal-body">		
				<form  id="form1" name="form1" method="post" enctype="multipart/form-data" onsubmit="return false">
					<article style="margin-top: 0px;">
						<div class="taResultPopBodyHeader">
							<input type="file"
								style="width: 100%; height: 24px; margin-bottom: 15px;" id="excelForm" name ="excelForm">
						</div>

					</article>


					<div class="taResultPopBodyHeader">
						<button
							style="float: right; width: 60px; height: 27px; margin-bottom: 8px; background-color: #1760F0; margin-right: 0px;" onclick="excelUpload()">업로드</button>
					</form>
						<button
							class ="modal-close" style="float: right; width: 60px; height: 27px; margin-bottom: 8px; background-color: #1760F0; margin-right: 11px;">닫기</button>
					</div>
				</div>
			</div>
			<div></div>
		</div>
</body>