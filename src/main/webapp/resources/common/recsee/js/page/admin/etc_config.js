// 전역변수 설정
var gridEtcConfigManage; // 그리드
var log = "", logRes = "";

/**셀렉트 옵션 불러오기
 * @pram : objSelect => select 객체(html) jquery select
 * @pram : comboType => 콤보 불러올 타입 (권한[auty], 채널[channel],
 * @pram : comboType2 => 콤보 기본값 추가 해주기 (default로 값 셋팅 해주면 됨.)
 * @pram : selectedIdx => index로 default 선택값 지정
 * @pram : selectedName => name(콤보 표출명)로 default 선택값 지정
 * @pram : selectedValue => value(콤보 값)로 default 선택값 지정
 *
 * */
function selectOptionLoad(objSelect, comboType, comboType2, selectedIdx, selectedName, selectedValue){

	// 옵션 붙여 넣기 전에 삭제 (대중소 콤보 로드시...)
	$(objSelect).children().remove()

	var dataStr = {
			"comboType" : comboType
		,	"comboType2" : comboType2
		,	"selectedIdx" : selectedIdx
		,	"selectedName" : selectedName
		,	"selectedValue" : selectedValue
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
				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}

function formFunction(){
	$("#usePrefix").change(function(){
		var strData = {
				"groupKey" : "Prefix"
			,	"configKey" : "PrefixYN"
			,	"configValue" : $("#usePrefix").val()
		}
		
		$.ajax({
			url:contextPath+"/updateOptionValue.do",
			data:strData,
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					if($("#usePrefix").val()=="Y"){
						$("#prefixBox").css("display","inline");
						$("#gridPrefixManage").css("display","inline");
					}else{
						$("#prefixBox").css("display","none");
						$("#gridPrefixManage").css("display","none");
					}
				}
			}
		});
	});
	
	$("#usePNumMasking").change(function(){
		var strData = {
				"groupKey" : "masking"
			,	"configKey" : "maskingYN"
			,	"configValue" : $("#usePNumMasking").val()
		}
		
		$.ajax({
			url:contextPath+"/updateOptionValue.do",
			data:strData,
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					if($("#usePNumMasking").val()=="Y"){
						$("#pNumMaskingBox").css("display","inline");
						pNumMaskingValLoad();
					}else{
						$("#pNumMaskingBox").css("display","none");
					}
				}
			}
		});
	});
	
	$("#useHyphen").change(function(){
		var strData = {
				"groupKey" : "hyphen"
			,	"configKey" : "hyphenYN"
			,	"configValue" : $("#useHyphen").val()
		}
		
		$.ajax({
			url:contextPath+"/updateOptionValue.do",
			data:strData,
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					if($("#useHyphen").val()=="Y"){
						$("#setHyphenBox").css("display","inline");
					}else{
						$("#setHyphenBox").css("display","none");
					}
				}
			}
		});
	});
	
	$("#setHyphen1").change(function(){
		if($("#setHyphen1").val()=="Y"){
			$("#setHyphen1Val").css("display","inline");
		}else{
			$("#setHyphen1Val").val("");
			$("#setHyphen1Val").css("display","none");
		}		
	});
	
	// 콤보 로드
	selectOptionLoad($("#groupKey"),"etcConfig");
	$("#groupKey").change(function(){
		if ($(this).val() != "")
			selectOptionLoad($("#configKey"),"etcConfig",$(this).val());
		else{
			$("#configKey").children().remove()
			$("#configKey").append('<option value="" disabled selected>설정 키 선택</option>')
		}
	})
	
	// 버튼 클릭
	$("#searchBtn").click(function(){
		gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
	});
	
