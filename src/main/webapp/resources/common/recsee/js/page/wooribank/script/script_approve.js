window.onload = function() {
	// 그리드 생성
	saInit();

	// 버튼 클릭 관련 함수
	initButtonFunctions();

	// 최초 진입 시 화면 사이즈에 따라 그리드 사이즈 설정
	var restHeight = $('.main_header').innerHeight()
			+ $('.main_lnb').innerHeight() + $('.filter').innerHeight()
			+ $('#ApprovalPaging').innerHeight();
	var gridHeight = window.innerHeight - restHeight;
	$('#scriptApproveListGrid').height(gridHeight);

	// 검색 엔터 처리
	$('#searchKeyword').keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});

	// 검색어 입력됐을 때만 x 표시 보이기
	$("#searchKeyword").on("input", function() {
		if ($("#searchKeyword").val() != "") {
			$("#keywordRemoveImg").show();
		} else {
			$("#keywordRemoveImg").hide();
		}
	});

}// window.onload 마감

// 화면 이동 시 리사이즈
$(window)
		.on(
				'resize',
				function() {

					var restHeight = $('.main_header').innerHeight()
							+ $('.main_lnb').innerHeight()
							+ $('.filter').innerHeight()
							+ $('#ApprovalPaging').innerHeight();
					var gridHeight = window.innerHeight - restHeight;
					$('#scriptApproveListGrid').height(gridHeight);

					// 목록보기 버튼 우측 margin 조정
					var agent = navigator.userAgent.toLowerCase(); // 브라우저 판단
																	// -- 단,
																	// IE11 이상은
																	// userAgent
																	// 정보를 가지고
																	// 있지 않으므로
																	// appName으로
																	// 판단하는 조건도
																	// 아래에 추가
					if (window.innerWidth > 1800) { // 해상도 width 1800 초과
						if ((navigator.appName == 'Netscape' && navigator.userAgent
								.search('Trident') != -1)
								|| (agent.indexOf("msie") != -1)) { // IE
							$("#scriptApprovalList").attr("style",
									"margin-right:22px");
							$("#scriptApproveAfterTitle").attr("style",
									"margin-left:23px");
						} else {
							$("#scriptApprovalList").attr("style",
									"margin-right:36px");
							$("#scriptApproveAfterTitle").attr("style",
									"margin-left:24px");
						}
					} else { // 해상도 width 1800 이하
						if ((navigator.appName == 'Netscape' && navigator.userAgent
								.search('Trident') != -1)
								|| (agent.indexOf("msie") != -1)) {
							$("#scriptApprovalList").attr("style",
									"margin-right:11px");
							$("#scriptApproveAfterTitle").attr("style",
									"margin-left:30px");
						} else {
							$("#scriptApprovalList").attr("style",
									"margin-right:24px");
							$("#scriptApproveAfterTitle").attr("style",
									"margin-left:28px");
						}
					}

				});

// 리로드
function approveLoad(isHeader) {
	var resourceType = "xml"; // 추후 xml 또는 json
	var scriptType = $('#scriptType option:selected').val(); // 전체, 상품, 공용
	var approveStatus = $('#approveType option:selected').val(); // 대기, 승인,
																	// 반려
	var startDate = '';
	if ($('#sDate').val() != '')
		startDate = $('#sDate').val();
	var endDate = '';
	if ($('#eDate').val() != '')
		endDate = $('#eDate').val();
	var keyword = $('#searchKeyword').val().trim();
	var data = "scriptType=" + scriptType + "&approverYN=Y&approveStatus="
			+ approveStatus + "&startDate=" + startDate + "&endDate=" + endDate
			+ "&keyword=" + keyword;

	// 리로드 시 헤더도 변경
	if (isHeader) {
		approveGridLoaded = false;
		scriptApproveListGrid = saCreateGrid("scriptApproveListGrid",
				"ApprovalPaging", "wooribank/script/api/approval/list/"
						+ resourceType + "?header=" + isHeader + "&"
						+ encodeURI(data), "", "", 100, [], []);
		scriptApproveListGrid.enableAutoWidth(true);
		// 그리드에 기능 등록
		saGridFunction();
	} else {
		scriptApproveListGrid.clearAndLoad(contextPath
				+ "/wooribank/script/api/approval/list/" + resourceType + "?"
				+ encodeURI(data));
	}

};

// 화면 로드 시 그리드 그리는 함수
function saInit() {

	/* 결재요청 목록 조회 API */
	var resourceType = "xml"; // 추후 xml 또는 json
	var scriptType = "A"; // 초기값=전체 -> 실제 컨트롤러에서는 enum타입에 없으므로 null처리됨
	scriptApproveListGrid = saCreateGrid("scriptApproveListGrid",
			"ApprovalPaging", "wooribank/script/api/approval/list/"
					+ resourceType
					+ "?header=true&approverYN=Y&approveStatus=request", "",
			"", 100, [], []);
	scriptApproveListGrid.enableAutoWidth(true);

	// 그리드에 기능 등록
	saGridFunction();
}
/**
 * Grid 생성 함수
 * 
 * @param objGrid
 *            그리드가 그려질 태그의 id값
 * @param objPaging
 *            페이징이 그려질 태그의 id값
 * @param url
 *            xml 데이터를 가져올 XmlController의 requestMapping 경로
 * @param strData
 *            parameter 정보
 * @returns
 */
function saCreateGrid(objGrid, objPaging, url, strData, excelFileName,
		initPageRow, hiddenColumn, showColumn) {

	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");

	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, initPageRow, 5, objPaging, true);
	objGrid.setPagingWTMode(true, true, true, [ 100, 250, 500 ]);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	// Load Grid
	objGrid.load(contextPath + "/" + url, function() {
		var grid_toolbar = objGrid.aToolBar;
		grid_toolbar.addSpacer("perpagenum");
		grid_toolbar.addText("total", 0,
				'<div style="width: 100%; text-align: center;">'
						+ objGrid.i18n.paging.results + objGrid.getRowsNum()
						+ objGrid.i18n.paging.found + '</div>')
		grid_toolbar.setWidth("total", 80)

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});

		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj, count) {
			console.log('pagechange:' + grid_obj.xmlFileUrl);
		});

		if (objPaging) {
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}

		objGrid.attachEvent("onXLS", function() {
			progress.on();
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj, count) {

			var setResult = '<div style="width: 100%; text-align: center;">'
					+ objGrid.i18n.paging.results + " " + objGrid.getRowsNum()
					+ " " + objGrid.i18n.paging.found + '</div>';
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
		if (!approveGridLoaded) {
			approveGridLoaded = true;
			approveLoad(false);
		}
	});

	return objGrid;
}

