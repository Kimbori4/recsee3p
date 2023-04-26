var getUserInfo = {}; // 전역 변수 ; 유저 정보 가져와서 담을 그릇 [키값은 내선번호]
var loadedOffice = [];
var realtime_rc;
var ajaxInfo;
var defineStime = "";
var defineCustNum = ""; // 콜인입 고객 전화번호
var defineUserName = ""; // 콜인입 상담사 이름
var defineUserId = ""; // 콜인ㅇㅂ
var monitoringInterval;

var startUserObj = {} // 처음 유저 한번 담을 그릇

var gridNumChk = 0; // 그리드 갯수 체크
var jsonObjGrid = {}; // json 추가

var appointmentListenExt; // 실감예약 내선
var appointmentListenPhone; // 실감예약 사용자 번호

var userQue;
var userAdd; // 유저 그리기
var userLength = 0;
var selectArr = [];				//조회 선택값
var selectCodeArr = [];
var selectCode = ""; // 분류 선택 값

var alertChk = false; // 조회 알림창 나타 낫었는지 확인 값

var listenUser={};					//	실감 지속 감청 팝업이 없어도 들을수 잇게 설정하기 위하여....

window.userList = []; // 사용자 정보
window.userBgList = []; // 대분류 코드 정보
window.userMgList = []; // 중분류 코드 정보
window.userSgList = []; // 소분류 코드 정보

/**
 * @param 실감예약
 *            할 타입 ext | phone
 * @param 실감예약
 *            할 대상 값
 */
function appointmentListen(type, value) {
	if (type == "ext") {
		appointmentListenExt = value;
		appointmentListenPhone = "";
	} else if (type == "value") {
		appointmentListenPhone = value;
		appointmentListenExt = "";
	}
}

$(function() {
	$('.onoffswitch-label').append('<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">on</span>');

	// 내선 번호 체크 함수
	$('.office_stay_check').change(
					function() {
						// DB 저장
						if ($(this).is(":checked") == true
								&& $('#statusSave').is(":checked") == true) {
							realtimeinsertType('E');
						} else if ($(this).is(":checked") == false
								&& $('#statusSave').is(":checked") == true) {
							realtimeinsertType('X');
						} else if ($('.custPhoneContinueChk').is(":checked") == true
								&& $('#statusSave').is(":checked") == true) {
							realtimeinsertType('C');
						} else if ($('.custPhoneContinueChk').is(":checked") == false
								&& $('#statusSave').is(":checked") == true) {
							realtimeinsertType('X');
						}
						try {
							if ($('.custPhoneContinueChk').is(":checked") == true)
								$('.custPhoneContinueChk').prop("checked",false)
							if ($(this).is(":checked") == true&& $("#custPhoneContinue").val() != null) {
								if ($('#gridStayRealtimeListen'+ $("#custPhoneContinue").val()).is(":checked") == false) {
									gridMonitoringView.cells($('#custPhoneContinue').val(), 11).cell	.getElementsByClassName('onoffswitch-label')[0].click()
								}
							} else if ($(this).is(":checked") == false && $("#custPhoneContinue").val() != null) {
								if ($('#gridStayRealtimeListen'+ $("#custPhoneContinue").val())	.is(":checked") == true) {
									gridMonitoringView.cells($('#custPhoneContinue').val(), 11).cell.getElementsByClassName('onoffswitch-label')[0].click()
								}
							}
						} catch (e) {
							tryCatch(e)
						}
					})

	// 고객 번호 체크 함수
	$('.custPhoneContinueChk').change(
			function() {
				if ($(this).is(":checked") == true
						&& $('#statusSave').is(":checked") == true) {
					realtimeinsertType('C');
				} else if ($(this).is(":checked") == false
						&& $('#statusSave').is(":checked") == true) {
					realtimeinsertType('X');
				} else if ($('.office_stay_check').is(":checked") == true
						&& $('#statusSave').is(":checked") == true) {
					realtimeinsertType('E');
				} else if ($('.office_stay_check').is(":checked") == false
						&& $('#statusSave').is(":checked") == true) {
					realtimeinsertType('X');
				}
				try {
					if ($('.office_stay_check').is(":checked") == true)
						$('.office_stay_check').prop("checked", false)
					gridMonitoringView.cells($('#custPhoneContinue').val(), 11).setAttribute("stay-target", 'off');
					$('#viewGridMode .onoffswitch-checkbox').removeAttr('checked');
					$('#viewGridMode .onoffswitch-checkbox').parents().removeAttr('stay-target');
				} catch (e) {
					tryCatch(e)
				}
			})

})

addLoadEvent(monitroingLoad);

function monitroingLoad() {
	onLoad();
	viewDisplay();
	onListenAlwayBtnEvent();
	 select2InputFilter();
}

/** **********레디스 임시 *************** */
function startRedisSocket() {
	clearInterval(monitoringInterval);
	clearInterval(userAdd);
	MONITORING_TIME = 3;

	monitoringInterval = window.setInterval(function() {
		repeat()
	}, MONITORING_TIME * 1000);

	userAdd = window.setInterval(function() {
		if (userQue.front() == undefined) {
			userQue.dequeue();
			return;
		}

		if (gridMonitoringView.getRowById(userQue.front().EXT) == null) {
			onUserInfoAdd(userQue.front());
		}
		userQue.dequeue();
	}, 1);

}

