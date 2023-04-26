var retryKeyArr = new Array();
var scriptList = new Map();
var scriptId = "";
var wavesurfer;
var scriptJsonArray = new Object();
var sendScriptSocketClient;
var isRestartFlag; 
var webSocket;
var productInfoScriptGrid;
var drag_base_item = false, drag_user_item = false, drag_base_list = false, drag_user_list = false, drag_copy_list = false;
var drag_item_mode = null, drag_list_mode = null;
var scriptCallKey; // 스크립트콜키
var playListArr = [];
var playCount=0;
var playListCount = 0; // 파일명 여러개잇을경우 카운트 변수
var faceScriptKey;
var arrayFlag = false;
var timer // 타임아웃 변수
var flag = false;
var callKeyArr = [];
var timeFlag = false;
var audioTag;
var interval;	// 타이머 인터벌
var recordTimerFlag2 = new Array(false,false);	// 녹취버튼 Flag
var minutes = 00;
var seconds = 00;
var hours = 00;
var params; // 파라미터 객체
var recResult = false; // 녹취 시작 종료 결과 (true | false)
var settimeout; // settimeout 클리어 하기위한 객체
var recstart=false; // 녹취 상태 판단플래그
var retryType="N";// 재녹취 유형판단위함. Y면 사후재녹취
var type;
var playIdx = 0;
var recordingFinish = false;
var rProductListPk;
var rectry= false;
var endRecTimeOut;
var rectryFlag= false;
var rectryFlag2 = false;
var productName;
var Ttype = null;
var manualType = false;
// /=======================
// RECTRY_OM => 한달이 지난 녹취이력일때 파라미터
// /=======================
var textListIdx = 0;
var preRecState;
var selectScriptStepDetailFlag = true;
var allRecParamJson = null;
var allRecFlag = false;
var moreProductStepArr = null;
var groupInfo = null;
var taResultPopUpFlag = false;			//TA 최종결과창이 뜰지 말지 정하는  플레그

// 프로그레스 처리
var progress = {
	on: function(mask) {
		if($("#progress").length < 1) {
			$("html").append("<div id='progress'>")
		}
		$("#progress").css("opacity", "0");
		$("#progress").css("height", "100%");
		HoldOn.open({
				theme: "sk-circle"
			,	backgroundColor: "#FFFFFF"
		});
	}, off: function(mask) {
		HoldOn.close();
		$("#progress").remove();
	}
}


window.onload = function() {
	
	checkSound();
	
	console.log(params);
	
	$('.nextPlayGBtn').click(function() {
		$('.nextPlayGBtn').css('display','none');
		clearTimeout(settimeout);
	})
	$('.clientDown').click(function() {
		window.open("http://aiprecsys.woorifg.com:8080/rsupdate/fdownload?file=RecSee_Voice_Client_v2.3.exe");
	})
	$('.nextPlaySBtn').click(function() {
		$('.nextPlaySBtn').css('display','none');
		clearTimeout(settimeout);
	})
	
	$(".chkIcon").change(function(){
        if($(".chkIcon").is(":checked")){
            $("#"+scriptId+" .chkImg").prop('checked',true);
        }else{
            $("#"+scriptId+" .chkImg").prop('checked',false);
        }
    });
	
	// 녹취시작
	$(document).on("click",".rec_startBtn",function() {
		$("#rec_script_box").scrollTop(0); 
		createCallKey();
		console.log(scriptCallKey);
		console.log("녹취시작");
		// 01 - 24 장진호 timout 수정
		progress.on();
		recStartWS();
		
// recStartFunction();
// scriptListPlay()
// scriptCallKeyUpdate();
		
	});
	
	$(".rec_endBtn").on("click",function(){
		console.log("녹취종료");
		
		console.log(scriptCallKey);
		if(!confirm('녹취를 종료하시겠습니까?\n확인버튼 클릭시 해당 구간 녹취내역은 저장되지 않습니다.\n이어서 녹취 진행을 원하는 경우 취소 후 다음버튼 클릭 바랍니다. ')){
			return false;
		}
		progress.on();
		stopEvent(null,'n');
	});
	
	// 오디오태그 속도조절
	$("input[name='speed']").unbind("change").bind("change",function(){
		var audio = document.getElementById("audioPlayer");
		var rate = $("input[name='speed']:checked").val();
		audio.playbackRate = rate;
	});
	
	$("#audioPlayer").on("ended",function(){
		nextplay()
	})
	
	webSocketConnect();
	SttWebSocketConnect();
	
	// 글자크기 키우기
	$(document).on("click",".script_text_big",function() {
		var size = $("#rec_script_box").css("font-size") // 폰트사이즈 가져오기
		size= parseInt(size,10); // 폰트 사이즈만 가져오기
		console.log(size)
		
		if((size + 2) <=30){
			 $("#rec_script_box").css("font-size", "+=2");
		}
	});
	
	
	$(document).on("click",".nextPlayGBtn",function() {
		$('.nextPlayGBtn').css('display','none');
		clearTimeout(settimeout);
	});
	$(document).on("click",".nextPlaySBtn",function() {
		$('.nextPlaySBtn').css('display','none');
		clearTimeout(settimeout);
	});
	
	// 글자크기 줄이기
	$(document).on("click",".script_text_small",function() {
		var size = $("#rec_script_box").css("font-size") // 폰트사이즈 가져오기
		size= parseInt(size,10); // 폰트 사이즈만 가져오기
		console.log(size)
		
// getsize();
		if((size - 2) >= 12){
			 $("#rec_script_box").css("font-size", "-=2");
		}
	});
	
	// 모달 팝업
	$('.modal-close').on('click',function(){
		$('#taResultLastPop').fadeOut(500);
		$('#taResultPop').fadeOut(500);
		$('#taErrorResultPop').fadeOut(500);
	})
		
	$('#taPopupCheckBtn').on('click',function(){
		$('#taResultLastPop').fadeOut(500);
	})
	// 결과창닫기
	$('.taResultPopClose').on('click',function(){
		$('#taResultPop').fadeOut(500);
		$('#taErrorResultPop').fadeOut(500);
	})
}

function stopEvent(d,errorYn) {
	$('.Tcontent').hide();
	$('.nextPlayEndBtn').css("display","none");
	$('.nextPlaySBtn').css('display',"none");
	$('.nextPlayGBtn').css('display',"none");
	$('.nextPlayG').css('display',"none");
	$(".rec_startBtn").removeClass("displayHidden");
	$(".rec_endBtn").addClass("displayHidden");
	$(".start_wave").addClass("displayHidden");
	$(".first_wave").removeClass("displayHidden");
	$(".playWaitTime").removeClass("disableddiv");
	$(".wave_btn").addClass("displayHidden");
	var audio = document.getElementById("audioPlayer");
	audio.pause();
	clearTimeout(timer);
	try{
		clearTimeout(settimeout);
	}catch(e){
		console.log(e)
	}
	
		if(preRecState =='Y' || rectryFlag){
			if(rectryFlag2)
				recStartBtnDisable()
				
			if( finalCheck() ){
				recStartBtnDisable()
			}
			productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),7).setValue("Y");
			productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),2).setValue('<div id="state" style=\"cursor: pointer; position: relative; top: 2px\">녹취완료</div>');
			var id = productInfoScriptGrid.getSelectedRowId()
			if(taResultStepArr.length > 0){
				taResultStepArr.forEach(i => {
					if(i.gridId == id ){
						if(i.result == 'N'){
							var imgSrc = "<div class='taErrorResultBtn' id='"+id+"' style='position: relative; top: 2px\' onclick='taErrorPopUp(this)'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleYellow.png' style='width:16px;' /></div>"
							productInfoScriptGrid.cells(id,4).setValue(imgSrc)
						}
						if(i.result == 'Y'){
							var imgSrc = "<div class='taErrorResultBtn' id='"+id+"' style='position: relative; top: 2px\' onclick='taErrorPopUp(this)'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleBlue.png' style='width:16px;' /></div>"
							productInfoScriptGrid.cells(id,4).setValue(imgSrc)
						}
					}
				})
				
			}
			rectryFlag =false;
		}else{
			productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),7).setValue("N");
			productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),2).setValue('<div id="state" style=\"cursor: pointer; position: relative; top: 2px\"><img id src="../../../recsee3p/resources/common/recsee/images/project/icon/loading.png"/></div>');
		}
	
	recstart=false; 
	if(d == undefined || d == null){
		sendmsg("recStop", scriptCallKey)
	}else if(d == '1'){
		setTimeout(() => {
			sendmsg("recStop", scriptCallKey)
		}, 2000);
	}
	
	//20220504 장진호 추가
	if(errorYn =='e'){
		recStopResFlag = 2;
		NotResErrorRecStopEvent();
	}
	//20220504 장진호 추가 (녹취종료버튼 클릭시 10초 타임아웃체크)
	if(errorYn =='n'){
		console.log("녹취종료 버튼을 클릭하여 stop전문을 날렷으며 , 10초 타임아웃을 생성한다.")
		recStopResFlag = 3;
		NotResErrorRecStopEvent();
	}
	
	if(SttTaUseFlag){
		stopBtnFinalSend();
	}
	
	recStopBtnFlag = true;
	stopTimer()
	resetTimer()
	waveDisplayOff()
//	clearInterval(interval);
//	console.log('stopEvent() : clearTimeout(micExcpectedTimer)')
//	console.log('stopEvent() : clearTimeout(enginExpectedTimer)')
	clearTimeout(micExcpectedTimer);
	clearTimeout(loginAndRecStartTimeout);		// 녹취시작버튼클릭시 타임아웃 클리어
//	clearTimeout(enginExpectedTimer);
}

function seconds2time (seconds, type) {
	var hours = 0;
    var time = "", chkTime = true;

	if ( type === undefined ) type = true;
	if ( type ) hours   = Math.floor(seconds / 3600);

	seconds = Math.floor( seconds, 0 );
	var minutes = Math.floor((seconds - (hours * 3600)) / 60);
    var seconds = seconds - (hours * 3600) - (minutes * 60);

	if ( type )
	{
		if (hours != 0) time = (hours < 10) ? "0"+hours+":" : hours+":";
		else time = "00:";

		if ( time !== "" ) chkTime = true;
		else chkTime = false;
	}

    if (minutes != 0 || chkTime )
	{
		minutes = (minutes < 10 && chkTime) ? "0"+minutes : String(minutes);
		time += minutes+":";
    } else {
		time += "00:";
	}

    if (time === "") time = seconds;
    else time += (seconds < 10) ? "0"+seconds : String(seconds);

    return time;
}


// ////////////////////////////////////////////////////////////////
// function scriptNextAction(){
// // var customerChk = $(".chkIcon").prop("checked");
// // if(!customerChk){
// // alert("고객님께 설명해 드리고 이해 여부를 확인해주세요.")
// // return false;
// // }
// $(".rec_startBtn").addClass("displayHidden");
// $(".rec_endBtn").removeClass("displayHidden");
// $(".wave_btn").addClass("displayHidden");
//
// createCallKey();
// sendmsg("recStart", scriptCallKey)
// //doSend('{"head":"RECSEE","code":"50","systemid":"CTR","tenantid":"WRBK","trxid":"'+scriptCallKey+'","repoid":"CR","branchnumber":"1","devicenumber":"1","ipaddress":"1","etcparamlength":"13","etcparam":{"apkey":"1814001580081"}}')
// scriptCallKeyUpdate();
// scriptListPlay();
// }

