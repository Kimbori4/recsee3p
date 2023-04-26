var scriptList = new Map();
var scriptId = "";
var scriptwavesurfer;
var scriptJsonArray = new Object();
var sendScriptSocketClient;
var isRestartFlag; 
var scriptGrid;
var scriptStepEditGrid;
var searchScriptGrid;
var commomSentence;
var ui_padding_22;
var scriptTitle;
//스크립트 단계 클릭 시 index번호
var scriptStepFk;
var searchScriptCommonGrid;
var scriptCommonList;  //검색한 공용스크립트결과
var selectCommonScriptDetail;
var commonDetailGrid;
var removeCommonScriptIdx;
var pagingToolbar;
var audioTag;
var insertDetailArray;
var scriptEvent;
var popUpScriptGrid;
var productListGroup;//상품 유형을 구뷴하기위함//단위형/추가형/트레이딩형
var ttsResult;
var playCount = 0;
var audioTag;
var scStepPK = 0;
var k = 0;
var realTimeValueArray;
var prizePk ;
var scriptType;
var productPk;
var pkarray=[];
var scriptGroupGrid;
var nowSortingColumn;
var allListenArray=[];
var productArray =[];
var productTypeArray =[];
var scriptChangeCodeArray=[];
var scriptChangeNameArray=[];
var changeText;
var searchGridLoaded = false;
var scriptVariableObject = [] ;
var productCode;
//scriptDetail내역을 받을 변수
var scriptDetailArray=[];
var resultData;
var typeselect;
var typeValue;
var typeText;
var writeCommonText="[공용문구]";
var incres = 0;

var selectProductScriptInfo = {	
	scriptName : null,
	rScriptStepPk : null,
	rProductCode :null,
	rScriptStepFk : null 	
}


