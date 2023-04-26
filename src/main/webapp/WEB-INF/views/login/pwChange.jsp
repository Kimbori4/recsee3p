<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="../common/include/loginVar.jsp" %>

    <%-- css page --%>
    <link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/page/change_pw.css"/>

    <%-- js page --%>
    <script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/page/changePw.js"></script>
    
    <script>
        window.onload = function(){
            ui_controller();
        }
        var contextPath = '<c:out value="${contextPath}"/>';
        var userId = '<c:out value="${userId}"/>';
        var type = <c:out value="${type}"/>;
    	// 파라미터 암호화
        var RSAModulus = '<c:out value="${sessionScope.RSAModulus}"/>';
        var RSAExponent = '<c:out value="${sessionScope.RSAExponent}"/>';
        
    </script>
 
</head>
<body onload="changePwLoad()">
    <div class="pwPolicyWrap">
        <div class="pw_policy_tit">
            <div class="site_logo"></div>
            <p><%-- <spring:message code="login.login.p.info.policy"/> --%></p>
        </div>
        <div class="pw_policy_txt">
            <p>사용자의 개인정보 보호를 위해 <span class="ui_txt_emphasis">비밀번호를 변경</span>해 주세요. 비밀번호 변경 후에는 재 로그인이 필요합니다.</p>
        </div>
        <div class="pw_policy_change">
            <div class="pw_policy_form">
                <form>
                    <label>현재 비밀번호</label>
                    <input id="nowPw" type="password"/>
                    <label>새로운 비밀번호</label>
                    <input id="newPw" type="password"/>
                    <p class="">비밀번호는 8~20자 영문 대/소문자, 숫자, 특수문자를 혼합해서 사용 하셔야 합니다.</p>            
                    <label>비밀번호 확인</label>
                    <input id="newPwChk" type="password"/>
                    <p class="">재확인을 위해서 입력하신 비밀번호를 다시 한번 입력해 주세요.</p>
                </form>
            </div>
            <div class="pw_policy_info">
                <p><span class="ui_txt_emphasis">쉬운 비밀번호</span>나 자주 쓰는 사이트의 비밀번호가 같을 경우, 도용되기 쉬우므로 <span class="ui_txt_emphasis">주기적으로 변경하셔서 사용</span>하는 것이 좋습니다.</p>
                <p>아이디와 주민등록번호, 생일, 전화번호 등 개인정보와 관련된 숫자, 연속된 숫자, 반복된 문자 등 다른사람이 쉽게 알아 낼 수 있는 비밀번호는 개인정보 유출 위험이 높으므로 사용을 자제해 주시기 바랍니다.</p>
            </div>
        </div>
        <div class="pw_policy_button">
            <button id="okBtn" class="button_pw_change">확인</button>
            <button id="afterBtn" class="button_next">다음에 변경하기</button>
        </div>
    </div>
    
</body>
</html>