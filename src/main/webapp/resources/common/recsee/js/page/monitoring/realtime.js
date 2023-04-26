var getUserInfo = {}; // 전역 변수 ; 유저 정보 가져와서 담을 그릇 [키값은 내선번호]
var loadedOffice = [];
var roomPersonnel = 16;
var realtime_rc;
var moniteringType = "card";
var ajaxInfo;
var defineStime = "";
var defineCustNum = "";			//콜인입 고객 전화번호
var defineUserName = "";				//콜인입 상담사 이름
var defineUserId = "";
var monitoringInterval;
var monitoringIpSelect= [];


var appointmentListenExt;       // 실감예약 내선
var appointmentListenPhone;     // 실감예약 사용자 번호

window.userList = [];		// 사용자 정보
window.userBgList = [];		// 대분류 코드 정보
window.userMgList = [];		// 중분류 코드 정보
window.userSgList = [];		// 소분류 코드 정보

var userQue;
var userAdd;					//유저 그리기
var userLength;
var selectArr = [];				//조회 선택값
var selectCodeArr = [];
var selectCode = "";						//분류 선택 값

var listenUser={};					//	실감 지속 감청 팝업이 없어도 들을수 잇게 설정하기 위하여....

var alertChk = false;						//조회 알림창 나타 낫었는지 확인 값

/**
 * @param 실감예약
 *            할 타입 ext | phone
 * @param 실감예약
 *            할 대상 값
 */
function appointmentListen(type, value){
	if (type == "ext"){
		appointmentListenExt = value;
		appointmentListenPhone = "";
	}else if(type == "value"){
		appointmentListenPhone = value;
		appointmentListenExt = "";
	}
}

$(function() {

	userQue = new Queue();

	// 옵션 체크 오브젝트 삽입
	$('.onoffswitch-label').append('<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">on</span>');

	//내선 번호 체크 함수
	$('.office_stay_check').change(function(){
		//	DB 저장
		if($(this).is(":checked") == true && $('#statusSave').is(":checked") == true){
			realtimeinsertType('E');
 		}else if($(this).is(":checked") == false && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('X');
 		}else if($('.custPhoneContinueChk').is(":checked")== true && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('C');
 		}else if($('.custPhoneContinueChk').is(":checked")== false && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('X');
 		}
		//체크한경우+번호 기입
		if($(this).is(":checked")==true && $("#custPhoneContinue").val() != ""){
			var agent = $("#"+$('#custPhoneContinue').val());
			if(agent.length>0 && agent.find('input').is(':checked')!= true ){
				agent.find(".onoffswitch-label").click();
			}

		}else{
			 if ($('.listen-away').length >= 0){
				 $('.listen-away').remove();
			 }
			var agent = $("#"+$('#custPhoneContinue').val());
			if(agent.length>0 && agent.find('input').is(':checked') == true ){
				agent.find(".onoffswitch-label").click();
			}
		}
 		if($('.custPhoneContinueChk').is(":checked")== true)
 			$('.custPhoneContinueChk').prop("checked",false)
 	})

 	//고객 번호 체크 함수
 	$('.custPhoneContinueChk').change(function(){
 		if($(this).is(":checked") == true && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('C');
 		}else if($(this).is(":checked") == false && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('X');
 		}else if($('.office_stay_check').is(":checked")== true && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('E');
 		}else if($('.office_stay_check').is(":checked")== false && $('#statusSave').is(":checked") == true){
 			realtimeinsertType('X');
 		}
 		if($('.office_stay_check').is(":checked")== true)
 			$('.office_stay_check').prop("checked",false)

 		//	고객 전화번호  체크 true 시 모든 on off [off]
		if($(this).is(':checked') == true){
			$('#viewCardMode .onoffswitch-checkbox').prop("checked",false);
		}
 	})

 	$("#monitoringWindow").click(function(){
 		//새창으로 모니터링 띄우기
 		try{
			clearInterval(monitoringInterval);
			clearInterval(userAdd);
			$('.main_contents').remove();
		}catch(e){tryCatch(e)}
		var targetPath = webPath+"/monitoring/realtime_newWindow"
		window.open(targetPath, "_blank", "resizable=yes, toolbar=no,location=no, width=1500, height=700,scrollbars=yes ,left=50,top=50'");

 	});
	
});

addLoadEvent(monitroingLoad);

function monitroingLoad(){
	
	onLoad();
	viewStatus();
    viewDisplay();
    if(telnoUse=='Y'){
    	$("#codeFilter").css("display","none");
    	$("#btnMonitSetOpen").css("display","none");
    	$("#mSearchBtn").css("display","none");
    	$('.select2').hide();
    	$('#mBgCode').val('B000').trigger('change');
    	onClickAction()
    }else{
    	$("#codeFilter").css("display","display");
    	$("#btnMonitSetOpen").css("display","display");
    	$("#mSearchBtn").css("display","display");
    	$('.select2').show();
    }
}

/** **********레디스 임시 *************** */
function startRedisSocket(){
	clearInterval(monitoringInterval);
	clearInterval(userAdd);
	MONITORING_TIME = 3;
	monitoringInterval = window.setInterval(function(){repeat()}, MONITORING_TIME * 1000);

	 userAdd = window.setInterval(function(){
		 if(userQue.front()==undefined){
			 return;
		 }

		 if($("#"+userQue.front().EXT).length==0){
			 onUserInfoAdd(userQue.front());
		 }
		 userQue.dequeue();
	 },1);
}
function repeat() {
	$.ajax({
//		url:contextPath+"/monitoring/officeMonitoringSelect.do",
		url:contextPath+"/monitoring/cardMonitoringSelect.do" ,
		data:{
//			SYSID : $('#systemCode option:selected')[0].getAttribute('ip')
			SYSID : monitoringIpSelect.toString()
		,	selectCode : selectCode
		,	selectArr : selectArr.toString()
		},
		type:"POST",
		dataType:"json",
		sync : false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var searchMonitoring = [];

				var responseData = jRes.resData.agentExt;

				var Code = [];
				if(selectCodeArr.length>0){
					for(var i=0;i<selectCodeArr.length;i++){
						var k = selectCodeArr[i].toString().split("\"")
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
									//
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
				//	조회값 없을 때 출력
//				if(searchMonitoring.length ==0 && alertChk == false){
//					alertChk = true;
//					alertText("조회","조회된 값이 없습니다.");
//				}
				
				// 내선번호 기준으로 정렬
				searchMonitoring.sort(function (a, b) {  return a.EXT < b.EXT ? -1 : a.EXT > b.EXT ? 1 : 0; });
				
		        onMessageMonitoring(searchMonitoring);
		        searchMonitoring = [];
			}else{
//				if($('#viewCardMode').html().trim() == ""){
//					alertText("조회","조회된 값이 없습니다.");
//				}
			}
		}
	})
    return true;
}

