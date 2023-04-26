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
 <link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/evaluation_master/evaluation.css" />
 <link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/evaluation_master/evaluationWrap.css" />

<%-- js page --%>
<script type="text/javascript"
	src="${recseeResourcePath }/js/page/evaluation_master/evaluation_manage.js"></script>
<script>
	$(function() {
		top.playerVisible(false);
		$(window).resize(function() {

			// 현재 document 높이
			var documentHeight = $(window).height();

			// 그리드 위의 높이 값
			var gridCalcHeight = $("#gridEvaluationManage").offset().top;

			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
			$("#gridEvaluationManage").css({"height": + (gridResultHeight - 2) + "px"});
		}).resize();
	});
</script>
<style>
#feedbackGrid {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 300px !important;
}

#gridEvaluationManage {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
}

#pagingEvaluationManage {
	clear: both;
}

#gridEvaluationMagician {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 500px !important;
}

#gridEvaluationEval {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 500px !important;
}

#pagingEvaluationMagician {
	clear: both;
}

#paginggridEvaluation {
	clear: both;
}

#gridEvaluation {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 305px !important;
}

#pagingEvaluation {
	clear: both;
}

#evaluationSheet {
	width: 921px;
	height: 760px;
}

#evaluationCreate {
	width: 921px;
	height: 350px;
}