function repeat() {
	$.ajax({
		url : contextPath + "/monitoring/gridMonitoringSelect.do",
		data : {
			"SYSID" : $('#systemCode option:selected')[0].getAttribute('ip')
		,	"selectCode" : selectCode
		,	"selectArr" : selectArr.toString()
		},
		type : "POST",
		dataType : "json",
		success : function(jRes) {
			if (jRes.success == "Y") {
				var searchMonitoring = [];

				var responseData = jRes.resData.agentExt;

				var Code = [];
				if (selectCodeArr.length > 0) {
					for (var i = 0; i < selectCodeArr.length; i++) {
						var k = selectCodeArr[i].toString().split("\"")
						Code.push(k[1]);
					}
				}
				for (var i = 0; i < responseData.length; i++) {
					if (Code.length > 0) {
						for (var j = 0; j < Code.length; j++) {
							switch (selectCode) {
							case "BG":
								if (responseData[i].BGCODE == Code[j])
									searchMonitoring.push(responseData[i]);
								break;
							case "MG":
								if (responseData[i].MGCODE == Code[j])
									searchMonitoring.push(responseData[i]);
								break;
							case "SG":
								if (responseData[i].SGCODE == Code[j])
									searchMonitoring.push(responseData[i]);
								break;
							case "NAME":
								if (responseData[i].AGENTNAME == Code[j])
									searchMonitoring.push(responseData[i]);
								break;
							case "NUM":
								if (responseData[i].AGENTID == Code[j])
									searchMonitoring.push(responseData[i]);
								break;
							case "EXT":
								if (responseData[i].EXT == Code[j])
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
					} else {
						if (getUserInfo[responseData[i].EXT] != undefined)
							searchMonitoring.push(responseData[i]);
					}

				}

				// 출력
				// 조회 없을 시 알림창
				if (searchMonitoring.length == 0 && alertChk == false) {
					progress.off();
					alertChk = true;
					alertText(lang.monitoring.alert.msg1/*"조회"*/,lang.monitoring.alert.msg3/*"조회된 값이 없습니다."*/);
				}
				onMessageMonitoring(searchMonitoring);
				searchMonitoring = [];
			}
		}
	})
	return true;
}

/** **********레디스 임시 *************** */
// IE9 Transport 오류 관련 처리
jQuery.support.cors = true;

function getSelect2Datas() {
	var selectedCode = $("#codeFilter :selected").val()
	var data = $('#' + selectedCode).select2('data');
	var dataId = [];
	for (var i = 0; i < data.length; i++) {
		dataId.push(data[i].id)
	}
	var o = {
		"data" : dataId,
		"selectedCode" : selectedCode
	}
	return o;
}

function onLoad() {
	//모바일인 경우 내선번호 검색 숨기기
	if(recsee_mobile == "Y"){
		$("select option[value*='searchAgentExt']").hide();
	}
	
	// 조회 버튼 클릭 시
	$("#mSearchBtn").on("click", function() {
		userLength = 0;
		alertChk = false;

		userQue = new Queue();
		jsonObjGrid.rows = [];
		var selectedCode = $("#codeFilter :selected").val()
		var data = $('#' + selectedCode).select2('data');

		if (data.length == 0) {
			clearInterval(monitoringInterval);
			clearInterval(userAdd);
			alertText(lang.monitoring.alert.msg1/*"조회"*/,lang.monitoring.alert.msg2/*"검색 조건을 입력해 주세요."*/);
			return false;
		}
		gridMonitoringView.clearAll();
		if (data.length > 0) {
			var selectorString = new Array();
    		var selectedValues = new Array();

			for (var i = 0; i < data.length; i++) {
				switch (selectedCode) {
				case "mBgCode":
					selectorString.push('[bgcode="' + data[i].id + '"]');
					selectedValues.push(data[i].id);
					selectCode = "BG";
					//userLength = Object.keys(getUserInfo).length;
					getUserLengthReal("bgCode", data[i].id);
					break;
				case "mMgCode":
					selectorString.push('[mgcode="' + data[i].id + '"]');
					selectedValues.push($('#mBgCode').val()+ "|" + data[i].id);
					selectCode = "MG";
					getUserLengthReal("mgCode", data[i].id);
					break;
				case "mSgCode":
					selectorString.push('[sgcode="' + data[i].id + '"]');
					selectedValues.push($('#mBgCode').val()+ "|" + $('#mMgCode').val()+ "|" + data[i].id);
					selectCode = "SG";
					getUserLengthReal("sgCode", data[i].id);
					break;
				case "searchAgentName":
					selectorString.push('[name="' + data[i].id + '"]');
					selectedValues.push(data[i].id);
					selectCode = "NAME";
					getUserLengthReal("name", data[i].id);
					break;
				case "searchAgentNum":
					selectorString.push('[num="' + data[i].id + '"]');
					selectedValues.push(data[i].id);
					selectCode = "NUM";
					getUserLengthReal("num", data[i].id);
					break;
				case "searchAgentExt":
					selectorString.push('[ext="' + data[i].id + '"]');
					selectedValues.push(data[i].id);
					selectCode = "EXT";
					getUserLengthReal("ext", data[i].id);
					break;
				}
			}
		}
    	selectArr = selectedValues;
		selectCodeArr = selectorString;

		/*
		 * var bgCodeArray = new Array(); var mgCodeArray = new Array(); var
		 * sgCodeArray = new Array();
		 *
		 * for(var i = 0 ; i < data.length ; i++){ sgCodeArray.push(data[i].id); }
		 *
		 * gridMonitoringView.forEachRow(function(id){ switch(selectedCode){
		 * case "mBgCode": if (
		 * !(bgCodeArray.indexOf(this.cells(id,7).getValue()) > -1) )
		 * this.deleteRow(id); break; case "mMgCode": if (
		 * !(mgCodeArray.indexOf(this.cells(id,8).getValue()) > -1) )
		 * this.deleteRow(id); break; case "mSgCode": if (
		 * !(sgCodeArray.indexOf(this.cells(id,9).getValue()) > -1) )
		 * this.deleteRow(id); break; } });
		 */
		repeat();

		// 클릭시 조직 자기 권한 갯수
		if (accessLevel != "A") {
			if (accessLevel == "B") {
				userLengthCnt("bgCode");
			}
			if (accessLevel == "M") {
				if (mgCode != userInfoJson.mgCode) {
					userLengthCnt("mgCode");
				}
			}
			if (accessLevel == "S") {
				if (sgCode != userInfoJson.sgCode) {
					userLengthCnt("sgCode");
				}
			}
			if (accessLevel == "U") {
				userLengthCnt("user");
			}
		}
		startRedisSocket(); // 레디스 임시
	});

	// 사용자 정보 담아 놓기
	getRuserInfo()

	// 분류 코드 필터링
	$("#codeFilter").change(
			function() {
				$("#mSgCode, #mMgCode").empty()
				$(".select2").remove();
				$(".codeList").hide().removeAttr("multiple").removeClass("select2-hidden-accessible");
				var selectedCode = $("#codeFilter :selected").val()
				switch (selectedCode) {
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
					selectOrganizationLoad($("#searchAgentName"), "agentName",undefined, undefined, undefined, true)
					break;
				case "searchAgentNum":
					groupSearch(lang.monitoring.alert.msg6/*"사원번호를 입력해주세요"*/);
					selectOrganizationLoad($("#searchAgentNum"), "agentNum",undefined, undefined, undefined, true)
					break;
				case "searchAgentExt":
					groupSearch(lang.monitoring.alert.msg7/*"내선번호를 입력해주세요"*/);
					selectOrganizationLoad($("#searchAgentExt"), "agentExt",undefined, undefined, undefined, true)
					break;
				}
				function groupSearch(txt) {
					$("#" + selectedCode).attr("multiple", "multiple").select2({
							placeholder : txt,
							allowClear : true
						,   dropdownAutoWidth : true
						,	width : 'auto'
					});
				}
			});

	// 대분류
	selectOrganizationLoad($("#mBgCode"), "bgCode", undefined, undefined,undefined, true, "callCenter")

	$("#mBgCode").attr("multiple", "multiple").css("width", "500").select2({
		placeholder: lang.statistics.js.alert.msg4,/*"대상 그룹을 선택 해 주세요"*/
		allowClear : true
	});

	// 대분류 변화시 중분류 로드
	$("#mBgCode").change(
			function() {
				$("#mSgCode").empty().val(null).trigger("change");
				selectOrganizationLoad($("#mMgCode"), "mgCode", $(this).val(),undefined, undefined, true)
				// 중분류 및의 소분류가 있다면 디폴트로 로드
			});

	// 중분류 변화시 소분류 로드
	$("#mMgCode").change(
			function() {
				selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(), undefined, true)
			});

	//	권한에 따른 초기 필터 설정
	searchFilterLoad();

	// 시스템 변환시 출력 변경
	$("#systemCode").change(function() {
		$(".now_system_info").text($("#systemCode option:selected").text())
		ajaxInfo.ip = $("#systemCode option:selected").attr("ip");
		monitoringIP = $("#systemCode option:selected").attr("ip");

		// 그리드 모니터링
		gridMonitoringView.clearAll();
	});

	dhx4.ajax.get(
			contextPath + "/monitoring/realtime_init.json",
					function(response) {
						var realTime = jQuery
								.parseJSON(response.xmlDoc.responseText);

						if (realTime.system.length > 0) {

							systemPath = realTime.system;

							for (var i = 0; i < realTime.system.length; i++) {
								$("#systemCode")
										.append(
												'<option ip='+ realTime.system[i].system_ip	+ ' value='+ realTime.system[i].system_code
														+ (i == 0 ? ' selected="selected"'	: '')	+ ' >'	+ realTime.system[i].system_name	+ '</option>');
								if (i == 0) {
									monitoringIP = realTime.system[i].system_ip;
									systemName = realTime.system[i].system_name;
									key = realTime.system[i].system_key;
									$(".now_system_info").text(systemName)
								}
							}
						}

						if (realTime.realtime.length > 0) {
							for (var i = 0; i < realTime.realtime.length; i++) {
								for (objItem in realTime.realtime[i]) {
									switch (objItem) {
									case "login":
									case "ready":
									case "calling":
									case "acw":
									case "logout":
										if (realTime.realtime[i][objItem] == "1")
											$('.swraper[input-name=user_status_'	+ objItem + ']')	.trigger('click');
										break;
									case "img":
									case "userId":
									case "calltime":
									case "phoneNo":
										if (realTime.realtime[i][objItem] == "1")
											$('.swraper[input-name=info_'+ objItem + ']').trigger('click');
										break;
									}
								}
							}
						}

					});

	// 하단 고정 플레이어 로드
	realtime_rc = new RecseePlayer({
		target : "#bottomFixedPlayer",
		"btnDownFile" : false // 전체파일 다운로드 버튼 사용 여부 (다운로드
		// 시 암호화 된 파일 다운로드)
		,
		"btnUpFile" : false // 파일 업로드 청취 버튼 사용 여부
		// (플레이어에서 다운로드 한 파일만 청취 가능)
		,
		"btnPlaySection" : false // 재생 구간 설정 버튼 사용 여부
		,
		"btnTimeSection" : false // 사용자 정의 구간 설정 버튼 사용 여부
		,
		"btnMouseSection" : false // 마우스로 구간 설정 버튼 사용 여부
		,"wave" : true // 웨이브 표출여부 (비 활성시 구간 지정 기능
		// 사용 불가)
		,"btnDown" : false // 구간 설정 시 구간에 대한 다운로드 기능
		// (다운로드 시 암호화 된 파일 다운로드)
		,"btnMute" : false // 구간 설정 시 묵음처리 기능
		,"btnDel" : false // 구간 설정 시 제외시키기 기능
		,"moveTime" : 5 // 플레이어 좌우 이동시 증감할 시간; 기본 5초
		,"list" : false // 플레이 리스트 사용 유무
		,"dual" : false // 화자분리 플레이어 사용 유무
		,"memo" : false // 메모 사용 유무
		,"requestIp" : $("#ip").val() // 통신 IP
		,"requestPort" : $("#port").val() // 통신 Port
		,"log" : true // 다운로드 로그 사용 유무
		,"replay" : false,
		audio : true,
		video : false,
		realtime : true
	// wave: (param.mode == "a" || param.mode == "wave")
	});

	checkOnOffExt();
	$("#bottomFixedPlayer").draggable({
		handle : ".player_wave.audioWave"
	})
}

function checkOnOffExt() {
	$('#custPhoneContinue').blur(function(e) {
				if ($('#statusSave').is(":checked") == true) {
					$.ajax({
						url : contextPath
								+ "/monitoring/realtimeSetting.do",
						data : {
							settingNum : $(this).val()
						},
						type : "POST",
						dataType : "json",
						async : false,
						success : function(jRes) {
						}
					});
				}

		$(this).val($(this).val().replace(/[^0-9]/g, ""));
//			if ($('.office_stay_check').is(":checked")) {
//					try {
//						gridMonitoringView.cells($('#custPhoneContinue').val(),11).cell.getElementsByClassName('onoffswitch-label')[0].click()
//					} catch (e) {
//						tryCatch(e)
//					}
//				}
		})
		
		$('#custPhoneContinue').keyup(function(e){
			var keyCode = e.keyCode;
			
			if(keyCode != 13){
				$(this).val($(this).val().replace(/[^0-9]/g,""));
			var agent = $("#"+$('#custPhoneContinue').val());
			if($('.office_stay_check').is(":checked") && $('#gridStayRealtimeListen'+ $('#custPhoneContinue').val()).is(":checked") == false){
					try {
						gridMonitoringView.cells($('#custPhoneContinue').val(),11).cell.getElementsByClassName('onoffswitch-label')[0].click()
					} catch (e) {
						tryCatch(e)
					}
				}
			}
		})
}

function userLengthCnt(Code) {
	var accessCnt = 0;

	switch (Code) {
	case "bgCode":
		for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
			if (getUserInfo[Object.keys(getUserInfo)[i]].bgCode == userInfoJson.bgCode) {
				accessCnt++;
			}
		}
		break;
	case "mgCode":
		for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
			if (getUserInfo[Object.keys(getUserInfo)[i]].mgCode == userInfoJson.mgCode) {
				for (var j = 0; j <= $('#mSgCode').val().length; j++) {
					if($('#mSgCode').val()[j] == getUserInfo[Object.keys(getUserInfo)[i]].sgCode)
						accessCnt++;
				}
			}
		}
		break;
	case "sgCode":
		for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
			if (getUserInfo[Object.keys(getUserInfo)[i]].sgCode == userInfoJson.sgCode) {
				accessCnt++;
			}
		}
		break;
	case "user":
		accessCnt = 1;
		break;
	}
	userLength = accessCnt;
}

