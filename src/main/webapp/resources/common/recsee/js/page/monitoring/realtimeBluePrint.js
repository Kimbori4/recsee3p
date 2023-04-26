/**
 * 
 */

var getUserInfo = {};		// 레디스 매번 들고와서 넣어줄 Obj
var currentSeq;	//현재 방 보는 번호

var userManageGrid; 	// 사원 조회 그리드

var websocket;		// 웹소켓

var selectPhone;			// 선택된 전화기

var menu_txt = "<ul class='menu_item'><li onclick=removeItem(this);>아이템 삭제</li><li onclick=insertAfterTarget(this);>앞으로보내기</li><li onclick=insertBeforeTarget(this);>뒤로보내기</li></ul>";		// 메뉴정의;
var phone_menu_txt = "<ul class='menu_item'><li onclick=userSelect(this);>상담사 선택</li><li onclick=removeItem(this);>아이템 삭제</li><li onclick=insertAfterTarget(this);>앞으로보내기</li><li onclick=insertBeforeTarget(this);>뒤로보내기</li></ul>";		// 메뉴정의;

//IE9 Transport 오류 관련 처리
jQuery.support.cors = true;

var stTimeInterval;

var detailStime;

//DEBUG 모드 사용   true : false ;
var DEBUG = false;

/*
 *  requestAnimationFrame 체킹 함수
 */
window.requestAnimationFrame = (function(callback) {
    return window.requestAnimationFrame ||  window.webkitRequestAnimationFrame ||    window.mozRequestAnimationFrame ||     window.oRequestAnimationFrame ||    window.msRequestAnimationFrame ||
    function(callback) { window.setTimeout(callback, 1000); };
})();

$(function(){
	//옵션 체크 로드
	optionBtnLoad();
	
	// 이벤트 정의
	bluePrintEventLoad();
	
	// 왼쪽 아이템 클릭 이벤트
	itemSelect();
		
	// 플레이어 로드
	recseePlayerLoad();
	
	//	웹소켓로드
	webSocketLoad();
	
	// INIT(); 상담원 requestAnimationFrame;
	init();
	
	// 메모 저장
	memoAdd();
	
	// UI CONTROLLER
	ui_controller();
})

function optionBtnLoad(){
	// 옵션 체크 오브젝트 삽입
	$('.onoffswitch-label').append('<span class="onoffswitch-inner"></span><span class="onoffswitch-switch">on</span>');
	
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
	
	
	
	//내선 번호 체크 함수
	$('.office_stay_check').change(function(){
 		if($('.custPhoneContinueChk').is(":checked")== true)
 			$('.custPhoneContinueChk').prop("checked",false)
 			
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
			
	})
	
	// 고객 번호 체크 함수
	$('.custPhoneContinueChk').change(function(){
		if ($('.office_stay_check').is(":checked") == true)
			$('.office_stay_check').prop("checked", false)
			
			if ($(this).is(":checked") == true && $('#statusSave').is(":checked") == true) {
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
	});
    
    //	지속감청의 INPUT 박스 키업으로 저장
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
	});
	

	//내선번호 보이지 않아도 감청 되게 하기
	if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && playState == false){
		setTimeout(function(){
			if (getUserInfo[$('#custPhoneContinue').val()] != undefined && getUserInfo[$('#custPhoneContinue').val()].RTP == "1" && getUserInfo[$('#custPhoneContinue').val()].CTI == "CALLING" ) {
				
				setTimeout(function(){
					listenAlwayEvent(getUserInfo[$('#custPhoneContinue').val()].EXT, getUserInfo[$('#custPhoneContinue').val()].CUSTNUM,getUserInfo[$('#custPhoneContinue').val()].STIME,getUserInfo[$('#custPhoneContinue').val()].SERVERIP);	
				},500)

				if($('.listen-away').length <= 0){
					$('#bottomFixedPlayer').after('<div class="listen-away"><p></p></div>');
					$('.listen-away p').text('현재 상담사 ['+getUserInfo[$('#custPhoneContinue').val()].AGENTNAME+'] 내선번호 ['+getUserInfo[$('#custPhoneContinue').val()].EXT+'] 지속 감청 중입니다.');
				}else{
					$('.listen-away').remove();
					$('#bottomFixedPlayer').after('<div class="listen-away"><p></p></div>');
					$('.listen-away p').text('현재 상담사 ['+getUserInfo[$('#custPhoneContinue').val()].AGENTNAME+'] 내선번호 ['+getUserInfo[$('#custPhoneContinue').val()].EXT+'] 지속 감청 중입니다.');
				}

				defineUserId =  getUserInfo[$('#custPhoneContinue').val()].AGENTID;
				defineCustNum = getUserInfo[$('#custPhoneContinue').val()].CUSTNUM;
				defineUserName = getUserInfo[$('#custPhoneContinue').val()].AGENTNAME;
				return;
			}
		},3000)
	}
}

