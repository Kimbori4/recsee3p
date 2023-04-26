// 전역변수 설정
var gridMobileManage; // 그리드
var gridMobileManageFileList; // 그리드
var mobile = "", mobileRes = "";
var mobileAddJSON = "";
var requestInfo = "";

addLoadEvent(mobileManageLoad);

function mobileManageLoad() {
	getMobileManageInfo();
	gridMobileManage = mobileManageGridLoad();
	
	// 모바일 녹취파일 보관기간 설정
	$("#storagePeriodBtn").click(function(){
		//$("#addSubNumber").find("p").text(lang.admin.subNumber.title.addSubNumber)
		layer_popup('#storagePeriodPop');
	});
	
	// 모바일 녹취파일 보관기간 설정
	$("#requestInfoBtn").click(function(){
		layer_popup('#requestInfoPop');
	});
	
	$("#setStoragePeriodBtn").click(function(){
		var storagePeriodValue = $("#storagePeriodValue").val();
		$.ajax({
			url : requestInfo + "/", //"지수매니저님이 알려주는 주소"
			type : "POST",
			data : { storagePeriodValue : storagePeriodValue },
			dataType : "JSON",
			success : function(result){
				$.ajax({
					url : contextPath + "/setStoragePeriod",
					type : "POST",
					data : { storagePeriodValue : storagePeriodValue },
					dataType : "JSON",
					success : function(result){
						var msg = jRes.resData.msg;
						if (jRes.success == "Y") {
							alert("보관기간을 설정했습니다.");
						} else {
							alert("보관기간 설정에 실패했습니다.");
						}
					}
				});
			}
		});
		layer_popup_close('#storagePeriodPop');
	});
	
	$("#setRequestInfoBtn").click(function(){
		var requestIP = $("#requestIPValue").val();
		var requestPort = $("#requestPortValue").val();
		$.ajax({
			url : contextPath + "/setRequestInfo",
			type : "POST",
			data : { 
				requestIP : requestIP,
				requestPort : requestPort
			},
			dataType : "JSON",
			success : function(result){
				var msg = jRes.resData.msg;
				if (jRes.success == "Y") {
					alert("요청 정보를 설정했습니다.");
				} else {
					alert("요청 정보 설정에 실패했습니다.");
				}
			}
		});
		layer_popup_close('#requestInfoPop');
	});
	
	
	$("#reqLogLevelBtn").click(function(){
		var userId = $("#rllUserId").val();
		var userPhone = $("#rllUserPhone").val();
		var logLevel = $("#logLevelList").val();
		$.ajax({
			url : requestInfo + "/", //"지수매니저님이 알려주는 주소"
			type : "POST",
			data : {
				userId : userId,
				userPhone : userPhone,
				logLevel : logLevel
			},
			dataType : "JSON",
			success : function(result){
				alert(result);
			}
		});
		
		layer_popup_close('#logLevelPop');
	});
	
	$("#reqFileUploadBtn").click(function(){
		var userId = $("#rfuUserId").val();
		var userPhone = $("#rfuUserPhone").val();
		var checked = gridMobileManageFileList.getCheckedRows(0).split(",");
		var rstIdx = new Array();
		for(var i = 0 ; i < checked.length ; i++){
			rstIdx.push({"recDateTime":gridMobileManageFileList.cells2(parseInt(checked[i])-1,1).getValue()+gridMobileManageFileList.cells2(parseInt(checked[i])-1,2).getValue(),
					"custPhone":gridMobileManageFileList.cells2(parseInt(checked[i])-1,3).getValue(),"userId":userId,"userPhone":userPhone});
		}
		
		$.ajax({
			url : requestInfo + "/", //"지수매니저님이 알려주는 주소"
			type : "POST",
			data : rstIdx,
			dataType : "JSON",
			success : function(result){
				alert(result);
			}
		});
		layer_popup_close('#fileUploadPop');
	});
	
	authyLoad();
	
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		//$('#storagePeriodBtn').hide();
	}

	if(delYn != 'Y') {
		//$('#subNumberDel').hide();
	}
}
// 모바일 관리 로드
function mobileManageGridLoad() {
    // mobile관리 Grid
	var objGrid = new dhtmlXGridObject("gridMobileManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingMobileManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/mobile_list.xml", function(){
		
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
		
	});

    ui_controller();
    
    return objGrid;
}

