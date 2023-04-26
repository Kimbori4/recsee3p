
// 전역변수 설정
var gridPacketLogManage; // 그리드


window.onload = function() {
	gridPacketLogManage = gridPacketManageLoad() // 패킷 에러 로그 그리드 로드
	formFunction()
	ui_controller();
}

// 패킷 에러 로그 그리드 로드
function gridPacketManageLoad() {
	objGrid = new dhtmlXGridObject("gridPacketLogManage");
	// 패킷 에러 로그 관리 그리드
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 20, 5, "pagingPacketLogManage", true);
	objGrid.setPagingWTMode(true,true,true,[20,50,100]);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	
	objGrid.load(contextPath+"/packet_log_list.xml?header=true", function(){

		var search_toolbar = objGrid.aToolBar;
		
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results +" "+ objGrid.getRowsNum() +'</div>')
		search_toolbar.setWidth("total",150)
		
		search_toolbar.setMaxOpen("pages", 5);
		
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
	}, 'xml')
	
	objGrid.attachEvent("onXLS", function(){
		progress.on()
	});
	
	objGrid.attachEvent("onXLE", function(grid_obj,count){

		if (objGrid.getRowsNum() > 0){
			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>'
			objGrid.aToolBar.setItemText("total", setResult)
		}
		
		ui_controller();
		progress.off();

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
			$(top.window).trigger("resize");
		});
	});

	objGrid.attachEvent("onRowSelect", function(id,ind){
	    return;
	});
	
	// 로우 더블클릭 시 수정 팝업 오픈
	objGrid.attachEvent("onRowDblClicked", function(id,ind){
		return;
	});

	//체크박스 전체 선택
	objGrid.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				this.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				this.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});
	
	// 페이지 체인지 이벤트
	objGrid.attachEvent("onBeforePageChanged", function(){
		if(!this.getRowsNum())
			return false;
		return true;
	});
	
    ui_controller();
    return objGrid;
}

function formFunction(){
	$('.main_form').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#packetLogSearchBtn").trigger("click");
	})
	
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})

	// 날짜 셋팅
	datepickerSetting(locale,'#sDate, #eDate');
	//@Kyle
	//캘린더 자동완성 /
	$('#sDate, #eDate').keyup(function(e) {
		autoCalendar(this, e)
	});

	// 날짜 조회 보조 옵션
	$("#daySelect").change(function(){
		var selectedOpt = $(this).val();
		var now = new Date();
		switch(selectedOpt){
		case "day":
//			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			datepickerSetting(locale,'#sDate, #eDate', now);
		break;
		case "week" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			now.setDate(now.getDate()-7)
//			$('#sDate').datepicker().datepicker("setDate", now);
			datepickerSetting(locale,'#sDate', now);
		break;
		case "month" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			now.setMonth(now.getMonth()-1)
//			$('#sDate').datepicker().datepicker("setDate", now);
			datepickerSetting(locale,'#sDate', now);
		break;
		case "custom" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", false ).datepicker("setDate", now);
			datepickerSetting(locale,'#sDate, #eDate', now);
		break;
		}
	});

	// 콤보 로드
	selectOptionLoad($("#sTime"),"Time");
	selectOptionLoad($("#eTime"),"Time","e");
	selectOptionLoad($("#sysCode"), "system",null,null,null,null,null,"all")
	
	// 마지막 시간값 가져온다....
	$("#eTime").val($("#eTime").children().last().text()).prop("selected", true);
	
	// 버튼 클릭
	$("#packetLogSearchBtn").click(function(){
		var strData = formToSerialize()
		gridPacketLogManage.clearAndLoad(contextPath+"/packet_log_list.xml?"+encodeURI(strData))
	});
}

function formToSerialize(requestOrderBy){

	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	var sTime = $("#sTime").val();
	var eTime = $("#eTime").val();

	var sysCode = $("#sysCode").val();
	var extNum = $("#extNum").val();
	var callId = $("#callId").val();
	var custPhone = $("#custPhone").val();
	
	var custCode = $("#custCode").val();

	var strData = "limitUse=Y&sDate="+sDate+"&eDate="+eDate+"&sTime="+sTime+"&eTime="+eTime+"&sysCode="+sysCode+"&extNum="+extNum+"&callId="+callId+"&custPhone="+custPhone+"&rCustCode="+custCode;

	return encodeURI(strData);
}