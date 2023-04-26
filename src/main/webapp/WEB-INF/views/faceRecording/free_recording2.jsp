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
    <link rel="stylesheet" type="text/css" href="${recseeResourcePath}/css/page/faceRecorder/sttTA/css/interview.css">
    
  
	<%-- js page --%>
<!-- 	단독 -->
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnectFreeRec2.js?version=20220510"></script> 
	<script type="text/javascript" src="${recseeResourcePath}/js/free_recording2.js?version=20220510"></script>
<!-- 	공용 -->
	<script type="text/javascript" src="${recseeResourcePath}/js/websocketConnectStt2.js?version=20220510"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/websocket2.js?version=20220510"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view_STT_TA_param2.js?version=20220510"></script>
	<script type="text/javascript" src="${recseeResourcePath}/js/page/faceRecording/face_recording_view_STT_TA2.js?version=20220510"></script>
	
		
	<script>
		var listenIp = "${listenIp}";
		var params = "${params}";
		var clientVersion = '${version}';
	</script>
	
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>자유 녹취</title>
    
</head>
<body style="min-width: 0px;background: rgb(247,249,254);">
    <!-- INTERVIEW HEADER -->
    <div class="interview-header">
        <span style="font-family:aGothic; font-weight:bold; font-size:18px;">상품 설명과정 도우미(TA)</span>
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
                        <a style="cursor:pointer;">state-icon</a>
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
							저는 상품판매 과정을 도와드리는 <span style="color:rgb(90,103,246);">TA(Text Analysis)</span>입니다.<br>
							상품판매시 필수설명내용에 대해 공부했어요!
                        </p>
                    </div>
                    <div style="display: flex; flex-direction: column; justify-content: flex-end;">
                    	<div style="font-family: 'aGothic'; width: 77px; margin: 0px 5px 0px 5px; color: rgb(62,126,219); font-weight: bold; font-size: 11px; line-height: 16px; cursor: pointer; border-bottom: 1px solid;" id="downPdf">
                    		스크립트 (다운로드)
                   		</div>
                    </div>
                </div>
                <div class="mesgTable">
                	<p>
                		<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num1.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
                		고객용 태블릿 PC에 스크립트를 띄워 드릴거에요! 화면이 나오는지 확인해주세요.
                    </p>
					<p style="padding:0px;">
                    	<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num2.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
						<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/start.png) center/cover no-repeat; width:111px; height:46px; display:block; margin-right:10px; border-radius:10px;"></span>
						 버튼을 누르세요.
					</p>
					<p>
					<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num3.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
						스크립트를 보시면서
						<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/necs.png) center/cover no-repeat; width: 111px; height: 30px; display: block; margin-right: 10px; margin-left: 10px;"></span>
						내용에 대해 고객님께 설명해주세요.
					</p>
					<p style="border: none; padding:3.5px 0px 3.5px 0px;">
					<span style="background: white url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/num4.png) center/cover no-repeat; width:30px; height:40px; display:block; margin-right:10px;"></span>
						설명이 다 끝나셨으면
					    <button class="btn-primary" style="pointer-events: none; margin-left: 10px; margin-right: 10px; min-width: 38px; max-height: 24px; font-size: 13px;">분석</button>
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

			<span class="fund">상품명</span>
            <span class="fund">위험등급</span>
            <span class="fund">투자대상자산</span>
            <span class="fund">운용사명</span>
            <span class="fund">비예금상품</span>
            <span class="fund">투자위험</span>
            <span class="fund">수수료 ㆍ 보수</span>
            <span class="fund">환매 ㆍ 중도해지 관련사항</span>
            <span class="fund">계약해제 ㆍ 해지에 관한 사항</span>
            <span class="fund">총회 ㆍ 공시 관련사항</span>
            <span class="fund">과세</span>
            <span class="fund">금소법 관련사항 등</span>
            <span class="etf" style="display: flex; height: 30px; justify-content: center; flex-direction: column; align-items: center;">ETF</span>
            <span class="etf">상품명, 위험등급, 운용자산명, 집합투자업자, 운용자산 목적, 신탁보수, 매매수수료, 매매방법,<br>주요투자위험(원본손실 위험, 상장거래에 따른 가격괴리 위험 등)</span>
            <span class="etf">채권형</span>
            <span class="etf">상품명, 위험등급, 신용등급, 신탁보수, 투자위험, 과세, 금소법 관련사항 등</span>
        </div>
    </section>
    <section style="display: flex;margin-top: 10px; padding-bottom:10px;flex-direction: row;    justify-content: flex-end; align-items: center; padding-right: 16px">
		<div style="display: flex; align-items: center;">
			<span
				style="font-family: NanumSquare; font-weight: bold; font-size: 14px;  color: rgb(104,74,212); height: auto;">
				※ 최소 4분 30초 이상 설명하신 후 버튼을 누르세요 .
			 </span>
			 <span style="display:inline-block; width:21px; height:20px; margin-right:5px; margin-left:5px; background: rgb(247,249,254) url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/hands.png) center/cover no-repeat;">
			 </span>
			<button class="btn-primary" id="checkInfo" style="pointer-events:none;">분석</button>
			<button class="btn-primary" id="checkInfo3" style="pointer-events:none; display:none;" onclick="mergeFileRe();">분석</button>
		   	<!-- <button class="btn-primary" id="checkInfo2" onclick="checkInfo()" style="display: none;margin-left: 3px;">분석 결과</button>
		   	<button class="btn-primary" id="freeRecExit" onclick="freeRecExit()" style="width:112px; background:rgb(44,51,59);display: none;margin-left: 3px;">상품설명 종료</button> -->
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
					<div></div>
				</div>
			</div>
			<div
				style="width: 500px; display: flex; flex-direction: column; align-items: center;">
				<div style="width: 430px; height: 597px; position: absolute; top: 116px; border-radius: 45px; background: rgb(247, 249, 254); display: flex; flex-direction: column; align-items: center; justify-content: space-evenly;">
					<div style="position: absolute;top:-53px; height: inherit; display: flex; flex-direction: column; flex-wrap: wrap; justify-content: space-evenly; align-items: center;">
					<div style="width: 370px; min-height: 120px; top: 85px; border-radius: unset; background: white; ">
						<div class="modal-header" style="background: linear-gradient(to right, rgb(129,106,217),rgb(49,183,213)); border-radius: unset; font-family: 'koDotum Bold'; font-weight: bold; font-size: 18px;">
							<span class="tit" style="font-family: 'aGothic'; font-weight: bold; font-size: 18px;">필수설명항목 분석 결과 <span style='font-family:aGothic; font-weight:bold; font-size:15px;'>(고객님께 설명하신 내용)</span></span>
						</div>
						<div id="necsUsed" style="text-align:center; display: flex; padding-top:10px; font-family: NanumSquare; font-weight: bold; font-size: 16px; flex-direction: row; justify-content: center; align-items: center; flex-wrap: wrap;">
						</div>
				</div>
				<div style="width: 370px; min-height: 130px; top: 237px; border-radius: unset; background: white; font-family:NanumSquare; font-weight:bold; font-size:16px;">
					<div class="modal-header" style="background: linear-gradient(to right, rgb(242,83,126),rgb(254,160,105)); border-radius: unset;">
						<span class="tit" style="font-family: 'aGothic'; font-weight: bold; font-size: 18px;">금칙어 분석 결과</span>
					</div>
					<div id="tabooUsed" style="text-align: center; display: flex; font-family: NanumSquare; font-weight: bold; font-size: 14px; flex-direction: row; align-content: center; align-items: center; justify-content: center; padding-top:10px; padding-bottom:10px;">
					</div>
				</div>
					<div style="width: 370px; min-height: 65px; top: 330px; background: rgb(247, 249, 254); display: flex; flex-direction: row; align-items: center;">
						<p style="background: url(${contextPath}/resources/common/recsee/css/page/faceRecorder/sttTA/img/bot3.gif) center/cover no-repeat; min-width: 62px; min-height: 62px"></p>
						<span style="font-family: 'NanumSquare';font-weight: bold;font-size: 12px;color: black;background-color: white;padding: 8px 10px; border-radius: 0px 10px 10px 10px; margin-left:6px;line-height: 18px;">
							주변 환경에 따라 제가 잘 이해하지 못한 부분이
							있을 수 있습니다.<br>  <span style="font-family: 'NanumSquare'; font-weight: bold; font-size: 14px;">필수설명항목</span> 에서 놓친
							부분이 있으시면 추가 설명 부탁드립니다!
						</span>
					</div>
					<div>
						<div style="width: 382px; min-height: 149px; top: 461px; padding-left:10px; background: rgb(44, 51, 59); border-radius: 20px 20px 20px 20px; display: flex; flex-wrap: wrap; flex-direction: row; align-content: center; justify-content: flex-start;">
							<div class="modal-header" style="background: inherit; border-radius: inherit; width: inherit; margin-top: -8px;">
								<div class="interview-in3" style="margin: 0px;">
									<span></span>
									<p>필수설명항목</p>
	   							</div>
							</div>
							<span class="fundPop">상품명</span>
				            <span class="fundPop">위험등급</span>
				            <span class="fundPop">투자대상자산</span>
				            <span class="fundPop">운용사명</span>
				            <span class="fundPop">비예금상품</span>
				            <span class="fundPop">투자위험</span>
				            <span class="fundPop">수수료 ㆍ 보수</span>
				            <span class="fundPop">환매 ㆍ 중도해지 관련사항</span>
				            <span class="fundPop">계약해제 ㆍ 해지에 관한 사항</span>
				            <span class="fundPop">총회 ㆍ 공시 관련사항</span>
				            <span class="fundPop">과세</span>
				            <span class="fundPop">금소법 관련사항 등</span>
							<span class="etfPop">ETF</span>
				            <span class="etfPop">상품명, 위험등급, 운용자산명, 집합투자업자, 운용자산 목적, 신탁보수, 매매수수료, 매매방법,<br>주요투자위험(원본손실 위험, 상장거래에 따른 가격괴리 위험 등)</span>
				            <span class="etfPop">채권형</span>
				            <span class="etfPop">상품명, 위험등급, 신용등급, 신탁보수, 투자위험, 과세, 금소법 관련사항 등</span>
						</div>
						 <!-- <span style="padding: 5px 0px 0px 180px;font-family: NanumSquareLight;display:block;font-weight: bold; font-size: 10px;color:rgb(44,51,59);height: auto;margin-right: 4px;">
						 	* 설명 후 <button class="btn-primary" style="pointer-events: none; margin-left: 1px; margin-right: 1px; min-width: 57px; max-height: 22px; font-size: 11px; background: rgb(62,126,219);">상품설명종료</button> 버튼을 눌러 다음 단계를 진행하세요.
	  			 		 </span> -->
		 		 	</div>
					 <div style="margin-bottom:-32px;">
					 	<p style="font-family: NanumSquareLight; font-weight: bold; font-size: 16px;color:black; text-align:center; padding:5px;">상품설명 분석이 끝났습니다.</p>
					 	<p style="font-family: NanumSquareLight; font-weight: bold; font-size: 16px;color:black; text-align:center; ">화면을 닫고 다음단계를 진행하시기 바랍니다.</p>
					 </div>
					 </div>
				</div>
				
			</div>
		</div>
	</div>
	<div class="modal" id="errorModalMic">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-size: 15px;">녹취기 전원 연결 상태를 확인해주세요.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">전원 케이블 재연결 시 10초 후 녹취 시작해주세요.</p>
				</article>
				<button class="btn-primary" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorModalMicExit();">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button>
			</div>
		</div>
	</div>
	<div class="modal" id="errorModalVolume">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-size: 15px;">PC 스피커 볼륨을 60 이상으로 변경해주세요.</p>
				</article>
				<button class="btn-primary" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorModalVolumeExit();">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button>
			</div>
		</div>
	</div>
	
	<div class="modal" id="errorModalTime">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-size: 15px;">4분 30초 이후 「분석」 버튼 클릭 가능합니다.</p>
				</article>
				<button class="btn-primary" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorModalTimeExit();">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button>
			</div>
		</div>
	</div>
	
	<div class="modal" id="errorModalRetry">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-size: 15px;">TA 분석 결과를 가져오지 못했습니다.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">새로고침 후 상품설명 시작 버튼을 눌러주세요.</p>
				</article>
