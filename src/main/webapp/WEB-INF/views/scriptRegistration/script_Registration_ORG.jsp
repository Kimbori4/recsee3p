<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/include/commonVar.jsp"%>
<%-- css page --%>
<%-- <link rel="stylesheet" type="text/css"	href="${recseeResourcePath}/css/page/faceRecording/face_recording.css" /> --%>

<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/scriptRegistration/script_Registration.css" /> 
<%-- js page --%>
<script type="text/javascript"	src="${recseeResourcePath}/js/page/scriptRegistration/script_Registration.js"></script>
<script type="text/javascript"	src="${compoResourcePath}/streaming/js/websocket.js"></script>
<script type="text/javascript"	src="${compoResourcePath}/streaming/js/jquery-sortable.js"></script>

<script>
	var listenIp = "${listenIp}";
	var scriptId = "";
	//프로그레스 처리
	$(function() {
		var PopupCheck =
<%=request.getAttribute("PopupCheck")%>
	;
		if (PopupCheck == true) {
			$(".main_lnb").hide();
			$(".main_header").hide();
		}

		$(window)
				.resize(
						function() {
							// 현재 document 높이
							var documentHeight = $(window).height();

							// 페이징이 있음 페이징 만큼 뺴주깅
							var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0
									: $(".dhx_toolbar_dhx_web").height());
							var gridResultHeight = (documentHeight - pagingHeight);

							/* $(".main_contents").css({"height": + (gridResultHeight - 92)+ "px"}) */
						}).resize();
	});
</script>
<style>
	html, body{
		width: 100%;
		height : 100%
	}
	.script_direct {
		background-color: #e4eef4 !important;
		border-radius: 5px;
		border: 1px solid #dad7d7 !important;
		margin-bottom: 3px;
		height: auto;
		overflow: auto;
		padding: 3px;
	}
	
	.script_Adviser {
		background-color: #c4dee2 !important;
		border-radius: 5px;
		border: 1px solid #dad7d7 !important;
		margin-bottom: 3px;
		height: auto;
		overflow: auto;
		padding: 3px;
	}

	#scriptGrid>.xhdr {
		height: 0px !important;
	}
	
	#scriptGrid {
		width: 100% !important;
		height: 100%;
	}
	
	.main_contents {
		width: 100%;
		/* height : 100% */
	}
	
	#top_box {
		width: 100%;
		height: 40%;
		background-color: #f7f7f7;
		padding: auto;
	}
	
	.top_box_contents {
		width: 75%;
		height: 15%;
		float: left;
	}
	
	#left_box {
		width: 20%;
		height: 100%;
		/* 			height:60%; */
		display: inline-block;
		float: left;
		padding-left: 10px;
		padding-right: 10px;
	}
 	.LRline{
       border-left: 1px solid #dad7d7;
       height : 100%;
     	float : left;
                }
	#scriptTreeGrid {
		width: 300px;
		/* 			width:calc(20% - 0px); */
		height: calc(100% - 2px);
		background-color: white;
		border-right: 1px solid #efefef;
		border-collapse: collapse;
		position: relative;
		overflow: auto;
	}
	
	#scriptTreeGrid ul {
		width: 100%;
		height: 100%;
	}
	
	#scriptTreeGrid ul li {
		width: calc(100% - 81px);
		height: 40px;	
		font-size: 15px;	
		text-align: left;
		color: #000000;
		border: 1px solid #efefef;
		border-collapse: collapse;
		padding: 20px 40px 0px;
		vertical-align: middle;
		cursor: pointer;
	}
	
	#scriptTreeGrid ul li:hover {
		background-color: #deedff;
	}
	
	#rec_script_box {
		width: calc(80% - 42px);
		height: calc(100% - 40px);
		background-color: white;
		padding: 20px;
		font-weight: bold;
		position: relative;
	}
	
	#select_script_box {
		width: calc(30% - 22px);
		height: calc(100% - 20px);
		background-color: white;
		padding: 10px;
		font-weight: bold;
		border-right: 1px solid #efefef;
		border-collapse: collapse;
	}
	
	#script_info_box {
		width: 975px;
		/* 			width:calc(50% - 22px); */
		height: 770px;
		/* 			height:calc(100% - 20px); */
		background-color: white;
		padding: 10px;
/* 		font-weight: bold; */
		float: left;
		font-size: 14px;
	}
	
	.float_left {
		float: left;
	}
	
	.display_none {
		display: none;
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
	overflow: auto;
}

