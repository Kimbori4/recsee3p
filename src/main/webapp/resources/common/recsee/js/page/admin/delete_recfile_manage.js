// ��������
var delMenuUserManageGrid; // �׸���
var treeView;
var check=0;
var addList = new Array;
var deleteList = new Array;

//	�ε� �Լ�
function deleteRecfileManageLoad(){

	treeView = new dhtmlXTreeObject("delMenuTreeViewAgent","100%","100%",0);
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


	delMenuUserManageGrid = createGrid("delMenuUserManageGrid", "delMenuInfo_list","",[],[]);
	delMenuUserManageGrid.clearAndLoad(contextPath+"/delMenuInfo_list.xml"+formToSerialize())
	
	gridFunction();
	formFunction();
	delRecfileYNLoad()
	
	
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
		$("#delBtn").hide();

		var del_schedule_name = objGrid.cells(id,objGrid.getColIndexById("delScheduleName")).getValue();
		var bg_name = objGrid.cells(id,objGrid.getColIndexById("bgName")).getValue();
		var mg_name = objGrid.cells(id,objGrid.getColIndexById("mgName")).getValue();
		var sg_name = objGrid.cells(id,objGrid.getColIndexById("sgName")).getValue();

		var del_type = objGrid.cells(id,objGrid.getColIndexById("delTypeValue")).getValue();
		var del_file_type = objGrid.cells(id,objGrid.getColIndexById("delFileTypeValue")).getValue();
		var log_type = objGrid.cells(id,objGrid.getColIndexById("logTypeValue")).getValue();
		var storage_send_chk = objGrid.cells(id,objGrid.getColIndexById("storageSendChkValue")).getValue();
		
		var scheCheck_Type	= objGrid.cells(id,objGrid.getColIndexById("backupSchedulDb")).getValue();
		var back_Week		= objGrid.cells(id,objGrid.getColIndexById("weekDb")).getValue();
		var back_Scheduler	= objGrid.cells(id,objGrid.getColIndexById("day")).getValue();
		var sche_Time		= objGrid.cells(id,objGrid.getColIndexById("time")).getValue();
		var sche_Time_Min		= objGrid.cells(id,objGrid.getColIndexById("min")).getValue();
		
		var del_file_path_type = objGrid.cells(id,objGrid.getColIndexById("delFilePathTypeValue")).getValue();
		var del_year = objGrid.cells(id,objGrid.getColIndexById("delYear")).getValue();
		var del_month = objGrid.cells(id,objGrid.getColIndexById("delMonth")).getValue();
		var del_day = objGrid.cells(id,objGrid.getColIndexById("delDay")).getValue();
		
		var del_year_offset = objGrid.cells(id,objGrid.getColIndexById("delYearOffset")).getValue();
		var del_month_offset = objGrid.cells(id,objGrid.getColIndexById("delMonthOffset")).getValue();
		var del_day_offset = objGrid.cells(id,objGrid.getColIndexById("delDayOffset")).getValue();
		var del_search_type = objGrid.cells(id,objGrid.getColIndexById("delSearchTypeValue")).getValue();
		
		var del_path = objGrid.cells(id,objGrid.getColIndexById("delPath")).getValue();
		
		var del_seq = objGrid.cells(id,objGrid.getColIndexById("delSeq")).getValue();

		$("#del_schedule_name").val(del_schedule_name);
		
		$("#bg_name").val(bg_name);
		$("#mg_name").val(mg_name);
		$("#sg_name").val(sg_name);
		
		$("#del_type").val(del_type);
		$("#del_file_type").val(del_file_type);
		$("#log_type").val(log_type);
		$("#storage_send_chk").val(storage_send_chk);
		
		$("#Schecheck_Type").val(scheCheck_Type) 
		$("#Schecheck_Type").trigger('change');
		$("#back_Week").val(back_Week)
		$("#back_Scheduler").val(back_Scheduler)
		$("#sche_Time").val(sche_Time)
		$("#sche_Time_Min").val(sche_Time_Min)
		
		$("#del_file_path_type").val(del_file_path_type);
		$("#del_year").val(del_year);
		$("#del_month").val(del_month);
		$("#del_day").val(del_day);
		$("#del_path").val(del_path);

		$("#del_year_offset").val(del_year_offset);
		$("#del_month_offset").val(del_month_offset);
		$("#del_day_offset").val(del_day_offset);
		$("#del_search_type").val(del_search_type);
		
		$("#delRecfileInfoSeq").val(del_seq);
		
		layer_popup('#delRecfileInfoModify');
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

function delRecfileYNLoad() {
	var dataStr = {
			"groupKey" : "SYSTEM"
		,	"configKey" : "RECSEE_DELETE"
	};
	
	$.ajax({
		url:contextPath+"/selectOptionYN.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#recseeDelete").val(jRes.resData.configValue).attr('selected', true);
				if(jRes.resData.configValue=="Y"){
					$("#rowDisabled").css("display","none");					
				}else{
					$("#rowDisabled").css("display","inline");		
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
	// ���� ���� ���� ��� ���� - ETC_CONFIG�� ����
	$("#recseeDelete").change(function(){
		var strData = {
				"groupKey" : "SYSTEM"
			,	"configKey" : "RECSEE_DELETE"
			,	"configValue" : $("#recseeDelete").val()
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
					if($("#recseeDelete").val()=="Y"){
						$("#rowDisabled").css("display","none");					
					}else{
						$("#rowDisabled").css("display","inline");		
					}
				}
			}
		});
	});
	
	// üũ ���� ���� ��ư 
	$("#delMenuCheckedRangeDeleteBtn").click(function(){
		if(delMenuUserManageGrid.getCheckedRows(0) != "") {
			if (confirm(lang.admin.alert.itemManage8) /* ���� �����Ͻðڽ��ϱ�? */) {
				var checked = delMenuUserManageGrid.getCheckedRows(0).split(",");
				var delRecfileInfoIdx = new Array();
				for( var index in checked ) {
					if (index == "trim") {
						break;
					}
					delRecfileInfoIdx.push(delMenuUserManageGrid.cells(parseInt(checked[index]),delMenuUserManageGrid.getColIndexById("delSeq")).getValue());
				}
				var idxArr = delRecfileInfoIdx.join(",");
				var dataStr = {
					"idx" : idxArr
				};
				$.ajax({
					url:contextPath+"/deleteDelRecfileInfo.do",
					data: dataStr,
					type:"POST",
					dataType:"json",
					async: false,
					success:function(jRes){
						if(jRes.success == "Y") {
							alert(lang.admin.delRecfileInfo.deleteSuccess /* "삭제 성공" */);
							delMenuUserManageGrid.clearAndLoad(contextPath+"/delMenuInfo_list.xml");
						} else {
							alert(lang.admin.delRecfileInfo.deleteFail /* "���� ����" */);
						}
					}
				});
			} 
		} else {
			alert(lang.admin.alert.etcConfig12 /* "������ �����͸� �������ּ���." */);			
		}
	})
	
	
	$("#delType").change(function(){
		var delType  = $("#delType").val();
		
		if(delType == "B"){
			$("#file_Back").show();
		}else{
			$("#file_Back").hide();
		}
	})
	
	// ���� ��ư 
	$("#delMenuRangeSaveBtn").click(function(){
		var rowId = treeView.getSelected();
		var level = treeView.getLevel(treeView.getSelected());
		var bgName = "";
		var mgName = "";
		var sgName = "";
		var bgCode = "";
		var mgCode = "";
		var sgCode = "";
		
		var delScheduleName = $("#delScheduleName").val();
		var delType  = $("#delType").val();
		var delFileType  = $("#delFileType").val();
		var logType  = $("#logType").val();
		var storageSendChk  = $("#storageSendChk").val();
		
		var delFilePathType  = $("#delFilePathType").val();
		var delYear  = $("#delYear").val();
		var delMonth = $("#delMonth").val();
		var delDay = $("#delDay").val();
		var delYearOffset = $("#delYearOffset").val();
		var delMonthOffset = $("#delMonthOffset").val();
		var delDayOffset = $("#delDayOffset").val();
		var path = $("#delPath").val();
		var delSearchType = $("#delSearchType").val();
		
		var scheCheckType	= $("#SchecheckType").val();
		var backWeek		= $("#backWeek").val();
		if(backWeek == null || backWeek =='')
			backWeek = "*";
		var backScheduler	= $("#backScheduler").val();
		if(backScheduler == null || backScheduler =='')
			backScheduler = "*";
		var scheTime		= $("#scheTime").val();
		var scheTimeMin		= $("#scheTimeMin").val();
		
		// ��ü�� �߰� �ȵ�
		if(rowId!=null && rowId!=''){
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
		}else{
			alert(lang.admin.alert.allowableRangeManage13);
			return;
		}
		
		
		if (delScheduleName == null || delScheduleName == '') {
			alert("삭제 스케줄 이름을 입력해주세요.");
			return;
		}
		
		if (delType == null || delType == '') {
			alert(lang.admin.alert.delType /* "삭제 유형을 선택해주세요." */);
			return;
		}
		
		if (delFileType == null || delFileType == '') {
			alert(lang.admin.alert.delFileType /* "삭제 파일 유형을 선택해주세요." */);
			return;
		}
		
		if (delFilePathType == null || delFilePathType == '') {
			alert(lang.admin.alert.delFilePathType /* "삭제 파일 위치를 선택해주세요." */);
			return;
		}
		
		if ((delYear == null && delMonth == null && delDay == null) 
				|| (delYear == '' && delMonth == '' && delDay == '') 
				|| (delYear == "0" && delMonth == "0" && delDay == "0") 
				|| (delYear == null && delMonth == "0" && delDay == "0")
				|| (delYear == "0" && delMonth == null && delDay == "0")
				|| (delYear == "0" && delMonth == "0" && delDay == null)
				|| (delYear == null && delMonth == null && delDay == "0")
				|| (delYear == null && delMonth == "0" && delDay == null)
				|| (delYear == "0" && delMonth == null && delDay == null)
				|| (delYear == '' && delMonth == "0" && delDay == "0")
				|| (delYear == "0" && delMonth == '' && delDay == "0")
				|| (delYear == "0" && delMonth == "0" && delDay == '')
				|| (delYear == '' && delMonth == '' && delDay == "0")
				|| (delYear == '' && delMonth == "0" && delDay == '')
				|| (delYear == "0" && delMonth == '' && delDay == '')) {
			alert(lang.admin.alert.delPeriod /* "���� �Ⱓ�� �Է����ּ���." */);
			return;
		} else {
			if (delYear == null || delYear == '') {
				var delYear = "0";	
			}
			if (delMonth == null || delMonth == '') {
				var delMonth = "0";	
			}
			if (delDay == null || delDay == '') {
				var delDay = "0";	
			}
			if (delYearOffset == null || delYearOffset == '') {
				delYearOffset = "0";	
			}
			if (delMonthOffset == null || delMonthOffset == '') {
				delMonthOffset = "0";	
			}
			if (delDayOffset == null || delDayOffset == '') {
				delDayOffset = "0";	
			}
		}

		if (logType == null || logType == '') {
			alert(lang.admin.alert.logType/* "로그 유형을 선택해주세요." */);
			return;
		}
		
		if (scheCheckType!= 'N') {
			if (scheCheckType == "W" && backWeek == "*") {
				alert(lang.admin.alert.del.scheDayWeek/* "�α� ������ �������ּ���." */);
				return;
			} else if (scheCheckType == "M" && backScheduler == "*") {
				alert(lang.admin.alert.del.scheDate/* "�α� ������ �������ּ���." */);
				return;
			}
			if (scheTime == null || scheTime == ''){
				alert(lang.admin.alert.del.scheTime/* "�α� ������ �������ּ���." */);
				return;
			}
			if (scheTimeMin == null || scheTimeMin == ''){
				alert(lang.admin.alert.del.scheTime/* "�α� ������ �������ּ���." */);
				return;
			}
			
		} 
		
		if (delSearchType == null || delSearchType == '') {
			alert("삭제 기준을 선택해주세요.");
			return;
		}
		
		if (path == null) {
			alert("삭제 할 녹취파일의 경로를 입력해주세요.");
			return;
		}
		
		if (delMonth > 11) {
			alert(lang.admin.alert.delPreriod.under11 /* "���� �������� 11���Ϸθ� �Է��� �����մϴ�." */);
			return;
		}
		
		if (confirm(lang.admin.etcConfig.confirm.delRecfile1 + " " + delYear + lang.admin.select.title.delYear + " " + delMonth + lang.admin.select.title.delMonth + " " + delDay + lang.admin.select.title.delDay + " " + lang.admin.etcConfig.confirm.delRecfile2 + "\n" 
		           + lang.admin.etcConfig.confirm.delRecfile3)) { /*선택한 그룹의 00 개월 이전 파일이 삭제됩니다. 정말 삭제하시겠습니까?*/
			var dataStr = {
					"delScheduleName" : delScheduleName
				,	"bgCode"	: bgCode
			    ,   "mgCode"	: mgCode
			    ,	"sgCode"	: sgCode
			    ,	"delType"	: delType
			    ,	"delFileType"	: delFileType
			    ,	"logType"	: logType
			    ,	"storageSendChk" : storageSendChk
			    ,	"delFilePathType" : delFilePathType
			    ,	"scheCheckType"		: 	scheCheckType
			    ,	"backWeek"			: 	backWeek
			    ,	"backScheduler"		: 	backScheduler	
				,	"scheTime"			: 	scheTime
				,	"scheTimeMin"	: 	scheTimeMin
			    ,	"delYear"	: delYear
			    ,	"delMonth"	: delMonth
			    ,	"delDay" : delDay
			    ,	"delYearOffset"	: delYearOffset
			    ,	"delMonthOffset" : delMonthOffset
			    ,	"delDayOffset" : delDayOffset
			    ,	"delSearchType" : delSearchType
			    ,	"path" : path
			};
			$.ajax({
				url:contextPath+"/delRecfileInfoSave.do"+formToSerialize(),
				type:"POST",
				data: dataStr,
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB�� ��ȸ�� ������ ������
					if(jRes.success == "Y") {
						// �ҷ��� �ɼ� �߰�
						alert(lang.admin.delRecfileInfo.saveSuccess);
						delMenuUserManageGrid.clearAndLoad(contextPath+"/delMenuInfo_list.xml"+formToSerialize())
						addList = new Array;
						deleteList = new Array;
						layer_popup_close()
					}else{
						if (jRes.resData.msg == "duplicate key") {
							alert("이미 등록된 그룹입니다.");
						} else {
							alert(lang.admin.delRecfileInfo.saveFail);
							if (jRes.resData.msg != "exception") {
								alert(jRes.resData.msg);
							}
						}
					}
				}
			});
		}
	})
	
	// �˾� �� ����
	$("#delRecfileModifyBtn").click(function(){

		if(confirm(lang.admin.delRecfileInfo.modify /* "���� �Ͻðڽ��ϱ�?" */)){
			var del_schedule_name = $("#del_schedule_name").val();
			
			var del_type = $("#del_type").val();
			var del_file_type = $("#del_file_type").val();
			var log_type = $("#log_type").val();
			var storage_send_chk = $("#storage_send_chk").val();

			var del_file_path_type = $("#del_file_path_type").val();
			var del_year = $("#del_year").val();
			var del_month = $("#del_month").val();
			var del_day = $("#del_day").val();

			var scheCheck_Type	= $("#Schecheck_Type").val();
			var back_Week		= $("#back_Week").val();
			if(back_Week == null || back_Week =='')
				back_Week = "*";
			var back_Scheduler	= $("#back_Scheduler").val();
			if(back_Scheduler == null || back_Scheduler =='')
				back_Scheduler = "*";
			var sche_Time		= $("#sche_Time").val();
			var sche_Time_Min		= $("#sche_Time_Min").val();
			
			var del_year_offset = $("#del_year_offset").val();
			var del_month_offset = $("#del_month_offset").val();
			var del_day_offset = $("#del_day_offset").val();
			var del_search_type = $("#del_search_type").val();
			
			var del_path = $("#del_path").val();
			var delRecfileInfoSeq = $("#delRecfileInfoSeq").val();
			
			
			if ((del_year == null && del_month == null && del_day == null) 
					|| (del_year == '' && del_month == '' && del_day == '') 
					|| (del_year == "0" && del_month == "0" && del_day == "0") 
					|| (del_year == null && del_month == "0" && del_day == "0")
					|| (del_year == "0" && del_month == null && del_day == "0")
					|| (del_year == "0" && del_month == "0" && del_day == null)
					|| (del_year == null && del_month == null && del_day == "0")
					|| (del_year == null && del_month == "0" && del_day == null)
					|| (del_year == "0" && del_month == null && del_day == null)
					|| (del_year == '' && del_month == "0" && del_day == "0")
					|| (del_year == "0" && del_month == '' && del_day == "0")
					|| (del_year == "0" && del_month == "0" && del_day == '')
					|| (del_year == '' && del_month == '' && del_day == "0")
					|| (del_year == '' && del_month == "0" && del_day == '')
					|| (del_year == "0" && del_month == '' && del_day == '')) {
				alert(lang.admin.alert.delPeriod /* "���� �Ⱓ�� �Է����ּ���." */);
				return;
			} else {
				if (del_year == null || del_year == '') {
					var del_year = "0";	
				}
				if (del_month == null || del_month == '') {
					var del_month = "0";	
				}
				if (del_day == null || del_day == '') {
					var del_day = "0";	
				}
				if (del_year_offset == null || del_year_offset == '') {
					del_year_offset = "0";	
				}
				if (del_month_offset == null || del_month_offset == '') {
					del_month_offset = "0";	
				}
				if (del_day_offset == null || del_day_offset == '') {
					del_day_offset = "0";	
				}
			}
			
			if (path == null) {
				alert("삭제 할 녹취파일의 경로를 입력해주세요.");
				return;
			}
			
			if (del_month > 11) {
				alert(lang.admin.alert.delPreriod.under11 /* "���� �������� 11���Ϸθ� �Է��� �����մϴ�." */);
				$("#del_month").val("");
				return;
			}
			
			if (del_month_offset > 11) {
				alert(lang.admin.alert.delPreriod.under11 /* "���� �������� 11���Ϸθ� �Է��� �����մϴ�." */);
				$("#del_month_offset").val("");
				return;
			}
			
			if (scheCheck_Type!= 'N') {
				if (scheCheck_Type == "W" && back_Week == "*") {
					alert("삭제 요일을 입력해주십시오.");
					return;
				} else if (scheCheck_Type == "M" && back_Scheduler == "*") {
					alert("삭제 일을 입력해주십시오.");
					return;
				}
				if (sche_Time == null || sche_Time == ''){
					alert("삭제 시간을 입력해주십시오.");
					return;
				}
				if (sche_Time_Min == null || sche_Time_Min == ''){
					alert("삭제 분을 입력해주십시오.");
					return;
				}
			} 
			
			var strData = {
					"del_schedule_name" : del_schedule_name
				,	"del_type" : del_type
				,	"del_file_type" : del_file_type
				,	"log_type" : log_type
				,	"storage_send_chk" : storage_send_chk
				,	"del_file_path_type" : del_file_path_type
				,	"scheCheck_Type" 	: 	scheCheck_Type
				,	"back_Week" 			: 	back_Week
				,	"back_Scheduler"		:	back_Scheduler
				,	"sche_Time"			:	sche_Time
				,	"sche_Time_Min"		:	sche_Time_Min
				,	"del_year" : del_year
				,	"del_month" : del_month
				,	"del_day" : del_day
				,	"del_year_offset" : del_year_offset
				,	"del_month_offset" : del_month_offset
				,	"del_day_offset" : del_day_offset
				,	"del_search_type" : del_search_type
				,	"path" : del_path
				,	"delRecfileInfoSeq" : delRecfileInfoSeq
			}

			$.ajax({
				url:contextPath+"/updateDelRecfileInfo.do",
				data:strData,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB�� ��ȸ�� ������ ������
					if(jRes.success == "Y") {
						alert(lang.admin.delRecfileInfo.modifySuccess /* "���� ���� ������ �����Ǿ����ϴ�." */)
						delMenuUserManageGrid.clearAndLoad(contextPath+"/delMenuInfo_list.xml"+formToSerialize())
						layer_popup_close()
					} else {
						alert(lang.admin.delRecfileInfo.modifyFail /* "���� ���� ���� ������ �����߽��ϴ�." */);
					}
				}
			});
		}
	});
	
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
			$('#scheTimeMin').css("display","inline");
			$('#scheTimeMin').val("");
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
			$('#scheTimeMin').css("display","inline");
			$('#scheTimeMin').val("");
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
			$('#scheTimeMin').css("display","none");
			$('#scheTimeMin').val("*");
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
			$('#scheTimeMin').css("display","inline");
			$('#scheTimeMin').val("");
		}
	})
	
	$('#Schecheck_Type').change(function(){		
		if($(this).val()=="D"){	
			$('.backup_WeekBox').css("display","none");
			$('#back_Week').css("display","none");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","none");
			$('#back_Scheduler').css("display","none");	
			$('#back_Scheduler').val("*");
			$('.backup_HourBox').css("display","inline");
			$('.backup_MinBox').css("display","inline");
			$('#sche_Time').css("display","inline");	
			$('#sche_Time').val("");
			$('#sche_Time_Min').css("display","inline");
			$('#sche_Time_Min').val("");
		}else if($(this).val()=="W"){	
			$('.backup_WeekBox').css("display","inline");
			$('#back_Week').css("display","inline");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","none");
			$('#back_Scheduler').css("display","none");	
			$('#back_Scheduler').val("*");
			$('.backup_HourBox').css("display","inline");
			$('.backup_MinBox').css("display","inline");
			$('#sche_Time').css("display","inline");	
			$('#sche_Time').val("");
			$('#sche_Time_Min').css("display","inline");
			$('#sche_Time_Min').val("");
		}else if($(this).val()=="N"){	
			$('.backup_WeekBox').css("display","none");
			$('#back_Week').css("display","none");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","none");
			$('#back_Scheduler').css("display","none");	
			$('#back_Scheduler').val("*");
			$('.backup_HourBox').css("display","none");
			$('.backup_MinBox').css("display","none");
			$('#sche_Time').css("display","none");	
			$('#sche_Time').val("");
			$('#sche_Time_Min').css("display","none");
			$('#sche_Time_Min').val("");
		}else{
			$('.backup_WeekBox').css("display","none");
			$('#back_Week').css("display","none");	
			$('#back_Week').val("*");	
			$(".backup_DayBox").css("display","inline");
			$('#back_Scheduler').css("display","inline");	
			$('#back_Scheduler').val("0");
			$('.backup_HourBox').css("display","inline");
			$('.backup_MinBox').css("display","inline");
			$('#sche_Time').css("display","inline");	
			$('#sche_Time').val("");
			$('#sche_Time_Min').css("display","inline");
			$('#sche_Time_Min').val("");
		}
	})
	
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
	for(var i=0;i<60;i++){
		$("#scheTimeMin").append("<option value="+i+">"+i+"</option");	
		$("#sche_Time_Min").append("<option value="+i+">"+i+"</option");	
	}
}


