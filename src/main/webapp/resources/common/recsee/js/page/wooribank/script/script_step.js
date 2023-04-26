window.onload = function() {
	// 그리드 생성
	ssInit();

	// 미리듣기
	$(document).on( "click", "#audioPlay", function() {

		var container = $(this).parent().parent();
		var detailPk = container.attr('stepdetailpk');
		var reqData = null;

		for (var i = 0; i < resultData.length; i++) {
			var detailInfo = resultData[i];
			if (detailInfo.rScriptDetailPk == detailPk) {

				var variableText = changeVariableValToName( detailInfo.rScriptDetailText, detailInfo.rScriptDetailIfCaseDetail );
				var scriptType = detailInfo.rScriptDetailText.indexOf('설명') !== -1 ? 'P' : 'N';
				reqData = {
					'rsScriptDetailType' : detailInfo.rScriptDetailType,
					'rsScriptDetailText' : variableText,
					'rsScriptDetailType' : scriptType,
					'rsProductPk' : detailInfo.rScriptStepFk
				}

				break;
			}
		}

		if (reqData == null)
			return;

		var url = contextPath + "/wooribank/script/api/step/" + detailInfo.rScriptStepPk + "/detail/listen";

		var callbackSuccess = function(jRes) {
			if (jRes.success == "Y") {
				var path = jRes.resData.filePath;
				if (path != null) {
					if(environment != 'prod') {
						path = path.replace("https","http");
					}
					audioTag = document.getElementById("audioPlayer");
					// 테스트서버
					audioTag.src = path;
					audioTag.play();

				}
			}
		}

		var callbackError = function(res) {
			console.log(res);
		}

		ajaxPost(url, reqData, 'application/json', callbackSuccess, callbackError);
	});

	// 스크립트목록 - 목록 수정 버튼 클릭
	$("#edit_script_btn").click(function() {
		/* 스크립트 편집 트랜잭션 시작 API */
		var oldTransactionId = '';
		var rsProductListPk = $("#productInfo").attr("productPk");
		var callbackSuccess = function(data) {
			transactionId = data.resData.transactionId;
			var productPk = productJson.rsProductListPk;
			openEdit(transactionId, productPk);
		};
		var callbackError = function(data) {

		};
		startTransaction(rsProductListPk, oldTransactionId, callbackSuccess, callbackError);

	});

	// 목록보기 버튼
	$("#back_script_btn").click(function() {
		if ($(".search_box").hasClass("disable") == true) {
			$(".contents").removeClass("enable");
			$(".contents").addClass("disable");
			$(".search_box").addClass("enable");
			$(".search_box").removeClass("disable");
		}

		// 오디오 스탑
		if (audioTag != undefined && audioTag != null
				&& false == audioTag.paused
				&& audioTag.currentTime > 0) {
			audioTag.pause();
			audioTag.currentTime = 0;
		}

		// 제목, 스크립트 내용 지우기
		$("#script_edit_title_area").html("");
		$('#realScriptDetailContentValue').html('<div id="noneScript" class="noneScript">등록된 스크립트가 없습니다.</div>');

		productJson = '';
		
		$("#scriptCopyBox").css({
	        'margin-top' : 0,
	        'margin-left' : 0
	    });
	});

	// ??
	$('#audioPlayer').bind('ended', function() {
		if (playCount == ttsResult.resultPath.length) {
			audioTag.pause();
		}
		playCount++;
		audioTag.src = ttsResult[playCount].resultPath;

		if (playCount < ttsResult.resultPath.length) {
			audioTag.pause();
		}
	})

	// 검색 엔터 처리
	$('#searchScript').keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});

	$('#popup_searchScript').keyup(function(e) {
		if (e.keyCode == 13)
			$("#common_script_searchBt").trigger("click");
	});

}// window.onload 마감

// 화면 로드 시 그리드 그리는 함수
function ssInit() {

	// 그리드에 기능 등록
	ssGridFunction();

	// 플레이어
	audioTag = document.getElementById("audioPlayer");

}

// 사용 안 되고 있으나 유틸리티성 함수로 미삭제, 추후 필요 시 수정 및 사용
function isNull(param) {
	if (param == null)
		return true;
	if (param == undefined)
		return true;
	if (param == "null" || param == "Null" || param == "NULL")
		return true;
	if (param.length == 0)
		return true;
	if (typeof (param) == 'string') {
		if (param.trim() == "")
			return true;
	}
	return false;
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
		$(".rec_script_title").text($("#" + id).text());
		$(".rec_script").text(obj[0].script);

		var isChecked = $("#" + scriptId + " .chkImg").prop('checked');
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
			progress.on();
			var dataStr = {
				"scriptCode" : scriptId,
				"productType" : $("#custProduct option:selected").val()
			}

			$.ajax({
				url : contextPath + "/playScriptTTS.do",
				data : dataStr,
				type : "POST",
				dataType : "json",
				timeout : 5000,
				async : false,
				success : function(jRes) {
					if (jRes.success == "Y") {
						var result = jRes.resData.listenUrl;
						scriptwavesurfer.load(result);
					} else {
						scriptwavesurfer.empty();
						$("#total_time").text("00:00:00");
						$("#now_time").text("00:00:00");
						alert("스크립트 오디오 파일을 찾을 수 없습니다.");
					}
					progress.off();
				},
				error : function(error) {
					progress.off();
					scriptwavesurfer.empty();
					$("#total_time").text("00:00:00");
					$("#now_time").text("00:00:00");
					alert("스크립트 오디오 파일을 찾을 수 없습니다.");
				}
			});
		}
	}
}