window.onload = function() {
	iframeControl();
	// 화면 기능 추가
	formFunction();
	// 그리드 생성
	init();

	//공용문구 추가 팝업창 -> 공용문구 추가버튼
	$("#commonScriptAddBtn").click(function() {
		var name = $("#insert_common_name").val();
		var desc = $("#insert_common_desc").val();
		var text = $("#insert_common_text").val();
		var type = $("#common_type option:selected").val();
		var realTime = $('#common_realTime option:selected').val();
		if(name.trim().length == 0 || desc.trim().length == 0 || text.trim().length == 0) {
			alert("필수 항목들을 채워야합니다.");
			return false;
		}
		data = {
			"name" : name,
			"desc" : desc,
			"text" : text,
			"type" : type,
			"realTime" : realTime
		};
		var flag = insertCommonScript(data);
		if(flag == 'Y') {
			searchScriptCommon('2');
		} else if(flag == 'N') {
			searchScriptCommon('2');
		}
	});
	
	//공용문구 수정버튼
	$('#commonUpdateBtn').click(function() {
		commomUpdateInputBoxChange();
		$('#commonUpdateBtn').addClass("disable");
		$('#commonScriptUpdateBtn').removeClass("disable");
	});
	
	//공용문구 업데이트 기능 AJAX
	$('#commonScriptUpdateBtn').click(function() {
		const common_name = $('#common_name').val();
		const common_desc = $('#common_desc').val();
		const common_text = $('#common_text').val();
		const common_type = $('#common_detail_type option:selected').val();
		const common_pk = $('#common_pk').val();
		const common_realTime = $('#common_detail_realTime option:selected').val();
		
		if(common_name.trim().length == 0 || common_desc.trim().length == 0 || common_text.trim().length == 0) {
			alert("수정할 스크립트 값이 입력되지 않았습니다.");
			return false;
		}
		const data = {
			"name" : common_name,
			"desc" : common_desc,
			"text" : common_text,
			"type" : common_type,
			"pk" : common_pk,
			"realTime" : common_realTime
		}
		updateCommonScript(data);
	})
	
	//
	$("#select_script_btn").click(function() {
		var selectedScriptCode = "";
		$("#script_list option:selected").each(function() {
			if($(this).val() != "" && $("#faceRecMenu ul").find("#"+$(this).val()).length == 0) {
			    $("#faceRecMenu ul").append("<li class='addedScript' onclick='showScript(\""+$(this).val()+"\")' id='"+$(this).val()+"'>");
			    $("#faceRecMenu ul").append("<input type='checkbox' class='chkImg' disabled='disabled'/>"+$(this).text());
			    $("#faceRecMenu ul").append("</li>");
			    
			    selectedScriptCode += ","+$(this).val();
			}
		});
		
		if (selectedScriptCode != "") {
			$.ajax({
				url:contextPath+"/getScriptList.do",
				data:{"scriptCode":selectedScriptCode.substring(1)},
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						var result = jRes.resData.script
						
						for (var i = 0; i < result.length; i++) {
						    var jsonData = result[i];
							scriptList[jsonData.scriptCode] = jsonData.script;
						}
					}
				}
			});
		}
	})

	$(".chkIcon").change(function() {
        if($(".chkIcon").is(":checked")) {
            $("#"+scriptId+" .chkImg").prop('checked',true);
        } else {
            $("#"+scriptId+" .chkImg").prop('checked',false);
        }
    });
	
	ui_controller();

	// 내용 추가 버튼 클릭시 화면단 추가 하는 로직
	$("#add_content_Btn").click(function() {
		//스크립트 목록 미생성시 내용추가 금지
		if(scriptGrid.getSelectedId() == null) {
			return;
		}
		var commonType = "N";
		var rScriptDetailType = "T";
		incres++;
		createNewScriptForm(commonType,rScriptDetailType,incres);
	});
		
	//지시 추가 버튼 클릭시 화면단 추가 하는 로직
	$("#add_direction_Btn").click(function() {
		//스크립트 목록 미생성시 내용추가 금지
		if(scriptGrid.getSelectedId() == null) {
			return;
		}
		var commonType = "N";
		var rScriptDetailType = "G";
		incres++;
		createNewScriptForm(commonType,rScriptDetailType,incres);			
	});	

	//상담원 추가 버튼 클릭시 화면단 추가 하는 로직
	$("#add_adviser_Btn").click(function() {
		//스크립트 목록 미생성시 내용추가 금지
		if(scriptGrid.getSelectedId() == null) {
			return;
		}
		var commonType = "N";
		var rScriptDetailType = "S";
		incres++;
		createNewScriptForm(commonType,rScriptDetailType,incres);
	});	
	
	// 객관식 추가
	$("#add_object_Btn").click(function() {
		//스크립트 목록 미생성시 내용추가 금지
		if(scriptGrid.getSelectedId() == null) {
			return;
		}
		var commonType = "N";
		var rScriptDetailType = "R";
		incres++;
		createNewScriptForm(commonType,rScriptDetailType,incres);
	});	
	//미리듣기
	$(document).on("click", "#audioPlay", function() {
		var attrArray = $(this).parent().parent().parent().children().eq(1).children()[0].attributes;
		var text = $(this).parent().parent().parent().children().eq(1).children()[0].innerText
		var caseDetail = attrArray.ifcasedetail.value;
		text = changeVariableValToName(text,caseDetail);
		var data = {
			"detailPk" : attrArray.scriptkey.nodeValue,
			"detailType" : attrArray.detailtype.nodeValue,
			"detailcomkind" : attrArray.detailcomkind.nodeValue,
			"detailcomfk" : attrArray.detailcomfk.nodeValue,
			"stepfk" : attrArray.stepfk.nodeValue,
			"text" : text,
			"type" : scriptType,
			"pk" : productPk
		}
		
		$.ajax({
			url:contextPath+"/selectDetailTextAuidoPath.do",
			type:"POST",
			dataType:"json",
			data : data,
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {					
						var path = jRes.resData.filePath;
						if(path !=null) {
							audioTag = document.getElementById("audioPlayer");
							//테스트서버
//							audioTag.src = "http://10.214.121.36:28881/listen?url="+path;
							//실 서버
							audioTag.src = path;
							
							audioTag.play();
						}
				} else {
//					alert("추가 실패")
				}
			}		
		})
	});
	
	//스크립트추가버튼 
	var s=0;
	$("#popadd_script_btn").click(function() {
		var getId = popUpScriptGrid.getSelectedId();
		var rsScriptStepFk = searchScriptGrid.cells(searchScriptGrid.getSelectedRowId(), searchScriptGrid.getColIndexById('rsScriptStepFk')).getValue();
		if(getId == null) {
			var newId = getId+'child'+(new Date).valueOf();
			var rsScriptStepFk = searchScriptGrid.cells(searchScriptGrid.getSelectedRowId(), searchScriptGrid.getColIndexById('rsScriptStepFk')).getValue();
		} else if(getId != null) {
			var newId = getId+'child'+(new Date).valueOf();
			var rsScriptStepFk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(0),4).getValue();
		}
		addOrderStep();
		
		k ++;
		s++;
		popUpScriptGrid.addRow(newId,["신규 스크립트 추가"+s,,0,scStepPK+k,rsScriptStepFk,,"new"], 0);
		
		var addCnt = popUpScriptGrid.getRowsNum();
		var prodductAddArray = [];    		
		insertDetailArray = [];
		var rScriptStepFk = "";
		var rScriptStepPk = "";
		var rScriptStepParent = "";
		var rScriptStepName = "";
		var rScriptStepType = "";

		//부모
		for(var i=0; i<addCnt; i++) {
			var chkNew = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),6).getValue();
			if(chkNew == "new") {
				rScriptStepFk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),4).getValue();
				rScriptStepParent = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),2).getValue();
				rScriptStepName = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),0).getValue();
				rScriptStepType = "N";
				prodductAddArray.push(rScriptStepFk+"//"+rScriptStepParent+"//"+rScriptStepName+"//"+rScriptStepType);
			}
		}
		
		// 추가추가
		if(prodductAddArray.length > 0) {    		
			var dataStr = {
				"prodductAddArray"	: prodductAddArray
			}
			$.ajax({
				url:contextPath+"/addTreeScript.do",
				type:"POST",
				data:dataStr,
				dataType:"json",
				async: false ,
				success:function(jRes) {
					if(jRes.success == "Y") {
						scriptLoadGrid(rScriptStepFk);
						scriptLoadGrid2(rScriptStepFk);						
					} else {
						alert("저장 실패");
					}
				}
			});   
		}
	});	
	
	// 하위메뉴 추가 버튼
	var h = 0;
	$("#lastBtn").click(function() {
		//parnetCode가 0이면 추가 가능하고 아니면 가증하지 않도록
		var num = popUpScriptGrid.getRowsNum();		
		var targetId = popUpScriptGrid.getSelectedRowId();
		var gettext11 = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),0).getValue();
		var gettext = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),6).getValue();
		var parent = popUpScriptGrid.cells(popUpScriptGrid.getRowId(0),2).getValue();
		var pk = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(0),2).getValue();
		var pk2 = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),3).getValue();
		var rsScriptStepFk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(0),4).getValue();

		var newId = targetId + 'child' + (new Date).valueOf();
		addOrderStep();
			
		if(pk == 0) {
			h++;
			popUpScriptGrid.addRow(newId,["하위 스크립트 추가"+h,,pk2,scStepPK,rsScriptStepFk,,"new_child"], 0, targetId);			
		} else {
			alert("현재 메뉴 단계는 최하위 메뉴이므로 하위메뉴를 추가할 수 없습니다.");
		}		
		popUpScriptGrid.expandAll();
		
		var addCnt = popUpScriptGrid.getRowsNum();
		var prodductAddArray = [];
		insertDetailArray = [];
		var rScriptStepFk = "";
		var rScriptStepPk = "";
		var rScriptStepParent = "";
		var rScriptStepName = "";
		var rScriptStepType = "";

		//자식
		for(var i=0; i<addCnt; i++) {      		
			var chkNew = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),6).getValue();
			if(chkNew == "new_child") {
				rScriptStepFk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),4).getValue();
				rScriptStepParent = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),2).getValue();
				rScriptStepName = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),0).getValue();
				rScriptStepPk = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(i),3).getValue();
				rScriptStepType = "N";
				prodductAddArray.push(rScriptStepFk+"//"+rScriptStepParent+"//"+rScriptStepName+"//"+rScriptStepType);
			}
		}
		
		//추가추가
		if(prodductAddArray.length > 0) {    		
			var dataStr = {
				"prodductAddArray"	: prodductAddArray
			}
			$.ajax({
				url:contextPath+"/addTreeScript.do",
				type:"POST",
				data:dataStr,
				dataType:"json",
				async: false ,
				success:function(jRes) {
					if(jRes.success == "Y") {
						scriptLoadGrid(rScriptStepFk);
						scriptLoadGrid2(rScriptStepFk);
					} else {
						alert("저장 실패");
					}
				}
			});   
		}
	});
	
	// 스크립트 1.수정 - 2.저장 버튼 클릭
	$("#change_script_btn").click(function() {
		var scriptArray = [];
		//1.수정하기 위한 부분
		// 스크립트 갯수 만큼 반복
		for(var i = 0; i< $(".scriptContext").length; i++) {
			// 스크립트 내용을 가져온다								replace(/{/gi
			var htmlText = $(".scriptContext").eq(i).html().replace(/<pre contenteditable="true">/gi,"").replace(/<\/pre>/gi,"\n").replace(/<br>/gi,"\n");
			
			// 자동입력칸이 있는경우 뒤에 div 태그 삭제 
			if(htmlText.indexOf('<div id="list" class="panel panel-default hide">') > -1) {
				htmlText = htmlText.substring(0,htmlText.indexOf('\n<div id="list" class="panel panel-default hide">'))
			}
			
			// 마지막에 엔터 부분 제거 
			if(htmlText.length == htmlText.lastIndexOf("\n")+1) {
				htmlText = htmlText.substring(0, htmlText.lastIndexOf("\n"));
			}
			
			// span 태그 삭제 
			if(htmlText.indexOf("<span") > -1) {
				htmlText = htmlText.replace(/<span style="color:red;">/gi,"").replace(/<\/span>/gi, "");
			}
	 			
			//고객 유형, 상품코드 존재여부 확인
			var ifcase ;
			var ifcase_select = $("#typeselect"+i).val();		
			var ifcasedetail_select = $("#typeValue"+i).val();
			var ifcasedetail_text = $("#typeText"+i).val();
			//ifcase 및 ifcaseDetail에 관한 내역
			if(ifcase_select == "-" || isNull(ifcase_select)) {
				ifcase = "N";
				ifcasedetail = "조건없음";
			} else {				 
				ifcase = "Y";
				if(ifcase_select == "" || isNull(ifcasedetail_select)){
					ifcasedetail = ifcasedetail_text
				}else if(ifcasedetail_text == "" || isNull(ifcasedetail_text)){
					ifcasedetail = ifcasedetail_select					
				}
			} 
			if(htmlText.trim().length == 0 || htmlText == null) {
				alert("리딩 내역이 입력되지 않았습니다. 확인 후 다시 저장하세요.");
				return false;
			} else if(htmlText != htmlText.trim().legth) {
				var scriptJson = {
						"detailType"	: $("#genneralCheck").val(),
						"htmlText"		: htmlText,
						"detailComKind"	: $("#genneralCheck").attr("detailComKind"),
						"detailComFk"	: $(".scriptContext").eq(i).attr("detailComFk"),
						"ifcase"		: ifcase.trim(),
						"ifcasedetail"	: ifcasedetail.trim()
				}
//				scriptArray.push(scriptJson);
			}
			}
						
		
		
		// 상세 스크립트 수정 함수 호출
		var updateSucc = updateScriptDetail(scriptJson);
		if(!updateSucc) {
			alert("저장 중 오류가 발생했습니다.\r\n잠시 후 다시 시도 바랍니다.");
			return;
		}
		

		// 상세 스크립트 저장 함수 호출
		var insertSucc = insertScriptDetail(insertDetailArray);
		if(insertSucc) {
			alert("정상적으로 저장되었습니다.");
		} else {
			alert("저장 중 오류가 발생했습니다.\r\n잠시 후 다시 시도 바랍니다.");
		}
	});
	
	// //!!!!!!!!!!!!공용문구 팝업!!!!!!!!!
	$("#common_sentence_btn").click(function() {
		if(searchScriptCommonGrid == null) {	
			searchScriptCommonGrid = createGrid("search_scriptCommon_edit_title1", "right_paging2", "scriptCommonSearchGrid", "&flag=1", "", 100, [], []);
		
		}
		layer_popup("#commomSentence");
		
	});
		

	$("#common_script_searchBt").click(function() {
	    var searchName = $("#popup_searchScript").val();
	    if (searchName.trim().length == 0) {
//	        alert("검색 내용을 입력해 주세요.");
//	        return;
	    }
	    searchScriptCommon('2');
	}) 


	
	$(function () {
		$(".ui_padding_1").sortable({
			flow:"horizontal",
			axis:"x",
			revert : false,
			scroll:false,
			containment:"parnt"
		});
	});
	s=0;
	h=0;
    
	//삭제항목 체크 담아두기
	$("#popFirstDelete_script_btn").click(function() {
		if(!confirm("정말로 삭제 하시겠습니까?")) {
			return false;
		}
		var productpk = '';
		var Parent = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),2).getValue();
		var productpk = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),3).getValue();
		var fk = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),4).getValue();
		var chkNew = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),6).getValue();
		
		if(chkNew == "new" || chkNew == "new_child") {
			alert("수정중인 데이터를 저장 후 사용해 주세요.");
			return;
		}
	
		var dataStr = {
				"rScriptStepPk": productpk,
				"rScriptStepParent" :  popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(),2).getValue()
		}
		$.ajax({
			url:contextPath+"/deleteTreeScript.do",
			type:"POST",
			data:dataStr,
			dataType:"json",
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
					var result = jRes.resData.deleteTreeScript;

 					popUpScriptGrid.deleteRow(popUpScriptGrid.getSelectedId());
     				fk = scriptGrid.cells(scriptGrid.getRowId(0), scriptGrid.getColIndexById('rScriptStepPk')).getValue();
     				scriptGrid.clearAndLoad(contextPath + "/scriptGrid.xml" + "?rs_script_step_fk="+fk);
     				alert("삭제되었습니다.");
				} else {
					alert("삭제 실패");
				}
			}
		});
	});
    		

   //스크립트 목록 추가 _수정
   	$("#productEditBtn").click(function() {
    	popUpScriptGrid.editStop();
    	// 추가추가
    	var addCnt = popUpScriptGrid.getRowsNum();
    	var prodductAddArray = [];    		
    	insertDetailArray = [];
    	var rScriptStepFk = "";
    	var rScriptStepPk = "";
   		var rScriptStepParent = "";
   		var rScriptStepName = "";
   		var rScriptStepType = "";

   		//부모
    	for(var i=0; i<addCnt; i++) {  
    		var chkNew = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),6).getValue();
    		if(chkNew == "new") {
    			rScriptStepFk= popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),4).getValue();
    			rScriptStepParent = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),2).getValue();
    			rScriptStepName = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),0).getValue();   				
    			rScriptStepType = "N";
    			prodductAddArray.push(rScriptStepFk+"//"+rScriptStepParent+"//"+rScriptStepName+"//"+rScriptStepType);
    		}
    	}
    		
    	//자식
    	for(var i=0; i<addCnt; i++) {
    		var chkNew = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),6).getValue();
    		if(chkNew == "new_child") {
    			rScriptStepFk= popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),4).getValue();
    			rScriptStepParent = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),2).getValue();
    			rScriptStepName = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),0).getValue();
    			rScriptStepPk = popUpScriptGrid.cells(popUpScriptGrid.getSelectedRowId(i),3).getValue();
    			rScriptStepType = "N";
    			prodductAddArray.push(rScriptStepFk+"//"+rScriptStepParent+"//"+rScriptStepName+"//"+rScriptStepType);
    		}
    	}

    	//추가추가
    	if(prodductAddArray.length>0) {    		
    		var dataStr = {
				"prodductAddArray":prodductAddArray
			}
    		$.ajax({
    			url:contextPath+"/addTreeScript.do",
    			type:"POST",
    			data:dataStr,
    			dataType:"json",
    			async: false,
    			success:function(jRes) {
    				if(jRes.success == "Y") {
    					var result = jRes.resData.tempResult;
    					for(var k=0; k < result; k++) {
    						pkarray.push(result.rScriptStepPk);
    					}						
    				} else {
    					alert("저장 실패");
    				}
    			}
    		});   
    	}
   		
    	// 수정수정  productEditBtn  //addOrderStep(rsScriptStepFk)
    	var editCnt = popUpScriptGrid.getRowsNum();
    	var editChArray = [];
    	var productpk = "";
    	var productparant = "";
    	var scriptStepName = "";
    	var fk = "";
    	for(var i=0; i<editCnt; i++) {
    		var chkNew = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),6).getValue();
    		if(chkNew !="new" && chkNew !="new_child") {
    			rScriptStepParent = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),2).getValue();
    			productpk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),3).getValue();
    			fk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),4).getValue();
    			rScriptStepName = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),0).getValue();
    			editChArray.push(productpk+"//"+rScriptStepParent+"//"+rScriptStepName);
    		} else {
    			fk = popUpScriptGrid.cells(popUpScriptGrid.getRowId(i),4).getValue();
    		}
    	}
    	
    	var dataStr = {					
			"editChArray" : editChArray					
    	}	
		$.ajax({
			url:contextPath+"/editTreeScript.do",
			type:"POST",
			data:dataStr,
			dataType:"json",
			traditional:true,
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
					var result = jRes.resData.editTreeScript;
 					alert("수정 성공");
 					loading(fk);
				} else {
					alert("수정  실패");
				}
			}
		});
    });
    	
	function loading(fk) {    		
		setTimeout(function() {    			
			scriptGrid.clearAndLoad(contextPath + "/scriptGrid.xml" + "?rs_script_step_fk="+fk);
		}, 10);
//		scriptGrid.expandAll();
		popUpScriptGrid.clearAndLoad(contextPath + "/popUpScriptGrid.xml" + "?rs_script_step_fk="+fk);
//		popUpScriptGrid.expandAll();
	}

	function addOrderStep() {
		$.ajax({
			url:contextPath+"/selectScriptDetailPK.do",
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
					var result = jRes.resData.selectScriptDetailPK;
					scStepPK = jRes.resData.selectScriptDetailPK[0]-1;
				} else {
					alert("저장 실패");
				}
			}
		});
	}

	// 스크립트 검색 닫기
	$("#close_script_btn").click(function() {
		$(".first_right_box").addClass("enable");
		$(".second_right_box").addClass("disable");
	});

	// 스크립트 추가 팝업창 표출
	$(".add_prize_btn").click(function() {
		var checked = searchScriptGrid.getCheckedRows(0);	//id번호 = pk		
		if(checked.length > 0) {
			return false;
		}
		$(".insert_prize_name").val("");
		$(".insert_script_prize_text").val("");
		layer_popup("#AddScriptprizePopup");
	});
	
	// 스크립트 추가 팝업창 닫기
	$("#prizeScripCloseBtn").click(function() {
		layer_popup_close();
		$(".insert_prize_name").val("");
		$(".insert_script_prize_text").val("");
	});


	$("#back_script_btn").click(function() {
		if($(".search_box").hasClass("disable") == true) {
			$(".left_box").removeClass("enable");
			$(".left_box").addClass("disable");
			$(".first_right_box").removeClass("enable");
			$(".first_right_box").addClass("disable");
			$(".search_box").addClass("enable");
			$(".search_box").removeClass("disable");
		}
	});
	
	$('#audioPlayer').bind('ended', function() {
		if(playCount == ttsResult.resultPath.length) {
			audioTag.pause();
		}
		playCount++;
		audioTag.src = ttsResult[playCount].resultPath;
		
		if(playCount < ttsResult.resultPath.length) {
			audioTag.pause();
		}
	})
	
	ButtonYN();
	
	// 검색 엔터 처리
	$('#searchScript').keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	});
	$('#popup_searchScript').keyup(function(e) {
		if (e.keyCode == 13)
			$("#common_script_searchBt").trigger("click");
	});
	
	
}//window.onload 마감

