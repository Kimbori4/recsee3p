// 전역변수 설정
//var gridEtcConfigManage; // 그리드
var log = "", logRes = "";
var existValueCode;
var existValueVal; 
var existvalueName;
var existProductName;
var codeManageGrid;
var valueList;

var detailListArr;
// 최초 실행 함수
function comboLoad() {
	codeManageGrid = createGrid();
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
function selectOptionLoad(objSelect, comboType, comboType2, selectedIdx, selectedName, selectedValue) {
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
		success:function(jRes) {
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}

function formFunction() {
	$("#usePrefix").change(function() {
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
			success:function(jRes) {
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					if($("#usePrefix").val()=="Y") {
						$("#prefixBox").css("display","inline");
						$("#gridPrefixManage").css("display","inline");
					} else {
						$("#prefixBox").css("display","none");
						$("#gridPrefixManage").css("display","none");
					}
				}
			}
		});
	});
	
	$("#usePNumMasking").change(function() {
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
			success:function(jRes) {
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					if($("#usePNumMasking").val()=="Y") {
						$("#pNumMaskingBox").css("display","inline");
						pNumMaskingValLoad();
					} else {
						$("#pNumMaskingBox").css("display","none");
					}
				}
			}
		});
	});
	
	
	
	$("#useHyphen").change(function() {
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
			success:function(jRes) {
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					if($("#useHyphen").val()=="Y") {
						$("#setHyphenBox").css("display","inline");
					} else {
						$("#setHyphenBox").css("display","none");
					}
				}
			}
		});
	});
	
	$("#setHyphen1").change(function() {
		if($("#setHyphen1").val()=="Y") {
			$("#setHyphen1Val").css("display","inline");
		} else {
			$("#setHyphen1Val").val("");
			$("#setHyphen1Val").css("display","none");
		}		
	});
	
	// 콤보 로드
	selectOptionLoad($("#groupKey"),"etcConfig");
	$("#groupKey").change(function() {
		if ($(this).val() != "")
			selectOptionLoad($("#configKey"),"etcConfig",$(this).val());
		else {
			$("#configKey").children().remove()
			$("#configKey").append('<option value="" disabled selected>설정 키 선택</option>')
		}
	})
	
	
	$('#rs_script_step_detail_pk').change(function() {
		detailListChange();
	});
	
	
	// 팝업 내 수정
$("#modifyBtn").click(function(){
		
		
		
		var valuePk = $('#valuePk').val();
		var productCode = $('#productCode').val();
		var type = $('#productType').val();
		var valueCode = $('#valueCode').val();
		var valueName = $('#valueName').val();
		var valueVal = $('#valueVal').val();
		var updateDate = $('#reserveDate').val();
		
		//즉시반영 & 예약반영 타입  (1,2)
		var updateType = $('#modifyType :selected').val();
		
		if (valueVal == null || valueVal == ""){
			alert("가변값과 가변명을 확인해주세요.")
			return ;
		}
		if (valueName == null || valueName == ""){
			alert("가변값과 가변명을 확인해주세요.")
			return ;
		}
//		existValueVal
		if(existValueVal == valueVal){
			alert('가변값이 동일합니다. \n 변경후 수정해 주십시오.')
			return false;
		}

		if(confirm('가변값과 가변명을 수정하시겠습니까? \n\n 수정후에는 기존 기록이 남지 않습니다.')){			
			var strData = {
							"valuePk" : vlauePk
						,	"productCode" : productCode
						,	"type" : type
						,	"valueCode" : valueCode
						,	"valueName" : valueName
						,	"valueVal" : valueVal
						,	"updateType" : updateType
			}
			$.ajax({
				url:contextPath+"/updateScriptVariable.do",
//				url:contextPath+"/updateScriptVariable.do11",
				data:strData,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(jRes.resData.msg);
						gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
						layer_popup_close()
					}
				}
			});
			
		}

	});
}

function formSirealize(requestOrderBy){
	var groupKey = $("#groupKey").val()||"";
	var configKey = $("#configKey").val()||"";
	var configValue = $("#configValue").val()||"";
	if(configValue == null || configValue.length == 0 ){
		configValue = "";
	}
	var strData = "groupKey="+groupKey+"&configKey="+configKey+"&configValue="+configValue;
	console.log(strData);
	return encodeURI(strData);
}

