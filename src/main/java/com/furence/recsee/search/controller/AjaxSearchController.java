package com.furence.recsee.search.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.furence.recsee.admin.model.AllowableRangeInfo;
import com.furence.recsee.admin.model.CustomizeItemInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;
import com.furence.recsee.admin.model.MultiPartInfo;
import com.furence.recsee.admin.model.PublicIpInfo;
import com.furence.recsee.admin.service.AllowableRangeInfoService;
import com.furence.recsee.admin.service.CustomizeInfoService;
import com.furence.recsee.admin.service.PublicIpInfoService;
import com.furence.recsee.admin.service.RUserInfoService;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.common.service.CommonCodeService;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.LogService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.service.OrganizationInfoService;
import com.furence.recsee.common.service.RecMemoService;
import com.furence.recsee.common.util.AesUtil;
import com.furence.recsee.common.util.ConvertUtil;
import com.furence.recsee.common.util.DateUtil;
import com.furence.recsee.common.util.ExcelView;
import com.furence.recsee.common.util.FindOrganizationUtil;
import com.furence.recsee.common.util.RecSeeUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.XssFilterUtil;
import com.furence.recsee.main.model.ApproveListInfo;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.service.SearchListInfoService;
import com.initech.shttp.server.Logger;

@Controller
public class AjaxSearchController {

	@Autowired
	private CommonCodeService commonCodeService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private CustomizeInfoService customizeInfoService;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private SearchListInfoService searchListInfoService;

	@Autowired
	private OrganizationInfoService organizatinoInfoService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private AllowableRangeInfoService allowableRangeInfoService;

	@Autowired
	private RUserInfoService ruserInfoService;

	@Autowired
	private RecMemoService recMemoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LogService logService;

	@Autowired
	private PublicIpInfoService publicIpInfoService;

	/*
	 * @RequestMapping(value = "/getVideo.do", method = RequestMethod.GET) public
	 * String getContentMediaVideo(HttpServletRequest request, HttpServletResponse
	 * response) throws IOException {
	 * 
	 * // progressbar 에서 특정 위치를 클릭하거나 해서 임의 위치의 내용을 요청할 수 있으므로 // 파일의 임의의 위치에서 읽어오기
	 * 위해 RandomAccessFile 클래스를 사용한다. // 해당 파일이 없을 경우 예외 발생
	 * 
	 * File file = null; Boolean isUrl = false; String path =
	 * request.getSession().getServletContext()
	 * .getRealPath("/resources/component/recsee_player/data_sample/500.mp4");
	 * 
	 * if (!StringUtil.isNull(request.getParameter("url"))) { path =
	 * request.getParameter("url"); URL url = new URL(path); file =
	 * File.createTempFile("temp_", ".mp4"); FileUtils.copyURLToFile(url, file);
	 * isUrl = true; } file = new File(path);
	 * 
	 * RandomAccessFile randomFile = new RandomAccessFile(file, "r"); long
	 * rangeStart = 0; // 요청 범위의 시작 위치 long rangeEnd = 0; // 요청 범위의 끝 위치 boolean
	 * isPart = false; // 부분 요청일 경우 true, 전체 요청의 경우 false // randomFile 을 클로즈 하기 위하여
	 * try~finally 사용 try { // 동영상 파일 크기 long movieSize = randomFile.length(); //
	 * 스트림 요청 범위, request의 헤더에서 range를 읽는다. String range =
	 * request.getHeader("range"); // 브라우저에 따라 range 형식이 다른데, 기본 형식은
	 * "bytes={start}-{end}" 형식이다. // range가 null이거나, reqStart가 0이고 end가 없을 경우 전체
	 * 요청이다. // 요청 범위를 구한다. if (range != null) { // 처리의 편의를 위해 요청 range에 end 값이 없을
	 * 경우 넣어줌 if (range.endsWith("-")) { range = range + (movieSize - 1); } int idxm
	 * = range.trim().indexOf("-"); // "-" 위치 rangeStart =
	 * Long.parseLong(range.substring(6, idxm)); rangeEnd =
	 * Long.parseLong(range.substring(idxm + 1)); if (rangeStart > 0) { isPart =
	 * true; } } else { // range가 null인 경우 동영상 전체 크기로 초기값을 넣어줌. 0부터 시작하므로 -1
	 * rangeStart = 0; rangeEnd = movieSize - 1; } // 전송 파일 크기 long partSize =
	 * rangeEnd - rangeStart + 1; // 전송시작 response.reset(); // 전체 요청일 경우 200, 부분 요청일
	 * 경우 206을 반환상태 코드로 지정 response.setStatus(isPart ? 206 : 200); // mime type 지정
	 * response.setContentType("video/mp4"); // 전송 내용을 헤드에 넣어준다. 마지막에 파일 전체 크기를 넣는다.
	 * response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd +
	 * "/" + movieSize); response.setHeader("Accept-Ranges", "bytes");
	 * response.setHeader("Content-Length", "" + partSize); OutputStream out =
	 * response.getOutputStream(); // 동영상 파일의 전송시작 위치 지정
	 * randomFile.seek(rangeStart); // 파일 전송... java io는 1회 전송 byte수가 int로 지정됨 //
	 * 동영상 파일의 경우 int형으로는 처리 안되는 크기의 파일이 있으므로 // 8kb로 잘라서 파일의 크기가 크더라도 문제가 되지 않도록 구현
	 * int bufferSize = 8 * 1024; byte[] buf = new byte[bufferSize]; do { int block
	 * = partSize > bufferSize ? bufferSize : (int) partSize; int len =
	 * randomFile.read(buf, 0, block); out.write(buf, 0, len); partSize -= block; }
	 * while (partSize > 0); } catch (IOException e) { Logger.error("", "", "",
	 * e.toString()); // 전송 중에 브라우저를 닫거나, 화면을 전환한 경우 종료해야 하므로 전송취소. // progressBar를
	 * 클릭한 경우에는 클릭한 위치값으로 재요청이 들어오므로 전송 취소. } finally { randomFile.close(); if
	 * (isUrl) file.deleteOnExit(); } return null; }
	 */

	// 플레이어 wave 파형 받아오기
	@RequestMapping(value = "/getWaveArray.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO getWaveArray(HttpServletRequest request) throws IOException {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		String UserId = "out user";
		if (userInfo != null)
			UserId = userInfo.getUserId();

		String dir = request.getParameter("path");
		AJaxResVO jRes = new AJaxResVO();

		URL u = new URL(dir);
		InputStream in = u.openStream();

		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			sb.append(new String(b, 0, n));
		}

		List<String> array = new ArrayList<String>();
		String data[] = sb.toString().split(",");

		for (int i = 0; i < data.length; i++) {
			if (!StringUtil.isNull(data[i], true))
				array.add(data[i]);
		}

		if (array.size() > 0) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("waveArray", array);

			logInfoService.writeLog(request, "Rec - Listen Record File Sucsess", UserId);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("NO-DATA");
		}
		return jRes;
	}

	@RequestMapping(value = "/getRecFileTest.do", method = { RequestMethod.POST, RequestMethod.GET })
	public void getRecFileTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

		FileInputStream in = null;
		BufferedOutputStream outs = null;

		response.setHeader("Content-Type", "audio/mp3");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Accept-Ranges", "bytes");

		String path = request.getSession().getServletContext().getRealPath("") + File.separator + "resources"
				+ File.separator + "component" + File.separator + "recsee_player" + File.separator + "data_sample"
				+ File.separator;
		try {
			//String fileName = request.getParameter("fileName");
			File file = new File(path + "beef.mp3");
			response.setHeader("Content-Length", "" + file.length());
			in = new FileInputStream(file);
			outs = new BufferedOutputStream(response.getOutputStream());
			int len;
			byte[] buf = new byte[4096];
			while ((len = in.read(buf)) > 0) {
				outs.write(buf, 0, len);
			}
		} catch (FileNotFoundException fe) {
			Logger.error("", "", "", fe.toString());
		} catch (Exception e) {
			Logger.error("", "", "", e.toString());
		} finally {
			if (outs != null) {
				outs.close();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e2) {
					Logger.error("", "", "", e2.toString());
				}
			}
		}
	}
	/*
	 * @RequestMapping(value = "/getRecFile.do", method = { RequestMethod.POST,
	 * RequestMethod.GET }) public @ResponseBody AJaxResVO
	 * getRecFile(HttpServletRequest request, HttpServletResponse response) throws
	 * IOException {
	 * 
	 * AJaxResVO jRes = new AJaxResVO();
	 * 
	 * String referer = request.getHeader("referer"); if
	 * (referer.indexOf("search/search") > -1) referer = "searchNListen"; else
	 * referer = "ecnerufnothingfurence";
	 * 
	 * @SuppressWarnings("unchecked") List<MMenuAccessInfo> sessAccessList =
	 * (List<MMenuAccessInfo>) request.getSession().getAttribute("AccessInfo");
	 * MMenuAccessInfo nowAccessInfo = new
	 * MMenuAccessInfo().getNowAccessRow(sessAccessList, referer); String fileName =
	 * request.getParameter("fileName"); if (nowAccessInfo.getDownloadYn() != null
	 * && nowAccessInfo.getDownloadYn().toUpperCase().equals("Y")) { EtcConfigInfo
	 * decryptionYn = new EtcConfigInfo(); decryptionYn.setGroupKey("download");
	 * decryptionYn.setConfigKey("decryption"); List<EtcConfigInfo>
	 * decryptionYnResult = etcConfigInfoService.selectEtcConfigInfo(decryptionYn);
	 * String decryptionResult = decryptionYnResult.get(0).getConfigValue();
	 * 
	 * // url에서 파일 받아와 리턴해주는 로직 URL url = new URL(request.getParameter("url") +
	 * "?decryption=" + (StringUtil.isNull(decryptionResult, true) ||
	 * decryptionResult.equals("y") ? "y" : "n"));
	 * 
	 * response.setHeader("Content-Disposition", "attachment; filename=" +
	 * fileName); InputStream is = url.openStream();
	 * 
	 * BufferedOutputStream outs = new
	 * BufferedOutputStream(response.getOutputStream()); int len; byte[] buf = new
	 * byte[4096]; while ((len = is.read(buf)) > 0) { outs.write(buf, 0, len); }
	 * logInfoService.writeLog(request, "Rec - File download Success", fileName);
	 * jRes.setSuccess(AJaxResVO.SUCCESS_Y); jRes.setResult("1");
	 * jRes.addAttribute("msg", "File download Success"); outs.close(); } else {
	 * logInfoService.writeLog(request, "Rec - File download Fail (Authy denied)",
	 * fileName); jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "File download Fail (Authy denied)"); } return jRes;
	 * }
	 */
	// 우수콜 편집파일 다운로드
	/*
	 * @RequestMapping(value = "/getlisten.do", produces = "text/plain;")
	 * public @ResponseBody AJaxResVO getlisten(HttpServletRequest request,
	 * HttpServletResponse response, Locale local, Model model) throws IOException {
	 * AJaxResVO jRes = new AJaxResVO(); LoginVO userInfo =
	 * SessionManager.getUserInfo(request); if (userInfo != null) { SearchListInfo
	 * searchListInfo = new SearchListInfo();
	 * 
	 * if (!StringUtil.isNull(request.getParameter("ip"), true) &&
	 * !StringUtil.isNull(request.getParameter("saveIp"), true)) {
	 * 
	 * if (!StringUtil.isNull(request.getParameter("userId"), true))
	 * searchListInfo.setUserId(request.getParameter("userId")); if
	 * (!StringUtil.isNull(request.getParameter("url"), true))
	 * searchListInfo.setUrl(request.getParameter("url")); if
	 * (!StringUtil.isNull(request.getParameter("cmd"), true))
	 * searchListInfo.setCmd(request.getParameter("cmd")); if
	 * (!StringUtil.isNull(request.getParameter("mStartTime"), true))
	 * searchListInfo.setmStartTime(request.getParameter("mStartTime")); if
	 * (!StringUtil.isNull(request.getParameter("mEndTime"), true))
	 * searchListInfo.setmEndTime(request.getParameter("mEndTime")); if
	 * (!StringUtil.isNull(request.getParameter("cStartTime"), true))
	 * searchListInfo.setcStartTime(request.getParameter("cStartTime")); if
	 * (!StringUtil.isNull(request.getParameter("cEndTime"), true))
	 * searchListInfo.setcEndTime(request.getParameter("cEndTime")); if
	 * (!StringUtil.isNull(request.getParameter("totalLength"), true))
	 * searchListInfo.setTotalLength(request.getParameter("totalLength")); if
	 * (!StringUtil.isNull(request.getParameter("encYn"), true))
	 * searchListInfo.setEncYn(request.getParameter("encYn")); if
	 * (!StringUtil.isNull(request.getParameter("format"), true))
	 * searchListInfo.setEncYn(request.getParameter("format"));
	 * 
	 * URL url = new URL("http://" + request.getParameter("ip") + ":28881/down?url="
	 * + searchListInfo.getUrl() + "&cmd=" + searchListInfo.getCmd() +
	 * "&mStartTime=" + searchListInfo.getmStartTime() + "&mEndTime=" +
	 * searchListInfo.getmEndTime() + "&cStartTime=" +
	 * searchListInfo.getcStartTime() + "&cEndTime=" + searchListInfo.getcEndTime()
	 * + "&totalLength=" + searchListInfo.getTotalLength() + "&encYn=" +
	 * searchListInfo.getEncYn() + "&format=mp3&downform=zip");
	 * 
	 * // url = new // URL(
	 * "http://172.18.120.154:28881/down?url=C%3A%2FUsers%2Fuser%2FDesktop%2Flisten.mp3&cmd=cut_off_mute&mStartTime=1.006508875739645&mEndTime=11.57485207100592&cStartTime=17.110650887573964&cEndTime=338.6902366863905&totalLength=340.2&encYn=false"
	 * ); HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	 * conn.setDoOutput(true); OutputStream outstream = null; String path = null;
	 * Date today = new Date(); SimpleDateFormat date = new
	 * SimpleDateFormat("yyyyMMdd"); SimpleDateFormat time = new
	 * SimpleDateFormat("HHmmss"); String filename = date.format(today) +
	 * time.format(today) + searchListInfo.getUserId() + ".mp3"; try { InputStream
	 * in = conn.getInputStream(); File newFile = null; File newFileDir = null;
	 * 
	 * // 우수콜 저장경로 EtcConfigInfo bestcallPath = new EtcConfigInfo();
	 * bestcallPath.setGroupKey("UPLOAD");
	 * bestcallPath.setConfigKey("BESTCALL_FILE_PATH"); List<EtcConfigInfo>
	 * decryptionYnResult = etcConfigInfoService.selectEtcConfigInfo(bestcallPath);
	 * path = decryptionYnResult.get(0).getConfigValue(); // 윈도우 서버일 경우 if
	 * (OsCheck.isWindow()) { newFile = new File(path, filename); newFileDir = new
	 * File(path);
	 * 
	 * if (!newFileDir.exists()) { newFileDir.mkdirs(); } } else { // 리눅스 서버일 경우
	 * newFile = new File(path, filename); newFileDir = new File(path); if
	 * (!newFileDir.exists()) { newFileDir.mkdirs(); }
	 * Runtime.getRuntime().exec("chmod 777" + path + filename);
	 * newFile.setExecutable(true, false); newFile.setReadable(true, false);
	 * newFile.setWritable(true, false); }
	 * 
	 * outstream = new FileOutputStream(newFile); byte[] buf = new byte[4096]; int
	 * length = 0; while ((length = in.read(buf)) != -1) { outstream.write(buf, 0,
	 * length); }
	 * 
	 * } catch (Exception e) { jRes.setSuccess(AJaxResVO.SUCCESS_N);
	 * jRes.setResult("0"); jRes.addAttribute("msg", "DOWN ERROR"); } finally {
	 * outstream.close(); conn.disconnect(); } BestCallInfo bestCallInfo = new
	 * BestCallInfo();
	 * 
	 * bestCallInfo.setrShareDate(date.format(today));
	 * bestCallInfo.setrShareTime(time.format(today));
	 * bestCallInfo.setIp(request.getParameter("saveIp")); if
	 * (!StringUtil.isNull(request.getParameter("userId"), true))
	 * bestCallInfo.setrUserId(request.getParameter("userId"));
	 * 
	 * if (!StringUtil.isNull(request.getParameter("userName"), true))
	 * bestCallInfo.setrUserName(request.getParameter("userName")); if
	 * (!StringUtil.isNull(request.getParameter("bgCode"), true))
	 * bestCallInfo.setBgCode(request.getParameter("bgCode")); if
	 * (!StringUtil.isNull(request.getParameter("mgCode"), true))
	 * bestCallInfo.setMgCode(request.getParameter("mgCode")); if
	 * (!StringUtil.isNull(request.getParameter("sgCode"), true))
	 * bestCallInfo.setSgCode(request.getParameter("sgCode"));
	 * 
	 * if (!StringUtil.isNull(request.getParameter("shareTitle"), true))
	 * bestCallInfo.setrShareTitle(request.getParameter("shareTitle"));
	 * 
	 * bestCallInfo.setrSharePath(filename);
	 * 
	 * Integer result = bestCallInfoService.insertBestCall(bestCallInfo); if (result
	 * != 1) { jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg1", "Query Error"); } else
	 * jRes.setSuccess(AJaxResVO.SUCCESS_Y); logService.writeLog(request,
	 * "BESTCALL", "REQUEST", bestCallInfo.toLogString(messageSource)); } else {
	 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "NO SEARCH IP"); }
	 * 
	 * } else { jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("0");
	 * jRes.addAttribute("msg", "NOT LOGIN"); } return jRes; }
	 */

	// @RequestMapping(value = "/getlisten.do", produces = "audio/mp3;")
	// public @ResponseBody void getLog(HttpServletRequest request,
	// HttpServletResponse response, Locale local, Model model) throws IOException {
	// LoginVO userInfo = SessionManager.getUserInfo(request);
	// if(userInfo != null) {
	// String referer = request.getHeader("referer");
	//
	// boolean nowAcc =false;
	//
	// if (referer.indexOf("search/search") > -1)
	// nowAcc = nowAccessChk2(request,"searchNListen");
	// else
	// nowAcc = nowAccessChk3(request,"approveList.approveList");
	//
	// if(nowAcc) {
	//
	// SearchListInfo searchListInfo = new SearchListInfo();
	// if(!StringUtil.isNull(request.getParameter("recDate"), true))
	// searchListInfo.setRecDateRaw(request.getParameter("recDate"));
	// if(!StringUtil.isNull(request.getParameter("recTime"), true))
	// searchListInfo.setRecTimeRaw(request.getParameter("recTime"));
	// if(!StringUtil.isNull(request.getParameter("extNum"), true))
	// searchListInfo.setExtNum(request.getParameter("extNum"));
	//
	// List<SearchListInfo> searchListResult =
	// searchListInfoService.selectFullPath(searchListInfo);
	//
	// if(searchListResult.size()==1) {
	// URL url = null;
	// if(!StringUtil.isNull(request.getParameter("RX"))) {
	// url = new
	// URL("http://"+searchListResult.get(0).getvRecIp()+":28881/listen?url="+searchListResult.get(0).getvRecFullpath()+".RX.mp3");
	// }else if(!StringUtil.isNull(request.getParameter("TX"))) {
	// url = new
	// URL("http://"+searchListResult.get(0).getvRecIp()+":28881/listen?url="+searchListResult.get(0).getvRecFullpath()+".TX.mp3");
	// }else {
	// url = new
	// URL("http://"+searchListResult.get(0).getvRecIp()+":28881/listen?url="+searchListResult.get(0).getvRecFullpath());
	// }
	// response.setHeader("Content-Type", "audio/mp3");
	// response.setHeader("Access-Control-Allow-Origin", "*");
	// response.setHeader("Accept-ranges", "bytes");
	// response.setHeader("Cache-Control", "max-age=0, no-store, must-revalidate");
	// response.setHeader("Pragma", "no-cache");
	// response.setHeader("Expires", "0");
	//
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setDoOutput(true);
	//
	// InputStream in = conn.getInputStream();
	//
	// OutputStream outstream = new FileOutputStream(new File("/temp/test.mp3"));
	//
	// byte[] buf = new byte[4096];
	// int length = 0;
	// while((length = in.read(buf)) != -1) {
	// outstream.write(buf, 0 , length);
	// }
	//
	// outstream.close();
	//// BufferedOutputStream out = new
	// BufferedOutputStream(response.getOutputStream());
	////
	//// byte[] buf = new byte[4096];
	//// int length = 0;
	////
	//// while((length = in.read(buf)) != -1) {
	//// out.write(buf, 0 , length);
	//// }
	// //conn.disconnect();
	// }
	// }
	// }
	// }

	/*
	 * @RequestMapping(value = "/getWave.do", produces =
	 * "application/json;charset=UTF-8") public @ResponseBody String
	 * getWave(HttpServletRequest request, HttpServletResponse response) throws
	 * IOException {
	 * 
	 * LoginVO userInfo = SessionManager.getUserInfo(request); String res = "";
	 * 
	 * if (userInfo != null) { String referer = request.getHeader("referer");
	 * boolean nowAcc = false; if (referer.indexOf("search/search") > -1) nowAcc =
	 * nowAccessChk2(request, "searchNListen"); else nowAcc = nowAccessChk3(request,
	 * "approveList.approveList");
	 * 
	 * if (nowAcc) {
	 * 
	 * SearchListInfo searchListInfo = new SearchListInfo(); if
	 * (!StringUtil.isNull(request.getParameter("recDate"), true))
	 * searchListInfo.setRecDateRaw(request.getParameter("recDate")); if
	 * (!StringUtil.isNull(request.getParameter("recTime"), true))
	 * searchListInfo.setRecTimeRaw(request.getParameter("recTime")); if
	 * (!StringUtil.isNull(request.getParameter("extNum"), true))
	 * searchListInfo.setExtNum(request.getParameter("extNum"));
	 * 
	 * List<SearchListInfo> searchListResult =
	 * searchListInfoService.selectFullPath(searchListInfo);
	 * 
	 * if (searchListResult.size() == 1) { URL url = null; if
	 * (!StringUtil.isNull(request.getParameter("RX"))) { url = new URL("http://" +
	 * searchListResult.get(0).getvRecIp() + ":28881/wave?url=" +
	 * searchListResult.get(0).getvRecFullpath() + ".RX.mp3"); } else if
	 * (!StringUtil.isNull(request.getParameter("TX"))) { url = new URL("http://" +
	 * searchListResult.get(0).getvRecIp() + ":28881/wave?url=" +
	 * searchListResult.get(0).getvRecFullpath() + ".TX.mp3"); } else { url = new
	 * URL("http://" + searchListResult.get(0).getvRecIp() + ":28881/wave?url=" +
	 * searchListResult.get(0).getvRecFullpath() + "&listenType=" +
	 * request.getParameter("listenType")); }
	 * 
	 * Map<String, Object> params = new LinkedHashMap(); if
	 * (!StringUtil.isNull(request.getParameter("RX"))) { params.put("url",
	 * searchListResult.get(0).getvRecFullpath() + ".RX.mp3"); } else if
	 * (!StringUtil.isNull(request.getParameter("TX"))) { params.put("url",
	 * searchListResult.get(0).getvRecFullpath() + ".TX.mp3"); } else {
	 * params.put("url", searchListResult.get(0).getvRecFullpath()); }
	 * 
	 * params.put("listenType", request.getParameter("listenType"));
	 * 
	 * StringBuilder postData = new StringBuilder(); for (Map.Entry<String, Object>
	 * param : params.entrySet()) { if (postData.length() != 0)
	 * postData.append('&'); postData.append(URLEncoder.encode(param.getKey(),
	 * "UTF-8")); postData.append('=');
	 * postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
	 * "UTF-8")); } byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	 * response.setHeader("Content-Type", "application/json");
	 * response.setHeader("Access-Control-Allow-Origin", "*");
	 * response.setHeader("Accept-Ranges", "bytes");
	 * 
	 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	 * conn.setRequestMethod("POST"); conn.setRequestProperty("Content-Type",
	 * "application/x-www-form=urlencoded");
	 * conn.setRequestProperty("Content-Length",
	 * String.valueOf(postDataBytes.length)); conn.setDoOutput(true);
	 * conn.getOutputStream().write(postDataBytes); conn.setConnectTimeout(3000);
	 * 
	 * InputStream in = conn.getInputStream();
	 * 
	 * ByteArrayOutputStream out = new ByteArrayOutputStream();
	 * 
	 * byte[] buf = new byte[1024 * 8]; int length = 0;
	 * 
	 * while ((length = in.read(buf)) != -1) { out.write(buf, 0, length); }
	 * 
	 * res = new String(out.toByteArray(), "UTF-8"); conn.disconnect();
	 * 
	 * } } } return res; }
	 */

	// 조회 검색 항목
	@RequestMapping(value = "/search_item.do", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody AJaxResVO search_item(HttpServletRequest request, HttpServletResponse response) {

		Locale setLocale = null;
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		Cookie[] cookie = request.getCookies();
		for (int c = 0; c < cookie.length; c++) {
			if (cookie[c].getName().contains("unqLang")) {
				setLocale = new Locale(cookie[c].getValue());
				localeResolver.setLocale(request, response, setLocale);
				java.util.Locale.setDefault(setLocale);
			}
		}

		String evalThema = SessionManager.getStringAttr(request, "evalThema");

		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		if (userInfo != null) {

			@SuppressWarnings("unchecked")
			List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) request.getSession()
					.getAttribute("AccessInfo");
			MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "searchNListen");
			String AccessLevel = nowAccessInfo.getAccessLevel();

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("EXCEPT");
			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String consentNoRecodingUse = "N";
			if (etcConfigResult.size() > 0) {
				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
			}

			CustomizeItemInfo customizeItemInfo = new CustomizeItemInfo();
			if (userInfo != null)
				customizeItemInfo.setrUserId(userInfo.getUserLevel());

			List<CustomizeItemInfo> searchSettingInfo = customizeInfoService.selectCustomizeItemInfo(customizeItemInfo);

			Integer settingInfoTotal = searchSettingInfo.size();
			if (settingInfoTotal < 1) {
				customizeItemInfo.setrUserId("default");

				searchSettingInfo = customizeInfoService.selectCustomizeItemInfo(customizeItemInfo);

				settingInfoTotal = searchSettingInfo.size();
			}

			if (settingInfoTotal > 0) {

				// xmls.setItemElements(new ArrayList<SearchItemRecordElement>());

				/**
				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
				 */
				Map<String, String> searchTemplItem = null;
				if (settingInfoTotal > 1) {
					for (int i = 0; i < settingInfoTotal; i++) {
						if (userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
							searchTemplItem = searchSettingInfo.get(i).getAllItem();
					}
				} else {
					searchTemplItem = searchSettingInfo.get(0).getAllItem();
				}
				Iterator<String> keys = null;
				if (searchTemplItem != null && searchTemplItem.keySet() != null) {
					keys = searchTemplItem.keySet().iterator();
				}

				/*
				 * Calendar cal = Calendar.getInstance(); String currentDate =
				 * String.valueOf(cal.get(Calendar.YEAR)); currentDate += "-" + ( (
				 * cal.get(Calendar.MONTH) + 1 ) < 10 ? "0" : "" ) + String.valueOf(
				 * cal.get(Calendar.MONTH) + 1 ); currentDate += "-" + ( cal.get(Calendar.DATE)
				 * < 10 ? "0" : "" ) + String.valueOf(cal.get(Calendar.DATE));
				 */

				EtcConfigInfo etcParm = new EtcConfigInfo();
				etcParm.setGroupKey("statistics");
				etcParm.setGroupKey("INLINE_LENGTH");
				List<EtcConfigInfo> inlineLength = etcConfigInfoService.selectEtcConfigInfo(etcParm); // inlineLength =
																										// 5

				String htmlString = "";
				String preFixName = "views.search.grid.head.";
				while (keys != null && keys.hasNext()) {
					String key = keys.next();

					if (key.equals("rUserId"))
						continue;

					if (searchTemplItem.get(key) != null) {

						String keyName = searchTemplItem.get(key);
						String keyId = searchTemplItem.get(key).substring(2);

						switch (keyName) {
						// 셀렉트 박스 옵션은 loadUrl attr로 만들어서 js에서 loadUrl호출하여 붙여줌
						// dhx구조를 보고 싶다면, XmlSearchController.java 참고 하면댐 ..헤헤..
						case "r_rec_date":
							htmlString += "<input title=\""
									+ messageSource.getMessage("search.option.startDate", null, Locale.getDefault())
									+ "\" maxlength=\"8\" type=\"text\" id=\"sDate\" class=\"ui_input_cal icon_input_cal inputFilter numberFilter dateFilter\" fieldset=\"fDate\" placeholder=\""
									+ messageSource.getMessage("search.option.startDate", null, Locale.getDefault())
									+ "\">" + "<input title=\""
									+ messageSource.getMessage("search.option.endDate", null, Locale.getDefault())
									+ "\" maxlength=\"8\" type=\"text\" id=\"eDate\" class=\"ui_input_cal icon_input_cal inputFilter numberFilter dateFilter\" fieldset=\"fDate\" placeholder=\""
									+ messageSource.getMessage("search.option.endDate", null, Locale.getDefault())
									+ "\">";
							break;
						case "r_rec_time":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" id=\"sTimeS\" class=\"sel_time\" fieldset=\"fTime\" required loadUrl=\"/selectOption.do?comboType=Time&comboType2=s&type=search\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "</option>" + "</select>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" id=\"eTimeS\" class=\"sel_time\" fieldset=\"fTime\" required loadUrl=\"/selectOption.do?comboType=Time&comboType2=e&type=search\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_call_stime_connect":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" maxlength=\"6\" id=\"sTimeConnect\" type=\"text\" class=\"input_time inputFilter numberFilter timeFilter\" fieldset=\"fTimeConnect\" placeholder=\""
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "\"/>" + "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" maxlength=\"6\" id=\"eTimeConnect\" type=\"text\" class=\"input_time inputFilter numberFilter timeFilter\" fieldset=\"fTimeConnect\" placeholder=\""
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "2", null, Locale.getDefault())
									+ "\"/>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" id=\"sTimeConnectS\" class=\"sel_time\" fieldset=\"fTimeConnect\" required loadUrl=\"/selectOption.do?comboType=Time&comboType2=s\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "</option>" + "</select>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" id=\"eTimeConnectS\" class=\"sel_time\" fieldset=\"fTimeConnect\" required loadUrl=\"/selectOption.do?comboType=Time&comboType2=s\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_rec_rtime":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" id=\"sRtime\" fieldset=\"fRtime\" required loadUrl=\"/selectOption.do?comboType=rTime&comboType2=s\">"
									+ "<option selected value=''>"
									+ messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" id=\"eRtime\" fieldset=\"fRtime\" required loadUrl=\"/selectOption.do?comboType=rTime&comboType2=s\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_call_kind1":// 녹취키
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" maxlength=\"255\" class=\"inputFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId) + "\" type=\"text\" placeholder=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\"/>";
							break;
						case "r_call_id1":// 녹취키
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" maxlength=\"255\" class=\"inputFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId) + "\" type=\"text\" placeholder=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\"/>";
							break;
						case "r_call_kind2":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fCallKind\" required loadUrl=\"/selectOption.do?comboType=callType\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;

						case "r_bg_code":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fOrgCode\" required loadUrl=\"/organizationSelect.do?comboType=bgCode&subOpt=ALL&accessLevel="
									+ AccessLevel + "\">" + "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;

						case "r_mg_code":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fOrgCode\" required loadUrl=\"/organizationSelect.do?comboType=mgCode&subOpt=ALL&accessLevel=\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;

						case "r_sg_code":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fOrgCode\" required loadUrl=\"/organizationSelect.do?comboType=sgCode&subOpt=ALL&accessLevel=\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_call_ttime":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" maxlength=\"6\" id=\"sTtime\" type=\"text\" class=\"input_time time_to_sec inputFilter numberFilter timeFilter\" fieldset=\"fTtime\" placeholder=\""
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "\"/>" + "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" maxlength=\"6\" id=\"eTtime\" type=\"text\" class=\"input_time time_to_sec inputFilter numberFilter timeFilter\" fieldset=\"fTtime\" placeholder=\""
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "2", null, Locale.getDefault())
									+ "\"/>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" id=\"sTtimeS\" class=\"sel_time\" fieldset=\"fTtime\" required loadUrl=\"/selectOption.do?comboType=tTime&comboType2\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "</option>" + "</select>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" id=\"eTtimeS\" class=\"sel_time\" fieldset=\"fTtime\" required loadUrl=\"/selectOption.do?comboType=tTime&comboType2\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_call_ttime_connect":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" maxlength=\"6\" id=\"sTtimeConnect\" type=\"text\" class=\"input_time time_to_sec inputFilter numberFilter timeFilter\" fieldset=\"fTtimeConnect\" placeholder=\""
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "\"/>" + "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" maxlength=\"6\" id=\"eTtimeConnect\" type=\"text\" class=\"input_time time_to_sec inputFilter numberFilter timeFilter\" fieldset=\"fTtimeConnect\" placeholder=\""
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "2", null, Locale.getDefault())
									+ "\"/>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "1", null,
											Locale.getDefault())
									+ "\" id=\"sTtimeConnectS\" class=\"sel_time\" fieldset=\"fTtimeConnect\" required loadUrl=\"/selectOption.do?comboType=tTime&comboType2\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(
											preFixName + keyName.toUpperCase() + "1", null, Locale.getDefault())
									+ "</option>" + "</select>" + "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "\" id=\"eTtimeConnectS\" class=\"sel_time\" fieldset=\"fTtimeConnect\" required loadUrl=\"/selectOption.do?comboType=tTime&comboType2\">"
									+ "<option selected value=''>"
									+ messageSource.getMessage(preFixName + keyName.toUpperCase() + "2", null,
											Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_queue_no1":
						case "r_queue_no2":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyName)
									+ "\" required loadUrl=\"/queueSelectOption.do?type=ALL\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_selfdis_yn":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" required loadUrl=\"/selectOption.do?comboType=YN\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_rec_start_type":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" required loadUrl=\"/selectOption.do?comboType=startType\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;

						case "r_counsel_result_bgcode":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fCounselResultCode\" required loadUrl=\"/selectOption.do?comboType=common&comboType2=counsel&ALL=all\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_counsel_result_mgcode":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fCounselResultCode\" required loadUrl=\"/selectOption.do?comboType=common&comboType2=\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_counsel_result_sgcode":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" fieldset=\"fCounselResultCode\" required loadUrl=\"/selectOption.do?comboType=common&comboType2=\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_cust_phone1":
						case "r_cust_phone2":
						case "r_cust_phone3":
						case "r_cust_phone_ap":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" maxlength=\"13\"class=\"inputFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fCustInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_stock_no":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" maxlength=\"255\" class=\"inputFilter numberFilter input_stock\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId) + "\" type=\"text\" placeholder=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\"/>";
							break;
						case "r_ext_num":
							String tempLength = "";
							if (inlineLength.size() > 0)
								tempLength = inlineLength.get(0).getConfigValue();
							else
								tempLength = "5";
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" maxlength=\"" + tempLength + "\"class=\"inputFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fUserInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";

							break;
						case "r_user_id":
							htmlString +=
									// "<input title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\" maxlength=\"8\" class=\"inputFilter
									// numberFilter input_userid\" id=\""+ConvertUtil.convert2CamelCase(keyId)+"\"
									// type=\"text\" fieldset=\"fUserInfo\"
									// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\"/>";
									"<input title=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\" maxlength=\"255\" class=\"inputFilter numberFilter\" id=\""
											+ ConvertUtil.convert2CamelCase(keyId)
											+ "\" type=\"text\" fieldset=\"fUserInfo\" placeholder=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\"/>";
							break;
						case "r_user_name":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fUserInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_cust_name":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fCustInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_memo_info":
						case "r_tag":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId) + "\" type=\"text\" placeholder=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\"/>";
							break;
						case "r_v_sys_code":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" required loadUrl=\"/selectOption.do?comboType=system&comboType2=&ALL=all\">"
									+ "<option selected value=''>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_buffer11":
							// 20210216 GS 용으로 바꿈
							htmlString += "<input title=\""
									+ messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fCounselResultCode\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
