// 전역변수 설정
var gridChannelManage; // 그리드
var system = "", chRes = "";
var chGenerationJSON = "", chExcelUploadJSON = "", chAddJSON = "", monitoringIP="127.0.0.1";

addLoadEvent(channelManageLoad);

function channelManageLoad() {
	gridChannelManage = channelManageGridLoad();
	formFunction();
	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(writeYn!= 'Y') {
		$('#btnChannelAdd').remove();
	}
	//다중채널추가 삭제 - 이슈있어서일단 삭제
	$('#btnChannelMulAdd').remove();

	if(delYn != 'Y') {
		$('#btnChannelDel').remove();
	}
}

// 채널관리 로드
function channelManageGridLoad() {
    // 채널관리 Grid
	gridChannelManage = new dhtmlXGridObject("gridChannelManage");
	gridChannelManage.setIconsPath(recseeResourcePath + "/images/project/");
	gridChannelManage.setImagePath(recseeResourcePath + "/images/project/");
	gridChannelManage.i18n.paging = i18nPaging[locale];
	gridChannelManage.enablePaging(true, 50, 5, "pagingChannelManage", true);
    gridChannelManage.setPagingWTMode(true,true,true,[50,100,250,500]);
	gridChannelManage.enablePreRendering(50);
    gridChannelManage.setPagingSkin("toolbar", "dhx_web");
	gridChannelManage.enableColumnAutoSize(false);
	gridChannelManage.enablePreRendering(50);
    gridChannelManage.setSkin("dhx_web");
	gridChannelManage.init();
	gridChannelManage.load(contextPath+"/channel_list.xml", function(){

		var search_toolbar = gridChannelManage.aToolBar;

		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+" "+gridChannelManage.getRowsNum() + '</div>')
//		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+" "+gridChannelManage.getRowsNum() + " " +objGrid.i18n.paging.found'</div>')
		search_toolbar.setWidth("total",150)

		$(window).resize();
		gridChannelManage.setSizes();

		$(window).resize(function() {
			gridChannelManage.setSizes();
		});

		//selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);

		gridChannelManage.aToolBar.setMaxOpen("pages", 5);
		$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter")
		$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter ipFilter")
		$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(5) input").addClass("inputFilter ipFilter")
		//$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(6) input").addClass("inputFilter timeFilter")
		
	    channelLicenceCheck();
	}, 'xml')

	// 체크박스 전체 선택
	gridChannelManage.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				gridChannelManage.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				gridChannelManage.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	gridChannelManage.attachEvent("onRowSelect", function(id,ind){
	    return;
	});

	gridChannelManage.attachEvent("onEditCell", function(stage,id,ind,nValue,oValue){

		if (stage == 2){

			var columnId = this.columnIds[ind];

			var channelNumber = this.cells(id,2).getValue();
			var systemCode = this.cells(id,3).getValue();
			var oldSysCode = this.cells(id,3).getValue();
			var extNumber = this.cells(id,4).getValue()
			var IpNumber = this.cells(id,5).getValue()
			var agtIpNumber = this.cells(id,6).getValue()
			
			if(columnId == "chIp"){
				if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(nValue))){
					alert(lang.admin.alert.chennelManage1);
					return false;
				}else{
					IpNumber = nValue;
					
					forEachResult = true;
					gridChannelManage.forEachRow(function(id2){
						if (IpNumber == null || IpNumber == "") {
							return;
						}
						if (forEachResult == false) {
							return;
						}
						if(id != id2 && systemCode == this.cells(id2,3).getValue() && IpNumber == this.cells(id2,5).getValue()){
							alert(lang.admin.alert.chennelManage4) /*"동일 시스템에서 이미 등록된 IP 입니다."*/
							forEachResult = false;
							return false;
						}
						if(systemCode == this.cells(id2,3).getValue() && IpNumber == this.cells(id2,6).getValue()) {
							alert(lang.admin.alert.chennelManage4) /*"동일 시스템에서 이미 등록된 IP 입니다."*/
							forEachResult = false;
							return false;
						}
					});
					if (forEachResult == false) {
						return false;
					}
				}
			}else if(columnId == "chAgtIp"){
				if(nValue != null  && nValue != "" && !(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(nValue))){
					alert(lang.admin.alert.chennelManage1);
					return false;
				}else{
					agtIpNumber = nValue;
					
					forEachResult = true;
					gridChannelManage.forEachRow(function(id2){
						if (agtIpNumber == null || agtIpNumber == "") {
							return;
						}
						if (forEachResult == false) {
							return;
						}
						if(systemCode == this.cells(id2,3).getValue() && agtIpNumber == this.cells(id2,5).getValue()){
							alert("동일 시스템에서 이미 등록된 백업 IP 입니다.")
							forEachResult = false;
							return false;
						}
						if(id != id2 && systemCode == this.cells(id2,3).getValue() && agtIpNumber == this.cells(id2,6).getValue()) {
							alert("동일 시스템에서 이미 등록된 백업 IP 입니다.")
							forEachResult = false;
							return false;
						}
					});
					if (forEachResult == false) {
						return false;
					}
				}
			}else if(columnId == "extNum"){
				if (nValue.length > 6){
					alert(lang.admin.alert.chennelManage2)
					return false;
				}
				extNumber = nValue;
			}else if(columnId =="systemCode"){
				systemCode = nValue;
				oldSysCode = oValue;
			}

			if (columnId != "chAgtIp" && nValue.trim() == ""){
				alert(lang.admin.alert.chennelManage3);
				return false;
			}
			
			if (nValue == oValue){
				return false;
			}
			
			var dataStr = {
					"chNum"				: channelNumber
			    ,   "systemCode"		: systemCode
			    ,	"oldSysCode"		: oldSysCode
			    ,	"method"            : "modify"
			};

			dataStr[columnId+""] = nValue;

			var result = true;

			$.ajax({
				url:contextPath+"/channel_generation_proc.do",
				data:dataStr,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB에 조회한 계정이 있으면
					if(jRes.success == "Y") {
						//alert(lang.admin.alert.chennelManage20);
					} else if(jRes.success == "N"){
						if (jRes.resData.msg == "server is working") {
							alert(lang.admin.channel.alert.serverWorking); // "작업이 진행중입니다. 잠시 후 다시 시도해 주세요."
						} else {
							alert(lang.admin.alert.chennelManage5)
						}
						result = false;
					}
				}
			});
			return result;

		}else{
			return true;
		}
	});

	gridChannelManage.attachEvent("onRowDblClicked", function(id,ind){
		if (!"Y" == modiYn)
			return false;
		else
			return true;

		/*$("#addChannel").find("p").text("채널 수정")

		var channelNumber 	= this.cells(id, 1).getValue();
		var	systemCode 		= this.cells(id, 2).getValue();
		var extNumber 		= this.cells(id, 3).getValue();
		var IpNumber 		= this.cells(id, 4).getValue();
		var recYn 			= this.cells(id, 5).getValue();
		var recKind 		= this.cells(id, 6).getValue();
		var recType 		= this.cells(id, 7).getValue();
		var screenYn 		= this.cells(id, 8).getValue();

		$("#channelNumber").val(channelNumber).attr("disabled","disabled");
		$("#systemCode").val(systemCode);
		$("#extNumber").val(extNumber);
		$("#IpNumber").val(IpNumber);
		$("#recYn").val(recYn);
		$("#recKind").val(recKind);
		$("#recType").val(recType);
		$("#screenYn").val(screenYn);

		$("#channelAddBtn").hide();
		$("#channelModifyBtn").show();

		layer_popup('#addChannel');*/

	});

	gridChannelManage.attachEvent("onXLS", function(){
		progress.on()
	});

	gridChannelManage.attachEvent("onXLE", function(grid_obj,count){

		$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");

		if (gridChannelManage.getRowsNum() > 0){
			try{
				var setResult = '<div style="width: 100%; text-align: center;">'+ gridChannelManage.i18n.paging.results+ gridChannelManage.getRowsNum()+gridChannelManage.i18n.paging.found+'</div>'
				gridChannelManage.aToolBar.setItemText("total", setResult)
			}catch(e){tryCatch(e)}
		}

		$(window).resize();
		gridChannelManage.setSizes();

		$(window).resize(function() {
			gridChannelManage.setSizes();
		});
		customFilter()
		progress.off()

		// 필터 명 변경 해주기..
		selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
		selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);
	});

    ui_controller();
    return gridChannelManage;
}

