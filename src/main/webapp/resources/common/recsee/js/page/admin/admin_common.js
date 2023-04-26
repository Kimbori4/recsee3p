// 권한 리스트 공통 이벤트
$(function() {
	$('.admin_menu').css("display","none");

	loadAuthy();
	menuClickEvent();
})

// 리스트 로드
function loadAuthy(){

	var $groupList = $('.group_list').find('.group_name');
	$groupList.empty();
	$.ajax({
		url: contextPath+"/getAuthyList.do",
		data: {},
		type: "POST",
		dataType: "json",
		success: function(jRes) {
			var authyList = jRes.resData.authyList;
			// 권한 리스트 뿌리기
			for(i=0; i< authyList.length; i++) {
				var authyName = authyList[i].levelName;
				var authyCode = authyList[i].levelCode;

				//마스타 권한 외의 사람은 마스타 권한 안보이게 ㅠ
				if(userInfoJson.userLevel != "E1001"){
					if(authyCode == "E1001"){
						continue;
					}
				}
				$groupList.append('<li><div class="group_name_wrap" level-code="'+authyCode+'"><p class="icon_authy_common icon_authy_'+authyCode+'">' + authyName + '</p></div></li>');

				//마지막에 이벤트 붙여주기
				if(i== authyList.length -1){
					attachEvent();
				}
			}
		}
	});

	// 플레이어 비활성화
	top.playerVisible(false);
	
}

function menuClickEvent(){
	//로드 후 메뉴 숨기기


	setTimeout(function(){
		$('.admin_menu').css("display","block");
	},500);

	$('.admin_menu').parent().find('li').slideUp(0);

	//클릭 이벤트
	$('.admin_menu ul p').click(function(){
		var idx = $(this).index('.admin_menu ul p');
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).find('span').removeClass('ui-icons-rotate');
			$('.admin_menu p').eq(idx).parent().find('li').slideUp(600);
		}else{
			$(this).addClass('open');
			$(this).find('span').addClass('ui-icons-rotate');
			$('.admin_menu p').eq(idx).parent().find('li').slideDown(600);
		}
	})

	var menuLiLength = $('.admin_menu ul').find('li').length;
	for(var i=0;i<menuLiLength;i++){
		if($('.admin_menu ul li').eq(i).css("background-color") != "" && $('.admin_menu ul li').eq(i).css("background-color") != "transparent" && $('.admin_menu ul li').eq(i).css("background-color") !="rgba(0, 0, 0, 0)" ){
			$('.admin_menu ul li').eq(i).parent().find('.admin_menu_tit').trigger("click");
		}
	}
}

// 이벤트 붙이기
function attachEvent(){
	var $listActivTarget = $('.group_name_wrap')

	// 첫 번 째 메뉴 기본 활성화
	//$listActivTarget.eq(0).addClass('group_name_wrap_active');
	$("[level-code='"+userLevel+"']").addClass('group_name_wrap_active');

    // 클릭 시 Active 적용
	$listActivTarget.on("click", function(event){
        event.stopPropagation();
        // 선택사항 초기화
        $listActivTarget.each(function() {
            $(this).removeClass('group_name_wrap_active');
        });
        // Active 활성화
        $(this).addClass('group_name_wrap_active');

        // 클릭시 이벤트 함수 호출

        // 권한관리 페이지 일때 호출할 함수
        if(typeof clickEventAuthy == "function")
        	clickEventAuthy($(this).attr("level-code"));

        // 검색 조회 관리 페이지 일때 호출할 함수
        else if(typeof clickEventSearch == "function")
        	clickEventSearch($(this).attr("level-code"));

    });
}

function addAuthorImg() {
}
