// ��������
var backMenuUserManageGrid; // �׸���
var treeView;
var check=0;
var addList = new Array;
var backList = new Array;
var rsfftUrlCheck = 0;

//	�ε� �Լ�
function backupRecfileManageLoad(){

	treeView = new dhtmlXTreeObject("backMenuTreeViewAgent","100%","100%",0);
	treeView.attachEvent("onXLS", function(){
		check=check+1;
		progress.on()
	});
	treeView.setImagePath("../resources/component/dhtmlxSuite/skins/skyblue/imgs/dhxtree_skyblue/");
	treeView.enableThreeStateCheckboxes(true);
	treeView.enableSmartXMLParsing(true);
	treeView.load(contextPath+"/AgentTreeView.xml?aUser=N");
	treeView.attachEvent("onXLE", function(grid_obj,count){
		check=check-1;
		if(check==0){
			progress.off()
		}
	});


	backMenuUserManageGrid = createGrid("backMenuUserManageGrid", "bakMenuInfo_list","",[],[]);
	backMenuUserManageGrid.clearAndLoad(contextPath+"/bakMenuInfo_list.xml"+formToSerialize())
	
	gridFunction();
	formFunction();
	backRecfileYNLoad();	
	
	$("#sDatebackup, #eDatebackup").change(function(){fromTo($("#sDatebackup_Date"),$("#eDatebackup_Date"),this)})
	$("#sDatebackup, #eDatebackup").keyup(function(){if($("#sDatebackup_Date").val().replace(/[:-]/g,'').length==8&&$("#eDatebackup_Date").val().replace(/[:-]/g,'').length==8) fromTo($("#sDatebackup_Date"),$("#eDatebackup_Date"),this)})
	
	$("#sDatebackup_Date, #eDatebackup_Date").change(function(){fromTo($("#sDatebackup_Date"),$("#eDatebackup_Date"),this)})
	$("#sDatebackup_Date, #eDatebackup_Date").keyup(function(){if($("#sDatebackup_Date").val().replace(/[:-]/g,'').length==8&&$("#eDatebackup_Date").val().replace(/[:-]/g,'').length==8) fromTo($("#sDatebackup_Date"),$("#eDatebackup_Date"),this)})
	
	
	var firstDateSetting = true;
	if (firstDateSetting) {
		datepickerSetting(unqLang,'#sDatebackup, #eDatebackup');
		datepickerSetting(unqLang,'#sDatebackup_Date, #eDatebackup_Date');
		firstDateSetting = false;
	}
	
	
	$("#sDatebackup").click(function() {
		if (!firstDateSetting) {
			var sDate = $("#sDatebackup").val();
			sDate = sDate.substring(0,4) + "-" + sDate.substring(4,6) + "-" + sDate.substring(6);
			if (sDate.length == 10) {
//				datepickerSetting(locale,'#sDate',sDate);
        $("#sDatebackup").datepicker().datepicker("setDate", sDate);
			}
		}
	});
		
	$("#eDatebackup").click(function() {
		if (!firstDateSetting) {
			var eDate = $("#eDatebackup").val();
			eDate = eDate.substring(0,4) + "-" + eDate.substring(4,6) + "-" + eDate.substring(6);
			if (eDate.length == 10) {
//				datepickerSetting(locale,'#eDate',eDate);
        $("#eDatebackup").datepicker().datepicker("setDate", eDate);
			}
		}
	});
	
	$("#sDatebackup_Date").click(function() {
		if (!firstDateSetting) {
			var sDate = $("#sDatebackup_Date").val();
			sDate = sDate.substring(0,4) + "-" + sDate.substring(4,6) + "-" + sDate.substring(6);
			if (sDate.length == 10) {
//				datepickerSetting(locale,'#sDate',sDate);
				$("#sDatebackup_Date").datepicker().datepicker("setDate", sDate);
			}
		}
	});
		
	$("#eDatebackup_Date_Date").click(function() {
		if (!firstDateSetting) {
			var eDate = $("#eDatebackup_Date").val();
			eDate = eDate.substring(0,4) + "-" + eDate.substring(4,6) + "-" + eDate.substring(6);
			if (eDate.length == 10) {
//				datepickerSetting(locale,'#eDate',eDate);
				$("#eDatebackup_Date").datepicker().datepicker("setDate", eDate);
			}
		}
	});
	
	if(telnoUse=='Y'){
		$(".group_tree_pannel").hide();
		$("#bg_name").hide();
		$("#mg_name").hide();
		$("#sg_name").hide();
		
		$(".bg_title").hide();
		$(".mg_title").hide();
		$(".sg_title").hide();
		
		$(".delRecfile_manage_pannel").width("100%");
	}else{
		//$(".delRecfile_manage_pannel").width("calc(99.9% - 256px) !important");
	}
	
	setInterval(function(){
		$.ajax({
			url:contextPath+"/sendBackupState.do",
			data: "{ 1 : 1}",
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					var backSateCheck = jRes.resData.backSateCheck;
				}
				
				for(var i=0;i < backSateCheck.length;i++){
					
					var seqData = backSateCheck[i][0];
					
					if(backSateCheck[i][1] == 'Y'){
						$(".nowBackStart"+seqData).show();
						$(".nowDownStart"+seqData).show();
					}else{
						$(".nowBackStart"+seqData).hide();
						$(".nowDownStart"+seqData).hide();
					}
					
					$(".backrunTime"+seqData).html(backSateCheck[i][2]);
					$(".backupState"+seqData).html(backSateCheck[i][3]);
					$(".backupStartTime"+seqData).html(backSateCheck[i][4]);					
					$(".backupEndTime"+seqData).html(backSateCheck[i][5]);
					
					
					
				}
			}
		});
	}, 3000);
	
}

