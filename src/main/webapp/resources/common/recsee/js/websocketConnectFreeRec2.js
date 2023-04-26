var wsURI = "wss://aiprecsyst.woorifg.com:18881/ai";
var webSocket;
var tttt;
var retryCount=0;
var nowClientVersion = '2.7';
var micError = false;
var	noResponse=false;
var expectedtimer;
var expectedState='';
var micErrorTimeout;
var enginExpectedTimer;
var micExcpectedTimer;
var recStopResTimerArr = [];
var netError=false;

// 녹취시작이벤트에 대한 플레그 ( 마이크 접선 | 불륨장애가 한번이라도 수신한 경우 false)
var allFlag = true;

// 87/003/ 처리 플레그
var edgeSoundFlag = false;

//ta결과 예외처리 플레그
// 마이크 접선
var micErrorFlag = false;
// 불륨장애
var soundErrorFlag = false;
// 엔진 & 마이크 정상전문이 안올때 
var micFlag2 = true;
var enginFlag2 = true;

var micFlag = false;
var enginFlag = false;

/*
 * code : 0	  	//에러 없이 녹취 정상종료 코드 
 * code : 1		// 94/002 , 50/002  에대한 마이크 불량 코드
 * code : 2		// 90/002 스피커에 대한 불량 코드
 * code : 3		// 추후..
 * */
var resultCode = 0;

var recStart = {
	"head" : "RECSEE",
	"code" : 50,
	"systemid" : "CTR",
	"repoid" : "CR",
	"tenantid" : "WRBK",
	"trxid" : "",
	"branchnumber" : 0,
	"devicenumber" : 0,
	"ipaddress" : 0,
	"etcparamlength" : 0,
	"etcparam" : ""
}
var recstop = {
	"head" : "RECSEE",
	"code" : 51,
	"systemid" : "CTR",
	"repoid" : "CR",
	"tenantid" : "WRBK",
	"trxid" : "",
	"branchnumber" : 0,
	"devicenumber" : 0
}
/*var etcparm = {
	"requestnum" : "",
	"newcallid" : "",
	"callid1" : "",
	"callid2" : ""
}*/
/*var merge = {
	"head" : "RECSEE",
	"code" : 81,
	"systemid" : "CTR",
	"repoid" : "CR",
	"tenantid" : "WRBK",
	"trxid" : 0,
	'etcparamlength' : JSON.stringify(etcparm).length,
	"etcparam" : etcparm
}*/

function webSocketConnect() {
	webSocket = new WebSocket("ws://127.0.0.1:14100/EventListner");

	webSocket.onopen = function(evt) {
		onOpen(evt);
	};
	webSocket.onclose = function(evt) {
		onClose(evt);
	};
	webSocket.onmessage = function(evt) {
		onMessage(evt);
	};
	webSocket.onerror = function(evt) {
		onError(evt);
	};

}

function onOpen(evt) {
	console.log("onOpen : " + evt);

}

function onClose(evt) {
	console.log("onClose : " + evt);
	 /*
	  * 아무것도 못하게 막는 팝업 해야함. 
	  * */
}

var recStartTimeOut = null;

