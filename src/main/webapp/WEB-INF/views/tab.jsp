<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
	<title>RecSee</title>

	<%@ include file="./common/include/commonVar.jsp" %>
	<meta charset="UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="resourcePath" value="${contextPath}/resources"/>
	<c:set var="commResourcePath" value="${resourcePath}/common"/>
	<c:set var="compoResourcePath" value="${resourcePath}/component"/>
	<c:set var="recseeResourcePath" value="${commResourcePath}/recsee"/>
	
	<c:set var="firstPath" value="${sessionScope.AccessInfo.get(0).getProgramPath()}"/>
	<c:set var="firstName" value="${sessionScope.AccessInfo.get(0).getProgramSrc()}"/>
	
	<c:set var="baseActivColor" value="${baseActivColor}"/>
	<c:set var="baseMainColor" value="${baseMainColor}"/>
	<c:set var="baseMainTabbarColor" value="${baseMainTabbarColor}"/>
	<c:set var="baseMainTxtColor" value="${baseMainTxtColor}"/>
	<c:set var="baseSubColor" value="${baseSubColor}"/>
	<c:set var="baseSubTxtColor" value="${baseSubTxtColor}"/>
	<c:set var="siteType" value="${systemTemplates}" />
	<c:set var="tabMode" value="${tabMode}"/>
	<c:set var="recsee_mobile" value="${recsee_mobile}"/>
	
	<c:set var="siteResourcePath" value="${commResourcePath}/${siteType}"/>
	
	<%-- <link rel="shortcut icon" type="image/x-icon" href="${siteResourcePath}/images/project/main/logo/favicon.ico?v=201902" /> --%>
	
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/normalize.css"/>	
	<link rel="stylesheet" type="text/css" href="<c:out value="${recseeResourcePath}"/>/css/common.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/codebase/dhtmlx.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/skins/web/dhtmlx.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${siteResourcePath}"/>/css/images.css"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${compoResourcePath}"/>/HoldOn/HoldOn.css"/>
	
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/layer_popup.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/jquery/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/ui_controller.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/dhtmlxSuite/codebase/dhtmlx.js"></script>
	<script type="text/javascript" src="<c:out value="${compoResourcePath}"/>/HoldOn/HoldOn.min.js"></script>
	
	
	<%-- css page --%>
	<style>
		#timeTag{
			width: 58px;
			margin-top:4px;
		}
		
		.textArea{
			background-color:#ffffff;
			clear:both;
			float:left;
			width:315px; 
			height:175px; 
			resize: none;
			overflow-y:auto;
			border:1px solid #dddddd;
			padding:5px;
		}
		
		.textArea a:hover{
			text-decoration:underline;
			cursor:pointer !important;
			color:rgba(45, 113, 196, 1);
		}
		#recEncPopup, #playerDown{
			width : 450px !important;
		}
		#recFormatPopup{
			width : 450px !important;
		}
		
	</style>
	<script>
	
	var contextPath = '<c:out value="${contextPath}"/>';
	var baseActivColor = '<c:out value="${baseActivColor}"/>';
	var baseMainColor = '<c:out value="${baseMainColor}"/>';
	var baseMainTabbarColor = '<c:out value="${baseMainTabbarColor}"/>';
	var baseMainTxtColor = '<c:out value="${baseMainTxtColor}"/>';
	var baseSubColor = '<c:out value="${baseSubColor}"/>';
	var baseSubTxtColor = '<c:out value="${baseSubTxtColor}"/>';
	var tabMode='<c:out value="${tabMode}"/>';
	var recsee_mobile='<c:out value="${recsee_mobile}"/>';
	var siteResourcePath = '<c:out value="${siteResourcePath}"/>';
	var compoResourcePath = '<c:out value="${compoResourcePath}"/>';

	var firstPath = '<c:out value="${firstPath}"/>';
	var firstName = lang.fn.get('header.menu.label.'+'<c:out value="${firstName}"/>');
	
	var rc = "";
	var dual_rc = "";
	var nowRc = "";
	var contextValue = null;				// 그리드 틀고정 값
	var searchGrid = undefined;
	
	var myTabbar;
	
	$(function() {
		
		  $(window).resize(function() {
			var documentHeight = $(window).height();
			//헤더 높이 92로 하드코딩
			var ResultHeight = (documentHeight - 92);
				
			$("#my_tabbar").css({"height": ResultHeight+ "px"})
			$("#my_tabbar").css({"top": "85px"})
			myTabbar.setSizes();
			
			var playerH=28;
			if(myTabbar.getActiveTab()!=null){
				playerH =  ($(myTabbar.tabs(myTabbar.getActiveTab()).cell).find('.player_pannel').height() != null)?$(myTabbar.tabs(myTabbar.getActiveTab()).cell).find('.player_pannel').height() : 28 ;
				$(myTabbar.tabs(myTabbar.getActiveTab()).cell).find('.mainFrame').not('.player_pannel').css({"height": $(window).height() - 110 - playerH+ "px"})
			}
		    });
	})
	
	function addTabBar(id,name){
		event.stopPropagation();
		event.stopImmediatePropagation();
		
		var idsArray= myTabbar.getAllTabs();
		if(idsArray.indexOf(id)!=-1){
			myTabbar.tabs(id).setActive();
			return false;
		}else{
			var count=myTabbar.getNumberOfTabs();
			if(count>=10){
				alert(lang.tab.alert.exceeds) <%-- 탭개수가 10개를 초과할 수 없습니다. 사용중인 탭을 종료해 주세요. --%>
				return false;
			}else{
				myTabbar.addTab(id, name, null, null, true, true);
				if(id==contextPath+'/search/search'||id==contextPath+'/approveList/approve_List'||id==contextPath+'/browse/browse'||id==contextPath+'/myfolder/myfolder'
				||id==contextPath+'/bestcall/bestcallManage'||id==contextPath+'/bestcall/bestcallShare'||id==contextPath+'/browse/browse'||id==contextPath+'/browse/cybercallmanagement'||id==contextPath+'/browse/browselisten'){
					myTabbar.tabs(id).attachHTMLString("<iframe name='pageFrame' class='mainFrame' id='pageFrame' style='width:100%; border:none;' src='"+id+"'></iframe><iframe style='width:100%; border:none;' name='playerFrame' class='player_pannel mainFrame' id='playerFrame' src='"+contextPath+"/Player'></iframe>");
				}else{
					myTabbar.tabs(id).attachHTMLString("<iframe name='pageFrame' class='mainFrame' id='pageFrame' style='width:100%; border:none;' src='"+id+"'></iframe>");
				}
				$(window).resize();
			}
		}
	}
	
	
	function doOnLoad() {
		myTabbar = new dhtmlXTabBar("my_tabbar");
		myTabbar.setSkin("material")
		myTabbar.setArrowsMode("auto")
	 	myTabbar.attachEvent("OnSelect", function (id, lastid) {
	 		$(".dhx_cell_cont_tabbar").hide()
	 		$(myTabbar.tabs(id).cell).find(".dhx_cell_cont_tabbar").show()
	 		
	 		if($(myTabbar.tabs(id).cell).find('#playerFrame').length!=0){
	 			nowRc=$(myTabbar.tabs(id).cell).find('#playerFrame')[0].contentWindow.rc_player;
	 		}
	 		playerH =  ($(myTabbar.tabs(id).cell).find('.player_pannel').height() != null)?$(myTabbar.tabs(id).cell).find('.player_pannel').height() : 28 ;
	 		$(myTabbar.tabs(id).cell).find('#pageFrame').css({"height": $(window).height() - 110 - playerH+ "px"})
			return true;
		});
		
		myTabbar.attachEvent("onTabClick", function (id, lastid) {
			//마이폴더 탭 클릭하는 경우에는 그리드 갱신이 필요함
			if(id==contextPath+'/myfolder/myfolder'){
				var myfolder = document.getElementsByName('pageFrame')[myTabbar._getIndex(contextPath+"/myfolder/myfolder")].contentWindow;
				myfolder.createGrid(myfolder.myfolderSideBar.getActiveItem())
			}
	 		if(id==myTabbar.getActiveTab()){
	 			if(id==contextPath+'/search/search'||id==contextPath+'/approvelist/approve_List'||id==contextPath+'/myfolder/myfolder'
	 					||id==contextPath+'/bestcall/bestcallManage'||id==contextPath+'/bestcall/bestcallShare'||id==contextPath+'/browse/browse'||id==contextPath+'/browse/browselisten'){
	 						myTabbar.tabs(id).attachHTMLString("<iframe name='pageFrame' class='mainFrame' id='pageFrame' style='width:100%; border:none;' src='"+id+"'></iframe><iframe style='width:100%; border:none;' name='playerFrame' class='player_pannel mainFrame' id='playerFrame' src='"+contextPath+"/Player'></iframe>");
	 					}else{
	 						myTabbar.tabs(id).attachHTMLString("<iframe name='pageFrame' class='mainFrame' id='pageFrame' style='width:100%; border:none;' src='"+id+"'></iframe>");
	 					}
	 		}
	 		$(window).resize();
		}) 
		
		if(firstPath.length > 0 && firstPath != null){
			myTabbar.addTab(contextPath+firstPath, firstName, null, null, true, false);
			myTabbar.tabs(contextPath+firstPath).attachHTMLString("<iframe name='pageFrame' class='mainFrame' id='pageFrame' style='width:100%; border:none;' src='"+contextPath+firstPath+"'></iframe><iframe style='width:100%; border:none;' name='playerFrame' class='player_pannel mainFrame' id='playerFrame' src='"+contextPath+"/Player'></iframe>");
		}		
		
		//myTabbar.addTab(contextPath+'/search/search', '조회 및 청취', null, null, true, false);
		//myTabbar.tabs(contextPath+"/search/search").attachHTMLString("<iframe name='pageFrame' class='mainFrame' id='pageFrame' style='width:100%;' src='"+contextPath+"/search/search'></iframe><iframe style='width:100%;' name='playerFrame' class='player_pannel mainFrame' id='playerFrame' src='"+contextPath+"/Player'></iframe>");
		
		 $(window).resize();
	}
		
	// 하단 플레이어 프레임 보이기, 않보이기 
	// visible가 true면 보이기, false면 숨기기
	// 프레임에서 호출 시 top.playerVisible(true|false)와 같은 형식으로 사용
	function playerVisible(visible){
		var $playerFrame = $("#playerFrame");
		if(visible){
			//$("#pageFrame").show();
			$playerFrame.show()
		}else{
			$playerFrame.hide()
			//$("#pageFrame").hide();
		}
		$(window).resize()
		
		// 플레이어 존재 시 재생 중단.
		if (nowRc != "" && !visible){
			try{
				nowRc.pause();
			}catch(e){}
		}
			 
	}
	
	// 하위 프레임에 키다운 이벤트 걸어주기 
	// 하위 프레임에서 호출 한다.
	// 하위 프레임 : 렉시플레이어, header.js
	function keyDownEvent(obj){
		$(obj).contents().on("keydown", function (e) {
			// F5 / ctrl+R / ctrl+shift+F5 누를 시, 이벤트 무시 후 프레임 새로고침
			if (e.which === 116 || (e.which === 82 && e.ctrlKey) || (e.which === 82 && e.ctrlKey && e.shiftKey)){
				e.preventDefault()
				e.stopPropagation()
				top.reload(); 
			}
		});			
	}
	
	// 각 프레임 새로고침
	function reload(){
		window.location.href=window.location.href
	}			
	
	// 하단 플레이어 듀얼 일 경우 셋팅
	function dualSetting(isDual){
		if(isDual)
			$("#playerFrame").css("height","181px");
		else
			$("#playerFrame").css("height","");
		$(window).resize()
	}

	function selectNowPlayRow(sec) {
		console.log(sec);
		var gridObj = $("#pageFrame")[0].contentWindow.listenPopupGrid;
		var stime = 0;
		gridObj.forEachRow(function(id){
				var ttime = Number(stime)+Number(gridObj.cells(id,gridObj.getColIndexById("r_call_ttime")).getValue());
				if (stime <= sec && sec < ttime) {
					gridObj.selectRow(Number(id)-1);
				}
				stime = ttime;
		});
	}
	// 메모 팝업 처리 (조회 후 그리드 에 나오는 메모 기능)
	function tagPopup(tagInfo, gridObj,writeYn){
		
		var tag = tagInfo.tag||'';
		var regex = /\[[0-9]{2}:[0-9]{2}:[0-9]{2}]/gi
		var startYn = tag.trim().search(regex);
		
		if (startYn > -1){
			var textArray = tag.split(regex);
			var timeArray = tag.match(regex);
			var newText = new Array();
			newText.push("<p>"+(textArray[0] ? textArray[0].replace(/\n/gi,"<br>") : "")+"</p>");
			
			for(var i = 0 ; i<timeArray.length ;i++){
				
				textArray[i+1] = (textArray[i+1] ? textArray[i+1].replace(/\n/gi,"<br>") : "")
				
				newText.push("<p>");
				newText.push("<a class='timeMove' href='javascript:void( 0 );'>"+timeArray[i]+"</a>");
				newText.push(" "+textArray[i+1]||"");
				newText.push("</p>");
			}
			
			$("#tagContents").html(newText.join(' '));
			
			$("#recTag .timeMove").off("click");
			$("#recTag .timeMove").on("click",function(){
				var time = $(this).text().replace(/\[|\]/gi,'');
				time = (function(time){
					var t = time.split(":");
					var hour =t[0]
					var minute =t[1]
					var second =t[2]
					
					return Number(hour*60*60) + Number(minute*60) + Number(second)
				})(time); 
				
				var lastColumn = $("#pageFrame")[0].contentWindow.gridSearch.getColumnsNum();
				// 청취 url : index 0 부터 시작이므로, 컬럼 갯수 -1번째 (row의 가장 마지막 컬럼값)
				var listenUrl = $("#pageFrame")[0].contentWindow.gridSearch.cells($("#pageFrame")[0].contentWindow.gridSearch.getSelectedRowId(),lastColumn-1).getValue()
				
				if (nowRc.listenUrl != listenUrl){
					if(confirm(lang.admin.confirm.chkPlayingMemo)){	/* 현재 재생중인 파일이 없거나, 재생중인 파일과 다릅니다.\n선택하신 메모의 파일을 재생 하시겠습니까? */
						$("#pageFrame")[0].contentWindow.play($("#pageFrame")[0].contentWindow.gridSearch,$("#pageFrame")[0].contentWindow.gridSearch.getSelectedRowId())
						nowRc.settingTime = time;
					}
				}else{
					nowRc.pause();
					nowRc.setTime(time);
					nowRc.settingTime = 0;
					nowRc.play();
				}
			});
			
		}else{
			$("#tagContents").html((tag?tag.replace(/\n/gi,"<br>"):""));
		}
		
		// 버튼 컨트롤
		if (tagInfo.save == "insert")
			$("#tagDel").hide()
		else
			$("#tagDel").show()
		
		//	상담사 일경우 작성권한 없음
		if(writeYn !="Y"){
			recTag.getElementsByClassName('ui_article')[1].style.display = "none";
		}
		
			if(tagInfo.userId.trim() != $("#pageFrame")[0].contentWindow.userId.trim()){
				recTag.getElementsByClassName('ui_article')[1].style.display = "none";
		}
		if(tagInfo.userId.trim() == $("#pageFrame")[0].contentWindow.userId.trim()){
			recTag.getElementsByClassName('ui_article')[1].style.display = "block";
		}
		ui_controller();
		layer_popup("#recTag");
		
		$("#tagAdd").off("click");
		$("#tagAdd").on("click",function(){
			
			var savedMemo = $("<p>"+$("#tagContents").html().replace(/<br\s?\/?>/gi,"\n")+"</p>").text();
							
			var dataStr = {
					"memoIdx"  : tagInfo.memoIdx
				,	"recDate" 	: tagInfo.recDate.replace(/-/gi,'')
				,	"recTime" 	: tagInfo.recTime.replace(/:/gi,'')
				,	"extNum" 	: tagInfo.extNum
				,	"userId"		: tagInfo.userId
				,	"memo" 		: savedMemo
				,	"tag"		: savedMemo
				,	"memoType"  : "T"
				,	"proc" 		: tagInfo.save
			}

			$.ajax({
				url: contextPath+"/recMemoProc.do",
				data: dataStr,
				type: "POST",
				dataType: "json",
				cache: false,
				async: false,
				success: function(jRes) {
					
					dataStr["save"] = "modify";
					dataStr["rowId"] = tagInfo.rowId;
					
					alert(lang.common.alert.savedMemo);	/* 메모 저장이 완료 되었습니다. */
					var cellValue ="<button class='ui_btn_white ui_main_btn_flat btn_icon_tag_white' style='height:25px; vertical-align:middle;' onclick='tag("+JSON.stringify(dataStr)+")'></button>"
					if(tagInfo.save == "modify")
						gridObj.cells(tagInfo.rowId,gridObj.getColIndexById("recMemo")).setValue(cellValue);
					else if(tagInfo.save == "insert"){
						var userInfo = $("#pageFrame")[0].contentWindow.userInfoJson;
						var jsonObj = {
								"memoIdx" : jRes.resData.memoIdx
							,	"recDate" : tagInfo.recDate
							,	"recTime" : tagInfo.recTime
							,	"extNum" : tagInfo.extNum
							,	"tag" : savedMemo
							,	"save" : "modify"
							,	"userId" : userInfo.userId
							,	"rowId" : gridObj.getRowsNum()
						}
						var bgName = userInfo.bgCodeName != null ? userInfo.bgCodeName : "";
							var mgName = userInfo.mgCodeName != null ? userInfo.mgCodeName : "";
						var sgName = userInfo.sgCodeName != null ? userInfo.sgCodeName : "";
						
						gridObj.addRow(gridObj.getRowsNum(),""+tagInfo.recDate+","+tagInfo.recTime+","+tagInfo.extNum+","+
								userInfo.userId+","+
								userInfo.userName+","+
								bgName+","+
								mgName+","+
								sgName+","+
								'<button class="ui_btn_white ui_main_btn_flat btn_icon_tag_white" style="vertical-align : middle; height:25px;" onClick="></button>');
						var rowNum = gridObj.getRowsNum()-1;
						setTimeout(function(){
							$("#pageFrame")[0].contentWindow.recMemoList.cells(rowNum, $("#pageFrame")[0].contentWindow.recMemoList.getColIndexById("recMemo")).setValue("<button class='ui_btn_white ui_main_btn_flat btn_icon_tag_white' style='height:25px; vertical-align:middle;' onclick='tag("+JSON.stringify(jsonObj)+")'></button>");
							$("#pageFrame")[0].contentWindow.ui_controller();
						},100)
					}
					$("#pageFrame")[0].contentWindow.ui_controller();
					$("#timeTag").val('')
					if(gridObj.getRowsNum()>0){
						var ori=$("#pageFrame")[0].contentWindow.gridSearch.cells($("#pageFrame")[0].contentWindow.gridSearch.getSelectedRowId(),$("#pageFrame")[0].contentWindow.gridSearch.getSelectedCellIndex()).getValue();
						$("#pageFrame")[0].contentWindow.gridSearch.cells($("#pageFrame")[0].contentWindow.gridSearch.getSelectedRowId(),$("#pageFrame")[0].contentWindow.gridSearch.getSelectedCellIndex()).setValue(ori.replace('ui_sub_btn_flat','ui_main_btn_flat'));
					}
					layer_popup_close();
					
				}
			});
		});
		
		$("#tagDel").off("click");
		$("#tagDel").on("click",function(){
			
			if(confirm(lang.admin.confirm.deletingMemo)){ /* 메모를 삭제 하시겠습니까? */
				var dataStr = {
						"memoIdx"  : tagInfo.memoIdx
					,	"recDate" 	: tagInfo.recDate.replace(/-/gi,'')
					,	"recTime" 	: tagInfo.recTime.replace(/:/gi,'')
					,	"extNum" 	: tagInfo.extNum
					,	"userId"		: tagInfo.userId
					,	"memoType"  : "T"
					,	"proc"		: "delete"
				}
				
				$.ajax({
					url: contextPath+"/recMemoProc.do",
					data: dataStr,
					type: "POST",
					dataType: "json",
					cache: false,
					async: false,
					success: function(jRes) {
						
						dataStr["save"] = "insert";
						dataStr["rowId"] = tagInfo.rowId;
						
						alert(lang.common.alert.deletedMemo);	/* 메모 삭제가 완료 되었습니다. */
						gridObj.deleteRow(tagInfo.rowId);
						//var cellValue ="<button class='ui_btn_white ui_sub_btn_flat icon_btn_pen_white' onclick='tag("+JSON.stringify(dataStr)+")'></button>"
						//gridObj.cells(tagInfo.rowId,gridObj.getColIndexById("r_memo")).setValue(cellValue);
						//$("#pageFrame")[0].contentWindow.ui_controller();
						//$("#timeTag").val('')
						if(gridObj.getRowsNum()==0){
							var ori=$("#pageFrame")[0].contentWindow.gridSearch.cells($("#pageFrame")[0].contentWindow.gridSearch.getSelectedRowId(),$("#pageFrame")[0].contentWindow.gridSearch.getSelectedCellIndex()).getValue();
							$("#pageFrame")[0].contentWindow.gridSearch.cells($("#pageFrame")[0].contentWindow.gridSearch.getSelectedRowId(),$("#pageFrame")[0].contentWindow.gridSearch.getSelectedCellIndex()).setValue(ori.replace('ui_main_btn_flat','ui_sub_btn_flat'));
							setTimeout(function(){
								$("#pageFrame")[0].contentWindow.ui_controller();
							},100)
						}
						layer_popup_close();
					}
				});
			}
		});
	}
	
	// 메모 팝업 처리
	function memoPopup(memoInfo, sectionObj){
		var memoIdx = memoInfo.memoIdx;
		var memo = memoInfo.memo;
		var recDate = memoInfo.recDate;
		var recTime = memoInfo.recTime;
		var extNum = memoInfo.extNum;
		var userName = memoInfo.userName;
		var userId = memoInfo.userId;
		
		$("#memoContents").val(memo);
		
		if(pageFrame.userId != userId){
			$("#memoAdd").hide();
		}else{
			$("#memoAdd").show();
		}
		
		$("#memoAdd").off("click");
		$("#memoAdd").on("click",function(){
			
			var savedMemo = $("#memoContents").val()
							
			var dataStr = {
					"memoIdx" :memoIdx
				,	"memo" : savedMemo
				,	"recDate" 	: recDate
				,	"recTime" 	: recTime
				,	"extNum" 	: extNum
				,	"proc" : "modify"
			}

			$.ajax({
				url: contextPath+"/recMemoProc.do",
				data: dataStr,
				type: "POST",
				dataType: "json",
				cache: false,
				async: false,
				success: function(jRes) {
					//sectionObj.attr("title",savedMemo||'')
					sectionObj.attr("title","["+pageFrame.userInfoJson.userId+"]"+"["+pageFrame.userInfoJson.userName+"]");
					sectionObj.data("memo",savedMemo);
					alert(lang.common.alert.savedPlayerTag);	/* 플레이어 태그 저장이 완료 되었습니다. */
					layer_popup_close();
					var rowId = nowRc.recFileData.rowId;
					var oriMemoInfo = "";
					if(searchGrid){
						var memoInfoCellIdx = searchGrid.getColIndexById("r_memo_info");
						if(memoInfoCellIdx){
							try{
								oriMemoInfo = searchGrid.cells(rowId,memoInfoCellIdx).getValue();
								
								var memoArray = oriMemoInfo.split('|')
								var memoInfoArray = new Array();
								
								for(var i = 0 ; i < memoArray.length ;i++){
									var findMemoInfo = memoArray[i].split(',');
									if (memoIdx == findMemoInfo[0]){
										findMemoInfo[3] = savedMemo;
									}
									memoInfoArray.push(findMemoInfo.join(","))
								}
								
								updateMemoInfo(rowId,memoInfoArray.join("|"));
								
							}catch (e){
								
							}
						}
									
					}
					
				}
			});
		});
		
		ui_controller();
		layer_popup("#recMemo");
	}
	
	// 그리드에 메모정보 반영
	function updateMemoInfo(rowId,memoStr){
		if(searchGrid){
							
			var memoInfoCellIdx = searchGrid.getColIndexById("r_memo_info");
			if(memoInfoCellIdx){
				try{
					searchGrid.cells(rowId,memoInfoCellIdx).setValue(memoStr)						
				}catch(e){
					alert(lang.common.alert.failUpdateTag)	/* 태그 정보가 정상적으로 갱신되지 못하였습니다.\n재 조회를 해주시면 태그 정보가 정상적으로 갱신 됩니다. */
				}
			}else{
				alert(lang.common.alert.failUpdateTag)	/* 태그 정보가 정상적으로 갱신되지 못하였습니다.\n재 조회를 해주시면 태그 정보가 정상적으로 갱신 됩니다. */
			}
		}else{
			alert(lang.common.alert.failUpdateTag)	/* 태그 정보가 정상적으로 갱신되지 못하였습니다.\n재 조회를 해주시면 태그 정보가 정상적으로 갱신 됩니다. */
		}
	}
	
	// 파일 다운로드 로그
	function fileDownLog(dataStr){
		$.ajax({
			url: contextPath+"/fileDownLog.do",
			data: dataStr,
			type: "POST",
			dataType: "json",
			async:true,
			cache: false,
			success: function(jRes) {}
		});
	}
	
	// 플레이어 토글링 
    function playerToggle(){
		$("#playerFrame")[0].contentWindow.playerToggle();
	}
	
	function rightMenuResize(){
		$("#pageFrame")[0].contentWindow.resizeRightMenu();
	}
	
	// url 파싱 해주는 함수
	// 익스 URL도 없냐..하....있는게 뭐야..?
	function parseUri(str){
		var o = parseUri.options;
		var m = o.parser[o.stircMode ? "strict" : "loose"].exec(str)
	 	var uri = {}
	 	var i = 14;
	 	
		while(i--){
			uri[o.key[i]] = m[i] || "";
		}
	 	
	 	uri[o.q.name] = {};
		uri[o.key[12]].replace(o.q.parser, function ($0, $1, $2){
			if ($1){
	 			uri[o.q.name][$1] = $2;
	 		}
	 	});
	 	
	 	return uri;
	}
	
	parseUri.options = {
			stricMode:false
		,	key:["source","protocol","authority","userInfo","user","password","host","port","relative","path","directory","file","query","anchor"]
		,	q:{
					name:"queryKey"
				,	parser:/(?:^|&)([^&=]*)=?([^&]*)/g
			}
		,	parser:{
					strict:/^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/
				,	loose: /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/
			}
	}
	   
	</script>

