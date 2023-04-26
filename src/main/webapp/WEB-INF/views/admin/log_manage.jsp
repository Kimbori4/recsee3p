<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
//@SuppressWarnings("unchecked")
//List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)session.getAttribute("AccessInfo");
//MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "management_setting_system");
%>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>

	<%-- css page --%>
	<%-- <link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" /> --%>

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/websocket.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/log_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridLogManage").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#gridLogManage").css({"height": + (gridResultHeight - 4) + "px"})
		    }).resize();
		})
	</script>

	<style>
		#gridLogManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */

        }
        #detailInfo{
            width: 400px;
        }
       #detailText{
      	 height:200px;
        width:100%;
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
	                    <form>
	                        <select id="daySelect" required>
	                            <option value="" disabled selected><spring:message code="admin.label.selectSearchDate"/></option>
	                            <option value="day"><spring:message code="admin.label.day"/></option>
	                            <option value="week"><spring:message code="admin.label.week"/></option>
	                            <option value="month"><spring:message code="admin.label.month"/></option>
	                            <option value="custom"><spring:message code="admin.label.userCustom"/></option>
	                        </select>
	                        <input maxlength="8" type="text" id="sDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilte" placeholder="Start Date"/>
	                        <input maxlength="8" type="text" id="eDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilte" placeholder="End Date"/>
	                    </form>
	                    <form>
	                        <select id="sTime" required>
	                            <option value="" disabled selected><spring:message code="common.label.start"/></option>
	                        </select>
	                        <select id="eTime" required>
	                            <option value="" disabled selected><spring:message code="common.label.end"/></option>
	                        </select>
	                    </form>
	                    <button id="logSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <form>
	                        <input id="rLogIp" type="text" class="inputFilter ipFilter" placeholder="<spring:message code="admin.label.logIp"/>"/>
	                        <input id="rLogServerIp" type="text" class="inputFilter ipFilter" placeholder="<spring:message code="admin.label.serverIp"/>"/>
	                        <input maxlength='8' id="rLogUserId" class="inputFilter" type="text" class="" placeholder="<spring:message code="admin.label.userId"/>"/>
	                        <select id="rLogName" required>

	                        </select>
	                        <select id="rLogContents" required>
	                        	<option value=""><spring:message code="access.grade.A"/></option>
	                        </select>
	                        <input id="rLogEtc" type="text" class="inputFilter korFilter engFilter numberFilter" placeholder="<spring:message code="admin.label.content"/>"/>
	                    </form>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>

		        <div class="gridWrap">
			        <div id="gridLogManage"></div>
			        <div id="pagingLogManage"></div>
		        </div>
	        </div>

        </div>
    </div>

	<div id="detailInfo" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.content"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                        <textarea disabled id="detailText" style="resize: none;"></textarea>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<div class="message_area">
	</div>
	<iframe name="downloadFrame" id="downloadFrame" src="" style="display:none;"></iframe>
</body>