$(document).ready(function() {
	
	// 상품명, 상품유형, 상품코드 
	$("#product_name_value").html(productGroupInfo.productName);
	$("#product_type_value").html(productGroupInfo.productTypeName);
	
	// 그룹상품 목록 표시용 span 만들기
	var questionMark = '<span id="showGroupList" class="questionMark"></span>';

	// 대표상품이면 대표상품코드 및 상품코드 표시, 대표상품코드에 mouseover 시 목록 팝업
	if(productGroupInfo.groupCode != null){
		$("#repre_product_code").html("ㆍ 그룹코드");
		$("#repre_product_code_value").html(productGroupInfo.groupCode);
		$("#product_code_title").html("ㆍ 상품코드");
		if(productGroupInfo.groupProductList!=null && productGroupInfo.groupProductList!=[]){
			$('#product_code_value').html(productGroupInfo.productCode
					+ "<span style='font-size:11px; font-weight:bold; margin-left:5px;'>외<span style='margin-right:5px'></span>"
					+ productGroupInfo.groupProductList.length + "건" + "</span>" + questionMark);
			$("#product_code_value").attr("style", "cursor: pointer");
			var product_code_value = document.getElementById("product_code_value");
			product_code_value.addEventListener("click", showGroupList, true);
		}else{
			$("#product_code_value").html(productGroupInfo.productCode);
		}
	}else{ // 대표상품이 아니면 상품코드만 표시
		$("#repre_product_code").html("ㆍ 상품코드");
		$("#repre_product_code_value").html(productGroupInfo.productCode);
	}
	// 상품 정보란에 표출 될 가변값 연동 시간 안내 문구
	winiInfo();
	
	initScriptStepGrid();
	seFormFunction();
	isEditMode = true;
	initEditMode();
   
   // pre에서 enter 시 div 입력 없이 개행 -> v버튼 누른 후에도 잘 조회됨 -> 결재목록에서는 정렬변경이 아닌 수정으로 표시됨
   document.addEventListener('keydown', function(event){
	   if (event.keyCode === 13) {
		   var insertLineBreak = document.execCommand('insertLineBreak');
		   if(!insertLineBreak){ // insertLineBreak를 실행할 수 없는 경우 - IE
			   var newline = document.createTextNode('\n');
			   var range = getSelection().getRangeAt(0);
			   range.surroundContents(newline);
			   range.selectNode(newline.nextSibling);
		   }
		   event.preventDefault();
	   }
   });	   

	$("#select_script_btn").click(function() {
		var selectedScriptCode = "";
		$("#script_list option:selected").each(function() {
			if($(this).val() != "" && $("#faceRecMenu ul").find("#"+$(this).val()).length == 0) {
			    $("#faceRecMenu ul").append("<li class='addedScript' onclick='showScript(\""+$(this).val()+"\")' id='"+$(this).val()+"'>");
			    $("#faceRecMenu ul").append("<input type='checkbox' class='chkImg' disabled='disabled'/>"+$(this).text());
			    $("#faceRecMenu ul").append("</li>");
			    
			    selectedScriptCode += ","+$(this).val();
			}
		});
		
		if (selectedScriptCode != "") {
			$.ajax({
				url:contextPath+"/getScriptList.do",
				data:{"scriptCode":selectedScriptCode.substring(1)},
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						var result = jRes.resData.script
						
						for (var i = 0; i < result.length; i++) {
						    var jsonData = result[i];
							scriptList[jsonData.scriptCode] = jsonData.script;
						}
					}
				}
			});
		}
	})

	$(".chkIcon").change(function() {
        if($(".chkIcon").is(":checked")) {
            $("#"+scriptId+" .chkImg").prop('checked',true);
        } else {
            $("#"+scriptId+" .chkImg").prop('checked',false);
        }
    });
	
	ui_controller();
	
	// 미리듣기
	$(document).on("click", "#audioPlay", function() {
		// 현재 스크립트단계 
		var previosLevl = scriptGrid.getSelectedRowId();
		var index = $(this).parent().parent().attr("scriptindex");
		var container = $(this).parent().parent();			
		var text= $("#script_input"+index).text();
		var caseDetail = container.attr("caseDetail");
		var reqData = null;
		var variableText = changeVariableValToName(text);
		var scriptType = text.indexOf('설명') !== -1 ? 'P': 'N';
		
		reqData = {
			'rsScriptDetailType' : container.attr("detailType"),
			'rsScriptDetailText' : variableText,
			'rsScriptDetailType' : scriptType,
			'rsProductPk' : container.attr("stepFK")
		}
		
		var stepPk = container.attr("steppk");
		var url = contextPath+"/wooribank/script/api/step/"+stepPk+"/detail/listen";
		
		var callbackSuccess = function(jRes) {
			if(jRes.success == "Y") {					
					var path = jRes.resData.filePath;
					if(path !=null) {
						if(environment != 'prod') {
							path = path.replace("https","http");
						}
						audioTag = document.getElementById("audioPlayer");
						//테스트서버
						audioTag.src = path;
						audioTag.play();

					}
			} 
		}	
		
		var callbackError = function (res) {
			console.log(res);
		}
		
		ajaxPost(url, reqData, 'application/json', callbackSuccess, callbackError);
//		reqData = {
//				'detailType' : container.attr("detailType"),
//				'text' : variableText,
//				'type' : scriptType,
//				'pk' : container.attr("stepFK")
//		}
//		
//		
//		if(reqData == null) return;
//		
//		$.ajax({
//			url:contextPath+"/selectDetailTextAuidoPath.do",
//			type:"POST",
//			dataType:"json",
//			data : reqData,
//			async: false,
//			success:function(jRes) {
//				if(jRes.success == "Y") {					
//						var path = jRes.resData.filePath;
//						if(path !=null) {
//							path = path.replace("https","http");
//							audioTag = document.getElementById("audioPlayer");
//							//테스트서버
////							audioTag.src = "http://10.214.121.36:28881/listen?url="+path;
//							
////							path = "http://localhost:28881/listen?url=D:/ttsFile/temp/20220115/20220115211209_temp.wav";
//
//							audioTag.src = path;
//							
//							audioTag.play();
//
//							
//						}
//				} else {
//
//				}
//			}		
//		})
	});
	
	//스크립트추가버튼 

	var s=0;
	$("#add_scriptStep_btn").click(function() {
		var getId = scriptGrid.getSelectedId();
		var newId = getId+'child'+(new Date).valueOf();
		var rScriptStepFk = $("#productInfo").attr("stepFk");
		//click 표출되어있던 +,U,D 안보이게하기
		if($(".scGirdStepOneDe").hasClass("clicked")){
			$(".scGirdStepOneDe").removeClass("clicked");
			$(".scGirdStepOneDe").css("display","none");		
		}
		if($(".scGirdStepTwoDe").hasClass("clicked")){
			$(".scGirdStepTwoDe").removeClass("clicked");
			$(".scGirdStepTwoDe").css("display","none");			
		}
		
		k++;
		s++;
		var buttonCol =
				"<div class='stepDivWrap'>"
				+"<div id = 'sc_gird_step_one_de_new"+k+"' class ='scGirdStepOneDe stepAddDeleteBtn clicked'>"
				+"<div id='sc_gird_add_new"+k+"' class='scGirdAdd ' onClick=addRowRank('"+newId+"',"+k+") style='display : inline-block ;margin-right : 10px; cursor: pointer; width : 20px; text-align :center;  position: relative; top: 5px'>"
					+"<img id src='"+recseeResourcePath +"/images/project/icon/plus.png ' style='width:20px; margin : auto;'/></div>"
				+"<div id='sc_gird_delete_new"+k+"' class='scGirdDelete ' onClick=deleteRowRank('"+newId+"',"+k+") style='cursor: pointer; display : inline-block ;margin-right : 10px;text-align :center; width : 20px; position: relative; top: 5px'>"
					+"<img id src='"+recseeResourcePath +"/images/project/icon/wooribank/ic_trash.svg ' style='width:20px; margin : auto;'/></div>"			
				+"</div>"
				+"<div class='showIfEdited' style='display:none; color:red;'>[수정]</div></div>";		
		// 그리드에 추가되는 이벤트
		scriptGrid.addRow(newId,["신규 스크립트 추가"+s,"",0,rScriptStepFk,"new",buttonCol], 0);
		// 추가된 그리드에 선택값이 주어지는 형태
		scriptGrid.selectRowById(newId);
		
		
		/* 스크립트 스텝 추가 API */
		var selectRowind = k;
		var stepName = scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('scriptName')).getValue(); 
		var transId = editTransactionId;
		
		// 신규 추가용 변수
		var stepFk = editProductPk; 
		var stepParent = scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('rScriptStepParent')).getValue(); // 최상위 스텝이면 0, 하위 스텝이면 상위 스텝의 고유값;	

			var info = {
				transId: transId,
				stepFk: stepFk,
				stepParent: stepParent,
				stepName: stepName
			}
			var callbackSuccess = function(data){
					if($("#sc_gird_add_new"+selectRowind).hasClass("disable")){
						$("#sc_gird_add_new"+selectRowind).removeClass("disable");
						$("#sc_gird_add_new"+selectRowind).css("display","inline-block");		
					}
					if( data.resData.rsScriptStepPk != null){
						var tempStepPk = data.resData.rsScriptStepPk;
	
						scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('rScriptStepPk')).setValue(tempStepPk);
						// 저장 성공 시 이름 변경
						scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('changeScriptName')).setValue('renewal');

						// 신규 저장 후 오른쪽 title에 stepName 입력, detail은 빈칸으로
						$("#script_edit_title_area").val(scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('scriptName')).getValue());
						$("#realScriptDetailContentValue").html("");
						$('#productInfo').attr("stepPk", tempStepPk);
					}
					showOrHideEdit();
			};			
			var callbackError = function(data){
				if(data.result=='-1001'){
					opener.fromEditToLogin();
				}
			};
			addStep(info , callbackSuccess , callbackError);		
	});	
	
	
	// 스크립트 1.수정 - 2.저장 버튼 클릭
	$("#change_script_btn").click(function() {
		var scriptArray = [];
		//1.수정하기 위한 부분
		// 스크립트 갯수 만큼 반복
		for(var i = 0; i< $(".scriptContext").length; i++) {
			// 스크립트 내용을 가져온다								
			var htmlText = $(".scriptContext").eq(i).html().replace(/<pre contenteditable="false">/gi,"").replace(/<\/pre>/gi,"\n").replace(/<br>/gi,"\n");
			
			// 자동입력칸이 있는경우 뒤에 div 태그 삭제 
			if(htmlText.indexOf('<div id="list" class="panel panel-default hide">') > -1) {
				htmlText = htmlText.substring(0,htmlText.indexOf('\n<div id="list" class="panel panel-default hide">'))
			}			
			// 마지막에 엔터 부분 제거 
			if(htmlText.length == htmlText.lastIndexOf("\n")+1) {
				htmlText = htmlText.substring(0, htmlText.lastIndexOf("\n"));
			}			
			// span 태그 삭제 
			if(htmlText.indexOf("<span") > -1) {
				htmlText = htmlText.replace(/<span style="color:red;">/gi,"").replace(/<\/span>/gi, "");
			}	 			
			// 고객 유형, 상품코드 존재여부 확인
			var ifcase ;
			var ifcase_select = $("#typeselect"+i).val();		
			var ifcasedetail_select = $("#typeValue"+i).val();
			var ifcasedetail_text = $("#typeText"+i).val();
			// ifcase 및 ifcaseDetail에 관한 내역
			if(ifcase_select == "-" || isNull(ifcase_select)) {
				ifcase = "N";
				ifcasedetail = "조건없음";
			} else {				 
				ifcase = "Y";
				if(ifcase_select == "" || isNull(ifcasedetail_select)){
					ifcasedetail = ifcasedetail_text
				}else if(ifcasedetail_text == "" || isNull(ifcasedetail_text)){
					ifcasedetail = ifcasedetail_select					
				}
			} 
			if(htmlText.trim().length == 0 || htmlText == null) {
				alert("리딩 내역이 입력되지 않았습니다. 확인 후 다시 저장하세요.");
				return false;
			} else if(htmlText != htmlText.trim().legth) {
				var scriptJson = {
						"detailType"	: $("#genneralCheck").val(),
						"htmlText"		: htmlText,
						"detailComKind"	: $("#genneralCheck").attr("detailComKind"),
						"detailComFk"	: $(".scriptContext").eq(i).attr("detailComFk"),
						"ifcase"		: ifcase.trim(),
						"ifcasedetail"	: ifcasedetail.trim()
				}
//				scriptArray.push(scriptJson);
			}
		}
							
		
		// 상세 스크립트 수정 함수 호출
		var updateSucc = updateScriptDetail(scriptJson);
		if(!updateSucc) {
			alert("저장 중 오류가 발생했습니다.\r\n잠시 후 다시 시도 바랍니다.");
			return;
		}
		
		// 상세 스크립트 저장 함수 호출
		var insertSucc = insertScriptDetail(insertDetailArray);
		if(insertSucc) {

		} else {
			alert("저장 중 오류가 발생했습니다.\r\n잠시 후 다시 시도 바랍니다.");
		}
	});
	

	
	//??
	$('#audioPlayer').bind('ended', function() {
		if(playCount == ttsResult.resultPath.length) {
			audioTag.pause();
		}
		playCount++;
		audioTag.src = ttsResult[playCount].resultPath;
		
		if(playCount < ttsResult.resultPath.length) {
			audioTag.pause();
		}
	});
	
	ButtonYN();
	
	// 스크립트  단계별 제목 수정 시 입력값이 있을경우에만 저장버튼 활성화
	$(".script_step_title_text").keyup(function(e){
		showSaveButton();

	});
	
	// 수정 버튼이 표출되도록
	$(document).on("click", ".script", function() {
		var getId =  $(this).attr("id");
		var getInd =  $(this).attr("ind");
	});	


	
}); // document ready 마감



