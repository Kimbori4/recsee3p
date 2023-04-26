var CUSTOM_CURRENT_MODE;

// 오브젝트 드래그 앤 드랍 이벤트
$(function() {
	ui_controller();
})

// 도큐멘트 사이즈에 맞춰서 위치 재정렬
function officeViewResize(){
	var getMode = function(width) {
		var widths = [1675, 1400];
		//var separator = parseInt($("body").css("min-width").replace(/[\D]+/g, ""), 10) //1675;
		var i, mode;
		for(var i=0, w; w = widths[i]; i++) {
			if(width > w) {
				break;
			}
		}

		// FIXME: 일단 모드 세개
		mode = (function(modeType) {
			if(roomPersonnel == 16){
				if(modeType == 0) {
					return "3-2";
				} else if(modeType == 1) {
					return "2-2";
				} else if(modeType == 2) {
					return "2-1";
				}
			}else if (roomPersonnel == 36){
				if(modeType == 0) {
					return "2-1";
				} else if(modeType == 1) {
					return "1-1";
				} else if(modeType == 2) {
					return "1-1";
				}
			}else{
				return "";
			}
		}(i));
		return mode;
	}
	var CURRENT_MODE = getMode(parseInt($("body").css("width").replace(/[\D]+/g, ""), 10));
	var MODE = {
		"1-1": null,
		"2-1": null,
		"2-2": null,
		"3-2": null,
	}

	resize();

	$(window).bind("resize", function() {
		var newMode = getMode(parseInt($("body").css("width").replace(/[\D]+/g, ""), 10));
		if(CURRENT_MODE == newMode) {
			return true;
		} else {
			CURRENT_MODE = newMode;
			resize();
		}
		//console.log("CURRENT_MODE: ", CURRENT_MODE);
	});

	function resize() {
		// 박스별로 가져옴
		var $parent = $(".office_obj_wrap");
		var $agentObj = $(".office_obj_wrap").find(".office_obj_agent_wrap");

		var index = 0;

		if(CURRENT_MODE == "3-2") {
			$agentObj.each(function(i) {
				var $room = $("[room-number="+Number(i+1)+"]");

				if (!$room.is(":visible"))
					return true;

				$room.removeClass("room_position_type_01 room_position_type_02 ui_clear_both")
				var row = index / 5;
				var col = index % 5;
				if(col == 0) {
					$room.addClass("room_position_type_01 ui_clear_both")
				} else if(col == 1) {
					$room.addClass("room_position_type_01")
				} else if(col == 2) {
					$room.addClass("room_position_type_01")
				} else if(col == 3) {
					$room.addClass("room_position_type_02 ui_clear_both")
				} else if(col == 4) {
					$room.addClass("room_position_type_02")
				}
				index++;
			});
		} else if(CURRENT_MODE == "2-2") {
			$agentObj.each(function(i) {
				var $room = $("[room-number="+Number(i+1)+"]");

				if (!$room.is(":visible"))
					return true;

				$room.removeClass("room_position_type_01 room_position_type_02 ui_clear_both")
				var row = index / 4;
				var col = index % 4;
				if(col == 0) {
					$room.addClass("room_position_type_01 ui_clear_both")
				} else if(col == 1) {
					$room.addClass("room_position_type_01")
				} else if(col == 2) {
					$room.addClass("room_position_type_02 ui_clear_both")
				} else if(col == 3) {
					$room.addClass("room_position_type_02")
				}
				index++;
			});
		} else if(CURRENT_MODE == "2-1") {
			$agentObj.each(function(i) {
				var $room = $("[room-number="+Number(i+1)+"]");

				if (!$room.is(":visible"))
					return true;

				var row = index / 3;
				var col = index % 3;
				$room.removeClass("room_position_type_01 room_position_type_02 ui_clear_both")
				if(col == 0) {
					$room.addClass("room_position_type_01 ui_clear_both")
				} else if(col == 1) {
					$room.addClass("room_position_type_01")
				} else if(col == 2) {
					$room.addClass("room_position_type_02 ui_clear_both")
				}
				index++;
			});
		} else if(CURRENT_MODE == "1-1") {
			$agentObj.each(function(i) {
				var $room = $("[room-number="+Number(i+1)+"]");

				if (!$room.is(":visible"))
					return true;

				var row = index / 2;
				var col = index % 2;
				$room.removeClass("room_position_type_01 room_position_type_02 ui_clear_both")
				if(col == 0) {
					$room.addClass("room_position_type_01 ui_clear_both")
				} else if(col == 1) {
					$room.addClass("room_position_type_02 ui_clear_both")
				}
				index++;
			});
		}
	}
}