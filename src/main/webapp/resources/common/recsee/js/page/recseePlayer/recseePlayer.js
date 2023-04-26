//전역 변수 셋팅
var data  = {};
var gridSearch, traceList, recSectionList , recMemoList, gridEvaluation, logListenList, csvGrid, listenPopupGrid;
var listenSimple;				// 심플 리슨 메뉴
var gridContextMenu;		// 틀고정 콘텍스트 메뉴
var gridContextUrl;			// 틀고정 후 주소 가지고 있기
var gridContextUrlCnt = 0;		// 틀고정 후 이동 페이지
var rc,mvSearchInterval;
var nowSortingColumn="";
var dateColumnState = "desc";
var timeColumnState = "desc";
var playGridClick = false;
var videoFileData = {};
var mvSearchTrue = true;
var mvSearchSate = true;
var encNCheck = false;				// ENC 여부 체크 하고 확인 함수 기본 false true 면 패스 하여
									// 다운로드 진행
var encYnReal = true;				// 암호화 여부 체크
var IVR=false;
var openPopup_addBestCall = "";
var openPopup_addBestCall_url = "";
var winObj;
var buffer12Combo;
var buffer13Combo;
var Eval_Thema;
var reasonStr='';
var treeView;
var check=0;
var csvA;
var csvT;
var csvF;
var csvColName;
var csvFlag=0;
var undervar=1;
var downId;
var recstart = false; // 녹취 시작 여부
var nowListenNo = 1; // 녹취 시작 여부
var gridCallKey;
var treeArr = new Array();
window.onload = function() {
// rc_player = new RecseePlayer({
// "target" : "#playerObj" // 플레이어를 표출 할 target
// , "requestIp" : ip // 통신 IP
// , "requestPort" : port // 통신 Port
// });
// progress.off();
// rc_player.setFile("audio", '1');
	openFaceRecordingPlayer('gridSearch');
	jQuery.support.cors = true;

}

function chtest(objSelect, comboType, serviceCode, receiptCode ,bgCode, mgCode, selectedValue, defaultSelect, subOpt, empty){

	// 옵션 붙여 넣기 전에 삭제
	$(objSelect).children().remove()
	$(objSelect).append("<option value=''>"+lang.combo.ALL+"</option>");
	var dataStr = {
			"comboType" : comboType
		,   "serviceCode" : serviceCode
		,	"receiptCode" : receiptCode
		,	"bgCode" : bgCode
		,	"mgCode" : mgCode
		,	"selectedValue" : selectedValue
		,	"accessLevel" : accessLevel
		,	"subOpt" : subOpt
	}

	$.ajax({
		url:contextPath+"/organizationSelect.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				if(empty){
					if(empty=="Y"){
						// $(objSelect).append("<option>"+lang.common.label.Noclassification/*
						// 해당분류없음 */+"</option>")
					}else if(empty=="empty"){
						// empty
					}else
						$(objSelect).append("<option></option>")
				}
								
				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
				if(defaultSelect)
					$(objSelect).val("")
			}
		}
	});
}

// 권한 불러 오기
function authyLoad() {
	/*
	 * if(listenYn != 'Y') { top.playerVisible(false); }
	 */

	// 청취 및 다운로드 요청 관리 페이지 갓다가 다시 돌아 올때 숨김처리 풀어주기
	top.playerFrame.$(".main_btn_filedownload").show();
	top.playerFrame.$(".play_list_menu,.main_btn_section,.main_btn_time,.main_btn_selector,.main_btn_delete").show();
}


// 조회 및 청취 그리드 로드 함수
function searchGridLoad(splitAt){
	var test = true;
	listenSimple = new dhtmlXMenuObject({
		context : true,
		items : [
			{id : "listenSimple", text : lang.views.search.alert.msg72/*
																		 * "선택
																		 * 파일
																		 * 재생"
																		 */}
		]
	});

	objGrid = new dhtmlXGridObject("gridSearch");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true,100, 5, "pagingSearch", true);
	objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	objGrid.setPagingSkin("toolbar","dhx_web");
	// objGrid.enableContextMenu(listenSimple);
	objGrid.enableColumnAutoSize(false);
	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	if(gridCopy == "Y"){
		objGrid.enableBlockSelection();
		objGrid.attachEvent("onKeyPress",onKeyPressed);
	}
	if (dataCopy == "Y"){
		// 우클릭시 클립보드 복사
		objGrid.attachEvent("onRightClick", function(id,ind){
			var temp = objGrid.getColumnId(ind);
			if (copyList.indexOf(temp) == -1) {
				return false;
			} else {
				var val = "";
				if (objGrid.getColType(ind) == "combo") {
					val = objGrid.cells(id,ind).getText();
				} else {
					val = objGrid.cells(id,ind).getValue();
				}
				
			    console.log(val);
			    
			    var t = document.createElement("textarea");
			    document.body.appendChild(t);
			    t.value = val;
			    t.select();
			    document.execCommand('copy');
			    document.body.removeChild(t);
			}
		});
	}
	objGrid.init();

	if(splitAt == undefined)
		splitAt = "";

	objGrid.load(contextPath+"/search_list.xml" + encodeURI("?header=true") + "&splitAt="+splitAt + "&recsee_mobile="+recsee_mobile, function(){

		if (objGrid.getRowsNum() > 0){
			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+ objGrid.getRowsNum()+ " "+objGrid.i18n.paging.found+'</div>'
			objGrid.aToolBar.setItemText("total", setResult)
		}

		if(listenYn != "Y"){
			var addPlayListIndex = objGrid.getColIndexById("r_list_add");
			if(addPlayListIndex != undefined){
				objGrid.setColumnHidden(addPlayListIndex,true); // 컬럼 안보이게
			}

			var screenPlayIndex = objGrid.getColIndexById("screen");
			if(screenPlayIndex != undefined){
				objGrid.setColumnHidden(screenPlayIndex,true); // 컬럼 안보이게
			}

			var traceIndex = objGrid.getColIndexById("trace");
			if(traceIndex != undefined){
				objGrid.setColumnHidden(traceIndex,true); // 컬럼 안보이게
			}
			
			var sttScript = objGrid.getColIndexById("r_stt_player");
			if(sttScript != undefined){
				objGrid.setColumnHidden(sttScript,true); // 컬럼 안보이게
			}
		}

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});

		objGrid.attachEvent("onXLS", function(){
			progress.on()
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");

			if (objGrid.getRowsNum() > 0){
				var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>'
				objGrid.aToolBar.setItemText("total", setResult)
			}

			top.searchGrid = objGrid;

			objGrid._HideSelection();
			
			ui_controller();
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
				$(top.window).trigger("resize");
			});

			if(listenYn != "Y"){
				var addPlayListIndex = objGrid.getColIndexById("r_list_add");
				if(addPlayListIndex != undefined){
					objGrid.setColumnHidden(addPlayListIndex,true); // 컬럼 안보이게
				}
				var screenPlayIndex = objGrid.getColIndexById("screen");
				if(screenPlayIndex != undefined){
					objGrid.setColumnHidden(screenPlayIndex,true); // 컬럼 안보이게
				}
				var traceIndex = objGrid.getColIndexById("trace");
				if(traceIndex != undefined){
					objGrid.setColumnHidden(traceIndex,true); // 컬럼 안보이게
				}
				var sttScript = objGrid.getColIndexById("r_stt_player");
				if(sttScript != undefined){
					objGrid.setColumnHidden(sttScript,true); // 컬럼 안보이게
				}
			}
		});

		// 체크박스 "+lang.combo.ALL+" 선택
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
		
		//row별로 uncheck되면 hewder부분 check 이미지 없음
		objGrid.attachEvent("onCheck", function(rowId, colIndex, isChecked){
			var checkBox = gridSearch.getCheckedRows(0);
			if (checkBox.length > 0) {
				$("#allcheck>img").attr("src",recseeResourcePath+ "/images/project/dhxgrid_web/item_chk0.gif");
			}
		});

		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj,count){
			// 틀고정시 카운트 알고 있기 위해서
			gridContextUrl = gridContextUrl;

			// 현재 페이지 비교로 첫번째 페이지 아닐 시 페이지 저장
			if(objGrid.currentPage !=1 ){
				gridContextUrlCnt = objGrid.currentPage;
			}else{
				// 처음 페이지 인입시 1로 표기때문에 t/f 처리
				if(!test)
					gridContextUrlCnt = 1;
				test = false
			}
			ui_controller();
		});

		addButtons(objGrid);
		objGrid.aToolBar.setMaxOpen("pages", 5);
	

		var evalColum=gridSearch.getColIndexById("evaluation");
		
		// 나중에 1001은 지워야 함 테스트용
	/*
	 * if(evalColum!=undefined&&(userInfoJson.userLevel == "E1006" ||
	 * userInfoJson.userLevel == "E1005")){
	 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌 }else
	 * if(userInfoJson.userLevel == "E1001"){
	 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌 }else{
	 * gridSearch.setColumnHidden(evalColum, true); // 평가 컬럼을 사라지게 함 }
	 */
		
	/*
	 * //마스타 테마일때는 컬럼 숨김처리 // 권한레벨에 따라 평가 처리 구분되어있음 if(evalThema == "master"){
	 * if(userInfoJson.userLevel == "E1006" || userInfoJson.userLevel ==
	 * "E1005"){ gridSearch.setColumnHidden(21, false); // 평가 컬럼을 보여줌 }else
	 * if(userInfoJson.userLevel == "E1001"){ gridSearch.setColumnHidden(21,
	 * false); // 평가 컬럼을 사라지게 함 }else{ gridSearch.setColumnHidden(21, true); //
	 * 평가 컬럼을 사라지게 함 } }
	 */		

		// 조회페이지 들어가자마자 이력 나오게
		$("#searchBtn").trigger("click");
	});

// top.searchGrid = objGrid;
	return objGrid;
}

function comboEvent(idx, state, objCombo){
	objCombo.setChecked(idx,state);
	var count=objCombo.getChecked().length

	objCombo.unSelectOption();
	if(count!=0)
		objCombo.setComboText(lang.statistics.js.alert.msg2+" "/* "총 " */+count)
	objCombo.openSelect();
}

// 그리드 생성 함수
function createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){
	listenSimple = new dhtmlXMenuObject({
		context : true,
		items : [
			{id : "listenSimple", text : lang.views.search.alert.msg72/*
																		 * "선택
																		 * 파일
																		 * 재생"
																		 */}
		]
	});
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.setImageSize(1,1);
	objGrid.enableAutoHeight(true,230);
	objGrid.enableColSpan(true);

	// 페이징 유무에 따른 실행
	if(objPaging){
		objGrid.i18n.paging = i18nPaging[locale];
		objGrid.enablePaging(true,initPageRow, 5, objPaging, true);
		objGrid.setPagingWTMode(true,true,true,[100,250,500]);
		objGrid.setPagingSkin("toolbar","dhx_web");
	}

	// objGrid.enableContextMenu(listenSimple);
	objGrid.enableColumnAutoSize(false);
//	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	if(parent.contextValue != null){
		objGrid.splitAt(parent.contextValue);
	}
	//treeArr
	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){
		treeArr = new Array();
        objGrid.forEachRow(function(idx){
			var ttime = objGrid.cells(idx,objGrid.getColIndexById("r_call_ttime")).getValue()
			console.log("idx yn : "+objGrid.cells(idx,objGrid.getColIndexById("moreProductYn")).getValue())
			console.log("idx ttime : "+objGrid.cells(idx,objGrid.getColIndexById("r_call_ttime")).getValue())
			if(ttime == 0){
				treeArr.push(idx);
				objGrid.setColspan(idx,3,5);
				//다계좌 체크
				
			}

	    })
		if(objPaging){
			if (objGrid.getRowsNum() > 0){
				var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>'
				objGrid.aToolBar.setItemText("total", setResult)
			}
		}


		if(listenYn != "Y"){
			var addPlayListIndex = objGrid.getColIndexById("r_list_add");
			if(addPlayListIndex != undefined){
				objGrid.setColumnHidden(addPlayListIndex,true); // 컬럼 안보이게
			}

			var screenPlayIndex = objGrid.getColIndexById("screen");
			if(screenPlayIndex != undefined){
				objGrid.setColumnHidden(screenPlayIndex,true); // 컬럼 안보이게
			}

			var traceIndex = objGrid.getColIndexById("trace");
			if(traceIndex != undefined){
				objGrid.setColumnHidden(traceIndex,true); // 컬럼 안보이게
			}
			var sttScript = objGrid.getColIndexById("r_stt_player");
			if(sttScript != undefined){
				objGrid.setColumnHidden(sttScript,true); // 컬럼 안보이게
			}
		}

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});

		objGrid.attachEvent("onXLS", function(){
			// progress.on()
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){

			$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");

			if(objPaging){
				if (objGrid.getRowsNum() > 0){
					var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>'
					objGrid.aToolBar.setItemText("total", setResult)
				}
			}
			objGrid.expandAll()
			top.searchGrid = objGrid;
			ui_controller();
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
				$(top.window).trigger("resize");
			});

			if(listenYn != "Y"){
				var addPlayListIndex = objGrid.getColIndexById("r_list_add");
				if(addPlayListIndex != undefined){
					objGrid.setColumnHidden(addPlayListIndex,true); // 컬럼 안보이게
				}
				var screenPlayIndex = objGrid.getColIndexById("screen");
				if(screenPlayIndex != undefined){
					objGrid.setColumnHidden(screenPlayIndex,true); // 컬럼 안보이게
				}
				var traceIndex = objGrid.getColIndexById("trace");
				if(traceIndex != undefined){
					objGrid.setColumnHidden(traceIndex,true); // 컬럼 안보이게
				}
				var sttScript = objGrid.getColIndexById("r_stt_player");
				if(sttScript != undefined){
					objGrid.setColumnHidden(sttScript,true); // 컬럼 안보이게
				}
			}
		});
		
		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj,count){
			ui_controller();
		});

		objGrid.expandAll();
		
		if(excelFileName)
			addButtons(objGrid);

		if(objPaging){
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}
	});
	
	top.searchGrid = objGrid;
	return objGrid;
}

// 그리드 생성 함수
function createTraceGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){
		objGrid.attachEvent("onXLS", function(){
			progress.on();
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			ui_controller();
			// objGrid.sortRows(0,"str","dsc")
			// objGrid.sortRows(1,"str","dsc")
			progress.off();
		});

		// objGrid.sortRows(0,"str","dsc")
		// objGrid.sortRows(1,"str","dsc")
	});
	return objGrid;
}

function createRecMemoGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){


	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setPagingSkin("toolbar","dhx_web");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){
		objGrid.attachEvent("onXLS", function(){
			progress.on();
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			ui_controller();
			objGrid.sortRows(0,"str","dsc")
			objGrid.sortRows(1,"str","dsc")
			progress.off();

		});

		objGrid.sortRows(0,"str","dsc")
		objGrid.sortRows(1,"str","dsc")
	});
	return objGrid;
}

