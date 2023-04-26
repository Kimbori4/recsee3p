// 전역변수 설정
var gridServerManage; // 그리드
var system = "", sysRes = "";
var sysAddJSON = "";

addLoadEvent(serverManageLoad);

function serverManageLoad() {
	gridServerManage = serverManageGridLoad();

	// 메인창 서버 추가
	$("#systemAddBtn").click(function(){
		$("#addServer").find("p").text(lang.admin.alert.serverManage1)

		$("#sysId").val("").removeAttr("disabled");
		$("#sysName").val("");
		$("#sysIp").val("");
		$("#pbxId").val("");
		$("#storagePath").val("");
		$("#sysDeleteYN").val("N");
		$("#sysDeleteSize").val("");
		$("#sysDeletePath").val("");
		$("#sysDeleteYN").trigger("change");

		$("#systemAdd").show();
		$("#systemModify").hide();

		layer_popup('#addServer');
	});

	// 팝업창 서버 추가
	$('#systemAdd').click(function(){
		onSystemAddProc();
	});

	// 팝업창 서버 수정
	$('#systemModify').click(function(){
		onSystemModifyProc();
	});

	// 서버 삭제
	$('#systemDel').click(function() {
		onSystemDelProc()
	})
	
	// 시스템 전환 팝업 sysId 로드
	selectOptionLoad($("#sSysId"), "system");
	
	// 시스템 전환 팝업 전환 버튼 클릭
	$('#switchSystem').click(function() {
		onSwitchSystem()
	})	
	
	$("#sysDeleteYN").change(function() {
		var sysDeleteYN = $("#sysDeleteYN").val();
		if (sysDeleteYN == "Y") {
			$("#sysDeleteSize").attr("disabled", false);
			$("#sysDeletePath").attr("disabled", false);
		} else {
			$("#sysDeleteSize").attr("disabled", true);
			$("#sysDeletePath").attr("disabled", true);
		}
	});	
	authyLoad();
}
//그리드 복원 버튼 클릭
function returningSwitchSystemPop(id) {
	var sysIdVal = gridServerManage.cells(id,1).getValue();	// 선택한 로우의 시스템 코드
	var sSysIdVal = gridServerManage.cells(id,7).getValue();	// 선택한 로우의 전환된 시스템 코드
	
	$.ajax({
		url:contextPath+"/switchSystemProc.do",
		data:{
			"sysId" : sysIdVal,
			"sSysId" : sSysIdVal,
			"type" : "returning"
		},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// update 성공시
			if(jRes.success == "Y") {
				alert("시스템이 복원되었습니다.")
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/system_list.xml", function(){})
				return
			} else if(jRes.success == "N") {
				alert("시스템 복원에 실패하였습니다. \n" + jRes.resData.msg)
				layer_popup_close();
			}
		}
	});
}

// 그리드 전환 버튼 클릭
function openSwitchSystemPop(id) {
	var sSysIdVal = gridServerManage.cells(id,1).getValue();	// 선택한 로우의 시스템 코드
	$("#oriSysId").val(sSysIdVal);								// 내선
	$("#sSysId").val(sSysIdVal);								// 
	layer_popup('#switchSystemPop');
}

// 시스템 전환 팝업 전환 버튼 클릭(1. ch_info update, 2. sys_info update)
function onSwitchSystem() {
	var sysId = $("#oriSysId").val(); // 원래 시스템 코드
	var sSysId = $("#sSysId").val(); // 전환 할 시스템 코드
	
	if (sysId == sSysId) {
		alert("동일한 시스템으로 전환 할 수 없습니다.");
		return;
	}
	
	var dataStr = {
			"sysId" : sysId,
			"sSysId" : sSysId,
			"type" : "switch"
	}
	$.ajax({
		url:contextPath+"/switchSystemProc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// update 성공시
			if(jRes.success == "Y") {
				alert("시스템이 전환되었습니다.")
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/system_list.xml", function(){})
				return
			} else if(jRes.success == "N") {
				alert("시스템 전환에 실패하였습니다. \n" + jRes.resData.msg)
				layer_popup_close();
			}
		}
	});
}
//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		$('#systemAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#systemDel').hide();
	}
}
// 서버관리 로드
function serverManageGridLoad() {
    // 서버관리 Grid
	objGrid = new dhtmlXGridObject("gridServerManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingServerManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/system_list.xml", function(){
		
		var search_toolbar = objGrid.aToolBar;
		
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum() + '</div>')
		search_toolbar.setWidth("total",150)

		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		$("#gridServerManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter ipFilter")
	}, 'xml')
	
	objGrid.attachEvent("onXLS", function(){
		progress.on()
	});
	
	objGrid.attachEvent("onXLE", function(grid_obj,count){
		
		$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
		
		if (objGrid.getRowsNum() > 0){
			try{
				var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>'
				objGrid.aToolBar.setItemText("total", setResult)
			}catch(e){tryCatch(e)}
		}
		
		ui_controller();
		progress.off();

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
			$(top.window).trigger("resize");
		});
	});
