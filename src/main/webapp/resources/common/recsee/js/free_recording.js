var timerFlag; //1sec 씩 count flag
var recTime =0; //초기 시간 설정값 0
//const recTimer = document.getElementById("recTimer");
var hour =0; //초기 시간 값 
var min = 0; //초기 분 값
var sec = 0; //초기 초 값
var recTimer; //timer 요소 주입용 변수
var scriptCallKey;
var settimeout;
var callKeyArr = [];
var callKey;
var endRecTimeOut;
var manualType = 'N';
var freeRecStop = false;
var progress = {
		on: function(mask) {
			if($("#progress").length < 1) {
				$("html").append("<div id='progress'>")
			}
			$("#progress").css("opacity", "0");
			$("#progress").css("height", "100%");
			HoldOn.open({
					theme: "sk-circle"
				,	backgroundColor: "#FFFFFF"
			});
		}, off: function(mask) {
			HoldOn.close();
			$("#progress").remove();
		}
	}

window.onload=function(){
	checkSound()
	document.title='상품 비교설명과정 도우미(TA)';
    window.focus();
    window.moveTo(860,540);
    window.resizeTo(2000,2000);
    changeNecs();
    $('#downPdf').click(function() {
    	var url = contextPath+"/faceRecording/product/"+callKey+"/script/call?fileType=pdf"
    	window.open(url);
    	
	})
	
	$("#checkInfo").click(function() {
		freeRecStop=true;
				$("#checkInfo").attr('style','pointer-events:none');
				$(".interview-in").attr('style','display:none;');
				$(".interview-in2").attr('style','padding:none;');
				$(".interview-in2").attr('style','padding:105px 0px 3px 22px;');
				$("#checkInfo").attr('style',"display:none");
				$("#checkInfo2").show();
				$("#freeRecStart").attr('class','rec-btn');
				$("#freeRecStart").removeAttr('style','pointer-events:none');
				$("#freeRecStart").attr('onclick','freeRecStart()');
				showExit();
				stopTimer();
				sendmsg("recStop", scriptCallKey)
				progress.on();
				$('#holdon-message').html('창을 닫지 말고 기다려주세요. 내용 분석중입니다.');
				callKeyArr = [];
				callKeyArr.push(scriptCallKey);
			setTimeout(() => {
				mergeFile(callKeyArr);
			}, 4000);
	})
	
	
} 
$(function() {
	recTimer = document.getElementById("recTimer");
	autoScroll();
	webSocketConnect();
	SttWebSocketConnect();
	freeRecFlag = true;
	
})
function changeNecs(){
	if(params.BIZ_DIS==1){
		$(".fund").attr("style","display:none")
		$(".fundPop").attr("style","display:none")
		$("#mesgFund").attr("style","display:none")
	}
	if(params.BIZ_DIS==2){
		$(".etf").attr("style","display:none")
		$(".etfPop").attr("style","display:none")
		$("#mesgEtf").attr("style","display:none")
	}
}
function errorInfo() {
	$("#errorModal").fadeIn(500);
	$("#errorModal").attr('class','modal ac');
}
//function errorModalConfirm() {
//	$("#errorModal").attr('class','modal');
//	$("#errorModal").fadeOut(500);
//}

//장애 상황
function errorOccur() {
	stopTimer();
	$("#freeRecStart").attr("style","pointer-events:none");
	$("#downPdf").attr("style","pointer-events:none");
	$("#checkInfo").attr("style","pointer-events:none");
	$("#checkInfo2").attr("style","pointer-events:none");
	errorInfo();
}

function autoScroll() {
	$("#sttMsg").scrollTop=$("#sttMsg").scrollHeight;
}
// 녹취시작, startTimer 이후로 웹소켓 작성 필요
function freeRecStart() {
	$(".interview-in").removeAttr('style','display:none;');
	$(".interview-in2").removeAttr('style','padding');
	$(".interview-in2").attr('style','padding:0px 0px 3px 22px;');
	$("#checkInfo").removeAttr('style','pointer-events:none');
	$("#freeRecFooter").attr('class','interview-foot ac');
	$("#freeRecStart").attr('class','rec-btn play');
	$("#statePlay").attr('style','pointer-events:none');
	$("#freeRecStart").attr('style','pointer-events:none');
	$("#freeRecording").show();
	recStartWS();
}