// �׸��� ����
function createGrid(objGrid, url, strData, hiddenColumn, showColumn){
	var tempGridName = objGrid;
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	objGrid.attachEvent("onXLS", function(){
		check=check+1;
		progress.on()
	});
	
	// �Ľ̿Ϸ�
	objGrid.attachEvent("onXLE", function(grid_obj,count){
		ui_controller();
		check=check-1;
		if(check==0)
			progress.off();
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});

	});
	
	// �ο� ��Ŭ
	objGrid.attachEvent("onRowDblClicked", function(id,cInd){
		
		$("#addBtn").hide();
		$("#modifyBtn").show();
		$("#backBtn").hide();
		
		var bg_name = objGrid.cells(id,objGrid.getColIndexById("bgName")).getValue(); //bgName
		var mg_name = objGrid.cells(id,objGrid.getColIndexById("mgName")).getValue(); //mgName
		var sg_name = objGrid.cells(id,objGrid.getColIndexById("sgName")).getValue(); //sgName
		
		if(telnoUse=='Y'){
			bg_name = 'B000';
			mg_name = 'M000';
			sg_name = 'S000';
		}

		var back_type = objGrid.cells(id,objGrid.getColIndexById("backTypeDb")).getValue();
		var log_type = objGrid.cells(id,objGrid.getColIndexById("logTypeDB")).getValue();
		
		var scheCheck_Type	= objGrid.cells(id,objGrid.getColIndexById("backupSchedulDb")).getValue();
		var back_Week		= objGrid.cells(id,objGrid.getColIndexById("weekDb")).getValue();
		var back_Scheduler	= objGrid.cells(id,objGrid.getColIndexById("day")).getValue();
		var sche_Time		= objGrid.cells(id,objGrid.getColIndexById("time")).getValue();
		
		var PreviousData_Third	= objGrid.cells(id,objGrid.getColIndexById("backupSelectDb")).getValue();
		
		var PreviousData_Value = objGrid.cells(id,objGrid.getColIndexById("backupData")).getValue();
			
		var back_path = objGrid.cells(id,objGrid.getColIndexById("backupPath")).getValue()
		
		var back_seq = objGrid.cells(id,objGrid.getColIndexById("seq")).getValue();

		var decType = objGrid.cells(id,objGrid.getColIndexById("decTypeDB")).getValue();
		var urlUpdateType = objGrid.cells(id,objGrid.getColIndexById("urlUpdateTypeDB")).getValue();
		var overWriteType = objGrid.cells(id,objGrid.getColIndexById("overWriteTypeDB")).getValue();
		var conformityType = objGrid.cells(id,objGrid.getColIndexById("conformityTypeDB")).getValue();
		var dualBackupType = objGrid.cells(id,objGrid.getColIndexById("dualBackupTypeDB")).getValue();
		
		$("#bg_name").val(bg_name);
		$("#mg_name").val(mg_name);
		$("#sg_name").val(sg_name);
		

		$("#dec_Type").val(decType);
		$("#urlUpdate_Type").val(urlUpdateType);
		$("#overWrite_Type").val(overWriteType);
		$("#conformity_Type").val(conformityType);
		$("#dualBackup_Type").val(dualBackupType);
		
		$("#back_type").val(back_type);
		$("#log_type").val(log_type);
		
		$("#Schecheck_Type").val(scheCheck_Type) 
		$("#Schecheck_Type").trigger('change');
		$("#back_Week").val(back_Week)
		$("#back_Scheduler").val(back_Scheduler)
		$("#sche_Time").val(sche_Time)
		
		
		$("#PreviousData_Third").val(PreviousData_Third);
		$("#PreviousData_Third").trigger('change');
		
		if(PreviousData_Third == 'mon'){
			$("#PreviousData_Month").val(PreviousData_Value);
		}else if(PreviousData_Third == 'day'){
			$("#PreviousData_Day").val(PreviousData_Value);
		}else if(PreviousData_Third == 'fromTo'){
			var sdateValue = PreviousData_Value.split(':')[0];
			var edateValue = PreviousData_Value.split(':')[1];
			
			sdateValue = sdateValue.substring(0,4)+'-'+sdateValue.substring(4,6)+'-'+sdateValue.substring(6,8);
			edateValue = edateValue.substring(0,4)+'-'+edateValue.substring(4,6)+'-'+edateValue.substring(6,8);
			
			$("#sDatebackup_Date").val(sdateValue);
			$("#eDatebackup_Date").val(edateValue);
			
		}
		else{
			$("#PreviousData_Time").val(PreviousData_Value);
		}
		$("#back_path").val(back_path);

		$("#backRecfileInfoSeq").val(back_seq);
		
		layer_popup('#backRecfileInfoModify');
	});
	
	
	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData),function(){

		//üũ�ڽ� ��ü ����
		objGrid.attachEvent("onHeaderClick",function(ind, obj){
    		if(ind == 0 && obj.type == "click") {
    			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
    				this.setCheckedRows(0, 1);
    				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
    			} else {
    				this.setCheckedRows(0, 0);
    				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
    			}
    		} else {
    			return true;
    		}
    	});
    		
		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		ui_controller();
	});
	
	return objGrid;
}

