// 전역변수 설정
var gridPacketManage; // 그리드


window.onload = function() {
	gridPacketManageLoad() // 로고 관리 그리드 로드
	
	// 팝업창 로고 등록
	$("#uploadPacketBtn").click(function(){
		onUploadPacketProc();
	});
	
	// 로고 변경 팝업창 콤보 값 바뀌면
	$("#PacketChangeUse").change(function(){
		var PacketChangeUseYn = $("#PacketChangeUse").val();
		if(PacketChangeUseYn == "Y") {
			$(".upload_disabled").removeClass("ui_input_hasinfo");
			$(".upload_disabled").attr("disabled",false);
		} else {
			$(".upload_disabled").addClass("ui_input_hasinfo");
			$(".upload_disabled").attr("disabled",true);
		}
	});
	
	// 메인창 서버 추가
	$("#systemAddBtn").click(function(){
		$("#addServer").find("p").text(lang.admin.systemOption.label.settingAdd)
		$("#custCode").attr("disabled", false);
		
		
		$("#custCode").val("");
		$("#phoneSetting").val("");
		$("#returnMsg").val("");
		$("#returnUrl").val("");

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
	
	ui_controller();
}

// 로고 관리 그리드 로드
function gridPacketManageLoad() {
	objGrid = new dhtmlXGridObject("gridPacketManage");
	// 로고 관리 그리드
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/Packet_list.xml", function(){
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
	}, 'xml')
	
	objGrid.attachEvent("onXLS", function(){
		progress.on()
	});
	
	objGrid.attachEvent("onXLE", function(grid_obj,count){
		ui_controller();
		progress.off();

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
			$(top.window).trigger("resize");
		});
	});

	objGrid.attachEvent("onRowSelect", function(id,ind){
	    return;
	});
	
	// 로우 더블클릭 시 수정 팝업 오픈
	objGrid.attachEvent("onRowDblClicked", function(id,ind){

		$("#addServer").find("p").text(lang.admin.systemOption.label.settingModi)
		$("#custCode").attr("disabled", true);
		var custCode =  this.cells(id, 1).getValue();
		var	phoneSetting = this.cells(id, 2).getValue();
		var returnMsg = this.cells(id, 3).getValue();
		var returnUrl = this.cells(id, 4).getValue();

		$("#custCode").val(custCode);
		$("#phoneSetting").val(phoneSetting);
		$("#returnMsg").val(returnMsg);
		$("#returnUrl").val(returnUrl);

		$("#systemAdd").hide();
		$("#systemModify").show();

		layer_popup('#addServer');
	});
    ui_controller();
}

//파일 형식 검사
function chkImgFomat(val) {
	var chkVal = false;
	var dotIdx = val.lastIndexOf(".");
	val = val.substring(dotIdx).toLowerCase();
	if (val == ".png") {
		chkVal = true;
	}
	return chkVal; 
}

//@ezra
//서버 추가, 기존 로직 그대로 사용
//FIXME : 저장위치 이렇게 쓰면, 사용자 실수가 나올 가능성이 높아 보임.
function onSystemAddProc(){

	var custCode = $("#custCode").val();
	var phoneSetting = $("#phoneSetting").val();
	var returnMsg = $("#returnMsg").val();
	var returnUrl = $("#returnUrl").val();
		
	var dataStr = {
			"cmd"					: "insert"
		,	"custCode"				: custCode
	    ,   "phoneSetting"			: phoneSetting
	    ,	"returnMsg"				: returnMsg
	    ,   "returnUrl"				: returnUrl
	};
	
	if (!confirm(lang.views.player.html.text37)){
		return;
	}

	$.ajax({
		url:contextPath+"/packet_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				
				
				alert(jRes.resData.msg);
				
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/Packet_list.xml", function(){})
			} else if(jRes.success == "N"){
				alert(jRes.resData.msg);
			}
		}
	});
};

function onSystemModifyProc(){

	var custCode = $("#custCode").val();
	var phoneSetting = $("#phoneSetting").val();
	var returnMsg = $("#returnMsg").val();
	var returnUrl = $("#returnUrl").val();

	var dataStr = {
			"cmd"					: "update"
		,	"custCode"				: custCode
	    ,   "phoneSetting"			: phoneSetting
	    ,	"returnMsg"				: returnMsg
	    ,   "returnUrl"				: returnUrl
	};

	if (!confirm(lang.views.player.html.text38)){
		return;
	}
	
	$.ajax({
		url:contextPath+"/packet_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				
				
				alert(jRes.resData.msg);
				
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/Packet_list.xml", function(){})
			} else if(jRes.success == "N"){
				alert(jRes.resData.msg);
			}
		}
	});
};

//서버 삭제
function onSystemDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstSysId = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstSysId.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
		}
		var rst = rstSysId.join(",");
		
		if (!confirm(lang.admin.alert.itemManage8)){
			return;
		}
				
		$.ajax({
			url:contextPath+"/packet_proc.do",
			data:{
					"custCode" : rst
				,	"cmd"	   : "delete"
				},
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					
					
					alert(jRes.resData.msg);
					
					layer_popup_close();
					objGrid.clearAndLoad(contextPath+"/Packet_list.xml", function(){})
				} else if(jRes.success == "N"){
					alert(jRes.resData.msg);
				}
			}
		});
	}
}