function bluePrintEventLoad(){
	
	//	조회시 해당 도면 로드 이벤트
	$("#bluePrintSearch").click(function(){
		currentSeq = $("#blueprintSelect").val();
		$.ajax({
			url : contextPath + "/monitoring/bluePrintPaint.do",
		    type: "POST",
	        dataType: "json",
	        async : false,
	        data: {
	        	Seq : currentSeq
	        },
	        beforeSend : function(){
//	        	top.progress.on();
	        },
	        success : function(jRes){
	        	var data = jRes.resData;
	        	$("#blueprintWrap").html(data.bluePrintPaint);
	        	
	        	//	 조회시 왼쪽 세팅 창 열려 있을 경우 저장 버튼 생성
	        	if($(".monitoring_wrap").hasClass("monitoring_left")){
	        		
	        		$("#blueprintWrap").append("<div id='savePaint' class='save_btn' onclick='saveBluePrint();'></div>");
//	        		$("#blueprintWrap .monitoring_items").find(".item_setting").css("display","block");
//	        		$("#blueprintWrap .monitoring_items").find(".item_rotate").css("display","block");
	        		$("#blueprintWrap .monitoring_items").css("pointer-events" , "");
	        		
	        		// 세팅창 열려 있으며 내부에 값이 있을때 드래그 가능하게 하기
	        		if($("#blueprintWrap").html() != ""){
	        			$("#blueprintWrap .monitoring_items").draggable({
	        				containment : "#blueprintWrap"
	        			});
	        		}
	        		
	        		// 리사이즈 할 아이템 있다면 다시 리사이즈 가능하게 
	        		$("#blueprintWrap .monitoring_items .item-wall").resizable({
	        			resize : function(e , ui){
	        				ui.size.height = ui.originalSize.height;
	        				$(e.target).parents(".monitoring_items").css("width", e.target.clientWidth + 5);
	        			}
	        		});
	        		
	        		$("#blueprintWrap .monitoring_items .item-wall-ver").resizable({
	        			resize : function(e , ui){
	        				ui.size.width = ui.originalSize.width;
	        				$(e.target).parents(".monitoring_items").css("height", e.target.clientHeight + 13);
	        			}
	        		});
	        	}
	        },
	        complete : function(){
//	        	top.progress.off();
	        	
	        	//	셋팅 중아니면 전화기에 커서 포인터
	        	if(!$(".monitoring_wrap").hasClass("monitoring_left")){
	        		$(".monitoring_items.ext_item").addClass("cursor-pointer");
//	        		$(".monitoring_items.ext_item").on("mouseover", function(e){
//		    			$(this).find(".item_setting").css("display","none");
//		    			$(this).find(".item_rotate").css("display","none");
//		    		});	
	        	}
	        	
	        }
		})
	});
	
	// 도면 공유 클릭
	$("#bluePrintShare").click(function(){
		if(currentSeq == undefined){
			alert("선택된 도면이 없습니다.");
			return false;
		}
		
		treeShareView = new dhtmlXTreeObject("treeViewShareAgent","100%","100%",0);
		treeShareView.attachEvent("onXLS", function(){
			//top.progress.on()
		});
		treeShareView.setImagePath("../resources/component/dhtmlxSuite/skins/skyblue/imgs/dhxtree_skyblue/");
		treeShareView.enableThreeStateCheckboxes(true); 
		treeShareView.enableSmartXMLParsing(true);
		treeShareView.load(contextPath+"/AgentTreeView.xml?aUser=N");
		
		treeShareView.attachEvent("onXLE", function(grid_obj,count){
				//top.progress.off();
		});
		
		treeShareView.attachEvent("onClick",function(id){
			
			var uri=contextPath+"/userRealTimeManageGrid.xml"+formToSerializeShare();
			userShareManageGrid.clearAndLoad(uri, function(){
				userShareManageGrid.changePage(1)
			})
		});
		
		
		//	 그리드
		userShareManageGrid = new dhtmlXGridObject("userShareManageGrid");
		userShareManageGrid.setIconsPath(recseeResourcePath + "/images/project/");
		userShareManageGrid.setImagePath(recseeResourcePath + "/images/project/");
		userShareManageGrid.i18n.paging = i18nPaging[locale];
		userShareManageGrid.enablePaging(true, 100, 5, "userShareManagePaging", true);
		userShareManageGrid.setPagingWTMode(true,true,true,[100,200,500]);
		userShareManageGrid.setPagingSkin("toolbar", "dhx_web");
		userShareManageGrid.enableColumnAutoSize(false);
		userShareManageGrid.setSkin("dhx_web");
		userShareManageGrid.init();
		
		userShareManageGrid.load(contextPath+"/userRealTimeManageGrid.xml?header=true&shareName="+encodeURIComponent($("#blueprintSelect option:selected").text()), function(){
			
			userShareManageGrid.attachEvent("onXLS", function(){
//				top.progress.on();
			});
			
			// 파싱완료
			userShareManageGrid.attachEvent("onXLE", function(grid_obj,count){
//				top.progress.off();
			});
			
			userShareManageGrid.aToolBar.addSpacer("perpagenum");
			userShareManageGrid.aToolBar.addButton("select",12, "저장", "check.svg", "check.svg");
			
			userShareManageGrid.aToolBar.attachEvent("onClick", function(name){
				if(name == "select"){
					if(userShareManageGrid.getCheckedRows(0) == ""){
						alert("선택된 사용자가 없습니다.");
						return  false;
					}
					
					//	도면 공유 선택하면 해당 사용자들에게 공유
					var k = userShareManageGrid.getCheckedRows(0);
					var t = k.split(",");
					var shareArr = new Array();

					for(var i = 0 ; i < t.length ; i++){
						shareArr.push(userShareManageGrid.cells(t[i],2).getValue());
					}
					
					$.ajax({
						url : contextPath + "/monitoring/insertBluePrintShare.do"
					,	data : {
								seq : currentSeq
							,	userId : shareArr.toString()
						}
					,	type:"POST"
					,	dataType:"json"
					,	success:function(jRes){
						if(jRes.success == "Y"){
							console.log("success")
							
						}
					}
				})
					
					//	사용자 체크 후 로직
					layer_popup_close("#userShareSelectPopup");
				}
			});
			
			
			userShareManageGrid.attachEvent("onPageChanged" ,function (){
				if($("#allcheck").find("img").hasClass("all_chk_btn_true")){
					$("#allcheck").find("img").removeClass("all_chk_btn_true");
					$("#allcheck").find("img").addClass("all_chk_btn_false");
				}
			})
			
		 	// UserShareGrid AllCheck
		 	$("#allcheck").click(function(){
		 		var img =$(this).find("img");
		 		
		 		//	체크 풀려 있는 경우
		 		if(img.hasClass("all_chk_btn_false")){
		 			img.removeClass("all_chk_btn_false");
		 			img.addClass("all_chk_btn_true");
		 			userShareManageGrid.checkAll();
		 		}else{
		 			img.removeClass("all_chk_btn_true");
		 			img.addClass("all_chk_btn_false");
		 			userShareManageGrid.uncheckAll();
		 		}//
		 		
		 	});
			
		},'xml');
		
		
		layer_popup("#userShareSelectPopup");
		
		
	});
	
	$("#shareUserSearchBtn").click(function(){
		var uri=contextPath+"/userRealTimeManageGrid.xml"+formToSerializeShare();
		userShareManageGrid.clearAndLoad(uri, function(){
			userShareManageGrid.changePage(1)
			
		})
	})
	
	
	//	다른 곳 클릭시
 	window.addEventListener("click",function(e){
 		if(e.target.className != "item_setting"){
 			$("#blueprintWrap .menu_item").hide();
 		}
 		
 		if(e.target.className == "ui-droppable monitoring_left"){
 			$("#blueprintWrap .monitoring_items").css({"background-color":"" , "cursor" : "" , "z-index" : "0"});
 			$("#blueprintWrap .monitoring_items .item_setting").css("display","none");
 			$("#blueprintWrap .monitoring_items .item_rotate").css("display","none");
 		}
 	})

}

function itemSelect(){
	$(".monitoring_interior ul li").click(function(){
		//	li  밑 보더 
		$(this).parent().find("li").removeClass("active");
		$(this).addClass("active");
		
		// li active item
		$(this).parent().find("i").removeClass("active_item");
		$(this).find("i").addClass("active_item");
		
		// li text
		$(this).parent().find(".item_text").removeClass("active_text");
		$(this).find(".item_text").addClass("active_text");
		
		//	아이템 선택후 아래 아이템 리스트 로드
		loadItem($(this).find(".item_text").text());
	});
	
	loadItem("Set");
}