<!-- 				<button class="btn-primary" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorInfoRetryExit();">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button> -->
			</div>
		</div>
	</div>
	
	<div class="modal" id="errorModalMerge">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-size: 15px;">TA 분석 결과 전산 등록에 실패했습니다.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">화면 우측하단의 「분석」버튼을 누르셔서</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">전산등록 완료하시기 바랍니다.</p>
				</article>
				<button class="btn-primary" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorInfoMergeExit();">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button>
			</div>
		</div>
	</div>
	
	<div class="modal" id="errorModalNet">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-size: 15px;">네트워크 연결에 문제가 발생했습니다.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">새로고침 후 상품설명 시작 버튼을 눌러주세요.</p>
				</article>
				<button class="btn-primary" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;"onclick="errorModalNetExit();">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px;">확</span>
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; margin-left: 5px;">인</span>
				</button>
			</div>
		</div>
	</div>
	
	<div class="modal" id="errorModalVersion">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-weight:bold; font-size: 15px;">대면녹취클라이언트를 최신 버전으로 설치해주세요.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 18px;">1. 아래 「다운로드」 버튼을 클릭하여 최신 버전 설치</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">2. 설치 후 현재 녹취창 닫기 → 녹취 재실행</p>
				</article>
				<button class="btn-primary" id="clientDown" style="background-color: #1760F0; margin-top: 15px; border-radius:unset !important; height:28px;">
					<span style="font-family: 'NanumSquare'; font-weight:normal !important; font-size: 15px; min-width: 69px;">다운로드</span>
				</button>
			</div>
		</div>
	</div>
	<!-- 녹취 시작전 & 창띄우고 웹소켓 끊긴팝업 -->
	<div class="modal" id="errorAICloseRecStartBefore">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-weight:bold; font-size: 15px;">현재 TA시스템 문제가 발생하여 녹취를 진행할 수 없습니다.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 18px;">1. 현재 녹취창 종료 닫기 -> 녹취 재실행</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">2. 새로고침(F5) -> 녹취 재실행</p>
				</article>
			</div>
		</div>
	</div>
	
	<!-- 녹취 중 웹소켓 끊긴팝업 -->
	<div class="modal" id="errorAICloseRecMiddle">
		<div class="modal-pop" style="width: max-content; height: auto; border-radius: unset !important;">
			<div class="modal-header" style="display: flex; flex-direction: row; justify-content: center; align-items: center; border-radius: unset !important;">
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic';">안</span>
				<span class="tit" style="font-weight: bold; font-size: 18px; font-family: 'aGothic'; margin-left:15px;">내</span>
			</div>
			<div class="modal-body" style="padding-top: 21px; display: flex; align-items: center; justify-content: space-evenly; flex-wrap: wrap; flex-direction: column;">
				<article style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
					<p style="font-family: 'NanumSquare'; font-weight:bold; font-size: 15px;">현재 TA시스템 문제가 발생하여 녹취를 진행할 수 없습니다.</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 18px;">1. 현재 녹취창 종료 닫기 -> 녹취 재실행</p>
					<p style="font-family: 'NanumSquare'; font-size: 15px; padding-top: 8px;">2. 새로고침(F5) -> 녹취 재실행</p>
				</article>
			</div>
		</div>
	</div>
	

	
</body>
</html>