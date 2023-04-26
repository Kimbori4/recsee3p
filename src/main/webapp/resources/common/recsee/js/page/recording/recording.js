var SocketClient;
var queue;
var RealTimeExt = "1026";
var FocusChecker = null;
var LastCheckTime;
var beforeCommand = 0;					// command 이전 값
var serverIP = "localhost"
var timer;
var start = new Date().getTime();
var durationSec;

var LogSocketClient;

window.onload = function() {
	try {
		top.playerVisible(false);
	} catch (e) {

	}
	$("#rec_start").click(function() {
		var sendUrl = "http://"+serverIP+":5711/MicStreaming?ext="+RealTimeExt+"&type=mic&command=start&data=192.168.0.72,5000,5001,4000,4001";
		$.ajax({
			url:sendUrl,
			type:"POST",
			timeout:5000,
			async: false,
			success:function(result){
				if(result == null || result == undefined) {
					alert("녹취 시작 요청에 실패했습니다.");
				} else {
					sendUrl ="http://localhost:7272/PcmReceiver?type=start";
					$.ajax({
						url:sendUrl,
						type:"GET",
						timeout:5000,
						async: false,
						success:function(result){
							if(result == null || result == undefined) {
								alert("녹취 시작 요청에 실패했습니다.");
							} else {
								alert("녹취가 시작되었습니다.");
								$("#rec_start").css("background-color", "#deedff");
								start = new Date().getTime();
								OnControlsPlay();
							}
						}, error:function(reason) {
							if(reason == null || reason == undefined) {
								alert("녹취 시작 요청에 실패했습니다.");
							} else {
								alert("녹취 시작 요청에 실패했습니다.");
							}
						}
					});
				}
			}, error:function(reason) {
				if(reason == null || reason == undefined) {
					alert("녹취 시작 요청에 실패했습니다.");
				} else {
					alert("녹취 시작 요청에 실패했습니다.");
				}
			}
		});
	});
	
	$("#rec_end").click(function() {
		var sendUrl = "http://"+serverIP+":5711/MicStreaming?ext="+RealTimeExt+"&type=mic&command=stop";
		$.ajax({
			url:sendUrl,
			type:"POST",
			timeout:5000,
			async: false,
			success:function(result){
				if(result == null || result == undefined) {
					alert("녹취 종료 요청에 실패했습니다.");
				} else {
					sendUrl ="http://localhost:7272/PcmReceiver?type=stop";
					$.ajax({
						url:sendUrl,
						type:"GET",
						timeout:5000,
						async: false,
						success:function(result){
							if(result == null || result == undefined) {
								alert("녹취 종료 요청에 실패했습니다.");
							} else {			
								alert("녹취가 종료되었습니다.");
							}
							insertData();
						}, error:function(reason) {
							if(reason == null || reason == undefined) {
								alert("녹취 종료 요청에 실패했습니다.");
							} else {
								alert("녹취 종료 요청에 실패했습니다.");
							}
							insertData();
						}
					});
			
				}
			}, error:function(reason) {
				if(reason == null || reason == undefined) {
					alert("녹취 종료 요청에 실패했습니다.");
				} else {
					alert("녹취 종료 요청에 실패했습니다.");
				}
				insertData();
			}
		});
	});
}


function insertData() {
	$("#rec_start").css("background-color", "#ffffff")
	var dataStr = {
		"custId" : sessionStorage.getItem("custNum"),
		"custName" : sessionStorage.getItem("custName"),
		"custPhone" : sessionStorage.getItem("custPhone"),
		"productCode" : sessionStorage.getItem("productCode"),
		"faceRecTtime" : durationSec
	}
	$.ajax({
		url:contextPath+"/insertFaceRecInfo.do",
		data:dataStr,
		type:"POST",
		timeout:5000,
		async: false,
		success:function(result){
			
		}, error:function(reason) {
	
		}
	});	
}
function OnControlsPlay(){
   // AudioPlayer.MobileUnmute();
   try{
	   SocketClient = new WebSocketCallBack('ws://'+serverIP+':5100', OnSocketError, OnSocketConnect, OnSocketDataReady, OnSocketDisconnect);
	   LogSocketClient = new WebSocketCallBack('ws://'+serverIP+':9988', OnLogSocketError, OnLogSocketConnect, OnLogSocketDataReady, OnLogSocketDisconnect);
   }
   catch (e){
	   console.log(e);
      return;
   }
}


// 콜백 함수
function OnLogSocketError(error){
   console.log(error);
}

// 콜백 함수
function OnSocketError(error){
   console.log(error);
}

function OnLogSocketConnect(){
	console.log("onLogSocketConnect success");
}

