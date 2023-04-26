package com.furence.recsee.audioCalibration.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.util.ConvertUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;

@Controller
public class AjaxAudioCalibrationController {
	private static final Logger logger = LoggerFactory.getLogger(AjaxAudioCalibrationController.class);
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LogInfoService logInfoService;

	// 전사 관리 검색 항목
	@RequestMapping(value="/face_rec_search_item.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO face_rec_search_item(HttpServletRequest request, HttpServletResponse response) {
		
		Locale setLocale = null;
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		Cookie[] cookie = request.getCookies();
		for(int c=0; c<cookie.length; c++) {
			if(cookie[c].getName().contains("unqLang")) {
				setLocale = new Locale(cookie[c].getValue());
				localeResolver.setLocale(request,response,setLocale);
				java.util.Locale.setDefault(setLocale);
			}			
		}
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();		
		
		if(userInfo != null) {
			String htmlString = "";
			String preFixName = "views.search.grid.head.";
			for(int i=0; i < 3; i++) {
				switch(i) {
				case 0:
					htmlString+=
					"<input title=\""+messageSource.getMessage("search.option.startDate", null,Locale.getDefault())+"\" maxlength=\"8\" type=\"text\" id=\"sDate\" class=\"ui_input_cal icon_input_cal inputFilter numberFilter dateFilter\" fieldset=\"fDate\" placeholder=\""+messageSource.getMessage("search.option.startDate", null,Locale.getDefault())+"\">"+
					"<input title=\""+messageSource.getMessage("search.option.endDate", null,Locale.getDefault())+"\" maxlength=\"8\" type=\"text\" id=\"eDate\" class=\"ui_input_cal icon_input_cal inputFilter numberFilter dateFilter\" fieldset=\"fDate\" placeholder=\""+messageSource.getMessage("search.option.endDate", null,Locale.getDefault())+"\">";
					break;
				case 1: 
					htmlString+=
					"<input title=\""+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"1", null,Locale.getDefault())+"\" maxlength=\"6\" id=\"sTime\" type=\"text\" class=\"input_time inputFilter numberFilter timeFilter\" fieldset=\"fTime\" placeholder=\""+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"1", null,Locale.getDefault())+"\"/>"+
					"<input title=\""+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"2", null,Locale.getDefault())+"\" maxlength=\"6\" id=\"eTime\" type=\"text\" class=\"input_time inputFilter numberFilter timeFilter\" fieldset=\"fTime\" placeholder=\""+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"2", null,Locale.getDefault())+"\"/>";
//					"<select title=\""+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"1", null,Locale.getDefault())+"\" id=\"sTimeS\" class=\"sel_time\" fieldset=\"fTime\" required loadUrl=\"/selectOption.do?comboType=Time&comboType2=s\">"+
//						"<option value='' selected>"+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"1", null,Locale.getDefault())+"</option>"+
//                    "</select>"+
//                    "<select title=\""+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"2", null,Locale.getDefault())+"\" id=\"eTimeS\" class=\"sel_time\" fieldset=\"fTime\" required loadUrl=\"/selectOption.do?comboType=Time&comboType2=s\">"+
//						"<option value='' selected>"+messageSource.getMessage(preFixName+"r_rec_time".toUpperCase()+"2", null,Locale.getDefault())+"</option>"+
//					"</select>";
					break;
				case 2:
					htmlString+=
					"<input title=\""+messageSource.getMessage(preFixName+"r_v_filename".toUpperCase(), null,Locale.getDefault())+"\" type=\"text\" id=\""+ConvertUtil.convert2CamelCase("file_name")+"\" class=\"inputFilter \" fieldset=\"fRecInfo\" placeholder=\""+messageSource.getMessage(preFixName+"r_v_filename".toUpperCase(), null,Locale.getDefault())+"\">";
					break;
				}
			}
			htmlString+=
			"<button id=\"searchBtn\" class=\"ui_main_btn_flat icon_btn_search_white\" >"+messageSource.getMessage("views.search.button.search", null,Locale.getDefault())+"</button>";

			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.setResult("1");
			jRes.addAttribute("htmlString", htmlString);

			logInfoService.writeLog(request, "Etc - Logout");
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
	
			logInfoService.writeLog(request, "Etc - Logout");
		}
		return jRes;
	}
	
	// 전사 관리 검색 항목
	@RequestMapping(value="/doAudioCalibration.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO doAudioCalibration(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		
		Locale setLocale = null;
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		Cookie[] cookie = request.getCookies();
		for(int c=0; c<cookie.length; c++) {
			if(cookie[c].getName().contains("unqLang")) {
				setLocale = new Locale(cookie[c].getValue());
				localeResolver.setLocale(request,response,setLocale);
				java.util.Locale.setDefault(setLocale);
			}			
		}
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		
		String paramStr = "";
		if(!StringUtil.isNull(request.getParameter("noise"),true)) {
			paramStr += "&noise="+request.getParameter("noise");
		}
		if(!StringUtil.isNull(request.getParameter("highFrequency"),true)) {
			paramStr += "&highFrequency="+request.getParameter("highFrequency");
		}
		if(!StringUtil.isNull(request.getParameter("volumeUp"),true)) {
			paramStr += "&volumeUp="+request.getParameter("volumeUp");
		}
		if(!StringUtil.isNull(request.getParameter("lowFrequency"),true)) {
			paramStr += "&lowFrequency="+request.getParameter("lowFrequency");
		}
		if(!StringUtil.isNull(request.getParameter("filePath"),true)) {
			String filePath = request.getParameter("filePath");
			if (filePath.indexOf("/listen?url=") > -1) {
				filePath = filePath.substring(filePath.indexOf("/listen?url=")+12);
			}
			paramStr += "&filePath="+filePath;
		}

		if(!"".equals(paramStr)) {
			paramStr = paramStr.substring(1);
		}
		if(userInfo != null) {
			try {
				String url = "http://localhost:28881/audioCalibration?" + paramStr;
				String result = httpConnection(url);
				
				if(!"".equals(result)) {
					result = "http://localhost:28881/listen?url=" + result;
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("listenUrl", result);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("0");
					jRes.addAttribute("msg", "NO RESULT");
				}
			} catch (Exception e) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "DOWN ERROR");
			} 
		}else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
	
			logInfoService.writeLog(request, "Etc - Logout");
		}
		return jRes;
	}
	
	public static String httpConnection(String targetUrl) {
		URL url = null;
		HttpURLConnection conn = null;
		String jsonData = "";
		BufferedReader br = null;
		StringBuffer sb = null;
		String returnText = "";

		try {
			url = new URL(targetUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.connect();

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			sb = new StringBuffer();

			while ((jsonData = br.readLine()) != null) {
				sb.append(jsonData);
			}

			returnText = sb.toString();

		} catch (IOException e) {
			logger.error("error",e);
		} catch (Exception e) {
			logger.error("error",e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (conn != null)
					conn.disconnect();
			} catch (IOException e) {
				logger.error("error",e);
			}
		}

		return returnText;
	}
}