function btnClickPopup(urlCode){
	console.log(urlCode)
	switch(urlCode){
	
	case "001" :
		
		break;
	
	}
}


function volumn(num){
	rc.playBackRate(num);
	
}

function init(){
// if(scriptType != "T"){
// // $(".rec_user_info").css("display","block");
// }
// window.open(contextPath+"/faceRecording/face_recording_view");
	layer_popup('#guideImgPopup');
	
	window.onbeforeunload = function() {
		if (!recordingFinish){
			//
		}else{
			// ;
		}
	}
	
	if(params.PRD_CD.charAt(0)=='T'){
		if(params.PRD_CD == 'T0000000000001'){
			params.BIZ_DIS='2';
			params.SYS_DIS='TOP'
			params["AGNPE_NM"] = '없음';
			if(params.RUSE_YN == 'Y'){
				params.PRD_CD = 'T0000000000002';
			}
		}
		if(params.PRD_CD == 'T0000000000003'){
			params["UP_INVEST_YN"] = 'Y'; 
			// params["CUS_ANSWER"] = '12:1,2,3,1,2,3,1,2,3,1,2,3';
			params.BIZ_DIS='2';
			params.SYS_DIS='TOP'
				params["AGNPE_NM"] = '없음';
			if(params.RUSE_YN == 'Y'){
				params.PRD_CD = 'T0000000000004';
			}
			
			upgradeTProductCodeCheck();
			
		}
		
		if(params.PRD_CD == 'T2014001580081'){
			params.BIZ_DIS='4';
			params.SYS_DIS='BK2';
		}
		  $("head").append('<link rel="stylesheet" type="text/css" href="' + recseeResourcePath + '/css/page/faceRecorder/faceRecorder_view2.css">');
	}
	
	SttTaUseFlag = closeAboutParams(params);
	// 재녹취창 오픈시 파라미터 받고 재녹취 flag 형성
	if(params.retryType != null && params.retryType.trim.length > 0){
		rectryFlag = true;
		rectryFlag2 = true;
	}
	
	if(params.REG_AM != null){
		var arr = params.REG_AM.split("|");
		if(arr.length > 1){
			var buf = ""
			for(var i=0;i<arr.length; i++){
				arr[i] = Number(arr[i]).toLocaleString();
			}
			for (var i = 0; i < arr.length; i++) {
				if(i == arr.length-1){
					buf+=arr[i];
					break;
				}
				buf+=arr[i]+"|";
			}
			params.REG_AM = buf;
		}else{
			try{		
				var regAm = Number(params.REG_AM).toLocaleString();
				if(regAm != 'NaN'){
					params["REG_AM"] = regAm;
				}
			}catch(e){
				console.log("가격에 한글이 들어감")
			}
		}
	}
	
	productInfoScriptGrid = createGrid("productInfoScriptGrid","recMemoAddButton","productInfoScriptGrid","?header=true","",20,[],[]);


	productScriptFk(params.PRD_CD)
	if(params.retryType != undefined){ // != undefined
		retryType = params.retryType;
		$('#clearAllRec').html('전체 재녹취'); 		// 사후재녹취시 전체초기화 -> 전체 재녹취 변경
	}else{
		retryType = 'N';
	}
	addBasicTaParam();
	
	productInfoScriptGrid.setImageSize(1,1);
	productInfoScriptGrid.enableAutoWidth(true)
	

	// 오디오태그 전역변수 설정
	audioTag = document.getElementById("audioPlayer");

	ui_controller();
	gridFunction();
}

function selectOptionLoad(objSelect, loadUrl){

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

function gridFunction(){
	productInfoScriptGrid.attachEvent("onRowDblClicked", function(id,ind){
		if($('.rec_startBtn').css('display')=='block'){ 
			if( productInfoScriptGrid.cells(id,3).getValue() !='' ){
				$("#script_name").html("[ "+productInfoScriptGrid.cells(id,0).getValue() + " ]");
				faceScriptLoad(productInfoScriptGrid.cells(id,1).getValue(),"N");
			} else 
				return false;
			
		}else{
			return false;
		}
		
	});
	
	productInfoScriptGrid.attachEvent("onRowClicked", function(id,ind){
		if(productInfoScriptGrid.cells(id,3).getValue()==''){
			return false
		}
	});
	
	productInfoScriptGrid.attachEvent("onBeforeSelect", function(id,ind){
		if(recstart || productInfoScriptGrid.cells(id,3).getValue() ==''){
			return false;
		}else{
			return true;
		}
	});
	
	$("#retryRecReason").change(function(){
		var retryRecReasonCd = $("#retryRecReason").val();
		if (retryRecReasonCd == "0") {
			$(".retryRecReasonDetailClass").show();
		} else {
			$(".retryRecReasonDetailClass").hide();
		}
	});
	$("#allRetryRecReason").change(function(){
		var retryRecReasonCd = $("#allRetryRecReason").val();
		if (retryRecReasonCd == "3") {
			$(".allRetryRecReasonDetailClass").show();
		} else {
			$(".allRetryRecReasonDetailClass").hide();
		}
	});

	$("#retryRecConfirmReason").click(function(){
		saveRetryRecReason();
	});
	$("#allRetryRecConfirmReason").click(function(){
		saveAllRetryRecReason();
	});
	
	$("#allRetryRecConfirmReasonExit").click(function(){
		layer_popup_close("#allRetryRecReasonPopup");
	});
	
	selectOptionLoad($("#retryRecReason"),"/selectOption.do?comboType=common&comboType2=retryRecReason&ALL=not&selectedValue=1");
}

function createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){

	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setPagingSkin("toolbar","dhx_web");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
// objGrid.enableDragAndDrop(true)
	objGrid.setSkin("dhx_web");
	
	objGrid.init();

	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){
		objGrid.attachEvent("onXLS", function(){
			progress.on();
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			var code = params.PRD_CD;
			if (type == "T" && retryType != "Y" && code !='T0000000000003' && code != 'T0000000000004') {
				// 투자성향분석이면서 신규녹취일경우 재녹취 버튼 숨김, 녹취 초기화 버튼 살림
				objGrid.setColumnHidden(objGrid.getColIndexById("retryRec"), true);
				$("#clearAllRec").css("display","block");
			} else {
				objGrid.setColumnHidden(objGrid.getColIndexById("retryRec"), false);
				$("#clearAllRec").css("display","block");
			}
			ui_controller();
			// freeRec();
			objGrid.expandAll()
			var deathFlag = scriptRecFileChecked('first');
			

			var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
			for(var i = 0; i< gridIds.length; i++){
				productInfoScriptGrid.setItemCloseable(gridIds[i],false)
			}
//			progress.off();
		});
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
			/*
			 * dhtmlx.alert({ type:"alert", title:"알림", ok:"확인",
			 * text:lang.admin.alert.searchManage1 });
			 */
		}
	}
	return fDrag;
}

// 콜키 만드는 함수
// 날짜와 상담원계정??뭐 상담원뭐랑 합쳐서 콜키 만듬
// TODO
// 년월일시분초+조작자사번
// 2021 11 22 장진호 SSS추가
function createCallKey(){
	scriptCallKey = new Date().getTime()+params.OPR_NO;
	console.log("키생성완료 : "+scriptCallKey + "    data :"+new Date());
	
}
function createCallKeyMerge(){
	scriptCallKey = new Date().getTime()+params.OPR_NO+"_merge";
	console.log("merge키생성완료 : "+scriptCallKey + "    data :"+new Date());
}

