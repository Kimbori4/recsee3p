<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<%@include file="encrypt.jsp" %>
<%
/*******************************************************
**  업무서버에서 선언해야 할 변수- Start
**  필수 전달 Parameter 에 넘겨줘야 할 변수 선언 부
*******************************************************/
	String AP_URL = "http://업무서버 경로 및 포트/main.jsp";  // 암호화된 SSO ID 를 받아 복호화 실행하는 URL
/*******************************************************
**  업무서버에서 선언해야 할 변수- End
*******************************************************/
	String SSOUserID 	= request.getParameter("userID") ;
	String ENT_CODE 	= request.getParameter("ENT_CODE") ;
	String UNIT_CODE 	= request.getParameter("UNIT_CODE") ;
	String rand = request.getParameter("rand") ;
	System.out.println("[SSO_PROXY_INFO][" + request.getRequestURL() + "] request SSOUserID==>" + SSOUserID) ;
	String xKeyValue = "";
	xKeyValue = request.getRemoteAddr();
	if(xKeyValue == null || xKeyValue.equals("")){
		System.out.println("[SSO_PROXY_ERROR][" + request.getRequestURL() + "] Header에서 client IP Address 를 얻어올 수 없습니다.");
		out.println("[SSO_PROXY_ERROR][" + request.getRequestURL() + "] Header에서 client IP Address 를 얻어올 수 없습니다.");
	}
	
	try {
		String encryptionKey = getEncryptionKey(xKeyValue, rand);
		SSOUserID = decryptAES(SSOUserID, encryptionKey);
	} catch (Exception ex) {
		System.out.println("[SSO_PROXY_ERROR][" + request.getRequestURL() + "] 사용자 ID 복호화에 실패했습니다.");
		SSOUserID = null;
	}

	System.out.println("[SSO_PROXY_INFO][" + request.getRequestURL() + "] DEC_USERID==>" + SSOUserID) ;
	if ( SSOUserID == null || SSOUserID.equals("") ) {
		System.out.println("[SSO_PROXY_ERROR][" + request.getRequestURL() + "] 복호화된 SSO ID 를 얻어올 수 없습니다.");
		out.println("[SSO_PROXY_ERROR][" + request.getRequestURL() + "] 복호화된 SSO ID 를 얻어올 수 없습니다.");
	} else {
		// 복호화한 SSOID 를 업무 Session에 기록한다. 		
    	session.setAttribute("SSOID", SSOUserID);

/*******************************************************
**  업무서버에서 선언해야 할 변수- Start
**  추가적으로 업무서버에서 사용할 세션 선언 부
*******************************************************/
		session.setAttribute("ENT_CODE", ENT_CODE);
		session.setAttribute("UNIT_CODE", UNIT_CODE);
/*******************************************************
**  업무서버에서 선언해야 할 변수- End
*******************************************************/		
		// session에 SSOID를 기록 후 업무 세션을 시작하기 위한 준비를 한다.
		response.sendRedirect(AP_URL);
	}
%>