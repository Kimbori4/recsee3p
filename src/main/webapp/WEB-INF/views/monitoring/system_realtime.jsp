<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../common/include/commonVar.jsp" %>

<!DOCTYPE html>
<html>
<head>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/monitoring/system_realtime.css" />

	<%-- js page --%>
  	<script src="${compoResourcePath}/d3/d3.v3.min.js"></script>
  	<%-- <script src="${compoResourcePath}/d3/d3.v2.js"></script> --%>
  	<script src="${compoResourcePath}/d3/liquidFillGauge/liquidFillGauge.js"></script>
  	<script src="${compoResourcePath}/d3/cubism/cubism.v1.js"></script>
  	<script src="${compoResourcePath}/d3/cubism/highlight.min.js"></script>
  	
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/system_realtime.js"></script>

	<script>
		var redisIp = "${redisIp}";
		var redisPort = "${redisPort}";
		var redisPw = "${redisPw}";
		var redisTimeout = "${redisTimeout}";
		
		$(function(){
			top.playerVisible(false);
			ui_controller();
		})
	</script>
	<style>
	#setLimitPopup{
       	width  : 400px;
    }
    
    #sysRedisIP{
		width:120px;
	    float: left;
	    padding: 3px;
	    -webkit-padding-before: 6px;
	    -webkit-padding-after: 6px;
	    margin: 3.5px;
	    font-size: 14px;
	    color: #333333;
	    letter-spacing: -1px;
	    padding-right:20px;
	    overflow:hidden;
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
    <div class="main_contents">
        
    </div>
   	<div class="ui_layout_pannel">
       	<div class="ui_article">
            <div class="headerWrap">
            	<div class="ui_main_bg_flat">
            		<%-- <spring:message code="monitoring.system.realtime.title"/> --%>시스템 모니터링
		         </div>
		         <div class="ui_pannel_row">
					<div class="ui_float_left">
						<select id="sysRedisIP"></select>
						<button id="setSysRedisIPBtn" class="ui_main_btn_flat">검색</button>
					</div>
				    <div class="ui_float_right">
				        <button id="btnLimitSetOpen" class="icon_btn_gear_gray">시스템 모니터링 설정</button>
				    </div>
				</div>
		         	<span id="mode" style="display:none"></span>
            </div>
			<div class="chartWrap"></div>
			<div class='chartWrap_cubism' id='testC'></div>
		</div>
	</div>
	
	
    <div id="setLimitPopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">임계치 설정</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential">CPU</label>
                        	<input type="text" id="cpuLimit" maxlength="5"/>
                    	<label class="ui_label_essential">Memory</label>
                        	<input type="text" id="memoryLimit" maxlength="5"/>
                    	<label class="ui_label_essential">Disk</label>
                        	<input type="text" id="diskLimit" maxlength="5"/>
                    </div>
                </div>
            </div>
            
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="setLimitBtn" class="ui_main_btn_flat">저장</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>


<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/system_realtime.js"></script>
</html>
