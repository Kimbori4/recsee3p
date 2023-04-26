<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<%
	String callKey = null;
	try {
		callKey = "" + request.getAttribute("callKey");
	} catch (Exception e) {
	}
%>

	<%-- css page --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecording/face_recording.css" /> --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecorder/faceRecorder.css" /> --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${compoResourcePath}/recsee_stt_player/player_common.css" /> --%>
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/faceRecorder/faceRecorder.css" /> 
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecorder/main2.css" /> 
	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view.js?version=20220507"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view_STT_TA.js?version=20220507"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view_STT_TA_param.js?version=20220507"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnect.js?version=20220507"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnectStt.js?version=20220507"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js?version=20220507"></script>
	<script>
	var listenIp = '<c:out value="${listenIp}"/>';
	var callKey ='<c:out value="${callKey}"/>'
	var recseeResourcePath = '<c:out value="${recseeResourcePath}"/>';
	var scriptId = "";
	var test ="../../../";
	var clientVersion = '<c:out value="${version}"/>'
	//프로그레스 처리
	$(function() {
		var PopupCheck = <%= request.getAttribute("PopupCheck") %>;
		if(PopupCheck == true){
			$(".main_lnb").hide();
			$(".main_header").hide();
		}
		
	    $(window).resize(function() {
	    	// 현재 document 높이
			//var documentHeight = $(window).height();

			// 페이징이 있음 페이징 만큼 뺴주깅
			//var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			//var gridResultHeight = (documentHeight - pagingHeight);

// 			$(".main_contents").css({"height": + (gridResultHeight - 100)+ "px"})

			 var documentWidth = $(window).width();
				document.getElementById('right_box').style.width = (documentWidth-550)+"px";
				document.getElementById('top_menu_contents').style.width = (documentWidth)+"px";
	    }).resize();
	   
		
	 });

	 function doNotReload() {
		if((event.ctrlKey == true && (event.KeyCode == 78 || event.keyCode == 82)) || (event.keyCode == 116)) {
			event.keyCode = 0;
			event.cancelBubble = true;
			event.returnValue = true;
			
			alert("페이지를 새로고침 할 수 없습니다.");
			return false;
		}
	 }

	 document.onkeydown = doNotReload;
	</script>
	<style>
	body{
	height:100% !important;
	}
		html{
			overflow:scroll;
		}
 		.top_menu_contents{ 
 			/* width: 1256px; */
 			height:35px; 
 			background-color:#000000; 
 		} 
  		.sub_Menu{ 
  		width: 200px;
    	height: 100%;
      	background-color: #1ea74a;  
/*       	background-color: #0067ac;   */
/*      	background-color: red;  */
    	text-align: center;
    	/* padding: 0px; */
    	font-size: 20px;
    	    line-height: 154%;
 			font-weight: bold;
  		} 
		.script_btn_click{
			background-color:#0067ac !important;
		}
		.main_contents {
			width: 100%;
  		    height: 96% !important;
		}
		.wave_contents{
			width:55%;
			float: left;
		    margin: 5px 15px 0px 3px;
/* 			border: 1px solid black; */
		    height: 90%;
		   
		}
		.first_wave{
			 background-image: url(/recsee3p/resources/common/recsee/images/project/icon/none.png);   
/* 			 background-position: center; */
		    background-repeat: no-repeat;
        	background-size:100%;
        	width: 134px;
		    position: absolute;
		    height: 68px;
		}
		.start_wave{
			 background-image: url(/recsee3p/resources/common/recsee/images/project/icon/play.gif);   
/* 			background-position: center; */
		    background-repeat: no-repeat;
        	background-size: 100%;
       		 width: 134px;
    		position: absolute;
   			  height: 68px;
		}
		.stop_wave{
			 background-image: url(/recsee3p/resources/common/recsee/images/project/icon/pause.gif);   
			background-position: center;
		    background-repeat: no-repeat;
        	background-size: 100%;
        	width: 134px;
    		position: absolute;
   			 height: 68px;
		}
		
		.skip_btn {
			width:95px;
			height:42px;
/* 			background-color:white; */
			color:black;
/* 			float:left; */
			float:right;
		    margin-top: 5px;
		    border: 1px solid #e8e8e8;
		    background : linear-gradient(to bottom,white,#e8e8e8);
			border-radius : 6px 6px 6px 6px ;
			cursor : pointer;
/* 			position: absolute; */
		}
		.next_btn {
			width:70px;
			height:29px;
			background-color:#0067ac;
			color:white;
			/* float:left; */
			float:right;			
			margin-left: 10px;
			margin-right: 5px;
			margin-top: -16px;
		    border:0;
			border-radius : 6px 6px 6px 6px ;
			cursor : pointer;
			/* position: absolute; */
			font-size: 14px;
			margin-top: 0.5%;
			display : none;
		}
		
		.next_rec[disabled] {
			color: #b5b5b5;
			cursor: default;
		}
		
		.next_rec {
		display : none;
			text-indent: 0px !important;
		}
		.script_box_contents{
			width: 100%;
		    height: 320px;
		    float: left;
		    margin-top: 25px;
		}
		.rec_start, .rec_pause, .rec_end{
			width:100%;
			height:75%;
			text-align: center;
		
		}
		
		.script_Alram_contents{
			width: 96%;
		    height: 25%;
/* 		    height: 53 %; */
		    float: left;
		}
		
	#left_box {
	    width: 445px;
	    height: 90.7%;
	/*     height: 97.7%; */
	    float: left;
/* 	    border-right: 1px solid #b2adad; */
	    padding: 15px 10px 10px 15px;
		}
		
  	.LRline{  
  		 border-left: 1px solid #dad7d7;  
           height : 100%;  
             float : left;  
  	}  
		#right_box {
			width: 750px;
		    height: 800px;
