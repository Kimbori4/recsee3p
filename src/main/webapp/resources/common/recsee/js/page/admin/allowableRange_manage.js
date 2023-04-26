// 전역변수
var userManageGrid; // 그리드
var treeView;
var check=0;
var addList = new Array;
var deleteList = new Array;

//	로드 함수
function allowManageLoad(){

	treeView = new dhtmlXTreeObject("treeViewAgent","100%","100%",0);
	treeView.attachEvent("onXLS", function(){
		check=check+1;
		progress.on()
	});
	treeView.setImagePath("../resources/component/dhtmlxSuite/skins/skyblue/imgs/dhxtree_skyblue/");
	treeView.enableThreeStateCheckboxes(true);
	treeView.enableSmartXMLParsing(true);
	treeView.load(contextPath+"/AgentTreeView.xml?aUser=N");
	treeView.attachEvent("onXLE", function(grid_obj,count){
		check=check-1;
		if(check==0){
			progress.off()
		}
	});


	userManageGrid = createGrid("userManageGrid", "allowable_list","?header=true",[],[]);
	
	gridFunction();
	formFunction();
	authyLoad();

}

// 그리드 생성
function createGrid(objGrid, url, strData, hiddenColumn, showColumn){
	var tempGridName = objGrid;
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	objGrid.attachEvent("onXLS", function(){
		check=check+1;
		progress.on()
	});
	// 파싱완료
	objGrid.attachEvent("onXLE", function(grid_obj,count){
		ui_controller();
		check=check-1;
		if(check==0)
			progress.off();
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});

	});
	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData),function(){

		//체크박스 전체 선택
		objGrid.attachEvent("onHeaderClick",function(ind, obj){
    		if(ind == 0 && obj.type == "click") {
    			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
    				this.setCheckedRows(0, 1);
    				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
    			} else {
    				this.setCheckedRows(0, 0);
    				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
    			}
    		} else {
    			return true;
    		}
    	});
    		
		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		ui_controller();
	});
	
	return objGrid;
}


//그리드 적용 함수
function gridFunction(){
}

//폼 함수
function formFunction(){

	// 허용 범위 추가 메인 버튼
	$("#allowableAddBtn").click(function(){
		$("#addJuri").find("p").text(lang.admin.allowableRange.button.rangeAdd)/*허용 범위 추가*/
		$('#authyName').val("");

		$("#modifyGroupBtn").hide();
		$("#addGroupBtn").text(lang.admin.allowableRange.button.rangeAdd)/*허용 범위 추가*/
		$("#addGroupBtn").show();
		layer_popup('#addJuri');
	});
	// 허용 범위 수정 메인 버튼
	$("#allowableModifyBtn").click(function(){
		$("#addJuri").find("p").text(lang.admin.allowableRange.button.rangeModi)/*허용 범위 수정*/
		$('#authyName').val($(".group_name_wrap_active").text());

		$("#modifyGroupBtn").text(lang.admin.allowableRange.button.rangeModi)/*허용 범위 수정*/
		$("#modifyGroupBtn").show();
		$("#addGroupBtn").hide();
		layer_popup('#addJuri');
	});
	// 허용 범위 삭제 메인 버튼
	$("#allowableDeleteBtn").click(function(){
		var level = $(".group_name_wrap_active").attr("level-code");
		var param = "proc=delete&levelCode=" + level

		if($(".group_name_wrap_active").length == 0){
			alert(lang.admin.alert.allowableRangeManage2)/*삭제할 허용범위를 먼저 선택 해 주세요!*/
			return
		}


		if(confirm(lang.admin.alert.allowableRangeManage1)){/*선택하신 허용범위를 정말 삭제 하시겠습니까?*/
			$.ajax({
				url:contextPath+"/allowable_proc.do",
				data:param,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB에 조회한 계정이 있으면
					if(jRes.success == "Y") {
						// 불러온 옵션 추가
						alert(lang.admin.alert.allowableRangeManage3)/*허용범위 삭제가 완료 되었습니다.*/
						authyLoad();
						layer_popup_close()
					}else{
						alert(lang.admin.alert.allowableRangeManage4)/*허용범위 삭제에 실패 하였습니다.*/
					}
				}
			});
		}
	})


	// 허용 범위 추가 팝업 버튼
	$("#addGroupBtn").click(function(){
		var acesName = $("#authyName").val();

		if(!acesName){
			alert(lang.admin.alert.allowableRangeManage5)/*허용 범위 명을 입력해 주세요.*/
			$("#authyName").focus();
			return
		}

		var param = "proc=insert&levelName="+acesName;
		

		$.ajax({
			url:contextPath+"/allowable_proc.do",
			data:param,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					// 불러온 옵션 추가
					alert(lang.admin.alert.allowableRangeManage6)/*허용 범위 추가가 완료 되었습니다.*/
					authyLoad();
					layer_popup_close()
				}else{
					alert(lang.admin.alert.allowableRangeManage7)/*허용 범위 추가에 실패 하였습니다.*/
				}
			}
		});
	});
