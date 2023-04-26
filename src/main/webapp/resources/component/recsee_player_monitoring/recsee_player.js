 "use strict";
// 초기 CSS 부르기
jQuery.support.cors = true;

var baseSrc = "";
(function(src) {
    // 초기 recsee_player.js의 경로 찾는 작업 수행
    src = ((src||[]).split("/")[1]?src:location.pathname);
    var path = function() {
        // recsee_player.js를 제외한 경로 얻기
        var pathArr = src.split("/");
        pathArr.pop();
        var path = pathArr.join("/");
        return path;
    }();
    
    $.getScript(path+"/component/layer_popup.js");
    
    $("head").append('<link rel="stylesheet" type="text/css" href="' + path + '/player_common.css">');
    
    baseSrc = path + "/";
}($("script[src*='"+"recsee_player.js"+"']").attr("src")));

/**
 * @define _RC
 * @type object
 * @description RecseePlayer 내부에서 사용하는 DEFINE값, 유용한 함수 등을 정의해둔 객체
 */
RecseePlayer.prototype._RC = (function() {
    var _RC = {  // 상수 정의
        /**
         * @define TYPE
         * @type object
         * @description 플레이어 타입 name들을 정의
         */
        TYPE: {
	        	HEADER: "header"			// 헤더
	        ,	AUDIO: "audio"             	// 오디오
	        ,	VIDEO: "video"             	// 비디오
	        ,	WAVE: "wave"               	// 텍스트 파형
	        ,	USER_DATA: "user_data"     	// 사용자 정보
	        ,	EXPAND_VIEW: "expand_view" 	// 플레이어 확대, 축소
	
	        ,	THUMBNAIL: "thumbnail"     	// 썸네일 바
	        ,	STT: "stt"                 	// 텍스트 스크립트
	        ,	REALTIME: "realtime"       	// 실시간 스트리밍 모드
	        ,	TOOL: "tool"               	// 툴 사용 여부
	        ,	LIST: "list"				// 리스트 사용 여부
	        ,	MEMO: "false"				// 메모 사용 여부
	        	
	        , 	BTNDOWNFILE:"btnDownFile"   // 전체파일 다운 사용 여부
	        , 	BTNUPFILE:"btnUpFile"		// 전체파일 다운 사용 여부
	        	
	        , 	BTNPLAYSECTION:"btnPlaySection"   // 재생 구간 설정 버튼 사용 여부
	        , 	BTNTIMESECTION:"btnTimeSection"   // 사용자 정의 구간 설정 버튼 사용 여부
	        , 	BTNMOUSESECTION:"btnMouseSection" // 마우스로 구간 설정 버튼 사용 여부	
	        	
	        ,	BTNDOWN: "btnDown"			// 구간 다운 버튼 사용 여부
        	,	BTNMUTE: "btnMute"			// 구간 음소거 버튼 사용 여부
    		,	BTNDEL: "btnDel"			// 구간 제거 버튼 사용 여부
    			
    		,   DUAL: "dual"				// 듀얼 플레이어(화자분리) 사용 여부
    			
    		,	REPLAY : "replay"				//리플레이 체크 사용 여부
    			
    		,	MENUCLICKRIGHT:"menuClickRight"	// 구간 메뉴 우클릭 사용 여부
    		,	MENUCLICKLEFT:"menuClickLeft"	// 구간 메뉴 좌클릭 사용 여부
    		,	LOG : "log"						// 로그 사용 여부 (조회&청취 페이지 전용)
        },
        /**
         * @define LANG
         * @type object
         * @description 사용하는 언어들을 정의(추후 언어팩 적용용)
         */
        LANG: {
            fileNotSet: "재생 중인 파일이 없습니다.",
            noUserSet: ""//"사용자 (1234,test)"
        },
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
	    ,  	BAR_COUNT: 200   			// 파형 bar 갯수
	    ,	MOVE_TIME: 5    			// 앞, 뒤로가기 버튼 시 이동 시간(s)
	    ,   REQUEST_IP: "10.17.32.143" 	// 음성파일 요청 IP
	    /*, REQUEST_IP: "172.18.64.23" 	// 음성파일 요청 IP*/	
	    ,   REQUEST_PORT: "28881"   	// 음성파일 요청 PORT
    };
    // 타입 설정:
    _RC.DEFAULT_OPTION.type[_RC.TYPE.HEADER] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.AUDIO] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.VIDEO] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.WAVE] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.USER_DATA] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.EXPAND_VIEW] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.THUMBNAIL] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.STT] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.REALTIME] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.TOOL] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.LIST] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.MEMO] = false;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.REPLAY] = true;
    
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNDOWNFILE] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNUPFILE] = true;
    
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNPLAYSECTION] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNTIMESECTION] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNMOUSESECTION] = true;
    
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNDOWN] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNMUTE] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.BTNDEL] = true;
    
    _RC.DEFAULT_OPTION.type[_RC.TYPE.DUAL] = false;
    
    _RC.DEFAULT_OPTION.type[_RC.TYPE.MENUCLICKRIGHT] = true;
    _RC.DEFAULT_OPTION.type[_RC.TYPE.MENUCLICKLEFT] = false;
    
    _RC.DEFAULT_OPTION.type[_RC.TYPE.LOG] = false;
    
    /**
    * @define toTimeString
    * @type function
    * @description 시간(s)을 HH:mm:ss 형태로 변환한다.
    * @param time(number)
    * @return (string)
    */
    _RC.toTimeString = function(time) {
        var sec_num = parseInt(time, 10); // 2번쨰 파라미터 빼먹지 말기..
        var hours   = Math.floor(sec_num / 3600);
        var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
        var seconds = sec_num - (hours * 3600) - (minutes * 60);

        if (hours   < 10) {hours   = "0"+hours;}
        if (minutes < 10) {minutes = "0"+minutes;}
        if (seconds < 10) {seconds = "0"+seconds;}
        return hours+':'+minutes+':'+seconds;
    }
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
    /**
    * @define RecseePlayer.set
    * @type function
    * @description RecseePlayer 객체 내에 값을 설정한다.
    * @param k(string): key, v(any): value, dv(any): default value
    * @return (any)
    */
    this.init = function(o) {
        // 옵션이 없다면 디폴트 옵션 적용
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
        this.set("target", $(o.target||defaultOption.target));
        this.set("autoplay", $(o.autoplay||defaultOption.autoplay));
        this.set("moveTime", o.moveTime||defaultOption.MOVE_TIME);
        this.set("barCount", o.barCount||defaultOption.BAR_COUNT);
        this.set("requestIp",o.requestIp||defaultOption.REQUEST_IP);
        this.set("requestPort",o.requestPort||defaultOption.REQUEST_PORT);

        // 제어값을 저장할 객체
        this.controlValue = {
            controlBar: {},
            volume: {},
            wave: {},
        }

        //o = o||JSON.parse(JSON.stringify(this));
        var $target = this.target;
        //var $player = $("article.recseePlayer");
        var $player = $(this.HTML.player);
        var TYPES = this._RC.TYPE;
        if(!this.type[TYPES.HEADER]) {
            // HEADER = false일 경우
            $player.find(".player_header").remove();
        }
        if(!this.type[TYPES.AUDIO]) {
            // AUDIO = false일 경우
            // 자동으로 WAVE false시키기
            this.type[TYPES.WAVE] = false;
        }
        if(!this.type[TYPES.VIDEO]) {
            // VIDEO = false일 경우
            $player.find(".player_video").remove();
        }else{
        	$player.find(".play_list_menu.ui_sub_btn_flat").remove();
        	$player.find(".play_list_menu").remove();
        }
        if(!this.type[TYPES.WAVE]) {
            // WAVE = false일 경우
            $player.find(".player_wave").remove();
            // 구간 설정 버튼 함께 제거 (wave가 없으면 소용 없음)
            $player.find(".btnMarking").remove();
        }
        if(!this.type[TYPES.USER_DATA]) {
            // USER_DATA = false일 경우
            //FIXME: 클래스 바뀌어서 일단 안됨
            $player.find(".play_rec_info").remove();
        }
        if(!this.type[TYPES.EXPAND_VIEW]) {
            // EXPAND_VIEW = false일 경우
            $player.find(".view_expand, .view_reduce").remove();
        }
        if(!this.type[TYPES.TOOL]) {
            // TOOL = false일 경우
            $player.find(".header_toolkit").remove();
        }
        
        // 파일 다운로드 버튼 비활성화 일 경우
        if(!this.type[TYPES.BTNDOWNFILE]) {
        	$player.find(".main_btn_filedownload").remove();
        }
        
        // 파일 업로드 버튼 비활성화 일 경우
        if(!this.type[TYPES.BTNUPFILE]) {
        	$player.find(".main_btn_fileupload").remove();
        }
        
        // 재생 구간 지정 버튼 비활 성화 일 경우
        if(!this.type[TYPES.BTNPLAYSECTION]) {
        	$player.find(".main_btn_section_end, .main_btn_section").remove();
        }else{
        	$player.find(".main_btn_section_end").hide();
        }
        
        // 시간 지정 구간 설정 버튼 비활성화 일 경우
        if(!this.type[TYPES.BTNTIMESECTION]) {
        	$player.find(".main_btn_time").remove();
        }
        
        // 마우스 구간 지정 버튼 비활성화 일 경우
        if(!this.type[TYPES.BTNMOUSESECTION]) {
        	$player.find(".main_btn_selector").remove();
        }
        
        if(!this.type[TYPES.LIST]) {
            //TOOL = false일 경우
            $player.find(".play_list_menu").remove();
        }
        
        // 마우스 클릭이 둘다 false 일 경우...
        // 좌클릭을 기본으로 처리 해준다.
        if(!(this.type[TYPES.MENUCLICKRIGHT] || this.type[TYPES.MENUCLICKLEFT])){
        	this.type[TYPES.MENUCLICKLEFT] = true;
        }
        
        if(this.type[TYPES.DUAL]) {
            // DAUL = true일 경우
        	$player.find(".player_rx").after($player.find(".player_rx").clone().removeClass("player_rx").addClass("player_tx"));
        	$player.find(".volume_rx").after($player.find(".volume_rx").clone().removeClass("volume_rx").addClass("volume_tx"));
        	$player.find(".volume_rx").addClass("add_volume_tx");
        	$player.find(".audio_rx").after($player.find(".audio_rx").clone().removeClass("audio_rx").addClass("audio_tx"));
        }
        
        //리플레이 체크 비활성화 일 경우
        if(!this.type[TYPES.REPLAY]){
        	$player.find(".replay_check").remove();
        }
        
        if(!this.type[TYPES.STT]) {
        // STT = false일 경우
        }
        // 최소화 모드 일 경우
        // 버튼 출력 변경
        if(this.type[TYPES.REALTIME]){
        	$player.find(".header_toolkit, .player_memo, .center_control, .play_list, .play_control_header").hide()

        	//wave 트루 일경우 기존 웨이브 숨기고, 실감용 웨이브 표시 (캔버스 사용'')
        	if(this.type[TYPES.WAVE]){
        		$player.find(".waveObj").remove();
        		$player.find(".waveCanvasObj").show();
        	}
        	// 추후 우측에 추가적으로 버튼 Add on 할경우 주석풀고 아래 주석
        	// $player.find(".play_control_wrap").css('width','calc(100% - 69px)');
        	$player.find(".play_control_wrap").css('width','100% - 12px');

        	$player.find(".player_wave").append(''+
				'<div class="player_info">'+
					'<marquee id="playInfo" style="text-align:left;"></marquee>'+
				'</div>'
        	)
        }

        // 비디오 전용 모드인지 표시
        this.set("videoOnly", !this.type[TYPES.AUDIO] && this.type[TYPES.VIDEO]);

        this.set("player", $player);

        $target./*find(".recseePlayer").remove().end().*/append($player);

        this.setObject();
        this.setEvent();
        
        var $target = $player.find('.replay_check_input');
    	var getHisListen = ((document.cookie.match(/hisListen=([\d]+)/)||[]).pop());
		// checked 속성이 있을 경우 속성 삭제
    	// off
		if(getHisListen==0) {
			$target.removeAttr('checked');
			$target.siblings('.replay_check_label').find('.replay_check_txt').removeClass('checked').text("OFF");
		}
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
    * @define RecseePlayer.get
    * @type function
    * @description RecseePlayer 객체 내에 설정된 값을 얻는다.
    * @param k(string): key
    * @return (any)
    */
    this.get = function(k) {
        return this[k];
    }
    /**
    * @define RecseePlayer.setFile
    * @type function
    * @description RecseePlayer 객체에 파일을 설정한다
    * @param t(string): type[audio, video], f(file)
    */
    this.setFile = undefined;
    /**
    * @define RecseePlayer.detachFile
    * @type function
    * @description RecseePlayer 객체가 제어중인 파일을 중단한다
    * @param t(string): type[audio, video]
    */
    this.detachFile = undefined;//function(t) {
    /**
    * @define RecseePlayer.canPlay
    * @type function
    * @description RecseePlayer 객체를 재생할 수 있는지 여부를 확인한다.
    * @return (boolean)
    */
    this.canPlay = undefined; //function() {
    /**
    * @define RecseePlayer.play
    * @type function
    * @description RecseePlayer 객체를 재생한다.
    */
    this.play = undefined; //function() {
    /**
    * @define RecseePlayer.pause
    * @type function
    * @description RecseePlayer 객체를 재생한다.
    */
    this.pause = undefined; //function() {
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
     * @define RecseePlayer.initWave
     * @type function
     * @description wave의 높이를 0으로 초기화 한다
     *  파일 변경 시 기존의 wave 데이터가 남아 있을 경우를 방지...
     */
    this.initWave = undefined;
    
    /**
    * @define RecseePlayer.setHeaderTitle
    * @type function
    * @description 헤더의 텍스트를 설정한다
    * @param str(string)
    */
    this.setHeaderTitle = undefined; //function(str) {
    /**
    * @define RecseePlayer.setNowTimeText
    * @type function
    * @description 초 단위의 시간을 넘겨받아 현재 시간 텍스트를 (HH:mm:ss 형태로) 설정한다.
    * @param sec(integer): 시간(초)
    */
    this.setNowTimeText = undefined; //function(sec) {
    /**
    * @define RecseePlayer.setTotalTimeText
    * @type function
    * @description 초 단위의 시간을 넘겨받아 총 시간 텍스트를 (HH:mm:ss 형태로) 설정한다.
    * @param sec(integer): 시간(초)
    */
    this.setTotalTimeText = undefined; //function(sec) {
    /**
    * @define RecseePlayer.setHeaderTitle
    * @type function
    * @description 파일명 텍스트를 설정한다
    * @param str(string)
    */
    this.setFileNameText = undefined; //function(str) {
    /**
    * @define RecseePlayer.setHeaderTitle
    * @type function
    * @description 정보 텍스트를 설정한다
    * @param str(string)
    */
    this.setFileInfoText = undefined; //function(str) {

    /**
     * @define RecseePlayer.setHeaderTitle
     * @type function
     * @description 정보 텍스트를 설정한다
     * @param ext(string), userId(string), userName(string), phoneNum(string)
     */
    this.setPlayerInfo = undefined;

    /**
     * @define RecseePlayer.setTime
     * @type function
     * @description 플레이어 시간을 변경 한다.
     * @param time(float)
     */
    this.setTime = undefined;
    
    /**
     * @define RecseePlayer.setTimeLoop
     * @type function
     * @description 플레이어를 sTime부터 eTime까지 반복 재생 한다.
     * @param sTime(float)
     * @param eTime(float)
     */
    this.setTimeLoop = undefined;
    
    /**
     * @define RecseePlayer.playBackRate
     * @type function
     * @description 플레이어의 audio | video 객체의 배속을 설정한다.
     * @param rate(float) 배속
     */
    this.playBackRate = undefined;
    
    /**
     * @define RecseePlayer.currentTime
     * @type function
     * @description 플레이어의 audio | video 객체의 배속을 설정한다.
     * @return 재생중인 파일의 현재시점을 시:분:초 반환
     */
    this.currentTime = undefined;
    
    /**
     * @define RecseePlayer.makeSection
     * @type function
     * @description 플레이어에 섹션을 만들어 준다.
     * @param startTime 섹션의 시작 점
     * @param endTime 섹션의 종료 점
     */
    this.makeSection = undefined;
    
    /**
     * @define RecseePlayer.uploadFile
     * @type function
     * @description 서버로부터 파일 업로드에 대한 응답을 받아 처리한다.
     * @param data 서버로 부터 받은 응답.
     */
    this.uploadFile = undefined; 
    
    
    /**
     * @define RecseePlayer.changeType
     * @type function
     * @description 플레이어 타입을 변경한다.
     *  단 변경시에는 해당 객체를 삭제하는게 아니라 숨기는거임
     * @param str(string)
     */
    this.changeType = function(o) {
        if(!o) {
            // 변경하지 않음
            return;
        }
        var rc = this;
        $.each(rc.type, function(t, b) {
            var newVal = o[t];
            // 값이 유효할 때만 변경
            if(newVal === true || newVal === false) {
                rc.type[t] = newVal;
            }
        });
        var $target = this.target;
        var $player = $target.find(".recseePlayer");
        var TYPES = this._RC.TYPE;

        $player.find(".player_header")[rc.type[TYPES.HEADER]?"show":"hide"]();
        $player.find(".player_audio")[rc.type[TYPES.AUDIO]?"show":"hide"]();
        $player.find(".player_video")[rc.type[TYPES.VIDEO]?"show":"hide"]();
        $player.find(".player_wave")[rc.type[TYPES.WAVE]?"show":"hide"]();
    }

    this.recFileData = new Object();
    this.listenUrl = undefined;
    this.settingTime = undefined;
    this.requestParam = undefined;
    this.sTime = undefined;
    this.eTime = undefined;
    this.upload = undefined;
    this.moveTime = 0.1;
    
    this.memoPopupHtmlString =
	'<div id="recMemo" class="popup_obj">'+
		'<div class="ui_popup_padding">'+
			'<div class="popup_header">'+
				'<div class="ui_pannel_popup_header">'+
					'<div class="ui_float_left">'+
						'<p class="ui_pannel_tit">메모</p>'+
					'</div>'+
					'<div class="ui_float_right">'+
						'<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="ui_article ui_row_input_wrap">'+
				'<div class="ui_pannel_row">'+
					'<div class="ui_padding">'+
						'<label>메모 내용</label>'+
						'<textArea class="" id="memoContents" style="width:315; height:175px; resize: none;"></textArea>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="ui_article">'+
				'<div class="ui_pannel_row">'+
					'<div class="ui_float_right">'+
						'<button id="memoAdd" class="ui_main_btn_flat icon_btn_save_white">저장</button>'+
					'</div>'+
				'</div>'+	
			'</div>'+
		'</div>'+
	'</div>';
    
    this.loopTimePopupHtmlString = 
    '<div class="popup_obj sectionSetting" style="width:150px">'+
	    '<div class="ui_popup_padding">'+
	        '<div class="popup_header">'+
	            '<div class="ui_pannel_popup_header">'+
	                '<div class="ui_float_left">'+
	                    '<p class="ui_pannel_tit">구간 설정</p>'+
	                '</div>'+
	                '<div class="ui_float_right">'+
	                    '<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>'+
	                '</div>'+
	            '</div>'+
	        '</div>'+
	        '<div class="ui_article ui_row_input_wrap">'+
	            '<div class="ui_pannel_row">'+
	                '<div class="ui_padding">'+
	                	'<label class="ui_label_essential">시작 시간</label>'+
	                    	'<input class="startTime" id="" value="" type="text" maxlength="5"/>'+
	                	'<label class="ui_label_essential">종료 시간</label>'+
	                    	'<input class="endTime" id="" value="" type="text" maxlength="5"/>'+
	                '</div>'+
	            '</div>'+
	        '</div>'+
	        '<div class="ui_article">'+
	            '<div class="ui_pannel_row">'+
	            	'<button class="ui_main_btn_flat icon_btn_cube_white saveSection">생성</button>'+
	            '</div>'+
	        '</div>'+
	    '</div>'+
	'</div>';	
   
    /**
    * @define setObject
    * @type function
    * @description RecseePlayer의 object들을 설정한다.
    */
    this.setObject = function() {
        // 플레이어
        var $player = this.player;
        this.obj = {};
        // 플레이어
        this.obj.player = $player;
        // 파형 표시
        this.obj.wave = $player.find(".player_wave.player_rx");
        // tx 파형 표시
        this.obj.tx_wave = $player.find(".player_wave.player_tx");
        
        // 컨트롤러
        this.obj.controller = $player.find(".player_controller");
        // 구간조절
        this.obj.controlBar = $player.find(".player_control");
        // 볼륨조절
        this.obj.volume = $player.find(".player_controller .audio_volume.volume_rx")
        // tx 볼륨 조절
        this.obj.tx_volume = $player.find(".player_controller .audio_volume.volume_tx")
        // 오디오
        this.obj.audio = $player.find(".audio_rx audio");
        // tx 오디오
        this.obj.tx_audio = $player.find(".audio_tx audio");
        
        // 비디오
        this.obj.video = $player.find("video");
        // 업로드 영역
        this.obj.upload = $player.find(".uploadMask");
    }
    /**
    * @define setEvent
    * @type function
    * @description RecseePlayer 객체에 이벤트들을 설정한다.
    * @param e(function): custom event
    */
    this.setEvent = function(e) {
        var rc = this;
        // 플레이어
        var $player = this.obj.player;
        // 파형 표시
        var $wave = this.obj.wave;
        // tx 파형 표시
        var $tx_wave = this.obj.tx_wave;
        // 컨트롤러
        var $controller = this.obj.controller;
        // 구간조절
        var $controlBar = this.obj.controlBar;
        // 볼륨조절
        var $volume = this.obj.volume;
        // tx 볼륨조절
        var $tx_volume = this.obj.tx_volume;
        
        // 업로드
        var $upload = this.obj.upload;
        
        // 오디오
        var $audio = this.obj.audio;
        var audio = $audio.get(0);
        
        // tx 오디오
        var $tx_audio = this.obj.tx_audio;
        var tx_audio = $tx_audio.get(0);
        
        // 비디오
        var $video = this.obj.video;
        var video = $video.get(0);

        var TYPES = rc._RC.TYPE;
        var DEF = rc._RC.DEFAULT_OPTION;
        var BAR_COUNT = rc.barCount||200;
        var MOVE_TIME = DEF.MOVE_TIME||15;
        // --------------------------------------------------
        // init
        $audio.init = function() {
            // bar 초기화
            $controlBar.changeValue(0);
            // 00:00:00
            $controller.setNowTimeText(0);
            $controller.setTotalTimeText(0);
            
            // 볼륨 불러오기
            var volume = ((document.cookie.match(/volume=([\d]+)/)||[]).pop()||100);
            $volume.changeValue(volume).moveTo(volume);
            
            if(rc.type.dual)
            	$tx_volume.changeValue(volume).moveTo(volume);

            // 실감일 경우 쿠키에서 가져온 볼륨으로 셋팅 해주깅
            if(rc.type.realtime){
        		try{
        			OnControlsVolumeChange(volume);
        		}catch(e){tryCatch(e)}
        	}

            // 파일명 설정하기
            $controller.setHeaderTitle(rc._RC.LANG.fileNotSet);
            $controller.setFileNameText(rc._RC.LANG.fileNotSet);
            $controller.setFileInfoText(rc._RC.LANG.noUserSet);


            /*if(rc.type[TYPES.AUDIO] && !rc.type[TYPES.WAVE]) {
                $player.find(".play_time").addClass("no_wave");
            } else {
                $player.find(".play_time").removeClass(".no_wave");
            }*/

            // 비디오 초기화
            if(rc.type[TYPES.VIDEO]) {
            //
            }

            // 파형 초기화
            if(rc.type[TYPES.WAVE]) {

                var half = 100/2/BAR_COUNT; //0.25
                for(var i=0; i<BAR_COUNT; i++) {
                    var $waves = $(rc.HTML.wave);
                    var pos = parseFloat((i / BAR_COUNT * 100), 10);
                    //console.timelog(pos);
                    $waves.css("width","calc("+half*2+"% - "+200/BAR_COUNT+"px)") 
                    $waves.find(".obj_key_left").data("pos", pos);
                    $waves.find(".obj_key_right").data("pos", pos + half);
                    //.find("div").css("height", "0");
                    $wave.find(".waveObj").append($waves);
                }
                $wave.find(".waveObj .obj_key > div").click(function(event) {
                    // 해당 위치로 이동
                    var val = $(this).data("pos");
                    $controlBar.moveTo(val);

                    //console.log(val)
                });

                // 컨트롤러 hover 이벤트
                $wave.find(".waveObj .obj_key").hover(function(event) {
                    // 플레이 중인 시간의 이전 wave의 class 변경
                    var $bar = $wave.find(".waveObj");
                    // 플레이어의 왼쪽 마진 px
                    var mLeft = $bar.offset().left;
                    // 마우스의 현 x위치
                    var cPosX = event.clientX;
                    // 마우스의 컨트롤러 내 상대적인 x위치
                    var rPosX = cPosX - mLeft;
                    // 상대적인 x의 %위치
                    var rPerX = rPosX/$bar.width() * 100;
                    var $removeTarget = $($.grep($wave.find(".obj_key > div"), function(d){return $(d).data("pos")>=rPerX})).find(".obj_key_value");
                    var $addTarget = $($.grep($wave.find(".obj_key > div"), function(d){return $(d).data("pos")<rPerX})).find(".obj_key_value");
                    $removeTarget.removeClass("obj_key_value_buffer");
                    $addTarget.addClass("obj_key_value_buffer");
                }).mouseleave(function(event) {
                    // 플레이 중인 시간의 이전 wave의 class 변경
                    var $target = $(".obj_key").find(".obj_key_value_buffer");
                    $target.removeClass("obj_key_value_buffer");
                });
                
                /**
                 * tx wave 파형 초기화
                 * 
                 * 
                 * */
                if (rc.type[TYPES.DUAL]){
                	for(var i=0; i<BAR_COUNT; i++) {
                		var $waves = $(rc.HTML.wave);
                        var pos = parseFloat((i / BAR_COUNT * 100), 10);
                        //console.timelog(pos);
                        $waves.css("width","calc("+half*2+"% - "+200/BAR_COUNT+"px)") 
                        $waves.find(".obj_key_left").data("pos", pos);
                        $waves.find(".obj_key_right").data("pos", pos + half);
                        //.find("div").css("height", "0");
                        $tx_wave.find(".waveObj").append($waves);
                    }
                    $tx_wave.find(".waveObj .obj_key > div").click(function(event) {
                        // 해당 위치로 이동
                        var val = $(this).data("pos");
                        $controlBar.moveTo(val);

                        //console.log(val)
                    });

                    // 컨트롤러 hover 이벤트
                    $tx_wave.find(".waveObj .obj_key").hover(function(event) {
                        // 플레이 중인 시간의 이전 wave의 class 변경
                        var $bar = $tx_wave.find(".waveObj");
                        // 플레이어의 왼쪽 마진 px
                        var mLeft = $bar.offset().left;
                        // 마우스의 현 x위치
                        var cPosX = event.clientX;
                        // 마우스의 컨트롤러 내 상대적인 x위치
                        var rPosX = cPosX - mLeft;
                        // 상대적인 x의 %위치
                        var rPerX = rPosX/$bar.width() * 100;
                        var $removeTarget = $($.grep($tx_wave.find(".obj_key > div"), function(d){return $(d).data("pos")>=rPerX})).find(".obj_key_value");
                        var $addTarget = $($.grep($tx_wave.find(".obj_key > div"), function(d){return $(d).data("pos")<rPerX})).find(".obj_key_value");
                        $removeTarget.removeClass("obj_key_value_buffer");
                        $addTarget.addClass("obj_key_value_buffer");
                    }).mouseleave(function(event) {
                        // 플레이 중인 시간의 이전 wave의 class 변경
                        var $target = $(".obj_key").find(".obj_key_value_buffer");
                        $target.removeClass("obj_key_value_buffer");
                    });
                }
            }
        }
        // --------------------------------------------------
        // 함수 선언
        // @function: 재생 가능 여부
        var canPlay = function(t) {
            if(t == "audio") {
                return !!$audio.attr("src");
            } else if(t == "video") {
                return !!$video.attr("src");
            }
        }
        $audio.canPlay = function() {
            return canPlay("audio");
        };
        $video.canPlay = function() {
            return canPlay("video");
        };
        $controller.canPlay = function() {
            if(rc.videoOnly) {
                // 비디오
                return $video.canPlay();
            } else if(rc.type[TYPES.AUDIO]) {
                // 오디오
                return $audio.canPlay();
            } else {
                return false;
            }
        }
        // @function: 헤더 타이틀 설정
        $controller.setHeaderTitle = function(str) {
            // HEADER 타입을 사용할때만 사용
            //if(!rc.type[TYPES.HEADER]) return;
            return $player.find("#player_title").text(str);
        }
        // @function: 현재 시간 텍스트 설정
        $controller.setNowTimeText = function(sec) {
            //$player.find(".now_time").text(rc._RC.toTimeString(sec));
            $player.find("#playTime").text(rc._RC.toTimeString(sec));
        }
        // @function: 총 시간 텍스트 설정
        $controller.setTotalTimeText = function(sec) {
            //$player.find(".total_time").text(rc._RC.toTimeString(sec));
            $player.find("#totalTime").text(rc._RC.toTimeString(sec));
        }
        // @function: 파일명 텍스트 설정
        $controller.setFileNameText = function(str) {
            //$player.find(".file_name").text(str);
            $player.find(".play_file_tit").text(str);
        }
        // @function: 정보 텍스트 설정
        $controller.setPlayerInfo = function(ext,userName,PhoneNumber) {
        	var maskingNumber;
        	var str = "";
        	if(PhoneNumber != null && PhoneNumber != undefined && PhoneNumber != "")
        		maskingNumber=phoneFomatter(PhoneNumber,"");
        		//maskingNumber=phoneFomatter(PhoneNumber,"masking");
        	
        	if(ext != null && ext != undefined && ext != "")
        		str += "내선번호 : "+ext
    		if(userName != null && userName != undefined && userName != "")
        		str += "     상담원이름 : "+userName
    		if(maskingNumber != null && maskingNumber != undefined && maskingNumber != "")
        		str += "     고객 번호 : "+maskingNumber
        	if(str.length > 0)
        		str = "청취중 정보 - " + str

            $player.find("#playInfo").text(str);
        }

        // @function: 정보 텍스트 설정
        $controller.setFileInfoText = function(str) {
            //$player.find(".file_name").text(str);
            $player.find(".play_file_info").text(str);
        }

        // @function: 녹취 텍스트 설정
        $controller.setRecInfoText = function(user, ext, id, recDate, recLen, custNo) {
            var o = {};
            if(arguments[0] != null && typeof arguments[0] == "object") {
                // object 설정
                o = arguments[0];
            } else {
                // 일반 설정
                var typeArray = ["user", "ext", "id", "recDate", "recLen", "custNo"];
                // 유효값만 설정하기
                var args = arguments;
                typeArray.forEach(function(v, i) {
                    o[v] = "" + args[i];
                });
            }
            // 예외처리
            if(!isNaN(o.recLen) && o.recLen.trim() != "") {
                // 녹취길이 HH:mm:ss 형태로 변환
                o.recLen = rc._RC.toTimeString(o.recLen);
            }
            $.each(o, function(n, v) {
                // camel to underbar
                var className = ".info_" + n.replace(/([A-Z])/g, function(s){return "_" + s[0].toLowerCase()});
                $player.find(className).text(v);
            });
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
        $wave.setWaveHeight = function(i, h) {
            // WAVE 타입을 사용할때만 사용
            if(!rc.type[TYPES.WAVE]) return;
            // 비율을 %로 변환
            if(typeof h == "undefined") {
                // 일괄설정
                i.forEach(function(v, index) {
                    $wave.find(".obj_key:eq(%d)".replace(/%d/, index)).find("div.obj_key_value").css("height", v + "%");
                });
            } else{
                // 단일 위치에 값 설정
                if(h<1) {h = parseFloat(h ,10)*100;}
                $wave.find(".obj_key:eq(%d)".replace(/%d/, i)).find("div.obj_key_value").css("height", h + "%");
            }
        }
        $tx_wave.setWaveHeight = function(i, h) {
            // WAVE 타입을 사용할때만 사용
            if(!rc.type[TYPES.WAVE]) return;
            // 비율을 %로 변환
            if(typeof h == "undefined") {
                // 일괄설정
                i.forEach(function(v, index) {
                    $tx_wave.find(".obj_key:eq(%d)".replace(/%d/, index)).find("div.obj_key_value").css("height", v + "%");
                });
            } else{
                // 단일 위치에 값 설정
                if(h<1) {h = parseFloat(h ,10)*100;}
                $tx_wave.find(".obj_key:eq(%d)".replace(/%d/, i)).find("div.obj_key_value").css("height", h + "%");
            }
        }

        /**
         * @define setWaveCanvas
         * @type function
         * @description 실감 Canvas에 wave를 그린다.
         * @param cmd:명령 [draw;그려라- 오디오 버퍼 있을 때 호출, clear;지워라 소켓 종료될 때 호출 ] AudioBuffer: 오디오 버퍼
         */
        $wave.setWaveCanvas = function(cmd,AudioBuffer,mode) {

        	// 모드에 맞는 함수명 찾아서 실행
        	switch (mode){
        	case "drawSpectrum":
        		drawSpectrum(cmd,AudioBuffer)
        	break;

        	case "volumeAvg":
        		volumeAvg(cmd,AudioBuffer)
        	break;

        	case "waveForm":
        		waveForm(cmd,AudioBuffer)
            break;

        	}

        	function drawSpectrum(cmd,AudioBuffer){
        		// 스펙트럼 그리기 모드
        		if(cmd == "draw"){
            		var array = AudioBuffer//AudioBuffer.getChannelData( 0 )

            		var canvas = $("#canvas").get(0)
            		var ctx = canvas.getContext("2d");
            		// clear the current state
            		ctx.clearRect(0, 0, 1000, 325);

                    var gradient = ctx.createLinearGradient(0,0,0,300);
                    gradient.addColorStop(1,'#000000');
                    gradient.addColorStop(0.75,'#ff0000');
                    gradient.addColorStop(0.25,'#ffff00');
                    gradient.addColorStop(0,'#ffffff');

                    // set the fill style
            		ctx.fillStyle=gradient;

            		drawSpectrum(array);

            		function drawSpectrum(array) {
            		    for ( var i = 0; i < (array.length); i++ ){
            		            var value = array[i];
            		            ctx.fillRect(i*5,325-value,3,325);
            		        }
            		}

            	}else if(cmd =="clear"){


            	}
        	}


        	function volumeAvg(cmd,AudioBuffer){
        		// 볼륨 평균 모드
            	if(cmd == "draw"){
            		var array = AudioBuffer//AudioBuffer.getChannelData( 0 )
            		var average = getAverageVolume(array);

            		//console.log(average)

            		var canvas = $("#canvas").get(0)
            		var ctx = canvas.getContext("2d");
            		// clear the current state
                    ctx.clearRect(0, 0, 60, 130);

                    var gradient = ctx.createLinearGradient(0,0,0,300);
                    gradient.addColorStop(1,'#000000');
                    gradient.addColorStop(0.75,'#ff0000');
                    gradient.addColorStop(0.25,'#ffff00');
                    gradient.addColorStop(0,'#ffffff');

                    // set the fill style
            		ctx.fillStyle=gradient;

                    // create the meters
            		ctx.fillRect(0,130-average,25,130);

            	}else if(cmd =="clear"){


            	}

            	function getAverageVolume(array) {
                    var values = 0;
                    var average;

                    var length = array.length;

                    // get all the frequency amplitudes
                    for (var i = 0; i < length; i++) {
                        values += array[i];
                    }

                    average = values / length;
                    return average;
                }
        	}

        	function waveForm(cmd,AudioBuffer){
        		// waveform.js 모드
            	if(cmd =="draw"){
                	// WAVE 타입을 사용할때만 사용
                    if(!rc.type[TYPES.WAVE]) return;

                    if(rc.waveform == null){
        	            rc.waveform = new Waveform({
        	            	container: $wave.find(".waveCanvasObj").get(0),
        	            	data: AudioBuffer.getChannelData( 0 ),
        	            	innerColor: "#3a9cd6"
        	            });
                    }else{
                    	rc.waveform.update({
                        	data: AudioBuffer.getChannelData( 0 )
                        })
                    }
                }else if(cmd =="clear"){
                	if(rc.waveform != null)
                		rc.waveform.clear()
                }
        	}
        }

        /**
         * @define $wave.clear
         * @type function
         * @description wave를 초기화한다.
         */
        $wave.clear = function() {
            var $removeTarget = $(".obj_key > div").find(".obj_key_value_now, .obj_key_value_target");
            $removeTarget.removeClass("obj_key_value_now obj_key_value_target");
        }
        $tx_wave.clear = function() {
            var $removeTarget = $(".obj_key > div").find(".obj_key_value_now, .obj_key_value_target");
            $removeTarget.removeClass("obj_key_value_now obj_key_value_target");
        }

        // --------------------------------------------------
        // audio 이벤트
        // AUDIO 타입을 사용할때만 사용
        if(rc.type[TYPES.AUDIO]) {
            // 컨트롤바와 media객체의 이벤트 연결
            setMediaEvent(audio);
            
            // 필요한 이벤트만 받아서 처리 한다..
            if(rc.type[TYPES.DUAL]){
            	tx_audio.addEventListener('timeupdate',function(event) {
            		
            		var targetCurrentTime = tx_audio.currentTime; 
                	var targetDuration = tx_audio.duration;
                	
                    // 현재 재생중인 음성의 현재/전체를 읽어 POSITION BAR에 반영하는 작업 수행
                    // 일반적인 재생중 이벤트가 아니라면 갱신하지 않음: 드래그 중에 현재 재생 위치로 자동 갱신하는 경우 방지
                    if(rc.obj.controlBar.dragging) return;
                    var percentPos = function() {
                        var max = targetDuration||0;
                        var current = targetCurrentTime||0;
                        return max == 0?0:current / max;
                    }();
                    var pos = percentPos * 100;
            		
            		var $playingTarget = $($.grep($tx_wave.find(".obj_key > div"), function(d){return $(d).data("pos")<=pos})).find(".obj_key_value");
            		$playingTarget.addClass("obj_key_value_now");
            	});
            }
            	
            
            // 파일 설정(로드) 이벤트
            $audio.setFile = function(file, name, uploadFile) {
            	
            	if(uploadFile){
            		rc.upload = true;
            		$player.find(".btn_tool").hide();
            	}else{
            		rc.upload = false;
            		$player.find(".btn_tool").not(".main_btn_section_end").show();
            	}
            	
            	$audio.data("loaded",false);
            	
            	//src가 바뀌고, 현재 재생중이던 파일이 있으면, 커렌트 타임 저장
            	if($player.find(".nowPlay").length > 0){
            		if(file.toUpperCase() != $player.find(".nowPlay").attr("listenUrl").toUpperCase()){
    	            	if($player.find(".nowPlay").length > 0){
    	            		var nowCurrentTime = $audio[0].currentTime
    	            		$player.find(".nowPlay").attr("currentTime",nowCurrentTime)
    	            	}
                	}
            	}
            	if(!rc.type[TYPES.DUAL])
            		$audio.attr("src", file);
            	else
            		$audio.attr("src", file+".RX.mp3");
                $audio.data("name", name||rc._RC.getFileNameFromURL(file));
                
                
                if(rc.type[TYPES.DUAL]){
                	audio.pause();
                	audio.load();
                	if(rc.autoplay){
                		audio.play();
                	}
                }else{
                	syncDo("pause")
                    syncDo("load")       	
                	// 자동 실행옵션 선택시 실행
    	            if(rc.autoplay){
    	            	syncDo("play")
    	            } 
                }
                	
                	
                $controller.setPlayerInfo($audio.data("ext"),$audio.data("userId"),$audio.data("userName"),$audio.data("phoneNum"))
                $controlBar.changeValue(0);

                //FIXME: 상담원 정보 설정.. 임시
                $controller.setHeaderTitle($audio.data("name")||rc._RC.getFileNameFromURL($audio.attr("src")));

                if(rc.type[TYPES.VIDEO]) {
                    // 비디오 파일 꺼내기
                    detachFile($video);
                }

                // 화면 초기화
                $controller.changeButtonState("pause");
                $wave.clear();
                
                if(rc.type[TYPES.WAVE]){
            		rc.initWave();
                	if(!rc.type[TYPES.DUAL])
                		rc.updateWave(getParameterFromUrl("url", rc.requestParam))
                	else
                		rc.updateWave(getParameterFromUrl("url", rc.requestParam+".RX.mp3"))
            	}
            }
            $audio.detachFile = function() {
                if(rc.type[TYPES.AUDIO]) {
                    detachFile($audio);
                    $controlBar.changeValue(0);

                    $controller.changeButtonState("pause");
                    $controller.setHeaderTitle(rc._RC.LANG.fileNotSet);
                    $controller.setFileNameText(rc._RC.LANG.fileNotSet);
                    $controller.setFileInfoText(rc._RC.LANG.noUserSet);
                }
            }

            $audio.setTime = function(time) {
                return syncDo("currentTime", time)
            };
            
            /**
             * 지금은..........
             * 하드 코딩을 선택 했다.......
             * 미안하다...............................................
             * 고멘........
             * */
            // 파일 설정(로드) 이벤트
            $tx_audio.setFile = function(file, name, uploadFile) {
            	
            	if(uploadFile){
            		rc.upload = true;
            		$player.find(".btn_tool").hide();
            	}else{
            		rc.upload = false;
            		$player.find(".btn_tool").not(".main_btn_section_end").show();
            	}
            	
            	$tx_audio.data("loaded",false);
            	
            	//src가 바뀌고, 현재 재생중이던 파일이 있으면, 커렌트 타임 저장
            	if($player.find(".nowPlay").length > 0){
            		if(file.toUpperCase() != $player.find(".nowPlay").attr("listenUrl").toUpperCase()){
    	            	if($player.find(".nowPlay").length > 0){
    	            		var nowCurrentTime = $tx_audio[0].currentTime
    	            		$player.find(".nowPlay").attr("currentTime",nowCurrentTime)
    	            	}
                	}
            	}
            	
                $tx_audio.attr("src", file+".TX.mp3");
                $tx_audio.data("name", name||rc._RC.getFileNameFromURL(file));
                
                tx_audio.pause();
                tx_audio.load();       	
            	// 자동 실행옵션 선택시 실행
	            if(rc.autoplay){
	            	tx_audio.play();
	            } 	 	
            		
                $controller.setPlayerInfo($tx_audio.data("ext"),$tx_audio.data("userId"),$tx_audio.data("userName"),$tx_audio.data("phoneNum"))
                $controlBar.changeValue(0);

                //FIXME: 상담원 정보 설정.. 임시
                $controller.setHeaderTitle($tx_audio.data("name")||rc._RC.getFileNameFromURL($tx_audio.attr("src")));

                if(rc.type[TYPES.VIDEO]) {
                    // 비디오 파일 꺼내기
                    detachFile($video);
                }

                // 화면 초기화
                $controller.changeButtonState("pause");
                $tx_wave.clear();
                
                if(rc.type[TYPES.WAVE]){
            		rc.initWave();
            		rc.updateWave(getParameterFromUrl("url", rc.requestParam+".TX.mp3"),true)
            	}
            }
            $tx_audio.detachFile = function() {
                if(rc.type[TYPES.tx_audio]) {
                    detachFile($tx_audio);
                    $controlBar.changeValue(0);

                    $controller.changeButtonState("pause");
                    $controller.setHeaderTitle(rc._RC.LANG.fileNotSet);
                    $controller.setFileNameText(rc._RC.LANG.fileNotSet);
                    $controller.setFileInfoText(rc._RC.LANG.noUserSet);
                }
            }

            $tx_audio.setTime = function(time) {
                return syncDo("currentTime", time)
            };
            /**
             * 지금은..........
             * 하드 코딩을 선택 했다.......
             * 미안하다...............................................
             * 고멘........
             * */
            
        }
        // --------------------------------------------------
        // 볼륨 hover 이벤트
        $player.find(".audio_volume_control").hover(function(){
            var $t = $player.find(".player_controller .audio_volume");
            clearTimeout($t.data("timeout"));
            $t.show();
        }, function(){
            var $t = $player.find(".player_controller .audio_volume");
            $t.data("timeout", setTimeout(function(){
                $t.hide({duration: 100});
            }, 50));
        });
        // view 변경 이벤트
        $controller.find(".btn_view").click(function() {
            rc.toggleView();
        })
        // --------------------------------------------------
        // RecseePlayer 객체 제어 함수
        /**
        * @define RecseePlayer.setFile
        * @type function
        * @description RecseePlayer 객체에 파일을 설정한다
        * @param t(string): type[audio, video], f(file), u(upload)
        */
        rc.setFile = function(t, f, u) {
        	
        	var f = f.replace(/\\/gi,'/'); 
        	
        	var file, name, fileMode;
        	if(typeof f == "object") {
                // 로컬 파일
        		file = URL.createObjectURL(f);
        		name = rc._RC.getFileNameFromURL(f.name);
        	} else {
        		
        		var OPTION = rc._RC.DEFAULT_OPTION
        		var prepixUrl = "http://"+OPTION.REQUEST_IP+":"+OPTION.REQUEST_PORT;
        		if (t == "audio")
        			f = prepixUrl+"/listen"+f
        		else if(t == "video"){
        			f = prepixUrl+"/view"+f
        		}

                // From URL
        		file = f;
        		
        		// 객체에 요청파라미터 저장
        		if (t == "audio")
        			rc.requestParam = f;
        		
        		name = f.split("/").pop();
        	}
        	if(t == "audio" || t == "video") {

        		if(t == "audio" && rc.type[TYPES.AUDIO]) {
        			$audio.setFile(file, name, u);
        			if(rc.type[TYPES.DUAL])
        				$tx_audio.setFile(file, name, u);
        			
        		} else if(t == "video" && rc.type[TYPES.VIDEO]) {
        			$video.setFile(file, name);
        		}
        	}
            
        	rc.sTime = 0;
        	rc.eTime = 0;
        	$player.find('.player_marking_wrap').remove()
        	
        }
        /**
        * @define RecseePlayer.detachFile
        * @type function
        * @description RecseePlayer 객체가 제어중인 파일을 중단한다
        * @param t(string): type[audio, video]
        */
        rc.detachFile = function(t) {
            if(t == "audio" && rc.type[TYPES.AUDIO]) {
                $audio.detachFile();
                if(rc.type[TYPES.DUAL]){
                	$tx_audio.detachFile();
                }
            } else if(t == "video" && rc.type[TYPES.VIDEO]) {
                $video.detachFile();
            }
        }
        /**
        * @define RecseePlayer.canPlay
        * @type function
        * @description RecseePlayer 객체를 재생할 수 있는지 여부를 확인한다.
        * @return (boolean)
        */
        rc.canPlay = function() {
            return $controller.canPlay();
        }
        /**
        * @define RecseePlayer.play
        * @type function
        * @description RecseePlayer 객체를 재생한다.
        */
        rc.play = function() {
            syncDo("play");
        }
        /**
        * @define RecseePlayer.pause
        * @type function
        * @description RecseePlayer 객체를 재생한다.
        */
        rc.pause = function() {
            syncDo("pause");
        }
        /**
        * @define RecseePlayer.setWaveHeight
        * @type function
        * @description wave의 높이값을 설정한다.
        *  index와 height을 파라미터로 넘길 경우 특정 index의 값을 바꿀 수 있으며,
        *  숫자 배열을 값으로 넘길 경우 높이를 일괄 설정할 수 있다.
        * @param i(number): index, h(number): height
        *  OR    i(array): height의 array
        */
        rc.setWaveHeight = function(i, h) {
            // WAVE 타입을 사용할때만 사용
            if(!rc.type[TYPES.WAVE]) return;
            $wave.setWaveHeight(i, h);
        }
        /**
         * @define RecseePlayer.initWave
         * @type function
         * @description wave의 높이를 100으로 초기화 한다
         *  파일 변경 시 기존의 wave 데이터가 남아 있을 경우를 방지...
         */
        rc.initWave = function(){
        	$wave.find(".obj_key").css("height","100%")
        }
        
        /**
         * @define RecseePlayer.updateWave
         * @type function
         * @description wave의 높이값을 설정하고 wave를 갱신한다.
         *  파라미터 값이 없다면 rc.currentWave에 저장된 값으로 wave를 갱신한다
         * @param arr(array) wave 높이 배열
         */
        rc.updateWave = function(path, isTx) {
            if(rc.type[TYPES.WAVE]) {
            	
            	var dataStr = {
                		"url" : path
                	,	"listenType" : ((rc.upload) ? "upload" : "none")
                }
                
            	function requestWave(handler){
            		$.ajax({
            			crossDomain:true,
            			//url:contextPath+"/getWaveArray.do",
                		url :"http://"+rc._RC.DEFAULT_OPTION.REQUEST_IP+":"+rc._RC.DEFAULT_OPTION.REQUEST_PORT+"/wave",
            			/*url :"http://"+rc._RC.DEFAULT_OPTION.REQUEST_IP+":"+"28883"+"/wave",*/
            			data:dataStr,
            			type:"POST",
            			dataType:"json",
            			cache: false,
            			/*async: false,*/
            			success:function(jRes){
            				handler(jRes.data)
            			}
            		});
            	}
                	
            	requestWave(function (data){
            		var arr = rc.formatWaveArray(data);
            		var heightArray = arr||rc.currentWave;
            		if(!isTx)
            			$wave.setWaveHeight(heightArray);
            		else
            			$tx_wave.setWaveHeight(heightArray);
            	});
            }
        }

        /**
         * @김화랑
         * @define RecseePlayer.formatWaveArray
         * @type function
         * @description 실 wave를 전달 받아 배열 200개로 계산하여 반환.
         * @param arr(array) 실 wave 배열
         */
        rc.formatWaveArray = function(arr)  {
            var LENGTH = rc.barCount - 1;
            var rtArr = new Array(LENGTH);

            var len = arr.length;
            // 마지막 배열 값을 출력하기 위해 +1을 해줌
            for(var i=0, revIndex=0; i<LENGTH+1; i++) {
               // 현재 배열의 index
               var currentArrIndex = parseInt(i/LENGTH*(len-1), 10);
               // 배열의 index가 바뀌었는지 여부
               var isArrIndexChanged = i === 0 || parseInt(i/LENGTH*(len-1), 10) !== parseInt((i-1)/LENGTH*(len-1), 10);

               // 값 변경치 index(다음값과의 차이를 줄이기 위해 더해주는 값)를 새로운 값으로 바뀌었다면 초기화 바뀌지
				// 않았다면 ++
               revIndex = isArrIndexChanged?0:revIndex+1;

               // 현재 Index에 대한 배열의 값
               var currentArrValue = parseFloat(arr[currentArrIndex], 10);
               // 현재 Index에 대한 배열의 다음값 없으면 현재 배열값
               var nextArrValue = parseFloat(arr[currentArrIndex + 1], 10)|| parseFloat(arr[currentArrIndex], 10);
               // 높이 = 현재 배열의 값 + (다음값과의 차이를 고려한 숫자를 더해준다)
               var height = currentArrValue + (nextArrValue - currentArrValue) * (revIndex * (len-1) / LENGTH);
               // var lastHeight = currentArrValue +(nextArrValue -
				// currentArrValue) * (revIndex*len / LENGTH);
               rtArr[i] = height;

            }
            return rtArr;
        }

        /**
         * @Kyle
         * @define RecseePlayer.addPlayList
         * @type function
         * @description 플레이 리스트 추가 함수
         * @param listenUrl(string) 추가 할 요청 URL 주소
         */
        rc.addPlayList = function(listenUrl){

        	var targetObj = $(rc.target)
        	var fileName = rc._RC.getFileNameFromURL(listenUrl)
        	var htmlString = "<li listenUrl='"+listenUrl+"'><input type='checkbox' class='listCheckBox' style='display:none'/><span>"+fileName+"</span></li>";

        	// 리스트에 없을 경우 추가 해주고, 클릭시 재생 이벤트 붙여준다.
        	if(targetObj.find("[listenUrl='"+listenUrl+"']").length == 0){
        		targetObj.find(".list_wrap ul").append(htmlString)
                targetObj.find("[listenUrl='"+listenUrl+"']").on("click", function() {
                	rc.playPlayList($(this).attr("listenUrl"))
                });
        		targetObj.find('.play_list_popup').slideDown()
        		// 추가 될 경우 스크롤에 가려지므로, 스크롤을 밑으로 움직여 추가된 것을 확인 하게 해준당..
        		var listWrapObj = targetObj.find(".list_wrap")[0]
        		listWrapObj.scrollTop=listWrapObj.scrollHeight
        	}
        }

        /**
         * @Kyle
         * @define RecseePlayer.removePlayList
         * @type function
         * @description 플레이 리스트 삭제 함수
         * @param listenUrl(string) 제거할 요청 URL 주소
         */
        rc.removePlayList = function(listenUrl){
        	$(rc.target).find("[listenUrl='"+listenUrl+"']").remove()
        }

        /**
         * @Kyle
         * @define RecseePlayer.playPlayList
         * @type function
         * @description 플레이 리스트 재생 함수
         * @param listenUrl(string) 재생 요청 할 URL 주소
         */
        rc.playPlayList = function(listenUrl){
    		$(".nowPlay").removeClass("nowPlay")
    		$(rc.target).find("[listenUrl='"+listenUrl+"']").addClass("nowPlay")
    		//임시 청취 데이터
    		//FIXME
    		//var listenUrl = "HTTP://192.168.0.222:8088/F/var/REC/RecSee_Data/20170526/17/201705261719490402_1003__A.mp3";
    		rc.setFile("audio", listenUrl);
        }

        /**
         * @define RecseePlayer.setHeaderTitle
         * @type function
         * @description 헤더의 텍스트를 설정한다
         * @param str(string)
         */
        rc.setHeaderTitle = function(str) {
            return $controller.setHeaderTitle(str);
        }
        /**
         * @define RecseePlayer.setNowTimeText
         * @type function
         * @description 초 단위의 시간을 넘겨받아 현재 시간 텍스트를 (HH:mm:ss 형태로) 설정한다.
         * @param sec(integer): 시간(초)
         */
        rc.setNowTimeText = function(sec) {
            return $controller.setNowTimeText(sec);
        }
        /**
         * @define RecseePlayer.setTotalTimeText
         * @type function
         * @description 초 단위의 시간을 넘겨받아 총 시간 텍스트를 (HH:mm:ss 형태로) 설정한다.
         * @param f(integer): 시간(초)
         */
        rc.setTotalTimeText = function(sec) {
            return $controller.setTotalTimeText(sec);
        }
        /**
         * @define RecseePlayer.setHeaderTitle
         * @type function
         * @description 파일명 텍스트를 설정한다
         * @param str(string)
         */
        rc.setFileNameText = function(str) {
            return $controller.setFileNameText(str);
        }
        /**
         * @define RecseePlayer.setHeaderTitle
         * @type function
         * @description 정보 텍스트를 설정한다
         * @param str(string)
         */
        rc.setFileInfoText = function(str) {
            return $controller.setFileInfoText(str);
        }

        /**
        * @define RecseePlayer.setRecInfoText
        * @type function
        * @description 녹취 텍스트를 설정한다. 객체 혹은 순서대로 인자를 넘김
        * @param {user: 상담원명, ext: 내선번호, id: 상담원 아이디, recDate: 녹취일자, recLen: 녹취길이, custNo: 고객번호}(object)
        * || user(string), ext(string), id(string), recDate(string), recLen(string), custNo(string)
        */
        rc.setRecInfoText = function(user, ext, id, recDate, recLen, custNo) {
            return $controller.setRecInfoText(user, ext, id, recDate, recLen, custNo);
        }

        /**
        * @define RecseePlayer.toggleView
        * @type function
        * @description player의 view를 변경한다
        * @param view(string): 변경하려는 view, 만약 올바르지 않으면 toggle한다.
        */
        rc.toggleView = function(view) {
            // FIXME: 뷰 형태 변경하는 부분
            //console.log("토글 안됨ㅜㅜ");
            // if(!rc.type[TYPES.EXPAND_VIEW]) {return;}   // 토글 불가 모드인 경우 작업 취소
            // view = (view||"").toLowerCase();
            // if(view != "expand" && view != "reduce") {
            //     // toggle
            //     view = $controller.find(".view_expand, .view_reduce").filter(":visible").attr("class").replace(/^view_/, "");
            // }
            // var path = function(src) {
            //     // recsee_player.js를 제외한 경로 얻기
            //     var pathArr = src.split("/");
            //     if(pathArr.length <= 1) {
            //         // 경로가 상대경로인 경우
            //         pathArr = location.pathname.split("/");
            //     }
            //     pathArr.pop();
            //     var path = pathArr.join("/");
            //     return path;
            // }($('link#player_css').attr("href"));
            // $('link#player_css').attr("href", path + (view == "reduce"?"/player_embed.css":"/player.css"));
        }
        // --------------------------------------------------
        
        /**
         * @define RecseePlayer.setTime
         * @type function
         * @description 플레이어 시간을 변경 한다.
         * @param time(float)
         */
        rc.setTime = function(time) {
        	//syncDo("pause");
        	//rc.playBackRate(getCookie("playBack"))
        	if(time > 0)
        		syncDo("currentTime", time);
        	//syncDo("play");
        	$wave.clear();
        	if(rc.type.dual == true)
        		$tx_wave.clear();
        }
        // --------------------------------------------------
        
        /**
         * @define RecseePlayer.setTimeLoop
         * @type function
         * @description 플레이어를 sTime부터 eTime까지 반복 재생 한다.
         * @param sTime(float)
         * @param eTime(float)
         */
        rc.setTimeLoop = function(sTime,eTime) {
        	rc.eTime = eTime;
        	rc.sTime = sTime;
        	if (Number(eTime)+ Number(sTime) > 0)
        		rc.setTime(sTime);
        }
        
        
        /**
         * @define RecseePlayer.playBackRate
         * @type function
         * @description 플레이어의 audio | video 객체의 배속을 설정한다.
         * @param rate(float) 배속
         */
        rc.playBackRate = function(rate, init) {
        		audio.playbackRate = rate;
        	if(rc.type[TYPES.AUDIO]) {
        		if (!init){
        			try{
        				audio.currentTime = audio.currentTime
            			if(rc.type[TYPES.DUAL]) {
            				tx_audio.currentTime = tx_audio.currentTime
            			}	
        			}catch(e){tryCatch(e)}
        		}
        		audio.playbackRate = rate;
        	}
        	if(rc.type[TYPES.VIDEO]) {
        		video.playbackRate = rate;
        		if (!init)
        			video.currentTime = video.currentTime
        		video.playbackRate = rate;
        	}

        	try{
        		$(".btn_speed input").val('x'+rate.toFixed(1));
        		rate = rate.toFixed(1)
        	}catch(e){
        		$(".btn_speed input").val('x'+rate);
        	}
        	
        	//setCookie("playBack",rate,14);
        }
        
        /**
         * @define RecseePlayer.currentTime
         * @type function
         * @description 플레이어의 audio | video 객체의 현 시점을 반환한다.
         * @return 재생중인 파일의 현재시점을 시:분:초 반환
         */
        rc.currentTime = function() {
        	var currentTime = audio.currentTime 
        	return rc._RC.toTimeString(currentTime)
        }
        
        /**
         * @define RecseePlayer.makeSection
         * @type function
         * @description 플레이어에 섹션을 만들어 준다.
         * @param startTime 섹션의 시작 시간
         * @param endTime 섹션의 종료 시간
         * @param startP 섹션의 시작 점 %
         * @param widthP 섹션의 종료 점 %
         * @param memoInfo 메모 정보 obj (memoIdx;메모 인덱스, memo;메모내용)
         * @return 만들어진 섹션의 obj(jquery selector형)
         */
        rc.makeSection = function(startTime, endTime, startP, widthP, memoInfo) {

        	// 라인, 마스크 제거
			$('.over_line_mark, .over_mask').remove();
    		$player.find(".player_marking_wrap").not(".marking_saved").remove();
        	
        	function percentPos(time) {
                var max = audio.duration||0;
                return max == 0?0:time / max * 100;
            };
            
            function PerToTime(per){
            	var max = audio.duration||0; 
            	return max == 0?0:max * per / 100
            }

        	var waveObj = $player.find(".waveObj");

        	// 시작점 % (css의 left 부분)
        	var sp = startP||percentPos(startTime);
        	// 섹션의 너비 % (css의 width 부분)
        	var wp = widthP||percentPos(endTime-startTime);
        	
        	// 100퍼센트 못넘게 처리
        	if (sp+wp >= 100)
        		wp = 100-sp
        	
        	var startTime = startTime||PerToTime(startP)
        	var endTime = endTime||PerToTime(Number(startP)+Number(widthP))

        	// 구간에 대한 버튼들 설정
        	var htmlString = "<ul>";
    		
        	/*htmlString += "<li class='playMarking'>재생</li>"*/
        	//메모 사용 할 때만 사용
        	if(rc.type[TYPES.MEMO])
        		htmlString += "<li class='memoMarking'>메모</li>"
        	
        	htmlString += "<li class='playMarkingLoop'>반복</li>"
        	htmlString += "<li class='playMarkingLoopDel' style='display:none;'>반복해제</li>"	
        	
        	// 다운 사용 할 때만 사용
        	if (rc.type[TYPES.BTNDOWN])		
        		htmlString += "<li class='downMarking'>다운</li>"
        	// 구간 묵음 사용 할 때만 사용
        	if (rc.type[TYPES.BTNMUTE]){
        		htmlString += "<li class='muteMarking'>묵음</li>"
        		htmlString += "<li class='muteMarkingDel' style='display:none;'>묵음해제</li>"	
        	}
        	// 구간 제거 사용 할 때만 사용
        	if (rc.type[TYPES.BTNDEL]){
        		htmlString += "<li class='cutoffMarking'>제거</li>"
        		htmlString += "<li class='cutoffMarkingDel' style='display:none;'>제거해제</li>"
        	}
        	htmlString += "<li class='deleteMarking'>삭제</li>"
        	htmlString += "</ul>";		
        	
        	waveObj.append('' +
        			'<div class="player_marking_wrap" style="left: '+sp+'%; width: '+wp+'%; background-color:rgba(255,140,0,0.83);">'+
        				'<div class="player_marking_obj" style="display: block;">'+
        				'</div>'+
        			'</div>')
        			
        	var nowFinishMarkingObj = $player.find(".player_marking_wrap:last-of-type");        	
        	var nowPlayerMarkingObj = nowFinishMarkingObj.find(".player_marking_obj");
        	var nowPlayerMarkingObjBtn = nowPlayerMarkingObj.append(htmlString).find("ul");
        	
        	nowFinishMarkingObj.attr("title",(memoInfo ? memoInfo.memo : ''))
        	nowFinishMarkingObj.data(memoInfo);
        	nowFinishMarkingObj.data({"startTimeSec" : startTime,"endTimeSec":endTime});
        	
        	nowFinishMarkingObj.addClass('marking_saved').css('pointer-events','');
	
	    	// 메모 사용 할 때만 사용
	        if(rc.type[TYPES.MEMO] && memoInfo == undefined){
	        	var dataStr = {
						"recDate" 	: rc.recFileDate.recDate.replace(/-/gi,'')
					,	"recTime" 	: rc.recFileDate.recTime.replace(/:/gi,'')
					,	"extNum" 	: rc.recFileDate.recExt
					,	"vFilename" : rc.recFileDate.recvFilename
					,	"startTime" : startTime
					,	"endTime" 	: endTime
					,	"proc" 		: "insert"
				}
	        		
				$.ajax({
					url: contextPath+"/recMemoProc.do",
					data: dataStr,
					type: "POST",
					dataType: "json",
					cache: false,
					success: function(jRes) {
						nowFinishMarkingObj.data("memoIdx",jRes.resData.memoIdx);
					}
				});
        	}
	
    		// 재생
    		nowFinishMarkingObj.find(".playMarking").click(function(){
    			setTimeout(function(){
    				syncDo("pause");
    				syncDo("currentTime",startTime);
    				syncDo("play");
    			}, 100);
    		});
	    		
    		// 반복재생
    		nowFinishMarkingObj.find(".playMarkingLoop").click(function(){
    			// 플레이어 전체 반복 삭제 비활 성 후 현재 클릭된 영역만 활성화
    			$player.find(".playMarkingLoopDel").hide();
    			nowFinishMarkingObj.find(".playMarkingLoopDel").show();
    			
    			// 전체 반복 재생 활성 화 후 현재 반복 재생 버튼 비활성화
    			$player.find(".playMarkingLoop").show()
    			$(this).hide();
    			
    			$player.find(".markingLoop").removeClass("markingLoop");
    			nowFinishMarkingObj.addClass("markingLoop")
    			setTimeout(function(){
    				rc.setTimeLoop(startTime,endTime)
    				rc.obj.wave.clear()
    				nowPlayerMarkingObjBtn.hide();
    			}, 100);
    		});
	    		
    		// 반복 해제
    		nowFinishMarkingObj.find(".playMarkingLoopDel").click(function(){
    			nowFinishMarkingObj.find(".playMarkingLoop").show();
    			$(this).hide();
    			setTimeout(function(){
    				rc.setTimeLoop(0,0);
    				rc.obj.wave.clear();
    				nowPlayerMarkingObjBtn.hide();
    			}, 100);
    		});
	    		
    		// 다운
    		nowFinishMarkingObj.find(".downMarking").click(function(){
    			setTimeout(function(){
    				// @kyle 구간 지정 후 파일 다운로드
	            	var dataStr = {
	            			"cStartTime" : startTime
	            		, 	"cEndTime" : endTime
	            		,	"url" 	: getParameterFromUrl("url", rc.requestParam)
	            		,	"cmd"   : "cut"
	            	}
	            	
	            	var src = "http://"+rc._RC.DEFAULT_OPTION.REQUEST_IP+":"+rc._RC.DEFAULT_OPTION.REQUEST_PORT+"/down?";
	            	
	            	$player.find( ".rDownloadFrame" ).attr("src",src+$.param(dataStr));
	            	nowPlayerMarkingObjBtn.hide();
    			}, 100);
    		});
	    		
    		// 음소거
    		nowFinishMarkingObj.find(".muteMarking").click(function(){
    			setTimeout(function(){
        			nowPlayerMarkingObjBtn.hide();
        			nowFinishMarkingObj.removeClass("markingCutoff").addClass("markingMute"); 
        			nowFinishMarkingObj.data({"st":startTime, "et":endTime});
        			nowFinishMarkingObj.find(".muteMarking, .cutoffMarkingDel").hide()
        			nowFinishMarkingObj.find(".muteMarkingDel, .cutoffMarking").show()
    			}, 100);
    		});
    		
    		// 음소거 해제
    		nowFinishMarkingObj.find(".muteMarkingDel").click(function(){
    			setTimeout(function(){
        			nowPlayerMarkingObjBtn.hide();
        			nowFinishMarkingObj.removeClass("markingMute"); 
        			nowFinishMarkingObj.data({"st":"", "et":""});
        			nowFinishMarkingObj.find(".muteMarkingDel").hide()
        			nowFinishMarkingObj.find(".muteMarking").show()
    			}, 100);
    		});
	    		
    		// 제거
    		nowFinishMarkingObj.find(".cutoffMarking").click(function(){
    			setTimeout(function(){
    				nowPlayerMarkingObjBtn.hide();
        			nowFinishMarkingObj.removeClass("markingMute").addClass("markingCutoff");
        			nowFinishMarkingObj.data({"st":startTime, "et":endTime});
        			nowFinishMarkingObj.find(".cutoffMarking, .muteMarkingDel").hide()
        			nowFinishMarkingObj.find(".cutoffMarkingDel, .muteMarking").show()
    			}, 100);
    		});
    		
    		// 제거해제
    		nowFinishMarkingObj.find(".cutoffMarkingDel").click(function(){
    			setTimeout(function(){
    				nowPlayerMarkingObjBtn.hide();
        			nowFinishMarkingObj.removeClass("markingCutoff")
        			nowFinishMarkingObj.data({"st":"", "et":""});
        			nowFinishMarkingObj.find(".cutoffMarkingDel").hide()
        			nowFinishMarkingObj.find(".cutoffMarking").show()
    			}, 100);
    		});
	
    		// 메모
			nowFinishMarkingObj.find(".memoMarking").click(function(){
				var memoInfo = {
						"memoIdx" : nowFinishMarkingObj.data("memoIdx")
					,	"memo" : nowFinishMarkingObj.data("memo")
				}
				top.memoPopup(memoInfo, nowFinishMarkingObj);
			});
	
			// 삭제
			nowFinishMarkingObj.find(".deleteMarking").click(function(){
				if(confirm("삭제 하시겠습니까?")){
					// 메모 사용 할 때만 사용
	            	if(rc.type[TYPES.MEMO]){
						var memoIdx = nowFinishMarkingObj.data("memoIdx");
						var dataStr = {
								"memoIdx" :memoIdx
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
								alert("메모 삭제가 완료 되었습니다.")
							}
						});
	            	}else{
	            		if(nowFinishMarkingObj.hasClass(".markingLoop")){
	            			rc.sTime = 0;
	            			rc.eTime = 0;
	            		}
	            		nowFinishMarkingObj.remove();
						/*alert("삭제가 완료 되었습니다.")*/
	            	}
	            	
	            }
			});
	
			// 설정 버튼 생성
			nowPlayerMarkingObj.show();
			
			// 설정 버튼 클릭 시 이벤트 처리
            (function onBtnMarkinObj() {
            	
            	if (rc.type[TYPES.MENUCLICKRIGHT]){
	            	nowPlayerMarkingObj.off("mousedown")
	            	nowPlayerMarkingObj.on("mousedown",function(e){
	            		if(e.button ==2){
	            			e.stopPropagation();
	                	    $('.player_marking_obj ul').hide();
	                	    var menuObj = $(this).find('ul');
	                	    if(menuObj.is(":visible"))
	                	    	menuObj.hide();
	                	    else
	                	    	menuObj.show();
	            		}
	            	});
            	}
            	
            	if (rc.type[TYPES.MENUCLICKLEFT]){
	            	nowPlayerMarkingObj.off("click")
	            	nowPlayerMarkingObj.on("click", function(e) {
	            	    e.stopPropagation();
	            	    $('.player_marking_obj ul').hide();
	            	    var menuObj = $(this).find('ul');
	            	    if(menuObj.is(":visible"))
	            	    	menuObj.hide();
	            	    else
	            	    	menuObj.show();
	                });
            	}
                $('body').click(function() {
                	$('.player_marking_obj ul').hide();
                })
            }());
            
            setTimeout(function(){
            	if(memoInfo == undefined)
            		nowPlayerMarkingObjBtn.show()
            },100)
            
            return nowFinishMarkingObj;
        }
        
        /**
         * @define RecseePlayer.uploadFile
         * @type function
         * @description 서버로부터 파일 업로드에 대한 응답을 받아 처리한다.
         * @param data 서버로 부터 받은 응답.
         */
        rc.uploadFile = function(data, oriname){
        	
        	var oriname = oriname
        	var data = data
        	if (oriname == undefined){
        		oriname = data.split(",")[0];
        		data= data.split(",")[1];
        	}
        	
        	if(data == "expiredFile"){
				alert("청취 가능 기간이 만료된 파일 입니다!");
			}else if(data =="noneexpiredate"){
				alert("청취 가능 기간이 만료된 파일 입니다!!");
			}else{
				rc.setFile("audio","?isUpload=true&url="+data.replace(/\\/gi,'/')+"&oriname=/"+oriname, true);
			}
			// 업로드 폼 초기화
			var uploadForm = $player.find(".fileUploadForm");
			// 익스 아닐 때
			uploadForm.val("");
			// 익스 일 때
			uploadForm.replaceWith(uploadForm.clone(true));
        }
        
        // 플레이어 포커스 시 키보드 및 마우스 우클릭 이벤트 적용
       /* $player.focusin(function(e){
        	document.orioncontextmemu = document.oncontextmenu;
        	document.oncontextmenu = function(){
        		return false;
        	}
        	
        	$(document).on("keydown", function (e) {
        		// 
        		if (e.which === 13){
        			e.preventDefault();
        			e.stopPropagation();
        			return false;
        		}
        		
        		// 스페이스바
        		if (e.which === 32){
        			
        			e.stopPropagation();
        			e.preventDefault();
        			
        			if($(".btn_start").is(":visible")){
        				$(".btn_start").trigger("click")
        			}else{
        				$(".btn_stop").trigger("click")
        			}
        		}	
        		// 방향키 좌
        		if (e.which === 37){
        			$player.find(".btn_prev").trigger("click");
        		}
        		// 방향키 우
        		if (e.which === 39){
        			$player.find(".btn_next").trigger("click");
        		}
        		// .> 키
        		if (e.which === 190){
        			$player.find(".audio_volume").show();
        			var volume = Number((getCookie("volume")) ? getCookie("volume") : 100) + 5;
        			if(volume >= 100)
        				volume = 100
        			else if (volume <= 0)
        				volume = 0
                    $volume.changeValue(volume).moveTo(volume);
        			setCookie("volume",volume,14);
        		}
        		// ,< 키
        		if (e.which === 188){
        			$player.find(".audio_volume").show();
        			var volume = Number((getCookie("volume")) ? getCookie("volume") : 100) - 5;
        			if(volume >= 100)
        				volume = 100
        			else if (volume <= 0)
        				volume = 0
                    $volume.changeValue(volume).moveTo(volume);
        			setCookie("volume",volume,14);
        		}
        		
        		// 방향키 상
        		if (e.which === 38){
        			$player.find(".speed_plus").trigger("click");
        		}
        		
        		// 방향키 하
        		if (e.which === 40){
        			$player.find(".speed_minus").trigger("click");
        		}
        		
        	});
        	
        	$(document).on("keyup", function (e) {
        		// < > 키업시 보여주었던 볼륨조절 0.5초 뒤 숨기기
        		if (e.which === 188 || e.which === 190){
        			clearTimeout($player.find(".audio_volume").data("timeout"))
        			$player.find(".audio_volume").data("timeout",setTimeout(function(){
        				$player.find(".audio_volume").hide();
        			},500));
        		}
        	});
        });
        
        $player.focusout(function(e){
        	$(document).off("keydown"); 
        	document.oncontextmenu = document.orioncontextmemu;
        });*/
        
        // 드래그 & 드랍 이벤트 처리
        // 파일이 들어 왔을 경우
        /*$player.on("dragenter", function(e){
        	e.stopPropagation();
        	e.preventDefault();
        	$upload.show(); 
        });
        
        // 파일이 덮어졌을 경우
        $player.on("dragover", function(e){
        	e.stopPropagation();
			e.preventDefault();
        });
        
        // 파일이 떠났을 경우
        $player.on("dragleave", function(e){
			e.stopPropagation();
			e.preventDefault();
			$upload.hide();
		});
        
        // 페이지에 파일을 떨어뜨렸을 경우
        $player.on("drop", function(e){
			e.preventDefault();
			var files = e.originalEvent.dataTransfer.files;
			handleFileUpload(files);
		});
        
        // 페이지에 파일이 들어왔을 경우 ( 무시 )
        $(document).on("dragenter", function(e){
			e.stopPropagation();
			e.preventDefault();
		});
        
        // 페이지에 파일이 덮어졌을 경우 (무시, 업로드 표시 출력)
		$(document).on("dragover", function(e){
			e.stopPropagation();
			e.preventDefault();
			$upload.show(); 
		});
		
		// 페이지에서 파일이 떠났을 경우 (무시, 업로드 표시 제거)
		$(document).on("dragleave", function(e){
			e.stopPropagation();
			e.preventDefault();
			$upload.hide();
		});
		
		// 페이지에 파일을 떨어뜨렸을 경우( 무시 )
		$(document).on("drop", function(e){
			e.stopPropagation();
			e.preventDefault();
			$upload.hide();
		});*/
		
		function handleFileUpload(files){
			for(var i = 0;i < files.length; i++){
				var fd = new FormData();
				fd.append("file",files[i]);
				
				if(files[i].name.split(".").pop() == "mp3"){
					sendFileToServer(fd,files[i].name);
				}else{
					alert("mp3형식의 파일만 업로드가 가능 합니다!");
					return;
				}
			}
		}
		
		function sendFileToServer(formData,oriname){
			var src = "http://"+rc._RC.DEFAULT_OPTION.REQUEST_IP+":"+rc._RC.DEFAULT_OPTION.REQUEST_PORT;
			var extraData = {};
			var jqXHR = $.ajax({
				crossDomain:true,
				xhr: function(){
					var xhrobj = $.ajaxSettings.xhr();			
					return xhrobj;
				},
				
				url:src+"/upload",
				type:"POST",
				processData:false,
				cache:false,
				data:formData,
				async:false,
				success:function(data){
					// 쓸데없는 구문 지워주깅
					rc.uploadFile(data.replace("<script>window.parent.postMessage(\"","").replace("\",\"*\")</script>",""))
				}
			});
		}
		
		function historyCheckPlay(target,url){

			var getCurrentTime = getCookie(url);
				
			if(getCurrentTime != undefined && getCurrentTime > 0 && $("#historyPlayCheck").is(":checked") && playerObj.duration != getCurrentTime){
				if(confirm("이전 재생 위치부터 이어서 재생 하시겠습니까?"))
					playerObj.currentTime = getCurrentTime
				else
					setCookie(playUrl, 0, 1);
			}else{
				setCookie(playUrl, 0, 1);
			}
			playerObj.play();
		}

        // 공통 이벤트
        function setMediaEvent(target) {
            var $target = $(target);
            target.addEventListener('timeupdate',function(event) {
            	
            	var targetCurrentTime = target.currentTime; 
            	var targetDuration = target.duration;
            	
                // 현재 재생중인 음성의 현재/전체를 읽어 POSITION BAR에 반영하는 작업 수행
                // 일반적인 재생중 이벤트가 아니라면 갱신하지 않음: 드래그 중에 현재 재생 위치로 자동 갱신하는 경우 방지
                if(rc.obj.controlBar.dragging) return;
                var percentPos = function() {
                    var max = targetDuration||0;
                    var current = targetCurrentTime||0;
                    return max == 0?0:current / max;
                }();
                var pos = percentPos * 100;
                $controlBar.changeValue(pos);
                // 플레이 중인 시간의 이전 wave의 class 변경
                var $playingTarget = $($.grep($wave.find(".obj_key > div"), function(d){return $(d).data("pos")<=pos})).find(".obj_key_value");
                $playingTarget.addClass("obj_key_value_now");
                
                $controller.setNowTimeText(targetCurrentTime);

                //익스 웹소켓 버전 변경으로 타이머 웹소켓으로 반영
                //$player.find(".procTime").text(secondToMinute(targetCurrentTime))

                function secondToMinute(sec){
                	var temp = seconds2time(sec).split(":")
                	return temp[0]+":"+temp[1]+":"+temp[2]
                }

	            // 플레이어 창이 않보이면 중단(평가 페이지 닫히면 꺼버령)
	            /*if(!$(rc.target).is(":visible"))
	            	rc.pause();
	            */
	            if((rc.sTime || rc.eTime) && $player.find(".markingLoop").length > 0){
            		if(rc.eTime < targetCurrentTime){
	            		rc.setTime(rc.sTime);
            		}
	            }
	            
	            if(target.currentTime > 0)
					setCookie(getParameterFromUrl("url",rc.requestParam), targetCurrentTime, 14);
	            
	            // 재생 구간 지정 시 막대기 변화
	            if( $player.find(".play_over_mask").length > 0){
	            	$player.find(".over_line").data("endTime",targetCurrentTime)	
	            	var percentPos = function() {
	                    var max = targetDuration||0;
	                    var current = targetCurrentTime||0;
	                    var start = $player.find(".over_line").data("startTime")
	                    return max == 0?0:(current-start) / max * 100;
	                }();
	            	
	                var markingWrapObj = $player.find(".player_marking_wrap").not(".marking_saved");
	                
	                if(percentPos < 0){
	                	var diff = $player.find(".over_line").data("oriLeft").replace(/px/gi,'')
	                	
	                	markingWrapObj.css("width", -1*percentPos+"%");
	                	diff -= markingWrapObj.css("width").replace(/px/gi,'')
	                	
	                	markingWrapObj.css("left", diff+"px");
	                	
	                }else{	                	
	                	markingWrapObj.css("width", percentPos+"%");
	                	markingWrapObj.css("left", $player.find(".over_line").data("oriLeft"));
	                }
	            	
	            }

            });
            target.addEventListener('durationchange',function(event) {
            	
                // 파일명 설정
                $controller.setFileNameText($target.data("name")||rc._RC.getFileNameFromURL($target.attr("src")));
                $controller.setTotalTimeText(target.duration);

                // 정보설정
                $controller.setPlayerInfo($target.data("ext"),$target.data("userId"),$target.data("userName"),$target.data("phoneNum"))
                var infoText = $player.find(".play_file_tit").text();

                var infoString = (rc.recFileDate && rc.recFileDate.recExt ? " / 내선번호 : " + rc.recFileDate.recExt : "")
                	infoString += (rc.recFileDate && rc.recFileDate.recUserName ? " / 상담원 이름 : " + rc.recFileDate.recUserName : "")
                	infoString += (rc.recFileDate && rc.recFileDate.recCustPhone ? " / 고객번호 : " + rc.recFileDate.recCustPhone : "")

                $player.find(".play_file_tit").text(infoText+infoString);

                $player.find(".waveObj").css('opacity','1');
                            	 
            });
            // 재생 시작 이벤트
            target.addEventListener('playing',function(event) {

            	// 리스트가 있을 때만 실행
            	if($player.find(".play_list_menu.ui_sub_btn_flat").length > 0){
            		// 다시 리스트에 있는 파일 재생시 커렌트 타임부터 이어서 재생시켜쥼 캬캬캬캬
                    if($player.find(".nowPlay").length > 0){
    	                if(target.src.toUpperCase() == $player.find(".nowPlay").attr("listenUrl").toUpperCase()){
    	                	if(parseFloat($player.find(".nowPlay").attr("currentTime")) > 0){
    	                		//target.currentTime=parseFloat($player.find(".nowPlay").attr("currentTime"))
    	                		//if($audio.canplay)
    	                		target.pause();
    	                		target.currentTime=parseFloat($player.find(".nowPlay").attr("currentTime"))
    	                		target.play();
    	                		var time = parseFloat($player.find(".nowPlay").attr("currentTime"));

    	                		setTimeout(function(){ rc.obj.audio.setTime(time); }, 100);
    	                		$player.find(".nowPlay").removeAttr("currentTime")
    	                	}
    	                }
                	}
            	}

            	if(rc.type.wave){
            		try{
            			//웨이브 너비
                    	var total = Number($player.find(".waveObj").css("width").replace(/px/gi,''))
                    	// 재생중인 파일의 총 재생 시간(초)
                    	var totalPlayTime = rc.obj.audio[0].duration;

                        $player.find(".player_marking_wrap").each(function( i ) {
                        	//넘어온 %left
                        	var leftPos = $(this).attr("left")
                        	// 재생시간/웨이브너비 = 너비당 재생시간
                        	var startTime = leftPos
                        	leftPos = startTime * total / totalPlayTime
                        	var per = Math.abs(leftPos/total) * 100
                        	//$(this).css("left","calc(" +per+ "% - 110px)")
                        	$(this).css("left",per+"%")

                        });
            		}catch(e){
            			tryCatch(e)
            		}
            	}
            	
            	// 플래이 시 배속 지정
            	// 로드 완료 시 배속 지정
    			setTimeout(function(){
	    			//var nowPlayBack = getCookie("playBack") || target.playbackRate || 1;
	    			var nowPlayBack = 1;
	            	rc.playBackRate(nowPlayBack);
    			},500);

            });
            
            // 데이터 로드 완료 이벤트
            target.addEventListener('loadeddata',function(){
            	   try{
            		   OnControlsPlay();
            	   }
            	   catch(e){
            	      return;
            	   }
            	var $target = $(target);
            	
            	(function(playerObj,playUrl){
            		
            		if ($(".replay_check_txt").hasClass("checked")){
            			var getCurrentTime = getCookie(playUrl);
        				if(getCurrentTime != undefined && getCurrentTime > 0 && playerObj.duration != getCurrentTime){
        					if(confirm("재생중이던 파일 입니다. 이어서 재생 하시겠습니까?")){
        						syncDo("currentTime",getCurrentTime);
        					}else
        						setCookie(playUrl, 0, 14);
        				}else{
        					setCookie(playUrl, 0, 14);
        				}
            		}
            		
    	            
    			}(target,getParameterFromUrl("url",rc.requestParam)))
    			
    			/*if(rc.type[TYPES.WAVE])
    				rc.updateWave(getParameterFromUrl("url", rc.requestParam))*/
                 
    			if((rc.settingTime||0) > 0){
    				rc.setTime(rc.settingTime);
    				rc.settingTime = 0;
    			}
    			
    			// 로드 완료 시 배속 지정
    			setTimeout(function(){
    				//var nowPlayBack = Number(getCookie("playBack") || target.playbackRate || 1)
    				var nowPlayBack = 1;
    				rc.playBackRate(nowPlayBack,true);
    			},500);
            	
            	if(rc.recFileData.recMemo){
            		var memoStr = rc.recFileData.recMemo
            		rc.recMemoInfo.recMemo = ''
    	        	var datas = memoStr.split("|")
    	        	var memoInfo = {};
    	        	var waveObj = rc.obj.wave.find(".waveObj")

    	        	// 메모 생성 객체 만드러 주깅
    	        	for(var i = 0 ; i < datas.length ; i++){
    	        		var memo = {}
    	        		memo["memoIdx"] = datas[i].split(",")[0];
    	        		memo["startTime"] = datas[i].split(",")[1];
    	        		memo["endTime"] = datas[i].split(",")[2];
    	        		memo["memo"] = datas[i].split(",")[3];

    	        		memoInfo[i+""] = memo;
    	        	}

    	        	for(var i = 0 ; i < Object.keys(memoInfo).length; i++){
    	        		
    	        		var memoObj =  memoInfo[i];
    	        		var memoIdx = memoObj.memoIdx;
    	        		var startTime = memoObj.startTime;
    	        		var endTime = memoObj.endTime;
    	        		var memo = memoObj.memo;
    	        		
    	        		var memoInfoParam = {
    	        				"memoIdx"	: memoIdx
    	        			,	"memo"		: memo
    	        		}
    	        		
    	        		rc.makeSection(startTime,endTime,undefined,undefined,memoInfoParam);
    	        	}
            	}
    			
	
            });
            
           
            
            // 재생 중단 이벤트
            target.addEventListener('ended',function(event) {
                syncDo("pause");
                
                // 재생 리스트가 있으면, 리스트에 있는 목록 재생
                if($player.find(".list_wrap ul li").length > 0){
                	var nowPlayObj = $player.find(".list_wrap ul li.nowPlay");
                	var nowPlayUrl = nowPlayObj.attr("listenurl");
                	var audioObj = $player.find("audio")

                	//nowPlayUrl = "HTTP://192.168.0.222:8088/F/var/REC/RecSee_Data/20170526/17/201705261719490402_1003__A.mp3"; // 암호화 않된거
                	// 종료된 시점에 url이 현재 재생 목록의 url과 일치한다면 재생 목록의 다음 파일 재생 (재생목록에서 재생한 파일이라고 판단)
                	if(nowPlayUrl == audioObj.attr("src") && audioObj.attr("src")){

                		var nextPlayObj = nowPlayObj.next();
                		if(nextPlayObj.length == 0){
                			alert("더 이상 재생할 목록이 없습니다.")
                		}else{
                			nowPlayObj.removeClass("nowPlay");
                    		nextPlayObj.addClass("nowPlay")
                    		var nextPlayUrl = nextPlayObj.attr("listenurl");
                    		// 임시데이터
                        	// FIXME;;
                    		//nextPlayUrl = "HTTP://192.168.0.222:8088/F/var/REC/RecSee_Data/20170526/17/201705261719490402_1003__A.mp3"; // 암호화 않된거
                    		rc.setFile("audio", nextPlayUrl);
                		}

                	// 종료된 시점에 url이 현재 재생 목록의 url과 일치하지 않는다면, 현재 재생목록에서 재생중인 파일이 없거나, 다른플레이어에서 강제로 기존 재생을 끊어버린 경우로 판단.
                	}else if(nowPlayUrl != audioObj.attr("src") && audioObj.attr("src")){
                		// 현재 재생중인 목록이 없다면. 리스트의 가장 첫번째를 재생시킨다.
                		if(!nowPlayUrl){
                			$player.find(".list_wrap ul li:eq(0)").addClass("nowPlay")
                			var listenUrl = $player.find(".list_wrap ul li:eq(0)").attr("listenUrl")
                			// FIXME;;임시데이터
                			//listenUrl = "HTTP://192.168.0.222:8088/F/var/REC/RecSee_Data/20170526/17/201705261719490402_1003__A.mp3"; // 암호화 않된거
                			rc.setFile("audio", listenUrl);
                		// 현재 재생중인 목록이 있고, audio의 url이 다르다면, 다른이벤트에서 강제로 연결을 끊은 것 이기때문에 지속하여 재생시킨다.
                		}else{
                			// FIXME;;임시데이터
                			//nowPlayUrl = "HTTP://192.168.0.222:8088/F/var/REC/RecSee_Data/20170526/17/201705261719490402_1003__A.mp3"; // 암호화 않된거
                			rc.setFile("audio", nowPlayUrl);
                		}
                	}
            	}
            });

            // 시점 이동 엔터 처리 이벤트 ; ie9에서 버튼에 포커스가 가 있을 경우, 이벤트를 무시하고, 버튼 클릭 이벤트가 작동 되므로..
            // 따로 처리함..... 하..ie9 진심..--;;;
            $player.find(".play_jump input").on("keydown",function(e) {
            	if (e.which === 13){
            		e.stopPropagation();
        			e.preventDefault();
            		moveTimeInput();
            	}
            });
            
            // 시점 이동 숫자 외 문자 걸러내기 이벤트
            $player.find(".play_jump input").on("keyup",function(e) {
            	
            	var value = this.value;
            		
            	// 숫자 이외 걸러 내기
            	value = value.replace(/[^0-9.]/g,'');
            	// 소숫점. 2개이상 입력 불가 처리
            	var result = value.match(/\./g);
            	if (result != null && result.length > 1)
            		value = value.replace(/\.+$/,'');
            	
            	this.value = value;	
            });
            
            // 이동 버튼 이벤트
            $player.find(".play_jump button").on("click",function(event) {
            	moveTimeInput()
            	$player.find(".play_jump input").focus();
            });
            
            function moveTimeInput(){
            	// 이동 할 시점
            	var setTime = $player.find(".play_jump input").val();
            	
            	// 전체 길이 및 음수 못벗어 나게 처리
            	var duration = target.duration
            	
            	if (setTime > duration)
            		setTime = duration
            	if (setTime <= 0)
            		setTime = 0
            		
            	rc.setTime(setTime)
            }

            // 시작, 일시정지 버튼 이벤트
            $player.find(".btn_start").on("click",function(event) {

            	// 실감일때 Init호출
            	// @kyle
            	// 실감 모드 일 경우 로직 추가...
            	if(rc.type.realtime){
            		if(RealTimeExt.trim().length==0){
            			alert("감청하실 대상의 청취 버튼을 클릭 해 주세요.")
            			$(this).removeClass("btn_stop");
            		}else{
            			// 실행 중이면 중지하기
            			if($player.find(".btn_stop").length > 0){
            				listenEvent(RealTimeExt)//listenEvent realtime.js에 있음
            				$(this).removeClass("btn_stop");
            			}else{
            				SetServerinfo()
                			Init(RealTimeExt) //audio_api.js에 있음
            				$(this).addClass("btn_stop");
            			}

            		}
            	}else{
	            	if(target.paused) {
	                    // 재생 이벤트
	                    var canPlay = $controller.canPlay();
	                    if(!canPlay) {
	                        // FIXME: 파일이 설정되지 않은 경우
	                        $(function() {
	                            alert("파일이 없습니다");
	                        });
	                    } else {
	                        if(target.currentTime == target.duration) {
	                        	if(rc.type[TYPES.WAVE]) {
	                                $wave.clear();
	                                if(rc.type[TYPES.DUAL]) {
	                                	$tx_wave.clear();
	                                }
	                            }
	                            // 맨끝에서 재생시, 처음으로 돌림
	                            syncDo("currentTime", 0);
	                        }
	                        syncDo("play");
	                        //syncDo("playbackRate", 2);
	                        $(this).removeClass("btn_start").addClass("btn_stop");
	                    }
	                } else {
	                    // 일시정지 이벤트
	                    syncDo("pause");
	                    $(this).removeClass("btn_stop").addClass("btn_start");
	                }
            	}
            });

            if(rc.type.realtime)
            	 $player.find(".btn_start").off("click")

            // @kyle
            // 메모 버튼 (실감 버전)
            // 헤헤헤...점점 하드코딩 되가네..;
            // 메모
//			$player.find(".addMemo").click(function(){
//				$("#memoContents").val("")
//				if(!RealTimeExt){
//					alert("현재 실시간 감청중인 대상이 없습니다.")
//					return;
//				}
//
//				$("#recMemo").data("obj",$("#"+RealTimeExt));
//
//				if($("#recMemo").length == 0){
//					$('body').append(rc.memoPopupHtmlString)
//					$("#recMemo").data("obj",$("#"+RealTimeExt));
//
//					// 메모 추가 이벤트
//					$("#memoAdd").click(function(){
//						var memo = $("#memoContents").val()
//
//						var dataStr = {
//								"recsTime" 	: $("#recMemo").data("obj").attr("callStime")
//							,	"extNum" 	: $("#recMemo").data("obj")[0].id
//							,	"memo"		: memo
//							,	"custPhone1": $("#recMemo").data("obj").attr("phonenum")
//							,	"proc" 		: "insert"
//							,	"type"		: "real"
//						}
//
//						$.ajax({
//							url: contextPath+"/recMemoProc.do",
//							data: dataStr,
//							type: "POST",
//							dataType: "json",
//							cache: false,
//							success: function(jRes) {
//								layer_popup_close();
//								alert("메모 저장이 완료 되었습니다.")
//							}
//						});
//					});
//				}
//				layer_popup("#recMemo")
//			});

            // 앞, 뒤로가기 버튼 이벤트 정의
            $player.find(".btn_prev, .btn_next").click(function(event) {
                var type = $(this).hasClass("btn_prev")?"prev":($(this).hasClass("btn_next")?"next":"");
                if(type == "")  return;
                // MOVE_TIME만큼 앞/뒤로 이동
                var moveVal = (target.currentTime + (rc.moveTime * (type == "prev"?-1:1))) / target.duration * 100;
                $controlBar.moveTo(moveVal);

            });

            // @ezra
            // 리스트 팝업 오픈
            $player.find(".play_list_menu").click(function(event) {
            	event.stopPropagation();
            	$player.find('.play_list_popup').slideToggle();
            })
            
            $player.find(".audio_realtime_memo").click(function(event){
              	if(RealTimeExt == ""){
              		//alert("청취중이 아닙니다.");
              		alertText("청취","청취중이 아닙니다.");
              		//$('.view_tooltip').html("청취중이 아닙니다.");
//              		setTimeout(function(){
//              			$('.view_tooltip').html("");
//              		},2000);
              		return false;
              	}
              	
            	var data  = {
            			"rectime" : defineStime
        			,	"extNum"	 	   : RealTimeExt
            	}
            	$.ajax({
        			url: contextPath+"/recMemoSelect.do",
        			data: data,
        			type: "POST",
        			dataType: "json",
        			cache: false,
        			async: false,
        			success: function(jRes) {
        				if(jRes.success == "Y"){
        					 $("#proc").val("modify");
        					 //$("#realTimeMemoContents").html(jRes.resData.contents.replace(/\[[0-9]{2}:[0-9]{2}:[0-9]{2}]/gi,''));
        				}else{
        					 $("#proc").val("insert");
        					 //$("#realTimeMemoContents").html("");
        				}
        			 	layer_popup('#realTimeMemo');
        			 	$('#realTimeMemo').css("position","fixed");
        			}
        		});
            });
            

            // @ezra
            // 배속 버튼 이벤트 정의
            $player.find(".speed_btn_wrap button").click(function(event) {

            	var nowPlayBack = Number(getCookie("playBack") || target.playbackRate || 1)
            	            	
            	if ($(this).hasClass("speed_plus")){
            		nowPlayBack += 0.1;
            		if(nowPlayBack > 3.0)
            			nowPlayBack = 3.0;
            	}else if($(this).hasClass("speed_minus")){
            		nowPlayBack -= 0.1;
            		if(nowPlayBack < 0.5)
            			nowPlayBack = 0.5;
            	}else{
            		nowPlayBack = 1;
            	}

            	// 함수 호출
            	rc.playBackRate(nowPlayBack);
            });

            //@재생 구간 지정
            // 구간 지정 버튼 이벤트 정의
            $player.find('.main_btn_section').on("click", function() {

            	if ($(this).hasClass('ui_disable'))
            		return;
            	
            	syncDo("pause");
            	
            	// 웨이브 레이어
            	var waveLayer = $player.find(".waveObj")

            	// 재생 구간 취소 버튼 활성 화 및 생성 후 나머지 숨김
            	$player.find('.main_btn_section').hide();
            	$player.find('.btn_section').addClass('ui_disable')
            	$player.find('.main_btn_section_end').show().removeClass('ui_disable')
            	
            	// 마스크 오브젝트 생성
            	// ※이유: 웨이브와 이벤트 중복 방지
            	waveLayer.append('<div class="over_mask play_over_mask"></div>');
            	
            	// 마스크 오브젝트
            	var waveMaskObj = waveLayer.find('.over_mask');
            	
            	// 라인 오브젝트 생성
        		waveMaskObj.append('<div class="over_line"></div>');
        		
        		var lineObj = waveMaskObj.find(".over_line");
        		
        		var percentPos = function() {
                    var max = audio.duration||0;
                    var current = audio.currentTime||0;
                    return max == 0?0:current / max * 100;
                }();
                
            	lineObj.css("left",percentPos+"%").data("startTime",audio.currentTime);
            	lineObj.data("oriLeft",lineObj.css("left"),'');
            	// 지정할 구간 표식해줄 라인 생성
        		waveMaskObj.append('<div class="over_line_mark" style="left:'+percentPos+'%;"></div>');
        		// 첫번째 표식이 생기면 드래그 이벤트를 반영할 오브젝트 생성
        		waveLayer.append(''+
            			'<div class="player_marking_wrap" style="left:'+percentPos+'%; background-color:rgba(255,140,0,0.83);">'+
    	            		'<div class="player_marking_obj"></div>'+
    		            '</div>');
            	
            	syncDo("play");
            	
            });
            
            //@kyle
            //재생 구간 지정 완료 이벤트
            $player.find('.main_btn_section_end').on("click", function() {
            	syncDo("pause");
            	
            	$player.find('.btn_section').removeClass('ui_disable').show();
            	$player.find('.main_btn_section_end').hide();
            	
            	var startTime = Number($player.find(".over_line").data("startTime")||0)
    			var endTime = Number($player.find(".over_line").data("endTime")||0)
    			
    			if (startTime < endTime)
    				rc.makeSection(startTime,endTime);
    			else
    				rc.makeSection(endTime,startTime);
            });
            

            // @ezra
            // 구간 지정 버튼 이벤트 정의
            $player.find('.main_btn_selector').on("click", function() {
            	
            	
            	if($player.find('.over_mask').length != 0){
            		initMarkingStatus();
            	}else{
            		if ($(this).hasClass('ui_disable'))
                		return;
                	initMarkingStatus();
                	
                	$player.find('.btn_section').not(this).addClass('ui_disable')
                	
                	// 웨이브 레이어
                	var waveLayer = $player.find(".waveObj")

                	// 구간 지정 버튼 삭제
                	$('.btnMarking').hide();

                	// 구간 취소 버튼 생성
                	$('.btnMarkingClose').show();

                	// 마스크 오브젝트 생성
                	// ※이유: 웨이브와 이벤트 중복 방지
                	waveLayer.append('<div class="over_mask"></div>');

                	// 마스크 오브젝트
                	var waveMaskObj = waveLayer.find('.over_mask');

                	// hover 이벤트
                	waveMaskObj.hover(function(event) {
                        // 마우스 x좌표 값 초기화
                    	var mouseX = 0;

                    	$(this).mousemove(function(event) { // move 이벤트
                    		// 라인 오브젝트 생성
                    		waveMaskObj.append('<div class="over_line"></div>');
                    		
                    		// 마우스 x좌표 값 초기화
                            var mouseX = 0;

                            // @ezra
                            // 라인 위치값 변경 이벤트
                            (function moveLine() {
                            	var lineObj = $('.over_line');

                            	// 마스크 오브젝트 x좌표 구하기
                            	// ※이유 : 레이어 팝업으로 뜰 경우 0에서 시작하지 않음
                            	var objPos = $('.over_mask').offset().left;

                            	// 마우스 위치값 구하기
                            	// 전체 페이지에서 마우스 x값 - 오브젝트 x값
                            	mouseX = event.clientX - objPos;

                            	// 라인 위치 파악하기
                            	var linePos = parseInt(lineObj.css('left').replace('px', ''));

                            	// 라인 위치값 변경
                            	lineObj.css("left", mouseX)
                            })();
                    		
                    		// 구간 지정 이벤트 실행
                    		(function onClickLineObj() {
                            	var lineObj = $player.find('.over_line');
                            	/*lineObj.click(function() {*/
                            	lineObj.mouseup(function() {
                                	var waveLayer   = $player.find('.waveObj');
                                	var waveMaskObj = $player.find('.over_mask');
                            		var objMarkLine = $player.find('.over_line_mark');

                            		var firstWidth = 'calc(100% - '+mouseX+'px)'
                            		var lineOffsetLeft = mouseX/waveMaskObj.width() * 100;
                            		
                            		// 지정할 구간 표식해줄 라인 생성
                            		waveMaskObj.append('<div class="over_line_mark" style="left:'+lineOffsetLeft+'%;"></div>');
                            		
                            		// 표식 갯수 구하기
                            		var markNo = $player.find('.over_line_mark').length
                            		var tempLeft = 0;
                            		if(markNo == 1) { // 첫번째 표식
                            			// 첫번째 표식이 생기면 드래그 이벤트를 반영할 오브젝트 생성
                            			waveLayer.append(''+
    	                        			'<div class="player_marking_wrap" style="left:'+lineOffsetLeft+'%; width:'+firstWidth+'">'+
    	                	            		'<div class="player_marking_obj"></div>'+
    	                		            '</div>'
                                		);
                            			
                                		// 마킹 부분 마우스 이벤트 제거
                                		// 이유 : 해당 부분에 마우스 이벤트 걸리면 구간 설정 중 줄이기 안됨.
                                    	$('.player_marking_wrap:last-of-type').css('pointer-events', 'none');

                                    	// 시작 구간이 설정 되면 마우스 이벤트 실행
                                		// last-of-type 기준으로 이벤트 걸기
                                		// ※이유 : 추가 생성 될 경우 기존 생성된 오브젝트에 영향을 주지 않기 위함
                            			waveMaskObj.mousemove(function(event) {
                            	        	var objPos = $('.over_mask').offset().left;

                            	        	mouseX = event.clientX - objPos;

                            				// 지정할 구간 width값 구하기
                            				var totalVal     = $('.player_marking_wrap:last-of-type').parent().width()
                            				var leftVal      = parseInt($(".over_line_mark").css('left').replace(/px/gi,''));
                            				var rightVal     = totalVal - mouseX;
                            				var markingWidth;
                            				// left가 right보다 클 경우 동일 연산식을 위해 스위칭
                            				if(leftVal > mouseX){
                            					$('.player_marking_wrap:last-of-type').css('left',mouseX+1)
                            					rightVal = totalVal - leftVal
                            					leftVal = Number(mouseX)+1
                            					markingWidth = totalVal - leftVal - rightVal +1;
                            				}else{
                            					$('.player_marking_wrap:last-of-type').css('left',leftVal+2)
                            					leftVal = Number(leftVal)+2
                            					markingWidth = totalVal - leftVal - rightVal;
                            				}

                            				var widthP = markingWidth/totalVal * 100
                            				var leftP = leftVal/totalVal * 100
                            				
                                        	$('.player_marking_wrap:last-of-type').css({
                                        		'background-color':'rgba(255,140,0,0.83)',
                                        		// %값으로 변환 작업
                                        		'width'  : widthP + '%',
                                        		'left'	 : leftP + '%'
                                        	}).data({"sp":leftP,"wp":widthP})

                            			});
                            			
                            			// ie9에서 마우스 무브 이벤트가 바로 먹히지 않는 현상으로 인해...
                            			// 강제로 현재 1번째 찍힌 라인기준 좌표로 마우스 무브 이벤트를 트리거 해준다..
                            			// 망할 ie 샛긔...
                            			var mouseEvent = $.Event('mousemove');
                            			mouseEvent.clientX = lineObj.offset().left;
                            			mouseEvent.clientY = 10;
                            			
                            			waveMaskObj.trigger(mouseEvent);
                            			
                            		} else if (markNo == 2) { // 두번째 표식
                            			
                            			var sp = $('.player_marking_wrap:last-of-type').data("sp")
                            			var wp = $('.player_marking_wrap:last-of-type').data("wp")
                            			$player.find('.btn_section').removeClass('ui_disable')	
                            			rc.makeSection(undefined,undefined,sp,wp)
                            		}
                            	})
                            })();
                    	}).mouseleave(function(event) { // leave 이벤트
                    		$(this).find('.over_line').remove();
                    	})
                	});
            	}
            })
            
            // 구간 지정 취소 버튼 이벤트
            $player.find('.btnMarkingClose').on("click", function() {
     			initMarkingStatus();
            });
            
            // @구간 설정 팝업
            $player.find('.main_btn_time').on("click", function() {
            	
            	if ($(this).hasClass('ui_disable'))
            		return;
            	
            	initMarkingStatus();
            	var loopTimePopup = $(".sectionSetting");
            	
            	if (loopTimePopup.length == 0){
            		$('body').append(rc.loopTimePopupHtmlString)
            		loopTimePopup = $(".sectionSetting");
            		loopTimePopup.find(".saveSection").on("click",function(){
            			makeSection();
            		});
            		
            		loopTimePopup.find(".startTime, .endTime").on("keydown",function(e) {
                    	if (e.which === 13){
                    		e.stopPropagation();
                			e.preventDefault();
                			makeSection();
                    	}
                    });
            		
            		loopTimePopup.find(".startTime, .endTime").on("keyup",function(e) {                    	
                    	var value = this.value;
                    		
                    	// 숫자 이외 걸러 내기
                    	value = value.replace(/[^0-9.]/g,'');
                    	// 소숫점. 2개이상 입력 불가 처리
                    	var result = value.match(/\./g);
                    	if (result != null && result.length > 1)
                    		value = value.replace(/\.+$/,'');
                    	
                    	this.value = value;	
                    });
            	}
            	
            	function makeSection(){
            		
            		var st = loopTimePopup.find(".startTime").val()
        			var et = loopTimePopup.find(".endTime").val()
            		
            		if(st == "" || st == undefined){
            			alert("설정하실 구간의 시작 시간을 입력 해 주세요.")
            			return;
            		}
            		if(et == "" || et == undefined){
            			alert("설정하실 구간의 종료 시간을 입력 해 주세요.")
            			return;
            		}
            		
            		if (st == et){
            			alert("시작 시간과 종료 시간은 같을 수 없습니다.")
            			return;
            		}
            		
            		rc.makeSection(st,et);
            		layer_popup_close();
            	}
            	
            	layer_popup(".loopTime")
            });

			// 권한 승인 요청 버튼
            // @may
            $player.find(".approveFile").click(function(event) {

            	var listenUrl = gridSearch.cells(gridSearch.getSelectedRowId(),gridSearch.getColumnsNum()-1).getValue()

            	var fileName = (listenUrl||"").split("/").pop().replace(/\?(.*?)/, "");
            	var fileUrl = listenUrl
            	if(fileName=="" || fileName==null){
            		alert("음성파일을 선택해주세요");
            		return false;
            	}
            	if(confirm("신청하시겠습니까?")){
            		$.ajax({
            			url:contextPath+"/approve_proc.do",
            			data:{"type":"insert",
            					"fileUrl": fileUrl,
            					"fileName": fileName},
            			type:"POST",
            			dataType:"json",
            			async: false,
            			cache: false,
            			success:function(jRes){
            				if(jRes.success == "Y") {
            					alert("신청하였습니다.");
            				}else{
                				if(jRes.resData.resultMessage=="already_approve"){
                					alert("이미 승인이 완료되었습니다.");
                				}else if(jRes.resData.resultMessage=="exist"){
                					alert("승인을 기다리는 파일입니다.");
                				}else{
                					alert("작업을 실패하였습니다.");
                				}
            				}
            			}
            		});
            	}else{
            		return false;
            	}
            });

            // @kyle 원본 파일 다운로드
            $player.find('.main_btn_filedownload').on("click", function() {
            	
            	var muteSt = new Array();
            	var muteEt = new Array();
            	var cutoffSt = new Array();
            	var cutoffEt = new Array();
            	var totalLength = $audio[0].duration
            	
            	var dataStr = {};
            	
            	var mute = false;
            	var cutoff = false;
            	var cmd="";
            	// 음소거 구간 확보
            	if ($player.find(".markingMute").length > 0){
            		$player.find(".markingMute").each(function(){
            			muteSt.push($(this).data("st"));
            			muteEt.push($(this).data("et"));            			
            		});
            		mute = true;
            	}
            	
            	// 도려낼 부분(?) 확보
            	if ($player.find(".markingCutoff").length > 0){
            		$player.find(".markingCutoff").each(function(){
            			cutoffSt.push($(this).data("st"));
            			cutoffEt.push($(this).data("et"));
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
            		}	
            	}else if(mute){
            		dataStr = {
            				url 		: 	getParameterFromUrl("url", rc.requestParam)
                		,	cmd			:  	"mute"
	            		,	mStartTime 	:	muteSt.toString()
	            		,	mEndTime 	:	muteEt.toString()  
            		}	
            	}else if(cutoff){
            		dataStr = {  
            				url 		: 	getParameterFromUrl("url", rc.requestParam)
                		,	cmd			:  	"cut_off"
            			,	cStartTime	:	cutoffSt.toString()
            			,	cEndTime	:	cutoffEt.toString()
            			,	totalLength :   totalLength
            		}
            	}else{
            		dataStr = {  
            				url 		: 	getParameterFromUrl("url", rc.requestParam)
                		,	cmd			:  	"down"
            		}
            	}	
				// 프레임에 다운로드 요청
            	if (rc.type.log)
            		top.fileDownLog(rc.recFileDate);
            	var src = "http://"+rc._RC.DEFAULT_OPTION.REQUEST_IP+":"+rc._RC.DEFAULT_OPTION.REQUEST_PORT+"/down?";
				$player.find( ".rDownloadFrame" ).attr("src",src+$.param(dataStr));
            });
            
            // @kyle 업로드 청취
            $player.find('.main_btn_fileupload').on("click", function() {
            	$player.find(".fileUpload").trigger("click");
            });	
            
            $player.find(".fileUpload").on("change", function(){
            	
            	var uploadForm = $player.find(".fileUploadForm");
            	uploadForm.attr("action","http://"+rc._RC.DEFAULT_OPTION.REQUEST_IP+":"+rc._RC.DEFAULT_OPTION.REQUEST_PORT+"/upload")
            	
            	if (this.files)
            		handleFileUpload(this.files);
            	else{
            		uploadForm.submit();
            	}
            		
            });
            
            window.addEventListener("message",function(e){
            	rc.uploadFile(e.data);
            },false);
            
            /*$player.find(".rFileUploadFrame").load(function(){
            	$(this).val();
            	rc.uploadFile();
            });*/
            
            
            
            // @kyle 수정 모드
            $player.find('.btn_list_modify').on("click", function(event) {
            	event.stopPropagation();

            	$player.find('.btn_list_del, .btn_list_exit, .listCheckBox').show();
            	$player.find('.btn_list_modify').hide();
            	// 클릭 이벤트 제거...
            	$player.find(".list_wrap ul li").unbind( "click" )
            	// 소팅 시작
            	$player.find(".list_wrap ul").sortable({
            		axis: "y",
            		disabled: false
            	});
            });

            // @kyle 삭제
            $player.find('.btn_list_del').on("click", function(event) {
            	event.stopPropagation();

            	if($(".listCheckBox:checked").length == 0)
            		alert("삭제하실 항목을 먼저 체크 해 주세요.")
            	else {
            		if($(".listCheckBox:checked").parent().hasClass("nowPlay")){
            			if(confirm("체크하신 리스트에 재생중인 리스트가 포함 되어있습니다.\n재생을 중지하시고 삭제 하시겠습니까?")){
            				rc.pause();
            				$(".listCheckBox:checked").parent().remove();
            				$player.find(".list_wrap ul li:eq(0)").addClass("nowPlay")
                			var listenUrl = $player.find(".list_wrap ul li:eq(0)").attr("listenUrl")
                			// FIXME;;임시데이터
                			//listenUrl = "HTTP://192.168.0.222:8088/F/var/REC/RecSee_Data/20170526/17/201705261719490402_1003__A.mp3"; // 암호화 않된거
                			rc.setFile("audio", listenUrl);
            			}
            		}else
            			$(".listCheckBox:checked").parent().remove()
            	}

            });

            // @kyle 수정모드 종료
            $player.find('.btn_list_exit').on("click", function(event) {
            	event.stopPropagation();

            	$player.find('.btn_list_del, .btn_list_exit, .listCheckBox').hide().removeAttr("checked");
            	$player.find('.btn_list_modify').show()
            	// 클릭 이벤트 다시 살려주기
            	//$player.find$(".list_wrap ul li").unbind( "click" );
            	$player.find(".list_wrap ul li").on("click", function() {
                	rc.playPlayList($(this).attr("listenUrl"))
                });
            	// 소팅 종료
            	$player.find(".list_wrap ul").sortable({
            		axis: "y",
            		disabled: true
            	});
            });
            
            /** 추가 시작 */ 
        	// 체크박스  변경 이벤트
            // 플레이어 인클루드가 많아서 그런지 .is(), .change() 함수가 제대로 인지 못함.. 참고하세여
            $player.find('.onReplayCheckd').on("click", function() {
            	var $target = $(this).find('.replay_check_input');
            	
        		// checked 속성이 있을 경우 속성 삭제
            	// off
        		if($target.attr('checked') == "checked") {
        			$target.removeAttr('checked');
        			$target.siblings('.replay_check_label').find('.replay_check_txt').removeClass('checked').text("OFF");
        			setCookie("hisListen",0,14)
        		// on
        		} else {
        			$target.attr('checked', 'checked');
        			$target.siblings('.replay_check_label').find('.replay_check_txt').addClass('checked').text("ON");
        			setCookie("hisListen",1,14)
        		}
            });
            /** 추가 끝 */ 
        }
        

        function initMarkingStatus() {
        	// 구간 지정 버튼 생성
        	$player.find('.btnMarking , .playBtnMarking').show();
        	// 지정 취소 버튼 삭제
        	$player.find('.btnMarkingClose, .playBtnMarkingClose').hide();
        	$player.find('.player_marking_wrap').each(function() {
        		if($(this).is('.marking_saved') == false) {
            		$(this).remove();
        		}
    		});

        	$player.find('.over_line, .over_mask').remove();
        	$player.find('.saveMarking').addClass('notAllowed');
        }

        

        function detachFile($obj) {
            $obj.attr("src", "").removeAttr("src");
            delete $obj.data()["name"];
        }

        // range 이벤트 일괄 주기: 플레이어 컨트롤러, 볼륨 컨트롤러
        setRangable($controlBar, [
            {unit: "%", target: "width", selector: ".play_value"},
            {unit: "%", target: "css", selector: ".play_location"},
        ], "controlBar", {
            afterSetValue: function(val) {
                var $target, target;
                if(rc.type[TYPES.AUDIO]) {
                  //$target = $audio;
                  target = audio;
                } else if(rc.type[TYPES.VIDEO]) {
                  //$target = $video;
                  target = video;
                } else {
                  return false;
                }
                // 시간 정보 설정
                if($controller.canPlay()) {
                    var percentPos = function(val) {
                        var max = 100;
                        var current = val;
                        return max == 0?0:current / max;
                    }(val);

                    //if(rc.fileMode != rc._RC.FILE_MODE.LOCAL) {return;}
                    setTime(percentPos * target.duration);
                    $controller.setNowTimeText(target.currentTime);

                    if(rc.type[TYPES.WAVE]) {
                        var $removeTarget = $($.grep($wave.find(".obj_key > div"), function(d){return $(d).data("pos")>=val})).find(".obj_key_value.obj_key_value_now");
                        var $addTarget = $($.grep($wave.find(".obj_key > div"), function(d){return $(d).data("pos")<val})).find(".obj_key_value:not(.obj_key_value_now)");
                        $removeTarget.removeClass("obj_key_value_now");
                        $addTarget.addClass("obj_key_value_now");
                        
                        if(rc.type[TYPES.DUAL]){
                        	var $removeTarget = $($.grep($tx_wave.find(".obj_key > div"), function(d){return $(d).data("pos")>=val})).find(".obj_key_value.obj_key_value_now");
                            var $addTarget = $($.grep($tx_wave.find(".obj_key > div"), function(d){return $(d).data("pos")<val})).find(".obj_key_value:not(.obj_key_value_now)");
                            $removeTarget.removeClass("obj_key_value_now");
                            $addTarget.addClass("obj_key_value_now");
                        }
                        
                    }
                } else {
                    $controlBar.changeValue(0);
                }
            }
        });
        // rx 볼륨
        setRangable($volume, [
          {unit: "px", max: /*$volume.find(".volume_bar").width()*/"100", target: "width", selector: ".volume_value"},
          {unit: "px", max: /*$volume.find(".volume_bar").width()*/"100", target: "css", selector: ".volume_location"},
        ], "volume", {
            afterSetValue: function(val) {
                //var target = rc.videoOnly?video:audio;
                var volume = function(val) {
                    var max = 100;
                    var current = val;
                    return max == 0?0:Math.round(current / max * 100) / 100;
                }(val);
                if(rc.type[TYPES.VIDEO]){
                	// @Kyle 익스예외
                	try{
                		video.volume = volume;
                	}catch(e){tryCatch(e)}
                }
                    
                if(rc.type[TYPES.AUDIO]){
                	// 실감 모드일 경우 실감 볼륨도 함께 조절
                	if(rc.type.realtime){
                		try{
                			OnControlsVolumeChange(volume);
                		}catch(e){tryCatch(e)}
                	}
                	// @Kyle 익스예외
                	try{
                		audio.volume = volume;
                	}catch(e){tryCatch(e)}
                }

                // 볼륨 쿠키에 저장
                document.cookie = "volume="+parseInt(volume * 100)+"; path=/; expires=" + function(date){
                    date.setDate(date.getDate() + parseInt(7, 10));
                    return date;
                }(new Date()).toGMTString() + ";";

                if(volume == 0) {
                    // TODO: 음소거 이벤트
                } else {
                    // TODO: 음소거 해제 이벤트
                }
            }
        }, 8); // 볼륨 클릭 포인트 값 보정치
        // FIXME: width가 100%이 되서 24에서 8로 바뀜 나중에 또 고쳐야될수 있음
        
        /**
         * tx 볼륨
         * 미안해.. 지금 정신으론 .........공통 함수로 못빼겠어..
         * 하하하하하ㅏ하하핳......
         * 고멘........................
         * ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ
         * */ 
        setRangable($tx_volume, [
          {unit: "px", max: /*$volume.find(".volume_bar").width()*/"100", target: "width", selector: ".volume_value"},
          {unit: "px", max: /*$volume.find(".volume_bar").width()*/"100", target: "css", selector: ".volume_location"},
        ], "volume", {
            afterSetValue: function(val) {
                //var target = rc.videoOnly?video:audio;
                var volume = function(val) {
                    var max = 100;
                    var current = val;
                    return max == 0?0:Math.round(current / max * 100) / 100;
                }(val);
                if(rc.type[TYPES.VIDEO]){
                	// @Kyle 익스예외
                	try{
                		video.volume = volume;
                	}catch(e){tryCatch(e)}
                }
                    
                if(rc.type[TYPES.AUDIO]){
                	// 실감 모드일 경우 실감 볼륨도 함께 조절
                	if(rc.type.realtime){
                		try{
                			OnControlsVolumeChange(volume);
                		}catch(e){tryCatch(e)}
                	}
                	// @Kyle 익스예외
                	try{
                		tx_audio.volume = volume;
                	}catch(e){tryCatch(e)}
                }

                // 볼륨 쿠키에 저장
                document.cookie = "volume="+parseInt(volume * 100)+"; path=/; expires=" + function(date){
                    date.setDate(date.getDate() + parseInt(7, 10));
                    return date;
                }(new Date()).toGMTString() + ";";

                if(volume == 0) {
                    // TODO: 음소거 이벤트
                } else {
                    // TODO: 음소거 해제 이벤트
                }
            }
        }, 8); // 볼륨 클릭 포인트 값 보정치
        // FIXME: width가 100%이 되서 24에서 8로 바뀜 나중에 또 고쳐야될수 있음

        // afterSetValue(function): 값 변경 후 일어날 이벤트 정의
        function setRangable($bar, changeObj, objName, event, preset) {
            preset = preset||0;
            $bar.data("changeObj", changeObj);
            $bar.data("objName", objName);
            $bar.data("preset", preset);
            var beforeMouseDown = event.beforeMouseDown;
            var afterSetValue = event.afterSetValue;
            $bar.mousedown(function(event) {
                if(typeof beforeMouseDown == "function") {
                    var skip = beforeMouseDown($bar.val());
                    if(!skip) return;
                }

                if(!rc.controlValue[objName].dragging) {
                    setBarValue(event.clientX);
                    if(typeof afterSetValue == "function") afterSetValue($bar.val());
                }
                rc.controlValue[objName].dragging = true;
            });
            $bar.bind('mouseup', function(event) {
                if(rc.controlValue[objName].dragging) {
                    setBarValue(event.clientX);
                    if(typeof afterSetValue == "function") afterSetValue($bar.val());
                    rc.controlValue[objName].dragging = false;
                }
                return true;
            }).bind('mouseleave', function(event) {
                if(rc.controlValue[objName].dragging)  rc.controlValue[objName].dragging = false;
                return true;
            });
            $bar.mousemove(function(event) {
                if(rc.controlValue[objName].dragging) {
                    setBarValue(event.clientX);

                    // TEST
                    if(typeof afterSetValue == "function") {
                        clearTimeout($bar.data("timeout"));
                        $bar.data("timeout", setTimeout(function() {
                            afterSetValue($bar.val());
                        }, 200));
                    };
                } else {
                    //
                }
            });
            // 해당 value로 이동한다
            $bar.moveTo = function(val) {
              if(typeof afterSetValue == "function") afterSetValue(val);
              return $bar;
            }

            // bar 값을 변경한다.
            $bar.changeValue = function(val) {
                val = parseFloat(val, 10);
                if(isNaN(val)) {
                    throw new ReferenceError("value is not valid.");
                    return false;
                }
                val = val < 0?0: (val> 100? 100: val);
                $bar.val(val);
                $.each(changeObj, function(i, obj) {
                    var u = obj.unit;
                    var t = obj.target;
                    var s = obj.selector;
                    var m = obj.margin;
                    var newVal = function(v) {
                        v = v + (m||0);
                        var max = obj.max;
                        return !max?v:(v / 100 * max);
                    }(val);
                    if(t == "width") {
                        $bar.find(s)[t](newVal + u);
                    } else if(t == "css") {
                        $bar.find(s)[t]("left", newVal + u);
                    }
                });
                return $bar;
            }

            // bar 값을 x좌표에 따라 설정한다.
            function setBarValue(clientX) {
                // 값 보정치 적용
                var p = preset;

                // 플레이어의 왼쪽 마진 px
                var mLeft = $bar.offset().left + p;
                // 마우스의 현 x위치
                var cPosX = clientX;
                // 마우스의 컨트롤러 내 상대적인 x위치
                var rPosX = cPosX - mLeft;
                // 상대적인 x의 %위치
                var rPerX = rPosX/$bar.width() * 100;
                // 맥시멈, 미니멈
                $bar.changeValue(rPerX);
                return rPerX;
            }
        }

        function setTime(time) {
            syncDo("currentTime", time)
        }

        // player들 특정 값들을 일괄적으로 설정
        function syncDo(something, value) {
            var TYPE = rc._RC.TYPE;
            if(value === undefined) {
                if(rc.type[TYPE.AUDIO]) {
	                try{ 
	                	audio[something]();
	                	if(something == "play" || something == "pause"){
	                		$controller.changeButtonState(something);	
	                	}
	                	
	                	if(rc.type.dual){
	                		tx_audio[something]();
	                	}
	                	
	                } catch(e) {tryCatch(e)}
                }
	            if(rc.type[TYPE.VIDEO]) {
	                try{ 
	                	video[something]();
	                	if(something == "play" || something == "pause"){
	                		$controller.changeButtonState(something);
	                	}
	                	//video.playbackRate = (getCookie("playBack") ? getCookie("playBack") : 1);
	                	video.playbackRate = 1;
	                } catch(e) {tryCatch(e)}
	            }
            } else {
                if(rc.type[TYPE.AUDIO]) {
                	
                	audio[something] = value;   
                	if(rc.type[TYPE.DUAL]){
                		tx_audio[something] = value;
                	}
                }
                if(rc.type[TYPE.VIDEO]) {
                    video[something] = value;
                }
                if(something == "play" || something == "pause"){
                	$controller.changeButtonState(something);
                }
            }
        }
        
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
        
        function getParameterFromUrl(name, url){
        	var result = new RegExp('[\?&]' + name+ '=([^&#]*)').exec( url )
        	if (result == null)
        		return null;
        	else 
        		return decodeURI(result[1]) || 0;
        }
        
        function setCookie(cName, cValue, cDay){
        	var expire = new Date();
        	expire.setDate(expire.getDate() + cDay);
        	var cookies = cName + "=" + escape(cValue) + "; path =/ ";
        	if (typeof cDay != undefined){
        		cookies += ";expires=" + expire.toGMTString() + ";";
        	}
        	document.cookie = cookies;
        }

        function getCookie(cName){
        	cName = cName + "=";
        	var cookieData = document.cookie;
        	var start = cookieData.indexOf(cName);
        	var cValue = "";
        	if(start != -1){
        		start += cName.length;
        		var end = cookieData.indexOf(";", start);
        		if(end == -1)
        			end = cookieData.length;
        		cValue = cookieData.substring(start,end);
        	}
        	return unescape(cValue);
        }
        
        $audio.init();
	}
    // 각 HTML 코드 작성해두는곳
    this.HTML = (function() {
        var html, wave, player;
        $.ajax({
            url: baseSrc + "recsee_player.ui.html",
            dataType: "html",
            async: false,
            cache: false,
            success: function(res) {
                html = res;
                var body = $(html).closest("#recsee_player_object");
                wave = $(body).find(".obj_key:eq(0)").get(0).outerHTML.trim();
                player = (function() {
                    var msg = $(body).find(".rplayer:eq(0)").get(0).outerHTML.trim();
                    // 유동적 데이터(data-dynamic 속성이 있는) 값 지우기
                    var $msg = $(msg).find("[data-dynamic]").remove().end();
                    // FIXME: 경로 임시처리......
                    var text = $msg.get(0).outerHTML.trim().replace(/src="images/, 'src="'+baseSrc+'images');
                    return text;
                }());
            }
        });
        return {
            wave: wave,
            player: player,
        }
    }());

}
RecseePlayer.prototype.setDefault();

function RecseePlayer(o) {
    this.init(o);

    return this;
}

Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|hh|mm|ss|ms)/gi, function($1) {
    	
    	var h = d.getHours();
    	
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "ms": return d.getMilliseconds().zf(3);
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

// TimeLog 출력
/*console.timelog = function() {
	var a = Array.prototype.slice.call(arguments);
	a.splice(0, 0, new Date().toLocaleString() + ": ")
	console.log.apply(this, a);
}*/


function tryCatch(e){
	var a=null
}

function phoneFomatter(num,type) {
	var formatNum = '';
	if(num.length==11){
		if(type=='masking'){
			formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-****-$3');
		}else{
			formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
		}
	}else if(num.length==8){
		if(type=='masking'){
			formatNum = num.replace(/(\d{4})(\d{4})/, '$1-****');
		}else{
			formatNum = num.replace(/(\d{4})(\d{4})/, '$1-$2');
		}
	}else if(num.length<8){
		if(type=='masking'){
			formatNum = num.replace(/[0-9]/g, '*');
		}else{
			formatNum = num;
		}
	}else{
		if(num.indexOf('02')==0){
			if(type=='masking'){
				formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-****-$3');
			}else{
				formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
			}
		}else{
			if(type=='masking'){
				formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-****-$3');
			}else{
				formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
			}
		}
	}
	return formatNum;
}