/**
 * 화면에서 버튼으로 동작하는 함수의 이벤트 설정
 */
function seFormFunction() {
	
	/* 스텝 저장(수정) API */
	$("#addStepBtn").click(function(){
		var info = {
			stepPk: $("#productInfo").attr("steppk"),
			transId: editTransactionId,
			stepName: $("#script_edit_title_area").val()
		}
		
		var callbackSuccess = function(data){
			var rowId = scriptGrid.getSelectedId();
			scriptGrid.cells(rowId, scriptGrid.getColIndexById('scriptName')).setValue($("#script_edit_title_area").val());
			disable(".add_Step_Btn");
			// 결재의뢰 전 수정 표시
			if(clickedStepOrgName==$("#script_edit_title_area").val()){ // 수정 시작 시점의 스텝명==현재 스텝명
				$(".rowselected").find(".showIfEdited").css("display", "none");
			}else{
				$(".rowselected").find(".showIfEdited").css("display", "inline-block");
			}
		
		};
		var callbackError = function(data){
			if(data.result=='-1001'){
				opener.fromEditToLogin();
			}
		};
		
		editStep(info, callbackSuccess , callbackError);
	});
	
	/* commit 관련 함수 시작 */
	$(document).on("click", '.commit_btn', function() {
		
		if($("#addStepBtn").hasClass("enable")==true || $(".modify_Btn").hasClass("enable")==true){
			alert("스크립트 변경건 중 완료버튼(√)을 누르지 않은 구간이 있습니다.\n확인하시기 바랍니다.");
			return false;
		}else{
			showCalendar();
		}
		
        if($(".Script_line ").text().trim =='' ){
            alert("스크립트 변경건 중 완료버튼(√)을 누르지 않은 구간이 있습니다.\n확인하시기 바랍니다.");
            return false;
        }
		
	});
	
	// 적용일 팝업창 닫기
	$(document).on("click", '#datePopupCloseBtn', function() {
		$('#saveApplyDatePopup').remove();
		$("#dateBox").remove();
		$('#mask').remove();
		$(this).off("click", arguments.callee);
	});

	// 적용일 저장
	$(document).on("click", "#dateSaveBtn", function() {
		
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
		
		/* 스크립트 트랜잭션 완료(commit) API */
		var info = {
			rsProductListPk: editProductListPk,
			transId: editTransactionId,
			endType: 'commit',
			applyDate: applyDate,
			applyType: applyType
		};
		var callbackSuccess = function(data){
			isEditMode = false;
			alert("저장되었습니다.");
			$('#saveApplyDatePopup').remove(); // 달력 지우기
			$("#dateBox").remove();
			$('#mask').remove();
			$(window).off("beforeunload"); // 브라우저가 자동실행하는 '변경된 내용이 저장되지 않을 수 있습니다.' confirm message 발생하지 않도록 수정
			window.close(); // 팝업창 닫기
		}; 
		var callbackError = function(data){
			if(data.result=='-1001'){
				opener.fromEditToLogin();
			}else{
				window.close(); // 문구 alert는 api.js의 showError 함수에서 처리
			}
		};
		endTransaction(info, callbackSuccess , callbackError);
	});
	
	// 적용일 취소(팝업닫기와 동일한 동작)
	$(document).on("click", '#dateNoSaveBtn', function() {
		$('#saveApplyDatePopup').remove();
		$("#dateBox").remove();
		$('#mask').remove();
		$(this).off("click", arguments.callee);
	});
	
	/* commit 관련 함수 끝 */
	
	// 취소 버튼으로 창 닫기 -- 팝업툴바의 x버튼으로 닫을 때는 브라우저 커스텀 confirm 팝업됨 
	$(document).on("click", '.rollback_btn', function() {
		isEditMode = false;
		if(scriptGrid.getRowsNum()==0){
			$(window).off("beforeunload"); // 브라우저가 자동실행하는 '변경된 내용이 저장되지 않을 수 있습니다.' confirm message 발생하지 않도록 수정
			window.close();
		}else{
			if(confirm("진행중인 작업은 저장되지 않습니다.")){
				/* 스크립트 편집 트랜잭션 취소(rollback) API */
				var info = {
					rsProductListPk: editProductListPk,
					transId: editTransactionId,
					endType: 'rollback'	
				};
				var callbackSuccess = function(data){
					$(window).off("beforeunload"); // 브라우저가 자동실행하는 '변경된 내용이 저장되지 않을 수 있습니다.' confirm message 발생하지 않도록 수정
					window.close();
				};
				var callbackError= function(data){
					if(data.result=='-1001'){
						opener.fromEditToLogin();
					}else{
						$(window).off("beforeunload"); // 브라우저가 자동실행하는 '변경된 내용이 저장되지 않을 수 있습니다.' confirm message 발생하지 않도록 수정
						window.close();
					}
				};
				endTransaction(info, callbackSuccess , callbackError);
			}else{
				return false;
			}
		}
		
	});
	
}


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


