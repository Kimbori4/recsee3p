// 전역변수 설정
var gridDBInfo; // 그리드
var dbInfo = "", dbInfoRes = "";
var dbInfoAddJSON = "";
var winObj;

addLoadEvent(gridDBInfoLoad);

function gridDBInfoLoad() {
	gridDBInfo = gridDBInfoGridLoad();
	
	// 메인페이지 DB 추가 버튼
	$("#addDBBtn").click(function(){
		$("#addDB").find("p").text("Add DB"/*lang.admin.subNumber.title.addSubNumber*/);

		$("#dbName").val("");
		$("#dbDriver").val("");
		$("#dbUrl").val("");
		$("#dbUser").val("");
		$("#dbPassword").val("");

		$("#dbAdd").show();
		$("#dbModify").hide();

		layer_popup('#addDB');
	});
	
	// 메인페이지 DB 수정 버튼
	$("#modifyDBBtn").click(function(){
		
		if(onModifyCheckBox()==1){
			var checked = objGrid.getCheckedRows(0).split(",");
			
			$("#addDB").find("p").text("Modify DB"/*lang.admin.subNumber.title.addSubNumber*/);
			
			var dbName = objGrid.cells2(parseInt(checked[0])-1,1).getValue();
			var dbDriver = objGrid.cells2(parseInt(checked[0])-1,2).getValue();
			var dbUrl = objGrid.cells2(parseInt(checked[0])-1,3).getValue();
			var dbUser = objGrid.cells2(parseInt(checked[0])-1,4).getValue();
			var dbPassword = objGrid.cells2(parseInt(checked[0])-1,5).getValue();

			$("#dbName").val(dbName);
			$("#dbName").prop("readonly", true);
			$("#dbName").css("background", "#d2d2d2")
			$("#dbDriver").val(dbDriver);
			$("#dbUrl").val(dbUrl);
			$("#dbUser").val(dbUser);
			$("#dbPassword").val(dbPassword);
			
			$("#dbAdd").hide();
			$("#dbModify").show();
			
			layer_popup('#addDB');
		}
		
	});
	
	// 쿼리 실행 팝업 버튼
	$("#excuteQueryBtn").click(function(){
		// 쿼리 리스트 가져오기(select, upsert 각각)
		onQueryList();
		
		$("#excuteQuery").find("p").text("Excute Query"/*lang.admin.subNumber.title.addSubNumber*/);
		
		$("#selectQuery").val("");
		$("#upsertQuery").val("");
		
		$("#excuteBtn").show();

		layer_popup('#excuteQuery');
	});

	// 팝업창 DB 추가
	$('#dbAdd').click(function(){
		onDBAddProc();
	});

	// 팝업창 DB 수정
	$('#dbModify').click(function(){
		onDBModifyProc();
	});

	// DB 삭제
	$('#deleteDBBtn').click(function() {
		onDBDeleteProc();
	});
	
	// 쿼리 실행
	$("#excuteBtn").click(function(){
		onExcuteQueryProc();
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
// 자번호관리 로드
function gridDBInfoGridLoad() {
    // 자번호관리 Grid
	objGrid = new dhtmlXGridObject("gridDBInfo");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingDBInfo", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/dbInfo_list.xml", function(){
		
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

	objGrid.attachEvent("onRowDblClicked", function(id,ind){
		var dbName = this.cells(id, 1).getValue();
		/*var dbDriver = this.cells(id, 2).getValue();
		var dbUrl = this.cells(id, 3).getValue();
		var dbUser = this.cells(id, 4).getValue();
		var dbPassword = this.cells(id, 5).getValue();*/
		onDBInfoPopup(dbName);
	});

    ui_controller();
}

// DB 수정 체크박스 적정성
function onModifyCheckBox() {
	
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		if(checked.length > 1){
			alert ("check : over");
		}else {
			return 1; 
		}
	}else {
		alert ("check : none");
	}
	
}

function onDBAddProc() {
	var dbName = $("#dbName").val();
	var dbDriver = $("#dbDriver").val();
	var dbUrl = $("#dbUrl").val();
	var dbUser = $("#dbUser").val();
	var dbPassword = $("#dbPassword").val();

	if (dbName == null || dbName == "") {
		alert("null dbName"/*lang.admin.alert.subNumberManage1*/);
		$('#dbName').focus();
		return;
	}

	if(dbDriver == null || dbDriver == "") {
		alert("null dbDriver");
		$('#dbDriver').focus();
		return;
	}
	
	if(dbUrl == null || dbUrl == "") {
		alert("null dbUrl");
		$('#dbUrl').focus();
		return;
	}
	
	if(dbUser == null || dbUser == "") {
		alert("null dbUser");
		$('#dbUser').focus();
		return;
	}
	
	if(dbPassword == null || dbPassword == "") {
		alert("null dbPassword");
		$('#dbPassword').focus();
		return;
	}

	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(dbName == objGrid.cells(id,1).getValue()) {
				alert("already"/*lang.admin.alert.subNumberManage5*/)
				$('#dbName').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"dbName"			: dbName
	    ,   "dbDriver"			: dbDriver
	    ,	"dbUrl"				: dbUrl
	    ,	"dbUser"            : dbUser
	    ,	"dbPassword"        : dbPassword
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/dbInfo_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert("success"/*lang.admin.alert.subNumberManage6*/);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/dbInfo_list.xml", function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert("error")
			}
		}
	});
}