function formFunction() {

	// 콤보 로드
	selectOptionLoad($("#systemCode"), "system")
	selectOptionLoad($("#recYn"), "YN2");
	selectOptionLoad($("#genesysRegisterYn"), "YN5");
	selectOptionLoad($("#recKind"), "callType", undefined, 0);
	selectOptionLoad($("#recType"), "callKind");
	selectOptionLoad($("#screenYn"), "YN2");

	// 메인페이지 채널 추가 버튼
	$('#btnChannelAdd').click(function() {

		$("#addChannel").find("p").text(lang.admin.channel.title.createChannel/*"채널 추가"*/)

		$("#channelNumber").removeAttr("disabled");
		$("#systemCode option:eq(0)").prop("selected",true)
		$("#extNumber").val("");
		$("#sExtNumber").val("");
		$("#eExtNumber").val("");
		$("#IpNumber").val("");
		$("#sIpNumber").val("");
		$("#agtIpNumber").val("");
		$('#screenYn').val('N')
		$("#recYn  option:eq(0)").prop("selected",true)
		$("#recKind option:eq(0)").prop("selected",true)
		$("#recType option:eq(0)").prop("selected",true)
		/*$("#screenYn option:eq(0)").prop("selected",true)*/

		$(".singleAdd").show();
		$(".mulAdd").hide();
		$("#channelAddBtn").show();
		$("#channelModifyBtn").hide();

		$('#addChannel').data({"method":"2"});
		layer_popup('#addChannel');
	});

	// 메인페이지 다중 채널 추가 버튼
	$('#btnChannelMulAdd').click(function() {

		$("#addChannel").find("p").text(lang.admin.channel.title.multiChannelAdd/*"다중 채널 추가"*/)

		$("#channelNumber").removeAttr("disabled");
		$("#systemCode option:eq(0)").prop("selected",true)
		$("#extNumber").val("");
		$("#sExtNumber").val("");
		$("#eExtNumber").val("");
		$("#IpNumber").val("");
		$("#sIpNumber").val("");
		$("#recYn  option:eq(0)").prop("selected",true)
		$("#recKind option:eq(0)").prop("selected",true)
		$("#recType option:eq(0)").prop("selected",true)
		$("#screenYn option:eq(0)").prop("selected",true)

		$(".singleAdd").hide();
		$(".mulAdd").show();
		$("#channelAddBtn").show();
		$("#channelModifyBtn").hide();

		$('#addChannel').data({"method":"1"});
		layer_popup('#addChannel');
	});


	// 팝업 생성 버튼
	$('#channelAddBtn').click(function() {
		onChannelAddProc();
	});

	// 팝업 수정 버튼
	$('#channelModifyBtn').click(function() {
		onChannelModifyProc();
	});

	// 적용 버튼
	$('#btnChannelApply').click(function() {
		onChannelApply();
	});

	// 채널 삭제 버튼
	$('#btnChannelDel').click(function() {
		onChannelDel();
	});
	
	// 채널 새로고침 버튼
	$('#btnChannelSearch').click(function() {
		gridChannelManage.clearAndLoad(contextPath+"/channel_list.xml", function(){
			$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter")
			$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter ipFilter")
			$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(5) input").addClass("inputFilter ipFilter")
			//$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(6) input").addClass("inputFilter timeFilter")
			selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
			selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
			selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
			selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
			selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
			selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);
		})
	});
	
}

function deleteChannel(id){
	var channelNumber = gridChannelManage.cells(id,2).getValue();
	var systemCode = gridChannelManage.cells(id,3).getValue();

	if(confirm(lang.admin.alert.chennelManage6)){
		$.ajax({
			url:contextPath+"/channel_generation_proc.do",
			data:"method=delete&chList=" + channelNumber + "&sysList="+systemCode,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.alert.attrManage2);
					channelUsageCheck2(systemCode);
					// gridChannelManage.deleteRow(id)
					gridChannelManage.clearAndLoad(contextPath+"/channel_list.xml", function(){
						$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter")
						$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter ipFilter")
						$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(5) input").addClass("inputFilter ipFilter")
						//$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(6) input").addClass("inputFilter timeFilter")
						selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
						selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
						selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
						selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
						selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
						selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);
					})
				} else {
					if (jRes.resData.msg == "server is working") {
						alert(lang.admin.channel.alert.serverWorking); // "작업이 진행중입니다. 잠시 후 다시 시도해 주세요."
					} else {
						alert(lang.admin.alert.attrManage3);
					}
				}
			}
		});
	}
}

