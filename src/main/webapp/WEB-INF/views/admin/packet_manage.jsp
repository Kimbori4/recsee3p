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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/packet_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridPacketManage").offset().top;
				
				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				
				$("#gridPacketManage").css({"height": + (gridResultHeight - 4) + "px"})	
		    }).resize();
		})
	</script>

	<style>
		/* layer popup */
        #addPacketPop{
            width: 485px;
        }
        .ui_input_hasinfo{
		    background-color: #f1f1f1;
		    cursor: not-allowed !important;
		}
		#gridPacketManage{
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
		                <button id="systemAddBtn" class="ui_main_btn_flat icon_btn_cube_white" ><spring:message code="admin.systemOption.label.settingAdd"/></button>
		                <button id="systemDel" class="ui_btn_white icon_btn_trash_gray"></button>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridPacketManage"></div>
		        </div>
	        </div>

        </div>
    </div>

    <div id="addServer" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.systemOption.label.settingAdd"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.systemOption.label.custCode"/></label>
                        	<input class="" id="custCode" value="" type="text"/>                               
                    	<label class="ui_label_essential"><spring:message code="admin.systemOption.label.phoneSetting"/></label>            
                        	<input class="" id="phoneSetting" value="" type="text"/>                             
                    	<label class="ui_label_essential"><spring:message code="admin.systemOption.label.returnMsg"/></label>
                        	<input class="" id="returnMsg" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.systemOption.label.returnUrl"/></label>
                        	<input class="" id="returnUrl" value="" type="text"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button class="ui_main_btn_flat icon_btn_cube_white" id="systemAdd"><spring:message code="admin.systemOption.label.settingAdd"/></button>
                        <button class="ui_main_btn_flat icon_btn_cube_white" id="systemModify"><spring:message code="admin.systemOption.label.settingModi"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>