function retryButton() {
	$("#checkInfo").removeAttr('style','display:none');
	$("#checkInfo").attr('style','pointer-events:auto');
	$("#checkInfo").attr('class',"btn-primary");
	$("#checkInfo").attr('onclick','mergeFileRe()');
}
//녹취 타이머 표시
function printTimer() {
	recTime++;
	recTimer.innerText=transTime();
}

//녹취 타이머 시작, 1000ms 마다 printTimer 실행
function startTimer() {
	timerFlag = setInterval(() => {
		printTimer();
	}, 1000);
}

//startTimer가 실행된 적이 있을 때만 stopTimer가 실행되게.
function stopTimer() {
	if(timerFlag !=null){
		clearTimeout(timerFlag);
	}
}

//타이머 멈춘 후 텍스트 원상복구
function resetTimer() {
	stopTimer();
	recTimer.innerText="00:00:00";
	recTime=0;
}


//시,분,초 전환 메서드
function transTime(){
	hour = parseInt(String(recTime/(60*60)));
	min = parseInt(String((recTime - (hour*60*60))/60));
	sec = recTime % 60;
	
	return String(hour).padStart(2,'0')+":"+String(min).padStart(2,'0')+":"+String(sec).padStart(2,'0');
}

function freeModalConfirm() {
	$("#freeRecModal").attr('class','modal');
	$("#freeRecModal").fadeOut(500);
	$("#freeRecFooter").attr('class','interview-foot ac');
}

function checkInfo() {
	$("#freeRecModal").fadeIn(500);
	$("#freeRecModal").attr('class','modal ac');
}

function messageAppend(res) {
	var sttMsg = res.result
	if( sttMsg.trim().length == 0 ){
		return;
	}
	var html ='';
	var sttMsgDiv = $('#sttMsg');
	switch(res.rxtx){
		case 'R' :
			html+='<div class="scroll">'+'<div class="script-customer">'+'<span class="profile">'+'profile - banker'+'</span>'+'<div class="mesgWrap">'+'<p class="mesg">'+sttMsg+'</p>'+'</div>'+'</div>'+'</div>';
			sttMsgDiv.append(html);
			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
		case 'T' :
			html+='<div class="scroll">'+'<div class="script-banker">'+'<span class="profile">'+'profile - banker'+'</span>'+'<div class="mesgWrap">'+'<p class="mesg">'+sttMsg+'</p>'+'</div>'+'</div>'+'</div>';
			sttMsgDiv.append(html);
			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
		case 'S' :
			html+='<div class="scroll">'+'<div class="script-banker">'+'<span class="profile">'+'profile - banker'+'</span>'+'<div class="mesgWrap">'+'<p class="mesg">'+sttMsg+'</p>'+'</div>'+'</div>'+'</div>';
			sttMsgDiv.append(html);
			document.querySelector(".scroll").scrollIntoView({behavior : 'smooth'});
			$(".scroll").attr('class','');
			break;
	}
}

