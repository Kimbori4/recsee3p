/*************전역 설정*******************/
var getUserInfo = {}; 											// 전역 변수 ; 유저 정보 가져와서 담을 그릇 [키값은 내선번호]
var getAgentSeatInfo = {}; 									// 전역 변수 ; 상담원 자리 배정 담을 그릇 [키값은 내선번호]
var realtime_rc;													//플레이어
var roomSelectCode = [];										//분류 값 코드
var selectCode = "";												//분류 선택 값
var monitoringInterval;											//레디스 ajax
var userLength;													//인원수
var userQue;														//유저 큐
var roomQue;														//방 큐
var roomAdd;														//room Interval
var userAdd;														//user Interval
var selectArr = [];				//조회 선택값
var userCodeArr = [];
var userCodeObj = {};											//USERCODE object
var roomName = {};											// DB 에 있는 방 이름
var ROOMDRAWTRUE = true;								//방 그렷는지 체크
var ROOMBOOLEAN = true;									//방 있는지 체크
var DBROOMCHECK = true;

var progressStart = false;										//progress 때문에 만듬

var listenUser={};					//	실감 지속 감청 팝업이 없어도 들을수 잇게 설정하기 위하여....

var alertChk = false;					//조회 알림창 나타 낫었는지 확인 값

/**********성능 개선 전역 ***********/
var roomViewArr ="";
var groupSeatNumber = 0;			// 좌석 찾아갈려고 만든 변수
var bgLocation = {};				//위치
var mgLocation = {};			//위치
var sgLocation={};				//위치
/**********성능 개선 전역 ***********/

$(function() {
	$('#officeMonitoringWindow').click(function() {
		/*
		 * var popWidth = screen.width - 100 var popHeight = screen.height - 100
		 */
		try{
			clearInterval(monitoringInterval);
			clearInterval(roomAdd);
			clearInterval(userAdd);
			$('.main_contents_office').remove();
			$('.monitoring_legend').remove();
			$('.listenAlwaysPannel').remove();
			$('.monitoring_option').remove();
		}catch(e){tryCatch(e)}
		var targetPath = webPath+"/monitoring/office_monitoring"
		window.open(targetPath, "_blank", "resizable=yes, toolbar=no,location=no, width=1500, height=700,scrollbars=yes ,left=50,top=50'");

	})

	$('#custPhoneContinue').keyup(function(e){
		var keyCode = e.keyCode;

		if($('#statusSave').is(":checked") == true){
			setCookie('saveValue',$(this).val(), 30);
		}
	})

	// 옵션 체크 오브젝트 삽입
	$('.onoffswitch-label').append('<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">on</span>');

	//내선 번호 체크 함수
	$(".office_stay_check").change(function(){
		if( $(this).is(":checked") )
			$('.custPhoneContinueChk').prop("checked",false);
	})

	//고객 번호 체크 함수
 	$('.custPhoneContinueChk').change(function(){
 		if($('.office_stay_check').is(":checked")== true)
 			$('.office_stay_check').prop("checked",false)
 	})

    //스크롤 시작 하면 멈추고
    $(document).scroll(function(){
    	clearInterval(monitoringInterval);
    })

    //스크롤 끝나면 다시 레디스 받기
    $(document).scrollStopped(function(event){
    	startRedisSocket();
    })

    // SAVE_____MEMO
    $('#realTimeMemoAdd').on("click",function(){
    	if(RealTimeExt == "")
    		return false;
    	var savedMemo = $("#realTimeMemoContents").text().replace(/<br\s?\/?>/gi,"\n");
    	var proc = $("#proc").val();
    	// update or insert
    	var dataStr = {
				"recTime" 	: defineStime
			,	"extNum" 	: RealTimeExt
			,	"memo" 		: savedMemo
			,	"tag"		: savedMemo
			,	"memoType"  : "T"
			,	"proc" 		: proc
			,	"type"        : "real"
			,	"memoInTime" : realtime_rc.obj.player.find(".procTime").html()
		}

    	$.ajax({
			url: contextPath+"/recMemoProc.do",
			data: dataStr,
			type: "POST",
			dataType: "json",
			cache: false,
			async: false,
			success: function(jRes) {
				alertText("메모 저장","메모 저장이 완료 되었습니다.");
				$("#realTimeMemoContents").text('');
				layer_popup_close();
			}
		});
    })

	// 체크박스 공통 변경 이벤트
    $('.onoffswitch-checkbox').change(function(){
		// 체크박스 CSS 등 설정값 부분
		var chkObj = $('.onoffswitch-checkbox');
		var chkObjAttr = $(this).find(chkObj);
		// checked 속성이 있을 경우 속성 삭제
		if($(this).is(':checked') == false) {
			$(this).removeAttr('checked');
			$(this).parent().find('.onoffswitch-switch').text('off');
		}
		// checked 속성이 없을 경우 속성 추가
		if($(this).is(':checked') == true) {
			$(this).attr('checked', true);
			$(this).parent().find('.onoffswitch-switch').text('on');
		}
	});

	ui_controller();
});
/*
 * LOAD EVENT
 */
addLoadEvent(monitoringLoad);

function monitoringLoad(){
	onLoad();
	select2InputFilter();
}

