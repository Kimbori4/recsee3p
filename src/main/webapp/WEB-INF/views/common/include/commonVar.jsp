<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="com.furence.recsee.common.model.LoginVO"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
%>


<c:set var="locale" value="${pageContext.response.locale}" scope="session" />

<!DOCTYPE html>
<html lang="<c:out value="{locale}"/>">
<head>
	<title>RecSee</title>

	<meta charset="UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<%-- 공통 변수 설정 --%>
	<c:set var="url" value="${url}"/>
	<c:set var="urlCode" value="${urlCode}"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="commResourcePath" value="${resourcePath}/common"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<c:set var="webPath" value="${contextPath}"/>
	<c:set var="userLevel" value="${userInfo.getUserLevel()}"/>
	<c:set var="userId" value="${userId}"/>
	<c:set var="userName" value="${userName}"/>
	<c:set var="userInfoJson" value="${userInfoJson}"/>
	<c:set var="accessLevel" value="${nowAccessInfo.getAccessLevel()}"/>
	
	<%-- 사이트 템플릿에 따른 경로 용도로 사용 --%>
	<c:set var="recseeResourcePath" value="${commResourcePath}/recsee"/>
	<%-- 사이트 템플릿에 따른 경로 용도로 사용 --%>
	<c:set var="siteType" value="${systemTemplates}" />
	<c:set var="evalThema" value="${evalThema}" />
	<c:set var="recsee_mobile" value="${recsee_mobile}" />
	<c:set var="tabMode" value="${tabMode}" />
	<c:set var="clientIpChk" value="${clientIpChk}" />
	<c:set var="ipChkDup" value="${ipChkDup}" />
	<c:set var="siteResourcePath" value="${commResourcePath}/${siteType}"/>
	
	<c:set var="baseActivColor" value="${baseActivColor}"/>
	<c:set var="baseMainColor" value="${baseMainColor}"/>
	<c:set var="baseMainTabbarColor" value="${baseMainTabbarColor}"/>
	<c:set var="baseMainTxtColor" value="${baseMainTxtColor}"/>
	<c:set var="baseSubColor" value="${baseSubColor}"/>
	<c:set var="baseSubTxtColor" value="${baseSubTxtColor}"/>
	
	<c:set var="tabMode" value="${tabMode}"/>
	<c:set var="downloadFormat" value="${downloadFormat}"/>
	
	<c:set var="STTPlayer" value="${STTPlayer}"/>
	
	<c:set var="HTTP" value="${HTTP}"/>
	<c:set var="path" value="${path}"/>
	
	<%! 
		public String XssFilter1(Object value) {
			if (value == null || "".equals(value) || "null".equals(value)) {
				return "";
			}
			
			String checked = value.toString();
			
			checked = checked.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			checked = checked.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
			checked = checked.replaceAll("'", "&#39;");
			checked = checked.replaceAll("eval\\((.*)\\)", "");
			checked = checked.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
			checked = checked.replaceAll("script", "");
			checked = checked.replaceAll("`", "");
			checked = checked.replaceAll("//", "");
		    return checked;
		}
	
		public String getUserInfoDetail(Object model, String detail) {
			String result = "";
			
			if(model != null && detail != null) {
				LoginVO userInfo = (LoginVO) model;
				switch(detail) {
				case "userLevel":
					result = userInfo.getUserLevel();
					break;
				}
			}
			return result;
		}
	%>
	
	<script>
		var connectUrl = "N";
		var connectUrlCode = "N";
		var contextPath = "<%=XssFilter1(pageContext.getAttribute("contextPath"))%>";
		var resourcePath = contextPath + "/resources";
		var commResourcePath = resourcePath + "/common";
		var compoResourcePath = resourcePath + "/component";
		var systemTemplates = "<%=XssFilter1(request.getAttribute("systemTemplates"))%>";
		var evalThema = "<%=XssFilter1(request.getAttribute("evalThema"))%>";
		var recsee_mobile = "<%=XssFilter1(request.getAttribute("recsee_mobile"))%>";
		var tabMode = "<%=XssFilter1(request.getAttribute("tabMode"))%>";
		var clientIpChk= "<%=XssFilter1(request.getAttribute("clientIpChk"))%>";
		var ipChkDup = "<%=XssFilter1(request.getAttribute("ipChkDup"))%>";
		var RecSeeVersion = "<%=XssFilter1(request.getAttribute("RecSeeVersion"))%>";
		var playerMode = "<%=XssFilter1(request.getAttribute("playerMode"))%>";
		var waveSurfer = "<%=XssFilter1(request.getAttribute("waveSurfer"))%>";
		var rowId = "<%=XssFilter1(request.getAttribute("rowId"))%>";
		var webPath = "<%=XssFilter1(request.getAttribute("contextPath"))%>";
		var userId = "<%=XssFilter1(request.getAttribute("userId"))%>";
		var	userInfoJson = "";
		try{
			userInfoJson = JSON.parse("${userInfoJson}".replace(/'/gi,'\"'));
		}catch(e){}
		var siteType = "recsee";
		var recseeResourcePath = commResourcePath + "/recsee";
		var siteResourcePath = commResourcePath + "/" + siteType;
		var userLevel = "<%=XssFilter1(getUserInfoDetail(request.getSession().getAttribute("USER_INFO"), "userLevel"))%>";
		var accessLevel = "<%=(MMenuAccessInfo)request.getAttribute("nowAccessInfo") != null ? XssFilter1(((MMenuAccessInfo)request.getAttribute("nowAccessInfo")).getAccessLevel()) : ""%>";
		var downloadFormat = "<%=XssFilter1(request.getSession().getAttribute("downloadFormat"))%>";
		var STTPlayer = "<%=XssFilter1(request.getSession().getAttribute("STTPlayer"))%>";
		var HTTP = "<%=XssFilter1(request.getAttribute("HTTP"))%>";
		var path = "<%=XssFilter1(request.getAttribute("path"))%>";
		var baseActivColor = "<%=XssFilter1(request.getSession().getAttribute("baseActivColor"))%>";
		var baseMainColor = "<%=XssFilter1(request.getSession().getAttribute("baseMainColor"))%>";
		var baseMainTabbarColor = "<%=XssFilter1(request.getSession().getAttribute("baseMainTabbarColor"))%>";
		var baseMainTxtColor = "<%=XssFilter1(request.getSession().getAttribute("baseMainTxtColor"))%>";
		var baseSubColor = "<%=XssFilter1(request.getSession().getAttribute("baseSubColor"))%>";
		var baseSubTxtColor = "<%=XssFilter1(request.getSession().getAttribute("baseSubTxtColor"))%>";
		var BestCallShareYN = "N";
		var listenPeriod = "24";
		var requestReason = "N";
		var updateGroupinfoYn = "N";
		var updateGroupinfoPeriod = "N";
		var telnoUse = "N";
		var unqLang = "ko";
	</script>

	<link rel="icon" type="image/png" href="<c:out value="${siteResourcePath}"/>/images/project/main/logo/favicon.png" />

	<%-- 공통 CSS 설정 --%>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/codebase/dhtmlx.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/skins/web/dhtmlx.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/jquery/jquery-ui/jquery-ui.min.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/jquery/jquery-treeview-master/jquery.treeview.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/select2-master/dist/css/select2.min.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/HoldOn/HoldOn.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/normalize.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/common.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/images.css"/>

	<%-- 공통 Javascript 설정 --%>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/codebase/dhtmlx.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/dhtmlx.i18n.Language.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-treeview-master/jquery.treeview.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/select2-master/dist/js/select2.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/HoldOn/HoldOn.min.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/common.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/layer_popup.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/ui_controller.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/languageConvert.js"></script>

	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/devExtreme/dx.all.js"></script>
	<%-- <script type="text/javascript" src="${compoResourcePath}/wavesurfer/wavesurfer.min.js"></script>
	<script type="text/javascript" src="${compoResourcePath}/wavesurfer/wavesurfer.cursor.min.js"></script> --%>

	<%-- 언어팩 인클루드 --%>
	<%@ include file="lang.jsp" %>
	
	<script>
		
$(document).ready(function() {
	 
	// 우클릭, F12 막깅~! admin 제외
	/* if (userId != "admin"){
		
		document.onkeydown = function (e){
			if (e.which === 123)
				return false;
		};
		
		document.orioncontextmemu = document.oncontextmenu;
       	document.oncontextmenu = function(){
       		return false;
       	}
	} */
	
	// grid xml관련 세션 처리
	var xmlError = false;
	dhx4.attachEvent("onLoadXMLError",function(){
		if(!xmlError){
			alert(lang.common.alert.loginAgain) /* 세션이 만료되어 로그인페이지로 이동합니다.\n재 로그인 후 이용해 주세요. */
			xmlError = true;
		}
		
		/* 22-02-08 조건문 추가(yoh) : 팝업창에서 xmlError 발생 관련 처리 */
		if(window.opener){ // 해당 개체의 opener가 있는지 확인
			if(window.opener.closed){ // opener가 close됐는지 확인
				top.location = top.location.protocol+"//"+top.location.host+"/"+top.contextPath+"/?secret=true"; // 현재 창을 로그인 화면으로 이동
			}else{ // opener가 열려 있는 경우, opener를 로그인 화면으로 이동하고 현재 창을 닫음
				window.opener.location.replace(contextPath + "/?secret=true");
				window.close();
			}
		}else{ // opener가 없는 경우
			top.location = top.location.protocol+"//"+top.location.host+"/"+top.contextPath; // 기존 코드: 현재 창을 로그인 화면으로 이동
		}
	});
});	
	</script>

