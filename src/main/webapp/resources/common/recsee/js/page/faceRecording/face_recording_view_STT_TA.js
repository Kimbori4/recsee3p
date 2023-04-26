var productListPk;
var RecEndFlag = false;
var taErrorFlag = false;
var sttErrorFlag = false;
//녹취시작시간
var recStartData = {
		"callid" : "",
		"recStartTime" : 0
};
var recStartTime = 0;
//녹취시작인터벌
var recStartInterVal;
var freeRecTaFlag=false;
var setTimeOutArr = new Array();

//TA결과 기다리는 시간
var taResultResTimeOut = null; 

var taReqFlag = false;
var sttRecStart = false;

//taParam에 기존적인 정보를 셋팅
function addBasicTaParam(){
	if(params == null || params == undefined){
		return;
	}
	taParam["rdg_id"] = params.RCD_KEY;
	try{
		taParam["prod_cd"] = rProductListPk;
	}catch (e) {
		freeRecTaParamSetting();
	}
	taParam["prod_type"] = params["BIZ_DIS"];
	taParam["prod_nm"] = params["PRD_NM"];
	
	if(taParam.prod_nm == null || taParam.prod_nm == undefined){
		taParam["prod_nm"] = "예시데이터";
	}
	
	taParam["cslr_no"] = params["ADVPE_NO"];
	taParam["cslr_nm"] = params["ADVPE_NM"];
	taParam["cus_id"] = params["CSNO"];
	taParam["cus_nm"] = params["CUS_NM"];
	taParam["cus_type"] = params["PSN_YN"];
	taParam["cus_inv_type"] = params["CSINC_GD"];
	if(taParam.cus_inv_type == null || taParam.cus_inv_type == undefined){
		taParam["cus_inv_type"] = "1";
	}
	
	if(params["AGNPE_NM"] == '없음' || params["AGNPE_NM"] == null || params["AGNPE_NM"] == undefined){
		taParam["cus_deputy_yn"] = 'N';
	}else{
		taParam["cus_deputy_yn"] = 'Y';
	}
	
	taParam["cus_senior_yn"] = params["FULL_65_TAX_ABV_YN"];
	if(taParam.cus_senior_yn == null || taParam.cus_senior_yn == undefined){
		taParam["cus_senior_yn"] = "Y";
	}
	
	taParam["rec_yn"] = params["REC_YN"];
	if(taParam.rec_yn == null || taParam.rec_yn == undefined){
		taParam["rec_yn"] = "Y";
	}
	if(!manualType){
		taParam["rec_manual_yn"] = 'N';
	}else{
		taParam["rec_manual_yn"] = 'Y';
	}
	
	callTypeCheck();
	
	var flag = getDeptData();
	if(!flag){
		taParam["dept_lcls_cd"] = '20Z28';
		taParam["dept_mcls_cd"] = '20308';
		taParam["dept_scls_cd"] = '20102';
		taParam["dept_lcls_nm"] = '우리은행';
		taParam["dept_mcls_nm"] = '영업점';
		taParam["dept_scls_nm"] = '잠실5단지(출)';
	}
	
	taParam["call_cmpl_yn"] = 'N';
	
	
	
}

function callTypeCheck(){
	if(params["PRD_CD"] == 'T0000000000001' || params["PRD_CD"] == 'T0000000000002' || params["PRD_CD"] == 'T0000000000003'){
		taParam["call_type"] = '1';
		return;
	}
	if(freeRecFlag && params.TA_TYPE == 1){
		taParam["call_type"] = '2_1';
	}if(freeRecFlag && params.TA_TYPE == 2 ){
		taParam["call_type"] = '3_2';
	}else{
		taParam["call_type"] = '3';
	}
}

function freeRecTaParamSetting() {
	taParam["script_cd"] = '00000001';
	taParam["script_dtl_cd"] = '00000001';
	var type = leadingZeros(params["BIZ_DIS"],2)
	
	var callPurpSclsCd = type+'0000000100000001';
	
	var prdCd;
	
	if(params.BIZ_DIS == 2 && params["TA_TYPE"] =='1'){
		prdCd = '00612674';
	}
	if(params.BIZ_DIS == 1 && params["TA_TYPE"] =='1'){
		prdCd = '00612675';
	}
	taParam["call_purp_scls_cd"]= prdCd + callPurpSclsCd;
	taParam["script_dtl_trgt"] = "1";
	

}

var loginRes = null;
var loginFailedRes = null;

