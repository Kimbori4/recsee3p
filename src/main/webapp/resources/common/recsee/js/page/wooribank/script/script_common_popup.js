// 공용 문구 팝업
$(document).on("click", "#common_sentence_btn", function() {
	// 팝업창 div 생성
	$('body').append("<div id='commonBox'></div>")
	initCommonPopup();
	if (scriptCommonGrid == null) {
		scriptCommonGrid = createCommonScriptGrid("scriptCommonGrid", "commonPopupPaging","scriptCommonPopupGrid", "?flag=2", "", 100, [], []);
	}
});


// 검색 엔터 처리
$(document).on("keyup", "#commonSearchText", function(e) {
	if (e.keyCode == 13)
		$("#searchCommonBtn").trigger("click");
});

// 검색어 입력됐을 때만 x 표시 보이기
$(document).on("input", "#commonSearchText", function(){
	if($("#commonSearchText").val()!=""){
		$("#keywordRemoveImg_popup").show();
	}else{
		$("#keywordRemoveImg_popup").hide();
	}
});


/**
 * 화면에서 버튼으로 동작하는 함수의 이벤트 설정
 */
function scFormFunction() {
	
	// 공용 문구 검색
	$("#searchCommonBtn").click(function() {
		var searchName = $("#commonSearchText").val();
		searchScriptCommon('2');
	});
	
	// 공용 문구 팝업창 닫기 (상단 x버튼)
	$('#commonCloseTopBtn').bind("click", function() {
		layer_popup_close();
		$('#commonPopup').remove();
		$('#mask').remove();
		$('#commonBox').remove();
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
function createCommonScriptGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {

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
	objGrid.load(contextPath + "/" + url + ".xml" + encodeURI(strData), function() {
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

			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>';
			objGrid.aToolBar.setItemText("total", setResult);

			ui_controller();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
			});
		});
	});

	objGrid.attachEvent("onDataReady", function() {
		console.log('=========onDataReady=========');
		// scriptCommonGrid 표출 + 스크롤바 표출
		scriptCommonGrid.selectRow(0);
		showCommonScriptDetail(scriptCommonGrid.getSelectedId());
		// detailType 입력
		var selectedCommonPk = scriptCommonGrid.getSelectedId();
		var detailType = scriptCommonGrid.cells(selectedCommonPk, scriptCommonGrid.getColIndexById('detailType')).getValue();
		$("#commonAddBtn").attr("addType", detailType);
	});

	return objGrid;
}

// Grid에 기능 붙이는 함수
function scGridFunction() {
	
	// 선택(싱글클릭)한 공용 문구 상세 조회
	scriptCommonGrid.attachEvent("onRowSelect", function(id, colIndex) {
		var selectedCommonPk = scriptCommonGrid.getSelectedId();
		showCommonScriptDetail(selectedCommonPk);
		var detailType = scriptCommonGrid.cells(selectedCommonPk, scriptCommonGrid.getColIndexById('detailType')).getValue();
		$("#commonAddBtn").attr("addType", detailType);
	});

}

function searchScriptCommon(flag) {

	var data = "";
	data += "flag=2";
	data += "&keyword=" + $('#commonSearchText').val().trim();
	//이름 설명 내용
	data += "&action=" + $('#commonSearchBy option:selected').val();
	//유형
	data += "&scriptType="
			+ $('#commonSearchTTS option:selected').val();

	scriptCommonGrid.clearAndLoad(contextPath + "/scriptCommonPopupGrid.xml?" + encodeURI(data));

}

