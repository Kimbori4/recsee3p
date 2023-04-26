window.onload = function(){
	searchManageLoad();
	if (!(dataCopy == "Y" && dataCopyAll == "N")) { // (그리드 복사 기능을 사용하면서, 복사할 컬럼 사용자 정의할 경우) 가 아니면 그리드 숨김처리 & 나머지 박스 width 늘림 
		$(".halfGridMaster").attr("style","width:49.8% !important");
		$(".halfGridMaster2").attr("style","width:49.8% !important");
		$(".halfGridRight").attr("style","width:50% !important");
		$("#copyListBox").hide();
	}
	$(window).trigger('resize');
}

var searchItemGrid, searchItemBaseGrid, searchListGrid, searchListBaseGrid, searchCopyListGrid;
var drag_base_item = false, drag_user_item = false, drag_base_list = false, drag_user_list = false, drag_copy_list = false;
var drag_item_mode = null, drag_list_mode = null;

//그리드 생성 함수
function _createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn, dragNdrop, multiselect){
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");

	// 페이징 유무에 따른 실행
	if(objPaging){
		objGrid.i18n.paging = i18nPaging[locale];
		objGrid.enablePaging(true,initPageRow, 5, objPaging, true);
		objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	    objGrid.setPagingSkin("toolbar","dhx_web");
	}

	objGrid.enableResizing("false,false,false");
	objGrid.enableDragAndDrop(dragNdrop);
	objGrid.enableMultiselect(multiselect);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});

		// 파싱 시작
		objGrid.attachEvent("onDynXLS", function(start,count){
			progress.on();
		});

		objGrid.attachEvent("onXLS", function(grid_obj){
			progress.on();
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			ui_controller();
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
			});
		});

		if(objPaging){
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}

		ui_controller();
		
		objGrid.setSizes();
	});
	return objGrid;
}

function someGridItemCheck(fDrag, sId, tId, sObj, tObj) {
	if(tObj.dragContext.mode == "copy") {
		var fCheck = true;
		var chkValue = sId.split(",");
		for(var i = 0; i < chkValue.length; i++) {
			tObj.forEachRow(function(id){
				if(sObj.cells(chkValue[i], 0).getValue() == tObj.cells(id, 0).getValue()
				&& sObj.cells(chkValue[i], 1).getValue() == tObj.cells(id, 1).getValue()) {
					fDrag = false;
					fCheck = false;
				}
			});
		}
		if(fCheck === false) {
			/*dhtmlx.alert({
				type:"alert",
				title:"알림",
				ok:"확인",
				text:lang.admin.alert.searchManage1
			});*/
			alert(lang.admin.alert.searchManage1)
		}
	}
	return fDrag;
}

function gridFunction(){

	searchItemBaseGrid.attachEvent("onBeforeDrag", function(id) {
		drag_base_item = false;
		drag_user_item = true;
		drag_base_list = false;
		drag_user_list = false;

		drag_item_mode = "copy";

		return true;
	});
	searchItemGrid.attachEvent("onBeforeDrag", function(id) {
		drag_base_item = true;
		drag_user_item = true;
		drag_base_list = false;
		drag_user_list = false;

		drag_item_mode = "move";

		return true;
	});
	searchListBaseGrid.attachEvent("onBeforeDrag", function(id) {
		drag_base_item = false;
		drag_user_item = false;
		drag_base_list = false;
		drag_user_list = true;
		drag_copy_list = true;

		drag_list_mode = "copy";

		return true;
	});
	searchListGrid.attachEvent("onBeforeDrag", function(id) {
		drag_base_item = false;
		drag_user_item = false;
		drag_base_list = true;
		drag_user_list = true;
		drag_copy_list = false;

		drag_list_mode = "move";

		return true;
	});
	searchCopyListGrid.attachEvent("onBeforeDrag", function(id) {
		drag_base_item = false;
		drag_user_item = false;
		drag_base_list = true;
		drag_user_list = false;
		drag_copy_list = true;

		drag_list_mode = "move";

		return true;
	});

	searchItemBaseGrid.attachEvent("onDrag", function(sId, tId, sObj, tObj) {
		searchItemBaseGrid.dragContext.mode = "move";
		return drag_base_item;
	});
	searchItemBaseGrid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj) {
		searchItemBaseGrid.deleteRow(dId);
	});

	searchItemGrid.attachEvent("onDrag", function(sId, tId, sObj, tObj) {
		searchItemGrid.dragContext.mode = drag_item_mode;

		drag_user_item = someGridItemCheck(drag_user_item, sId, tId, sObj, tObj);

		return drag_user_item;
	});


	searchListBaseGrid.attachEvent("onDrag", function(sId, tId, sObj, tObj) {
		searchListBaseGrid.dragContext.mode = "move";
		return drag_base_list;
	});
	searchListBaseGrid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj) {
		searchListBaseGrid.deleteRow(dId);
	});

	searchListGrid.attachEvent("onDrag", function(sId, tId, sObj, tObj) {
		searchListGrid.dragContext.mode = drag_list_mode;

		drag_user_list = someGridItemCheck(drag_user_list, sId, tId, sObj, tObj);

		return drag_user_list;
	});

	searchCopyListGrid.attachEvent("onDrag", function(sId, tId, sObj, tObj) {
		searchCopyListGrid.dragContext.mode = drag_list_mode;

		drag_copy_list = someGridItemCheck(drag_copy_list, sId, tId, sObj, tObj);

		return drag_copy_list;
	});
}

