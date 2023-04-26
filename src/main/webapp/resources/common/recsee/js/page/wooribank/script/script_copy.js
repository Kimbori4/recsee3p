// 복사하기 팝업
$(document).on("click", "#copy_script_btn", function() {
	// 팝업창 div 생성
	initScriptCopyPopup();
	if (scriptCopyGrid == null) {
		scriptCopyGrid = createCopyScriptGrid("scriptCopyGrid", "scriptCopyPopupPaging","wooribank/script/api/product/list/notregistered/xml?header=true", "", "", 100, [], []);
		scriptCopyGrid.enableAutoWidth(true);
	}

});

// 검색 엔터 처리
$(document).on("keyup", "#scriptCopySearchText", function(e) {
	if (e.keyCode == 13)
		$("#scriptCopySearchBtn").trigger("click");
});

// 검색어 입력됐을 때만 x 표시 보이기
$(document).on("input", "#scriptCopySearchText", function(){
	if($("#scriptCopySearchText").val()!=""){
		$("#keywordRemoveImg_popup").show();
	}else{
		$("#keywordRemoveImg_popup").hide();
	}
});

// 결재의뢰
$(document).on("click", "#scriptCopyBtn", function(){
	// 현재 체크되어있는 rowID알아내기 
	var checkedRowsList = scriptCopyGrid.getCheckedRows(0);
	if( checkedRowsList.length > 0 ){
		if ( saveApplyDatePopup == null ){
			showCopyCalendar();
		}
	}else{
		alert("미등록 상품을 선택해주세요.");
		return false;
	}
});

/**
 * 화면에서 버튼으로 동작하는 함수의 이벤트 설정
 */
function scFormFunction() {

	// 복사할 상품목록 검색
	$("#scriptCopySearchBtn").click(function() {
		searchScriptNotRegistered();
	});
	
	// 복사 팝업창 닫기 (상단 x버튼)
	$('#scriptCopyCloseTopBtn').bind("click", function() {
		copyGridLoaded = false;
		$('#scriptCopyPopup').remove();
		$('#scriptCopyBox').remove();
		saveApplyDatePopup = null;
		$(this).unbind("click", arguments.callee);
		
		/* 엔터키 입력 금지 이벤트 해제 */
		document.removeEventListener("keydown", blockEnter, true); 
	});

}

/**
 * Grid 생성 함수
 * @param objGrid 그리드가 그려질 태그의 id값
 * @param objPaging 페이징이 그려질 태그의 id값
 * @param url xml 데이터를 가져올 XmlController의 requestMapping 경로
 * @param strData parameter 정보
 * @returns
 */
function createCopyScriptGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {

	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");

	objGrid.i18n.paging = i18nPaging['ko'];
	objGrid.enablePaging(true, initPageRow, 5, objPaging, true);
	objGrid.setPagingWTMode(true, true, true, [ 100, 250, 500 ]);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	// Load Grid
	objGrid.load(contextPath + "/" + url + encodeURI(strData) , function() {
		var grid_toolbar = objGrid.aToolBar;
		grid_toolbar.addSpacer("perpagenum");
		grid_toolbar.addText("total", 0,'<div style="width: 100%; text-align: center">'+ objGrid.i18n.paging.results + objGrid.getRowsNum()+ objGrid.i18n.paging.found + '</div>')
		grid_toolbar.setWidth("total", 80)

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});

		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj, count) {
			ui_controller();
		});

		if (objPaging) {
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj, count) {
			//$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");

			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>';
			objGrid.aToolBar.setItemText("total", setResult);

			ui_controller();
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
			});
		});
		
		// sorting
		objGrid.attachEvent("onBeforeSorting", function(index,type,direction){
			if(index == 0) return;
			
			var pageNo = this.getStateOfView()[0]; // 현재 페이지 번호
			var orderColumn= this.getColumnId(index);		
			
			if(orderColumn == null || orderColumn == "") return;
			
			var orderDirection = direction == "des" ? "desc" : "asc";
			
			var queryString = "";
			queryString += "orderBy="+ orderColumn
			queryString += "&direction="+ orderDirection;
			
			searchScript(queryString, function(){
				objGrid.setSortImgState(true, index, orderDirection);
				objGrid.changePage(pageNo);
			});
			
		});
	});
	
		
	// All CheckBox
	objGrid.attachEvent("onHeaderClick", function(ind, obj) {
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				this.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			}else {
				this.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			 }
		} else {
			return true;
		}
	});
	
	// row별로 uncheck되면 header부분 check 이미지 없음
	objGrid.attachEvent("onCheck", function(rowId, colIndex, isChecked){
		var checkBox = scriptCopyGrid.getCheckedRows(0);
		if (checkBox.length > 0) {
			$("#allcheck>img").attr("src",recseeResourcePath+ "/images/project/dhxgrid_web/item_chk0.gif");
		}
	});

	objGrid.attachEvent("onDataReady", function() {
		console.log('=========onDataReady=========');
		console.log("copyGridLoaded:" + copyGridLoaded);
		if(!copyGridLoaded) {
			copyGridLoaded = true;
			searchScriptNotRegistered();
		}
	});

	return objGrid;
}



