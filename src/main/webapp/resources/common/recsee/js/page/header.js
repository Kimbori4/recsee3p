var t = 0;
window.sheetList=[];
//평가 시트 그리드 공통
var gridEvaluation;

/** 그리드 그려주는 메서드
 *  objGrid : 생성될 그리드 div 아이디
 *  objPaging : 함께 생성될 페이징
 *  url : 그리드 데이터 url
 *  strData : 초기 조건 로드 필요 할때 넣어줄 조건 데이터 (get 방식)
 *  excelFileName : 엑셀 다운시 표출 될 파일 명
 *  initPageRow : 한 페이지당 표출 될 데이터 갯수
 *  hiddenColumn : 엑셀 다운시 숨겨졌다 다시 표시되어야 할 컬럼 넘버 (배열)
 *  showColum : 엑셀 다운시 표시 되었다가 다시 숨겨져야 할 컬럼 넘버 (배열)
 *  */
/** 그리드 그려주는 메서드 */
/*function createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");

	// 페이징 유무에 따른 실행
	if(objPaging){
		objGrid.enablePaging(true,initPageRow, 5, objPaging, true);
		objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
		objGrid.setPagingSkin("toolbar","dhx_web");
	}

	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function(){
		ui_controller();
	});
	return objGrid;
}*/

/**
 * 평가 팝업을 연다
 * @param o
 * o에 파라미터 형태로 넘어오는 것
 * 	1. 평가 결과 페이지에서 팝업
 *	evalCode
 * 	sheetCode
 *
 * 	2. 녹취이력 조회 페이지에서 팝업
 * 	userId
 * @returns
 */

//프로그레스 처리
var progress = {
	on: function(mask) {
		if($("#progress").length < 1) {
			$("html").append("<div id='progress'>")
		}
		$("#progress").css("opacity", "0");
		$("#progress").css("height", "100%");
		HoldOn.open({
				theme: "sk-circle"
			,	backgroundColor: "#FFFFFF"
		});
	}, off: function(mask) {
		HoldOn.close();
		$("#progress").remove();
	}
}

