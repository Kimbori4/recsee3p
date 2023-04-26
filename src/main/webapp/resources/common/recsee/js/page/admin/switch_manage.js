// 전역변수 설정
var gridSwitchManage; // 그리드
var pbx = "", pbxRes = "";
var pbxAddJSON = "";

addLoadEvent(switchManageLoad);

function switchManageLoad() {
	gridSwitchManage = switchManageGridLoad();

	// 메인
	$("#pbxAddBtn").click(function(){
		$("#addSwitch").find("p").text(lang.admin.alert.switchManage1)

		$("#pbxId").val("").removeAttr("disabled");
		$("#pbxName").val("");
		$("#pbxIp").val("");
		$("#pbxSipId").val("");
		$("#pbxSipPassword").val("");

		$("#pbxAdd").show();
		$("#pbxModify").hide();

		layer_popup('#addSwitch');
	});

	// 교환기 연동정보 추가
	$('#pbxAdd').click(function(){
		onPbxAddProc();
	});

	$("#pbxModify").click(function(){
		onPbxModifyProc();
	});

	// 교환기 연동정보 삭제
	$('#pbxDel').click(function() {
		onPbxDelProc();
	});

	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != 'Y') {
		$('#pbxAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#pbxDel').hide();
	}
}
// 교환기 연동정보 로드
function switchManageGridLoad() {
    // 교환기 연동정보 Grid
	objGrid = new dhtmlXGridObject("gridSwitchManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingSwitchManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/pbx_list.xml", function(){
		objGrid.aToolBar.addSpacer("perpagenum");

		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
	}, 'xml')

	objGrid.attachEvent("onFilterEnd", function(elements) {
		var flag = true;
		for(var key in elements) {
			if( elements[key][0].value.length > 0 ) flag = false;
		}
		if (flag) objGrid.clearAndLoad(contextPath + "/pbx_list.xml");
	});

	// 체크박스 전체 선택
	objGrid.attachEvent("onHeaderClick",function(ind, obj){
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				objGrid.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			} else {
				objGrid.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			}
		} else {
			return true;
		}
	});

	objGrid.attachEvent("onRowSelect", function(id,ind){
	    return;
	});

	objGrid.attachEvent("onRowDblClicked", function(id,ind){
		$("#addSwitch").find("p").text(lang.admin.alert.switchManage2)

		var pbxId =  this.cells(id, 1).getValue();
		var	pbxName = this.cells(id, 2).getValue();
		var pbxIp = this.cells(id, 3).getValue();

		var pbxSipId = this.cells(id, 5).getValue();
		var pbxSipPassword = this.cells(id, 6).getValue();

		$("#pbxId").val(pbxId).attr("disabled","disabled");
		$("#pbxName").val(pbxName);
		$("#pbxIp").val(pbxIp);
		$("#pbxSipId").val(pbxSipId);
		$("#pbxSipPassword").val(pbxSipPassword);

		$("#pbxAdd").hide();
		$("#pbxModify").show();

		layer_popup('#addSwitch');
	});






	/*objGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue){
		if(modiYn == "Y") {
			if(cInd == 0) return true;

			if(stage == 2) {
				if(nValue == oValue) return false;

				var param = "proc=modify";

				param += "&pbxId=" + pbx_tab_grid.cells(rId,1).getValue();

				switch(cInd) {
				case 2: param += "&pbxName=";
					if (nValue == "" || nValue.length < 1) {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="management.pbx.alert.pbxName.empty" />"
						});
						return false;
					} else if (nValue == "0") {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="management.pbx.alert.pbxName.zero" />"
						});
						return false;
					} else if (!nValue.isSpecialCharCheck(true,"-.")) {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="ws.message.isCharacter" />"
						});
						return false;
					}

					break;
				case 3: param += "&pbxIp=";
					if (!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(nValue))) {
						dhtmlx.alert({
							type:"alert-warning",
							title:"<spring:message code="message.title.notifications" />",
							ok:"<spring:message code="message.btn.ok" />",
							text:"<spring:message code="management.pbx.alert.pbxIp.rule_violation" />",
							callback: function(result) {
								objForm.setItemFocus("pbxIp");
							}
						});
						return;
					}
					break;
				case 4: return; break;
				case 5: param += "&pbxSipId="; break;
				case 6: param += "&pbxSipPassword="; break;
				}
				param += nValue;
				var response = window.dhx4.ajax.postSync(contextPath+"/management/pbx_proc.do?" + param );
				response = window.dhx4.s2j(response.xmlDoc.responseText);

				var returnFlag = true;
				var msg = "<spring:message code="management.pbx.message.modify.success" />";
				if(response.result != "1") {
					returnFlag = false;
					msg = "<spring:message code="management.pbx.message.modify.fail" />";
				}

				dhtmlx.alert({
					type:"alert",
					title:"<spring:message code="message.title.notifications" />",
					ok:"<spring:message code="message.btn.ok" />",
					text:msg
				});
				return returnFlag;
			}
		} else {
			return false;
		}
	});*/


	/*gridSwitchManage = new dhtmlXGridObject({
		parent: "gridSwitchManage"
	});
	//gridSwitchManage.setHeader("체크,교환기 ID,교환기 명,교환기 IP,라이선스,SIP ID,SIP 비밀번호,최근 수정 / 아이디,수정,");
	gridSwitchManage.setHeader("Check,Switch ID,Switch Name,Switch IP,License,SIP ID,SIP PW,Last Modified / ID,Modify,");
    gridSwitchManage.attachHeader("#rspan,#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#rspan,#text_filter,#rspan,#rspan");
	gridSwitchManage.setInitWidths("50,100,100,130,100,80,100,220,100,*");
	gridSwitchManage.setColAlign("center,center,center,center,center,center,center,center,center,center");
	gridSwitchManage.setColTypes("ch,ed,ed,ed,ed,ed,ed,ed,ed,ed");
	gridSwitchManage.setColSorting("str,str,str,str,str,str,str,str,str,str");
    gridSwitchManage.setImagePath(recseeResourcePath + "/images/project/");
    gridSwitchManage.enablePaging(true, 20, 5, "pagingSwitchManage", true);
    gridSwitchManage.setPagingSkin("toolbar","dhx_web");
	gridSwitchManage.init();
    gridSwitchManage.parse(
            function(){
                return {
                    "rows":[
                        { "id":1001, "data":[
                            "ch",
                            "Switch-01",
                            "Switch-01",
                            "192.168.0.188",
                            "0",
                            "SIP",
                            "",
                            "2017-03-17 16:05:48 / Admin",
                            "<button class='ui_btn_white icon_btn_gear_gray'>Modify</button>"
                        ]}
                    ]}
                }(), "json"
        );*/

    ui_controller();

    return objGrid;
}