// 공통문구 상세 (목록 우측)
function showCommonScriptDetail(selectRowNumPk) {
	dataStr = { "selectRowNumPk" : selectRowNumPk };

	$.ajax({
			url : contextPath + "/getCommonScriptDetail.do",
			type : "GET",
			data : dataStr,
			dataType : "json",
			async : false,
			success : function(jRes) {
				if(jRes.resData.searchCommonScriptDetail.length>0){
					var searchCommonScriptDetail = jRes.resData.searchCommonScriptDetail[0];
					$('#common_pk').val(selectRowNumPk);
					
					// 이름, 설명, 내용
					var name = searchCommonScriptDetail.rsScriptCommonName!=null? searchCommonScriptDetail.rsScriptCommonName : '';
					$("#common_name").val(name);
					var desc = searchCommonScriptDetail.rsScriptCommonDesc!=null? searchCommonScriptDetail.rsScriptCommonDesc : '';
					$('#common_desc').val(desc);
					var text = searchCommonScriptDetail.rsScriptCommonText!=null? searchCommonScriptDetail.rsScriptCommonText : '';
					// 가변값이 있을 경우 빨간색으로 색상을 변경해 줌
					if(text.indexOf("{")>-1){
						$('#commonScriptDetailText').text("");
						var html = "";
						html = text.replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi, "}</span>");								
						$('#commonScriptDetailText').append(html);
					}else{
						$('#commonScriptDetailText').text(text);
					}
					$('#commonScriptDetailText').addClass('readonly');
					
					$('.common_input').attr('readonly');
					$('.common_input').addClass('readonly');
					
					// TTS구분
					$('#common_detail_type').attr('disabled', true);
					var type = searchCommonScriptDetail.rsScriptCommonType!=null? searchCommonScriptDetail.rsScriptCommonType : 'T';
					$('#common_detail_type').val(type).prop('selected', true);
					
					// RealTime 구분
					$('#common_detail_realTime').attr('disabled', true);
					var realTime = searchCommonScriptDetail.rsScriptCommonRealTimeTts!=null? searchCommonScriptDetail.rsScriptCommonRealTimeTts : 'Y';
					$('#common_detail_realTime').val(realTime).prop('selected', true);
				}
			}
	
		});
	}

// 공용 팝업 초기화
function initCommonPopup(){
	
	//div 생성
	createDiv();
	popupSizing();
	$('.common_not_updatable').show(); // 작성자, 생성일자, 수정일자 보이기
	
	// row select 가능하도록
	if(scriptCommonGrid!=null){
		scriptCommonGrid.detachEvent(rowSelectDisabled);
	}
	rowSelectDisabled = null;
	$('#scriptCommonGrid').css('cursor', 'default');
	
	// div에 공용문구 목록(좌측화면) 보이기
	scriptCommonGrid = createCommonScriptGrid("scriptCommonGrid", "commonPopupPaging","scriptCommonPopupGrid", "?flag=2", "", 100, [], []);
	
	// 팝업창 기능 추가
	scGridFunction();
	scFormFunction();
	
	/* 중복 팝업 방지 */
	document.addEventListener("keydown", blockEnter, true);
	if(!$("#mask").length) {
        $('body').append('<div id="mask"></div>');
        $('#mask').fadeIn(300);
    }
}

//엔터키로 중복 팝업 방지
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
	$('#common_text').height(popupHeight-365);
	if(window.innerWidth>=1800){
		popupMinus = 360;
	}else if(window.innerWidth<=1280){
		popupMinus = 80;
	}else{
		popupMinus = window.innerWidth*0.1;
	}
	
	var popupWidth = window.innerWidth-popupMinus;
	$('#commonPopup').css("width",popupWidth);
	
	// 팝업위치 중앙정렬
	$('#commonPopup').css("transform", "translate(-50%, -50%)");
	$('#commonPopup').css("margin-top", 0);
	$('#commonPopup').css("margin-left", 0);
	
	var gridMinus = 40;
	var gridWidth = (popupWidth/3)*2-gridMinus;
	var gridHeight = popupHeight-250;
	var detailSize = popupWidth-gridWidth;
	
	$('#scriptCommonGrid').css("width", gridWidth);
	$('#scriptCommonGrid').css("height", gridHeight);
	$('#scriptCommonGrid').css("margin-left", "10px");
	$("#productcloseBtn").addClass("enable");
	$('#commonScriptSearchBar').addClass("enable");
	$('#commonInsertBtn').addClass("enable");
	$('#commonDeleteBtn').addClass("enable");
	$('#commonUpdateBtn').removeClass("disable");
	$('#commonScriptDetail').width(detailSize-15);
	$('#commonEdit').width(detailSize);
	
	// 페이징 가로 사이즈를 공통문구 grid와 맞춤
	$('#commonPopupPaging').width(gridWidth);
	$('#commonPopupPaging').find('.dhxtoolbar_float_left').attr("style", "float: none !important; display: flex; justify-content: space-between; height: 100%;");
	$('#commonPopupPaging').find('.dhxtoolbar_float_left').width(gridWidth);
	
	$('#commonButtons').width(popupWidth);
	
}

