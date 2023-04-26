package com.furence.recsee.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.service.ChannelInfoService;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;

@Controller
@RequestMapping("/admin")
public class JsonSystemSetupController {
	@Autowired
	private ChannelInfoService channelInfoService; 


	private static String OS = System.getProperty("os.name").toLowerCase();

	// 통계 
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/channelMonitoring_data.json", method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody void dashboard_data(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		
		if(userInfo != null) {
			ChannelInfo channelInfo = new ChannelInfo();
			
			JSONObject jsonData = new JSONObject();

			List<ChannelInfo> channelDailyResult = channelInfoService.selectChannelMonitoringInfo(channelInfo);
			List<ChannelInfo> channelExtDailyResult = channelInfoService.selectChannelMonitoringExtInfo(channelInfo);
			Integer channelDailyTotal = channelDailyResult.size();
			Integer channelExtDailyTotal = channelExtDailyResult.size();
			
			JSONArray jsonDaily = new JSONArray();

			Integer totalCalls = channelDailyTotal;
			Integer totalCallYns = 0;
			
			Integer totalExts = channelExtDailyTotal;
			Integer totalExtYns = 0;
			Integer totalRecNone = 0;
			if(channelDailyTotal > 0) {
				for(int i=0; i<channelDailyTotal; i++) {
					ChannelInfo dailyItem = channelDailyResult.get(i);
					if(dailyItem.getRecSize().equals("0")) {
						totalCallYns ++;
					}
				}
				for(int i = 0; i < channelExtDailyTotal; i++) {
					ChannelInfo dailyExtItem = channelExtDailyResult.get(i);
					if(dailyExtItem.getRecYn().equals("N")) {
						totalExtYns ++;
					}
					if(dailyExtItem.getRecSum().equals("0")) {
						totalRecNone++;
					}
				}
			}

			JSONObject jsonDailyItem = new JSONObject();

			jsonData.put("totalChannel", totalCalls);
			jsonData.put("totalSizeNone", totalCallYns);
			
			jsonData.put("totalExtChannel", totalExts);
			jsonData.put("totalExtYn", totalExtYns);
			jsonData.put("totalExtSizeNone", totalRecNone);

			jsonDaily.add(jsonDailyItem);
			
			response.setContentType("application/json");
			response.getWriter().write(jsonData.toString());
		}
	}

}