function getListFilter(jsonArray, key, value) {
	return jsonArray.filter(function(object) {
		return object[key] === value;
	});
}

// 트리 그리드
function scCreateScriptStepGrid(objGrid, objPaging, url, strData,
		excelFileName, initPageRow, hiddenColumn, showColumn) {
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.enableTooltips("true,false,false,false,false,false"); // [수정] 표시 안 보이게 처리
	objGrid.init();

	objGrid.attachEvent("onXLS", function() {
		progress.on();
	});

	// 파싱완료
	objGrid.attachEvent("onXLE", function(grid_obj, count) {
		progress.off();
		// 권한 구분

		var productType = productJson.rsProductType == null ? "" : productJson.rsProductType;
		
		// 상품부서인지 체크
		var isProductPart = false; 
		var productCode = "";
		if(accessLevel.length > 1 && accessLevel.charAt(0) == 'P') {
			isProductPart = true;
			productCode = accessLevel.charAt(1);
		} 
		
		// 스크립트수정+복사 기능은 편집권한 있거나 , 해당 상품의 소속부서 권한을 가진 경우에만.
		var editable = isProductPart ? productCode == productType : writeYn == "Y";
		
		if ( editable ) {
			$("#edit_script_btn").removeClass("disable");
			$("#edit_script_btn").addClass("enable");
		} else {
			$("#edit_script_btn").removeClass("enable");
			$("#edit_script_btn").addClass("disable");
		}
		
		if (objGrid.getRowsNum() == 0) {
			// step이 하나도 없을 때는 디테일 조회 미실행, 다운로드 기능 숨김
			$(".downloadBtn").hide();
			$("#copy_script_btn").removeClass("enable");
			$("#copy_script_btn").addClass("disable");
		} else {
			$(".downloadBtn").show();
			if( editable ) {
				$("#copy_script_btn").removeClass("disable");
				$("#copy_script_btn").addClass("enable");
			}
			var rScriptStepFk = grid_obj.cells(grid_obj.getRowId(0),
					grid_obj.getColIndexById('rScriptStepFk')).getValue();
			var rScriptStepPk = grid_obj.cells(grid_obj.getRowId(0),
					grid_obj.getColIndexById('rScriptStepPk')).getValue();
			var scriptName = grid_obj.cells(grid_obj.getRowId(0), grid_obj
					.getColIndexById('scriptName')).cell.innerText;
			var rProductCode = productJson.rsProductCode;

			var scriptInfo = {
				rScriptStepFk : rScriptStepFk,
				rScriptStepPk : rScriptStepPk,
				scriptName : scriptName,
				rProductCode : rProductCode

			}

			loadScriptDetail(scriptInfo);
		}
	});

	return objGrid;
}

// 스크립트 수정 창에 표출
function scriptLoadGrid(key) {
	var key = key;
	// new api
	scriptGrid.clearAndLoad(contextPath + "/wooribank/script/api/step/list/" + key + "/xml", function() {
		scriptGrid.expandAll();
	});
}