/**
 * 화면에서 버튼으로 동작하는 함수의 이벤트 설정
 */
function formFunction() {
	// 팝업 창 닫기 버튼 클릭
	$('.popup_close').click(function() {
		commomUpdateInputBoxChangeRev();
		if(!$('#commonScriptUpdateBtn').hasClass("disable")) {
			$('#commonScriptUpdateBtn').addClass("disable");
			$('#commonUpdateBtn').removeClass("disable");
		}
		$("#toggleCommonScriptDetail").addClass("disable");
		$("#search_scriptCommon_edit_title1").addClass("enable");
		$("div[id=commomSentence]").css("height","624px");
		$("#productcloseBtn").addClass("enable");
		$("#commonDetailBackBtn").addClass("disable");
		$('#commonScriptSearchBar').addClass("enable");
		$('#commonInsertBtn').addClass("enable");
		$('#commonRemoveBtn').addClass("enable");
		$('#commonUpdateBtn').removeClass("enable");
	});
	
	// 조회 버튼 클릭
	$("#searchBtn").click(function() {
		searchScript();
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
				alert("fk립트를 즉시반영에 하였습니다.");
			},
			complete:function() {
				alert(fk+"번 스크립트를 즉시반영에 하였습니다.");
				progress.off();
			}
		});
	});
	
	// 공용문구 추가버튼
	$("#commonInsertBtn").click(function() {
		layer_popup("#commonScriptDetailPopup");		
	});
	
	// 공용문구 팝업창 닫기 버튼
	$("#commonScriptAddCloseBtn").click(function() {
		$("#commonScriptDetailPopup").fadeOut();
	});
	
	// 공용문구 뒤로가기버튼 기능
	$("#commonDetailBackBtn").click(function() {
		$("#toggleCommonScriptDetail").removeClass("enable");
		$("#search_scriptCommon_edit_title1").removeClass("disable");
		$("div[id=commomSentence]").css("height","503px");
		$("#productcloseBtn").removeClass("disable");
		$("#commonDetailBackBtn").removeClass("enable");
		$('#commonScriptSearchBar').removeClass("disable");
		$('#commonInsertBtn').removeClass("disable");
		$('#commonRemoveBtn').removeClass("disable");
		$('#commonUpdateBtn').addClass("disable");
		$('#commonScriptUpdateBtn').addClass("disable");
		commomUpdateInputBoxChangeRev();
		$('#common_script_searchBt').trigger('click');
	});
	
	// 공용문구 선택 삭제 버튼 기능
	$("#commonRemoveBtn").click(function() {
		var cnt = searchScriptCommonGrid.getCheckedRows(0);	//id번호 = pk		
		if(cnt.length == 0) {
			alert("삭제할 공용문구의 체크박스를 선택해 주세요");
			return false;
		}
		deleteCommonScript(cnt);
		searchScriptCommon('2');
	});

	//공용문구 선택 삽입 버튼 기능
	$("#commonCopytBtn").click(function() {
		var cnt = searchScriptCommonGrid.getCheckedRows(0);	//id번호 = pk		
		if(cnt.length == 0) {
			alert("삽입할 공용문구의 체크박스를 선택해 주세요");
			return false;
		}
		insertDetailArray=[];
		// stepFK, comFK
		if($(".scriptContext").attr("stepfk") != null	){
			var stepFK = $(".scriptContext").attr("stepfk");
		}else if($(".scriptContext").attr("stepfk") == null){
			var stepFK = scriptGrid.cells(scriptGrid.getSelectedRowId(), scriptGrid.getColIndexById('rScriptStepFk')).getValue();
		}
			
			
		var comFK = cnt;	
		var type = searchScriptCommonGrid.cells(searchScriptCommonGrid.getCheckedRows(0),3).getValue();
		var text = cnt;	
		var comKind = "Y";
		var ifcase = "N";
		var ifcasedetail = "null";
		
		// 디테일타입
		var scriptJson = {
				"detailType"	: type.trim()
			,	"detailText"	: text.trim()
			,	"scriptStepFk"	: stepFK
			,	"detailComFk"	: comFK
			,	"detailComKind"	: comKind.trim()
			,	"ifcase"		: ifcase.trim()
			,	"ifcasedetail"	: ifcasedetail.trim()
		}
		insertDetailArray.push(scriptJson);
		
		if(confirm("공용문구를 삽입하시겠습니까?")) {
			insertScriptDetail(insertDetailArray);
			searchScriptCommon('2');
			layer_popup_close();
		} else {
			alert("공용문구 삽입이 취소되었습니다.");
		}
	});
	
	// 스크립트목록 - 목록 수정 버튼 클릭
	$("#add_script_btn").click(function() {
		layer_popup("#addProduct");	
	});
	
	// 취소버튼 클릭 시 팝업창 닫기
	$("#productcloseBtn").click(function() {
		alert("진행중인 작업은 저장되지 않습니다.");
		layer_popup_close();
	});
	$(".script_step_Btn").click(function() {
		alert("진행중인 작업은 저장되지 않습니다.");
		layer_popup_close();
	});
	$(".scriptStepClose").click(function() {
		alert("진행중인 작업은 저장되지 않습니다.");
		layer_popup_close();
	});
	
	// 트리그리드 클릭시 메뉴 표출
	$("#scriptGrid").click(function() {
		$(".second_right_box").addClass("disable");
	});
	
	// 스크립트 삭제 버튼
	$(document).on("click", "#delete_content_Btn", function() {
		if(!confirm("정말로 삭제 하시겠습니까?")) {
			return false;
		}
		
		var detailtype = $(this).parent().next().children().attr("detailtype");
		var scriptkey = $(this).parent().next().children().attr("scriptkey");
		var stepFk = $(this).parent().next().children().attr("stepFK");
		
		var dataStr = {
			"rScriptDetailType"	: detailtype,
			"rScriptDetailPk"	: scriptkey,
			"rScriptStepFk"		: stepFk
		}
			
		$.ajax({
			url:contextPath+"/deleteScriptDetail.do",
			type:"POST",
			dataType:"json",
			data : dataStr,
			traditional:true,	
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
					var result = jRes.resData.deleteScriptDetail;
					
					//컨텐츠 타이틀 가져오기
					var title = $("#script_edit_title2").text();
					
					//스크립트그리드 개수 및 이름 가져오기
					var num = scriptGrid.getRowsNum();
					for(var i=0; i < num; i++) {
						var gridTitle = scriptGrid.getItemText(scriptGrid.getRowId(i))
						if(title == gridTitle) {
							var rScriptStepFk = scriptGrid.cells(scriptGrid.getRowId(i), scriptGrid.getColIndexById('rScriptStepFk')).getValue();
							var rScriptStepPk = scriptGrid.cells(scriptGrid.getRowId(i), scriptGrid.getColIndexById('rScriptStepPk')).getValue();
							var rScriptName = scriptGrid.cells(scriptGrid.getRowId(i), scriptGrid.getColIndexById('scriptName')).getValue();
							
							var rScriptArr= {
								"rScriptStepPk"	: rScriptStepPk,
								"scriptName"	: rScriptName
							}
							loadScriptDetail(rScriptStepFk, rScriptArr);		
						}
					}
					alert("삭제 성공");
				} else {
					alert("삭제 실패");
				}
			}
		});
	});
	
	//그룹 더보기 버튼 클릭
	$("#group_code_btn").click(function() {
		
		layer_popup("#groupCodePopup");
	});
	//그룹 더보기 팝업창에서 조회버튼 클릭
	$("#group_script_searchBt").click(function() {
		var rProductCode = $("#popup_searchScript_group").val();
		var dataStr = {
			"rProductCode"	: rProductCode
		}
		$.ajax({
			url:contextPath+"/`.do",
			type:"POST",
			dataType:"json",
			data : dataStr,
			traditional:true,
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
					productListGroup = jRes.resData.selectGRProductList;
					scriptGroupGrid = createGrid("search_group_title",	"right_paging", "scriptGroupGrid", "&flag=2", "", 20, [], []);
				} else {
					
				}
			}
		});
	});
}

