<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../common/include/commonVar.jsp" %>

<!DOCTYPE html>
<html>
<head>

	<%-- css page --%>
 	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/evaluation_master/evaluation.css" />
	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/evaluation_master/question_manage.js"></script>
<script>
	$(function() {
		top.playerVisible(false);
		$(window).resize(function() {

			// 현재 document 높이
			var documentHeight = $(window).height();

			// 그리드 위의 높이 값
			var gridCalcHeight = $("#BgridQuestionManage").offset().top;

			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
			$("#BgridQuestionManage").css({"height": + (gridResultHeight - 2) + "px"});
			
			
			var documentHeight1 = $(window).height();
			var gridCalcHeight1 = $("#MgridQuestionManage").offset().top;

			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight1 = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			var gridResultHeight1 = (documentHeight1 - gridCalcHeight1 - pagingHeight1);
			$("#MgridQuestionManage").css({"height": + (gridResultHeight1 - 2) + "px"});
			
			var documentHeight = $(window).height();
			var gridCalcHeight = $("#SgridQuestionManage").offset().top;

			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
			$("#SgridQuestionManage").css({"height": + (gridResultHeight - 2) + "px"});
			var documentHeight = $(window).height();
			var gridCalcHeight = $("#IgridQuestionManage").offset().top;

			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
			$("#IgridQuestionManage").css({"height": + (gridResultHeight - 2) + "px"});
		}).resize();
	});
</script>
    <style>
        #BgridQuestionManage, #MgridQuestionManage, #SgridQuestionManage, #IgridQuestionManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
        }

        /* layer popup */
        #questionMagician, #questionMagician_Mark, #questionMagician2_Mark, #questionMagician2{
            width: 400px;
        }

        .itemContent_Mark1{
			float: left;
			clear: both;
		    width: calc(100% - 6.5em) !important;
		    padding: 10px 0 !important;
		    margin: 0.15em !important;
		}

		.itemMark1{
			float: right;
		    width: calc(100% - 21em) !important;
		    padding: 10px 0 !important;
		    margin: 0.15em !important;
		}

    </style>