//ta 파라미터 기본셋팅
function taParamMaker(){
	
}
//ta 파라미터 해당녹취 구간 파라미터 셋팅
function addTaParamScriptStep(detailList){
	//script_cd : 스크립트 구간 세팅
	taParam["script_cd"] = productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),1).getValue();
	taParam["prod_cd"] = productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),10).getValue();
	
	try{
		taParam["para_id"] = 
			productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),1).getValue()+"_"+productInfoScriptGrid.getRowIndex(productInfoScriptGrid.getSelectedRowId());
	}catch (e) {
		taParam["para_id"] = "00000001";
	}
	
	//다계좌시 마지막 스탭검사
	if(params.TR_AC_YN == 'N' && params.MORE_PRODUCT == 'Y'){
		var lastStepId = productInfoScriptGrid.getAllItemIds().split(",").pop();
		//마지막 스탭과 현재 선택스탭이 같으면 로직시작
		if(lastStepId == productInfoScriptGrid.getSelectedRowId()){
			detailList = factoryMoreProductDetailSetting(detailList);
			taParam["script_cd"] = leadingZeros('99999999',8);
			taParam["prod_cd"] = leadingZeros('99999999',8);
		}
	}
	
	//script_dtl_cd 세팅
	var scriptDetailPkList = detailListPkSelected(detailList);
	taParam["script_dtl_cd"] = scriptDetailPkList;
	taParam["call_purp_scls_cd"]= callPurpSclsCdSetting(detailList);
	taParam["script_dtl_trgt"] = calltrgt(detailList);
	
}
//call_purp_scls_cd 파라미터 생성
function detailListPkSelected(detailList){
	var delimeter = "|";
	var buf = "";
	detailList.forEach( info => {
		buf+= info.rScriptDetailPk + delimeter;
	});
	
	if(buf[buf.length-1] == "|"){
		buf = buf.substring(0 , (buf.length-1) );
	}
	
	return buf;
}
// script_detl_trgt  파라미터 생성
function calltrgt(detailList){
	var delimeter = "|";
	var buf = "";
	detailList.forEach( i => {
		buf+= switchDetailType(i.rScriptDetailType) + delimeter;
		
	})
	if(buf[buf.length-1] == "|"){
		buf = buf.substring(0 , (buf.length-1) );
	}
	return buf;
}

function switchDetailType(type) {
	if(type == 'T'){
		return '0';
	}
	if(type == 'S'){
		return '1';
	}
	if(type == 'G'){
		return '2';
	}
	if(type =='R'){
		return '1';
	}
	return '0';
}


function callPurpSclsCdSetting(detailList){
	var prodCd = "";
	if(productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),10).getValue() != null && 
	   productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),10).getValue() != undefined){
		prodCd = leadingZeros(productInfoScriptGrid.cells(productInfoScriptGrid.getSelectedRowId(),10).getValue(),8)
	}
	/*
	if(groupInfo == null || groupInfo == undefined){
		prodCd = leadingZeros(rProductListPk,8);
	}else{
		prodCd = groupInfo.groupPk;
	}
	*/
	
	//다계좌시 마지막 스탭검사
	if(params.TR_AC_YN == 'N' && params.MORE_PRODUCT == 'Y'){
		var lastStepId = productInfoScriptGrid.getAllItemIds().split(",").pop();
		//마지막 스탭과 현재 선택스탭이 같으면 로직시작
		if(lastStepId == productInfoScriptGrid.getSelectedRowId()){
			prodCd = leadingZeros('99999999',8);
		}
	}
	
	var prodType = leadingZeros(params["BIZ_DIS"],2);
	var scCd = leadingZeros(taParam["script_cd"],8);
	var scdtcd = getDetailPkListArr(detailList);
	
	var buf = '';
	var delimeter = "|";
	scdtcd.forEach( i => {
		buf+= prodCd + prodType + scCd + i + delimeter;
	})
	if(buf[buf.length-1] == "|"){
		buf = buf.substring(0 , (buf.length-1) );
	}
	
	return buf;
}

function getDetailPkListArr(detailList){
	var arr = new Array();
	detailList.forEach( i => {
		arr.push(leadingZeros(i.rScriptDetailPk,8));
	});
	return arr;
}



//로그인 & TA요청 파라미터 콜키 셋팅
function addLoginParamCallKey(){
	loginParam["callid"] = scriptCallKey;
//	taParam["para_id"] = scriptCallKey;
	taParam["rec_id"] = scriptCallKey
}

function sttTaLoginReq(){
}

