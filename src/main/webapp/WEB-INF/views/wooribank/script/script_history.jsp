<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
if(request.getProtocol().equals("HTTP/1.1"))
	response.setHeader("Cache-Control", "no-cache");
%>

<!DOCTYPE html>
	<head>
		<meta http-equiv="Expires" content="-1">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="No-Cache">
		<title>변경이력 조회</title>
		<%@ include file="/WEB-INF/views/common/include/commonVar.jsp"%>
		<script type="text/javascript">
		var downloadYn = "${nowAccessInfo.getDownloadYn()}";
		var excelYn = "${nowAccessInfo.getExcelYn()}";
		var writeYn = "${nowAccessInfo.getWriteYn()}";
		var modiYn 	= "${nowAccessInfo.getModiYn()}";
		var delYn 	= "${nowAccessInfo.getDelYn()}";
		// 페이지 팝업시 전달받은 값을 전역변수로 추가
		var productListPk = '<c:out value="${rsProductPk}"/>';
		var dummyString = '<c:out value="${dummyString}"/>';
		</script>
		<%-- compo --%>
		<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/streaming/js/jquery-sortable.js"></script>
		<%-- recsee --%>
		<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/wooribank/script/script_history.css?ver=20220331" />
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_var.js?ver=20220331"></script>
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_api.js?ver=20220331"></script>
		<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/wooribank/script/script_history.js?ver=20220331"></script>
	</head>
	<body onpagehide="resetPage()">
		<div id="tableBox"></div>
		<div id="dataBox"></div>
	</body>
</html>