/*
	objGrid.attachEvent("onFilterEnd", function(elements) {
		var flag = true;
		for(var key in elements) {
			if( elements[key][0].value.length > 0 ) flag = false;
		}
		for(var i=0; i<elements.length;i++){
			if( elements[key][0].value.length > 0 ) flag = false;
		}
		if (flag) objGrid.clearAndLoad(contextPath + "/system_list.xml");
	});*/

	// 체크박스 전체 선택
	objGrid.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				objGrid.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				objGrid.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	objGrid.attachEvent("onRowSelect", function(id,ind){
	    return;
	});

	objGrid.attachEvent("onRowDblClicked", function(id,ind){
		$("#addServer").find("p").text(lang.admin.alert.serverManage2)

		var sysId =  this.cells(id, 1).getValue();
		var sysName = this.cells(id, 2).getValue();
		var sysIp = this.cells(id, 3).getValue();
		var pbxId = this.cells(id, 4).getValue();
		var storagePath = this.cells(id, 5).getValue();
		var sysDeleteYN = this.cells(id, 9).getValue();
		var sysDeleteSize = this.cells(id, 10).getValue();
		var sysDeletePath = this.cells(id, 11).getValue();

		
		$("#sysId").val(sysId).attr("disabled","disabled");
		$("#sysName").val(sysName);
		$("#sysIp").val(sysIp);
		$("#pbxId").val(pbxId);
		$("#storagePath").val(storagePath);

		$("#sysDeleteYN").val(sysDeleteYN);
		$("#sysDeleteSize").val(sysDeleteSize);
		$("#sysDeletePath").val(sysDeletePath);
		$("#sysDeleteYN").trigger("change");
		
		$("#systemAdd").hide();
		$("#systemModify").show();

		layer_popup('#addServer');
	});

	/*objGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue){
	 	//if(nowAccessInfo.getModiYn().equals("Y")) {
	 	if(modiYn == "Y") {
			if(cInd == 0) return true;

			if(stage == 2) {
				if(nValue == oValue) return false;

				var param = "proc=modify";

				param += "&sysId=" + objGrid.cells(rId,1).getValue();

				switch(cInd) {
				case 2: param += "&sysName=";
					if (nValue == "" || nValue.length < 1) {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="management.system.alert.sysName.empty" />"
						});
						return false;
					} else if (nValue == "0") {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="management.system.alert.sysName.zero" />"
						});
						return false;
					} else if (!nValue.isSpecialCharCheck(true,"-.")) {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="ws.message.isCharacter" />"
						});
						return false;
					}

					break;
				case 3: param += "&sysIp=";
					if (!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(nValue))) {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="management.system.alert.sysIp.rule_violation" />",
							callback: function(result) {
								objForm.setItemFocus("sysIp");
							}
						});
						return;
					}
					break;
				case 4: param += "&pbxId="; break;
				case 5: param += "&storagePath="; break;
				}
				param += nValue;
				var response = window.dhx4.ajax.postSync(contextPath+"/management/system_proc.do?" + param );
				response = window.dhx4.s2j(response.xmlDoc.responseText);

				var returnFlag = true;
				var msg = "<spring:message code="management.system.message.modify.success" />";
				if(response.result != "1") {
					returnFlag = false;
					msg = "<spring:message code="management.system.message.modify.fail" />";
				}

				dhtmlx.alert({
					type:"alert",
					title:"<spring:message code="message.title.notifications" />",
					ok:"<spring:message code="message.btn.ok" />",
					text:msg
				});
				return returnFlag;
			}
		} else {
		return false;
		}
	});*/

    ui_controller();
    
    return objGrid;
}