//	// 설정값 추가 팝업
//	$("#addPopBtn").click(function(){
//		
//		$("#addBtn").show();
//		$("#modifyBtn").hide();
//		$("#delBtn").hide();
//		
//		$("#groupKey").val("");
//		$("#configKey").val("");
//		$("#configValue").val("");
//		
//		layer_popup('#modifyConfig');
//	});
//	
//	// 체크 삭제
//	$("#deleteBtn").click(function(){
//		var chCol = -1;
//		for(var i = 0; i < objGrid.getColumnsNum()-1; i++) {
//			if(objGrid.getColType(i) == "ch") {
//				chCol = i;
//				break;
//			}
//		}
//
//		if(chCol != -1 && objGrid.getCheckedRows(chCol) != "" ) {
//			var checked = objGrid.getCheckedRows(chCol).split(",");
//			var deleteList = new Array();
//			for( var i = 0 ; i < checked.length ;i++ ) {
//				var keyList = objGrid.cells(checked[i],1).getValue()+","+objGrid.cells(checked[i],2).getValue();
//				deleteList.push(keyList);
//			}
//			
//			if(confirm(lang.admin.alert.etcConfig1 + "\n\n"+ lang.admin.alert.etcConfig2)){
//				
//				if(confirm(lang.admin.alert.etcConfig1 + "\n\n"+ lang.admin.alert.etcConfig2)){
//			
//					var strData = {
//							"proc" : "delete"
//						,	"deleteList" : deleteList.join("|")
//					}
//		
//					$.ajax({
//						url:contextPath+"/etc_config_proc.do",
//						data:strData,
//						type:"GET",
//						dataType:"json",
//						async: false,
//						success:function(jRes){
//							// DB에 조회한 계정이 있으면
//							if(jRes.success == "Y") {
//								alert(lang.admin.alert.attrManage2)
//								selectOptionLoad($("#groupKey"),"etcConfig");
//								gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
//								layer_popup_close()
//							}
//						}
//					});
//				}
//			}
//		}
//	});
	
//	// 팝업내 저장
//	$("#addBtn").click(function(){
//		var groupKey = $("#mGroupKey").val();
//		var configKey = $("#mConfigKey").val();
//		var configValue = $("#mConfigValue").val();
//		
//		if (groupKey == null || groupKey == ""){
//			alert(lang.admin.alert.etcConfig5);
//			$("#groupKey").focus();
//			return;
//		} 
//		
//		if (configKey == null || configKey == ""){
//			alert(lang.admin.alert.etcConfig6);
//			$("#configKey").focus();
//			return;
//		} 
//
//		if (configValue == null || configValue == ""){
//			if(!confirm(lang.admin.alert.etcConfig7))
//				return;
//		} 
//		
//	   // 추가하기전에 그리드내에 같은 이름 있는지 확인할지 아니면 db에서 확인하고 나오는값으로 중복여부 알려줄지... 어쨋든 중복값확인을 넣긴 넣어야될거같음
//		
//		var strData = {
//				"proc" : "insert"
//			,	"groupKey" : groupKey
//			,	"configKey" : configKey
//			,	"configValue" : configValue
//		}
//
//		$.ajax({
//			url:contextPath+"/etc_config_proc.do",
//			data:strData,
//			type:"GET",
//			dataType:"json",
//			async: false,
//			success:function(jRes){
//				// DB에 조회한 계정이 있으면
//				if(jRes.success == "Y") {
//					alert(lang.admin.alert.etcConfig9)
//					selectOptionLoad($("#groupKey"),"etcConfig");
//					gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
//					$("#mGroupKey").val("");
//					$("#mConfigKey").val("");
//					$("#mConfigValue").val("");
//					layer_popup_close()
//				}
//			}
//		});
//		
//		
//	});
	
	// 팝업 내 수정
	$("#modifyBtn").click(function(){
		
		var mGroupKey = $("#mGroupKey").val();
		var mConfigKey = $("#mConfigKey").val();
		var configValue = $("#mConfigValue2").val();
		
		if (configValue == null || configValue == ""){
			if(!confirm(lang.admin.alert.etcConfig7))
				return;
		}
		
//		var oriGroupKey = gridEtcConfigManage.cells(gridEtcConfigManage.getSelectedRowId(),0).getValue()
//	    var oriConfigKey = gridEtcConfigManage.cells(gridEtcConfigManage.getSelectedRowId(),1).getValue()
//		
//	    if (oriGroupKey != mGroupKey && oriConfigKey != mConfigKey){
//	    	if (gridEtcConfigManage.findCell(groupKey,0).length > 0 && gridEtcConfigManage.findCell(configKey,1).length > 0){
//				alert(lang.admin.alert.etcConfig8);
//				return
//			}
//	    }
		
		var groupKey = gridEtcConfigManage.cells(gridEtcConfigManage.getSelectedRowId(),0).getValue();
		var configKey = gridEtcConfigManage.cells(gridEtcConfigManage.getSelectedRowId(),1).getValue();
		
		if(confirm(lang.admin.alert.etcConfig3 + "\n\n" + lang.admin.alert.etcConfig4)){
			
			if(confirm(lang.admin.alert.etcConfig3 + "\n\n" + lang.admin.alert.etcConfig4)){

				var strData = {
						"proc" : "update"
					,	"groupKey" : groupKey
					,	"mGroupKey" : mGroupKey
					,	"configKey" : configKey
					,	"mConfigKey" : mConfigKey
					,	"configValue" : configValue
				}
	
				$.ajax({
					url:contextPath+"/etc_config_proc.do",
					data:strData,
					type:"GET",
					dataType:"json",
					async: false,
					success:function(jRes){
						// DB에 조회한 계정이 있으면
						if(jRes.success == "Y") {
							alert(lang.admin.alert.etcConfig10)
							selectOptionLoad($("#groupKey"),"etcConfig");
							gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
							$("#mGroupKey").val("");
							$("#mConfigKey").val("");
							$("#mConfigValue").val("");
							layer_popup_close()
						}
					}
				});
			}
		}
	});
	