/**
 * @Auth David
 * @param t :  interior item text
 * @Description 아이템 로드후 세팅 대메뉴 클릭 할때마다 아이템 로드
 * */
function loadItem(t){
	for(var i = 0 ; i < 18 ; i++){
		$(".monitoring_item").eq(i).html("");
	}
	switch(t){
		case "Set":
			$(".monitoring_item").eq(0).html("<div class='item monitoring_items ext_item'><i class='interior_item item-set-1'></i></div><div class='item_text'>세트 1</div>");
			$(".monitoring_item").eq(1).html("<div class='item monitoring_items ext_item'><i class='interior_item item-set-2'></i></div><div class='item_text'>세트 2</div>");
			$(".monitoring_item").eq(2).html("<div class='item monitoring_items ext_item'><i class='interior_item item-set-3'></i></div><div class='item_text'>세트 3</div>");
			$(".monitoring_item").eq(3).html("<div class='item monitoring_items ext_item'><i class='interior_item item-set-4'></i></div><div class='item_text'>세트 4</div>");
			$(".monitoring_item").eq(4).html("<div class='item monitoring_items ext_item'><i class='interior_item item-set-5'></i></div><div class='item_text'>세트 5</div>");
			alreadyItem();
			dragItem();
		break;
		case "Desk":
			$(".monitoring_item").eq(0).html("<div class='item monitoring_items'><i class='interior_item item-eclipse-table'></i></div><div class='item_text'>타원형 탁자</div>");
			$(".monitoring_item").eq(1).html("<div class='item monitoring_items'><i class='interior_item item-exlipse-office-table'></i></div><div class='item_text'>둥근 탁자</div>");
			$(".monitoring_item").eq(2).html("<div class='item monitoring_items'><i class='interior_item item-long-desk'></i></div><div class='item_text'>ㄱ 탁자</div>");
			$(".monitoring_item").eq(3).html("<div class='item monitoring_items'><i class='interior_item item-meeting-table'></i></div><div class='item_text'>긴 탁자</div>");
			$(".monitoring_item").eq(4).html("<div class='item monitoring_items'><i class='interior_item item-meeting-table-2'></i></div><div class='item_text'>긴 탁자(2)</div>");
			$(".monitoring_item").eq(5).html("<div class='item monitoring_items'><i class='interior_item item-office-table-l'></i></div><div class='item_text'>책상</div>");
			$(".monitoring_item").eq(6).html("<div class='item monitoring_items'><i class='interior_item item-office-table-r'></i></div><div class='item_text'>책상(2)</div>");
			$(".monitoring_item").eq(7).html("<div class='item monitoring_items'><i class='interior_item item-table'></i></div><div class='item_text'>사각 탁자</div>");
			alreadyItem();
			dragItem();
		break;
		case "Chair":
			$(".monitoring_item").eq(0).html("<div class='item monitoring_items'><i class='interior_item item-boss-chair'></i></div><div class='item_text'>실장 의자</div>");
			$(".monitoring_item").eq(1).html("<div class='item monitoring_items'><i class='interior_item item-dining-chair'></i></div><div class='item_text'>의자</div>");
			$(".monitoring_item").eq(2).html("<div class='item monitoring_items'><i class='interior_item item-meeting-chair'></i></div><div class='item_text'>둥근 의자</div>");
			$(".monitoring_item").eq(3).html("<div class='item monitoring_items'><i class='interior_item item-office-chair'></i></div><div class='item_text'>책상 의자</div>");
			$(".monitoring_item").eq(4).html("<div class='item monitoring_items'><i class='interior_item item-sofar'></i></div><div class='item_text'>소파</div>");
			$(".monitoring_item").eq(5).html("<div class='item monitoring_items'><i class='interior_item item-sofar-2'></i></div><div class='item_text'>소파(2)</div>");
			alreadyItem();
			dragItem();
		break;
		case "Etc":
			$(".monitoring_item").eq(0).html("<div class='item monitoring_items'><i class='interior_item item-box'></i></div><div class='item_text'>박스</div>");
			$(".monitoring_item").eq(1).html("<div class='item monitoring_items'><i class='interior_item item-door-l'></i></div><div class='item_text'>문</div>");
			$(".monitoring_item").eq(2).html("<div class='item monitoring_items'><i class='interior_item item-door-r'></i></div><div class='item_text'>문(2)</div>");
			$(".monitoring_item").eq(3).html("<div class='item monitoring_items'><i class='interior_item item-locker'></i></div><div class='item_text'>사물함</div>");
			$(".monitoring_item").eq(4).html("<div class='item monitoring_items'><i class='interior_item item-monitor'></i></div><div class='item_text'>모니터</div>");
			$(".monitoring_item").eq(5).html("<div class='item monitoring_items ext_item'><i class='interior_item item-phone'></i></div><div class='item_text'>전화기</div>");
			$(".monitoring_item").eq(6).html("<div class='item monitoring_items'><i class='interior_item item-printer'></i></div><div class='item_text'>프린터</div>");
			$(".monitoring_item").eq(7).html("<div class='item monitoring_items'><i class='interior_item item-tv'></i></div><div class='item_text'>TV</div>");
			$(".monitoring_item").eq(8).html("<div class='item monitoring_items'><i class='interior_item item-water'></i></div><div class='item_text'>정수기</div>");
			$(".monitoring_item").eq(9).html("<div class='item monitoring_items'><div class='interior_item item-wall'></div></div><div class='item_text'>파티션</div>");
			$(".monitoring_item").eq(10).html("<div class='item monitoring_items'><div class='interior_item item-wall-ver'></div></div><div class='item_text'>파티션(세로)</div>");
			alreadyItem();
			dragItem();
		break;
	}
}

