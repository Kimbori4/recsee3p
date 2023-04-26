// 전역변수 설정
var gridLogManage; // 그리드
var log = "", logRes = "";

addLoadEvent(logManageLoad);

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
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})

	// 날짜 셋팅
//	$('#sDate, #eDate').datepicker().datepicker("setDate", new Date()).datepicker( "option", "disabled", true );
	datepickerSetting(locale,'#sDate, #eDate');
	//@Kyle
	//캘린더 자동완성 /
	$('#sDate, #eDate').keyup(function(e) {
		autoCalendar(this, e)
	});

	// 날짜 조회 보조 옵션
	$("#daySelect").change(function(){
		var selectedOpt = $(this).val();
		var now = new Date();
		switch(selectedOpt){
		case "day":
//			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			datepickerSetting(locale,'#sDate, #eDate', now);
		break;
		case "week" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			now.setDate(now.getDate()-7)
//			$('#sDate').datepicker().datepicker("setDate", now);
			datepickerSetting(locale,'#sDate', now);
		break;
		case "month" :
			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", true ).datepicker("setDate", now);
			now.setMonth(now.getMonth()-1)
//			$('#sDate').datepicker().datepicker("setDate", now);
			datepickerSetting(locale,'#sDate', now);
		break;
		case "custom" :
//			$('#sDate, #eDate').datepicker().datepicker( "option", "disabled", false ).datepicker("setDate", now);
			datepickerSetting(locale,'#sDate, #eDate', now);
		break;
		}
	});

	if(telnoUse=='Y'){
		$("#rLogIp").hide();
		$("#rLogServerIp").hide();
	}

	// 콤보 로드
	selectOptionLoad($("#sTime"),"Time");
	selectOptionLoad($("#eTime"),"Time","e");
	
	// 마지막 시간값 가져온다....
	$("#eTime").val($("#eTime").children().last().text()).prop("selected", true);
	selectOptionLoad($("#rLogName"),"logName");
	$("#rLogName").change(function(){
		if ($(this).val() != "")
			selectOptionLoad($("#rLogContents"),"logContents",$(this).val());
		else
			$("#rLogContents").children().remove()
	})
	
	// 버튼 클릭
	$("#logSearchBtn").click(function(){
		if($("#rLogIp").val()!=""&&!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test($("#rLogIp").val()))){ 
			alert(lang.admin.alert.logManage1);
			$("#rLogIp").val("")
			$("#rLogIp").focus()
			return false;
		}
		if($("#rLogServerIp").val()!=""&&!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test($("#rLogServerIp").val()))){ 
			alert(lang.admin.alert.logManage1);
			$("#rLogServerIp").val("")
			$("#rLogServerIp").focus()
			return false;
		}
		var strData = formSirealize()
		gridLogManage.clearAndLoad(contextPath+"/log_list.xml?"+encodeURI(strData))
	});

	$("#logDeleteBtn").click(function(){

		if(confirm(lang.admin.alert.logManage2)){

			var strData = formSirealize()+"&proc=delete"

			$.ajax({
				url:contextPath+"/log_proc.do",
				data:strData,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB에 조회한 계정이 있으면
					if(jRes.success == "Y") {
						alert(lang.admin.alert.etcConfig11)
						gridLogManage.clearAndLoad(contextPath+"/log_list.xml?"+encodeURI(strData))
					}
				}
			});
		}
	});
}
function formSirealize(requestOrderBy){

	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	var sTime = $("#sTime").val();
	var eTime = $("#eTime").val();
	var rLogIp = $("#rLogIp").val();
	var rLogServerIp = $("#rLogServerIp").val();
	var rLogUserId = $("#rLogUserId").val();
	var rLogCode = $("#rLogName").val();
	var rLogDetailCode = $("#rLogContents").val()||"";
	var rLogEtc = $("#rLogEtc").val();

	var strData = "limitUse=Y&sDate="+sDate+"&eDate="+eDate+"&sTime="+sTime+"&eTime="+eTime+
				  "&rLogIp="+rLogIp+"&rLogServerIp="+rLogServerIp+"&rLogUserId="+rLogUserId+
				  "&rLogEtc="+rLogEtc+"&rLogCode="+rLogCode+"&rLogDetailCode="+rLogDetailCode;
	
	
	if (requestOrderBy != undefined && requestOrderBy.split("|").length == 2){
		var orderBy = requestOrderBy.split("|")[0]
		var direction = requestOrderBy.split("|")[1]
		
		strData += "&orderBy="+orderBy + "&direction="+direction;
	}

	return encodeURI(strData);
}

function logManageLoad() {
	gridLogManage = logManageGridLoad();
	formFunction()
	authyLoad();
}

//권한 불러 오기
function authyLoad() {
	if(delYn != 'Y') {
		$('#logDeleteBtn').hide();
	}
}

