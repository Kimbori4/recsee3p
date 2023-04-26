// 전역변수 설정
var gridUIManage; // 그리드
var myTabbar;
var mapContainer;
var iconContainer;
var tempRowId;

addLoadEvent(UIManageLoad);

function UIManageLoad() {
	myTabbar= new dhtmlXTabBar("my_tabbar");
	myTabbar.addTab("tab1", lang.admin.alert.uiManage2,null,null,true);
	myTabbar.addTab("tab2",lang.admin.alert.uiManage1);
	myTabbar.addTab("tab3",lang.admin.alert.uiManage3);
	
	//맵 데이터뷰 양식 정의
	mapContainer = new dhtmlXDataView({
		container:"map_container",
		type:{
			template:"<div class='cardWrap #useCardClass#'>" +
							"<div index='#index#' use = '#use#' class='dataViewCard' style='background-image:url("+recseeResourcePath+"/images/project/map-view/#fileName#);'>" +
							"</div>" +
							"<p class='dataViewP #useClass#'>#MapName#</p>" +
						"</div>"
		},
		autowidth:6,
	});
	myTabbar.tabs("tab2").attachObject("tab2");
	//아이콘 데이터 뷰 양식 정의
	iconContainer = new dhtmlXDataView({
		container:"icon_container",
		type:{
			template:"<div class='cardWrap'>" +
							"<div index='#index#' use = '#use#' class='dataViewCard' style='background-image:url("+recseeResourcePath+"/images/project/map-view/#fileName#);'>" +
							"</div>" +
							"<p class='dataViewP'>#iconName#</p>" +
						"</div>"
		},
		autowidth:6,
	});
	myTabbar.tabs("tab3").attachObject("tab3");
	
	
	mapContainer.attachEvent("onItemDblClick" , function (id){
		var url='url('+recseeResourcePath+'/images/project/map-view/'+mapContainer.get(id).fileName+")";
		$("#map_detail_wrap").css('background-image',url);
		$("#map_name").val(mapContainer.get(id).MapName)
		layer_popup('#mapDetailPopup')
	});
	
	selectMapList();
	selectIconList();
	UIManageGridLoad();
	formFunction();
	authyLoad();
	
}

//권한 불러 오기
function authyLoad() {
/*	if(modiYn!= 'Y') {
		$('#btnUIAdd').hide();
	}

	if(delYn != 'Y') {
		$('#btnUIDel').hide();
	}*/
}

