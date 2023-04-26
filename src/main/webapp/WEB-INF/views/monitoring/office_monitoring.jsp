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

	<%-- body zoom lib --%>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/zoomooz/jquery.zoomooz.min.js"></script>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/monitoring/realtime.css" />
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/monitoring/realtime_office.css" />

	<%-- js page --%>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/websocket.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/monitoring/realtime_common.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/monitoring/realtime_officemonitoring.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/monitoring/drag_drop.js"></script>
	<%-- <script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/realtime_office.js"></script> --%>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/monitoring/audio_api.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/monitoring/Queue.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/recsee_player_monitoring/recsee_player.js"></script>

	<%-- real time listening --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/playerctrl.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/log_window.css"/>
	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/streaming/css/main.css" />
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/player_controls.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/log_window.js"></script> --%>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/streaming/js/websocket.js"></script>
<%-- 	<script type="text/javascript" src="${compoResourcePath}/streaming/js/audio_player.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/waveform.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/monitoring/Queue.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/format_reader.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/mpeg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/ogg.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/formats/wav.js"></script> --%>

	<script>
		var listenIp = '<c:out value="${listenIp}"/>';
		var ctiUse = '<c:out value="${ctiUse}"/>';
		var channelInfo = JSON.parse('<c:out value="${channelInfo}"/>');

		$(function() {
			$(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();
		    	var bodyHeight = $('body').prop("scrollHeight");
		    	var calcHeight = $('.option_pannel').height();
				var gridResultHeight = (bodyHeight - calcHeight);
				$(".office_view_pannel_bg").css({"height": $(".office_view_pannel").height()+"px"})
				//$(".office_view_pannel_bg").css({"width":$(".office_obj_wrap").width()+"px","height": bodyHeight+"px"})
				console.log("documentHeight::"+documentHeight);
				console.log("bodyHeight::"+bodyHeight);
				console.log("calcHeight::"+calcHeight);
				console.log("gridResultHeight::"+gridResultHeight);
				$(".office_view_pannel").css({"min-height": + (documentHeight - 50) + "px"});
			}).resize();
		});


		/**대중소 셀렉트 옵션 불러오기
		 * @pram : objSelect => select 객체(html) jquery select
		 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
		 * @pram : selectedIdx => index로 default 선택값 지정
		 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
		 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
		 * @pram : defaultSelect => 기본값 선택 true/false
		 * */
		function selectOrganizationLoad(objSelect, comboType, bgCode, mgCode, selectedValue, defaultSelect, subOpt, empty){

			// 옵션 붙여 넣기 전에 삭제
			$(objSelect).children().remove()

			var dataStr = {
					"comboType" : comboType
				,	"bgCode" : bgCode
				,	"mgCode" : mgCode
				,	"selectedValue" : selectedValue
				,	"accessLevel" : accessLevel
				,	"subOpt" : subOpt
			}

			$.ajax({
				url:contextPath+"/organizationSelect.do",
				data:dataStr,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB에 조회한 계정이 있으면
					if(jRes.success == "Y") {
						if(empty){
							if(empty=="Y"){
								$(objSelect).append("<option>"+lang.common.label.Noclassification/* 해당분류없음 */+"</option>")
							}else if(empty=="empty"){
								//empty
							}else
								$(objSelect).append("<option></option>")
						}

						// 불러온 옵션 추가
						$(objSelect).append(jRes.resData.optionResult)
						if(defaultSelect)
							$(objSelect).val("")
					}
				}
			});
		}
	</script>

	<style>
		.ui_layout_pannel{
			min-width: 1130px !important;
		}
		body,html{
			min-width: 1130px !important;
		}
		.monitoring_contents{
			/* padding-top: 40px !important; */
		}
		.option_pannel{
			/* position: fixed !important; */
			z-index: 1000016 !important;
			min-width: 1130px;
		}


		#realTimeMemo{
			width : 320px;
			height : 300px;
		}

		#realTimeMemoContents{
			width : 100%;
			height : 186px;
		}


		#bottomFixedPlayer{
            width: 440px !important;
		    height: auto;
		    position: fixed;
		    top: calc(100% - 190px);
		    left: 8px;
		    /* margin-top: -190px; */
		    z-index: 99999;
		    border: 1px solid #dddddd;
		    box-shadow: 0px 0px 15px rgba(0,0,0,0.4);
	        border: 1px solid rgba(0,0,0,0.4);
		}
		#bottomFixedPlayer .rplayer{
            min-width: 466px !important;
		}
		.select2-dropdown,.select2-dropdown--below{
			z-index: 9999999 !important;
		}
		.codeList{
			width:150px !important;
		}
		.main_contents_office{
			width: 100%;
			height: 100%;
			clear : both;
			float : left;
		}
	</style>
