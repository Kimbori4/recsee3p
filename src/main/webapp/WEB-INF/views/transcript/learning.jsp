<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/search/search.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/transcript/learning.js"></script>
	<script>
	$(function() {
	    $(window).resize(function() {
	    	// 현재 document 높이
			var documentHeight = $(window).height();
	    	var documentWidth  = $(window).width();

			// 그리드 위의 높이 값
			var gridCalcHeight = $("#gridSttDataset").offset().top;
			
			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight - 2);
			
			$("#gridSttDataset").css({"height": + gridResultHeight + "px"});
	    }).resize();
	})
	</script>
</head>
<body>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
    <div class="ui_layout_pannel">
	    <div class="main_contents">
        <div class="ui_layout_pannel">
        	<div class="main_pannel">
				<div class="ui_article">
					<div class="ui_pannel_row">
						<div class="ui_float_left">
							<div class="main_form">
								
							</div>
						</div>
					</div>
				</div>
				<div class="gridWrap">
					<div id="gridSttDataset" style="width:100%"></div>
					<div id="pagingSttDataset"></div>
				</div>
			</div>
		</div>
    	</div>
    </div>
    
    <div id="learnDataset" class="popup_obj" style="width: 400px;">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.sttModel.title.model"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="views.sttModel.label.rModelName"/></label>
                        	<input class="" id="modelName" value="" type="text"/>                               
                    	<label class="ui_label_essential"><spring:message code="views.sttModel.label.rType"/></label>
                    		<select id="type">
                    			<option value='am'><spring:message code="views.sttModel.label.am" /></option>
                    			<option value='lm'><spring:message code="views.sttModel.label.lm" /></option>
                    			<option value='recog'><spring:message code="views.sttModel.label.recog" /></option>
                    		</select>
                    	<div id="selectBox">
                    	</div>
                    	<div id="recogAmModel">
                    	</div>
                    	<div id="recogLmModel">
                    	</div>
                    	<input type="hidden" id="datasetName">
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="learnBtn" class="ui_main_btn_flat icon_btn_cube_white" ><spring:message code="views.sttDataset.button.learn"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
	<input type="hidden" id ="ip" value="${ip}">
    <input type="hidden" id ="port" value="${port}">
    <input type="hidden" id ="delYn" value="${delYn}">
</body>