function searchScriptNotRegistered() {
	var productType = productJson.rsProductType;
	var data = "";
	data += "keyword=" + $('#scriptCopySearchText').val().trim();
	data += "&searchType=1";
	data += "&productType=" + productType;

	scriptCopyGrid.clearAndLoad(contextPath + "/wooribank/script/api/product/list/notregistered/xml?"+ encodeURI(data));
	
}


// 공용 팝업 초기화
function initScriptCopyPopup(){
	
	// div 생성
	createDiv();
	popupSizing();

	scriptCopyGrid = createCopyScriptGrid("scriptCopyGrid", "scriptCopyPopupPaging","wooribank/script/api/product/list/notregistered/xml?header=true", "", "", 100, [], []);
	
	// 팝업창 기능 추가
	scFormFunction();
	
	/* 중복 팝업 방지 */
	document.addEventListener("keydown", blockEnter, true);

}

// 중복 팝업 방지
function blockEnter(event){
	if(event.keyCode === 13) event.preventDefault();
}

// window resize event
$(window).on('resize', function(){
	var timer = null;
	clearTimeout(timer);
	timer = setTimeout(function(){
		popupSizing();
	}, 100);
});

// popupSizing
function popupSizing(){
	
	var popupMinus;
	var popupHeight = screen.availHeight-250;
	

	if(window.innerWidth>=1800){
		popupMinus = 360;
	}else if(window.innerWidth<=1280){
		popupMinus = 490;
	}else{
		popupMinus = window.innerWidth*0.1;
	}
	
	var popupWidth = window.innerWidth-popupMinus;
	$('#scriptCopyPopup').css("width",popupWidth);
	
	// 팝업위치 중앙정렬
	$('#scriptCopyPopup').css("transform", "translate(-50%, -50%)");
	$('#scriptCopyPopup').css("margin-top", 0);
	$('#scriptCopyPopup').css("margin-left", 0);
	
	var gridMinus = 40;
	var gridWidth = popupWidth-gridMinus;
	var gridHeight = popupHeight-250;
	var detailSize = popupWidth-gridWidth;
	var pagingSize = gridWidth-30;
	
	$('#scriptCopyGrid').css("width", gridWidth);
	$('#scriptCopyGrid').css("height", gridHeight);
	$('#scriptCopyGrid').css("margin-left", "10px");
	$("#productcloseBtn").addClass("enable");
	$('#commonScriptSearchBar').addClass("enable");

	
	// 페이징 가로 사이즈를 공통문구 grid와 맞춤
	$('#scriptCopyPopupPaging').width(gridWidth);
	$('#scriptCopyPopupPaging').find('.dhxtoolbar_float_left').attr("style", "float: none !important; display: flex; justify-content: space-between; height: 100%;");
	$('#scriptCopyPopupPaging').find('.dhxtoolbar_float_left').width(gridWidth);
	
}