</head>
<body>
	<canvas id="canvas" width="1000" height="325" style="display:none;"></canvas>
    <div class="main_contents_office">
        <div class="ui_layout_pannel">
			<div class="ui_article option_pannel">
                <div class="ui_pannel_row">
                    <div class="ui_float_left">
                        <p class="now_system" style="display:none;">
                            <span class="now_system_tit"><spring:message code="monitoring.display.selectedSystem"/></span>
                            <span class="now_system_info">A001</span>
                        </p>
                        <form>
                            <select id="systemCode" required style="display:none;">
                                <option value="" disabled ><spring:message code="monitoring.display.system"/></option>
                            </select>
							<select id="roomPersonnel" required style="display:none;">
								<option value="" disabled ><spring:message code="monitoring.display.setCount"/></option>
								<option value="16" selected><spring:message code="monitoring.display.viewby16"/></option>
								<option value="36"><spring:message code="monitoring.display.viewby32"/></option>
								<option value="100"><spring:message code="monitoring.display.viewby100"/></option>
                       		</select>
                            <select id="codeFilter" class="codefilter_select">
                            	<option value="mBgCode"><spring:message code="statistics.selectbox.bgFilter"/></option>
                            	<option value="mMgCode"><spring:message code="statistics.selectbox.mgFilter"/></option>
                            	<option value="mSgCode"><spring:message code="statistics.selectbox.sgFilter"/></option>
                           	  	<option value="searchAgentName"><spring:message code="monitoring.display.filterByAgent"/></option>
                            	<option value="searchAgentNum"><spring:message code="monitoring.display.filterByNum"/></option>
                            	<option value="searchAgentExt"><spring:message code="monitoring.display.filterByExt"/></option>
                            </select>
                            <select id="codeFilterResize" style="display:none;">
                            	<option id="codeFilterResizeOption"></option>
                            </select>
                            <select id="mBgCode"  class="codeList"></select>
							<select id="mMgCode"  class="codeList" style="display:none;"></select>
							<select id="mSgCode"  class="codeList" style="display:none;"></select>
							<select id="searchAgentName"  class="codeList" style="display:none;"></select>
							<select id="searchAgentNum"  class="codeList" style="display:none;"></select>
							<select id="searchAgentExt"  class="codeList" style="display:none;"></select>
                        </form>

                        <button id="mSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="monitoring.display.search"/></button>
                    </div>
                    <div class="ui_float_right">
                   	 	<button id="btnListenAlway" class="ui_sub_btn_flat icon_btn_headset_white"><spring:message code="monitoring.display.officerealtime"/></button>
					 	<button id="btnMonitSetOpen" class="ui_main_btn_flat icon_btn_gear_white"><spring:message code="monitoring.display.monitoringOption"/></button>
                    </div>
                </div>
            </div>


			<div class="monitoring_contents">
               	<div class="office_view_pannel_bg"></div>
	            <div class="ui_article office_view_pannel">
	                <div class="office_obj_wrap"></div>
				</div>
			</div>
		</div>
		<div class="player_pannel">
			<div class="obj_key">
			<div class="obj_key_left">
				<div class="obj_key_value"></div>
			</div>
			<div class="obj_key_right">
		        <div class="obj_key_value"></div>
		    </div>
		</div>
		<%-- @saint: 플레이어 시작 - html 코드는 /component/recsee_player/recsee_player.ui.html 참조 --%>
		<div id="bottomFixedPlayer" class="recsee_player">
		</div>
		<%-- @saint: 플레이어 끝 --%>
          </div>
	</div>

    	<Table class="monitoring_legend">
    		<tr>
    			<td><spring:message code="monitoring.state.login"/></td><td class="legend_login_status"></td>
    		</tr>
    		<tr>
    			<td><spring:message code="monitoring.state.logout"/></td><td class="legend_logout_status"></td>
    		</tr>
    		<tr>
    			<td><spring:message code="monitoring.state.ready"/></td><td class="legend_ready_status"></td>
    		</tr>
    		<tr>
    			<td><spring:message code="monitoring.state.calling"/></td><td class="legend_calling_status"></td>
    		</tr>
    		<tr>
    			<td><spring:message code="monitoring.state.ACW"/></td><td class="legend_ACW_status"></td>
    		</tr>
    		<tr>
    			<td><spring:message code="monitoring.state.away"/></td><td class="legend_away_status"></td>
    		</tr>
    	</Table>

    <div class="monitoring_option" id="monitoring" style="z-index:2000000;top:135px;">

    	<div class="monitoring_option_header">
    		<p><spring:message code="monitoring.display.monitoringOption"/></p>
               <button id="btnMonitSetExit" class="ui_btn_white icon_btn_exit_gray"></button>
    	</div>

   		<div class="contents_display_sel change_mode">
          </div>

            <div class="display_option">
            	<div class="setting_tit_wrap">
	                <p class="ui_main_txt display_option_tit"><spring:message code="monitoring.display.statusViewOptions"/></p>
                </div>
				<div class="setting_dropdown_cont">
	                <div class="option_check">
	                    <span class="onofftit"><spring:message code="monitoring.state.login"/></span>
	                    <div class='onoffswitch check_status_login'>
	                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusLogin' checked/>
	                        <label class='onoffswitch-label' for='selStatusLogin'></label>
	                    </div>
	                </div>
	                <div class="option_check">
	                    <span class="onofftit"><spring:message code="monitoring.state.ready"/></span>
	                    <div class='onoffswitch check_status_ready'>
	                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusReady' checked/>
	                        <label class='onoffswitch-label' for='selStatusReady'></label>
	                    </div>
	                </div>
	                <div class="option_check">
	                    <span class="onofftit"><spring:message code="monitoring.state.calling"/></span>
	                    <div class='onoffswitch check_status_calling'>
	                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusCalling' checked/>
	                        <label class='onoffswitch-label' for='selStatusCalling'></label>
	                    </div>
	                </div>
	                <div class="option_check">
                      <span class="onofftit"><spring:message code="monitoring.state.ACW"/></span>
	                    <div class='onoffswitch check_status_aftercallwork'>
	                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusaftercallwork' checked/>
	                        <label class='onoffswitch-label' for='selStatusaftercallwork'></label>
	                    </div>
	                </div>
	                <div class="option_check">
	                    <span class="onofftit"><spring:message code="monitoring.state.logout"/></span>
	                    <div class='onoffswitch check_status_logout'>
	                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusLogout' checked/>
	                        <label class='onoffswitch-label' for='selStatusLogout'></label>
	                    </div>
	                </div>
                   <div class="option_check">
	                    <span class="onofftit"><spring:message code="monitoring.state.away"/></span>
	                    <div class='onoffswitch check_status_away'>
	                        <input type='checkbox' name='selStatus' class='onoffswitch-checkbox' id='selStatusNotready' checked/>
	                        <label class='onoffswitch-label' for='selStatusNotready'></label>
	                    </div>
	                </div>
            	</div>
           	</div>
		</div>
      </div>

      <div class="listenAlwaysPannel" id="" style="padding-bottom:25px;top:135px;">

      	<div class="monitoring_option_header">
      		<p><spring:message code="monitoring.display.listenAlways"/></p>
            <button id="btnListenAlwayExit" class="ui_btn_white icon_btn_exit_gray"></button>
      	</div>

		<div class="display_option">
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
			<div class="stay_check_wrap">
				<input class="office_stay_check" type="checkbox"/>
				<label><spring:message code="monitoring.display.empListen"/></label>
			<div style="margin-top:45px;">
				<input class="custPhoneContinueChk" type="checkbox"/>
				<label><spring:message code="monitoring.display.custNumListen"/></label><br><br>
				<input type="text" id="custPhoneContinue" placeholder="<spring:message code="monitoring.display.custNumOrEmp"/>"/>
			</div>
			</div>
		</div>
      </div>

     <div id="realTimeMemo" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="header.menu.label.searchNListenMemo"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<textarea class="textArea" contentEditable="true" id="realTimeMemoContents"></textarea>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
					</div>
					<div class="ui_float_right">
						<input type="hidden" id="proc"  value="">
						<button id="realTimeMemoAdd" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="message.btn.save"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="dialog-confirm" class="alert_title ">
		<p class="alert_contents displaynone"></p>
	</div>
</body>