function addButtons(objGrid){

	var search_toolbar = objGrid.aToolBar;

	search_toolbar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>')
	search_toolbar.setWidth("total",200)

	
	if(excelYn == "Y") {
	// if(false){
		search_toolbar.addButton("excelDownload",9, lang.common.excel.download/*
																				 * "엑셀
																				 * 다운로드"
																				 */, "icon_excel_download.png", "icon_excel_download.png");
	}
	
	if(userLevel ='E1001'){
		search_toolbar.addButton("adwCSVDownload",9, "ADW이력 다운로드"
		/*
		 * "엑셀
		 * 다운로드"
		 */, "icon_download.png", "icon_download.png");
	}
	// if(delYn=="Y"){
	if(false){
		search_toolbar.addButton("deleteAll",10, lang.views.search.alert.msg92/*
																				 * "파일
																				 * 삭제"
																				 */, "icon_btn_trash_gray.png", "icon_btn_trash_gray.png");
	}
	
	 if(downloadYn =="Y") {
	//if(false){
		/*search_toolbar.addButton("fullDownload",11, lang.views.search.alert.msg76
																					 * "파일
																					 * 다운로드"
																					 , "icon_download.png", "icon_download.png");*/
		
		 search_toolbar.addButton("multiDownload",12,lang.views.search.alert.msg75/*"선택파일 다운로드"*/, "icon_download.png","icon_download.png");
	 }else if(downloadYn =="N" &&downloadApprove=="Y"){
		search_toolbar.addButton("fileDownload",11, lang.views.search.alert.msg76/*
																					 * "파일
																					 * 다운로드"
																					 */, "icon_download.png", "icon_download.png");
	}
	// 우수콜 권한
	// if(bestcallYn == 'Y'){
	if(false){
		search_toolbar.addButton("addBestCall",14, lang.admin.approve.label.addToBestcall/*
																							 * 선택녹취
																							 * 우수콜
																							 * 추가
																							 */, "thumbs-up.svg", "thumbs-up.svg");
	}
	// 업로드 권한
	// if(uploadYn == 'Y'){
	if(false){
		search_toolbar.addButton("individualUpload",15, lang.views.search.alert.indivUpload/*
																							 * "선택파일
																							 * 개별
																							 * 업로드"
																							 */, "upload.svg", "upload.svg");	
	}
	// if(listenYn == "Y") {
	if(false){
		search_toolbar.addButton("addPlayList",12, lang.views.search.alert.msg77/*
																				 * "선택파일
																				 * 리스트에
																				 * 추가"
																				 */, "icon_btn_add_gray.png", "icon_download.png");
		// @TODO
		if(separation_speaker == "Y")
			search_toolbar.addButton("playerToggle",8, lang.views.search.alert.msg79/*
																					 * "플레이어
																					 * 변경"
																					 */, "icon_btn_change_gray.png", "icon_btn_change_gray.png");
	}
	// if(transcriptYn == "Y") {
	if(false){
		search_toolbar.addButton("fileDivision",8, lang.admin.approve.label.transcript/*
																						 * "전사
																						 * 리스트
																						 * 추가"
																						 */, "icon_btn_transcript_gray.png", "icon_btn_transcript_gray.png");
	}
		
	// if(myFolderYn == "Y") {
	if(false){
		search_toolbar.addButton("addMyFolder",13, lang.admin.approve.label.addToMyfolder/*
																							 * 선택파일
																							 * 마이폴더
																							 * 추가
																							 */, "folder.svg", "folder.svg");
	}
	
	// if(userId == 'admin'){
	if(false){
		search_toolbar.addButton("rsRecfileUpdate",15, "녹취 정보 변경", "icon_btn_exchange_file_gray.png", "icon_btn_exchange_file_gray.png");	
	}
	
	search_toolbar.addSpacer("perpagenum");

	// 다른 곳 클릭시 엑셀 다운로드 닫아주기
 	window.addEventListener("click",function(){
 		deleteMenu(null, null, false);
 		fileDownMenu(null, null, false);
	 	excelDownMenu(null,null,false);
	 	playerChangeMenu(null,null,false);
 	})

	search_toolbar.attachEvent("onClick", function(name){
		switch(name) {
		case "adwCSVDownload":
			layer_popup("#adwPopup");
			break;
		case "excelDownload":
			if(objGrid.getRowsNum() > 0) {

				if($("#sDate").val().length != 10 || $("#eDate").val().length != 10)
					if($("#sDate").val().length == 8){
						var dateTemp = $("#sDate").val();
						dateTemp = dateTemp.substring(0,4)+"-"+dateTemp.substring(4,6)+"-"+dateTemp.substring(6,8);
						$("#sDate").val(dateTemp);
					}else if($("#eDate").val().length == 8){
						var dateTemp = $("#eDate").val();
						dateTemp = dateTemp.substring(0,4)+"-"+dateTemp.substring(4,6)+"-"+dateTemp.substring(6,8);
						$("#eDate").val(dateTemp);
					}else{					
						alert(lang.views.search.alert.msg1/*
															 * "녹취 일자를 다음 형식에 맞게
															 * 입력 해
															 * 주세요!\n예)2018-01-01"
															 */);
					}
				else{
					allExcelDown();
					/*
					 * | @author DAVID | @type funtcion | @param event | @return
					 * void; | @description
					 */
// window.addEventListener("click",function(e){
// var x,y;
// x = e.clientX;
// y = e.clientY;
// if(e.target.className == "dhxtoolbar_text" && e.target.textContent ==
// lang.common.excel.download/*"엑셀 다운로드"*/)
// excelDownMenu(x,y,true);
// else
// excelDownMenu(x,y,false);
//
// // 이벤트가 중첩되어 마지막에 이벤트 삭제 추가
// this.removeEventListener("click",arguments.callee,false);
// })
				}
			} else {
				alert(lang.views.search.alert.msg4/* "선택된 녹취 파일이 없습니다." */);
			}
		break;
		case "multiDownload":
			
			  if(downloadYn == "Y" && downloadFormat=='Y'){
				if(requestReason=="Y")
					requestReasonByDown(objGrid) 
				else{
					checkFormat(objGrid) 
				} 
			  }else if(downloadYn == "Y" &&downloadFormat!='Y'){
				if(requestReason=="Y")
					requestReasonByDown(objGrid) 
				else 
					multiDownloadFunc(objGrid); 
			}
			 
		break;
		case "addBestCall":
			var chCol = -1;
			for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
				if(objGrid.getColType(i) == "ch") {
					chCol = i;
					break;
				}
			}

			var checkGrid = objGrid.getCheckedRows(chCol);
			
			if(checkGrid==""){
				alert(lang.views.search.alert.msg31/* "선택된 파일이 없습니다." */);
				return false;
			}
			
			var checked = objGrid.getCheckedRows(chCol).split(",");
				
			if(checked.length ==1){
				var id=checked[0];
				var urlsimple;

				var urldate={
						recDate : (gridSearch.getColIndexById("r_rec_date")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_date")).getValue()).replace(/-/gi,''),
						recTime	: (gridSearch.getColIndexById("r_rec_time")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_time")).getValue()).replace(/:/gi,''),
						recExt	: (gridSearch.getColIndexById("r_ext_num")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_ext_num")).getValue())
	        	};
	    		$.ajax({
	    			url:contextPath+"/getListenUrl.do",
	    			data:urldate,
	    			type:"POST",
	    			dataType:"json",
	    			async: false,
	    			success:function(jRes){
	    				if(jRes.success=="Y"){
	    				// var ip=jRes.resData.ListenUrl[0].vRecIp;
	    				// var
						// file=jRes.resData.ListenUrl[0].vRecFullpath.replace(/\\/gi,"/");
	    					// urlsimple='http://'+ip+':28881/listen?url='+file;
	    					
	    					var checkFile = true;
	    					
	    					// STT 작업 되었는지 확인
	    					$.ajax({
	    							url : contextPath + "/player/getList"
	    						,	type : "POST"
	    						,	data : {
	    								"recDate" : urldate.recDate
	    							,	"recTime" : urldate.recTime
	    							,	"ext" : urldate.recExt
	    						}
	    						,	dataType:"json"
	    						,	async: false
	    						,	success : function(res){
	    							var cnt = res.resData.count; 
	    							if(cnt > 0){
	    								checkFile = false;
	    							}else{
	    								checkFile = true;
	    							}
	    							
	    						}
	    					})
	    					
	    					if(checkFile){
	    						// STT가 작업되지 않았으면 기본 플레이어로 실행한다.
	    						urlsimple=encodeURI(jRes.resData.ListenUrl+"&userId="+userInfoJson.userId+"&userName="+userInfoJson.userName+"&bgCode="+userInfoJson.bgCode+"&mgCode="+userInfoJson.mgCode+"&sgCode="+userInfoJson.sgCode);
	    						    						
	    						if(!openPopup_addBestCall.closed && openPopup_addBestCall && openPopup_addBestCall_url == urlsimple){
		    		    			if(confirm(lang.views.search.alert.msg89/*
																			 * 같은
																			 * 내용의
																			 * 창이
																			 * 있습니다.
																			 * 그래도
																			 * 띄우시겠습니까?
																			 */)){
		    		    			}else{
		    		    				// 생성된 팝업에 포커스 주기
		    		    				openPopup_addBestCall.focus();
		    		    				return false;
		    		    			}
		    		    		}else{}
	    						
	    						openPopup_addBestCall_url = urlsimple;
		    	    			openPopup_addBestCall = window.open(contextPath+"/SharePlayer?url="+urlsimple, "addBestCall", "resizable=no, toolbar=no,location=no, width=800, height=130,scrollbars=no ,left=500,top=250'");
		    		    		
	    					}else{
	    						// STT가 작업되엇으면 STT플레이어로 실행한다.
	    						urlsimple=encodeURI("&userId="+userInfoJson.userId+"&userName="+userInfoJson.userName+"&bgCode="+userInfoJson.bgCode+"&mgCode="+userInfoJson.mgCode+"&sgCode="+userInfoJson.sgCode);
	    						
	    						if(!openPopup_addBestCall.closed && openPopup_addBestCall && openPopup_addBestCall_url == urlsimple){
		    		    			if(confirm(lang.views.search.alert.msg89/*
																			 * 같은
																			 * 내용의
																			 * 창이
																			 * 있습니다.
																			 * 그래도
																			 * 띄우시겠습니까?
																			 */)){
		    		    			}else{
		    		    				// 생성된 팝업에 포커스 주기
		    		    				openPopup_addBestCall.focus();
		    		    				return false;
		    		    			}
		    		    		}else{
		    		    			
		    		    		}
	    						
	    						$("#popRecDate").val(urldate.recDate);
		    		    		$("#popRecTime").val(urldate.recTime);
		    		    		$("#popExt").val(urldate.recExt);
		    		    		
		    		    		var myForm = document.popForm;
		    		    		var url =  contextPath + "/SttPlayer?bestCall=true"+urlsimple;
		    		    		window.open("","popForm","toolbar=no,width=1050,height=830,directories=no,status=no,left=0,top=0");
		    		    		myForm.action = url;
		    		    		myForm.method = "post";
		    		    		myForm.target="popForm";
		    		    		myForm.submit();
	    					}
	    					
	    					// urlsimple=encodeURI('http://'+ip+'/listen?url='+file+"&userId="+userInfoJson.userId+"&userName="+userInfoJson.userName+"&bgCode="+userInfoJson.bgCode+"&mgCode="+userInfoJson.mgCode+"&sgCode="+userInfoJson.sgCode);
	    		    	
// window.open(contextPath+"/SharePlayer?url="+urlsimple, "_blank",
// "resizable=no, toolbar=no,location=no, width=800, height=130,scrollbars=no
// ,left=500,top=250'");
	    		    		// window.open 중복 열림 방지 체크
	    		    		
	    		    		// 팝업 띄우면서 저장하기
	    		    		/*
							 * openPopup_addBestCall -> window.open 객체
							 * openPopup_SaveUrl - > url 값
							 */ 		
	    				}else {
	    					alert(lang.views.search.alert.msg26/* 요청에 실패 하였습니다. */);
	    				}
	    			}
	    		});
			}else{
				alert(lang.views.search.alert.msg90/* 한개의 파일만 선택해 주세요. */);
			}
			break;
		case "addPlayList":
			var chCol = -1;
			for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
				if(objGrid.getColType(i) == "ch") {
					chCol = i;
					break;
				}
			}

			if(chCol != -1 && objGrid.getCheckedRows(chCol) != "" ) {
				var checked = objGrid.getCheckedRows(chCol).split(",");

				for( var i = 0 ; i < checked.length ;i++ ) {
					rc = top.nowRc;
					var CheckMode=top.playerFrame.$("#playerObj .btn_list_del").css("display");
					if(rc.type.dual){
						alert(lang.views.search.alert.msg2/*
															 * "화자분리 플레이어 상태에서는
															 * 파일리스트 추가가 불가능
															 * 합니다."
															 */);
						return;
					}else if(CheckMode!="none"){
						alert(lang.views.search.alert.msg3/*
															 * "플레이리스트 편집모드에서는
															 * 파일리스트 추가가 불가능
															 * 합니다."
															 */);
						return;
					}else{
						var id = checked[i];

						var listenUrl 		= (objGrid.getColIndexById("rec_url")==undefined ? 		"" : objGrid.cells(id,objGrid.getColIndexById("rec_url")).getValue().replace(':8088',':28881/listen?url='));
						var recDate 		= (objGrid.getColIndexById("r_rec_date")==undefined ? 	"" : objGrid.cells(id,objGrid.getColIndexById("r_rec_date")).getValue());
						var recTime			= (objGrid.getColIndexById("r_rec_time")==undefined ? 	"" : objGrid.cells(id,objGrid.getColIndexById("r_rec_time")).getValue());
						var recExt			= (objGrid.getColIndexById("r_ext_num")==undefined ? 	"" : objGrid.cells(id,objGrid.getColIndexById("r_ext_num")).getValue());
						var recCustPhone	= (objGrid.getColIndexById("r_cust_phone1")==undefined?	"" : objGrid.cells(id,objGrid.getColIndexById("r_cust_phone1")).getValue());
						var recUserName 	= (objGrid.getColIndexById("r_user_name")==undefined? 	"" : objGrid.cells(id,objGrid.getColIndexById("r_user_name")).getValue());
						var recvFileName    = (objGrid.getColIndexById("r_v_filename")==undefined?	"" : objGrid.cells(id,objGrid.getColIndexById("r_v_filename")).getValue());
						var recMemo 		= (objGrid.getColIndexById("r_memo_info")==undefined?   "" : objGrid.cells(id,objGrid.getColIndexById("r_memo_info")).getValue());
						var recCustName 	= (objGrid.getColIndexById("r_cust_name")==undefined?   "" : objGrid.cells(id,objGrid.getColIndexById("r_cust_name")).getValue());
						var recVolume 		= (objGrid.getColIndexById("r_rec_volume_value")==undefined?  "1" : objGrid.cells(id,objGrid.getColIndexById("r_rec_volume_value")).getValue());

						// 녹취 파일 정보

						var recFileData = {
								"listenUrl"		: listenUrl
							,	"recDate"		: recDate.replace(/-/gi,'')			// 녹취일
							,	"recTime"		: recTime				// 녹취시간
							,	"recExt"		: recExt				// 내선버노
							,	"recCustPhone"	: recCustPhone			// 고객
																		// 저나버노
							,	"recUserName"	: recUserName			// 상담원 명
							,	"recvFileName" 	: recvFileName			// 파일 명
							,	"recMemo"		: recMemo				// 메모
							, 	"recCustName"	: recCustName			// 고객명
							, 	"recVolume"		: recVolume				// 볼륨 증폭
																		// 값
						}
						rc.addPlayList(recFileData);
					}
				}
			}
		break;
		case "playerToggle":
			if(STTPlayer=='Y')
				top.playerToggle();
			else{
				/**
				 * |
				 * 
				 * @author DAVID |
				 * @type funtcion |
				 * @param event |
				 * @return void; |
				 * @description
				 */
				 window.addEventListener("click",function(e){
					 var x,y;
					 x = e.clientX;
					 y = e.clientY;
					
					 if((e.target.className == "dhxtoolbar_text" && e.target.textContent == lang.views.search.alert.msg79/*
																															 * "플레이어
																															 * 변경"
																															 */) ||e.target.nameProp=="icon_btn_change_gray.png")
						 playerChangeMenu(x,y,true);
					 else
						 playerChangeMenu(x,y,false);

					// 이벤트가 중첩되어 마지막에 이벤트 삭제 추가
					this.removeEventListener("click",arguments.callee,false);
				 })
			}
		break;
		case "addMyFolder":
			var checkItem  = gridSearch.getCheckedRows(0);

			if(checkItem == ""){
				alert(lang.views.search.alert.msg4/* "선택된 녹취 파일이 없습니다." */);
			}else{
				$.ajax({
					url:contextPath+"/selectMyfolder.do",
					data : {
						userId : userInfoJson.userId
					},
					type:"POST",
					dataType:"json",
					async: false,
					success:function(jRes){
						if(jRes.success=="Y"){
							$("#myfolderSelect").empty();
							$('#myfolderSelect').append(jRes.resData.myFolderList);
							layer_popup("#addMyfolderPopup");
						}else if(jRes.result=="myFolder is not exist"){
							// else의 경우 '마이폴더 목록을 불러오는데 실패했습니다.' msg추가하기
							alert(lang.views.search.alert.msg5/*
																 * "마이폴더에 추가된
																 * 폴더가 없습니다."
																 */);
							$("#addMyfolderPopup .ui_row_input_wrap .ui_padding").empty();
							$("#addMyfolderPopup .ui_row_input_wrap .ui_padding").append('<label class="ui_label_essential">'+lang.common.label.folderName+'</label><input class="" id="addFolderName" value="" type="text" maxlength="15"/>');
							$("#addMyfolderItem").attr('onclick', 'addMyfolder(addFolderName.value);');
							layer_popup("#addMyfolderPopup");
						}
					}
				});
				
// if(myFolderSize != "0")
// layer_popup("#addMyfolderPopup");
// else
// alert(lang.views.search.alert.msg5/*"마이폴더에 추가된 폴더가 없습니다."*/);
			}
		break;
		case "individualUpload":
			var checkItem  = gridSearch.getCheckedRows(0);
			
			if(checkItem == ""){
				alert(lang.views.search.alert.noSelectRec/*
															 * "선택된 녹취 파일이
															 * 없습니다."
															 */);
			}else{
				// 이미 즉시신청된 파일인지 확인하기...
				if(confirm(lang.views.search.alert.directUpload/*
																 * "선택된 녹취 파일을
																 * 즉시 업로드
																 * 하시겠습니까?"
																 */)){
					uploadSend();
				}
			}
		break;
		case "rsRecfileUpdate":
			// 새로추가
			var chCol = -1;
			for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
				if(objGrid.getColType(i) == "ch") {
					chCol = i;
					break;
				}
			}

			var checkGrid = objGrid.getCheckedRows(chCol);
			
			if(checkGrid==""){
				alert(lang.views.search.alert.msg31/* "선택된 파일이 없습니다." */);
				return false;
			}
			
			var checked = objGrid.getCheckedRows(chCol).split(",");
				
			if(checked.length ==1){
				var id=checked[0];

				var urldate={
						recDate : (gridSearch.getColIndexById("r_rec_date")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_date")).getValue()).replace(/-/gi,''),
						recTime	: (gridSearch.getColIndexById("r_rec_time")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_time")).getValue()).replace(/:/gi,''),
						recExt	: (gridSearch.getColIndexById("r_ext_num")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_ext_num")).getValue()),
						sysCode : (gridSearch.getColIndexById("r_v_sys_code") ==undefined ? "" : gridSearch.cells(id,gridSearch.getColIndexById("r_v_sys_code")).getValue())
				};		
				
				$.ajax({
					url:contextPath+"/getRsRecfileInfo.do",
					data : {
						recDate : urldate.recDate,
						recTime : urldate.recTime,
						recExt : urldate.recExt,
						sysCode : urldate.sysCode
					},
					type:"POST",
					dataType:"json",
					async: false,
					success:function(jRes){
						if(jRes.success=="Y"){
							
							var value = jRes.resData.SearchData;
							$("#rsRecfileColumn").html("");
							$("#rsRecfileColumn").append("<label>"+convertLanguage("views.search.grid.head.R_REC_DATE")+"</label>")
							$("#rsRecfileColumn").append("<input id='rec_date' value='"+urldate.recDate+"' readonly='readonly' type='text'/>")
							$("#rsRecfileColumn").append("<label>"+convertLanguage("views.search.grid.head.R_REC_TIME")+"</label>")
							$("#rsRecfileColumn").append("<input id='rec_time' value='"+urldate.recTime+"' readonly='readonly' type='text'/>")
							$("#rsRecfileColumn").append("<label>"+convertLanguage("views.search.grid.head.R_EXT_NUM")+"</label>")
							$("#rsRecfileColumn").append("<input id='ext_num' value='"+urldate.recExt+"' readonly='readonly' type='text'/>")
							$("#rsRecfileColumn").append("<label>"+convertLanguage("views.search.grid.head.R_V_SYS_CODE")+"</label>")
							$("#rsRecfileColumn").append("<input id='v_sys_code' value='"+urldate.sysCode+"' readonly='readonly' type='text'/>")
							$.each(value,function(key,value){								
								// views.search.grid.head.
								$("#rsRecfileColumn").append("<label>"+convertLanguage("views.search.grid.head."+key.toUpperCase())+"</label>")
								if (value == null) {
									value = "";
								} 
								$("#rsRecfileColumn").append("<input id='"+key.replace('r_','')+"' value='"+value +"' type='text'/>")
							    // console.log("key : " + key + " | value : " +
								// value)
							});
							
							// $('#myfolderSelect').append(jRes.resData.myFolderList);
							layer_popup("#rsRecfileUpdatePopup");
						}else{
							alert('Load Fail')
							return;
						}
					}
				});
				
			}else{
				alert(lang.views.search.alert.msg90/* 한개의 파일만 선택해 주세요. */);
			}
			break;
		case "fileDownload":
			
			downId = new Array();
			
			var chCol = -1;
			for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
				if(objGrid.getColType(i) == "ch") {
					chCol = i;
					break;
				}
			}

			if(chCol != -1 && objGrid.getCheckedRows(chCol) != "" ) {
				var checked = objGrid.getCheckedRows(chCol).split(",");

				for( var i = 0 ; i < checked.length ;i++ ) {

					var id = checked[i];
			
			
					// var id = gridSearch.getSelectedRowId();
					var result = checkApprove(id,"down");
					if(downloadApprove=='N'){
						// alert(lang.views.search.alert.msg6/*"다운로드 신청 권한이
						// 없습니다."*/)
						// return false;
					}else if(result == "prereciptWait"){
						// alert(lang.views.search.alert.msg7/*"지점 승인 대기중
						// 입니다."*/)
						// return false;
					}else if(result == "prereciptReject"){
						// if(confirm(lang.views.search.alert.msg8/*"지점 승인 거부된
						// 요청 입니다.\n재 요청 하시겠습니까?"*/)){
							downId.push(id);
							// openApprovePopup()
							// return false;
						// }
						// return false;
					}else if(result == "reciptWait"){
						// alert(lang.views.search.alert.msg9/*"접수 대기중 입니다."*/)
						// return false;
					}else if(result == "reciptReject"){
						// if(confirm(lang.views.search.alert.msg10/*"접수가 거부된 요청
						// 입니다.\n재 요청 하시겠습니까?"*/)){
							downId.push(id);
							// openApprovePopup()
							// return false;
						// }
						// return false;
					}else if(result == "approveWait"){
						// alert(lang.views.search.alert.msg11/*"승인 대기중 입니다."*/)
						// return false;
					}else if(result == "approveReject"){
						// if(confirm(lang.views.search.alert.msg12/*"승인이 거부된 요청
						// 입니다.\n재 요청 하시겠습니까?"*/)){
							downId.push(id);
							// openApprovePopup()
							// return false;
						// }
						// return false;
					}else if (result == "approveFinish"){
						// if(confirm(lang.views.search.alert.msg13/*"승인 후 허용일이
						// 만료된 요청 입니다.\n재 요청 하시겠습니까?"*/)){
							downId.push(id);
							// openApprovePopup()
							// return false;
						// }
						// return false;
					}else if (result == "none"){
						downId.push(id);
						// openApprovePopup()
						// return false;
					}else if (result == "approve"){
						fileDown(id)
						return false;
					}
		
					function openApprovePopup(){
						if(!$("#approveType").hasClass("ui_input_hasinfo"))
							$("#approveType").addClass("ui_input_hasinfo");
						$("#approveType").val("down");
						$("#approveType").attr("disabled",true);
						$("#approveReason").prop("selectedIndex",0);
						layer_popup("#approvePopup");
			}

			function fileDown(id){
				progress.on();
				fileName = gridSearch.cells(id,gridSearch.getColIndexById('rec_url')).getValue();

				$("#url").val(fileName);
				var nowTime = new Date();
				var requestUrl = parseUri(fileName).authority;

				var url = HTTP+"://"+requestUrl+"/down?"+parseUri(fileName).query+"&cmd=down&time"+nowTime.getTime();
				$("#download").attr("action", url);
						$("#download").submit();
						progress.off();
					}
				}
			}
			if(downId.length > 0){
				openApprovePopup();
			}
			
		break;
		// "+lang.combo.ALL+" 다운
		case "fullDownload":
			window.addEventListener("click",function(e){
				 var x,y;
				 x = e.clientX;
				 y = e.clientY;
				 if(e.target.className == "dhxtoolbar_text" && e.target.textContent == lang.views.search.alert.msg76/*
																													 * "파일
																													 * 다운로드"
																													 */)
					 fileDownMenu(x,y,true);
				 else
					 fileDownMenu(x,y,false);

				// 이벤트가 중첩되어 마지막에 이벤트 삭제 추가
				this.removeEventListener("click",arguments.callee,false);
			 });
			/*
			 * if(objGrid.getRowsNum() == 0){
			 * alert(lang.views.search.alert.noDownTarget)다운로드할 대상이 없습니다. //TODO
			 * }else{ var query = formToSerialize(); progress.on(); $.ajax({
			 * url:contextPath+"/fulldownTime.do"+query, type:"get",
			 * dataType:"json", async: true, cache: false,
			 * success:function(jRes){ if(jRes.success == "Y"){ var totalTime =
			 * jRes.resData.totalTime; var totalRecTime; if(totalTime>3600){
			 * totalRecTime =
			 * Math.floor(totalTime/3600)+lang.views.search.alert.hours"시간
			 * "+Math.floor((totalTime%3600)/60)+lang.views.search.alert.minutes"분
			 * "+Math.floor(((totalTime%3600)%60))+lang.views.search.alert.second"초";
			 * }else if(totalTime>60){ totalRecTime =
			 * Math.floor(totalTime/60)+lang.views.search.alert.minutes"분
			 * "+Math.floor((totalTime%60))+lang.views.search.alert.second"초";
			 * }else{ totalRecTime =
			 * Math.floor(totalTime)+lang.views.search.alert.second"초"; } // 조회된
			 * 녹취파일의 총 통화시간은 totalTime이며 제한용량 초과시 다운로드에 실패 할 수 있습니다. // 다운로드 시작
			 * 후 등록된 녹취파일은 포함되지 않습니다. setTimeout(function(){
			 * 
			 * 
			 * //2GB이상일 경우 다운로드 할 수 없습니다. //다운로드 가능한 총 통화시간(예상치): 240시간
			 * if(confirm(lang.views.search.alert.comfrimFullDown현재 조회하신 조건의 모든
			 * 녹취파일을 다운로드하시겠습니까?+"\n\n"+lang.views.search.alert.caution2GB
			 *//**
																																											 */
			break;
			// "+lang.combo.ALL+"삭제
		case "deleteAll":
			window.addEventListener("click",function(e){
				 var x,y;
				 x = e.clientX;
				 y = e.clientY;
				 if(e.target.className == "dhxtoolbar_text" && e.target.textContent == lang.views.search.alert.msg92/*
																													 * "파일
																													 * 삭제"
																													 */)
					 deleteMenu(x,y,true);
				 else
					 deleteMenu(x,y,false);

				// 이벤트가 중첩되어 마지막에 이벤트 삭제 추가
				this.removeEventListener("click",arguments.callee,false);
			 })
			/*
			 * if(objGrid.getRowsNum() == 0){
			 * alert(lang.views.search.alert.msg83)삭제할 대상이 없습니다. }else{ //db삭제
			 * 유무 var dbDel='N';
			 * 
			 * if(confirm(lang.views.search.alert.msg84)){현재 조회하신 조건의 모든 녹취파일을
			 * 삭제하시겠습니까? if(confirm(lang.views.search.alert.msg85)){현재 조회하신 조건의
			 * 모든 녹취이력 또한 삭제하시겠습니까? //디비도 삭제하는 경우 dbDel='Y' }
			 * 
			 * var query = formToSerialize();
			 * 
			 * progress.on();
			 * 
			 * $.ajax({ url:contextPath+"/delete_all.do"+query, type:"get",
			 * dataType:"json", async: true, cache: false,
			 * success:function(jRes){
			 * 
			 * if(jRes.success=="Y"){ //삭제 결과 담을 배열 var succeseList = [];
			 * 
			 * var recIpList = jRes.resData.serverIpList; var whereQuery =
			 * jRes.resData.whereQuery; var listenType =
			 * jRes.resData.listenType; console.log(recIpList)
			 * 
			 * for(var i=0;i<recIpList.length;i++){ var dataStr = {
			 * "whereQuery" :whereQuery, "dbDel" : dbDel, "ip" :
			 * recIpList[i].split("://")[1].split(":")[0], "listenType"
			 * :listenType }
			 * 
			 * $.ajax({ url:recIpList[i]+"delete",
			 * //url:"http://127.0.0.1:28881/delete", type:"post", data:dataStr,
			 * async: true, cache: false, success:function(jRes){
			 * console.log(i+"::"+jRes) succeseList.push(jRes); } });
			 *  } var inter = setInterval(function(){ //삭제 결과 확인하는 인터벌 $.ajax({
			 * url:contextPath+"/keepAlive.do", dataType:"json", async: true,
			 * cache: false, success:function(jRes){
			 * if(succeseList.length==recIpList.length){ //클리어 인터벌
			 * clearInterval(inter); progress.off(); succeseList.forEach(
			 * function( v, i ){ if( v === 'N' ){
			 * alert(lang.views.search.alert.msg88)//녹취 삭제에 실패하였습니다. return
			 * false; } }); alert(lang.views.search.alert.msg86)//삭제에 성공하였습니다.
			 * if(dbDel=='Y'){
			 * gridSearch.clearAndLoad(contextPath+"/search_list.xml" +
			 * formToSerialize(), function() { gridContextUrl =
			 * contextPath+"/search_list.xml" + formToSerialize();
			 * gridSearch.changePage(1)
			 * 
			 * var evalColum=gridSearch.getColIndexById("evaluation");
			 * 
			 * //나중에 1001은 지워야 함 테스트용
			 * if(evalColum!=undefined&&(userInfoJson.userLevel == "E1006" ||
			 * userInfoJson.userLevel == "E1005")){
			 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌 }else
			 * if(userInfoJson.userLevel == "E1001"){
			 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌
			 * }else{ gridSearch.setColumnHidden(evalColum, true); // 평가 컬럼을
			 * 사라지게 함 } }); } } }, error : function(error) { //클리어 인터벌
			 * clearInterval(inter); progress.off();
			 * alert(lang.views.search.alert.msg87)//녹취 삭제에 실패하였습니다. } });
			 * },3000)
			 * 
			 * }else { //아이피 읽어오기 실패 alert(lang.views.search.alert.msg87)//녹취
			 * 삭제에 실패하였습니다. } } }); }else{ return false; } }
			 */
		break;
		case "fileDivision":			
			var gridArr = gridSearch.getCheckedRows(0).split(",");
			if(gridArr == ""){
				alert(lang.views.search.alert.msg4/* "선택된 녹취 파일이 없습니다." */);
			}
			
			var recDateArr = [];
			var recTimeArr = [];
			var extArr = [];
			var sysCodeArr = [];
			var chkDivision = "";
			for (var i = 0; i < gridArr.length; i++) {
				/*
				 * chkDivision =
				 * gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_division")).getValue();
				 * if (chkDivision != "-") {
				 * alert(lang.views.search.alert.already.division "선택한 파일 중 이미
				 * 분배된 녹취 파일이 있습니다."); return; }
				 */
				recDateArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,''))
				recTimeArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,''))
				extArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_ext_num")).getValue())
				sysCodeArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_v_sys_code")).getValue())
			}

			$.ajax({
				url : contextPath + "/transcriptListProc.do",
			 	type : "POST",
			 	data : {
			 		 	recDateArr : recDateArr.toString()
		 		 	,	recTimeArr : recTimeArr.toString()
		 		 	,	extArr : extArr.toString()
		 		 	,	sysCodeArr : sysCodeArr.toString()
		 		 	,	procType : "insert"
			 	},
			 	dataType:"json",
			 	async: true,
			 	success:function(jRes){
			 		if(jRes.success == "Y"){
			 			alert(lang.views.search.alert.recfile.division.success /*
																				 * "분배리스트에
																				 * 선택한
																				 * 녹취파일을
																				 * 추가하였습니다."
																				 */);
			 			gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize());
			 		}else{
			 			if (jRes.resData.msg == "inserted") {
			 				alert(lang.views.search.alert.already.division /*
																			 * "선택한
																			 * 파일 중
																			 * 이미
																			 * 분배된
																			 * 녹취
																			 * 파일이
																			 * 있습니다."
																			 */);
			 			} else { 
			 				alert(lang.views.search.alert.recfile.division.fail /*
																				 * "분배리스트에
																				 * 선택한
																				 * 녹취파일
																				 * 추가를
																				 * 실패
																				 * 하였습니다."
																				 */);
			 			}
			 		}
			 	}
			})			
			break;
		}
	});
}