function settingOn(){
	//	돌아오기 (셋팅중) 
	if($(".monitoring_wrap").hasClass("monitoring_left")){
		$(".monitoring_wrap").removeClass("monitoring_left");
		$(".setting_menu_on").removeClass("left_icon_menu");
		$("#blueprintWrap").removeClass("monitoring_left");
		$(".menu_item").css("display","none");
		
		// 모니터링 wrap 드래깅 풀기
		//$("#blueprintWrap .item").draggable("destroy").removeClass("monitoring_items").find(".item_setting").css("display","none");
		$("#blueprintWrap .monitoring_items").css("pointer-events" , "none");
		$("#blueprintWrap .ext_item").css("pointer-events" , "");
		
		// 저장 버튼 삭제
		$("#savePaint").remove();
		
		// 모든 리사이즈 이벤트 지우기
		$("#blueprintWrap .monitoring_items .item-wall").resizable("destroy");
		$("#blueprintWrap .monitoring_items .item-wall-ver").resizable("destroy");
		
		mouseEventItem()
	}else{
		// 옆으로 가기 (셋팅중 아님)
		$(".monitoring_wrap").addClass("monitoring_left");
		$(".setting_menu_on").addClass("left_icon_menu");
//		$("#blueprintWrap").addClass("monitoring_left");
		
		// 모니터링 wrap 드래깅 걸어주기
		$("#blueprintWrap .monitoring_items").css("pointer-events" , "");
		
		// 세팅 열리면 저장 버튼 생성
		$("#blueprintWrap").append("<div id='savePaint' class='save_btn' onclick='saveBluePrint();'></div>");
		
		if($("#blueprintWrap").html() != ""){
			$("#blueprintWrap .monitoring_items").draggable({
				containment : "#blueprintWrap"
			});
		}
		
		// 리사이즈 할 아이템 있다면 다시 리사이즈 가능하게 
		$("#blueprintWrap .monitoring_items .item-wall").resizable({
			resize : function(e , ui){
				ui.size.height = ui.originalSize.height;
				$(e.target).parents(".monitoring_items").css("width", e.target.clientWidth + 5);
			}
		});
		
		$("#blueprintWrap .monitoring_items .item-wall-ver").resizable({
			resize : function(e , ui){
				ui.size.width = ui.originalSize.width;
				$(e.target).parents(".monitoring_items").css("height", e.target.clientHeight + 13);
			}
		});
		
		mouseEventItem();
	}
}

function dragItem(){
	//모든 아이템
	$(".item").draggable({
		containment : "#blueprintWrap",
		start : function(e , ui ){
		},
		drag : function(e, ui){
		},
		helper : 'clone'
	});
	
	
	$("#blueprintWrap").droppable({
		drop : function(e , ui){
			var arr = $(ui.helper).attr("class").split(" ");
			
			for(var i = 0 ; i < arr.length ; i++){
				if(arr[i] == "item"){
					var newClone = $(ui.helper).clone();
					var left = Number(newClone.css("left").replace("px","")) - 350 + "px";
					var top = Number(newClone.css("top").replace("px","")) - 160 + "px";

					if(!newClone.hasClass("clone_move")){
						var menu = "";
						
						if(newClone.hasClass("ext_item"))
							menu = phone_menu_txt;
						else
							menu = menu_txt;
						
						
						$(this).append(newClone.css({"left" : left , "top" : top}).append("<div class='item_setting'  onclick='settingItem(this);'></div><div class='item_rotate'></div>"+menu).addClass("clone_move")
								.draggable({
									containment : "#blueprintWrap",
									start : function(){
										if(!$(".monitoring_wrap").hasClass("monitoring_left")){
											return false;
										}
									},
									drag : function(){
										if(!$(".monitoring_wrap").hasClass("monitoring_left")){
											return false;
										}
									}
								}));
						
						//	가로 파티션 
						if(newClone.find(".item-wall").length > 0){
							$(this).find(".item-wall").resizable({
								resize : function(e , ui){
									ui.size.height = ui.originalSize.height;
									$(e.target).parents(".monitoring_items").css("width", e.target.clientWidth + 5);
								}
							});
						}
						
						//	세로 파티션
						if(newClone.find(".item-wall-ver").length > 0){
							$(this).find(".item-wall-ver").resizable({
								resize : function(e , ui){
									ui.size.width = ui.originalSize.width;
									$(e.target).parents(".monitoring_items").css("height", e.target.clientHeight + 13);
								}
							});
						}
					}
					
				}
			}
			
			mouseEventItem();
			
			rotate();
		}
	})
	
	
}


function saveBluePrint(){
	
	if(currentSeq == undefined){
		alert("도면을 먼저 조회하여주세요.")
		return false;
	}
	
	if(confirm("도면을 저장 하시겠습니까?")){
		$("#blueprintWrap .monitoring_items").css("pointer-events" , "none");
		$("#blueprintWrap .ext_item").css("pointer-events" , "");
		
		// 모든 리사이즈 이벤트 지우기
		$("#blueprintWrap .monitoring_items .item-wall").resizable("destroy");
		$("#blueprintWrap .monitoring_items .item-wall-ver").resizable("destroy");
		
		
		// 아이템 선택란 취소 및 저장버튼 잠시 숨김
		$("#blueprintWrap #savePaint").remove();
		
		$("#blueprintWrap .monitoring_items").css({"background-color":"" , "cursor" : "","z-index" : "0"});
		$("#blueprintWrap .monitoring_items .item_setting").css("display","none");
		$("#blueprintWrap .monitoring_items .item_rotate").css("display","none");
		
		$(".menu_item").css("display","none");
		$.ajax({
				url : contextPath + "/monitoring/saveBluePrint.do"
			,	data : {
						seq : currentSeq
					,	blueprintpaint : $("#blueprintWrap").html().replace('<div class="save_btn" id="savePaint" onclick="saveBluePrint();"></div>','')
				}
			,	type:"POST"
			,	dataType:"json"
			,	async: false
			,	beforeSend : function(){
//				top.progress.on();
			},	success:function(jRes){
				if(jRes.success == "Y"){
					
					alert("도면 수정이 완료 되었습니다.")
					
				}
			}, complete : function(){
//	        	top.progress.off();
	        }
		})
		$("#blueprintWrap .monitoring_items").css("pointer-events" , "");
		
		$("#blueprintWrap").append("<div id='savePaint' class='save_btn' onclick='saveBluePrint();'></div>");
		
		// 리사이즈 할 아이템 있다면 다시 리사이즈 가능하게 
		$("#blueprintWrap .monitoring_items .item-wall").resizable({
			resize : function(e , ui){
				ui.size.height = ui.originalSize.height;
				$(e.target).parents(".monitoring_items").css("width", e.target.clientWidth + 5);
			}
		});
		
		$("#blueprintWrap .monitoring_items .item-wall-ver").resizable({
			resize : function(e , ui){
				ui.size.width = ui.originalSize.width;
				$(e.target).parents(".monitoring_items").css("height", e.target.clientHeight + 13);
			}
		});
	}
}


function alreadyItem(){
	$("#blueprintWrap").find(".monitoring_items").removeClass("clone_move");
	$("#blueprintWrap").find(".monitoring_items").removeClass("item");
	$("#blueprintWrap").find(".monitoring_items").addClass("itemx");
	$("#blueprintWrap").find(".monitoring_items").addClass("itemx_move");
	$("#blueprintWrap").find(".monitoring_items").draggable({
		containment : "#blueprintWrap"
	});
}

