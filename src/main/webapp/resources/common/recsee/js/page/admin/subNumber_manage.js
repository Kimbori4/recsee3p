// 전역변수 설정
var gridSubNumberManage; // 그리드
var subNumber = "", subNumRes = "";
var subNumAddJSON = "";

addLoadEvent(subNumberManageLoad);

function subNumberManageLoad() {
	gridSubNumberManage = subNumberManageGridLoad();
	$("#use").append("<option value='Y' selected>"+ lang.admin.subNumber.label.selY + "</option>")
	$("#use").append("<option value='N'>"+ lang.admin.subNumber.label.selN + "</option>")
	// 메인페이지 자번호 추가 버튼
	$("#subNumberAddBtn").click(function(){
		$("#addSubNumber").find("p").text(lang.admin.subNumber.title.addSubNumber)

		$("#telNo").val("");
		$("#nickName").val("");
		$("#use").val("Y");

		$("#subNumberAdd").show();
		$("#subNumberModify").hide();

		layer_popup('#addSubNumber');
	});

	// 팝업창 자번호 추가
	$('#subNumberAdd').click(function(){
		onSubNumberAddProc();
	});

	// 팝업창 자번호 수정
	$('#subNumberModify').click(function(){
		onSubNumberModifyProc();
	});

	// 자번호 삭제
	$('#subNumberDel').click(function() {
		onSubNumberDelProc()
	})

	authyLoad();
	
	$("#use").hide();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		$('#subNumberAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#subNumberDel').hide();
	}
}
// 자번호관리 로드
function subNumberManageGridLoad() {
    // 자번호관리 Grid
	objGrid = new dhtmlXGridObject("gridSubNumberManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingSubNumberManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/subNumber_list.xml", function(){
		
		var search_toolbar = objGrid.aToolBar;
		
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum() + '</div>')
		search_toolbar.setWidth("total",150)

		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		$(objGrid.getFilterElement(3)).children().remove()
		$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
		$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.subNumber.label.selY + "</option>")
		$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.subNumber.label.selN + "</option>")
		
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
		$("#addSubNumber").find("p").text(lang.admin.subNumber.title.addSubNumber)

		var telNo =  this.cells(id, 1).getValue();
		var	nickName = this.cells(id, 2).getValue();
		var use = this.cells(id, 3).getValue();
		var idx = this.cells(id, 4).getValue();

		$("#telNo").val(telNo)
		$("#nickName").val(nickName);
		$("#use").val(use);
		$("#idx").val(idx);

		$("#subNumberAdd").hide();
		$("#subNumberModify").show();

		layer_popup('#addSubNumber');
	});

    ui_controller();
}


// 자번호 추가, 기존 로직 그대로 사용
function onSubNumberAddProc(){

	var telNo = $('#telNo').val();
	var nickName =  $('#nickName').val();
	var use =  $('#use').val();

	if (telNo == null || telNo == "") {
		alert(lang.admin.alert.subNumberManage1);
		$('#telNo').focus();
		return;
	} else if(!/^-?\d*$/.test(telNo)) {
		alert(lang.admin.alert.subNumberManage2);
		$('#telNo').focus();
		return;
	}

	if(nickName == null || nickName == "") {
		alert(lang.admin.alert.subNumberManage3);
		$('#nickName').focus();
		return;
	}

	if(use == null || use == "") {
		alert(lang.admin.alert.subNumberManage4);
		$('#use').focus();
		return;
	}

	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(telNo == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.subNumberManage5)
				$('#telNo').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"telNo"				: telNo
	    ,   "nickName"			: nickName
	    ,	"use"				: use
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/subNumber_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.subNumberManage6);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/subNumber_list.xml", function(){
					$(objGrid.getFilterElement(3)).children().remove()
					$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
					$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.subNumber.label.selY + "</option>")
					$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.subNumber.label.selN + "</option>")
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.alert.subNumberManage7)
			}
		}
	});
};

function onSubNumberModifyProc(){

	var telNo = $('#telNo').val();
	var nickName =  $('#nickName').val();
	var use =  $('#use').val();
	var idx =  $('#idx').val();

	if (telNo == null || telNo == "") {
		alert(lang.admin.alert.subNumberManage1);
		$('#telNo').focus();
		return;
	} else if(!/^-?\d*$/.test(telNo)) {
		alert(lang.admin.alert.subNumberManage2);
		$('#telNo').focus();
		return;
	}

	if(nickName == null || nickName == "") {
		alert(lang.admin.alert.subNumberManage3);
		$('#nickName').focus();
		return;
	}

	if(use == null || use == "") {
		alert(lang.admin.alert.subNumberManage4);
		$('#use').focus();
		return;
	}

	var forEachResult = true;
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){

			if(this.getSelectedRowId() == id)
				return;

			if(telNo == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.subNumberManage5)
				$('#telNo').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"telNo"				: telNo
	    ,   "nickName"			: nickName
	    ,	"use"				: use
	    ,	"idx"				: idx
	    ,	"proc"              : "modify"
	};

	$.ajax({
		url:contextPath+"/subNumber_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.subNumberManage12);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/subNumber_list.xml", function(){
					$(objGrid.getFilterElement(3)).children().remove()
					$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
					$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.subNumber.label.selY + "</option>")
					$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.subNumber.label.selN + "</option>")
				})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.subNumberManage13)
			}
		}
	});
};

// 자번호 삭제
function onSubNumberDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstIdx = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstIdx.push(objGrid.cells2(parseInt(checked[i])-1,4).getValue());
		}

		var rst = rstIdx.join(",");
		if (confirm(lang.admin.alert.subNumberManage8)){
			$.ajax({
				url:contextPath+"/subNumber_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.subNumberManage9)
						objGrid.clearAndLoad(contextPath + "/subNumber_list.xml", function(){ 
							$(objGrid.getFilterElement(3)).children().remove()
							$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
							$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.subNumber.label.selY + "</option>")
							$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.subNumber.label.selN + "</option>")
						}, "xml");
					} else {
						alert(lang.admin.alert.subNumberManage10)
					}
				}
			});
		}
	} else {
		alert (lang.admin.alert.subNumberManage11)
	}
}