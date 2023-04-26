var key =0;
//
var dataStr = {
		"RCD_KEY":	0,//녹취키
		"OPR_NO" :1111111,	//조작자번호
		"OPR_NM":"조작자",//조작자명
		"ADVPE_NO":	2222222,//권유자번호
		"ADVPE_NM" :"권유자",//권유자명
		"CSNO" :	33333333,//고객번호
		"CUS_NM" :	"고객명",//고객명
		"BZ_BRCD" :"2566212",//영업점코드
		"BZBR_NM" :	"영업점",//영업점명
		"BIZ_DIS" :"2",//구분코드
		"PRD_NM":"녹취",//운용상품명
		"BAS_ASET_NM":"KOSPI200지수, S&P500지수, EuroStoxx50지수",//기초자산명
		"PRD_DIS":"ELF",//운용상품구분
		"SSBLT_IVPE_YN":"N",//부적합투자자
		"AGTP_YN":"Y",//공격투자자
		"AGTP_PRD_RCM_RSN":"1",//상품선택
		"FST_BAS_PR_DCS_DT":"20181228",//최초기준가격결정일
		"MN_PAY_YN":"N",//월지급식여부
		"MN_PAY_BAR":"000.00000",//월지급베리어
		"MN_PAY_PTRT":"000.00000",//월지급수익률
		"XPR_ERRP_RPY_DT":"201903262019042420190524201906262019122320200624202012232021062420211223",//만기및조기상환일
		"XPR_ERRP_RPY_BAR":"095.00000093.00000090.00000087.00000087.00000082.00000082.00000077.00000065.00000",//만기및조기상환베리어
		"XPR_ERRP_RPY_PTRT":"001.26000001.68000002.10000002.52000005.04000007.56000010.08000012.60000015.12000",//만기및조기상환수익률
		"XPR_BAR":"065.00000",//만기베리어
		"XPR_MINUS_BAR":"035.00000",//만기베리어 미충족 최소손실율
		"LZR_TP":"0",//리자드유형구분
		"LZR_SQC_CN":"0",//리자드차수
		"LZR_RPY_DT":"00000000",//리자드상환일
		"LZR_BAR":"000.00000",//리자드베리어
		"LZR_PTRT":"000.00000",//리자드수익률
		"FAST_LZR_YN":"N",//패스트리자드여부
		"FAST_LZR_MECN":"0",//패스트리자드개월수
		"KNOCK_IN_YN":"N",//낙인형여부
		"KNOCK_IN_BAR":"000.00000",//낙인베리어
		"XPR_PRFT_RT":"000.00000",//낙인수익률->만기수익률
		"TRTPY_TP":"1",//신탁보수구분
		"POCP_TRTPY_RT":"000.80000",//선취신탁보수율
		"DFAN_TRTPY_RT":"000.00000",//후취신탁보수율
		"PSN_YN":"Y",//개인여부
		"REG_CAN_XPR_DT":"20181227",//가입취소만료일
		"ADVPE_NM":"권유자",//권유자명
		"SYS_DIS":"DPT",//파생녹취 시스템구분
		"BIZ_DIS":"1",//업무구분
		"FULL_65_TAX_ABV_YN":"N",//만65세이상여부
		"AGNPE_NM":"없음",//대리인명(대리인이 없을 경우:없음
		"PRD_RISK_GD":"1",//상품위험등급(TOP/ISA - 1:매우높은위험,2:높은위험,3:다소높은위험,4:보통위험,5:낮은위험,6:매우낮은위험, (DPT/WMS - 0:해당없음,1:매우낮은위험,2:낮은위험,3:보통위험,4:다소높은위험,5:높은위험,6:매우높은위험
		"PRD_RISK_GD_NM":"매우낮은위험",//상품위험등급명
		"CSINC_GD":"1",//고객성향등급(TOP/ISA - 1:공격투자형,2:적극투자형,3:위험중립형,4:안정추구형,5:안정형, (DPT/WMS - 1:안정형,2:안정추구형,3:위험중립형,4:적극투자형,5:공격투자형
		"CSINC_GD_NM":"안정형",//고객성향등급명
		"AGTP_PRD_RCM_ETC":"",//공격투자자 기타사유
		"CUCD":"USD",//통화코드
		"HRPY_RQ_AVL_BZ_DCNT":"5",//중도상환신청가능영업일수(5
		"HRPY_AM_EVL_RT1":"095.00000",//중도상환금액평가비율1(095.00000
		"HRPY_AM_ESTM_MECN":"6",//중도상환금액산정개월수(6
		"HRPY_AM_EVL_RT2":"090.00000",//중도상환금액평가비율2(090.00000
		"REG_AM":"100000",//가입금액
		"LATT_APL_TM":"16시30분",//LATERADING적용시각
		"FNDBP_APL_DCNT_1":"5",//펀드기준가적용일수
		"BUY_RPRH_PAY_DCNT_1":"6",//매입환매지급일수
		"FNDBP_APL_DCNT_2":"6",//펀드기준가적용일수
		"BUY_RPRH_PAY_DCNT_2":"7",//매입환매지급일수
		"FND_HFRPH_FEE_TEM_MCN":"6",//펀드중도환매수수료기간월수
		"FNDR_FEE_RT_1":"007.00000",//펀드환매수수료율
		"FNDR_FEE_RT_2":"005.00000",//펀드환매수수료율
		"FND_POCP_FEE_RT":"001.00000",//펀드선취수수료율
		"TOT_PFE_RT":"000.24000"//총보수율
	}


 