function onChannelDel() {
	if(gridChannelManage.getCheckedRows(0) != "") {
		var checked = gridChannelManage.getCheckedRows(0).split(",");
		var rstChNum = new Array();
		var rstSysCode = new Array();
		var sysList = new Array();

		if(confirm(lang.admin.alert.chennelManage6)){
			for(var i = 0 ; i < checked.length ; i++){
				rstChNum.push(gridChannelManage.cells(parseInt(checked[i]),2).getValue());
				rstSysCode.push(gridChannelManage.cells(parseInt(checked[i]),3).getValue());
				// 채널 여러개 동시에 삭제하는경우에 해당 되는 서버만 채널현황 갱신할라고 여기에 담아두는거
				if(!sysList.indexOf(gridChannelManage.cells(parseInt(checked[i]),3).getValue()) == -1){
					sysList.push(gridChannelManage.cells(parseInt(checked[i]),3).getValue());
				}
			}
			var rst = rstChNum.join(",");
			var rstS = rstSysCode.join(",");


			$.ajax({
				url:contextPath+"/channel_generation_proc.do",
				data:"method=delete&chList=" + rst + "&sysList="+rstS,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						gridChannelManage.clearAndLoad(contextPath + "/channel_list.xml", function(){
							$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter")
							$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter ipFilter")
							$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(5) input").addClass("inputFilter ipFilter")
							//$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(6) input").addClass("inputFilter timeFilter")
							selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
							selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
							selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
							selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
							selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
							selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);
						}, "xml");
						for(var i=0; i<sysList.length; i++ ){
							channelUsageCheck2(sysList[i]);
						}
					} else {
						if (jRes.resData.msg == "server is working") {
							alert(lang.admin.channel.alert.serverWorking + "\n" + jRes.resData.reason); // "작업이 진행중입니다. 잠시 후 다시 시도해 주세요."
						} else {
							alert(lang.admin.alert.attrManage3);
						}
					}
				}
			});
		}
 	} else {
 		alert (lang.admin.alert.chennelManage7)
 	}
}
/*
function onChannelSend() {
	var sendMessage = {
		"head": 	"RECSEE",
		"code":		"ADMINISTRATION",
		"kind":		"request",
		"command":	"reload",
		"body":		null
	};
	onWebSocketSend(JSON.stringify(sendMessage));
}


function onApplyMessage(rst) {
	var msg = jQuery.parseJSON(rst.data);

	if(msg !== undefined && msg.head == "RECSEE" && msg.code == "ADMINISTRATION" && msg.kind == "response" && msg.command == "reload") {
		var items = msg.body;
		if(items == "success") {
			alert("성공")
		} else {
			alert("실패")
		}
	}
	onWebSocketClose();
}*/

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