/* 		    height: 92%; */
		    float: left;
		    padding: 15px 10px 10px 15px;
		    position: absolute;
   			margin-left: 485px;
		  

		}
		.left_box_contents{
		    width: calc(100% - 20px);
		    height: 12%;
/* 		    height: calc(21% - 30px); */
		    margin-top: 6px ;
/* 		    margin: 3px auto; */
/* 		    border-bottom: 1px solid grey; */
		}
		.right_box_contents{
		    width: calc(100% - 40px);
		    height: calc(9% - 30px);
/* 		    height: calc(21% - 30px); */
		    margin: 2px auto;
		}
		.rec_btn_contents {
			width: 58%;
    		height: 99%;
    		float:left;
    		
		}
		.script_list{
			width:100%;
			height: 24%;
		}
		.rec_time_contents {
			width: 24%;
    		height: 99%;
    		float:left;
   		    display: flex;
    		align-items: center;
    		margin-top: 21px
		}
		.rec_btn{
	  		width: 25%;
		    /* height: 100%; */
		    height: 82%;
		    background: linear-gradient(to bottom,white,#e8e8e8);
		    border: 1px solid #d0cfcf;
		    border-radius: 5px 5px 5px 5px;
		    float: left;
		    margin: 4px 15px 0px 3px;
		    cursor: pointer;
		}
		.rec_text{
	        float: left;
		    margin: auto;
		    width: 100%;
		    text-align: center;
		    margin-top: 5px;
	        font-size: 13px;
	        font-weight: 700;
	        
		}
		.retry_rec{
			float: left;
		    width: 100%;
		    text-align: center;
		    height: 28px;
		    margin-top: 20px;
		    border: 1px solid black;
		}
		
		#top_box {
			width:100%;
			height:40%;
			background-color:#f7f7f7;
			padding:auto;
		}
		
		.top_box_contents {
			width: calc(100% - 15px);
/* 			width: calc(100% - 9px); */
 			height:8%; 
			float:left;
			font-size: 14px;
 			margin-bottom: -15px; 
 				}
		
		#bottom_box {
			width:100%;
			height:60%;
		}
		

		.script_opt_contents{
			width: 100%;
		    height: 30px;
		    background: #dad7d7;
		    border: 1px solid #dad7d7;
		    border-radius: 6px 6px 0px 0px;
			
		}
		.tellerText{
   	    padding: 10px;
	    background-color: #ffffff;
	    border-bottom: 1px solid #dcdcdc;
	    border-top-left-radius: 10px;
	    border-top-right-radius: 10px;
	    line-height: 1.5em;
	    letter-spacing: normal;
	/*      line-height: 30px; */
	    /* line-height: 38px; */
	/*     font-size: 15px; */
	    margin-bottom: 10px;
		}
		.customerText{
		margin-bottom: 10px;
	    padding: 6px;
	    background-color: #ecececab;
	    border: 1px solid #dcdcdc;
	    border-radius: 10px 10px 10px 10px;
	    border-bottom-right-radius: 10px;
     	line-height: 30px;
	    font-weight: bold;
/*     height: 54px; */
			
		}
		.AdviserText{
			margin-bottom:10px;
			padding : 10px;
/* 		    background-color: #deeffd; */
		    border-bottom: 1px solid #dcdcdc;
 			color: #0067ac; 
		    line-height: 30px;
		    font-weight: bold;
/* 			  color: red; */
/* 			font-size: 15px; */

		}
		
		#rec_script_box {
			width:calc(100% - 22px);
			height:calc(94% - 40px);
			background-color:white;
			padding:10px;
			font-weight: 300;
/* 			font-size: 12pt; */
			position: relative;
			font-size: 14px;
/* 			border-bottom: 1px solid #dcdcdc; */
			overflow:auto;
			
		}
/* 		.script_text_info{ */
/* 			width: 35%; */
/* 		    padding: 4px; */
/* 		    float: left; */
/* 		} */
		.script_rec_btn{
			width:12%;
			height:5%;
			float:left;
		    margin-right: 5px;
		    background : linear-gradient(to bottom,white,#e8e8e8);
			border-radius : 3px 3px 3px 3px ;
			border: 1px solid #e8e8e8;
			cursor : pointer;
		}
		#script_name{
			width: 70%;
	   		float: left;
	   		line-height: 3;
		    font-weight: 900;
		    height: 100%;
		    font-size: 9pt;
		    width: 100%;
		    display: inline-flex;
		    flex-direction: column;
		    justify-content: space-around;
		}
		.float_left {
			float:left;
		}
		.script_text_info{
/* 		    width: 24%; */
/* 		    padding-top: 0px; */
/* 		    height: -webkit-fill-available; */
/* 		    float: right; */
/* 		    text-align: center; */
/* 		    margin-left: 20px; */
/* 	        display: flex; */
/*    			flex-direction: column; */
/*     		justify-content: space-around; */
    		
    		width: 24%;
    		padding-top: 0px;
		    height: -webkit-fill-available;
		    float: right;
		    text-align: center;
		    margin-left: 20px;
		    display: flex;
		    flex-direction: row;
		    flex-wrap: nowrap;
		    align-content: center;
		    align-items: center;
		}
		.display_none {
			display:none;
		}
		.play_contents_info{
		    float: left;
		    width: 17%;
		    font-size: 14px;
		    font-weight: bold;
		    height: 100%;
		    text-align: center;
		    margin-right: -20px;
		    padding-top: 6px;
			
		}
		.play_contents_info2{
			float: left;
			width: 18%;
		 	font-size: 16px;
		 	font-weight: bold;
			height: 100%;
			margin-left: 10px;
		 	margin-top: 6px;
			
		}
		[type="radio"] {
			margin-left:1.5em;
			margin-top: 0.1em;
			cursor: pointer;
			margin-right: 3px;
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
			float:left;
			width:330px;
			height:37px;
			border-radius:20px;
			border:1px solid #efefef;
			background-color:#0067ac;
		    left: 20px;
		    z-index:10;
		    font-family: NotoSansKR;
			font-size: 13px;
			font-weight: 500;
			font-stretch: normal;
			font-style: normal;
			line-height: 1.78;
			letter-spacing: normal;
			text-align: left;
			padding-top:10px;
			color: #ffffff;
			margin-right: 60px;
		}
		
		.noticeYN input {
			width:20px;
			height:20px;
			margin:3px 10px 0 17px;
			float:left;
		}
		
		#recording_player {
			width: calc(100% - 20px);
			height: calc(47% - 20px);
			border-radius: 40px;
			border: 1px solid #efefef;
			background-color: white;
			float: left;
			margin: 10px;
			box-shadow: 1px 2px 6px #e8e8e8;
		}
		
		#cust_info_contents, #user_info_contents {
			width: calc(100% - 40px);
    		height: 50%;
 			border: 1px solid #d0cfcf !important; 
