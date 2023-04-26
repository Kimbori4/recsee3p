$(function(){
	
	// 지속 감청 입력 하고 체크 안할시 알림 창 알림
	$('.listenAlwaysPannel').mouseleave(function(){
		if($('#custPhoneContinue').val() != "" && ($('.office_stay_check').is(":checked") == false && $('.custPhoneContinueChk').is(":checked") == false )){
			alertText("",lang.monitoring.alert.msg21);
			return false;
		}
		if($('#custPhoneContinue').val() == "" && ($('.office_stay_check').is(":checked") == true || $('.custPhoneContinueChk').is(":checked") == true )){
			alertText("",lang.monitoring.alert.msg22);
			return false;
		}
	});
	
	$('#btnListenAlwayExit').click(function(){
		if($('#custPhoneContinue').val() != "" && ($('.office_stay_check').is(":checked") == false && $('.custPhoneContinueChk').is(":checked") == false )){
			alertText("",lang.monitoring.alert.msg21);
			return false;
		}
		if($('#custPhoneContinue').val() == "" && ($('.office_stay_check').is(":checked") == true || $('.custPhoneContinueChk').is(":checked") == true )){
			alertText("",lang.monitoring.alert.msg22);
			return false;
		}
	})
	
	// 지속 감청 저장 값 
	$.ajax({
		url: contextPath+"/monitoring/realtimeSetting.do",
		data: {},
		type: "POST",
		dataType: "json",
		async: false,
		success: function(jRes) {
			 var settingYn = jRes.resData.settingYn;
			 var settingType = jRes.resData.settingType;
			 var settingNum = jRes.resData.settingNum;
			 
			 if(settingYn == 'Y'){
				 $('#statusSave').prop("checked",true);
				 $('#custPhoneContinue').val(settingNum);
				 if(settingType == 'C'){
					 $('.custPhoneContinueChk').prop("checked",true)
				 }else if(settingType == 'E'){
					 $('.office_stay_check').prop("checked",true);
				 }else{
					$('.office_stay_check').prop("checked",false)
					$('.custPhoneContinueChk').prop("checked",false)
				 }
			 }else{
				 $('#statusSave').prop("checked",false)
				 setTimeout(function(){
					 $('#statusSave').parent().find('.onoffswitch-switch').text('off');
				 },500);
			 }
		}
	});
	
	//	상태 저장
	$('#statusSave').change(function(){
		var check;
		if($(this).is(':checked')){
			check = 'Y';
		}else{
		   check = 'N';
		}
		$.ajax({
			url: contextPath+"/monitoring/realtimeSetting.do",
			data: {
				settingYn : check 
			},
			type: "POST",
			dataType: "json",
			async: false,
			success: function(jRes) {
				if(check == 'Y'){
					 var settingYn = jRes.resData.settingYn;
					 var settingType = jRes.resData.settingType;
					 var settingNum = jRes.resData.settingNum;
					 
					 if(settingYn == 'Y'){
						 $('#statusSave').prop("checked",true);
						 $('#custPhoneContinue').val(settingNum);
						 if(settingType == 'C'){
							 $('.custPhoneContinueChk').prop("checked",true)
						 }else if(settingType == 'E'){
							 $('.office_stay_check').prop("checked",true);
						 }else{
							$('.office_stay_check').prop("checked",false)
							$('.custPhoneContinueChk').prop("checked",false)
						 }
						 try{
							 //		카드 뷰 일때
				        	if($('.custPhoneContinueChk').is(":checked") == false && $('#statusSave').is(":checked") == true){
				        		if($("#"+$('#custPhoneContinue').val()).find(".onoffswitch-checkbox").is(":checked") == false){
				        			$("#"+$('#custPhoneContinue').val()).find(".onoffswitch-label").click();
				        		}
				        	}
						 }catch(e){}
					 }else{
						 $('#statusSave').prop("checked",false)
					 }
				}
			}
		});
	});

	$('#codeFilterResizeOption').html($('#codeFilter option:selected').text());
	
	$('#codeFilter').width($('#codeFilterResize').width()+20);
	
	$('#codeFilter').change(function(){
		$('#codeFilterResizeOption').html($('#codeFilter option:selected').text());
		$(this).width($('#codeFilterResize').width()+20);
		
		setTimeout(function(){
			$('.select2-search__field').on('keyup',function(){
				if($('#codeFilter option:selected').val() == "searchAgentNum" || $('#codeFilter option:selected').val() == "searchAgentExt" ){
					var value = this.value;
		        	// 숫자 이외 걸러 내기
		        	value = value.replace(/[^0-9]/g,'');
		        	this.value = value;	
				}
			})
		},500);
	})
})


/*
 |	@david	
 |	@param title 알림창 제목
 |	@param contents 알림창 콘텐츠 들어갈 부분 내용
 |
 */

function alertText(title,contents){
	$("#dialog-confirm").dialog({
		resizable : false,
		height : "auto",
		width : 400,
		modal : true,
		buttons: {
			Cancel : function(){
				$(this).dialog("close");
			}
		}
	})
	
	$(".ui-dialog-title").html("");
	$(".ui-dialog-titlebar").css({"background":"#2d71c4","color":"#eee"});
	$(".ui-dialog-buttonset .ui-button").css({"background":"#d99103","color":"#eee"});
	$(".alert_contents").html("");
	
	if(title == ""){
		title = "　";
	}
	
	$(".ui-dialog-title").append(title);
	$(".alert_contents").append("<span class='ui-icon ui-icon-alert' style='margin-right:10px;'></span>"+contents);
	$("#dialog-confirm p").removeClass("displaynone");
}

function	realtimeinsertType(str){
	$.ajax({
		url: contextPath+"/monitoring/realtimeSetting.do",
		data: {
			settingType : str 
		},
		type: "POST",
		dataType: "json",
		async: false,
		success: function(jRes) {
			if(check == 'Y'){
			}
		}
	});
}