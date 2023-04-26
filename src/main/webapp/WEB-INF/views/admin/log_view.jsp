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

	<%-- js page --%>
	<script type="text/javascript" src="${compoResourcePath }/line/jquery.lineline.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/log_view.js" charset="utf-8"></script>

	<script>

	$(function() {
	    $(window).resize(function() {
	    	// 현재 document 높이
			var documentHeight = $(window).height();

			// 그리드 위의 높이 값
			var gridCalcHeight = $(".linedwrap").offset().top;
			var pagingHeight = $(".pagination").height();
			// paging 높이 값

			// 탭 전환 등의 시점 때문에 임의로 계산
			$("#fileframe").css({"height": + (documentHeight - gridCalcHeight - pagingHeight - 12) + "px"});
			$(".linedwrap").css({"height": + (documentHeight - gridCalcHeight - pagingHeight - 12) + "px"});

			//
			$('#paging').hide();

	    }).resize();
	});
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
		<%@ include file="./admin_menu.jsp" %>
    	<div class="main_contents admin_body">

	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <div class="div">
	                        <input type="text" id="filter" style="width:235px;" class="" placeholder="<spring:message code="admin.label.searchWord"/>"/>
	                        <button class="ui_main_btn_flat icon_btn_search_white" id="searchBtn"><spring:message code="admin.label.search"/></button>
	                    </div>
	                </div>
	            </div>

		        <div class="gridWrap">
					<div id="fileframe">
						<div id="filetree"></div>
					</div>

					<div id="contentframe">
						<div id="content">
							<div class="linedwrap">
								<div class="linedtextarea"></div>
							</div>
						</div>
						<div class="paginationWrap">
							<input type="text" id="paging" class="paginSize" value="1" placeholder="<spring:message code="admin.label.size"/>" maxlength="1" disabled/>
							<div class="pagination">
								<a class="default absLeft" href="#" >&laquo;</a>
								<a class="default left" href="#">&lsaquo;</a>
								<a class="default right" href="#">&rsaquo;</a>
								<a class="default absRight" href="#">&raquo;</a>
							</div>
						</div>
					</div>
		        </div>
	        </div>
        </div>
    </div>
    <input id="logPath" type="hidden" value="${logPath}"/>
    <input id="logUrl" type="hidden" value="${logUrl}"/>
</body>