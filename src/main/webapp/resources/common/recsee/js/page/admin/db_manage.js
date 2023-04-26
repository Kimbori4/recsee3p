// 전역변수 설정
var gridDBManage; // 그리드
var dbManage = "", dbManageRes = "";
var dbManageAddJSON = "";
var winObj;

addLoadEvent(gridDBManageLoad);

function gridDBManageLoad() {
	
	gridDBManage = gridDBManageGridLoad();
	onOptionFunction();
	
	$("#dbManageBtn").click(function(){
		location.href = contextPath+"/admin/db_manage?tab=db";
	});
	$("#queryManageBtn").click(function(){
		location.href = contextPath+"/admin/db_manage?tab=query";
	});
	$("#jobManageBtn").click(function(){
		location.href = contextPath+"/admin/db_manage?tab=job";
	});
	$("#executeManageBtn").click(function(){
		location.href = contextPath+"/admin/db_manage?tab=execute";
	});
	
	// DB 추가
	$('#addDBBtn').click(function(){
		onDBAddProc();
	});
	// SQL 추가
	$('#addSQLBtn').click(function(){
		onSQLAddProc();
	});
	// Job 추가
	$('#addJobBtn').click(function(){
		onJobAddProc();
	});

	// 팝업창 DB 수정
	$('#dbModify').click(function(){
		onDBModifyProc();
	});
	
	// 팝업창 SQL 수정
	$('#sqlModify').click(function(){
		onSQLModifyProc();
	});
	
	// 팝업창 Job 수정
	$('#jobModify').click(function(){
		onJobModifyProc();
	});

	// DB 삭제
	$('#deleteBtn').click(function() {
		onDeleteProc();
	});
	
	// (monthly, weekly, daily) select태그 변경 
	$('#rSchedulerSelect').change(function(){
		onDisplayProc();
	});
	
	// execute 추가
	$('#executeBtn').click(function() {
		onExecuteProc();
	});
	
	// (monthly, weekly, daily) select태그 변경 
	$('#rSchedulerSelectPop').change(function(){
		onPopDisplayProc();
	});
	
	// 팝업창 Execute 수정
	$('#executeModify').click(function(){
		onExecuteModifyProc();
	});
	
	// Job 리스트에 추가
	$("#addListBtn").click(function(){
		onAddListProc();
	});
	// Job 리스트에 추가(popup)
	$("#addListBtnPop").click(function(){
		onAddListProcPop();
	});
	
	// db/sql set 동시선택
	$("#dbName").change(function(){
		$("#sqlName option:eq("+$(this).children('option:selected').index()+")").prop("selected", true);
	});
	
	$("#sqlName").change(function(){
		$("#dbName option:eq("+$(this).children('option:selected').index()+")").prop("selected", true);
	});
	// db/sql set 동시선택(popup)
	$("#dbNamePop").change(function(){
		$("#sqlNamePop option:eq("+$(this).children('option:selected').index()+")").prop("selected", true);
	});
	
	$("#sqlNamePop").change(function(){
		$("#dbNamePop option:eq("+$(this).children('option:selected').index()+")").prop("selected", true);
	});
	
	// Job 리스트에서 제거
	$("#delListBtn").click(function(){
		onDelListProc();
	});
	// Job 리스트에서 제거(popup)
	$("#delListBtnPop").click(function(){
		onDelListProcPop();
	});
	
	authyLoad();
}