function saveRecVol() {
	var volume = $("#volume").val();
	var id = $("#hiddenRowId").val();
	gridSearch.cells(id,gridSearch.getColIndexById("r_rec_volume_value")).setValue(volume);
	var obj = gridSearch.cells(id,gridSearch.getColIndexById("r_rec_volume"));	
	switch(volume) {
	case "1" : 
		obj.setValue(lang.views.search.column.value.volume/* "기본" */);
		break;
	case "2" :
		obj.setValue(lang.views.search.column.value.volumeTwo/* "2배" */);
		break;
	case "3" :
		obj.setValue(lang.views.search.column.value.volumeThree/* "3배" */);
		break;
	}
	
	layer_popup_close("#setRecVolume");
}
// 그리드 관련 기능
function gridFunction(){
	// 더블 클릭시 파일 재생
	gridSearch.attachEvent("onRowDblClicked", function(id,ind){
		if (gridSearch.getColIndexById("r_rec_volume") != null && ind == gridSearch.getColIndexById("r_rec_volume")) {
			var vol = gridSearch.cells(id,gridSearch.getColIndexById("r_rec_volume_value")).getValue();
			
			$("#hiddenRowId").val(id);
			$("#volume").val(vol);
			
			layer_popup("#setRecVolume");
		}else if(listenYn === "Y"){
			nowListenNo = gridSearch.getColIndexById("rownumber")==undefined ? 	1 : gridSearch.cells(id,gridSearch.getColIndexById("rownumber")).getValue();
			
			openFaceRecordingPlayer("gridSearch");
			layer_popup("#faceRecordingPlayer");
		}else{
			
			if(telnoUse == 'Y'){
				alert(lang.log.etc.noAuthy/* "권한없음" */)
				return false;
			}

			var result = checkApprove(id,"listen");

			if(result == "prereciptWait"){
				alert(lang.views.search.alert.msg14/* "지점 승인 대기중 입니다." */)
				return false;
			}else if(result == "prereciptReject"){
				if(confirm(lang.views.search.alert.msg15/*
														 * "지점 승인 거부된 요청 입니다.\n재
														 * 요청 하시겠습니까?"
														 */)){
					openApprovePopup()
					return false;
				}
				return false;
			}else if(result == "reciptWait"){
				alert(lang.views.search.alert.msg16/* "접수 대기중 입니다." */)
				return false;
			}else if(result == "reciptReject"){
				if(confirm(lang.views.search.alert.msg17/*
														 * "접수가 거부된 요청 입니다.\n재
														 * 요청 하시겠습니까?"
														 */)){
					openApprovePopup()
					return false;
				}
				return false;
			}else if(result == "approveWait"){
				alert(lang.views.search.alert.msg18/* "승인 대기중 입니다." */)
				return false;
			}else if(result == "approveReject"){
				if(confirm(lang.views.search.alert.msg19/*
														 * "승인이 거부된 요청 입니다.\n재
														 * 요청 하시겠습니까?"
														 */)){
					openApprovePopup()
					return false;
				}
				return false;
			}else if (result == "approveFinish"){
				if(confirm(lang.views.search.alert.msg20/*
														 * "승인 후 허용일이 만료된 요청
														 * 입니다.\n재 요청 하시겠습니까?"
														 */)){
					openApprovePopup()
					return false;
				}
				return false;
			}else if (result == "none"){
				openApprovePopup()
				return false;
			}else if (result == "approve"){
				checkSoundDevice(this,id)
				return false;
			}

			function openApprovePopup(){
				if(!$("#approveType").hasClass("ui_input_hasinfo"))
					$("#approveType").addClass("ui_input_hasinfo");
				$("#approveType").attr("disabled",true);
				$("#approveType").val("listen");
				$("#approveReason").prop("selectedIndex",1);
				layer_popup("#approvePopup");
			}
		}
	});

	// 단순 row 셀렉트 무시
	gridSearch.attachEvent("onRowSelect", function(id,ind){});

	// 더블 클릭시 파일 재생
	traceList.attachEvent("onRowDblClicked", function(id,ind){

		if(listenYn === "Y"){
			checkSoundDevice(this,id)
			// play(this,id)
		}else{
			$("#approveType").removeClass("ui_input_hasinfo");
			$("#approveType").attr("disabled",false);
			if(downloadApprove=='N'){
				$("#approveType").addClass("ui_input_hasinfo");
				$("#approveType").val("listen");
				$("#approveType").attr("disabled",true);
			}
			$("#approveReason").prop("selectedIndex",0);
			layer_popup("#approvePopup");
		}
	});
	
	// 더블 클릭시 파일 재생
	recSectionList.attachEvent("onRowDblClicked", function(id,ind){

		if(listenYn === "Y"){
			checkSoundDevice(this,id)
			// play(this,id)
		}else{
			$("#approveType").removeClass("ui_input_hasinfo");
			$("#approveType").attr("disabled",false);
			if(downloadApprove=='N'){
				$("#approveType").addClass("ui_input_hasinfo");
				$("#approveType").val("listen");
				$("#approveType").attr("disabled",true);
			}
			$("#approveReason").prop("selectedIndex",0);
			layer_popup("#approvePopup");
		}
	});
	
	// 트레이스 리스트 헤더 클릭 방지 이벤트 처리
	traceList.attachEvent("onHeaderClick",function(){return false;})

	recSectionList.attachEvent("onHeaderClick",function(){return false;})
	
	
	// 체크박스 "+lang.combo.ALL+" 선택
	/*
	 * gridSearch.attachEvent("onHeaderClick",function(ind, obj){ if(ind ==
	 * gridSearch.getColIndexById("r_check_box") && obj.type == "click") {
	 * if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
	 * gridSearch.setCheckedRows(ind, 1); $("#allcheck>img").attr("src",
	 * recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif"); } else {
	 * gridSearch.setCheckedRows(ind, 0); $("#allcheck>img").attr("src",
	 * recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif"); } }
	 * else { return true; } });
	 */

	// 페이지 체인지 이벤트
	gridSearch.attachEvent("onBeforePageChanged", function(){
		if(!this.getRowsNum()){
			return false;
		}
		return true;
	});

	// 소팅 이벤트 커스텀
	gridSearch.attachEvent("onBeforeSorting", function(ind){
		
		if(csvFlag==1){
			// csv sorting 기능 추가해야함
			// return ind; 할 경우 컬럼 sort이미지는 반영되나 type이 server로 되어 있어서 그런지
			// sorting이 안됨
		}else{
			if($("#sDate").val().length != 10 || $("#eDate").val().length != 10)
				alert(lang.views.search.alert.msg21/*
													 * "녹취 일자를 다음 형식에 맞게 입력 해
													 * 주세요!\n예)2018-01-01"
													 */);
			else{
				var a_state = this.getSortingState()
				
				var direction = "asc"
					if(nowSortingColumn==ind)
						direction = ((a_state[1] == "asc") ? "desc" : "asc");
				
				var columnId = this.getColumnId(ind);
				
				if ("r_rec_date" == columnId)
					dateColumnState = direction
					if ("r_rec_time" == columnId)
						timeColumnState = direction
						var requestOrderby = columnId+"|"+direction;
				
				var nowPage = this.getStateOfView()[0]
				this.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(requestOrderby));
				this.setSortImgState(true,ind,direction)
				
				var a_state = this.getSortingState()
				nowSortingColumn = a_state[0];
			}
		}
	});

	gridSearch.sortRows = function(col,type,order){};

	// 고객 번호 마우스 오버 툴팁 이벤트 처리
	gridSearch.attachEvent("onMouseOver", function(id, ind){

		var columnId = this.getColumnId(ind)
		switch (columnId){
			case "r_cust_phone1":
			case "r_cust_phone2":
			case "r_cust_phone3":
			case "r_cust_name":
			case "r_user_name":
				if(false/* maskingYn == "Y" */){
					var oriData = this.getUserData(id,columnId);
					if (oriData != null && oriData != undefined && oriData != "")
						this.cells(id,ind).cell.title = oriData;
					else
						this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
					break;
				}
			default:
				this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
				break;
		}

	});

	// 고객 번호 마우스 오버 툴팁 이벤트 처리
	traceList.attachEvent("onMouseOver", function(id, ind){

		var columnId = this.getColumnId(ind)
		switch (columnId){
			case "r_cust_phone1":
			case "r_cust_phone2":
			case "r_cust_phone3":
			case "r_cust_name":
			case "r_user_name":
				if(false/* maskingYn == "Y" */){
					var oriData = this.getUserData(id,columnId);
					if (oriData != null && oriData != undefined && oriData != "")
						this.cells(id,ind).cell.title = oriData;
					else
						this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
					break;
				}
			default:
				this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
				break;
		}

	});
		
	recSectionList.attachEvent("onMouseOver", function(id, ind){

		var columnId = this.getColumnId(ind)
		switch (columnId){
			case "r_cust_phone1":
			case "r_cust_phone2":
			case "r_cust_phone3":
			case "r_cust_name":
			case "r_user_name":
				if(false/* maskingYn == "Y" */){
					var oriData = this.getUserData(id,columnId);
					if (oriData != null && oriData != undefined && oriData != "")
						this.cells(id,ind).cell.title = oriData;
					else
						this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
					break;
				}
			default:
				this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
				break;
		}

	});

	// 단순 row 셀렉트 무시
	traceList.attachEvent("onRowSelect", function(id,ind){});
	
	recSectionList.attachEvent("onRowSelect", function(id,ind){});
}

// 폼 관련 기능
function formFunction(){
	var checkDate=null;
	var checkSearch=null;
	if(listenPeriod!=''){
		checkDate= oneMonthBefore(new Date(),listenPeriod);
	}
	checkSearch = oneMonthBefore(new Date(),6);
	// 조회 버튼 연동
	$("#searchBtn").click(function(){
		if($("#sDate").val().length != 10 || $("#eDate").val().length != 10){
			if($("#sDate").val().length == 8){
				var dateTemp = $("#sDate").val();
				dateTemp = dateTemp.substring(0,4)+"-"+dateTemp.substring(4,6)+"-"+dateTemp.substring(6,8);
				$("#sDate").val(dateTemp);
			}else if($("#eDate").val().length == 8){
				var dateTemp = $("#eDate").val();
				dateTemp = dateTemp.substring(0,4)+"-"+dateTemp.substring(4,6)+"-"+dateTemp.substring(6,8);
				$("#eDate").val(dateTemp);
			}else{					
				alert(lang.views.search.alert.msg1/*
													 * "녹취 일자를 다음 형식에 맞게 입력 해
													 * 주세요!\n예)2018-01-01"
													 */);
			}
		}/*
			 * else if(listenPeriod!="" && (checkDate-new
			 * Date($("#sDate").val()))>0){
			 * alert(lang.views.search.alert.listenPeriod1+listenPeriod+lang.views.search.alert.listenPeriod2) }
			 * else
			 * if($("input#userName.inputFilter.korFilter.engFilter.numberFilter").val().length >
			 * 0 &&
			 * $("input#userName.inputFilter.korFilter.engFilter.numberFilter").val().length <
			 * 2){ alert('2'+lang.views.search.alert.nameCheck) } else
			 * if($("#custName").val().length > 0 && $("#custName").val().length <
			 * 2){ alert('2'+lang.views.search.alert.nameCheck) } else
			 * if($("#custPhone1").val().length > 0 &&
			 * $("#custPhone1").val().length < 4){
			 * alert('4'+lang.views.search.alert.nameCheck) }
			 */	
		
		else{
			csvFlag = 0;
			gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
				gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
				gridSearch.changePage(1)

				var evalColum=gridSearch.getColIndexById("evaluation");
										
				
				// 나중에 1001은 지워야 함 테스트용
				/*
				 * if(evalColum!=undefined&&(userInfoJson.userLevel == "E1006" ||
				 * userInfoJson.userLevel == "E1005")){
				 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌
				 * }else if(userInfoJson.userLevel == "E1001"){
				 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌
				 * }else{ gridSearch.setColumnHidden(evalColum, true); // 평가 컬럼을
				 * 사라지게 함 }
				 */
			});
		}
	});

	
	$("#searchClear").click(function(){
		$("input").val('');
		$("select").prop('selectedIndex',0);
		datepickerSetting(locale,'#sDate, #eDate');
	});
	
	$("#searchCSV").click(function(){
		$("#upLoadFile_hidden1").val("");
		$('#upload_name1').val(lang.views.search.alert.msg107);
		layer_popup("#csvLoad");
	});
	
	$("#formDownBtn").click(function(){
		if( !$("#formDownloadFrame").length)
			$("body").append('<iframe name="formDownloadFrame" id="formDownloadFrame" src="" style="width:0px;height:0px;"></iframe>')
			
		$("#formDownloadFrame").attr("src",contextPath+"/formDownload.do");
	});
	
	
	$("#listen_popup_close").click(function(){
		$('#playerFrame').contents().find(".btn_init").click();
	});
	
	// csv 업로드 기능
	$("#uploadBtn").click(function(){
		var fileName=$('#upload_name1').val().split('.');
		var fileNameChk = fileName[fileName.length-1]
		if(fileNameChk.toLowerCase()!='csv'){
			alert(lang.views.search.alert.msg111);/* 업로드 하는 파일은 csv 파일이 아닙니다. */ 
			return false;
		}
		
		progress.on();
		
		var csvDataList = new Array();
		
		var csvFile = new FileReader();
		csvFile.readAsText($("#upLoadFile_hidden1")[0].files[0]);
		csvFile.onload = function(){
			csvDataList = csvFile.result.split("\n");
			csvDataList.pop();
			
			if(csvDataList.length<2){
				alert(lang.views.search.alert.msg112);/* 데이터가 없습니다. */
				progress.off();
				return false;
			}
			
			// 업로드 데이터 유효성 검사
			$.ajax({
				url:contextPath+"/csvUpload.do",
				data:{
					csvDataList : csvDataList
				},
				type:"POST",
				dataType:"json",
				success:function(jRes){
					csvT = jRes.resData.csvT;
					csvF = jRes.resData.csvF;
					csvA = [].concat(csvT, csvF);
					csvColName = jRes.resData.colName;
					/*
					 * $("input[name='uploadResult']").removeAttr("checked");
					 * $("input[name='uploadResult']:radio[value='a']").attr("checked",true);
					 */ 
					$("input[name='uploadResult']:radio[value='a']").prop("checked",true);
					
					layer_popup_close();
					
					try{
						csvGrid.destructor();
					}catch(e){
						
					}
					var initWidths="", colAlign="", colTypes="", colLen=jRes.resData.csvF.length==0?0:jRes.resData.csvF[0].split(",").length;
					
					for (var i = 0; i < colLen; i++) {
						if(i == colLen-1){
							initWidths += "100";
							colAlign += "center";
							colTypes += "ed";
						}else{
							initWidths += "100,";
							colAlign += "center,";
							colTypes += "ed,";
						}
					}
					
					if(initWidths == "") initWidths = "100";
					if(colAlign == "") colAlign ="center"
					if(colTypes == "") colTypes = "ed";

					// 업로드 데이터 그리드 생성
					csvGrid = new dhtmlXGridObject('csvGrid');
					csvGrid.setImagePath(recseeResourcePath + "/images/project/");
					csvGrid.setImagePath(recseeResourcePath + "/images/project/");
					csvGrid.i18n.paging = i18nPaging[locale];
					csvGrid.enablePaging(true, 30, 5, "csvPaging", true);
					csvGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
					csvGrid.enablePreRendering(50);
					csvGrid.setPagingSkin("toolbar", "dhx_web");
					csvGrid.setHeader(csvColName);
					csvGrid.enableColumnAutoSize(true);
					/* csvGrid.setInitWidths(initWidths); */
					csvGrid.setColAlign(colAlign);
					csvGrid.setColTypes(colTypes);
					csvGrid.enablePreRendering(50);
					csvGrid.setSkin("dhx_web");
					csvGrid.init();
					
					
					
					data = {
						rows:[
						]
					}
					
					for (var i = 0; i < csvA.length; i++) {
						var jsonObject = {'id':i+1, 'data':csvA[i].split(",")}
						data.rows.push(jsonObject);
					}
					// 데이터 로드
					csvGrid.parse(data,"json");
					
					$("#resultCnt").empty();
					$("#resultCnt").append("<label class='ui_label_essential'></label>원본:"+jRes.resData.cnt[0]+", 중복:"+jRes.resData.cnt[1]+", 유효:"+jRes.resData.cnt[2]+", 오류:"+jRes.resData.cnt[3]);
					
					$("#csvGrid").css("height", ($('#csvPopup').css('height').replace('px','')-140)+"px");
					layer_popup("#csvPopup", true,{minWidth:450, minHeight:550});
					for(var i=0; i< csvA.length; i++){
	                	if(i < csvT.length){
	                        	csvGrid.setRowColor(i+1,'white');
	                	}else{
	                        	csvGrid.setRowColor(i+1,'#D3D3D3');
	                	}
					}

					progress.off();
					
				}, error : function(error) {
					progress.off();
	                alert("error");
	                console.log(error);
	                console.log(error.status);
				}
			});
		}
	});
	// 재녹취 팝업 띄우기
	$("#retryRec").click(function(){
		$("#approvalNumberValue").val("");
		layer_popup("#approvalNumberPopup");
	});
	
	// 다음 녹취 재생
	$("#playNext").click(function(){
		nowListenNo = Number(nowListenNo)+1;
		if (gridSearch.getRowsNum() < nowListenNo) {
			nowListenNo = "1";
		}
		openFaceRecordingPlayer();
	});
	
	
	// csv 업로드 결과 그리드에서 전체/유효/오류 탭 변경 기능(라디오태그 활용)
	$("input[name=uploadResult]").click(function(){
		if($(this).val()=='a'){
			progress.on();
			
			csvGrid.clearAll();
			data = {
					rows:[
					]
				}
			for (var i = 0; i < csvA.length; i++) {
				var jsonObject = {'id':i+1, 'data':csvA[i].split(",")}
				data.rows.push(jsonObject);
			}

			csvGrid.parse(data,"json");

			for(var i=0; i< csvA.length; i++){
                                if(i < csvT.length){
					csvGrid.setRowColor(i+1,'white');
				}else{
					csvGrid.setRowColor(i+1,'#D3D3D3');
				}
                        }

			
			progress.off();
		}else if($(this).val()=='t'){
			progress.on();
			
			csvGrid.clearAll();
			data = {
					rows:[
					]
				}
			for (var i = 0; i < csvT.length; i++) {
				var jsonObject = {'id':i+1, 'data':csvT[i].split(",")}
				data.rows.push(jsonObject);
			}
			csvGrid.parse(data,"json");
			
			for (var i = 0; i < csvT.length; i++) {
                        	csvGrid.setRowColor(i+1,'white');
			}

			progress.off();
		}else if($(this).val()=='f'){
			progress.on();
			
			csvGrid.clearAll();
			data = {
					rows:[
					]
				}
			for (var i = 0; i < csvF.length; i++) {
				var jsonObject = {'id':i+1, 'data':csvF[i].split(",")}
				data.rows.push(jsonObject);
			}
			csvGrid.parse(data,"json");
			
			for(var i=0; i< csvF.length; i++){
				csvGrid.setRowColor(i+1,'#D3D3D3');
			}

			progress.off();
		}
	});
	
	$("#csvSearchBtn").click(function(){
		if(!csvT.length>0){
			alert(lang.views.search.alert.msg112);/* 데이터가 없습니다. */
			layer_popup_close();
			return false;
		}
		csvFlag = 1;
		// post 방식은 clearAndLoad 기능이 없어 clear처리를 별도로 해야함.
		gridSearch.clearAll();
		// grid post방식 호출(request parameter의 데이터가 많은 경우)
		gridSearch.post(contextPath+"/search_list.xml" + formToSerialize(), "csv="+csvFlag+"&csvColName="+csvColName.join(",")+"&csvList="+csvT.join("!e!"), function(){
			// csvT데이터는 전역변수로 따로 저장하므로 contextUrl에는 그 외 parameter 값만 저장
			gridContextUrl = contextPath+"/search_list.xml" + formToSerialize()+"&csv="+csvFlag+"&csvColName="+csvColName.join(",");
			gridSearch.changePage(1);
			
			var evalColum=gridSearch.getColIndexById("evaluation");
		});
		layer_popup_close();
	});
	
	// 엔터 처리
	$('.main_form').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});

	// 날짜 셋팅