function searchValue() {
	console.log("searchValue")
	codeManageGrid.clearAndLoad(contextPath+"/admin_product_list.xml?"+formSirealize())
	
}


// Create Grid
function createGrid() {
	objGrid = new dhtmlXGridObject("codeManageGrid");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	//objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 250, 5, "codeManagePaging", true);
	objGrid.setPagingWTMode(true,true,true,[100,250,500,1000]);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/admin_product_list.xml", function() {
		objGrid.aToolBar.setMaxOpen("pages", 5);
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count) {
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
			
			console.log(id);

			var flag = stepAppend(id);
			if( !flag ){
				alert('해당 상품은 단계가 추가되지않은 미등록 상품입니다.');
				return;
			}
			layer_popup('#modifyConfig');
		});
		

	}, 'xml');

    ui_controller();
    return objGrid;
}

function stepAppend(id){
	var flag =true;
	$.ajax({
		url:contextPath+"/stepAppend/"+id,
		data: "",
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				var arr = jRes.resData.stepList;

				arr.forEach(i => {
					var sufix = '<option name='+i.rScriptStepPk+' value = '+i.rScriptStepPk+'>';
					var prefix = '</option>';
					$('#rs_script_step_name').append(sufix+i.rScriptStepName+prefix);
				})
			}else if(jRes.success == "N"){
				detailListArr = null;
				flag = false;
			}
		}
	});
	return flag;
}

function searchScriptStep() {
	$('#rs_script_step_detail_pk').empty();
	var pk =$("#rs_script_step_name option:selected").val();
	$.ajax({
		url:contextPath+"/stepDetailAppend/"+pk,
		data: "",
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				var arr = jRes.resData.stepDetailList;
				console.log(arr);
				
				
				detailListArr = new Array();
				
				arr.forEach(i => {
					var sufix = '<option name='+i.rscriptDetailPk+' value = '+i.rscriptDetailPk+'>';
					var prefix = '</option>';
					$('#rs_script_step_detail_pk').append(sufix+i.rscriptDetailPk+prefix);
					
					var data = {"pk": i.rscriptDetailPk , "data": i};
					detailListArr.push(data);
				})
				detailSetting(arr[0])
				$('#rs_script_step_detail_pk').attr('disabled',false);
				detailInputNotDisabled()
				
			}else if(jRes.success == "N"){
				detailListArr = null;
				alert('스탭단계 내부에 스크립트가 존재하지않습니다.');
			}
		}
	});
}
	
function detailInputNotDisabled(){
	$('#rs_script_step_detail_type').attr('disabled',false);
	$('#rs_script_step_detail_if_case').attr('disabled',false);
	$('#rs_script_step_detail_if_case_code').attr('disabled',false);
	$('#rs_script_step_detail_if_case_detail_code').attr('disabled',false);
	$('#rs_script_step_detail_elt_case').attr('disabled',false);
	$('#rs_product_attributes2').attr('disabled',false);
	$('#rs_use_yn2').attr('disabled',false);
	$('#rs_product_attributes_ext').attr('disabled',false);
	$('#rs_script_step_detail_case_attributes').attr('disabled',false);
}
function detailInputDisabled(){
	$('#rs_script_step_detail_type').attr('disabled',true);
	$('#rs_script_step_detail_text').attr('disabled',true);
	$('#rs_script_step_detail_if_case').attr('disabled',true);
	$('#rs_script_step_detail_if_case_code').attr('disabled',true);
	$('#rs_script_step_detail_if_case_detail_code').attr('disabled',true);
	$('#rs_script_step_detail_elt_case').attr('disabled',true);
	$('#rs_product_attributes2').attr('disabled',true);
	$('#rs_use_yn2').attr('disabled',true);
	$('#rs_product_attributes_ext').attr('disabled',true);
	$('#rs_script_step_detail_case_attributes').attr('disabled',true);
}
	
	
	
	

