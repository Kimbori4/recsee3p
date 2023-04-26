// 20200420 jbs 기존 화면 구성 활용(subNumber_manage 참고)
// 전역변수 설정
var gridPhoneMapping; // 그리드
var phoneMapping = "", phoneMappingRes = "";
var phoneMappingAddJSON = "";

addLoadEvent(phoneMappingLoad);

function phoneMappingLoad() {
	gridPhoneMapping = phoneMappingLoadGridLoad();
	
	// 콤보박스 옵션 생성
	$("#useNickName").append("<option value='Y' selected>"+ lang.admin.phoneMapping.label.selY + "</option>")
	$("#useNickName").append("<option value='N'>"+ lang.admin.phoneMapping.label.selN + "</option>")
	
	// 메인페이지 자번호 추가 버튼
	$("#phoneMappingAddBtn").click(function(){
		$("#addPhoneMapping").find("p").text(lang.admin.phoneMapping.title.addPhoneMapping)

		$("#custPhone").val("");
		$("#custNickName").val("");
		$("#useNickName").val("Y");

		$("#phoneMappingAdd").show();
		$("#phoneMappingModify").hide();

		layer_popup('#addPhoneMapping');
	});

	// 팝업창 자번호 추가
	$('#phoneMappingAdd').click(function(){
		onPhoneMappingAddProc();
	});

	// 팝업창 자번호 수정
	$('#phoneMappingModify').click(function(){
		onPhoneMappingModifyProc();
	});

	// 자번호 삭제
	$('#phoneMappingDel').click(function() {
		onPhoneMappingDelProc()
	})

	authyLoad();
	
	$("#useNickName").hide();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		$('#phoneMappingAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#phoneMappingDel').hide();
	}
}
// 자번호관리 로드
function phoneMappingLoadGridLoad() {
    // 자번호관리 Grid
	objGrid = new dhtmlXGridObject("gridPhoneMapping");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingPhoneMapping", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/phoneMapping_list.xml", function(){
		
		var search_toolbar = objGrid.aToolBar;
		
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum() + '</div>')
		search_toolbar.setWidth("total",150)

		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		// select필터 재정의(value값을 텍스트값으로 나오게)		
		$(objGrid.getFilterElement(3)).children().remove()
		$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
		$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.phoneMapping.label.selY + "</option>")
		$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.phoneMapping.label.selN + "</option>")
		
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
		$("#addPhoneMapping").find("p").text(lang.admin.phoneMapping.title.addPhoneMapping)

		var custPhone =  this.cells(id, 1).getValue();
		var	custNickName = this.cells(id, 2).getValue();
		var useNickName = this.cells(id, 3).getValue();
		var custPhoneKey = this.cells(id, 4).getValue();
		var procType = this.cells(id, 5).getValue();
		var procPosition = this.cells(id, 6).getValue();

		$("#custPhone").val(custPhone)
		$("#custNickName").val(custNickName);
		$("#useNickName").val(useNickName);
		$("#custPhoneKey").val(custPhoneKey);
		$("#procType").val(procType);
		$("#procPosition").val(procPosition);

		$("#phoneMappingAdd").hide();
		$("#phoneMappingModify").show();

		layer_popup('#addPhoneMapping');
	});

    ui_controller();
}