#evaluationMagician {
	width: 921px;
	height: 735px;
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
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.management"/></p>
					</div>
					<div class="ui_float_right">
						<button class="icon_btn_trash_gray" onclick="deleteEvalSheet();"></button>
						<button class="ui_btn_blue" onclick="sheetCopy();"><spring:message code="evaluation.campaign.copySheet"/></button>
						<button class="ui_btn_blue createEval" onclick="stepChoicePopup();" data-target="ms"><spring:message code="evaluation.management.sheet.create"/></button>
					</div>
				</div>
			</div>
			<div class="evaluation_manage_grid">
				<div id="gridEvaluationManage"></div>
				<div id="pagingEvaluationManage"></div>
			</div>
		</div>
	</div>

	<div id="evaluationCreate" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.management.create.selectstep"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray" onClick="layer_popup_close()" ></button>
					</div>
				</div>
			</div>
			<div class="evaluation_create">
				<div class="create_step" id="step1">
					<div class="ui_padding">
						<p class="step_tit"><spring:message code="evaluation.management.create.steptitle1"/></p>
						<div class="step_img step_img_01"></div>
						<p class="step_txt"><spring:message code="evaluation.management.create.step1"/></p>
					</div>
				</div>
				<div class="create_step" id="step2">
					<div class="ui_padding">
						<p class="step_tit"><spring:message code="evaluation.management.create.steptitle2"/></p>
						<div class="step_img step_img_02"></div>
						<p class="step_txt"><spring:message code="evaluation.management.create.step2"/></p>
					</div>
				</div>
				<div class="create_step" id="step3">
					<div class="ui_padding">
						<p class="step_tit"><spring:message code="evaluation.management.create.steptitle3"/></p>
						<div class="step_img step_img_03"></div>
						<p class="step_txt"><spring:message code="evaluation.management.create.step3"/></p>
					</div>
				</div>
				<div class="create_step" id="step4">
					<div class="ui_padding">
						<p class="step_tit"><spring:message code="evaluation.management.create.steptitle4"/></p>
						<div class="step_img step_img_04"></div>
						<p class="step_txt"><spring:message code="evaluation.management.create.step4"/></p>
					</div>
				</div>
			</div>

			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button class="ui_main_btn_flat radius_button" onclick="nextCreat();">
							<spring:message code="evaluation.label.next"/>
						</button>
					</div>
				</div>
			</div>

		</div>
	</div>

	<div id="evaluationMagician" class="popup_obj">
		<div class="ui_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.createSheet"/></p>
					</div>

					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"  onClick="layer_popup_close()" ></button>
					</div>
				</div>
			</div>
			<div class="evaluation_magician_header">
				<input id="evalMakeTitle" class="icon_input_tit" type="text" placeholder="<spring:message code="evaluation.management.placeholder.name"/>" />
				<input id="evalMakeDescription" class="icon_input_info" type="text" placeholder="<spring:message code="evaluation.management.placeholder.description"/>" />
			</div>
			<div class="evaluation_magician_sheet">
				<div id="gridEvaluationMagician"></div>
				<div id="pagingEvaluationMagician"></div>
			</div>
			<div class="evaluation_magician_footer">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<p class="score_result_txt" id="totalScoreTxt"><spring:message code="evaluation.management.sheet.score"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" id="totalScore" value="0"/>
						</form>
					</div>
					<div class="ui_float_right"><input type="hidden" class="sheetCode" />
						<%-- <button class="ui_btn_white" id="prevButton" onclick="prevCreat();"><spring:message code="evaluation.management.sheet.b1ack"/></button> --%>
						<%-- <button class="ui_main_btn_flat deleteCode"><spring:message code="evaluation.management.sheet.deletion"/></button> --%>
						<%-- <button class="ui_main_btn_flat addCode"><spring:message code="evaluation.management.sheet.add"/></button> --%>
						<button class="ui_main_btn_flat insertSheet" onclick="regitEvalSheet('insert')"><spring:message code="evaluation.management.sheet.insertSheet"/></button>
						<button class="ui_main_btn_flat deleteEvalSheet" onclick="deleteEvalSheet_popup()"><spring:message code="message.btn.del"/></button>
						<button class="ui_main_btn_flat excelDownloadBtn" onclick="excelDownloadSheetInfo();"><spring:message code="message.btn.excelDownload"/></button>
						<iframe id="downloadFrame" src="" style="display:none;width:0px;height:0px;"></iframe>
					</div>
				</div>
			</div>
		</div>
		<script>
			function prevCreat() {
				layer_popup_close();
				//바로 실행하면 mask 생성시간(0.3초)를 무시하므로 0.3초의 텀을 준다.
				setTimeout(function() {
					layer_popup('#evaluationCreate')
				}, 300);
			}
		</script>
	</div>

	<div id="evaluationWrap" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.evaluation"/></p>
					</div>
					<div class=ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray" onClick="gridClear();" ></button>
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
								<option>TEST</option>
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
						<button class="ui_btn_blue" id="refreshStatus"><spring:message code="evaluation.management.sheet.refresh"/></button>
						<button id="evalStatus"><spring:message code="evaluation.management.sheet.state"/></button>
					</div>
					<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.begin"/></button>
					<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.end"/></button>
					<div class="ui_float_right">
						<form class="comment_form comment_form_boxSizing">
							<textarea class="comment_form comment_form_boxSizing" rows="2"></textarea>
						</form>
						<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.input"/></button>
					</div>
				</div>
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<button class="ui_btn_white popup_close"><spring:message code="evaluation.management.sheet.transterence"/></button>
					</div>
					<div class="ui_float_right">
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score2"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.evalresult"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.compleeval"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="evaluationWrap2" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.evaluation"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray" onClick="gridClear();" ></button>
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
								<option>TEST</option>
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
						<button class="ui_btn_blue" id="refreshStatus"><spring:message code="evaluation.management.sheet.refresh"/></button>
						<button id="evalStatus2"><spring:message code="evaluation.management.sheet.state"/></button>
					</div>
					<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.agreeleader"/></button>
					<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.objection"/></button>
					<div class="ui_float_right">
						<form class="comment_form comment_form_boxSizing">
							<textarea class="comment_form comment_form_boxSizing" rows="3"></textarea>
						</form>
						<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.input"/></button>
					</div>
				</div>
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<button class="ui_btn_white popup_close"><spring:message code="evaluation.management.sheet.transterence"/></button>
					</div>
					<div class="ui_float_right">
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score2"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.evalresult"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.compleeval"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="evaluationWrap3" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.evaluation"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
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
								<option>테스트</option>
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
						<button class="ui_btn_blue" id="refreshStatus"><spring:message code="evaluation.management.sheet.refresh"/></button>
						<button id="evalStatus3"><spring:message code="evaluation.management.sheet.state"/></button>
					</div>
					<div class="ui_float_right">
						<form class="comment_form comment_form_boxSizing">
							<textarea class="comment_form comment_form_boxSizing" rows="3"></textarea>
						</form>
						<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.input"/></button>
					</div>
				</div>
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<button class="ui_btn_white popup_close"><spring:message code="evaluation.management.sheet.transterence"/></button>
					</div>
					<div class="ui_float_right">
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score2"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.score"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<p class="score_result_txt"><spring:message code="evaluation.management.sheet.evalresult"/></p>
						<form class="score_result">
							<input type="text" class="ui_input_hasinfo" />
						</form>
						<button class="ui_btn_blue"><spring:message code="evaluation.management.sheet.compleeval"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