.noticeYN {
	width: 383px;
	height: 37px;
	border-radius: 20px;
	border: 1px solid #efefef;
	background-color: #0067ac;
	position: absolute;
	/* 		    bottom: 20px; */
	left: 20px;
	z-index: 10;
	font-size: 14px;
	font-weight: 500;
	font-stretch: normal;
	font-style: normal;
	line-height: 1.78;
	letter-spacing: normal;
	text-align: left;
	padding-top: 10px;
	color: #ffffff;
}

	.noticeYN input {
		width: 20px;
		height: 20px;
		margin: 3px 10px 0 17px;
		float: left;
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
		width: 95px;
		height: 42px;
		background-color: #0067ac;
/* 		background-color: #449ed7; */
		color: white;
		float: right;
		border: 0;
		margin-left: 5px;
		margin-top: 10px;
		border-radius: 3px;
	}
	
	#script_menu {
		
	}
	
	#select_script {
		width: 430px;
		height: 40px;
		margin-bottom: 10px;
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
		margin-bottom: 10px;
	}
	
	#script_list {
		width: 100%;
		height: 71%;
		margin: 0 15px 10px 0;
		border: solid 1px #e1e1e1;
		background-color: #ffffff;
		text-align: center;
		overflow: auto;
		background: none;
		cursor: auto;
	}
	
	#script_list option {
		width: 100%;
		height: 30px;
		padding-top: 10px;
		border-bottom: 1px solid #e1e1e1;
		font-family: NotoSansKR;
		font-size: 14px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: normal;
		color: #000000;
	
	}
	
	#script_list option:first-child {
		width: 100%;
		height: 30px;
		padding-top: 10px;
		border-bottom: 1px solid #e1e1e1;
		font-weight: 500;
	}
	
	#script_list option:hover:not (:first-child ) {
		background-color: #fcf4c8;
	}
	
	#cust_info_contents ul label, #user_info_contents ul label,
		#script_info_box ul label {
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
		margin-bottom: 5px;
		clear: both;
	}
	
	#cust_info_contents ul input, #user_info_contents ul input {
		width: calc(100% - 153px);
		height: 30px;
		float: left;
		display: block;
		margin-bottom: 5px;
	}
	
	#cust_info_contents ul select {
		width: calc(100% - 150px);
		height: 40px;
		float: left;
		display: block;
		margin-bottom: 5px;
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
		margin-bottom: 5px;
	/* 	background-color: #f7f7f7; */
		background-color: #0067ac;
/* 		background-color: #449ed7; */
	    color: white;
		border: none;
		padding-left: 10px;
		margin: 3px 3px 3px 3px;
	}
	
	#script_info_box ul select {
		width: 100px;
		height: 38px;
		float: left;
		display: block;
		margin-bottom: 5px;
		margin: 5px 5px 9px 5px ;
		/*    background-color:#f7f7f7; */
		border: 1px solid #dddddd;
		/* 		    border:none; */
		padding-left: 10px;
	}
	
	#script_info_box ul textarea {
		background-color: white;
		border: 2px solid #4ccde2;
		border-radius: 10px;
		width: calc(99% - 155px);
		height: calc(107% - 5px);
		float: left;
		display: block;
		margin-bottom: 5px;
		border: none;
		padding-left: 15px;
		resize: none;
	}
	
	#script_info_box ul li .textWrap {
		width: calc(100% - 152px);
		height: 107%;
		float: left;
		overflow: hidden;
	}
	
	#cust_info_save_btn, #user_info_save_btn {
		width: 70px;
		height: 27px;
		border-radius: 5px;
		border: 1px solid #0067ac;
		background-color: #ffffff;
		float: right;
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
		cursor: pointer;
	}
	
	.chkImg {
		width: 20px;
		height: 20px;
		margin-top: 3px;
		margin-right: 15px;
	}
	
	#recording_player .rec_script_title {
		width: 100%;
		height: 35px;
		margin-top: -20px;
		text-align: center;
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
		width: 86%;
		height: 30px;
		background-color: #f2f2f2;
		position: relative;
		float: right;
	}
	
	.rec_player_controller {
		width: 100%;
		height: 46px;
	}
	
	.rec_player_controller .info_controller {
		width: 15%;
		height: 42px;
	}
	
	.rec_player_controller .play_controller {
		width: 36%;
		height: 32px;
		/* margin: 0 auto; */
	}
	
	.play_time {
		margin: 0px 0 0 10px;
		/* 			margin:20px 0 0 10px; */
		float: left;
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
		width: 24px;
		height: 24px;
		margin: 20px 10px 0 0;
		float: right;
		background-image:
			url(${siteResourcePath}/images/project/main/icon/icon_volume.png);
		background-position: center;
		background-repeat: no-repeat;
	}
	
	#play_btn {
		width: 15px;
		height: 15px;
		padding: 12px 12px 12px 12px;
		border: solid 1px #e5e5e5;
		background-color: #ffffff;
		border-radius: 50%;
		float: left;
		cursor: pointer;
	}
	
	#pre_btn, #next_btn {
		width: 10px;
		height: 10px;
		/* 			width: 25px;
				height: 25px; */
		padding: 7px 7px 7px 7px;
		border: solid 1px #e5e5e5;
		background-color: #ffffff;
		border-radius: 50%;
		float: left;
		margin: 10px 20px 0 20px;
		cursor: pointer;
	}
	
	#search_script_btn {
		float: left !important;
	}
	
	#addProduct {
		width: 730px;
		height: 700px;
	}
	
	#productAddBtn, #productcloseBtn {
		width: 70p;
		height: 40px;
		padding: 0;
		margin-left: 5px;
	}
	
	.play {
		background-image:
			url(${siteResourcePath}/images/project/icon/icon_play.png);
		background-position: center;
		background-repeat: no-repeat;
		background-size: 55px;
	}
	
	.pause {
		background-image:
			url(${siteResourcePath}/images/project/icon/icon_pause.png);
		background-position: center;
		background-repeat: no-repeat;
		background-size: 55px;
	}
	
	.pre_btn_unload {
		background-image:
			url(${siteResourcePath}/images/project/main/icon/before_10_sec_gray.png);
		background-position: center;
		background-repeat: no-repeat;
	}
	
	.next_btn_unload {
		background-image:
			url(${siteResourcePath}/images/project/main/icon/after_10_sec_gray.png);
		background-position: center;
		background-repeat: no-repeat;
	}
	
	.pre_btn_load {
		background-image:
			url(${siteResourcePath}/images/project/main/icon/before_10_sec_blue.png);
		background-position: center;
		background-repeat: no-repeat;
	}
	
	.next_btn_load {
		background-image:
			url(${siteResourcePath}/images/project/main/icon/after_10_sec_blue.png);
		background-position: center;
		background-repeat: no-repeat;
	}
	
	.search_script_contents {
		width: 520px !important;
		/* 	    width: 58%; */
		height: 30px;
		background-color: #f7f7f7 !important;
		    color: black !important;
		display: inline-block;
		margin: 5px 0px 5px 3px;
	}
	
	.search_btn {
		width: 80px !important;
		/* 	    width: 12%; */
		height: 37px !important;
		margin: 3px -1px 3px 3px !important;
		text-align: center;
		float: left !important;
	}
	.direct_Scipt_Insert_Btn{
	width: 80px !important;
		/* 	    width: 12%; */
		height: 37px !important;
		margin: 3px -1px 3px 3px !important;
		text-align: center;
		float: left !important;
	}
	
	.group_script_contents {
		width: 25%;
		height: 40px;
		margin: 5px -3px 5px 5px;
		font-size: 12px;
	}
	
	.script_contents {
		width: 97%;
		height: 30px;
		margin: 5px 5px 5px 5px;
	}
	
	.title {
		width: 300px;
		margin: -15px 0px 5px 5px;
	}
	
	.script_gbn {
		width: 13% !important;
		height: calc(30% - 100px) !important;
	}
	
	/*      .add_script_contents{
	      float: right !important;
	      } */
	.ui_left {
		width: 40%;
		float: left;
	}
	
	.ui_right {
		width: 60%;
		float: right;
	}
	
	.ui_row_input_wrap label {
		width: 95px !important;
	}
	
	#productContent {
		width: 75.5%;
		height: 500px;
	}
	
	.ui_row_input_wrap input {
		width: 100%;
		height: 30px;
		/*     	height: 500px; */
		background-color: white;
	}
	
	.editProduct {
		width: 400px;
		display: block;
		margin-top: -117px;
		margin-left: -212px;
	}
	
	.line1, .line2, .line3 {
		width: 300px;
	    height: 40px;
	    margin-left: 5px;
	    float: left;
	    margin-top: 10px;
	}
	
	#prize_name, #prize_type, #group_code, #prize_grade {
		width: 70px;
		height: 40px;
		line-height: 40px;
		display: inline-block;
		float: left;
		background: #dad7d7;