function onLoad(){

	//모바일인 경우 내선번호 검색 숨기기
	if(recsee_mobile == "Y"){
		$("select option[value*='searchAgentExt']").hide();
	}
	try{
		// 사용자 정보 담아 놓기
		getRuserInfo();
	}catch(e){tryCatch(e)}


	dhx4.ajax.get(contextPath+"/monitoring/realtime_init.json", function( response ){
	    var realTime = jQuery.parseJSON(response.xmlDoc.responseText);
	    if(realTime.system.length > 0) {
	        systemPath = realTime.system;
	        for(var i = 0; i < realTime.system.length; i++) {
	            $("#systemCode").append('<option ip='+realTime.system[i].system_ip+' value=' + realTime.system[i].system_code + (i==0 ? ' selected="selected"' :'') +' >' + realTime.system[i].system_name + '</option>');
	            if(i == 0) {
	                monitoringIP = realTime.system[i].system_ip;
	                systemName = realTime.system[i].system_name;
	                key = realTime.system[i].system_key;
	                $(".now_system_info").text(systemName)
	            }
	        }
	    }
		if(realTime.realtime.length > 0) {
			for(var i = 0; i < realTime.realtime.length; i++) {
			    for(objItem in realTime.realtime[i]) {
			        switch(objItem) {
			        case "login" :
			        case "ready" :
			        case "calling" :
			        case "acw" :
			        case "logout" :
			            if(realTime.realtime[i][objItem]=="1")
			                $('.swraper[input-name=user_status_'+objItem+']').trigger('click');
			            break;
			        case "img" :
			        case "userId" :
			        case "calltime" :
			        case "phoneNo" :
			            if(realTime.realtime[i][objItem]=="1")
			                $('.swraper[input-name=info_'+objItem+']').trigger('click');
			            break;
			        }
			    }
			}
		}
	});

	// 하단 고정 플레이어 로드
	realtime_rc = new RecseePlayer({
        target: "#bottomFixedPlayer"
        ,	"btnDownFile" 		: false				// 전체파일 다운로드 버튼 사용 여부 (다운로드
        														// 시 암호화 된 파일 다운로드)
		,	"btnUpFile" 		: false				// 파일 업로드 청취 버튼 사용 여부
													// (플레이어에서 다운로드 한 파일만 청취 가능)
		,	"btnPlaySection"	: false			// 재생 구간 설정 버튼 사용 여부
		,	"btnTimeSection"	: false			// 사용자 정의 구간 설정 버튼 사용 여부
		,	"btnMouseSection"	: false				// 마우스로 구간 설정 버튼 사용 여부
		,	"wave"   			: true				// 웨이브 표출여부 (비 활성시 구간 지정 기능
													// 사용 불가)
		,	"btnDown"			: false				// 구간 설정 시 구간에 대한 다운로드 기능
													// (다운로드 시 암호화 된 파일 다운로드)
		,	"btnMute"			: false			// 구간 설정 시 묵음처리 기능
		,	"btnDel" 			: false				// 구간 설정 시 제외시키기 기능
		,	"moveTime"			: 5					// 플레이어 좌우 이동시 증감할 시간; 기본 5초
		,	"list"				: false				// 플레이 리스트 사용 유무
		,	"dual"				: false 			// 화자분리 플레이어 사용 유무
		,	"memo"				: false				// 메모 사용 유무
		,	"requestIp"			: $("#ip").val()	// 통신 IP
		,	"requestPort"		: $("#port").val()	// 통신 Port
		,	"log"				: true				// 다운로드 로그 사용 유무
		,	"replay"	: false
		,    audio: true
		,    video: false
		,    realtime: true
        // wave: (param.mode == "a" || param.mode == "wave")
    });
	$("#bottomFixedPlayer").draggable({handle: ".player_wave.audioWave"})

	// 대분류
	selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true,"callCenter");

	// 대분류 변화시 중분류 로드
	$( "#mBgCode" ).change(function() {
		$("#mSgCode").empty().val(null).trigger("change");
		selectOrganizationLoad($("#mMgCode"), "mgCode", $(this).val(),undefined,undefined,true)
		// 중분류 및의 소분류가 있다면 디폴트로 로드
	});

	// 중분류 변화시 소분류 로드
	$( "#mMgCode" ).change(function() {
		selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),undefined,true)
	});

	//	권한에 따른 초기 필터 설정
	searchFilterLoad();

	// 분류 코드 필터링
	$( "#codeFilter" ).change(function() {
		$("#mSgCode, #mMgCode").empty()
		$(".select2").remove();
		$(".codeList").hide().removeAttr("multiple").removeClass("select2-hidden-accessible");
		var selectedCode = $( "#codeFilter :selected" ).val()
		switch(selectedCode){
		case "mBgCode":
			$("#mBgCode").show().val("")
			groupSearch(lang.monitoring.alert.msg4/*"대상 그룹을 선택 해 주세요"*/);
			break;
		case "mMgCode":
			$("#mBgCode, #mMgCode").show().val("")
			groupSearch(lang.monitoring.alert.msg4/*"대상 그룹을 선택 해 주세요"*/);
			break;
		case "mSgCode":
			$("#mBgCode, #mMgCode,#mSgCode").show().val("")
			$('#mMgCode option').remove();
			groupSearch(lang.monitoring.alert.msg4/*"대상 그룹을 선택 해 주세요"*/);
			break;
		case "searchAgentName":
			groupSearch(lang.monitoring.alert.msg5/*"상담사명을 입력해주세요"*/);
			selectOrganizationLoad($("#searchAgentName"), "agentName",undefined,undefined,undefined,true)
			break;
		case "searchAgentNum":
			groupSearch(lang.monitoring.alert.msg6/*"사원번호를 입력해주세요"*/);
			selectOrganizationLoad($("#searchAgentNum"), "agentNum",undefined,undefined,undefined,true)
			break;
		case "searchAgentExt":
			groupSearch(lang.monitoring.alert.msg7/*"내선번호를 입력해주세요"*/);
			selectOrganizationLoad($("#searchAgentExt"), "agentExt",undefined,undefined,undefined,true)
			break;

		}
		function groupSearch(txt){
			$("#"+selectedCode).attr("multiple","multiple").select2({
					placeholder: txt
				,	allowClear: true
				,   dropdownAutoWidth : true
				,	width : 'auto'
			});
		}
	});
	$( "#codeFilter" ).trigger("change");
	// 상태활성화 체크박스 변경 이벤트
	$('.onoffswitch-checkbox[name=selStatus]').change(function(){
	   // 아이디의 뒷부분을 소문자로 얻음
	   var targetClass = $(this).attr("id").replace(/^selStatus(.*?)$/, function(){return arguments[1].toLowerCase()});
	   // 처리할 데이터 얻기
	   var $targetOffice = $(".obj_" + targetClass).parent();
	   // 처리하기
	   if($(this).is(':checked')) {
		   $targetOffice.show();
	   } else {
		   $targetOffice.hide();
	   }
	});

	//지속 감청 부분
	//모니터링 지속 감청 레이어
	var $listenAlwaysPannel = $('.listenAlwaysPannel');

	// 모니터링 셋팅 레이어
	var $monitoringSettingPannel = $('.monitoring_option');


	//지속 감청 레이어 열기 이벤트
    $("#btnListenAlway").click(function(){
    	$monitoringSettingPannel.hide("slide", { direction: "right" }, "slow");
    	$listenAlwaysPannel.show("slide", { direction: "right" }, "slow");
    })
	// 지속감청 닫기 이벤트
	$('#btnListenAlwayExit').click(function() {
		$listenAlwaysPannel.hide("slide", { direction: "right" }, "slow");
	})


	// 모니터링 셋팅 열기 이벤트
	$('#btnMonitSetOpen').click(function() {
		$listenAlwaysPannel.hide("slide", { direction: "right" }, "slow");
		$monitoringSettingPannel.show("slide", { direction: "right" }, "slow");
	})
	// 모니터링 셋팅 닫기 이벤트
	$('#btnMonitSetExit').click(function() {
		$monitoringSettingPannel.hide("slide", { direction: "right" }, "slow");
	})
	// 모니터링 옵션 드랍다운 이벤트
