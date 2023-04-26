package com.furence.recsee.common.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.furence.recsee.common.model.LoginVO;

public class SessionManager implements KeyDefine {

	/**
	 * 지정 리퀘스트로부터 Session을 취득한다.
	 * 지정 리퀘스트가null일 경우, null을 반환한다.
	 * 지정 리퀘스트에 세션이 없을 경우, 새로운 세션을 작성한다.
	 *
	 * @param request 리퀘스트
	 * @return Session 세션
	 */
	public static HttpSession getSession(HttpServletRequest request) {
		return getSession(request, true);
	}

	/**
	 * 지정 리퀘스트로부터 Session을 취득한다.
	 * 지정 리퀘스트가null일 경우, null을 반환한다.
	 * 지정 리퀘스트에 세션이 없을 경우, create가 true이면 새로운 세션을 작성하고,
	 * false일 경우 null을 반환한다.
	 *
	 * @param request 리퀘스트
	 * @param create 리퀘스트에 세션이 없을 경우, 새로운 세션 생성 유무(true:생성함(기본값), false:생성안함)
	 * @return Session 세션
	 */
	public static HttpSession getSession(HttpServletRequest request, boolean create) {
		if (request != null) {
			return request.getSession(create);
		}
		return null;
	}

	/**
	 * 지정 리퀘스트의 세션에 Attribute를 설정한다.
	 * 세션에 저장할 키가 null일 경우, 저장하지 않는다.
	 *
	 * @param request 리퀘스트
	 * @param sesKey 세션에 저장할 키
	 * @param sesVal 세션에 저장할 값
	 */
	public static void setAttribute(HttpServletRequest request, String sesKey, Object sesVal) {
		HttpSession session = getSession(request);
		if (session != null && sesKey != null) {
			session.setAttribute(sesKey, sesVal);
		}
	}

	/**
	 * 지정 리퀘스트의 세션에 String형으로 저장된  Attribute를 취득한다.
	 * 세션에 저장할 키가 null일 경우, null을 반환한다.
	 *
	 * @param request 리퀘스트
	 * @param sesKey 세션키
	 */
	public static String getStringAttr(HttpServletRequest request, String sesKey) {
		HttpSession session = getSession(request);
		if (session != null && sesKey != null) {
			return (String) session.getAttribute(sesKey);
		}
		return null;
	}

	/**
	 * 지정 리퀘스트의 세션에 저장된 모든 Attribute값을 삭제한다
	 *
	 * @param request 리퀘스트
	 */
	public static void sessionClear(HttpServletRequest request) {
		HttpSession session = getSession(request);
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * 로그인 한 유저정보를 세션으로부터 취득한다.
	 * @param request 리퀘스트
	 * @return 유저정보
	 */
	public static LoginVO getUserInfo(HttpServletRequest request) {
		return getUserInfo(request, SES_KEY_USER_INFO);
	}

	/**
	 * 로그인 한 유저정보를 세션으로부터 취득한다.
	 * @param request 리퀘스트
	 * @param sesKey 세션키
	 * @return 유저정보
	 */
	public static LoginVO getUserInfo(HttpServletRequest request, String sesKey) {

		LoginVO userInfo = null;
		if (request != null) {
			userInfo = (LoginVO)request.getSession().getAttribute(sesKey);
		}

		return userInfo;
	}


	/**
	 * 시스템 설정정보를 세션으로부터 취득한다.
	 * @param request 리퀘스트
	 * @param sesKey 세션키
	 * @return 시스템 설정정보
	 */
	public static Map<String, String> getSysConfig(HttpServletRequest request) {
		return getSysConfig(request, SES_KEY_SYS_CONFIG);
	}

	/**
	 * 시스템 설정정보를 세션으로부터 취득한다.
	 * @param request 리퀘스트
	 * @param sesKey 세션키
	 * @return 시스템 설정정보
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getSysConfig(HttpServletRequest request, String sesKey) {

		Map<String, String> sysConfig = null;
		if (request != null) {
			sysConfig = (Map<String, String>)request.getSession().getAttribute(sesKey);
		}

		return sysConfig;
	}


}
