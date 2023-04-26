//전역변수 설정
var gridApproveManage; // 그리드

var encNCheck = false;				//	ENC 여부 체크 하고 확인 함수 기본 false  true 면 패스 하여 다운로드 진행
var encYnReal = true;				//	암호화 여부 체크

addLoadEvent(approveManageLoad);

function approveManageLoad() {
	gridApproveManage = approveManageGridLoad();
	formFunction()
	authyLoad();
	top.nowRc.pause();
	top.playerFrame.$(".main_btn_filedownload").hide();
	top.playerFrame.$(".play_list_menu,.play_list_popup,.main_btn_section,.main_btn_section_end,.main_btn_time,.main_btn_selector,.main_btn_delete").hide();
	top.playerVisible(true);
}

//권한 불러 오기
function authyLoad() {
	if(approveYn != 'Y' && reciptYn != 'Y'&&preReciptYn != 'Y') {
		$('#approveBtn').remove();
		$('#rejectBtn').remove();
	}

}
//청취 / 다운로드 요청 관리
function approveManageGridLoad() {
	objGrid = new dhtmlXGridObject("gridApproveManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingApproveManage", true);
	objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/approve_list.xml?header=true", function(){

		var search_toolbar = objGrid.aToolBar;

		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+" "+objGrid.getRowsNum() +'</div>')
		search_toolbar.setWidth("total",150)
		search_toolbar.addButton("fileDownload",8, "파일 다운로드", "icon_download.png", "icon_download.png");
