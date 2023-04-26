//로그인요청 파라미터
var taResultStepArr = new Array();

var taParam = {
		"rdg_id" : "" , 		//콜아이디
		"para_id" : "", 		//단락아이디
		"rec_id" : "" , 		//녹취아이디
		"call_type" : "",		//통화유형
		"call_cmpl_yn" : "", 	//통화 종료 여부
		"prod_cd" : "",			//상품코드
		"prod_type" : "",		//상품 타입
		"script_cd" : "",		//스크립트 코드
		"script_dtl_cd" : "",	//스크립트 하위 상세코드
		"prod_nm" : "" ,		//상품명
		"cslr_no" : "",			//상담원아이디
		"cslr_nm" : "",			//상담원 이름
		"cus_id" : "", 			//고객아이디
		"cus_nm" : "",			//고객명
		"cus_type" : "",		//고객유형
		"cus_inv_type" : "",	//고객 투자 성향
		"cus_deputy_yn" : "",	//대리인여부
		"cus_senior_yn" : "",	//65세 이상 여부
		"rec_yn" : "",			//녹취대상여부
		"rec_manual_yn" : ""	//수동녹취 여부
		
};


//로그인 요청
var loginParam = {
							"header":"RSAI",
							"code" 	:"login",
							"callid":"" 
						}
//로그인 결과 				
var loginResultParam = {
							"header":"RSAI",
							"code" 	:"login",
							"callid":"",
							"result" : "success",
							"reason" : ""
						}
// STT & TA 녹취시작 응답 결과			
var recStartResParam = {
							"header":"RSAI",
							"code" 	:"recstart",
							"callid":"",
							"result" : "success",
							"reason" : ""
						}
						
// TA 요청
var taReq = {
		"header":"RSAI",
		"code" : "tareq",
		"callid" : "",
		"taparam" : ""
};
	
// TA 요청에 대한 응답
var taRes = {
				"header":"RSAI",
				"code" : "tareq",
				"callid" : "",
				"result" : "success",
				"reason" : ""
			}
// TA 결과
var taResult = {
				"header":"RSTA",
				"code" : "sttrst",
				"callid" : "164775375893000",
				"result" : "Y",
				"reason" : ""
			}

// merge 전 최종녹취 요청
var finalRecParam = {
		"header" : "RSAI",
		"code" : "finalrec",
		"callid" : "",
		"taparam" : ""
	}

var finalRecTaParam = {
		
	}

var recEndParam = {
		"header":"RSAI",
		"code" 	:"recstop",
		"callid":"",
		"result" : "success",
		"reason" : ""
	}

var sttInfoParam = {
		"header" : "RSSTT",
		"code" : "sttrst",
		"callid" : "",
		"rxtx" : "",
		"result" : ""
	}
var logoutResParam = {
		"header" : "RSAI",
		"code" : "logout",
		"callid" : "",
		"result" : "success",
		"reason" : ""
	}

	/*
var test ={
				"test" : "1",
				"data" : JSON.stringify(taResult)
			}
	*/