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

	authyLoad();
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
	objGrid.load(contextPath+"/stt_server_list.xml", function(){
		
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
		var	sysName = this.cells(id, 2).getValue();
		var sysIp = this.cells(id, 3).getValue();

		$("#sysId").val(sysId).attr("disabled","disabled");
		$("#sysName").val(sysName);
		$("#sysIp").val(sysIp);

		$("#systemAdd").hide();
		$("#systemModify").show();

		layer_popup('#addServer');
	});


    ui_controller();
}


// @ezra
// 서버 추가, 기존 로직 그대로 사용
// FIXME : 저장위치 이렇게 쓰면, 사용자 실수가 나올 가능성이 높아 보임.
function onSystemAddProc(){

	var sysId = $('#sysId').val();
	var sysName =  $('#sysName').val();
	var sysIp =  $('#sysIp').val();
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
			} else if(sysIp == objGrid.cells(id,3).getValue()) {
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
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/stt_server_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.serverManage20);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/stt_server_list.xml", function(){})
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
	    ,	"proc"              : "modify"
	};

	$.ajax({
		url:contextPath+"/stt_server_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.serverManage14);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/stt_server_list.xml", function(){})
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
				url:contextPath+"/stt_server_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.serverManage17)
						objGrid.clearAndLoad(contextPath + "/stt_server_list.xml", function(){ }, "xml");
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