// 각종 dhtmlx 로드
function UIManageGridLoad() {
	gridUIManage = new dhtmlXGridObject("gridUIManage");
	gridUIManage.setIconsPath(recseeResourcePath + "/images/project/");
	gridUIManage.setImagePath(recseeResourcePath + "/images/project/");
	gridUIManage.enablePreRendering(50);
    gridUIManage.setPagingSkin("toolbar", "dhx_web");
	gridUIManage.enableColumnAutoSize(false);
	gridUIManage.enablePreRendering(50);
	gridUIManage.enableDragAndDrop(true);
	//gridUIManage.enableDragOrder(true);
    gridUIManage.setSkin("dhx_web");
	gridUIManage.init();
	gridUIManage.load(contextPath+"/UIManage.xml", function(){

		$(window).resize();
		gridUIManage.setSizes();
		
		$("#my_tabbar").css("height",$(".admin_lnb").css("height"));
		myTabbar.setSizes();
		
		$(window).resize(function() {
			gridUIManage.setSizes();
			$("#my_tabbar").css("height",$(".admin_lnb").css("height"));
			myTabbar.setSizes();
		});
		
		myTabbar.tabs("tab1").attachObject("tab1");
		
	}, 'xml')

	// 체크박스 전체 선택
	gridUIManage.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 1 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				gridUIManage.setCheckedRows(1, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				gridUIManage.setCheckedRows(1, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	
	gridUIManage.attachEvent("onRowSelect" , function (id, ind){
		return true;
	});

	gridUIManage.attachEvent("onDrop" , function (sid, ind){
		var count=this.getRowsNum();
		for(var i=1;i<=count;i++){
			this.cells(i,2).setValue(this.getRowIndex(i)+1);
		}
		return true;
	});
	
	gridUIManage.attachEvent("onRowDblClicked", function(id,ind){
		$.ajax({
			url:contextPath+"/ui_select_icon_combo.do",
			data:"",
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					var iconComboList=jRes.resData.result;
					$("#UIobject").empty();
					for(var i=0; i<iconComboList.length;i++){
						$("#UIobject").append("<option filename='"+iconComboList[i].rm_icon_filename+"' value='"+iconComboList[i].rm_icon_index+"'>"+iconComboList[i].rm_icon_descr+"</option>");
					}
					
				selectTo();
				} else {
					alert(lang.admin.alert.uiManage4)
				}
			}
		});
		
		var UIname 	= this.cells(id, 3).getValue();
		var UItype 		= this.cells(id, 4).getValue();
		var UIdetail 		= this.cells(id, 5).getValue();
		var UIwatchTime 		= this.cells(id, 6).getValue();
		var UIobject 			= this.cells(id, 9).getValue();

		$("#UIname").val(UIname);
		$("#UItype").val(UItype);
		$("#UIdetail").val(UIdetail);
		$("#UIwatchTime").val(UIwatchTime);
		$("#UIobject").select2('val',UIobject);
		
		layer_popup('#autoAddChannel')
		
	});
	
    ui_controller();
    return gridUIManage;
}

function formFunction() {
	

	// 관제UI 팝업 ui수정버튼
	$('#targat_ui_save').click(function() {
		targetUiSave();
	})
	//관측 순번 수정
	$('#btnUISave').click(function() {
		updateOrder();
	})
	
	// UI초기화
	$('#btnUIDel').click(function() {
		onUIDel();
	})
	//맵 이름 수정
	$('#map_detail_save').click(function() {
		updateMapName();
	})
	//맵 선택
	$('#map_detail_choose').click(function() {
		chooseMap();
	})
	
}

