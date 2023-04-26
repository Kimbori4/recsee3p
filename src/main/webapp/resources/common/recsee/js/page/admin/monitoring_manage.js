// 전역변수 설정
var gridSystemManage; // 그리드
var forEachResult=false;
var targetIPChk;
addLoadEvent(SystemManageLoad);

function SystemManageLoad() {
	gridSystemManage = SystemManageGridLoad();
	formFunction();
	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn!= 'Y') {
		$('#btnSystemAdd').hide();
	}

	if(delYn != 'Y') {
		$('#btnSystemDel').hide();
	}
}

// 채널관리 로드
function SystemManageGridLoad() {
    // 채널관리 Grid
	gridSystemManage = new dhtmlXGridObject("gridSystemManage");
	gridSystemManage.setIconsPath(recseeResourcePath + "/images/project/");
	gridSystemManage.setImagePath(recseeResourcePath + "/images/project/");
	gridSystemManage.enablePreRendering(50);
    gridSystemManage.setPagingSkin("toolbar", "dhx_web");
	gridSystemManage.enableColumnAutoSize(false);
	gridSystemManage.enablePreRendering(50);
    gridSystemManage.setSkin("dhx_web");
	gridSystemManage.init();
	gridSystemManage.load(contextPath+"/SystemManage.xml", function(){

		$(window).resize();
		gridSystemManage.setSizes();

		$(window).resize(function() {
			gridSystemManage.setSizes();
		});
		
	}, 'xml')

	// 체크박스 전체 선택
	gridSystemManage.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				gridSystemManage.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				gridSystemManage.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	
	gridSystemManage.attachEvent("onRowSelect" , function (id, ind){
		return true;
	});


	gridSystemManage.attachEvent("onRowDblClicked", function(id,ind){
		$("#addTarget").find("p").text(lang.admin.alert.monitoringManage1)

		var targetId 	= this.cells(id, 1).getValue();
		var	targetName 		= this.cells(id, 3).getValue();
		var targetType;
		switch(this.cells(id, 4).getValue()){
			case '서버':targetType='server';
				break;
			case '업무':targetType='job'
				break;
			default :targetType='tel'
		};
		var targetIp 		= this.cells(id, 5).getValue();
		var targetDescr		= this.cells(id, 6).getValue();
		var watchYn  		= this.cells(id, 7).getValue();
		targetIPChk=targetIp;
		
		$("#targetid").val(targetId);
		$("#targetName").val(targetName);
		$("#targetType").val(targetType);
		$("#targetIP").val(targetIp);
		$("#targetDescr").val(targetDescr);
		$("#watchYn").val(watchYn);

		$("#targetAddBtn").hide();
		$("#targetlModifyBtn").show();
		layer_popup('#addTarget')

	});
	
    ui_controller();
    return gridSystemManage;
}

function formFunction() {

	// 관제대상 추가버튼
	$('#btnTargetlAdd').click(function() {

		$("#addSystem").find("p").text(lang.admin.alert.monitoringManage2)

		$("#targetid").val('')
		$("#targetName").val('');
		$("#targetType").val('server');
		$("#targetIP").val('');
		$("#targetDescr").val('');
		$("#watchYn").val('Y');

		
		$("#targetlModifyBtn").hide();
		$("#targetAddBtn").show();
		
		targetIPChk='';
		
		layer_popup('#addTarget')
	})


	// 팝업 저장 버튼
	$('#targetAddBtn').click(function() {
		onSystemAddProc();
	});

	// 팝업 수정 버튼
	$('#targetlModifyBtn').click(function() {
		onSystemModifyProc();
	})

	// 채널 삭제 버튼
	$('#btnTargetDel').click(function() {
		onSystemDel();
	})
}



function onSystemDel() {
	if(gridSystemManage.getCheckedRows(0) != "") {
		var checked = gridSystemManage.getCheckedRows(0).split(",");
		var rstChNum = new Array();

		for( var index=0;index<checked.length;index++) {
			rstChNum.push(gridSystemManage.cells(parseInt(checked[index]),1).getValue());
		}
		var rst = rstChNum.join(",");
	
		if(confirm(lang.admin.alert.itemManage8)){
			$.ajax({
				url:contextPath+"/Monitoring_target_proc.do",
				data:"method=del&data="+ rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.attrManage2)
						gridSystemManage.clearAndLoad(contextPath + "/SystemManage.xml", function(){ }, "xml");
					} else {
						alert(lang.admin.alert.attrManage3)
					}
				}
			});
		}
 	} else {
 		alert (lang.admin.alert.attrManage4)
 	}
}