/* 		background: #efefef; */
		padding-top: 3px;
		margin-top: 3px;
		font-size: 14px;
		font-weight: 500;
		text-align: center;
	}
	
	#group_code2 {
		margin: 4px 0px 4px 5px !important;
		float: left !important;
	}
	
	#prize_name2 {
		margin: 4px 0px 4px 5px !important;
		float: left !important;
	}
	
	#prize_type2 {
		margin: 4px 0px 4px 5px !important;
		float: left !important;
		line-height : 40px;
	}
	
	#prize_name2, #prize_type2, #prize_grade2 {
		width: 200px;
		height: 40px;
		float: right;
		border: 1px solid #efefef;		
		font-size: 14px;
		font-weight: 500;
	}
	
	#group_code_btn{
	    width: 20%;
	    height: 100%;
	    margin-top: 5px !important;
	    float: left;
	    font-size: 10pt;
	}
	#group_code2{
		    width: 135px;
		height: 40px;
		line-height: 40px;
		float: right;
		border: 1px solid #efefef;
		font-size: 14px;
		font-weight: 500;
		
	}
	
	.script_tree {
		height: 564px;
/* 		height: 486px; */
		width: 100%;
	}
	
	.top_area {
		margin-bottom: 19px;
/* 		margin-top: 10px; */
	}
	
	.first_right_box {
		width: 77%;
		height: 80%;
		display: inline-block;
		float: right;
		margin-right: 10px;
		position: absolute;
    margin-left: 330px;
    margin-top: 110px;
	}
	
	.second_right_box {
		width: 77%;
		height: 80%;
		/* display: inline-block; */
		float: right;
		margin-right: 10px;
		position: absolute;
    margin-left: 330px;
    margin-top: 110px;
	}
	
	.right_bottom {
/* 		width: 100%; */
		height: 20%;
		float: left;
		width: 975px;
	}
/* 	.right_bottom_btn{ */
/* 	margin-top: -75px; */
/* 	} */
	
	
	.right_bottom_btn{
	    width: 500px;
   		 float: right;
   		 
	}
	.ui_padding_1 {
		width: 100%;
		height: 100%;
	}
	
	
	.add_script_btn {
		font-size: 14px;
	}
	
	.scriptContext {
		padding: 10px;
		display: inline-block;
		width: 98%;
		resize: vertical;
		overflow: hidden;
	/* 	border: 1px solid grey; */
	}
	
	pre {
		white-space: pre-wrap;
	}
	
	#script {
