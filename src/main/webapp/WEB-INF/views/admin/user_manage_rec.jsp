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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/user_manage_rec.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/user_manage_rec.js"></script>
	<script>
		var locale = "${locale}";
		var hidden_rUserInfo = "${hidden_rUserInfo}";
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
				if(tabMode=="Y"){
					$("#userManageGrid").css({"height": + (gridResultHeight - 86) + "px"});
					//$(".ui_tree_wrap").css({"height": + (gridResultHeight - 90) + "px"});
					$("#treeViewAgent").css({"height": + (gridResultHeight - 53) + "px"});
					//$(".group_list_wrap").css({"height": + (gridResultHeight - 141) + "px"});
				} else{
					$("#userManageGrid").css({"height": + (gridResultHeight - 174) + "px"});
					//$(".ui_tree_wrap").css({"height": + (gridResultHeight - 90) + "px"});
					$("#treeViewAgent").css({"height": + (gridResultHeight - 142) + "px"});
					//$(".group_list_wrap").css({"height": + (gridResultHeight - 141) + "px"});
				}
				
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
       	
       	#treeViewAgent {
       		overflow:visible !important;
       	}
	</style>
</head>
<body onload="userManageLoad();">
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
		                    <input title="<spring:message code="admin.label.userId"/>" class="inputFilter engFilter numberFilter" id="userId" type="text" placeHolder="<spring:message code="admin.label.userId"/>"/>
		                    <input title="<spring:message code="admin.label.name"/>" class="inputFilter korFilter engFilter numberFilter" id="userName" type="text" placeHolder="<spring:message code="admin.label.name"/>"/>
		                    <input title="<spring:message code="admin.label.extNo"/>" class="inputFilter numberFilter" id="extNo" type="text" style="display:none;" placeHolder="<spring:message code="admin.label.extNo"/>"/>
		                    <select title="<spring:message code="admin.label.authy"/>" id="Authy" >
		                    </select>
		                    <select title=<spring:message code="views.search.grid.head.EmploymentCategory"/> id="EmploymentSearch" >
		                	 	<option selected="selected" value="Y">재직자</option>
		                	 	<option value="N">퇴사자</option>
		                  	</select>
		                  	<select title=<spring:message code="admin.label.skinCode"/> id="skinCode" >
		                	 	<option selected="selected" value=""><spring:message code="admin.label.skinCode"/></option>
		                	 	<option value="Y">사용</option>
		                	 	<option value="N">미사용</option>
		                  	</select>
			                <button id="searchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>
		                </div>
					</div>
					<div class="ui_float_right">
						<button id= "pwPolicyBtn" class="ui_main_btn_flat icon_btn_key_white"><spring:message code="admin.alert.recUser41"/></button>
						<button id="userAddBtn" class="ui_main_btn_flat icon_btn_adduser_white"><spring:message code="admin.label.addUser"/></button>
						<button id="MultiModigroupBtn" class="ui_main_btn_flat"><spring:message code="admin.label.MultiModiGroup"/></button>						
						<button id="excelDownBtn"class="ui_main_btn_flat icon_btn_exelTxt_white"><spring:message code="common.excel.download"/></button>
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
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userId"/></label>
                        	<input maxlength="255" class="inputFilter disableTarget"type="text" id="mUserId"/>   
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userName"/></label>
                        	<input class="inputFilter korFilter engFilter numberFilter disableTarget" type="text" id="mUserName" maxlength="30"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.password"/></label>
                        	<input type="password" id="mPassword" class="disableTarget"/>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.passwordCheck"/></label>
                        	<input type="password" id="mPasswordCheck" class="disableTarget"/>
                    	<label class="mExtNum"><spring:message code="header.popup.modifyUser.label.ext"/></label>
	                        <select id="mExtNum" class="mExtNum disableTarget"></select>
						<label class="mUserPhone"><spring:message code="header.popup.modifyUser.label.phoneNo"/></label>
							<input maxlength="11" class="inputFilter numberFilter mUserPhone disableTarget" type="text" id="mPhoneNumber"/>
						<label class="mSex"><spring:message code="header.popup.modifyUser.label.sex"/></label>
                        	<select class="mSex disableTarget" id="mSex">
                        		<option id="m"><spring:message code="header.popup.modifyUser.option.male"/></option>
                        		<option id="w"><spring:message code="header.popup.modifyUser.option.female"/></option>
                        	</select>
                    	<label class="ui_label_essential"><spring:message code="header.popup.modifyUser.label.userLevel"/></label>
	                        <select id="mAuthy" class="disableTarget"></select>
                    	<label class="ui_label_essential"><spring:message code="views.search.grid.head.R_BG_NAME"/></label>
	                        <select id="mBgCode" class="disableTarget"></select>
                    	<label><spring:message code="views.search.grid.head.R_MG_NAME"/></label>
	                        <select id="mMgCode" class="disableTarget"></select>
                    	<label><spring:message code="views.search.grid.head.R_SG_NAME"/></label>
	                        <select id="mSgCode" class="disableTarget"></select>
                    	<label class="mEmail">EMAIL</label>
                        	<input class="mEmail disableTarget" type="text" id="mEmail"/>
                        <label class="mUseYN" style="display:none;">사용여부</label>
                        	<select class="mUseYN disableTarget" id="mUseYN"  style="display:none;">
                        		<option value="Y">사용</option>
                        		<option value="N">미사용</option>
                        	</select>
                       	<label class="ui_label_essential mClientIp"><spring:message code="admin.grid.clientIp"/></label>
                        	<input type="text" class='mClientIp disableTarget' id="mClientIp" maxlength='15'/>
                    	<label class="mCtiId">CTI ID</label>
                       		<input class="mCtiId disableTarget" type="text" id="mCtiId"/>
						<label class="EmploymentCategory" style="display:none;"><spring:message code="views.search.grid.head.EmploymentCategory"/></label>	
                        	<select title=<spring:message code="views.search.grid.head.EmploymentCategory"/> id="EmploymentCategory" class="disableTarget" style="display:none;">
		                	 	<option selected="selected" value="Y"><spring:message code="views.search.grid.head.Incumbent"/></option>
		                	 	<option value="N"><spring:message code="views.search.grid.head.Retiree"/></option>
		                  	</select>
                        <%-- <label class="BatchUsageStatus"><spring:message code="views.search.grid.head.BatchUsageStatus"/></label> --%>
                        <label class="skinCode">신규 녹취 사용</label>
                        	<select title="신규 녹취 사용" id="mSkinCode" >
								<option selected="selected" value="Y"><spring:message code="admin.detail.option.use"/></option>
			                	 <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
		                	</select>
		                	
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
                        	<input id="cycleNum" type=number class="policySubGroup cyclePolicy input_width_50 inputFilter numberFilter" maxlength="2" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
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
                        	<input id="pastNum" type=number class="policySubGroup inputFilter numberFilter" maxlength="2" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
                        <label><spring:message code="admin.label.tryPolicy"/></label>
                        	<select id="tryPolicy" class="policyGroup">
	                            <option value="Y"><spring:message code="admin.detail.option.use"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                    <label><spring:message code="admin.label.tryNumPolicy"/></label>
                        	<input id="tryNum" type=number class="policySubGroup inputFilter numberFilter" maxlength="2" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
                        <label><spring:message code="admin.label.lockPolicy"/></label>
                        	<select id="lockPolicy" class="policyGroup">
	                            <option value="Y"><spring:message code="admin.detail.option.use"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.notUse"/></option>
	                        </select>
	                   	<label><spring:message code="admin.label.lockNumTerm"/></label>
	                   	<input id="lockNum" type=number class="policySubGroup input_width_50 inputFilter numberFilter" maxlength="2" type="text" placeholder="<spring:message code="admin.label.enterNum"/>"/>
	                        <select id="lockDate" class="policySubGroup select_width_50 select_margin_right_0">
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
                       	<label class="ui_label_essential">신규 녹취 사용</label>
	                        <select id="groupUseYN">
	                        	<option value="Y" selected="selected">사용</option>
	                        	<option value="N">미사용</option>
	                        </select>
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
    
    <div id="multiModifyGroup" class="popup_obj" style="width: 400px;">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.MultiModiGroup"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="views.search.grid.head.R_BG_CODE"/></label>
	                        <select id="MbgGroup"></select>
	                    <label class=""><spring:message code="views.search.grid.head.R_MG_CODE"/></label>
	                        <select id="MmgGroup"></select>
                    	<label class=""><spring:message code="views.search.grid.head.R_SG_CODE"/></label>
	                        <select id="MsgGroup"></select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="MgroupModifyBtn" class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.label.MultiModiGroup"/></button>
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
<iframe name="downloadFrame" id="downloadFrame" src="" style="display:none;"></iframe>
</body>
</html>