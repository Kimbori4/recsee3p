/************* 공통 함수 **************/	

// base url
var baseUrl = contextPath + "/wooribank/script/api";

// ajax get (without data)
function ajaxGet(url, callbackSuccess, callbackError){
	$.ajax({
		type: 'GET',
		url: url,
		success: function(data){
			data.success == "Y"? callbackSuccess(data) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}


// ajax post (with data and index)
function ajaxPostWithInd(url, reqData, ind, contentType, callbackSuccess, callbackError){
	$.ajax({
		type: 'POST',
		url: url,
		contentType: contentType,
		data : contentType=="application/json"? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callbackSuccess(data, ind) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax post (with data)
function ajaxPost(url, reqData, contentType, callbackSuccess, callbackError){
	$.ajax({
		type: 'POST',
		url: url,
		contentType: contentType,
		data : contentType=="application/json"? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callbackSuccess(data) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax post (without data)
function ajaxPostNoData(url, callbackSuccess, callbackError){
	$.ajax({
		type: 'POST',
		url: url,
		success: function(data){
			data.success == "Y"? callbackSuccess(data) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax put (with data)
function ajaxPut(url, reqData, contentType, callbackSuccess, callbackError){
	$.ajax({
		type: 'PUT',
		url: url,
		contentType: contentType,
		data : contentType=="application/json"? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callbackSuccess(data) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax put (without data)
function ajaxPutNoData(url, callbackSuccess, callbackError){
	$.ajax({
		type: 'PUT',
		url: url,
		success: function(data){
			data.success == "Y"? callbackSuccess(data) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// ajax delete (with data)
function ajaxDeleteData(url, reqData, contentType, callbackSuccess, callbackError){
	$.ajax({
		type: 'DELETE',
		url: url,
		contentType: contentType,
		data: (contentType=="application/json") ? JSON.stringify(reqData) : reqData,
		success: function(data){
			data.success == "Y"? callbackSuccess(data) : showError(data, callbackError);
		}, error: function(request, status, error){
			onFail(request, status, error);
		}
	});	
}

// 서버 연동 실패
function onFail(request, status, error){
	console.log("code:"+request.status);
	console.log("message:"+request.responseText);
	console.log("error:"+error);
	progress.off();
}
	
// 서버 연동 성공 but 작업 실패
function showError(data, callbackError){
	
	alert(getErrorMessage(data.result));
	
	var url = getLocationUrl(data.result);
	
	if( null != url && !window.opener ) { // url이 null이 아님 and 현재창의 오프너가 존재하지 않을 때만
		location.replace(contextPath + url); // 현재창을 로그인화면으로 이동
		return;
	}
	callbackError(data);
}

// showError 실행 시 사용하는 컴포넌트
var resultMessageMap = {
		// User
		"-1001":{
					message:"로그인이 필요합니다." ,
					url: "/"
				},
		"-1002":{
					messgae:"권한이 없습니다."
				},
		// Parameter
		"-2001":{
					message:"필수항목이 누락되었습니다."
				},
		"-2002":{
					message:"입력된 항목이 유효하지 않습니다."
				},
		// Transaction
		"-3001":{
					message:"스크립트 변경 결재중인건이 있습니다.\n결재목록에서 상태를 확인하시기 바랍니다. "
				},
		"-3002":{
					message:"이미 완료요청되었습니다."
				},
		"-3003":{
					message:"요청이 존재하지 않습니다."
				},
		"-3004":{
					message:"스크립트를 반영할 수 없습니다."
				},
		"-3005":{
					message:"저장할 내용이 없습니다."
				},
		"-3006":{
					message:"상품스크립트에 사용중인 공용문구입니다."
				},
		"-3007":{
					message:"현재 스크립트를 수정할 수 없습니다."
				}
}

function getErrorMessage(code){
	
	var item = resultMessageMap[code];
	var message = '일시적인 에러가 발생했습니다.'
	if (item != null){
		message = item.message;
	}
	
	return message;
}

function getLocationUrl(code) {
	var item = resultMessageMap[code];
	if (item == null){ return null; }
	
	return item.url;
}

/***************** 공통 스크립트 관리 ******************/

// 공통 스크립트 추가
function addCommon(info, callbackSuccess , callbackError) {
	
	var url = baseUrl + '/common';
	var reqData = {
		rsScriptCommonType: info.type,
		rsScriptCommonName: info.name,
		rsScriptCommonDesc: info.desc,
		rsScriptCommonText: info.text,
		rsScriptCommonRealtimeTTS: info.realtimeTTS,
		rsScriptCommonApplyDate: info.applyDate,
		rsScriptCommonApplyType: info.applyType
	};
	
	ajaxPost(url, reqData, 'application/json' , callbackSuccess , callbackError);

}

// 공통 스크립트 수정
function editCommon(info, callbackSuccess , callbackError) {

	var url = baseUrl + '/common/' + info.pk;
	var reqData = {
		rsScriptCommonType: info.type,
		rsScriptCommonName: info.name,
		rsScriptCommonDesc: info.desc,
		rsScriptCommonText: info.text,
		rsScriptCommonRealtimeTTS: info.realtimeTTS,
		rsScriptCommonApplyDate: info.applyDate,
		rsScriptCommonApplyType: info.applyType
	};
	
	ajaxPut(url, reqData, 'application/json' , callbackSuccess , callbackError);
	
}

// 공통 스크립트 삭제
function deleteCommon( info , callbackSuccess , callbackError) {
	
	var url = baseUrl + '/common/' + info.pk;
	var reqData = {
		rsScriptCommonApplyDate: info.applyDate,
		rsScriptCommonApplyType: info.applyType
	};
	ajaxDeleteData(url , reqData, 'application/json', callbackSuccess , callbackError);
	
}

// 수정 또는 삭제 시, 이전 요청이 있는지 체크
function checkCommon( pk , callbackSuccess , callbackError ) {
	
	var url = baseUrl + '/common/' + pk + '/status';
	ajaxGet(url, callbackSuccess, callbackError);
} 

/**************************** 스크립트 스텝 디테일 관리 **************************/

// (조회 화면) 스크립트 스텝 디테일 조회
function getStepDetail(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk + "/detail?"
	+ "useYn=Y" + "&"
	+ "productPk=" + info.productPk + "&"
	+ "productCode=" + info.productCode;
	
	ajaxGet(url , callbackSuccess , callbackError);
}

// (수정 화면=저장 전 임시값) 스크립트 스텝 디테일 조회
function getTempStepDetail(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk + "/detail/preview?"
	+ "useYn=Y" + "&"
	+ "productCode=" + info.productCode + "&"
	+ "transactionId=" + info.transId;
	
	ajaxGet(url , callbackSuccess , callbackError);
}

// 스크립트 스텝 디테일 편집 - 추가
function addStepDetail(info, ind, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk + "/detail";
	var reqData = {
			transactionId: info.transId,
			rsScriptDetailType: info.detailType,
			rsScriptDetailComKind: info.comKind,
			rsScriptStepFk: info.stepFk,
			rsScriptDetailText: info.detailText,
			rsScriptDetailIfCase: info.ifCase,
			rsScriptDetailIfCaseCode: info.ifCaseCode,
			rsScriptDetailIfCaseDetail: info.ifCaseDetail,
			rsScriptDetailIfCaseDetailCode: info.ifCaseDetailCode,
			rsScriptDetailRealtimeTTS: info.realtimeTTS,
			rsScriptDetailComFk: info.comFk,
		};
	
	ajaxPostWithInd(url, reqData, ind, 'application/json' , callbackSuccess , callbackError);
}

// 스크립트 스텝 디테일 편집 - 수정
function editStepDetail(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk + "/detail/" + info.stepDetailPk;
	var reqData = {
			transactionId: info.transId,
			rsScriptDetailText: info.detailText,
			rsScriptDetailComKind: info.comKind,
			rsScriptDetailIfCase: info.ifCase,
			rsScriptDetailIfCaseCode: info.ifCaseCode,
			rsScriptDetailIfCaseDetail: info.ifCaseDetail,
			rsScriptDetailIfCaseDetailCode: info.ifCaseDetailCode,
			rsScriptDetailRealtimeTTS: info.realtimeTTS,
			rsScriptDetailType: info.type,
			rsScriptDetailComFk: info.comFk
		};
	
	ajaxPut(url, reqData, 'application/json' , callbackSuccess , callbackError);
}

// 스크립트 스텝 디테일 편집 - 삭제
function deleteStepDetail(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk + "/detail/" + info.stepDetailPk;
	var reqData = {
			transactionId: info.transId
		};
	
	ajaxDeleteData(url, reqData, 'application/json' , callbackSuccess , callbackError);
	
}

/********************************** 스크립트 스텝 관리 ****************************/

// 신규
function addStep(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step";
	var reqData = {
			transactionId: info.transId,
			rsScriptStepFk: info.stepFk,
			rsScriptStepParent: info.stepParent,
			rsScriptStepName: info.stepName
		};
	
	ajaxPost(url, reqData, 'application/json' , callbackSuccess , callbackError);
}

// 수정
function editStep(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk;
	var reqData = {
			transactionId: info.transId,
			rsScriptStepName: info.stepName
		};
	
	ajaxPut(url, reqData, 'application/json' , callbackSuccess , callbackError);
}


// 삭제
function deleteStep(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/step/" + info.stepPk;
	var reqData = {
			transactionId: info.transId
		};
	
	ajaxDeleteData(url, reqData, 'application/json' , callbackSuccess , callbackError);
}

/********************** 스크립트 편집 트랜잭션 관리 *******************/

// 트랜잭션 시작
function startTransaction(rsProductListPk , oldTransactionId , callbackSuccess , callbackError){
	
	var url = baseUrl + "/edit/" + rsProductListPk + "/begin?oldTransactionId=" + oldTransactionId;
	ajaxPutNoData(url , callbackSuccess , callbackError);
	
}

// 트랜잭션 종료 : commit, rollback
function endTransaction(info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/edit/"+ info.rsProductListPk +"/end" ;
	var reqData = {
			transactionId: info.transId,
			endType: info.endType,
			applyDate: info.applyDate, // yyyy-MM-dd
			applyType: info.applyType // immediately or reserved
		};
	
	ajaxPut(url, reqData, 'application/json' , callbackSuccess , callbackError);
	
}

/************************ 상품 정보 관리 ***************************/

// 상품정보 조회
function getProductInfo(productPk , callbackSuccess , callbackError){
	
	var url = baseUrl + "/product/" + productPk;
	ajaxGet(url , callbackSuccess , callbackError);
	
}

/************************ 결재 관리 ***************************/

// 결재요청 상세 조회
function getApprovalDetail(info, callbackSuccess, callbackError){
	
	var url = baseUrl + "/approval/" + info.detailTransactionId + "?"
	+ "scriptType=" + info.scriptType;
	
	ajaxGet(url , callbackSuccess , callbackError);
	
}

// 결재 : 승인, 반려, 승인취소, 상신취소
function putApproval(info, callbackSuccess, callbackError){
	var url = baseUrl + "/approval/" + info.detailTransactionId;
	var reqData = {
			rsApproveStatus: info.approveType, // approve: 승인, reject: 반려+승인취소, cancel: 상신취소
			rsScriptType: info.scriptType, // A: 전체, P: 상품스크립트, C: 공통스크립트
			rsApplyDate: info.approveDate, // 즉시적용 시 ''
			rsApplyType: info.applyType // dayafter:익일적용 immediately:즉시적용 reserved:예약일적용 
		};
	
	ajaxPut(url, reqData, 'application/json' , callbackSuccess, callbackError);
}

/********************* 상품스크립트 변경이력 조회 ***************/

// 변경이력 유무 확인
function checkHistory(rsProductPk, callbackSuccess, callbackError){
	
	var url = baseUrl + "/product/" + rsProductPk + "/history/check";
	ajaxGet(url, callbackSuccess, callbackError);
	
}

// 변경이력 조회
function getProductHistory(info, callbackSuccess, callbackError){
	
	var url = baseUrl + "/product/" + info.productPk + "/history?v=" + info.dummyString;
	ajaxGet(url , callbackSuccess , callbackError);

}

// 변경상세 조회
function getHistoryDetail(info, callbackSuccess, callbackError){
	
	var url = baseUrl + "/product/" + info.productPk + "/history/" + info.version;
	ajaxGet(url , callbackSuccess , callbackError);
	
}

/********************* 스크립트 미등록 상품 복사 ***************/

// 스크립트 미등록 상품 복사
function putNotRegisteredScriptCopy(sourceProductPk, info, callbackSuccess , callbackError){
	
	var url = baseUrl + "/edit/copy/" + sourceProductPk;
	var reqData = {
			targetList: info.targetList,
			applyDate: info.applyDate,
			applyType: info.applyType
		};
	
	ajaxPut(url, reqData, 'application/json' , callbackSuccess , callbackError);
}
