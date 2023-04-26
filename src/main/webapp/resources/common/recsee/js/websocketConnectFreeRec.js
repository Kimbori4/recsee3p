var wsURI = "ws://localhost:12969";
var webSocket;
var tttt;
var expectedtimer;
var expectedState='';
var retryCount=0;
var micError = false;

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
}

function onMessage(evt) {
	console.log("onmessage : " + evt);
	try{
		json = JSON.parse(evt.data)
		
		if (json.outputtext != undefined)
			params = JSON.parse(json.outputtext);
			if(params["RCD_KEY"] != null || params["RCD_KEY"] != undefined) {
				callKey = params["RCD_KEY"]
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
			};
		switch (json.code) {
		case "60":
			break;
		}
	}catch(e){
		switch (evt.data) {
		
		case '50/001/':
			if(expectedState!='retry')
				expectedState='50/003/';
			break;
		
		case '50/003/':
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
			break;
			/*
		case '94/002/':
			if(!micError) {
				micError = true;
				// 재생 멈춤
				// stop 이벤트 보내기
				stopEvent()
				alert("마이크 연결 상태를 확인해주세요.");
			}
			break;
			*/
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
			doSend(JSON.stringify(recStart))
			
				
			if(expectedState =='retry'){
				
			}else{
				expectedState ='50/001/'
				
			}
			expectedtimer = setTimeout(() => {
				console.log('시작:'+expectedtimer)
				expect('start')
			}, 7000);
		},500);
		

		break;

	case "recStop":
		recStart.trxid = key;
		recstop.trxid = key;
		doSend(JSON.stringify(recstop));
		clearTimeout(expectedtimer)
		clearTimeout(settimeout)
		/*if(expectedState !='retry'){
			progress.off()
		}*/
		expectedState=''
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

	default:
		break;
	}

}

function expect(comm){
	switch (comm) {
	case "start":
		console.log("expectedState0::"+expectedState)
		if(expectedState!=''){
			console.log("expectedState1::"+expectedState)
			if(expectedState !='retry'){
				console.log("expectedState2::"+expectedState)
				expectedState ='retry'//일단 두번넣자..
				sendmsg("recStop", scriptCallKey)
				settimeout = setTimeout(function(){
					expectedState ='retry'
					createCallKey();
					progress.on()
					sendmsg("recStart", scriptCallKey)
				},1500)
			} else if(expectedState =='retry'){
				console.log("expectedState3::"+expectedState)
				progress.off()	
				sendmsg("recStop", scriptCallKey)
				waveDisplayOff()
				clearInterval(interval);
				resetTimer()
				alert('네트워크 또는 마이크에 문제가 발생하였습니다. 화면을 새로 고침 후 다시 시도해 주세요');
			}
			
		}
		
		break;

	default:
		break;
	}
	
}

function webSocketClose() {
	webSocket.close()
}