// 맵핑 추가, 기존 로직 그대로 사용
function onPhoneMappingAddProc(){

	var custPhone = $('#custPhone').val();
	var custNickName =  $('#custNickName').val();
	var useNickName =  $('#useNickName').val();
	var procType =  $('#procType').val();
	var procPosition =  $('#procPosition').val();

	if (custPhone == null || custPhone == "") {
		alert(lang.admin.alert.phoneMapping1);
		$('#custPhone').focus();
		return;
	} else if(!/^-?\d*$/.test(custPhone)) {
		alert(lang.admin.alert.phoneMapping2);
		$('#custPhone').focus();
		return;
	}

	if(custNickName == null || custNickName == "") {
		alert(lang.admin.alert.phoneMapping3);
		$('#custNickName').focus();
		return;
	}

	if(useNickName == null || useNickName == "") {
		alert(lang.admin.alert.phoneMapping4);
		$('#useNickName').focus();
		return;
	}

	var forEachResult =true
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if(custPhone == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.phoneMapping5)
				$('#custPhone').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"custPhone"				: custPhone
	    ,   "custNickName"			: custNickName
	    ,	"useNickName"			: useNickName
	    ,	"procType"				: procType
	    ,	"procPosition"			: procPosition
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/phoneMapping_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.phoneMapping6);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/phoneMapping_list.xml", function(){
					$(objGrid.getFilterElement(3)).children().remove()
					$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
					$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.phoneMapping.label.selY + "</option>")
					$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.phoneMapping.label.selN + "</option>")
				})
				return
				
			} else if(jRes.success == "N"){
				alert(lang.admin.alert.phoneMapping7)
			}
		}
	});
};

function onPhoneMappingModifyProc(){

	var custPhone = $('#custPhone').val();
	var custNickName =  $('#custNickName').val();
	var useNickName =  $('#useNickName').val();
	var custPhoneKey =  $('#custPhoneKey').val();
	var procType =  $('#procType').val();
	var procPosition =  $('#procPosition').val();

	if (custPhone == null || custPhone == "") {
		alert(lang.admin.alert.phoneMapping1);
		$('#custPhone').focus();
		return;
	} else if(!/^-?\d*$/.test(custPhone)) {
		alert(lang.admin.alert.phoneMapping2);
		$('#custPhone').focus();
		return;
	}

	if(custNickName == null || custNickName == "") {
		alert(lang.admin.alert.phoneMapping3);
		$('#custNickName').focus();
		return;
	}

	if(useNickName == null || useNickName == "") {
		alert(lang.admin.alert.phoneMapping4);
		$('#useNickName').focus();
		return;
	}

	var forEachResult = true;
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){

			if(this.getSelectedRowId() == id)
				return;

			if(custPhone == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.phoneMapping5)
				$('#custPhone').focus();
				forEachResult = false;
			} 
		});
	}

	if(!forEachResult)
		return;

	var dataStr = {
			"custPhone"			: custPhone
		,   "custNickName"		: custNickName
		,	"useNickName"		: useNickName
		,	"custPhoneKey"		: custPhoneKey
		,	"procType"			: procType
		,	"procPosition"		: procPosition
	    ,	"proc"              : "modify"
	};

	$.ajax({
		url:contextPath+"/phoneMapping_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.phoneMapping12);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/phoneMapping_list.xml", function(){
					$(objGrid.getFilterElement(3)).children().remove()
					$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
					$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.phoneMapping.label.selY + "</option>")
					$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.phoneMapping.label.selN + "</option>")
				})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.phoneMapping13)
			}
		}
	});
};

// 자번호 삭제
function onPhoneMappingDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstCustPhone = new Array();

		for(var i = 0 ; i < checked.length ; i++){
			rstCustPhone.push(objGrid.cells2(parseInt(checked[i])-1,1).getValue());
		}

		var rst = rstCustPhone.join(",");
		if (confirm(lang.admin.alert.phoneMapping8)){
			$.ajax({
				url:contextPath+"/phoneMapping_proc.do",
				data:"proc=delete&chList=" + rst,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.phoneMapping9)
						objGrid.clearAndLoad(contextPath + "/phoneMapping_list.xml", function(){ 
							$(objGrid.getFilterElement(3)).children().remove()
							$(objGrid.getFilterElement(3)).append("<option value='' selected></option>")
							$(objGrid.getFilterElement(3)).append("<option value='Y'>"+ lang.admin.phoneMapping.label.selY + "</option>")
							$(objGrid.getFilterElement(3)).append("<option value='N'>"+ lang.admin.phoneMapping.label.selN + "</option>")
						}, "xml");
					} else {
						alert(lang.admin.alert.phoneMapping10)
					}
				}
			});
		}
	} else {
		alert (lang.admin.alert.phoneMapping11)
	}
}