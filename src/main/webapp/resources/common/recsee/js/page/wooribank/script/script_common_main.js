$(document).ready(function(){
	gridSizing(); // 페이지 로드 시 사이징
	// DB에서 SSDT -> recValue 사용 (T: TTS리딩 / S: 직원리딩 / G: 상담가이드 / R: 적합성보고서)
	
	// tts 셀렉트 타입 설정 - 검색용
	for(var i=0; i<recValue.length; i++){
		var type = recValue[i].code;
		var typeValue = recValue[i].name;
		var ttsOption = '<option id="popup_contet_script_Type" value='+type+'>'+typeValue+'</option>';
		$('.ttsSelect').append(ttsOption);
	}
	// tts 라디오 버튼 설정 - 작성용
	for(var i=0; i<recValue.length; i++){
		var type = recValue[i].code;
		var typeValue = recValue[i].name;
		var ttsOption = '<span style="margin-right:50px"><input type="radio" class="commonRadio" id='+type+' name="ttsOption" value='+type+' disabled><label for='+type+'>'+typeValue+'</label></span>';
		$('.ttsRadio').append(ttsOption);
	}
	
	if (scriptCommonGrid == null) {
		scriptCommonGrid = scCreateGrid("scriptCommonGrid", "commonPaging","scriptCommonMainGrid", "?flag=2", "", 10, [], []);
	}
	// 최초 진입 시 화면 사이즈에 따라 그리드,테이블 사이즈 설정
	gridSizing();
	
	// 화면 이동 시 리사이즈
	$(window).on('resize', function(){
		var timer = null;
		clearTimeout(timer);
		timer = setTimeout(function(){
			gridSizing();
		}, 100);
	});
	
	scFormFunction();
	scGridFunction();
	
	// 검색 엔터 처리
	$('#searchCommon').keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});
	
	// 검색어 입력됐을 때만 x 표시 보이기
	$("#searchCommon").on("input", function(){
		if($("#searchCommon").val()!=""){
			$("#keywordRemoveImg").show();
		}else{
			$("#keywordRemoveImg").hide();
		}
	});

}); // document ready 종료

//화면 이동 시 리사이즈
$(window).on('resize', function(){
	
	// 검색어 입력 width 조정
	var notSearchBoxWidth = $("#scriptTypeTitle").width() + $("#commonSearchTTS").width() + $("#searchTypeTitle").width() + $("#commonSearchBy").width() + $("#searchBtn").width() + 100;
	var searchBoxWidth = window.innerWidth-notSearchBoxWidth;
	var searchCommonWidth = searchBoxWidth-40;
	
	$("#searchBox").width(searchBoxWidth);
	$("#searchCommon").attr("style", "width:" + searchCommonWidth + "px !important");
	
	
});

function gridSizing(){
	var headerHeight = 95; // 상단 헤더 height 항상 고정값 + 서로 다른 파일에 있어서 높이 직접 구할 수 없으므로 고정값 변수 할당
	var bodyHeight = window.innerHeight - headerHeight;
	
	var gridHeight = bodyHeight - $("#filter").height() - $("#commonEdit").height() - 150;
	
	$('#scriptCommonGrid').css("width", "100%");
	$('#scriptCommonGrid').height(gridHeight);
	
	if(window.innerWidth>1800){
		$('.common_main_input').css("width", "26%");
		$('#common_text').css("width", "98%");
	}else{
		$('.common_main_input').css("width", "40%");
		$('#common_text').css("width", "97%");
	}
	
}

/**
 * 화면에서 버튼으로 동작하는 함수의 이벤트 설정
 */
