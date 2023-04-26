var Agent = navigator.userAgent.toLowerCase();

var isInternetExplorer = false;
var isSafari;
var isOpera;
var isChrome;
var isFirefox;

var BrowserName;
var ServerIPAddr = "127.0.0.1";
var Port = 0;
var SelectedMIME;
var Formats;

var Mime = "audio/mpeg";

var LastCheckTime;
var FocusChecker = null;

var PlayerControls;
var AudioPlayer;
var FormatReader;
var SocketClient;

var RealTimeExt = "";

var playState=false;
var sec=0
var timer;

var defineWave;

var waveDraw;					// IE Wave draw interval 변수
var queue; 			// 큐

var beforeCommand = 0;					// command 이전 값

// Browse Check
if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (Agent.indexOf("msie") != -1)) {
   isInternetExplorer = true;
}

isSafari  = (Agent.match(/(chromium|chrome|crios)/g) ? false : (Agent.match('safari') ? true : false));
isOpera   = (Agent.match('opera') ? true : false);
isChrome  = (Agent.match(/(chromium|chrome|crios)/g) ? true : false);
// isFirefox = (Agent.match('like gecko') ? false :
// (ua.match(/(gecko|fennec|firefox)/g) ? true : false));


if (isInternetExplorer)
   BrowserName = "IE";
else if (isSafari)
   BrowserName = "Safari";
else if (isOpera)
   BrowserName = "Opera";
else if (isChrome)
   BrowserName = "Chrome";
else if (isFirefox)
   BrowserName = "Firefox";
else if (isNativeChrome)
   BrowserName = "NativeChrome";
else
   BrowserName = "Unknown";

//서버 정보 셋팅
function SetServerinfo(serverIp){
// ServerIPAddr = listenIp; //document.getElementById('hostaddr').value;
ServerIPAddr = serverIp;
Port = '5100'; //document.getElementById('port').value; -> wave 요청 포트
Port *= 1;
Formats = new Array({"MIME": Mime, "PORT": Port});
}

//init
function Init(ext, serverIp){
RealTimeExt = ext;

// 미디어 포멧 사용 가능 여부 체크
for(var i = 0; i < Formats.length; i++){
   var AudioTag = new Audio();
   var answer = AudioTag.canPlayType(Formats[i]["MIME"]);
   if (answer === "probably" || answer === "maybe"){
      SelectedMIME = Formats[i]["MIME"];
      SelectedPORT = Formats[i]["PORT"];
      break;
   }
}

//if(isInternetExplorer == true){
		var oAudio = realtime_rc.obj.audio[0];
		oAudio.src =
			(HTTP=="https"?"https":"http")+"://"
	        + serverIp
	        + ":" + (HTTP=="https"?"5201":"5200")
	       // + ":5201"
	        + "/RECSEE/"
	        + ext //"1205"
	        + "/START/"
	        + "CHROME/"+new Date().getTime();
		oAudio.play();	
		
		oAudio.addEventListener('error',function(event) {
			//alertText("청취","컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요.");
			RealTimeExt= "";
		});
	//	RealTimeExt = ext;
//}

if(SelectedMIME == "" || SelectedPORT == 0){
   document.getElementById("typesunsupported").style.display = "block";
   return;
}
try{
	   AudioPlayer = new PCMAudioPlayer();
	   AudioPlayer.UnderrunCallback = OnPlayerUnderrun;
} catch(e){
   //alert("Init of PCMAudioPlayer failed: " + e);
   tryCatch(e)
   return;
}

try{
		  FormatReader = new AudioFormatReader(SelectedMIME, OnReaderError, OnReaderDataReady);
}
catch(e){
   return;
}


}

// Player 컨트롤
function OnPlayerUnderrun(){
   // LogEvent("Player error: Buffer underrun.");
}

function OnControlsVolumeChange(value){
   if(AudioPlayer != null)
	   AudioPlayer.SetVolume(value);
}

function OnControlsPlay(){
   // AudioPlayer.MobileUnmute();
   try{
	   SocketClient = new WebSocketCallBack('ws://' + ServerIPAddr + ':' + SelectedPORT.toString(), OnSocketError, OnSocketConnect, OnSocketDataReady, OnSocketDisconnect);
	   console.log('ws://' + ServerIPAddr + ':' + SelectedPORT.toString());	 
   }
   catch (e){
	   console.log(e);
      return;
   }
}