/* 		height: 180px; */
		padding: 5px;
		background-color: white !important;
		border: 1px solid #dad7d7 !important;
		border-radius: 5px;
		margin-bottom: 5px;
	/* 	overflow:scroll; */
	}
	
	select {
		margin-right: 10px;
	}
	
	.content_btn {
		width: 66px !important;
		height: 30px !important;
		float: right !important;
		text-align: center;
		margin-left: 10px;
	}
	
	.content_value {
		width: 200px;
		height: 300px;
		background-color: red;
	}
	
	.step {
		width: 100%;
		height: 35px;
		border: 1px solid #f7f7f7;
	}
	
	#script_content {
		background-color: white !important;
		border: 2px solid #4ccde2 !important;
	}
	
	#script_direct {
		background-color: #e4eef4 !important;
		border-radius: 5px;
		border: 1px solid #dad7d7 !important;
		margin-bottom: 10px;
	/* 	overflow: scroll; */
	}
	
	
	#script_Adviser {
	background-color: #c4dee2 !important;
		border-radius: 5px;
		border: 1px solid #dad7d7 !important;
		margin-bottom: 10px;
	/* 	overflow: scroll; */
	}
	
	.script_direct {
		background-color: #acd6f1 !important;
		border-radius: 5px;
		margin-bottom: 3px;
	}
	
	.script_Adviser {
		background-color: #c4dee2 !important;
		border-radius: 5px;
		margin-bottom: 3px;
	}
	
	
	.script_edit_line1 {
		height: 50px;
		margin-bottom: 10px;
	}
	
	
	
	#script_edit_title {
		line-height: 50px;
	}
	
	#script_edit_content {
		line-height: 400px;
	}
	#script_edit_content3{
	font-size:	16px ;
	}
	
	#script_edit_title, #script_edit_content {
		width: 12%;
		height: 100%;
		display: inline-block;
		float: left;
		background-color: #0067ac;
		color: white;
		font-size: 14px;
		font-weight: 500;
		text-align: center;
	}
	
	#script_edit_title2 {
		float: right;
		width: 87%;
		height: 100%;
		border: 1px solid #efefef;
		background-color: #efefef;
		font-size: 18px;
		line-height: 50px;
	}
	
	#script_edit_content2 {
		float: right;
		width: 86%;
		height: 98%;
		border: 1px solid #efefef;
		background-color: #efefef;
		overflow: auto;
		padding: 6px;
	}
	
	#script_edit_content_btn {
		height: 30px;
		 margin-bottom: 5px;
	}
	
	#script_line1 {
		width: 100%;
		height: 15%;
		display: block;
		font-size: 14px;
	}
	#script_line{
		width: 100%;
		height: 15%;
		display: block;
		font-size: 14px;
	}
	
	
	.script_line1 {
		width: 100%;
		height: 15%;
		display: block;
		font-size: 14px;
	}
	
	#script_line2 {
		width: 100%;
		height: 83%;
		overflow: auto;
	}
	
	.ch_content_btn_2 {
		float: right;
		width: 66px;
		margin-right: 10px;
	}
	
	.change_content_btn_2 {
		float: right;
		width: 66px;
		margin-left: 10px;
	}
	
	.delete_content_btn_2 {
		float: right;
		width: 66px;
		margin-left: 10px;
	}
	
	#condition_ch_2 {
		width: 70px !important;
		margin-top: 5px !important;
	}
	
	#commomSentence {
/* 		width: 1500px; */
		width: 1000px;
		height: 500px;
	/* 	display: block; */
		margin-top: -367px;
		margin-left: -563px;
	}
	
	#popup_scriptType {
		position: relative;
		width: 200px;
		height: 47px;
		margin-right: 5px;
	}
	
	#popup_searchScript {
		width: 1150px;
		height: 30px;
		margin-right: 8px;
	}
	
	#popup_searchBtn {
		width: 100px;
		height: 48px;
		margin-top: 5px;
	}
	.common_script_searchBtn {
		width: 14% !important;
	    height: 48px !important;
	    top: 1px !important;
	    position: relative;
	}
	
	.disable {
		display: none !important;
	}
	
	.enable {
		display: block !important;
	}
	
	#search_script_edit_title1 {
		width: 975px !important;
	/* 	height: 30%; */
		height: 682px !important;
	}
	
	.script_edit_line1 {
		height: 50px;
		margin-bottom: 10px;
	}
	
	.script_edit_line2 {
		height: 675px
	}
	
	#script_edit_title {
		line-height: 50px;
	}
	
	#script_edit_content {
		line-height: 400px;
	}
	
	#addscriptTitle{
		overflow: scroll;
		height: 592px !important;
	}
	.common_padding{
		padding-top: 8px;
		padding-bottom: 8px;
	}
	
	.commonDetailSize{
		height: 775px !important;
	}
	.readonly{
		color: #666666 !important;
		cursor : not-allowed;
	}
	#common_search_btn{
		width: 116px;
	    height: 52px;
	    margin-top: 3px;
	}
	.hide{
		display : none;
	}
.rplayer .player_wave {
       display: none !important;
    }
      #back_script_btn{
    background-color: #dad7d7 !important;
    color: black;
    margin-right: 10px;
    }
  
  #close_script_btn{
  background-color: #dad7d7 !important;
    color: black;
  }
  #audioPlay{
  	margin-right: 5px;
  	width: 64px !important;
  	border-radius: 15px;
  }
    
    #fixed_div{
    width: 850px;
    }
  
    