function eXcell_Pasw(cell) {
	if(cell) {
	   this.cell = cell;
	   this.grid = this.cell.parentNode.grid;
	}
	this.setValue = function(val) {
		this.setCValue("<input type='password' size='15' readonly value='" + val + "' class='inpass'></input>");
	}
	this.getValue = function() {
		return this.cell.childNodes[0].value;
	}
	this.edit = function() {
		this.val = this.getValue();
		this.cell.innerHTML = "<input type='password' size='15' class='inpass2'>";
		this.cell.firstChild.value=this.val;
		this.cell.childNodes[0].onclick = function(e){
			(e||event).cancelBubble=true;
		};
	}
	this.detach = function(){
	   this.setValue(this.cell.childNodes[0].value);
	   return this.cell.title!=this.getValue();
	};
}

eXcell_Pasw.prototype = new eXcell;

// @ezra
// 교환기 추가, 기존 로직 그대로 사용
// FIXME : 라이선스는 어디서 받아오는지 모르겠음. 기존에도 없었음
function onPbxAddProc(){

	var pbxId = $('#pbxId').val();
	var pbxName =  $('#pbxName').val();
	var pbxIp =  $('#pbxIp').val();
	var pbxSipId =  $('#pbxSipId').val();
	var pbxSipPassword =  $('#pbxSipPassword').val();

	if (pbxId == null || pbxId == "") {
		alert(lang.admin.alert.switchManage3);
		$('#pbxId').focus();
		return;
	} else if(pbxId.length < 2) {
		alert(lang.admin.alert.switchManage4);
		$('#pbxId').focus();
		return;
	} else if(pbxId.length > 5) {
		alert(lang.admin.alert.switchManage5);
		$('#pbxId').focus();
		return;
	}

	if(pbxName == null || pbxName == "") {
		alert(lang.admin.alert.switchManage6);
		$('#pbxName').focus();
		return;
	} else if(pbxName.length < 2) {
		alert(lang.admin.alert.switchManage7);
		$('#pbxName').focus();
		return;
	} else if(pbxName.match(/[`~!@#$%^&*|\\\'\";:\/?]/gi)) {
		alert(lang.admin.alert.switchManage8);
		$('#pbxName').focus();
		return;
	}

	if(pbxIp == null || pbxIp == "") {
		alert(lang.admin.alert.switchManage9);
		$('#pbxIp').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(pbxIp))) {
		alert(lang.admin.alert.monitoringManage7);
		$('#pbxIp').focus();
		return false;
	}

	if(pbxSipId == null || pbxSipId == "") {
		alert(lang.admin.alert.switchManage10);
		$('#pbxSipId').focus();
		return;
	}

	if(pbxSipPassword == null || pbxSipPassword == "") {
		alert(lang.admin.alert.switchManage11);
		$('#pbxSipPassword').focus();
		return;
	}
	var forEachResult =true;
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){
			if($('#pbxId').val() == objGrid.cells(id,1).getValue()) {
				alert(lang.admin.alert.switchManage12)
				$('#pbxId').focus();
				forEachResult = false;
			} else if($('#pbxName').val() == objGrid.cells(id,2).getValue()) {
				alert(lang.admin.alert.switchManage13)
				$('#pbxName').focus();
				forEachResult = false;
			} else if($('#pbxIp').val() == objGrid.cells(id,3).getValue()) {
				alert(lang.admin.alert.switchManage14)
				$('#pbxIp').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;


	var dataStr = {
			"pbxId"				: pbxId
	    ,   "pbxName"			: pbxName
	    ,	"pbxIp"				: pbxIp
	    ,   "pbxSipId"			: pbxSipId
	    ,   "pbxSipPassword"	: pbxSipPassword
	    ,	"proc"              : "insert"
	};

	$.ajax({
		url:contextPath+"/pbx_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.switchManage15);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/pbx_list.xml", function(){})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.switchManage16)
			}
		}
	});
}

//@ezra
//교환기 추가, 기존 로직 그대로 사용
//FIXME : 라이선스는 어디서 받아오는지 모르겠음. 기존에도 없었음
function onPbxModifyProc(){

	var pbxId = $('#pbxId').val();
	var pbxName =  $('#pbxName').val();
	var pbxIp =  $('#pbxIp').val();
	var pbxSipId =  $('#pbxSipId').val();
	var pbxSipPassword =  $('#pbxSipPassword').val();

	if (pbxId == null || pbxId == "") {
		alert(lang.admin.alert.switchManage3);
		$('#pbxId').focus();
		return;
	} else if(pbxId.length < 2) {
		alert(lang.admin.alert.switchManage4);
		$('#pbxId').focus();
		return;
	} else if(pbxId.length > 5) {
		alert(lang.admin.alert.switchManage5);
		$('#pbxId').focus();
		return;
	}

	if(pbxName == null || pbxName == "") {
		alert(lang.admin.alert.switchManage6);
		$('#pbxName').focus();
		return;
	} else if(pbxName.length < 2) {
		alert(lang.admin.alert.switchManage7);
		$('#pbxName').focus();
		return;
	} else if(pbxName.match(/[`~!@#$%^&*|\\\'\";:\/?]/gi)) {
		alert(lang.admin.alert.switchManage8);
		$('#pbxName').focus();
		return;
	}

	if(pbxIp == null || pbxIp == "") {
		alert(lang.admin.alert.switchManage9);
		$('#pbxIp').focus();
		return;
	} else if(!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(pbxIp))) {
		alert(lang.admin.alert.monitoringManage7);
		$('#pbxIp').focus();
		return false;
	}

	if(pbxSipId == null || pbxSipId == "") {
		alert(lang.admin.alert.switchManage10);
		$('#pbxSipId').focus();
		return;
	}

	if(pbxSipPassword == null || pbxSipPassword == "") {
		alert(lang.admin.alert.switchManage11);
		$('#pbxSipPassword').focus();
		return;
	}
	var forEachResult = true;
	// 중복 처리
	if(objGrid.getRowsNum() > 0 ) {
		objGrid.forEachRow(function(id){

			if(this.getSelectedRowId() == id)
				return;

			if($('#pbxId').val() == this.cells(id,1).getValue()) {
				alert(lang.admin.alert.switchManage12)
				$('#pbxId').focus();
				forEachResult = false;
			} else if($('#pbxName').val() == this.cells(id,2).getValue()) {
				alert(lang.admin.alert.switchManage13)
				$('#pbxName').focus();
				forEachResult = false;
			} else if($('#pbxIp').val() == this.cells(id,3).getValue()) {
				alert(lang.admin.alert.switchManage14)
				$('#pbxIp').focus();
				forEachResult = false;
			}
		});
	}
	if(!forEachResult)
		return;

	var dataStr = {
			"pbxId"				: pbxId
	    ,   "pbxName"			: pbxName
	    ,	"pbxIp"				: pbxIp
	    ,   "pbxSipId"			: pbxSipId
	    ,   "pbxSipPassword"	: pbxSipPassword
	    ,	"proc"              : "modify"
	};

	$.ajax({
		url:contextPath+"/pbx_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.switchManage15);

				layer_popup_close();
				objGrid.clearAndLoad(contextPath+"/pbx_list.xml", function(){})
				return

			} else if(jRes.success == "N"){
				alert(lang.admin.alert.switchManage16)
			}
		}
	});
}
// 교환기 정보 삭제
function onPbxDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = objGrid.getCheckedRows(0).split(",");
		var rstPbxId = new Array();

		for( var index in checked ) {
			rstPbxId.push(objGrid.cells2(parseInt(checked[index])-1,1).getValue());
		}

		var rst = rstPbxId.join(",");

		$.ajax({
			url:contextPath+"/pbx_proc.do",
			data:"proc=delete&chList=" + rst,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.alert.switchManage17)
					objGrid.clearAndLoad(contextPath + "/pbx_list.xml", function(){ }, "xml");
				} else {
					alert(lang.admin.alert.switchManage18)
				}
			}
		});
	} else {
		alert (lang.admin.alert.switchManage19)
	}
}