function onDBModifyProc() {
	var dbName = $("#dbName").val();
	var dbDriver = $("#dbDriver").val();
	var dbUrl = $("#dbUrl").val();
	var dbUser = $("#dbUser").val();
	var dbPassword = $("#dbPassword").val();
	
	if (dbName == null || dbName == "") {
		alert("null dbName"/*lang.admin.alert.subNumberManage1*/);
		$('#dbName').focus();
		return;
	}
	
	if(dbDriver == null || dbDriver == "") {
		alert("null dbDriver");
		$('#dbDriver').focus();
		return;
	}
	
	if(dbUrl == null || dbUrl == "") {
		alert("null dbUrl");
		$('#dbUrl').focus();
		return;
	}
	
	if(dbUser == null || dbUser == "") {
		alert("null dbUser");
		$('#dbUser').focus();
		return;
	}
	
	if(dbPassword == null || dbPassword == "") {
		alert("null dbPassword");
		$('#dbPassword').focus();
		return;
	}
	
	var forEachResult =true
	// 중복 처리
	/*if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(dbName == objGrid.cells(id,1).getValue()) {
				alert("already"lang.admin.alert.subNumberManage5)
				$('#dbName').focus();
				forEachResult = false;
			} 
		});
	}*/
	
	if(!forEachResult)
		return;
	
	var dataStr = {
				"dbName"			: dbName
			,   "dbDriver"			: dbDriver
			,	"dbUrl"				: dbUrl
			,	"dbUser"            : dbUser
			,	"dbPassword"        : dbPassword
			,	"proc"              : "modify"
	};
	
	$.ajax({
		url:contextPath+"/dbInfo_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert("success"/*lang.admin.alert.subNumberManage6*/);
				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/dbInfo_list.xml", function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert("error")
			}
		}
	});
}

//DB 삭제
function onDBDeleteProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstIdx = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstIdx.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
		}
		console.log(rstIdx)
		var rst = rstIdx.join(",");
		if (confirm(lang.admin.alert.subNumberManage8)){
			$.ajax({
				url:contextPath+"/dbInfo_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert("success"/*lang.admin.alert.subNumberManage9*/)
						objGrid.clearAndLoad(contextPath + "/dbInfo_list.xml", function(){ 
						}, "xml");
					} else {
						alert("error"/*lang.admin.alert.subNumberManage10*/)
					}
				}
			});
		}
	} else {
		alert ("check : none"/*lang.admin.alert.subNumberManage11*/)
	}
}

function onDBInfoPopup(dbName){
	var popHeight = "600";
	
	var windowOpenUrlInfo = contextPath+"/dbInfoPopup?dbName="+dbName;
	
	if(winObj == null){ //window.open 중복 생성 방지 및 기존 열린곳으로 포커스 주기
		winObj = window.open(windowOpenUrlInfo,"dbInfo",'height=' + popHeight + ',width=' + (screen.width-50) + ',fullscreen=yes');
	}else{
		if(winObj.closed == true){ // 기존 팝업이 꺼져있으면 새로 열어줌
			winObj = window.open(windowOpenUrlInfo,"dbInfo",'height=' + popHeight + ',width=' + (screen.width-50) + ',fullscreen=yes');
		}else{
			if(confirm("already")){
				winObj = window.open(windowOpenUrlInfo,"dbInfo",'height=' + popHeight + ',width=' + (screen.width-50) + ',fullscreen=yes');

			}else{
				winObj.focus();
			}
		}
	}
}

$(function(){
	//쿼리 선택
	$("#selectQuery").change(function(){
		var dbSQLName = $("#selectQuery").val();
		$.ajax({
			url : contextPath + "/queryProc",
			data : {
				dbSQLName : dbSQLName,
				proc : "select"
			},
			success : function(result){
				$("#selectQueryContent").val(JSON.parse(result).dbSQLContent)
				$("#selectQueryDescription").val(JSON.parse(result).dbSQLDescription)
			}
		});
	});
	$("#upsertQuery").change(function(){
		var dbSQLName = $("#upsertQuery").val();
		$.ajax({
			url : contextPath + "/queryProc",
			data : {
				dbSQLName : dbSQLName,
				proc : "upsert"
			},
			success : function(result){
				$("#upsertQueryContent").val(JSON.parse(result).dbSQLContent)
				$("#upsertQueryDescription").val(JSON.parse(result).dbSQLDescription)
			}
		});
	});
});

function onExcuteQueryProc() {
	var selectQueryName = $("#selectQuery").val();
	var upsertQueryName = $("#upsertQuery").val();
	$.ajax({
		url : contextPath + "/excuteQueryProc",
		data : {
			selectQueryName : selectQueryName,
			upsertQueryName : upsertQueryName
		},
		success : function(result) {
			layer_popup_close();
		}
	});
}

function onQueryList() {
	$("#selectQuery").empty();
	$("#upsertQuery").empty();
	$.ajax({
		url : contextPath + "/queryListProc",
		success : function(result){
			$(JSON.parse(result).select).each(function(index, sql){
				$("#selectQuery").append('<option value="'+sql.dbSQLName+'">'+sql.dbSQLName+'</option>');
			});
			$(JSON.parse(result).upsert).each(function(index, sql){
				$("#upsertQuery").append('<option value="'+sql.dbSQLName+'">'+sql.dbSQLName+'</option>');
			});
		}
	});
}
