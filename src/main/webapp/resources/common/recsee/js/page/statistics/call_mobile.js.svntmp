var objForm, gridCallMobile,myCombo;
var currentSelect;
addLoadEvent(callStatisticsLoad);
var currentSelect;
var excelFileName;
var saveStrData; //페이징 처리로 인해 검색값 저장해두기
var nowSortingColumn; //페이징 처리로 컬럼명

function createMyCombo(){
	myCombo = dhtmlXComboFromSelect("selectPeople", null,null,"checkbox");
	myCombo.setSkin("dhx_web")
	myCombo.setImagePath(commResourcePath + "/images/");
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
}

//모바일 시간, 일, 월별 조회
function createCallMobileGrid() {
	gridCallMobile = new dhtmlXGridObject('gridCallMobile');
	gridCallMobile.setIconsPath(recseeResourcePath + "/images/project/");
	gridCallMobile.setImagePath(recseeResourcePath + "/images/project/");
	gridCallMobile.i18n.paging = eval("i18nPaging."+locale);
	gridCallMobile.enablePaging(true, 20, 1, "pagingGridCall", false);
	gridCallMobile.enableColumnAutoSize(false);
	gridCallMobile.enablePreRendering(50);
	gridCallMobile.setPagingSkin("toolbar", "dhx_web");
	gridCallMobile.setSkin("dhx_web");
	gridCallMobile.init();
	gridCallMobile.load(contextPath + "/statistics/call_mobile_statistics.xml?init=true", function() {
		gridCallMobile.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ gridCallMobile.i18n.paging.results+'    '+ gridCallMobile.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
		gridCallMobile.aToolBar.setWidth("total",80)

		$(window).resize();
		$(window).resize(function() {
			gridCallMobile.setSizes();
		});

		gridCallMobile.aToolBar.hideItem("perpagenum");
		gridCallMobile.attachEvent("onPageChanged", function(ind, find, lInd){
		});
		gridCallMobile.attachEvent("onXLS", function(){
			progress.on()
		});
		gridCallMobile.attachEvent("onXLE", function(grid_obj,count){
			progress.off();
		});
		gridCallMobile.attachEvent("onAfterSorting", function(){
			totalcal();
		});
		gridCallMobile.attachEvent("onStatReady", function(){
			totalcal();
		});

		ui_controller();
	}, 'xml');

	return gridCallMobile;
}

//모바일 그룹별 사용자별 조회
function createGroupAndUserCallMobileGrid() {
	gridCallMobile = new dhtmlXGridObject('gridCallMobile');
	gridCallMobile.setIconsPath(recseeResourcePath + "/images/project/");
	gridCallMobile.setImagePath(recseeResourcePath + "/images/project/");
	gridCallMobile.i18n.paging = eval("i18nPaging."+locale);
	gridCallMobile.enablePaging(true, 20, 1, "pagingGridCall", true);
	gridCallMobile.enableColumnAutoSize(false);
	gridCallMobile.enablePreRendering(50);
	gridCallMobile.setPagingSkin("toolbar", "dhx_web");
	gridCallMobile.setSkin("dhx_web");
	gridCallMobile.init();
	gridCallMobile.load(contextPath + "/statistics/group_user_call_mobile_statistics.xml?init=true&" + saveStrData, function() {
		gridCallMobile.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ gridCallMobile.i18n.paging.results+'    '+ gridCallMobile.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
		gridCallMobile.aToolBar.setWidth("total",80)

		$(window).resize();
		$(window).resize(function() {
			gridCallMobile.setSizes();
		});

		gridCallMobile.aToolBar.hideItem("perpagenum");
		gridCallMobile.attachEvent("onPageChanged", function(ind, find, lInd){
		});
		gridCallMobile.attachEvent("onXLS", function(){
			progress.on()
		});
		gridCallMobile.attachEvent("onXLE", function(grid_obj,count){
			progress.off();
		});
		gridCallMobile.attachEvent("onAfterSorting", function(){
			totalcal();
		});
		gridCallMobile.attachEvent("onStatReady", function(){
			totalcal();
		});
		var divisionBy = $("#divisionBy").val();
		if(divisionBy == "groupBy"){
			gridCallMobile.attachFooter(lang.statistics.js.alert.msg6/*전체 합계*/+',#cspan,#cspan,#cspan,#cspan,<div id="total"></div>,<div id="success_connect"></div>,#cspan,<div id="total_time"></div>,<div id="avg_time"></div>,<div id="user_count"></div>');
		}else{
			gridCallMobile.attachFooter(lang.statistics.js.alert.msg6/*전체 합계*/+',#cspan,#cspan,#cspan,#cspan,<div id="total"></div>,<div id="success_connect"></div>,#cspan,<div id="total_time"></div>,<div id="avg_time"></div>');
		}

		ui_controller();
	}, 'xml');

	return gridCallMobile;
}