// Callback functions from format reader
function OnReaderError(){
  // LogEvent("Reader error: Decoding failed.");
}

function OnReaderDataReady(data){
   while (FormatReader.SamplesAvailable()){
	   var Samples = FormatReader.PopSamples();
	   AudioPlayer.PushBuffer(Samples);
	   realtime_rc.obj.wave.setWaveCanvas("draw",Samples,"waveForm");
   }
}

// 콜백 함수
function OnSocketError(error){
   // LogEvent("Network error: " + error);
   RealTimeExt = "";
   console.log(error);
   // @saint: realtime_listen 주석처리
   // == 수정 ==
   // $("#realtime_listen").hide();
   // == 수정 끝 ==
   msg = $("#monitoring_realtime_listen_fail4").val();
   // alert(msg);
}

function OnSocketConnect(){
	if(RealTimeExt == "") {
		console.error("내선번호가 비어있습니다.")
	} else {
		
		if(isInternetExplorer == true){
			SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/DB/IE/");
			console.log("/RECSEE/"+RealTimeExt+"/DB/IE/");
			queue = new Queue();
			
			
			
			waveDraw = setInterval(function(){
				if(queue.empty()==true){
					$('.waveCanvasObj').prepend("<div style='width: 5px; height: 100%; float: right;'>" +
							"<div style='border:0.5px solid black;background: #3592c9; width: 4px; height: 3%; bottom: 0px; position: absolute;opacity:1;transition:all 1s linear;'></div>" +
							"</div>");
		   			
		   			if($('.waveCanvasObj div').length>125){
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
		   			
		   			if($('.waveCanvasObj div').length>125){
						$('.waveCanvasObj div').last().prev().remove();
						$('.waveCanvasObj div').last().remove();
						}
				}
				queue.dequeue();
				 realtime_rc.obj.controller.setPlayerInfo(RealTimeExt,defineUserName,defineCustNum);
				// console.log(queue.front());
			},170);
			
			 // 중지 버튼 누르면 끊어주기
			 realtime_rc.player.find(".btn_stop").click(function(){
				 event.stopPropagation();
				 event.stopImmediatePropagation();
				 document.getElementById('realtimePlayer').getElementsByTagName('audio')[0].src="";
				 if ($('.listen-away').length >= 0){
					 $('.listen-away').remove();
				 }
				try{
					$(gridMonitoringView.getRowById(RealTimeExt)).removeClass("gridListening")
				}catch(e){
					tryCatch(e)
				}
				 OnSocketDisconnect();
			 })
		}else{
			SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/START/CHROME/");
			console.log("/RECSEE/"+RealTimeExt+"/START/CHROME/");
		}
		
		
		// 크롬에서 플레이어 보이기
		// $("#listen_wrap").show();
		playState=true;
      
	   /** LOG REAL LISTEN MONITORING* */
		$.ajax({
		    url: contextPath+"/monitoring/listenStartLog.do",
		    type: "POST",
		    data: {
		    	"listenExt" : RealTimeExt,
		    	"listenName" : defineUserName,
		    	"listenId" : defineUserId,
		    	"custPhone" : defineCustNum
		    },
		    dataType: 'json',
		    success: function(response) {
		    }
		});
		clearInterval( timer )
		timer = setInterval(function(){
			if(RealTimeExt != ""){
				// if(isChrome == true)
// realtime_rc.target.find(".procTime").text(secondToMinute(++sec))
				timeDiff(defineStime);
			}else{
				clearInterval( timer )
				sec = 0
				realtime_rc.target.find(".procTime").text("00:00:00")
			}
				
		},1000)
	}
}

function OnSetPlaystate(){
   // PlayerControls.SetPlaystate(true);
   StartFocusChecker();
   // LogEvent("Established connection with server.");
}

function OnSocketDisconnect(){
   try{
	   AudioPlayer.Close();
   } catch(e) {
	   tryCatch(e)
   }
   
	console.log("DISCONNECT")
	SocketClient.Socket.close();
   // PlayerControls.SetPlaystate(false);
   StopFocusChecker();
   // while(PlayerControls.ToogleActivityLight());
   // LogEvent("Lost connection to server.");
	$.ajax({
	    url: contextPath+"/monitoring/listenEndLog.do",
	    type: "POST",
	    data: {
	    	"listenExt" : RealTimeExt,
	    	"listenName" : defineUserName,
	    	"listenId" : defineUserId,
	    	"custPhone" : defineCustNum
	    },
	    dataType: 'json',
	    success: function(response) {
	    }
	});
	try{
		$(gridMonitoringView.getRowById(RealTimeExt)).removeClass("gridListening")
	}catch(e){tryCatch(e)}
	RealTimeExt = "";

   // $("#now_ext_no").text("");
   // @saint: realtime_listen 주석처리
   // == 수정 ==
   // $("#realtime_listen").hide();
   try {
      // @saint: 6번 생성하면 오류가 발생하여, 재생성 방지하기
      // == 기존 ==
      // FormatReader.MIMEReader.Close();
	   FormatReader.Self.SoundContext.close();
	   
      /*
		 * .then(function() { AudioPlayer.SoundContext.close(); AudioPlayer =
		 * null; FormatReader = null; });
		 */
      // @mars
      /* AudioPlayer.Close().then(function(){ */

      /* }); */
      // })
      // == 기존 끝 ==
      // == 수정 ==
   } catch(e) {
	   tryCatch(e)
      // alert(e);
   }

   // $("#listen_wrap").hide();
   // == 수정 끝 ==
   console.log("소켓이 끊어짐")
   clearInterval(waveDraw);
   playState=false;
   realtime_rc.player.find(".waveCanvasObj").html("");
   realtime_rc.obj.wave.setWaveCanvas("clear",null,"waveForm");
   realtime_rc.obj.controller.setPlayerInfo("")
}

function secondToMinute(sec){
	var temp = seconds2time(sec).split(":")
	return temp[0]+":"+temp[1]+":"+temp[2]
}

var PacketModCounter = 0;
var fBuffering = false, fEnd = false;
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

function OnSocketBufferingData(data){
	PacketModCounter++;

	if (PacketModCounter > 100){
		//PlayerControls.ToogleActivityLight();
		PacketModCounter = 0;
	}
	FormatReader.PushData(data);
	//console.log(data)
}

function StartFocusChecker(){
	if (FocusChecker == null){
		LastCheckTime = Date.now();
		FocusChecker = window.setInterval(CheckFocus, 2000);
	}
}

function StopFocusChecker(){
	if (FocusChecker != null){
		window.clearInterval(FocusChecker);
		FocusChecker = null;
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


// 시간차 구해주는 함수
function timeDiff(startTime) {
	websocket.send("getCurrentTime");
}

function setPlayerTime(time){
	var now = "";
	
	var _year = time.substring(0, 4)
	var _month = time.substring(4, 6)
	var _date = time.substring(6, 8)
	var _call_hour = time.substring(8, 10)
	var _call_minute = time.substring(10, 12)
	var _call_second = time.substring(12, 14)
	
	now = new Date(_year, _month, _date, _call_hour, _call_minute,_call_second)
	
	var year = now.getFullYear();
	var month = now.getMonth();
	var date = now.getDate();

	var call_hour = defineStime.substr(0, 2)
	var call_minute = defineStime.substr(2, 2)
	var call_second = defineStime.substr(4, 2)

	call_date = new Date(year, month, date, call_hour, call_minute, call_second)
	
	realtime_rc.target.find(".procTime").text(msToTime(now.getTime() - call_date.getTime()));
}

// 밀리 세컨드 to 시간
function msToTime(duration) {

	var milliseconds = parseInt((duration % 1000) / 100), seconds = parseInt((duration / 1000) % 60), minutes = parseInt((duration / (1000 * 60)) % 60), hours = parseInt((duration / (1000 * 60 * 60)) % 24);

	hours = (hours < 10) ? "0" + hours : hours;
	minutes = (minutes < 10) ? "0" + minutes : minutes;
	seconds = (seconds < 10) ? "0" + seconds : seconds;

	return lpad(hours + "", 2, "0")+":"+ lpad(minutes + "", 2, "0")+":"+lpad(seconds + "", 2, "0");
}