function detailSetting(detailInfo){
	
	//타입
	$('#rs_script_step_detail_type').val(detailInfo.rscriptDetailType).prop('selected',true)
	//조건유무
	$('#rs_script_step_detail_if_case').val(detailInfo.rscriptDetailIfCase).prop('selected',true)
	//조건 상세
	if(detailInfo.rscriptDetailIfCase == 'N'){
		$('#rs_script_step_detail_if_case_detail').attr('disabled',true);
	}else{
		$('#rs_script_step_detail_if_case_detail').attr('disabled',false);
	}
	$('#rs_script_step_detail_if_case_detail').val(detailInfo.rscriptDetailIfCaseDetail);
	//사용유무
	$('#rs_use_yn2').val(detailInfo.ruseYn).prop('selected',true)
	
	$('#rs_script_step_detail_text').val(detailInfo.rscriptDetailText)
	
	$('#rs_script_step_detail_if_case_code').val(detailInfo.rscriptDetailIfCaseCode)
	$('#rs_script_step_detail_if_case_detail_code').val(detailInfo.rscriptDetailIfCaseDetailCode)
	
	$('#rs_product_attributes2').val(detailInfo.rproductAttributes);
	
	$('#rs_product_attributes_ext').val(detailInfo.rproductAttributesExt);
	
	if(detailInfo.rscriptDetailEltCase == null || detailInfo.rscriptDetailEltCase == undefined){
		$('#rs_script_step_detail_elt_case').val("");
	}else{
		$('#rs_script_step_detail_elt_case').val(detailInfo.rscriptDetailEltCase);
	}
	
	if(detailInfo.rscriptStepDetailCaseAttributes == null || detailInfo.rscriptStepDetailCaseAttributes == undefined){
		$('#rs_script_step_detail_case_attributes').val("");
	}else{
		$('#rs_script_step_detail_case_attributes').val(detailInfo.rscriptStepDetailCaseAttributes);
	}
}	
	
	


function VariableUpdate(){
	var data = detailVariableCheck();
	
	if(!data){
		return false;
	}
	
	$.ajax({
		url:contextPath+"/stepDetailUpdate",
		data: JSON.stringify(data),
		type:"PUT",
		dataType:"json",
		contentType : "application/json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				console.log(jRes);
				alert('수정에 성공하였습니다.');
				//팝업종료
			}else if(jRes.success == "N"){
				detailListArr = null;
				alert('수정에 실패 하였습니다.');
				
				//팝업종료
			}
		},
		complete : function() {
			closePop();
			
		}
	});
	

}

function detailVariableCheck(){
	
	var ifCaseDetail =  $("#rs_script_step_detail_if_case_detail").val().trim().length == 0 ? null : $("#rs_script_step_detail_if_case_detail").val().trim();
	var ifCaseCode = $("#rs_script_step_detail_if_case_code").val().trim().length == 0 ? null : $("#rs_script_step_detail_if_case_code").val().trim();
	var ifCaseDetailCode = $("#rs_script_step_detail_if_case_detail_code").val().trim().length == 0 ? null : $("#rs_script_step_detail_if_case_detail_code").val().trim();
	var detailProductAttr = $("#rs_product_attributes2").val().trim().length == 0 ? null : $("#rs_product_attributes2").val().trim();
	var productAttrExt = $("#rs_product_attributes_ext").val().trim().length == 0 ? null : $("#rs_product_attributes_ext").val().trim();
	var detailEltCase = $("#rs_script_step_detail_elt_case").val().trim().length == 0 ? null : $("#rs_script_step_detail_elt_case").val().trim();
	var detailCaseAttr = $("#rs_script_step_detail_case_attributes").val().trim().length == 0 ? null : $("#rs_script_step_detail_case_attributes").val().trim();
	//var rTimeTts = $("#rs_realtime_tts option:selected").val().trim();
	
	try{
		if(detailProductAttr != null){
			JSON.parse(detailProductAttr);
		}
	}catch(e){
		alert('detailProductAttr을 JSON형식에 맞춰주세요.')
		return false;
		
	}
	try{
		if(productAttrExt != null){
			JSON.parse(productAttrExt);
		}
	}catch(e){
		alert('productAttrExt을 JSON형식에 맞춰주세요.')
		return false;
	}
	try{
		if(detailCaseAttr != null){
			JSON.parse(detailCaseAttr);
		}
	}catch(e){
		alert('detailCaseAttr을 JSON형식에 맞춰주세요.')
		return false;
	}
	
	var data = {
					"detailType" : $("#rs_script_step_detail_type option:selected").val().trim(),
					"ifCase" : $("#rs_script_step_detail_if_case option:selected").val().trim(),
					"useYn" : $("#rs_use_yn2 option:selected").val().trim(),
					"ifCaseDetail" : ifCaseDetail,
					"ifCaseCode" : ifCaseCode,
					"ifCaseDetailCode" : ifCaseDetailCode,
					"detailProductAttr" : detailProductAttr,
					"productAttrExt" : productAttrExt,
					"detailEltCase" : detailEltCase,
					"detailCaseAttr" : detailCaseAttr
					//"rTimeTts" : rTimeTts
				}
	var info = {
			"detailPk" : $("#rs_script_step_detail_pk option:selected").val().trim(),
			"info" : data
	}
	return info;
}





