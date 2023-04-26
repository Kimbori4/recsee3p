<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>
	<%-- body zoom lib --%>
	<script type="text/javascript" src="${compoResourcePath }/zoomooz/jquery.zoomooz.min.js"></script>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/monitoring/realtime.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/websocket.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/realtime_common.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/realtime.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/audio_api.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/Queue.js"></script>
	<script type="text/javascript" src="${compoResourcePath }/recsee_player_monitoring/recsee_player.js"></script>


	<%-- real time listening 추후에 chrome 풀어주기 --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/playerctrl.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/log_window.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/main.css" /> --%>
<%-- 	<script type="text/javascript" src="${compoResourcePath}/streaming/js/player_controls.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/log_window.js"></script> --%>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js"></script>
	<%-- <script type="text/javascript" src="${compoResourcePath}/streaming/js/audio_player.js"></script> --%>
<%-- 	<script type="text/javascript" src="${compoResourcePath}/streaming/js/waveform.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/format_reader.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/mpeg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/ogg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/wav.js"></script> --%>


	<script>

	var listenIp = "${listenIp}";
	var ctiUse = "${ctiUse}";
	var channelInfo = JSON.parse('${channelInfo}');
	/*
		이쪽에.. 모니터링 뷰 주석하면 visibleTabNo이랑
		mode_cont data-target 이랑 맞춰서 display 켜주는거 꼬여버리네용..
	*/
		$(function() {
			
		
			var PopupCheck = <%= request.getAttribute("PopupCheck") %>;
			if(PopupCheck == true){
				$(".main_lnb").hide();
				$(".main_header").hide();
				
			}
		
		
			
			try{
			top.playerVisible(false);
			}catch (e) {
				// TODO: handle exception
			}
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - pagingHeight);

				$("#gridMonitoringView").css({"height": + (gridResultHeight - 175) + "px"})
		    }).resize();
		    
		    
		    
		 });
	
		
	
	</script>

	<style>
		#gridMonitoringView{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
        }
        #gridMonitoringView td{
            font-size: 16px !important;
        }
        #bottomFixedPlayer{
            width: 440px !important;
		    height: auto;
		    position: fixed;
		    top: calc(100% - 190px);
		    left: 8px;
		    /* margin-top: -190px; */
		    z-index: 99999;
		    border: 1px solid #dddddd;
		    box-shadow: 0px 0px 15px rgba(0,0,0,0.4);
	        border: 1px solid rgba(0,0,0,0.4);
		}
		#bottomFixedPlayer .rplayer{
            min-width: 466px !important;
		}
		.gridListening{
			background-color: #ff7e7e !important;
		}
		.hardware_info{
			display: none;
		}
		.codeList{
			width:150px !important;
		}

		.textArea{
			background-color:#ffffff;
			clear:both;
			float:left;
			width:315px;
			height:175px;
			resize: none;
			overflow-y:auto;
			border:1px solid #dddddd;
			padding:5px;
		}

		.textArea a:hover{
			text-decoration:underline;
			cursor:pointer !important;
			color:rgba(45, 113, 196, 1);
		}
		.anget_mgname{
			text-overflow: ellipsis;
			overflow: hidden;
			white-space: nowrap;
		}
	</style>
