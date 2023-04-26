<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
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
 	<link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecording/face_recording.css" /> 

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/scriptRegistration/script_Registration.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/websocket.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/streaming/js/jquery-sortable.js"></script>
	
	<script>
	var listenIp = "${listenIp}";
	var scriptId = "";
	//프로그레스 처리
	$(function() {
		var PopupCheck = <%= request.getAttribute("PopupCheck") %>;
		if(PopupCheck == true){
			$(".main_lnb").hide();
			$(".main_header").hide();
		}
		
	    $(window).resize(function() {
	    	// 현재 document 높이
			var documentHeight = $(window).height();

			// 페이징이 있음 페이징 만큼 뺴주깅
			var pagingHeight = ($(".dhx_toolbar_dhx_web").length == 0 ? 0 : $(".dhx_toolbar_dhx_web").height());
			var gridResultHeight = (documentHeight - pagingHeight);

			/* $(".main_contents").css({"height": + (gridResultHeight - 92)+ "px"}) */
	    }).resize();
	 });
	</script>
	<style>
		.xhdr{
			height:0px !important;
		}
		#scriptGrid{
			width:100% !important;
			height:400px;
		}
		.main_contents {
			width:100%;
			/* height : 100% */
		}
		
		#top_box {
			width:100%;
			height:40%;
			background-color:#f7f7f7;
			padding:auto;
		}
		
		.top_box_contents {
			width:75%;
			height:15%;
			float:left;
		}
		
		#left_box {
			width:20%;
			height:100%;
/* 			height:60%; */
			display : inline-block;
			float: left;
		}
		
		#faceRecMenu {
			width:100%;
/* 			width:calc(20% - 0px); */
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
		
		#script_search_box {
			width:100%;
/* 			width:calc(50% - 22px); */
			height:80%;
/* 			height:calc(100% - 20px); */
			background-color:white;
			padding:10px;
			font-weight: bold;
			float:right;
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
		    margin-left : 5px;
		   	margin-top: 10px;
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
  		
		#cust_info_contents ul label, #user_info_contents ul label, #script_search_box ul label{
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
		
		#script_search_box ul {
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
		#script_search_box ul input {
			width: calc(100% - 162px);
		    height: 30px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		    background-color:#f7f7f7;
		    border:none;
		    padding-left:10px;
		}
		#script_search_box ul select{
			width: 150px;
/* 			width: calc(100% - 152px); */
		    height: 30px;
		    float: left;
		    display: block;
		    margin-bottom:5px;
		 /*    background-color:#f7f7f7; */
		    border:1px solid #dddddd;
/* 		    border:none; */
		    padding-left:10px;
		}
		#script_search_box ul textarea{
			background-color: white ;
   			border : 2px solid #4ccde2 ;
   			border-radius: 10px;
			width: calc(99% - 155px);
 		    height: calc(107% - 5px); 
		    float: left;
		    display: block;
		    margin-bottom:5px;
		    border:none;
		    padding-left:15px;
		    resize: none;
		}
		#script_search_box ul li .textWrap {
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
		

		

		

		
		.play_time {
			margin:0px 0 0 10px;
/* 			margin:20px 0 0 10px; */
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
		

		#search_script_btn{
			float:left !important;
		}
		
		#addProduct{
            /* width: 400px; */
           	width: 500px;
    		height: 700px;
        }
        
        #productAddBtn, #productcloseBtn {
        	width:70p;
        	height:40px;
        	padding:0;
        	margin-left:5px;
        }
        
        
        .search_script_contents  {
	    width: 58%;
	    height: 30px;
	    display: inline-block;
	    margin: 5px 0px 5px 3px;
        }
      .script_search_btn{
	    width: 12%;
	    height: 39px;
	   /*  margin: 0px 0px 0px 5px; */
	    text-align: center;
      }
      
      .group_script_contents{
		width: 25%;
	    height: 40px;
	    margin: 5px -3px 5px 5px;
	    font-size: 12px;

      }
      
      .script_contents{
        width: 97%;
    	height: 30px;
    	margin: 5px 5px 5px 5px;
      
      }
      
      .title{
      width : 100%;
      margin: 20px 0px 5px 5px;
      }
      
      .script_gbn{
      width : 13% !important;
      height : calc(30% - 100px) !important;
      
      }

 /*      .add_script_contents{
      float: right !important;
      } */
      
      .ui_left{
      width : 40%;
      float : left;
      }
      
      .ui_right{
      width : 60%;
      float : right;
      }
      
      .ui_row_input_wrap label{
      	width: 95px !important;
      }
      
      #productContent{
      width : 75.5%;
      height: 500px;
      }
      
      .ui_row_input_wrap input{
      
        width: 100%;
    	height: 30px;
/*     	height: 500px; */
    	background-color: white;
      }
      .editProduct{
      width : 400px;
      display: block;
      margin-top: -117px;
      margin-left: -212px;
      }
      
      .line1, .line2, .line3{
        width: 100%;
   	 	height: 40px;
    	margin-left: 5px;
      
      }
      
      #prize_name, #prize_type, #prize_grade{
      	width: 25%;
      	height: 40px;
    	display: inline-block;
  		float: left;
      	background: #efefef;
      	padding-top: 3px;
      	margin-top: 3px;
      	font-family: NotoSansKR;
	    font-size: 14px;
	    font-weight: 500;
	    font-stretch: normal;
	    font-style: normal;
      }
      
      #prize_name2{
		margin: 5px -3px 5px 1px !important;
	    float: left !important;
      }
       #prize_name2, #prize_type2, #prize_grade2{
     
	    width: 74%;
	    height: 40px;
	    float: right;
	    border: 1px solid #efefef;
	     font-family: NotoSansKR;
	    font-size: 14px;
	    font-weight: 500;
	    font-stretch: normal;
	    font-style: normal;
      }
      #prize_type2{
          margin-top: 3px !important;
      }
	   .script_tree{
	    height: 400px;
    	background-color:#f7f7f7 ;
    	margin-left: 7px;
   		width: 97%;
	   }   
	   
	   .top_area{
	   margin-bottom: 45px;
	   }
	   
	   #right_box{
	    width: 78%;
    	height: 80%;
    	display: inline-block;
   	 	float: right;
   	 	margin-right: 10px;
	   }
	   .right_bottom{
	   width : 100%;
	   height: 20%;
	   }
   
   
   	.ui_padding_1{
   		width : 100%;
	   height: 100%;
   	}
   
   		#popadd_script_btn{
   		width : 100%;
   		}
   .add_script_btn{
   font-size: 14px;
   }
   
  
 
 
   
  
  
   #popup_scriptType{
   position: relative;
    width: 200px;
    height: 47px;
    margin-right: 5px;
   }
   #popup_searchScript{
       width: 1150px;
       height: 30px;
    	margin-right: 8px;
   }
   #popup_searchBtn{
       width: 100px;
      height: 48px;
      margin-top: 5px;
   }
   
	</style>