//권한 불러 오기 // 권한 기능 구현 필요
function authyLoad() {
	/*if(modiYn != 'Y') {
		$('#subNumberAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#subNumberDel').hide();
	}*/
}
// DB Scheduler 관리 로드
function gridDBManageGridLoad() {
    // DB Scheduler 관리 Grid
	objGrid = new dhtmlXGridObject("gridDBManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingDBManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
		
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

	objGrid.attachEvent("onRowDblClicked", function(i,ind){
		var tab = $("#tab").val();
		if(tab=="db" || tab==""){
			var dbName = this.cells(i, 1).getValue();
			var dbServer = this.cells(i, 2).getValue();
			var url = this.cells(i, 3).getValue();
			var id = this.cells(i, 4).getValue();
			var pw = this.cells(i, 5).getValue();
			var timeout = this.cells(i, 6).getValue();
			
			$("#dbNamePop").val(dbName);
			$("#dbNamePop").prop("readonly", true);
			$("#dbNamePop").css("background", "#d2d2d2")
			$("#dbServerPop").val(dbServer);
			$("#urlPop").val(url);
			$("#idPop").val(id);
			$("#pwPop").val(pw);
			$("#timeoutPop").val(timeout);
			
			$("#dbModify").show();
		}else if(tab=="query"){
			var sqlName = this.cells(i, 1).getValue();
			var sql = this.cells(i, 2).getValue();
			
			$("#sqlNamePop").val(sqlName);
			$("#sqlNamePop").prop("readonly", true);
			$("#sqlNamePop").css("background", "#d2d2d2")
			$("#sqlPop").val(sql);
			
			$("#sqlModify").show();
		}else if(tab=="job"){
			$("#dbNamePop").empty();
			$("#sqlNamePop").empty();
			$("#dbNameSelectPop").val("");
			$("#sqlNameSelectPop").val("");
			
			var jobName = this.cells(i, 1).getValue();
			var dbName = this.cells(i, 2).getValue();
			var sqlName = this.cells(i, 3).getValue();
			
			$("#jobNamePop").val(jobName);
			$("#jpbNamePop").prop("readonly", true);
			$("#jobNamePop").css("background", "#d2d2d2");
			
			dbName = dbName.split("/");
			for(var i = 0 ; i < dbName.length ; i++){
				$("#dbNamePop").append("<option value="+dbName[i]+">"+dbName[i]+"</option")
			}
			sqlName = sqlName.split("/");
			for(var i = 0 ; i < sqlName.length ; i++){
				$("#sqlNamePop").append("<option value="+sqlName[i]+">"+sqlName[i]+"</option")
			}
			
			$("#addListBtnPop").show();
			$("#delListBtnPop").show();
			$("#jobModify").show();
		}else if(tab=="execute"){
			onPopDisplayProc()
			var executeName = this.cells(i, 1).getValue();
			var jobName = this.cells(i, 2).getValue();
			var rSchedulerSelect = this.cells(i, 3).getValue();
			var rSchedulerWeek = this.cells(i, 4).getValue();
			var rSchedulerDay = this.cells(i, 5).getValue();
			var rSchedulerHour = this.cells(i, 6).getValue();
			
			$("#executeNamePop").val(executeName);
			$("#executeNamePop").prop("readonly", true);
			$("#executeNamePop").css("background", "#d2d2d2")
			$("#jobNamePop").val(jobName);
			$("#rSchedulerSelectPop").val(rSchedulerSelect);
			$("#rSchedulerWeekPop").val(rSchedulerWeek);
			$("#rSchedulerDayPop").val(rSchedulerDay);
			$("#rSchedulerHourPop").val(rSchedulerHour);
			
			$("#executeModify").show();
		}
		layer_popup('#popupManage');
	});

    ui_controller();
}

function onDBAddProc() {
	var dbName = $("#dbName").val();
	var dbServer = $("#dbServer").val();
	var url = $("#url").val();
	var id = $("#id").val();
	var pw = $("#pw").val();
	var timeout = $("#timeout").val();

	if (dbName == null || dbName == "") {
		alert(lang.admin.dbManage.alert.message1);
		$('#dbName').focus();
		return;
	}

	if(dbServer == null || dbServer == "") {
		alert(lang.admin.dbManage.alert.message2);
		$('#dbServer').focus();
		return;
	}
	
	if(url == null || url == "") {
		alert(lang.admin.dbManage.alert.message3);
		$('#url').focus();
		return;
	}
	
	if(id == null || id == "") {
		alert(lang.admin.dbManage.alert.message4);
		$('#id').focus();
		return;
	}
	
	if(pw == null || pw == "") {
		alert(lang.admin.dbManage.alert.message5);
		$('#pw').focus();
		return;
	}
	
	if(timeout == null || timeout == "") {
		alert(lang.admin.dbManage.alert.message6);
		$('#timeout').focus();
		return;
	}

	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(dbName == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.dbManage.alert.message7);
				$('#dbName').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;
	var dataStr = {
			"dbName"			: dbName
	    ,   "dbServer"			: dbServer
	    ,	"url"				: url
	    ,	"id"        	    : id
	    ,	"pw"        		: pw
	    ,	"timeout"      		: timeout
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/dbManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message8);
				$("#dbName").val("");
				$("#dbServer").val("");
				$("#url").val("");
				$("#id").val("");
				$("#pw").val("");
				$("#timeout").val("");
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message9);
			}
		}
	});
}

