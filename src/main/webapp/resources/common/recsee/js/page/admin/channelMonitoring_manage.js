// 전역변수 설정
var gridChannelManage; // 그리드
var system = "", chRes = "";

var myPalette = ['#7CBAB4', '#92C7E2', '#75B5D6', '#B78C9B', '#F2CA84', '#A7CA74'];
var series;
DevExpress.viz.core.registerPalette('mySuperPalette', myPalette);
addLoadEvent(dashboardLoad);
addLoadEvent(channelManageLoad);
function dashboardLoad() {
	createChart();
	ui_controller();
}
function channelManageLoad() {
	gridChannelManage = channelManageGridLoad();
	formFunction();
	authyLoad();
//	$('#sDate').datepicker().datepicker("setDate", new Date());
	datepickerSetting(locale,'#sDate');
	
	$('.codeList').keyup(function(e) {
		if (e.keyCode == 13){
			$("#searchBtn").trigger("click");
		}
			
	});
//	searchSelectOptionLoad($("#sysCode"),'/selectOption.do?comboType=system&comboType2=&ALL=');
}

//권한 불러 오기
function authyLoad() {
}
// 요약 로드
createChart = function() {
	dhx4.ajax.get(contextPath+'/admin/channelMonitoring_data.json', function(response) {
		var jsonData = "";
		if (response.xmlDoc.response != undefined) {
			jsonData = jQuery.parseJSON(response.xmlDoc.response);
		} else {
			jsonData = jQuery.parseJSON(response.xmlDoc.responseText);
		}
		$('#totalChannel').html(jsonData.totalChannel.format());
		$('#totalSizeNone').html(jsonData.totalSizeNone.format());
	
		$('#totalExtChannel').html(jsonData.totalExtChannel.format());
		$('#totalExtYn').html(jsonData.totalExtYn.format());
		$('#totalExtSizeNone').html(jsonData.totalExtSizeNone.format());
	
		window.setTimeout('createChart()', 1000*60);
	});
}
// 채널관리 로드
function channelManageGridLoad() {
    // 채널관리 Grid
	gridChannelManage = new dhtmlXGridObject("gridChannelManage");
	gridChannelManage.setIconsPath(recseeResourcePath + "/images/project/");
	gridChannelManage.setImagePath(recseeResourcePath + "/images/project/");
	gridChannelManage.i18n.paging = i18nPaging[locale];
	gridChannelManage.enablePaging(true, 50, 5, "pagingChannelManage", true);
    gridChannelManage.setPagingWTMode(true,true,true,[50,100,250,500]);
	gridChannelManage.enablePreRendering(50);
    gridChannelManage.setPagingSkin("toolbar", "dhx_web");
	gridChannelManage.enableColumnAutoSize(false);
	gridChannelManage.enablePreRendering(50);
    gridChannelManage.setSkin("dhx_web");
	gridChannelManage.init();
	if($("#search_type_by option:selected").val() == "extBy"){
			 $("#callTypew").hide();
			 $("#extTypew").show();
//			gridChannelManage.clearAndLoad(contextPath+"/channelMonitoring_list.xml?sysCode=" + $("#sysCode").val(), function(){
				gridChannelManage.load(contextPath+"/channelMonitoringExt_list.xml", function(){
				
				var search_toolbar = gridChannelManage.aToolBar;
				
				search_toolbar.addSpacer("perpagenum");
				search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+" "+gridChannelManage.getRowsNum() +'</div>')
				search_toolbar.setWidth("total",150)
	
				$(window).resize();
				gridChannelManage.setSizes();
	
				$(window).resize(function() {
					gridChannelManage.setSizes();
				});
				
				gridChannelManage.aToolBar.setMaxOpen("pages", 5);
			}, 'xml')
	}
	$("#search_type_by").change(function(){
		if($(this).val() == "extBy"){
			$("#extTypew").show();
			 $("#callTypew").hide();
			 
			 $("#mextNum").show();
			 $("#muserId").hide();
			 $("#muserName").hide();
			 $("#mBgCode").hide();
			 $("#mMgCode").hide();
			 $("#mSgCode").hide();
			 $("#muserId").val("");
			 $("#muserName").val("");
			 $("#mBgCode").val("");
			 $("#mMgCode").val("");
			 $("#mSgCode").val("");
			gridChannelManage.clearAndLoad(contextPath+"/channelMonitoringExt_list.xml", function(){
				
//				var search_toolbar = gridChannelManage.aToolBar;
//				
//				search_toolbar.addSpacer("perpagenum");
//				search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+gridChannelManage.getRowsNum()+gridChannelManage.i18n.paging.found+'</div>')
//				search_toolbar.setWidth("total",80)
//
//				$(window).resize();
//				gridChannelManage.setSizes();
//
//				$(window).resize(function() {
//					gridChannelManage.setSizes();
//				});
//				
//				gridChannelManage.aToolBar.setMaxOpen("pages", 5);
			}, 'xml')
		}else if($(this).val() == "userBy"){
			 $("#muserId").show();
			 $("#muserName").show();
			 $("#mextNum").hide();
			 $("#mBgCode").hide();
			 $("#mMgCode").hide();
			 $("#mSgCode").hide();
			 $("#mextNum").val(""); 
			 $("#mBgCode").val("");
			 $("#mMgCode").val(""); 
			 $("#mSgCode").val("");
		}else if($(this).val() == "groupBy"){
			// 대중소 다 검색!!
			selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true,'callCenter')
			$("#mBgCode").prepend($('<option value="">콜센터 전체</option>'));
			$("#mMgCode").prepend($('<option value="">센터 전체</option>'));
			$("#mSgCode").prepend($('<option value="">실 전체</option>'));
			$( "#mBgCode" ).change(function() {
				selectOrganizationLoad($("#mMgCode"), "mgCode", $("#mBgCode option:selected").val(),undefined,undefined,true);
				$("#mMgCode").prepend($('<option value="">센터 전체</option>'));
			});
			$( "#mMgCode" ).change(function() {
				selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),undefined,true)
				$("#mSgCode").prepend($('<option value="">실 전체</option>'));
			});
			// 중 소 만 검색