// 조회 버튼 클릭 시 검색란에 표출
function searchScript() {
	var data = "";
	data += "keyword="+ $('#searchScript').val().trim();
	data += "&action=" + $('#scripSearchtType option:selected').val();
	data += "&productType="+$('#scriptType option:selected').val();
	searchScriptGrid.clearAndLoad(contextPath+"/scriptSearchGrid.xml?" + encodeURI(data));
};



// 화면 로드 시 그리드 그리는 함수
function init() {
	// 목록수정 트리그리드
	popUpScriptGrid = createScriptStepGrid("addscriptTitle","recMemoAddButton","popUpScriptGrid","?header=true&rs_script_step_fk=1","",20,[],[]);
	
	// 트리그리드 이미지 없애기
	popUpScriptGrid.setImageSize(1,1);
	popUpScriptGrid.enableAutoWidth(true);

	// 스크립트 검색 그리드 헤더
	searchScriptGrid = createGrid("search_script_edit_title1","right_paging","scriptSearchGrid","?header=true","", 100, [], []);
	searchScriptGrid.enableAutoWidth(true);
	
	searchScriptCommonGrid = createGrid("search_scriptCommon_edit_title1", "right_paging2", "scriptCommonSearchGrid", "?flag=1", "", 100, [], []);
	
	// 그리드에 기능 등록
	gridFunction();

	// 플레이어
	audioTag = document.getElementById("audioPlayer");
}

/**
 * 스크립트 수정 화면에서 저장 버튼 클릭 시 기존 스크립트 항목 Update 하는 함수
 * @param array	기존 스크립트 항목
 */
function updateScriptDetail(array) {
	var updateSucc = false;
	
	if(array.length == 0) {
		updateSucc = true;
	} else {
		var dataStr =  JSON.stringify(array)
		$.ajax({
			url:contextPath+"/updateScriptDetail.do",
			type:"POST",
			dataType:"json",
			data : dataStr,
			traditional:true,	
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
					updateSucc = true;
				}
			}
		});
	}
	
	return updateSucc;
}

/**
 * 스크립트 수정 화면에서 저장 버튼 클릭 시 신규 스크립트 항목 Insert 하는 함수
 * @param array	신규 스크립트 항목
 */
