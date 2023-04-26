// 전역변수 설정
var gridFileRecoverManage; // 그리드


window.onload = function() {
	top.playerVisible(true);
	gridFileRecoverManage = gridFileRecoverManageLoad(); // 패킷 에러 로그 그리드 로드
	formFunction();
	ui_controller();
}
function gridFileRecoverManageLoad() {
	objGrid = new dhtmlXGridObject("gridFileRecoverManage");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 20, 5, "pagingFileRecoverManage", true);
	objGrid.setPagingWTMode(true,true,true,[20,50,100]);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
    objGrid.setSkin("dhx_web");
    // 영역 선택해서 복사
    objGrid.enableBlockSelection();
	objGrid.attachEvent("onKeyPress",onKeyPressed);
		
	objGrid.init();
	
	objGrid.load(contextPath+"/file_recover_list.xml?header=true", function(){
		
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
	
		objGrid.attachEvent("onXLS", function(){
			progress.on()
		});
		objGrid.attachEvent("onXLE", function(grid_obj,count){

			if (objGrid.getRowsNum() > 0){
				var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>'
				objGrid.aToolBar.setItemText("total", setResult)
			}
			objGrid._HideSelection();
			
			ui_controller();
			progress.off();
	
			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
				$(top.window).trigger("resize");
			});
		});
		objGrid.attachEvent("onRowCreated", function(rId,rObj,rXml){
			if(objGrid.cells(rId,objGrid.getColIndexById("fileState")).getValue()== lang.admin.fileRecover.state.MS || objGrid.cells(rId,objGrid.getColIndexById("fileState")).getValue()== lang.admin.fileRecover.state.AS) 
				   $($(objGrid.cells(rId,0).cell)[0]).empty()
				   
		   var nortp = objGrid.cells(rId,objGrid.getColIndexById("nortp")).getValue();
			if (nortp == "Y") {
				objGrid.setRowColor(rId,"#D6F3DA");
			}
			var notRecording = objGrid.cells(rId,objGrid.getColIndexById("notRecording")).getValue();
			var nowSearchType =  $("#nowSearchType").val();
			if (nowSearchType != "N" && notRecording == "N") {
				objGrid.setRowColor(rId,"#DEE6FA");
			}
		});
		addButtons(objGrid);
		
		objGrid.attachEvent("onRowSelect", function(id,ind){
		    return;
		});
	
		// 로우 더블클릭 시 청취
		objGrid.attachEvent("onRowDblClicked", function(id,ind){
			listenRecoverFile(id);
		});
		
		// 페이지 체인지 이벤트
		objGrid.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum())
				return false;
			return true;
		});
		
		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj,count){
			ui_controller();
		});
		
		//체크박스 전체 선택
		objGrid.attachEvent("onHeaderClick",function(ind, obj){
			if(ind == 0 && obj.type == "click") {
				var recFrom = objGrid.getStateOfView()[1]; // 현재 페이지의 시작 지점
				var recTo = objGrid.getStateOfView()[2]; // 현재 페이지의 끝 지점
				if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
					objGrid.forEachRow(function(id){
						var idx = objGrid.getRowIndex(id); // 그리드 돌면서 ind 받아와
						if (recFrom <= idx && idx < recTo) { // 현재 페이지에 있는 녹취이면
							if(objGrid.cells(id,objGrid.getColIndexById("fileState")).getValue()== lang.admin.fileRecover.state.MS || objGrid.cells(id,objGrid.getColIndexById("fileState")).getValue()== lang.admin.fileRecover.state.AS) {
								$($(objGrid.cells(id,0).cell)[0]).empty()
							}else{ // 체크박스 상태 변경
								// this.setCheckedRows(0, 1);
								objGrid.cells(id,0).setValue(1)
								$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
							}
						}
					});
				} else {
					objGrid.forEachRow(function(id){
						var idx = objGrid.getRowIndex(id); // 그리드 돌면서 ind 받아와
						if (recFrom <= idx && idx < recTo) { // 현재 페이지에 있는 녹취이면
							if(objGrid.cells(id,objGrid.getColIndexById("fileState")).getValue()== lang.admin.fileRecover.state.MS || objGrid.cells(id,objGrid.getColIndexById("fileState")).getValue()== lang.admin.fileRecover.state.AS) {
								$($(objGrid.cells(id,0).cell)[0]).empty()
							}else{ // 체크박스 상태 변경
								// this.setCheckedRows(0, 0);
								objGrid.cells(id,0).setValue(0)
								$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
							}
						}
					});
				}
			} else {
				return true;
			}
		});

		objGrid.attachEvent("onAfterSorting", function(ind){
			ui_controller();
		});
		
	}, 'xml');
	
    return objGrid;
}
//복사 붙여넣기
function onKeyPressed(code,ctrl,shift){
	if(code==67&&ctrl){
		if (!objGrid._selectionArea) return alert(lang.views.search.alert.selectGrid);/*복사할 범위를 선택해 주세요.*/
			objGrid.setCSVDelimiter("\t");
			objGrid.copyBlockToClipboard()
		}
		if(code==86&&ctrl){
			objGrid.setCSVDelimiter("\t");
			objGrid.pasteBlockFromClipboard()
		}
	return true;
}

