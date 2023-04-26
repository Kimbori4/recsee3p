
package com.furence.recsee.scriptRegistration.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.util.DateUtil;
import com.furence.recsee.common.util.DirectoryUtil;
import com.furence.recsee.common.util.FileCopyUtil;
import com.furence.recsee.common.util.OsUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.TTSUtil;
import com.furence.recsee.scriptRegistration.dao.AdminProductInsertDAO;
import com.furence.recsee.scriptRegistration.model.AdminProductInsertVO;
import com.furence.recsee.scriptRegistration.model.AdminScriptStepDetailInfo;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.scriptRegistration.service.ScriptRegistrationService;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.facerecording.request.RequestAdminScriptUpdateInfo;
import com.furence.recsee.wooribank.facerecording.request.RequestSinkTableInfo;

@Controller
public class AjaxScriptRegistrationController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AjaxScriptRegistrationController.class);
	private final static Logger Log = Logger.getGlobal();

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private ScriptRegistrationService scriptRegistrationService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private FileCopyUtil copyUtil;

	@Value("#{ttsProperties['path.windowHistoryPath']}")
	private String windowHistoryPath;

	@Value("#{ttsProperties['path.linuxHistoryPath']}")
	private String linuxHistoryPath;

	@Value("#{ttsProperties['path.windowRealTimeTTSPath']}")
	private String windowRealTTSPath;

	@Value("#{ttsProperties['path.linuxRealTimeTTSPath']}")
	private String linuxRealTTSPath;
	
	@Value("#{ttsProperties['server.ip']}")
	private String serverIp;
	
	@Value("#{ttsProperties['server.http']}")
	private String HTTP;

	

	// product
	@RequestMapping(value = "/selectProductList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectProductList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		if (!StringUtil.isNull(request.getParameter("rsScriptStepPk"), true))
			scriptRegistrationInfo.setrProductListPk(Integer.parseInt(request.getParameter("rsScriptStepPk")));

		List<ScriptRegistrationInfo> selectProductList = scriptRegistrationService.selectProductList(scriptRegistrationInfo);

		if (selectProductList != null && selectProductList.size() > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectProductList", selectProductList);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}


		return jRes;

	}

	// 공통문구 수정 팝업 에서 키워드 조회
	@RequestMapping(value = "/searchScriptCommonList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO searchScriptCommonList(HttpServletRequest request) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String parameter = request.getParameter("keyword");
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			logger.info("keyword="+request.getParameter("keyword"));
			logger.info("action="+request.getParameter("action"));
			logger.info("scriptType="+request.getParameter("scriptType"));
			
			if (!StringUtil.isNull(request.getParameter("keyword"), true)) {
				scriptRegistrationInfo.setrSearchWord(request.getParameter("keyword"));
			}
			if (!StringUtil.isNull(request.getParameter("action"), true)) {
				scriptRegistrationInfo.setrSearchType(request.getParameter("action"));
			}
			if (!StringUtil.isNull(request.getParameter("rsScriptCommonDesc"), true)) {
				scriptRegistrationInfo.setRsScriptCommonDesc(request.getParameter("rsScriptCommonDesc"));
			}
			logger.info("request.getParameter(\"scriptType\"):"+request.getParameter("scriptType"));
			if (!StringUtil.isNull(request.getParameter("scriptType"), true)) {
				scriptRegistrationInfo.setScriptType(request.getParameter("scriptType"));
				scriptRegistrationInfo.setrScriptCommonType(request.getParameter("scriptType"));
			}						
			List<ScriptRegistrationInfo> searchCommonScriptDetail = scriptRegistrationService.searchCommonScriptDetail(scriptRegistrationInfo);

			if (searchCommonScriptDetail != null && searchCommonScriptDetail.size() > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("searchCommonScriptList", searchCommonScriptDetail);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");
		}

		return jRes;

	}

	@RequestMapping(value = "/insertScriptCommon.do", method = { RequestMethod.POST })
	public @ResponseBody AJaxResVO insertCommonScript(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			// userId
			scriptRegistrationInfo.setRsScriptCommonCreateUser(userInfo.getUserId());
			if (!StringUtil.isNull(request.getParameter("name"), true))
				scriptRegistrationInfo.setRsScriptCommonName((request.getParameter("name")));
			if (!StringUtil.isNull(request.getParameter("desc"), true))
				scriptRegistrationInfo.setRsScriptCommonDesc((request.getParameter("desc")));
			if (!StringUtil.isNull(request.getParameter("text"), true))
				scriptRegistrationInfo.setRsScriptCommonText((request.getParameter("text")));
			// T,G 구분 해서 추가해넣기!
			if (!StringUtil.isNull(request.getParameter("type"), true))
				scriptRegistrationInfo.setRsScriptCommonType((request.getParameter("type")));
			if (!StringUtil.isNull(request.getParameter("realTime"), true))
				scriptRegistrationInfo.setRsScriptCommonRealTimeTts(request.getParameter("realTime"));
			// 날짜
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");
			scriptRegistrationInfo.setRsScriptCommonCreateDate(date);

			Integer inserResult = scriptRegistrationService.insertCommonScript(scriptRegistrationInfo);

			if (inserResult != null && inserResult > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				/* jRes.addAttribute("product", product); */
				jRes.addAttribute("inserResult", inserResult);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");

		}
		return jRes;
	}

	@RequestMapping(value = "/deleteScriptCommon.do", method = { RequestMethod.POST })
	public @ResponseBody AJaxResVO deleteCommonScript(HttpServletRequest request) throws IOException {
		Integer ResultDeleteCommonScript = null;
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			if (!StringUtil.isNull(request.getParameter("idx"), true))
				scriptRegistrationInfo.setRsScriptCommonPk(Integer.parseInt((request.getParameter("idx"))));

			ResultDeleteCommonScript = scriptRegistrationService.deleteCommonScript(scriptRegistrationInfo);
			if (ResultDeleteCommonScript != null && ResultDeleteCommonScript > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				/* jRes.addAttribute("product", product); */
				jRes.addAttribute("ResultDeleteCommonScript", ResultDeleteCommonScript);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");

		}
		return jRes;
	}

	@RequestMapping(value = "/updateScriptCommon.do", method = { RequestMethod.POST })
	public @ResponseBody AJaxResVO updateCommonScript(HttpServletRequest request) throws IOException {
		Integer ResultUpdateCommonScript = null;
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			// 공용문구를 수정하고있는 userId
			scriptRegistrationInfo.setRsScriptCommonUpdateUser(userInfo.getUserId());
			if (!StringUtil.isNull(request.getParameter("pk"), true))
				scriptRegistrationInfo.setRsScriptCommonPk(Integer.parseInt(request.getParameter("pk")));
			if (!StringUtil.isNull(request.getParameter("name"), true))
				scriptRegistrationInfo.setRsScriptCommonName((request.getParameter("name")));
			if (!StringUtil.isNull(request.getParameter("desc"), true))
				scriptRegistrationInfo.setRsScriptCommonDesc((request.getParameter("desc")));
			if (!StringUtil.isNull(request.getParameter("text"), true))
				scriptRegistrationInfo.setRsScriptCommonText((request.getParameter("text")));
			if (!StringUtil.isNull(request.getParameter("type"), true))
				scriptRegistrationInfo.setRsScriptCommonType((request.getParameter("type")));
			if (!StringUtil.isNull(request.getParameter("realTime"), true))
				scriptRegistrationInfo.setRsScriptCommonRealTimeTts((request.getParameter("realTime")));
			// Update Date
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");
			scriptRegistrationInfo.setRsScriptCommonUpdateDate(date);
			ResultUpdateCommonScript = scriptRegistrationService.updateCommonScript(scriptRegistrationInfo);

			if (ResultUpdateCommonScript != null && ResultUpdateCommonScript > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				/* jRes.addAttribute("product", product); */
				jRes.addAttribute("ResultUpdateCommonScript", ResultUpdateCommonScript);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");

		}
		return jRes;
	}

	// tree_step 내용 가져오기
	@RequestMapping(value = "/selectScriptStepList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectScriptStepList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		scriptRegistrationInfo.setrScriptStepPk(request.getParameter("rScriptStepPk"));
		if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true))
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));
		if (!StringUtil.isNull(request.getParameter("rScriptStepName"), true))
			scriptRegistrationInfo.setrScriptStepName(request.getParameter("rScriptStepName"));

		List<ScriptRegistrationInfo> selectScriptStepList = scriptRegistrationService
				.selectScriptStepList(scriptRegistrationInfo);

		if (selectScriptStepList != null && selectScriptStepList.size() > 0) {
			// 성공시
			jRes.setResult("Sucess");
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			/* jRes.addAttribute("product", product); */
			jRes.addAttribute("", selectScriptStepList);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}