//	$('.setting_dropdown_btn').click(function() {
//		var $dropdownTarget = $(this).parent().next('.setting_dropdown_cont');
//		$dropdownTarget.slideToggle("slow");
//	})

	// 조회 버튼 클릭 시
	$("#mSearchBtn").click(function(){
		alertChk = false;
		progressStart = false;										//progressbar 초기화
		//좌석 정보 구하기
		getAgentSeat();
		userQue = new Queue();
		roomQue = new Queue();
		var selectedCode = $( "#codeFilter :selected" ).val()
		var data = $('#'+selectedCode).select2('data');

		if(data.length == 0){
			clearInterval(monitoringInterval);
			clearInterval(roomAdd);
			clearInterval(userAdd);
			alertText(lang.monitoring.alert.msg1/*"조회"*/,lang.monitoring.alert.msg2/*"검색 조건을 입력해 주세요."*/);
			return false;
		}

		ROOMBOOLEAN = true;
		ROOMDRAWTRUE = true;
		userCodeObj = {};
		roomName = {};
		$(".office_obj_wrap").html("");

		if( data.length > 0 ){
    		var selectorString = new Array();
    		var selectedValues = new Array();

			for(var i = 0 ; i < data.length ; i++){
				switch(selectedCode){
					case "mBgCode":
						selectorString.push('[bg-code="'+data[i].id+'"]');
						selectedValues.push(data[i].id);
						selectCode = "BG";
						break;
					case "mMgCode":
						selectorString.push('[mg-code="'+data[i].id+'"]');
						selectedValues.push($('#mBgCode').val()+ "|" + data[i].id);
						selectCode = "MG";
						break;
					case "mSgCode":
						selectorString.push('[sg-code="'+data[i].id+'"]');
						selectedValues.push($('#mBgCode').val()+ "|" + $('#mMgCode').val()+ "|" + data[i].id);
						selectCode = "SG";
					break;
					case "searchAgentName":
						selectorString.push('[name="'+data[i].id+'"]');
						selectedValues.push(data[i].id);
						selectCode = "NAME";
						break;
					case "searchAgentNum":
						selectorString.push('[num="'+data[i].id+'"]');
						selectedValues.push(data[i].id);
						selectCode = "NUM";
						break;
					case "searchAgentExt":
						selectorString.push('[ext="'+data[i].id+'"]');
						selectedValues.push(data[i].id);
						selectCode = "EXT";
						break;
				}
			}
		}else{
			var selectorString = new Array();
    		if($('#mBgCode').val() != null){
    			selectCode = "BG";
    			selectorString.push('[bgcode="'+$('#mBgCode').val()+'"]');
    		}
    		if($('#mMgCode').val() != null){
    			selectCode = "MG";
    			selectorString.push('[mgcode="'+$('#mMgCode').val()+'"]');
    		}
		}
/*		roomViewArr = "";
		$(".office_obj_wrap").html("");
		roomSelectCode = selectorString;*/
    	selectArr = selectedValues;
		roomSelectCode = selectorString;
		repeat();
		startRedisSocket();			// 레디스
	});
}

/*************레디스 ************* */
function startRedisSocket(){
	clearInterval(monitoringInterval);
	clearInterval(roomAdd);
	clearInterval(userAdd);
	MONITORING_TIME = 20;

	monitoringInterval = window.setInterval(function(){repeat()}, MONITORING_TIME * 1000);

	roomAdd = window.setInterval(function(){
		 if(roomQue.front()==undefined){
			 roomQue.dequeue();
			 return;
		 }
		 if(ROOMDRAWTRUE){
			 onRoomInfoAdd(roomQue.front());
			 ROOMDRAWTRUE = false;
		 }

		 roomQue.dequeue();
	},1);

	userAdd = window.setInterval(function(){
		 if(userQue.front()==undefined){
			 userQue.dequeue();
			 return;
		 }
		 if($("[office-ext='"+userQue.front().EXT+"']").length==0){
			 onUserInfoAdd(userQue.front());
		 }
		 userQue.dequeue();
	 },1);
}

/*
 * db에서 사용자 정보 긁어와서 getUserInfo에 ext를 키값으로 하는 obj 생성
 * 실감페이지에서 사용자 정보 매칭 할때 사용!
 */
function getRuserInfo(){
	$.ajax({
		url:contextPath+"/monitoring/selectRuserInfo.do",
		data:{
			monitoring : "true"
		},
		type:"POST",
		dataType:"json",
		sync: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				var userList = jRes.resData.rUserList;
				getUserInfo = {};
				for(var i = 0 ; i < userList.length; i++ ){
					if(userList[i].rUseYn == "Y")
						getUserInfo[userList[i].extNo] = userList[i]
				}
			}
		}
	});
}