/**
 * 모니터링 아이템 세팅 부분 정의
 * ***/
// 톱니바퀴 클릭
function settingItem(t){
	var item = $(t).closest(".monitoring_items").find(".menu_item");
	if(item.css("display") == "block"){
		item.css("display","none");
	}else{
		item.css("display","block");
	}
}

/**
 *  @Description 아이템 셋팅 선택메뉴
 * */
function removeItem(t){
	$(t).closest(".monitoring_items").remove();
}

function insertBeforeTarget(t){
	var moveItem = $(t).closest(".monitoring_items");
	
	var target = $("#blueprintWrap .monitoring_items").eq(0)
	
	moveItem.insertBefore(target);
	
	$(moveItem).find(".item_setting").css("display","none");
	$(moveItem).find(".item_rotate").css("display","none");
}

function insertAfterTarget(t){
	var moveItem = $(t).closest(".monitoring_items");
	
	var target = $("#blueprintWrap .monitoring_items").last();
	
	moveItem.insertAfter(target);
	
	$(moveItem).find(".item_setting").css("display","none");
	$(moveItem).find(".item_rotate").css("display","none");
}

function userSelect(t){
	selectPhone = $(t).closest(".monitoring_items");
	layer_popup('#userSelectPopup');
	treeviewLoad();
}


function treeviewLoad(){
	treeView = new dhtmlXTreeObject("treeViewAgent","100%","100%",0);
	treeView.attachEvent("onXLS", function(){
//		top.progress.on()
	});
	treeView.setImagePath("../resources/component/dhtmlxSuite/skins/skyblue/imgs/dhxtree_skyblue/");
	//treeView.enableCheckBoxes(1);
	treeView.enableThreeStateCheckboxes(true); 
	treeView.enableSmartXMLParsing(true);
	//treeView.enableDistributedParsing(true,10,10);
	treeView.load(contextPath+"/AgentTreeView.xml?aUser=N");
	
	treeView.attachEvent("onXLE", function(grid_obj,count){
			top.progress.off();
			
	});
	
	treeView.attachEvent("onClick",function(id){
		var uri=contextPath+"/userRealTimeRecManageGrid.xml"+formToSerialize();
		userManageGrid.clearAndLoad(uri, function(){
			userManageGrid.changePage(1)
		})
	})
	
	userManageGrid = new dhtmlXGridObject("userManageGrid");
	userManageGrid.setIconsPath(recseeResourcePath + "/images/project/");
	userManageGrid.setImagePath(recseeResourcePath + "/images/project/");
	userManageGrid.i18n.paging = i18nPaging[locale];
	userManageGrid.enablePaging(true, 100, 5, "userManagePaging", true);
	userManageGrid.setPagingWTMode(true,true,true,[100,200,500]);
	userManageGrid.setPagingSkin("toolbar", "dhx_web");
	userManageGrid.enableColumnAutoSize(false);
	userManageGrid.setSkin("dhx_web");
	userManageGrid.init();
	
	$('.main_form fieldset').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtnUser").trigger("click");
	})
	
	userManageGrid.load(contextPath+"/userRealTimeRecManageGrid.xml?header=true", function(){
		
		userManageGrid.attachEvent("onXLS", function(){
//			top.progress.on();
		});
		
		// 파싱완료
		userManageGrid.attachEvent("onXLE", function(grid_obj,count){
//			top.progress.off();
		});
		
		
		userManageGrid.aToolBar.addSpacer("perpagenum");
		userManageGrid.aToolBar.addButton("select",12, "선택", "check.svg", "check.svg");
		
		userManageGrid.aToolBar.attachEvent("onClick", function(name){
			if(name == "select"){
				if(userManageGrid.getSelectedRowId() == null){
					alert("선택된 사용자가 없습니다.");
					return  false;
				}
				
				if(userManageGrid.cells(userManageGrid.getSelectedRowId() , 2).getTitle().trim() == ""){
					alert("상담사의 내선 등록후 이용해주세요.");
					return false;
				}
				
				selectPhone.attr("id",userManageGrid.cells(userManageGrid.getSelectedRowId() , 2).getTitle());
				
				//	사용자 체크 후 로직
				layer_popup_close("#userSelectPopup");
			}
		});
		
	},'xml');

	$("#searchBtnUser").click(function(event){
		if(treeView.getSelected()==null || treeView.getSelected()==''){
			alert("조직도를 먼저 선택해 주세요")
		}else{
			userManageGrid.clearAndLoad(contextPath+"/userRealTimeRecManageGrid.xml"+formToSerialize())
		}
	});

}

function formToSerialize(){

	$(".inputFilter").trigger('blur');
	$(".inputFilter").trigger('keyup');
	
	var resultValue = "";

	$(".main_form fieldset").children().each(function (i){
		
		var id = this.id;
		var value = ($(this).val()) ? $(this).val().trim() : "";
		var $obj = $(this);
		
		if (value == "" || value == undefined || value == null)
			return;
		
			resultValue += (resultValue.length > 0?"&":"?") + id+"="+value;
		
	});
	
	if(treeView.getSelected()!=null && treeView.getSelected()!=''&&treeView.getSelected()!='all'){
			var level = treeView.getLevel(treeView.getSelected());
			
		switch (level) {
		case 2:
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+treeView.getSelected();
			break;
		case 3:
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+treeView.getSelected().split("_")[0];
			break;
		case 4:
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+treeView.getSelected();
			break;
		}
	}
	return encodeURI(resultValue);
}


function formToSerializeShare(){

	$(".inputFilter").trigger('blur');
	$(".inputFilter").trigger('keyup');
	
	var resultValue = "";

	$(".main_form input").each(function (i){
		
		var id = this.id;
		var value = ($(this).val()) ? $(this).val().trim() : "";
		var $obj = $(this);
		
		if (value == "" || value == undefined || value == null)
			return;
		
		resultValue += (resultValue.length > 0?"&":"?") + id+"="+value;
	});
	resultValue +=  (resultValue.length > 0?"&":"?")+"shareName="+encodeURIComponent($("#blueprintSelect option:selected").text());
	
	if(treeShareView.getSelected()!=null && treeShareView.getSelected()!='' && treeShareView.getSelected()!='all'){
			var level = treeShareView.getLevel(treeShareView.getSelected());
			
		switch (level) {
		case 2:
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+treeShareView.getSelected();
			break;
		case 3:
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+treeShareView.getSelected().split("_")[0];
			break;
		case 4:
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+treeShareView.getSelected();
			break;
		}
	}
	return encodeURI(resultValue);
}