// 로그관리 로드
function logManageGridLoad() {

    // 로그관리 Grid
	objGrid = new dhtmlXGridObject("gridLogManage");
	objGrid.setIconsPath(recseeResourcePath + "/images/project/");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.i18n.paging =  i18nPaging[locale];
	objGrid.enablePaging(true, 30, 5, "pagingLogManage", true);
	objGrid.setPagingWTMode(true,true,true,[30,60,90,100]);
	objGrid.setPagingSkin("toolbar", "dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();

	objGrid.load(contextPath+"/log_list.xml?header=true", function(){
		
		var search_toolbar = objGrid.aToolBar;
		
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results +" "+ objGrid.getRowsNum() +'</div>')
		search_toolbar.setWidth("total",150)
		
		search_toolbar.setMaxOpen("pages", 5);
		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		objGrid.attachEvent("onXLS", function(){
			progress.on()
		});

		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			
			if (objGrid.getRowsNum() > 0){
				var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>'
				objGrid.aToolBar.setItemText("total", setResult)
			}
			
			ui_controller();
			progress.off();

			$(window).resize();
			objGrid.setSizes();
			$(window).resize(function() {
				objGrid.setSizes();
			});

		});
		
		objGrid.attachEvent("onRowDblClicked", function(id,cInd){
			$("#detailText").text(objGrid.cells(id,8).getValue());
			layer_popup("#detailInfo");
		});
		
		//체크박스 전체 선택
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
		
		// 페이지 체인지 이벤트
		objGrid.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum())
				return false;
			return true;
		});
		
		// 소팅 이벤트 커스텀
		objGrid.attachEvent("onBeforeSorting", function(ind){
			
			var a_state = this.getSortingState()
			
			var direction = ((a_state[1] == "des") ? "asc" : "desc")
			
			var columnId = this.getColumnId(ind)
			var requestOrderby = columnId+"|"+direction;

			var nowPage = this.getStateOfView()[0]
			
			this.clearAndLoad(contextPath+"/log_list.xml?" + formSirealize(requestOrderby));
			this.setSortImgState(true,ind,direction)
		});
		
		search_toolbar.addSpacer("perpagenum");
		
		if(excelYn == "Y") {
			search_toolbar.addButton("excelDownload",8, lang.common.excel.download/*"Excel Download"*/, "icon_excel_download.png", "icon_excel_download.png");
		}
		
		
		search_toolbar.attachEvent("onClick", function(name){
			switch(name) {
			case "excelDownload":
				if(objGrid.getRowsNum() > 0) {
					progress.on();
					var grid_link = contextPath+'/logExcel.do?' + formSirealize()+ "&fileName=logList";
					$("#downloadFrame").attr("src", grid_link);
					if (window.dhx4.isIE) {
						$(window).blur(function(){
							progress.off()
						});
					} else {
						$("#downloadFrame").ready(function(){
							progress.off()
						});
					}
				} else {
				}
				break;
			}
		});
		
	}, 'xml');

	
	
	
	
	/*objGrid.attachEvent("onHeaderClick",function(ind, obj){
		return true;
	});
	objGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue){
		return false;
	});*/

	// 헤더 필터링 이벤트
	/*objGrid.attachEvent("onFilterEnd", function(elements) {
		var flag = true;
		for(var key in elements) {
			if( elements[key][0].value.length > 0 ) flag = false;
		}
		if (flag) objGrid.clearAndLoad(contextPath + "/log_list.xml");
	});*/

	// 체크박스 전체 선택 이벤트
	/*objGrid.attachEvent("onHeaderClick",function(ind, obj){
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
	});*/

    ui_controller();
    return objGrid;
}


/* 기존 로그 관련 함수 */

/*function onLogInitForm( response ){
	var responseXml = response.xmlDoc.responseXML;
	if ( window.dhx4.isIE )
	{
		logFormJSON = responseXml.getElementsByTagName('logForm')[0].text;
	} else {
		logFormJSON = responseXml.getElementsByTagName('logForm')[0].textContent;
	}
}

function onLogDel(skin) {
 	<c:if test="${nowAccessInfo.getDelYn().equals('Y')}">
	if(log_grid.getCheckedRows(0) != "") {
		dhtmlx.confirm({
		    type:"confirm",
		    title:"<spring:message code="message.title.alert" />",
		    text: "<spring:message code="management.log.message.delete.confirm" />",
		    ok:"<spring:message code="message.btn.ok" />",
		    cancel:"<spring:message code="message.btn.cancel" />",
		    callback: function(result){
		        if(result) {
					layout.progressOn();
					window.dhx4.ajax.post(contextPath+"/management/log_proc.do", "proc=delete&" + onFormSerialize(logForm), function( response ){
						var res = window.dhx4.s2j(response.xmlDoc.responseText);

						var msg = "<spring:message code="management.log.message.delete.success" />";
						if(res.result == "0") {
							msg = "<spring:message code="management.log.message.delete.fail" />";
						} else {
							log_grid.clearAndLoad(contextPath + "/management/log_list.xml?"+onFormSerialize(logForm), function(){
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
	}
	</c:if>
	<c:if test="${nowAccessInfo.getDelYn().trim().isEmpty() or nowAccessInfo.getDelYn().equalsIgnoreCase('N')}">
	return false;
	</c:if>
}*/