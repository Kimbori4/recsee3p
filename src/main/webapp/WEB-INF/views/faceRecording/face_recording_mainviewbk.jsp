<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>
	<%-- css page --%>
	

	<%-- js page --%>

	<script type="text/javascript" src="${recseeResourcePath}/js/face_recording_mainview.js"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnect.js"></script>
	<link rel="stylesheet" type="text/css" href="/recsee3p/resources/common/recsee/css/page/faceRecorder/faceRecoderMain.css" />

</head>
<body>
	<canvas id="canvas" width="1000" height="325" style="display:none;"></canvas>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
	
	
    <div class="main_contents">
    	<div class="centerDesc">New 대면녹취 / TTS 연동 시연</div>
    	<div class="leftDesc none">Pilot 시연</div>
    	<div class="numInq none">01</div>
    	<div class="numEx none">02</div>
    	<div class="numRec none">03</div>
    	
		<div class="inquiry none">투자성향 분석</div>
		<div id="6"  class="inquiry_1 none "  >투자성향 분석 - 1<br><p>펀드, 신탁, 퇴직연금...</p></div>
	    <div  id="1" class="inquiry_2 none" >투자성향 분석 - 2<br><p>변액보험</p></div>
	    
	    <div class="explain none">상품 설명</div>
	    <div   id="2"  class="explain_1 none " >펀드 - 1<br><p>AB글로벌 수익증권투자신탁</p></div>
	    <div   id="3"  class="explain_2 none" >펀드 - 2<br><p>미래에셋 글로벌혁신기업 ESG</p></div>
	    <div    id="4" class="explain_3 none" >신탁 - 1<br><p>ARIRANG 고배당주</p></div>
	    <div    id="5" class="explain_4 none" >신탁 - 2<br><p>HANARO e커머스</p></div>

		<div class="freeRec none">자유 녹취</div>
		<div   id="7" class="start none" >녹취 시작</div>
	    <div class="rec displayHidden none" >녹취 중입니다.</div>
		<div class="selectCode">
			<fieldset class='borderBox' align="center">
				<legend class='title'>수동 진입</legend>
			<fieldset class='borderBox'>
				<legend class='title'>상품종류</legend>
				<select id='BIZ_DIS' style="width: 100%;" onchange="changE()">
					<option value='4'>방카</option>
					<option value='99'>방카(권유녹취)</option>
				</select>
			</fieldset>
			<fieldset class='RECO_AFCO_CD_NO' align="center">
				<legend class='title'>보험사코드</legend>
				<select id='AFCO_CD_NO' style="width: 100%;">
					<option value='L02'>L02</option>
					<option value='L03'>L03</option>
					<option value='L04'>L04</option>
					<option value='L05'>L05</option>
					<option value='L34'>L34</option>
					<option value='L62'>L62</option>
				</select>
			</fieldset>
			<fieldset class='RECO_RPRS_PDCD'>
				<legend class='title'>대표코드</legend>
				<input class="kingCode" type="text" placeholder="ex ) L03_B21F101" style="width: 100%">
			</fieldset>
			<fieldset class='PRD_CD_NO'>
				<legend class='title'>보종코드</legend>
				<input class="subCode" type="text" placeholder="ex ) B21F101" style="width: 100%">
			</fieldset>
			<fieldset class='FND_CD_LIST_FILED'>
				<legend class='title'>펀드특징코드</legend>
				<input class="FND_CD_LIST" type="text" placeholder="ex) L032601000|L032631000" style="width: 100%">
			</fieldset>
			<fieldset class='PRD_CD_NO_LIST' style="display:none;">
				<legend class='title'>상품특징코드1</legend>
				<input class="FND_CD_LIST1" type="text"  style="width: 100%;">
			</fieldset>
			<fieldset class='PRD_CD_NO_LIST' style="display:none;">
				<legend class='title'>상품특징코드2</legend>
				<input class="FND_CD_LIST2" type="text"  style="width: 100%;">
			</fieldset>
			<fieldset class='PRD_CD_NO_LIST' style="display:none;">
				<legend class='title'>상품특징코드3</legend>
				<input class="FND_CD_LIST3" type="text"  style="width: 100%;">
			</fieldset>
			<fieldset class='FUND_RATE'>
				<legend class='title'>펀드비율</legend>
				<input class="FND_NM_RATE_INFO" type="text" placeholder="ex) 인덱스주식형|20%|글로벌MVP60|80%" style="width: 100%">
			</fieldset>
			<input class="codeBtnB" type="button" value="확인" style="width: 60%">
			<input class="codeBtnK" type="button" value="확인" style="width: 60%;display:none;">
		</div>
	</div>
	</fieldset>
	
	
    
	