//	// 팝업 내 삭제
//	$("#delBtn").click(function(){
//		var groupKey = gridEtcConfigManage.cells(gridEtcConfigManage.getSelectedRowId(),1).getValue();
//		var configKey = gridEtcConfigManage.cells(gridEtcConfigManage.getSelectedRowId(),2).getValue();
//		
//		if(confirm(lang.admin.alert.etcConfig1 + "\n\n"+ lang,admin.alert.etcConfig2)){
//			
//			if(confirm(lang.admin.alert.etcConfig1 + "\n\n"+ lang,admin.alert.etcConfig2)){
//
//				var strData = {
//						"proc" : "delete"
//					,	"groupKey" : groupKey
//					,	"configKey" : configKey
//				}
//	
//				$.ajax({
//					url:contextPath+"/etc_config_proc.do",
//					data:strData,
//					type:"GET",
//					dataType:"json",
//					async: false,
//					success:function(jRes){
//						// DB에 조회한 계정이 있으면
//						if(jRes.success == "Y") {
//							alert(lang.admin.alert.etcConfig11)
//							selectOptionLoad($("#groupKey"),"etcConfig");
//							gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
//							$("#mGroupKey").val("");
//							$("#mConfigKey").val("");
//							$("#mConfigValue").val("");
//							layer_popup_close()
//						}
//					}
//				});
//			}
//		}
//	});
}

function formSirealize(requestOrderBy){
	var groupKey = $("#groupKey").val()||"";
	var configKey = $("#configKey").val()||"";
	var configValue = $("#configValue").val()||"";
	var strData = "groupKey="+groupKey+"&configKey="+configKey+"&configValue="+configValue
	return encodeURI(strData);
}

function etcConfigManageLoad() {
	gridEtcConfigManage = etcConfigGridLoad();
	formFunction()
	authyLoad();

	prefixYNLoad();
	gridPrefixManage = prefixGridLoad();
	pNumMaskingYNLoad();
	hyphenYNLoad();
	opener.document.oncontextmenu = opener.document.orioncontextmemu
}

