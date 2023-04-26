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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/logo_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridLogoManage").offset().top;
				
				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				
				$("#gridLogoManage").css({"height": + (gridResultHeight - 4) + "px"})	
		    }).resize();
		})
	</script>

	<style>
		/* layer popup */
        #addLogoPop{
            width: 485px;
        }
        .ui_input_hasinfo{
		    background-color: #f1f1f1;
		    cursor: not-allowed !important;
		}
		#gridLogoManage{
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
		        <div class="gridWrap">
			        <div id="gridLogoManage"></div>
		        </div>
	        </div>

        </div>
    </div>

    <div id="uploadLogoPop" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.logo.label.Logo"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
					<div class="ui_padding">
	                   	<label class="ui_label_essential"><spring:message code="admin.logo.label.Logo"/></label>
	                       	<select id="logoChangeUse" style="width:100px; display: block; float: none;">
	                       		<option value="N" selected="selected"><spring:message code="admin.logo.label.N"/></option>
	                       		<option value="Y"><spring:message code="admin.logo.label.Y"/></option>
	                       	</select>
	                    <label class="ui_label_essential"><spring:message code="admin.subNumber.label.logoSize"/></label>
	                       	<select id="logoSize" disabled="disabled" style="width:100px;">
	                       		<option value="200" selected="selected">200</option>
	                       		<option value="250">250</option>
	                       		<option value="300">300</option>
	                       		<option value="350">350</option>
	                       	</select>
	                       	<br/>
							<form id="uploadLogoFrm" method="post" enctype="multipart/form-data" action="${contextPath}/logoImgUpload/upLoadFile_hidden1/logoType_hidden" target="iframe1" >
		                 		<label class="ui_label_essential"><spring:message code="admin.subNumber.label.logoRegister"/></label>           
									<div class="filebox file_upload_wrap"  id="filebox1">
										<input class="upload_name upload_disabled ui_input_hasinfo" id="upLoad_name1" disabled="disabled" value="<spring:message code="views.search.alert.msg107"/>" readonly="readonly"/>
										<label class="ButtonLabel upload_disabled ui_input_hasinfo" disabled="disabled" for="upLoadFile_hidden1" style="padding-left:12px !important;padding-right:12px !important;"><spring:message code="views.search.alert.msg108"/></label>
										<input class="upload_hidden upload_disabled" id="upLoadFile_hidden1" disabled="disabled" type="file" name="upLoadFile_hidden1"  >
										<input type="hidden" id="logoType_hidden"/>
									</div>
				        	</form>
					</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="uploadLogoBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="admin.etcConfig.btn.set"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<iframe name="iframe1" style="display:none;"></iframe> 
</body>