//		search_toolbar.addButton("playerToggle",14, "플레이어 변경", "icon_btn_change_gray.png", "icon_btn_change_gray.png");
		$(window).resize();
		objGrid.setSizes();

		search_toolbar.attachEvent("onClick", function(name){
			if(downloadYn=='Y'){
			}else{
				if(objGrid.cells(objGrid.getSelectedId(),6).getValue()!='down'){
					alert(lang.admin.alert.approveManage1)
					return false;
				}else if(objGrid.cells(objGrid.getSelectedId(),8).getValue()!='6'){
					alert(lang.admin.alert.approveManage2)
					return false;
				}else if(objGrid.cells(objGrid.getSelectedId(),3).getValue()!=userInfoJson.userId){
					alert(lang.admin.alert.approveManage3)
					return false;
				}else if(dueDateCal(objGrid,objGrid.getSelectedId(),25,28)){
					alert(lang.admin.alert.approveManage4)
					return false;
				}
			}

			 switch(name) {
				case "fileDownload":
					var id = objGrid.getSelectedRowId();
					var result = checkApprove(id,"down");

					top.$("#encAgentName").val(gridApproveManage.cells(gridApproveManage.getSelectedRowId(),4).getValue());
        			top.$("#encExt").val(gridApproveManage.cells(gridApproveManage.getSelectedRowId(),12).getValue());
        			top.$("#encFileName").val(gridApproveManage.cells(gridApproveManage.getSelectedRowId(),9).getValue());

					top.$("#closeEncYn").unbind("click");
					top.$("#downloadEncYn").unbind("click");
					if(encYn == "N" && encNCheck == false){
						top.layer_popup("#recEncPopup");

						top.$(".enc_ui_hide").show();
						top.ui_controller();

						top.$("#closeEncYn").click(function(event){
							top.layer_popup_close("#recEncPopup");
						})

						top.$("#downloadEncYn").click(function(event){
							if(top.$("#approveUserGroup").val() == "encY"){
								encNCheck = true;
								encYnReal = true;
								fileDown(id,objGrid);
							}else{
								encNCheck = true;
								encYnReal = false;
								fileDown(id,objGrid);
							}
						})
					}else{
						fileDown(id,objGrid);
					}
					encNCheck = false;
					encYnReal = true;
				break;
				case "playerToggle":
					top.playerToggle();
					break;
			 }
		});

		$(window).resize(function() {
			objGrid.setSizes();
			$(top.window).trigger("resize");
		});
	}, 'xml');


	objGrid.attachEvent("onXLS", function(){
		progress.on()
	});

	objGrid.attachEvent("onXLE", function(grid_obj,count){

		$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");

		if (objGrid.getRowsNum() > 0){
			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>'
			objGrid.aToolBar.setItemText("total", setResult)
		}

		ui_controller();
		progress.off();

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
			$(top.window).trigger("resize");
		});

		$("#reqsDate, #reqeDate").change(function(){fromTo($("#reqsDate"),$("#reqeDate"),this)})
		$("#reqsDate, #reqeDate").keyup(function(){if($("#reqsDate").val().replace(/[:-]/g,'').length==8&&$("#reqeDate").val().replace(/[:-]/g,'').length==8) fromTo($("#reqsDate"),$("#reqeDate"),this)})

	});

	objGrid.attachEvent("onRowSelect", function(id,ind){
		return;
	});

	// 더블 클릭시 파일 재생
	objGrid.attachEvent("onRowDblClicked", function(id,ind){
		if(approveYn == 'Y' ||reciptYn == 'Y'){
			checkSoundDevice(this,id)
			//play(this,id)
		}else if(objGrid.cells(id,6).getValue()!='listen'){
			alert(lang.admin.alert.approveManage5)
		}else if(objGrid.cells(id,8).getValue()!='6'){
			alert(lang.admin.alert.approveManage6)
		}else if(dueDateCal(objGrid,id,25,28)){
			alert(lang.admin.alert.approveManage7)
		}else if(objGrid.cells(id,3).getValue()!=userInfoJson.userId&&preReciptYn=="N"){
			alert(lang.admin.alert.approveManage8)
		}else{
			checkSoundDevice(this,id)
			//play(this,id)
		}
	});

	objGrid.attachEvent("onMouseOver", function(id, ind){
		switch (ind){
			case 15:
				if(false/*maskingYn == "Y"*/){
					var oriData = this.getUserData(id,"r_cust_name");
					if (oriData != null && oriData != undefined && oriData != ""){
						this.cells(id,ind).cell.title = oriData;
					}
					else{
						this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
					}
					break;
				}
			case 16:
				if(false/*maskingYn == "Y"*/){
					var oriData = this.getUserData(id,"r_cust_name");
					if (oriData != null && oriData != undefined && oriData != ""){
						this.cells(id,ind).cell.title = oriData;
					}
					else{
						this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
					}
					break;
				}
			default:
				this.cells(id,ind).cell.title = this.cells(id,ind).getTitle();
				break;
		}

	});
	ui_controller();
	return objGrid;
}
function formFunction() {

	// 날짜 셋팅
//	$('#reqsDate, #reqeDate').datepicker().datepicker("setDate", new Date());
	datepickerSetting(locale,'#reqsDate, #reqeDate');
	// 조회 버튼 연동
	$("#searchBtn").click(function(){
		if($("#reqsDate").val().length != 10 || $("#reqeDate").val().length != 10)
			alert(lang.admin.alert.approveManage8 +"!\n예)2018-01-01");
		else
			gridApproveManage.clearAndLoad(contextPath+"/approve_list.xml" + formToSerialize());
	});

	// 엔터 처리
	$('.main_form').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});

	// 조회 콤보 로드
	searchSelectOptionLoad($("#approveReason"),"/selectOption.do?comboType=common&comboType2=approveReason&ALL=");
	searchSelectOptionLoad($("#approveType"),"/selectOption.do?comboType=common&comboType2=approveType&ALL=");
	searchSelectOptionLoad($("#approveState"),"/selectOption.do?comboType=common&comboType2=approveState&ALL=");

	// 승인 버튼
	$("#approveBtn").click(function(){
		approveProc("accept")
	});

	// 반려 버튼
	$("#rejectBtn").click(function(){
		approveProc("reject")
	});

	// 삭제 버튼
	$("#deleteBtn").click(function(){
		approveProc("delete")
	});

}

