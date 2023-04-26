/*
 * ********************************************************************
 *																				  *																					
 **********************************************************************
 */
"use strict";
jQuery.support.cors = true;				//	AJAX 사용시 크로스 도메인 허용
var baseSrc = "";
var $audio;

var canplayCheck=0;
(function(src){
	// 초기 recsee_player.js의 경로 찾는 작업 수행
	src = ((src||[]).split("/")[1]?src:location.pathname);
	var path = function(){
		var pathArr = src.split("/");
		pathArr.pop();
		var path = pathArr.join("/");
		return path;
	}();

	$("head").append('<link rel="stylesheet" type="text/css" href="' + path + '/component/jquery/jquery-ui/jquery-ui.min.css">');
	$("head").append('<link rel="stylesheet" type="text/css" href="' + path + '/player_common.css">');
    $("head").append('<link rel="stylesheet" type="text/css" href="' + path + '/component/HoldOn/HoldOn.css">');
    $("head").append('<script type="text/javascript" src="' + path + '/component/HoldOn/HoldOn.min.js">');
    $("head").append('<script type="text/javascript" src="' + path + '/component/layerPopup/layer_popup.js">');
	$.getScript(path+"/component/jquery/jquery-ui/jquery-ui.min.js");


	baseSrc = path + "/";
}($("script[src*='"+"recsee_stt_player.js"+"']").attr("src")))

/**
 * @define _RC
 * @type object
 * @description RecseePlayer 내부에서 사용하는 DEFINE값, 유용한 함수 등을 정의해둔 객체
 */

 RecseePlayer.prototype._RC = (function(){
 	var _RC = {	// 상수 정의
 		/**
 		 * @define TYPE
 		 * @type object
 		 * @description 플레이어 타입 name들을 정의
 		 */
 		 TYPE : {
 			 		HEADER : "header"
 			 	  ,	TAG : "tag"								// 태그 버튼 사용 여부
 				  ,	BTNREPLAY : "btnReplay"			// 반복 버튼 재생 사용 여부
 				  , BTNMUTE : "btnMute"				// 음소거 버튼 사용 여부
 				  , BTNDEL : "btnDel"					// 구간 제거 버튼 사용 여부 
 				  , BESTCALL : "bestCall"				// 우수콜 사용 여부
	    		  ,	MENUCLICKRIGHT : "menuClickRight"	// 구간 메뉴 우클릭 사용 여부
	       		  ,	MENUCLICKLEFT : "menuClickLeft"	// 구간 메뉴 좌클릭 사용 여부
 		 },
         MARKING : {
         	frontMarking : null,
         	backMarking : null
         }
 	};

	/**
	* @define DEFAULT_OPTION
	* @description 디폴트 옵션을 정의
	*  type: 각 player object를 사용하는지 여부
	*  target: RecseePlayer가 위치할 Element로, jQuery Selector 형태로 입력한다
	*  BAR_COUNT: WAVE 모드에서, wave를 구현할 bar의 갯수. 기본 200
	*  MOVE_TIME: prev, next버튼, 클릭 시 이동할 시간(s). 기본 15
	*  REALTIME: 실감 플레이어로 사용 할 경우
	*  TOOL: true, 구간지정, 구간저장, 구간다운, 파일 다운 등 툴
	*/

	_RC.DEFAULT_OPTION = {
			type: {}
		,   target: "body"
		,   autoplay: false   			// 자동시작 여부
		,	MOVE_TIME: 5    			// 앞, 뒤로가기 버튼 시 이동 시간(s)
		,   REQUEST_IP: "127.0.0.1" 	// 음성파일 요청 IP
		,   REQUEST_PORT: "28881"   	// 음성파일 요청 PORT
		,	CURRENTSIZE : "1"				// 현재 사이즈
		,	DEBUG : true						// 디버깅 모드
	};
	
	// 타입 설정:
    _RC.DEFAULT_OPTION.type[_RC.TYPE.HEADER] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.TAG] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNREPLAY] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNMUTE] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNDEL] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BESTCALL] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.MENUCLICKRIGHT] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.MENUCLICKLEFT] = false;

 	/**
 	* @define getFileNameFromURL
 	* @type function
 	* @description URL 주소에서 파일명만을 추출한다.
 	* @param url(string)
 	* @return (string)
 	*/

 	_RC.getFileNameFromURL = function(url) {
 			return (url||"").split("/").pop().replace(/\?(.*?)/, "");
 	}
 	return _RC;
 }());