</head>
<body onload="doOnLoad();">
	
	<%@ include file="./common/header.jsp" %>	
	
	<div id="my_tabbar" style="width:100%;">
	</div>
	<div id="recMemo" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">Tag</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<textArea class="textArea" id="memoContents"></textArea>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="memoAdd" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="message.btn.save"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="recTag" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit">Memo</p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<div class="textArea" contentEditable="true" id="tagContents"></div>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_left">
						<input id="timeTag" value="" type="text" maxlength=8/>
					</div>
					<div class="ui_float_left">
						<button id="timeTagAdd" class="ui_main_btn_flat icon_btn_time_white">Time Tag</button>
					</div>
					<div class="ui_float_right">
						<button id="tagAdd" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="message.btn.save"/></button>
						<button id="tagDel" class="ui_btn_white icon_btn_trash_gray"></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="recEncPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="common.label.encYn"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<label class="ui_label_essential enc_ui_hide"><spring:message code="views.search.grid.head.R_USER_NAME"/></label>
	                        <input readOnly class="ui_input_hasinfo enc_ui_hide" id="encAgentName" value="" type="text"/>
                        <label class="ui_label_essential enc_ui_hide"><spring:message code="views.search.grid.head.R_EXT_NUM"/></label>
	                        <input readOnly class="ui_input_hasinfo enc_ui_hide" id="encExt" value="" type="text"/>
                       	<label class="ui_label_essential enc_ui_hide"><spring:message code="common.label.fileName"/></label>
	                        <input readOnly class="ui_input_hasinfo enc_ui_hide" id="encFileName" value="" type="text"/>
                       	<label class="ui_label_essential"><spring:message code="common.label.encYn"/></label>
            		        <select class="" id="approveUserGroup">
            		        	<option value="encY">Y</option>
            		        	<option value="encN">N</option>
            		        </select>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="downloadEncYn" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="common.label.download"/></button>
						<button id="closeEncYn" class="ui_main_btn_flat"><spring:message code="message.btn.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="recFormatPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="common.label.format"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
                       	<label class="ui_label_essential"><spring:message code="common.label.format"/></label>
            		        <select class="" id="selectFormat">
            		        	<option value="mp3">MP3</option>
            		        	<option value="g723">WAV(G.723)</option>
            		        	<option value="gsm">WAV(GSM)</option>
            		        </select>
					</div>
					<input id='downloadType' style='display:none;' value='mp3'>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="confirmFormat" class="ui_main_btn_flat"><spring:message code="message.btn.ok"/></button>
						<button id="closeFormatYn" class="ui_main_btn_flat"><spring:message code="message.btn.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="requestReasonPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="common.label.reason"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
                       	<label class="ui_label_essential"><spring:message code="common.label.reason"/></label>
            		        <input class="" maxlength ="30" id="downloadReason"/>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="confirmReason" class="ui_main_btn_flat"><spring:message code="message.btn.ok"/></button>
						<button id="closeReason" class="ui_main_btn_flat"><spring:message code="message.btn.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>		
	
	<div id="playerDown" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
				<div class="ui_pannel_popup_header">
					<div class="ui_float_left">
						<p class="ui_pannel_tit"><spring:message code="common.label.playerDownload"/></p>
					</div>
					<div class="ui_float_right">
						<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
					</div>
				</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				<div class="ui_pannel_row">
					<div class="ui_padding">
						<label class="ui_label_essential playerPeriod"><spring:message code="common.label.playerPeriod"/></label>
	                        <input class="inputFilter numberFilter playerPeriod" maxlength="2" id="playerPeriod" value="" type="text"/>
                        <label class="ui_label_essential playerPassword"><spring:message code="common.label.playerPassword"/></label>
	                        <input class="playerPassword" id="playerPassword" maxlength="128" value="" type="password"/>
                        <label class="ui_label_essential playerPassword"><spring:message code="common.label.playerRePassword"/></label>
	                        <input class="playerPassword" id="playerRePassword" maxlength="128" value="" type="password"/>
					</div>
				</div>
			</div>
			<div class="ui_article">
				<div class="ui_pannel_row">
					<div class="ui_float_right">
						<button id="playerDownBtn" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="common.label.download"/></button>	
						<button id="playerDownClose" class="ui_main_btn_flat"><spring:message code="message.btn.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>


</script>
</html>