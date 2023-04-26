// 전역변수 설정
var gridLogoManage; // 그리드


window.onload = function() {
	gridLogoManageLoad() // 로고 관리 그리드 로드
	
	// 팝업창 로고 등록
	$("#uploadLogoBtn").click(function(){
		onUploadLogoProc();
	});
	
	// 로고 변경 팝업창 콤보 값 바뀌면
	$("#logoChangeUse").change(function(){
		var logoChangeUseYn = $("#logoChangeUse").val();
		if(logoChangeUseYn == "Y") {
			$(".upload_disabled").removeClass("ui_input_hasinfo");
			$(".upload_disabled").attr("disabled",false);
		} else {
			$(".upload_disabled").addClass("ui_input_hasinfo");
			$(".upload_disabled").attr("disabled",true);
		}
	});
	
	ui_controller();
}

// 로고 관리 그리드 로드
function gridLogoManageLoad() {
    // 로고 관리 그리드
	objGrid = new dhtmlXGridObject("gridLogoManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/logo_list.xml", function(){
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
	}, 'xml')
	
	objGrid.attachEvent("onXLS", function(){
		progress.on()
	});
	
	objGrid.attachEvent("onXLE", function(grid_obj,count){
		ui_controller();
		progress.off();

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
			$(top.window).trigger("resize");
		});
	});

	objGrid.attachEvent("onRowSelect", function(id,ind){
	    return;
	});
	
	// 로우 더블클릭 시 수정 팝업 오픈
	objGrid.attachEvent("onRowDblClicked", function(id,ind){

		var logoLocation =  this.cells(id, 0).getValue();
		var	logoChangeUseYn = this.cells(id, 1).getValue();
		if(logoLocation == "login") {
			$("#logoType_hidden").val("login");
			$("#uploadLogoPop").find("p").text(convertLanguage("admin.subNumber.label.loginLogo")/*"로그인 페이지 로고 변경"*/)
		} else {
			$("#logoType_hidden").val("main");
			$("#uploadLogoPop").find("p").text(convertLanguage("admin.subNumber.label.mainLogo")/*"메인 페이지 로고 변경"*/)
		}
		$("#logoChangeUse").val(logoChangeUseYn);
		$("#logoChangeUse").trigger("change");
		
		layer_popup('#uploadLogoPop');
	});
    ui_controller();
}



function onUploadLogoProc(){
	if(confirm(convertLanguage("admin.subNumber.confirm.changeLogo")/*"로고를 변경하시겠습니까?"*/)){	
		var logoType = $("#logoType_hidden").val();
		var logoChangeUseYn  = $("#logoChangeUse").val();
		var logoPath = $('#upLoadFile_hidden1').val();
		var logoName = $('#upLoad_name1').val();
		
		if (logoChangeUseYn == "N") { // 기본 로고 선택시
			logoName = "";
		} else { // 로고 변경시
			if (logoPath == null || logoPath == "") {
	    		alert(convertLanguage("admin.subNumber.alert.registerLogo")/*"변경할 로고 이미지를 등록해주세요."*/);
	    		return;
	    	} else if (!chkImgFomat(logoPath)) {
	    		alert(convertLanguage("admin.subNumber.alert.logo.onlyPng")/*"파일 업로드는 png형식만 가능합니다."*/);
	    		$("#upLoadFile_hidden1").val("");  
	    		$("#upLoad_name1").val(convertLanguage("admin.subNumber.alert.SelectLogo")/*"파일을 선택하세요."*/);    		
	    		return;
	    	}	
	    }
		
		var form = $("#uploadLogoFrm")[0];
		var formData = new FormData(form);
		formData.append("logoType", logoType);
		// 이미지 업로드
		$.ajax({
			url:contextPath+"/logoImgUpload.do",
			processData:false,
			contentType:false,
			data:formData,
			type:"POST",
			success:function(jRes){
				
			}
		});

		var dataStr = {
				"logoType"				: logoType,
				"logoChangeUseYn"		: logoChangeUseYn,
				"logoName"				: logoName
		};
		
		// DB에 저장
		$.ajax({
			url:contextPath+"/updateLogoInfo.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(convertLanguage("admin.subNumber.alert.success.registerLogo")/*"로고가 등록되었습니다."*/);
					layer_popup_close();
					location.reload();
				}else{
					alert(convertLanguage("admin.subNumber.alert.fail.registerLogo")/*"로고 등록에 실패하였습니다."*/);
				}
			}
		});
	}
};

//파일 형식 검사
function chkImgFomat(val) {
	var chkVal = false;
	var dotIdx = val.lastIndexOf(".");
	val = val.substring(dotIdx).toLowerCase();
	if (val == ".png") {
		chkVal = true;
	}
	return chkVal; 
}