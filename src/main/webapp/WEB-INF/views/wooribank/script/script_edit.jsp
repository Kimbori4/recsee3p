<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
	<head>
		<title>스크립트 수정</title>
		<%@ include file="/WEB-INF/views/common/include/commonVar.jsp"%>
		<script type="text/javascript">
		var detailCase = JSON.parse('${codeMap.SCRT}');
		var recValue = JSON.parse('${codeMap.SSDT}');
		var excelYn = "${nowAccessInfo.getExcelYn()}";
		var writeYn = "${nowAccessInfo.getWriteYn()}";
		var modiYn 	= "${nowAccessInfo.getModiYn()}";
		var delYn = "${nowAccessInfo.getDelYn()}";
		
		
		// 상품그룹정보
		var productGroupInfo = JSON.parse('${productGroupInfo}');
		
		// [공용문구] 
		var writeCommonText;
		// TTS 리딩
		var recValue;
		// 스트립트 유형 
		var DetailCase;
		var listenIp = '<c:out value="${listenIp}"/>';
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
		// 수정 페이지 팝업시 전달받은 값을 전역변수로 추가
		var editProductPk = '<c:out value="${productPk}"/>';
		var editTransactionId = '<c:out value="${transactionId}"/>';
		var editProductListPk = '<c:out value="${productListPk}"/>';
		var environment = '<c:out value="${environment}"/>';
		</script>
		<%-- css page --%>
		<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/wooribank/script/script_main.css?ver=20220331" />
		
		<link rel="stylesheet" media="screen and (min-width: 1800px)" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/wooribank/script/script_common_big.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (min-width: 1281px) and (max-width: 1799px)" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/wooribank/script/script_common_mid.css?ver=20220331" />
		<link rel="stylesheet" media="screen and (max-width: 1280px)" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/wooribank/script/script_common_mini.css?ver=20220331" />
		
		<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/codebase/dhtmlx.css"/>
		<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/skins/terrace/dhtmlx.css"/>
		<link rel="stylesheet" type="text/css" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-ui/jquery-ui.css"></script>
		
		<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/wooribank/script/script_edit.css?ver=20220331" />
		
		<%-- js page --%>
		
		<%-- compo --%>
		<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/streaming/js/jquery-sortable.js"></script>
		<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/codebase/dhtmlx.js"></script>
		
		<%-- recsee --%>
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_var.js?ver=20220331"></script>
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_api.js?ver=20220331"></script>
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_edit.js?ver=20220331"></script>
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_common_popup.js?ver=20220331"></script>
	
		<style>
		.questionMark {
			width: 14px;
			height: 14px;
			background-image: url(<c:out value="${recseeResourcePath}"/>/images/project/icon/wooribank/question-circle.svg);
			background-position: center;
			background-repeat: no-repeat;
			background-size: 12px;
			position: absolute;
			cursor: pointer;
		}
		</style>
	</head>
<body onpagehide="makePopupVarNull()" onbeforeunload="makePopupVarNull()">	
	<section class="contents wrap">
        <div class="script_wrap">
            <div class="product_info">
            	<div class="real_product_info">
	            	<div id ="productName" class ="productInfoVal">
	            		<div id="product_name_title" class="productInfoName">ㆍ 상품명</div>
	            		<div id="product_name_value" class="productInfoText"></div>
	            	</div>
	            	<div id="productType" class ="productInfoVal">
	            		<div id="product_type_title" class="productInfoName">ㆍ 상품유형</div>
	            		<div id="product_type_value"  class="productInfoText"></div>            	
	            	</div>
	            	<div id="productGroupCode" class ="productInfoVal">
	            		<div id="repre_product_code" class="productInfoName"></div>
	            		<div id="repre_product_code_value"  class="productInfoText"></div>            	
	            	</div>            	
	            	<div id ="productCode" class ="productInfoVal">
	            		<div id="product_code_title" class="productInfoName"></div>
	            		<div id="product_code_value"  class="productInfoText"></div>            	
	            	</div>            	
	            	<div id="productDataInfo" class ="productInfoVal">
	            		<div id="winiValue"></div>		
	     		    </div>            	
     		    </div>            	
            </div>
            <div class="scriptWrap">           
                <div class="script_step_wrap">
                	<div class ="script_step_area" >
	                    <div class="script_step_header">스크립트 목록
	                    <button id="add_scriptStep_btn" class="addScriptStepBtn">스크립트목록 추가</button>	               
	                    </div>	                    	                    
						<div id="scriptTreeGrid" class ="scripTreeGrid">
							<div id ="scriptGrid"></div>
						</div>
					</div>	
                </div>
                
                <div class="script_detail_wrap">
                	<div class="script_detail_area">
	                    <div class="script_detail_header">
	                    	<div id="productInfo" style ="display:none" stepPk="" stepFk="" productCode="" productPk=""></div> 	                    
							<div class="script_step_title">제목						
							</div>
								<input autocomplete="off" id="script_edit_title_area" class="script_step_title_text" type="text" placeholder="제목을 입력하세요" style="ime-mode:active"> 
								<button id="addStepBtn" class="add_Step_Btn disable" style="">저장</button>
								                    </div>
	                    <div class="script_detail_content">
	                    	<div class="script_detail_content_info">
								<div class="script_detail_content_title">스크립트 구성</div>
								<div id="scriptContentBtnWrap" class="script_detail_content_btn_Line">
									<button id="common_sentence_btn" class="content_btn" value="공용 문구 ">공용문구</button>
									<button id="add_content_Btn" class="detail_content_btn" value="TTS 리딩" addType="T" comKind="N">TTS리딩</button>
									<button id="add_adviser_Btn" class="detail_content_btn" value="직원직접리딩 " addType="S" comKind="N">직원직접리딩</button>
									<button id="add_direction_Btn" class="detail_content_btn" value="고객 답변" addType="G" comKind="N">고객답변</button>
									<button id="add_object_Btn" class="detail_content_btn" value="적합성보고서" addType="R" comKind="N">적합성보고서</button>
								</div>
							</div>
							<div id="scriptDetailContentValue" class ="script_detail_content_value">
								<div id="realScriptDetailContentValue" class ="real_script_detail_content_value"></div>																	
							</div>
	                    </div>
	                    <div id="playZone" class="play_Zone">
 							<audio id="audioPlayer" controls="" src=""></audio> 
						</div> 						        
			   			<div class="transactionButtons">
							<button id="rollbackBtn" class="edit_btn rollback_btn">취소</button>
							<button id="commitBtn" class="edit_btn commit_btn">결재 의뢰</button>
						</div>
                	</div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>