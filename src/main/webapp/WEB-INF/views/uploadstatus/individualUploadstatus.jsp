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
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/uploadstatus/individualUploadstatus.css" />
	
	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/uploadstatus/individualUploadstatus.js"></script>
	
</head>
<body onload='individualUploadstatusLoad();'>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
	
    <div class="main_contents">
    
    	<div class="ui_pannel_row">
			<div class="ui_float_left">
               	<p class="ui_pannel_tit"><spring:message code="uploadstatus.label.indivUpStat"/></p>
				<div class="main_form">
				
					<fieldset class="search_fieldset" id="">
		    			<legend><spring:message code="uploadstatus.label.date"/></legend>
		   				
		    			<input title="<spring:message code="uploadstatus.label.sDate"/>" type="text" id="sDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" maxlength='8' placeholder="<spring:message code="uploadstatus.label.sDate"/>"/>
				        <input title="<spring:message code="uploadstatus.label.eDate"/>" type="text" id="eDate" class="ui_input_cal icon_input_cal inputFilter numberFilter dateFilter" maxlength='8' placeholder="<spring:message code="uploadstatus.label.eDate"/>"/>
				        
		   				<select title="<spring:message code="uploadstatus.label.timeFrom"/>" class="sel_time" id="sTime" required="" fieldset="sTime">
		   					<option value=""><spring:message code="uploadstatus.label.timeFrom"/></option>
		   					<option value=""><spring:message code="uploadstatus.label.all"/></option>
		   					<c:forEach var = "item" varStatus="i" begin="0" end="23" step="1">
		   						<option value="<c:if test="${item < 10 }">0</c:if>${item}:00:00"><c:if test="${item < 10 }">0</c:if>${item}:00:00</option>
		   					</c:forEach>
	   					</select>
		   				<select title="<spring:message code="uploadstatus.label.timeTo"/>" class="sel_time" id="eTime" required="" fieldset="eTime">
		   					<option value=""><spring:message code="uploadstatus.label.timeTo"/></option>
		   					<option value=""><spring:message code="uploadstatus.label.all"/></option>
		   					<c:forEach var = "item" varStatus="i" begin="0" end="23" step="1">
		   						<option value="<c:if test="${item < 10 }">0</c:if>${item}:00:00"><c:if test="${item < 10 }">0</c:if>${item}:00:00</option>
		   					</c:forEach>
	   					</select>
		   			</fieldset>
		   			
	   				<fieldset class="search_fieldset" id="extNumberField"><legend><spring:message code="uploadstatus.label.recInfo"/></legend>
						<input title="<spring:message code="uploadstatus.label.extNum"/>" class="inputFilter numberFilter" id="extNum" type="text" maxlength="11" placeholder="<spring:message code="uploadstatus.label.extNum"/>" fieldset="extNumberField">		   			
						<input title="<spring:message code="uploadstatus.label.custPhone1"/>" class="inputFilter numberFilter" id="custPhone1" type="text" maxlength="11" placeholder="<spring:message code="uploadstatus.label.custPhone1"/>" fieldset="extNumberField">
		   			</fieldset>
		   
		   			<fieldset class="search_fieldset" id="fCustInfo"><legend><spring:message code="uploadstatus.label.status"/></legend>
		   				<select title="<spring:message code="uploadstatus.label.workType"/>" class="inputFilter numberFilter" id="workType" maxlength="11" placeholder="<spring:message code="uploadstatus.label.workType"/>" fieldset="fCustInfo">
		   					<option value=""><spring:message code="uploadstatus.label.workType"/></option>
		   					<option value="B"><spring:message code="uploadstatus.label.batch"/></option>
		   					<option value="D"><spring:message code="uploadstatus.label.direct"/></option>
		   				</select>
		   				<select title="<spring:message code="uploadstatus.label.uploadStatus"/>" class="inputFilter numberFilter" id="result" maxlength="11" placeholder="<spring:message code="uploadstatus.label.uploadStatus"/>" fieldset="fCustInfo">
		   					<option value=""><spring:message code="uploadstatus.label.uploadStatus"/></option>
		   					<option value="1"><spring:message code="uploadstatus.label.transCompl"/></option>
		   					<option value="9"><spring:message code="uploadstatus.label.noRec"/></option>
		   					<option value="2"><spring:message code="uploadstatus.label.retry1"/></option>
		   					<option value="4"><spring:message code="uploadstatus.label.retry2"/></option>
		   					<option value="6"><spring:message code="uploadstatus.label.transFailInfo"/></option>
		   					<option value="7"><spring:message code="uploadstatus.label.transFailFile"/></option>
		   					<option value="8"><spring:message code="uploadstatus.label.waitTrans"/></option>
		   					<option value="0"><spring:message code="uploadstatus.label.notSend"/></option>
		   				</select>
		   			</fieldset>
		   			
		   			<button class="ui_main_btn_flat icon_btn_search_white" id="searchBtn" ><spring:message code="uploadstatus.label.search"/></button>
			</div>
		</div>
	                
    	
		<div class="gridWrap">
	        <div id="gridUploadManage"></div>
	        <div id="pagingUploadManage"></div>
       </div>
	</div>
	</div>
</body>
</html>