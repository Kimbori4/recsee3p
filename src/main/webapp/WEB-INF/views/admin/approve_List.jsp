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

	<script>
		$(function() {
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();

				// 그리드 위의 높이 값
				var gridCalcHeight = $("#gridApproveManage").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
				var gridResultHeight = (documentHeight - gridCalcHeight - pagingHeight);

				$("#gridApproveManage").css({"height": + (gridResultHeight - 4) + "px"})
		    }).resize();
		})
	</script>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/approve_Manage.js"></script>

	<style>
		.main_contents {
			width:100%;
		}
		
		#top_box {
			width:100%;
			height:40%;
			background-color:#f7f7f7;
			padding:auto;
		}
		
		.top_box_contents {
			width:33.3%;
			height:100%;
			float:left;
		}
		
		#bottom_box {
			width:100%;
			height:60%;
		}
		
		#faceRecMenu {
			width:calc(20% - 0px);
			height:calc(100% - 2px);
			background-color:white;
			border-right:1px solid #efefef;
			border-collapse:collapse;
			position:relative;
			overflow:auto;
		}
		
		#faceRecMenu ul {
			width:100%;
			height:100%;
		}
		
		#faceRecMenu ul li{
			width: calc(100% - 81px);
		    height: 40px;
		    font-family: NotoSansKR;
			font-size: 15px;
			font-weight: normal;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			text-align: left;
			color: #000000;
		    border: 1px solid #efefef;
		    border-collapse:collapse;
		    padding: 20px 40px 0px;
		    vertical-align: middle;
		    cursor:pointer;
		}
		
		#faceRecMenu ul li:hover {
			background-color:#deedff;
		}
		
		#rec_script_box {
			width:calc(80% - 42px);
			height:calc(100% - 40px);
			background-color:white;
			padding:20px;
			font-weight: bold;
			position: relative;
		}
		
		#select_script_box {
			width:calc(30% - 22px);
			height:calc(100% - 20px);
			background-color:white;
			padding:10px;
			font-weight: bold;
		    border-right: 1px solid #efefef;
		    border-collapse:collapse;
		}
		
		#script_info_box {
			width:calc(50% - 22px);
			height:calc(100% - 20px);
			background-color:white;
			padding:10px;
			font-weight: bold;
		}
		
		.float_left {
			float:left;
		}
		
		.display_none {
			display:none;
		}
		
		.rec_script {
			width: calc(100% - 30px);
		    max-height: calc(100% - 65px);
		    background-color: #f7f7f7;
		    margin-top: 15px;
		    padding: 15px;
		    font-weight: normal;
		    font-size: 15px;
		    line-height: 25px;
			overflow:auto;
		}
		
		.noticeYN {
			width:383px;
			height:37px;
			border-radius:20px;
			border:1px solid #efefef;
			background-color:#0067ac;
			position: absolute;
/* 		    bottom: 20px; */
		    left: 20px;
		    z-index:10;
		    font-family: NotoSansKR;
			font-size: 16px;
			font-weight: 500;
			font-stretch: normal;
			font-style: normal;
			line-height: 1.78;
			letter-spacing: normal;
			text-align: left;
			padding-top:10px;
			color: #ffffff;
		}
		
		.noticeYN input {
			width:20px;
			height:20px;
			margin:3px 10px 0 17px;
			float:left;
		}
		
		#recording_player {
			width: calc(100% - 20px);
			height: calc(100% - 20px);
			border-radius: 40px;
			border: 1px solid #efefef;
			background-color: white;
			float: left;
			margin: 10px;
			box-shadow: 1px 2px 6px #e8e8e8;
		}
		
		#cust_info_contents, #user_info_contents {
			width: calc(100% - 40px);
    		height: calc(100% - 70px);
			border: 2px solid #0067ac;
			margin: 30px auto;
			padding: 10px;
		}
	
		button {
			width:95px;
			height:42px;
			background-color:#449ed7;
			color:white;
			float:right;
		    border:0;
		}
		
		#script_menu {
			
		}
		
		#select_script {
		    width: 430px;
			height: 40px;
			margin-bottom:10px;
			padding: 0 0 0 19px;
			border: solid 1px #e1e1e1;
			background-color: #ffffff;
			float: left;
			font-family: NotoSansKR;
			font-size: 14px;
			font-weight: 500;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
		}
		
		#select_script_btn {
			width: 95px;
			height: 42px;
			background-color: #449ed7;
			font-family: NotoSansKR;
			font-size: 14px;
			font-weight: bold;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			text-align: center;
			color: #ffffff;
			float: right;
			margin-bottom:10px;
  		}
  		
  		#script_list {
  			width: 100%;
    		height: 71%;
			margin: 0 15px 10px 0;
			border: solid 1px #e1e1e1;
			background-color: #ffffff;
  			text-align:center;
  			overflow:auto;
  			background:none;
  			cursor:auto;
  		}
  		
  		#script_list option {
  			width:100%;
  			height:30px;
  			padding-top:10px;
  			border-bottom:1px solid #e1e1e1;
  			font-family: NotoSansKR;
			font-size: 14px;
			font-weight:normal;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			color: #000000;
  		}
  		
  		#script_list option:first-child {
  			width:100%;
  			height:30px;
  			padding-top:10px;
  			border-bottom:1px solid #e1e1e1;
			font-weight: 500;
  		}
  		
  		#script_list option:hover:not(:first-child) {
  			background-color:#fcf4c8;
  		}
  		
		#cust_info_contents ul label, #user_info_contents ul label, #script_info_box ul label{
			width: 130px;
			height: 20px;
			display: block;
			text-align: center;
			background-color: #0067ac;
			font-family: NotoSansKR;
			font-size: 14px;
			font-weight: 500;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			/* text-align: left; */
			color: #ffffff;
			padding: 10px;
			float: left;
			margin-bottom:5px;
			clear:both;
		}  		
		#cust_info_contents ul input, #user_info_contents ul input {
			width: calc(100% - 153px);
		    height: 30px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		}
		
		#cust_info_contents ul select{
			width: calc(100% - 150px);
		    height: 40px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		}
		
		#script_info_box ul {
			font-family: NotoSansKR;
			font-size: 15px;
			font-weight: normal;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			text-align: left;
			color: #000000;
		}  
		#script_info_box ul input {
			width: calc(100% - 162px);
		    height: 30px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		    background-color:#f7f7f7;
		    border:none;
		    padding-left:10px;
		}
		#script_info_box ul select{
			width: calc(100% - 152px);
		    height: 40px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		    background-color:#f7f7f7;
		    border:none;
		    padding-left:10px;
		}
		#script_info_box ul textarea{
			width: calc(100% - 168px);
		    height: calc(107% - 5px);
		    float: left;
		    display: block;
		    margin-bottom:5px;
		    background-color:#f7f7f7;
		    border:none;
		    padding-left:15px;
		    resize: none;
		}
		#script_info_box ul li .textWrap {
			width: calc(100% - 152px);
	        height: 107%;
  			float: left;
			overflow: hidden;
		}
		#cust_info_save_btn, #user_info_save_btn {
			width:70px;
			height:27px;
			border-radius:5px;
			border:1px solid #0067ac;
			background-color:#ffffff;
			float:right;
			font-family: NotoSansKR;
			font-size: 13px;
			font-weight: bold;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			text-align: center;
			color: #0067ac;
			padding-top: 8px;
			cursor:pointer;
		}
		
		.chkImg {
			width: 20px;
		    height: 20px;
		    margin-top: 3px;
		    margin-right: 15px;
		}
		
		#recording_player .rec_script_title {
			width:100%;
			height:35px;
			margin-top:25px;
			text-align:center;
			font-family: NotoSansKR;
			font-size: 14px;
			font-weight: normal;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			color: #000000;
		}
		
		#rec_wave {
			width:100%;
			height:100px;
			background-color:#f2f2f2;
			position: relative;
		}
		
		.rec_player_controller {
			width:100%;
			height: 135px;
		}
		
		.rec_player_controller .info_controller{
			width:100%;
			height: 42px;
		}
		
		.rec_player_controller .play_controller{
			width:36%;
			height: 70px;
			margin:0 auto;
		}
		
		.play_time {
			margin:20px 0 0 10px;
			float:left;
			font-family: NotoSansKR;
			font-size: 16px;
			font-weight: 300;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			color: #000000;
		}
		
		#volume_btn {
			width:24px;
			height:24px;
			margin:20px 10px 0 0;
			float:right;
			background-image: url(${siteResourcePath}/images/project/main/icon/icon_volume.png);
		    background-position: center;
		    background-repeat: no-repeat;
		}
		
		#play_btn {
			width: 30px;
			height: 30px;
			padding: 12px 12px 12px 12px;
			border: solid 1px #e5e5e5;
			background-color: #ffffff;
			border-radius:50%;
			float:left;
			cursor:pointer;
		}
		
		#pre_btn, #next_btn {
			width: 25px;
			height: 25px;
			padding: 7px 7px 7px 7px;
			border: solid 1px #e5e5e5;
			background-color: #ffffff;
			border-radius: 50%;
			float: left;
			margin: 10px 20px 0 20px;
			cursor:pointer;
		}
		#search_script_btn{
			float:left !important;
		}
		
		#addProduct{
            width: 400px;
        }
        
        #productAddBtn {
        	width:70p;
        	height:40px;
        	padding:0;
        	margin:0;
        }
        .play {
        	background-image: url(${siteResourcePath}/images/project/icon/icon_play.png);
		    background-position: center;
		    background-repeat: no-repeat;
        	background-size: 55px;
        }
        
        .pause {
        	background-image: url(${siteResourcePath}/images/project/icon/icon_pause.png);
		    background-position: center;
		    background-repeat: no-repeat;
	        background-size: 55px;
        }
        
        .pre_btn_unload {
        	background-image: url(${siteResourcePath}/images/project/main/icon/before_10_sec_gray.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .next_btn_unload {
        	background-image: url(${siteResourcePath}/images/project/main/icon/after_10_sec_gray.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .pre_btn_load {
        	background-image: url(${siteResourcePath}/images/project/main/icon/before_10_sec_blue.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .next_btn_load {
        	background-image: url(${siteResourcePath}/images/project/main/icon/after_10_sec_blue.png);
		    background-position: center;
		    background-repeat: no-repeat;
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

 <div class="main_contents">

		<div id="bottom_box">
			<div id="faceRecMenu" class="float_left">
				<ul>
					<li id="script_menu" onclick="showScript('script_menu')">스크립트 추가</li>
				</ul>
			</div>
			<div id="rec_script_box" class="rec_script_menu_class float_left display_none">
				<div style="width:5px; height:25px; background-color:#449ed7; margin-right:5px;" class="float_left"></div>
				<div class="rec_script_title" style="height:25px; font-weight: bold; margin-top:2px;"></div>
				<div class="rec_script" style="min-height: 88%;">
				</div>
				<div class="noticeYN">
					<input class="chkIcon" type="checkbox"/>고객님께 설명해 드리고 이해 여부를 확인함.</div>
			</div>

			
			<div id="script_info_box" class="script_menu_class float_left">
				<div style="width:5px; height:25px; background-color:#449ed7; margin:6px 5px 0 0;" class="float_left"></div>
				<div style="height:25px; font-weight: bold; margin:10px 0 10px 0;">스크립트 추가 및 수정</div>
				<ul style="height:52%">
					<li><label>상품명</label><input type="text" id="scriptName" class="add_script_contents"/></li>
						<select id="scriptType" style="position:relative" class="add_script_contents">
							<option value="">상품 분류</option>
							<option value="common">공통</option>
							<option value="invest">투자</option>
						</select>
						<input type="text" id="inputDirect" style="width:90%; display:none; position:absolute"/>
					</li>

					<li style="height:100%"><label style="height:100%">내용</label><textarea id="script" class="add_script_contents"></textarea></li>
				</ul>
				<input type="hidden" id="scriptCode"/>
				<button id="add_script_btn">추가</button>
			</div>
		</div>		
	</div>
	
	<div id="addProduct" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">상품 추가</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" style="height: 24px;"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential">상품명</label>
                        	<input id="productName" type="text"/>
           	            <label class="ui_label_essential">상품타입</label>
                        	<input id="productType" type="text"/>
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="productAddBtn" class="ui_main_btn_flat">추가</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>