//	도면 추가 로직
function addBluePrint(str){
	if(str == ""){
		alert("도면명을 입력해주세요");
	}else{
		str = str.replace(/&/g , '&amp;').replace(/</g , '&lt;').replace(/>/g,'&gt;');
		$.ajax({
				url : contextPath + "/monitoring/createBluePrint.do"
			,	data : {
					blueprintName : str
				}
			,	type:"POST"
			,	dataType:"json"
			,	async: false
			,	success:function(jRes){
				if(jRes.success == "Y"){
					
					alert("도면 추가가 완료 되었습니다.")
					
					//	팝업 닫기
					layer_popup_close("#addBluePrintPopup")
					addBluePrintName.value = "";	
					
					// 선택파일 이동 select 에도 추가
					blueprintSelect.innerHTML += '<option value='+jRes.resData.seq+'>'+str+'</option>';
					
					currentSeq = jRes.resData.seq;
					
					$("#blueprintSelect option:last").prop("selected","true");
					
					$("#bluePrintSearch").trigger("click");
				}else{
					alert("도면을 추가하는데 문제가 생겼습니다. \n 다시 입력해주세요.");
				}
			}
		})
	}
}

// 이벤트 정리
function mouseEventItem(){
	$(".monitoring_items.clone_move").off("click");
	$(".monitoring_items.clone_move").on("click", function(e){
		
		$("#blueprintWrap .monitoring_items").css({"background-color":"" , "cursor" : "","z-index" : "0"});
		$("#blueprintWrap .monitoring_items .item_setting").css("display","none");
		$("#blueprintWrap .monitoring_items .item_rotate").css("display","none");
		
		if($(this).find(".item_setting").css("display") == "none"){
			$(this).css({"background-color":"rgba(255,0,0,0.4)" , "cursor" : "move" , "z-index" : "100"});
			$(this).find(".item_setting").css("display","block");
			$(this).find(".item_rotate").css("display","block");
		}
	});
	
	$(".monitoring_items.itemx_move").off("click");
	$(".monitoring_items.itemx_move").on("click", function(e){
		
		$("#blueprintWrap .monitoring_items").css({"background-color":"" , "cursor" : "","z-index" : "0"});
		$("#blueprintWrap .monitoring_items .item_setting").css("display","none");
		$("#blueprintWrap .monitoring_items .item_rotate").css("display","none");
		
		if($(this).find(".item_setting").css("display") == "none"){
			$(this).css({"background-color":"rgba(255,0,0,0.4)" , "cursor" : "move","z-index" : "100"});
			$(this).find(".item_setting").css("display","block");
			$(this).find(".item_rotate").css("display","block");
		}
	});
	
	
	//	 셋팅중이 아니면
	if(!$(".monitoring_wrap").hasClass("monitoring_left")){
		
		$("#blueprintWrap .monitoring_items").css({"background-color":"" , "cursor" : "","z-index" : "0"});
		$("#blueprintWrap .monitoring_items .item_setting").css("display","none");
		$("#blueprintWrap .monitoring_items .item_rotate").css("display","none");
		
		$(".monitoring_items.ext_item").off("mouseover");
		$(".monitoring_items.ext_item").off("mouseout");
		$(".monitoring_items.ext_item").on("mouseover", function(e){
			$(this).addClass("cursor-pointer");
		});		
		
		//	전화기 셋팅중 아닐때는 그대로
		$(".monitoring_items.ext_item").on("click", function(e){
			$(this).find(".item_setting").css("display","none");
			$(this).find(".item_rotate").css("display","none");
			$(this).css("background-color","");
		});	
	}else if($(".monitoring_wrap").hasClass("monitoring_left")){
		$(".monitoring_items.ext_item").on("mouseover", function(e){
//			$(this).find(".item_setting").css("display","none");
//			$(this).find(".item_rotate").css("display","none");
			$(this).removeClass("cursor-pointer");
		});	
	}
	
	
	$(".ext_item").draggable({
		containment : "#blueprintWrap",
		start : function(){
			if(!$(".monitoring_wrap").hasClass("monitoring_left")){
				return false;
			}
		},
		drag : function(){
			if(!$(".monitoring_wrap").hasClass("monitoring_left")){
				return false;
			}
		}
	});
	
	//	전화기 커서 포인트 끄기
	$(".monitoring_items.ext_item").removeClass("cursor-pointer");
}


//	아이템 세팅 회전
function rotate(){
	// 기존 회전
	/*$(".item_rotate").off();
	$(".item_rotate").on("mousedown" , function(e){
		e.stopPropagation();
		var rotateFlag = true; 	//회전 가능한 플래그 값

		var item = $(this).closest(".monitoring_items").find(".interior_item");
		
		if(item.length > 0){
			var offset = item.offset();
			
			function mouse(e){
				if(rotateFlag){
					var center_x = (offset.left) + (item.width() / 2);
					var center_y = (offset.top) + (item.height() / 2);
					
					var mouse_x = e.pageX;
					var mouse_y = e.pageY;
					
					var radians = Math.atan2(mouse_x - center_x , mouse_y - center_y);
					
					var degree = (radians * (180 / Math.PI) * -1) + 90;
					
					item.css("transform" , "rotate("+degree+"deg)");
				}
				$(document).mouseup(cancelRotate);
				
				function cancelRotate(e){
					rotateFlag = false;
				}
			}
			
			$(document).mousemove(mouse);
		}
		
	});*/  // 기존 회전
	
	// 클릭으로 8방향 회전
	$(".item_rotate").off();
	$(".item_rotate").on("click" , function(e){
		e.stopPropagation();
		
		var item = $(this).closest(".monitoring_items").find(".interior_item");
		
		if(item.length > 0){
			var deg = 0;
			
			if(item.css("transform") != "none"){
				var matrix = item.css("transform"); 
				var values = matrix.split("(")[1].split(")")[0].split(",");
				var a = values[0];
				var b = values[1];
				var angle = Math.round(Math.atan2(b,a) * (180 / Math.PI));
				deg = angle;
			}
			
			var degree =  deg + 45;
			
			item.css("transform" , "rotate("+degree +"deg)");
		}
	});
	
}

/**
 *  @Auth David
 *  @Description 렉시플레이어 로드
 * */

function recseePlayerLoad(){
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
}


/**
 * @Auth David
 * @Description 웹소켓 접속후 받아오는 값 getUserInfo에 저장
 * */