function onMessage(evt) {
	console.log("onmessage : " + evt);
	try{
		json = JSON.parse(evt.data)
		
		if (json.outputtext != undefined)
			params = JSON.parse(json.outputtext);
			var version = json.version;
			callKey = params.RCD_KEY;
			changeNecs();
			progress.on();
			$('#holdon-message').html('대면녹취 시스템 최적화 중입니다. 잠시만 기다려주세요.');
			//params에 버전이 파라미터로 들어올때 이벤트 생성예정
			/*
			 * 
			 * */
			setTimeout(() => {
				
				if(version != clientVersion || version == undefined){
					console.log('클라이언트 버전 미일치 : '+new Date());
					progress.off();
					//클라이언트 버전 미 일치
					errorOccurVersion();
					sendmsg("recStop",scriptCallKey);
				}
			}, 2000);
		
			//	 87/003/ 에대한 확인처리 timeout (15초)
			edgeSoundCheckTimeout = setTimeout(() => {
				progress.off();
				if(!edgeSoundFlag){
					//팝업오픈
					console.log('edgeSoundFlag : '+new Date());
					//녹취단말기의 연결상태를 확인하시고, 볼륨을 60이상 유지해주세요.
					errorOccurVolume();
					sendmsg("recStop", scriptCallKey);
					noResponse=true;
				}
			}, 30000);
		
		
//			if(params.REC_YN =='N'){
//				goFreeRecPost();
//				return;
//			}
		
		switch (json.code) {
		case "60":
			init();
			break;
		}
	}catch(e){
		switch (evt.data) {
		
		case '51/003/':
			console.log('51/003/ res  time : '+new Date());
			recstopTimerArrClear();		//recstop타임아웃 클리어
			console.log('clearTimeout(recStopResTimer)');
			switch (resultCode) {
			case 0:
				progress.off();
				console.log("resultCode :"+ resultCode +" 정상녹취종료")
				break;
			case 1:
				//모달 이벤트용
				errorOccurMic();
				progress.off();
				console.log("resultCode :"+ resultCode +" 정상녹취종료")
				break;
			case 2:
				errorOccurVolume();
				progress.off();
				console.log("resultCode :"+ resultCode +" 정상녹취종료")
				break;
			case 4:
				$('#holdon-message').html('창을 닫지 말고 기다려주세요. 내용 분석중입니다.');
				console.log("resultCode :"+ resultCode +" 정상녹취종료");
				break;
			}
			
			break;
		case '50/001/':
			if(expectedState!='retry' && expectedState!='micError')
				expectedState='50/003/';
			break;
		
		case '50/003/':
			console.log('50/003/ res');
			enginFlag = true;
			enginFlag2 = false;
			if(allFlag){

				console.log('50/003/ freeRecStart')
				recResult = true;
				micError = false;
				progress.off() 
				try {
					recordTimerOn()
					waveDisplayOn()
				} catch (e) {
	
				}
//			recStartFunction();
//			scriptListPlay();
			//scriptCallKeyUpdate();
			
				progress.off();
				startTimer();
				expectedState='';
				clearTimeout(expectedtimer)
				clearTimeout(settimeout)
				
				//최서영
				if(recStartTimeOut != null){
					clearTimeout(recStartTimeOut);
					recStartTimeOut = null;
				}
				// ****
				clearTimeout(enginExpectedTimer);
				clearTimeout(micExcpectedTimer);
				
			}
			break;
			
		case '87/003/':
			console.log('87/003/ response :'+new Date())
			progress.off()
			edgeSoundFlag = true;
			clearTimeout(edgeSoundCheckTimeout);
			console.log("clearTimeout(edgeSoundCheckTimeout);")
			break;
			
		case '94/002/':
		case '50/002/':
			resultCode = 1;
			console.log("94/002/  | 50/002 Response!!!!!!!!!")
			if(!micError) {
				allFlag = false;
				micErrorFlag = true;
				micError = true;
				clearTimeout(micExcpectedTimer);
				// 재생 멈춤
				// stop 이벤트 보내기
				micErrorTimeout = setTimeout(() => {
					sendmsg("recStop", scriptCallKey)
					noResponse=true;
				}, 1000);
			}
			break;
		case '90/002/' :
			resultCode = 2;
			
			allFlag = false;
			soundErrorFlag = true;
			console.log('90/002 \nPC불륨 60%아래!!');
			clearTimeout(micExcpectedTimer);
			if(!micError){
				setTimeout(() => {
					sendmsg("recStop", scriptCallKey)
				}, 1000);
				noResponse=true;
			}
			progress.off();
			break;
		default:
			break;
		}
	}
	
	
	

}

function onerror(evt) {
	console.log("onerror : " + evt);
}

function doSend(message) {
	webSocket.send(message);
}

