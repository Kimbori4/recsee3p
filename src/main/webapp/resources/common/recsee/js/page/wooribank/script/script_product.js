$(document).ready(function() {

	// 상품유형 select 옵션 생성
	makeProductOptions();
	
	// 화면 기능 추가
	spFormFunction();
	// 그리드 생성
	spInit();
	
	//목록보기 버튼
	$("#back_script_btn").click(function() {
		if($(".search_box").hasClass("disable") == true) {
			$(".contents").removeClass("enable");
			$(".contents").addClass("disable");					
			$(".search_box").addClass("enable");
			$(".search_box").removeClass("disable");
		}
	});
	$(".modal-close").click(function() {
		$('#excelForm').val('')
		$('#bkExcelPop').fadeOut(200);
	});
	$("#back_script_btn").click(function() {
		if($(".search_box").hasClass("disable") == true) {
			$(".contents").removeClass("enable");
			$(".contents").addClass("disable");					
			$(".search_box").addClass("enable");
			$(".search_box").removeClass("disable");
		}
	});
	
	// 검색 엔터 처리
	$('#searchKeyword').keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});
	
	// 검색어 입력됐을 때만 x 표시 보이기
	$("#searchKeyword").on("input", function(){
		if($("#searchKeyword").val()!=""){
			$("#keywordRemoveImg").show();
		}else{
			$("#keywordRemoveImg").hide();
		}
	});

	// 최초 진입 시 화면 사이즈에 따라 그리드 사이즈 설정 -- 작업표시줄 있어도 페이징 가려지지 않도록
	var restHeight = $('.main_header').innerHeight()+$('.main_lnb').innerHeight()+$('.filter').innerHeight()+$('#right_paging').innerHeight();
	var gridHeight = window.innerHeight-restHeight;	
	$('#search_script_grid').attr("style", "height:"+gridHeight+"px !important");
	//화면 이동 시 리사이즈
	$(window).on('resize', function(){
		
		// 그리드 페이징 툴바 잘리지 않게 처리 -- IE에서 사이즈 자동조절 ok, 엣지에서 초기 로드값(작업표시줄 없는 기준) 변경 안 되어 추후 확인예정
		var restHeight = $('.main_header').innerHeight()+$('.main_lnb').innerHeight()+$('.filter').innerHeight()+$('#right_paging').innerHeight();
		var gridHeight = window.innerHeight-restHeight;	
		$('#search_script_grid').attr("style", "height:"+gridHeight+"px !important");
		
	});

});//window.onload 마감




/**
 * 화면에서 버튼으로 동작하는 함수의 이벤트 설정
 */
function spFormFunction() {
	
	// 조회 버튼 클릭
	$("#searchBtn").click(function() {
		searchScript(null);
	});
	
	// 시스템 반영 버튼 클릭
	$('#directSciptInsertBtn').click(function() {
		var fk = searchScriptGrid.getCheckedRows(0);
		if(fk.length == 0) {
			alert("즉시반영할 상품을 선택해주세요.");
			return false;
		}
		
		var fkList = fk.split(',');
		var dataStr = {
			"fkList" : fkList
		}
		
		$.ajax({
//			url:"http://localhost:10000/furence/script/tts/load2?pk="+fkList[0],
			// 테스트
//			url:"http://aiprecsyst.woorifg.com:8080/makeTTS/furence/script/tts/load2?pk="+fkList[0],
			// 운영VIP
			url:"https://aiprecsys01.woorifg.com:8080/makeTTS/furence/script/tts/load2?pk="+fkList[0],
			type: "GET",
			data: dataStr,
			dataType: "json",
			async: false,
			success: function(jRes) {
				alert(fk+"번 스크립트를 즉시반영하였습니다.");
			},
			complete:function() {
				alert(fk+"번 스크립트를 즉시반영하였습니다.");
				progress.off();
			}
		});
	});

}

// 조회 버튼 클릭 시 검색란에 표출
function searchScript(query, callback) {
	var data = "keyword="+ $('#searchKeyword').val().trim()
			+ "&searchType=" + $('#scripSearchtType option:selected').val()
			+ "&productType="+$('#scriptType option:selected').val()
			+ "&useYN="+$('#scriptUseType option:selected').val()
			+ "&registeredYN="+$('#scriptRegistType option:selected').val();
	if(query != null && query != "") data += "&"+query;
	
	var url = contextPath+"/wooribank/script/api/product/list/xml?" + encodeURI(data);
	searchScriptGrid.clearAndLoad(url, callback);
};