function productScriptFk(productCode){
	var flag = false;
	var dataStrFirst = {
			"params" :JSON.stringify(params),
			"callKey" : callKey
	};
	
	$.ajax({
		url:contextPath+'/faceRecInsertParameter.do',
		type:"POST",
		dataType:"json",
		data:dataStrFirst,
		async: false,
		success:function(jRes){
			console.log("params 저장완료");
		}
	});
	
	
	var selectUrl = "/faceRecSelectProductList.do";
	
	if(params.RCD_DSCD_NO != null || params.RCD_DSCD_NO != undefined){
		if(Number(params.RCD_DSCD_NO) == 2){
			console.log("해당 상품은 권유녹취");
			selectUrl = "/faceRecSelectOfferProduct.do";
		}
		
	}
	if(params["PRD_NM"] != undefined){
		if(params["PRD_NM"].indexOf('ELS-파생형') > -1){
			params["ELF_YN"] = 'Y';
		}
	}
	
	
	if(params.PRD_CD.length==0 || params.PRD_CD == null || params.PRD_CD==undefined){
		alert("자동리딩방식이 불가하오니, 투자설명서 및 상품설명서를 참고하여 판매과정 녹취하여 주시기 바랍니다.\n[스크립트 조회: 포탈>투자상품 녹취시스템>스크립트 관리(검색)]")
		window.close();
		return false;
	}
	if(params.PRD_CD.split("|").length>1){
		console.log("다계좌상품")
		params["MORE_PRODUCT"] = 'Y'
	}else{
		console.log("다계좌상품 X")
		params["MORE_PRODUCT"] = 'N'
	}
	
	if(params.AGNPE_NM == undefined){
		params["AGNPE_NM"] = '없음';
	}
	
	// 강제로 BIZ_DIS 바꿔야함
	if(params["BIZ_DIS"] == 2){
		if(params["SYS_DIS"] =='WMS' || params["SYS_DIS"] =='DPT' || params["SYS_DIS"] =='TOP'){
			if(params.PRD_CD.charAt(0)!='T'){
				params["SYS_DIS"] = 'WMS';
			}
		}
	}
	if(params["BIZ_DIS"] == 4){
		var text = $('.checkNumberBox').html() + '(방카 재녹취는 방카시스템에서만 가능)';
		$('.checkNumberBox').html(text);
		
	}
	if(params["COM_REPT_TYPE"] != null){
		if(params["COM_REPT_TYPE"].trim().length == 0){
			params["COM_REPT_TYPE"] = 0;
		}
	}
	
	var dataStr = {
			"searchWord" : productCode,
			"callKey" : callKey,
			"params" :JSON.stringify(params)
	}
	console.log(dataStr);
	$.ajax({
		url:contextPath+selectUrl,
		type:"POST",
		dataType:"json",
		data:dataStr,
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var eltData = jRes.resData.eltProduct;
				if(eltData != null && eltData != undefined){
					params["eltProduct"] = eltData;
				}
				var result = jRes.resData.selectProductList;
				if(result != null){
					console.log(jRes.resData);
					rProductListPk = result[0].rProductListPk;
					
					if(jRes.resData.productInfo != null && jRes.resData.productInfo != undefined) groupPkSet(jRes.resData.productInfo);
					
					if(result[0].rGroupPk > 0){
						groupInfo = {
										"groupYn" :"Y",
										"groupPk" : leadingZeros(result[0].rGroupPk,8)
									}
					}else{
						groupInfo = {
								"groupYn" :"N",
								"groupPk" : leadingZeros(rProductListPk,8)
						}
					}
					
					faceRecodeGridLoad(rProductListPk);
					faceScriptKey = result[0].rProductListPk
					$('#user_name2').html((params.CUS_NM!=undefined && params.CUS_NM.trim()!='') ? params.CUS_NM : '' );// 고객명
					$('#user_step2').html((params.CSINC_GD_NM!=undefined && params.CSINC_GD_NM.trim()!='') ? params.CSINC_GD_NM : '' );// 고객성향
					$('#prize_name2').html(result[0].rProductName);// 상품명
					productName = result[0].rProductName;
					if(params["PRD_RISK_GD_NM"] != null){
						MakePrizeStep2();
						flag = true;
					}
					if(jRes.resData.riskInfo != null && jRes.resData != undefined){
						params["PRD_RISK_GD"] = jRes.resData.riskInfo.productRiskNumer;
						params["PRD_RISK_GD_NM"] = jRes.resData.riskInfo.productRiskName;
						if(!flag){
							MakePrizeStep2();
						}
					}
					
					if(params.PRD_CD.charAt(0)=="T"){
						type = 'T';			
						Ttype='T';
					}
					
					if(jRes.resData.trFlag != null && jRes.resData.trFlag != undefined){
						params["trFlag"] = jRes.resData.trFlag;
					}
					
					if(jRes.resData.eltProduct != null && jRes.resData.eltProduct != undefined){
						params["eltProduct"] = jRes.resData.eltProduct;
					}
				}
				sendmsg("pdfdown");
				
			}else{
//				manualRecBoxShow();
				console.log("select ProductList Load Fail");
				console.log(jRes);
				console.log(jRes.resData.msg);
				
				// 여기다 수동녹취 띄우는 Ajax만들기
				
				$.ajax({
					url:contextPath+"/faceManualRecordingProductSelect.do",
					type:"POST",
					dataType:"json",
					data:dataStr,
					async: false,
					success:function(jRes){
						if(jRes.success == "Y") {
							var result = jRes.resData.selectProductList;
							if(result != null){
								manualType = true;
								rProductListPk = result[0].rProductListPk;
								faceRecodeGridLoad(rProductListPk);
								faceScriptKey = result[0].rProductListPk
								$('#user_name2').html((params.CUS_NM!=undefined && params.CUS_NM.trim()!='') ? params.CUS_NM : '' );// 고객명
								$('#user_step2').html((params.CSINC_GD_NM!=undefined && params.CSINC_GD_NM.trim()!='') ? params.CSINC_GD_NM : '' );// 고객성향
								if(params.retryType != undefined && params.retryType =='Y'){
									$('#prize_name2').html(params["PRD_NM"]);// 상품명
								}else{
									if(result[0].rProductName != null){
										params["PRD_NM"] = result[0].rProductName;
										productName = result[0].rProductName;
										$('#prize_name2').html(result[0].rProductName);// 상품명
									}
								}
								if(jRes.resData.riskInfo != null && jRes.resData != undefined){
									$('#prize_step2').html(jRes.resData.riskInfo.productRiskName);// 상품성향
									params["PRD_RISK_GD"] = jRes.resData.riskInfo.productRiskNumer;
									params["PRD_RISK_GD_NM"] = jRes.resData.riskInfo.productRiskName;
								}
								if(params.PRD_CD.charAt(0)=="T"){
									type = 'T';						
								}
								
								//수동녹취시 TA 소켓 해제
								errorSttAction();
							}
						}
					}
				});
				
			}
		}
	});
	grepRecParamHistoryRecTryJson();
	
}
// prizeStep2 텍스트 삽입
function MakePrizeStep2() {
	var gdNm = null;
	if(params["MORE_PRODUCT"] == 'Y'){
		gdNm = MakeMoreProductPrdRiskNm();
	}else{
		gdNm = params["PRD_RISK_GD_NM"];
	}
	var gdNmArr = gdNm.split('/');
	prizeStepCssMove(gdNmArr);
	$('#prize_step2').html(gdNm);
}
// 다계좌 위험등급상세 조합
function MakeMoreProductPrdRiskNm() {
	while(params["PRD_RISK_GD_NM"].includes('|')){
		params["PRD_RISK_GD_NM"] = params["PRD_RISK_GD_NM"].replace('|',' / ');
    };
	return params["PRD_RISK_GD_NM"];
}
// 위험등급상세칸 CSS변경
function prizeStepCssMove(gdNmArr) {
	var size = gdNmArr.length;
	switch (size) {
	case  3 :
	case  4 :
		$('#prize_step2').css('font-size','12px');
		break;
	case  5 :
		$('#prize_step2').css('font-size','11px');
		break;
	case  6 :
		$('#prize_step2').css('font-size','10px')
		break;
	default:
		$('#prize_step2').css('font-size','13px');
		break;
	}
	
}
	
	

function faceRecodeGridLoad(stepKey){
	console.log("stepKey :" + stepKey);
	
	productInfoScriptGrid.clearAndLoad(contextPath + "/productInfoScriptGrid.xml" + "?rs_script_step_fk="+stepKey+"&callKey="+ callKey+"&moreProduct="+params.MORE_PRODUCT+"&bizDis="+params.BIZ_DIS+'&sysDis='+params.SYS_DIS+"&sttTa="+SttTaUseFlag, function(){
		if(productInfoScriptGrid != null && productInfoScriptGrid != undefined){
			// 다계좌 스탭세팅
			checkEnableMoreProductStep();
		}
		if(taResultStepArr.length == 0){
			var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
			for (var i = 0; i < gridIds.length; i++) {
				if(productInfoScriptGrid.cells(gridIds[i],6).getValue() != null && productInfoScriptGrid.cells(gridIds[i],6).getValue() != undefined){
					if(productInfoScriptGrid.cells(gridIds[i],6).getValue().trim().length > 0 ){
						var taR = productInfoScriptGrid.cells(gridIds[i],9).getValue();
						var reason;
						try{
							reason = JSON.parse(productInfoScriptGrid.cells(gridIds[i],productInfoScriptGrid.getColIndexById("rTaReason")).getValue());
							if(taR != 'R'){
								if(reason == null || reason == undefined){
									var data = {
											"gridId":  gridIds[i],
											"result" : taR
									}
									taResultStepArr.push(data);
								}else{
									var data = {
											"gridId":  gridIds[i],
											"result" : taR,
											"taDetail" : reason
									}
									taResultStepArr.push(data);
								}
							}
						}catch (e) {
							console.log(e);
							if(taR != 'R'){
								var data = {
										"gridId":  gridIds[i],
										"result" : taR 
								}
								taResultStepArr.push(data);
							}
						}
					}
				}
			}
		}
	});
	
	
}

function faceScriptLoad(scriptKey,type){
	if(params["TR_AC_YN"] == 'N'){	
	    var morProductNextStepFlag = moreProductStepCheckToNextPlay()
	}
	var flag = false;
	playListArr = [];	// 배열초기화
	var dataStr = {
			"rScriptStepFk" : scriptKey,
			"callKey" : callKey,
			"ifCase" : params.PSN_YN,
			"type" : type,
			"params" :JSON.stringify(params),
			"productCode" : params.PRD_CD,
			"moreProductFlag" : morProductNextStepFlag
		}
	$.ajax({
		url:contextPath+"/selectScriptDetailHistoryList.do",
		type:"POST",
		dataType:"json",
		data:dataStr,
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var btnText = "다음";
				var btnEndText = "전체녹취종료";

				
				flag =true;
				textListIdx = 0;
				$("#rec_script_box").html("")
				var result = jRes.resData.scriptDetailHistoryList;
				
				// Ta파라미터 스탭구간셋팅
				addTaParamScriptStep(result);
				
				var htmlString = "";
				console.log(jRes);
				for(var i = 0; i< result.length; i++) {
					var textIdx = i;
					var html = '';
					if(result[i].rScriptDetailType =='T'){
						htmlString = '';
                        var text = result[i].rScriptDetailText.replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi,"}</span>"); 
                        var attr = JSON.parse(result[i].rScriptStepDetailCaseAttributes);
						if(attr == null){
							htmlString += "<div class='tellerText'>";
						}else{
							if(attr.isTextRed == null){
								htmlString += "<div class='tellerText'>";
							}else{
								htmlString += "<div class='tellerText' style ='color:red;'>";
							}
						}
                        htmlString +=  forTextList(result[i].rScriptDetailTextList,textIdx);  
                    	if(type=='T' && i==(result.length-1)){
                    		htmlString += "<button class='nextPlayGBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextplay()'>"+btnText+"</button>"                        		
                    	}else if(i==(result.length-1)){
                    		htmlString += "<button class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</button>"                        		                    		
                    	}
                    	htmlString +=  "</div>"
                		htmlString += "<button id='moreProductEndBtn' style='padding: 5px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;cursor: pointer;' onclick='faceRecEnd()'>"+btnText+"</button>"                        		                    		
                        $("#rec_script_box").append(htmlString);
						 
					}else if(result[i].rScriptDetailType=='G'){
						html = '';
                        htmlString = ''
                        htmlString += "<div class='customerText'>";
                        htmlString += forTextList(result[i].rScriptDetailTextList,result[i].rScriptDetailType); 
                         if(Ttype=='T'){
                        	if(i==(result.length-1)){
                              	htmlString += "<button class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</button>"
                        	}else{
                                htmlString += "<button class='nextPlayG' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextplay()'>"+btnText+"</button>"   	
                        	}
                        	htmlString+= "<div class='Tcontent' id='Tcontent"+(i+1)+"' style='display:inline;float: right; color:red; font-weight:bold;font-size: small;'>고객 답변이 끝나면&nbsp;&nbsp;</div>";
                        }else{
                        	if(i==(result.length-1)){
                            	htmlString += "<button class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</button>"
                        	}else{
                        		htmlString += "<button class='nextPlayG' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextplay()'>"+btnText+"</button>"   	
                        	}
                        }

                        htmlString += "<div>";
                        // 보내기
                        $("#rec_script_box").append(htmlString);
					}else if(result[i].rScriptDetailType=='S'){
						htmlString = ''
						 var attr = JSON.parse(result[i].rScriptStepDetailCaseAttributes);
						if(attr == null){
							htmlString += "<div class='AdviserText'>";
						}else{
							if(attr.isTextRed == null){
								htmlString += "<div class='AdviserText'>";
							}else{
								htmlString += "<div class='AdviserText' style ='color:red;'>";
							}
						}
// if(manualType && params.MORE_PRODUCT =='Y'){
// if(attr.isMoreProductText){
// var arr = new Array();
// arr.push(createMoreProductManualText());
// htmlString += forTextList(arr,result[i].rScriptDetailType);
// }else{
// htmlString +=
// forTextList(result[i].rScriptDetailTextList,result[i].rScriptDetailType);
// }
// }else{
						htmlString += forTextList(result[i].rScriptDetailTextList,result[i].rScriptDetailType);
// }

                        if(Ttype=='T'){
                        	if(i==(result.length-1)){
                              	htmlString += "<button class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</button>"
                        	}else{
                                htmlString += "<button class='nextPlaySBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextplay()'>"+btnText+"</button>"                        		
                        	}
                        	htmlString+= "<div class='Tcontent' id='Tcontent"+(i+1)+"' style='display:inline;float: right; color:red; font-weight:bold;font-size: small;'>고객 답변이 끝나면&nbsp;&nbsp;</div>";
                        }else{
                        	if(i==(result.length-1)){
                            	htmlString += "<button class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</button>"
                        	}else{
                        		htmlString += "<button class='nextPlaySBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextplay()'>"+btnText+"</button>"                        		
                        	}
                        }
                         
                        
                        htmlString += "<div>";
                        $("#rec_script_box").append(htmlString);
					}else if(result[i].rScriptDetailType=='R'){
						htmlString = ''
	                        htmlString += "<div class='AdviserText'>";
	                        htmlString += forTextList(result[i].rScriptDetailTextList,result[i].rScriptDetailType); 
	                        
	                        if(Ttype=='T'){
	                     
	                        	if(i==(result.length-1)){
	                        		htmlString += "<div class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</div>"
	                        	}else{
	                        		htmlString += "<div class='nextPlayGBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextplay()'>"+btnText+"</div>"                        		
	                        	}
	                        	htmlString+= "<div class='Tcontent' id='Tcontent"+(i+1)+"' style='display:inline;float: right; color:red; font-weight:bold;font-size: small;'>고객 답변이 끝나면&nbsp;&nbsp;</div>";
	                        }else{
	                        	if(i==(result.length-1)){
	                        		htmlString += "<button class='nextPlayEndBtn' id='playBtn"+(i+1)+"' style='padding: 0px 10px; background-color: #0067ac; color: #ffffff; display:none; float:right;' onclick='nextPlayEnd()'>"+btnText+"</button>"
	                        	}
	                        }
	                        htmlString += "<div>";
	                        $("#rec_script_box").append(htmlString);
					}else if(result[i].rScriptDetailType=='Z'){
						html = '';
                        htmlString = ''
                        htmlString += "<div class='customerText'>";
                        htmlString += forTextList(result[i].rScriptDetailTextList,textIdx); 
                        htmlString += "<div>";
                        // 보내기
                        $("#rec_script_box").append(htmlString);	
                    }
					
					
// playListArr.push(result[i].rScriptTtsServerIp+":28881/listen?url="+result[i].rScriptFilePath+"|"+result[i].rScriptDetailType);
					
					// 그리드만 더블클릭햇을때 음성파일 배열 담지않게하기
					if(type != "N"){
						if(result[i].rScriptFilePath != null){
							if(result[i].rScriptFilePath.indexOf(",") > -1){
								var t = result[i].rScriptFilePath.split(",");
								for (var k=0;k<t.length;k++){
									if(result[i].rScriptDetailType == "T"){
										playListArr.push(result[i].rScriptTtsServerIp+":28881/listen?url=|"+t[k]+"|"+result[i].rScriptDetailType);
									}else if(result[i].rScriptDetailType == "R"){
										playListArr.push(result[i].rScriptTtsServerIp+":28881/listen?url=|"+t[k]+"|"+result[i].rScriptDetailType);
									}else{
										playListArr.push("||"+result[i].rScriptDetailType);
									}
								}
							}else{
								if(result[i].rScriptDetailType == "T"){
									playListArr.push(result[i].rScriptTtsServerIp+":28881/listen?url=|"+result[i].rScriptFilePath+"|"+result[i].rScriptDetailType);
								}else if(result[i].rScriptDetailType == "R"){
									playListArr.push(result[i].rScriptTtsServerIp+":28881/listen?url=|"+result[i].rScriptFilePath+"|"+result[i].rScriptDetailType);
								}else if(result[i].rScriptDetailType == "Z"){
									playListArr.push(result[i].rScriptTtsServerIp+":28881/listen?url=|"+result[i].rScriptFilePath+"|"+result[i].rScriptDetailType);									
								}else{
									playListArr.push("||"+result[i].rScriptDetailType);
								}
							}
							
						}else{
							if(result[i].rScriptDetailType == "S"){
								playListArr.push("||"+result[i].rScriptDetailType);
							}else if(result[i].rScriptDetailType == "G"){
								playListArr.push("||"+result[i].rScriptDetailType);
							}
// else if(result[i].rScriptDetailType == "Z"){
// playListArr.push("||"+result[i].rScriptDetailType);
// }
						}
						
						
						
					}
					
				}
				/**
				 * 적용은되나 재녹취시 recState까지 변경이 안되기때문에 음..... 재녹취한부분에도 종료가뜸
				 */
				 
				$('.Tcontent').hide();
				var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
				var endCheck = 0;
				for(var i =0; i<gridIds.length; i++){
					 var recState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
					 if(recState == 'N'){
						 endCheck++;
					 }
				}
				if(endCheck == 1){
					$('.nextPlayEndBtn').html(btnEndText);
					$('.nextPlayEndBtn').next().remove();
				}
			}else{
				flag = false;
				if(selectScriptStepDetailFlag){
					selectScriptStepDetailFlag = false;
					var resultFlag = faceScriptLoad(scriptKey,type);
					if(!resultFlag){
						alert("에러");
						stopTimer()
						resetTimer()
						recStartBtnDisable()
						stopEvent();
					}else{
						flag = true;
					}
				}
			}
		}
	
	});
	return flag;
}

