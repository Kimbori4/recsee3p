<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title></title>
		<%@ include file="../common/include/commonVar.jsp" %>
</head>
<body>

<script>
$(function(){
	
	alert(lang.login.login.alert.alreadyLogin);	/* 해당 아이디는 이미 로그인 되어 있습니다.\n 관리자에게 문의 하십시오. */
	top.duple = null;
	window.open('about:blank','_self').self.close();
	
	//disconnect();
	//기존 연결 제거
	function disconnect(){
		var dataStr = {};
		// DB 조회 ajax 처리
			$.ajax({
			url:contextPath+"/disconnect.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){

				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y" && jRes.result == "DISSCONNECT") {
					login_proc()
				}
			}
		});
	}
	

	function login_proc() {
		window.location.href = contextPath+"/login/ssoLogin.do";
	}
})
</script>
</body>
</html>