// div 생성
function createDiv(){
	var closeSvg = recseeResourcePath + "/images/project/icon/wooribank/modal_close.svg";
	var selectSvg = recseeResourcePath + "/images/project/icon/wooribank/select_arr.svg";
	var html ='<div id="scriptCopyPopup" style="z-index:102">'
			//<!--Header-->
				+'<div id="scriptCopyHeader">'
					+'<span id="scriptCopyTit">스크립트 복사</span>'
					+'<a id="scriptCopyCloseTopBtn" style="background:url(' + closeSvg + ') center/cover no-repeat"></a>'
				+'</div>'
			//<!-- search -->
				+'<div id="scriptCopyBody">'
				+'<div id="scriptCopyField">'
					+ '<div id="searchBox_popup">'
						+ '<input autocomplete="off" id="scriptCopySearchText" type="text" placeholder="상품명 또는 대표상품코드를 입력하세요." style="ime-mode:active">'
						+ '<span id="keywordRemoveImg_popup" onclick="removeCopyKeyword()" style="display:none"></span>'
					+ '</div>'
					+'<button id="scriptCopySearchBtn">조회</button>'
				+'</div>'
			+'</div>'
			//<!--body  -->
				+'<div id="scriptCopyGrid"></div>'
				+'<div id="scriptCopyPopupPaging"></div>'
				+'<div>'
					+'<button id="scriptCopyBtn">결재 의뢰</button>'
				+'</div>'
			+'</div>' ;
	$('body').append("<div id='scriptCopyBox'></div>");
	$("#scriptCopyBox").append(html);
	$("#scriptCopyBox").draggable({
		handle: "#scriptCopyHeader",
    	scroll:false
	});
	
	var marginTop = $(window).height()/2;
	
	$("#scriptCopyBox").css({
        'margin-top' : -marginTop,
        'margin-left' : 0
    });
}


// x버튼 클릭 시 현재 입력된 검색어 지움
function removeCopyKeyword(){
	console.log("키워드삭제버튼 클릭 확인");
	$("#scriptCopySearchText").val("");
	$("#keywordRemoveImg_popup").hide();
	$("#scriptCopySearchText").focus();
}

