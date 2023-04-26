window.onload = function() {
	// 그리드 생성
	saInit();
	
	// 검색
	$('#searchBtn').click(function(){
		/* 결재요청 목록 조회 API */
		approveLoad(true);
	});
	
	// 검색 엔터 처리
	$('#resultSearchKeyword').keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});
	
	// 검색어 입력됐을 때만 x 표시 보이기
	$("#resultSearchKeyword").on("input", function(){
		if($("#resultSearchKeyword").val()!=""){
			$("#keywordRemoveImg").show();
		}else{
			$("#keywordRemoveImg").hide();
		}
	});
	
	//뒤로가기	
	$("#scriptApprovalList").click(function() {
		$("#scriptApproveListWrap").css("display","block");
		$("#scriptApproveBeforeAfter").css("display","none");
		$("#filter").css("display","block");
	});
	
	// 취소 버튼 클릭
	$("#scriptReturningCancel").click(function() {
		if(confirm("결재의뢰를 취소하시겠습니까?")) {
			/* 취소 API */
			var approveType = "cancel";
			var info = {
				detailTransactionId: detailTransactionId,
				approveType: approveType,
				scriptType: scriptType,
				approveDate: '',
				applyType: ''
			};
			var callbackSuccess = function(data){
				alert("취소되었습니다.");
				// 반려 후 목록 리로드
				approveLoad(false);
				$("#scriptApproveListWrap").css("display","block");
				$("#scriptApproveBeforeAfter").css("display","none");
				$("#filter").css("display","block");
			};
			var callbackError = function(data){
				alert("일시적인 오류로 작업이 완료되지 않았습니다. 관리자에게 문의하세요.");
			};
			putApproval(info, callbackSuccess, callbackError); // 상세 조회 시 가져온 transactionId 전역변수 사용
		}else{
			return false;
		}
	});
	

	
}//window.onload 마감

//화면 이동 시 리사이즈
$(window).on('resize', function(){
	
	var restHeight = $('.main_header').innerHeight()+$('.main_lnb').innerHeight()+$('.filter').innerHeight()+$('#ApprovalPaging').innerHeight();
	var gridHeight = window.innerHeight-restHeight;
	$('#scriptApproveReportListGrid').height(gridHeight);
	
	// 목록보기 버튼 우측 margin 조정
	var agent = navigator.userAgent.toLowerCase(); // 브라우저 판단 -- 단, IE11 이상은 userAgent 정보를 가지고 있지 않으므로 appName으로 판단하는 조건도 아래에 추가
	if( window.innerWidth > 1800 ){ // 해상도 width 1800 초과
		if( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') !=-1) || ( agent.indexOf("msie") != -1 ) ){
			$("#scriptApprovalList").attr("style", "margin-right:22px");
			$("#scriptApproveAfterTitle").attr("style", "margin-left:23px");
		}else{
			$("#scriptApprovalList").attr("style", "margin-right:36px");
			$("#scriptApproveAfterTitle").attr("style", "margin-left:24px");
		}
	}else{ // 해상도 width 1800 이하
		if( ( navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') !=-1 ) || ( agent.indexOf("msie") != -1) ){
			$("#scriptApprovalList").attr("style", "margin-right:11px");
			$("#scriptApproveAfterTitle").attr("style", "margin-left:30px");
		}else{
			$("#scriptApprovalList").attr("style", "margin-right:24px");
			$("#scriptApproveAfterTitle").attr("style", "margin-left:28px");
		}
	}
	
});

// 리로드
function approveLoad(isHeader) {
	var resourceType = "xml"; // 추후 xml 또는 json
	var scriptType = $('#scriptType option:selected').val(); // 전체, 상품, 공용
	var approveStatus = $('#approveType option:selected').val(); // 대기, 승인, 반려
	var startDate = '';
	if($('#sDate').val()!='') startDate = $('#sDate').val();
	var endDate = '';
	if($('#eDate').val()!='') endDate = $('#eDate').val();
	var keyword = $('#resultSearchKeyword').val().trim();
	var data = "scriptType="+scriptType+"&approverYN=N&approveStatus="+approveStatus+"&startDate="+startDate+"&endDate="+endDate+"&keyword="+keyword;
	
	// 리로드 시 헤더도 변경
	if(isHeader) {
		approveGridLoaded = false;
		scriptApproveReportListGrid = sarCreateGrid("scriptApproveReportListGrid","ApprovalPaging","wooribank/script/api/approval/list/"+resourceType+"?header="+isHeader+"&"+encodeURI(data),"", "", 100, [], []);
		scriptApproveReportListGrid.enableAutoWidth(true);
		// 그리드에 기능 등록
		saGridFunction();
	} else {
		scriptApproveReportListGrid.clearAndLoad(contextPath+"/wooribank/script/api/approval/list/"+resourceType+"?" + encodeURI(data));
	}
	
};

// 화면 로드 시 그리드 그리는 함수
function saInit() {

	/* 결재요청 목록 조회 API */
	var resourceType = "xml"; // 추후 xml 또는 json
	var scriptType = "A"; // 초기값=전체 -> 실제 컨트롤러에서는 enum타입에 없으므로 null처리됨
	scriptApproveReportListGrid = sarCreateGrid("scriptApproveReportListGrid","ApprovalPaging","wooribank/script/api/approval/list/"+resourceType+"?header=true&approverYN=N&approveStatus=request","", "", 100, [], []);
	scriptApproveReportListGrid.enableAutoWidth(true);
		
	// 그리드에 기능 등록
	saGridFunction();
}
/**
 * Grid 생성 함수
 * 
 * @param objGrid	그리드가 그려질 태그의 id값
 * @param objPaging	페이징이 그려질 태그의 id값
 * @param url		xml 데이터를 가져올 XmlController의 requestMapping 경로
 * @param strData	parameter 정보
 * @returns
 */
function sarCreateGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {
	
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, initPageRow, 5, objPaging, true);
	objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	objGrid.setPagingSkin("toolbar","dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	// Load Grid
	objGrid.load(contextPath + "/" + url , function() {
		var grid_toolbar = objGrid.aToolBar;
		grid_toolbar.addSpacer("perpagenum");
		grid_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>')
		grid_toolbar.setWidth("total",80)
		
		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj, count){
		});
		
		if(objPaging) {
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}
		
		objGrid.attachEvent("onXLS", function(){
			progress.on();
		});
		
		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){

			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>';
			objGrid.aToolBar.setItemText("total", setResult);
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
			});	
		});
	});
	
	objGrid.attachEvent("onDataReady", function() {
		console.log('=========onDataReady=========');
		if(!approveGridLoaded) {
			approveGridLoaded = true;
			approveLoad(false);
		}
	});
	
	return objGrid;
} 