//							if ("master".equals(evalThema)) {
//								htmlString += "<select title=\""
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//										+ "\" required mode=\"checkbox\">" + "</select>";
//							} else {
//								htmlString += "<select title=\""
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//										+ "\" fieldset=\"fCounselResultCode\" required\">"
//										+ "<option selected value=''>"
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "</option>" + "</select>";// "<input
//																	// title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
//																	// null,Locale.getDefault())+"\" class=\"inputFilter
//																	// korFilter engFilter numberFilter\"
//																	// fieldset=\"fCounselResultCode\"
//																	// id=\""+ConvertUtil.convert2CamelCase(keyId)+"\"
//																	// type=\"text\"
//																	// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
//																	// null,Locale.getDefault())+"\"/>";
//							}

							break;
						case "r_buffer12":
							// 20210216 GS 용으로 바꿈
							htmlString += "<input title=\""
									+ messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fCounselResultCode\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";

//							if ("master".equals(evalThema)) {
//								htmlString += "<select title=\""
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//										+ "\" required mode=\"checkbox\">" + "</select>";
//							} else {
//								htmlString += "<select title=\""
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//										+ "\" fieldset=\"fCounselResultCode\" required\">"
//										+ "<option selected value=''>"
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "</option>" + "</select>";// "<input
//																	// title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
//																	// null,Locale.getDefault())+"\" class=\"inputFilter
//																	// korFilter engFilter numberFilter\"
//																	// fieldset=\"fCounselResultCode\"
//																	// id=\""+ConvertUtil.convert2CamelCase(keyId)+"\"
//																	// type=\"text\"
//																	// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
//																	// null,Locale.getDefault())+"\"/>";
//							}

							break;
						case "r_buffer13":
							// 20210216 GS 용으로 바꿈
							htmlString += "<input title=\""
									+ messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fCounselResultCode\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";

//							if ("master".equals(evalThema)) {
//								htmlString += "<select title=\""
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//										+ "\" required mode=\"checkbox\">" + "</select>";
//							} else {
//								htmlString += "<select title=\""
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//										+ "\" fieldset=\"fCounselResultCode\" required\">"
//										+ "<option selected value=''>"
//										+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//												Locale.getDefault())
//										+ "</option>" + "</select>";// "<input
//																	// title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
//																	// null,Locale.getDefault())+"\" class=\"inputFilter
//																	// korFilter engFilter numberFilter\"
//																	// fieldset=\"fCounselResultCode\"
//																	// id=\""+ConvertUtil.convert2CamelCase(keyId)+"\"
//																	// type=\"text\"
//																	// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
//																	// null,Locale.getDefault())+"\"/>";
//							}

							break;
						case "r_buffer14":
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" required loadUrl=\"/selectOption.do?comboType=buffer14\">"
									+ "<option value='' selected>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";

