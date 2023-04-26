package com.furence.recsee.wooribank.script.controller.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.scriptRegistration.service.ScriptRegistrationService;
import com.furence.recsee.wooribank.script.repository.entity.ProductGroup;
import com.furence.recsee.wooribank.script.service.ProductService;


@Controller
@RequestMapping("/wooribank/script")
public class ScriptViewRoutingController {	
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptViewRoutingController.class);
	
	@Value("#{scriptManageProperties['environment']}")
	private String environment;
	
	@Autowired
	private ScriptRegistrationService scriptRegistrationService;
	
	@Autowired
	private ProductService productService;
	
	/**
	 * 상품스크립트 관리(목록,조회) - 메인
	 * @param request
	 * @param local
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public ModelAndView routeMain(HttpServletRequest request, Locale local, Model model) {

		return setModelAndView("/wooribank/script/script_main", request, local, model, "scriptRegistration");
	}
	
	/**
	 * 상품스크립트 관리(수정) - 팝업
	 * @param request
	 * @param local
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit/{transactionId}/{productPk}")
	public ModelAndView routeEdit(
			@PathVariable(value = "transactionId") String transactionId,
			@PathVariable(value = "productPk") String productPk,
			HttpServletRequest request, Locale local, Model model) throws Exception {
		
		if( transactionId != null ) {
			model.addAttribute("transactionId", transactionId);
		}
		
		if( productPk != null ) {
			model.addAttribute("productPk", productPk); // 상품정보 조회용
		}
		
		String productListPk = SessionManager.getStringAttr(request, transactionId);
		if( productListPk != null ) {
			model.addAttribute("productListPk", productListPk); // 트랜잭션용(rs_script_step_fk)
		}
		
		// 상품 정보 조회
		ProductGroup productGroup = this.productService.getProductById(productPk);
		productGroup = Optional.ofNullable(productGroup).orElseThrow();
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(productGroup);
		
		
		model.addAttribute("productGroupInfo", jsonStr); // 트랜잭션용
		
		return setModelAndView("/wooribank/script/script_edit", request, local, model, "scriptRegistration");
	}
	
	/**
	 * 공용스크립트 관리 - 하위메뉴
	 * @param request
	 * @param local
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/script_common")
	public ModelAndView routeCommon(HttpServletRequest request, Locale local, Model model) {

		return setModelAndView("/wooribank/script/script_common", request, local, model, "scriptRegistration");
	}
	
	
	/**
	 * 결재목록 - 하위메뉴
	 * @param request
	 * @param local
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/script_approve")
	public ModelAndView routeApprove(HttpServletRequest request, Locale local, Model model) {

		return setModelAndView("/wooribank/script/script_approve", request, local, model, "scriptRegistration");
	}
	
	/**
	 * 상신목록 - 하위메뉴
	 * @param request
	 * @param local
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/script_approve_result")
	public ModelAndView routeApproveReport(HttpServletRequest request, Locale local, Model model) {

		return setModelAndView("/wooribank/script/script_approve_result", request, local, model, "scriptRegistration");
	}
	
	/**
	 * 변경이력 - 팝업
	 * @param request
	 * @param local
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/script_history/{rsProductPk}/history")
	public ModelAndView routeChangeList(
			@PathVariable(value = "rsProductPk") String rsProductPk,
			@RequestParam(value = "v") String dummyString,
			HttpServletRequest request, Locale local, Model model) {
		
		if( rsProductPk != null ) {
			model.addAttribute("rsProductPk", rsProductPk); // 변경이력 조회용
		}
		
		return setModelAndView("/wooribank/script/script_history", request, local, model, "scriptRegistration");
	}
	
	
	@SuppressWarnings("unchecked")
	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		String rScriptName = (String)request.getSession().getAttribute("rScriptName");
		String rScriptcode = (String)request.getSession().getAttribute("rScriptcode");

		if(userInfo != null) {
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("rScriptName", rScriptName);
			model.addAttribute("rScriptcode", rScriptcode);
			
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				logger.error( e.toString());
			}
			
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);
			model.addAttribute("nowAccessInfo", nowAccessInfo);

			ModelAndView result = new ModelAndView();
			String accessLevel = Optional.ofNullable(nowAccessInfo.getAccessLevel()).orElse("") ;
			
			if( accessLevel.isEmpty() ) {
				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
				rv.setExposeModelAttributes(false);
				return new ModelAndView(rv);
			}
				
			// 로그인 유저의 경우 , 스크립트 관련 공통 코드 정보를 모델에 담아 뷰로 전달함. 				
			model.addAttribute("nowAccessInfo", nowAccessInfo);
			
			// 공통코드 정보 리턴
			List<String> categories = new ArrayList<>();
			categories.add("SCRT");
			categories.add("SSDT");
			categories.add("r_biz_dis");
			// 공통 코드 json 데이터 가져오기
			Map<String, JSONArray> codeMap = scriptRegistrationService.selectScriptCommonCode(categories);
			
			if (codeMap == null || codeMap.isEmpty()) {
				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
				rv.setExposeModelAttributes(false);
				return new ModelAndView(rv);
			}
//
//			if (String.valueOf(accessLevel.charAt(0)).equals("P")) {
//				// 각 상품부서는 해당상품에 대해서만 조회 가능하도록 처리
//				for(int i=0; i<codeMap.get("r_biz_dis").size(); i++) {
//					JSONObject item = (JSONObject)codeMap.get("r_biz_dis").get(i);
//					String code = (String)item.get("code");
//					//if(String.valueOf(accessLevel.charAt(accessLevel.length()-1)).equals(code)){
//					if( accessLevel.startsWith(code) ) {
//						
//						JSONArray jsonArray = codeMap.get("r_biz_dis");
//						jsonArray.clear();
//						jsonArray.add(item);
//						
//					}
//				}
//				
//			}
			
			model.addAttribute("codeMap", codeMap);
			
			// 빌드환경 정보
			model.addAttribute("environment", environment);
			
			result.setViewName(viewName);
			return result;
			
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);
			return new ModelAndView(rv);
		}
	}

}