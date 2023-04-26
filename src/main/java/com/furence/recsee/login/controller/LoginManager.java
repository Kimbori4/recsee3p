package com.furence.recsee.login.controller;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.furence.recsee.common.model.Log;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.StringUtil;

/*
* session이 끊어졌을때를 처리하기 위해 사용
* static메소드에서는 static만사용 하므로static으로 선언한다.
*/
@Component
public class LoginManager implements HttpSessionBindingListener {
	
	private static LogService logService;
	
	@Autowired
	public void setLogService(LogService logService) {
	    this.logService = logService;
	}

	private static LoginManager loginManager = null;

	// 로그인한 접속자를 담기위한 해시테이블
	private static ConcurrentHashMap<String, HttpSession> loginUsers = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, HttpSession> loginIps = new ConcurrentHashMap<>();


	/*
	 * 싱글톤 패턴 사용
	 */
	public static synchronized LoginManager getInstance() {
		if (loginManager == null) {
			loginManager = new LoginManager();
		}
		return loginManager;
	}

	/*
	 * 이 메소드는 세션이 연결되을때 호출된다.(session.setAttribute("login", this)) Hashtable에
	 * 세션과 접속자 아이디를 저장한다.
	 */
	public void valueBound(HttpSessionBindingEvent event) {
		
		// session값을 put한다.
		if(event.getName().indexOf(".")!=-1) {
			loginIps.put(event.getName(),event.getSession());
		}else {
			loginUsers.put(event.getName(),event.getSession());
		}
	}

	/*
	 * 이 메소드는 세션이 끊겼을때 호출된다.(invalidate) Hashtable에 저장된 로그인한 정보를 제거해 준다.
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {

		String serverIp = new RecSeeUtil().getLocalServerIp();
		Log log = new Log();
		
		
		Iterator<String> keySetIteratorId = loginUsers.keySet().iterator();
		while (keySetIteratorId.hasNext()) {
		    String key = keySetIteratorId.next();
		    HttpSession session = (HttpSession)loginUsers.get(key);
		    if(event.getSession().getId().equals(session.getId())) {
				log.setrLogUserId(key);
		    	loginUsers.remove(key);
		    }
		}

		Iterator<String> keySetIteratorIp = loginIps.keySet().iterator();
		while (keySetIteratorIp.hasNext()) {
		    String key = keySetIteratorIp.next();
		    HttpSession session = (HttpSession)loginIps.get(key);
		    if(event.getSession().getId().equals(session.getId())) {
				log.setrLogIp(key);
		    	loginIps.remove(key);
		    }
		}
		// 세션해제 로그기록
		log.setrLogServerIp(serverIp);
		log.setrLogCode("CONNECT");
		log.setrLogDetailCode("SESSION");
		if(!StringUtil.isNull(log.getrLogUserId(), true)) {
			logService.insertLog(log);
		}
	}

	/*
	 * 입력받은 아이디를 해시테이블에서 삭제.
	 * @param userID 사용자 아이디
	 * @return void
	 */
	public void removeSession(String userId) {
//		for( Object key : loginUsers.keySet() ){
//		    HttpSession session = (HttpSession)loginUsers.get(key);
//		    if(userId.equals(key)) {
//				session.invalidate();
//		    }
//		}
		
		Enumeration e = loginUsers.keys();
		
		while (e.hasMoreElements()) {
			String target = (String)e.nextElement();
			
			if (userId.equals(target)) {
				HttpSession session = (HttpSession)loginUsers.get(target);
				try {
					session.invalidate();	
				}catch (Exception e2) {
					loginUsers.remove(userId);
				}
					
					
			}
		}
	}

	/*
	 * 입력받은 아이디를 해시테이블에서 삭제.
	 * @param userID 사용자 아이디
	 * @return void
	 */
	public void removeIpSession(String userIp) {
//		for( Object key : loginIps.keySet() ){
//		    HttpSession session = (HttpSession)loginIps.get(key);
//		    if(userIp.equals(key)) {
//				session.invalidate();
//		    }
//		}
		Enumeration e = loginIps.keys();
		
		while (e.hasMoreElements()) {
			String target = (String)e.nextElement();
			
			if (userIp.equals(target)) {
				HttpSession session = (HttpSession)loginIps.get(target);
				try {
					session.invalidate();	
				}catch (Exception e2) {
					loginIps.remove(userIp);
				}
			}
		}
	}
	
	/*
	 * 입력받은 아이디를 해시테이블에서 삭제.
	 * @param userID 사용자 아이디
	 * @return void
	 */
	public void removeMapIdData(String userId) {
		loginUsers.remove(userId);
	}
	