// $('#sDate, #eDate').datepicker().datepicker("setDate", new Date());
	
	var firstDateSetting = true;
	if (firstDateSetting) {
		datepickerSetting(locale,'#sDate, #eDate');
		firstDateSetting = false;
	}
	$("#sDate").click(function() {
		if (!firstDateSetting) {
			var sDate = $("#sDate").val();
			sDate = sDate.substring(0,4) + "-" + sDate.substring(4,6) + "-" + sDate.substring(6);
			if (sDate.length == 10) {
// datepickerSetting(locale,'#sDate',sDate);
        $("#sDate").datepicker().datepicker("setDate", sDate);
			}
		}
	});
		
	$("#eDate").click(function() {
		if (!firstDateSetting) {
			var eDate = $("#eDate").val();
			eDate = eDate.substring(0,4) + "-" + eDate.substring(4,6) + "-" + eDate.substring(6);
			if (eDate.length == 10) {
// datepickerSetting(locale,'#eDate',eDate);
        $("#eDate").datepicker().datepicker("setDate", eDate);
			}
		}
	});
	
	

	// 청취 / 다운 요청
	// 그룹 로드
	// 요청 사유 로드
	selectOrganizationLoad($("#approveUserGroup"), "sgCode", userInfoJson.bgCode, userInfoJson.mgCode, userInfoJson.sgCode,"","","Y")
	searchSelectOptionLoad($("#approveReason"),"/selectOption.do?comboType=common&comboType2=approveReason&ALL=not");
	searchSelectOptionLoad($("#approveType"),"/selectOption.do?comboType=common&comboType2=approveType&ALL=not");
	selectOrganizationLoad($("#recCategory"), "category",undefined,undefined,undefined,true,undefined);
	selectOrganizationLoad($("#recKeyword"), "keyword",undefined,undefined,undefined,true,undefined);

	// 카테고리, 키워드, 통화내용 검색 창 세팅
	$("#recCategory").attr("multiple","multiple").css("width","500").select2({
			placeholder: lang.views.search.grid.head.R_REC_CATEGORY
		,	allowClear: true
	});
	$("#recKeyword").attr("multiple","multiple").css("width","500").select2({
		placeholder: lang.views.search.grid.head.R_REC_KEYWORD
	,	allowClear: true
	});
	$("#recText").css("width","500");
	
	// 청취 / 다운 팝업 승인 버튼
	// 취소 버튼은 onclick으로 처리되어 이씀 ㅎㅎ..
	$("#approveBtn").click(function(){
		if($("#approveDay").val().length=2){
			if($("#approveDay").val().substr(0,1)=="0")
				$("#approveDay").val($("#approveDay").val().substr(1,1));
		}

		if($("#approveDay").val()=="0"){
			alert(lang.views.search.alert.msg22/* "요청기간은 1일보다 길거나 같아야 합니다" */)
			return false;
		}

		// 요청일이 비어있을 경우 자동 7일 기입
		$("#approveDay").val($("#approveDay").val()||7)
			
			for(var j=0; j< downId.length;j++){			
				var dataStr;
				if(fileNameSettingYN == "Y"){
				
					if($("#fileNameSetting").select2("data").length ==0){
						alert(lang.views.search.alert.msg118/*
															 * "파일 형식을 지정하여
															 * 주십시오."
															 */)
						return false;
					}
					
					var FileSettingColumnKey = new Array();
					for(var i =0; i < $("#fileNameSetting").select2("data").length; i++ ){
						FileSettingColumnKey.push($("#fileNameSetting").select2("data")[i].text);
					}
					
					var FileSettingColumnValue = new Array();
					for(var i =0; i < $("#fileNameSetting").select2("data").length; i++ ){
						var FileColumnValue = $("#fileNameSetting").select2("data")[i].id.indexOf("r_") != -1 ? FileColumnValue = $("#fileNameSetting").select2("data")[i].id : "'"+$("#fileNameSetting").select2("data")[i].id+"'";
						
						FileSettingColumnValue.push(FileColumnValue);
					}
					
					var FileColumnKey = FileSettingColumnKey.join(",");
					var FileColumnValue = FileSettingColumnValue.join(",");							
				
				var id = downId[j];// gridSearch.getSelectedRowId();
				dataStr = {
						"sgName" 		: $("#approveUserGroup option:selected").text()
					,	"approveType" 	: $("#approveType").val()
					,	"approveReason" : $("#approveReason").val()
					,	"approveDay" 	: $("#approveDay").val()||7
				// , "fileName" :
				// (gridSearch.getColIndexById("r_v_filename")==undefined? "" :
				// gridSearch.cells(id,gridSearch.getColIndexById("r_v_filename")).getValue())
					,	"recExt"		: (gridSearch.getColIndexById("r_ext_num")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_ext_num")).getValue())
					,	"recDate" 		: (gridSearch.getColIndexById("r_rec_date")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_date")).getValue())
					,	"recTime"		: (gridSearch.getColIndexById("r_rec_time")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_time")).getValue())
					,	"type"			: "insert"
					,   "listenUrl":    (gridSearch.getColIndexById("rec_url")==undefined ? "" : gridSearch.cells(id,gridSearch.getColIndexById("rec_url")).getValue()).replace(/amp;/gi,'')
					,	"FileColumnKey" : FileColumnKey
					,	"FileColumnValue": FileColumnValue
				}
			}else{				
				var id = downId[j];// gridSearch.getSelectedRowId();
					dataStr = {
							"sgName" 		: $("#approveUserGroup option:selected").text()
						,	"approveType" 	: $("#approveType").val()
						,	"approveReason" : $("#approveReason").val()
						,	"approveDay" 	: $("#approveDay").val()||7
					// , "fileName" :
					// (gridSearch.getColIndexById("r_v_filename")==undefined?
					// "" :
					// gridSearch.cells(id,gridSearch.getColIndexById("r_v_filename")).getValue())
						,	"recExt"		: (gridSearch.getColIndexById("r_ext_num")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_ext_num")).getValue())
						,	"recDate" 		: (gridSearch.getColIndexById("r_rec_date")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_date")).getValue())
						,	"recTime"		: (gridSearch.getColIndexById("r_rec_time")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_time")).getValue())
						,	"type"			: "insert"
						,   "listenUrl":    (gridSearch.getColIndexById("rec_url")==undefined ? "" : gridSearch.cells(id,gridSearch.getColIndexById("rec_url")).getValue()).replace(/amp;/gi,'')
					}
				}
				$.ajax({
					url:contextPath+"/approve_proc.do",
					data:dataStr,
					type:"POST",
					dataType:"json",
					async: false,
					cache: false,
					success:function(jRes){
						// if(jRes.success=="Y"){
							// layer_popup_close("#approvePopup");
		    				// alert(lang.views.search.alert.msg23/*"요청이 정상적으로
							// 처리 되었습니다!"*/);
						// }else {
						// if(jRes.result=="overDate")
								// alert(lang.views.search.alert.msg24/*"요청기간은
								// "*/ + jRes.resData.result +
								// lang.views.search.alert.msg25/*"일을 넘을 수
								// 없습니다"*/)
						// else
								// alert(lang.views.search.alert.msg26/*"요청에 실패
								// 하였습니다."*/);
						// }
					}
				});
			}
			layer_popup_close("#approvePopup");
			alert(lang.views.search.alert.msg23/* "요청이 정상적으로 처리 되었습니다!" */);
		});
	$("#deleteBtn").click(function(){
		// let delVolume = $("#delVolume").val();
		var delVolume = $("#delVolume").val();
		// let delType = $("#deleteSelect").val();
		var delType = $("#deleteSelect").val();

		clickDelete(delVolume, delType);
	});
	

	$("#rsRecfileUpdateBtn").click(function(){
		var dataObj = {};
		// 녹취 정보 변경 팝업 내 input 값이 있으면 돌면서 파라미터 문자열 생성
		$('#rsRecfileColumn :input').each(function(index) {
			dataObj[$(this).attr('id')] = $(this).val();
		});
		// var updateData = JSON.stringify(dataObj);
		
		$.ajax({
			url:contextPath+"/updateRsRecfileInfo.do",
			data : dataObj,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success=="Y"){
					alert("녹취 정보가 변경되었습니다.");
					layer_popup_close("#rsRecfileUpdatePopup");
					gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize());
				}else{
					alert("녹취 정보 변경에 실패하였습니다.");
					return;
				}
			}
		});
	});
	
	$("#approvalNumberCheck").click(function() {
		var value = $("#approvalNumberValue").val();
		
		if (value == null || value == '') {
			alert("승인번호를 입력해주세요");
			$("#approvalNumberValue").focus();
			return false;
		} else {
			var id = $("#rowId").val();
			var custNum	= getGridValue(gridSearch, id, "r_cust_phone1");
			var approvalNumber = (parseInt(Number(custNum) *8 /3)).toString();
			approvalNumber = approvalNumber.substring(approvalNumber.length -4, approvalNumber.length);
			if (value == approvalNumber) {
				layer_popup_close("#approvalNumberPopup");
				// 승인
				// $('#playerFrame').contents().find(".btn_init").click();
				
				$.ajax({
					url:contextPath+"/selectRecParamHistory.do",
					data:{"callId1" : getGridValue(gridSearch, id, "r_call_id1")},// 녹취키(그룹키)
					type:"POST",
					dataType:"text",
					async: false,
					success:function(jRes){
						var result = JSON.parse(jRes);
						var params = JSON.parse(result.resData.msg);
						var rectry_om = result.resData.rectry_om;
						params.retryType = "Y"
						if (rectry_om == "1") {
							params.RECTRY_OM = "Y"
						} else {
							params.RECTRY_OM = "N"
						}
						
						$.ajax({
							url:"http://localhost:80",
							data:params,// 녹취키(그룹키)
							type:"POST",
							dataType:"text",
							async: false,
							success:function(res){
								if(res=="001") {
									$('#playerFrame').contents().find(".btn_init").click();
									layer_popup_close("#faceRecordingPlayer");
								} else {
									alert("마이크 및 클라이언트 상태를 확인해주세요.");
								}
							}
						});
					}
				});
			} else {
				alert("승인번호 불일치!\n「사후 점검 부서 담당자」를 통해 승인번호를 확인 하시기 바랍니다..");
				$("#approvalNumberValue").focus();
				return false;
			}
		}
	});
	
	$('#adwDownBtn').click(function() {
		var sTime = new Date($('#sAdwDate').val()).format('yyyyMMdd');
		var eTime = new Date($('#eAdwDate').val()).format('yyyyMMdd');
		var url = contextPath+"/adw/"+sTime+"/"+eTime+"/csv";
		window.open(url);
	})
}

// 스크린 팝업 오픈 [스크린 팝업 버튼 onclick에서 불러옴]
function openScreenPop(param){

	videoFileData = param;

	rc = top.nowRc;
	rc.pause();

	window.open(
				contextPath+"/videoPlayer"
			,	"recseePlayer"
			, 	"resizable=yes"+
			",	minimizable=no"+
			",	location=no"+
			",	menubar=no"+
			",	toolbar=no"+
			",	directories=no"+
			",	top=0"+
			",	left=0"+
			",	height="+screen.height/2+
			",	width="+screen.width*0.75)

}

// 폼 로드
function loadForm(targetObj){
	$.ajax({
		url: contextPath+"/search_item.do",
		data: {},
		type: "POST",
		dataType: "json",
		async: false,
		cache: false,
		success: function(jRes) {
			// var obj = targetObj.append(jRes.resData.htmlString);
			// 크롬 488
			/*
			 * $(obj).find("#sgCode").attr("multiple","multiple").css("width","496").select2({
			 * placeholder: "소분류 그룹을 선택 해 주세요" , allowClear: true
			 * }).val('empty').change();
			 */
			var parseHtml =jQuery.parseHTML(jRes.resData.htmlString);
			// $(".main_form").append(parseHtml)


  			for(var i = 0; i < parseHtml.length;i++){
				var id= parseHtml[i].id;
				var obj = $(parseHtml[i]);
				var fieldset = obj.attr("fieldset");



				if (obj.is("input") || obj.is("select")){
					if (fieldset == undefined)
						fieldset = "fEtc";

					$fieldset = $("#"+fieldset);
					var fieldsetName = lang.fn.get('views.search.form.fieldset.'+fieldset);

					if($fieldset.length == 0){
						$fieldset = $('<fieldset class="search_fieldset" id="'+fieldset+'"><legend>'+fieldsetName+'</legend></fieldset>')
						$(".main_form").append($fieldset)
					}
					$fieldset.append(parseHtml[i]);
				}else{
					$(".main_form").append(parseHtml[i])
				}
			}

			formFunction();

			loadSelectValue($(".main_form").find('select'));

			var obj = targetObj;
			$(window).resize();
			ui_controller();
		}
	})

	// 셀렉트 박스값 로드 해주는 함수
	function loadSelectValue(selectObj){
		selectObj.each(function( i ) {
			var loadUrl  = $(this).attr("loadUrl")
			searchSelectOptionLoad(this,loadUrl);
		});
	}

	// 상담유형 대중소 이벤트
	if($("#counselResultBgcode").length > 0){

		$("#counselResultBgcode").change(function(){
			if($("#counselResultMgcode").length > 0){
				var loadUrl = "/selectOption.do?comboType=common&comboType2="+$(this).val()+"&ALL=all"
				var itemName = $("#counselResultMgcode").children().first().text();
				$("#counselResultMgcode").children().remove();
				$("#counselResultMgcode").append("<option selected value=''>"+itemName+"</option>");
				searchSelectOptionLoad($("#counselResultMgcode"),loadUrl);
				if ($("#counselResultMgcode option:eq(2)").length != 0)
					$("#counselResultMgcode").prop("selectedIndex",2);
				else
					$("#counselResultMgcode").prop("selectedIndex",1);
				setTimeout(function(){
					$("#counselResultMgcode").change();
    			}, 1);
			}
			
			
			if($("#counselResultSgcode").length > 0){

				itemName = $("#counselResultSgcode").children().first().text();
				$("#counselResultSgcode").children().remove();
				$("#counselResultSgcode").append("<option selected value=''>"+itemName+"</option><option value=''>"+lang.combo.ALL+"</option>");
			}
		});

		$("#counselResultMgcode").change(function(){
			if($("#counselResultSgcode").length > 0){
				var loadUrl = "/selectOption.do?comboType=common&comboType2="+$(this).val()+"&ALL=all"
				var itemName = $("#counselResultSgcode").children().first().text();
				$("#counselResultSgcode").children().remove();
				$("#counselResultSgcode").append("<option selected value=''>"+itemName+"</option>");
				searchSelectOptionLoad($("#counselResultSgcode"),loadUrl);

				if ($("#counselResultSgcode option:eq(2)").length != 0)
					$("#counselResultSgcode").prop("selectedIndex",2);
				else
					$("#counselResultSgcode").prop("selectedIndex",1);

			}
		});
	}

	var $bgCodeObj = $(".main_form").find("#bgCode");
	var $mgCodeObj = $(".main_form").find("#mgCode");
	var $sgCodeObj = $(".main_form").find("#sgCode");

	if ($mgCodeObj.length > 0) {
		var itemName = $mgCodeObj.children().first().text();
		$mgCodeObj.children().remove();
		$mgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
	}
	if ($sgCodeObj.length > 0) {
		var itemName = $sgCodeObj.children().first().text();
		$sgCodeObj.children().remove();
		$sgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
	}
		

	// 대분류 변화시 중분류 로드

	if ($bgCodeObj.length > 0){
		$bgCodeObj.change(function() {

			if ($mgCodeObj.length > 0){
				var itemName = $mgCodeObj.children().first().text();
				if ($(this).val()!=""){
					selectOrganizationLoad($mgCodeObj, "mgCode", $(this).val(),undefined,undefined,undefined,"ALL")
					$mgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
				}else{
					$mgCodeObj.children().remove();
					$mgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
				}
			}

			if ($sgCodeObj.length > 0){
				itemName = $sgCodeObj.children().first().text();
				$sgCodeObj.children().remove();
				$sgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
			}
		});
	}


	if ($mgCodeObj.length > 0){
		// 중분류 변화시 소분류 로드
		$mgCodeObj.change(function() {
			if ($sgCodeObj.length > 0){
				var itemName = $sgCodeObj.children().first().text();
				if ($(this).val()!=""){
					selectOrganizationLoad($sgCodeObj, "sgCode", $bgCodeObj.val(), $(this).val(),undefined,undefined,"ALL")
					$sgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
				}else{
					$sgCodeObj.children().remove();
					$sgCodeObj.prepend("<option selected value=''>"+itemName+"</option>");
				}
			}
		});
	}

// var data = [
// {id: 'KOR',
// text: '한국'},
// {id: 'ENG',
// text: '영국'},
// {id: 'US',
// text: '미국'}
// ]
// $("#testSelect2").attr("multiple","multiple").select2({
// data: data
// , allowClear: true
// , dropdownAutoWidth : true
// });


}

// 트레이스 팝업
function openTracePop(recFileData) {

	var strData = "?mode=trace&file="+recFileData.listenUrl+"&callKeyAp="+recFileData.callKeyAp;
	
	traceList.clearAndLoad(contextPath+"/search_list.xml" + encodeURI(strData),function(){
		ui_controller();
	});

	layer_popup('#tracePopup')
}

// 부분저장 팝업
function openRecSectionPop(callid1) {

	var strData = "?callid1="+callid1+"&header=false";
	
	recSectionList.clearAndLoad(contextPath+"/recSection_list.xml" + encodeURI(strData),function(){
		ui_controller();
	});

	layer_popup('#RecSectionPopup')
}

// 연광콜
function openReferencePop(buffer3,type,date) {

	var strData = "?buffer3="+buffer3+"&type="+type+"&date="+date+"&header=false";
	
	recSectionList.clearAndLoad(contextPath+"/recSection_list.xml" + encodeURI(strData),function(){
		ui_controller();
	});

	layer_popup('#RecSectionPopup')
}

/**
 * 셀렉트 옵션 불러오기
 * 
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : comboType2 => 콤보 기본값 추가 해주기 (default로 값 셋팅 해주면 됨.)
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 * 
 */
function searchSelectOptionLoad(objSelect, loadUrl){

	$.ajax({
		url:contextPath+loadUrl,
		data:{},
		type:"GET",
		dataType:"json",
		async: false,
		cache: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}

// 폼 돌면서 시리얼라이즈 해주기
function formToSerialize(requestOrderBy){

	$(".inputFilter").trigger('blur');
	$(".inputFilter").trigger('keyup');

	var resultValue = "";

	// 통화내용(select2 사용)인 경우도 변수에 따로 넣어야 됨
	$(".search_fieldset").children().not("legend, #recKeyword, #recCategory").each(function (i){

		var id = this.id;
		var value = ($(this).val()) ? $(this).val().trim() : "";
		var $obj = $(this);

		if(id!='custName')
			this.value=sqlFilter(value);

		if (value == "" || value == undefined || value == null)
			return;

		// 조회 시간 및 통화시간 선택 셀렉트 박스의 경우
		// 입력창에 데이터가 없을 경우에만 유효한 데이터로 인지 하여 넘긴다.
		if ($(this).hasClass("sel_time")){

			var oriId = id.substr(0,id.length-1)

			if(!$("#"+oriId).val())
				id = oriId
		}
		if(id=='custName'){
			value+="";
			resultValue += (resultValue.length > 0?"&":"?") + id+"="+value.replace(/\+/g,"%2B").replace(/\&/g,"%26");
		}
		else
			resultValue += (resultValue.length > 0?"&":"?") + id+"="+value;

	});
	if (requestOrderBy != undefined && requestOrderBy.split("|").length == 2){
		var orderBy = requestOrderBy.split("|")[0]
		var direction = requestOrderBy.split("|")[1]
		if (orderBy !="r_rec_date" && orderBy != "r_rec_time")
			resultValue += (resultValue.length > 0?"&":"?") + "orderBy="+orderBy + "&direction="+direction;

	}

	var buffer12 = "&buffer12="
	var buffer13 = "&buffer13="
		
		
	// if($("#fEtc").find("#buffer12").length > 0 &&
	// $("#fEtc").find("#buffer13").length){
	if($('#fEtc .dhxcombo_input').length==2){
		if(buffer12Combo.getChecked().length>0){
			for(j=0; j<buffer12Combo.getChecked().length; j++){
				if(j == (buffer12Combo.getChecked().length-1)){
					buffer12 += buffer12Combo.getChecked()[j];
				}else{
					buffer12 += buffer12Combo.getChecked()[j]+",";
				}
			}
		}


		if(buffer13Combo.getChecked().length>0){
			for(j=0; j<buffer13Combo.getChecked().length; j++){
				if(j == (buffer13Combo.getChecked().length-1)){
					buffer13 += buffer13Combo.getChecked()[j];
				}else{
					buffer13 += buffer13Combo.getChecked()[j]+",";
				}
			}
		}
		
		resultValue += "&dateOrderBy="+ dateColumnState+"&timeOrderBy="+ timeColumnState + buffer12 + buffer13 + "&Eval_Thema="+evalThema + "&recsee_mobile="+recsee_mobile + "&listenColor="+listenColor + "&custEncryptDate="+custEncryptDate;
	}else{
		resultValue += "&dateOrderBy="+ dateColumnState+"&timeOrderBy="+ timeColumnState + "&Eval_Thema="+evalThema + "&recsee_mobile="+recsee_mobile + "&listenColor="+listenColor + "&custEncryptDate="+custEncryptDate;
	}
	
	// 카테고리, 키워드 변수보내기
	var selectArr = [];
	var data = $('#recCategory').select2('data');
	if(data!=undefined && data.length > 0 ){
		for(var i = 0 ; i < data.length ; i++){
			selectArr.push(data[i].id);
		}
		resultValue += "&recCategory="+ selectArr.toString();
	}
	selectArr = [];
	data = $('#recKeyword').select2('data');
	if(data!=undefined && data.length > 0 ){
		for(var i = 0 ; i < data.length ; i++){
			selectArr.push(data[i].id);
		}
		resultValue += "&recKeyword="+ selectArr.toString();
	}
	
	return encodeURI(resultValue);
}

function addPlayList(recFileData){
	rc = top.nowRc;
	var CheckMode=top.playerFrame.$("#playerObj .btn_list_del").css("display");
	if(rc.type.dual){
		alert(lang.views.search.alert.msg2/*
											 * "화자분리 플레이어 상태에서는 파일리스트 추가가 불가능
											 * 합니다."
											 */);
		return;
	}else if(CheckMode!="none"){
		alert(lang.views.search.alert.msg3/*
											 * "플레이리스트 편집모드에서는 파일리스트 추가가 불가능
											 * 합니다."
											 */);
		return;
	}
	rc.addPlayList(recFileData);
}

function deleteItem(){
	$('.delete-item').click(function(){
		$(this).parents(".search_fieldset").remove();
	})
}
function searchDragStart(){
	var $start;
	$(".search_fieldset").css({"cursor":"move"});
	$(".search_fieldset").append("<div class='ui-icon ui-icon-close delete-item'></div>");
	$('.main_form').sortable({
		containment : "parent",
		items : "> fieldset",
		opacity : "0.7"
	});
	deleteItem();
	$('.main_form').sortable('enable');
}


function searchDragDestroy(){
	$(".search_fieldset").css({"cursor":""});
	$(".search_fieldset").find(".ui-icon").remove();
	$('.main_form').sortable('disable');
}

function mvSearchFieldset(){
	$("#listSettingBtn").click(function(){
		layer_popup('#searchListPopup');
	});
}

function searchLoad() {

	// 하단 고정 플레이어
	rc = top.nowRc;

	/* 폼 */
	loadForm($(".main_form"))

	/* 조회및청취 그리드 */
	// gridSearch = createGrid("gridSearch", "pagingSearch",
	// "search_list","?header=true","bla",100,[],[]);

	gridSearch = searchGridLoad();

	/* 트레이스 그리드 */
	traceList = createTraceGrid("traceList", "", "search_list","?header=true&mode=trace","",20,[],[]);

	/* 부분저장 그리드 */
	recSectionList = createTraceGrid("RecSectionList", "recSection_list", "recSection_list","?header=true","",20,[],[]);	
	
	recMemoList = createRecMemoGrid("recMemoList","recMemoAddButton","recMemo_list","?header=true","",20,[],[]);
	
	logListenList = createRecMemoGrid("logListenList","","logListen_list","?header=true","",20,[],[]);
	
	gridFunction();

	mvSearchFieldset();

	ui_controller();
	
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})
	$("#sTimeS, #eTimeS").change(function(){
		fromTo($("#sTimeS"),$("#eTimeS"),this)
	})
	$("#sTtimeS, #eTtimeS").change(function(){fromTo($("#sTtimeS"),$("#eTtimeS"),this)})
	$("#sTime, #eTime").keyup(function(){if($("#sTime").val().replace(/[:-]/g,'').length==6&&$("#eTime").val().replace(/[:-]/g,'').length==6) fromTo($("#sTime"),$("#eTime"),this)})
	$("#sTtime, #eTtime").keyup(function(){if($("#sTtime").val().replace(/[:-]/g,'').length==6&&$("#eTtime").val().replace(/[:-]/g,'').length==6) fromTo($("#sTtime"),$("#eTtime"),this)})
	$("#sRtime, #eRtime").change(function(){fromTo($("#sRtime"),$("#eRtime"),this)})

	$("#sTimeSConnect, #eTimeSConnect").change(function(){fromTo($("#sTimeSConnect"),$("#eTimeSConnect"),this)})
	$("#sTtimeSConnect, #eTtimeSConnect").change(function(){fromTo($("#sTtimeSConnect"),$("#eTtimeSConnect"),this)})
	$("#sTimeConnect, #eTimeConnect").keyup(function(){if($("#sTimeConnect").val().replace(/[:-]/g,'').length==6&&$("#eTimeConnect").val().replace(/[:-]/g,'').length==6) fromTo($("#sTimeConnect"),$("#eTimeConnect"),this)})
	$("#sTtimeConnect, #eTtimeConnect").keyup(function(){if($("#sTtimeConnect").val().replace(/[:-]/g,'').length==6&&$("#eTtimeConnect").val().replace(/[:-]/g,'').length==6) fromTo($("#sTtimeConnect"),$("#eTtimeConnect"),this)})
}

// 메모 그리드 팝업 처리
function tagPopupGrid(tagInfo){
	if(memoReadYn != "Y"){
		alert(lang.views.search.alert.msg27/* "메모 접근권한이 없습니다." */);
		return false;
	}
	
	// $("#timeTag").hide();
	// $("#timeTagAdd").hide();
	// telnoUse

	var strData = "?date="+tagInfo.recDate+"&time="+tagInfo.recTime + "&ext="+tagInfo.extNum;
	recMemoList.clearAndLoad(contextPath+"/recMemo_list.xml" + encodeURI(strData),function(){

		 var A = new dhtmlXToolbarObject({
				parent: "recMemoAddButton",
				icons_path : recseeResourcePath + "/images/project/dhxgrid_web/"
			});

		A.setAlign("right");
		A.addButton("addMemo",1, lang.views.search.alert.msg28/* "메모 작성" */, "icon_btn_add_gray.png", "icon_download.png");
		A.attachEvent("onClick", function(name){
			switch(name) {
				case "addMemo":
					tag({
							"extNum" : tagInfo.extNum
						,	"save"		:	"insert"
						,	"userId"		:  userId
						,	"recDate"	:	tagInfo.recDate
						,	"recTime"	:	tagInfo.recTime
						,	"tag"			: ""
					})

				break;
			}
		});

		ui_controller();
	});
	layer_popup('#recMemoPopup')
}

// 청취이력 그리드 팝업 처리
function logPopupGrid(logInfo){

	var strData = "?date="+logInfo.recDate+"&time="+logInfo.recTime + "&ext="+logInfo.extNum;
	logListenList.clearAndLoad(contextPath+"/logListen_list.xml" + encodeURI(strData),function(){

		ui_controller();
	});

	layer_popup('#logListenPopup')
}

function listenLog(recFileData,justPath){
	var url = recFileData.listenUrl.replace(':8088',':28881/listen?url=');

	$.ajax({
		url:contextPath+"/listenLog.do",
		data:recFileData,
		type:"POST",
		dataType:"json",
		async: true,
		cache: false,
		success:function(jRes){
			if(gridSearch.getColIndexById("r_log_listen")!=undefined){
				gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_log_listen")).setValue(
						"<button class='ui_btn_white ui_sub_btn_flat icon_btn_historySearch_white' " +
						"onclick='logPopupGrid(" +
						"{&quot;recTime&quot;:&quot;"+gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_rec_time")).getValue()
						+"&quot;,&quot;recDate&quot;:&quot;"+gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_rec_date")).getValue()
						+"&quot;,&quot;extNum&quot;:&quot;"+gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_ext_num")).getValue()
						+"&quot;})' " 
						+"style='background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease 0s;'>"
						+"</button>")
			}
			if(listenColor=="Y"){
				gridSearch.setRowTextStyle(gridSearch.getSelectedId(),"color:#000;");
			}
		}
	});

	return url;
}

function tag(dataStr){
	if(memoReadYn != "Y"){
		alert(lang.views.search.alert.msg27/* "메모 접근권한이 없습니다." */);
		return false;
	}
	top.tagPopup(dataStr,recMemoList,memoWriteYn);
}

function play(gridObj, id){
		
	var listenUrl 		= (data.listenUrl==undefined ? 		"" : data.listenUrl);
	var recDate 		= (data.recDate==undefined ? 	"" : data.recDate);
	var recTime			= (data.recTime==undefined ? 	"" : data.recTime);
	var recExt			= (data.extNum==undefined ? 	"" : data.extNum);
	var recCustPhone	= (data.custPhone1==undefined ? 	"" : data.custPhone1);
	var recUserName 	= (data.userName==undefined ? 	"" : data.userName);
	var recvFileName    = (data.vFileName==undefined ? 	"" : data.vFileName);
	var recMemo 		= "-"
	var recCustName 	= (data.custName==undefined ? 	"" : data.custName);
	var vSysCode	    = "";
	var sysCode 	  = ""; 
	var recStartType  = "";
	var recVolume       = "";
	// 녹취 파일 정보
    
	var recFileData = {
			"listenUrl"		: listenUrl
		,	"recDate"		: recDate	  			// 녹취일
		,	"recTime"		: recTime				// 녹취시간
		,	"recExt"		: recExt				// 내선버노
		,	"recCustPhone"	: recCustPhone			// 고객 저나버노
		,	"recUserName"	: recUserName			// 상담원 명
		,	"recvFileName" 	: recvFileName			// 파일 명
		,	"recMemo"		: recMemo				// 메모
		,	"rowId"			: id					// row 번호
		, 	"recCustName"   : recCustName  			// 고객명
		,	"recVolume"		: recVolume				// 파일 볼륨
		,	"vSysCode"		: vSysCode				// 시스템 코드
		,	"sysCode"		: sysCode				// 시스템코드
		,	"recStartType"	: recStartType			// 콜 타입
	}
//    var recFileData = {"listenUrl":"HTTP://waipap01d.woorifg.com:28881/listen?url=/usr/local/tomcat9/REC/20220415/18/165001594824129711875_merge_all_enc.mp3",
//    		"recDate":"2022-04-15",
//    		"recTime":"18:45:48",
//    		"recExt":"29711875",
//    		"recCustPhone":"2000478955",
//    		"recUserName":"김은진",
//    		"recvFileName":"165001594824129711875_merge_all_enc.mp3",
//    		"recMemo":"-",
//    		"rowId":"2022041518454829711875",
//    		"recCustName":"김명석",
//    		"recVolume":"","vSysCode":"","sysCode":"","recStartType":""}
    

	// 녹취 주소 복호화
//	listenLog(recFileData)
	rc = top.nowRc;
	rc.recFileData = recFileData;
	if(rc.type.dual){
		var delYn = false;
		/*
		 * | RX TX 파일은 열람서버로 올리지 않기 때문에 듀얼일 경우 ajax 로 buffer 1 체크 하여 | 파일 삭제 유무
		 * 판단후 rx tx값 넘겨준다. | 일단은 기존 녹취 파일 있는 곳 바로보면 됨 r_v_rec_ip 와
		 * r_v_rec_fullpath 로 listenUrl 값 다시 넘겨줌
		 */
		$.ajax({
			url:contextPath+"/dualModeListen.do?recseePlayer=Y",
			data:recFileData,
			type:"POST",
			dataType:"json",
			async: false,
			cache: false,
			success:function(jRes){
				if(jRes.success=="Y"){
					delYn = jRes.resData.delFileYn;
					listenUrl = jRes.resData.listenUrl.replace(':8088',':28881/listen?url=');
					if(jRes.resData.IVR == "P"){
						IVR = true;
						return false;
					}else{
						IVR=false;
					}
										
					$.ajax({
                        url:listenUrl.split("/listen")[0]+"/ping",
                        data:{},
                        type:"GET",
                        dataType:"json",
                        timeout:2000,
                        async: false,
                        success:function(jRes){
                        	var url = listenUrl;
                			var data = recFileData;
                			$(".player_tx .procTime").addClass("dual");
                	       	$(".player_rx .procTime").addClass("dual");
                	       	top.dual_rc.recFileData = data;
                	       	top.dual_rc.setFile("audio", url , undefined , true, "rsfft", recVolume);
                        },
                        error:function(request, satus, error){
                            top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.search.alert.msg26/*
																																					 * "현재
																																					 * 재생
																																					 * 중인
																																					 * 파일 : "
																																					 */);
                             top.$('#playerFrame').contents().find(".btn_init").click();
                               return false;
                        }
                    });
					
					
				}else{
					top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.search.alert.msg26/*
																																			 * "현재
																																			 * 재생
																																			 * 중인
																																			 * 파일 : "
																																			 */);
	                   // lang.views.search.alert.msg47
	                    top.$('#playerFrame').contents().find(".btn_init").click(); 
	                   // alert(lang.views.search.alert.msg26/*"요청에 실패
						// 하였습니다."*/);
				}
			}
		})
	}else{
		$.ajax({
			url:contextPath+"/getListenUrl.do",
			data:recFileData,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success=="Y"){
					// var ip=jRes.resData.ListenUrl[0].vRecIp;
				// var file=jRes.resData.ListenUrl[0].vRecFullpath;
					listenUrl=jRes.resData.ListenUrl.replace(':8088',':28881/listen?url=');
					// listenUrl='http://'+ip+':28881/listen?url='+file;
                    waveType=jRes.resData.WaveType;
                    $.ajax({
                        url:listenUrl.split("/listen")[0]+"/ping",
                        data:{},
                        type:"GET",
                        dataType:"json",
                        timeout:2000,
                        async: false,
                        success:function(jRes){
                            top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.player.html.text2/*
																																					 * "현재
																																					 * 재생
																																					 * 중인
																																					 * 파일 : "
																																					 */);
                            
                            rc.setFile("audio", encodeURI(listenUrl), undefined, true, waveType/* "rsfft" */,recVolume);
                            rc.listenUrl =encodeURI(listenUrl)
                        },
                        error:function(request, satus, error){
                            top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.search.alert.msg26/*
																																					 * "요청에
																																					 * 실패
																																					 * 하였습니다."
																																					 */);
                             top.$('#playerFrame').contents().find(".btn_init").click();
                            
                               // alert(lang.views.search.alert.msg26/*"요청에 실패
								// 하였습니다."*/);
                               return false;
                        }

                    });
				}else {
                    top.$('#playerFrame').contents().find(".play_list").find(".play_info").find("span").html(lang.views.search.alert.msg26/*
																																			 * "요청에
																																			 * 실패
																																			 * 하였습니다."
																																			 */);
                   // lang.views.search.alert.msg47
                    top.$('#playerFrame').contents().find(".btn_init").click(); 
                   // alert(lang.views.search.alert.msg26/*"요청에 실패 하였습니다."*/);
				}
			}
		});
    }
    

   
}

