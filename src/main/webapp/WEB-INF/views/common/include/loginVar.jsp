<%@include file="./commonVar.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<%-- 공통 변수 설정 --%>
	<c:set var="loginResourcePath" value="${resourcePath}/login"/>

	<script>
	var loginResourcePath = "${loginResourcePath}";
	</script>

	<%-- RSA 암호 모듈 --%>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/jsbn.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/rsa.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/prng4.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/rsa/rng.js"></script>