// 화면 로드 시 그리드 그리는 함수
function spInit() {
	
	// 스크립트 검색 그리드 헤더	
	searchScriptGrid = spCreateGrid("search_script_grid","right_paging","wooribank/script/api/product/list/xml","?header=true","", 100, [], []);
	searchScriptGrid.enableAutoWidth(true);
	// 그리드에 기능 등록
	spGridFunction();
	
	// 플레이어
	audioTag = document.getElementById("audioPlayer");
}


function isNull(param) {
	if(param == null) return true;
	if(param == undefined) return true;
	if(param == "null" || param == "Null" || param == "NULL") return true;
	if(param.length == 0) return true;
	if(typeof(param) == 'string') {
		if(param.trim() == "") return true;
	}
	return false;
}




// 콜백 함수
function OnSendScriptSocketError(error) {
   console.log(error);
}

function OnSendScriptSocketConnect() {
	console.log("sendScriptSocketClient success");
}

function OnSendScriptSocketDataReady(data) {
	console.log(data);
}

function OnSendScriptSocketDisconnect() {
   console.log("소켓이 끊어짐");
}

function showScript(id) {
	scriptId = id;
	var obj = getListFilter(scriptJsonArray, "scriptCode", id);
	if (id == "script_menu") {
		scriptId = "";
		$("#rec_script_box").hide();
		$("#select_script_box").show();
		$("#script_info_box").show();
	} else {
		$(".rec_script_title").text($("#"+id).text());
		$(".rec_script").text(obj[0].script);
		
		var isChecked = $("#"+scriptId+" .chkImg").prop('checked');
		if (isChecked == true) {
			$(".chkIcon").prop('checked', true);
		} else {
			$(".chkIcon").prop('checked', false);
		}
		$("#rec_script_box").show();
		$("#select_script_box").hide();
		$("#script_info_box").hide();	
		
	    $("#play_btn").removeClass("pause");
	    $("#play_btn").addClass("play");
		
		if (obj[0].audioFilePath != undefined && obj[0].audioFilePath != "") {
			scriptwavesurfer.load(obj[0].audioFilePath);
		} else {
			progress.on();
			var dataStr = {
				"scriptCode" : scriptId,
				"productType" : $("#custProduct option:selected").val()
			}
			
			$.ajax({
				url:contextPath+"/playScriptTTS.do",
				data : dataStr,
				type:"POST",
				dataType:"json",
				timeout: 5000,
				async: false,
				success:function(jRes) {
					if(jRes.success == "Y") {
						var result = jRes.resData.listenUrl;
						scriptwavesurfer.load(result);
					} else {
						scriptwavesurfer.empty();
						$("#total_time").text("00:00:00");
						$("#now_time").text("00:00:00");
						alert("스크립트 오디오 파일을 찾을 수 없습니다.");
					}
					progress.off();
				},
				error : function(error) {
					progress.off();
					scriptwavesurfer.empty();
					$("#total_time").text("00:00:00");
					$("#now_time").text("00:00:00");
					alert("스크립트 오디오 파일을 찾을 수 없습니다.");
			    }
			});
		}
	}
}

function getListFilter(jsonArray, key,value) {
	return jsonArray.filter (function(object) {
		return object[key] === value;
	});
}

function seconds2time (seconds, type) {
	var hours = 0;
    var time = "", chkTime = true;

	if ( type === undefined ) type = true;
	if ( type ) hours   = Math.floor(seconds / 3600);

	seconds = Math.floor( seconds, 0 );
	var minutes = Math.floor((seconds - (hours * 3600)) / 60);
    var seconds = seconds - (hours * 3600) - (minutes * 60);

	if ( type )
	{
		if (hours != 0) time = (hours < 10) ? "0"+hours+":" : hours+":";
		else time = "00:";

		if ( time !== "" ) chkTime = true;
		else chkTime = false;
	}

    if (minutes != 0 || chkTime )
	{
		minutes = (minutes < 10 && chkTime) ? "0"+minutes : String(minutes);
		time += minutes+":";
    } else {
		time += "00:";
	}

    if (time === "") time = seconds;
    else time += (seconds < 10) ? "0"+seconds : String(seconds);

    return time;
}

//상품 선택시 생기는 트리 구조
function loadAuthy() {
	var $groupList = $('.group_list').find('.group_name');
	$groupList.empty();
	$.ajax({
	//	url: contextPath+"/getAuthyList.do",
		data: {},
		type: "POST",
		dataType: "json",
		success: function(jRes) {
			var authyList = jRes.resData.authyList;
			// 권한 리스트 뿌리기
			for(i=0; i< authyList.length; i++) {
				var authyName = authyList[i].levelName;
				var authyCode = authyList[i].levelCode;
	
				//마스타 권한 외의 사람은 마스타 권한 안보이게 ㅠ
				if(userInfoJson.userLevel != "E1001") {
					if(authyCode == "E1001") {
						continue;
					}
				}
				$groupList.append('<li><div class="group_name_wrap" level-code="'+authyCode+'"><p class="icon_authy_common icon_authy_'+authyCode+'">' + authyName + '</p></div></li>');
	
				//마지막에 이벤트 붙여주기
				if(i== authyList.length -1) {
					attachEvent();
				}
			}
		}
	});
	
	// 플레이어 비활성화
	top.playerVisible(false);
}


