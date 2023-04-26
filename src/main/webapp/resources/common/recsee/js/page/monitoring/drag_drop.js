//코드값으로부터 스테이터스 이름 반환 해주는 함수
function getStatusNameByCode(code){
	var status = "logout";
	switch(code) {
	    case "2" : status = "logout";break;       	//"0"
	    case "1" : status = "login"; break;      	//"1"
	    case "4" : status = "ready"; break;      	//"2"
	    case "5" : status = "calling"; break;   	//"3"
	    case "3" : 									//not ready  이석
	    case "6" : status = "acw"; break;      		//"4"
	    default  : status = "logout"; break;
	}
	return status;
}

/**********전역 설정***********/
var isRevert = true;					//드래그 제대로 됬는지  확인 값 

/**********전역 설정***********/
function drag(){
	var $start,$final,$startId,$classTest;
	var $finalWhere,$finalWhereSeat;				//상담원 서로 바뀔 경우 넣어주는 값
	
	//드래그 객체 메서드
	$(".office_obj_agent").draggable({
		revert : function(event,ui){
			if(event == false){
				isRevert = false; 
				return true;
			}else{
				isRevert = true;
			}
			
		},
		opacity : "0.7",
		zIndex : "200",
		start : function(event,ui){
			$start = $(this).parent().html();
			$classTest = $(this).parents().parent();
			$(document).find(".office_drop_obj").css({"border":"1px dotted black"})				//처음 이동 시 위치 이동 가능 장소 표출
			$finalWhere = $(this).parents('.office_obj_room').attr("code");
			$finalWhereSeat = $(this).parents(".office_drop_obj")[0].className.replace(/^.*office_room_in_number_seat_(.*?)/, function(){return arguments[1].toLowerCase()}).replace(/[a-z|\-|\s]/gi,"");
			//$(document).find(".office_obj_agent").not($(this)).css({"display":"none"})			//this 선택 값 외 모두 숨김
			
			//roomPlusFunc();																			//드래그 선택시 임의 방 생성
			//$(document).find(".office_obj_room").last().css("opacity","0.5");					//임의방투명 표시
			clearInterval(monitoringInterval);
		},
		
		drag : function (event,ui){
			$start = $(this).parent().html();
			$startId = $(this).parent();
			$classTest = $(this).parents();
			$(document).find(".office_drop_obj").css({"border":"1px dotted black"})				//처음 이동 시 위치 이동 가능 장소 표출
			//$(document).find(".office_obj_agent").not($(this)).css({"display":"none"})			//this 선택 값 외 모두 숨김
			clearInterval(monitoringInterval);
		},
		stop : function(event,ui){
			if(isRevert){
				$(this).parent().html($final);
				thisShowHide();
				//원래 상담원 있을경우 바뀌는 곳도 위치 조정
				if($final!= ""){
					try{
						//드래그 끝난 후 값 ajax로 DBINSERT
						//방 코드,좌석 번호
						var roomCode = $finalWhere;
						var targetClass = $finalWhereSeat;
						var targetExt = $final.replace(/^.*office-ext="(.*?)".*/, function(){return arguments[1]});
						
						$.ajax({
							url: contextPath+"/monitoring/officeMonitoringInsertAgent.do",
							data:  {
									codeFilter : $('#codeFilter').val()
								,	roomCode : roomCode
								,	seatNum : targetClass
								,	agentExt : targetExt
							},
							type: "POST",
							dataType: "json",
							success: function(jRes) {					
							}
						})
					}catch(e){tryCatch(e)}
				}
				drag();
			}
			$(this).css({"left":"","top":"","opacity":"","z-index":""});
			$(document).find(".office_drop_obj").css({"border":"","background":""});			//이동후 style 값 제거
			$(document).find(".office_obj_agent").css({"display":"block"});						//이동후 숨김값 표출
			//$(document).find(".office_obj_room").last().css("opacity","");						//생성방 다시 투명 끄기
			//roomMinusFunc();
			
			startRedisSocket();		
		}
		
	});
	
	//드롭 객체 메서드
	$(".office_drop_obj").droppable({
		accept : ".office_obj_agent",
		tolerance : "intersect",
		drop : function(evnet,ui){
			if(isRevert == true){
					if($start == "" || $start == null)
						return false;
					if($start !=""){
						//재정렬 후 html 뽑아 내기
						var sex = $start.replace(/^.*office_obj_agent_(.*?)_.*/, function(){return arguments[1]});
						$final = $(this).html();
						$(this).children().remove();
						
						//방 코드,좌석 번호
						var roomCode = $(this).parents('.office_obj_room').attr('code');
						var targetClass = $(this).attr("class").replace(/^.*office_room_in_number_seat_(.*?)/, function(){return arguments[1].toLowerCase()}).replace(/[a-z|\-|\s]/gi,"");
						var targetExt = $start.replace(/^.*office-ext="(.*?)".*/, function(){return arguments[1]});
						
						//드래그 끝난 후 값 ajax로 DBINSERT
						$.ajax({
							url: contextPath+"/monitoring/officeMonitoringInsertAgent.do",
							data:  {
									codeFilter : $('#codeFilter').val()
								,	roomCode : roomCode
								,	seatNum : targetClass
								,	agentExt : targetExt
							},
							type: "POST",
							dataType: "json",
							success: function(jRes) {					
							}
						})
						
						//드래그 시작한 아이템 넣어주기
						$(this).html($start);
						$(this).children().css({"left":"","top":"","opacity":"","z-index":""});
						sorting($(this).attr('class'),$(this).children(),sex);
						$start="";	
					}
				}
			//roomMinusFunc();
		},
		over : function(event){
			//console.log(event);
			$(this).css({"background":"crimson"});
		},
		out : function(){
			$(this).css({"background":""})
		}
	
	})
	
	
	/*$('.office_obj_room').droppable({
		accept : ".office_obj_agent",
		deactivate : function(event,ui){
			
		},
		over : function(){
			
			roomPlusFunc();
			$(document).find(".office_obj_room").last().css("opacity","0.5");
		},
		out : function(){
			$(document).find(".office_obj_room").last().css("opacity","");
		}	
	})*/
}