//권한 불러 오기
function authyLoad() {
	
}

// 로그관리 로드
function etcConfigGridLoad() {
	
    // 로그관리 Gridd
	objGrid = new dhtmlXGridObject("gridEtcConfigManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	//objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 250, 5, "pagingEtcConfigManage", true);
	objGrid.setPagingWTMode(true,true,true,[100,250,500,1000]);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/etc_config.xml", function(){
		objGrid.aToolBar.setMaxOpen("pages", 5);
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			ui_controller();
			$(window).resize();
			objGrid.setSizes();
			$(window).resize(function() {
				objGrid.setSizes();
			});

		});
		
		// 로우 덥클
		objGrid.attachEvent("onRowDblClicked", function(id,cInd){
			
			$("#addBtn").hide();
			$("#modifyBtn").show();
			$("#delBtn").hide();
			
			var groupKey = objGrid.cells(id,0).getValue();
			var configKey = objGrid.cells(id,1).getValue();
			var configValue;
			var configOption = new Array();
			if(objGrid.cells(id,4).getValue()==""){
				configValue = objGrid.cells(id,2).getValue();
				$("#mConfigValue").html("<input type='text' id='mConfigValue2'/>");
				$("#mConfigValue input").val(configValue);
			}
			else{
				configValue = "";
				$("#mConfigValue").html("<select id='mConfigValue2'><select>");
				configOption = objGrid.cells(id,4).getValue().split("/");
				for(var i=0; i<configOption.length; i++){
					if(configOption[i]==objGrid.cells(id,2).getValue()){
						configValue += "<option selected>"+configOption[i]+"</option>"
					}else{
						configValue += "<option>"+configOption[i]+"</option>"
					}
				}
				$("#mConfigValue select").html(configValue);
			}
			
			$("#mGroupKey").val(groupKey);
			$("#mConfigKey").val(configKey);
			
			layer_popup('#modifyConfig');
		});
		
	
//		var search_toolbar = objGrid.aToolBar;
//		search_toolbar.addSpacer("perpagenum");
//		search_toolbar.addButton("excelDownload",8, lang.common.excel.download/*"엑셀 다운로드"*/, "icon_excel_download.png", "icon_excel_download.png");		
//		search_toolbar.attachEvent("onClick", function(name){
//			switch(name) {
//			case "excelDownload":
//				if(objGrid.getRowsNum() > 0) {
//					objGrid.toExcel(contextPath+"/generateExcel.do?fileName=etcConfigList");
//				}
//				break;
//			}
//		});

	}, 'xml');

    ui_controller();
    return objGrid;
}

//prefix 그리드 로드
function prefixGridLoad() {

    // 로그관리 Grid
	prefixGrid = new dhtmlXGridObject("gridPrefixManage");
	prefixGrid.setIconsPath(recseeResourcePath + "/images/project/");
	prefixGrid.setImagePath(recseeResourcePath + "/images/project/");
	//objGrid.i18n.paging =  i18nPaging[locale];
	//objGrid.enablePaging(true, 250, 5, "pagingEtcConfigManage", true);
	//objGrid.setPagingWTMode(true,true,true,[100,250,500,1000]);
	//objGrid.setPagingSkin("toolbar", "dhx_web");
	prefixGrid.enableColumnAutoSize(false);
	prefixGrid.setSkin("dhx_web");
	prefixGrid.init();

	prefixGrid.load(contextPath+"/prefixList.xml", function(){
		
		//체크박스 전체 선택
		prefixGrid.attachEvent("onHeaderClick",function(ind, obj){
			if(ind == 0 && obj.type == "click") {
				if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
					prefixGrid.setCheckedRows(0, 1);
					$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
				} else {
					prefixGrid.setCheckedRows(0, 0);
					$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
				}
			} else {
				return true;
			}
		});
		
	}, 'xml');
	
	ui_controller();
    return prefixGrid;
}