function click(key) {
	alert('t');
		if(webSocket.readyState==1){
			alert('녹취를 종료 후 이용해 주시기 바랍니다.')
			return false;
		}
	 
		if('1'==key){
			delete dataStr.PRD_NM;
			dataStr.BIZ_DIS=6
		}else if('2'==key){
			delete dataStr.PRD_NM;
			dataStr.BIZ_DIS=6
		}else if('3'==key){
			dataStr.PRD_CD = '2021102713201'
			dataStr.PRD_NM = 'AB글로벌수익증권투자신탁(채권-재간접형)'
			dataStr.BIZ_DIS=2
		}else if('4'==key){
			dataStr.PRD_CD = '2021102713202'
			dataStr.PRD_NM = '미래에셋글로벌혁신기업ESG증권자투자신탁(주식)'
			dataStr.BIZ_DIS=2
		}else if('5'==key){
			dataStr.PRD_CD = '2021102713203'
			dataStr.PRD_NM = 'ARIRANG 고배당주'
			dataStr.BIZ_DIS=1
		}else if('6'==key){
			dataStr.PRD_CD = '2021102713204'
			dataStr.PRD_NM = 'HANARO e커머스'
			dataStr.BIZ_DIS=1
		}


	    var date = new Date();
	    dataStr['RCD_KEY'] = key + new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds();//callkey
	    
	    console.log(dataStr); 

	$.ajax({
			url:"http://localhost:80",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(res){
				console.log(res)
			}
		});

}

function rec(commnd){

	if('start'==commnd){
		
		webSocketConnect();

		
		if(key==null){
			key = '99'+new Date().format("yyyyMMddHH24mmss")+new Date().getMilliseconds()+'0000000000000000000'
			sendmsg('recStart', key)
			$(".rec").removeClass("displayHidden");
		}else{
			alert('현재 녹취가 진행 중입니다. 녹취를 먼저 종료해 주세요')
			return false;
		}
		
	}else {
		if(key==null){
			alert('진행중인 녹취가 없습니다. 녹취를 먼저 시작해 주세요')
			return false;
		}else{
			sendmsg('recStop', key)
			webSocket.close()
			$(".rec").addClass("displayHidden");
			key = null;
		}
	}
	

	
}