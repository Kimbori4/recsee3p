/**
 * 
 */

var cateGorySettingGrid;
var keywordSettingGrid;
var categoryKeywordListGrid;



$(document).ready(function(){
	// Settings...
	CategorySettingGridLoad();
	KeywordSettingGridLoad();
	categoryKeywordListGridLoad();
	
	// 카테고리 세팅
	$("#categorySetting").click(function(){
		
		layer_popup('#categoryPopup');
		layer_popup('#categoryListPopup');
		
		var query = "type=category"
			
		cateGorySettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
			ui_controller();
		}, 'xml');
	});
	
	// 카테고리 검색
	$("#searchCategoryList").click(function(){
		var query = "type=category&categoryName=" + $("#categoryInput").val()
			
		cateGorySettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
			ui_controller();
		}, 'xml');
	});
	
	$("#categoryInput").keydown(function(e){
		if(e.keyCode == 13)
			$("#searchCategoryList").trigger('click');
	})
	
	// 카테고리 등록
	$("#InsertCategoryBtn").click(function(){
		layer_popup('#CategoryInsertPopup');
		$("#InsertCategoryName").val("");
		
		$.ajax({
			url : contextPath + "/getCategoryOrKeyword.do",
			data : {
						type : 'keyword'
			},
			type : "POST",
			async : false,
			cache: false,
			success : function(jRes){
				var res = jRes.resData.data;
				
				var str = "";
				for(var i = 0; i < res.length; i++){
					if(res[i] != null)
						str += "<option value='"+res[i].rKeywordName+"'>" + res[i].rKeywordName + "</option>"
				}
				$("#InsertKeywordToCategory").html(str);
				
				$("#InsertKeywordToCategory").attr("multiple","multiple").css("width","230").select2({
						placeholder: ''
					,	allowClear: true
				});
				$("#InsertKeywordToCategory").val('')
				$("#InsertKeywordToCategory").select2()
				
			},
			error : function(){
				
			}
			
		});
		

	})
	
	$("#InsertCategoryBtnInPopup").click(function(){
		
		if($("#InsertCategoryName").val() == ""){
			alert(top.lang.statistics.js.alert.msg47);
			$("#InsertCategoryName").focus();
			return false;
		}
		
		var keywords = ($("#InsertKeywordToCategory").select2('val') == null) ? "" : $("#InsertKeywordToCategory").select2('val').toString();
		
 		$.ajax({
			url : contextPath + "/insertCategoryOrKeyword.do",
			data : {
					type : 'category'
				,	name : $("#InsertCategoryName").val()
				,	keyword : keywords
			},
			type : "POST",
			async : false,
			cache: false,
			beforeSend : function(){
			},
			complete : function(){
			},
			success : function(jRes){
				
				if(jRes.success == "Y"){
					alert(top.lang.statistics.js.alert.msg48);
					
					layer_popup_close("#CategoryInsertPopup");
					
					var query = "type=category&categoryName=" + $("#categoryInput").val()
					
					cateGorySettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
						ui_controller();
					}, 'xml');
					
					$("#InsertCategoryName").val("");
				}
				
			},
			error : function(){
				alert(top.lang.statistics.js.alert.msg45);
			}
			
		})
	})
	
	// 카테고리 수정
	$("#updateCategoryBtnInPopup").click(function(){
		
		$.ajax({
			url : contextPath + "/updateCategoryOrKeyword.do",
			data : {
					type : 'category'
				,	id : $("#updateCategoryId").val()
				,	name : $("#updateCategoryName").val()
			},
			type : "POST",
			async : false,
			cache: false,
			beforeSend : function(){
			},
			complete : function(){
			},
			success : function(jRes){
				
				if(jRes.success == "Y"){
					alert(top.lang.statistics.js.alert.msg49);
					
					layer_popup_close("#CategoryUpdatePopup");
					
					var query = "type=category&categoryName=" + $("#categoryInput").val()
					
					cateGorySettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
						ui_controller();
					}, 'xml');
				}
				
			},
			error : function(){
				
			}
			
		});
	})
	
	
	// 키워드 세팅
	$("#keywordSetting").click(function(){
		layer_popup('#keywordPopup');
		
		var query = "type=keyword"
			
		keywordSettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
			ui_controller();
		}, 'xml');
	});
	
	// 키워드 검색
	$("#searchKeywordList").click(function(){
		var query = "type=keyword&keywordName=" + $("#keywordInput").val()
			
		keywordSettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
			ui_controller();
		}, 'xml');
	});
	
	$("#keywordInput").keydown(function(e){
		if(e.keyCode == 13)
			$("#searchKeywordList").trigger('click');
	})
	
	// 키워드 등록
	$("#InsertKeywordBtn").click(function(){
		layer_popup('#KeywordInsertPopup');
	})
	
	$("#InsertKeywordBtnInPopup").click(function(){
		if($("#InsertKeywordName").val() == ""){
			alert(top.lang.statistics.js.alert.msg50);
			$("#InsertKeywordName").focus();
			return false;
		}
				
		$.ajax({
			url : contextPath + "/insertCategoryOrKeyword.do",
			data : {
					type : 'keyword'
				,	name : $("#InsertKeywordName").val()
			},
			type : "POST",
			async : false,
			cache: false,
			beforeSend : function(){
			},
			complete : function(){
			},
			success : function(jRes){
				
				if(jRes.success == "Y"){
					alert(top.lang.statistics.js.alert.msg51);
					
					layer_popup_close("#KeywordInsertPopup");
					
					var query = "type=keyword"
						
					keywordSettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
						ui_controller();
					}, 'xml');
					
					$("#InsertKeywordName").val("")
				}
				
			},
			error : function(){
				alert(top.lang.statistics.js.alert.msg45);
				$("#InsertKeywordName").focus();
			}
			
		})
	});
	
	// 키워드 목록에서의 키워드 등록
	$("#InsertCategoryBtnInList").click(function(){
		if($("#InsertKeywordNameInList").select2('val') == null){
			alert("키워드 명를 선택해주세요.");
			return false;
		}
				
		$.ajax({
			url : contextPath + "/insertCategoryOrKeyword.do",
			data : {
					type : 'keyword'
				,   id : $("#insertCategoryId").val()
				,	keyword : $("#InsertKeywordNameInList").select2('val').toString()
			},
			type : "POST",
			async : false,
			cache: false,
			beforeSend : function(){
			},
			complete : function(){
			},
			success : function(jRes){
				
				if(jRes.success == "Y"){
					alert(top.lang.statistics.js.alert.msg51);
					
					var query = "type=keywordDetail&id="+ $("#insertCategoryId").val()
						
					categoryKeywordListGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
						ui_controller();
					}, 'xml');
					
				}
				
			},
			error : function(){
				alert(top.lang.statistics.js.alert.msg45);
				$("#InsertKeywordName").focus();
			}
			
		})
	})
	
})
	
