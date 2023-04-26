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

	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/face_recording_mainview.js"></script>
	<script type="text/javascript" src="<c:out value="${recseeResourcePath}"/>/js/websocketConnect.js"></script>
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
		<input class="codeBtnF" id="freeRec" type="button" value="펀드자유녹취(비교설명)" style="width: 60%" onclick="freeRecBtnE(2)">
		<input class="codeBtnF" id="freeRec" type="button" value="펀드자유녹취(상품설명)" style="width: 60%" onclick="freeRecBtnE2(2)">
		<input class="codeBtnF" id="freeRec" type="button" value="신탁자유녹취(비교설명)" style="width: 60%" onclick="freeRecBtnE(1)">
		<input class="codeBtnF" id="freeRec" type="button" value="신탁자유녹취(상품설명)" style="width: 60%" onclick="freeRecBtnE2(1)">
		<input class="codeBtnF" id="freeRec" type="button" value="투자성향분석 고도화 진입" style="width: 60%" onclick="goDooT()">
		<input class="codeBtnF" id="freeRec" type="button" value="투자성향분석 고도화 진입 (재사용Y)" style="width: 60%" onclick="goDooT2()">
		<input class="codeBtnF" id="freeRec" type="button" value="방카 상품권유 진입" style="width: 60%" onclick="bkCompare()">
		<input class="codeBtnF" id="freeRec" type="button" value="방카 상품설명 진입" style="width: 60%" onclick="bkProduct()">
		<input class="codeBtnF" id="freeRec" type="button" value="ELS상품진입" style="width: 60%" onclick="eltProduct()">
		<fieldset class='borderBox'>
		<legend class='title'>상품종류</legend>
			<select id='BIZ_DIS' style="width: 100%;" onchange="changE()">
				<option value='3'>다계좌 & 양수도</option>
				<option value='2'>펀드</option>
				<option value='1'>신탁</option>
				<option value='T'>투자성향분석</option>
				<option value='5'>퇴직연금</option>
			</select>
		</fieldset>
			<fieldset class='borderBox'>
				<legend class='title'>상품코드</legend>
				<input class="codeBox" type="text" placeholder="ex) A101|A102|A103" style="width: 100%">
			</fieldset>
			<fieldset class='borderBox' id="PSY_YNField">
				<legend class='title'>개인/법인</legend>
				<select id='PSN_YN' style="width: 100%;">
					<option value='Y'>개인</option>
					<option value='N'>법인</option>
				</select>
			</fieldset>
			<fieldset class='borderBox' id="PSY_YNField">
				<legend class='title'>대리인</legend>
				<select id='AGNPE_NM' style="width: 100%;">
					<option value='Y'>있음</option>
					<option value='N'>없음</option>
				</select>
			</fieldset>
<!-- 			<fieldset class='borderBox' id="TBox"> -->
<!-- 				<legend class='title'>투자성향분석</legend> -->
<!-- 				<select id='RUSE_YN' style="width: 100%;"> -->
<!-- 					<option value='N'>재사용녹취(N)</option> -->
<!-- 					<option value='Y'>재사용녹취(Y)</option> -->
<!-- 				</select> -->
<!-- 			</fieldset> -->
			<fieldset class='borderBox' id="RUSEField" style="display:none;">
				<legend class='title'>재사용녹취</legend>
				<select id='RUSE_YN' style="width: 100%;">
					<option value='N'>재사용녹취(N)</option>
					<option value='Y'>재사용녹취(Y)</option>
				</select>
			</fieldset>
			<fieldset class='borderBox' id="TR_AC_YN_Field" style="display:block;">
				<legend class='title'>양수도녹취</legend>
				<select id='TR_AC_YN' style="width: 100%;">
					<option value='Y'>Y</option>
					<option value='N'>N</option>
				</select>
			</fieldset>
			<fieldset class='borderBox' id="CUCDField">
				<legend class='title'>통화</legend>
				<select id='CUCD' style="width: 100%;">
					<option value='KRW'>KRW</option>
					<option value='USD'>USD</option>
				</select>
			</fieldset>
			<fieldset class='borderBox' id="SYSField" style="display: none;">
				<legend class='title'>ISA</legend>
				<select id='SYSDIS' style="width: 100%;" onchange="SYSDISF()">
					<option value='TOP'>-</option>
					<option value='TISA'>TISA(신탁형ISA)</option>
					<option value='EISA'>EISA(일임형ISA)</option>
				</select>
			</fieldset>
			<fieldset class='borderBox' id="REG_AMField">
				<legend class='title'>가입금액</legend>
				<input id ="REG_AM" type="text" placeholder="10000|20000|30000" style="width: 100%;">
			</fieldset>
     <!-- 
      -->			
