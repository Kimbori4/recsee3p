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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/statistics/call_mobile.js"></script>
    <script>
    	var locale = "${locale}"
    </script>

	<script>
		$(function() {
			top.playerVisible(false);
		     $(window).resize(function() {
					// 현재 document 높이
					var documentHeight = $(window).height();

					var playerHeight = ($("#playerFrame").is(":visible") ? $('#playerFrame').contents().find(".rplayer").outerHeight() : 4);
					$("#pageFrame").css({"height": + (documentHeight - playerHeight) + "px"});
					
					// 그리드 위의 높이 값
					var calcHeight = $('#gridCallMobile').offset().top;
					var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
					var resultHeight = (documentHeight - calcHeight - pagingHeight);
					var totalRowHeight = $('#gridCallMobile').find(".ftr").height();
		           // height 값 문서 크기에 맞추기
		           $('#gridCallMobile').css({"height": + (resultHeight - playerHeight) + "px"});
		           $('#gridCallMobile').find(".objbox").css({"height": + (resultHeight - totalRowHeight) + "px"});
		           $('#gridCallMobile').css({"width": "100%"});
		     }).resize();
		 });

	</script>

    <style>
		#chart{
			margin: 0 auto;
			margin-top: 10px;
			margin-bottom: 10px;
			width: calc(100% - 20px);
			height: 250px;
		}
		/* #gridCallAll{
			height: 300px !important;
		} */
		.codeList{
			width:150px !important;
		}

		.select2-container--default .select2-selection--multiple .select2-selection__rendered{
			min-width: 300px !important;
		}
		.ui_float_left form span{
			float:left;
		}
		.dhxcombo_dhx_web{
			margin-top:4px;
		}
		.select2-container--default .select2-selection--multiple .select2-selection__rendered {
			min-width: 280px !important; 
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
                <div class="ui_pannel_row ui_pannel_row_border_none">
                    <div class="ui_float_left">
                       	<p class="ui_pannel_tit"><spring:message code="statistics.userStatistics.grid.title.mobileStatistics"/></p>
                    </div>
                    <div class="ui_float_left">
                       	<form>
                    		<input title='<spring:message code="search.option.startDate"/>' type="text" id="sDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" maxlength='8' placeholder="<spring:message code="search.option.startDate"/>"/>
				            <input title='<spring:message code="search.option.endDate"/>' type="text" id="eDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" maxlength='8' placeholder="<spring:message code="search.option.endDate"/>"/>
				            <select title='<spring:message code="statistics.userStatistics.grid.title.statisticsType"/>' id="divisionBy">
                            	<option value="detailBy"><spring:message code="statistics.userStatistics.grid.title.byDetail"/></option>   
				            	<option value="timeBy"><spring:message code="statistics.userStatistics.grid.title.byTime"/></option>
				            	<option value="dateBy"><spring:message code="statistics.userStatistics.grid.title.byDate"/></option>
                            	<option value="monthBy"><spring:message code="statistics.userStatistics.grid.title.byMonth"/></option>                            	
                            	<option value="groupBy"><spring:message code="statistics.userStatistics.grid.title.byGroup"/></option>
                            	<option value="userBy"><spring:message code="statistics.userStatistics.grid.title.byUser"/></option>                         	
                            </select>
				            <select title='<spring:message code="admin.label.searchKind"/>' id="codeFilter">
                            	<option value="mBgCode"><spring:message code="statistics.selectbox.bgFilter"/></option>
                            	<option value="mMgCode"><spring:message code="statistics.selectbox.mgFilter"/></option>
                            	<option value="mSgCode"><spring:message code="statistics.selectbox.sgFilter"/></option>
                            	<option value="selectPeople"><spring:message code="statistics.selectbox.peopleFilter"/></option>
                            </select>
                            <select title=<spring:message code="views.search.grid.head.R_BG_CODE"/> id="mBgCode"  class="codeList"></select>
							<select title=<spring:message code="views.search.grid.head.R_MG_CODE"/> id="mMgCode"  class="codeList" style="display:none;"></select>
							<select title=<spring:message code="views.search.grid.head.R_SG_CODE"/> id="mSgCode"  class="codeList" style="display:none;"></select>
							<select title='사번' id='selectPeople' class="codeList" style="display:none;" mode="checkbox"></select>
			            </form>
                    	<button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="search.option.search"/></button>
                    	<button id="excelDownBtn"class="ui_main_btn_flat icon_btn_exelTxt_white"><spring:message code="common.excel.download"/></button>
                    </div>
                    <div class="ui_float_right">
                    	<div id="contents_form"></div>
                    </div>
                </div>
            </div>
            <%-- <div class="recordInfoWrap ui_main_bg_flat">
            	<div class="report_txt_wrap">
					<p class="icon_record_val recordInfo_tit"><spring:message code="statistics.title.allCount" /></p>
					<p class="icon_record_time recordInfo_txt" id="allCalls"></p>
				</div>
				<div class="report_txt_wrap">
					<p class="recordInfo_tit"><spring:message code="statistics.title.allHour" /></p>
					<p class="recordInfo_txt" id="allTime"></p>
				</div>
            </div> --%>
			<div class="gridWrap">
				<div id="gridCallMobile"></div>
				<div id="pagingGridCall"></div>
			</div>
        </div>
    </div>
    
<iframe name="downloadFrame" id="downloadFrame" src="" style="display:none;"></iframe>
</body>
</html>