</body>
<script type="text/javascript">
window.onload = function() {

	var L02 = {
			"kingCodes" : {
							"L02_M8193" : ['M08193','M08194','M08195','M08196'],
							"L02_M9053" : ['M09053','M09054','M09055','M09056']
											
			},
			"kingCode" :['L02_M08193','L02_M09053']
	}

	var L03 = {
			"kingCodes" : {
							"L03_B21F101" : ["B21F101","B21F102"],
							"L03_B22F101" : ["B22F101","B22F102"]
			},
			"kingCode" :['L03_B21F101','L03_B22F101']
	}
	var L04 = {
			"kingCodes" : {
							"L04_20352" : ["20351","20352"]
			},
			"kingCode" :['L04_20352']
	}
	var L05 = {
			"kingCodes" : {
							"L05_167232" : ["100067232"]
			},
			"kingCode" :['L05_167232']
	}
	var L34 = {
			"kingCodes" : {
							"L34_20207" : ["20207","20208","20209","20210"],
							"L34_20220" : ["20220","20221","20222","20223"]
			},
			"kingCode" :['L34_20207','L34_20220']
	}
	var L62 = {
			"kingCodes" : {
							"L62_150470" : ["150470","150540","15046"]
			},
			"kingCode" :['L62_150470']
	}

	
	
	$('.codeBtnT').hide();
	
	

	
	$('.click').click(function(){
		console.log(this)
		var id = this.id;
		click(id);
		
		})

	$('.recbtn').click(function(){
		console.log(this)
		var id = this.id;
		rec(id);
		})

		
	$('.codeBtnB').click(function(){
//AFCO_CD_NO:selected
// kingCode
// FND_CD_LIST
// FND_NM_RATE_INFO
		
		var bis_dis = $('#BIZ_DIS :selected').val()
		var AFCO_CD_NO = $('#AFCO_CD_NO :selected').val()
		var kingCode = $('.kingCode').val().trim();
		var subCode = $('.subCode').val().trim();
		var FND_CD_LIST = $('.FND_CD_LIST').val().trim();
		var FND_NM_RATE_INFO = $('.FND_NM_RATE_INFO').val().trim();
		var kingCodesArr = null;
		var subCodesArr = null;
		if(kingCode == null || kingCode.length==0){
			alert('대표 상품코드를 입력해주세요');
			return false;
		}
		if(subCode == null || subCode.length==0){
			alert('보종코드를 입력해주세요');
			return false;
		}
			
		if(FND_CD_LIST == null || FND_CD_LIST.length==0){
			alert('펀드 특징코드를 입력해주세요');
			return false;
		}
			
		if(FND_NM_RATE_INFO == null || FND_NM_RATE_INFO.length==0){
			alert('펀드 비율을 입력해주세요');
			return false;
		}

		if(kingCode.indexOf(AFCO_CD_NO) == -1){
			alert('보험사 코드와 대표코드가 맞지 않습니다.');
			return false;
		}

		if(AFCO_CD_NO == 'L02'){
			kingCodesArr=L02.kingCode;
		}
		if(AFCO_CD_NO == 'L03'){
			kingCodesArr=L03.kingCode;
		}
		if(AFCO_CD_NO == 'L04'){
			kingCodesArr=L04.kingCode;
		}
		if(AFCO_CD_NO == 'L05'){
			kingCodesArr=L05.kingCode;
		}
		if(AFCO_CD_NO == 'L34'){
			kingCodesArr=L34.kingCode;
		}
		if(AFCO_CD_NO == 'L62'){
			kingCodesArr=L62.kingCode;
		}
		kingFlag = false;
		
		for(var i = 0; i<kingCodesArr.length; i++){
			if(kingCode == kingCodesArr[i]){
				kingFlag=true;
			}
		}

		if(kingFlag == false){
			alert('보험사에 맞는 대표코드를 잘 작성해 주세요')
			return false;
		}

		

		bkParams['FND_CD_LIST'] = FND_CD_LIST;
		bkParams['FND_NM_RATE_INFO'] = FND_NM_RATE_INFO;
		bkParams['PRD_CD'] = kingCode;
		bkParams['PRD_CD_NO'] = subCode;
		bkParams['AFCO_CD_NO'] = AFCO_CD_NO;

		var key = Math.floor(Math.random()*11);
		bkParams['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
		$.ajax({
			url:"http://localhost:80",
			data: bkParams,
// 			data: testParam1,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(res){
				console.log(res)
			}
		});
		})


		$('.codeBtnK').click(function(){
			var list1 = $('.FND_CD_LIST1').val().trim();
			var list2 = $('.FND_CD_LIST2').val().trim();
			var list3 = $('.FND_CD_LIST3').val().trim();

			bkParams["RECO_RPRS_PDCD_1"] = list1;
			bkParams["RECO_RPRS_PDCD_2"] = list2;
			bkParams["RECO_RPRS_PDCD_3"] = list3;

			var key = Math.floor(Math.random()*11);
			bkParams['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
			$.ajax({
				url:"http://localhost:80",
				data: bkParams,
//	 			data: testParam1,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(res){
					console.log(res)
				}
			});
			
			
		});
}
// $(function(){
	
