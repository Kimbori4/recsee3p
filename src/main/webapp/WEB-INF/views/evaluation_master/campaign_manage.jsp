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
	src="${recseeResourcePath }/js/page/evaluation_master/campaign_manage.js"></script>

<script>
$(function() {
	top.playerVisible(false);
	$(window).resize(function() {

		// 현재 document 높이
		var documentHeight = $(window).height();

		// 그리드 위의 높이 값
	    var gridCalcHeight = $("#gridCampaignManage").offset().top;
	    var gridCalcHeight2 = $("#gridCampaignHistory").offset().top;

		// 페이징이 있음 페이징 만큼 뺴주깅
		var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
		var gridCampaignManage = (documentHeight - gridCalcHeight - pagingHeight);
		var gridCampaignHistory = (documentHeight - gridCalcHeight2 - pagingHeight);
		$("#gridCampaignManage").css({"height": + (gridCampaignManage - 2) + "px"});
		$("#gridCampaignHistory").css({"height": + (gridCampaignHistory - 2) + "px"});
	}).resize();

})
</script>
<style>

.campSetting{
	margin-top: 16px;
}
/*search Form*/
.search_fieldset{
	display:block;
	float:left;
	border: 1px solid #2d71c4 !important;
	position : relative;
	transition : transform 1s;
}

.search_fieldset legend{
	font-size : 12px;
	margin-left : 5px;
	color: #3da0e3;
}

.search_fieldset_move{
	position : absolute;
	right : 3px;
	transform : scale(1.1);
	cursor : pointer;
}

#gridCampaignManage {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
}

#gridCampaignHistory {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
}