//WebSocket 호출 로그인 이벤트
function sttLoginEvent(res) {
	loginResult = res.result;
	
	switch (loginResult) {
	case "success":
		console.log("(SttLoginEvent) login success");
		break;
	case "fail":
		console.log("(SttLoginEvent) login failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
	default:
		break;
	}
}


function sttRecStartResEvent(res) {
	recStartResult = res.result;
	
	switch (recStartResult) {
	case "success":
		sttRecStart = true;
		console.log("(SttLoginEvent) RecStart success");
		console.log('(SttLoginEvent) scriptCallKey : '+ scriptCallKey);
		arrRecStartTimeoutClear();
//		if(micErrorFlag || soundErrorFlag){
//			console.log("(sttRecStartResEvent) micErrorFlag : "+micErrorFlag);
//			console.log("(sttRecStartResEvent) soundErrorFlag : "+soundErrorFlag);
//			return;
//		}
		
		recStartData.callid = res.callid;
		recStartInterVal = setInterval(() => {
			recStartData.recStartTime = recStartData.recStartTime+500;
//			console.log("time : "+ recStartTime);
		}, 500);
		
		
		//녹취시작시 TA요청
		console.log("taReqFlag : "+taReqFlag)
		setTimeout(() => {
			taReqSendMsg();
		}, 1500);
		
		break;
	case "fail":
		console.log("(SttLoginEvent) login failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
//		alert(reason);
		/*
		 녹취 시작 응답 실패 이벤트 작성구간
		 * */
	default:
		break;
	}
}

//TA 요청 응답
function taResponseEvent(res) {
	taResResult = res.result;
	switch (taResResult) {
	case "success":
		console.log("(taResponseEvent) ta Request success");
		break;
	case "fail":
		console.log("(taResponseEvent) ta Request failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
//		alert(reason);
		/*
		 녹취 시작 응답 실패 이벤트 작성구간
		 * */
	default:
		break;
	}
}

function taReqSendMsg() {
	sendmsgStt("taReq");
}
	
// TA 결과 분기
function taResultResponseEvent(res) {
	//callid, ta 결과
	var taResult = res.result;
	var reason = res.reason;
	var callId = res.callid;
	console.log("["+callId+"] taResultResponseEvent()");
	console.log(res);
	taResultEvent(res);
	if(taResult=='Y' || taResult=='N') {
		$.post(contextPath+'/sendTaResult.do',{callId:callId, taResult:taResult,reason:JSON.stringify(reason)},
				function(res){
			console.log('sendTaResult Ok')
		},'json');
	}else {
		$.post(contextPath+'/sendTaResult.do',{callId:callId, taResult:'F',reason:JSON.stringify(reason)},
				function(result){
			console.log('sendTaResult Ok')
		},'json');
	}
	
	
	//마지막구간일때 팝업오픈
	if(RecEndFlag){
		RecEndFlag = false;
		$('#taResultSeeBtn').show()
//		TaPopupSetting();
		taResultPopUpFlag = true;
	}
}

function taResultEvent(res) {
	console.log("TA result : "+res.result);
	var callId = res.callid;
		
	try{
		var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
		
			var data = {};
			console.log('this product is normalRec');
				
			//스탭 녹취키 체크
			for (var i = 0; i < gridIds.length; i++) {
					var gridCallId = productInfoScriptGrid.cells(gridIds[i],6).getValue();
		            console.log("gridId : " +gridCallId);
		            console.log("callId : "+callId);
					if(gridCallId.length == 0){
						continue;
					}
					if(gridCallId == callId){
						data["gridId"] = gridIds[i];
						data["result"] = res.result;
						data["taDetail"] = res.reason;
						var flag = false;
						console.log('ccccccc1')
						for (var j = 0; j < taResultStepArr.length; j++) {
							console.log('ccccccc2')
							var taListId = taResultStepArr[j].gridId;
							if(taListId == gridIds[i]){
								flag = true;
								taResultStepArr.splice(j , 1 , data);
								console.log('ccccccc3')
//								break;
							}else{
								continue;
							}
						}
						if( !flag ){
							taResultStepArr.push(data);
						}
						break;
					}
			}
			// 결과별 이미지변경 액션
			switch (res.result) {
			case "Y":
				taResultSuccessAction(data.gridId);
				break;
			case "N":
				taResultFailedAction(data.gridId);
				break;
			}
	}catch (e) {	//자유녹취 taResult이벤트
		
		console.log("error!!!!!!!!")
		console.log(e)
		
		//TA결과가 왓다는 플레그
		freeRecTaResultFlag = true;
		progress.off();
		freeRecTaFlag=true;
		checkInfo();
		taResultInfo(res);
		console.log('this product is freeRec');
		
	}

}

//이미지 경로 설정할것
//TA 결과 성공 액션
function taResultSuccessAction(id){
	try{
		//	common/recsee/images/project/icon/CircleGray.png
		var imgSrc = "<div id='recPlay' style='position: relative; top: 2px\'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleBlue.png' style='width:16px;' /></div>"
		console.log('s');
		console.log(id);
		productInfoScriptGrid.cells(id,4).setValue(imgSrc)
	}catch (e) {
		console.log(e)
	}
}
//TA 결과 실패 액션
function taResultFailedAction(id){
	try{
		
	var imgSrc = "<div class='taErrorResultBtn' id='"+id+"' style='position: relative; top: 2px\' onclick='taErrorPopUp(this)'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleYellow.png' style='width:16px;' /></div>"
	productInfoScriptGrid.cells(id,4).setValue(imgSrc)
	//TA결과시 마지막구간이 아닐때 팝업오픈이벤트
	}catch (e) {
		console.log(e)
	}
	if( !RecEndFlag ){
/*
	// 팝업오픈이벤트
	//		var stepName = productInfoScriptGrid.cells(id,0).getValue();
	//		taResultPop(stepName);
*/
	}
}

//실시간 TA결과 팝업창 오픈 이벤트
function taResultPop(stepName) {
	console.log('===================')
	console.log('taResultPop()')
	console.log('stepName : '+stepName)
	console.log('===================')
	$('.taResultPopStepNameBody').html(stepName);
	$('#taResultPop').fadeIn(500);
}

var taResultResFlag = true;
//sttRecStop이벤트
function sttRecStopResEvent(res) {
	recStartResult = res.result;
	switch (recStartResult) {
	case "success":
		console.log("(sttRecStopResEvent) Rec Stop");
		
		/*
		 * recStopBtnFlag => 기본값 false (녹취중단 버튼을 누를경우를 대비) 
		 * 자유녹취는 버튼이 없으니 항상 이 로직을 탐
		 * */
		if(!recStopBtnFlag){
			var callid = res.callid;
			//STT장애상황 로직
			if(recStartData.recStartTime < 3000 ){
				//STT장애상황일때 실행함수 (자유녹취 , 일반녹취 구분은 내부)
				try{
					if(recStartData.callid == callid){
//						alert("예기치 못한 오류가 발생하였습니다.\n일반녹취로 전환합니다.");
						productInfoScriptGrid.getAllItemIds().split(",");
//						errorSttAction();
						return;
					}
				}catch (e) {//자유녹취일때 예외상황 함수
					if(freeRecStop==false && recStartTime <1500) {
						//errorOccur();
						alert("네트워크 에러가 발생했습니다. 새로고침 후 다시 실행해주세요.")
					}
					
				}
			}else{
				console.log("rectime > 5");
			}
		}
		
		/*
		 * recStopBtnFlag => 기본값 false (녹취중단 버튼을 누를경우를 대비) 
		 * 자유녹취는 버튼이 없으니 항상 이 로직을 탐
		 * */
		//TA장애상황로직
		if( !recStopBtnFlag ){
//			if(!micErrorFlag && !soundErrorFlag && !micFlag2 && !enginFlag2){
				var taErrorTimeOut = setTimeout(() => {
					try{
						console.log("TA ERROR TEST START");
						
						var callId = res.callid;
						var retryCheck  =  false;
						retryKeyArr.forEach(i=>{
							if(i == callId){
								retryCheck = true;
							}
						});
						
						if(retryCheck){
							console.log('재녹취시 날라갓던 키에 대한 TA결과로인해 TA장애상황  체크는 생략['+callId+']')
							return;
						}
						productInfoScriptGrid.getAllItemIds().split(",");
						
						if(taResultStepArr.length == 0){
							console.log("taResultStepArr size == 0 Error")
							taResultResFlag = false;
							errorSttAction();
							return;
						}
						var findResult = false;
						var findIdx = 0
						for (var i = 0; i < taResultStepArr.length; i++) {
							var gridCallId = productInfoScriptGrid.cells(taResultStepArr[i].gridId,6).getValue();
	                        console.log("gridId : "+ gridCallId);
	                        console.log("resId : "+ callId);
							if(gridCallId == callId){
								findResult = true;
								findIdx = i;
							}else{
								continue;
							}
						}
						
						if( !findResult ){
							console.log("해당 스탭의 TA결과값을 찾을 수 없어 에러가남");
							taResultResFlag = false;
							errorSttAction();
							return;
						}
					}catch (e) {
						//자유녹취일때 예외상황 함수
						if(freeRecTaFlag==false) {
							//errorOccur();
							alert("네트워크 에러가 발생했습니다. 새로고침 후 다시 실행해주세요.");
						}
					}
				}, 30000);
				setTimeOutArr.push(taErrorTimeOut);
//			}
		}
		recStopBtnFlag = false;
		
		recStartData.callid = '';
		recStartData.recStartTime = 0;
		
		break;
	case "fail":
		console.log("(SttLoginEvent) RecStop failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
//		alert(reason);
		/*
		 녹취 종료 응답 실패 이벤트 작성구간
		 * */
	default:
		break;
	}
	clearRecStartInterVal();
}

function sttLogoutEvent(res) {
	recStartResult = res.result;
	switch (recStartResult) {
	case "success":
		console.log("(SttLoginEvent) RecStart success");
		break;
	case "fail":
		console.log("(SttLoginEvent) login failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
//		alert(reason);
		/*
		 로그아웃  실패 이벤트 작성구간
		 * */
	default:
		break;
	}
}

//STT 결과 response Event
function sttResultResponseEvent(res) {
	var  rxtx = res.rxtx;		//화자
	var  callid = res.callid;	//해당 상품 콜아이디
	var  msg = res.result;		//인식결과 (카톡메세지) 
	/*
	 * STT 결과 이벤트 구간
	 * */
	try{
		productInfoScriptGrid.getAllItemIds().split(",");
		faceMessageAppend(res);
	}catch (e) {
		console.log("freeRec!")
		//messageAppend(res);
	}
	
}


//최종녹취 Response 이후 이벤트
function finalRecStopEvent(res) {
	recStartResult = res.result;
	switch (recStartResult) {
	case "success":
		console.log("(SttLoginEvent) RecStart success");
		
//		TaPopupSetting();
//		$('.modal').fadeIn(400);
		
		break;
	case "fail":
		console.log("(SttLoginEvent) login failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
//		alert(reason);
		/*
		 최종녹취 실패 이벤트 작성구간
		 * */
	default:
		break;
	}
}
// TA 결과 팝업 수정 함수
function TaPopupSetting(){
	if(taResultStepArr == null || taResultStepArr == undefined) return false;
	
	var failStepArr = TaFailedResultArrayMaker();
	if(failStepArr == null || failStepArr == undefined || failStepArr.length == 0) { 
		$('#'+stepNamePopupId).empty();
		$('#'+stepOkPopupId).empty();
		return false;
		};
	
	//taResultStepName
	//taResultStepOk
	TaPopupAppend(failStepArr , 'taResultStepName' , 'taResultStepOk');
	
}

// Ta결과 Fail Array Maker
function TaFailedResultArrayMaker(){
	var failStepArr = new Array();
	if(taResultStepArr.length == 0){
		return null;
	}
	taResultStepArr.forEach(i=>{
		if(i.result == 'N'){
//			failStepArr.push({"callid":i.callid , "result":"N" , "name" : gridCallIdSearch(callid)});
			failStepArr.push({"callid":i.gridId , "result":"N" , "name" : gridCallIdSearch(i.gridId)});
		}
	})
	return failStepArr;
}

//TA 결과 팝업  내용 추가 함수
function TaPopupAppend(failStepArr , stepNamePopupId , stepOkPopupId){
	var stepNameDivOpen = '<p class="modal-text" style="color: black;padding: 10px 1px 10px 1px;font-size: 0.9em;text-align:center;">';
	var stepOkDivOpen = '<p class="modal-text" style="color: black;padding: 10px 1px 10px 1px;font-size: 0.9em;text-align:center;text-align:center;border-left:1px solid lightgray;">';
	var pTagClosed= '</p>';
	
	$('#'+stepNamePopupId).empty();
	$('#'+stepOkPopupId).empty();
	
	failStepArr.forEach(i=>{
		var stepName = stepNameDivOpen + i.name + pTagClosed;
		var stepOk = stepNameDivOpen + (i.result == 'N' ? '<span style="color:red; font-weight:bold;">재녹취 필요<span>' : 'O') + pTagClosed;
		$('#'+stepNamePopupId).append(stepName)
		$('#'+stepOkPopupId).append(stepOk);
	})
}
	
function gridCallIdSearch(callid){
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	var name;
	for(var i =0; i<gridIds.length; i++){
//		if( gridIds[i] == productInfoScriptGrid.cells(gridIds[i],6).getValue() ){
		if( gridIds[i] == callid ){
			name = productInfoScriptGrid.cells(gridIds[i],0).getValue();
			break;
		}
	}
	return name;
}

function gridGetCallKey(){
	var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
	for(var i =0; i<gridIds.length; i++){
		console.log(productInfoScriptGrid.cells(gridIds[i],6).getValue())
	}
}

function leadingZeros(n,digits){
    var zero = '';
    n = n.toString();
    
    if(n.length < digits){
        for (var i = 0; i < digits - n.length; i++)
            zero += '0';
    }
    return zero+n;
}

//자동 스크롤
function messageScroll() {
    var msgScr = document.getElementById("sttMsg");
    msgScr.scrollTop = msgScr.scrollHeight;
} 


function clearTaResult(){
	
	
}

function finalRecParameter(info) {
	var finalRecParameter = taParam;
	// 최종녹취완료시 빈값
	finalRecParameter.call_cmpl_yn='Y';
	finalRecParameter.rec_id = '';
	finalRecParameter.para_id = '';
	try{
		productInfoScriptGrid.getAllItemIds().split(",");
		finalRecParameter.prod_cd = rProductListPk;
	}catch (e) {
		finalRecParameter.prod_cd = taParam.prod_cd;
	}
	finalRecParameter.script_cd = '';
	finalRecParameter.script_dtl_cd = '';
	finalRecParameter.script_dtl_trgt = '';
	finalRecParameter = getRecTimeDate(finalRecParameter , info );
	finalRecParameter = setFinalCallType(finalRecParameter);
	return finalRecParameter;
}

function setFinalCallType(parameter){
	var code = params.PRD_CD;
	
	if(params.REC_YN == 'Y'){
		parameter.call_type = '3_1';
	}
	
	if( params.REC_YN == 'Y' &&(code == 'T0000000000003' || code == 'T0000000000004' || code == 'T0000000000001' || code == 'T0000000000002')){
		parameter.call_type = '1_1';
		return parameter;
	}
	//방카 상품권유 call_type
	if(params.BIZ_DIS == '4'){
		if( params.RCD_DSCD_NO == '02'){
			parameter.call_type = '2_2';
			return parameter;
		}
	}
	return parameter;
}

function getRecTimeDate(finalRecParameter,info){
	finalRecParameter["stnd_dt"] = info.recDate;
	finalRecParameter["rdg_ed_dtm"] = info.endTime;
	finalRecParameter["rdg_bgng_dtm"] = info.startTime;
	finalRecParameter["rdg_ptm"] = info.recTime;
	return finalRecParameter;
}

function faceMessageAppend(res) {
	var sttMsg = res.result
	if( sttMsg.trim().length == 0 ){
		return;
	}
	var html ='';
	var sttMsgDiv = $('#normalRecCommentBox');
	switch(res.rxtx){
		case 'R' :
			html+='<div class="scroll">'+'<p>'+'고객 : '+sttMsg+'</p>'+'</div>';
//			sttMsgDiv.append(html);
//			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
		case 'T' :
			html+='<div class="scroll">'+'<p style="color:red;">'+'상담원 : '+sttMsg+'</p>'+'</div>';
//			sttMsgDiv.append(html);
//			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
		case 'S' :
			html+='<div class="scroll">'+'<p style="color:red;">'+'TTS : '+sttMsg+'</p>'+'</div>';
//			sttMsgDiv.append(html);
//			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
	}
}

function clearRecStartInterVal(){
	recStartData.recStartTime = 0;
	recStartData.callid = '';
	clearInterval(recStartInterVal);
	console.log("clearInterVal Success");
}

function errorSttAction(){
	try{
		//일반녹취
		clearTaErrorTimeOut();
		clearRecStartInterVal();
		var gridIds = productInfoScriptGrid.getAllItemIds().split(",");
		SttTaUseFlag = false;
		if(productInfoScriptGrid != null && productInfoScriptGrid != undefined){
			webSocketStt.close();
//			stopEvent();
		}
	}catch (e) {
		//자유녹취
		
	}
}

function getDeptData() {
	var flag = false;
	if(params.OPR_NO != null && params.OPR_NO != undefined){
	var loadUrl = "/getDeptData/"+params.OPR_NO
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
					var data = jRes.resData.userInfo;
					console.log("===userInfo===");
					console.log(data);
					console.log("==============");
					flag = true;
					taParam["dept_lcls_cd"] = data.rbgCode;
					taParam["dept_mcls_cd"] = data.rmgCode;
					taParam["dept_scls_cd"] = data.rsgCode;
					taParam["dept_lcls_nm"] = data.rbgName;
					taParam["dept_mcls_nm"] = data.rmgName;
					taParam["dept_scls_nm"] = data.rsgName;
				}
			}
		});
	}
	return flag;
	
}

function caseMoreProductTaParam(){
	//다계좌시 마지막 스탭검사
	var lastStepId = productInfoScriptGrid.getAllItemIds().split(",").pop();
	//마지막 스탭과 현재 선택스탭이 같으면 로직시작
	if(lastStepId == productInfoScriptGrid.getSelectedRowId()){
		//상품갯수체크
		var codeArr = params.PRD_CD.split("|");
		//갯수에따른 arr 리턴
		var arrData = factoryMoreProductDetailParamData(codeArr);
		//
		factoryMoreProductDetailParam(arrData);
	}
}

	
function factoryMoreProductDetailSetting(detailList){
	for (var i = 0; i < detailList.length; i++) {
		if(i == 0){
			detailList[i].rScriptDetailPk = '99999971';
		}
		if(i == 1){
			detailList[i].rScriptDetailPk = '99999972';
		}
		if(i == (detailList.length-1)){
			detailList[i].rScriptDetailPk = '99999973';
		}
	}
	return detailList;
	
}
	
function  clearTaErrorTimeOut(){
	if(setTimeOutArr.length > 0){
		setTimeOutArr.forEach(i => {
			clearTimeout(i);
		});
	}
	setTimeOutArr = new Array();
}	

function factoryTaResonCase(){
	var logText = "[TaResonCase] ";
	if(taResultStepArr.length == 0 ) {
		console.log(logText+"taResultStepArr size [0]");
	}
}

function taErrorPopUp(info){
	var codeArr = null;
	var data = null;
	var stepName = productInfoScriptGrid.cells(info.id,0).getValue();
	
	//TA결과에 대한 디테일 가져오기
	taResultStepArr.forEach( i=> {
		if(i.gridId == info.id){
			data = i.taDetail;
		}
	})
	
	if(data != null){
		var codeArr = detailRuleCheckMethod(data);
		if(codeArr.length == 0){
			console.log("[taErrorPopUp] codeArr length == 0");
		}
	}
	var html = '';
	if(codeArr != null){
		html+= makerTaReasonHtml(codeArr);
	}
	
	$('.taErrorResultPopStepNameBody').empty();
	$('.taErrorResultPopStepNameBody').append(html);
	$('#taErrorResultPop').fadeIn(500);
}
	
	
function detailRuleCheckMethod(resultData){
	var codeArr = new Array();				//불완전판매 (대상인 데이터) 리스트
	var saleArr = resultData;	//불판완판 상세리스트
	saleArr.forEach( i => {
		var code = rullCheck(i);
		switch (code) {
		case '000':
			console.log('해당문구는 완판');
			console.log(i);
			break;
		case '999':
			console.log('해당문구는 불판인데 상세코드 누락');
			console.log(i);
			break;
		default:
			console.log('해당문구는 불판이며 사유표출가능');
			codeArr.push(i);
			break;
		}
	});
	return codeArr;
}
/*
 * resultCode = 000 (완판) 
 * resultCode = 001 (불판 사유 표출가능)
 * resultCode = 999 (상세코드 누락)
 * */
function rullCheck(data){
	var resultCode = '000';
	var saleState = data.imcmpl_sale_stat;	//완판여부
	
	if(saleState == 'Y' || saleState == 'E'){
		return resultCode;
	}
	//여기서부터 불판일떄
	//상세코드 누락일시
	try{
		var reasonArr = data.imcmpl_sale_cd.trim().split(',');
		return '001';
		
	}catch (e) {
		console.log("불판이며, 상세코드 누락");
		return '999';
	}
	return resultCode;
	
}
	

function makerTaReasonHtml(reasonArr){
	console.log('[makerTaReasonHtml] start');
	var html = '';
	
	var distinctFlagT = false;
	var distinctFlagS = false;
	var distinctFlagG = false;
	
	var distinctArr = [];
	
	//타입별 한개씩만 뽑기
	reasonArr.forEach(function(i,idx,array) {
		if(!distinctFlagT){
			if(i.script_dtl_trgt == '0'){
				distinctArr.push(i);
				distinctFlagT = true;
			}
		}
		if(!distinctFlagS){
			if(i.script_dtl_trgt == '1'){
				distinctArr.push(i);
				distinctFlagS = true;
			}
		}
		if(!distinctFlagG){
			if(i.script_dtl_trgt == '2'){
				distinctArr.push(i);
				distinctFlagG = true;
			}
		}
		
	});
	var compareIdx = 0;
	distinctArr.forEach(function(i,idx,array) {
		if(i.script_dtl_trgt == '0'){
			compareIdx++;
			return false;
		}
		if(idx > compareIdx){
			return false;
		}
		var noRullHtml ='';
		var bnedHtml = '';
		var notMatchHtml = '';
		var reasonCodeArr = i.imcmpl_sale_cd.split(',');
		
		//코드 중복건을 나누기위함
		var tempArr = [];
		reasonCodeArr.forEach(data => {
			var code = data;
			if(code == '01'){ code = '01'; }
			if(code == '03'){ code = '01'; }
			if(code == '04'){ code = '01'; }
			tempArr.push(code);
		});
		
		//중복제거
		var distinctTempArr = new Set(tempArr);
		
		if(distinctTempArr.length == 0){
			html+= "<div><strong>불완전판매 사유를 찾을 수 없습니다.</strong></div>"
		}
		
		var cnt = 0;
		
		var distinctArr2 = Array.from(distinctTempArr);
		
		for (var i2 = 0; i2 < 1; i2++) {
			var code = distinctArr2[i2];
			switch (code) {
			case '01': noRullHtml+= makerNoRullMatchHtml(i);break;
			case '02': bnedHtml+= makerBnedHtml(i);			break; 
			case '03': notMatchHtml+= makerNotMatchHtml(i);	break;
			case '04': makerNoRullDataHtml(i);				break;
			}
			cnt++;
			if(cnt>0){
				break;
			}

		}
		/*
		distinctArr2.forEach( i2 => {
			var code = i2;
			switch (code) {
			case '01': noRullHtml+= makerNoRullMatchHtml(i);break;
			case '02': bnedHtml+= makerBnedHtml(i);			break; 
			case '03': notMatchHtml+= makerNotMatchHtml(i);	break;
			case '04': makerNoRullDataHtml(i);				break;
			}
//			cnt++;
//			if(cnt>0){
//				break;
//			}
//			
		})
		*/
		html+= noRullHtml + bnedHtml + notMatchHtml;
//		if(idx != array.length-1){
//			html+= '<hr style="width: 100%;margin-bottom: 13px;" color="lightgray">';
		
		});
//		}
	console.log(html);
	return html;
	
}

//룰없음
function makerNoRullMatchHtml(data){
	var htmlOpen = '<div class="NoRullMatch taResultReason">';
	var close = '</div>';
	var typeStr = '<div>'+typeAnwerStrRes(data.script_dtl_trgt)+' 을 확인하시기 바랍니다.'+'</div>';
	console.log('[makerNoRullMatchHtml]')
	console.log(data)
	console.log('룰 매칭 데이터가 없음');
	
	return htmlOpen + typeStr + close ;
	
}
function makerBnedHtml(data){
	var htmlOpen = '<div class="BnedHtml taResultReason">';
	var close = '</div>';
	var typeStr = '<div>'+typeAnwerStrRes(data.script_dtl_trgt) +' 을 확인하시기 바랍니다.'+'</div>';
	
	var reasonHtml = '<div> ex ) 아니오 , 거부 , 거절 등</div>';
	
	console.log('[makerBnedHtml]')
	console.log(data)
	console.log('해당 구간에는 금칙어가 포함되어있습니다.   :' +data.matc_bned_dec);
	
	return htmlOpen + typeStr + reasonHtml + close;
}
function makerNotMatchHtml(data){
	var htmlOpen = '<div class="NotMatchHtml taResultReason">';
	var close = '</div>';
	var typeStr = '<div>'+typeAnwerStrRes(data.script_dtl_trgt)+'을 확인하시기 바랍니다.'+'</div>';
//	var reasonHtml = '<div> 필수어 : '+data.not_matc_necs_dec+'</div>';
	console.log('[makerNotMatchHtml]')
	console.log(data)
	console.log('필수어가 매칭되지 않았습니다.   :' +data.not_matc_necs_dec);
	
	return htmlOpen + typeStr  + close;
}
function makerNoRullDataHtml(data){
	var htmlOpen = '<div class="NoRullDataHtml taResultReason">';
	var close = '</div>';
	var typeStr = '<div>'+typeAnwerStrRes(data.script_dtl_trgt)+'을 확인해 주시기 바랍니다.'+'</div>';
	console.log('[makerNoRullDataHtml]')
	console.log('룰매칭정보없음')
	
	return htmlOpen + typeStr + close;
}
	


function typeAnwerStrRes(type){
	var html = '';
	var result = '';
	
	switch (type) {
	case '0': result='직원 질문 구간';	 	break;
	case '1': result='직원 질문 구간';	break;
	case '2': result='고객 답변 구간';	break;
	}
	
	html = '<strong style="color:black;!important;font-size: 15px;">'+result+'</strong>'
	
	return html;
}




//직원 질문구간 금칙어가 발견되었습니다.
//금칙어 : {}

//직원 질문구간 필수어가 누락되었습니다.
//필수어 : {}

//고객 답변구간 금칙어가 발견되었습니다.
//금칙어 : {}

//고객 답변구간 필수어가 누락되었습니다.
//필수어 : {}











