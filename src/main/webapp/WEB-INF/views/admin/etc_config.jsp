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
	<%-- <link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" /> --%>

	<%-- js page --%>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/admin/etc_config.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이d
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridEtcConfigManage").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#gridEtcConfigManage").css({"height": + (gridResultHeight - 4) + "px"})
				
				
				
				$("#gridPrefixManage").css({"height":"200px"});
		    }).resize();
		})
	</script>

	<style>
		#gridEtcConfigManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }

        #modifyConfig{
        	width: 400px;
        }
	</style>
</head>
<body onload="etcConfigManageLoad()">

    <div class="ui_layout_pannel">

    	<div class="main_contents admin_body">

	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <div class="main_form">
	                        <select id="groupKey" required>
	                            <option value="" disabled selected><spring:message code="admin.label.selectGroupKey"/></option>
	                        </select>
	                        <select id="configKey" required style="width:250px !important;">
	                            <option value="" disabled selected><spring:message code="admin.label.selectEtcKey"/></option>
	                        </select>
	                        <input id="configValue" type="text" style="width:300px !important;" placeholder=""/>
	                       	<button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>
	                    	<%-- <button id="addPopBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.add"/></button> --%>
	                    </div>
	                </div>
	                    <div style="
						    float: right;
						    margin-right: 17px;
						"><button id="etc_insert_btn" onclick="insertPopOpen()">추가</button></div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridEtcConfigManage"></div>
			        <div id="pagingEtcConfigManage"></div>
		        </div>
	        </div>

        </div>
    </div>
    
    <div class="ui_layout_pannel">
    	<div class="main_contents admin_body" style="float:left;">
	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <div class="main_form" style="width:850px;">
	                    	<input type="text" style="width:20%; border:none; background-color:#f7f7f7;" readonly="readonly" value="<spring:message code="admin.etcConfig.label.prefix"/>"/>
		                    <select id="usePrefix">
								<option value="Y"><spring:message code="admin.etcConfig.option.use"/></option>
								<option value="N"><spring:message code="admin.etcConfig.option.nouse"/></option>
							</select>
	                        
	                        <div id="prefixBox" style="display: inline;">
								<input id="prefixVal" type="text" style="width:30%;" />
								<button id="addPrefix" class=""
									style="width: 60px; height: 28px; padding: 6px 10px; margin:3.5px; background-color: rgb(96, 179, 220); color: white; display: inline;"
									onclick="addPrefixVal();"><spring:message code="admin.etcConfig.btn.add"/></button>
								<button id="delPrefix" class=""
									style="width: 60px; height: 28px; padding: 6px 10px; margin:3.5px; background-color: rgb(96, 179, 220); color: white;  display: inline;"
									onclick="delPrefixVal();"><spring:message code="admin.etcConfig.btn.del"/></button>
							</div>
	                    </div>
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridPrefixManage" style="border:1px solid #efefef"></div>
		        </div>
	        </div>
        </div>
    </div>
    
	<div class="ui_layout_pannel">
    	<div class="main_contents admin_body" style="float:left;">
	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <div class="main_form" style="width:850px;">
	                    	<input type="text" style="width:21%; border:none; background-color:#f7f7f7;" readonly="readonly" value="<spring:message code="admin.etcConfig.label.masking"/>"/>
		                    <select id="usePNumMasking">
								<option value="Y"><spring:message code="admin.etcConfig.option.use"/></option>
								<option value="N"><spring:message code="admin.etcConfig.option.nouse"/></option>
							</select>
	                        
	                        <div id="pNumMaskingBox" style="display: inline;">
	                    		<input type="text" style="width:8%; border:none; background-color:#f7f7f7; text-align: right;" readonly="readonly" value="<spring:message code="admin.etcConfig.label.masking.from"/>"/>
								<input id="startIdx" type="text" style="width:5%;" />
								<input type="text" style="width:10%; border:none; background-color:#f7f7f7; text-align: right;" readonly="readonly" value="<spring:message code="admin.etcConfig.label.masking.count"/>"/>
								<input id="maskingEA" type="text" style="width:5%;" />
								
								<button id="pNumMasking" class=""
									style="width: 60px; height: 28px; padding: 6px 10px; margin:3.5px; background-color: rgb(96, 179, 220); color: white; display: inline;"
									onclick="setMaskingVal();"><spring:message code="admin.etcConfig.btn.set"/></button>
							</div>
	                    </div>
	                </div>
	            </div>
	        </div>
        </div>
    </div>
    
	<div class="ui_layout_pannel">
    	<div class="main_contents admin_body" style="float:left;">
	        <div class="ui_acrticle">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
	                    <div class="main_form" style="width:850px;">
	                    	<input type="text" style="width:21%; border:none; background-color:#f7f7f7;" readonly="readonly" value="<spring:message code="admin.etcConfig.label.hyphen"/>"/>
		                    <select id="useHyphen" style="margin-right:5px;">
								<option value="Y"><spring:message code="admin.etcConfig.option.use"/></option>
								<option value="N" selected="selected"><spring:message code="admin.etcConfig.option.nouse"/></option>
							</select>
	                        
	                        <div id="setHyphenBox" style="display: none;">
		                        <select id="setHyphen1">
									<option value="2"><spring:message code="admin.etcConfig.option.hyphen.val2"/></option>
									<option value="3"><spring:message code="admin.etcConfig.option.hyphen.val3"/></option>
									<option value="4"><spring:message code="admin.etcConfig.option.hyphen.val4"/></option>
									<option value="Y"><spring:message code="admin.etcConfig.option.direct"/></option>
									<option value="N"><spring:message code="admin.etcConfig.option.nouse"/></option>
								</select>
								<input type="text" id="setHyphen1Val" style="width:5%; display:none;" value=""/>
								<input type="text" style="width:13px; border:none; background-color:#f7f7f7;" readonly="readonly" value="-"/>
								<select id="setHyphen2">
									<option value="2"><spring:message code="admin.etcConfig.option.hyphen.val2"/></option>
									<option value="3"><spring:message code="admin.etcConfig.option.hyphen.val3"/></option>
									<option value="4"><spring:message code="admin.etcConfig.option.hyphen.val4"/></option>
								</select>
		                        <input type="text" style="width:7%; border:none; background-color:#f7f7f7;" readonly="readonly" value="- XXXX"/>
		                        								
								<button id="setHyphen" class=""
									style="width: 60px; height: 28px; padding: 6px 10px; margin:3.5px; background-color: rgb(96, 179, 220); color: white; display: inline;"
									onclick="setHyphenVal();"><spring:message code="admin.etcConfig.btn.set"/></button>
							</div>
	                    </div>
	                </div>
	            </div>
	        </div>
        </div>
    </div>
    
    
    <div id="modifyConfig" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.label.groupKey"/></label>
                        	<input id="mGroupKey" type="text" readonly="readonly"/>
                    	<label class="ui_label_essential"><spring:message code="admin.label.etcKey"/></label>
                        	<input id="mConfigKey" type="text" readonly="readonly"/>
                    	<label class="ui_label_essential"><spring:message code="admin.label.etcValue"/></label>
                        	<span id="mConfigValue"></span>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<button id="addBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.save"/></button>
                        <button id="modifyBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.modify"/></button>
                        <button id="delBtn" class="ui_btn_white icon_btn_trash_gray"></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="insertConfig" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.label.groupKey"/></label>
                        	<input id="gk" type="text">
                    	<label class="ui_label_essential"><spring:message code="admin.label.etcKey"/></label>
                        	<input id="ck" type="text">
                    	<label class="ui_label_essential"><spring:message code="admin.label.etcValue"/></label>
                        	<input id="cv" type="text">
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<button id="addBtn" class="ui_main_btn_flat icon_btn_cube_white" onclick="etc_insert_action()"><spring:message code="message.btn.save"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>