/** **********레디스 임시 *************** */
// IE9 Transport 오류 관련 처리
jQuery.support.cors = true;

function getSelect2Datas(){
	var selectedCode = $( "#codeFilter :selected" ).val()
	var data = $('#'+selectedCode).select2('data');
	var dataId = [];
	for (var i = 0 ; i < data.length ;i++){
		dataId.push(data[i].id)
	}
	var o = {
			"data" : dataId
		,	"selectedCode" : selectedCode
	}
	return o;
}


function onLoad() {
	
	if(telnoUse == "Y") {
	     // 자번호 사용시 아이콘 표시 속성 off
	     $('.onoffswitch-checkbox[id=selDisplayIcon]').removeAttr('checked');
	     $('.onoffswitch-checkbox[id=selDisplayIcon]').parent().find('.onoffswitch-switch').text('off');
	     
	     $("#selDisplayIconBox").hide();
	     
	} 
	
	//모바일인 경우 내선번호 검색 숨기기
	if(recsee_mobile == "Y"){
		$("select option[value*='searchAgentExt']").hide();
	}

	// 조회 버튼 클릭 시
	$("#mSearchBtn").on("click",function(){
		onClickAction();
	});

	//selectCode = selectorString;
	// 사용자 정보 담아 놓기
	if (telnoUse != "Y") {
		getRuserInfo()
	}

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
			//selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,undefined,true)
			break;
		case "mMgCode":
			$("#mBgCode, #mMgCode").show().val("")
			//$("#mBgCode option:last").remove();
			//$("#mBgCode").append("<option value='' selected disabled>"+lang.admin.label.bgName+"</option>");
			groupSearch(lang.monitoring.alert.msg4/*"대상 그룹을 선택 해 주세요"*/);
			//selectOrganizationLoad($("#mMgCode"), "mgCode",undefined,undefined,undefined,undefined,true)
			break;
		case "mSgCode":
			$("#mBgCode, #mMgCode,#mSgCode").show().val("")
			//$("#mBgCode option:last").remove();
			//$('#mMgCode option').remove();
			//$("#mBgCode").append("<option value='' selected disabled>"+lang.admin.label.bgName+"</option>");
			//$("#mMgCode").append("<option value='' selected disabled>"+lang.admin.label.mgName+"</option>");
			groupSearch(lang.monitoring.alert.msg4/*"대상 그룹을 선택 해 주세요"*/);
			//selectOrganizationLoad($("#mSgCode"), "sgCode",undefined,undefined,undefined,undefined,true)
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
				, dropdownAutoWidth : true
				,	width : 'auto'
			});
		}
	});
	// 대분류