// Grid에 기능 붙이는 함수
function ssGridFunction() {

	searchScriptGrid.attachEvent("onRowDblClicked", function(id, ind) {
		if (excelYn == "N")
			return;// 엑셀조회 권한 없는 경우 상세조회 불가
		
		var flag = eltCheck(id);
		if(flag=='Y'){
			return false;
		}
		
		if ($(".search_box").hasClass("enable") == true) {
			$(".contents").removeClass("disable");
			$(".contents").addClass("enable");
			$(".search_box").addClass("disable");
			$(".search_box").removeClass("enable");
		}

		/* 스크립트 상품 정보 API */
		var stepFk = searchScriptGrid.getSelectedRowId();

		var callbackSuccess = function(data) {

			// resData를 변수에 담아놓기 (추후 다른 기능에 사용)
			productJson = data.resData.productInfo;

			// 그룹상품 목록 표시용 span 만들기
			var questionMark = '<span id="showGroupList" class="questionMark"></span>';

			// 상품정보 표시
			$('#product_name_value').html(productJson.rsProductName);
			$('#product_type_value').html(productJson.rsProductTypeName);
			if (productJson.rsGroupCode != null) { // 그룹상품인 경우,
				// 그룹상품코드 표시
				// + 상품코드에 외
				// n건 표시 +
				// 물음표 클릭 시
				// 목록 레이어 팝업
				$('#repre_product_code_value').html(productJson.rsGroupCode);
				if (productJson.rsGroupProductList != null && productJson.rsGroupProductList != []) {
					$('#product_code_value').html(
						productJson.rsProductCode
								+ "<span style='font-size:11px; font-weight:bold; margin-left:5px;'>외<span style='margin-right:5px'></span>"
								+ productJson.rsGroupProductList.length
								+ "건" + "</span>"
								+ questionMark );
					
					$("#product_code_value").attr("style", "cursor: pointer");
					var product_code_value = document.getElementById("product_code_value");
					product_code_value.addEventListener("click", showGroupList, true);
					
				} else {
					$('#product_code_value').html(productJson.rsProductCode);

					// 그룹상품 아닐 때 팝업 발생 방지
					$("#product_code_value").attr("style","cursor: default");
					var product_code_value = document.getElementById("product_code_value");
					product_code_value.removeEventListener("click", showGroupList, true);
				}
			} else {
				$('#repre_product_code_value').html(productJson.rsProductCode);
				$('#product_code_value').html(productJson.rsProductCode);

				// 그룹상품 아닐 때 팝업 발생 방지
				$("#product_code_value").attr("style","cursor: default");
				var product_code_value = document.getElementById("product_code_value");
				product_code_value.removeEventListener("click",	showGroupList, true);
			}

			// transaction에 필요한 rsProductListPk 세팅
			$('#productInfo').attr("productPk",	productJson.rsScriptStepFk);

			$("#winiValue").text("")
			// 상품 정보란에 표출 될 가변값 연동 시간 안내 문구
			winiInfo();

			// 트리그리드
			scriptGrid = scCreateScriptStepGrid("scriptGrid","recMemoAddButton", "scriptGrid", "", "", 20,	[], []);
			// 트리그리드 이미지 없애기
			scriptGrid.setImageSize(1, 1);
			scriptGrid.enableAutoWidth(true);

			// 전체듣기 초기화
			allListenArray = [];
			// 스크립트 그리드 클릭
			scriptGridAttchEvent();
			// 스크립트 그리드 리로드(clearAndLoad)
			scriptLoadGrid(productJson.rsProductListPk);

		};

		var callbackError = function(data) {
			alert("상품 정보를 조회하지 못했습니다.");
		};
		getProductInfo(stepFk, callbackSuccess, callbackError);

		// 자동완성기능 미리 호출
		var rScriptStepFk = id;
		var rProductType = searchScriptGrid.cells(id,searchScriptGrid.getColIndexById('rsScriptType')).getValue(); // 선택한 로우 가지고 오기

	});

}

function scriptGridRowClickHandler(id, ind) {
	// 오디오 스탑
	if (audioTag != undefined && audioTag != null && false == audioTag.paused
			&& audioTag.currentTime > 0) {
		audioTag.pause();
		audioTag.currentTime = 0;

	}

	var rScriptStepPk = scriptGrid.cells(id,
			scriptGrid.getColIndexById('rScriptStepPk')).getValue();
	var rScriptStepFk = scriptGrid.cells(id,
			scriptGrid.getColIndexById('rScriptStepFk')).getValue();
	var scriptName = scriptGrid.cells(id, scriptGrid
			.getColIndexById('scriptName')).cell.innerText;
	var rProductCode = searchScriptGrid.cells(searchScriptGrid.getSelectedId(),
			searchScriptGrid.getColIndexById('rsProductCode')).getValue();

	var rScriptInfo = {
		rScriptStepPk : rScriptStepPk,
		rScriptStepFk : rScriptStepFk,
		scriptName : scriptName,
		rProductCode : rProductCode
	}
	if (ind == 5) {
		return;
	}
	if (rScriptStepPk != null && rScriptStepPk != '') {
		loadScriptDetail(rScriptInfo);
	}
	$(".second_right_box").addClass("disable");
	$(".first_right_box").addClass("enable");
	incres = 0;

}

// 스크립트 그리드 클릭 시 동작
function scriptGridAttchEvent() {
	scriptGrid.attachEvent("onRowSelect", function(id, ind) {
		scriptGridRowClickHandler(id, ind);
	});

	scriptGrid.attachEvent("onRowDblClicked", function() {
		scriptGrid.editStop();
	});
}

// 스크립트 그리드 데이터 받아와서 뿌려주기 시작
function loadScriptDetail(scriptInfo) {

	var isDblDirect = (scriptInfo.rScriptStepFk == null) ? true : false;
	var name = scriptInfo.scriptName;

	/* 스크립트 스텝 디테일 조회 API */
	var stepPk = scriptInfo.rScriptStepPk;
	var stepFk = scriptInfo.rScriptStepFk;
	var productPk = searchScriptGrid.cells(searchScriptGrid.getSelectedId(),
			searchScriptGrid.getColIndexById('rsProductPk')).getValue();
	var productCode = scriptInfo.rProductCode;
	var editMode = isEditMode == false ? "N" : "Y";

	$("#productInfo").attr("stepPk", stepPk);
	$("#productInfo").attr("stepFk", stepFk);
	$("#productInfo").attr("productCode", productCode);

	// div 초기화
	$("#script_edit_title_area").html("");
	$('#realScriptDetailContentValue').html("");

	var info = {
		stepPk : stepPk,
		productPk : productPk,
		productCode : productCode
	};

	var callbackSuccess = function(jRes) {

		$("#script_edit_title_area").html(name);

		// 선택한 로우 가지고 오기
		var audio = document.getElementById("audioPlayer");
		var rProductCode = searchScriptGrid.cells(
				searchScriptGrid.getSelectedId(),
				searchScriptGrid.getColIndexById('rsProductCode')).getValue();
		var rScriptStepFk = searchScriptGrid.cells(
				searchScriptGrid.getSelectedId(),
				searchScriptGrid.getColIndexById('rsScriptStepFk')).getValue();
		var rProductType = searchScriptGrid.cells(
				searchScriptGrid.getSelectedId(),
				searchScriptGrid.getColIndexById('rsScriptType')).getValue();

		resultData = jRes.resData.scriptDetailList;
		if (resultData.length > 0) {
			for (var i = 0; i < resultData.length; i++) {
				scriptDetailInfoList = resultData[i];
				displayScriptDetailBlock(resultData[i], i);
			}

		}
		if (scriptGrid.getSelectedId() == null) {
			scriptGrid.selectRow(0); // 최초 진입 시(사용자 선택 행이 없을 시) 첫 번째 행 선택
		}

		if ($("#realScriptDetailContentValue").html() == ''
				|| $("#realScriptDetailContentValue").html() == null) {
			var html = "<div id ='noneScript' class='none_script' style='color:#a19a9ab8; font-size:16px; width:auto; height :auto;'>등록된 스크립트가 없습니다.</div>";
			$("#realScriptDetailContentValue").append(html);
		}
		progress.off();
	};

	var callbackError = function(data) {
		progress.off();
	};

	getStepDetail(info, callbackSuccess, callbackError);
	progress.on();
}