/* 			margin: 3px auto; */
			margin-top: 8px;
/* 			margin-top: 19px; */
/* 		    padding: 7px; */
		    border-radius: 4px 4px 4px 4px;
		    background: linear-gradient(to bottom,white,#eae7e7);
		}
		#cust_info_contents, #user_info_contents , #warning_info_contents {
			width: calc(100% - 20px);
    		height: 44%;
			border: 1px solid grey;
/* 			margin: 3px auto; */
/* 		    padding: 7px; */
		}
		
		
		#productInfoScriptGrid{
			width: 100% !important;
    		height: calc(100% - 65px);
			border: 1px solid gray;
			margin: 0px auto;
/* 			margin: 10px auto; */
		}
		
		.script_list_btn{
		    width: calc(100%);
		    height: 6%;
/* 		    margin-top: 10px; */
		    margin: 30PX 0PX 0PX 0PX;
		    position: absolute;
		}
		.script_play_contents{
		    border-radius: 6px 6px 6px 6px;
		    width: 98.5%;
		    height: 95%;
}
		}
		.script_left_btn{
		    width: 21%;
		    float: left;
		    border-radius: 50%;
		    font-size: 31px;
		    border: 1px solid #9a9797;
		    text-align: center;
		    margin-left: 20px;
		}
		.script_left_text{
			background-image: url(/recsee3p/resources/common/recsee/images/project/icon/T.png);
			    background-position: center;
		    background-repeat: no-repeat;
			width: 24%;
		    font-family: "궁서";
		    font-size: 20px;
		    float: left;
		    text-align: center;
		    margin-right: -8px;
		}
		.script_content_info{
			width: 100%;
		    height: 93%;
/* 		    height: 100%; */
		    border-radius: 0px 0px 6px 6px;
		    border: 1px solid #dad7d7;
		}
		.script_btn {
			border: 1px solid #c3c3c3;
			width: 75px;
			height: 30px;
/* 			background-color: #ffffff; */
			background : linear-gradient(to bottom,white,#e8e8e8);
			border-radius : 6px 6px 6px 6px ;
			cursor : pointer;
			font-family: NotoSansKR;
			font-size: 14px;
			font-weight: bold;
			font-stretch: normal;
			font-style: normal;
			line-height: 30px;
/* 			line-height: normal; */
			letter-spacing: normal;
			text-align: center;
			color: #040404;
			float: left;
			margin: 0px 10px 0px 0px;
  		}
  		
		#cust_info_contents ul label, #user_info_contents ul label, #script_info_box ul label{
			width: 50px;
			height: 10px;
			display: block;
			text-align: center;
			background-color: #0067ac;
			font-family: NotoSansKR;
			font-size: 11px;
			font-weight: 500;
			font-stretch: normal;
			font-style: normal;
			letter-spacing: normal;
			color: #ffffff;
			padding: 10px;
			float: left;
			margin-bottom:5px;
		}  		
		#cust_info_contents ul input, #user_info_contents ul input {
			width: calc(54% - 96px);
    		height: 20px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		}
		
		#cust_info_contents ul select{
			width: calc(54% - 96px);
		    height: 40px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
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
		#rec_wave2 {
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
			margin:0px 0 0 0px;
			float:left;
			font-family: NotoSansKR;
			font-size: 29px;
			font-weight: 300;
			font-stretch: normal;
			font-style: normal;
			line-height: normal;
			letter-spacing: normal;
			color: #000000;
			margin-top: 5px;
			width: 100%;
    		text-align: center;
    		display : contents;
		}
		
		#volume_btn {
			width:24px;
			height:24px;
			margin:20px 10px 0 0;
			float:right;
			background-image: url(/recsee3p/resources/common/recsee/images/project/main/icon/icon_volume.png);
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
		
		#addProduct{
            width: 400px;
        }
        
        .play {
        	height:47px;
           	background-image: url(/recsee3p/resources/common/recsee/images/project/icon/PlayRed.png);   
		    background-position: center;
		    background-repeat: no-repeat;
        	background-size: 21px;
/*         	background-size: 33px; */
        }
        .stop{
        height:47px;
       background-position: center;
		    background-repeat: no-repeat;
        	background-size: 21px;
/*         	background-size: 33px; */
        }
        
        .pre_btn_unload {
        	background-image: url(/recsee3p/resources/common/recsee/images/project/main/icon/before_10_sec_gray.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .next_btn_unload {
        	background-image: url(/recsee3p/resources/common/recsee/images/project/main/icon/after_10_sec_gray.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .pre_btn_load {
        	background-image: url(/recsee3p/resources/common/recsee/images/project/main/icon/before_10_sec_blue.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .next_btn_load {
        	background-image: url(/recsee3p/resources/common/recsee/images/project/main/icon/after_10_sec_blue.png);
		    background-position: center;
		    background-repeat: no-repeat;
        }
        
        .disableddiv{
       	    pointer-events: none;
       	    opacity:0.4;
        }
        .displayHidden{
        	display:none;
        }
        .script_bottom_fld{
        	float:right;
        	margin-right:10px;
        }
        #faceRecPopup{
        	width:800px;
        	heigth:240px;
        }

       .rec_user_info{
        width: 100%;
    	height: 100px;
/*     	border-bottom: 1px solid #d0cfcf; */
       } 
        
        
   .line1, .line2, .line3, .line4 {
	    width: 100%;
	    height: 36%;
	    float: left;
	    overflow: hidden;
}
     #user_name{
     	 height: 30px;
	    line-height: 30px;
	    width: 23.5%;
	    display: inline-block;
	    float: left;
	    border: 1px solid #d0cfcf !important;
	    background-color: #dad7d7;
	    text-align: center;
	    font-size: 14px;
	    margin-left: 4.5px;
       }
       
       #user_name2{
   	    height: 30px;
	    line-height: 35px;
	    width: 73.5%;
/* 	    width: 31%; */
	    float: left;
	    border: 1px solid #d0cfcf !important;
	    font-size: 14px;
	    text-align: center;
       }
       
       #user_step{
	    float: left;
	    height: 30px;
	    line-height: 30px;
	    width: 23.5%;
	    margin-left: 1%;
	    border: 1px solid #d0cfcf !important;
	    background-color: #dad7d7;
	    text-align: center;
   		 font-size: 14px;
       }
       
       #user_step2{
        height: 30px;
	    line-height: 30px;
	    width: 73.5%;
	    float: left;
	    border: 1px solid #d0cfcf !important;
	    text-align: center;
	    font-size: 14px;
	}
       
        #prize_name{
        height: 30px;
	    line-height: 30px;
	    width: 23.5%;
	    display: inline-block;
	    float: left;
	    border: 1px solid #d0cfcf !important;
	    background-color: #dad7d7;
	    font-size: 14px;
	    text-align: center;
	    margin-left: 4.5px;
        }
        
        #prize_name2{
         height: 30px;
	    line-height: 30px;
	    width: 73.5%;
	    float: left;
	    border: 1px solid #d0cfcf !important;
	    font-size: 11px;
	    text-align: center;
        }        

        #prize_step{
          float: left;
	    height: 30px;
	    line-height: 30px;
	    width: 23.5%;
	    margin-left: 1%;
	    border: 1px solid #d0cfcf !important;
	    background-color: #dad7d7;
	    font-size: 14px;
	    text-align: center;
	    margin-top: 1px;
        }
        #prize_step2{
	    height: 30px;
	    line-height: 30px;
	    width: 73.5%;
	    float: left;
	    border: 1px solid #d0cfcf !important;
	    font-size: 14px;
	    text-align: center;
	    margin-top: 1px;
        }
        
		#Alram_wrap{
        border-radius: 10px 10px 10px 10px;
	    border: 1px solid #d0cfcf;
	    height: 135px;
	    width: 99%;
	    /* margin-left: 5px; */
	    padding: 10px;
	    display: flex;
        flex-direction: column;
	    justify-content: center;
		}
        #Alram_line1{
        background-color: #f2f2f2;
        display: inline-block;
        float: left;
        width: 29%;
        height: 30px;
        line-height: 30px;
    	 border-right: 1px solid #d0cfcf;
   	 	border-bottom: 1px solid #d0cfcf;
   	 	text-align: center;
        }
        #Alram_line2{
           background-color: #f2f2f2;
            float: left;
           height: 30px;
           line-height: 30px;
           border-bottom: 1px solid #d0cfcf;
           width : 70.5%;
           text-align: center;
        }
        #Alram_line3{
        
        }
        
        .line{
		margin-top: 48px;
	    border-bottom: 1px solid #d0cfcf;
	    width: 100%;
        }
         
         .hrCss{
        position: relative;
  		 width: 101%;
   		 right: 5px;
   		 border: 1px solid #eaeaea;
        }
		.flex_rec_info {
	    display: flex;
	    flex-direction: row;
	    flex-wrap: wrap;
	    align-content: flex-start;
	    font-size: 9pt;
	    text-align: center;
		}
		.script_text_big{
			background-image: url(/recsee3p/resources/common/recsee/images/project/icon/plus.png);
		    background-position: center;
		    background-repeat: no-repeat;
		    cursor: pointer;
		}
		.script_text_small{
			background-image: url(/recsee3p/resources/common/recsee/images/project/icon/minus.png);
		    background-position: center;
		    background-repeat: no-repeat;
		    cursor: pointer;
		}
		.script_img_size{
			background-size: 20px;
/* 			background-size: 28px; */
		}
		.play_contents_info{
				background-image: url(/recsee3p/resources/common/recsee/images/project/main/icon/PlaySpeed.png);
		background-position: center;
		    background-repeat: no-repeat;
		}
		
		#retryRecReasonPopup {
			width:400px;
		}
		div.gridbox_dhx_web.gridbox table.obj.row20px tr td {

   	 height: 25px !important;
    line-height: 25px !important;

}
div.gridbox_dhx_web.gridbox table.obj.row20px tr.rowselected td {
    background-color: #ffffffbf !important;
   border-right: 0px !important;
}

        } 
		
		.disabled{
			pointer-events:none;
			opacity:0.4;
		}
		
		#fixed_div{
		width: 500px;
		}
		
		#clearAllRec {
			width: 80px;
			height: 30px;
			font-size: 14px;
			border: 1px solid #dedede;
			/* border-radius: 8px; */
			color: white;
			font-weight: bold;
			background-color: #0067ac;
			float: right;
			margin: 0 0px 3px 0;
			display:none;
	    }
	    .readingText{
	    	display: inline;
	    }
	    .readingTextBack{
	    	background: #85d3ff38;
	    	font-weight: bold; 
	    }
	    .btnRedText{
	    	color: #f93131;
	    	-webkit-text-stroke: medium;
	    }
	    .moreProductRow{
	    	background:59bcff;
	    	font-size: 11px;
	    }
	    
	    .guideImg{
			width: 100%;
			height: 100%;
			background-image: url(/recsee3p/resources/common/recsee/images/project/guide/guideImg.png);
	   		background-position: center;
		    background-repeat: no-repeat;
		    background-size: contain;
	}
	#guideImgPopup{
		width: 837px;
	    height: 690px;
	    display: block;
	    margin-top: -397px;
	    margin-left: -512px;
		left: 64%;
  		top: 53%;
	}
	.ui_pannel_popup_header{
		background-color: rgb(43, 148, 200);
   		color: rgb(255, 255, 255);
	}
	.TplayBtn:before{
		content: "테스트";
		color: red;
	}
	.rec_confirm{
		width: 80px;
	    height: 30px;
	    font-size: 14px;
	    border: 1px solid #dedede;
	    color: white;
	    font-weight: bold;
	    background-color: #0067ac;
	    float: right;
	    margin: 0 9px 3px 0;
    }
    .nextPlayEndBtn{
    	cursor: pointer;
    }
    .nextPlayGBtn{
		cursor: pointer !important;
	}
	.inputMedia{
		height: 12px;
	}
	.taResultReason {
	width: inherit;
/*     margin-bottom: 10px; */
	display: flex;
    flex-direction: column;
	font-size : 14px;
	}
	.taErrorResultPopStepNameBody{
		width: 100%;
	    height: 100%;
	    display: flex;
	    flex-direction: column;
	    align-items: center;
	}
	.taResultPopBodyAll{
		display: flex;
	    flex-direction: column;
	    align-items: center;
	}
	.taResultPopBodyReason{
		padding: 8px 0px 9px 1px;
		border: 1px solid;
		font-weight: bold;
		padding: 9px 15px 9px 15px;
	
	}
	.taResultPopUpMentDetailReason{
		color: blue;
	    line-height: 2;
  		letter-spacing: unset;
  		width : 88%;
  		font-size: 14PX;
  		margin-bottom: 5px;
	
	}
	
	</style>
