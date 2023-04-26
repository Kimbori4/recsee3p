// �������� ����
var gridUserDBInterface; // �׸���
var userDBInterface = "", userDBInterfaceRes = "";
var userDBInterfaceAddJSON = "";
var winObj;

addLoadEvent(gridUserDBInterfaceLoad);

function gridUserDBInterfaceLoad() {
	
	gridUserDBInterface = gridUserDBInterfaceGridLoad();
	onOptionFunction();

	// �߰�
	$('#addBtn').click(function() {
		onAddProc();
	});
	
	// �˾�â  ����
	$('#modifyBtn').click(function(){
		onModifyProc();
	});
	
	// ����
	$('#deleteBtn').click(function() {
		onDeleteProc();
	});
	
	authyLoad();
}

//���� �ҷ� ���� // ���� ��� ���� �ʿ�
function authyLoad() {
	/*if(modiYn != 'Y') {
		$('#subNumberAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#subNumberDel').hide();
	}*/
}
// �����DB �������̽� Scheduler ���� �ε�
function gridUserDBInterfaceGridLoad() {
    // ����DB �������̽� Scheduler Grid
	objGrid = new dhtmlXGridObject("gridUserDBInterface");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingUserDBInterface", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/userDBInterface_list.xml", function(){
		
		var search_toolbar = objGrid.aToolBar;
		
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum() + '</div>')
		search_toolbar.setWidth("total",150)

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

	// üũ�ڽ� ��ü ����
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

	objGrid.attachEvent("onRowDblClicked", function(i,ind){
			$("#popupUserDBInterface").find("p").text(lang.admin.userDBInterface.title.modify)
			
			var rDbIp = this.cells(i, 2).getValue();
			var rDbUser = this.cells(i, 3).getValue();
			var rDbPwd = this.cells(i, 4).getValue();
			var rDbPort = this.cells(i, 5).getValue();
			var rDbName = this.cells(i, 6).getValue();
			var rTeamCode = this.cells(i, 7).getValue();
			var rTeamName = this.cells(i, 8).getValue();
			var rHh = this.cells(i, 9).getValue();
			var seq = this.cells(i, 1).getValue();
			
			$("#rDbIpPop").val(rDbIp);
			$("#rDbUserPop").val(rDbUser);
			$("#rDbPwdPop").val(rDbPwd);
			$("#rDbPortPop").val(rDbPort);
			$("#rDbNamePop").val(rDbName);
			$("#rTeamCodePop").val(rTeamCode);
			$("#rTeamNamePop").val(rTeamName);
			$("#rHhPop").val(rHh);
			$("#seqPop").val(seq);
			
			$("#modifyBtn").show();
		layer_popup('#popupUserDBInterface');
	});

    ui_controller();
}

function onAddProc() {
	var rDbIp = $("#rDbIp").val();
	var rDbUser = $("#rDbUser").val();
	var rDbPwd = $("#rDbPwd").val();
	var rDbPort = $("#rDbPort").val();
	var rDbName = $("#rDbName").val();
	var rTeamCode = $("#rTeamCode").val();
	var rTeamName = $("#rTeamName").val();
	var rHh = $("#rHh").val();
	
	if (rDbIp == null || rDbIp == "") {
		alert(lang.admin.userDBInterface.alert.message1);
		$('#rDbIp').focus();
		return;
	}
	if (rDbUser == null || rDbUser == "") {
		alert(lang.admin.userDBInterface.alert.message2);
		$('#rDbUser').focus();
		return;
	}
	if (rDbPwd == null || rDbPwd == "") {
		alert(lang.admin.userDBInterface.alert.message3);
		$('#rDbPwd').focus();
		return;
	}
	if (rDbPort == null || rDbPort == "") {
		alert(lang.admin.userDBInterface.alert.message4);
		$('#rDbPort').focus();
		return;
	}
	if (rDbName == null || rDbName == "") {
		alert(lang.admin.userDBInterface.alert.message5);
		$('#rDbName').focus();
		return;
	}
	if (rTeamCode == null || rTeamCode == "") {
		alert(lang.admin.userDBInterface.alert.message6);
		$('#rTeamCode').focus();
		return;
	}
	if (rTeamName == null || rTeamName == "") {
		alert(lang.admin.userDBInterface.alert.message7);
		$('#rTeamName').focus();
		return;
	}
	if (rHh == "*") {
		alert(lang.admin.userDBInterface.alert.message8);
		$('#rHh').focus();
		return;
	}
	
	var forEachResult =true
	
	if(!forEachResult)
		return;
	var dataStr = {
				"rDbIp"				: rDbIp
			,	"rDbUser"			: rDbUser
			,	"rDbPwd"			: rDbPwd
			,	"rDbPort"			: rDbPort
			,	"rDbName"			: rDbName
			,	"rTeamCode"			: rTeamCode
			,	"rTeamName"			: rTeamName
			,	"rHh"				: rHh
			,	"proc"              : "insert"
	};
	
	$.ajax({
		url:contextPath+"/userDBInterface_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB�� ��ȸ�� ������ ������
			if(jRes.success == "Y") {
				alert(lang.admin.userDBInterface.alert.message10);
				$("#rDbIp").val("");
				$("#rDbUser").val("");
				$("#rDbPwd").val("");
				$("#rDbPort").val("");
				$("#rDbName").val("");
				$("#rTeamCode").val("");
				$("#rTeamName").val("");
				$("#rHh").val("*");
				objGrid.clearAndLoad(contextPath+"/userDBInterface_list.xml", function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.userDBInterface.alert.message11);
			}
		}
	});
}