/*
 *  @author david
 * 	@param cls       class 구분자 
 *  @parma id  		 변경할 오브젝트 
 * 
 *	 @brief 상담사 이동시 방 클래스 보고 클래스 변경 
 */

function sorting(cls,id,sex){
	if(cls.indexOf("office_room_in_obj_01")>0){
		id.removeClass();
		id.addClass("office_obj_agent office_obj_agent_"+sex+"_back_right agent_num_01");
	}
	if(cls.indexOf("office_room_in_obj_02")>0){
		id.removeClass();
		id.addClass("office_obj_agent office_obj_agent_"+sex+"_front_right agent_num_02");
	}
	if(cls.indexOf("office_room_in_obj_03")>0){
		id.removeClass();
		id.addClass("office_obj_agent office_obj_agent_"+sex+"_back_right agent_num_03");
	}
	if(cls.indexOf("office_room_in_obj_04")>0){
		id.removeClass();
		id.addClass("office_obj_agent office_obj_agent_"+sex+"_front_right agent_num_04");
	}
}


/*
 *  @author david
 *  
 *	@brief 방 생성 메서드 
 *
 */
function roomPlusFunc(){
	roomPersonnel = parseInt($("#roomPersonnel").val());
	var topObj = $(".office_obj_wrap");
	var roomGroupCount = parseInt(roomPersonnel/4);	
	//첫 실행 룸 카운트 더해줌
	roomLastCnt++;
	
	// html 스트링 셋팅
	var roomObjString = ""+
	"<div class='new_room_plus office_obj_room' room-number bg-code mg-code sg-code>"+
		"<div class='office_obj_agent_wrap'>"+
		"<div class='office_wrap_tit'><p>sgName</p></div>"
		"</div>"+
	"</div>";

	var roomGroupObjString = ""+
	"<div class='office_agent_group_right group_num'>"
	"</div>";
	
	var roomInObjString = "<div class='office_drop_obj office_room_in_obj'></div>";



	tempStr = roomObjString.replace("room-number","room-number="+roomLastCnt)
	/*tempStr = tempStr.replace("bg-code","bg-code="+bgCode);
	tempStr = tempStr.replace("mg-code","mg-code="+mgCode);
	tempStr = tempStr.replace("sg-code","sg-code="+sgCode);
	tempStr = tempStr.replace("sgName",sgName+(roomCount > 1 ? " - "+i : ""));*/

	topObj.append(tempStr);
	
	$roomObj = $("[room-number="+roomLastCnt+"]");
	
	if(roomPersonnel == 16){
		$roomObj.addClass("office_obj_room_type_01")
	}else if(roomPersonnel == 36){
		$roomObj.addClass("office_obj_room_type_02").css("padding","0px")
	}else{
		$roomObj.addClass("office_obj_room_type_03")
	}
	
	
	// 방 obj마다 4개묶음 그룹 생성
	for(var j = 1; j < roomGroupCount+1 ; j ++){
		

		$roomObj.append(roomGroupObjString.replace("group_num","group_num_"+lpad(j+"", 2, "0")));
		
		for(var k = 1; k < 5; k++){
			 $roomObj.find(".group_num_"+lpad(j+"", 2, "0")).append(roomInObjString.replace("office_room_in_obj","office_room_in_obj_"+lpad(k+"", 2, "0")));
		}
	}
	officeViewResize();
	drag();
}


/*
 *  @author david
 *  
 *	@brief 방 삭제 메서드 
 *
 */
function roomMinusFunc(){
	var roomCount = $(document).find(".office_obj_room").length;
	
	for(var j=0;j<roomCount;j++){
		
		var room = $(document).find(".office_obj_room").eq(j);
		var roomBkChk = 0;					//방 상담사 있는지 체킹
		var roomObjCount = room.find(".office_drop_obj").length;
		
		for(var i=0;i<roomObjCount;i++){
			
			if(room.find(".office_drop_obj").eq(i).html()==""){
				roomBkChk++;	
			}
			if(roomBkChk==roomObjCount){
				roomLastCnt--;					//방 삭제 하여 값 하나 빼기
				room.remove();
			}
		}
	}
}

function dragRoom(){
	$('.office_obj_room').draggable({
		drag : function (event,ui){
			//console.log(event);
			clearInterval(monitoringInterval);
		},
		stop : function(event){
			var sameRoom = $('.'+$(this).attr('code'));
			
			var locationRoom = "";
			for(var i=0;i<sameRoom.length;i++){
				var le = sameRoom[i].style.left;
				var to = sameRoom[i].style.top;
				if(i==sameRoom.length-1){
					locationRoom += le+","+to;
				}else{
					locationRoom += le+","+to+"|";	
				}
			}
			startRedisSocket();		
//			var left = $(this).css("left");
//			var top = $(this).css("top");
			var roomCode = $(this).attr("code");
			var roomSeat = sameRoom.find(".agent_status").length;
			var dataStr = {
						codeFilter : $('#codeFilter').val()
					,	roomCode : roomCode
					,	roomSeat : roomSeat
					,	roomLocation : locationRoom
			}
			//드래그 끝난 후 값 ajax로 DBINSERT
			$.ajax({
				url: contextPath+"/monitoring/officeMonitoringInsertRoom.do",
				data:  dataStr,
				type: "POST",
				dataType: "json",
				success: function(jRes) {					
				}
			})
		}
	})
}