function scFormFunction() {
	
	// 적용일 팝업닫기(x버튼) 클릭
	$(document).on("click", '#commonDatePopupCloseBtn', function() {
		layer_popup_close();
		$('#saveCommonApplyDatePopup').remove();
		$('#mask').remove();
		$("#commonScriptDetailText").attr("contenteditable",true); 
		$(this).off("click", arguments.callee);
	});
	
	// 적용일 팝업창 취소 버튼 클릭 (팝업닫기와 동일한 동작)
	$(document).on("click", '#commonDateNoSaveBtn', function() {
		layer_popup_close();
		$('#saveCommonApplyDatePopup').remove();
		$('#mask').remove();
		$("#commonScriptDetailText").attr("contenteditable",true); 
		$(this).off("click", arguments.callee);
	});

	// 적용일 저장
	$(document).on("click", "#commonDateSaveBtn", function() {
		
		if($("#applyDate").val() != ''){
			applyDate = $("#applyDate").val();
			applyType = "reserved";
		}
		
		if($("#applyDate").val() == '' && $("input:checkbox[id=nowApply]").is(":checked")==false){
			alert("적용일이 지정되지 않았습니다.");
			return false;
		}
		
		if($("#applyDate").val() == '' && $("input:checkbox[id=nowApply]").is(":checked")==true){
			applyDate = null;
			applyType = "immediately";
		}
		
		var name = $('#common_name').val().trim();
		var desc = $('#common_desc').val().trim();
		
		var text = $('#commonScriptDetailText').text().trim();
		text = text.replace(/\</g, "&lt;");
		text = text.replace(/\>/g, "&gt;");
		text = text.replace(/\"/g, "&quot;");
		text = text.replace(/\'/g, "&#39;");
		text = text.replace("<br>", "\n");
		
		var type = $('input[name=ttsOption]:checked').val();
		var realtimeTTS = $('input[name=realtimeOption]:checked').val();
		var pk = scriptCommonGrid.getSelectedId();
		
		progress.on();
		
		switch(commonEditType){
			case "insert":
				/* 공용 스크립트 추가 API */
				
				var info = {
					name: name,
					desc: desc,
					text: text,
					type: type,
					realtimeTTS: realtimeTTS,
					applyDate: applyDate,
					applyType: applyType
				};
				
				var callbackSuccess = function(data){
					progress.off();
					alert("저장되었습니다.");
					$("#insertUpdate").css("visibility", "hidden");
					$('#saveCommonApplyDatePopup').remove();
					$('#mask').remove();
					// row select 가능하도록
					scriptCommonGrid.detachEvent(rowSelectDisabled);
					rowSelectDisabled = null;
					$('#scriptCommonGrid').css('cursor', 'default');
					// 0번째 로우 조회
					showCommonScriptDetail(scriptCommonGrid.getRowId(0));
					// 버튼 초기화
					showInitButtons();
					// 입력불가 처리
					setReadonly();
					// 검색 활성화
					setSearchAvailable();
					// pre태그 비활성화
					$("#commonScriptDetailText").attr("contenteditable",false); 
				};
				
				var callbackError = function(data){
					progress.off();
					alert("일시적인 오류가 발생했습니다. 다시 시도해주세요.");
					console.log(JSON.stringify(data));
					// pre 태그 비활성화
					$("#commonScriptDetailText").attr("contenteditable",false);
				};
				
				addCommon(info, callbackSuccess , callbackError );
				break;
				
			case "update":
				/* 공용 스크립트 수정 API */
				var info = {
					name: name,
					desc: desc,
					text: text,
					type: type,
					pk: pk,
					realtimeTTS: realtimeTTS,
					applyDate: applyDate,
					applyType: applyType
				};
				
				var callbackSuccess = function(data){
					progress.off();
					alert("저장되었습니다.");
					$("#insertUpdate").css("visibility", "hidden");
					$('#saveCommonApplyDatePopup').remove();
					$('#mask').remove();
					// row select 가능하도록
					scriptCommonGrid.detachEvent(rowSelectDisabled);
					rowSelectDisabled = null;
					$('#scriptCommonGrid').css('cursor', 'default');
					// 타이틀 초기화
					$('#commonHeader').css('background-color', '#1760F0');
					$('#common_tit').text('공용 스크립트 조회');
					// 수정 항목 재조회
					showCommonScriptDetail(selectedRowBeforeEdit);
					// 버튼 초기화
					showInitButtons();
					// 입력불가 처리
					setReadonly();
					// 검색 활성화
					setSearchAvailable();
					// 변경표시 css 원복
					setOriginCss();
				};
			
				var callbackError = function(data){
					progress.off();
					alert("일시적인 오류가 발생했습니다. 다시 시도해주세요.");
					console.log(JSON.stringify(data));
				};
				
				editCommon(info, callbackSuccess , callbackError );
				break;
			
			case "delete":
				/* 공용 스크립트 삭제 API */
				var info = {
					pk: pk,
					applyDate: applyDate,
					applyType: applyType
				};
				
				var callbackSuccess = function(data){
					progress.off();
					alert("저장되었습니다.");
					$('#saveCommonApplyDatePopup').remove();
					$('#mask').remove();
				};
				
				var callbackError = function(data){
					progress.off();
					if(data.result!='-3006'){
						alert("삭제할 수 없습니다. 관리자에게 문의하세요.");
					}
				};
				
				deleteCommon( info , callbackSuccess , callbackError );
				break;
		}
	});
	
	// 공용 문구 검색
	$("#searchBtn").click(function() {
		var data = "";
		data += "flag=2";
		data += "&keyword=" + $('#searchCommon').val();
		data += "&action=" + $('#commonSearchBy option:selected').val();
		data += "&scriptType=" + $('#commonSearchTTS option:selected').val();

		scriptCommonGrid.clearAndLoad(contextPath + "/scriptCommonMainGrid.xml?" + encodeURI(data));
	});

	// 공용 문구 수정버튼
	$('#commonUpdateBtn').on('click', function() {
		// 공용스크립트 가변값 가져오기 ajax
		checkRealTimeTTS();
		// 가변값 데이터를 기반으로 자동완성 리스트 표출하기
		automaticPhrase(realTimeValueArray);
		
		/* 공용 스크립트 편집 가능 확인 API */
		var pk = $('#common_pk').val();
		
		var callbackCheckSuccess = function(data){
			commonUpdateInputBoxChange();
			// 선택된 row id 저장 : 선택된 row가 없을 때(최초 로드한 상세를 수정하는 경우)는 목록의 첫 번째 id
			selectedRowBeforeEdit = scriptCommonGrid.getSelectedId()==null? scriptCommonGrid.getRowId(0) : scriptCommonGrid.getSelectedId();
			// 우측 하단 (수정)저장, 취소 버튼만 보이기
			$('#commonUpdateBtn').removeClass('enable');
			$('#commonUpdateBtn').addClass('disable');
			$('#commonUpdateBtn').css('display', 'none');
			$('#commonInsertSaveBtn').addClass('disable');
			$('#commonInsertSaveBtn').css('display', 'none');
			$('#commonUpdateSaveBtn').removeClass('disable');
			$('#commonUpdateSaveBtn').css('display', 'block');
			$('#commonUpdateCancelBtn').removeClass('disable');
			$('#commonUpdateCancelBtn').css('display', 'block');
			$('#commonInsertCancelBtn').addClass('disable');
			$('#commonInsertCancelBtn').css('display', 'none');
			// 조회 버튼 및 검색창 비활성화
			$('#searchBtn').css('pointer-events', 'none');
			$('#commonSearchTTS').attr("disabled", "disabled");
			$('#commonSearchTTS').addClass("readonly");
			$('#commonSearchBy').attr("disabled", "disabled");
			$('#commonSearchBy').addClass("readonly");
			$('#searchCommon').attr("disabled", "disabled");
			// 좌측 상단 '수정' div 표시
			$("#insertUpdate").attr("style", "background-color:#44bb97; color:white; visibility:visible; width:90px; height:20px; line-height:20px; margin-left:30px; font-weight:bold; margin-top:50px; margin-bottom:5px; text-align:center");
			$("#insertUpdate").html("스크립트 수정");
			// 최종수정일자 tr 안 보이게 하기
			$("#updateDate").css("visibility", "hidden");
			// 좌측 하단 신규, 삭제 숨기기
			hideInsertDeleteBtn();
			// 수정 진입 후 그리드 셀렉트 불가
			disableSelect();
			// 수정한 부분에 red border
			orgCommonData = {
				commonName: $("#common_name").val(),
				commonDesc: $("#common_desc").val(),
				commonTTS: $("input[name=ttsOption]:checked").val(),
				commonRealtime: $("input[name=realtimeOption]:checked").val(),
				commonText: $("#commonScriptDetailText").text()
			};
			colorIfEdited(orgCommonData);
			
			$('#common_text').removeAttr("readonly");
			$('#common_text').removeClass("readonly");
			$('#commonScriptDetailText').removeClass("readonly");
			// pre 태그 활성화
			$("#commonScriptDetailText").attr("contenteditable",true); 
		};
			
		var callbackCheckError = function(data){
			if(data.result!='-3001'){
				alert("수정할 수 없습니다. 관리자에게 문의하세요.");
			}
		};
		
		checkCommon(pk, callbackCheckSuccess , callbackCheckError);

	});
	
	// 공용스크립트 수정 저장
	$('#commonUpdateSaveBtn').on('click', function() {
		// 가변값 리스트 활성화 되던거 없어지기
		$(".autoList").addClass('hide');
		
		// 가변값 리스트 활성화 되던거 없어지기
		$(".autoList").addClass('hide');
		if($("#common_name").val()==orgCommonData.commonName
				&& $("#common_desc").val()==orgCommonData.commonDesc
				&& $("input[name=ttsOption]:checked").val()==orgCommonData.commonTTS
				&& $("input[name=realtimeOption]:checked").val()==orgCommonData.commonRealtime
				&& $("#common_text").text()==orgCommonData.commonText){
			alert("저장할 내용이 없습니다.");
			return false;
		}else{
			// null 체크
			var name = $('#common_name').val();
			var desc = $('#common_desc').val();
			var text = $('#commonScriptDetailText').text();
			var nullParam = '';
			
			if(isNull(name)) nullParam = "이름";
			if(isNull(desc)) nullParam == ''? nullParam = "설명" : nullParam += ", 설명";
			if(isNull(text)) nullParam == ''? nullParam = "내용" : nullParam += ", 내용";
			
			if(nullParam != '') {
				alertNull(nullParam);
				return false;
			}
			
			// pre 태그 비활성화
			$("#commonScriptDetailText").attr("contenteditable",false);
			
			commonEditType = "update";
			showCommonCalendar();
		}
	});
	
	// 공용 문구 수정 취소
	$('#commonUpdateCancelBtn').on("click", function(){

		
		if(confirm("취소하시면 수정 내용은 사라집니다. 취소하시겠습니까?")){
			// 수정 전 조회 화면 원복
			$("#insertUpdate").css("visibility", "hidden");
			$("#updateDate").css("visibility", "visible");
			showCommonScriptDetail(selectedRowBeforeEdit);
			// row select 가능하게 원복
			scriptCommonGrid.detachEvent(rowSelectDisabled);
			rowSelectDisabled = null;
			$('#scriptCommonGrid').css('cursor', 'default');
			// 타이틀 초기화
			$('#commonHeader').css('background-color', '#1760F0');
			$('#common_tit').text('공용 스크립트 조회');
			// 신규, 수정, 삭제 보이기
			showInitButtons();
			// 입력불가 처리
			setReadonly();
			// 검색 활성화
			setSearchAvailable();
			// 수정중 변경된 css 원복
			setOriginCss();
			// pre 태그 비활성화 하기
			$("#commonScriptDetailText").attr("contenteditable",false); 
			// 가변값 리스트 활성화 되던거 없어지기
			$(".autoList").addClass('hide');
		}else{
			return false;

		}
	});
	
	// 공용 문구 신규
	$("#commonInsertBtn").click(function() {
		// 공용문구 가변값 데이터 가져오기
		checkRealTimeTTS();
		// 가변값 데이터를 기반으로 자동완성 리스트 표출하기
		automaticPhrase(realTimeValueArray);
		// 가변값 리스트 활성화 되던 거 없어지기
		$(".autoList").addClass('hide');
		
		newCommonScriptDetail();
		// 우측 하단 (신규)저장, 취소 버튼만 보이기
		$('#commonUpdateBtn').removeClass('enable');
		$('#commonUpdateBtn').addClass('disable');
		$('#commonUpdateBtn').css('display', 'none');
		$('#commonInsertSaveBtn').removeClass('disable');
		$('#commonInsertSaveBtn').css('display', 'block');
		$("#commonInsertSaveBtn").removeAttr('style', 'display: none !important');
		$('#commonUpdateSaveBtn').addClass('disable');
		$('#commonUpdateSaveBtn').css('display', 'none');
		$('#commonUpdateCancelBtn').addClass('disable');
		$('#commonUpdateCancelBtn').css('display', 'none');
		$('#commonInsertCancelBtn').removeClass('disable');
		$('#commonInsertCancelBtn').css('display', 'block');
		$("#commonInsertCancelBtn").removeAttr('style', 'display: none !important');
		// 조회 버튼 및 검색창 비활성화
		$('#searchBtn').css('pointer-events', 'none');
		$('#commonSearchTTS').attr("disabled", "disabled");
		$('#commonSearchTTS').addClass("readonly");
		$('#commonSearchBy').attr("disabled", "disabled");
		$('#commonSearchBy').addClass("readonly");
		$('#searchCommon').attr("disabled", "disabled");
		// 좌측 상단 '신규' div 표시
		$("#insertUpdate").attr("style", "background-color:#4c52c3; color:white; width:90px; height:20px; line-height:20px; margin-left:30px; font-weight:bold; margin-top:50px; margin-bottom:5px; text-align:center");
		$("#insertUpdate").html("스크립트 입력");
		
		// 최종수정일자 tr 안 보이게 하기
		$("#updateDate").css("visibility", "hidden");
		// 신규, 삭제 안 보이게 하기
		hideInsertDeleteBtn();
		// 신규 작성 진입 후 그리드 셀렉트 불가
		disableSelect();
		// 신규 작성 시 빨간색 표시 x
		noColorForNew();
	
		
	});
	
	// 공용 문구 신규 저장
	$("#commonInsertSaveBtn").on('click', function(e) {
		// 가변값 리스트 활성화 되던거 없어지기
		$(".autoList").addClass('hide');
		
		// null 체크
		var name = $('#common_name').val();
		var desc = $('#common_desc').val();
		var text = $('#commonScriptDetailText').text();
		var nullParam = '';
		
		if(isNull(name)) nullParam = "이름";
		if(isNull(desc)) nullParam == ''? nullParam = "설명" : nullParam += ", 설명";
		if($('input:radio[name=ttsOption]').is(':checked')==false) nullParam == ''? nullParam = "유형" : nullParam += ", 유형";
		if($('input:radio[name=realtimeOption]').is(':checked')==false) nullParam == ''? nullParam = "실시간 여부" : nullParam += ", 실시간 여부";
		if(isNull(text)) nullParam == ''? nullParam = "내용" : nullParam += ", 내용";
		
		if(nullParam != '') {
			alertNull(nullParam);
			return false;
		}
		
		// pre태그 비활성화
		$("#commonScriptDetailText").attr("contenteditable",false); 
		
		commonEditType = "insert";
		showCommonCalendar();
	});
	
	// 공용 문구 신규 취소
	$('#commonInsertCancelBtn').on('click', function(e){
		if(confirm("취소하시면 작성 내용은 사라집니다. 취소하시겠습니까?")){
			// '신규' div 안 보이게 하기
			$("#insertUpdate").css("visibility", "hidden");
			// 테이블에서 select된 row의 정보 보이기
			showCommonScriptDetail(scriptCommonGrid.getSelectedId());
			// 입력불가 처리
			setReadonly();
			// row select 가능하게 원복
			scriptCommonGrid.detachEvent(rowSelectDisabled);
			rowSelectDisabled = null;
			$('#scriptCommonGrid').css('cursor', 'default');
			// 검색 활성화
			setSearchAvailable();
			// 버튼 초기화: 신규, 수정, 삭제 버튼 보이기
			showInitButtons();
			// 최종수정일자 tr 보이게 하기
			$("#updateDate").css("visibility", "visible");
			// pre 태그 비활성화
			$("#commonScriptDetailText").attr("contenteditable",false);
			// 이벤트 버블 방지
			e.stopPropagation();
			//가변값 리스트 활성화 되던거 없어지기
			$(".autoList").addClass('hide');	

		}else{
			return false;
		}
	});

	// 공용 문구 삭제
	$("#commonDeleteBtn").bind('click', function() {
		var selectedId = scriptCommonGrid.getSelectedId();
		if (selectedId == null) {
			alert("삭제할 공용문구를 선택해 주세요");
			return false;
		}else{
			var callbackSuccess = function(data){
				showCommonCalendar();
				commonEditType = "delete";
			}
			var callbackError = function(data){
				var callbackCheckError = function(data){
					if(data.result!='-3001'){
						alert("수정할 수 없습니다. 관리자에게 문의하세요.");
					}
				};
			}
			checkCommon( selectedId , callbackSuccess , callbackError);
		}
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
function scCreateGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {

	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");

	objGrid.i18n.paging = i18nPaging['ko'];
	objGrid.enablePaging(true, initPageRow, 5, objPaging, true);
	objGrid.setPagingWTMode(true, true, true, [ 10, 25, 50 ]);
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
		grid_toolbar.setWidth("total", 80);

		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj, count) {
			ui_controller();
		});

		if (objPaging) {
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}
		
		// 파싱시작
		objGrid.attachEvent("onXLS", function(){
			progress.on();
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj, count) {

			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>';
			objGrid.aToolBar.setItemText("total", setResult);
			
			$(window).resize();
			ui_controller();
			progress.off();
			
		});
	});

	objGrid.attachEvent("onDataReady", function() {
		console.log('=========onDataReady=========');
		// scriptCommonGrid 표출 + 스크롤바 표출
		scriptCommonGrid.selectRow(0);
		showCommonScriptDetail(scriptCommonGrid.getSelectedId());
	});

	return objGrid;
}

// Grid에 기능 붙이는 함수
function scGridFunction() {
	
	// 선택(싱글클릭)한 공용 문구 상세 조회
	scriptCommonGrid.attachEvent("onRowSelect", function(id, colIndex) {
		var selectRowNumPk = scriptCommonGrid.getSelectedId();
		showCommonScriptDetail(selectRowNumPk);
		// 수정일자 보이기
		$('#common_updateDate').show();
		// 버튼 초기화
		showInitButtons();
	});

}

// 공용문구 수정버튼 클릭 시 input 태그 변경
function commonUpdateInputBoxChange() {
	$('#common_name').removeAttr("readonly");
	$('#common_name').removeClass("readonly");
	$('#common_desc').removeAttr("readonly");
	$('#common_desc').removeClass("readonly");
	$('#common_text').removeAttr("readonly");
	$('#common_text').removeClass("readonly");
	$('#common_detail_realTime').removeAttr('disabled');
	$('#common_detail_realTime').removeClass("readonly");
	$('#common_text').css("color", "black");
	$('.commonRadio').attr("disabled", false);

	$('#common_name').focus();
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
				if(jRes.resData.msg=="login fail"){
					alert("세션 만료되어 로그인이 필요합니다.");
					return false;
				}else if(jRes.resData.searchCommonScriptDetail.length>0){
					var searchCommonScriptDetail = jRes.resData.searchCommonScriptDetail[0];
					$('#common_pk').val(selectRowNumPk);
					
					// 이름, 설명, 내용
					var name = searchCommonScriptDetail.rsScriptCommonName!=null? searchCommonScriptDetail.rsScriptCommonName : '';
					$("#common_name").val(name);
					var desc = searchCommonScriptDetail.rsScriptCommonDesc!=null? searchCommonScriptDetail.rsScriptCommonDesc : '';
					$('#common_desc').val(desc);
					var text = searchCommonScriptDetail.rsScriptCommonText!=null? searchCommonScriptDetail.rsScriptCommonText : '';
					console.log("commonScriptDetailText : "+ text);
					// 가변값이 있을 경우 빨간색으로 색상을 변경해 줌
					if(text.indexOf("{")>-1){
						$('#commonScriptDetailText').text("");
						var html = "";
						html = text.replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi, "}</span>");								
						$('#commonScriptDetailText').append(html);
					}else{
						$('#commonScriptDetailText').text(text);
					}
					
					$('.common_main_input').attr('readonly');
					$('.common_main_input').addClass('readonly');
					$('#common_text').addClass('readonly');
					$('#common_text').addClass('readonly');
					$('#commonScriptDetailText').addClass('readonly');
					
					// TTS구분
					$('input[name=ttsOption]').attr('disabled', true);
					var type = searchCommonScriptDetail.rsScriptCommonType!=null? searchCommonScriptDetail.rsScriptCommonType : '';
					$(':radio[name=ttsOption][value='+type+']').prop('checked', true);
					
					// RealTime 구분
					$('input[name=realtimeOption]').attr('disabled', true);
					var realTime = searchCommonScriptDetail.rsScriptCommonRealTimeTts!=null? searchCommonScriptDetail.rsScriptCommonRealTimeTts : 'Y';
					$(':radio[name=realtimeOption][value='+realTime+']').prop('checked', true);
					

				}
			}
	
		});
	}

