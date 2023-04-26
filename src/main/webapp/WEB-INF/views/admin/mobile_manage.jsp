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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/mobile_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridMobileManage").offset().top;
				
				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				
				$("#gridMobileManage").css({"height": + (gridResultHeight - 4) + "px"})	
		    }).resize();
		})
	</script>

	<style>
		#gridMobileManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
        #gridMobileManageFileList{
            height: 400px;
        }
        #storagePeriodLabel{
        	margin-top: 15px;
        }
	</style>
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
		<%@ include file="./admin_menu.jsp" %>
	    <div class="main_contents admin_body">

	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
		                <button id="storagePeriodBtn" class="ui_main_btn_flat icon_btn_cube_white" ><%-- <spring:message code="admin.button.addSubNumber"/> --%>보관기간 설정</button>
		                <label>보관 기간 : <span id='storagePeriodLabel'></span></label>
		                <button id="requestInfoBtn" class="ui_main_btn_flat icon_btn_cube_white" ><%-- <spring:message code="admin.button.addSubNumber"/> --%>요청 정보 설정</button>
						요청 IP : <span id='requestIPLabel'></span>
						요청 Port : <span id='requestPortLabel'></span>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridMobileManage"></div>
			        <div id="pagingMobileManage"></div>
		        </div>
	        </div>

        </div>
    </div>

    <div id="storagePeriodPop" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><%-- <spring:message code="admin.subNumber.title.addSubNumber"/> --%>보관기간 설정</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential">보관기간<%-- <spring:message code="admin.subNumber.label.telNo"/> --%></label>
                    		<input id="storagePeriodValue" type='text' />
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="setStoragePeriodBtn" class="ui_main_btn_flat icon_btn_cube_white" >요청<%-- <spring:message code="admin.button.addSubNumber"/> --%></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="requestInfoPop" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><%-- <spring:message code="admin.subNumber.title.addSubNumber"/> --%>요청 정보 설정</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential">IP</label>
                    		<input id="requestIPValue" type='text' />
                    	<label class="ui_label_essential">Port</label>
                    		<input id="requestPortValue" type='text' />
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="setRequestInfoBtn" class="ui_main_btn_flat icon_btn_cube_white" >설정<%-- <spring:message code="admin.button.addSubNumber"/> --%></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    
    <div id="logLevelPop" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><%-- <spring:message code="admin.subNumber.title.addSubNumber"/> --%>로그레벨설정</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential">로그레벨<%-- <spring:message code="admin.subNumber.label.telNo"/> --%></label>
                    		<select id="logLevelList"></select>
                    		<input id="rllUserId" type="hidden" />
                    		<input id="rllUserPhone" type="hidden" />
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="reqLogLevelBtn" class="ui_main_btn_flat icon_btn_cube_white" >요청<%-- <spring:message code="admin.button.addSubNumber"/> --%></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div id="fileUploadPop" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><%-- <spring:message code="admin.subNumber.title.addSubNumber"/> --%>파일업로드요청</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_acrticle">
            	<div class="gridWrap">
			        <div id="gridMobileManageFileList"></div>
			        <div id="pagingMobileManageFileList"></div>
			        <input id="rfuUserId" type="hidden" />
                    <input id="rfuUserPhone" type="hidden" />
		        </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="reqFileUploadBtn" class="ui_main_btn_flat icon_btn_cube_white">요청<%-- <spring:message code="admin.button.addSubNumber"/> --%></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<div class="message_area">
	</div>
</body>