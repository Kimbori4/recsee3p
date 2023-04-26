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

	<link rel="stylesheet" type="text/css" href="${siteResourcePath }/css/page/header.css" />

	<script type="text/javascript" src="${compoResourcePath }/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/db_infoPopup.js"></script>
	
	<%-- css page --%>
 	<link rel="stylesheet" type="text/css"	href="${recseeResourcePath }/css/page/admin/admin.css" />

	<script>
	$(function() {
	    $(window).resize(function() {

	    }).resize();
	})
	</script>
	<style>
		#popup_header {
			font-size:1.1em;
			margin:10px;
			vertical-align:middle;
		}
		#selectDBHeader,#upsertDBHeader {
			width:100%;
			height:40px;
			background-color:#dddddd;
		}
		#selectDBContent {
			width:100%;
			padding-bottom:10px;
		}
		.selectTable, .selectColumn, #inoutBtn, #inoutBtn2, .setColumnBox{
			margin:10px;
			float:left;
		}
		.selectTable {
			clear:left;
		}
		.setSqlInfo {
			margin:10px;
			float:left;
		}
		#inoutBtn {
			padding-top:100px;
		}
		#inoutBtn2 {
			padding-top:50px;
		}
		#selectedColumn, #selectedColumn2, #selectedColumn3 {
			width:250px;
			height:300px;
		}
		.setBtn {
			width:80px;
			margin:10px;
		}
		.dbInfoList {
			width:250px !important;
			height:300px;
		}
		.sqlInfo {
			margin-right:10px;
			height:15px;
			padding:0 !important;
		}
		#sqlName, #sqlName2 {
			width:200px !important;
			margin-right:50px;
		}
		#sqlDescription, #sqlDescription2{
			width:363px !important;
		}
		#sqlContent, #sqlContent2{
			width:765px !important;
		}
		.popup_obj {
			display:block;
			top:0;
			left:0;
			border:none;
			box-shadow:none;
		}
		.ui_pannel_popup_header .ui_pannel_tit {
			color:black !important;
		}
		.ui_row_input_wrap label {
			width: 110px !important;
		}
		#selectSqlInfoBox, #upsertSqlInfoBox{
			clear:both;
		}
		.ui_article{
			min-width:1210px;
		}
	</style>
</head>
<body>

	<div id="addDB" class="popup_obj" style="width:100%">
        <div class="ui_popup_padding">
            <div class="ui_article">
                <div class="ui_pannel_row" style="background-color: rgb(43, 148, 200); color:#ffffff; ">
                    <div class="ui_float_left">
                        <p id="popup_header">Setting SQL Info</p>
                    </div>
                </div>
            </div>
            
            <div class="ui_article ui_row_input_wrap">
            	
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    
			            <div class="ui_article">
		                    <div class="ui_float_left">
		                        <p id="popup_header">Select query</p>
		                    </div>
			            </div>  
	                
						<div id="selectSqlInfoBox">
							<div class="setSqlInfo">
								<label class="ui_label_essential">SQL Name</label>
									<input type="text" class="sqlInfo" id="sqlName">
							</div>
							<div class="setSqlInfo">
								<label class="ui_label_essential">SQL Description</label>
									<input type="text" class="sqlInfo" id="sqlDescription">
							</div>
						</div>      
						
						
						<div class="selectTable">
							<select id="tableList" class="dbInfoList" size="12">
								<option value="" selected disabled>Table</option>
								<c:forEach var="tableResult" items="${tableResultList}">
									<option value="${tableResult.table_name}">${tableResult.table_name}</option>
								</c:forEach>
							</select>
						</div>
                    	
						<div class="selectColumn">
							<select id="columnList" class="dbInfoList" size="12">
								<option value="" selected disabled>Column</option>
							</select>
						</div>
						
						<div id="inoutBtn">
							<button class="setBtn" id="addColumnList">Add</button>
							<br>
							<button class="setBtn" id="takeAwayColumnList">Remove</button>
						</div>      
						                      
						<div class="setColumnBox">
							<select id="selectedColumn" multiple="multiple">
							</select>
						</div> 
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
						<button id="selectSQL">save</button>
                    </div>
                </div>
            </div>
			
            <div class="ui_article ui_row_input_wrap">
                <div class="ui_pannel_row">
                    <div class="ui_padding">
                    
			            <div class="ui_article">
		                    <div class="ui_float_left">
		                        <p id="popup_header">Upsert query</p>
		                    </div>
			            </div>
			            
						<div id="upsertSqlInfoBox">
							<div class="setSqlInfo">
								<label class="ui_label_essential">SQL Name</label>
									<input type="text" class="sqlInfo" id="sqlName2">
							</div>
							<div class="setSqlInfo">
								<label class="ui_label_essential">SQL Description</label>
									<input type="text" class="sqlInfo" id="sqlDescription2">
							</div>
						</div> 
						
						<div id="selectSqlInfoBox">
							<div class="setSqlInfo">
								<label class="ui_label_essential">SQL Query</label>
									<input type="text" class="sqlInfo" id="sqlContent2">
							</div>
						</div>
						
						<div class="selectTable">
		                    <select id="tableList2" class="dbInfoList" size="12">
								<option value="" selected disabled>Table</option>
								<c:forEach var="tableResult" items="${tableResultList}">
									<option value="${tableResult.table_name}">${tableResult.table_name}</option>
								</c:forEach>
							</select>
						</div>
                    	
						<div class="selectColumn">
							<select id="columnList2" class="dbInfoList" size="12">
								<option value="" selected disabled>Column</option>
							</select>
						</div>
						
						<div id="inoutBtn2">
							<button class="setBtn" id="addColumnList2">Add Key</button>
							<br>
							<button class="setBtn" id="takeAwayColumnList2">Remove Key</button>
							<br>
							<button class="setBtn" id="addColumnList3">Add Normal</button>
							<br>
							<button class="setBtn" id="takeAwayColumnList3">Remove Normal</button>
						</div>      

						<div class="setColumnBox">
							<select id="selectedColumn2" multiple="multiple">
							</select>
						</div>
						<div class="setColumnBox">
							<select id="selectedColumn3" multiple="multiple">
							</select>
						</div>       
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
						<button id="upsertSQL">save</button>
                    </div>
                </div>
            </div>            
        </div>
    </div>	
    <input type="hidden" id="dbName" value="${dbName}">
</body>
</html>

	