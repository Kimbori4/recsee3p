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
<%--  <link rel="stylesheet" type="text/css"
	href="${recseeResourcePath }/css/page/search/search.css" /> --%>

<%-- js page --%>
<script type="text/javascript"
	src="${recseeResourcePath }/js/page/evaluation_master/evaluation_result.js"></script>

<%-- <script type="text/javascript" src="${compoResourcePath }/recsee_player/recsee_player.js"></script> --%>
<script>
$(function() {
	top.playerVisible(false);
	$(window).resize(function() {

		// 현재 document 높이
		var documentHeight = $(window).height();

		// 그리드 위의 높이 값
	    var gridCalcHeight = $("#gridEvaluationResult").offset().top;

		// 페이징이 있음 페이징 만큼 뺴주깅
		var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
		var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
		$("#gridEvaluationResult").css({"height": + (gridResultHeight - 2) + "px"});
	}).resize();

})
</script>
<style>
#gridEvaluationResult {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
}

#closedResultGrid {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
	height: 885px !important;
}

#inProgressResultGrid {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
	height: 885px !important;
}
/* layer popup */
#evaluationSheet {
	width: 866px;
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
		<%-- 	<div class="ui_tabbar">
				<div class="ui_tabbar_header">
					<ul>
						<li class="tabbar_menu"><spring:message	code="evaluation.result.label.whole" />평가 결과 전체</li>
						<li class="tabbar_menu"><spring:message	code="evaluation.result.label.closed" />완료된 평가</li>
						<li class="tabbar_menu"><spring:message	code="evaluation.result.label.inProgress" />평가 중</li>
					</ul>
				</div> --%>

				<div class="ui_tabbar_section">
					<div class="tabbar_cont">
						<div class="ui_article">
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<input type="text" name="sEvalDate" id = "sEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateStart"/>" />
										<%-- 평가 날짜 --%>
										<input type="text" name="eEvalDate" id = "eEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateEnd"/>" />
										<%-- 종료 날짜 --%>
										<input type="text" name="sRecDate" id = "sRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateStart"/>" />
										<%-- 녹취 날짜 --%>
										<input type="text" name="eRecDate" id ="eRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateEnd"/>" />
										<%-- 종료 날짜 --%>
										<%-- <select name="sRecTime">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.recTimeStart" />녹취 시작 시간</option>
											<option value="010000">01:00:00</option>
											<option value="020000">02:00:00</option>
											<option value="030000">03:00:00</option>
											<option value="040000">04:00:00</option>
											<option value="050000">05:00:00</option>
											<option value="060000">06:00:00</option>
											<option value="070000">07:00:00</option>
											<option value="080000">08:00:00</option>
											<option value="090000">09:00:00</option>
											<option value="100000">10:00:00</option>
											<option value="110000">11:00:00</option>
											<option value="120000">12:00:00</option>
											<option value="130000">13:00:00</option>
											<option value="140000">14:00:00</option>
											<option value="150000">15:00:00</option>
											<option value="160000">16:00:00</option>
											<option value="170000">17:00:00</option>
											<option value="180000">18:00:00</option>
											<option value="190000">19:00:00</option>
											<option value="200000">20:00:00</option>
											<option value="210000">21:00:00</option>
											<option value="220000">22:00:00</option>
											<option value="230000">23:00:00</option>
										</select> <select name="eRecTime">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.recTimeEnd" />녹취 종료 시간</option>
											<option value="015959">01:59:59</option>
											<option value="025959">02:59:59</option>
											<option value="035959">03:59:59</option>
											<option value="045959">04:59:59</option>
											<option value="055959">05:59:59</option>
											<option value="065959">06:59:59</option>
											<option value="075959">07:59:59</option>
											<option value="085959">08:59:59</option>
											<option value="095959">09:59:59</option>
											<option value="105959">10:59:59</option>
											<option value="115959">11:59:59</option>
											<option value="125959">12:59:59</option>
											<option value="135959">13:59:59</option>
											<option value="145959">14:59:59</option>
											<option value="155959">15:59:59</option>
											<option value="165959">16:59:59</option>
											<option value="175959">17:59:59</option>
											<option value="185959">18:59:59</option>
											<option value="195959">19:59:59</option>
											<option value="205959">20:59:59</option>
											<option value="215959">21:59:59</option>
											<option value="225959">22:59:59</option>
											<option value="235959">23:59:59</option>
										</select> --%>
									</form>
								</div>
							</div>
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<select name="rEcampCode">
											<option value="" disabled selected><spring:message code="evaluation.result.option.campaignTitle" /><%-- 캠페인명 --%></option>

										</select> <select name="rEvalStatus">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.status" /><%-- 평가상태 --%></option>
											<option value=""><spring:message
													code="evaluation.result.option.all" /><%-- 전체 --%></option>
											<option value="C"><spring:message
													code="evaluation.result.option.status.C" /><%-- 완료 --%></option>
											<option value="I"><spring:message
													code="evaluation.result.option.status.I" /><%-- 미완료 --%></option>
										</select> <input type="text" name="rEvalatorName"
											placeholder="<spring:message code="evaluation.result.placeholder.evaluator"/>" />
										<%-- 평가자 --%>
										<%-- <input type="text" name="rEvalatorFeedback"
											placeholder="<spring:message code="evaluation.result.placeholder.evaluatorComment"/>" /> --%>
										<%-- 평가자 피드백 --%>
										<%-- <input type="text" name="rAgentFeedback"
											placeholder="<spring:message code="evaluation.result.placeholder.appraiseeComment"/>" /> --%>
										<%-- 상담원 피드백 --%>
										<input type="text" name="rRecId"
											placeholder="<spring:message code="evaluation.result.placeholder.agentId"/>" />
										<%-- 상담원ID --%>
										<input type="text" name="rRecName"
											placeholder="<spring:message code="evaluation.result.placeholder.agentName"/>" />
										<%-- 상담원명 --%>
										<input type="text" name="rCustPhone1"
											placeholder="<spring:message code="evaluation.result.placeholder.callNumber"/>" />
										<%-- 고객번호 --%>
									</form>
									<button id="search_result"
										class="ui_main_btn_flat icon_btn_search_white">
										<spring:message code="evaluation.result.button.search" />
										<%-- 조회 --%>
									</button>
								</div>
							</div>
						</div>
						<div class="gridWrap">
							<div id="gridEvaluationResult"></div>
							<div id="pagingEvaluationResult"></div>
						</div>

					<%-- <div class="tabbar_cont">
						<div class="ui_article">
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<input type="text" name="sEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateStart"/>" />
										평가 날짜
										<input type="text" name="eEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateEnd"/>" />
										종료 날짜
										<input type="text" name="sRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateStart"/>" />
										녹취 날짜
										<input type="text" name="eRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateEnd"/>" />
										종료 날짜
										<select name="sRecTime">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.recTimeStart" />녹취 시작 시간</option>
											<option value="010000">01:00:00</option>
											<option value="020000">02:00:00</option>
											<option value="030000">03:00:00</option>
											<option value="040000">04:00:00</option>
											<option value="050000">05:00:00</option>
											<option value="060000">06:00:00</option>
											<option value="070000">07:00:00</option>
											<option value="080000">08:00:00</option>
											<option value="090000">09:00:00</option>
											<option value="100000">10:00:00</option>
											<option value="110000">11:00:00</option>
											<option value="120000">12:00:00</option>
											<option value="130000">13:00:00</option>
											<option value="140000">14:00:00</option>
											<option value="150000">15:00:00</option>
											<option value="160000">16:00:00</option>
											<option value="170000">17:00:00</option>
											<option value="180000">18:00:00</option>
											<option value="190000">19:00:00</option>
											<option value="200000">20:00:00</option>
											<option value="210000">21:00:00</option>
											<option value="220000">22:00:00</option>
											<option value="230000">23:00:00</option>
										</select> <select name="eRecTime">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.recTimeEnd" />녹취 종료 시간</option>
											<option value="015959">01:59:59</option>
											<option value="025959">02:59:59</option>
											<option value="035959">03:59:59</option>
											<option value="045959">04:59:59</option>
											<option value="055959">05:59:59</option>
											<option value="065959">06:59:59</option>
											<option value="075959">07:59:59</option>
											<option value="085959">08:59:59</option>
											<option value="095959">09:59:59</option>
											<option value="105959">10:59:59</option>
											<option value="115959">11:59:59</option>
											<option value="125959">12:59:59</option>
											<option value="135959">13:59:59</option>
											<option value="145959">14:59:59</option>
											<option value="155959">15:59:59</option>
											<option value="165959">16:59:59</option>
											<option value="175959">17:59:59</option>
											<option value="185959">18:59:59</option>
											<option value="195959">19:59:59</option>
											<option value="205959">20:59:59</option>
											<option value="215959">21:59:59</option>
											<option value="225959">22:59:59</option>
											<option value="235959">23:59:59</option>
										</select>
									</form>
									<button id="search_result"
										class="ui_main_btn_flat icon_btn_search_white">
										<spring:message code="evaluation.result.button.search" />
										조회
									</button>
								</div>
							</div>
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<select name="rEcampCode">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.campaignTitle" />캠페인명</option>
											<option value="a"></option>
										</select> <select name="rEvalStatus">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.status" />평가상태</option>
											<option value=""><spring:message
													code="evaluation.result.option.all" />전체</option>
											<option value="C"><spring:message
													code="evaluation.result.option.status.C" />완료</option>
											<option value="I"><spring:message
													code="evaluation.result.option.status.I" />미완료</option>
										</select> <input type="text" name="rEvalatorName"
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
										<input type="text" name="rRecName"
											placeholder="<spring:message code="evaluation.result.placeholder.agentName"/>" />
										상담원명
										<input type="text" name="rCustPhone1"
											placeholder="<spring:message code="evaluation.result.placeholder.callNumber"/>" />
										고객번호
									</form>
								</div>
							</div>
						</div>
						<div class="gridWrap">
							<div id="closedResultGrid"></div>
							<div id="pagingEvaluationResultclosed"></div>
						</div>
					</div>

					<div class="tabbar_cont">
						<div class="ui_article">
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<input type="text" name="sEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateStart"/>" />
										평가 날짜
										<input type="text" name="eEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateEnd"/>" />
										종료 날짜
										<input type="text" name="sRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateStart"/>" />
										녹취 날짜
										<input type="text" name="eRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateEnd"/>" />
										종료 날짜
										<select name="sRecTime">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.recTimeStart" />녹취 시작 시간</option>
											<option value="010000">01:00:00</option>
											<option value="020000">02:00:00</option>
											<option value="030000">03:00:00</option>
											<option value="040000">04:00:00</option>
											<option value="050000">05:00:00</option>
											<option value="060000">06:00:00</option>
											<option value="070000">07:00:00</option>
											<option value="080000">08:00:00</option>
											<option value="090000">09:00:00</option>
											<option value="100000">10:00:00</option>
											<option value="110000">11:00:00</option>
											<option value="120000">12:00:00</option>
											<option value="130000">13:00:00</option>
											<option value="140000">14:00:00</option>
											<option value="150000">15:00:00</option>
											<option value="160000">16:00:00</option>
											<option value="170000">17:00:00</option>
											<option value="180000">18:00:00</option>
											<option value="190000">19:00:00</option>
											<option value="200000">20:00:00</option>
											<option value="210000">21:00:00</option>
											<option value="220000">22:00:00</option>
											<option value="230000">23:00:00</option>
										</select> <select name="eRecTime">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.recTimeEnd" />녹취 종료 시간</option>
											<option value="015959">01:59:59</option>
											<option value="025959">02:59:59</option>
											<option value="035959">03:59:59</option>
											<option value="045959">04:59:59</option>
											<option value="055959">05:59:59</option>
											<option value="065959">06:59:59</option>
											<option value="075959">07:59:59</option>
											<option value="085959">08:59:59</option>
											<option value="095959">09:59:59</option>
											<option value="105959">10:59:59</option>
											<option value="115959">11:59:59</option>
											<option value="125959">12:59:59</option>
											<option value="135959">13:59:59</option>
											<option value="145959">14:59:59</option>
											<option value="155959">15:59:59</option>
											<option value="165959">16:59:59</option>
											<option value="175959">17:59:59</option>
											<option value="185959">18:59:59</option>
											<option value="195959">19:59:59</option>
											<option value="205959">20:59:59</option>
											<option value="215959">21:59:59</option>
											<option value="225959">22:59:59</option>
											<option value="235959">23:59:59</option>
										</select>
									</form>
									<button id="search_result"
										class="ui_main_btn_flat icon_btn_search_white">
										<spring:message code="evaluation.result.button.search" />
										조회
									</button>
								</div>
							</div>
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<select name="rEcampCode">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.campaignTitle" />캠페인명</option>
											<option value="a"></option>
										</select> <select name="rEvalStatus">
											<option value="" disabled selected><spring:message
													code="evaluation.result.option.status" />평가상태</option>
											<option value=""><spring:message
													code="evaluation.result.option.all" />전체</option>
											<option value="C"><spring:message
													code="evaluation.result.option.status.C" />완료</option>
											<option value="I"><spring:message
													code="evaluation.result.option.status.I" />미완료</option>
										</select> <input type="text" name="rEvalatorName"
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
										<input type="text" name="rRecName"
											placeholder="<spring:message code="evaluation.result.placeholder.agentName"/>" />
										상담원명
										<input type="text" name="rCustPhone1"
											placeholder="<spring:message code="evaluation.result.placeholder.callNumber"/>" />
										고객번호
									</form>
								</div>
							</div>
						</div>
						<div class="gridWrap">
							<div id="inProgressResultGrid"></div>
							<div id="pagingEvaluationResultinProgress"></div>
						</div>
					</div>

				</div>
			</div> --%>
			
		</div>
	</div>

</body>