function onChannelAddProc(){
	var method = $('#addChannel').data("method");
	var sExtNumber = $("#sExtNumber").val();
	var eExtNumber = $("#eExtNumber").val();
	var sIpNumber = $("#sIpNumber").val();
	

	/*var channelNumber= $("#channelNumber").val();*/
	var systemCode= $("#systemCode").val();
	var systemName= $("#systemCode option:selected").text();
	var extNumber= $("#extNumber").val();
	var IpNumber= $("#IpNumber").val();
	var agtIpNumber = $("#agtIpNumber").val();
	var recYn= $("#recYn").val()
	var recKind= $("#recKind").val()
	var recType= $("#recType").val()
	var screenYn= $("#screenYn").val()

	if(systemCode == null || systemCode == ""){
		alert(lang.admin.alert.chennelManage8);
		$('#systemCode').focus();
		return;
	} else if((extNumber == null || extNumber == "") && method == "2") {
		alert(lang.admin.alert.chennelManage9);
		$('#extNumber').focus();
		return;
	} else if((sExtNumber == null || sExtNumber == "") && method == "1") {
		alert(lang.admin.alert.chennelManage10);
		$('#sExtNumber').focus();
		return;
	} else if((eExtNumber == null || eExtNumber == "") && method == "1") {
		alert(lang.admin.alert.chennelManage11);
		$('#eExtNumber').focus();
		return;
	} else if(( Number(sExtNumber) >= Number(eExtNumber) ) && method == "1") {
		alert(lang.admin.alert.chennelManage12);
		$('#sExtNumber').focus();
		return;
	} else if((IpNumber == null || IpNumber == "") && method == "2"){
		alert(lang.admin.alert.chennelManage13);
		$('#IpNumber').focus();
		return;
	} else if((sIpNumber == null || sIpNumber == "") && method == "1"){
		alert(lang.admin.alert.chennelManage14);
		$('#sIpNumber').focus();
		return;
	} else if(recYn == null || recYn == ""){
		alert(lang.admin.alert.chennelManage15);
		$('#recYn').focus();
		return;
	} else if(recType == null || recType == ""){
		alert(lang.admin.alert.chennelManage16);
		$('#recType').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(IpNumber)) && method == "2" ) {
		alert(lang.admin.alert.chennelManage1);
		$('#IpNumber').focus();
		return false;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(sIpNumber)) && method == "1" ) {
		alert(lang.admin.alert.chennelManage1);
		$('#sIpNumber').focus();
		return false;
	} else if(agtIpNumber != null &&  agtIpNumber != "" && (!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(agtIpNumber)) && method == "2" )) {
		alert(lang.admin.alert.chennelManage1);
		$('#agtIpNumber').focus();
		return false;
	} else if(screenYn == null || screenYn == ""){
		alert(lang.admin.alert.chennelManage18);
		$('#screenYn').focus();
		return;
	}
	if((agtIpNumber == null || agtIpNumber == "") && method == "2"){
		$('#agtIpNumber').val("");
	}
	forEachResult = true;
	//db 들어가서 중복체크 확인
	$.ajax({
		url:contextPath+"/channelOverlap.do",
		data:{
			"systemCode" : systemCode
		,	"extNumber"	:	extNumber
		,	"IpNumber"	:	IpNumber
		,	"agtIpNumber"	:	agtIpNumber
		},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success=="Y"){
				
				// 다중일 때 ip대역과 내선 범위 체크
				if( (Number(eExtNumber) - Number(sExtNumber)) > (255- Number(sIpNumber.split(".").pop()))) {
					alert(lang.admin.alert.chennelManage21+sIpNumber.split(".").op()+lang.admin.alert.chennelManage22)
				}

				// 다중일 때 중복처리
				if(gridChannelManage.getRowsNum() > 0 && method == "1") {
					// 동일 시스템 검출
					var findSystem = gridChannelManage.findCell(systemCode,3);

					if (findSystem.length > 0){
						var extArray = new Array();
						var ipArray = new Array();
						for(var i = sExtNumber, j=0 ; i <= eExtNumber; i++, j++ ){
							extArray.push(i);
							var ip = sIpNumber.split(".");
							ip[3] = Number(ip.pop())+Number(j)
							ipArray.push(ip.join("."))
						}
						for (var i = 0 ; i < findSystem.length; i++){
							var rowId = findSystem[i][0];
							var findExtNum = Number(gridChannelManage.cells(rowId,4).getValue());
							var findIpNum = gridChannelManage.cells(rowId,5).getValue();

							if( extArray.indexOf(findExtNum) > -1) {
								alert(lang.admin.alert.chennelManage23+findExtNum)
								$('#sExtNumber').focus();
								forEachResult = false;
								return false;
							}
							if(ipArray.indexOf(findIpNum) > -1) {
								alert(lang.admin.alert.chennelManage24+findIpNum)
								$('#sIpNumber').focus();
								forEachResult = false;
								return false;
							}
						}
					}
				}
				if(!forEachResult)
					return;

				var dataStr = {
					/*	"chNum"				: channelNumber
				    ,*/   "systemCode"		: systemCode
				    ,   "recordingUse"		: recYn
				    ,   "recordingType"		: recKind
				    ,   "recordingMethod"	: recType
				    ,   "screenYn"			: screenYn
				    ,	"method"            : method

				};

				if (method == "2"){
					dataStr["sExtNum"]= extNumber
					dataStr["chIp"]= IpNumber
					dataStr["chAgtIp"]= agtIpNumber
				}else{
					dataStr["sExtNum"]= sExtNumber
					dataStr["eExtNum"]= eExtNumber
					dataStr["chIp"]= sIpNumber
				}

				$.ajax({
					url:contextPath+"/channel_generation_proc.do",
					data:dataStr,
					type:"POST",
					dataType:"json",
					async: false,
					success:function(jRes){
						// DB에 조회한 계정이 있으면
						if(jRes.success == "Y") {
							alert(lang.admin.alert.chennelManage25);
							channelUsageCheck(systemCode);
							layer_popup_close();
							gridChannelManage.clearAndLoad(contextPath+"/channel_list.xml", function(){
								$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter")
								$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter ipFilter")
								$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(5) input").addClass("inputFilter ipFilter")
								//$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(6) input").addClass("inputFilter timeFilter")
								selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
								selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
								selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
								selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
								selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
								selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);
							})
							return
						} else if(jRes.success == "N"){
							if (jRes.resData.msg == "server is working") {
								alert(lang.admin.channel.alert.serverWorking); // "작업이 진행중입니다. 잠시 후 다시 시도해 주세요."
							} else {
								alert(lang.admin.alert.chennelManage26)
							}
						}
					}
				});
			}else if(jRes.result=="licence over"){
				alert(lang.admin.alert.chennelManage30)/*"채널 라이센스가 부족합니다."*/
				return false;				
			}else if(jRes.result=="extnum is overlap"){
				alert(lang.admin.alert.chennelManage29)/*"이미 등록된 내선번호 입니다."*/
				return false;
			}else if(jRes.result=="ip is overlap"){
				alert(lang.admin.alert.chennelManage4) /*"동일 시스템에서 이미 등록된 백업 IP 입니다."*/
				return false;
			}else if(jRes.result=="agt ip is overlap"){
				alert("동일 시스템에서 이미 등록된 백업 IP 입니다.");
				return false;
			}
		}
	});
}