function taResultInfo(res) {
	var necsAllArr = new Array();
	var tabooArr = new Array();
	var necsUseArr = new Array();
	var necsAllHtml ='';
	var necsHtml ='';
	var tabooHtml='';
	res.reason.forEach( i => {
		if(i.imcmpl_sale_stat != 'E' && res.result =='N' || i.imcmpl_sale_stat != 'E' && res.result =='Y'){
			//전체 필수어
			i.not_matc_necs_dec.split(',').forEach(info => {necsAllArr.push(info.trim())});
//			necsAllArr.push(i.not_matc_necs_dec.split(','));
			//사용 필수어
			i.matc_necs_dec.split(',').forEach(info => {necsUseArr.push(info.trim())});
//			necsUseArr.push(i.matc_necs_dec.split(','));
			//사용 금칙어
//			tabooArr.push(i.matc_bned_dec.split(','));
			i.matc_bned_dec.split(',').forEach( info => {tabooArr.push(info.trim())});
		}
		//사용 금칙어 화면에 추가.
		if(tabooArr[0].trim()!='') {
			for(let i=0; i<tabooArr.length; i++){
				if(i==tabooArr.length-1) {
					tabooHtml+='<span style="padding:1px; font-family: NanumSquare;">'+tabooArr[i]+'<span style="color:rgb(243,86,126); padding:1px; font-family: NanumSquare;">'+'은(는) 투자상품 판매시 사용 불가'+'</span>'+'합니다.'+'<br>'
					+'정정하여 다시 설명 하신 후' +'<button class="btn-primary" style="background: rgb(62,126,219); font-size: 11px; min-width: 58px; max-height: 22px; margin: 0px 5px 0px 5px;">'+'비교설명종료'+'</button>'+'버튼을 누르세요.'+'</span>';
					break;
				}
				tabooHtml+='<span style="padding:1px; font-family: NanumSquare;">'+tabooArr[i]+'<span style="padding:1px;">'+','+'</span>'+'</span>';
			}
			$('#tabooUsed').append(tabooHtml);
		}else{
			//금칙어가 없을 때
			tabooHtml+='<span style="padding:20px; font-family: NanumSquare;">'+'금칙어를 발견하지 못했어요.'+'</span>';
			$('#tabooUsed').append(tabooHtml);
		}
	})
}

//종료 버튼 보이기.
function showExit(){
	$("#freeRecExit").show();
}

function freeRecExit() {
	var exitConfirm = confirm("상품설명을 종료 하시겠습니까?");
	if(exitConfirm) {
		window.open('','_self','');
		window.close();
	}
}

function recStartWS(){
	progress.on();
	createCallKey();
	//taParam 기본셋팅
	addBasicTaParam()
	//STT & TA
	sendmsgStt("login");
	//최종파라미터 미리셋팅
	freeRecTaParamSetting();
	alreadyFinalRecParamSetting();
	
	taParam["para_id"] = "";
	callKey = params.RCD_KEY;
	
	//엔진녹취시작
	setTimeout(() => {
		sendmsg("recStart", scriptCallKey)
	}, 300);
	
}

function recStartFunction() {
}
function createCallKey(){
	scriptCallKey = new Date().getTime()+params.OPR_NO;
	
}
function createCallKeyMerge(){
	scriptCallKey = new Date().getTime()+params.OPR_NO+"_merge";
}
function alreadyFinalRecParamSetting() {
	finalRecParam.callid = params["RCD_KEY"];	
}