/**
 * @define setDefault
 * @type function
 * @description prototype 함수를 일괄 설정
 */

 RecseePlayer.prototype.setDefault = function() {
	 this.init = function(o) {
		 // 플레이어 옵션
	   var defaultOption = RecseePlayer.prototype._RC.DEFAULT_OPTION;
	   o = o||defaultOption;
	   
	   this.set("type", function(oType) {
           var type = {};
           $.each(defaultOption.type, function(t, b) {
               // 설정값이 있으면 설정값을, 없으면 디폴트값 적용
               var newVal = oType[t] === true || oType[t] === false?oType[t]:b
               type[t] = newVal;
           });
           return type;
       }(o));

		 this.set("requestIp",o.requestIp||defaultOption.REQUEST_IP);
		 this.set("requestPort",o.requestPort||defaultOption.REQUEST_PORT);
		 this.set("currentSize",o.currentSize||defaultOption.CURRENTSIZE);
		 this.set("moveTime", o.moveTime||defaultOption.MOVE_TIME);
		 this.set("target", $(o.target||defaultOption.target));
		 this.set("DEBUG", o.DEBUG||defaultOption.DEBUG);
		 
		 var $target = this.target;
		 //var $player = $(this.HTML.player);
		 var $player = this.HTML.player;
		 var TYPES = this._RC.TYPE;

		 this.set("player", $player);
		 
		 // 우수콜 사용 안할 경우
		  if(this.type[TYPES.BESTCALL]) {
			  $player.find(".best_call_wrap").removeClass("display_none");
		  }

		 $target.append($player);
		 
		 setTimeout(function(){
			 // jquery UI Slider Load
				$("#playSoundBar").slider({
					value : 100,
					slide : function(event , ui ){
						$(this).find(".progress").css("width",ui.value+"%");
						//	sound 0 일때 off 처리
						if(ui.value == "0"){
							$(this).parents(".play_btn_group").find(".volume_icon").addClass("volume_off_all");
							$("#rxPlaySoundBar").parents(".rx_audio_sound").find(".volume_icon").addClass("volume_off");
							$("#txPlaySoundBar").parents(".tx_audio_sound").find(".volume_icon").addClass("volume_off");
							
							$(".audio_rx audio")[0].muted = true;
							$(".audio_tx audio")[0].muted = true;
						}else{
							$(this).parents(".play_btn_group").find(".volume_icon").removeClass("volume_off_all");
							$("#rxPlaySoundBar").parents(".rx_audio_sound").find(".volume_icon").removeClass("volume_off");
							$("#txPlaySoundBar").parents(".tx_audio_sound").find(".volume_icon").removeClass("volume_off");
							
							$(".audio_rx audio")[0].muted = false;
							$(".audio_tx audio")[0].muted = false;
						}
						changeAudioSound(true , ui.value)
						changeAudioSound(false , ui.value)
					}
				});
				
				$("#rxPlaySoundBar").slider({
					value : 100,
					slide : function(event , ui ){
						$(this).find(".progress").css("width",ui.value+"%");
						
						if(ui.value == "0"){
							$(this).parents(".rx_audio_sound").find(".volume_icon").addClass("volume_off");
							$(".audio_rx audio")[0].muted = true;
						}else{
							$(this).parents(".rx_audio_sound").find(".volume_icon").removeClass("volume_off");
							
							$(".audio_rx audio")[0].muted = false;
						}
						
						changeAudioSound(true , ui.value)
					}
				});
				
				$("#txPlaySoundBar").slider({
					value : 100,
					slide : function(event , ui ){
						$(this).find(".progress").css("width",ui.value+"%");
						
						if(ui.value == "0"){
							$(this).parents(".tx_audio_sound").find(".volume_icon").addClass("volume_off");
							$(".audio_tx audio")[0].muted = true;
						}else{
							$(this).parents(".tx_audio_sound").find(".volume_icon").removeClass("volume_off");
							
							$(".audio_tx audio")[0].muted = false;
						}
						
						changeAudioSound(false , ui.value)
					}
				});
				
				$("#playerSizeBar").slider({
					orientation : "vertical",
					step : 10,
					value : 80,
					slide : function(event , ui ){
						$(this).find(".progress").css("height",ui.value+"%");
					}
				});
				
				function changeAudioSound( isRx , value){
					if(isRx){
						$(".audio_rx audio")[0].volume = value / 100;
						$("#rxPlaySoundBar").slider('value', value );
						$("#rxPlaySoundBar").find(".progress").css("width",value +"%");
					}else{
						$(".audio_tx audio")[0].volume = value / 100;
						$("#txPlaySoundBar").slider('value', value );
						$("#txPlaySoundBar").find(".progress").css("width",value +"%");
					}
				}
				
		 },1000)
		 
		//기본 이미지 셋팅
//		 this.mainImageLoad();
		 
		 //	object 설정
		 this.setObject();
		 
		 //	객체 이벤트 설정
		 this.setEvent();
	 }
	 /**
	 * @define RecseePlayer.set
	 * @type function
	 * @description RecseePlayer 객체 내에 값을 설정한다.
	 * @param k(string): key, v(any): value, dv(any): default value
	 * @return (any)
	 */
	 this.set = function(k, v, dv) {
			 return this[k] = v == 0?v:(v||dv);
	 }

	 /**
	 * @define RecseePlayer.setFile
	 * @type function
	 * @description RecseePlayer 객체에 파일을 설정한다
	 * @param t(string): type[audio, video], f(file)
	 */
	 this.setFile = undefined;

	 /**
	 * @define RecseePlayer.setWaveHeight
	 * @type function
	 * @description wave의 높이값을 설정한다.
	 *  index와 height을 파라미터로 넘길 경우 특정 index의 값을 바꿀 수 있으며,
	 *  숫자 배열을 값으로 넘길 경우 높이를 일괄 설정할 수 있다.
	 * @param i(number): index, h(number): height
	 *  OR    i(array): height의 array
	 */
	 this.setWaveHeight = undefined; //function(i, h) {
	 
	 /**
	 * @define RecseePlayer.updateMiniMap
	 * @type function
	 * @description stt 플레이어 미니맵 을 표시 해줌
 	 * @param u : url , i : 이미지 넣어줄 id 값 , c : 복사 이미지 넣어줄 아이디 값 
	 */
	 this.updateMiniMap = undefined;
	 
	 /**
		 * @define RecseePlayer.FileInfo
		 * @type function
		 * @description 파일정보를 리턴 해줌...
		 *  				   
		 * @param txt(파싱 데이터) , rx  ( rx 인지 여부)
		 */
	 this.fileInfo = undefined;
	 
	 /**
	 * @define RecseePlayer.setSttChar
	 * @type function
	 * @description 파일의 STT 텍스트 형태의 파일을 표출해줌
	 *  recDate , recTime, ext (필)
	 * @param 
	 */
	 this.setSttChar = undefined; 
 
	 
	 /**
	 * @define RecseePlayer.sttParserInsert
	 * @type function
	 * @description Stt받은XML 파일 파싱하여 플레이어 아래 칸에 넣어줌
	 *  				   
	 * @param txt(파싱 데이터) , rx  ( rx 인지 여부)
	 */
	 this.sttParserInsert = undefined;
	 
	 /**
	 * @define RecseePlayer.sttParseValue
	 * @type function
	 * @description Stt받은XML 파일 파싱하여 내부에 저장
	 *  				   
	 * @param txt(파싱 데이터) , rx  ( rx 인지 여부)
	 */
	 this.sttParseValue = undefined;
	 
	 /**
	 * @define RecseePlayer.setSttTypeSentence
	 * @type function
	 * @description 파일의 STT 텍스트 형태의 파일을 표출해줌
	 *  recDate , recTime, ext (필)
	 * @param 
	 */
	 this.setSttTypeSentence = undefined; 
	 
	 /**
	 * @define RecseePlayer.setSttTypeText
	 * @type function
	 * @description 파일의 STT 텍스트 형태의 파일을 표출해줌
	 *  recDate , recTime, ext (필)
	 * @param 
	 */
	 this.setSttTypeText = undefined; 
	 
	 /**
	 * @define RecseePlayer.log
	 * @type function
	 * @description 디버깅 모드 일대 콘솔 로그 찍기 편하게 하기 위해서 제작
	 * @param t : 찍어줄 로그
	 */
	 this.log = undefined; 
	 
	 /**
	 * @define RecseePlayer.setLocalStorage
	 * @type function
	 * @description 로컬 스토리지 저장
	 * @param k : key  ,  v : value 
	 */
	 this.setLocalStorage = undefined; 
	 
    /**
     * @define RecseePlayer.makeSection
     * @type function
     * @description 플레이어에 섹션을 만들어 준다.
     * @param startTime 섹션의 시작 점
     * @param endTime 섹션의 종료 점
     */
    this.makeSection = undefined;
	 
	 /**
	 * @define RecseePlayer.getLocalStorage
	 * @type function
	 * @description 로컬 스토리지 불러오기
	 * @param k : key 
	 */
	 this.getLocalStorage = undefined; 
	 
	 /**
	 * @define RecseePlayer.textToMarking
	 * @type function
	 * @description 왼쪽 아래 텍스트 구간 지정
	 */
	 this.textToMarking = undefined; 
	 
	 
	 /**
	 * @define RecseePlayer.clearSearchText
	 * @type function
	 * @description 검색한 문자열 클리어 효과
	 */
	 this.clearSearchText = undefined; 
	 
	 
	this.XML = {};
    this.sTime = undefined;		// 시작 시간
    this.eTime = undefined;		//	종료 시간
    this.fullRepeat = false;			// 전체 반복 재생 유무
    this.repeat = false;				//	반복 유무
    this.viewFix = false;				// 고정 플래그
    this.seHistory = 0;				// 검색 히스토리 순서 값..
    this.startMarking = undefined;				// 구간지정 시작 값
    this.endMarking = undefined;				// 구간지정 종료 값
    this.currentSearchText = "";					// 현재 검색 값
    this.currentSearchIdx = 0; 						// 현재 검색 값 순서지정

	 /*
	  * @define mainImageLoad 
	  * @type function
	  * @description RecseePlayer 로딩 후 기본 메인 이미지 로드	함수
	  */
	 /**
	 * @define setObject
	 * @type function
	 * @description RecseePlayer의 object들을 설정한다.
	 */
	 this.setObject = function(){
		 var $player = this.player;
		 this.obj = {};

		 this.obj.player = $player;

		 this.obj.body = $player.find(".play_control_wrap");

		 this.obj.rxWaveObj = $player.find(".rxWaveObj");
		 this.obj.txWaveObj = $player.find(".txWaveObj");

		 this.obj.rxAudio = $player.find(".audio_rx audio");
		 this.obj.txAudio = $player.find(".audio_tx audio");
		 
		 $audio = this.obj.rxAudio;
		 
		 this.obj.downSize = $player.find("#playerDownSize");
		 this.obj.upSize = $player.find("#playerUpSize");

		 this.obj.sttAllText = $player.find(".play_wrap_all_text");					//	STT 플레이어 하단 전체 표시
		 this.obj.sttFileInfo = $player.find(".player_play_infomation");			//	플레이어 파일 정보 표시 wrap
	 }
	 /**
	 * @define setEvent
	 * @type function
	 * @description RecseePlayer 객체에 이벤트들을 설정한다.
	 * @param e(function): custom event
	 */
	 this.setEvent = function(e){
		 var rc = this;
		 //	플레이어
		 var $player = this.obj.player;

		 // 플레이어 바디
		 var $body = this.obj.body;

		 // RX / TX 파형
		 var rxWave = this.obj.rxWaveObj;
		 var txWave = this.obj.txWaveObj;

		 // rx 오디오
		 var $rxAudio = this.obj.rxAudio;
		 var rxAudio = $rxAudio.get(0);

		 // tx 오디오
		 var $txAudio = this.obj.txAudio;
		 var txAudio = $txAudio.get(0);
		 
		 // 확대 축소 버튼
		 var upSizeBtn = this.obj.upSize;
		 var downSizeBtn = this.obj.downSize;
		 
	     var TYPES = rc._RC.TYPE;
	     var DEF = rc._RC.DEFAULT_OPTION;
		 var MOVE_TIME = DEF.MOVE_TIME||15;

		 //	audio 이벤트

		 // 컨트롤바와 media객체의 이벤트 연결
		 setMediaEvent(rxAudio);
		// setMediaEvent(txAudio);
		 
		 //	Stt 하단 모든 텍스투 구간 
		 var sttAllText = this.obj.sttAllText;
		 
		 // Stt 오른쪽 하단 파일 정보
		 var sttFileInfo = this.obj.sttFileInfo;

     //RX 파일 설정(로드) 이벤트
     $rxAudio.setFile = function(file, name, uploadFile, all) {
    	 
     	$rxAudio.data("loaded",false);

   		$rxAudio.attr("src", file+".RX.mp3");
        $rxAudio.data("name", name||rc._RC.getFileNameFromURL(file));
        
     }

    //TX 파일 설정(로드) 이벤트
    $txAudio.setFile = function(file, name, uploadFile, all) {

	  	$txAudio.data("loaded",false);

		$txAudio.attr("src", file+".TX.mp3");
	   	$txAudio.data("name", name||rc._RC.getFileNameFromURL(file));
    }


		 // RecseePlayer 객체 제어 함수
		 /**
		 * @define RecseePlayer.setFile
		 * @type function
		 * @description RecseePlayer 객체에 파일을 설정한다
		 * @param t(string): type[audio, video], f(file), u(upload)
		 */
		 rc.setFile = function(t, f, u, a) {
			 
			$(".player_init_wrap_text").remove();
			 
			 // Progress On...
			HoldOn.open({
					theme : "sk-folding-cube",
					backgroundColor : "#222"
			})
			 
			rc.all = a||false;
			var f = f.replace(/\\/gi,'/');

			var file, name, fileMode;
			if(typeof f == "object") {
						 // 로컬 파일
				file = URL.createObjectURL(f);
				name = rc._RC.getFileNameFromURL(f.name);
			} else {

				var OPTION = rc._RC.DEFAULT_OPTION
				var prepixUrl = "http://"+rc.requestIp+":"+rc.requestPort;
				if(!a){
						f = prepixUrl+"/listen"+f
				}

			 	// From URL
				file = f;
				rc.log("============FILE NAME :: "+file+"================");

				// 객체에 요청파라미터 저장
				if (t == "audio")
					rc.requestParam = f;

				name = f.split("/").pop();
			}

			if(t == "audio") {
				
				$rxAudio.setFile(file, name, u, a);
				$txAudio.setFile(file, name, u, a);
					
//					$rxAudio.get(0).play();
//					$txAudio.get(0).play();
//					
			}
			//rc.sTime = 0;
			//rc.eTime = 0;
			//$player.find('.player_marking_wrap').remove()
		 }
		 /**
		* @define setWaveHeight
		* @type function
		* @description wave의 높이값을 설정한다.
		*  index와 height을 파라미터로 넘길 경우 특정 index의 값을 바꿀 수 있으며,
		*  숫자 배열을 값으로 넘길 경우 높이를 일괄 설정할 수 있다.
		* @param i(number): index, h(number): height
		*  OR    i(array): height의 array
		*/
	 	rxWave.setWaveHeight = function(i, h) {

 		var rxWaveCanvas = rxWave.find(".rxWaveCanvas");
 		var rxWaveProg = rxWave.find("#rxWaveSvgProg");

		 if(typeof h == "undefined"){
				i.forEach(function(v, index){
					
				 	var k = document.createElementNS("http://www.w3.org/2000/svg","rect");
				 	
				 	k.setAttributeNS(null,"x",150 - v / 2);
				 	k.setAttributeNS(null,"y",index*5);
				 	k.setAttributeNS(null,"width",v);
				 	k.setAttributeNS(null,"height",5);
				 	k.setAttributeNS(null,"stroke", "#333");
				 	k.setAttributeNS(null,"fill-opacity", "0");
				 	
				 	document.getElementById('rxWaveSvg').appendChild(k);
				 	
					
				 	var j = document.createElementNS("http://www.w3.org/2000/svg","rect");
				 	
				 	j.setAttributeNS(null,"x",150 - v / 2);
				 	j.setAttributeNS(null,"y",index*5);
				 	j.setAttributeNS(null,"width",v);
				 	j.setAttributeNS(null,"height",3);
				 	j.setAttributeNS(null,"stroke-width","1");
				 	j.setAttributeNS(null,"fill", "#3a9cd6");
				 	
				 	document.getElementById('rxWaveSvgProg').appendChild(j);
				 	
				});
			 }
		 }
	 	
	 	/**
		* @define setWaveHeight
		* @type function
		* @description wave의 높이값을 설정한다.
		*  index와 height을 파라미터로 넘길 경우 특정 index의 값을 바꿀 수 있으며,
		*  숫자 배열을 값으로 넘길 경우 높이를 일괄 설정할 수 있다.
		* @param i(number): index, h(number): height
		*  OR    i(array): height의 array
		*/
	 	txWave.setWaveHeight = function(i, h) {

 		var txWaveCanvas = txWave.find(".txWaveCanvas");
 		var txWaveProg = txWave.find("#txWaveSvgProg");

		 if(typeof h == "undefined"){
				i.forEach(function(v, index){
					
				 	var k = document.createElementNS("http://www.w3.org/2000/svg","rect");
				 	
				 	k.setAttributeNS(null,"x",150 - v / 2);
				 	k.setAttributeNS(null,"y",index*5);
				 	k.setAttributeNS(null,"width",v);
				 	k.setAttributeNS(null,"height",5);
				 	k.setAttributeNS(null,"stroke", "#333");
				 	k.setAttributeNS(null,"fill-opacity", "0");
				 	
				 	document.getElementById('txWaveSvg').appendChild(k);
				 	
					
				 	var j = document.createElementNS("http://www.w3.org/2000/svg","rect");
				 	
				 	j.setAttributeNS(null,"x",150 - v / 2);
				 	j.setAttributeNS(null,"y",index*5);
				 	j.setAttributeNS(null,"width",v);
				 	j.setAttributeNS(null,"height",3);
				 	j.setAttributeNS(null,"stroke-width","1");
				 	j.setAttributeNS(null,"fill", "#3a9cd6");
				 	
				 	document.getElementById('txWaveSvgProg').appendChild(j);
				 	
				});
			 }
		 }

		 /**
			* @define RecseePlayer.updateWave
			* @type function
			* @description wave의 높이값을 설정하고 wave를 갱신한다.
			*  파라미터 값이 없다면 rc.currentWave에 저장된 값으로 wave를 갱신한다
			* @param arr(array) wave 높이 배열
			*/
			rc.updateWave = function(path, isTx) {
				var height = parseInt(rxAudio.duration) * 10;					//	길이는 rx기준으로 이미지를 만든다
				var dataStr = {
							"url" : path
						,	"listenType" : ((rc.upload) ? "upload" : "none")
					}

				var requestUrl = "http://"+rc.requestIp+":"+rc.requestPort+"/waveStt";

				function requestWave(handler){
					$.ajax({
						crossDomain:true,
							url :requestUrl,
						data:dataStr,
						type:"POST",
						dataType:"json",
						cache: false,
						success:function(jRes){
							handler(jRes.data)
						}
					});
				}

				requestWave(function (data){
					var heightArray = data||rc.currentWave;
					 if(!isTx){
						 	// progress 파형
						 	rxWave.append('<div id="rxWaveProgWrap"></div>');
						 	
						 	//	svg 태그 만들기
						 	var svg = document.createElementNS("http://www.w3.org/2000/svg","svg");
						 	svg.setAttributeNS(null,"id","rxWaveSvg");
						 	svg.setAttributeNS(null,"height",height*5);
						 	svg.setAttributeNS(null,"width",300);
						 	rxWave.get(0).appendChild(svg);
						 	
						 	//	svg 태그 만들기
						 	var svgProg = document.createElementNS("http://www.w3.org/2000/svg","svg");
						 	svgProg.setAttributeNS(null,"id","rxWaveSvgProg");
						 	svgProg.setAttributeNS(null,"height",height*5);
						 	svgProg.setAttributeNS(null,"width",300);
						 	
						 	rxWave.find("#rxWaveProgWrap").get(0).appendChild(svgProg);
						 	
							rxWave.setWaveHeight(heightArray);
							
							/*$('.rxWaveObj').draggable({
								axis : "y"
							});*/
							
							
							/*
							 *	@Auth David
							 *	@Description 사이드 틱차트 그리기 위해서 삽입
							 * 						Rx 에서 한번만 실행...
							 */
							(function sideTickLoad(){
								for(var i = 0 ; i < height ; i++){
									
									if(i % 60 == 0 && i != 0){
										var k = seconds2time((i / 10) -1 );
										$body.find(".stt_side_view").append("<div class='side_tick_txt'>"+k+"</div>");
									}else{
										$body.find(".stt_side_view").append("<div class='side_tick'></div>");	
									}
									if(i == height - 1 ){
										HoldOn.close();
									}
								}
							}());
					 }else{
						 	// progress 파형
						 	txWave.append('<div id="txWaveProgWrap"></div>');
						 	
						 	//	svg 태그 만들기
						 	var svg = document.createElementNS("http://www.w3.org/2000/svg","svg");
						 	svg.setAttributeNS(null,"id","txWaveSvg");
						 	svg.setAttributeNS(null,"height",height*5);
						 	svg.setAttributeNS(null,"width",300);
						 	txWave.get(0).appendChild(svg);
						 	
						 	//	svg 태그 만들기
						 	var svgProg = document.createElementNS("http://www.w3.org/2000/svg","svg");
						 	svgProg.setAttributeNS(null,"id","txWaveSvgProg");
						 	svgProg.setAttributeNS(null,"height",height*5);
						 	svgProg.setAttributeNS(null,"width",300);
						 	
						 	txWave.find("#txWaveProgWrap").get(0).appendChild(svgProg);
						 	
							txWave.setWaveHeight(heightArray);
							
							/*$('.txWaveObj').draggable({
								axis : "y"
							});*/
					 }
					 $body.find(".rx_wrap, .tx_wrap").height(height*5)
					 $body.find(".stt_side_view, .stt_center_view, .stt_right_view").height(height*5 + 300)
					 $body.find(".stt_playing_wrap").height(height*4.4 )
				});
			}
		
		 /**
		 * @define RecseePlayer.updateMiniMap
		 * @type function
		 * @description stt 플레이어 미니맵 을 표시 해줌
		 * @param u : url , i : 이미지 넣어줄 id 값 , c : 복사 이미지 넣어줄 아이디 값 
		 */
		 rc.updateMiniMap = function( u , i , c){
			 $("#"+i)[0].src = "http://"+rc.requestIp+":"+rc.requestPort+"/waveSttMini?url="+ u;
			 $("#"+i).clone().appendTo("#" + c );
			 
			 // 미니맵 추가후 이미지 드래그 방지 코드
			 $('img').on('dragstart',function(e){
					e.preventDefault();
			 })
		 };
		 
		 /**
		 * @define RecseePlayer.sttParseValue
		 * @type function
		 * @description Stt받은XML 파일 파싱하여 내부에 저장
		 *  				   
		 * @param txt(파싱 데이터) , rx  ( rx 인지 여부)
		 */
		 this.sttParseValue = function( rx , tx ){
			var parser = new DOMParser();
			var rxDoc = parser.parseFromString(rx,"text/xml");
			var txDoc = parser.parseFromString(tx,"text/xml");
			var rxTag = rxDoc.getElementsByTagName('p');
			var txTag = txDoc.getElementsByTagName('p');
			
			var len = (rxTag.length > txTag.length) ? rxTag.length : txTag.length;
			
			for(var i = 0 ; i < len ; i++){
				if(rxTag[i] != undefined){
					//rc.log("===============rxChar===================");
					//rc.log(rxTag[i]);
					rxTag[i].setAttribute("key","RX");
					var rxbegin = Number(rxTag[i].getAttribute("begin").replace("s",""));
					rc.XML[rxbegin] = rxTag[i];
				}
				if(txTag[i] != undefined){
					//rc.log("===============txChar===================");
					//rc.log(txTag[i]);
					txTag[i].setAttribute("key","TX");
					var txbegin = Number(txTag[i].getAttribute("begin").replace("s",""));
					rc.XML[txbegin] = txTag[i];
				}
			}
			
			
		 }
		 
		 /**
		 * @define RecseePlayer.sttParserInsert
		 * @type function
		 * @description Stt받은XML 파일 파싱하여 플레이어 아래 칸에 넣어줌
		 *  				   추가로 가운데 텍스트 구간 표시
		 * @param txt(파싱 데이터) , rx  ( rx 인지 여부)
		 */
		rc.sttParserInsert = function( rx , tx ){

			var parser = new DOMParser();
			var rxDoc = parser.parseFromString(rx,"text/xml");
			var txDoc = parser.parseFromString(tx,"text/xml");
			var rxTag = rxDoc.getElementsByTagName('p');
			var txTag = txDoc.getElementsByTagName('p');
			var SortMin,temp;
			var TempArr = [];
			
			var len = (rxTag.length > txTag.length) ? rxTag.length : txTag.length;
			
			var Height = 0;
			
			for(var i = 0 ; i < len ; i++){
				var rxbegin,txbegin,rxend,txend;
				var rxb,rxe,txb,txe;
				
				if(rxTag[i] != undefined){
					rxTag[i].setAttribute("key","RX");
					TempArr.push(rxTag[i]);									// 아래 텍스트 때문에 배열에 넣어줌 ..
					rxbegin = rxTag[i].getAttribute("begin");			//	Rx 시작
					rxend = rxTag[i].getAttribute("end");				//	Rx 종료

					//	센터 쪽 위치 표시 밑 텍스트 추가 
					rxb = Number(rxbegin.replace("s",""));
					rxe = Number(rxend.replace("s",""));
					var gap = rxe - rxb;
					$(".stt_center_view").append("<div class='stt_center_rx_text_box' style='margin-top:"+rxb * 50+"px;'></div>")
					$(".stt_center_rx_text_box").eq(i).css("height",gap * 50+"px");
					
					if(Height == 0)
						Height = rxb * 50;
					else
						Height = (rxb * 50) + ($(".stt_text_wrap").last().height() * 2.2);
					
					$(".stt_rx_view").append("<div class='stt_rx_text_wrap stt_text_wrap' style='margin-top:"+Height+"px;' begin='"+rxb+"' end='"+rxe+"'>"
							+"<div class='rx_content_box contents_wrap display_none'>"
							+"<div class='player_content_replay_btn'></div>"
							+"<div class='player_content_repeat_btn'></div>"
							+"<div class='player_content_marking_btn'></div>"
							+"</div>"
							+rxTag[i].textContent
							+"</div>");
				}
				if(txTag[i] != undefined){
					txTag[i].setAttribute("key","TX");							
					TempArr.push(txTag[i]);									// 아래 텍스트 때문에 배열에 넣어줌 ..
					txbegin = txTag[i].getAttribute("begin");			//	Tx 시작
					txend = txTag[i].getAttribute("end");				//	Tx	종료
					
					//	센터 쪽 위치 표시 밑 텍스트 추가 
					txb = Number(txbegin.replace("s",""));
					txe = Number(txend.replace("s",""));
					var gap = txe - txb;
					$(".stt_center_view").append("<div class='stt_center_tx_text_box' style='margin-top:"+txb * 50+"px;'></div>")
					$(".stt_center_tx_text_box").eq(i).css("height",gap * 50+"px");
					
					if(Height == 0)
						Height = txb * 50;
					else
						Height = (txb * 50) + ($(".stt_text_wrap").last().height() * 2.2);
					
					$(".stt_tx_view").append("<div class='stt_tx_text_wrap stt_text_wrap' style='margin-top:" + Height +"px;'  begin='"+txb+"' end='"+txe+"'>"
							+"<div class='tx_content_box contents_wrap display_none'>"
							+"<div class='player_content_replay_btn'></div>"
							+"<div class='player_content_repeat_btn'></div>"
							+"<div class='player_content_marking_btn'></div>"
							+"</div>"
							+txTag[i].textContent
							+"</div>");
				}
			}
			
			/**
			 * @Auth David
			 * @Description 빈 배열에 값 넣어서 선택정렬 이용하여 시작 시간 기준으로 다시 정렬하여 아래 텍스트 창에 넣어줌..
			 */
			for(var i= 0 ; i < TempArr.length-1 ; i++){
				var beforeBegin,begin,temp;
				SortMin = i;
				for(var j = i + 1 ; j < TempArr.length ; j++){
					var beforeBegin =Number(TempArr[SortMin].getAttribute("begin").replace("s",""));
					var begin = Number(TempArr[j].getAttribute("begin").replace("s",""));
					if(beforeBegin > begin){
						SortMin = j;
					}
				}
				temp = TempArr[i];
				TempArr[i] = TempArr[SortMin];
				TempArr[SortMin] = temp;
			}
			
			TempArr.forEach(function(val , idx ){
				var flag = val.getAttribute("key");
				var begin = Number(val.getAttribute("begin").replace("s",""));
				
				if(flag == "RX"){
					sttAllText.append("<div class='rx_text_wrap'><span class='rx_t'>고객<font begin="+begin+">["+seconds2time(begin)+"]</font></span><span>:</span></div>");
					$(".rx_text_wrap").last().append(val);
					
				}else{
					sttAllText.append("<div class='tx_text_wrap'><span class='tx_t'>상담사<font begin="+begin+">["+seconds2time(begin)+"]</font></span><span>:</span></div>");
					$(".tx_text_wrap").last().append(val);
				}
			})
			
			
			/*****************************************************************************************************/
			/*	
			 * 	@Auth David
			 *  @Description 	문장 로드후 클릭 이벤트 처리 로드후 이벤트라 다른곳에선 잘 안 먹힘..
			 *  
			 */
	        $player.find(".stt_text_wrap").mouseenter(function(){
	        	$(this).find(".rx_content_box").removeClass("display_none");
	        	$(this).find(".tx_content_box").removeClass("display_none");
	        })
	        
	        /**
	         * @Auth David
	         * @Description 텍스트 재생 버튼 함수
	         *	 
	         */
	        $player.find(".contents_wrap .player_content_replay_btn , .contents_wrap .player_content_repeat_btn").click(function(){
	        	rc.log("=============== Contents Click ===================");
	        	rc.viewFix = true;																		// 뷰 고정
	        	var begin = $(this).parents(".stt_text_wrap").attr("begin");				// 시작
	        	var end = $(this).parents(".stt_text_wrap").attr("end");					// 끝
	        	
	        	syncDo("currentTime" , begin);
	        	syncDo("play");
	        	
	        	// 시작 버튼이 일시정지 모양이면 재생 버튼으로 바꿔주기
	        	if(rc_player.player.find("audio")[0].paused){
	        		$("#Start").removeClass("player_pause");
	        		$("#Start").addClass("player_playing");
	        	}else{
	        		$("#Start").addClass("player_pause");
	        		$("#Start").removeClass("player_playing");
	        	}
	        	
	        	if($(this).hasClass("player_content_repeat_btn")){
	        		//	반복 재생 중일 때 작업..
	        		if($(this).hasClass("player_content_repeat_stop_btn")){
	        			$(this).removeClass("player_content_repeat_stop_btn");
	        			rc.repeat = false;
	        			syncDo("pause");
		        		$("#Start").removeClass("player_pause");
		        		$("#Start").addClass("player_playing");
	        			return false;
	        		}
	        		$(this).addClass("player_content_repeat_stop_btn");
	        		rc.repeat = true;
	        	}else{
	        		rc.repeat = false;
	        	}
	        	rc.sTime = begin;
	        	rc.eTime = end;
	        })
		        
		      /**
	         * @Auth David
	         * @Description 텍스트 구간에서 구간 선택 버튼 함수
	         */
	        
	        $player.find(".contents_wrap .player_content_marking_btn").click(function(){
	        	var begin = $(this).parents(".stt_text_wrap").attr("begin");				// 시작
	        	var end = $(this).parents(".stt_text_wrap").attr("end");					// 끝
	        	
	        	rc.makeSection(begin , end );
	        })
	        
	        $player.find(".stt_text_wrap").mouseleave(function(){
	        	$(this).find(".rx_content_box").addClass("display_none");
	        	$(this).find(".tx_content_box").addClass("display_none");
	        })
		}
		
		 /**
		 * @define RecseePlayer.setSttChar
		 * @type function
		 * @description 파일의 STT 텍스트 형태의 파일을 표출해줌
		 *  recDate , recTime, ext (필)
		 * @param 
		 */
		 this.setSttChar = function(r, t ,e){
			 $.ajax({
					url: contextPath+"/player/getListType1",
				 	dataType: "json",
				 	data : {
				 			recDate : r
				 		,	recTime : t
				 		,	ext : e
				 	},
					async: false,
				 	cache: false,
					success: function(res){
						var rxText = res.resData.rxText;
						var txText = res.resData.txText;
						
						//	rx , tx  (XML  파싱 ) 
						rc.sttParseValue(rxText , txText );
					}
				 });
		 }; 
		
		 /**
		 * @define RecseePlayer.setSttTypeSentence
		 * @type function
		 * @description 파일의 STT 문장 형태의 파일을 표출해줌
		 *  recDate , recTime, ext (필)
		 * @param  r(recDate), t(recTime) , e(Ext)
		 */
		 rc.setSttTypeSentence = function(r,t,e){
			 $.ajax({
				url: contextPath+"/player/getListType2",
			 	dataType: "json",
			 	data : {
			 			recDate : r
			 		,	recTime : t
			 		,	ext : e
			 	},
				async: false,
			 	cache: false,
				success: function(res){
					var rxText = res.resData.rxText;
					var txText = res.resData.txText;
					
					//	rx , tx  (XML  파싱 ) 
					rc.sttParserInsert(rxText , txText );
				}
			 });
			 rc.fileInfo(r ,t , e);
		 }
			
			
		 /**
		 * @define RecseePlayer.setSttTypeText
		 * @type function
		 * @description 파일의 STT 텍스트 형태의 파일을 표출해줌
		 *  recDate , recTime, ext (필)
		 * @param r(recDate), t(recTime) , e(Ext)
		 */
		 rc.setSttTypeText = function(r,t,e){
			 $.ajax({
				url: contextPath+"/player/getListType3",
			 	dataType: "json",
			 	data : {
			 			recDate : r
			 		,	recTime : t
			 		,	ext : e
			 	},
				async: false,
			 	cache: false,
				success: function(res){
					rc.log(res);
				}
			 })
		 }; 
		 
		 /**
			 * @define RecseePlayer.FileInfo
			 * @type function
			 * @description 파일정보를 리턴 해줌...
			 * @param r(recDate), t(recTime) , e(Ext)
			 */
		 rc.fileInfo = function(r , t , e){
			$.ajax({
				url : contextPath+"/player/getFileInfo",
				dataType : "json",
				data : {
						recDate : r
					,	recTime : t
					,	ext : e
				},
				async: false,
			 	cache: false,
				success: function(res){
					sttFileInfo.find("#infoRecDate").html(getDateFormat(res.resData.recDate));
					sttFileInfo.find("#infoRecTime").html(getTimeFormat(res.resData.recTime));
					sttFileInfo.find("#infoUserName").html(res.resData.rUserName);
					sttFileInfo.find("#infoExt").html(res.resData.Ext);
					sttFileInfo.find("#infoCustPhone").html(getPhoneFormat(res.resData.rCustPhone));
					
					// rec 파일 정보 저장
					rc.recFileData = res.resData;
				}
			})
		 }
		 
		 /**
		 * @define RecseePlayer.log
		 * @type function
		 * @description 디버깅 모드 일대 콘솔 로그 찍기 편하게 하기 위해서 제작
		 * @param t : 찍어줄 로그
		 */
		 this.log = function(t){
			 if(rc.DEBUG)
				 console.log(t);
		 }; 
		 
		 /**
		 * @define RecseePlayer.setLocalStorage
		 * @type function
		 * @description 로컬 스토리지 저장
		 * @param k : key  ,  v : value 
		 */
		 this.setLocalStorage = function( k , v){
			 localStorage.setItem(k , v);
		 }; 
		 
	    /**
	     * @define RecseePlayer.makeSection
	     * @type function
	     * @description 플레이어에 섹션을 만들어 준다.
	     * @param startTime 섹션의 시작 시간
         * @param endTime 섹션의 종료 시간
         * @param startP 섹션의 시작 점 %
         * @param widthP 섹션의 종료 점 %
         * @param memoInfo 메모 정보 obj (memoIdx;메모 인덱스, memo;메모내용)
	     */
	    this.makeSection = function(startTime, endTime, startP, widthP , memoInfo) {
            
        	// 시작점 % (css의 left 부분)
        	var sp = percentPos(startTime);
        	// 섹션의 너비 % (css의 width 부분)
        	var wp = percentPos(endTime - startTime);
        	
        	// 100퍼센트 못넘게 처리
        	if (sp+wp >= 100)
        		wp = 100-sp
        	
        	var htmlString = "<ul>";
        	
        	//메모 사용 할 때만 사용
        	if(rc.type[TYPES.TAG])
        		htmlString += "<li class='memoMarking'>"+top.$("#pageFrame")[0].contentWindow.lang.views.player.html.text9/*플레이어 태그*/+"</li>";
        	        	
        	if(rc.type[TYPES.BESTCALL]){
        		htmlString += "<li class='muteMarking'>"+top.$("#pageFrame")[0].contentWindow.lang.views.player.html.text20/*묵음*/+"</li>"
    			htmlString += "<li class='muteMarkingDel' style='display:none;'>"+top.$("#pageFrame")[0].contentWindow.lang.views.player.html.text21/*묵음해제*/+"</li>"
				htmlString += "<li class='cutoffMarking'>"+top.$("#pageFrame")[0].contentWindow.lang.views.player.html.text22/*제거*/+"</li>"
				htmlString += "<li class='cutoffMarkingDel' style='display:none;'>"+top.$("#pageFrame")[0].contentWindow.lang.views.player.html.text23/*제거해제*/+"</li>"
        	}
    			
			htmlString += "<li class='deleteMarking'>"+top.$("#pageFrame")[0].contentWindow.lang.views.player.html.text24/*삭제*/+"</li>"
        	htmlString += "</ul>";
        	
        	htmlString += "<ul>";
        	htmlString += "<li class='marking_front_move'>앞으로 보내기</li>"
    		htmlString += "<li class='marking_back_move'>뒤로 보내기</li>"
        	htmlString += "</ul>";
            
        	var memoIdx = "";
        	
        	if(memoInfo != undefined){
        		memoIdx = memoInfo.memoIdx;
        	}
        	
        	//	구간 지정 마킹 Wrap 감싸기
        	$player.find(".stt_play_map").append("<div class='marking_wrap' id='idx_"+memoIdx+"' style='top:"+sp+"%;height:"+wp+"%' ><div class='marking_wrap_box'></div><div>"+htmlString+"</div></div>");
        	
        	var nowFinishMarkingObj = $player.find(".marking_wrap:last-of-type");
        	
        	var nowPlayerMarkingObjBtn = nowFinishMarkingObj.find("ul");
        	
        	nowFinishMarkingObj.data(memoInfo);
        	
        	nowPlayerMarkingObjBtn.hide();
        	
	    	if(rc.type[TYPES.TAG]  && memoInfo == undefined){
	    		var dataStr = {
	    					"recDate" 	: rc.recFileData.recDate.replace(/-/gi,'')
	    				,	"recTime" 	: rc.recFileData.recTime.replace(/:/gi,'')
						,	"extNum" 	: rc.recFileData.Ext
						,	"startTime" : startTime
						,	"endTime" 	: endTime
						,	"proc" 		: "insert"
	    		};
	    		
	    		//---- MEMO MARKING WRAP INSERT
				$.ajax({
					url: contextPath+"/recMemoProc.do",
					data: dataStr,
					type: "POST",
					dataType: "json",
					cache: false,
					success: function(jRes) {
						rc.log("=============== Rec Tag Success ===================");
						
						$(".marking_wrap").last().data("memoIdx",jRes.resData.memoIdx);
						$(".marking_wrap").last().data("st",startTime);
						$(".marking_wrap").last().data("et",endTime);

					}
				});
	    	}
				//----- MEMO MARKING WRAP MUTE EVENT
	    		// 음소거
	    		nowFinishMarkingObj.find(".muteMarking").click(function(){
	    			setTimeout(function(){
	        			nowPlayerMarkingObjBtn.hide();
	        			nowFinishMarkingObj.find(".marking_wrap_box").removeClass("markingCutoff").addClass("markingMute");
	        			nowFinishMarkingObj.data({"st":startTime, "et":endTime});
	        			nowFinishMarkingObj.find(".muteMarking, .cutoffMarkingDel").hide()
	        			nowFinishMarkingObj.find(".muteMarkingDel, .cutoffMarking").show()
	    			}, 100);
	    		});
	    		
				//----- MEMO MARKING WRAP MUTE EVENT
	    		// 음소거 해제
	    		nowFinishMarkingObj.find(".muteMarkingDel").click(function(){
	    			setTimeout(function(){
	        			nowPlayerMarkingObjBtn.hide();
	        			nowFinishMarkingObj.find(".marking_wrap_box").removeClass("markingMute");
	        			nowFinishMarkingObj.data({"st":"", "et":""});
	        			nowFinishMarkingObj.find(".muteMarkingDel").hide()
	        			nowFinishMarkingObj.find(".muteMarking").show()
	    			}, 100);
	    		});
	    		
	    		//----- MEMO MARKING WRAP CUTTING EVENT
	    		// 제거
	    		nowFinishMarkingObj.find(".cutoffMarking").click(function(){
	    			setTimeout(function(){
	    				nowPlayerMarkingObjBtn.hide();
	        			nowFinishMarkingObj.find(".marking_wrap_box").removeClass("markingMute").addClass("markingCutoff");
	        			nowFinishMarkingObj.data({"st":startTime, "et":endTime});
	        			nowFinishMarkingObj.find(".cutoffMarking, .muteMarkingDel").hide()
	        			nowFinishMarkingObj.find(".cutoffMarkingDel, .muteMarking").show()
	    			}, 100);
	    		});

	    		// 제거해제
	    		nowFinishMarkingObj.find(".cutoffMarkingDel").click(function(){
	    			setTimeout(function(){
	    				nowPlayerMarkingObjBtn.hide();
	        			nowFinishMarkingObj.find(".marking_wrap_box").removeClass("markingCutoff")
	        			nowFinishMarkingObj.data({"st":"", "et":""});
	        			nowFinishMarkingObj.find(".cutoffMarkingDel").hide()
	        			nowFinishMarkingObj.find(".cutoffMarking").show()
	    			}, 100);
	    		});
				
				//----- MEMO MARKING WRAP MEMO POPUP
	    		// 메모
				nowFinishMarkingObj.find(".memoMarking").click(function(){
					var memoInfo = {
							"memoIdx" : nowFinishMarkingObj.data("memoIdx")
						,	"memo" : nowFinishMarkingObj.data("memo")
						,	"userName" : nowFinishMarkingObj.data("userName")
						,	"userId"	 : nowFinishMarkingObj.data("userId")
						,	"recDate" : rc_player.recFileData.recDate.replace(/-/gi,'')
						,	"recTime" : rc_player.recFileData.recTime.replace(/:/gi,'')
						,	"extNum" : rc_player.recFileData.Ext
					}
					rc.log(memoInfo);
					memoPopup(memoInfo , nowFinishMarkingObj);
				});
				
				
				//----- MEMO MARKING WRAP DELETE
				nowFinishMarkingObj.find(".deleteMarking").click(function(){
					if(confirm("삭제 하시겠습니까?")){
						// 메모 사용 할 때만 사용
		            	if(rc.type[TYPES.TAG]){
							var memoIdx = nowFinishMarkingObj.data("memoIdx");
							var dataStr = {
									"memoIdx" :memoIdx
								,	"recDate" : rc_player.recFileData.recDate.replace(/-/gi,'')
								,	"recTime" : rc_player.recFileData.recTime.replace(/:/gi,'')
								,	"extNum" : rc_player.recFileData.Ext
								,	"proc" : "delete"
							}

							$.ajax({
								url: contextPath+"/recMemoProc.do",
								data: dataStr,
								type: "POST",
								dataType: "json",
								cache: false,
								success: function(jRes) {
									nowFinishMarkingObj.remove();
									alert("플레이어 태그 삭제가 완료 되었습니다.")

								}
							});
		            	}

		            }
				});
				
				
				nowFinishMarkingObj.find(".marking_front_move").click(function(){
					rc.log("===============MARKING FRONT MOVE EVENT ================");
					var markingMv = rc._RC.MARKING;
					markingMv.backMarking.insertAfter(markingMv.frontMarking);
					nowPlayerMarkingObjBtn.hide();
				});

				nowFinishMarkingObj.find(".marking_back_move").click(function(e){
					rc.log("===============MARKING BACK MOVE EVENT ================");
					var markingMv = rc._RC.MARKING;
					markingMv.frontMarking.insertAfter(markingMv.backMarking);
					nowPlayerMarkingObjBtn.hide();
				});
	    	
	    	(function(){
	    		
	    		//	구간 우클릭 이벤트 처리
	    	  	if (rc.type[TYPES.MENUCLICKRIGHT]){
	    	  		rc.log("================ Recsee Player MenuRight Click Event ==============");
	    	  		nowFinishMarkingObj.off("mousedown")
	            	nowFinishMarkingObj.on("mousedown",function(e){
	            		if(e.button ==2){
	            			e.stopPropagation();
	                	    $('.marking_wrap div ul').hide();
	                	    var menuObj = $(this).find('ul');
	                	    if(menuObj.is(":visible"))
	                	    	menuObj.hide();
	                	    else
	                	    	menuObj.show();
	            		}
	            	});
            	}

	    	  // 바디 클릭 시 우클릭 메뉴 전부 숨기기
    		  $('body').click(function() {
    			  	rc.log("================= RecSee Player Body Event ================");
                	$('.marking_wrap div ul').hide();
              });
	    		
	          setTimeout(function(){
	        	  if(memoInfo == undefined)
	            		nowPlayerMarkingObjBtn.show()
	          },100)
	    	}())
        	
        	function memoPopup( memoInfo , sectionObj ){
    			var memoIdx = memoInfo.memoIdx;
    			var memo = memoInfo.memo;
    			var recDate = memoInfo.recDate;
    			var recTime = memoInfo.recTime;
    			var extNum = memoInfo.extNum;
    			var userName = memoInfo.userName;
    			var userId = memoInfo.userId;
    			
    			$("#memoContents").val(memo);
    			$("#memoAdd").off("click");
    			$("#memoAdd").on("click",function(){
    				var savedMemo = $("#memoContents").val();
    				
    				var dataStr = {
    						"memoIdx" :memoIdx
    					,	"memo" : savedMemo
    					,	"recDate" 	: recDate
    					,	"recTime" 	: recTime
    					,	"extNum" 	: extNum
    					,	"proc" : "modify"
    				}
    				
    				//-------------  MODIFY
    				$.ajax({
    					url: contextPath+"/recMemoProc.do",
    					data: dataStr,
    					type: "POST",
    					dataType: "json",
    					cache: false,
    					async: false,
    					success: function(jRes) {
    						//sectionObj.attr("title",savedMemo||'')
    						//sectionObj.attr("title","["+pageFrame.userInfoJson.userId+"]"+"["+pageFrame.userInfoJson.userName+"]");
    						sectionObj.data("memo",savedMemo);
    						alert("플레이어 태그 저장이 완료 되었습니다.");
    						layer_popup_close();
    					}
    				});
    				
    			});
    			layer_popup("#recMemo");
        	}
	    }
		 
		 
		 /**
		 * @define RecseePlayer.getLocalStorage
		 * @type function
		 * @description 로컬 스토리지 불러오기
		 * @param k : key 
		 */
		 this.getLocalStorage =  function( k ){
			 return localStorage.getItem(k);
		 };
		 
		 /**
		 * @define RecseePlayer.textToMarking
		 * @type function
		 * @description 왼쪽 아래 텍스트 구간 지정
		 */
		 this.textToMarking = function(){
			 
		 }; 
		 
		 
		 /**
		 * @define RecseePlayer.clearSearchText
		 * @type function
		 * @description 검색한 문자열 클리어 효과
		 */
		 this.clearSearchText = function(){
			 $(".stt_text_wrap .highlight").each(function(){
				$(this)[0].outerHTML = $(this)[0].innerHTML;
			});
		 }

		 //공통 이벤트
		 function setMediaEvent(target){
			 var $target = $(target);
			 var playerHeight = $body.height();

			 target.addEventListener('seeked',function(event){
				 var targetCurrentTime = target.currentTime;
				 
				 var rxWaveProg = $('#rxWaveProgWrap');
				 var txWaveProg = $('#txWaveProgWrap');
				 
				 rxWaveProg.css({"transition":""});
				 rxWave.css({"transition":""});
				 txWaveProg.css({"transition":""});
				 txWave.css({ "transition":""});
				 
				 rc_player.obj.rxWaveObj.find('#rxWaveProgWrap').height(target.currentTime * 50)
				 rc_player.obj.txWaveObj.find('#txWaveProgWrap').height(target.currentTime * 50)
			 })
			 
			 target.addEventListener('timeupdate',function(event){
				 var targetCurrentTime = target.currentTime;					// 현재 재생 시간
				 var targetDuration = target.duration;								// 총 길이
				 var currentTime = seconds2time(targetCurrentTime);
				 /*
				  |
				  |
				  |
				  */
				 
				 $player.find("#procTime").text(currentTime);
				 
				 /*-----------------------현재 시간 퍼센트 구간  표시 오른쪽 미니맵--------------------------------*/
				 
				 var per = percentPos(targetCurrentTime);
				 var currentHP = $("#currentWrap").height() / $(".stt_play_map").height() * 100;
				 
				 var resPer = parseInt(per) + "%";
				 if(per + currentHP > 100){
					 resPer = "calc(100% - "+$("#currentWrap").css("height")+")";
				 }
				 
				 $(".stt_play_current_wrap").css({"top" : resPer});
				 
				 /**
				 |	@David
				 |	@description RX / TX 구분하여 웨이브 유동적으로 움직여줌
				 */
				 if (target.parentElement.className.match("audio_rx")){

					 var sttView = $('.stt_playing_wrap');
					 var rxWaveProg = $('#rxWaveProgWrap');
					 var txWaveProg = $('#txWaveProgWrap');
					 var miniMap = $(".stt_play_current_wrap");
					 
					 if(target.currentTime > rc.eTime){
				        	if(rc.repeat == false){
				        		syncDo("pause");
				        		rc.eTime = undefined;	
				        	}else{
				        		syncDo("currentTime" , rc.sTime);
				        	}
					 }
					 
					 if(!target.paused){
						 rxWaveProg.css({"height":targetCurrentTime*50+50+"px" , "transition":"all 1s"});
						 txWaveProg.css({"height":targetCurrentTime*50+50+"px" , "transition":"all 1s"});
						 
						 rxWave.css({"transition":"all 0.5s"});
						 txWave.css({"transition":"all 0.5s"});
						 sttView.css({"transition":"all 0.5s"});
						 
//						 rxWave.css({"margin-top":"-"+(targetCurrentTime*50 >= 300 ? targetCurrentTime*50 - 300 : "0" )+"px" , "transition":"all 1s"});
//						 txWave.css({"margin-top":"-"+(targetCurrentTime*50 >= 300 ? targetCurrentTime*50 - 300 : "0" )+"px" , "transition":"all 1s"});
//						 sttView.css({"margin-top":"-"+(targetCurrentTime*50 >= 300 ? targetCurrentTime*50 - 300 : "0" )+"px" , "transition":"all 1s"});
					 }else{
						 rxWaveProg.css({"transition":""});
						 txWaveProg.css({"transition":""});
						 rxWave.css({"transition":""});
						 txWave.css({ "transition":""});
						 sttView.css({ "transition":""});
					 }
				 }
			 })

			 target.addEventListener('loadeddata',function(){
				 console.log('loadeddata')
				 //	 녹취 시작 가능 할때 전체 사이즈 와 하번에 보이는 10초 기준으로 미니맵 현재 구간 표시
				 $(".stt_play_current_wrap").css("height", Number(10 / target.duration * 100) + "%");
				 
				 // Wave 호출
				rc.updateWave(getParameterFromUrl("url", rc.requestParam),false);	
				rc.updateMiniMap(getParameterFromUrl("url", rc.requestParam) , "rxMiniMap" , "rxMiniMapInner");
				
				 setInterval(function(){
					moveView();
				 },2500);
					
				 
				//	태그 사용시 저장 된 태그 가지고 오기
				if(rc.type[TYPES.TAG]){
					var dataStr = {
							"recDate" 	: rc.recFileData.recDate
						,	"recTime" 	: rc.recFileData.recTime
						,	"extNum"		: rc.recFileData.Ext
						,	"proc" 		: "select"
					};
					
					$.ajax({
						url: contextPath+"/recMemoProc.do",
						data: dataStr,
						type: "POST",
						dataType: "json",
						async : false,
						cache: false,
						success: function(jRes) {
							var data = jRes.resData.recMemo;
							var length = 0;
							try{
								length = data.length;
							}catch(e){
								rc.log(e)
							}
							
							for(var i = 0 ; i < length ; i++){
								rc.log(data[i]);
								rc.makeSection(data[i].startTime , data[i].endTime ,undefined , undefined , data[i]);
								
								var recStime = seconds2time(data[i].startTime);
								var userId = (data[i].userId != null) ? data[i].userId : "";
								var userName = (data[i].userName != null) ? data[i].userName : "";
								var memo = (data[i].memo != null) ? data[i].memo : "";
								
								var str = "<div class='tag_memo_box' memoIdx='"+data[i].memoIdx+"'><p class='tag_memo_time'>"+recStime + "</p>"
											+ "<p class='tag_memo_p'><span class='tag_memo_user'>["+userId+" , " +userName + "] : </span><span class='tag_memo_text'>"+memo+"</span>"+"<span class='player_tag_go'></span>"+"</p></div>";
								
								$player.find(".player_tag_infomation").append(str)
								
							}							
						}
					});
				}

			 })
			 
			 target.addEventListener('play',function(){
				 console.log('play')
			 })
			 
			 target.addEventListener('pause',function(){
				 console.log('pause')
			 })
			 
		 	 target.addEventListener('playing',function(){
		 		 console.log('playing')
			 })

			 //	 녹취 파일이 재생 가능할때 이벤트
			 //	 재생 가능할때 웨이브 업데이트 시켜줌...
			 target.addEventListener('canplay',function(){
				 
				 console.log('canplay')
/*
				 var agent = navigator.userAgent.toLowerCase();

				if (agent.indexOf("chrome") != -1) {
					console.log('크롬에서 두번이상 이벤트 탐')
					canplayCheck++;
					if(canplayCheck>2)
						return false;
					
				}
				 
				 //	 녹취 시작 가능 할때 전체 사이즈 와 하번에 보이는 10초 기준으로 미니맵 현재 구간 표시
				 $(".stt_play_current_wrap").css("height", Number(10 / target.duration * 100) + "%");
				 
				 // RX Wave
				rc.updateWave(getParameterFromUrl("url", rc.requestParam+".RX.mp3"),false);	
				rc.updateMiniMap(getParameterFromUrl("url", rc.requestParam+".RX.mp3") , "rxMiniMap" , "rxMiniMapInner");
				//TX Wave
				rc.updateWave(getParameterFromUrl("url", rc.requestParam+".TX.mp3"),true);
				rc.updateMiniMap(getParameterFromUrl("url", rc.requestParam+".TX.mp3") , "txMiniMap" , "txMiniMapInner");
				
				 setInterval(function(){
					moveView();
				 },2500);
					
				 
				//	태그 사용시 저장 된 태그 가지고 오기
				if(rc.type[TYPES.TAG]){
					var dataStr = {
							"recDate" 	: rc.recFileData.recDate
						,	"recTime" 	: rc.recFileData.recTime
						,	"extNum"		: rc.recFileData.Ext
						,	"proc" 		: "select"
					};
					
					$.ajax({
						url: contextPath+"/recMemoProc.do",
						data: dataStr,
						type: "POST",
						dataType: "json",
						async : false,
						cache: false,
						success: function(jRes) {
							var data = jRes.resData.recMemo;
							var length = 0;
							try{
								length = data.length;
							}catch(e){
								rc.log(e)
							}
							
							for(var i = 0 ; i < length ; i++){
								rc.log(data[i]);
								rc.makeSection(data[i].startTime , data[i].endTime ,undefined , undefined , data[i]);
								
								var recStime = seconds2time(data[i].startTime);
								var userId = (data[i].userId != null) ? data[i].userId : "";
								var userName = (data[i].userName != null) ? data[i].userName : "";
								var memo = (data[i].memo != null) ? data[i].memo : "";
								
								var str = "<div class='tag_memo_box' memoIdx='"+data[i].memoIdx+"'><p class='tag_memo_time'>"+recStime + "</p>"
											+ "<p class='tag_memo_p'><span class='tag_memo_user'>["+userId+" , " +userName + "] : </span><span class='tag_memo_text'>"+memo+"</span>"+"<span class='player_tag_go'></span>"+"</p></div>";
								
								$player.find(".player_tag_infomation").append(str)
								
							}							
						}
					});
				}*/
			 })
			 
			 //	재생이 끝낫을때 이벤트
			 target.addEventListener('ended', function(){
				 // 반복재생 엑티브 시 처음으로 돌아가 다시 재생
				 if(rc.fullRepeat){
					 syncDo("currentTime" , "0");
					 syncDo("play");
				 }else{
		        	// 시작 버튼이 일시정지 모양이면 재생 버튼으로 바꿔주기
		        	if($("#Start").hasClass("player_pause")){
		        		$("#Start").removeClass("player_pause");
		        		$("#Start").addClass("player_playing");
		        	}
				 }
			 })
			 
	         // 앞, 뒤로가기 버튼 이벤트 정의
	        $player.find("#backward , #forward").on("click" , function(event) {
	            var type = $(this).hasClass("player_backward")?"prev":($(this).hasClass("player_forward")?"next":"");
	            if(type == "")  return;
	            // MOVE_TIME만큼 앞/뒤로 이동
	            var moveVal = target.currentTime + (rc.moveTime * (type == "prev"?-1:1));
	            rxAudio.currentTime = moveVal;
            	txAudio.currentTime = moveVal;
            	
	        	// 바로 그 구간 표시해주기
	        	setTimeout(function(){
	        		moveView();	
	        	},100);
	        });
			 
	         //  앞, 뒤로가기 버튼(앞뒤 버튼) 이벤트 정의
	        $player.find("#stepBackward , #stepForward").on("click" , function(event) {
	            var type = $(this).hasClass("player_step_backward")?"prev":($(this).hasClass("player_step_forward")?"next":"");
	            if(type == "")  return;
	            // MOVE_TIME만큼 앞/뒤로 이동
	            var moveVal = target.currentTime + (rc.moveTime * (type == "prev"?-1:1) * 2);
	            rxAudio.currentTime = moveVal;
            	txAudio.currentTime = moveVal;
            	
	        	// 바로 그 구간 표시해주기
	        	setTimeout(function(){
	        		moveView();	
	        	},100);
	        });
	        
	        //	플레이어 미니맵 클릭후 해당 구간으로 이동 정의
	        $player.find(".stt_play_map").click(function(event){
	        	//	선택 된 곳이 메뉴나 구간일때 false 처리..
	        	var targetClass = event.target.className;
	        	var targetArr = ["memoMarking" , "playMarkingLoop" , "downMarking" , "muteMarking" , "muteMarkingDel" , "cutoffMarkingDel" , "cutoffMarking" , "deleteMarking" , "marking_front_move" , "marking_back_move" , "current_box"];
	        	
	        	if(targetArr.indexOf(targetClass) > -1){
	        		event.preventDefault();
	        		return false;
	        	}
	        	
	        	var all = $(this).height();
	        	
	        	// 구간이동~~
	        	var choose = (event.clientY - 140) / all;					//	 offsetY 였으나 구간이 늘어날 시 offsetY 쓰면 안되어서 ClientY 로 교체
	        	rxAudio.currentTime = target.duration * choose ;
	        	txAudio.currentTime = target.duration * choose ;
	        	
	        	// 바로 그 구간 표시해주기
	        	setTimeout(function(){
	        		moveView();	
	        	},100);
	        	
	        })
		 }
		 
		 //RX 이벤트
		 rxWave.on('click',function(event){
			 rxAudio.currentTime = event.offsetY / 50;
			 txAudio.currentTime = event.offsetY / 50;
		 })
		 
		 //TX 이벤트
		 txWave.on('click',function(event){
			 rxAudio.currentTime = event.offsetY / 50;
			 txAudio.currentTime = event.offsetY / 50;
		 })
		 
		 
		 //	재생 일시정지 이벤트 정의
		 $(".btn").on('click',function(){
			 if($(this).hasClass("player_pause")){
				 $(this).removeClass("player_pause");
				 $(this).addClass("player_playing");
				 syncDo("pause");
			 }else	 if($(this).hasClass("player_playing")){
				 $(this).removeClass("player_playing");
				 $(this).addClass("player_pause");
				 syncDo("play");
				 rc.eTime = undefined;
				 rc.viewFix = false;
				 rc.repeat = false;
			 }
		 });
		 
		// 우수콜 등록
         $player.find('.best_call_submit_btn').on("click", function() {
         	
         	var shareTitle= $("#bestCallName").val().trim();
         	var userId= $("#bestUserId").val().trim();
         	var userName= $("#bestUserName").val().trim();
         	var saveIp= $("#saveIp").val().trim();
         	var ip= $("#ip").val().trim();
         	var bgCode= $("#bestBgCode").val().trim() == "null" ? '' : $("#bestBgCode").val().trim();
         	var mgCode= $("#bestMgCode").val().trim() == "null" ? '' : $("#bestMgCode").val().trim();;
         	var sgCode= $("#bestSgCode").val().trim() == "null" ? '' : $("#bestSgCode").val().trim();;
         	
         	if(shareTitle==''){
         		alert('우수콜 제목을 입력해 주세요');
         		return false;
         	}

         	var muteSt = new Array();
         	var muteEt = new Array();
         	var cutoffSt = new Array();
         	var cutoffEt = new Array();
         	var totalLength = $audio[0].duration;

         	var dataStr = {};

         	var mute = false;
         	var cutoff = false;
         	var encYn = true;

          	if(rc.requestParam == undefined){
         		alert("재생 중인 파일이 없습니다.");
         		return false;
         	}

         	var cmd="";
         	// 음소거 구간 확보
         	if ($player.find(".markingMute").length > 0){
         		$player.find(".markingMute").each(function(){
         			muteSt.push($player.find(".marking_wrap:last-of-type").data("st"));
         			muteEt.push($player.find(".marking_wrap:last-of-type").data("et"));
         		});
         		mute = true;
         	}

         	// 도려낼 부분(?) 확보
         	if ($player.find(".markingCutoff").length > 0){
         		$player.find(".markingCutoff").each(function(){
         			cutoffSt.push($player.find(".marking_wrap:last-of-type").data("st"));
         			cutoffEt.push($player.find(".marking_wrap:last-of-type").data("et"));
         		});
         		cutoff = true;
         	}

         	if (mute && cutoff){
         		dataStr = {
         				url 		: 	getParameterFromUrl("url", rc.requestParam)
         			,	cmd			:  	"cut_off_mute"
	            		,	mStartTime 	:	muteSt.toString()
	            		,	mEndTime 	:	muteEt.toString()
         			,	cStartTime	:	cutoffSt.toString()
         			,	cEndTime	:	cutoffEt.toString()
         			,	totalLength :   totalLength
         			,	encYn			: 	encYn
         			,	"shareTitle"	:	shareTitle
         			,	"userId"		:	userId
         			,	"userName"		:	userName
         			,	"ip"			:	ip
         			,	"bgCode"		:	bgCode
         			,	"mgCode"		:	mgCode
         			,	"sgCode"		:	sgCode
         			,	"saveIp"		:	saveIp
         		}
         	}else if(mute){
         		dataStr = {
         				url 		: 	getParameterFromUrl("url", rc.requestParam)
             		,	cmd			:  	"mute"
	            		,	mStartTime 	:	muteSt.toString()
	            		,	mEndTime 	:	muteEt.toString()
	            		,	encYn			: 	encYn
	            		,	"shareTitle"	:	shareTitle
	            		,	"userId"		:	userId
	            		,	"userName"		:	userName
	            		,	"ip"			:	ip
	            		,	"bgCode"		:	bgCode
         			,	"mgCode"		:	mgCode
         			,	"sgCode"		:	sgCode
         			,	"saveIp"		:	saveIp
         		}
         	}else if(cutoff){
         		dataStr = {
         				url 		: 	getParameterFromUrl("url", rc.requestParam)
             		,	cmd			:  	"cut_off"
         			,	cStartTime	:	cutoffSt.toString()
         			,	cEndTime	:	cutoffEt.toString()
         			,	totalLength :   totalLength
         			,	encYn			: 	encYn
         			,	"shareTitle"	:	shareTitle
         			,	"userId"		:	userId
         			,	"userName"		:	userName
         			,	"ip"			:	ip
         			,	"bgCode"		:	bgCode
         			,	"mgCode"		:	mgCode
         			,	"sgCode"		:	sgCode
         			,	"saveIp"		:	saveIp
         		}
         	}
         	
         	if(!cutoff&&!mute){
         		alert('녹취 파일의 묵음/제거 할 부분을 표시해 주세요');
         		return false;
         	}

         	rc.recFileData['encYn'] = encYn;
         	
         	if(!confirm("우수콜 추가를 하시겠습니까?")){
         		return false;
         	}
         	
         	//var src = "http://"+rc.requestIp+":"+rc.requestPort+"/down?"
         	//$player.find( ".rDownloadFrame" ).attr("src",src+$.param(dataStr));
         	$.ajax({
	    			url:contextPath+"/getlisten.do",
	    			data:dataStr,
	    			type:"POST",
	    			dataType:"json",
	    			async: false,
	    			success:function(jRes){
	    				console.log(jRes)
	    				if(jRes.success=="Y"){
	    					alert("우수콜 등재에 성공하였습니다.");
	    					//alert("파일 편집에 성공하였습니다.");
	    					window.self.close();
	    				}else {
	    					alert("우수콜 등재에 실패하였습니다.");
	    					//alert("파일 편집에 실패했습니다.");
	    				}
	    			}
	    		})

				
         });	
		 
		 /**
		  * 	@Auth David
		  * 	@Description 검색 기능 정의
		  * 						검색 결과에 표시
		  * */
		 //------------------------------------
		 //	엔터는 클릭 처리
		 $player.find("#searchText").keydown(function(event){
			 // Enter
			 if(event.keyCode == 13){
				 var txt = $player.find("#searchText").val();
				 $player.find(".input_focus_search").remove();	 
			    	
		    	if(txt != rc.currentSearchText){
		    		// 검색 글자와 다를 경우 clear 처리
		    		rc.clearSearchText();
		    		rc.currentSearchIdx = 0;
		    		rc.currentSearchText = txt;
		    		$player.find("#searchBtn").click();
		    	}else{
		    		 // 같을 경우
		    		
		    		var audioCurTime = $(".highlight").eq(rc.currentSearchIdx).parents(".stt_text_wrap").attr("begin");
		    		
		    		if(audioCurTime == undefined){
		    			rc.currentSearchIdx = 0;
		    			audioCurTime = $(".highlight").eq(rc.currentSearchIdx).parents(".stt_text_wrap").attr("begin");
		    		}
		    		
		        	rxAudio.currentTime = audioCurTime ;
		        	txAudio.currentTime = audioCurTime ;
		        	
		        	// 바로 그 구간 표시해주기
		        	setTimeout(function(){
		        		moveView();	
		        	},100);
		        	
		        	$(".highlight").parents(".stt_text_wrap").removeClass("hover")
		        	$(".highlight").eq(rc.currentSearchIdx).parents(".stt_text_wrap").addClass("hover")
		    		
		    		rc.currentSearchIdx += 1;
		    	}
			 }
			 
			 // ESC
			 if(event.keyCode == 27){
				 $player.find("#searchText").val("");
				 rc.currentSearchIdx = 0;
				 rc.currentSearchText = "";
				 rc.clearSearchText();
			 }
		 })
		 
		 // 인풋 박스에 포커스 들어올 경우
//		 $player.find("#searchText").focus(function(event){
//			 var storageT = rc.getLocalStorage("findTxt");
//			 if(storageT == null || storageT == undefined || storageT == ""){
//				 return;
//			 }
//			 
//			 var st = storageT.split("||/||");
//			 var txt = "<div class='input_focus_search'>";
//			 
//			 st.forEach(function(val , idx ){
//				 txt += "<div class='search_history_box'><span class='search_history_inner'>" 
//					 	+ val + "</span><span class='remove_search_history'>x</span></div>";
//			 })
//			 
//			 txt +="</div>";
//			 $(this).after(txt);
//			 
//		 })
		 //------------------------------------- 인풋 박스 셀렉 한 경우....
		 $(document).on("click",".search_history_inner",function(){
			 $player.find("#searchText").val("");
			 var txt = $(this).html() ;
			 $player.find("#searchText").val(txt);
    	})
		 
		 //------------------------------------- 인풋 박스 삭제 한 경우 ...
    	$(document).on("click",".remove_search_history",function(){
    		var a = $(this).parents(".search_history_box");
    		
    		var historyInner = a.find(".search_history_inner").html();			// 삭제 하려는 서치 히스토리
    		
			var storageT = rc.getLocalStorage("findTxt");
			var st = storageT.split("||/||");
			
			var txt = "";
			st.forEach(function(val , idx , array){
    			if(val == historyInner){
    				return;
    			}
    			//	 마지막 인덱스 에서는 구분값 제거 해서 넣어주기...
    			if ( idx === array.length -1){
    				txt += val;
    			}else{
    				txt += val + "||/||";
    			}
			})
			
			//	 storage 저장~
			rc.setLocalStorage("findTxt", txt );
    		a.remove();
    	})
		 
		 // 인풋 박스에 포커스 나간 경우
		 $player.find("#searchText").blur(function(event){
			 setTimeout(function(){
				 $player.find(".input_focus_search").remove();	 
			 },150);
		 })
		 
    	 //------------------------------------- 인풋 박스 위로 버튼 ...
 		 $player.find(".search_up_btn").click(function(event){
 			var storageT = rc.getLocalStorage("findTxt");
			var st = storageT.split("||/||");
			
			$player.find("#searchText").val(st[rc.seHistory]);
			
 			if(rc.seHistory > 0)
				rc.seHistory--;
		 })
		 
     	 //------------------------------------- 인풋 박스 아래로 버튼 ...
 		 $player.find(".search_down_btn").click(function(event){
 			var storageT = rc.getLocalStorage("findTxt");
			var st = storageT.split("||/||");
			
			$player.find("#searchText").val(st[rc.seHistory]);
			
			if(st.length - 1  > rc.seHistory)
				rc.seHistory++;
		 })
		 
		 
		//-------------------------------------
	    $player.find("#searchBtn").click(function(event){
	    	var txt = $player.find("#searchText").val();
	    	var inner = $player.find(".play_play_search_wrap .body");
	    	if(txt == ""){
	    		rc.clearSearchText();
	    		return false;
	    	}
	    	
	    	//----------------------
	    	inner.html("");			// 내용 클리어
	    	var innerTxt = "";
	    	//----------------------
	    	var storageVal = rc.getLocalStorage("findTxt");
	    	if(storageVal == "" || storageVal == undefined)
	    		rc.setLocalStorage("findTxt" , txt);
	    	else{
	    		var st = storageVal.split("||/||");
	    		
	    		var flag = false;
	    		st.forEach(function(val , idx , array ){
	    			//	 같으면 플래그 바꿔주기
	    			if(val == txt){
	    				flag = true;
	    				return;
	    			}
	    			//	 마지막 인덱스 까지 같은 값 없으면 스토리지 저장
	    			if ( idx === array.length -1 && flag == false){
	    				rc.setLocalStorage("findTxt" , txt+"||/||"+storageVal);		
	    			}
	    		})
	    	}
	    	
	    	// 검색 기능...
	    	var search_param = txt;
	    	var pattern = new RegExp(search_param, 'gi');
	    	
	    	

	    	$(".stt_text_wrap").each(function(){
	    	   $(this).html($(this).html().replace(
	    	       pattern, "<span class='highlight'>" + search_param +"</span>"
	    	       )
	    	   );

	    	});
	    	
//	    	Object.keys(rc.XML).forEach(function(val , idx){
//	    		var t = rc.XML[val];
//	    		// Find ...
//	    		if(t.textContent.indexOf(txt) != -1){
//	    			var begin = t.getAttribute("begin").replace("s","");
//	    			var end = t.getAttribute("end").replace("s","");
//	    			var key = (t.getAttribute("key") == "RX")?"고객" : "상담사";
//	    			innerTxt += "<div class='search_result_box' begin="+begin+" end="+end+">"
//	    						+ "<p>"+seconds2time(begin)+"</p>" 
//	    						+ "<p class='search_result_txt_"+t.getAttribute("key")+"'><span>"+key+"</span>" 
//	    						+ "<span>"+t.textContent+"...</span>" 
//	    						+	"<span class='player_current_go'></span></p>"
//	    						+ "</div>"; 
//	    		}
//	    	});
//	    	$player.find(".input_focus_search").remove();	
	    	//----------------------
//	    	inner.append(innerTxt);
	    });
		 
		 //------------------------------------ Window Scroll Event
		 $player.find(".play_control_wrap").scroll(function(event){
			 var duration = Number(rxAudio.duration * 50);
			 if (event.target.scrollTop > duration - 550){
			 }
		 })
	    
		 
		 /**
		  * 	@Auth David
		  * 	@Description 검색  후 구간이동 정의  오른 쪽 구간 클릭
		  * */
	    $(document).on("click",".player_current_go",function(){
	    	var resultBox = $(this).parents(".search_result_box");
	    	
	    	//	rx , tx 오디오 이동~
	    	rxAudio.currentTime = resultBox.attr("begin") ;
        	txAudio.currentTime = resultBox.attr("begin") ;
        	
        	// 바로 그 구간 표시해주기
        	setTimeout(function(){
        		moveView();	
        	},100);
    	})
    	
    	/**
    	 *		@Auth David 
    	 * 		@Description 태그 정보 클릭시 해당 태그 오른 쪽 클릭 실행~
    	 * */
    	 $(document).on("click",".player_tag_go",function(){
	    	var resultBox = $(this).parents(".tag_memo_box");
	    	rc.log("===============CLICK MEMO IDX ==============::"+resultBox.attr("memoIdx"));
	    	var memoIdx = resultBox.attr("memoIdx");

	    	$(".marking_wrap").find("ul").hide();
	    	$("#idx_"+memoIdx).find("ul").show();
    	})
    	    	
    	/**
    	 * 	@Description 아래 텍스트의 시간클릭시 구간 이동 정의~
    	 * 
    	 */
    	$(document).on("click",".play_wrap_all_text span font",function(){
	    	rc.log("ALL TEXT CURRENT TIME GO EVENT========================");
	    	var start = Number($(this).attr("begin").replace("s",""));
	    	
	    	//	rx , tx 오디오 이동~
	    	rxAudio.currentTime = start ;
        	txAudio.currentTime = start ;
        	
        	// 바로 그 구간 표시해주기
        	setTimeout(function(){
        		moveView();	
        	},100);
    	})
    	
    	
    	/**
    	 * 
    	 * 
    	 */
	    $(document).on("mousedown",".play_wrap_all_text p",function(){
	    	rc.log("DRAG EVENT========================");
	    	var start = Number($(this).attr("begin").replace("s",""));
	    	
	    	rc.startMarking = start;
    	})
    	
	    $(document).on("mouseup",".play_wrap_all_text p",function(){
	    	var end = Number($(this).attr("end").replace("s",""));
	    	
	    	var txt = document.getSelection().toString();
	    	if(txt == ""){
	    		rc.log("DRAG EVENT NULL==================="+txt);
	    		return false;
	    	}
	    	
	    	rc.endMarking = end;
	    	
	    	var min = rc.startMarking;
	    	var max = rc.endMarking;
	    	
	    	if( min > max ){
	    		var temp = max;
	    		max = min;
	    		min = temp;
	    	}
	    	
	    	rc.log("================== MIN  :: "+min);
	    	rc.log("================== MAX  :: "+max);
	    	
	    	rc.makeSection(min , max);
	    })
    	
    	/**
    	 * 	@Auth David
    	 *  @Description 드래그 드롭으로 구간 지정 이벤트 처리 문장단위 텍스트 에서...
    	 * */
	    //	마우스 다운 이벤트
	    $(document).on("mousedown",".stt_text_wrap",function(){
    	})
    	
	    $(document).on("mouseup",".stt_text_wrap",function(){
	    	rc.log("DRAG EVENT========================");
	    	var start = Number($(this).attr("begin").replace("s",""));
	    	var end = Number($(this).attr("end").replace("s",""));
	    	var key = ($(this).hasClass("stt_rx_text_wrap"))?"RX" : "TX";
	    	var txt = document.getSelection().toString();
	    	var txtArr = txt.split(" ");
	    	
	    	if(txt == ""){
	    		rc.log("DRAG EVENT NULL==================="+txt);
	    		return false;
	    	}
	    	
	    	var startArr = [] ,endArr = [];
	    	//	 Obj  넣어둔 XML 파싱 데이터 찾기...
	    	Object.keys(rc.XML).forEach(function(val , idx){
	    		var t = rc_player.XML[val];
				var tStart = Number(t.getAttribute("begin").replace("s",""));
	    		var tEnd = Number(t.getAttribute("end").replace("s",""));
	    		var tKey = t.getAttribute("key");
//메모 시작, 종료 시간때문에 수정	    		
	    		if ( start <= tStart && tEnd <= end  && tKey == key) {
	    			if( txt.indexOf(t.textContent) != -1){
	    				startArr.push(tStart);
	    				endArr.push(tEnd);
	    			}
	    		}
	    	});
	    	var min = Number(Math.min.apply(null , startArr));
	    	var max = Number(Math.max.apply(null , endArr));
	    	
	    	rc.log("MIN ===============" + min );
	    	rc.log("MAX===============" + max );
	    	
	    	rc.makeSection(min , max);
    	})
		 
/*		추후 확대 추가 추가시 추가 해서 사용 작업 해야됨... 
 		//확대 버튼
		 upSizeBtn.on('click',function(){
			 if(rc.currentSize >= 1.2 ){
	 			 return false;
	 		 }
			 var temp = $("#playerSizeBar").slider('value');
			 $("#playerSizeBar").slider('value',temp + 10);
			 
			 var v =  $("#playerSizeBar").slider('value');
			 $("#playerSizeBar").find(".progress").css("height",v);
			 
//			 //	확대 축소하면서 위치 조절
//			 var leftMargin  = Number(rxWave.css("margin-left").replace("px" , ""));
//			 var rightMargin  = Number(txWave.css("margin-left").replace("px" , ""));
//			 
//			 rxWave.css({"zoom": ((v / 100) + 0.2) , "margin-left" : leftMargin - ( leftMargin / 20 * 2.5 ) + "px"});
//			 txWave.css({"zoom": ((v / 100) + 0.2) , "margin-left" : rightMargin - ( rightMargin / 20 * 2 ) + "px"});
			 
			 rc.currentSize = (v / 100) + 0.2;
		 })
		 
		 //축소 버튼 
	 	 downSizeBtn.on('click',function(){
	 		 if(rc.currentSize <= 0.2 ){
	 			 return false;
	 		 }
	 		 var temp = $("#playerSizeBar").slider('value');
			 $("#playerSizeBar").slider('value',temp - 10);
			 
			 var v =  $("#playerSizeBar").slider('value');
			 $("#playerSizeBar").find(".progress").css("height",v);
			 
			 //	확대 축소하면서 위치 조절
			 
//			 var leftMargin  = Number(rxWave.css("margin-left").replace("px" , ""));
//			 var rightMargin  = Number(txWave.css("margin-left").replace("px" , ""));
//			 
//			 rxWave.css({"zoom": ((v / 100) + 0.2) , "margin-left" : leftMargin + ( leftMargin / 20 * 2.5 ) + "px"});
//			 txWave.css({"zoom": ((v / 100) + 0.2) , "margin-left" : rightMargin + ( rightMargin / 20 * 3 ) + "px"});
			 
			 rc.currentSize = (v / 100) + 0.2;
		 })*/
		 
		 /**
		  * 	@description Setinterval 로 currentTime 마다 구해서 스크롤 
		  * 
		  */
		 function moveView(){
			if(rc.viewFix)
				return false;
//			rc.log("=================moveView=================");
			var sttView = $('.play_control_wrap');
			var progH = rxAudio.currentTime * 50;
			
			sttView.animate({ scrollTop : progH - 300} , 300 , 'swing' ,function(){
				
			});
		 }
		 
		 function syncDo(something , value){
			 if(value === undefined){
				 rxAudio[something]();
				 txAudio[something]();
				 
			 }else{
				 if(something == "currentTime"){
					 rxAudio.currentTime = value;
					 txAudio.currentTime = value;
				 }
			 }
		 }
		 
		 /**
		  * 	@Auth David
		  * 	@Description 볼륨 버튼 클릭시 음소거 버튼 Function
		  * 	@Param
		  * */
		 $player.find(".volume_icon").on("click" , function(){
			 var parent = $(this).parent().get(0).className;
			 if(parent == "rx_audio_sound"){
				 // Rx 음소거
				 if(rxAudio.muted == false ){
					 rxAudio.muted = true;	 
					 $(this).addClass("volume_off");
					 
					 $("#rxPlaySoundBar").slider('value', 0 );
					 $("#rxPlaySoundBar").find(".progress").css("width",0 +"%");
					 
				 }else{
					 rxAudio.muted = false;
					 rxAudio.volume = 1;
					 
					 $(this).removeClass("volume_off");
					 
					 $("#rxPlaySoundBar").slider('value', 100 );
					 $("#rxPlaySoundBar").find(".progress").css("width",100 +"%");
				 }
				 
			 }else if(parent == "tx_audio_sound"){
				 //	Tx 음소거
				 if(txAudio.muted == false ){
					 txAudio.muted = true;	 
					 $(this).addClass("volume_off");
					 
					 $("#txPlaySoundBar").slider('value', 0 );
					 $("#txPlaySoundBar").find(".progress").css("width",0 +"%");
				 }else{
					 txAudio.muted = false;
					 txAudio.volume = 1;
					 
					 $(this).removeClass("volume_off");
					 
					 $("#txPlaySoundBar").slider('value', 100 );
					 $("#txPlaySoundBar").find(".progress").css("width",100 +"%");
				 }
			 }else{
				 // 모두 음소거
				 if($(this).hasClass("volume_off_all")){
					 //	음소거 상태 일때
					 $(this).removeClass("volume_off_all");
					 
					 if(rxAudio.muted == true ){
						 rxAudio.muted = false;
						 rxAudio.volume = 1;
						 $player.find(".rx_audio_sound .volume_icon").removeClass("volume_off");
					 }
					 
					 if(txAudio.muted == true ){
						 txAudio.muted = false;	
						 txAudio.volume = 1;
						 $player.find(".tx_audio_sound .volume_icon").removeClass("volume_off");
					 }
					 
					 //	프로그래스 바 처리
					$("#playSoundBar").slider('value', 100 );
					$("#playSoundBar").find(".progress").css("width",100 +"%");
					
					$("#rxPlaySoundBar").slider('value', 100 );
					$("#rxPlaySoundBar").find(".progress").css("width",100 +"%");
					
					$("#txPlaySoundBar").slider('value', 100 );
					$("#txPlaySoundBar").find(".progress").css("width",100 +"%");
				 }else{
					 // 음소거 아닐때
					 $(this).addClass("volume_off_all");
					 
					 if(rxAudio.muted == false ){
						 rxAudio.muted = true;	 
						 $player.find(".rx_audio_sound .volume_icon").addClass("volume_off");
					 }
					 
					 if(txAudio.muted == false ){
						 txAudio.muted = true;	 
						 $player.find(".tx_audio_sound .volume_icon").addClass("volume_off");
					 }
					 
					 //	프로그래스 바 처리
					$("#playSoundBar").slider('value', 0 );
					$("#playSoundBar").find(".progress").css("width",0 +"%");
					
					$("#rxPlaySoundBar").slider('value', 0 );
					$("#rxPlaySoundBar").find(".progress").css("width",0 +"%");
					
					$("#txPlaySoundBar").slider('value', 0 );
					$("#txPlaySoundBar").find(".progress").css("width",0 +"%");
				 }
			 }
		 })
		 
		 /**
		  * 	@Description  플레이어 포커스 시 키보드 및 마우스 우클릭 이벤트 적용
		  * */
		 $player.focusin(function(e){
			 document.oncontextmenu = function(){
				 rc.log("=========== OncontextMenu(우클릭) Event ===============");
				 	if($(e.target.parentNode).prev().hasClass("stt_play_current_wrap")){
				 		rc.log("============ PREV STT CURRENT WRAP RETURN FALSE ============");
				 		return false;
				 	}
					 var frontMarking = $(e.target.parentNode).prev();
	        		 var backMarking = $(e.target.parentNode);
		        	 rc._RC.MARKING.frontMarking = frontMarking;
		        	 rc._RC.MARKING.backMarking = backMarking;
				 return false;
			 }
		 })
		 

		 
		 // 화면 고정 버튼 정의
		 $("#playerFix").click(function(){
			 if ( $(this).hasClass("active_btn")){
				 rc.viewFix  = false;
				 $(this).removeClass("active_btn");
			 }else{
				 rc.viewFix  = true;		
				 $(this).addClass("active_btn");
			 }
		 })
		 
		 // 반복재생 버튼 정의
		 $("#reload").click(function(){
			 if ( $(this).hasClass("active_btn")){
				 rc.fullRepeat = false;
				 $(this).removeClass("active_btn");
			 }else{
				 rc.fullRepeat = true;		
				 $(this).addClass("active_btn");
			 }
		 })
		
		 //	현재 녹취파일 기준으로 현재 시간 % 구하기 
	    function percentPos(time) {
             var max = rxAudio.duration||0;
             return max == 0?0:time / max * 100;
         };

         function PerToTime(per){
         	var max = rxAudio.duration||0;
         	return max == 0?0:max * per / 100
         }
		 
		 // 날짜형식으로 변경
		 function getDateFormat(a){
			 return a.slice(0,4)+"-"+a.slice(4,6)+"-"+a.slice(6,8);
		 }
		 
		 //	시간형식으로 변경
		 function getTimeFormat(a){
			 return a.slice(0,2)+":"+a.slice(2,4)+":"+a.slice(4,6);
		 }
		 
		 //	전화번호 형식으로 변경
		 function getPhoneFormat(a){
			 return getMaskingFormat(a.replace(/(^02.{0}|^01.{1}|^[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3"));
		 }
		 
		 // 전화번호 마스킹 .. (추후 정규식 좀더 처리)
		 function getMaskingFormat(a){
			 return  a.replace(/-(.*?)-/,"-****-");
		 }
		 
		 //	get url 형식으로 변경
		 function getParameterFromUrl(name, url){
			var result = new RegExp('[\?&]' + name+ '=([^&#]*)').exec( url )
			if (result == null)
				return null;
			else
				return decodeURI(result[1]) || 0;
		 }
		 
		 //	Time -> Long
        function time2second(sec){
    		return Number(sec.split(":")[0]*60*60) + Number(sec.split(":")[1]*60) + Number(sec.split(":")[2]);
    	}
		 
		 //	Long Type -> Time
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
	 }

	 // HTML 코드 로드
	 this.HTML = (function() {
		 var html, wave, player;

		 $.ajax({
			 	url: baseSrc + "recsee_stt_player.ui.html",
			 	dataType: "html",
			 	async: false,
			 	cache: false,
				success: function(res){
					html = res;
					var body = $(html).closest("#recsee_player_object");
					 
					 player = (function(){
						 return body;
					 }())
				}
		 });
		 return {
			 wave : wave,
			 player: player
		 }
	 }());
 }
 RecseePlayer.prototype.setDefault();

function RecseePlayer(o){
	this.init(o);
	 
	return this;
}
