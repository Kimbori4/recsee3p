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
<%-- <link rel="stylesheet" type="text/css"
	href="${recseeResourcePath }/css/page/evaluation/evaluation.css" />
 --%>
<%-- js page --%>
<script type="text/javascript"
	src="${recseeResourcePath }/js/page/evaluation_master/evaluation_evaluating.js"></script>
<%-- <script type="text/javascript" src="${compoResourcePath }/recsee_player/recsee_player.js"></script> --%>

	<script>
		$(function() {
			top.playerVisible(false);
			$(window).resize(function() {

				// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridEvaluating").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				$("#gridEvaluating").css({"height": + (gridResultHeight - 2) + "px"});
			}).resize();
		})
	</script>
<style>
#gridEvaluating {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
	/* height: 885px !important; */
}

#closedResultGrid {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
	/* height: 885px !important; */
}

#inProgressResultGrid {
	position: relative;
	float: left;
	clear: both;
	width: 100% !important;
	/* height: 885px !important; */
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


				<div class="ui_tabbar_section">
					<div class="tabbar_cont">
						<div class="ui_article">
							<div class="ui_pannel_row">
								<div class="ui_float_left search_form">
									<form>
										<%-- <input type="text" name="sEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateStart"/>" /> --%>
										<%-- 평가 날짜 --%>
										<%-- <input type="text" name="eEvalDate"
											class="ui_input_cal icon_input_cal today"
											placeholder="<spring:message code="evaluation.result.placeholder.evalDateEnd"/>" /> --%>
										<%-- 종료 날짜 --%>
										<input type="text" id="sRecDate" name="sRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateStart"/>" />
										<%-- 녹취 날짜 --%>
										<input type="text" id="eRecDate" name="eRecDate"
											class="ui_input_cal icon_input_cal"
											placeholder="<spring:message code="evaluation.result.placeholder.recDateEnd"/>" />
										<%-- 종료 날짜 --%>
										<select name="rEcampCode">
											<option value="" disabled selected><spring:message code="evaluation.result.option.campaignTitle" /><%-- 캠페인명 --%></option>
											<option value="a">test</option>
										</select>
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
							<div id="gridEvaluating"></div>
							<div id="pagingEvaluating"></div>
						</div>
					</div>

				</div>

			<div id="evaluationWrap1" class="popup_obj">
				<div class="ui_popup_padding">
					<div class="popup_header">
						<div class="ui_pannel_row">
							<div class="ui_float_left">
								<p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.evaluation" /></p>
							</div>
							<div class="ui_float_right">
								<button class="popup_close ui_btn_white ui_icon_exit"></button>
							</div>
						</div>
					</div>
					<div id="rcpObj"></div>
					<div class="evaluation_header">
						<div class="ui_pannel_row">
							<div class="ui_float_left">
								<p class="comp_choice_txt"><spring:message code="evaluation.management.sheet.selectCampaign"/></p>
								<form class="camp_choice">
									<select class="camp_choice_select">
										<option id="evalCampList">테스트</option>
										<option id= "evalCampList2">test2</option>
									</select>
								</form>
							</div>
							<div class="ui_float_right">
								<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score2"/></p>
								<form class="score_result">
									<input type="text" value="100점" class="ui_input_hasinfo" />
								</form>
							</div>
						</div>
					</div>
					<div class="gridWrap">
						<div id="gridEvaluationEval"></div>
						<div id="paginggridEvaluation"></div>
					</div>
					<div class="feedback_dialogue">
						<div class="evaluation_magician_sheet">
							<div id="feedbackGrid"></div>
							<div id="pagingGridFeedback"></div>
						</div>
					</div>
					<div class="evaluation_footer">
						<div class="ui_pannel_row">
							<div>
								<button class="ui_btn_blue" id="refreshStatus">새로고침</button>
								<button id="evalStatus"><spring:message code="evaluation.management.sheet.state"/></button>
							</div>
							<button class="ui_btn_blue">평가시작</button>
							<button class="ui_btn_blue">평가종료</button>
							<div class="ui_float_right">
								<form class="comment_form comment_form_boxSizing">
									<textarea class="comment_form comment_form_boxSizing" rows="2"></textarea>
								</form>
								<button class="ui_btn_blue">입력</button>
							</div>
						</div>
						<div class="ui_pannel_row">
							<div class="ui_float_left">
								<button class="ui_btn_white popup_close">평가 관리로 이동</button>
							</div>
							<div class="ui_float_right">
								<p class="score_result_txt">평가지 점수 합계</p>
								<form class="score_result">
									<input type="text" class="ui_input_hasinfo" />
								</form>
								<p class="score_result_txt">점수</p>
								<form class="score_result">
									<input type="text" class="ui_input_hasinfo" />
								</form>
								<p class="score_result_txt">결과</p>
								<form class="score_result">
									<input type="text" class="ui_input_hasinfo" />
								</form>
								<button class="ui_btn_blue">평가 완료</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>