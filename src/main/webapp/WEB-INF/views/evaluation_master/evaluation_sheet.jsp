<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<%@ include file="../common/include/commonVar.jsp" %>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>

	<link rel="stylesheet" type="text/css" href="<c:out value="${siteResourcePath}"/>/css/page/header.css" />

	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/evaluation_master/evaluation_sheet.js"></script>

	<script>
	$(function() {
	    $(window).resize(function() {

	    }).resize();
	})
	</script>
	<style>
		/* 평가시트 그리드 */
		 #gridEvaluation{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			height: 500px !important;
        }
        #evaluationSheet{
            width: 100%;
            position: absolute;
        }
        #evaluationSheet .comment_row{
			height: 130px !important;;
			overflow: auto;
		}
        #evalPlayerFrame{
            display: block;
		    position: relative;
		    width: 100%;
		    height: 118px;
		    top: 0px;
		    border: 0px;
		    overflow: hidden;
        }
		#gridEvaluation.gridbox input[type="radio"]{
			width: 15px !important;
		}
		.subRadioBox{
			widht: 40px !important;
		}
		.itemTitle{
			width: 100%;
			min-height: 20px;
			height: auto;
			line-heig1ht: 20px;
			border-bottom: 2px solid #BDBDBD;
			background-color: #F9F9F9;
			padding-left: 5px;
		}
		.subItemTitle{
			width: 100%;
			min-height: 20px;
			height: auto;
			border-bottom: 2px solid #BDBDBD;
			background-color: #F9F9F9;

		}
		/* .bottomLiner{
			min-height: 16px;
			height: 22px;
			line-height: 22px;
			border-bottom: 1px solid #ddd;
			background-color: #FCFCFC;
		} */
		.bottomLiner{
			min-height: 22px;
			/* height: 22px; */
			line-height: 22px;
			border-bottom: 1px solid #ddd;
			background-color: #FCFCFC;
			float:left;
			/* display:table-cell; */
		}
		.bottomLinerSubject{
			width: calc(100% - 100px) !important;
			order: 1;
		}
		.bottomLinerMark{
			width: 100px !important;
			order: 2;
		}
		#gridEvaluation .obj tr td{
			vertical-align: middle;
		}
		#gridEvaluation.gridbox_dhx_web.gridbox table.obj tr td{
			padding: 0px !important;
		}
		#gridEvaluation.gridbox_dhx_web.gridbox .odd_dhx_web{
			background-color: #fff;
		}
		#gridEvaluation.gridbox_dhx_web.gridbox table.obj tr td{
			border-bottom: 2px solid #BDBDBD;
		}

		/* 선택했을때의 row 색상 없애기 병합때문에 이상함... default 스타일로 강제 변경 */
		#gridEvaluation.gridbox.gridbox_dhx_web table.obj tr td.cellselected{
			background-color: #fff !important;
			border-right: 1px solid #ededed !important;
		}
		#gridEvaluation.gridbox.gridbox_dhx_web table.obj tr.rowselected td{
			background-color: #fff !important;
			border-right: 1px solid #ededed !important;
		}
	</style>