function onSQLAddProc() {
	var sqlName = $("#sqlName").val();
	var sql = $("#sql").val();

	if (sqlName == null || sqlName == "") {
		alert(lang.admin.dbManage.alert.message10);
		$('#sqlName').focus();
		return;
	}

	if(sql == null || sql == "") {
		alert(lang.admin.dbManage.alert.message11);
		$('#sql').focus();
		return;
	}
	
	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(sqlName == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.dbManage.alert.message12);
				$('#sqlName').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;
	var dataStr = {
			"sqlName"			: sqlName
	    ,	"sql"				: sql
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/sqlManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message13);
				$("#sqlName").val("");
				$("#sql").val("");
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message14);
			}
		}
	});
}

function onJobAddProc() {
	var jobName = $("#jobName").val();
	
	var dbName = new Array();
	for(var i = 0 ; i < $("#dbName option").length ; i++){
		dbName.push($("#dbName option")[i].text);
	}
	var sqlName = new Array();
	for(var i = 0 ; i < $("#sqlName option").length ; i++){
		sqlName.push($("#sqlName option")[i].text);
	}
	if (jobName == null || jobName == "") {
		alert(lang.admin.dbManage.alert.message15);
		$('#jobName').focus();
		return;
	}
	if ($("#dbName option").length == 0 && $("#sqlName option").length == 0) {
		alert(lang.admin.dbManage.alert.message16);
		$('#dbNameSelect').focus();
		return;
	}
	
	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(jobName == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.dbManage.alert.message17)
				$('#jobName').focus();
				forEachResult = false;
			} 
		});
	}
	
	if(!forEachResult)
		return;
	var dataStr = {
				"jobName"			: jobName
			,	"dbName"			: JSON.stringify(dbName)
			,	"sqlName"			: JSON.stringify(sqlName)
			,	"proc"              : "insert"
	};
	
	$.ajax({
		url:contextPath+"/jobManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message18);
				$("#jobName").val("");
				$("#dbName").empty();
				$("#sqlName").empty();
				$("#dbNameSelect").val("");
				$("#sqlNameSelect").val("");
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message19);
			}
		}
	});
}

function onExecuteProc() {
	var executeName = $("#executeName").val();
	var jobName = $("#jobName").val();
	var rSchedulerSelect = $("#rSchedulerSelect").val();
	var rSchedulerWeek = $("#rSchedulerWeek").val();
	var rSchedulerDay = $("#rSchedulerDay").val();
	var rSchedulerHour = $("#rSchedulerHour").val();
	
	if (executeName == null || executeName == "") {
		alert(lang.admin.dbManage.alert.message20);
		$('#executeName').focus();
		return;
	}
	if (jobName == null || jobName == "") {
		alert(lang.admin.dbManage.alert.message15);
		$('#jobName').focus();
		return;
	}
	if (rSchedulerSelect == null || rSchedulerSelect == "") {
		alert(lang.admin.dbManage.alert.message21);
		$('#rSchedulerSelect').focus();
		return;
	}
	if (rSchedulerSelect == "w" && rSchedulerWeek=="*") {
		alert(lang.admin.dbManage.alert.message22);
		$('#rSchedulerWeek').focus();
		return;
	}
	if (rSchedulerSelect == "m" && rSchedulerDay=="*") {
		alert(lang.admin.dbManage.alert.message23);
		$('#rSchedulerDay').focus();
		return;
	}
	if(rSchedulerHour == "*"){
		alert(lang.admin.dbManage.alert.message24);
		$('#rSchedulerHour').focus();
		return;
	}
	
	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(executeName == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.dbManage.alert.message25);
				$('#executeName').focus();
				forEachResult = false;
			} 
		});
	}
	
	if(!forEachResult)
		return;
	var dataStr = {
				"executeName"		: executeName
			,	"jobName"			: jobName
			,	"rSchedulerSelect"	: rSchedulerSelect
			,	"rSchedulerWeek"	: rSchedulerWeek
			,	"rSchedulerDay"		: rSchedulerDay
			,	"rSchedulerHour"	: rSchedulerHour
			,	"proc"              : "insert"
	};
	
	$.ajax({
		url:contextPath+"/executeManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message26);
				$("#executeName").val("");
				$("#jobName").val("");
				$("#rSchedulerSelect").val("d");
				$("#rSchedulerWeek").val("*");
				$("#rSchedulerDay").val("*");
				$("#rSchedulerHour").val("*");
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message27);
			}
		}
	});
}

