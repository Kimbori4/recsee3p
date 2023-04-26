<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="locale" value="${pageContext.response.locale}" scope="session" />
<!DOCTYPE html>
<html lang="<c:out value="${locale}"/>">
<head>
<title>RecSee 3.0</title>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="resourcePath" value="${contextPath}/resources"/>
<c:set var="commResourcePath" value="${resourcePath}/common"/>
<c:set var="compoResourcePath" value="${resourcePath}/component"/>
<script>
	var contextPath = '<c:out value="${contextPath}"/>';
	var resourcePath = '<c:out value="${resourcePath}"/>';
	var commResourcePath = '<c:out value="${commResourcePath}"/>';
	var compoResourcePath = '<c:out value="${compoResourcePath}"/>';
</script>
<script src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
<script src="<c:out value="${compoResourcePath}"/>/jquery/jquery-ui.js"></script>
	<%! 
		public String XssFilter(String value) {
			if (value == null) {
				return value;
			}
			value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		    value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		    value = value.replaceAll("'", "&#39;");
		    value = value.replaceAll("eval\\((.*)\\)", "");
		    value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		    value = value.replaceAll("script", "");
		    return value;
		}
	%>
<script>
	top.onMessage("<%=(request.getParameter("msg")!=null? XssFilter(request.getParameter("msg")).replaceAll("<","&lt;").replaceAll(">","&gt;") : "")%>");
</script>
<body>
</body>
</html>