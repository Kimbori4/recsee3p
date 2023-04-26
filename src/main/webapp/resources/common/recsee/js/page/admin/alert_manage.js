// 전역변수 설정
var gridAlertManage; // 그리드
var Alert = "", chRes = "";
var chGenerationJSON = "", chExcelUploadJSON = "", chAddJSON = "", monitoringIP="127.0.0.1";

addLoadEvent(AlertManageLoad);

function AlertManageLoad() {
	gridAlertManage = AlertManageGridLoad();
	formFunction();
	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn!= 'Y') {
		$('#btnAlertAdd').hide();
	}

	if(delYn != 'Y') {
		$('#btnAlertDel').hide();
	}
}

// 채널관리 로드
function AlertManageGridLoad() {
    // 채널관리 Grid
	gridAlertManage = new dhtmlXGridObject("gridAlertManage");
	gridAlertManage.setIconsPath(recseeResourcePath + "/images/project/");
	gridAlertManage.setImagePath(recseeResourcePath + "/images/project/");
	gridAlertManage.enablePreRendering(50);
	gridAlertManage.i18n.paging = i18nPaging[locale];
	gridAlertManage.enablePaging(true, 30, 5, "pagingAlertManage", true);
	gridAlertManage.setPagingWTMode(true,true,true,[30,60,90,100]);
    gridAlertManage.setPagingSkin("toolbar", "dhx_web");
	gridAlertManage.enableColumnAutoSize(false);
	gridAlertManage.enablePreRendering(50);
    gridAlertManage.setSkin("dhx_web");
	gridAlertManage.init();
	gridAlertManage.load(contextPath+"/alertManage.xml", function(){
		gridAlertManage.aToolBar.addSpacer("perpagenum");
		$(window).resize();
		gridAlertManage.setSizes();

		$(window).resize(function() {
			gridAlertManage.setSizes();
		});

	}, 'xml')

	// 체크박스 전체 선택
	gridAlertManage.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				gridAlertManage.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				gridAlertManage.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	gridAlertManage.attachEvent("onRowSelect", function(id,ind){
	    return;
	});


    ui_controller();
    return gridAlertManage;
}

function formFunction() {


	// 팝업 생성 버튼
	$('#AlertAddBtn').click(function() {
		onAlertAddProc();
	});

	// 팝업 수정 버튼
	$('#AlertModifyBtn').click(function() {
		onAlertModifyProc();
	})

	// 적용 버튼
	$('#btnAlertApply').click(function() {
		onAlertApply();
	})

	// 채널 삭제 버튼
	$('#DeleteBtn').click(function() {
		onAlertDel();
	})
}

function onAlertDel() {
	if(gridAlertManage.getCheckedRows(0) != "") {
		var checked = gridAlertManage.getCheckedRows(0).split(",");
		var rstChNum = new Array();

		for( var index=0;index<checked.length;index++) {
			rstChNum.push(gridAlertManage.cells(parseInt(checked[index]),2).getValue());
		}
		var rst = rstChNum.join(",");

		$.ajax({
			url:contextPath+"/Alert_delete.do",
			data:"chList=" + rst,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					gridAlertManage.clearAndLoad(contextPath + "/alertManage.xml", function(){ }, "xml");
					alert(lang.admin.alert.successDelete)
				} else {
					alert(lang.admin.alert.failDelete)
				}
			}
		});
 	} else {
 		alert (lang.admin.alert.selectDeleteServer)
 	}
}