function checkSoundDevice(gridObj, id){
		try {
			var taudio = new Audio();
			var src = contextPath+"/getRecFileTest.do?fileName=beef.mp3"
			taudio.autoplay = true;
			taudio.volume = 0;
			taudio.src = src;
	    	taudio.pause();
			taudio.load();
			taudio.play();
	
			taudio.addEventListener('error',function(event) {
				taudio.pause();
				alert(lang.views.search.alert.msg32/*
													 * "컴퓨터에 음성파일을 재생 가능한 장치가
													 * 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결
													 * 후 청취 시도를 해주세요."
													 */)
	        });
	
			taudio.addEventListener('loadeddata',function(){
				try {
					taudio.currentTime=0.1
					taudio.pause();
					play(gridObj, id);
					return true;
				} catch(e){
					taudio.pause();
					alert(lang.views.search.alert.msg32/*
														 * "컴퓨터에 음성파일을 재생 가능한 장치가
														 * 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저
														 * 연결 후 청취 시도를 해주세요."
														 */)
				}
			});
		}catch(e){
			taudio.pause();
			alert(lang.views.search.alert.msg32/*
												 * "컴퓨터에 음성파일을 재생 가능한 장치가
												 * 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취
												 * 시도를 해주세요."
												 */)
		}
}

function checkApprove(id,type){

	var approveValue = "none";

	var dataStr = {
			"fileName"		: (gridSearch.getColIndexById("r_v_filename")==undefined?	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_v_filename")).getValue())
		,	"recExt"		: (gridSearch.getColIndexById("r_ext_num")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_ext_num")).getValue())
		,	"recDate" 		: (gridSearch.getColIndexById("r_rec_date")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_date")).getValue())
		,	"recTime"		: (gridSearch.getColIndexById("r_rec_time")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_time")).getValue())
		,	"approveType"	: type
		,	"type"			: "check"
	}

	$.ajax({
		url:contextPath+"/approve_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		cache: false,
		success:function(jRes){
			if(jRes.success=="Y"){
				approveValue = jRes.resData.result;
			}else {
				alert(lang.views.search.alert.msg26/* "요청에 실패 하였습니다." */);
			}
		}
	});

	return approveValue;
}

function multiAllDownloadFunc(objGrid){

	if(objGrid.getRowsNum() != 0 ) {
		var checked = objGrid.getAllRowIds().split(",");
		var fileName = new Array();

		top.$("#closeEncYn").unbind("click");
		top.$("#downloadEncYn").unbind("click");
		
		var format = 'mp3';
		if(top.$('#downloadType').val()=='gsm'||top.$('#downloadType').val()=='g723')
			format=top.$('#downloadType').val()
			
		if(encYn == "Y" && encNCheck == false &&format=='mp3'){

			top.layer_popup("#recEncPopup");

			top.$(".enc_ui_hide").hide();
			top.ui_controller();

			top.$("#closeEncYn").click(function(event){
				top.layer_popup_close("#recEncPopup");
			})

			top.$("#downloadEncYn").click(function(event){
				if(top.$("#approveUserGroup").val() == "encY"){
					encNCheck = true;
					encYnReal = true;
					multiAllDownloadFunc(objGrid);
				}else{
					encNCheck = true;
					encYnReal = false;
					multiAllDownloadFunc(objGrid);
				}
			})
			return false;
		}

		if(format!='mp3')
			encYnReal=false;
		
		progress.on();
		var mobileUFlag = true;
		for( var i = 0 ; i < checked.length ;i++ ) {
			var recVolume = (objGrid.getColIndexById("r_rec_volume_value")==undefined?   "1" : objGrid.cells(checked[i],objGrid.getColIndexById("r_rec_volume_value")).getValue());
			var checkItem = "";
			if(recsee_mobile == "Y"){
				var r_bufferIndex3=objGrid.getColIndexById("r_buffer3");
				if(undefined!=r_bufferIndex3){
					var uploadCheck = objGrid.cells(checked[i],r_bufferIndex3).getValue(); // 파일
																							// 업로드
																							// 체크
					if(uploadCheck == "Y"){
						checkItem = objGrid.cells(checked[i],objGrid.getColIndexById('rec_url')).getValue().split("listen?").join("listen?encYn="+encYnReal+"&");
					}
				}
			}else{
				checkItem = objGrid.cells(checked[i],objGrid.getColIndexById('rec_url')).getValue().replace(':8088',':28881/listen?url=').split("listen?").join("listen?encYn="+encYnReal+"&recVolume="+recVolume+"&");
			}
			
			if(checkItem != ""){
				if("http"==HTTP)
					fileName.push(checkItem);
				else
					fileName.push(checkItem.replace("HTTP://","HTTPS://"));
			}else{
				alert(lang.views.search.grid.alert.mobileNotUpload/*
																	 * "선택하신 파일은
																	 * 업로드 되지 않은
																	 * 파일입니다."
																	 */);
				mobileUFlag = false;
				break;
			}
		}
		
		if(mobileUFlag){
		
			$("#fileName").val(fileName.join("|"));
			$("#format").val(format);
			
			var nowTime = new Date();

			var firstUrl = objGrid.cells(checked[0],objGrid.getColIndexById('rec_url')).getValue().replace(':8088',':28881/listen?url=');
			var requestUrl = parseUri(firstUrl).authority;
			var url="";
			if("http"==HTTP)
				url = HTTP+"://"+requestUrl+"/zipDown?"+nowTime.getTime();
			else 
				url = HTTP+"://"+requestUrl.split(":")[0]+":"+rc.requestPort+"/zipDown?"+nowTime.getTime();
			$("#download").attr("action", url);
			// $("#download").attr("action", contextPath +
			// "/zipDownload?"+nowTime.getTime());

			// 다운로드 로그 남기기 위해 추가
			$.ajax({
				url:contextPath + "/downloadLog.do",
				data:{
						recvFileName : fileName.toString()
					,	recvFileEnc	:	encYnReal
					,	reasonStr : reasonStr
				},
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
					}
				}
			});

			$("#download").submit();
			encNCheck = false;
			encYnReal = true;
		}		
		top.layer_popup_close("#recEncPopup");
		progress.off();
	} else {
		alert(lang.views.search.alert.noDownTarget)/* 다운로드할 대상이 없습니다. */
	}
}

function multiDownloadFunc(objGrid){
	
	if(!confirm('선택하신 녹취이력을 다운로드 하시겠습니까?')){
		return false;
	}
	
	var chCol = -1;
	for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
		if(objGrid.getColType(i) == "ch") {
			chCol = i;
			break;
		}
	}

	if(chCol != -1 && objGrid.getCheckedRows(chCol) != "" ) {
		var checked = objGrid.getCheckedRows(chCol).split(",");
		var fileName = new Array();
		var format = 'mp3';
		
		progress.on();
		for( var i = 0 ; i < checked.length ;i++ ) {
			var checkItem = "";
			checkItem = objGrid.cells(checked[i],objGrid.getColIndexById('rec_url')).getValue().replace(':8088',':28881/listen?url=').split("listen?").join("listen?encYn=false&");
			if(checkItem != ""){
				if("http"==HTTP)
					fileName.push(checkItem);
				else
					fileName.push(checkItem.replace("HTTP://","HTTPS://"));
			}else{
				alert(lang.views.search.grid.alert.mobileNotUpload/*
																	 * "선택하신 파일은
																	 * 업로드 되지 않은
																	 * 파일입니다."
																	 */);
				break;
			}
		}
		
		$("#fileName").val(fileName.join("|"));
		$("#format").val(format);
		
		var nowTime = new Date();

		var firstUrl = objGrid.cells(checked[0],objGrid.getColIndexById('rec_url')).getValue().replace(':8088',':28881/listen?url=');
		var requestUrl = parseUri(firstUrl).authority;
		var url="";
		if("http"==HTTP)
			url = HTTP+"://"+requestUrl+"/zipDown?"+nowTime.getTime();
		else 
			url = HTTP+"://"+requestUrl.split(":")[0]+":"+rc.requestPort+"/zipDown?"+nowTime.getTime();
		$("#download").attr("action", url);
		// $("#download").attr("action", contextPath +
		// "/zipDownload?"+nowTime.getTime());

		// 다운로드 로그 남기기 위해 추가
		$.ajax({
			url:contextPath + "/downloadLog.do",
			data:{
					recvFileName : fileName.toString()
				,	recvFileEnc	:	encYnReal
				,	reasonStr : reasonStr
			},
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
				}
			}
		});

		$("#download").submit();
		progress.off();
	} else {
// onMessage("<spring:message code=\"views.search.grid.alert.noChecked\" />");
		alert(lang.views.search.grid.alert.noChecked/* 다운로드할 대상을 선택해주세요. */);
	}
}

// 다운로드 파일 형식 선택
function checkFormat(objGrid){
	
	// 셀렉터 초기화
	top.$('#selectFormat').val('mp3');
	
	top.$("#closeFormatYn").unbind("click");
	top.$("#confirmFormat").unbind("click");

	top.$("#closeFormatYn").click(function(event){
		top.layer_popup_close("#recFormatPopup");
	})
	
	top.$("#confirmFormat").click(function(event){
		top.$('#downloadType').val(top.$('#selectFormat').val());
		
		multiDownloadFunc(objGrid);
		
		top.layer_popup_close("#recFormatPopup");
	});
	
	var chCol = -1;
	for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
		if(objGrid.getColType(i) == "ch") {
			chCol = i;
			break;
		}
	}

	if(chCol != -1 && objGrid.getCheckedRows(chCol) != "" ){
		
		top.layer_popup("#recFormatPopup")
		top.ui_controller();
		
	}else{
		alert(lang.views.search.grid.alert.noChecked/* 다운로드할 대상을 선택해주세요. */);
	}
	
}

// 다운로드 사유 입력
function requestReasonByDown(objGrid){
	
	// 인풋 초기화
	top.$('#downloadReason').val('');
	reasonStr='';
	
	top.$("#closeReason").unbind("click");
	top.$("#confirmReason").unbind("click");

	top.$("#closeReason").click(function(event){
		top.layer_popup_close("#requestReasonPopup");
	})
	
	top.$("#confirmReason").click(function(event){
		
		if(top.$('#downloadReason').val().trim().length<5){
			alert(lang.views.search.grid.alert.moreReason/*
															 * 최소 5글자의 사유를 입력해
															 * 주세요.
															 */);
			
			top.$('#downloadReason').focus();
			
			return false;
		}else{
			reasonStr = top.$('#downloadReason').val();
			
			if(downloadFormat=='Y'){
				checkFormat(objGrid)
			}else{
				multiDownloadFunc(objGrid);
			}
			top.layer_popup_close("#requestReasonPopup");
		}
	});
	
	var chCol = -1;
	for(var i = 0; i < objGrid.getColIndexById('rec_url'); i++) {
		if(objGrid.getColType(i) == "ch") {
			chCol = i;
			break;
		}
	}

	if(chCol != -1 && objGrid.getCheckedRows(chCol) != "" ){
		
		top.layer_popup("#requestReasonPopup")
		top.ui_controller();
		
	}else{
		alert(lang.views.search.grid.alert.noChecked/* 다운로드할 대상을 선택해주세요. */);
	}
	
}