//모바일 상세통계
function createDetailCallMobileGrid() {
	gridCallMobile = new dhtmlXGridObject('gridCallMobile');
	gridCallMobile.setIconsPath(recseeResourcePath + "/images/project/");
	gridCallMobile.setImagePath(recseeResourcePath + "/images/project/");
	gridCallMobile.i18n.paging = eval("i18nPaging."+locale);
	gridCallMobile.enablePaging(true, 20, 1, "pagingGridCall", false);
	gridCallMobile.enableColumnAutoSize(false);
	gridCallMobile.enablePreRendering(50);
	gridCallMobile.setPagingSkin("toolbar", "dhx_web");
	gridCallMobile.setSkin("dhx_web");
	gridCallMobile.init();
	gridCallMobile.load(contextPath + "/statistics/detail_call_mobile_statistics.xml?head=true&"+saveStrData, function() {
		gridCallMobile.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ gridCallMobile.i18n.paging.results+'    '+ gridCallMobile.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
		gridCallMobile.aToolBar.setWidth("total",80)

		$(window).resize();
		$(window).resize(function() {
			gridCallMobile.setSizes();
		});

		gridCallMobile.aToolBar.hideItem("perpagenum");
		gridCallMobile.attachEvent("onXLS", function(){
			progress.on();
		});
		gridCallMobile.attachEvent("onXLE", function(grid_obj,count){
			progress.off();
		});
		gridCallMobile.attachEvent("onAfterSorting", function(){
			totalcal();
		});
		gridCallMobile.attachEvent("onStatReady", function(){
			totalcal();
		});

		ui_controller();
	}, 'xml');

	return gridCallMobile;
}

function callStatisticsLoad() {
	createMyCombo();
	gridCallMobile = createCallMobileGrid();

	formFunction();
	authyLoad();
	select2InputFilter()
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})
	
	//멀티 코드 밀리는현상때문에 묶어서 재 동작 시킴