// 공통문구 신규 (상세 보기 화면에 빈칸 표시)
function newCommonScriptDetail() {
	// pre 태그 활성화 하기
	$("#commonScriptDetailText").attr("contenteditable",true)
	
	// input 수정 가능
	$('.common_main_input').val('');
	$('.common_main_input').removeAttr('readonly');
	$('.common_main_input').removeClass('readonly');
	$('#common_text').removeAttr('readonly');
	$('#common_text').removeClass('readonly');
	$('#commonScriptDetailText').text('');
	$('#commonScriptDetailText').removeClass('readonly');

	
	// radio 수정 가능
	$('.commonRadio').removeAttr('disabled');
	$('input[name=ttsOption]').prop('checked', false);
	$('input[name=realtimeOption]').prop('checked', false);
	
	// 작성자, 생성일자, 수정일자 안 보이게 처리
	$('.common_not_updatable').hide();
	
	// 확인 버튼만 보이기
	$('#commonInsertSaveBtn').removeClass('disable');
	$('#commonInsertSaveBtn').css('display', 'block');
	$('#commonUpdateBtn').addClass('disable');
	$('#commonUpdateBtn').css('display', 'none');
	$('#commonUpdateSaveBtn').addClass('disable');
	$('#commonUpdateSaveBtn').css('display', 'none');
	$('#commonScriptCancelBtn').addClass('disable');
	$('#commonScriptCancelBtn').css('display', 'none');
	
	// 이름 포커스
	$('#common_name').focus();
	
}