var sttRecStartResultTimeoutArr = [];

function faceRecEnd(flag){
	setTimeout(() => {
	if(flag != undefined || flag != null){
		if(flag == 'empty'){
			
		}else{
			var result = moreProductStepCheckToNextPlay();
			if(!result){
				$('#moreProductEndBtn').show();
				return;
			}
		}
	}
	console.log("재생끝남..")
	playIdx =0;

	var gridId = productInfoScriptGrid.getSelectedId();
	var gridKey = productInfoScriptGrid.cells(gridId,1).getValue();
	var dataStr = {
			"rScriptStepPk" : gridKey,
			"scriptCallKey" : callKey,
			"rScriptRecState" : "Y",
			"scriptStepCallKey"	  : scriptCallKey
	}
	$.ajax({
		url:contextPath+"/updateScriptStepHistory.do",
		type:"POST",
		dataType:"json",
		data:dataStr,
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
// faceRecodeGridLoad(faceScriptKey);
				//20220504 장진호
//				productInfoScriptGrid.cells(gridId,7).setValue("Y");
//				productInfoScriptGrid.cells(gridId,2).setValue('<div id="state" style=\"cursor: pointer; position: relative; top: 2px\">녹취완료</div>');
//				productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),6).setValue(scriptCallKey);
//				console.log("그리드에 키저장완료 : "+scriptCallKey + "시간 : "+new Date());
				// doSend('{"head":"RECSEE","code":"51","systemid":"CTR","tenantid":"WRBK","trxid":"'+scriptCallKey+'","branchnumber":"","devicenumber":""}')
				sendmsg("recStop", scriptCallKey)
				//녹취종료플레그
				recstart=false;
				console.log("녹취중지 전문 보냄 : "+new Date());
				
				
				$(".rec_startBtn").removeClass("displayHidden");
				$(".rec_endBtn").addClass("displayHidden");
				$(".wave_btn").addClass("displayHidden");
				rectryFlag = false;
				stopTimer()
				resetTimer()
				waveDisplayOff()
				 // $(".next_rec").css("display","block");
				 // $(".next_rec").prop("disabled",false)
				 
				 
	 			//20220504 장진호
				progress.on();
				recStopResFlag = 1;
				NotResRecStopEvent();
//				scriptRecFileChecked();
				
			}
		}
	});
			}, 500);
}


function scriptListPlay(){
	try{
		playIdx=0;
		recordTimerOn()
		waveDisplayOn()
	}catch(e){
		
	}
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	playCount = 0;
	var rate = $("input[name='speed']:checked").val();
	for(var i = 0; i< gridIds.length; i++){
		if(i== gridIds.length-1)
			$('.nextPlayEndBtn').attr('onclick','faceRecEnd()')
		var recState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
		if(recState == "N"){
// productInfoScriptGrid.cells(gridIds[i],).getValue()
			productInfoScriptGrid.selectRowById(gridIds[i]);
			
			recstart=true;
			
			$("#script_name").html("[ "+productInfoScriptGrid.cells(gridIds[i],0).getValue() +" ]");
			var playState  = faceScriptLoad(productInfoScriptGrid.cells(gridIds[i],1).getValue());
			
			if(!playState){
				alert("녹취중지")
				return;
			}
			
			
			var listLen = playListArr.length
			var audio = document.getElementById("audioPlayer");
			preRecState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
			
			productInfoScriptGrid.cells(gridIds[i],2).setValue('<div id="state" style=\"cursor: pointer; position: relative; top: 2px\"><img id src="../../../recsee3p/resources/common/recsee/images/project/icon/listen.png"/></div>');
			
			
			var waitTime = $("input[name='speed2']:checked").val();
			var count = 1;
			if(playListArr.length == 0){
				setTimeout(function(){
					if($('.rec_startBtn').css('display')!='block')
						nextplay();
					playCount++;
				},2000);
			}
			
			if(playListArr[playCount].split("|")[2] != "T"){
				for(var j = 0; j<playListArr.length; j++){
					var t = playListArr[j].split("|")[2];
					if(t == "G" || t == "S"){
						if(j==0){
							count--;
						}
						count++;
						playCount++;
					}else{
						break;
					}
				}
				if(type=='T'){
					if(playCount == playListArr.length){
						$('#Tcontent'+(count)).show();
						$('.nextPlayEndBtn').css('display','block');
						$('.nextPlayEndBtn').attr('onclick','faceRecEnd()');
					}else{
						setTimeout(function(){
							audio.src = HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1];
							audio.playbackRate = rate;
							if($('.rec_startBtn').css('display')!='block')
								audio.play();
							playCount++;
							
						},5*count*1000);			
					}
				}else{				
					if(playCount == playListArr.length){
						audio.playbackRate = rate;
						
						$('.nextPlayEndBtn').css('display','block');
					}else{
						audio.playbackRate = rate;
						$('#playBtn'+(count)).css('display','block');
					}
				}
			}else{
				audio.src = HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1];
				$('span[readnumber="'+playCount+'"]').addClass('readingTextBack')
				audio.playbackRate = rate;
				if($('.rec_startBtn').css('display')!='block')
					audio.play();
				playCount++;
			}
			
			
			break;
		}
	}
	
}

// 그리드 청취하기
function recFilePlay(target){
	var playurl = ''
	if($('.rec_startBtn').css('display')!='block'){
		return false;
	}
		
	if(target=='all'){
		playurl = window.location.origin+"/recseePlayer?SEQNO="+callKey
	}else{
		var recCallKey = $(target).parent().next().next().next()[0].innerText;
		var recState = $(target).parent().next().next().next().next()[0].innerText;
		console.log(recCallKey)
		if(recState=='N'){
			errroPopUpdateText('「녹취완료」상태인 경우만 청취 가능합니다.','','','')
			return false
		}else if(recCallKey==''){
			errroPopUpdateText('해당구간 녹취 저장에 실패하였습니다.','해당구간을 재녹취해 주세요.','「녹취완료」상태인 경우만 청취 가능합니다.','');
			return false
		}
		playurl = window.location.origin+"/recseePlayer?callKey="+recCallKey
	}
	
	if($('.rec_confirm').eq(1).css('display')!='none')
		endAlert('first');
	window.open(playurl,'player','width=520,height=560 resizeable=no, scrollbars=no, menubar=no, toolbar=no, status=no')
	
}


// 재녹취
function retryRecFile(rowId){
	if(recstart)
		return false;
	
	if(productInfoScriptGrid.cells(rowId,7).getValue()=='N')
		return false;
	
	 if(params.RECTRY_OM == 'Y'){
		 errroPopUpdateText('최종 녹취일로부터 한달이 지난 건은','전체 녹취만 가능합니다.','「녹취 초기화」를 눌러주세요.','');
	 return false;
	 }
	
	if(confirm("선택 구간 재녹취를 하시겠습니까?")){
		var originalKey = scriptCallKey;
		recordingFinish =false;
		createCallKey();
		 if(retryType == "Y"){
			 $("#rowId").val(rowId);
			 $("#retryRecReason").val("2");
			 $("#retryRecReason").trigger("change");
			 $("#retryRecReasonDetail").val("");
			 top.layer_popup("#retryRecReasonPopup")
			 top.ui_controller();
		 }else{
			 retryKeyArr.push(originalKey)
			 rerectry(rowId);
		 }
	}
}