/*			selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true,'callCenter')
			var BgSelectValues = $.map($("#mBgCode option"), function(e) {return e.value;});
			var MgSelectValues;
			var option = "";
			for(var i = 0; i < BgSelectValues.length; i ++){
				selectOrganizationLoad($("#mMgCode"), "mgCode", BgSelectValues[i],undefined,undefined,true);
				var MgSelectValues = $.map($("#mMgCode option"), function(e) {return e.value;})
				var MgSelectTexts = $.map($("#mMgCode option"), function(e) {return e.innerHTML;})
				for(var j = 0; j < MgSelectValues.length; j ++){
					option += "<option value=" + MgSelectValues[j] + ">" + MgSelectTexts[j] + "</option>";
				}
			}
			$("#mMgCode").empty();
			$("#mMgCode").append($('<option value="">센터 전체</option>'+ option));
			$("#mSgCode").append($('<option value="">실 전체</option>'));
			$( "#mBgCode" ).change(function() {
				$("#mSgCode").empty().val(null).trigger("change");
				selectOrganizationLoad($("#mMgCode"), "mgCode", $("#mBgCode").val(),undefined,undefined,true);
			});
			$( "#mMgCode" ).change(function() {
				selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),undefined,true)
			});*/
			
			$("#mextNum").hide();
			 $("#muserId").hide();
			 $("#muserName").hide();
			 $("#mextNum").val("");
			 $("#muserId").val("");
			 $("#muserName").val("");
			 $("#mBgCode").show();
			 $("#mMgCode").show();
			 $("#mSgCode").show();
		}
		else {
			 $("#callTypew").show();
			 $("#extTypew").hide();
			 
			 $("#mextNum").show();
			 $("#muserId").hide();
			 $("#muserName").hide();
			 $("#mBgCode").hide();
			 $("#mMgCode").hide();
			 $("#mSgCode").hide();
			 $("#muserId").val("");
			 $("#muserName").val("");
			 $("#mBgCode").val("");
			 $("#mMgCode").val("");
			 $("#mSgCode").val("");
				gridChannelManage.clearAndLoad(contextPath+"/channelMonitoring_list.xml", function(){
				
//				var search_toolbar = gridChannelManage.aToolBar;
//				
//				search_toolbar.addSpacer("perpagenum");
//				search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+gridChannelManage.getRowsNum()+gridChannelManage.i18n.paging.found+'</div>')
//				search_toolbar.setWidth("total",80)
//
//				$(window).resize();
//				gridChannelManage.setSizes();
//
//				$(window).resize(function() {
//					gridChannelManage.setSizes();
//				});
//				
//				gridChannelManage.aToolBar.setMaxOpen("pages", 5);
			}, 'xml')
		}
	});
	

	gridChannelManage.attachEvent("onRowSelect", function(id,ind){
	    return;
	});
	

	gridChannelManage.attachEvent("onXLS", function(){
		progress.on()
	});
	
	gridChannelManage.attachEvent("onXLE", function(grid_obj,count){
		
		if (gridChannelManage.getRowsNum() > 0){
			try{
				var setResult = '<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+ gridChannelManage.getRowsNum()+gridChannelManage.i18n.paging.found+'</div>'
				gridChannelManage.aToolBar.setItemText("total", setResult)
			}catch(e){tryCatch(e)}
		}
		
		$(window).resize();
		gridChannelManage.setSizes();

		$(window).resize(function() {
			gridChannelManage.setSizes();
		});
		
		progress.off()
		
	});

    ui_controller();
    return gridChannelManage;
}