</head>
<body>
	<input type="hidden" id="levelUser" value="${levelUser}">
	<input type="hidden" id="listenUrl" value="${listenUrl}">
	<input type="hidden" id="rRecId" value="${rRecId}">
	<input type="hidden" id="rRecName" value="${rRecName}">
	<input type="hidden" id="rRecDate" value="${rRecDate}">
	<input type="hidden" id="rRecTime" value="${rRecTime}">
	<input type="hidden" id="ip" value="${ip}">
	<input type="hidden" id="port" value="${port}">
	<input type="hidden" id="firstEvalYN" value="${firstEvalYN}">
	<input type="hidden" id="cclist" value="${cclist}">
	<input type="hidden" id="cnlist" value="${cnlist}">
	<input type="hidden" id="evaluator" value="${evaluator}">
	<input type="hidden" id="evalatorId" value="${evalatorId}">
	<input type="hidden" id="evalatorName" value="${evalatorName}">
	<input type="hidden" id="evalCode" value="${evalCode}">
	<input type="hidden" id="campCode" value="${campCode}">
	<input type="hidden" id="custName" value="${custName}">
	<input type="hidden" id="custPhone" value="${custPhone}">
	<input type="hidden" id="userType" value="${userType}">
	<input type="hidden" id="mgCode" value="${mgCode}">
	<input type="hidden" id="sgCode" value="${sgCode}">
	<input type="hidden" id="bgCode" value="${bgCode}">

    <div id="evaluationSheet">
        <div class="ui_popup_padding">
        	<div class="ui_article eval_popup_body">
                <div class="ui_pannel_row" style="border-top: 1px solid #bbbbbb;">
                    <div class="ui_float_left">
	                    <form>
			        		<label id="recDateInfoLabel"><spring:message code="views.search.grid.call.title.date"/></label>
			        			<input type="text" value="${rRecDate}" id="recDateInfo" class="clear_target ui_input_hasinfo"/>
							<label id="recTimeInfoLabel"><spring:message code="views.search.grid.call.title.time"/></label>
			        			<input type="text" value="${rRecTime}" id="recTimeInfo" class="clear_target ui_input_hasinfo"/>
		        			<label id="recAgentNameLabel"><spring:message code="evaluation.result.placeholder.agentName"/></label>
			        			<input type="text" value="${rRecName}" id="recAgentName" class="clear_target ui_input_hasinfo"/>
		        			<label id="recEvalNameLabel"><spring:message code="evaluation.result.placeholder.evaluator"/></label>
			        			<input type="text" value="${evalatorName}" id="recEvalName" class="clear_target ui_input_hasinfo"/>
							<label id="revaluationLabel"><spring:message code="evaluation.management.sheet.evaluationCount"/></label>
	                      		<input class="clear target ui_input_hasinfo" id="revaluation"/>
	        			</form>
        			</div>
        		</div>
        	</div>
           	<iframe id="evalPlayerFrame">
	            <div id="playerObj"class="togglePlayer">
	          	</div>
			</iframe>

            <div class="ui_article eval_popup_body">
                <div class="ui_pannel_row" style="border-top: 1px solid #bbbbbb;">
                    <div class="ui_float_left">
                        <form class="campaign_view_form">
                             <select class="clear_target" name="campaign_list" id="camp_list" required>
                                <option value="" disabled selected><spring:message code="header.popup.evaluation.option.selectCampaign"/></option>
                            </select>
                        </form>
                        <input class="clear_target" name="campaignlist" id="campList" style="display: none;"/>
                    </div>
                    <div class="ui_float_right">
						<form class="score_form">
							<label><spring:message code="header.popup.evaluation.label.sheetScore"/></label>
                            <input type="text" value="" name="maxScore" class="clear_target ui_input_hasinfo"/>
                            <label><spring:message code="header.popup.evaluation.label.totalScore"/></label>
                            <input type="text" value="" name="currentScore" class="clear_target ui_input_hasinfo"/>
                        </form>
					</div>
                </div>
                <div class="ui_pannel_row" style="display:none">
                    <div class="ui_float_left">
                        <form class="campagin_option_form">
							<input type="text" class="clear_target ui_input_hasinfo" data-code="notification" value="<spring:message code="header.popup.evaluation.input.notification"/>"/>
							<input type="text" class="clear_target ui_input_hasinfo" data-code="objection" value="<spring:message code="header.popup.evaluation.input.objection"/>"/>
							<input type="text" class="clear_target ui_input_hasinfo" data-code="revaluation" value="<spring:message code="header.popup.evaluation.input.revaluation"/>"/>
							<input type="text" class="clear_target ui_input_hasinfo" data-code="scoringSystem" value="<spring:message code="header.popup.evaluation.input.scoringSystem"/>"/>
                        </form>
                    </div>
                </div>
	            <div class="gridWrap">
	                <div id="gridEvaluation"></div>
	            </div>
                <div class="ui_pannel_row comment_row">
                    <div class="ui_float_left float_left_full_size">
	                    <form class="comment_form evalatorFeedForm">
	                        <label><spring:message code="header.popup.evaluation.label.evaluatorComment"/></label>
	                        <br/>
	                        <textarea id="EvalatorFeedback" class="clear_target" rows="5"></textarea>
	                    </form>
	                    <form class="comment_form agentFeedForm">
	                        <label><spring:message code="header.popup.evaluation.label.appraiseeComment"/></label>
	                        <br/>
	                        <textarea id="agentFeedback" class="clear_target" rows="5"></textarea>
	                    </form>
                   	</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<%--  <button class="ui_btn_white"><spring:message code="header.popup.evaluation.button.interimStorage"/></button> --%>
                        <button class="ui_main_btn_flat excelDownloadBtn" style=" display: none;" onclick="excelDownloadSheetInfo();"><spring:message code="message.btn.excelDownload"/></button>
                        <button id="evaluate_complete_btn" class="ui_main_btn_flat"><spring:message code="header.popup.evaluation.button.done"/></button>
                        <button class="ui_btn_white" id="updateBtn" style="display : none;"><spring:message code="message.btn.modify"/></button>
                        <button class="ui_btn_white" id="deleteBtn" style="display : none;"><spring:message code="message.btn.del"/></button>
                        <iframe id="downloadFrame" src="" style="display:none;width:0px;height:0px;"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>