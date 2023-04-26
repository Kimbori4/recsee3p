<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-ui/jquery-ui.min.js"></script>
</head>
<body>
	
</body>
<script>
	var readYn = "<%=request.getAttribute("readYn")%>";
	if (readYn == "N") {
		alert("페이지 접근 권한이 없습니다. \n관리자에게 문의 바랍니다.");	/* 페이지 접근 권한이 없습니다. \n관리자에게 문의 바랍니다. */
		top.window.open('about:blank','_self').self.close();
	} else {
		alert(lang.login.login.alert.noLoginRight);	/* 로그인 권한이 없습니다. \n관리자에게 문의 바랍니다. */
		window.open('about:blank','_self').self.close();
	}
</script>