function addButtons(objGrid){

	var fileRecover_toolbar = objGrid.aToolBar;

	fileRecover_toolbar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>')
	fileRecover_toolbar.setWidth("total",200)
	
	fileRecover_toolbar.addSpacer("perpagenum");
	
}
function allFileRecover() {
	setTimeout(function() {
			if (confirm("전체 파일을 복구하시겠습니까?")) {
				progress.on();
				$.ajax({
					url : contextPath + "/requestAllFileRecover.do"+ formToSerialize(),
				 	type : "POST",
				 	data : {"listType" : $("#nowListType").val()},
				 	dataType:"json",
				 	async: true,
				 	success:function(jRes){
				 		if(jRes.success == "Y"){
				 			progress.off();
				 			alert("파일 복구 요청에 성공했습니다.");
				 			gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml" + formToSerialize());
				 		}else{
				 			progress.off();
				 			if (jRes.resData.msg == "working") {
								alert("현재 진행중인 작업이 있습니다. 잠시 후 다시 시도해 주세요. ");
							} else if (jRes.resData.msg == "serverError") {
								alert("복구 서버에 연결할 수 없습니다.");
							} else if (jRes.resData.msg == "requestRecoverFail") {
				 				var succCnt = 0;
				 				if (jRes.resData.succCnt != undefined) {
				 					succCnt = jRes.resData.succCnt;
				 				} 
				 				var failCnt = 0;
				 				if (jRes.resData.failCnt != undefined) {
				 					failCnt = jRes.resData.failCnt;
				 				}
				 				alert("파일 복구 요청에 실패했습니다. \n 총 : " + jRes.resData.total + " 건, 성공 : " + succCnt + " 건, 실패 : " + failCnt + "건");
								gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml"+formToSerialize());
				 			} else { 
				 				alert("파일 복구 요청에 실패했습니다.");
				 			}
				 		}
			 		}
				})		
			}
	}, 250);
}