//���� ���� ���� ��ư 
function rangeDelete(rowId){
	var listPush = true;
	for(i=0; i< addList.length; i++) {
		if(addList[i][addList[i].length-1]==rowId){
			addList.splice(i,1);
			listPush = false;
		}
	}
	if(listPush){
		if(""==delMenuUserManageGrid.cells(rowId,delMenuUserManageGrid.getColIndexById("sgName")).getValue()){ // 소그룹 
			if(""==delMenuUserManageGrid.cells(rowId,delMenuUserManageGrid.getColIndexById("mgName")).getValue()){ // 중그룹
				deleteList.push([rowId,'B']);
			}else{
				deleteList.push([rowId,'M']);
			}
		}else{
			deleteList.push([rowId,'S']);
		}
	}
	delMenuUserManageGrid.deleteRow(rowId);
}

function formToSerialize(){
	
	var resultValue = "";
	
	
	if(treeView.getSelected()!=null && treeView.getSelected()!=''&&treeView.getSelected()!='all'){
		var level = treeView.getLevel(treeView.getSelected());

		switch (level) {
		case 2:
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+treeView.getSelected();
			break;
		case 3:
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+treeView.getSelected().split("_")[0];
			break;
		case 4:
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+treeView.getSelected();
			break;
		}
	}

	resultValue+= (resultValue.length > 0?"&":"?")+"allowableCode="+$('.group_name_wrap_active').attr("level-code");
	if(addList.length>0){
		resultValue+= (resultValue.length > 0?"&":"?")+"addList="+addList.join('/');
	}
	if(deleteList.length>0){
		resultValue+= (resultValue.length > 0?"&":"?")+"deleteList="+deleteList.join('/');
	}
	
	return encodeURI(resultValue);
}