//	$(".select2").remove();
//	$(".codeList").hide().removeAttr("multiple").removeClass("select2-hidden-accessible");
//	var selectedCode = $( "#codeFilter :selected" ).val()
//
//	$("#mBgCode").show()
//				$(".codeList").val("")
//				myCombo.hide()
//				myCombo.clearAll()
//				myCombo.setComboText("")
//
//	$("#"+selectedCode).attr("multiple","multiple").select2({
//					placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
//				,	allowClear: true
//				, 	dropdownAutoWidth : true
//				,	width : 'auto'
//	});
	//멀티 코드 밀리는현상때문에 묶어서 재 동작 시킴
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

		$("#"+currentSelect).attr("multiple","multiple").css("width","300px").select2({
			placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
				,	allowClear: true
		});

	}else{
		selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true)
		$("#"+"mBgCode").attr("multiple","multiple").css("width","300px").select2({
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

		var divisionBy = $("#divisionBy").val();
		switch(divisionBy){
		case "timeBy":
			excelFileName = "MobileCallTimeStats";
			break;
		case "dateBy":
			excelFileName = "MobileCallDailyStats";
			break;
		case "monthBy":
			excelFileName = "MobileCallMonthlyStats";
			break;
		case "groupBy":
			excelFileName = "MobileCallGroupByStats";
			break;
		case "userBy":
			excelFileName = "MobileCallUserByStats";
			break;
		case "detailBy":
			excelFileName = "MobileCallDetailedStats";
			break;		
		}

		var data=null;
		var dataArray = new Array();
		var bgCode = $("#mBgCode :selected").val() ? "'"+$("#mBgCode :selected").val()+"'" : ""
		var mgCode = $("#mMgCode :selected").val() ? "'"+$("#mMgCode :selected").val()+"'" : ""
		var sgCode = $("#mSgCode :selected").val() ? "'"+$("#mSgCode :selected").val()+"'" : ""
		var userId = "";

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
			strData = "sDate="+sDate+"&eDate="+eDate+"&userId="+"'"+userInfoJson.userId+"'"+"&divisionBy="+divisionBy
		}else{
			strData = "sDate="+sDate+"&eDate="+eDate+"&bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode+"&userId="+userId+"&divisionBy="+divisionBy
		}
		
		$("#gridCallMobile").empty();
		//시, 일, 월별 통계 조회시
		if(divisionBy != null && (divisionBy=="timeBy" || divisionBy=="dateBy" || divisionBy == "monthBy")){
			gridCallMobile = createCallMobileGrid();
			gridCallMobile.clearAndLoad(contextPath+'/statistics/call_mobile_statistics.xml?' + strData, function() {
				gridCallMobile.attachFooter(lang.statistics.js.alert.msg6/*전체 합계*/+',<div id="total">{#stat_total}</div>,<div id="success_connect">{#stat_total}</div>,#cspan,<div id="giveup_call">{#stat_total}</div>,#cspan,<div id="no_answer">{#stat_total}</div>,#cspan,<div id="busy_call">{#stat_total}</div>,#cspan');
				gridCallMobile.callEvent('onGridReconstructed',[]);
				
				if (gridCallMobile.getRowsNum() > 0){
					var setResult = '<div style="width: 100%; text-align: center;">'+ gridCallMobile.i18n.paging.results+ (Number(gridCallMobile.getRowsNum()))+gridCallMobile.i18n.paging.found+'</div>'
					gridCallMobile.aToolBar.setItemText("total", setResult)
				}
				
				$(window).resize();
			}, 'xml');
			
		//그룹, 유저별 통계 조회시
		}else if(divisionBy != null && (divisionBy == "groupBy" || divisionBy == "userBy")){
			saveStrData = strData;
			gridCallMobile = createGroupAndUserCallMobileGrid();
			gridCallMobile.clearAndLoad(contextPath+'/statistics/group_user_call_mobile_statistics.xml?' + saveStrData, function() {
				
				if (gridCallMobile.getRowsNum() > 0){
					var setResult = '<div style="width: 100%; text-align: center;">'+ gridCallMobile.i18n.paging.results+ (Number(gridCallMobile.getRowsNum()))+gridCallMobile.i18n.paging.found+'</div>'
					gridCallMobile.aToolBar.setItemText("total", setResult)
				}
				
				gridCallMobile.attachEvent("onBeforePageChanged", function(){
					if(!this.getRowsNum()){
						return false;
					}
					return true;
				});
				gridCallMobile.attachEvent("onStatReady", function(ind, find, lInd){
					ui_controller();
				});
				gridCallMobile.attachEvent("onBeforeSorting", function(ind){
					var a_state = this.getSortingState()

					var direction = "asc"
					if(nowSortingColumn==ind)
						direction = ((a_state[1] == "asc") ? "desc" : "asc");

					var columnId = this.getColumnId(ind);
					
					var requestOrderby = "columnId="+columnId+"&direction="+direction;

					var nowPage = this.getStateOfView()[0]
					this.clearAndLoad(contextPath+"/statistics/group_user_call_mobile_statistics.xml?" + requestOrderby + "&" + saveStrData);
					this.setSortImgState(true,ind,direction)

					var a_state = this.getSortingState()
					nowSortingColumn = a_state[0];
				});
				
				$.ajax({
					url:contextPath+"/mobileUserAndGroupTotalCount.do?" + saveStrData,
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
				
				$(window).resize();
			}, 'xml');
		}else{ //상세 콜 통계
			saveStrData = strData;
			gridCallMobile = createDetailCallMobileGrid()
			gridCallMobile.clearAndLoad(contextPath+'/statistics/detail_call_mobile_statistics.xml?' + saveStrData, function() {
				
				if (gridCallMobile.getRowsNum() > 0){
					var setResult = '<div style="width: 100%; text-align: center;">'+ gridCallMobile.i18n.paging.results+ (Number(gridCallMobile.getRowsNum()))+gridCallMobile.i18n.paging.found+'</div>'
					gridCallMobile.aToolBar.setItemText("total", setResult)
				}
				gridCallMobile.attachEvent("onBeforePageChanged", function(){
					if(!this.getRowsNum()){
						return false;
					}
					return true;
				});
				gridCallMobile.attachEvent("onStatReady", function(ind, find, lInd){
					ui_controller();
				});
				gridCallMobile.attachEvent("onBeforeSorting", function(ind){
					var a_state = this.getSortingState()

					var direction = "asc"
					if(nowSortingColumn==ind)
						direction = ((a_state[1] == "asc") ? "desc" : "asc");

					var columnId = this.getColumnId(ind);
					
					var requestOrderby = "columnId="+columnId+"&direction="+direction;

					var nowPage = this.getStateOfView()[0]
					this.clearAndLoad(contextPath+"/statistics/detail_call_mobile_statistics.xml?" + requestOrderby + "&" + saveStrData);
					this.setSortImgState(true,ind,direction)

					var a_state = this.getSortingState()
					nowSortingColumn = a_state[0];
				});
				
				$(window).resize();
			}, 'xml');
		}
		
		
	});

	// 엑셀 다운
	$("#excelDownBtn").click(function(){

		var sDate = $("#sDate").val();
		var eDate = $("#eDate").val();
		var divisionBy = $("#divisionBy").val();
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
		
		var date = new Date();		
		
		if(divisionBy == "userBy" || divisionBy == "groupBy" || divisionBy == "detailBy"){
			if(gridCallMobile.getRowsNum() > 0) {
				progress.on();

				var grid_link = contextPath+'/mobile_staticExcel.do?' + saveStrData + "&fileName="+excelFileName+"_"+date.nowDate();

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
		}else{
			if(gridCallMobile.getRowsNum() > 0) {
				gridCallMobile.toExcel(contextPath+"/generateExcel.do?fileName="+excelFileName);
				dhx4.ajax.get(contextPath+"/excelLog.do?fileName="+excelFileName+"&sDate="+sDate+"&eDate="+eDate+"&divisionBy="+divisionBy+
						"&codeFilter="+codeFilter+"&codeFilterResult="+codeFilterResult, function( response ){});
			} else {
				//dhtmlx.alert({title:"<spring:message code="message.title.notifications" />", text:"<spring:message code="message.alert.noData" />"});
			}
		}
		
	});

	if(accessLevel=="U"){
		$("#codeFilter").hide()
		$("#codeList").hide()
		$(".select2").hide()
	}
	
	//모바일 녹취 선택 변경시	
	$("#divisionBy").change(function() { 
		var divisionBy = $("#divisionBy option:selected").val();
		switch(divisionBy){
		case "timeBy":
		case "dateBy":
		case "monthBy":
			if($( "#codeFilter option[value='selectPeople']").css("display") == "none")
				$( "#codeFilter option[value='selectPeople']").show();
			break;
		case "groupBy":
		case "userBy":
			if($("#codeFilter").val() == "selectPeople")
				$('#codeFilter').val("mBgCode").trigger('change');
			
			if($( "#codeFilter option[value='selectPeople']").css("display") == "block")
				$( "#codeFilter option[value='selectPeople']").hide();
			break;
		case "detailBy":
			if($( "#codeFilter option[value='selectPeople']").css("display") == "none")
				$( "#codeFilter option[value='selectPeople']").show();
			break;
		}
		
		// 사용자 기준 조회 허용시  모바일 통계 
//		if(accessLevel=="U"){
//			$("#codeFilter").hide()
//			$("#codeList").hide()
//			$(".select2").hide()
//		}else if(accessLevel=="S"){
//			$("#mBgCode").hide()
//			$("#mMgCode").hide()
//			$("#mSgCode").hide()
//			comboLoadFunction(myCombo)
//			myCombo.show()
//			$("#mBgCode").val(userInfoJson.bgCode)
//			$("#mMgCode").val(userInfoJson.mgCode)
//			$("#mSgCode").val(userInfoJson.sgCode)
//			currentSelect='selectPeople';
//			$( "#codeFilter option[value='mBgCode']").remove()
//			$( "#codeFilter option[value='mMgCode']").remove()
//			$( "#codeFilter option[value='mSgCode']").remove()
//		}else if(accessLevel=="M"){
//			$("#mBgCode").hide()
//			$("#mMgCode").hide()
//			$("#mSgCode").show()
//			$("#mBgCode").val(userInfoJson.bgCode)
//			$("#mMgCode").val(userInfoJson.mgCode)
//			currentSelect='mSgCode';
//			$( "#codeFilter option[value='mBgCode']").remove()
//			$( "#codeFilter option[value='mMgCode']").remove()
//		}else if(accessLevel=="B"){
//			$("#mBgCode").hide()
//			$("#mBgCode").val(userInfoJson.bgCode)
//			currentSelect='mMgCode';
//			$( "#codeFilter option[value='mBgCode']").remove()
//		}
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
			comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+userInfoJson.bgCode+"&mobile="+recsee_mobile);
		else
			comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+$("#mBgCode").val()+"&mobile="+recsee_mobile);
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

function totalcal(obj) {
	var rowsNum=gridCallMobile.getRowsNum();
	if(rowsNum > 0) {
		var divisionBy = $("#divisionBy").val();
		if(divisionBy != null && (divisionBy=="timeBy" || divisionBy=="dateBy" || divisionBy == "monthBy")){
			$("#total").text(gridCallMobile.getUserData(rowsNum,"allCalls"))
			$("#success_connect").text(gridCallMobile.getUserData(rowsNum,"success_connect"))
			$("#giveup_call").text(gridCallMobile.getUserData(rowsNum,"giveup_call"))
			$("#no_answer").text(gridCallMobile.getUserData(rowsNum,"no_answer"))
			$("#busy_call").text(gridCallMobile.getUserData(rowsNum,"busy_call"))
		}else if(divisionBy != null && (divisionBy == "groupBy" || divisionBy == "userBy")){
			$("#total").text(obj.totalCalls);
			$("#success_connect").text(obj.success_connect);
			console.log(obj.totalTime)
			$("#total_time").text(String(obj.totalTime).toHHMMSS());
			$("#avg_time").text(String(obj.avgCallTime).toHHMMSS());
			if(divisionBy == "groupBy"){
				$("#user_count").text(obj.total_user);
			}else{				
//				$("#total_dial").text("00:00")				
			}
		}else{
			
		}
		
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

//현재 년월일시분초 뽑는 함수
Date.prototype.nowDate = function() {
	var mm = this.getMonth() + 1; // getMonth() is zero-based
	var dd = this.getDate();
	var HH = this.getHours();
	var MM = this.getMinutes();
	var SS = this.getSeconds();
  return [this.getFullYear(),
          (mm>9 ? '' : '0') + mm,
          (dd>9 ? '' : '0') + dd,
		  (HH>9 ? '' : '0') + HH,
		  (MM>9 ? '' : '0') + MM,
		  (SS>9 ? '' : '0') + SS
         ].join('');
};

//초를 시분초로 변경해주는 함수
String.prototype.toHHMMSS = function () {
    var sec_num = parseInt(this, 10); // don't forget the second param
    var hours   = Math.floor(sec_num / 3600);
    var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
    var seconds = sec_num - (hours * 3600) - (minutes * 60);

    if (hours   < 10) {hours   = "0"+hours;}
    if (minutes < 10) {minutes = "0"+minutes;}
    if (seconds < 10) {seconds = "0"+seconds;}
    return hours+':'+minutes+':'+seconds;
}