function variabilltyDataDblClick(code){ 
	
	var html = '<span class = "editor-link" style=" display: inline-block; color:red;" contenteditable = "true" href = "#" > {' + code+ '}</span>';
	
	return html;
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
	var TRIGGER = '@';   //괄호열리고 시작키워드
	var QUERY = '';					// {@ 이후 검색한 코드
	var DIVISION ='{';
	var SEARCH = false;
	var SELECTPOS = 1;
	var EDITOR = $('.script_input');	// 어떤태그에서 에디터를 사용할것인지
	var LIST = $(".autoList");
	var IDX = 0;						// 변경할스크립트가 많으면 칸도많아지지, 그럼 결국 IDX가 필요
	
	
	LIST.css({
		'position' : 'absolute',
		'margin-left' : '3px',
		'margin-bottom' : '3px',		
		'width' : '200px',
		'height' : '75px',	
		'background-color' : 'white',
		'overflow' : 'auto',
		'z-index' : '5'
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
	

	//선택한 스크립트 태그를 만들어 리턴해준다.
	function createWordHtml(index) {

		var selectEl = $('div:eq('+SELECTPOS+')',LIST.get(index));
		//이게 키입력받고 만들어지는 html이네																		// {체크한목록이름}
		var html = '<span class = "editor-link" style=" display: inline-block; color:red;" contenteditable = "true" href = "#" >' + (DIVISION+selectEl.attr('code')) + '}</span>&nbsp';
		initList();
		return html;
	}
	
		
	//스크립트란 체크시 scriptindex검사후 IDX에 저장
	EDITOR.parent().parent().click(function() {

		var i = 0;
		var z = 0;
		for (i = 0; i < $(this).children().length; i++) {

			if($(this).children().eq(i).children().hasClass('script_input')) {	//이틀을 깨지 말것
				var j = $(this).children().eq(i).children().length;
				for (var z = 0; z < j; z++) {
					if($(this).children().eq(i).children().eq(z).hasClass('script_input')) {
//						IDX = $(this).children().eq(i).children().eq(z).attr('scriptindex');	//scriptindex = IDX와 맞출 인덱스번호 (수정할 스크립트 span 에 있음)
						IDX = $(this).attr('scriptindex');	//scriptindex = IDX와 맞출 인덱스번호 (수정할 스크립트 span 에 있음)
						
					}
				}
			}
		}
	});
	


	//커서 확인 할 수 있도록 keyup해서 list에  값 넣기
	EDITOR.parent().parent().keyup(function(e) {
		if (e.which == 13) {
			return;
		}
		
		//var text = $(this).text(),				// 키가 눌릴때마다 html이 text에 담기고
		var text = window.getSelection().anchorNode.wholeText;
		
		if(text != undefined){
			trigger_pos = text.lastIndexOf(TRIGGER);		// @를 찾는 마지막 인덱스가 trigger_pos에 담김 -1일시 맨마지막임
			
			// "{" 괄호 찾기
			var tmpTrigger_pos;
			var inputText = window.getSelection().anchorNode.wholeText;
			
			if(trigger_pos > -1) {
					var trigger_end = window.getSelection().anchorOffset;
					QUERY = text.substring(trigger_pos , trigger_end).trim();
	//				QUERY = text.substring(trigger_pos + 1, trigger_end).trim();
					QUERY = QUERY.replace(/}/gi,"");
					console.log("keyup Query : "+QUERY);
					//QUERY = keyword -->>> 
					let words = '';
					let codes = '';
					if(QUERY != '') {
						
						words = datas; 
						codes = codeDatas; 
					}
					
					if( variabilltyData == null || variabilltyData == 'undefined' || variabilltyData.length == 0 ){
				
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
			
					
					//SELECTPOS = 리스트상에서 선택되어져있는 인덱스 번호
						LIST.eq(IDX).html(html);
						LIST.eq(IDX).removeClass('hide');
	//				$('div:eq('+SELECTPOS+')',LIST).css("background-color","white");		
					
					//마우스 원클릭 이벤트 LIST마다 클릭이벤트 부여 ( SELECTPOS ) 에맞게 버튼 색칠
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
						//문구박스 더블클릭이벤트
						$('div:eq('+i+')',LIST).dblclick(function() {	
	
							var createHtml = createWordHtml(IDX);								
							var editHtml = EDITOR.get(IDX).innerText;		
							
							$(EDITOR.get(parseInt(IDX))).html($(EDITOR.get(parseInt(IDX))).html().replace(DIVISION+TRIGGER , createHtml));
							setEndOfContenteditable(EDITOR.get(IDX));
						});
	//			
	
	
					}
					
					// enter
					if(e.which ==13) {
						return false;
					}
					$('div', LIST).removeClass('active');
					var taa = inputText.substring(0,trigger_pos).replace(/{/gi,"")
					taa = taa.substring(taa.indexOf("<span style=",0)).replace(/<span style="color:#1e34db;">/gi,"")
	
					$('div:eq('+ SELECTPOS + ')', LIST).addClass('active');
				
					
			} else {
				initList();
			}
		}
	});// KEYUP

};



// 콜백 함수
function OnSendScriptSocketError(error) {
   console.log(error);
}

function OnSendScriptSocketConnect() {
	console.log("sendScriptSocketClient success");
}

function OnSendScriptSocketDataReady(data) {
	console.log(data);
}

function OnSendScriptSocketDisconnect() {
   console.log("소켓이 끊어짐");
}

function showScript(id) {
	scriptId = id;
	var obj = getListFilter(scriptJsonArray, "scriptCode", id);
	if (id == "script_menu") {
		scriptId = "";
		$("#rec_script_box").hide();
		$("#select_script_box").show();
		$("#script_info_box").show();
	} else {
		$(".rec_script_title").text($("#"+id).text());
		$(".rec_script").text(obj[0].script);
		
		var isChecked = $("#"+scriptId+" .chkImg").prop('checked');
		if (isChecked == true) {
			$(".chkIcon").prop('checked', true);
		} else {
			$(".chkIcon").prop('checked', false);
		}
		$("#rec_script_box").show();
		$("#select_script_box").hide();
		$("#script_info_box").hide();	
		
	    $("#play_btn").removeClass("pause");
	    $("#play_btn").addClass("play");
		
		if (obj[0].audioFilePath != undefined && obj[0].audioFilePath != "") {
			scriptwavesurfer.load(obj[0].audioFilePath);
		} else {
//			progress.on();
			var dataStr = {
				"scriptCode" : scriptId,
				"productType" : $("#custProduct option:selected").val()
			}
			
			$.ajax({
				url:contextPath+"/playScriptTTS.do",
				data : dataStr,
				type:"POST",
				dataType:"json",
				timeout: 5000,
				async: false,
				success:function(jRes) {
					if(jRes.success == "Y") {
						var result = jRes.resData.listenUrl;
						scriptwavesurfer.load(result);
					} else {
						scriptwavesurfer.empty();
						$("#total_time").text("00:00:00");
						$("#now_time").text("00:00:00");
						alert("스크립트 오디오 파일을 찾을 수 없습니다.");
					}
					//progress.off();
				},
				error : function(error) {
					//progress.off();
					scriptwavesurfer.empty();
					$("#total_time").text("00:00:00");
					$("#now_time").text("00:00:00");
					alert("스크립트 오디오 파일을 찾을 수 없습니다.");
			    }
			});
		}
	}
}

function getListFilter(jsonArray, key,value) {
	return jsonArray.filter (function(object) {
		return object[key] === value;
	});
}

function seconds2time (seconds, type) {
	var hours = 0;
    var time = "", chkTime = true;

	if ( type === undefined ) type = true;
	if ( type ) hours   = Math.floor(seconds / 3600);

	seconds = Math.floor( seconds, 0 );
	var minutes = Math.floor((seconds - (hours * 3600)) / 60);
    var seconds = seconds - (hours * 3600) - (minutes * 60);

	if ( type )
	{
		if (hours != 0) time = (hours < 10) ? "0"+hours+":" : hours+":";
		else time = "00:";

		if ( time !== "" ) chkTime = true;
		else chkTime = false;
	}

    if (minutes != 0 || chkTime )
	{
		minutes = (minutes < 10 && chkTime) ? "0"+minutes : String(minutes);
		time += minutes+":";
    } else {
		time += "00:";
	}

    if (time === "") time = seconds;
    else time += (seconds < 10) ? "0"+seconds : String(seconds);

    return time;
}

//상품 선택시 생기는 트리 구조
function loadAuthy() {
	var $groupList = $('.group_list').find('.group_name');
	$groupList.empty();
	$.ajax({
	//	url: contextPath+"/getAuthyList.do",
		data: {},
		type: "POST",
		dataType: "json",
		success: function(jRes) {
			var authyList = jRes.resData.authyList;
			// 권한 리스트 뿌리기
			for(i=0; i< authyList.length; i++) {
				var authyName = authyList[i].levelName;
				var authyCode = authyList[i].levelCode;
	
				//마스타 권한 외의 사람은 마스타 권한 안보이게 ㅠ
				if(userInfoJson.userLevel != "E1001") {
					if(authyCode == "E1001") {
						continue;
					}
				}
				$groupList.append('<li><div class="group_name_wrap" level-code="'+authyCode+'"><p class="icon_authy_common icon_authy_'+authyCode+'">' + authyName + '</p></div></li>');
	
				//마지막에 이벤트 붙여주기
				if(i== authyList.length -1) {
					attachEvent();
				}
			}
		}
	});
	
	// 플레이어 비활성화
	top.playerVisible(false);
}

// 그리드 기본 설정후 생성
function seCreateScriptStepGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {
	
	var grid = new dhtmlXGridObject(objGrid);
	grid.setPagingSkin("toolbar","dhx_web");
	grid.setImagePath(recseeResourcePath + "/images/project/");
	grid.enableColumnAutoSize(false);
	grid.setSkin("dhx_web");
	grid.setImageSize(1, 1);
	grid.enableAutoWidth(true);
	grid.enableTooltips("true,false,false,false,false,false");
	grid.init();
	
	return grid;
}


// 스크립트 수정 창에 표출
function scriptLoadGrid(key) {
	scriptGrid.clearAndLoad(contextPath + "/wooribank/script/api/step/list/" + key + "/xml?transactionId=" + editTransactionId, function() {
		scriptGrid.expandAll();
	});
}


// Grid 초기화 함수
function initScriptStepGrid() {
	
	// transaction에 필요한 rsProductListPk 세팅
	$('#productInfo').attr("productPk", editProductListPk);
	
	// 트리그리드
	scriptGrid = seCreateScriptStepGrid("scriptGrid", "recMemoAddButton","scriptGrid", "?header=true&rs_script_step_fk=1", "", 20, [],[]);
	// 전체듣기 초기화
	allListenArray = [];
	// 스크립트 그리드 클릭
	scriptGridAttchEvent();
	// 스크립트 그리드 리로드(clearAndLoad)
	scriptLoadGrid(productGroupInfo.productPk);
}

function scriptGridEditRowClickHandler(id, ind) {

	disable("#addStepBtn");
	// 오디오 스탑
	if(audioTag != undefined && audioTag != null && false == audioTag.paused && audioTag.currentTime > 0){
		audioTag.pause();
		audioTag.currentTime = 0;
		
	}
	//click 표출되어있던 +,U,D 안보이게하기
	if($(".scGirdStepOneDe").hasClass("clicked")){
		$(".scGirdStepOneDe").removeClass("clicked");
		$(".scGirdStepOneDe").css("visibility","hidden");		
	}
	if($(".scGirdStepTwoDe").hasClass("clicked")){
		$(".scGirdStepTwoDe").removeClass("clicked");
		$(".scGirdStepTwoDe").css("visibility","hidden");			
	}

	// 선택된 로우의 버튼만 보이게 하는 로직
	var newRowId = scriptGrid.getSelectedRowId();
    var cells = scriptGrid.cells(newRowId, scriptGrid.getColIndexById('addScriptStepRowLank'));
    
    if(cells == null) return;
    
	var newRowValue = cells.getValue();
	var newRowValueId = $(newRowValue).find('.stepAddDeleteBtn').attr('id');
	
	$('#'+newRowValueId).css("visibility", "visible");
	$('#'+newRowValueId).css("display", "inline-block");
	$('#'+newRowValueId).addClass("clicked");

	var rScriptStepPk = scriptGrid.cells(newRowId,scriptGrid.getColIndexById('rScriptStepPk')).getValue();
	var rScriptStepFk = scriptGrid.cells(newRowId,scriptGrid.getColIndexById('rScriptStepFk')).getValue();
	var scriptName = scriptGrid.cells(newRowId,scriptGrid.getColIndexById('scriptName')).cell.innerText;

	var rProductCode = productGroupInfo.productCode;
	var changeScriptName = scriptGrid.cells(newRowId,scriptGrid.getColIndexById('changeScriptName')).getValue(); 


	var rScriptInfo = {
		rScriptStepPk : rScriptStepPk,
		rScriptStepFk : rScriptStepFk ,	
		scriptName : scriptName,
		rProductCode :rProductCode
	}
	
	if(rScriptStepPk != null && rScriptStepPk != '') {
		loadScriptDetail(rScriptInfo); 
	}

}

//스크립트 그리드 클릭시 동작
function scriptGridAttchEvent() {
	
	scriptGrid.attachEvent("onXLS", function() {

	});


	// 파싱완료
	scriptGrid.attachEvent("onXLE", function(grid_obj,count) {
		ui_controller();
		
		// 인입 시 스텝 개수 세기 - 우측 하단에 취소or취소+결재의뢰 표출 구분용
		editInitStepNum = scriptGrid.getRowsNum();
		console.log("초기 값:" + editInitStepNum);
		
		if(grid_obj.getRowsNum()==0){
			showOrHideEdit();
		}else{
			var rScriptStepFk =grid_obj.cells(grid_obj.getRowId(0),grid_obj.getColIndexById('rScriptStepFk')).getValue();
			var rScriptStepPk =grid_obj.cells(grid_obj.getRowId(0),grid_obj.getColIndexById('rScriptStepPk')).getValue();
			var scriptName =grid_obj.cells(grid_obj.getRowId(0),grid_obj.getColIndexById('scriptName')).cell.innerText;
			var rProductCode = productGroupInfo.productCode;
			
			var scriptInfo = {
				rScriptStepFk : rScriptStepFk,
				rScriptStepPk : rScriptStepPk,
				scriptName : scriptName,
				rProductCode : rProductCode,
			}
			
			if(scriptGrid.getSelectedId() == null){
				scriptGrid.selectRow(0); // 최초 진입 시(사용자 선택 행이 없을 시) 첫 번째 행 선택
			}
			loadScriptDetail( scriptInfo);
		}
	});
	
	scriptGrid.attachEvent("onRowSelect", function(id, ind) {
	
		scriptGridEditRowClickHandler(id, ind);
	});
	
	scriptGrid.attachEvent("onRowDblClicked", function() {
		scriptGrid.editStop();
	});	

}


//스크립트 그리드 데이터 받아와서 뿌려주기 시작
function loadScriptDetail(scriptInfo) {

	
	var name = scriptInfo.scriptName;
	var stepPk = scriptInfo.rScriptStepPk;
	var stepFk = scriptInfo.rScriptStepFk;
	var productCode = scriptInfo.rProductCode;
	
	$("#productInfo").attr("stepPk",stepPk);
	$("#productInfo").attr("stepFk",stepFk);
	$("#productInfo").attr("productCode",productCode);
	
	// div 초기화
	$('#realScriptDetailContentValue').html("");
	
	/* 스크립트 스텝 디테일 조회 API */
	var param = {
		stepPk: stepPk,
		productCode: productCode,
		name: name,
		transId: editTransactionId
	};
	
	var callbackSuccess = function(jRes){
		
		// 선택한 로우 가지고 오기
		var rProductCode = productGroupInfo.productCode;
		var rScriptStepFk = editProductPk;
		var rProductType = productGroupInfo.productType;
		checkRealTimeTTS(rProductCode,rScriptStepFk, rProductType);
		resultData = jRes.resData.scriptDetailList;		
		
		if(resultData.length > 0 ){
			for (var i = 0; i < resultData.length; i++) {
				scriptDetailInfoList = resultData[i];
				displayScriptDetailBlock(scriptDetailInfoList,i);		
				
				attachDetailContentEvent(i , scriptDetailInfoList);
			}
			
			automaticPhrase(realTimeValueArray);
		}
		
		// 현재 '수정' 표시가 없는 경우, 조회되는 디테일 값을 array로 저장
		var scriptLength = $(".script").length;
		if($(".rowselected").find(".showIfEdited").css("display")=="none"){
			clickedStepOrgDetail = []; // 이전 조회내용 비우기
			for( var i=0 ; i < scriptLength ; i++ ){
				clickedStepOrgDetail.push($(".script").eq(i).attr("stepdetailpk"));
			}
		}

		// stepName
		$("#script_edit_title_area").val(name);
		clickedStepOrgName = name;
		
		if($("#realScriptDetailContentValue").html()=='' ||$("#realScriptDetailContentValue").html()==null ){
			var html ='';	
			html += '';
			html += "<div id ='noneScript' class='none_script enable' style='color:#a19a9a; font-size:16px; width:auto; height :auto;'>등록된 스크립트가 없습니다.</div>"
			$("#realScriptDetailContentValue").append(html);
		}

	};
	
	var callbackError = function(data){
		if(data.result=='-1001'){
			opener.fromEditToLogin();
		}
	};
	
	getTempStepDetail(param, callbackSuccess , callbackError);

}

// 스크립트 디테일 형태 그려주기
function displayScriptDetailBlock(scrtiptDetailInfo,ind){
	
	var scriptDetailValue;
	if(scrtiptDetailInfo != null){
		scriptDetailValue = 1;
	}else {
		scriptDetailValue = 0;
	}
	
	var rProductClass = scrtiptDetailInfo.rProductClass ;
	var rScriptCommonConfirm = scrtiptDetailInfo.rScriptCommonConfirm ;
	var rScriptCommonPk = scrtiptDetailInfo.rScriptDetailComFk;
	var rScriptCommonType = scrtiptDetailInfo.rScriptCommonType ;
	var commonType = scrtiptDetailInfo.rScriptDetailComKind ;
	var rScriptDetailComFk = scrtiptDetailInfo.rScriptDetailComFk ;
	var rScriptDetailComKind = scrtiptDetailInfo.rScriptDetailComKind ;
	var rScriptDetailConfirm = scrtiptDetailInfo.rScriptDetailConfirm ;
	var rScriptDetailConfirmDate = scrtiptDetailInfo.rScriptDetailConfirmDate ;
	var rScriptDetailConfirmUser = scrtiptDetailInfo.rScriptDetailConfirmUser ;
	var rScriptDetailCreateDate = scrtiptDetailInfo.rScriptDetailCreateDate ;
	var rScriptDetailCreateUser = scrtiptDetailInfo.rScriptDetailCreateUser ;
	var rScriptIfCase = scrtiptDetailInfo.rScriptDetailIfCase ;
	var rScriptDetailIfCaseCode = scrtiptDetailInfo.rScriptDetailIfCaseCode ;
	var rScriptDetailIfCaseDetail = scrtiptDetailInfo.rScriptDetailIfCaseDetail ;
	var rScriptDetailIfCaseDetailCode= scrtiptDetailInfo.rScriptDetailIfCaseDetailCode ;
	var rScriptDetailOrder= scrtiptDetailInfo.rScriptDetailOrder ;
	var rScriptDetailPk= scrtiptDetailInfo.rScriptDetailPk ;
	var rScriptDetailRealtimeTTS= scrtiptDetailInfo.rScriptDetailRealtimeTTS ;	
	var rScriptDetailReservDate = scrtiptDetailInfo.rScriptDetailReservDate ;
	var rScriptDetailText = scrtiptDetailInfo.rScriptDetailText ;
	var rScriptDetailType = scrtiptDetailInfo.rScriptDetailType ;
	if(rScriptDetailComKind == "Y"){
		rScriptDetailType = rScriptCommonType;
	}
	var rScriptDetailUdateDate = scrtiptDetailInfo.rScriptDetailUdateDate ;
	var rScriptDetailUpdateUser = scrtiptDetailInfo.rScriptDetailUpdateUser ;
	var rScriptStepFk = scrtiptDetailInfo.rScriptStepFk ;
	var rScriptStepPk = scrtiptDetailInfo.rScriptStepPk ;
	var rUseYn = scrtiptDetailInfo.rUseYn ;
	var rScriptDetailComDesc = scrtiptDetailInfo.rScriptCommonDesc;
	var rProdutAttributes = scrtiptDetailInfo.rProdutAttributes;
	var disabledVal = "";
	
	if(scrtiptDetailInfo.rScriptDetailComKind == 'Y'){
		disabledVal = "disabled";
	}
	

	var html ='';	
		html += "<div id='script"+ind+"' class='script' ind='"+ind+"' stepFK='"+rScriptStepFk+"' stepPk='"+rScriptStepPk+"' stepDetailPk='"+rScriptDetailPk+"' realTTS='"+rScriptDetailRealtimeTTS+"' scriptindex='"+ind+"' detailType='"+rScriptDetailType+"'>";
		html += 	"<div id='script_BtnLine"+ind+"' class='BtnLine' style='height : 30px'>";
		// [공통문구]**리딩 혹은 **리딩 표출되는 function
        html += detailTextCommonCheck(commonType,rScriptDetailType,writeCommonText,recValue,ind,rScriptCommonPk);
        html +=         "<div id='selectBtnLine"+ind+"' class='selectBtnLine' style='height:30px; width:75px; float:right; text-align:center' >";
        if(rProdutAttributes.length != '0'){
        	if(productGroupInfo.eltYN.toLowerCase() == 'n'){
    			html +=             "<div  id='delete_Btn"+ind+"' class='delete_Btn' style='background-color: white; border-radius : 4px; width:22px; display: inline-block; cursor: pointer; float:right; border:1px solid #CED3D9; margin-left:10px; margin-right:0px'  value='삭제' >" 
				+"<img id='delete_content_Btn"+ind+"' class='delete_contentBtn' src='"+recseeResourcePath +"/images/project/icon/wooribank/ic_trash.svg' style= 'width: 22px;   height: 20px;  text-align: center;'/>" 
			+"</div>";
        	}else{
        		html +=             "<div  id='delete_Btn"+ind+"' class='delete_Btn disable' style='background-color: white; border-radius : 4px; width:22px; display: inline-block; cursor: pointer; float:right; border:1px solid #CED3D9; margin-left:10px; margin-right:10px'  value='삭제' >" 
        		+"<img id='delete_content_Btn"+ind+"' class='delete_contentBtn disable' src='"+recseeResourcePath +"/images/project/icon/wooribank/ic_trash.svg' style= 'width: 22px;   height: 20px;  text-align: center;'/>" 
        		+"</div>";	
        	}
		}else{	
			html +=             "<div  id='delete_Btn"+ind+"' class='delete_Btn' style='background-color: white; border-radius : 4px; width:22px; display: inline-block; cursor: pointer; float:right; border:1px solid #CED3D9; margin-left:10px; margin-right:0px'  value='삭제' >" 
    							+"<img id='delete_content_Btn"+ind+"' class='delete_contentBtn' src='"+recseeResourcePath +"/images/project/icon/wooribank/ic_trash.svg' style= 'width: 22px;   height: 20px;  text-align: center;'/>" 
    						+"</div>";
		}
        	html +=             "<div id='modify_Btn"+ind+"'  class='modify_Btn disable' style='background-color: white; margin-right : 0px; width:20px; display: inline-block; cursor: pointer;  float:right; border-radius : 4px;border:1px solid #CED3D9;'  value='수정' >" 
					        	+"<img id='modify_content_Btn"+ind+"' class='modify_contentBtn'ind="+ind+" ind="+ind+" src='"+recseeResourcePath +"/images/project/icon/wooribank/icon_path_check.png' style= 'width: 20px;   height: 20px;  text-align: center;'/>"
					        +"</div>";        
        	html +=           "</div>"; 
		// 조건 선택해주는   onchange='setScriptDetailCaseType("+ind+")'
		html += 		"<div id='selectLine' style='height:30px; width:350px; float:right;'>"
		if(productGroupInfo.eltYN.toLowerCase() == "y"){
			html +=				"<select  id='typeselect"+ind+"'  class='type_select'  ind='"+ind+"' style='background-color: white; width:150px;' "+ " name='조건추가' onchange='selectChange(this,"+ind+")' disabled  >";
			html += 				scriptSelectBox(ind,rScriptIfCase,rScriptDetailIfCaseCode,rScriptDetailIfCaseDetail,rScriptDetailIfCaseDetailCode);			
			html += 			"</select>";		
			html += 			"<select  id ='typeValue"+ind+"' class='type_value' ind='"+ind+"'  style='background-color: white; display:none; width:177px;' disabled >";			
			html += 			"</select>";
			html += 			"<input type='text' id='typeText"+ind+"' ind='"+ind+"' class='type_Text'  style='height : 15px; width:177px; margin-right:10px;' contenteditable='true' disabled />";
			html +=			"</div>"
		}else{
			html +=				"<select  id='typeselect"+ind+"'  class='type_select'  ind='"+ind+"' style='background-color: white; width:150px;' "+ " name='조건추가' onchange='selectChange(this,"+ind+")'  >";
			html += 				scriptSelectBox(ind,rScriptIfCase,rScriptDetailIfCaseCode,rScriptDetailIfCaseDetail,rScriptDetailIfCaseDetailCode);			
			html += 			"</select>";		
			html += 			"<select  id ='typeValue"+ind+"' class='type_value' ind='"+ind+"'  style='background-color: white; display:none; width:177px;' >";			
			html += 			"</select>";
			html += 			"<input type='text' id='typeText"+ind+"' ind='"+ind+"' class='type_Text'  style='height : 15px; width:177px; margin-right:10px;' placeholder='여기에 입력하세요' contenteditable='true' />";
			html +=			"</div>"
		}
		
		html +=			"<input type='button' id='audioPlay' class='preview"+ind+" disable' style='float:right;width:65px;text-indent:0px !important; cursor:pointer; margin-right:10px;'margin-top:3px; value='미리듣기'/>";
		html +=		"</div>"
			
		html += 	"<div id='script_textAtrea"+ind+"' class='textArea' text='textArea' ind='"+ind+"'>";	
		// 공통문구일 시 공통문구 설명 표출
		if(commonType == "Y"){
			html +=  	"<div id='comExplain"+ind+"' class='comExplain' style='height : 15px; color :#628095; font-weight : bold; padding:5px; font-size:12px; margin-top:3px; ' >"+rScriptDetailComDesc+"</div>"	
		}	
		var text = scrtiptDetailInfo.rScriptDetailText;
		if(text != null){
			text = text.replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi, "}</span>");								
		}
		html +=	"<div id='script_input"+ind+"' class='Script_line scriptContext"+ind+" script_input'>";
		if(commonType == "Y"){
			html += 	"<pre id ='scriptDetailText"+ind+"' contenteditable='false' style='min-height : 20px; padding:5px;'>"+ text + "</pre>";
		}else {
			html += 	"<pre id ='scriptDetailText"+ind+"' class='scriptDetailText'  contenteditable='true' style='min-height : 20px; padding:5px;'>"+ text + "</pre>";
		}
		html += "</div>";
		
		// 우측 상단 셀렉트 값 저장 -- 상품코드 입력 시 빈칸 입력 불가하게 함
		html += "<input type='hidden' id='normalSelectedType"+ind+"'>";
		
		html += "</div>";

		// 가변값 자동완성 내역
		html += 	"<div id='list' class='panel panel-default hide autoList '  autoList  scriptindex='"+ind+"'>";
		html += 		"<div class='list-group'></div>";
		html += 	"</div>";
 
		// ELT상품일 떄만 보여질 상품 조건	 productJson.rsEltYN.toLowerCase()
		if(rProdutAttributes.length != '0'){
		html += 	"<div id='eltProductCondition"+ind+"' class='eltProductCondition ' scriptindex='"+ind+"' >";
		html +=			displayEltProductCondition(ind,rProdutAttributes);
		html += 	"</div>";
		}	
	
	$("#realScriptDetailContentValue").append(html);
	
	setScriptDetailCaseType(rScriptDetailIfCaseDetail,ind,rScriptDetailIfCaseDetailCode);
	
	// TTS리딩, 적합성보고서 일 경우에 미리듣기 버튼 활성화
	if ( scrtiptDetailInfo.rScriptDetailType == "T"|| scrtiptDetailInfo.rScriptDetailType  == "R") {
		enable(".preview"+ind);
		enable(".allListen");
		allListenArray.push(text);		
	}
	// productInfo의 rsEltYn 값에 따라 수정 화면 변경 함수
	setVisibleScriptDetailAddButtons(rProdutAttributes,productGroupInfo.eltYN);
	// rProdutAttributes가 null이어서 false값을 받을 시 안 보이도록
	if($("#eltProductCondition"+ind).text()=='false'){
		$("#eltProductCondition"+ind).css("display","none");
	}
	if(rProdutAttributes.length != '0'){
		$(".scGirdStepOneDe").remove();
		$(".scGirdStepTwoDe").remove();
	}
}


/**공통문구 용인지 체크하는 로직 - 기존 디테일 표시
 * @param commonType : script_detail 테이블의 rScriptDetailComKind(공통문구 사용여부)
 * @param rScriptDetailType : script_detail 테이블의 rScriptDetailType
 * @param commonCode : 선택된 상품의 공통문구에 대한 코드 
 * @param commonName : 선택된 상품의 공통문구에 대한 이름 
 * @param code : 선택된 rScriptDetailType의 코드 
 * @param name : 선택된 rScriptDetailType의 이름 
 * @returns
 */
function detailTextCommonCheck(commonType,rScriptDetailType,writeCommonText,recValue,ind,commonFk) {
	var html = "";		
		for(var i=0 ; i<recValue.length; i++){
			var scriptTypeVal= recValue[i].code
			if(scriptTypeVal == rScriptDetailType){
				var scriptTypeName = recValue[i].name;
				if(commonType == 'Y'){						
					html = "<div id='genneralCheckCommon"+ind+"' class ='genneralCheckY common' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' detailComFk='"+commonFk+"' >"+writeCommonText+" "+scriptTypeName+"</div>"							
				}else if(commonType == 'N'){	
					if(scriptTypeVal == 'T'){
						html = "<div id='genneralCheck"+ind+"' class ='genneralCheckNT' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' detailComFk='"+commonFk+"' >"+scriptTypeName+"</div>"						

					}else if(scriptTypeVal == 'S'){
						html = "<div id='genneralCheck"+ind+"' class ='genneralCheckNS' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' detailComFk='"+commonFk+"' >"+scriptTypeName+"</div>"						

					}else if(scriptTypeVal == 'G'){
						html = "<div id='genneralCheck"+ind+"' class ='genneralCheckNG' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' detailComFk='"+commonFk+"' >"+scriptTypeName+"</div>"						

					}else if(scriptTypeVal == 'R'){
						html = "<div id='genneralCheck"+ind+"' class ='genneralCheckNR' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' detailComFk='"+commonFk+"'>"+scriptTypeName+"</div>"						
					}
				}
		return html;
		}
	}
}

function createFileTTS() {
	var mentTxt = "이 투자신탁은 환매가 가능한 개방형이며, 추가로 자금 납입이 가능한 추가형이고, "
		+ "판매보수 등의 차이로 인하여 기준가격이 다르거나 판매수수료가 다른 여러 종류의 집합투자증권을 "
		+ "발행하는 종류형 투자신탁입니다. 또한 투자신탁이 발행하는 집합투자증권을 자투자신탁이 취득하는 "
		+ "구조의 모자형투자신탁입니다."
		+ "펀드는 집합투자를 수행하는 기구로서 법적으로 집합투자기구라 표현되며, "
		+ "다수의 투자자의 돈을 모아서 운용사 집합투자업자)를 통하여 수익을 얻어 이를 투자자들이 "
		+ "나누어 갖게 됩니다. 펀드의 형태로는 투자신탁, 투자회사 등이 있으며, "
		+ "고객님께서 선택하신 펀드는 투자신탁 형태입니다.";
	var dataStr = {
		"mentTxt" : mentTxt
	}
	$.ajax({
		url:contextPath+"/createFileTTS.do",
		type:"POST",
		dataType:"json",
		data:dataStr,
		async: false,
		success:function(jRes) {
			if(jRes.success == "Y") {
			} else {
			}
		}
	});
}


// 실시간 여부 확인하는 ajax function
function checkRealTimeTTS(rProductCode,rScriptStepFk,rProductType) {
	realTimeValueArray = [];
	var dataStr={
		"rProductCode" :  rProductCode,
		"rScriptStepFk"	: rScriptStepFk,
		"rProductType"	: rProductType
	}
	$.ajax({
		url:contextPath+"/selectValueTTS.do",
		type:"POST",
		dataType:"json",
		data : dataStr,
		traditional:true,	
		async: false,
		success:function(jRes) {
			if(jRes.success == "Y") {	
				var result = jRes.resData.selectValueTTS;
				for(var i=0; i<result.length; i++) {
					realTimeValueArray.push({name : result[i].rsProductValueName, code : result[i].rsProductValueCode, value : result[i].rsProductValueVal});
				}
				//console.log("realTimeValue 성공");
			} else {
				//console.log("realTimeValue 실패");
			}
		}
	});
}

// 고개유형 등 첫번쨰  SelectBox에 값을 채워주는 function
function scriptSelectBox(ind,rScriptIfCase,rScriptDetailIfCaseCode,rScriptDetailIfCaseDetail,rScriptDetailIfCaseDetailCode){
	var option ="" ;	
	
	// 조건 없는 경우에만 '조건 없음' select option 추가
	// if(rScriptIfCase== 'N' || rScriptDetailIfCaseCode == null){// 실제로는 Case N으로만 체크해도 되지만, 데이터 오입력 등을 고려하여 CaseCode가 null인 경우도 체크함
		option = "<option  value='' selected='selected'>"+"조건 없음"+"</option>";
	// } 
	for(var i=0; i <detailCase.length; i++){
		var selected = "";
		if(rScriptIfCase == 'Y' && rScriptDetailIfCaseCode == detailCase[i].code){
			selected = " selected";		
		}
		option += "<option value='"+detailCase[i].code+"' "+selected+">"+detailCase[i].name+"</option>";
	}
	return option;
	
	$("#typeselect"+ind).on("change",function(option){
		setScriptDetailCaseType();
	});
};
	

// 고객유형 클릭 시 셀렉트 박스 show 함수
function selectChange(obj,ind) {
	
	var appendToName = $(obj).hasClass("newSelect");
	var selectVal = $(obj).val();
	if(appendToName==false){
		$("#typeValue"+ind).empty();	
		$("#normalSelectedType"+ind).val(selectVal);
	}else{
		$("#typeValue_new"+ind).empty();	
		$("#normalSelectedType_new"+ind).val(selectVal);
	}
	
	for(var j = 0; j<detailCase.length; j++){
		if(detailCase[j].values != null && detailCase[j].values.length > 0){
			if(selectVal == detailCase[j].code){
				$(obj).next().css("display","inline");
				$(obj).next().next().css("display","none");
				for(var i =0; i< detailCase[j].values.length; i++){
					var subitem = detailCase[j].values[i];
					option = "<option  value='"+subitem.code+"'>"+subitem.name+"</option>";
					if(appendToName==false){				
						$("#typeValue"+ind).append(option);
						$("#typeValue"+ind).css("border","1px solid red");
					}else{						
						$("#typeValue_new"+ind).append(option);						
						$("#typeValue_new"+ind).css("border","1px solid red");				
					}
				}	
			return true;
			}			
		}else {
			$(obj).next().css("display","none");
			$(obj).next().next().css("display","inline");
		}
	}
};

// 첫번쨰 SelectBox에 선택된 값을 기준으로 rScriptDetailIfCaseDetail의 컬럼의 값을 표출하기 위한 현재상태 저장 function
function setScriptDetailCaseType(rScriptDetailIfCaseDetail,ind,rScriptDetailIfCaseDetailCode){
	//선택한 값
	var check = $("#typeselect"+ind+" option:selected").val(); 
	for(var i=0; i <detailCase.length; i++){
		var selectedType = detailCase[i].code;			
		if(check == selectedType){
			setScriptDetailCaseValue(detailCase[i],rScriptDetailIfCaseDetailCode,rScriptDetailIfCaseDetail,ind);
		}
	}
}

// 현재 선택된 첫번쨰 SelectBox의 데이터 내용에 따라 옆의 테그에 데이터 넣어주기
function setScriptDetailCaseValue(caseItem,rScriptDetailIfCaseDetailCode,rScriptDetailIfCaseDetail,ind){
	
	$("#typeValue"+ind).empty();
	$("#typeValue_new"+ind).empty();
	
	// 선택한 케이스의 value 를 가져와서 보여준다.
	if ( caseItem.values == null || caseItem.values.length == 0 ) {
		$("#typeText"+ind).attr('value',rScriptDetailIfCaseDetail)
		$("#typeValue"+ind).hide();
		$("#typeText"+ind).show();
	} else {
		$("#typeValue"+ind).show();
		$("#typeText"+ind).hide();
		
		for(var i =0; i< caseItem.values.length; i++){
			var caseDetailCode = rScriptDetailIfCaseDetailCode == null ? "" : rScriptDetailIfCaseDetailCode;
			var selected = caseItem.values[i].code == caseDetailCode ? " selected" : "";	
			var subitem = caseItem.values[i];
			option = "<option  value='"+subitem.code+"' "+selected+" >"+subitem.name+"</option>";
			$("#typeValue"+ind).append(option)
			
			
		}			
	} 

}


function ScriptChangeCaree(){
    
    $.ajax({
        url:contextPath+"/selectProductList.do",
        type:"POST",
        dataType:"json",
        async: false,
        success:function(jRes){
            if(jRes.success == "Y") {
                var result = jRes.resData.selectProductList;
                var scriptJsonArray = result;
                var htmlString = "";
                for(var i = 0; i < scriptJsonArray.length; i++) {
                    productArray.push(result[i].rProductCode)
                    productTypeArray.push(result[i].rProductType)
                };
            
            }else{
               
            }
        }
    });            
}; 

   	
    	
function ButtonYN(){
	
	if(writeYn == "Y") {
		// 조회페이지 저장 버튼
		$("#change_script_btn").css("display","block");
		// 조회페이지 신규등록버튼
		$("#add_prize_btn").css("display","block");
		// 스크립트 수수정 페이지 목록수정 버튼
		$("#add_script_btn").css("display","block");
		
		
		$("#scriptContentBtnWrap").css("display","block");

		// 공용문구 신규 버튼
		$("#commonInsertBtn").css("display","block");
		// 공용문구 삽입버튼
		$("#commonCopytBtn").css("display","block");
	}
	if(modiYn == "Y") {
		// 공용문구 팝업창 수정 버튼
		$("#commonUpdateBtn").css("display","block");
	}
	if(delYn == "Y") {
		// 공용문구 삭제버튼
		$("#commonRemoveBtn").css("display","block");
		// 제거
		$(".delete_contentBtn").css("display","block");
	}
}

 
//realtest
function serachingVariableText(text){
	
	var searchArr = [];

	for (var i = 0; i < realTimeValueArray.length; i++) {
		var name = realTimeValueArray[i].name;
		var code = realTimeValueArray[i].code;
		var value = realTimeValueArray[i].value;
		
		if(text.indexOf("{"+code+"}") > -1){
			searchArr.push({code : code, value :value });
		}
	}	
	return searchArr;
	
};
 

function changeVariableValToName(text){
	
	var searchArr = serachingVariableText(text);
//	replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi,"}</span>"); 
	for (var i = 0; i < searchArr.length; i++) {
		var code = searchArr[i].code;
		var value = searchArr[i].value;
		text = text.replace("{"+code+"}" , value );
	}
	return text;
}

// 텍스트 부분에 가변값 항목이 아닌 {}가 있으면 replace로 변환하는 함수
function bracketRemove (text){
	
	if(ifdetail=='N'||ifdetail==null){
		// 앞에 괄호가 있을떄
		if(text.indexOf("{")>-1){
			text.repalce(/{/gi,"")
		}	
		// 뒤에 괄호가 있을때
		if(text.indexOf("}")>-1){
			text.replace(/}/gi,"")				
		}
			return text;		
	}else {
		return false;
	}
	
}


// TTS 리딩, 직원리딩, 상담 가이드, 적합성 보고서 클릭 시 생성되는 DIV의 첫 select박스 내역을 채워주는 함수
function addScriptSelectBox(){
	
	var option ="" ;
	option = "<option  value='' selected='selected'>"+"조건 없음"+"</option>";
	for(var i=0; i <detailCase.length; i++){
		var selected = "";
		option += "<option value='"+detailCase[i].code+"' "+selected+">"+detailCase[i].name+"</option>";
	}
			
	return option;
	
	$("#typeselect_new"+ind).on("change",function(option){
		setScriptDetailCaseType()
	})
	
};

// 디테일 수정
$(document).on("click", ".modify_contentBtn", function() {
	
	var nowIndex = $(this).attr("id");
	var parent = $(this).parent().attr("id");
	var index = $(this).attr("ind");
	
	// selectBox, textbox 수정한 항목이 있을 경우 원래 css로 
	returnOriginCss(index);
	
	var ind = nowIndex.substring(nowIndex.lastIndexOf('n')+1);
	
	if( $(".cancelScriptDetailBtn").hasClass("disable") ){
		$(".cancelScriptDetailBtn").removeClass("disable");
		$(".cancelScriptDetailBtn").addClass("enable");		
	}
	
	var stepPk = $("#script"+ind).attr("steppk");
	var stepDetailPk = $("#script"+ind).attr("stepdetailpk");
	var detailText = $("#scriptDetailText"+ind).text();
	
	if( isNull(detailText) ){
		alert("스크립트 내역을 입력해주세요.");
		return false;
	}else{
		detailText = detailText.trim();
		detailText = detailText.replace(/\</g, "&lt;");
		detailText = detailText.replace(/\>/g, "&gt;");
		detailText = detailText.replace(/\"/g, "&quot;");
		detailText = detailText.replace(/\'/g, "&#39;");
		detailText = detailText.replace("<br>", "\n");
	}
	
	var ifCase = "N"; 	
	var ifCaseCode = null;
	
	if( $('#typeselect'+ind+' option:selected').val() != ''  ){
		ifCase = "Y";
		ifCaseCode = $("#typeselect"+ind).val();
	}
	
	var ifCaseDetail = null;
	var ifCaseDetailCode = null;
	
	if( ifCaseCode != 'SCRT02' && ifCaseCode != null ){
		ifCaseDetail = $('#typeValue'+ind+' option:selected').text();
		ifCaseDetailCode = $('#typeValue'+ind+' option:selected').val();
	}else if( ifCaseCode == 'SCRT02' ){
		ifCaseDetail = $("#typeText"+ind).val().trim();
		ifCaseDetailCode = null;
	}
	
	// 셀렉트의 선택값이 상품코드인데 옆 input값이 빈값일때 alert 창 띄우기
	if( ifCaseCode=="SCRT02" && $("#typeText"+ind).val().trim() == '' ){
		alert("상품코드 선택 시 값을 반드시 입력해야 합니다.");
		return;
	}
	
	// 펀드상품인 경우, 수수료 공용문구에서 상품코드 입력 필수 -- 조건 select 값이 SCRT02(상품코드)가 아니면 디테일수정 불가, refresh 처리
	if( productGroupInfo.productType == "2"
		&& detailText.indexOf("수수료")!=-1
		&& ifCaseCode!="SCRT02" 
		&& $("#comExplain"+ind).html()!=undefined ){
		alert("수수료 관련 공용문구는 상품코드를 반드시 입력해야 합니다.");
		refreshScriptDetail();
		return;
	}
	
	var info = {
		stepPk: stepPk,
		stepDetailPk: stepDetailPk,
		transId: editTransactionId,
		detailText: detailText,
		ifCase: ifCase,
		ifCaseCode: ifCaseCode,
		ifCaseDetail: ifCaseDetail,
		ifCaseDetailCode: ifCaseDetailCode,
		realtimeTTS: "N"
	};
	
	var callbackSuccess = function(data){
		refreshScriptDetail();
		$(".rowselected").find(".showIfEdited").css("display", "inline-block"); // 변경 있는 경우
		disable("#"+parent);
	};
	
	var callbackError = function(data){
		if( data.result=='-1001' ){
			opener.fromEditToLogin();
		}
	};
	
	editStepDetail(info, callbackSuccess , callbackError);
	
});

// scriptStepDetail 추가 시 add 실행
$(document).on("click", ".detail_content_btn", function() {
	
	incres++;
	
	/* 스크립트 스텝 디테일 편집 - 추가 API */
	var ind = incres;
	var detailType = $(this).attr("addType");
	var comKind = $(this).attr("comKind")==undefined? "Y" : $(this).attr("comKind");
	var realtimeTTS = $('#common_detail_realTime option:selected').val()==undefined? "N" : $('#common_detail_realTime option:selected').val();
	var detailText = $('#common_text').val()==undefined? "" : $('#common_text').val();
	var comFk = $('#common_pk').val()==undefined? null : $('#common_pk').val();
	$("#newScript"+ind).attr("realtime", "N");
	
	var stepPk = $("#productInfo").attr("steppk");
	var stepFk = $("#productInfo").attr("stepfk");
	
	// api 연동 x, 공용문구를 디테일에 추가 시 alert용
	var comName = $('#common_name').val();
	var comDesc = $('#common_desc').val();
	
	var info = {
		stepPk: stepPk,
		transId: editTransactionId,
		detailType: detailType,
		comKind: comKind,
		stepFk: stepFk,
		detailText: detailText,
		ifCase: "N",
		ifCaseCode: null,
		ifCaseDetail: null,
		ifCaseDetailCode: null,
		realtimeTTS: realtimeTTS,
		comFk: comFk
	};
	
	var	callbackSuccess = function(data, ind){
		createNewScriptForm(info,ind);
		$("#stepDetailPk"+ind).val(data.resData.rsScriptStepDetailPk);
		disable("#add_new_contents"+ind);
		refreshScriptDetail();
		// 공용문구 추가한 경우 alert
		var timer = null;
		timer = setTimeout(function(){
			if(comFk != null) alert(comName + " [" + comDesc + "]이(가) 추가되었습니다.");
		}, 200);
		// 결재의뢰 전 수정 표시
		$(".rowselected").find(".showIfEdited").css("display", "inline-block");
	};
	var callbackError = function(data){
		if(data.result=='-1001'){
			opener.fromEditToLogin();
		}
	};

	addStepDetail(info, ind, callbackSuccess , callbackError);
	
});

// 추가버튼 클릭 시 추가되는 DIV형식
function createNewScriptForm(info,ind){
 	var stepFk = $("#productInfo").attr("stepfk");
 	var html = '';
    html += "<div id='script_new"+ind+"' class='new script' ind='"+ind+"' style='height : auto' detailType='"+info.detailType+"' stepFK='"+stepFk+"' scriptindex='"+$("#realScriptDetailContentValue").children().length+"'>";
    html +=     "<div id='scriptBtnLineNew"+ind+"' class='scriptBtnLine' style='height : 30px' >";
    html +=                detailTextCommonCheck(info.comKind,info.detailType,writeCommonText,recValue,ind,info.comFk);
    html +=         "<div id='selectBtnLine_new"+ind+"' class='selectBtnLine_new ' style='height:30px; width:75px; float:right; cursor: pointer;' >";      
    html +=             "<div  id='delete_new_contents"+ind+"' class='delete_new_contents' style='float : right; background-color: white; border-radius :4px;  display: inline-block; margin-right : 10px; cursor: pointer;  border:1px solid #CED3D9;' value='삭제' >"
    						+"<img id src='"+recseeResourcePath +"/images/project/icon/wooribank/ic_trash.svg'   style=  'width: 22px;   height: 20px;  text-align: center;'/></div>";
    html +=             "<div  id='modify_new_contents"+ind+"' class='modify_new_contents disable ' style='background-color: white; display: inline-block;border-radius :4px; border:1px solid #CED3D9;'  value='수정' ;>"
    						+"<img id src='"+recseeResourcePath +"/images/project/icon/btn_icon/icon_path_check.png' style=  'width: 19px;   height: 20px;  text-align: center;'/></div>";
    html +=            "</div>"; 
	html += 		"<div class='selectLine'style='height:30px; width:350px; float:right;' >";
	html +=				"<select  id='typeselect_new"+ind+"' ind='"+ind+"' class='type_select newSelect'  style='background-color: white; width:150px;' name='조건추가' onchange='selectChange(this,"+ind+")' >";	
	html +=					addScriptSelectBox();	
	html += 			"</select>";		
	html += 			"<select  id ='typeValue_new"+ind+"' ind='"+ind+"' class='type_value newType'  style='background-color: white; display:none; width:177px;' >";							
	html += 			"</select>";
	html += 			"<input type='text' id='typeText_new"+ind+"' ind='"+ind+"' class='type_Text newText' style='height : 15px; width:177px; margin-right:10px;' placeholder='여기에 입력하세요' />";

	html +=			"</div>";
	html +=				"<input type='button' id='audioPlay' class='preview_new"+ind+" disable' style='float:right; width:65px;text-indent:0px !important; cursor:pointer; margin-right:10px; margin-top:3px;' value='미리듣기'/>";
	html +=		"</div>";
	
	html += 	"<div id='scriptTextArea_new"+ind+"' class='scriptTextArea new' ind='"+ind+"' style='height:auto; overflow: auto; background-color: white; border-radius :4px'>";
	html += 		"<div class='script_input newScript"+ind+" scriptContext"+ind+" ' id='script_input"+$("#realScriptDetailContentValue").children().length+"' style='margin-top:3px ;' value='text' tabindex='-1' >";
	html += 			"<pre id ='scriptDetailNewText"+ind+"' class='scriptDetailText' contenteditable='true' style='min-height : 20px; padding:5px;'>" + info.detailText + "</pre>";
	html += 		"</div>";
	html += "<input type='hidden' id='normalSelectedType_new"+ind+"'>";// 우측 상단 셀렉트 값 저장 -- 상품코드 입력 시 빈칸 입력 불가하게 함
    html += 	"</div>";
	html += 		"<div id='list' class='panel panel-default hide autoList'  autoList  scriptindex='"+$("#realScriptDetailContentValue").children().length+"'>";
	html += 			"<div class='list-group'></div>";
	html += 		"</div>";
	html += "</div>";
	html += "<input type='hidden' id='stepDetailPk"+ind+"' value=''>";

	
	$("#realScriptDetailContentValue").append(html);
	// 권한
	if (delYn == "Y") {
		$(".delete_new_contents").css("display","inline-block");
	}
	if(modiYn == "Y") {
		$(".add_new_contents").css("display","inline-block");
	}	
	
	var rProductCode = productGroupInfo.productCode;
	var rScriptStepFk = editProductPk;
	var rProductType = productGroupInfo.productType;
	checkRealTimeTTS(rProductCode,rScriptStepFk, rProductType);
	automaticPhrase(realTimeValueArray); 		 
	
	setScriptDetailCaseType(info.ifCaseDetail,ind,info.ifCaseDetailCode);
	
	if ( info.detailType == "T" || info.detailType  == "R" ) {
		enable(".preview_new"+ind);
		enable(".allListen");
//				allListenArray.push(text);
	}

 };

// 디테일 삭제
$(document).on("click", ".delete_contentBtn", function() {
	
	if($(".cancelScriptDetailBtn").hasClass("disable")){
		$(".cancelScriptDetailBtn").removeClass("disable");
		$(".cancelScriptDetailBtn").addClass("enable");		
	}
	if($(".addScriptDetailBtn").hasClass("disable")){
		$(".addScriptDetailBtn").removeClass("disable");
		$(".addScriptDetailBtn").addClass("enable");		
	}
	if(!confirm("삭제 하시겠습니까?")) {
		return false;
	}else{
		/* 스크립트 스텝 디테일 편집 - 삭제 API */
		var id = $(this).attr("id");
		var ind = id.substr(id.length-1);
		var stepPk =scriptDetailInfoList.rScriptStepPk;
		var stepDetailPk = $("#script"+ind).attr("stepdetailpk");
		var transId = editTransactionId;
		var info = {
			stepPk: stepPk,
			stepDetailPk: stepDetailPk,
			transId: transId
		}
		var callbackSuccess = function(data){
			$("#script"+ind).remove();
			$(".cancelScriptDetailBtn").css("display","inline-block");
			$(".addScriptDetailBtn").css("display","inline-block");
			
			/* 결재의뢰 전 [수정] 표시 지움 */
			
			// 현재 화면의 디테일 값 확인
			var nowDetails = [];
			var nowDetailLength = $(".script").length;
			var orgDetailLength = clickedStepOrgDetail.length;
			console.log("현재 디테일 개수: " + nowDetailLength);
			console.log("원래 디테일 개수: " + orgDetailLength);
			
			for(var i=0; i < nowDetailLength; i++){
				nowDetails.push($(".script").eq(i).attr("stepdetailpk"));
			}
			
			if(nowDetailLength!=0){
				// 신규추가 디테일이 없음 and 기존 디테일=현재 디테일 :: [수정] 미표시
				if(nowDetails.toString().indexOf("N")==-1 && clickedStepOrgDetail.toString() == nowDetails.toString()){
					$(".rowselected").find(".showIfEdited").css("display", "none");
				}else{ // 신규추가 디테일이 있음 or 기존 디테일!=현재 디테일 :: [수정] 표시
					$(".rowselected").find(".showIfEdited").css("display", "inline-block");
				}
			}else{
				if(orgDetailLength==0){ // 기존 디테일이 없음 and 현재 디테일이 없음 :: [수정] 미표시
					$(".rowselected").find(".showIfEdited").css("display", "none");
				}
			}
			
		};
		var callbackError= function(data){
			if(data.result=='-1001'){
				opener.fromEditToLogin();
			}
		};
		deleteStepDetail(info, callbackSuccess , callbackError);
	}

});


// 스크립트 목록 수정/추가/삭제 시 '설명'이란 단어기준으로 ScriptStepType을 결정하는 함수
function findExplainWord(rScriptStepType){
	
	if(rScriptStepType.indexOf("설명") > -1){
		rScriptStepType = "P";
	}else{
		rScriptStepType = "N";
	}	
	return rScriptStepType
}
    

// 스크립트 목록 하위메뉴 추가 클릭	
var h = 0;
function addRowRank(obj ,ind){
	var selectRowId;
	if(obj != null){
		selectRowId = obj;		
	}
	
	// parnetCode가 0이면 추가 가능하고 아니면 가증하지 않도록
	var rScriptStepFk = scriptGrid.cells(selectRowId, scriptGrid.getColIndexById('rScriptStepFk')).getValue();
	var num = scriptGrid.getRowsNum();		
	var targetId = selectRowId;
	var parent = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('rScriptStepParent')).getValue();
	var pk = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('rScriptStepPk')).getValue();	
	var rsScriptStepFk = $('#productInfo').attr("productPk"); // rsScriptStepFk = rs_product_list_pk

	var newId = targetId + 'child' + (new Date).valueOf();
	
	/* 스크립트 스텝 추가 API */
	var selectRowId;
	var selectRowind;
	if(obj != null){
		selectRowId = obj ;		
	}
	if(ind != null){
		selectRowind = ind ;				
	}
	var stepName = "하위 스크립트 추가"+h;

	var transId = editTransactionId;
	
	// 신규 추가용 변수
	var stepFk = $('#productInfo').attr("productPk"); // 현재 조회한 상품의 product_list_pk -- 상품코드 1:1연결
	
	var stepParent = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('rScriptStepPk')).getValue(); // 하위 스텝 추가 시 상위 스텝의 pk를 parent로 함
	
	// 신규 여부 체크
	var chkNew = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('changeScriptName')).getValue();
	
	
	var info = {
		transId: transId,
		stepFk: stepFk,
		stepParent: stepParent,
		stepName: stepName
	};
	
	var callbackSuccess = function(data){
		if(parent == 0) {
			k++;
			h++;
			var buttonCol =	
				"<div class='stepDivWrap'>"
				+"<div class='placeholderDiv' style='width:30px'></div>"
				+"<div id = 'sc_gird_step_two_de_new"+k+"' class ='scGirdStepTwoDe stepAddDeleteBtn' style='visibility: hidden;'>"
					+"<div id='sc_gird_delete_rowLank"+k+"' class='scGirddeleteRowLank' onClick=deleteRowRank('"+newId+"',"+k+")  style='cursor:pointer; width:20px; text-align:center; display:inline-block; margin-right:10px; position:relative; top:5px;'>" 
						+"<img id src='"+recseeResourcePath +"/images/project/icon/wooribank/ic_trash.svg ' style='width:20px; margin : auto;'/></div>"
				+"</div>"
				+"<div class='showIfEdited' style='display:none; color:red;'>[수정]</div></div>";
				
			// 그리드에 추가된값이 표출되는 이벤트
			scriptGrid.addRow(newId,["하위 스크립트 추가"+h,"",pk,rsScriptStepFk,"new_child",buttonCol], 0, targetId);	
			
			// 추가된 그리드에 선택값이 주어지는 이벤트
			scriptGrid.selectRowById(newId);
		} else {
			alert("현재 메뉴 단계는 최하위 메뉴이므로 하위메뉴를 추가할 수 없습니다.");
		}
			
		if($("#sc_gird_add_new"+selectRowind).hasClass("disable")){
			$("#sc_gird_add_new"+selectRowind).removeClass("disable");
			$("#sc_gird_add_new"+selectRowind).css("display","inline-block");		
		}
		if( data.resData.rsScriptStepPk != null){
			var tempStepPk = data.resData.rsScriptStepPk;
			scriptGrid.cells(newId,scriptGrid.getColIndexById('rScriptStepPk')).setValue(tempStepPk);
			scriptGrid.cells(newId,scriptGrid.getColIndexById('rScriptStepFk')).setValue(editProductPk);
			// 저장 성공 시 이름 변경				
			scriptGrid.cells(newId,scriptGrid.getColIndexById('changeScriptName')).setValue('renewal');
			
			// 신규 저장 후 오른쪽 title에 stepName 입력, detail은 빈칸으로				
			$('#productInfo').attr("stepPk", tempStepPk);
			scriptGrid.selectRowById(newId);
		}
		scriptGrid.expandAll();
	};
	
	var callbackError = function(data){
		if(data.result=='-1001'){
			opener.fromEditToLogin();
		}
	};
	addStep(info, callbackSuccess , callbackError);	
	
	
};