function evaluationSheetLoad() {
//	var strData = "?" + $.param({depth: depth, sheetCode: sheetCode})
	gridEvaluation = createGrid("gridEvaluation", null, "evaluation/resultGrid", "", "", 20, [],[]);


	var baseAjaxOption = {
			type: "GET",
			//data:
			dataType: "json",
			beforeSend: function() {},
			complete: function() {},
			error: function() {},
	}

	var getCurrentScore = function() {
		var currentScore = 0;
		var $parent = $("#gridEvaluation");
		$parent.find(".onoffswitch-checkbox:radio:checked").each(function(){
			// 체크된 radio의 다음 tag(label)에서 .onoffswitch-switch의 텍스트를 찾아 그 값을 숫자로 변환한다.
			var score = parseInt($(this).find("+ > span.onoffswitch-switch").text(), 10)||0;
			currentScore += score;
		});
		return currentScore;
	}
	var changeCurrentScore = (function() {
		var currentScore = getCurrentScore();
		$("input[name=currentScore]").val(currentScore);
	});
	var getRadioItems = (function() {
		var rv = {};
		$(".onoffswitch-checkbox:radio").each(function() {
			var name = ($(this).attr("name")||"").replace(/^radio/, "");
			// 값이 있으면 체크 중단
			if(rv[name]) 	return true;
			var itemCode;
			var $target;
			if(($target = $(".onoffswitch-checkbox:radio[name='radio"+name+"']:checked"))[0]) {
				// 체크된 값이 있으면
				itemCode = ($target.attr("id")||"").replace(/^radio/, "");
			}
			rv[name] = itemCode;
		});
		return rv;
	});
	/**
	 * 평가 저장(업데이트) 관련
	 */
	var evalUpdateAjaxOption = $.extend({}, baseAjaxOption);
	evalUpdateAjaxOption.url = contextPath + "/evaluation/updateResult?t=" + new Date().getTime();
	evalUpdateAjaxOption.error = function() {
		alert(lang.header.popup.evaluation.alert.updateEvaluationFailed); // 평가 저장에 실패했습니다.
	}
	evalUpdateAjaxOption.success = function(jRes) {
		if(jRes.success == "Y") {
			alert(lang.header.popup.evaluation.alert.updateEvaluationComplete); // 평가 저장이 완료되었습니다.

			// 팝업 닫기
			$("#evaluationSheet").layerPopupClose();
			gridEvaluation.clearAndLoad(contextPath + "/evaluation/resultGrid.xml" + "?" + "head=true", function() {
				$("input[name=maxScore]").val(maxScore);

				ui_controller();
			});
			// 데이터 초기화
			$("#evaluationSheet").removeData();
			// 데이터 클리어(클래스가 "clear_target"인 엘리먼트들의 값을 지움)
			$("#evaluationSheet").find(".clear_target").val("");
		} else {
			evalUpdateAjaxOption.error(jRes);
		}
	};

//	$("#evaluate_complete_btn").click(function(event) {
//		var items = getRadioItems();
//		// 소분류코드: 아이템코드로 매칭되는 데이터
//		// Object {301: "402", 302: "403", 303: undefined, 304: undefined, 305: undefined}
//
//		// 항목이 있는지 검사
//		if(!Object.keys(items).length) {
//			return alert(lang.header.popup.evaluation.alert.noData); // 항목이 없습니다.
//		}
//
//		// 선택되지 않은 값이 있는지 검사
//		var notSelectedSsgCode;
//		$.each(items, function(k, v){
//			if(v === undefined) {
//				notSelectedSsgCode = k;
//				return false;
//			}
//		});
//		if(notSelectedSsgCode !== undefined) {
//			return alert(lang.header.popup.evaluation.alert.notAllSelected); // 선택하지 않은 항목이 있습니다.
//		}
//		// 값 저장하기
//		//FIXME: 여기부터 보면 될듯....
//		evalUpdateAjaxOption.data = {
//				evalCode: $("#evaluationSheet").data("evalCode"),
//				campaignCode: $("select[name=campaign_list]").val(),
//				sheetCode: $("select[name=sheet_list]").val(),
//				items: JSON.stringify(items),
//		};
//		$.ajax(evalUpdateAjaxOption);
//	});

	/**
	 * 캠페인 셀렉트 관련
	 */
//	$("select[name=campaign_list]").change(function() {
//		var val = $(this).val();
//		if((val||"") == "") {
//			return;
//		}
//		var $target = $(this).find("option[value="+val+"]");
//		var rule = $target.data("rule");
//
//		// 룰 초기화
//		var $ruleForm = $(".campagin_option_form");
//
//		// 룰에 따른 값 수정
//		$ruleForm.find("> *").each(function() {
//			var code = $(this).data("code");
//			// 인풋이 아니면 스킵
//			if(code !== 0 && !code) 	return true;
//			var $input = $(this);
//			// rule에 해당 값이 있으면 표출, 없으면 숨김
//			if(rule[code]) {
//				var textValue = rule[code + "Text"]; 	// code + "Text" 값이 valueName임
//				$input.val(lang.get("header.popup.evaluation.input." + code) + ": " + textValue);
//				$input.show();
//			} else {
//				$input.hide();
//			}
//		});
//	});
	var campaignAjaxOption = $.extend({}, baseAjaxOption);
	campaignAjaxOption.url = contextPath + "/evaluation/campaignList?t=" + new Date().getTime();
	campaignAjaxOption.success = function(jRes) {
		if(jRes.success == "Y") {
			var $select = $("select[name=campaign_list]");
			$select.find("option[value!='']").remove();

			var campaignList = jRes.resData.result;
			$.each(campaignList, function(i, c) {
				// 옵션 생성 및 데이터에 값 숨겨두고 텍스트 설정
				var $option = $("<option></option>").data(c).attr({
					value: c.rEcampCode,
				}).text(c.rEcampName);
				$select.append($option);
			});
		}
	};
	$.ajax(campaignAjaxOption);

	/**
	 * 시트 셀렉트 관련
	 */

	$(function(){

		$.ajax({
			url: contextPath+"/selectSheetInfo.do",
			type: "POST",
			dataType: "json",
			data: sheetList,
			success: function(jRes){
				if(jRes.success =="Y") {
					window.sheetList = jRes.resData.sheetList;
					for(var i=0;i<sheetList.length;i++){
						$('#sheet_list').append('<option value="' + sheetList[i].sheetCode + '" >'
								+ sheetList[i].sheetName + '</option>');
						}
						//버튼 클릭 시 평가 설명, 단계에 값을 뿌려줌
						$('#sheet_list').on('change', function(){
						/*	var strData = "sheetCode=" + $("#sheet_list option:selected").val();
							gridEvaluation.clearAndLoad(contextPath+'/evaluation/resultGrid.xml?' + strData);
*/
						})
					}
				}
		  });
		
	});

	$("select[name=sheet_list]").change(function() {
		var val = $(this).val();
		if((val||"") == "") {
			return;
		}
		var $parent = $("#gridEvaluation");
		var $target = $(this).find("option[value="+val+"]");
//		var sheetCode = $target.data("rSheetCode");
		var sheetCode =  $("#sheet_list option:selected").val();
		var depth = $target.data("rSheetDepth");
		var strData = "";
		strData += $.param({depth: depth, sheetCode: sheetCode});
		gridEvaluation.clearAndLoad(contextPath + "/evaluation/resultGrid.xml" + "?" + strData, function() {
			/*var maxScore = $target.data("maxScore");
			$("input[name=maxScore]").val(maxScore);

			$parent.find(".onoffswitch-checkbox:radio").change(changeCurrentScore);
			changeCurrentScore();
*/
			ui_controller();
		});
	});
	var sheetAjaxOption = $.extend({}, baseAjaxOption);
	sheetAjaxOption.url = contextPath + "/evaluation/sheetList?t=" + new Date().getTime();
	sheetAjaxOption.success = function(jRes) {
		if(jRes.success == "Y") {
			var $select = $("select[name=sheet_list]");
			$select.find("option[value!='']").remove();
			var sheetList = jRes.resData.result;
			$.each(sheetList, function(i, c) {
				// 옵션 생성 및 데이터에 값 숨겨두고 텍스트 설정
				var $option = $("<option></option>").data(c).attr({
					value: c.rSheetCode,
				}).text(c.rSheetName);
				$select.append($option);
			});
		}
	};
	$.ajax(sheetAjaxOption);
}

