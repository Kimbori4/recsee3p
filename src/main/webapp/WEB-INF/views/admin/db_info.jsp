<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	@SuppressWarnings("unchecked")
	List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) session.getAttribute("AccessInfo");
	MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList,
			"systemOption.dbInfo");
%>


<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/include/commonVar.jsp"%>
<script type="text/javascript">
	$(function() {
		$(window)
				.resize(
						function() {
							// 현재 document 높이
							var documentHeight = $(window).height();

							// 그리드 위의 높이 값
							var gridCalcHeight = $("#gridDBInfo").offset().top;

							// 페이징이 있음 페이징 만큼 뺴주깅
							var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0
									: $(".dhx_toolbar_dhx_web").height());
							var gridResultHeight = (documentHeight
									- gridCalcHeight - pagingHeight);

							$("#gridDBInfo").css({
								"height" : +(gridResultHeight - 4) + "px"
							})
						}).resize();
	})
</script>

<%-- css page --%>
<link rel="stylesheet" type="text/css"
	href="${recseeResourcePath }/css/page/admin/admin.css" />

<%-- js page --%>
<script type="text/javascript"
	src="${recseeResourcePath }/js/page/admin/db_info.js"></script>
<script src="${recseeResourcePath }/js/websocket.js"></script>

<style>
#gridDBInfo {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 100%;
}
/* layer popup */
#addDB {
	width: 400px;
}
/* layer popup */
#excuteQuery {
	width: 1000;
}
</style>
</head>
<body>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp"%>
		</c:otherwise>
	</c:choose>
	<div class="ui_layout_pannel">
		<%@ include file="./admin_menu.jsp"%>
		<div class="main_contents admin_body">
			<div class="ui_acrticle">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<button id="addDBBtn" class="ui_main_btn_flat icon_btn_cube_white">
							Add DB
							<%-- <spring:message code="admin.button.addSubNumber"/> --%>
						</button>
						<button id="modifyDBBtn"
							class="ui_main_btn_flat icon_btn_cube_white">
							Modify DB
							<%-- <spring:message code="admin.button.addSubNumber"/> --%>
						</button>
						<button id="deleteDBBtn" class="ui_btn_white icon_btn_trash_gray"></button>
					</div>
					<div class="ui_float_right">
						<button id="excuteQueryBtn"
							class="ui_main_btn_flat icon_btn_cube_white">
							Excute Query
							<%-- <spring:message code="admin.button.addSubNumber"/> --%>
						</button>
					</div>
				</div>
				<div class="gridWrap">
					<div id="gridDBInfo"></div>
					<div id="pagingDBInfo"></div>
				</div>
			</div>
		</div>
	</div>

	<div id="addDB" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<label class="ui_label_essential">
							DB Name<%-- <spring:message code="admin.subNumber.label.telNo"/> --%>
						</label> <input class="" id="dbName" value="" type="text" /> <label
							class="ui_label_essential">
							DB Driver<%-- <spring:message code="admin.subNumber.label.nickName"/> --%>
						</label> <input class="" id="dbDriver" value="" type="text" /> <label
							class="ui_label_essential">
							DB Url<%-- <spring:message code="admin.subNumber.label.use"/> --%>
						</label> <input class="" id="dbUrl" value="" type="text" /> <label
							class="ui_label_essential">
							DB User<%-- <spring:message code="admin.subNumber.label.use"/> --%>
						</label> <input class="" id="dbUser" value="" type="text" /> <label
							class="ui_label_essential">
							DB Password<%-- <spring:message code="admin.subNumber.label.use"/> --%>
						</label> <input class="" id="dbPassword" value="" type="text" />
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="dbAdd" class="ui_main_btn_flat icon_btn_cube_white">
							Add
							<%-- <spring:message code="admin.button.addSubNumber"/> --%>
						</button>
						<button id="dbModify" class="ui_main_btn_flat icon_btn_cube_white">
							Modify
							<%-- <spring:message code="admin.button.modifySubNumber"/> --%>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="excuteQuery" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<label class="ui_label_essential">SelectQuery<%-- <spring:message code="admin.subNumber.label.telNo"/> --%></label>
						<select class="" id="selectQuery">
							<option value="" selected disabled>Select Query</option>
						</select> <label>&nbsp;</label> <input type="text" id="selectQueryContent"
							readonly style="background: #d2d2d2"> <label>&nbsp;</label>
						<input type="text" id="selectQueryDescription" readonly
							style="background: #d2d2d2"> <label
							class="ui_label_essential">UpsertQuery<%-- <spring:message code="admin.subNumber.label.nickName"/> --%></label>
						<select class="" id="upsertQuery">
							<option value="" selected disabled>Upsert Query</option>
						</select> <label>&nbsp;</label> <input type="text" id="upsertQueryContent"
							readonly style="background: #d2d2d2"> <label>&nbsp;</label>
						<input type="text" id="upsertQueryDescription" readonly
							style="background: #d2d2d2">
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="excuteBtn"
							class="ui_main_btn_flat icon_btn_cube_white">
							Excute
							<%-- <spring:message code="admin.button.modifySubNumber"/> --%>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>