function multiFileRecover(){
		setTimeout(function() {
			progress.on();
			var gridArr = gridFileRecoverManage.getCheckedRows(0).split(",");
			if(gridArr == ""){
	 			progress.off();
				alert(lang.views.search.alert.msg4/*"선택된 녹취 파일이 없습니다."*/);
				return;
			}

			var fileDateArr = [];
			var fileTimeArr = [];
			var fileNameArr = [];
			var fileRecoverIpArr = [];
			var sysCodeArr = [];
			var extIpArr = [];
			var extNumArr = [];
			var callIdArr = [];

			var listType = $("#nowListType").val();
			
			for (var i = 0; i < gridArr.length; i++) {
				fileDateArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("fileDate")).getValue().replaceAll("-",""))
				fileTimeArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("fileTime")).getValue().replaceAll(":",""))
				fileNameArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("fileName")).getValue())
				fileRecoverIpArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("fileRecoverIp")).getValue())
				sysCodeArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("sysCode")).getValue())
				extIpArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("extIp")).getValue())
				extNumArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("extNum")).getValue());
				callIdArr.push(gridFileRecoverManage.cells(gridArr[i],gridFileRecoverManage.getColIndexById("callId")).getValue());
			}

			$.ajax({
				url : contextPath + "/requestMultiFileRecover.do",
			 	type : "POST",
			 	data : {
			 		fileDateArr : fileDateArr.toString()
		 			,fileTimeArr : fileTimeArr.toString()
			 		,fileNameArr : fileNameArr.toString()
			 		,fileRecoverIpArr : fileRecoverIpArr.toString()
			 		,sysCodeArr : sysCodeArr.toString()
			 		,extIpArr : extIpArr.toString()
			 		,extNumArr : extNumArr.toString()
			 		,callIdArr : callIdArr.toString()
			 		,listType : listType
			 	},
			 	dataType:"json",
			 	async: true,
			 	success:function(jRes){
			 		if(jRes.success == "Y"){
			 			progress.off();
			 			alert("파일 복구 요청에 성공했습니다.");
			 			gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml" + formToSerialize());
			 		}else{
			 			progress.off();
			 			if (jRes.resData.msg == "working") {
							alert("현재 진행중인 작업이 있습니다. 잠시 후 다시 시도해 주세요. ");
						} else if (jRes.resData.msg == "serverError") {
							alert("복구 서버에 연결할 수 없습니다.");
						} else if (jRes.resData.msg == "requestRecoverFail") {
			 				var succCnt = 0;
			 				if (jRes.resData.succCnt != undefined) {
			 					succCnt = jRes.resData.succCnt;
			 				} 
			 				var failCnt = 0;
			 				if (jRes.resData.failCnt != undefined) {
			 					failCnt = jRes.resData.failCnt;
			 				} 
			 				alert("파일 복구 요청에 실패했습니다. \n 총 : " + gridArr.length + " 건, 성공 : " + succCnt + " 건, 실패 : " + failCnt + "건");
							gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml"+formToSerialize());
			 			} else { 
			 				alert("파일 복구 요청에 실패했습니다.");
			 			}
			 		}
		 		}
			})			
	}, 250);
}

