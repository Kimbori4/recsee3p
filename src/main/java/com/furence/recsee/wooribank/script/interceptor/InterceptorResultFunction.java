package com.furence.recsee.wooribank.script.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface InterceptorResultFunction {
	boolean resultHandler(HttpServletRequest request, 
			HttpServletResponse response,
			Object handler) throws Exception;
}
