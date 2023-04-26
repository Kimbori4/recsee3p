<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/include/commonVar.jsp"%>

<%-- css page --%>
 <link rel="stylesheet" type="text/css"	href="${recseeResourcePath }/css/page/evaluation_master/evaluation.css" />

<%-- js page --%>
<script type="text/javascript"
	src="${recseeResourcePath }/js/page/evaluation_master/evaluationStatistics.js"></script>

<%-- <script type="text/javascript" src="${compoResourcePath }/recsee_player/recsee_player.js"></script> --%>
<script>
$(function() {
	top.playerVisible(false);
	$(window).resize(function() {

		// 현재 document 높이
		var documentHeight = $(window).height();

		// 그리드 위의 높이 값
	    var gridCalcHeight = $("#gridEvaluationStatistics").offset().top;

		// 페이징이 있음 페이징 만큼 뺴주깅
		var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
		var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
		$("#gridEvaluationStatistics").css({"height": + (gridResultHeight - 2) + "px"});
	}).resize();

})
</script>
<style>
#gridEvaluationStatistics {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
}

.arrow{
	width: 30px;
	height: 30px;
	background-repeat: no-repeat;
	margin-top: -18%;
	margin-right: -25%;
}
.arrow2{
	width: 30px;
	height: 30px;
	background-repeat: no-repeat;
	margin-right: -35%;
	float:	left;
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
				<div class="ui_tabbar_section">
					<div class="tabbar_cont">
						<div class="ui_article">
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<select id="selectPart">
											<option value="" disabled selected><spring:message code="evaluation.campaign.selectType" /></option>
											<option value="part">파트별</option>
											<option value="job">근무별</option>
											<option value="person">개인별</option>
											<option value="person2">평가DATA</option>
											<option value="person3">개인별평가DATA</option>
										</select>
										<select id="selectPeriod">
											<option value="day" selected><spring:message code="evaluation.selectPeriod.day"/></option>
											<option value="month"><spring:message code="evaluation.selectPeriod.month"/></option>
											<option value="quarter"><spring:message code="evaluation.selectPeriod.querter"/></option>
											<option class="person3Year" value="year" hidden><spring:message code="evaluation.selectPeriod.year"/></option>
										</select>
										<select id="selectYear" style="display: none;">
											<option value="" disabled selected>년도 선택</option>
										</select>
										<select id="perMonth" style="display: none;">
											<option value="" disabled selected>전체</option>
											<option value="01"><spring:message code="evaluation.month.1"/></option>
											<option value="02"><spring:message code="evaluation.month.2"/></option>
											<option value="03"><spring:message code="evaluation.month.3"/></option>
											<option value="04"><spring:message code="evaluation.month.4"/></option>
											<option value="05"><spring:message code="evaluation.month.5"/></option>
											<option value="06"><spring:message code="evaluation.month.6"/></option>
											<option value="07"><spring:message code="evaluation.month.7"/></option>
											<option value="08"><spring:message code="evaluation.month.8"/></option>
											<option value="09"><spring:message code="evaluation.month.9"/></option>
											<option value="10"><spring:message code="evaluation.month.10"/></option>
											<option value="11"><spring:message code="evaluation.month.11"/></option>
											<option value="12"><spring:message code="evaluation.month.12"/></option>
										</select>
										<select id="perQuarter" style="display: none;">
											<option value="" disabled selected>전체</option>
											<option value="1"><spring:message code="evaluation.querter.1"/></option>
											<option value="2"><spring:message code="evaluation.querter.2"/></option>
											<option value="3"><spring:message code="evaluation.querter.3"/></option>
											<option value="4"><spring:message code="evaluation.querter.4"/></option>
										</select>
										<input type="text" name="sEvalDate" id = "sEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateStart"/>" />
										<%-- 평가 날짜 --%>
										<input type="text" name="eEvalDate" id = "eEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateEnd"/>" />
										<%-- 종료 날짜 --%>
										<%-- <input type="text" name="sRecDate" id = "sRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateStart"/>" /> --%>
										<%-- 녹취 날짜 --%>
										<%--<input type="text" name="eRecDate" id ="eRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateEnd"/>" /> --%>

										<select id="selected" style="display : none;">
											<option value="" disabled selected><spring:message code="evaluation.campaign.selectType" /></option>
										</select>
										<select name="rEcampCode" id = "rEcampCode" style="display: none;">
											<option value="" disabled selected><spring:message code="evaluation.result.option.campaignTitle" /><%-- 캠페인명 --%></option>

										</select>
										<input type="text" id="agentName" style="display: none;"
											placeholder="<spring:message code="evaluation.result.placeholder.agentName"/>" />
									</form>

										<div class="campSetting" style = "display: none; width: 50px;">
											<div class="checks etrans">
												<input type="checkbox" id="currentMonth" data-target="1">
												<label for="currentMonth"><spring:message code="evaluation.campaign.thisMonth"/></label>
											</div>
										</div>
										<div class="campSetting" style = "display: none; width: 50px;">
											<div class="checks etrans">
												<input type="checkbox" id="lastMonth" data-target="2">
												<label for="lastMonth"><spring:message code="evaluation.campaign.lastMonth"/></label>
											</div>
										</div>
										<div class="campSetting" style = "display: none; width: 50px;">
											<div class="checks etrans">
												<input type="checkbox" id="gap" data-target="3">
												<label for="gap">GAP</label>
											</div>
										</div>
										<button id="search_result"
										class="ui_main_btn_flat icon_btn_search_white">
										<spring:message code="evaluation.result.button.search" />
										<%-- 조회 --%>
									</button>
								</div>
								<div class="ui_float_right">
									<button id="excelDownBtn" class="ui_main_btn_flat icon_btn_exelTxt_white"><spring:message code="message.btn.excelDownload"/></button>
								</div>
							</div>
						</div>

						<%-- 	<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										 <input type="text" name="rEvalatorName"
											placeholder="<spring:message code="evaluation.result.placeholder.evaluator"/>" />
										평가자
										<input type="text" name="rEvalatorFeedback"
											placeholder="<spring:message code="evaluation.result.placeholder.evaluatorComment"/>" />
										평가자 피드백
										<input type="text" name="rAgentFeedback"
											placeholder="<spring:message code="evaluation.result.placeholder.appraiseeComment"/>" />
										상담원 피드백
										<input type="text" name="rRecId"
											placeholder="<spring:message code="evaluation.result.placeholder.agentId"/>" />
										상담원ID

										상담원명
										<input type="text" name="rCustPhone1"
											placeholder="<spring:message code="evaluation.result.placeholder.callNumber"/>" />
										고객번호
									</form>

								</div>
							</div> --%>
						</div>
						<div class="gridWrap">
							<div id="gridEvaluationStatistics"></div>
							<div id="pagingEvaluationStatistics"></div>
						</div>
		</div>
	</div>

</body>