// Grid에 기능 붙이는 함수
function saGridFunction() {
	
	scriptApproveReportListGrid.attachEvent("onRowDblClicked", function(id, ind) {
		progress.on();
		detailTransactionId = scriptApproveReportListGrid.getSelectedId();
		ifCommon = scriptApproveReportListGrid.cells(detailTransactionId, scriptApproveReportListGrid.getColIndexById('rsCommonKind')).getValue();
		ifApproved = scriptApproveReportListGrid.cells(detailTransactionId, scriptApproveReportListGrid.getColIndexById('rsConfirmYN')).getValue();
		
		if(ifCommon == "N") {
			scriptType = "P";
			productName = scriptApproveReportListGrid.cells(detailTransactionId, scriptApproveReportListGrid.getColIndexById('rsScriptTypeDesc')).getValue();
		}else{
			scriptType = "C";
			$("#productName").hide();
		}
		if(ifApproved=="결재의뢰"){
			$('#scriptReturningCancel').show();
		}else{
			$('#scriptReturningCancel').hide();
		}
		
		/* 결재요청 상세 조회 API */
		
		var info = {
			detailTransactionId: detailTransactionId,
			scriptType: scriptType
		};
		
		var callbackSuccess = function(data){
			progress.off();
			
			var before = data.resData.compareData.before;
			var after = data.resData.compareData.after;
			if(before == null && after == null){
				alert("조회할 정보가 없습니다.");
				return;
			}
				
			$("#scriptApproveBeforeAfter").css("display","block");
			$("#scriptApproveListWrap").css("display","none");
			$("#filter").css("display","none");
			$("#scriptApproveBeforeGrid").html("");
			$("#scriptApproveAfterGrid").html("");
			
			// before 에 stepList 가 있는지
			var hasStepListInBefore = before != null && before.stepList != undefined ;
			// after 에 stepList 가 있는지
			var hasStepListInAfter = after != null && after.stepList != undefined ;
			
			// before,after 둘중 하나라도 stepList 를 포함하고 있으면 상품 스크립트
			if( hasStepListInBefore ||  hasStepListInAfter ) {				
				displayProductScriptBlock(before, after);				
			} else {	
				// 공용 상세
				displayCommonScriptBlock(before, after);				
			}
		
		};
		
		var callbackError = function(data){
			console.log("api 연동 실패");
		};
		getApprovalDetail(info, callbackSuccess, callbackError);
		
	});

}

