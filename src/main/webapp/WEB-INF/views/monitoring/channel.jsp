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
	<script type="text/javascript" src="${compoResourcePath }/zoomooz/jquery.zoomooz.min.js"></script>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/monitoring/realtime.css" />
	<%-- <link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/monitoring/channel.css" /> --%>

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/websocket.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/realtime_common.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/channel_gridView.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/audio_api.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/Queue.js"></script>
	<script type="text/javascript" src="${compoResourcePath }/recsee_player_monitoring/recsee_player.js"></script>


	<%-- real time listening --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/playerctrl.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/log_window.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/main.css" />
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/player_controls.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/log_window.js"></script> --%>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js"></script>
	<%-- <script type="text/javascript" src="${compoResourcePath}/streaming/js/audio_player.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/waveform.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/format_reader.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/mpeg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/ogg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/wav.js"></script> --%>


	<script>
	var listenIp = "${listenIp}";
	var ctiUse = "${ctiUse}";
	var channelInfo = JSON.parse('${channelInfo}');
	/**
		이쪽에.. 모니터링 뷰 주석하면 visibleTabNo이랑
		mode_cont data-target 이랑 맞춰서 display 켜주는거 꼬여버리네용..
	*/
		$(function() {
			top.playerVisible(false);
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - pagingHeight);

				$("#gridChannel").css({"height": + (gridResultHeight - 175) + "px"})
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

		}

		#realTimeMemo{
			width : 300px;
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
		
		div.gridbox_dhx_web.gridbox table.hdr td {
			font-size: 16px !important;
		}
		
		div.gridbox_dhx_web.gridbox table.obj tr td {
			font-size: 16px !important;
		}
		
		#btnTimerSetOpen,#btnMonitSetOpen{
			float: right;
		}
		
		.ui_pannel_row button{
		    background-color: rgb(96, 179, 220);
		    border-color: rgb(96, 179, 220);
		    color: rgb(255, 255, 255);
		    cursor: pointer;
		    transition: all 0.3s ease 0s;
		}
		
		.grid_status_calling {
		    background-color: #1b00ff7d  !important;
		    color: #ffffff !important;
		}
		
		.recodeButton{
			color : black !important;
			text-indent:-10000px
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
                        <p class="now_system" style="display:none;">
                            <span class="now_system_tit"><spring:message code="monitoring.display.selectedSystem"/></span>
                            <span class="now_system_info">A001</span>
                        </p>
                        <form>
                            <select id="systemCode" required  style="display:none;">
                                <option value="" disabled  ><spring:message code="monitoring.display.system"/></option>
                            </select>
                            <select id="serverFilter">
                            	<option disabled="disabled" selected="selected" value="">SERVER</option>
                            </select>
                            
                            <select id="redisFilter">
                            </select>
                            
                        </form>
                        <button id="mSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="monitoring.display.search"/></button>
                    </div>
                    <div class="ui_float_right">    
                    	<button id="btnMonitSetOpen" class="icon_btn_gear_white"><spring:message code="monitoring.display.monitoringOption"/></button>                   		               	
						<button id="btnTimerSetOpen" class="icon_btn_gear_white" onclick="TimerSetting();"><spring:message code="monitoring.display.timerOption"/></button>
						<select id="timeValue" style="width: 40px; height: 30px; margin-top: 3px;" required>						   
							<option value="1">1</option>
							<option value="2">2</option>
                            <option selected="selected" value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                        </select>                        
                        
                    </div>
                </div>
            </div>
            
            <div class="monitoring_contents">
                <div class="mode_cont" id="viewCardMode">
				</div>
			</div>
            
			
			
			<div class="monitoring_contents">

               <div class="mode_cont" id="viewGridMode" style="display: none">
                    <div class="gridWrap">
                        <div id="gridMonitoringView"></div>
                        <div id="pagingMonitoringView" style="border-bottom: 1px solid #dddddd !important;"></div>
                    </div>
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
            	</div>
           	</div>
		</div>
	
	</div>	

</body>