function webSocketLoad(){
	
	realtimeLog("=======WEBSOCKET START========");
	
	websocket = new WebSocket("ws://"+websocketIp+":12969");
	
	websocket.onopen = function(e){
		
	}

	websocket.onmessage = function(e){
		try{
			var data = JSON.parse(e.data);
			
			//	유저 상태가 들어 올때 getUserInfo 에 담기
			if(data[0] == "ExtStatus"){
				getUserInfo = {};
				
				if(data.length > 0){
					for(var i = 0 ; i < data.length; i++ ){
						getUserInfo[data[i].EXT] = data[i];
					}
				}
			}
			
			if(data[0].getTime != null ){
				setPlayerTime(data[0].getTime);
			}
			
			if(data[0].getUserTime != null ){
				setUserTime(data[0].getUserTime);
			}
			
			//	지속감청
			checkListenAlways();
		}catch(error){
			realtimeLog("=======WEBSOCKET ERR =========" + error);
			//websocket.close();
		}
		
	}

	websocket.onerror = function(e){
		
	}

	websocket.onclose = function(e){
	}
}

/**
 * @Auth David
 * @Description 주기적으로 getUserInfo 계속 긁어와서 전화기 업데이트 처리
 * */
function init(){
	window.requestAnimationFrame(step);
}

function step(timestamp){
	$(".ext_item").each(function(e , ui){
		var extNum = $(ui).attr("id");
		if(extNum != undefined){
			userInfoChange(extNum);
		}
	})
	browserMonitoring();
	window.requestAnimationFrame(step);
}

function userInfoChange(ext){
	try{
		var userExt = (getUserInfo[ext].EXT)?getUserInfo[ext].EXT:"";
		var userId = (getUserInfo[ext].AGENTID)?getUserInfo[ext].AGENTID : "";
		var userName = (getUserInfo[ext].AGENTNAME)?getUserInfo[ext].AGENTNAME:"";
		var userStatus = (getUserInfo[ext].CTI)?getUserInfo[ext].CTI : "";
		var custNum = (getUserInfo[ext].CUSTNUM)?getUserInfo[ext].CUSTNUM:"";
		var serverIP = (getUserInfo[ext].SERVERIP)?getUserInfo[ext].SERVERIP:"";
		
		var statusTxt = userStatus.toLowerCase();
		if(statusTxt == "login"){
			statusTxt = "로그인";
		}
		if(statusTxt == "logout"){
			statusTxt = "로그아웃";
		}
		if(statusTxt == "calling"){
			statusTxt = "전화중";
		}
		if(statusTxt == "aftercallwork"){
			statusTxt = "후처리";
		}
		if(statusTxt == "ready"){
			statusTxt = "대기";
		}
		if(statusTxt == "ringing"){
			statusTxt = "연결중";
		}
		if(statusTxt == "notready"){
			statusTxt = "이석";
		}
		
		$("#"+ext).attr("status",userStatus.toLowerCase());
		
/*		$("#"+ext).find(".interior_item").removeClass(function(index , className){
			return (className.match(/item-phone(.*?)$/gi || [])).join("");
		});
		$("#"+ext).find(".interior_item").addClass("item-phone-"+userStatus.toLowerCase());*/
		
		var boxLength = $("#"+ext).find(".arrow_box").length;
		
		if(boxLength == 0){
			//	전화기 유저 정보 없을 경우 새로 생성..
			$("#"+ext).append("<div class='arrow_box' onclick='listenEvent(this);'><span class='status_text user_status_logout'>["+statusTxt+"]</span><span>"+userExt+"</span><span>"+userName+"</span><div onclick='infoUser(this);' class='ext_info'>i</div></div>");
		}else{
			
			//	 전화기 유저 정보 있을 경우
			$("#"+ext).find(".status_text").html("["+statusTxt+"]");
			
			$("#"+ext).find(".status_text").removeClass(function(index , className){
				return (className.match(/user_status(.*?)$/gi || [])).join("");
			});
			
			$("#"+ext).find(".status_text").addClass("user_status_"+userStatus.toLowerCase());
			
			//	녹취 서버가 다르기 때문에 추가
			$("#"+ext).attr("serverip",serverIP);
		}
	}catch(e){
		realtimeLog(e);
	}
}


function listenEvent(t){
	var ext = $(t).closest(".monitoring_items").attr("id");
	var serverIP = $(t).closest(".monitoring_items").attr("serverip");
	
	if($(t).closest(".monitoring_items").attr("status") != "calling"){
		alert("해당 상담사가 통화중이 아닙니다.");
		return false;
	}
	
	listenIp = serverIP;
	
	try {
		realtime_rc.obj.audio[0].pause();
		realtime_rc.obj.audio[0].currentTime= 0;
		console.log("/RECSEE/"+RealTimeExt+"/STOP/IE/");
		SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
		SocketClient.Socket.close();
	}catch(e){
		realtimeLog(e);
	}
	
	setTimeout(function(){
		SetServerinfo(serverIP);
		Init(ext, serverIP);
		
		defineStime = getUserInfo[ext].STIME;
		defineUserId = getUserInfo[ext].AGENTID;
		defineUserName = getUserInfo[ext].AGENTNAME;
		defineCustNum = getUserInfo[ext].CUSTNUM;
		
	},100)
}

function listenAlwayEvent(ext, phoneNum, stime, serverIP){
	
	listenIp = serverIP;
	
	try {
		realtime_rc.obj.audio[0].pause();
		realtime_rc.obj.audio[0].currentTime= 0;
		console.log("/RECSEE/"+RealTimeExt+"/STOP/IE/");
		SocketClient.Socket.send("/RECSEE/"+RealTimeExt+"/STOP/IE/");
		SocketClient.Socket.close();
	}catch(e){
		realtimeLog(e);
	}
	
	setTimeout(function(){
		SetServerinfo();
		Init(ext);
		
		defineStime = getUserInfo[ext].STIME;
		defineUserId = getUserInfo[ext].AGENTID;
		defineUserName = getUserInfo[ext].AGENTNAME;
		defineCustNum = getUserInfo[ext].CUSTNUM;
		
	},100)
	
}

function memoAdd(){
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
				$("#realTimeMemoContents").text('')
				layer_popup_close();
			}
		});


    })
}


//자릿수 메꿔주는 함슈
function lpad(s, padLength, padString){
while(s.length < padLength)
 s = padString + s;
return s;
}