// 스크립트 디테일 형태 그려주기
function displayScriptDetailBlock(scrtiptDetailInfo, ind) {
	var scriptDetailValue;
	if (scrtiptDetailInfo != null) {
		scriptDetailValue = 1;
	} else {
		scriptDetailValue = 0;
	}
	var rProductClass = scrtiptDetailInfo.rProductClass;
	var rScriptCommonConfirm = scrtiptDetailInfo.rScriptCommonConfirm;
	var rScriptCommonPk = scrtiptDetailInfo.rScriptCommonPk;
	var rScriptCommonType = scrtiptDetailInfo.rScriptCommonType;
	var commonType = scrtiptDetailInfo.rScriptDetailComKind;
	var rScriptDetailConfirm = scrtiptDetailInfo.rScriptDetailConfirm;
	var rScriptDetailConfirmDate = scrtiptDetailInfo.rScriptDetailConfirmDate;
	var rScriptDetailConfirmUser = scrtiptDetailInfo.rScriptDetailConfirmUser;
	var rScriptDetailCreateDate = scrtiptDetailInfo.rScriptDetailCreateDate;
	var rScriptDetailCreateUser = scrtiptDetailInfo.rScriptDetailCreateUser;
	var rScriptIfCase = scrtiptDetailInfo.rScriptDetailIfCase;
	var rScriptDetailIfCaseCode = scrtiptDetailInfo.rScriptDetailIfCaseCode;
	var rScriptDetailIfCaseDetail = scrtiptDetailInfo.rScriptDetailIfCaseDetail;
	var rScriptDetailIfCaseDetailCode = scrtiptDetailInfo.rScriptDetailIfCaseDetailCode;
	var rScriptDetailOrder = scrtiptDetailInfo.rScriptDetailOrder;
	var rScriptDetailPk = scrtiptDetailInfo.rScriptDetailPk;
	var rScriptDetailRealtimeTTS = scrtiptDetailInfo.rScriptDetailRealtimeTTS;
	var rScriptDetailReservDate = scrtiptDetailInfo.rScriptDetailReservDate;
	var rScriptDetailText = scrtiptDetailInfo.rScriptDetailText;
	var rScriptDetailType = scrtiptDetailInfo.rScriptDetailType;
	var rScriptDetailUdateDate = scrtiptDetailInfo.rScriptDetailUdateDate;
	var rScriptDetailUpdateUser = scrtiptDetailInfo.rScriptDetailUpdateUser;
	var rScriptStepFk = scrtiptDetailInfo.rScriptStepFk;
	var rScriptStepPk = scrtiptDetailInfo.rScriptStepPk;
	var rUseYn = scrtiptDetailInfo.rUseYn;
	var rScriptDetailComDesc = scrtiptDetailInfo.rScriptCommonDesc;
	var rProdutAttributes = scrtiptDetailInfo.rProdutAttributes;
	
	var disabledVal = "";
	if (scrtiptDetailInfo.rScriptDetailComKind == 'Y') {
		disabledVal = "disabled";
	}
	var html = '';
	html += '';
	html += "<div id='script" + ind + "' class='script' stepFK='"
			+ rScriptStepFk + "' stepPk='" + rScriptStepPk + "' stepDetailPk='"
			+ rScriptDetailPk + "' realTTS='" + rScriptDetailRealtimeTTS + "'>";
	html += "<div id='script_BtnLine" + ind
			+ "' class='BtnLine' style='height : 30px'>";
	// [공통문구]**리딩 혹은 **리딩 표출되는 function
	html += detailTextCommonCheck(commonType, rScriptDetailType,
			writeCommonText, recValue, ind, rScriptCommonPk);
	html += "<div id='selectBtnLine"
			+ ind
			+ "' class='selectBtnLine disable ' style='height:30px; width:75px; float:right;' >";
	html += "<div  id='delete_Btn"
			+ ind
			+ "' class='delete_Btn disable ' style='background-color: white; border-radius : 4px; width:22px; display: inline-block; cursor: pointer; float:right; border:1px solid #CED3D9;'  value='삭제' >"
			+ "<img id='delete_content_Btn"
			+ ind
			+ "' class='delete_contentBtn ' src='"
			+ recseeResourcePath
			+ "/images/project/icon/wooribank/ic_trash.svg' style= 'width: 22px;   height: 20px;  text-align: center;'/>"
			+ "</div>";
	html += "<div id='modify_Btn"
			+ ind
			+ "' class='modify_Btn disable ' style='background-color: white; margin-right :0px; width:20px; display: inline-block; cursor: pointer;  float:right;border-radius : 4px;border:1px solid #CED3D9;'  value='수정' >"
			+ "<img id='modify_content_Btn"
			+ ind
			+ "' class='modify_contentBtn' src='"
			+ recseeResourcePath
			+ "/images/project/icon/wooribank/icon_btn_pen_gray.png' style= 'width: 20px;   height: 20px;  text-align: center;'/>"
			+ "</div>";
	html += "</div>"
	html += "<div id='selectLine' style='height:30px; width:350px; float:right;'>"
	html += "<select  id='typeselect"
			+ ind
			+ "'  class='type_select'  style='background-color: white; width:150px;' "
			+ " name='조건추가'  >";
	html += scriptSelectBox(ind, rScriptIfCase, rScriptDetailIfCaseCode,
			rScriptDetailIfCaseDetail);
	html += "</select>";
	html += "<select  id ='typeValue"
			+ ind
			+ "' class='type_value'  style='background-color: white; display:none; width:177px;' >";
	html += "</select>";
	html += "<input type='text' id='typeText"
			+ ind
			+ "' class='type_Text' style='height : 15px; width:177px; margin-right:10px;' contenteditable='false' />";
	html += "</div>"
	html += "<input type='button' id='audioPlay' class='allListen"
			+ ind
			+ " disable' style='float:right;width:65px;text-indent:0px !important; cursor:pointer; margin-right:10px;'margin-top:3px; value='미리듣기'/>";
	html += "</div>"

	html += "<div id='script_textAtrea' class='textAtrea''>";
	// 공통문구일 시 공통문구 설명 표출
	if (commonType == "Y") {
		html += "<div id='comExplain"
				+ ind
				+ "' class='comExplain' style='height : 15px; color :#628095; font-weight : bold; padding:5px; font-size:12px; margin-top:3px; ' >"
				+ rScriptDetailComDesc + "</div>"
	}
	var text = scrtiptDetailInfo.rScriptDetailText;
	// 가변값 빨간색으로 표시
	if (text != null) {
		text = text.replace(/{/gi, "<span style='color:red;'>{").replace(/}/gi,
				"}</span>");
	}
	html += "<div id='scriptLineContext' class='Script_line scriptContext"
			+ ind + " script_input' style='margin-top:3px;'>";
	html += "<pre id ='scriptDetailText" + ind
			+ "' contenteditable='false' style='height : auto; padding:5px;'>"
			+ text + "</pre>";
	html += "</div>";
	html += "</div>";

	// 오토리스트
	html += "<div id='list' class='panel panel-default hide autoList' autoList   scriptIndex=''>";
	html += "<div class='list-group'></div>";
	html += "</div>";
	// ELT상품일떄만 보여질 상품 조건

	if (rProdutAttributes.length != '0') {
		html += "<div id='eltProductCondition" + ind
				+ "' class='eltProductCondition ' scriptindex='" + ind + "' >";
		html += displayEltProductCondition(ind, rProdutAttributes);
		html += "</div>";
	}

	$("#realScriptDetailContentValue").append(html);

	setScriptDetailCaseType(rScriptDetailIfCaseDetail, ind,
			rScriptDetailIfCaseDetailCode);

	// 첫번째 박스 select했을 시 유형에 맞게 다음 div형태값 변경해주는 event
	$(".type_select").attr("disabled", true);
	$(".type_value").attr("disabled", true);
	$(".type_Text").attr("disabled", true);
	// TTS리딩, 적합성보고서 일 경우에 미리듣기 버튼 활성화
	if (scrtiptDetailInfo.rScriptDetailType == "T"
			|| scrtiptDetailInfo.rScriptDetailType == "R") {
		enable(".allListen" + ind);
		allListenArray.push(text);
	}
	// rProdutAttributes가 null이어서 false값을 받을 시 안보이도록
	if ($("#eltProductCondition" + ind).text() == 'false') {
		$("#eltProductCondition" + ind).css("display", "none");
	}

};

