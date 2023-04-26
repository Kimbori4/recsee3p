var getUserInfo = {}; // 전역 변수 ; 유저 정보 가져와서 담을 그릇 [키값은 내선번호]
var loadedOffice = [];
var realtime_rc;
var ajaxInfo;
var defineStime = "";
var defineCustNum = ""; // 콜인입 고객 전화번호
var defineUserName = ""; // 콜인입 상담사 이름
var defineUserId = ""; // 콜인ㅇㅂ
var monitoringInterval;
var gridSearch, gridLog;

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
var serverip;
var redisTable;
var MONITORING_TIME = 3;

addLoadEvent(monitroingLoad);

function monitroingLoad() {
	serverInfoCall();
	redisInfoCall();
	onLoad();
	onSettingBtnEvent();
}

/** **********레디스 임시 *************** */
function startRedisSocket() {
	clearInterval(monitoringInterval);
	clearInterval(userAdd);
	
	monitoringInterval = window.setInterval(function() {
		repeat()
	}, MONITORING_TIME * 1000);

}

function repeat() {
		
	/*$.ajax({
		url : contextPath + "/monitoring/channelMonitoringSelect.do",
		data : {
				"serverip" 		: serverip
			,	"redisTable" 	: redisTable
		},
		type : "POST",
		dataType : "json",
		async : false,
		success : function(jRes) {
			// DB에 조회한 계정이 있으면
			if (jRes.success == "Y") {
				var selectData = jRes.resData.selectData;
				
				for(var i=0; i< selectData.length; i++){
					for(var j=0; j< selectData[0].length; j++){
						gridSearch.cells2(i,j).setValue(selectData[i][j]);
					}
				}
				
				for(var i=0; i< selectData.length; i++){
					var Status = gridSearch.cells2(i,gridSearch.getColIndexById("Status")).getValue();
					var RTP = gridSearch.cells2(i,gridSearch.getColIndexById("RTP")).getValue();
					
					if(Status == "RECORDING" && RTP.indexOf('card_record_acw') != -1){
						$("#gridChannel .objbox table tbody tr").eq(i+1).css("backgroundColor","red");
					}else{
						$("#gridChannel .objbox table tbody tr").eq(i+1).css("backgroundColor","white");
					}
				}
				
			}
		}
	});	*/
	
	$.ajax({
		url : contextPath + "/monitoring/channelMonitoringHeader.do",
		data : {
				"serverip" 		: serverip
			,	"redisTable" 	: redisTable
		},
		type : "POST",
		dataType : "json",
		async : false,
		success : function(jRes) {
			// DB에 조회한 계정이 있으면
			var selectTitle = jRes.resData.selectTitle;	
			var selectData = jRes.resData.selectData;	 
			
			for(var j=0; j< selectData.length; j++){
				for(var i=0; i < selectTitle.length;i++){
					$(".agent_class"+selectData[j][0]+" .agent_info_wrap .agent_info").eq(i).children('p').html(selectData[j][i]);
				}
			}
			
			for(var i=0; i< selectData.length; i++){
				var Status = $(".agent_class"+selectData[i][0]+" .agent_info_wrap .agent_info .agnet_Status").html();
				var RTP = $(".agent_class"+selectData[i][0]+" .agent_info_wrap .agent_info .recodeButton p").html();
				
				if(Status == "RECORDING" && RTP != "1"){
					$(".agent_class"+selectData[i][0]+" .agent_info_wrap").css('backgroundColor', 'red');
					$(".agent_class"+selectData[i][0]+" .agent_info_wrap .agent_info p").css("color","white")
				}else{
					$(".agent_class"+selectData[i][0]+" .agent_info_wrap").css('backgroundColor', 'white');
					$(".agent_class"+selectData[i][0]+" .agent_info_wrap .agent_info p").css("color","black")
				}
			}
		}
	});	
}


