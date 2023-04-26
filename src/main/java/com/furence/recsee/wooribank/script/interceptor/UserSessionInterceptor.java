package com.furence.recsee.wooribank.script.interceptor;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.wooribank.script.param.response.AJaxResult;
import com.furence.recsee.wooribank.script.param.response.ResultCode;


public class UserSessionInterceptor extends InterceptorValidator {

	private	final String[] apiPrefixs = {"/wooribank/script/api"};
	
	@Override
	protected InterceptorResultFunction handleInvalidRequest(
			HttpServletRequest request, 
			HttpServletResponse response,
			Object handler) {
		
		return Optional.ofNullable(checkSession(request))
				.orElseGet(()->{ return checkValidUri(request); });				
	}
	
	/**
	 * 세션 활성화 체크
	 * @param request
	 * @return
	 */
	private InterceptorResultFunction checkSession(HttpServletRequest request) {
		return SessionManager.getUserInfo(request) == null 
				? null 
				: (req, res, handler)->{return true; }; 
	}
	
	/**
	 * uri 검사 후 결과처리 분기
	 * @param request
	 * @return
	 */
	private InterceptorResultFunction checkValidUri(HttpServletRequest request) {
		
		String context = request.getContextPath();
		String requestUri = request.getRequestURI();
		
		return Arrays.asList(apiPrefixs).stream()
				.allMatch(s-> requestUri.startsWith(context+s))
				? loginApiResultFunction() 
				: loginPageFunction();
	}
	
	/**
	 * API 에러 결과 리턴
	 * @return
	 */
	private InterceptorResultFunction loginApiResultFunction() {
		
		return (req, res, handler)->{ 
			AJaxResult resultObj = new AJaxResult.Builder(ResultCode.NO_LGOING_USER).build();
			
			ObjectMapper objmapper = new ObjectMapper();
			String jsonStr = objmapper.writeValueAsString(resultObj);	
			
			res.setStatus(200);
			res.setContentType("application/json");
			res.getWriter().write(jsonStr); 
			res.getWriter().flush();
			return false;
		}; 
	}
	
	/**
	 * 로그인 페이지 리턴 
	 * @return
	 */
	private InterceptorResultFunction loginPageFunction() {
		return (req, res, handler)->{ 
			res.sendRedirect(req.getContextPath() + "/login/init");
			return true; 
		}; 
	}
}