function customContext(){
	// 콘텍스트 메뉴 지정
	gridContextMenu = document.getElementById("context-menus");
	var conValue;

	dhtmlxEvent(gridSearch.hdr,"contextmenu",function(e){
		var btn = (e || event).button;
		if(btn == 2){
			toggleOnOff(1);
			if(e.target.id == "allcheck"){
				conValue= gridSearch.getColIndexById('r_check_box');
			}else if(e.target.id == undefined || e.target.id == ""){
				conValue = gridSearch.getColIndexById(e.target.getElementsByTagName('div')[1].id);
			}else{
				conValue= gridSearch.getColIndexById(e.target.id)
			}

			showMenu(e.clientX, e.clientY,conValue);
		}
	})

	setTimeout(function(){
		// 셀 선택되어 있는부분
		if(gridSearch.globalBox != undefined){
				dhtmlxEvent(gridSearch.globalBox,"contextmenu",function(e){
				var btn = (e || event).button;
				if(btn == 2){
					toggleOnOff(1);
					try{
						if(e.target.id == "allcheck"){
							conValue= gridSearch.getColIndexById('r_check_box');
						}else if(e.target.id == undefined || e.target.id == ""){
							conValue = gridSearch.getColIndexById(e.target.getElementsByTagName('div')[1].id);
						}else{
							conValue= gridSearch.getColIndexById(e.target.id)
						}

						showMenu(e.clientX, e.clientY,conValue);
					}catch(e){
						// 헤드 말고 클릭할 경우 하이드 처리
						toggleOnOff(0);
					}
				}
			})
		}
	},1000);

	dhtmlxEvent(gridSearch.obj.parentNode,"click",function(e){
		toggleOnOff(0);
	})

	$(window).click(function(e){
		if(!$(e.target).is("context-menus")){
			toggleOnOff(0);
		}
	})

	function toggleOnOff(num){
		num === 1 ? gridContextMenu.classList.add("active") : gridContextMenu.classList.remove("active");
	}

	function showMenu(x, y,txt){
		gridContextMenu.style.top = y + "px";
		gridContextMenu.style.left = x + "px";
		gridContextMenu.getElementsByTagName('li')[0].setAttribute('id',conValue);
	}

	listenSimple.attachEvent("onClick",function(id){
		if(gridSearch.getRowById(gridSearch.getSelectedRowId()) != null){
			if(listenYn === "Y"){
				var id=gridSearch.getSelectedRowId();
				var urlsimple;

				var urldate={
						recDate : (gridSearch.getColIndexById("r_rec_date")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_date")).getValue()).replace(/-/gi,''),
						recTime	: (gridSearch.getColIndexById("r_rec_time")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_rec_time")).getValue()).replace(/:/gi,''),
						recExt	: (gridSearch.getColIndexById("r_ext_num")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_ext_num")).getValue())
	        	};
	    		$.ajax({
	    			url:contextPath+"/getListenUrl.do",
	    			data:urldate,
	    			type:"POST",
	    			dataType:"json",
	    			async: false,
	    			success:function(jRes){
	    				if(jRes.success=="Y"){
	    					$('#url').val('');
	    					urlsimple=encodeURI(jRes.resData.ListenUrl.replace(':8088',':28881/listen?url='));

					function openApprovePopup(){
						if(!$("#approveType").hasClass("ui_input_hasinfo"))
							$("#approveType").addClass("ui_input_hasinfo");
						$("#approveType").attr("disabled",true);
						$("#approveType").val("listen");
						$("#approveReason").prop("selectedIndex",1);
						layer_popup("#approvePopup");
					}
				
				
	    					$('#url').val(urlsimple);
	    		    		var myForm = document.popForm;
	    		    		var url =  contextPath + "/listenSimple";
	    		    		window.open("","popForm","resizable=no, toolbar=no,location=no, width=800, height=125,scrollbars=yes ,left=500,top=250'");
	    		    		myForm.action = url;
	    		    		myForm.method = "post";
	    		    		myForm.target="popForm";
	    		    		myForm.submit();


	    					// window.open(contextPath+"/listenSimple?url="+urlsimple,
							// "_blank", "resizable=no, toolbar=no, location=0,
							// width=800, height=120, titlebar=no,
							// scrollbars=yes ,left=500,top=250'");
	    					
	    				}else {
	    					alert(lang.views.search.alert.msg26/*
																 * "요청에 실패
																 * 하였습니다."
																 */);
	    					return false;
	    				}
	    			}
	    		});
			}else{
				var result = checkApprove(gridSearch.getSelectedRowId(),"listen");

				if(result == "prereciptWait"){
					alert(lang.views.search.alert.msg14/* "지점 승인 대기중 입니다." */)
					return false;
				}else if(result == "prereciptReject"){
					if(confirm(lang.views.search.alert.msg15/*
															 * "지점 승인 거부된 요청
															 * 입니다.\n재 요청
															 * 하시겠습니까?"
															 */)){
						openApprovePopup()
						return false;
					}
					return false;
				}else if(result == "reciptWait"){
					alert(lang.views.search.alert.msg16/* "접수 대기중 입니다." */)
					return false;
				}else if(result == "reciptReject"){
					if(confirm(lang.views.search.alert.msg17/*
															 * "접수가 거부된 요청
															 * 입니다.\n재 요청
															 * 하시겠습니까?"
															 */)){
						openApprovePopup()
						return false;
					}
					return false;
				}else if(result == "approveWait"){
					alert(lang.views.search.alert.msg18/* "승인 대기중 입니다." */)
					return false;
				}else if(result == "approveReject"){
					if(confirm(lang.views.search.alert.msg19/*
															 * "승인이 거부된 요청
															 * 입니다.\n재 요청
															 * 하시겠습니까?"
															 */)){
						openApprovePopup()
						return false;
					}
					return false;
				}else if (result == "approveFinish"){
					if(confirm(lang.views.search.alert.msg20/*
															 * "승인 후 허용일이 만료된 요청
															 * 입니다.\n재 요청
															 * 하시겠습니까?"
															 */)){
						openApprovePopup()
						return false;
					}
					return false;
				}else if (result == "none"){
					openApprovePopup()
					return false;
				}else if (result == "approve"){
					checkSoundDevice(this,id)
					return false;
				}

				function openApprovePopup(){
					if(!$("#approveType").hasClass("ui_input_hasinfo"))
						$("#approveType").addClass("ui_input_hasinfo");
					$("#approveType").attr("disabled",true);
					$("#approveType").val("listen");
					$("#approveReason").prop("selectedIndex",1);
					layer_popup("#approvePopup");
				}
			}
			
		}else{
			alert(lang.views.search.alert.msg31/* "선택된 파일이 없습니다." */);
		}
	})
}

function gridSplitAt(s){
	// dhx 그리드 파괴
	gridSearch.destructor();

	// 부모 창에서 그리드가 틀 고정 되어 있는 지 확인 값
	parent.contextValue = parseInt(s)+1;

	// 처음 로드시 페이지 처음으로 가는부분 막음 처리
	gridSearch = searchGridLoad(parseInt(s)+1);

	gridFunction();

	// 그리드 틀고정
	// gridSearch.splitAt(parseInt(s)+1);

	// 바로 클리어앤 로드시 이벤트 처리가 되지 않음
	setTimeout(function(){
		if(gridContextUrl != undefined ){
			if(csvFlag==1){
				gridSearch.clearAll();
				gridSearch.post(gridContextUrl, "csvList="+csvT.join("!e!"), function(){
					gridSearch.changePage(1);
					
				});
			}else{
				gridSearch.clearAndLoad(gridContextUrl,function(){
					if(gridContextUrlCnt != 0){
						gridSearch.changePage(gridContextUrlCnt);
					}
				});
			}
		}
	},1500)
	customContext();
	return false;
}

function gridSplitDisable(){
	// dhx 그리드 파괴
	gridSearch.destructor();

	// 부모 창에서 그리드가 틀 고정 되어 있는 지 확인 값
	parent.contextValue = null;

	// 처음 로드시 페이지 처음으로 가는부분 막음 처리
	gridSearch = searchGridLoad();

	// 바로 클리어앤 로드시 이벤트 처리가 되지 않음
	setTimeout(function(){
		if(gridContextUrl != undefined ){
			if(csvFlag==1){
				gridSearch.clearAll();
				gridSearch.post(gridContextUrl, "csvList="+csvT.join("!e!"), function(){
					gridSearch.changePage(1);
					
				});
			}else{
				gridSearch.clearAndLoad(gridContextUrl,function(){
					if(gridContextUrlCnt != 0){
						gridSearch.changePage(gridContextUrlCnt);
					}
				});
			}
		}
	},1500)
	customContext();
	return false;
}


function playerChangeMenu(x , y , chk){
	
	var playerChangeMenu = document.getElementById("playerChangeMenu");
	if(chk){
		toggleOnOff(1);
		showMenu(x, y);
	}else{
		toggleOnOff(0);
	}
	
	function toggleOnOff(num){
		num === 1 ? playerChangeMenu.classList.add("active") : playerChangeMenu.classList.remove("active");
	}

	function showMenu(x, y){
		playerChangeMenu.style.top = y-50 + "px";		// iframe 플레이어 부분때문에 위로
														// 메뉴를 보여줘야 함
		playerChangeMenu.style.left = x - 70 + "px";
	}
}

function excelDownMenu(x,y,chk){

	// 메뉴 생성
	var excelMenu = document.getElementById('excelMenu');
	if(chk){
		toggleOnOff(1);
		showMenu(x, y);
	}else{
		toggleOnOff(0);
	}
	function toggleOnOff(num){
		num === 1 ? excelMenu.classList.add("active") : excelMenu.classList.remove("active");
	}

	function showMenu(x, y){
		excelMenu.style.top = y-50 + "px";		// iframe 플레이어 부분때문에 위로 메뉴를 보여줘야
												// 함
		excelMenu.style.left = x + "px";
	}

}

function fileDownMenu(x,y,chk){
	
	// 메뉴 생성
	var downloadMenu = document.getElementById('downloadMenu');
	if(chk){
		toggleOnOff(1);
		showMenu(x, y);
	}else{
		toggleOnOff(0);
	}
	function toggleOnOff(num){
		num === 1 ? downloadMenu.classList.add("active") : downloadMenu.classList.remove("active");
	}
	
	function showMenu(x, y){
		downloadMenu.style.top = y-50 + "px";		// iframe 플레이어 부분때문에 위로 메뉴를
													// 보여줘야 함
		downloadMenu.style.left = x + "px";
	}
	
}

function deleteMenu(x,y,chk){
	
	// 메뉴 생성
	var deleteMenu = document.getElementById('deleteMenu');
	if(chk){
		toggleOnOff(1);
		showMenu(x, y);
	}else{
		toggleOnOff(0);
	}
	function toggleOnOff(num){
		num === 1 ? deleteMenu.classList.add("active") : deleteMenu.classList.remove("active");
	}
	
	function showMenu(x, y){
		deleteMenu.style.top = y-50 + "px";		// iframe 플레이어 부분때문에 위로 메뉴를 보여줘야
												// 함
		deleteMenu.style.left = x + "px";
	}
	
}

/*
 * | @author DAVID | @type funtcion | @param void | @return void; | @description
 * 조회 및 청취 선택적 엑셀 다운로드 받기 위함. | dhtmlx 엑셀 선택 로우를 구하여 조회한 결과내의 |
 * recdate,rectime,recext를 넘겨 유니크 한 값으로 쿼리를 다시 날려엑셀 생성
 */

function selectExcelDown(){
	
	var chCol = -1;
	for(var i = 0; i < gridSearch.getColIndexById('rec_url'); i++) {
		if(gridSearch.getColType(i) == "ch") {
			chCol = i;
			break;
		}
	}

	if(chCol == -1 || gridSearch.getCheckedRows(chCol) == "" ) {
		alert(lang.views.search.alert.msg4/* 선택된 녹취 파일이 없습니다. */);
		return false;
	}
	
	 progress.on();

	 var gridArr = gridSearch.getCheckedRows(0).split(",");

	 var recDateArr = [];
	 var recTimeArr = [];
	 var extArr = [];

	 for (var i = 0; i < gridArr.length; i++) {
		 recDateArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,''))
		 recTimeArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,''))
		 extArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_ext_num")).getValue())
	 }

	 var grid_link = contextPath+'/searchExcel.do' + formToSerialize() + "&recsee_mobile=" + recsee_mobile + "&fileName=searchList&type=select";

	 	$("#recDateArr").val(recDateArr);
	 	$("#recTimeArr").val(recTimeArr);
	 	$("#extArr").val(extArr);

	 	$("#downloadExcel")[0].action = grid_link;
	 	$("#downloadExcel")[0].submit();
		// $("#downloadFrame").attr("src", grid_link);

		if (window.dhx4.isIE) {
			$(window).blur(function(){
				progress.off()
			});
		} else {
			$("#downloadFrame").ready(function(){
				progress.off()
			});
		}

		gridArr = null;
		recDateArr = null;
		recTimeArr = null;
		extArr = null;
}

function allExcelDown(){
		if(csvFlag==1){
			if(gridSearch.getRowsNum() == 0 ) {
				alert(lang.views.search.alert.msg4/* 선택된 녹취 파일이 없습니다. */);
				return false;
			}
			
			 progress.on();

			 var gridArr = gridSearch.getAllRowIds().split(",");

			 var recDateArr = [];
			 var recTimeArr = [];
			 var extArr = [];

			 for (var i = 0; i < gridArr.length; i++) {
				 recDateArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,''))
				 recTimeArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,''))
				 extArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_ext_num")).getValue())
			 }

			 var grid_link = contextPath+'/searchExcel.do' + formToSerialize() + "&recsee_mobile=" + recsee_mobile + "&fileName=searchList&type=select";

			 	$("#recDateArr").val(recDateArr);
			 	$("#recTimeArr").val(recTimeArr);
			 	$("#extArr").val(extArr);

			 	$("#downloadExcel")[0].action = grid_link;
			 	$("#downloadExcel")[0].submit();
				// $("#downloadFrame").attr("src", grid_link);

				if (window.dhx4.isIE) {
					$(window).blur(function(){
						progress.off()
					});
				} else {
					$("#downloadFrame").ready(function(){
						progress.off()
					});
				}

				gridArr = null;
				recDateArr = null;
				recTimeArr = null;
				extArr = null;
		}else{
			progress.on();
			
			var grid_link = contextPath+'/searchExcel.do' + formToSerialize()+ "&fileName=searchList&type=all";
			
			$("#downloadFrame").attr("src", grid_link);
			
			if (window.dhx4.isIE) {
				$(window).blur(function(){
					progress.off()
				});
			} else {
				$("#downloadFrame").ready(function(){
					progress.off()
				});
			}
		}
}

function selectFileDown(){
	if(downloadYn == "Y" && downloadFormat=='Y'){
		if(requestReason=="Y")
			requestReasonByDown(gridSearch)
		else{
			checkFormat(gridSearch)
		}
	}else if(downloadYn == "Y" && downloadFormat!='Y'){
		if(requestReason=="Y")
			requestReasonByDown(gridSearch)
		else
			multiDownloadFunc(gridSearch);
	}
}

function allFileDown(){
	if(csvFlag==1){
		multiAllDownloadFunc(gridSearch);
	}else{
		if(gridSearch.getRowsNum() == 0){
			alert(lang.views.search.alert.noDownTarget)/* 다운로드할 대상이 없습니다. */
		}else{
			var query = formToSerialize();
			progress.on();
			$.ajax({
				url:contextPath+"/fulldownTime.do"+query,
				type:"get",
				dataType:"json",
				async: true,
				cache: false,
				success:function(jRes){
					if(jRes.success == "Y"){
						var totalTime = jRes.resData.totalTime;
						var totalRecTime;
						if(totalTime>3600){
							totalRecTime = Math.floor(totalTime/3600)+lang.views.search.alert.hours/* "시간 " */+Math.floor((totalTime%3600)/60)+lang.views.search.alert.minutes/* "분 " */+Math.floor(((totalTime%3600)%60))+lang.views.search.alert.second/* "초" */;
						}else if(totalTime>60){
							totalRecTime = Math.floor(totalTime/60)+lang.views.search.alert.minutes/* "분 " */+Math.floor((totalTime%60))+lang.views.search.alert.second/* "초" */;
						}else{
							totalRecTime = Math.floor(totalTime)+lang.views.search.alert.second/* "초" */;
						}
						// 조회된 녹취파일의 총 통화시간은 totalTime이며 제한용량 초과시 다운로드에 실패 할 수
						// 있습니다.
						// 다운로드 시작 후 등록된 녹취파일은 포함되지 않습니다.
						setTimeout(function(){
							
							
							// 2GB이상일 경우 다운로드 할 수 없습니다.
							// 다운로드 가능한 총 통화시간(예상치): 240시간
							if(confirm(lang.views.search.alert.comfrimFullDown/*
																				 * 현재
																				 * 조회하신
																				 * 조건의
																				 * 모든
																				 * 녹취파일을
																				 * 다운로드하시겠습니까?
																				 */+"\n\n"+lang.views.search.alert.caution2GB/**
																																							 * **파일용량이
																																							 * 2GB이상일
																																							 * 경우
																																							 * 다운로드
																																							 * 할 수
																																							 * 없습니다***
																																							 */+"\n"+lang.views.search.alert.selectCallTime/*
																																																												 * 선택한
																																																												 * 파일의
																																																												 * 총
																																																												 * 통화시간 :
																																																												 */+totalRecTime+"\n"+lang.views.search.alert.downloadableCallTime/*
																																																																																				 * 다운로드
																																																																																				 * 가능한
																																																																																				 * 총
																																																																																				 * 통화시간(예상) :
																																																																																				 * 250시간
																																																																																				 */)){
								var enc="false";
								
								if(encYn == "Y"){
									top.layer_popup("#recEncPopup");
									top.$(".enc_ui_hide").hide();
									top.ui_controller();
									top.$("#closeEncYn").unbind("click");
									top.$("#downloadEncYn").unbind("click");
									
									top.$("#closeEncYn").click(function(event){
										top.layer_popup_close("#recEncPopup");
									})
									
									top.$("#downloadEncYn").click(function(event){
										if(top.$("#approveUserGroup").val()=="encY")
											fulldown(true);
										else
											fulldown(false);
										top.layer_popup_close("#recEncPopup");
									});
									
								}else{
									fulldown(true);
								}
								progress.off();
							}else{
								progress.off();
							}
							
						}, 1000)
					}
				}
			});
			
		}
	}
}

function selectDelete(){
	layer_popup("#deletePopup");
	$("#delVolume").val("S"); // S(Select)
	$("#deleteSelect").val("A"); // A(inActive)
	$("#deleteBtn").show();
}

function allDelete(){
	layer_popup("#deletePopup");
	$("#delVolume").val("A"); // A(all)
	$("#deleteSelect").val("A") // A(inActive)
	$("#deleteBtn").show();
}