// div 생성
function createDiv(){
	var closeSvg = recseeResourcePath + "/images/project/icon/wooribank/modal_close.svg";
	var selectSvg = recseeResourcePath + "/images/project/icon/wooribank/select_arr.svg";
	var html ='<div id="commonPopup">'
				+ '<div id="commonHeader">'
					+ '<span id="common_tit">공용 스크립트 조회</span>'
					+ '<a id="commonCloseTopBtn" style="background:url(' + closeSvg + ') center/cover no-repeat"></a>'
				+ '</div>'
				+ '<div id="commonModalBody">'
					+ '<div id="commonBodyLeft">'
						+ '<div id="commonSearchField">'
							+ '<select id="commonSearchTTS" style="background:url(' + selectSvg + ') right 4px center/16px no-repeat" class="ttsSelect">'
								+ '<option value="all">전체</option>'
							+ '</select>'
							+ '<select id="commonSearchBy" style="background:url(' + selectSvg + ') right 4px center/16px no-repeat">'
								+ '<option value="all">전체</option>'
								+ '<option value="1">이름</option>'
								+ '<option value="2">설명</option>'
								+ '<option value="3">내용</option>'
							+ '</select>'
							+ '<div id="searchBox_popup">'
								+ '<input autocomplete="off" id="commonSearchText" type="text" placeholder="검색어를 입력하세요." style="ime-mode:active">'
								+ '<span id="keywordRemoveImg_popup" onclick="removeKeyword()" style="display:none"></span>'
							+ '</div>'
							+ '<button id="searchCommonBtn">조회</button>'
						+ '</div>'
						+ '<div id="scriptCommonGrid"></div>'
						+ '<div id="commonPopupPaging"></div>'
					+ '</div>'
					+'<div id="commonBodyRight">'
						+'<input type="hidden" id="common_pk" value="" />'
						+'<table id="commonInfoPopupTable" class="commonInfoTable">'
							+'<tr>'
								+'<th>이름</th>'
								+'<td><input id="common_name" type="text" class="readonly common_input" readonly="readonly"></td>'
							+'</tr>'
							+'<tr>'
								+'<th>설명</th>'
								+'<td><input id="common_desc" type="text" class="readonly common_input" readonly="readonly"></td>'
							+'</tr>'
							+'<tr>'
								+'<th>유형</th>'
								+'<td><select id="common_detail_type" class="common_select ttsSelect readonly" disabled="disabled" style="background:url(' + selectSvg + ') right 4px center/16px no-repeat"></select></td>'
							+'</tr>'
							+'<tr>'
								+'<th>실시간</th>'
								+'<td>'
									+ '<select id="common_detail_realTime" class="common_select readonly" disabled="disabled" style="background:url(' + selectSvg + ') right 4px center/16px no-repeat">'
										+ '<option id="common_detail_realTime_Y" value="Y">실시간</option>'
										+ '<option id="common_detail_realTime_N" value="N">비실시간</option>'
									+ '</select>'
								+ '</td>'
							+'</tr>'
							+'<tr>'
								+'<th id="common_text_title">내용</th>'
								+'<td>'
									+'<div id="common_text"  class="readonly script_input" readonly="readonly">'
										+'<pre id="commonScriptDetailText" contenteditable="false"></pre>'
									+'</div>'
								+'</td>'
							+'</tr>'
						+'</table>'
					+'</div>'
				+'</div>'
				+'<div id="commonButtons">'
					+'<!-- 우측 버튼 -->'
					+'<div id="commonButtonRight">'
						+'<button id="commonAddBtn" class="detail_content_btn" addType="">추가</button>'
					+'</div>'
				+'</div>';
			+'</div>';
	
	$("#commonBox").append(html);
	
	$("#commonBox").draggable({
		handle: "#commonHeader",
    	scroll:false
	});
	
	var marginTop = $(window).height()/2;
	
	$("#commonBox").css({
        'margin-top' : -marginTop,
        'margin-left' : 0
    });
	
	// DB에서 SSDT -> recValue 사용 (T: TTS리딩 / S: 직원리딩 / G: 상담가이드 / R: 적합성보고서)
	for(var i=0; i<recValue.length; i++){
		var type = recValue[i].code;
		var typeValue = recValue[i].name;
		var ttsOption = '<option id="popup_contet_script_Type" value='+type+'>'+typeValue+'</option>';
		$('.ttsSelect').append(ttsOption);
	}
}

//x버튼 클릭 시 현재 입력된 검색어 지움
function removeKeyword(){
	$("#commonSearchText").val("");
	$("#keywordRemoveImg_popup").hide();
	$("#commonSearchText").focus();
}