function onChannelModifyProc(){
	var channelNumber= $("#channelNumber").val()
	var systemCode= $("#systemCode").val()
	var oldSysCode= gridChannelManage.cells(gridChannelManage.getSelectedRowId(), 3).getValue();
	var extNumber= $("#extNumber").val();
	var IpNumber= $("#IpNumber").val();
	var agtIpNumber = $("#agtIpNumber").val();
	var recYn= $("#recYn").val()
	var recKind= $("#recKind").val()
	var recType= $("#recType").val()
	var screenYn= $("#screenYn").val()


	if (channelNumber == null || channelNumber == "") {
		alert(lang.admin.alert.chennelManage27);
		$('#channelNumber').focus();
		return;
	}else if(systemCode == null || systemCode == ""){
		alert(lang.admin.alert.chennelManage8);
		$('#systemCode').focus();
		return;
	} else if(extNumber == null || extNumber == "") {
		alert(lang.admin.alert.chennelManage9);
		$('#extNumber').focus();
		return;
	} else if(IpNumber == null || IpNumber == ""){
		alert(lang.admin.alert.chennelManage13);
		$('#IpNumber').focus();
		return;
	} else if(recYn == null || recYn == ""){
		alert(lang.admin.alert.chennelManage15);
		$('#recYn').focus();
		return;
	} else if(recType == null || recType == ""){
		alert(lang.admin.alert.chennelManage16);
		$('#recType').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(IpNumber))) {
		alert(lang.admin.alert.chennelManage1);
		$('#IpNumber').focus();
		return false;
	}  else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(agtIpNumber))) {
		alert(lang.admin.alert.chennelManage1);
		$('#agtIpNumber').focus();
		return false;
	} else if(screenYn == null || screenYn == ""){
		alert(lang.admin.alert.chennelManage18);
		$('#screenYn').focus();
		return;
	}
	if((agtIpNumber == null || agtIpNumber == "") && method == "2"){
		$('#agtIpNumber').val("");
	}
	forEachResult =true;
	// 중복 처리
	if(gridChannelManage.getRowsNum() > 0 ) {
		gridChannelManage.forEachRow(function(id){

			if(gridChannelManage.getSelectedRowId() == id)
				return;

			if(channelNumber == this.cells(id,2).getValue()) {
				alert(lang.admin.alert.chennelManage28)
				$('#channelNumber').focus();
				forEachResult = false;
			} /*else if(systemCode == this.cells(id,2).getValue() && extNumber == this.cells(id,3).getValue()) {
				alert("동일시스템에서 이미 등록된 내선번호 입니다.")
				$('#extNumber').focus();
				forEachResult = false;
			} */else if(systemCode == this.cells(id,3).getValue() && IpNumber == this.cells(id,5).getValue()) {
				alert(lang.admin.alert.chennelManage4)
				$('#IpNumber').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;

	var dataStr = {
			"chNum"			: channelNumber
	    ,   "systemCode"		: systemCode
	    ,	"oldSysCode"		: oldSysCode
	    ,	"extNum"			: extNumber
	    ,   "chIp"					: IpNumber
	    ,   "chAgtIp"			: agtIpNumber
	    ,   "recYn"				: recYn
	    ,   "recType"			: recKind
	    ,   "recKind"			: recType
	    ,   "screenYn"			: screenYn
	    ,	"method"           : "modify"

	};

	$.ajax({
		url:contextPath+"/channel_generation_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.chennelManage20);

				layer_popup_close();
				gridChannelManage.clearAndLoad(contextPath+"/channel_list.xml", function(){
					$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter")
					$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter ipFilter")
					$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(5) input").addClass("inputFilter ipFilter")
					//$("#gridChannelManage table tbody tr:nth-child(3) td:nth-child(6) input").addClass("inputFilter timeFilter")
					selectOptionLoad($(gridChannelManage.getFilterElement(3)), "system", null,null,null,null,true);
					selectOptionLoad($(gridChannelManage.getFilterElement(8)), "YN2", null,null,null,null,true);
					selectOptionLoad($(gridChannelManage.getFilterElement(9)), "YN5", null,null,null,null,true);
					selectOptionLoad($(gridChannelManage.getFilterElement(10)), "callType", null,null,null,null,true);
					selectOptionLoad($(gridChannelManage.getFilterElement(11)), "callKind", null,null,null,null,true);
					selectOptionLoad($(gridChannelManage.getFilterElement(12)), "YN2", null,null,null,null,true);
				})
				return

			} else if(jRes.success == "N"){
				if (jRes.resData.msg == "server is working") {
					alert(lang.admin.channel.alert.serverWorking); // "작업이 진행중입니다. 잠시 후 다시 시도해 주세요."
				} else {
					alert(lang.admin.alert.chennelManage5)
				}
			}
		}
	});
}

//라이센스 불러오기
function channelLicenceCheck(){
	var systemInfo;
	$.ajax({
		url:contextPath+"/channelLicence.do?"+new Date().getTime(),
		data:{},
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				systemInfo = jRes.resData;
				var totalLicense = 0;
				for(var i = 0; i < Object.values(systemInfo).length; i++) {
					var htmlString ='<div id="'+Object.values(systemInfo)[i].sysId+'" class="licenceUsage"><label class="sysName">'+Object.values(systemInfo)[i].sysName+'</label>&nbsp;:&nbsp;&nbsp;<label class="channelUsage">'+0+'</label>';
					
					if (defaultLicence == "Y") {
						htmlString += '/<label class="licenceSize">'+Object.values(systemInfo)[i].sysLicence+'</label>';
					} else {
						totalLicense += Number(Object.values(systemInfo)[i].sysLicence);
					}
					htmlString += '</div>';
					$(".dhxtoolbar_float_right").append(htmlString);
					if(Object.values(systemInfo)[i].sysLicence == null){
						$(".dhxtoolbar_float_right #"+Object.values(systemInfo)[i].sysId+" .licenceSize").html("0");
					}
					channelUsageCheck(Object.values(systemInfo)[i].sysId);
				}
				if (defaultLicence == "N") {
					$(".dhxtoolbar_float_right").prepend('<div id="total" class="licenceUsage"><label class="sysName">전체</label>&nbsp;:&nbsp;&nbsp;<label class="channelUsage">'+totalLicense+'</label>');
				}
			}
		}
	});
}

//채널 사용 현황 불러오기
function channelUsageCheck(sysCode){
	$.ajax({
		url:contextPath+"/channelUsage.do?"+new Date().getTime(),
		data:{
			"sysCode" : sysCode
		},
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				if($(".dhxtoolbar_float_right #"+sysCode+" .licenceSize").html() == jRes.result){
					$(".dhxtoolbar_float_right #"+sysCode+" .channelUsage").html(jRes.result);
					$(".dhxtoolbar_float_right #"+sysCode).css("color", "red");
				}else{
					$(".dhxtoolbar_float_right #"+sysCode+" .channelUsage").html(jRes.result);
					$(".dhxtoolbar_float_right #"+sysCode).css("color", "");
				}
			}

		}
	});
}

//채널 사용 현황 불러오기 - 채널 지울때는 시스템코드가 아니라 시스템이름이 변수로 들어옴...그래서 함수 두개로 만듦
function channelUsageCheck2(sysName){
	$.ajax({
		url:contextPath+"/channelUsage.do?"+new Date().getTime(),
		data:{
			"sysName" : sysName
		},
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				if($(".dhxtoolbar_float_right #"+jRes.resData.sysCode+" .licenceSize").html() == jRes.result){
					$(".dhxtoolbar_float_right #"+jRes.resData.sysCode+" .channelUsage").html(jRes.result);
					$(".dhxtoolbar_float_right #"+jRes.resData.sysCode).css("color", "red");
				}else{
					$(".dhxtoolbar_float_right #"+jRes.resData.sysCode+" .channelUsage").html(jRes.result);
					$(".dhxtoolbar_float_right #"+jRes.resData.sysCode).css("color", "");
				}
			}

		}
	});
}

//그리드필터 equal로 커스텀
function customFilter(){
	/*SERVER ID*/
    gridChannelManage.getFilterElement(3)._filter = function () {
        var input = this.value;
        return function (value, id) {
            if (value == input || input == '') {
                return true
            } else {
                return false
            }
        }
    }
}