//	selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,undefined,true)
	selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true,"callCenter") //기존
	
	
	
	$("#mBgCode").attr("multiple","multiple").css("width","500").select2({
			placeholder: lang.monitoring.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
		,	allowClear: true
	});

	// 대분류 변화시 중분류 로드
	$( "#mBgCode" ).change(function() {
		$("#mSgCode").empty().val(null).trigger("change");
		selectOrganizationLoad($("#mMgCode"), "mgCode", $(this).val(),undefined,undefined,true)
		//$("#mMgCode").append("<option value='' selected disabled>"+lang.views.monitoring.mgName+"</option>");
		// 중분류 및의 소분류가 있다면 디폴트로 로드
	});

	// 중분류 변화시 소분류 로드
	$( "#mMgCode" ).change(function() {
		$("#mSgCode").empty().val(null).trigger("change");
		selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),undefined,true)
	});

	//	권한에 따른 초기 필터 설정
	searchFilterLoad();

	// 시스템 변환시 출력 변경
	$("#systemCode").change(function() {
		$(".now_system_info").text($("#systemCode option:selected").text())
		ajaxInfo.ip=$("#systemCode option:selected").attr("ip");
		monitoringIP=listenIp;

		// 카드 모니터링
		$(".status_card_wrap").remove();
	});

	dhx4.ajax.get(contextPath+"/monitoring/realtime_init.json", function( response ){
	    var realTime = jQuery.parseJSON(response.xmlDoc.responseText);

	    if(realTime.system.length > 0) {

	        systemPath = realTime.system;

	        //	시스템 코드 추가
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

	if(recsee_mobile == "Y"){
		$("#bottomFixedPlayer").hide();
		$("#btnListenAlway").hide();		
	}else{
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
			,	"btnMute"			: false				// 구간 설정 시 묵음처리 기능
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
	}
	ui_controller();
	select2InputFilter();
}


// 모니터링 오브젝트 생성 함수
function onUserInfoAdd(userInfo, userInfoArray){
	if((userInfo.EXT||"") == "") {
		   //
	} else if($("#"+userInfo.EXT).length < 1) {
    		var userExt = userInfo.EXT;
    		var phoneNum = userInfo.CUSTNUM;
    		var callStime = userInfo.STIME;
    		var duration = userInfo.duration;
    		var userName = (getUserInfo[userExt]) ? getUserInfo[userExt].userName : "";
    		var userId = (getUserInfo[userExt]) ? getUserInfo[userExt].userId : "";
    		var bgCode = (getUserInfo[userExt]) ? getUserInfo[userExt].bgCode : "emptyBg";
    		var mgCode = (getUserInfo[userExt]) ? getUserInfo[userExt].mgCode : "emptyMg";
			var sgCode = (getUserInfo[userExt]) ? getUserInfo[userExt].sgCode : "emptySg";
			var mgName = (getUserInfo[userExt]) ? getUserInfo[userExt].mgName : "emptyMg";
			var rtpCode = userInfo.RTP;
			//이석 상태 값 추가
			var ctiReason = userInfo.CTIREASON;

			var data = getSelect2Datas();
			var datas = data.data;
			var selectedCode = data.selectedCode;
			var noListen = "";
			var notAllowed = "";
			var telnoClass = "hideColumn";
			if(telnoUse == "N") {
				telnoClass = "";
				if(datas.length > 0){
					if (selectedCode == "mBgCode"){
						if (!(datas.indexOf(bgCode) > -1))
							return;
						else
							$("#"+userExt).remove();
	
					}else if (selectedCode == "mMgCode"){
						if (!(datas.indexOf(mgCode) > -1))
							return;
						else
							$("#"+userExt).remove();
	
					}else if (selectedCode == "mSgCode"){
						if (!(datas.indexOf(sgCode) > -1))
							return;
						else
							$("#"+userExt).remove();
					}
				}
			}
			
			
			if(userInfo.CTI.toLowerCase() == "calling" && userInfo.RTP == "0"){
				notAllowed = "realtime_no_listen";
				noListen = "realtime_slash_status";
			}
			if(userInfo.CTI.toLowerCase() == "logout"){
				duration = "";
			}
			var status = userInfo.CTI.toLowerCase();
			
			var statusTxt = userInfo.CTI.toLowerCase();
			// GS홈쇼핑에서 통화 시작 후 이석으로 상태 변경해서 사용한다고 해서 RTP 1인것은 무조건 통화중으로 표시하게 함
			if (userInfo.RTP == "1") {
				statusTxt = lang.monitoring.alert.msg10/*"전화중"*/;
				status = "calling";
			} else if(statusTxt == "login"){
				statusTxt = lang.monitoring.alert.msg8/*"로그인"*/;
			}else if(statusTxt == "logout"){
				statusTxt = lang.monitoring.alert.msg9/*"로그아웃"*/;
			}else if(statusTxt == "calling"){
				statusTxt = lang.monitoring.alert.msg10/*"전화중"*/;
			}else if(statusTxt == "aftercallwork"){
				statusTxt = lang.monitoring.alert.msg11/*"후처리"*/;
			}else if(statusTxt == "ready"){
				statusTxt = lang.monitoring.alert.msg12/*"준비"*/;
			}else if(statusTxt == "ringing"){
				statusTxt = lang.monitoring.alert.msg13/*"연결중"*/;
			}else if(statusTxt == "notready"){
//				statusTxt = ctiReason;
				statusTxt = lang.monitoring.alert.msg14/*"이석"*/;
			}else if(statusTxt == "offering"){
				statusTxt = lang.monitoring.alert.msg9/*"로그아웃"*/;
			}

			//채널 정보 들고온 후 녹취 안하면 표출 안해주기..
			/*if(userInfo.RECORD != "Y"){
				statusTxt = "미사용";
				//return false;
			}*/
			
			if(userInfo.BGCODE != "BGIVR"){
								
				// 고객전화번호 마스킹 처리
				var realcustnum = userInfo.CUSTNUM;
	           	 if(realcustnum.length > 8){
	           		//realcustnum = realcustnum.substring(0,3)+"-****-"+realcustnum.substring(7);
	           	 }			
				
				// 상담원 오브젝트
				$("#viewCardMode").append(
			        "<div class='status_card_wrap' id='" + userExt + "' bgCode='"+bgCode+"' mgCode='"+mgCode+"' sgCode='"+sgCode+"' phoneNum='"+phoneNum+"' callStime='"+callStime+"' RTP='"+rtpCode+"'>"+
			        	"<div class='status_card "+notAllowed+" agent_status_"+status+"\' id=\""+userInfo.CTI.toLowerCase()+"\">"+
					        "<div info-ext='"+userExt+"' class='obj_agent'></div>"+
					        /*"<div style='position:absolute;width:30px;height:30px;top:20px;margin-left:7px;z-index:1000000;text-align:center;border:1px solid #eee;border-radius:20px;'><i class='fas fa-bullhorn' style='color:white;font-size:20px;margin-top:5px;'></i></div>"+*/
					        "<div class='status_txt "+noListen+"'>"+
					           "<p>"+statusTxt+"</p>"+
					        "</div>"+
					        "<div class='card_info_wrap'>"+
						         "<div class='agent_info_wrap'>"+
							         "<div class='agent_info'>"+
							         	"<p class='agnet_exnum'>"+userExt+"</p>"+
							         "</div>"+
							         "<div class='agent_info'>"+
							         	"<p class='anget_id'>"+userInfo.AGENTID+"</p>"+
							         "</div>"+
							         "<div class='agent_info'>"+
							         	"<p class='anget_custnum' style='overflow:hidden'>"+realcustnum+"</p>"+
							         "</div>"+
							         "<div class='agent_info "+telnoClass+"'>"+
							         	"<p class='anget_name' style='overflow:hidden'>"+userName+"</p>"+
							         "</div>"+
							         "<div class='agent_info "+telnoClass+"'>"+
							         	"<p class='anget_mgname'>"+mgName+"</p>"+
							         "</div>"+
							         "<div class='agent_info'>"+
							         	"<p class='anget_duration'>"+duration+"</p>"+
							         "</div>"+
							         "<div class='agent_info'>"+
								         '<div class="option_check">' +
								            '<div class="onoffswitch">' +
								                '<input type="checkbox" name="stayRealtime'+userExt+'" class="onoffswitch-checkbox" id="stayRealtimeListen'+userExt+'"/>' +
								                '<label class="onoffswitch-label" for="stayRealtimeListen'+userExt+'">' +
								                	'<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">off</span>' +
								                '</label>' +
								            '</div>' +
								        '</div>' +
							         "</div>"+
						         "</div>"+
					        "</div>"+
				        "</div>"+
			        "</div>"
			    );
			}

			if(telnoUse == "Y") {
			     $(".obj_agent").hide();
			} 
			
			if(recsee_mobile != "Y"){
				//@david
				// 청취 버튼 이벤트
				$('#'+userExt).on("click", function(event){
					event.stopPropagation();
					event.stopImmediatePropagation();

					if(event.target.className != "onoffswitch-checkbox"){
						// 청취 상태에서만 클릭클릭 클릭 현재 클릭된객체의 아이디(내선) 넘기기
						if($(this).find("div").hasClass("agent_status_calling") && !$(this).find("div").hasClass("realtime_slash_status")){
							listenEvent($(this)[0].id,$(this)[0].getAttribute('phonenum'),$(this)[0].getAttribute('callstime'),userInfo.SERVERIP);
							defineUserId = $(this)[0].getElementsByClassName('anget_id')[0].textContent;
							defineCustNum = $(this)[0].getAttribute('phonenum');
							defineUserName = $(this)[0].getElementsByClassName('anget_name')[0].textContent;
						}
					}
				});
				
				// @ezra
				// on-off에 따른 attribute 달기 (for 감청 유지)
	        	$('#'+userExt+' .onoffswitch-checkbox').change(function() {

	        		if($('.listen-away').length >= 0){
	        			$('.listen-away').remove();
	        		}

	        		if($(this).is(':checked') == false) {
	        			// checked 속성이 있을 경우 속성 삭제
	        			$(this).removeAttr('checked');
	        			$(this).parent().find('.onoffswitch-switch').text('off');
	        			$(this).parents('.status_card_wrap').removeAttr('stay-target');

	            	} else if($(this).is(':checked') == true) {
	            		// this를 제외하고 checked, stay 속성 제거
	        			$('#viewCardMode .onoffswitch-checkbox').not(this).removeAttr('checked');
	        			$('#viewCardMode .onoffswitch-checkbox').not(this).parent().find('.onoffswitch-switch').text("off");
	        			$('.status_card_wrap').removeAttr("stay-target");
//	        			$(this).not().find('.onoffswitch-switch').text('off');
	        			$(this).parent().find('.onoffswitch-switch').text('on');

	        			// checked 속성이 없을 경우 속성 추가
	        			$(this).attr('checked', true);

	        			// checked 속성이 있는 객체의 최상위 부모 찾기
	        			var $stayTarget = $(this).closest('.status_card_wrap');
	        			// stay on
	        			$stayTarget.attr("stay-target","on");
	        		}
	        	})

	        	//	onoff value  값 체크
	        	if($('.custPhoneContinueChk').is(":checked") == false && $('#statusSave').is(":checked") == true){
	        		if($("#"+$('#custPhoneContinue').val()).find(".onoffswitch-checkbox").is(":checked") == false){
	        			$("#"+$('#custPhoneContinue').val()).find(".onoffswitch-label").click();
	        		}
	        	}
			}else{
				$(".status_card_wrap").find(".card_info_wrap").find(".agent_info:last-child").hide()
			}

			// 성별
			var agnetObj = $("[info-ext='"+userExt+"']");
			var sex = (getUserInfo[userExt]) ? getUserInfo[userExt].userSex : "";
			sex = ((sex == "w" || sex == undefined) ? "w" : "m");
			if(sex == 'm'){
			   	$(agnetObj).css({"background-image": "url("+commResourcePath+"/recsee/images/project/main/obj_man.png)"})
			}else{
			    $(agnetObj).css({"background-image": "url("+commResourcePath+"/recsee/images/project/main/obj_woman.png)"})
			}
		}

	$("#stayRealtimeListen"+userExt).click(function(){
			$(".office_stay_check, .custPhoneContinueChk").prop("checked",false)
			$(".office_stay_check").prop("checked",true)
			$("#custPhoneContinue").val(userExt);

			$.ajax({
				url: contextPath+"/monitoring/realtimeSetting.do",
				data: {
					settingNum : userExt
				},
				type: "POST",
				dataType: "json",
				async: false,
				success: function(jRes) {
				}
			});

			//	재생 중지
			try{
				realtime_rc.obj.audio[0].pause();
				realtime_rc.obj.audio[0].currentTime= 0;
				document.getElementById('realtimePlayer').getElementsByTagName('audio')[0].src="";
				$("#"+RealTimeExt+" > div").find('.agent_status_active').remove();
				SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
				SocketClient.Socket.close();
			}catch(e){tryCatch(e)}

			if($(this).is(":checked") == false){
				$("#custPhoneContinue").val("")
				$(".office_stay_check, .custPhoneContinueChk").prop("checked",false)
				$.ajax({
					url: contextPath+"/monitoring/realtimeSetting.do",
					data: {
						settingNum : ""
					},
					type: "POST",
					dataType: "json",
					async: false,
					success: function(jRes) {
					}
				});
			}
	})
//	 if(userLength == $('.status_card_wrap').length){
	if(userLength == $('.status_card_wrap').length-4){
		userQue = new Queue();
		clearInterval(userAdd);
	 }
    ui_controller();
}


// @ezra
// 감청 유지 실행 함수 (Card View)
function ListenStayOn() {
	$('.status_card_wrap').each(function() {
    	if($(this).attr("stay-target") == "on") {
    		var test = $(this).attr("id")
    		$(this).click();
    	}
	})
}


// 상담원 실시간 상태 받아오는 함수
function onUserInfoChange(userInfo){
	var userExt = userInfo.EXT;
	if($("#"+userExt).length > 0) {

		var status = userInfo.CTI.toLowerCase();
    	var thisObj = $("#"+userExt).find(".status_card");
		var phoneNum = userInfo.CUSTNUM;
		var callStime = userInfo.STIME;

		//이석 상태 값 추가
		var ctiReason = userInfo.CTIREASON;
		
		var statusTxt = userInfo.CTI.toLowerCase();
		// GS홈쇼핑에서 통화 시작 후 이석으로 상태 변경해서 사용한다고 해서 RTP 1인것은 무조건 통화중으로 표시하게 함
		if (userInfo.RTP == "1") {
			statusTxt = lang.monitoring.alert.msg10/*"전화중"*/;
			status = "calling";
		} else if(statusTxt == "login"){
			statusTxt = lang.monitoring.alert.msg8/*"로그인"*/;
		}else if(statusTxt == "logout"){
			statusTxt = lang.monitoring.alert.msg9/*"로그아웃"*/;
		}else if(statusTxt == "calling"){
			statusTxt = lang.monitoring.alert.msg10/*"전화중"*/;
		}else if(statusTxt == "aftercallwork"){
			statusTxt = lang.monitoring.alert.msg11/*"후처리"*/;
		}else if(statusTxt == "ready"){
			statusTxt = lang.monitoring.alert.msg12/*"준비"*/;
		}else if(statusTxt == "ringing"){
			statusTxt = lang.monitoring.alert.msg13/*"연결중"*/;
		}else{
			statusTxt = lang.monitoring.alert.msg14/*"이석"*/;
		}

		//채널 정보 들고온 후 녹취 안하면 표출 안해주기..
		/*if(userInfo.RECORD != "Y"){
			statusTxt = "미사용";
			//return false;
		}*/       
		
		$("#"+userExt).attr("phoneNum",phoneNum);
		$("#"+userExt).attr("callStime",callStime);
		$("#"+userExt).attr("SERVERIP",userInfo.SERVERIP);
	    thisObj.removeClass (function (index, className) {
    	    return (className.match (/(^|\s)agent_status_\S+/g) || []).join(' ');
    	});
	    thisObj.removeClass('realtime_no_listen');
	    thisObj.children('.status_txt').removeClass('realtime_slash_status');
	    thisObj.css("cursor","");
	    thisObj.addClass("agent_status_" + status);
	    thisObj.attr("id",status);
	    thisObj.children('.status_txt').find('p').text(statusTxt);

	    if(status=="calling" && userInfo.RTP == "0"){
	    	thisObj.children('.status_txt').addClass('realtime_slash_status');
	    	thisObj.addClass("realtime_no_listen");
	    }
	    if(status=="calling" && userInfo.RTP == "1"){
	    	thisObj.children(".status_txt").removeClass("realtime_slash_status");
	    	thisObj.removeClass("realtime_no_listen");
	    }

	    // == 추가 ==
	    if(RealTimeExt == userInfo.EXT && isInternetExplorer) {
	        beforeStatus = $(thisObj).data("beforeStatus");
	        if(beforeStatus == "5" && userInfo.CTI != "5") {
	            // Calling에서 값이 바뀐 경우
	            // 라벨 및 오디오 숨기기
	            $(".listen_data.ie").hide();

	            // 오디오 끊기
	            $("#audio_player")[0].src = "";
	        }
	    }

	    thisShowHide();

	    $(thisObj).data("beforeStatus", userInfo.CTI.toLowerCase());
	    // == 추가 끝

	    // 실감중에 정보 새로 받아오면... 처리해주기
	    if(RealTimeExt == userInfo.EXT){
	    	if($("#"+userInfo.EXT).find('.status_card').lenght == 0)
	    		$("#"+userInfo.EXT).find('.status_card').append('<div class="agent_status_active"></div>');
	    }else{
	    	$("#"+userInfo.EXT+" > div").find('.agent_status_active').remove();
	    }
	    // @ezra
	    // 실감 유지 on-off 및 상태 체크하여 실행
    	if($(thisObj).parent().attr("stay-target") == "on" && status=="calling" && playState== false) { // on일
																					// 경우
    		var stayTargetId = $(thisObj).parent().attr("id");
    		RealTimeExt = "";
    		//console.log("감청 유지 중인 내선 번호 : "+stayTargetId)

    		if($(thisObj).find('.agent_status_active').length == 0){
	    		//listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME);
    			if(telnoUse!='Y'){
	    			defineUserId = getUserInfo[userExt].userId;				
					defineUserName =  getUserInfo[userExt].userName;
    			}
				defineCustNum = userInfo.CUSTNUM;
    		} else {
    			//console.log("청취 유지중입니다.")
    		}
    	}

		//내선번호 감청 유지[체크박스]
		if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && status == "calling" && playState == false && userInfo.RTP == "1"){
			if($('#custPhoneContinue').val().replace(/-/gi,'') == userInfo.EXT && RealTimeExt == ""){
				//listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME);
				if(telnoUse!='Y'){
					defineUserId = getUserInfo[userExt].userId;
					defineUserName =  getUserInfo[userExt].userName;
				}				
				defineCustNum = userInfo.CUSTNUM;
				if($('.listen-away').length <= 0){
					 if(telnoUse=='Y'){
						 $('.listen-away').remove();
					 }
					
					$('#bottomFixedPlayer').after('<div class="listen-away"><marquee></marquee></div>');
					$('.listen-away marquee').text(lang.monitoring.alert.msg15+/*현재 상담사*/' ['+listenUser[$('#custPhoneContinue').val()].userName+'] '+lang.monitoring.alert.msg16/*내선번호*/+' ['+listenUser[$('#custPhoneContinue').val()].EXT+'] '+lang.monitoring.alert.msg17/*지속 감청 중입니다.*/);
				}else{
					$('.listen-away').remove();
					$('#bottomFixedPlayer').after('<div class="listen-away"><marquee></marquee></div>');
					$('.listen-away marquee').text(lang.monitoring.alert.msg15+/*현재 상담사*/' ['+listenUser[$('#custPhoneContinue').val()].userName+'] '+lang.monitoring.alert.msg16/*내선번호*/+' ['+listenUser[$('#custPhoneContinue').val()].EXT+'] '+lang.monitoring.alert.msg17/*지속 감청 중입니다.*/);
				}

				return;
			}
		}

    	//고객번호 감청 유지
		if($('#custPhoneContinue').val() != "" && $('.custPhoneContinueChk').is(":checked") == true && status == "calling" && playState == false && userInfo.RTP == "1"){
			if($('#custPhoneContinue').val().replace(/-/gi,'') == userInfo.CUSTNUM){
				listenEvent(userInfo.EXT, userInfo.CUSTNUM,userInfo.STIME,userInfo.SERVERIP);
				defineUserId = getUserInfo[userExt].userId;
				defineCustNum = userInfo.CUSTNUM;
				defineUserName =  getUserInfo[userExt].userName;
			}
		}
		
		if(recsee_mobile == "Y"){
			$(".status_card_wrap").css("cursor","normal");
		}

	}
}


//실시간으로 체크 값 받아서 상담원  show hide
function thisShowHide(){
	$('.onoffswitch-checkbox[name=selStatus]').each(function(i){
		// 아이디의 뒷부분을 소문자로 얻음
	   var targetClass = $(this).attr("id").replace(/^selStatus(.*?)$/, function(){return arguments[1].toLowerCase()});

	   // 처리할 데이터 얻기
	   var $targetCard = $(".agent_status_" + targetClass).parents(".status_card_wrap");

	   if($(this).is(':checked')){
		   $targetCard.show();
	   }else{
		   $targetCard.hide();
	   }
	})
}

function onOpenMonitoring(){
    var sendMessage = {
	    "head":    "RECSEE",
	    "code":      "MONITORING",
	    "kind":      "request",
	    "command":   "channel_list",
	    "body":      null,
	    "option":   {"cycle": 0}
    };

    onWebSocketSend(JSON.stringify(sendMessage));
}

// ajax부분
var MONITORING_TYPE = "ajax";
// 상태 모니터링 웹소켓/ajax 구분 부분
if(MONITORING_TYPE != "websocket"){
	// 웹소켓 함수 재정의
	var MONITORING_TIME = 3;

	function onOpenMonitoring(ajaxInfo){
	    $.ajax({
	        url: "http://URL:PORT/PATH".replace(/URL/, ajaxInfo.ip).replace(/:PORT/, ((ajaxInfo.port||"") == ""?"":":" + ajaxInfo.port)).replace(/\/PATH/, ajaxInfo.path),
	        type: "GET",
	        dataType: "text",
	        data: {t: new Date().getTime()},
	        complete: function(jRes) {

	            var rst = "";
	            try {
	               rst = JSON.parse(jRes.responseText);
	               window[ajaxInfo.messageFunction](rst);
	            } catch(e) {
	            	tryCatch(e)
	               // FIXME: json 파싱이 되면 작업하고 안되면 안하고
	            }
	        },
	        error: function() {
	        },
	        success: function() {
	        }
	    });
	}

	function onMessageMonitoring(rst){
		var items = rst;
		if(items.length > 0) {
			userLength = items.length;

			for(var i = 0; i < items.length; i++) {
				var extNo = items[i].EXT;

				//cti미사용이면 ringing일때 콜링으로 바꿔주기..이게 말이냐
				if("N"==ctiUse){
					if(items[i].CTI.toLowerCase()=="ringing"){
						items[i].CTI = "CALLING";
					}
				}

				if($("#"+extNo).length > 0 ) {
					 if(($("#"+extNo).find(".status_card").attr("id") != items[i].CTI.toLowerCase()) ||
							(items[i].RTP == "1" && items[i].CTI.toLowerCase()=="calling") ||
							(items[i].CTIREASON != $("#"+extNo).find('status_txt').text())){
						 window.requestAnimationFrame = (function(){
							 onUserInfoChange(items[i]);
						 })();
					}
					if(items[i].CTI.toLowerCase()!="logout"){
						 $("#"+extNo).find(".anget_duration").text(items[i].duration);
					}
					//if(telnoUse == "Y") {
						if($("#"+extNo).find(".anget_id").attr("id") != items[i].AGENTID &&
		                    $("#"+extNo).find(".anget_id").attr("id") != ''){
		                         $("#"+extNo).find(".anget_id").text(items[i].AGENTID);
		                 }
		
		                 if($("#"+extNo).find(".anget_custnum").attr("id") != items[i].CUSTNUM &&
		                    $("#"+extNo).find(".anget_custnum").attr("id") != ''){
		                	 var custnum = items[i].CUSTNUM;
		                	 if(custnum != null && custnum.length > 8){
		                		 //custnum = custnum.substring(0,3)+"-****-"+custnum.substring(7);
		                	 }		                	 
		                     $("#"+extNo).find(".anget_custnum").text(custnum);
		                 }
					//}
					$("#"+extNo).addClass("delUser");
				}else{
					userQue.enqueue(items[i]);
				}
			}

			// 텍스트 표출 : 임시
//			$('.agent_status_login').children('.status_txt').find('p').text("로그인");
//			$('.agent_status_logout').children('.status_txt').find('p').text("로그아웃");
//			$('.agent_status_calling').children('.status_txt').find('p').text("전화중");
//			$('.agent_status_ready').children('.status_txt').find('p').text("대기");
//			$('.agent_status_acw').children('.status_txt').find('p').text("후처리");
//			$('.agent_status_away').children('.status_txt').find('p').text("이석");

			$('.onoffswitch-label').click(function(event){
				event.stopPropagation();
				event.stopImmediatePropagation();
			})

			//내선번호 보이지 않아도 감청 되게 하기
			if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && playState == false){
				if (listenUser[$('#custPhoneContinue').val()] != undefined && listenUser[$('#custPhoneContinue').val()].RTP == "1" && listenUser[$('#custPhoneContinue').val()].CTI == "CALLING" ) {

					setTimeout(function(){
						listenEvent(listenUser[$('#custPhoneContinue').val()].EXT, listenUser[$('#custPhoneContinue').val()].CUSTNUM,listenUser[$('#custPhoneContinue').val()].STIME,listenUser[$('#custPhoneContinue').val()].SERVERIP);
					},500)

					if($('.listen-away').length <= 0){
						if(telnoUse=='Y'){
							 $('.listen-away').remove();
						 }
						$('#bottomFixedPlayer').after('<div class="listen-away"><marquee></marquee></div>');
						$('.listen-away marquee').text(lang.monitoring.alert.msg15+/*현재 상담사*/' ['+listenUser[$('#custPhoneContinue').val()].AGENTNAME+'] '+lang.monitoring.alert.msg16/*내선번호*/+' ['+listenUser[$('#custPhoneContinue').val()].EXT+'] '+lang.monitoring.alert.msg17/*지속 감청 중입니다.*/);
					}else{
						$('.listen-away').remove();
						$('#bottomFixedPlayer').after('<div class="listen-away"><marquee></marquee></div>');
						$('.listen-away marquee').text(lang.monitoring.alert.msg15+/*현재 상담사*/' ['+listenUser[$('#custPhoneContinue').val()].AGENTNAME+'] '+lang.monitoring.alert.msg16/*내선번호*/+' ['+listenUser[$('#custPhoneContinue').val()].EXT+'] '+lang.monitoring.alert.msg17/*지속 감청 중입니다.*/);
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
					SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
					SocketClient.Socket.close();
				}catch(e){
					tryCatch(e)
				}
				RealTimeExt ="";
			}*/

			// 청취 버튼 이벤트
			$('.status_card_wrap').on("click", function(event){
				event.stopPropagation();
				event.stopImmediatePropagation();

				if(event.target.className != "onoffswitch-checkbox"){
					// 청취 상태에서만 클릭클릭 클릭 현재 클릭된객체의 아이디(내선) 넘기기
					if($(this).find("div").hasClass("agent_status_calling") && !$(this).find("div").hasClass("realtime_slash_status")){
						listenEvent($(this)[0].id,$(this)[0].getAttribute('phonenum'),$(this)[0].getAttribute('callstime'),listenUser[$('#custPhoneContinue').val()].SERVERIP);
						defineUserId = $(this)[0].getElementsByClassName('anget_id')[0].textContent;
						defineCustNum = $(this)[0].getAttribute('phonenum');
						defineUserName = $(this)[0].getElementsByClassName('anget_name')[0].textContent;

						//	스위치 온이면 자동으로 오프
//						if($('#'+extNo)[0].getAttribute("stay-target") == "on"){
//							$("#"+extNo).find(".onoffswitch-label").click();
//						}
					}
				}
			});
		}
		$(".status_card_wrap").not(".delUser").remove()
		$(".status_card_wrap").removeClass("delUser")
	}
}

// 로딩 후 실행할 함수
$(function() {

//	// Display Mode 체인지 이벤트
//    $(".change_mode").each(function() {
//        var $parent = $(this);
//
//        // 탭 번호 달기
//        $parent.find(".mode_list").each(function(i) {
//            $(this).attr("data-target", i+1);
//        });
//    })

    $(".monitoring_contents").each(function() {
        var $parent = $(this);

        $parent.find(".mode_cont").each(function(i) {
            $(this).attr("data-target", i+1);
        });
    })

    $(".change_mode").find(".mode_list").click(function() {
        // 선택된 탭 재선택할 경우 중단
        if($(this).hasClass(".mode_list_active"))  return;

        var $parent = $(this).parents(".change_mode");
        var $tabno = $(this).attr("data-target");

        // 기존 탭 비활성화
        $parent.find(".mode_list_active").removeClass("mode_list_active");

        //그리드 뷰 페이지 이동
        if($tabno == 3){
        	window.parent.document.getElementById('pageFrame').src = contextPath+"/monitoring/realtime_grid";
        }

        // 클릭한 탭 활성화
        $(this).addClass("mode_list_active");
        //$tabno = ($tabno == 1 ? 2 : $tabno);
        //$(".monitoring_contents").find(".mode_cont[data-target="+$tabno+"]").show();
        viewDisplay()
    });

    $(".change_mode").each(function() {
        var $parent = $(this);

        // 첫번째 탭 기본 활성화
        // FIXME : 입체 모니터링 추가 되면 '0'으로 바꿀 것
        $parent.find(".mode_list:eq(1)").click();
    });

    checkOnOff();

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
				alertText(lang.monitoring.alert.msg18/*"메모 저장"*/,lang.monitoring.alert.msg19/*"메모 저장이 완료 되었습니다."*/);
				$("#realTimeMemoContents").text('')
				layer_popup_close();
			}
		});


    })
    checkOnOffExt();
    viewStatus();
    viewDisplay();
    onSettingBtnEvent();
    onListenAlwayBtnEvent();
})

