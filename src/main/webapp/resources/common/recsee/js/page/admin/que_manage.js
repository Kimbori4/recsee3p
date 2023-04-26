// 전역변수 설정
var gridQueManage; // 그리드

addLoadEvent(queManageLoad);


function queManageLoad() {
	gridQueManage = queManageGridLoad();

	// 큐 추가
	$('#rQueueAdd').click(function(){
		onQueueAddProc();
	});

	// 큐 삭제
	$('#rQueueDel').click(function() {
		onQueueDelProc();
	});
}

// 큐 관리 로드
function queManageGridLoad() {
    // 큐 관리 Grid
	objGrid = new dhtmlXGridObject("gridQueManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingQueManage", true);
    objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.enablePreRendering(50);
    objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enablePreRendering(50);
    objGrid.setSkin("dhx_web");
	objGrid.init();
	objGrid.load(contextPath+"/queue_list.xml", function(){
		objGrid.aToolBar.addSpacer("perpagenum");

		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		/*objGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
			if(cInd == 0) return true;

			if(stage == 2) {
				if(nValue == oValue) return false;

				if(window.dhx4.trim(nValue) == "") {
					dhtmlx.alert({
						type:"alert",
						title:"<spring:message code="message.title.notifications" />",
						ok:"<spring:message code="message.btn.ok" />",
						text:"<spring:message code="message.alert.noData" />"
					});

					return false;
				}

				var rQueueNum = queueGrid.cells(rId, 1).getValue();
				var szParam = "proc=update&rQueueNum=" + rQueueNum + "&rQueueName=" + nValue;

				layout.progressOn();
				var response = window.dhx4.ajax.postSync(contextPath+"/management/queue_proc.do", szParam);
				var res = window.dhx4.s2j(response.xmlDoc.responseText);
				var rst = true;
				//var msg = "<spring:message code="management.queue.alert.message.success" />";

				if(res.result == "0") {
					rst = false;
					//msg = "<spring:message code="management.queue.alert.message.error" />";
				}
				//layout.progressOff();

				dhtmlx.alert({
					type:"alert",
					title:"<spring:message code="message.title.notifications" />",
					ok:"<spring:message code="message.btn.ok" />",
					text:msg
				});
				return rst;
			}
		});*/

	}, 'xml')

	objGrid.attachEvent("onFilterEnd", function(elements) {
		var flag = true;
		for(var key in elements) {
			if( elements[key][0].value.length > 0 ) flag = false;
		}
		if (flag) objGrid.clearAndLoad(contextPath + "/queue_list.xml");
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

    ui_controller();

	return objGrid;
}

// @ezra
// 큐 번호 중복 체크
function onQueueDupChk() {

	var queueNum = $('#rQueueNum').val();
	for(var i=0; i < objGrid.getRowsNum(); i++) {
		if(objGrid.cells2(i, 1).getValue() == queueNum) {
			alert(lang.admin.alert.queManage1)

			return false;
		}
	}
	return true;
}


// @ezra
// 큐 추가, 기존 로직 그대로 사용
function onQueueAddProc(){

	var rQueueNum = $('#rQueueNum').val();
	var rQueueName =  $('#rQueueName').val();


	if (rQueueNum == null || rQueueNum == "") {
		alert(lang.admin.alert.queManage2);
		$('#rQueueNum').focus();
		return;
	} else if(rQueueName == null || rQueueName == "") {
		alert(lang.admin.alert.queManage3);
		$('#rQueueName').focus();
		return;
	} else if(confirm(lang.admin.alert.queManage4)) {
		// 큐 번호 중복 체크
		onQueueDupChk();

		var dataStr = {
				"rQueueNum"				: rQueueNum
		    ,   "rQueueName"			: rQueueName
		    ,	"proc"              	: "insert"
		};

		$.ajax({
			url:contextPath+"/queue_proc.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					alert(lang.admin.alert.queManage5);

					layer_popup_close();
					objGrid.clearAndLoad(contextPath+"/queue_list.xml", function(){})
					return

				} else if(jRes.success == "N"){
					alert(lang.admin.alert.queManage6)
				}
			}
		});
	}
};

// 큐 삭제
function onQueueDelProc() {
	if(objGrid.getCheckedRows(0) != "") {
		var checked = gridQueManage.getCheckedRows(0).split(",");
		var rstQueueNum = new Array();

		for( var index in checked ) {
			rstQueueNum.push(gridQueManage.cells2(parseInt(checked[index]),gridQueManage.getColIndexById("queNum")).getValue());//sec300
		}
		var rst = rstQueueNum.join(",");

		$.ajax({
			url:contextPath+"/queue_proc.do",
			data:"proc=delete&queueList=" + rst,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.alert.queManage7)
					objGrid.clearAndLoad(contextPath + "/queue_list.xml", function(){ }, "xml");
				} else {
					alert(lang.admin.alert.queManage8)
				}
			}
		});
	} else {
		alert (lang.admin.alert.queManage9)
	}
}