// 스텝 삭제
function deleteRowRank(obj){
	var selectRowId;
	if(obj != null) selectRowId = obj;		
	
	var stepPk = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('rScriptStepPk')).getValue();
	var rScriptStepFk = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('rScriptStepFk')).getValue();
	var chkNew = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('changeScriptName')).getValue();
	
	// 삭제할 스텝명
	var scriptName = scriptGrid.cells(selectRowId,scriptGrid.getColIndexById('scriptName')).cell.innerText;
	if(!confirm("선택하신  '"+scriptName+"' 을(를) 삭제 하시겠습니까?")) {
		return false;
	}else {
		/* 스크립트 스텝 삭제 API */
		var info = {
			transId: editTransactionId,
			stepPk: stepPk
		};
		
		if(stepPk == null || stepPk == ''){
			scriptGrid.deleteRow(selectRowId);
			showOrHideEdit();
			return;
		}else if(stepPk != null  ){
			var callbackSuccess = function(data){
				scriptGrid.deleteRow(selectRowId);
				showOrHideEdit();
				if(scriptGrid.getRowsNum() > 0){ // step이 1개 이상인 경우에만
					if(selectRowId !=null){
						// 삭제 후 1번째 row를 조회
						scriptGrid.selectRowById(0);
						var nowEditTitle = $("#script_edit_title_area").val(scriptGrid.cells(scriptGrid.getRowId(0), scriptGrid.getColIndexById('scriptName')).getValue());

						var rScriptStepPk = scriptGrid.cells(scriptGrid.getRowId(0),scriptGrid.getColIndexById('rScriptStepPk')).getValue();
						var rScriptStepFk = scriptGrid.cells(scriptGrid.getRowId(0),scriptGrid.getColIndexById('rScriptStepFk')).getValue();
						var scriptName = scriptGrid.cells(scriptGrid.getRowId(0),scriptGrid.getColIndexById('scriptName')).cell.innerText;
						var rProductCode = productGroupInfo.productCode;
		
						var rScriptInfo = {
							rScriptStepPk : rScriptStepPk,
							rScriptStepFk : rScriptStepFk ,	
							scriptName : scriptName,
							rProductCode : rProductCode
						}
						loadScriptDetail(rScriptInfo);	
					}
					
					if(scriptGrid.getRowId(1) == undefined || scriptGrid.getRowId(1) == 'undefined'){
						$("#script_edit_title_area").val("");
						$("#realScriptDetailContentValue").html("");
						var html ='';	
						html += '';
						html += "<div id ='noneScript' class='none_script enable' style='color:#a19a9a; font-size:16px; width:auto; height :auto;'>등록된 스크립트가 없습니다.</div>"
						$("#realScriptDetailContentValue").append(html);	
					}
				}	
			}
			
			var callbackError = function(data){
				if(data.result=='-1001'){
					opener.fromEditToLogin();
				}
			};
			
			deleteStep(info, callbackSuccess , callbackError);
			
		}			
	}
}