//상품 상세 표시
function displayProductScriptBlock(before, after){
	
	// 상품명 표시
	$("#productName").html(productName);
	$("#productName").show();
	
	// 적용 전
	var beforeDetailMap = [];
	var list = before == null || before.stepList == undefined || before.stepList == null 
				? [
					{
						rsScriptStepName: "",
						detailList: [
							{
								rsScriptStepDetailTypeName: null,
								rsScriptDetailText: null
							}
						]
					}
				] 
				: before.stepList;
	
	for(var i=0; i< list.length; i++){
		var html = '';
		var stepParent = list[i].rsScriptStepParent;
		if( i!= list.length-1 ){
			if( stepParent == "0" && list[i+1].rsScriptStepParent != "0" ){ // 하위스텝이 있는 상위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" style="margin-bottom : 3px !important;">'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealBeforeText" class="step_real_before_text">' + list[i].rsScriptStepName + '</div>'
						+ '</div>';
			}else if( stepParent !="0" && list[i+1].rsScriptStepParent != "0" ){ // 다음 스텝이 하위스텝인 하위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" style="margin-bottom : 3px !important;">'
						+'<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<div id="StepRealBeforeText" class="step_real_before_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName + '</div>'
						+ '</div>';
			}else if(stepParent !="0" && list[i+1].rsScriptStepParent == "0" ){ // 다음 스텝이 상위스텝인 하위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
					+'<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext" >'
					+ '<div id="StepRealBeforeText" class="step_real_before_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName + '</div>'
					+ '</div>'
			}else{ // 하위스텝이 없는 상위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" >'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealBeforeText" class="step_real_before_text">' + list[i].rsScriptStepName + '</div>'
						+ '</div>';
			}
		}else{
			html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
					+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
						+ '<div id="StepRealBeforeText" class="step_real_before_text">' + list[i].rsScriptStepName + '</div>'
					+ '</div>';
		}
		
		var detailList = list[i].detailList == null ? [] : list[i].detailList;
		
		for(var j=0; j< detailList.length; j++){
			
			beforeDetailMap[detailList[j].rsScriptStepDetailPk] = detailList[j].rsScriptDetailText;
			
			var type = detailList[j].rsScriptStepDetailTypeName;			
			var detailConditionName = detailList[j].rsScriptDetailCaseValue ;
			var detailConditionValue = detailList[j].rsScriptDetailCaseDetailValue;
			var eltDetailContion = detailList[j].rsProductAttributesText;
			
			if(type == null) {
				type="";
				detailConditionName="";
				detailConditionValue="";
				eltDetailContion="";
			}else {
				//기본 조건
				if(detailConditionName == null){
					detailConditionValue="";
					detailConditionName="";
				}else {
					detailConditionName = detailList[j].rsScriptDetailCaseValue +'&nbsp'+':'+'&nbsp';
				}
				//elt조건
				if(eltDetailContion == null){
					eltDetailContion ="";
				}else{
					eltDetailContion = detailList[j].rsProductAttributesText;
				}	
			}
			
			
			var text = detailList[j].rsScriptDetailText;
			
			var isEmptyScript = text == null;
			
			if(isEmptyScript) text="등록된 스크립트가 없습니다.";			
			
			if( text == '' && stepParent == "0" ){// 상위스텝 text가 빈값인 경우 detailText 블럭 생성x
				
			}else if( text != '' && stepParent == "0" ){
				html += '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext detailWrap">'
						+ '<div id="commonType" class="beforeDetailType detailType">' + type + '</div>';
			}else if( text != '' && stepParent !="0" ) {
				html += '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext detailWrap" style="margin-left:20px !important; width:94% !important">'
						+ '<div id="commonType" class="beforeDetailType detailType">' + type + '</div>';
			}
			if(text=="등록된 스크립트가 없습니다."){
				html += '<div id="textArea" class="textArea beforeDetailText detailText">'+ text + '</div>';
			}else if(text!=''){
				html += '<div id="textArea" class="textArea beforeDetailText detailText"><pre>'+ text + '</pre></div>';
				if(detailConditionName != "" && eltDetailContion == ""){
					html +=	beforeDetailCondition(detailConditionName,detailConditionValue);
				}else if(eltDetailContion !="" && detailConditionName=="" ){
					html += '<div id="detailCondition" class="beforedetailCondition">';
					html += beforeDetailEltCondition(eltDetailContion);
					html += '</div>'
				}
				html += '</div>';
			}
		}
	

			$("#scriptApproveBeforeGrid").append(html);	
		$("#scriptApproveBeforeGridWrap").css("visibility","");
	}
	
	list = after == null || after.stepList == undefined || after.stepList == null ? [] : after.stepList;
	// 적용 후
	for(var i=0; i< list.length; i++){
		var stepEditType = list[i].rsScriptStepEditType;
		var stepParent = list[i].rsScriptStepParent;
		var html ;
		if(stepEditType=="D"){
			stepEditType="삭제";
			if( i!= list.length-1 ){
				if( stepParent == "0" && list[i+1].rsScriptStepParent != "0" ){ // 하위스텝이 있는 상위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:1.5px dashed red !important; margin-bottom : 3px !important; ">'
					+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
					+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
					+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName  +'</div>' 
					+ '</div>';
				}else if(stepParent !="0" && list[i+1].rsScriptStepParent != "0" ){ // 다음 스텝이 하위스텝인 하위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:1.5px dashed red !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}else if(stepParent !="0" && list[i+1].rsScriptStepParent == "0" ){ // 다음 스텝이 상위스텝인 하위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}else{ // 하위스텝이 없는 상위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}
			}else{
				if( stepParent !="0" ){
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName + '</div>'
							+ '</div>';
				}else {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName + '</div>'
							+ '</div>';
				}
			}
		}else if(stepEditType=="C"){
			stepEditType="신규";
			if( i!= list.length-1 ){
				if(stepParent == "0" && list[i+1].rsScriptStepParent != "0" ){ // 하위스텝이 있는 상위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:2px solid #9fb4e1 !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}else if(stepParent !="0" && list[i+1].rsScriptStepParent != "0" ){ // 다음 스텝이 하위스텝인 하위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:2px solid #9fb4e1 !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}else if(stepParent !="0" && list[i+1].rsScriptStepParent == "0" ){ // 다음 스텝이 상위스텝인 하위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}else{ // 하위스텝이 없는 상위스텝
					 html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';
				}

			}else{
				if( stepParent !="0" ){
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName + '</div>'
							+ '</div>';
				}else {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName + '</div>'
							+ '</div>';
				}
			}
		}else{ // 수정 또는 기존값
			if( i!= list.length-1 ){
				if(stepParent == "0" && list[i+1].rsScriptStepParent != "0" ){ // 하위스텝이 있는 상위스텝
					html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="margin-bottom : 3px !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';				
				}else if(stepParent !="0" && list[i+1].rsScriptStepParent != "0" ){ // 다음 스텝이 하위스텝인 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="margin-bottom : 3px !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';		
				}else if(stepParent !="0" && list[i+1].rsScriptStepParent == "0" ){ // 다음 스텝이 상위스텝인 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';		
				}else{ // 하위스텝이 없는 상위스텝
					html =  '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName  +'</div>' 
							+ '</div>';	
				}
			}else{
				if( stepParent !="0" ){
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">' + " - " + list[i].rsScriptStepName + '</div>'
							+ '</div>';
				}else {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">' + list[i].rsScriptStepName + '</div>'
							+ '</div>';
				}
			}

		}
		
		var detailList = list[i].detailList == null ? [] : list[i].detailList;
		var stepParent = list[i].rsScriptStepParent;
		
		for(var j=0; j< detailList.length; j++){
			 
			var detailEditType = detailList[j].rsScriptStepDetailEditType;
			if(detailEditType=="D"){
				detailEditType="삭제";
				detailEditTypeShape='<div id="script_approve_type" class ="scriptApproveType" style=" width:30px; height:20px; line-height:20px; background-color:red; color : white; margin-left:5px; text-align:center; font-weight:bolder;  ">'+detailEditType+'</div>';
			}else if(detailEditType=="C"){
				detailEditType="신규";
				detailEditTypeShape='<div id="script_approve_type" class ="scriptApproveType" style="width:30px; height:20px; line-height:20px; background-color:#4c52c3; color : white; margin-left:5px; text-align:center; font-weight:bolder;">'+detailEditType+'</div>';
			}else if(detailEditType=="U"){ // 수정 or 정렬변경
				var beforeText = beforeDetailMap[detailList[j].rsScriptStepDetailPk];  //before.stepList[i].detailList[j].rsScriptDetailText;
				if(beforeText == undefined ) continue;
				
				var afterText = detailList[j].rsScriptDetailText;
				
				// 값 비교용 정규식
				var regexBlank = /\s|\r|\n/g; // 띄어쓰기,개행
				var regexWord = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]|\w/g; // 한글 또는 영문,숫자,언더바(\w)
				var regexSpecial = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g; // 특수문자
				// case 1. 변경 있음
				if(beforeText != afterText){
					// case 1-1. 텍스트 변경 있음 : 한글,영문,숫자,특수문자
					if(JSON.stringify(beforeText.replace(regexBlank, '').match(regexWord)) !== JSON.stringify(afterText.replace(regexBlank, '').match(regexWord))
							|| JSON.stringify(beforeText.replace(regexBlank, '').match(regexSpecial)) !== JSON.stringify(afterText.replace(regexBlank, '').match(regexSpecial))){
						detailEditType="수정";
						detailEditTypeShape='<div id="script_approve_type" class ="scriptApproveType" style="width:30px;height:20px; line-height:20px;  background-color:#44bb97; color : white; margin-left:5px; text-align:center;font-weight:bolder; padding:0px 2px;">'+detailEditType+'</div>';
					}else{ // case 1-2. 텍스트 변경 없음 = 정렬 변경
						detailEditType="정렬 변경";
						detailEditTypeShape='<div id="script_approve_type" class ="scriptApproveType" style="width:60px;height:20px; line-height:20px;  background-color:#44bb97; color : white; margin-left:5px; text-align:center;font-weight:bolder; padding:0px 2px;">'+detailEditType+'</div>';
					}
				}// case 2. 변경 없음 - 표시x
			}else{
				detailEditType="";
				detailEditTypeShape="";
			}
			var type = detailList[j].rsScriptStepDetailTypeName;
			if(type != null){
				var detailConditionName = detailList[j].rsScriptDetailCaseValue;
				var detailConditionValue = detailList[j].rsScriptDetailCaseDetailValue;			
				var eltDetailContion = detailList[j].rsProductAttributesText;				
			}

			
			if(type == null) {
				type="";
				detailConditionName="";
				detailConditionValue="";
				eltDetailContion="";
			}else {
				//기본 조건
				if(detailConditionName == null){
					detailConditionValue="";
					detailConditionName="";
				}else {
					detailConditionName = detailList[j].rsScriptDetailCaseValue +'&nbsp'+':'+'&nbsp';
				}
				//elt조건
				if(eltDetailContion == null){
					eltDetailContion ="";
				}else{
					eltDetailContion = detailList[j].rsProductAttributesText;
				}	

			}

			var text = detailList[j].rsScriptDetailText;
			if(text==null) {text="등록된 스크립트가 없습니다.";}
			if(text == '' && stepParent == "0"){
				
			}else{ // 상위스텝 text가 빈값인 경우 detailText 블럭 생성x
				if(detailEditType=="삭제"){
					html +=  '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_del" style="border:1.5px dashed red !important">'+detailEditTypeShape 
							+ '<div id="commonType" class="afterDetailType detailType">' + type +'</div>'
							+ '<div id="textArea" class="textArea afterDetailText detailText">' + text + '</div>';
					if(detailConditionName != "" && eltDetailContion == ""){
						html +=	afterDetailCondition(detailConditionName,detailConditionValue);
					}else if(eltDetailContion !="" && detailConditionName=="" ){
						html += '<div id="afterCondition" class="afterDetailCondition ">';
						html += afterDetailEltCondition(eltDetailContion);
						html += '</div>'
					}
				}else if(detailEditType=="신규"){
					html +=  '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_new" style="border:1.5px solid #4c52c3 !important">'+detailEditTypeShape 
								+ '<div id="commonType" class="afterDetailType detailType">' + type +'</div>'
								+ '<div id="textArea" class="textArea afterDetailText detailText">' + text + '</div>'
					if(detailConditionName != "" && eltDetailContion == ""){
						html +=	afterDetailCondition(detailConditionName,detailConditionValue);
					}else if(eltDetailContion !="" && detailConditionName=="" ){
						html += '<div id="afterCondition" class="afterDetailCondition ">';
						html += afterDetailEltCondition(eltDetailContion);
						html += '</div>'
					}
				}else if(detailEditType=="수정" || detailEditType=="정렬 변경"){
					html +=  '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_modi" style="border:1.5px solid #44bb97 !important">'+detailEditTypeShape 
								+ '<div id="commonType" class="afterDetailType detailType">' + type +'</div>'
								+ '<div id="textArea" class="textArea afterDetailText detailText"><pre>' + text + '</pre></div>'
					if(detailConditionName != "" && eltDetailContion == ""){
						html +=	afterDetailCondition(detailConditionName,detailConditionValue);
					}else if(eltDetailContion !="" && detailConditionName=="" ){
						html += '<div id="afterCondition" class="afterDetailCondition ">';
						html += afterDetailEltCondition(eltDetailContion);
						html += '</div>'
					}
				}else{
					html += '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_default">'+detailEditTypeShape 
							+ '<div id="commonType" class="afterDetailType detailType">' + type +'</div>';
					if(text=="등록된 스크립트가 없습니다."){
						html += '<div id="textArea" class="textArea beforeDetailText detailText">'+ text + '</div>';
					}else{
						html += '<div id="textArea" class="textArea beforeDetailText detailText"><pre>'+ text + '</pre></div>';
						if(detailConditionName != "" && eltDetailContion == ""){
							html +=	afterDetailCondition(detailConditionName,detailConditionValue);
						}else if(eltDetailContion !="" && detailConditionName=="" ){
							html += '<div id="afterCondition" class="afterDetailCondition ">';
							html += afterDetailEltCondition(eltDetailContion);
							html += '</div>'
						}
					} 
				}
				html += '</div>';
			}

		}
			
		$("#scriptApproveAfterGrid").append(html);
		$("#scriptApproveAfterGridWrap").css("visibility","");
		$(".child_step").find(".detail_del").attr("style", "margin-left:20px !important; width:94% !important; border:1.5px dashed red !important;");
		$(".child_step").find(".detail_new").attr("style", "margin-left:20px !important; width:94% !important; border:1.5px solid #4c52c3 !important;");
		$(".child_step").find(".detail_modi").attr("style", "margin-left:20px !important; width:94% !important; border:1.5px solid #44bb97 !important;");
		$(".child_step").find(".detail_default").attr("style", "margin-left:20px !important; width:94% !important; border:1px solid #dbdbdb !important;");
	}
	
	// null값 또는 빈값에 대한 스타일 적용
	for(var i=0; i<$('.detailWrap').length; i++){
		
		if( $(".detailType").eq(i).html()=="" ) {
			$(".detailType").eq(i).attr("style", "width: 0; height: 0;")
		}
		
		if( $(".detailText").eq(i).html()=="등록된 스크립트가 없습니다." ) {
			
			if( $(".detailText").eq(i).is(".beforeDetailText") ) {
				$(".detailText").eq(i).attr("style", "color: #d3d3d3; background-color: #f9f9f9 !important");
				$(".detailWrap").eq(i).attr("style", "background-color: #f9f9f9");
			}else{
				$(".detailText").eq(i).attr("style", "color: #d3d3d3; background-color: #e4eff1 !important");
				$(".detailWrap").eq(i).attr("style", "background-color: #e4eff1");
			}
		}
		
	}

}