function checkOnOffExt(){
	$('#custPhoneContinue').keyup(function(e){
		var keyCode = e.keyCode;

		if($('#statusSave').is(":checked") == true){
			$.ajax({
				url: contextPath+"/monitoring/realtimeSetting.do",
				data: {
					settingNum : $(this).val()
				},
				type: "POST",
				dataType: "json",
				async: false,
				success: function(jRes) {
				}
			});
		}
		if(keyCode != 13){
				$(this).val($(this).val().replace(/[^0-9]/g,""));
			var agent = $("#"+$('#custPhoneContinue').val());
			if(agent.length>0 && $('.office_stay_check').is(":checked") && $("#"+$('#custPhoneContinue').val()).find(".onoffswitch-checkbox").is(":checked") == false){
					agent.find(".onoffswitch-label").click();
			}
		}
	})
}

function viewStatus() {
	// 상태활성화 체크박스 변경 이벤트
	$('.onoffswitch-checkbox[name=selStatus]').change(function(){
	   // 아이디의 뒷부분을 소문자로 얻음
	   var targetClass = $(this).attr("id").replace(/^selStatus(.*?)$/, function(){return arguments[1].toLowerCase()});

	   // 처리할 데이터 얻기
	   var $targetCard = $(".agent_status_" + targetClass).parents(".status_card_wrap");
	   var $targetOffice = $(".obj_" + targetClass);

	   var $target = $.merge($targetCard, $targetOffice);

	   // 처리하기
	   if($(this).is(':checked')) {
	      $target.show();
	   } else {
	      $target.hide();
	   }
	});
}