function openEvalPopup(o) {
	// 예외조건 확인
	// Object가 아니면 리턴
	if(!o || o.toString().toLowerCase() !== "[object Object]".toLowerCase()) {
		return false;
	}
	// evalCode나 sheetCode가 정의되지 않은 경우
	if(((o.evalCode !== 0 && !o.evalCode) || (o.sheetCode !== 0 && !o.sheetCode))
			// userId가 정의되지 않은 경우
			&&	(o.userId !== 0 && !o.userId)) {
		return false;
	}

	// 데이터 초기화
	$("#evaluationSheet").removeData();
	// 데이터 넣기
	$("#evaluationSheet").data(o);
	// 팝업 열기
	$("#evaluationSheet").layerPopup();
}


//프로그레스 처리
var progress = {
	on: function(mask) {
//		if($("#progress").length < 1) {
//			$("html").append("<div id='progress'>")
//		}
//		$("#progress").css("opacity", "0");
//		$("#progress").css("height", "100%");
		HoldOn.open({
				theme: "sk-circle"
			,	backgroundColor: "#FFFFFF"
		});
	}, off: function(mask) {
		HoldOn.close();
//		$("#progress").remove();
	}
}

//사용자 정보 수정
function setUserInfo() {

	var userId = userInfoJson.userId 			// 아이디
	var userName = userInfoJson.userName		// 이름
	var extNum = userInfoJson.extNo				// 내선
	var authy = userInfoJson.userLevel			// 권한
	var phoneNumber = userInfoJson.userPhone	// 폰버노
	var sex = userInfoJson.userSex				// 성별
	var bgCode = userInfoJson.bgCode			// 대
	var mgCode = userInfoJson.mgCode			// 중
	var sgCode = userInfoJson.sgCode			// 소
	var empId = userInfoJson.empId				// 사번
	var email =	userInfoJson.userEmail			// 이멜
	var ctiId = userInfoJson.ctiId				// cti id

	$("#userId").val(userId).addClass("ui_input_hasinfo").attr("readonly",true)// 아이디
	$("#userName").val(userName)							// 이름
	$("#password").val("")									// 비번
	$("#passwordCheck").val("")							// 비번확인
	$("#ext").val(extNum)								// 내선
	$("#authy").val(authy)									// 권한
	$("#phoneNumber").val(phoneNumber)						// 폰버노
	$("#sex").val(sex)										// 성별

	$("#bgCode").val(bgCode)								// 대
	selectOrganizationLoad($("#mgCode"), "mgCode", $("#bgCode").val(), "", mgCode)					// 중
	selectOrganizationLoad($("#sgCode"), "sgCode", $("#bgCode").val(), $("#mgCode").val(), sgCode) 	// 소

	$("#empId").val(empId)									// 사번
	$("#email").val(email)									// 이멜
	$("#ctiId").val(ctiId)									// cti id

	layer_popup('#modifyUser');
	// 탭모드인경우 안먹어서 일단 임시로...
	ui_controller();
}