</style>
</head>
<body>
	<canvas id="canvas" width="1000" height="325" style="display: none;"></canvas>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp"%>
		</c:otherwise>
	</c:choose>
	<div class="main_Registration_contents" Id="main_script_Registration_contents" style="display: block;">
		<!--죄측 스크립트 목록  -->
		<div id="left_box" style="display: none;">
			<!-- 			<div class="title"> -->
			<!-- 			<div style="width:5px; height:25px; background-color:#449ed7; margin:-3px 5px 0 0;" class="float_left"></div> -->
			<!-- 			<div style="height:25px; font-weight: bold; margin:10px 0 10px 0;">스크립트 검색</div> -->
			<!-- 			</div> -->
			<div class="top_area">



				<div class="script_search_result" style= "height : 186px">
					<div class="line1">
						<div id="group_code">상품코드</div>
						<div id="group_code2" value="Kbstar2"></div>
<!-- 						<button id="group_code_btn" >그룹 <br/>더보기</button> -->
						<!-- 								<select id="group_code2" style="position:relative" class="group_script_contents" value=""> -->

						<!-- 								</select> -->
					</div>
					<div class="line2">
						<div id="prize_name">상품명</div>
						<div id="prize_name2" value="Kbstar2"></div>
						<!-- 								<select id="prize_name2" style="position:relative" class="group_script_contents" value=""> -->

						<!-- 								</select> -->
					</div>
					<div class="line3">
						<div id="prize_type">상품유형</div>
						<div id="prize_type2" value="Kbstar2" ></div>
					</div>

				</div>
			</div>

			<div class="title">
				<div
					style="width: 5px; height: 25px; background-color: #0067ac;; margin: -3px 5px 0 0;" class="float_left"></div>
				<div style="height: 25px; font-weight: bold; margin: 10px 0 10px 0;">스크립트	목록</div>
			</div>
			<div id="scriptTreeGrid" class="float_left">
				<!--▼ 상품 선택에 따른 트리구조로 상품 보여주기▼  -->
				<div class="script_tree" value="KBstar" id="script_name_list">
					<div id="scriptGrid">
						<!-- 						<div class="step" id="script_name1" value="KBstar1">1</div> -->
						<!-- 						<div class="step" id="script_name2" value="KBstar2">2</div> -->
						<!-- 						<div class="step" id="script_name3" value="KBstar3">3</div> -->
						<!-- 						<div class="step" id="script_name4" value="KBstar4">4</div> -->
						<!-- 						<div class="step" id="script_name5" value="KBstar5">5</div> -->
					</div>
				</div>
				<input type="hidden" id="scriptCode" />
<!-- 				<button id="copy_script_btn" style="float : left !important; width : 130px" >스크립트<br/> 복사하기 </button> -->
				<button id="add_script_btn" style="float : left !important; width :270px !important; margin-right: 5px !important;" >목록 수정</button>
			</div>			
		</div>
		
	<!-- 중간 라인 -->	
	<div class="LRLine"></div>
	
		<!--우측 스크립트 수정 -->
		<div id="first_right_box" class="first_right_box"style="display: none;">
			<div id="script_info_box" class="script_menu_class float_left">
				<div style="width: 5px; height: 25px; background-color: #0067ac;; margin: 6px 5px 0 0;"class="float_left"></div>
				<div style="height: 25px; font-weight: bold; margin: 10px 0 10px 0;">스크립트 수정</div>

				<div class="script_edit">
					<div class="script_edit_line1">
						<div id="script_edit_title">제목</div>
						<div id="script_edit_title2"></div>

					</div>
					<div class="script_edit_line2">
						<div id="script_edit_content" class="a" contenteditable="false">내용</div>
						<div id="script_edit_content2" class="script_edit_content2"	value="Kbstar3">
							<div id="script_edit_content_btn">
								 <input type="button" id="add_object_Btn" class="content_btn"style="background-color:white ; cursor: pointer; " value="객관식 "> 
								<input type="button" id="add_direction_Btn" class="content_btn"	style="background-color: white; cursor: pointer; " value="상담 가이드">
								 <input type="button" id="add_adviser_Btn" class="content_btn"style="background-color:white ; cursor: pointer; " value="직원 리딩 "> 
								 <input type="button" id="add_content_Btn" class="content_btn"style="background-color: white; cursor: pointer; " value="TTS 리딩">
								 <input type="button" id="common_sentence_btn" class="content_btn"style="background-color:  ;cursor: pointer; " value="공통문구 ">
							</div>
							<div id="script_edit_content3" class="script_edit_content3">
							
							</div>

						</div>
					</div>
				</div>
				<!--우측하단-->
			</div>
				<div class="right_bottom" >
					<!--수정/삭제 버튼-->
					<div class="right_bottom_btn">
						<input type="hidden" id="scriptCode" />
						<button id="back_script_btn">목록보기</button>
<!-- 						<button id="delete_btn">삭제</button> -->
<!-- 						<button id="add_btn">추가</button> -->
						<button id="change_script_btn">저장</button>
					</div>
					<audio id="audioPlayer" controls src ="" style="width:600px;"></audio>
