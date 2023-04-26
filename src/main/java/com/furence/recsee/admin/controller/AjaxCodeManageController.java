package com.furence.recsee.admin.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.PlayListInfo;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.CommonCodeService;
import com.furence.recsee.common.service.CustomerInfoService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.PlayListInfoService;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;

@Controller
public class AjaxCodeManageController {

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private PlayListInfoService playerListInfoService;

	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private CustomerInfoService customerInfoService;
	
	@Autowired
	private MenuAccessInfoService menuAccessInfoService;



	@RequestMapping(value = "/playerCodeChange.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playerCodeChange(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			CommonCodeVO commonCode = new CommonCodeVO();
			commonCode.setParentCode("player");

			if(!StringUtil.isNull(request.getParameter("rCodeVal"))) {
				commonCode.setCodeValue(request.getParameter("rCodeVal"));
			}
			if(!StringUtil.isNull(request.getParameter("rChangeVal"))) {
				commonCode.setCodeName(request.getParameter("rChangeVal"));
			}

			Integer codeUpdate = commonCodeService.updateCommonCode(commonCode);

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	@RequestMapping(value = "/expirationCodeChange.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO expirationCodeChange(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("APPROVE");
			etcConfigInfo.setConfigKey("DUE_DATE");


			if(!StringUtil.isNull(request.getParameter("rChangeVal"))) {
				etcConfigInfo.setConfigValue(request.getParameter("rChangeVal"));
			}

			Integer result=etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);


			if(result==1) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("msg", "update success");
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult(String.valueOf(result));
				jRes.addAttribute("msg", "update fail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	@RequestMapping(value = "/playerDownloadSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playerDownloadSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("download");
			etcConfigInfo.setConfigKey(request.getParameter("rId"));
			if("true".equals(request.getParameter("state"))) {
				etcConfigInfo.setConfigValue("Y");
			}else {
				etcConfigInfo.setConfigValue("N");
			}

			Integer result=etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);


			if(result==1) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("msg", "update success");
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult(String.valueOf(result));
				jRes.addAttribute("msg", "update fail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}
	
	@RequestMapping(value = "/addCode.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO addCode(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			CodeInfoVO codeinfo= new CodeInfoVO();
			Integer result=0;
			if(!StringUtil.isNull(request.getParameter("gridUrl"))&&request.getParameter("gridUrl").equals("oraganizationManage")) {

				if(!StringUtil.isNull(request.getParameter("code"))) {
					codeinfo.setrBgCode(request.getParameter("code"));
				}
				if(!StringUtil.isNull(request.getParameter("codeName"))) {
					codeinfo.setrBgName(request.getParameter("codeName"));
				}
				if(!StringUtil.isNull(request.getParameter("useYn"))) {
					codeinfo.setUseYn(request.getParameter("useYn"));
				}
				result=customerInfoService.insertBgInfo(codeinfo);
			}else {
				if(!StringUtil.isNull(request.getParameter("code"))) {
					codeinfo.setrMgCode(request.getParameter("code"));
				}
				if(!StringUtil.isNull(request.getParameter("codeName"))) {
					codeinfo.setrMgName(request.getParameter("codeName"));
				}
				if(!StringUtil.isNull(request.getParameter("useYn"))) {
					codeinfo.setUseYn(request.getParameter("useYn"));
				}
				result=customerInfoService.insertMgInfo(codeinfo);
			}

			if(result==1) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("msg", "insert success");
			}else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult(String.valueOf(result));
				jRes.addAttribute("msg", "insert fail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	@RequestMapping(value = "/bgCodeSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO bgCodeSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		OrganizationInfo bgcode= new OrganizationInfo();

		if(userInfo != null) {
			if(!StringUtil.isNull(request.getParameter("bgCode"),true)) {
				bgcode.setrBgCode(request.getParameter("bgCode"));
			}
			if(!StringUtil.isNull(request.getParameter("temp"),true)) {
				bgcode.setType(request.getParameter("temp"));
			}
			if(!StringUtil.isNull(request.getParameter("state"),true)) {
				String temp="";
				if(request.getParameter("state").equals("true"))
					temp="Y";
				else
					temp="N";
				bgcode.setState(temp);
			}

			Integer result = organizationInfoService.updateUseBg(bgcode);

			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.setResult(String.valueOf(result));
			jRes.addAttribute("msg", "success");
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "fail");
		}

		return jRes;
	}

	@RequestMapping(value = "/playerCodeSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playerCodeSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {

			CommonCodeVO commonCode = new CommonCodeVO();
			commonCode.setParentCode("player");
			List<CommonCodeVO> codeList = commonCodeService.selectCommonCode(commonCode);

			jRes.addAttribute("codeList", codeList);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	//	플레이어 리스트  사용 유무
	@RequestMapping(value = "/playerListUseYn.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playerListSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			PlayListInfo rsPlayListInfo = new PlayListInfo();
			rsPlayListInfo.setrUserId(userInfo.getUserId());

			List<PlayListInfo> playListUseYn  = playerListInfoService.selectUsePlayList(rsPlayListInfo);
			if(playListUseYn.size()==0) {
				Integer insertUsePlayList =  playerListInfoService.insertUsePlayList(rsPlayListInfo);
				jRes.addAttribute("playListUse", "Y");
			}else {
				jRes.addAttribute("playListUse", playListUseYn.get(0).getrUseYn());
			}

			List<PlayListInfo> playListSelect = playerListInfoService.playListSelect(rsPlayListInfo);
			jRes.addAttribute("playList",playListSelect);

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}


	//	플레이어 리스트  ONOFF CHECK
	@RequestMapping(value = "/playerListOnOff.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playerListOnOff(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			PlayListInfo rsPlayListInfo = new PlayListInfo();
			rsPlayListInfo.setrUserId(userInfo.getUserId());
			if(!StringUtil.isNull(request.getParameter("onOffCheck")))
				rsPlayListInfo.setrUseYn(request.getParameter("onOffCheck"));

			Integer updateUsePlayList = playerListInfoService.updateUsePlayList(rsPlayListInfo);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}
	//	플레이어 리스트 select
	@RequestMapping(value = "/playListSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playListSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			PlayListInfo rsPlayListInfo = new PlayListInfo();
			rsPlayListInfo.setrUserId(userInfo.getUserId());

			List<PlayListInfo> playListsize= playerListInfoService.playListSelect(rsPlayListInfo);

			jRes.addAttribute("playList", playListsize);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	//	플레이어 리스트 insert
	@RequestMapping(value = "/playerListInsert.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playerListInsert(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			PlayListInfo rsPlayListInfo = new PlayListInfo();
			rsPlayListInfo.setrUserId(userInfo.getUserId());

			if(!StringUtil.isNull(request.getParameter("listenUrl")))
				rsPlayListInfo.setRecUrl(request.getParameter("recDate").replaceAll("-", "")+request.getParameter("recTime").replaceAll(":", "")+request.getParameter("recExt"));

			List<PlayListInfo> playListsize= playerListInfoService.playListSelect(rsPlayListInfo);
			rsPlayListInfo.setrListOrder(String.valueOf(playListsize.size()+1));

			if(!StringUtil.isNull(request.getParameter("recDate")))
				rsPlayListInfo.setrRecDate(request.getParameter("recDate"));

			if(!StringUtil.isNull(request.getParameter("recTime")))
				rsPlayListInfo.setrRecTime(request.getParameter("recTime"));

			if(!StringUtil.isNull(request.getParameter("recExt")))
				rsPlayListInfo.setrExtNum(request.getParameter("recExt"));
			else
				rsPlayListInfo.setrExtNum("");

			if(!StringUtil.isNull(request.getParameter("recCustPhone")))
				rsPlayListInfo.setrCustPhone1(request.getParameter("recCustPhone"));
			else
				rsPlayListInfo.setrCustPhone1("");

			if(!StringUtil.isNull(request.getParameter("recUserName")))
				rsPlayListInfo.setrUserNameCall(request.getParameter("recUserName"));
			else
				rsPlayListInfo.setrUserNameCall("");

			if(!StringUtil.isNull(request.getParameter("recvFileName")))
				rsPlayListInfo.setrVFileName(request.getParameter("recvFileName"));

			if(!StringUtil.isNull(request.getParameter("recMemo")))
				rsPlayListInfo.setrMemoInfo(request.getParameter("recMemo"));
			else
				rsPlayListInfo.setrMemoInfo("");

			Integer playListinsert = playerListInfoService.playListInsert(rsPlayListInfo);

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	@RequestMapping(value = "/playListDelete.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playListDelete(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			PlayListInfo rsPlayListInfo = new PlayListInfo();
			rsPlayListInfo.setrUserId(userInfo.getUserId());
			String [] itemList=null;

			if(!StringUtil.isNull(request.getParameter("delItem"))) {
				itemList = request.getParameter("delItem").split(",");
				for(int i=0; i<itemList.length;i++) {
					rsPlayListInfo.setRecUrl(itemList[i]);
					playerListInfoService.playListDelete(rsPlayListInfo);
				}
			}
			
			List<PlayListInfo> playListsize= playerListInfoService.playListSelect(rsPlayListInfo);

			int playListSize = playListsize.size();

			//	순서 정렬
			for(int i =0 ; i < playListSize ; i++) {
				int columnNum = i+1;
				PlayListInfo rsPlayNumInto = new PlayListInfo();

				rsPlayNumInto.setrUserId(userInfo.getUserId());
				rsPlayNumInto.setRecUrl(playListsize.get(i).getRecUrl());
				rsPlayNumInto.setrListOrder(String.valueOf(columnNum));

				Integer playNumUpdate = playerListInfoService.playListUpdateNum(rsPlayNumInto);
			}


		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	@RequestMapping(value = "/playListSort.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO playListSort(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {

			PlayListInfo rsPlayListInfo = new PlayListInfo();
			rsPlayListInfo.setrUserId(userInfo.getUserId());

			List<PlayListInfo> playListsize= playerListInfoService.playListSelect(rsPlayListInfo);

			int playListSize = playListsize.size();

			if(!StringUtil.isNull(request.getParameter("sortItem")))
				rsPlayListInfo.setRecUrl(request.getParameter("sortItem"));

			if(!StringUtil.isNull(request.getParameter("listOrder")))
				rsPlayListInfo.setrListOrder(request.getParameter("listOrder"));

			Integer playNumUpdate = playerListInfoService.playListUpdateNum(rsPlayListInfo);

		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}


	@RequestMapping(value = "/CallCenterCodeSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO CallCenterCodeSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("CALLCENTER");
			etcConfigInfo.setConfigKey("CALLCENTER");


			if(!StringUtil.isNull(request.getParameter("rst")))
				etcConfigInfo.setConfigValue(request.getParameter("rst"));

			Integer etcConfigResult = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);

		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}


	@RequestMapping(value = "/channalTypeCodeSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO channalTypeCodeSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("START_TYPE");


			if(!StringUtil.isNull(request.getParameter("rst")))
				etcConfigInfo.setConfigValue(request.getParameter("rst"));

			Integer etcConfigResult = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);

		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
	@RequestMapping(value = "/maskingCodeSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO maskingCodeSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SEARCH");
			etcConfigInfo.setConfigKey("MASKING_INFO");


			if(!StringUtil.isNull(request.getParameter("rst")))
				etcConfigInfo.setConfigValue(request.getParameter("rst"));

			Integer etcConfigResult = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);

		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}

	@RequestMapping(value = "/directGroupSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO directGroupSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if(userInfo != null) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("APPROVE");
			etcConfigInfo.setConfigKey("DIRECTGROUP");


			if(!StringUtil.isNull(request.getParameter("rst")))
				etcConfigInfo.setConfigValue(request.getParameter("rst"));

			Integer etcConfigResult = etcConfigInfoService.updateEtcConfigInfo(etcConfigInfo);

		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}	
	
}
