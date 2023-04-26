<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="java.net.*"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>
	
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${siteResourcePath }/css/page/monitoring/blueprint.css" />
	
	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/websocket.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/realtime_common.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/realtimeBluePrint.js?1231241232"></script>
 	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/audio_api_websocket.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/Queue.js"></script>
	<script type="text/javascript" src="${compoResourcePath }/recsee_player_monitoring/recsee_player.js"></script>
	
	<%-- real time listening 추후에 chrome 풀어주기 --%>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js"></script>
	<%-- real time listening 추후에 chrome 풀어주기 --%>
	<%--<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/playerctrl.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/log_window.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/main.css" /> --%>
	<%--<script type="text/javascript" src="${compoResourcePath}/streaming/js/player_controls.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/log_window.js"></script> --%>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js"></script>
	<%-- <script type="text/javascript" src="${compoResourcePath}/streaming/js/audio_player.js"></script> --%>
	<%-- <script type="text/javascript" src="${compoResourcePath}/streaming/js/waveform.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/format_reader.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/mpeg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/ogg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/wav.js"></script> --%>
	
	<%
		InetAddress inet = InetAddress.getLocalHost();
	
	%>
	<script>
		var listenIp = "127.0.0.1";
		var websocketIp = "<%= inet.getHostAddress() %>";

		$(function() {
			if(tabMode!='Y')
			top.playerVisible(false);
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - pagingHeight);

				$("#gridMonitoringView").css({"height": + (gridResultHeight - 175) + "px"})
		    }).resize();
		 });
	</script>
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
	<div class="monitoring_header_wrap ui_pannel_row">
          <select id="blueprintSelect" class="">
	          <c:forEach items="${bluePrint}" var = "list" varStatus="status">
					<option value="${list.getrBlueprintSeq()}">${list.getrBlueprintname()}</option>
			  </c:forEach> 
         </select>
         <button id="bluePrintSearch" class="ui_main_btn_flat icon_btn_search_white blueprint_btn_search">조회</button>
         <button id="bluePrintShare" class="ui_main_btn_flat icon_btn_search_white blueprint_btn_search">공유</button>
         
         <div class="total_monitoring_browser">
         	<p>총 : </p>
         	<input type="text" id="browserTotal" disabled/>
       	 	<p>로그인 : </p>
         	<input type="text" id="browserLogin" disabled/>
       	 	<p>대기 : </p>
         	<input type="text" id="browserReady" disabled />
       	 	<p>전화중 : </p>
         	<input type="text" id="browserCalling" disabled />
       	 	<p>후처리 : </p>
         	<input type="text" id="browserAcw" disabled />
         	<p>로그아웃 : </p>
         	<input type="text" id="browserLogout" disabled />
         	<p>이석 : </p>
         	<input type="text" id="browserNotReady" disabled />
         </div>
         
		<button id="" class="button_white icon_btn_gear_gray float_right blueprint_btn" onclick="settingOn();">도면 세팅</button>
		<button id="" class="ui_sub_btn_flat icon_btn_headset_white float_right blueprint_btn" onclick="layer_popup('#addBluePrintPopup');">도면 생성</button>
        <button id="btnListenAlway" class="ui_sub_btn_flat float_right icon_btn_headset_white">지속 감청</button>
        
       <div class="listenAlwaysPannel" id="">

       	<div class="monitoring_option_header">
       		<p><spring:message code="monitoring.display.listenAlways"/></p>
                  <button id="btnListenAlwayExit" class="ui_btn_white icon_btn_exit_gray"></button>
       	</div>

			<div class="display_option">
				<%-- <div class="setting_tit_wrap">
     	          <p class="ui_main_txt display_option_tit"><spring:message code="monitoring.display.cookieSaveOptions"/></p>
               </div> --%>
               <div class="setting_dropdown_cont">
               	<div class="option_check">
                    <span class="onofftit"><spring:message code="monitoring.state.save"/></span>
                    <div class='onoffswitch'>
                        <input type='checkbox' name='statusSave' class='onoffswitch-checkbox' id='statusSave' checked/>
                        <label class='onoffswitch-label' for='statusSave'></label>
                    </div>
                </div>
               </div>
			</div>
       	<div class="">
       		<div class="stay_check_wrap monitoring_check">
       				<input class="office_stay_check" type="checkbox"/>
					<label>내선번호 지속 감청</label>
				<div style="margin-top:45px;">
					<input class="custPhoneContinueChk" type="checkbox"/>
					<label>고객전화번호로 지속 감청</label></br></br>
					<input type="text" class='inputFilter numberFilter' maxlength='11' id="custPhoneContinue" placeholder="고객 번호 또는 내선번호"/>
				</div>
			</div>
              </div>
	</div>
	</div>
	<div class="monitoring_wrap">
		<div class="monitoring_setting">
			<div class="monitoring_interior">
				<span>Interior  <div id="closeSettingMenu" onclick="settingOn();"></div></span>
				<ul>
					<li class="active"><i class="item_set active_item"></i><div class="item_text active_text">Set</div></li>
					<li class=""><i class="item_desk"></i><div class="item_text">Desk</div></li>
					<li class=""><i class="item_chair"></i><div class="item_text">Chair</div></li>
					<li class=""><i class="item_etc"></i><div class="item_text">Etc</div></li>
				</ul>
			</div>
			<div class="monitoring_interior_item">
           		<c:forEach var = "item" varStatus="i" begin="1" end="6" step="1">
          			<ul class="monitoring_select_item">
						<li><div class="monitoring_item"></div></li>
						<li><div class="monitoring_item"></div></li>
						<li><div class="monitoring_item"></div></li>
					</ul>
      			</c:forEach> 
			</div>
		</div>
		<div id="blueprintWrap"></div>
	</div>
	
	<%-- 도면  추가  --%>
	<div id="addBluePrintPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
					<div class="ui_pannel_popup_header">
							<div class="ui_float_left">
									<p class="ui_pannel_tit">도면 생성</p>
							</div>
							<div class="ui_float_right">
									<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
							</div>
					</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential">도면 명</label>
                    		<input id="addBluePrintName" class="inputFilter korFilter engFilter numberFilter" value="" type="text" maxlength="15"/>
                    </div>
                  </div>
			</div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addMyfolderItem" onclick="addBluePrint(addBluePrintName.value);" class="ui_main_btn_flat">추가</button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#addBluePrintPopup")'>취소</button>
                    </div>
                </div>
            </div>
		</div>
	</div>
	
	<div id="bottomFixedPlayer" class="recsee_player">
	</div>
	
    <div id="userSelectPopup" class="popup_obj" style="top:63.5px;">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">사원 조회</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="main_form">
             <fieldset class="search_fieldset" id="">
				<legend>사번</legend>
				<input maxlength="8" title="사번" class="inputFilter numberFilter input_userid" id="userId" type="text" style="width:120px;height:12px;margin:5px;"/>
			</fieldset>
	        <fieldset class="search_fieldset" id="">
				<legend>성함</legend>
				 <input title="이름" class="inputFilter korFilter engFilter" id="userName" style="width:120px;height:12px;margin:5px;" type="text"/>
			</fieldset>
			</div>
			<button class="ui_main_btn_flat icon_btn_search_white browser_btn" style="padding-right:10px;" id="searchBtnUser" onclick="">조회</button>
			<div class="tree_view_wrap">
	            <div class="tree_agent_view" id="treeViewAgent"></div>
			</div>
            <div class="gridWrap">
            	<div id="userManageGrid"></div>
            	<div id="userManagePaging"></div>
            </div>
        </div>
    </div>
    
    <div id="realTimeMemo" class="popup_obj" style="top:150px;">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">메모</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<textarea class="textArea" contentEditable="true" id="realTimeMemoContents" style="height:200px;"></textarea>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
					</div>
					<div class="ui_float_right">
						<input type="hidden" id="proc"  value="">
						<button id="realTimeMemoAdd" class="ui_main_btn_flat icon_btn_save_white">저장</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
    <div id="userShareSelectPopup" class="popup_obj" style="top:63.5px;">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">도면 공유</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
			<div class="tree_view_share_wrap">
	            <div class="tree_agent_view" id="treeViewShareAgent"></div>
			</div>
			<div class="main_form">
               <input maxlength="8" title="사번" class="inputFilter numberFilter input_userid" id="userId" type="text" placeHolder="사번"/>
               <input title="이름" class="inputFilter korFilter engFilter" id="userName" type="text" placeHolder="이름"/>
               <button id="shareUserSearchBtn" class="ui_main_btn_flat icon_btn_search_white" style="margin:5px;font-size:13px;padding-right:7px;height:27px;">검색</button>
            </div>
            <div class="gridWrap">
            	<div id="userShareManageGrid"></div>
            	<div id="userShareManagePaging"></div>
            </div>
        </div>
    </div>
	
	
    <div id="realTimeUserDetail" class="popup_obj" style="top:150px;">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">상세 정보</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray" onclick="stopstTimeInterval();"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<label class="">상담사 명</label>
                        <input class="" id="detailUserName" value="" type="text" readonly disabled />
  						<label class="">내선 번호</label>
                        <input class="" id="detailExtNum" value="" type="text" readonly disabled />
  						<label class="">지점</label>
                        <input class="" id="detailMg" value="" type="text" readonly disabled />
                        <label class="">실</label>
                        <input class="" id="detailSg" value="" type="text" readonly disabled />
                         <label class="">고객전화번호</label>
                        <input class="" id="detailCustNum" value="" type="text" readonly disabled />
                         <label class="">상태 지속시간</label>
                        <input class="" id="detailUpdateTime" value="" type="text" readonly disabled />
                         <label class="">오늘 총 콜수</label>
                        <input class="" id="detailTotalCall" value="" type="text" readonly disabled />
                         <label class="">오늘 통화 시간</label>
                        <input class="" id="detailTotalCallTime" value="" type="text" readonly disabled />
                       	<label class="" style="width:180px;">할입</label>
                        <input class="" id="halipeExt" value="" type="text" placeholder="할입에 사용할 내선번호" style="margin-right:5px;"/>
                        <button onclick="suspendCall(this);" class="ui_main_btn_flat icon_btn_save_white" style="height:36px;float:right;">할입</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<div id="dialog-confirm" class="alert_title ">
		<p class="alert_contents displaynone"></p>
	</div>
</body>
</html>