function onDBModifyProc() {
	var dbName = $("#dbNamePop").val();
	var dbServer = $("#dbServerPop").val();
	var url = $("#urlPop").val();
	var id = $("#idPop").val();
	var pw = $("#pwPop").val();
	var timeout = $("#timeoutPop").val();

	if (dbName == null || dbName == "") {
		alert(admin.dbManage.alert.message1);
		$('#dbNamePop').focus();
		return;
	}

	if(dbServer == null || dbServer == "") {
		alert(admin.dbManage.alert.message2);
		$('#dbServerPop').focus();
		return;
	}
	
	if(url == null || url == "") {
		alert(admin.dbManage.alert.message3);
		$('#urlPop').focus();
		return;
	}
	
	if(id == null || id == "") {
		alert(admin.dbManage.alert.message4);
		$('#idPop').focus();
		return;
	}
	
	if(pw == null || pw == "") {
		alert(admin.dbManage.alert.message5);
		$('#pwPop').focus();
		return;
	}
	
	if(timeout == null || timeout == "") {
		alert(admin.dbManage.alert.message6);
		$('#timeoutPop').focus();
		return;
	}
	
	var forEachResult =true
	
	if(!forEachResult)
		return;
	
	var dataStr = {
				"dbName"			: dbName
		    ,   "dbServer"			: dbServer
		    ,	"url"				: url
		    ,	"id"        	    : id
		    ,	"pw"        		: pw
		    ,	"timeout"      		: timeout
			,	"proc"              : "modify"
	};
	
	$.ajax({
		url:contextPath+"/dbManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message28);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message29);
			}
		}
	});
}

function onSQLModifyProc() {
	var sqlName = $("#sqlNamePop").val();
	var sql = $("#sqlPop").val();
	
	if (sqlName == null || sqlName == "") {
		alert(lang.admin.dbManage.alert.message10);
		$('#sqlNamePop').focus();
		return;
	}
	
	if(sql == null || sql == "") {
		alert(lang.admin.dbManage.alert.message11);
		$('#sqlPop').focus();
		return;
	}
	
	var forEachResult =true
	
	if(!forEachResult)
		return;
	
	var dataStr = {
				"sqlName"			: sqlName
			,	"sql"				: sql
			,	"proc"              : "modify"
	};
	
	$.ajax({
		url:contextPath+"/sqlManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message30);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message31)
			}
		}
	});
}

function onJobModifyProc() {
	var jobName = $("#jobNamePop").val();
	
	var dbName = new Array();
	for(var i = 0 ; i < $("#dbNamePop option").length ; i++){
		dbName.push($("#dbNamePop option")[i].text);
	}
	var sqlName = new Array();
	for(var i = 0 ; i < $("#sqlNamePop option").length ; i++){
		sqlName.push($("#sqlNamePop option")[i].text);
	}
	if (jobName == null || jobName == "") {
		alert(lang.admin.dbManage.alert.message15);
		$('#jobName').focus();
		return;
	}
	if ($("#dbNamePop option").length == 0 && $("#sqlNamePop option").length == 0) {
		alert(lang.admin.dbManage.alert.message16);
		$('#dbNameSelectPop').focus();
		return;
	}
	
	var forEachResult =true
	
	if(!forEachResult)
		return;
	
	var dataStr = {
				"jobName"			: jobName
			,	"dbName"			: JSON.stringify(dbName)
			,	"sqlName"			: JSON.stringify(sqlName)
			,	"proc"              : "modify"
	};
	
	$.ajax({
		url:contextPath+"/jobManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message32);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message33);
			}
		}
	});
}