function repeat() {
	$.ajax({
		url:contextPath+"/monitoring/officeMonitoringSelect.do",
		data:{
			"SYSID" : $('#systemCode option:selected')[0].getAttribute('ip')
		,	"selectCode" : selectCode
		,	"selectArr" : selectArr.toString()
		},
		type:"POST",
		dataType:"json",
		beforeSend: function(){
			if(!progressStart){
				//$('.office_obj_wrap').addClass("realtime_progress");
			}
		},
		complete : function(){
			if(!progressStart){
				//$('.office_obj_wrap').removeClass("realtime_progress");
				progressStart = true;
			}
		},
		success:function(jRes){
			if(jRes.success == "Y") {
				var searchMonitoring = [];

				var responseData = jRes.resData.agentExt;

				var Code = [];
				if(roomSelectCode.length>0){
					for(var i=0;i<roomSelectCode.length;i++){
						var k = roomSelectCode[i].toString().split("\"")
						Code.push(k[1]);
					}
				}

				for(var i=0;i<responseData.length;i++){
					if(Code.length>0){
						for(var j=0;j<Code.length;j++){
							switch(selectCode){
								case "BG":
									if(responseData[i].BGCODE ==Code[j])
										searchMonitoring.push(responseData[i]);
								break;
								case "MG":
									if(responseData[i].MGCODE ==Code[j])
										searchMonitoring.push(responseData[i]);
								break;
								case "SG":
									if(responseData[i].SGCODE ==Code[j])
										searchMonitoring.push(responseData[i]);
								break;
								case "NAME":
									if(responseData[i].AGENTNAME==Code[j])
										searchMonitoring.push(responseData[i]);
								break;
								case "NUM":
									if(responseData[i].AGENTID==Code[j])
										searchMonitoring.push(responseData[i]);
								break;
								case "EXT":
									if(responseData[i].EXT==Code[j])
										searchMonitoring.push(responseData[i]);
								break;
							}
							
							//	지속 감청 유저 안보일때도 듣기 위해서 넣어줌
							if(accessLevel != "A") {
								if(accessLevel == "B") {
									if(responseData[i].BGCODE == userInfoJson.bgCode)
										listenUser[responseData[i].EXT] = responseData[i];
								}
								if(accessLevel == "M") {
									if(responseData[i].MGCODE == userInfoJson.mgCode)
										listenUser[responseData[i].EXT] = responseData[i];
								}
								if(accessLevel == "S") {
									if(responseData[i].SGCODE == userInfoJson.sgCode)
										listenUser[responseData[i].EXT] = responseData[i];
								}
							}else{
								listenUser[responseData[i].EXT] = responseData[i];
							}
						}
					}else{
						if(getUserInfo[responseData[i].EXT] != undefined)
							searchMonitoring.push(responseData[i]);
					}
				}
				
				//출력
				//	조회 없을 시 알림창
				if(searchMonitoring.length ==0 && alertChk == false){
					alertChk = true;
					$('.alert_contents').html(lang.monitoring.alert.msg3);/*"조회된 값이 없습니다."*/
					$('#holdon-overlay2').removeClass('displaynone');
					$('#holdon-overlay2').addClass('displaytable');
					setTimeout(function(){
						$('#holdon-overlay2').removeClass('displaytable');
						$('#holdon-overlay2').addClass('displaynone');
					},2000)
				}

	        onMessageMonitoring(searchMonitoring);

					//내선번호 보이지 않아도 감청 되게 하기
					if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && playState == false){
						if (listenUser[$('#custPhoneContinue').val()] != undefined && listenUser[$('#custPhoneContinue').val()].RTP == "1" && listenUser[$('#custPhoneContinue').val()].CTI == "CALLING" ) {
							
							setTimeout(function(){
								listenEvent(listenUser[$('#custPhoneContinue').val()].EXT, listenUser[$('#custPhoneContinue').val()].CUSTNUM,listenUser[$('#custPhoneContinue').val()].STIME, listenUser[$('#custPhoneContinue').val()].SERVERIP);	
							},500)

							if($('.listen-away').length <= 0){
								$('#bottomFixedPlayer').after('<div class="listen-away"><marquee></marquee></div>');
								$('.listen-away marquee').text('현재 상담사 ['+listenUser[$('#custPhoneContinue').val()].AGENTNAME+'] 내선번호 ['+listenUser[$('#custPhoneContinue').val()].EXT+'] 지속 감청 중입니다.');
							}else{
								$('.listen-away').remove();
								$('#bottomFixedPlayer').after('<div class="listen-away"><marquee></marquee></div>');
								$('.listen-away marquee').text('현재 상담사 ['+listenUser[$('#custPhoneContinue').val()].AGENTNAME+'] 내선번호 ['+listenUser[$('#custPhoneContinue').val()].EXT+'] 지속 감청 중입니다.');
							}

							defineUserId =  listenUser[$('#custPhoneContinue').val()].AGENTID;
							defineCustNum = listenUser[$('#custPhoneContinue').val()].CUSTNUM;
							defineUserName = listenUser[$('#custPhoneContinue').val()].AGENTNAME;
							return;
						}
					}/*else if ($('.office_stay_check').is(":checked") == false && playState == true ){
						$('.listen-away').remove();
						//	재생 중지
						try{
							realtime_rc.obj.audio[0].pause();
							realtime_rc.obj.audio[0].currentTime= 0;
							document.getElementById('realtimePlayer').getElementsByTagName('audio')[0].src="";
					    	$("#"+RealTimeExt+" > div").find('.agent_status_active').remove();
							console.log("/RECSEE/"+RealTimeExt+"/STOP/IE/");
							SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
							SocketClient.Socket.close();
						}catch(e){
							tryCatch(e)
						}
						RealTimeExt ="";
					}*/
			}
		}
	})
    return true;
}

/*
 * db에서 사용자 정보 긁어와서 getAgent에 ext를 키값으로 하는 obj 생성
 * 오피스 모니터링 자리
 */
function getAgentSeat(){
	$.ajax({
		url: contextPath+"/monitoring/officeMonitoringSelectAgent.do",
		data: {},
		type: "POST",
		dataType: "json",
		async: false,
		success: function(jRes) {
			var agent = jRes.resData.agent;
			for(var i=0;i<agent.length;i++){
				getAgentSeatInfo[agent[i].rExtNo] = jRes.resData.agent[i];
			}

		}
	});
}


//스크롤 정지 함수
$.fn.scrollStopped = function(callback){
	var that = this, $this = $(this);
	$this.scroll(function(ev){
		clearTimeout($this.data('scrollTimeout'));
		$this.data('scrollTimeout',setTimeout(callback.bind(that),250,ev));
	});
};

/*
 * 오피스 모니터링 동작
 */