function directTTS(pk) {
	$.ajax({
		//url:"http://localhost:10000/furence/script/tts/load2?pk="+pk,
		// 테스트
		url:"https://aiprecsyst.woorifg.com:8443/makeTTS/furence/script/tts/load2?pk="+fkList[0],
		// 운영VIP
//		url:"https://aiprecsys01.woorifg.com:8080/makeTTS/furence/script/tts/load2?pk="+pk,
		type: "GET",
		data: pk,
		dataType: "json",
		async: false,
		success: function(jRes) {
			alert("즉시반영성공")
		}
	});
}

function getFormatDate(date){
	var year = date.getFullYear();
	var month = (1+date.getMonth());
	month = month >= 10 ? month : '0' + month;
	var day = date.getDate()+1;
	day = day > 10 ? day : '0' +day;
	return year+'-'+month+'-'+day;
}

function typeChange() {
	var type = $('#modifyType :selected').val();
	
	if(type == 1){
		$('.dateClass').css("display","none")	
	}
	if(type == 2){
		$('.dateClass').css("display","block")	
	}
	
}

function dateChange(state) {
	var test = $('#reserveDate')
	var minDate = $('#reserveDate').attr("min");
	
	var date1 = new Date(test.val());
	var date2 = new Date(minDate);
	
	if(date1 < date2 || date1==null || date1.length ==0){
		alert("오늘 및 이전 날짜는 즉시반영을 이용해주십시오.");
		test.val(minDate);
	}
	
	
}

function VariableInsertPopUp() {
	layer_popup('#InsertConfig');
}

function searchProductCode(){
	valueList = null;
	var productCode = $('#insertProductCode').val().trim();
	
	if(productCode.length == 0){
		alert("상품코드를 입력해 주세요.")
		return;
	}
	
	if(isNaN(productCode)){
		alert('상품코드는 한글을 입력할 수 없습니다.');
		$('#insertProductCode').val("");
		return;
	}
	var strData = {
		"productCode" : productCode,
		"productType" : $('#insertSelectOption :selected').val()
	}
	
	$.ajax({
		url:contextPath+"/checkProductCode.do",
		data:strData,
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var product = jRes.resData.selectProduct;
				valueList = jRes.resData.valueList;
				enableChange();
				$('#insertProductCode').attr("readonly","readonly");
				$('#insertValueVal').removeAttr("disabled")
				$('#insertValueName').removeAttr("disabled")
				$('#insertValueCode').removeAttr("disabled")
				$('#insertrealTime').removeAttr("disabled")
				$('#insertProductName').val(product.rProductName);
			}
			if(jRes.success == "N") {
				alert(jRes.resData.msg);
				$('#insertValueCode').attr("disabled","disabled")
				$('#insertValueName').attr("disabled","disabled")
				$('#insertValueVal').attr("disabled","disabled")
				$('#insertrealTime').attr("disabled","disabled")
				disableChange();
				$('#insertProductName').val("");
			}
		}
	});
	
	
	
}