// 공용 상세 표시
function displayCommonScriptBlock(before, after){
	// DB에서 SSDT -> recValue 사용 (T: TTS리딩 / S: 직원리딩 / G: 상담가이드 / R: 적합성보고서)
	for(var i=0; i<recValue.length; i++){
		if(before != null){
			if(before.rsScriptCommonType == recValue[i].code){
				before.rsScriptCommonType = recValue[i].name;
			}
		}
		if(after.rsScriptCommonType == recValue[i].code){
			after.rsScriptCommonType = recValue[i].name;
		}
	}
	// 신규 케이스 구분
	if(before != null){
		$("#scriptApproveBeforeGridWrap").css("visibility","");
		var beforeHtml = '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
							+'<div id="script_approve_common_type" class ="scriptApproveCommonType" style="height :20px;line-height:20px; margin-left:12px; "></div>'
							+ '<div class="common_info">'
								+'<div class="script_approve_commons">'
									+'<div class="scriptApproveMenuName">'+"공용문구 이름 "+'</div>'
									+'<div class="scriptApproveValueName">'+before.rsScriptCommonName +'</div>'
								+'</div>'
								+'<div class="script_approve_commons">'
									+'<div class="scriptApproveMenuName">'+"공용문구 설명"+'</div>'
									+'<div class="scriptApproveValueName">'+before.rsScriptCommonDesc+'</div>'
								+'</div>'
								+'<div class="script_approve_commons">'
									+'<div class="scriptApproveMenuName">'+"실시간 사용여부"+'</div>'
									+'<div class="scriptApproveValueName">'+before.rsScriptCommonRealtimeTTS+'</div>'
								+'</div>'
							+ '</div>'	
							+ '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext">'
								+ '<div id="commonType" class="beforeDetailType">' + before.rsScriptCommonType + '</div>'
								+ '<div id="textArea" class="textArea beforeDetailText"><pre>' + before.rsScriptCommonText + '</pre></div>'
							+ '</div>'
						+ '</div>';
	
		$("#scriptApproveBeforeGrid").append(beforeHtml);
	}
	else if(after.rsScriptCommonEditType=="C"){
		$("#scriptApproveAfterGrid").html("");
		var emptyHtml = '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
			+'<div id="script_approve_common_type" class ="scriptApproveCommonType" style="height :20px; line-height:20px;margin-left:12px; "></div>'
			+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext common_info" >'
				+ '<div id="StepRealBeforeText" class="step_real_before_text"  style="color:#a19a9a">'
					+ " 등록된 스크립트 내역이 없습니다." + '<br>'
				+ '</div>'
			+ '</div>'
			+ '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext" style="opacity:0.5;">'
				+ '<div id="commonType" class="beforeDetailType"></div>'
				+ '<div id="textArea" class="textArea beforeDetailText" style="visibility:hidden;">'+ after.rsScriptCommonText +'</div>'
			+ '</div>'
		+ '</div>';
		$("#scriptApproveBeforeGrid").append(emptyHtml);
		$("#StepRealBeforeText").removeClass("common_info");
	}else {
		var emptyHtml = '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
					+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext common_info" style="height : 75px;">'
					+ '<div id="StepRealBeforeText" class="step_real_before_text"  style="color:#a19a9a">'
						+ " 등록된 스크립트 내역이 없습니다." + '<br>'
					+ '</div>'
				+ '</div>'
				+ '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext" style="opacity:0.5;">'
					+ '<div id="commonType" class="beforeDetailType"></div>'
					+ '<div id="textArea" class="textArea beforeDetailText" style="visibility:hidden;"></div>'
				+ '</div>'
			+ '</div>';
				
		$("#scriptApproveBeforeGrid").append(emptyHtml);
		$("#scriptApproveBeforeGridWrap").css("visibility","hidden");
	}
	
	// 이름, 설명, 시간
	var commonEditType = after.rsScriptCommonEditType;

	if(commonEditType=="D"){
		commonEditType="삭제";
		commonEditTypeShape='<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:red; color:white; width:30px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'+commonEditType+'</div>';
	}else if(commonEditType=="C"){
		commonEditType="신규";
		commonEditTypeShape='<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#4c52c3; color:white; width:30px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'+commonEditType+'</div>';
	}else if(commonEditType=="U"){ // 수정 or 정렬변경
		// 값 비교용 정규식
		var regexBlank = /\s|\r|\n/g; // 띄어쓰기,개행
		var regexWord = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]|\w/g; // 한글 또는 영문,숫자,언더바(\w)
		var regexSpecial = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g; // 특수문자
		// case 1. 변경 있음
		if(before.rsScriptCommonName != after.rsScriptCommonName || before.rsScriptCommonDesc != after.rsScriptCommonDesc || before.rsScriptCommonText != after.rsScriptCommonText){
			// case 1-1. 텍스트 변경 있음 : 한글,영문,숫자,특수문자
			//console.log(JSON.stringify(before.rsScriptCommonText.replace(regexBlank,'').match(regexWord)));
			//console.log(JSON.stringify(before.rsScriptCommonText.replace(regexBlank,'').match(regexSpecial)));
			//console.log(JSON.stringify(after.rsScriptCommonText.replace(regexBlank,'').match(regexWord)));
			//console.log(JSON.stringify(after.rsScriptCommonText.replace(regexBlank,'').match(regexSpecial)));
			if(JSON.stringify(before.rsScriptCommonName.replace(regexBlank, '').match(regexWord)) !== JSON.stringify(after.rsScriptCommonName.replace(regexBlank, '').match(regexWord))
					|| JSON.stringify(before.rsScriptCommonDesc.replace(regexBlank, '').match(regexWord)) !== JSON.stringify(after.rsScriptCommonDesc.replace(regexBlank, '').match(regexWord))
					|| JSON.stringify(before.rsScriptCommonText.replace(regexBlank, '').match(regexWord)) !== JSON.stringify(after.rsScriptCommonText.replace(regexBlank, '').match(regexWord))
					|| JSON.stringify(before.rsScriptCommonName.replace(regexBlank, '').match(regexSpecial)) !== JSON.stringify(after.rsScriptCommonName.replace(regexBlank, '').match(regexSpecial))
					|| JSON.stringify(before.rsScriptCommonDesc.replace(regexBlank, '').match(regexSpecial)) !== JSON.stringify(after.rsScriptCommonDesc.replace(regexBlank, '').match(regexSpecial))
					|| JSON.stringify(before.rsScriptCommonText.replace(regexBlank, '').match(regexSpecial)) !== JSON.stringify(after.rsScriptCommonText.replace(regexBlank, '').match(regexSpecial))){
				commonEditType="수정";
				commonEditTypeShape='<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#44bb97; color:white; width:30px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'+commonEditType+'</div>';
			}else{ // case 1-2. 텍스트 변경 없음 = 정렬 변경
				commonEditType="정렬 변경";
				commonEditTypeShape='<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#44bb97; color:white; width:60px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'+commonEditType+'</div>';
			}
		}// case 2. 변경 없음 - 따로 표시x
	}else{
		commonEditType="";
	}
	var afterHtml = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
						+commonEditTypeShape
						+ '<div class="common_info">'
							+'<div class="script_approve_commons">'
								+'<div class="scriptApproveMenuName">'+"공용문구 이름 "+'</div>'
								+'<div class="scriptApproveValueName">'+after.rsScriptCommonName +'</div>'
							+'</div>'
							+'<div class="script_approve_commons">'
								+'<div class="scriptApproveMenuName">'+"공용문구 설명"+'</div>'
								+'<div class="scriptApproveValueName">'+after.rsScriptCommonDesc+'</div>'
							+'</div>'
							+'<div class="script_approve_commons">'
								+'<div class="scriptApproveMenuName">'+"실시간 사용여부"+'</div>'
								+'<div class="scriptApproveValueName">'+after.rsScriptCommonRealtimeTTS+'</div>'
							+'</div>'
						+ '</div>'	
						+ '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext">'
							+ '<div id="commonType" class="afterDetailType">' + after.rsScriptCommonType + '</div>'
							+ '<div id="textArea" class="textArea afterDetailText"><pre>' + after.rsScriptCommonText + '</pre></div>'
						+ '</div>'
					+ '</div>';

	$("#scriptApproveAfterGrid").append(afterHtml);
	

	if(after.rsScriptCommonEditType=="D"){
		$("#scriptApproveAfterGrid").html("");
		var afteremptyHtml = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
			+'<div id="script_approve_common_type" class ="scriptApproveCommonType" style="height :20px;line-height:20px; margin-left:12px; "></div>'
			+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext common_info">'
				+ '<div id="StepRealAfterText" class="step_real_after_text"  style="color:#a19a9a; margin-left:10px;">'
					+ "삭제" + '<br>'
				+ '</div>'
			+ '</div>'
			+ '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext" style="opacity:0.5;">'
				+ '<div id="commonType" class="afterDetailType"></div>'
				+ '<div id="textArea" class="textArea afterDetailText" style="visibility:hidden;">' + before.rsScriptCommonText +'</div>'
			+ '</div>'
		+ '</div>';
		$("#scriptApproveAfterGrid").append(afteremptyHtml);
		$("#StepRealBeforeText").removeClass("common_info");

	}else{
		$("#scriptApproveAfterGridWrap").css("visibility","");
	}
}

