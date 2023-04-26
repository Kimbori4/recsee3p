var objForm, gridCallAll,myCombo;
var chartType;
var currentSelect;
addLoadEvent(callStatisticsLoad);
var currentSelect;

function createCallAllGrid() {
	myCombo = dhtmlXComboFromSelect("selectPeople", null,null,"checkbox");
	myCombo.setSkin("dhx_web")
	myCombo.setImagePath(commResourcePath + "/images/");
	//myCombo.enableAutocomplete(true);
	myCombo.enableFilteringMode(true);
	myCombo.setPlaceholder(lang.statistics.js.alert.msg1/*"사원을 선택하세요"*/)
	myCombo.hide()

	myCombo.attachEvent("onCheck", function(value, state) {
		comboEvent(this.getIndexByValue(value),state);
		return false;
	});

	myCombo.attachEvent("onChange", function(value, text) {
		var indx=this.getSelectedIndex()
		comboEvent(indx,!this.isChecked(indx));
		return false;
	});

	function comboEvent(idx, state){

		myCombo.setChecked(idx,state);
		var count=myCombo.getChecked().length

		myCombo.unSelectOption();
		if(count!=0)
			myCombo.setComboText(lang.statistics.js.alert.msg2/*"총 "*/ + count + lang.statistics.js.alert.msg3/*"명 선택"*/)
		myCombo.openSelect();
	}

	gridCallAll = new dhtmlXGridObject('gridCallAll');
	gridCallAll.setIconsPath(recseeResourcePath + "/images/project/");
	gridCallAll.setImagePath(recseeResourcePath + "/images/project/");
	gridCallAll.i18n.paging = eval("i18nPaging."+locale);
	gridCallAll.enablePaging(true, 20, 1, "pagingGridCall", false);
	gridCallAll.enableColumnAutoSize(false);
	gridCallAll.enablePreRendering(50);
	gridCallAll.setPagingSkin("toolbar", "dhx_web");
	gridCallAll.setSkin("dhx_web");
	gridCallAll.init();
	gridCallAll.load(contextPath + "/statistics/call_statistics_list.xml?init=true", function() {
		gridCallAll.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ gridCallAll.i18n.paging.results+'    '+ gridCallAll.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
		gridCallAll.aToolBar.setWidth("total",80)

		$(window).resize();
		$(window).resize(function() {
			gridCallAll.setSizes();
		});

		gridCallAll.aToolBar.hideItem("perpagenum");
		gridCallAll.attachEvent("onPageChanged", function(ind, find, lInd){
			onGraphProc(find, lInd);
		});
		gridCallAll.attachEvent("onXLS", function(){
			progress.on()
		});
		gridCallAll.attachEvent("onXLE", function(grid_obj,count){
			progress.off();
		});
		gridCallAll.attachEvent("onAfterSorting", function(){
			onGraphProc()
			totalcal();
		});
		gridCallAll.attachEvent("onStatReady", function(){
			totalcal()
		});

		onGraphProc();
		ui_controller();
	}, 'xml');

	return gridCallAll;
}

function callStatisticsLoad() {
	gridCallAll = createCallAllGrid();

	formFunction();
	authyLoad();
	select2InputFilter()
	searchSelectOptionLoad($("#sysCode"),'/selectOption.do?comboType=system&comboType2=&ALL=');
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})

	if(telnoUse == 'Y'){		
		$("#sysCode").hide();
		$("#codeFilter").hide();
		$(".select2").hide();
	}
}

//권한 불러 오기
function authyLoad() {
	if(excelYn != 'Y') {
		$('#excelDownBtn').hide();
	}
}

