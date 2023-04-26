<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>	
	
	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/search/search.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/search/search.js?version=20220212"></script>

	<script>
		var excelYn = "${nowAccessInfo.getExcelYn()}";
		var uploadYn = "${nowAccessInfo.getUploadYn()}";
		var bestcallYn = "${nowAccessInfo.getBestcallYn()}";
		
		var transcriptYn = "${transcriptYn}";
		var myFolderYn = "${myFolderYn}"
		
		var memoReadYn =  "${memoAccessInfo.getReadYn()}";
		var memoWriteYn = "${memoAccessInfo.getWriteYn()}";
		var gridCopy = "${gridCopy}";
		var dataCopy = "${dataCopy}";
		var listenColor = "${listenColor}";
		var separation_speaker = '<c:out value="${separation_speaker}"/>';
		var custEncryptDate = '<c:out value="${custEncryptDate}"/>';
		
		var myFolderSize = "${myFolder.size()}";

		var fileNameSettingYN = "${fileNameSettingYN}"
		
		var tempCopyList = "";
		var copyList = new Array();
		
		if ("${customizeCopyList}" != "") { // 권한별 copyList 조회 , 해당 권한에 대한 데이터가 있을경우 
			tempCopyList = eval("(${customizeCopyList})"); // CustomizeCopyList 객체 받아옴
			
			for(key in tempCopyList) {
				copyList.push(tempCopyList[key]); // 우클릭시 컬럼 허용 여부 확인하기 위해 list 에 담아둠 -> copyList.indexOf(우클릭한 컬럼 ID)로 검사
			}
		} 
		
		
		
		//var maskingYn = "${maskingAccessInfo.getReadYn()}";
		$(function() {
			//top.playerVisible(false);
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();
		    	var documentWidth  = $(window).width();

				var playerHeight = $('.player_pannel').height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridSearch").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight - playerHeight);

				// dhtmlx 틀고정 사용 할경우 헤드를 두번 읽어서 표 크기가 커져 사이즈 줄임
				if(parent.contextValue != null){
					 var oneGridWidth = $('#gridSearch').find('.gridbox').width();
					 var resultWidth = documentWidth - oneGridWidth;
					 setTimeout(function(){
						 $("#gridSearch").css({"height": + (gridResultHeight - 2) + "px"});
						 $('#gridSearch').find('.xhdr').eq(1).css({"width":resultWidth+"px"});
						 $('#gridSearch').find('.objbox').eq(0).css({"height":gridResultHeight-40+"px"});
						 $('#gridSearch').find('.objbox').eq(1).css({"width":resultWidth+"px","height":gridResultHeight-40+"px"});
					 },100);
				}else{
					$("#gridSearch").css({"height": + (gridResultHeight - 2) + "px"})
				}

				if($('#gridSearch .gridbox div').css("width") == "1715px"){
					$('#gridSearch>div:eq(1)').css("left","1715px");
				}

				if($('#gridSearch .gridbox div').css("width") == "1115px"){
					$('#gridSearch>div:eq(1)').css("left","1115px");
				}

		    }).resize();
		})
	</script>

	<style>
		#gridSearch{
            position: relative;
            float: left;
            clear: both;
			width: 100% !important;
        }
        #tracePopup{
        	width: 580px;
        }

		#RecSectionPopup{
        	width: 950px;
        }

        #recMemoPopup{
        	width  : 865px;
        }

        #logListenPopup{
        	width  : 400px;
        }

        #approvePopup{
        	width: 380px;
        }
		#setRecVolume{
        	width: 250px;
        }
		#csvPopup{
        	width: 450px;
        	height: 550px;
        }
		#volume{
        	width: 100%;
        }
        #traceList, #recMemoList, #logListenList, #RecSectionList{
        	height: 300px !important;
        }

        .gridWrap{
        	border-bottom: 1px solid #dddddd !important;
        }
        .select2-container {
    		float: left;
		}
		#deletePopup, #rsRecfileUpdatePopup{
			width: 400px;
		}
		
		#approvalNumberValue {
		    width: 40%;
		    height: 13px;
		    margin-top: 4px;
		}
		
		#approvalNumberLabel {
			margin-top : 9px;
		}
		.grid_collapse_icon{
			display:none !important;
		}
		div.gridbox_dhx_web.gridbox table.obj.row20px tr td {
			line-height: 23px !important;
    		height: 23px !important;
		}
		input[id=buffer2]{
			-webkit-ime-mode:active;
			-moz-ime-mode:active;
			-ms-ime-mode:active;
			ime-mode:active;
		}
	</style>