//검색용 달력 세팅
function setStartCalendar(){
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
	
	var startDateCalendar;
	if(!startDateCalendar){
		startDateCalendar = new dhtmlXCalendarObject("sDate");
		startDateCalendar.setSkin("dhx_terrace");
		startDateCalendar.loadUserLanguage("ko");
		startDateCalendar.hideTime();
		startDateCalendar.setWeekStartDay(7);
	}
	
	startDateCalendar.show();
	
	startDateCalendar.attachEvent("onClick", function(){
		var startDate = new Date($("#sDate").val());
		var endDate = new Date($("#eDate").val());
		if(startDate > endDate) {
			alert("시작일은 종료일 이후일 수 없습니다.");
			$("#sDate").val($("#eDate").val());
		}
	});
}

function setEndCalendar(){
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
	
	var endDateCalendar;
	if(!endDateCalendar){
		endDateCalendar = new dhtmlXCalendarObject("eDate");
		endDateCalendar.setSkin("dhx_terrace");
		endDateCalendar.loadUserLanguage("ko");
		endDateCalendar.hideTime();
		endDateCalendar.setWeekStartDay(7);
	}
	
	endDateCalendar.show();
	
	endDateCalendar.attachEvent("onClick", function(){
		var startDate = new Date($("#sDate").val());
		var endDate = new Date($("#eDate").val());
		if(startDate > endDate) {
			alert("종료일은 시작일 이전일 수 없습니다.");
			$("#eDate").val($("#sDate").val());
		}
	});
}