// 카테고리 관리 그리드
function CategorySettingGridLoad(){
//	cateGorySettingGrid = drawSettingGrid("category", "categorySettingGrid", "pagingcategorySettingGrid");
	
	var query = "&type=category";
	
	cateGorySettingGrid = new dhtmlXGridObject("categorySettingGrid");
	cateGorySettingGrid.setIconsPath(recseeResourcePath + "/images/project/");
	cateGorySettingGrid.setImagePath(recseeResourcePath + "/images/project/");
	cateGorySettingGrid.i18n.paging = i18nPaging[locale];
	cateGorySettingGrid.enablePaging(true, 20, 5, "pagingcategorySettingGrid", true);
	cateGorySettingGrid.setPagingWTMode(true,true,true,[20,40,240]);
	cateGorySettingGrid.setPagingSkin("toolbar", "dhx_web");
	cateGorySettingGrid.enableColumnAutoSize(false);
	cateGorySettingGrid.setSkin("dhx_web");
	cateGorySettingGrid.init();
	
	cateGorySettingGrid.load(contextPath + "/statistics/keywordCategorySetting.xml?head=true" + query, function() {
		try{
			//cateGorySettingGrid.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ cateGorySettingGrid.i18n.paging.results+'    '+ cateGorySettingGrid.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
			cateGorySettingGrid.aToolBar.setWidth("total",80)
		}catch(e){console.error(e)}

		cateGorySettingGrid.aToolBar.hideItem("perpagenum");

		cateGorySettingGrid.aToolBar.setMaxOpen("pages", 5);
		cateGorySettingGrid.attachEvent("onXLS", function(){
			progress.on()
		});
		
		//그래프그릴때 아직 그리드가 파싱되지 않아서 오류 발생
		cateGorySettingGrid.attachEvent("onPaging", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});
		
		cateGorySettingGrid.attachEvent("onEditCell", function(stage,id,ind,nValue,oValue){
			
			if (stage == 2){
				var modifyId = this.cells(id,2).getValue();
				var modifyVal = this.cells(id,0).getValue();
				
				$.ajax({
					url : contextPath + "/updateCategoryOrKeyword.do",
					data : {
							type : 'category'
						,	id : modifyId
						,	name : modifyVal
					},
					type : "POST",
					async : false,
					cache: false,
					beforeSend : function(){
					},
					complete : function(){
					},
					success : function(jRes){
						
						if(jRes.success == "Y"){
							alert(top.lang.statistics.js.alert.msg49);
							
							var query = "type=category&categoryName=" + $("#categoryInput").val()
							
							cateGorySettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
								ui_controller();
							}, 'xml');
						}
						
					},
					error : function(){
						
					}
					
				});
				return false;
			}else{
				return true;
			}
			
		});
		
		cateGorySettingGrid.attachEvent("onRowSelect", function(id,ind){
		    return;
		});
		
		cateGorySettingGrid.attachEvent("onRowDblClicked", function(id,ind){
			return true;
		});

		cateGorySettingGrid.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum()){
				return false;
			}
			return true;
		});
		// 소팅 이벤트 커스텀
		cateGorySettingGrid.attachEvent("onBeforeSorting", function(ind){
				var a_state = this.getSortingState()

				var direction = "asc"
				if(nowSortingColumn==ind)
					direction = ((a_state[1] == "asc") ? "desc" : "asc");

				var columnId = this.getColumnId(ind);

				formData(columnId, direction)

				this.setSortImgState(true,ind,direction)
				var a_state = this.getSortingState()
				nowSortingColumn = a_state[0];

		});

		cateGorySettingGrid.attachEvent("onXLE", function(grid_obj,count){
			try{
				var setResult = '<div style="width: 100%; text-align: center;">'+ cateGorySettingGrid.i18n.paging.results+ cateGorySettingGrid.getRowsNum()+cateGorySettingGrid.i18n.paging.found+'</div>'
				cateGorySettingGrid.aToolBar.setItemText("total", setResult)
			}catch(e){console.error(e)}
			progress.off();
		})

		ui_controller();

		$(window).resize();
		$(window).resize(function() {
			cateGorySettingGrid.setSizes();
		});

	}, 'xml');

	return cateGorySettingGrid;
}

