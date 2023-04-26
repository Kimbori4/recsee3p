// 전역변수
var authyGrid; // 그리드
var originalAuthyName;

//그리드 생성 함수
function _createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){
	var tempGridName = objGrid;
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");

	// 페이징 유무에 따른 실행
	if(objPaging){
		objGrid.i18n.paging = i18nPaging[locale];
		objGrid.enablePaging(true,initPageRow, 5, objPaging, true);
		objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	    objGrid.setPagingSkin("toolbar","dhx_web");
	}

	objGrid.enableRowspan(true);
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){

		if(url == "authy_list"){

			var nRow = objGrid.getRowsNum();
			var nColumn = objGrid.getColumnsNum();

			for(var i=0; i<nRow; i++) {
				if(objGrid.cells2(i,2).cell.className=="myCell"){
					objGrid.cells2(i,2).setDisabled(true)
				}
				for(var j=0; j<nColumn; j++) {
					if(objGrid.cells2(i,j).getAttribute("disabled")) {
						/*objGrid.cells2(i,j).setDisabled(true);*/
						objGrid.cells2(i,j).cell.innerHTML = "";
						
					}	
					if(objGrid.cells2(i,j).cell.className=="recDate"){
						
						var numberlevel = $(".group_name_wrap_active").attr("level-code");
						
						$.ajax({
							url:contextPath+"/access_recdate_search.do",
							data:{
										"numberlevel" 	: numberlevel  
									,	"type"			: "select"
								 },
							type:"GET",
							dataType:"json",
							async: false,
							success:function(jRes){								
									var resultValue = jRes.resData.resultValue;
									objGrid.cells2(i,j).cell.innerHTML = "<input min='1' max='1000' id='recdateSearch' type='number' value='"+resultValue+"' />";
							},
							fail:function(jRes){
									objGrid.cells2(i,j).cell.innerHTML = "<input min='1' max='1000' id='recdateSearch' type='number' value='0' />";
							}
						});
						
						
					}					
					if(objGrid.cells2(i,j).cell.className=="recDateBtn"){
						objGrid.cells2(i,j).cell.innerHTML = "<button class='ui_main_btn_flat icon_btn_save_white' onclick='recdateSave()' style='background-color: #60b3dc;color: white;' id='recdateSave'>저장</button>";
					}
					
				}
			}
		}

		if(modiYn != "Y"){
			$('.userModiBtn').hide();
		}

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});

		
		if(objPaging){
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}
		
		ui_controller();
	});
	
	// 파싱 시작
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

		//컬럼명 로우 추가할 기준 아이디 찾기 authy_init.xml에 player="Y"라고 되어 있는 로우 찾음
		var targetRowId;
		var calltypeRowId;
		
		if(tempGridName == "authyGrid"){
			var nRow = objGrid.getRowsNum();
			var nColumn = objGrid.getColumnsNum();

			for(var i=0; i<nRow; i++) {
				if(objGrid.cells2(i,2).cell.className=="myCell"){
					objGrid.cells2(i,2).setDisabled(true)
				}
				for(var j=0; j<nColumn; j++) {
					if(objGrid.cells2(i,j).getAttribute("disabled")) {
						/*objGrid.cells2(i,j).setDisabled(true);*/
						objGrid.cells2(i,j).cell.innerHTML = "";
					}
					if(objGrid.cells2(i,j).cell.className=="recDate"){
						
						var numberlevel = $(".group_name_wrap_active").attr("level-code");
						
						$.ajax({
							url:contextPath+"/access_recdate_search.do",
							data:{
										"numberlevel" 	: numberlevel  
									,	"type"			: "select"
								 },
							type:"GET",
							dataType:"json",
							async: false,
							success:function(jRes){								
									var resultValue = jRes.resData.resultValue;
									objGrid.cells2(i,j).cell.innerHTML = "<input min='1' max='1000' id='recdateSearch' type='number' value='"+resultValue+"' />";
							},
							fail:function(jRes){
									objGrid.cells2(i,j).cell.innerHTML = "<input min='1' max='1000' id='recdateSearch' type='number' value='0' />";
							}
						});
						
						
					}
					if(objGrid.cells2(i,j).cell.className=="recDateBtn"){
						objGrid.cells2(i,j).cell.innerHTML = "<button class='ui_main_btn_flat icon_btn_save_white' onclick='recdateSave()' style='background-color: #60b3dc;color: white;' id='recdateSave'>저장</button>";
					}
				}
				
				
				if("Y"==authyGrid.getRowAttribute(authyGrid.getRowId(i),"player"))
					targetRowId=authyGrid.getRowId(i);
				
				if("Y"==authyGrid.getRowAttribute(authyGrid.getRowId(i),"calltype"))
					calltypeRowId=authyGrid.getRowId(i);
				
			}
			var bottomMenu = ""+
			"<tr class='ev_dhx_web'>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td align='center' title="+lang.admin.alert.authyManage1/*'사용유무'*/+">"+ lang.admin.alert.authyManage1 + "</td>" +
				"<td align='center' title="+lang.admin.alert.authyManage2/*'재생시점 구간지정'*/+">" + lang.admin.alert.authyManage2 + "</td>" +
				"<td align='center' title="+lang.admin.alert.authyManage3/*'사용자 구간지정'*/+">" + lang.admin.alert.authyManage3 + "</td>" +
				"<td align='center' title="+lang.admin.alert.authyManage4/*'마우스 구간지정'*/+">" + lang.admin.alert.authyManage4 + "</td>" +
				"<td align='center' title="+lang.admin.alert.authyManage5/*'메모일괄삭제'*/+">" + lang.admin.alert.authyManage5 + "</td>" +
				"<td align='center' title="+lang.admin.alert.authyManage6/*'업로드'*/+">" + lang.admin.alert.authyManage6 + "</td>" +
				"<td align='center' title="+lang.admin.alert.authyManage7/*'다운로드'*/+">" + lang.admin.alert.authyManage7 + "</td>" +
				"<td align='center'></td>" +
				"<td align='center' title="+lang.admin.alert.authyManage23/*'플레이어 다운로드'*/+">" + lang.admin.alert.authyManage23 + "</td>" +
			"</tr>"

			var sectionMenu = ""+
				"<tr class='ev_dhx_web'>" +
					"<td></td>" +
					"<td></td>" +
					"<td></td>" +
					"<td align='center' title="+lang.admin.alert.authyManage1/*'사용유무'*/+">"+ lang.admin.alert.authyManage1 + "</td>" +
					"<td align='center' title="+lang.search.player.playerTag+">"+lang.search.player.playerTag+"</td>" +/*플레이어 태그*/
					"<td align='center' title="+lang.search.player.repeat+">"+lang.search.player.repeat+"</td>" +/*반복*/
					"<td align='center' title="+lang.search.player.down+">"+lang.search.player.down+"</td>" +/*다운*/
					"<td align='center' title="+lang.search.player.mute+">"+lang.search.player.mute+"</td>" +/*묵음*/
					"<td align='center' title="+lang.search.player.remove+">"+lang.search.player.remove+"</td>" +/*제거*/
					"<td></td>" +
					"<td></td>" +
					"<td></td>" +
				"</tr>"
					
			var calltypeMenu = ""+
			"<tr class='ev_dhx_web'>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td align='center' title="+lang.admin.alert.authyManage1/*'사용유무'*/+">"+ lang.admin.alert.authyManage1 + "</td>" +
				"<td align='center' title="+lang.call.type.I/*'수신'*/+">"+ lang.call.type.I + "</td>" +
				"<td align='center' title="+lang.call.type.O/*'발신'*/+">" + lang.call.type.O + "</td>" +
				"<td align='center' title="+lang.call.type.TR/*'전환수신'*/+">" + lang.call.type.TR + "</td>" +
				"<td align='center' title="+lang.call.type.TS/*'전환송신'*/+">" + lang.call.type.TS + "</td>" +
				"<td align='center' title="+lang.call.type.Z/*'내선'*/+">" + lang.call.type.Z + "</td>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
			"</tr>"
				
			var recdatetypeMenu = ""+
			"<tr class='ev_dhx_web'>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td align='center' title='기간유무'>기간유무</td>" +
				"<td align='center' title='기간설정'>기간설정(일)</td>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
				"<td></td>" +
			"</tr>"
			
			$(authyGrid.getRowById(targetRowId)).before(bottomMenu)
			$(authyGrid.getRowById(targetRowId)).after(sectionMenu)
			$(authyGrid.getRowById(calltypeRowId)).before(calltypeMenu)
			$(authyGrid.getRowById(calltypeRowId)).after(recdatetypeMenu)
			
			if($('.group_name_wrap_active').attr('level-code')!='E1001'){
				$.ajax({
					url:contextPath+"/access_hide.do",
					data:{},
					type:"GET",
					dataType:"json",
					async: false,
					success:function(jRes){
						if(jRes.success == "Y") {
							var list = jRes.resData.hiddenMenu.split(",");
							list.forEach(function(id){
								authyGrid.deleteRow(id);
							});
						}else{
							alert(lang.admin.alert.authyManage19 + "\n" + lang.admin.alert.authyManage19)
							authyGrid.clearAll();
						}
					},
					fail:function(jRes){
						alert(lang.admin.alert.authyManage19 + "\n" + lang.admin.alert.authyManage19)
						authyGrid.clearAll();
					}
				});
			}
			
			var rowspanstartInd =0;
			var rowspanlength;
			
			for(var i=1; i<authyGrid.getRowsNum()-1;i++){
								
				
				if(authyGrid.cells2(i,0).getValue()!=authyGrid.cells2(i-1,0).getValue()){
					rowspanlength = i-rowspanstartInd;
					try{
					authyGrid.setRowspan(authyGrid.getRowId(rowspanstartInd),0,rowspanlength);
					}catch(e){
						continue;
					}
					rowspanstartInd = i;
				}
			}
			
		}
		if(telnoUse == 'Y') {
			objGrid.setColumnHidden(11,true);
			objGrid.setColumnHidden(12,true);
			objGrid.setColumnHidden(13,true);
			objGrid.setColumnHidden(14,true);

			objGrid.setColumnHidden(17,true);
			objGrid.setColumnHidden(18,true);
		}
	});
	
	
	return objGrid;
}