function onSystemAddProc(){
	
	var targetName= $("#targetName").val()
	var targetType= $("#targetType").val();
	var targetIP= $("#targetIP").val();
	var targetDescr= $("#targetDescr").val();
	var watchYn= $("#watchYn").val();

	if (targetName == null || targetName == "") {
		alert(lang.admin.alert.monitoringManage3);
		$('#targetName').focus();
		return;
	}else if(targetType == null || targetType == ""){
		alert(lang.admin.alert.monitoringManage4);
		$('#targetType').focus();
		return;
	} else if(targetIP == null || targetIP == "") {
		alert(lang.admin.alert.monitoringManage5);
		$('#targetIP').focus();
		return;
	} else if(watchYn == null || watchYn == ""){
		alert(lang.admin.alert.monitoringManage6);
		$('#watchYn').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(targetIP))) {
		alert(lang.admin.alert.monitoringManage7);
		$('#targetIP').focus();
		return false;
	}
	forEachResult =true;
	// 중복 처리
	if(gridSystemManage.getRowsNum() > 0 ) {
		gridSystemManage.forEachRow(function(id){
			if(targetName == this.cells(id,3).getValue()) {
				alert(lang.monitoring.alert.sameName /* "이미 등록된 이름입니다." */)
				$('#targetName').focus();
				forEachResult = false;
			} else if(targetIP == this.cells(id,5).getValue()) {
				alert(lang.admin.alert.monitoringManage8)
				$('#targetIP').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;
	

	var dataStr = {
			"method"  : "add"
	    ,   "targetName"		: targetName
	    ,	"targetType"			: targetType
	    ,   "targetIP"				: targetIP
	    ,   "targetDescr"		: targetDescr
	    ,   "watchYn"		: watchYn
	};
	$.ajax({
		url:contextPath+"/Monitoring_target_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.monitoringManage11);

				layer_popup_close();
				gridSystemManage.clearAndLoad(contextPath+"/SystemManage.xml", function(){})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.monitoringManage12)
			}
		}
	});
}


function onSystemModifyProc(){
	var targetid= $("#targetid").val();
	var targetName= $("#targetName").val();
	var targetType= $("#targetType").val();
	var targetIP= $("#targetIP").val();
	var targetDescr= $("#targetDescr").val();
	var watchYn= $("#watchYn").val();


	if (targetName == null || targetName == "") {
		alert(lang.admin.alert.monitoringManage3);
		$('#targetName').focus();
		return;
	}else if(targetType == null || targetType == ""){
		alert(lang.admin.alert.monitoringManage4);
		$('#targetType').focus();
		return;
	} else if(targetIP == null || targetIP == "") {
		alert(lang.admin.alert.monitoringManage5);
		$('#targetIP').focus();
		return;
	} else if(watchYn == null || watchYn == ""){
		alert(lang.admin.alert.monitoringManage6);
		$('#watchYn').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(targetIP))) {
		alert(lang.admin.alert.monitoringManage7);
		$('#targetIP').focus();
		return false;
	}
	
	forEachResult =true;
	// 중복 처리
	if(gridSystemManage.getRowsNum() > 0 ) {
		gridSystemManage.forEachRow(function(id){
			if(targetIP == this.cells(id,5).getValue() && targetIPChk!=targetIP) {
				alert(lang.admin.alert.monitoringManage8)
				$('#targetIP').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;
	
	var dataStr = {
			"method"  : "upd"
		,	"targetid"				: targetid
	    ,   "targetName"		: targetName
	    ,	"targetType"			: targetType
	    ,   "targetIP"				: targetIP
	    ,   "targetDescr"		: targetDescr
	    ,   "watchYn"		: watchYn
	};

	$.ajax({
		url:contextPath+"/Monitoring_target_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.monitoringManage9);

				layer_popup_close();
				gridSystemManage.clearAndLoad(contextPath+"/SystemManage.xml", function(){})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.monitoringManage10)
			}
		}
	});
}