<!-- 				<iframe style="width:72%; border:none;" name="playerFrame" class="player_pannel mainFrame" id="playerFrame" src="/recsee3p/Player"></iframe> -->
				</div>
		</div>

		<!-- 조회 클릭시에만 보여질 화면 -->
		<div id="second_right_box" class="second_right_box" style="width: 100%">
			<div id="script_info_box" class="script_menu_class float_left">
				<div
					style="width: 5px; height: 25px; background-color: #0067ac;; margin: 6px 5px 0 0;"class="float_left"></div>
				<div style="height: 25px; font-weight: bold; margin: 10px 0 10px 0;">스크립트 검색</div>

				<div class="search_script_edit">
				<div id="fixed_div">
					<ul>
						<li>	
							<select id="scriptType" style="position: relative" class="group_script_contents">
								<option id="script_Type" value="all">전체</option>
								<option id="script_Type" class="script_Type_S" value="1">신탁</option>
								<option id="script_Type" class="script_Type_F" value="2">펀드</option>
							</select>
							<select id="scripSearchtType" style="position: relative" class="group_script_contents">
								<option id="script_Type" class="script_Type" value="1">전체</option>
								<option id="script_Type" class="script_Type" value="2">상품명</option>
								<option id="script_Type" class="script_Type" value="3">상품코드</option>
							</select> 
							<input type="text" id="searchScript" class="search_script_contents" placeholder="상품을 검색하세요." />
							<button id="searchBtn" class="search_btn" >조회</button>
							<button id="directSciptInsertBtn" class="direct_Scipt_Insert_Btn" >시스템반영</button>
<!-- 							<input	type="button" id="searchBtn" class="search_btn" value="조회"> -->
<!-- 							<input	type="button" id="directSciptInsertBtn" class="search_btn" value="즉시반영"> -->
							<input type="text" id="inputDirect"	style="width: 90%; display: none; position: absolute" />
						</li>

					</ul>
				</div>
					<div id="search_script_edit_title1" class="search_script_edit_title1">
						<!-- <div id="aaaaaaaa" class="search_script_edit_title1" style="height:300px;"> -->
					</div>
					<div id="search_script_edit_content" class="search_script_edit_content"></div>
			</div>
				<!--우측하단-->
				<div class="right_bottom">
				<div clss="right_paging" id="right_paging"></div>
<!-- 				<iframe style="width:72%; border:none;" name="playerFrame" class="player_pannel mainFrame" id="playerFrame" src="/recsee3p/Player"></iframe> -->
					<!--수정/삭제 버튼-->
					<div class="right_bottom_btn">
						<input type="hidden" id="scriptCode" />
						<button id="close_script_btn" style="display:none;">닫기</button>
						<button id="add_prize_btn" class="add_prize_btn">신규등록</button>
					</div>
				</div>
			</div>
		</div>


	</div>


	<!-- 스크립트 단계 추가 팝업창 -->
	<div id="addProduct" class="popup_obj">
		<div class="ui_popup_padding">
			<!-- popup header -->
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">스크립트 목록 수정</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"
							style="height: 24px;"></button>
					</div>
				</div>
			</div>
			<!-- popup body -->
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row" id="addscriptTitle">
					<ul class="ui_padding_1 scripSteptTitle" id="scriptTitle"style="background-color: white;">
					

					</ul>
<!-- 					<div class="ui_padding_2"> -->
<!-- 						<button id="popadd_script_btn" class="popadd_script_btn">스크립트 추가</button> -->
<!-- 					</div> -->
				</div>
			</div>
			<!--팝업창 하단 수정 버튼-->
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right " style="width:100%;">
						<button id="productcloseBtn" class="ui_main_btn_flat" style="width:15%;float:right">취소</button>
						<button id="productEditBtn" class="ui_main_btn_flat" style="width:15%;float:right">저장</button>
<!-- 						<button id="productAddBtn" class="ui_main_btn_flat" style="width:15%;float:right">저장</button> -->
					<button id="popFirstDelete_script_btn" class="ui_main_btn_flat" style="width:15%; float:right">삭제</button>
						<button id="lastBtn" class="ui_main_btn_flat" style="width:15%;float:right">하위항목 추가</button>
						<button id="popadd_script_btn" class="ui_main_btn_flat" style="width:15%;float:right">스크립트 추가</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<!--변경이력 확인 팝업창 -->
	<!-- 	<div id="changeCareerPopup" class="popup_obj"> -->
	<!-- 		<div class="ui_popup_padding"> -->
	<!-- 			<!-- popup header -->
	<!-- 			<div class="popup_header"> -->
	<!-- 				<div class="ui_pannel_popup_header"> -->
	<!-- 					<div class="ui_float_left"> -->
	<!-- 						<p class="ui_pannel_tit">변경이력</p> -->
	<!-- 					</div> -->
	<!-- 					<div class="ui_float_right"> -->
	<!-- 						<button class="popup_close ui_btn_white icon_btn_exit_gray" -->
	<!-- 							style="height: 24px;"></button> -->
	<!-- 					</div> -->
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 			<!-- popup body -->
	<!-- 			<div class="ui_article ui_row_input_wrap"> -->
	<!-- 				<div class="PopUPcareerContent"> -->
	<!-- 					<div id="PopUPcareerGrid" class="PopUPcareerContent"></div> -->
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 			<!--팝업창 하단 수정 버튼-->
	<!-- 			<div class="ui_article"> -->
	<!-- 				<div class="ui_pannel_row"> -->
	<!-- 					<div class="ui_float_right"> -->
	<!-- 						<button id="productAddBtn" class="check_btn">확인</button> -->
	<!-- 					</div> -->
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
	<!-- 	</div> -->