function reqLogFile(idx){
	var userId = gridMobileManage.cells(idx,1).getValue();
	var userPhone = gridMobileManage.cells(idx,3).getValue();
	$.ajax({
		url : requestInfo + "/", //"지수매니저님이 알려주는 주소"		type : "POST",
		data : {
			userId : userId,
			userPhone : userPhone
		},
		dataType : "JSON",
		success : function(result){
			alert(result);
		}
	});
}
function reqLogLevel(idx){
	$("#logLevelList").empty();
	$("#logLevelList").append("<option value='debug' selected>debug</option>" +
							  "<option value='info'>info</option>" +
							  "<option value='error'>error</option>" +
							  "<option value='warn'>warn</option>");
	$("#rllUserId").val(gridMobileManage.cells(idx,1).getValue());
	$("#rllUserPhone").val(gridMobileManage.cells(idx,3).getValue());
	
	layer_popup("#logLevelPop");
}

function reqFileUpload(idx){
	$("#rfuUserId").val(gridMobileManage.cells(idx,1).getValue());
	$("#rfuUserPhone").val(gridMobileManage.cells(idx,3).getValue());
	mobileManageFileListGridLoad(idx);
		
	layer_popup("#fileUploadPop");
}

//모바일 관리 파일리스트 로드
function mobileManageFileListGridLoad(idx) {
    // mobile관리 파일리스트 Grid
	gridMobileManageFileList = new dhtmlXGridObject("gridMobileManageFileList");
	gridMobileManageFileList.setIconsPath(recseeResourcePath + "/images/project/");
	gridMobileManageFileList.setImagePath(recseeResourcePath + "/images/project/");
	gridMobileManageFileList.i18n.paging = i18nPaging[locale];
	gridMobileManageFileList.enablePaging(true, 30, 5, "pagingMobileManageFileList", true);
    gridMobileManageFileList.setPagingWTMode(true,true,true,[30,60,90,100]);
	gridMobileManageFileList.enablePreRendering(50);
    gridMobileManageFileList.setPagingSkin("toolbar", "dhx_web");
	gridMobileManageFileList.enableColumnAutoSize(false);
	gridMobileManageFileList.enablePreRendering(50);
    gridMobileManageFileList.setSkin("dhx_web");
	gridMobileManageFileList.init();
	gridMobileManageFileList.load(contextPath+"/mobileFileUpload_list.xml?rUserId="+gridMobileManage.cells(idx,1).getValue(), function(){
		
		var search_toolbar2 = gridMobileManageFileList.aToolBar;
		
		search_toolbar2.addSpacer("perpagenum");
		search_toolbar2.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridMobileManageFileList.i18n.paging.results+" "+gridMobileManageFileList.getRowsNum() + '</div>')
		search_toolbar2.setWidth("total",150)

		$(window).resize();
		gridMobileManageFileList.setSizes();
		$(window).resize(function() {
			gridMobileManageFileList.setSizes();
		});
		
	}, 'xml')
	
	gridMobileManageFileList.attachEvent("onXLS", function(){
		progress.on()
	});
	
	gridMobileManageFileList.attachEvent("onXLE", function(grid_obj,count){
		
		$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
		
		if (gridMobileManageFileList.getRowsNum() > 0){
			try{
				var setResult = '<div style="width: 100%; text-align: center;">'+ gridMobileManageFileList.i18n.paging.results+ gridMobileManageFileList.getRowsNum()+gridMobileManageFileList.i18n.paging.found+'</div>'
				gridMobileManageFileList.aToolBar.setItemText("total", setResult)
			}catch(e){tryCatch(e)}
		}
		
		ui_controller();
		progress.off();

		$(window).resize();
		$(window).resize(function() {
			gridMobileManageFileList.setSizes();
			$(top.window).trigger("resize");
		});
	});

	// 체크박스 전체 선택
	gridMobileManageFileList.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				gridMobileManageFileList.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				gridMobileManageFileList.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	gridMobileManageFileList.attachEvent("onRowSelect", function(id,ind){
	    return;
	});

	gridMobileManageFileList.attachEvent("onRowDblClicked", function(id,ind){
		
	});

    ui_controller();
    
}

function getMobileManageInfo(){
	$.ajax({
		url : contextPath+"/getMobileManageSetting",
		type : "GET",
		dataType : "JSON",
		success : function(jRes){
			var msg = jRes.resData.msg;
			$("#storagePeriodLabel").text(jRes.resData.storagePeriod+"일"/*언어팩*/);
			$("#requestIPLabel").text(jRes.resData.requestIP);
			$("#requestPortLabel").text(jRes.resData.requestPort);
			
			requestInfo = "http://" + jRes.resData.requestIP + ":" + jRes.resData.requestPort;
			
			if (msg != undefined) { 
				if (msg.indexOf("storagePeriod") != -1) {
					$("#storagePeriodLabel").text("no data");
				}
				if (msg.indexOf("requestIP") != -1) {
					$("#requestIPLabel").text("no data");
				}
				if (msg.indexOf("requestPort") != -1) {
					$("#requestPortLabel").text("no data");
				}
			}
		}
	});
}