function mergeFile(callKeyArr){
    $.ajax({
        url:contextPath+"/updateMergeRecfile.do",
        type:"POST",
        dataType:"json",
        data:{"callKey":callKey},
        async: false,
        success:function(jRes){
            createCallKeyMerge();
            
//            sendmsgStt('finalrec');
            var parm = sendmsg("merge", scriptCallKey, callKeyArr);
            $.ajax({
                url:HTTP+"://"+listenIp+":28881/merge",
                type:"POST",
                dataType:"json",
                data:parm,
                async: false,
                success:function(jRes){
                    
                    if(jRes=='0'){
                    	progress.off();
                    	errorInfoRetry();
                        taFlag=false;
                        //retryButton();
                        return false;
                        
                    }else{
                        var temp = {
                                "r_v_rec_fullpath" : jRes.r_v_rec_fullpath,
                                "r_v_rec_ip" : jRes.r_v_rec_ip,
                                "r_v_filename" :jRes.r_v_filename,
                                "callKey" : callKey, // 그룹키
                                "scriptCallKey" : scriptCallKey , // 개별키
                                "productName" : params["PRD_NM"],
                                "productRiskNM" : params["PRD_RISK_GD"],
                                "server" : jRes.server,
                                "freeRec" : 'Y'
                        }
                        
                        $.ajax({
                            url:contextPath+"/insertMergeRecfile.do",
                            type:"POST",
                            dataType:"json",
                            data:temp,
                            async: false,
                            success:function(jRes){
                                console.log(jRes)
                                $('.rec_save').hide()
                                sendmsgStt('finalrec',jRes.resData.recDate);
//                                progress.off()
//                                alert("녹취가 완료되었습니다. 창 종료후 최종적으로 전산등록까지 \n완료하셔야 녹취파일이 WINI에 저장됩니다.\n(* 창이 열려 있을 시 다른 녹취 불가)");	
//                                AllRectryMergeInsert();
//                                endAlert('first');
                                startBtndisable();
                                recordingFinish='true;'
                            	rectryFlag = true;
                                rectryFlag2 = true;
                                resetTimer();
                            },
                            error:function(r){
                                progress.off()
                                alert('녹취저장에 실패하였습니다. 해당 구간 다시 녹취 진행해 주세요.')
                            }
                        });
                    }
                },
                error:function(r){
                    progress.off()
                     alert('녹취저장에 실패하였습니다.다시 시도해 주세요')
                }
            });
        }
    });
} 
function addProdCd(){
	if(params.BIZ_DIS  == 1 ){
		taParam["prod_cd"] = '00612677';
	}
	if(params.BIZ_DIS  == 2 ){
		taParam["prod_cd"] = '00612676';
	}
}

function endAlert(first){
	clearTimeout(endRecTimeOut);
	if(!first)
	alert('우측상단에 (X) 버튼을 눌러 화면을 종료해 주세요.\화면을 종료하지 않으면 다른 녹취를 시작할 수 없습니다.');
	  endRecTimeOut = setTimeout(() => {
		  endAlert();						
	  }, 20000);
}


function startBtnEnable(){
	$('#freeRecStart').attr('onclick','freeRecStart()');
}

function startBtndisable(){
	$('#freeRecStart').attr('onclick',null);
}

function sttTest(code,result,reason){
	switch (code) {
	//TA 요청응답
	case "taReq":
		taRes.callid = scriptCallKey;
		doSendStt(JSON.stringify(taRes));
		break;
	// TA 결과
	case "taResult" :
		taRes.callid = scriptCallKey;
		taRes.result = result;
		taRes.reason = reason;
		doSendStt(JSON.stringify(taResult));
		break;
	case "recEnd" :
		recEndParam.callid = scriptCallKey;
		recEndParam.result = result;
		recEndParam.reason = reason;
		doSendStt(JSON.stringify(recEndParam));
		break;
	case "logout" :
		logoutResParam.callid = scriptCallKey;
		logoutResParam.result = result;
		logoutResParam.reason = reason;
		doSendStt(JSON.stringify(logoutResParam));
	default:
		break;
	}
}
function sttInfoTest(code,rxtx,result){
	sttInfoParam.callid = scriptCallKey;
	sttInfoParam.rxtx = rxtx
	sttInfoParam.result = result;
	doSendStt(JSON.stringify(sttInfoParam));
}

function init(){
	var parameter = params;
	parameter = makeParam(parameter);
	
	productScriptFk(parameter.PRD_CD,parameter);
}