function infoUser(t){
	event.stopPropagation();
	try{
		var ext = $(t).closest(".monitoring_items").attr("id");
		
		var userExt = (getUserInfo[ext].EXT)?getUserInfo[ext].EXT:"";
		var userId = (getUserInfo[ext].AGENTID)?getUserInfo[ext].AGENTID : "";
		var userName = (getUserInfo[ext].AGENTNAME)?getUserInfo[ext].AGENTNAME:"";
		var userStatus = (getUserInfo[ext].CTI)?getUserInfo[ext].CTI : "";
		var custNum = (getUserInfo[ext].CUSTNUM)?getUserInfo[ext].CUSTNUM:"";
		var mgCode = (getUserInfo[ext].MGCODE)?getUserInfo[ext].MGCODE : "";
		var sgCode = (getUserInfo[ext].SGCODE)?getUserInfo[ext].SGCODE:"";
		detailStime = (getUserInfo[ext].STIME)?getUserInfo[ext].UPDATETIME.substr(0,8)+getUserInfo[ext].STIME:getUserInfo[ext].UPDATETIME;
		
		$("#detailUserName").val(userName);
		$("#detailExtNum").val(userExt);
		
		$("#detailMg").val(mgCode);
		$("#detailSg").val(sgCode);
		
		
		if(userStatus.toLowerCase() != "calling"){
			$("#detailCustNum").val("전화중 아님");
		}else{
			$("#detailCustNum").val(custNum);
		}
		
		// 상세정보 총 콜 수 통화 시간
		$.ajax({
				url : contextPath + "/monitoring/countRecFileAndCallTime.do"
			,	data : {
				ext : ext
			}
			,	type: "POST"
			,	dataType: "json"
			,	async : false
			,	success : function(jRes){
				
				$("#detailTotalCall").val(jRes.resData.count + "건");
				$("#detailTotalCallTime").val(secondToMinute(jRes.resData.callTtime));
			}
			
			
		})
		
		
		
		stTimeInterval = setInterval(function(){
			websocket.send("getUserTime");
		},300);
		
		
		layer_popup("#realTimeUserDetail");
	}catch(e){
		alert("상담사 정보를 가져오는데 문제가 생겼습니다. \n 잠시후 다시 이용해 주시기 바랍니다. ");
	}

}


//	할입 
function suspendCall(t){
	var parent = $(t).closest(".ui_padding");
	var ext = parent.find("#detailExtNum").val();		// 내선번호
	
	var oriNum=parent.find("#halipeExt").val(); 	//	할입출발지 내선번호
	
	if(oriNum==''){
		alert('할입을 위한 올바른 내선번호를 입력해 주세요.');
		return false;
	}else if(getUserInfo[ext].CTI.toLowerCase() != "calling"){
		alert('선택한 설계사가 통화중이 아닙니다.')
		return false;
	}else if(confirm("내선번호 " +oriNum+" 으로 내선번호 "+ext+" 에게 할입하시겠습니까?")){
		$.ajax({
			url : contextPath + "/avaya/suspendCall.do"
		,	data: {
        	"oriNum" :oriNum,
        	"destNum" : ext
		}
		,	type:"POST"
		,	dataType:"json"
		,	async: true
		,	success:function(jRes){
			if(jRes.success="Y"){
				alert("할입요청을 성공였습니다.")
			}else{
				alert("할입요청에 실패하였습니다.")
			}
		}
		,  error:function(error){
			alert("할입에 실패하였습니다.")
		}
	}) ;
	}
}



function setUserTime(time){
	var now = "";
	var _year = time.substring(0, 4)
	var _month = time.substring(4, 6)
	var _date = time.substring(6, 8)
	var _call_hour = time.substring(8, 10)
	var _call_minute = time.substring(10, 12)
	var _call_second = time.substring(12, 14)
	
	now = new Date(_year, _month, _date, _call_hour, _call_minute,_call_second)
	
	var year =  detailStime.substring(0, 4)
	var month =  detailStime.substring(4, 6)
	var date =detailStime.substring(6, 8)

	var call_hour = detailStime.substr(8, 2)
	var call_minute = detailStime.substr(10, 2)
	var call_second = detailStime.substr(12, 2)

	call_date = new Date(year, month, date, call_hour, call_minute, call_second);
	
	
	$("#detailUpdateTime").val(msToTime(now.getTime() - call_date.getTime()));
}

function stopstTimeInterval(){
	clearInterval(stTimeInterval);
}

function checkListenAlways(){
	if($('#custPhoneContinue').val() != "" && $('.office_stay_check').is(":checked") == true && playState == false){
		if (getUserInfo[$('#custPhoneContinue').val()] != undefined && getUserInfo[$('#custPhoneContinue').val()].RTP == "1" && getUserInfo[$('#custPhoneContinue').val()].CTI == "CALLING" ) {
			listenAlwayEvent(getUserInfo[$('#custPhoneContinue').val()].EXT, getUserInfo[$('#custPhoneContinue').val()].CUSTNUM,getUserInfo[$('#custPhoneContinue').val()].STIME,getUserInfo[$('#custPhoneContinue').val()].SERVERIP);	

			if($('.listen-away').length <= 0){
				$('#bottomFixedPlayer').after('<div class="listen-away"><p></p></div>');
				$('.listen-away p').text('현재 상담사 ['+getUserInfo[$('#custPhoneContinue').val()].AGENTNAME+'] 내선번호 ['+getUserInfo[$('#custPhoneContinue').val()].EXT+'] 지속 감청 중입니다.');
			}else{
				$('.listen-away').remove();
				$('#bottomFixedPlayer').after('<div class="listen-away"><p></p></div>');
				$('.listen-away p').text('현재 상담사 ['+getUserInfo[$('#custPhoneContinue').val()].AGENTNAME+'] 내선번호 ['+getUserInfo[$('#custPhoneContinue').val()].EXT+'] 지속 감청 중입니다.');
			}

			defineUserId =  getUserInfo[$('#custPhoneContinue').val()].AGENTID;
			defineCustNum = getUserInfo[$('#custPhoneContinue').val()].CUSTNUM;
			defineUserName = getUserInfo[$('#custPhoneContinue').val()].AGENTNAME;
			return;
		}else{
			$('.listen-away').remove();
		}
	}
}

function browserMonitoring(){
	$("#browserTotal").val($(".ext_item.clone_move").length + $(".ext_item.itemx_move").length);
	$("#browserLogin").val(	$(".ext_item[status=login]").length);
	$("#browserReady").val(	$(".ext_item[status=ready]").length);
	$("#browserCalling").val(	$(".ext_item[status=calling]").length + $(".ext_item[status=ringing]").length);
	$("#browserAcw").val(	$(".ext_item[status=aftercallwork]").length);
	$("#browserLogout").val(	$(".ext_item[status=logout]").length);
	$("#browserNotReady").val(	$(".ext_item[status=notready]").length);
}


/**
@Auth David
@description 	실감 로깅 디버깅 모드일때 console.log
*/

function realtimeLog(t){
	if(DEBUG){
		console.log(t);
	}
}


/**
 *	@param a : 기준값 ,  b : on , off 
 * 
 */
function locationCardSet(a , b){
	localStorage.setItem("blueMonitoring_"+a , b);
}
function locationCardGet(a){
	return localStorage.getItem("blueMonitoring_"+a);
}