function backRecfileYNLoad() {
	var dataStr = {
			"groupKey" : "SYSTEM"
		,	"configKey" : "RECSEE_BACKUP"
	};
	
	$.ajax({
		url:contextPath+"/selectOptionYN.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#recseeBackUp").val(jRes.resData.configValue).attr('selected', true);
				if(jRes.resData.configValue=="Y"){
					$(".backOptionWrap").css("display","inline");	
					$("#rowDisabled").css("display","none");	
					$("#backMenuRangeSaveBtn").show();
					$("#backMenuCheckedRangeDeleteBtn").show();
				}else{
					$(".backOptionWrap").css("display","none");
					$("#rowDisabled").css("display","inline");		
					$("#backMenuRangeSaveBtn").hide();
					$("#backMenuCheckedRangeDeleteBtn").hide();
				}
			}
		}
	});
}


//�׸��� ���� �Լ�
function gridFunction(){
}

//�� �Լ�
function formFunction(){
	
	
	$('#SchecheckType').change(function(){		
		if($(this).val()=="D"){	
			$('.backupWeekBox').css("display","none");
			$('#backWeek').css("display","none");	
			$('#backWeek').val("*");	
			$(".backupDayBox").css("display","none");
			$('#backScheduler').css("display","none");	
			$('#backScheduler').val("*");
			$('.backupHourBox').css("display","inline");	
			$('#scheTime').css("display","inline");
			$('#scheTime').val("");
		}else if($(this).val()=="W"){	
			$('.backupWeekBox').css("display","inline");
			$('#backWeek').css("display","inline");	
			$('#backWeek').val("*");	
			$(".backupDayBox").css("display","none");
			$('#backScheduler').css("display","none");	
			$('#backScheduler').val("*");
			$('.backupHourBox').css("display","inline");	
			$('#scheTime').css("display","inline");
			$('#scheTime').val("");
		}else if($(this).val()=="N"){	
			$('.backupWeekBox').css("display","none");
			$('#backWeek').css("display","none");	
			$('#backWeek').val("*");	
			$(".backupDayBox").css("display","none");
			$('#backScheduler').css("display","none");	
			$('#backScheduler').val("*");			
			$('.backupHourBox').css("display","none");	
			$('#scheTime').css("display","none");			
			$('#scheTime').val("*");
		}else{
			$('.backupWeekBox').css("display","none");
			$('#backWeek').css("display","none");	
			$('#backWeek').val("*");	
			$(".backupDayBox").css("display","inline");
			$('#backScheduler').css("display","inline");	
			$('#backScheduler').val("0");
			$('.backupHourBox').css("display","inline");
			$('#scheTime').css("display","inline");
			$('#scheTime').val("");
		}
	})
	
	$("#PreviousDataThird").on("change", function(){
		var PreviousDataThird = $("#PreviousDataThird").val();
		switch(PreviousDataThird){
		case "mon":			
			$("#PreviousDataMonth").val('').css("display","inline");
			$("#PreviousDataDay").val('').css("display","none");
			$("#PreviousDataTime").val('').css("display","none");
			$("input#sDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").hide()
			$("input#eDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").hide()
			break;
		case "day":
			$("#PreviousDataMonth").val('').css("display","none");
			$("#PreviousDataDay").val('').css("display","inline");
			$("#PreviousDataTime").val('').css("display","none");
			$("input#sDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").hide()
			$("input#eDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").hide()
			break;
		case "hour":
			$("#PreviousDataMonth").val('').css("display","none");
			$("#PreviousDataDay").val('').css("display","none");
			$("#PreviousDataTime").val('').css("display","inline");
			$("input#sDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").hide()
			$("input#eDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").hide()
			break;
		case "fromTo":
			$("#PreviousDataMonth").val('').css("display","none");
			$("#PreviousDataDay").val('').css("display","none");
			$("#PreviousDataTime").val('').css("display","none");
			$("input#sDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").show()
			$("input#eDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").show()
			break;
		}		
	});
	
	$('#Schecheck_Type').change(function(){		
		if($(this).val()=="D"){	
			$('.backup_WeekBox').css("display","none");
			$('#back_Week').css("display","none");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","none");
			$('#back_Scheduler').css("display","none");	
			$('#back_Scheduler').val("*");
			$('.backup_HourBox').css("display","inline");
			$('#sche_Time').css("display","inline");	
			$('#sche_Time').val("");
		}else if($(this).val()=="W"){	
			$('.backup_WeekBox').css("display","inline");
			$('#back_Week').css("display","inline");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","none");
			$('#back_Scheduler').css("display","none");	
			$('#back_Scheduler').val("*");
			$('.backup_HourBox').css("display","inline");
			$('#sche_Time').css("display","inline");	
			$('#sche_Time').val("");
		}else if($(this).val()=="N"){	
			$('.backup_WeekBox').css("display","none");
			$('#back_Week').css("display","none");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","none");
			$('#back_Scheduler').css("display","none");	
			$('#back_Scheduler').val("*");
			$('.backup_HourBox').css("display","none");
			$('#sche_Time').css("display","none");	
			$('#sche_Time').val("");
		}else{
			$('.backup_WeekBox').css("display","none");
			$('#back_Week').css("display","none");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","inline");
			$('#back_Scheduler').css("display","inline");	
			$('#back_Scheduler').val("0");
			$('.backup_HourBox').css("display","inline");
			$('#sche_Time').css("display","inline");	
			$('#sche_Time').val("");
		}
	})
	
	$("#PreviousData_Third").on("change", function(){
		var PreviousDataThird = $("#PreviousData_Third").val();
		switch(PreviousDataThird){
		case "mon":			
			$(".PreviousData_MonthBox").css("display","inline");
			$("#PreviousData_Month").val('').css("display","inline");
			$(".PreviousData_DayBox").css("display","none");
			$("#PreviousData_Day").val('').css("display","none");
			$(".PreviousData_TimeBox").css("display","none");
			$("#PreviousData_Time").val('').css("display","none");
			$(".from_Date").css("display","none");
			$("#sDatebackup_Date").val('').css("display","none");
			$(".to_Date").css("display","none");
			$("#eDatebackup_Date").val('').css("display","none");
			break;
		case "day":
			$(".PreviousData_MonthBox").css("display","none");
			$("#PreviousData_Month").val('').css("display","none");
			$(".PreviousData_DayBox").css("display","inline");
			$("#PreviousData_Day").val('').css("display","inline");
			$(".PreviousData_TimeBox").css("display","none");
			$("#PreviousData_Time").val('').css("display","none");
			$(".from_Date").css("display","none");
			$("#sDatebackup_Date").val('').css("display","none");
			$(".to_Date").css("display","none");
			$("#eDatebackup_Date").val('').css("display","none");
			break;
		case "hour":
			$(".PreviousData_MonthBox").css("display","none");
			$("#PreviousData_Month").val('').css("display","none");
			$(".PreviousData_DayBox").css("display","none");
			$("#PreviousData_Day").val('').css("display","none");
			$(".PreviousData_TimeBox").css("display","inline");
			$("#PreviousData_Time").val('').css("display","inline");
			$(".from_Date").css("display","none");
			$("#sDatebackup_Date").val('').css("display","none");
			$(".to_Date").css("display","none");
			$("#eDatebackup_Date").val('').css("display","none");
			break;
		case "fromTo":
			$(".PreviousData_MonthBox").css("display","none");
			$("#PreviousData_Month").val('').css("display","none");
			$(".PreviousData_DayBox").css("display","none");
			$("#PreviousData_Day").val('').css("display","none");
			$(".PreviousData_TimeBox").css("display","none");
			$("#PreviousData_Time").val('').css("display","none");
			$(".from_Date").css("display","inline");
			$("#sDatebackup_Date").val('').css("display","inline");
			$(".to_Date").css("display","inline");
			$("#eDatebackup_Date").val('').css("display","inline");
			break;
		}		
	});
	
	$('#BackupcheckType').change(function(){		
		if($(this).val()=="mon"){
			$('.backupDate1').css("display","block");			
			$('#backdate1').css("display","block");
			$('.backupMonthBox').val("*");
			$('.backupWeekBox').css("display","none");
			$('.backupWeekBox').val("*");
			$('.backupHourBox').val("0");
		}else if($(this).val()=="day"){
			$('.backupDate1').css("display","none");
			$('.#backdate1').val("*");
			$('.backupDate1').css("display","block");			
			$('#backdate1').css("display","block");
		}else{
			$('.backupWeekBox').css("display","block");
			$('.backupMonthBox').css("display","none");
			$('.backupMonthBox').val("*");
			$('.backupDayBox').css("display","none");
			$('.backupDayBox').val("*");
			$('.backupHourBox').val("0");
		}
	})
	
	// ���� ���� ���� ��� ���� - ETC_CONFIG�� ����
	$("#recseeBackUp").change(function(){
		var strData = {
				"groupKey" : "SYSTEM"
			,	"configKey" : "RECSEE_BACKUP"
			,	"configValue" : $("#recseeBackUp").val()
		}
		
		$.ajax({
			url:contextPath+"/updateOptionValue.do",
			data:strData,
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB�� ��ȸ�� ������ ������
				if(jRes.success == "Y") {
					if($("#recseeBackUp").val()=="Y"){
						$(".backOptionWrap").css("display","inline");	
						$("#rowDisabled").css("display","none");	
						$("#backMenuRangeSaveBtn").show();
						$("#backMenuCheckedRangeDeleteBtn").show();
					}else{
						$(".backOptionWrap").css("display","none");
						$("#rowDisabled").css("display","inline");	
						$("#backMenuRangeSaveBtn").hide();
						$("#backMenuCheckedRangeDeleteBtn").hide();
					}
				}
			}
		});
	});
	
	// üũ ���� ���� ��ư 
	$("#backMenuCheckedRangeDeleteBtn").click(function(){
		if(backMenuUserManageGrid.getCheckedRows(0) != "") {
			if (confirm(lang.admin.alert.itemManage8) /* ���� �����Ͻðڽ��ϱ�? */) {
				var checked = backMenuUserManageGrid.getCheckedRows(0).split(",");
				var backRecfileInfoIdx = new Array();
				for( var index in checked ) {
					if (index == "trim") {
						break;
					}
					//seq
					backRecfileInfoIdx.push(backMenuUserManageGrid.cells(parseInt(checked[index]),backMenuUserManageGrid.getColIndexById("seq")).getValue());
				}
				var idxArr = backRecfileInfoIdx.join(",");
				var dataStr = {
					"idx" : idxArr
				};
				$.ajax({
					url:contextPath+"/deleteBackupRecfileInfo.do",
					data: dataStr,
					type:"POST",
					dataType:"json",
					async: false,
					success:function(jRes){
						if(jRes.success == "Y") {
							alert(lang.admin.backRecfileInfo.deleteSuccess /* "삭제 성공" */);
							backMenuUserManageGrid.clearAndLoad(contextPath+"/bakMenuInfo_list.xml");
						} else {
							alert(lang.admin.backRecfileInfo.deleteFail /* "���� ����" */);
						}
					}
				});
			} 
		} else {
			alert(lang.admin.alert.etcConfig12 /* "������ �����͸� �������ּ���." */);			
		}
	})
	
	
	$("#backType").change(function(){
		var delType  = $("#backType").val();
		
		if(delType == "B"){
			$("#file_Back").show();
		}else{
			$("#file_Back").hide();
		}
	})
	
	// ���� ��ư 
	$("#backMenuRangeSaveBtn").click(function(){
		var rowId = treeView.getSelected();
		var level = treeView.getLevel(treeView.getSelected());
		var bgName = "";
		var mgName = "";
		var sgName = "";
		var bgCode = "";
		var mgCode = "";
		var sgCode = "";				
		var backType  		= $("#backType").val();
		var logType  		= $("#logType").val();
		var backupPath 		= replaceAll($("#backPath").val(),'\\','/');
		var decType 		= $("#decType").val();
		var urlUpdateType	= $("#urlUpdateType").val();
		var overWriteType 	= $("#overWriteType").val();
		var conformityType  = $("#conformityType").val();
		var dualBackupType  = $("#dualBackupType").val();
		
		var scheCheckType	= $("#SchecheckType").val();
		var backWeek		= $("#backWeek").val();
		if(backWeek == null || backWeek =='')
			backWeek = "*";
		var backScheduler	= $("#backScheduler").val();
		if(backScheduler == null || backScheduler =='')
			backScheduler = "*";
		var scheTime		= $("#scheTime").val();
		
		var PreviousDataThird	= $("#PreviousDataThird").val();
		var PreviousDataMonth	= $("#PreviousDataMonth").val();
		var PreviousDataDay		= $("#PreviousDataDay").val();
		var PreviousDataTime	= $("#PreviousDataTime").val();
		var sdatebackup			= replaceAll($("input#sDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").val(),'-','');
		var edatebackup			= replaceAll($("input#eDatebackup.ui_input_cal.icon_input_cal.hasDatepicker").val(),'-','');
		// ��ü�� �߰� �ȵ�
		
		if(telnoUse=='Y'){
			bgCode = 'B000';
			mgCode = 'M000';
			sgCode = 'S000';
		}
		
		if(rowId!=null && rowId!=''&&telnoUse != 'Y'){
			switch (level) {
			case 2:
				bgCode = treeView.getSelected();
				bgName = treeView.getSelectedItemText();
				break;
			case 3:
				mgCode = treeView.getSelected();
				mgName = treeView.getSelectedItemText();

				bgName = treeView.getItemText(bgCode);
				bgCode = treeView.getParentId(rowId);
				break;
			case 4:
				sgCode = treeView.getSelected();
				mgCode = treeView.getParentId(rowId);
				bgCode = treeView.getParentId(mgCode);
				
				sgName = treeView.getSelectedItemText();
				mgName = treeView.getItemText(mgCode);
				bgName = treeView.getItemText(bgCode);
				break;
			}					
		}else if(telnoUse != 'Y'){
			alert(lang.admin.alert.allowableRangeManage13);
			return;
		}
		
		if(rsfftUrlCheck != 1){
			alert(lang.admin.alert.urlCheck /* "���� ������ �������ּ���." */);
			return;
		}
		
		if (backType == null || backType == '') {
			alert(lang.admin.alert.backType /* "���� ������ �������ּ���." */);
			return;
		}
		
		if (logType == null || logType == '') {
			alert(lang.admin.alert.logType/* "�α� ������ �������ּ���." */);
			return;
		}
		
		if (decType == null || decType == '') {
			alert(lang.admin.alert.decType/* "�α� ������ �������ּ���." */);
			return;
		}
		
		if (urlUpdateType == null || urlUpdateType == '') {
			alert(lang.admin.alert.urlUpdateType/* "�α� ������ �������ּ���." */);
			return;
		}
		
		if (overWriteType == null || overWriteType == '') {
			alert(lang.admin.alert.overWriteType/* "�α� ������ �������ּ���." */);
			return;
		}
		
		/*if (conformityType == null || conformityType == '') {
			alert(lang.admin.alert.conformityType "�α� ������ �������ּ���." );
			return;
		}*/
		
		if (dualBackupType == null || dualBackupType == '') {
			alert(lang.admin.alert.conformityType/* "�α� ������ �������ּ���." */);
			return;
		}
		
		if (scheCheckType!= 'N') {
			if (scheCheckType == "W" && backWeek == "*") {
				alert(lang.admin.alert.scheDayWeek/* "�α� ������ �������ּ���." */);
				return;
			} else if (scheCheckType == "M" && backScheduler == "*") {
				alert(lang.admin.alert.scheDate/* "�α� ������ �������ּ���." */);
				return;
			}
			if (scheTime == null || scheTime == ''){
				alert(lang.admin.alert.scheTime/* "�α� ������ �������ּ���." */);
				return;
			}
		} 
		
		
		if(confirm(lang.admin.alert.backupScheCreate)){
			var dataStr = {
					"bgCode"			: 	bgCode
			    ,   "mgCode"			: 	mgCode
			    ,	"sgCode"			: 	sgCode
			    ,	"backType"			: 	backType
			    ,	"logType"			: 	logType
			    ,	"backupPath"		:	backupPath
			    ,	"scheCheckType"		: 	scheCheckType
			    ,	"backWeek"			: 	backWeek
			    ,	"backScheduler"		: 	backScheduler	
				,	"scheTime"			: 	scheTime
			    ,	"PreviousDataThird" : 	PreviousDataThird
			    ,	"PreviousDataMonth" : 	PreviousDataMonth
			    ,	"PreviousDataDay"	:	PreviousDataDay
			    ,	"PreviousDataTime"	:	PreviousDataTime
			    ,	"sdatebackup"		:	sdatebackup
			    ,	"edatebackup"		:	edatebackup
			    ,	"decType"			:	decType
			    ,	"urlUpdateType"		:	urlUpdateType
			    ,	"overWriteType"		:	overWriteType
			    ,	"conformityType"	:	conformityType
			    ,	"dualBackupType"	:	dualBackupType
			};
			$.ajax({
				url:contextPath+"/backupRecfileInfoSave.do"+formToSerialize(),
				type:"POST",
				data: dataStr,
				dataType:"json",
				async: false, 
				success:function(jRes){
					// DB�� ��ȸ�� ������ ������
					if(jRes.success == "Y") {
						// �ҷ��� �ɼ� �߰�
						alert(lang.admin.backRecfileInfo.saveSuccess);
						backMenuUserManageGrid.clearAndLoad(contextPath+"/bakMenuInfo_list.xml"+formToSerialize())
						addList = new Array;
						backList = new Array;
						layer_popup_close()
					}else{
						alert(lang.admin.backRecfileInfo.saveFail);
						alert(jRes.resData.msg);
					}
				}
			});
		}
	})
	
	// �˾� �� ����
	$("#backRecfileModifyBtn").click(function(){
		if(confirm(lang.admin.backRecfileInfo.modify /* "���� �Ͻðڽ��ϱ�?" */)){
			var back_type  		= $("#back_type").val();
			var log_type  		= $("#log_type").val();
			var backup_Path 		= $("#back_path").val();
			var dec_Type 		= $("#dec_Type").val();
			var urlUpdate_Type	= $("#urlUpdate_Type").val();
			var overWrite_Type 	= $("#overWrite_Type").val();
			var conformity_Type  = $("#conformity_Type").val();
			var dualBackup_Type  = $("#dualBackup_Type").val();
			
			var scheCheck_Type	= $("#Schecheck_Type").val();
			var back_Week		= $("#back_Week").val();
			if(back_Week == null || back_Week =='')
				back_Week = "*";
			var back_Scheduler	= $("#back_Scheduler").val();
			if(back_Scheduler == null || back_Scheduler =='')
				back_Scheduler = "*";
			var sche_Time		= $("#sche_Time").val();
			
			var PreviousData_Third	= $("#PreviousData_Third").val();
			var PreviousData_Month	= $("#PreviousData_Month").val();
			var PreviousData_Day		= $("#PreviousData_Day").val();
			var PreviousData_Time	= $("#PreviousData_Time").val();
			var sdatebackup			= replaceAll($("input#sDatebackup_Date.ui_input_cal.icon_input_cal.hasDatepicker").val(),'-','');
			var edatebackup			= replaceAll($("input#eDatebackup_Date.ui_input_cal.icon_input_cal.hasDatepicker").val(),'-','');
			
	
			var backRecfileInfoSeq = $("#backRecfileInfoSeq").val();
			
			
			if (back_type == null || back_type == '') {
				alert(lang.admin.alert.backType /* "���� ������ �������ּ���." */);
				return;
			}
			
			if (log_type == null || log_type == '') {
				alert(lang.admin.alert.logType/* "�α� ������ �������ּ���." */);
				return;
			}
			
			if (decType == null || decType == '') {
				alert(lang.admin.alert.decType/* "�α� ������ �������ּ���." */);
				return;
			}
			
			if (urlUpdateType == null || urlUpdateType == '') {
				alert(lang.admin.alert.urlUpdateType/* "�α� ������ �������ּ���." */);
				return;
			}
			
			if (overWriteType == null || overWriteType == '') {
				alert(lang.admin.alert.overWriteType/* "�α� ������ �������ּ���." */);
				return;
			}
			
			/*if (conformityType == null || conformityType == '') {
				alert(lang.admin.alert.conformityType);
				return;
			}*/
			
			if (dualBackupType == null || dualBackupType == '') {
				alert(lang.admin.alert.dualBackupType);
				return;
			}
			
			if (scheCheck_Type!= 'N') {
				if (scheCheck_Type == "W" && back_Week == "*") {
					alert(lang.admin.alert.scheDayWeek/* "�α� ������ �������ּ���." */);
					return;
				} else if (scheCheck_Type == "M" && back_Scheduler == "*") {
					alert(lang.admin.alert.scheDate/* "�α� ������ �������ּ���." */);
					return;
				}
				if (sche_Time == null || sche_Time == ''){
					alert(lang.admin.alert.scheTime/* "�α� ������ �������ּ���." */);
					return;
				}
			} 
			
			var strData = {
					"back_type" 			: 	back_type
				,	"log_type" 				: 	log_type
				,	"backup_Path"			:	backup_Path
				,	"scheCheck_Type" 		: 	scheCheck_Type
				,	"back_Week" 			: 	back_Week
				,	"back_Scheduler"		:	back_Scheduler
				,	"sche_Time"				:	sche_Time
				,	"PreviousData_Third"	:	PreviousData_Third
				,	"PreviousData_Month"	:	PreviousData_Month
				,	"PreviousData_Day"		:	PreviousData_Day
				,	"PreviousData_Time"		:	PreviousData_Time
				,	"backRecfileInfoSeq"	:	backRecfileInfoSeq
				,	"backup_Path"			:	backup_Path
				,	"sdatebackup"			:	sdatebackup
				,	"edatebackup"			:	edatebackup
			    ,	"dec_Type"				:	dec_Type
			    ,	"urlUpdate_Type"			:	urlUpdate_Type
			    ,	"overWrite_Type"			:	overWrite_Type
			    ,	"conformity_Type"		:	conformity_Type
			    ,	"dualBackup_Type"		:	dualBackup_Type
		}

			$.ajax({
				url:contextPath+"/updateBackupRecfileInfo.do",
				data:strData,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB�� ��ȸ�� ������ ������
					if(jRes.success == "Y") {
						alert(lang.admin.backRecfileInfo.modifySuccess /* "���� ���� ������ �����Ǿ����ϴ�." */)
						backMenuUserManageGrid.clearAndLoad(contextPath+"/bakMenuInfo_list.xml"+formToSerialize())
						layer_popup_close()
					} else {
						alert(lang.admin.backRecfileInfo.modifyFail /* "���� ���� ���� ������ �����߽��ϴ�." */);
					}
				}
			});
		}
	});
	
	//fault 관리
	for(var i=1;i<13;i++){
		$("#PreviousDataMonth").append("<option value="+i+">"+i+"</option");
		$("#PreviousData_Month").append("<option value="+i+">"+i+"</option");
	}
	for(var i=1;i<32;i++){
		$("#PreviousDataDay").append("<option value="+i+">"+i+"</option");
		$("#PreviousData_Day").append("<option value="+i+">"+i+"</option");
		$("#backScheduler").append("<option value="+i+">"+i+"</option");
		$("#back_Scheduler").append("<option value="+i+">"+i+"</option");
	}
	for(var i=1;i<24;i++){
		$("#PreviousDataTime").append("<option value="+i+">"+i+"</option");		
		$("#scheTime").append("<option value="+i+">"+i+"</option");	
		$("#PreviousData_Time").append("<option value="+i+">"+i+"</option");		
		$("#sche_Time").append("<option value="+i+">"+i+"</option");	
	}
}


