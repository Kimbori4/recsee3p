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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/detail_manage.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/detail_manage.js"></script>

	<style>
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
               	<div class="detail_manage_tit"><p><spring:message code="admin.detail.title.recordingEngine"/></p></div>
                <div class="detail_manage_pannel detail_engine_pannel">
	                <button class="btn_restart ui_btn_white ui_main_btn_flat" onclick=""><spring:message code="admin.button.restart"/></button>
                </div>
        	</div>

	        <div class="ui_acrticle">
               	<div class="detail_manage_tit"><p><spring:message code="admin.detail.title.etcSetting"/></p></div>
                <div class="detail_manage_pannel detail_etc_pannel">
                	<form class="detail_manage_form">
						<label><spring:message code="admin.detail.label.ipktsAliveCheck"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.use"/></option>
	                            <option value=""><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
						<label><spring:message code="admin.detail.label.sipLog"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.use"/></option>
	                            <option value=""><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                    <div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.lanCard"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.choose"/></option>
	                        </select>
						<label><spring:message code="admin.detail.label.encodedFileDonwload"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.use"/></option>
	                            <option value=""><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                    <div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.agentIdView"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.use"/></option>
	                            <option value=""><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
						<label><spring:message code="admin.detail.label.haName"/></label>
                       		<input type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.fileExtension"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.mp3"/></option>
	                            <option value=""><spring:message code="admin.detail.option.wav"/></option>
	                        </select>
						<label><spring:message code="admin.detail.label.guideSpeechRecording"/></label>
	                        <select>
	                            <option value=""><spring:message code="admin.detail.option.use"/></option>
	                            <option value=""><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                    <p class="file_name_sample">Date(YYYYMMDDhhmmss)-Agent ID</p>
	                    <div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.filenamePattern"/></label>
	                        <select class="select_file_pattern">
	                            <option value=""><spring:message code="admin.detail.option.cid"/></option>
	                        </select>
	                        <select class="select_file_pattern">
	                            <option value=""><spring:message code="admin.detail.option.callType"/></option>
	                        </select>
	                        <select class="select_file_pattern">
	                            <option value=""><spring:message code="admin.detail.option.callType"/></option>
	                        </select>
                	</form>
	                <div class="ui_clear_both"></div>
	                <button class="ui_btn_white ui_main_btn_flat icon_btn_save_white" onclick=""><spring:message code="admin.button.save"/></button>
                </div>
        	</div>

	        <div class="ui_acrticle">
               	<div class="detail_manage_tit"><p><spring:message code="admin.detail.title.uploadRecordingFiles"/></p></div>
                <div class="detail_manage_pannel detail_voicefile_pannel">
                	<form class="detail_manage_form">
						<label><spring:message code="admin.detail.label.uploadFile"/>1</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/>
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/> 2</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/>3</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/>4</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/>5</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/> 6</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/>7</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/>8</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.uploadFile"/>9</label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                	</form>
                    <p class="info_txt ui_float_right"><spring:message code="admin.detail.label.maxFileSize"/> : 100M</p>
	                <button class="ui_clear_both ui_btn_white ui_main_btn_flat icon_btn_save_white" onclick="">Save</button>
                </div>
        	</div>

	        <div class="ui_acrticle">
               	<div class="detail_manage_tit"><p><spring:message code="admin.detail.title.uploadUpdateFile"/></p></div>
                <div class="detail_manage_pannel detail_updatefile_pannel">
                	<form class="detail_manage_form">
						<label><spring:message code="admin.detail.label.updateFile"/></label>
	                		<div class="filebox file_upload_wrap">
	                			<input class="upload_name" value="<spring:message code="admin.detail.placeholder.selectFile"/>"/> 
								<label for="upLoadFile"><spring:message code="admin.detail.label.upload"/></label>
	                          	<input type="file" id="upLoadFile" class="upload_hidden">
	                        </div>
						<label class="label_meno"><spring:message code="admin.detail.label.memo"/></label>
                       		<input class="input_meno" value="" type="text"/>
                	</form>
                    <div class="ui_clear_both"></div>
                    <p class="info_txt ui_float_right"><spring:message code="admin.detail.label.maxFileSize"/> : 100M</p>
                    <div class="ui_clear_both"></div>
	                <button class="ui_btn_white ui_main_btn_flat icon_btn_save_white" onclick=""><spring:message code="admin.button.save"/></button>
                </div>
        	</div>

	        <div class="ui_acrticle">
               	<div class="detail_manage_tit"><p><spring:message code="admin.detail.title.mailServerData"/></p></div>
                <div class="detail_manage_pannel">
                	<form class="detail_manage_form">
						<label><spring:message code="admin.detail.label.mailServerAddress"/></label>
                       		<input value="" type="text"/>
						<label><spring:message code="admin.detail.label.mailSendPort"/></label>
                       		<input value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.mailSendId"/></label>
                       		<input value="" type="text"/>
						<label><spring:message code="admin.detail.label.mailSendPw"/></label>
                       		<input value="" type="text"/>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.mailSenderAddress"/></label>
                       		<input value="" type="text"/>
						<label><spring:message code="admin.detail.label.mailReceiverAddress"/></label>
                       		<input value="" type="text"/>
                	</form>
                	<div class="ui_clear_both"></div>
	                <button class="ui_btn_white ui_main_btn_flat" onclick=""><spring:message code="admin.button.sendTestMail"/></button>
                </div>
        	</div>

			<div class="ui_acrticle">
		       	<div class="detail_manage_tit"><p><spring:message code="admin.detail.title.serverTimezoneManage"/></p></div>
		        <div class="detail_manage_pannel detail_timezone_pannel">
		        	<form class="detail_manage_form">
			            <p class="timezone_sample"><spring:message code="admin.detail.label.nowTimezone"/> : Asia/Seoul - Wed Mar 15 11:58:47 KST 2017</p>
                       	<div class="ui_clear_both"></div>
						<label><spring:message code="admin.detail.label.changeServerTimezone"/></label>
			                <select class="select_timezone">
			                    <option value=""><spring:message code="admin.detail.option.cid"/></option>
			                </select>
			                <select class="select_timezone">
			                    <option value=""><spring:message code="admin.detail.option.callType"/></option>
			                </select>
			                <select class="select_timezone">
			                    <option value=""><spring:message code="admin.detail.option.callType"/></option>
			                </select>
		        	</form>
			        <button class="ui_clear_both ui_btn_white ui_main_btn_flat icon_btn_save_white" onclick=""><spring:message code="admin.button.save"/></button>
		        </div>
		    </div>
	    </div>
    </div>





    <div id="addChannel" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.channel.title.createChannel"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.channelNo"/></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_null"></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.system"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.ext"/></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.ip"/></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.usageRec"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.recKind"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.recType"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="admin.button.createChannel"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="autoAddChannel" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.channel.title.autoCreateChannel"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.channelNo"/></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_null"></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.system"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.ext"/></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.ip"/></label>
                        	<input class="" id="itemCode" value="" type="text"/>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.usageRec"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.recKind"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.recType"/></label>
	                        <select>
	                            <option value="a"></option>
	                        </select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_left ui_chkbox_group">
                       	<input class="ui_input_chkbox" id="itemCode" value="" type="checkbox"  placeholder="I<spring:message code="admin.channel.placeholder.ip"/>"/>
                    	<label class="ui_label_null"><spring:message code="admin.channel.label.removeChannel"/></label>
                    </div>
                    <div class="ui_float_right">
                        <button class="ui_main_btn_flat icon_btn_gear_white"><spring:message code="admin.button.autoCreateChannel"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<div class="message_area">
	</div>
</body>