// 키워드 관리 그리드
function KeywordSettingGridLoad(){
	keywordSettingGrid = drawSettingGrid("keyword", "keywordSettingGrid", "pagingkeywordSettingGrid");
}

// 카테고리 키워드 목록 그리드
function categoryKeywordListGridLoad(){
	categoryKeywordListGrid = drawSettingGrid("keywordDetail", "categoryKeywordListGrid", "pagingcategoryKeywordListGrid");
}

function drawSettingGrid(target, id, pagingId){
	var query = "&type=" + target;
		
	keywordTable = new dhtmlXGridObject(id);
	keywordTable.setIconsPath(recseeResourcePath + "/images/project/");
	keywordTable.setImagePath(recseeResourcePath + "/images/project/");
	keywordTable.i18n.paging = i18nPaging[locale];
	keywordTable.enablePaging(true, 20, 5, pagingId, true);
	keywordTable.setPagingWTMode(true,true,true,[20,40,240]);
	keywordTable.setPagingSkin("toolbar", "dhx_web");
	keywordTable.enableColumnAutoSize(false);
	keywordTable.setSkin("dhx_web");
	keywordTable.init();
	
	keywordTable.load(contextPath + "/statistics/keywordCategorySetting.xml?head=true" + query, function() {
		try{
			keywordTable.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ keywordTable.i18n.paging.results+'    '+ keywordTable.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
			keywordTable.aToolBar.setWidth("total",80)
		}catch(e){console.error(e)}

		keywordTable.aToolBar.hideItem("perpagenum");

		keywordTable.aToolBar.setMaxOpen("pages", 5);
		keywordTable.attachEvent("onXLS", function(){
			progress.on()
		});
		
		if(target == "category"){
			alert('123123')
		}
		
		/*gridCallUser.attachEvent("onDataReady", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});*/
		//그래프그릴때 아직 그리드가 파싱되지 않아서 오류 발생
		keywordTable.attachEvent("onPaging", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});

		keywordTable.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum()){
				return false;
			}
			return true;
		});
		// 소팅 이벤트 커스텀
		keywordTable.attachEvent("onBeforeSorting", function(ind){
				var a_state = this.getSortingState()

				var direction = "asc"
				if(nowSortingColumn==ind)
					direction = ((a_state[1] == "asc") ? "desc" : "asc");

				var columnId = this.getColumnId(ind);

				formData(columnId, direction)

				this.setSortImgState(true,ind,direction)
				var a_state = this.getSortingState()
				nowSortingColumn = a_state[0];

		});

		keywordTable.attachEvent("onXLE", function(grid_obj,count){
			try{
				var setResult = '<div style="width: 100%; text-align: center;">'+ keywordTable.i18n.paging.results+ keywordTable.getRowsNum()+keywordTable.i18n.paging.found+'</div>'
				keywordTable.aToolBar.setItemText("total", setResult)
			}catch(e){console.error(e)}
			progress.off();
		})

		ui_controller();

		$(window).resize();
		$(window).resize(function() {
			keywordTable.setSizes();
		});

	}, 'xml');

	return keywordTable;
}

