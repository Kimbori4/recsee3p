var objForm, gridCallUser;
var chartType;
var currentSelect;
var nowSortingColumn;
var hideConference;
var hideTranser;

addLoadEvent(userStatisticsLoad);

function createView() {
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

	gridCallUser = new dhtmlXGridObject("gridCallUser");
	gridCallUser.setIconsPath(recseeResourcePath + "/images/project/");
	gridCallUser.setImagePath(recseeResourcePath + "/images/project/");
	gridCallUser.i18n.paging = i18nPaging[locale];
	gridCallUser.enablePaging(true, 20, 5, "pagingGridCallUser", true);
	gridCallUser.setPagingWTMode(true,true,true,[20,40,240]);
	gridCallUser.setPagingSkin("toolbar", "dhx_web");
	gridCallUser.enableColumnAutoSize(false);
	gridCallUser.setSkin("dhx_web");
	gridCallUser.init();

	gridCallUser.load(contextPath + "/statistics/user_statistics_list.xml?header=true", function() {
		gridCallUser.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ gridCallUser.i18n.paging.results+'    '+ gridCallUser.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
		gridCallUser.aToolBar.setWidth("total",80)

		gridCallUser.aToolBar.hideItem("perpagenum");

		gridCallUser.aToolBar.setMaxOpen("pages", 5);
		gridCallUser.attachEvent("onXLS", function(){
			progress.on()
		});

		/*gridCallUser.attachEvent("onDataReady", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});*/
		//그래프그릴때 아직 그리드가 파싱되지 않아서 오류 발생
		gridCallUser.attachEvent("onPaging", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});

		gridCallUser.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum()){
				return false;
			}
			return true;
		});
		// 소팅 이벤트 커스텀
		gridCallUser.attachEvent("onBeforeSorting", function(ind){
				var a_state = this.getSortingState()

				var direction = "asc"
				if(nowSortingColumn==ind)
					direction = ((a_state[1] == "asc") ? "desc" : "asc");

				var columnId = this.getColumnId(ind);

				formData(columnId, direction)

				this.setSortImgState(true,ind,direction)
				var a_state = this.getSortingState()
				nowSortingColumn = a_state[0];

		});

		gridCallUser.attachEvent("onXLE", function(grid_obj,count){
			var setResult = '<div style="width: 100%; text-align: center;">'+ gridCallUser.i18n.paging.results+ gridCallUser.getRowsNum()+gridCallUser.i18n.paging.found+'</div>'
			gridCallUser.aToolBar.setItemText("total", setResult)
			progress.off();
		})
		gridCallUser.attachFooter(',,,'+lang.statistics.js.alert.msg6/*전체 합계*/+',#cspan,#cspan,#cspan,#cspan,#cspan,<div id="total">0</div>,<div id="totalTime">00:00:00</div>,<div id="inbound">0</div>,<div id="inboundTime">00:00:00</div>,<div id="outbound">'+lang.statistics.js.alert.msg9/*아웃*/+'</div>,<div id="outboundTime">'+lang.statistics.js.alert.msg10/*아웃시간*/+'</div>,<div id="transfer">'+lang.statistics.js.alert.msg11/*전환*/+'</div>,<div id="transferTime">'+lang.statistics.js.alert.msg12/*전환시간*/+'</div>,<div id="conference">'+lang.statistics.js.alert.msg13/*회의*/+'</div>,<div id="conferenceTime">'+lang.statistics.js.alert.msg14/*회의시간*/+'</div>,<div id="intenal">'+lang.statistics.js.alert.msg15/*내선*/+'</div>,<div id="intenalTime">'+lang.statistics.js.alert.msg16/*내선시간*/+'</div>,,,,<div id="TimeTotal0">'+lang.statistics.js.alert.msg17/*합계*/+'</div>,<div id="TimeTotal1">'+lang.statistics.js.alert.msg18/*타임1*/+'</div>,<div id="TimeTotal2">'+lang.statistics.js.alert.msg19/*타임2합*/+'</div>,<div id="TimeTotal3">'+lang.statistics.js.alert.msg20/*타임3합*/+'</div>,<div id="TimeTotal4">'+lang.statistics.js.alert.msg21/*타임4*/+'</div>,<div id="TimeTotal5">'+lang.statistics.js.alert.msg22/*타임5*/+'</div>,<div id="TimeTotal6">'+lang.statistics.js.alert.msg23/*타임6*/+'</div>,<div id="TimeTotal7">'+lang.statistics.js.alert.msg24/*타임7*/+'</div>,<div id="TimeTotal8">'+lang.statistics.js.alert.msg25/*타임8*/+'</div>,<div id="TimeTotal9">'+lang.statistics.js.alert.msg26/*타임9*/+'</div>,<div id="realCalls">'+lang.statistics.js.alert.msg27/*리얼*/+'</div>,<div id="realCallTime">'+lang.statistics.js.alert.msg28/*리얼시간*/+'</div>')
		
		ui_controller();

		gridCallUser.setColumnsVisibility("true,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false")

		if(hideTranser==1){
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("transfer_calls"),true);//transfer_calls
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("transfer_time"),true);//transfer_time
		}
		if(hideConference==1){
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("conference_calls"),true);//conference_calls
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("conference_time"),true);//conference_time
		}

		$(window).resize();
		$(window).resize(function() {
			gridCallUser.setSizes();
		});

	}, 'xml');

	return gridCallUser;
}

