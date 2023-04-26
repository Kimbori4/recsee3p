// 전역변수 설정
var gridAttrManage; // 그리드
addLoadEvent(AttrManageLoad);

function AttrManageLoad() {
	gridAttrManage = AttrManageGridLoad();
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
function AttrManageGridLoad() {
    // 채널관리 Grid
	gridAttrManage = new dhtmlXGridObject("gridAttrManage");
	gridAttrManage.setIconsPath(recseeResourcePath + "/images/project/");
	gridAttrManage.setImagePath(recseeResourcePath + "/images/project/");
	gridAttrManage.enablePreRendering(50);
	gridAttrManage.enableColumnAutoSize(false);
	gridAttrManage.enablePreRendering(50);
	gridAttrManage.setSkin("dhx_web");
	gridAttrManage.init();
	gridAttrManage.load(contextPath+"/attrManage.xml", function(){
		/*$(window).resize();
		gridAttrManage.setSizes();

		$(window).resize(function() {
			gridAttrManage.setSizes();
		});

		//슬라이더 표시
		var slider =[];
		var ind=0;
		for(var i=1; i<=gridAttrManage.getRowsNum();i++){
			if(gridAttrManage.cells(i,5).getValue()=='percent'){
				slider.push(new dhtmlXSlider("sliderObj"+i));
				slider[ind].linkTo("inputVal"+i);
				ind++;
			}
		}*/
		sliderAttach();
	}, 'xml')

	// 체크박스 전체 선택
	gridAttrManage.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				gridAttrManage.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				gridAttrManage.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	gridAttrManage.attachEvent("onRowSelect", function(id,ind){
	    return;
	});

	//변경사항 적용 팝업 표시
	gridAttrManage.attachEvent("onRowDblClicked", function(id,ind){
		$("#addAlert").find("p").text("관제지표 수정")
		$("#itemName").val($(this.cells(id,2).cell).children().text());
		$("#itemType").val(this.cells(id,3).getValue());
		$("#limitType").val(this.cells(id,5).getValue()).change();
		if($("#limitType").val()=='on/off'){
			var radio=$("input[name='inputVal"+id+"']:checked").val();
			$("#radio_"+radio).prop("checked", true);
		}
		else
			$("#limit").val($("#inputVal"+id).val());

		var radio=$("input[name='inputValLog"+id+"']:checked").val();
		$("#log_"+radio).prop("checked", true);

		$("#AttrAddBtn").hide();
		$("#AttrModifyBtn").show();

		layer_popup('#addAlert')

	});

    ui_controller();
    return gridAttrManage;
}

function formFunction() {

	// 추가 버튼
	$('#btnAlertAddBtn').click(function() {

		$("#addAlert").find("p").text(lang.admin.alert.attrManage1)
		$("#itemType").val("서버");
		$("#limitType").val("percent").change();

		$("#AttrAddBtn").show();
		$("#AttrModifyBtn").hide();

		layer_popup('#addAlert')
	})

	// 팝업 수정 버튼
	$('#AttrModifyBtn').click(function() {
		onAttrModifyProc();
	})
	//팝업 저장 버튼
	$('#AttrAddBtn').click(function() {
		onAlertAddProc();
	})
	// 삭제 버튼
	$('#btnTargetDel').click(function() {
		onAlertDel();
	})
	//저장 버튼
	$('#btnAlertApply').click(function() {
		onAlertSaveProc();
	})

	//알림 임계치 예외처리
	$(document).on("keyup",'.input_input', function(e){
		//왼쪽 오른쪽 화살표 키 예외처리
		if(e.keyCode!=37&&e.keyCode!=39){
			var value = this.value;
			value = value.replace(/[^0-9]/g,'');
			if(value.substr(0,1)=='0'&&value.length>1)
				this.value = value.substring(1,value.length);
			else
				this.value = value;
		}
	});
	//알림 임계치 예외처리
	$("#limit").keyup(function(e) {
		//왼쪽, 오른쪽 화살표 예외처리
		if(e.keyCode!=37&&e.keyCode!=39){
			var value = this.value;
			value = value.replace(/[^0-9]/g,'');
			if(value.substr(0,1)=='0'&&value.length>1)
				this.value = value.substring(1,value.length);
			else
				this.value = value;
			if($("#limitType").val()=='percent'){
				if(Number(this.value)>100){
					this.value='100';
				}
			}
		}
	});
	//팝압 임계치 유형에 따름 임계치 input 변경
	$("#limitType").change(function() {
		if($("#limitType").val()=='on/off'){
			$("#limit_wrap").css('display','none');
			$("#radio_wrap").css('display','block');
		}else{
			$("#limit_wrap").css('display','block');
			$("#radio_wrap").css('display','none');
		}
	});

}
//삭제
function onAlertDel() {
		if(gridAttrManage.getCheckedRows(0) != "") {
			var checked = gridAttrManage.getCheckedRows(0).split(",");
			var rstChNum = new Array();

			for( var index in checked ) {
				rstChNum.push(gridAttrManage.cells(parseInt(checked[index]),1).getValue());
			}
			var rst = rstChNum.join(",");

			if(confirm(lang.admin.alert.item.Manage8 /* "정말 삭제하시겠습니까?" */)){
				$.ajax({
					url:contextPath+"/Attr_delete_proc.do",
					data:"data="+ rst,
					type:"POST",
					dataType:"json",
					async: false,
					success:function(jRes){
						if(jRes.success == "Y") {
							alert(lang.admin.alert.attrManage2)
							gridAttrManage.clearAndLoad(contextPath+"/attrManage.xml", function(){
								sliderAttach();
							})
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

function onAttrModifyProc(){
	var itemIndex=gridAttrManage.cells(gridAttrManage.getSelectedId(),1).getValue();
	var itemName= $("#itemName").val()
	var itemType= $("#itemType").val()
	var limitType= $("#limitType").val();
		if(limitType=='on/off')
			var limit=$("input[name='limitRadio']:checked").val();
		else
			var limit= $("#limit").val();
	var logYn=$("input[name='logYn']:checked").val();

	if (itemName == null || itemName == "") {
		alert(lang.admin.alert.attrManage5);
		$('#itemName').focus();
		return;
	}else if((limit == null || limit == "")&&limitType!="on/off"){
		alert(lang.admin.alert.attrManage6);
		$('#limit').focus();
		return;
	}
	var forEachResult = true;
	// 중복 처리
	if(gridAttrManage.getRowsNum() > 0 ) {
		gridAttrManage.forEachRow(function(id){
			if(itemName ==$(gridAttrManage.cells(id,2).cell).text()&&gridAttrManage.getSelectedId()!=id) {
				alert(lang.admin.alert.attrManage7)
				$('#itemName').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;

	var dataStr = {
			"itemIndex"	: itemIndex
		,	"itemName"	: itemName
	    ,   "itemType"		: itemType
	    ,	"limitType"		: limitType
	    ,	"logYn"			: logYn
	    ,   "limit"			: limit
	};

	$.ajax({
		url:contextPath+"/Attr_update_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.attrManage8);

				layer_popup_close();
				gridAttrManage.clearAndLoad(contextPath+"/attrManage.xml", function(){
					sliderAttach();
				})

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.attrManage9)
			}
		}
	});
}
//관제 지표 수정
function onAlertAddProc() {
	var itemName= $("#itemName").val();
	var itemType= $("#itemType").val();
	var limitType= $("#limitType").val();
		if(limitType=='on/off')
			var limit=$("input[name='limitRadio']:checked").val();
		else
			var limit= $("#limit").val();

	if (itemName == null || itemName == "") {
		alert(lang.admin.alert.attrManage10);
		$('#itemName').focus();
		return;
	}else if((limit == null || limit == "")&&limitType!="on/off"){
		alert(lang.admin.alert.attrManage11);
		$('#limit').focus();
		return;
	}
	var forEachResult = true;
	// 중복 처리
	if(gridAttrManage.getRowsNum() > 0 ) {
		gridAttrManage.forEachRow(function(id){
			if(itemName ==$(gridAttrManage.cells(id,2).cell).text()) {
				alert(admin.alert.attrManage7)
				$('#itemName').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;

	var dataStr = {
			"itemName"	: itemName
	    ,   "itemType"		: itemType
	    ,	"limitType"		: limitType
	    ,   "limit"				: limit
	};

	$.ajax({
		url:contextPath+"/Attr_add_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.attrManage12);

				layer_popup_close();
				gridAttrManage.clearAndLoad(contextPath+"/attrManage.xml", function(){
					sliderAttach();
				})

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.attrManage13)
			}
		}
	});
}

//변경 사항 저장
function onAlertSaveProc(){
	var data=[];
	var dataChk=true;
	var totalCount=gridAttrManage.getRowsNum();
	for(var i=1;i<=totalCount;i++){
		var obj = new Object();
		obj.id=gridAttrManage.cells(i,1).getValue();

		var unit=gridAttrManage.cells(i,5).getValue();
		switch (unit) {
		case 'on/off':
			obj.limit=$("input[name='inputVal"+i+"']:checked").val();
			break;
		default:
			obj.limit=$("#inputVal"+i).val();
			break;
		}
		if(obj.limit=='NaN'){
			dataChk=false;
			break;
		}
		obj.logYn=$("input[name='inputValLog"+i+"']:checked").val()
		data.push(obj);
	}
	if(!dataChk){
		alert(lang.admin.alert.attrManage14)
		return;
	}
	var jsonData=JSON.stringify(data);
	$.ajax({
		url:contextPath+"/saveLimit.do",
		data:{"jsonData":jsonData},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.attrManage15);

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.attrManage16)
			}
		}
	});
}
//슬라이더 붙이기
function sliderAttach() {

	$(window).resize();
	gridAttrManage.setSizes();

	$(window).resize(function() {
		gridAttrManage.setSizes();
	});

	//슬라이더 표시
	var slider =[];
	var ind=0;
	for(var i=1; i<=gridAttrManage.getRowsNum();i++){
		if(gridAttrManage.cells(i,5).getValue()=='percent'){
			slider.push(new dhtmlXSlider("sliderObj"+i));
			slider[ind].linkTo("inputVal"+i);
			ind++;
		}
	}


}