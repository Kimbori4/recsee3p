package com.furence.recsee.transcript.controller;


import org.springframework.stereotype.Controller;

public class AjaxTranscriptController {}

	/*@ResponseBody
	@RequestMapping("/modelReleaseProc")
	public AJaxResVO modelReleaseProc(HttpServletRequest request) {
		
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		if(userInfo != null) {
			try{
				SttModel sttModel =  new SttModel();
				sttModel.setrModelName(request.getParameter("rModelName"));
				sttModel.setrType(request.getParameter("rType"));
				sttModel.setrApplyYn("N");
				transcriptService.updateApplySttModel(sttModel);
				
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("msg", "ReleaseSuccess");
				jRes.setResult("Model Release Success");
			}catch(Exception e){
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "ReleaseFail");
				jRes.setResult("Model Release Fail");
			}
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "LoginFail");
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}*/

