/*package com.furence.recsee.admin.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.monitoring.model.AlertList;
import com.furence.recsee.monitoring.model.ControlTargetVO;
import com.furence.recsee.monitoring.model.ItemVo;
import com.furence.recsee.monitoring.model.UiInfoVO;
import com.furence.recsee.monitoring.service.MonitoringInfoService;

@Controller
public class AjaxSystemMonitoringController {

	@Autowired
	private MonitoringInfoService monitoringInfoService;
	
	//관제 대상 추가, 삭제
	@RequestMapping(value = "/Monitoring_target_proc.do")
	public @ResponseBody AJaxResVO MonitoringTargetProc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ControlTargetVO setTargetInfo = new ControlTargetVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				if(request.getParameter("method")!=null && request.getParameter("method").equals("del")) {
					Integer deleteTargetResult=0;
					Integer deleteTargetUiResult=0;
					Integer deleteTargetUseItemResult=0;
					String[] delList = request.getParameter("data").split(",");

					for(int i = 0; i < delList.length; i++) {
						if(delList[i]!=null) {
							setTargetInfo.setRm_target_id(delList[i].toString());
							deleteTargetResult+=monitoringInfoService.deleteTarget(setTargetInfo);
							monitoringInfoService.updateTargetOrderbyDelete(setTargetInfo);
							deleteTargetUiResult = monitoringInfoService.deleteTargetUi(setTargetInfo);
							deleteTargetUseItemResult = monitoringInfoService.deleteUseItem(setTargetInfo);
						}
					}
					if(deleteTargetResult == delList.length) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult(String.valueOf(delList.length));

					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "delete fail");
					}
					
				}else if(request.getParameter("method")!=null && request.getParameter("method").equals("upd")) {
					Integer updTargetResult=0;
					
					if(request.getParameter("targetid")!=null&&!request.getParameter("targetid").equals("")) {
						setTargetInfo.setRm_target_id(request.getParameter("targetid"));
					}
					if(request.getParameter("targetName")!=null&&!request.getParameter("targetName").equals("")) {
						setTargetInfo.setRm_target_name(request.getParameter("targetName"));
					}
					if(request.getParameter("targetType")!=null&&!request.getParameter("targetType").equals("")) {
						setTargetInfo.setRm_target_type(request.getParameter("targetType"));
					}
					if(request.getParameter("targetIP")!=null&&!request.getParameter("targetIP").equals("")) {
						setTargetInfo.setRm_target_ip(request.getParameter("targetIP"));
					}
					if(request.getParameter("targetDescr")!=null&&!request.getParameter("targetDescr").equals("")) {
						setTargetInfo.setRm_target_descr(request.getParameter("targetDescr"));
					}
					if(request.getParameter("watchYn")!=null&&!request.getParameter("watchYn").equals("")) {
						setTargetInfo.setRm_target_use(request.getParameter("watchYn"));
					}
					updTargetResult+=monitoringInfoService.updateTarget(setTargetInfo);
					
					if(updTargetResult == 1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");

					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "add fail");
					}
					
				}else if(request.getParameter("method")!=null && request.getParameter("method").equals("add")){
					Integer addTargetResult=0;
					Integer addTargetOrderResult=0;
					
					if(request.getParameter("targetName")!=null&&!request.getParameter("targetName").equals("")) {
						setTargetInfo.setRm_target_name(request.getParameter("targetName"));
					}
					if(request.getParameter("targetType")!=null&&!request.getParameter("targetType").equals("")) {
						setTargetInfo.setRm_target_type(request.getParameter("targetType"));
					}
					if(request.getParameter("targetIP")!=null&&!request.getParameter("targetIP").equals("")) {
						setTargetInfo.setRm_target_ip(request.getParameter("targetIP"));
					}
					if(request.getParameter("watchYn")!=null&&!request.getParameter("watchYn").equals("")) {
						setTargetInfo.setRm_target_use(request.getParameter("watchYn"));
					}
					setTargetInfo.setRm_target_descr(request.getParameter("targetDescr"));
					
					addTargetResult=monitoringInfoService.insertTarget(setTargetInfo);
					addTargetOrderResult=monitoringInfoService.insertTargetOrder(setTargetInfo);
					
					if(addTargetResult == 1&&addTargetOrderResult==1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");

					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("0");
						jRes.addAttribute("msg", "add fail");
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "Monitoring_target_proc fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");

		}

		return jRes;
	}
	//관제 항목 추가
	@RequestMapping(value = "/addItemCombo.do")
	public @ResponseBody AJaxResVO addItemCombo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ControlTargetVO setTargetInfo = new ControlTargetVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				if(request.getParameter("hasChildren")!=null&&request.getParameter("targetType")!=null&&request.getParameter("targetId")!=null) {
					
					setTargetInfo.setRm_target_type(request.getParameter("targetType"));
					
					Integer countItemListResult=monitoringInfoService.countItemList(setTargetInfo);
					
					if(countItemListResult > Integer.parseInt(request.getParameter("hasChildren"))) {
						
						
						setTargetInfo.setRm_target_id(request.getParameter("targetId"));
						
						List<ItemVo> targetItemComboResult=monitoringInfoService.targetItemCombo(setTargetInfo);
						String html="<select class='selectItemCombo' style='width:90% !important; padding:0;'>";
						
						for(int i=0; i<targetItemComboResult.size();i++) {
							html+="<option id='"+targetItemComboResult.get(i).getRm_item_index()+"' value='"+targetItemComboResult.get(i).getRm_item_limit()+"'>"+targetItemComboResult.get(i).getRm_item_name()+"</option>";
						}
						html+="</select>";
						
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.addAttribute("jResDate", html);
						jRes.addAttribute("limit", targetItemComboResult.get(0).getRm_item_limit());
						jRes.addAttribute("id", targetItemComboResult.get(0).getRm_item_index());
						jRes.addAttribute("countItem", countItemListResult);

					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "full list");
					}
					
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}	
	//관제 항목관리 삭제
	@RequestMapping(value = "/delItemCombo.do")
	public @ResponseBody AJaxResVO delItemCombo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ControlTargetVO setTargetInfo = new ControlTargetVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				if(request.getParameter("targetId")!=null) {
					
					setTargetInfo.setRm_target_id(request.getParameter("targetId"));
					
					Integer countItemListResult=monitoringInfoService.deleteItemList(setTargetInfo);
					
					if(countItemListResult==1) {
						
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");

					} else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.setResult("1");
						jRes.addAttribute("msg", "delete fail");
					}
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}
	//관제 항목 관리 저장
	@RequestMapping(value = "/saveItem.do")
	public @ResponseBody AJaxResVO saveItem(HttpServletRequest request, Locale local, Model model) throws ParseException{
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo!=null) {
			JSONArray JsonList = null;
			JSONParser jsonPasor = new JSONParser();
			JsonList=(JSONArray) jsonPasor.parse(request.getParameter("jsonData"));
			int totalsize=JsonList.size();
			int itemUseResult=0;
			
			for(int i=0;i<JsonList.size();i++) {
				ItemVo itemVO = new ItemVo();
			
				if((String) ((JSONObject)JsonList.get(i)).get("targetId")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("targetId")).equals("")))
					itemVO.setRm_monitoring_target_id((String) ((JSONObject)JsonList.get(i)).get("targetId"));
				
				if((String) ((JSONObject)JsonList.get(i)).get("itemId")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("itemId")).equals("")))
					itemVO.setRm_use_monitoring_item_id((String) ((JSONObject)JsonList.get(i)).get("itemId"));
				
				itemUseResult += monitoringInfoService.addUseItem(itemVO);
			}
			
			if(itemUseResult==totalsize) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult(String.valueOf(itemUseResult));

			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult(String.valueOf(totalsize-itemUseResult));
				jRes.addAttribute("msg", "save "+String.valueOf(totalsize-itemUseResult)+" fail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
	
	//관제 지표 설정 저장
		@RequestMapping(value = "/saveLimit.do")
		public @ResponseBody AJaxResVO saveLimit(HttpServletRequest request, Locale local, Model model) throws ParseException{
			AJaxResVO jRes = new AJaxResVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo!=null) {
				JSONArray JsonList = null;
				JSONParser jsonPasor = new JSONParser();
				JsonList=(JSONArray) jsonPasor.parse(request.getParameter("jsonData"));
				int totalsize=JsonList.size();
				int itemUseResult=0;
				
				for(int i=0;i<JsonList.size();i++) {
					ItemVo itemVO = new ItemVo();
				
					if((String) ((JSONObject)JsonList.get(i)).get("id")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("id")).equals("")))
						itemVO.setRm_item_index((String) ((JSONObject)JsonList.get(i)).get("id"));
					
					if((String) ((JSONObject)JsonList.get(i)).get("limit")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("limit")).equals("")))
						itemVO.setRm_item_limit((String) ((JSONObject)JsonList.get(i)).get("limit"));
					
					if((String) ((JSONObject)JsonList.get(i)).get("logYn")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("logYn")).equals("")))
						itemVO.setRm_item_log_yn((String) ((JSONObject)JsonList.get(i)).get("logYn"));
					
					itemUseResult += monitoringInfoService.updateItem(itemVO);
				}
				
				if(itemUseResult==totalsize) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult(String.valueOf(itemUseResult));

				} else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult(String.valueOf(totalsize-itemUseResult));
					jRes.addAttribute("msg", "save "+String.valueOf(totalsize-itemUseResult)+" fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
			return jRes;
		}
		
	//관제 지표 추가
	@RequestMapping(value = "/Attr_add_proc.do")
	public @ResponseBody AJaxResVO Attr_add_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ItemVo setItemInfo = new ItemVo();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				
				if(request.getParameter("itemName")!=null&&!request.getParameter("itemName").equals(""))
					setItemInfo.setRm_item_name(request.getParameter("itemName"));
				if(request.getParameter("itemType")!=null&&!request.getParameter("itemType").equals(""))
					if(request.getParameter("itemType").equals("서버"))
						setItemInfo.setRm_item_type("server");
					else
						setItemInfo.setRm_item_type("work");
				if(request.getParameter("limitType")!=null&&!request.getParameter("limitType").equals(""))
					setItemInfo.setRm_item_unit(request.getParameter("limitType"));
				if(request.getParameter("limit")!=null&&!request.getParameter("limit").equals(""))
					setItemInfo.setRm_item_limit(request.getParameter("limit"));
					
				int itemAddResult = monitoringInfoService.addItemList(setItemInfo);
				
				if(itemAddResult>0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult(String.valueOf(itemAddResult));
				}else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult(String.valueOf(itemAddResult));
					jRes.addAttribute("msg", "add fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}	
		
	//관제 지표 수정
	@RequestMapping(value = "/Attr_update_proc.do")
	public @ResponseBody AJaxResVO Attr_update_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ItemVo setItemInfo = new ItemVo();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				
				if(request.getParameter("itemIndex")!=null&&!request.getParameter("itemIndex").equals(""))
					setItemInfo.setRm_item_index(request.getParameter("itemIndex"));
				if(request.getParameter("itemName")!=null&&!request.getParameter("itemName").equals(""))
					setItemInfo.setRm_item_name(request.getParameter("itemName"));
				if(request.getParameter("itemType")!=null&&!request.getParameter("itemType").equals(""))
					if(request.getParameter("itemType").equals("서버"))
						setItemInfo.setRm_item_type("server");
					else
						setItemInfo.setRm_item_type("work");
				if(request.getParameter("limitType")!=null&&!request.getParameter("limitType").equals(""))
					setItemInfo.setRm_item_unit(request.getParameter("limitType"));
				if(request.getParameter("logYn")!=null&&!request.getParameter("logYn").equals(""))
					setItemInfo.setRm_item_log_yn(request.getParameter("logYn"));
				if(request.getParameter("limit")!=null&&!request.getParameter("limit").equals(""))
					setItemInfo.setRm_item_limit(request.getParameter("limit"));
					
				int itemUpdateResult = monitoringInfoService.updateItemList(setItemInfo);
				
				if(itemUpdateResult>0) {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.setResult(String.valueOf(itemUpdateResult));
				}else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult(String.valueOf(itemUpdateResult));
					jRes.addAttribute("msg", "add fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}	
				

	//관제 지표 삭제
	@RequestMapping(value = "/Attr_delete_proc.do")
	public @ResponseBody AJaxResVO Attr_delete_proc(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ItemVo setItemInfo = new ItemVo();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				int itemDeleteResult=0;
				String[] rowDate=null;
				if(request.getParameter("data")!=null&&!request.getParameter("data").equals("")) {
					rowDate=request.getParameter("data").split(",");
					
					for(int i=0;i<rowDate.length;i++) {
						setItemInfo.setRm_item_index(rowDate[i]);
						itemDeleteResult = monitoringInfoService.deleteItemAttrList(setItemInfo);
						monitoringInfoService.deleteItemAttrList2(setItemInfo);
					}
					
					if(itemDeleteResult==rowDate.length) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult(String.valueOf(itemDeleteResult));
					}
				}else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult(String.valueOf(rowDate.length-itemDeleteResult));
					jRes.addAttribute("msg", "delete fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}	
	//관제 맵 선택
	@RequestMapping(value = "/ui_select_map.do")
	public @ResponseBody AJaxResVO UISelectMap(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
						List<UiInfoVO> uiInfoResult=monitoringInfoService.selectMaplist();
					
					if(uiInfoResult.size()>=0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult(String.valueOf(uiInfoResult.size()));
						jRes.addAttribute("result", uiInfoResult);
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "select fail");
					}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}	
	
	//관제 아이콘 선택
	@RequestMapping(value = "/ui_select_icon.do")
	public @ResponseBody AJaxResVO UISelectIcon(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
						List<UiInfoVO> uiInfoResult=monitoringInfoService.selectIconList();
					
					if(uiInfoResult.size()>=0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult(String.valueOf(uiInfoResult.size()));
						jRes.addAttribute("result", uiInfoResult);
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "select fail");
					}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}

	//관제 아이콘 콤보박스
	@RequestMapping(value = "/ui_select_icon_combo.do")
	public @ResponseBody AJaxResVO UISelectIconCombo(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
						List<UiInfoVO> uiInfoResult=monitoringInfoService.selectIconListCombo();
					
					if(uiInfoResult.size()>=0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult(String.valueOf(uiInfoResult.size()));
						jRes.addAttribute("result", uiInfoResult);
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "select fail");
					}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}	
	
	//관제 ui 수정
	@RequestMapping(value = "/target_ui_save.do")
	public @ResponseBody AJaxResVO targetUiSave(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			ControlTargetVO controlTargetVO= new ControlTargetVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
					
				if(request.getParameter("index")!=null&&!("".equals(request.getParameter("index")))){
					controlTargetVO.setRm_target_id(request.getParameter("index"));
				}
				if(request.getParameter("uiWatchTime")!=null&&!("".equals(request.getParameter("uiWatchTime")))){
					controlTargetVO.setRm_target_ui_watch_time(request.getParameter("uiWatchTime"));
				}
				if(request.getParameter("uiObject")!=null&&!("".equals(request.getParameter("uiObject")))){
					controlTargetVO.setRm_target_ui_object_icon(request.getParameter("uiObject"));
				}
					int updateTargetUiResult=monitoringInfoService.updateTargetUi(controlTargetVO);
					
					if(updateTargetUiResult==1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "update fail");
					}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
		}
	
	//관제 UI 설정 초기화
	@RequestMapping(value = "/UI_del.do")
	public @ResponseBody AJaxResVO UIDel(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		ControlTargetVO controlTargetVO = new ControlTargetVO();
		try {
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
				int UIDeleteResult=0;
				String[] rowDate=null;
				if(request.getParameter("data")!=null&&!request.getParameter("data").equals("")) {
					rowDate=request.getParameter("data").split(",");
					
					for(int i=0;i<rowDate.length;i++) {
						controlTargetVO.setRm_target_id(rowDate[i]);
						UIDeleteResult = monitoringInfoService.updateUisetting(controlTargetVO);
					}
					
					if(UIDeleteResult==rowDate.length) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult(String.valueOf(UIDeleteResult));
					}
				}else {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult(String.valueOf(rowDate.length-UIDeleteResult));
					jRes.addAttribute("msg", "delete fail");
				}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
	}
	
	//관제 ui map 이름 수정
	@RequestMapping(value = "/update_map_name.do")
	public @ResponseBody AJaxResVO updateMapName(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			UiInfoVO uiInfoVO = new UiInfoVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
					
				if(request.getParameter("index")!=null&&!("".equals(request.getParameter("index")))){
					uiInfoVO.setRm_map_name(request.getParameter("index"));
				}
				if(request.getParameter("mapName")!=null&&!("".equals(request.getParameter("mapName")))){
					uiInfoVO.setRm_map_descr(request.getParameter("mapName"));
				}
					int updateTargetUiResult=monitoringInfoService.updateMapName(uiInfoVO);
					
					if(updateTargetUiResult==1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "update fail");
					}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
		}
	
	//관제 ui map 사용 결정
	@RequestMapping(value = "/choose_map.do")
	public @ResponseBody AJaxResVO chooseMap(HttpServletRequest request, Locale local, Model model) {
		AJaxResVO jRes = new AJaxResVO();
		try {
			UiInfoVO uiInfoVO = new UiInfoVO();
			LoginVO userInfo = SessionManager.getUserInfo(request);
			if(userInfo != null) {
					
				if(request.getParameter("index")!=null&&!("".equals(request.getParameter("index")))){
					uiInfoVO.setRm_map_name(request.getParameter("index"));
				}
				
					monitoringInfoService.updateUseMapDefault();
					int updateTargetUiResult=monitoringInfoService.updateUseMap(uiInfoVO);
					
					if(updateTargetUiResult==1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.setResult("1");
					}else {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "update fail");
					}
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "login fail");
			}
		} catch(Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.setResult("0");
		}
		return jRes;
		}
	
	//관측 순번 수정
	@RequestMapping(value = "/update_order.do")
	public @ResponseBody AJaxResVO updateOrder(HttpServletRequest request, Locale local, Model model) throws ParseException{
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo!=null) {
			JSONArray JsonList = null;
			JSONParser jsonPasor = new JSONParser();
			JsonList=(JSONArray) jsonPasor.parse(request.getParameter("data"));
			int totalsize=JsonList.size();
			int itemUseResult=0;
			
			for(int i=0;i<JsonList.size();i++) {
				ControlTargetVO controlTargetVO = new ControlTargetVO();
			
				if((String) ((JSONObject)JsonList.get(i)).get("index")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("index")).equals("")))
					controlTargetVO.setRm_target_id((String) ((JSONObject)JsonList.get(i)).get("index"));
				
				if((String) ((JSONObject)JsonList.get(i)).get("order")!=null && !(((String) ((JSONObject)JsonList.get(i)).get("order")).equals("")))
					controlTargetVO.setRm_target_ui_order((String) ((JSONObject)JsonList.get(i)).get("order"));
				
				itemUseResult += monitoringInfoService.updateOrder(controlTargetVO);
			}
			
			if(itemUseResult==totalsize) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult(String.valueOf(itemUseResult));

			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult(String.valueOf(totalsize-itemUseResult));
				jRes.addAttribute("msg", "update "+String.valueOf(totalsize-itemUseResult)+" fail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
	
	//관측 순번 수정
	@RequestMapping(value = "/Alert_delete.do")
	public @ResponseBody AJaxResVO Alert_delete(HttpServletRequest request, Locale local, Model model) throws ParseException{
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if(userInfo!=null) {
			Integer deleteTargetResult=0;
			String[] delList = request.getParameter("chList").split(",");

			AlertList alertList= new AlertList();
			for(int i = 0; i < delList.length; i++) {
				if(delList[i]!=null) {
					alertList.setR_alert_index(delList[i].toString());
					deleteTargetResult+=monitoringInfoService.deleteAlert(alertList);
				}
			}
			if(deleteTargetResult == delList.length) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.setResult(String.valueOf(delList.length));
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.setResult("0");
				jRes.addAttribute("msg", "delete fail");
			}
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "login fail");
		}
		return jRes;
	}
}//end class
*/