function formFunction(){
	// 버튼 클릭 이벤트
	// 조회
	$("#searchBtn").click(function(){
		var sDate = $('#sDate').val().replace(/[:-]/g,'');
		var data=null;
		var dataArray = new Array();
		var extNum = $("#mextNum").val() ? $("#mextNum").val() : '';
		var userId = $("#muserId").val() ? $("#muserId").val() : '';
		var userName=$("#muserName").val() ? $("#muserName").val() : '';
		var mgCode = $("#mMgCode :selected").val() ? $("#mMgCode :selected").val() : ""
		var sgCode = $("#mSgCode :selected").val() ? $("#mSgCode :selected").val() : ""
		
		var strData;
			strData = "&sDate=" + sDate +"&extNum="+extNum+"&userName="+userName+"&userId="+userId+"&mgCode="+mgCode+"&sgCode="+sgCode;
			if($("#search_type_by").val() == "extBy"){
				gridChannelManage.clearAndLoad(contextPath+"/channelMonitoringExt_list.xml?" + encodeURI(strData), function(){
					
//					var search_toolbar = gridChannelManage.aToolBar;
//					
//					search_toolbar.removeText();
//					search_toolbar.addSpacer("perpagenum");
//					search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+gridChannelManage.getRowsNum()+gridChannelManage.i18n.paging.found+'</div>')
//					search_toolbar.setWidth("total",80)
//
//					$(window).resize();
//					gridChannelManage.setSizes();
//
//					$(window).resize(function() {
//						gridChannelManage.setSizes();
//					});
//					
//					gridChannelManage.aToolBar.setMaxOpen("pages", 5);
				}, 'xml')
			} else {
					gridChannelManage.clearAndLoad(contextPath+"/channelMonitoring_list.xml?" + encodeURI(strData), function(){
					
//					var search_toolbar = gridChannelManage.aToolBar;
//					
//					search_toolbar.removeText();
//					search_toolbar.addSpacer("perpagenum");
//					search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+gridChannelManage.getRowsNum()+gridChannelManage.i18n.paging.found+'</div>')
//					search_toolbar.setWidth("total",80)
//
//					$(window).resize();
//					gridChannelManage.setSizes();
//
//					$(window).resize(function() {
//						gridChannelManage.setSizes();
//					});

					gridChannelManage.aToolBar.setMaxOpen("pages", 5);
				}, 'xml')
			}
	});
}


/**
 * 콤보 로드 함수
 * comboObj : 콤보 오브젝트
 * parentCode : 파라미터 값
 * defaultSelect : 기본으로 셋팅 해줄 값 없으면 ""
 * autoComplate : 자동완성 기능 사용 여부
 * defaultSelect : 필터 기능 사용 여부
 * readonly : 읽기전용 사용 여부
 * */

function searchSelectOptionLoad(objSelect, loadUrl){
	
	$.ajax({
		url:contextPath+loadUrl,
		data:{},
		type:"GET",
		dataType:"json",
		async: false,
		cache: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}
function comboLoadFunction (comboObj){
	if (comboObj != null){
		if(accessLevel!='A')
			comboObj.load(contextPath+"/opt/user_combo_option.xml?mgCode="+userInfoJson.sgCode);
		else
			comboObj.load(contextPath+"/opt/user_combo_option.xml?mgCode="+$("#mMgCode").val());
		return true
	}else
		return false;
}