function addPrefixVal() {
	$("#addPrefix").click(function(){
		var prefixVal = $("#prefixVal").val();
		if (prefixVal == null || prefixVal == "") {
			alert(lang.admin.etcConfig.alert.prefix.emptyNum /* "번호를 입력해주세요." */);
			return;
		}
		var dataStr = {
					"groupKey" : "Prefix"
				,	"configKey" : "Prefix"
				,	"configValue" : prefixVal
		};

		$.ajax({
			url:contextPath+"/addPrefixVal.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.etcConfig.alert.addSuccess /* "추가 성공" */);			
					$("#prefixVal").val("");
					gridPrefixManage.clearAndLoad(contextPath+"/prefixList.xml")
				}else{
					if (jRes.resData.msg == "duplicationValue") {
						alert(lang.admin.etcConfig.alert.prefix.useNum /* "이미 등록된 번호입니다." */);
					} else {
						alert(lang.admin.etcConfig.alert.addFail /* "추가 실패" */);
					}
				}
			}
		});
	});
}


function delPrefixVal() {
	if(gridPrefixManage.getCheckedRows(0) != "") {
		var checked = gridPrefixManage.getCheckedRows(0).split(",");
		var prefixIdx = new Array();
		for( var index in checked ) {
			if (index == "trim") {
				break;
			}
			prefixIdx.push(gridPrefixManage.cells(parseInt(checked[index]),1).getValue());
		}
		var idxArr = prefixIdx.join(",");
		console.log(idxArr);
		
		var dataStr = {
				"groupKey" : "Prefix"
			,	"configKey" : "Prefix"
			,	"idx" : idxArr
		};
		$.ajax({
			url:contextPath+"/delPrefixVal.do",
			data: dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.etcConfig.alert.delSuccess /* "삭제 성공" */);
					gridPrefixManage.clearAndLoad(contextPath+"/prefixList.xml")
				} else {
					alert(lang.admin.etcConfig.alert.delFail /* "삭제 실패" */);
				}
			}
		});
	}else{
		alert(lang.admin.etcConfig.alert.prefix.chkDelNum /* "삭제할 번호를 선택해주세요" */);
	}
}

function prefixYNLoad() {
	var dataStr = {
			"groupKey" : "Prefix"
		,	"configKey" : "PrefixYN"
	};
	
	$.ajax({
		url:contextPath+"/selectOptionYN.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#usePrefix").val(jRes.resData.configValue).attr('selected', true);
				if(jRes.resData.configValue=="Y"){
					$("#prefixBox").css("display","inline");
				}else{
					$("#prefixBox").css("display","none");
					$("#gridPrefixManage").css("display","none");
				}
			}
		}
	});
}

function pNumMaskingYNLoad() {
	var dataStr = {
			"groupKey" : "masking"
		,	"configKey" : "maskingYN"
	};
	
	$.ajax({
		url:contextPath+"/selectOptionYN.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#usePNumMasking").val(jRes.resData.configValue).attr('selected', true);
				if(jRes.resData.configValue=="Y"){
					$("#pNumMaskingBox").css("display","inline");
					pNumMaskingValLoad();
				}else{
					$("#pNumMaskingBox").css("display","none");
				}
			}
		}
	});
}

function hyphenYNLoad() {
	var dataStr = {
			"groupKey" : "hyphen"
		,	"configKey" : "hyphenYN"
	};
	
	$.ajax({
		url:contextPath+"/selectOptionYN.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#useHyphen").val(jRes.resData.configValue).attr('selected', true);
				if(jRes.resData.configValue=="Y"){
					$("#setHyphenBox").css("display","inline");
					hyphenValLoad();
				}else{
					$("#setHyphenBox").css("display","none");
				}
			}
		}
	});
}

function pNumMaskingValLoad() {
	var dataStr = {
			"groupKey" : "masking"
		,	"configKey" : "masking"
	};
	
	$.ajax({
		url:contextPath+"/selectMaskingInfo.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#startIdx").val(jRes.resData.startIdx);	
				$("#maskingEA").val(jRes.resData.maskingEA);
			}
		}
	});
}

