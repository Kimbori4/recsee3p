package com.furence.recsee.wooribank.script.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.SessionManager;

public class recseePlayerInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		MMenuAccessInfo menuAccess = new MMenuAccessInfo();
		menuAccess.setLevelCode("E1001");
		menuAccess.setDisplayLevel(100);

		List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectMenuAccessInfo(menuAccess);
		int menuAccessListTotal = menuAccessList.size();
		
		if(menuAccessListTotal > 3) {
			SessionManager.setAttribute(request, "AccessInfo", menuAccessList);
		}
		return super.preHandle(request, response, handler);
	}

	
}