// 버튼 비활성
function disable(id){
	if($(id).hasClass("enable")){
		$(id).removeClass("enable");
		$(id).addClass("disable");		
	}	
}

// 버튼활성화
function enable(id){
	if($(id).hasClass("disable")){
		$(id).removeClass("disable");
		$(id).addClass("enable");		
	}
}

// 스크립트 디테일의 내역중 공통문구가 포함되면 편집되지 못하도록 막는 함수
function commonDisable(){
	
	var cnt = $(".script").size();
	for(var i=0; i<cnt; i++){
		if($("#edit_script_btn").hasClass("disable")){
			if($("#genneralCheckCommon"+i).hasClass("common")){
				$(".scriptContext"+i).children().attr("contenteditable",false); 
				$("#typeselect"+i).attr("disabled",true); 
				$("#typeValue"+i).attr("disabled",true); 
				$("#typeText"+i).attr("disabled",true); 
				
				//detail 공통문구 삭제 버튼
				enable("#selectBtnLine"+i);
				//detail 공통문구 삭제,수정 정체 DIV 넓이 수정
				$("#selectBtnLine"+i).css("width","75px");
				//detail 공통문구 삭제,수정 정체 DIV 넓이 수정
				$("#delete_Btn"+i).css("margin-left","25px");
				//detail 공통문구 삭제 버튼
				enable("#delete_Btn"+i);
				//detail 공통문구 수정 버튼
				disable("#modify_Btn"+i);
			
			}	
		}
	}
 }