function onMessageMonitoring(items){
	if(items.length > 0) {
		userLength = items.length;
		//방 먼저 그리기
		if(ROOMBOOLEAN){
			var codeVal = $('#codeFilter').val().substring(1,$('#codeFilter').val().length);
			codeVal = codeVal.toUpperCase();					//분류값 들고오기
			var codeName = "";
			var selectedCode = $( "#codeFilter :selected" ).val()
			var codeSelectBox = $('#'+selectedCode).select2('data');
			var TempStr = [];

			//DB에서 방 있으면 가지고 오기
			$.ajax({
				url: contextPath+"/monitoring/officeMonitoringSelectRoom.do",
				data: {
					code : codeVal
				},
				type: "POST",
				dataType: "json",
				async: false,
				success: function(jRes) {

					var roomCode = jRes.resData.roomCode;
					var roomSeePerson = jRes.resData.roomSeePerson;
					var roomInfo = jRes.resData.roomInfo;

					if(roomCode.length == 0)
						DBROOMCHECK = false;
					for(var i=0;i<roomCode.length;i++){
						if(roomCode[i] != "" && roomCode[i] != null){
							//select box 선택한 경우
							if(codeSelectBox.length>0){
								for(var j=0;j<codeSelectBox.length;j++){
									//console.log(codeSelectBox[j].id+"    "+ roomCode[i].split("-")[0]);
									if(codeSelectBox[j].id == roomCode[i].split("-")[0]){
										var r = roomCode[i];
										TempStr[i] = r;
										userCodeObj[r] = roomSeePerson[i];
										roomName[r] = roomInfo[i];

										bgLocation[r] = jRes.resData.roomBgLocation[i];
										mgLocation[r] = jRes.resData.roomMgLocation[i];
										sgLocation[r] = jRes.resData.roomSgLocation[i];
									}
								}
							}else{
								var r = roomCode[i];
								TempStr[i] = r;
								userCodeObj[r] = roomSeePerson[i];
								roomName[r] = roomInfo[i];
								bgLocation[r] = jRes.resData.roomBgLocation[i];
								mgLocation[r] = jRes.resData.roomMgLocation[i];
								sgLocation[r] = jRes.resData.roomSgLocation[i];
							}
						}
					}
				}
			});

			for(var i = 0; i < items.length; i++) {
				//console.log(items[i])
				switch(codeVal){
					case "BGCODE":
						codeName = items[i].BGCODE
					break;
					case "MGCODE":
						//codeName = items[i].MGCODE
						codeName = items[i].BGCODE+"_"+items[i].MGCODE
					break;
					case "SGCODE":
						//codeName = items[i].SGCODE
						codeName = items[i].BGCODE+"_"+items[i].MGCODE+"_"+items[i].SGCODE
					break;
					case "EARCHAGENTNAME":
						codeName = items[i].SGCODE
					break;
					case "EARCHAGENTNUM":
						codeName = items[i].SGCODE
					break;
					case "EARCHAGENTEXT":
						codeName = items[i].SGCODE
					break;
				}
				//배열에 넣어주고 중복값 제거하기
				if(userCodeObj[codeName] == null)
					userCodeObj[codeName] = 0;
				userCodeArr.push(codeName);
				userCodeArr = arrayUniq(userCodeArr);
				for(var j=0;j<userCodeArr.length;j++){

//					console.log(codeName +"   "+ TempStr[j])
//					console.log(codeName +"   "+ userCodeArr[j])
//					if(TempStr.length>0){
//						if(TempStr[j]!=undefined || codeName != TempStr[j]){
//							break;
//						}
//					}

					if(codeName ==userCodeArr[j] && TempStr[j] ==undefined){
						userCodeObj[codeName]++;
					}
				}

				var userExt = items[i].EXT;
				var bgCode = (getUserInfo[userExt]) ? getUserInfo[userExt].bgCode : "emptyBg";
	    		var mgCode = (getUserInfo[userExt]) ? getUserInfo[userExt].mgCode : "emptyMg";
				var sgCode = (getUserInfo[userExt]) ? getUserInfo[userExt].sgCode : "emptySg";

				if(accessLevel != "A") {
					if(accessLevel == "B") {
						if (items[i].BGCODE != userInfoJson.bgCode){
							delete userCodeObj[items[i][codeVal]];
							roomQue.enqueue(userCodeObj);
						}else{
							roomQue.enqueue(userCodeObj);
							userQue.enqueue(items[i]);
						}
					}
					if(accessLevel == "M") {
						if (items[i].MGCODE != userInfoJson.mgCode){
							delete userCodeObj[items[i][codeVal]];
							roomQue.enqueue(userCodeObj);
						}else{
							roomQue.enqueue(userCodeObj);
							userQue.enqueue(items[i]);
						}
					}
					if(accessLevel == "S") {
						if (items[i].SGCODE != userInfoJson.sgCode){
							delete userCodeObj[items[i][codeVal]];
							roomQue.enqueue(userCodeObj);
						}else{
							roomQue.enqueue(userCodeObj);
							userQue.enqueue(items[i]);
						}
					}
					if(accessLevel == "U"){
						if (items[i].AGENTID != userInfoJson.userId){
							delete userCodeObj[items[i][codeVal]];
							roomQue.enqueue(userCodeObj);
						}else{
							roomQue.enqueue(userCodeObj);
							userQue.enqueue(items[i]);
						}
					}
				}else{
					roomQue.enqueue(userCodeObj);
					userQue.enqueue(items[i]);
				}
			}
			ROOMBOOLEAN =false;
		}else{
			for(var i = 0; i < items.length; i++) {
				var extNo = items[i].EXT;
				//cti미사용이면 ringing일때 콜링으로 바꿔주기..이게 말이냐
				if("N"==ctiUse){
					if(items[i].CTI.toLowerCase()=="ringing"){
						items[i].CTI = "CALLING";
					}
				}
				
				if($("[office-ext='"+extNo+"']").length > 0){
					if(($("[office-ext='"+extNo+"']").attr('class').replace("agent_status obj_","").indexOf(items[i].CTI.toLowerCase())) < 0
							|| (items[i].RTP == "1" &&items[i].CTI.toLowerCase() == "calling" && $("[office-ext='"+extNo+"']").find('.rtp-not').hasClass('office_slash_status'))){
						 onUserInfoChange(items[i]);
					}
				}
			}
		}
	}
}
//오피스 모니터링 방 그리기 함수
function onRoomInfoAdd(roomInfo){
	for(var i=0;i<Object.keys(roomInfo).length;i++){
		if(Object.keys(roomInfo)[i] == ""){
			delete roomInfo[Object.keys(roomInfo)[i]];
		}
		setTimeout(drawRoom(Object.keys(roomInfo)[i],roomInfo[Object.keys(roomInfo)[i]],i),25);
		//	룸 드래그 시작
		 //dragRoom();
	}
}
var kfer = 0;
function drawRoom(code,user,index){
	kfer++;
	var codeVal = $('#codeFilter').val().substring(1,2).toLowerCase()+$('#codeFilter').val().substring(2,$('#codeFilter').val().length);
	// 초기값들 지정 해주기
	var topObj = $(".office_obj_wrap");
	var groupName = "";
	try{
		if(codeVal=="bgCode"){
			var loc = bgLocation[code.split("_")[0]].split("|");
			var xLoc = loc[kfer-1].split(",")[0];
			var yLoc = loc[kfer-1].split(",")[1];
		}else if(codeVal == "mgCode"){
			var loc = mgLocation[code.split("_")[1]].split("|");
			var xLoc = loc[kfer-1].split(",")[0];
			var yLoc = loc[kfer-1].split(",")[1];
		}else{
			var loc = sgLocation[code.split("_")[2]].split("|");
			var xLoc = loc[kfer-1].split(",")[0];
			var yLoc = loc[kfer-1].split(",")[1];
		}
	}catch(e){tryCatch(e)}

	var roomObjString = ""+
		"<div class='office_obj_room office_obj_room_type roomGroupCode' room-number code style='left:"+xLoc+";top:"+yLoc+";'>"+
		"<div class='office_obj_agent_wrap'>"+
		"<div class='office_wrap_tit'><p>groupName</p></div>";

	var dataStr;
	if(codeVal=="bgCode"){
		dataStr = {comboType : codeVal,	bgCode : code}
	}else if(codeVal == "mgCode"){
		//dataStr = {comboType : codeVal, mgCode : code}
		dataStr = {comboType : codeVal,	bgCode : code.split("_")[0], mgCode : code.split("_")[1]}
	}else if(codeVal == "earchAgentName"){
		codeVal = "sgCode";
		dataStr = {comboType : codeVal,	sgCode : code}
	}else if(codeVal == "earchAgentNum"){
		codeVal = "sgCode";
		dataStr = {comboType : codeVal,	sgCode : code}
	}else if(codeVal == "earchAgentExt"){
		codeVal = "sgCode";
		dataStr = {comboType : codeVal,	sgCode : code}
	}else{
		//dataStr = {comboType : codeVal,	sgCode : code}
		dataStr = {comboType : codeVal,	bgCode : code.split("_")[0], mgCode : code.split("_")[1], sgCode : code.split("_")[2]}
	}

	//분류별 이름
	$.ajax({
		url: contextPath+"/organizationSelect.do",
		data: dataStr,
		type: "POST",
		dataType: "json",
		async: false,
		success: function(jRes) {
			try{
				//console.log(jRes.resData.optionResult.replace(/(<([^>]+)>)/gi,""));
				roomObjString=roomObjString.split("groupName").join(jRes.resData.optionResult.replace(/(<([^>]+)>)/gi,""));
			}catch(e){tryCatch(e)}
		}
	});
	tempStr = roomObjString.split("room-number").join("room-number="+index);
	tempStr = tempStr.split("roomGroupCode").join(code);
	tempStr = tempStr.split("groupName").join(roomName[code]?roomName[code]:"이름없음");
	tempStr = tempStr.split("code").join("code="+code);
	if(code==undefined)
			return false;
	if(user>100){
		tempStr =  tempStr.split("office_obj_room_type").join("office_obj_room_type_03");
		roomGroupCount = 25;
		user = user -100;
	}else if(user<=16){
		tempStr =  tempStr.split("office_obj_room_type").join("office_obj_room_type_01");
		roomGroupCount = 4;
		user = user -16;
	}else{
		tempStr =  tempStr.split("office_obj_room_type").join("office_obj_room_type_02");
		roomGroupCount = 9;
		user = user -36;
	}
	roomViewArr += tempStr;
	for(var j = 1; j < roomGroupCount+1 ; j ++){
		setTimeout(roomGroupView(j),25);
		for(var k = 1; k < 5; k++){
			setTimeout(roomInObjView(j,k),25);			// 좌석 표시
			roomViewArr += "</div>";
		}
		roomViewArr += "</div>";
	}
	topObj.append(roomViewArr);
	if(DBROOMCHECK == false){
		$('.office_obj_room').css("position","relative");
	}
	var officeObjLenght = $('.office_obj_room').length;

	for(var i=0;i<officeObjLenght;i++){
		if($('.office_obj_room').eq(i).find(".office_wrap_tit").text() == "이름없음"){
			$('.office_obj_room').eq(i).remove();
		}
	}
	roomViewArr = "";
	if(user>0){
		setTimeout(drawRoom(code,user,index),25);
	}else{

//		if(window.innerWidth <1300){
//			$(".office_view_pannel_bg").css({"height": ($('body').prop("scrollHeight")+500)+"px"})
//		}else{
//			$(".office_view_pannel_bg").css({"height": $('body').prop("scrollHeight")+"px"})
//		}

		groupSeatNumber = 0;
		kfer = 0;
	}
}