function formFunction(){
	$('.main_form').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#fileRecovorSearchBtn").trigger("click");
	})
	 $("#searchListType").change(function() {
		 if ($("#searchListType").val() == "genesys") {
			 $("#searchFileName").hide();
			 $("#listReloadBtn").hide()
		 } else {
			 $("#searchFileName").show();
			 $("#listReloadBtn").show();
		 }
	 })
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})

	$("#sTime, #eTime").change(function(){fromTo($("#sTime"),$("#eTime"),this)})
	$("#searchsCallTtime, #searcheCallTtime").keyup(function(){if($("#searchsCallTtime").val().replace(/[:-]/g,'').length==6&&$("#searcheCallTtime").val().replace(/[:-]/g,'').length==6) fromTo($("#searchsCallTtime"),$("#searcheCallTtime"),this)})
	
	// 날짜 셋팅
	datepickerSetting(locale,'#sDate, #eDate');
	//@Kyle
	//캘린더 자동완성 /
	$('#sDate, #eDate').keyup(function(e) {
		autoCalendar(this, e)
	});

	// 날짜 조회 보조 옵션
	$("#daySelect").change(function(){
		var selectedOpt = $(this).val();
		var now = new Date();
		switch(selectedOpt){
		case "day":
//			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			datepickerSetting(locale,'#sDate, #eDate', now);
		break;
		case "week" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			now.setDate(now.getDate()-7)
//			$('#sDate').datepicker().datepicker("setDate", now);
			datepickerSetting(locale,'#sDate', now);
		break;
		case "month" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			now.setMonth(now.getMonth()-1)
//			$('#sDate').datepicker().datepicker("setDate", now);
			datepickerSetting(locale,'#sDate', now);
		break;
		case "custom" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", false ).datepicker("setDate", now);
			datepickerSetting(locale,'#sDate, #eDate', now);
		break;
		}
	});

	// 콤보 로드
	selectOptionLoad($("#sTime"),"Time");
	selectOptionLoad($("#eTime"),"Time","e");
	selectOptionLoad($("#searchSysCode"), "system",null,null,null,null,null,"all")
	selectOptionLoad($("#sys_code"), "system",null,null,null,null,null,"all")
	selectOptionLoad($("#searchCallKind"), "callType");
	selectOptionLoad($("#call_kind"), "callType");
	
	// 마지막 시간값 가져온다....
	$("#eTime").val($("#eTime").children().last().text()).prop("selected", true);
	
	// 검색 버튼 클릭
	$("#fileRecovorSearchBtn").click(function(){
		var strData = formToSerialize()
		gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml?"+encodeURI(strData));
	});
	
	$("#multiFileRecoverBtn").click(function() {
		multiFileRecover();
	});
	
	$("#allFileRecoverBtn").click(function() {
		allFileRecover();
	});
	
	$("#listReloadBtn").click(function() {
		listReload();
	});
	
	// 3차 백업 정보 팝업 -> 수정 버튼 클릭
	$("#modifyBtn").click(function(){
		var sysCode = $("#sys_code").val();
		var extNum = $("#ext_num").val();
		var extIp = $("#ext_ip").val();
		var callId = $("#call_id").val();
		var callTtime = $("#call_ttime").val();
		if (callTtime == null) {
			callTtime = 0;
		}
		var custPhone = $("#cust_phone").val();
		var custPhoneChange = "N";
		var callKind = $("#call_kind").val();

		var fileDate = $("#file_date").val();
		var fileTime = $("#file_time").val();
		var fileName = $("#file_name").val();
		var nowListType = $("#nowListType").val();
		
		if (nowListType == "rtp" && extIp != null && extIp != '' && !ValidateIPaddress(extIp)) {
			alert("내선 IP는 IP 형식으로 입력해주세요.");
			$("#ext_ip").focus();
			return;
		};
		
		if (custPhone != $("#cust_phone_hidden").val()) {
			custPhoneChange = "Y";
		}
		
		if ("Y" == custPhoneChange && custPhone != null && custPhone != '' && !numberFilter(custPhone)) {
			alert("고객 전화번호는 숫자만 입력해주세요.");
			$("#cust_phone").focus();
			return;
		};
		
		var dataStr = {
				"sysCode" : sysCode
			,	"extNum"	: extNum
		    ,   "extIp"	: extIp
		    ,   "callId"	: callId
		    ,	"callTtime"	: callTtime
		    ,	"custPhone"	: custPhone
		    ,	"custPhoneChange" : custPhoneChange
		    ,	"callKind"	: callKind
		    ,	"fileDate"	: fileDate
		    ,	"fileTime"	: fileTime
		    ,	"fileName"	: fileName
		    ,	"nowListType" : nowListType
		};
		
		$.ajax({
			url:contextPath+"/updateFileRecoverInfo.do",
			type:"POST",
			data: dataStr,
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert("3차 백업 정보가 저장되었습니다.");
					layer_popup_close();
					gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml"+formToSerialize());
				}else{
					alert("3차 백업 정보 저장에 실패하였습니다.");
				}
			}
		});
	})
}

function ValidateIPaddress(inputText) {
    var ipformat = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    if (ipformat.test(inputText)) {
        return true;
    } else {
        return false;
    }
}

function numberFilter(str) {
	var pattern_num = /[0-9]/;	// 숫자 
	var pattern_eng = /[a-zA-Z]/;	// 문자 
	var pattern_spc = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자
	var pattern_kor = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/; // 한글체크

	if( (pattern_num.test(str)) && !(pattern_eng.test(str)) && !(pattern_spc.test(str)) && !(pattern_kor.test(str)) ){
		return true;
	}else{
		return false;
	}
}

function listReload() {
	var listType = $("#searchListType").val();
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	var sTime = $("#sTime").val();
	var eTime = $("#eTime").val();
	
	if(confirm(sDate+" "+sTime + "부터 "+ eDate+" "+eTime + "사이의 RTP 리스트가 삭제 후 새로 갱신됩니다.\n진행 하시겠습니까?")) {
		$.ajax({
			url:contextPath+"/requestListReload.do",
			type:"POST",
			data: { 
				  "sDate" : sDate
				, "eDate" : eDate 
				, "sTime" : sTime 
				, "eTime" : eTime
				, "listType" : listType
			},
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert("복구 리스트 요청이 완료되었습니다.");
					gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml"+formToSerialize());
				}else{
					if (jRes.resData.msg == "working") {
						alert("현재 진행중인 작업이 있습니다. 잠시 후 다시 시도해 주세요. ");
					} else {
						alert("복구 리스트 요청에 실패했습니다.");
					}
				}
			}
		});
	}
}

