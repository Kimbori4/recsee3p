package com.furence.recsee.wooribank.script.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


abstract class InterceptorValidator implements HandlerInterceptor{
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		return handleInvalidRequest(request, response, handler)
				.resultHandler(request, response, handler);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	
	protected abstract InterceptorResultFunction handleInvalidRequest(HttpServletRequest request, HttpServletResponse response, Object handler);
	
	
}