// selectbox 값 채움
function scriptSelectBox(ind, rScriptIfCase, rScriptDetailIfCaseCode,
		rScriptDetailIfCaseDetail) {
	var option = "";

	// ifCase가 조건이 없을경우 조건을 선택해주세요 멘트를 옵션처리에 넣어주기
	if (rScriptIfCase == 'N' || rScriptDetailIfCaseCode == null) {
		option = "<option  value='' selected='selected'>" + "조건 없음"
				+ "</option>";
	}
	for (var i = 0; i < detailCase.length; i++) {
		var selected = "";
		if (rScriptIfCase == 'Y'
				&& rScriptDetailIfCaseCode == detailCase[i].code) {
			selected = " selected";
		}
		option += "<option value='" + detailCase[i].code + "' " + selected
				+ ">" + detailCase[i].name + "</option>";
	}
	return option;
};

// 첫번쨰 SelectBox에 선택된 값을 기준으로 rScriptDetailIfCaseDetail의 컬럼의 값을 표출하기 위한 현재상태 저장
// function
function setScriptDetailCaseType(rScriptDetailIfCaseDetail, ind,
		rScriptDetailIfCaseDetailCode) {
	// 선택한 값
	var check = $("#typeselect" + ind + " option:selected").val();
	for (var i = 0; i < detailCase.length; i++) {
		var selectedType = detailCase[i].code;
		if (check == selectedType) {
			setScriptDetailCaseValue(detailCase[i],
					rScriptDetailIfCaseDetailCode, rScriptDetailIfCaseDetail,
					ind);
		}
	}
}