function onListenAlwayBtnEvent() {
	// 모니터링 지속 감청 레이어
	var $listenAlwaysPannel = $('.listenAlwaysPannel');

	// 모니터링 셋팅 레이어
	var $monitoringSettingPannel = $('.monitoring_option');

	// 지속 감청 레이어 열기 이벤트
	$("#btnListenAlway").click(function() {
		$monitoringSettingPannel.hide("slide", { direction: "right" }, "slow");
		$listenAlwaysPannel.show("slide", {direction : "right"}, "slow");
	})

	// 지속감청 닫기 이벤트
	$('#btnListenAlwayExit').click(function() {
		$listenAlwaysPannel.hide("slide", {direction : "right"}, "slow");
	})

}

// 모니터링 오브젝트 생성 함수
function onUserInfoAdd(userInfo, userInfoArray) {
	var status = userInfo.CTI.toLowerCase();
	var extNo = userInfo.EXT;
	var stime = userInfo.STIME;
	var custNum = userInfo.CUSTNUM;
	var rtpCode = userInfo.RTP;

	var statusTxt = userInfo.CTI.toLowerCase();
	if (statusTxt == "login") {
		statusTxt = lang.monitoring.alert.msg8/*"로그인"*/;
	} else if (statusTxt == "logout") {
		statusTxt = lang.monitoring.alert.msg9/*"로그아웃"*/;
	} else if (statusTxt == "calling") {
		statusTxt = lang.monitoring.alert.msg10/*"전화중"*/;
	} else if (statusTxt == "aftercallwork") {
		statusTxt = lang.monitoring.alert.msg11/*"후처리"*/;
	} else if (statusTxt == "ready") {
		statusTxt = lang.monitoring.alert.msg12/*"준비"*/;
	} else if (statusTxt == "ringing"){
		statusTxt = lang.monitoring.alert.msg13/*"연결중"*/;
	}else if(statusTxt == "offering"){
		statusTxt = lang.monitoring.alert.msg9/*"로그아웃"*/;
	} else {
		/*statusTxt = userInfo.CTIREASON;*/
		statusTxt = lang.monitoring.alert.msg14/*"이석"*/;
	}

	var statusString = "<div class='grid_status grid_status_login'>로그인</div>".replace(/login/gi, status)
	statusString = statusString.replace("로그인", statusTxt)
//	var listenString = (status == "calling" && userInfo.RTP != "0") ? "<button id='btn"+ extNo
//			+ "' class='ui_btn_white icon_btn_historySearch_white' style='background-color: rgb(217,145,3);color: rgb(255, 255, 255);border-top-color: #d99103;border-bottom-color: #d99103;'>Listen</button>"
//			: "미사용";
	var listenString = (status == "calling" && rtpCode == "1") ? "<button id='btn"+ extNo
			+ "' class='ui_btn_white' style='background-color: rgb(217,145,3);color: rgb(255, 255, 255);border-top-color: #d99103;border-bottom-color: #d99103;'>Listen</button>"
			: "미사용";
			/*"<button id='btn"+ extNo+ "' class='ui_btn_white ui_main_btn_flat style='background-color: rgb(217,145,3);color: rgb(255, 255, 255);border-top-color: #d99103;border-bottom-color: #d99103;' user-stime="
			+ stime + ">Listen</button>"	: "";*/
	// var callTime = (status == "calling") ? timeDiff(userInfo.STIME) : "";
	// var callTime = (status == "calling") ? userInfo.STIME : "";
	var callTime = userInfo.duration;
	var rowIds = gridMonitoringView.getAllRowIds().split(",");
	var userName = (getUserInfo[extNo]) ? getUserInfo[extNo].userName : "";
	var userId = (getUserInfo[extNo]) ? getUserInfo[extNo].userId : "";
	var bgCode = (getUserInfo[extNo]) ? getUserInfo[extNo].bgCode : "";
	var mgCode = (getUserInfo[extNo]) ? getUserInfo[extNo].mgCode : "";
	var sgCode = (getUserInfo[extNo]) ? getUserInfo[extNo].sgCode : "";
	var bgName = (getUserInfo[extNo]) ? getUserInfo[extNo].bgName : "";
	var mgName = (getUserInfo[extNo]) ? getUserInfo[extNo].mgName : "";
	var sgName = (getUserInfo[extNo]) ? getUserInfo[extNo].sgName : "";
	
	var stayListen = ""
			+ '<div class="option_check">'
			+ '<div class="onoffswitch">'
			+ '<input type="checkbox" ext-no = "'
			+ extNo
			+ '" name="gridStayRealtime'
			+ extNo
			+ '" class="onoffswitch-checkbox" id="gridStayRealtimeListen'
			+ extNo
			+ '"/>'
			+ '<label class="onoffswitch-label" for="gridStayRealtimeListen'
			+ extNo
			+ '">'
			+ '<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">off</span>'
			+ '</label>' + '</div>' + '</div>';

	var data = getSelect2Datas();

	var sgCodeArray = [];
	for (var i = 0; i < data.length; i++) {
		sgCodeArray.push(data[i].id);
	}
	if (accessLevel != "A") {
		if (accessLevel == "B") {
			if (bgCode != userInfoJson.bgCode) {
				return;
			}
		}
		if (accessLevel == "M") {
			if (mgCode != userInfoJson.mgCode) {
				return;
			}
		}
		if (accessLevel == "S") {
			if (sgCode != userInfoJson.sgCode) {
				return;
			}
		}
		if (accessLevel == "U") {
			if (userId != userInfoJson.userId) {
				return;
			}
		}
	}

	if (gridMonitoringView.doesRowExist(extNo) == false) {
		gridNumChk++;
		if (userInfo.BGCODE != "BGIVR") {
			jsonObjGrid.rows.push({
				id : extNo,
				data : [ extNo // 내선
				, userId // 아이디
				, userName // 이름
				, statusString // 상태
				, callTime // 콜타임
				, phoneFomatter(userInfo.CUSTNUM,'masking') // 인입버노
				, listenString // 청취 버튼
				, bgName // bg코드
				, mgName // mg코드
				, sgName // sg코드
				, "0" // 청취중 유무 true:1|false:0
				, stayListen // 실감 유지
				, rtpCode, userInfo.STIME ]
			})
		}
	}
	if (userLength == gridNumChk) {
		userQue = new Queue();
		gridNumChk = 0;
		gridMonitoringView.clearAll();
		// gridMonitoringView.init();
		// console.log(gridMonitoringView)
		gridMonitoringView.parse(jsonObjGrid, "json");
		clearInterval(userAdd);

		// 감청 유지 버튼 체크 함수
		// @ezra
		// on-off에 따른 attribute 달기 (for 감청 유지)
		$('#viewGridMode .onoffswitch-checkbox').off("change")
		$('#viewGridMode .onoffswitch-checkbox').on("change",function() {
					if ($(this).is(':checked') == true) {
						gridMonitoringView.cells($(this).attr("ext-no"),11).setAttribute("stay-target","on")
						$('#viewGridMode .onoffswitch-checkbox').prop('checked', false)
						$('#gridStayRealtimeListen' + $(this).attr('ext-no')).prop('checked', true);
						$(this).parent().find('.onoffswitch-switch').text('on');
						$('.office_stay_check').prop("checked",true);
						if($('.office_stay_check').is(":checked") == true ){
							$("#custPhoneContinue").val(gridMonitoringView.cells(gridMonitoringView.getSelectedRowId(), 0).getTitle().trim());

							$.ajax({
								url: contextPath+"/monitoring/realtimeSetting.do",
								data: {
									settingNum : gridMonitoringView.cells(gridMonitoringView.getSelectedRowId(), 0).getTitle().trim()
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
						}
					}else{
						$('.office_stay_check').prop("checked",false);
						$("#custPhoneContinue").val("");
						
						$(this).parent().find('.onoffswitch-switch').text('off');
						$(this).parents().removeAttr('stay-target');
		    			gridMonitoringView.cells($(this).attr("ext-no"),11).setAttribute("stay-target","off")
//						if($("#custPhoneContinue").val() != null){
//							return;
//						}
						if($('.office_stay_check').is(":checked") == true ){
							$(this).parent().find('.onoffswitch-switch').text('off');
								$("#custPhoneContinue").val("");
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
					}
				});

		if ($('#statusSave').is(":checked") == true && $('.office_stay_check').is(":checked") == true) {
			try{
				gridMonitoringView.cells($('#custPhoneContinue').val(), 11).cell	.getElementsByClassName('onoffswitch-label')[0].click()
			}catch(e){tryCatch(e)}
		}

		// 감청 유지 버튼 체크 함수
		var rowIdCnt = gridMonitoringView.getAllItemIds().split(',');
		for (var i = 0; i < rowIdCnt.length; i++) {
			var status = gridMonitoringView.cells(rowIdCnt[i], 3).getTitle().trim();
			
			if(userInfo.RECORD != "Y" && status!=lang.monitoring.alert.msg10){
				var k = gridMonitoringView.getRowById(rowIdCnt[i]).children;
				k[6].classList.add("realtime_use_false");
			}else{
				if (status == lang.monitoring.alert.msg10 && gridMonitoringView.cells(rowIdCnt[i], 12).getTitle() == "1") {
					// onUserInfoChange(startUserObj[rowIdCnt[i]]);
				}
				if (status == lang.monitoring.alert.msg10 && gridMonitoringView.cells(rowIdCnt[i], 12).getTitle() == "0") {
					var k = gridMonitoringView.getRowById(rowIdCnt[i]).children;
					k[6].classList.add("realtime_cancel_status");
				}
			}
		}
		
		ui_controller();
	}
}

// 상담원 실시간 상태 받아오는 함수
function onUserInfoChange(userInfo) {
	var status = userInfo.CTI.toLowerCase();
	var extNo = userInfo.EXT;
	var stime = userInfo.STIME;
	//var custNum = phoneFomatter(userInfo.CUSTNUM,'masking');
	var custNum = userInfo.CUSTNUM;
	var rtpCode = userInfo.RTP;

	var statusTxt = userInfo.CTI.toLowerCase();
	if (statusTxt == "login") {
		statusTxt = lang.monitoring.alert.msg8/*"로그인"*/;
	} else if (statusTxt == "logout") {
		statusTxt = lang.monitoring.alert.msg9/*"로그아웃"*/;
	} else if (statusTxt == "calling") {
		statusTxt = lang.monitoring.alert.msg10/*"전화중"*/;
	} else if (statusTxt == "aftercallwork") {
		statusTxt = lang.monitoring.alert.msg11/*"후처리"*/;
	} else if (statusTxt == "ready") {
		statusTxt = lang.monitoring.alert.msg12/*"준비"*/;
	} else if (statusTxt == "ringing"){
		statusTxt = lang.monitoring.alert.msg13/*"연결중"*/;
	}else {
		/*statusTxt = userInfo.CTIREASON;*/
		statusTxt = lang.monitoring.alert.msg14/*"이석"*/;
	}

	if(statusTxt=='로그아웃')
		custNum='';

	var statusString = "<div class='grid_status grid_status_login'>로그인</div>".replace(/login/gi, status);
	statusString = statusString.replace(/로그인/gi, statusTxt);
	var listenString = (status == "calling" && rtpCode == "1") ? "<button id='btn"+ extNo
			+ "' class='ui_btn_white' style='background-color: rgb(217,145,3);color: rgb(255, 255, 255);border-top-color: #d99103;border-bottom-color: #d99103;'>Listen</button>"
			: "미사용";
	// var callTime = (status == "calling" && rtpCode == "1") ?
	// timeDiff(userInfo.STIME) : "";
//	var callTime = (status == "calling" && rtpCode == "1") ? userInfo.STIME
//			: "";
	var callTime = userInfo.duration;
	if(userInfo.CTI.toLowerCase() == "logout"){
		callTime = null;
	}
	// if(status == "calling" && rtpCode=="1" ){
	// var k = setInterval(function(){
	// try{
	// if(gridMonitoringView.cells(extNo,3).getTitle()=="전화중")
	// gridMonitoringView.cells(extNo,4).setValue(timeDiff(userInfo.STIME));
	// else
	// clearInterval(k);
	// }catch(e){clearInterval(k); }
	// },1000);
	// }

	gridMonitoringView.cells(extNo, 3).setValue(statusString)
	gridMonitoringView.cells(extNo, 4).setValue(callTime)
	gridMonitoringView.cells(extNo, 5).setValue(custNum)
	gridMonitoringView.cells(extNo, 6).setValue(listenString)
	//ui_controller();
	
	//채널 정보 들고온 후 녹취 안하면 표출 안해주기..
	//TODO
	if(userInfo.RECORD != "Y"&&status != "calling"){
		var k = gridMonitoringView.getRowById(extNo).children;
		k[6].classList.add("realtime_use_false");
	}else{
		if (status == "calling" && rtpCode == "0") {
			var k = gridMonitoringView.getRowById(extNo).children;
			k[6].classList.add("realtime_cancel_status");
		}
		if ((status != "calling" && rtpCode == "0")	|| (status == "calling" && rtpCode == "1")) {
			var k = gridMonitoringView.getRowById(extNo).children;
			k[6].classList.remove("realtime_cancel_status");
		}
	}

	$("#btn" + extNo).unbind("click");
	$("#btn" + extNo).bind("click", function() {
		if (gridMonitoringView.cells(extNo, 3).getTitle() == lang.monitoring.alert.msg10) {
			defineUserName = gridMonitoringView.cells(extNo, 2).getTitle();
			listenEvent(extNo, custNum, stime, userInfo.SERVERIP);
		}
	});

	thisShowHide(extNo);

	// 실감중에 정보 새로 받아오면... 처리해주기
	// if(RealTimeExt == extNo){
	// $(".gridListening").removeClass("gridListening")
	// $(gridMonitoringView.getRowById(extNo)).addClass("gridListening")
	// }else{
	// $(gridMonitoringView.getRowById(extNo)).removeClass("gridListening")
	// }

	// @ezra
	// 실감 유지 on-off 및 상태 체크하여 실행
	if (gridMonitoringView.cells(extNo, 11).getAttribute("stay-target") == "on"	&& gridMonitoringView.cells(extNo, 3).getTitle() == lang.monitoring.alert.msg10	&& gridMonitoringView.cells(extNo, 12).getTitle() == "1" && playState == false) { // on일 경우
		defineUserName = gridMonitoringView.cells(extNo, 2).getTitle();
		//listenEvent(extNo, custNum, stime);
	}

	// 고객번호 감청 유지
	if ($('#custPhoneContinue').val() != ""	&& $('.custPhoneContinueChk').is(":checked") == true && status == "calling" && playState == false && rtpCode == "1") {
		if ($('#custPhoneContinue').val().replace(/-/gi, '') == userInfo.CUSTNUM) {
			listenEvent(extNo, custNum, stime, userInfo.SERVERIP);
			defineUserName = gridMonitoringView.cells(extNo, 2).getTitle();
		}
	}
}

// 실시간으로 체크 값 받아서 상담원 show hide
function thisShowHide(ext) {
	$('.onoffswitch-checkbox[name=selStatus]').each(
					function(i) {
						// 아이디의 뒷부분을 소문자로 얻음
						var targetClass = $(this).attr("id").replace(
								/^selStatus(.*?)$/, function() {
									return arguments[1].toLowerCase()
								});

						if ($(this).is(':checked')) {
							var grid = gridMonitoringView.getAllItemIds().split(',');
							var gridStr = gridMonitoringView.cells(ext, 3).getValue();
							var state = gridStr.replace(/^(.*)grid_status_(.?)/, function() {return arguments[2]}).replace(/"(.*)/, "");
							if (targetClass == state){
								try{
									gridMonitoringView.getRowById(ext).style.display = "";
								}catch(e){tryCatch(e)}
							}
						} else {
							var grid = gridMonitoringView.getAllItemIds().split(',');
							var gridStr = gridMonitoringView.cells(ext, 3).getValue();
							var state = gridStr.replace(/^(.*)grid_status_(.?)/, function() {return arguments[2]}).replace(/"(.*)/, "");
							if (targetClass == state){
								try{
									gridMonitoringView.getRowById(ext).style.display = "none";
								}catch(e){tryCatch(e)}
							}
						}
					})
}

function onOpenMonitoring() {
	var sendMessage = {
			"head" : "RECSEE",
			"code" : "MONITORING",
			"kind" : "request",
			"command" : "channel_list",
			"body" : null,
			"option" : {
			"cycle" : 0
		}
	};

	onWebSocketSend(JSON.stringify(sendMessage));
}

// ajax부분
var MONITORING_TYPE = "ajax";
// 상태 모니터링 웹소켓/ajax 구분 부분
if (MONITORING_TYPE != "websocket") {
	// 웹소켓 함수 재정의
	var MONITORING_TIME = 3;
	function startWebSocket() {
		ajaxInfo = {
			ip : monitoringIP,
			port : "9981",
			path : "",
			openFunction : "onOpenMonitoring",
			messageFunction : "onMessageMonitoring"
		};

		clearInterval(monitoringInterval);
		monitoringInterval = window.setInterval(function repeat() {
			window[ajaxInfo.openFunction](ajaxInfo);
			return repeat;
		}(), MONITORING_TIME * 1000);
	}

	function onOpenMonitoring(ajaxInfo) {
		$.ajax({
			url : "http://URL:PORT/PATH".replace(/URL/, ajaxInfo.ip).replace(
					/:PORT/,
					((ajaxInfo.port || "") == "" ? "" : ":" + ajaxInfo.port))
					.replace(/\/PATH/, ajaxInfo.path),
			type : "GET",
			dataType : "text",
			data : {
				t : new Date().getTime()
			},
			complete : function(jRes) {

				var rst = "";
				try {
					rst = JSON.parse(jRes.responseText);
					window[ajaxInfo.messageFunction](rst);
				} catch (e) {
					// FIXME: json 파싱이 되면 작업하고 안되면 안하고
					tryCatch(e)
				}
			},
			error : function() {
			},
			success : function() {
			}
		});
	}

	function onMessageMonitoring(rst) {
		var items = rst;
		if (items.length > 0) {
			// userLength = items.length;

			for (var i = 0; i < items.length; i++) {
				var extNo = items[i].EXT;
				
				//cti미사용이면 ringing일때 콜링으로 바꿔주기..이게 말이냐
				if("N"==ctiUse){
					if(items[i].CTI.toLowerCase()=="ringing"){
						items[i].CTI = "CALLING";
					}
				}
				
				startUserObj[items[i].EXT] = items[i];

				if (gridMonitoringView.getRowById(extNo) != null) {
					if (gridMonitoringView.cells(extNo, 3).getValue().replace(/^(.*)grid_status_(.?)/, function() {return arguments[2]}).replace(/"(.*)/, "") != items[i].CTI	.toLowerCase()	|| items[i].RTP == "1") {
						onUserInfoChange(items[i]);
					}
					if(items[i].CTI.toLowerCase()!="logout"){
						gridMonitoringView.cells(extNo,4).setValue(items[i].duration);
					}
				} else {
					userQue.enqueue(items[i]);
				}
			}

			//내선번호 보이지 않아도 감청 되게 하기
			if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && playState == false){
				if (listenUser[$('#custPhoneContinue').val()] != undefined && listenUser[$('#custPhoneContinue').val()].RTP == "1" && listenUser[$('#custPhoneContinue').val()].CTI == "CALLING" ) {
					
					listenEvent(listenUser[$('#custPhoneContinue').val()].EXT, listenUser[$('#custPhoneContinue').val()].CUSTNUM,listenUser[$('#custPhoneContinue').val()].STIME, listenUser[$('#custPhoneContinue').val()].SERVERIP);	

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
					SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
					SocketClient.Socket.close();
				}catch(e){
					tryCatch(e)
				}
				RealTimeExt ="";
			}*/
		}
	}
}

// 로딩 후 실행할 함수
$(function() {
	userQue = new Queue();

	$('#btnOnloadeOffice').click(	function() {
						/*
						 * var popWidth = screen.width - 100 var popHeight =
						 * screen.height - 100
						 */
						var targetPath = webPath+ "/monitoring/office_monitoring";
						window.open(targetPath,"_blank","resizable=yes, toolbar=no,location=no, width=1300, height=700,scrollbars=yes ,left=50,top=50'");
					})

	// Display Mode 체인지 이벤트
	$(".change_mode").each(function() {
		var $parent = $(this);

		// 탭 번호 달기
		$parent.find(".mode_list").each(function(i) {
			$(this).attr("data-target", i + 1);
		});
	})

	$(".monitoring_contents").each(function() {
		var $parent = $(this);

		$parent.find(".mode_cont").each(function(i) {
			$(this).attr("data-target", i + 1);
		});
	})

	$(".change_mode")
			.find(".mode_list")
			.click(
					function() {
						// 선택된 탭 재선택할 경우 중단
						if ($(this).hasClass(".mode_list_active"))
							return;

						var $parent = $(this).parents(".change_mode");
						var $tabno = $(this).attr("data-target");

						if ($tabno == 2) {
							window.parent.document.getElementById('pageFrame').src = contextPath
									+ "/monitoring/realtime";
						}

						// 기존 탭 비활성화
						$parent.find(".mode_list_active").removeClass(
								"mode_list_active");

						// 클릭한 탭 활성화
						$(this).addClass("mode_list_active");
						viewDisplay()
					});

	$(".change_mode").each(function() {
		var $parent = $(this);

		// 첫번째 탭 기본 활성화
		// FIXME : 입체 모니터링 추가 되면 '0'으로 바꿀 것
		$parent.find(".mode_list:eq(2)").click();
	});

	// 체크박스 공통 변경 이벤트
	$('.onoffswitch-checkbox').change(function() {

		// 체크박스 CSS 등 설정값 부분
		// input[type:checkbox] class
//		var chkObj = $('.onoffswitch-checkbox');
//		var chkObjAttr = $(this).find(chkObj);

		// checked 속성이 있을 경우 속성 삭제
		if ($(this).is(':checked') == false) {
			$(this).removeAttr('checked');
			$(this).parent().find('.onoffswitch-switch').text('off');
		}
		// checked 속성이 없을 경우 속성 추가
		if ($(this).is(':checked') == true) {
			$(this).attr('checked', true);
			$(this).parent().find('.onoffswitch-switch').text('on');
		}
	});

	// SAVE_____MEMO
	$('#realTimeMemoAdd').on("click",	function() {

				if (RealTimeExt == "")
					return false;

				var savedMemo = $("#realTimeMemoContents").text().replace(
						/<br\s?\/?>/gi, "\n");
				var proc = $("#proc").val();

				// update or insert
				var dataStr = {
					"recTime" : defineStime,
					"extNum" : RealTimeExt,
					"memo" : savedMemo,
					"tag" : savedMemo,
					"memoType" : "T",
					"proc" : proc,
					"type" : "real",
					"memoInTime" : realtime_rc.obj.player.find(".procTime").html()
				}

				$.ajax({
					url : contextPath + "/recMemoProc.do",
					data : dataStr,
					type : "POST",
					dataType : "json",
					cache : false,
					async : false,
					success : function(jRes) {
						alertText("메모 저장", "메모 저장이 완료 되었습니다.");
						layer_popup_close();
						$("#realTimeMemoContents").text('');
					}
				});
			})
	viewStatus();
	viewDisplay();
	onSettingBtnEvent();
})

function viewStatus() {
	// 상태활성화 체크박스 변경 이벤트
	$('.onoffswitch-checkbox[name=selStatus]').change(function() {
			// 아이디의 뒷부분을 소문자로 얻음
						var targetClass = $(this).attr("id").replace(
								/^selStatus(.*?)$/, function() {
									return arguments[1].toLowerCase()
								});

						// 처리하기
						if ($(this).is(':checked')) {
							var grid = gridMonitoringView.getAllItemIds()
									.split(',');
							for (var i = 0; i < grid.length; i++) {
								var gridStr = gridMonitoringView.cells(grid[i],
										3).getValue();
								var state = gridStr.replace(
										/^(.*)grid_status_(.?)/, function() {
											return arguments[2]
										}).replace(/"(.*)/, "");
								if (targetClass == state) {
									gridMonitoringView.getRowById(grid[i]).style.display = "";
								}
							}
						} else {
							var grid = gridMonitoringView.getAllItemIds()
									.split(',');
							for (var i = 0; i < grid.length; i++) {
								var gridStr = gridMonitoringView.cells(grid[i],
										3).getValue();
								var state = gridStr.replace(
										/^(.*)grid_status_(.?)/, function() {
											return arguments[2]
										}).replace(/"(.*)/, "");
								if (targetClass == state) {
									gridMonitoringView.getRowById(grid[i]).style.display = "none";
								}
							}
						}
					});
}

function viewDisplay() {
	// display 체크박스 변경 이벤트
	$('.onoffswitch-checkbox[name=selDisplay]').change(
			function() {
				// 아이디의 뒷부분을 소문자로 얻음
				var targetClass = $(this).attr("id").replace(
						/^selDisplay(.*?)$/, function() {
							return arguments[1].toLowerCase()
						});
				// 처리할 데이터 얻기
				var $target = (function(targetClass) {
					if (targetClass == "userid")
						return "userId";
					else if (targetClass == "callnum")
						return "callnum";
					else if (targetClass == "callingtime")
						return "callingtime";
				}(targetClass));
				// 처리하기
				if ($(this).is(':checked')) {
					if ($target == "userId") {
							gridMonitoringView.setColumnHidden(1, false);
					}
					if ($target == "callnum") {
						gridMonitoringView.setColumnHidden(5, false);
					}
					if ($target == "callingtime") {
						gridMonitoringView.setColumnHidden(4, false);
					}
				} else {
					if ($target == "userId") {
						gridMonitoringView.setColumnHidden(1, true);
					}
					if ($target == "callnum") {
						gridMonitoringView.setColumnHidden(5, true);
					}
					if ($target == "callingtime") {
						gridMonitoringView.setColumnHidden(4, true);
					}
				}
			});
}

function onSettingBtnEvent() {

	// 모니터링 셋팅 레이어
	var $monitoringSettingPannel = $('.monitoring_option');

	//모니터링 지속 감청 레이어
	var $listenAlwaysPannel = $('.listenAlwaysPannel');

	// 모니터링 셋팅 열기 이벤트
	$('#btnMonitSetOpen').click(function() {
		$listenAlwaysPannel.hide("slide", { direction: "right" }, "slow");
		$monitoringSettingPannel.show("slide", {
			direction : "right"
		}, "slow");
	})

	// 모니터링 셋팅 닫기 이벤트
	$('#btnMonitSetExit').click(function() {
		$monitoringSettingPannel.hide("slide", {
			direction : "right"
		}, "slow");
	})

	// 모니터링 옵션 드랍다운 이벤트
	$('.setting_dropdown_btn').click(function() {
		var $dropdownTarget = $(this).parent().next('.setting_dropdown_cont');
		$dropdownTarget.slideToggle("slow");
	})
}
// 코드값으로부터 스테이터스 이름 반환 해주는 함수 (사용 중지)
function getStatusNameByCode(code) {
	var status = "logout";
	switch (code) {
	case "LOGOUT":
		status = "logout";
		break; // "0"
	case "LOGIN":
		status = "login";
		break; // "1"
	case "READY":
		status = "ready";
		break; // "2"
	case "CALLING":
		status = "calling";
		break; // "3"
	case "NOTREADY":
		status = "notready";
		break; // not ready
	case "AFTERCALLWORK":
		status = "acw";
		break; // "4"
	default:
		status = "logout";
		break;
	}
	return status;
}

// db에서 사용자 정보 긁어와서 getUserInfo에 ext를 키값으로 하는 obj 생성
// 실감페이지에서 사용자 정보 매칭 할때 사용!
function getRuserInfo() {

	$.ajax({
		url : contextPath + "/monitoring/selectRuserInfo.do",
		data : {
			monitoring : "true"
		},
		type : "POST",
		dataType : "json",
		sync : false,
		success : function(jRes) {
			// DB에 조회한 계정이 있으면
			if (jRes.success == "Y") {
				var userList = jRes.resData.rUserList;
				getUserInfo = {};
				for (var i = 0; i < userList.length; i++) {
					getUserInfo[userList[i].extNo] = userList[i]
				}
				// setTimeout(function(){
				// $("#mSearchBtn").trigger("click");
				// },300)
			}
		}
	});
}

// 자릿수 메꿔주는 함슈
function lpad(s, padLength, padString) {

	while (s.length < padLength)
		s = padString + s;
	return s;
}

// 시간차 구해주는 함수
function timeDiff(startTime) {
	var now = "";

	$.ajax({
		url : contextPath + "/monitoring/getCurrentTime.do",
		data : {},
		type : "POST",
		dataType : "json",
		async : false,
		success : function(jRes) {
			// DB에 조회한 계정이 있으면
			if (jRes.success == "Y") {
				var currentTime = jRes.resData.currentTime;

				var _year = currentTime.substring(0, 4)
				var _month = currentTime.substring(4, 6)
				var _date = currentTime.substring(6, 8)
				var _call_hour = currentTime.substring(8, 10)
				var _call_minute = currentTime.substring(10, 12)
				var _call_second = currentTime.substring(12, 14)

				now = new Date(_year, _month, _date, _call_hour, _call_minute,
						_call_second)

			}
		}
	});

	var year = now.getFullYear();
	var month = now.getMonth();
	var date = now.getDate();

	var call_hour = startTime.substr(0, 2)
	var call_minute = startTime.substr(2, 2)
	var call_second = startTime.substr(4, 2)

	call_date = new Date(year, month, date, call_hour, call_minute, call_second)

	return msToTime(now.getTime() - call_date.getTime());
}

// 밀리 세컨드 to 시간
function msToTime(duration) {

	var milliseconds = parseInt((duration % 1000) / 100), seconds = parseInt((duration / 1000) % 60), minutes = parseInt((duration / (1000 * 60)) % 60), hours = parseInt((duration / (1000 * 60 * 60)) % 24);

	hours = (hours < 10) ? "0" + hours : hours;
	minutes = (minutes < 10) ? "0" + minutes : minutes;
	seconds = (seconds < 10) ? "0" + seconds : seconds;

	return lpad(hours + "", 2, "0") + ":" + lpad(minutes + "", 2, "0") + ":"
			+ lpad(seconds + "", 2, "0");
}

// 청취 기능
// extNo : 내선
function listenEvent(extNo, phoneNum, stime,serverIp) {
	defineStime = stime;
	defineCustNum = phoneNum;
	function doActive(extNo) {
		// $(gridMonitoringView.getRowById(RealTimeExt)).addClass("gridListening")
		//
		// // active 활성화 된 오브젝트 수 구하기
		// var otherActived = $(".gridListening");
		//
		//
		// // 활성화 된 오브젝트가 1개 초과라면
		// if(otherActived.length > 1){
		// // 전부 활성화 제거후 현재꺼 다시 활성화
		// otherActived.removeClass("officeListening")
		// }
		setTimeout(function() {
			$(gridMonitoringView.getRowById(RealTimeExt)).addClass("gridListening")
		}, 500)
		// gridMonitoringView.clearSelection();

		var setExt = extNo;
		var setUserName = ((getUserInfo[extNo]) ? getUserInfo[extNo].userName: "");
		realtime_rc.obj.controller.setPlayerInfo(extNo,setUserName,phoneNum);
	}

	if (RealTimeExt != "") {
		try {
			document.getElementById('realtimePlayer').getElementsByTagName('audio')[0].src = "";
			gridMonitoringView.setRowColor(RealTimeExt, "")
			SocketClient.Socket.send("/RECSEE/" + RealTimeExt + "/STOP/IE/");
			SocketClient.Socket.close();
		} catch (e) {
			tryCatch(e)
		}
		$(gridMonitoringView.getRowById(RealTimeExt)).removeClass("gridListening")
		RealTimeExt = "";
	} else {
		SetServerinfo(serverIp);
		Init(extNo,serverIp);
		doActive(extNo);

		// 실감시 seleted 색깔 없애기
		setTimeout(function() {
			try{
				gridMonitoringView.getRowById(extNo).classList.remove("rowselected");
				var k = gridMonitoringView.getRowById(extNo).cells.length
				for (var i = 0; i < k; i++) {
					gridMonitoringView.getRowById(extNo).cells[i].classList.remove("cellselected");
				}
			}catch(e){tryCatch(e)}
		}, 250)
	}

}

// 쿠키설정
function setCookie(cName, cValue, cDay) {
	var expire = new Date();
	expire.setDate(expire.getDate() + cDay);
	var cookies = cName + "=" + escape(cValue) + "; path =/ ";
	if (typeof cDay != undefined) {
		cookies += ";expires=" + expire.toGMTString() + ";";
	}
	document.cookie = cookies;
}

// 쿠키 불러오기
function getCookie(cName) {
	cName = cName + "=";
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cName);
	var cValue = "";
	if (start != -1) {
		start += cName.length;
		var end = cookieData.indexOf(";", start);
		if (end == -1)
			end = cookieData.length;
		cValue = cookieData.substring(start, end);
	}
	return unescape(cValue);
}

// 스크롤 정지 함수
$.fn.scrollStopped = function(callback) {
	var that = this, $this = $(this);
	$this.scroll(function(ev) {
		clearTimeout($this.data('scrollTimeout'));
		$this.data('scrollTimeout', setTimeout(callback.bind(that), 250, ev));
	});
};

function getUserLengthReal(code, data) {
	var countUser = 0;
	switch (code) {
		case "bgCode":
			for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
				if (getUserInfo[Object.keys(getUserInfo)[i]].bgCode == data) {
					countUser++;
				}
			}
		break;
		case "mgCode":
			for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
				if (getUserInfo[Object.keys(getUserInfo)[i]].mgCode == data) {
					countUser++;
				}
			}
		break;
		case "sgCode":
			for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
				if (getUserInfo[Object.keys(getUserInfo)[i]].sgCode == data) {
					countUser++;
				}
			}
		break;
		case "name":
			for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
				if (getUserInfo[Object.keys(getUserInfo)[i]].userName == data) {
					countUser++;
				}
			}
		break;
		case "num":
			for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
				if (getUserInfo[Object.keys(getUserInfo)[i]].userId == data) {
					countUser++;
				}
			}
		break;
		case "ext":
			for (var i = 0; i < Object.keys(getUserInfo).length; i++) {
				if (getUserInfo[Object.keys(getUserInfo)[i]].extNo == data) {
					countUser++;
				}
			}
		break;
		default:
		break;
	}
	userLength += countUser;
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
