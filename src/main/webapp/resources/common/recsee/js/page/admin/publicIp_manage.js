// 전역변수 설정
var gridPublicIpManage; // 그리드
var publicIp = "", publicIpRes = "";
var publicIpAddJSON = "";

addLoadEvent(publicIpManageLoad);

function publicIpManageLoad() {
	gridPublicIpManage = publicIpManageGridLoad();
	
	// 메인페이지 IP 등록 버튼
	$("#ipAddBtn").click(function(){
		$("#addIp").find("p").text(lang.admin.publicIp.title.addIp)

		$("#rPublicIp").val("");

		$("#ipAdd").show();
		$("#ipModify").hide();

		layer_popup('#addIp');
	});

	// 팝업창 자번호 추가
	$('#ipAdd').click(function(){
		onIpAddProc();
	});

	// 팝업창 자번호 수정
	$('#ipModify').click(function(){
		onIpModifyProc();
	});

	// 자번호 삭제
	$('#ipDel').click(function() {
		onIpDelProc()
	})

	authyLoad();
	
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		$('#ipAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#ipDel').hide();
	}
}
// 공인IP 관리 로드
function publicIpManageGridLoad() {
    // 공인IP 관리 Grid
	objGrid = new dhtmlXGridObject("gridPublicIpManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingPublicIpManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/publicIpInfo_list.xml", function(){
		
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
		$("#addip").find("p").text(lang.admin.publicIp.title.modifyIp)

		var rPublicIp =  this.cells(id, 1).getValue();

		$("#rPublicIp").val(rPublicIp);
		$("#rPublicIpOld").val(rPublicIp);

		$("#ipAdd").hide();
		$("#ipModify").show();

		layer_popup('#addIp');
	});

    ui_controller();
}


// ip 등록, 기존 로직 그대로 사용
function onIpAddProc(){

	var rPublicIp = $('#rPublicIp').val();

	if (rPublicIp == null || rPublicIp == "") {
		alert(lang.admin.alert.publicIpManage1);
		$('#rPublicIp').focus();
		return;
	}

	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(rPublicIp == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.publicIpManage2)
				$('#rPublicIp').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"rPublicIp"		: rPublicIp
	    ,	"proc"          : "insert"
	};

	$.ajax({
		url:contextPath+"/publicIp_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.publicIpManage3);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/publicIpInfo_list.xml", function(){
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.alert.publicIpManage4)
			}
		}
	});
};

// ip 수정
function onIpModifyProc(){

	var rPublicIp = $('#rPublicIp').val();
	var rPublicIpOld = $('#rPublicIpOld').val();

	if (rPublicIp == null || rPublicIp == "") {
		alert(lang.admin.alert.publicIpManage1);
		$('#rPublicIp').focus();
		return;
	}

	var forEachResult = true;
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){

			if(this.getSelectedRowId() == id)
				return;

			if(rPublicIp == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.publicIpManage2)
				$('#rPublicIp').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"rPublicIp"			: rPublicIp
		,	"rPublicIpOld"   	: rPublicIpOld
	    ,	"proc"              : "modify"
	};

	$.ajax({
		url:contextPath+"/publicIp_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.publicIpManage5);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/publicIpInfo_list.xml", function(){
				})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.publicIpManage6)
			}
		}
	});
};

// ip 삭제
function onIpDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstIdx = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstIdx.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
		}

		var rst = rstIdx.join(",");
		if (confirm(lang.admin.alert.publicIpManage7)){
			$.ajax({
				url:contextPath+"/publicIp_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.publicIpManage8)
						objGrid.clearAndLoad(contextPath + "/publicIpInfo_list.xml", function(){ 
						}, "xml");
					} else {
						alert(lang.admin.alert.publicIpManage9)
					}
				}
			});
		}
	} else {
		alert (lang.admin.alert.publicIpManage10)
	}
}