</head>
<body>
	<canvas id="canvas" width="1000" height="325" style="display:none;"></canvas>
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
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_left">
                        <p class="now_system"  style="display:none;">
                            <span class="now_system_tit"><spring:message code="monitoring.display.selectedSystem"/></span>
                            <span class="now_system_info">A001</span>
                        </p>
                            <select id="systemCode" required  style="display:none;">
                            </select>
                            <select id="codeFilter" class="codefilter_select">
                            	<option value="mBgCode"><spring:message code="statistics.selectbox.bgFilter"/></option>
                            	<option value="mMgCode"><spring:message code="statistics.selectbox.mgFilter"/></option>
                            	<option value="mSgCode"><spring:message code="statistics.selectbox.sgFilter"/></option>
                            	<option value="searchAgentName"><spring:message code="monitoring.display.filterByAgent"/></option>
                            	<option value="searchAgentNum"><spring:message code="monitoring.display.filterByNum"/></option>
                            	<option value="searchAgentExt"><spring:message code="monitoring.display.filterByExt"/></option>
                            </select>

                            <select id="codeFilterResize" style="display:none;">
                            	<option id="codeFilterResizeOption"></option>
                            </select>

                            <select id="mBgCode"  class="codeList codefilter_select"><option disabled selected>대분류</option></select>
														<select id="mMgCode"  class="codeList codefilter_select" style="display:none;"><option disabled selected>중분류</option></select>
														<select id="mSgCode"  class="codeList" style="display:none;"></select>
														<select id="searchAgentName"  class="codeList" style="display:none;"></select>
														<select id="searchAgentNum"  class="codeList" style="display:none;"></select>
														<select id="searchAgentExt"  class="codeList" style="display:none;"></select>
                        <button id="mSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="monitoring.display.search"/></button>
                    </div>
                    <div class="ui_float_right">
                    	<button id="btnListenAlway" class="ui_sub_btn_flat icon_btn_headset_white"><spring:message code="monitoring.menu.continueTapping"/></button>
						<button id="btnMonitSetOpen" class="icon_btn_gear_gray"><spring:message code="monitoring.display.monitoringOption"/></button>
                    </div>
                </div>
            </div>

	        <div class="monitoring_option" id="monitoring">

	        	<div class="monitoring_option_header">
	        		<p><spring:message code="monitoring.display.monitoringOption"/></p>
                    <button id="btnMonitSetExit" class="ui_btn_white icon_btn_exit_gray"></button>
	        	</div>

	            <div class="display_option">
	            	<div class="setting_tit_wrap">
		                <p class="ui_main_txt display_option_tit"><spring:message code="monitoring.display.statusViewOptions"/></p>
	                </div>
					<div class="setting_dropdown_cont">
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.login"/></span>
		                    <div class='onoffswitch check_status_login'>
		                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusLogin' checked/>
		                        <label class='onoffswitch-label' for='selStatusLogin'></label>
		                    </div>
		                </div>
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.ready"/></span>
		                    <div class='onoffswitch check_status_ready'>
		                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusReady' checked/>
		                        <label class='onoffswitch-label' for='selStatusReady'></label>
		                    </div>
		                </div>
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.calling"/></span>
		                    <div class='onoffswitch check_status_calling'>
		                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusCalling' checked/>
		                        <label class='onoffswitch-label' for='selStatusCalling'></label>
		                    </div>
		                </div>
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.ACW"/></span>
		                    <div class='onoffswitch check_status_aftercallwork'>
		                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusaftercallwork' checked/>
		                        <label class='onoffswitch-label' for='selStatusaftercallwork'></label>
		                    </div>
		                </div>
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.logout"/></span>
		                    <div class='onoffswitch check_status_logout'>
		                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusLogout' checked/>
		                        <label class='onoffswitch-label' for='selStatusLogout'></label>
		                    </div>
		                </div>
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.away"/></span>
		                    <div class='onoffswitch check_status_away'>
		                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusNotready' checked/>
		                        <label class='onoffswitch-label' for='selStatusNotready'></label>
		                    </div>
		                </div>
	            	</div>
            	</div>

	            <div class="display_option">
	            	<div class="setting_tit_wrap">
		                <p class="ui_main_txt display_option_tit"><spring:message code="monitoring.display.displayOptions"/></p>
	                </div>

					<div class="setting_dropdown_cont">
		                <div class="option_check" id="selDisplayIconBox">
		                    <span class="onofftit"><spring:message code="monitoring.display.icon"/></span>
		                    <div class='onoffswitch'>
		                        <input type='checkbox' name='selDisplay' class='onoffswitch-checkbox' id='selDisplayIcon' checked/>
		                        <label class='onoffswitch-label' for='selDisplayIcon'></label>
		                    </div>
		                </div>
		                <div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.display.agentId"/></span>
		                    <div class='onoffswitch'>
		                        <input type='checkbox' name='selDisplay' class='onoffswitch-checkbox' id='selDisplayUserId' checked/>
		                        <label class='onoffswitch-label' for='selDisplayUserId'></label>
		                    </div>
		                </div>
	                </div>
	            </div>
	        <div class="listenAlwaysPannel" id="">

	        	<div class="monitoring_option_header">
	        		<p><spring:message code="monitoring.display.listenAlways"/></p>
                    <button id="btnListenAlwayExit" class="ui_btn_white icon_btn_exit_gray"></button>
	        	</div>

 				<div class="display_option">
	                <div class="setting_dropdown_cont">
	                	<div class="option_check">
		                    <span class="onofftit"><spring:message code="monitoring.state.save"/></span>
		                    <div class='onoffswitch'>
		                        <input type='checkbox' name='statusSave' class='onoffswitch-checkbox' id='statusSave' checked/>
		                        <label class='onoffswitch-label' for='statusSave'></label>
		                    </div>
		                </div>
	                </div>
 				</div>

	        	<div class="">
	        		<div class="stay_check_wrap monitoring_check">
	        				<input class="office_stay_check" type="checkbox"/>
							<label><spring:message code="monitoring.menu.numberContinueTapping"/></label>
						<div style="margin-top:45px;">
							<input class="custPhoneContinueChk" type="checkbox"/>
							<label><spring:message code="monitoring.menu.custNumberContinueTapping"/></label></br></br>
							<input type="text" id="custPhoneContinue" placeholder="<spring:message code="monitoring.menu.custNumberOrNumber"/>"/>
						</div>
					</div>
                </div>
			</div>


			<div class="monitoring_contents">
                <div class="mode_cont" id="viewCardMode">
				</div>
			</div>
			<div id="bottomFixedPlayer" class="recsee_player">
			</div>
			<%-- @saint: 플레이어 끝 --%>

		</div>
	</div>
	<div id="realTimeMemo" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="monitoring.menu.memo"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<textarea class="textArea" contentEditable="true" id="realTimeMemoContents"></textarea>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
					</div>
					<div class="ui_float_right">
						<input type="hidden" id="proc"  value="">
						<button id="realTimeMemoAdd" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="monitoring.menu.save"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="dialog-confirm" class="alert_title ">
		<p class="alert_contents displaynone"></p>
	</div>
</body>
