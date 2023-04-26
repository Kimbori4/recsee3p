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

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridApproveManage").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#gridApproveManage").css({"height": + (gridResultHeight - 4) + "px"})
		    }).resize();
		})
	</script>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/approve_Manage.js"></script>

	<style>
		#gridApproveManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			height: 100%;
        }

        #approvePopup{
        	width: 380px;
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

	    <div class="main_contents">
	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	               <div class="ui_float_left">
	                    <div class="main_form">

	                    	<input title="<spring:message code="common.date.start"/>" maxlength=8 class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" id="reqsDate" type="text" placeHolder="<spring:message code="admin.approve.label.req_date"/>"/>
	                    	<input title="<spring:message code="common.date.end"/>" maxlength=8 class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" id="reqeDate" type="text" placeHolder="<spring:message code="admin.approve.label.req_date"/>"/>

	                    	<input maxlength=6 title="<spring:message code="views.search.grid.head.R_REC_TIME1"/>" class="input_time inputFilter numberFilter timeFilter" id="reqsTime" type="text"  placeholder="<spring:message code="admin.approve.label.req_dateFrom"/>"/>
	                    	<input maxlength=6 title="<spring:message code="views.search.grid.head.R_REC_TIME2"/>" class="input_time inputFilter numberFilter timeFilter" id="reqeTime" type="text"  placeholder="<spring:message code="admin.approve.label.req_dateTo"/>"/>
 
	                    	<input title="<spring:message code="admin.approve.label.req_emp"/>" class="inputFilter numberFilter" id="aUserId" type="text" placeHolder="<spring:message code="admin.approve.label.req_emp"/>"/>
	                    	<input title="<spring:message code="admin.approve.label.req_name"/>" class="inputFilter korFilter engFilter" id="aUserName" type="text" placeHolder="<spring:message code="admin.approve.label.req_name"/>"/>

	                        <select title="<spring:message code="admin.approve.label.req_type"/>" id="approveType" required>
	                            <option value="" disabled selected><spring:message code="admin.approve.label.req_type"/></option>
	                        </select>
	                        <select title="<spring:message code="admin.approve.label.req_reason"/>" id="approveReason" required >
	                            <option value="" disabled selected><spring:message code="admin.approve.label.req_reason"/></option>
	                        </select>
	                        <select title="<spring:message code="admin.approve.label.status"/>" id="approveState" required >
	                            <option value="" disabled selected><spring:message code="admin.approve.label.status"/></option>
	                        </select>

		                    <button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="views.search.button.search"/></button>
		                    <button id="approveBtn" class="ui_main_btn_flat "><spring:message code="admin.button.chkApprove"/></button>
		                    <button id="rejectBtn" class="ui_main_btn_flat "><spring:message code="admin.button.chkNo"/></button>
	                    </div>
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridApproveManage"></div>
			        <div id="pagingApproveManage"></div>
		        </div>
	        </div>
	    </div>
    </div>

	<div class="message_area">
	</div>


    <div id="approvePopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.button.listenAndDown"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.approve.label.req_name"/></label>
	                        <input readOnly class="ui_input_hasinfo" id="approveUserName" value="<c:out value="${userName}"/>" type="text"/>
						<label class="ui_label_essential"><spring:message code="admin.approve.label.req_emp"/></label>
	                        <input readOnly class="ui_input_hasinfo" id="approveUserId" value="<c:out value="${userId}"/>" type="text"/>
						<label class="ui_label_essential"><spring:message code="admin.approve.label.req_team"/></label>
	                        <select class="ui_input_hasinfo" id="approveUserGroup"></select>
                    	<label class="ui_label_essential"><spring:message code="admin.approve.label.req_type"/></label>
	                        <select id="approveType"></select>
                    	<label class="ui_label_essential"><spring:message code="admin.approve.label.req_reason"/></label>
	                        <select id="approveReason"></select>
						<label class="ui_label_essential"><spring:message code="admin.approve.label.req_day"/></label>
							<input maxLength="2" class="inputFilter numberFilter" id="approveDay" value="7" type="text"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="approveBtn" class="ui_main_btn_flat"><spring:message code="admin.button.approve"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#approvePopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<form id="download" name="download" method="post" action="/audioDownload" target="downloadFrame" style="display:none;">
		<input type="hidden" name="fileName" id="fileName" value=""/>
	</form>
    <iframe name="downloadFrame" id="downloadFrame" src="" style="display:none;"></iframe>
	<input type="hidden" id ="ip" value="${ip}">
    <input type="hidden" id ="port" value="${port}">
</body>