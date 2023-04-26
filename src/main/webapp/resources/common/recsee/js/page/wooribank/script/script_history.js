window.onload = function(){
	$("#tableBox").empty();
	$("#dataBox").empty();
	
	/* 변경이력 조회 API */
	var info = {
		productPk: productListPk,
		dummyString: dummyString
	};
	var callbackSuccess = function(data){
		var history = data.resData.history;
		if(history == undefined || history == null || history == [] || history.length==0){
			alert("조회할 이력이 없습니다.");
			window.close();
		}else{
			setTable(history);
			$(".historyTd")[0].onclick();
		}
	};
	var callbackError = function(data){
		if(data.result=='-1001'){
			opener.fromHistoryToLogin();
		}else{
			alert("이력 조회에 실패했습니다. 관리자에게 문의하세요.");
			window.close();
		}
	};
	getProductHistory(info, callbackSuccess, callbackError);
}

function setTable(history){
	var table = '<table id="historyTable">'
					+ '<tr>'
						+ '<th>변경이력</th>'
					+ '</tr>';
	for(var i=history.length-1; i>-1; i--){
		var displayIndex = i+1;
		table += '<tr>'
					+ '<td id="history'+i+'" index="'+i+'" onclick="showData(this); colorRow(this);" class="historyTd">'
						+ '<div>'
							+ '<span id="indexSpan'+i+'">' + displayIndex + '. ' + '</span>'
							+ '<span id="updateDate'+i+'">' + history[i].rsUpdateDate + '</span>'
						+ '</div>'
						+ '<input type="hidden" id="history'+ history[i].rsScriptVersion +'" value="'+ history[i].rsScriptVersion +'">'
					+'</td>'
				+ '</tr>';
	}
	table += '</table>';
	$("#tableBox").append(table);
}

function showData(obj){
	/* 변경상세 조회 API */
	var index = $(obj).attr('index');
	var displayIndex = $('#indexSpan'+index).html();
	var version = $(obj).find('input').val();
	var date = $('#updateDate'+index).html();
	var info = {
		version: version,
		productPk: productListPk 
	};
	
	var callbackSuccess = function(data){
		var snapshot = data.resData.scriptSnapshot;
		if(snapshot == undefined || snapshot == null){
			alert("조회할 정보가 없습니다.");
		}else{
			var divData = {
				displayIndex: displayIndex,
				date: date,
				version: version,
				editUser: snapshot.rsUpdateUser,
				approveUser: snapshot.rsApproveUser,
				stepList: snapshot.scriptInfo.stepList
			};
			document.title = "[" + snapshot.scriptInfo.rsProductName + "] 변경이력"
			$("#dataBox").empty();
			showDiv(divData);
		}
	};
	var callbackError = function(data){
		if(data.result=='-1001'){
			opener.fromHistoryToLogin();
		}else{
			alert("이력 조회에 실패했습니다. 관리자에게 문의하세요.");
			window.close();
		}
	};
	
	getHistoryDetail(info, callbackSuccess, callbackError);
}

function colorRow(obj){
	var index = $(obj).attr("index");
	$("#historyTable").find('td').attr("style", "background-color:#fff");
	$("#history"+index).attr("style", "background-color:#bfe6ff");
}