</head>
<body>
	<canvas id="canvas" width="1000" height="325" style="display:none;"></canvas>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
    <!-- Contents -->
    <div class="main_search_contents" id="main_search_script_contents" style="display:none;">
		<!--죄측 스크립트 목록  -->
		<div id="left_box">
			<div class="title">
			<div style="width:5px; height:25px; background-color:#449ed7; margin:-3px 5px 0 0;" class="float_left"></div>
			<div style="height:25px; font-weight: bold; margin:10px 0 10px 0;">스크립트 검색</div>
			</div>
				<div class="top_area">
					<ul>
						<li>
							<select id="scriptType" style="position:relative" class="group_script_contents">
								<option id="script_Type" value=""></option>
								<option id="script_Type" value=""></option>
								<option id="script_Type"  value=""></option>
								<!-- <option value="direct">직접 입력</option> -->
							</select>
							<input type="text" id="searchScript" class="search_script_contents"placeholder="상품을 검색하세요."/>
							<input type="button" id="searchBtn" class="script_search_btn" value="조회" >	
							<input type="text" id="inputDirect" style="width:90%; display:none; position:absolute"/>
						</li>

					</ul>

					 
					<div class="script_search_result" >
						<div class="line1">
							<div id="prize_name">상품명</div>
								<select id="prize_name2" style="position:relative" class="group_script_contents" value="">

								</select>
							</div>
						<div class="line2">
							<div id="prize_type">상품분류</div>
							<div id="prize_type2" value="Kbstar2"></div>
						</div>

					</div>
					
					
					
				</div>
				
			<div class="title">
			<div style="width:5px; height:25px; background-color:#449ed7; margin:-3px 5px 0 0;" class="float_left"></div>
			<div style="height:25px; font-weight: bold; margin:10px 0 10px 0;">스크립트 목록</div>
			</div>	
			
			

			<div id="faceRecMenu" class="float_left">		
	<!--▼ 상품 선택에 따른 트리구조로 상품 보여주기▼  -->
				<div class="script_tree" value="KBstar" id="script_name_list">
				
			
					<div id="scriptGrid">
				
				</div>
				</div>
				<input type="hidden" id="scriptCode"/>
				<button id="add_script_btn">스크립트 단계 수정</button>			
			</div>
		</div>
			

		<!--우측 스크립트 검색 -->
			<div id="right_box">
				<div id="script_search_box" class="script_menu_class ">
					<div style="width:5px; height:25px; background-color:#449ed7; margin:6px 5px 0 0;" class="float_left"></div>
					<div style="height:25px; font-weight: bold; margin:10px 0 10px 0;">스크립트 작성</div>
					<!-- 스크립트 검색 전체 영역-->
					<div class="script_search_result" >
						<div class="script_search_result_header">
							
						</div>
						<div class="script_search_result_content">
						</div>
					</div>
					
				</div>
				<!--우측하단-->
				<div class="right_bottom">
					<!--수정/삭제 버튼-->
					<div class="right_bottom_btn">
						<input type="hidden" id="scriptCode"/>
						<button id="close_script_btn">닫기</button>
					</div>
				</div>
			</div>		
			
			
	
	
</div>
	
	<!-- 상품 추가 팝업창 -->
	<div id="addProduct" class="popup_obj">
        <div class="ui_popup_padding">
            <!-- popup header -->
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit">스크립트 추가</p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray" style="height: 24px;"></button>
                    </div>
                </div>
            </div>
            <!-- popup body -->
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
	             <!--    <div class="ui_padding_1" id="scriptTitle" style=" background-color:white;">
	                 <input type="text" id="scriptTitle_1" class="add_script_contents" />
	                </div> -->
	                <ul class="ui_padding_1" id="scriptTitle" style=" background-color:white;">
	                	<li><input type="text" id="scriptTitle_1" class="scriptTitle_1" /></li>
	                	<li><input type="text" id="scriptTitle_1" class="scriptTitle_1" /></li>

	                </ul>
                   	<div class="ui_padding_2">
                   	<button id="popadd_script_btn" class="popadd_script_btn">스크립트 추가</button>
	                </div>
               </div>
            </div>
            <!--팝업창 하단 수정 버튼-->
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                    	<button id="productcloseBtn" class="ui_main_btn_flat">취소</button>
                        <button id="productAddBtn" class="ui_main_btn_flat">저장</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    

    
    
</body>