function updateUserInfo() {
	var userId		=$("#userId").val();								// ID
	var userName	=$("#userName").val();								// 사용자이름
	var password	=$("#password").val();								// 비밀번호
	var passwordCheck	=$("#passwordCheck").val(); 						// 비밀번호 확인
/*	var extNum		=$("#ext :selected").val();							// 내선
	var phoneNumber	=$("#phoneNumber").val();							// 연락처
	var sex			=$("#sex :selected").val();							// 성별
	var authy		=$("#authy :selected").val();  						// 권한등급
	var bgCode		=$("#bgCode :selected").val();  					// 대분류
	var mgCode		=$("#mgCode :selected").val();  					// 중분류
	var sgCode		=$("#sgCode :selected").val();  					// 소분류
	var empId		=$("#empId").val();									// 사원번호
	var email		=$("#email").val();									// 이메일
	var ctiId		=$("#ctiId").val();									// CTIID
	var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/; // 이메일 정규식
*/

	var rsa = new RSAKey();
	rsa.setPublic(RSAModulus, RSAExponent);
	
	var dataStr ={};
	
	if(password==''){
		dataStr = {
				"userId":rsa.encrypt(userId)
			,	"userName":userName
//			,	"extNum":extNum
//			,	"phoneNumber":phoneNumber
//			,	"sex":sex
//			,	"empId":empId
//			,	"email":email
//			,	"ctiId":ctiId
		}
	}else{
		dataStr = {
				"userId":rsa.encrypt(userId)
			,	"userName":userName
			,	"password":rsa.encrypt(password)
//			,	"extNum":extNum
//			,	"phoneNumber":phoneNumber
//			,	"sex":sex
//			,	"empId":empId
//			,	"email":email
//			,	"ctiId":ctiId
		}
	}

	if(!userName){
		alert(lang.admin.alert.recUser9/*"이름을 입력 해 주세요!"*/)
		$("#userName").focus();
		return;
	}
	
	if(!password){
		alert(lang.admin.alert.recUser47/*"비밀번호를 입력 해 주세요!"*/)
		$("#password").focus();
		return;
		
	} else if(password){
		if(!passwordCheck){
			alert(lang.admin.alert.recUser33 /* "비밀번호 확인을 입력 해 주세요!" */)
			$("#passwordCheck").focus();
			return;
		}
	}
	if(password != passwordCheck){
		alert(lang.admin.alert.recUser29 /* "비밀번호 확인이 일치하지 않습니다.\n다시입력 해 주세요!" */)
		$("#passwordCheck").focus();
		return;
	}
	/*if(email && !email.match(regExp)){
		alert("이메일 형식을 확인 해 주세요!\nEx)furence@furence.com")
		$("#mEmail").focus();
		return;
	}*/

	$.ajax({
		url:contextPath+"/updateUserInfoIndevidaul.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		success:function(jRes){
			if(jRes.success == "Y"){
				alert(lang.common.alert.modification.completed /* "정보수정이 완료 되었습니다." */);
				layer_popup_close('#modifyUser');
				if(userName){
					$('.user_id p').html($('.user_id p').html().replace(userInfoJson.userName,userName))
					userInfoJson.userName=userName;
				}
			}else if(jRes.success == "N" && jRes.result == "PASSWORD PATTERN IS MISS MATCH"){
				alert(jRes.resData.msg);
			}else{
				alert( lang.admin.alert.recUser28 /* "정보수정에 실패 하였습니다. */ + " \n Error : "+jRes.result);
			}
		}
	});
}

