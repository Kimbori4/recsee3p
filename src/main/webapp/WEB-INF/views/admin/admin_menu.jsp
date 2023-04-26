<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/admin_common.js"></script>

	<script>

		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 메뉴 위의 높이 값
				var calcHeight = $('.admin_lnb').offset().top;
				var resultHeight = (documentHeight - calcHeight);
				$('.admin_lnb').css({"height": + (resultHeight - 4) + "px"});
		    }).resize();
		})
	</script>

	<style>
		/* Layer Popup */
        #addJuri, #addAllowable{
            width: 400px;
        }
	</style>

</head>
<body>
	<div class="admin_lnb">
		   	<%-- <ul>
		   		<p class="admin_menu_tit"><spring:message code="admin.menu.systemOption"/></p>
		   		<li><a href="${webPath }/admin/channel_manage" target="_self"><p><spring:message code="admin.menu.li.systemOption.channel"/></p></a></li>
		   		<li><a href="${webPath }/admin/server_manage" target="_self"><p><spring:message code="admin.menu.li.systemOption.server"/></p></a></li>
		   		<li><a href="${webPath }/admin/switch_manage" target="_self"><p><spring:message code="admin.menu.li.systemOption.switchboard"/></p></a></li>
		   	</ul>
		   	<ul>
		   		<p class="admin_menu_tit"><spring:message code="admin.menu.systemManage"/></p>
		   		<li><a href="${webPath }/admin/group_manage" target="_self"><p><spring:message code="admin.menu.li.systemManage.group"/></p></a></li>
		   		<li><a href="${webPath }/admin/detail_manage" target="_self"><p><spring:message code="admin.menu.li.systemManage.details"/></p></a></li>
		   		<li><a href="${webPath }/admin/log_manage" target="_self"><p><spring:message code="admin.menu.li.systemManage.log"/></p></a></li>
		   		<li><a href="${webPath }/admin/que_manage" target="_self"><p><spring:message code="admin.menu.li.systemManage.queue"/></p></a></li>
		   	</ul>
		   	<ul>
		   		<p class="admin_menu_tit"><spring:message code="admin.menu.userManage"/></p>
		   		<li><a href="${webPath }/admin/user_manage" target="_self"><p><spring:message code="admin.menu.li.userManage.userAuthority"/></p></a></li>
		   		<li><a href="${webPath }/admin/screen_manage" target="_self"><p><spring:message code="admin.menu.li.userManage.usageScreen"/></p></a></li>
		   		<li><a href="${webPath }/admin/search_manage" target="_self"><p><spring:message code="admin.menu.li.userManage.userDefinition"/></p></a></li>
		   	</ul> --%>
	</div>

    <div id="addJuri" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.group.add"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_padding add_juri_wrap">
                        <input id="authyName" class="icon_input_tit inputFilter korFilter engFilter numberFilter" maxlength="30" value="" type="text"  placeholder="<spring:message code="admin.menu.placeholder.group"/>"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addGroupBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.button.addGroup"/></button>
                        <button id="modifyGroupBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.button.addModi"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="addAllowable" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.allowableRange.button.rangeAdd"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_padding add_juri_wrap">
                        <input id="allowableName" class="icon_input_tit inputFilter korFilter engFilter numberFilter" maxlength="30" value="" type="text"  placeholder="<spring:message code="admin.alert.allowableRangeManage5"/>"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addAllowableBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.allowableRange.button.rangeAdd"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</body>