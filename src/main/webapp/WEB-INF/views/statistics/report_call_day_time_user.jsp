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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/statistics/statistics.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/statistics/call_day_time_user.js"></script>

	<script>
		$(function(){
			top.playerVisible(false);
			ui_controller();
		})
	</script>

    <style>
    	#chartDaily, #chartRecfile, #chartWeekly, #chartStorage{
    		width: 100%;
    		height: 100%;
    		float: left;
    	}
    	.chartWrap{
    	    border-bottom: 1px solid #dddddd;
    	}
    	.chartWrap_harf{
		    float: left;
		    width: 45%;
		    height: 400px;
		    margin-left: 3%;
		    margin-bottom: 25px;
    	}
    	.chartWrap_harf:last-child{
    		border-right: 0px;
    	}
    	.chartWrap svg {
			width:100%;
		}

		.select2-container--default .select2-selection--multiple .select2-selection__rendered{
			min-width: 300px !important;
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
        <div class="ui_layout_pannel">
        	<div class="ui_article">
	            <div class="recordInfoWrap ui_main_bg_flat">
	            	<div class="report_txt_wrap">
						<p class="icon_record_val recordInfo_tit"><spring:message code="statistics.title.allCount" /></p>
						<p class="icon_record_time recordInfo_txt" id="allCalls"></p>
					</div>
					<div class="report_txt_wrap">
						<p class="recordInfo_tit"><spring:message code="statistics.title.allHour" /></p>
						<p class="recordInfo_txt" id="allTime"></p>
					</div>
					<div class="report_txt_wrap">
						<p class="recordInfo_tit"><spring:message code="statistics.title.inbound" /></p>
						<p class="icon_record_time recordInfo_txt" id="inboundCount"></p>
					</div>
					<div class="report_txt_wrap">
						<p class="recordInfo_tit"><spring:message code="statistics.title.outbound" /></p>
						<p class="icon_record_time recordInfo_txt" id="outboundCount"></p>
					</div>
					<div class="ui_pannel_row" id="dash">
                       	<form>
                            <select title='<spring:message code="monitoring.display.recServer"/>' id="sysCode"></select>
				            <select title='<spring:message code="admin.label.searchKind"/>' id="codeFilter">
                            	<<option value="mBgCode"><spring:message code="statistics.selectbox.bgFilter"/></option>
                            	<option value="mMgCode"><spring:message code="statistics.selectbox.mgFilter"/></option>
                            	<option value="mSgCode"><spring:message code="statistics.selectbox.sgFilter"/></option>
                            	<option value="selectPeople"><spring:message code="statistics.selectbox.peopleFilter"/></option>
                            </select>
                            <select title=<spring:message code="views.search.grid.head.R_BG_CODE"/> id="mBgCode"  class="codeList" style="display:none; width:300px;"></select>
							<select title=<spring:message code="views.search.grid.head.R_MG_CODE"/> id="mMgCode"  class="codeList" style="display:none;"></select>
							<select title=<spring:message code="views.search.grid.head.R_SG_CODE"/> id="mSgCode"  class="codeList" style="display:none;"></select>
							<select title='사번' id='selectPeople' class="codeList" style="display:none;" mode="checkbox"></select>
			            </form>
			            <button id="searchBtn" class="ui_sub_btn_flat icon_btn_search_white"><spring:message code="search.option.search"/></button>
			         </div>
	            </div>
				<div class="chartWrap">
					<div class="chartWrap_harf">
						<div id="chartDaily"></div>
					</div>
					<div class="chartWrap_harf">
						<div id="chartWeekly"></div>
					</div>
				</div>
				<div class="chartWrap">
						<div id="chartRecfile"></div>
				</div>
			</div>
        </div>
    </div>
</body>
</html>