// 방 그룹 그리기 함수
function roomGroupView(roomGroupCnt){
	var roomGroupObjString = ""+
	"<div class='office_agent_group_right group_num'>";
	roomViewArr +=roomGroupObjString.split("group_num").join("group_num_"+lpad(roomGroupCnt+"", 2, "0"));
}

// 방안 좌석 그리기
function roomInObjView(j,k){
	var roomInObjString = "<div class='office_drop_obj office_room_in_obj office_room_in_number_seat'>";
	 groupSeatNumber++;
	 var objString = roomInObjString.replace("office_room_in_number_seat","office_room_in_number_seat_"+groupSeatNumber);
	 objString = objString.replace("office_room_in_obj","office_room_in_obj_"+lpad(k+"", 2, "0"));
	 roomViewArr += objString;
}

//모니터링 오브젝트 생성 함수
function onUserInfoAdd(userInfo){
	var userExt = userInfo.EXT;
	var phoneNum = userInfo.CUSTNUM;
	var userStime  = userInfo.STIME;
	var userId = userInfo.AGENTID;
	var rtpCode = userInfo.RTP;
	var name = userInfo.AGENTNAME;
	var sex = (getUserInfo[userExt]) ? getUserInfo[userExt].userSex : "";
	sex = ((sex == "w" || sex == undefined) ? "woman" : "man");
	var status =userInfo.CTI.toLowerCase();
	var codeVal = $('#codeFilter').val().substring(1,$('#codeFilter').val().length).toUpperCase();
	var order = 1;
	var tempSeat = "";


	if(getAgentSeatInfo[userExt]!=null){
		if(codeVal=="BGCODE"){
			if(getAgentSeatInfo[userExt].rBgCode !="" && getAgentSeatInfo[userExt].rBgCode != null){
				userInfo[codeVal] = getAgentSeatInfo[userExt].rBgCode;
				tempSeat = getAgentSeatInfo[userExt].rBgCodeSeat;
			}
		}else if(codeVal=="MGCODE"){
			if(getAgentSeatInfo[userExt].rMgCode !="" && getAgentSeatInfo[userExt].rMgCode != null){
				userInfo[codeVal] = getAgentSeatInfo[userExt].rMgCode;
				tempSeat = getAgentSeatInfo[userExt].rMgCodeSeat;
			}
		}else{
			if(getAgentSeatInfo[userExt].rSgCode !="" && getAgentSeatInfo[userExt].rSgCode != null){
				userInfo[codeVal] = getAgentSeatInfo[userExt].rSgCode;
				tempSeat = getAgentSeatInfo[userExt].rSgCodeSeat;
			}
		}
	}
	var agentObjString = ""+
	"<div class='office_obj_agent office_obj_agent_woman_back_right agent_num'><div office-ext class='agent_status obj_calling'   office-phone  office-stime office-id><div class='rtp-not'></div></div><div class='agent_name'><p>agentName</p></div></div>"
	var codeVal = $('#codeFilter').val().substring(1,$('#codeFilter').val().length).toUpperCase();

	changedAgentObjString = agentObjString.split("obj_calling").join("obj_"+status);
	changedAgentObjString = changedAgentObjString.split("office-ext").join("office-ext="+userExt);
	changedAgentObjString = changedAgentObjString.split("office-id").join("office-id="+userId);
	changedAgentObjString = changedAgentObjString.split("office-stime").join("office-stime="+userStime);
	changedAgentObjString = changedAgentObjString.split("office-phone").join((phoneNum) ? "office-phone='"+phoneNum+"'" : "");
	changedAgentObjString = changedAgentObjString.split("agentName").join((name)?name:userExt);
	changedAgentObjString = changedAgentObjString.split("woman").join(sex);

	if(status == "calling" && rtpCode =="0"){
		changedAgentObjString = changedAgentObjString.split("rtp-not").join("rtp-not  office_slash_status");
	}

	try{
		//	사번, 상담원이름 , 내선 번호 검색 조건 때문에 추가
		if(codeVal == "EARCHAGENTNAME")
			codeVal = "SGCODE";
		if(codeVal == "EARCHAGENTNUM")
			codeVal = "SGCODE";
		if(codeVal == "EARCHAGENTEXT")
			codeVal = "SGCODE";

		var seatCode = "";
		if (codeVal == "BGCODE")
			seatCode = userInfo["BGCODE"]
		if (codeVal == "MGCODE")
			seatCode = userInfo["BGCODE"] +"_"+ userInfo["MGCODE"]
		if (codeVal == "SGCODE")
			seatCode = userInfo["BGCODE"] +"_"+ userInfo["MGCODE"] +"_"+ userInfo["SGCODE"]
		
		var userObjSeat = $('.'+seatCode).find('.office_drop_obj');
		for(var i=0;i<userObjSeat.length;i++){
			if(userObjSeat.eq(i).html()==""){
				if(tempSeat!=null && tempSeat != ""){
					switch((tempSeat-1)%4){
						case 1 : order = "2"; break;
						case 2 : order = "3"; break;
				 		case 3 : order = "4"; break;
				 		case 0 : order = "1"; break;
					}
					if(Number(order)%2==0)
				 		changedAgentObjString = changedAgentObjString.split("back").join("front");
					changedAgentObjString = changedAgentObjString.split("agent_num").join("agent_num_"+lpad(order, 2, "0"));
					userObjSeat.eq(tempSeat-1).html(changedAgentObjString);
				}else{
					switch(i%4){
						case 1 : order = "2"; break;
						case 2 : order = "3"; break;
				 		case 3 : order = "4"; break;
				 		case 0 : order = "1"; break;
					}
					if(Number(order)%2==0)
				 		changedAgentObjString = changedAgentObjString.split("back").join("front");
					changedAgentObjString = changedAgentObjString.split("agent_num").join("agent_num_"+lpad(order, 2, "0"));
					userObjSeat.eq(i).html(changedAgentObjString);
				}
				break;
			}
		}
	}catch(e){
		tryCatch(e)
	}

	//청취 이벤트
	$(".agent_status").parent().click(function(event){
		event.stopPropagation();
		event.stopImmediatePropagation();
	 	if($(this).find("div").hasClass("obj_calling")){
	 		listenEvent($(this).find("div").attr("office-ext"), $(this).find("div").attr("office-phone"),$(this).find('div').attr("office-stime"),userInfo.SERVERIP);
	 		defineUserId =  $(this).find("div").attr("office-id");
			defineCustNum = $(this).find("div").attr("office-phone");
			defineUserName = $(this).parent().find(".agent_name p").html();
	 	}
 	});

	//내선번호 감청 유지
	if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && status == "calling"){
		if($('#custPhoneContinue').val().replace(/-/gi,'') == userInfo.EXT && RealTimeExt == ""){
			$("[office-ext="+userExt+"]").parent().trigger("click");
		}
//			listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME);
//			defineUserId = getUserInfo[userExt].userId;
//			defineCustNum = userInfo.CUSTNUM;
//			defineUserName =  getUserInfo[userExt].userName;
	}

	//고객번호 감청 유지
	if($('#custPhoneContinue').val() != "" && $('.custPhoneContinueChk').is(":checked") == true && status == "calling" && playState == false){
		if($('#custPhoneContinue').val().replace(/-/gi,'') == userInfo.CUSTNUM){
//			listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME);
//			defineUserId = getUserInfo[userExt].userId;
//			defineCustNum = userInfo.CUSTNUM;
//			defineUserName =  getUserInfo[userExt].userName;
			$("[office-ext="+userExt+"]").parent().trigger("click");
		}
	}

