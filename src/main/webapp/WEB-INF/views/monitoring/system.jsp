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

	<%-- body zoom lib --%>
	<%-- <script type="text/javascript" src="${compoResourcePath }/zoomooz/jquery.zoomooz.min.js"></script> --%>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/monitoring/system.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/websocket.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/system.js"></script>

	<style>
	    /* 트랜지션 이벤트 걸기 위해 이 페이지만 적용 */
		body, html{
			transition: transform ease 0.9s, transform-origin ease 0.9s;
		}
	</style>

	<script>
		$(function() {
			top.playerVisible(false);
		    $(window).resize(function() {
		    	// 현재 scroll 높이
				var documentHeight = $(window).height();
		    	var bodyHeight = $('body').prop("scrollHeight");

				// 리모컨 위의 높이 값
				var calcHeight = $(".zoom_controll_bar").offset().top;

				var gridResultHeight = (bodyHeight - calcHeight);

				$(".zoom_controll_bar").css({"height": + (gridResultHeight - 4) + "px"})
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
    <div class="main_contents">
        <div class="ui_layout_pannel">
        	<div class="sys_monitroing_wrap">
			    <div class="sys_alert" style='display:none;'>
			    	<i class="sys_siren"><img src="${recseeResourcePath }/images/project/map-view/icon_siren_red.gif"/></i>
			    	<marquee></marquee>
			    </div>

	        	<div class="zoomcontents">
	        		<div class="sys_monitroing_bg"></div>
	        		<div class="sys_monitroing_main">

	        			<div class="map_obj_wrap">
	        				<div class="ui_map_pos">
	        					<div class="ui_map_kor"></div>
		        			</div>
		        		</div>
		       		</div>
	       		</div>
	       		<div class="conWrap zoom_controll_bar">
					<div class="" id='remoCon'>
						<button id="zoomStartBtn" class="ui_remote_btn_start zoom_btn_style">Start</button>
						<button id="prevBtn" class="ui_remote_btn_prev zoom_btn_style">Prev</button>
						<button id="nextBtn" class="ui_remote_btn_next zoom_btn_style">Next</button>
						<button id="initBtn" class="ui_remote_btn_stop zoom_btn_style">Stop</button>
						<button id="rotationBtn" class="ui_remote_btn_rotation zoom_btn_style">Rotation</button>
						<button id="fullScreenBtn" class="ui_remote_btn_extend zoom_btn_style">Extend</button>
						<button id="editTargetBtn" class="ui_remote_btn_conf zoom_btn_style"><spring:message code="monitoring.display.editTarget"/></button>
					</div>
				</div>
				<div class="detail_info_wrap">
					<div class="popup_detailinfo_wrap">
						<div class="sys_monitroing_atc hidddenElement" id='dbWrapParent' style='display:none;'>
							<div class="sys_monitroing_atc_header">
								<p class="sys_monitroing_atc_tit">데이터베이스(Postgresql)</p>
							</div>
							<div class="sys_monitroing_atc_body">
								<div class="call_info_wrap">
									<div class="call_info" id='dbWrap'>
										<p><span class="ui_icon_nowcall">Postgresql 서비스</span><span class="call_info_val" id='pgOnOff'></span></p>
										<p><span class="ui_icon_ready">Postgresql 세션 개수</span><span class="call_info_val" id='pgCount'></span></p>
									</div>
								</div>
							</div>
						</div>
						<div class="sys_monitroing_atc hidddenElement" id='recWrapParent' style='display:none;'>
							<div class="sys_monitroing_atc_header">
								<p class="sys_monitroing_atc_tit"><spring:message code="monitoring.display.recServer"/></p>
							</div>
							<div class="sys_monitroing_atc_body">
								<div class="call_info_wrap">
									<div class="call_info"  id='recWrap'>
										<p><span class="ui_icon_recfile"><spring:message code="monitoring.display.recService"/></span><span class="call_info_val" id='recOnOff'></span></p>
									</div>
								</div>
							</div>
						</div>
						<div class="sys_monitroing_atc hidddenElement" id='wasWrap' style='display:none;'>
							<div class="sys_monitroing_atc_header">
								<p class="sys_monitroing_atc_tit">WAS SERVER</p>
							</div>
							<div class="sys_monitroing_atc_body">
								<div class="sys_connection_info">
									<div id="wasChart"></div>
								</div>
							</div>
						</div>
					</div>
					<div class="popup_sysinfo_wrap">
						<div class="popup_sysinfo hidddenElement" id='cpuWrap' style='display:none;'>
							<div class="sys_monitroing_atc_header">
								<p class="sys_monitroing_atc_tit">CPU <spring:message code="monitoring.display.use"/></p>
							</div>
	   						<div class="popup_sysinfo_header">
								<div id='cpuGrap' style='height: 190px'></div>
							</div>
						</div>
						<div class="popup_sysinfo hidddenElement" id='memoryWrap' style='display:none;'>
							<div class="sys_monitroing_atc_header">
								<p class="sys_monitroing_atc_tit">MEMORY <spring:message code="monitoring.display.use"/></p>
							</div>
	   						<div class="popup_sysinfo_header">
								<div id='memoryGrap' style='height: 190px'></div>
							</div>
						</div>
						<div class="popup_sysinfo hidddenElement" id='hddWrap' style='display:none;'>
							<div class="sys_monitroing_atc_header">
								<p class="sys_monitroing_atc_tit">HDD <spring:message code="monitoring.display.use"/></p>
							</div>
	   						<div class="popup_sysinfo_header">
								<div id='hddGrap' style='height: 190px'></div>
							</div>
						</div>
		    		</div>
		    	</div>
			</div>
        </div>
    </div>
</body>