function clickDelete(delVolume, delType){
	if(delVolume == 'S' && delType == 'A'){ // 선택삭제 + 데이터 비활성
		/* alert(delVolume + delType); */ // SA
		if(gridSearch.getCheckedRows(0) == ""){
			alert(lang.views.search.alert.msg101)/* 비활성화할 대상이 없습니다. */
		}else{
			if(confirm(lang.views.search.alert.msg103)){ /*
															 * 현재 선택하신 녹취이력을
															 * 비활성시키겠습니까?
															 */
				var checked = gridSearch.getCheckedRows(0).split(",");
				var rstIdx = new Array();
				
				for(var i = 0 ; i < checked.length ; i++){
					rstIdx.push(gridSearch.cells(checked[i], gridSearch.getColIndexById('rec_url')).getValue());
				}
				
				var rst = rstIdx.join(",");
				
				$.ajax({
					url : contextPath + "/inActive.do",
					data : {
						rowList : rst
					},
					type:"POST",
					dataType:"json",
					success : function(result){
						alert(lang.views.search.alert.msg104)// 비활성에 성공하였습니다.
						gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
							gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
							gridSearch.changePage(1);
						});
					},
					error : function(error) {
						alert(lang.views.search.alert.msg105)// 비활성에 실패하였습니다.
					}
				});
			}
		}
	}else if(delVolume == 'A' && delType == 'A'){ // 전체삭제 + 데이터 비활성
		/* alert(delVolume + delType); */ // AA
		if(gridSearch.getRowsNum() == 0){
			alert(lang.views.search.alert.msg101)/* 비활성화할 대상이 없습니다. */
		}else{
			if(confirm(lang.views.search.alert.msg102)){/*
														 * 현재 조회하신 조건의 모든 녹취이력을
														 * 비활성시키겠습니까?
														 */
				var all = gridSearch.getAllRowIds().split(",");
				var rstIdx = new Array();
				
				for(var i = 0 ; i < all.length ; i++){
					rstIdx.push(gridSearch.cells(all[i], gridSearch.getColIndexById('rec_url')).getValue());
				}
				
				var rst = rstIdx.join(",");
				
				$.ajax({
					url : contextPath + "/inActive.do",
					data : {
						rowList : rst
					},
					type:"POST",
					dataType:"json",
					success : function(result){
						alert(lang.views.search.alert.msg104)// 비활성에 성공하였습니다.
						gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
							gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
							gridSearch.changePage(1);
						});
					},
					error : function(error) {
						alert(lang.views.search.alert.msg105)// 비활성에 실패하였습니다.
					}
				});
			}
		}
	}else if(delVolume == 'S' && delType == 'D'){ // 선택삭제 + 파일/데이터 삭제
		/* alert(delVolume + delType); */ // SD
		if(gridSearch.getCheckedRows(0) == ""){
			alert(lang.views.search.alert.msg83)/* 삭제할 대상이 없습니다. */
		}else{
			// db삭제 유무
			var dbDel='N';
			
			if(confirm(lang.views.search.alert.msg99)){/*
														 * 현재 선택하신 녹취파일을
														 * 삭제하시겠습니까?
														 */
				if(confirm(lang.views.search.alert.msg100)){/*
															 * 현재 선택하신 녹취이력 또한
															 * 삭제하시겠습니까?
															 */
					// 디비도 삭제하는 경우
					dbDel='Y'
				}
				
				var checked = gridSearch.getCheckedRows(0).split(",");
				var rstIdx = new Array();
				for(var i = 0 ; i < checked.length ; i++){
					rstIdx.push(gridSearch.cells(checked[i], gridSearch.getColIndexById('rec_url')).getValue());
				}
				var rst = rstIdx.join(",");
				
				var query = formToSerialize();
				 
					progress.on();
					
					$.ajax({
						url:contextPath+"/delete_select.do"+query,
						data : {
							chList : rst
						},
						type:"POST",
						dataType:"json",
						async: true,
						cache: false,
						success:function(jRes){
							
							if(jRes.success=="Y"){
								// 삭제 결과 담을 배열
								var succeseList = [];
							
								var recIpList = jRes.resData.serverIpList;
								var whereQuery = jRes.resData.whereQuery;
								var listenType = jRes.resData.listenType;
								
								for(var i=0;i<recIpList.length;i++){
									var dataStr = {
										"whereQuery" :whereQuery,
										"dbDel" : dbDel,
										"ip" :	recIpList[i].split("://")[1].split(":")[0],
										"listenType" :listenType
									}
									
									$.ajax({
										url:recIpList[i]+"delete",
										// url:"http://127.0.0.1:28881/delete",
										type:"post",
										data:dataStr,
										async: true,
										cache: false,
										success:function(jRes){
											succeseList.push(jRes);
										}
									});
									
								}
								var inter = setInterval(function(){
									// 삭제 결과 확인하는 인터벌
									$.ajax({
										url:contextPath+"/keepAlive.do",
										dataType:"json",
										async: true,
										cache: false,
										success:function(jRes){
											if(succeseList.length==recIpList.length){
												// 클리어 인터벌
												clearInterval(inter);
												progress.off();
												succeseList.forEach( function( v, i ){
												  if( v === 'N' ){
													    alert(lang.views.search.alert.msg88)// 녹취
																							// 삭제에
																							// 실패하였습니다.
													    return false;
												   }
												});
												alert(lang.views.search.alert.msg86)// 삭제에
																					// 성공하였습니다.
												if(dbDel=='Y'){
													gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
														gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
														gridSearch.changePage(1)

														var evalColum=gridSearch.getColIndexById("evaluation");
														
														// 나중에 1001은 지워야 함 테스트용
														/*
														 * if(evalColum!=undefined&&(userInfoJson.userLevel ==
														 * "E1006" ||
														 * userInfoJson.userLevel ==
														 * "E1005")){
														 * gridSearch.setColumnHidden(evalColum,
														 * false); // 평가 컬럼을 보여줌
														 * }else
														 * if(userInfoJson.userLevel ==
														 * "E1001"){
														 * gridSearch.setColumnHidden(evalColum,
														 * false); // 평가 컬럼을 보여줌
														 * }else{
														 * gridSearch.setColumnHidden(evalColum,
														 * true); // 평가 컬럼을 사라지게
														 * 함 }
														 */
													});
												}
											}
										},
										error : function(error) {
											// 클리어 인터벌
											clearInterval(inter);
											progress.off();
											alert(lang.views.search.alert.msg87)// 녹취
																				// 삭제에
																				// 실패하였습니다.
									    }
									});
								},3000)
								
							}else {
								// 아이피 읽어오기 실패
								alert(lang.views.search.alert.msg87)// 녹취 삭제에
																	// 실패하였습니다.
							}
						}
					});
			}else{
				return false;
			}
		}
	}else if(delVolume == 'A' && delType == 'D'){ // 전체삭제 + 파일/데이터 삭제
		/* alert(delVolume + delType); */ // AD
		
		if(csvFlag==1){
			if(gridSearch.getRowsNum() == 0){
				alert(lang.views.search.alert.msg83)/* 삭제할 대상이 없습니다. */
			}else{
				// db삭제 유무
				var dbDel='N';
				
				if(confirm(lang.views.search.alert.msg84)){/*
															 * 현재 조회하신 조건의 모든
															 * 녹취파일을 삭제하시겠습니까?
															 */
					if(confirm(lang.views.search.alert.msg85)){/*
																 * 현재 조회하신 조건의
																 * 모든 녹취이력 또한
																 * 삭제하시겠습니까?
																 */
						// 디비도 삭제하는 경우
						dbDel='Y'
					}
					
					var checked = gridSearch.getAllRowIds().split(",");
					var rstIdx = new Array();
					for(var i = 0 ; i < checked.length ; i++){
						rstIdx.push(gridSearch.cells(checked[i], gridSearch.getColIndexById('rec_url')).getValue());
					}
					var rst = rstIdx.join(",");
					
					var query = formToSerialize();
					 
						progress.on();
						
						$.ajax({
							url:contextPath+"/delete_select.do"+query,
							data : {
								chList : rst
							},
							type:"POST",
							dataType:"json",
							async: true,
							cache: false,
							success:function(jRes){
								
								if(jRes.success=="Y"){
									// 삭제 결과 담을 배열
									var succeseList = [];
								
									var recIpList = jRes.resData.serverIpList;
									var whereQuery = jRes.resData.whereQuery;
									var listenType = jRes.resData.listenType;
									
									for(var i=0;i<recIpList.length;i++){
										var dataStr = {
											"whereQuery" :whereQuery,
											"dbDel" : dbDel,
											"ip" :	recIpList[i].split("://")[1].split(":")[0],
											"listenType" :listenType
										}
										
										$.ajax({
											url:recIpList[i]+"delete",
											// url:"http://127.0.0.1:28881/delete",
											type:"post",
											data:dataStr,
											async: true,
											cache: false,
											success:function(jRes){
												succeseList.push(jRes);
											}
										});
										
									}
									var inter = setInterval(function(){
										// 삭제 결과 확인하는 인터벌
										$.ajax({
											url:contextPath+"/keepAlive.do",
											dataType:"json",
											async: true,
											cache: false,
											success:function(jRes){
												if(succeseList.length==recIpList.length){
													// 클리어 인터벌
													clearInterval(inter);
													progress.off();
													succeseList.forEach( function( v, i ){
													  if( v === 'N' ){
														    alert(lang.views.search.alert.msg88)// 녹취
																								// 삭제에
																								// 실패하였습니다.
														    return false;
													   }
													});
													alert(lang.views.search.alert.msg86)// 삭제에
																						// 성공하였습니다.
													if(dbDel=='Y'){
														gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
															gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
															gridSearch.changePage(1)

															var evalColum=gridSearch.getColIndexById("evaluation");
															
															// 나중에 1001은 지워야 함
															// 테스트용
															/*
															 * if(evalColum!=undefined&&(userInfoJson.userLevel ==
															 * "E1006" ||
															 * userInfoJson.userLevel ==
															 * "E1005")){
															 * gridSearch.setColumnHidden(evalColum,
															 * false); // 평가 컬럼을
															 * 보여줌 }else
															 * if(userInfoJson.userLevel ==
															 * "E1001"){
															 * gridSearch.setColumnHidden(evalColum,
															 * false); // 평가 컬럼을
															 * 보여줌 }else{
															 * gridSearch.setColumnHidden(evalColum,
															 * true); // 평가 컬럼을
															 * 사라지게 함 }
															 */
														});
													}
												}
											},
											error : function(error) {
												// 클리어 인터벌
												clearInterval(inter);
												progress.off();
												alert(lang.views.search.alert.msg87)// 녹취
																					// 삭제에
																					// 실패하였습니다.
										    }
										});
									},3000)
									
								}else {
									// 아이피 읽어오기 실패
									alert(lang.views.search.alert.msg87)// 녹취
																		// 삭제에
																		// 실패하였습니다.
								}
							}
						});
				}else{
					return false;
				}
			}
		}else{
			if(gridSearch.getRowsNum() == 0){
				alert(lang.views.search.alert.msg83)/* 삭제할 대상이 없습니다. */
			}else{
				// db삭제 유무
				var dbDel='N';
				
				if(confirm(lang.views.search.alert.msg84)){/*
															 * 현재 조회하신 조건의 모든
															 * 녹취파일을 삭제하시겠습니까?
															 */
					if(confirm(lang.views.search.alert.msg85)){/*
																 * 현재 조회하신 조건의
																 * 모든 녹취이력 또한
																 * 삭제하시겠습니까?
																 */
						// 디비도 삭제하는 경우
						dbDel='Y'
					}
					
					var query = formToSerialize();
					
					progress.on();
					
					$.ajax({
						url:contextPath+"/delete_all.do"+query,
						type:"get",
						dataType:"json",
						async: true,
						cache: false,
						success:function(jRes){
							
							if(jRes.success=="Y"){
								// 삭제 결과 담을 배열
								var succeseList = [];
								
								var recIpList = jRes.resData.serverIpList;
								var whereQuery = jRes.resData.whereQuery;
								var listenType = jRes.resData.listenType;
								
								for(var i=0;i<recIpList.length;i++){
									var dataStr = {
											"whereQuery" :whereQuery,
											"dbDel" : dbDel,
											"ip" :	recIpList[i].split("://")[1].split(":")[0],
											"listenType" :listenType
									}
									
									$.ajax({
										url:recIpList[i]+"delete",
										// url:"http://127.0.0.1:28881/delete",
										type:"post",
										data:dataStr,
										async: true,
										cache: false,
										success:function(jRes){
											succeseList.push(jRes);
										}
									});
									
								}
								var inter = setInterval(function(){
									// 삭제 결과 확인하는 인터벌
									$.ajax({
										url:contextPath+"/keepAlive.do",
										dataType:"json",
										async: true,
										cache: false,
										success:function(jRes){
											if(succeseList.length==recIpList.length){
												// 클리어 인터벌
												clearInterval(inter);
												progress.off();
												succeseList.forEach( function( v, i ){
													if( v === 'N' ){
														alert(lang.views.search.alert.msg88)// 녹취
																							// 삭제에
																							// 실패하였습니다.
														return false;
													}
												});
												alert(lang.views.search.alert.msg86)// 삭제에
																					// 성공하였습니다.
												if(dbDel=='Y'){
													gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
														gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
														gridSearch.changePage(1)
														
														var evalColum=gridSearch.getColIndexById("evaluation");
														
														// 나중에 1001은 지워야 함 테스트용
														/*
														 * if(evalColum!=undefined&&(userInfoJson.userLevel ==
														 * "E1006" ||
														 * userInfoJson.userLevel ==
														 * "E1005")){
														 * gridSearch.setColumnHidden(evalColum,
														 * false); // 평가 컬럼을 보여줌
														 * }else
														 * if(userInfoJson.userLevel ==
														 * "E1001"){
														 * gridSearch.setColumnHidden(evalColum,
														 * false); // 평가 컬럼을 보여줌
														 * }else{
														 * gridSearch.setColumnHidden(evalColum,
														 * true); // 평가 컬럼을 사라지게
														 * 함 }
														 */
													});
												}
											}
										},
										error : function(error) {
											// 클리어 인터벌
											clearInterval(inter);
											progress.off();
											alert(lang.views.search.alert.msg87)// 녹취
																				// 삭제에
																				// 실패하였습니다.
										}
									});
								},3000)
								
							}else {
								// 아이피 읽어오기 실패
								alert(lang.views.search.alert.msg87)// 녹취 삭제에
																	// 실패하였습니다.
							}
						}
					});
				}else{
					return false;
				}
			}
		}
		
	}else{
		alert("error")
	}
	layer_popup_close("#deletePopup");
}


/*
 * @David @description myfolder 추가 로직
 */

function addMyfolder(str){
	if(str == ""){
		alert(lang.myfolder.alert.inputName); /* 폴더명을 입력해주세요. */
	}else{
		$.ajax({
				url : contextPath + "/createMyfolder.do"
			,	data : {
					folderName : str
				}
			,	type:"POST"
			,	dataType:"json"
			,	async: false
			,	success:function(jRes){
				if(jRes.success == "Y"){
					alert(lang.myfolder.alert.completingAdd); /*
																 * 폴더 추가가 완료
																 * 되었습니다.
																 */
					// 팝업 닫기
					layer_popup_close("#addMyfolderPopup")
					// 팝업창 원상태 복구시키기
					$("#addMyfolderPopup .ui_row_input_wrap .ui_padding").empty();
					$("#addMyfolderPopup .ui_row_input_wrap .ui_padding").append('<label class="ui_label_essential">'+lang.common.label.selectFolder+'</label><select id="myfolderSelect"></select>');
					$("#addMyfolderItem").attr('onclick', 'addMyfolderItem();');
					// 마이폴더 탭 열려있으면 폴더목록 리로드 시키기
					if(top.myTabbar.getAllTabs().includes(contextPath +"/myfolder/myfolder")){
						var myfolder = top.document.getElementsByName('pageFrame')[top.myTabbar._getIndex(contextPath +"/myfolder/myfolder")].contentWindow;
						
						myfolder.myfolderSideBar.addItem({id:str,text:str,icon: "regular/folder-open.svg",selected : false})
						myfolder.myfolderSideBar._setItemActive(str)
					
					}
				}else{
					alert(lang.myfolder.alert.existSameFolder); /*
																 * 동일한 폴더가
																 * 존재합니다. 다시
																 * 입력해주세요.
																 */
				}
			}
		})
	}
}

function addMyfolderItem(){
		 var gridArr = gridSearch.getCheckedRows(0).split(",");

		 var recDateArr = [];
		 var recTimeArr = [];
		 var extArr = [];

		 for (var i = 0; i < gridArr.length; i++) {
			 recDateArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,''))
			 recTimeArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,''))
			 extArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_ext_num")).getValue())
		 }


		 $.ajax({
			 url : contextPath + "/insertMyfolderItem.do",
		 	 type : "POST",
		 	 data : {
		 		 	rFolderName : $('#myfolderSelect').val()
		 		, 	recDateArr : recDateArr.toString()
	 		 	,	recTimeArr : recTimeArr.toString()
	 		 	,	extArr : extArr.toString()
		 	 },
		 	  dataType:"json",
		 	  async: true,
		 	  success:function(jRes){
		 		 if(jRes.success == "Y"){
		 			 alert(lang.views.search.alert.msg33/*
														 * "마이폴더에 녹취파일을
														 * 추가하였습니다."
														 */);
		 		 }else{
		 			alert(lang.views.search.alert.msg34/*
														 * "마이폴더에 녹취파일 추가를 실패
														 * 하였습니다."
														 */);
		 		 }
		 		layer_popup_close("#addMyfolderPopup")
		 	  }
		 })
}


// //////////////////////////// 평가하기 //////////////////////////////
function evaluationSheetSetLoad(){
	gridEvaluation = createGrid("gridEvaluation", null, "evaluation/resultGrid", "", "", 20, [],[]);
}

// 평가 팝업 오픈 [그리드 평가 버튼 onclick에서 불러옴]
function openEvalPop(listenUrl, rRecId, rRecName, rRecDate, rRecTime, custName, custPhone, userType, bgCode, mgCode, sgCode, evalStatus){
	// FIXME : 임시로 하단고정 플레이어에 값 전달 // 우선은 recsee에 있는 테스트 파일 재생 // 실 테스트시 2번째 인자에
	// listenUrl 넘기면 됨
// var $select = $("select[name=campaign_list]");
// $select.find("option[value!='']").remove()

// $("#evaluationSheet").data("ListenUrl", listenUrl);
// $("#evaluationSheet").data("rRecId", rRecId);
// $("#evaluationSheet").data("rRecName", rRecName);
// $("#evaluationSheet").data("rRecDate", rRecDate);
// $("#evaluationSheet").data("rRecTime", rRecTime);

// gridEvaluation.clearAndLoad(contextPath +
// "/evaluation/resultGrid.xml",function(){
//
// });
	if(evalStatus == "Y"){
		var dataStr = {
				"listenUrl" : listenUrl
		}
		$.ajax({
			url : contextPath + "/evaluation/searchEvalData",
			type : "POST",
			data : dataStr,
			dataType : "json",
			async : false,
			success : function(jRes) {
				if(jRes.success == "Y") {
					var resultList = jRes.resData.result;
					if(resultList.length>0){
						var evalatorName = resultList[0].r_evalator_name;
						var evalatorId = resultList[0].r_evalator_id;
						var cclist = resultList[0].r_ecamp_code;
						var cnlist = resultList[0].r_ecamp_name;
						var evalCode = resultList[(resultList.length-1)].r_eval_code;
						var campCode =  resultList[0].r_ecamp_code;

						var windowOpenUrlInfo = contextPath+"/evalSheetPage?listenUrl="+listenUrl+"&rRecId="+rRecId+"&rRecName="+encodeURI(encodeURIComponent(rRecName))+"&rRecDate="+rRecDate
						+"&rRecTime="+rRecTime+"&ip="+$('#ip').val()+"&port="+$('#port').val()+"&firstEvalYN=N&cclist="+encodeURI(encodeURIComponent(cclist))+"&cnlist="+encodeURI(encodeURIComponent(cnlist))
						+"&evalatorId="+userInfoJson.userId+"&evalatorName="+encodeURI(encodeURIComponent(userInfoJson.userName))
						+"&custName="+encodeURI(encodeURIComponent(custName))+"&custPhone="+custPhone+"&userType="+encodeURI(encodeURIComponent(userType))+"&bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode
						+"&evaluator="+evalatorId+"&evalatorId="+userInfoJson.userId+"&evalatorName="+encodeURI(encodeURIComponent(evalatorName))+"&evalCode="+evalCode+"&campCode="+campCode;

						if(winObj == null){ // window.open 중복 생성 방지 및 기존 열린곳으로
											// 포커스 주기
							// 사이즈 등 여러가지 문제로 인해
							// winObj = window.open(windowOpenUrlInfo,
							// "evalPage", "resizable=yes,
							// toolbar=no,location=no, width=1300,
							// height=925,scrollbars=yes
							// ,left=200,top=50,fullscreen'");
							winObj = window.open(windowOpenUrlInfo,"evalPage",'height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes');
						}else{
							if(winObj.closed == true){ // 기존 팝업이 꺼져있으면 새로 열어줌
								winObj = window.open(windowOpenUrlInfo,"evalPage",'height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes');
								// winObj = window.open(windowOpenUrlInfo,
								// "evalPage", "resizable=yes,
								// toolbar=no,location=no, width=1300,
								// height=925,scrollbars=yes
								// ,left=200,top=50,fullscreen'");
							}else{
								if(confirm(lang.views.search.alert.msg35/*
																		 * "이미
																		 * 평가중인
																		 * 평가지가
																		 * 있습니다.
																		 * 선택한
																		 * 평가지를
																		 * 새로
																		 * 열겠습니까?"
																		 */)){
									winObj = window.open(windowOpenUrlInfo,"evalPage",'height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes');

									// winObj = window.open(windowOpenUrlInfo,
									// "evalPage", "resizable=yes,
									// toolbar=no,location=no, width=1300,
									// height=925,scrollbars=yes
									// ,left=200,top=50,fullscreen'");
								}else{
									winObj.focus();
								}
							}
						}


					}
				}
			}
		});
	}else{
		$.ajax({
			url : contextPath + "/evaluation/campaignList?callId=" + rRecId + "&t=" + new Date().getTime(),
			type : "POST",
			dataType : "json",
			async : false,
			success : function(jRes) {
				if(jRes.success == "Y") {
					var campCodeArr = [];
					var groupTypeArr = [];
					var campaignList = jRes.resData.result;
					$.each(campaignList, function(i, c) {
						campCodeArr[i] = c.rEcampCode;
						groupTypeArr[i] = c.rGroupType;

						if(groupTypeArr[i] == "" || groupTypeArr[i] == null || groupTypeArr[i] == undefined) groupTypeArr[i] = "99999";
					});

					var dataStr = {
						"campCodeList" : campCodeArr.toString(),
						"listenUrl" : listenUrl,
						"userId"	: userInfoJson.userId,
						"groupType" : groupTypeArr.toString()
					}

					$.ajax({
						url : contextPath + "/evaluation/checkCampList",
						type : "POST",
						data : dataStr,
						dataType : "json",
						async : false,
						success : function(jRes) {
							if(jRes.success == "Y") {
								var cclist = jRes.resData.campCodeListResult;
								var cnlist = jRes.resData.campNameListResult;
								var $select = $("select[name=campaign_list]");
								$select.find("option[value!='']").remove();
								if(cclist.length>0){
									for(z=0; z<cclist.length; z++){
										var $option = $("<option></option>").attr({
											value: cclist[z],
										}).text(cnlist[z]);
										$select.append($option);
									}
									$("select[id=camp_list]").find("option:eq(0)").prop("selected", true);

									$(".agentFeedForm").css("display","none");

									var windowOpenUrlInfo = contextPath+"/evalSheetPage?listenUrl="+listenUrl+"&rRecId="+rRecId+"&rRecName="+encodeURI(encodeURIComponent(rRecName))+"&rRecDate="+rRecDate
															+"&rRecTime="+rRecTime+"&ip="+$('#ip').val()+"&port="+$('#port').val()+"&firstEvalYN=Y&cclist="+encodeURI(encodeURIComponent(cclist))+"&cnlist="+encodeURI(encodeURIComponent(cnlist))
															+"&evalatorId="+userInfoJson.userId+"&evalatorName="+encodeURI(encodeURIComponent(userInfoJson.userName))
															+"&custName="+encodeURI(encodeURIComponent(custName))+"&custPhone="+custPhone+"&userType="+encodeURI(encodeURIComponent(userType))+"&bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode;
									if(winObj == null){ // window.open 중복 생성 방지
														// 및 기존 열린곳으로 포커스 주기
										// 사이즈 등 여러가지 문제로 인해
										winObj = window.open(windowOpenUrlInfo, "evalPage", "resizable=yes, toolbar=no,location=no, width=1300, height=925,scrollbars=yes ,left=200,top=50'");
									}else{
										if(winObj.closed == true){ // 기존 팝업이
																	// 꺼져있으면 새로
																	// 열어줌

											winObj = window.open(windowOpenUrlInfo, "evalPage", "resizable=yes, toolbar=no,location=no, width=1300, height=925,scrollbars=yes ,left=200,top=50'");
										}else{
											if(confirm(lang.views.search.alert.msg35/*
																					 * "이미
																					 * 평가중인
																					 * 평가지가
																					 * 있습니다.
																					 * 선택한
																					 * 평가지를
																					 * 새로
																					 * 열겠습니까?"
																					 */)){

												winObj = window.open(windowOpenUrlInfo, "evalPage", "resizable=yes, toolbar=no,location=no, width=1300, height=925,scrollbars=yes ,left=200,top=50'");
											}else{
												winObj.focus();
											}
										}
									}
								}else{
									alert(lang.views.search.alert.msg36/*
																		 * "평가 할
																		 * 캠페인이
																		 * 존재하지
																		 * 않습니다."
																		 */)
								}

							}
						}
					});
				}
			}
		});
	}
}

// 평가 완료되면 해당 함수 처리로 clearAndLoad
function searchClearAndLoadGrid(){
	gridSearch.clearAndLoad(contextPath+"/search_list.xml" + formToSerialize(), function() {
		gridContextUrl = contextPath+"/search_list.xml" + formToSerialize();
		gridSearch.changePage(1)

		var evalColum=gridSearch.getColIndexById("evaluation");
		
		// 나중에 1001은 지워야 함 테스트용
		/*
		 * if(evalColum!=undefined&&(userInfoJson.userLevel == "E1006" ||
		 * userInfoJson.userLevel == "E1005")){
		 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌 }else
		 * if(userInfoJson.userLevel == "E1001"){
		 * gridSearch.setColumnHidden(evalColum, false); // 평가 컬럼을 보여줌 }else{
		 * gridSearch.setColumnHidden(evalColum, true); // 평가 컬럼을 사라지게 함 }
		 */
	});
}

