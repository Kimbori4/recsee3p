var wsURI = "ws://localhost:12969";	// STT (Ai connect모듈)
var webSocketStt;

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
}

function onCloseStt(evt) {
	console.log("onCloseSTT : " + evt);
	console.log(evt);
	//ai conn 모듈이 종료되었을때
	SttTaUseFlag = false;
	//TA연결실패
	//녹취중 끊김
	if(enginFlag){
		$('#errorAICloseRecMiddle').show();
		sendmsg("recStop", scriptCallKey);
		resetTimer()
	}else{
		$('#errorAICloseRecStartBefore').show();
		resetTimer()
		setTimeout(() => {
			resetTimer()
		}, 2000);
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
				if(!noResponse){
					taFlag=true;					//TA결과가 왔으니 체크 타임아웃클리어 및 플레그 전환
					setTimeOutArr.forEach(i => {
						clearTimeout(i);
					})
					taResultResponseEvent(json);
				}
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
		}else{
			sendmsg("recStop", scriptCallKey);
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
			setTimeout(function(){
				console.log('(doSendStt) : login req');
				doSendStt(JSON.stringify(loginParam))
			},500);
		}
		break;
	case "taReq":
		console.log('websocket STT (TA) req');
		taReq.callid = scriptCallKey;
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
 