function viewDisplay() {
	// display 체크박스 변경 이벤트
	$('.onoffswitch-checkbox[name=selDisplay]').change(function(){
	    // 아이디의 뒷부분을 소문자로 얻음
	    var targetClass = $(this).attr("id").replace(/^selDisplay(.*?)$/, function(){return arguments[1].toLowerCase()});

	    // 처리할 데이터 얻기
	    var $target = (function(targetClass) {
	       if(targetClass == "icon")                return $('.obj_agent');
	       else if(targetClass == "userid")         return $('.anget_id').parent('.agent_info');
	       else if(targetClass == "callnum")        return $('.callnum_info');
	       else if(targetClass == "callingtime")    return $('.time_info');
	    }(targetClass));
	    
	    // 처리하기
	    if($(this).is(':checked')) {
	       $target.show();
	    } else {
	       $target.hide();
	    }
	});
}

function onListenAlwayBtnEvent(){
	//모니터링 지속 감청 레이어
	var $listenAlwaysPannel = $('.listenAlwaysPannel');

	//지속 감청 레이어 열기 이벤트
    $("#btnListenAlway").click(function(){
    	$listenAlwaysPannel.show("slide", { direction: "right" }, "slow");
    })

	// 지속감청 닫기 이벤트
	$('#btnListenAlwayExit').click(function() {
		$listenAlwaysPannel.hide("slide", { direction: "right" }, "slow");
	})

}

