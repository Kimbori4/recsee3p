package com.furence.recsee.myfolder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.myfolder.model.MyFolderInfo;
import com.furence.recsee.myfolder.model.MyFolderListinfo;
import com.furence.recsee.myfolder.service.MyFolderService;

@Controller
public class AjaxMyfolderController {

	@Autowired
	MyFolderService myFolderService;
	
	@RequestMapping(value="/selectMyfolder.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO selectMyfolder(HttpServletRequest request) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		if(userInfo != null) {
			MyFolderInfo myfolderInfo = new MyFolderInfo();
			myfolderInfo.setrUserId(userInfo.getUserId());
			String myFolderList = "";
			List <MyFolderInfo> myFolder =  myFolderService.selectMyFolderInfo(myfolderInfo);
			if(myFolder.size()>0) {
				for(int i=0; i<myFolder.size(); i++) {
					myFolderList += "<option value="+myFolder.get(i).getrFolderName()+">"+myFolder.get(i).getrFolderName()+"</option>";
				}
				jRes.addAttribute("myFolderList", myFolderList);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			}else {
				jRes.setResult("myFolder is not exist");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}else{
			jRes.setResult("LOGIN FAIL");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;		
	}
	
	@RequestMapping(value="/insertMyfolderItem.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO insertMyfolderItem(HttpServletRequest request, Locale local, Model model,@RequestParam Map<String,String> params) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		List<String> recDateArr = new ArrayList<String>();
		List<String> recTimeArr = new ArrayList<String>();
		List<String> extArr = new ArrayList<String>();
		for(String s : params.get("recDateArr").split(",")) { recDateArr.add(s); }
		for(String s : params.get("recTimeArr").split(","))	{ recTimeArr.add(s); }
		for(String s :params.get("extArr").split(",")) {	extArr.add(s); }
		
		try {
			for(int i = 0 ; i < recDateArr.size() ; i++) {
				MyFolderListinfo myFolderListInfo = new MyFolderListinfo();
				myFolderListInfo.setrUserId(userInfo.getUserId());
				myFolderListInfo.setrFolderName((!StringUtil.isNull(params.get("rFolderName"))?params.get("rFolderName"):""));
				myFolderListInfo.setrRecDate(recDateArr.get(i).replaceAll("-",""));
				myFolderListInfo.setrRecTime(recTimeArr.get(i).replaceAll(":",""));
				myFolderListInfo.setrExtNum(extArr.get(i));
				Integer test = myFolderService.insertMyFolderItem(myFolderListInfo);
			}
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	@RequestMapping(value="/moveMyfolderItem.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO moveMyfolderItem(HttpServletRequest request, Locale local, Model model,@RequestParam("rFolderName") String rfolderName) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		List<String> itemIndex = new ArrayList<String>();
		
		for(String s : request.getParameter("itemIndex").split(",")) {	itemIndex.add(s); }
		try {
			MyFolderListinfo myFolderListInfo = new MyFolderListinfo();
			myFolderListInfo.setrUserId(userInfo.getUserId());
			myFolderListInfo.setrFolderName((!StringUtil.isNull(rfolderName)?rfolderName:""));
			myFolderListInfo.setrItemSerialArr(itemIndex);
			Integer moveItem = myFolderService.moveMyFolderItem(myFolderListInfo);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	@RequestMapping(value="/deleteMyfolderItem.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO deleteMyfolderItem(HttpServletRequest request, Locale local, Model model,@RequestParam("rFolderName") String folderName,
			@RequestParam("itemIndex") String index) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		List<String> itemIndex = new ArrayList<String>();
		
		for(String s : index.split(",")) {	itemIndex.add(s); }
		try {
			MyFolderListinfo myFolderListInfo = new MyFolderListinfo();
			myFolderListInfo.setrUserId(userInfo.getUserId());
			myFolderListInfo.setrFolderName((!StringUtil.isNull(folderName)?folderName:""));
			myFolderListInfo.setrItemSerialArr(itemIndex);
			Integer deleteItem = myFolderService.deleteMyFolderItem(myFolderListInfo);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	@RequestMapping(value="/createMyfolder.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO createMyfolder(HttpServletRequest request, Locale local, Model model,@RequestParam("folderName") String folderName) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		MyFolderInfo myFolderInfo = new MyFolderInfo();
		myFolderInfo.setrUserId(userInfo.getUserId());
		myFolderInfo.setrFolderName(!StringUtil.isNull(folderName)?folderName:"");
		try {
			myFolderService.createMyFolder(myFolderInfo);	
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	@RequestMapping(value="/modifyMyfolder.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO modifyMyfolder(HttpServletRequest request, Locale local, Model model,@RequestParam("folderName") String folderName
			,@RequestParam("oldrFolderName") String oldFolderName) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		MyFolderInfo myFolderInfo = new MyFolderInfo();
		myFolderInfo.setrUserId(userInfo.getUserId());
		myFolderInfo.setrFolderName(!StringUtil.isNull(folderName)?folderName:"");
		myFolderInfo.setOldrFolderName(!StringUtil.isNull(oldFolderName)?oldFolderName:"");
		
		try {
			myFolderService.modifyMyFolder(myFolderInfo);	
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	@RequestMapping(value="/deleteMyfolder.do",produces = "text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET }) 
	public @ResponseBody AJaxResVO deleteMyfolder(HttpServletRequest request, Locale local, Model model , @RequestParam("folderName") String folderName) { 
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		MyFolderInfo myFolderInfo = new MyFolderInfo();
		myFolderInfo.setrUserId(userInfo.getUserId());
		myFolderInfo.setrFolderName(folderName);
		
		try {
			myFolderService.deleteMyFolder(myFolderInfo);	
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

}