/**
 * Grid 생성 함수
 * 
 * @param objGrid	그리드가 그려질 태그의 id값
 * @param objPaging	페이징이 그려질 태그의 id값
 * @param url		xml 데이터를 가져올 XmlController의 requestMapping 경로
 * @param strData	parameter 정보
 * @returns
 */
function spCreateGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {
	
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, initPageRow, 5, objPaging, true);
	objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	objGrid.setPagingSkin("toolbar","dhx_web");
	objGrid.enableColumnAutoSize(true);
	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	// Load Grid
	objGrid.load(contextPath + "/" + url + ".xml" + encodeURI(strData), function() {
		var grid_toolbar = objGrid.aToolBar;
		grid_toolbar.addSpacer("perpagenum");
		grid_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>')
		grid_toolbar.setWidth("total",80)
		grid_toolbar.addButton("fileDownload",8, "방카슈랑스 액셀업로드", "icon_excel_download.png", "icon_excel_download.png");
		
		grid_toolbar.attachEvent("onClick", function(name){
			 switch(name) {
				case "fileDownload":
					$('#bkExcelPop').fadeIn(200);
					break;
					
			 }
		});
			 
		
		
		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		// 페이지 변경 완료
		objGrid.attachEvent("onPageChanged", function(grid_obj, count){
			ui_controller();
		});
		
		if(objPaging) {
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}
		
		objGrid.attachEvent("onXLS", function(){
			//progress.on()
		});
		
		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count){
			$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");

			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>';
			objGrid.aToolBar.setItemText("total", setResult);
			
			ui_controller();
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
			});	
		});
		
		// sorting
		objGrid.attachEvent("onBeforeSorting", function(index,type,direction){
			if(index == 0) return;
			
			var pageNo = this.getStateOfView()[0];//현재 페이지 번호
			var orderColumn= this.getColumnId(index);		
			
			if(orderColumn == null || orderColumn == "") return ;
			
			var orderDirection = direction == "des" ? "desc" : "asc";
			
			var queryString = "";
			queryString += "orderBy="+ orderColumn
			queryString += "&direction="+ orderDirection;
			
			searchScript(queryString, function(){
				objGrid.setSortImgState(true, index, orderDirection);
				objGrid.changePage(pageNo);
			});
			
		});
	});
	
	// All CheckBox
	objGrid.attachEvent("onHeaderClick", function(ind, obj) {
		if(ind == 0 && obj.type == "click") {
			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
				this.setCheckedRows(0, 1);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
			}else {
				this.setCheckedRows(0, 0);
				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
			 }
		} else {
			return true;
		}
	});
	
	// 체크박스 체크 이벤트 row별로 uncheck되면 hewder부분 check 이미지 없음
	objGrid.attachEvent("onCheck", function(rowId, colIndex, isChecked){
		var checkBox = searchScriptGrid.getCheckedRows(0);
		if (checkBox.length > 0) {
			$("#allcheck>img").attr("src",recseeResourcePath+ "/images/project/dhxgrid_web/item_chk0.gif");
		}
		//
		var searchGird = searchScriptGrid.cells(rowId,searchScriptGrid.getColIndexById('rsScriptStepFk')).getValue();
		console.log("searchGird :"+ searchGird);
	});
	
	objGrid.attachEvent("onDataReady", function() {
		console.log('=========onDataReady=========');
		if(!searchGridLoaded) {
			searchGridLoaded = true;
			searchScript(null);
		}
	});
	
	return objGrid;
} 