function onSettingBtnEvent() {

	// 모니터링 셋팅 레이어
	var $monitoringSettingPannel = $('.monitoring_option');

	// 모니터링 셋팅 열기 이벤트
	$('#btnMonitSetOpen').click(function() {
		$monitoringSettingPannel.show("slide", { direction: "right" }, "slow");
	})

	// 모니터링 셋팅 닫기 이벤트
	$('#btnMonitSetExit').click(function() {
		$monitoringSettingPannel.hide("slide", { direction: "right" }, "slow");
	})

	// 모니터링 옵션 드랍다운 이벤트
	$('.setting_dropdown_btn').click(function() {
		var $dropdownTarget = $(this).parent().next('.setting_dropdown_cont');
		$dropdownTarget.slideToggle("slow");
	})

}

// 코드값으로부터 스테이터스 이름 반환 해주는 함수 (사용 중지)
function getStatusNameByCode(code){
	var status = "logout";
	switch(code) {
	    case "LOGOUT" : status = lang.monitoring.alert.msg9/*"로그아웃"*/;break;       	// "0"
	    case "LOGIN" : status = lang.monitoring.alert.msg8/*"로그인"*/; break;      	// "1"
	    case "READY" : status = lang.monitoring.alert.msg12/*"준비"*/; break;      	// "2"
	    case "CALLING" : status = lang.monitoring.alert.msg10/*"전화중"*/; break;   	// "3"
	    case "NOTREADY" : status = lang.monitoring.alert.msg14/*"이석"*/;  break;			// not ready
	    case "AFTERCALLWORK" : status = lang.monitoring.alert.msg11/*"후처리"*/; break;      		// "4"
	    default  : status = "logout"; break;
	}
	return status;
}