/* 기존 채널 추가/삭제 버튼이벤트 (페이징에 있었음) */
/*var channel_paging_toolbar = gridChannelManage.aToolBar;
	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
channel_paging_toolbar.addButton("btnApply", 8, "<spring:message code="message.btn.apply" />", "icon_btn_apply.svg", "icon_btn_apply.svg");
<% } %>
	<% if(nowAccessInfo.getExcelYn().equals("Y")) { %>
channel_paging_toolbar.addButton("excelDownload",8, "<spring:message code="views.search.button.excelDownload" />", "icon_excel_download.svg", "icon_excel_download.svg");
<% } %>
	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
channel_paging_toolbar.addButton("btnAdd", 8, "<spring:message code="message.btn.add" />", "icon_btn_add.svg", "icon_btn_add.svg");
<% } %>
	<% if(nowAccessInfo.getDelYn().equals("Y")) { %>
channel_paging_toolbar.addButton("btnDelete", 9, "<spring:message code="message.btn.del" />", "icon_btn_delete.svg", "icon_btn_delete.svg");
<% } %>
channel_paging_toolbar.addSpacer("perpagenum");

channel_paging_toolbar.attachEvent("onClick", function(name){
	switch(name) {
	case "btnApply" :
	 	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
		onChannelApply(skin);
		<% } %>
		break;
	case "excelDownload":
		<% if (nowAccessInfo.getExcelYn().equals("Y")) { %>
		gridChannelManage.setColumnHidden(0, true);
		gridChannelManage.toExcel(contextPath+"/generateExcel.do?fileName=channelList");
		gridChannelManage.setColumnHidden(0, false);
		<% } %>
		break;
	case "btnAdd":
	 	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
		onChannelAdd(skin);
		<% } %>
		break;
	case "btnDelete":
	 	<% if(nowAccessInfo.getDelYn().equals("Y")) { %>
		onChannelDel(skin);
		<% } %>
		break;
	}
});*/



/* 기존 채널 관련 함수 */