/**대중소 셀렉트 옵션 불러오기
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 * @pram : defaultSelect => 기본값 선택 true/false
 * */
function selectOrganizationLoad(objSelect, comboType, bgCode, mgCode, selectedValue, defaultSelect, subOpt, empty){
	// ($("#mBgCode"), "bgCode",undefined,undefined,undefined,true,"callCenter");
	// 옵션 붙여 넣기 전에 삭제
	$(objSelect).children().remove()

	var dataStr = {
			"comboType" : comboType // "bgCode"
		,	"bgCode" : bgCode		// 'undefined'
		,	"mgCode" : mgCode		// 'undefined'
		,	"selectedValue" : selectedValue //'undefined'
		,	"accessLevel" : accessLevel
		,	"subOpt" : subOpt 		// 'callcenter'
	}

	$.ajax({
		url:contextPath+"/organizationSelect.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				if(/*empty == undefined || */empty){
					if(empty=="Y"){
						$(objSelect).append("<option value=''>"+lang.common.label.Noclassification/* 해당분류없음 */+"</option>")
					}else if(empty=="empty"){
						//empty
					}else
						$(objSelect).append("<option></option>")
				}

				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
				if(defaultSelect)
					$(objSelect).val("")
			}
		}
	});
}

/**셀렉트 옵션 불러오기
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : comboType2 => 콤보 기본값 추가 해주기 (default로 값 셋팅 해주면 됨.)
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 *
 * */