// 공용 문구 로드
function initScriptCommonGrid(){
	
	popupSizing();
	
	// 최초 로드 시, 목록 첫 번째 줄이 자동 선택된 채로 조회
	scriptCommonGrid.selectRow(0);
	showCommonScriptDetail(scriptCommonGrid.getSelectedId());
	
	// 작성자, 생성일자, 수정일자 보이기
	$('.common_not_updatable').show();
	
	// 수정 버튼만 보이기
	showBtnModifyOnly();
	
}

// grid row select 불가 : 수정 또는 신규 작성중
function disableSelect(){
	$('#scriptCommonGrid').css('cursor', 'not-allowed');
	scriptCommonGrid.setStyle("background-color:#d3d3d3");
	rowSelectDisabled = scriptCommonGrid.attachEvent("onBeforeSelect", function(new_row,old_row){
		alert("수정사항을 저장 또는 취소 후 조회해주세요.");
		return false;
	});
}

// 신규, 삭제 버튼 숨기기 : 수정 또는 신규 작성중
function hideInsertDeleteBtn(){
	$("#commonInsertBtn").addClass('disable');
	$("#commonInsertBtn").attr('style', 'display: none !important');
	$("#commonDeleteBtn").addClass('disable');
	$("#commonDeleteBtn").attr('style', 'display: none !important');
}