function onExecuteModifyProc() {
	var executeName = $("#executeNamePop").val();
	var jobName = $("#jobNamePop").val();
	var rSchedulerSelect = $("#rSchedulerSelectPop").val();
	var rSchedulerWeek = $("#rSchedulerWeekPop").val();
	var rSchedulerDay = $("#rSchedulerDayPop").val();
	var rSchedulerHour = $("#rSchedulerHourPop").val();
	
	if (executeName == null || executeName == "") {
		alert(lang.admin.dbManage.alert.message20);
		$('#executeNamePop').focus();
		return;
	}
	if (jobName == null || jobName == "") {
		alert(lang.admin.dbManage.alert.message15);
		$('#jobNamePop').focus();
		return;
	}
	if (rSchedulerSelect == null || rSchedulerSelect == "") {
		alert(lang.admin.dbManage.alert.message21);
		$('#rSchedulerSelectPop').focus();
		return;
	}
	if (rSchedulerSelect == "w" && rSchedulerWeek=="*") {
		alert(lang.admin.dbManage.alert.message22);
		$('#rSchedulerWeekPop').focus();
		return;
	}
	if (rSchedulerSelect == "m" && rSchedulerDay=="*") {
		alert(lang.admin.dbManage.alert.message23);
		$('#rSchedulerDayPop').focus();
		return;
	}
	if(lang.admin.dbManage.alert.message24){
		alert("null rSchedulerHour"/*lang.admin.alert.subNumberManage1*/);
		$('#rSchedulerHourPop').focus();
		return;
	}
	
	var forEachResult =true
	
	if(!forEachResult)
		return;
	
	var dataStr = {
			"executeName"		: executeName
		,	"jobName"			: jobName
		,	"rSchedulerSelect"	: rSchedulerSelect
		,	"rSchedulerWeek"	: rSchedulerWeek
		,	"rSchedulerDay"		: rSchedulerDay
		,	"rSchedulerHour"	: rSchedulerHour
		,	"proc"              : "modify"
	};
	
	$.ajax({
		url:contextPath+"/executeManage_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message34);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/dbManage_list.xml?tab="+onTab(), function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.dbManage.alert.message35);
			}
		}
	});
}

//DB 삭제
function onDeleteProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var tab = $("#tab").val();
		var tabPath ="";
		if(tab=="" || tab=="db"){
			if(confirm(lang.admin.dbManage.confirm.message1)){
				tabPath = "/dbManage_proc.do";
			}else{
				return
			}
		}else if(tab=="query"){
			if(confirm(lang.admin.dbManage.confirm.message2)){
				tabPath = "/sqlManage_proc.do";
			}else{
				return
			}
		}else if(tab=="job"){
			if(confirm(lang.admin.dbManage.confirm.message3)){
				tabPath = "/jobManage_proc.do";
			}else{
				return
			}
		}else if(tab=='execute'){
			if(confirm(lang.admin.dbManage.confirm.message4)){
				tabPath = "/executeManage_proc.do";
			}else{
				return
			}
		}else{
			return
		}
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstIdx = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstIdx.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
		}
		var rst = rstIdx.join(",");
			$.ajax({
				url:contextPath+tabPath,
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.dbManage.alert.message36);
						objGrid.clearAndLoad(contextPath + "/dbManage_list.xml?tab="+onTab(), function(){ 
						}, "xml");
					} else {
						alert(lang.admin.dbManage.alert.message37);
					}
				}
			});
	} else {
		alert (lang.admin.dbManage.alert.message38);
	}
}

function onTab(){
	var tab = "";
	if($("#tab").val()==""){
		tab = "db";
	}else{
		tab = $("#tab").val();
	}
	return encodeURI(tab);
}

function onOptionFunction() {
	for(var i=1;i<32;i++){
		$("#rSchedulerDay").append("<option value="+i+">"+i+"</option");
		$("#rSchedulerDayPop").append("<option value="+i+">"+i+"</option");
	}
	for(var i=1;i<24;i++){
		$("#rSchedulerHour").append("<option value="+i+">"+i+"</option");	
		$("#rSchedulerHourPop").append("<option value="+i+">"+i+"</option");	
	} 
}