//���� ���� ���� ��ư 
function rangeBack(rowId){
	var listPush = true;
	for(i=0; i< addList.length; i++) {
		if(addList[i][addList[i].length-1]==rowId){
			addList.splice(i,1);
			listPush = false;
		}
	}
	if(listPush){
		if(""==backMenuUserManageGrid.cells(rowId,3).getValue()){
			if(""==backMenuUserManageGrid.cells(rowId,2).getValue()){
				backList.push([rowId,'B']);
			}else{
				backList.push([rowId,'M']);
			}
		}else{
			backList.push([rowId,'S']);
		}
	}
	backMenuUserManageGrid.deleteRow(rowId);
}


function formToSerialize(){
	
	var resultValue = "";
	
	
	if(treeView.getSelected()!=null && treeView.getSelected()!=''&&treeView.getSelected()!='all'){
		var level = treeView.getLevel(treeView.getSelected());

		switch (level) {
		case 2:
			var bgCode = treeView.getSelected().split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+bgCode;
			break;
		case 3:
			var mgCode = treeView.getSelected().split("_")[0];
			var bgCode = treeView.getParentId(treeView.getSelected()).split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+mgCode+"&bgCode="+bgCode;
			break;
		case 4:
			var sgCode = treeView.getSelected().split("_")[0];
			var mgCode = treeView.getParentId(treeView.getSelected()).split("_")[0];
			var bgCode = treeView.getParentId(treeView.getParentId(treeView.getSelected())).split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+sgCode+"&mgCode="+mgCode+"&bgCode="+bgCode;
			break;
		}
	}

	resultValue+= (resultValue.length > 0?"&":"?")+"allowableCode="+$('.group_name_wrap_active').attr("level-code");
	if(addList.length>0){
		resultValue+= (resultValue.length > 0?"&":"?")+"addList="+addList.join('/');
	}
	if(backList.length>0){
		resultValue+= (resultValue.length > 0?"&":"?")+"backList="+backList.join('/');
	}
	
	return encodeURI(resultValue);
}