// 플레이어 변경
function dualPlayerChange(){
	top.playerToggle();
}

function selectPlayer(gridObj, id){
	var selectId = gridSearch.getSelectedId();
	if(selectId == null){
		alert(lang.views.search.alert.msg4/* 선택된 녹취 파일이 없습니다. */);
		return false;
	}
	
	var recDate = gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,'');
	var recTime = gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,'');
	var extArr = gridSearch.cells(gridSearch.getSelectedId(),gridSearch.getColIndexById("r_ext_num")).getValue().replace(/-/gi,'');
	
	var checkFile = true;
	
	// STT 작업 되었는지 확인
	$.ajax({
			url : contextPath + "/player/getList"
		,	type : "POST"
		,	data : {
				recDate : recDate
			,	recTime : recTime
			,	ext : extArr
		}
		,	dataType:"json"
		,	async: false
		,	success : function(res){
			var cnt = res.resData.count; 
			if(cnt > 0){
				checkFile = false;
			}else{
				checkFile = true;
			}
			
		}
	})
	
	if(checkFile){
		// STT가 작업되지 않았으면 기본 플레이어로 실행한다.
		play(gridObj, id);
	}else{
		// STT가 작업되엇으면 STT플레이어로 실행한다.
		sttPlayerPopup();
	}
}

// STT 플레이어
function sttPlayerPopup(t){
	var selectId=null;
	
	if(t == undefined){
		selectId = gridSearch.getSelectedId();
	}else{
		selectId=t;
	}
	
	if(selectId == null){
		alert(lang.views.search.alert.msg4/* 선택된 녹취 파일이 없습니다. */);
		return false;
	}
	
	var recDate = gridSearch.cells(selectId,gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,'');
	var recTime = gridSearch.cells(selectId,gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,'');
	var extArr = gridSearch.cells(selectId,gridSearch.getColIndexById("r_ext_num")).getValue().replace(/-/gi,'');
	
	var checkFile = true;
	
	// STT 작업 되었는지 확인
	$.ajax({
			url : contextPath + "/player/getList"
		,	type : "POST"
		,	data : {
				recDate : recDate
			,	recTime : recTime
			,	ext : extArr
		}
		,	dataType:"json"
		,	async: false
		,	success : function(res){
			var cnt = res.resData.count; 
			if(cnt > 0){
				checkFile = false;
			}else{
				checkFile = true;
			}
			
		}
	})
	
	if(checkFile){
		alert(lang.views.search.alert.msg80/* "현재 STT 분석 진행중입니다." */);
		return false;
	}
	
	$("#popRecDate").val(recDate);
	$("#popRecTime").val(recTime);
	$("#popExt").val(extArr);
	
	if(t == undefined){
		var myForm = document.popForm;
		var url =  contextPath + "/SttPlayer";
		window.open("","popForm","toolbar=no,width=1050,height=830,directories=no,status=no,left=0,top=0");
		myForm.action = url;
		myForm.method = "post";
		myForm.target="popForm";
		myForm.submit();
	}else {
		var myForm = document.popForm;
		var url =  contextPath + "/callScript";
		window.open("","popForm2","toolbar=no,width=700,height=500,location=no, directories=no,status=no,left=0,top=0");
		myForm.action = url;
		myForm.method = "post";
		myForm.target="popForm2";
		myForm.submit();
	}

}

// 풀 다운로드
function fulldown(enc){
	var encYn = "true";
	progress.on(); 
	if(enc==false)
		encYn='false';
	var query = formToSerialize();
	$.ajax({
		url:contextPath+"/fullDownload.do"+query+"&encYn="+encYn,
		type:"get",
		dataType:"json",
		async: true,
		cache: false,
		success:function(jRes){
			 if(jRes.success == "Y"){
				// 셋 인터벌
				var inter = setInterval(function(){
					$.ajax({
						url:contextPath+"/keepAlive.do?path="+jRes.resData.filePath,
						dataType:"json",
						async: true,
						cache: false,
						success:function(jRes){
							console.log(jRes.result);
							if(jRes.result == "finished making zip"){
								clearInterval(inter);
								alert(lang.views.search.alert.completedZip/*
																			 * "압축파일
																			 * 생성이
																			 * 완료되었습니다."
																			 */);
								$("#download").attr("action", contextPath+"/downloadFull.do?path="+jRes.resData.filePath);
								$("#download").submit();
								progress.off();
							} else if(jRes.result == "1.8G over"){
								clearInterval(inter);
								alert(lang.views.search.alert.zipOver2GB/*
																		 * '압축파일
																		 * 크기가
																		 * 2GB를
																		 * 초과하여
																		 * 다운로드에
																		 * 실패하였습니다.'
																		 */+'\n\n'+lang.views.search.alert.successFileBefore/*
																																								 * 2GB이전까지
																																								 * 압축
																																								 * 성공한
																																								 * 파일
																																								 * 개수:
																																								 */+jRes.resData.fileCount)
								progress.off();
							} else if(jRes.success == "Y"){
								$("#holdon-content-container").append("<div id='holdon-message'></div>")
								$("#holdon-message").text(lang.views.search.alert.zipProgress/*
																								 * "압축파일을
																								 * 생성하는
																								 * 중입니다.(진행률: "
																								 */+((jRes.resData.fileCount/jRes.resData.totalCount)*100).toFixed(1)+"%)")
							}
						}
					});
				},1000);
			 }
		}
	});
}

// 복사 붙여넣기
function onKeyPressed(code,ctrl,shift){
	if(code==67&&ctrl){
		if (!objGrid._selectionArea) return alert(lang.views.search.alert.selectGrid);/*
																						 * 복사할
																						 * 범위를
																						 * 선택해
																						 * 주세요.
																						 */
			objGrid.setCSVDelimiter("\t");
			objGrid.copyBlockToClipboard()
		}
		if(code==86&&ctrl){
			objGrid.setCSVDelimiter("\t");
			objGrid.pasteBlockFromClipboard()
		}
	return true;
}


// 선택파일 업로드 하기
function uploadSend(){
	 var gridArr = gridSearch.getCheckedRows(0).split(",");
	 
	 progress.on();

	 var recDateArr = [];
	 var recTimeArr = [];
	 var extArr = [];

	 for (var i = 0; i < gridArr.length; i++) {
		 recDateArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_date")).getValue().replace(/-/gi,''));
		 recTimeArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_rec_time")).getValue().replace(/:/gi,''));
		 extArr.push(gridSearch.cells(gridArr[i],gridSearch.getColIndexById("r_ext_num")).getValue());
	 }
	 
	 $.ajax({
		 url : contextPath + "/insertUploadSend.do",
	 	 type : "POST",
	 	 data : {
	 		 	recDateArr : recDateArr.toString()
		 	,	recTimeArr : recTimeArr.toString()
		 	,	extArr : extArr.toString()
	 	 },
	 	  dataType:"json",
	 	  async: true,
	 	  success:function(jRes){
		 		 if(jRes.success == "Y"){
			 			$("#searchBtn").trigger('click');
			 			if(jRes.resDate.msg == "SOME REQUEST FAIL"){
				 			 alert(lang.views.search.alert.someReqFail);/*
																		 * "일부
																		 * 신청이
																		 * 실패
																		 * 하였습니다."
																		 */
			 			}else{
				 			 alert(lang.views.search.alert.reqTrans);/*
																		 * "즉시
																		 * 전송
																		 * 신청을
																		 * 하였습니다."
																		 */
			 			}

			 		 }else{
			 			alert(lang.views.search.alert.transReqFail);/*
																	 * "즉시 전송
																	 * 신청에 실패
																	 * 하였습니다."
																	 */
			 		 }
			 	  },
	 	  complete : function(){
	 		 progress.off();
	 	  }
	 })
}

function campaignPageChange(){

	// 평가하기 캠페인 선택 시;
	$("#camp_list").change(function(){
		var camp_Code = this.value;
		var dataStr = {
			"campCode" : camp_Code
		}

		$.ajax({
			url : contextPath + "/evaluation/CampToSheetInfo",
			type : "POST",
			data : dataStr,
			dataType : "json",
			async : false,
			success : function(jRes) {
				if(jRes.success == "Y") {
					var sheetCode = jRes.resData.sheetCode;
					$("#evaluationSheet").data("sheetCode", sheetCode);

					$.ajax({
						url: contextPath+"/selectSheetInfo.do",
						type: "POST",
						dataType: "json",
						data: {sheetCode: sheetCode},
						success: function(jRes){
							if(jRes.success=="Y"){
								var sheetScore=0;
								Number(sheetScore);

								window.selectResult = jRes.resData.selectResult;
								for(var i = 0; i< jRes.resData.selectboxResultsize; i++){
									sheetScore += Number(selectResult[i].mark);
								}
								$("input[name='maxScore']").val(sheetScore);
							}
						}
					})

					gridEvaluation.clearAndLoad(contextPath + "/evaluation/resultGrid.xml" + "?campCode=" + camp_Code + "&sheetCode=" + sheetCode, function(){

						// 라디오 버튼 이벤트 처리
						$("input:radio").click(function(){
							var point = $(this).val();
							var evId = gridEvaluation.attachEvent('onRowSelect',function(){
								var total_point = 0;

								var id = gridEvaluation.getSelectedRowId();

								gridEvaluation.cells(id, 12).setValue(point);

								for(i=1; i<=gridEvaluation.getRowsNum(); i++){
									var nowCellPoint = 0;
									if(gridEvaluation.cells(i, 12).getValue() == "-"){
										nowCellPoint = 0
									}else if(gridEvaluation.cells(i, 12).getValue().indexOf('Input') != '-1' || gridEvaluation.cells(i, 12).getValue().indexOf('입력없음') != '-1'){
										nowCellPoint = 0;
									}else{
										nowCellPoint = gridEvaluation.cells(i, 12).getValue();
									}

									total_point = total_point + Number(nowCellPoint);
								}
								
								$("input[name='currentScore']").val(total_point);
								gridEvaluation.detachEvent(evId);
							});
						});

						var totalRows = (gridEvaluation.getRowsNum()+1);
						var bgMerge = 0;
						var bgCnt = 0;
						var mgMerge = 0;
						var mgCnt = 0;
						var sgMerge = 0;
						var sgCnt = 0;
						// 셀 병합 처리
						for(i=1; i<totalRows; i++){
							// 대분류 항목 병합처리
							if(gridEvaluation.cells(i, 7).getValue() == ""){
								if(bgCnt == 0){
									bgMerge = i-1;
								}

								bgCnt++;
							}else{
								if(bgMerge != 0 && bgCnt != 0){
									gridEvaluation.setRowspan(bgMerge,7,(bgCnt+1));

									bgMerge = 0;
									bgCnt = 0;
								}
							}

							// 중분류 항목 병합처리
							if(gridEvaluation.cells(i, 8).getValue() == ""){
								if(mgCnt == 0){
									mgMerge = i-1;
								}

								mgCnt++;
							}else{
								if(mgMerge != 0 && mgCnt != 0){
									gridEvaluation.setRowspan(mgMerge,8,(mgCnt+1));

									mgMerge = 0;
									mgCnt = 0;
								}
							}

							// 마지막 행인 경우 병합할것이 남아 있으면
							if(i+1 == totalRows && bgCnt != 0) {
								gridEvaluation.setRowspan(bgMerge,7,(bgCnt+1));
							}
							if(i+1 == totalRows && mgCnt != 0) {
								gridEvaluation.setRowspan(mgMerge,8,(mgCnt+1));
							}
						}

						if(gridEvaluation.cells(1, 3).getValue() == "Nan"){
							gridEvaluation.setColumnHidden(7, true);
							gridEvaluation.setColumnHidden(8, true);
							gridEvaluation.setColumnHidden(9, true);
						}else if(gridEvaluation.cells(1, 4).getValue() == "Nan"){
							gridEvaluation.setColumnHidden(8, true);
							gridEvaluation.setColumnHidden(9, true);
						}else if(gridEvaluation.cells(1, 5).getValue() == "Nan"){
							gridEvaluation.setColumnHidden(9, true);
						}
						progress.off();
					});

				}			}
		});

	});


	// 평가 저장버튼 이벤트
	$("#evaluate_complete_btn").click(function(event) {
		// 평가지가 선택되어있는지 체크
		var campCode = $("select[id=camp_list]").val();
		if(campCode == "" || campCode == undefined || campCode == null){
			alert(lang.views.search.alert.msg37/* "평가지 선택 후 평가를 진행해주세요." */);
			return;
		}

		// 라디오 체크가 되어있는지 체크
		var totalRows = (gridEvaluation.getRowsNum()+1);
		for(i=1; i<totalRows; i++){
			if(gridEvaluation.cells(i, 12).getValue() == "-"){
				alert(lang.views.search.alert.msg38/* "선택 되지 않은 항목이 존재합니다." */);
				return;
			}
		}
		var evalBgcode = [];
		var evalMgcode = [];
		var evalSgcode = [];
		var evalIcode = [];
		var evalScore = [];

		for(i=1; i<totalRows; i++){
			evalBgcode[i-1] = gridEvaluation.cells(i, 3).getValue();
			evalMgcode[i-1] = gridEvaluation.cells(i, 4).getValue();
			evalSgcode[i-1] = gridEvaluation.cells(i, 5).getValue();
			evalIcode[i-1] = gridEvaluation.cells(i, 6).getValue();
			evalScore[i-1] = gridEvaluation.cells(i, 12).getValue();
		}

		var dataStr = {
			"sheetCode" : $("#evaluationSheet").data("sheetCode"),
			"ListenUrl" : $("#evaluationSheet").data("ListenUrl"),
			"rEcampCode" : campCode,
			"rEcampName" : $("select[name='campaign_list'] option:selected").text(),
			"rEvalatorId" : userInfoJson.userId,
			"rEvalatorName" : userInfoJson.userName,
			"rRecDate" : $("#evaluationSheet").data("rRecDate"),
			"rRecTime" : $("#evaluationSheet").data("rRecTime"),
			"rRecId" : $("#evaluationSheet").data("rRecId"),
			"rRecName" : $("#evaluationSheet").data("rRecName"),
			"rEvalTotalMark" : $("input[name='maxScore']").val(),
			"rEvalStatus" : "C",
			"rEvalatorFeedback" : $("#EvalatorFeedback").val(),
			"rCompleteStatus" : "C",
// "rEvalDegree" : gridEvaluation.getRowsNum(),
			"rEvalDegree" : "1",
			"rEvalTotalScore" : $("input[name='currentScore']").val(),
			"evalBgcode" : evalBgcode.toString(),
			"evalMgcode" : evalMgcode.toString(),
			"evalSgcode" : evalSgcode.toString(),
			"evalIcode" : evalIcode.toString(),
			"evalScore" : evalScore.toString()
		}

		$.ajax({
			url : contextPath + "/evaluation/updateResult",
			type : "POST",
			data : dataStr,
			dataType : "json",
			async : false,
			success : function(jRes) {
				if(jRes.success == "Y") {
					alert(lang.views.search.alert.msg39/* "평가 저장이 완료되었습니다." */);
					layer_popup_close("#evaluationSheet");
				}
			}
		});
	});
		
}

function fileNameSetting(){
		
	$.ajax({
		url : contextPath + "/fileNameSetting.do",
		type : "POST",
		data : {},
		dataType : "json",
		async : false,
		success : function(jRes) {
			if(jRes.success == "Y") {
				var FileColumn = jRes.resData.FileColumn;
				
				for(key in FileColumn){
					var option = "<option value="+FileColumn[key]+">"+key+"</option>";
					$("#fileNameSetting").append(option);
				}
				
				// $("#fileNameSetting").append("<option
				// value='undervar"+undervar+"'>_</option>");
				
			}else{
				alert(jRes.result);
				return;
			}
		}
	});	
	
	$('#fileNameSetting').select2({
	    placeholder: 'File Name Setting' ,
	    templateResult : resultState ,
	    tags: true ,
	   createTag: function (params) {
		    if(params.term.indexOf(' ') != -1){
		        return {
		          id: params.term.replace(' ',''),
		          text: params.term.replace(' ',''),
		          newOption: true
		        }
		   }	        
	      },
	      multiple: true
	});
	
	function resultState (data, container) {
	    if (data.element) {
	        $ (container).addClass ($ (data.element) .attr ( "class"));
	    }
	    return data.text;
	}

	$('#fileNameSetting').on('select2:select', function(e) {
		 var element = e.params.data.element; 
		 
		 if(element == "undefined" || element == null || element == undefined) return;			 
		 
		 var $element = $(element); 	
		 
		 $element.detach(); 
		 
		 $(this).append($element); 
		 
		 $(this).trigger("change"); 
		 		 
		/*
		 * if(element.text=="_"){ $("#fileNameSetting
		 * option[value='undervar"+undervar+"']").addClass("optInvisible")
		 * undervar++; $("#fileNameSetting").append("<option
		 * value='undervar"+undervar+"'>_</option>"); }
		 */
		 
	});
	
	/*
	 * $('#fileNameSetting').on('select2:unselect', function(e) { var element =
	 * e.params.data.element;
	 * 
	 * if(element == "undefined" || element == null || element == undefined)
	 * return;
	 * 
	 * if(element.text=="_"){ $("#fileNameSetting
	 * option[value='undervar"+undervar+"']").remove(); undervar--;
	 * $("#fileNameSetting
	 * option[value='undervar"+undervar+"']").removeClass("optInvisible") } });
	 */
}

function openFaceRecordingPlayer(from) {
//	var id = gridSearch.getSelectedId();
	
//	gridSearch.forEachRow(function(val){
//		if (gridSearch.cells(val,gridSearch.getColIndexById("rownumber")).getValue() == nowListenNo) {
//			id = val;
//			return;
//		}
//	});
	
	// AJAX를 통해구해야할 로직
//	var productCode = gridSearch.getColIndexById("r_buffer7")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_buffer7")).getValue();
	var url = contextPath+"/player/recdata/"+callKey;
	$.ajax({
		url: url,
		data:{},
		type:"POST",
		dataType:"json",
		async: false,
		cache: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				data = jRes.resData.recData;
				callKey = jRes.resData.recKey;
			}else{
				data = null;
			}
		}
	});

	
	var productCode = data.buffer7;
	if (productCode.substring(0,1) == "T") {
		//투자성향일시
		$("#listen_popup_info").hide();
	} else {
		//투자성향이 아닐시
		$("#listen_popup_info").show();
		/*
		var custName = gridSearch.getColIndexById("r_cust_name")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_cust_name")).getValue();
		var custLevel = gridSearch.getColIndexById("r_buffer18")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_buffer18")).getValue();
		var productName = gridSearch.getColIndexById("r_buffer8")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_buffer8")).getValue();
		var productLevel = gridSearch.getColIndexById("r_buffer17")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_buffer17")).getValue();
		*/
		var custName = data.custName;
		var custLevel = data.buffer18;
		var productName = data.buffer8;
		var productLevel = data.buffer17;
		
		$("#cust_name_value").text(custName);
		$("#cust_level_value").text(custLevel);
		$("#product_name_value").text(productName);
		$("#product_level_value").text(productLevel);
	}
	
//	var fileName = gridSearch.getColIndexById("r_v_filename")==undefined ? 	"" : gridSearch.cells(id,gridSearch.getColIndexById("r_v_filename")).getValue();
	var fileName = data.callKeyAp;
	$("#now_playing_file_name > p").text(fileName);
	
//	$("#rowId").val(id);
	
	var callId1 = callKey;
	gridCallKey = callId1;
	
	if (from == "gridSearch") {
		listenPopupGrid = createGrid("listen_popup_grid","","listenPopupGridNotLogin","?callId1="+callId1,"",20,[],[]);
		listenPopupGrid.forEachRow(function(idx){
		var ttime = listenPopupGrid.cells(idx,listenPopupGrid.getColIndexById("r_call_ttime")).getValue()
		if(ttime == 0){
			var id = listenPopupGrid.getRowId(idx);
			listenPopupGrid.setColspan(listenPopupGrid.getRowId(idx),3,5);
			listenPopupGrid.setCellTextStyle(listenPopupGrid.getRowId(idx),idx,"color:red")
		}
		
	    })
	} else {
		listenPopupGrid.clearAndLoad(contextPath+"/listenPopupGrid.xml?callId1="+callId1);
	}

	listenPopupGrid.attachEvent("onRowSelect", function(id,ind){
		var ttime = 0;
		var isMergedFile = listenPopupGrid.cells(id,listenPopupGrid.getColIndexById("merge")).getValue();
		
		// 녹취 병합이 아닐경우 (스크립트 이력 클릭했을경우)
		if (isMergedFile != "N") {
			var cnt = listenPopupGrid.getRowIndex(listenPopupGrid.getSelectedRowId())+1;
			listenPopupGrid.forEachRow(function(idx){
				if(cnt==1)
				    cnt=0;
				
				console.log('idx : '+idx);
				console.log('cnt : '+cnt);
				console.log('idx < cnt : '+(idx<cnt));
				console.log("ttime : "+ttime)
	            console.log("idx Time : "+Number(listenPopupGrid.cells(idx,listenPopupGrid.getColIndexById("r_call_ttime")).getValue()));
			    console.log('===================================')
				if (listenPopupGrid.cells(idx,listenPopupGrid.getColIndexById("r_call_ttime")).getValue() != undefined && idx < cnt) {
					if(listenPopupGrid.cells(id,6).getValue() == 0){
						ttime += (Number(listenPopupGrid.cells(idx,listenPopupGrid.getColIndexById("r_call_ttime")).getValue())+2);
					}else{
						ttime += (Number(listenPopupGrid.cells(idx,listenPopupGrid.getColIndexById("r_call_ttime")).getValue()));
					}
				}
			});
		}
		top.dual_rc.setTime(ttime);
	});
	listenPopupGrid.attachEvent("onBeforeSelect", function(id,ind){
//		if(listenPopupGrid.cells(id,6).getValue() ==''){
//			return false;
//		}else{
//			return true;
//		}
		if(treeArr != null){
			var flag = true;
			for (var i = 0; i < treeArr.length; i++) {
				if(treeArr[i] == listenPopupGrid.cells(id,2).getValue()){
					return !flag;
				}else{
					flag = true;
				}
			}
			return flag;
		}
			
	});
	checkSoundDevice(gridSearch,'');
}

function getGridValue(grid, id, columnId) {
	return grid.getColIndexById(columnId)==undefined?   "" : grid.cells(id,grid.getColIndexById(columnId)).getValue();
}

function test(){
	var targetPath = webPath+ "/faceRecording/face_recording_view?callKey=1814001580081";
	window.open(targetPath,"_blank","resizable=yes, toolbar=no,location=no, width=1300, height=700,scrollbars=yes ,left=50,top=50'");
}

function retryRec(rowId) {
	$("#approvalNumberValue").val("");
	layer_popup("#approvalNumberPopup");
}


function scriptPdfDown(){
	window.open(contextPath+"/faceRecording/product/"+callKey+"/script/call?fileType=pdf&recType=Y"); 
}