// Grid에 기능 붙이는 함수
function spGridFunction() {
	
	// 더블클릭 시 상세조회
	searchScriptGrid.attachEvent("onRowDblClicked", function(id, ind) {
		if(excelYn == "N") return;// 엑셀조회 권한 없는 경우 상세조회 불가
		
		var flag = eltCheck(id);
		if(flag=='Y'){
			showHistory(id);
			return false;
		}

    	if($(".search_box").hasClass("enable") == true) {
			$(".contents").removeClass("disable");
			$(".contents").addClass("enable");
			$(".search_box").addClass("disable");
			$(".search_box").removeClass("enable");		
		}

	});

}

    	
function ButtonYN(){
	if(writeYn == "Y") {
		//조회페이지 저장 버튼
		$("#change_script_btn").css("display","block");
		//조회페이지 신규등록버튼
		$("#add_prize_btn").css("display","block");
		//스크립트 수수정 페이지 목록수정 버튼
		$("#add_script_btn").css("display","block");
		//TTS리딩 버튼
		$("#add_content_Btn").css("display","block");
		//직원리딩버튼
		$("#add_adviser_Btn").css("display","block");
		//상담가이드 버튼
		$("#add_direction_Btn").css("display","block");
		//적합성보고서 버튼
		$("#add_object_Btn").css("display","block");
		//공용문수 신규 버튼
		$("#commonInsertBtn").css("display","block");
		//공용문수 삽입버튼
		$("#commonCopytBtn").css("display","block");
	}
	if(modiYn == "Y") {
		//공용문구 팝업팡 수정 버튼
		$("#commonUpdateBtn").css("display","block");
		//스크립트 수정 
//    		$("#modify_content_Btn").css("display","block");
	}
	if(delYn == "Y") {
		//공용문구 삭제버튼
		$("#commonRemoveBtn").css("display","block");
		//제거
		$(".delete_contentBtn").css("display","block");
	}
}

// 변경이력 팝업창
function showHistory(productListPk){
	var callbackSuccess = function(data){
		var url = contextPath + '/wooribank/script/script_history/' + productListPk + '/history?v=' + uuidv4();
		var name = 'script_history';
		if( productListPk==null || data.resData.isExist==false ){
			alert("조회할 이력이 없습니다.");
			return false;
		}else{
			historyPopup = window.open(url, name, "width="+ screen.width + ", height=" + screen.height +", location=no, resizable=yes, channelmode=yes, scrollbars=yes");
		}
	};
	var callbackError = function(data){
		
	};
	checkHistory(productListPk, callbackSuccess, callbackError);
}

// 캐시로 저장된 이전 화면 보이지 않게 하기 위해 더미스트링 생성
function uuidv4() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxxx'.replace(/[xy]/g, function(c){
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	})
}

// x버튼 클릭 시 현재 입력된 검색어 지움
function removeKeyword(){
	$("#searchKeyword").val("");
	$("#keywordRemoveImg").hide();
	$("#searchKeyword").focus();
}

// 물음표 아이콘 mouseover 시 도움말 표시
function showHeaderHelp(obj){
	var rect = obj.getBoundingClientRect();
	var x= 0;
	if($(obj).attr('id')=="useHelpMessage"){
		x = rect.left-280;
	}else if($(obj).attr('id')=="approverHelpMessage"){
		x = rect.left-145;
	}else{
		x = rect.left-195;
	}
	var y= rect.top-280;
	headerHelpMessage = new dhtmlXPopup();
	var html = $(obj).siblings('input').val();
	headerHelpMessage.attachHTML(html);
	headerHelpMessage.show(x,y,400,300);
}

// 물음표 아이콘 mouseout 시 도움말 사라짐
function hideHeaderHelp(){
	if(headerHelpMessage) headerHelpMessage.hide();
}

// 상품유형 select 옵션 생성
function makeProductOptions(){
	var option = "";
	// DB에서 가져온 값으로 상품유형 옵션 생성 -- productDept 변수는 script_main.jsp에서 선언
	// 영업점, 모니터링 : 전체 + 신탁,펀드,방카,퇴직연금
	// 각 상품부서: 해당 상품유형만
	if(productDept.length > 1){
		option += "<option value='all' selected>전체</option>";
	}
	for(var i=0; i<productDept.length; i++){
		if(productDept[i].name!="공통"){
			option += "<option value='"+productDept[i].code+"'>"+productDept[i].name+"</option>";
		}
	}
	$('#scriptType').append(option);
}

// Grid에서 현재 버전 스크립트 PDF 다운로드 - 영업점,본부부서용
function nowPdfDownload(productPk){
	window.open(contextPath + "/wooribank/script/download/product/" + productPk + "/script/latest?fileType=pdf");
}

// 팝업 열린 상태에서 세션 만료 -1001 코드 응답 시, 팝업창 닫고 팝업오프너(현재 창)에서 로그인화면으로 이동
function fromHistoryToLogin(){
	if(historyPopup != null) {
		historyPopup.close();
		location.replace(contextPath + "/?secret=true");
	}
}
function excelUpload(){
	var f = new FormData(document.getElementById('form1'));

	$.ajax({
		url : contextPath+"/script/excel/upload",
		data : f,
		processData: false,
		contentType: false,
		type: "POST",
		dataType:"json",
		success: function(res) {
				alert(res.resData.msg);
				$('#excelForm').val('')
				$('#bkExcelPop').fadeOut(200);
			},
		error: function(e){
			alert("에러가 발생하였습니다. 관리자에게 문의하십시오.");
			$('#excelForm').val('')
			$('#bkExcelPop').fadeOut(200);
			}

		})
}