// db에서 사용자 정보 긁어와서 getUserInfo에 ext를 키값으로 하는 obj 생성
// 실감페이지에서 사용자 정보 매칭 할때 사용!
function getRuserInfo(){
	$.ajax({
		url:contextPath+"/monitoring/selectRuserInfo.do",
		data:{
			monitoring : "true",
			mobile : recsee_mobile
		},
		type:"POST",
		dataType:"json",
		sync : false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				var userList = jRes.resData.rUserList;
				getUserInfo = {};
				for(var i = 0 ; i < userList.length; i++ ){
					if(userList[i].rUseYn == "Y"){
						if(recsee_mobile == "Y"){ //모바일 렉시의 경우 내선정보가 없음 -> 폰번호를 기준으로
							getUserInfo[userList[i].userPhone] = userList[i]
						}else{
							getUserInfo[userList[i].extNo] = userList[i]
						}						
					}
				}
			}else{
				if($('#viewCardMode').html().trim() == ""){
					$('#viewCardMode').html("<div class='not_found'>"+lang.monitoring.alert.msg20+/*조회결과가 없습니다.*/"</div>")
				}
			}
		}
	});
}

// 자릿수 메꿔주는 함슈
function lpad(s, padLength, padString){

    while(s.length < padLength)
        s = padString + s;
    return s;
}

