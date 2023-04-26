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
	if(manualType =='N'){
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
	
	if(params.BIZ_DIS == 2 &&  params["TA_TYPE"] =='2'){
		prdCd = '00612676';
	}
	if(params.BIZ_DIS == 1 &&  params["TA_TYPE"] =='2'){
		prdCd = '00612677';
	}
	
	taParam["prod_cd"] = prdCd;
	
	taParam["call_purp_scls_cd"]= prdCd + callPurpSclsCd;
	taParam["script_dtl_trgt"] = "1";
	

}

function createCallKey(){
	scriptCallKey = new Date().getTime()+params.OPR_NO;
	
}
function createCallKeyMerge(){
	scriptCallKey = new Date().getTime()+params.OPR_NO+"_merge";
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
		taParam["para_id"] = "00000001"
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
	if(groupInfo == null || groupInfo == undefined){
		prodCd = leadingZeros(rProductListPk,8);
	}else{
		prodCd = groupInfo.groupPk;
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
//		arrRecStartTimeoutClear();
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
//		setTimeout(() => {
//			taReqSendMsg();
//		}, 1500);
		
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
	var callId = res.callid;
	console.log("["+callId+"] taResultResponseEvent()");
	console.log(res);
	if(taResult=='Y' || taResult=='N') {
		$.post(contextPath+'/sendTaResult.do',{callId:callId, taResult:taResult},
				function(res){
		},'json');
	}else {
		$.post(contextPath+'/sendTaResult.do',{callId:callId, taResult:'F'},
				function(result){
		},'json');
	}
	
//	taResultEvent(res);
	
	//마지막구간일때 팝업오픈
	if(RecEndFlag){
		progress.off();
		RecEndFlag = false;
		$('#taResultSeeBtn').show()
		TaPopupSetting();
		taResultPopUpFlag = true;
		$('#taResultLastPop').fadeIn(400);
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
		            console.log(gridCallId)
					if(gridCallId.length == 0){
						continue;
					}
					if(gridCallId == callId){
		                console.log("ok")
						data["gridId"] = gridIds[i];
						data["result"] = res.result;
						taResultStepArr.push(data);
						break;
					}
			}
		    console.log(data)
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
		console.log('this product is freeRec');
		//TA결과가 왓다는 플레그
		if(!noResponse) {
//			noResponse=true;					//네트워크 에러 결과  true : 정상 ,  
//			freeRecTaResultFlag = true;			//자유녹취 TA받은거
//			progress.off();
//			freeRecTaFlag=true;
//			callKeyArr = [];
//			callKeyArr.push(scriptCallKey);
//			setTimeout(() => {
//				mergeFile(callKeyArr);
//			}, 2000);
//			taResultInfo(res);					//TA결과 넣어주기
//			checkInfo();						//TA결과창 띄우기
		}
	}

}