function formFunction(){
	if(accessLevel=="U"){
		$("#codeFilter").hide()
		$("#codeList").hide()
		$(".select2").hide()
	}else if(accessLevel=="S"){
		$("#mBgCode").hide()
		$("#mMgCode").hide()
		$("#mSgCode").hide()
		comboLoadFunction(myCombo)
		myCombo.show()
		$("#mBgCode").val(userInfoJson.bgCode)
		$("#mMgCode").val(userInfoJson.mgCode)
		$("#mSgCode").val(userInfoJson.sgCode)
		currentSelect='selectPeople';
		$( "#codeFilter option[value='mBgCode']").remove()
		$( "#codeFilter option[value='mMgCode']").remove()
		$( "#codeFilter option[value='mSgCode']").remove()
	}else if(accessLevel=="M"){
		$("#mBgCode").hide()
		$("#mMgCode").hide()
		$("#mSgCode").show()
		$("#mBgCode").val(userInfoJson.bgCode)
		$("#mMgCode").val(userInfoJson.mgCode)
		currentSelect='mSgCode';
		$( "#codeFilter option[value='mBgCode']").remove()
		$( "#codeFilter option[value='mMgCode']").remove()
	}else if(accessLevel=="B"){
		$("#mBgCode").hide()
		$("#mBgCode").val(userInfoJson.bgCode)
		currentSelect='mMgCode';
		$( "#codeFilter option[value='mBgCode']").remove()
	}


	// 분류 코드 필터링
	$( "#codeFilter" ).change(function() {
		$("#mSgCode, #mMgCode").empty()
		$(".select2").remove();
		$(".codeList").hide().removeAttr("multiple").removeClass("select2-hidden-accessible");
		var selectedCode = $( "#codeFilter :selected" ).val()

		switch(selectedCode){
		case "mBgCode":
			$("#mBgCode").show()
			$(".codeList").val("")
			myCombo.hide()
			myCombo.clearAll()
			myCombo.setComboText("")
			break;
		case "mMgCode":
			if(accessLevel=="B"){
				$("#mMgCode").show()
				$("#mBgCode").val(userInfoJson.bgCode).trigger("change");
			}
			else
				$("#mBgCode, #mMgCode").show()
			$(".codeList").val("")
			myCombo.hide()
			myCombo.clearAll()
			myCombo.setComboText("")
			break;
		case "mSgCode":
			if(accessLevel=="M"){
				$("#mSgCode").show()
				$("#mBgCode").val(userInfoJson.bgCode).trigger("change");
				$("#mMgCode").val(userInfoJson.mgCode).trigger("change");
			}
			else if(accessLevel=="B"){
				$("#mMgCode, #mSgCode").show()
				$("#mBgCode").val(userInfoJson.bgCode).trigger("change");
			}
			else
				$("#mBgCode, #mMgCode, #mSgCode").show()
			$(".codeList").val("")
			myCombo.hide()
			myCombo.clearAll()
			myCombo.setComboText("")
			break;
		case "selectPeople":
			$(".codeList").hide()
			$(".codeList").val("")
			myCombo.show()
			myCombo.clearAll()
			myCombo.setComboText("")
			comboLoadFunction(myCombo)
			break;
		}
		$("#"+selectedCode).attr("multiple","multiple").select2({
				placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
			,	allowClear: true
			, 	dropdownAutoWidth : true
			,	width : 'auto'
		});
		$(".select2-search__field").addClass("inputFilter korFilter engFilter");
		$(".dhxcombo_input").addClass("inputFilter korFilter engFilter numberFilter");
	});

	// 대분류
	selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true)

	if(currentSelect!=null){
		switch (currentSelect) {
		case 'mBgCode':

			break;
		case 'mMgCode':
			selectOrganizationLoad($("#mMgCode"), "mgCode",userInfoJson.bgCode,undefined,undefined,true)
			break;
		case 'mSgCode':
			selectOrganizationLoad($("#mSgCode"), "sgCode",userInfoJson.bgCode,userInfoJson.mgCode,undefined,true)
			break;

		default:
			break;
		}

		$("#"+currentSelect).attr("multiple","multiple").css("width","300").select2({
			placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
				,	allowClear: true
		});

	}else{
		selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true)
		$("#"+"mBgCode").attr("multiple","multiple").css("width","300").select2({
			placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
				,	allowClear: true
		});
	}


	/// 대분류 변화시 중분류 로드
	$( "#mBgCode" ).change(function() {
		$("#mSgCode").empty().val(null).trigger("change");
		selectOrganizationLoad($("#mMgCode"), "mgCode", $("#mBgCode").val(),undefined,undefined,true);
		myCombo.clearAll()
		myCombo.setComboText('')
		// 중분류 및의 소분류가 있다면 디폴트로 로드
	});

	// 중분류 변화시 소분류 로드
	$( "#mMgCode" ).change(function() {
		selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),undefined,true)
		myCombo.clearAll()
		myCombo.setComboText('')
	});


	dhtmlx.image_path='${compoResource}/dhtmlxSuite/codebase/imgs/';

	// 날짜 셋팅