function onDisplayProc() {
	if($("#rSchedulerSelect").val()=="d"){
		$('.week').css("display","none");
		$('#rSchedulerWeek').val("*");
		
		$(".day").css("display","none");
		$('#rSchedulerDay').val("*");
		
		$('#rSchedulerHour').val("*");
	}else if($("#rSchedulerSelect").val()=="w"){	
		$('.week').css("display","inline");
		$('#rSchedulerWeek').val("*");
		
		$(".day").css("display","none");
		$('#rSchedulerDay').val("*");
		
		$('#rSchedulerHour').val("*");
	}else{
		$('.week').css("display","none");
		$('#rSchedulerWeek').val("*");
		
		$(".day").css("display","inline");
		$('#rSchedulerDay').val("*");
		
		$('#rSchedulerHour').val("*");
	}
}

function onPopDisplayProc() {
	if($("#rSchedulerSelectPop").val()=="d"){
		$('.weekPop').css("display","none");
		$('#rSchedulerWeekPop').val("*");
		
		$(".dayPop").css("display","none");
		$('#rSchedulerDayPop').val("*");
		
		$('#rSchedulerHourPop').val("*");
	}else if($("#rSchedulerSelectPop").val()=="w"){	
		$('.weekPop').css("display","inline");
		$('#rSchedulerWeekPop').val("*");
		
		$(".dayPop").css("display","none");
		$('#rSchedulerDayPop').val("*");
		
		$('#rSchedulerHourPop').val("*");
	}else{
		$('.weekPop').css("display","none");
		$('#rSchedulerWeekPop').val("*");
		
		$(".dayPop").css("display","inline");
		$('#rSchedulerDayPop').val("*");
		
		$('#rSchedulerHourPop').val("*");
	}
}

// DB 연결 테스트
function dbConnectionTest(i) {
	$.ajax({
		url : contextPath+"/dbConnectionTestProc",
		data : {
			dbName : objGrid.cells(i,1).getValue()
		},
		type:"POST",
		dataType:"json",
		success : function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message39);
			} else {
				alert(lang.admin.dbManage.alert.message40);
			}
		}
	});
}

function onAddListProc() {
	var dbNameSelect = $("#dbNameSelect").val();
	var sqlNameSelect = $("#sqlNameSelect").val();
	if(dbNameSelect==null || dbNameSelect==""){
		alert(lang.admin.dbManage.alert.message41);
	}else if(sqlNameSelect==null || sqlNameSelect==""){
		alert(lang.admin.dbManage.alert.message42);
	}else{
		$("#dbName").append('<option value="'+dbNameSelect+'">'+dbNameSelect+'</option>');
		$("#sqlName").append('<option value="'+sqlNameSelect+'">'+sqlNameSelect+'</option>');
	}
}
function onDelListProc() {
	var dbName = $("#dbName").val();
	var sqlName = $("#sqlName").val();
	if(dbName==null && sqlName==null){
		alert(lang.admin.dbManage.alert.message43);
	}else{
		$("#dbName option:selected[value='"+dbName+"']").remove();
		$("#sqlName option:selected[value='"+sqlName+"']").remove();
	}
}

function onAddListProcPop() {
	var dbNameSelect = $("#dbNameSelectPop").val();
	var sqlNameSelect = $("#sqlNameSelectPop").val();
	if(dbNameSelect==null || dbNameSelect==""){
		alert(lang.admin.dbManage.alert.message41);
	}else if(sqlNameSelect==null || sqlNameSelect==""){
		alert(lang.admin.dbManage.alert.message42);
	}else{
		$("#dbNamePop").append('<option value="'+dbNameSelect+'">'+dbNameSelect+'</option>');
		$("#sqlNamePop").append('<option value="'+sqlNameSelect+'">'+sqlNameSelect+'</option>');
	}
}

function onDelListProcPop() {
	var dbName = $("#dbNamePop").val();
	var sqlName = $("#sqlNamePop").val();
	if(dbName==null && sqlName==null){
		alert(lang.admin.dbManage.alert.message43);
	}else{
		$("#dbNamePop option:selected[value='"+dbName+"']").remove();
		$("#sqlNamePop option:selected[value='"+sqlName+"']").remove();
	}
}

function excuteQuery(i){
	
	$.ajax({
		url : contextPath+"/excuteQuery",
		data : {
			executeName : objGrid.cells(i,1).getValue()
		},
		type:"POST",
		dataType:"json",
		success : function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.dbManage.alert.message44);
				objGrid.clearAndLoad(contextPath + "/dbManage_list.xml?tab="+onTab(), function(){ 
				}, "xml");
			} else {
				alert(lang.admin.dbManage.alert.message45);
			}
		}
	});
	
}