// 현재 선택된 첫번쨰 SelectBox의 데이터 내용에 따라 옆의 테그에 데이터 넣어주기
function setScriptDetailCaseValue(caseItem, rScriptDetailIfCaseDetailCode,
		rScriptDetailIfCaseDetail, ind) {

	$("#typeValue" + ind).empty();
	$("#typeValue_new" + ind).empty();

	// 선택한 케이스의 value 를 가져와서 보여준다.
	if (caseItem.values == null || caseItem.values.length == 0) {
		$("#typeText" + ind).attr('value', rScriptDetailIfCaseDetail)
		$("#typeValue" + ind).hide();
		$("#typeText" + ind).show();
	} else {
		$("#typeValue" + ind).show();
		$("#typeText" + ind).hide();

		for (var i = 0; i < caseItem.values.length; i++) {
			var caseDetailCode = rScriptDetailIfCaseDetailCode == null ? ""
					: rScriptDetailIfCaseDetailCode;
			var selected = caseItem.values[i].code == caseDetailCode ? " selected"
					: "";
			var subitem = caseItem.values[i];
			option = "<option  value='" + subitem.code + "' " + selected + " >"
					+ subitem.name + "</option>";
			$("#typeValue" + ind).append(option)

		}
	}

}

/**
 * 공통문구 용인지 체크하는 로직
 * 
 * @param commonType :
 *            script_detail 테이블의 rScriptDetailComKind(공통문구 사용여부)
 * @param rScriptDetailType :
 *            script_detail 테이블의 rScriptDetailType
 * @param commonCode :
 *            선택된 상품의 공통문구에 대한 코드
 * @param commonName :
 *            선택된 상품의 공통문구에 대한 이름
 * @param code :
 *            선택된 rScriptDetailType의 코드
 * @param name :
 *            선택된 rScriptDetailType의 이름
 * @returns
 */
function detailTextCommonCheck(commonType, rScriptDetailType, writeCommonText,
		recValue, ind, commonFk) {
	var html = "";
	for (var i = 0; i < recValue.length; i++) {
		var scriptTypeVal = recValue[i].code
		if (scriptTypeVal == rScriptDetailType) {
			var scriptTypeName = recValue[i].name;
			if (commonType == 'Y') {
				html = "<div id='genneralCheckCommon"
						+ ind
						+ "' class ='genneralCheck common' value ='"
						+ scriptTypeVal
						+ "' detailComKind='"
						+ commonType
						+ "' detailComFk='"
						+ commonFk
						+ "' style='font-weight:bold; float:left; width : 160px; height : 15px; padding :5px;  background-color:#083378; color: white; text-align:left; margin-left:3px;'>"
						+ writeCommonText + " " + scriptTypeName + "</div>"
			} else if (commonType == 'N') {
				if (scriptTypeVal == 'T') {
					html = "<div id='genneralCheck"
							+ ind
							+ "' class ='genneralCheck' value ='"
							+ scriptTypeVal
							+ "' detailComKind='"
							+ commonType
							+ "' detailComFk='"
							+ commonFk
							+ "' style='font-weight:bold; float:left; padding :5px; width : 160px; height : 15px; background-color:#BABABA; color: #fff; text-align: left; margin-left:3px;'>"
							+ scriptTypeName + "</div>"

				} else if (scriptTypeVal == 'G') {
					html = "<div id='genneralCheck"
							+ ind
							+ "' class ='genneralCheck' value ='"
							+ scriptTypeVal
							+ "' detailComKind='"
							+ commonType
							+ "' detailComFk='"
							+ commonFk
							+ "' style='font-weight:bold; float:left; padding :5px; width : 160px;  height : 15px; background-color:#e7b356; color: #fff; text-align: left; margin-left:3px;'>"
							+ scriptTypeName + "</div>"

				} else if (scriptTypeVal == 'S') {
					html = "<div id='genneralCheck"
							+ ind
							+ "' class ='genneralCheck' value ='"
							+ scriptTypeVal
							+ "' detailComKind='"
							+ commonType
							+ "' detailComFk='"
							+ commonFk
							+ "' style='font-weight:bold; float:left; padding :5px; width : 160px; height : 15px; background-color:#e39696; color: #fff; text-align: left; margin-left:3px;'>"
							+ scriptTypeName + "</div>"

				} else if (scriptTypeVal == 'R') {
					html = "<div id='genneralCheck"
							+ ind
							+ "' class ='genneralCheck' value ='"
							+ scriptTypeVal
							+ "' detailComKind='"
							+ commonType
							+ "' detailComFk='"
							+ commonFk
							+ "' style='font-weight:bold; float:left; padding :5px; width : 160px; height : 15px; background-color:#4f6cbf; color: #fafbfd; text-align: left; margin-left:3px;'>"
							+ scriptTypeName + "</div>"
				}
			}
			return html;
		}
	}
};

