<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/user_manage.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/user_manage.js"></script>
	<script>
		var locale = "${locale}"
		var hidden_aUserInfo = "${hidden_aUserInfo}";
	</script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#userManageGrid").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = documentHeight;

				// 탭 전환 등의 시점 때문에 임의로 계산
				$("#userManageGrid").css({"height": + (gridResultHeight - 176) + "px"});
				$("#treeViewAgent").css({"height": + (gridResultHeight - 90) + "px"});
				$(".ui_tree_wrap").css({"height": + (gridResultHeight - 90) + "px"});
				$(".group_list_wrap").css({"height": + (gridResultHeight - 141) + "px"});
		    }).resize();
		})
	</script>

	<style>
		#userManageGrid{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
        /* layer popup */
        #addGroup{
            width: 400px;
        }
        #addUser{
            width: 400px;
        }
        #passwordManage{
            width: 400px;
        }
        #groupExelUpload{
            width: 400px;
        }
        .tree_view_wrap{
 			margin-top: 48px;   		
       	}
       	.ui_row_input_wrap .input_width_50{
       	    width: calc(50% - 86px);
       	}
       	
	</style>
</head>
<body onload="userManageLoad()">
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
    <div class="ui_layout_pannel">
		<%@ include file="./admin_menu.jsp" %>
	    <div class="main_contents admin_body">
	        <div class="ui_acrticle group_tree_pannel">
				<div class="ui_pannel_row">
	                <div class="ui_float_left">
		                <button id="btnGroupAdd" class="ui_main_btn_flat icon_btn_addgroup_white" ><spring:message code="admin.label.addGroup"/></button>
		                <button id="btnGroupModify" class="ui_main_btn_flat icon_btn_addgroup_white" ><spring:message code="admin.label.modiGroup"/></button>
		                <%-- <button class="ui_main_btn_flat icon_btn_exelTxt_white" onclick="layer_popup('#groupExelUpload');"><spring:message code="management.group.form.title.excelUpload"/></button> --%>
						<button id="btnGroupDelete" class="ui_btn_white icon_btn_trash_gray"></button>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>
		         <div class="tree_view_wrap">
		            <div class="tree_agent_view" id="treeViewAgent"></div>
				</div>
			</div>

			<div class="ui_acrticle user_manage_pannel">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<div class="main_form">
		                    <input maxlength="255" title=<spring:message code="admin.grid.emp"/> class="inputFilter input_userid" id="userId" type="text" placeHolder="<spring:message code="admin.grid.emp"/>"/>
		                    <input title=<spring:message code="admin.label.name"/> class="inputFilter korFilter engFilter numberFilter" id="userName" type="text" placeHolder=<spring:message code="admin.label.name"/>/>
			                <button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>
		                </div>
							<button id= "pwPolicyBtn" class="ui_main_btn_flat icon_btn_key_white"><spring:message code="admin.alert.recUser41"/></button>
					</div>
					<div class="ui_float_right">
							<button id="userAddBtn" class="ui_main_btn_flat icon_btn_adduser_white"><spring:message code="admin.label.addUser"/></button>
							<button id="userDeleteBtn" class="ui_btn_white icon_btn_trash_gray"></button>
					</div>
				</div>
				<div class="gridWrap">
					<div id="userManageGrid"></div>
					<div id="userManagePaging"></div>
				</div>
			</div>
		</div>
    </div>


    <div id="addUser" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.addUser"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="search.option.empId"/></label>
                        	<input maxlength="255" class="inputFilter"type="text" id="mUserId"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userName"/></label>
                        	<input class="inputFilter korFilter engFilter numberFilter" type="text" id="mUserName" maxlength="30" />
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.password"/></label>
                        	<input type="password" id="mPassword"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.passwordCheck"/></label>
                        	<input type="password" id="mPasswordCheck"/>
						<label class="telPhoneNum"><spring:message code="admin.label.phone"/></label>
							<input maxlength="11" class="inputFilter numberFilter" type="text" id="mPhoneNumber"/>
						<label class="telSex"><spring:message code="header.popup.modifyUser.label.sex"/></label>
                        	<select id="mSex">
                        		<option id="m"><spring:message code="admin.label.men"/></option>
                        		<option id="w"><spring:message code="admin.label.women"/></option>
                        	</select>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userLevel"/></label>
	                        <select id="mAuthy"></select>
                    	<label class="choose telBg" ><spring:message code="views.search.grid.head.R_BG_CODE"/></label>
	                        <select id="mBgCode"></select>
                    	<label class="choose telMg"><spring:message code="views.search.grid.head.R_MG_CODE"/></label>
	                        <select id="mMgCode"></select>
                    	<label class="choose telSg"><spring:message code="views.search.grid.head.R_SG_CODE"/></label>
	                        <select id="mSgCode"></select>
                    	<label class="telEmail">EMAIL</label>
                        	<input type="text" id="mEmail"/>
                       	<input type="hidden" id="rowId"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="mUserAddBtn" class="ui_main_btn_flat icon_btn_adduser_white"><spring:message code="admin.label.addUser"/></button>
                        <button id="mUserModifyBtn" style="display:none" class="ui_main_btn_flat icon_btn_adduser_white"><spring:message code="admin.label.modiUser"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="passwordManage" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.passwordPolicySetting"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label><spring:message code="admin.label.pwPolicy"/></label>
	                        <select id="pwPolicy">
	                            <option value="Y"><spring:message code="admin.detail.option.use"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
                    	<label><spring:message code="admin.label.pwModiTerm"/></label>
	                         <select id="cyclePolicy" class="policyGroup">
	                            <option value="F"><spring:message code="admin.detail.option.use"/><spring:message code="admin.label.policyF"/></option>
	                            <option value="Y"><spring:message code="admin.detail.option.use"/><spring:message code="admin.label.policyY"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
                    	<label><spring:message code="admin.label.setModiTerm"/></label>
                        	<input id="cycleNum" type=number class="policySubGroup cyclePolicy input_width_50" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
	                        <select id="cycleDate" class="policySubGroup cyclePolicy select_width_50 select_margin_right_0">
	                            <option value="D"><spring:message code="admin.label.cycleDateD"/></option>
	                            <option value="W"><spring:message code="admin.label.cycleDateW"/></option>
	                            <option value="M"><spring:message code="admin.label.cycleDateM"/></option>
	                            <option value="Y"><spring:message code="admin.label.cycleDateY"/></option>
	                        </select>
                    	<label><spring:message code="admin.label.lastPw"/></label>
	                        <select id="pastPolicy" class="policyGroup">
	                            <option value="Y"><spring:message code="admin.label.pastPolicyY"/></option>
	                            <option value="C"><spring:message code="admin.label.pastPolicyC"/></option>
	                            <option value="N"><spring:message code="admin.label.pastPolicyB"/></option>
	                        </select>
                    	<label><spring:message code="admin.label.policySubGroup"/></label>
                        	<input id="pastNum" type=number class="policySubGroup" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
                        <label><spring:message code="admin.label.tryPolicy"/></label>
                        	<select id="tryPolicy" class="policyGroup">
	                            <option value="Y"><spring:message code="admin.detail.option.use"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                   <label><spring:message code="admin.label.tryNumPolicy"/></label>
                        	<input id="tryNum" type=number class="policySubGroup" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
                        <label><spring:message code="admin.label.lockPolicy"/></label>
                        	<select id="lockPolicy" class="policyGroup">
	                            <option value="Y"><spring:message code="admin.detail.option.use"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                     	<label><spring:message code="admin.label.lockNumTerm"/></label>
	                   	<input id="lockNum" type=number class="policySubGroup cyclePolicy input_width_50" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
	                        <select id="lockDate" class="policySubGroup cyclePolicy select_width_50 select_margin_right_0">
	                            <option value="D"><spring:message code="admin.label.lockDateD"/></option>
	                            <option value="W"><spring:message code="admin.label.lockDateW"/></option>
	                            <option value="M"><spring:message code="admin.label.lockDateM"/></option>
	                            <option value="Y"><spring:message code="admin.label.lockDateY"/></option>
	                        </select>


                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="modifyPwPolicy" class="ui_main_btn_flat icon_btn_key_white"><spring:message code="admin.label.policySet1"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>



    <div id="addGroup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.addGroup"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.label.groupUnit"/></label>
	                        <select id="seletCode">
	                            <option value="bgCode"><spring:message code="views.search.grid.head.R_BG_CODE"/></option>
	                            <option value="mgCode"><spring:message code="views.search.grid.head.R_MG_CODE"/></option>
	                            <option value="sgCode"><spring:message code="views.search.grid.head.R_SG_CODE"/></option>
	                        </select>
                    	<label class="ui_label_essential"><spring:message code="views.search.grid.head.R_BG_CODE"/></label>
	                        <select id="bgGroup"></select>
	                    <label class="ui_label_essential"><spring:message code="views.search.grid.head.R_MG_CODE"/></label>
	                        <select id="mgGroup"></select>
                    	<label class="ui_label_essential"><spring:message code="admin.label.groupName"/></label>
                        	<input id="groupName" type="text" maxlength="15"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="groupAddBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.label.addGroup"/></button>
                        <button id="groupModifyBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.label.modiGroup"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="groupExelUpload" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="management.group.form.title.excelUpload"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding"></div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button><spring:message code="admin.label.formDownload"/></button>
                        <button class="ui_main_btn_flat icon_btn_exelTxt_white"><spring:message code="admin.label.complete"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<div class="message_area"></div>
</body>