// 신규, 수정, 삭제 버튼 보이기 : 수정,신규 저장 또는 수정 취소 시
function showInitButtons(){
	$("#commonInsertBtn").removeClass('disable');
	$("#commonInsertBtn").addClass('enable');
	$("#commonInsertBtn").removeAttr('style', 'display: none !important');
	$("#commonDeleteBtn").removeClass('disable');
	$("#commonDeleteBtn").addClass('enable');
	$("#commonDeleteBtn").removeAttr('style', 'display: none !important');
	$('#commonUpdateBtn').removeClass('disable');
	$('#commonUpdateBtn').css('display', 'block');
	$('#commonInsertSaveBtn').addClass('disable');
	$('#commonInsertSaveBtn').css('display', 'none');
	$('#commonUpdateSaveBtn').addClass('disable');
	$('#commonUpdateSaveBtn').css('display', 'none');
	$('#commonUpdateCancelBtn').addClass('disable');
	$('#commonUpdateCancelBtn').css('display', 'none');
	$('#commonInsertCancelBtn').addClass('disable');
	$('#commonInsertCancelBtn').css('display', 'none');
}

// 수정 저장 또는 취소 후 상세정보 readonly 처리
function setReadonly(){
	$('#common_name').attr("readonly", "readonly");
	$('#common_desc').attr("readonly", "readonly");
	$('#common_text').attr("readonly", "readonly");
	$('#commonScriptDetailText').attr("readonly", "readonly");
	$('#common_detail_type').addClass("readonly");
	$('#common_detail_realTime').addClass("readonly");
}