<!-- 	공용스크립트 검색 조회팝업 -->
	<div id="commomSentence" class="popup_obj" style="height: 503px;">
		<div class="ui_popup_padding">
			<!-- popup header -->
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">공용 스크립트 조회</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"
							style="height: 24px;"></button>
					</div>
				</div>
			</div>
			<!-- popup body -->
			<div class="ui_article ui_row_input_wrap">
				<div class="PopUPcareerContent">
					<ul class="commomSentence1" id="commonScriptSearchBar"
						style="background-color: white;">
						<li><select id="popup_scriptType" style="position: relative"
							class="group_script_contents">
								<option id="popup_script_Type" value="01">이름</option>
								<option id="popup_script_Type" value="02">설명</option>
								<option id="popup_script_Type" value="03">내용</option>
								<option value="direct">직접 입력</option>
						</select> <input type="text" id="popup_searchScript"
							class="search_script_contents" placeholder="상품을 검색하세요." /> <input
							type="button" id="common_script_searchBt" class="common_script_searchBtn search_btn" value="조회">
							<input type="text" id="popup_inputDirect"
							style="width: 90%; display: none; position: absolute" /></li>
					</ul>

				</div>
			</div>
<!-- 			팝업 소속 > 공용스크립트 상세조회 -->
			<div class="ui_article ui_row_input_wrap" id="toggleCommonScriptDetail" style="display: none;">
				<div class="PopUPcareerContent">
					<ul class="commomSentence1" id="scriptTitle"
						style="background-color: white;">
						<input type="hidden" id="common_pk" value=""/>
						<li>
							<label class=" common_padding">스크립트 명</label>
						</li>
						<li>
							<input type="text" id="common_name" class="inputFilter readonly" readonly="readonly"/>
						</li>
						<li>
							<label class=" common_padding">용도</label>
						</li>
						<li>
							<input type="text" id="common_desc" class="inputFilter readonly" readonly="readonly" />
						</li>
						<li>
							<label class=" common_padding">구분</label>
						</li>
						<li>
							<select id="common_detail_type" style="position: relative"
						class="group_script_contents">
								<option id="common_detail_type" value="T">TTS리딩</option>
								<option id="common_detail_type" value="G">직원리딩</option>
							</select>
						</li>
						<li>
							<label class=" common_padding">실시간</label>
						</li>
						<li>
							<select id="common_detail_realTime" style="position: relative"
						class="group_script_contents">
								<option id="common_detail_realTime_Y" value="Y">실시간</option>
								<option id="common_detail_realTime_N" value="N">비실시간</option>
							</select>
						</li>	
						<li>
							<label class=" common_padding">내용</label>
						</li>
						<li>
							<textarea id="common_text" class="readonly" rows="" cols="" style="width: 100%; height:130px; resize: none;" readonly="readonly"></textarea>
						</li>
						<div id="commonDetailGrid" style="width : 100% ; height: 75px; position: relative; left: 0px; margin-top: 3px;"></div>
					</ul>
				</div>
			</div>
			<div id="search_scriptCommon_edit_title1" class="PopUPcareerContent"
				style="height: 346px;"></div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="commonPopupCloseBtn" class="popup_close ui_main_btn_flat" style="background-color:white !importnat;border-color:white !importnat; color: black  !importnat;" >닫기</button>
						<button id="commonRemoveBtn" class="ui_main_btn_flat" style="background-color:#0067ac !importnat;border-color:#0067ac !importnat ;color: black  !importnat; ">삭제</button>
						<button id="commonDetailBackBtn" class="ui_main_btn_flat" style="display:none; background-color:white;border-color:white !importnat; color: black  !importnat;">뒤로가기</button>
						<button id="commonInsertBtn" class="ui_main_btn_flat" style="background-color:#0067ac !importnat;border-color:#0067ac !importnat; color: black  !importnat; ">추가</button>
						<button id="commonCopytBtn" class="ui_main_btn_flat" style="background-color:#0067ac !importnat;border-color:#0067ac !importnat; color: black  !importnat; ">삽입</button>
						<button id="commonUpdateBtn" class="ui_main_btn_flat disable" style="background-color:#0067ac !importnat;border-color:#0067ac !importnat; color: black  !importnat; ">수정</button>
						<button id="commonScriptUpdateBtn" class="ui_main_btn_flat disable" style="background-color:#0067ac !importnat;border-color:#0067ac !importnat; color: black  !importnat; ">수정하기</button>
					</div>
				</div>
			</div>
		</div>
	</div>


	<!--변경이력 확인 팝업창 -->
	<div id="changeCareerPopup" class="popup_obj" >
		<div class="ui_popup_padding">
			<!-- popup header -->
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">변경이력</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"
							style="height: 24px;"></button>
					</div>
				</div>
			</div>
			<!-- popup body -->
			<div class="ui_article ui_row_input_wrap">
				<div class="PopUPcareerContent">
					<ul class="commomSentence1" id="scriptTitle"
						style="background-color: white;">
						<li><select id="popup_scriptType" style="position: relative"
							class="group_script_contents">
								<option id="popup_script_Type" value=""></option>
								<option id="popup_script_Type" value="common"></option>
								<option id="popup_script_Type" value="invest"></option>
								<option value="direct">직접 입력</option>
						</select> <input type="text" id="popup_searchScript"
							class="search_script_contents" placeholder="상품을 검색하세요." /> <input
							type="button" id="popup_searchBtn" class="search_btn" value="조회">
							<input type="text" id="popup_inputDirect"
							style="width: 90%; display: none; position: absolute" /></li>
					</ul>

				</div>
				<div id="PopUPcareerGrid" class="PopUPcareerContent"></div>
			</div>
			<!--팝업창 하단 수정 버튼-->
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="productAddBtn" class="check_btn">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
<!-- 	공용스크립트 추가 팝업 -->
	<div id="commonScriptDetailPopup" class="popup_obj commonDetailPopup_obj" style="width:550px;">
		<div class="ui_popup_padding">
			<!-- popup header -->
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">공용 스크립트 추가</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"
							style="height: 24px;"></button>
					</div>
				</div>
			</div>
			<!-- popup body -->
			<div class="ui_article ui_row_input_wrap">
				<div class="PopUPcareerContent">
					<ul class="commomSentence1" id="scriptTitle"
						style="background-color: white;">
						<li>
							<label class="common_padding ui_label_essential">이름</label>
						</li>
						<li>
							<input type="text" id="insert_common_name" class="inputFilter "  />
						
						</li>
						<li>
							<label class=" common_padding ui_label_essential">설명</label>
						</li>
						<li>
							<input type="text" id="insert_common_desc" class="inputFilter" />
						</li>
						<li>
							<label class="common_padding ui_label_essential">내용</label>
						</li>
						<li>
							<textarea id="insert_common_text" rows="" cols="" style="width: 100%; height:130px; resize: none;"></textarea>