//	$('#sDate, #eDate').datepicker().datepicker("setDate", new Date());
	datepickerSetting(locale,'#sDate, #eDate');
	
	//@Kyle
	//캘린더 자동완성 /
	/*$('#sDate, #eDate').keyup(function(e) {
		autoCalendar(this, e)
	});*/

	// 버튼 클릭 이벤트
	// 조회
	$("#searchBtn").click(function(){

		var sDate = $("#sDate").val();
		var eDate = $("#eDate").val();

		if(sDate=='' ||eDate==''){
			alert(lang.statistics.js.alert.msg5/*'시작날짜와 종료날짜를 모두 입력해 주세요'*/);
			return false;
		}

		var dayTimeBy = $( "#day_time_by" ).val();
		chartType=dayTimeBy;

		if(chartType=='dateBy'){
			gridCallAll.rowsBufferOutSize = 30;
		}else if(chartType=='timeBy'){
			gridCallAll.rowsBufferOutSize = 15;
		}


		var data=null;
		var dataArray = new Array();
		var bgCode = $("#mBgCode :selected").val() ? "'"+$("#mBgCode :selected").val()+"'" : ""
		var mgCode = $("#mMgCode :selected").val() ? "'"+$("#mMgCode :selected").val()+"'" : ""
		var sgCode = $("#mSgCode :selected").val() ? "'"+$("#mSgCode :selected").val()+"'" : ""
		var userId = "";
		var sysCode=$("#sysCode  :selected").val();

		var selectedCode = $( "#codeFilter :selected" ).val();

		if($('#'+selectedCode).hasClass("select2-hidden-accessible")){
			data = $('#'+selectedCode).select2('data');
			for(var i = 0; i < data.length ; i++){
				dataArray.push("'"+data[i].id+"'")
			}
		}
		else {
			data=$('#'+selectedCode).val();
			dataArray.push("'"+data+"'")
		}

		switch(selectedCode){
		case"mBgCode":
			bgCode=dataArray.toString();
			break;
		case"mMgCode":
			mgCode=dataArray.toString();
			break;
		case"mSgCode":
			sgCode=dataArray.toString();
			break;
		case"selectPeople":
			for(var i=0;i<myCombo.getChecked().length;i++){
				if(i==myCombo.getChecked().length-1)
					userId+="'"+myCombo.getChecked()[i]+"'";
				else
					userId+="'"+myCombo.getChecked()[i]+"',";
			}
			break;
		}
		var strData;
		if(accessLevel=="U"){
			strData = "sDate="+sDate+"&eDate="+eDate+"&userId="+"'"+userInfoJson.userId+"'"+"&dayTimeBy="+dayTimeBy+"&sysCode="+sysCode
		}else{
			strData = "sDate="+sDate+"&eDate="+eDate+"&bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode+"&userId="+userId+"&dayTimeBy="+dayTimeBy+"&sysCode="+sysCode
		}
		gridCallAll.clearAndLoad(contextPath+'/statistics/call_statistics_list.xml?' + strData, function() {
			gridCallAll.attachFooter(lang.statistics.js.alert.msg6/*전체 합계*/+',#cspan,<div id="total">{#stat_total}</div>,<div id="totalTime">{#stat_total}</div>,<div id="inbound">{#stat_total}</div>,<div id="inboundTime">{#stat_total}</div>,<div id="outbound">{#stat_total}</div>,<div id="outboundTime">{#stat_total}</div>,<div id="transfer">{#stat_total}</div>,<div id="transferTime">{#stat_total}</div>,<div id="conference">{#stat_total}</div>,<div id="conferenceTime">{#stat_total}</div>,<div id="intenal">{#stat_total}</div>,<div id="intenalTime">{#stat_total}</div>,<div id="realCalls">{#stat_total}</div>,<div id="realCallTime">{#stat_total}</div>')
			gridCallAll.setSizes();
			onGraphProc();
			gridCallAll.setCustomSorting("time_custom",3)
			gridCallAll.setCustomSorting("time_custom",5)
			gridCallAll.setCustomSorting("time_custom",7)
			gridCallAll.setCustomSorting("time_custom",9)
			gridCallAll.setCustomSorting("time_custom",11)
			gridCallAll.setCustomSorting("time_custom",13)
			gridCallAll.setCustomSorting("time_custom",15)

			if (gridCallAll.getRowsNum() > 0){
				var setResult = '<div style="width: 100%; text-align: center;">'+ gridCallAll.i18n.paging.results+ (Number(gridCallAll.getRowsNum()))+gridCallAll.i18n.paging.found+'</div>'
				gridCallAll.aToolBar.setItemText("total", setResult)
			}
			gridCallAll.callEvent('onGridReconstructed',[])
		}, 'xml');
	});

	// 엑셀 다운
	$("#excelDownBtn").click(function(){

		var sDate = $("#sDate").val();
		var eDate = $("#eDate").val();
		var day_time_by = $("#day_time_by").val();
		var sysCode = $("#sysCode").val();
		var codeFilter = $("#codeFilter").val();
		var codeFilterResult = "";

		if($("#mBgCode").val()!=null){
			codeFilterResult = $("#mBgCode").val()
		}else if($("#mMgCode").val() != null){
			codeFilterResult = $("#mMgCode").val()
		}else if($("#mSgCode").val() != null){
			codeFilterResult = $("#mSgCode").val()
		}else{
			codeFilterResult = myCombo.getChecked();
		}

		if(gridCallAll.getRowsNum() > 0) {
			gridCallAll.toExcel(contextPath+"/generateExcel.do?fileName=AllCallStatisticsList");
			dhx4.ajax.get(contextPath+"/excelLog.do?fileName=AllCallStatisticsList&sDate="+sDate+"&eDate="+eDate+"&dayTimeBy="+day_time_by+
					"&sysCode="+sysCode+"&codeFilter="+codeFilter+"&codeFilterResult="+codeFilterResult, function( response ){});
		} else {
			//dhtmlx.alert({title:"<spring:message code="message.title.notifications" />", text:"<spring:message code="message.alert.noData" />"});
		}
	});

	if(accessLevel=="U"){
		$("#codeFilter").hide()
		$("#codeList").hide()
		$(".select2").hide()
	}
	
	//fault 관리
	for(var i=1;i<13;i++){
		$("#dataMonth").append("<option value="+i+">"+i+"</option");
	}
	
	$("#day_time_by").change(function(){
		var selectValue = $("#day_time_by").val();
		if(selectValue == 'dateBy' || selectValue == 'timeBy' || selectValue == 'monthBy' || selectValue == 'weekBy' || selectValue == 'dayWeekBy'){
			$("#sDate").show();
			$("#eDate").show();
			$("#dataMonth").hide();
			$("#dataDayWeek").hide();			
		}
	})

}