function makeParam(parameter){
	
	if(parameter.PRD_CD.charAt(0)=='T'){
		if(parameter.PRD_CD == 'T0000000000001'){
			parameter.BIZ_DIS='2';
			parameter.SYS_DIS='TOP'
				parameter["AGNPE_NM"] = '없음';
			if(parameter.RUSE_YN == 'Y'){
				parameter.PRD_CD = 'T0000000000002';
			}
		}
		if(parameter.PRD_CD == 'T0000000000003'){
			parameter["UP_INVEST_YN"] = 'Y'; 
			// params["CUS_ANSWER"] = '12:1,2,3,1,2,3,1,2,3,1,2,3';
			parameter.BIZ_DIS='2';
			parameter.SYS_DIS='TOP'
				parameter["AGNPE_NM"] = '없음';
			if(parameter.RUSE_YN == 'Y'){
				parameter.PRD_CD = 'T0000000000004';
			}
			
			upgradeTProductCodeCheck();
			
		}
		
		if(parameter.PRD_CD == 'T2014001580081'){
			parameter.BIZ_DIS='4';
			parameter.SYS_DIS='BK2';
		}
	}
	return parameter;
	
}
	
function productScriptFk(productCode,parameter){
	var flag = false;
	var dataStrFirst = {
			"params" :JSON.stringify(params),
			"callKey" : callKey
	};
	
	$.ajax({
		url:contextPath+'/faceRecInsertParameter.do',
		type:"POST",
		dataType:"json",
		data:dataStrFirst,
		async: false,
		success:function(jRes){
			console.log("params 저장완료");
		}
	});
	
	
	var selectUrl = "/faceRecSelectProductList.do";
	
	if(parameter.RCD_DSCD_NO != null || parameter.RCD_DSCD_NO != undefined){
		if(Number(parameter.RCD_DSCD_NO) == 2){
			console.log("해당 상품은 권유녹취");
			selectUrl = "/faceRecSelectOfferProduct.do";
		}
		
	}
	if(parameter["PRD_NM"] != undefined){
		if(parameter["PRD_NM"].indexOf('ELS-파생형') > -1){
			parameter["ELF_YN"] = 'Y';
		}
	}
	
	
	if(parameter.PRD_CD.length==0 || parameter.PRD_CD == null || parameter.PRD_CD==undefined){
		window.close();
		return false;
	}
	if(parameter.PRD_CD.split("|").length>1){
		console.log("다계좌상품")
		parameter["MORE_PRODUCT"] = 'Y'
	}else{
		console.log("다계좌상품 X")
		parameter["MORE_PRODUCT"] = 'N'
	}
	
	if(params.AGNPE_NM == undefined){
		parameter["AGNPE_NM"] = '없음';
	}
	
	// 강제로 BIZ_DIS 바꿔야함
	if(parameter["BIZ_DIS"] == 2){
		if(parameter["SYS_DIS"] =='WMS' || parameter["SYS_DIS"] =='DPT' || parameter["SYS_DIS"] =='TOP'){
			if(parameter.PRD_CD.charAt(0)!='T'){
				parameter["SYS_DIS"] = 'WMS';
			}
		}
	}
	if(parameter["BIZ_DIS"] == 4){
		var text = $('.checkNumberBox').html() + '(방카 재녹취는 방카시스템에서만 가능)';
		$('.checkNumberBox').html(text);
		
	}
	if(parameter["COM_REPT_TYPE"] != null){
		if(parameter["COM_REPT_TYPE"].trim().length == 0){
			parameter["COM_REPT_TYPE"] = 0;
		}
	}
	
	var dataStr = {
			"searchWord" : productCode,
			"callKey" : callKey,
			"params" :JSON.stringify(parameter)
	}
	console.log(dataStr);
	$.ajax({
		url:contextPath+selectUrl,
		type:"POST",
		dataType:"json",
		data:dataStr,
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				sendmsg("pdfdown");
			}
		}
	});
}

function checkSound(){
	var taudio = new Audio();
	var src = contextPath+"/getRecFileTest.do?fileName=beef.mp3"
	taudio.autoplay = true;
	taudio.src = src;
	taudio.pause();
	taudio.load();
	taudio.play();

	taudio.addEventListener('error',function(event) {
		taudio.pause();
    });

	taudio.addEventListener('loadeddata',function(){
		try {
			taudio.currentTime=0.1
			taudio.pause();
			return true;
		} catch(e){
			taudio.pause();
		}
	});
}
	