function selectOptionLoad(objSelect, comboType, comboType2, selectedIdx, selectedName, selectedValue, empty,all){

	// 옵션 붙여 넣기 전에 삭제 (대중소 콤보 로드시...)
	$(objSelect).children().remove()

	var dataStr = {
			"comboType" : comboType
		,	"comboType2" : comboType2
		,	"selectedIdx" : selectedIdx
		,	"selectedName" : selectedName
		,	"selectedValue" : selectedValue
		,	"ALL" : all
	}

	$.ajax({
		url:contextPath+"/selectOption.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				if(empty){
					if(empty=='all')
						$(objSelect).append("<option value=''>"+lang.admin.label.noAuthy+"</option>")
					else if(empty=='none')
						$(objSelect).append("<option disabled selected>"+lang.admin.label.authy+"</option><option value=''>"+lang.admin.label.all+"</option>")
					else
						$(objSelect).append("<option></option>")
				}

				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}

//평가 플레이어 객체
var eval_rc;

$(function() {
	// 고객사별 전용 메뉴 추가 및 제거
	customerCodeProc()
	
	if("recsee"!=evalThema && evalThema!=null && evalThema!="" && evalThema!=undefined && "Y"!=recsee_mobile && (userInfoJson.userLevel == "E1001" || userInfoJson.userLevel == "E1006" || userInfoJson.userLevel == "E1005")){
		$("#QAuserBtn").show();
	}else{
		$("#QAuserBtn").hide();
	}
	
	try{
	// 새로고침 이벤트
	top.keyDownEvent(document)
	}catch (e) {
		// TODO: handle exception
	}
	
	// 클릭 이벤트
	$("#UserInfoBtn").click(function(){
		setUserInfo();
	});
	$("#userModifyBtn").click(function(){
		updateUserInfo();
	});

	// 셀렉트 옵션값 셋팅
	// 내선
	selectOptionLoad($("#ext"), "channel")
	// 권한
	selectOptionLoad($("#authy"), "authy")
	// 대분류
	selectOrganizationLoad($("#bgCode"), "bgCode")

	// 대분류 변화시 중분류 로드
	$( "#bgCode" ).change(function() {
		selectOrganizationLoad($("#mgCode"), "mgCode", $(this).val())
		// 중분류 및의 소분류가 있다면 디폴트로 로드
		selectOrganizationLoad($("#sgCode"), "sgCode", $(this).val(), $("#mgCode").val())
	});

	// 중분류 변화시 소분류 로드
	$( "#mgCode" ).change(function() {
		selectOrganizationLoad($("#sgCode"), "sgCode", $("#sgCode").val(), $(this).val())
	});

	// 메뉴 로드
	$.ajax({
		url:contextPath+"/menu.do",
		data:{},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				//$(objSelect).append(jRes.resData.optionResult)

				var menuList = jRes.resData.menuAccessList;
				//메뉴 리스트 로그
				var adminMenuCount = 0;
				for(var i = 0 ; i < menuList.length ;i++){
					var menu = menuList[i];
					var displayLevel = menu.displayLevel;

					var menuTop = menu.programTop.split("|")[0];
					var menuTopSub = menu.programTop.split("|")[1];
					var menuPath = contextPath+menu.programPath;
					var read = (menu.readYn == "Y" ? true : false);
					try{
						var menuName = lang.fn.get('header.menu.label.'+menu.programSrc);
						var adminTopMenuName = lang.fn.get('header.menu.label.administer'); /*계정관리*/
						var adminMenuName = lang.fn.get('admin.menu.'+menu.programSrc);
						var adminSubMenuName = lang.fn.get('admin.menu.li.'+menu.programSrc);
					}catch(e){
					}
					switch(menuTop){
						case "Search":
							loadMenu("icon_menu_agent", menuPath, menuName, displayLevel );
							break;
						case "Statistics":
							loadMenu("icon_menu_dashboard", menuPath, menuName, displayLevel );
							break;
						case "Monitoring":
							loadMenu("icon_menu_monitoring", menuPath, menuName, displayLevel );
							break;
						case "Evaluation":
							loadMenu("icon_menu_evaluation", menuPath, menuName, displayLevel );
							break;
						case "Myfolder":
							loadMenu("icon_menu_myfolder", menuPath, menuName, displayLevel );
							break;
						case "BestCall":
							loadMenu("icon_menu_bestcall", menuPath, menuName, displayLevel );
						break;
						case "UploadStatus":
							loadMenu("icon_menu_upload", menuPath, menuName, displayLevel );
						break;
						case "ApproveList":
							loadMenu("icon_menu_approvelist", menuPath, menuName, displayLevel );
							break;
						case "Transcript":
							loadMenu("icon_menu_transcript", menuPath, menuName, displayLevel );
							break;
						//대면녹취
						case "FaceRecording":
							loadMenu("icon_menu_FaceRecording", menuPath, menuName, displayLevel );
							break;
						//스크립트관리
						case "ScriptRegistration":
							loadMenu("icon_menu_ScriptRegistration", menuPath, menuName, displayLevel );
							break;
						case "Management":
							if(displayLevel == 0){
								if($(".menu_admin").length == 0){
									var htmlString='';
									var tempCss = "position:absolute; margin:12px 0px 0px -75px; font-size:23px;";
									if (unqLang == "jp") {
										tempCss = "position:absolute;margin:11px 0px 0px -90px;font-size:23px;";
									}									
									if(tabMode=='Y'){
										htmlString ='<ul class="menu_admin"><li class="icon_menu_admin" onclick="topRedirect(this)"><a><i class=\"fas fa-cogs\" style=\"'+tempCss+'\"></i></a></li></ul>';
									}else{
										htmlString ='<ul class="menu_admin"><li class="" onclick="topRedirect(this)" style="padding: 0 23px 0 60px;"><a href="'+menuPath+'" target="_self"><i class=\"fas fa-cogs\" style=\"'+tempCss+'\"></i><p>'+menuName+'</p></a></li></ul>';
									}
									$(".top_menu").append(htmlString)
								}
							}

							// 어드민 페이지에서만 좌측 메뉴 출력
							if ($(".admin_lnb").length =! 0){
								// 메뉴 담을 div가 없을때만 추가
								if($(".admin_menu").length == 0){
									htmlString = "<div class='admin_menu'>";
									$(".admin_lnb").append(htmlString)
								}
							}

							if(displayLevel == 1){
								// 셋팅 메뉴가 없다면 div 추가
								if($(".admin_"+menuTopSub).length == 0){
									htmlString = '<ul><p class="admin_menu_tit admin_'+menuTopSub+'">'+adminMenuName+'<span class="ui-icon ui-icon-plus ui-icon-plus-white ui-icons-right"><span></p></ul>';
									$(".admin_menu").append(htmlString)
								}
							}else if(displayLevel == 2){
								// 계정관리 경로 지정
								if($('.icon_menu_admin').find('p').length == 0){
									htmlString ="<p onclick='"+'addTabBar("'+menuPath+'","'+adminTopMenuName+'")'+"'>"+adminTopMenuName+"</p>"
									$(".icon_menu_admin").append(htmlString);
								}
								// 탑 메뉴 눌렀을 때 0번 인덱스의 서브 페이지 호출하게..
								if($(".menu_admin > li a").attr("href") != undefined)
									if($(".menu_admin > li a").attr("href").indexOf("management") > -1)
										$(".menu_admin > li a").attr("href",menuPath);

								htmlString = '<li><a href="'+menuPath+'" target="_self"><p>'+adminSubMenuName+'</p></a></li>'
								$(".admin_"+menuTopSub).parent().append(htmlString);

								adminMenuCount++;
							}
							break;
					}
					//마지막, 페이지 수 계산하여 메뉴 삭제 처리
					if(i == menuList.length-1){
						$(".menu_agent > li").each(function(i){
							if($(this).find("li").length == 1)
								$(this).find("li").hide();
							else if($(this).find("li").length == 0){
								console.log($(this).find("li"))
								console.log($(this))
								$(this).remove();
							}
						});
						$(".admin_menu ul").each(function(i){
							if($(this).find("li").length == 0)
								$(this).remove();
						});
						if (adminMenuCount == 0)
							$(".menu_admin").remove();
					}
				}
				if(tabMode!='Y')
					onActiveMenu();
			}
		}
	});

	// 메뉴 로드 해주는 펑션
	function loadMenu(topMenuObj, menuPath, menuName, displayLevel){
		
		if($(".menu_agent").length == 0){
			var htmlString ="<ul class='menu_agent'></ul>";
			$(".top_menu").append(htmlString)
		}
		if(displayLevel == 0){
			console.log($("."+topMenuObj).length)
			console.log(topMenuObj);
			if($("."+topMenuObj).length == 0){
				if(tabMode=='Y'){
					var htmlString ="<li onclick='topRedirect(this)' class='"+topMenuObj+"'></li>";
					$(".menu_agent").append(htmlString);
					htmlString = "<p>"+menuName+"</p><ul></ul>";
				}else{
					var htmlString ="<li onclick='topRedirect(this)' class='"+topMenuObj+"'></li>";
					$(".menu_agent").append(htmlString)
					htmlString = "<a href='"+menuPath+"' target=_self><p>"+menuName+"</p><ul></ul></a>";
				}

				$("."+topMenuObj).append(htmlString)
			}
		}else{
			// 탑 메뉴 눌렀을 때 0번 인덱스의 서브 페이지 호출하게..
			if($("."+topMenuObj).find("li").length == 0)
				$("."+topMenuObj+" > a").attr("href",menuPath);

			if(tabMode=='Y'){
				var htmlStirng = "<li><p onclick='"+'addTabBar("'+menuPath+'","'+menuName+'")'+"'>"+menuName+"</p></li>";
			}else{
				var htmlStirng = "<li><a href='"+menuPath+"' target=_self><p>"+menuName+"</p></a></li>";
			}
			$("."+topMenuObj).find("ul").append(htmlStirng)
		}
	}

 	// 파라미터 가져와서 파일 세팅하기
 /*   var param = (function(paramStr) {
        // 파라미터 가져오기
        var parameters = paramStr.replace(/^\?/, "").split("&");
        // 파라미터 담기
        var p = {};
        var res = "";
        var splitRegexp = /^(.*?)=(.*?)$/
        $.each(parameters, function(i, str){
            try {
                var data = str.match(splitRegexp);
                var name = data[0];
                var value = data[1];
                p[name] = value;
            } catch(e) { }
        });
        return p;
    }(window.location.search));

 	try{
	    eval_rc = new RecseePlayer({
	        target: "#playerObj",
		    audio: true,
		    video: false,
	        wave: true,
	        //wave: (param.mode == "a" || param.mode == "wave")
	    })
 	}catch(e){}*/

	$(document).on("mousedown", ".menu_admin", function(e){

		if(e.button ==2 && userId =="admin"){

			document.orioncontextmemu = document.oncontextmenu;
        	document.oncontextmenu = function(){
        		return false;
        	}
        	e.preventDefault();
			e.stopPropagation();
			window.open(contextPath+"/admin/etc_config","etc_config", "resizable=yes,minimizable=no")
		}
	});

	// font-awesome	icon 삽입
	$('.icon_menu_upload').prepend("<i class=\"fas fa-cloud-upload-alt\" style=\"position:absolute;margin:13px 0px 0px -32px;font-size:20px;\"></i>");
	$('.icon_menu_bestcall').prepend("<i class=\"fas fa-share-square\" style=\"position:absolute;margin:13px 0px 0px -32px;font-size:20px;\"></i>");
	$('.icon_menu_approvelist').prepend("<i class=\"fas fa-clipboard-list\" style=\"position:absolute;margin:13px 0px 0px -32px;font-size:23px;\"></i>");
	//$('.icon_menu_transcript').prepend("<i class=\"fas fa-clipboard-list\" style=\"position:absolute;margin:13px 0px 0px -32px;font-size:23px;\"></i>");

	
	/*if(evalThema == 'master'){
		$(".btn_user_qa").show();
	}else{
		$(".btn_user_qa").hide();
	}*/
	


});