// 달력 보이기
function showCopyCalendar(){
	/* 적용일 선택 div 팝업 생성 */
	var closeSvg = recseeResourcePath + "/images/project/icon/wooribank/modal_close.svg";
	var html = '<div id="saveApplyDatePopup" style="z-index:104">'
				+ '<div id="saveApplyDateHeader">'
					+ '<span id="applyDateTitle">적용할 날짜를 지정해 주세요</span>'
					+ '<a id="datePopupCloseBtn" style="background:url(' + closeSvg + ') center/cover no-repeat"></a>'
				+ '</div>'
				+ '<div id="saveApplyDateBody">'
					+ '<table id="selectDateTable">'
						+ '<tr>'
							+ '<th>적용예정일</th>'
							+ '<td id="applyDateTd">'
								+ '<input autocomplete="off" type="text" id="copyApplyDate" class="icon_input_cal" style="cursor:pointer;width:120px;">'
							+ '</td>'
						+ '</tr>'
					+ '</table>'
					+ '<div style="text-align:center; padding-bottom:20px"><input type="checkbox" id="nowApply"><label for="nowApply" style="font-size:14px; vertical-align: top; padding:5px">즉시 적용</span></div>'
				+ '</div>'
				+ '<div id="dateButtons">'
					+'<button id="dateSaveBtn" class="date-btn-primary">저장</button>'
					+'<button id="dateNoSaveBtn" class="date-btn-grey">취소</button>'
				+ '</div>'
			+ '</div>';
	$("#dateBox").append(html);
	saveApplyDatePopup = layer_popup("#saveApplyDatePopup");
	
	/* 중복 팝업 방지 */
	document.addEventListener("keydown", blockEnter, true);
	
	/* 캘린더 생성 */
	dhtmlxCalendarObject.prototype.langData["ko"] = {
		dateformat: '%Y-%m-%d',
		hdrformat: '%Y년 %F',
		monthesFNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		monthesSNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		daysFNames: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
		daysSNames: ["일", "월", "화", "수", "목", "금", "토"],
		weekStart: 1
	};
	var saveDateCalendar = new dhtmlXCalendarObject("copyApplyDate");
	saveDateCalendar.setSkin("dhx_terrace");
	saveDateCalendar.loadUserLanguage("ko");
	saveDateCalendar.hideTime();
	saveDateCalendar.setWeekStartDay(7);
	var year = new Date().getFullYear(); // 현재 년도
	var month = new Date().getMonth()+1; // 현재 월
	if(month<10){
		month = "0"+month;
	}
	var day = new Date().getDate()+1; // 다음날
	var tomorrow = new Date(year + "-" + month + "-" + day);
	saveDateCalendar.setDate(tomorrow);
	saveDateCalendar.setSensitiveRange(tomorrow, null);
	
	$("#copyApplyDate").on("click", function(){
		saveDateCalendar.show();
	});
	
	// 즉시적용 체크 시 기존 입력값을 보관, 즉시적용 해제 시 기존 입력값을 다시 가져옴
	var beforeDate = "";
	
	$("#nowApply").on("click", function(){
		if($("input:checkbox[id=nowApply]").is(":checked")){
			if($("#copyApplyDate").val()!=""){
				beforeDate = $("#applyDate").val();
			}
			$("#copyApplyDate").val("");
			$("#copyApplyDate").css('cursor', 'default');
			$("#copyApplyDate").attr("disabled", "disabled");
			$("#copyApplyDate").addClass("readonly");
		}else{
			if(beforeDate != ""){
				$("#copyApplyDate").val(beforeDate);
			}
			$("#copyApplyDate").css('cursor', 'pointer');
			$("#copyApplyDate").removeAttr("disabled");
			$("#copyApplyDate").removeClass("readonly");
		}
	});
	
	/* 오늘 날짜 표시 */
	// 달력이 처음 보일 때 이번달 달력인 경우
	saveDateCalendar.attachEvent("onShow", function(){ // 달력이 표시될 때
		if($('.dhtmlxcalendar_cell_month_dis').length>0){
			var todayIndex = $('.dhtmlxcalendar_cell_month_dis').length-1;
			var today = $('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerText;
			$('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>" + today + "</div>";
		}
	});
	// 월 이동하여 이번달 달력을 보는 경우
	saveDateCalendar.attachEvent("onArrowClick", function(){ // 달력에서 월 이동할 때
		if($('.dhtmlxcalendar_cell_month_dis').length>0){
			var todayIndex = $('.dhtmlxcalendar_cell_month_dis').length-1;
			var today = $('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerText;
			$('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>" + today + "</div>";
		}
	});
	
}

// 저장 버튼 onclick - api 연동
$(document).on("click", '#dateSaveBtn', function() {
	var realProductPk = $("#productInfo").attr("stepfk");
	var productType = productJson.rsProductType;
	// 현재 체크되어있는 rowID알아내기 
	var checkedRowsList = scriptCopyGrid.getCheckedRows(0);
	// 체크된 항목을 담은 array
	var checkedRow = checkedRowsList.split(',');
	// 체크된 항목의 productPK값을 담을 array
	var targetList = [];
	var productPk;
	var RowNum;

	// 추출된 ROWID로 productPK값 추출하여 array에 넣어두기
	for(var i=0; i<checkedRow.length; i++){
		productPk = scriptCopyGrid.cells(checkedRow[i],scriptCopyGrid.getColIndexById('rsProductListPk')).getValue();
		targetList.push(productPk);
	}
	
	if($("#copyApplyDate").val() != ''){
		copyApplyDate = $("#copyApplyDate").val();
		copyApplyType = "reserved";
	}
	
	if($("#copyApplyDate").val() == '' && $("input:checkbox[id=nowApply]").is(":checked")==false){
		alert("적용일이 지정되지 않았습니다.");
		return false;
	}

	if($("#copyApplyDate").val() == '' && $("input:checkbox[id=nowApply]").is(":checked")==true){
		copyApplyDate = null;
		copyApplyType = "immediately";
	}
	
	var info ={
		targetList: targetList,
		applyDate: copyApplyDate,
		applyType: copyApplyType
	}
	
	var callbackSuccess = function(data){
		alert("저장되었습니다.");
		$('#saveApplyDatePopup').remove();
		$("#mask").remove();
		saveApplyDatePopup = null;
		scriptCopyGrid.clearAndLoad(contextPath + "/wooribank/script/api/product/list/notregistered/xml"+"?productType="+productType);
		
	}
	
	var callbackError = function(data){
		alert("일시적인 오류로 작업이 완료되지 않았습니다. 관리자에게 문의하세요.");
		$('#saveApplyDatePopup').remove();
		$("#mask").remove();
		saveApplyDatePopup = null;
	};
	
	putNotRegisteredScriptCopy(realProductPk, info, callbackSuccess, callbackError);
});

// 적용일 팝업창 닫기
$(document).on("click", '#datePopupCloseBtn', function() {
	$('#saveApplyDatePopup').remove();
	$("#mask").remove();
	saveApplyDatePopup = null;
	$(this).off("click", arguments.callee);
});

// 취소 버튼
$(document).on("click", '#dateNoSaveBtn', function() {
	$('#saveApplyDatePopup').remove();
	$("#mask").remove();
	saveApplyDatePopup = null;
	$(this).off("click", arguments.callee);
});