// 청취 기능
// extNo : 내선
function listenEvent(extNo, phoneNum,stime,serverIp){
	defineStime = stime;

	function doActive(extNo) {

		var clickedObj = $("#"+extNo);
		clickedObj.find('.agent_status_calling').append('<div class="agent_status_active"></div>');


		// active 활성화 된 오브젝트 수 구하기
		var otherActived = $('body').find('.status_card_wrap .agent_status_calling').children('.agent_status_active');
		// 활성화 된 오브젝트가 1개 초과라면
		if(otherActived.length > 1){
			// 클릭한 대상을 제외하고 전부 활성화 제거
			$('.agent_status_active').remove().not(clickedObj);
			// 클릭한 대상 활성화
			clickedObj.find('.agent_status_calling').append('<div class="agent_status_active"></div>')
		}


		var setExt = extNo;
		var setUserName = ((getUserInfo[extNo]) ? getUserInfo[extNo].userName : "");
		//realtime_rc.obj.controller.setPlayerInfo(extNo,setUserName,phoneNum);
	}
	if(RealTimeExt+"".trim() != ""){
		//	재생 중지
		try{
			realtime_rc.obj.audio[0].pause();
			realtime_rc.obj.audio[0].currentTime= 0;
			document.getElementById('realtimePlayer').getElementsByTagName('audio')[0].src="";
	    	$("#"+RealTimeExt+" > div").find('.agent_status_active').remove();
			SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
			SocketClient.Socket.close();
		}catch(e){
			tryCatch(e)
		}
		RealTimeExt ="";
	}else{
		SetServerinfo(serverIp);
		Init(extNo,serverIp);
		doActive(extNo);
	}
}



// 쿠키설정
function setCookie(cName, cValue, cDay){
	var expire = new Date();
	expire.setDate(expire.getDate() + cDay);
	var cookies = cName + "=" + escape(cValue) + "; path =/ ";
	if (typeof cDay != undefined){
		cookies += ";expires=" + expire.toGMTString() + ";";
	}
	document.cookie = cookies;
}
// 쿠키 불러오기
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


//스크롤 정지 함수
$.fn.scrollStopped = function(callback){
	var that = this, $this = $(this);
	$this.scroll(function(ev){
		clearTimeout($this.data('scrollTimeout'));
		$this.data('scrollTimeout',setTimeout(callback.bind(that),250,ev));
	});
};

function searchFilterLoad(){
	if(accessLevel != "A" && accessLevel != "B") {
		if(accessLevel == "M") {
			$('#codeFilter option[value="mBgCode"]').remove();
			$('#codeFilter').val('mMgCode').trigger('change');
		}
		if(accessLevel == "S") {
			$('#codeFilter option[value="mBgCode"]').remove();
			$('#codeFilter option[value="mMgCode"]').remove();
			$('#codeFilter').val('mSgCode').trigger('change');
		}
		if(accessLevel == "U") {
			$('#codeFilter option[value="mBgCode"]').remove();
			$('#codeFilter option[value="mMgCode"]').remove();
			$('#codeFilter option[value="mSgCode"]').remove();
			$('#codeFilter').val('searchAgentName').trigger('change');
		}
//		if(accessLevel == "U"){
//			$('#codeFilter').hide();
//			$('#mBgCode').val(userInfoJson.bgCode)
//			.trigger('change');
//			$('.select2').hide();
//		}
	}
}

function checkOnOff(){
	// 체크박스 공통 변경 이벤트
    $('.onoffswitch-checkbox').change(function(){
		// 체크박스 CSS 등 설정값 부분
		// input[type:checkbox] class
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
}

function onClickAction() {
	var selectedCode = $( "#codeFilter :selected" ).val();
	var data = $('#'+selectedCode).select2('data');
	alertChk = false;

	//	시스템 IP 선택 부분
	monitoringIpSelect = [];
	var systemOption = $("#systemCode option").length;
	for(var i=0;i<systemOption;i++){
		monitoringIpSelect.push($('#systemCode option')[i].getAttribute("ip"));
	}

	if(data.length == 0){
		clearInterval(monitoringInterval);
		clearInterval(userAdd);
		$("#viewCardMode").html("");
		alertText(lang.monitoring.alert.msg1/*"조회"*/,lang.monitoring.alert.msg2/*"검색 조건을 입력해 주세요."*/);
		return false;
	}

	if( data.length > 0 ){
		var selectorString = new Array();
		var selectedValues = new Array();

		for(var i = 0 ; i < data.length ; i++){
			switch(selectedCode){
			case "mBgCode":
				selectorString.push('[bgcode="'+data[i].id+'"]');
				selectedValues.push(data[i].id);
				selectCode = "BG";
				break;
			case "mMgCode":
				selectorString.push('[mgcode="'+data[i].id+'"]');
				selectedValues.push($('#mBgCode').val()+ "|" + data[i].id);
				selectCode = "MG";
				break;
			case "mSgCode":
				selectorString.push('[sgcode="'+data[i].id+'"]');
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
		$(".status_card_wrap").not(selectorString.toString()).remove()
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
		$(".status_card_wrap").remove();
	}
	//	조회 로그 남기기
	selectArr = selectedValues;
	selectCodeArr = selectorString;
	repeat();
    startRedisSocket();	// 레디스 임시
    setTimeout(function(){
	    if($('#viewCardMode').html().trim() == ""){
			alertText(lang.monitoring.alert.msg1/*"조회"*/,lang.monitoring.alert.msg3/*"조회된 값이 없습니다."*/);
		}
    },5000)
}