</head>
<body oncontextmenu="return false">
    <div class="main_contents">
    <div class="top_menu_contents" id="top_menu_contents">
    	<div class="sub_Menu org"  style="color: white;">운용상품 안내</div> 
<!--     	<div class="sub_Menu org"  style="color: white;">테스트 서버</div>  -->
    	<div class="sub_Menu cusMenu"  style="color: white;  display: none;">고객 투자성향조사</div> 
<!--     	<div class="sub_Menu freeMenu"  style="color: white ; display: none;">테스트 서버</div>  -->
<!--     	<div class="sub_Menu cusMenu"  style="color: white;  display: none;">테스트 서버</div>  -->
<!--     	<div class="sub_Menu freeMenu"  style="color: white ; display: none;">자유녹취</div>  -->
    </div>
		<div id="left_box"><i class="fas fa-square-full" style="margin: 11px 0px 0px -67px; color: red; font-size: 23px; position: absolute;"></i> 
			<div style="width: 5px; height: 20px; background-color: #82e276; margin:2px 5px 0px 3px;"class="float_left cs"></div>
			<p style="padding:5px; font-weight: 600;     margin-bottom: 3px;" class="float_left_csp">고객 정보</p>
			<div style="width: 5px; height: 20px;  display:none; background-color: #449ed7; margin:2px 5px 0px 3px;"class="float_left free"></div>
			<p style="padding:5px; font-weight: 600; display:none;  margin-bottom: 3px;" class="float_left_free">자유녹취</p>
					<div class="rec_user_info">
					<div class="line1">
						<div id="user_name">고객명</div>
						<div id="user_name2" ></div>
					</div>
					<div class="line2">
						<div id="user_step">고객성향등급</div>
						<div id="user_step2" ></div>
					</div>
					<div class="line3">
						<div id="prize_name">상품명</div>
						<div id="prize_name2"></div>
					</div>
					<div class="line4">
						<div id="prize_step">상품위험등급</div>
						<div id="prize_step2"></div>
					</div>
			</div>

			<div class="line"></div>
			
			
			<div id="fixed_div">
			<div id="rec_btn_box" class="left_box_contents">
				<div class="rec_btn_contents">
					<div class="rec_btn rec_startBtn ">
						<div class="play"></div>
						<div style="text-align:center;font-size: 14px; font-weight: bold; margin-bottom: 4px;">녹취</div>
					</div>
					
					<div class="rec_btn rec_endBtn displayHidden">
						<div class="stop">
						<i class="fas fa-share-square" style="margin: 14px 27px 6px 25px;  background-color: #f65555; width: 20px; height: 20px; position: absolute; "></i> 
						</div>
						<div style="text-align: center; text-align:center;font-size: 11px; font-weight: bold; margin-bottom: 4px;" >구간녹취종료</div>
					</div>
					<div class="wave_contents">		
						<div class="rec_wav first_wave"></div>
						<div class="rec_wav start_wave displayHidden"></div>
						<div class="rec_wav stop_wave displayHidden"></div>			
					</div>
				</div>
				<div class="rec_time_contents">
				<div class="play_time"><img src="/recsee3p/resources/common/recsee/images/project/icon/TimeCircleRed.png;" style="margin-right: 3px;"/><span id="now_time" style="font-size: 25px;">
					<span id="hour">00</span>:<span id="minutes">00</span>:<span id="seconds">00</span>
					</span></div>
				</div>
			</div>
			<div id="cust_info_box" class="top_box_contents">
								<div id="cust_info_contents" style="display: flex; flex-direction: row;  width: 91%; ">
					<div class="mediaFlex1" style="display: flex; width: 100%;  height: 71%;   flex-direction: row;">
					<img src="/recsee3p/resources/common/recsee/images/project/icon/PlaySpeed.png;" style="float: left; margin-left: 10px;	margin-top: 3px; width: 22px; height: 22px; "/>
						<div class="play_contents_info" >재생속도</div>
						<div style="    width: 25%; text-align: center; padding-top: 6px;">
							<input class="inputMedia" type="radio" name="speed" id="nomal" value="1.1" checked = "checked"><span>보통</span>
						</div>
						<div style="width: 25%; text-align: center; padding-top: 6px;">
							<input class="inputMedia" type="radio" name="speed" id="fast_1" value="1.25"><span>1.2배</span>
						</div>
						<div style="width: 25%; text-align: center; padding-top: 6px;">
							<input  class="inputMedia" type="radio" name="speed" id="fast_2" value="1.35"><span>1.3배</span>
						</div>
						<div style="width: 25%; text-align: center; padding-top: 6px;">
							<input  class="inputMedia" type="radio" name="speed" id="fast_3" value="1.5"><span>1.5배</span>
						</div>
					</div>
				</div>
			</div>
			</div>
			
			<div id="cust_info_box" class="script_box_contents">
			<div style="display:inline-flex;margin-top: 5px;">
					<div style="width: 5px;height: 20px;background-color: #82e276;margin: 2px 10px 0px 3px;" class="float_left"></div>
					<p style="/* padding:3px; */font-weight: 600;/* margin-bottom: 3px; */width: 80px;float: left;align-self: center; color: black;">상품 설명</p>
				</div>
				<button id="clearAllRec" onclick="clearAllRec()">녹취 초기화</button>
				<button class="next_btn rec_confirm" onclick="recFilePlay('all')" style="border-radius: 0px 0px 0px 0px !important; font-size:14px;">청취 확인</button>
				<div id="productInfoScriptGrid"></div>
			</div>
			<div id="cust_info_box" class="script_Alram_contents">

			<div style="
			    display: flex;
			    flex-direction: row;
			    margin-bottom: 5px;
			">
			<div style="width: 5px; height: 20px; background-color: #82e276; margin:2px 5px 0px 3px;" class="float_left"></div>
			<strong style="padding:3px;font-weight: 600;/* margin-bottom: 5px; */color:black;align-self: end;"><span style="font-weight: bold;">'분석결과'</span>에 노란색이 뜨는 경우?</strong>
    		</div>
			<div id="Alram_wrap" class="Alram_wrap" style="height: 65px; margin-bottom: 15px; padding-top: 15px;
    padding-bottom: 15px; line-height: 23px;">
						<div id ="normalRecCommentBox" style="color:blue; font-size: 11pt;font-weight: bold; ">