function insertScriptDetail(array) {
	var insertSucc = false;	
	if(array.length == 0) {
		insertSucc = true;
	} else {
		var dataStr = {
			"insertDetailArray"	: JSON.stringify(array)
		}
	$.ajax({
		url:contextPath+"/addScriptDetail.do",
		type:"POST",
		dataType:"json",
		data : dataStr,
		traditional:true,	
		async: false,
		success:function(jRes) {
			if(jRes.success == "Y") {					
				var title = $("#script_edit_title2").text();
				//스크립트그리드 개수 및 이름 가져오기
				var num = scriptGrid.getRowsNum();					
				for(var i=0; i<num; i++) {
					var gridTitle = scriptGrid.getItemText(scriptGrid.getRowId(i));
					if(title == gridTitle) {
						var rScriptStepPk = scriptGrid.cells(scriptGrid.getRowId(i), scriptGrid.getColIndexById('rScriptStepPk')).getValue();
						var rScriptStepFk = scriptGrid.cells(scriptGrid.getRowId(i), scriptGrid.getColIndexById('rScriptStepFk')).getValue();
						var rScriptName = scriptGrid.cells(scriptGrid.getRowId(i), scriptGrid.getColIndexById('scriptName')).getValue();
						var rScriptArr = {
							"rScriptStepPk"	: rScriptStepPk,
							"scriptName"	: rScriptName
						}
						loadScriptDetail(rScriptStepFk, rScriptArr);
					};
				}
				insertSucc = true;
			}
		}		
	});
	}
	
	return insertSucc;
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

// "@" 입력시 자동 완성 함수
function automaticPhrase(realTimeValueArray) {
	var datas = [];
	for(var i=0; i<realTimeValueArray.length; i++) {
		datas.push(realTimeValueArray[i]);
	}
	var TRIGGER = '@'   //괄호열리고 시작키워드
		, QUERY = ''					// {@ 이후 검색한 코드
		, DIVISION ='{'
		, SEARCH = false
		, SELECT_POS = 0
		, EDITOR = $('.script_input')	// 어떤태그에서 에디터를 사용할것인지
		, LIST = $(".autoList")
		, IDX = 0						// 변경할스크립트가 많으면 칸도많아지지, 그럼 결국 IDX가 필요
	;
	LIST.css({
		'position' : 'absoulte',
		'width' : '200px',
		'background-color' : 'white',
		'overflow' : 'auto'
	});
	
	//query 가 포함된 단어 찾기
	function searchWordFromArray(query) {
		return function(word) {
				return word.indexOf(query) > -1;
		}
	}
	
	// 단어 검색
	function getWords(query) {
		return datas.filter(searchWordFromArray(QUERY));
	}

	// 커서를 마지막으로 이동
	function setEndOfContenteditable(contentEditableElement) {
		var range, selection;
		if(document.createRange) {
			range = document.createRange();
			range.selectNodeContents(contentEditableElement);
			range.collapse(false);
			selection = window.getSelection();
			selection.removeAllRanges();
			selection.addRange(range);
		} else if(document.selection) {
			range = document.body.createTextRange();
			range.moveToElementText(contentEditableElement);
			range.collapse(false);
			range.select();
		}
	}
	
	function initList() {
		LIST.empty();
		LIST.addClass('hide');
		SELECT_POS = 0;		//list에서 현재 선택될 데이터
		SEARCH = false;
	}
	
	//선택한 스크립트 태그를 만들어 리턴해준다.
	function createWordHtml(index) {
		var selectEl = $('div:eq('+SELECT_POS+')',LIST.get(index));
		//이게 키입력받고 만들어지는 html이네																		// {체크한목록이름}
		var html = '<span class = "editor-link" style=" display: inline-block; color:red;" contenteditable = "false" href = "#" >' + (DIVISION+selectEl.text()) + '}</span>';
		initList();
		return html;
	}
	
	//스크립트란 체크시 scriptindex검사후 IDX에 저장
	EDITOR.parent().parent().click(function() {
		var i = 0;
		var z = 0;
		for (i = 0; i < $(this).children().length; i++) {
			if($(this).children().eq(i).children().hasClass('script_input')) {	//이틀을 깨지 말것
				var j = $(this).children().eq(i).children().length;
				for (var z = 0; z < j; z++) {
					if($(this).children().eq(i).children().eq(z).hasClass('script_input')) {
						IDX = $(this).children().eq(i).children().eq(z).attr('scriptindex');	//scriptindex = IDX와 맞출 인덱스번호 (수정할 스크립트 span 에 있음)
					}
				}
			}
		}
	})
	
	//키 입력 받음 
	EDITOR.keypress(function(e) {
		if(e.which == 13) {
			if(SEARCH) {
				var index = this.attributes.scriptindex.value;
				var createHtml = createWordHtml(index);
				$(EDITOR.get(parseInt(IDX))).html($(EDITOR.get(parseInt(IDX))).html().replace(DIVISION+TRIGGER + QUERY, createHtml));
				setEndOfContenteditable(EDITOR.get(IDX));
			}
		}
	});
	
	EDITOR.parent().parent().keypress(function(e) {
		if(e.which == 13) {
			if(SEARCH) {
				var index = $("this").children().children().attr('scriptindex');
				var createHtml = createWordHtml(index);
				
				$(EDITOR.get(parseInt(IDX))).html($(EDITOR.get(parseInt(IDX))).html().replace(DIVISION+TRIGGER + QUERY, createHtml));
				setEndOfContenteditable(EDITOR.get(IDX));
			}
		}
	});

	//화살표
	EDITOR.parent().parent().keydown(function(e) {
		if(SEARCH) {
			if(e.which==38) {
				// ↑
				if(SELECT_POS > 0) { 
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","white");
					SELECT_POS--;
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","blue");
					return false;
				} else {
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","white");
					SELECT_POS = words.length;
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","blue");
					return false;
				}
			} else if (e.which == 40) {
				// ↓
				if(SELECT_POS < words.length -1) {
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","white");
					SELECT_POS++;
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","blue");
					return false;
				} else {
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","white");
					SELECT_POS = 0;
					$('div:eq('+SELECT_POS+')',LIST).css("background-color","blue");
					return false;
				}
			}
		}
	});

	//커서 확인 할 수 있도록 keyup해서 list에  값 넣기
	EDITOR.parent().parent().keyup(function(e) {
		if (e.which == 13) {
			return;
		}
		
		//var text = $(this).text(),				// 키가 눌릴때마다 html이 text에 담기고
		var text = window.getSelection().anchorNode.wholeText;
		trigger_pos = text.lastIndexOf(TRIGGER);		// @를 찾는 마지막 인덱스가 trigger_pos에 담김 -1일시 맨마지막임
		// "{" 괄호 찾기
		var tmpTrigger_pos;
		var inputText = window.getSelection().anchorNode.wholeText;
		
		if(trigger_pos > -1) {
				var trigger_end = window.getSelection().anchorOffset;
				QUERY = text.substring(trigger_pos + 1, trigger_end).trim();
				QUERY = QUERY.replace(/}/gi,"");
				console.log("keyup Query : "+QUERY);
				//QUERY = keyword -->>> 
				let words = '';
				if(QUERY != '') words = getWords(QUERY); 
				
				if( words == null || words == 'undefined' || words.length == 0 ){
					initList();
					return;
				}
				
				
				SEARCH = true;
				selection = window.getSelection();
				var node = selection.focusNode;
				var offset = selection.anchorOffset;
				LIST.empty();
				var html = '';
				$.each(words, function(i, word) {
					html += '<div class="list-group-item"  contenteditable = "false" wordsIdx="'+i+'" style="border: 1px solid #0067ac;" href="#">' + word + '</div>';
				});
				
				//SELECT_POS = 리스트상에서 선택되어져있는 인덱스 번호
				LIST.eq(IDX).html(html);
				LIST.eq(IDX).removeClass('hide');
				$('div:eq('+SELECT_POS+')',LIST).css("background-color","#85d3ff");
				
				//마우스 원클릭 이벤트 LIST마다 클릭이벤트 부여 ( SELECT_POS ) 에맞게 버튼 색칠
				for(var i=0; i<words.length; i++) {
					$('div:eq('+i+')',LIST).click(function(i) {
						//this = 태그 html 자체
						$('div:eq('+SELECT_POS+')',LIST.eq(IDX)).css("background-color","white");
						SELECT_POS= this.attributes[2].value;
						$('div:eq('+SELECT_POS+')',LIST.eq(IDX)).css("background-color","#85d3ff");
						setEndOfContenteditable(EDITOR.get(IDX));
					});
					//문구박스 더블클릭이벤트
					$('div:eq('+i+')',LIST).dblclick(function(i) {
						var createHtml = createWordHtml();
						var editHtml = EDITOR.get(IDX).innerText;
						$(EDITOR.get(parseInt(IDX))).html($(EDITOR.get(parseInt(IDX))).html().replace(DIVISION+TRIGGER + QUERY, createHtml));
						setEndOfContenteditable(EDITOR.get(IDX));
					});
				}
				
				// enter
				if(e.which ==13) {
					return false;
				}
				$('div', LIST).removeClass('active');
				var taa = inputText.substring(0,trigger_pos).replace(/{/gi,"")
				taa = taa.substring(taa.indexOf("<span style=",0)).replace(/<span style="color:#1e34db;">/gi,"")

				$('div:eq('+ SELECT_POS + ')', LIST).addClass('active');
				
		} else {
			initList();
		}
	}); // KEYUP
	
	//새로운 내용추가 삭제 
	$(".delete_new_contents").click(function(e) {
		$(this).parent().parent().remove();
	});
};

function modifyScript(id) {
	if (id == "") {
		$("#scriptName").val("");
		$("#scriptType").val("");
		$("#script").val("");
		$("#scriptCode").val("");
	} else {
		if (scriptJsonArray != undefined && scriptJsonArray.length > 0) {
			var obj = getListFilter(scriptJsonArray, "scriptCode", id);
			$("#scriptName").val(obj[0].scriptName);
			$("#scriptType").val(obj[0].scriptType);
			$("#script").val(obj[0].script);
			$("#scriptCode").val(id);
		}		
	}
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

//트리 그리드
function createScriptStepGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setPagingSkin("toolbar","dhx_web");
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	objGrid.enableColumnAutoSize(false);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	strData = "?header=true";
	// 테스트일떈 주석처리
//	strData = "?header=true"+strData;
	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData), function() {
		objGrid.attachEvent("onXLS", function() {
			progress.on();
		});

		
		// 파싱완료
		objGrid.attachEvent("onXLE", function(grid_obj,count) {
			ui_controller();
			progress.off();
			
			var rScriptStepFk =grid_obj.cells(grid_obj.getRowId(0),grid_obj.getColIndexById('rScriptStepFk')).getValue();
			var rScriptStepPk =grid_obj.cells(grid_obj.getRowId(0),grid_obj.getColIndexById('rScriptStepPk')).getValue();
			var scriptName =grid_obj.cells(grid_obj.getRowId(0),grid_obj.getColIndexById('scriptName')).getValue();
			var rProductCode = $("#product_code_value").text();
			
			var scriptInfo = {
				rScriptStepFk : rScriptStepFk,
				rScriptStepPk : rScriptStepPk,
				rProductCode : rProductCode,
			}
			
			loadScriptDetail( scriptInfo);
		});
	});
	return objGrid;
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
function createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn) {
	
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	
	objGrid.i18n.paging = i18nPaging[locale];
	objGrid.enablePaging(true, initPageRow, 5, objPaging, true);
	objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	objGrid.setPagingSkin("toolbar","dhx_web");
	objGrid.enableColumnAutoSize(false);
	objGrid.enableMultiline(true);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	// Load Grid
	objGrid.load(contextPath + "/" + url + ".xml" + encodeURI(strData), function() {
		var grid_toolbar = objGrid.aToolBar;
		grid_toolbar.addSpacer("perpagenum");
		grid_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>')
		grid_toolbar.setWidth("total",80)
		
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

			if (objGrid.getRowsNum() > 0){
				var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ " "+objGrid.getRowsNum()+" "+objGrid.i18n.paging.found+'</div>'
				objGrid.aToolBar.setItemText("total", setResult)
			}
			
			ui_controller();
			progress.off();

			$(window).resize();
			$(window).resize(function() {
				objGrid.setSizes();
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
	
	objGrid.attachEvent("onDataReady", function() {
		console.log('=========onDataReady=========');
		if(!searchGridLoaded) {
			searchGridLoaded = true;
			searchScript();
		}
	});
	
	return objGrid;
} 

// 스크립트 수정 창에 표출
function scriptLoadGrid(key, arr) {
	var key = key;
	scriptGrid.clearAndLoad(contextPath + "/scriptGrid.xml" + "?rs_script_step_fk="+key, function() {
		scriptGrid.expandAll();		
		// 상세페이지 우측 화면
    	//loadScriptDetail(null, arr);
	});
}

function scriptLoadGrid2(key) {
	var key = key;
	popUpScriptGrid.clearAndLoad(contextPath + "/popUpScriptGrid.xml" + "?rs_script_step_fk="+key, function() {
		popUpScriptGrid.expandAll();
	});
}

// Grid에 기능 붙이는 함수
function gridFunction() {
	

	searchScriptGrid.attachEvent("onRowDblClicked", function(id, ind) {
		
    	if($(".search_box").hasClass("enable") == true) {
			$(".left_box").removeClass("disable");
			$(".left_box").addClass("enable");
			$(".first_right_box").removeClass("disable");
			$(".first_right_box").addClass("enable");
			$(".search_box").addClass("disable");
			$(".search_box").removeClass("enable");		
		}
  
		// 자동완성기능 미리 호출
    	var rScriptStepFk = id;
//		var rProductType = searchScriptGrid.cells(searchScriptGrid.getSelectedId(), 4).getValue();	// 선택한 로우 가지고 오기
		var rProductType = searchScriptGrid.cells(id, searchScriptGrid.getColIndexById('rsScriptType')).getValue();	// 선택한 로우 가지고 오기
		checkRealTimeTTS(rScriptStepFk, rProductType);
		automaticPhrase(realTimeValueArray);
		
		// 상세페이지 좌측 화면 
    	var Fk = searchScriptGrid.cells();
		var rScriptName = searchScriptGrid.cells(id, searchScriptGrid.getColIndexById('rsScriptName')).getValue();
		var arr = {
			"rScriptStepPk"	: ""+rScriptStepFk,
			"scriptName"	: null
		};
		productLoad();
		
		
		// 트리그리드
		scriptGrid = createScriptStepGrid("scriptGrid", "recMemoAddButton","scriptGrid", "?header=true&rs_script_step_fk=1", "", 20, [],[]);
		// 트리그리드 이미지 없애기
		scriptGrid.setImageSize(1, 1);
		scriptGrid.enableAutoWidth(true);
		//전체듣기 초기화
		allListenArray = [];
		//스크립트 그리드 더블클릭
		scriptGridDbl();
		// 스크립트 그리드 리로드(clearAndLoad)
		scriptLoadGrid(rScriptStepFk, arr);
		
	});
	

	
	searchScriptCommonGrid.attachEvent("onRowDblClicked", function() {
		$("#toggleCommonScriptDetail").addClass("enable");
		$("#search_scriptCommon_edit_title1").addClass("disable");
		$("div[id=commomSentence]").css("height","624px");
		$("#productcloseBtn").addClass("disable");
		$("#commonDetailBackBtn").addClass("enable");
		$('#commonScriptSearchBar').addClass("disable");
		$('#commonInsertBtn').addClass("disable");
		$('#commonRemoveBtn').addClass("disable");
		$('#commonUpdateBtn').removeClass("disable");
		
		var selectRowNumPk = searchScriptCommonGrid.getSelectedId();
		openCommonScriptDetailPopup(selectRowNumPk);
	})

}

//스크립트 그리드 더블클릭시 동작
function scriptGridDbl() {
	incres=0;
	// 스크립트 목록 그리드
	scriptGrid.attachEvent("onRowDblClicked", function(id, ind) {
		var rScriptStepFk = scriptGrid.cells(id,scriptGrid.getColIndexById('rScriptStepFk')).getValue();
		var rScriptStepPk = scriptGrid.cells(id,scriptGrid.getColIndexById('rScriptStepPk')).getValue();
		var scriptType = searchScriptGrid.cells(searchScriptGrid.getSelectedId(),searchScriptGrid.getColIndexById('rsScriptType')).getValue();
		var productPk = scriptGrid.cells(id,scriptGrid.getColIndexById('rScriptStepPk')).getValue();
		var rScriptInfo = {
			rScriptStepPk : rScriptStepPk,
			scriptName : scriptGrid.cells(id,scriptGrid.getColIndexById('scriptName')).getValue(),
			rProductCode :searchScriptGrid.cells(searchScriptGrid.getSelectedId(), searchScriptGrid.getColIndexById('rsProductCode')).getValue(),
			rScriptStepFk : rScriptStepFk 	
		}

		loadScriptDetail(rScriptInfo);
		$(".second_right_box").addClass("disable");
		$(".first_right_box").addClass("enable");
	});
}

function loadScriptDetail(scriptInfo) {
	
	var isDblDirect = (scriptInfo.rScriptStepFk == null) ? true : false;
	var realscriptName = scriptInfo.scriptName;
	var pk = 0;
	if (scriptInfo.rScriptStepPk != null) {
		pk = scriptInfo.rScriptStepPk;
	}
//	var dataStr = null;
//	if (pk != 0) {
//		if (rScriptStepFk == null) {
//			dataStr = {
//				rScriptStepPk : scriptName.rScriptStepPk,
//				flag : "dblClick"
//			}
//		} else {
//			dataStr = {
//				rScriptStepFk : 	rScriptStepFk,
//				rScriptStepPk : 	scriptName.rScriptStepPk,
//				rProductType 	: 	scriptName.scriptType,
//				rProductCode  : 	scriptName.rProductCode
//			}
//		}
//	} else {
//		dataStr = {
//			rScriptStepFk : rScriptStepFk,
//			rProductType : scriptName.scriptType
//		}
//	}
	//detaResultData
	var stepPk = scriptInfo.rScriptStepPk;
	var stepFk = scriptInfo.rScriptStepFk;
	var productCode = scriptInfo.rProductCode;
	var apiUri = "/script/step/"+stepPk+"/detail/?rScriptStepFk="+stepFk+"&rProductCode="+productCode;
	
//	console.log("datastr:"+dataStr);
	console.log("contextPat:"+apiUri);

	$.ajax({
		url : contextPath + apiUri,
		type : "GET",
		dataType : "json",
		async : false,
		success : function(jRes) {

			if (jRes.success == "Y") {
				$("#script_edit_title2").html(realscriptName);
				$("#script_edit_content3").html("");
				if (isDblDirect) {
						// 스크립트 검색 화면에서 Grid DblClick으로 화면 전환이 된 경우
					$("#script_edit_title2").html(scriptGrid.cells(scriptGrid.getRowId(0), scriptGrid.getColIndexById('scriptName')).getValue());
					scriptGrid.selectRowById(1);
				}
				// 선택한 로우 가지고 오기
				var audio = document.getElementById("audioPlayer");
				var rProductCode = searchScriptGrid.cells(searchScriptGrid.getSelectedId(),searchScriptGrid.getColIndexById('rsProductCode')).getValue();
				
				// 선택한 로우 가지고 오기
				var rScriptStepFk = searchScriptGrid.cells(searchScriptGrid.getSelectedId(),searchScriptGrid.getColIndexById('rsScriptStepFk')).getValue();
				// 선택한 로우 가지고 오기
				var rProductType = searchScriptGrid.cells(searchScriptGrid.getSelectedId(),searchScriptGrid.getColIndexById('rsScriptType')).getValue();
					checkRealTimeTTS(rScriptStepFk, rProductType);
					resultData = jRes.resData.scriptDetailList;						
				var scriptDetailInfoList = resultData;
				for (var i = 0; i < scriptDetailInfoList.length; i++) {
					//test
					displayScriptDetailBlock(resultData[i],i);							
				}
				automaticPhrase(realTimeValueArray);
			} 
		}
	});
}

////////////////////////////////////////
	function displayScriptDetailBlock(scrtiptDetailInfo,ind){
		
		var commonType = scrtiptDetailInfo.rScriptDetailComKind ;
		var rScriptDetailType= scrtiptDetailInfo.rScriptDetailType ;
		var rScriptIfCase = scrtiptDetailInfo.rScriptDetailIfCase ;
		var rScriptIfCaseDetail = scrtiptDetailInfo.rScriptDetailIfCaseDetail ;
		var rScriptDetailIfCaseCode = scrtiptDetailInfo.rScriptDetailIfCaseCode ;
		var rScriptDetailIfCaseDetail = scrtiptDetailInfo.rScriptDetailIfCaseDetail ;
		var rScriptDetailIfCaseDetailCode = scrtiptDetailInfo.rScriptDetailIfCaseDetailCode ;
		var disabledVal = "";
		
		if(scrtiptDetailInfo.rScriptDetailComKind == 'Y'){
			disabledVal = "disabled";
		}
		
		var html ='';
		html += '';
		html += "<div id='script' class='script'>";
		html += 	"<div id='script_BtnLine"+ind+"' class='BtnLine' style='height : 30px'>";
		//[공통문구]**리딩 혹은 **리딩 표출되는 function
		html += detailTextCommonCheck(commonType,rScriptDetailType,writeCommonText,recValue);    
		html += 	"<input type='button' id='delete_content_Btn' class='delete_contentBtn' style='background-color: white;  ' value='X' >";
		//조건 선택해주는   onchange='setScriptDetailCaseType("+ind+")'
		html += 		"<div id='selectLine' style='height:30px; width:350px; float:right;'>"
		html +=				"<select  id='typeselect"+ind+"'  class='type_select'  style='background-color: white; width:150px;' "+ " name='조건추가' onchange='selectChange(this,"+ind+")' >";
		html += 				scriptSelectBox(ind,rScriptIfCase,rScriptIfCaseDetail,rScriptDetailIfCaseCode,rScriptDetailIfCaseDetail);			
		html += 			"</select>";		
		html += 			"<select  id ='typeValue"+ind+"' class='type_value'  style='background-color: white; display:none; width:177px;' >";			
		html += 			"</select>";
		html += 			"<input type='text' id='typeText"+ind+"' class='type_Text' style='height : 20px; width:177px; margin-right:10px;' placeholder='여기에 입력하세요' />";
		html +=			"</div>"
		html +=		"</div>"
			
		html += 	"<div id='script_textAtrea' class='textAtrea''>";			
					var text = scrtiptDetailInfo.rScriptDetailText;
					text = text.replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi, "}</span>");		
		
		html +=	"<div class='Script_line scriptContext script_input'>";
		html += '	<pre contenteditable="true">'+ text.replace(/\\n/gi, "<br>")+ "</pre>";
		html += "</div>";
		// 전체듣기		
		if ($('.allListen').hasClass("enable")) {
				$('.allListen').removeClass("enable");
				$('.allListen').addClass("disable");
		}
		
		if ( scrtiptDetailInfo.rScriptDetailType == 'T'|| scrtiptDetailInfo.rScriptDetailType  == 'R') {
				allListenArray.push(text);
		}
		
		if ($('.allListen').hasClass("disable")) {
				$('.allListen').removeClass("disable");
				$('.allListen').addClass("enable");
		}
		
//			console.log("전체듣기 내역 : " + allListenArray[i])			
		// 오토리스트
		html += 	"<div id='list' class='panel panel-default hide autoList' autoList   scriptIndex=''>";
		html += 		"<div class='list-group'></div>";
		html += 	"</div>";
		html += "</div>";
		// 권한
		if (delYn == "Y") {
			$(".delete_contentBtn").css("display","block");
		}
		$("#script_edit_content3").append(html);
		
		setScriptDetailCaseType(rScriptDetailIfCaseDetail,ind,rScriptDetailIfCaseDetailCode);
		//첫번째 박스 select했을 시 유형에 맞게 다음 div형태값 변경해주는 event
		
	};		
////////////////////////////////////////

	//추가버튼 클릭시 추가되는 DIV형식
	 function createNewScriptForm(commonType,rScriptDetailType,ind){
		 	var rScriptDetailIfCaseDetail ;
		 	var rScriptDetailIfCaseDetailCode  ;
			var html = '';
			html += "<div id='script' class='script new' style='height : auto'>";
			html += 	"<div id='scriptBtnLine' class='scriptBtnLine' style='height : 30px' >";
			html +=				detailTextCommonCheck(commonType,rScriptDetailType,writeCommonText,recValue)
			html += 		"<input type='button' id='a' class='delete_new_contents' style='background-color: white' value='X' >";
			html += 		"<div class='selectLine'style='height:30px; width:350px; float:right;' >";
			html +=				"<select  id='typeselect_new"+ind+"'  class='type_select newSelect'  style='background-color: white; width:150px;' name='조건추가' onchange='selectChange(this,"+ind+")' >";	
			html +=					addScriptSelectBox();	
			html += 			"</select>";		
			html += 			"<select  id ='typeValue_new"+ind+"' class='type_value newType'  style='background-color: white; display:none; width:177px;' >";							
			html += 			"</select>";
			html += 			"<input type='text' id='typeTex_new"+ind+"' class='type_Text newText' style='height : 20px; width:177px; margin-right:10px;' placeholder='여기에 입력하세요' />";
			html +=				"</div>";
			html +=			"</div>";
			html += 	"<div id='scriptTextArea_new"+ind+"' class='scriptTextArea new' style='height:auto; overflow: auto;'>";
			html += 		"<div class='script_input newScript scriptContext ' id='script_input' value='text' tabindex='-1' scriptindex='"+$("#script_edit_content3").children().length+"' detailComKind = '' detailComFk = '' detailType = 'T' scriptKey='' detailOrder='' stepFK='' ifcase='' ifcasedetail='-' realTime=''>";
			html += 			'<pre contenteditable="true">'+"<br>"+"<br>"+"</pre>";
			html += 		"</div>";
			html += 		"<div id='list' class='panel panel-default hide autoList' autoList ' scriptIndex='"+$("#script_edit_content3").children().length+"'>";
			html += 			"<div class='list-group'></div>";
			html += 		"</div>";
	        html += 	"</div>";
			html += "</div>";

			$("#script_edit_content3").append(html);

			automaticPhrase(realTimeValueArray); 		 
			
			setScriptDetailCaseType(rScriptDetailIfCaseDetail,ind,rScriptDetailIfCaseDetailCode);
	 };

	/**공통문구 용인지 체크하는 로직
	 * @param commonType : script_detail 테이블의 rScriptDetailComKind(공통문구 사용여부)
	 * @param rScriptDetailType : script_detail 테이블의 rScriptDetailType
	 * @param commonCode : 선택된 상품의 공통문구에 대한 코드 
	 * @param commonName : 선택된 상품의 공통문구에 대한 이름 
	 * @param code : 선택된 rScriptDetailType의 코드 
	 * @param name : 선택된 rScriptDetailType의 이름 
	 * @returns
	 */
	function detailTextCommonCheck(commonType,rScriptDetailType,writeCommonText,recValue) {
		var html = "";		
			for(var i=0 ; i<recValue.length; i++){
				var scriptTypeVal= recValue[i].code
				if(scriptTypeVal == rScriptDetailType){
					var scriptTypeName = recValue[i].name;
				}				
			}
			if(commonType == 'Y') {	
					html = "<div id='genneralCheck' class ='genneralCheck' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' style='font-weight:bold; float:left; color:#0067ac; padding-left:10px; padding-top:4px;'>"+writeCommonText+" "+scriptTypeName+"</div>"			
			}else if(commonType == 'N'){		
					html = "<div id='genneralCheck' class ='genneralCheck' value ='"+scriptTypeVal+"' detailComKind='"+commonType+"' style='font-weight:bold; float:left; color:#0067ac; padding-left:10px; padding-top:4px;'>"+scriptTypeName+"</div>"
			}
		return html;
	}
	
	//스크립트 수정 페이지 상단의 내용 채워주기
	function productLoad() {
		// 상품코드 
		$("#product_code_value").html(searchScriptGrid.cells(searchScriptGrid.getSelectedId(), searchScriptGrid.getColIndexById('rsProductCode')).getValue());
		// 상품명 
		$("#product_name_value").html(searchScriptGrid.cells(searchScriptGrid.getSelectedId(), searchScriptGrid.getColIndexById('rsScriptName')).getValue());
		// 상품유형 
		$("#product_type_value").html(searchScriptGrid.cells(searchScriptGrid.getSelectedId(), searchScriptGrid.getColIndexById('rsScriptType')).getValue());			
	};


	function createFileTTS() {
		var mentTxt = "이 투자신탁은 환매가 가능한 개방형이며, 추가로 자금 납입이 가능한 추가형이고, "
			+ "판매보수 등의 차이로 인하여 기준가격이 다르거나 판매수수료가 다른 여러 종류의 집합투자증권을 "
			+ "발행하는 종류형 투자신탁입니다. 또한 투자신탁이 발행하는 집합투자증권을 자투자신탁이 취득하는 "
			+ "구조의 모자형투자신탁입니다."
			+ "펀드는 집합투자를 수행하는 기구로서 법적으로 집합투자기구라 표현되며, "
			+ "다수의 투자자의 돈을 모아서 운용사 집합투자업자)를 통하여 수익을 얻어 이를 투자자들이 "
			+ "나누어 갖게 됩니다. 펀드의 형태로는 투자신탁, 투자회사 등이 있으며, "
			+ "고객님께서 선택하신 펀드는 투자신탁 형태입니다.";
		var dataStr = {
			"mentTxt" : mentTxt
		}
		$.ajax({
			url:contextPath+"/createFileTTS.do",
			type:"POST",
			dataType:"json",
			data:dataStr,
			async: false,
			success:function(jRes) {
				if(jRes.success == "Y") {
				} else {
				}
			}
		});
	}
// 공용스크립트 삭제 AJAX
function deleteCommonScript(idx) {
	dataStr = {
		"idx" : idx
	}
	$.ajax({
		url : contextPath + "/deleteScriptCommon.do",
		data : dataStr,
		type : "POST",
		dataType : "json",
		async : false,
		success : function(jRes) {
			if (jRes.success == "Y") {
				alert("정상적으로 삭제되었습니다.");
			}
		},
		error : function(error) {
			alert("error [deleteCommonScript]")
		}
	});
}

//공용스크립트 추가 AJAX
function insertCommonScript(data) {
	var flag = "";
	$.ajax({
		url : contextPath + "/insertScriptCommon.do",
		data : data,
		type : "POST",
		dataType : "json",
		async : false,
		success : function(jRes) {
			if (jRes.success == "Y") {
				$('#insert_common_name').val("");
				$('#insert_common_desc').val("");
				$('#insert_common_text').val("");
				alert("공용문구가 정상적으로 등록 되었습니다.");
				flag= "Y";
			} else {
				alert("공용문구가 정상적으로 등록 되었습니다.");
				flag= "N";
			}
			$("#commonScriptDetailPopup").fadeOut();
		},
		error : function(error) {
			alert("error [deleteCommonScript]");
		}
	});
	return flag;
}

function updateCommonScript(data) {
	$.ajax({
		url : contextPath + "/updateScriptCommon.do",
		data : data,
		type : "POST",
		dataType : "json",
		async : false,
		success : function(jRes) {
			if (jRes.success == "Y") {
				alert("공용문구가 정상적으로 수정 되었습니다.");
				$('#commonScriptUpdateBtn').addClass("disable");
				$('#commonUpdateBtn').removeClass("disable");
				$('#common_name').attr("readonly","readonly");
				$('#common_name').addClass("readonly");
				$('#common_desc').attr("readonly","readonly");
				$('#common_desc').addClass("readonly");
				$('#common_text').attr("readonly","readonly");
				$('#common_text').addClass("readonly");
				$('#common_detail_type').attr('disabled',true);
				$('#common_detail_realTime').attr('disabled',true);
				$('#common_text').css("color","#666666");
				searchScriptCommon('1');
				var updateData = null;
				$.each(scriptCommonList,function(index,value) {
					if(data.pk == scriptCommonList[index].rsScriptCommonPk) {
						updateData = scriptCommonList[index];
					}
				})
				openCommonScriptDetailPopup(updateData);
				
			}
			if (jRes.success == "N") {
				alert("공용문구가 정상적으로 수정 되지 않았습니다.");
			}
		},
		error : function(error) {
			alert("error [deleteCommonScript]");
		}
	});
}
//공용문구 수정버튼 누를시 input 태그 변경
function commomUpdateInputBoxChange() {
	const commonValue = $('#common_name').val();
	const commonDesc = $('#common_desc').val();
	const commonText = $('#common_text').val();
	
	$('#common_name').removeAttr("readonly");
	$('#common_name').removeClass("readonly");
	$('#common_desc').removeAttr("readonly");
	$('#common_desc').removeClass("readonly");
	$('#common_text').removeAttr("readonly");
	$('#common_text').removeClass("readonly");
	$('#common_detail_type').removeAttr('disabled');
	$('#common_detail_realTime').removeAttr('disabled');
	$('#common_text').css("color","black");
	
	$('#common_name').focus();
}

// 공용문구 수정버튼 클릭후 뒤로가기, 닫기 버튼클릭시 원래 input박스로 돌아오는 이벤트
function commomUpdateInputBoxChangeRev() {
	$('#common_name').attr("readonly","readonly");
	$('#common_name').addClass("readonly");
	$('#common_desc').attr("readonly","readonly");
	$('#common_desc').addClass("readonly");
	$('#common_text').attr("readonly","readonly");
	$('#common_text').addClass("readonly");
	$('#common_detail_type').attr('disabled',true);
	$('#common_detail_realTime').attr('disabled',true);
	$('#common_text').css("color","#666666");
}

function searchScriptCommon(flag) {
	
	var data = "";
	data += "flag=2";
	data += "&keyword="+ $('#popup_searchScript').val().trim();
	data += "&action=" + $('#popup_scriptType option:selected').val();
	data += "&scriptType="+ $('#popup_contet_script_Type option:selected').val();
	
	searchScriptCommonGrid.clearAndLoad(contextPath+"/scriptCommonSearchGrid.xml?" + encodeURI(data));
	
}	


//상세보기창오픈시 이벤트
function openCommonScriptDetailPopup(selectRowNumPk) {
	
	console.log(selectRowNumPk);
	
	dataStr = {
			"selectRowNumPk" : selectRowNumPk
	}
	
	$.ajax({
		url : contextPath + "/getCommonScriptDetail.do",
		type : "GET",
		data : dataStr,
		dataType : "json",
		async : false,
		success : function(jRes) {
			var searchCommonScriptDetail = jRes.resData.searchCommonScriptDetail[0];
			console.log(searchCommonScriptDetail)
			
			//zinee
			
			var createDate = searchCommonScriptDetail.rsScriptCommonCreateDate.substring(0,19);
			var updateDate = "";
			var updateUser = "";
			var confirmUser = "";
			$('#common_pk').val(selectRowNumPk);
			$("#common_name").val(searchCommonScriptDetail.rsScriptCommonName);
			$('#common_desc').val(searchCommonScriptDetail.rsScriptCommonDesc);
			$('#common_text').val(searchCommonScriptDetail.rsScriptCommonText);
			//confirmUser check
			//TTS구분
			if(searchCommonScriptDetail.rsScriptCommonType == 'G') {
				$('#common_detail_type').val('G').prop('selected',true);
			} else if(searchCommonScriptDetail.rsScriptCommonType == 'T') {
				$('#common_detail_type').val('T').prop('selected',true);
			} else if(searchCommonScriptDetail.rsScriptCommonType == 'R') {
				$('#common_detail_type').val('R').prop('selected',true);
			} else if(searchCommonScriptDetail.rsScriptCommonType == 'S') {
				$('#common_detail_type').val('S').prop('selected',true);
			}
			//RealTime 구분
			$('#common_detail_realTime').attr('disabled',true);
			if(searchCommonScriptDetail.rsScriptCommonRealTimeTts == 'Y') {
				$('#common_detail_realTime').val('Y').prop('selected',true);
			} else if(searchCommonScriptDetail.rsScriptCommonRealTimeTts == 'N') {
				$('#common_detail_realTime').val('N').prop('selected',true);
			}
			
			/*
			if(searchCommonScriptDetail.rsScriptCommonConfirmUser == null) {
				confrimUser = "확인자 없음";
			} else {
				confrimUser = searchCommonScriptDetail.rsScriptCommonConfirmUser;
			}
			if(searchCommonScriptDetail.rsScriptCommonUpdateUser == null) {
				updateUser = "-";
			} else {
				updateUser = searchCommonScriptDetail.rsScriptCommonUpdateUser;
			}
			if(searchCommonScriptDetail.rsScriptCommonUpdateDate == null) {
				updateDate = "-";
			} else {
				updateDate = searchCommonScriptDetail.rsScriptCommonUpdateDate.substring(0,19);
			}
			if(searchCommonScriptDetail.rsScriptCommonUpdateUser == null) {
				updateUser = "-";
			}
			

			//그리드
			commonDetailGrid = new dhtmlXGridObject("commonDetailGrid");
			commonDetailGrid.setPagingSkin("toolbar", "dhx_web");
			commonDetailGrid.setImagePath(recseeResourcePath + "/images/project/");
			commonDetailGrid.enableColumnAutoSize(false);
			commonDetailGrid.setSkin("dhx_web");
			commonDetailGrid.enablePreRendering(50);
			commonDetailGrid.setHeader("작성자,작성일자,최종수정일자,최근수정관리자,확정여부,확인자,사용여부");
			commonDetailGrid.setInitWidths("200,200,200,200,200,200,*")
			commonDetailGrid.setColAlign("center,center,center,center,center,center,center");
			commonDetailGrid.setAwaitedRowHeight(100);
			commonDetailGrid.setEditable(false);
			
			const datas = {
					rows : [
						{id:1 , data: [
							searchCommonScriptDetail.rsScriptCommonCreateUser,
							createDate,
							updateDate,
							updateUser,
							searchCommonScriptDetail.rsScriptCommonConfirm,
							confrimUser,
							searchCommonScriptDetail.rsUseYn]}
						]
			}
			
			commonDetailGrid.init();
			commonDetailGrid.parse(datas,"json");*/
		}
	})

}

	function iframeControl() {
		var test = $('#playerFrame');
	}


//실시간 여부 확인하는 ajax function
function checkRealTimeTTS(rScriptStepFk,rProductType) {
	realTimeValueArray = [];
	var dataStr={
		"rScriptStepFk"	: rScriptStepFk,
		"rProductType"	: rProductType
	}
	$.ajax({
		url:contextPath+"/selectValueTTS.do",
		type:"POST",
		dataType:"json",
		data : dataStr,
		traditional:true,	
		async: false,
		success:function(jRes) {
			if(jRes.success == "Y") {	
				var result = jRes.resData.selectValueTTS;
				for(var i=0; i<result.length; i++) {
					realTimeValueArray.push(result[i].rsProductValueName);
				}
				console.log("realTimeValue 성공");
			} else {
				console.log("realTimeValue 실패");
			}
		}
	});
}
	//고개유형등 첫번쨰  SelectBox에 값을 채워주는 function
	function scriptSelectBox(ind,rScriptIfCase,rScriptIfCaseDetail,rScriptDetailIfCaseCode,rScriptDetailIfCaseDetail){
		var option ="" ;	
		
		//ifCase가 조건이 없을경우 조건을 선택해주세요 멘트를 옵션처리에 넣어주기
		if(rScriptIfCase== 'N'){
			option = "<option  value='' selected='selected'>"+"조건을 선택해주세요"+"</option>";
		} 
		for(var i=0; i <detailCase.length; i++){
			console.log("detailCase:"+detailCase)
			var selected = "";
			console.log("rScriptDetailIfCaseCode:"+rScriptDetailIfCaseCode);
			console.log("detailCase["+i+"].code:"+detailCase[i].code + " name:"+detailCase[i].name);
			if(rScriptIfCase == 'Y' && rScriptDetailIfCaseCode == detailCase[i].code){
				selected = " selected";		
				console.log(i+": Selected");	
			}
			option += "<option value='"+detailCase[i].code+"' "+selected+">"+detailCase[i].name+"</option>";
		}
		return option;
		
		$("#typeselect"+ind).on("change",function(option){
			console.log(""+"option:"+option);
			setScriptDetailCaseType()
		})
	};
	

//고객유형 클릭시 셀렉트 박스 show 함수
function selectChange(obj,ind) {
	var appendToName = $(obj).hasClass("newSelect");
	if(appendToName==false){
		$("#typeValue"+ind).empty();		
	}else{
		$("#typeValue_new"+ind).empty();		
	}
	var selectVal = $(obj).val();
	
	for(var j = 0; j<detailCase.length; j++){
		if(detailCase[j].values != null && detailCase[j].values.length > 0){
			if(selectVal == detailCase[j].code){
				$(obj).next().css("display","inline");
				$(obj).next().next().css("display","none");
				for(var i =0; i< detailCase[j].values.length; i++){
					var selected = "selected";
					var subitem = detailCase[j].values[i];
					option = "<option  value='"+subitem.code+"' "+selected+" >"+subitem.name+"</option>";
					console.log(option);						
					if(appendToName==false){				
						$("#typeValue"+ind).append(option);						
					}else{						
						$("#typeValue_new"+ind).append(option);						
					}
				}	
			return true;
			}			
		}else {
			$(obj).next().css("display","none");
			$(obj).next().next().css("display","inline");
		}
	}
};

// 첫번쨰 SelectBox에 선택된 값을 기준으로 rScriptDetailIfCaseDetail의 컬럼의 값을 표출하기 위한 현재상태 저장 function
function setScriptDetailCaseType(rScriptDetailIfCaseDetail,ind,rScriptDetailIfCaseDetailCode){
	console.log("rScriptDetailIfCaseDetail : "+ rScriptDetailIfCaseDetail);

	//선택한 값
	var check = $("#typeselect"+ind+" option:selected").val(); 
	console.log("check : "+ check);
	console.log("check text: "+ $("#typeselect"+ind+" option:selected").text());
//	var check = $(".type_select option:selected").val(); 
	for(var i=0; i <detailCase.length; i++){
		var selectedType = detailCase[i].code;			
		console.log("selectedType : "+ selectedType);
		if(check == selectedType){
			setScriptDetailCaseValue(detailCase[i],rScriptDetailIfCaseDetailCode,rScriptDetailIfCaseDetail,ind);
			console.log("check :"+check + " type :"+selectedType);
		}
	}
}

// 현재 선택된 첫번쨰 SelectBox의 데이터 내용에 따라 옆의 테그에 데이터 넣어주기
function setScriptDetailCaseValue(caseItem,rScriptDetailIfCaseDetailCode,rScriptDetailIfCaseDetail,ind){
	$("#typeValue"+ind).empty();
	$("#typeValue_new"+ind).empty();
	console.log("caseItemCode : "+ caseItem.code);
	console.log("caseItemName : "+ caseItem.name);
	console.log("caseItemVal : "+ caseItem.valuse);
	
	// 선택한 케이스의 value 를 가져와서 보여준다.
	if ( caseItem.values == null || caseItem.values.length == 0 ) {
//		option += "<input type='text' id='typeText"+ind+"' class='typeText' style='height : 20px; width:177px; margin-right:10px;' name='caseItem[i].code' value='rScriptDetailIfCaseDetail' />";
		$("#typeText"+ind).attr('value',rScriptDetailIfCaseDetail)
		$("#typeValue"+ind).hide();
		$("#typeText"+ind).show();
		console.log("caseItem.values is empty");
//		$("#typeText"+ind).append(option)
	} else {
		$("#typeValue"+ind).show();
		$("#typeText"+ind).hide();
		
		for(var i =0; i< caseItem.values.length; i++){
			var caseDetailCode = rScriptDetailIfCaseDetailCode == null ? "" : rScriptDetailIfCaseDetailCode;
			var selected = caseItem.values[i].code == caseDetailCode ? " selected" : "";	
			var subitem = caseItem.values[i];
			option = "<option  value='"+subitem.code+"' "+selected+" >"+subitem.name+"</option>";
			console.log(option);
			$("#typeValue"+ind).append(option)
//			$(option).appendTo(typeValue);	
		}			
		console.log("caseItem.values count:" + caseItem.values.length);
	} 
}


function ScriptChangeCaree(){
    
    $.ajax({
        url:contextPath+"/selectProductList.do",
        type:"POST",
        dataType:"json",
        async: false,
        success:function(jRes){
            if(jRes.success == "Y") {
                var result = jRes.resData.selectProductList;
                var scriptJsonArray = result;
                var htmlString = "";
                for(var i = 0; i < scriptJsonArray.length; i++) {
                    productArray.push(result[i].rProductCode)
                    productTypeArray.push(result[i].rProductType)
//                

                };
            
                }else{
                    alert("팝업 표출 실패")
                };
            }
        });            
    }; 

    
//    스크립트 디테일 가변값 불러오는 함수
    function scriptChangeValue(rProductType,rProductCode){
    	if(rProductType=="신탁"){
    		rProductType='1'
    	}else if(rProductType=="펀드"){
    		rProductType='2'
    	}
    	var dataStr={
    			
    			"rProductType"	: rProductType,
    			"rProductCode"  : rProductCode
    		}
    		$.ajax({
    			url:contextPath+"/selectProductChangeList.do",
    			type:"POST",
    			dataType:"json",
    			data : dataStr,
    			traditional:true,	
    			async: false,
    			success:function(jRes) {
    				if(jRes.success == "Y") {	
    					var result = jRes.resData.selectProductChangeList;
    					for(var i=0; i<result.length; i++) {
    						scriptChangeNameArray.push(result[i].rProductValueName);
    						scriptChangeCodeArray.push(result[i].rProductValueCode);
    					}
    					console.log("scriptChangeValue 성공");
    				} else {
    					console.log("scriptChangeValue 실패");
    				}
    			}
    		});
    }
//가변값으로 변경해주기
    function textChangeValue(text){
    	changeText =text;
    	
    	console.log("text:"+text);
    	if(scriptChangeCodeArray.length > 0){
    		for(var i= 0; i<scriptChangeCodeArray.length; i++){
    			changeText = changeText.replace(scriptChangeCodeArray[i],scriptChangeNameArray[i]);    				
    		}	
    	}else {
    		changeText = text;
		}
    	
    	return changeText
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
		}
		if(delYn == "Y") {
			//공용문구 삭제버튼
			$("#commonRemoveBtn").css("display","block");
			//제거
			$(".delete_contentBtn").css("display","block");
		}
    }
//    testtesttest
 function serachingVariableText(text,caseDetail){
	var searchArr = [];
	
	if(!isNaN(caseDetail)){ //caseDetail 이 숫자면 그 값만 비교
    	for (var i = 0; i < scriptVariableObject.length; i++) {
    		var name = scriptVariableObject[i].rProductValueName;
    		var code = scriptVariableObject[i].rProductCode;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
    		if(code == caseDetail){
    			if(text.indexOf("{"+name+"}") > -1){
    				searchArr.push(scriptVariableObject[i]);
    			}	    			
    		}
    		
		}
	}else{	//아닐시 일반비교    	
    	for (var i = 0; i < scriptVariableObject.length; i++) {
    		var name = scriptVariableObject[i].rProductValueName;
    		var code = scriptVariableObject[i].rProductCode;
    		if(code == productCode){
	    		if(text.indexOf("{"+name+"}") > -1){
	    			searchArr.push(scriptVariableObject[i]);
	    		}
    		}
    	}
	}
	
	return searchArr;
}


function changeVariableValToName(text,caseDetail){
	var searchArr = serachingVariableText(text,caseDetail);
//	replace(/{/gi,"<span style='color:red;'>{").replace(/}/gi,"}</span>"); 
	for (var i = 0; i < searchArr.length; i++) {
		var name = searchArr[i].rProductValueName;
		var value = searchArr[i].rProductValueVal;
		text = text.replace("{"+name+"}" , value );
	}
	return text;
}

//텍스트 부분에 가변값 항목이 아닌 {}가 있으면 replace로 변환하는 함수
function bracketRemove (text){
	if(ifdetail=='N'||ifdetail==null){
		//앞에 괄화가 있을떄
		if(text.indexOf("{")>-1){
			text.repalce(/{/gi,"")
		}	
		//뒤에 괄호가 있을때
		if(text.indexOf("}")>-1){
			text.replace(/}/gi,"")				
		}
			return text;		
	}else {
		return false;
	}
	
}


//TTS 리딩, 직원리딩, 상담 가이드, 적합성 보고서 클릭 시 생성되는 DIV의 첫 select박스 내역을 채워주는 함수
function addScriptSelectBox(){
	var option ="" ;
	option = "<option  value='' selected='selected'>"+"조건을 선택해주세요"+"</option>";
	for(var i=0; i <detailCase.length; i++){
		var selected = "";
		option += "<option value='"+detailCase[i].code+"' "+selected+">"+detailCase[i].name+"</option>";
	}
			
//	alert(option);
	return option;
	
	$("#typeselect_new"+ind).on("change",function(option){
		console.log(""+"option:"+option);
		setScriptDetailCaseType()
	})
	
};
    
    
    
 