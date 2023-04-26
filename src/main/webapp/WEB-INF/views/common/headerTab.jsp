<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="<c:out value="${siteResourcePath}"/>/css/page/header.css" />
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/fontawesome-free-5.0.8/web-fonts-with-css/css/fontawesome-all.min.css" />
	<%-- <link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/evaluation/evaluation.css" /> --%>

	<%-- js page --%>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/headerTab.js"></script>
	<%-- RSA 암호 모듈 --%>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/jsbn.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/rsa.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/prng4.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/rng.js"></script>

	<script>
		var locale = "${locale}"
		var readYn = "${nowAccessInfo.getReadYn()}"
		var writeYn = "${nowAccessInfo.getWriteYn()}";
		var modiYn = "${nowAccessInfo.getModiYn()}";
		var delYn = "${nowAccessInfo.getDelYn()}"
		var listenYn = "${nowAccessInfo.getListenYn()}"
		var downloadYn = "${nowAccessInfo.getDownloadYn()}"
		var excelYn = "${nowAccessInfo.getExcelYn()}"
		var maskingYn = "${nowAccessInfo.getMaskingYn()}"
		var reciptYn = "${nowAccessInfo.getReciptYn()}"
		var approveYn = "${nowAccessInfo.getApproveYn()}"
		var preReciptYn = "${nowAccessInfo.getPrereciptYn()}"
		var downloadApprove = "${nowAccessInfo.getDownloadApprove()}"
		var encYn = "${nowAccessInfo.getEncYn()}"
		var accessLevel = "${nowAccessInfo.getAccessLevel()}"
		var dutyInfo = "${Duty}"	
		var feedbackModifyYn = "${nowAccessInfo.getFeedbackModifyYn()}"
		
    	// 파라미터 암호화
        var RSAModulus = "${sessionScope.RSAModulus}";
        var RSAExponent = "${sessionScope.RSAExponent}";
	$.ajax({
       		url:contextPath+"/logoSetting.do",
       		data:{
       			"logoType":"main"
       		},
       		type:"POST",
       		dataType:"json",
       		success:function(jRes){			
       			var logoChangeUse = jRes.resData.logoChangeUse;
       			if (logoChangeUse != null && logoChangeUse == "Y") {
       				$(".site_logo").css("background-image", "url('"+siteResourcePath + "/images/project/main/logo/main_logo.png')");
       			} else {
       				$(".site_logo").css("background-image", "url('"+siteResourcePath + "/images/project/main/logo/recsee_bi_blue_78x30.png')");
   				}
       		}
       	});
				
		// 세션에 저장된 유저 bg mg sg 저장
		var bginfo = "${USER_INFO.getBgCode()}"
		var mginfo = "${USER_INFO.getMgCode()}"
		var sginfo = "${USER_INFO.getSgCode()}"
	</script>

	<style>
		/* 평가시트 그리드 */
        #gridEvaluation{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			height: 300px !important;
        }

		/* layer popup */
        #modifyUser{
            width: 400px;
        }
        #evaluationSheet{
            width: 866px;
        }
	</style>

	<div class="headerWrap" style="display:none;">
	    <div class="main_header">
	        <div class="ui_layout_pannel">
	            <div class="site_logo" ><p style="display:none"></p></div>
	            <div class="user_info">
	                <div class="user_id"><p><span>${mgCodeName}&nbsp;&nbsp;${sgCodeName}&nbsp;&nbsp;&nbsp;&nbsp;</span>${userName} <span>[</span>${userId}<span>]</span> <span>님</span></p></div>
	                <%-- <button class="btn_gear" title="<spring:message code="header.title.settings"/>"></button> --%>
	                <%-- <button id="UserInfoBtn" class="btn_user" title="<spring:message code="header.title.modifyUser"/>" onclick="layer_popup('#modifyUser');"></button> --%>
	                <button class="btn_logout" title="<spring:message code="header.title.logout"/>" onclick="top.location.href='${contextPath}/logout'"></button>
	            </div>
	        </div>
	    </div>

	    <div class="main_lnb">
	        <div class="ui_layout_pannel">
	            <div class="top_menu">
	                <%-- <ul class="menu_agent">
	                    <li class="icon_menu_agent">
	                    	<a href="${webPath }/search/search" target="_self"><p><spring:message code="header.menu.label.searchNListen"/></p>
		                    	<ul></ul>
	                    	</a>
                    	</li>
	                    <li class="icon_menu_dashboard">
	                    	<a href="${webPath }/statistics/dashboard" target="_self"><p><spring:message code="header.menu.label.reportNDashboard"/></p>
		                    	<ul>
		                    		<li class=""><a href="${webPath }/statistics/dashboard" target="_self"><p><spring:message code="header.menu.label.dashboard"/></p></a></li>
		                    		<li class=""><a href="${webPath }/statistics/report_call_all" target="_self"><p><spring:message code="header.menu.label.callAll"/></p></a></li>
				                    <li class=""><a href="${webPath }/statistics/report_call_user" target="_self"><p><spring:message code="header.menu.label.callUser"/></p></a></li>
				                </ul>
	                    	</a>
                    	</li>
	                    <li class="icon_menu_monitoring">
	                    	<a href="${webPath }/monitoring/realtime" target="_self"><p><spring:message code="header.menu.label.monitoring"/></p>
		                    	<ul>
		                    		<li class=""><a href="${webPath }/monitoring/realtime" target="_self"><p><spring:message code="header.menu.label.realtimeMonitoring"/></p></a></li>
		                    		<li class=""><a href="${webPath }/monitoring/system" target="_self"><p>시스템 모니터링</p></a></li>
		                    		<li class=""><a href="${webPath }/monitoring/office_monitoring" target="_blank"><p>입체 모니터링</p></a></li>
		                    	</ul>
	                    	</a>
                    	</li>
	                    <li class="icon_menu_evaluation">
	                    	<a href="${webPath }/evaluation/evaluation_result" target="_self"><p><spring:message code="header.menu.label.evaluation"/></p>
		                    	<ul>
				                    <li class=""><a href="${webPath }/evaluation/evaluation_result" target="_self"><p><spring:message code="header.menu.label.evaluationList"/></p></a></li>
		                    		<li class=""><a href="${webPath }/evaluation/question_manage" target="_self"><p><spring:message code="header.menu.label.itemManage"/></p></a></li>
				                    <li class=""><a href="${webPath }/evaluation/evaluation_manage" target="_self"><p><spring:message code="header.menu.label.sheetManage"/></p></a></li>
				                    <li class=""><a href="${webPath }/evaluation/campaign_manage" target="_self"><p><spring:message code="header.menu.label.campaignManage"/></p></a></li>
		                    	</ul>
	                    	</a>
                    	</li>
	                </ul> --%>
	               <%--  <ul class="menu_admin">
	                    <li class="ui_main_btn_flat"><a href="${webPath }/admin/channel_manage" target="_self"><p><spring:message code="header.menu.label.administer"/></p></a></li>
	                </ul> --%>
	            </div>
	        </div>
	    </div>

    </div>

    <div id="modifyUser" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="header.popup.modifyUser.label.manageUserInfo"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.id"/></label>
                        	<input type="text" id="userId"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userName"/></label>
                        	<input type="text" id="userName"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.password"/></label>
                        	<input type="password" id="password"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.passwordCheck"/></label>
                        	<input type="password" id="passwordCheck"/>
                    	<%-- <label><spring:message code="header.popup.modifyUser.label.ext"/></label>
	                        <select id="ext"></select> --%>
					<%-- 	<label><spring:message code="header.popup.modifyUser.label.phoneNo"/></label>
							<input type="text" id="phoneNumber"/> --%>
						<%-- <label><spring:message code="header.popup.modifyUser.label.sex"/></label>
                        	<select id="sex">
                        		<option id="m"><spring:message code="header.popup.modifyUser.option.male"/></option>
                        		<option id="w"><spring:message code="header.popup.modifyUser.option.female"/></option>
                        	</select> --%>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userLevel"/></label>
	                        <select id="authy" class="ui_input_hasinfo"></select>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.level1"/></label>
	                        <select id="bgCode" class="ui_input_hasinfo"></select>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.level2"/></label>
	                        <select id="mgCode" class="ui_input_hasinfo"></select>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.level3"/></label>
	                        <select id="sgCode" class="ui_input_hasinfo"></select>
                    	<%-- <label><spring:message code="header.popup.modifyUser.label.empNo"/></label>
                        	<input type="text" id="empId"/> --%>
                    	<%-- <label><spring:message code="header.popup.modifyUser.label.email"/></label>
                        	<input type="text" id="email"/> --%>
                    	<%-- <label><spring:message code="header.popup.modifyUser.label.ctiId"/></label>
                       		<input type="text" id="ctiId" class="ui_input_hasinfo"/> --%>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="userModifyBtn" class="ui_main_btn_flat icon_btn_adduser_white"><spring:message code="header.popup.modifyUser.button.modify"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="evaluationSheet" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="header.popup.evaluation.label.evaluation"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
           	<iframe id="evalPlayerFrame" src="">
	            <div id="playerObj"class="togglePlayer">
	          	</div>
			</iframe>
            <div id="playerObj"></div>
            <div class="ui_article">
                <div class="ui_pannel_row" style="border-top: 1px solid #bbbbbb;">
                    <div class="ui_float_left">
                        <form class="campaign_view_form">
                            <select class="clear_target" name="campaign_list" required>
                                <option value="" disabled selected><spring:message code="header.popup.evaluation.option.selectCampaign"/></option>
                            </select>
                            <select class="clear_target" name="sheet_list" required>
                                <option value="" disabled selected><spring:message code="header.popup.evaluation.option.selectSheet"/></option>
                            </select>
                        </form>
                    </div>
                </div>
                <div class="ui_pannel_row">
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
                <div class="ui_pannel_row">
                    <div class="ui_float_left">
	                    <form class="comment_form">
	                        <label><spring:message code="header.popup.evaluation.label.evaluatorComment"/></label>
	                        <textarea class="clear_target" rows="2000"></textarea>
	                    </form>
	                    <form class="comment_form">
	                        <label><spring:message code="header.popup.evaluation.label.appraiseeComment"/></label>
	                        <textarea class="clear_target" rows="2000"></textarea>
	                    </form>
                   	</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_left">
                        <button class="ui_btn_white"><spring:message code="header.popup.evaluation.button.moveCampaign"/></button>
                    </div>
                    <div class="ui_float_right">
                        <form class="score_form">
                            <label><spring:message code="header.popup.evaluation.label.sheetScore"/></label>
                            <input type="text" value="" name="maxScore" class="clear_target ui_input_hasinfo"/>
                            <label><spring:message code="header.popup.evaluation.label.totalScore"/></label>
                            <input type="text" value="" name="currentScore" class="clear_target ui_input_hasinfo"/>
                        </form>
                        <button class="ui_btn_white"><spring:message code="header.popup.evaluation.button.interimStorage"/></button>
                        <button id="evaluate_complete_btn" class="ui_main_btn_flat"><spring:message code="header.popup.evaluation.button.done"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>