// x버튼 클릭 시 현재 입력된 검색어 지움
function removeKeyword(){
	$("#resultSearchKeyword").val("");
	$("#keywordRemoveImg").hide();
	$("#resultSearchKeyword").focus();
}


// 재상신 버튼 클릭 시 new transaction ID 생성 - api 연동으로 old transaction ID에 있던 편집데이터 new transaction ID로 복사
function restartApproval(productPk, oldTransactionId){
	var callbackSuccess = function(data) {
		transactionId = data.resData.transactionId;
		openEdit(transactionId, productPk);
	};
	var callbackError = function(data) {

	};
	startTransaction(productPk, oldTransactionId, callbackSuccess, callbackError);
}

// 수정 팝업 열기
function openEdit(transactionId, productPk) {
	// 열려 있는 수정팝업이 있으면 focus
	var url = contextPath + '/wooribank/script/edit/' + transactionId + "/" + productPk;
	var name = 'script_edit';
	if (editPopup != null && editPopup != undefined && !editPopup.closed) {
		var nowUrl = editPopup.location.href;
		if (nowUrl != null && nowUrl != undefined) {
			var productPkIndex = nowUrl.split("/").length - 1;
			var nowProductPk = nowUrl.split("/")[productPkIndex];
			if (productPk == nowProductPk) {
				alert("이 상품스크립트에 대한 수정 팝업창이 열려 있습니다.");
				editPopup.focus();
				return false;
			}else{
				alert("다른 상품스크립트를 수정 중입니다.");
				editPopup.focus();
				return false;
			}
		}
	}

	editPopup = window.open(url, name, "width=" + screen.width + ", height=" + screen.height + ", location=no, resizable=yes, channelmode=yes");

}