// 	$('.click').click(function(){
// 		console.log(this)
// 		var id = this.id;
// 		click(id);
// 		})

// 	$('.recbtn').click(function(){
// 		console.log(this)
// 		var id = this.id;
// 		rec(id);
// 		})
// })



var key = null;
// var ws =null;
// var recStart =  {"head":"RECSEE","code":50,"systemid":"CTR", "repoid":"CR","tenantid": "WRBK", "trxid":", "branchnumber":0, "devicenumber":0, "ipaddress":0, "etcparamlength":0, "etcparam":"}
// var recstop =  {"head":"RECSEE","code":51,"systemid":"CTR", "repoid":"CR","tenantid": "WRBK", "trxid":", "branchnumber":0, "devicenumber":0}

//
var shtParams = {
// 		"RCD_KEY":"1234545",
		"OPR_NO":"12345",
		"OPR_NM":"장진호",
		"ADVPE_NO":"12345",
		"ADVPE_NM":"장진호(권유자)",
		"CSNO":"12345",
		"CUS_NM":"윤우중",
		"BZ_BRCD":"1234578",
		"BZBR_NM":"상암IT장진호자리",
		"BIZ_DIS":"5",
		"SYS_DIS":"SHT",
		"PRD_RISK_GD":"1",
		"PRD_RISK_GD_NM":"매우높은위험",
		"CSINC_GD":"5",
		"PRD_NM":"삼성증권 제20199회 사모형 파생결합증권",
		"PRD_DIS":"FND",
		"PRD_CD" : "1610307220081"
	};
var bkParams = {

		"PCD_DSCD_NO" : "03",
		
		"FND_CD_LIST":"L032601000|L032631000",	//필수
		"FND_NM_RATE_INFO":"인덱스주식형|20%|글로벌MVP60|80%",	//필수

		
		"PSN_YN":"Y",
		"BIZ_DIS":"4",
		"PRD_CD":"L03_B21F101", //필수
		"PRD_CD_NO":"B21F101",  //필수 마지막XML
		"AFCO_CD_NO":"L03", //필수

		
		
		"RPRS_PDCD":"L03_B21F101",
		
		"OPR_NO":"12321321",
		"OPR_NM":"312312113",
		"ADVPE_NO":"조작자",
		"ADVPE_NM":"아무개씨",
		"CSNO":"123213",
		"CUS_NM":"구입하는고객",
		"BZ_BRCD":"12345678",
		"BZBR_NM":"영업점",
		"SYS_DIS":"BK2",
		"PRD_RISK_GD_NM":"매우낮은위험",
		"CSINC_GD_NM":"안정형",
		"PRD_DIS":"삼성증권 제20486회 공모형 파생결합증권 (주가연계증권)",
		"AFCO_NM":"에이비엘생명",
		"RPRS_PDCD_NM":"(무)보너스주는변액저축보험Ⅳ",
		"PRD_CD_NM":"(무)보너스주는변액저축보험Ⅳ(계약유지미선택_월납형)",
		"PRD_ICNT_NM":"저축형",
		"PRD_ICNT_DT_CT":"장기적인 자산증식, 목적자금 마련",
		"FUND_ICNT_NM":"위험중립형",
		"FUND_ICNT_DT_CT":"원금손실위험을 충분히 인식하고, 예/적금보다 높은 수익률을 위해 일정수준의 손실 감내가 가능한",
		"FUND_RATE":"50% 이상",
		"JOIN_AFCO_PRD":"미래에셋생명의 미래에셋생명 변액저축보험(무)2101",
		"PAY_INFO":"10년(1월납)",
		"ISFE":"152,000원",
		"INS_COMM_1_TIME":"계약체결시 또는 매월",
		"INS_COMM_1_COST":"1개월~15개월 일시납기본보험료의 0.1642%(82,100원)",
		"INS_COMM_4_TIME":"매월",
		"INS_COMM_4_COST":"2개월~120개월까지 기본적립금*0.0166667%(연간 기본보험료의 0.3% 한도) 121개월 이후 없음",
		"INS_COMM_2_TIME":"매월",
		"INS_COMM_2_COST":"기본보험료의 0.00152% ~ 0.01463%(761원 ~ 7,314원)",
		"SA_APINCM_TIME":"매일",
		"SA_APINCM_COST":"인덱스주식형: 적립금의 0.00167%(연 0.610%), 글로벌 MVP 60: 적립금의 0.00181%(연 0.660%)",
		"SA_ETC_COST_TIME":"사유가 발생할 때",
		"SA_ETC_COST_COST":"인덱스주식형: 적립금의 연 0.030%, 글로벌 MVP 60: 적립금의 연 0.012%",
		"SA_FUND_INCM_TIME":"매일",
		"SA_FUND_INCM_COST":"인덱스주식형: 적립금의 0.00012%(연 0.047%), 글로벌 MVP 60: 적립금의 0.00164%(연 0.599%)",
		"ANNU_ACUM_GUR_TIME":"매월(연금개시전)",
		"ANNU_ACUM_GUR_COST":"없음",
		"DEAD_AMT_GUR_TIME":"매월(연금개시전)",
		"DEAD_AMT_GUR_COST":"적립금의 0.00417%(연0.05%)",
		"CONT_MGNT_COST_OBJ":"계약관리비용",
		"CONT_MGNT_COST_TIME":"납입시",
		"CONT_MGNT_COST_COST":"추가납입보험료의 0.0%",
		"FUND_CHG_COST_OBJ":"펀드변경에 따른 관리비용",
		"FUND_CHG_COST_TIME":"펀드변경시",
		"FUND_CHG_COST_COST":"없음",
		"SVR_COMN_OBJ":"중도인출에 따른 비용",
		"SVR_COMN_TIME":"중도인출시",
		"SVR_COMN_COST":"인출금액의 0.2% 와 2,000원 중 적은금액 (단, 연4회에 한하여 인출 수수료면제)"
	}

