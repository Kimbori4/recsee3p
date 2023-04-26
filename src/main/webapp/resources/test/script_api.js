/************* 공통 함수 **************/	

// ajax get (without data)
function ajaxGet(url, callback){
	$.ajax({
		type: 'GET',
		url: url,
		success: function(data){
			data.success == "Y"? callback(data) : showError(data);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax post (with data)
function ajaxPost(url, reqData, contentType, callback){
	$.ajax({
		type: 'POST',
		url: url,
		contentType: contentType,
		data : contentType=="application/json"? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callback(data) : showError(data);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax put (with data)
function ajaxPut(url, reqData, contentType, callback){
	$.ajax({
		type: 'PUT',
		url: url,
		contentType: contentType,
		data : contentType=="application/json"? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callback(data) : showError(data);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax put (without data)
function ajaxPutNoData(url, callback){
	$.ajax({
		type: 'PUT',
		url: url,
		success: function(data){
			data.success == "Y"? callback(data) : showError(data);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax delete (without data)
function ajaxDelete(url, callback){
	$.ajax({
		type: 'DELETE',
		url: url,
		success: function(data){
			data.success == "Y"? callback(data) : showError(data);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax delete (with data)
function ajaxDeleteData(url, reqData, contentType, callback){
	$.ajax({
		type: 'DELETE',
		url: url,
		contentType: contentType,
		data: contentType=="application/json"? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callback(data) : showError(data);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// 서버 연동 실패
function onFail(request, status, error){
	console.log("code:"+request.status);
	console.log("message"+request.responseText);
	console.log("error:"+error);
	alert("에러코드 9999: 알 수 없는 에러가 발생하였습니다. 관리자에게 문의하세요.");
}
	
// 서버 연동 성공 but 작업 실패
function showError(data){
	switch(data.resData.code){
		// 1001 - 세션 없음. 로그인 필요
		case 1001:
			alert("에러코드 1001: 로그인이 필요합니다.");
			location.replace(contextPath + "/");
			break;
		// 1002 - 권한 없음
		case 1002:
			alert("에러코드 1002: 권한이 없습니다.");
			break;
		// 2001 - 필수 파라미터 누락
		case 2001:
			alert("에러코드 2001: 필수항목이 누락되었습니다.");
			break;
		// 2002 - 파라미터 유효성 검사 실패
		case 2002:
			alert("에러코드 2002: 입력된 항목이 유효하지 않습니다.");
			break;
	}
}

/***************** 공통 스크립트 관리 ******************/

// 공통 스크립트 추가
function addCommon( type, name, desc, text, realtimeTTS , callback ) {
	
	var url = contextPath + '/script/common';
	var reqData = {
		rsScriptCommonType: type,
		rsScriptCommonName: name,
		rsScriptCommonDesc: desc,
		rsScriptCommonText: text,
		rsScriptCommonRealtimeTTS: realtimeTTS
	};
	
	ajaxPost(url, reqData, 'application/json', callback);

}

// 공통 스크립트 수정
function editCommon( pk, type, name, desc, text, realtimeTTS , callback ) {

	var url = contextPath + '/script/common/' + pk;
	var reqData = {
		rsScriptCommonType: type,
		rsScriptCommonName: name,
		rsScriptCommonDesc: desc,
		rsScriptCommonText: text,
		rsScriptCommonRealtimeTTS: realtimeTTS
	};
	
	ajaxPut(url, reqData, 'application/json', callback);
	
}

// 공통 스크립트 삭제
function deleteCommon( pk, callback ) {
	
	var url = contextPath + '/script/common/' + pk;
	ajaxDelete(url, callback);
	
}

/**************************** 스크립트 스텝 디테일 관리 **************************/

// 스크립트 스텝 디테일 조회
function getStepDetail(stepPk, stepFk, productCode, callback){
	
	var url = contextPath + "/script/step/" + stepPk + "/detail" + "?"
	+ "rsScriptStepFk=" + stepFk + "&"
	+ "rProductCode=" + productCode;
	
	ajaxGet(url, callback);

}

// 스크립트 스텝 디테일 편집 - 추가
function addStepDetail(stepPk, transId, detailType, comKind, stepFk, detailText, ifCase, ifCaseCode, ifCaseDetail, ifCaseDetailCode, realtimeTTS, comFk, callback){
	
	var url = contextPath + "/script/step/" + stepPk + "/detail";
	var reqData = {
			transactionId: transId,
			rsScriptDetailType: detailType,
			rsScriptDetailComKind: comKind,
			rsScriptStepFk: stepFk,
			rsScriptDetailText: detailText,
			rsScriptDetailIfCase: ifCase,
			rsScriptDetailIfCaseCode: ifCaseCode,
			rsScriptDetailIfCaseDetail: ifCaseDetail,
			rsScriptDetailIfCaseDetailCode: ifCaseDetailCode,
			rsScriptDetailRealtimeTTS: realtimeTTS,
			rsScriptDetailComFk: comFk
		};
	
	ajaxPost(url, reqData, 'application/json', callback);
}

// 스크립트 스텝 디테일 편집 - 수정
function editStepDetail(stepPk, stepDetailPk, transId, detailText, ifCase, ifCaseCode, ifCaseDetail, ifCaseDetailCode, realtimeTTS, callback){
	
	var url = contextPath + "/script/step/" + stepPk + "/detail/" + stepDetailPk;
	var reqData = {
			transactionId: transId,
			rsScriptDetailText: detailText,
			rsScriptDetailIfCase: ifCase,
			rsScriptDetailIfCaseCode: ifCaseCode,
			rsScriptDetailIfCaseDetail: ifCaseDetail,
			rsScriptDetailIfCaseDetailCode: ifCaseDetailCode,
			rsScriptDetailRealtimeTTS: realtimeTTS
		};
	
	ajaxPut(url, reqData, 'application/json', callback);
}

// 스크립트 스텝 디테일 편집 - 삭제
function deleteStepDetail(stepPk, stepDetailPk, transId, callback){
	
	var url = contextPath + "/script/step/" + stepPk + "/detail/" + stepDetailPk;
	var reqData = {
			transactionId: transId
		};
	
	ajaxDeleteData(url, reqData, 'application/json', callback);
	
}

// 스크립트 스텝 디테일 편집 - 완료
function finishStepDetail(stepPk, stepDetailPk, snapshotId, callback){
	
	var url = contextPath + "/script/step/" + stepPk + "/detail/" + stepDetailPk;
	var reqData = {
			snapshotId: snapshotId
		};
	
	ajaxPut(url, reqData, 'application/json', callback);
}

/********************************** 스크립트 스텝 관리 ****************************/
function addStep(transId, stepFk, stepParent, stepType, stepName, callback){
	
	var url = contextPath + "/script/step";
	var reqData = {
			transactionId: transId,
			rsScriptStepFk: stepFk,
			rsScriptStepParent: stepParent,
			rsScriptStepType: stepType,
			rsScriptStepName: stepName
		};
	
	ajaxPost(url, reqData, 'application/json', callback);
}

function editStep(stepPk, transId, stepName, callback){
	
	var url = contextPath + "/script/step/" + stepPk;
	var reqData = {
			transactionId: transId,
			rsScriptStepName: stepName
		};
	
	ajaxPut(url, reqData, 'application/json', callback);
}

function deleteStep(stepPk, transId, callback){
	
	var url = contextPath + "/script/step/" + stepPk;
	var reqData = {
			transactionId: transId
		};
	
	ajaxDeleteData(url, reqData, 'application/json', callback);
}

/********************** 스크립트 편집 트랜잭션 관리 *******************/
function startTransation(stepPk , callback){
	
	var url = contextPath + "/script/" + stepPk + "/edit/begin";
	ajaxPutNoData(url, callback);
	
}

function endTransaction(stepPk , callback){
	
	var url = contextPath + "/script/" + stepPk + "/edit/end";
	ajaxPutNoData(url, callback);
	
}

/************************ 상품 정보 관리 ***************************/

function getProductInfo(productPk, callback){
	
	var url = contextPath + "/script/product/" + productPk;
	ajaxGet(url, callback);
	
}