function updateCategory(t){
	var id = $(t).data("id");
	var name = $(t).data("name");
	
	$("#updateCategoryId").val(id);
	$("#updateCategoryName").val(name);
	
	layer_popup('#CategoryUpdatePopup');
}


function ListCategory(t){
	var id = $(t).data("id");
	var name = $(t).data("name");

	$("#insertCategoryId").val(id);
	var query = "id=" + id + "&type=keywordDetail";
	
	categoryKeywordListGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
		ui_controller();
	}, 'xml');
	
	$.ajax({
		url : contextPath + "/getCategoryOrKeyword.do",
		data : {
					type : 'keyword'
		},
		type : "POST",
		async : false,
		cache: false,
		success : function(jRes){
			var res = jRes.resData.data;
			
			var str = "";
			for(var i = 0; i < res.length; i++){
				if(res[i] != null)
					str += "<option value='"+res[i].rKeywordName+"'>" + res[i].rKeywordName + "</option>"
			}
			$("#InsertKeywordNameInList").html(str);
			
			$("#InsertKeywordNameInList").attr("multiple","multiple").css("width","230").select2({
					placeholder: ''
				,	allowClear: true
			});
			$("#InsertKeywordNameInList").val('')
			$("#InsertKeywordNameInList").select2()
			
		},
		error : function(){
			
		}
		
	});
	
	
	layer_popup('#categoryListPopup');
	
	
	
}


function deleteCategory(t){
	var id = $(t).data("id");
	
	if(!confirm(top.lang.statistics.js.alert.msg42)){
		return false;
	}
	
	$.ajax({
		url : contextPath + "/removeCategoryOrKeyword.do",
		data : {
				type : 'category'
			,	id : id
		},
		type : "POST",
		async : false,
		cache: false,
		beforeSend : function(){
		},
		complete : function(){
			
		},
		success : function(jRes){
			
			if(jRes.success == "Y"){
				alert(top.lang.statistics.js.alert.msg44);
				
				var query = "type=category&categoryName=" + $("#categoryInput").val()
				
				cateGorySettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
					ui_controller();
				}, 'xml');
				
				// 키워드도 클리어
				categoryKeywordListGrid.clearAll();
			}
			
		},
		error : function(){
			
		}
		
	})
	
}

function deleteKeyword(t){
	var id = $(t).data("id");
	var name = $(t).data("name");
	
	if(!confirm(top.lang.statistics.js.alert.msg42)){
		return false;
	}
	
	var dataset = {
			type : 'keyword'
		,	name : name
	}
	
	if(id != 0)
		dataset.id = id;
	
	$.ajax({
		url : contextPath + "/removeCategoryOrKeyword.do",
		data : dataset,
		type : "POST",
		async : false,
		cache: false,
		beforeSend : function(){
		},
		complete : function(){
			
		},
		success : function(jRes){
			
			if(jRes.success == "Y"){
				alert(top.lang.statistics.js.alert.msg43);
				
				var query = "type=keyword&keywordName=" + $("#keywordInput").val()
				
				keywordSettingGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
					ui_controller();
				}, 'xml');
				
				
				var query = "id=" + $("#insertCategoryId").val() + "&type=keywordDetail";
				
				categoryKeywordListGrid.clearAndLoad(contextPath+'/statistics/keywordCategorySetting.xml?' + query , function() {
					ui_controller();
				}, 'xml');
			}
			
		},
		error : function(){
			
		}
		
	})
}