<!-- 						 style="color:blue; font-size: 11pt;font-weight: bold; overflow:auto; height:400px !important;" -->
							<div>
							※ '추가확인'이 필요한 구간은 노란색으로 표시됩니다.
							</div>
							<div>
							&nbsp;&nbsp;&nbsp;&nbsp;구간별 노란색 버튼 클릭 ☞ AI분석결과 확인 ☞ 재녹취여부 결정
							</div>
							<div>
							 ※ 다만, 환경에 따라 실제 녹취내용과 분석결과가 다를 수 있습니다.
							</div>
						</div>
			</div>
						<div style="
			    display: flex;
			    flex-direction: row;
			    margin-bottom: 5px;
			">
			<div style="width: 5px; height: 20px; background-color: #82e276; margin:2px 5px 0px 3px;" class="float_left"></div>
			<strong style="padding:3px;font-weight: 600;/* margin-bottom: 5px; */color:black;align-self: end;"><span style="font-weight: bold;">녹취 시 유의사항</strong>
    		</div>
			<div id="Alram_wrap" class="Alram_wrap" style="    line-height: 22px;">
						<div id ="normalRecCommentBox" style="color:blue; font-size: 11pt;font-weight: bold;">
						<!--  style="color:blue; font-size: 11pt;font-weight: bold; overflow:auto; height:400px !important;" -->