// 편집 모드 진입
function initEditMode(){	
	//detail  수정 삭제
	var cnt = $(".script").size();
	for(var i=0; i<cnt; i++){
		//detail 삭제 버튼		
		enable("#delete_Btn"+i);
		//detail  수정 버튼
		enable("#modify_Btn"+i);
		//detail 수정,삭제 감싼 전체DIV
		enable("#selectBtnLine"+i);	
		
		$(".scriptContext"+i).children().attr("contenteditable",true); 
	}
	//detail text수정 할 수 있도록 
	$(".type_select").removeAttr("disabled"); 
	$(".type_value").removeAttr("disabled"); 
	$(".type_Text").removeAttr("disabled"); 
	
	// ELT 상품인경우 , 새로운 스텝 추가,삭제 버튼 감춤
//		if(productGroupInfo.eltYN.toLowerCase() == "y"){
//		$(".scGirdStepOneDe").remove();
//		$(".scGirdStepTwoDe").remove();
//	}
	
	//공통문구일 시 수정하지 못하도록
	commonDisable();
}

// 수정 팝업 화면에서 저장 누른 후 적용일 선택 -> commit api 연동
function showCalendar(){
	/* 적용일 선택 div 팝업 생성 */
	var closeSvg = recseeResourcePath + "/images/project/icon/wooribank/modal_close.svg";
	var html = '<div id="saveApplyDatePopup">'
				+ '<div id="saveApplyDateHeader">'
					+ '<span id="applyDateTitle">적용할 날짜를 지정해 주세요</span>'
					+ '<a id="datePopupCloseBtn" style="background:url(' + closeSvg + ') center/cover no-repeat"></a>'
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
					+'<button id="dateSaveBtn" class="date-btn-primary">저장</button>'
					+'<button id="dateNoSaveBtn" class="date-btn-grey">취소</button>'
				+ '</div>'
			+ '</div>';
	$('body').append("<div id='dateBox'></div>");
	$("#dateBox").append(html);
	$("#applyDate").width($("#applyDateTd").width());
	layer_popup("#saveApplyDatePopup");
	
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

// 엔터키로 중복 팝업 방지
function blockEnter(event){
	if(event.keyCode === 13) event.preventDefault();
}

// 팝업 닫은 후 팝업변수 초기화
function makePopupVarNull(){
	editPopup = null;
}

// 스텝이 하나도 없을 때는 수정 disable
function showOrHideEdit(){
	
    if($.contains(document.body, document.getElementById("nodeval"))){
        $(".script_detail_wrap").show();
        $("#hideEditDiv").remove();
        $("#scriptContentBtnWrap").removeClass("disable");
        $("#scriptContentBtnWrap").addClass("enable");
    }else{
        $(".script_detail_wrap").hide();
        var greyBox = '<div id="hideEditDiv">'
        				+ '<div id="greyArea"></div>'
        			+ '</div>';
        var rollbackForNothing = '<div id="rollbackForNothing" class="transactionButtons">'
									+ '<button id="rollbackNothingBtn" class="edit_btn rollback_btn">취소</button>'
								+ '</div>';
        var transactionForNothing = '<div id="transactionForNothing" class="transactionButtons">'
        							+ '<button class="edit_btn rollback_btn">취소</button>'
        							+ '<button class="edit_btn commit_btn">결재 의뢰</button>'
        						+ '</div>';
        $(".script_step_wrap").after(greyBox);
        $("#hideEditDiv").attr("style", "width:100%; height:100%");
        $("#greyArea").attr("style", "width:100%; height:95%; background-color:#efefef");
        
        if(editInitStepNum==0){ // 초기값: 미등록 스크립트
        	$("#greyArea").after(rollbackForNothing);
        }else{ // 초기값: 등록 스크립트
        	$("#greyArea").after(transactionForNothing);
        }
    }
} 

//스크립트  단계별 제목 수정 시 입력값이 있을경우에만 저장버튼 활성화
function showSaveButton(){
	
	var beforeText = scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('scriptName')).getValue(); 
	var afterText =$("#script_edit_title_area").val();
	if(beforeText != afterText){
		enable(".add_Step_Btn");
	}else if(beforeText == afterText){
		disable(".add_Step_Btn");
	}
}