//									"<input title=\""
//											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
//													Locale.getDefault())+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
//											+ "\" type=\"text\" fieldset=\"fCounselResultCode\" placeholder=\"" + messageSource
//													.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
//											+ "\"/>";
							break;
						case "r_cust_social_num":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fCustInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_counsel_code":
						case "r_buffer1":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fOrgCode\" placeholder=\"" + messageSource
									.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_buffer2":// 지점명 임시
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fOrgCode\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_buffer3":
						case "r_buffer4":// 상품유형
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" required fieldset=\"product\" loadUrl=\"/selectOption.do?comboType=buffer4\">"
									+ "<option value='' selected>" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;
						case "r_product_type":// 스크립트 구분
							htmlString += "<select title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" required fieldset=\"product\" >"
									+ "<option value='' selected>전체</option>"
									+ "<option value=\"T\">투자 성향 분석</option>"
									+ "<option value=\"S\">상품 설명</option>"
									+ "</select>";
							break;
						case "r_buffer5":
						case "r_buffer6":
						case "r_buffer7":// 상품코드
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"product\" placeholder=\"" + messageSource.getMessage(
											preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_buffer8":// 상품명
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"product\" placeholder=\"" + messageSource.getMessage(
											preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_buffer9":

						case "r_buffer17":
						case "r_buffer18":
						case "r_buffer19":
						case "r_buffer20":
							htmlString +=
									// "<input title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\" class=\"inputFilter korFilter engFilter
									// numberFilter\" fieldset=\"fCounselResultCode\"
									// id=\""+ConvertUtil.convert2CamelCase(keyId)+"\" type=\"text\"
									// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\"/>";
									"<select title=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
											+ "\" fieldset=\"fCounselResultCode\" required loadUrl=\"/selectOption.do?comboType=callType\">"
											+ "<option selected value=''>"
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "</option>" + "</select>";
							break;
						// 서비스 구분(천재교과서)
						case "r_buffer15":
							htmlString +=
									// "<input title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\" class=\"inputFilter korFilter engFilter
									// numberFilter\" fieldset=\"fCounselResultCode\"
									// id=\""+ConvertUtil.convert2CamelCase(keyId)+"\" type=\"text\"
									// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\"/>";
									"<select title=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
											+ "\" fieldset=\"fCounselResultCode\" required loadUrl=\"/selectOption.do?comboType=common&comboType2=SERVICE&ALL=all\">"
											+ "<option disabled selected>"
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "</option>" + "</select>";
							break;
						// 서비스 구분(천재교과서)
						case "r_buffer16":
							htmlString +=
									// "<input title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\" class=\"inputFilter korFilter engFilter
									// numberFilter\" fieldset=\"fCounselResultCode\"
									// id=\""+ConvertUtil.convert2CamelCase(keyId)+"\" type=\"text\"
									// placeholder=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\"/>";
									"<select title=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
											+ "\" fieldset=\"fCounselResultCode\" required loadUrl=\"/selectOption.do?comboType=common&comboType2=RECEIPT&ALL=all\">"
											+ "<option disabled selected>"
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "</option>" + "</select>";
							break;
						// VOC 구분(천재교과서)
						case "r_buffer10":
							htmlString +=
									// "<select title=\""+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"\" id=\""+ConvertUtil.convert2CamelCase(keyId)+"\"
									// required
									// loadUrl=\"/selectOption.do?comboType=common&comboType2=VOC&ALL=all\">"+
									// "<option disabled
									// selected>"+messageSource.getMessage(preFixName+keyName.toUpperCase(),
									// null,Locale.getDefault())+"</option>"+
									// "</select>";
									"<input title=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
											+ ConvertUtil.convert2CamelCase(keyId)
											+ "\" type=\"text\" fieldset=\"fCustInfo\" placeholder=\""
											+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
													Locale.getDefault())
											+ "\"/>";
							break;
						// 카테고리 검색창
						case "r_rec_category":
							htmlString += "<select id=\"" + ConvertUtil.convert2CamelCase("recCategory")
									+ "\" fieldset=\"fText\" class=\"codeList codefilter_select\"></select>";
							break;
						// 키워드 검색창
						case "r_rec_keyword":
							htmlString += "<select id=\"" + ConvertUtil.convert2CamelCase("recKeyword")
									+ "\" fieldset=\"fText\" class=\"codeList codefilter_select\"></select>";
							break;
						// 텍스트 검색창
						case "r_rec_text":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase("recText")
									+ "\" type=\"text\" fieldset=\"fText\" placeholder=\"" + messageSource.getMessage(
											preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						// 20200421 jbs 자번호 정보 검색 추가 및 fieldset 추가 *start
						case "r_company_telno":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fSubNumberInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;
						case "r_company_telno_nick":
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"\" id=\"" + ConvertUtil.convert2CamelCase(keyId)
									+ "\" type=\"text\" fieldset=\"fSubNumberInfo\" placeholder=\"" + messageSource
											.getMessage(preFixName + keyName.toUpperCase(), null, Locale.getDefault())
									+ "\"/>";
							break;

						case "r_stt_result":
							htmlString += "<select title=\""
									+ messageSource.getMessage("views.sttResult.label.rResult", null,
											Locale.getDefault())
									+ "\" id=\"" + ConvertUtil.convert2CamelCase("stt_result")
									+ "\" fieldset=\"fSttResult\" required loadUrl=\"/selectOption.do?comboType=SttResult\">"
									+ "<option value='' selected>"
									+ messageSource.getMessage("views.sttResult.label.all", null, Locale.getDefault())
									+ "</option>" + "</select>";
							break;

						// 자번호 정보 검색 추가 및 fieldset 추가 *end
						// 전사여부 검색
						default:
							htmlString += "<input title=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\" class=\"inputFilter korFilter engFilter numberFilter\" id=\""
									+ ConvertUtil.convert2CamelCase(keyId) + "\" type=\"text\" placeholder=\""
									+ messageSource.getMessage(preFixName + keyName.toUpperCase(), null,
											Locale.getDefault())
									+ "\"/>";
							break;
						}
					}
				}

				etcConfigInfo.setGroupKey("SEARCH");
				etcConfigInfo.setConfigKey("CSV_SEARCH_AUTH");
				List<EtcConfigInfo> csvAuth = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo); // csv 조회 권한
				String csvAuthYN = "N";

				if (csvAuth.size() > 0) {
					String[] csvAuthList = csvAuth.get(0).getConfigValue().split(",");
					for (int i = 0; i < csvAuthList.length; i++) {
						if (csvAuthList[i].equals(userInfo.getUserLevel())) {
							csvAuthYN = "Y";
						}
					}
				}

				etcConfigInfo.setGroupKey("SEARCH");
				etcConfigInfo.setConfigKey("CSV_SEARCH");
				List<EtcConfigInfo> csvYN = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo); // csv 조회 기능 사용 여부

				if (csvYN.size() > 0 && "Y".equals(csvYN.get(0).getConfigValue()) && "Y".equals(csvAuthYN)) {
					htmlString += "<button id=\"searchBtn\" class=\"ui_main_btn_flat icon_btn_search_white\" >"
							+ messageSource.getMessage("views.search.button.search", null, Locale.getDefault())
							+ "</button>" + "<button id=\"searchClear\" class=\"ui_main_btn_flat\" >"
							+ messageSource.getMessage("message.btn.clear", null, Locale.getDefault()) + "</button>"
							+ "<button id=\"searchCSV\" class=\"ui_main_btn_flat\" >"
							+ messageSource.getMessage("message.btn.csv", null, Locale.getDefault()) + "</button>";
				} else {
					htmlString += "<button id=\"searchBtn\" class=\"ui_main_btn_flat icon_btn_search_white\" >"
							+ messageSource.getMessage("views.search.button.search", null, Locale.getDefault())
							+ "</button>" + "<button id=\"searchClear\" class=\"ui_main_btn_flat\" >"
							+ messageSource.getMessage("message.btn.clear", null, Locale.getDefault()) + "</button>";
				}

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("1");
				jRes.addAttribute("htmlString", htmlString);

				logInfoService.writeLog(request, "Etc - Logout");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");
		}
		return jRes;
	}

	// 청취, 다운 권한 신청하기
	@RequestMapping(value = "/approve_proc.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO approve_proc(HttpServletRequest request) throws IOException {

		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		if (userInfo != null) {
			// try {
			String type = request.getParameter("type");
			if ("insert".equals(type)) {

				ApproveListInfo approveListInfo = new ApproveListInfo();

				if (!StringUtil.isNull(request.getParameter("approveDay"), true)) {
					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("APPROVE");
					etcConfigInfo.setConfigKey("DUE_DATE");
					List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					if (etcConfigResult.size() > 0) {
						if (Integer.parseInt(request.getParameter("approveDay")) > Integer
								.parseInt(etcConfigResult.get(0).getConfigValue())) {
							jRes.setSuccess(AJaxResVO.SUCCESS_N);
							jRes.setResult("overDate");
							jRes.addAttribute("result", etcConfigResult.get(0).getConfigValue());
							return jRes;
						}
					}
				}

				approveListInfo.setUserId(userInfo.getUserId());
				approveListInfo.setUserName(userInfo.getUserName());
				approveListInfo.setBgCode(userInfo.getBgCode());
				approveListInfo.setMgCode(userInfo.getMgCode());
				approveListInfo.setSgCode(userInfo.getSgCode());
				String state = "0";

				EtcConfigInfo APPROVE = new EtcConfigInfo();
				APPROVE.setGroupKey("APPROVE");
				APPROVE.setConfigKey("APPROVEALCNT");
				List<EtcConfigInfo> APPROVEResult = etcConfigInfoService.selectEtcConfigInfo(APPROVE);

				if (APPROVEResult.size() > 0) {

					String approveData = APPROVEResult.get(0).getConfigValue();

					switch (approveData) {
					case "3":
						state = "0";
						break;
					case "2":
						state = "2";
						break;
					case "1":
						state = "4";
						break;
					default:
						state = "0";
						break;
					}

				} else {
					APPROVE.setConfigValue("3");
					APPROVE.setDesc("청취 및 파일 다운로드 승인 권한 카운터");
					APPROVE.setConfigOption("1/2/3");

					etcConfigInfoService.insertEtcConfigInfo(APPROVE);
				}

				approveListInfo.setApproveState(state);

				if (!StringUtil.isNull(request.getParameter("sgName"), true))
					approveListInfo.setSgName(request.getParameter("sgName"));

				if (!StringUtil.isNull(request.getParameter("listenUrl"), true))
					approveListInfo.setListenUrl(request.getParameter("listenUrl"));

				if (!StringUtil.isNull(request.getParameter("approveType"), true))
					approveListInfo.setApproveType(request.getParameter("approveType"));

				if (!StringUtil.isNull(request.getParameter("approveReason"), true))
					approveListInfo.setApproveReason(request.getParameter("approveReason"));

				if (!StringUtil.isNull(request.getParameter("approveDay"), true))
					approveListInfo.setApproveDay(request.getParameter("approveDay"));

				if (!StringUtil.isNull(request.getParameter("FileColumnKey"), true))
					approveListInfo.setFileColumnKey(request.getParameter("FileColumnKey"));

				if (!StringUtil.isNull(request.getParameter("FileColumnValue"), true))
					approveListInfo.setFileColumnValue(request.getParameter("FileColumnValue"));

				SearchListInfo recfile = new SearchListInfo();
				recfile.setRecTimeRaw(request.getParameter("recTime").replaceAll(":", ""));
				recfile.setExtNum(request.getParameter("recExt"));
				recfile.setRecDateRaw(request.getParameter("recDate").replaceAll("-", ""));
				if (!StringUtil.isNull(request.getParameter("FileColumnValue"), true)
						&& !StringUtil.isNull(request.getParameter("FileColumnKey"), true)) {
					recfile.setFileNameChange(request.getParameter("FileColumnValue"));
				}

				List<ApproveListInfo> selectResult = searchListInfoService.selectApproveInfoByFileName(recfile);

				approveListInfo.setCustName(selectResult.get(0).getCustName());
				approveListInfo.setCustPhone1(selectResult.get(0).getCustPhone1());
				approveListInfo.setStockNo(selectResult.get(0).getStockNo());
				approveListInfo.setRecExt(selectResult.get(0).getExtNum());
				approveListInfo.setRecDate(selectResult.get(0).getRecDate().replaceAll("-", ""));
				approveListInfo.setRecTime(selectResult.get(0).getRecTime().replaceAll(":", ""));
				approveListInfo.setFileName(selectResult.get(0).getFileName());
				approveListInfo.setCallKind1(selectResult.get(0).getCallKind1());
				approveListInfo.setUserIdCall(selectResult.get(0).getUserId());
				approveListInfo.setUserNameCall(selectResult.get(0).getUserNameCall());

				/*
				 * if(!StringUtil.isNull(request.getParameter("fileName"),true))
				 * approveListInfo.setFileName(request.getParameter("fileName"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("recExt"),true))
				 * approveListInfo.setRecExt(request.getParameter("recExt"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("recDate"),true))
				 * approveListInfo.setRecDate(request.getParameter("recDate").replaceAll("-",
				 * ""));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("recTime"),true))
				 * approveListInfo.setRecTime(request.getParameter("recTime").replaceAll(":",
				 * ""));
				 */
				Integer result = searchListInfoService.insertApproveInfo(approveListInfo);

				if (result > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("insert success");
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("insert fail");
				}

			} else if ("check".equals(type)) {

				ApproveListInfo approveListInfo = new ApproveListInfo();

				approveListInfo.setUserId(userInfo.getUserId());
				approveListInfo.setCheckYn("Y");

				if (!StringUtil.isNull(request.getParameter("approveType"), true))
					approveListInfo.setApproveType(request.getParameter("approveType"));

				if (!StringUtil.isNull(request.getParameter("fileName"), true))
					approveListInfo.setFileName(request.getParameter("fileName"));

				if (!StringUtil.isNull(request.getParameter("recExt"), true))
					approveListInfo.setRecExt(request.getParameter("recExt"));

				if (!StringUtil.isNull(request.getParameter("recDate"), true))
					approveListInfo.setRecDate(request.getParameter("recDate").replaceAll("-", ""));

				if (!StringUtil.isNull(request.getParameter("recTime"), true))
					approveListInfo.setRecTime(request.getParameter("recTime").replaceAll(":", ""));

				List<ApproveListInfo> result = searchListInfoService.selectApproveInfo(approveListInfo);
				String resultStr = "none";
				if (result.size() > 0) {
					ApproveListInfo item = result.get(0);

					switch (item.getApproveState()) {
					case "0":
						resultStr = "prereciptWait";
						break;
					case "1":
						resultStr = "prereciptReject";
						break;
					case "2":
						resultStr = "reciptWait";
						break;
					case "3":
						resultStr = "reciptReject";
						break;
					case "4":
						resultStr = "approveWait";
						break;
					case "5":
						resultStr = "approveReject";
						break;
					case "6":
						if (!StringUtil.isNull(item.getApproveDate(), true)
								&& !StringUtil.isNull(item.getApproveTime(), true)
								&& !StringUtil.isNull(item.getApproveDay(), true)) {
							Date approveDate = DateUtil.toDate(item.getApproveDate().replaceAll("-", "")
									+ item.getApproveTime().replaceAll(":", ""));
							Integer approveDay = Integer.parseInt(item.getApproveDay());
							Date limitDate = DateUtil.calcDate(approveDate, "day", approveDay);

							if (DateUtil.isBetweenDate(new Date(), approveDate, limitDate))
								resultStr = "approve";
							else
								resultStr = "approveFinish";
						} else
							resultStr = "none";
						break;
					default:
						resultStr = "none";
						break;
					}
				} else {
					resultStr = "none";
				}

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("result", resultStr);
				jRes.setResult("select success");

			} else if ("accept".equals(type) || "reject".equals(type)) {

				ApproveListInfo approveListInfo = new ApproveListInfo();

				approveListInfo.setApproveId(userInfo.getUserId());
				approveListInfo.setReciptId(userInfo.getUserId());
				approveListInfo.setPrereciptId(userInfo.getUserId());

				if (!StringUtil.isNull(request.getParameter("reqDate"), true))
					approveListInfo.setReqDate(request.getParameter("reqDate").replaceAll("-", ""));

				if (!StringUtil.isNull(request.getParameter("reqTime"), true))
					approveListInfo.setReqTime(request.getParameter("reqTime").replaceAll(":", ""));

				if (!StringUtil.isNull(request.getParameter("userId"), true))
					approveListInfo.setUserId(request.getParameter("userId"));

				if (!StringUtil.isNull(request.getParameter("approveType"), true))
					approveListInfo.setApproveType(request.getParameter("approveType"));

				if (!StringUtil.isNull(request.getParameter("approveReason"), true))
					approveListInfo.setApproveReason(request.getParameter("approveReason"));

				if (!StringUtil.isNull(request.getParameter("fileName"), true))
					approveListInfo.setFileName(request.getParameter("fileName"));

				if (!StringUtil.isNull(request.getParameter("recExt"), true))
					approveListInfo.setRecExt(request.getParameter("recExt"));

				if (!StringUtil.isNull(request.getParameter("recDate"), true))
					approveListInfo.setRecDate(request.getParameter("recDate").replaceAll("-", ""));

				if (!StringUtil.isNull(request.getParameter("recTime"), true))
					approveListInfo.setRecTime(request.getParameter("recTime").replaceAll(":", ""));

				if (!StringUtil.isNull(request.getParameter("approveState"), true))
					approveListInfo.setApproveState(request.getParameter("approveState"));

				Integer result = searchListInfoService.updateApproveInfo(approveListInfo);

				if (result > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("update success");
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("update fail");
				}

			} /*
				 * else if("delete".equals(type)) {
				 * 
				 * ApproveListInfo approveListInfo = new ApproveListInfo();
				 * 
				 * if(!StringUtil.isNull(request.getParameter("reqDate"),true))
				 * approveListInfo.setReqDate(request.getParameter("reqDate").replaceAll("-",
				 * ""));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("reqTime"),true))
				 * approveListInfo.setReqTime(request.getParameter("reqTime").replaceAll(":",
				 * ""));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("userId"),true))
				 * approveListInfo.setUserId(request.getParameter("userId"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("approveType"),true))
				 * approveListInfo.setApproveType(request.getParameter("approveType"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("approveReason"),true))
				 * approveListInfo.setApproveReason(request.getParameter("approveReason"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("approveState"),true))
				 * approveListInfo.setApproveState(request.getParameter("approveState"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("fileName"),true))
				 * approveListInfo.setFileName(request.getParameter("fileName"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("recExt"),true))
				 * approveListInfo.setRecExt(request.getParameter("recExt"));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("recDate"),true))
				 * approveListInfo.setRecDate(request.getParameter("recDate").replaceAll("-",
				 * ""));
				 * 
				 * if(!StringUtil.isNull(request.getParameter("recTime"),true))
				 * approveListInfo.setRecTime(request.getParameter("recTime").replaceAll(":",
				 * ""));
				 * 
				 * Integer result = searchListInfoService.updateApproveInfo(approveListInfo);
				 * 
				 * if(result > 0) { jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				 * jRes.setResult("update success"); }else {
				 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("update fail"); }
				 * 
				 * }
				 */
			// }catch(Exception e) {
			/*
			 * jRes.setSuccess(AJaxResVO.SUCCESS_N); jRes.setResult("approve_proc fail");
			 * jRes.addAttribute("fail reason",e.getMessage());
			 */
			// }

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

//	@RequestMapping(value = "/zipDownload")
//	public ModelAndView zipDownload(HttpServletRequest request, HttpServletResponse response, Model model)
//			throws IOException {
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//
//		String[] aFileName = request.getParameter("fileName").split("\\|");
//		String fileName = "";
//
//		File tmpfile = null, tempFile = null, file = null;
//		List<String> fileList = new ArrayList<String>();
//		URL url = null;
//
//		// 암호화 여부 알아오는 쿼리
//		EtcConfigInfo decryptionYn = new EtcConfigInfo();
//		decryptionYn.setGroupKey("download");
//		decryptionYn.setConfigKey("decryption");
//		List<EtcConfigInfo> decryptionYnResult = etcConfigInfoService.selectEtcConfigInfo(decryptionYn);
//		String decryptionResult = ((StringUtil.isNull(decryptionYnResult.get(0).getConfigValue(), true)
//				|| decryptionYnResult.get(0).getConfigValue().equals("y")) ? "y" : "n");
//
//		if (userInfo != null) {
//			for (int f = 0; f < aFileName.length; f++) {
//				fileName = aFileName[f].toString();
//
//				try {
//					url = new URL(fileName.toString());
//				} catch (MalformedURLException e) {
//					continue;
//				}
//
//				BufferedInputStream in = null;
//				try {
//					file = new File(url.getPath());
//					String extension = url.toString().substring(url.toString().lastIndexOf("."));
//					tmpfile = File.createTempFile(file.getName().replace(extension, "") + "_", extension);
//				} catch (IOException e) {
//					continue;
//				}
//
//				FileOutputStream fout = null;
//
//				try {
//					in = new BufferedInputStream(url.openStream());
//					fout = new FileOutputStream(tmpfile);
//
//					final byte data[] = new byte[1024];
//					int count;
//					while ((count = in.read(data, 0, 1024)) != -1) {
//						fout.write(data, 0, count);
//					}
//					fileList.add(tmpfile.getAbsolutePath());
//				} finally {
//					if (in != null) {
//						in.close();
//					}
//					if (fout != null) {
//						fout.close();
//					}
//				}
//			}
//			try {
//				tempFile = File.createTempFile("zipfile_", ".zip");
//			} catch (IOException e) {
//				Logger.error("", "", "", e.toString());
//			}
//
//			if (fileList != null && fileList.size() > 0) {
//				try {
//
//					byte[] buffer = new byte[1024];
//
//					FileOutputStream fos = new FileOutputStream(tempFile);
//					ZipOutputStream zos = new ZipOutputStream(fos);
//
//					for (int i = 0; i < fileList.size(); i++) {
//
//						// 암호화 됬을 땐 파일 앞에 [encrypt]붙이고 암호화 진행 및 암호화된 파일 다운
//						if (decryptionResult.equals("n")) {
//							String oriPath = fileList.get(i);
//							String destPath = oriPath.substring(0, fileList.get(i).lastIndexOf(File.separator))
//									+ File.separator + "[encrypt]"
//									+ fileList.get(i).substring(fileList.get(i).lastIndexOf(File.separator) + 1);
//							SeedUtil.encryptFile(fileList.get(i), destPath);
//							fileList.set(i, destPath);
//						}
//						File srcFile = new File(fileList.get(i));
//						FileInputStream fis = new FileInputStream(srcFile);
//
//						String extension = srcFile.getName().substring(srcFile.getName().lastIndexOf("."));
//						String tmpFileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("_"))
//								+ extension;
//
//						zos.putNextEntry(new ZipEntry(tmpFileName));
//
//						int length;
//
//						while ((length = fis.read(buffer)) > 0) {
//							zos.write(buffer, 0, length);
//						}
//						zos.closeEntry();
//
//						fis.close();
//					}
//					zos.close();
//
//					for (int i = 0; i < fileList.size(); i++) {
//						File delFile = new File(fileList.get(i));
//						delFile.delete();
//					}
//
//					logInfoService.writeLog(request, "ZipFile download success",
//							(tempFile != null && tempFile.isFile()) ? tempFile.toURI().toString() : "",
//							userInfo.getUserId());
//
//					return new ModelAndView("fileDownload", "downloadFile", tempFile);
//				} catch (IOException ioe) {
//					Logger.error("", "", "", ioe.toString());
//
//					RedirectView rv = new RedirectView(request.getContextPath() + "/common/message_proc");
//					rv.addStaticAttribute("msg",
//							messageSource.getMessage("message.download.nofile", null, Locale.getDefault()));
//
//					logInfoService.writeLog(request, "ZipFile download nofile", null, userInfo.getUserId());
//
//					return new ModelAndView(rv);
//				}
//			} else {
//				RedirectView rv = new RedirectView(request.getContextPath() + "/common/message_proc");
//				rv.addStaticAttribute("msg",
//						messageSource.getMessage("message.download.nofile", null, Locale.getDefault()));
//
//				logInfoService.writeLog(request, "ZipFile download nofile list", null, userInfo.getUserId());
//
//				return new ModelAndView(rv);
//			}
//
//		} else {
//			RedirectView rv = new RedirectView(request.getContextPath() + "/common/message_proc");
//			rv.addStaticAttribute("msg",
//					messageSource.getMessage("message.download.logout", null, Locale.getDefault()));
//
//			logInfoService.writeLog(request, "ZipFile download logout");
//
//			return new ModelAndView(rv);
//		}
//	}

	// 플레이어 다운로드용 압축파일 만들기
//	@RequestMapping(value = "/playerZip.do")
//	public @ResponseBody AJaxResVO playerDownload(HttpServletRequest request, HttpServletResponse response, Model model)
//			throws IOException {
//		AJaxResVO jRes = new AJaxResVO();
//		LoginVO userInfo = SessionManager.getUserInfo(request);
//		if (userInfo != null) {
//			String rsTemp = request.getSession().getServletContext()
//					.getRealPath("/resources/component/rs_player/rsTemp");
//			String rs = request.getSession().getServletContext().getRealPath("/resources/component/rs_player/rs");
//			File tempFile = new File(rsTemp);
//			File rsFile = new File(rs);
//			if (tempFile.exists()) {
//				tempFile.delete();
//			}
//			if (rsFile.exists()) {
//				rsFile.delete();
//			}
//			String period;
//			byte[] pwd = new byte[128];
//			Date now = new Date();
//			if (!"Y".equals(request.getParameter("playerPeriod"))) {
//				period = DateUtil.toString(now, "yyyyMMdd") + "99991231";
//			} else {
//				int add = Integer.parseInt(request.getParameter("period"));
//				period = DateUtil.toString(now, "yyyyMMdd")
//						+ DateUtil.toString(DateUtil.calcDate(now, "day", add - 1), "yyyyMMdd");
//			}
//			if (!"Y".equals(request.getParameter("playerPassword"))) {
//				// 2차인증 없는경우
//				byte[] nonePass = { (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02,
//						(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00,
//						(byte) 0x04, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x01,
//						(byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x07, (byte) 0x00,
//						(byte) 0x01, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x09,
//						(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x01, (byte) 0x00,
//						(byte) 0x0B, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x01,
//						(byte) 0x00, (byte) 0x0D, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0E, (byte) 0x00,
//						(byte) 0x01, (byte) 0x00, (byte) 0x0F, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x10,
//						(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x01, (byte) 0x00,
//						(byte) 0x12, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x01,
//						(byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x15, (byte) 0x00,
//						(byte) 0x01, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x17,
//						(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x01, (byte) 0x00,
//						(byte) 0x19, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x1A, (byte) 0x00, (byte) 0x01,
//						(byte) 0x00, (byte) 0x1B, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x1C, (byte) 0x00,
//						(byte) 0x01, (byte) 0x00, (byte) 0x1D, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x1E,
//						(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x1F, (byte) 0x00, (byte) 0x01, (byte) 0x00,
//						(byte) 0x20, (byte) 0x00, (byte) 0x01 };
//				pwd = nonePass;
//			} else {
//				byte[] password = request.getParameter("password").getBytes();
//				for (int i = 0; i < password.length; i++) {
//					pwd[i] = password[i];
//				}
//			}
//
//			FileOutputStream out = new FileOutputStream(tempFile);
//			out.write(period.getBytes());
//			out.write(pwd);
//
//			out.close();
//			SeedUtil.encryptFile(rsTemp, rs);
//			if (tempFile.exists()) {
//				tempFile.delete();
//			}
//
//			String path = request.getSession().getServletContext().getRealPath("/resources/component/rs_player");
//
//			// 임시 압축파일 생성 경로(db에 있는 경로 중 임시로 쓸만한게 이거밖에없음)
//			EtcConfigInfo pathInfo = new EtcConfigInfo();
//			pathInfo.setGroupKey("UPLOAD");
//			pathInfo.setConfigKey("BESTCALL_FILE_PATH");
//			List<EtcConfigInfo> pathInfoResult = etcConfigInfoService.selectEtcConfigInfo(pathInfo);
//			final String zipPath = pathInfoResult.get(0).getConfigValue();
//			File folder = new File(zipPath);
//			if (!folder.exists()) {
//				try {
//					folder.mkdir(); // 폴더 생성합니다.
//				} catch (Exception e) {
//					e.getStackTrace();
//				}
//			}
//			String outZipNm = zipPath + "/rs_player.zip";
//			File zipF = new File(outZipNm);
//			if (zipF.exists()) {
//				zipF.delete();
//			}
//			File file = new File(path);
//			String files[] = null;
//
//			// 파일이 디렉토리 일경우 리스트를 읽어오고
//			// 파일이 디렉토리가 아니면 첫번째 배열에 파일이름을 넣는다.
//			if (file.isDirectory()) {
//				files = file.list();
//			} else {
//				files = new String[1];
//				files[0] = file.getName();
//			}
//
//			// buffer size
//			int size = 1024;
//			byte[] buf = new byte[size];
//			FileInputStream fis = null;
//			ZipOutputStream zos = null;
//			BufferedInputStream bis = null;
//			try {
//				// Zip 파일생성
//				zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outZipNm)));
//
//				for (int i = 0; i < files.length; i++) {
//
//					// buffer에 해당파일의 stream을 입력한다.
//					fis = new FileInputStream(path + "/" + files[i]);
//					bis = new BufferedInputStream(fis, size);
//
//					// zip에 넣을 다음 entry 를 가져온다.
//					zos.putNextEntry(new ZipEntry(files[i]));
//
//					// 압출레벨을 설정한다.
//					// 기본값은 8이라고 한다. 최대는 9이다.
//					final int COMPRESSION_LEVEL = 8;
//					zos.setLevel(COMPRESSION_LEVEL);
//
//					// 준비된 버퍼에서 집출력스트림으로 write 한다.
//					int len;
//					while ((len = bis.read(buf, 0, size)) != -1) {
//						zos.write(buf, 0, len);
//					}
//					zos.closeEntry();
//					bis.close();
//					fis.close();
//
//				}
//
//				zos.close();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (rsFile.exists()) {
//				rsFile.delete();
//			}
//			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
//			jRes.addAttribute("filePath", outZipNm);
//		} else {
//			jRes.setSuccess(AJaxResVO.SUCCESS_N);
//			jRes.setResult("0");
//			jRes.addAttribute("msg", "NOT LOGIN");
//		}
//		return jRes;
//
//	}

	// 플레이어 다운로드
	@RequestMapping(value = "/playerDownload.do")
	public void playerDownload(HttpServletRequest request, Locale local, Model model, HttpServletResponse response) throws Exception {
		String filePath = request.getParameter("path");
		
		String tempFile = getValidPath(filePath);
		
		File zipFile = new File(tempFile);

		response.setHeader("Content-Type", "application/zip, application/octet-stream");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Content-Disposition", "attachment; filename=rs_player.zip");
		response.setHeader("Content-Length", String.valueOf(zipFile.length()));

		OutputStream os = null;
		try {
			os = response.getOutputStream();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(zipFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[4096];
		int count;
		try {
			while (fis != null && (count = fis.read(buffer)) != -1) {
				os.write(buffer, 0, count);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// close 부문
		try {
			if (fis != null)
				fis.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		try {
			if (os != null)
				os.close();
		} catch (Exception e2) {
			System.out.println(e2.toString());
		} finally {
			zipFile.delete();
		}
	}

	@RequestMapping(value = "/searchExcel.do")
	public void searchExcel(HttpServletRequest request, Map<String, Object> ModelMap, HttpServletResponse response)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		String systemTemplates = (String) request.getSession().getAttribute("systemTemplates");

		List<String[]> contents = new ArrayList<String[]>();

		String[] row = null;
		int colPos = 0;
		String recsee_mobile = "N";
		if (!StringUtil.isNull(request.getParameter("recsee_mobile"))) {
			recsee_mobile = "Y";
		}

		@SuppressWarnings("unchecked")
		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, "searchNListen");

		if ("N".equals(nowAccessInfo.getExcelYn())) {
			row = new String[1];
			row[0] = messageSource.getMessage("views.search.alert.msg78", null,
					Locale.getDefault())/* "권한이 없는 사용자의 요청입니다.\n엑셀 다운로드 권한이 있는 사용자로 다운로드 요청을 해 주세요!" */;
			contents.add(row);
			ModelMap.put("excelList", contents);
			ModelMap.put("target", request.getParameter("fileName"));
		} else if (userInfo != null) {
			// SAFE DB CHECK
			EtcConfigInfo safeDBetcConfigInfo = new EtcConfigInfo();
			safeDBetcConfigInfo.setGroupKey("SEARCH");
			safeDBetcConfigInfo.setConfigKey("SAFE_DB");
			List<EtcConfigInfo> safeDbResult = etcConfigInfoService.selectEtcConfigInfo(safeDBetcConfigInfo);

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("EXCEPT");
			etcConfigInfo.setConfigKey("CONSENT_NO_RECODING_USE");
			List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String consentNoRecodingUse = "N";
			if (etcConfigResult.size() > 0) {
				consentNoRecodingUse = etcConfigResult.get(0).getConfigValue();
			}
			OrganizationInfo organizationInfo = new OrganizationInfo();
			List<OrganizationInfo> organizationBgInfo = organizatinoInfoService
					.selectOrganizationBgInfo(organizationInfo);
			List<OrganizationInfo> organizationMgInfo = organizatinoInfoService
					.selectOrganizationMgInfo(organizationInfo);
			List<OrganizationInfo> organizationSgInfo = organizatinoInfoService
					.selectOrganizationSgInfo(organizationInfo);

			etcConfigInfo.setGroupKey("SYSTEM");
			etcConfigInfo.setConfigKey("START_TYPE");
			List<EtcConfigInfo> callTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			String[] callTypeResultArray = callTypeResult.get(0).getConfigValue().split(",");

			Map<String, String> startTypeList = new HashMap<String, String>();
			for (int i = 0; i < callTypeResultArray.length; i++) {
				startTypeList.put(callTypeResultArray[i], messageSource
						.getMessage("call.type.startType." + callTypeResultArray[i], null, Locale.getDefault()));
			}

			CommonCodeVO commonCode = new CommonCodeVO();
			commonCode.setParentCode("counsel");
			List<CommonCodeVO> codeList = commonCodeService.selectCommonCode(commonCode);
			Map<String, String> counselBgList = new HashMap<String, String>();
			if (codeList.size() > 0) {
				for (int i = 0; i < codeList.size(); i++) {
					CommonCodeVO item = codeList.get(i);
					counselBgList.put(item.getCodeValue(), item.getCodeName());
				}
			}
			commonCode.setParentCode("mgCode");
			codeList = commonCodeService.selectCommonCode(commonCode);
			Map<String, String> counselMgList = new HashMap<String, String>();
			if (codeList.size() > 0) {
				for (int i = 0; i < codeList.size(); i++) {
					CommonCodeVO item = codeList.get(i);
					counselMgList.put(item.getCodeValue(), item.getCodeName());
				}
			}

			commonCode.setParentCode("sgCode");
			codeList = commonCodeService.selectCommonCode(commonCode);
			Map<String, String> counselSgList = new HashMap<String, String>();
			if (codeList.size() > 0) {
				for (int i = 0; i < codeList.size(); i++) {
					CommonCodeVO item = codeList.get(i);
					counselSgList.put(item.getCodeValue(), item.getCodeName());
				}
			}

			CustomizeListInfo customizeListInfo = new CustomizeListInfo();
			if (userInfo != null)
				customizeListInfo.setrUserId(userInfo.getUserLevel());

			List<CustomizeListInfo> searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);

			Integer settingInfoTotal = searchSettingInfo.size();
			if (settingInfoTotal < 1) {
				customizeListInfo.setrUserId("default");

				searchSettingInfo = customizeInfoService.selectCustomizeListInfo(customizeListInfo);

				settingInfoTotal = searchSettingInfo.size();
			}

			if (settingInfoTotal > 0) {
				/**
				 * 설정된 검색 출력 항목만을 출력할 수 있도록 처리
				 */
				Map<String, String> searchTemplItem = null;
				if (settingInfoTotal > 1) {
					for (int i = 0; i < settingInfoTotal; i++) {
						if (userInfo.getUserId().equals(searchSettingInfo.get(i).getrUserId()))
							searchTemplItem = searchSettingInfo.get(i).getAllItem();
					}
				} else {
					searchTemplItem = searchSettingInfo.get(0).getAllItem();
				}

				String titleBaseName = "views.search.grid.head.";

				Integer colNum = (searchTemplItem != null) ? searchTemplItem.size() : 0;

				row = new String[colNum];
				colPos = 0;

				Iterator<String> keys = searchTemplItem.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();

					if (key.equals("rUserId"))
						continue;

					if (searchTemplItem.get(key) != null && !searchTemplItem.get(key).isEmpty()) {

						switch (searchTemplItem.get(key)) {
						case "r_check_box":
						case "screen":
						case "evaluation":
						case "trace":
						case "r_list_add":
						case "r_memo_info":
						case "r_memo":
						case "r_stt_player":
							break;
						case "r_ext_num":
							if (recsee_mobile.equals("Y")) {
								row[colPos++] = messageSource.getMessage(
										titleBaseName + "R_EXT_NUM_MOBILE".toUpperCase(), null, Locale.getDefault());
							} else {
								row[colPos++] = messageSource.getMessage(
										titleBaseName + searchTemplItem.get(key).toUpperCase(), null,
										Locale.getDefault());
							}
							break;
						case "r_buffer1":
						case "r_buffer2":
						case "r_buffer3":
						case "r_buffer4":
						case "r_buffer5":
						case "r_buffer6":
							if (recsee_mobile.equals("Y")) {
								row[colPos++] = messageSource.getMessage(
										titleBaseName + "mobile" + searchTemplItem.get(key).toUpperCase(), null,
										Locale.getDefault());
							} else {
								row[colPos++] = messageSource.getMessage(
										titleBaseName + searchTemplItem.get(key).toUpperCase(), null,
										Locale.getDefault());
							}
							break;
						case "r_division": // 20200421 jbs 엑셀다운로드 시 '분배'컬럼 안나오게 하기 위함.
						case "r_rec_visible":
							break;
						case "r_rec_volume":
							break;
						default:
							row[colPos++] = messageSource.getMessage(
									titleBaseName + searchTemplItem.get(key).toUpperCase(), null, Locale.getDefault());
							break;
						}
					}
				}
				contents.add(row);

				SearchListInfo searchListInfo = new SearchListInfo();

				searchListInfo.setParamMap(request, consentNoRecodingUse, "N");

				MMenuAccessInfo accessInfo = new MMenuAccessInfo();
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P10002");
				List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

				List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
				if (accessResult.get(0).getAccessLevel().substring(0, 1).equals("R")) {
					List<AllowableRangeInfo> allowableList = null;

					AllowableRangeInfo allowableRangeInfoChk = new AllowableRangeInfo();
					allowableRangeInfoChk.setrAllowableCode(accessResult.get(0).getAccessLevel());
					allowableList = allowableRangeInfoService.selectAllowableRangeInfo(allowableRangeInfoChk);
					if (allowableList.size() > 0) {
						for (int i = 0; i < allowableList.size(); i++) {
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("bgcode", allowableList.get(i).getrBgCode());
							item.put("mgcode", allowableList.get(i).getrMgCode());
							item.put("sgcode", allowableList.get(i).getrSgCode());
							authyInfo.add(item);
						}
					} else {
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("noneallowable", "noneallowable");
						authyInfo.add(item);
					}

				} else {
					if (!accessResult.get(0).getAccessLevel().equals("A")) {
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("bgcode", userInfo.getBgCode());
						if (!accessResult.get(0).getAccessLevel().equals("B")) {
							item.put("mgcode", userInfo.getMgCode());
						}
						if (!accessResult.get(0).getAccessLevel().equals("B")
								&& !accessResult.get(0).getAccessLevel().equals("M")) {
							item.put("sgcode", userInfo.getSgCode());
						}
						if (!accessResult.get(0).getAccessLevel().equals("B")
								&& !accessResult.get(0).getAccessLevel().equals("M")
								&& !accessResult.get(0).getAccessLevel().equals("S")) {
							item.put("user", userInfo.getUserId());
						}

						authyInfo.add(item);
					}
				}

				MultiPartInfo multiPartInfo = new MultiPartInfo();
				multiPartInfo.setrTarget(userInfo.getUserId());
				List<MultiPartInfo> multiPartResult = ruserInfoService.selectMultiPartInfo(multiPartInfo);
				if (multiPartResult.size() > 0) {
					for (int j = 0; j < multiPartResult.size(); j++) {
						MultiPartInfo multiPartItem = multiPartResult.get(j);

						HashMap<String, String> item = new HashMap<String, String>();

						if (multiPartItem.getrBgCode() != null && !multiPartItem.getrBgCode().isEmpty()) {
							item.put("bgcode", multiPartItem.getrBgCode());
						}
						if (multiPartItem.getrMgCode() != null && !multiPartItem.getrMgCode().isEmpty()) {
							item.put("mgcode", multiPartItem.getrMgCode());
						}
						if (multiPartItem.getrSgCode() != null && !multiPartItem.getrSgCode().isEmpty()) {
							item.put("sgcode", multiPartItem.getrSgCode());
						}

						authyInfo.add(item);
					}
				}

				if (authyInfo != null && authyInfo.size() > 0) {
					searchListInfo.setAuthyInfo(authyInfo);
				}

				// Postgres 암호화 사용여부
				String postgresColumn = "";
				etcConfigInfo.setGroupKey("ENCRYPT");
				etcConfigInfo.setConfigKey("POSTGRES");
				etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if (etcConfigResult.size() > 0) {
					if ("Y".equals(etcConfigResult.get(0).getConfigValue())) {
						etcConfigInfo.setGroupKey("ENCRYPT");
						etcConfigInfo.setConfigKey("COLUMN");

						etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

						postgresColumn = etcConfigResult.get(0).getConfigValue();
					}
				} else {
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
					etcConfigInfo.setGroupKey("ENCRYPT");
					etcConfigInfo.setConfigKey("COLUMN");
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				}

				if (!"".equals(postgresColumn)) {
					if (postgresColumn.contains("r_cust_phone1")) {
						searchListInfo.setCustPhone1IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_cust_phone2")) {
						searchListInfo.setCustPhone2IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_cust_phone3")) {
						searchListInfo.setCustPhone3IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_cust_social_num")) {
						searchListInfo.setCustSocailNumIsEncrypt("Y");
					}

					if (postgresColumn.contains("r_cust_phone_ap")) {
						searchListInfo.setCustPhoneApIsEncrypt("Y");
					}

					if (postgresColumn.contains("r_cust_name")) {
						searchListInfo.setCustNameIsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer1")) {
						searchListInfo.setBuffer1IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer2")) {
						searchListInfo.setBuffer2IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer3")) {
						searchListInfo.setBuffer3IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer4")) {
						searchListInfo.setBuffer4IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer5")) {
						searchListInfo.setBuffer5IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer6")) {
						searchListInfo.setBuffer6IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer7")) {
						searchListInfo.setBuffer7IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer8")) {
						searchListInfo.setBuffer8IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer9")) {
						searchListInfo.setBuffer9IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer10")) {
						searchListInfo.setBuffer10IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer11")) {
						searchListInfo.setBuffer11IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer12")) {
						searchListInfo.setBuffer12IsEncrypt("Y");
					}

					if (postgresColumn.contains("r_buffer13")) {
						searchListInfo.setBuffer13IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer1")) {
						searchListInfo.setBuffer14IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer15")) {
						searchListInfo.setBuffer15IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer16")) {
						searchListInfo.setBuffer16IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer17")) {
						searchListInfo.setBuffer17IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer18")) {
						searchListInfo.setBuffer18IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer19")) {
						searchListInfo.setBuffer19IsEncrypt("Y");
					}
					if (postgresColumn.contains("r_buffer20")) {
						searchListInfo.setBuffer20IsEncrypt("Y");
					}
				}

				// 고객전화번호 암호화 시작한 날짜
				if (!StringUtil.isNull(request.getParameter("custEncryptDate"))) {
					searchListInfo.setCustEncryptDate(request.getParameter("custEncryptDate"));
				}

				// 전화번호, 이름 마스킹 처리 여부 확인
				EtcConfigInfo etcConfigMasking = new EtcConfigInfo();
				etcConfigMasking.setGroupKey("SEARCH");
				etcConfigMasking.setConfigKey("MASKING_INFO");
				List<EtcConfigInfo> maskingModeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigMasking);
				String maskingYn = "";
				if (maskingModeResult.size() > 0) {
					maskingYn = maskingModeResult.get(0).getConfigValue();
				}

				/* 20200128 김다빈 추가 */
				// prefix 제거 옵션 사용 여부
				EtcConfigInfo etcConfigPrefixYN = new EtcConfigInfo();
				etcConfigPrefixYN.setGroupKey("Prefix");
				etcConfigPrefixYN.setConfigKey("PrefixYN");
				EtcConfigInfo PrefixYN = etcConfigInfoService.selectOptionYN(etcConfigPrefixYN);
				String PrefixYNVal = PrefixYN.getConfigValue();

				// 전화번호 prefix 제거 처리
				EtcConfigInfo etcConfigPrefixNumber = new EtcConfigInfo();
				etcConfigPrefixNumber.setGroupKey("Prefix");
				etcConfigPrefixNumber.setConfigKey("Prefix");
				List<EtcConfigInfo> PrefixNumberInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigPrefixNumber);
				String[] arrPrefixInfo = null;
				if (PrefixNumberInfo.size() > 0) {
					arrPrefixInfo = PrefixNumberInfo.get(0).getConfigValue().split(",");
				}
				// 전화번호 마스킹 여부
				EtcConfigInfo etcConfigMaskingYN = new EtcConfigInfo();
				etcConfigMaskingYN.setGroupKey("masking");
				etcConfigMaskingYN.setConfigKey("maskingYN");
				EtcConfigInfo maskingYN = etcConfigInfoService.selectOptionYN(etcConfigMaskingYN);
				String maskingYNVal = maskingYN.getConfigValue();

				// 전화번호 마스킹 처리
				EtcConfigInfo etcConfigMaskingNumber = new EtcConfigInfo();
				etcConfigMaskingNumber.setGroupKey("masking");
				etcConfigMaskingNumber.setConfigKey("masking");
				List<EtcConfigInfo> maskingNumberInfo = etcConfigInfoService
						.selectEtcConfigInfo(etcConfigMaskingNumber);
				String[] arrMaskingInfo;
				int startIdx = 0, ea = 0;
				if (maskingNumberInfo.size() > 0) {
					arrMaskingInfo = maskingNumberInfo.get(0).getConfigValue().split(",");
					startIdx = Integer.parseInt(arrMaskingInfo[0]);
					ea = Integer.parseInt(arrMaskingInfo[1]);
				}

				// 전화번호 표기옵션 여부
				EtcConfigInfo etcConfigHyphenYN = new EtcConfigInfo();
				etcConfigHyphenYN.setGroupKey("hyphen");
				etcConfigHyphenYN.setConfigKey("hyphenYN");
				EtcConfigInfo hyphenYN = etcConfigInfoService.selectOptionYN(etcConfigHyphenYN);
				String hyphenYNVal = hyphenYN.getConfigValue();

				// 전화번호 표기옵션 적용
				EtcConfigInfo etcConfigSetHyphen = new EtcConfigInfo();
				etcConfigSetHyphen.setGroupKey("hyphen");
				etcConfigSetHyphen.setConfigKey("hyphen");
				List<EtcConfigInfo> setHyphenInfo = etcConfigInfoService.selectEtcConfigInfo(etcConfigSetHyphen);
				String[] arrHyphenInfo;
				String h1 = "", h2 = "";
				if (setHyphenInfo.size() > 0) {
					arrHyphenInfo = setHyphenInfo.get(0).getConfigValue().split(",");
					h1 = arrHyphenInfo[0];
					h2 = arrHyphenInfo[1];
				}
				
				//병합된 것만 받도록 하드코딩
				searchListInfo.setPartRecYn("N");
				searchListInfo.setErrorYn("N");
				
				List<SearchListInfo> searchListResult = null;
				if (StringUtil.isNull(request.getParameter("type"), true)
						|| "all".equals(request.getParameter("type"))) {
					// 기존 타입 (all)
					searchListResult = searchListInfoService.selectSearchListInfoSTT(searchListInfo);
				} else {
					List<String> recDateArr = new ArrayList<String>();
					List<String> recTimeArr = new ArrayList<String>();
					List<String> extArr = new ArrayList<String>();

					for (String s : request.getParameter("recDateArr").split(",")) {
						recDateArr.add(s);
					}
					for (String s : request.getParameter("recTimeArr").split(",")) {
						recTimeArr.add(s);
					}
					for (String s : request.getParameter("extArr").split(",")) {
						extArr.add(s);
					}
					searchListInfo.setRecDateArr(recDateArr);
					searchListInfo.setRecTimeArr(recTimeArr);
					searchListInfo.setRecExtArr(extArr);
					
					// 엑셀 다운로드 선택 적으로 다운 받을 때
					searchListResult = searchListInfoService.selectSearchListInfoSTT(searchListInfo);
				}

				Integer searchListResultTotal = searchListResult.size();
				for (int i = 0; i < searchListResultTotal; i++) {
					Iterator<String> columnList = searchTemplItem.keySet().iterator();

					String tempStrValue = null;

					row = new String[colNum];

					colPos = 0;

					while (columnList.hasNext()) {

						String columnName = columnList.next();
						if (searchTemplItem.get(columnName) == null)
							continue;
						switch (searchTemplItem.get(columnName)) {
						case "r_check_box":
						case "r_list_add":
						case "evaluation":
						case "screen":
						case "r_memo_info":
						case "r_memo":
							break;
						case "r_product_type":	
							if (searchListResult.get(i).getBuffer7() == null)
								tempStrValue = "";
							else {
								tempStrValue = searchListResult.get(i).getBuffer7();
								if(tempStrValue.startsWith("T")) {
									tempStrValue="투자 성향 분석";
								} else {
									tempStrValue="상품 설명";
								}
							}
							row[colPos++] = tempStrValue;
							break;
						case "r_rec_date":
							String orgDate = searchListResult.get(i).getRecDate();
							if (orgDate == null)
								orgDate = "";
							row[colPos++] = orgDate;
							break;
						case "r_rec_rtime":
							if (searchListResult.get(i).getRecRtime() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getRecRtime();
							row[colPos++] = tempStrValue;
							break;
						case "r_rec_time":
							String orgTime = searchListResult.get(i).getRecTime();
							if (orgTime == null)
								orgTime = "";
							row[colPos++] = orgTime;
							break;
						case "r_call_stime_connect":
							String orgTimeConnect = searchListResult.get(i).getCallStimeConnect();
							if (orgTimeConnect == null)
								orgTimeConnect = "";
							row[colPos++] = orgTimeConnect;
							break;
						case "r_bg_code":
							if (searchListResult.get(i).getBgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBgCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_mg_code":
							if (searchListResult.get(i).getMgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getMgCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_sg_code":
							if (searchListResult.get(i).getSgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getSgCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_user_id":
							if (searchListResult.get(i).getUserId() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getUserId();
							row[colPos++] = tempStrValue;
							break;
						case "r_ch_num":
							if (searchListResult.get(i).getChNum() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getChNum();
							row[colPos++] = tempStrValue;
							break;
						case "r_user_name":
							if (searchListResult.get(i).getUserName() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getUserName();
							row[colPos++] = tempStrValue;
							break;
						case "r_call_id1":
							if (searchListResult.get(i).getCallId1() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCallId1();
							row[colPos++] = tempStrValue;
							break;
						case "r_call_id2":
							if (searchListResult.get(i).getCallId2() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCallId2();
							row[colPos++] = tempStrValue;
							break;
						case "r_call_id3":
							if (searchListResult.get(i).getCallId3() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCallId3();
							row[colPos++] = tempStrValue;
							break;
						case "r_ext_num":
							if (searchListResult.get(i).getExtNum() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getExtNum();
							row[colPos++] = tempStrValue;
							break;
						case "r_cust_name":
							if (searchListResult.get(i).getCustName() == null)
								tempStrValue = "";
							else {
								if (safeDbResult.size() > 0) {
									if (safeDbResult.get(0).getConfigValue().equals("Y"))
										tempStrValue = searchListResult.get(i)
												.SafeDBGetter(searchListResult.get(i).getCustName());
									else
										tempStrValue = searchListResult.get(i).getCustName();
								}
							}
							if (searchListResult.get(i).getCustName() == null) {
								tempStrValue = "";
							} else {
								// if(maskingYn.contains("r_cust_name")){
								// tempStrValue=new RecSeeUtil().makingName(tempStrValue);
								// }
							}
							row[colPos++] = tempStrValue;
							break;
						case "r_cust_phone_ap":

							tempStrValue = searchListResult.get(i).getrCustPhoneAp();

							if (StringUtil.isNull(tempStrValue)) {
								tempStrValue = searchListResult.get(i).getCustPhone1();
								if (StringUtil.isNull(tempStrValue)) {
									tempStrValue = searchListResult.get(i).getCustPhone2();
									if (StringUtil.isNull(tempStrValue)) {
										tempStrValue = "";
									}
								}
							}

							if (!"-".equals(tempStrValue)) {
								if (tempStrValue.substring(0, 1).equals("9")) {
									tempStrValue = tempStrValue.substring(1);
								}
							}

							/* 20200129 김다빈 추가 */
							if (PrefixYNVal.equals("Y")) {
								tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
							}
							if (maskingYNVal.equals("Y")) {
								tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
							}
							if (hyphenYNVal.equals("Y")) {
								tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
							}

							/*
							 * if(maskingYn.contains("r_cust_phone1")){ // tempStrValue = new
							 * RecSeeUtil().makePhoneNumber(tempStrValue);
							 * if(StringUtil.isNull(tempStrValue,true)) tempStrValue = ""; // else //
							 * tempStrValue = new RecSeeUtil().maskingNumber(tempStrValue); }else{ //
							 * tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue); }
							 */

							row[colPos++] = tempStrValue;
							break;

						case "r_cust_phone1":

							if (searchListResult.get(i).getCustPhone1() == null)
								tempStrValue = "";
							else {
								tempStrValue = searchListResult.get(i).getCustPhone1();
							}

							row[colPos++] = tempStrValue;
							break;
						case "r_cust_phone2":
							if (searchListResult.get(i).getCustPhone2() == null)
								tempStrValue = "";
							else {
								tempStrValue = searchListResult.get(i).getCustPhone2();
								/* 20200129 김다빈 추가 */
								if (PrefixYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().prefixPhoneNum(tempStrValue, arrPrefixInfo);
								}
								if (maskingYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().maskingPhoneNum(tempStrValue, startIdx, ea);
								}
								if (hyphenYNVal.equals("Y")) {
									tempStrValue = new RecSeeUtil().setHyphenNum(tempStrValue, h1, h2);
								}
							}
							row[colPos++] = tempStrValue;
							break;
						case "r_cust_phone3":
							if (searchListResult.get(i).getCustPhone3() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCustPhone3();

							if (maskingYn.contains("r_cust_phone3")) {
								// tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
								if (StringUtil.isNull(tempStrValue, true))
									tempStrValue = "";
								// else
								// tempStrValue = new RecSeeUtil().maskingNumber(tempStrValue);
							} else {
								// tempStrValue = new RecSeeUtil().makePhoneNumber(tempStrValue);
							}

							row[colPos++] = tempStrValue;
							break;
						case "r_call_kind1":
							tempStrValue = "";
							if (searchListResult.get(i).getCallKind1() != null
									&& !searchListResult.get(i).getCallKind1().equals(""))
								tempStrValue = messageSource.getMessage(
										"call.type." + searchListResult.get(i).getCallKind1(), null,
										Locale.getDefault());

							row[colPos++] = tempStrValue;
							break;
						case "r_call_kind2":
							tempStrValue = "";
							if (searchListResult.get(i).getCallKind2() != null
									&& !searchListResult.get(i).getCallKind2().equals(""))
								tempStrValue = messageSource.getMessage(
										"call.type." + searchListResult.get(i).getCallKind2(), null,
										Locale.getDefault());

							row[colPos++] = tempStrValue;
							break;
						case "r_call_stime":
							if (searchListResult.get(i).getCallStime() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCallStime();
							row[colPos++] = tempStrValue;
							break;
						case "r_call_etime":
							if (searchListResult.get(i).getCallEtime() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCallEtime();
							row[colPos++] = tempStrValue;
							break;
						case "r_call_ttime":
							tempStrValue = "00:00:00";
							if (consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")
									&& searchListResult.get(i).getBuffer1() != null
									&& searchListResult.get(i).getBuffer1().equals("##")) {
								tempStrValue = "00:00:00";
							} else {
								// if(searchListResult.get(i).getCallTtime() != null &&
								// searchListResult.get(i).getCallTtime().length() > 0 &&
								// searchListResult.get(i).getCallTtime().length() <= 7)
								// tempStrValue = new
								// RecSeeUtil().getSecToTime(Integer.parseInt(searchListResult.get(i).getCallTtime()));
								if (searchListResult.get(i).getCallTtime() != null)
									tempStrValue = searchListResult.get(i).getCallTtime();
							}
							row[colPos++] = tempStrValue;
							break;
						case "r_call_ttime_connect":
							tempStrValue = "00:00:00";
							if (consentNoRecodingUse != null && consentNoRecodingUse.equals("Y")
									&& searchListResult.get(i).getBuffer1() != null
									&& searchListResult.get(i).getBuffer1().equals("##")) {
								tempStrValue = "00:00:00";
							} else {
								// if(searchListResult.get(i).getCallTtimeConnect() != null &&
								// searchListResult.get(i).getCallTtimeConnect().length() > 0 &&
								// searchListResult.get(i).getCallTtimeConnect().length() <= 7)
								// tempStrValue = new
								// RecSeeUtil().getSecToTime(Integer.parseInt(searchListResult.get(i).getCallTtimeConnect()));
								if (searchListResult.get(i).getCallTtimeConnect() != null
										&& searchListResult.get(i).getCallTtimeConnect().length() > 0
										&& searchListResult.get(i).getCallTtimeConnect().length() <= 7)
									tempStrValue = searchListResult.get(i).getCallTtimeConnect();
							}
							if (searchListResult.get(i).getCallStimeConnect().equals(""))
								tempStrValue = "";
							row[colPos++] = tempStrValue;
							break;
						case "r_selfdis_yn":
							if (searchListResult.get(i).getSelfDisYn() == null) {
								tempStrValue = "N";
							} else if (searchListResult.get(i).getSelfDisYn().equals("Y")) {
								tempStrValue = "Y";
							} else {
								tempStrValue = "N";
							}

							// tempStrValue = selfDisYnList.get(tempStrValue);

							row[colPos++] = tempStrValue;
							break;
						case "r_v_sys_code":
							if (searchListResult.get(i).getvSysCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getvSysCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_v_hdd_flag":
							if (searchListResult.get(i).getvHddFlag() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getvHddFlag();
							row[colPos++] = tempStrValue;
							break;
						case "r_listen_url":
							if (searchListResult.get(i).getListenUrl() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getListenUrl();
							row[colPos++] = tempStrValue;
							break;
						case "r_v_filename":
							if (searchListResult.get(i).getvFileName() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getvFileName();
							row[colPos++] = tempStrValue;
							break;
						case "r_s_sys_code":
							if (searchListResult.get(i).getsSysCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getsSysCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_s_hdd_flag":
							if (searchListResult.get(i).getsHddFlag() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getsHddFlag();
							row[colPos++] = tempStrValue;
							break;
						case "r_screen_url":
							if (searchListResult.get(i).getScreenUrl() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getScreenUrl();
							row[colPos++] = tempStrValue;
							break;
						case "r_rec_visible":
							// if(searchListResult.get(i).getRecVisible() == null) tempStrValue = "";
							// else tempStrValue = searchListResult.get(i).getRecVisible();
							// row[colPos++] = tempStrValue;
							break;
						case "r_s_filename":
							if (searchListResult.get(i).getsFileName() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getsFileName();
							row[colPos++] = tempStrValue;
							break;
						case "r_s_upload_yn":
							if (searchListResult.get(i).getsUploadYn() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getsUploadYn();
							row[colPos++] = tempStrValue;
							break;
						case "r_t_sys_code":
							if (searchListResult.get(i).gettSysCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).gettSysCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_eval_yn":
							if (searchListResult.get(i).getEvalYn() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getEvalYn();
							row[colPos++] = tempStrValue;
							break;
						case "r_text_url":
							if (searchListResult.get(i).getTextUrl() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getTextUrl();
							row[colPos++] = tempStrValue;
							break;
						case "r_listen_yn":
							if (searchListResult.get(i).getListenYn() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getListenYn();
							row[colPos++] = tempStrValue;
							break;
						case "r_t_filename":
							if (searchListResult.get(i).gettFileName() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).gettFileName();
							row[colPos++] = tempStrValue;
							break;
						case "r_t_upload_yn":
							if (searchListResult.get(i).gettUploadYn() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).gettUploadYn();
							row[colPos++] = tempStrValue;
							break;
						case "r_part_start":
							if (searchListResult.get(i).getPartStart() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getPartStart();
							row[colPos++] = tempStrValue;
							break;
						case "r_part_end":
							if (searchListResult.get(i).getPartEnd() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getPartEnd();
							row[colPos++] = tempStrValue;
							break;
						case "r_marking1":
							if (searchListResult.get(i).getMarking1() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getMarking1();
							row[colPos++] = tempStrValue;
							break;
						case "r_marking2":
							if (searchListResult.get(i).getMarking2() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getMarking2();
							row[colPos++] = tempStrValue;
							break;
						case "r_marking3":
							if (searchListResult.get(i).getMarking3() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getMarking3();
							row[colPos++] = tempStrValue;
							break;
						case "r_marking4":
							if (searchListResult.get(i).getMarking4() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getMarking4();
							row[colPos++] = tempStrValue;
							break;
						case "r_cust_social_num":
							if (searchListResult.get(i).getCustSocialNum() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCustSocialNum();
							row[colPos++] = tempStrValue;
							break;
						case "r_contract_num":
							if (searchListResult.get(i).getContractNum() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getContractNum();
							row[colPos++] = tempStrValue;
							break;
						case "r_counsel_code":
							if (searchListResult.get(i).getCounselCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCounselCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_counsel_content":
							if (searchListResult.get(i).getCounselContent() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCounselContent();
							row[colPos++] = tempStrValue;
							break;
						case "r_cust_address":
							if (searchListResult.get(i).getCustAddress() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCustAddress();
							row[colPos++] = tempStrValue;
							break;
						case "r_player_kind":
							if (searchListResult.get(i).getPlayerKind() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getPlayerKind();
							row[colPos++] = tempStrValue;
							break;
						case "r_t_contents":
							if (searchListResult.get(i).gettContents() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).gettContents();
							row[colPos++] = tempStrValue;
							break;
						case "r_call_key_ap":
							if (searchListResult.get(i).getCallKeyAp() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCallKeyAp();
							row[colPos++] = tempStrValue;
							break;
						case "r_receipt_num":
							if (searchListResult.get(i).getReceiptNum() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getReceiptNum();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer1":
							if (searchListResult.get(i).getBuffer1() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer1();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer2":
							if (searchListResult.get(i).getBuffer2() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer2();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer3":
							if (searchListResult.get(i).getBuffer3() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer3();
							row[colPos++] = tempStrValue;
							break;
						case "r_bw_yn":
							if (searchListResult.get(i).getBwYn() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBwYn();
							row[colPos++] = tempStrValue;
							break;
						case "r_bw_bg_code":
							if (searchListResult.get(i).getBwBgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBwBgCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_bw_sg_code":
							if (searchListResult.get(i).getBwSgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBwSgCode();
							row[colPos++] = tempStrValue;
							break;
						case "r_screen_dual_url":
							if (searchListResult.get(i).getScreenDualUrl() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getScreenDualUrl();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer4":
							if (searchListResult.get(i).getBuffer4() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer4();
							
							if("1".equals(tempStrValue)) {
								tempStrValue = "신탁";
							}else if("2".equals(tempStrValue)) {
								tempStrValue = "펀드";
							}else if("3".equals(tempStrValue)) {
								tempStrValue = "공통";
							}else if("4".equals(tempStrValue)) {
								tempStrValue = "방카";
							}else if("5".equals(tempStrValue)) {
								tempStrValue = "퇴직연금";
							}
							
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer5":
							if (searchListResult.get(i).getBuffer5() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer5();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer6":
							if (searchListResult.get(i).getBuffer6() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer6();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer7":
							if (searchListResult.get(i).getBuffer7() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer7();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer8":
							if (searchListResult.get(i).getBuffer8() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer8();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer9":
							if (searchListResult.get(i).getBuffer9() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer9();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer10":
							if (searchListResult.get(i).getBuffer10() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer10();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer11":
							if (searchListResult.get(i).getBuffer11() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer11();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer12":
							if (searchListResult.get(i).getBuffer12() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer12();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer13":
							if (searchListResult.get(i).getBuffer13() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer13();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer14":
							if (searchListResult.get(i).getBuffer14() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer14();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer15":
							if (searchListResult.get(i).getBuffer15() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer15();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer16":
							if (searchListResult.get(i).getBuffer16() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer16();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer17":
							if (searchListResult.get(i).getBuffer17() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer17();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer18":
							if (searchListResult.get(i).getBuffer18() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer18();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer19":
							if (searchListResult.get(i).getBuffer19() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer19();
							row[colPos++] = tempStrValue;
							break;
						case "r_buffer20":
							if (searchListResult.get(i).getBuffer20() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getBuffer20();
							row[colPos++] = tempStrValue;
							break;
						case "r_cnid":
							if (searchListResult.get(i).getCnId() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCnId();
							row[colPos++] = tempStrValue;
							break;
						case "r_dest_ip":
							if (searchListResult.get(i).getDestIp() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getDestIp();
							row[colPos++] = tempStrValue;
							break;
						case "r_bg_name":
							if (searchListResult.get(i).getBgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = new FindOrganizationUtil()
										.getOrganizationName(searchListResult.get(i).getBgCode(), organizationBgInfo);

							row[colPos++] = tempStrValue;
							break;
						case "r_mg_name":
							if (searchListResult.get(i).getMgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = new FindOrganizationUtil().getOrganizationName(
										searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(),
										organizationMgInfo);

							row[colPos++] = tempStrValue;
							break;
						case "r_sg_name":
							if (searchListResult.get(i).getSgCode() == null)
								tempStrValue = "";
							else
								tempStrValue = new FindOrganizationUtil().getOrganizationName(
										searchListResult.get(i).getBgCode(), searchListResult.get(i).getMgCode(),
										searchListResult.get(i).getSgCode(), organizationSgInfo);

							row[colPos++] = tempStrValue;
							break;
						case "r_queue_no1":
							if (searchListResult.get(i).getQueueNo1() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getQueueNo1();
							// else tempStrValue = queueList.get(searchListResult.get(i).getQueueNo1());

							row[colPos++] = tempStrValue;
							break;
						case "r_queue_no2":
							if (searchListResult.get(i).getQueueNo2() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getQueueNo2();
							// else tempStrValue = queueList.get(searchListResult.get(i).getQueueNo2());

							row[colPos++] = tempStrValue;
							break;
						case "r_rec_start_type":
							if (StringUtil.isNull(searchListResult.get(i).getRecStartType(), true))
								tempStrValue = "A";
							else
								tempStrValue = startTypeList.get(searchListResult.get(i).getRecStartType());

							row[colPos++] = tempStrValue;

							break;

						case "r_stock_no":
							if (searchListResult.get(i).getStockNo() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getStockNo();

							row[colPos++] = tempStrValue;
							break;

						case "r_counsel_result_bgcode":
							if (StringUtil.isNull(searchListResult.get(i).getCounselResultBgcode(), true))
								tempStrValue = "";
							else
								tempStrValue = counselBgList.get(searchListResult.get(i).getCounselResultBgcode());

							row[colPos++] = tempStrValue;
							break;
						case "r_counsel_result_mgcode":
							if (StringUtil.isNull(searchListResult.get(i).getCounselResultMgcode(), true))
								tempStrValue = "";
							else
								tempStrValue = counselMgList.get(searchListResult.get(i).getCounselResultMgcode());

							row[colPos++] = tempStrValue;
							break;
						case "r_counsel_result_sgcode":
							if (StringUtil.isNull(searchListResult.get(i).getCounselResultSgcode(), true))
								tempStrValue = "";
							else
								tempStrValue = counselSgList.get(searchListResult.get(i).getCounselResultSgcode());

							row[colPos++] = tempStrValue;
							break;
						case "rownumber":
							if (searchListResult.get(i).getRownumber() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getRownumber();

							if (StringUtil.isNull(tempStrValue, true))
								tempStrValue = "";

							row[colPos++] = tempStrValue;
							break;
						// 20200421 jbs 엑셀다운로드 시 자번호 데이터 추가 *start
						case "r_company_telno":
							if (searchListResult.get(i).getCompanyTelno() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCompanyTelno();
							row[colPos++] = tempStrValue;
							break;
						case "r_company_telno_nick":
							if (searchListResult.get(i).getCompanyTelnoNick() == null)
								tempStrValue = "";
							else
								tempStrValue = searchListResult.get(i).getCompanyTelnoNick();
							row[colPos++] = tempStrValue;
							break;
						// 20200421 jbs 엑셀다운로드 시 자번호 데이터 추가 *end
						}
					}
					contents.add(row);
				}

				ModelMap.put("excelList", contents);
				ModelMap.put("target", request.getParameter("fileName"));
				logService.writeLog(request, "EXCELDOWN", "DO", searchListInfo.toLogString());
			}
		}
		String realPath = request.getSession().getServletContext().getRealPath("/search");
		ExcelView.createXlsx(ModelMap, realPath, response);

		// logInfoService.writeLog(request, "Search - Excel download", null,
		// userInfo.getUserId());
	}

	@RequestMapping(value = "/recMemoSelect.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO recMemoSelect(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			RecMemo recMemo = new RecMemo();

			String nowDate = DateUtil.toString(new Date(), "yyyyMMdd");
			recMemo.setRecDate(nowDate);

			if (!StringUtil.isNull(request.getParameter("rectime"), true))
				recMemo.setRecTime(request.getParameter("rectime"));
			if (!StringUtil.isNull(request.getParameter("extNum"), true))
				recMemo.setStartTime(request.getParameter("extNum"));

			List<RecMemo> recMemoSelect = recMemoService.selectRecMemo(recMemo);

			if (recMemoSelect.size() == 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("contents", recMemoSelect.get(0).getMemo());
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;

	}

	// 승인, 플레이어 리슨 url 반환
	@RequestMapping(value = "/getListenUrl.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO getListenUrl(HttpServletRequest request, Locale local, Model model)
			throws UnknownHostException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			// etc config의 listen type이 URL이거나 PATH이거나에 따른 분기

			String ip = null;
			String port = null;
			String url = null;
			String http = "http";
			String urlEncYn = null;

			// https 사용 유무 체크
			EtcConfigInfo listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("SYSTEM");
			listenTYpe.setConfigKey("HTTPS");
			List<EtcConfigInfo> HttpSResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);

			if ("https".equals(HttpSResult.get(0).getConfigValue())) {
				http = "https";
			}

			listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("LISTEN");
			listenTYpe.setConfigKey("TYPE");

			List<EtcConfigInfo> listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);

			listenTYpe.setConfigKey("PORT");

			port = etcConfigInfoService.selectEtcConfigInfo(listenTYpe).get(0).getConfigValue();

			listenTYpe.setConfigKey("URL_ENC_YN");
			urlEncYn = etcConfigInfoService.selectEtcConfigInfo(listenTYpe).get(0).getConfigValue();
			// 리슨타입이 path일떄

			SearchListInfo searchInfo = new SearchListInfo();

			if (!StringUtil.isNull(request.getParameter("recDate"), true))
				searchInfo.setRecDateRaw(request.getParameter("recDate").replaceAll("-", ""));
			if (!StringUtil.isNull(request.getParameter("recTime"), true))
				searchInfo.setRecTimeRaw(request.getParameter("recTime").replaceAll(":", ""));
			if (!StringUtil.isNull(request.getParameter("recExt"), true))
				searchInfo.setExtNum(request.getParameter("recExt").replaceAll("-", ""));
			if (!StringUtil.isNull(request.getParameter("listenUrl"), true))
				searchInfo.setListenUrl(request.getParameter("listenUrl"));
			// vSysCode
			if (!StringUtil.isNull(request.getParameter("vSysCode"), true))
				searchInfo.setvSysCode(request.getParameter("vSysCode"));
			if (!StringUtil.isNull(request.getParameter("sysCode"), true))
				searchInfo.setvSysCode(request.getParameter("sysCode"));
			if (!StringUtil.isNull(request.getParameter("recStartType"), true))
				searchInfo.setRecStartType(request.getParameter("recStartType"));

			searchInfo.setUrlEncYn(urlEncYn);

			List<SearchListInfo> ListenUrl = null;

			// 내/외부망 구분 사용여부 확인
			EtcConfigInfo public_ip_yn = new EtcConfigInfo();
			public_ip_yn.setGroupKey("LISTEN");
			public_ip_yn.setConfigKey("PUBLIC_IP");
			List<EtcConfigInfo> public_ip_yn_result = null;
			String yn = "N";
			try {
				public_ip_yn_result = etcConfigInfoService.selectEtcConfigInfo(public_ip_yn);
				yn = public_ip_yn_result.get(0).getConfigValue();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if ("Y".equals(yn)) {
				String oriUrl = "";
				PublicIpInfo publicIpInfo = new PublicIpInfo();
				publicIpInfo.setrPublicIp(request.getServerName());

				try {
					publicIpInfo = publicIpInfoService.selectOnePublicIpInfo(publicIpInfo);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
					ListenUrl = searchListInfoService.selectURL(searchInfo);
					if (publicIpInfo == null) {// 내부vs외부망 체크
						oriUrl = ListenUrl.get(0).getListenUrl();
					} else {
						oriUrl = ListenUrl.get(0).getTextUrl();
					}
					// HTTP://localhost:28881/listen?url=D:/REC/RecSee_Data/20200620/13/202006201302060124_COMPANYTEL_O_CID_A_BGNAME_MGNAME_SGNAME.mp3

					// db에서 가져올 때 암호화 하므로 인코딩 필요 X - 2021.08.19 retina
//					try {
//						url = oriUrl.replace(oriUrl.split("url=")[1],
//								URLEncoder.encode(oriUrl.split("url=")[1], "UTF-8"));
//					} catch (UnsupportedEncodingException e) {
//						url = oriUrl;
//						e.printStackTrace();
//					}
					url = oriUrl;

					if ("https".equals(http)) {
						url = url.replace("HTTP", "HTTPS").replace("http", "https").replace("28881", port);
					}

				} else {
					ListenUrl = searchListInfoService.selectFullPath(searchInfo);
//					try {
//						url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url="
//								+ URLEncoder.encode(ListenUrl.get(0).getvRecFullpath(), "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url="
//								+ ListenUrl.get(0).getvRecFullpath();
//						e.printStackTrace();
//					}
					url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url="
							+ ListenUrl.get(0).getvRecFullpath();
				}
			} else {

				if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
					ListenUrl = searchListInfoService.selectURL(searchInfo);
					if ("https".equals(http)) {
						url = ListenUrl.get(0).getListenUrl().replace("HTTP", "HTTPS").replace("http", "https")
								.replace("28881", port);
					} else {
						url = ListenUrl.get(0).getListenUrl();
					}
				} else {
					ListenUrl = searchListInfoService.selectFullPath(searchInfo);
					// 파일명에 포함되는 한글 인코딩
//					try {
//						url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url="
//								+ URLEncoder.encode(ListenUrl.get(0).getvRecFullpath(), "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url="
//								+ ListenUrl.get(0).getvRecFullpath();
//						e.printStackTrace();
//					}
					url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url="
							+ ListenUrl.get(0).getvRecFullpath();

				}
			}

			listenTYpe.setGroupKey("LISTEN");
			listenTYpe.setConfigKey("WAVE");

			listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);
			if (!StringUtil.isNull(listenTypeResult.get(0).getConfigValue())) {
				jRes.addAttribute("WaveType", listenTypeResult.get(0).getConfigValue());
			} else {
				jRes.addAttribute("WaveType", "rsfft");
			}

			if (ListenUrl.size() == 1) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("ListenUrl", url);
				if ("IVR".equals(ListenUrl.get(0).getIvrChk()))
					jRes.addAttribute("IVR", ListenUrl.get(0).getIvrChk());
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 녹취 정보 변경 팝업 표출용
	@RequestMapping(value = "/getRsRecfileInfo.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO getRsRecfileInfo(HttpServletRequest request, Locale local, Model model)
			throws UnknownHostException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			SearchListInfo searchInfo = new SearchListInfo();

			if (!StringUtil.isNull(request.getParameter("recDate"), true))
				searchInfo.setRecDateRaw(request.getParameter("recDate").replaceAll("-", ""));
			if (!StringUtil.isNull(request.getParameter("recTime"), true))
				searchInfo.setRecTimeRaw(request.getParameter("recTime").replaceAll(":", ""));
			if (!StringUtil.isNull(request.getParameter("recExt"), true))
				searchInfo.setExtNum(request.getParameter("recExt"));
			if (!StringUtil.isNull(request.getParameter("sysCode"), true))
				searchInfo.setvSysCode(request.getParameter("sysCode"));

			EtcConfigInfo rsRecfile_Update_column = new EtcConfigInfo();
			rsRecfile_Update_column.setGroupKey("RSRECFILE");
			rsRecfile_Update_column.setConfigKey("UPDATECOLUMN");
			List<EtcConfigInfo> rsRecfile_Update_column_Result = etcConfigInfoService
					.selectEtcConfigInfo(rsRecfile_Update_column);

			String[] Columns = rsRecfile_Update_column_Result.get(0).getConfigValue().split(",");

			ArrayList<String> UpdateColumn = new ArrayList<>();
			for (int i = 0; i < Columns.length; i++) {
				UpdateColumn.add(Columns[i]);
			}

			searchInfo.setUpdateColumn(UpdateColumn);

			List<SearchListInfo> SearchData = searchListInfoService.selectRsRecfileInfo(searchInfo);

			if (SearchData.size() == 1) {

				// Postgres 암호화 사용여부
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				String postgresColumn = "";
				etcConfigInfo.setGroupKey("ENCRYPT");
				etcConfigInfo.setConfigKey("POSTGRES");
				List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				if (etcConfigResult.size() > 0) {
					if ("Y".equals(etcConfigResult.get(0).getConfigValue())) {
						etcConfigInfo.setGroupKey("ENCRYPT");
						etcConfigInfo.setConfigKey("COLUMN");

						etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

						postgresColumn = etcConfigResult.get(0).getConfigValue();
					}
				} else {
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
					etcConfigInfo.setGroupKey("ENCRYPT");
					etcConfigInfo.setConfigKey("COLUMN");
					etcConfigInfo.setConfigValue("N");
					etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
				}

				SearchListInfo returnSearchData = SearchData.get(0);
				Map<String, String> MapData = new HashMap<>();

				for (int i = 0; i < Columns.length; i++) {
					String temp = "";

					if (Columns[i].trim().equals("r_cust_name")) {
						temp = returnSearchData.getCustName();
						if (postgresColumn.contains("r_cust_name") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_cust_name", temp);
					} else if (Columns[i].trim().equals("r_cust_phone1")) {
						temp = returnSearchData.getCustPhone1();
						if (postgresColumn.contains("r_cust_phone1") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_cust_phone1", temp);
					} else if (Columns[i].trim().equals("r_cust_phone_ap")) {
						temp = returnSearchData.getrCustPhoneAp();
						if (postgresColumn.contains("r_cust_phone_ap")
								&& (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_cust_phone_ap", temp);
					} else if (Columns[i].trim().equals("r_cust_social_num")) {
						temp = returnSearchData.getCustSocialNum();
						if (postgresColumn.contains("r_cust_social_num")
								&& (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_cust_social_num", temp);
					} else if (Columns[i].trim().equals("r_call_key_ap")) {
						temp = returnSearchData.getCallKeyAp();
						if (postgresColumn.contains("r_call_key_ap") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_call_key_ap", temp);
					} else if (Columns[i].trim().equals("r_buffer1")) {
						temp = returnSearchData.getBuffer1();
						if (postgresColumn.contains("r_buffer1") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer1", temp);
					} else if (Columns[i].trim().equals("r_buffer2")) {
						temp = returnSearchData.getBuffer2();
						if (postgresColumn.contains("r_buffer2") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer2", temp);
					} else if (Columns[i].trim().equals("r_buffer3")) {
						temp = returnSearchData.getBuffer3();
						if (postgresColumn.contains("r_buffer3") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer3", temp);
					} else if (Columns[i].trim().equals("r_buffer4")) {
						temp = returnSearchData.getBuffer4();
						if (postgresColumn.contains("r_buffer4") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer4", temp);
					} else if (Columns[i].trim().equals("r_buffer5")) {
						temp = returnSearchData.getBuffer5();
						if (postgresColumn.contains("r_buffer5") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer5", temp);
					} else if (Columns[i].trim().equals("r_buffer6")) {
						temp = returnSearchData.getBuffer6();
						if (postgresColumn.contains("r_buffer6") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer6", temp);
					} else if (Columns[i].trim().equals("r_buffer7")) {
						temp = returnSearchData.getBuffer7();
						if (postgresColumn.contains("r_buffer7") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer7", temp);
					} else if (Columns[i].trim().equals("r_buffer8")) {
						temp = returnSearchData.getBuffer8();
						if (postgresColumn.contains("r_buffer8") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer8", temp);
					} else if (Columns[i].trim().equals("r_buffer9")) {
						temp = returnSearchData.getBuffer9();
						if (postgresColumn.contains("r_buffer9") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer9", temp);
					} else if (Columns[i].trim().equals("r_buffer10")) {
						temp = returnSearchData.getBuffer10();
						if (postgresColumn.contains("r_buffer10") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer10", temp);
					} else if (Columns[i].trim().equals("r_buffer11")) {
						temp = returnSearchData.getBuffer11();
						if (postgresColumn.contains("r_buffer11") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer11", temp);
					} else if (Columns[i].trim().equals("r_buffer12")) {
						temp = returnSearchData.getBuffer12();
						if (postgresColumn.contains("r_buffer12") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer12", temp);
					} else if (Columns[i].trim().equals("r_buffer13")) {
						temp = returnSearchData.getBuffer13();
						if (postgresColumn.contains("r_buffer13") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer13", temp);
					} else if (Columns[i].trim().equals("r_buffer14")) {
						temp = returnSearchData.getBuffer14();
						if (postgresColumn.contains("r_buffer14") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer14", temp);
					} else if (Columns[i].trim().equals("r_buffer15")) {
						temp = returnSearchData.getBuffer15();
						if (postgresColumn.contains("r_buffer15") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer15", temp);
					} else if (Columns[i].trim().equals("r_buffer16")) {
						temp = returnSearchData.getBuffer16();
						if (postgresColumn.contains("r_buffer16") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer16", temp);
					} else if (Columns[i].trim().equals("r_buffer17")) {
						temp = returnSearchData.getBuffer17();
						if (postgresColumn.contains("r_buffer17") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer17", temp);
					} else if (Columns[i].trim().equals("r_buffer18")) {
						temp = returnSearchData.getBuffer18();
						if (postgresColumn.contains("r_buffer18") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer18", temp);
					} else if (Columns[i].trim().equals("r_buffer19")) {
						temp = returnSearchData.getBuffer19();
						if (postgresColumn.contains("r_buffer19") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer19", temp);
					} else if (Columns[i].trim().equals("r_buffer20")) {
						temp = returnSearchData.getBuffer20();
						if (postgresColumn.contains("r_buffer20") && (temp.length() == 32 || temp.length() == 64)) {
							temp = searchListInfoService.selectPgDecoding(temp);
						}
						MapData.put("r_buffer20", temp);
					}

				}

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("SearchData", MapData);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 녹취 정보 변경 팝업 표출용
	@RequestMapping(value = "/updateRsRecfileInfo.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO updateRsRecfileInfo(HttpServletRequest request, Locale local, Model model)
			throws UnknownHostException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			Map<String, String> param = request.getParameterMap();
			System.out.println(param.size());
			SearchListInfo searchInfo = new SearchListInfo();
			searchInfo.setParamMap(request, "N", "Y");

			Integer updateResult = searchListInfoService.updateRsRecfileInfo(searchInfo);

			if (updateResult == 1) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 전체 파일 삭제
	@RequestMapping(value = "/delete_all.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO delete_all(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			SearchListInfo searchListInfo = new SearchListInfo();
			searchListInfo.setParamMap(request, "N", "Y");

			if (!StringUtil.isNull(request.getParameter("sgCode"), true))
				searchListInfo.setSgCodeArray(
						new ArrayList<String>(Arrays.asList(request.getParameter("sgCode").split(","))));

			if (!StringUtil.isNull(request.getParameter("buffer12"), true))
				searchListInfo.setBuffer12Array(
						new ArrayList<String>(Arrays.asList(request.getParameter("buffer12").split(","))));

			if (!StringUtil.isNull(request.getParameter("buffer13"), true))
				searchListInfo.setBuffer13Array(
						new ArrayList<String>(Arrays.asList(request.getParameter("buffer13").split(","))));

			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10002");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

			List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
			if (accessResult.get(0).getAccessLevel().substring(0, 1).equals("R")) {
				List<AllowableRangeInfo> allowableList = null;

				AllowableRangeInfo allowableRangeInfoChk = new AllowableRangeInfo();
				allowableRangeInfoChk.setrAllowableCode(accessResult.get(0).getAccessLevel());
				allowableList = allowableRangeInfoService.selectAllowableRangeInfo(allowableRangeInfoChk);
				if (allowableList.size() > 0) {
					for (int i = 0; i < allowableList.size(); i++) {
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("bgcode", allowableList.get(i).getrBgCode());
						item.put("mgcode", allowableList.get(i).getrMgCode());
						item.put("sgcode", allowableList.get(i).getrSgCode());
						authyInfo.add(item);
					}
				} else {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("noneallowable", "noneallowable");
					authyInfo.add(item);
				}

			} else {
				if (!accessResult.get(0).getAccessLevel().equals("A")) {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("bgcode", userInfo.getBgCode());
					if (!accessResult.get(0).getAccessLevel().equals("B")) {
						item.put("mgcode", userInfo.getMgCode());
					}
					if (!accessResult.get(0).getAccessLevel().equals("B")
							&& !accessResult.get(0).getAccessLevel().equals("M")) {
						item.put("sgcode", userInfo.getSgCode());
					}
					if (!accessResult.get(0).getAccessLevel().equals("B")
							&& !accessResult.get(0).getAccessLevel().equals("M")
							&& !accessResult.get(0).getAccessLevel().equals("S")) {
						item.put("user", userInfo.getUserId());
					}

					authyInfo.add(item);
				}
			}
			if (authyInfo != null && authyInfo.size() > 0) {
				searchListInfo.setAuthyInfo(authyInfo);
			}

			try {
				accessInfo = new MMenuAccessInfo();
				// 콜 타입 조회 권한
				accessInfo.setLevelCode(userInfo.getUserLevel());
				accessInfo.setProgramCode("P20002");
				accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
				// 수신, 발신, 전환수신, 전환송신, 내선
				String[] Calltype = { "Y", "Y", "Y", "Y", "Y", "Y" };

				if (accessResult != null && accessResult.size() > 0) {
					// 사용유무
					if (accessResult.get(0).getReadYn().equals("N")) {
						Calltype[0] = "N";
					}
					// 수신
					if (accessResult.get(0).getWriteYn().equals("N")) {
						Calltype[1] = "N";
					}
					// 발신
					if (accessResult.get(0).getModiYn().equals("N")) {
						Calltype[2] = "N";
					}
					// 전환수신
					if (accessResult.get(0).getDelYn().equals("N")) {
						Calltype[3] = "N";
					}
					// 전환송신
					if (accessResult.get(0).getListenYn().equals("N")) {
						Calltype[4] = "N";
					}
					// 내선
					if (accessResult.get(0).getDownloadYn().equals("N")) {
						Calltype[5] = "N";
					}
				}

				if ("Y".equals(Calltype[0])) {
					ArrayList<String> callType = new ArrayList<>();
					if ("Y".equals(Calltype[1])) {
						callType.add("I");
					}
					if ("Y".equals(Calltype[2])) {
						callType.add("O");
					}
					if ("Y".equals(Calltype[3])) {
						callType.add("TR");
					}
					if ("Y".equals(Calltype[4])) {
						callType.add("TS");
					}
					if ("Y".equals(Calltype[5])) {
						callType.add("Z");
					}

					if (callType.size() > 0) {
						searchListInfo.setRecCallType(callType);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 녹취 기간 제한 설정
			accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P20003");
			try {
				accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

				if ("Y".equals(accessResult.get(0).getReadYn())) {
					int recDate = accessResult.get(0).getTopPriority();

					searchListInfo.setRecDateLimit(String.valueOf(recDate));

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			String whereQuery = searchListInfoService.selectWhere(searchListInfo);
			whereQuery = whereQuery.replaceAll("(\r|\n|\r\n|\n\r)", " ");
			whereQuery = whereQuery.replaceAll("\\s", " ").trim().replaceAll("  ", " ").replaceAll("  ", " ");

			String port = null;
			String url = null;
			String http = "http";

			// https 사용 유무 체크
			EtcConfigInfo listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("SYSTEM");
			listenTYpe.setConfigKey("HTTPS");
			List<EtcConfigInfo> HttpSResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);

			if ("https".equals(HttpSResult.get(0).getConfigValue())) {
				http = "https";
			}

			listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("LISTEN");
			listenTYpe.setConfigKey("TYPE");

			List<EtcConfigInfo> listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);

			List<SearchListInfo> serverIpList = new ArrayList<SearchListInfo>();

			if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
				serverIpList = searchListInfoService.selectRecIp(searchListInfo);
			} else {
				serverIpList = searchListInfoService.selectRecIp2(searchListInfo);
			}

			listenTYpe.setConfigKey("PORT");

			port = etcConfigInfoService.selectEtcConfigInfo(listenTYpe).get(0).getConfigValue();

			List<String> serverip = new ArrayList<String>();

			if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
				for (int i = 0; i < serverIpList.size(); i++) {
					url = http + "://" + serverIpList.get(i).getListenUrl() + ":" + port + "/";
					serverip.add(url);
				}
			} else {
				for (int i = 0; i < serverIpList.size(); i++) {
					url = http + "://" + serverIpList.get(i).getvRecIp() + ":" + port + "/";
					serverip.add(url);
				}
			}

			if (serverIpList.size() > 0) {
				logService.writeLog(request, "REC_DELETE", "DO",
						"user_id=" + userInfo.getUserId() + ", query=" + whereQuery);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("serverIpList", serverip);
				jRes.addAttribute("whereQuery", whereQuery);
				jRes.addAttribute("listenType", listenTypeResult.get(0).getConfigValue());
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;

	}

	// 선택 파일 삭제
	@RequestMapping(value = "/delete_select.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO delete_select(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			SearchListInfo searchListInfo = new SearchListInfo();
			searchListInfo.setParamMap(request, "N", "Y");

			if (!StringUtil.isNull(request.getParameter("sgCode"), true))
				searchListInfo.setSgCodeArray(
						new ArrayList<String>(Arrays.asList(request.getParameter("sgCode").split(","))));

			if (!StringUtil.isNull(request.getParameter("buffer12"), true))
				searchListInfo.setBuffer12Array(
						new ArrayList<String>(Arrays.asList(request.getParameter("buffer12").split(","))));

			if (!StringUtil.isNull(request.getParameter("buffer13"), true))
				searchListInfo.setBuffer13Array(
						new ArrayList<String>(Arrays.asList(request.getParameter("buffer13").split(","))));

			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10002");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);

			List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
			if (accessResult.get(0).getAccessLevel().substring(0, 1).equals("R")) {
				List<AllowableRangeInfo> allowableList = null;

				AllowableRangeInfo allowableRangeInfoChk = new AllowableRangeInfo();
				allowableRangeInfoChk.setrAllowableCode(accessResult.get(0).getAccessLevel());
				allowableList = allowableRangeInfoService.selectAllowableRangeInfo(allowableRangeInfoChk);
				if (allowableList.size() > 0) {
					for (int i = 0; i < allowableList.size(); i++) {
						HashMap<String, String> item = new HashMap<String, String>();
						item.put("bgcode", allowableList.get(i).getrBgCode());
						item.put("mgcode", allowableList.get(i).getrMgCode());
						item.put("sgcode", allowableList.get(i).getrSgCode());
						authyInfo.add(item);
					}
				} else {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("noneallowable", "noneallowable");
					authyInfo.add(item);
				}

			} else {
				if (!accessResult.get(0).getAccessLevel().equals("A")) {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("bgcode", userInfo.getBgCode());
					if (!accessResult.get(0).getAccessLevel().equals("B")) {
						item.put("mgcode", userInfo.getMgCode());
					}
					if (!accessResult.get(0).getAccessLevel().equals("B")
							&& !accessResult.get(0).getAccessLevel().equals("M")) {
						item.put("sgcode", userInfo.getSgCode());
					}
					if (!accessResult.get(0).getAccessLevel().equals("B")
							&& !accessResult.get(0).getAccessLevel().equals("M")
							&& !accessResult.get(0).getAccessLevel().equals("S")) {
						item.put("user", userInfo.getUserId());
					}

					authyInfo.add(item);
				}
			}
			if (authyInfo != null && authyInfo.size() > 0) {
				searchListInfo.setAuthyInfo(authyInfo);
			}

			String whereQuery = searchListInfoService.selectWhere(searchListInfo);
			whereQuery = whereQuery.replaceAll("(\r|\n|\r\n|\n\r)", " ");
			whereQuery = whereQuery.replaceAll("\\s", " ").trim().replaceAll("  ", " ").replaceAll("  ", " ");
			// where r_rec_date between a and b

			whereQuery = whereQuery + " AND R_LISTEN_URL IN ('";
			String[] chList = request.getParameter("chList").split(",");

			// ListenUrl 암호화된 경우 복호화 처리 먼저
			EtcConfigInfo urlEncYn = new EtcConfigInfo();
			urlEncYn.setGroupKey("LISTEN");
			urlEncYn.setConfigKey("URL_ENC_YN");
			urlEncYn = etcConfigInfoService.selectOptionYN(urlEncYn);

			for (int i = 0; i < chList.length; i++) {
				String ch = chList[i];
				if (urlEncYn.getConfigValue().equals("Y")) {
					ch = searchListInfoService.selectUrlDecrypt(ch);
				}
				if (i == chList.length - 1) {
					whereQuery = whereQuery + ch + "')";
				} else {
					whereQuery = whereQuery + ch + "', ";
				}
			}
			// EX) R_REC_DATE BETWEEN '20200422' AND '20200422' AND R_LISTEN_URL IN
			// ('HTTP://192.168.0.169:28881/listen?url=C:\project\192.168.0.31\RecSee_Data\RecSee_Data\20200422\11\202004221115410032_1001_1001_A.mp3',
			// 'HTTP://192.168.0.169:28881/listen?url=C:\project\192.168.0.31\RecSee_Data\RecSee_Data\20200422\11\202004221112280241_1001_1001_A.mp3')

			String port = null;
			String url = null;
			String http = "http";

			// https 사용 유무 체크
			EtcConfigInfo listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("SYSTEM");
			listenTYpe.setConfigKey("HTTPS");
			List<EtcConfigInfo> HttpSResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);

			if ("https".equals(HttpSResult.get(0).getConfigValue())) {
				http = "https";
			}

			listenTYpe = new EtcConfigInfo();
			listenTYpe.setGroupKey("LISTEN");
			listenTYpe.setConfigKey("TYPE");

			List<EtcConfigInfo> listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(listenTYpe);

			List<SearchListInfo> serverIpList = new ArrayList<SearchListInfo>();

			ArrayList<String> chArrayList = new ArrayList<>();
			for (int i = 0; i < chList.length; i++) {
				chArrayList.add(chList[i]);
			}
			searchListInfo.setListenUrlList(chArrayList);
			if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
				serverIpList = searchListInfoService.selectRecIp3(searchListInfo);
			} else {
				serverIpList = searchListInfoService.selectRecIp4(searchListInfo);
			}

			listenTYpe.setConfigKey("PORT");

			port = etcConfigInfoService.selectEtcConfigInfo(listenTYpe).get(0).getConfigValue();

			List<String> serverip = new ArrayList<String>();
			if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
				for (int i = 0; i < serverIpList.size(); i++) {
					url = http + "://" + serverIpList.get(i).getListenUrl() + ":" + port + "/";
					serverip.add(url);
				}
			} else {
				for (int i = 0; i < serverIpList.size(); i++) {
					url = http + "://" + serverIpList.get(i).getvRecIp() + ":" + port + "/";
					serverip.add(url);
				}
			}

			if (serverIpList.size() > 0) {
				logService.writeLog(request, "REC_DELETE", "DO",
						"user_id=" + userInfo.getUserId() + ", query=" + whereQuery);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("serverIpList", serverip);
				jRes.addAttribute("whereQuery", whereQuery);
				jRes.addAttribute("listenType", listenTypeResult.get(0).getConfigValue());
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;

	}

	// 파일 비활성 r_rec_visible 'H' 처리
	@RequestMapping(value = "/inActive.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO inActive(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			String[] rowList = request.getParameter("rowList").split(",");

			String rowListLog = "[";
			ArrayList<String> rowArrayList = new ArrayList<>();
			for (int i = 0; i < rowList.length; i++) {
				if (i == rowList.length - 1) {
					rowListLog = rowListLog + rowList[i];
				} else {
					rowListLog = rowListLog + rowList[i] + ", ";
				}
				rowArrayList.add(rowList[i]);
			}
			rowListLog = rowListLog + "]";

			SearchListInfo searchListInfo = new SearchListInfo();
			searchListInfo.setListenUrlList(rowArrayList);

			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("LISTEN");
			etcConfigInfo.setConfigKey("URL_ENC_YN");
			String urlEncYn = etcConfigInfoService.selectOptionYN(etcConfigInfo).getConfigValue();
			searchListInfo.setUrlEncYn(urlEncYn);

			Integer inActiveResult = searchListInfoService.updateInActive(searchListInfo);

			if (inActiveResult > 0) {
				logService.writeLog(request, "REC_DELETE", "DO",
						"user_id=" + userInfo.getUserId() + ", inActive=" + rowListLog);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 일괄다운로드
	/*
	 * @RequestMapping(value = "/fullDownload.do", produces =
	 * "text/plain;charset=UTF-8") public @ResponseBody AJaxResVO fullDownload(final
	 * HttpServletRequest request, Locale local, Model model) { AJaxResVO jRes = new
	 * AJaxResVO(); final LoginVO userInfo = SessionManager.getUserInfo(request); if
	 * (userInfo != null) {
	 * 
	 * // 저장 경로 EtcConfigInfo pathInfo = new EtcConfigInfo();
	 * pathInfo.setGroupKey("UPLOAD"); pathInfo.setConfigKey("BESTCALL_FILE_PATH");
	 * List<EtcConfigInfo> pathInfoResult =
	 * etcConfigInfoService.selectEtcConfigInfo(pathInfo);
	 * 
	 * final String zipPath = pathInfoResult.get(0).getConfigValue(); final String
	 * savePath = pathInfoResult.get(0).getConfigValue() + "/" +
	 * userInfo.getUserId() + System.currentTimeMillis();
	 * 
	 * File folder = new File(zipPath); if (!folder.exists()) { try {
	 * folder.mkdirs(); // 폴더 생성합니다. } catch (Exception e) { e.getStackTrace(); } }
	 * 
	 * // IE의 경우 압축생성시간이 길어지면 클라이언트에서 리턴값을 못받는 경우가 있음... 그래서 리턴값 먼저주고 압축파일 만들게함(압축파일
	 * // 완성여부는 keepAlive 통해서 확인하도록 함) Runnable r = new Runnable() { private
	 * HttpServletRequest request; public Runnable init(HttpServletRequest request)
	 * { this.request = request; return this; }
	 * 
	 * @Override public void run() { HttpSession session =
	 * this.request.getSession();
	 * 
	 * // String savePath=""; String encYn = "false";
	 * 
	 * int fileCount = 0;
	 * 
	 * List<SearchListInfo> fileList = new ArrayList<SearchListInfo>();
	 * SearchListInfo searchListInfo = new SearchListInfo();
	 * searchListInfo.setParamMap(this.request, "N", "Y"); if
	 * (!StringUtil.isNull(this.request.getParameter("encYn"), true)) encYn =
	 * this.request.getParameter("encYn");
	 * 
	 * if (!StringUtil.isNull(this.request.getParameter("sgCode"), true))
	 * searchListInfo.setSgCodeArray( new
	 * ArrayList<String>(Arrays.asList(this.request.getParameter("sgCode").split(","
	 * ))));
	 * 
	 * if (!StringUtil.isNull(this.request.getParameter("buffer12"), true))
	 * searchListInfo.setBuffer12Array( new
	 * ArrayList<String>(Arrays.asList(this.request.getParameter("buffer12").split(
	 * ","))));
	 * 
	 * if (!StringUtil.isNull(this.request.getParameter("buffer13"), true))
	 * searchListInfo.setBuffer13Array( new
	 * ArrayList<String>(Arrays.asList(this.request.getParameter("buffer13").split(
	 * ","))));
	 * 
	 * MMenuAccessInfo accessInfo = new MMenuAccessInfo();
	 * accessInfo.setLevelCode(userInfo.getUserLevel());
	 * accessInfo.setProgramCode("P10002"); List<MMenuAccessInfo> accessResult =
	 * menuAccessInfoService.checkAccessInfo(accessInfo);
	 * 
	 * List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String,
	 * String>>(); if (!accessResult.get(0).getAccessLevel().equals("A")) {
	 * HashMap<String, String> item = new HashMap<String, String>();
	 * 
	 * item.put("bgcode", userInfo.getBgCode()); if
	 * (!accessResult.get(0).getAccessLevel().equals("B")) { item.put("mgcode",
	 * userInfo.getMgCode()); } if
	 * (!accessResult.get(0).getAccessLevel().equals("B") &&
	 * !accessResult.get(0).getAccessLevel().equals("M")) { item.put("sgcode",
	 * userInfo.getSgCode()); } if
	 * (!accessResult.get(0).getAccessLevel().equals("B") &&
	 * !accessResult.get(0).getAccessLevel().equals("M") &&
	 * !accessResult.get(0).getAccessLevel().equals("S")) { item.put("user",
	 * userInfo.getUserId()); } authyInfo.add(item); } if (authyInfo != null &&
	 * authyInfo.size() > 0) { searchListInfo.setAuthyInfo(authyInfo); }
	 * 
	 * EtcConfigInfo etcConfigInfo = new EtcConfigInfo(); List<EtcConfigInfo>
	 * listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	 * etcConfigInfo.setGroupKey("LISTEN"); etcConfigInfo.setConfigKey("TYPE");
	 * listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	 * 
	 * etcConfigInfo.setGroupKey("SYSTEM"); etcConfigInfo.setConfigKey("HTTPS");
	 * List<EtcConfigInfo> httpResult =
	 * etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
	 * 
	 * Integer totalCount =
	 * searchListInfoService.selectDownRecIpCount(searchListInfo);
	 * session.setAttribute("totalCount", totalCount); int times = totalCount / 500;
	 * int remainder = totalCount % 500; if (remainder != 0) times += 1;
	 * 
	 * File Folder = new File(savePath);
	 * 
	 * try { Folder.mkdirs(); // 폴더 생성합니다. } catch (Exception e) {
	 * e.getStackTrace(); }
	 * 
	 * byte[] buf = new byte[4096]; ZipOutputStream out = null; try { out = new
	 * ZipOutputStream(new FileOutputStream(savePath + "/recFileTemp.zip")); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * // 500개씩 반복 for (int i = 0; i < times; i++) { if (i == 0) {
	 * searchListInfo.setPosStart(0); searchListInfo.setCount(500); } else if (i ==
	 * (times - 1)) { searchListInfo.setPosStart(i * 500);
	 * searchListInfo.setCount(remainder); } else { searchListInfo.setPosStart(i *
	 * 500); searchListInfo.setCount(500); }
	 * 
	 * if ("URL".equals(listenTypeResult.get(0).getConfigValue())) { fileList =
	 * searchListInfoService.selectDownRecIp(searchListInfo); } else { fileList =
	 * searchListInfoService.selectDownRecIp2(searchListInfo); } // 파일 다운로드 - 암호화된
	 * 파일 풀기위해서 파일 다운로드 해야됨 for (int z = 0; z < fileList.size(); z++) { // 파일 암복호화
	 * 옵션.... String url = ""; if
	 * ("URL".equals(listenTypeResult.get(0).getConfigValue())) { url =
	 * fileList.get(z).getListenUrl(); url = url.replace("listen", "down"); } else {
	 * url = "http://" + fileList.get(z).getvRecIp() + ":28881/down?url=" +
	 * fileList.get(z).getvRecFullpath(); }
	 * 
	 * try { // 파일명에 포함되는 한글 인코딩 : 파일명에 한글이 포함되어있을 경우 HttpURLConnection 연결 안됨 url =
	 * url.replace(url.split("url=")[1],URLEncoder.encode(url.split("url=")[1],
	 * "UTF-8")); url = url.replace(URLEncoder.encode("/","UTP-8"),"/"); url =
	 * url.replace(URLEncoder.encode(":","UTP-8"),":"); url =
	 * url.replace(URLEncoder.encode("\\","UTP-8"),"\\"); } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); }
	 * 
	 * if ("https".equals(httpResult.get(0).getConfigValue())) { url =
	 * url.replace("http://", "https://").replace("HTTP://", "HTTPS://"); }
	 * 
	 * url += "&cmd=down&encYn=" + encYn + "&format=mp3&downform=full";
	 * 
	 * try { URL url2 = new URL(url); HttpsURLConnection cons = null;
	 * HttpURLConnection conn = null;
	 * 
	 * if ("https".equals(httpResult.get(0).getConfigValue())) { ignoreSsl(); cons =
	 * (HttpsURLConnection) url2.openConnection(); cons.setDoOutput(true); } else {
	 * conn = (HttpURLConnection) url2.openConnection(); conn.setDoOutput(true); }
	 * 
	 * OutputStream outstream = null; String path = null; String fullpath = "";
	 * 
	 * if ("URL".equals(listenTypeResult.get(0).getConfigValue())) { fullpath =
	 * fileList.get(z).getListenUrl().split("url=")[1]; } else { fullpath =
	 * fileList.get(z).getvRecFullpath(); }
	 * 
	 * String tempPath = fullpath.substring(0,
	 * fullpath.lastIndexOf("/")).split("RecSee_Data")[1]; String tempfileName =
	 * fullpath.substring(fullpath.lastIndexOf("/") + 1);
	 * 
	 * InputStream in = null;
	 * 
	 * try { if ("https".equals(httpResult.get(0).getConfigValue())) { in =
	 * cons.getInputStream(); } else { in = conn.getInputStream(); } } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * File newFile = null; File newFileDir = null; path = savePath + tempPath;
	 * 
	 * newFile = new File(path, tempfileName); newFileDir = new File(path); if
	 * (!newFile.exists()) { newFile.delete(); } if (!newFileDir.exists()) {
	 * newFileDir.mkdirs();
	 * 
	 * ZipEntry ze = new ZipEntry(tempPath + "/"); out.putNextEntry(ze); } // 리눅스일
	 * 경우
	 * 
	 * if(!OsCheck.isWindow()) { Runtime.getRuntime().exec("chmod 777"+
	 * path+tempfileName); newFile.setExecutable(true, false);
	 * newFile.setReadable(true,false); newFile.setWritable(true, false); }
	 * 
	 * outstream = new FileOutputStream(newFile); int length = 0; try { while
	 * ((length = in.read(buf)) != -1) { outstream.write(buf, 0, length); } } catch
	 * (Exception e) { e.printStackTrace(); } finally { if(outstream!=null) {
	 * outstream.flush(); outstream.close(); }
	 * 
	 * if (conn != null) { conn.disconnect(); } if (cons != null) {
	 * cons.disconnect(); } try { in.close(); } catch (Exception e) {
	 * 
	 * } }
	 * 
	 * FileInputStream in2 = new FileInputStream(path + "/" + tempfileName);
	 * 
	 * ZipEntry ze = new ZipEntry(tempPath + "/" + tempfileName);
	 * out.putNextEntry(ze);
	 * 
	 * length = 0; while ((length = in2.read(buf)) > 0) { out.write(buf, 0, length);
	 * }
	 * 
	 * out.closeEntry(); out.flush(); in2.close();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * long fileSize = new File(savePath + "/recFileTemp.zip").length(); fileCount
	 * += 1; session.setAttribute("fileCount", fileCount); // 다운하고 압축에 추가. 하면서 크기
	 * 재고, 1.8G넘으면 삭제. 에러 1099511627776L if (fileSize >= 1932735283L) { try {
	 * out.close(); } catch (IOException e) { System.out.println("파일크기커서 압축종료::" +
	 * e.getMessage()); } File tempFile = new File(savePath + "/recFileTemp.zip");
	 * File file = new File(savePath + "/recFileOver.zip"); tempFile.renameTo(file);
	 * 
	 * z = fileList.size(); i = times; } } } try { out.close(); } catch (IOException
	 * e) { System.out.println("압축종료에러::" + e.getMessage()); e.printStackTrace(); }
	 * // logService.writeLog(this.request, "FILEDOWN", "All_DO", //
	 * "user_id="+userInfo.getUserId()+", query="+searchListInfo.toLogString());
	 * File tempFile = new File(savePath + "/recFileTemp.zip"); File file = new
	 * File(savePath + "/recFile.zip"); tempFile.renameTo(file);
	 * 
	 * } }.init(request);
	 * 
	 * Thread thread = new Thread(r); jRes.setSuccess(AJaxResVO.SUCCESS_Y);
	 * jRes.addAttribute("filePath", savePath + "/recFile.zip"); thread.start();
	 * 
	 * } else { jRes.setSuccess(AJaxResVO.SUCCESS_N);
	 * jRes.setResult("Not LoginInfo"); } return jRes; }
	 */

	public static void ignoreSsl() {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true; // 모든 서버를 신뢰한다
			}
		};

		try {
			trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements TrustManager, X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}
	}

	// 풀다운로드 및 삭제
	/*
	 * @RequestMapping(value = "/downloadFull.do") public void
	 * downloadFull(HttpServletRequest request, Locale local, Model model,
	 * HttpServletResponse response) {
	 * 
	 * String filePath = request.getParameter("path"); //
	 * System.out.println(filePath); File zipFile = new File(filePath); // File
	 * zipFile = new File ("C:/Users/kk/Desktop/RecSee_Data/recFile.zip");
	 * 
	 * response.setHeader("Content-Type",
	 * "application/zip, application/octet-stream");
	 * response.setHeader("Access-Control-Allow-Origin", "*");
	 * response.setHeader("Accept-Ranges", "bytes");
	 * response.setHeader("Content-Disposition",
	 * "attachment; filename=recFile.zip"); try {
	 * response.setHeader("Content-Length", String.valueOf(zipFile.length())); }
	 * catch (Exception e) { response.setHeader("Content-Length", "0"); }
	 * 
	 * OutputStream os = null; try { os = response.getOutputStream(); } catch
	 * (IOException e3) { // TODO Auto-generated catch block e3.printStackTrace(); }
	 * FileInputStream fis = null; try { fis = new FileInputStream(zipFile); } catch
	 * (FileNotFoundException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } byte[] buffer = new byte[4096]; int count; try {
	 * while (fis != null && (count = fis.read(buffer)) != -1) { os.write(buffer, 0,
	 * count); } } catch (IOException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } // close 부문 try { fis.close(); } catch (Exception e)
	 * { System.out.println(e.toString()); } try { os.close(); } catch (Exception
	 * e2) { System.out.println(e2.toString()); } finally {
	 * deleteFile(filePath.split("recFile.zip")[0]); }
	 * 
	 * }
	 */

	// 파일 삭제 함수
//	public static void deleteFile(String path) {
//		File deleteFolder = new File(path);
//
//		if (deleteFolder.exists()) {
//			File[] deleteFolderList = deleteFolder.listFiles();
//
//			for (int i = 0; i < deleteFolderList.length; i++) {
//				if (deleteFolderList[i].isFile()) {
//					deleteFolderList[i].delete();
//				} else {
//					deleteFile(deleteFolderList[i].getPath());
//				}
//				deleteFolderList[i].delete();
//			}
//			deleteFolder.delete();
//		}
//	}

	// 킵얼라이브용
	@RequestMapping(value = "/keepAlive.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO keepAlive(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		String filePath = "";
		String overFilepath = "";
		if (request.getParameter("path") != null && !"".equals(request.getParameter("path"))) {
			filePath = request.getParameter("path");
			overFilepath = filePath.substring(0, filePath.lastIndexOf("/")) + "/recFileOver.zip";
		}
		HttpSession session = request.getSession();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			// 파일 있는지 체크
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("fileCount", session.getAttribute("fileCount"));
			jRes.addAttribute("totalCount", session.getAttribute("totalCount"));
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 전체 다운로드 파일의 총 통화시간
	@RequestMapping(value = "/fulldownTime.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO fulldownTime(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			SearchListInfo searchListInfo = new SearchListInfo();
			searchListInfo.setParamMap(request, "N", "Y");
			if (!StringUtil.isNull(request.getParameter("sgCode"), true))
				searchListInfo.setSgCodeArray(
						new ArrayList<String>(Arrays.asList(request.getParameter("sgCode").split(","))));

			if (!StringUtil.isNull(request.getParameter("buffer12"), true))
				searchListInfo.setBuffer12Array(
						new ArrayList<String>(Arrays.asList(request.getParameter("buffer12").split(","))));

			if (!StringUtil.isNull(request.getParameter("buffer13"), true))
				searchListInfo.setBuffer13Array(
						new ArrayList<String>(Arrays.asList(request.getParameter("buffer13").split(","))));

			MMenuAccessInfo accessInfo = new MMenuAccessInfo();
			accessInfo.setLevelCode(userInfo.getUserLevel());
			accessInfo.setProgramCode("P10002");
			List<MMenuAccessInfo> accessResult = menuAccessInfoService.checkAccessInfo(accessInfo);
			List<HashMap<String, String>> authyInfo = new ArrayList<HashMap<String, String>>();
			if (!accessResult.get(0).getAccessLevel().equals("A")) {
				HashMap<String, String> item = new HashMap<String, String>();

				item.put("bgcode", userInfo.getBgCode());
				if (!accessResult.get(0).getAccessLevel().equals("B")) {
					item.put("mgcode", userInfo.getMgCode());
				}
				if (!accessResult.get(0).getAccessLevel().equals("B")
						&& !accessResult.get(0).getAccessLevel().equals("M")) {
					item.put("sgcode", userInfo.getSgCode());
				}
				if (!accessResult.get(0).getAccessLevel().equals("B")
						&& !accessResult.get(0).getAccessLevel().equals("M")
						&& !accessResult.get(0).getAccessLevel().equals("S")) {
					item.put("user", userInfo.getUserId());
				}

				authyInfo.add(item);
			}
			if (authyInfo != null && authyInfo.size() > 0) {
				searchListInfo.setAuthyInfo(authyInfo);
			}
			// 전체시간 구해오는 쿼리 만들고 시간합 구하기
			Integer totalTime = searchListInfoService.selectTotalTime(searchListInfo);
			String totalRecTime = null;
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("totalTime", totalTime);

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	@RequestMapping(value = "/recMemoProc.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO recMemoProc(HttpServletRequest request, Locale local, Model model) {

		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {

			RecMemo recMemo = new RecMemo();

			String proc = request.getParameter("proc");

			if (!StringUtil.isNull(request.getParameter("recDate"), true)) {
				recMemo.setRecDate(request.getParameter("recDate"));
			}

			if (!StringUtil.isNull(request.getParameter("recTime"), true))
				recMemo.setRecTime(request.getParameter("recTime"));

			if (!StringUtil.isNull(request.getParameter("extNum"), true))
				recMemo.setExtNum(request.getParameter("extNum"));

			if (!StringUtil.isNull(request.getParameter("memo"), true))
				recMemo.setMemo(request.getParameter("memo"));

			if (!StringUtil.isNull(request.getParameter("startTime"), true))
				recMemo.setStartTime(request.getParameter("startTime"));

			if (!StringUtil.isNull(request.getParameter("endTime"), true))
				recMemo.setEndTime(request.getParameter("endTime"));

			if (!StringUtil.isNull(request.getParameter("memoIdx"), true))
				recMemo.setMemoIdx(request.getParameter("memoIdx"));

			if (!StringUtil.isNull(request.getParameter("memoType"), true))
				recMemo.setMemoType(request.getParameter("memoType"));
			else
				recMemo.setMemoType("S");

			recMemo.setServerIp(new RecSeeUtil().getLocalServerIp());
			recMemo.setContextPath(request.getContextPath());

			// 실감 메모.... startTime endTime은 고민좀 해보자..ㅎㅎ;;
			if (!StringUtil.isNull(request.getParameter("type")) && request.getParameter("type").equals("real")) {
				if (!StringUtil.isNull(request.getParameter("type")) && proc.equals("modify"))
					recMemo.setType("real");
				String nowDate = DateUtil.toString(new Date(), "yyyyMMdd");
				String nowDateTime = DateUtil.toString(new Date());
				String recStime = request.getParameter("recsTime");

				recMemo.setRecDate(nowDate);

				Long memoStart = Long.valueOf(request.getParameter("recTime"));
				Long memoEnd = Long.valueOf(DateUtil.toString(new Date()).substring(8, 14));
				//

				recMemo.setMemo("[" + request.getParameter("memoInTime") + "]" + request.getParameter("memo"));
			}

			recMemo.setUserId(userInfo.getUserId());

			if (proc.equals("insert")) {

				Integer TagCheck = recMemoService.selectTagCheck(recMemo);

				if (TagCheck == 0) {
					Integer result = recMemoService.insertRecMemo(recMemo);
					if (result > 0) {
						String memoIdx = recMemo.getMemoIdx();
						jRes.addAttribute("memoIdx", memoIdx);
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("Insert Sucess");

						logInfoService.writeLog(request, "RecMemo - RecMemo Insert Success", recMemo.toString(),
								userInfo.getUserId());
					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("Insert Fail");

						logInfoService.writeLog(request, "RecMemo - RecMemo Insert Fail", recMemo.toString(),
								userInfo.getUserId());
					}
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("Duplicate Tag");

					logInfoService.writeLog(request, "RecMemo - RecMemo Duplicate Tag", recMemo.toString(),
							userInfo.getUserId());
				}
			} else if (proc.equals("modify")) {

				Integer result = recMemoService.updateRecMemo(recMemo);
				if (result > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("Update Sucess");

					logInfoService.writeLog(request, "RecMemo - RecMemo Update Success", recMemo.toString(),
							userInfo.getUserId());
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("Update Fail");

					logInfoService.writeLog(request, "RecMemo - RecMemo Update Fail", recMemo.toString(),
							userInfo.getUserId());
				}

			} else if (proc.equals("delete")) {
				recMemo.setUserId(userInfo.getUserId());
				Integer result = recMemoService.deleteRecMemo(recMemo);
				if (result > 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult("Delete Sucess");

					logInfoService.writeLog(request, "RecMemo - RecMemo Delete Success", null, userInfo.getUserId());
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("Delete Fail");

					logInfoService.writeLog(request, "RecMemo - RecMemo Delete Fail", null, userInfo.getUserId());
				}
			} else if (proc.equals("select")) {
				recMemo.setUserId(userInfo.getUserId());
				List<RecMemo> recMemoSelect = recMemoService.selectRecMemo(recMemo);

				if (recMemoSelect.size() == 0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("recMemo", recMemoSelect);
				}
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 녹취 URL 복호화 처리
	@RequestMapping(value = "/listenLog.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO decryptListenUrl(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {

			SearchListInfo searchListInfo = new SearchListInfo();

			String url = request.getParameter("listenUrl");
			String recDate = "";
			String recTime = "";
			String recExt =  "";
			String recCustPhone =  "";
			String recUserName =  "";
			String recvFileName =  "";

			if (!StringUtil.isNull(request.getParameter("recDate")))
				recDate = request.getParameter("recDate");

			if (!StringUtil.isNull(request.getParameter("recTime")))
				recTime = request.getParameter("recTime");

			if (!StringUtil.isNull(request.getParameter("recExt")))
				recExt = request.getParameter("recExt");

			if (!StringUtil.isNull(request.getParameter("recCustPhone")))
				recCustPhone = request.getParameter("recCustPhone");

			if (!StringUtil.isNull(request.getParameter("recUserName")))
				recUserName = request.getParameter("recUserName");

			if (!StringUtil.isNull(request.getParameter("recvFileName")))
				recvFileName = request.getParameter("recvFileName");

			String LogStr = "Listen File Info [ "
					+ (recDate != null ? " "
							+ messageSource.getMessage("management.searchCustomize.rRecDate", null, Locale.getDefault())
							+ "=" + recDate : "")
					+ (recTime != null ? " "
							+ messageSource.getMessage("management.searchCustomize.rRecTime", null, Locale.getDefault())
							+ "=" + recTime : "")
					+ (recExt != null ? " "
							+ messageSource.getMessage("management.channel.title.extnum", null, Locale.getDefault())
							+ "=" + recExt : "")
					+ (recCustPhone != null
							? " " + messageSource.getMessage("views.player.html.text7", null, Locale.getDefault()) + "="
									+ recCustPhone
							: "")
					+ (recUserName != null
							? " " + messageSource.getMessage("management.user.title.name", null, Locale.getDefault())
									+ "=" + recUserName
							: "")
					+ (recvFileName != null ? " "
							+ messageSource.getMessage("views.search.grid.head.R_V_FILENAME", null, Locale.getDefault())
							+ "=" + recvFileName : "")
					+ " ]";

			if (!StringUtil.isNull(url, true)) {
				/*
				 * AesUtil util = new AesUtil(); url = util.decrypt(url);
				 */

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("Listen Log Success");
				/* jRes.addAttribute("url", url); */
				/* if(StringUtil.isNull(request.getParameter("decOnly"),true)) */
				logService.writeLog(request, "LISTEN", "DO", LogStr);

				searchListInfo.setRecDate(recDate.replaceAll("-", ""));
				searchListInfo.setRecTime(recTime.replaceAll(":", ""));
				searchListInfo.setExtNum(recExt);
				searchListInfo.setUserId(userInfo.getUserId());
				searchListInfo.setUserName(userInfo.getUserName());
				searchListInfoService.insertLogListen(searchListInfo);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Listen Log Fail, Fail Reason => ListenUrl Param is empty");
			}

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 녹취 파일 다운로드 로그
	@RequestMapping(value = "/fileDownLog.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO fileDownLog(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);

		if (userInfo != null) {

			// try{
			String url = request.getParameter("listenUrl");
			String recDate = null;
			String recTime = null;
			String recExt = null;
			String recCustPhone = null;
			String recUserName = null;
			String recvFileName = null;
			String recEncYn = null;
			String UserName = userInfo.getUserId();
			String reasonStr = "";
			String fileType = "";

			if (!StringUtil.isNull(request.getParameter("recDate")))
				recDate = request.getParameter("recDate");

			if (!StringUtil.isNull(request.getParameter("recTime")))
				recTime = request.getParameter("recTime");

			if (!StringUtil.isNull(request.getParameter("recExt")))
				recExt = request.getParameter("recExt");

			if (!StringUtil.isNull(request.getParameter("recCustPhone")))
				recCustPhone = request.getParameter("recCustPhone");

			if (!StringUtil.isNull(request.getParameter("recUserName")))
				recUserName = request.getParameter("recUserName");

			if (!StringUtil.isNull(request.getParameter("recvFileName")))
				recvFileName = request.getParameter("recvFileName");

			if (!StringUtil.isNull(request.getParameter("encYn")))
				recEncYn = request.getParameter("encYn");

			if (!StringUtil.isNull(request.getParameter("reasonStr")))
				reasonStr = XssFilterUtil.XssFilter(request.getParameter("reasonStr"));

			if (!StringUtil.isNull(request.getParameter("fileType")))
				fileType = XssFilterUtil.XssFilter(request.getParameter("fileType"));

			String LogStr = "Download File Info [ "
					+ (recDate != null ? " " + (fileType.equals("approve")
							? messageSource.getMessage("views.approveList.grid.reqDate", null, Locale.getDefault())
							: messageSource.getMessage("management.searchCustomize.rRecDate", null,
									Locale.getDefault()))
							+ "=" + recDate : "")
					+ (recTime != null ? " " + (fileType.equals("approve")
							? messageSource.getMessage("views.approveList.grid.reqTime", null, Locale.getDefault())
							: messageSource.getMessage("management.searchCustomize.rRecTime", null,
									Locale.getDefault()))
							+ "=" + recTime : "")
					+ (recEncYn != null
							? " " + messageSource.getMessage("common.label.encYn", null, Locale.getDefault()) + "="
									+ recEncYn
							: "")
					+ (recExt != null ? " "
							+ messageSource.getMessage("management.channel.title.extnum", null, Locale.getDefault())
							+ "=" + recExt : "")
					+ (recCustPhone != null
							? " " + messageSource.getMessage("views.player.html.text7", null, Locale.getDefault()) + "="
									+ recCustPhone
							: "")
					+ (recUserName != null
							? " " + messageSource.getMessage("management.user.title.name", null, Locale.getDefault())
									+ "=" + recUserName
							: "")
					+ (recvFileName != null ? " "
							+ messageSource.getMessage("views.search.grid.head.R_V_FILENAME", null, Locale.getDefault())
							+ "=" + recvFileName : "")
					+ (recUserName != null ? " "
							+ messageSource.getMessage("views.search.grid.head.R_USER_NAME", null, Locale.getDefault())
							+ "=" + recUserName : "")
					+ (!"".equals(reasonStr) ? " "
							+ messageSource.getMessage("views.approveList.grid.reqReason", null, Locale.getDefault())
							+ "=" + reasonStr : "")
					+ " ]";

			if (!StringUtil.isNull(url, true)) {
				// AesUtil util = new AesUtil();
				// url = util.decrypt(url);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult("Decrypt Listen Success");
				jRes.addAttribute("url", url);

				logService.writeLog(request, "FILEDOWN", "DO", LogStr);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Decrypt Listen Url Fail, Fail Reason => ListenUrl Param is empty");
			}
			/*
			 * }catch(Exception e){ jRes.setSuccess(AJaxResVO.SUCCESS_N);
			 * jRes.setResult("Decrypt Listen Url Fail, Fail Reason => " + e.getMessage());
			 * }
			 */
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	// 듀얼 모드 체크
	// 녹취 파일 다운로드 로그
	@RequestMapping(value = "/dualModeListen.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO dualModeListen(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		String playerFlag = Optional.ofNullable(request.getParameter("recseePlayer")).orElse("N");
		if(playerFlag.equals("Y")) {
			userInfo = new LoginVO();
		}
		
		
		String ip = null;
		String port = null;
		String url = null;
		String http = "http";

		// https 사용 유무 체크
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYSTEM");
		etcConfigInfo.setConfigKey("HTTPS");
		List<EtcConfigInfo> HttpSResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		if ("https".equals(HttpSResult.get(0).getConfigValue())) {
			http = "https";
		}

		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("LISTEN");
		etcConfigInfo.setConfigKey("TYPE");

		List<EtcConfigInfo> listenTypeResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);

		etcConfigInfo.setConfigKey("PORT");

		port = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo).get(0).getConfigValue();
		
		String recPathEncYn = "N";
		
		etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("encryption");
		etcConfigInfo.setConfigKey("rec_path");
		List<EtcConfigInfo> recPathEncYnResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if (recPathEncYnResult!= null && recPathEncYnResult.size() > 0) {
			recPathEncYn = recPathEncYnResult.get(0).getConfigValue();
		}

		String recDate = null;
		String recTime = null;
		String recExt = null;
		
		if (userInfo != null) {
			if (!StringUtil.isNull(request.getParameter("recDate")))
				recDate = request.getParameter("recDate").replaceAll("-", "");

			if (!StringUtil.isNull(request.getParameter("recTime")))
				recTime = request.getParameter("recTime").replaceAll(":", "");

			if (!StringUtil.isNull(request.getParameter("recExt")))
				recExt = request.getParameter("recExt");

			SearchListInfo seacrhListInfo = new SearchListInfo();

			seacrhListInfo.setRecDateRaw(recDate.replaceAll("-", ""));
			seacrhListInfo.setRecTimeRaw(recTime.replace(":", ""));
			seacrhListInfo.setExtNum(recExt.replace("-", ""));
			// List<SearchListInfo> searchListResult =
			// searchListInfoService.selectSearchListInfo(seacrhListInfo);

			List<SearchListInfo> ListenUrl = null;

			// 내/외부망 구분 사용여부 확인
			EtcConfigInfo public_ip_yn = new EtcConfigInfo();
			public_ip_yn.setGroupKey("LISTEN");
			public_ip_yn.setConfigKey("PUBLIC_IP");
			List<EtcConfigInfo> public_ip_yn_result = null;
			String yn = "N";
			try {
				public_ip_yn_result = etcConfigInfoService.selectEtcConfigInfo(public_ip_yn);
				yn = public_ip_yn_result.get(0).getConfigValue();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if ("Y".equals(yn)) {
				String oriUrl = "";
				PublicIpInfo publicIpInfo = new PublicIpInfo();
				// publicIpInfo.setrPublicIp(RecSeeUtil.getClientIp(request));
				publicIpInfo.setrPublicIp(request.getServerName());
				try {
					publicIpInfo = publicIpInfoService.selectOnePublicIpInfo(publicIpInfo);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
					ListenUrl = searchListInfoService.selectURL(seacrhListInfo);
					if (publicIpInfo == null) {// 내부vs외부망 체크
						oriUrl = ListenUrl.get(0).getListenUrl();
					} else {
						oriUrl = ListenUrl.get(0).getTextUrl();
					}
					if ("https".equals(http)) {
						url = oriUrl.replace("HTTP", "HTTPS").replace("http", "https").replace("28881", port);
					} else {
						url = oriUrl;
					}
				} else {
					ListenUrl = searchListInfoService.selectFullPath(seacrhListInfo);
					String recFullPath = ListenUrl.get(0).getvRecFullpath();
					recFullPath = ListenUrl.get(0).getvRecFullpath().replaceAll("\\\\", "/");
					String fileName = recFullPath.substring(recFullPath.lastIndexOf("/"));
					recFullPath = recFullPath.replace(fileName, "");
					try {
						// 파일 경로 암/복호화 여부에 따라 AES256으로 암호화(추후 SEED나 ARIA도 구분하기)
						if ("Y".equals(recPathEncYn)) {
							AesUtil aes = new AesUtil();
							recFullPath = aes.encrypt(recFullPath);
						}
					} catch (Exception e) {
						recFullPath = ListenUrl.get(0).getvRecFullpath();	
					}
					
					url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url=" + recFullPath + fileName;
				}
			} else {

				if ("URL".equals(listenTypeResult.get(0).getConfigValue())) {
					ListenUrl = searchListInfoService.selectURL(seacrhListInfo);
					if ("https".equals(http)) {
						url = ListenUrl.get(0).getListenUrl().replace("HTTP", "HTTPS").replace("http", "https")
								.replace("28881", port);
					} else {
						url = ListenUrl.get(0).getListenUrl();
					}
				} else {
					ListenUrl = searchListInfoService.selectFullPath(seacrhListInfo);
					String recFullPath = ListenUrl.get(0).getvRecFullpath().replaceAll("\\\\", "/");
					String fileName = recFullPath.substring(recFullPath.lastIndexOf("/"));
					recFullPath = recFullPath.replace(fileName, "");
					try {
						// 파일 경로 암/복호화 여부에 따라 AES256으로 암호화(추후 SEED나 ARIA도 구분하기)
						if ("Y".equals(recPathEncYn)) {
							AesUtil aes = new AesUtil();
							recFullPath = aes.encrypt(recFullPath);
						}
					} catch (Exception e) {
						recFullPath = ListenUrl.get(0).getvRecFullpath();	
					}
					
					url = http + "://" + ListenUrl.get(0).getvRecIp() + ":" + port + "/listen?url=" + recFullPath + fileName;
				}
			}

			// String listenUrl =
			// "http://"+searchListResult.get(0).getvRecIp()+":28881/listen?url="+searchListResult.get(0).getvRecFullpath();
			boolean delYn = false;
			String startType = ListenUrl.get(0).getRecStartType();

			// rbuffer 1에 공백값이 아니면 녹취파일 삭제 처리 - 3개월
			if (!StringUtil.isNull(ListenUrl.get(0).getBuffer1()) && !"".equals(ListenUrl.get(0).getBuffer1())) {
				delYn = true;
			}

			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("listenUrl", url);
			jRes.addAttribute("delFileYn", delYn);
			jRes.addAttribute("IVR", startType);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not LoginInfo");
		}
		return jRes;
	}

	private boolean nowAccessChk2(HttpServletRequest request, String string) {
		boolean readYn = false;

		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, string);

		if (nowAccessInfo.getListenYn() != null && nowAccessInfo.getListenYn().equals("Y")) {
			readYn = true;
		}
		return readYn;
	}

	private boolean nowAccessChk3(HttpServletRequest request, String string) {
		boolean readYn = false;

		List<MMenuAccessInfo> sessAccessList = (List<MMenuAccessInfo>) request.getSession().getAttribute("AccessInfo");
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo().getNowAccessRow(sessAccessList, string);

		if ((nowAccessInfo.getApproveYn() != null && nowAccessInfo.getApproveYn().equals("Y"))
				|| nowAccessInfo.getReciptYn() != null && nowAccessInfo.getReciptYn().equals("Y")) {
			readYn = true;
		}
		return readYn;
	}

	// 녹취 파일 다운로드 로그
	@RequestMapping(value = "/fileNameSetting.do", produces = "text/plain;charset=UTF-8")
	public @ResponseBody AJaxResVO fileNameSetting(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();

		// 파일다운로드 요청시 파일이름 변경 기능을 사용할지 여부
		EtcConfigInfo FileNameSettingYn = new EtcConfigInfo();
		FileNameSettingYn.setGroupKey("FILE_NAME_SETTING");
		FileNameSettingYn.setConfigKey("USE_YN");
		List<EtcConfigInfo> FileNameSettingYn_result = etcConfigInfoService.selectEtcConfigInfo(FileNameSettingYn);

		if (FileNameSettingYn_result.size() > 0) {
			if (FileNameSettingYn_result.get(0).getConfigValue().equals("N")) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not Use File Name Setting");

				return jRes;
			}

		} else {
			FileNameSettingYn.setDesc("FileName Setting Use YN");
			FileNameSettingYn.setConfigValue("N");
			FileNameSettingYn.setConfigOption("Y/N");
			etcConfigInfoService.insertEtcConfigInfo(FileNameSettingYn);

			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not Use File Name Setting");

			return jRes;
		}

		// 파일이름 변경 기능이 사용일때 입력할 컬럼을 가져온다.
		EtcConfigInfo FileNameSettingColumn = new EtcConfigInfo();
		FileNameSettingColumn.setGroupKey("FILE_NAME_SETTING");
		FileNameSettingColumn.setConfigKey("COLUMN");
		List<EtcConfigInfo> FileNameSettingColumn_result = etcConfigInfoService
				.selectEtcConfigInfo(FileNameSettingColumn);

		if (FileNameSettingColumn_result.size() > 0) {
			if (FileNameSettingYn_result.get(0).getConfigValue().equals("")) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("Not Find FileName Setting Column");

				return jRes;
			} else {
				String[] FileColumnData = FileNameSettingColumn_result.get(0).getConfigValue().split(",");

				HashMap<String, String> FileColumn = new HashMap<>();

				for (int i = 0; i < FileColumnData.length; i++) {
					FileColumn.put(FileColumnData[i].split(":")[0], FileColumnData[i].split(":")[1]);
				}

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("FileColumn", FileColumn);
			}

		} else {
			FileNameSettingColumn.setDesc("FileName Setting Column");
			FileNameSettingColumn.setConfigValue("날짜:r_rec_date");
			etcConfigInfoService.insertEtcConfigInfo(FileNameSettingColumn);

			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("Not Find FileName Setting Column");

			return jRes;
		}

		return jRes;
	}
	

	@RequestMapping(value = "/updateMergeRecfile.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO updateMergeRecfile(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		SearchListInfo searchListInfo = new SearchListInfo();
		
		if (!StringUtil.isNull(request.getParameter("callKey"), true)) {
			searchListInfo.setCallKeyAp(request.getParameter("callKey"));
		}

		Integer result = searchListInfoService.updateRsRecfile2Info(searchListInfo);
		
		jRes.setSuccess(AJaxResVO.SUCCESS_Y);

		return jRes;
	}
	
	@RequestMapping(value = "/insertMergeRecfile.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO insertMergeRecfile(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		SearchListInfo searchListInfo = new SearchListInfo();
		
		if (!StringUtil.isNull(request.getParameter("callKey"), true)) {
			searchListInfo.setCallKeyAp(request.getParameter("callKey"));
		}
		if (!StringUtil.isNull(request.getParameter("r_v_rec_fullpath"), true)) {
			searchListInfo.setvRecFullpath(request.getParameter("r_v_rec_fullpath"));
		}
		if (!StringUtil.isNull(request.getParameter("r_v_filename"), true)) {
			searchListInfo.setvFileName(request.getParameter("r_v_filename"));
		}
		if (!StringUtil.isNull(request.getParameter("r_v_rec_ip"), true)) {
			searchListInfo.setvRecIp(request.getParameter("r_v_rec_ip"));
		}
		if (!StringUtil.isNull(request.getParameter("scriptCallKey"), true)) {
			searchListInfo.setrRecKey(request.getParameter("scriptCallKey"));
		}
		if (!StringUtil.isNull(request.getParameter("productName"), true)) {
			searchListInfo.setrPdtNm(request.getParameter("productName"));
		}
		if (!StringUtil.isNull(request.getParameter("productRiskNM"), true)) {
			searchListInfo.setRiskNm(request.getParameter("productRiskNM"));
		}
		if (!StringUtil.isNull(request.getParameter("server"), true)) {
			searchListInfo.setServer(request.getParameter("server"));
		}
		if (!StringUtil.isNull(request.getParameter("freeRec"), true)) {
			if(request.getParameter("freeRec").equals("Y")) {
				searchListInfo.setFreeRecFlag(false);
			}else {
				searchListInfo.setFreeRecFlag(true);
			}
		}else {
			searchListInfo.setFreeRecFlag(true);
		}
		
		
		SearchListInfo resultTemp = searchListInfoService.selectttime(searchListInfo);
		
		
		searchListInfo.setCallStime(Optional.ofNullable(resultTemp.getCallStime()).orElse(""));
		searchListInfo.setCallEtime(Optional.ofNullable(resultTemp.getCallEtime()).orElse(""));
		searchListInfo.setCallTtime(Optional.ofNullable(resultTemp.getCallTtime()).orElse(""));
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		String formatDate = sdf.format(date);
		
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("recDate", formatDate);
		hash.put("startTime", formatDate+resultTemp.getCallStime());
		hash.put("endTime", formatDate+resultTemp.getCallEtime());
		hash.put("recTime", resultTemp.getCallTtime());
		
		
		Integer result = searchListInfoService.insertRsRecfileInfo(searchListInfo);
		if (result > 0) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("recDate",hash);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("recDate",hash);
		}

		return jRes;
	}
	@RequestMapping(value = "/searchRecTime.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO searchRecTime(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		SearchListInfo searchListInfo = new SearchListInfo();
		
		if (!StringUtil.isNull(request.getParameter("callKey"), true)) {
			searchListInfo.setCallKeyAp(request.getParameter("callKey"));
		}
		SearchListInfo resultTemp = searchListInfoService.selectttime(searchListInfo);
		searchListInfo.setCallStime(resultTemp.getCallStime());
		searchListInfo.setCallEtime(resultTemp.getCallEtime());
		searchListInfo.setCallTtime(resultTemp.getCallTtime());
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		String formatDate = sdf.format(date);
		
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("recDate", formatDate);
		hash.put("startTime", formatDate+resultTemp.getCallStime());
		hash.put("endTime", formatDate+resultTemp.getCallEtime());
		hash.put("recTime", resultTemp.getCallTtime());
		
		if (hash.size() > 0) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("recDate",hash);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		
		return jRes;
	}
	
	@RequestMapping(value = "/selectRecParamHistory.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectRecParamHistory(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		SearchListInfo searchListInfo = new SearchListInfo();
		
		if (!StringUtil.isNull(request.getParameter("callId1"), true)) {
			searchListInfo.setCallId1(request.getParameter("callId1"));
		}

		String result = searchListInfoService.selectRecParamHistory(searchListInfo);
		if (result != null && !"".equals(result)) {
			String rectry_om = searchListInfoService.selectRectryOneMonth(searchListInfo);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("msg", result);
			jRes.addAttribute("rectry_om", rectry_om);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
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
		
		return path;
		
	}
}
