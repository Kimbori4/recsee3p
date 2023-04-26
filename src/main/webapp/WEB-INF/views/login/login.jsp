<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<%
response.setHeader("Pragma", "no-cache"); //HTTP 1.0
response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
response.setDateHeader("Expires", 0L); // Do not cache in proxy server
%>  
<!DOCTYPE html>
<html>
<head>
	<%@include file="../common/include/loginVar.jsp" %>
    <%-- css page --%>
    <link rel="stylesheet" type="text/css" href="<c:out value="${siteResourcePath}"/>/css/page/login.css"/>

    <%-- js page --%>
    <script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/login.js"></script>

	<%-- css, js 세팅 및 값 조작해주는 함수 실행 --%>
	<%-- <script type="text/javascript" src="${siteResourcePath }/js/include_component.js"></script> --%>
	
    <script>
        window.onload = function(){
            ui_controller();
            // 프레임 에서 로그아웃 되었을 경우
            // 상위 프레임도 함께 로그아웃 처리
            var d = new String(window.top.location.pathname);
            if ((d.localeCompare(contextPath) != 0 ) && (d.localeCompare(contextPath+"/") != 0 ) && (d.localeCompare(contextPath+"/login/init.do") != 0)){
            	top.location = top.location.protocol+"//"+top.location.host+"/"+top.contextPath;
            } 
        }        
    </script>
    <style>
    body, html {
	    height: 100%;
	}
    </style>

</head>
<body>
	<div class="loginWrap">

		<div class="background"></div>


		<div class="logo_main" >투자상품 녹취시스템</div>

		<div class="login_box">
			<fieldset class="login_field">
				<form action="#" name="form1" id="form-id">
					<div class="input_form">
						<label class="icon_user"></label>
						<input type="text" id="userId" name="userId" onkeypress="if(event.keyCode==13){$('#confirmBtn').trigger('click');}" placeholder="<spring:message code="login.login.placeholder.id" />"/>
					</div>
					<div class="input_form">
						<label class="icon_key"></label>
						<input type="password" id="userPw" name="userPw" onkeypress="if(event.keyCode==13){$('#confirmBtn').trigger('click');}" placeholder="<spring:message code="login.login.placeholder.password" />">
					</div>
					 <div class="input_form" style="display:none">
						<label class="icon_lang"></label>
						<select id="unqLang" name="unqLang">
							<option value="ko" selected>한국어</option>
						</select>
					</div> 
					<div class="check_form">
						<input type="checkbox" id="userIdCheck" name="userIdCheck"/>
						<label id="lblUserIdCheck" for="userIdCheck"><spring:message code="login.login.title.rememberMe" /></label>
					</div>
				</form>
			</fieldset>
			<div class="btnWrap" id="confirmBtn">
				<button class="btn_login"><spring:message code="login.login.title.login" /></button>
			</div>
            <p class="alert_txt" style="display: none;"></p>
		</div>

        <div class="login_footer">
            <p>RecSee3.0 Plus</p>
        </div>

	</div>

	<form id="main" name="main" action="${pageContext.request.contextPath}/main" method="post" style="display:none">
		<input type="hidden" name="path"/>
	</form>

	<input type="hidden" id="RSAModulus" value="<c:out value="${sessionScope.RSAModulus}"/>" />
	<input type="hidden" id="RSAExponent" value="<c:out value="${sessionScope.RSAExponent}"/>" />
<%--
	<input type="hidden" id="loginloginalertfaillogin" value='<spring:message code="login.login.alert.fail.login" />'>
	<input type="hidden" id="pageTitle" value='<spring:message code="login.login.page.title" />'>
	<input type="hidden" id="loginloginalertnotinputcode" value='<spring:message code="login.login.alert.not.input.code" />'>
	<input type="hidden" id="loginloginalertnotinputid" value='<spring:message code="login.login.alert.not.input.id" />'>
	<input type="hidden" id="loginloginalertnotinputpw" value='<spring:message code="login.login.alert.not.input.pw" />'>
	<input type="hidden" id="loginloginalertnotinputext" value='<spring:message code="login.login.alert.not.input.ext" />'>
	<input type="hidden" id="loginloginalertnotinputlang" value='<spring:message code="login.login.alert.not.input.lang" />'>
	<input type="hidden" id="loginloginalertfaillogin" value='<spring:message code="login.login.alert.fail.login" />'>
 --%>

</body>
</html>