// 수정 저장 또는 취소 후 검색 활성화
function setSearchAvailable(){
	$('#searchBtn').css('pointer-events', '');
	$('#commonSearchTTS').removeAttr("disabled");
	$('#commonSearchTTS').removeClass("readonly");
	$('#commonSearchBy').removeAttr("disabled");
	$('#commonSearchBy').removeClass("readonly");
	$('#searchCommon').removeAttr("disabled");
}

// 공용 신규,수정,삭제 시 적용일 선택
function showCommonCalendar(){
	
	/* 적용일 선택 div 팝업 생성 */
	var closeSvg = recseeResourcePath + "/images/project/icon/wooribank/modal_close.svg";
	var html = '<div id="saveCommonApplyDatePopup" style="position: absolute; top: 50%; left: 50%; background-color: #ffffff; box-shadow: 0px 0px 10px rgba(0,0,0,0.4); border-radius: 5px; text-align: center; padding-bottom: 5px; z-index: 104;">'
				+ '<div id="saveApplyDateHeader">'
					+ '<span id="applyDateTitle">적용할 날짜를 지정해 주세요</span>'
					+ '<a id="commonDatePopupCloseBtn" style="background:url(' + closeSvg + ') center/cover no-repeat"></a>'
				+ '</div>'
				+ '<div id="saveApplyDateBody">'
					+ '<table id="selectDateTable">'
						+ '<tr>'
							+ '<th>적용예정일</th>'
							+ '<td id="applyDateTd">'
								+ '<input autocomplete="off" type="text" id="applyDate" class="icon_input_cal" style="cursor:pointer">'
							+ '</td>'
						+ '</tr>'
					+ '</table>'
					+ '<div style="text-align:center; padding-bottom:20px"><input type="checkbox" id="nowApply"><label for="nowApply" style="font-size:14px; vertical-align: top; padding:5px">즉시 적용</span></div>'
				+ '</div>'
				+ '<div id="dateButtons">'
					+'<button id="commonDateSaveBtn" class="date-btn-primary">저장</button>'
					+'<button id="commonDateNoSaveBtn" class="date-btn-grey">취소</button>'
				+ '</div>'
			+ '</div>';
	$("#dateBox").append(html);
	$("#applyDate").width($("#applyDateTd").width());
	layer_popup("#saveCommonApplyDatePopup");
	
	/* 중복 팝업 방지 */
	document.addEventListener("keydown", function(event){
		if(event.keyCode === 13) event.preventDefault();
	}, true);
	
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
	var saveDateCalendar = new dhtmlXCalendarObject("applyDate");
	saveDateCalendar.setSkin("dhx_terrace");
	saveDateCalendar.loadUserLanguage("ko");
	saveDateCalendar.hideTime();
	saveDateCalendar.setWeekStartDay(7);
	var year = new Date().getFullYear(); // 현재 년도
	
	var month = new Date().getMonth()+1; // 현재 월
	if(month<10) month = "0"+month;
	
	var day = new Date().getDate()+1; // 다음날
	if(day<10) day = "0"+day;
	
	var tomorrow = new Date(year + "-" + month + "-" + day);
	
	saveDateCalendar.setDate(tomorrow);
	saveDateCalendar.setSensitiveRange(tomorrow, null);
	
	// 즉시적용 체크 시 기존 입력값을 보관, 즉시적용 해제 시 기존 입력값을 다시 가져옴
	var beforeDate = "";
	
	$("#nowApply").on("click", function(){
		if($("input:checkbox[id=nowApply]").is(":checked")){
			if($("#applyDate").val()!=""){
				beforeDate = $("#applyDate").val();
			}
			$("#applyDate").val("");
			$("#applyDate").css('cursor', 'default');
			$("#applyDate").attr("disabled", "disabled");
			$("#applyDate").addClass("readonly");
		}else{
			if(beforeDate != ""){
				$("#applyDate").val(beforeDate);
			}
			$("#applyDate").css('cursor', 'pointer');
			$("#applyDate").removeAttr("disabled");
			$("#applyDate").removeClass("readonly");
		}
	});
	
	/* 오늘 날짜 표시 */
	// 달력이 처음 보일 때 이번달 달력인 경우
	saveDateCalendar.attachEvent("onShow", function(){
		if($('.dhtmlxcalendar_cell_month_dis').length>0){
			var todayIndex = $('.dhtmlxcalendar_cell_month_dis').length-1;
			var today = $('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerText;
			$('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>" + today + "</div>";
		}
	});
	// 월 이동하여 이번달 달력을 보는 경우
	saveDateCalendar.attachEvent("onArrowClick", function(){
		if($('.dhtmlxcalendar_cell_month_dis').length>0){
			var todayIndex = $('.dhtmlxcalendar_cell_month_dis').length-1;
			var today = $('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerText;
			$('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>" + today + "</div>";
		}
	});
}

// x버튼 클릭 시 현재 입력된 검색어 지움
function removeKeyword(){
	$("#searchCommon").val("");
	$("#keywordRemoveImg").hide();
	$("#searchCommon").focus();
}

// 변경 시 red border 추가
function colorIfEdited(orgCommonData){
	
	$("#common_name").on("keyup change paste", function() {	
		$(this).css("border", "1.5px solid red");
		returnCssIfNotChanged(orgCommonData);
	});
	
	$("#common_desc").on("keyup change paste", function() {
		$(this).css("border", "1.5px solid red");
		returnCssIfNotChanged(orgCommonData);
	});
	
	$("input:radio[name=ttsOption]").on("click", function(){
		$("input[name=ttsOption]").siblings('label').attr("style", "color:black; font-weight:400");
		$("input[name=ttsOption]:checked").siblings('label').attr("style", "color:red; font-weight:700");
		returnCssIfNotChanged(orgCommonData);
	});
	
	$("input:radio[name=realtimeOption]").on("click", function(){
		$("input[name=realtimeOption]").siblings('label').attr("style", "color:black; font-weight:400");
		$("input[name=realtimeOption]:checked").siblings('label').attr("style", "color:red; font-weight:700");
		returnCssIfNotChanged(orgCommonData);
	});
	
	$("#common_text").on("keyup change paste", function() {
		$(this).css("border", "1.5px solid red");
		returnCssIfNotChanged(orgCommonData);
	});
	
}

// 신규 입력 시 red border x
function noColorForNew(){
	
	$("#common_name").on("keyup change paste", function() {	
		$("#common_name").css("border", "1px solid #CED3D9");
	});
	
	$("#common_desc").on("keyup change paste", function() {
		$("#common_desc").css("border", "1px solid #CED3D9");
	});
	
	$("input:radio[name=ttsOption]").on("click", function(){
		$("input[name=ttsOption]").siblings('label').attr("style", "color:black; font-weight:400");
	});
	
	$("input:radio[name=realtimeOption]").on("click", function(){
		$("input[name=realtimeOption]").siblings('label').attr("style", "color:black; font-weight:400");
	});
	
	$("#common_text").on("keyup change paste", function() {
		$("#common_text").css("border", "1px solid #CED3D9");
	});
	
}

// 수정중 초기값이 됐을 때는 red border 없애고 기존 css 사용
function returnCssIfNotChanged(orgCommonData){
	
	if($("#common_name").val()==orgCommonData.commonName){
		$("#common_name").css("border", "1px solid #CED3D9");
	}
	
	if($("#common_desc").val()==orgCommonData.commonDesc){
		$("#common_desc").css("border", "1px solid #CED3D9");
	}
	
	if($("input[name=ttsOption]:checked").val()==orgCommonData.commonTTS){
		$("input[name=ttsOption]").siblings('label').attr("style", "color:black; font-weight:400");
	}
	
	if($("input[name=realtimeOption]:checked").val()==orgCommonData.commonRealtime){
		$("input[name=realtimeOption]").siblings('label').attr("style", "color:black; font-weight:400");
	}

	if($("#commonScriptDetailText").text()==orgCommonData.commonText){
		$("#common_text").css("border", "1px solid #CED3D9");
	}
}

// 수정 취소 or 저장 시, 변경표시 css 원복
function setOriginCss(){
	$(".common_main_input").css("border", "1px solid #CED3D9");
	$("input:radio").siblings('label').attr("style", "color:black; font-weight:400;");
	$("#common_text").css("border", "1px solid #CED3D9");
}



// 가변값 가져오기 
function checkRealTimeTTS() {
	realTimeValueArray = [];

	$.ajax({
		url:contextPath+"/selectValueComTTS.do",
		type:"POST",
		dataType:"json",
		data : dataStr,
		traditional:true,	
		async: false,
		success:function(jRes) {
			if(jRes.success == "Y") {	
				var result = jRes.resData.selectValueComTTS;
				for(var i=0; i<result.length; i++) {                                                
					realTimeValueArray.push({name : result[i].rsProductValueName, code : result[i].rsProductValueCode, value : result[i].rsProductValueVal});
				}
				console.log("COM realTimeValue 성공");
			} else {
				console.log("COM realTimeValue 실패");
			}
		}
	});
}

// "@" 입력시 자동 완성 함수
function automaticPhrase(realTimeValueArray) {
	
	var datas = [];
	var codeDatas = [];
	var variabilltyData = [];
	for(var i=0; i<realTimeValueArray.length; i++) {
		variabilltyData.push({name : realTimeValueArray[i].name,code: realTimeValueArray[i].code});
		datas.push(realTimeValueArray[i].name);
		codeDatas.push(realTimeValueArray[i].code);
	}
	var TRIGGER = '@';   // 괄호열리고 시작키워드
	var QUERY = '';					// {@ 이후 검색한 코드
	var DIVISION ='{';
	var SEARCH = false;
	var SELECTPOS = 1;
	var EDITOR = $('.script_input');	// 어떤태그에서 에디터를 사용할것인지
	var LIST = $(".autoList");
	var IDX = 0;						// 변경할스크립트가 많으면 칸도많아지지, 그럼 결국 IDX가 필요

	
	
	LIST.css({
		'position':'absolute',
		'float' : 'right',		
		'width' : '200px',
		'height' : '75px',	
		'background-color' : 'white',
		'overflow' : 'auto'
	});
	

	// 커서를 마지막으로 이동
	function setEndOfContenteditable(contentEditableElement) {
		var range, selection;
		if(document.createRange) {
			range = document.createRange();
			range.selectNodeContents(contentEditableElement);
			range.collapse(false);
			selection = window.getSelection();
			selection.removeAllRanges();
			selection.addRange(range);
		} else if(document.selection) {
			range = document.body.createTextRange();
			range.moveToElementText(contentEditableElement);
			range.collapse(false);
			range.select();
		}
	}
	
	function initList() {
		LIST.empty();
		LIST.addClass('hide');
		SELECTPOS = 1;		//list에서 현재 선택될 데이터
		SEARCH = false;
	}
	
	
	// 선택한 스크립트 태그를 만들어 리턴해준다.
	function createWordHtml(index) {

		var selectEl = $('div:eq('+SELECTPOS+')',LIST.get(index));
		//이게 키입력받고 만들어지는 html이네																		// {체크한목록이름}
		var html = '&nbsp<span class = "editor-link" style=" display: inline-block; color:red;" contenteditable = "true" href = "#" >' + (DIVISION+selectEl.attr('code')) + '}</span>&nbsp';

		initList();
		return html;
	}


	// 커서 확인 할 수 있도록 keyup해서 list에  값 넣기
	EDITOR.keyup(function(e) {
		if (e.which == 13) {
			return;
		}
		// 키가 눌릴 때마다 html이 text에 담기고
		var text = window.getSelection().anchorNode.wholeText;
		trigger_pos = text.lastIndexOf(TRIGGER);		// @를 찾는 마지막 인덱스가 trigger_pos에 담김 -1일시 맨마지막임
		// "{" 괄호 찾기
		var tmpTrigger_pos;
		var inputText = window.getSelection().anchorNode.wholeText;
		
		if(trigger_pos > -1) {
			var trigger_end = window.getSelection().anchorOffset;
			QUERY = text.substring(trigger_pos , trigger_end).trim();
			QUERY = QUERY.replace(/}/gi,"");
			console.log("keyup Query : "+QUERY);
			// QUERY = keyword -->>> 
			let words = '';
			let codes = '';
			if(QUERY != '') {
				
				words = datas; 
				codes = codeDatas; 
			}
			
			if( isNull(variabilltyData) ){		
				initList();
				return;
			}
			
			
			SEARCH = true;
			selection = window.getSelection();
			var node = selection.focusNode;
			var offset = selection.anchorOffset;
			LIST.empty();
			var html = "";
			$.each(variabilltyData, function(i, obj) {
				html += "<div class='list-group-item' code='"+obj.code+"' wordsIdx='"+i+"' style='padding-left :5px; height : 20px;'  contenteditable = 'false' href='#'>" + obj.name + "</div>";
			});
			
			var top = document.querySelector('.script_input');
			var divTop = top.getBoundingClientRect().top - $("#list").height();
			var tempWidth = ($("#common_text").width() + $("#list").height()) - $("#common_text").width()
			var divLeft =  $("#common_text").width() - tempWidth ;
			
			LIST.css({
				"top" : divTop ,
				"left" : divLeft

			});
			
			// SELECTPOS = 리스트상에서 선택되어져있는 인덱스 번호
				LIST.eq(IDX).html(html);
				LIST.eq(IDX).removeClass('hide');

			// 마우스 원클릭 이벤트 LIST마다 클릭이벤트 부여 ( SELECTPOS ) 에맞게 버튼 색칠
			for(var i=0; i<words.length; i++) {
				$('div:eq('+i+')',LIST).click(function(i) {
					//this = 태그 html 자체
					$('div:eq('+SELECTPOS+')',LIST.eq(IDX)).css("background-color","white");
					$('div:eq('+SELECTPOS+')',LIST.eq(IDX)).css("color","black");
					SELECTPOS= $(this).attr('wordsidx'); //.attributes[2].value;
					$('div:eq('+SELECTPOS+')',LIST.eq(IDX)).css("background-color","linear-gradient(0deg, #1448a7, #3b58bf)");
					$('div:eq('+SELECTPOS+')',LIST.eq(IDX)).css("color","white");
					setEndOfContenteditable(EDITOR.get(IDX));
				});
				// 문구박스 더블클릭이벤트
				$('div:eq('+i+')',LIST).dblclick(function() {	

					var createHtml = createWordHtml(IDX);								
					var editHtml = EDITOR.get(IDX).innerText;		
					
					$(EDITOR.get(parseInt(IDX))).html($(EDITOR.get(parseInt(IDX))).html().replace(DIVISION+TRIGGER , createHtml));
					setEndOfContenteditable(EDITOR.get(IDX));
				});
			}				
			// enter
			if(e.which ==13) {
				return false;
			}
			$(LIST).removeClass('active');
			var taa = inputText.substring(0,trigger_pos).replace(/{/gi,"")
			taa = taa.substring(taa.indexOf("<span style=",0)).replace(/<span style="color:#1e34db;">/gi,"")

			$('div:eq('+ SELECTPOS + ')', LIST).addClass('active');
				
		} else {
			initList();
		}
	});// KEYUP
		
};

// null 체크
function isNull(param) {
	if(param == null) return true;
	if(param == undefined) return true;
	if(param == "null" || param == "Null" || param == "NULL") return true;
	if(param.length == 0) return true;
	if(typeof(param) == 'string') {
		if(param.trim() == "") return true;
	}
	return false;
}

// null alert
function alertNull(param){
	var postposition = lastHangeulCheck(param)? "이" : "가"; // 받침 여부에 따라 조사 변경
	alert(param + postposition +" 입력되지 않았습니다.");
}

// 마지막 글자 받침 체크
function lastHangeulCheck(param){
	if(typeof param !== 'string') return null;
	
	var lastLetter = param[param.length-1];
	var unicode = lastLetter.charCodeAt(0);
	
	if(unicode < 44032 || unicode > 55203) return null;
	
	return (unicode - 44032) % 28 != 0;
}