function hyphenValLoad() {
	var dataStr = {
			"groupKey" : "hyphen"
		,	"configKey" : "hyphen"
	};
	
	$.ajax({
		url:contextPath+"/selectHyphenInfo.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				if (jRes.resData.hyphen1 != "N" && jRes.resData.hyphen1 != 2 && jRes.resData.hyphen1 != 3 && jRes.resData.hyphen1 != 4) {
					$("#setHyphen1").val("Y").attr('selected', true);
					$("#setHyphen1Val").css("display","inline");
					$("#setHyphen1Val").val(jRes.resData.hyphen1);
				} else {
					$("#setHyphen1").val(jRes.resData.hyphen1).attr('selected', true);
					$("#setHyphen1Val").css("display","none");
				}				
				$("#setHyphen2").val(jRes.resData.hyphen2).attr('selected', true);
			}
		}
	});
}
function setMaskingVal() {
	$("#pNumMasking").click(function(){
		var startIdx = $("#startIdx").val();
		var maskingEA = $("#maskingEA").val();
		if (startIdx == null || startIdx == "") {
			alert(lang.admin.etcConfig.alert.masking.emptyFrom /* "마스킹 할 시작 위치를 입력해주세요" */);
			return;
		}
		if (maskingEA == null || maskingEA == "") {
			alert(lang.admin.etcConfig.alert.masking.emptyCount /* "마스킹 개수를 입력해주세요" */);
			return;
		}
		var configValue = startIdx + "," + maskingEA
		var dataStr = {
					"groupKey" : "masking"
				,	"configKey" : "masking"
				,	"configValue" : configValue
		};

		$.ajax({
			url:contextPath+"/updateOptionValue.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.etcConfig.alert.modifySuccess /* "변경 성공" */);			
					pNumMaskingValLoad();
				}else{
					alert(lang.admin.etcConfig.alert.modifyFail /* "변경 실패" */);
				}
			}
		});
	});
}

function setHyphenVal() {
	$("#setHyphen").click(function(){
		var setHyphen1 = $("#setHyphen1").val();
		if (setHyphen1 == "Y") {
			var setHyphen1Val = $("#setHyphen1Val").val();
			
			if (setHyphen1Val == null || setHyphen1Val == "") {
				alert(lang.admin.etcConfig.alert.hyphen.emptyNum /* "첫번째 전화번호 표기 개수를 입력해주세요." */);
				return;
			}
			if (setHyphen1Val > 10) {
				alert(lang.admin.etcConfig.alert.hyphen.under10 /* "첫번째 전화번호 표기 개수는 10이하로 입력해주세요." */);
				return;
			}
			setHyphen1 = setHyphen1Val;
		}
		var setHyphen2 = $("#setHyphen2").val();
		
		var configValue = setHyphen1 + "," + setHyphen2;
		var dataStr = {
					"groupKey" : "hyphen"
				,	"configKey" : "hyphen"
				,	"configValue" : configValue
		};

		$.ajax({
			url:contextPath+"/updateOptionValue.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.etcConfig.alert.modifySuccess /* "변경 성공" */);		
				}else{
					alert(lang.admin.etcConfig.alert.modifyFail /* "변경 실패" */);
				}
			}
		});
	});
}

function insertPopOpen(){
	layer_popup('#insertConfig');
}

function etc_insert_action(){
	var gk = $('#gk').val().trim();
	var ck = $('#ck').val().trim();
	var cv = $('#cv').val().trim();
	
	if(gk.length == 0 || ck.length ==0 || cv.length == 0){
		alert('유효성검사 실패');
		return false;
	}
	
	var dataStr = {
			"groupKey" : gk,
			"configKey" : ck,
			"configValue" : cv
	}
	
	$.ajax({
		url:contextPath+"/insertEtc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				alert('추가성공');
			}
		}
	});
	
	
	
}