//
	// 허용 범위 수정 팝업 버튼
	$("#modifyGroupBtn").click(function(){

		var acesName = $("#authyName").val();
		var acesCode = $(".group_name_wrap_active").attr("level-code");

		if($(".group_name_wrap_active").length == 0){
			alert(lang.admin.alert.allowableRangeManage8)/*수정할 허용범위를 먼저 선택 해 주세요!*/
			return
		}else if(!acesName){
			alert(lang.admin.alert.allowableRangeManage5)/*허용범위명을 입력 해 주세요!*/
			$("#authyName").focus();
			return
		}


		param = "proc=modify&levelName="+acesName+"&levelCode="+acesCode;
		$.ajax({
			url:contextPath+"/allowable_proc.do",
			data:param,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					// 불러온 옵션 추가
					alert(lang.admin.alert.allowableRangeManage10)/*허용범위 수정이 완료 되었습니다.*/
					authyLoad();
					layer_popup_close()
				}else{
					alert(lang.admin.alert.allowableRangeManage11)/*허용범위 수정이 실패 하였습니다.*/
				}
			}
		});
	});
	
	// 선택 조직 추가 버튼 
	$("#rangeAddBtn").click(function(){
		var rowId = treeView.getSelected().split("_")[0];
		var bgName = "";
		var mgName = "";
		var sgName = "";
		var bgCode = "";
		var mgCode = "";
		var sgCode = "";
		var existValue ="";
		
		// 전체는 추가 안됨
		if(rowId!=null && rowId!=''&&rowId!='all'){
			var level = treeView.getLevel(treeView.getSelected());
			
			switch (level) {
			case 2:
				bgCode = treeView.getSelected().split("_")[0];
				bgName = treeView.getSelectedItemText()
				existValue = bgCode;
				break;
			case 3:
				mgCode = treeView.getSelected().split("_")[0];
				mgName = treeView.getSelectedItemText();
				bgCode = treeView.getParentId(treeView.getSelected());
				bgName = treeView.getItemText(bgCode)
				existValue = bgCode + "_" + mgCode;
				break;
			case 4:
				sgCode = treeView.getSelected().split("_")[0];
				sgName = treeView.getSelectedItemText()
				mgCode = treeView.getParentId(treeView.getSelected());
				bgCode = treeView.getParentId(treeView.getParentId(treeView.getSelected()));
				mgName = treeView.getItemText(mgCode)
				bgName = treeView.getItemText(bgCode)
				existValue = bgCode + "_" + mgCode + "_" + sgCode;
				break;
			}
			
			var existChk = false;
			for (var j = 0; j < addList.length; j++) {
				var tmpVal = addList[j][0] + "_" + addList[j][1] + "_" + addList[j][2]; 
				if (existValue = tmpVal) {
					existChk = true;
				}
			}
			
			if(!userManageGrid.doesRowExist(existValue) && !existChk){
				var groupList = ["",bgName, mgName, sgName,"<button class='ui_btn_white icon_btn_trash_gray' onclick='rangeDelete(\""+rowId+"\")'></button>"]
				userManageGrid.addRow(rowId,groupList);
				
				var listPush = true;
				for(i=0; i< deleteList.length; i++) {
					if(deleteList[i][0]==rowId){
						deleteList.splice(i,1);
						listPush = false;
					}
				}
				if(listPush){
					if(bgCode==""){
						addList.push([bgCode]);
					}else if(mgCode==""){
						addList.push([bgCode,mgCode]);
					}else{
						addList.push([bgCode,mgCode,sgCode]);
					}
				}
			}else{
				alert(lang.admin.alert.allowableRangeManage12);/*이미 추가된 조직입니다.*/
			}			
		}else{
			alert(lang.admin.alert.allowableRangeManage13);/*조직을 선택해 주세요.*/
		}
	})
	
	// 체크 조직 삭제 버튼 
	$("#checkedRangeDeleteBtn").click(function(){
		if(userManageGrid.getCheckedRows(0) != "") {
			var checkedRows = userManageGrid.getCheckedRows(0).split(",");
			for(i=0; i< checkedRows.length; i++) {
				if(addList.length==0){
					// 대,중,소그룹 분류
					var deleteValue = "";
					if(""==userManageGrid.cells(checkedRows[i],3).getValue()){
						if(""==userManageGrid.cells(checkedRows[i],2).getValue()){
							deleteValue = userManageGrid.cells(checkedRows[i],1).getValue();
							deleteList.push([deleteValue,'B']);
						}else{
							deleteValue = userManageGrid.cells(checkedRows[i],1).getValue() + "_" + userManageGrid.cells(checkedRows[i],2).getValue();
							deleteList.push([deleteValue,'M']);
						}
					}else{
						deleteValue = userManageGrid.cells(checkedRows[i],1).getValue() + "_" + userManageGrid.cells(checkedRows[i],2).getValue() + "_" + userManageGrid.cells(checkedRows[i],3).getValue();
						deleteList.push([deleteValue,'S']);
					}
				}else{
					var listPush = true;
					for(j=0; j< addList.length; j++) {
						if(addList[j][addList[j].length-1]==checkedRows[i]){
							addList.splice(j,1);
							listPush = false;
						}
					}
					if(listPush){
						if(""==userManageGrid.cells(checkedRows[i],3).getValue()){
							if(""==userManageGrid.cells(checkedRows[i],2).getValue()){
								deleteValue = userManageGrid.cells(checkedRows[i],1).getValue();
								deleteList.push([deleteValue,'B']);
							}else{
								deleteValue = userManageGrid.cells(checkedRows[i],1).getValue() + "_" + userManageGrid.cells(checkedRows[i],2).getValue();
								deleteList.push([deleteValue,'M']);
							}
						}else{
							deleteValue = userManageGrid.cells(checkedRows[i],1).getValue() + "_" + userManageGrid.cells(checkedRows[i],2).getValue() + "_" + userManageGrid.cells(checkedRows[i],3).getValue();
							deleteList.push([deleteValue,'S']);
						}
					}
				}
				userManageGrid.deleteRow(checkedRows[i]);
			}
		}else{
			alert(lang.admin.alert.allowableRangeManage14)/*삭제 하실 조직을 체크해 주세요.*/
		}
	})

	// 저장 버튼 
	$("#rangeSaveBtn").click(function(){
		
		

		$.ajax({
			url:contextPath+"/allowable_proc.do"+formToSerialize()+"&proc=save",
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					// 불러온 옵션 추가
					alert(lang.admin.alert.allowableRangeManage15)/*조직 변경이 완료 되었습니다.*/
					userManageGrid.clearAndLoad(contextPath+"/allowable_list.xml"+formToSerialize())
					addList = new Array;
					deleteList = new Array;
					layer_popup_close()
				}else{
					alert(lang.admin.alert.allowableRangeManage16)/*조직 변경에 실패 하였습니다.*/
				}
			}
		});
	})
}