function enableChange(){
	$('#insertBtn').css("opacity","1.0");
	$('#insertBtn').css("pointer-events","auto");
	$('#insertBtn').css("cursor","pointer");
	
}
function disableChange(){
	$('#insertBtn').css("opacity","0.4");
	$('#insertBtn').css("pointer-event","none");
	$('#insertBtn').css("cursor","none");
}
function inputClear(){
	$('#insertProductCode').val("");
	$('#insertProductName').val("");
	$('#insertValueVal').val("")
	$('#insertValueName').val("")
	$('#insertValueCode').val("")
	$('#insertValueCode').attr("disabled","disabled")
	$('#insertValueName').attr("disabled","disabled")
	$('#insertValueVal').attr("disabled","disabled")
	$('#insertrealTime').attr("disabled","disabled")
	disableChange()
}

function VariableInsert(){
	var valueCode = $('#insertValueCode').val().trim();
	var valueName = $('#insertValueName').val().trim();
	var insertValueVal = $('#insertValueVal').val().trim();
	
	if(valueCode.length == 0 || valueName == 0 || insertValueVal.length == 0 ){
		alert("상품에 대한 가변정보를 필수로 입력해주시길 바랍니다.");
		return false;
	}
	
	
	for (var i = 0; i < valueList.length; i++) {
		var originalValueCode = valueList[i].rProductValueCode;
		var originalValueName = valueList[i].rProductValueName;
		
		if(originalValueCode == valueCode){
			alert("해당 상품코드에 대해 동일한 가변코드가  존재합니다.");
			return false;
		}
		if(originalValueName == valueName){
			alert("해당 상품코드에 대해 동일한 가변명이 존재합니다.");
			return false;
		}
		
	}
	
	var strData = {
			"productCode" : $('#insertProductCode').val().trim() ,
			"productType" : $('#insertSelectOption :selected').val(),
			"valueCode" : valueCode,
			"valueName" : valueName,
			"valueVal" : insertValueVal,
			"realTimeTTS" : $('#insertrealTime :selected').val()
			
	}
	
	$.ajax({
		url:contextPath+"/insertVariable.do",
		data:strData,
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(jRes.resData.msg);
				disableChange(); 
				inputClear();
				layer_popup_close()
				gridEtcConfigManage.clearAndLoad(contextPath+"/etc_config.xml?"+formSirealize())
			}
			if(jRes.success == "N") {
				alert(jRes.resData.msg);
				$('#insertValueCode').attr("disabled","disabled")
				$('#insertValueName').attr("disabled","disabled")
				$('#insertValueVal').attr("disabled","disabled")
				disableChange();
				$('#insertProductName').val("");
			}
		}
	});
	
	
	
	
}

function detailListChange(){
	var selectedPk = $("#rs_script_step_detail_pk option:selected").val();
	var detailInfo;
	detailListArr.forEach( i => {
		if(i.pk == selectedPk){
			detailInfo = i;
		}
	});
	detailSetting(detailInfo.data);
}

function closePop(){
	layer_popup_close()
	inputClear2()
	detailListArr = null;
	
}

function detailIfCaseChange(){
	var ifCase=$("#rs_script_step_detail_if_case option:selected").val();
	if(ifCase == 'Y'){
		$('#rs_script_step_detail_if_case_detail').attr('disabled',false)
		
	}else{
		$('#rs_script_step_detail_if_case_detail').prop('disabled','disabled');
		$('#rs_script_step_detail_if_case_detail').val('')
	}
}

//디테일 Append 팝업 .empty 
function inputClear2(){ 
    $("#rs_script_step_detail_type").val(""); 
    $("#rs_script_step_detail_if_case").val(""); 
    $("#rs_script_step_detail_if_case_detail").val(""); 
    $("#rs_use_yn2").val(""); 
    $("#rs_script_step_detail_if_case_code").val(""); 
    $("#rs_script_step_detail_if_case_detail_code").val(""); 
    $("#rs_product_attributes2").val(""); 
    $("#rs_product_attributes_ext").val(""); 
    $("#rs_script_step_detail_elt_case").val(""); 
    $("#rs_script_step_detail_case_attributes").val(""); 
    $("#rs_script_step_name").val(""); 
    $("#rs_script_step_detail_pk").val(""); 
    $('#rs_script_step_detail_text').val("");
    detailInputDisabled();
    $('#rs_script_step_name').empty();
    $('#rs_script_step_detail_pk').empty();
    $('#rs_script_step_detail_pk').attr('disabled',true);
}

