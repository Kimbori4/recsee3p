var wsURI = "ws://localhost:12969";	// STT (Ai connect모듈)
var webSocketStt;
// 0 : 오류
// 1 : 붙음
// 2 : 장애
var stateCode = 0;

var resCode;
var resHeader;

var freeRecFlag = false;
var finalRecTaResultFlag =false;

var SttTaUseFlag = true;

var recStopBtnFlag = false;

function SttWebSocketConnect() {
	
	webSocketStt = new WebSocket("wss://aiprecap.woorifg.com:18881/ai");

	webSocketStt.onopen = function(evt) {
		onOpenStt(evt);
	};
	webSocketStt.onclose = function(evt) {
		onCloseStt(evt);
	};
	webSocketStt.onmessage = function(evt) {
		onMessageStt(evt);
	};
	webSocketStt.onerror = function(evt) {
		onerrorStt(evt);
	};

}

function onOpenStt(evt) {
	console.log("STT WS Open!!!!!")
	stateCode = 1;
}

function onCloseStt(evt) {
	console.log("onCloseSTT : " + evt);
	console.log(evt);
	progress.off();
	//ai conn 모듈이 종료되었을때
	SttTaUseFlag = false;
	var flag = freeRecCheck();
	if(productInfoScriptGrid != null && productInfoScriptGrid != undefined){
		//녹추 중일때 아닐때 다른이벤트
		if(!recstart){
			if(!manualType){
				if(!taResultResFlag){
					errroPopUpdateText("불완전 판매 결과값을 받아올 수 없습니다.","일반녹취로 전환합니다.","","");
					clearTaErrorTimeOut()	//TA검증 타임아웃 클리어
				}else{
					errroPopUpdateText("현재 TA시스템에 문제가 발생하여,","일반녹취로 전환합니다.","이후의 녹취는 TA분석 결과가 제공되지 않습니다.","해당 구간을 다시 녹취하세요.");
				}
			}else{
				setTimeout(() => {
					errroPopUpdateText("수동녹취는 불완전판매 결과값을 받지 않습니다.","일반녹취로 전환합니다.","","");
					SttTaNotUseGridFactory();
				}, 1500);
			}
			SttTaNotUseGridFactory();
		}else{
			//녹취 중 TA 결과를 못받온게 왓을시
			if(!taResultResFlag){
				errroPopUpdateText("불완전 판매 결과값을 받아올 수 없습니다.","녹취가 중단되며 일반녹취로 전환합니다.","해당구간을 다시 녹취하세요.");
			}else{
				errroPopUpdateText("현재 TA시스템에 문제가 발생하여,","일반녹취로 전환합니다.","이후의 녹취는 TA분석 결과가 제공되지 않습니다.");
			}
			stopEvent('1','e');		//recStop 이벤트
			clearTaErrorTimeOut()	//TA검증 타임아웃 클리어
			SttTaNotUseGridFactory();	//그리드 전환
		}
	}
}

function onMessageStt(evt) {
	console.log("onmessage (STT) : " + evt);
	try{
		json = JSON.parse(evt.data)
		
		// 명령코드
		if(json.code != undefined){
			resCode = json.code;
		}
		// 헤더
		if(json.header != undefined){
			resHeader = json.header;
		}
		if(resHeader == 'RSAI'){				// STT & TA 기본 응답 헤더
			console.log(resCode);
			//명령코드 별 실행
			switch (resCode) {
			//로그인요청 Res
			case "login":
				sttLoginEvent(json);
				break;
			//로그아웃  Res
				case "logout":
				sttLogoutEvent(json);
				break;
			//녹취시작 Res
			case "recstart":
				sttRecStartResEvent(json);
				break;
			//Ta 요청 상태 Res
			case "tareq":
				taResponseEvent(json);
				break;
			//녹취 종료 Res
			case "recstop":
				sttRecStopResEvent(json);
				break;
				//녹취 최종 종료 Res
			case "finalrec":
				finalRecStopEvent(json);
				break;
				
			}
		}else if (resHeader == 'RSTA') {			// TA 결과 헤더
			//명령코드 별 실행
			switch (resCode) {
			//Ta 결과 Res
			case "tarst":
				taResultResponseEvent(json);
				break;
			}
		}else if (resHeader == 'RSSTT') {			// STT 결과 헤더
			//명령코드 별 실행
			switch (resCode) {
			//Ta 결과 Res
			case "sttrst":
				sttResultResponseEvent(json);
				break;
			}
		}
	}catch(e){
	}
}

function onerrorStt(evt) {
	console.log("onerror : " + evt);
}

function doSendStt(message) {
	if(webSocketStt != undefined && webSocketStt.readyState == 1){
		console.log('send ok');
		webSocketStt.send(message);
	}else{
		console.log('send no');
		if(webSocketStt != undefined){
			webSocketStt.close();
			
		}
	}
}


function sendmsgStt(commond, key) {
	switch (commond) {
	case "login":
		//파라미터 셋팅
		console.log('websocket STT (login) req');
		addLoginParamCallKey();
//		loginParam["taparam"] = taParam;
		if(loginParam != null && loginParam != undefined){
			console.log("(login) param");
			console.log(loginParam);
			//로그인요청
			console.log('(doSendStt) : login req');
			doSendStt(JSON.stringify(loginParam))
		}
		break;
	case "taReq":
		console.log('websocket STT (TA) req');
		taReq["callid"] = scriptCallKey;
		taReq.taparam = taParam;
		//ta요청
		console.log('(doSendStt) : TA req');
		doSendStt(JSON.stringify(taReq));
		break;
	//merge전 최종 녹취 요청
	case "finalrec":
		console.log('websocket STT (finalRec) req');
		finalRecParam.taparam = finalRecParameter(key);
		finalRecParam.callid = params.RCD_KEY;
		console.log('(doSendStt) : finalRecParam');
		doSendStt(JSON.stringify(finalRecParam));
		break;
	default:
		break;
	}

}

function closeAboutParams(params){
	var type = params.BIZ_DIS;
	var productCode = params.PRD_CD;
	
	var flag = true;
	
	if(type == 4){
		if(params.RCD_DSCD_NO != null && params.RCD_DSCD_NO != undefined){
			if(params.RCD_DSCD_NO == '01'){
				webSocketStt.close();
				flag =false;
			}
		}
	}
	
	if(productCode == 'T0000000000001' || productCode == 'T0000000000002'){
		webSocketStt.close();
		flag =false;
	}
	if(manualType){
		webSocketStt.close();
		flag =false;
	}
	
	if(productCode == 'T0000000000001' || productCode == 'T0000000000002'){
		webSocketStt.close();
		flag =false;
	}
	
	if(flag){
		console.log("closeAboutParams() : close STT WebSocket!");
	}
	
	return flag;
}


function freeRecCheck(){
	var result = 0;
	try{
		productInfoScriptGrid.getAllItemIds().split(",");
		result = 1;
	}catch (e) {
		result = 0;
	}
	return result;
}