<!-- 							<input type="text" id="common_text" class="inputFilter ui_input_hasinfo" placeholder="상품을 검색하세요." style="height: 150px;" /> -->
						</li>
						<li>
							<label class="common_padding ui_label_essential">유형</label>
							<select id="common_type" style="position: relative"
						class="group_script_contents">
							<option id="common_type" value="T">Teller</option>
							<option id="common_type" value="G">Guide</option>
					</select>
						</li>
						<li>
							<label class="common_padding ui_label_essential">실시간</label>
						</li>
						<li>
							<select id="common_realTime" style="position: relative"
						class="group_script_contents">
								<option id="common_realTime_Y" value="Y">실시간</option>
								<option id="common_realTime_N" value="N">비실시간</option>
							</select>
						</li>
					</ul>

				</div>
			</div>
			<!--팝업창 하단 수정 버튼-->
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="commonScriptAddBtn" class=" ">추가</button>
						<button id="commonScriptAddCloseBtn" class="popup_close">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
		<!-- 	공용스크립트 추가 팝업 -->
	<div id="AddScriptprizePopup" class="popup_obj AddScriptprizePopup_obj" style="width:550px;">
		<div class="ui_popup_padding">
			<!-- popup header -->
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
			<!-- popup body -->
			<div class="ui_article ui_row_input_wrap">
				<div class="PopUPcareerContent">
					<ul class="commomSentence1" id="scriptTitle"
						style="background-color: white;">
						<li>
							<label class="common_padding ui_label_essential">상품명</label>
						</li>
						<li>
							<input type="text" id="insert_prize_name" class="insert_prize_name " style="width: 74%; height:30px; resize: none;" />
						
						</li>
						<li>
							<label class="common_padding ui_label_essential">유형</label>
							<select id="script_prize_type" style="position: relative" class="script_prize_type_contents">
							<option id="script_prize_type" value="1">신탁</option>
							<option id="script_prize_type" value="2">펀드</option>
							</select>
						</li>
<!-- 						<li> -->
<!-- 							<label class=" common_padding ui_label_essential">상품유형</label> -->
<!-- 						</li> -->
<!-- 						<li> -->
<!-- 							<input type="text" id="insert_prize_desc" class="inputFilter" /> -->
<!-- 						</li> -->
						<li>
							<label class="common_padding ui_label_essential">상품코드</label>
						</li>
						<li>
							<textarea id="insert_script_prize_text" class="insert_script_prize_text" rows="" cols="" style="width: 74%; height:30px; resize: none;"></textarea>
<!-- 						<input type="text" id="common_text" class="inputFilter ui_input_hasinfo" placeholder="상품을 검색하세요." style="height: 150px;" /> -->
						</li>
<!-- 						<li> -->
<!-- 							<label class="common_padding ui_label_essential">그룹유무</label> -->
<!-- 							<select id="script_prize_group_type" style="position: relative" class="group_script_prize"> -->
<!-- 							<option id="script_prize_group_type" value="Y">그룹화</option> -->
<!-- 							<option id="script_prize_group_type" value="N">비그룹화</option> -->
<!-- 							</select> -->
<!-- 						</li> -->
<!-- 						<li> -->
<!-- 							<label class="common_padding ui_label_essential">실시간</label> -->
<!-- 						</li> -->
<!-- 						<li> -->
<!-- 							<select id="script_prize_realTime" class="script_prize_realTime" style="position: relative" class="group_script_contents"> -->
<!-- 								<option id="common_realTime_Y" value="Y">실시간</option> -->
<!-- 								<option id="common_realTime_N" value="N">비실시간</option> -->
<!-- 							</select> -->
<!-- 						</li> -->
					</ul>
							<!--팝업창 하단 수정 버튼-->
					<div class="ui_article">
						<div class="ui_pannel_row">
							<div class="ui_float_right">
								<button id="prizeScriptAddeBtn" class=" ">추가</button>
								<button id="prizeScripCloseBtn" class="popup_close">닫기</button>
							</div>
						</div>
					</div>
				</div>
			</div>
	
	
</body>