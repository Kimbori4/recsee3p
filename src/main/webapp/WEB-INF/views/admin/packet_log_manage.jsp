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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/packet_log_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridPacketLogManage").offset().top;
				
				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				
				$("#gridPacketLogManage").css({"height": + (gridResultHeight - 4) + "px"})	
		    }).resize();
		})
	</script>

	<style>
        .ui_input_hasinfo{
		    background-color: #f1f1f1;
		    cursor: not-allowed !important;
		}
		#gridPacketLogManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
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
						<div class="main_form">
							<select id="daySelect" required>
								<option value="" disabled selected><spring:message code="admin.label.selectSearchDate"/></option>
								<option value="day"><spring:message code="admin.label.day"/></option>
								<option value="week"><spring:message code="admin.label.week"/></option>
								<option value="month"><spring:message code="admin.label.month"/></option>
								<option value="custom"><spring:message code="admin.label.userCustom"/></option>
							</select>
							<input maxlength="8" type="text" id="sDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilte" placeholder="Start Date"/>
							<input maxlength="8" type="text" id="eDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilte" placeholder="End Date"/>
							<select id="sTime" required>
								<option value="" disabled selected><spring:message code="common.label.start"/></option>
							</select>
							<select id="eTime" required>
								<option value="" disabled selected><spring:message code="common.label.end"/></option>
							</select>
	                        <input maxlength='20'  id="extNum" class="inputFilter numberFilter" type="text" placeholder="내선"/>
							<select id="sysCode"></select>
	                        <input maxlength='300'  id="callId" class="inputFilter" type="text" placeholder="콜 아이디"/>
	                        <input maxlength='100'  id="custPhone" class="inputFilter numberFilter" type="text" placeholder="고객 전화번호"/>
	                        <input maxlength='20' id="custCode" class="inputFilter" type="text" class="" placeholder="고객 코드"/>
							<button id="packetLogSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>
						</div>
					</div>
	                <div class="ui_float_right">
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridPacketLogManage"></div>
			        <div id="pagingPacketLogManage"></div>
		        </div>
	        </div>

        </div>
    </div>
</body>