function onModifyProc() {
	var rDbIp = $("#rDbIpPop").val();
	var rDbUser = $("#rDbUserPop").val();
	var rDbPwd = $("#rDbPwdPop").val();
	var rDbPort = $("#rDbPortPop").val();
	var rDbName = $("#rDbNamePop").val();
	var rTeamCode = $("#rTeamCodePop").val();
	var rTeamName = $("#rTeamNamePop").val();
	var rHh = $("#rHhPop").val();
	var seq = $("#seqPop").val();
	
	if (rDbIp == null || rDbIp == "") {
		alert(lang.admin.userDBInterface.alert.message1);
		$('#rDbIpPop').focus();
		return;
	}
	if (rDbUser == null || rDbUser == "") {
		alert(lang.admin.userDBInterface.alert.message2);
		$('#rDbUserPop').focus();
		return;
	}
	if (rDbPwd == null || rDbPwd == "") {
		alert(lang.admin.userDBInterface.alert.message3);
		$('#rDbPwdPop').focus();
		return;
	}
	if (rDbPort == null || rDbPort == "") {
		alert(lang.admin.userDBInterface.alert.message4);
		$('#rDbPort').focus();
		return;
	}
	if (rDbName == null || rDbName == "") {
		alert(lang.admin.userDBInterface.alert.message5);
		$('#rDbName').focus();
		return;
	}
	if (rTeamCode == null || rTeamCode == "") {
		alert(lang.admin.userDBInterface.alert.message6);
		$('#rTeamCodePop').focus();
		return;
	}
	if (rTeamName == null || rTeamName == "") {
		alert(lang.admin.userDBInterface.alert.message7);
		$('#rTeamName').focus();
		return;
	}
	if (rHh == "*") {
		alert(lang.admin.userDBInterface.alert.message8);
		$('#rHh').focus();
		return;
	}
	
	var forEachResult =true
	
	if(!forEachResult)
		return;
	
	var dataStr = {
			"rDbIp"				: rDbIp
		,	"rDbUser"			: rDbUser
		,	"rDbPwd"			: rDbPwd
		,	"rDbPort"			: rDbPort
		,	"rDbName"			: rDbName
		,	"rTeamCode"			: rTeamCode
		,	"rTeamName"			: rTeamName
		,	"rHh"				: rHh
		,	"seq"				: seq
		,	"proc"              : "modify"
	};
	
	$.ajax({
		url:contextPath+"/userDBInterface_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB�� ��ȸ�� ������ ������
			if(jRes.success == "Y") {
				alert(lang.admin.userDBInterface.alert.message12);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/userDBInterface_list.xml", function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.userDBInterface.alert.message13);
			}
		}
	});
}

// ����
function onDeleteProc() {
	if(objGrid.getCheckedRows(0) != "") {
		if(confirm(lang.admin.userDBInterface.confirm.message1)){
			var checked = objGrid.getCheckedRows(0).split(",");
			var rstIdx = new Array();
			
			for(var i = 0 ; i < checked.length ; i++){
				rstIdx.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
			}
			var rst = rstIdx.join(",");
			$.ajax({
				url:contextPath+"/userDBInterface_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.userDBInterface.alert.message14);
						objGrid.clearAndLoad(contextPath + "/userDBInterface_list.xml", function(){ 
						}, "xml");
					} else {
						alert(lang.admin.userDBInterface.alert.message15);
					}
				}
			});
		}
	} else {
		alert (lang.admin.userDBInterface.alert.message16);
	}
}

function onOptionFunction() {
	for(var i=1;i<24;i++){
		$("#rHh").append("<option value="+i+">"+i+"</option");	
		$("#rHhPop").append("<option value="+i+">"+i+"</option");	
	} 
}

function quickExcution(i){
	
	$.ajax({
		url : contextPath+"/quickExcution_proc",
		data : {
			seq : objGrid.cells(i,1).getValue()
		},
		type:"POST",
		dataType:"json",
		success : function(jRes){
			if(jRes.success == "Y") {
				alert("success"/*lang.admin.dbManage.alert.message44*/);
				objGrid.clearAndLoad(contextPath + "/userDBInterface_list.xml", function(){ 
				}, "xml");
			} else {
				alert("fail"/*lang.admin.dbManage.alert.message45*/);
			}
		}
	});
	
}