// @ezra
// 서버 추가, 기존 로직 그대로 사용
// FIXME : 저장위치 이렇게 쓰면, 사용자 실수가 나올 가능성이 높아 보임.
function onSystemAddProc(){

	var sysId = $('#sysId').val();
	var sysName =  $('#sysName').val();
	var sysIp =  $('#sysIp').val();
	
	var sysDeleteYN =  $('#sysDeleteYN').val();
	var sysDeleteSize =  $('#sysDeleteSize').val();
	var sysDeletePath =  $('#sysDeletePath').val();
	
	var pbxId =  $('#pbxId').val();
	var storagePath =  $('#storagePath').val();

	if (sysId == null || sysId == "") {
		alert(lang.admin.alert.serverManage3);
		$('#sysId').focus();
		return;
	} else if(sysId.length < 2) {
		alert(lang.admin.alert.serverManage4);
		$('#sysId').focus();
		return;
	} else if(sysId.length > 5) {
		alert(lang.admin.alert.serverManage5);
		$('#sysId').focus();
		return;
	} /*else if(pbxId.length > 5) {
		alert("PBX 아이디는 다섯 글자까지 입력해주세요.");
		$('#pbxId').focus();
		return;
	}*/

	else if (sysDeleteYN == null || sysDeleteYN == "") {
		alert("삭제 사용 여부를 선택해주세요.");
		$('#sysDeleteYN').val("N");
		return;
	}
	if (sysDeleteYN == "Y") {
		if (sysDeleteSize == 0 || sysDeleteSize > 100) {
			alert("삭제 용량은 0부터 100사이로 입력이 가능합니다.");
			$('#sysDeleteSize').focus();
			return;
		}
	}
	
	if(sysName == null || sysName == "") {
		alert(lang.admin.alert.serverManage6);
		$('#sysName').focus();
		return;
	} else if(sysName.length < 2) {
		alert(lang.admin.alert.serverManage7);
		$('#sysName').focus();
		return;
	} else if(sysName.match(/[`~!@#$%^&*|\\\'\";:\/?]/gi)) {
		alert(lang.admin.alert.serverManage8);
		$('#sysName').focus();
		return;
	}
	

	if(sysIp == null || sysIp == "") {
		alert(lang.admin.alert.serverManage9);
		$('#sysIp').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(sysIp))) {
		alert(lang.admin.alert.monitoringManage7);
		$('#sysIp').focus();
		return false;
	}

	/*if(pbxId == null || pbxId == "") {
		alert("PBX ID를 입력해주세요.");
		$('#pbxId').focus();
		return;
	}
	if(storagePath == null || storagePath == "") {
		alert("저장위치를 설정해주세요.");
		$('#storagePath').focus();
		return;
	}*/
	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(sysId == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.serverManage11)
				$('#sysId').focus();
				forEachResult = false;
			} else if(sysName == objGrid.cells(id,2).getValue()) {
				alert(lang.admin.alert.serverManage12)
				$('#sysName').focus();
				forEachResult = false;
			} else if(sysId == objGrid.cells(id,1).getValue() && sysIp == objGrid.cells(id,3).getValue()) {
				alert(lang.admin.alert.serverManage13)
				$('#sysIp').focus();
				forEachResult = false;
			}
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"sysId"				: sysId
	    ,   "sysName"			: sysName
	    ,	"sysIp"				: sysIp
	    ,	"sysDeleteYN"		: sysDeleteYN
	    ,	"sysDeleteSize"	: sysDeleteSize
	    ,	"sysDeletePath"	: sysDeletePath
	    ,   "pbxId"				: pbxId
	    ,   "storagePath"		: storagePath
	    ,	"proc"              : "insert"
	};
	
	$.ajax({
		url:contextPath+"/system_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.serverManage20);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/system_list.xml", function(){})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.serverManage21)
			}
		}
	});
};

function onSystemModifyProc(){

	var sysId = $('#sysId').val();
	var sysName =  $('#sysName').val();
	var sysIp =  $('#sysIp').val();
	var pbxId =  $('#pbxId').val();
	var storagePath =  $('#storagePath').val();

	var sysDeleteYN =  $('#sysDeleteYN').val();
	var sysDeleteSize =  $('#sysDeleteSize').val();
	var sysDeletePath =  $('#sysDeletePath').val();
	
	if (sysId == null || sysId == "") {
		alert(lang.admin.alert.serverManage3);
		$('#sysId').focus();
		return;
	} else if(sysId.length < 2) {
		alert(lang.admin.alert.serverManage4);
		$('#sysId').focus();
		return;
	} else if(sysId.length > 5) {
		alert(lang.admin.alert.serverManage5);
		$('#sysId').focus();
		return;
	} /*else if(pbxId.length > 5) {
		alert("PBX 아이디는 다섯 글자까지 입력해주세요.");
		$('#pbxId').focus();
		return;
	}*/
	else if (sysDeleteYN == null || sysDeleteYN == "") {
		alert("삭제 사용 여부를 선택해주세요.");
		$('#sysDeleteYN').val("N");
		return;
	}
	if (sysDeleteYN == "Y") {
		if (sysDeleteSize == 0 || sysDeleteSize > 100) {
			alert("삭제 용량은 0부터 100사이로 입력이 가능합니다.");
			$('#sysDeleteSize').focus();
			return;
		}
	}
	
	if(sysName == null || sysName == "") {
		alert(lang.admin.alert.serverManage6);
		$('#sysName').focus();
		return;
	} else if(sysName.length < 2) {
		alert(lang.admin.alert.serverManage7);
		$('#sysName').focus();
		return;
	} else if(sysName.match(/[`~!@#$%^&*|\\\'\";:\/?]/gi)) {
		alert(lang.admin.alert.serverManage8);
		$('#sysName').focus();
		return;
	}

	if(sysIp == null || sysIp == "") {
		alert(lang.admin.alert.serverManage9);
		$('#sysIp').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(sysIp))) {
		alert(lang.admin.alert.monitoringManage7);
		$('#sysIp').focus();
		return false;
	}

	/*if(pbxId == null || pbxId == "") {
		alert("PBX ID를 입력해주세요.");
		$('#pbxId').focus();
		return;
	}*/
	/*if(storagePath == null || storagePath == "") {
		alert("저장위치를 설정해주세요.");
		$('#storagePath').focus();
		return;
	}*/
	var forEachResult = true;
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){

			if(this.getSelectedRowId() == id)
				return;

			if(sysId == this.cells(id,1).getValue()) {
				alert(lang.admin.alert.serverManage11)
				$('#sysId').focus();
				forEachResult = false;
			} else if(sysName == this.cells(id,2).getValue()) {
				alert(lang.admin.alert.serverManage12)
				$('#sysName').focus();
				forEachResult = false;
			} else if(sysIp == this.cells(id,3).getValue()) {
				alert(lang.admin.alert.serverManage13)
				$('#sysIp').focus();
				forEachResult = false;
			}
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"sysId"				: sysId
	    ,   "sysName"			: sysName
	    ,	"sysIp"				: sysIp
	    ,   "pbxId"				: pbxId
	    ,   "storagePath"		: storagePath
	    ,	"sysDeleteYN"		: sysDeleteYN
	    ,	"sysDeleteSize"	: sysDeleteSize
	    ,	"sysDeletePath"	: sysDeletePath
	    ,	"proc"              : "modify"
	};

	$.ajax({
		url:contextPath+"/system_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.serverManage14);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/system_list.xml", function(){})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.serverManage15)
			}
		}
	});
};

// 서버 삭제
function onSystemDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstSysId = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstSysId.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
		}

		var rst = rstSysId.join(",");
		if (confirm(lang.admin.alert.serverManage16)){
			$.ajax({
				url:contextPath+"/system_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.serverManage17)
						objGrid.clearAndLoad(contextPath + "/system_list.xml", function(){ }, "xml");
					} else {
						alert(lang.admin.alert.serverManage18)
					}
				}
			});
		}
	} else {
		alert (lang.admin.alert.serverManage19)
	}
}