// 상단(상품정보) 채우기
function productLoad() {
	// 상품코드
	$("#product_code_value").html(
			searchScriptGrid.cells(searchScriptGrid.getSelectedId(),
					searchScriptGrid.getColIndexById('rsProductCode'))
					.getValue());
	// 상품명
	$("#product_name_value").html(
			searchScriptGrid.cells(searchScriptGrid.getSelectedId(),
					searchScriptGrid.getColIndexById('rsProductName'))
					.getValue());
	// 상품유형
	$("#product_type_value").html(
			searchScriptGrid.cells(searchScriptGrid.getSelectedId(),
					searchScriptGrid.getColIndexById('rsScriptType'))
					.getValue());
};

// 가변값 관련
function serachingVariableText(text, caseDetail) {
	var searchArr = [];

	if (!isNaN(caseDetail)) { // caseDetail 이 숫자면 그 값만 비교
		for (var i = 0; i < scriptVariableObject.length; i++) {
			var name = scriptVariableObject[i].rProductValueName;
			var code = scriptVariableObject[i].rProductCode;
			if (code == caseDetail) {
				if (text.indexOf("{" + name + "}") > -1) {
					searchArr.push(scriptVariableObject[i]);
				}
			}

		}
	} else { // 아닐 시 일반비교
		for (var i = 0; i < scriptVariableObject.length; i++) {
			var name = scriptVariableObject[i].rProductValueName;
			var code = scriptVariableObject[i].rProductCode;
			if (code == productCode) {
				if (text.indexOf("{" + name + "}") > -1) {
					searchArr.push(scriptVariableObject[i]);
				}
			}
		}
	}

	return searchArr;
}

function changeVariableValToName(text, caseDetail) {
	var searchArr = serachingVariableText(text, caseDetail);
	for (var i = 0; i < searchArr.length; i++) {
		var name = searchArr[i].rProductValueName;
		var value = searchArr[i].rProductValueVal;
		text = text.replace("{" + name + "}", value);
	}
	return text;
}

// 텍스트 부분에 가변값 항목이 아닌 {}가 있으면 replace로 변환하는 함수
function bracketRemove(text) {
	if (ifdetail == 'N' || ifdetail == null) {
		// 앞에 괄호가 있을떄
		if (text.indexOf("{") > -1) {
			text.repalce(/{/gi, "")
		}
		// 뒤에 괄호가 있을때
		if (text.indexOf("}") > -1) {
			text.replace(/}/gi, "")
		}
		return text;
	} else {
		return false;
	}

}

// 버튼 비활성화
function disable(id) {
	if ($(id).hasClass("enable")) {
		$(id).removeClass("enable");
		$(id).addClass("disable");
	}
}