function showDiv(divData){
	
	var div = '<div id="history">';
	
	/* 엑셀&PDF 다운로드 */	
	var excelImg = recseeResourcePath + "/images/project/icon/wooribank/iconXLS.gif";
	var pdfImg = recseeResourcePath + "/images/project/icon/wooribank/iconPDF.gif";
	div += '<div id="downloadArea">'
			+ '<div id="excelDownload" onclick="downloadExcel('+ productListPk + ',' + divData.version +')" class="scriptDownload">'
				+ '<img class="downloadImage" src="'+ excelImg +'"/>' + '다운로드'
			+ '</div>'
			+ '<div id="pdfDownload" onclick="downloadPdf('+ productListPk + ',' + divData.version +')" class="scriptDownload">'
				+ '<img class="downloadImage" src="'+ pdfImg +'"/>' + '다운로드'
			+ '</div>'
		+ '</div>';

	div += '<div id="titleInfo">'
			+ '<div id="historyDate">' + divData.displayIndex + divData.date + '</div>'
			+ '<div id="userInfo">'
				+ '<span class="userInfoTitle">수정자</span><span class="userInfo">' + divData.editUser + '</span>'
				+ '<span class="userInfoTitle">확인자</span><span class="userInfo">' + divData.approveUser + '</span>'
			+ '</div>'
		+ '</div>';
				
	for(var i=0; i< divData.stepList.length; i++) {
		var step = divData.stepList[i];
		var stepParent = divData.stepList[i].rsScriptStepParent;
		if( i!= divData.stepList.length-1 ){
			if( stepParent == "0" && divData.stepList[i+1].rsScriptStepParent != "0" ){ // 하위스텝이 있는 상위스텝
				div += '<div class="stepNameWrap"style="margin-bottom : 3px !important;">'
					+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" class="stepNameArr"/>'
					+ '<div class="stepName">'+ step.rsScriptStepName + '</div>';
			}else if(stepParent !="0" && divData.stepList[i+1].rsScriptStepParent != "0" ){
				div += '<div class="stepNameWrap"style="margin-bottom : 3px !important;">'
					+ '<div class="stepName" style="margin-left:20px">' + " - " + step.rsScriptStepName + '</div>';
			}else if(stepParent !="0" && divData.stepList[i-1].rsScriptStepParent != "0" ){
				div += '<div class="stepNameWrap">'
					+ '<div class="stepName" style="margin-left:20px">' + " - " + step.rsScriptStepName + '</div>';
			}else{
				div += '<div class="stepNameWrap">'
					+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" class="stepNameArr"/>'
					+ '<div class="stepName">'+ step.rsScriptStepName + '</div>';
			}
		}else{
			div += '<div class="stepNameWrap">'
				+ '<img src="'+recseeResourcePath+'/images/project/icon/wooribank/select_arr.svg" class="stepNameArr"/>'
				+ '<div class="stepName">'+ step.rsScriptStepName + '</div>';
		}
			for(var j = 0; j<step.detailList.length; j++) {
				
				var type = step.detailList[j].rsScriptStepDetailTypeName;
				var text = step.detailList[j].rsScriptDetailText;
				var detailConditionName = step.detailList[j].rsScriptDetailCaseValue;
				var detailConditionValue = step.detailList[j].rsScriptDetailCaseDetailValue;
				var eltDetailContion = step.detailList[j].rsProductAttributesText;
				
				if(type == null) {
					type="";
					detailConditionName="";
					detailConditionValue="";
					eltDetailContion="";
				}else {
					//기본 조건
					if(detailConditionName == null){
						detailConditionValue="";
						detailConditionName="";
					}else {
						detailConditionName = step.detailList[j].rsScriptDetailCaseValue +'&nbsp'+':'+'&nbsp';
					}
					//elt조건
					if(eltDetailContion == null){
						eltDetailContion ="";
					}else{
						eltDetailContion =step.detailList[j].rsProductAttributesText;
					}	
				}
				
				
				if(text == null){
					div	+= '<div class="stepDetailWrap" style="background-color: #f9f9f9">'
							+ '<div class="stepDetailType"></div>'
							+ '<div class="stepDetailText" style="color: #d3d3d3; background-color: #f9f9f9 !important">등록된 스크립트가 없습니다.</div>';
				}else{
					div += '<div class="stepDetailWrap ">'
							+ '<div class="stepDetailType detailType">' + type + '</div>'
							+ '<div class="stepDetailText"><pre>' + text + '</pre></div>'
					if(detailConditionName != "" && eltDetailContion == ""){
		
							div += beforeDetailCondition(detailConditionName,detailConditionValue);
						
						
					}else if(eltDetailContion !="" && detailConditionName==""){
							div +=		 '<div id="detailCondition" class="detailCondition detailType">'
							div +=		 	 beforeDetailEltCondition(eltDetailContion);
							div +=	 	 '</div>';
					}else if(detailConditionName == "" || eltDetailContion == "" ){
							div +=		 '<div id="detailCondition" class="detailCondition detailType" style="display:none !important;"></div>'
					}
				}
					div += '</div>';
			}
		div += '</div>';
	}
	
	div	+= '</div>';
	
	$("#dataBox").append(div);
	
	
	
}

function resetPage(){
	$("#tableBox").remove();
	$("#dataBox").remove();
}

// 버튼 클릭 시 엑셀 저장 
function downloadExcel(productPk, version){
	window.open(contextPath + "/wooribank/script/download/product/" + productPk + "/script/" + version + "?fileType=excel");
}

// 버튼 클릭 시 PDF 저장 
function downloadPdf(productPk, version){
	window.open(contextPath + "/wooribank/script/download/product/" + productPk + "/script/" + version + "?fileType=pdf");
}


// 변경 전 상품기본조건
function beforeDetailCondition(detailConNm,detailConVal){
	var html='';
	html += '';
	
	if(detailConNm !=""){
		html += '<div id="detailCondition" class="detailCondition detailType">'
					+ '<div id="detailConditionName" class="detailTypeCon ">'+ detailConNm + detailConVal +'</div>'
				+'</div>';
	}
	
	return html;
}
// 변경 전 elt상품일 경우
function beforeDetailEltCondition(eltCon){
	var html='';
	html += '';
	
	for(var i =0; i < eltCon.length; i++){
		html +=  '<div id="detailConditionName'+[i]+'" class="detailTypeCon ">'+ eltCon[i]+'&nbsp' +'</div>'
	}
	return html;
}