// 수동 복구 요청
function requestFileRecover(idx) {
	progress.on();
	
	setTimeout(function() { 
		var fileDate = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileDate")).getValue().replaceAll("-","");
		var fileTime = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileTime")).getValue().replaceAll(":","");
		var fileName = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileName")).getValue();
		var fileRecoverIp = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileRecoverIp")).getValue();
		var sysCode = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("sysCode")).getValue();
		var extIp = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("extIp")).getValue();
		var extNum = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("extNum")).getValue();
		var callId = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("callId")).getValue();
		var listType = $("#nowListType").val();
		
		$.ajax({
			url:contextPath+"/requestFileRecover.do",
			type:"POST",
			data: { 
				  "fileDate" : fileDate
				, "fileTime" : fileTime 
				, "fileName" : fileName 
				, "fileRecoverIp" : fileRecoverIp 
				, "sysCode" : sysCode
				, "extIp" : extIp
				, "extNum" : extNum
				, "callId" : callId
				, "listType" : listType
			},
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					progress.off();
					alert(fileName + "파일의 수동 복구 요청이 완료되었습니다.");
					layer_popup_close();
					gridFileRecoverManage.clearAndLoad(contextPath+"/file_recover_list.xml"+formToSerialize());
				}else{
					progress.off();
					if (jRes.resData.msg == "working") {
						alert("현재 진행중인 작업이 있습니다. 잠시 후 다시 시도해 주세요. ");
					} else if (jRes.resData.msg == "serverError") {
						alert("복구 서버에 연결할 수 없습니다.");
					} else if (jRes.resData.msg == "notRegistered") {
						alert("등록되지 않은 내선입니다.");
					} else {
						alert(fileName + "파일의 수동 복구 요청에 실패하였습니다.");
					}
				}
			}
		});
	},250);
}

function modifyFileInfo(id) {
	if (modiYn == "N") {
		return false;
	}
	var sysCode = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("sysCode")).getValue().replaceAll("-","");
	var extNum = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("extNum")).getValue().replaceAll("-","");
	var extIp = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("extIp")).getValue().replaceAll("-","");
	var callId = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("callId")).getValue().replaceAll("-","");
	var callTtime = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("callTtime")).getValue();
	var custPhone = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("custPhone")).getValue().replaceAll("-","");
	var cust_phone_hidden = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("custPhone")).getValue().replaceAll("-","");
	var callKind = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("callKind")).getValue().replaceAll("-","");
	var fileDate = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("fileDate")).getValue().replaceAll("-","");
	var fileTime = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("fileTime")).getValue().replaceAll(":","");
	var fileName = gridFileRecoverManage.cells(id,gridFileRecoverManage.getColIndexById("fileName")).getValue();
	
	$("#sys_code").val(sysCode);
	$("#ext_num").val(extNum);
	$("#ext_ip").val(extIp);
	$("#call_id").val(callId);
	if (callTtime != "") {
		$("#call_ttime").val(time2seconds(callTtime));
	}
	$("#cust_phone").val(custPhone);
	$("#call_kind").val(callKind);

	$("#file_date").val(fileDate);
	$("#file_time").val(fileTime);
	$("#file_name").val(fileName);
	$("#cust_phone_hidden").val(cust_phone_hidden);

	if ($("#nowListType").val() == "genesys") {
		$(".hideInfo").hide();
		$("#sys_code").attr("disabled","true");
		$("#ext_num").attr("disabled","true");
		$("#call_id").attr("disabled","true");
	} else {
		$(".hideInfo").show();
		$("#sys_code").removeAttr("disabled");
		$("#ext_num").removeAttr("disabled");
		$("#call_id").removeAttr("disabled");
	}
	layer_popup('#fileRecoverInfo');
}