//셀렉트 박스 수정시 수정버튼 활성화
function showModifyButton(id,idx){
	if(id.indexOf("new") == -1){
		enable("#modify_Btn"+idx);
	}else{
		enable("#modify_new_contents"+idx);		
	}
}

function disableModifyButton(id,idx){
	if(id.indexOf("new") == -1){
		disable("#modify_Btn"+idx);
	}else{
		disable("#modify_new_contents"+idx);		
	}
}

function attachDetailContentEvent(index,detailInfo){
	
	$("#typeselect"+index).on("change",function() {	
		$(this).css("border","1.5px solid red");
		detectContentModified(index,detailInfo);
	});
	
	$("#typeValue"+index).on("change",function() {
		$(this).css("border","1.5px solid red");
		detectContentModified(index,detailInfo);
	});
	
	$("#typeText"+index).on("keyup change paste",function() {
		$(this).css("border","1.5px solid red");
		detectContentModified(index,detailInfo);
	});

	$(".scriptContext"+index).on("keyup change paste","#scriptDetailText"+index,function() {
		$("#script_textAtrea"+index).attr("style", "border: 1.5px solid red !important");
		detectContentModified(index,detailInfo);
	});
	
}

function detectContentModified(index,detailInfo){
	
	var orgIfCase = detailInfo.rScriptDetailIfCase;
	orgIfCase = orgIfCase == undefined || orgIfCase == null ? "" : orgIfCase;
	
	var orgIfCaseCode = detailInfo.rScriptDetailIfCaseCode;
	orgIfCaseCode = orgIfCaseCode == undefined || orgIfCaseCode == null || orgIfCase == 'N'? "" : orgIfCaseCode;
	
	var orgifCaseDetail = detailInfo.rScriptDetailIfCaseDetail;
	orgifCaseDetail = orgifCaseDetail == undefined || orgifCaseDetail == null || orgIfCase == 'N'? "" : orgifCaseDetail;
	
	var orgIfCaseDetailCode = detailInfo.rScriptDetailIfCaseDetailCode;
	orgIfCaseDetailCode = orgIfCaseDetailCode == undefined || orgIfCaseDetailCode == null || orgIfCase == 'N'? "" : orgIfCaseDetailCode;
	
	var orgDetailText = detailInfo.rScriptDetailText;
	orgDetailText = orgDetailText == undefined || orgDetailText == null ? "" : orgDetailText;
	orgDetailText = orgDetailText.replace(/\r\n/g, "\n"); // 개행문자 \r\n을 \n으로 치환

	
	var firstSelectValue = $("#typeselect"+index+" option:selected").val(); 
	firstSelectValue = firstSelectValue == undefined || firstSelectValue == null ? "" : firstSelectValue;
	
	var secondSelectValue = $("#typeValue"+index+" option:selected").val();
	secondSelectValue = secondSelectValue == undefined || secondSelectValue == null ? "" : secondSelectValue;
	
	var productTextValue =$("#typeText"+index).val();
	productTextValue = productTextValue == undefined || productTextValue == null ? "" : productTextValue;
	
	var detailTextValue = $("#scriptDetailText"+index).text();
	detailTextValue = detailTextValue == undefined || detailTextValue == null ? "" : detailTextValue;

	
	// 변경이 없는 경우
	if( firstSelectValue == orgIfCaseCode 
		&& secondSelectValue == orgIfCaseDetailCode 
		&& detailTextValue == orgDetailText 
		&& productTextValue == orgifCaseDetail ){
		disable("#modify_Btn"+index);
		returnOriginCss(index);
	} else {		
		enable("#modify_Btn"+index);
	}
}