//선택 조직 삭제 버튼 
function rangeDelete(rowId){
	var listPush = true;
	for(i=0; i< addList.length; i++) {
		if(addList[i][addList[i].length-1]==rowId){
			addList.splice(i,1);
			listPush = false;
		}
	}
	if(listPush){
		if(""==userManageGrid.cells(rowId,3).getValue()){
			if(""==userManageGrid.cells(rowId,2).getValue()){
				deleteList.push([rowId,'B']);
			}else{
				deleteList.push([rowId,'M']);
			}
		}else{
			deleteList.push([rowId,'S']);
		}
	}
	userManageGrid.deleteRow(rowId);
}

//권한 불러 오기
function authyLoad() {

	var $groupList = $('.group_list').find('.allowable_name');
	$groupList.empty();
	$.ajax({
		url: contextPath+"/getAllowableList.do",
		data: {},
		type: "POST",
		dataType: "json",
		success: function(jRes) {
			if(jRes.resData.msg=="authy list get"){
				var allowableList = jRes.resData.allowableList;
				// 허용범위 리스트 뿌리기
				for(i=0; i< allowableList.length; i++) {
					var allowableName = allowableList[i].levelName;
					var allowableCode = allowableList[i].levelCode;

					$groupList.append('<li><div class="group_name_wrap" level-code="'+allowableCode+'"><p class="icon_authy_common icon_authy_'+allowableCode+'">' + allowableName + '</p></div></li>');
					//마지막에 이벤트 붙여주기
					if(i==(allowableList.length -1)){
						allowableListEvent();
					}
				}
			}
		}
	});

}