function formFunction(){
	// 저장 버튼 클릭시 기존껄로 대체 ..
/*	$("#searchSave").click(function(){

		var itemParamValue = new Array();
		var listParamValue = new Array();
		var authyCode = $(".group_name_wrap_active").attr("level-code")

		for(var i = 0; i < searchItemGrid.getRowsNum(); i++) {
			if (searchItemGrid.cells2(i,3).isChecked())
				itemParamValue.push(searchItemGrid.cells2(i, 2).getValue());
		}
		for(var i = 0; i < searchListGrid.getRowsNum(); i++) {
			if (searchListGrid.cells2(i,3).isChecked())
				listParamValue.push(searchListGrid.cells2(i, 2).getValue());
		}

		if(itemParamValue.length > 0){
			var szParamValue = itemParamValue.join("|");
			var param = "proc=apply&userId="+authyCode+"&type=item&columns="+szParamValue;

			$.ajax({
				url: contextPath+"/search_customize_proc.do?"+param,
				data: {},
				type: "GET",
				dataType: "json",
				// 아이템 그리드 적용 완료 되고, 리스트 그리드 적용

				success: function(jRes) {
					if(listParamValue.length > 0){
						var szParamValue = listParamValue.join("|");
						var param = "proc=apply&userId="+authyCode+"&type=list&columns="+szParamValue;

						$.ajax({
							url: contextPath+"/search_customize_proc.do?"+param,
							data: {},
							type: "GET",
							dataType: "json",
							success: function(jRes) {
								clickEventSearch(authyCode)
							}
						})
					}else{
						clickEventSearch(authyCode)
					}
				}
			})
		}


	});*/

	$("#searchSave").click(function(){
		saveSearchInfo();
	});

	// 초기화 버튼 클릭시
	$("#searchReset").click(function(){
		var authyCode = $(".group_name_wrap_active").attr("level-code")
		clickEventSearch(authyCode)
	});
}