//청취 요청
function listenRecoverFile(idx) {
	progress.on();
	
	setTimeout(function() {
		var fileDate = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileDate")).getValue().replaceAll("-","");;
		var fileTime = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileTime")).getValue().replaceAll(":","");;
		var fileName = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileName")).getValue();
		var fileRecoverIp = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("fileRecoverIp")).getValue();
		var sysCode = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("sysCode")).getValue();
		var extNum = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("extNum")).getValue();
		var extIp = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("extIp")).getValue();
		var custPhone = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("custPhone")).getValue();
		var callId = gridFileRecoverManage.cells(idx,gridFileRecoverManage.getColIndexById("callId")).getValue();
		var listType = $("#nowListType").val();
		
		$.ajax({
			url:contextPath+"/listenRecoverFile.do",
			type:"POST",
			data: {
				  "fileDate" : fileDate
				, "fileTime" : fileTime 
				, "fileName" : fileName 
				, "fileRecoverIp" : fileRecoverIp 
				, "sysCode" : sysCode
				, "extIp" : extIp
				, "extNum" : extNum
				, "callId" : callId
				, "listType" : listType
			},
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					progress.off();
					// rsfft listen 주소 받아옴
					listenUrl=jRes.resData.listenUrl;
					waveType=jRes.resData.WaveType;
					
					if (listenUrl.indexOf("HTTPS") == -1 && listenUrl.indexOf("https") == -1 ) {
						listenUrl = listenUrl.replace("HTTP", "HTTPS");
						listenUrl = listenUrl.replace("http", "https");
					}
                    $.ajax({
                        url:listenUrl.split("/listen")[0]+"/ping",
                        data:{},
                        type:"GET",
                        dataType:"json",
                        timeout:2000,
                        async: false,
                        success:function(jRes){
                        	recFileData = {
    								"listenUrl"		: listenUrl
    							,	"recDate"		: fileDate	  			// 녹취일
    							,	"recTime"		: fileTime				// 녹취시간
    							,	"recExt"		: extNum				// 내선버노
    							,	"recCustPhone" : custPhone
    						}
                            top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.player.html.text2/*"현재 재생 중인 파일 : "*/);
                            
                            top.rc.setFile("audio", encodeURI(listenUrl), undefined, true, waveType/*"rsfft"*/,1);
                            top.rc.listenUrl =encodeURI(listenUrl)
    						top.rc.recFileData = recFileData;
                        },
                        error:function(request, satus, error){
                        	top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.search.alert.msg26/*"요청에 실패 하였습니다."*/);
                        	top.$('#playerFrame').contents().find(".play_list").find(".play_file_tit").html("");
                        	top.$('#playerFrame').contents().find(".btn_init").click();
                        	return false;
                        }
                    });
				} else {
					progress.off();
					top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.search.alert.msg26/*"현재 재생 중인 파일 : "*/);
                	top.$('#playerFrame').contents().find(".play_list").find(".play_file_tit").html("");
                    top.$('#playerFrame').contents().find(".btn_init").click();
                    
                    if (jRes.resData.msg == "working") {
						alert("현재 진행중인 작업이 있습니다. 잠시 후 다시 시도해 주세요. ");
					} else if (jRes.resData.msg == "notRegistered") {
						alert("등록되지 않은 내선입니다.");
					}
                    return false;
				}
			}
		});
	},250);
}

function checkSoundDevice(objGrid, id){
	try {
		var taudio = new Audio();
		var src = contextPath+"/getRecFileTest.do?fileName=beef.mp3"
		taudio.autoplay = true;
		taudio.volume = 0;
		taudio.src = src;
    	taudio.pause();
		taudio.load();
		taudio.play();

		taudio.addEventListener('error',function(event) {
			taudio.pause();
			alert(lang.views.search.alert.msg32) /* 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. */
        });

		taudio.addEventListener('loadeddata',function(){
			try {
				taudio.currentTime=0.1
				taudio.pause();
				play(objGrid, id);
				return true;
			} catch(e){
				taudio.pause();
				alert(lang.views.search.alert.msg32) /* 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. */
			}
		});
	}catch(e){
		taudio.pause();
		alert(lang.views.search.alert.msg32) /* 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. */
	}
}