function OnSocketConnect(){
	if(RealTimeExt == "") {
		console.error("내선번호가 비어있습니다.")
	} else {
		SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/DB/IE/");
		
		queue = new Queue();
			
		waveDraw = setInterval(function(){
			if(queue.empty()==true){
				$('.waveCanvasObj').prepend("<div style='width: 5px; height: 100%; float: right;'>" +
						"<div style='border:0.5px solid black;background: #3592c9; width: 4px; height: 3%; bottom: 0px; position: absolute;opacity:1;transition:all 1s linear;'></div>" +
						"</div>");
	   			
	   			if($('.waveCanvasObj div').length>450){
					$('.waveCanvasObj div').last().prev().remove();
					$('.waveCanvasObj div').last().remove();
					}
			}else{
				var waveHeight;
				if(queue.front() ==0)
					waveHeight = 5;
				else
					waveHeight = queue.front();
				$('.waveCanvasObj').prepend("<div style='width: 5px; height: 100%; float: right;'>" +
						"<div style='border:0.5px solid black;background: #3592c9; width: 4px; height: "+waveHeight+"%; bottom: 0px; position: absolute;opacity:1;transition:all 1s linear;'></div>" +
						"</div>");
	   			
	   			if($('.waveCanvasObj div').length>450){
					$('.waveCanvasObj div').last().prev().remove();
					$('.waveCanvasObj div').last().remove();
					}
			}
			queue.dequeue();
		},170);
		
		clearInterval( timer )
		timer = setInterval(function(){
			if(RealTimeExt != ""){
				setPlayerTime(start);
			}else{
				clearInterval( timer )
				sec = 0
				$("#recordingTime").text("00:00:00")
			}
				
		},1000)
	}
}

var PacketModCounter = 0;
var fBuffering = false, fEnd = false;



function OnLogSocketDataReady(data){
	console.log(data);
	if(JSON.parse(data).msg != "") {
		$(".log_contents").append("<span class='"+JSON.parse(data).type+"'>["+JSON.parse(data).type.toUpperCase()+"] "+JSON.parse(data).msg+"</span>");
	}
}

function OnSocketDataReady(data){
	//console.log(data);
   var regexp = /RECSEE/;
   if(regexp.test(data)) {
      var arData = data.split("/");
      var command = arData[3];
      var reason = arData[4];
      var msg = "";
      fBuffering = false;
   }
   if(!fBuffering) {
      switch(command) {
      case "SUCCESS" :
         fBuffering = true;
         msg = "";
         OnSetPlaystate();
         break;
      case "FAIL" :
         switch(reason) {
         case "1" :
            msg = $("#monitoring_realtime_listen_fail1").val();
            break;
         case "2" :
            msg = $("#monitoring_realtime_listen_fail2").val();
            break;
         case "3" :
            msg = $("#monitoring_realtime_listen_fail3").val();
            break;
         default :
            msg = $("#monitoring_realtime_listen_fail4").val();
            break;
         }
         fBuffering = false;
         OnSocketDisconnect();
         break;
      case "END" :
         fBuffering = false;
         msg = $("#monitoring_realtime_listen_callend").val();
         OnSocketDisconnect();
         
         break;
       	default:
       		//console.log(command)
       		
       		var cmSplit = command.split(",");
       		var beforeWave = getAverageVolume([beforeCommand,cmSplit[0]]);
       		var avg = getAverageVolume(cmSplit);
       		
    		queue.enqueue(cmSplit[0]);
    		queue.enqueue(avg);
       		queue.enqueue(cmSplit[1]);
       	break;
      }

      /*if(msg != "" ) alert(msg)*/

   } else {
      OnSocketBufferingData(data);
   }
}

function getAverageVolume(array) {
    var values = 0;
    var average;

    var length = array.length;
    // get all the frequency amplitudes
    for (var i = 0; i < length; i++) {
        values += parseInt(array[i]);
    }
    average = values / length;
    return average;
}

function OnSocketDisconnect(){
   try{
	   AudioPlayer.Close();
   } catch(e) {
	   tryCatch(e)
   }
   
	console.log("DISCONNECT")
	SocketClient.Socket.close();
   StopFocusChecker();
	RealTimeExt = "";
   try {
	   FormatReader.Self.SoundContext.close();
   } catch(e) {
	   tryCatch(e)
   }

   console.log("소켓이 끊어짐")
   clearInterval(waveDraw);
   $(".waveCanvasObj").html("");
}

function OnLogSocketDisconnect(){
   try{
	   AudioPlayer.Close();
   } catch(e) {
	   tryCatch(e)
   }
   
	console.log("DISCONNECT")
	SocketClient.Socket.close();
   StopFocusChecker();
	RealTimeExt = "";
   try {
	   FormatReader.Self.SoundContext.close();
   } catch(e) {
	   tryCatch(e)
   }

   console.log("소켓이 끊어짐")
   clearInterval(waveDraw);
   $(".waveCanvasObj").html("");
}

function StopFocusChecker(){
	if (FocusChecker != null){
		window.clearInterval(FocusChecker);
		FocusChecker = null;
	}
}

function StartFocusChecker(){
	if (FocusChecker == null){
		LastCheckTime = Date.now();
		StartTime = Date.now();
		FocusChecker = window.setInterval(CheckFocus, 2000);
	}
}

function CheckFocus(){
	var CheckTime = Date.now();
	// Check if focus was lost
	if (CheckTime - LastCheckTime > 10000){
		// If so, drop all samples in the buffer
		// LogEvent("Focus lost, purging format reader.")
		FormatReader.PurgeData();
	}
	LastCheckTime = CheckTime;
}

function OnSetPlaystate(){
   // PlayerControls.SetPlaystate(true);
   StartFocusChecker();
   // LogEvent("Established connection with server.");
}


function setPlayerTime(time){
	var now = new Date().getTime();
	
	var sec = Math.floor((now - start) / 1000);
	durationSec = sec;
	var time = seconds2time(sec); 
	$("#recordingTime").text(time);
}

// 자릿수 메꿔주는 함슈
function lpad(s, padLength, padString){

    while(s.length < padLength)
        s = padString + s;
    return s;
}