function refreshScriptDetail(){
	var rScriptStepPk = scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('rScriptStepPk')).getValue();
	var rScriptStepFk = scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('rScriptStepFk')).getValue();
	var scriptName = scriptGrid.cells(scriptGrid.getSelectedRowId(),scriptGrid.getColIndexById('scriptName')).cell.innerText;
	var rProductCode = productGroupInfo.productCode;

	var rScriptInfo = {
		rScriptStepPk : rScriptStepPk,
		rScriptStepFk : rScriptStepFk ,	
		scriptName : scriptName,
		rProductCode : rProductCode
	}
	loadScriptDetail(rScriptInfo);
}

// productInfo의  rsEltYN 값이 Y인 경우 스텝 및 디테일 추가+삭제 비활성화
function setVisibleScriptDetailAddButtons(rProdutAttributes,elfYn){
	if(rProdutAttributes.length != '0'){
		if(elfYn.toLowerCase() == 'Y'){
			$("#scriptContentBtnWrap").remove();
		for(var i=0; i< $(".selectBtnLine").length; i++){
			$("#delete_Btn"+i).remove();
		}
	}
		
		$('select').attr("disabled", "true");
		$('.type_Text').attr("disabled", "true");
	}else{
		$("#scriptContentBtnWrap").removeClass("disable");
	}
}


function returnOriginCss(index){
	$("#typeselect"+index).css("border","1px solid #dddddd");
	$("#typeValue"+index).css("border","1px solid #dddddd");
	$("#typeText"+index).css("border","1px solid #dddddd");
	$("#script_textAtrea"+index).attr("style", "border: 1px solid #dddddd !important");
}

//상품코드 옆 물음표 클릭 시 목록 팝업 표출
function showGroupList(obj){
	var pos = $("#showGroupList").position();
	var closeImg = recseeResourcePath + "/images/project/icon/wooribank/icon_btn_exit_gray.png";
	var labelImg = recseeResourcePath + "/images/project/icon/wooribank/obj_record_abled.png";
	var html = '<div id="groupListPopup">'
				+ '<div id="groupListCloseHeader"><span id="groupListTitle">상품목록</span><a id="groupListPopupCloseBtn" style="background:url(' + closeImg + ') center/cover no-repeat" onclick="removeGroupListPopup()"></a></div>'
				+ '<div id="groupListContentWrap">';
	if( productGroupInfo.groupProductList!=null && productGroupInfo.groupProductList!=[] ){
		for(var i = 0; i < productGroupInfo.groupProductList.length; i++){
			var rsGroupProductList =  productGroupInfo.groupProductList[i];
			html += "<div class='flexList'><span class='groupProductListLabel' style='background:url(" + labelImg + ") center/cover no-repeat'></span><span style='margin:5px;  '>" + rsGroupProductList.rsproductcode + "</span></div>";			
			html += "<div class='flexListName'><span style='margin:0px 5px 5px 20px; text-align : start; width :max-content; '>" + rsGroupProductList.rsproductname + "</span></div>";					
		}
			html += '</div></div>';
		$('body').append("<div id='groupListBox'></div>");
		$("#groupListBox").append(html);
		$("#groupListPopup").css("height", "auto");
		
		if($("#groupListPopup").height()>210){
			$("#groupListPopup").height("210px");
			$("#groupListContentWrap").height("185px");
			if(pos != undefined){//pos = {top: 50, left: 1114.5}
				$("#groupListPopup").css("top", (pos.top + 115) + "px");
				$("#groupListPopup").css("left", (pos.left + 120) + "px");
			}
		}else{
			$("#groupListPopup").height("170px");
			$("#groupListContentWrap").height("145px");
			if(pos != undefined){
				$("#groupListPopup").css("top", (pos.top + 115) + "px");
				$("#groupListPopup").css("left", (pos.left + 120) + "px");;
			}
		}

		layer_popup("#groupListPopup");
		
		/* 중복 팝업 방지 */
		document.addEventListener("keydown", blockEnter, true);
	}
	
}

// 중복 팝업 방지
function blockEnter(event){
	if(event.keyCode === 13) event.preventDefault();
}

// 상품목록 팝업창 닫기 (상단 x버튼)
function removeGroupListPopup(){
	$('#groupListPopup').remove();
	$('#groupListBox').remove();
	$('#mask').remove();
	$(this).unbind("click", arguments.callee);
	
	/* 엔터키 입력 금지 이벤트 해제 */
	document.removeEventListener("keydown", blockEnter, true);
}

//window resize event
$(window).on('resize', function(){

	// 상품코드목록 팝업 리사이징
	var timer = null;
	timer = setTimeout(function(){
		var pos = $("#showGroupList").position();
		if(pos != undefined){
			if($("#groupListPopup").height()==130){
				$("#groupListPopup").css("top", (pos.top+85) + "px");
			}else{
				$("#groupListPopup").css("top", (pos.top+75) + "px");
			}
			$("#groupListPopup").css("left", (pos.left+100) + "px");
		}
	}, 100);
	
	// 스텝목록 width 수정
	if( window.innerWidth < 1800 && navigator.userAgent.search('Trident') !=-1 ){ // 해상도 width 1800 미만 IE
		$(".script_step_wrap").width("28%");
	}
	
});

// ELT 상품의 각 조건내용을 UI로 표출할 function 
function displayEltProductCondition(ind,rProdutAttributes){
	var html ='';
	html += '' ;
	if(rProdutAttributes.length != '0'){
	var resultLenght = rProdutAttributes.length;		
	for(var i= 0; i<resultLenght; i++){
		var mod = i % 4;
		if(mod == 0){
			html += "<div class='eltProductValue' style='background-color : #5a9fbd !important;'>";			
		}else if(mod == 1){
			html += "<div class='eltProductValue' style='background-color :#769f46 !important;'>";				
		}else if(mod  == 2){
			html += "<div class='eltProductValue' style='background-color : #a693e5 !important;'>";			
		}else if(mod  == 3){
			html += "<div class='eltProductValue' style='background-color : #6a71b7 !important;'>";					
		}
		html += 	"<div id='eltProductConditionName"+ind+"' class='eltProductConditionName'>"+rProdutAttributes[i]+"</div>";
		html += "</div>";		
	}
		return html
	}else{
		return false;
	}
}

// 상품 정보란에 표출 될 가변값 연동 시간 안내 문구
function winiInfo(){
	var html =''
		html += "<div id='winiInfoTitle' class='winiInfoText' >[WINI 펀드정보와 스크립트 가변값 연동시간]</div>";
		html += "<div id='winiInfoValOne' class='winiInfoText'>1.일괄 연동(상품속성 전체) : 매 영업일 새벽1시</div>";
		html += "<div id='winiInfoValTwo'class='winiInfoText'>2.시간 중 변경분 연동(일부 항목) : 8시 30분 ~16시 30분(30분단위로 연동)</div>";
		html += "<div id='winiInfoValThree' class='winiInfoText'>→영업점 신규가 17시까지만 가능하므로 마지막 연동시간 16시 30분</div>";
		
		$("#winiValue").append(html);
}