<!-- 						style="color:blue; font-size: 10pt;font-weight: bold;" -->

							<div>
							※ 녹취는 "권유자"가 진행하시기 바랍니다.  
							</div>
							<div>
							※ 자동리딩일 경우에도 고객 답변여부는 직원이 직접 확인 필수
 
							</div>
							<div class="checkNumberBox">
							</div>
							<div>
							※ 버튼설명
							</div>
							<div style="font-size: 10pt">
							- ■ 구간녹취종료 : 구간별(단계별) 녹취를 종료하는 버튼
							</div>
							<div style="font-size: 10pt">
			       			  - 녹취초기화 : 새로고침기능. 처음부터 다시 녹취할 때 클릭. 
							</div>
							
						</div>
						<div id ="ManualRecCommentBox" style="color:blue; font-size: 11pt;font-weight:bold; display:none;	">
							<div>
							※ 녹취는 "권유자"가 진행하시기 바랍니다.  
							</div>
							<div>
							※ 자동리딩일 경우에도 고객 답변여부는 직원이 직접 확인 필수
							</div>
							<div class="checkNumberBox">
							</div>
							<div>
							※ 버튼설명
							</div>
							<div style="font-size: 10pt">
							- ■ 구간녹취종료 : 구간별(단계별) 녹취를 종료하는 버튼
							</div>
							<div style="font-size: 10pt">
							- 녹취초기화 : 새로고침기능. 처음부터 다시 녹취할 때 클릭.
							</div>
						</div>
			</div>
		</div>
		</div>
			<div class="LRline"></div>
		<div id="right_box">
			<div style="padding-left: 0px;">
				<div style="width: 5px; height: 20px; background-color: #82e276; margin:2px 5px 0px 3px;"class="float_left"></div>
				<p style="padding:3px; font-weight: 600;    margin-bottom: 3px; color:black; font-size:11pt;">스크립트</p>
			</div>

			<div class="script_play_contents">
				<div class="script_opt_contents">
					<div class="script_info_fld" style=" width: 64%; float: left; height: 100%;">
						<div id="script_name" style="font-weight: 900;  padding-left: 10px; font-size: 14px;"><span>[적합성 진단 정보 판단]</span></div>
						<div id="script_name_free" style="font-weight: 900; display:none;  padding-left: 10px; font-size: 14px;"><span>[자유녹취]</span></div>
					</div>
					<div class="script_text_info">
					<div style=" display: flex;   height: 70%;    width: 108%; justify-content: flex-end; align-items: center;">	
							<div class="script_text script_left_text script_img_size" style=" font-size: xx-large; font-weight: bolder;">&nbsp;</div>
							<div class="script_text_big script_left_btn script_img_size" style="width: 25%">&nbsp;</div>
							<div class="script_text_small script_left_btn script_img_size" style="width: 13% ">&nbsp;</div>
					</div>
					</div>
				</div>
				<div class="script_content_info">
					<div id="rec_script_box">

					</div>
				</div>
			</div>
			<button class="next_btn rec_confirm rec_save" onclick="scriptRecFileMerge()" style="font-size:14px;">녹취 완료</button>
			<input type="button" class="next_btn next_rec" onclick="nextStepRec()" value="다음"/>

		</div>
		<audio id="audioPlayer" controls src ="" style="display:none;">
		</audio>
		
		<div id="faceRecPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="ui_pannel_popup_header">
				<div class="ui_float_left">
						<p class="ui_pannel_tit">청취</p>
				</div>
				<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
			</div>
		</div>
	</div>
	</div>
	<div id="retryRecReasonPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">재녹취 사유</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
                       	<label class="ui_label_essential">재녹취 사유 타입</label>
                       		<select id="retryRecReason"></select>
                     	<label class="ui_label_essential retryRecReasonDetailClass" style="display:none;">재녹취 사유</label>
            		        <input type="text" class="retryRecReasonDetailClass" placeholder="(30자내로입력)" maxlength ="300" id="retryRecReasonDetail" style="display:none;"/>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="retryRecConfirmReason" class=""><spring:message code="message.btn.ok"/></button>
						<button id="retryRecCloseReason" class=""><spring:message code="message.btn.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="rowId"/>
	</div>	
	<div id="allRetryRecReasonPopup" class="popup_obj" style="margin-top: -116px;
    margin-left: -212px;width: 400px;">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">재녹취 사유</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
                       	<label class="ui_label_essential">재녹취 사유 타입</label>
                       		<select id="allRetryRecReason">
                       			<option value="4">전체 재녹취 - 녹취환경 미흡</option>
                       			<option value="5">전체 재녹취 - 녹취점검 지적</option>
                       			<option value="3">전체 재녹취 - 기타(직접입력)</option>
                       		</select>
                     	<label class="ui_label_essential allRetryRecReasonDetailClass" style="display:none;">재녹취 사유</label>
            		        <input type="text" class="allRetryRecReasonDetailClass" placeholder="(30자내로입력)" maxlength ="60" id="allRetryRecReasonDetail" style="display:none;"/>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="allRetryRecConfirmReason" class=""><spring:message code="message.btn.ok"/></button>
						<button id="allRetryRecConfirmReasonExit" class=""><spring:message code="message.btn.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="rowId"/>
	</div>	
	<div id="guideImgPopup" class="popup_obj" style="display:none;">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">단말기 가이드</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
		</div>
		<div class="ui_article ui_row_input_wrap" style="height:96%;">
			<div class="ui_pannel_row" style="height:inherit;">
				<div class="ui_padding" style="height:inherit;">
                	<div class="ui_article ui_row_input_wrap guideImg">
				</div>
				</div>
			</div>
		</div>
	</div>	
	
	<div id="guideImgPopup2" class="popup_obj ui-draggable" style="margin-top: -369px; margin-left: -430.5px; left: 1175.41px; top: 415.547px; width: 450px;">
		<div class="ui_popup_padding">
			<div class="popup_header ui-draggable-handle">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">불완전 판매</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
		</div>
		<div class="ui_article ui_row_input_wrap" style="/* height: inherit; */height: 203px;padding: 0px;">
			<div class="ui_pannel_row" style="height:inherit;">
				<div class="ui_padding" style="/* height:inherit; */">
                	<div class="ui_article ui_row_input_wrap guideImg">
				</div>
				</div>
			</div>
		</div>
	</div>
	 <div class="modal ac" id="taResultLastPop" style="display:none;">
   <div class="modal-pop" style="width: 425px;">
    <div class="modal-header">
     <span class="tit" style=" font-weight:bold;font-size:16px;">분석 결과</span>
    </div>
    <div class="modal-body" style="display: flex;flex-wrap: wrap; height: 190px; justify-content: center; background: rgb(242,242,242);">
      <div style="margin-top:13px; height: 107px; width: 90%; background-color: rgb(89,89,89); display: flex; flex-direction: row; justify-content: center; align-items: center;">
       <div style="text-align:center;">
        <p style="color:white; font-weight:bold; font-size:15px;">AI 분석결과 녹취내용 중</p>
        <p style="color:white; font-weight:bold; font-size:15px;">확인이 필요한 구간이 있습니다.</p>
        <p style="color: white; padding-top: 15px; font-size: 13px; display: flex; flex-direction: row; align-items: center;">
         <span style="height:16px; width:16px; margin-right:4px; text-indent:-9999px; border-radius: 8px; display:inline-block; background: white url(${resourcePath}/common/recsee/images/project/icon/CircleYellow.png) center/cover no-repeat;">dummy</span>버튼클릭 ☞ AI분석결과 확인 ☞ 재녹취여부 결정
        </p>
       </div>
      </div>
      <button id="taPopupCheckBtn" class="btn-primary" style="margin-top: 30px;">확인</button>
    </div>
   </div>
  </div>

	<div class="modal ac" id="taResultPop" style="display: none;">
		<div class="modal-pop" style="">
			<!-- 	<div class="modal-pop"> -->
			<div class="modal-header">
				<span class="tit">TA 도움</span> <a href="#" class="modal-close"></a>
			</div>
			<div class="modal-body taResultPopBodyAll">
				<article style="margin-top: 0px;">
					<div class="taResultPopBodyHeader">불완전판매</div>
					<div class="taResultPopBody">
						해당 <span class="taResultPopStepNameBody"> </span> 부분은 녹취요건 불충족으로
						재확인이 필요한 구간입니다.
					</div>
				</article>

				<div class="btn-wrap flex-r">
					<p class="taResultPopUpMent">본 TA 도움화면은 판매인의 녹취점검에 도움을 드릴수있으나
						최종판단은 판매인에 있습니다.</p>
					<button class="btn-primary taResultPopClose" id="">확인</button>
				</div>
			</div>
		</div>
		<div></div>
	</div> 

	<div class="modal ac" id="taErrorResultPop" style="font-family: 'aGothic' !important;border-radius: 0px; display:none">
		<div class="modal-pop" style="    width: 450px; border-radius: 0px;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">분</span>
				<span class="tit" style="font-weight: bold;font-size: 18px;font-family: 'aGothic';margin-left: 7px;">석</span>
    <span class="tit" style="font-weight: bold;font-size: 18px;font-family: 'aGothic';margin-left: 7px;">결</span>
    <span class="tit" style="font-weight: bold;font-size: 18px;font-family: 'aGothic';margin-left: 7px;">과</span>