function userStatisticsLoad() {
	gridCallUser = createView();
	formFunction();
	authyLoad();
	select2InputFilter()
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})
	showCalls();
	searchSelectOptionLoad($("#sysCode"),'/selectOption.do?comboType=system&comboType2=&ALL=');
	//$("#sysCode").hide();
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
			, dropdownAutoWidth : true
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
			placeholder: "대상 그룹을 선택 해 주세요"
				,	allowClear: true
		});

	}else{
		selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true,'callCenter')
		$("#"+"mBgCode").attr("multiple","multiple").css("width","300").select2({
			placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
				,	allowClear: true
		});
	}

	// 대분류 변화시 중분류 로드
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



	// 날짜 셋팅
	$('#sDate, #eDate').datepicker().datepicker("setDate", new Date());
	datepickerSetting(locale,'#sDate, #eDate');
	
	//@Kyle
	//캘린더 자동완성 /
	/*$('#sDate, #eDate').keyup(function(e) {
		autoCalendar(this, e)
	});*/


	// 버튼 클릭 이벤트
	// 조회
	$("#searchBtn").click(function(){
		gridCallUser.changePage(1)
		 formData();
	});

	// 엑셀 다운
	$("#excelDownBtn").click(function(){

		if(gridCallUser.getRowsNum() > 0) {
			/*gridCallUser.toExcel(contextPath+"/generateExcel.do?fileName=UserCallStatisticsList");
			dhx4.ajax.get(contextPath+"/excelLog.do?fileName=UserCallStatisticsList&sDate="+sDate+"&eDate="+eDate+"&dayTimeBy="+day_time_by+
					"&sysCode="+sysCode+"&codeFilter="+codeFilter+"&codeFilterResult="+codeFilterResult, function( response ){});
					*/
			progress.on();

			var grid_link = contextPath+'/staticExcel.do?' + formToSerialize()+ "&fileName=staticList";

			$("#downloadFrame").attr("src", grid_link);

			if (window.dhx4.isIE) {
				$(window).blur(function(){
					progress.off()
				});
			} else {
				$("#downloadFrame").ready(function(){
					progress.off()
				});
			}
		} else {
			alert(lang.statistics.js.alert.msg29/*"조회된 결과가 없어 엑셀다운로드를 할 수 없습니다."*/)
		}
	});

	if(accessLevel=="U"){
		$("#codeFilter").hide()
		$("#codeList").hide()
		$(".select2").hide()
	}
}