function onActiveMenu(){
	// header 메뉴 활성화
    // menu_agent > li가 포함하는 a 중에서 href 가 location.pathname의 /앞까지 부분 일치하는 li에 이벤트
    $(".menu_agent > li").filter(function() {
    	var target = location.pathname.split("/");
    	target.pop();
    	target = target.join("/");
    	return function() {
    		return $(this).find("a[href*='"+target+"']").length;
    	}
    }()).css({ // css 적용
		"background-color": "#"+baseActivColor,
		"border-color": "#"+baseActivColor
	});
    $(".menu_admin > li").filter(function() {
    	var target = location.pathname.split("/");
    	target.pop();
    	target = target.join("/");
    	return function() {
    		return $(this).find("a[href*='"+target+"']").length;
    	}
    }()).css({ // css 적용
		"background-color": "#"+baseActivColor,
		"border-color": "#"+baseActivColor
	});

	// admin 메뉴 활성화
    // admin_menu li가 포함하는 a 중에서 href 가 location.pathname과 부분 일치하는 li에 이벤트
	$(".admin_menu li").filter(function() {
		if($(this).children().attr("href")==location.pathname){
			return 1
		}
		else {
			return 0
		}

	}).css({ // css 적용
		"background-color": "#"+baseActivColor
	});
}