//		} else { jRes.setResult("0");
//			
//			 jRes.addAttribute("msg", "login fail");
//		  
//			logInfoService.writeLog(request, "Etc - Logout"); 
//		  
//		}

		return jRes;

	}

	// 스크립트 검색 시 가져오는 데이터

	@RequestMapping(value = "/searchScript.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO searchScript(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			// ▼데이터 널값 체크 후 파라미터/getter,setter 가져오기
			// type
			if (!StringUtil.isNull(request.getParameter("rScriptDetailPk"), true))
				scriptRegistrationInfo.setrScriptDetailPk(Integer.parseInt(request.getParameter("rScriptDetailPk")));
			// text
			if (!StringUtil.isNull(request.getParameter("rScriptDetailType"), true))
				scriptRegistrationInfo.setrScriptDetailType(request.getParameter("rScriptDetailType"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailComKind"), true))
				scriptRegistrationInfo.setrScriptDetailComKind(request.getParameter("rScriptDetailComKind"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailText"), true))
				scriptRegistrationInfo.setrScriptDetailText(request.getParameter("rScriptDetailText"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailIfCase"), true))
				scriptRegistrationInfo.setrScriptDetailIfCase(request.getParameter("rScriptDetailIfCase"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailIfCaseDetail"), true))
				scriptRegistrationInfo.setrScriptDetailIfCaseDetail(request.getParameter("rScriptDetailIfCaseDetail"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailRealtimeTTS"), true))
				scriptRegistrationInfo.setrScriptDetailRealtimeTTS(request.getParameter("rScriptDetailRealtimeTTS"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailCreateDate"), true))
				scriptRegistrationInfo.setrScriptDetailCreateDate(request.getParameter("rScriptDetailCreateDate"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailUpdateUser"), true))
				scriptRegistrationInfo.setrScriptDetailUpdateUser(request.getParameter("rScriptDetailUpdateUser"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirm"), true))
				scriptRegistrationInfo.setrScriptDetailConfirm(request.getParameter("rScriptDetailConfirm"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmDate"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmDate(request.getParameter("rScriptDetailConfirmDate"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmUser"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmUser(request.getParameter("rScriptDetailConfirmUser"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmOrder"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmOrder(request.getParameter("rScriptDetailConfirmOrder"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmComFk"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmComFk(request.getParameter("rScriptDetailConfirmComFk"));

			List<ScriptRegistrationInfo> searchScript = scriptRegistrationService.searchScript(scriptRegistrationInfo);

			if (searchScript != null && searchScript.size() > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				/* jRes.addAttribute("product", product); */
				jRes.addAttribute("searchScript", searchScript);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");

		}

		return jRes;

	}

	// script UPdate
	@RequestMapping(value = "/updateScript.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO updateScript(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			// ▼데이터 널값 체크 후 파라미터/getter,setter 가져오기
			// type
			if (!StringUtil.isNull(request.getParameter("rScriptDetailPk"), true))
				scriptRegistrationInfo.setrScriptDetailPk(Integer.parseInt(request.getParameter("rScriptDetailPk")));
			// text
			if (!StringUtil.isNull(request.getParameter("rScriptDetailType"), true))
				scriptRegistrationInfo.setrScriptDetailType(request.getParameter("rScriptDetailType"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailComKind"), true))
				scriptRegistrationInfo.setrScriptDetailComKind(request.getParameter("rScriptDetailComKind"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailText"), true))
				scriptRegistrationInfo.setrScriptDetailText(request.getParameter("rScriptDetailText"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailIfCase"), true))
				scriptRegistrationInfo.setrScriptDetailIfCase(request.getParameter("rScriptDetailIfCase"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailIfCaseDetail"), true))
				scriptRegistrationInfo.setrScriptDetailIfCaseDetail(request.getParameter("rScriptDetailIfCaseDetail"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailRealtimeTTS"), true))
				scriptRegistrationInfo.setrScriptDetailRealtimeTTS(request.getParameter("rScriptDetailRealtimeTTS"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailCreateDate"), true))
				scriptRegistrationInfo.setrScriptDetailCreateDate(request.getParameter("rScriptDetailCreateDate"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailUpdateUser"), true))
				scriptRegistrationInfo.setrScriptDetailUpdateUser(request.getParameter("rScriptDetailUpdateUser"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirm"), true))
				scriptRegistrationInfo.setrScriptDetailConfirm(request.getParameter("rScriptDetailConfirm"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmDate"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmDate(request.getParameter("rScriptDetailConfirmDate"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmUser"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmUser(request.getParameter("rScriptDetailConfirmUser"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmOrder"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmOrder(request.getParameter("rScriptDetailConfirmOrder"));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailConfirmComFk"), true))
				scriptRegistrationInfo.setrScriptDetailConfirmComFk(request.getParameter("rScriptDetailConfirmComFk"));

//		  List<ScriptRegistrationInfo> updateScript = scriptRegistrationService.updateScript(scriptRegistrationInfo);
			Integer updateScript = scriptRegistrationService.updateScript(scriptRegistrationInfo);

			if (updateScript != null && updateScript > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				/* jRes.addAttribute("product", product); */
				jRes.addAttribute("updateScript", updateScript);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");

		}

		return jRes;

	}

	// update Detail
		@RequestMapping(value = "/updateScriptDetail.do", method = { RequestMethod.POST, RequestMethod.GET })
		public @ResponseBody AJaxResVO updateScriptDetail(HttpServletRequest request) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			
			// 미로그인 리턴
			if (userInfo == null) {
				jRes.setResult("0");
				jRes.addAttribute("msg", "login fail");
				logInfoService.writeLog(request, "Etc - Logout");
				return jRes;
			}
			
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			
			JSONParser parser = new JSONParser();
			JSONArray scriptArray = null;
			try {
				scriptArray = (JSONArray) parser.parse(request.getParameter("scriptArray"));
			} catch (Exception e) {
				logger.error("error",e);
			}

			Integer result = 0;
			if (scriptArray == null) {
				result = 1;
			} else if (scriptArray.size() > 0) {
				String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
				
				for (int i = 0; i < scriptArray.size(); i++) {
					scriptRegistrationInfo = new ScriptRegistrationInfo();
					
					JSONObject param = (JSONObject) scriptArray.get(i);
					scriptRegistrationInfo.setrScriptDetailPk(Integer.parseInt(param.get("scriptkey").toString()));
					scriptRegistrationInfo.setrScriptDetailType(param.get("detailType").toString());
					scriptRegistrationInfo.setrScriptDetailText(param.get("htmlText").toString());
					scriptRegistrationInfo.setrScriptDetailIfCase(param.get("ifcase").toString());
					scriptRegistrationInfo.setrScriptDetailIfCaseDetail(param.get("ifcasedetail").toString());
					scriptRegistrationInfo.setrScriptDetailUdateDate(now);
					scriptRegistrationInfo.setrScriptDetailUpdateUser(userInfo.getUserId());

					Integer tempResult = 0;

					if (!StringUtil.isNull(param.get("detailComKind").toString(), true) && "N".equals(param.get("detailComKind").toString())) {
						tempResult = scriptRegistrationService.updateScriptDetail(scriptRegistrationInfo);
					} else {
						scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(param.get("detailComFk").toString()));
						tempResult = 1;
//						tempResult = scriptRegistrationService.updateCommonScriptDetail(scriptRegistrationInfo);
					}

					if (tempResult > 0) {
						result++;
					}
				}
			}
			if (result > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}

			return jRes;

		}

	// delete Detail
	@RequestMapping(value = "/deleteScriptDetail.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO deleteScriptDetail(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
			Integer tempResult = 0;

			if (!StringUtil.isNull(request.getParameter("rScriptDetailPk"), true))
				scriptRegistrationInfo.setrScriptDetailPk(Integer.parseInt(request.getParameter("rScriptDetailPk")));

			if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true))
				scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));

			if (!StringUtil.isNull(request.getParameter("rScriptDetailPk"), true))
				scriptRegistrationInfo.setrScriptDetailPk(Integer.parseInt(request.getParameter("rScriptDetailPk")));

			// text
			if (!StringUtil.isNull(request.getParameter("rScriptDetailType"), true))
				scriptRegistrationInfo.setrScriptDetailType(request.getParameter("rScriptDetailType"));

			Integer deleteScriptDetail = scriptRegistrationService.deleteScriptDetail(scriptRegistrationInfo);
//			Integer deleteCommonScriptDetail= scriptRegistrationService.deleteCommonScriptDetail(scriptRegistrationInfo);

			if (deleteScriptDetail > 0) { // 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("deleteScriptDetail", deleteScriptDetail);
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
			logInfoService.writeLog(request, "Etc - Logout");
		}

		return jRes;

	}

	// add Detail
	@RequestMapping(value = "/addScriptDetail.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO addScriptDetail(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request); 
		
		// 미로그인 리턴
		if (userInfo == null) {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
			logInfoService.writeLog(request, "Etc - Logout");
			return jRes;
		}
		
		JSONParser parser = new JSONParser();
		JSONArray insertDetailArray = null;
		try {
			insertDetailArray = (JSONArray) parser.parse(request.getParameter("insertDetailArray"));
		} catch (Exception e) {
			logger.error("error",e);
		}
		
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		Integer result = 0;
		if (insertDetailArray != null) {
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
			
			for (int i = 0; i < insertDetailArray.size(); i++) {
				scriptRegistrationInfo = new ScriptRegistrationInfo();
				
				JSONObject param = (JSONObject) insertDetailArray.get(i); 
				scriptRegistrationInfo.setrScriptDetailType(param.get("detailType").toString());
				scriptRegistrationInfo.setrScriptDetailText(param.get("detailText").toString());
				scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(param.get("scriptStepFk").toString()));
				if (param.get("detailComFk").toString().trim().length() > 0) {
					scriptRegistrationInfo.setrScriptDetailConfirmComFk(param.get("detailComFk").toString());
				}
				scriptRegistrationInfo.setrScriptDetailComKind(param.get("detailComKind").toString());
				scriptRegistrationInfo.setrScriptDetailIfCase(param.get("ifcase").toString());
				scriptRegistrationInfo.setrScriptDetailIfCaseDetail(param.get("ifcasedetail").toString());
				scriptRegistrationInfo.setrUseYn("Y");
				scriptRegistrationInfo.setrScriptDetailCreateDate(now);
				scriptRegistrationInfo.setrScriptDetailCreateUser(userInfo.getUserId());

				Integer realResult = 0;
				realResult = scriptRegistrationService.addScriptDetail(scriptRegistrationInfo);
				if (realResult > 0) {
					result++;
				}
			} // forans 마루리

			if (insertDetailArray.size() == result) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		}
		return jRes;
	}

	// scriptSampleTTS
	@RequestMapping(value = "/scriptSampleTTS.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO scriptSampleTTS(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		String[] scriptArray = request.getParameterValues("scriptArray");
		Integer result = 0;
		for (int i = 0; i < scriptArray.length; i++) {
			scriptRegistrationInfo = new ScriptRegistrationInfo();
			String[] param = scriptArray[i].split("//");
			scriptRegistrationInfo.setrScriptDetailPk(Integer.parseInt(param[0]));
			scriptRegistrationInfo.setrScriptDetailType(param[1]);
			scriptRegistrationInfo.setrScriptDetailText(param[2]);
			Integer tempResult = 0;
			if (!StringUtil.isNull(param[3], true) && "N".equals(param[3])) {
				tempResult = scriptRegistrationService.updateScriptDetail(scriptRegistrationInfo);
			} else {
				scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(param[4]));
				tempResult = scriptRegistrationService.updateCommonScriptDetail(scriptRegistrationInfo);
			}

			if (tempResult > 0) {
				result++;
			}
		}
		if (scriptArray.length == result) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}
//	  } else { jRes.setResult("0");
//		  jRes.addAttribute("msg", "login fail");
//		  logInfoService.writeLog(request, "Etc - Logout"); 
//	  }

		return jRes;

	}

//  //scriptSampleTTS
//  @RequestMapping(value = "/createFileTTS.do", method = {RequestMethod.POST, RequestMethod.GET })
//  public @ResponseBody AJaxResVO createFileTTS(HttpServletRequest request) throws IOException {
//	  AJaxResVO jRes = new AJaxResVO(); 
//	  LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
//
//		  
//		  logger.info("들어옴");
////		  if (!StringUtil.isNull(request.getParameter("mentTxt"))) {
////			  logger.info("mentTxt");
////			  logger.info(request.getParameter("mentTxt"));
////		  }
//		  
//		  HashMap<String,String> result = TTSUtil.createTTS(request.getParameter("mentTxt"),userInfo.getUserId());
//		  
//		  logger.info("끝");
//		  
//		  logger.info(result);
//		  
//		  
//	  } else { jRes.setResult("0");
//	  	jRes.addAttribute("msg", "login fail");
//	  	logInfoService.writeLog(request, "Etc - Logout"); 
//	  }
//	  
//	  return jRes; 
//	  
//  }

	// Script Detail
	@RequestMapping(value = "/selectScriptDetailList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectScriptDetailList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		// 미로그인 리턴
		if (userInfo == null) {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
			logInfoService.writeLog(request, "Etc - Logout");
			return jRes;
		}
		
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
		int fk = 0;
		boolean flag = false;

		String type = null;
		if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true)) {
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));
			fk = Integer.parseInt(request.getParameter("rScriptStepFk"));
		}
		if (!StringUtil.isNull(request.getParameter("rProductType"), true)) {
			scriptRegistrationInfo.setrProductType((request.getParameter("rProductType")));
		}
		if (!StringUtil.isNull(request.getParameter("rProductCode"), true)) {
			scriptRegistrationInfo.setrProductCode((request.getParameter("rProductCode")));
		}
		// dbl 클릭시 따라오는 파라미터
		if (!StringUtil.isNull(request.getParameter("flag"), true)) {
			flag = true;
		}

		scriptRegistrationInfo.setrScriptDetailComKind("Y");
		scriptRegistrationInfo.setrUseYn("Y");

		if (!StringUtil.isNull(request.getParameter("type"), true))
			type = request.getParameter("type");

		List<ScriptRegistrationInfo> scriptDetailList = null;
		List<scriptProductValueInfo> valueList = null;

		if (type != null && type.equals("T")) {
			Integer pk = scriptRegistrationInfo.getrScriptStepFk();
			List<Integer> pkList = scriptRegistrationService.selectOneScriptStepPk(pk);
			scriptRegistrationInfo.setrScriptStepFk(pkList.get(0));
			scriptDetailList = scriptRegistrationService.scriptDetailList(scriptRegistrationInfo);
		} else {
			if (!StringUtil.isNull(request.getParameter("rScriptStepPk"), true)) {
				scriptRegistrationInfo.setrProductListPk(Integer.parseInt(request.getParameter("rScriptStepPk")));
			}
			
			scriptRegistrationInfo.setrScriptStepFk(scriptRegistrationInfo.getrProductListPk());
			valueList = scriptRegistrationService.selectProductValue(scriptRegistrationInfo);
			
			// dbl 클릭으로 들어오면 fk가따로 들어오지 않기때문에 step을 따로 구해줘야함
			if (flag) {
				scriptDetailList = scriptRegistrationService.searchScriptDblClick(scriptRegistrationInfo);
			} else {
				scriptRegistrationInfo.setrScriptStepFk(fk);
				scriptDetailList = scriptRegistrationService.scriptDetailList(scriptRegistrationInfo);
			}
		}
		// 가변체크
		if (valueList != null && valueList.size() > 0) {
			for (ScriptRegistrationInfo data : scriptDetailList) {
				String text = data.getrScriptDetailText();
				text = TTSUtil.randomRemove2(text, valueList);
				data.setrScriptDetailText(text);
			}
		}

		List<HashMap<String, String>> scriptDetailHashList = new ArrayList<HashMap<String, String>>();

		if (scriptDetailList != null && scriptDetailList.size() > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			/* jRes.addAttribute("product", product); */
			jRes.addAttribute("scriptDetailList", scriptDetailList);
			jRes.addAttribute("scriptDetailTTSList", scriptDetailHashList);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}
		return jRes;
	}
	
	

	// 스크립트 단계 불러오기
	@RequestMapping(value = "/selectTreeScript.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectTreeScript(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		if (!StringUtil.isNull(request.getParameter("rsScriptStepFk"), true))
			scriptRegistrationInfo.setRsScriptStepFk(Integer.parseInt(request.getParameter("rsScriptStepFk")));
		
		if (!StringUtil.isNull(request.getParameter("rScriptStepParent"), true))
			scriptRegistrationInfo.setrScriptStepParent(Integer.parseInt(request.getParameter("rScriptStepParent")));

		

		List<ScriptRegistrationInfo> selectTreeScript = scriptRegistrationService.selectTreeScript(scriptRegistrationInfo);

		if (selectTreeScript != null && selectTreeScript.size() > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectTreeScript", selectTreeScript);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}
//		} else { jRes.setResult("0");
//			
//			 jRes.addAttribute("msg", "login fail");
//		  
//			logInfoService.writeLog(request, "Etc - Logout"); 
//		  
//		}

		return jRes;

	}

	// 스크립트 단계 추가
	@RequestMapping(value = "/addTreeScript.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO addTreeScript(HttpServletRequest request,
			@RequestParam("prodductAddArray[]") String[] arr) throws IOException {
//	  public @ResponseBody AJaxResVO addTreeScript(HttpServletRequest request,@RequestParam("prodductAddArray[]") String[] arr) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

//		  String [] prodductAddArray = request.getParameterValues("prodductAddArray");
			String[] prodductAddArray = arr;
			Integer result = 0;
			logger.info("prodductAddArray : " + prodductAddArray);
			Integer tempResult = 0;
			for (int i = 0; i < prodductAddArray.length; i++) {
				scriptRegistrationInfo = new ScriptRegistrationInfo();

				String[] param = prodductAddArray[i].split("//");
				scriptRegistrationInfo.setRsScriptStepFk(Integer.parseInt(param[0]));
				scriptRegistrationInfo.setrScriptStepParent(Integer.parseInt(param[1]));
				scriptRegistrationInfo.setrScriptStepName(param[2]);
				scriptRegistrationInfo.setrScriptStepType(param[3]);

				tempResult = scriptRegistrationService.addTreeScript(scriptRegistrationInfo);
			

				if (tempResult > 0) {
					result++;
				}
				
			}

			if (prodductAddArray.length == result) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("tempResult", tempResult);
				

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}

		}
		return jRes;
	}

	// 스크립트 삭제
	@RequestMapping(value = "/deleteTreeScript.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO deleteTreeScript(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			if (!StringUtil.isNull(request.getParameter("rScriptStepPk"), true))
				scriptRegistrationInfo.setrScriptStepPk((request.getParameter("rScriptStepPk")));

//		  if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true))
//			  scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));
//		  
			if (!StringUtil.isNull(request.getParameter("rsScriptStepFk"), true))
				scriptRegistrationInfo.setRsScriptStepFk(Integer.parseInt(request.getParameter("rsScriptStepFk")));

			if (!StringUtil.isNull(request.getParameter("rScriptStepOrder"), true))
				scriptRegistrationInfo.setrScriptStepOrder(Integer.parseInt(request.getParameter("rScriptStepOrder")));

			if (!StringUtil.isNull(request.getParameter("rScriptStepParent"), true))
				scriptRegistrationInfo
						.setrScriptStepParent(Integer.parseInt(request.getParameter("rScriptStepParent")));

			if (!StringUtil.isNull(request.getParameter("rScriptStepName"), true))
				scriptRegistrationInfo.setrScriptStepName((request.getParameter("rScriptStepName")));

			Integer deleteTreeScript = scriptRegistrationService.deleteTreeScript(scriptRegistrationInfo);

			if (deleteTreeScript != null && deleteTreeScript > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("deleteTreeScript", deleteTreeScript);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");

			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");

		}

		return jRes;

	}

	// 스크립트 수정
	@RequestMapping(value = "/editTreeScript.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO editTreeScript(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			String[] editChArray = request.getParameterValues("editChArray");
			Integer result = 0;
			Integer tempResult = 0;
//		  logger.info("editChArray.length : " + editChArray.length);

			if (editChArray == null) {
				result = 1;
			} else if (editChArray.length > 0) {
				for (int i = 0; i < editChArray.length; i++) {
					scriptRegistrationInfo = new ScriptRegistrationInfo();
					logger.info("editChArray[i] : " + editChArray[i]);
					String[] param = editChArray[i].split("//");
					scriptRegistrationInfo.setrScriptStepPk(param[0]);
					logger.info("param[0] : " + param[0]);
					scriptRegistrationInfo.setrScriptStepParent(Integer.parseInt(param[1]));
					logger.info("param[1] : " + param[1]);
					scriptRegistrationInfo.setrScriptStepName(param[2]);
					logger.info("param[2] : " + param[2]);

					tempResult = scriptRegistrationService.editTreeScript(scriptRegistrationInfo);

					if (tempResult > 0) {
						result++;
					}
				}
			}
			if (result > 0) {
				// 성공시
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
			logInfoService.writeLog(request, "Etc - Logout");
		}

		return jRes;

	}

	// ProductListGroup 상품 유형을 구분하기 위한
	@RequestMapping(value = "/selectGRProductList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectProductGroupList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

//		  if (!StringUtil.isNull(request.getParameter("rProductListPk"), true))
//			  scriptRegistrationInfo.setrProductListPk(Integer.parseInt(request.getParameter("rProductListPk")));
//		  
		if (!StringUtil.isNull(request.getParameter("rProductCode"), true))
			scriptRegistrationInfo.setrProductCode(request.getParameter("rProductCode"));

		List<ScriptRegistrationInfo> selectGRProductList = scriptRegistrationService.selectGRProductList(scriptRegistrationInfo);

		if (selectGRProductList != null && selectGRProductList.size() > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectGRProductList", selectGRProductList);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}

		return jRes;

	}

//order
	@RequestMapping(value = "/selectScriptDetailPK.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectScriptDetailPK(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		if (!StringUtil.isNull(request.getParameter("rScriptStepPk"), true))
			scriptRegistrationInfo.setrScriptStepPk(request.getParameter("rScriptStepPk"));
		if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true))
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));

		List<ScriptRegistrationInfo> selectScriptDetailPK = scriptRegistrationService
				.selectScriptDetailPK(scriptRegistrationInfo);

		if (selectScriptDetailPK != null && selectScriptDetailPK.size() > 0) {
			// 성공시
			jRes.setResult("Sucess");
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);

			jRes.addAttribute("selectScriptDetailPK", selectScriptDetailPK);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}

		return jRes;

	}

//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
	@RequestMapping(value = "/selectValueTTS.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectValueTTS(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		if (!StringUtil.isNull(request.getParameter("rProductCode"), true))
			scriptRegistrationInfo.setrProductCode(request.getParameter("rProductCode"));
		
		if (!StringUtil.isNull(request.getParameter("rProductType"), true))
			scriptRegistrationInfo.setrProductType(request.getParameter("rProductType"));
		
		if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true))
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));

		List<ScriptRegistrationInfo> selectValueTTS = scriptRegistrationService.selectValueTTS(scriptRegistrationInfo);

		if (selectValueTTS != null && selectValueTTS.size() > 0) {
			// 성공시
			jRes.setResult("Sucess");
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);

			jRes.addAttribute("selectValueTTS", selectValueTTS);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}

		return jRes;

	}

	@RequestMapping(value = "/addproducList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO addproducList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			// ▼데이터 널값 체크 후 파라미터/getter,setter 가져오기
			// type
			if (!StringUtil.isNull(request.getParameter("rProductListPk"), true))
				scriptRegistrationInfo.setrProductListPk((Integer.parseInt(request.getParameter("rProductListPk"))));

			if (!StringUtil.isNull(request.getParameter("rProductType"), true))
				scriptRegistrationInfo.setrProductType((request.getParameter("rProductType")));
			// text
			if (!StringUtil.isNull(request.getParameter("rProductCode"), true))
				scriptRegistrationInfo.setrProductCode(request.getParameter("rProductCode"));

			if (!StringUtil.isNull(request.getParameter("rProductName"), true))
				scriptRegistrationInfo.setrProductName(request.getParameter("rProductName"));

			if (!StringUtil.isNull(request.getParameter("rGroupYn"), true))
				scriptRegistrationInfo.setrGroupYn(request.getParameter("rGroupYn"));

			if (!StringUtil.isNull(request.getParameter("rProductGroupCode"), true))
				scriptRegistrationInfo.setrProductGroupCode(request.getParameter("rProductGroupCode"));

			Integer addproducList = scriptRegistrationService.addproducList(scriptRegistrationInfo);

			if (addproducList != null && addproducList > 0) {
				// 성공시
				jRes.setResult("Sucess");
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("addproducList", addproducList);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		}

		return jRes;

	}

	@RequestMapping(value = "/selectDetailTextAuidoPath.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectDetailTextAuidoPath(HttpServletRequest request) throws IOException {
		String date = DateUtil.toString(new Date());
		logger.info(date);
		
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String detailType = null;
		String type = "";
		int pk = 0;
		String text = null;

		if (!StringUtil.isNull(request.getParameter("detailType"), true))
			detailType = request.getParameter("detailType");
		if (!StringUtil.isNull(request.getParameter("pk"), true))
			pk = Integer.parseInt(request.getParameter("pk"));
		if (!StringUtil.isNull(request.getParameter("text"), true))
			text = request.getParameter("text");
		if (!StringUtil.isNull(request.getParameter("type"), true))
			type = request.getParameter("type");
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
		scriptRegistrationInfo.setrScriptStepFk(pk);
		scriptRegistrationInfo.setrProductType(type);

		String resultTempPath = null;
		List<scriptProductValueInfo> selectValueTTS = null;
			selectValueTTS = scriptRegistrationService.selectValueTTS2(scriptRegistrationInfo);
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String format = sdf.format(new Date()) + "/";
		
		String ipAdd = Optional.ofNullable(serverIp).orElse("");
		detailType = Optional.ofNullable(detailType).orElse("");
		if (detailType.equals("T") || detailType.equals("R")) {
			if (selectValueTTS != null && selectValueTTS.size() != 0) {
				logger.info("selectValue is not null && selectValue Size > 0");
				String replaceText = TTSUtil.randomRemove3(text, selectValueTTS);
				replaceText = TTSUtil.specialTextParse(replaceText);
				replaceText = TTSUtil.dotChangeUtil(replaceText);
				replaceText = TTSUtil.textOneChange(replaceText);
				logger.info(replaceText);
				String tempPath = "";
				String os = OsUtil.getInstance().OsCheck();
				if (os.contains("win")) {
					tempPath = this.windowRealTTSPath + format;
					Log.info("path : " + tempPath);
					DirectoryUtil.mkdirDirectory(new File(tempPath));
				} else {
					tempPath = this.linuxRealTTSPath + format;
					Log.info("path : " + tempPath);
					DirectoryUtil.mkdirDirectory(new File(tempPath));
				}
				String dateFormat = DateUtil.toString(new Date());
				String fileName = dateFormat + "_temp";
				
				logger.info("IP : "+ipAdd);
				
				
				resultTempPath = "https://"+ipAdd+":28881/listen?url="+tempPath + fileName + ".wav";
				logger.info("path : "+resultTempPath );
				boolean createTTS2 = false;
				try {
					createTTS2 = TTSUtil.createTTS2(replaceText, fileName, tempPath);
				} catch (Exception e) {
					createTTS2 = false;
				}
				if(createTTS2) {
					logger.info("create TTS!!");
				}
			}
			if (selectValueTTS == null || selectValueTTS.size() == 0) {
				String replaceText = TTSUtil.specialTextParse(text);
				replaceText = TTSUtil.dotChangeUtil(replaceText);
				replaceText = TTSUtil.textOneChange(replaceText);
				logger.info(replaceText);
				String tempPath = "";
				String os = OsUtil.getInstance().OsCheck();
				if (os.contains("win")) {
					tempPath = this.windowRealTTSPath + format;
					Log.info("path : " + tempPath);
					DirectoryUtil.mkdirDirectory(new File(tempPath));
				} else {
					tempPath = this.linuxRealTTSPath + format;
					Log.info("path : " + tempPath);
					DirectoryUtil.mkdirDirectory(new File(tempPath));
				}
				String dateFormat = DateUtil.toString(new Date());
				String fileName = dateFormat + "_temp";
				
				logger.info("IP : "+ipAdd);
				resultTempPath = "https://"+ipAdd+":28881/listen?url="+tempPath + fileName + ".wav";
				logger.info("path : "+resultTempPath );
				boolean createTTS2 = false;
				try {
					createTTS2 = TTSUtil.createTTS2(replaceText, fileName, tempPath);
				} catch (Exception e) {
					createTTS2 = false;
				}
				if(createTTS2) {
					logger.info("create TTS!!");
				}
			}

		}
		if (resultTempPath != null) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("filePath", resultTempPath);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}

		return jRes;

	}

	@RequestMapping(value = "/directButtonService.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO directButtonService(HttpServletRequest request,
			@RequestParam(value = "fkList[]") String[] fkList) throws IOException {
		AJaxResVO jRes = new AJaxResVO();

		String pk = fkList[0];
//	  String url = "http://localhost:8080/makeTTS/furence/script/tts/load2";
		String url = "http://aiprecsys.woorifg.com:8080/makeTTS/furence/script/tts/load2?pk=" + pk;
//		String url = "http://10.190.201.112:8080/makeTTS/furence/script/tts/load2?pk=" + pk;

//		"rScriptStepFk" : fk,
//		"fkList" : fkList

		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");

//	  List<Integer> fkArrList = new ArrayList<Integer>();
//	  for(String i : fkList) {
//		  fkArrList.add(Integer.parseInt(i));
//	  }

		HttpEntity<List<Integer>> entity = new HttpEntity<>(header);

		// connection Pool
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(300);
		factory.setReadTimeout(300000);

		RestTemplate restApi = new RestTemplate(factory);
		ResponseEntity<String> result = null;
		try {
			result = restApi.exchange(url, HttpMethod.GET, entity, String.class);
			String body = result.getBody();
		} catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}

		if (result != null) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}

		return jRes;
	}

	// 재녹취 TTS 재생성
	@RequestMapping(value = "/reScriptPlay.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO reScriptPlay(HttpServletRequest request , @RequestParam Map<String, String> params) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		HashMap<String, String> hash = new HashMap<>();
		String flag = null;
	    int productPk = 0;
	    ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
    
	    hash.put("params", request.getParameter("params"));
		ObjectMapper mapper = new ObjectMapper();
		HashMap parameter = mapper.readValue(hash.get("params"), HashMap.class);

		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    String format = sdf.format(new Date()) + "/";
	    
		//상품코드
	    if(parameter.containsKey("PRD_CD")) {
	    	scriptRegistrationInfo.setrProductCode(""+parameter.get("PRD_CD"));
	    }
    	//해당STEP의 PK
		if (!StringUtil.isNull(request.getParameter("rScriptStepPk"), true))
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepPk")));
	  // 재녹취할 콜키
		if (!StringUtil.isNull(request.getParameter("scriptCallKey"), true))
			scriptRegistrationInfo.setrScriptCallKey(request.getParameter("scriptCallKey"));
		if (!StringUtil.isNull(request.getParameter("flag"), true))
			flag = ""+request.getParameter("flag");
//	  
		
		flag = Optional.ofNullable(flag).orElse("");
		
	    String resultTempPath = null;
	    //상품코드에 대한 가변값 select
	    List<scriptProductValueInfo> selectValueTTS = scriptRegistrationService.selectValueFromScriptStepPk(scriptRegistrationInfo);
	    //
	    List<ScriptRegistrationInfo> scriptDetailHistoryList = scriptRegistrationService.scriptDetailHistoryList(scriptRegistrationInfo);

	    //실시간폴더 path 생성
	    String tempPath= "";
	    String os = OsUtil.getInstance().OsCheck();
	    if(os.contains("win")) {
	    	tempPath = this.windowRealTTSPath+format;
	    	Log.info("path : "+tempPath);
	    	DirectoryUtil.mkdirDirectory(new File(tempPath));
	    }else {
	    	tempPath = this.linuxRealTTSPath+format;
	    	Log.info("path : "+tempPath);
	    	DirectoryUtil.mkdirDirectory(new File(tempPath));
	    }
	    
	    //month면 실시간 (재녹취시)
	    if(flag.toUpperCase().equals("month")) {
	    	logger.info("1 month overflow All date realTimeTTS = 'Y' ");
	    	for (ScriptRegistrationInfo data : scriptDetailHistoryList) {
	    		data.setrScriptTTSFilePath("");
				String text = data.getrScriptDetailText();
				List<String> ttsTextList = TTSUtil.ttsTextList(text);
				StringBuffer buf = new StringBuffer();
				for (int i = 1; i <= ttsTextList.size(); i++) {
					String ttsText = ttsTextList.get(i-1);
					//가공과정
					ttsText = TTSUtil.randomRemove3(ttsText, selectValueTTS);
						logger.info("data replace (1)");
					ttsText= TTSUtil.specialTextParse(ttsText);
						logger.info("data replace (2)");
					ttsText = TTSUtil.dotChangeUtil(ttsText);
						logger.info("data replace (3) success");
					String dateFormat = DateUtil.toString(new Date());
					String fileName = dateFormat+"_temp_"+i;
					try {
						TTSUtil.createTTS2(ttsText, fileName, tempPath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("error",e);
					}
					String filePath = tempPath+fileName+".wav";
					if(i<ttsTextList.size()) {
						buf.append(filePath+",");
					}else {
						buf.append(filePath);						
					}
				}
				data.setrScriptTTSFilePath(buf.toString());
			}
	    }else { // month가 아니면 기존에 있던 TTS를 가져온다.
	    	logger.info("1 month overflow All date realTimeTTS = 'Y' ");
	    	for (ScriptRegistrationInfo data : scriptDetailHistoryList) {
	    		String realtimeTTS = data.getrScriptDetailRealtimeTTS();
	    		if(realtimeTTS.equals("Y")) {
	    			String text = data.getrScriptDetailText();
					List<String> ttsTextList = TTSUtil.ttsTextList(text);
					StringBuffer buf = new StringBuffer();
					for (int i = 1; i <= ttsTextList.size(); i++) {
						String ttsText = ttsTextList.get(i-1);
						//가공과정
						ttsText = TTSUtil.randomRemove3(ttsText, selectValueTTS);
							logger.info("data replace (1)");
						ttsText= TTSUtil.specialTextParse(ttsText);
							logger.info("data replace (2)");
						ttsText = TTSUtil.dotChangeUtil(ttsText);
							logger.info("data replace (3) success");
						String dateFormat = DateUtil.toString(new Date());
						String fileName = dateFormat+"_temp_"+i;
						try {
							TTSUtil.createTTS2(ttsText, fileName, tempPath);
						} catch (Exception e) {
							logger.error("error",e);
						}
						String filePath = tempPath+fileName+".wav";
						if(i<ttsTextList.size()) {
							buf.append(filePath+",");
						}else {
							buf.append(filePath);						
						}
					}
					data.setrScriptTTSFilePath(buf.toString());
	    		}
	    	}
	    }
	    
	    

    if(resultTempPath != null) {
        //성공시
        jRes.setSuccess(AJaxResVO.SUCCESS_Y); 
		jRes.addAttribute("selectProductList", scriptDetailHistoryList);
        
    } else { 
        //실패시
        jRes.setSuccess(AJaxResVO.SUCCESS_N);
           jRes.addAttribute("msg","no result"); 
        } 

		return jRes;

	}

	@RequestMapping(value = "/selectTree_Script.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectTree_Script(HttpServletRequest request,
			@RequestParam("pkarray[]") String[] arr) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//	  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
		String[] pkarray = arr;

		Integer result = 0;
		logger.info("pkarray : " + pkarray);
		Integer tempResult = 0;
		for (int i = 0; i < pkarray.length; i++) {
			scriptRegistrationInfo = new ScriptRegistrationInfo();

			String[] param = pkarray[i].split("//");
			scriptRegistrationInfo.setrScriptStepPk((param[0]));

		}
		List<ScriptRegistrationInfo> selectTree_Script = scriptRegistrationService
				.selectTree_Script(scriptRegistrationInfo);

		if (selectTree_Script != null && selectTree_Script.size() > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectTree_Script", selectTree_Script);

		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}
		return jRes;

	}
	public static String getValidPath(String path) throws Exception{
		
		if(path == null || "".equals(path)) {
			throw new Exception("path is null");
		}
		
			if(path.indexOf("..") > -1) {
				throw new Exception("path contains ..");
			}
			if(path.indexOf("..") > -1) {
				throw new Exception("path contains // : ");
			}
		
		
		return path;
		
	}
	@RequestMapping(value = "/updateScriptVariable.do", method = { RequestMethod.POST, RequestMethod.GET })
	@Transactional
	public @ResponseBody AJaxResVO updateScriptVariable(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
		String updateDate = null;
		String updateType = null;
		if (!StringUtil.isNull(request.getParameter("valuePk"), true))
			scriptRegistrationInfo.setrProductValuePk(Integer.parseInt(request.getParameter("valuePk")));
		if (!StringUtil.isNull(request.getParameter("productCode"), true)) {
			scriptRegistrationInfo.setrProductCode(request.getParameter("productCode"));
			scriptRegistrationInfo.setrProductReserveCode(request.getParameter("productCode"));
		}
		if (!StringUtil.isNull(request.getParameter("type"), true)) {
			if(request.getParameter("type").equals("펀드")) {
				scriptRegistrationInfo.setrProductReserveType("2");				
				scriptRegistrationInfo.setrProductType("2");
			}			
			if(request.getParameter("type").equals("신탁")) {
				scriptRegistrationInfo.setrProductReserveType("1");				
				scriptRegistrationInfo.setrProductType("1");
			}
			
		}
		if (!StringUtil.isNull(request.getParameter("valueCode"), true))
			scriptRegistrationInfo.setrProductValueCode(request.getParameter("valueCode"));
		if (!StringUtil.isNull(request.getParameter("valueName"), true))
			scriptRegistrationInfo.setrProductValueName(request.getParameter("valueName"));
		if (!StringUtil.isNull(request.getParameter("valueVal"), true))
			scriptRegistrationInfo.setrProductValueVal(request.getParameter("valueVal"));
		if (!StringUtil.isNull(request.getParameter("reserveDate"), true)) {
			scriptRegistrationInfo.setrProductReserveDate(request.getParameter("reserveDate"));
			updateDate= request.getParameter("reserveDate");
		}
		if (!StringUtil.isNull(request.getParameter("updateType"), true)) {
			updateType = request.getParameter("updateType");
		}
		
		
		
		List<ScriptRegistrationInfo> reserveDataList = null;
		
		reserveDataList = scriptRegistrationService.selectRsProductReserveFromProductCode(scriptRegistrationInfo);
		boolean flag1 = false; 
		boolean flag2 = false; 
		//예약반영인경우
		updateType = Optional.ofNullable(updateType).orElse("");
		if(updateType.equals("2")) {
			//예약잡힌 테이블이 아닌경우 정상insert
			if(reserveDataList.size() == 0  || reserveDataList == null ) {
				// 그냥 추가
				int reserveInsertInt = scriptRegistrationService.insertRsProductReserve(scriptRegistrationInfo);
				if(reserveInsertInt > 0)
					flag1 = true;
					
			}else { //이미 있는경우 update는 최신화해야한다.
				ScriptRegistrationInfo originalData = reserveDataList.get(0);
				if (!StringUtil.isNull(request.getParameter("reserveDate"), true))
					originalData.setrProductReserveDate(request.getParameter("reserveDate"));
				
				int reserveUpdateInt = scriptRegistrationService.updateProdcutReservceDate(originalData);
				if(reserveUpdateInt > 0)
					flag1 = true;
			}
		}
		//즉시반영인 경우
			if(updateType.equals("1")) {
				// 해당 상품코드엑관환 예약 테이블을 rs_use_yn을 N으로 바꾸어야한다?
				if(reserveDataList != null || reserveDataList.size() != 0){
					int result = scriptRegistrationService.updateProductReserveRsUseYn(scriptRegistrationInfo);
					
				jRes.addAttribute("resultType" , "direct");		
			}
		
		//가변값 업데이트
			int valueUpdateInt = scriptRegistrationService.updateProductValueNameAndVal(scriptRegistrationInfo);
			if(valueUpdateInt > 0)
				flag2 = true;
		
			if(updateType.equals("1") && flag2) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("msg", "가변 데이터를 성공적으로 변경하였습니다.");			
			}
			
			if (updateType.equals("2") && flag1 && flag2) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("msg", "가변 데이터를 성공적으로 변경하였습니다.\n 예약일자 ["+updateDate+"]");
			jRes.addAttribute("resultType" , "reserve");		
			
			} else {
			// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "가변 데이터를 수정하는데 실패하였습니다.");
				jRes.addAttribute("resultType" , "reserve");		
			}		
		}
		return jRes;
	}
		
	// 스크립트 세부내용 불러올떄 각 상품코드에 맞는 가변값 불러오기
			@RequestMapping(value = "/selectProductChangeList.do", method = { RequestMethod.POST, RequestMethod.GET })
			public @ResponseBody AJaxResVO selectProductChangeList(HttpServletRequest request) throws IOException {
				AJaxResVO jRes = new AJaxResVO();
				LoginVO userInfo = SessionManager.getUserInfo(request);
			  if (userInfo != null) {
				ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
	;
			//상품타입
				if (!StringUtil.isNull(request.getParameter("rProductType"), true))
					scriptRegistrationInfo.setrProductType(request.getParameter("rProductType"));
			//상품코드
				if (!StringUtil.isNull(request.getParameter("rProductCode"), true))
					scriptRegistrationInfo.setrProductCode(request.getParameter("rProductCode"));

				List<ScriptRegistrationInfo> selectProductChangeList = scriptRegistrationService.selectProductChangeList(scriptRegistrationInfo);

				if (selectProductChangeList != null && selectProductChangeList.size() > 0) {
					// 성공시
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("selectProductChangeList", selectProductChangeList);

				} else {
					// 실패시
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "no result");
				}

				  } else {
				  jRes.setResult("0");
				  logInfoService.writeLog(request, "Etc - Logout"); 
			  }
				  jRes.addAttribute("msg", "login fail");
		return jRes;
	}
			
			@RequestMapping(value = "/getCommonScriptDetail.do", method = { RequestMethod.POST, RequestMethod.GET })
			public @ResponseBody AJaxResVO getCommonScriptDetail(HttpServletRequest request) {	
			
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			
			if (userInfo != null) {
				ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();
				
				
				scriptRegistrationInfo.setrScriptCommonPk(request.getParameter("selectRowNumPk"));
				logger.info(request.getParameter("selectRowNumPk"));
									
				List<ScriptRegistrationInfo> searchCommonScriptDetail = scriptRegistrationService.searchCommonScriptDetail(scriptRegistrationInfo);

				if (searchCommonScriptDetail != null && searchCommonScriptDetail.size() > 0) {
					// 성공시
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("searchCommonScriptDetail", searchCommonScriptDetail);

				} else {
					// 실패시
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.addAttribute("msg", "no result");
				}
			} else {
				jRes.setResult("0");

				jRes.addAttribute("msg", "login fail");

				logInfoService.writeLog(request, "Etc - Logout");
			}

			return jRes;
		}
	
	private String getPrdRiskGdNm(scriptProductValueInfo variableData) {
		String name = null;
		if(variableData.getRsProductValueVal().contains("1")) {
			name = "매우높은위험";
		}
		if(variableData.getRsProductValueVal().contains("2")) {
			name = "높은 위험";
		}
		if(variableData.getRsProductValueVal().contains("3")) {
			name = "다소 높은 위험";
		}
		if(variableData.getRsProductValueVal().contains("4")) {
			name = "보통위험";
		}
		if(variableData.getRsProductValueVal().contains("5")) {
			name = "낮은위험";
		}	
		return name;
	}
	
	
	//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
		@RequestMapping(value = "/selectValueComTTS.do", method = { RequestMethod.POST, RequestMethod.GET })
		public @ResponseBody AJaxResVO selectValueComTTS(HttpServletRequest request) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
//		  if (userInfo != null) {
			ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

			List<ScriptRegistrationInfo> selectValueComTTS = scriptRegistrationService.selectValueComTTS(scriptRegistrationInfo);

			if (selectValueComTTS != null && selectValueComTTS.size() > 0) {
				// 성공시
				jRes.setResult("Sucess");
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				jRes.addAttribute("selectValueComTTS", selectValueComTTS);

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}

			return jRes;

		}
		
		//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
		@RequestMapping(value = "/stepAppend/{id}", method = { RequestMethod.POST, RequestMethod.GET })
		public @ResponseBody AJaxResVO adminSelectScriptStep(HttpServletRequest request,@PathVariable("id") String tKey) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);

			List<ScriptStepVo> stepPkList = scriptRegistrationService.adminSelectScriptStep(tKey);
			
			
			if (stepPkList != null && stepPkList.size() > 0) {
				// 성공시
				jRes.setResult("Sucess");
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				
				jRes.addAttribute("stepList", stepPkList);
				
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
			
			return jRes;
			
		}
		//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
		@RequestMapping(value = "/stepDetailAppend/{id}", method = { RequestMethod.POST, RequestMethod.GET })
		public @ResponseBody AJaxResVO adminSelectScriptStepDetail(HttpServletRequest request,@PathVariable("id") String tKey) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			
			List<AdminScriptStepDetailInfo> stepPkList = scriptRegistrationService.adminSelectScriptStepDetail(tKey);
			
			
			if (stepPkList != null && stepPkList.size() > 0) {
				// 성공시
				jRes.setResult("Sucess");
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				
				jRes.addAttribute("stepDetailList", stepPkList);
				
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
			
			return jRes;
			
		}
		//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
		@RequestMapping(value = "/stepDetailUpdate", method = { RequestMethod.PUT})
		public @ResponseBody AJaxResVO stepDetailUpdate(HttpServletRequest request,@RequestBody RequestAdminScriptUpdateInfo info) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			
			int result = scriptRegistrationService.adminUpdateScriptDetail(info);
			
			if (result == 1) {
				// 성공시
				jRes.setResult("Sucess");
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
			
			return jRes;
			
		}
		//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
		@RequestMapping(value = "/inserSinkTable", method = { RequestMethod.POST})
		public @ResponseBody AJaxResVO stepDetailUpdate(HttpServletRequest request,@RequestBody RequestSinkTableInfo sinkInfo) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.setResult("Sucess");
			
			try {
				scriptRegistrationService.insertSinkTable(sinkInfo);
			} catch (Exception e) {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
			
			
			return jRes;
			
		}
		//script_value에서 TTS가 Y인 값 가져오기(실시간 여부)
		@RequestMapping(value = "/deleteProduct/{pk}", method = { RequestMethod.POST})
		public @ResponseBody AJaxResVO stepDetailUpdate(HttpServletRequest request,@PathVariable(value = "pk") String pk) throws IOException {
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			
			int result = 0;
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.setResult("Sucess");
			try {
				result = scriptRegistrationService.deleteProductListToPk(pk);
			} catch (Exception e) {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
			return jRes;
			
		}

		//productInsert, VO DAO
		@Autowired
			private SqlSession sqlSession;
		@RequestMapping(value="/adminProductInsert",method=RequestMethod.POST,produces="application/text; charset=utf8")
		@ResponseBody
		public String productInsert(HttpServletRequest hsr) {
			AdminProductInsertDAO listInsert=sqlSession.getMapper(AdminProductInsertDAO.class);
			String pType=hsr.getParameter("productType");
			String pCode=hsr.getParameter("productCode");
			String pName=hsr.getParameter("productName");
			String useYn=hsr.getParameter("useYn");
			String groupYn=hsr.getParameter("groupYn");
			
			
			String gCode=hsr.getParameter("groupCode");
			if(groupYn.equals("N")) {
				gCode = null;
			}else {
				if(gCode.trim().length() == 0) {
					gCode = null;
				}
			}
			String sysType=hsr.getParameter("sysdisType");
			String pAttr=hsr.getParameter("productAttr");
			if(pAttr.trim().length() == 0) {
				pAttr = null;
			}
			listInsert.doInsertProduct(pType, pCode, pName, useYn, groupYn, gCode, sysType, pAttr);
			listInsert.executeProcdure();
			return "ok";
			
		}

}// 끝
