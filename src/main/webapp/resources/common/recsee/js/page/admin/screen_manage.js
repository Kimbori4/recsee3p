// 전역변수 설정
var gridScreenManage; // 그리드

addLoadEvent(screenManageLoad);


function screenManageLoad() {
	screenManageGridLoad();
}


// 스크린 사용자 관리 로드
function screenManageGridLoad() {
    // 스크린 사용자 Grid
	objGrid = new dhtmlXGridObject("gridScreenManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingScreenManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/screen_user_list.xml", function(){
		objGrid.aToolBar.addSpacer("perpagenum");

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		ui_controller();
	}, 'xml')

	objGrid.attachEvent("onXLE", function(elements) {
		ui_controller();
	});

	// 체크박스 전체 선택
	objGrid.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				objGrid.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				objGrid.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

    ui_controller();

}