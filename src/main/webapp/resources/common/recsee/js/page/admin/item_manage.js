// 전역변수 설정
var griditemManage; // 그리드
var chkval;//콤보박스 변경전 값
var idToSave=[];//저장위한 배열
addLoadEvent(itemManageLoad);

function itemManageLoad() {
	griditemManage = itemManageGridLoad();
	formFunction();
	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn!= 'Y') {
		$('#btnitemAdd').hide();
	}

	if(delYn != 'Y') {
		$('#btnitemDel').hide();
	}
}

// 채널관리 로드
function itemManageGridLoad() {
    // 채널관리 Grid
	griditemManage = new dhtmlXGridObject("griditemManage");
	griditemManage.setIconsPath(recseeResourcePath + "/images/project/dhxgrid_web/tree/");
	griditemManage.setImagePath(recseeResourcePath + "/images/project/");
	griditemManage.enablePreRendering(50);
	griditemManage.enableColumnAutoSize(false);
	griditemManage.enablePreRendering(50);
	griditemManage.enableTreeCellEdit(false);
    griditemManage.setSkin("dhx_web");
	griditemManage.init();
	griditemManage.load(contextPath+"/itemManage.xml", function(){

		$(window).resize();
		griditemManage.setSizes();
		
		$(window).resize(function() {
			griditemManage.setSizes();
		});
		
		$('tbody').on("click",'.add_item', function(){
			$(this).parent().prev().click();
			var id= griditemManage.getSelectedId();
			var targetType=griditemManage.cells(id,1).getValue();

			if(!griditemManage.getOpenState(id)){
				addItemCombo(id,targetType);
				griditemManage.openItem(id)
			}else{
				addItemCombo(id,targetType);
			}
			
		});
		$('tbody').on("click",'.icon_btn_trash_gray', function(){
			$(this).parent().click();
			if((griditemManage.cells(griditemManage.getSelectedId(),0).cell.innerHTML).search('select')>0){
				griditemManage.deleteRow(griditemManage.getSelectedId());	
				for(var i=0; i<idToSave.length;i++){
					if(idToSave.rowId==griditemManage.getSelectedId())
						idToSave.splice(i,1);
				}
			}else{
				deletItem(griditemManage.getSelectedId());
			}
		});	
		
		
		$('tbody').on("click",'.selectItemCombo', function(){
			//chkval=$(this).find("option:selected").text();
			chkval=$(this).children('option:selected').attr('id');
		});
		
		$('tbody').on("change",'.selectItemCombo', function(){
			var chk=true;
			var targetVal=$(this).children('option:selected').attr('id');
			$(this).parent().click();
			var	parentId= griditemManage.getParentId(griditemManage.getSelectedId());
			var childCount=griditemManage.hasChildren(parentId);
			
			for(var i=0;i<childCount;i++){
				id=griditemManage.getChildItemIdByIndex(parentId,i);
				var tt=griditemManage.cells(id,3).getValue();
				if(targetVal==tt){
					alert(lang.admin.alert.itemManage1);
					chk=false;
					break;
				}
				
			}
			if(chk){
				griditemManage.cells(griditemManage.getSelectedId(),3).setValue($(this).children('option:selected').attr('id'));
				griditemManage.cells(griditemManage.getSelectedId(),4).setValue($(this).val());
			}else{
				$(this).find("option[id='"+chkval+"']").prop("selected","true");
			}
		});	
		
	}, 'xml')

	
	griditemManage.attachEvent("onRowSelect" , function (id, ind){
		return true;
	});
	
	
    ui_controller();
       
    
    return griditemManage;
}

function formFunction() {
	$("#btnItemAdd").click(function() {
		if(idToSave.length>0)
			saveItem();
		else
			alert(lang.admin.alert.itemManage2);
	});
}


function addItemCombo(targetId,targetType) {
	
	var dataStr = {
			"hasChildren"	: griditemManage.hasChildren(targetId)
	    ,   "targetId"		: targetId
	    ,   "targetType"	: targetType
	};
	
	$.ajax({
		url:contextPath+"/addItemCombo.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				var resultArr=[];
				var parentIndex=griditemManage.getRowIndex(targetId);
				var id;
				var size=griditemManage.hasChildren(targetId);
				var newId=targetId+'child'+(new Date).valueOf();
				for(var i=0;i<size;i++){
					id=griditemManage.getChildItemIdByIndex(targetId,i);
					var tt=griditemManage.cells(id,3).getValue();
					resultArr.push(tt);
				}
				
				var index=0;
				var dup=[];
				
				for(var i=1;i<=Number(jRes.resData.countItem); i++){
					dup.push(i);
				}
				
				for(var i=0; i<resultArr.length;i++){
					for(var y=0;y<dup.length;y++){
						if(Number(resultArr[i])==dup[y]){
							dup.splice(y,1);
							break;
						}
					}
				}
				griditemManage.addRow(newId,[jRes.resData.jResDate,,,,,"<button class='ui_btn_white icon_btn_trash_gray'></button>"],0,targetId);
				//관계항목 리스트 입력
				griditemManage.cells(newId,3).setValue(dup[0]);
				//콤보박스 변경 - 위에 입력한 값의 id로
				$(griditemManage.cells(newId,0).cell).find("select option[id='"+griditemManage.cells(newId,3).getValue()+"']").attr('selected','true');
				//임계점
				griditemManage.cells(newId,4).setValue($(griditemManage.cells(newId,0).cell).find("select").val());
				
				var obj=new Object();
				obj.rowId=newId;
				obj.parentId=griditemManage.getParentId(newId);
				
				idToSave.push(obj);
			} else if(jRes.success == "N" &&jRes.resData.msg=="full list"){
				alert(lang.admin.alert.itemManage3)
			}else {
				alert(lang.admin.alert.itemManage4);
			}
		}
	});
}

function deletItem(targetId) {
	if(confirm(lang.admin.alert.itemManage8)){
		var dataStr = {
			      "targetId"		: griditemManage.cells(targetId,6).getValue()
			};
		$.ajax({
			url:contextPath+"/delItemCombo.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					alert(lang.admin.alert.itemManage7);
					griditemManage.deleteRow(targetId);
				}else {
					alert(lang.admin.alert.itemManage6);
				}
			}
		});
	}
}

function saveItem() {
	var dataStr = [];
	for(var i=0; i<idToSave.length;i++){
		var obj= new Object;
		obj.targetId=griditemManage.cells(idToSave[i].parentId,2).getValue();
		obj.itemId=griditemManage.cells(idToSave[i].rowId,3).getValue();
		dataStr.push(obj);
	}
	var jsonData=JSON.stringify(dataStr);
	$.ajax({
		url:contextPath+"/saveItem.do",
		data:{"jsonData":jsonData},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				idToSave=[];
				var openRow=[];
				for(var i=1; i<=griditemManage.getRowsNum(); i++){
					if(griditemManage.getOpenState(i)){
						openRow.push(i);
					}
				}
				griditemManage.clearAndLoad(contextPath+"/itemManage.xml", function() {
					for(var i=0;i<griditemManage.getRowsNum();i++){
						griditemManage.openItem(openRow[i]);
					}
				});
				//griditemManage.clearAndLoad(contextPath + "/itemManage.xml", function(){ }, "xml");
				alert(lang.admin.alert.itemManage5);
			}else {
				alert(lang.admin.alert.itemManage4);
			}
		}
	});
}