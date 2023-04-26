
addLoadEvent(realtimeGridLoad);

function realtimeGridLoad() {
	// 평가관리 Grid
    gridMonitoringView = new dhtmlXGridObject({
        parent: "gridMonitoringView"
    });
    //"내선번호,상담사 ID,상담사 이름,상태,통화 시간,전화 번호,청취하기,콜센터,지점,실,listening,감청 유지,rtpCode,stime"
    gridMonitoringView.setHeader(convertLanguage("monitoring.grid.headerSetting"));
    gridMonitoringView.setInitWidths("*,*,*,*,*,*,100,100,100,100,0,100,0,0");
    gridMonitoringView.setColAlign("center,center,center,center,center,center,center,center,center,center,center,center,center,center");
    gridMonitoringView.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    gridMonitoringView.setColumnsVisibility("false,false,false,false,false,false,false,false,false,false,true,false,false,false");
    gridMonitoringView.setImagePath(recseeResourcePath + "/images/project/");
    gridMonitoringView.setColSorting("int,int,str,str,int,int,str,str,str,str,na,na");
    //gridMonitoringView.enableSmartRendering(true,100);
    //gridMonitoringView.i18n.paging = eval("i18nPaging."+locale);
    //gridMonitoringView.enablePaging(true, 20, 5, "pagingMonitoringView", true);
    //gridMonitoringView.setPagingSkin("toolbar","dhx_web");
    gridMonitoringView.init();

	$(window).resize();
	$(window).resize(function() {
		gridMonitoringView.setSizes();
	});


	// 이벤트 시작
	gridMonitoringView.attachEvent("onClearAll", function(start,count){
		progress.on();
	});

	// 이벤트 종료
	gridMonitoringView.attachEvent("onDataReady", function(start,count){
		progress.off();
	});

	ui_controller();
}