//상품 INSERT
function adminProductInsert(){
	//상품 타입
	var productType = $("#rs_product_type option:selected").val();
	//상품 코드
	var productCode = $("#rs_product_code").val();
	//상품명
	var productName =$("#rs_product_name").val();
	//사용여부
	var useYn = $("#rs_use_yn option:selected").val();
	//그룹여부
	var groupYn= $("#rs_group_yn option:selected").val();
		if(groupYn.trim()=="N") {
			$("#rs_group_code").attr("disabled","disabled");
		}
	//대표코드
	var groupCode=$("#rs_group_code").val();
	if(groupCode.trim().length==0) {
		groupCode = null;
	}
	//sysdis
	var sysdisType=$("#rs_sysdis_type option:selected").val();
	//attributes
	var productAttr=$("#rs_product_attributes").val();
	if(productType==null||productType.trim().length==0||productCode==null||productCode.trim().length==0||productName==null||productName.trim().length==0) {
		alert("상품타입, 상품코드, 상품명은 필수로 기입해야 합니다.");
		return;
	}
	if(productAttr.trim().length > 0 && productAttr != null){
		try{
			JSON.parse(productAttr);
		}catch(e){
			if(productAttr==null || productAttr.trim().length==0) {
				return;
			}else{
			alert('상품속성을 JSON 형식에 맞게 작성해 주세요.')
			return false;
			}
		}
	}else{
		productAttr = null;
	}
	$.post('/recsee3p/adminProductInsert',{productType:productType,productCode:productCode,productName:productName,useYn:useYn,groupYn:groupYn,groupCode:groupCode,sysdisType:sysdisType,productAttr:productAttr},
			function(result){
				if(result=='ok') {
					location.reload();
				}
	},'json')
}

//중복 체크
/*function checkProductCode(){
	var productType = $("#rs_product_type option:selected").val();
	var productCode = $("#rs_product_code").val();
	$.post('/recsee3p/checkProductCode',{productType:productType,productCode:productCode},
			function(result){
				if(result=='ok') {
					alert("사용 가능 상품코드입니다.")
				}else {
					alert("사용 가능 상품코드입니다.");
					return false;
				}
	},'text')
	
}*/

function SinkInsert() {
	layer_popup('#InsertSink');
}

function SinkInsertAction(){
	var type = $('#sink_type :selected').val().trim();
	var attr = $('#r_prd_attr_yn :selected').val().trim();
	var colCode = $('#r_col_cd').val().trim();
	var colName = $('#r_col_nm').val().trim();
	var colOrder = $('#r_order').val().trim();
	
	var data = {
				"type" : type,
				"attr" : attr,
				"colCode" : colCode,
				"colName" : colName,
				"colOrder" : colOrder
				};
	
	$.ajax({
		url:contextPath+"/inserSinkTable",
		data: JSON.stringify(data),
		type:"POST",
		dataType:"json",
		contentType : "application/json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				console.log(jRes);
				alert('수정에 성공하였습니다.');
				//팝업종료
			}else if(jRes.success == "N"){
				detailListArr = null;
				alert('수정에 실패 하였습니다.');
				
				//팝업종료
			}
		},
		complete : function() {
			closePop();
		}
	});
	
}

function productDelete(pk) {
	var flag = confirm('상품을 정말삭제 하시겠습니까?\n삭제이후 복구 불가능합니다.');
	if(!flag){
		return false;
	}
	
	$.ajax({
		url:contextPath+"/deleteProduct/"+pk,
		data: "",
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				console.log(jRes);
				alert('삭제에 성공하였습니다.');
				codeManageGrid.clearAndLoad(contextPath+"/admin_product_list.xml");
				//팝업종료
			}else if(jRes.success == "N"){
				detailListArr = null;
				alert('삭제에 실패 하였습니다.');
				codeManageGrid.clearAndLoad(contextPath+"/admin_product_list.xml");
				
				
				//팝업종료
			}
		},
		complete : function() {
			closePop();
		}
	});
	
}