function allowableListEvent(){
	var $listActivTarget = $('.group_name_wrap')
	// 첫 번 째 메뉴 기본 활성화
	$('.allowable_name li:nth-child(1) div').addClass('group_name_wrap_active');
	userManageGrid.clearAndLoad(contextPath+"/allowable_list.xml"+formToSerialize())
    // 클릭 시 Active 적용
	$listActivTarget.on("click", function(event){
		addList = new Array;
		deleteList = new Array;
        event.stopPropagation();
        // 선택사항 초기화
        $listActivTarget.each(function() {
            $(this).removeClass('group_name_wrap_active');
        });
        // Active 활성화
        $(this).addClass('group_name_wrap_active');

        // 허용 범위 그리드 호출
        userManageGrid.clearAndLoad(contextPath+"/allowable_list.xml"+formToSerialize())
    });
}


function formToSerialize(){
	
	var resultValue = "";
	
	
	if(treeView.getSelected()!=null && treeView.getSelected()!=''&&treeView.getSelected()!='all'){
		var level = treeView.getLevel(treeView.getSelected());

		switch (level) {
		case 2:
			var bgCode = treeView.getSelected().split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+bgCode;
			break;
		case 3:
			var mgCode = treeView.getSelected().split("_")[0];
			var bgCode = treeView.getParentId(treeView.getSelected()).split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+mgCode+"&bgCode="+bgCode;
			break;
		case 4:
			var sgCode = treeView.getSelected().split("_")[0];
			var mgCode = treeView.getParentId(treeView.getSelected()).split("_")[0];
			var bgCode = treeView.getParentId(treeView.getParentId(treeView.getSelected())).split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+sgCode+"&mgCode="+mgCode+"&bgCode="+bgCode;
			break;
		}
	}

	resultValue+= (resultValue.length > 0?"&":"?")+"allowableCode="+$('.group_name_wrap_active').attr("level-code");
	if(addList.length>0){
		resultValue+= (resultValue.length > 0?"&":"?")+"addList="+addList.join('/');
	}
	if(deleteList.length>0){
		resultValue+= (resultValue.length > 0?"&":"?")+"deleteList="+deleteList.join('/');
	}
	
	return encodeURI(resultValue);
}