function saveRetryRecReason() {
	var rowId = $("#rowId").val();
	
	var retryRecReason = $("#retryRecReason").val();
	if (retryRecReason == undefined) {
		alert("재녹취 사유 타입을 선택해주세요.");
		return false;
	}
	var retryRecReasonDetail = $("#retryRecReasonDetail").val();
	if (retryRecReason == '0' &&retryRecReasonDetail == undefined) {
		alert("재녹취 사유를 입력해주세요.");
		return false;
	} 
	if(retryRecReason == '0' && (retryRecReasonDetail.length < 3 || retryRecReasonDetail.length > 30)){
		alert("재녹취사유는 3~30자 까지 입력이 가능합니다.");
		return false;
	}
	$(".rec_confirm").css("display","none");
	$(".rec_startBtn").attr("disabled", false);
	
	productInfoScriptGrid.selectRowById(rowId)
	
	var gridKey = productInfoScriptGrid.cells(rowId,1).getValue();

	
	// 1. 업데이트 성공 시 해당 콜키로 재녹취 사유 insert
	$.ajax({
		url : contextPath + "/insertRetryRecReason.do",
	 	type : "POST",
	 	data : {
	 			"userId" : params.OPR_NO
	 		,	"callKeyAp" : scriptCallKey // 유니크 키
 		 	,	"retryReason" : retryRecReason
 		 	,	"retryReasonDetail" : retryRecReasonDetail
	 	},
	 	dataType:"json",
	 	async: true,
	 	success:function(jRes){
	 		if(jRes.success == "Y"){
	 			$('.rec_save').show();
		 		layer_popup_close("#retryRecReasonPopup");
		 		
		 	// 2. rs_script_step_history 테이블에 pk 조건으로 callKeyAp 업데이트
		 		rerectry(rowId)
// reScriptPlay();
	 			
	 		}else{
	 			errroPopUpdateText('재녹취 사유 저장에 실패하였습니다.','다시 재녹취를 시도해 주세요','','');
	 		}
	 	}
	})
}

function saveAllRetryRecReason() {
	
	var retryRecReason = $("#allRetryRecReason").val();
	if (retryRecReason == undefined) {
		alert("재녹취 사유 타입을 선택해주세요.");
		return false;
	}
	var retryRecReasonDetail = "";
	
	if(retryRecReason == '3'){
		retryRecReasonDetail = "전체 재녹취 - 기타사유 : " +$("#allRetryRecReasonDetail").val();
	}
	if(retryRecReason == '4'){
		retryRecReasonDetail = "전체 재녹취 - 녹취환경 미흡";
	}
	if(retryRecReason == '5'){
		retryRecReasonDetail = "전체 재녹취 - 녹취점검 지적";
	}
	
	if (retryRecReason == '3' &&retryRecReasonDetail == undefined) {
		alert("재녹취 사유를 입력해주세요.");
		return false;
	} 
	if(retryRecReason == '3' && (retryRecReasonDetail.length < 3 || retryRecReasonDetail.length > 30)){
		alert("재녹취사유는 3~30자 까지 입력이 가능합니다.");
		return false;
	}
	
	
	
	if(confirm("※주의! 해당 버튼은 전체 녹취 초기화 버튼입니다.\n전체 녹취를 초기화 하시겠습니까?")) {
		recordingFinish =false;
		dataStr = {
			"rScriptRecState" 	: "N",
			"scriptCallKey"	  	: callKey,
			"clearRecYn"		: "Y" ,// 녹취 초기화 여부
			"reason"			: retryRecReason
		}
		
		
		var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
		productInfoScriptGrid.selectRowById(gridIds[0]);
		var scriptCallKeyArr = new Array();
		for(var i = 0; i< gridIds.length; i++){
			var callKeyAp = productInfoScriptGrid.cells(gridIds[i],6).getValue()
			if(callKeyAp.length!=0){
				scriptCallKeyArr.push(callKeyAp);
			}
		}
		

		
		var arrText="";
		for(var i = 0; i< scriptCallKeyArr.length; i++){
			if(i==scriptCallKeyArr.length-1){
				arrText+= scriptCallKeyArr[i];
				break;
			}
			arrText+= scriptCallKeyArr[i]+","
		}
			
		allRecParamJson = {
				"userId" : params.OPR_NO
				,"retryReason" : retryRecReason
				,"retryReasonDetail" : retryRecReasonDetail
				,"callKey" : callKey
			};
		
		$.ajax({
			url:contextPath+"/updateScriptStepHistory.do",
			type:"POST",
			dataType:"json",
			data:dataStr,
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					errroPopUpdateText('전체 녹취가 초기화되었습니다.','처음부터 다시 녹취해주세요.','','');
					$(".rec_startBtn").removeClass("displayHidden");
					$(".rec_startBtn").css("pointer-events","auto")
                    $(".rec_startBtn").css("opacity","1")
                    clearTimeout(endRecTimeOut); 
					$(".rec_confirm").css("display","none");
					rectryFlag = false;
					rectryFlag2 = false;
					allRecFlag = true;
					//TA결과 초기화
					taResultStepArr = new Array();
					clearRecStartInterVal();
					clearTaErrorTimeOut();
					arrRecStartTimeoutClear()
					
				} else {
					errroPopUpdateText('전체 녹취 초기화에 실패하였습니다.','다시 시도해주세요.','','');
				}
			},
			complete: function() {
				productInfoScriptGrid.clearAndLoad(contextPath+"/productInfoScriptGrid.xml?rs_script_step_fk="+rProductListPk+"&callKey="+ callKey+"&bizDis="+params.BIZ_DIS+"&sttTa="+SttTaUseFlag, function() {});
				$('.rowselected .cellselected').dblclick();
				layer_popup_close("#allRetryRecReasonPopup");
				progress.off()
			}
		});
		
		$.ajax({
			url:contextPath+"/addAllRectryParamHistory.do",
			type:"POST",
			dataType:"json",
			data:allRecParamJson,
			async: false,
			success:function(jRes){
				console.log("[addAllRectryParamHistory.do] action success");
			}
		});
		/*
		 * // 1. 업데이트 성공 시 해당 콜키로 재녹취 사유 insert $.ajax({ url : contextPath +
		 * "/insertAllRetryRecReason.do", type : "POST", data : jsonParam,
		 * dataType:"json", async: false, success:function(jRes){
		 * if(jRes.success == "Y"){ $('.rec_save').show();
		 * productInfoScriptGrid.clearAndLoad(contextPath+"/productInfoScriptGrid.xml?rs_script_step_fk="+rProductListPk+"&callKey="+
		 * callKey+"&bizDis="+params.BIZ_DIS, function() {});
		 * layer_popup_close("#allRetryRecReasonPopup"); $('.rowselected
		 * .cellselected').dblclick(); }else{ alert("재녹취 사유 저장에 실패하였습니다. 다시 재녹취를
		 * 시도해 주세요"); } } })
		 */
	}else{
		return false;
	}
		
	$(".rec_confirm").css("display","none");
	$(".rec_startBtn").attr("disabled", false);

}

	// 재녹취 시도
function rerectry(rowId){
	rectry = false;
	$(".rec_confirm").hide();
	$(".rec_startBtn").attr("disabled", false)
	$(".rec_startBtn").css("pointer-events","auto")
	$(".rec_startBtn").css("opacity","1")
	var imgSrc = "<div id='recPlay' style='position: relative; top: 2px\'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleGray.png' style='width:16px;' /></div>"
	productInfoScriptGrid.cells(rowId,7).setValue("N");
	productInfoScriptGrid.cells(rowId,2).setValue('<div id="state" style=\"cursor: pointer; position: relative; top: 2px\"><img id src="../../../recsee3p/resources/common/recsee/images/project/icon/loading.png"/></div>');
	$("#script_name").html("[ "+productInfoScriptGrid.cells(rowId,0).getValue() +" ]");

	//TA 녹취상황일때만 이미지 셋팅
	if(SttTaUseFlag){
		productInfoScriptGrid.cells(rowId,4).setValue(imgSrc)
	}

	
	progress.on();
	// 재녹취시 alert timeout 클리어
	clearTimeout(endRecTimeOut);
	// 녹취시작
	recStartWS();
	rectry = true;
	rectryFlag=true;
}


function scriptCallKeyUpdate(del){
// createCallKey();
	var gridKey = productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),1).getValue();
	
	var dataStr = {
			"rScriptStepPk" : gridKey,
			"scriptCallKey" : callKey,
			"scriptStepCallKey"	  : scriptCallKey
	}
	if(del=='del'){
		dataStr.scriptCallKey='';
	}
		
	$.ajax({
		url:contextPath+"/updateScriptStepHistory.do",
		type:"POST",
		dataType:"json",
		data:dataStr,
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
// faceRecodeGridLoad(faceScriptKey);
				productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),6).setValue(scriptCallKey);
// productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),3).setValue("<div
// id='recPlay' onClick='recFilePlay('"+scriptCallKey+"')' style=\"cursor:
// pointer; position: relative; top: 2px\"><img id
// src='../../../recsee3p/resources/common/recsee/images/project/icon/authy_icon/icon_authy_agent_gray.png'/></div>");
// productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),3).setValue("<div
// id='recPlay' onClick='recFilePlay("+item.getrSccriptStepCallKey()+")'
// style=\"cursor: pointer; position: relative; top: 2px\"><img id
// src='"+request.getContextPath()+"/resources/common/recsee/images/project/icon/authy_icon/icon_authy_agent_gray.png'
// /></div>");
			}
		}
	});
}

function recStartFunction(){
	$(".playWaitTime").addClass("disableddiv");
	$(".rec_startBtn").addClass("displayHidden");
	$(".rec_endBtn").removeClass("displayHidden");
	$(".wave_btn").removeClass("displayHidden");
	// $(".next_rec").prop("disabled",true);
	// $(".next_rec").css("display","none");
	playCount = 0; // 플레이카운트 초기화
	playListCount = 0; // 플레이카운트 초기화
}

function deathScriptChecked(){
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	var recState = productInfoScriptGrid.cells(gridIds[0],7).getValue();
	if(recState == ""){
		productInfoScriptGrid.selectRowById(gridIds[1]);
		$('.rowselected .cellselected').dblclick();
	}
}

