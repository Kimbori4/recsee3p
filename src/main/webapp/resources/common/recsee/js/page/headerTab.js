var t = 0;
window.sheetList=[];
var gridEvaluation;



//프로그레스 처리
var progress = {
	on: function(mask) {
		if($("#progress").length < 1) {
			$("html").append("<div id='progress'>")
		}
		$("#progress").css("opacity", "0");
		$("#progress").css("height", "100%");
		HoldOn.open({
				theme: "sk-circle"
			,	backgroundColor: "#FFFFFF"
		});
	}, off: function(mask) {
		HoldOn.close();
		$("#progress").remove();
	}
}
/**셀렉트 옵션 불러오기
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : comboType2 => 콤보 기본값 추가 해주기 (default로 값 셋팅 해주면 됨.)
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 *
 * */
function selectOptionLoad(objSelect, comboType, comboType2, selectedIdx, selectedName, selectedValue, empty){

	// 옵션 붙여 넣기 전에 삭제 (대중소 콤보 로드시...)
	$(objSelect).children().remove()

	var dataStr = {
		"comboType" : comboType
		,	"comboType2" : comboType2
		,	"selectedIdx" : selectedIdx
		,	"selectedName" : selectedName
		,	"selectedValue" : selectedValue
	}

	$.ajax({
		url:contextPath+"/selectOption.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				if(empty){
					if(empty=='all')
						$(objSelect).append("<option value=''>"+lang.admin.label.noAuthy+"</option>") /*권한없음*/
					else if(empty=='none')
						$(objSelect).append("<option disabled selected>"+lang.admin.label.authy+"</option><option value=''>"+lang.admin.label.all+"</option>")/*권한등급*//*전체*/
					else
						$(objSelect).append("<option></option>")
				}

				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}

/**대중소 셀렉트 옵션 불러오기
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 * @pram : defaultSelect => 기본값 선택 true/false
 * */
function selectOrganizationLoad(objSelect, comboType, bgCode, mgCode, selectedValue, defaultSelect, subOpt, empty){

	// 옵션 붙여 넣기 전에 삭제
	$(objSelect).children().remove()

	var dataStr = {
			"comboType" : comboType
		,	"mgCode" : mgCode
		,	"bgCode" : bgCode
		,	"selectedValue" : selectedValue
		,	"accessLevel" : accessLevel
		,	"subOpt" : subOpt
	}
	
	$.ajax({
		url:contextPath+"/organizationSelect.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				if(empty){
					if(empty=="Y"){
						$(objSelect).append("<option value=''>"+lang.common.label.Noclassification/* 해당분류없음 */+"</option>")
					}else
						$(objSelect).append("<option></option>")
				}
				
				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
				if(defaultSelect)
					$(objSelect).val("")
			}
		}
	});
}	
$(function() {
	// 새로고침 이벤트 
	top.keyDownEvent(document)
	
$.ajax({
	url:contextPath+"/menu.do",
	data:{},
	type:"POST",
	dataType:"json",
	async: false,
	success:function(jRes){
		// DB에 조회한 계정이 있으면
		if(jRes.success == "Y") {
			// 불러온 옵션 추가
			//$(objSelect).append(jRes.resData.optionResult)

			var menuList = jRes.resData.menuAccessList;
			//메뉴 리스트 로그
			var adminMenuCount = 0;
			for(var i = 0 ; i < menuList.length ;i++){
				var menu = menuList[i];
				var displayLevel = menu.displayLevel;

				var menuTop = menu.programTop.split("|")[0];
				var menuTopSub = menu.programTop.split("|")[1];
				var menuPath = contextPath+menu.programPath;
				var read = (menu.readYn == "Y" ? true : false);
				try{
					var menuName = lang.fn.get('header.menu.label.'+menu.programSrc);
					var adminTopMenuName = lang.fn.get('header.menu.label.administer'); /*계정관리*/
					var adminMenuName = lang.fn.get('admin.menu.'+menu.programSrc);
					var adminSubMenuName = lang.fn.get('admin.menu.li.'+menu.programSrc);
				}catch(e){
				}
				
				switch(menuTop){
					case "Management":
						// 어드민 페이지에서만 좌측 메뉴 출력
						if ($(".admin_lnb").length =! 0){
							// 메뉴 담을 div가 없을때만 추가
							if($(".admin_menu").length == 0){
								htmlString = "<div class='admin_menu'>";
								$(".admin_lnb").append(htmlString)
							}
						}

						if(displayLevel == 1){
							// 셋팅 메뉴가 없다면 div 추가
							if($(".admin_"+menuTopSub).length == 0){
								htmlString = '<ul><p class="admin_menu_tit admin_'+menuTopSub+'">'+adminMenuName+'<span class="ui-icon ui-icon-plus ui-icon-plus-white ui-icons-right"><span></p></ul>';
								$(".admin_menu").append(htmlString)
							}
						}else if(displayLevel == 2){
							// 계정관리 경로 지정
							if($('.icon_menu_admin').find('p').length == 0){
								htmlString ="<p onclick='"+'addTabBar("'+menuPath+'","'+adminTopMenuName+'")'+"'>"+adminTopMenuName+"</p>"
								$(".icon_menu_admin").append(htmlString);
							}
							// 탑 메뉴 눌렀을 때 0번 인덱스의 서브 페이지 호출하게..
							if($(".menu_admin > li a").attr("href") != undefined)
								if($(".menu_admin > li a").attr("href").indexOf("management") > -1)
									$(".menu_admin > li a").attr("href",menuPath);

							htmlString = '<li><a href="'+menuPath+'" target="_self"><p>'+adminSubMenuName+'</p></a></li>'
							$(".admin_"+menuTopSub).parent().append(htmlString);

							adminMenuCount++;
						}
						break;
				}
				//마지막, 페이지 수 계산하여 메뉴 삭제 처리
				if(i == menuList.length-1){
					$(".menu_agent > li").each(function(i){
						if($(this).find("li").length == 1)
							$(this).find("li").hide();
						else if($(this).find("li").length == 0)
							$(this).remove();
					});
					$(".admin_menu ul").each(function(i){
						if($(this).find("li").length == 0)
							$(this).remove();
					});
					if (adminMenuCount == 0)
						$(".menu_admin").remove();
				}
			}
			if(tabMode!='Y')
				onActiveMenu();
		}
	}
});
});
