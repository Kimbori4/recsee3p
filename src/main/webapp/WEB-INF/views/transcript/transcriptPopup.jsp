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
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/transcript/transcriptPopup.js"></script>
	<%-- <script type="text/javascript" src="${compoResourcePath }/recsee_player/recsee_transcript_player.js"></script> --%>
	
	<%-- css page --%>
 	<link rel="stylesheet" type="text/css"	href="<c:out value="${recseeResourcePath}"/>/css/page/transcript/transcript.css" />

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
        #transcriptPopup{
            width: 100%;
            position: absolute;
        }
        #transcriptPopup .comment_row{
			height: 300px !important;
			overflow: auto;
		}
        #playerFrame{
            display: block;
		    position: relative;
		    width: 100%;
		    height: 131px;
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
			/* height: 20px; */
			line-height: 20px;
			border-top: 2px solid #BDBDBD;
			border-bottom: 2px solid #BDBDBD;			
			background-color: #F9F9F9;
			padding-left: 5px;
		}
		.subItemTitle{
			width: 100%;
			min-height: 20px;
			/* height: 20px; */
			border-bottom: 2px solid #BDBDBD;
			background-color: #F9F9F9;

		}
		.itemWrapper{
			display: flex;
		}
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
			border-bottom: 1px solid black;
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
		#gridEvaluation.gridbox.gridbox_dhx_web table.obj tr td.trcellselected{
			background-color: #fff !important;
			border-right: 1px solid #ededed !important;
		}
		#gridEvaluation.gridbox.gridbox_dhx_web table.obj tr.rowselected td{
			background-color: #fff !important;
			border-right: 1px solid #ededed !important;
		}
		.disableButton{
			background-color: gray !important;
			border-color: gray !important;
			cursor: default !important;
		}
		.notAllowedButton{
			cursor: not-allowed !important;
		}
		.transcriptPopup .transcript_popup_body{
        	position: relative;
        }
	</style>
</head>
<body>

	<input type="hidden" id="listenUrl" value="<c:out value="${listenUrl}"/>">
	<input type="hidden" id="userId" value="<c:out value="${userId}"/>">
	<input type="hidden" id="rRecId" value="<c:out value="${rRecId}"/>">
	<input type="hidden" id="rRecDate" value="<c:out value="${rRecDate}"/>">
	<input type="hidden" id="rRecTime" value="<c:out value="${rRecTime}"/>">
	<input type="hidden" id="rExtNum" value="<c:out value="${rExtNum}"/>">
	<input type="hidden" id="transcriptYn" value="<c:out value="${transcriptYn}"/>">
	<input type="hidden" id="rTranscriptSerial" value="<c:out value="${rTranscriptSerial}"/>">
	
	<input type="hidden" id="ip" value="<c:out value="${ip}"/>">
	<input type="hidden" id="port" value="<c:out value="${port}"/>">
	<input type="hidden" id="http" value="<c:out value="${http}"/>">
	<input type="hidden" id="fileAudioType" value="<c:out value="${fileAudioType}"/>">

	<script>
		transcriptYn = $("#transcriptYn").val();
		fileAudioType = $("#fileAudioType").val();
		http = $("#http").val();
	</script>
	
	
    <div id="transcriptPopup">
        <div class="ui_popup_padding">
        	<div class="ui_article transcript_popup_body">
                <div class="ui_pannel_row" style="border-top: 1px solid #bbbbbb;">
                    <div class="ui_float_left">
	                    	<label id="recIdInfoLabel"><spring:message code="views.search.grid.user.title.id"/></label>
			        			<input type="text" value="<c:out value="${rRecId}"/>" id="recIdInfo" class="clear_target ui_input_hasinfo"/>
			        		<label id="recDateInfoLabel"><spring:message code="views.search.grid.call.title.date"/></label>
			        			<input type="text" value="<c:out value="${rRecDate}"/>" id="recDateInfo" class="clear_target ui_input_hasinfo"/>
							<label id="recTimeInfoLabel"><spring:message code="views.search.grid.call.title.time"/></label>
			        			<input type="text" value="<c:out value="${rRecTime}"/>" id="recTimeInfo" class="clear_target ui_input_hasinfo"/>
							<label id="recExtNumInfoLabel"><spring:message code="views.search.grid.user.title.ext"/></label>
			        			<input type="text" value="<c:out value="${rExtNum}"/>" id="recExtNumInfo" class="clear_target ui_input_hasinfo"/>
	        			</form>
        			</div>
        		</div>
        	</div>
        	
           	<%@ include file="../recseePlayer/recseeTranscriptPlayer.jsp" %>
			
            <div class="ui_article transcript_popup_body" id="transcriptPopupMix">
                <div class="ui_pannel_row comment_row">
                    <div class="ui_float_left float_left_full_size">
	                    <form class="comment_form transcriptTextForm">
	                        <br/>
	                        <textarea id="transcriptText" class="clear_target" rows="15"></textarea>
	                    </form>
                   	</div>
                </div>
            </div>
            
            
           <div class="ui_article transcript_popup_body" id="transcriptPopupRxTx" style="display:none;">
               <div class="ui_pannel_row comment_row">
                   <div class="ui_float_left float_left_full_size">
                    <form class="comment_form transcriptTextForm">
                        <br/>
                        <textarea id="transcriptTextRx" class="clear_target" rows="7"></textarea>
                           <textarea id="transcriptTextTx" class="clear_target" rows="7"></textarea>
                    </form>
                  	</div>
               </div>
           </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="transcript_complete_btn" class="ui_main_btn_flat"><spring:message code="views.transcript.button.transcript.save"/></button>
                        <button class="ui_btn_white" id="transcript_update_btn" style="display : none;"><spring:message code="views.transcript.button.transcript.modify"/></button>
                        <button class="ui_btn_white" id="transcript_delete_btn" style="display : none;"><spring:message code="views.transcript.button.transcript.del"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
     <div class="transcriptPopup">
        <div class="ui_popup_padding">

        </div>
    </div>
</body>
</html>