// 녹취파일 긑낫기때문에 녹취 완료여부 체크
function scriptRecFileChecked(first){
	if(first != 'first'){
		var gridId = productInfoScriptGrid.getSelectedId();
		productInfoScriptGrid.cells(gridId,7).setValue("Y");
		productInfoScriptGrid.cells(gridId,2).setValue('<div id="state" style=\"cursor: pointer; position: relative; top: 2px\">녹취완료</div>');
		productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),6).setValue(scriptCallKey);
		console.log("그리드에 키저장완료 : "+scriptCallKey + "시간 : "+new Date());
	}
	
	
	var flag = true;
	var deathProduct = false;
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	for(var i = 0; i< gridIds.length; i++){
		var recState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
		if(recState == "N"){
			flag = false;
			if(first){
				productInfoScriptGrid.selectRowById(gridIds[i]);
				$('.rowselected .cellselected').dblclick();
			}
			break;
		}
		
		if (i+1 ==gridIds.length){
			if(productInfoScriptGrid.cells(gridIds[0],7).getValue() == ''){
				productInfoScriptGrid.selectRowById(gridIds[1]);
			}else{
				if(params["BIZ_DIS"] == '4'){
					productInfoScriptGrid.selectRowById(gridIds[gridIds.length-1]);
				}else{
					productInfoScriptGrid.selectRowById(gridIds[0]);
				}
			}
			$('.rowselected .cellselected').dblclick();
		}
	}
	
	if(flag){
		$(".rec_confirm").css("display","block");
		// $(".next_rec").css("display","none");
		$(".rec_startBtn").attr("disabled", true)
		$(".rec_startBtn").css("pointer-events","none")
		$(".rec_startBtn").css("opacity","0.4")
		
		if (first) {
			// 처음 페이지 접속 시 암것도 안함
		} else {
		// if (retryType == "Y") {
				// 재녹취 - 팝업
		// alert('모든 단계의 녹취가 종료되었습니다. 우측 하단의 녹취 완료 버튼을 눌러 녹취파일을 저장해 주세요.');
		// } else {
				// 신규녹취 - 자동저장
				scriptRecFileMerge();
		// }
		}
	}else{
		// TODO
		if (!first) {
			nextStepRec()	
			$(".rec_confirm").css("display","none");
		}
	}
	return deathProduct;
}

// 녹취 병합 요청
function scriptRecFileMerge(){
    
    if(recstart){
    	errroPopUpdateText('녹취 중에는 「녹취 저장」 불가합니다.','녹취 종료 후 다시 시도해주세요.','','')
        return false;
    }
    
    var flag = true;
    callKeyArr = [];
    var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
    for(var i = 0; i< gridIds.length; i++){
        var recState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
        if(recState == "N"){
            flag = false;
            errroPopUpdateText(productInfoScriptGrid.cells(gridIds[i],0).getValue()+' 단계 녹취 미완료 되었습니다. 해당 부분을 녹취해 주세요.','','','');
            break;
        }else{
            if(productInfoScriptGrid.cells(gridIds[i],6).getValue() != ""){
                callKeyArr.push(productInfoScriptGrid.cells(gridIds[i],6).getValue())
            }else if (productInfoScriptGrid.cells(gridIds[i],2).getValue()!=''){
                flag = false;
                errroPopUpdateText(productInfoScriptGrid.cells(gridIds[i],0).getValue()+' 단계 녹취 미완료 되었습니다. 해당 부분을 녹취해 주세요.','','','');
                break;
            }
        }
    }
    
    if(!flag){
        $(".rec_confirm").css("display","none");
        $(".rec_startBtn").attr("disabled", false)
        $(".rec_startBtn").css("pointer-events","auto")
        $(".rec_startBtn").css("opacity","1")
    }else{
// $('#alertPopUp').css('display','block');
    		RecEndFlag = true;
    		progress.on()
    		$('#holdon-message').html('저장 중입니다. 화면을 닫지 말고 기다려 주시기 바랍니다.');
    		settimeout = setTimeout(function(){ 
    			mergeFile(callKeyArr)
    		},4000)		
        // 20211115 bella
        // 녹취 병합 전 update rs_recfile2 -> 기존 병합했던 녹취 이력의 r_erroryn = 'Y'로 업데이트
    }
} 

function startTimer() {
	seconds++
	if(seconds < 9){
		$('#seconds').html("0"+seconds);
	}
	if(seconds > 9){
		$('#seconds').html(seconds);
	}
	if(seconds > 59){
		minutes++;
	// $('#minutes').html("0"+minutes);

		if(minutes>9){
			$('#minutes').html(Number(minutes));			
		}else{
		$('#minutes').html("0"+minutes);
		}
		seconds = 0;
		$('#seconds').html("0"+seconds);
	}
	if(minutes > 59){
		hours++;
		$('#hours').html("0"+hours);
		minutes = 0;
		$('#minutes').html("0"+minutes);
	}
}
function stopTimer() {
	clearInterval(interval);
}
function resetTimer(){
	hours = 0;
	minutes = 0;
	seconds = 0;
	$('#seconds').html("0"+seconds);
	$('#hours').html("0"+hours);
	$('#minutes').html("0"+minutes);
}
function intervalManage(flag){
	if(flag == "exit"){
		clearInterval(interval);
		resetTimer();
		stopFlag = false;
		return;
	}
	if(flag == "stop"){
		interval = setInterval(stopTimer(),1000);
		return;
	}
	if(flag == "restart"){
// clearInterval(interval);
		interval = setInterval(startTimer , 1000);
		return;
	}
}

function recordTimerOn() {
	clearInterval(interval);
	clearTimeout(settimeout);
	interval = setInterval(startTimer , 1000);
	
}
function setTimer() {
	let time = new Date();
	let hour = time.getHours();
	let minutes = time.getMinutes();
	let seconds = time.getSeconds();
	if( minutes.toString().length == 1){
		minutes = "0"+minutes;
	}
	if( seconds.toString().length == 1){
		seconds = "0"+seconds;
	}
	
	return hour+":"+minutes+":"+seconds;
}
function waveDisplayOn(){
	$(".start_wave").removeClass("displayHidden");
	$(".first_wave").addClass("displayHidden");
	
}
function waveDisplayOff() {
	$(".start_wave").addClass("displayHidden");
	$(".first_wave").removeClass("displayHidden");	
}

function playPause(){
	progress.on()
	setTimeout(() => {
		progress.on()		
	}, 2000);
	
}

function freeRec(){
	var rowCnt=productInfoScriptGrid.getRowsNum();
	var freeRecArray = [];
	for(var i=0; i<rowCnt; i++){
		var sen=productInfoScriptGrid.cells(productInfoScriptGrid.getRowId(i),2).getValue();
		// 녹취 완료가 한개 이상일시 값을 담아 마지막값을 구하고, 마지막일때 녹취버튼을 비활성화 해준다

		// 녹취완료가 있으면 녹취버튼을 비활성화 한다
		
		// 그리드의 마지막칸에 녹취완료가 있으면 녹취버튼 비활성화
		if(productInfoScriptGrid.cells(productInfoScriptGrid.getRowId(rowCnt-1),2).getValue().indexOf("녹취완료")>-1){
			$(".rec_startBtn").attr("disabled", true)
			$(".rec_startBtn").css("pointer-events","none")
			$(".rec_startBtn").css("opacity","0.4")
		}
	}
}

function nextplay(){
	setTimeout(() => {
		
	
	$('.Tcontent').hide();
	$('.nextPlaySBtn').css('display',"none");
	$('.nextPlayGBtn').css('display',"none");
	$('.nextPlayG').css('display',"none");
	console.log("1")
	var listLen = playListArr.length
	var audio = document.getElementById("audioPlayer");
	var waitTime = $("input[name='speed2']:checked").val();
	var rate = $("input[name='speed']:checked").val();
	if(listLen != playCount){
		try{
			var fileName = playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1]; 
		}catch (e) {
			faceRecEnd();
		}
// if(playListArr[0].indexOf("`") > -1){
//			
// fileName = fileName.split("`")[0];
// }else{
// }
			if($('.rec_startBtn').css('display')!='block')
				audio.src = HTTP+"://"+fileName;
			var count = 0;
			audio.playbackRate = rate;
			if(playListArr[playCount].split("|")[2] != "T"){
				for(var j = playCount; j<playListArr.length; j++){
					var t = playListArr[j].split("|")[2];
					if(t == "G" || t == "S"){
						count++;
	// playCount++;
					}else{
						break;
					}
				}
			}else if(type =='T' &&  playListArr[playCount].split("|")[2] == "T"){
				for(var j = playCount; j<playListArr.length; j++){
					var t = playListArr[j].split("|")[2];
					if(t == "G" || t == "S" || t=='T'){
						count++;
	// playCount++;
					}else{
						break;
					}
				}
			}
		
		
		
		
		if(playListArr[playCount].split("|")[2] == "G"){
			$('span[readnumber="'+(playCount-1)+'"]').removeClass('readingTextBack')
			playIdx++;
// $(".nextPlayGBtn").css("display","block");
			playCount++;
			if(listLen == playCount){
				$('.nextPlayEndBtn').css('display','block');
				if(type=='T'){						
					settimeout = setTimeout(function(){
						faceRecEnd();
						$(".playWaitTime").removeClass("disableddiv");	
					},1000)
				}
			}else{
				fileName =  HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1]; 
				playIdx++;
				audio.src = fileName;
				audio.playbackRate = rate;
				if(type=='T'){
					$('#playBtn'+(count+1)).css('display','block');
				}else{
					$('#playBtn'+(count+1)).css('display','block');
					playCount--;
// settimeout = setTimeout(function(){
// audio.play()
// },5000)
				}
			}
		}else if(playListArr[playCount].split("|")[2] == "R"){
			$('span[readnumber="'+(playCount-1)+'"]').removeClass('readingTextBack')
			playIdx++;
			fileName =  HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1]; 
			audio.src = fileName;
			audio.playbackRate = rate;
			if($('.rec_startBtn').css('display')!='block')
				audio.play();
			timeFlag = true;
			
		
		}else if(playListArr[playCount].split("|")[2] == "S"){
			$('span[readnumber="'+(playCount-1)+'"]').removeClass('readingTextBack')
			playIdx++;
			fileName =  HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1]; 
			audio.src = fileName;
			audio.playbackRate = rate;
			if(type=='T'){
				if(count>1){
					$('#Tcontent'+(count+1)).show();
					$('.nextPlayEndBtn').css('display','block');
				}else{
					$('#Tcontent'+(playIdx+1)).show();
					$(".nextPlaySBtn").css("display","block");							
				}
			}else{				
				if(count>0){
					$('#Tcontent'+(playIdx+1)).show();
					$('.nextPlayEndBtn').css('display','block');
				}
			}
		}else if(playListArr[playCount].split("|")[2] == "G"){
			$('span[readnumber="'+(playCount-1)+'"]').removeClass('readingTextBack')
			playIdx++;
			playCount++;
			fileName =  HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1]; 
			audio.src = fileName;
			audio.playbackRate = rate;
			if(count>0){
				$('#Tcontent'+(playIdx+1)).show();
				$('.nextPlayEndBtn').css('display','block');
			}
		}else if(playListArr[playCount].split("|")[2] == "Z"){
			playIdx++;
			console.log("Z time")
			fileName =  HTTP+"://"+playListArr[playCount].split("|")[0]+playListArr[playCount].split("|")[1]; 
			audio.src = fileName;
			audio.playbackRate = rate;
			if($('.rec_startBtn').css('display')!='block')
				audio.play();
		}else{
			if(timeFlag){
				$('#Tcontent'+(playIdx+1)).show();
				$('#playBtn'+(playIdx+1)).css('display','block');
				$('#playBtn'+(playIdx+1)).attr('onclick','audioPlayNow()');
				// 자동스크롤
				playIdx++;
				timeFlag = false;
			}else{
				$('span[readnumber="'+(playCount-1)+'"]').removeClass('readingTextBack')
				$('span[readnumber="'+playCount+'"]').addClass('readingTextBack')
				// 자동스크롤
				try{
				    document.querySelector(".readingTextBack").scrollIntoView({behavior : 'smooth'});	
				}catch(e){
                    console.log("마디가 다름");
				}finally{
					if($('.rec_startBtn').css('display')!='block')
					audio.play();
				}
			}
		}
		
		playCount++;
	}else if(listLen == playCount){
		if(timeFlag){
			// 투자성향분석일때 10초
			if(type =='T'){
				$('#Tcontent'+(playIdx+1)).show();
				$('.nextPlayEndBtn').css('display','block');
			}else{
				settimeout = setTimeout(function(){
					faceRecEnd();
					$(".playWaitTime").removeClass("disableddiv");
				},5000)				
			}
			timeFlag = false;
		}else{
			// 다계좌 버튼 생성구간
			if(params["MORE_PRODUCT"] != undefined || params["MORE_PRODUCT"] == 'Y'){
				faceRecEnd(true);
			}else{
				faceRecEnd();
			}
			if(rectry!=false){
				rectry=false;
			}else{
			}
		}
		
	}
	}, 300);
}