#gridCampaignMagician {
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 350px !important;
}
#gridEvalViewGroup{
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 350px !important;
}
#gridEvalViewAssign{
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 350px !important;
}
#gridCampaignViewGroup{
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 350px !important;
}
#gridCampaignViewAssign{
	position: relative;
	clear: both;
	float: left;
	width: 100% !important;
	height: 350px !important;
}
/* Layer  */
#campaginMagician {
	width: 760px;
}
#campaignViewGroup {
	width: 500px;
}
#campaignViewAssign {
	width: 500px;
}
 #EvalViewGroup {
	width: 500px;
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

			<div class="ui_tabbar">
				<div class="ui_tabbar_header">
					<div class="tabberMenu">
					<ul>
						<li class="tabbar_menu"><spring:message code="evaluation.campaign.ongoing.tabName"/></li>
						<li class="tabbar_menu"><spring:message code="evaluation.campaign.outOfDate.tabName"/></li>
					</ul>
					</div>
				</div>

				<div class="ui_tabbar_section">
					<div class="tabbar_cont">
						<div class="ui_acrticle">
							<div class="ui_pannel_row">
								<div class="ui_float_left">
									<form id="searchForm">
										
									</form>
									
								</div>
								<div class="ui_float_right">
									<button class="icon_btn_trash_gray" id="deleteCampaign"></button>
									<button class="ui_main_btn_flat copyCampaign"><spring:message code="evaluation.campaign.copyCampaign"/></button>
									<button class="ui_main_btn_flat mCampaign"
										onclick="layer_popup('#campaginMagician'); gridCampaignMagicianClear(); $('.makeCampaign').show(); $('.makeCampaign').text('캠페인 생성');" data-target="mc"><spring:message code="evaluation.campaign.ongoing.create"/></button>
								</div>
							</div>
						</div>
						<div class="gridWrap">
							<div id="gridCampaignManage"></div>
							<div id="pagingCampaignManage"></div>
						</div>
					</div>

					<div class="tabbar_cont">
						<div class="ui_acrticle">
							<div class="ui_pannel_row">
								<div class="ui_float_left">
									<form>
										
									</form>
								</div>
								<div class="ui_float_right">
									<button class="icon_btn_trash_gray" id="deleteCampaign2"></button>
									<button class="ui_main_btn_flat copyCampaign2"><spring:message code="evaluation.campaign.copyCampaign"/></button>
									<%-- <button class="ui_main_btn_flat" id="campaignReuse" onclick="reUse();" data-target="uc"><spring:message code="evaluation.campaign.outOfDate.reuse"/></button> --%>
								</div>
							</div>
						</div>
						<div class="gridWrap">
							<div id="gridCampaignHistory"></div>
							<div id="pagingCampaignHistory"></div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>

	<div id="campaginMagician" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.campaign.ongoing.create"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray" onclick="gridCampaignMagicianClear();"></button>
					</div>
				</div>
			</div>
			<div class="evaluation_tit">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<input class="ui_input_title icon_input_tit" type="text"
							id="rEcampName" placeholder="<spring:message code="evaluation.campaign.ongoing.create"/>" /> <input
							class="ui_input_title icon_input_info" type="text"
							id="rEcampContent" placeholder="<spring:message code="evaluation.campaign.placeholder.campContent"/>" />
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<form class="campagin_term_form">
							<fieldset class="search_fieldset" id="evalDate"><legend><spring:message code="evaluation.campaign.ongoing.date"/></legend>
								<select id="rEvalTerm" required>
									<option value="" disabled selected><spring:message code="evaluation.campaign.ongoing.date"/></option>
									<option value="N"><spring:message code="evaluation.campaign.create.duration"/></option>
									<option value="Y"><spring:message code="evaluation.campaign.create.durationEnabled"/></option>
								</select>
								<input type="text" id="sDate" class="ui_input_cal icon_input_cal" disabled="disabled" placeholder="<spring:message code="evaluation.campaign.ongoing.evalStart"/>" />
								<input type="text" id="eDate" class="ui_input_cal icon_input_cal" disabled="disabled" placeholder="<spring:message code="evaluation.campaign.create.endEval"/>" />
							</fieldset>
						</form>
						<div class="campSetting">
							<div class="checks etrans">
								<input type="checkbox" id="selectEvalGroupId" data-target="4">
								<label for="selectEvalGroupId"><spring:message code="evaluation.campaign.grid.evalTarget"/></label>
							</div>
						</div>
						

						<form>
							<input type="text" placeholder="<spring:message code="evaluation.campaign.create.evalGroup"/>"
								class="ui_input_hasinfo groupCode" style ="display: none; ">
							<input type="text" placeholder="<spring:message code="evaluation.campaign.create.evalGroup"/>"
								class="ui_input_hasinfo group" style ="display: none; margin-top: 16px;">
						</form>
						<button id="selectGroup" class="ui_main_btn_flat" style ="display: none; margin-top: 16px;"
							onClick="layer_popup('#campaignViewGroup', function(){}); campaignGridClear();"><spring:message code="evaluation.campaign.grid.evalTargetSet"/></button>
					</div>
				</div>
				<div class="ui_pannel_row">
					<div class="ui_float_left">
					<form class="campagin_option_form">
						<fieldset class="search_fieldset" id="evalDegree"><legend><spring:message code="evaluation.campaign.grid.evalCount"/></legend>
							<input type="number" placeholder="<spring:message code="evaluation.campaign.grid.evalCount"/>" class="evalDegree" style="float:left;width:117px;cursor: allowed !important;">
						</fieldset>
					</form>
					<div class="campSetting">
						<div class="checks etrans">
							<input type="checkbox" id="objection" data-target="2">
							<label for="objection"><spring:message code="evaluation.campaign.create.disagree"/></label>
						</div>
					</div>
					<div class="campSetting">
						<div class="checks etrans">
							<input type="checkbox" id="revaluation" data-target="3">
							<label for="revaluation"><spring:message code="evaluation.campaign.create.reEval"/></label>
						</div>
					</div>
					<div class="campSetting" >
						<div class="checks etrans">
							<input type="checkbox" id="selectEvalAssign" data-target="5">
							<label for="selectEvalAssign"><spring:message code="evaluation.campaign.grid.evalShare"/></label>
						</div>
					</div>
						<%-- <form class="campagin_option_form">
							
							<select required class="rRuleValCode" id="objection" data-target="2">
								<option value="" disabled selected><spring:message code="evaluation.campaign.create.disagree"/></option>
								<option value="Y"><spring:message code="evaluation.campaign.create.permission"/></option>
								<option value="N"><spring:message code="evaluation.campaign.create.forbidden"/></option>
							</select>
							<select required class="rRuleValCode" id="revaluation" data-target="3">
								<option value="" disabled selected><spring:message code="evaluation.campaign.create.reEval"/></option>
								<option value="Y"><spring:message code="evaluation.campaign.create.permission"/></option>
								<option value="N"><spring:message code="evaluation.campaign.create.forbidden"/></option>
							</select>
							<select required class="rRuleValCode" id="scoringSystem" data-target="4">
								<option value="" disabled selected><spring:message code="evaluation.campaign.create.socoring"/></option>
								<option value="Y"><spring:message code="evaluation.campaign.create.permission"/></option>
								<option value="N"><spring:message code="evaluation.campaign.create.forbidden"/></option>
							</select>
							<input type="text" placeholder="평가 차수" class="evalDegree" style="cursor: allowed !important;">
							<select required class="rRuleValCode selectEvalAssign" data-target="5" id="selectEvalAssign">
								<option value="" disabled selected>평가 분배</option>
								<option value="Y"><spring:message code="evaluation.campaign.create.permission"/></option>
								<option value="N"><spring:message code="evaluation.campaign.create.forbidden"/></option>
							</select>
						</form> --%>
						<form>
							<input type="text" placeholder="<spring:message code="evaluation.campaign.grid.evalShareSet"/>" style ="display: none; " class="ui_input_hasinfo assignCode"/>
							<input type="text" placeholder="<spring:message code="evaluation.campaign.grid.evalShareSet"/>" style ="display: none; " class="ui_input_hasinfo assign"/>
						</form>
						<button id="evalAssign" class="ui_main_btn_flat" style ="display: none;"
								onClick="layer_popup('#campaignViewAssign', function(){}); campaignGridClear();"><spring:message code="evaluation.campaign.grid.evalShareSet"/></button>
					</div>

					</div>
				</div>
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<form class="campagin_choice_form">
							<fieldset class="search_fieldset" id="evalSheet"><legend><spring:message code="evaluation.campaign.grid.evalsheet"/></legend>
								<select class="campagin_select" required>
									<option value="" disabled selected><spring:message code="evaluation.campaign.create.sheetSelect"/></option>
								</select>
								<input type="text" placeholder="<spring:message code="evaluation.campaign.create.evaldescription"/>" class="campagin_info ui_input_hasinfo" id="sheetContent" />
								<input type="text" placeholder="<spring:message code="evaluation.campaign.create.step"/>" class="campagin_step ui_input_hasinfo" id="sheetDepth" />
								<input type="text" placeholder="<spring:message code="evaluation.campaign.grid.evalsheetTotalScore"/>" class="ui_input_hasinfo" id="sheetScore"/>
							</fieldset>
						</form>
						<button class="ui_main_btn_flat insertSheet" style="display: none;"><spring:message code="evaluation.campaign.create.add"/></button>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right" id="page">
						<form class="result_form">
							<input type="hidden" id="rEcampCode" /> <%-- <label><spring:message code="evaluation.campaign.sheetNumber"/></label>
							<input type="text" class="ui_input_hasinfo" /> --%> <%-- <label><spring:message code="evaluation.campaign.sumCampScore"/></label>
							<input type="text" class="ui_input_hasinfo" /> --%>
						</form>
						<button class="ui_main_btn_flat deleteSheet" style="display: none;"><spring:message code="evaluation.campaign.deleteSheet"/></button>

						<button class="ui_main_btn_flat makeCampaign"><spring:message code="evaluation.campaign.ongoing.create"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>


	<div id="campaignViewGroup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.campaign.evalGroupSelect"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close2 ui_btn_white icon_btn_exit_gray" onclick ="closeGroupPopup();"  style=" height: 23px; margin-top: 15%;"></button>
					</div>
				</div>
			</div>

			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
					<form>
						<select required class="rRuleValCode groupSelected">
								<option value="0"  disabled selected><spring:message code="evaluation.campaign.grid.evalTargetSet"/></option>
								<option value="1"><spring:message code="evaluation.campaign.byGroup"/></option>
								<option value="2"><spring:message code="evaluation.campaign.bySkill"/></option>
								<option value="3"><spring:message code="evaluation.campaign.byPerson"/></option>
								<option value="4"><spring:message code="evaluation.campaign.byAffilicate"/></option>
							</select>
						<select class="rRuleValCode group" id="group">
						<option value='' disabled selected><spring:message code="evaluation.campaign.selectGroupType"/></option>
						
						</select>
						<input type="text" id="selectName" style="display: none;" placeholder="<spring:message code="evaluation.campaign.selectGroupType"/>"/>
						</form>
						<button id = "allCheck"><spring:message code="evaluation.campaign.all"/></button>
						<button id="noCheck"><spring:message code="evaluation.campaign.selectionCancle"/></button>
					</div>

					<div class="ui_float_right">
						<button class="ui_main_btn_flat" id="selectedGruops"><spring:message code="evaluation.campaign.selectComplete"/></button>
					</div>
				</div>
				<div class="gridWrap">
					<div id="gridCampaignViewGroup"></div>
					<div id="pagingCampaignViewGroup"></div>
				</div>
			</div>
		</div>
	</div>

	<div id="campaignViewAssign" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="evaluation.campaign.grid.evalShareSet"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close3 ui_btn_white icon_btn_exit_gray" onclick ="closeGroupPopup();"></button>
					</div>
				</div>
			</div>

			<div class="ui_article">
				<div class="ui_pannel_row">

					<div class="ui_float_right">
						<button class="ui_main_btn_flat" id="selectedAssign"><spring:message code="evaluation.campaign.selectComplete"/></button>
					</div>
				</div>
				<div class="gridWrap">
					<div id="gridCampaignViewAssign"></div>
					<div id="pagingCampaignViewAssign"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