// 버튼 활성화
function enable(id) {
	if ($(id).hasClass("disable")) {
		$(id).removeClass("disable");
		$(id).addClass("enable");
	}
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

// 물음표 아이콘 mouseover 시 도움말 표시
function showTitleHelp(obj) {
	var rect = obj.getBoundingClientRect();
	var x = rect.left - 380;
	var y = rect.top - 145;
	if (!titleHelpMessage) {
		titleHelpMessage = new dhtmlXPopup({
			mode : "right"
		});
		var html = "「스크립트 목록 제목」은 상품의 그룹별로 변경됩니다." + "<br>"
				+ "( 1개의 「스크립트 목록 제목」 변경 시 全펀드 일괄 변경 아님! )";
		titleHelpMessage.attachHTML(html);
	}
	titleHelpMessage.show(x, y, 400, 300);
}

// 물음표 아이콘 mouseout 시 도움말 사라짐
function hideTitleHelp() {
	if (titleHelpMessage)
		titleHelpMessage.hide();
}

// 상품코드 옆 물음표 클릭 시 목록 팝업 표출
function showGroupList(obj) {
	var pos = $("#showGroupList").position();
	var closeImg = recseeResourcePath
			+ "/images/project/icon/wooribank/icon_btn_exit_gray.png";
	var labelImg = recseeResourcePath
			+ "/images/project/icon/wooribank/obj_record_abled.png";
	var html = '<div id="groupListPopup">'
			+ '<div id="groupListCloseHeader"><span id="groupListTitle">상품목록</span><a id="groupListPopupCloseBtn" style="background:url('
			+ closeImg
			+ ') center/cover no-repeat" onclick="removeGroupListPopup()"></a></div>'
			+ '<div id="groupListContentWrap">';
	if (productJson.rsGroupProductList != null
			&& productJson.rsGroupProductList != []) {
		for (var i = 0; i < productJson.rsGroupProductList.length; i++) {
			var rsGroupProductList = productJson.rsGroupProductList[i];
			html += "<div class='flexList'><span class='groupProductListLabel' style='background:url("
					+ labelImg
					+ ") center/cover no-repeat'></span><span style='margin:5px;  '>"
					+ rsGroupProductList.rsproductcode + "</span></div>";
			html += "<div class='flexListName'><span style='margin:0px 5px 5px 20px; text-align : start; width :max-content; '>"
					+ rsGroupProductList.rsproductname + "</span></div>";
		}
		html += '</div></div>';

		$("#groupListBox").append(html);
		$("#groupListPopup").css("height", "auto");

		if ($("#groupListPopup").height() > 170) {
			$("#groupListPopup").height("170px");
			$("#groupListContentWrap").height("145px");
			if (pos != undefined) {// pos = {top: 125, left: 1049.796875
				$("#groupListPopup").css("top", (pos.top + 115) + "px");
				$("#groupListPopup").css("left", (pos.left + 121) + "px");
			}
		} else {
			$("#groupListContentWrap").css("border", "none");
			$("#groupListContentWrap").css("height", "auto");
			if (pos != undefined) {
				$("#groupListPopup").css("top", (pos.top + 95) + "px");
				$("#groupListPopup").css("left", (pos.left) + "px");
			}
		}

		layer_popup("#groupListPopup");

		/* 중복 팝업 방지 */
		document.addEventListener("keydown", blockEnter, true);
	}
}

// 중복 팝업 방지
function blockEnter(event) {
	if (event.keyCode === 13)
		event.preventDefault();
}

// 상품목록 팝업창 닫기 (상단 x버튼)
function removeGroupListPopup() {
	$('#groupListPopup').remove();
	$('#mask').remove();
	$(this).unbind("click", arguments.callee);

	/* 엔터키 입력 금지 이벤트 해제 */
	document.removeEventListener("keydown", blockEnter, true);
}

// window resize event
$(window).on('resize', function() {
	var timer = null;
	timer = setTimeout(function() {
		// 상품목록 팝업 위치 조정
		var pos = $("#showGroupList").position();
		if (pos != undefined) {
			if ($("#groupListPopup").height() == 130) {
				$("#groupListPopup").css("top", (pos.top + 85) + "px");
			} else {
				$("#groupListPopup").css("top", (pos.top + 75) + "px");
			}
			$("#groupListPopup").css("left", (pos.left + 100) + "px");
		}
		// 그리드 사이즈 조정

	}, 100);
});

// 현재 버전 스크립트 엑셀 다운로드
function downloadExcel() {
	window.open(contextPath + "/wooribank/script/download/product/"
			+ productJson.rsProductListPk + "/script/latest?fileType=excel");
}

// 현재 버전 스크립트 PDF 다운로드
function downloadPdf() {
	window.open(contextPath + "/wooribank/script/download/product/"
			+ productJson.rsProductListPk + "/script/latest?fileType=pdf");
}

// ELT 상품의 각 조건내용을 UI로 표출할 function
function displayEltProductCondition(ind, rProdutAttributes) {
	var html = '';
	html += '';
	if (rProdutAttributes.length != '0') {
		var resultLenght = rProdutAttributes.length;
		for (var i = 0; i < resultLenght; i++) {
			var mod = i % 4;
			if (mod == 0) {
				html += "<div class='eltProductValue' style='background-color : #5a9fbd !important;'>";
			} else if (mod == 1) {
				html += "<div class='eltProductValue' style='background-color :#769f46 !important;'>";
			} else if (mod == 2) {
				html += "<div class='eltProductValue' style='background-color : #a693e5 !important;'>";
			} else if (mod == 3) {
				html += "<div class='eltProductValue' style='background-color : #6a71b7 !important;'>";
			}
			html += "<div id='eltProductConditionName" + ind
					+ "' class='eltProductConditionName'>"
					+ rProdutAttributes[i] + "</div>";
			html += "</div>";
		}
		return html
	} else {
		return false;
	}
}

// 상품 정보란에 표출 될 가변값 연동 시간 안내 문구
function winiInfo() {
	var html = ''
	html += "<div id='winiInfoTitle' class='winiInfoText' >[WINI 펀드정보와 스크립트 가변값 연동시간]</div>";
	html += "<div id='winiInfoValOne' class='winiInfoText'>1.일괄 연동(상품속성 전체) : 매 영업일 새벽1시</div>";
	html += "<div id='winiInfoValTwo'class='winiInfoText'>2.시간 중 변경분 연동(일부 항목) : 8시 30분 ~16시 30분(30분단위로 연동)</div>";
	html += "<div id='winiInfoValThree' class='winiInfoText'>→영업점 신규가 17시까지만 가능하므로 마지막 연동시간 16시 30분</div>";

	$("#winiValue").append(html);
}

// 팝업 열린 상태에서 세션 만료 -1001 코드 응답 시, 팝업창 닫고 팝업오프너(현재 창)에서 로그인화면으로 이동
function fromEditToLogin() {
	if(editPopup!=null){
		editPopup.close();
		location.replace(contextPath + "/?secret=true");
	}
}

function eltCheck(id){
	var reqData = {
					"productListPk" : id
				  };
	var flag = 'N';
	$.ajax({
		url:contextPath+"/wooribank/script/api/product/eltcheck",
		data: reqData,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
				console.log(jRes.resData.result)
				flag = jRes.resData.result;
			}
	
	});
	
	return flag;

}
