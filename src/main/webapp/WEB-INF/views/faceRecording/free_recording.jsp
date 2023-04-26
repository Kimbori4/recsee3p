<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ page import="com.furence.recsee.common.model.EtcConfigInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html style="background:rgb(247,249,254);">
<head>
	<%@ include file="../common/include/commonVar.jsp" %>
	<%-- css page --%>
    
    <link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecorder/sttTA/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecorder/sttTA/css/main.css">
    <link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecorder/sttTA/css/interview2.css">
    
  
	<%-- js page --%>
<!-- 	단독 -->
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnectFreeRec.js?version=21212"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/free_recording.js?version=21212"></script>
<!-- 	공용 -->
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnectStt.js?version=21212"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/websocket.js?version=21212"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view_STT_TA_param.js?version=21212"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view_STT_TA.js?version=21212"></script>
	
		
	<script>
		var listenIp = "${listenIp}";
		var params = "${params}";
	</script>
	
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>자유 녹취</title>
    
</head>
<body style="min-width: 0px;background: rgb(247,249,254);">
    <!-- INTERVIEW HEADER -->
    <div class="interview-header">
        <span style="font-family:aGothic; font-weight:bold; font-size:18px;">상품 비교설명과정 도우미(TA)</span>
    </div>
    <!-- INTERVIEW BODY -->
    <section class="interview-body interview-inner">
        <div class="con-left" style="background:rgb(247,249,254);">
            
            <!-- 상단 플레이어 : rec-console -->
            <div class="rec-console">
                <div class="rec-console-top">
                    <div class="rec-btn" id ="freeRecStart" onclick="freeRecStart()">
                        <!-- rec-btn에 play 클래쓰 넣으면 : 녹취시작 -->
                        <!-- rec-btn에 play 클래쓰 빼면 : 일시정지 -->
                        <a href="#">state-icon</a>
                    </div>
                    <div class="play-time">
                        <span class="rec-time" id ="recTimer">00:00:00</span>
                        <span class="rec-recoding">본 과정은 녹취되지 않습니다.</span>
                    </div>
                </div>
            </div>

           <!--  <div class="user-info">
                <h2>상담 유의사항</h2>
                <ul>
                    <li>자동 리딩일 경우에도 고객 이하여부는 직원이 직접 확인 필수</li>
                    항목이 추가되는 경우 li에 넣어서 표현
                    <li>자동 리딩일 경우에도 고객 이하여부는 직원이 직접 확인 필수</li>
                </ul>
            </div> -->

        </div>
        <div class="con-right">
            <div class="script-mesg">
                <!-- 은행원 (상담원) -->
                <div class="script-banker">
                    <span class="profile">profile - banker</span>
                    <div class="mesgWrap" style="width:80% !important">
                        <p class="mesg">
							안녕하세요!<br>
							저는 상품판매 과정을 도와드리는 <span style="color:rgb(90,103,246);">TA(Text Analysis)</span>입니다.
                        </p>
                    </div>
                </div>
                <div class="mesgTable">
                	<p id='mesgFund'>
                		<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num1.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
                		<span style="font-family: NanumSquare; font-size: 14px; line-height: 18px; font-weight: bold;">WINI 상품검색창의 [상품비교목록]의 정보를 참고하여 <br>
                		고객님께 상품 비교설명 해주세요! <span style="color: rgb(243,86,126); font-family: NanumSquare; font-size: 16px; font-weight: bold; padding: 0px 4px 0px 4px;">추천이유는 2개 이상</span> 제시해주세요!
                		</span>
                    </p>
                    <p id='mesgEtf' style="font-family: NanumSquare; min-height: 45px; font-size: 12px; line-height: 14px; font-weight: bold;">
                		<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num1.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
                		<span style="font-family: NanumSquare; font-size: 14px; line-height: 18px; font-weight: bold;">고객님의 투자성향에 맞는 상품을 
                		<span style="color: rgb(243,86,126); font-family: NanumSquare; font-size: 16px; font-weight: bold; padding: 0px 4px 0px 4px;">업무포탈 신탁PLAZA</span>의 상품별<br>
                		운용자산설명서를 참고하여 비교설명 해주세요!</span>
                    </p>
					<p>
					<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num2.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
						설명이 다 끝나셨으면
					    <button class="btn-primary" style="pointer-events: none; margin-left: 10px; margin-right: 10px; min-width: 38px; max-height: 24px; font-size: 11px;">분석</button>
					       버튼을 눌러 결과를 확인해주세요!
					</p>
				</div>
                
            </div>
        </div>
    </section>


    <!-- INTERVIEW FOOT -->
    <section class="interview-foot ac">
        <!-- interview-foot 은 기본 display none (숨김) 입니다. 
            활성화시에 ac 클래스 넣어주시면 display 됩니다.-->
        <div class="interview-in" style="display:none;">
			<div>
				<span style="font-family: aGothic; font-weight:bold; font-size: 18px;">듣고 있습니다. 계속 설명해주세요.</span>
			</div>
			<div>
				<p style="margin-top:5px"></p>
			</div>
        </div>
		<div class="interview-in2">
			<span></span>
			<p>필수설명항목</p>
   		</div>
        <div class="interview-inner">

			<span class="fund">"펀드의 성과인 최근 1년 수익률은 A펀드 ~%이며, B펀드 ~%입니다.<br>
			동일 유형 펀드이지만 최근 1년 수익률은 B펀드가 높습니다."</span>
			<span class="fund">"펀드의 비용인 총 보수는 A펀드는 ~%이며, B펀드는 ~%입니다.<br>
			동일 유형의 펀드이지만 A펀드 대비 B펀드의 보수가 낮습니다."</span>
            <span class="etf">"고객님의 투자성향에 적합한 ETF상품으로 A ETF, B ETF 상품을 추천해 드리며,<br>
            A ETF 상품의 특징은 ~ 이고, B ETF 상품의 특징은 ~ 입니다."</span>
            <span class="etf">"고객님의 투자성향에 적합한 ELT상품으로 A ELT, B ELT 상품을 추천해 드리며,<br>
            A ELT의 기초자산은 ~이고 상환조건은 ~ 이며, 예상수익률은 ~%이고,<br>
            B ELT의 기초자산은 ~이고 상환조건은 ~ 이며, 예상수익률은 ~%입니다."</span>
        </div>
    </section>
    <section style="display: flex;margin-top: 10px; padding-bottom:10px;flex-direction: row;    justify-content: flex-end; align-items: center; padding-right: 16px">
		<div style="display: flex; align-items: center;">
			<button class="btn-primary" id="checkInfo" style="pointer-events:none;">분석</button>
		</div>
	</section>
	<div class="modal" id="freeRecModal"
		style="overflow: auto;">
		<div class="modal-pop" style="width: 500px; height: 735px; top: 5px; display: flex; flex-direction: column; position: relative;">
			<div class="modal-header" style="background: linear-gradient(to bottom,rgb(118,70,218),rgb(129,155,217)); height: 213px; display: flex; padding-top: 4px; flex-direction: row; align-items: flex-start;">
				<div style="margin-top: 10px; width: 100%; display: flex; flex-direction: row; justify-content: space-between;">
					<span class="tit" style="flex-grow: 1; font-family: 'aGothic'; font-weight: bold; font-size: 32px; padding-left: 15px; padding-top: 7px; ">
					TA분석 결과
					</span>
					<a class="modal-close" onclick="freeRecExit()" style="cursor: pointer; height: 32px; padding-top: 17px; margin-right:10px;"></a>
					<div></div>
				</div>
			</div>
			<div
				style="width: 500px; display: flex; flex-direction: column; align-items: center;">
				<div style="width: 430px; height: 465px; position: absolute; top: 116px; border-radius: 45px; background: rgb(247, 249, 254); display: flex; flex-direction: column; align-items: center; justify-content: space-evenly;">
					<div style="position: absolute;top:-10px; height: inherit; display: flex; flex-direction: column; flex-wrap: wrap; justify-content: space-evenly; align-items: center;">
					<div style="width: 370px; min-height: 165px; top: 237px; border-radius: unset; background: white; font-family:NanumSquare; font-weight:bold; font-size:16px;">
						<div class="modal-header" style="background: linear-gradient(to right, rgb(242,83,126),rgb(254,160,105)); border-radius: unset;">
							<span class="tit" style="font-family: 'aGothic'; font-weight: bold; font-size: 18px;">금칙어 분석 결과</span>
						</div>
						<div id="tabooUsed" style="text-align: center; display: flex; padding-top: 10px; font-family: NanumSquare; font-weight: bold; font-size: 14px; line-height: 20px; flex-direction: row; justify-content: center; flex-wrap: wrap; align-items: center;">
						</div>
					</div>
					<div style="width: 377px; min-height: 65px; top: 330px; background: rgb(247, 249, 254); display: flex; flex-direction: row; align-items: center;">
						<p style="background: url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/bot3.gif) center/cover no-repeat; margin-bottom: 48px; min-width: 57px; min-height: 57px"></p>
						<span style=" font-family: 'NanumSquare';font-weight: bold;font-size: 12px;color: black;background-color: white;padding: 8px 10px; border-radius: 0px 10px 10px 10px; margin-left:6px;line-height: 18px;">
							주변 환경에 따라 제가 잘 이해하지 못한 부분이
							있을 수 있습니다.<br>  <span style="font-family: 'NanumSquare'; font-weight: bold; font-size: 14px;">비교설명</span>이 끝나셨으면 신규 할 상품을 선택하여 신규를 진행해주세요.
						</span>
					</div>
					 <div>
					 	<p style="font-family: NanumSquareLight; font-weight: bold; font-size: 16px;color:black; text-align:center; padding:5px;">비교설명 분석이 끝났습니다.</p>
					 	<p style="font-family: NanumSquareLight; font-weight: bold; font-size: 16px;color:black; text-align:center; ">화면을 닫고 다음단계를 진행하시기 바랍니다.</p>
					 </div>
					 </div>
				</div>
				
			</div>
		</div>
	</div>
	<div class="modal" id="errorModal">
			<div class="modal-pop" style="width: 400px;">
				<div class="modal-header">
					<span class="tit">에러 안내</span>
				</div>
				<div class="modal-body" style="min-height: 372px; width:auto; overflow:auto;/* background: #F5F5F5; */">
					<article style="margin-top: 0px;">
						<p>에러가 발생했습니다. 창을 종료 후 WINI에서 재시작 해주세요.</p>
					</article>
				</div>
			</div>
		</div>  
	
</body>
</html>