//이미지 경로 설정할것
//TA 결과 성공 액션
function taResultSuccessAction(id){
//	common/recsee/images/project/icon/CircleGray.png
	var imgSrc = "<div id='recPlay' style='position: relative; top: 2px\'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleBlue.png' style='width:16px;' /></div>"
	console.log('s');
	console.log(id);
	productInfoScriptGrid.cells(id,4).setValue(imgSrc)
}
//TA 결과 실패 액션
function taResultFailedAction(id){
	var imgSrc = "<div id='recPlay' style='position: relative; top: 2px\'><img id src='"+resourcePath+"/common/recsee/images/project/icon/CircleRed.png' style='width:16px;' /></div>"
	console.log('f');
	console.log(id);
	productInfoScriptGrid.cells(id,4).setValue(imgSrc)
	//TA결과시 마지막구간이 아닐때 팝업오픈이벤트
	if( !RecEndFlag ){
		var stepName = productInfoScriptGrid.cells(id,0).getValue();
		// 팝업오픈이벤트
		taResultPop(stepName);
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


//sttRecStop이벤트
function sttRecStopResEvent(res) {
	recStartResult = res.result;
	switch (recStartResult) {
	case "success":
		console.log("(SttLoginEvent) RecStop success");
		
		/*
		 * recStopBtnFlag => 기본값 false (녹취중단 버튼을 누를경우를 대비) 
		 * 자유녹취는 버튼이 없으니 항상 이 로직을 탐
		 * */
		/*
		if(!recStopBtnFlag){
			var callid = res.callid;
			//STT장애상황 로직
			if(recStartData.recStartTime < 1500 ){
				//STT장애상황일때 실행함수 (자유녹취 , 일반녹취 구분은 내부)
				try{
					if(recStartData.callid == callid){
						productInfoScriptGrid.getAllItemIds().split(",");
						alert("예기치 못한 오류가 발생하였습니다.\n일반녹취로 전환합니다.");
						errorSttAction();
					}
				}catch (e) {//자유녹취일때 예외상황 함수
					if(freeRecStop==false && recStartTime <1500) {
//						errorOccur();
						if(allFlag){
							errorInfoNet();
							sendmsg("recStop",scriptCallKey);
							noResponse=true;
						}
					}
					
				}
			}else{
				console.log("rectime > 5");
			}
		}
		*/
		
		/*
		 * recStopBtnFlag => 기본값 false (녹취중단 버튼을 누를경우를 대비) 
		 * 자유녹취는 버튼이 없으니 항상 이 로직을 탐
		 * */
		//TA장애상황로직
		/*
		if( !recStopBtnFlag ){
			var taErrorTimeOut = setTimeout(() => {
				console.log("TA ERROR TEST START");
				var callId = res.callid;
				try{
					productInfoScriptGrid.getAllItemIds().split(",");
					
					if(taResultStepArr.length == 0){
						console.log("taResultStepArr size == 0 Error")
						alert("불완전 판매 결과값을 받아올 수 없습니다.\n일반녹취로 전환합니다. 녹취를 계속진행해 주세요.");
						errorSttAction();
						return;
					}
					var findResult = false;
					var findIdx = 0
					for (var i = 0; i < taResultStepArr.length; i++) {
						var gridCallId = productInfoScriptGrid.cells(taResultStepArr[i].gridId,6).getValue();
						if(gridCallId == callId){
							findResult = true;
							findIdx = i;
						}else{
							continue;
						}
					}
					
					if( !findResult ){
						console.log("해당 스탭의 TA결과값을 찾을 수 없어 에러가남");
						alert("불완전 판매 결과값을 받아올 수 없습니다.\n일반녹취로 전환합니다.\n진행중인 녹취가 취소됩니다.");
						errorSttAction();
						return;
					}
				}catch (e) {
					//자유녹취일때 예외상황 함수
					//TA결과가 오지 않을시 실행 -> 오면 타임아웃클리어됨
					if( !taFlag && freeRecStop ) {
//						retryButton();
						//재녹취해야한다는 파업해야함 + 모든 타임아웃 클리어
						progress.off()
						//TA 결과 받아오기 실패
						errorInfoRetry();		//분석결과 팝업?
					}
				}
			}, 30000);
		}
		setTimeOutArr.push(taErrorTimeOut);
		*/
		recStopBtnFlag = false;
		
		recStartData.callid = '';
		recStartData.recStartTime = 0;
		
		break;
	case "fail":
		console.log("(SttLoginEvent) RecStop failed");
		var reason = JSON.stringify(res.reason);
		console.log("WHY ? "+reason);
		alert(reason);
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
	if(failStepArr == null || failStepArr == undefined || failStepArr.length == 0) return false;
	
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
	finalRecParameter.rec_id = taParam.rec_id;
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
		parameter.call_type = '3';
	}else if( params.REC_YN == 'Y' &&(code == 'T0000000000003' || code == 'T0000000000004' || code == 'T0000000000001' || code == 'T0000000000002')){
		parameter.call_type = '1';
	}
	if(params.REC_YN == 'N' && params.TA_TYPE == '1'){
		parameter.call_type = '2_1';
	}
	if(params.REC_YN == 'N' && params.TA_TYPE == '2'){
		parameter.call_type = '3_2';
	}
	
	//양수도 상품설명시 콜타입셋팅
	if(manualFlag){
		parameter.call_type = '3_2';
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
			sttMsgDiv.append(html);
			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
		case 'T' :
			html+='<div class="scroll">'+'<p style="color:red;">'+'상담원 : '+sttMsg+'</p>'+'</div>';
			sttMsgDiv.append(html);
			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
		case 'S' :
			html+='<div class="scroll">'+'<p style="color:red;">'+'TTS : '+sttMsg+'</p>'+'</div>';
			sttMsgDiv.append(html);
			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
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

function oldClientCheck(params){
	var flag = false;
	var recYn = params.REC_YN;
	var taType = params.TA_TYPE;
	
	// 녹취대상여부 체크 + TA_TYPE 체크
	if(recYn == 'Y' && taType != '1' && taType != '2'){
		flag = false;
	}
	
	if(recYn == 'N' && (taType == '1' || taType == '2')){
		flag = true;
	}
	
	if(taType == '1'){
		if(params.PRD_CD == null || params.PRD_CD == undefined ){
			flag = true;
		}
		if(params.PRD_CD.trim().length == 0){
			flag = true;
		}
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