function nextStepRec(){
	$('.rec_startBtn').click();
}

function nextPlayG() {
	var audio = document.getElementById("audioPlayer");
	clearTimeout(settimeout);
	if(type=='T'){
		audio.play();
	}else{		
	nextplay();
	}
	
	$('.nextPlayGBtn').css('display','none');
	
// audio.play();
}
function nextPlayS() {
	var audio = document.getElementById("audioPlayer");
	clearTimeout(settimeout);
	if(type=='T'){
		nextplay();
	}else{
		nextplay();
	}
	
	$('.nextPlaySBtn').css('display','none');
	
// audio.play();
}

function clearAllRec() {
	
	if (recstart) {
		errroPopUpdateText('녹취 중에는 「녹취 초기화」 불가합니다.','구간녹취종료」 후 다시 시도해주세요.','','');
		return false;
	}
	
	if(retryType =='Y'){
		YesRecTryAllClear();
	}else{
		NoRecTryAllClear();
	}
}

function NoRecTryAllClear(){
	if(confirm("※주의! 해당 버튼은 전체 녹취 초기화 버튼입니다.\n전체 녹취를 초기화 하시겠습니까?")) {
		clearTaResult();
		recordingFinish =false;
		var dataStr = {
			"rScriptRecState" 	: "N",
			"scriptCallKey"	  	: callKey,
			"clearRecYn"		: "Y" // 녹취 초기화 여부
		}
		$.ajax({
			url:contextPath+"/updateScriptStepHistory.do",
			type:"POST",
			dataType:"json",
			data:dataStr,
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
		        	productInfoScriptGrid.clearAndLoad(contextPath+"/productInfoScriptGrid.xml?rs_script_step_fk="+rProductListPk+"&callKey="+ callKey+"&bizDis="+params.BIZ_DIS+"&sttTa="+SttTaUseFlag, function() {});
		        	errroPopUpdateText('전체 녹취가 초기화되었습니다.','처음부터 다시 녹취해주세요.','','');
					$(".rec_startBtn").removeClass("displayHidden");
					$(".rec_startBtn").css("pointer-events","auto")
                    $(".rec_startBtn").css("opacity","1")
                    clearTimeout(endRecTimeOut); 
					$(".rec_confirm").css("display","none");
					rectryFlag = false;
					rectryFlag2 = false;
					//TA결과 초기화
					taResultStepArr = new Array();
					clearRecStartInterVal();
					arrRecStartTimeoutClear()
					clearTaErrorTimeOut();
				
					
				} else {
					errroPopUpdateText('전체 녹취 초기화에 실패하였습니다.','다시 시도해주세요.','','');
				}
			},
			complete: function() {
				progress.off();
			}
		});
	}
}
function YesRecTryAllClear(){
	 layer_popup('#allRetryRecReasonPopup');
	 return;
}



function nextPlayEnd() {
	var audio = document.getElementById("audioPlayer");
	clearTimeout(settimeout);
		faceRecEnd();
		$(".playWaitTime").removeClass("disableddiv");
// nextStepRec()

	
	$('.nextPlaySBtn').css('display','none');
// audio.play();
}
function endPlayEnd() {
	clearTimeout(settimeout);
	faceRecEnd();
	$(".playWaitTime").removeClass("disableddiv");
	nextplay();
	
}	
	

function mergeFile(callKeyArr){
	clearRecStartInterVal();
	params["PRD_NM"] = productName;
    $.ajax({
        url:contextPath+"/updateMergeRecfile.do",
        type:"POST",
        dataType:"json",
        data:{"callKey":callKey},
        async: false,
        success:function(jRes){
            createCallKeyMerge();
            var parm = sendmsg("merge", scriptCallKey, callKeyArr);
            
            $.ajax({
                url:HTTP+"://"+listenIp+":28881/merge",
                type:"POST",
                dataType:"json",
                data:parm,
                async: false,
                success:function(jRes){
                    
                    if(jRes=='0'){
                    	progress.off()
                        errroPopUpdateText('녹취파일 저장에 실패했습니다.','화면 우측하단에 「녹취저장」버튼을',
                        '반드시 누르셔서 저장완료를 하시기바랍니다.','')
                    }else{
                        var temp = {
                                "r_v_rec_fullpath" : jRes.r_v_rec_fullpath,
                                "r_v_rec_ip" : jRes.r_v_rec_ip,
                                "r_v_filename" :jRes.r_v_filename,
                                "callKey" : callKey, // 그룹키
                                "scriptCallKey" : scriptCallKey , // 개별키
                                "productName" : params["PRD_NM"],
                                "productRiskNM" : params["PRD_RISK_GD"] , 
                                "server" : jRes.server,
                                "freeRec" : "N"
                        }
                        
                        $.ajax({
                            url:contextPath+"/insertMergeRecfile.do",
                            type:"POST",
                            dataType:"json",
                            data:temp,
                            async: false,
                            success:function(jRes){
                                console.log(jRes)
                                if(jRes.resData.recDate == null || jRes.resData.recDate == undefined){
                                	sendmsgStt('finalrec',jRes.resData.recDate);
                                }else{
                                	sendmsgStt('finalrec',jRes.resData.recDate);
                                }
                                
                            	$('.rec_save').hide()
                            	$('#holdon-message').html('녹취완료 후 TA결과 분석중입니다. 잠시만 기다려주세요.');
//                        		endAlert('first');
                        		errroPopUpdateText('녹취가 완료되었습니다.','창 종료후 최종적으로 전산등록까지','완료하셔야 녹취파일이 WINI에 저장됩니다.','(주의! 창이 열려 있을 시 다른 녹취 불가)');	
                        		progress.off();
                            	AllRectryMergeInsert();
                            	recordingFinish='true;'
                        		rectryFlag = true;
                            	rectryFlag2 = true;
                            	//TA팝업오픈
                            	if(SttTaUseFlag){
                            		afterMergeOfTapop();
                            	}
                            },
                            error:function(r){
                                progress.off()
                                $('.rec_save').show();
                                errroPopUpdateText('녹취저장에 실패하였습니다.','해당 구간 다시 녹취 진행해 주세요.','','')
                            }
                        });
                    }
                },
                error:function(r){
                    progress.off()
                     errroPopUpdateText('녹취저장에 실패하였습니다.','다시 시도해 주세요','','')
                }
            });
        }
    });
} 
function AllRectryMergeInsert(){
	if(allRecParamJson == null){
		return false;
	}
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	productInfoScriptGrid.selectRowById(gridIds[0]);
	var scriptCallKeyArr = new Array();
	for(var i = 0; i< gridIds.length; i++){
		var callKeyAp = productInfoScriptGrid.cells(gridIds[i],6).getValue()
		if(callKeyAp.length!=0){
			scriptCallKeyArr.push(callKeyAp);
		}
	}
	
	var arrText="";
	for(var i = 0; i< scriptCallKeyArr.length; i++){
		if(i==scriptCallKeyArr.length-1){
			arrText+= scriptCallKeyArr[i];
			break;
		}
		arrText+= scriptCallKeyArr[i]+","
	}
	
	//전체 각가의 녹취키
	allRecParamJson["callKeyApArr"] = arrText;
	allRecParamJson["callKey"] = callKey;
	
	// 1. 업데이트 성공 시 해당 콜키로 재녹취 사유 insert
	$.ajax({
		url : contextPath + "/insertAllRetryRecReason.do",
		type : "POST",
		data : allRecParamJson,
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y"){
				allRecParamJson = null;
			}else{
				console.log("전체녹취 사유 저장실패");
			}
		}
	})
}


function endAlert(first){
	clearTimeout(endRecTimeOut);
	if(!first)
		errroPopUpdateText('우측상단에 (X) 버튼을 눌러 화면을 종료해 주세요.','화면을 종료하지 않으면 다른 녹취를 시작할 수 없습니다.','','');
	  endRecTimeOut = setTimeout(() => {
		  endAlert();						
	  }, 20000);
}
function recStartBtnDisable(){
	$(".rec_startBtn").attr("disabled", true)
	$(".rec_startBtn").css("pointer-events","none")
	$(".rec_startBtn").css("opacity","0.4")
}
function clearAllBtenDisable(){
	$("#clearAllRec").attr("disabled", true)
	$("#clearAllRec").css("pointer-events","none")
	$("#clearAllRec").css("opacity","0.4")
}
function retryBtnDisable(){
	$(".ui_btn_white").attr("disabled", true)
	$(".ui_btn_white").css("pointer-events","none")
	$(".ui_btn_white").css("opacity","0.4")
}


function forTextList(rScriptDetailTextList,type){
	if(rScriptDetailTextList.length == 0){
		return "";
	}
	var html = "";
	for (var i = 0; i < rScriptDetailTextList.length; i++) {
		var text = "";
        var buf = "";
		var brArr = rScriptDetailTextList[i].split("\n");
		if(brArr[brArr.length-1].trim().length == 0){
			for(var j=0; j<brArr.length -1; j++){
				if(j == brArr.length-2){
					buf+= brArr[j];
					break;
				}
				buf+= brArr[j]+"<br>";
			}
			text = "&nbsp;&nbsp;"+buf;
		}else{
		    text = "&nbsp;&nbsp;"+rScriptDetailTextList[i].replace(/\n/gi,"<br>")
		}
		if(i==0){
			text = text.replace('&nbsp;&nbsp;','');
		}
		html+= "<span class='readingText' readNumber='"+textListIdx+"'>"+text+"</span>";
		textListIdx++;
	}
	return html;
}
function getFirst(name,callBack){
	setTimeout(() => {
		callBack();
	}, 3000);
	
}

function audioPlayNow(){
	$('.Tcontent').hide()
	$('span[readnumber="'+(playCount-1)+'"]').addClass('readingTextBack')
	audioTag.play()
}
	
