package com.furence.recsee.common.util;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

public class CookieSetToLang {
	
	public void langSetFunc(HttpServletRequest request, HttpServletResponse response) {
		Locale setLocale = null;
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		Cookie[] cookie = request.getCookies();
		for(int c=0; c<cookie.length; c++) {
			if(cookie[c].getName().contains("unqLang")) {
				setLocale = new Locale(cookie[c].getValue());
				localeResolver.setLocale(request,response,setLocale);
				java.util.Locale.setDefault(setLocale);
			}			
		}
	}	
}