function onLoad() {	
	$("#mSearchBtn").click(function(){
		
		serverip = $("#serverFilter option:selected").val();
		redisTable =  $("#redisFilter option:selected").val();
		
		$(".status_card_wrap").remove();
		
		/*objGrid = new dhtmlXGridObject("gridChannel");
		objGrid.setImagePath(recseeResourcePath + "/images/project/");
		objGrid.i18n.paging = i18nPaging[locale];
		objGrid.enablePaging(true,100, 5, "pagingChannel", true);
		objGrid.setPagingWTMode(true,true,true,[100,250,500]);
		objGrid.setPagingSkin("toolbar","dhx_web");
		//objGrid.enableContextMenu(listenSimple);
		objGrid.enableColumnAutoSize(false);
		objGrid.enableMultiline(true);
		objGrid.setSkin("dhx_web");

		objGrid.init();
		
		objGrid.load(contextPath+"/monitoring/channelMonitoring.xml?serverip="+serverip+"&redisTable="+redisTable, function(){});		
		
		
		gridSearch = objGrid;
		
		*/
		// 상담원 오브젝트
				
		$.ajax({
			url : contextPath + "/monitoring/channelMonitoringHeader.do",
			data : {
					"serverip" 		: serverip
				,	"redisTable" 	: redisTable
			},
			type : "POST",
			dataType : "json",
			async : false,
			success : function(jRes) {
				// DB에 조회한 계정이 있으면
				var selectTitle = jRes.resData.selectTitle;	
				var selectData = jRes.resData.selectData;	 
				
				for(var j=0; j< selectData.length; j++){
				
					var stateText = "<div class='status_card_wrap realtime_slash_status  agent_class"+selectData[j][0]+"'>"+
							        	"<div class='status_card'>"+			        
								        	"<div class='card_info_wrap'>"+
								        		"<div class='agent_info_wrap'>";
									for(var i=0; i < selectTitle.length;i++){
										stateText+= "<div class='agent_info'>"
										
														if(selectData[j][i].indexOf('<p class') != -1){
															stateText+= selectData[j][i]
														}else{
															stateText += "<p class='agnet_"+selectTitle[i]+"'>"+selectData[j][i]+"</p>"
														}
										stateText+=	"</div>"			        	 
									}
						stateText += 			"</div>"+
						        			"</div>"+
						        		"</div>"+
						        	"</div>"
					
					$("#viewCardMode").append(stateText); 
										
				}
				
				$(".monitoring_option .display_option .setting_dropdown_cont .option_check").remove();
				
				for(var j=0; j< selectTitle.length; j++){
								
					var monitoringSetting = "<div class='option_check'>"+
												"<span class='onofftit'>"+selectTitle[j].toUpperCase()+"</span>" +
														"<div class='onoffswitch check_status_ready'>" +
															"<input type='checkbox' name='selStatus' class='onoffswitch-checkbox' data-hide='agnet_"+selectTitle[j]+"' id='selStatus"+selectTitle[j]+"' checked/>" +
																"<label class='onoffswitch-label' for='selStatus"+selectTitle[j]+"''></label>" +
														"</div>" +
											"</div>"
						
					$(".monitoring_option .display_option .setting_dropdown_cont").append(monitoringSetting);	
				}
				
				// 옵션 체크 오브젝트 삽입
				$('.onoffswitch-label').append('<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">on</span>');
				
				
				checkOnOff();				
			}
		});	
		
		startRedisSocket();
		
		/*
		objGrid = new dhtmlXGridObject("gridLog");
		objGrid.setImagePath(recseeResourcePath + "/images/project/");
		objGrid.i18n.paging = i18nPaging[locale];
		objGrid.enablePaging(true,100, 5, "pagingLog", true);
		objGrid.setPagingWTMode(true,true,true,[100,250,500]);
		objGrid.setPagingSkin("toolbar","dhx_web");
		//objGrid.enableContextMenu(listenSimple);
		objGrid.enableColumnAutoSize(false);
		objGrid.enableMultiline(true);
		objGrid.setSkin("dhx_web");

		objGrid.init();
		
		objGrid.load(contextPath+"/monitoring/logMonitoring.xml?serverip="+serverip+"&redisTable="+redisTable, function(){});		
				
		gridLog = objGrid;
		*/
	});
	
}



function serverInfoCall(){
	$.ajax({
		url : contextPath + "/monitoring/channelMonitoringSysInfoSend.do",
		data : {
		},
		type : "POST",
		dataType : "json",
		success : function(jRes) {
			if (jRes.success == "Y") {
				var systemInfoResult = jRes.resData.systemInfoResult;				
				
				for(var i =0; i< systemInfoResult.length; i++){
					$("#serverFilter").append("<option value='"+ systemInfoResult[i].sysIp +"' >"	+ systemInfoResult[i].sysName	+ "</option>");
				}
				
			}else{
				alert('Not ServerInfo')
			}
		}
	})
}

function redisInfoCall(){
	
	$("#serverFilter").change(function(){
		
		$("#redisFilter").children('option').remove();
		
		
		var serverip = $("#serverFilter option:selected").val();
		
		if(serverip != '' && serverip != null){
		
			$.ajax({
				url : contextPath + "/monitoring/channelMonitoringRedisInfoSend.do",
				data : {
					"serverip" : serverip
				},
				type : "POST",
				dataType : "json",
				success : function(jRes) {
					if (jRes.success == "Y") {
						var redisArrayList = jRes.resData.redisArrayList;	
						var redisArrayListName = jRes.resData.redisArrayListName;
						
						for(var i =0; i< redisArrayList.length; i++){
							$("#redisFilter").append("<option value='"+ redisArrayList[i] +"' >"	+ redisArrayListName[i]	+ "</option>");
						}
						
					}else{
						alert('Not RedisInfo')
					}
				}
			})
		}
	});
	
}

function TimerSetting(){
	var timer = $("#timeValue option:selected").val()
		
	MONITORING_TIME = timer;
	
	alert("Timer Setting Complete");
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

function checkOnOff(){
	// 체크박스 공통 변경 이벤트
    $('.onoffswitch-checkbox').change(function(){
		// 체크박스 CSS 등 설정값 부분
		// input[type:checkbox] class
		var chkObj = $('.onoffswitch-checkbox');
		var chkObjAttr = $(this).find(chkObj);
		var agentData = $(this).data().hide;
		
		// checked 속성이 있을 경우 속성 삭제
		if($(this).is(':checked') == false) {
			$(this).removeAttr('checked');
			$(this).parent().find('.onoffswitch-switch').text('off');		
			$("."+agentData).hide();
		}
		// checked 속성이 없을 경우 속성 추가
		if($(this).is(':checked') == true) {
			$(this).attr('checked', true);
			$(this).parent().find('.onoffswitch-switch').text('on');
			$("."+agentData).show();
		}
	});
}