function formFunction(){

	// 권한 추가 메인 버튼
	$("#authyAddBtn").click(function(){
		$("#addJuri").find("p").text(lang.admin.alert.authyManage8)
		$('#authyName').val("");

		$("#modifyGroupBtn").hide();
		$("#addGroupBtn").show();
		layer_popup('#addJuri');
	});
	// 권한 수정 메인 버튼
	$("#authyModifyBtn").click(function(){
		$("#addJuri").find("p").text(lang.admin.alert.authyManage9)
		$('#authyName').val($(".group_name_wrap_active").text());
		
		originalAuthyName = $(".group_name_wrap_active").text();
		
		$("#modifyGroupBtn").show();
		$("#addGroupBtn").hide();
		layer_popup('#addJuri');
	});
	// 권한 삭제 메인 버튼
	$("#authyDeleteBtn").click(function(){
		var level = $(".group_name_wrap_active").attr("level-code");
		var param = "proc=delete&levelCode=" + level

		if($(".group_name_wrap_active").length == 0){
			alert(lang.admin.alert.authyManage10)
			return
		}

		if(level == "E1001"){
			alert(lang.admin.alert.authyManage11)
			return
		}

		if(confirm(lang.admin.alert.authyManage14)){
			$.ajax({
				url:contextPath+"/access_proc.do",
				data:param,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB에 조회한 계정이 있으면
					if(jRes.success == "Y") {
						// 불러온 옵션 추가
						alert(lang.admin.alert.authyManage12)
						loadAuthy()
						layer_popup_close()
						clickEventAuthy(userLevel);
					}else if (jRes.resData.msg == "admin authy is can not deleted"){
						alert(lang.admin.alert.authyManage11)
					}else{
						alert(lang.admin.alert.authyManage13)
					}
				}
			});
		}
	})


	// 권한 추가 팝업 버튼
	$("#addGroupBtn").click(function(){
		
		var acesName = $("#authyName").val();
		var nRow = authyGrid.getRowsNum();
		var nColumn = authyGrid.getColumnsNum();

		if(!acesName){
			alert(lang.admin.alert.authyManage15)
			$("#authyName").focus();
			return
		}

		var param = "proc=insert&levelName="+acesName+"&param=";
		for(var i=0; i<nRow; i++) {
			if(i>0) param += "|";
			for(var j=2; j<nColumn; j++) {
				if(j>2) param += ",";
				if(authyGrid.cells2(i,j).getAttribute("disabled")) {
					param += "0";
				} else {
					if(authyGrid.getColType(j) == "combo") {
						/*if(authyGrid.getRowAttribute(i,"range")) {*/
							param += "A";
						/*} else {
							param += "A";
						}*/
					} else {

						if(authyGrid.cells2(i,j).getAttribute("programeCode"))
							param += authyGrid.cells2(i,j).getValue();
						else
							param += "0";
					}
				}
			}
		}
			

		$.ajax({
			url:contextPath+"/access_proc.do",
			data:param,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					// 불러온 옵션 추가
					alert(lang.admin.alert.authyManage21/*"권한 추가가 완료 되었습니다."*/)
					loadAuthy()
					layer_popup_close()
				}else{
					alert(lang.admin.alert.authyManage22/*"권한 추가에 실패 하였습니다."*/)
				}
			}
		});
		
		
		
	});

	// 권한 수정 팝업 버튼
	$("#modifyGroupBtn").click(function(){

		var acesName = $("#authyName").val();
		var acesCode = $(".group_name_wrap_active").attr("level-code");
		
		// 권한그룹명 변경 없이 수정완료하는 경우 그룹명이 이미 있어서 중복확인에서 success-N으로 빠지게됨
		// ∴ 변경사항이 없는 경우 별도로 처리해주기 위한 부분
		if(originalAuthyName === acesName){
			alert(lang.admin.alert.authyManage17);
			loadAuthy();
			layer_popup_close();
			return;
		}
		
		if($(".group_name_wrap_active").length == 0){
			alert(lang.admin.alert.authyManage16)
			return
		}else if(!acesName){
			alert(lang.admin.alert.authyManage15)
			$("#authyName").focus();
			return
		}


		param = "proc=modify&levelName="+acesName+"&levelCode="+acesCode;
		$.ajax({
			url:contextPath+"/access_proc.do",
			data:param,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					// 불러온 옵션 추가
					alert(lang.admin.alert.authyManage17)
					loadAuthy()
					layer_popup_close()
				}else{
					alert(lang.admin.alert.authyManage18)
				}
			}
		});
	});
	
	$("#addAllowableBtn").click(function(){
		var acesName = $("#allowableName").val();

		if(!acesName){
			alert(lang.admin.alert.allowableRangeManage5)/*허용 범위 명을 입력해 주세요.*/
			$("#allowableName").focus();
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
					authyGrid = _createGrid("authyGrid", "", "authy_list","?levelCode="+userLevel,"","",[],[]);
					layer_popup_close()
				}else{
					alert(lang.admin.alert.allowableRangeManage7)/*허용 범위 추가에 실패 하였습니다.*/
				}
			}
		});
	});
	
	


}