//재생 이벤트
function play(gridObj, id){
	rc = top.nowRc;
	var recFileData;
	var listenUrl = "";
	var recDate 		= (gridObj.getColIndexById("r_rec_date")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_rec_date")).getValue());
	var recTime			= (gridObj.getColIndexById("r_rec_time")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_rec_time")).getValue());
	var recExt			= (gridObj.getColIndexById("r_ext_num")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_ext_num")).getValue());
	var recUserName		= (gridObj.getColIndexById("r_user_name")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_user_name")).getValue());
	var recCustName		= (gridObj.getColIndexById("r_cust_name")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_cust_name")).getValue());
	$.ajax({
		url:contextPath+"/getListenUrl.do",
		data:{
				"recDate" : recDate
			,	"recTime" : recTime
			,	"recExt" : recExt
			,	"recUserName" : recUserName
			,	"recCustName" : recCustName
		},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success=="Y"){
				listenUrl=jRes.resData.ListenUrl;
				recFileData = {
						"listenUrl"		: listenUrl
					,	"recDate"		: recDate	  			// 녹취일
					,	"recTime"		: recTime				// 녹취시간
					,	"recExt"		: recExt				// 내선버노
					,	"recUserName" 	: recUserName			// 상담사명
					,	"recCustName" 	: recCustName			// 고객명
				}
				listenLog(recFileData);
				$(top.myTabbar.tabs(top.myTabbar.getActiveTab()).cell).find('#playerFrame')[0].contentWindow.$(".play_list").find(".play_info").find("span").html(lang.views.player.html.text2/*"현재 재생 중인 파일 : "*/);

			}else {
				alert(lang.views.search.alert.msg26); /* 요청에 실패 하였습니다. */
			}
		}
	});
	rc.recFileData = recFileData;
	rc.setFile("audio", encodeURI(listenUrl), undefined, true);
	rc.listenUrl =encodeURI(listenUrl)
}

function listenLog(recFileData,justPath){
	var url = recFileData.listenUrl

	$.ajax({
		url:contextPath+"/listenLog.do",
		data:recFileData,
		type:"POST",
		dataType:"json",
		async: true,
		cache: false,
		success:function(jRes){}
	});
	return url;
}

function formToSerialize(requestOrderBy){
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	var sTime = $("#sTime").val();
	var eTime = $("#eTime").val();
	var sTtime = $("#searchsCallTtime").val();
	var eTtime = $("#searcheCallTtime").val();
	var sysCode = $("#searchSysCode").val();
	var extNum = $("#searchExtNum").val();
	var extIp = $("#searchExtIp").val();
	var callId = $("#searchCallId").val();
	var custPhone = $("#searchCustPhone").val();
	var callKind = $("#searchCallKind").val();
	var fileName = $("#searchFileName").val();
	var fileState = $("#searchFileState").val();
	var recfileExists = $("#searchRecfileExists").val();
	var listType = $("#searchListType").val();

	var strData = "?limitUse=Y&sDate="+sDate+"&eDate="+eDate+"&sTime="+sTime+"&eTime="+eTime+"&sTtime="+sTtime+"&eTtime="+eTtime+"&sysCode="+sysCode
	+"&extNum="+extNum+"&extIp="+extIp+"&callId="+callId+"&custPhone="+custPhone+"&callKind="+callKind+"&fileName="+fileName+"&fileState="+fileState+"&recfileExists="+recfileExists
	+"&listType="+listType;

	// 현재 조회된 리스트 타입이 뭔지 알기위해 -> genesys냐 rtp냐에 따라 테이블+보내는 데이터가 달라짐
	 $("#nowListType").val(listType);
	 $("#nowSearchType").val(recfileExists);
	 
	return encodeURI(strData);
}

function time2seconds(time){
	var hour, min, sec, resultSec;
	hour = time.split(":")[0];
	min = time.split(":")[1];
	sec = time.split(":")[2];
	
	resultSec = parseInt(hour*3600);
	resultSec += parseInt(min*60);
	resultSec += parseInt(sec);

	return resultSec;
}