<!--      	 <fieldset class='borderBox' id="RISK_Field"> -->
<!-- 			<legend class='title'>상품위험등급</legend> -->
<!-- 			<select id='PRD_RISK_GD' style="width: 100%;"> -->
<!-- 				<option value='1'>1등급(매우높은위험)</option> -->
<!-- 				<option value='2'>2등급(높은 위험)</option> -->
<!-- 				<option value='3'>3등급 (다소 높은 위험)</option> -->
<!-- 				<option value='4'>4등급(보통위험)</option> -->
<!-- 				<option value='5'>5등급(낮은위험)</option> -->
<!-- 			</select> -->
<!-- 		</fieldset> -->

		<!--
		<fieldset class='borderBox' id="CUCDField">
					<legend class='title'>가이드 파일 다운로드</legend>
					<input class="downBtn" type="button" value="다운로드" style="width: 60%" onclick="guideFileDown()">
				</fieldset>
						-->
			<input class="codeBtn" type="button" value="확인" style="width: 60%">
			<input class="codeBtnT" type="button" value="확인" style="width: 60%">
		</div>
	</div>
	</fieldset>
	
	
    
	
</body>
<script type="text/javascript">
window.onload = function() {


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

	$('.codeBtnT').click(function(){
		var code = $('.codeBox').val().trim();
		var bis_dis = $('#BIZ_DIS :selected').val()
		if(bis_dis =='T'){
			dataStrT.PSN_YN = $('#PSN_YN :selected').val()
			dataStrT.PRD_CD = code;
			dataStrT.BIZ_DIS = 2;
			dataStrT.RUSE_YN = $('#RUSE_YN :selected').val();
			var date = new Date();
			var key = Math.floor(Math.random()*11);
			dataStrT['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
			
			$.ajax({
				url:"http://localhost:80",
				data:dataStrT,
				type:"POST",
				dataType:"json",
				async: false,
				success:function(res){
					console.log(res)
				}
			});
			return;
			}
	
		
		
		
		})
		
	$('.codeBtn').click(function(){
		var bis_dis = $('#BIZ_DIS :selected').val()
		var psn_yn = null;
		var cucd = null;
		var first = null;
		var two = null;
		var three = null;
		var reg_am = $('#REG_AM').val()
		var ad047 = first+"-"+two+"-"+three;
		var code = $('.codeBox').val().trim();
		if(reg_am.length==0){
			alert('가입금액을 하나라도 적어주세요.')
			return false;
		}
		if(code == null || code.length==0){
			alert('상품코드를 입력해주세요');
			return false;
		}


		dataStr.REG_AM = reg_am;


		
		//펀드
		if(bis_dis == 2 || bis_dis==3){
			psn_yn = $('#PSN_YN :selected').val()
			cucd = $('#CUCD :selected').val()
			first = $('#fff :selected').val()
			two = $('#ggg :selected').val()
			three = $('#hhh :selected').val()
// 			prd_risk_gd = $('#PRD_RISK_GD :selected').val()
// 			var ad047 = first+"-"+two+"-"+three;


			dataStr.CUCD = cucd;
// 			dataStr.AD047 = ad047;
		}

		//신탁
		if(bis_dis == 1){
			psn_yn = $('#PSN_YN :selected').val()
// 			prd_risk_gd = $('#PRD_RISK_GD :selected').val()

		}	



		dataStr.PRD_CD = code;
		dataStr.PSN_YN = psn_yn;
		dataStr["BIZ_DIS"] = bis_dis;
		if(bis_dis == 3){
			dataStr["BIZ_DIS"] = "2";
			dataStr["SYS_DIS"] = "TOP";
			dataStr["TR_AC_YN"] = $('#TR_AC_YN :selected').val();
		}else{
			dataStr["TR_AC_YN"] = "N";
		}
		if(bis_dis == 5){
			dataStr["BIZ_DIS"] = "5";
			dataStr["SYS_DIS"] = "SHT";
		}
		dataStr.REG_AM = reg_am;


		if($('#AGNPE_NM :selected').val() == 'Y'){
			dataStr["AGNPE_NM"] = "테스트대리인";
		}
		if($('#AGNPE_NM :selected').val() == 'N'){
			dataStr["AGNPE_NM"] = "없음";
		}
		
		var date = new Date();
		var key = Math.floor(Math.random()*11);
		dataStr['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
		testParam1['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
		$.ajax({
			url:"http://localhost:80",
			data: dataStr,
// 			data: testParam1,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(res){
				console.log(res)
			}
		});
		})
		
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
// 		"FND_CD_LIST":"L02A5004|L02C1004|L02C1005|L02C1006|L02C4002",	//필수
// 		"FND_CD_LIST":"L34N220|L34N260|L34N320",	//필수
		"FND_CD_LIST":"L032601000|L032631000",	//필수
		"FND_NM_RATE_INFO":"인덱스주식형|20%|글로벌MVP60|80%",	//필수
		
		"PSN_YN":"Y",
		"BIZ_DIS":"4",
// 		"PRD_CD":"L34_20207", //필수
		"PRD_CD":"L03_B21F101", //필수
// 		"PRD_CD_NO":"20209",  //필수 마지막XML
		"PRD_CD_NO":"B21F101",  //필수 마지막XML
		"AFCO_CD_NO":"L03", //필수
		
		"RPRS_PDCD":"L03_B21F101",
		
		"OPR_NO":"12321321",
		"OPR_NM":"312312113",
		"ADVPE_NO":"조작자",
		"ADVPE_NM":"67890",
		"CSNO":"권유자",
		"CUS_NM":"홍길동",
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


var dataStr2 = {

        "OPR_NO":"29310221",
        "RCD_KEY":"29310221084255202111251123",
        "OPR_NM":"사단구", //판매자 = 조작자명 
        "ADVPE_NO":"29310221",
        "CSNO":"614486582",
        "CUS_NM":"문정종",
        "BZ_BRCD":"084255",
        "BZBR_NM":"상암DMC금융센터",
        "BIZ_DIS":2,
        "SYS_DIS":"DPT",
        "PRD_RISK_GD_NM":"보통위험",
        "CSINC_GD_NM":"공격투자형",
        "PRD_NM":"유진챔피언배당주증권자투자신탁[채권혼합]",
        "PRD_DIS":"DLF",
        "PRD_CD":"1610200210081",
        "CUCD":"USD",
        "REG_AM":"1000000000.000",
        "PRD_RISK_GD":4,
        "PSN_YN":"Y",//개인여부
        "AD047" : "2014-11-26"
                 
}

var dataStr = {
		"TR_AC_YN" : "N" , // 양수도
		"ISA_OP_TYPE" : "2",
		"COM_REPT_TYPE" : "4",
		"COM_REPT_TYPE_ETC" : "테스트 글씨입니다.",
		"PRD_NO_MORE" : "Y",
        "CSINC_GD_NM":"공격투자형",
		"RCD_KEY":	0,//녹취키
		"OPR_NO" :1111111,	//조작자번호
		"OPR_NM":"조작자",//조작자명
		"ADVPE_NO":	2222222,//권유자번호
		"CSNO" :	33333333,//고객번호
		"CUS_NM" :	"고객명",//고객명
		"BZ_BRCD" :"2566212",//영업점코드
		"BZBR_NM" :	"영업점",//영업점명
		"PRD_DIS":"ELF",//운용상품구분
		"SSBLT_IVPE_YN":"N",//부적합투자자
		"AGTP_YN":"Y",//공격투자자
		"AGTP_PRD_RCM_RSN":"1",//상품선택
		"FST_BAS_PR_DCS_DT":"20181228",//최초기준가격결정일
		"MN_PAY_YN":"N",//월지급식여부
		"SYS_DIS" : "TOP",
		"AGNPE_NM":"없음",//대리인명(대리인이 없을 경우:없음
 		"ADVPE_NM" :"권유자",//권유자명
	}


var dataStrT = {
		"ALL_DIS" : "02", //전체구분(AS-IS구분)
		"OPR_NO"  : "00", //조작자 번호
		"OPR_NM"  : "조작자", //조작자명
		"ADVPE_NO" : "67890", //권유자번호
		"ADVPE_NM" : "권유자", //권유자명
		"CSNO" : "23456", //고객번호
		"CUS_NM" : "고객명", //고객명
		"BZ_BRCD" : "12345678", //영업점코드
		"BZBR_NM" : "영업점", //영업정 명
		"BIZ_DIS" : 1, //업무구분
		"SYS_DIS" : "TOP", //시스템구분
		"AGE" : 80, //나이
		"AGNPE_NM" : "장진호"
// 		"PSN_YN" : "Y", //개인법인유무
// 		"RUSE_YN" : "Y" //재사용녹취
}

/*
var freeRec = {
		"ADVPE_NM": "권유자",
		"ADVPE_NO": "2222222",
		"AGNPE_NM": "테스트대리인",
		"AGTP_PRD_RCM_RSN": "1",
		"AGTP_YN": "Y",
		"BIZ_DIS": "2",
		"BZBR_NM": "영업점",
		"BZ_BRCD": "2566212",
		"COM_REPT_TYPE": "4",
		"COM_REPT_TYPE_ETC": "테스트 글씨입니다.",
		"CSINC_GD_NM": "공격투자형",
		"CSNO": "33333333",
		"CUCD": "KRW",
		"CUS_NM": "고객명",
		"FST_BAS_PR_DCS_DT": "20181228",
		"ISA_OP_TYPE": "2",
		"MN_PAY_YN": "N",
		"MORE_PRODUCT": "N",
		"OPR_NM": "조작자",
		"OPR_NO": "1111111",
		"PRD_CD": "2021102713206",
		"PRD_DIS": "ELF",
		"PRD_NO_MORE": "N",
		"PSN_YN": "Y",
		"RCD_KEY": null,
		"REG_AM": "123",
		"SSBLT_IVPE_YN": "N",
		"SYS_DIS": "WMS",
		"TR_AC_YN": "N"
	}
	*/
var freeRec = {
		"ADVPE_NM": "권유자",
		"ADVPE_NO": "2222222",
		"AGNPE_NM": "테스트대리인",
		"AGTP_PRD_RCM_RSN": "1",
		"AGTP_YN": "Y",
		"BIZ_DIS": "1",
		"BZBR_NM": "영업점",
		"BZ_BRCD": "2566212",
		"COM_REPT_TYPE": "4",
		"COM_REPT_TYPE_ETC": "테스트 글씨입니다.",
		"CSINC_GD_NM": "공격투자형",
		"CSNO": "33333333",
		"CUCD": "KRW",
		"CUS_NM": "고객명",
		"FST_BAS_PR_DCS_DT": "20181228",
		"ISA_OP_TYPE": "2",
		"MN_PAY_YN": "N",
		"MORE_PRODUCT": "N",
		"OPR_NM": "조작자",
		"OPR_NO": "1111111",
		"PRD_CD": "2021102713206",
		"PRD_DIS": "ELF",
		"PRD_NO_MORE": "N",
		"PSN_YN": "Y",
		"RCD_KEY": null,
		"REG_AM": "123",
		"SSBLT_IVPE_YN": "N",
		"SYS_DIS": "WMS",
		"TR_AC_YN": "N",
		"REC_YN" : "N",
		"TA_TYPE1" : "Y",
		"TA_TYPE" : "1"
	}

var freeRec2 = {
		"ADVPE_NM": "권유자",
		"ADVPE_NO": "2222222",
		"AGNPE_NM": "테스트대리인",
		"AGTP_PRD_RCM_RSN": "1",
		"AGTP_YN": "Y",
		"BIZ_DIS": "1",
		"BZBR_NM": "영업점",
		"BZ_BRCD": "2566212",
		"COM_REPT_TYPE": "4",
		"COM_REPT_TYPE_ETC": "테스트 글씨입니다.",
		"CSINC_GD_NM": "공격투자형",
		"CSNO": "33333333",
		"CUCD": "KRW",
		"CUS_NM": "고객명",
		"FST_BAS_PR_DCS_DT": "20181228",
		"ISA_OP_TYPE": "2",
		"MN_PAY_YN": "N",
		"MORE_PRODUCT": "N",
		"OPR_NM": "조작자",
		"OPR_NO": "1111111",
		"PRD_CD": "200004146",
		"PRD_DIS": "ELF",
		"PRD_NO_MORE": "N",
		"PSN_YN": "Y",
		"RCD_KEY": null,
		"REG_AM": "123",
		"SSBLT_IVPE_YN": "N",
		"SYS_DIS": "TOP",
		"TR_AC_YN": "N",
		"REC_YN" : "N",
		"TA_TYPE2" : "Y",
		"TA_TYPE" : "2"
	}


var testParam1 = {
		
			"ALL_DIS":"02",
			"OPR_NO":"20201185",
			"RCD_KEY":"2020118502070020211207133327"
			,"OPR_NM":"동용홍",
			"CSNO":"616811033",
			"CUS_NM":"동용홍",
			"BZ_BRCD":"020700",
			"BZBR_NM":"우리에프아이에스",
			"BIZ_DIS":3,
			"SYS_DIS":"TOP",
			"AGE":"030",
			"PSN_YN":"Y",
			"RUSE_YN":"N",
			"ADVPE_NM":"동용홍",
			"PRD_CD":"T0000000000001" 
			
		
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
	if(biz_dis == 1){
		dataStr["SYS_DIS"] = "TOP";
		$('#CUCDField').hide();
		$('#AD047Field').hide();
		$('#PSY_YNField').show();
		$('#REG_AMField').show();
// 		$('#RISK_Field').show();
		$('#RUSEField').hide();
		$('.codeBtn').show();
		$('.codeBtnT').hide();
		$('#SYSField').show();
		$('#TR_AC_YN_Field').hide();
		prdCdNo()
	}
	if(biz_dis == 2){
		dataStr["SYS_DIS"] = "TOP";
		$('#CUCDField').show();
		$('#AD047Field').show();
		$('#PSY_YNField').show();
		$('#REG_AMField').show();
// 		$('#RISK_Field').show();
		$('#RUSEField').hide();
		$('.codeBtn').show();
		$('.codeBtnT').hide();
		$('#SYSField').show();
		$('#TR_AC_YN_Field').hide();
		
		prdCdNo()
	}
	if(biz_dis == 3){
		dataStr["SYS_DIS"] = "TOP";
		$('#CUCDField').show();
		$('#AD047Field').show();
		$('#PSY_YNField').show();
		$('#REG_AMField').show();
// 		$('#RISK_Field').show();
		$('#RUSEField').hide();
		$('.codeBtn').show();
		$('.codeBtnT').hide();
		$('#SYSField').hide();
		$('#TR_AC_YN_Field').show();
		prdCdNoMore()
	}

	if(biz_dis == 'T'){
		dataStr["SYS_DIS"] = "TOP";
		$('#CUCDField').hide();
		$('#AD047Field').hide();
// 		$('#PSY_YNField').hide();
		$('#REG_AMField').hide();
		$('#RISK_Field').hide();
		$('#RUSEField').show();
		$('.codeBtnT').show();
		$('.codeBtn').hide();
		$('#SYSField').hide();
		$('#TR_AC_YN_Field').hide();
		prdCdNo()
		}
	
	if(biz_dis == '5'){
		dataStr["SYS_DIS"] = "SHT";
		$('#CUCDField').hide();
		$('#AD047Field').hide();
// 		$('#PSY_YNField').hide();
		$('#REG_AMField').show();
		$('#RISK_Field').hide();
		$('#RUSEField').hide();
		$('.codeBtnT').hide();
		$('.codeBtn').show();
		$('#SYSField').hide();
		$('#TR_AC_YN_Field').hide();
		prdCdNo()
		}

	
	
	
}

function guideFileDown(){
	alert("download")

	window.location.href = "/recsee3p/faceRecording/face_recording_guidepdf";
	
}

function encodeURI(uri){
	return encodeURI(uri);
}

function prdCdNoMore(){
	dataStr["PRD_NO_MORE"] = "Y";	
	$('.codeBox').attr('placeholder','ex) A101|A102|A103');
	$('#REG_AM').attr('placeholder','10000|20000|30000');

}
function prdCdNo(){
	dataStr["PRD_NO_MORE"] = "N";	
	$('.codeBox').attr('placeholder','상품코드를 입력해주세요');
	$('#REG_AM').attr('placeholder','가입금액를 입력해주세요');
}

function SYSDISF(){
	dataStr["SYS_DIS"] = $('#SYSDIS :selected').val();		
}
function freeRecBtnE(bizType){
	var date = new Date();
	var key = Math.floor(Math.random()*11);
	freeRec['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	freeRec['BIZ_DIS'] = bizType;
	$.ajax({
		url:"http://localhost:80",
		data: freeRec,
// 			data: testParam1,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

}
function freeRecBtnE2(bizType){
	var date = new Date();
	var key = Math.floor(Math.random()*11);
	freeRec2['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	freeRec2['BIZ_DIS'] = bizType;

	if(bizType == 1){
		freeRec2["PRD_CD"] = '200004146';
	}
	if(bizType ==2){
		freeRec2["PRD_CD"] = '1613502378281';
	}
	
	
	$.ajax({
		url:"http://localhost:80",
		data: freeRec2,
// 			data: testParam1,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

}
function goDooT(){
	var date = new Date();
	var key = Math.floor(Math.random()*11);
	var goDooP= {
			  "AGE": "80",
			  "CSNO": "23456",
			  "CUS_NM": "고객명",
			  "OPR_NM": "조작자",
			  "OPR_NO": "00",
			  "PRD_CD": "T0000000000003",
			  "PSN_YN": "N",
			  "ALL_DIS": "02",
			  "BIZ_DIS": "1",
			  "BZBR_NM": "영업점",
			  "BZ_BRCD": "12345678",
			  "RCD_KEY": "8202213245321232132196785541",
			  "RUSE_YN": "N",
			  "SYS_DIS": "TOP",
			  "ADVPE_NM": "권유자",
			  "ADVPE_NO": "67890",
			  "AGNPE_NM": "없음",
			  "REC_YN" : "Y",
			  "CUS_ANSER" : "12:1,2,3,1,2,3,1,2,3,4,2,1"
			}
	goDooP["RCD_KEY"] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	$.ajax({
		url:"http://localhost:80",
		data: goDooP,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

}
function goDooT2(){
	var date = new Date();
	var key = Math.floor(Math.random()*11);
	var goDooP2= {
			  "AGE": "80",
			  "CSNO": "23456",
			  "CUS_NM": "고객명",
			  "OPR_NM": "조작자",
			  "OPR_NO": "00",
			  "PRD_CD": "T0000000000003",
			  "PSN_YN": "Y",
			  "ALL_DIS": "02",
			  "BIZ_DIS": "1",
			  "BZBR_NM": "영업점",
			  "BZ_BRCD": "12345678",
			  "RCD_KEY": "8202213245321232132196785541",
			  "RUSE_YN": "Y",
			  "SYS_DIS": "TOP",
			  "ADVPE_NM": "권유자",
			  "ADVPE_NO": "67890",
			  "AGNPE_NM": "없음",
			  "REC_YN" : "Y",
			  "CUS_ANSER" : "3:1,3,4"
			}
	goDooP2["RCD_KEY"] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	$.ajax({
		url:"http://localhost:80",
		data: goDooP2,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

}

var bkCompareParam = {
		  "CSNO": "2000478955",
		  "CUS_NM": "김명석",
		  "OPR_NM": "김은진",
		  "OPR_NO": "29711875",
		  "PRD_CD": "L99_99999",
		  "PRD_NM": "방카슈랑스 변액보험",
		  "BIZ_DIS": "4",
		  "BZBR_NM": "TCE강남센터",
		  "BZ_BRCD": "020802",
		  "RCD_KEY": "2971187502050304110423",
		  "SYS_DIS": "BK2",
		  "ADVPE_NM": "김은진",
		  "ADVPE_NO": "29711875",
		  "AGNPE_NM": "없음",
		  "FUND_RATE": "30% 미만",
		  "retryType": "",
		  "CSINC_GD_NM": "위험선호형",
		  "PRD_ICNT_NM": "연금형",
		  "RCD_DSCD_NO": "02",
		  "FUND_ICNT_NM": "위험선호형",
		  "PRD_ICNT_DT_CT": "노후 대비 등을 위한 목적자금 마련",
		  "RECO_AFCO_NM_1": "미래에셋생명",
		  "RECO_AFCO_NM_2": "교보생명",
		  "RECO_AFCO_NM_3": "",
		  "FUND_ICNT_DT_CT": "아주 높은 수익률을 원하고, 대부분을 위험 자산에 투자할 의향이 있는",
		  "RECO_RPRS_PDCD_1": "L34_20220",
		  "RECO_RPRS_PDCD_2": "L05_167232",
		  "RECO_RPRS_PDCD_3": "",
		  "RECO_AFCO_CD_NO_1": "L34",
		  "RECO_AFCO_CD_NO_2": "L05",
		  "RECO_AFCO_CD_NO_3": ""
		}

var bkProductParam = 	{
		  "CSNO": "2010545823",
		  "ISFE": "200,000원",
		  "CUS_NM": "보선황",
		  "OPR_NM": "지우리",
		  "OPR_NO": "20200200",
		  "PRD_CD": "L34_20209",
		  "PRD_NM": "미래에셋생명 변액저축보험(무)2108 - [기본형] 월납입형",
		  "AFCO_NM": "미래에셋생명",
		  "BIZ_DIS": "4",
		  "BZBR_NM": "부천금융센터",
		  "BZ_BRCD": "020808",
		  "PRD_DIS": "BNK2",
		  "RCD_KEY": "2020020002080820230207135347",
		  "SYS_DIS": "BK2",
		  "ADVPE_NM": "지우리",
		  "ADVPE_NO": "20200200",
		  "AGNPE_NM": "없음",
		  "PAY_INFO": "10년(1월납)",
		  "FUND_RATE": "",
		  "PRD_CD_NM": "미래에셋생명 변액저축보험(무)2108 - [기본형] 월납입형",
		  "PRD_CD_NO": "20209",
		  "RPRS_PDCD": "L34_20209",
		  "retryType": "",
		  "AFCO_CD_NO": "L34",
		  "CSINC_GD_NM": "적극투자형",
		  "FND_CD_LIST": "L34N360|L34N500|L34N350",
		  "PRD_ICNT_NM": "",
		  "RCD_DSCD_NO": "03",
		  "FUND_ICNT_NM": "",
		  "MORE_PRODUCT": "N",
		  "RPRS_PDCD_NM": "미래에셋생명 변액저축보험(무)2108",
		  "SVR_COMN_OBJ": "중도인출에 따른 비용",
		  "JOIN_AFCO_PRD": "미래에셋생명의 미래에셋생명 변액저축보험(무)2108",
		  "SVR_COMN_COST": "인출금액의 0.2% 와 2,000원 중 적은금액 (단, 연4회에 한하여 인출 수수료 면제)",
		  "SVR_COMN_TIME": "중도인출시",
		  "PRD_ICNT_DT_CT": "",
		  "SA_APINCM_COST": "ETF글로벌MVP60: 적립금의 0.00107%(연 0.390%), ETF글로벌MVP30: 적립금의 0.00096%(연 0.350%), 국내채권형: 적립금의 0.00060%(연 0.220%)",
		  "SA_APINCM_TIME": "매일",
		  "FUND_ICNT_DT_CT": "",
		  "INS_COMM_1_COST": "7년이내 기본보험료의 3.7100%(7,420원)",
		  "INS_COMM_1_TIME": "매월",
		  "INS_COMM_2_COST": "기본보험료의 0.16070% ~ 0.73148%(321원 ~ 1,462원)",
		  "INS_COMM_2_TIME": "매월",
		  "INS_COMM_4_COST": "납입기간 이내 기본보험료의 1.000%(2,000원), 납입기간 경과 후 기본보험료의 1.000%(2,000원)",
		  "INS_COMM_4_TIME": "매월",
		  "FND_NM_RATE_INFO": "ETF글로벌MVP30 은(는) 20% , 국내채권형 은(는) 30% , ETF글로벌MVP60 은(는) 50%",
		  "SA_ETC_COST_COST": "ETF글로벌MVP60: 적립금의 연 0.073%, ETF글로벌MVP30: 적립금의 연 0.072%, 국내채권형: 적립금의 연 0.003%",
		  "SA_ETC_COST_TIME": "사유가 발생할 때",
		  "DEAD_AMT_GUR_COST": "적립금의 0.00417%(연0.05%)",
		  "DEAD_AMT_GUR_TIME": "매월(연금개시전)",
		  "FUND_CHG_COST_OBJ": "펀드변경에 따른 관리비용",
		  "SA_FUND_INCM_COST": "ETF글로벌MVP60: 적립금의 0.00062%(연 0.227%), ETF글로벌MVP30: 적립금의 0.00064%(연 0.233%), 국내채권형: 적립금의 0.00032%(연 0.116%)",
		  "SA_FUND_INCM_TIME": "매일",
		  "ANNU_ACUM_GUR_COST": "없음",
		  "ANNU_ACUM_GUR_TIME": "매월(연금개시전)",
		  "CONT_MGNT_COST_OBJ": "계약관리비용",
		  "FUND_CHG_COST_COST": "없음",
		  "FUND_CHG_COST_TIME": "펀드변경시",
		  "CONT_MGNT_COST_COST": "추가납입보험료의 0.0%",
		  "CONT_MGNT_COST_TIME": "납입시"
		}
function bkCompare(){
	var key = Math.floor(Math.random()*11);
	bkCompareParam["RCD_KEY"] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	$.ajax({
		url:"http://localhost:80",
		data: bkCompareParam,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

	
}
function bkProduct(){
	var key = Math.floor(Math.random()*11);
	bkProductParam["RCD_KEY"] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	$.ajax({
		url:"http://localhost:80",
		data: bkProductParam,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

	
}
var eltParam =  {"OPR_NO":"20201207",
		"RCD_KEY":"2020120702070020220412154405",
		"OPR_NM":"금주대",
		"ADVPE_NO":"20201207",
		"ADVPE_NM":"금주대",
		"CSNO":"681293529",
		"CUS_NM":"홍조문",
		"BZ_BRCD":"020700",
		"BZBR_NM":"우리에프아이에스",
		"BIZ_DIS":"1",
		"SYS_DIS":"TOP",
		"FULL_65_TAX_ABV_YN":"N",
		"AGNPE_NM":"",
		"SSBLT_IVPE_YN":"N",
		"AGTP_YN":"Y",
		"CSINC_GD":"1",
		"CSINC_GD_NM":"공격투자형",
		"PRD_CD":"200009756",
		"PRD_NM":"메리츠증권 ELS 제 3578회(더블리자드형)",
		"PSN_YN":"Y",
		"PRD_RISK_GD":"1",
		"PRD_RISK_GD_NM":"매우높은위험",
		"REG_AM":"10,000,000",
		"CUST_TYPE":"1",
		"TR_AC_YN":"N",
		"AGTP_PRD_RCM_RSN":"1",
		"AGTP_PRD_RCM_ETC":""}
		
function eltProduct(){
	var code = $('.codeBox').val().trim();	
	var key = Math.floor(Math.random()*11);
	eltParam["RCD_KEY"] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	eltParam["PRD_CD"]  = code;
	$.ajax({
		url:"http://localhost:80",
		data: eltParam,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(res){
			console.log(res)
		}
	});

}

</script>

</html>