function sendmsg(commond, key, keyarray) {
	switch (commond) {
	case "recStart":
		recStart.trxid = key;
		recstop.trxid = key;
		
		setTimeout(function(){
			$('#holdon-message').html('대면녹취 준비상태를 확인중입니다.\n잠시만 기다려주세요.');
			console.log('[sendmsg] '+key+" recstart send");
			doSend(JSON.stringify(recStart))
				
			if(expectedState =='retry' || expectedState == 'micError'){
				
			}else{
				expectedState ='50/001/'
				
			}
			
			if(allFlag) {
				/*
				enginExpectedTimer = setTimeout(() => {
					console.log('enginExpectedTimer')
					if(!micFlag){
						console.log('enginExpectedTimer logic start')
						micFlag2 = true;
						progress.off();
						stopEvent();
						$('#newMicErrorPop').show();
					}
				}, 12500);
				*/
				
				//50/003 안올시 타임아웃(네트워크)
				micExcpectedTimer = setTimeout(() => {
					console.log('micExcpectedTimer')
					if( !enginFlag ){
						console.log('micExcpectedTimer logic start')
						enginFlag2 = true;
						progress.off();
						//네트워크 연결 오류
						if(!netError){
						console.log('50/003 not Received')
						errorOccurNet();
						netError=true;
						noResponse=true;
						sendmsg("recStop", scriptCallKey);
						}
					}
				}, 10000);
			}
			
			/*
			expectedtimer = setTimeout(() => {
				console.log('시작:'+expectedtimer)
				expect('start')
			}, 5000);
			*/
			
		},500);

		break;

	case "recStop":
		recStopCheckFlag = false;
		recStart.trxid = key;
		recstop.trxid = key;
		doSend(JSON.stringify(recstop));
		console.log('[sendmsg] '+key+" recstop send");
		
//		delayRecStopCheck(key);				//녹취종료 응답 판단 함수
		
		clearTimeout(expectedtimer)
		clearTimeout(settimeout)
		/*if(expectedState !='retry'){
			progress.off()
		}*/
		expectedState=''
		enginFlag = false;
		micFlag = false;
		taReqFlag = false;
		sttRecStart = false;
		
		// 51/003 이 안들어 왔을때 처리
		if(!netError){
			recStopResTimerArr.push( setTimeout(() => {
				console.log("51/003/ 이 들어오지 않아 이벤트 처리를함.")
				//에러 팝업 녹취장애?
				errorOccurNet();
				netError=true;
				sendmsg("recStop", scriptCallKey);
				progress.off();
			}, 10000));
		}
		break;
		
	case "merge":
		var mergeParam = {
			"count" : "",
			"newCallID" : "",
			"callid1" : "",
			"callid2" : ""
		};

		mergeParam.count = (keyarray.length).toString();
		mergeParam.newCallID = key;

		for (var i = 0; i <= keyarray.length; i++) {
			mergeParam['callid' + (i+1)] = keyarray[i];
		}
		console.log(mergeParam)
		return mergeParam;
		//doSend(JSON.stringify(merge))
			
		break;
	case "pdfdown" :
        if(params.REC_YN == undefined || params.REC_YN == null){
            params.REC_YN = 'Y';
        }
		var pdfParam = {
			"head" : "RECSEE",
			"code" : 80,
			"pdf" : params.RCD_KEY,
			"type" : params.REC_YN
		}
		
		if( params.PRD_CD == 'T0000000000001' ||
			params.PRD_CD == 'T0000000000002' ||
			params.PRD_CD == 'T0000000000003' ||
			params.PRD_CD == 'T0000000000004' ){
			
		}else{
			doSend(JSON.stringify(pdfParam));
		}
		break;
	default:
		break;
	}

}

function expect(comm){
	switch (comm) {
	case "start":
		console.log("expectedState0::"+expectedState)
		if(expectedState!='' && expectedState != 'micError'){
			console.log("expectedState1::"+expectedState)
			if(expectedState !='retry' && expectedState != 'micError'){
				console.log("expectedState2::"+expectedState)
				expectedState ='retry'//일단 두번넣자..
				sendmsg("recStop", scriptCallKey)
				settimeout = setTimeout(function(){
					expectedState ='retry'
					createCallKey();
					sendmsgStt("login");
					progress.on()
					$('#holdon-message').html('대면녹취 준비상태를 확인중입니다.\n잠시만 기다려주세요.');
					sendmsg("recStart", scriptCallKey)
				},5000)
			} else if(expectedState =='retry' || expectedState == 'micError'){
				console.log("expectedState3::"+expectedState)
				progress.off()	
				waveDisplayOff()
				clearInterval(interval);
				resetTimer()
				if(!enginFlag && micFlag){
					errorOccurMic();
					setTimeout(() => {
						sendmsg("recStop", scriptCallKey)
					}, 2000);
					noResponse=true;
					return;
				}
				if((!micFlag && enginFlag) || (!micFlag && !enginFlag)){
					//clearTaErrorTimeOut();
					console.log('팝업을 띄우시오');
					//마이크 연결확인 에러
					errorOccurMic();
					setTimeout(() => {
						sendmsg("recStop", scriptCallKey)
					}, 2000);
					noResponse=true;
					/*					
					progress.on();
					$('#holdon-message').html('대면녹취 준비상태를 확인중입니다.\n잠시만 기달려주세요.');
					expectedState = 'micError';
					console.log('expect : micFlag 가 정상이 아닌 상태이기때문에 10초후 녹취시작 재요청')
					clearTaErrorTimeOut()
					setTimeout(() => {
						sendmsg("recStart", scriptCallKey)
					}, 300);
					*/
				}
			}
			
		}else if(expectedState == 'micError'){
			progress.off();
			//clearTaErrorTimeOut()
			console.log('팝업을 띄우시오');
			//마이크 연결확인 에러
			errorOccurMic();
			setTimeout(() => {
				sendmsg("recStop", scriptCallKey)
			}, 2000);
			noResponse=true;
		}
		
		break;

	default:
		break;
	}
	
}

function webSocketClose() {
	webSocket.close()
}

function recstopTimerArrClear(){
	console.log('recstopTimerArrClear()');
	recStopResTimerArr.forEach( i => {
		clearTimeout(i);
	})
}