</head>
<body onload="qustionManagaLoad()">
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
            		<ul>
                        <li class="tabbar_menu" name="b_tab"><spring:message code="evaluation.management.sheet.big"/></li>
                        <li class="tabbar_menu" name="m_tab"><spring:message code="evaluation.management.sheet.Middle"/></li>
                        <li class="tabbar_menu" name="s_tab"><spring:message code="evaluation.management.sheet.Small"/></li>
                        <li class="tabbar_menu" name="m_tab"><spring:message code="evaluation.management.sheet.item"/></li>
            		</ul>
            	</div>
            	<div class="ui_tabbar_section">

                    <div class="tabbar_cont">
	                    <div class="ui_article">
	                        <div class="ui_pannel_row">
	                            <div class="ui_float_left">
	                            </div>
	                            <div class="ui_float_right">
	                                <button class="ui_btn_white icon_btn_trash_gray delete_item" data-target="bg"></button>
	                           		<button class="ui_btn_blue update_item" data-target="ub"><spring:message code="evaluation.management.sheet.modify"/></button>
	                                <button class="ui_btn_blue popup_make_item" data-target="bg" onclick="layer_popup('#questionMagician');"><spring:message code="evaluation.management.sheet.create2"/></button>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="gridWrap">
	                        <div id="BgridQuestionManage"></div>
	                        <div id="pagingEvaluationManage"></div>
	                    </div>
	                </div>

                     <div class="tabbar_cont">
	                    <div class="ui_article">
	                        <div class="ui_pannel_row">
	                            <div class="ui_float_left">
	                               
	                            </div>
	                            <div class="ui_float_right">
	                                <button class="ui_btn_white icon_btn_trash_gray delete_item" data-target="mg"></button>
	                           		<button class="ui_btn_blue update_item" data-target="um"><spring:message code="evaluation.management.sheet.modify"/></button>
	                                <button class="ui_btn_blue popup_make_item" data-target="mg" onclick="layer_popup('#questionMagician');"><spring:message code="evaluation.management.sheet.create2"/></button>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="gridWrap">
	                        <div id="MgridQuestionManage"></div>
	                        <div id="pagingMEvaluationManage"></div>
	                    </div>
	                </div>

                    <div class="tabbar_cont">
	                    <div class="ui_article">
	                        <div class="ui_pannel_row">
	                            <div class="ui_float_left">
	                               
	                            </div>
	                            <div class="ui_float_right">
	                                <button class="ui_btn_white icon_btn_trash_gray delete_item" data-target="sg"></button>
	                           		<button class="ui_btn_blue update_item" data-target="us"><spring:message code="evaluation.management.sheet.modify"/></button>
	                                <button class="ui_btn_blue popup_make_item" data-target="sg" onclick="layer_popup('#questionMagician');"><spring:message code="evaluation.management.sheet.create2"/></button>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="gridWrap">
	                        <div id="SgridQuestionManage"></div>
	                        <div id="pagingSEvaluationManage"></div>
	                    </div>
	                </div>

                    <div class="tabbar_cont">
                    	 <div class="ui_article">
	                        <div class="ui_pannel_row">
	                            <div class="ui_float_left">
	                               
	                            </div>
	                            <div class="ui_float_right">
	                                <button class="ui_btn_white icon_btn_trash_gray delete_item" data-target="ig"></button>
	                          	 	<button class="ui_btn_blue update_item" data-target="ui"><spring:message code="evaluation.management.sheet.modify"/></button>
	                                <button class="ui_btn_blue popup_make_item" data-target="ig" onclick="layer_popup('#questionMagician_Mark');"><spring:message code="evaluation.management.sheet.create2"/></button>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="gridWrap">
	                        <div id="IgridQuestionManage"></div>
	                        <div id="pagingIEvaluationManage"></div>
	                    </div>
                    </div>
            	</div>
            </div>
        </div>
    </div>

    <div id="questionMagician" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.create2"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" onclick="emptySubItem();"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                        <input class="ui_input_title icon_input_tit" id="itemCode" type="text"  placeholder="<spring:message code="evaluation.management.sheet.itemtitle"/>"/>
                        <input class="ui_input_title icon_input_info" id="itemContent" type="text" placeholder="<spring:message code="evaluation.management.sheet.itemdescrip"/>"/>
                	</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<input type="hidden" id="bmsCode" />
                        <button class="ui_main_btn_flat insert_item"><spring:message code="evaluation.management.sheet.create2"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="questionMagician2" class="popup_obj" >
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">

                        <p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.modify"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" onclick="emptySubItem();"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                        <input class="ui_input_title icon_input_tit" id="itemName" type="text"  placeholder="<spring:message code="evaluation.management.sheet.itemtitle"/>"/>
                        <input class="ui_input_title icon_input_info" id="itemContent2" type="text" placeholder="<spring:message code="evaluation.management.sheet.itemdescrip"/>"/>

                	</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<input type="hidden" id="bmsCode" />
                        <button class="ui_main_btn_flat update_btn"><spring:message code="evaluation.management.sheet.modify"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="questionMagician_Mark" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.create2"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" onClick="emptySubItem();"></button>
                    </div>
                </div>
            </div>

            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<select class="ui_input_title sgList" id="sgList" style ="display: none;"><option value="" disabled selected><spring:message code="evaluation.management.sheet.selectsmall"/></option></select>

                        <input class="ui_input_title icon_input_tit" id="itemCode_Mark" type="text" placeholder="<spring:message code="evaluation.management.sheet.itemtitle"/>"/>
                        <button class="addSubMark" id="addSubMark"><spring:message code="message.btn.add"/></button>

                        <div class="subitem" id="subitem">
                       		<input class="subitem itemContent_Mark1 icon_input_tit" id="itemContent_Mark1" type="text" placeholder="subitem Name"/>
             				<input class="subitem itemMark1" id="itemMark1" type = "number" placeholder="Score"/>
             			</div>

                        
                	</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<input type="hidden" id="bmsCode" />
                        <button class="ui_main_btn_flat insert_item" data-target="ig"><spring:message code="evaluation.management.sheet.create2"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="questionMagician2_Mark" class="popup_obj" >
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">

                        <p class="ui_pannel_tit"><spring:message code="evaluation.management.sheet.modify"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" onClick="emptySubItem();"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                        <select class="ui_input_title icon_input_tit sgList" id="sgList2" style="display: none;"><spring:message code="evaluation.management.sheet.selectsmall"/></select>
                        <input class="ui_input_title icon_input_tit" id="itemName_Mark" type="text"  placeholder="<spring:message code="evaluation.management.sheet.itemtitle"/>"/>

                        <input class="ui_input_title icon_input_info" id="itemContent2_Mark" type="text" style="display: none;" placeholder="<spring:message code="evaluation.management.sheet.itemdescrip"/>"/>
                        <input class="ui_input_title icon_input_info" id="updateItemMark" type="text" style="display: none;" placeholder="<spring:message code="evaluation.management.sheet.itemscore"/>"/>
                        <button class="modiSubMark" id="modiSubMark" style = "display: none;">추가</button>
                        <div class="subitem" id="subitem">
                      	  <div class="mode_cont" id="subMark2"></div>
						</div>
                	</div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<input type="hidden" id="bmsCode" />
                        <button class="ui_main_btn_flat update_btn" data-target="ui"><spring:message code="evaluation.management.sheet.modify"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
