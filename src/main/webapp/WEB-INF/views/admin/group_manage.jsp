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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/group_manage.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/group_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridGroupManage").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#gridGroupManage").css({"height": + (gridResultHeight - 4) + "px"})
		    }).resize();
		})
	</script>

	<style>
		#gridGroupManage{
            position: relative;
            clear: both;
            float: left;
            width: 100% !important;
			height: 858px !important;
        }
        .group_tree_view{
        	float: left;
			width: 220px !important;
			background-color: #ffffff;
		}
		.group_userList_pannel{
			float: left;
			width: calc(100% - 221px) !important;
			clear: none !important;
			border-left: 1px solid #bbbbbb !important;
		}
        /* layer popup */
        #groupExelUpload{
            width: 400px;
        }
        #groupAddUser{
            width: 400px;
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
    <div class="ui_layout_pannel">
		<%@ include file="./admin_menu.jsp" %>
	    <div class="main_contents admin_body">
	        <div class="ui_acrticle group_tree_view">
	            <div class="ui_pannel_row">
	                <div class="ui_float_left">
		                <button class="ui_main_btn_flat icon_btn_addgroup_white"><spring:message code="admin.label.addGroup"/></button>
		                <button class="ui_main_btn_flat icon_btn_exelTxt_white" onclick="layer_popup('#groupExelUpload');"><spring:message code="management.group.form.title.excelUpload"/></button>
	                </div>
	                <div class="ui_float_right">
	                </div>
	            </div>
	            <div class="ui_tree_wrap">
					<ul id="browser" class="filetree">
						<li><span class="folder">개발연구소</span>
							<ul>
								<li><span class="folder">녹취파트</span>
									<ul>
										<li><span class="folder">웹 개발</span>
											<ul>
												<li><span class="folder system_group_user">서지은</span></li>
												<li><span class="folder system_group_user">김성인</span></li>
												<li><span class="folder system_group_user">김화랑</span></li>
											</ul>
										</li>
										<li><span class="folder">엔진 개발</span>
											<ul>
												<li><span class="folder system_group_user">이강욱</span></li>
												<li><span class="folder system_group_user">박준모</span></li>
												<li><span class="folder system_group_user">정석재</span></li>
											</ul>
										</li>
									</ul>
								</li>
							</ul>
						</li>
						<li><span class="folder">영업전략</span>
							<ul>
								<li><span class="folder">영업파트</span></li>
							</ul>
						</li>
						<li><span class="folder">경영지원</span>
							<ul>
								<li><span class="folder">회계파트</span></li>
							</ul>
						</li>
					</ul>
				</div>
				<script>
					$(function(){
						// 해당 함수를 호출안할시, 트리기능이 동작을 안함
						$("#browser").treeview({});
					});
				</script>
			</div>
	        <div class="ui_acrticle group_userList_pannel">
	        	<div class="ui_pannel_row">
	                <div class="ui_float_left">
		                <button class="ui_main_btn_flat icon_btn_adduser_white" onclick="layer_popup('#groupAddUser');"><spring:message code="admin.label.addGroupPeo"/></button>
	                    <button class="ui_btn_white icon_btn_trashTxt_gray"><spring:message code="admin.label.removeGroupPeo"/></button>
	                </div>
	                <div class="ui_float_right">
	                	<form>
	                        <select required>
	                            <option value="" disabled selected><spring:message code="views.search.grid.head.R_BG_CODE"/></option>
	                        </select>
	                        <select required>
	                            <option value="" disabled selected><spring:message code="views.search.grid.head.R_MG_CODE"/></option>
	                        </select>
	                        <select required>
	                            <option value="" disabled selected><spring:message code="views.search.grid.head.R_SG_CODE"/></option>
	                        </select>
	                    </form>
	                    <button class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>

	                </div>
                </div>
		        <div class="gridWrap">
			        <div id="gridGroupManage"></div>
			        <div id="pagingGroupManage"></div>
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
                    <div class="ui_padding">


                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button class="ui_main_btn_flat icon_btn_exelTxt_white"><spring:message code="admin.label.complete"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="groupAddUser" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.addGroupPeo"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">


                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button class="ui_main_btn_flat icon_btn_adduser_white"><spring:message code="message.btn.add"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>


	<div class="message_area">
	</div>
</body>