//	drag();
}

//상태 변화시  유저 체인지
function onUserInfoChange(userInfo){
	var status = userInfo.CTI.toLowerCase();
	var userExt = userInfo.EXT
	var phoneNum = userInfo.CID;
	var rtpCode = userInfo.RTP;
	var $agentObj = $("[office-ext="+userExt+"]");
	// obj로 시작하는거 다 지워버리깅(상태값 지워버리깅)
	$agentObj.removeClass (function (index, className) {
	    return (className.match (/(^|\s)obj_\S+/g) || []).join(' ');
	});

	if(status=="calling" && rtpCode == "1"){
		$agentObj.find('.rtp-not').removeClass("office_slash_status");
	}else if(status=="calling" && rtpCode == "0"){
		$agentObj.find('.rtp-not').addClass("office_slash_status");
	}else{
		$agentObj.find('.rtp-not').removeClass("office_slash_status");
	}
	// 상태값 다시 입력해주깅
	$agentObj.addClass("obj_"+status)

	// 실감중에 정보 새로 받아오면... 처리해주기
    if(RealTimeExt == userExt){
    	$(".officeListening").removeClass("officeListening")
    	$("[office-ext="+userExt+"]").parent().addClass("officeListening")
    }else{
    	$("[office-ext="+userExt+"]").parent().removeClass("officeListening")
    }

	thisShowHide(userExt);

	//청취 이벤트
	$(".agent_status").parent().click(function(event){
		event.stopPropagation();
		event.stopImmediatePropagation();
	 	if($(this).find("div").hasClass("obj_calling")){
	 		listenEvent($(this).find("div").attr("office-ext"), $(this).find("div").attr("office-phone"),$(this).find('div').attr("office-stime"),userInfo.SERVERIP);
	 		defineUserId =  $(this).find("div").attr("office-id");
			defineCustNum = $(this).find("div").attr("office-phone");
			defineUserName = $(this).parent().find(".agent_name p").html();
	 	}
 	});

	//내선번호 감청 유지
	if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && status == "calling"){
		if($('#custPhoneContinue').val().replace(/-/gi,'') == userInfo.EXT && RealTimeExt == ""){
			$("[office-ext="+userExt+"]").parent().trigger("click");
		}
//			listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME);
//			defineUserId = getUserInfo[userExt].userId;
//			defineCustNum = userInfo.CUSTNUM;
//			defineUserName =  getUserInfo[userExt].userName;
	}

	//고객번호 감청 유지
	if($('#custPhoneContinue').val() != "" && $('.custPhoneContinueChk').is(":checked") == true && status == "calling" && playState == false){
		if($('#custPhoneContinue').val().replace(/-/gi,'') == userInfo.CUSTNUM){
//			listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME);
//			defineUserId = getUserInfo[userExt].userId;
//			defineCustNum = userInfo.CUSTNUM;
//			defineUserName =  getUserInfo[userExt].userName;
			$("[office-ext="+userExt+"]").parent().trigger("click");
		}
	}