function backNowStart(seqValue){
	
	if (confirm(lang.admin.alert.itemManage9)) {
		
		$(".nowBackStart"+seqValue).hide();
		$(".nowDownStart"+seqValue).hide();
	
		var seq = seqValue;
		$.ajax({
			url:contextPath+"/NowBackUpStart.do",
			data:{"seq" : seq},
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					var resultValue =  jRes.result;
					//var resultIp =  jRes.resData.rsfftIp
					if(resultValue != "0"){
						if(resultValue == "2"){
							alert("ERROR");
						}else{
							alert("ALREADY THREAD START");
						}
					}else{
						alert("Now Backup Success")
					}
				}
			}
		});
	}
}

function UrlCheck(){
	
	var path = $("#backPath").val();
		
	var returnCheck = 0;
	if(path == null || path == ''){
		return;
	}
		
	$.ajax({
		url:contextPath+"/sendBackupPathCheck.do",
		data:{	"path" : path},
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				var resultValue =  jRes.result;
				if(resultValue == "0"){
					alert("Path Check Success");
					rsfftUrlCheck = 1;
					$("#backPath").attr("disabled", true);
					$(".urlCheck").val(lang.views.search.button.pathChange);
					$(".urlCheck").attr("onclick","UrlCHANGE();");					
				}else if(resultValue == "1"){
					alert("Not Found Path");
				}else{
					alert("Error");
				}				
			}
		}
	});
}

function UrlCHANGE(){
	rsfftUrlCheck = 0;
	$("#backPath").val('');
	$("#backPath").attr("disabled", false);
	$(".urlCheck").val(lang.views.search.button.pathCheck);
	$(".urlCheck").attr("onclick","UrlCheck();");
}

function FileDownload(seq){
	$(".nowBackStart"+seq).hide();
	$(".nowDownStart"+seq).hide();
	progress.on();
	var url = contextPath+"/BackUpFileDown.do?seq="+seq
	$("#download").attr("action", url);
	$("#download").submit();		
	progress.off();
}

function replaceAll(str, searchStr, replaceStr){
	return str.split(searchStr).join(replaceStr)
}