function manualRecBoxShow() {
	$('#normalRecCommentBox').hide();
	$('#ManualRecCommentBox').show();
}	
function manualRecBoxHide() {
	$('#normalRecCommentBox').show();
	$('#ManualRecCommentBox').hide();
}	
function createMoreProductManualText(){
	var text = "자동리딩방식이 불가하오니, 스크립트를 출력하여 직접리딩으로 녹취하여 주시기 바랍니다.\n" +
			"[스크립트 조회: 포탈>투자상품 녹취시스템>스크립트 관리(검색)]\n" +
			"◎ 다계좌 신규시 유의사항\n" +
			"- 하나의 펀드상품을 '다른 클래스 다건 신규' 및 '동일 클래스 다건 신규'시 녹취방법 안내\n" +
			"① 항목 : 일괄 녹취 (1번만 리딩)\n" +
			"② ~ ④ 항목 : 녹취 건수 대로 리딩 - 예: 동일상품 A클래스 2계좌 녹취 → 스크립트 내에 ② ~ ④구간 2번 반복리딩(같은내용 반복)\n" +
			"⑤ ~ ⑥ 항목 : 일괄 녹취(1번만 리딩)\n\n" +
			"[⑤~⑥ 스크립트 안내]" +
			" ⑤ 기타사항 - 과세, 금소법 등\n" +
			"- 직원리딩 : 지금부터 「투자자에게 설명해야 할 공통 설명사항」에 대해 설명드리겠습니다. 이는 고객님께서 가입하시는 상품에 공통적으로 적용되는 내용입니다.\n" +
			"이는 고객님께서 가입하시는 상품에 공통적으로 적용되는 내용입니다.\n" +
			" - 직원리딩 : 개인고객일 경우 → 스크립트 내에 개인고객 과세 문구 리딩/ 법인고객일 경우 → 스크립트 내에 법인고객 과세 문구 리딩\n" +
			"⑥ 투자의사 확인 및 계약체결\n" +
			"- 직원리딩 : 이상으로 펀드에 대한 모든 설명을 마치겠습니다. 추가로 더 궁금한 점은 없으십니까?\n" +
			"- 고객답변 : 네 → 진행/ 아니오 → 질문사항 답변 또는 상품판매 중단\n" +
			"- 직원리딩 : 고객님께서 가입하신 상품은 첫번째, OOOOO로서, 가입금액은 XXXXX 입니다. (녹취 건수에 따라 해당 멘트 반복 : 첫번째, 두번째, 세번째 ~) \n" +
			"- 고객답변 : 네 → 진행/ 아니오 → 운용자산 명 가입금액 재확인 또는 상품판매 중단\n" +
			" - 직원리딩 : 가입 후, 해피콜을 통해 상품 설명이 제대로 이루어졌는지, 비예금상품에 가입하려는 고객님의 의사가 명확한지, 투자성향 분석 결과에 대한 설명과 고객님의 투자성향이 일치하는지 등에 대해 질문 드릴 예정이니 설명드린 대로 답변 부탁 드립니다.\n" +
			"지금까지 판매직원 OOO 이었습니다. 감사합니다. ";
	return text;
}

function grepRecParamHistoryRecTryJson(){
	var dataStr = {"callKey" : params.RCD_KEY};
	
	if(dataStr.callKey == undefined || dataStr.callKey == null){
		return false;
	}
	
	$.ajax({
		url:contextPath+'/grepRecParamHistoryJson.do',
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: true,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				if(jRes.resData.historyParam.rrecRetryJson !=null && jRes.resData.historyParam.rrecRetryJson != undefined){
					allRecParamJson = JSON.parse(jRes.resData.historyParam.rrecRetryJson)
				}
			}
		}
	});
	
	
	
}

/*
*	그리드에 key를 실제 step key와 비교해서 다계좌 상품의 스탭인지 체
*/
function checkEnableMoreProductStep(){
	if(moreProductStepArr != null){
		return;
	}
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	//그리드 사이즈
	var gridIdsArr = new Array();
	for(var i = 0; i< gridIds.length; i++){
		gridIdsArr.push(productInfoScriptGrid.cells(gridIds[i],1).getValue())
	}
	
	var dataStr =  {
		"gridIdsArr" : gridIdsArr
	}
    console.log(JSON.stringify(dataStr))
	
	
	$.ajax({
		url:contextPath+'/checkEnableMoreProductStep.do',
		data:JSON.stringify(dataStr),
		type:"POST",
		dataType:"json",
		async: false,
        contentType: 'application/json; charset=utf-8',
		success:function(jRes){
			console.log(jRes);
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				console.log(jRes)
				moreProductStepArr = jRes.resData.result.moreProductPkArr; 
			}
		},
		error:function(e){
			console.log(e)
		}
	});
}

function moreProductStepCheckToNextPlay() {
	
	if(productInfoScriptGrid != null && productInfoScriptGrid != undefined){
		//다계좌  스탭세팅
		checkEnableMoreProductStep();
	}
	
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	
	var selectedId = productInfoScriptGrid.getSelectedRowId();
	

	//선택된 grid Idx
	var selectedIdx = 0;
	if(params["MORE_PRODUCT"] == 'N' || params["MORE_PRODUCT"] == undefined) return true;

	if(selectedId == gridIds[0]){
		return true;
	}
	
	if(gridIds == null || selectedId == null) return true;
	
	var gridSize = gridIds.length;
	
	// 다계좌일경우 체크 시작
	for (var i = 0; i < gridIds.length; i++) {
		 if(selectedId == gridIds[i]) selectedIdx = i;
	}
	
	var nextSelectId = gridIds[selectedIdx+1];
	if(nextSelectId == null || nextSelectId == undefined) return true;
	
	
	var result = true;
	for (var i = 0; i < moreProductStepArr.length; i++) {
		var tKey = productInfoScriptGrid.cells(nextSelectId,1).getValue()
		if(tKey == moreProductStepArr[i]){
			result = false;
		}
	}
	
	return result;
}

function upgradeTProductCodeCheck() {
	var cusAnswer = params["CUS_ANSER"];
	
	var cusAnswerSplit = cusAnswer.split(':');
	
	var cusAnswerArr = cusAnswerSplit[1].split(',');

	if(params.RUSE_YN == 'N' && cusAnswerArr.length != 12){
		params.PRD_CD = 'T0000000000001';
		return;
	}
	if(params.RUSE_YN == 'Y' && cusAnswerArr.length != 3){
		params.PRD_CD = 'T0000000000002';
		return;
	}
	
	if(cusAnswerSplit[0] != cusAnswerArr.length) {
		delete params.UP_INVEST_YN;
		params.PRD_CD = 'T0000000000001';
		if(params.RUSE_YN == 'Y'){
			params.PRD_CD = 'T0000000000002';
		}
	}
	
	tProductValueListMaker(cusAnswerArr);
}

function tProductValueListMaker(arr) {
	for (var i = 0; i < arr.length; i++) {
		console.log(i)
		params["CUS_ANSER_"+(i+1)] = arr[i];
	}
}


var loginAndRecStartTimeout;
function recStartWS(){
	// STT & TA
	loginAndRecStartTimeout =setTimeout(() => {
		sendmsgStt("login");
		
		//stt recStart 가 안올때
		if(SttTaUseFlag){
			console.log('나 sttRecStartResultTimeoutArr에 푸시할거야');
			sttRecStartResultTimeoutArr.push(setTimeout(() => {
				errroPopUpdateText('서비스가 일시적으로 중단되었습니다.','녹취버튼을 눌러 재실행 해주세요.','','')
				progress.off();
				stopEvent('1','e')
				errorSttAction();
			}, 10000));
		}
		
		
		// 엔진녹취시작
		setTimeout(() => {
			sendmsg("recStart", scriptCallKey)
			allFlag = true;
			enginFlag = false;
			micErrorFlag = false;
			micError = false;
//			micFlag = false;
//			soundErrorFlag = false;
			
			//20220504 장진호 추가
			//51/003 플레그 초기화
			recStopResFlag = 0;
		}, 100);
	}, 900);
	
}
function goFreeRecPost(){
	var f = document.createElement('form');
	
	var obj;
	obj = document.createElement('input');
	obj.setAttribute('type','hidden');
	obj.setAttribute('name','params');
	obj.setAttribute('value',JSON.stringify(params));
	
	f.appendChild(obj);
	f.setAttribute('method','post');
	f.setAttribute('action','/recsee3pTEST/faceRecording/free_recording');
	document.body.appendChild(f);
	
//	f.submit();
}	
function taPopupOpen() {
	$('#taResultLastPop').fadeIn(400);
}	
	
function groupPkSet(data){
	var flag = data.rProductGroupYn;
	var pk = leadingZeros(data.rProductGroupPk,8)
	console.log("pk : "+pk);
	if(flag == 'Y') {
		groupInfo = {
						"groupYn" :"Y",
						"groupPk" : pk
					};
	}
	if(flag == 'N'){
		groupInfo = {
					"groupYn" :"N",
					"groupPk" : pk
					};
	}
}

function afterMergeOfTapop(){
	if( taResultPopUpFlag ){
		TaPopupSetting();
		$('#taResultLastPop').fadeIn(400);
		$('#taResultSeeBtn').show()
	}
}

function SttTaNotUseGridFactory(){
	var imgSrc = "<div id='recPlay' style='position: relative; top: 2px\'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleGray.png' style='width:16px;' /></div>"
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	
	for (var i = 0; i < gridIds.length; i++) {
		productInfoScriptGrid.cells(gridIds[i],4).setValue('');
	}
	var loadUrl = "/clearStepRec/"+params.RCD_KEY
	$.ajax({
		url:contextPath+loadUrl,
		data:{},
		type:"PUT",
		dataType:"json",
		async: false,
		cache: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				console.log("TA상태 초기화 완료!");
			}
		}
	});
}

function finalCheck(){
	var flag = false;
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	var arr = new Array();
		for(var i = 0; i< gridIds.length; i++){
			var recState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
	        if( recState.trim().length > 0 ) {
	            if(recState == 'N'){arr.push(recState);}
	        }
	    }
	if(arr.length == 1){
		flag = true;
	}
	
	return flag;
}

function stopBtnFinalSend(){
	var flag = false;
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	var arr = new Array();
		for(var i = 0; i< gridIds.length; i++){
			var recState = productInfoScriptGrid.cells(gridIds[i],7).getValue();
	        if( recState.trim().length > 0 ) {
	            if(recState == 'N'){arr.push(recState);}
	        }
	    }
	if(arr.length == 0){
		flag = true;
	}
	var loadUrl = "/searchRecTime.do";
	if(flag){
		$.ajax({
			url:contextPath+loadUrl,
			data:{"callKey" : callKey},
			type:"GET",
			dataType:"json",
			async: false,
			cache: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					// 불러온 옵션 추가
					sendmsgStt('finalrec',jRes.resData.recDate);
					
				}
			}
		});
	}
}
function checkSound(){
	var taudio = new Audio();
	var src = contextPath+"/getRecFileTest.do?fileName=beef.mp3"
	taudio.autoplay = true;
	taudio.src = src;
	taudio.pause();
	taudio.load();
	try{
		taudio.play();
	}catch (e) {
		console.log(e)
	}

	taudio.addEventListener('error',function(event) {
		taudio.pause();
    });

	taudio.addEventListener('loadeddata',function(){
		try {
			taudio.currentTime=0.1
			taudio.pause();
			return true;
		} catch(e){
			taudio.pause();
		}
	});
}

function stopE(){
	$('.Tcontent').hide();
	$('.nextPlayEndBtn').css("display","none");
	$('.nextPlaySBtn').css('display',"none");
	$('.nextPlayGBtn').css('display',"none");
	$('.nextPlayG').css('display',"none");
	$(".rec_startBtn").removeClass("displayHidden");
	$(".rec_endBtn").addClass("displayHidden");
	$(".start_wave").addClass("displayHidden");
	$(".first_wave").removeClass("displayHidden");
	$(".playWaitTime").removeClass("disableddiv");
	$(".wave_btn").addClass("displayHidden");
}

function errorModalMicClose(){
	 $('#errorModalMic').attr('class','modal')
}