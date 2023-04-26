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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/transcript/transcript.js"></script>
	<script>
	$(function() {
	    $(window).resize(function() {
	    	// 현재 document 높이
			var documentHeight = $(window).height();
	    	var documentWidth  = $(window).width();

			var playerHeight = $('.player_pannel').height();
			
			// 그리드 위의 높이 값
			var gridCalcHeight = $("#gridTranscript").offset().top;
			
			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight - playerHeight - 2);
			
			$("#gridTranscript").css({"height": + gridResultHeight + "px"});
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
					<div id="gridTranscript" style="width:100%"></div>
					<div id="pagingTranscript"></div>
				</div>
			</div>
		</div>
    	</div>
    </div>
    
    <div id="createDataset" class="popup_obj" style="width: 400px;">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.sttDataset.title.dataset"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="views.sttDataset.label.rDatasetName"/></label>
                        	<input class="" id="datasetName" value="" type="text"/>                               
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="createDatasetBtn" class="ui_main_btn_flat icon_btn_cube_white" ><spring:message code="views.sttDataset.button.dataset"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div id="fileDivision" class="popup_obj">
        <div class="ui_popup_padding">
        
        
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.approve.label.transcript"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            
            <div class="ui_article ui_row_input_wrap">
            	<div class="ui_pannel_row" style="width:500px; height:400px; overflow:auto;">
            		<div class="ui_padding">
                    	<div class="tree_view_wrap">
				            <div class="tree_agent_view" id="searchTreeViewAgent"></div>
						</div>
                    </div>
                </div>
            </div>
            
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="fileDivisionBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="views.search.grid.head.R_DIVISION"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#fileDivision")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
        </div>
        <input id="delRecfileInfoSeq" type="hidden"/>
    </div>
    
    <div id="fileLoad" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.transcript.label.msg1"/><%-- 로컬파일 업로드 --%> </p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
	            <div class="ui_article ui_row_input_wrap">
	                <div class="ui_pannel_row">
	                    <div class="ui_padding">
	                    	<label class="ui_label_essential">mp3/audio</label>
	                      	<div class="filebox file_upload_wrap"  id="filebox1">
	                			<input class="upload_name" id="upload_name1"  value="<spring:message code="views.transcript.label.msg2"/>" readonly="readonly"/> 
								<label class="ButtonLabel" for="upLoadFile_hidden1" style="padding-left:12px !important;padding-right:12px !important;"><spring:message code="views.transcript.label.msg3"/></label>
	                          	<input type="file" name="upLoadFile_hidden1" id="upLoadFile_hidden1" class="upload_hidden" accept=".mp3,audio,.pcm" multiple>
	                        </div>
	               		</div>
	                    <div class="ui_padding">
	                    	<label>txt</label>
	                        <div class="filebox file_upload_wrap"  id="filebox1">
	                			<input class="upload_name" id="upload_name2"  value="<spring:message code="views.transcript.label.msg2"/>" readonly="readonly"/> 
								<label class="ButtonLabel" for="upLoadFile_hidden2" style="padding-left:12px !important;padding-right:12px !important;"><spring:message code="views.transcript.label.msg3"/></label>
	                          	<input type="file" name="upLoadFile_hidden2" id="upLoadFile_hidden2" class="upload_hidden" accept=".txt" multiple>
		                    </div>
	                    </div>
	                </div>
	            </div>
	         
            <div class="ui_article">
                <div class="ui_pannel_row">
                	<div class="ui_float_left">
                	</div>
                    <div class="ui_float_right">
                        <button id="uploadBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="views.transcript.label.msg4"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
	<input type="hidden" id ="ip" value="<c:out value="${ip}"/>">
    <input type="hidden" id ="port" value="<c:out value="${port}"/>">
    <input type="hidden" id ="http" value="<c:out value="${http}"/>">
    <input type="hidden" id ="writeYn" value="<c:out value="${writeYn}"/>">
    <input type="hidden" id ="delYn" value="<c:out value="${delYn}"/>">
</body>
