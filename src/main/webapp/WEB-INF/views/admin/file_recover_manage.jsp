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

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/file_recover_manage.js"></script>

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridFileRecoverManage").offset().top;
				
				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);
				
				$("#gridFileRecoverManage").css({"height": + (gridResultHeight - 25) + "px"})	
		    }).resize();
		})
	</script>

	<style>
        .ui_input_hasinfo{
		    background-color: #f1f1f1;
		    cursor: not-allowed !important;
		}
		#gridPacketLogManage{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
        #fileRecoverInfo {
        	width : 400px;
        }
        #fileRecovorSearchBtn {
        	float:left;
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
	        <div class="ui_acrticle">
	         	<div class="ui_pannel_row">
	                <div class="ui_float_left">
						<div class="main_form">
							<input maxlength="8" type="text" id="sDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilte" placeholder="Start Date"/>
							<input maxlength="8" type="text" id="eDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilte" placeholder="End Date"/>
							<select id="sTime" required>
								<option value="" disabled selected><spring:message code="common.label.start"/></option>
							</select>
							<select id="eTime" required>
								<option value="" disabled selected><spring:message code="common.label.end"/></option>
							</select>
							<select id="searchSysCode"></select>
	                        <input maxlength='20'  id="searchExtNum" class="inputFilter numberFilter" type="text" placeholder="내선"/>
	                        <input maxlength='15'  id="searchExtIp" class="inputFilter ipFilter" type="text" placeholder="내선 IP"/>
	                        <input maxlength='300'  id="searchCallId" class="inputFilter" type="text" placeholder="콜 아이디"/>
	                        <input maxlength='6'  id="searchsCallTtime" class="input_time time_to_sec inputFilter numberFilter timeFilter" type="text" placeholder="통화 시간(부터)"/>
	                        <input maxlength='6'  id="searcheCallTtime" class="input_time time_to_sec inputFilter numberFilter timeFilter" type="text" placeholder="통화 시간(까지)"/>
	                        <input maxlength='100'  id="searchCustPhone" class="inputFilter numberFilter" type="text" placeholder="고객 전화번호"/>
							<select id="searchCallKind" ></select>
	                        <input maxlength='100'  id="searchFileName" class="inputFilter" style="display:none;"type="text" placeholder="파일 이름"/>
	                        <select id="searchFileState" >
		                        <option value ="" >파일 상태</option>
		                        <option value = "N">기본 상태</option>
		                        <option value = "AS">자동 복구 성공</option>
		                        <option value = "AF">자동 복구 실패</option>
		                        <option value = "AW">자동 복구 대기</option>
		                        <option value = "MS">수동 복구 성공</option>
		                        <option value = "MF">수동 복구 실패</option>
		                        <option value = "MW">수동 복구 대기</option>
	                        </select>
                            <select id="searchRecfileExists" >
		                        <option value = "">전체</option>
		                        <option value="N" selected="selected">복구 필요</option>
	                        </select>
                            <select id="searchListType" >
		                        <option value="genesys" selected="selected">Genesys</option>
		                        <option value="rtp">RTP</option>
	                        </select>
							<button id="fileRecovorSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="admin.label.search"/></button>
							<button id="listReloadBtn" style="display:none;" class="ui_main_btn_flat">리스트 갱신</button>
						</div>
					</div>
	                <div class="ui_float_right">
	                	<button id="multiFileRecoverBtn" class="ui_main_btn_flat">선택 파일 복구</button>
	                	<button id="allFileRecoverBtn" class="ui_main_btn_flat">전체 파일 복구</button>
	                </div>
	            </div>
		        <div class="gridWrap">
			        <div id="gridFileRecoverManage"></div>
			        <div id="pagingFileRecoverManage"></div>
		        </div>
	        </div>
        </div>
        <input type="hidden" id="nowListType" value="genesys"/>
        <input type="hidden" id="nowSearchType" value="all"/>
    </div>
    
    
    <div id="fileRecoverInfo" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">3차 백업 정보</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.system"/></label>
	                        <select id="sys_code"></select>
                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.ext"/></label>
                        	<input class="inputFilter numberFilter" id="ext_num" value="" type="text" maxlength=6/>
                    	<label class="ui_label_essential">콜 아이디</label>
                        	<input class="inputFilter " id="call_id" value="" type="text"/>
                    	<label class="ui_label_essential">고객 전화번호</label>
                        	<input class="inputFilter numberFilter" id="cust_phone" value="" type="text"/>
                    	<div class="hideInfo">
	                    	<label class="ui_label_essential"><spring:message code="admin.channel.label.ip"/></label>
	                        	<input class="inputFilter ipFilter " id="ext_ip" value="" type="text"/>
	                    	<label class="ui_label_essential">통화 시간</label>
	                        	<input class="inputFilter numberFilter" id="call_ttime" value="" type="text"/>
	                    	<label class="ui_label_essential">콜 타입</label>
		                        <select id="call_kind"></select>
                       	</div>
                        <input id="file_date" value="" type="hidden"/>
                        <input id="file_time" value="" type="hidden"/>
                        <input id="file_name" value="" type="hidden"/>
                        <input id="cust_phone_hidden" value="" type="hidden"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="modifyBtn" class="ui_main_btn_flat icon_btn_cube_white">수정</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>