//	if($('.listenTarget').length > 0){
//    	if(!playState)
//    		if( $(".listenTarget").hasClass("obj_calling") )
//    			listenEvent($(".listenTarget").attr("office-ext"), $(".listenTarget").attr("office-phone"))
//	} else {
//		/* console.log("청취 유지중입니다.") */
//	}
}

//실시간으로 체크 값 받아서 상담원  show hide
function thisShowHide(ext){
	$('.onoffswitch-checkbox[name=selStatus]').each(function(i){
		// 아이디의 뒷부분을 소문자로 얻음
	   var targetClass = $(this).attr("id").replace(/^selStatus(.*?)$/, function(){return arguments[1].toLowerCase()});
	   var $targetOffice = $(".obj_" + targetClass).parents('.office_obj_agent');

	   if($(this).is(':checked')) {
		   $targetOffice.show();
	   } else {
		   $targetOffice.hide();
	   }
	})
}

//자릿수 메꿔주는 함슈
function lpad(s, padLength, padString){
while(s.length < padLength)
   s = padString + s;
return s;
}

//중복 배열 제거 함수
function arrayUniq(arr){
	var uniq = arr.reduce(function(a,b){
		if(a.indexOf(b) <0) a.push(b);
		return a;
	},[]);
	return uniq;
}

//청취 기능
//extNo : 내선
function listenEvent(extNo, phoneNum,stime,serverIp){
	defineStime = stime;

	function doActive(extNo) {
		//console.log(extNo)

//		var clickedObj = $("#"+extNo);
//
//		var setExt = extNo;
//		var setUserName = ((getUserInfo[extNo]) ? getUserInfo[extNo].userName : "");
		//realtime_rc.obj.controller.setPlayerInfo(extNo,setUserName,phoneNum);

	}
	if(RealTimeExt+"".trim() != ""){
		try{
			document.getElementById('realtimePlayer').getElementsByTagName('audio')[0].src="";

			SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");

			SocketClient.Socket.close();
		}catch(e){tryCatch(e)}
		RealTimeExt ="";
	}else{
		SetServerinfo(serverIp);
		Init(extNo,serverIp);
		doActive(extNo);
	}
}

function searchFilterLoad(){
	if(accessLevel != "A" && accessLevel != "B") {
		if(accessLevel == "M") {
			$('#codeFilter option[value="mBgCode"]').remove();
			$('#codeFilter').val('mMgCode')
							.trigger('change');
		}
		if(accessLevel == "S") {
			$('#codeFilter option[value="mBgCode"]').remove();
			$('#codeFilter option[value="mMgCode"]').remove();
			$('#codeFilter').val('mSgCode')
							.trigger('change');
		}
		if(accessLevel == "U") {
			$('#codeFilter option[value="mBgCode"]').remove();
			$('#codeFilter option[value="mMgCode"]').remove();
			$('#codeFilter option[value="mSgCode"]').remove();
			$('#codeFilter').val('searchAgentName')
							.trigger('change');
		}
//		if(accessLevel == "U"){
//			$('#codeFilter').hide();
//			$('#mBgCode').val(userInfoJson.bgCode)
//			.trigger('change');
//			$('.select2').hide();
//		}
	}
}