function approveProc(command){
	if (command == "accept" || command == "reject"){
		var id = gridApproveManage.getSelectedRowId();
		var grid = gridApproveManage;
		var dataStr = {
				"reqDate"			: grid.cells(id,1).getValue()
			,	"reqTime"			: grid.cells(id,2).getValue()
			,	"userId"			: grid.cells(id,3).getValue()
			,	"approveType"		: grid.cells(id,6).getValue()
			,	"approveReason"		: grid.cells(id,7).getValue()
			,	"fileName"			: grid.cells(id,9).getValue()
			,	"recDate"			: grid.cells(id,10).getValue()
			,	"recTime"			: grid.cells(id,11).getValue()
			,	"recExt"			: grid.cells(id,12).getValue()
			,	"type"				: command
		}

		var state = grid.cells(id,8).getValue();

		if(state == "3" || state == "5"  || state == "6"  || state == "7"){
			alert(lang.admin.alert.approveManage9)
			return;
		}

		if (command == "accept"){
			if (state == "0"){
				if (preReciptYn != "Y"){
					alert(lang.admin.alert.approveManage10 + "\n" + admin.alert.approveManage11);
					return false;
				}
				dataStr["approveState"] = "2"

			}else if (state == "1"){
				if (reciptYn != "Y"){
					alert(admin.alert.approveManage12 + "\n" + admin.alert.approveManage13)
					return false;
				}
				dataStr["approveState"] = "4"

			}else if (state == "2"){
				if (reciptYn != "Y"){
					alert(admin.alert.approveManage14 + "\n" + admin.alert.approveManage13)
					return false;
				}
				dataStr["approveState"] = "4"

			}else if (state == "4"){
				if (approveYn != "Y"){
					alert(admin.alert.approveManage16 + "\n" + admin.alert.approveManage17)
					return false;
				}
				dataStr["approveState"] = "6"

			}
		}else if (command == "reject"){
				if (state == "0"){
					if (preReciptYn != "Y"){
						alert(lang.admin.alert.approveManage10 + "\n" + admin.alert.approveManage11)
						return false;
					}
					dataStr["approveState"] = "3"
			}else if (state == "1"){
				if (reciptYn != "Y"){
					alert(admin.alert.approveManage12 + "\n" + admin.alert.approveManage13)
					return false;
				}
				dataStr["approveState"] = "5"
			}else if (state == "2"){
				if (reciptYn != "Y"){
					alert(admin.alert.approveManage14 + "\n" + admin.alert.approveManage13)
					return false;
				}
				dataStr["approveState"] = "5"
			}else if (state == "4"){
				if (approveYn != "Y"){
					alert(admin.alert.approveManage16 + "\n" + admin.alert.approveManage17)
					return false;
				}
				dataStr["approveState"] = "7"
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
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					alert(lang.admin.alert.approveManage18)
					$("#searchBtn").trigger("click");
				}
			}
		});
	/*}else if (command == "delete"){
		var grid = gridApproveManage;
		var chCol = -1;
		for(var i = 0; i < grid.getColumnsNum()-1; i++) {
			if(grid.getColType(i) == "ch") {
				chCol = i;
				break;
			}
		}

		if(chCol != -1 && grid.getCheckedRows(chCol) != "" ) {
			var checked = grid.getCheckedRows(chCol).split(",");
			for( var i = 0 ; i < checked.length ;i++ ) {
				dataStr = {
						"reqDate"			: grid.cells(checked[i],1).getValue()
					,	"reqTime"			: grid.cells(checked[i],2).getValue()
					,	"userId"			: grid.cells(checked[i],3).getValue()
					,	"approveType"		: grid.cells(checked[i],6).getValue()
					,	"approveReason"		: grid.cells(checked[i],7).getValue()
					,	"fileName"			: grid.cells(checked[i],9).getValue()
					,	"recDate"			: grid.cells(checked[i],10).getValue()
					,	"recTime"			: grid.cells(checked[i],11).getValue()
					,	"recExt"			: grid.cells(checked[i],12).getValue()
					,	"approveState"		: grid.cells(checked[i],13).getValue()
					,	"type"				: command
				}

				$.ajax({
					url:contextPath+"/approve_proc.do",
					data:dataStr,
					type:"POST",
					dataType:"json",
					async: false,
					cache: false,
					success:function(jRes){
						// DB에 조회한 계정이 있으면
						if(jRes.success == "Y") {}
					}
				});
			}
			alert("정상적으로 처리 되었습니다.")
			$("#searchBtn").trigger("click");
		}else{
			alert("삭제하실 요청을 먼저 체크 해 주세요!")
		}
	}*/
}
}
//폼 돌면서 시리얼라이즈 해주기
function formToSerialize(){

	$(".inputFilter").trigger('blur');
	$(".inputFilter").trigger('keyup');

	var resultValue = "";

	$('.main_form').children().each(function (i){
		var id = this.id;
		var value = ($(this).val()) ? $(this).val().trim() : "";
		resultValue += (resultValue.length > 0?"&":"?") + id+"="+value;
	});
	return encodeURI(resultValue);
}

/**셀렉트 옵션 불러오기
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : comboType2 => 콤보 기본값 추가 해주기 (default로 값 셋팅 해주면 됨.)
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 *
 * */