var onGraphProc = function(str,end) {
	var str;
	var end;
	if(str==undefined){
		str=gridCallUser.getStateOfView()[1];
		end=gridCallUser.getStateOfView()[2];
	}else{
		str=str;
		end=end;
	}

	var rowsNum = gridCallUser.getRowsNum();
	var dataSource = new Array();
	var hour=[];
	var seriesParm;
	var seriesSet=[];
	var chkSeriesSet = new Array();
	var sec0=[],sec1=[],sec2=[],sec3=[],sec4=[],sec5=[],sec6=[],sec7=[],sec8=[],sec9=[];
	var sec10=[lang.statistics.js.alert.msg30/*'30초 미만'*/,lang.statistics.js.alert.msg31/*'1분 미만'*/,lang.statistics.js.alert.msg32/*'2분 미만'*/,lang.statistics.js.alert.msg33/*'3분 미만'*/,lang.statistics.js.alert.msg34/*'4분 미만'*/,lang.statistics.js.alert.msg35/*'5분 미만'*/,lang.statistics.js.alert.msg36/*'7분 미만'*/,lang.statistics.js.alert.msg37/*'10분 미만'*/,lang.statistics.js.alert.msg38/*'10분 이상'*/];
	if(rowsNum > 0) {
		for(var i = str; i < end; i++) {
	
			if(chartType.indexOf('callTimeBy')!=-1){
				var graphInfo = new Object();
				sec0.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue());//r_user_id 
				sec1.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("totalCallbytime")).getValue());//totalCallbytime
				sec2.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec30")).getValue());//sec30
				sec3.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec60")).getValue());//sec60
				sec4.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec120")).getValue());//sec120
				sec5.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec180")).getValue());//sec180
				sec6.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec240")).getValue());//sec240
				sec7.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec300")).getValue());//sec300
				sec8.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec420")).getValue());//sec420
				sec9.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("sec600")).getValue());//sec600
				seriesSet.push({valueField:gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue(), name: gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_name")).getValue()+"("+gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()+")"});
			}	
			if(chartType=='dateBy'){
				var graphInfo = new Object();
				graphInfo.group = gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_name")).getValue()+"("+gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()+")";
				graphInfo.calls = parseInt(gridCallUser.cells2(i,gridCallUser.getColIndexById("total_calls")).getValue());
	
				if(gridCallUser.cells2(i,21).getValue()!='')
					graphInfo.minCallTime = Number(((parseInt(gridCallUser.cells2(i,21).getValue()))/60).toFixed(2));
				else
					graphInfo.minCallTime = 0;
	
				if(gridCallUser.cells2(i,22).getValue()!='')
					graphInfo.maxCallTime = Number(((parseInt(gridCallUser.cells2(i,22).getValue()))/60).toFixed(2));
				else
				graphInfo.maxCallTime = 0;
	
				if(gridCallUser.cells2(i,23).getValue()!='')
					graphInfo.avgCallTime = Number(((parseInt(gridCallUser.cells2(i,23).getValue()))/60).toFixed(2));
				else
					graphInfo.avgCallTime = 0;
				dataSource.push(graphInfo);
			}else if(chartType=='timeBy'){
				if(i!=0){
					dataSource.push(graphInfo);
				}
	
				var graphInfo = new Object();
				graphInfo.hour = gridCallUser.cells2(i,0).getValue();
				var name=gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue();
				graphInfo[name] = parseInt(gridCallUser.cells2(i,gridCallUser.getColIndexById("total_calls")).getValue());
				if(i%15==0)
					seriesSet.push({valueField:gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue(), name: gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_name")).getValue()+"("+gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()+")"});
			}else if(chartType=='dayWeekBy'){
				if(i!=0){
					dataSource.push(graphInfo);
				}
	
				var graphInfo = new Object();
				graphInfo.dayOfWeek = gridCallUser.cells2(i,1).getValue();
				var name=gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue();
				graphInfo[name] = parseInt(gridCallUser.cells2(i,gridCallUser.getColIndexById("total_calls")).getValue());

				if(chkSeriesSet.length == 0 || chkSeriesSet.indexOf(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()) == -1){
					seriesSet.push({valueField:gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue(), name: gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_name")).getValue()+"("+gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()+")"});
					chkSeriesSet.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue());
				}
			}else if(chartType=='weekBy'){
				if(i!=0){
					dataSource.push(graphInfo);
				}
	
				var graphInfo = new Object();
				graphInfo.week = gridCallUser.cells2(i,2).getValue();
				var name=gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue();
				graphInfo[name] = parseInt(gridCallUser.cells2(i,gridCallUser.getColIndexById("total_calls")).getValue());

				if(chkSeriesSet.length == 0 || chkSeriesSet.indexOf(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()) == -1){
					seriesSet.push({valueField:gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue(), name: gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_name")).getValue()+"("+gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()+")"});
					chkSeriesSet.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue());
				}
			}else if(chartType=='monthBy'){
				if(i!=0){
					dataSource.push(graphInfo);
				}
	
				var graphInfo = new Object();
				graphInfo.month = gridCallUser.cells2(i,2).getValue();
				var name=gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue();
				graphInfo[name] = parseInt(gridCallUser.cells2(i,gridCallUser.getColIndexById("total_calls")).getValue());
				
				if(chkSeriesSet.length == 0 || chkSeriesSet.indexOf(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()) == -1){
					seriesSet.push({valueField:gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue(), name: gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_name")).getValue()+"("+gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue()+")"});
					chkSeriesSet.push(gridCallUser.cells2(i,gridCallUser.getColIndexById("r_user_id")).getValue());
				}
			}
		}
		if(chartType.indexOf('callTimeBy')!=-1){
			for(var i=0; i<10;i++){
				var graphInfo = new Object();
				graphInfo.time=sec10[i];
				for(var k=0;k<10;k++){
					var name=sec0[k];
					graphInfo[name]=Number(eval('sec'+(i+1))[k]);
				}
				dataSource.push(graphInfo);
			}
		}
	}
	//날짜별
	if(chartType=='dateBy'){
		$("#chart").remove();
		$(".chartWrap").append("<div id='chart'></div>");
		$("#chart").dxChart({
			dataSource: dataSource,
			commonSeriesSettings: {
				argumentField: 'group',
			},argumentAxis:{
				label:{
					overlappingBehavior:"stagger"
				}
			},
			panes:[{
					name:"topPane"
			}, 	   {
					name:"bottomPane"
			}],
			defaultPane:"bottomPane",
			series: [{
					pane:"topPane",
					color:"#b0daff",
					type:"rangeArea",
					rangeValue1Field:"minCallTime",
					rangeValue2Field:"maxCallTime",
					name:lang.statistics.js.alert.msg39/*"녹취시간"*/,
			},		{
					pane:"topPane",
					valueField:"avgCallTime",
					name:lang.statistics.js.alert.msg40/*"평균 녹취시간"*/,
					label:{
						visible:true,
						customizeText: function() {
							return this.valueText + lang.statistics.js.alert.msg41+""/*"분"*/;
						}
					}
			},		{
					type:"bar",
					valueField:"calls",
					name:lang.statistics.js.alert.msg7/*"녹취 건 수"*/,
					label:{
						visible:true,
						customizeText: function() {
							return this.valueText + lang.statistics.js.alert.msg8/*"건"*/;
						}
					}
				}
			],
			valueAxis: [{
				pane:"bottomPane",
				gird:{
					visible:true
				},
				title:{
					text:lang.statistics.js.alert.msg7/*"녹취 건수"*/
				}
			},  {
				pane:"topPane",
				gird:{
					visible:true
				},
				title:{
					text:lang.statistics.js.alert.msg40/*"평균 녹취 시간"*/

				}
			}],export:{
		    	enabled : true,
            	printingEnabled : false,
            	formats : ["JPEG"]
		    },
		    size:{
		    	height:335,
		    },
			legend: {
				verticalAlignment: 'top',
				horizontalAlignment: 'right'
			}

	    });
	}else{
		var argumentField;
		if(chartType=='timeBy'){
			argumentField='hour'
		}else if(chartType=='dayWeekBy'){
			argumentField='dayOfWeek'
		}else if(chartType=='weekBy'){
			argumentField='week'
		}else if(chartType=='monthBy'){
			argumentField='month'
		}else{
			argumentField='time'
		}
		
		$("#chart").remove();
		$(".chartWrap").append("<div id='chart'></div>");
		$("#chart").dxChart({
			dataSource:dataSource,//[{hour:"00",advance1:2,advance2:5,advance3:5},{hour:"01",advance1:4,advance2:6,advance3:4},{hour:"02",advance1:5,advance2:6,advance3:4},{hour:"03",advance1:6,advance2:1,advance3:3},{hour:"04",advance1:6,advance2:1,advance3:2},{hour:"05",advance1:6,advance2:2,advance3:7},{hour:"06",advance1:1,advance2:7,advance3:2},{hour:"07",advance1:1,advance2:7,advance3:4},{hour:"08",advance1:4,advance2:1,advance3:6},{hour:"09",advance1:2,advance2:6,advance3:1},{hour:"10",advance1:8,advance2:3,advance3:4},{hour:"11",advance1:5,advance2:1,advance3:7},{hour:"12",advance1:8,advance2:7,advance3:9},{hour:"13",advance1:8,advance2:6,advance3:9},{hour:"14",advance1:9,advance2:8,advance3:8},{hour:"15",advance1:8,advance2:6,advance3:8},{hour:"16",advance1:7,advance2:2,advance3:6}],
			commonSeriesSettings: {
				argumentField: argumentField,
				type: "line",
			},
			series:seriesSet, //[{valueField: 'advance1', name: 'advance1', color: '#56CFD2'},{valueField: 'advance2', name: 'advance2', color: '#56CFD2'},{valueField: 'advance3', name: 'advance3', color: '#56CFD2'}],
			legend: {
				verticalAlignment: 'top',
				horizontalAlignment: 'right'
			},
	        argumentAxis: {
	        	valueMarginsEnabled:false,
	            grid: {visible: true},
				discreateAxisDivisionMode:"crossLabels"
	        },
	        margin:{
	        	bottom:20
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
	    }).dxChart("instance");
	}
};
function totalcal(result) {
		if(chartType.indexOf('callTimeBy')==-1){
			$('#hidden_text').show();

			if(result.totalCalls==null){
				$("#total").text("0")
			}else{
				$("#total").text(result.totalCalls.format())
			}
			if(result.totalTime==null){
				$("#totalTime").text(seconds2time("0"))
			}else{
				$("#totalTime").text(seconds2time(result.totalTime))
			}
			if(result.inboundCalls==null){
				$("#inbound").text("0")
			}else{
				$("#inbound").text(result.inboundCalls.format())
			}
			if(result.inboundCalls==null){
				$("#inboundTime").text(seconds2time("0"))
			}else{
				$("#inboundTime").text(seconds2time(result.inboundTime))
			}
			if(result.outboundCalls==null){
				$("#outbound").text("0")
			}else{
				$("#outbound").text(result.outboundCalls.format())
			}
			if(result.outboundTime==null){
				$("#outboundTime").text(seconds2time("0"))
			}else{
				$("#outboundTime").text(seconds2time(result.outboundTime))
			}
			if(result.transferCalls==null){
				$("#transfer").text("0")
			}else{
				$("#transfer").text(result.transferCalls.format())
			}
			if(result.transferTime==null){
				$("#transferTime").text(seconds2time("0"))
			}else{
				$("#transferTime").text(seconds2time(result.transferTime))
			}
			if(result.conferenceCalls==null){
				$("#conference").text("0")
			}else{
				$("#conference").text(result.conferenceCalls.format())
			}
			if(result.conferenceTime==null){
				$("#conferenceTime").text(seconds2time("0"))
			}else{
				$("#conferenceTime").text(seconds2time(result.conferenceTime))
			}
			if(result.internalCalls==null){
				$("#intenal").text("0")
			}else{
				$("#intenal").text(result.internalCalls.format())
			}
			if(result.internalTime==null){
				$("#intenalTime").text(seconds2time("0"))
			}else{
				$("#intenalTime").text(seconds2time(result.internalTime))
			}
			if(result.realCalls==null){
				$("#realCalls").text("0")
			}else{
				$("#realCalls").text(result.realCalls.format())
			}
			if(result.realCallTime==null){
				$("#realCallTime").text(seconds2time("0"))
			}else{
				$("#realCallTime").text(seconds2time(result.realCallTime))
			}
			$('#allCalls').html($("#total").text());
			$('#allTime').html($("#totalTime").text());

		}else{
			$("#TimeTotal0").text(result.totalCallbytime.format())
			$("#TimeTotal1").text(result.sec30.format())
			$("#TimeTotal2").text(result.sec60.format())
			$("#TimeTotal3").text(result.sec120.format())
			$("#TimeTotal4").text(result.sec180.format())
			$("#TimeTotal5").text(result.sec240.format())
			$("#TimeTotal6").text(result.sec300.format())
			$("#TimeTotal7").text(result.sec420.format())
			$("#TimeTotal8").text(result.sec600.format())
			$("#TimeTotal9").text(result.moresec600.format())

			$('#allCalls').html(result.totalCallbytime.format());
			$('#hidden_text').hide();
		}
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


function formData(col, order){
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();

	if(sDate=='' ||eDate==''){
		alert(lang.statistics.js.alert.msg5/*'시작날짜와 종료날짜를 모두 입력해 주세요'*/);
		return false;
	}

	var dayTimeBy = $( "#day_time_by" ).val();
	chartType=dayTimeBy;

	//통계 유형에 따른 그리드 페이징 수 분리
	if(chartType=='dateBy'){
		gridCallUser.rowsBufferOutSize = 20;
	}else if(chartType=='timeBy'){
		gridCallUser.rowsBufferOutSize = 150;
	}else if(chartType=='dayWeekBy'){
		gridCallUser.rowsBufferOutSize = 70;
	}else if(chartType=='weekBy'){
		gridCallUser.rowsBufferOutSize = 100;
	}else if(chartType=='monthBy'){
		gridCallUser.rowsBufferOutSize = 80;
	}else{
		gridCallUser.rowsBufferOutSize = 10;
	}

	var data=null;
	var dataArray = new Array();

	var bgCode = $("#mBgCode :selected").val() ? "'"+$("#mBgCode :selected").val()+"'" : ""
	var mgCode = $("#mMgCode :selected").val() ? "'"+$("#mMgCode :selected").val()+"'" : ""
	var sgCode = $("#mSgCode :selected").val() ? "'"+$("#mSgCode :selected").val()+"'" : ""

	var userId = "";
	var sysCode=$("#sysCode  :selected").val();

	var selectedCode = $( "#codeFilter :selected" ).val()

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
		strData = "sDate="+sDate+"&eDate="+eDate+"&userId="+"'"+userInfoJson.userId+"'"+"&dayTimeBy="+dayTimeBy+"&sysCode="+sysCode;
	}else{
		strData = "sDate="+sDate+"&eDate="+eDate+"&bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode+"&userId="+userId+"&dayTimeBy="+dayTimeBy+"&sysCode="+sysCode;
	}

	if(col!=undefined && order!=undefined){
		strData+="&col="+col+"&order="+order+"&dayTimeBy="+dayTimeBy;
	}

	gridCallUser.clearAndLoad(contextPath+'/statistics/user_statistics_list.xml?' + strData, function() {
		if(chartType.indexOf('callTimeBy')>-1){
			gridCallUser.setColumnsVisibility("true,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false,false,false,false,false,false,false,false,false,true,true")
		}else if(chartType.indexOf('dateBy')>-1){
			gridCallUser.setColumnsVisibility("true,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true")
		}else if(chartType.indexOf('timeBy')>-1){
			gridCallUser.setColumnsVisibility("false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true")
		}else if(chartType.indexOf('dayWeekBy')>-1){
			gridCallUser.setColumnsVisibility("true,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true")
		}else if(chartType.indexOf('monthBy')>-1){
			gridCallUser.setColumnsVisibility("true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true")
		}else if(chartType.indexOf('weekBy')>-1){
			gridCallUser.setColumnsVisibility("true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true")
		}
		var a_state = gridCallUser.getSortingState();

		if(a_state[0]!=undefined){
			gridCallUser.setSortImgState(true,a_state[0],a_state[1])
		}
		if(hideTranser==1){
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("transfer_calls"),true);
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("transfer_time"),true);
		}
		if(hideConference==1){
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("conference_calls"),true);
			gridCallUser.setColumnHidden(gridCallUser.getColIndexById("conference_time"),true);
		}


		 $.ajax({
				url:contextPath+"/totalcount.do?"+strData,
				data:{},
				type:"GET",
				dataType:"json",
				async: false,
				cache: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						// 불러온 옵션 추가
						totalcal(jRes.resData.total)
					}
				}
			});

	}, 'xml');
}

//전환, 회의통화 숨김여부
function showCalls() {
	$.ajax({
		url:contextPath+"/useCallType.do",
		data:[],
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success=="Y"){
				hideTranser=jRes.resData.hideTranser;
				hideConference=jRes.resData.hideConference;
			}
		}
	});
}
//엑셀 다운로드위한 쿼리에서만 씀
function formToSerialize(){
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	var dayTimeBy = $( "#day_time_by" ).val();
	var data=null;
	var dataArray = new Array();
	var bgCode ='';
	var mgCode = '';
	var sgCode = '';
	var userId = "";

	var sysCode=$("#sysCode  :selected").val();

	var selectedCode = $( "#codeFilter :selected" ).val()

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
	var strData="sDate="+sDate+"&eDate="+eDate+"&bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode+"&userId="+userId+"&dayTimeBy="+dayTimeBy+"&sysCode="+sysCode;
	return strData;
}