function rec(commnd){

// 	if('7'==commnd){
	if('start'==commnd){
		
		ws = new WebSocket("ws://127.0.0.1:14100/EventListner");
		ws.onopen = function(evt){
			console.log(evt)
			if(key==null){
				key = '99'+new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();
							
				recStart.trxid=key;
				recstop.trxid=key;
				
				ws.send(JSON.stringify(recStart))
				
				
				$(".rec").removeClass("displayHidden");
			}else{
				alert('현재 녹취가 진행 중입니다. 녹취를 먼저 종료해 주세요')
				return false;
			}
		};
		ws.onclose = function(evt){console.log(evt);ws = null;key = null;};
		ws.onmessage = function(evt){console.log(evt);};
		ws.onerror = function(evt){console.log(evt);alert('녹취가 정상적으로 진행되지 않았습니다. 페이지를 새로고침 해주세요.');ws = null;key = null;};
		
	}else {
		if(key==null){
			alert('진행중인 녹취가 없습니다. 녹취를 먼저 시작해 주세요')
			return false;
		}else{
			if(ws!=null && ws.OPEN==1){
				ws.send(JSON.stringify(recstop));
				setTimeout(function() {
					console.log(recstop)
					ws.close();
					$(".rec").addClass("displayHidden");
				},500);
				
			}else{
				alert('녹취가 정상적으로 진행되지 않았습니다. 페이지를 새로고침 해주세요.')
			}
		}
	}
}


function changE(){

	var biz_dis = $('#BIZ_DIS').val();
	//신탁
	if(biz_dis == 4){
		bkParams["RCD_DSCD_NO"] = "03"
		$('.RECO_AFCO_CD_NO').show()
		$('.RECO_RPRS_PDCD').show();
		$('.PRD_CD_NO').show();
		$('.FND_CD_LIST_FILED').show();
		$('.PRD_CD_NO_LIST').hide();
		$('.FUND_RATE').show();
		$('.codeBtnB').show();
		$('.codeBtnK').hide();
	}
	if(biz_dis == 99){
		bkParams["RCD_DSCD_NO"] = "02";
		$('.RECO_AFCO_CD_NO').hide();
		$('.RECO_RPRS_PDCD').hide();
		$('.PRD_CD_NO').hide();
		$('.FND_CD_LIST_FILED').hide();
		$('.PRD_CD_NO_LIST').show();
		$('.FUND_RATE').hide();
		$('.codeBtnB').hide();
		$('.codeBtnK').show();
		
	}

	
	
	
}

function guideFileDown(){
	alert("download")

	window.location.href = "/recsee3p/faceRecording/face_recording_guidepdf";
	
}

function encodeURI(uri){
	return encodeURI(uri);
}

</script>

</html>