function searchSelectOptionLoad(objSelect, loadUrl){

	$.ajax({
		url:contextPath+loadUrl,
		data:{},
		type:"POST",
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
			alert(admin.alert.approveManage19 + "\n" + admin.alert.approveManage20)
        });

		taudio.addEventListener('loadeddata',function(){
			try {
				taudio.currentTime=0.1
				taudio.pause();
				play(gridObj, id);
				top.playerFrame.$(".main_btn_filedownload").hide();
				top.playerFrame.$(".play_list_menu,.play_list_popup,.main_btn_section,.main_btn_section_end,.main_btn_time,.main_btn_selector,.main_btn_delete").hide();
				return true;
			} catch(e){
				taudio.pause();
				alert(admin.alert.approveManage19 + "\n" + admin.alert.approveManage20)
			}
		});
	}catch(e){
		taudio.pause();
		alert(admin.alert.approveManage19 + "\n" + admin.alert.approveManage20)
	}
}

function play(gridObj, id){
	//checkSoundDevice();
	var listenUrl;
	var recDate 		=gridObj.cells(id,10).getValue();
	var recTime		=	 gridObj.cells(id,11).getValue();
	var recExt			= gridObj.cells(id,12).getValue();
	var recCustPhone	= gridObj.cells(id,16).getValue();
	var recUserName 	= gridObj.cells(id,14).getValue();
	var recvFileName    = gridObj.cells(id,9).getValue();
	var recMemo 		= "";

	var dataStr = {
			"recDate"		: recDate	  			// 녹취일
		,	"recTime"		: recTime				// 녹취시간
		,	"recExt"		: recExt				// 내선버노
	}

	$.ajax({
		url:contextPath+"/getListenUrl.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success=="Y"){
				var ip=jRes.resData.ListenUrl[0].vRecIp;
				var file=jRes.resData.ListenUrl[0].vRecFullpath;
				listenUrl='http://'+ip+':28881/listen?url='+file;
			}else {
				alert(lang.approveList.alert.msg13 /* "요청에 실패 하였습니다." */);
			}
		}
	});

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
	}

	// 녹취 주소 복호화
	listenLog(recFileData)
	rc = top.nowRc;
	rc.recFileData = recFileData;
    rc.setFile("audio", listenUrl, undefined, true);
	rc.listenUrl =listenUrl
}


function listenLog(recFileData,justPath){
	var url = recFileData.listenUrl

	$.ajax({
		url:contextPath+"/listenLog.do",
		data:recFileData,
		type:"POST",
		dataType:"json",
		async: true,
		cache: false,
		success:function(jRes){}
	});

	return url;
}

function fileDown(id,objgrid){
	progress.on();
	fileName = objgrid.cells(id,objgrid.getColumnsNum() -3).getValue();

	var nowTime = new Date();
	var requestUrl = parseUri(fileName).authority;

	var url = "http://"+requestUrl+"/down?"+parseUri(fileName).query+"&cmd=down&encYn="+encYnReal+"&time"+nowTime.getTime();
	$("#download").attr("action", url);
	$("#download").submit();
	top.layer_popup_close("#recEncPopup");
	progress.off();
}

function dueDateCal(objGrid, id,approveDateColumn, dueDateColumn) {
	var date= objGrid.cells(id,approveDateColumn).getValue().split("-");
	var currentDate = new Date();
	var approveDate= new Date(date[0],Number(date[1])-1,date[2])
	var dueDate = Number(objGrid.cells(id,dueDateColumn).getValue().split(" ")[0]);
	if((currentDate-approveDate)>dueDate*24*60*60*1000)
		return 1
	else
		return 0
}


function checkApprove(id,type){

	var approveValue = "none";

	var dataStr = {
			"fileName"		: (gridApproveManage.getColIndexById("r_v_filename")==undefined?	"" : gridApproveManage.cells(id,gridApproveManage.getColIndexById("r_v_filename")).getValue())
		,	"recExt"		: (gridApproveManage.getColIndexById("r_ext_num")==undefined ? 	"" : gridApproveManage.cells(id,gridApproveManage.getColIndexById("r_ext_num")).getValue())
		,	"recDate" 		: (gridApproveManage.getColIndexById("r_rec_date")==undefined ? 	"" : gridApproveManage.cells(id,gridApproveManage.getColIndexById("r_rec_date")).getValue())
		,	"recTime"		: (gridApproveManage.getColIndexById("r_rec_time")==undefined ? 	"" : gridApproveManage.cells(id,gridApproveManage.getColIndexById("r_rec_time")).getValue())
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
				alert(lang.admin.alert.approveManage21);
			}
		}
	});

	return approveValue;
}