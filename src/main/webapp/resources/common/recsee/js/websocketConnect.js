var wsURI = "ws://localhost:12969";
var webSocket;
var tttt;
var expectedtimer;
var expectedState='';
var retryCount=0;
var micError = false;
var micErrorTimeout;
//2.6 ㄱㄱ
//var nowClientVersion = '2.7';

var enginExpectedTimer;
var micExcpectedTimer;

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

//녹취 중지 응답 플레그
/*
 * 0 :	현재 종료
 * 1 :	정상 녹취구간에 대한 종료 코드
 * 2 :	에러로인한 종료코드
 * */
var recStopResFlag = 0;

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

var timeoutArr = [];

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
	$('.modal').hide();
	$('#webSocketClosePop').show();
	
}

var recStartTimeOut = null;

function onMessage(evt) {
	console.log("onmessage : " + evt);
	try{
		json = JSON.parse(evt.data)
		
		if (json.outputtext != undefined)
			params = JSON.parse(json.outputtext);
			var version = json.version;
			
			progress.on();
			$('#holdon-message').html('대면녹취 시스템 최적화 중입니다. 잠시만 기다려주세요.');
			//params에 버전이 파라미터로 들어올때 이벤트 생성예정
			/*
			 * 
			 * */
			setTimeout(() => {
				
				if(version != clientVersion  || version == undefined){
					console.log('클라이언트 버전 미일치 : '+new Date());
					progress.off();
					$('#micErrorPop').show();
					clearTimeout(edgeSoundCheckTimeout);
				}
			}, 2000);
		
			//	 87/003/ 에대한 확인처리 timeout (15초)
			edgeSoundCheckTimeout = setTimeout(() => {
				progress.off();
				if(!edgeSoundFlag){
					//팝업오픈
					console.log('edgeSoundFlag : '+new Date());
					errroPopUpdateText('대면녹취시스템 최적화에 실패하였습니다.','창을 종료 후 녹취를 다시 시작하세요.','','')
					$('#errorModalMicBtn').hide();
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
			console.log('51/003/ res Time : '+new Date());
			console.log('현재 recStopResFlag : '+recStopResFlag);
			switch (recStopResFlag) {
			case 0:
				console.log("recStopResFlag = 0 인데 51/003/ 을 받음");
				arrTimeoutClear()
				progress.off();
				break;
			case 1:
				recStopEvent();
				arrTimeoutClear()
				break;
			case 2:
				console.log("현재 error이후 51/003/ 을 받음");
				arrTimeoutClear()
				progress.off();
				break;
			case 3:
				console.log("종료 버튼에 대한 51/003/ 을 받음");
				arrTimeoutClear()
				progress.off();
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
				/*
				if(enginFlag && micFlag){
					console.log('50/003/ recStartTTS()')
					recStartTTS();
				}else{
				*/
				console.log('50/003/ recStartTTS()')
				recStartTTS();
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
			console.log("94/002/  | 50/002 Response!!!!!!!!!")
			if(!micError) {
				allFlag = false;
				micErrorFlag = true;
				micError = true;
				// 재생 멈춤
				// stop 이벤트 보내기
//				micErrorTimeout = setTimeout(() => {
				errroPopUpdateText("녹취기 전원 연결 상태를 확인해주세요.",'전원 케이블 재연결 시 10초 후 녹취 시작해주세요.','','')
//				stopEvent(null,'e')
				stopEvent('1','e')
//				}, 500);
			}
			break;
		case '90/002/' :
			allFlag = false;
			soundErrorFlag = true;
			stopEvent('1','e')
			console.log('90/002 \nPC불륨 60%아래!!');
			if(!micError){
				errroPopUpdateText("PC 스피커 불륨을 60 이상으로 변경해주세요.",'','','')
			}
//			progress.off();
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
				micExcpectedTimer = setTimeout(() => {
					console.log('micExcpectedTimer')
					if( !enginFlag ){
						console.log('micExcpectedTimer logic start')
						enginFlag2 = true;
//						progress.off();
						stopEvent('1','e');
						errroPopUpdateText('네트워크 또는 녹취기 연결에 실패하였습니다.','화면 새로고침(F5) 후 다시 시도해 주세요','','')
					}
				}, 10000);
			}
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
		arrRecStartTimeoutClear()
		expectedState=''
		enginFlag = false;
		micFlag = false;
		taReqFlag = false;
		sttRecStart = false;
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
			params.PRD_CD == 'T0000000000004' ||
			params.PRD_CD == 'T2014001580081' ){
			
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
					$('#holdon-message').html('대면녹취 준비상태를 확인중입니다.\n잠시만 기달려주세요.');
					sendmsg("recStart", scriptCallKey)
				},5000)
			} else if(expectedState =='retry' || expectedState == 'micError'){
				console.log("expectedState3::"+expectedState)
				progress.off()	
				waveDisplayOff()
				clearInterval(interval);
				resetTimer()
				if(!enginFlag && micFlag){
					sendmsg("recStop", scriptCallKey)
					alert('네트워크 또는 마이크에 문제가 발생하였습니다. 화면을 새로 고침 후 다시 시도해 주세요');
					clearTaErrorTimeOut()
					stopE();
					return;
				}
				if((!micFlag && enginFlag) || (!micFlag && !enginFlag)){
					sendmsg("recStop", scriptCallKey)
					clearTaErrorTimeOut()
					console.log('팝업을 띄우시오');
					$('#micErrorPop').show();	
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
			clearTaErrorTimeOut()
			console.log('팝업을 띄우시오');
			$('#micErrorPop').show();	
		}
		
		break;

	default:
		break;
	}
	
}

function webSocketClose() {
	webSocket.close()
}


function recStartTTS(){
	if(!allFlag){
		return;
	}
	
	// STT & TA
//	sendmsgStt("login");
	//TA 결과여부 체크 플레그
	enginFlag = false;
	micFlag = false;
	enginFlag2 = false;
	micFlag2 = false;
	soundErrorFlag = false;
	micErrorFlag = false;
	
	
	micError = false;
	recResult = true;
	
	//20220504 장진호 추가
	//51/003 플레그 초기화
	recStopResFlag = 0;
	
	progress.off() 
	try {
		recordTimerOn()
		waveDisplayOn()
	} catch (e) {

	}
	recStartFunction();
	scriptListPlay();
	//scriptCallKeyUpdate();
	expectedState='';
	clearTimeout(expectedtimer)
	clearTimeout(settimeout)
	if(recStartTimeOut != null){
		clearTimeout(recStartTimeOut);
		recStartTimeOut = null;
	}
	
	clearTimeout(enginExpectedTimer);
	clearTimeout(micExcpectedTimer);
	clearTimeout(micErrorTimeout);
	
	allFlag = true;
}
// 51/003/ 이 들어왔으면 true
var recStopCheckFlag = false;
// 51/003/ 루프로직이 몇번들어가는지 체크
var stopCnt = 0;
//51/003/ 체크하는 함수
function delayRecStopCheck(key){
	setTimeout(() => {
		if(!recStopCheckFlag){
			console.log('\n==================51/003/ 이 10초 지나도 안왓음\n====================');
			stopCnt = stopCnt+1;
			if(stopCnt < 2){
				sendmsg("recStop", key);
				console.log('recStop 다시보내기');
			}else{
				
			}
		}
	}, 10000);
	
	
}
//TA결과 수신을받기위한 플레그 값 셋팅
function taCheckFlagReset(){
	console.log('taCheck Flag Reset');
	micErrorFlag = false;
	soundErrorFlag = false;
	micFlag2 = true;
	enginFlag2 = true;
}
// 51/003/ 받기전 이벤트
function recStopEvent(){
	//다음구간을 강제 클릭 한 상태이며 이제 녹취시작버튼 클릭
	scriptRecFileChecked();
}

function NotResRecStopEvent(){
	arrTimeoutClear()
	
	timeoutArr.push(setTimeout(() => {
		errroPopUpdateText('녹취 저장에 실패하였습니다.','현재 구간을 재녹취 해주시길 바랍니다.','','')
		progress.off();
		stopEvent('1');
	}, 10000));
}
function NotResErrorRecStopEvent(){
	timeoutArr.push(setTimeout(() => {
		errroPopUpdateText('네트워크 및 녹취기 연결에 실패하였습니다.','화면 새로고침(F5) 후 다시 시도해 주세요.','','')
		progress.off();
	}, 10000));
}

function errroPopUpdateText(text1,text2,text3,text4){
	$('#errorText1').text(text1);
	$('#errorText2').text(text2);
	$('#errorText3').text(text3);
	$('#errorText4').text(text4);
	$('#errorModalMic').attr('class','modal ac')
	$('#errorModalMicBtn').show();
}

function arrTimeoutClear(){
	timeoutArr.forEach(i => {
		console.log('arrTimeoutClear()');
		clearTimeout(i);
	});
}
function arrRecStartTimeoutClear(){
	sttRecStartResultTimeoutArr.forEach(i => {
		console.log('sttRecStartResultTimeoutArr Clear()');
		clearTimeout(i);
	});
}