function topRedirect(t){
	//window.location.href = $(t).children().attr("href");
	if(tabMode=='Y'){
		if("icon_menu_admin"==t.className)
			$(t).find('p')[0].click();
		else
			$(t).children().find('p')[0].click();
	}else{
		window.location.href = $(t).children().attr("href");
	}
}

function selectCampaign(){
/*	var $select = $("select[id=campaignList]");
	$select.find("option[value!='']").remove();*/
	$("#countCamp").val("");

	var dataStr = {
			"userId" : userInfoJson.userId
	}
				$.ajax({
					url : contextPath + "/evaluation/evalDegree",
					type : "POST",
					data : dataStr,
					dataType : "json",
					async : false,
					success : function(jRes) {
						if(jRes.success == "Y") {

							var rEcampCode = jRes.resData.rEcampCode;
							var rEcampName = jRes.resData.rEcampName;
							var rEvalCount = jRes.resData.rEvalCount;
							var rEvalTotal = jRes.resData.rEvalTotal;

							var $select = $("select[id=campaignList]");
							$select.find("option[value!='']").remove();
							if(rEcampCode.length>0){
								for(z=0; z<rEcampCode.length; z++){
									var $option = $("<option></option>").attr({
										value: rEcampCode[z]
									}).text(rEcampName[z]);
									$select.append($option);
								}
								$("select[id=campaignList]").find("option:eq(0)").prop("selected", true);

							}else{
								alert(lang.common.alert.noEvaluation /* "평가분배 받은 캠페인이 없습니다." */)
								return;
							}

							layer_popup('#qaUser');

							$("#campaignList").on('change', function(){
								var index = $("#campaignList option").index($("#campaignList option:selected"));
								$("#countCamp").val(rEvalCount[Number(index-1)])
								$("#countCamp").attr("disabled", "true");
								$("#totalCamp").val(rEvalTotal[Number(index-1)])
								$("#totalCamp").attr("disabled", "true");
							});

						}
					}
				});
}

function customerCodeProc(){
	$.ajax({
		url : contextPath + "/customer_code.do",
		dataType : "json",
		success : function(jRes){}
	});
}