//관제 오브젝트 map 불러오기
function selectMapList() {
	$.ajax({
		url:contextPath+"/ui_select_map.do",
		data:"",
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var maplist=jRes.resData.result;
				for(var i=0; i<maplist.length;i++){
					if(maplist[i].rm_map_use=='Y'){
						mapContainer.add({
							index:maplist[i].rm_map_name,
							path:maplist[i].rm_map_path,
							fileName:maplist[i].rm_map_filename,
							MapName:maplist[i].rm_map_descr+' (사용중)',
							use:maplist[i].rm_map_use,
							useClass:'useMap',
							useCardClass:'useMapCard'
						});
					}else{
						mapContainer.add({
							index:maplist[i].rm_map_name,
							path:maplist[i].rm_map_path,
							fileName:maplist[i].rm_map_filename,
							MapName:maplist[i].rm_map_descr,
							use:maplist[i].rm_map_use
						});
					}
				}
				mapContainer.refresh();
			} else {
				alert(lang.admin.alert.uiManage4)
			}
		}
	});
}
//관제 아이콘 불러오기
function selectIconList() {
	$.ajax({
		url:contextPath+"/ui_select_icon.do",
		data:"",
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var iconlist=jRes.resData.result;
				for(var i=0; i<iconlist.length;i++){
						iconContainer.add({
							index:iconlist[i].rm_icon_index,
							path:iconlist[i].rm_icon_path,
							fileName:iconlist[i].rm_icon_filename,
							iconName:iconlist[i].rm_icon_descr
						});
				}
				iconContainer.refresh();
			} else {
				alert(lang.admin.alert.uiManage4)
			}
		}
	});
}
//관제 ui 삭제
function onUIDel() {
	if(gridUIManage.getCheckedRows(1) != "") {
		var checked = gridUIManage.getCheckedRows(1).split(",");
		var rstChNum = new Array();

		for( var index=0; index<checked.length;index++ ) {
			rstChNum.push(gridUIManage.cells(parseInt(checked[index]),0).getValue());
		}
		var data = rstChNum.join(",");
		
		$.ajax({
			url:contextPath+"/UI_del.do",
			data:"data="+data,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.alert.uiManage14);
					gridUIManage.clearAndLoad(contextPath + "/UIManage.xml", function(){ }, "xml");
				} else {
					alert(lang.admin.alert.uiManage15)
				}
			}
		});
 	} else {
 		alert (lang.admin.alert.uiManage13)
 	}
}
//관제 대상 ui 설정
function targetUiSave() {
	var uiWatchTime = $("#UIwatchTime").val();
	var uiObject = $("#UIobject").val();
	var index= gridUIManage.cells(gridUIManage.getSelectedId(),0).getValue();
	
	var data={
		"uiWatchTime" :	 uiWatchTime,
		"uiObject"		:	uiObject,
		"index" : index
	};
	
	$.ajax({
		url:contextPath+"/target_ui_save.do",
		data:data,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.uiManage5)
				gridUIManage.clearAndLoad(contextPath + "/UIManage.xml", function(){ }, "xml");
				layer_popup_close();
			} else {
				alert(lang.admin.alert.uiManage4)
			}
		}
	});
}
//관측 순번 수정
function updateOrder() {
	var obj = new Array();

	for( var index=1; index<=gridUIManage.getRowsNum();index++ ) {
		var temp = new Object();
		temp.index=gridUIManage.cells(index,0).getValue()
		temp.order=gridUIManage.cells(index,2).getValue()
		obj.push(temp);
	}
	var jsondata=JSON.stringify(obj)
	$.ajax({
		url:contextPath+"/update_order.do",
		data:"data="+jsondata,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.uiManage6);
				gridUIManage.clearAndLoad(contextPath + "/UIManage.xml", function(){ }, "xml");
			} else {
				alert(lang.admin.alert.uiManage7)
			}
		}
	});
}

//맵 이름 수정
function updateMapName() {
	var mapName = $("#map_name").val().split("(")[0];
	var index= $(".dhx_dataview_default_item_selected .dataViewCard").attr('index');
	
	var data={
		"mapName" :	 mapName,
		"index" : index
	};
	
	$.ajax({
		url:contextPath+"/update_map_name.do",
		data:data,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.uiManage8)
				mapContainer.clearAll();
				selectMapList();
			} else {
				alert(lang.admin.alert.uiManage9)
			}
		}
	});
}
//맵 사용 결정
function chooseMap() {
	var index= $(".dhx_dataview_default_item_selected .dataViewCard").attr('index');
	
	var data={
		"index" : index
	};
	
	$.ajax({
		url:contextPath+"/choose_map.do",
		data:data,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.uiManage10)
				mapContainer.clearAll();
				selectMapList();
				layer_popup_close();
			} else {
				alert(lang.admin.alert.uiManage11)
			}
		}
	});
}
//관제 대상 ui설정 팝업 콤보박스
function selectTo() {
	//selct2 포멧
	function formatState(state) {
		if(!state.id){
			return state.text;
		}
		var temp=$(state.element).attr('filename');
		var $state=$('<span class="select2_option" id='+state.id+'"><div class="iconWrap"><img class="iconCombo" src="'+recseeResourcePath+'/images/project/map-view/'+temp+'"></div><div class="pWrap">'+state.text+'</div></span>');
		
		return $state;
	}
	//slect2 옵션 넣기
	$("#UIobject").select2({
		placeholder:lang.admin.alert.uiManage12,
		templateResult: formatState,
		templateSelection: formatState,
		width:'calc(100% - 118px)',
	});
}