/*function onChannelInitForm( response ){
	var responseXml = response.xmlDoc.responseXML;
	if ( window.dhx4.isIE )
	{
		chAddJSON = responseXml.getElementsByTagName('add')[0].text;
		chGenerationJSON = responseXml.getElementsByTagName('generationForm')[0].text;
		chExcelUploadJSON = responseXml.getElementsByTagName('upload')[0].text;
	} else {
		chAddJSON = responseXml.getElementsByTagName('add')[0].textContent;
		chGenerationJSON = responseXml.getElementsByTagName('generationForm')[0].textContent;
		chExcelUploadJSON = responseXml.getElementsByTagName('upload')[0].textContent;
	}
}

function onSystemComboMapping(objForm) {
	if(system.length > 0) {
		var systemCombo = objForm.getCombo("systemCode");

		for(var i = 0; i < system.length; i++) {
			systemCombo.addOption(system[i].system_code,system[i].system_name,null, '', i == 0 ? true: false );
		}
	}
}

<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
function onChannelApply(skin) {
 	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
	dhtmlx.confirm({
	    type:"confirm",
	    title:"<spring:message code="message.title.notifications" />",
	    text: "<spring:message code="management.channel.message.apply.confirm" />",
	    ok:"<spring:message code="message.btn.ok" />",
	    cancel:"<spring:message code="message.btn.cancel" />",
	    callback: function(result){
	        if(result) {
	        	if(webSocket == null || webSocket == undefined) {
	    			var webSocketInfo = {ip:monitoringIP, port:"9980", path:"/administration", openFunction:"onChannelSend", messageFunction:"onApplyMessage"};
	        		onWebSocketConnect(webSocketInfo);
	        	} else {
	        		onChannelSend();
	        	}
	        }
	    }
	});
	<% } %>
}




function onChannelAdd(skin) {
 	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
	skin = skin||"dhx_recsee";

	var channelWins = new dhtmlXWindows();
	channelWins.setSkin( skin );
	var channel_add = channelWins.createWindow( "channel_add", 0, 0, 290, 350 );
	channel_add.setText( '<spring:message code="management.channel.title.channel_add" />' );
	channel_add.button('park').hide();
	channel_add.button('minmax').hide();
	channel_add.centerOnScreen();

	channelWins.window("channel_add").keepInViewport(true);
	channelWins.window("channel_add").denyResize();
	channelWins.window("channel_add").setModal(true);

	chAddJSON = chAddJSON.replace(/management.channel.title.channel_add/m, "<spring:message code="management.channel.title.channel_add" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.no/m, "<spring:message code="management.channel.title.no" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.extnum/m, "<spring:message code="management.channel.title.extnum" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.system/m, "<spring:message code="management.channel.title.system" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.ip/m, "<spring:message code="management.channel.title.ip" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.recording_use/m, "<spring:message code="management.channel.title.recording_use" />");
	chAddJSON = chAddJSON.replace(/use.yes/m, "<spring:message code="use.yes" />");
	chAddJSON = chAddJSON.replace(/use.no/m, "<spring:message code="use.no" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.recording_type/m, "<spring:message code="management.channel.title.recording_type" />");
	chAddJSON = chAddJSON.replace(/call.type.ALL/m, "<spring:message code="call.type.ALL" />");
	chAddJSON = chAddJSON.replace(/call.type.I/m, "<spring:message code="call.type.I" />");
	chAddJSON = chAddJSON.replace(/call.type.O/m, "<spring:message code="call.type.O" />");
	chAddJSON = chAddJSON.replace(/management.channel.title.recording_kind/m, "<spring:message code="management.channel.title.recording_kind" />");
	chAddJSON = chAddJSON.replace(/call.rec.type.acr/m, "<spring:message code="call.rec.type.acr" />");
	chAddJSON = chAddJSON.replace(/call.rec.type.odr/m, "<spring:message code="call.rec.type.odr" />");
	chAddJSON = chAddJSON.replace(/message.btn.add/m, "<spring:message code="message.btn.add" />");
	chAddJSON = chAddJSON.replace(/%contextPath%/gm, "${contextPath}");

	var chForm = channel_add.attachForm(jQuery.parseJSON(chAddJSON));

	onSystemComboMapping(chForm);

	chForm.attachEvent("onButtonClick",function(name){
		var objForm = chForm;
		if(name == "btnChAdd") {

			var chkRst = onChAddChk(2,chForm);
			if (!chkRst) return;

			var param = onFormSerialize(objForm);
			layout.progressOn();
			window.dhx4.ajax.post(contextPath+"/management/channel_generation_proc.do", param, function( response ){
				var res = window.dhx4.s2j(response.xmlDoc.responseText);

				var msg = "<spring:message code="management.channel.message.add.success" />";
				if(res.result == "0") {
					msg = "<spring:message code="management.channel.message.add.fail" />";
				}
				layout.progressOff();

				dhtmlx.alert({
					type:"alert",
					title:"<spring:message code="message.title.notifications" />",
					ok:"<spring:message code="message.btn.ok" />",
					text:msg,
					callback: function(result) {
						channelWins.unload();
						gridChannelManage.clearAndLoad(contextPath + "/management/channel_list.xml",function(){
							onResize();
						});
					}
				});
			});
		}
	});
	<% } else { %>
	return false;
	<% } %>
}

function onChannelDel(skin) {
 	<% if(nowAccessInfo.getDelYn().equals("Y")) { %>
 	if(gridChannelManage.getCheckedRows(0) != "") {
		dhtmlx.confirm({
		    type:"confirm",
		    title:"<spring:message code="message.title.alert" />",
		    text: "<spring:message code="management.channel.message.delete.confirm" />",
		    ok:"<spring:message code="message.btn.ok" />",
		    cancel:"<spring:message code="message.btn.cancel" />",
		    callback: function(result){
		        if(result) {
					var checked = gridChannelManage.getCheckedRows(0).split(",");
					var rstChNum = new Array();
					var rstSysCode = new Array();

					for( var index in checked ) {
						rstChNum.push(gridChannelManage.cells(parseInt(checked[index]),1).getValue());
						rstSysCode.push(gridChannelManage.cells(parseInt(checked[index]),2).getValue());
					}
					var rst = rstChNum.join(",");
					var rstS = rstSysCode.join(",");

					layout.progressOn();
					window.dhx4.ajax.post(contextPath+"/management/channel_generation_proc.do", "method=delete&chList=" + rst + "&sysList="+rstS, function( response ){
						var res = window.dhx4.s2j(response.xmlDoc.responseText);

						var msg = "<spring:message code="management.channel.message.delete.success" />";
						if(res.result == "0") {
							msg = "<spring:message code="management.channel.message.delete.fail" />";
						} else {
							gridChannelManage.clearAndLoad(contextPath + "/management/channel_list.xml", function(){
								onResize();
							}, "xml");
						}
						layout.progressOff();

						dhtmlx.alert({
							type:"alert",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:msg
						});
					});
		        }
		    }
		});
 	} else {
		dhtmlx.alert({
			type:"alert",
			title:"<spring:message code="message.title.notifications" />",
			ok:"<spring:message code="message.btn.ok" />",
			text:"<spring:message code="management.channel.message.delete.nochecked" />"
		});
 	}
	<% } else { %>
	return false;
	<% } %>
}

function onChAddChk(type, objForm) {
 	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
	if (type == 1 ) {
		if (window.dhx4.trim(objForm.getItemValue("sChNum", true)) == "" || window.dhx4.trim(objForm.getItemValue("sChNum", true)).length < 1) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.sChNum.empty" />",
				callback: function(result) {
					objForm.setItemFocus("sChNum");
				}
			});
			return;
		} else if (window.dhx4.trim(objForm.getItemValue("sChNum", true)) == "0") {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.sChNum.zero" />",
				callback: function(result) {
					objForm.setItemFocus("sChNum");
				}
			});
			return;
		} else if (!window.dhx4.trim(objForm.getItemValue("sChNum", true)).isNumber()) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="ws.message.isNaN" />",
				callback: function(result) {
					objForm.setItemFocus("sChNum");
				}
			});
			return;
		}

		if (window.dhx4.trim(objForm.getItemValue("eChNum", true)) == "" || window.dhx4.trim(objForm.getItemValue("eChNum", true)).length < 1) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.eChNum.empty" />",
				callback: function(result) {
					objForm.setItemFocus("eChNum");
				}
			});
			return;
		} else if (window.dhx4.trim(objForm.getItemValue("eChNum", true)) == "0") {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.sChNum.zero" />",
				callback: function(result) {
					objForm.setItemFocus("sChNum");
				}
			});
			return;
		} else if (!window.dhx4.trim(objForm.getItemValue("eChNum", true)).isNumber()) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="ws.message.isNaN" />",
				callback: function(result) {
					objForm.setItemFocus("eChNum");
				}
			});
			return;
		}

		if (parseInt(window.dhx4.trim(objForm.getItemValue("sChNum", true))) > parseInt(window.dhx4.trim(objForm.getItemValue("eChNum", true)))) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.eChNum.small" />",
				callback: function(result) {
					objForm.setItemFocus("eChNum");
				}
			});
			return;
		}
	} else {
		if (window.dhx4.trim(objForm.getItemValue("chNum", true)) == "" || window.dhx4.trim(objForm.getItemValue("chNum", true)).length < 1) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.chNum.empty" />",
				callback: function(result) {
					objForm.setItemFocus("chNum");
				}
			});
			return;
		} else if (window.dhx4.trim(objForm.getItemValue("chNum", true)) == "0") {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="management.channel.alert.chNum.zero" />",
				callback: function(result) {
					objForm.setItemFocus("chNum");
				}
			});
			return;
		} else if (!window.dhx4.trim(objForm.getItemValue("chNum", true)).isNumber()) {
			dhtmlx.alert({
				type:"alert-warning",
				title:"<spring:message code="message.title.notifications" />",
				ok:"<spring:message code="message.btn.ok" />",
				text:"<spring:message code="ws.message.isNaN" />",
				callback: function(result) {
					objForm.setItemFocus("chNum");
				}
			});
			return;
		}
	}

	if (window.dhx4.trim(objForm.getItemValue("sExtNum", true)) == "" || window.dhx4.trim(objForm.getItemValue("sExtNum", true)).length < 1) {
		dhtmlx.alert({
			type:"alert-warning",
			title:"<spring:message code="message.title.notifications" />",
			ok:"<spring:message code="message.btn.ok" />",
			text: type == 1 ? "<spring:message code="management.channel.alert.sExtNum.empty" />" : "<spring:message code="management.channel.alert.extNum.empty" />",
			callback: function(result) {
				objForm.setItemFocus("sExtNum");
			}
		});
		return;
	} else if (window.dhx4.trim(objForm.getItemValue("sExtNum", true)).length < 3) {
		dhtmlx.alert({
			type:"alert-warning",
			title:"<spring:message code="message.title.notifications" />",
			ok:"<spring:message code="message.btn.ok" />",
			text: type == 1 ? "<spring:message code="management.channel.alert.sExtNum.length_three" />" : "<spring:message code="management.channel.alert.extNum.length_three" />",
			callback: function(result) {
				objForm.setItemFocus("sExtNum");
			}
		});
		return;
	} else if (!window.dhx4.trim(objForm.getItemValue("sExtNum", true)).isNumber()) {
		dhtmlx.alert({
			type:"alert-warning",
			title:"<spring:message code="message.title.notifications" />",
			ok:"<spring:message code="message.btn.ok" />",
			text:"<spring:message code="ws.message.isNaN" />",
			callback: function(result) {
				objForm.setItemFocus("sExtNum");
			}
		});
		return;
	}

	if (window.dhx4.trim(objForm.getItemValue("chIp", true)) == "" || window.dhx4.trim(objForm.getItemValue("chIp", true)).length < 1) {
		dhtmlx.alert({
			type:"alert-warning",
			title:"<spring:message code="message.title.notifications" />",
			ok:"<spring:message code="message.btn.ok" />",
			text: type == 1 ? "<spring:message code="management.channel.alert.chIp.empty" />" : "<spring:message code="management.channel.alert.chIp2.empty" />",
			callback: function(result) {
				objForm.setItemFocus("chIp");
			}
		});
		return;
	} else if (!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(window.dhx4.trim(objForm.getItemValue("chIp", true))))) {
		dhtmlx.alert({
			type:"alert-warning",
			title:"<spring:message code="message.title.notifications" />",
			ok:"<spring:message code="message.btn.ok" />",
			text:"<spring:message code="management.channel.alert.chIp.rule_violation" />",
			callback: function(result) {
				objForm.setItemFocus("chIp");
			}
		});
		return;
	}

	return true;
	<% } else { %>
	return false;
	<% } %>
}

function onGeneration(skin) {
 	<% if(nowAccessInfo.getModiYn().equals("Y")) { %>
	contents_tab.tabs("generation").attachHTMLString("<div id='chGenerationForm'></div><div id='chExcelUploadForm'></div>");

	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.channel_generation/m, "<spring:message code="management.channel.title.channel_generation" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.no/m, "<spring:message code="management.channel.title.no" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.extnum/m, "<spring:message code="management.channel.title.extnum" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.system/m, "<spring:message code="management.channel.title.system" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.ip/m, "<spring:message code="management.channel.title.ip" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.recording_use/m, "<spring:message code="management.channel.title.recording_use" />");
	chGenerationJSON = chGenerationJSON.replace(/use.yes/m, "<spring:message code="use.yes" />");
	chGenerationJSON = chGenerationJSON.replace(/use.no/m, "<spring:message code="use.no" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.recording_type/m, "<spring:message code="management.channel.title.recording_type" />");
	chGenerationJSON = chGenerationJSON.replace(/call.type.ALL/m, "<spring:message code="call.type.ALL" />");
	chGenerationJSON = chGenerationJSON.replace(/call.type.I/m, "<spring:message code="call.type.I" />");
	chGenerationJSON = chGenerationJSON.replace(/call.type.O/m, "<spring:message code="call.type.O" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.recording_kind/m, "<spring:message code="management.channel.title.recording_kind" />");
	chGenerationJSON = chGenerationJSON.replace(/call.rec.type.acr/m, "<spring:message code="call.rec.type.acr" />");
	chGenerationJSON = chGenerationJSON.replace(/call.rec.type.odr/m, "<spring:message code="call.rec.type.odr" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.channel_delete/m, "<spring:message code="management.channel.title.channel_delete" />");
	chGenerationJSON = chGenerationJSON.replace(/management.channel.title.channel_delete_help/m, "<spring:message code="management.channel.title.channel_delete_help" />");
	chGenerationJSON = chGenerationJSON.replace(/%contextPath%/gm, "${contextPath}");

	chGenerationJSON = chGenerationJSON.replace(/management.channel.btn.generation/m, "<spring:message code="management.channel.btn.generation" />");

	generation_form = new dhtmlXForm("chGenerationForm",jQuery.parseJSON(chGenerationJSON));

	onSystemComboMapping(generation_form);
	generation_form.save();

	generation_form.attachEvent("onButtonClick",function(name){
		var objForm = generation_form;
		if(name == "btnGeneration") {

			var chkRst = onChAddChk(1,generation_form);
			if (!chkRst) return;

			var param = onFormSerialize(objForm);

			layout.progressOn();
			window.dhx4.ajax.post(contextPath+"/management/channel_generation_proc.do", param, function( response ){
				var res = window.dhx4.s2j(response.xmlDoc.responseText);

				var msg = "<spring:message code="management.channel.message.generation.success" />";
				if(res.result == "0") {
					msg = "<spring:message code="management.channel.message.generation.fail" />";
				} else {
					objForm.unload();
					onGeneration(skin);
				}
				layout.progressOff();

				dhtmlx.alert({
					type:"alert",
					title:"<spring:message code="message.title.notifications" />",
					ok:"<spring:message code="message.btn.ok" />",
					text:msg
				});
			});
		}


		chExcelUploadJSON = chExcelUploadJSON.replace(/management.channel.title.excel_upload/m, "<spring:message code="management.channel.title.excel_upload" />");
		chExcelUploadJSON = chExcelUploadJSON.replace(/management.channel.title.excel_file/m, "<spring:message code="management.channel.title.excel_file" />");
		chExcelUploadJSON = chExcelUploadJSON.replace(/management.channel.title.channel_delete/m, "<spring:message code="management.channel.title.channel_delete" />");
		chExcelUploadJSON = chExcelUploadJSON.replace(/management.channel.btn.upload/m, "<spring:message code="management.channel.btn.upload" />");

		var excelUpload_form = new dhtmlXForm("chExcelUploadForm",jQuery.parseJSON(chExcelUploadJSON));

		excelUpload_form.attachEvent("onButtonClick", function(name){

		});

		onResize();
	});
	<% } else { %>
	return false;
	<% } %>
}*/