function gridFunction(){

	authyGrid.attachEvent("onCheckbox", function(rId, cInd, state){
		var authyCode = $(".group_name_wrap_active").attr("level-code")

		if(!authyCode) return false;

		var param = "proc=change&levelCode="+authyCode+"&menuCode="+authyGrid.cells(rId,19).getValue();
		param += "&acesIdx="+cInd+"&acesSta="+state;
		var response = window.dhx4.ajax.postSync(contextPath+"/access_proc.do",param);
		var rst = window.dhx4.s2j(response.xmlDoc.responseText);
		if(rst == null || rst.result == false) {
			alert(lang.admin.alert.authyManage19 + "\n" + lang.admin.alert.authyManage19)
			/*dhtmlx.alert({
				type:"alert",
				title:lang.get("message.title.notifications"),
				ok:lang.get("message.btn.ok"),
				text:lang.get("management.access.message.modify.error"),
				callback: function() {
					authyGrid.doUndo();
				}
			});*/
		}
	});
	authyGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue){
		if(stage == 2) {
			if(cInd == 2) {

				var authyCode = $(".group_name_wrap_active").attr("level-code")

				if(authyCode) {

					var param = "proc=change&levelCode="+authyCode+"&menuCode="+authyGrid.cells(rId,19).getValue();
					param += "&acesIdx="+cInd+"&acesSta="+nValue;
					var response = window.dhx4.ajax.postSync(contextPath+"/access_proc.do",param);
					var rst = window.dhx4.s2j(response.xmlDoc.responseText);
					if(rst == null || rst.result == false) {
						alert(lang.admin.alert.authyManage19 + "\n" + lang.admin.alert.authyManage19)
						/*dhtmlx.alert({
							type:"alert",
							title:lang.get("message.title.notifications"),
							ok:lang.get("message.btn.ok"),
							text:lang.get("management.access.message.modify.error"),
							callback: function() {
								authyGrid.doUndo();
							}
						});*/
					} else if(rst.result == true && rst.resData.msg == "allowableAdd"){
						$('#allowableName').val("");
						$("#addAllowableBtn").show();
						layer_popup('#addAllowable');
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		} else {
			return true;
		}
	});
}

//권한 리스트 클릭 시 우측 그리드 로딩 ;; 호출은 admin_common.js에 있음
function clickEventAuthy(authyCode){
	authyGrid.clearAndLoad(contextPath+"/authy_list.xml?levelCode="+authyCode, function(){
		if(modiYn != "Y"){
			$('.userModiBtn').hide();
		}
	})
}

//권한 불러 오기
function authyLoad() {

	if(writeYn != "Y"){
		$('#btnGroupAdd').remove();
		$('#userAddBtn').remove();
		$('#authyAddBtn').remove();
		$('#userAddBtn').remove();
	}

	if(modiYn != "Y") {
		$('#btnGroupModify').remove();
		$('#pwPolicyBtn').remove();
		$('#authyModifyBtn').remove();

	}

	if(delYn != 'Y') {
		$('#authyDeleteBtn').remove();
		$('#userDeleteBtn').remove();
		$('#btnGroupDelete').remove();

	}
}

//로드 함수
function userManageLoad(){

	var noUseMsg = lang.admin.label.notUse/*사용불가*/; 
//	$('<style>.myCell:after{content:"'+str+'"}</style>').appendTo('head');
	document.styleSheets[0].addRule('.myCell:after','content: "'+noUseMsg+'" !important;');
	
	/*권한 관리 그리드*/
	authyGrid = _createGrid("authyGrid", "", "authy_list","?levelCode="+userLevel,"","",[],[]);

	formFunction();
	gridFunction();

	//loadTree();
	authyLoad();
}


function recdateSave(){
	var dateValue = $("#recdateSearch").val();
	var numberlevel = $(".group_name_wrap_active").attr("level-code");
	
	$.ajax({
		url:contextPath+"/access_recdate_search.do",
		data:{
					"numberlevel" 	: numberlevel  
				,	"type"			: "update"
				,   "dateValue"		: dateValue
			 },
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){	
			alert("Save Success")
		}
	});
}