// Grid에 기능 붙이는 함수
function saGridFunction() {

	scriptApproveListGrid.attachEvent("onRowDblClicked", function(id, ind) {
		progress.on();
		detailTransactionId = scriptApproveListGrid.getSelectedId();
		applyDate = scriptApproveListGrid.cells(detailTransactionId,
				scriptApproveListGrid.getColIndexById('rsApplyDate'))
				.getValue();
		applyName = scriptApproveListGrid.cells(detailTransactionId,
				scriptApproveListGrid.getColIndexById('rsApplyName'))
				.getValue();
		var ifCommon = scriptApproveListGrid.cells(detailTransactionId,
				scriptApproveListGrid.getColIndexById('rsCommonKind'))
				.getValue();
		var approveStatus = $('#approveType option:selected').val();

		// 상품스크립트인 경우 상품명 표시
		if (ifCommon == "N") {
			scriptType = "P";
			productName = scriptApproveListGrid.cells(detailTransactionId,
					scriptApproveListGrid.getColIndexById('rsScriptTypeDesc'))
					.getValue();
		} else if (ifCommon == "Y") {
			scriptType = "C";
			$("#productName").hide();
		}

		// 결재상태가 대기인 경우만 [결재],[반려] 버튼 표시
		if (approveStatus == "request") {
			$("#scriptApproval").show();
			$("#scriptReturning").show();
			$("#cancelApproval").hide();
		} else {
			$("#scriptApproval").hide();
			$("#scriptReturning").hide();

			// 적용예정일이 오늘 이후인 승인 건은 [승인취소] 버튼 표시
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			if (month < 10)
				month = "0" + month;
			var day = date.getDate();
			if (day < 10)
				day = "0" + day;

			var today = year + month + day;
			var compareDate = "";
			if (applyDate != null & applyDate != "") {
				compareDate = applyDate.replace("-", "").replace("-", "");
			}

			if (approveStatus == "approve" && eval(compareDate) > eval(today)) {
				$("#cancelApproval").show();
			} else { // 즉시적용 및 적용일이 지난 승인건
				$("#cancelApproval").hide();
			}
		}

		/* 결재요청 상세 조회 API */
		var info = {
			detailTransactionId : detailTransactionId,
			scriptType : scriptType
		};

		var callbackSuccess = function(data) {
			progress.off();

			var before = data.resData.compareData.before;
			var after = data.resData.compareData.after;

			if (before == null && after == null) {
				alert("조회할 정보가 없습니다.");
				return;
			}

			$("#scriptApproveBeforeAfter").css("display", "block");
			$("#scriptApproveListWrap").css("display", "none");
			$("#filter").css("display", "none");
			$("#scriptApproveBeforeGrid").html("");
			$("#scriptApproveAfterGrid").html("");

			// before 에 stepList 가 있는지
			var hasStepListInBefore = before != null
					&& before.stepList != undefined;
			// after 에 stepList 가 있는지
			var hasStepListInAfter = after != null
					&& after.stepList != undefined;

			// before,after 둘중 하나라도 stepList 를 포함하고 있으면 상품 스크립트
			if (hasStepListInBefore || hasStepListInAfter) {
				displayProductScriptBlock(before, after);
			} else {
				// 공용 상세
				displayCommonScriptBlock(before, after);
			}
		}

		var callbackError = function(data) {
			console.log("api 연동 실패");
			progress.off();
		};

		getApprovalDetail(info, callbackSuccess, callbackError);

	});

}