var onGraphProc = function(str,end) {
	var str;
	var end;
	if(str==undefined){
		str=gridCallAll.getStateOfView()[1];
		end=gridCallAll.getStateOfView()[2];
	}else{
		str=str;
		end=end;
	}
	var rowsNum = gridCallAll.getRowsNum();
	var dataSource = new Array();
	var seriesParm;
	var seriesSet=[];
	if(rowsNum > 0) {

		for(var i = str; i < end; i++) {
			var graphInfo = new Object();

			if(($("#mBgCode :selected").length+$("#mMgCode :selected").length+$("#mSgCode :selected").length)>0){
				graphInfo.group = gridCallAll.cells2(i,0).getValue();
				graphInfo.name = gridCallAll.cells2(i,1).getValue();
				graphInfo.count = parseInt(gridCallAll.cells2(i,2).getValue());
				seriesSet.push(gridCallAll.cells2(i,1).getValue());
				dataSource.push(graphInfo);
			}else{
				graphInfo.group = gridCallAll.cells2(i,0).getValue();
				graphInfo.calls = parseInt(gridCallAll.cells2(i,2).getValue());
				dataSource.push(graphInfo);
				seriesParm=[{valueField: 'calls', name: lang.statistics.js.alert.msg7/*'녹취 건 수'*/, color: '#56CFD2',label:{
					visible:true,
					customizeText: function() {
						return this.value +" "+lang.statistics.js.alert.msg8/*"건"*/;
					}
				},font:{family: 'Noto Sans'}}];
			}
		}
		if(($("#mBgCode :selected").length+$("#mMgCode :selected").length+$("#mSgCode :selected").length)>0){
			var series=[];
			$.each(seriesSet, function(i,el) {
				if($.inArray(el,series)===-1)series.push(el);
			})
			var str=[];
			for (var i=0;i<series.length;i++){
				if(series[i]!="")
					str.push({valueField:series[i], name: series[i],label:{
						visible:true,
						customizeText: function() {
							return this.value +" "+lang.statistics.js.alert.msg8/*"건"*/;
						}
					},font:{family: 'Noto Sans'}});
			}
			seriesParm=str;
			var dataSource1=[];
			for(var i=0;i<dataSource.length;i++){
				var name=dataSource[i].name;
				rawdate=new Object();
				rawdate.group=dataSource[i].group;
				rawdate[name]=dataSource[i].count;
				dataSource1.push(rawdate);
			}
			dataSource=dataSource1;

		}

	}
	$("#chart").dxChart({
		palette:"Soft Pastel",
		dataSource: dataSource,
		baseFontSize: "20",
		commonSeriesSettings: {
			argumentField: 'group',
			type: 'bar',
			label: {
				visible:false,format:'largeNumber',percision:0
			}
		},
		commonPaneSettings: {
			border: {visible:true}
		},

		series:
			seriesParm
		,
		legend: {
			visible: true,
			verticalAlignment: 'top',
			horizontalAlignment: 'right'
		},
		valueAxis: {
			label: {
				format: 'largeNumber'
			},
			min: 0,
//			tickInterval: 10
		},
        argumentAxis: {
            grid: {visible: true}
        },
        tooltip: {
            enabled:true,
            customizeTooltip: function (arg) {
                return {
                    text: arg.valueText
                };
            }
        },export:{
	    	enabled : true,
        	printingEnabled : false,
        	formats : ["JPEG"]
	    },
		commonAxisSettings: {
			visible: true,
			opacity: 0.3
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
function comboLoadFunction (comboObj){
	if (comboObj != null){
		if(accessLevel!='A')
			comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+userInfoJson.bgCode);
		else
			comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+$("#mBgCode").val());
		//comboObj.setSkin(skin);
		//comboObj.enableAutocomplete(autoComplate != true ? false : true );
		//comboObj.enableFilteringMode(filteringMode != true ? false : "between");
		//comboObj.readonly(readonly != true ? false : true);
		return true
	}else
		return false;
}

function searchSelectOptionLoad(objSelect, loadUrl){

	$.ajax({
		url:contextPath+loadUrl,
		data:{"recIp":parent.playerFrame.rc_player.requestIp},
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

function totalcal() {
	var rowsNum=gridCallAll.getRowsNum();
	if(rowsNum > 0) {

		if(gridCallAll.getUserData(rowsNum,"allCalls")==null){
			$("#total").text("0")
		}else{
			$("#total").text(gridCallAll.getUserData(rowsNum,"allCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"allTime")==null){
			$("#totalTime").text("00:00:00")
		}else{
			$("#totalTime").text(gridCallAll.getUserData(rowsNum,"allTime"))
		}
		
		if(gridCallAll.getUserData(rowsNum,"inboundCalls")==null){
			$("#inbound").text("0")
		}else{
			$("#inbound").text(gridCallAll.getUserData(rowsNum,"inboundCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"inboundTime")==null){
			$("#inboundTime").text("00:00:00")
		}else{
			$("#inboundTime").text(gridCallAll.getUserData(rowsNum,"inboundTime"))
		}
		
		if(gridCallAll.getUserData(rowsNum,"outboundCalls")==null){
			$("#outbound").text("0")
		}else{
			$("#outbound").text(gridCallAll.getUserData(rowsNum,"outboundCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"outboundTime")==null){
			$("#outboundTime").text("00:00:00")
		}else{
			$("#outboundTime").text(gridCallAll.getUserData(rowsNum,"outboundTime"))
		}
		
		if(gridCallAll.getUserData(rowsNum,"transferCalls")==null){
			$("#transfer").text("0")
		}else{
			$("#transfer").text(gridCallAll.getUserData(rowsNum,"transferCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"transferTime")==null){
			$("#transferTime").text("00:00:00")
		}else{
			$("#transferTime").text(gridCallAll.getUserData(rowsNum,"transferTime"))
		}
		
		if(gridCallAll.getUserData(rowsNum,"conferenceCalls")==null){
			$("#conference").text("0")
		}else{
			$("#conference").text(gridCallAll.getUserData(rowsNum,"conferenceCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"conferenceTime")==null){
			$("#conferenceTime").text("00:00:00")
		}else{
			$("#conferenceTime").text(gridCallAll.getUserData(rowsNum,"conferenceTime"))
		}
		
		if(gridCallAll.getUserData(rowsNum,"internalCalls")==null){
			$("#intenal").text("0")
		}else{
			$("#intenal").text(gridCallAll.getUserData(rowsNum,"internalCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"internalTime")==null){
			$("#intenalTime").text("00:00:00")
		}else{
			$("#intenalTime").text(gridCallAll.getUserData(rowsNum,"internalTime"))
		}
		
		if(gridCallAll.getUserData(rowsNum,"realCalls")==null){
			$("#realCalls").text("0")
		}else{
			$("#realCalls").text(gridCallAll.getUserData(rowsNum,"realCalls").format())
		}
		
		if(gridCallAll.getUserData(rowsNum,"realCallTime")==null){
			$("#realCallTime").text("00:00:00")
		}else{
			$("#realCallTime").text(gridCallAll.getUserData(rowsNum,"realCallTime"))
		}

		$('#allCalls').html($("#total").text());
		$('#allTime').html($("#totalTime").text());

	} else {
		$('#allCalls').html("0");
		$('#allTime').html("00:00:00");
	}
}

function time_custom(a,b,order) {
	a=a.split(":")
	b=b.split(":")
	if(a[0]==b[0]){
		if(a[1]==b[1]){
			return (Number(a[2])>Number(b[2])?1:-1)*(order=="asc"?1:-1);
		}else{
			return (Number(a[1])>Number(b[1])?1:-1)*(order=="asc"?1:-1);
		}
	}
	else
		return (Number(a[0])>Number(b[0])?1:-1)*(order=="asc"?1:-1);
}