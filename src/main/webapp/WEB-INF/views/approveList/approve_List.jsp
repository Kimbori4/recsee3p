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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/approveList/approveList.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/approveList/approve_Manage.js"></script>

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
	                   		<fieldset class="search_fieldset" id="fDate">
								<legend><spring:message code="views.approveList.grid.reqDate"/></legend>
		                    	<input title="<spring:message code="views.approveList.from.sDate"/>" maxlength=8 class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" id="reqsDate" type="text" placeHolder="<spring:message code="views.approveList.grid.reqDate"/>"/>
		                    	<input title="<spring:message code="views.approveList.from.eDate"/>" maxlength=8 class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" id="reqeDate" type="text" placeHolder="<spring:message code="views.approveList.grid.reqDate"/>"/>
	                    	</fieldset>
							<fieldset class="search_fieldset" id="fOrgCode">
								<legend><spring:message code="views.approveList.grid.reqTime"/></legend>
		                    	<input maxlength=6 title="<spring:message code="views.approveList.from.fTime"/>" class="input_time inputFilter numberFilter timeFilter" id="reqsTime" type="text"  placeholder="<spring:message code="views.approveList.from.fTime"/>"/>
		                    	<input maxlength=6 title="<spring:message code="views.approveList.from.tTime"/>" class="input_time inputFilter numberFilter timeFilter" id="reqeTime" type="text"  placeholder="<spring:message code="views.approveList.from.tTime"/>"/>
		                    </fieldset>
							<fieldset class="search_fieldset" id="fUserInfo">
									<legend><spring:message code="views.approveList.grid.reqInfo"/></legend>
		                    	<input maxlength=8 title="<spring:message code="views.approveList.grid.reqId"/>" class="inputFilter engFilter numberFilter" id="aUserId" type="text" placeHolder="<spring:message code="views.approveList.grid.reqId"/>"/>
		                    	<input maxlength=6 title="<spring:message code="views.approveList.grid.reqName"/>" class="inputFilter korFilter engFilter" id="aUserName" type="text" placeHolder="<spring:message code="views.approveList.grid.reqName"/>"/>
	                    	</fieldset>
	                    	<fieldset class="search_fieldset" id="fShare">
								<legend><spring:message code="views.bestCall.form.fieldset.fShare"/></legend>
		                        <select title="<spring:message code="views.approveList.grid.reqType"/>" id="approveType" required>
		                            <option value="" disabled selected><spring:message code="views.approveList.grid.reqType"/></option>
		                        </select>
		                        <select title="<spring:message code="views.approveList.grid.reqReason"/>" id="approveReason" required >
		                            <option value="" disabled selected><spring:message code="views.approveList.grid.reqReason"/></option>
		                        </select>
		                        <select title="<spring:message code="views.approveList.grid.state"/>" id="approveState" required >
		                            <option value="" disabled selected><spring:message code="views.approveList.grid.state"/></option>
		                        </select>
			            	</fieldset>
		                </div>
		            </div>
		            <div class="ui_float_right">
	                    <button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="views.approveList.from.search"/></button>
	                    <button id="approveBtn" class="ui_main_btn_flat "><spring:message code="views.approveList.from.approval"/></button>
	                    <button id="rejectBtn" class="ui_main_btn_flat "><spring:message code="views.approveList.from.return"/></button>
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
	
	
	
	<form id="download" name="download" method="post" action="/audioDownload" target="downloadFrame" style="display:none;">
		<input type="hidden" name="fileName" id="fileName" value=""/>
	</form>
    <iframe name="downloadFrame" id="downloadFrame" src="" style="display:none;"></iframe>
	<input type="hidden" id ="ip" value="${ip}">
    <input type="hidden" id ="port" value="${port}">
</body>