// 조회 조건 저장 한방에...처리
function saveSearchInfo(){
	var level = $(".group_name_wrap_active").attr("level-code");
	itemType = "item";
	gridObj = searchItemGrid;

	var arParamValue = new Array();

	if(itemType.length > 0) {

		for(var i = 0; i < gridObj.getRowsNum(); i++) {
			arParamValue.push(gridObj.cells2(i, 1).getValue());
		}

		if (arParamValue.indexOf("r_counsel_result_mgcode") > -1){
			if (arParamValue.indexOf("r_counsel_result_bgcode") == -1){
				alert(lang.admin.alert.searchManage3)
				return;
			}
		}

		if (arParamValue.indexOf("r_counsel_result_sgcode") > -1){
			if (arParamValue.indexOf("r_counsel_result_mgcode") == -1){
				alert(lang.admin.alert.searchManage2)
				return;
			}
		}

		if(arParamValue.length > 0){
			var szParamValue = arParamValue.join("|");
			var param = "proc=apply&userId="+level+"&type="+itemType+"&columns="+szParamValue;

			window.dhx4.ajax.post(contextPath+"/search_customize_proc.do",param,function(response){
				var rst = window.dhx4.s2j(response.xmlDoc.responseText);
				var msg = lang.admin.alert.searchManage5
				if(rst == null || rst.result == "0") {
					msg = lang.admin.alert.searchManage4
				}else{
					itemType = "list";
					gridObj = searchListGrid;

					var arParamValue = new Array();

					if(itemType.length > 0) {
						for(var i = 0; i < searchListGrid.getRowsNum(); i++) {
							arParamValue.push(searchListGrid.cells2(i, 1).getValue());
						}

						if(arParamValue.length > 0){
							var szParamValue = arParamValue.join("|");
							var param = "proc=apply&userId="+level+"&type="+itemType+"&columns="+szParamValue;

							window.dhx4.ajax.post(contextPath+"/search_customize_proc.do",param,function(response){
								var rst = window.dhx4.s2j(response.xmlDoc.responseText);
								var msg = lang.admin.alert.searchManage5
								if(rst == null || rst.result == "0") {
									msg = lang.admin.alert.searchManage4
								}else {
									if (dataCopy == "Y" && dataCopyAll == "N") {
										itemType = "copy";
										gridObj = searchCopyListGrid;
	
										var arParamValue = new Array();
	
										if(itemType.length > 0) {
											for(var i = 0; i < searchCopyListGrid.getRowsNum(); i++) {
												arParamValue.push(searchCopyListGrid.cells2(i, 1).getValue());
											}
	
											if(arParamValue.length > 0){
												var szParamValue = arParamValue.join("|");
												var param = "proc=apply&userId="+level+"&type="+itemType+"&columns="+szParamValue;
	
												window.dhx4.ajax.post(contextPath+"/search_customize_proc.do",param,function(response){
													var rst = window.dhx4.s2j(response.xmlDoc.responseText);
													var msg = lang.admin.alert.searchManage5
													if(rst == null || rst.result == "0") {
														msg = lang.admin.alert.searchManage4
													}
												});
											} else {
												alert(lang.admin.alert.searchManage6)
											}
										} else {
											alert(lang.admin.alert.searchManage6)
										}
									}	
									alert(msg);
								}
								/*dhtmlx.alert({
									type:"alert",
									title:"알림",
									ok:"확인",
									text:msg
								});*/
							});
						} else {
							alert(lang.admin.alert.searchManage6)
							/*dhtmlx.alert({
								type:"alert",
								title:"알림",
								ok:"확인",
								text:lang.admin.alert.searchManage6
							});*/
						}
					} else {
						alert(lang.admin.alert.searchManage6)
						/*dhtmlx.alert({
							type:"alert",
							title:"알림",
							ok:"확인",
							text:lang.admin.alert.searchManage6
						});*/
					}
				}
				/*dhtmlx.alert({
					type:"alert",
					title:"알림",
					ok:"확인",
					text:msg
				});*/
			});

		} else {
			alert(lang.admin.alert.searchManage6)
			/*dhtmlx.alert({
				type:"alert",
				title:"알림",
				ok:"확인",
				text:lang.admin.alert.searchManage6
			});*/
		}
	} else {
		alert(lang.admin.alert.searchManage6)
		/*dhtmlx.alert({
			type:"alert",
			title:"알림",
			ok:"확인",
			text:lang.admin.alert.searchManage6
		});*/
	}
}

// 권한 리스트 클릭 시
function clickEventSearch(authyCode){
	//searchItemBaseGrid.clearAndLoad(contextPath+"/customize_info.xml?type=item");
	searchItemGrid.clearAndLoad(contextPath+"/customize_info.xml?type=item&userId="+authyCode);
	//searchListBaseGrid.clearAndLoad(contextPath+"/customize_info.xml?type=list");
	searchListGrid.clearAndLoad(contextPath+"/customize_info.xml?type=list&userId="+authyCode);
	searchCopyListGrid.clearAndLoad(contextPath+"/customize_info.xml?type=copy&userId="+authyCode);
}

// 스크린 사용자 관리 로드
function searchManageLoad() {

	/*조회폼 설정 그리드*/
	searchItemBaseGrid = _createGrid("searchItemBaseGrid", "", "customize_info","?type=item","",0,[],[], true,true);
	searchItemGrid = _createGrid("searchItemGrid", "", "customize_info","?type=item&userId="+userLevel,"",0,[],[] , true,false);
	/*조회 그리드 컬럼 설정 그리드*/
	searchListBaseGrid = _createGrid("searchListBaseGrid", "", "customize_info","?type=list","",0,[],[], true,true);
	searchListGrid = _createGrid("searchListGrid", "", "customize_info","?type=list&userId="+userLevel,"",0,[],[], true,false);

	searchCopyListGrid = _createGrid("searchCopyListGrid", "", "customize_info","?type=copy&userId="+userLevel,"",0,[],[], true,false);
	
	gridFunction();
	formFunction();
	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		$('#searchSave').hide();
	}
}