// 팝업 열린 상태에서 세션 만료 -1001 코드 응답 시, 팝업창 닫고 팝업오프너(현재 창)에서 로그인화면으로 이동
function fromEditToLogin() {
	if(editPopup!=null){
		editPopup.close();
		location.replace(contextPath + "/?secret=true");
	}
}

// 변경 전 상품기본조건
function beforeDetailCondition(detailConNm,detailConVal){
	var html='';
	
	if( detailConNm !="" && detailConNm != null && detailConVal != null ){
		html += '<div id="detailCondition" class="beforedetailCondition detailType">'
					+ '<div id="detailConditionName" class="beforeDetailTypeCon ">'+ detailConNm + detailConVal +'</div>'
				+'</div>';
	}
	
	return html;
}
// 변경 전 elt상품일경우
function beforeDetailEltCondition(eltCon){
	var html='';
	for(var i =0; i < eltCon.length; i++){
		html +=  '<div id="detailConditionName'+[i]+'" class="beforeDetailTypeCon ">'+ eltCon[i]+'&nbsp' +'</div>'
	}
	
	return html;
}
// 변경 후 상품기본조건
function afterDetailCondition(detailConNm,detailConVal){
	var html='';
	
	if( detailConNm !="" && detailConNm != null && detailConVal != null ){
		html += '<div id="afterCondition" class="afterDetailCondition detailType">'
					+ '<div id="afterDetailConditionName" class="afterDetailTypeCon ">'+ detailConNm + detailConVal +'</div>'
				+'</div>';
	}
	
	return html;
}
// 변경 후 elt상품일경우
function afterDetailEltCondition(eltCon){
	var html='';
	
	for(var i =0; i < eltCon.length; i++){
		html +=  '<div id="afterDetailConditionName'+[i]+'" class="afterDetailTypeCon ">'+ eltCon[i]+'&nbsp' +'</div>'
	}
	return html;
}

