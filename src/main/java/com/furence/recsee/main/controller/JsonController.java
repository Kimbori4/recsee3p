//package com.furence.recsee.main.controller;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.furence.recsee.common.model.LoginVO;
//import com.furence.recsee.common.model.MMenuAccessInfo;
//import com.furence.recsee.common.service.EtcConfigInfoService;
//import com.furence.recsee.common.service.LogInfoService;
//import com.furence.recsee.common.service.MenuAccessInfoService;
//import com.furence.recsee.common.service.SystemInfoService;
//import com.furence.recsee.common.util.SessionManager;
//import com.furence.recsee.main.model.SearchListInfo;
//import com.furence.recsee.main.service.SearchListInfoService;
//
//@Controller
//public class JsonController {
//
//	@Autowired
//	private MenuAccessInfoService menuAccessInfoService;
//
//	@Autowired
//	private SearchListInfoService searchListInfoService;
//
//	@Autowired
//	private EtcConfigInfoService etcConfigInfoService;
//
//	@Autowired
//	private SystemInfoService systemInfoService;
//
//	@Autowired
//	private RUserInfoService ruserInfoService;
//
//	@Autowired
//	private LogInfoService logInfoService;
//
//	@Autowired
//	private MessageSource messageSource;
//
//	/**
//	 * public Properties conf = null;
//	 * public Properties systemCheckerConf = null;
//	 * public static String systemCheckerUCURL = null;
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value="/menu/left_menu.json", method=RequestMethod.GET)
//	public @ResponseBody void leftMenuCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		String currentLeftMenu = request.getParameter("leftMenu");
//
//		//String subMenu = "";
//		//String leftMenu = "";
//
//		JSONArray subMenu = new JSONArray();
//
//		if(userInfo != null) {
//
//			MMenuAccessInfo menuAccessInfoChk = new MMenuAccessInfo();
//
//			menuAccessInfoChk.setLevelCode(userInfo.getUserLevel());
//			menuAccessInfoChk.setProgramTop(currentLeftMenu.toUpperCase());
//
//			List<MMenuAccessInfo> menuAccessList = menuAccessInfoService.selectSubMenuAccessInfo(menuAccessInfoChk);
//
//			int menuAccessListTotal = menuAccessList.size();
//
//			if (menuAccessListTotal > 0) {
//
//				for(int seqMenu = 0;seqMenu < menuAccessListTotal;seqMenu++) {
//
//					MMenuAccessInfo menuItem = menuAccessList.get(seqMenu);
//
//					if (menuItem.getDisplayLevel() == 0) continue;
//
//					if (menuItem.getDisplayLevel() == 1) {
//						/**
//						 * display_level=1인 경우가 발생하면, 무조건 sub menu가 바뀐것으로 간주
//						 */
//						JSONObject subItem = new JSONObject();
//						subItem.put("sub_name", menuItem.getProgramSrc());
//						subItem.put("sub_text", messageSource.getMessage("menu.top."+menuItem.getProgramSrc(), null, Locale.getDefault()));
//						subItem.put("link", menuItem.getProgramPath());
//						subMenu.add(subItem);
//					}
//
//					if (menuItem.getDisplayLevel() == 2) {
//						JSONObject leftItem = new JSONObject();
//						leftItem.put("name", menuItem.getProgramSrc());
//						leftItem.put("text", messageSource.getMessage("menu.top."+menuItem.getProgramSrc(), null, Locale.getDefault()));
//						leftItem.put("link", menuItem.getProgramPath());
//
//						JSONObject temp = (JSONObject) subMenu.get((subMenu.size() - 1));
//						if(temp.get("left_menu") == null) {
//							temp.put("left_menu", new JSONArray());
//						}
//
//						JSONArray tempArray = (JSONArray) temp.get("left_menu");
//						tempArray.add(leftItem);
//					}
//				}
//			}
//
//		}
//		System.out.println("subMenu : " + subMenu.toString());
//
//		response.setContentType("application/json");
//		response.getWriter().write(subMenu.toString());
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/myinfo/account_proc.do", produces="text/html;charset=UTF-8")
//	public @ResponseBody void account_proc(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//
//		JSONObject ruserInfoJson = new JSONObject();
//
//		if(userInfo != null) {
//			RUserInfo ruserInfo = new RUserInfo();
//			ruserInfo.setUserId(userInfo.getUserId());
//
//			if(request.getParameter("proc") != null && request.getParameter("proc").equals("info")) {
//				List<RUserInfo> ruserInfoResult = ruserInfoService.selectRUserInfo(ruserInfo);
//
//				int ruserInfoTotal = ruserInfoResult.size();
//
//				if(ruserInfoTotal > 0) {
//					RUserInfo ruserItem = ruserInfoResult.get(0);
//
//					ruserInfoJson.put("result",  (Boolean) true);
//
//					ruserInfoJson.put("userId",  ruserItem.getUserId());
//					ruserInfoJson.put("userName", ruserItem.getUserName());
//					if(ruserItem.getExtNo() != null) {
//						ruserInfoJson.put("extNum", ruserItem.getExtNo());
//					} else {
//						ruserInfoJson.put("extNum", "");
//					}
//
//					ruserInfoJson.put("authy", ruserItem.getUserLevel());
//					if(ruserItem.getEmpId() != null) {
//						ruserInfoJson.put("empId", ruserItem.getEmpId());
//					} else {
//						ruserInfoJson.put("empId", "");
//					}
//					if(ruserItem.getUserEmail() != null) {
//						ruserInfoJson.put("email", ruserItem.getUserEmail());
//					} else {
//						ruserInfoJson.put("email", "");
//					}
//					if(ruserItem.getCtiId() != null) {
//						ruserInfoJson.put("ctiId", ruserItem.getCtiId());
//					} else {
//						ruserInfoJson.put("ctiId", "");
//					}
//
//				} else {
//					ruserInfoJson.put("result", (Boolean) false);
//				}
//			} else if (request.getParameter("proc") != null && request.getParameter("proc").equals("modify")) {
//				ruserInfo.setUserName(request.getParameter("userName"));
//				if(request.getParameter("password") != null && !request.getParameter("password").trim().equals(""))
//					ruserInfo.setPassword(request.getParameter("password"));
//				else
//					ruserInfo.setPassword(null);
//				if(request.getParameter("empId") != null) {
//					if(!request.getParameter("empId").trim().equals(""))
//						ruserInfo.setEmpId(request.getParameter("empId"));
//					else
//						ruserInfo.setEmpId("");
//				} else
//					ruserInfo.setEmpId(null);
//				if(request.getParameter("email") != null) {
//					if(!request.getParameter("email").trim().equals(""))
//						ruserInfo.setUserEmail(request.getParameter("email"));
//					else
//						ruserInfo.setUserEmail("");
//				} else
//					ruserInfo.setUserEmail(null);
//				if(request.getParameter("ctiId") != null) {
//					if(!request.getParameter("ctiId").trim().equals(""))
//						ruserInfo.setCtiId(request.getParameter("ctiId"));
//					else
//						ruserInfo.setCtiId("");
//				} else
//					ruserInfo.setCtiId(null);
//
//				Integer ruserUpdate = ruserInfoService.updateRUserInfo(ruserInfo);
//				if(ruserUpdate > 0) {
//					ruserInfoJson.put("result", (Boolean) true);
//
//					logInfoService.writeLog(request, "Account - Update succecss", ruserInfo.toString(), userInfo.getUserId());
//				} else {
//					ruserInfoJson.put("result", (Boolean) false);
//
//					logInfoService.writeLog(request, "Account - Update fail", ruserInfo.toString(), userInfo.getUserId());
//				}
//			}
//		}
//		response.setContentType("application/json");
//		response.getWriter().write(ruserInfoJson.toString());
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value="/search/player_rec_info.json", method={RequestMethod.GET,RequestMethod.POST})
//	public @ResponseBody void player_rec_info(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		JSONObject recInfoResult = new JSONObject();
//
//		if(userInfo != null && request.getParameter("date") != null &&
//		request.getParameter("time") != null && request.getParameter("fileName") != null) {
//
//			SearchListInfo searchListInfo = new SearchListInfo();
//
//			searchListInfo.setsDate(request.getParameter("date"));
//			searchListInfo.seteDate(request.getParameter("date"));
//			searchListInfo.setsTime(request.getParameter("time"));
//			searchListInfo.seteTime(request.getParameter("time"));
//			searchListInfo.setvFileName(request.getParameter("fileName"));
//
//			List<SearchListInfo> searchListResult = searchListInfoService.selectSearchListInfo(searchListInfo);
//			Integer searchListResultTotal = searchListResult.size();
//
//			if(searchListResultTotal == 1) {
//				SearchListInfo item = searchListResult.get(0);
//
//				recInfoResult.put("result", true);
//				if(item.getUserId() != null){
//					recInfoResult.put("userId", item.getUserId());
//				} else {
//					recInfoResult.put("userId", "");
//				}
//				if(item.getCustPhone1() != null) {
//					recInfoResult.put("phoneNo", item.getCustPhone1());
//				} else {
//					recInfoResult.put("phoneNo", "");
//				}
//			} else {
//				recInfoResult.put("result", false);
//				recInfoResult.put("msg", messageSource.getMessage("views.search.grid.alert.noInfo", null, Locale.getDefault()));
//			}
//		}
//
//		response.setContentType("application/json");
//		response.getWriter().write(recInfoResult.toString());
//	}
//}
