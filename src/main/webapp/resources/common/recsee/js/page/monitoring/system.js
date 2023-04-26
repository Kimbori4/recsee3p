/*addLoadEvent(logSysMonitoringLoad);


function logSysMonitoringLoad() {
	//db에서 관제대상 긁어오기
	mapSelect();
	selectTargetItemListAjax();
	//selectTargetAjax();
	
}

var targetArray=[];// 로케이션 타겟 배열로 담기 임시
var eveTarget = ""
var arrayCount = 0;
var targetTop=0;//대상 죄표
var targetLeft=0;//대상 죄표
var targetId='';//대상 아이디
var targetPostionMap;//위치편집시 사용할 맵
var limitObject=new Map();//항목 임계치 저장할 오브젝트
var targetObject='';//각 관제 항목의 관제치 저장할 맵
var targetsip='';//관제 대상들의 ip
var updateTime;//업데이트 시간
var targetItemlist;//관제 대상들의관측항목
var targetItemlistArray=new Object();
var alertListArray=[];//알람 비교위한 리스트

// 웹 로딩 후 이벤트
$(function() {
	ui_controller();
	selectLimit();
	
	setInterval(function(){
		selectMonitoringRedis();
	}, 5000);
	
	// 임시 : 버튼 이벤트 실행하면 5초간 디저블 시키기
	// 이유 : 버튼 이벤트 발생시 setTimeout 이벤트 너무 많아 여러 이벤트 실행시 이벤트 꼬임을 방지하기 위함
	$('.zoom_controll_bar').find('button').click(function() {

		var $btnTarget = $('.zoom_controll_bar').find('button')

		$btnTarget.attr('disabled','disabled');
		$btnTarget.css('cursor', 'wait');

		setTimeout(function(){
			$btnTarget.removeAttr('disabled');
			$btnTarget.css('cursor', 'pointer');
		}, 2000)
	})

	// 오브젝트 클릭 시 정보 표출 이벤트
	$(document).on("click",'.ui_map_obj_building',function(e) {
		if($('.zoomObj').prop("draggable")==true){
			alert("위치 편집을 완료해 주세요");
		}else if($("#zoomStartBtn").css("display")=="none"){
			alert("플레이나 로테이션 중엔 사용할 수 없습니다.");
		}else{
			var id=$(this).attr('locale');
			onObjClickInfo(id);
		}
			
	});

	// 전체 화면 축소 이벤트
	$('#fullScreenBtn').click(function(e) {
		requestFullScreen();
		var btnTxt = $(this).text();
		var btnResTxt = btnTxt == "Extend" ? "Reduce" : "Extend";
		$(this).text(btnResTxt);
	})

	// Start 이벤트
	$('#zoomStartBtn').click(function(e) {
		if($('.zoomObj').prop("draggable")==true){
			alert("위치 편집을 완료해 주세요");
		}else{
			onZoomTarget();
		}
	});

	// next 이벤트
	$('#nextBtn').click(function(e) {
		// 함수 실행 할 때마다 카운트 증가
		arrayCount += 1;
		if(arrayCount >= targetArray.length) arrayCount = 0;

		eveTarget = '.ui_map_obj_'+targetArray[arrayCount];
		rotationZoomTarget(eveTarget);
	});
	
	// prev 이벤트
	$('#prevBtn').click(function(e) {
		// 함수 실행 할 때마다 카운트 감소
		arrayCount -= 1;
		if(arrayCount == -1) arrayCount = (targetArray.length)-1;

		eveTarget = '.ui_map_obj_'+targetArray[arrayCount];
		rotationZoomTarget(eveTarget);

	});
	
	//위치편집 버튼
	$("#editTargetBtn").on("click", function() {
		if($('.zoomObj').prop("draggable")){
			$("#zoomStartBtn, #rotationBtn").show();
			$(".move_target").css("display", "none");
			if(targetPostionMap.size>0)
				setTargetPosionAjax();
			else {
				$('.zoomObj').prop("draggable",false);
				$(".zoomObj").draggable("destroy");
				alert("위치편집 모드가 종료되었습니다.");
			}
		}else{
			$("#zoomStartBtn, #rotationBtn").hide();
			$(".move_target").css("display", "block");
			alert("위치편집모드가 활성화되었습니다.");
			$('.zoomObj').prop("draggable",true);
			targetPostionMap = new Map;
			$('.zoomObj').draggable({ containment:".ui_map_pos", stop:function(){
				setTargetPostion(this);
			}});
		}
	});
	
	// init 이벤트
	$('#initBtn').click(function(e) {
		initMonitroingFunc(eveTarget);
	})

	// rotation 이벤트
	$('#rotationBtn').click(function(e) {
		if($('.zoomObj').prop("draggable")==true){
			alert("위치 편집모드를 종료해 주세요");
		}else{
			onZoomTarget();
			$('.ui_remote_btn_start, .ui_remote_btn_prev, .ui_remote_btn_next, .ui_remote_btn_conf').hide();
			
			watchingtarget((Number($(eveTarget).attr('watch_time'))+4)*1000);
		}
	});
	
});

// 전체 화면, 축소 이벤트
function requestFullScreen() {
	var document=top.document;
	if (!document.msFullscreenElement && !document.fullscreenElement &&!document.mozFullScreenElement && !document.webkitFullscreenElement) {
		// current working methods
		if (document.documentElement.msRequestFullscreen) {
			document.documentElement.msRequestFullscreen();
		} else if (document.documentElement.requestFullscreen) {
			document.documentElement.requestFullscreen();
		} else if (document.documentElement.mozRequestFullScreen) {
			document.documentElement.mozRequestFullScreen();
		} else if (document.documentElement.webkitRequestFullscreen) {
			document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
		}
	} else {
		if (document.exitFullscreen) {
			document.exitFullscreen();
		}else if (document.msExitFullscreen) {
			document.msExitFullscreen();
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		} else if (document.webkitCancelFullScreen) {
			document.webkitCancelFullScreen();
		}
	}
}

// @ezra
// 오브젝트 클릭 시 정보 표출 이벤트
function onObjClickInfo(id) {
	$('.ui_remote_btn_stop').show();
	$('.ui_remote_btn_start, .ui_remote_btn_rotation').hide();

	openInfoPopup(eveTarget,id);

	setTimeout(function() {
		// 1초 뒤 팝업 위치 보정
		// 이유 : 스타트, 로테이션 이벤트 실행 후 클릭 팝업 불러낼 시 이벤트 꼬임 방지
		$('.detail_info_wrap').css('top','89px');
	}, 1000)

}

// @ezra
// First Zoom In 이벤트
function onZoomTarget() {
	// 배열의 첫 번째 값 Zoom In 실행
	eveTarget = '.ui_map_obj_'+targetArray[0];

	// 헤더 숨김
	$('.headerWrap').css({'opacity':'0','z-index':'0'});

	// 배경 숨김 (배경 새로 깔림)
	$('.sys_monitroing_bg').css({'background-color':'transparent','background-image':'none'});

	// 우측 레이어, 상단 경고창, 줌 리모컨 위치 보정 위치 보정
	$('.sys_monitroing_right, .sys_monitroing_timecall, .zoom_controll_bar, .detail_info_wrap').css('top','0px');

	// 바디 스크롤 숨김
	$('body').css('overflow','hidden');

	// 이전, 다음 버튼 생성
	$('.ui_remote_btn_prev, .ui_remote_btn_next, .ui_remote_btn_stop').show();

	// 로테이션 버튼 숨김
	// 이유 : 스타트 후, 로테이션 누르면 이벤트 꼬임
	$('.ui_remote_btn_rotation, .ui_remote_btn_conf, .ui_remote_btn_start').hide();

	moveTargetEve(eveTarget);
}

// @ezra
// Rotation Zoom 이벤트
function rotationZoomTarget(eveTarget) {
	// 초기화 이벤트들 실행
	onZoomOut(eveTarget);
	initGuideAnimation();
	initTargetPos();

	// 팝업 닫기
	closeInfoPopup();

	setTimeout(function() {
		// 2초 후 무빙 이벤트 실행
		moveTargetEve(eveTarget)
	},2000)
}
//지정한 시간만큼만 타겟 자동 관측
function watchingtarget(eveTarget) {
	rotationInterval = setTimeout(function() {
		arrayCount += 1;
		if(arrayCount >= targetArray.length) arrayCount = 0;
		eveTarget = '.ui_map_obj_'+targetArray[arrayCount];
		rotationZoomTarget(eveTarget);
		watchingtarget((Number($(eveTarget).attr('watch_time'))+6)*1000);
	}, eveTarget)
}
// @ezra
// Obj Moving & Zoom  이벤트 정의
function moveTargetEve(eveTarget) {
	// 가이드 라인 오브젝트 생성
	appendGuideAnimation();
	// 현재 웹창 전체 너비
	var placeWidth = parseInt($('.ui_map_pos').css('width').replace('px',''));
	var clientWith = document.documentElement.clientWidth
	// 현재 웹창 전체 높이
	var placeHeight = document.documentElement.clientHeight
	// 지역 오브젝트 위치값 받기
	var eveTargetLeft = $(eveTarget).offset().left;
	var eveTargetTop = $(eveTarget).offset().top;
	// 센터 값 구하기
	var placeWidthCenter = (placeWidth / 2)+$('.ui_map_pos').offset().left; //지도 너비의 중앙
	var placeHeightCenter = (placeHeight / 2);//웹창 화면의 중앙
	// Center 값 기준으로 계산 (+,- 값은 오브젝트 사이즈 보정 값)
	// 좌우 계산
	centerWidthVal = placeWidthCenter - (eveTargetLeft+75)
	targetLeftPos = {left: centerWidthVal+"px"};
	// 상하 계산
	centerHeightVal = placeHeightCenter - (eveTargetTop+25)
	targetTopPos = {top: centerHeightVal+"px"};
	// 줌 위치 시작값
	var zoomLeftPos = $(eveTarget).css('left');
	var zoomTopPos = $(eveTarget).css('top');
	// Zoom Scale Value
	var defaultOriginPos = "scale(3.0, 3.0)";
	// 도달할 곳 위치값 받기
	var targetZoomPos = {
		"transform-origin": zoomLeftPos+" "+zoomTopPos+" 0px",
		"transform": defaultOriginPos
	}
	// 좌우 가이드라인 무브 이벤트
	onGuideAnimation();
	// 좌우 이동
	$('.ui_map_pos').animate(targetTopPos, 135, function() {
		setTimeout(function(){
			// 좌우 이동 후 상하 이동
			$('.ui_map_pos').animate(targetLeftPos, 135, function() {
				setTimeout(function(){
					// 상하 이동 후 ZOOM-IN 실행
					$('.ui_map_pos').css(targetZoomPos)
					timeOpenPopup=setTimeout(function() {
						// 1초 뒤 팝업 열기
						openInfoPopup(eveTarget);
					}, 1000)
				}, 1100);
			})
		}, 500)
	});
}

// @ezra
// 좌우 가이드라인 생성 및 이동 이벤트 정의
function onGuideAnimation() {
	$('.sys_monitoring_left_border').animate({
		left: "50%",
		opacity: 1
	}, 135, function() {
		// 상하 가이드라인 생성 및 이동
		$('.sys_monitoring_top_border').animate({
			top: "50%",
			opacity: 1
		}, 135, function() {
			setTimeout(function() {
				// 상하 가이드라인 제거
				hideGuideAnimation();
			},900);
		});
	});
}

// @ezra
// 가이드라인 위치 초기화 이벤트 정의
function initGuideAnimation() {
	$('.sys_monitoring_left_border').animate({
		left: "0%",
		opacity: 1
	}, 135, function() {
		// 상하 가이드라인 생성 및 이동
		$('.sys_monitoring_top_border').animate({
			top: "0%",
			opacity: 1
		}, 135);
	});
}

// @ezra
// 모니터링 모드 일때 위치 초기화 이벤트 정의
function initTargetPos() {
	$('.ui_map_pos').animate({left: "0px"}, 135, function() {
		setTimeout(function(){
			// 좌우 이동 후 상하 이동
			$('.ui_map_pos').animate({top: "0px"}, 135)
		}, 500)
	});
}

// @ezra
// 상하 가이드라인 제거 이벤트 정의
function hideGuideAnimation() {
	$('.sys_monitoring_left_border, .sys_monitoring_top_border').animate({
		opacity: 0
	},135)

	initGuideAnimation();
}

// @ezra
// 가이드 라인 마스킹 생성 이벤트 정의
function appendGuideAnimation() {
	// 이벤트 실행되면 바디에 마스크 생성 (배경 이동), 좌측 레이아웃 선 생성, 위쪽 레이아웃 선 생성
	$('body').append(''+
		'<div class="sys_monitroing_mask"></div>'+
		'<div class="sys_monitoring_border_place">'+
			'<div class="sys_monitoring_left_border"></div>'+
			'<div class="sys_monitoring_top_border"></div>'+
		'</div>'
	)
}

// @ezra
// 가이드 라인 마스킹 제거 이벤트 정의
function initGuideAnimation() {
	setTimeout(function() {
		$('.sys_monitroing_mask, .sys_monitoring_border_place, .sys_monitoring_left_border, .sys_monitoring_top_border').remove()
	},500)
}

// @ezra
// Zoom Out 이벤트 이벤트 정의
function onZoomOut(eveTarget) {
	$('.ui_map_pos').css({
		"transform-origin": "",
		"transform": "",
		"left": "0px",
		"top": "0px"
	});
	$('.ui_map_obj_tit').css('background-color','rgba(0,0,0,0.7)');
	$('.ui_map_obj_building').css('background-image', 'url(' + recseeResourcePath + '/images/project/map-view/ui_building_twintower.png');

	closeInfoPopup();
}


// @ezra
// 모니터링 모드 초기화 이벤트 이벤트 정의
function initMonitroingFunc(eveTarget) {
	// 로테이션 인터벌 클리어
	try {
		clearInterval(rotationInterval);
	} catch(e) {}

	// 팝업 정보 제거
	if($('.detail_info_wrap').css('display')=='none'){
		clearTimeout(timeOpenPopup);
	}
	else{
		closeInfoPopup();
	}
		
	
	// 스타일 초기화
	$('.ui_map_pos').css({
		"transform-origin": "",
		"transform": "",
		"left": "0px",
		"top": "0px"
	});
	$('.ui_map_obj_tit').css('background-color','rgba(0,0,0,0.7)');
	$('.ui_map_obj_building').css('background-image', 'url(' + recseeResourcePath + '/images/project/map-view/ui_building_twintower.png');

	// 헤더 숨김 복원
	$('.headerWrap').css({'opacity':'1','z-index':'100000005'});

	// 배경 복원 (배경 새로 깔림)
	$('.sys_monitroing_bg').css({'background-color':'#061116','background-image': 'url(' + recseeResourcePath + '/images/project/map-view/squre_bg_pattern.png'});

	// 우측 레이어, 상단 경고창 위치 복원
	$('.sys_monitroing_timecall, .detail_info_wrap').css('top','80px');

	// 스크롤 복원
	$('body').css('overflow', 'auto');

	// 줌 리모컨 위치 보정
	$('.zoom_controll_bar').css('top','89px');

	// 이전, 다음 버튼 숨김
	$('.ui_remote_btn_prev, .ui_remote_btn_next, .ui_remote_btn_stop').hide();

	// 로테이션 버튼 생성
	$('.ui_remote_btn_start, .ui_remote_btn_rotation, .ui_remote_btn_conf').show();
}

// @ezra
// 팝업 닫기 이벤트 정의
// FIXME : 팝업 닫을 때 해당 지역의 값 같이 제거
function closeInfoPopup() {
	var $slideTarget = $('.detail_info_wrap')
	//var $detailTarget = $('.popup_detailinfo_wrap');

	$slideTarget.hide("slide", { direction: "left" }, 1000);
	//$detailTarget.hide("slide", { direction: "left" }, 1000);
}

// @ezra
// 팝업 열기 및 생성  이벤트 정의
// FIXME : 팝업 불러 올 때 해당 지역의 값 같이 생성
function openInfoPopup(eveTarget, id) {
	$(eveTarget).find('.ui_map_obj_tit').css('background-color','rgba(0, 52, 127, 0.7)');
	//$(eveTarget).css('background-image', 'url(' + recseeResourcePath + '/images/project/map-view/ui_building_twintower_over.png');
	if(eveTarget==""){
		eveTarget=".ui_map_obj_"+id;
	}
		
	if(id==undefined){
		id=eveTarget.slice(12);
	}
	
	var ip = $(eveTarget).attr("ip");
			
	$(".hidddenElement").hide();
	
	var showstr="";
	
	if(targetItemlist[ip]!=undefined){
		for(var i=0;i<targetItemlist[ip].length;i++){
			showstr+=mathItem(targetItemlist[ip][i].rm_item_index)+",";
		}
		showstr=showstr.slice(0,-1);
		$(showstr).show();
	}
	
	var $slideTarget = $('.detail_info_wrap')
	$slideTarget.show("slide", { direction: "right" }, 1000);
	
	if($("#dbWrapParent").is(":visible")&&targetObject!=''){
		$("#pgOnOff").text(targetObject[ip].pg);
		$("#pgCount").text(targetObject[ip].pgcount);
	}
	if($("#recWrapParent").is(":visible")&&targetObject!=''){
		$("#recOnOff").text(targetObject[ip].recsee);
	}
	if($("#cpuWrap").is(":visible")&&targetObject!=''){
		cpuChart(Number(targetObject[ip].cpu));
	}
	if($("#memoryWrap").is(":visible")&&targetObject!=''){
		memoryChart(Number(targetObject[ip].rem));
	}
	if($("#hddWrap").is(":visible")&&targetObject!=''){
		hddChart(Number(targetObject[ip].hdd));
	}
	if($("#wasWrap").is(":visible")&&targetObject!=''){
		wasChart(Number(targetObject[ip].was));
	}
}

//관제대상 지도에 부르기
function selectTargetAjax(){
	$.ajax({
		url:contextPath+"/monitoring/selectControlTargetList.do",
		type:"get",
		data:{},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: false,
		success: function(jRes) {
			if(jRes.success =="Y") {
				//관측 순서를 위한 배열 담기
				for(var i=0;i<jRes.resData.ControlTargetResult.length;i++){
					if(jRes.resData.ControlTargetResult[i].rm_target_watch_time!='0'&&jRes.resData.ControlTargetResult[i].rm_icon_filename!=''&&jRes.resData.ControlTargetResult[i].rm_target_ui_position_top!='0'){
						targetArray.push(jRes.resData.ControlTargetResult[i].rm_target_id);
						
						targetsip+=jRes.resData.ControlTargetResult[i].rm_target_ip;
						targetsip+="/";
					}
					
				}
				targetsip=targetsip.slice(0,-1);
				var ipArray=targetsip.split("/");
				for(var i=0;i<ipArray.length;i++){
					var temp= [];
					var tempIp=ipArray[i];
					
					if(targetItemlist[tempIp]!=undefined){
						for(var y=0;y<targetItemlist[tempIp].length;y++){
							temp.push(targetItemlist[tempIp][y].rm_item_index);
						}
						targetItemlistArray[tempIp]=temp;
					}
					
				}
				
				//관제대상 추가
				$(".ui_map_pos").append(jRes.resData.appendHtml);
				selectMonitoringRedis();
			}
		}
	});
}
//레디스통해 관제 정보 업데이트
function selectMonitoringRedis() {
	$.ajax({
		url:contextPath+"/monitoring/selectMonitoringRedis.do",
		type:"post",
		data:{"targetsip":targetsip},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: true,
		success: function(jRes) {
			if(jRes.success =="Y") {
				targetObject=jRes.resData.jsonobject;
				updateTime=getTimeStamp();
				
				var ipArray=targetsip.split("/");
				var chk=[];
				for(var i=0; i<ipArray.length;i++){
					var tempip=ipArray[i];
					if(targetItemlistArray[tempip]!=undefined){
						if(targetItemlistArray[tempip].indexOf("1")!=-1&&Number(targetObject[ipArray[i]].cpu)>=Number(limitObject.get("1").limit)){
							alertSystem(ipArray[i],"CPU 사용률",targetObject[ipArray[i]].cpu, limitObject.get("1").limit,"percent" );
							chk.push(ipArray[i]+"/"+"CPU 사용률");
						}
						if(targetItemlistArray[tempip].indexOf("2")!=-1&&Number(targetObject[ipArray[i]].hdd)>=Number(limitObject.get("2").limit)){
							alertSystem(ipArray[i],"HDD 사용률",targetObject[ipArray[i]].hdd, limitObject.get("2").limit,"percent" );
							chk.push(ipArray[i]+"/"+"HDD 사용률");
						}
						if(targetItemlistArray[tempip].indexOf("3")!=-1&&Number(targetObject[ipArray[i]].rem)>=Number(limitObject.get("3").limit)){
							alertSystem(ipArray[i],"메모리 사용률",targetObject[ipArray[i]].rem, limitObject.get("3").limit,"percent" );
							chk.push(ipArray[i]+"/"+"메모리 사용률");
						}
						if(targetItemlistArray[tempip].indexOf("4")!=-1&&targetObject[ipArray[i]].recsee==limitObject.get("4").limit){
							alertSystem(ipArray[i],"녹취 엔진 상태",targetObject[ipArray[i]].recsee, limitObject.get("4").limit,"On/Off");
							chk.push(ipArray[i]+"/"+"녹취 엔진 상태");
						}
						if(targetItemlistArray[tempip].indexOf("5")!=-1&&Number(targetObject[ipArray[i]].was)>=Number(limitObject.get("5").limit)){
							alertSystem(ipArray[i],"톰캣 메모리 사용률",targetObject[ipArray[i]].was, limitObject.get("5").limit,"percent" );
							chk.push(ipArray[i]+"/"+"톰캣 메모리 사용률");
						}
						if(targetItemlistArray[tempip].indexOf("7")!=-1&&targetObject[ipArray[i]].pg==limitObject.get("7").limit){
							alertSystem(ipArray[i],"Postgresql 서비스 상태",targetObject[ipArray[i]].pg, limitObject.get("7").limit,"On/Off");
							chk.push(ipArray[i]+"/"+"Postgresql 서비스 상태");
						}
						if(targetItemlistArray[tempip].indexOf("8")!=-1&&Number(targetObject[ipArray[i]].pgcount)>=Number(limitObject.get("8").limit)){
							alertSystem(ipArray[i],"Postgresql 세션 개수",targetObject[ipArray[i]].pgcount, limitObject.get("8").limit,"input");
							chk.push(ipArray[i]+"/"+"Postgresql 세션 개수");
						}
					}
				}
				if(chk.length==0){
					$(".sys_alert").hide();
					$(".sys_alert marquee").text("");
				}
					for(var i=0;i<alertListArray.length;i++){
						if(chk.indexOf(alertListArray[i])==-1){
							var tempIp=alertListArray[i].split("/")[0];
							var tempReason=alertListArray[i].split("/")[1];
							var tempId = $(".ui_map_obj_building[ip='"+tempIp+"']").attr('locale');//아이디
							$(".ui_map_obj_building[ip='"+tempIp+"'] .iconWrap").css('background-image',$(".ui_map_obj_building[ip='"+tempIp+"']  .iconWrap").css('background-image').replace("_alert","")).removeClass("alert");
							
							unAlertList(tempIp, tempId, tempReason);
						}
					}
					
				alertListArray=chk;
			}else{
				alert("모니터링 정보 업데이트에 실패하였습니다.");
			}
		}
	});
}

function alertSystem(target, item, current, limit, unit) {
	$.ajax({
		url:contextPath+"/monitoring/alertSystem.do",
		type:"post",
		data:{"target":target, "item":item, "current":current, "limit":limit, "unit":unit},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: true,
		success: function(jRes) {
			if(jRes.success =="Y") {
				if(jRes.resData.YN=="On"){
					if($(".sys_alert marquee").text().length==0){
						$(".sys_alert marquee").text(jRes.resData.msg);
					}else if($(".sys_alert marquee").text().length!=0&&jRes.result=='0'){
	  					$(".sys_alert marquee").text($(".sys_alert marquee").text()+" "+jRes.resData.msg);
					}
					$(".sys_alert").show();
					if(!($(".ui_map_obj_"+jRes.resData.target_id+" .iconWrap").hasClass("alert"))){
						$(".ui_map_obj_"+jRes.resData.target_id+" .iconWrap").css('background-image',$(".ui_map_obj_"+jRes.resData.target_id+" .iconWrap").css('background-image').replace(".png","_alert.png"));
						$(".ui_map_obj_"+jRes.resData.target_id+" .iconWrap").addClass("alert")
					}
				}
			}else{
				alert("모니터링 경보 업데이트에 실패하였습니다.");
			}
		}
	});
}

function unAlertList(ip,id,reason){
	var ip=ip;
	var id=id;
	var reason=reason;
	
	$.ajax({
		url:contextPath+"/monitoring/AuAlertSystem.do",
		type:"post",
		data:{"ip":ip,"id":id, "reason":reason},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: true,
		success: function(jRes) {
			if(jRes.success =="Y") {
				$(".sys_alert marquee").text($(".sys_alert marquee").text().replace(jRes.resData.msg,""));
			}else{
				alert("모니터링 경보 업데이트에 실패하였습니다.");
			}
		}
	});
	
}
function getTimeStamp() {
	var d = new Date();
	
	var s = leadingZero(d.getFullYear(),4)+'-'+
				leadingZero(d.getMonth()+1,2)+'-'+
				leadingZero(d.getDate(),2)+' '+
				
				leadingZero(d.getHours(),2)+':'+
				leadingZero(d.getMinutes(),2)+':'+
				leadingZero(d.getSeconds(),2);
	
	return s;
}

function leadingZero(n, digits) {
	var zero='';
	n= n.toString();
	
	if(n.length<digits){
		for(i=0;i<digits - n.length; i++)
			zero+='0';
	}
	return zero + n;
}

function setTargetPosionAjax(){
		var JArray = new Array();
		targetPostionMap.forEach(function(item,key) {
			var JObject = new Object();
			JObject=item;
			JArray.push(JObject);
		})
		var JsonList = JSON.stringify(JArray);
		$.ajax({
			url:contextPath+"/monitoring/updateTargetPosition.do",
			type:"post",
			data:{"JsonList":JsonList},
			contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			dataType:"json",
			async: false,
			success: function(jRes) {
				if(jRes.success =="Y") {
					$('.zoomObj').prop("draggable",false);
					$(".zoomObj").draggable("destroy");
					alert("위치가 수정되었습니다.")
				}else{
					alert("위치 수정에 실패하였습니다.");
				}
			}
		});
}

function setTargetPostion(target){
	targetId=$(target).attr("locale");
	
	var JObject = new Object();
	JObject.r_id=targetId;
	JObject.r_top=($("[locale="+targetId+"]").css("top")).replace("px","");
	JObject.r_left=($("[locale="+targetId+"]").css("left")).replace("px","");
	
	targetPostionMap.set(targetId,JObject);
}

//임계치 처음 한번만 불러옴
function selectLimit() {
	$.ajax({
		url:contextPath+"/monitoring/selectLimit.do",
		type:"post",
		data:{},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: false,
		success: function(jRes) {
			if(jRes.success =="Y") {
				for(var i=0; i<jRes.resData.selectResult.length;i++){
					var ob=new Object();
					ob.unit=jRes.resData.selectResult[i].rm_item_unit;
					ob.name=jRes.resData.selectResult[i].rm_item_name;
					ob.limit=jRes.resData.selectResult[i].rm_item_limit;
					limitObject.set(jRes.resData.selectResult[i].rm_item_index,ob);
				}
			}else{
				alert("임계치 설정에 실패하였습니다.");
			}
		}
	});
}
function selectTargetItemListAjax() {
	$.ajax({
		url:contextPath+"/monitoring/selecMonitoringItemOfTarget.do",
		type:"post",
		data:{},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: false,
		success: function(jRes) {
			if(jRes.success =="Y") {
				targetItemlist=jRes.resData.selectResult;
				selectTargetAjax();
			}else{
				alert("관제 대상 관측 항목 불러오기 실패")
			}
		}
	});
}

//맵 가져오기
function mapSelect() {
	$.ajax({
		url:contextPath+"/monitoring/mapSelect.do",
		type:"post",
		data:{},
		contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		dataType:"json",
		async: false,
		success: function(jRes) {
			if(jRes.success =="Y") {
				$(".ui_map_kor").css('background-image','url('+recseeResourcePath+'/images/project/map-view/'+jRes.resData.map+')');
			}else{
				alert("Map 불러오기에 실패하였습니다.");
			}
		}
	});
}

function mathItem(item_index) {
	var result="";
	switch (item_index) {
	case "1":
		result="#cpuWrap";
		break;
	case "2":
		result="#memoryWrap";
		break;
	case "3":
		result="#hddWrap";
		break;
	case "4":
		result="#recWrapParent";
		break;
	case "5":
		result="#wasWrap";
		break;
	case "7":
		result="#dbWrapParent";
		break;
	case "8":
		result="#dbWrapParent";
		break;
	}
	return result;
}

//그래프
function wasChart(target) {
	
	$("#wasChart").dxPieChart({
		palette:["#2D71C3","#D89003"],
		dataSource:[{state:"used",val:target},{state:"not used",val:(100-target)}],
		series:[{
			type:"doughnut",
			argumentField:"state",
			label:{
				visible:true,
				connector:{
					visible:true
				}
			}
		}],
		legend:{
			horizontalAlignment:"center",
			verticalAlignment:"bottom",
			font:{size:15,
					color:"white",
					family:"Noto Sans, sans-serif"
			}
		}
	});
}
function cpuChart(target) {
	$("#cpuGrap").dxCircularGauge({
		geometry:{
			startAngle:230,
			endAngle:310
		},
		scale:{
			startValue:0,
			endValue:100,
			majorTick:{
				tickInterval:20,
				color:"black"
			},
			label:{
				customizeText: function(arg) {
					return "";
				}
			}
		},
		value:target,
		valueIndicator:{
			offset:8,
			spindleSize:8,
			spindelGapSize:1,
			width:1
		},
		title:{
			text:"현재 사용률 : "+target+"%",
			font:{
				size:16,
				color:"white",
				family:"Noto Sans, sans-serif"
			},
			position:"bottom"
		}
	});
}
function memoryChart(target) {
	$("#memoryGrap").dxCircularGauge({
		geometry:{
			startAngle:230,
			endAngle:310
		},
		scale:{
			startValue:0,
			endValue:100,
			majorTick:{
				tickInterval:20,
				color:"black"
			},
			label:{
				customizeText: function(arg) {
					return "";
				}
			}
		},
		value:target,
		valueIndicator:{
			offset:8,
			spindleSize:8,
			spindelGapSize:1,
			width:1
		},
		title:{
			text:"현재 사용률 : "+target+"%",
			font:{
				size:16,
				color:"white",
				family:"Noto Sans, sans-serif"
			},
			position:"bottom"
		}
	});
}
function hddChart(target) {
	$("#hddGrap").dxCircularGauge({
		geometry:{
			startAngle:230,
			endAngle:310
		},
		scale:{
			startValue:0,
			endValue:100,
			majorTick:{
				tickInterval:20,
				color:"black"
			},
			label:{
				customizeText: function(arg) {
					return "";
				}
			}
		},
		value:target,
		valueIndicator:{
			offset:8,
			spindleSize:8,
			spindelGapSize:1,
			width:1
		},
		title:{
			text:"현재 사용률 : "+target+"%",
			font:{
				size:16,
				color:"white",
				family:"Noto Sans, sans-serif"
			},
			position:"bottom"
		}
	});
}*/