	/*
	 * 입력받은 IP를 해시테이블에서 삭제.
	 * @param userIP 사용자 IP
	 * @return void
	 */
	public void removeMapIpData(String userIp) {
		loginIps.remove(userIp);
	}
	
	/*
	 * 사용자가 입력한 ID, PW가 맞는지 확인하는 메소드
	 * 
	 * @param userID 사용자 아이디
	 * 
	 * @param userPW 사용자 패스워드
	 * 
	 * @return boolean ID/PW가 일치하는 지 여부
	 */
	public boolean isValid(String userId, String userPw) {

		/*
		 * 이부분에 Database 로직이 들어간다.
		 */
		return true;
	}

	/*
	 * 해당 아이디의 동시 사용을 막기위해서 이미 사용중인 아이디인지를 확인한다.
	 * 
	 * @param userID 사용자 아이디
	 * 
	 * @return boolean 이미 사용 중인 경우 true, 사용중이 아니면 false
	 */
	
	// @david 		다른 아이피에서 로그인 햇을때 비교
	public boolean isUsing(String userID) {
		boolean result = false;
		if(loginUsers.containsKey(userID)) {
			result = true;
		}
		if(loginIps.containsKey(userID)) {
			result = true;
		}
		return result;
	}

	/*
	 * 로그인을 완료한 사용자의 아이디를 세션에 저장하는 메소드
	 * 
	 * @param session 세션 객체
	 * 
	 * @param userID 사용자 아이디
	 */
	public void setSession(HttpSession session, String userId) {
		
		//System.out.println(userId);
		// 이순간에 Session Binding이벤트가 일어나는 시점
		// name값으로 userId, value값으로 자기자신(HttpSessionBindingListener를 구현하는
		// Object)
		session.setAttribute(userId, this);// login에 자기자신을 집어넣는다.
	}

	/*
	 * 입력받은 세션Object로 아이디를 리턴한다.
	 * 
	 * @param session : 접속한 사용자의 session Object
	 * 
	 * @return String : 접속자 아이디
	 */
//	public String getUserID(HttpSession session) {
//		return (String) loginUsers.get(session);
//	}

	/*
	 * 현재 접속한 총 사용자 수
	 * 
	 * @return int 현재 접속자 수
	 */
	public int getUserCount() {
		return loginUsers.size();
	}

	/*
	 * 현재 접속중인 모든 사용자 아이디를 출력
	 * 
	 * @return void
	 */
	public void printloginUsers() {
//		Enumeration e = loginUsers.keySet();
//		HttpSession session = null;
////		System.out.println("===========================================");
//		int i = 0;
//		while (e.hasMoreElements()) {
//			session = (HttpSession) e.nextElement();
////			System.out.println((++i) + ". 접속자 : " + loginUsers.get(session));
//		}
//		System.out.println("===========================================");
	}

	/*
	 * 현재 접속중인 모든 사용자리스트를 리턴
	 * 
	 * @return list
	 */
	public Collection getUsers() {
		Collection collection = loginUsers.values();
		return collection;
	}
}

/*
 * 거의 대부분의 웹사이트를 보면 브라우저를 열고 로그인후 또다시 다른브라우저를 열고 로그인을 하면 로그인이 되는것을 확인하실수 있습니다.
 * 이는 즉... 여러곳에서 동일한 아이디로 접속을 할수있다는 예입 니다. 이와 반대로 메신저같은경우는 이미 로그인이 되어있을시 다른곳에서
 * 로그인을 하면 접속을 끊을지를 물어보는 기능도 보셨을 겁니다. 이를 웹에 서 구현하여 보았습니다. 본 예제소스는 우리가 구현하려고 예제에서
 * 가장 핵심적인 부분을 맡고있 는 소스입니다. 여기서 HttpSessionBindingListener는 서블릿 컨테이너에서 세션이 끊길때
 * (valueUnBound)와 이를 구현하는 오브젝트가 해당 세션에 setAttribute될 때(valueBound) 호출합니다. 굳이 이를
 * 구현하는이유는 세션이 끊기는 시 점을 정확히 잡아내기 위함입니다. 사용자가 로그아웃버튼을 누를시도 있지 만 세션이 타임아웃되는경우도 세션이
 * 끊겨야 하기 때문입니다. 그리고 브라우저의 닫기버튼, Alt+F4, Ctrl+E버튼 을 누를시 이벤트를 잡는방법도 차근차근 알아보도록
 * 합시다.
 */