</div>
			<div class="modal-body taResultPopBodyAll">
				<article style="margin-top: 0px; width: 100%;">
					<div class="taResultPopBodyReason" style="text-align: center;border: 4px solid lightgray;">
						<div class="taErrorResultPopStepNameBody" style="width: 100%;height: 100%;">

						</div>
					</div>
				</article>

				<div class="btn-wrap flex-r" style="flex-direction: column;align-items: center; height: auto;">
					<p class="taResultPopUpMentDetailReason">
					<span>
					※ 환경에 따라 실제 녹취내용과 분석결과가 다를 수 있습니다. 
					</span>
					<span>
					&nbsp;&nbsp;&nbsp;	재녹취 대상이 아닐 경우 다음단계 진행하시기 바랍니다.
					</span>
					</p>
					<button class="btn-primary taResultPopClose" id="" style="
    border-radius: 0px;
    font-size: 15px;
">확인</button>
				</div>
			</div>
		</div>
		<div></div>
	</div> 
	<div class="modal ac" id="oldClientPop" style="display:none;">
		<div class="modal-pop" style="">
			<div class="modal-header">
				<span class="tit">신규 클라이언트 다운로드 안내</span> <a href="#"
					class="modal-close"></a>
			</div>
			<div class="modal-body ">
				<article style="margin-top: 0px;">
					<div class="taResultPopBodyHeader">보이스 클라이언트 버전 불일치</div>
					<div class="taResultPopBody">
						<div
							style="display: flex; flex-direction: row; justify-content: space-evenly;">
							<button class="btn-primary blink" style="width: 142px;"	id="clientDown">최신 클라이언트 다운로드</button>
						</div>

					</div>
				</article>
				<div class="btn-wrap flex-r">
					<div
						style="display: flex; width: 100%; justify-content: center; align-content: space-around; flex-direction: column; align-items: center;">
						<p class="taResultPopUpMent"
							style="width: 100%; text-align: center;">클라이언트가 최신버전이 아니므로,
							본녹취를 진행하실 수 없습니다.</p>
					</div>

				</div>
			</div>
		</div>
		<div></div>
	</div>
	<div class="modal ac" id="micErrorPop" style="display:none;">
		<div class="modal-pop" style="">
			<div class="modal-header">
				<span class="tit">단말기 연결상태 및 클라이언트 버전 확인</span>
			</div>
			<div class="modal-body">
				<article style="margin-top: 0px;">
					<div class="taResultPopBodyHeader">※ 아래 2개 사항을 반드시 확인해주세요!</div>
					<div class="taResultPopBody"  style="border-bottom:0px solid;">
						<div
							style="display: flex; flex-direction: column; justify-content: space-evenly;">
							<p><strong style="color:black">1. 클라이언트 최신버전 설치여부확인! (최신버전 v2.3)</strong></p>
							<p><strong style="color:black">&nbsp;&nbsp;&nbsp;&nbsp;☞ 아래 「최신클라이언트다운로드」 버튼 클릭 후 설치</strong></p>
							<p><strong style="color:black">&nbsp;&nbsp;&nbsp;&nbsp;☞설치후 현재 녹취창 닫기 → 다시 실행</strong></p>
							<p><strong style="color:black">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(새로 녹취창을 열지 않으면 정상녹취 불가)</strong></p>
							<p><strong style="color:black">2. 녹취단말기 연결 상태확인 </strong></p>
							<p><strong style="color:black">&nbsp;&nbsp;&nbsp;&nbsp;☞ 녹취단말기 케이블을 PC 본체에 연결</strong></p>
							<p><strong style="color:black">&nbsp;&nbsp;&nbsp;&nbsp;☞ 녹취단말기 전원부 적색 LED 점등 확인</strong></p>
							<button class="btn-primary blink clientDown" style="width: 142px; margin-top: 4px ;align-self: center;"
								id="clientDown">최신 클라이언트 다운로드</button>
						</div>

					</div>
				</article>
			</div>
		</div>
		<div></div>
	</div>
	<div class="modal ac" id="newMicErrorPop" style="display:none;">
		<div class="modal-pop" style="">
			<div class="modal-header">
				<span class="tit">단말기 이상</span><a href="#" class="modal-close"></a>
			</div>
			<div class="modal-body">
				<article style="margin-top: 0px;">
					<div class="taResultPopBodyHeader">※ 녹취 단말기 상태확인 필요!</div>
					<div class="taResultPopBody"  style="border-bottom:0px solid;">
						<div
							style="display: flex; flex-direction: column; justify-content: space-evenly;">
							<p><img src="${recseeResourcePath}/images/project/icon/wooribank/RecSee_VOIP.ico;" style="width: 20px;height: 20px;"/><strong style="color:black">버전 확인후 클라이언트 버전이 2.3이 아닌경우 재설치하시길 바랍니다.</strong></p>
							<p><strong style="color:black">문의 02-xxxx-xxxx</strong></p>
							<button class="btn-primary blink clientDown" style="width: 142px; margin-top: 4px ;align-self: center;"
								id="clientDown">최신 클라이언트 다운로드</button>
						</div>

					</div>
				</article>
			</div>
		</div>
		<div></div>
	</div>
	<div class="modal ac" id="webSocketClosePop" style="display:none;">
		<div class="modal-pop" style="">
			<div class="modal-header">
				<span class="tit">클라이언트 안내</span><a href="#" class="modal-close"></a>
			</div>
			<div class="modal-body">
				<article style="margin-top: 0px;">
					<div class="taResultPopBodyHeader">※보이스 클라이언트 상태확인 필요!</div>
					<div class="taResultPopBody"  style="border-bottom:0px solid;">
						<div
							style="display: flex; flex-direction: column; justify-content: space-evenly;">
							<p><strong>클라이언트 상태를 확인 후, WINI에서 녹취를 재실행 해주세요.</strong></p>
						</div>

					</div>
				</article>
			</div>
		</div>
		<div></div>
	</div>
<div class="modal" id="errorModalMic">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px;display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; color:black; font-size: 15px;" id="errorText1"></p>
					<p style="font-family: 'NanumSquare'; color:black; font-size: 15px; padding-top: 8px;" id="errorText2"></p>
					<p style="font-family: 'NanumSquare'; color:black; font-size: 15px; padding-top: 8px;" id="errorText3"></p>
					<p style="font-family: 'NanumSquare'; color:black; font-size: 15px; padding-top: 8px;" id="errorText4"></p>
				</article>
				<button class="btn-primary" id="errorModalMicBtn" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorModalMicClose()">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button>
			</div>
		</div>
	</div>
</body>

</html>
