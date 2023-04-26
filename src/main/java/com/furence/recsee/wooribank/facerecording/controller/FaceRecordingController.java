package com.furence.recsee.wooribank.facerecording.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.TemplateKeyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.login.service.LoginService;
import com.furence.recsee.wooribank.script.param.request.FileDownloadParam;
import com.furence.recsee.wooribank.script.service.FileProviderService;
import com.furence.recsee.wooribank.script.service.file.types.FileType;
import com.furence.recsee.wooribank.script.service.file.types.FileService.FileServiceType;
import com.furence.recsee.wooribank.script.service.file.types.FileService.ProviderParam;
import com.initech.shttp.server.Logger;


@Controller
@RequestMapping("/faceRecording")
public class FaceRecordingController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FaceRecordingController.class);
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private OrganizationInfoService organizationInfoService;
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value = "/face_recording")
	public ModelAndView face_recording(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView("/faceRecording/face_recording", request, local, model, "faceRecording");
	}
	
	private ModelAndView setModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");

		if(userInfo != null) {
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LISTEN");
			etcConfigInfo.setConfigKey("IP");

			List<EtcConfigInfo> listenIp = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("extNo", userInfo.getExtNo());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			model.addAttribute("listenIp",listenIp.get(0).getConfigValue());
			
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);
			model.addAttribute("nowAccessInfo", nowAccessInfo);

			ModelAndView result = new ModelAndView();
			if( nowAccessInfo.getAccessLevel() != null ){
				model.addAttribute("nowAccessInfo", nowAccessInfo);
				result.setViewName(viewName);
				return result;
			}else{
				RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
				rv.setExposeModelAttributes(false);

				return new ModelAndView(rv);
			}
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);

			return new ModelAndView(rv);
		}
	}
	
	@RequestMapping(value = "/face_recording_demo")
	public ModelAndView face_recording_demo(HttpServletRequest request, Locale local, Model model) {
		return setModelAndView2("/faceRecording/face_recording_demo", request, local, model, "face_recording_demo");
	}
	
	private ModelAndView setModelAndView2(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String)request.getSession().getAttribute("systemTemplates");
		String defaultSkin = (String)request.getSession().getAttribute("defaultSkin");
		if(userInfo != null) {
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("userInfoJson", json);
			model.addAttribute("bgCode", userInfo.getBgCode());
			model.addAttribute("mgCodeName", userInfo.getMgCodeName());
			model.addAttribute("sgCodeName", userInfo.getSgCodeName());
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userId", userInfo.getUserId());
			model.addAttribute("extNo", userInfo.getExtNo());
			model.addAttribute("systemTemplates", systemTemplates);
			model.addAttribute("defaultSkin", defaultSkin);
			model.addAttribute("callKey", request.getParameter("callKey"));
			
			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>)request.getSession().getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, AccessPath);
			model.addAttribute("nowAccessInfo", nowAccessInfo);

			ModelAndView result = new ModelAndView();
			result.setViewName(viewName);
			return result;
		} else {
			RedirectView rv = new RedirectView(request.getContextPath() + "/login/init");
			rv.setExposeModelAttributes(false);
			return new ModelAndView(rv);
		}
	}
	
	   @RequestMapping(value = "/face_recording_view")
	    public ModelAndView face_recording_view(HttpServletRequest request, Locale local, Model model) {

		   	if(!StringUtil.isNull(request.getParameter("callKey"),true)) {
		   		String parameter = request.getParameter("callKey");
		   		model.addAttribute("callKey",parameter);
		   	}
	            return faceRecordingModelAndView("/faceRecording/face_recording_view", request, local, model, "face_recording_demo")    ;
	        
	    }
	   
	   @RequestMapping(value = "/face_recording_guide_img")
		public ModelAndView face_recording_guide_img(HttpServletRequest request, Locale local, Model model) {
		   return faceRecordingModelAndView("/faceRecording/face_recording_guide_img", request, local, model, "face_recording_guide_img")    ;
		}
	   
	   @RequestMapping(value = "/face_recording_guidepdf")
	   public ModelAndView face_recording_guidepdf(HttpServletRequest request, Locale local, Model model) {
		   File pdfFile = null;
		   String pdfPath = null;
		   EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey("guidePdf");
			List<EtcConfigInfo> pdfPathResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if(pdfPathResult.size()>0) {
				pdfPath = pdfPathResult.get(0).getConfigValue();
				pdfFile = new File(pdfPath);
				
			}
			ModelAndView mv = new ModelAndView();
			mv.setViewName("fileDownload");
			mv.addObject("downloadFile",pdfFile);
		   return mv;
	   }
	    private ModelAndView faceRecordingModelAndView(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
	    	EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("TEMPLATES");
			List<EtcConfigInfo> systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

			String systemTemplates = "rs";
			if (systemConfigInfoResult.get(0).getConfigValue() != null) {
				systemTemplates = systemConfigInfoResult.get(0).getConfigValue().toLowerCase();
			}

			TemplateKeyInfo templateKeyInfo = new TemplateKeyInfo();
			templateKeyInfo.setTemplateKey(systemTemplates);

			List<TemplateKeyInfo> templateKeyResult = etcConfigInfoService.selectTemplateKeyInfo(templateKeyInfo);

			for (int i = 0; i < templateKeyResult.size(); i++) {
				request.setAttribute(templateKeyResult.get(i).getColorKey(), templateKeyResult.get(i).getColorValue());

			}
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("DEFAULT_SKIN");
			systemConfigInfoResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String defaultSkin = "web";
			if (systemConfigInfoResult.get(0).getConfigValue() != null) {
				defaultSkin = systemConfigInfoResult.get(0).getConfigValue().toLowerCase();
			}
	    	model.addAttribute("defaultSkin", "web");
            model.addAttribute("callKey", request.getParameter("callKey"));
            if(!"".equals(request.getParameter("type"))) {
                model.addAttribute("type", request.getParameter("type"));
            }else {
                model.addAttribute("type", "");
            }        
            
        	etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LISTEN");
			etcConfigInfo.setConfigKey("IP");

			List<EtcConfigInfo> etc = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			model.addAttribute("listenIp",etc.get(0).getConfigValue());
			
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("HTTPS");
			
			etc = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			model.addAttribute("HTTP",etc.get(0).getConfigValue());
			
			etcConfigInfo.setGroupKey("voice_client");
			etcConfigInfo.setConfigKey("version");
			
			List<EtcConfigInfo> selectEtcConfigInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			selectEtcConfigInfo = Optional.of(selectEtcConfigInfo).orElse(Collections.EMPTY_LIST);
			if(!selectEtcConfigInfo.isEmpty()) {
				model.addAttribute("version",Optional.ofNullable(selectEtcConfigInfo.get(0).getConfigValue()).orElse(""));
			}else {
				model.addAttribute("version","");
			}
			
			
            ModelAndView result = new ModelAndView();
            result.setViewName(viewName);
            return result;
	    }
	
	
	
	@RequestMapping(value = "/face_recording_mainview")
	public ModelAndView face_recording_mainview3(HttpServletRequest request, Locale local, Model model) {
		return faceRecordingModelAndView3("/faceRecording/face_recording_mainview", request, local, model, "face_recording_demo")	;
	}
	
	/*
	 * @RequestMapping(value = "/free_recording") //비교설명 public ModelAndView
	 * free_recording(HttpServletRequest request, Locale local, Model model
	 * , @RequestParam(required = false) String params) { return
	 * faceRecordingModelAndView("/faceRecording/free_recording", request, local,
	 * model, "free_recording") ; }
	 */
	
	@RequestMapping(value = "/free_recording2") //상품설명
	public ModelAndView free_recording2(HttpServletRequest request, Locale local, Model model , @RequestParam(required = false) String params) {
		return faceRecordingModelAndView("/faceRecording/free_recording2", request, local, model, "free_recording2")	;
	}
	
	@RequestMapping(value = "/face_recording_mainviewBk")
	public ModelAndView face_recording_mainviewBk(HttpServletRequest request, Locale local, Model model) {
		return faceRecordingModelAndView3("/faceRecording/face_recording_mainviewbk", request, local, model, "face_recording_demo")	;
	}
	
	@RequestMapping(value = "/freeRec.do")
	public ModelAndView face_recording_freeRec(HttpServletRequest request, Locale local, Model model
						, @RequestParam(required = false) String params) {
		request.setAttribute("params", params);
		
		return faceRecordingModelAndView3("/faceRecording/face_recording_mainviewbk", request, local, model, "face_recording_demo")	;
	}
	
	@RequestMapping(value = "/face_recording_mainviewAuth")
	public ModelAndView face_recording_mainviewAuth(HttpServletRequest request, Locale local, Model model) {
		return faceRecordingModelAndView3("/faceRecording/face_recording_mainview_auth", request, local, model, "face_recording_demo")	;
	}
	
	private ModelAndView faceRecordingModelAndView3(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
			ObjectMapper mapper = new ObjectMapper();
			String json = "";
			try {
				json = mapper.writeValueAsString(userInfo).replaceAll("\"", "\'");
			} catch (IOException e) {
				Logger.error("", "", "", e.toString());
			}
			model.addAttribute("defaultSkin", "web");
			model.addAttribute("callKey", request.getParameter("callKey"));
			if(!"".equals(request.getParameter("type"))) {
				model.addAttribute("type", request.getParameter("type"));
			}else {
				model.addAttribute("type", "");
			}		
			ModelAndView result = new ModelAndView();
			result.setViewName(viewName);
			return result;

	}
	
	@RequestMapping(value = "/testPopupOpen")
	public ModelAndView testPopupOpen(HttpServletRequest request, Locale local, Model model) {
		return setModelAndViewTest("/faceRecording/testPopup", request, local, model, "faceRecording");
	}
	
	private ModelAndView setModelAndViewTest(String viewName, HttpServletRequest request, Locale local, Model model, String AccessPath) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for( String key : parameterMap.keySet()) {
			map.put(key, parameterMap.get(key)[0]);
		};

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(map);
		} catch (IOException e) {
			Logger.error("", "", "", e.toString());
		}
		logger.info(json);
		model.addAttribute("pdtParams", json);
		
		String groupCode = map.get("BZ_BRCD");
		String personCode = map.get("OPR_NO");
		if(groupCode!=null && !"".equals(groupCode)){
			model.addAttribute("useYn", "N");
			OrganizationInfo org = new OrganizationInfo();
			org.setrMgCode(groupCode);
			List<OrganizationInfo> orgInfo = organizationInfoService.selectOrganizationMgInfo(org);
			if(orgInfo != null && orgInfo.size() > 0) {
				String orgUseYn = orgInfo.get(0).getUseYn();
				if(orgUseYn != null && "Y".equals(orgUseYn)) {
					if(personCode!=null && !"".equals(personCode)){
						LoginVO userInfo = new LoginVO();
						userInfo.setUserId(personCode);
						LoginVO usrInfo = loginService.selectUserInfo(userInfo);
						if (usrInfo != null) {
							String usrUseYn = usrInfo.getSkinCode();
							if(usrUseYn == null || "".equals(usrUseYn) || "Y".equals(usrUseYn)) {
								model.addAttribute("useYn", "Y");
							}
						}
					}
				}
			}
		}
		
		ModelAndView result = new ModelAndView();
		result.setViewName(viewName);
		return result;
	}
	
	@Autowired
	private FileProviderService fileService;
	
	@RequestMapping(value = "/product/{callkey}/script/call", 
			method = RequestMethod.GET )
		public void downloadScriptCallFile(
			@PathVariable(value = "callkey") String callkey,
			@RequestParam(value = "fileType") String fileType,
			@RequestParam(value = "recType") String recType,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		ProviderParam<FileDownloadParam.ScriptCallInfo> providerParam = ProviderParam.of(FileServiceType.CallScript);
		FileDownloadParam.ScriptCallInfo callScriptParam = providerParam.getParamter();
		callScriptParam.setCallKey(callkey);
		callScriptParam.setFileType( FileType.create(fileType) );
		callScriptParam.setType(2);
//		callScriptParam.setRecType(0);
		recType = Optional.ofNullable(recType).orElse("0");
		if(recType.equals("0") || recType.equals("N")) {
			callScriptParam.setRecType(0);
		}else {
			callScriptParam.setRecType(1);
		}
		
		// 요청 파라미터를 확인해서 , 처리할 비즈니스 로직을 결정
		File file = fileService.createFile(providerParam); 
		
		/* response에 파일 반환 */
		
		// 1. 브라우저별 한글 파일이름 표시
		String header = request.getHeader("User-Agent");
		String downloadFileName = file.getName(); // file이 null인 경우 500에러 발생, 이 컨트롤러에서 따로 처리x
		
		downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 2. 클라이언트에게 전달할 파일 복사 -- 상품 버전별 파일을 생성 후 보관, 파일 삭제 x
		try (OutputStream out = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);){		
			FileCopyUtils.copy(fis, out);
		} 
	}
	
	
	
}