// 상품 상세 표시
function displayProductScriptBlock(before, after) {

	// 상품명 표시
	$("#productName").html(productName);
	$("#productName").show();

	// 적용 전
	var beforeDetailMap = [];
	var list = before == null || before.stepList == undefined
			|| before.stepList == null ? [ {
		rsScriptStepName : "",
		detailList : [ {
			rsScriptStepDetailTypeName : null,
			rsScriptDetailText : null
		} ]
	} ] : before.stepList;

	for (var i = 0; i < list.length; i++) {
		var html = '';
		var stepParent = list[i].rsScriptStepParent;
		if (i != list.length - 1) {
			if (stepParent == "0" && list[i + 1].rsScriptStepParent != "0") { // 하위스텝이
																				// 있는
																				// 상위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" style="margin-bottom : 3px !important;">'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<img src="'
						+ recseeResourcePath
						+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
						+ '<div id="StepRealBeforeText" class="step_real_before_text">'
						+ list[i].rsScriptStepName + '</div>' + '</div>';
			} else if (stepParent != "0"
					&& list[i + 1].rsScriptStepParent != "0") { // 다음 스텝이 하위스텝인
																// 하위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" style="margin-bottom : 3px !important;">'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<div id="StepRealBeforeText" class="step_real_before_text" style="margin-left:20px">'
						+ " - "
						+ list[i].rsScriptStepName
						+ '</div>'
						+ '</div>';
			} else if (stepParent != "0"
					&& list[i + 1].rsScriptStepParent == "0") { // 다음 스텝이 상위스텝인
																// 하위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext" >'
						+ '<div id="StepRealBeforeText" class="step_real_before_text" style="margin-left:20px">'
						+ " - "
						+ list[i].rsScriptStepName
						+ '</div>'
						+ '</div>'
			} else { // 하위스텝이 없는 상위스텝
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" >'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<img src="'
						+ recseeResourcePath
						+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
						+ '<div id="StepRealBeforeText" class="step_real_before_text">'
						+ list[i].rsScriptStepName + '</div>' + '</div>';
			}
		} else {
			if (stepParent != "0") {
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap" style="margin-bottom : 3px !important;">'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<div id="StepRealBeforeText" class="step_real_before_text" style="margin-left:20px">'
						+ " - "
						+ list[i].rsScriptStepName
						+ '</div>'
						+ '</div>';
			} else {
				html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
						+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
						+ '<img src="'
						+ recseeResourcePath
						+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
						+ '<div id="StepRealBeforeText" class="step_real_before_text">'
						+ list[i].rsScriptStepName + '</div>' + '</div>';
			}
		}

		var detailList = list[i].detailList == null ? [] : list[i].detailList;

		for (var j = 0; j < detailList.length; j++) {

			beforeDetailMap[detailList[j].rsScriptStepDetailPk] = detailList[j].rsScriptDetailText;

			var type = detailList[j].rsScriptStepDetailTypeName;
			var detailConditionName = detailList[j].rsScriptDetailCaseValue;
			var detailConditionValue = detailList[j].rsScriptDetailCaseDetailValue;
			var eltDetailContion = detailList[j].rsProductAttributesText;

			if (type == null) {
				type = "";
				detailConditionName = "";
				detailConditionValue = "";
				eltDetailContion = "";
			} else {
				// 기본 조건
				if (detailConditionName == null) {
					detailConditionValue = "";
					detailConditionName = "";
				} else {
					detailConditionName = detailList[j].rsScriptDetailCaseValue
							+ '&nbsp' + ':' + '&nbsp';
				}
				// elt조건
				if (eltDetailContion == null) {
					eltDetailContion = "";
				} else {
					eltDetailContion = detailList[j].rsProductAttributesText;
				}
			}

			var text = detailList[j].rsScriptDetailText;

			var isEmptyScript = text == null;

			if (isEmptyScript)
				text = "등록된 스크립트가 없습니다.";

			if (text == '' && stepParent == "0") { // 상위스텝 text가 빈값인 경우
													// detailText 블럭 생성x

			} else if (text != '' && stepParent == "0") {
				html += '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext detailWrap">'
						+ '<div id="commonType" class="beforeDetailType detailType">'
						+ type + '</div>';
			} else if (text != '' && stepParent != "0") {
				html += '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext detailWrap" style="margin-left:20px !important; width:94% !important">'
						+ '<div id="commonType" class="beforeDetailType detailType">'
						+ type + '</div>';
			}
			if (text == "등록된 스크립트가 없습니다.") {
				html += '<div id="textArea" class="textArea beforeDetailText detailText">'
						+ text + '</div>';
			} else if (text != '') {
				html += '<div id="textArea" class="textArea beforeDetailText detailText"><pre>'
						+ text + '</pre></div>';
				if (detailConditionName != "" && eltDetailContion == "") {
					html += beforeDetailCondition(detailConditionName,
							detailConditionValue);
				} else if (eltDetailContion != "" && detailConditionName == "") {
					html += '<div id="detailCondition" class="beforedetailCondition">';
					html += beforeDetailEltCondition(eltDetailContion);
					html += '</div>'
				}
				html += '</div>';
			}
		}

		$("#scriptApproveBeforeGrid").append(html);
		$("#scriptApproveBeforeGridWrap").css("visibility", "");
	}

	list = after == null || after.stepList == undefined
			|| after.stepList == null ? [] : after.stepList;
	// 적용 후
	for (var i = 0; i < list.length; i++) {
		var stepEditType = list[i].rsScriptStepEditType;
		var stepParent = list[i].rsScriptStepParent;
		var html;
		if (stepEditType == "D") {
			stepEditType = "삭제";
			if (i != list.length - 1) {
				if (stepParent == "0" && list[i + 1].rsScriptStepParent != "0") { // 하위스텝이
																					// 있는
																					// 상위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:1.5px dashed red !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				} else if (stepParent != "0"
						&& list[i + 1].rsScriptStepParent != "0") { // 다음 스텝이
																	// 하위스텝인
																	// 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:1.5px dashed red !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else if (stepParent != "0"
						&& list[i + 1].rsScriptStepParent == "0") { // 다음 스텝이
																	// 상위스텝인
																	// 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else { // 하위스텝이 없는 상위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				}
			} else {
				if (stepParent != "0") {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:1.5px dashed red !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				}
			}
		} else if (stepEditType == "C") {
			stepEditType = "신규";
			if (i != list.length - 1) {
				if (stepParent == "0" && list[i + 1].rsScriptStepParent != "0") { // 하위스텝이
																					// 있는
																					// 상위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:2px solid #9fb4e1 !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				} else if (stepParent != "0"
						&& list[i + 1].rsScriptStepParent != "0") { // 다음 스텝이
																	// 하위스텝인
																	// 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:2px solid #9fb4e1 !important; margin-bottom : 3px !important; ">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else if (stepParent != "0"
						&& list[i + 1].rsScriptStepParent == "0") { // 다음 스텝이
																	// 상위스텝인
																	// 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else { // 하위스텝이 없는 상위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				}

			} else {
				if (stepParent != "0") {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="border:2px solid #9fb4e1 !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				}
			}
		} else { // 수정 또는 기존값
			if (i != list.length - 1) {
				if (stepParent == "0" && list[i + 1].rsScriptStepParent != "0") { // 하위스텝이
																					// 있는
																					// 상위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap" style="margin-bottom : 3px !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				} else if (stepParent != "0"
						&& list[i + 1].rsScriptStepParent != "0") { // 다음 스텝이
																	// 하위스텝인
																	// 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step" style="margin-bottom : 3px !important;">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else if (stepParent != "0"
						&& list[i + 1].rsScriptStepParent == "0") { // 다음 스텝이
																	// 상위스텝인
																	// 하위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext child_step">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else { // 하위스텝이 없는 상위스텝
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				}
			} else {
				if (stepParent != "0") {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap child_step">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<div id="StepRealAfterText" class="step_real_After_text" style="margin-left:20px">'
							+ " - "
							+ list[i].rsScriptStepName
							+ '</div>'
							+ '</div>';
				} else {
					html = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
							+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext">'
							+ '<img src="'
							+ recseeResourcePath
							+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
							+ '<div id="StepRealAfterText" class="step_real_After_text">'
							+ list[i].rsScriptStepName + '</div>' + '</div>';
				}
			}

		}

		var detailList = list[i].detailList == null ? [] : list[i].detailList;
		var stepParent = list[i].rsScriptStepParent;

		for (var j = 0; j < detailList.length; j++) {

			var detailEditType = detailList[j].rsScriptStepDetailEditType;
			if (detailEditType == "D") {
				detailEditType = "삭제";
				detailEditTypeShape = '<div id="script_approve_type" class ="scriptApproveType" style=" width:30px; height:20px; line-height:20px; background-color:red; color : white; margin-left:5px; text-align:center; font-weight:bolder;  ">'
						+ detailEditType + '</div>';
			} else if (detailEditType == "C") {
				detailEditType = "신규";
				detailEditTypeShape = '<div id="script_approve_type" class ="scriptApproveType" style="width:30px; height:20px; line-height:20px; background-color:#4c52c3; color : white; margin-left:5px; text-align:center; font-weight:bolder;">'
						+ detailEditType + '</div>';
			} else if (detailEditType == "U") { // 수정 or 정렬변경
				var beforeText = beforeDetailMap[detailList[j].rsScriptStepDetailPk]; // before.stepList[i].detailList[j].rsScriptDetailText;
				if (beforeText == undefined)
					continue;

				var afterText = detailList[j].rsScriptDetailText;

				// 값 비교용 정규식
				var regexBlank = /\s|\r|\n/g; // 띄어쓰기,개행
				var regexWord = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]|\w/g; // 한글 또는 영문,숫자,언더바(\w)
				var regexSpecial = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g; // 특수문자
				// case 1. 변경 있음
				if (beforeText != afterText) {
					// case 1-1. 텍스트 변경 있음 : 한글,영문,숫자,특수문자
					if (JSON.stringify(beforeText.replace(regexBlank, '')
							.match(regexWord)) !== JSON.stringify(afterText
							.replace(regexBlank, '').match(regexWord))
							|| JSON.stringify(beforeText
									.replace(regexBlank, '')
									.match(regexSpecial)) !== JSON
									.stringify(afterText
											.replace(regexBlank, '').match(
													regexSpecial))) {
						detailEditType = "수정";
						detailEditTypeShape = '<div id="script_approve_type" class ="scriptApproveType" style="width:30px;height:20px; line-height:20px;  background-color:#44bb97; color : white; margin-left:5px; text-align:center;font-weight:bolder; padding:0px 2px;">'
								+ detailEditType + '</div>';
					} else { // case 1-2. 텍스트 변경 없음 = 정렬 변경
						detailEditType = "정렬 변경";
						detailEditTypeShape = '<div id="script_approve_type" class ="scriptApproveType" style="width:60px;height:20px; line-height:20px;  background-color:#44bb97; color : white; margin-left:5px; text-align:center;font-weight:bolder; padding:0px 2px;">'
								+ detailEditType + '</div>';
					}
				}// case 2. 변경 없음 - 표시x
			} else {
				detailEditType = "";
				detailEditTypeShape = "";
			}
			var type = detailList[j].rsScriptStepDetailTypeName;
			if (type != null) {
				var detailConditionName = detailList[j].rsScriptDetailCaseValue;
				var detailConditionValue = detailList[j].rsScriptDetailCaseDetailValue;
				var eltDetailContion = detailList[j].rsProductAttributesText;
			}

			if (type == null) {
				type = "";
				detailConditionName = "";
				detailConditionValue = "";
				eltDetailContion = "";
			} else {
				// 기본 조건
				if (detailConditionName == null) {
					detailConditionValue = "";
					detailConditionName = "";
				} else {
					detailConditionName = detailList[j].rsScriptDetailCaseValue
							+ '&nbsp' + ':' + '&nbsp';
				}
				// elt조건
				if (eltDetailContion == null) {
					eltDetailContion = "";
				} else {
					eltDetailContion = detailList[j].rsProductAttributesText;
				}

			}

			var text = detailList[j].rsScriptDetailText;
			if (text == null) {
				text = "등록된 스크립트가 없습니다.";
			}
			if (text == '' && stepParent == "0") {

			} else { // 상위스텝 text가 빈값인 경우 detailText 블럭 생성x
				if (detailEditType == "삭제") {
					html += '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_del">'
							+ detailEditTypeShape
							+ '<div id="commonType" class="afterDetailType detailType">'
							+ type
							+ '</div>'
							+ '<div id="textArea" class="textArea afterDetailText detailText">'
							+ text + '</div>';
					if (detailConditionName != "" && eltDetailContion == "") {
						html += afterDetailCondition(detailConditionName,
								detailConditionValue);
					} else if (eltDetailContion != ""
							&& detailConditionName == "") {
						html += '<div id="afterCondition" class="afterDetailCondition ">';
						html += afterDetailEltCondition(eltDetailContion);
						html += '</div>'
					}
				} else if (detailEditType == "신규") {
					html += '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_new">'
							+ detailEditTypeShape
							+ '<div id="commonType" class="afterDetailType detailType">'
							+ type
							+ '</div>'
							+ '<div id="textArea" class="textArea afterDetailText detailText">'
							+ text + '</div>'
					if (detailConditionName != "" && eltDetailContion == "") {
						html += afterDetailCondition(detailConditionName,
								detailConditionValue);
					} else if (eltDetailContion != ""
							&& detailConditionName == "") {
						html += '<div id="afterCondition" class="afterDetailCondition ">';
						html += afterDetailEltCondition(eltDetailContion);
						html += '</div>'
					}
				} else if (detailEditType == "수정" || detailEditType == "정렬 변경") {
					html += '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap deatil_modi">'
							+ detailEditTypeShape
							+ '<div id="commonType" class="afterDetailType detailType">'
							+ type
							+ '</div>'
							+ '<div id="textArea" class="textArea afterDetailText detailText"><pre>'
							+ text + '</pre></div>'
					if (detailConditionName != "" && eltDetailContion == "") {
						html += afterDetailCondition(detailConditionName,
								detailConditionValue);
					} else if (eltDetailContion != ""
							&& detailConditionName == "") {
						html += '<div id="afterCondition" class="afterDetailCondition ">';
						html += afterDetailEltCondition(eltDetailContion);
						html += '</div>'
					}
				} else {
					html += '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext detailWrap detail_default">'
							+ detailEditTypeShape
							+ '<div id="commonType" class="afterDetailType detailType">'
							+ type + '</div>';
					if (text == "등록된 스크립트가 없습니다.") {
						html += '<div id="textArea" class="textArea beforeDetailText detailText">'
								+ text + '</div>';
					} else {
						html += '<div id="textArea" class="textArea beforeDetailText detailText"><pre>'
								+ text + '</pre></div>';
						if (detailConditionName != "" && eltDetailContion == "") {
							html += afterDetailCondition(detailConditionName,
									detailConditionValue);
						} else if (eltDetailContion != ""
								&& detailConditionName == "") {
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
		$("#scriptApproveAfterGridWrap").css("visibility", "");
		$(".child_step")
				.find(".detail_del")
				.attr(
						"style",
						"margin-left:20px !important; width:94% !important; border:1.5px dashed red !important;");
		$(".child_step")
				.find(".detail_new")
				.attr(
						"style",
						"margin-left:20px !important; width:94% !important; border:1.5px solid #4c52c3 !important;");
		$(".child_step")
				.find(".detail_modi")
				.attr(
						"style",
						"margin-left:20px !important; width:94% !important; border:1.5px solid #44bb97 !important;");
		$(".child_step")
				.find(".detail_default")
				.attr(
						"style",
						"margin-left:20px !important; width:94% !important; border:1px solid #dbdbdb !important;");
	}

	// null값 또는 빈값에 대한 스타일 적용
	for (var i = 0; i < $('.detailWrap').length; i++) {

		if ($(".detailType").eq(i).html() == "") {
			$(".detailType").eq(i).attr("style", "width: 0; height: 0;")
		}

		if ($(".detailText").eq(i).html() == "등록된 스크립트가 없습니다.") {

			if ($(".detailText").eq(i).is(".beforeDetailText")) {
				$(".detailText").eq(i).attr("style",
						"color: #d3d3d3; background-color: #f9f9f9 !important");
				$(".detailWrap").eq(i).attr("style",
						"background-color: #f9f9f9");
			} else {
				$(".detailText").eq(i).attr("style",
						"color: #d3d3d3; background-color: #e4eff1 !important");
				$(".detailWrap").eq(i).attr("style",
						"background-color: #e4eff1");
			}
		}

	}

}

// 공용 상세 표시
function displayCommonScriptBlock(before, after) {

	// DB에서 SSDT -> recValue 사용 (T: TTS리딩 / S: 직원리딩 / G: 상담가이드 / R: 적합성보고서)
	for (var i = 0; i < recValue.length; i++) {
		if (before != null) {
			if (before.rsScriptCommonType == recValue[i].code) {
				before.rsScriptCommonType = recValue[i].name;
			}
		}
		if (after.rsScriptCommonType == recValue[i].code) {
			after.rsScriptCommonType = recValue[i].name;
		}
	}
	// 신규 케이스 구분
	if (before != null) {
		$("#scriptApproveBeforeGridWrap").css("visibility", "");
		var beforeHtml = '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
				+ '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="height :20px;line-height:20px; margin-left:12px; "></div>'
				+ '<div class="common_info">'
				+ '<div class="script_approve_commons">'
				+ '<div class="scriptApproveMenuName">'
				+ "공용문구 이름 "
				+ '</div>'
				+ '<div class="scriptApproveValueName">'
				+ before.rsScriptCommonName
				+ '</div>'
				+ '</div>'
				+ '<div class="script_approve_commons">'
				+ '<div class="scriptApproveMenuName">'
				+ "공용문구 설명"
				+ '</div>'
				+ '<div class="scriptApproveValueName">'
				+ before.rsScriptCommonDesc
				+ '</div>'
				+ '</div>'
				+ '<div class="script_approve_commons">'
				+ '<div class="scriptApproveMenuName">'
				+ "실시간 사용여부"
				+ '</div>'
				+ '<div class="scriptApproveValueName">'
				+ before.rsScriptCommonRealtimeTTS
				+ '</div>'
				+ '</div>'
				+ '</div>'
				+ '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext">'
				+ '<div id="commonType" class="beforeDetailType">'
				+ before.rsScriptCommonType
				+ '</div>'
				+ '<div id="textArea" class="textArea beforeDetailText"><pre>'
				+ before.rsScriptCommonText
				+ '</pre></div>'
				+ '</div>'
				+ '</div>';

		$("#scriptApproveBeforeGrid").append(beforeHtml);
	} else if (after.rsScriptCommonEditType == "C") {
		$("#scriptApproveAfterGrid").html("");
		var emptyHtml = '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
				+ '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="height :20px; line-height:20px;margin-left:12px; "></div>'
				+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext common_info" >'
				+ '<div id="StepRealBeforeText" class="step_real_before_text"  style="color:#a19a9a">'
				+ " 등록된 스크립트 내역이 없습니다."
				+ '<br>'
				+ '</div>'
				+ '</div>'
				+ '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext" style="opacity:0.5;">'
				+ '<div id="commonType" class="beforeDetailType"></div>'
				+ '<div id="textArea" class="textArea beforeDetailText" style="visibility:hidden;">'
				+ after.rsScriptCommonText + '</div>' + '</div>' + '</div>';
		$("#scriptApproveBeforeGrid").append(emptyHtml);
		$("#StepRealBeforeText").removeClass("common_info");
	} else {
		var emptyHtml = '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap">'
				+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext common_info" style="height : 75px;">'
				+ '<div id="StepRealBeforeText" class="step_real_before_text"  style="color:#a19a9a">'
				+ " 등록된 스크립트 내역이 없습니다."
				+ '<br>'
				+ '</div>'
				+ '</div>'
				+ '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext" style="opacity:0.5;">'
				+ '<div id="commonType" class="beforeDetailType"></div>'
				+ '<div id="textArea" class="textArea beforeDetailText" style="visibility:hidden;"></div>'
				+ '</div>' + '</div>';

		$("#scriptApproveBeforeGrid").append(emptyHtml);
		$("#scriptApproveBeforeGridWrap").css("visibility", "hidden");
	}

	// 이름, 설명, 시간
	var commonEditType = after.rsScriptCommonEditType;

	if (commonEditType == "D") {
		commonEditType = "삭제";
		commonEditTypeShape = '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:red; color:white; width:30px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'
				+ commonEditType + '</div>';
	} else if (commonEditType == "C") {
		commonEditType = "신규";
		commonEditTypeShape = '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#4c52c3; color:white; width:30px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'
				+ commonEditType + '</div>';
	} else if (commonEditType == "U") { // 수정 or 정렬변경
		// 값 비교용 정규식
		var regexBlank = /\s|\r|\n/g; // 띄어쓰기,개행
		var regexWord = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]|\w/g; // 한글 또는 영문,숫자,언더바(\w)
		var regexSpecial = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g; // 특수문자
		// case 1. 변경 있음
		if (before.rsScriptCommonName != after.rsScriptCommonName
				|| before.rsScriptCommonDesc != after.rsScriptCommonDesc
				|| before.rsScriptCommonText != after.rsScriptCommonText) {
			// case 1-1. 텍스트 변경 있음 : 한글,영문,숫자,특수문자
			// console.log(JSON.stringify(before.rsScriptCommonText.replace(regexBlank,'').match(regexWord)));
			// console.log(JSON.stringify(before.rsScriptCommonText.replace(regexBlank,'').match(regexSpecial)));
			// console.log(JSON.stringify(after.rsScriptCommonText.replace(regexBlank,'').match(regexWord)));
			// console.log(JSON.stringify(after.rsScriptCommonText.replace(regexBlank,'').match(regexSpecial)));
			if (JSON.stringify(before.rsScriptCommonName
					.replace(regexBlank, '').match(regexWord)) !== JSON
					.stringify(after.rsScriptCommonName.replace(regexBlank, '')
							.match(regexWord))
					|| JSON.stringify(before.rsScriptCommonDesc.replace(
							regexBlank, '').match(regexWord)) !== JSON
							.stringify(after.rsScriptCommonDesc.replace(
									regexBlank, '').match(regexWord))
					|| JSON.stringify(before.rsScriptCommonText.replace(
							regexBlank, '').match(regexWord)) !== JSON
							.stringify(after.rsScriptCommonText.replace(
									regexBlank, '').match(regexWord))
					|| JSON.stringify(before.rsScriptCommonName.replace(
							regexBlank, '').match(regexSpecial)) !== JSON
							.stringify(after.rsScriptCommonName.replace(
									regexBlank, '').match(regexSpecial))
					|| JSON.stringify(before.rsScriptCommonDesc.replace(
							regexBlank, '').match(regexSpecial)) !== JSON
							.stringify(after.rsScriptCommonDesc.replace(
									regexBlank, '').match(regexSpecial))
					|| JSON.stringify(before.rsScriptCommonText.replace(
							regexBlank, '').match(regexSpecial)) !== JSON
							.stringify(after.rsScriptCommonText.replace(
									regexBlank, '').match(regexSpecial))) {
				commonEditType = "수정";
				commonEditTypeShape = '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#44bb97; color:white; width:30px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'
						+ commonEditType + '</div>';
			} else { // case 1-2. 텍스트 변경 없음 = 정렬 변경
				commonEditType = "정렬 변경";
				commonEditTypeShape = '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#44bb97; color:white; width:60px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'
						+ commonEditType + '</div>';
			}
		} else { // case 2. 변경 없음
			commonEditType = "변경 없음"; // 정렬 및 문구 변경 없는 경우 결재의뢰 불가하므로 실제로는 발생하지
										// 않아야 하는 케이스
			commonEditTypeShape = '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="background-color:#44bb97; color:white; width:60px; height:20px; line-height:20px; margin-left:12px; padding:0px 2px; font-weight:bold; text-align:center">'
					+ commonEditType + '</div>';
		}
	} else {
		commonEditType = "";
	}
	var afterHtml = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
			+ commonEditTypeShape
			+ '<div class="common_info">'
			+ '<div class="script_approve_commons">'
			+ '<div class="scriptApproveMenuName">'
			+ "공용문구 이름 "
			+ '</div>'
			+ '<div class="scriptApproveValueName">'
			+ after.rsScriptCommonName
			+ '</div>'
			+ '</div>'
			+ '<div class="script_approve_commons">'
			+ '<div class="scriptApproveMenuName">'
			+ "공용문구 설명"
			+ '</div>'
			+ '<div class="scriptApproveValueName">'
			+ after.rsScriptCommonDesc
			+ '</div>'
			+ '</div>'
			+ '<div class="script_approve_commons">'
			+ '<div class="scriptApproveMenuName">'
			+ "실시간 사용여부"
			+ '</div>'
			+ '<div class="scriptApproveValueName">'
			+ after.rsScriptCommonRealtimeTTS
			+ '</div>'
			+ '</div>'
			+ '</div>'
			+ '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext">'
			+ '<div id="commonType" class="afterDetailType">'
			+ after.rsScriptCommonType
			+ '</div>'
			+ '<div id="textArea" class="textArea afterDetailText"><pre>'
			+ after.rsScriptCommonText + '</pre></div>' + '</div>' + '</div>';

	$("#scriptApproveAfterGrid").append(afterHtml);

	if (after.rsScriptCommonEditType == "D") {
		$("#scriptApproveAfterGrid").html("");
		var afteremptyHtml = '<div id="scriptApproveAfterTextWrap" class="script_approve_After_text_wrap">'
				+ '<div id="script_approve_common_type" class ="scriptApproveCommonType" style="height :20px;line-height:20px; margin-left:12px; "></div>'
				+ '<div id="scriptApproveAfterStepText" class="script_approve_After_steptext common_info">'
				+ '<div id="StepRealAfterText" class="step_real_after_text"  style="color:#a19a9a; margin-left:10px;">'
				+ "삭제"
				+ '<br>'
				+ '</div>'
				+ '</div>'
				+ '<div id="scriptApproveAfterDetailText" class="script_approve_After_detailtext" style="opacity:0.5;">'
				+ '<div id="commonType" class="afterDetailType"></div>'
				+ '<div id="textArea" class="textArea afterDetailText" style="visibility:hidden;">'
				+ before.rsScriptCommonText + '</div>' + '</div>' + '</div>';
		$("#scriptApproveAfterGrid").append(afteremptyHtml);
		$("#StepRealBeforeText").removeClass("common_info");
	} else {
		$("#scriptApproveAfterGridWrap").css("visibility", "");
	}
}

// 승인 전 적용일 재확인
function showApprovalCalendar() {
	/* 적용일 선택 div 팝업 생성 */
	var closeSvg = recseeResourcePath
			+ "/images/project/icon/wooribank/modal_close.svg";
	// popup div에 inline style 생성되어 css 값 미적용되므로 inline style 입력함
	var html = '<div id="saveApplyDatePopup" style="position: absolute; top: 50%; left: 50%; background-color: #ffffff; box-shadow: 0px 0px 10px rgba(0,0,0,0.4); border-radius: 5px; text-align: center; padding-bottom: 5px; z-index: 104;">'
			+ '<div id="saveApplyDateHeader">'
			+ '<span id="applyDateTitle">승인하시겠습니까?</span>'
			+ '<a id="datePopupCloseBtn" style="background:url('
			+ closeSvg
			+ ') center/cover no-repeat"></a>'
			+ '</div>'
			+ '<div id="saveApplyDateBody">'
			+ '<table id="selectDateTable">'
			+ '<tr>'
			+ '<th>적용예정일</th>'
			+ '<td id="applyDateTd" style="text-align:center">'
			+ '<input autocomplete="off" type="text" id="applyDate" class="icon_input_cal" style="cursor:pointer">'// 상신자가
																													// 입력한
																													// 적용예정일
																													// 표시 -
																													// 결재자가
																													// 변경
																													// 가능
			+ '</td>'
			+ '</tr>'
			+ '</table>'
			+ '<div style="text-align:center; padding-bottom:20px"><input type="checkbox" id="nowApply"><label for="nowApply" style="font-size:14px; vertical-align: top; padding:5px">즉시 적용</span></div>'
			+ '</div>'
			+ '<div id="dateButtons">'
			+ '<button id="dateSaveBtn" class="date-btn-primary">저장</button>'
			+ '<button id="dateNoSaveBtn" class="date-btn-grey">취소</button>'
			+ '</div>' + '</div>';
	$("#dateBox").append(html);
	$("#applyDate").width($("#applyDateTd").width());
	layer_popup("#saveApplyDatePopup");

	/* 중복 팝업 방지 */
	document.addEventListener("keydown", function(event) {
		if (event.keyCode === 13)
			event.preventDefault();
	}, true);

	/* 캘린더 생성 */
	dhtmlxCalendarObject.prototype.langData["ko"] = {
		dateformat : '%Y-%m-%d',
		hdrformat : '%Y년 %F',
		monthesFNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월" ],
		monthesSNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월" ],
		daysFNames : [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
		daysSNames : [ "일", "월", "화", "수", "목", "금", "토" ],
		weekStart : 1
	};
	var saveDateCalendar = new dhtmlXCalendarObject("applyDate");
	saveDateCalendar.setSkin("dhx_terrace");
	saveDateCalendar.loadUserLanguage("ko");
	saveDateCalendar.hideTime();
	saveDateCalendar.setWeekStartDay(7);

	var year = new Date().getFullYear(); // 현재 년도

	var month = new Date().getMonth() + 1; // 현재 월
	if (month < 10)
		month = "0" + month;

	var day = new Date().getDate() + 1; // 다음날
	if (day < 10)
		day = "0" + day;

	var tomorrow = new Date(year + "-" + month + "-" + day);
	var tomorrowStr = year + "-" + month + "-" + day;

	if (applyDate != null && applyDate != "" && applyName == "예약 적용") {
		if (new Date(applyDate) < tomorrow) { // 상신자가 선택한 적용일이 내일 날짜 이전인 경우,
												// 적용일 초기값은 내일
			$("#applyDate").val(tomorrowStr);
		} else { // 상신자가 선택한 적용일이 내일 날짜(포함) 이후인 경우, 적용일 초기값은 상신자가 선택한 날짜
			$("#applyDate").val(applyDate);
		}
		var initDate = $("#applyDate").val();
	}

	if (applyName == "즉시 적용") { // 상신자가 즉시적용을 선택한 경우, 즉시적용 체크된 채로 달력 초기화
		$("#nowApply").prop("checked", true);
	}

	saveDateCalendar.setSensitiveRange(tomorrow, null);

	$("#applyDate").on("click", function() {
		saveDateCalendar.show();
	})

	// 즉시적용 체크 시 input 비우기, 즉시적용 해제 시 상신자 입력값을 다시 가져옴
	$("#nowApply").on("click", function() {
		if ($("input:checkbox[id=nowApply]").is(":checked")) {
			$("#applyDate").val("");
			$("#applyDate").css('cursor', 'default');
			$("#applyDate").attr("disabled", "disabled");
			$("#applyDate").addClass("readonly");
			applyType = "immediately"; // 즉시 적용
		} else {
			if (initDate != undefined)
				$("#applyDate").val(initDate);
			$("#applyDate").css('cursor', 'pointer');
			$("#applyDate").removeAttr("disabled");
			$("#applyDate").removeClass("readonly");
			applyType = "reserved"; // 예약일 적용
		}
	});

	// 날짜 선택 시 즉시적용 체크 해제
	saveDateCalendar.attachEvent("onClick", function() {
		$("input:checkbox[id=nowApply]").prop("checked", false);
	});

	/* 오늘 날짜 표시 */
	// 참고: 결재목록 페이지에서만 마지막 날짜가 month_date_dis 클래스로 생성됨(타 페이지에서는 month_dis 클래스),
	// 선택 날짜가 없는 경우 발생하는 것으로 보임
	// 달력이 처음 보일 때 이번달 달력인 경우
	saveDateCalendar
			.attachEvent(
					"onShow",
					function() { // 달력이 표시될 때
						if ($('.dhtmlxcalendar_cell_month_dis').length > 0) {
							if ($('.dhtmlxcalendar_cell_month_date_dis').length > 0) {
								var todayIndex = $('.dhtmlxcalendar_cell_month_date_dis').length - 1;
								var today = $('.dhtmlxcalendar_cell_month_date_dis')[todayIndex].innerText;
								$('.dhtmlxcalendar_cell_month_date_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>"
										+ today + "</div>";
							} else {
								var todayIndex = $('.dhtmlxcalendar_cell_month_dis').length - 1;
								var today = $('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerText;
								$('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>"
										+ today + "</div>";
							}
						}
					});
	// 월 이동하여 이번달 달력을 보는 경우
	saveDateCalendar
			.attachEvent(
					"onArrowClick",
					function() { // 달력에서 월 이동할 때
						if ($('.dhtmlxcalendar_cell_month_dis').length > 0) {
							if ($('.dhtmlxcalendar_cell_month_date_dis').length > 0) {
								var todayIndex = $('.dhtmlxcalendar_cell_month_date_dis').length - 1;
								var today = $('.dhtmlxcalendar_cell_month_date_dis')[todayIndex].innerText;
								$('.dhtmlxcalendar_cell_month_date_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>"
										+ today + "</div>";
							} else {
								var todayIndex = $('.dhtmlxcalendar_cell_month_dis').length - 1;
								var today = $('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerText;
								$('.dhtmlxcalendar_cell_month_dis')[todayIndex].innerHTML = "<div class='dhtmlxcalendar_label' style='color:blue'>"
										+ today + "</div>";
							}
						}
					});

}

function initButtonFunctions() {

	// 검색(조회 버튼)
	$('#searchBtn').click(function() {
		/* 결재요청 목록 조회 API */
		approveLoad(true);
	});

	// 목록보기 버튼
	$("#scriptApprovalList").click(function() {
		$("#scriptApproveListWrap").css("display", "block");
		$("#scriptApproveBeforeAfter").css("display", "none");
		$("#filter").css("display", "block");
	});

	// 결재 버튼
	$(document).on("click", "#scriptApproval", function() {
		showApprovalCalendar();
	});

	// 적용일 팝업창 닫기
	$(document).on("click", '#datePopupCloseBtn', function() {
		layer_popup_close();
		$('#saveApplyDatePopup').remove();
		$('#mask').remove();
		$(this).off("click", arguments.callee);
	});

	// 적용일 저장
	$(document)
			.on(
					"click",
					"#dateSaveBtn",
					function() {
						if ($("#applyDate").val() == ''
								&& $("input:checkbox[id=nowApply]").is(
										":checked") == false) {
							alert("적용일이 지정되지 않았습니다.");
							return false;
						}
						var approveDate = null;
						/* 승인 API */
						if ($("input:checkbox[id=nowApply]").is(":checked") == true) {
							applyType = "immediately"; // 즉시 적용
						} else {
							applyType = "reserved"; // 예약일 적용
							approveDate = $("#applyDate").val();
						}
						var approveType = "approve";
						var info = {
							detailTransactionId : detailTransactionId,
							approveType : approveType,
							scriptType : scriptType,
							approveDate : approveDate,
							applyType : applyType
						};
						if (detailTransactionId != null
								&& detailTransactionId != undefined) {
							$.ajax({
								url : contextPath
										+ "/wooribank/script/api/approval/elt/"
										+ detailTransactionId,
								data : {},
								type : "PUT",
								dataType : "json",
								async : true,
								cache : false,
								success : function(jRes) {
									// DB에 조회한 계정이 있으면
									if (jRes.success == "Y") {
										// 불러온 옵션 추가
									}
								}
							});
						}

						var callbackSuccess = function(data) {
							progress.off();
							alert("승인되었습니다.");
							// 팝업 닫기
							layer_popup_close();
							$('#saveApplyDatePopup').remove();
							$('#mask').remove();
							$(this).off("click", arguments.callee);
							// 승인 후 목록 리로드
							approveLoad(false);
							$("#scriptApproveListWrap").css("display", "block");
							$("#scriptApproveBeforeAfter").css("display",
									"none");
							$("#filter").css("display", "block");
						};
						var callbackError = function(data) {
							progress.off();
							alert("일시적인 오류로 작업이 완료되지 않았습니다. 관리자에게 문의하세요.");
						};
						putApproval(info, callbackSuccess, callbackError); // 상세
																			// 조회 시
																			// 가져온
																			// transactionId
																			// 전역변수
																			// 사용
						progress.on();
					});

	// 적용일 취소(팝업닫기와 동일한 동작)
	$(document).on("click", '#dateNoSaveBtn', function() {
		layer_popup_close();
		$('#saveApplyDatePopup').remove();
		$('#mask').remove();
		$(this).off("click", arguments.callee);
	});

	// 반려 버튼
	$("#scriptReturning").click(function() {
		if (confirm("반려하시겠습니까?")) {
			/* 반려 API */
			var approveType = "reject";
			var info = {
				detailTransactionId : detailTransactionId,
				approveType : approveType,
				scriptType : scriptType,
				approveDate : null,
				applyType : ''
			};
			var callbackSuccess = function(data) {
				alert("반려되었습니다.");
				// 반려 후 목록 리로드
				approveLoad(false);
				$("#scriptApproveListWrap").css("display", "block");
				$("#scriptApproveBeforeAfter").css("display", "none");
				$("#filter").css("display", "block");
			};
			var callbackError = function(data) {
				alert("일시적인 오류로 작업이 완료되지 않았습니다. 관리자에게 문의하세요.");
			};
			putApproval(info, callbackSuccess, callbackError); // 상세 조회 시 가져온
																// transactionId
																// 전역변수 사용
		} else {
			return false;
		}
	});

	// 승인취소 버튼
	$("#cancelApproval").click(function() {
		if (confirm("승인을 취소하시겠습니까?")) {
			/* 반려 API */
			var approveType = "reject";
			var info = {
				detailTransactionId : detailTransactionId,
				approveType : approveType,
				scriptType : scriptType,
				approveDate : null,
				applyType : ''
			};
			var callbackSuccess = function(data) {
				alert("취소되었습니다.");
				// 반려 후 목록 리로드
				approveLoad(false);
				$("#scriptApproveListWrap").css("display", "block");
				$("#scriptApproveBeforeAfter").css("display", "none");
				$("#filter").css("display", "block");
			};
			var callbackError = function(data) {
				alert("일시적인 오류로 작업이 완료되지 않았습니다. 관리자에게 문의하세요.");
			};
			putApproval(info, callbackSuccess, callbackError); // 상세 조회 시 가져온
																// transactionId
																// 전역변수 사용
		} else {
			return false;
		}
	});
}

// 검색용 달력 세팅
function setStartCalendar() {
	/* 캘린더 생성 */
	dhtmlxCalendarObject.prototype.langData["ko"] = {
		dateformat : '%Y-%m-%d',
		hdrformat : '%Y년 %F',
		monthesFNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월" ],
		monthesSNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월" ],
		daysFNames : [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
		daysSNames : [ "일", "월", "화", "수", "목", "금", "토" ],
		weekStart : 1
	};

	var startDateCalendar;
	if (!startDateCalendar) {
		startDateCalendar = new dhtmlXCalendarObject("sDate");
		startDateCalendar.setSkin("dhx_terrace");
		startDateCalendar.loadUserLanguage("ko");
		startDateCalendar.hideTime();
		startDateCalendar.setWeekStartDay(7);
	}

	startDateCalendar.show();

	startDateCalendar.attachEvent("onClick", function() {
		var startDate = new Date($("#sDate").val());
		var endDate = new Date($("#eDate").val());
		if (startDate > endDate) {
			alert("시작일은 종료일 이후일 수 없습니다.");
			$("#sDate").val($("#eDate").val());
		}
	});
}

function setEndCalendar() {
	/* 캘린더 생성 */
	dhtmlxCalendarObject.prototype.langData["ko"] = {
		dateformat : '%Y-%m-%d',
		hdrformat : '%Y년 %F',
		monthesFNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월" ],
		monthesSNames : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월" ],
		daysFNames : [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
		daysSNames : [ "일", "월", "화", "수", "목", "금", "토" ],
		weekStart : 1
	};

	var endDateCalendar;
	if (!endDateCalendar) {
		endDateCalendar = new dhtmlXCalendarObject("eDate");
		endDateCalendar.setSkin("dhx_terrace");
		endDateCalendar.loadUserLanguage("ko");
		endDateCalendar.hideTime();
		endDateCalendar.setWeekStartDay(7);
	}

	endDateCalendar.show();

	endDateCalendar.attachEvent("onClick", function() {
		var startDate = new Date($("#sDate").val());
		var endDate = new Date($("#eDate").val());
		if (startDate > endDate) {
			alert("종료일은 시작일 이전일 수 없습니다.");
			$("#eDate").val($("#sDate").val());
		}
	});
}

// x버튼 클릭 시 현재 입력된 검색어 지움
function removeKeyword() {
	$("#searchKeyword").val("");
	$("#keywordRemoveImg").hide();
	$("#searchKeyword").focus();
}

// 변경 전 상품기본조건
function beforeDetailCondition(detailConNm, detailConVal) {
	var html = '';

	if (detailConNm != "" && detailConNm != null && detailConVal != null) {
		html += '<div id="detailCondition" class="beforedetailCondition detailType">'
				+ '<div id="detailConditionName" class="beforeDetailTypeCon ">'
				+ detailConNm + detailConVal + '</div>' + '</div>';
	}

	return html;
}
// 변경 전 elt상품일경우
function beforeDetailEltCondition(eltCon) {
	var html = '';

	for (var i = 0; i < eltCon.length; i++) {
		html += '<div id="detailConditionName' + [ i ]
				+ '" class="beforeDetailTypeCon ">' + eltCon[i] + '&nbsp'
				+ '</div>'
	}
	return html;
}
// 변경 후 상품기본조건
function afterDetailCondition(detailConNm, detailConVal) {
	var html = '';

	if (detailConNm != "" && detailConNm != null && detailConVal != null) {
		html += '<div id="afterCondition" class="afterDetailCondition detailType">'
				+ '<div id="afterDetailConditionName" class="afterDetailTypeCon ">'
				+ detailConNm + detailConVal + '</div>' + '</div>';
	}

	return html;
}
// 변경후 elt상품일경우
function afterDetailEltCondition(eltCon) {
	var html = '';

	for (var i = 0; i < eltCon.length; i++) {
		html += '<div id="afterDetailConditionName' + [ i ]
				+ '" class="afterDetailTypeCon ">' + eltCon[i] + '&nbsp'
				+ '</div>'
	}
	return html;
}

function displayEltProductCondition(ind, rProdutAttributes) {
	var html = '';
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

function displayHighRankScriptStep(stepName, scriptStepPk) {
	var html = '';
	html += '<div id="scriptApproveBeforeTextWrap" class="script_approve_Before_text_wrap " stepPk="'
			+ scriptStepPk
			+ '">'
			+ '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
			+ '<img src="'
			+ recseeResourcePath
			+ '/images/project/icon/wooribank/select_arr.svg" style= "width: 22px;   height: 20px;  text-align: center;"/>'
			+ '<div id="StepRealBeforeText" class="step_real_before_text">'
			+ stepName + '</div>' + '</div>';

	return html;

}
function displayLowRankScriptStep(stepName) {
	var html = '';
	html += '<div id="scriptApproveBeforeStepText" class="script_approve_Before_steptext">'
			+ '<div id="StepRealBeforeText" class="step_real_before_text"style="margin-left:20px">'
			+ " - " + stepName + '</div>' + '</div>';

	return html;

}

function displayScriptDetail(type, text, stepParent, detailConditionName,
		detailConditionValue, eltDetailContion, parentCode) {
	var html = '';
	if (text == null && stepParent == "0") {// 상위스텝 text가 빈값인 경우 detailText 블럭
											// 생성x

	} else if (text != '' && stepParent == "0") {
		html += '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext detailWrap">'
				+ '<div id="commonType" class="beforeDetailType detailType">'
				+ type + '</div>';
	} else if (text != '' && stepParent != "0") {
		html += '<div id="scriptApproveBeforeDetailText" class="script_approve_Before_detailtext detailWrap" style="margin-left:20px !important; width:94% !important">'
				+ '<div id="commonType" class="beforeDetailType detailType">'
				+ type + '</div>';
	}
	if (text == null) {
		html += '<div id="textArea" class="textArea beforeDetailText detailText">'
				+ "등록된 스크립트가 없습니다." + '</div>';
	} else if (text != '') {
		html += '<div id="textArea" class="textArea beforeDetailText detailText"><pre>'
				+ text + '</pre></div>';
		if (detailConditionName != "" && eltDetailContion == "") {
			html += beforeDetailCondition(detailConditionName,
					detailConditionValue);
		} else if (eltDetailContion != "" && detailConditionName == "") {
			html += '<div id="detailCondition" class="beforedetailCondition">';
			html += beforeDetailEltCondition(eltDetailContion);
			html += '</div>'
		} else if (detailConditionName == "" || eltDetailContion == "") {
			html += '<div id="detailCondition" class="beforedetailCondition " style="display:none !important;"></div>';
		}
		html += '</div>';
	}

	return html;
}