</head>

<body oncontextmenu="return false;">

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
        	<div class="search_main_pannel">
				<div class="ui_article">
					<div class="ui_pannel_row">
						<div class="ui_float_left">
							<div class="main_form">
							</div>
						</div>
					</div>
				</div>
				<div class="gridWrap">
					<div id="gridSearch"></div>
					<div id="pagingSearch"></div>
				</div>
			</div>
		</div>

		<div id="downloadMenu" class="context-menus">
			<ul>
				<li onclick="selectFileDown();"><spring:message code="views.search.alert.msg75"/></li>
				<li onclick="allFileDown();"><spring:message code="views.search.alert.msg82"/></li>
			</ul>
		</div>
		<div id="deleteMenu" class="context-menus">
			<ul>
				<li onclick="selectDelete();"><spring:message code="views.search.alert.msg93" /></li>
				<li onclick="allDelete();"><spring:message code="views.search.alert.msg81" /></li>
			</ul>
		</div>
		<div id="excelMenu" class="context-menus">
			<ul>
				<li onclick="selectExcelDown();"><spring:message code="views.search.form.fieldset.selectFileExcelDown"/></li>
				<li onclick="allExcelDown();"><spring:message code="views.search.form.fieldset.fileExcelDown"/></li>
			</ul>
		</div>
		<div id="context-menus" class="context-menus">
			<ul>
				<li onclick="gridSplitAt(this.id);"><spring:message code="views.search.form.fieldset.fix"/></li>
				<li onclick="gridSplitDisable();"><spring:message code="views.search.form.fieldset.nonFix"/></li>
			</ul>
		</div>
		
		<div id="playerChangeMenu" class="context-menus">
			<ul>
				<li onclick="dualPlayerChange();"><spring:message code="views.search.form.fieldset.defaultPlayerChange"/></li>
				<li onclick="sttPlayerPopup();"><spring:message code="views.search.form.fieldset.STTPlayer"/></li>
			</ul>
		</div>
		
		<form name="popForm">
			<input type="hidden" id ="popRecDate" name="recDate" value="">
		    <input type="hidden" id ="popRecTime" name="recTime" value="">
		    <input type="hidden" id ="popExt" name="ext" value="">
		    <input type="hidden" id ="url" name="url" value="">
   		</form>

	</div>

    <div id="tracePopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.form.fieldset.trace"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="gridWrap">
            	<div id="traceList"></div>
            </div>
        </div>
    </div>
    
    <div id="RecSectionPopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">녹취 팝업</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="gridWrap">
            	<div id="RecSectionList"></div>
            </div>
        </div>
    </div>

    <div id="recMemoPopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.form.fieldset.memoList"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="gridWrap">
            	<div id="recMemoList"></div>
            	<div id="recMemoAddButton"></div>
            </div>
        </div>
    </div>

    <div id="logListenPopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.form.fieldset.listenList"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="gridWrap">
            	<div id="logListenList"></div>
            </div>
        </div>
    </div>

    <div id="approvePopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.button.listenAndDown"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="admin.approve.label.req_name"/></label>
	                        <input readOnly class="ui_input_hasinfo" id="approveUserName" value="<c:out value="${userName}"/>" type="text"/>
						<label class="ui_label_essential"><spring:message code="admin.approve.label.req_emp"/></label>
	                        <input readOnly class="ui_input_hasinfo" id="approveUserId" value="<c:out value="${userId}"/>" type="text"/>
						<label class="ui_label_essential"><spring:message code="admin.approve.label.req_team"/></label>
	                        <select class="ui_input_hasinfo" id="approveUserGroup"></select>
                    	<label class="ui_label_essential"><spring:message code="admin.approve.label.req_type"/></label>
	                        <select id="approveType"></select>
                    	<label class="ui_label_essential"><spring:message code="admin.approve.label.req_reason"/></label>
	                        <select id="approveReason"></select>
						<label class="ui_label_essential"><spring:message code="admin.approve.label.req_day"/></label>
							<input maxLength="2" class="inputFilter numberFilter" id="approveDay" value="7" type="text"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="approveBtn" class="ui_main_btn_flat"><spring:message code="admin.button.approve"/></button></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#approvePopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<%-- myFolderList add Popup : 녹취 파일 마이폴더로 이동 팝업 --%>
	<div id="addMyfolderPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
					<div class="ui_pannel_popup_header">
							<div class="ui_float_left">
									<p class="ui_pannel_tit"><spring:message code="admin.approve.label.addMyFolder"/></p>
							</div>
							<div class="ui_float_right">
									<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
							</div>
					</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="common.label.selectFolder"/></label>
                    	<select id='myfolderSelect'>
                    		<%-- <c:forEach items="${myFolder}" var = "list" >
                    			<option value="${list.getrFolderName()}">${list.getrFolderName()}</option>
                    		</c:forEach> --%>
                   		</select>
                    </div>
                  </div>
			</div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addMyfolderItem" onclick="addMyfolderItem();" class="ui_main_btn_flat"><spring:message code="message.btn.add"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#addMyfolderPopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
		</div>
	</div>
	
	
    <div id="setRecVolume" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.volume.title.volumeSetting"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                       	<select id="volume">
                       		<option value="1" selected="selected"><spring:message code="views.search.column.value.volume"/></option>
                       		<option value="2"><spring:message code="views.search.column.value.volumeTwo"/></option>
                       		<option value="3"><spring:message code="views.search.column.value.volumeThree"/></option>
                       	</select>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="saveRecVol" onclick="saveRecVol()" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="views.search.volume.btn.setup"/></button>
                    </div>
                </div>
            </div>            
        </div>
        <input type="hidden" name="hiddenRowId" id="hiddenRowId" value=""/>
    </div>
    
    <div id="searchListPopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="common.label.manageSearchList"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
            </div>
        </div>
    </div>
    
    <div id="deletePopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.alert.msg94" /></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding">
            			<input type="hidden" id='delVolume'>
            			<label class="ui_label_essential"><spring:message code="views.search.alert.msg95" /></label>
               			<select id='deleteSelect'>
                			<option value='A'><spring:message code="views.search.alert.msg96" /></option>
                			<option value='D'><spring:message code="views.search.alert.msg97" /></option>
               			</select>
            		</div>
            	</div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="deleteBtn" class="ui_main_btn_flat"><spring:message code="views.search.alert.msg98" /></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div id="csvLoad" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.alert.msg106"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <form id="csvFileUpload" method="post" enctype="multipart/form-data">
	            <div class="ui_article ui_row_input_wrap">
	                <div class="ui_pannel_row">
	                    <div class="ui_padding">
	                      	<div class="filebox file_upload_wrap"  id="filebox1">
	                			<input class="upload_name" id="upload_name1"  value="<spring:message code="views.search.alert.msg107"/>" readonly="readonly"/>
								<label class="ButtonLabel" for="upLoadFile_hidden1" style="padding-left:12px !important;padding-right:12px !important;"><spring:message code="views.search.alert.msg108"/></label>
	                          	<input type="file" name="upLoadFile_hidden1" id="upLoadFile_hidden1" class="upload_hidden">
		                    </div>
	                    </div>
	                </div>
	            </div>
	         </form>
            <div class="ui_article">
                <div class="ui_pannel_row">
                	<div class="ui_float_left">
                        <button id="formDownBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="views.search.alert.msg109"/></button>
                	</div>
                    <div class="ui_float_right">
                        <button id="uploadBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="views.search.alert.msg110"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="csvPopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="views.search.alert.msg113"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <button id="csvSearchBtn" class="ui_main_btn_flat icon_btn_search_white"><spring:message code="views.search.alert.msg114"/></button>
                    <div class="ui_float_right">
                    	<table>
                    		<tr>
                    			<td align="right"><spring:message code="views.search.alert.msg115"/><input type="radio" name="uploadResult" value="a">
                    			<spring:message code="views.search.alert.msg116"/><input type="radio" name="uploadResult" value="t">
                    			<spring:message code="views.search.alert.msg117"/><input type="radio" name="uploadResult" value="f"></td>
                    		</tr>
                    		<tr>
                    			<td align="right" id="resultCnt"></td>
                    		</tr>
                    	</table>
                    </div>
                </div>
            </div>
            <div class="gridWrap">
            	<div id="csvGrid"></div>
            	<div id="csvPaging"></div>
            </div>
        </div>
    </div>
    
    
    <div id="rsRecfileUpdatePopup" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">녹취 정보 변경</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding" id="rsRecfileColumn">            			
            		</div>
            	</div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="rsRecfileUpdateBtn" class="ui_main_btn_flat"><spring:message code="message.btn.modify" /></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
	<form id="download" name="download" method="post" action="/audioDownload" target="downloadFrame" style="display:none;">
		<input type="hidden" name="fileName" id="fileName" value=""/>
		<input type="hidden" name="format" id="format" value=""/>
	</form>
	
	<form id="downloadExcel" name="downloadExcel" method="post" action="" target="downloadFrame" style="display:none;">
		<input type="hidden" name="recDateArr" id="recDateArr" value=""/>
		<input type="hidden" name="recTimeArr" id="recTimeArr" value=""/>
		<input type="hidden" name="extArr" id="extArr" value=""/>
	</form>
	
    <iframe name="downloadFrame" id="downloadFrame" src="" style="display:none;"></iframe>
	<input type="hidden" id ="ip" value="${ip}">
    <input type="hidden" id ="port" value="${port}">
    
    <div id="faceRecordingPlayer" class="popup_obj">
    	<div id="listen_popup_header" class="popup_header">
    		<div id="listen_popup_title">청취</div>
    		<div class="ui_float_right">
			    <button class="popup_close ui_btn_white icon_btn_exit_gray" id="listen_popup_close"></button>
			</div>
    	</div>
    	<div id="listen_popup_info">
    		<div class="cust_info_box_title">고객 정보</div>
    		<div id="cust_info_box">
	    		<div class="cust_info_box">
    			<div class="cust_info">고객명</div>
    			<div class="cust_info_value" id="cust_name_value"></div>
	    		</div>
				<div class="cust_info_box">
    			<div class="cust_info">고객성향등급</div>
    			<div class="cust_info_value" id="cust_level_value"></div>
	    		</div>
				<div class="cust_info_box">
    			<div class="cust_info">상품명</div>
    			<div class="cust_info_value" id="product_name_value"></div>
	    		</div>
	    		<div class="cust_info_box">
    			<div class="cust_info">상품위험등급</div>
    			<div class="cust_info_value" id="product_level_value"></div>
	    		</div>
	    		<div class="cust_info_box">
    			<div class="cust_info">녹취일에 사용된 스크립트</div>
    			<button id="script_pdf_down" onclick="scriptPdfDown()">PDF</button>
	    		</div>
			</div>
    	</div>
    	<div class="cust_info_box_title">녹취 목록</div>
    	<div id="listen_popup_grid" class=""></div>
    	<div class="cust_info_box_title">플레이어<span id="now_playing_file_name">현재 재생 중인 파일 : <p></p></span></div>
		<iframe name="playerFrame" id="playerFrame"  class="mainFrame" style="width:98%; height: 181px; border:none;" src="${contextPath}/Player"></iframe>
		<button id="playNext">다음 녹취 듣기</button>
		<button id="retryRec">재녹취</button>
		<input type="hidden" id="rowId" value=""/>
    </div>
    
    <div id="approvalNumberPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">승인번호</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
                       	<label class="ui_label_essential" id="approvalNumberLabel">승인번호</label>
            		        <input type="text" class="inputFilter numberFilter" maxlength ="4" id="approvalNumberValue"/>
            		    <button id="approvalNumberCheck" class="ui_main_btn_flat"><spring:message code="message.btn.ok"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="adwPopup" class="popup_obj ui-draggable"
		style="margin-top: -91.5px; margin-left: -191px; left: 908.5px; top: 460px;">
		<div class="ui_popup_padding">
			<div class="popup_header ui-draggable-handle">
				<div class="ui_pannel_popup_header"
					style="background-color: rgb(43, 148, 200); color: rgb(255, 255, 255);">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">ADW CSV DOWNLOAD</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding" style="width: 100%; height: 100%;">

						<input title="시작 날짜" maxlength="8" type="date" id="sAdwDate"
							class=""
							fieldset="fDate" placeholder="시작 날짜" style="width: 96%;"> 
							<input title="시작 날짜" maxlength="8" type="date" id="eAdwDate"
							class=""
							fieldset="fDate" placeholder="종료날짜" style="width: 96%;">

						<button id="adwDownBtn" class="ui_main_btn_flat"
							style="background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease 0s;">다운로드</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
