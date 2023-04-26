<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script>
// 언어팩 정의
window.lang = (function() {
	var lang = {};

	lang.fn = {};

	// 언어팩 정의 함수
	// .으로 구분된 코드를 json 형태로 변환해준다. 없으면 객체 자동 생성
	lang.fn.set = function(code, value) {
		var rv;
		var arr = code.split(".");
		var len = arr.length;
		var res = false;
		for(var i=0, cn;cn=arr[i];i++) {
			rv = (i==0?lang:rv)[cn];
			if(i == len - 1) {
				var evalText = "lang." + code;
				if(value.indexOf("'")){
					value=value.replace("'","\\'")
				}
				eval(evalText + " = '" + value + "';");
				res = value;
			} else if(rv === undefined) {
				var target = "";
				for(var j=0; j<=i; j++) {
					target += '["' + arr[j] + '"]';
				}
				eval("lang" + target + " = {};");
				rv = eval("lang" + target + ";");
			}
		}
		return res;
	}
	// 언어팩의 값을 가져오는 함수
	// lang.get(code)의 형태로 사용가능
	lang.fn.get = function(code) {
		var rv;
		var arr = code.split(".");
		var len = arr.length;
		for(var i=0, cn;cn=arr[i];i++) {
			rv = (i==0?lang:rv)[cn];
			if(rv === undefined)
				break;
		}
		return rv||code;
	}
	// 언어팩의 값을 검색하는 함수
	lang.fn.searchByText = function(text) {
		var regExp = new RegExp(text, "g", "i");
		var result = {};

		search(lang, "");
		return result;

		function search(obj, currentCode) {
			$.each(obj, function(c, o) {
				var fullCode = (currentCode?currentCode + ".":"") + c;
				if(o.toString().toLowerCase() !== "[object Object]".toLowerCase()) {
					try {
						if(regExp.test(o)) {
							result[fullCode] = o;
						}
					} catch(e){}
				} else {
					search(o, fullCode);
				}
			});
		}
	}

	lang.fn.set("evaluation.js.alert.msg107", 	 "<spring:message code='evaluation.js.alert.msg107'/>");
	lang.fn.set("evaluation.js.alert.msg108",	 "<spring:message code='evaluation.js.alert.msg108'/>");
	lang.fn.set("evaluation.js.alert.msg109",	 "<spring:message code='evaluation.js.alert.msg109'/>");
	lang.fn.set("evaluation.js.alert.msg110",	 "<spring:message code='evaluation.js.alert.msg110'/>");
	lang.fn.set("evaluation.js.alert.msg111",	 "<spring:message code='evaluation.js.alert.msg111'/>");
	lang.fn.set("evaluation.js.alert.msg112",	 "<spring:message code='evaluation.js.alert.msg112'/>");
	lang.fn.set("evaluation.js.alert.msg113",	 "<spring:message code='evaluation.js.alert.msg113'/>");
	lang.fn.set("evaluation.js.alert.msg114",	 "<spring:message code='evaluation.js.alert.msg114'/>");
	lang.fn.set("evaluation.js.alert.msg115",	 "<spring:message code='evaluation.js.alert.msg115'/>");
	lang.fn.set("evaluation.js.alert.msg116",	 "<spring:message code='evaluation.js.alert.msg116'/>");
	lang.fn.set("evaluation.js.alert.msg117",	 "<spring:message code='evaluation.js.alert.msg117'/>");
	lang.fn.set("evaluation.js.alert.msg118",	 "<spring:message code='evaluation.js.alert.msg118'/>");
	lang.fn.set("evaluation.js.alert.msg119",	 "<spring:message code='evaluation.js.alert.msg119'/>");
	lang.fn.set("evaluation.js.alert.msg120",	 "<spring:message code='evaluation.js.alert.msg120'/>");
	lang.fn.set("evaluation.js.alert.msg121",	 "<spring:message code='evaluation.js.alert.msg121'/>");
	lang.fn.set("evaluation.js.alert.msg122",	 "<spring:message code='evaluation.js.alert.msg122'/>");
	lang.fn.set("evaluation.js.alert.msg123",	 "<spring:message code='evaluation.js.alert.msg123'/>");
	lang.fn.set("evaluation.js.alert.msg124",	 "<spring:message code='evaluation.js.alert.msg124'/>");
	lang.fn.set("evaluation.js.alert.msg125",	 "<spring:message code='evaluation.js.alert.msg125'/>");
	lang.fn.set("evaluation.js.alert.msg126",	 "<spring:message code='evaluation.js.alert.msg126'/>");
	lang.fn.set("evaluation.js.alert.msg127",	 "<spring:message code='evaluation.js.alert.msg127'/>");
	lang.fn.set("evaluation.js.alert.msg128",	 "<spring:message code='evaluation.js.alert.msg128'/>");
	lang.fn.set("evaluation.js.alert.msg129",	 "<spring:message code='evaluation.js.alert.msg129'/>");
	lang.fn.set("evaluation.js.alert.msg130",	 "<spring:message code='evaluation.js.alert.msg130'/>");
	lang.fn.set("evaluation.js.alert.msg131",	 "<spring:message code='evaluation.js.alert.msg131'/>");
	
	
	lang.fn.set("evaluation.js.alert.msg106", "<spring:message code='evaluation.js.alert.msg106'/>");
	lang.fn.set("evaluation.js.alert.msg105", "<spring:message code='evaluation.js.alert.msg105'/>");
	lang.fn.set("evaluation.sheet.label.revaluation", "<spring:message code='evaluation.sheet.label.revaluation'/>");
	lang.fn.set("evaluation.sheet.btn.revaluation", "<spring:message code='evaluation.sheet.btn.revaluation'/>");
	lang.fn.set("admin.alert.selectDeleteServer", "<spring:message code='admin.alert.selectDeleteServer'/>");
	lang.fn.set("admin.alert.failDelete", "<spring:message code='admin.alert.failDelete'/>");
	lang.fn.set("admin.alert.successDelete", "<spring:message code='admin.alert.successDelete'/>");
	lang.fn.set("admin.menu.systemOption", "<spring:message code='admin.menu.systemOption'/>"); <%-- 시스템 설정 --%>
	lang.fn.set("admin.menu.systemManage", "<spring:message code='admin.menu.systemManage'/>"); <%-- 시스템 관리 --%>
	lang.fn.set("admin.menu.userManage", "<spring:message code='admin.menu.userManage'/>"); <%-- 어플리케이션 사용자 관리 --%>
	lang.fn.set("admin.menu.backupProcess", "<spring:message code='admin.menu.backupProcess'/>"); <%-- 백업 프로세스 --%>
	lang.fn.set("admin.menu.schedulerManage", "<spring:message code='admin.menu.schedulerManage'/>"); <%-- 스케줄러 관리 --%>
	lang.fn.set("admin.menu.systemMonitoring", "<spring:message code='admin.menu.systemMonitoring'/>");<%-- 시스템모니터링 관리--%>
	lang.fn.set("admin.menu.li.systemOption.channel", "<spring:message code='admin.menu.li.systemOption.channel'/>"); <%-- 채널 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.server", "<spring:message code='admin.menu.li.systemOption.server'/>"); <%-- 서버 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.switchboard", "<spring:message code='admin.menu.li.systemOption.switchboard'/>"); <%-- 교환기 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.subNumber", "<spring:message code='admin.menu.li.systemOption.subNumber'/>"); <%-- 자번호 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.phoneMapping", "<spring:message code='admin.menu.li.systemOption.phoneMapping'/>"); <%-- 전화번호 맵핑 --%>
	lang.fn.set("admin.menu.li.systemOption.publicIp", "<spring:message code='admin.menu.li.systemOption.publicIp'/>"); <%-- 공인IP 관리 --%>
	lang.fn.set("admin.menu.li.systemManage.group", "<spring:message code='admin.menu.li.systemManage.group'/>"); <%-- 그룹 관리 --%>
	lang.fn.set("admin.menu.li.systemManage.details", "<spring:message code='admin.menu.li.systemManage.details'/>"); <%-- 상세 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.channelMonitoring", "<spring:message code='admin.menu.li.systemOption.channelMonitoring'/>"); <%-- 그룹 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.logoSetting", "<spring:message code='admin.menu.li.systemOption.logoSetting'/>"); <%-- 로고 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.packetSetting", "<spring:message code='admin.menu.li.systemOption.packetSetting'/>"); <%-- 로고 관리 --%>
	lang.fn.set("admin.menu.li.systemOption.sttServer", "<spring:message code='admin.menu.li.systemOption.sttServer'/>"); <%-- STT서버관리 --%>
		
	lang.fn.set("admin.menu.li.systemManage.log", "<spring:message code='admin.menu.li.systemManage.log'/>"); <%-- 로그 관리 --%>
	lang.fn.set("admin.menu.li.systemManage.logView", "<spring:message code='admin.menu.li.systemManage.logView'/>"); <%-- 로그 뷰 --%>
	lang.fn.set("admin.menu.li.systemManage.queue", "<spring:message code='admin.menu.li.systemManage.queue'/>"); <%-- 큐 관리 --%>
	lang.fn.set("admin.menu.li.systemManage.backupSearch", "<spring:message code='admin.menu.li.systemManage.backupSearch'/>"); <%-- 백업관리 --%>
	lang.fn.set("admin.menu.li.systemManage.userInterface", "<spring:message code='admin.menu.li.systemManage.userInterface'/>"); <%-- 사용자 인터페이스 --%>
	lang.fn.set("admin.menu.li.systemManage.packetLogManage", "<spring:message code='admin.menu.li.systemManage.packetLogManage'/>"); <%-- 패킷 에러 로그 --%>
	lang.fn.set("admin.menu.li.systemManage.fileRecoverManage", "<spring:message code='admin.menu.li.systemManage.fileRecoverManage'/>"); <%-- 3차 백업 관리 --%>

	lang.fn.set("admin.menu.li.schedulerManage.deleteRecfile", "<spring:message code='admin.menu.li.schedulerManage.deleteRecfile'/>"); <%-- 녹취 삭제 관리 --%>
	lang.fn.set("admin.menu.li.schedulerManage.backupRecfile", "<spring:message code='admin.menu.li.schedulerManage.backupRecfile'/>"); <%-- 녹취 삭제 관리 --%>
	lang.fn.set("admin.menu.li.schedulerManage.dbManage", "<spring:message code='admin.menu.li.schedulerManage.dbManage'/>"); <%-- DBSync 관리 --%>
	lang.fn.set("admin.menu.li.schedulerManage.userDBInterface", "<spring:message code='admin.menu.li.schedulerManage.userDBInterface'/>"); <%-- 사용자DB 인터페이스 --%>
	lang.fn.set("admin.menu.li.userManage.userManage", "<spring:message code='admin.menu.li.userManage.userManage'/>"); <%-- 사용자 관리 --%>
	lang.fn.set("admin.menu.li.userManage.userManageRec", "<spring:message code='admin.menu.li.userManage.userManageRec'/>"); <%-- 사용자 관리 --%>
	lang.fn.set("admin.menu.li.userManage.authyManage", "<spring:message code='admin.menu.li.userManage.authyManage'/>"); <%-- 권한관리 --%>
	lang.fn.set("admin.menu.li.userManage.allowableRangeManage", "<spring:message code='admin.menu.li.userManage.allowableRangeManage'/>"); <%-- 허용 범위 관리 --%>
	lang.fn.set("admin.menu.li.userManage.usageScreen", "<spring:message code='admin.menu.li.userManage.usageScreen'/>"); <%-- 스크린 사용자 관리 --%>
	lang.fn.set("admin.menu.li.userManage.userDefinition", "<spring:message code='admin.menu.li.userManage.userDefinition'/>"); <%-- 조회 및 청취 사용자 정의 --%>
	lang.fn.set("admin.menu.li.userManage.approveList", "<spring:message code='admin.menu.li.userManage.approveList'/>"); <%-- 다운로드 요청 관리 --%>
	lang.fn.set("admin.menu.li.userManage.comboList", "<spring:message code='admin.menu.li.userManage.comboList'/>"); <%--콤보 리스트 관리 --%>
	lang.fn.set("admin.menu.li.backupProcess.rec", "<spring:message code='admin.menu.li.backupProcess.rec'/>"); <%-- 녹취파일 백업 --%>
	lang.fn.set("admin.menu.li.backupProcess.ftp", "<spring:message code='admin.menu.li.backupProcess.ftp'/>"); <%-- FTP 백업 --%>
	lang.fn.set("admin.menu.li.systemMonitoring.targetSetting", "<spring:message code='admin.menu.li.systemMonitoring.targetSetting'/>"); <%-- 관제 대상  관리 --%>
	lang.fn.set("admin.menu.li.systemMonitoring.targetItemSetting", "<spring:message code='admin.menu.li.systemMonitoring.targetItemSetting'/>"); <%-- 관제 대상  관리 --%>
	lang.fn.set("admin.menu.li.systemMonitoring.attrSetting", "<spring:message code='admin.menu.li.systemMonitoring.attrSetting'/>"); <%-- 관제 항목  관리 --%>
	lang.fn.set("admin.menu.li.systemMonitoring.UISetting", "<spring:message code='admin.menu.li.systemMonitoring.UISetting'/>"); <%-- 관제 대상  관리 --%>
	lang.fn.set("admin.menu.li.systemMonitoring.alertList", "<spring:message code='admin.menu.li.systemMonitoring.alertList'/>"); <%-- 알림 이력  관리 --%>
	lang.fn.set("admin.menu.placeholder.group", "<spring:message code='admin.menu.placeholder.group'/>"); <%-- 권한명을 입력해 주세요. --%>
	lang.fn.set("admin.channel.title.createChannel", "<spring:message code='admin.channel.title.createChannel'/>"); <%-- 채널 추가 --%>
	lang.fn.set("admin.channel.title.autoCreateChannel", "<spring:message code='admin.channel.title.autoCreateChannel'/>"); <%-- 자동 채널 추가 --%>
	lang.fn.set("admin.channel.label.channelNo", "<spring:message code='admin.channel.label.channelNo'/>"); <%-- 채널 번호 --%>
	lang.fn.set("admin.channel.label.system", "<spring:message code='admin.channel.label.system'/>"); <%-- 시스템 --%>
	lang.fn.set("admin.channel.label.ext", "<spring:message code='admin.channel.label.ext'/>"); <%-- 내선 번호 --%>
	lang.fn.set("admin.channel.label.ip", "<spring:message code='admin.channel.label.ip'/>"); <%-- IP --%>
	lang.fn.set("admin.channel.label.usageRec", "<spring:message code='admin.channel.label.usageRec'/>"); <%-- 녹취 사용 --%>
	lang.fn.set("admin.channel.label.recKind", "<spring:message code='admin.channel.label.recKind'/>"); <%-- 녹취 종류 --%>
	lang.fn.set("admin.channel.label.recType", "<spring:message code='admin.channel.label.recType'/>"); <%-- 녹취 유형 --%>
	lang.fn.set("admin.channel.label.removeChannel", "<spring:message code='admin.channel.label.removeChannel'/>"); <%-- 기존 채널 삭제 --%>
	lang.fn.set("admin.channel.placeholder.ip", "<spring:message code='admin.channel.placeholder.ip'/>"); <%-- IP --%>
	lang.fn.set("admin.channel.title.multiChannelAdd",	"<spring:message code='admin.channel.title.multiChannelAdd'/>");
	lang.fn.set("admin.subNumber.title.addSubNumber", "<spring:message code='admin.subNumber.title.addSubNumber'/>"); <%-- 자번호 추가 --%>
	lang.fn.set("admin.subNumber.title.modifySubNumber", "<spring:message code='admin.subNumber.title.modifySubNumber'/>"); <%-- 자번호 수정 --%>
	lang.fn.set("admin.subNumber.label.telNo", "<spring:message code='admin.subNumber.label.telNo'/>"); <%-- 전화번호--%>
	lang.fn.set("admin.subNumber.label.nickName", "<spring:message code='admin.subNumber.label.nickName'/>"); <%-- 대체문자 --%>
	lang.fn.set("admin.subNumber.label.use", "<spring:message code='admin.subNumber.label.use'/>"); <%-- 사용구분 --%>
	lang.fn.set("admin.subNumber.label.selY", "<spring:message code='admin.subNumber.label.selY'/>"); <%-- 사용 --%>
	lang.fn.set("admin.subNumber.label.selN", "<spring:message code='admin.subNumber.label.selN'/>"); <%-- 미사용 --%>
	lang.fn.set("admin.phoneMapping.title.addPhoneMapping", "<spring:message code='admin.phoneMapping.title.addPhoneMapping'/>"); <%-- 전화번호 추가 --%>
	lang.fn.set("admin.phoneMapping.title.modifyPhoneMapping", "<spring:message code='admin.phoneMapping.title.modifyPhoneMapping'/>"); <%-- 전화번호 수정 --%>
	lang.fn.set("admin.phoneMapping.label.custPhone", "<spring:message code='admin.phoneMapping.label.custPhone'/>"); <%-- 전화번호--%>
	lang.fn.set("admin.phoneMapping.label.custNickName", "<spring:message code='admin.phoneMapping.label.custNickName'/>"); <%-- 대체문자 --%>
	lang.fn.set("admin.phoneMapping.label.useNickName", "<spring:message code='admin.phoneMapping.label.useNickName'/>"); <%-- 사용구분 --%>
	lang.fn.set("admin.phoneMapping.label.selY", "<spring:message code='admin.phoneMapping.label.selY'/>"); <%-- 사용 --%>
	lang.fn.set("admin.phoneMapping.label.selN", "<spring:message code='admin.phoneMapping.label.selN'/>"); <%-- 미사용 --%>
	lang.fn.set("admin.detail.title.recordingEngine", "<spring:message code='admin.detail.title.recordingEngine'/>"); <%-- 녹취 엔진 --%>
	lang.fn.set("admin.detail.title.etcSetting", "<spring:message code='admin.detail.title.etcSetting'/>"); <%-- 기타 설정 --%>
	lang.fn.set("admin.detail.title.uploadRecordingFiles", "<spring:message code='admin.detail.title.uploadRecordingFiles'/>"); <%-- 음성 파일 업로드 --%>
	lang.fn.set("admin.detail.title.uploadUpdateFile", "<spring:message code='admin.detail.title.uploadUpdateFile'/>"); <%-- 업데이트 파일 업로드 --%>
	lang.fn.set("admin.detail.title.mailServerData", "<spring:message code='admin.detail.title.mailServerData'/>"); <%-- 메일 서버 정보 --%>
	lang.fn.set("admin.detail.title.serverTimezoneManage", "<spring:message code='admin.detail.title.serverTimezoneManage'/>"); <%-- 서버 타임존 관리 --%>
	lang.fn.set("admin.detail.label.ipktsAliveCheck", "<spring:message code='admin.detail.label.ipktsAliveCheck'/>"); <%-- IPKTS Alive check --%>
	lang.fn.set("admin.detail.label.sipLog", "<spring:message code='admin.detail.label.sipLog'/>"); <%-- SIP Log --%>
	lang.fn.set("admin.detail.label.lanCard", "<spring:message code='admin.detail.label.lanCard'/>"); <%-- Lan Card --%>
	lang.fn.set("admin.detail.label.encodedFileDonwload", "<spring:message code='admin.detail.label.encodedFileDonwload'/>"); <%-- 암호화된 파일 다운로드 --%>
	lang.fn.set("admin.detail.label.agentIdView", "<spring:message code='admin.detail.label.agentIdView'/>"); <%-- 에이전트 ID 출력 옵션 --%>
	lang.fn.set("admin.detail.label.haName", "<spring:message code='admin.detail.label.haName'/>"); <%-- HA Name --%>
	lang.fn.set("admin.detail.label.fileExtension", "<spring:message code='admin.detail.label.fileExtension'/>"); <%-- 다운로드 파일 확장자 --%>
	lang.fn.set("admin.detail.label.guideSpeechRecording", "<spring:message code='admin.detail.label.guideSpeechRecording'/>"); <%-- 안내 멘트 녹취 --%>
	lang.fn.set("admin.detail.label.filenamePattern", "<spring:message code='admin.detail.label.filenamePattern'/>"); <%-- 파일명 패턴 --%>
	lang.fn.set("admin.detail.label.uploadFile", "<spring:message code='admin.detail.label.uploadFile'/>"); <%-- 음성 파일 --%>
	lang.fn.set("admin.detail.label.upload", "<spring:message code='admin.detail.label.upload'/>"); <%-- 업로드 --%>
	lang.fn.set("admin.detail.label.memo", "<spring:message code='admin.detail.label.memo'/>"); <%-- 메모 --%>
	lang.fn.set("admin.detail.label.updateFile", "<spring:message code='admin.detail.label.updateFile'/>"); <%-- 업데이트 파일 --%>
	lang.fn.set("admin.detail.label.maxFileSize", "<spring:message code='admin.detail.label.maxFileSize'/>"); <%-- 파일 최대 크기 --%>
	lang.fn.set("admin.detail.label.mailServerAddress", "<spring:message code='admin.detail.label.mailServerAddress'/>"); <%-- 메일 서버 주소 --%>
	lang.fn.set("admin.detail.label.mailSendPort", "<spring:message code='admin.detail.label.mailSendPort'/>"); <%-- 메일 발송 포트 --%>
	lang.fn.set("admin.detail.label.mailSendId", "<spring:message code='admin.detail.label.mailSendId'/>"); <%-- 메일 발송 ID --%>
	lang.fn.set("admin.detail.label.mailSendPw", "<spring:message code='admin.detail.label.mailSendPw'/>"); <%-- 메일 발송 비밀번호 --%>
	lang.fn.set("admin.detail.label.mailSenderAddress", "<spring:message code='admin.detail.label.mailSenderAddress'/>"); <%-- 발신자 메일 주소 --%>
	lang.fn.set("admin.detail.label.mailReceiverAddress", "<spring:message code='admin.detail.label.mailReceiverAddress'/>"); <%-- 수신자 메일 주소 --%>
	lang.fn.set("admin.detail.label.nowTimezone", "<spring:message code='admin.detail.label.nowTimezone'/>"); <%-- 현재 타임 존 --%>
	lang.fn.set("admin.detail.label.changeServerTimezone", "<spring:message code='admin.detail.label.changeServerTimezone'/>"); <%-- 서버 타임존 / 시간 변경 --%>
	lang.fn.set("admin.detail.option.use", "<spring:message code='admin.detail.option.use'/>"); <%-- 사용 --%>
	lang.fn.set("admin.detail.option.notUse", "<spring:message code='admin.detail.option.notUse'/>"); <%-- 미사용 --%>
	lang.fn.set("admin.detail.option.choose", "<spring:message code='admin.detail.option.choose'/>"); <%-- 선택 --%>
	lang.fn.set("admin.detail.option.mp3", "<spring:message code='admin.detail.option.mp3'/>"); <%-- mp3 --%>
	lang.fn.set("admin.detail.option.wav", "<spring:message code='admin.detail.option.wav'/>"); <%-- wav --%>
	lang.fn.set("admin.detail.option.cid", "<spring:message code='admin.detail.option.cid'/>"); <%-- CID --%>
	lang.fn.set("admin.detail.option.callType", "<spring:message code='admin.detail.option.callType'/>"); <%-- 콜타입 --%>
	lang.fn.set("admin.detail.placeholder.selectFile", "<spring:message code='admin.detail.placeholder.selectFile'/>"); <%-- 파일을 선택하세요. --%>
	lang.fn.set("admin.group.add", "<spring:message code='admin.group.add'/>"); <%-- 권한 그룹 추가 --%>
	lang.fn.set("admin.button.addGroup", "<spring:message code='admin.button.addGroup'/>"); <%-- 권한 그룹 추가 --%>
	lang.fn.set("admin.button.modify", "<spring:message code='admin.button.modify'/>"); <%-- 변경 사항 적용 --%>
	lang.fn.set("admin.button.autoCreateChannel", "<spring:message code='admin.button.autoCreateChannel'/>"); <%-- 채널 자동 생성 --%>
	lang.fn.set("admin.button.createChannel", "<spring:message code='admin.button.createChannel'/>"); <%-- 채널 추가 --%>
	lang.fn.set("admin.button.create", "<spring:message code='admin.button.create'/>"); <%-- 생성 --%>
	lang.fn.set("admin.button.addSubNumber", "<spring:message code='admin.button.addSubNumber'/>"); <%-- 자번호 추가 --%>
	lang.fn.set("admin.button.modifySubNumber", "<spring:message code='admin.button.modifySubNumber'/>"); <%-- 자번호 수정 --%>
	lang.fn.set("admin.button.addPhoneMapping", "<spring:message code='admin.button.addPhoneMapping'/>"); <%-- 전화번호 추가 --%>
	lang.fn.set("admin.button.modifyPhoneMapping", "<spring:message code='admin.button.modifyPhoneMapping'/>"); <%-- 전화번호 수정 --%>
	lang.fn.set("admin.button.restart", "<spring:message code='admin.button.restart'/>"); <%-- 녹취 엔진 재시작 --%>
	lang.fn.set("admin.button.save", "<spring:message code='admin.button.save'/>"); <%-- 저장 --%>
	lang.fn.set("admin.button.sendTestMail", "<spring:message code='admin.button.sendTestMail'/>"); <%-- 테스트 메일 발송 --%>
	lang.fn.set("admin.alert.restartMessage", "<spring:message code='admin.alert.restartMessage'/>"); <%-- *녹취 엔진을 재시작하는 동안 녹취가 안 될 수 있습니다. --%>
	lang.fn.set("admin.alert.fileUploadMessage", "<spring:message code='admin.alert.fileUploadMessage'/>"); <%-- * 파일 업로드 후 서버를 재부팅해야 업데이트가 완료됩니다. --%>
	
	// 공인IP 관리 페이지
	lang.fn.set("admin.button.addIp", 					"<spring:message code='admin.button.addIp'/>"); <%-- 등록 --%>
	lang.fn.set("admin.button.modifyIp", 				"<spring:message code='admin.button.modifyIp'/>"); <%-- 수정 --%>
	lang.fn.set("admin.publicIp.title.addIp", 			"<spring:message code='admin.publicIp.title.addIp'/>"); <%-- IP 등록 --%>
	lang.fn.set("admin.publicIp.title.modifyIp", 		"<spring:message code='admin.publicIp.title.modifyIp'/>"); <%-- IP 수정 --%>
	lang.fn.set("admin.publicIp.label.rPublicIp", 		"<spring:message code='admin.publicIp.label.rPublicIp'/>"); <%-- IP --%>
	lang.fn.set("admin.alert.publicIpManage1", 			"<spring:message code='admin.alert.publicIpManage1'/>"); <%-- IP 정보를 입력하세요. --%>
	lang.fn.set("admin.alert.publicIpManage2", 			"<spring:message code='admin.alert.publicIpManage2'/>"); <%-- 이미 등록된 IP 입니다. --%>
	lang.fn.set("admin.alert.publicIpManage3", 			"<spring:message code='admin.alert.publicIpManage3'/>"); <%-- 등록이 완료 되었습니다. --%>
	lang.fn.set("admin.alert.publicIpManage4", 			"<spring:message code='admin.alert.publicIpManage4'/>"); <%-- 등록에 실패했습니다. --%>
	lang.fn.set("admin.alert.publicIpManage5", 			"<spring:message code='admin.alert.publicIpManage5'/>"); <%-- 수정이 완료 되었습니다. --%>
	lang.fn.set("admin.alert.publicIpManage6", 			"<spring:message code='admin.alert.publicIpManage6'/>"); <%-- 수정에 실패했습니다. --%>
	lang.fn.set("admin.alert.publicIpManage7", 			"<spring:message code='admin.alert.publicIpManage7'/>"); <%-- 선택한 IP 정보를 삭제하시겠습니까? --%>
	lang.fn.set("admin.alert.publicIpManage8", 			"<spring:message code='admin.alert.publicIpManage8'/>"); <%-- 삭제가 완료 되었습니다. --%>
	lang.fn.set("admin.alert.publicIpManage9", 			"<spring:message code='admin.alert.publicIpManage9'/>"); <%-- 삭제에 실패했습니다. --%>
	lang.fn.set("admin.alert.publicIpManage10", 		"<spring:message code='admin.alert.publicIpManage10'/>"); <%-- 삭제할 IP를 선택해주세요. --%>
	
	lang.fn.set("admin.dbManage.label.dbName", "<spring:message code='admin.dbManage.label.dbName'/>"); <%-- DB명 --%>
	lang.fn.set("admin.dbManage.label.dbServer", "<spring:message code='admin.dbManage.label.dbServer'/>"); <%-- 서버 --%>
	lang.fn.set("admin.dbManage.label.url", "<spring:message code='admin.dbManage.label.url'/>"); <%-- URL --%>
	lang.fn.set("admin.dbManage.label.id", "<spring:message code='admin.dbManage.label.id'/>"); <%-- ID --%>
	lang.fn.set("admin.dbManage.label.pw", "<spring:message code='admin.dbManage.label.pw'/>"); <%-- PW --%>
	lang.fn.set("admin.dbManage.label.timeout", "<spring:message code='admin.dbManage.label.timeout'/>"); <%-- Timeout --%>
	lang.fn.set("admin.dbManage.label.dbConnectionTest", "<spring:message code='admin.dbManage.label.dbConnectionTest'/>"); <%-- Connection Test --%>
	lang.fn.set("admin.dbManage.label.sqlName", "<spring:message code='admin.dbManage.label.sqlName'/>"); <%-- SQL명 --%>
	lang.fn.set("admin.dbManage.label.sql", "<spring:message code='admin.dbManage.label.sql'/>"); <%-- SQL --%>
	lang.fn.set("admin.dbManage.label.jobName", "<spring:message code='admin.dbManage.label.jobName'/>"); <%-- 작업명 --%>
	lang.fn.set("admin.dbManage.label.executeName", "<spring:message code='admin.dbManage.label.executeName'/>"); <%-- 스케줄명 --%>
	lang.fn.set("admin.dbManage.label.rSchedulerSelect", "<spring:message code='admin.dbManage.label.rSchedulerSelect'/>"); <%-- 일정 --%>
	lang.fn.set("admin.dbManage.label.rSchedulerWeek", "<spring:message code='admin.dbManage.label.rSchedulerWeek'/>"); <%-- 주 --%>
	lang.fn.set("admin.dbManage.label.rSchedulerDay", "<spring:message code='admin.dbManage.label.rSchedulerDay'/>"); <%-- 일 --%>
	lang.fn.set("admin.dbManage.label.rSchedulerHour", "<spring:message code='admin.dbManage.label.rSchedulerHour'/>"); <%-- 시 --%>
	lang.fn.set("admin.dbManage.label.rExecuteDate", "<spring:message code='admin.dbManage.label.rExecuteDate'/>"); <%-- 실행날짜 --%>
	lang.fn.set("admin.dbManage.label.rExecuteTime", "<spring:message code='admin.dbManage.label.rExecuteTime'/>"); <%-- 실행시간 --%>
	lang.fn.set("admin.dbManage.label.rCompleteDate", "<spring:message code='admin.dbManage.label.rCompleteDate'/>"); <%-- 완료날짜 --%>
	lang.fn.set("admin.dbManage.label.rCompleteTime", "<spring:message code='admin.dbManage.label.rCompleteTime'/>"); <%-- 완료시간 --%>
	lang.fn.set("admin.dbManage.label.rErrorMessage", "<spring:message code='admin.dbManage.label.rErrorMessage'/>"); <%-- Error --%>
	lang.fn.set("admin.dbManage.label.executeQuery", "<spring:message code='admin.dbManage.label.executeQuery'/>"); <%-- 실행 --%>

	lang.fn.set("admin.dbManage.tab.db", "<spring:message code='admin.dbManage.tab.db'/>"); <%-- DB --%>
	lang.fn.set("admin.dbManage.tab.query", "<spring:message code='admin.dbManage.tab.query'/>"); <%-- SQL --%>
	lang.fn.set("admin.dbManage.tab.job", "<spring:message code='admin.dbManage.tab.job'/>"); <%-- Job --%>
	lang.fn.set("admin.dbManage.tab.execute", "<spring:message code='admin.dbManage.tab.execute'/>"); <%-- Schedule --%>

	lang.fn.set("admin.dbManage.button.addList", "<spring:message code='admin.dbManage.button.addList'/>"); <%-- 추가 --%>
	lang.fn.set("admin.dbManage.button.delList", "<spring:message code='admin.dbManage.button.delList'/>"); <%-- 제거 --%>
	lang.fn.set("admin.dbManage.button.addSQL", "<spring:message code='admin.dbManage.button.addSQL'/>"); <%-- SQL등록 --%>
	lang.fn.set("admin.dbManage.button.addJob", "<spring:message code='admin.dbManage.button.addJob'/>"); <%-- 작업등록 --%>
	lang.fn.set("admin.dbManage.button.addExecute", "<spring:message code='admin.dbManage.button.addExecute'/>"); <%-- 스케줄등록 --%>
	lang.fn.set("admin.dbManage.button.addDB", "<spring:message code='admin.dbManage.button.addDB'/>"); <%-- DB등록 --%>
	lang.fn.set("admin.dbManage.button.modify", "<spring:message code='admin.dbManage.button.modify'/>"); <%-- 수정 --%>
	
	lang.fn.set("admin.dbManage.alert.message1", "<spring:message code='admin.dbManage.alert.message1'/>"); <%-- DB명을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message2", "<spring:message code='admin.dbManage.alert.message2'/>"); <%-- 서버를 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message3", "<spring:message code='admin.dbManage.alert.message3'/>"); <%-- URL을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message4", "<spring:message code='admin.dbManage.alert.message4'/>"); <%-- ID를 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message5", "<spring:message code='admin.dbManage.alert.message5'/>"); <%-- 비밀번호를 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message6", "<spring:message code='admin.dbManage.alert.message6'/>"); <%-- Timeout을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message7", "<spring:message code='admin.dbManage.alert.message7'/>"); <%-- 이미 등록된 DB명 입니다. --%>
	lang.fn.set("admin.dbManage.alert.message8", "<spring:message code='admin.dbManage.alert.message8'/>"); <%-- DB정보가 등록되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message9", "<spring:message code='admin.dbManage.alert.message9'/>"); <%-- DB정보 등록에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message10", "<spring:message code='admin.dbManage.alert.message10'/>"); <%-- SQL명을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message11", "<spring:message code='admin.dbManage.alert.message11'/>"); <%-- SQL을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message12", "<spring:message code='admin.dbManage.alert.message12'/>"); <%-- 이미 등록된 SQL명 입니다. --%>
	lang.fn.set("admin.dbManage.alert.message13", "<spring:message code='admin.dbManage.alert.message13'/>"); <%-- SQL정보가 등록되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message14", "<spring:message code='admin.dbManage.alert.message14'/>"); <%-- SQL정보 등록에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message15", "<spring:message code='admin.dbManage.alert.message15'/>"); <%-- 작업명을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message16", "<spring:message code='admin.dbManage.alert.message16'/>"); <%-- DB/SQL set을 추가하세요. --%>
	lang.fn.set("admin.dbManage.alert.message17", "<spring:message code='admin.dbManage.alert.message17'/>"); <%-- 이미 등록된 작업명 입니다. --%>
	lang.fn.set("admin.dbManage.alert.message18", "<spring:message code='admin.dbManage.alert.message18'/>"); <%-- 작업정보가 등록되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message19", "<spring:message code='admin.dbManage.alert.message19'/>"); <%-- 작업정보 등록에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message20", "<spring:message code='admin.dbManage.alert.message20'/>"); <%-- 스케줄명을 입력하세요. --%>
	lang.fn.set("admin.dbManage.alert.message21", "<spring:message code='admin.dbManage.alert.message21'/>"); <%-- 일정을 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message22", "<spring:message code='admin.dbManage.alert.message22'/>"); <%-- 요일을 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message23", "<spring:message code='admin.dbManage.alert.message23'/>"); <%-- 날짜를 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message24", "<spring:message code='admin.dbManage.alert.message24'/>"); <%-- 시간을 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message25", "<spring:message code='admin.dbManage.alert.message25'/>"); <%-- 이미 등록된 스케줄명 입니다. --%>
	lang.fn.set("admin.dbManage.alert.message26", "<spring:message code='admin.dbManage.alert.message26'/>"); <%-- 스케줄이 등록되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message27", "<spring:message code='admin.dbManage.alert.message27'/>"); <%-- 스케줄 등록에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message28", "<spring:message code='admin.dbManage.alert.message28'/>"); <%-- DB정보가 수정되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message29", "<spring:message code='admin.dbManage.alert.message29'/>"); <%-- DB정보 수정에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message30", "<spring:message code='admin.dbManage.alert.message30'/>"); <%-- SQL정보가 수정되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message31", "<spring:message code='admin.dbManage.alert.message31'/>"); <%-- SQL정보 수정에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message32", "<spring:message code='admin.dbManage.alert.message32'/>"); <%-- 작업정보가 수정되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message33", "<spring:message code='admin.dbManage.alert.message33'/>"); <%-- 작업정보 수정에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message34", "<spring:message code='admin.dbManage.alert.message34'/>"); <%-- 스케줄이 수정되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message35", "<spring:message code='admin.dbManage.alert.message35'/>"); <%-- 스케줄 수정에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message36", "<spring:message code='admin.dbManage.alert.message36'/>"); <%-- 삭제되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message37", "<spring:message code='admin.dbManage.alert.message37'/>"); <%-- 삭제에 실패했습니다. --%>
	lang.fn.set("admin.dbManage.alert.message38", "<spring:message code='admin.dbManage.alert.message38'/>"); <%-- 삭제할 정보를 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message39", "<spring:message code='admin.dbManage.alert.message39'/>"); <%-- DB연결에 성공하였습니다. --%>
	lang.fn.set("admin.dbManage.alert.message40", "<spring:message code='admin.dbManage.alert.message40'/>"); <%-- 유효하지 않은 DB정보 입니다. --%>
	lang.fn.set("admin.dbManage.alert.message41", "<spring:message code='admin.dbManage.alert.message41'/>"); <%-- 추가할 DB명을 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message42", "<spring:message code='admin.dbManage.alert.message42'/>"); <%-- 추가할 SQL명을 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message43", "<spring:message code='admin.dbManage.alert.message43'/>"); <%-- 제거할 DB/SQL set을 선택하세요. --%>
	lang.fn.set("admin.dbManage.alert.message44", "<spring:message code='admin.dbManage.alert.message44'/>"); <%-- 작업이 완료 되었습니다. --%>
	lang.fn.set("admin.dbManage.alert.message45", "<spring:message code='admin.dbManage.alert.message45'/>"); <%-- 작업 실행에 실패 했습니다. --%>

	lang.fn.set("admin.dbManage.confirm.message1", "<spring:message code='admin.dbManage.confirm.message1'/>"); <%-- 선택한 DB를 포함하는 작업 및 스케줄 정보도 같이 삭제됩니다.\\n삭제하시겠습니까? --%>
	lang.fn.set("admin.dbManage.confirm.message2", "<spring:message code='admin.dbManage.confirm.message2'/>"); <%-- 선택한 SQL을 포함하는 작업 및 스케줄 정보도 같이 삭제됩니다.\\n삭제하시겠습니까? --%>
	lang.fn.set("admin.dbManage.confirm.message3", "<spring:message code='admin.dbManage.confirm.message3'/>"); <%-- 선택한 작업을 포함하는 스케줄 정보도 같이 삭제됩니다.\\n삭제하시겠습니까? --%>
	lang.fn.set("admin.dbManage.confirm.message4", "<spring:message code='admin.dbManage.confirm.message4'/>"); <%-- 선택한 스케줄을 삭제하시겠습니까? --%>

	lang.fn.set("admin.userDBInterface.title.modify", 			"<spring:message code='admin.userDBInterface.title.modify'/>"); <%-- 수정 --%>
	
	lang.fn.set("admin.userDBInterface.label.rDbIp", 			"<spring:message code='admin.userDBInterface.label.rDbIp'/>"); <%-- IP --%>
	lang.fn.set("admin.userDBInterface.label.rDbUser", 			"<spring:message code='admin.userDBInterface.label.rDbUser'/>"); <%-- DB USER --%>
	lang.fn.set("admin.userDBInterface.label.rDbPwd", 			"<spring:message code='admin.userDBInterface.label.rDbPwd'/>"); <%-- DB PWD --%>
	lang.fn.set("admin.userDBInterface.label.rDbPort", 			"<spring:message code='admin.userDBInterface.label.rDbPort'/>"); <%-- PORT --%>
	lang.fn.set("admin.userDBInterface.label.rDbName", 			"<spring:message code='admin.userDBInterface.label.rDbName'/>"); <%-- DB NAME --%>
	lang.fn.set("admin.userDBInterface.label.rTeamCode", 		"<spring:message code='admin.userDBInterface.label.rTeamCode'/>"); <%-- Team Code --%>
	lang.fn.set("admin.userDBInterface.label.rTeamName", 		"<spring:message code='admin.userDBInterface.label.rTeamName'/>"); <%-- Team Name --%>
	lang.fn.set("admin.userDBInterface.label.rHh", 				"<spring:message code='admin.userDBInterface.label.rHh'/>"); <%-- Process Time --%>
	lang.fn.set("admin.userDBInterface.label.rQuickExcution", 	"<spring:message code='admin.userDBInterface.label.rQuickExcution'/>"); <%-- Excute --%>
	
	lang.fn.set("admin.userDBInterface.button.rQuickExcution", 	"<spring:message code='admin.userDBInterface.button.rQuickExcution'/>"); <%-- QUick Excute --%>
	lang.fn.set("admin.userDBInterface.button.add", 			"<spring:message code='admin.userDBInterface.button.add'/>"); <%-- 추가 --%>
	lang.fn.set("admin.userDBInterface.button.modify", 			"<spring:message code='admin.userDBInterface.button.modify'/>"); <%-- 수정 --%>
	
	lang.fn.set("admin.userDBInterface.alert.message1", 		"<spring:message code='admin.userDBInterface.alert.message1'/>"); <%-- IP를 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message2", 		"<spring:message code='admin.userDBInterface.alert.message2'/>"); <%-- DB USER를 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message3", 		"<spring:message code='admin.userDBInterface.alert.message3'/>"); <%-- DB PWD를 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message4", 		"<spring:message code='admin.userDBInterface.alert.message4'/>"); <%-- PORT를 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message5", 		"<spring:message code='admin.userDBInterface.alert.message5'/>"); <%-- DB NAME을 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message6", 		"<spring:message code='admin.userDBInterface.alert.message6'/>"); <%-- Team Code를 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message7", 		"<spring:message code='admin.userDBInterface.alert.message7'/>"); <%-- Team Name을 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message8", 		"<spring:message code='admin.userDBInterface.alert.message8'/>"); <%-- Process Time을 입력하세요. --%>
	lang.fn.set("admin.userDBInterface.alert.message9", 		"<spring:message code='admin.userDBInterface.alert.message9'/>"); <%-- 이미 등록된 DB NAME 입니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message10", 		"<spring:message code='admin.userDBInterface.alert.message10'/>"); <%-- 등롱되었습니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message11", 		"<spring:message code='admin.userDBInterface.alert.message11'/>"); <%-- 등록에 실패했습니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message12", 		"<spring:message code='admin.userDBInterface.alert.message12'/>"); <%-- 수정되었습니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message13", 		"<spring:message code='admin.userDBInterface.alert.message13'/>"); <%-- 수정에 실패했습니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message14", 		"<spring:message code='admin.userDBInterface.alert.message14'/>"); <%-- 삭제되었습니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message15", 		"<spring:message code='admin.userDBInterface.alert.message15'/>"); <%-- 삭제에 실패했습니다. --%>
	lang.fn.set("admin.userDBInterface.alert.message16", 		"<spring:message code='admin.userDBInterface.alert.message16'/>"); <%-- 삭제할 정보를 선택하세요. --%>
	
	lang.fn.set("admin.userDBInterface.confirm.message1", 		"<spring:message code='admin.userDBInterface.confirm.message1'/>"); <%-- 선택한 정보를 삭제하시겠습니까? --%>
	
	// common
	lang.fn.set("header.label.hello", "<spring:message code='header.label.hello'/>"); <%-- 안녕하세요 --%>
	lang.fn.set("header.title.settings", "<spring:message code='header.title.settings'/>"); <%-- 환경설정 --%>
	lang.fn.set("header.title.modifyUser", "<spring:message code='header.title.modifyUser'/>"); <%-- 개인정보변경 --%>
	lang.fn.set("header.title.logout", "<spring:message code='header.title.logout'/>"); <%-- 로그아웃 --%>
	lang.fn.set("header.menu.label.searchNListen", "<spring:message code='header.menu.label.searchNListen'/>"); <%-- 조회 및 청취 --%>
	lang.fn.set("header.menu.label.searchNListenCallType", "<spring:message code='header.menu.label.searchNListenCallType'/>"); <%-- 조회 및 청취 --%>
	
	lang.fn.set("header.menu.label.playerSetting", "<spring:message code='header.menu.label.playerSetting'/>"); <%-- 조회 및 청취 --%>
	lang.fn.set("header.menu.label.playerUnderMenu", "<spring:message code='header.menu.label.playerUnderMenu'/>"); <%-- 조회 및 청취 --%>
	lang.fn.set("header.menu.label.playersectionMenu", "<spring:message code='header.menu.label.playersectionMenu'/>"); <%-- 조회 및 청취 --%>
	lang.fn.set("header.menu.label.downloadApproveInit", "<spring:message code='header.menu.label.downloadApproveInit'/>"); <%-- 조회 및 청취 --%>
	lang.fn.set("header.menu.label.searchNListenMemo", "<spring:message code='header.menu.label.searchNListenMemo'/>"); <%-- 조회 및 청취 메모 --%>
	lang.fn.set("header.menu.label.systemMonitoring", "<spring:message code='header.menu.label.systemMonitoring'/>"); <%-- 시스템 모니터링 --%>
	lang.fn.set("header.menu.label.reportNDashboard", "<spring:message code='header.menu.label.reportNDashboard'/>"); <%-- 레포트 및 대시보드 --%>
	lang.fn.set("header.menu.label.dashboard", "<spring:message code='header.menu.label.dashboard'/>"); <%-- 대시보드 --%>
	lang.fn.set("header.menu.label.callAll", "<spring:message code='header.menu.label.callAll'/>"); <%-- 전체 콜 통계 --%>
	lang.fn.set("header.menu.label.callUser", "<spring:message code='header.menu.label.callUser'/>"); <%-- 사용자별 콜 통계 --%>
	lang.fn.set("header.menu.label.callDayTimeUser", "<spring:message code='header.menu.label.callDayTimeUser'/>"); <%-- 일 시 사용자별 콜 통계 --%>
	lang.fn.set("header.menu.label.callMobile", "<spring:message code='header.menu.label.callMobile'/>"); <%-- 모바일 콜 통계 --%>
	lang.fn.set("header.menu.label.monitoring", "<spring:message code='header.menu.label.monitoring'/>"); <%-- 모니터링 --%>
	lang.fn.set("header.menu.label.realtimeMonitoring", "<spring:message code='header.menu.label.realtimeMonitoring'/>"); <%-- 카드뷰 모니터링 --%>
	lang.fn.set("header.menu.label.realtimeMonitoringGrid", "<spring:message code='header.menu.label.realtimeMonitoringGrid'/>"); <%-- 그리드뷰 모니터링 --%>
	lang.fn.set("header.menu.label.realtimeMonitoringOffice", "<spring:message code='header.menu.label.realtimeMonitoringOffice'/>"); <%-- 오피스뷰 모니터링 --%>
	lang.fn.set("header.menu.label.serverMonitoring", "<spring:message code='header.menu.label.serverMonitoring'/>"); <%-- 서버 모니터링 --%>
	lang.fn.set("header.menu.label.systemRealtimeMonitoring", "<spring:message code='header.menu.label.systemRealtimeMonitoring'/>"); <%-- 시스템 모니터링 --%>
	lang.fn.set("header.menu.label.BluePrintMonitoring", "<spring:message code='header.menu.label.BluePrintMonitoring'/>"); <%-- 도면 모니터링 --%>
	lang.fn.set("header.menu.label.myFolderMenu", "<spring:message code='header.menu.label.myFolderMenu'/>"); <%-- 마이 폴더 --%>
	lang.fn.set("header.menu.label.uploadStatus", "<spring:message code='header.menu.label.uploadStatus'/>"); <%-- 업로드 현황 --%>
	lang.fn.set("header.menu.label.individualUploadstatus", "<spring:message code='header.menu.label.individualUploadstatus'/>"); <%-- 개별 업로드 현황 --%>
	lang.fn.set("header.menu.label.uploadStatusSearch", "<spring:message code='header.menu.label.uploadStatusSearch'/>"); <%-- 업로드 현황 조회 --%>
	lang.fn.set("header.menu.label.BestCall", "<spring:message code='header.menu.label.BestCall'/>"); <%-- 우수콜 --%>
	lang.fn.set("header.menu.label.BestCallManage", "<spring:message code='header.menu.label.BestCallManage'/>"); <%-- 우수콜 관리 --%>
	lang.fn.set("header.menu.label.BestCallShare", "<spring:message code='header.menu.label.BestCallShare'/>"); <%-- 우수콜 공유 --%>
	lang.fn.set("header.menu.label.approveList", "<spring:message code='header.menu.label.approveList'/>"); <%-- 청취 및 다운로드 요청 관리 --%>
	lang.fn.set("header.menu.label.evaluation", "<spring:message code='header.menu.label.evaluation'/>"); <%-- 평가 관리 --%>
	lang.fn.set("header.menu.label.evaluationList", "<spring:message code='header.menu.label.evaluationList'/>"); <%-- 평가 조회 --%>
	lang.fn.set("header.menu.label.itemManage", "<spring:message code='header.menu.label.itemManage'/>"); <%-- 평가항목 관리 --%>
	lang.fn.set("header.menu.label.sheetManage", "<spring:message code='header.menu.label.sheetManage'/>"); <%-- 평가지 관리 --%>
	lang.fn.set("header.menu.label.campaignManage", "<spring:message code='header.menu.label.campaignManage'/>"); <%-- 캠페인 관리 --%>
	lang.fn.set("header.menu.label.faceRecording", "<spring:message code='header.menu.label.faceRecording'/>"); <%-- 대면 녹취--%>
	lang.fn.set("header.menu.label.recording", "<spring:message code='header.menu.label.recording'/>"); <%-- 녹취--%>
	lang.fn.set("header.menu.label.audioCalibration", "<spring:message code='header.menu.label.audioCalibration'/>"); <%-- 음질 보정--%>
	lang.fn.set("header.menu.label.scriptRegistration", "<spring:message code='header.menu.label.scriptRegistration'/>"); <%-- 스크립트 조회--%>
	lang.fn.set("header.menu.label.scriptRegistrationApprove", "<spring:message code='header.menu.label.scriptRegistrationApprove'/>"); <%-- 결재 관리--%>
	lang.fn.set("header.menu.label.scriptRegistrationCommon", "<spring:message code='header.menu.label.scriptRegistrationCommon'/>"); <%-- 결재 관리--%>
	lang.fn.set("header.menu.label.scriptRegistrationApproveReport", "<spring:message code='header.menu.label.scriptRegistrationApproveReport'/>"); <%-- 결재 관리 상신--%>
	
	lang.fn.set("header.menu.label.evaluating", "<spring:message code='header.menu.label.evaluating'/>"); <%-- 캠페인 관리 --%>
	lang.fn.set("header.menu.label.evaluationStatistics", "<spring:message code='header.menu.label.evaluationStatistics'/>"); <%-- 캠페인 관리 --%>

	lang.fn.set("header.menu.label.administer", "<spring:message code='header.menu.label.administer'/>"); <%-- 관리하기 --%>
	lang.fn.set("header.popup.modifyUser.label.manageUserInfo", "<spring:message code='header.popup.modifyUser.label.manageUserInfo'/>"); <%-- 사용자 정보 관리 --%>
	lang.fn.set("header.popup.modifyUser.label.id", "<spring:message code='header.popup.modifyUser.label.id'/>"); <%-- ID --%>
	lang.fn.set("header.popup.modifyUser.label.userName", "<spring:message code='header.popup.modifyUser.label.userName'/>"); <%-- 사용자 이름 --%>
	lang.fn.set("header.popup.modifyUser.label.password", "<spring:message code='header.popup.modifyUser.label.password'/>"); <%-- 비밀번호 --%>
	lang.fn.set("header.popup.modifyUser.label.passwordCheck", "<spring:message code='header.popup.modifyUser.label.passwordCheck'/>"); <%-- 비밀번호 확인 --%>
	lang.fn.set("header.popup.modifyUser.label.ext", "<spring:message code='header.popup.modifyUser.label.ext'/>"); <%-- 내선번호 --%>
	lang.fn.set("header.popup.modifyUser.label.phoneNo", "<spring:message code='header.popup.modifyUser.label.phoneNo'/>"); <%-- 연락처 --%>
	lang.fn.set("header.popup.modifyUser.label.sex", "<spring:message code='header.popup.modifyUser.label.sex'/>"); <%-- 성별 --%>
	lang.fn.set("header.popup.modifyUser.option.male", "<spring:message code='header.popup.modifyUser.option.male'/>"); <%-- 남자 --%>
	lang.fn.set("header.popup.modifyUser.option.female", "<spring:message code='header.popup.modifyUser.option.female'/>"); <%-- 여자 --%>
	lang.fn.set("header.popup.modifyUser.label.userLevel", "<spring:message code='header.popup.modifyUser.label.userLevel'/>"); <%-- 권한등급 --%>
	lang.fn.set("header.popup.modifyUser.label.level1", "<spring:message code='header.popup.modifyUser.label.level1'/>"); <%-- 대분류 --%>
	lang.fn.set("header.popup.modifyUser.label.level2", "<spring:message code='header.popup.modifyUser.label.level2'/>"); <%-- 중분류 --%>
	lang.fn.set("header.popup.modifyUser.label.level3", "<spring:message code='header.popup.modifyUser.label.level3'/>"); <%-- 소분류 --%>
	lang.fn.set("header.popup.modifyUser.label.empNo", "<spring:message code='header.popup.modifyUser.label.empNo'/>"); <%-- 사원번호 --%>
	lang.fn.set("header.popup.modifyUser.label.email", "<spring:message code='header.popup.modifyUser.label.email'/>"); <%-- 이메일 --%>
	lang.fn.set("header.popup.modifyUser.label.ctiId", "<spring:message code='header.popup.modifyUser.label.ctiId'/>"); <%-- CTI ID --%>
	lang.fn.set("header.popup.modifyUser.button.modify", "<spring:message code='header.popup.modifyUser.button.modify'/>"); <%-- 개인 정보 수정 --%>
	lang.fn.set("header.popup.evaluation.label.evaluation", "<spring:message code='header.popup.evaluation.label.evaluation'/>"); <%-- 상담원 평가  --%>
	lang.fn.set("header.popup.evaluation.option.selectCampaign", "<spring:message code='header.popup.evaluation.option.selectCampaign'/>"); <%-- 캠페인 선택 --%>
	lang.fn.set("header.popup.evaluation.option.selectSheet", "<spring:message code='header.popup.evaluation.option.selectSheet'/>"); <%-- 평가지 선택 --%>
	lang.fn.set("header.popup.evaluation.input.notification", "<spring:message code='header.popup.evaluation.input.notification'/>"); <%-- 상담원 알림 --%>
	lang.fn.set("header.popup.evaluation.input.objection", "<spring:message code='header.popup.evaluation.input.objection'/>"); <%-- 이의제기 --%>
	lang.fn.set("header.popup.evaluation.input.revaluation", "<spring:message code='header.popup.evaluation.input.revaluation'/>"); <%-- 재평가 여부 --%>
	lang.fn.set("header.popup.evaluation.input.scoringSystem", "<spring:message code='header.popup.evaluation.input.scoringSystem'/>"); <%-- 채점방식 --%>
	lang.fn.set("header.popup.evaluation.label.evaluatorComment", "<spring:message code='header.popup.evaluation.label.evaluatorComment'/>"); <%-- 평가자 종합 의견 --%>
	lang.fn.set("header.popup.evaluation.label.evaluatorComment2", "<spring:message code='header.popup.evaluation.label.evaluatorComment2'/>"); <%-- 2차 평가자 종합 의견 --%>
	lang.fn.set("header.popup.evaluation.label.appraiseeComment", "<spring:message code='header.popup.evaluation.label.appraiseeComment'/>"); <%-- 상담원 종합 의견 --%>
	lang.fn.set("header.popup.evaluation.button.moveCampaign", "<spring:message code='header.popup.evaluation.button.moveCampaign'/>"); <%-- 캠페인 관리로 이동 --%>
	lang.fn.set("header.popup.evaluation.label.sheetScore", "<spring:message code='header.popup.evaluation.label.sheetScore'/>"); <%-- 평가 총 점수 --%>
	lang.fn.set("header.popup.evaluation.label.totalScore", "<spring:message code='header.popup.evaluation.label.totalScore'/>"); <%-- 평가된 점수 --%>
	lang.fn.set("header.popup.evaluation.button.interimStorage", "<spring:message code='header.popup.evaluation.button.interimStorage'/>"); <%-- 중간 저장 --%>
	lang.fn.set("header.popup.evaluation.button.done", "<spring:message code='header.popup.evaluation.button.done'/>"); <%-- 평가 완료 --%>
	lang.fn.set("header.popup.evaluation.alert.noData", "<spring:message code='header.popup.evaluation.alert.noData'/>"); <%-- 항목이 없습니다. --%>
	lang.fn.set("header.popup.evaluation.alert.notAllSelected", "<spring:message code='header.popup.evaluation.alert.notAllSelected'/>"); <%-- 선택하지 않은 항목이 있습니다. --%>
	lang.fn.set("header.popup.evaluation.alert.updateEvaluationComplete", "<spring:message code='header.popup.evaluation.alert.updateEvaluationComplete'/>"); <%-- 평가가 저장되었습니다. --%>
	lang.fn.set("header.popup.evaluation.alert.updateEvaluationFailed", "<spring:message code='header.popup.evaluation.alert.updateEvaluationFailed'/>"); <%-- 평가 저장에 실패했습니다. --%>
	
	lang.fn.set("tab.alert.exceeds", "<spring:message code='tab.alert.exceeds'/>"); <%-- 탭개수가 10개를 초과할 수 없습니다. 사용중인 탭을 종료해 주세요. --%>

	// evaluation
	lang.fn.set("evaluation.result.label.whole", "<spring:message code='evaluation.result.label.whole'/>"); <%-- 평가 결과 전체 --%>
	lang.fn.set("evaluation.result.label.closed", "<spring:message code='evaluation.result.label.closed'/>"); <%-- 완료된 평가 --%>
	lang.fn.set("evaluation.result.label.inProgress", "<spring:message code='evaluation.result.label.inProgress'/>"); <%-- 평가 중 --%>
	lang.fn.set("evaluation.result.placeholder.evalDateStart", "<spring:message code='evaluation.result.placeholder.evalDateStart'/>"); <%-- 평가 날짜 --%>
	lang.fn.set("evaluation.result.placeholder.evalDateEnd", "<spring:message code='evaluation.result.placeholder.evalDateEnd'/>"); <%-- 종료 날짜 --%>
	lang.fn.set("evaluation.result.placeholder.recDateStart", "<spring:message code='evaluation.result.placeholder.recDateStart'/>"); <%-- 녹취 날짜 --%>
	lang.fn.set("evaluation.result.placeholder.recDateEnd", "<spring:message code='evaluation.result.placeholder.recDateEnd'/>"); <%-- 종료 날짜 --%>
	lang.fn.set("evaluation.result.option.recTimeStart", "<spring:message code='evaluation.result.option.recTimeStart'/>"); <%-- 녹취 시작 시간 --%>
	lang.fn.set("evaluation.result.option.recTimeEnd", "<spring:message code='evaluation.result.option.recTimeEnd'/>"); <%-- 녹취 종료 시간 --%>
	lang.fn.set("evaluation.result.button.search", "<spring:message code='evaluation.result.button.search'/>"); <%-- 조회 --%>
	lang.fn.set("evaluation.result.option.campaignTitle", "<spring:message code='evaluation.result.option.campaignTitle'/>"); <%-- 캠페인명 --%>
	lang.fn.set("evaluation.result.option.status", "<spring:message code='evaluation.result.option.status'/>"); <%-- 평가상태 --%>
	lang.fn.set("evaluation.result.option.all", "<spring:message code='evaluation.result.option.all'/>"); <%-- 전체 --%>
	lang.fn.set("evaluation.result.option.status.C", "<spring:message code='evaluation.result.option.status.C'/>"); <%-- 완료 --%>
	lang.fn.set("evaluation.result.option.status.I", "<spring:message code='evaluation.result.option.status.I'/>"); <%-- 미완료 --%>
	lang.fn.set("evaluation.result.option.status.X", "<spring:message code='evaluation.result.option.status.X'/>"); <%-- - --%>
	lang.fn.set("evaluation.result.placeholder.evaluator", "<spring:message code='evaluation.result.placeholder.evaluator'/>"); <%-- 평가자 --%>
	lang.fn.set("evaluation.result.placeholder.evaluatorComment", "<spring:message code='evaluation.result.placeholder.evaluatorComment'/>"); <%-- 평가자 피드백 --%>
	lang.fn.set("evaluation.result.placeholder.appraiseeComment", "<spring:message code='evaluation.result.placeholder.appraiseeComment'/>"); <%-- 상담원 피드백 --%>
	lang.fn.set("evaluation.result.placeholder.agentId", "<spring:message code='evaluation.result.placeholder.agentId'/>"); <%-- 상담원ID --%>
	lang.fn.set("evaluation.result.placeholder.agentName", "<spring:message code='evaluation.result.placeholder.agentName'/>"); <%-- 상담원명 --%>
	lang.fn.set("evaluation.result.placeholder.callNumber", "<spring:message code='evaluation.result.placeholder.callNumber'/>"); <%-- 고객번호 --%>

	// login
	lang.fn.set("login.login.title.rememberMe", "<spring:message code='login.login.title.rememberMe'/>"); <%-- 아이디 저장 --%>
	lang.fn.set("login.login.title.login", "<spring:message code='login.login.title.login'/>"); <%-- 로그인 --%>
	lang.fn.set("login.login.placeholder.code", "<spring:message code='login.login.placeholder.code'/>"); <%-- 코드 --%>
	lang.fn.set("login.login.placeholder.id", "<spring:message code='login.login.placeholder.id'/>"); <%-- 아이디 --%>
	lang.fn.set("login.login.placeholder.password", "<spring:message code='login.login.placeholder.password'/>"); <%-- 비밀번호 --%>
	lang.fn.set("login.login.placeholder.extension", "<spring:message code='login.login.placeholder.extension'/>"); <%-- 내선번호 --%>
	lang.fn.set("login.login.alert.not.input.code", "<spring:message code='login.login.alert.not.input.code'/>"); <%-- 시스템 코드를 입력하세요. --%>
	lang.fn.set("login.login.alert.not.input.id", "<spring:message code='login.login.alert.not.input.id'/>"); <%-- Username를 입력하세요. --%>
	lang.fn.set("login.login.alert.not.input.pw", "<spring:message code='login.login.alert.not.input.pw'/>"); <%-- Password를 입력하세요. --%>
	lang.fn.set("login.login.alert.not.input.ext", "<spring:message code='login.login.alert.not.input.ext'/>"); <%-- 내선번호를 입력하세요. --%>
	lang.fn.set("login.login.alert.not.input.lang", "<spring:message code='login.login.alert.not.input.lang'/>"); <%-- 언어를 선택하세요. --%>
	lang.fn.set("login.login.alert.fail.login", "<spring:message code='login.login.alert.fail.login'/>"); <%-- 로그인에 실패했습니다. --%>

	// monitoring
	lang.fn.set("monitoring.display.selectedSystem", "<spring:message code='monitoring.display.selectedSystem'/>"); <%-- 현재 조회 중인 시스템 --%>
	lang.fn.set("monitoring.display.system", "<spring:message code='monitoring.display.system'/>"); <%-- 시스템 --%>
	lang.fn.set("monitoring.display.group", "<spring:message code='monitoring.display.group'/>"); <%-- 선택할 그룹 --%>
	lang.fn.set("monitoring.display.searchAll", "<spring:message code='monitoring.display.searchAll'/>"); <%-- 전체 조회 --%>
	lang.fn.set("monitoring.display.search", "<spring:message code='monitoring.display.search'/>"); <%-- 조회 --%>
	lang.fn.set("monitoring.display.monitoringOption", "<spring:message code='monitoring.display.monitoringOption'/>"); <%-- 모니터링 설정 --%>
	lang.fn.set("monitoring.display.timerOption", "<spring:message code='monitoring.display.timerOption'/>"); <%-- 타이머 설정 --%>
	lang.fn.set("monitoring.display.listenAlways", "<spring:message code='monitoring.display.listenAlways'/>"); <%-- 지속감청 --%>
	lang.fn.set("monitoring.display.officeView", "<spring:message code='monitoring.display.officeView'/>"); <%-- Office View --%>
	lang.fn.set("monitoring.display.cardView", "<spring:message code='monitoring.display.cardView'/>"); <%-- Card View --%>
	lang.fn.set("monitoring.display.gridView", "<spring:message code='monitoring.display.gridView'/>"); <%-- Grid View --%>
	lang.fn.set("monitoring.display.systemHardware", "<spring:message code='monitoring.display.systemHardware'/>"); <%-- System Hardware --%>
	lang.fn.set("monitoring.display.CPU", "<spring:message code='monitoring.display.CPU'/>"); <%-- CPU --%>
	lang.fn.set("monitoring.display.MEMORY", "<spring:message code='monitoring.display.MEMORY'/>"); <%-- MEMORY --%>
	lang.fn.set("monitoring.display.HDD", "<spring:message code='monitoring.display.HDD'/>"); <%-- HDD --%>
	lang.fn.set("monitoring.display.statusViewOptions", "<spring:message code='monitoring.display.statusViewOptions'/>"); <%-- Status view options --%>
	lang.fn.set("monitoring.display.displayOptions", "<spring:message code='monitoring.display.displayOptions'/>"); <%-- Display options --%>
	lang.fn.set("monitoring.display.cookieSaveOptions", "<spring:message code='monitoring.display.cookieSaveOptions'/>"); <%-- Display options --%>
	lang.fn.set("monitoring.display.icon", "<spring:message code='monitoring.display.icon'/>"); <%-- 아이콘 표시 --%>
	lang.fn.set("monitoring.display.agentId", "<spring:message code='monitoring.display.agentId'/>"); <%-- 사용자ID 표시 --%>
	lang.fn.set("monitoring.display.number", "<spring:message code='monitoring.display.number'/>"); <%-- 전화번호 표시 --%>
	lang.fn.set("monitoring.display.callTime", "<spring:message code='monitoring.display.callTime'/>"); <%-- 통화시간 표시 --%>
	lang.fn.set("monitoring.display.realtimeListen", "<spring:message code='monitoring.display.realtimeListen'/>"); <%-- REALTIME LISTEN --%>
	lang.fn.set("monitoring.state.save", "<spring:message code='monitoring.state.save'/>"); <%-- Login --%>
	lang.fn.set("monitoring.state.login", "<spring:message code='monitoring.state.login'/>"); <%-- Login --%>
	lang.fn.set("monitoring.state.ready", "<spring:message code='monitoring.state.ready'/>"); <%-- Ready --%>
	lang.fn.set("monitoring.state.calling", "<spring:message code='monitoring.state.calling'/>"); <%-- Calling --%>
	lang.fn.set("monitoring.state.away", "<spring:message code='monitoring.state.away'/>"); <%-- AWAY --%>
	lang.fn.set("monitoring.state.ACW", "<spring:message code='monitoring.state.ACW'/>"); <%-- ACW --%>
	lang.fn.set("monitoring.state.logout", "<spring:message code='monitoring.state.logout'/>"); <%-- Logout --%>

	// search
	lang.fn.set("search.option.startDate", "<spring:message code='search.option.startDate'/>"); <%-- 시작 날짜 --%>
	lang.fn.set("search.option.endDate", "<spring:message code='search.option.endDate'/>"); <%-- 종료 날짜 --%>
	lang.fn.set("views.search.button.pathCheck", "<spring:message code='views.search.button.pathCheck'/>"); <%-- 경로 확인 --%>
	lang.fn.set("views.search.button.pathChange", "<spring:message code='views.search.button.pathChange'/>"); <%-- 경로 변경 --%>	
	lang.fn.set("search.option.recStart", "<spring:message code='search.option.recStart'/>"); <%-- 녹취 시작 --%>
	lang.fn.set("search.option.recEnd", "<spring:message code='search.option.recEnd'/>"); <%-- 녹취 종료 --%>
	lang.fn.set("search.option.callStart", "<spring:message code='search.option.callStart'/>"); <%-- 통화 시작 --%>
	lang.fn.set("search.option.callEnd", "<spring:message code='search.option.callEnd'/>"); <%-- 통화 종료 --%>
	lang.fn.set("search.option.search", "<spring:message code='search.option.search'/>"); <%-- 조회 --%>
	lang.fn.set("search.option.callType", "<spring:message code='search.option.callType'/>"); <%-- 콜타입 --%>
	lang.fn.set("search.option.callType2", "<spring:message code='search.option.callType2'/>"); <%-- 콜타입2 --%>
	lang.fn.set("search.option.custNo", "<spring:message code='search.option.custNo'/>"); <%-- 고객 번호 --%>
	lang.fn.set("search.option.custName", "<spring:message code='search.option.custName'/>"); <%-- 고객명 --%>
	lang.fn.set("search.option.agentId", "<spring:message code='search.option.agentId'/>"); <%-- 사용자ID --%>
	lang.fn.set("search.option.agentName", "<spring:message code='search.option.agentName'/>"); <%-- 사용자명 --%>
	lang.fn.set("search.option.ext", "<spring:message code='search.option.ext'/>"); <%-- 내선번호 --%>
	lang.fn.set("search.option.que1", "<spring:message code='search.option.que1'/>"); <%-- 큐1 --%>
	lang.fn.set("search.option.que2", "<spring:message code='search.option.que2'/>"); <%-- 큐2 --%>
	lang.fn.set("search.option.agentClose", "<spring:message code='search.option.agentClose'/>"); <%-- 상담원 종료 --%>
	lang.fn.set("search.option.medium", "<spring:message code='search.option.medium'/>"); <%-- 중분류 --%>
	lang.fn.set("search.option.small", "<spring:message code='search.option.small'/>"); <%-- 소분류 --%>
	lang.fn.set("search.option.natId", "<spring:message code='search.option.natId'/>"); <%-- 주민등록번호 --%>
	lang.fn.set("search.option.empId", "<spring:message code='search.option.empId'/>"); <%-- 사번 --%>
	lang.fn.set("search.option.screen", "<spring:message code='search.option.screen'/>"); <%-- 스크린 --%>
	lang.fn.set("search.option.trace", "<spring:message code='search.option.trace'/>"); <%-- 트레이스 --%>
	lang.fn.set("search.option.evaluation", "<spring:message code='search.option.evaluation'/>"); <%-- 평가 --%>
	lang.fn.set("search.option.startType", "<spring:message code='search.option.startType'/>"); <%-- 시작유형 --%>
	lang.fn.set("search.player.nowPlaying", "<spring:message code='search.player.nowPlaying'/>"); <%-- 현재 재생 중인 파일 :  --%>
	lang.fn.set("search.player.noFiles", "<spring:message code='search.player.noFiles'/>"); <%-- 재생중인 파일이 없습니다. --%>

	//search Form
	lang.fn.set("views.search.form.fieldset.fDate", "<spring:message code='views.search.form.fieldset.fDate'/>"); <%-- 녹취 일자 --%>
	lang.fn.set("views.search.form.fieldset.product", "<spring:message code='views.search.form.fieldset.product'/>"); <%-- 상품정보 --%>
	lang.fn.set("views.search.form.fieldset.fTime", "<spring:message code='views.search.form.fieldset.fTime'/>"); <%-- 녹취 시간 --%>
	lang.fn.set("views.search.form.fieldset.fTtime", "<spring:message code='views.search.form.fieldset.fTtime'/>"); <%-- 통화 시간 --%>
	lang.fn.set("views.search.form.fieldset.fRtime", "<spring:message code='views.search.form.fieldset.fRtime'/>"); <%-- 녹취 시각 --%>
	lang.fn.set("views.search.form.fieldset.fCallKind", "<spring:message code='views.search.form.fieldset.fCallKind'/>"); <%-- 콜 타입 --%>
	lang.fn.set("views.search.form.fieldset.fCounselResultCode", "<spring:message code='views.search.form.fieldset.fCounselResultCode'/>"); <%-- 상담 결과 코드 --%>
	lang.fn.set("views.search.form.fieldset.fStatus", "<spring:message code='views.search.form.fieldset.fStatus'/>"); <%-- 진행 상태 --%>
	lang.fn.set("views.search.form.fieldset.fUserInfo", "<spring:message code='views.search.form.fieldset.fUserInfo'/>"); <%-- 사용자 정보 --%>
	lang.fn.set("views.search.form.fieldset.fCustInfo", "<spring:message code='views.search.form.fieldset.fCustInfo'/>"); <%-- 고객 정보 --%>
	lang.fn.set("views.search.form.fieldset.fOrgCode", "<spring:message code='views.search.form.fieldset.fOrgCode'/>"); <%-- 조직 정보 --%>
	lang.fn.set("views.search.form.fieldset.fRecInfo", "<spring:message code='views.search.form.fieldset.fRecInfo'/>"); <%-- 녹취 정보 --%>
	lang.fn.set("views.search.form.fieldset.fEtc", "<spring:message code='views.search.form.fieldset.fEtc'/>"); <%-- 기타 --%>
	lang.fn.set("views.search.form.fieldset.fTtimeConnect", "<spring:message code='views.search.form.fieldset.fTtimeConnect'/>"); <%-- 유효통화시간 --%>
	lang.fn.set("views.search.form.fieldset.fTimeConnect", "<spring:message code='views.search.form.fieldset.fTimeConnect'/>"); <%-- 유효녹취시간 --%>
	lang.fn.set("views.search.form.fieldset.fText", "<spring:message code='views.search.form.fieldset.fText'/>"); <%-- 통화 내용 --%>
	lang.fn.set("views.search.form.fieldset.transcriber", "<spring:message code='views.search.form.fieldset.transcriber'/>"); <%-- 전사 정보--%>
	lang.fn.set("views.search.form.fieldset.fSubNumberInfo", "<spring:message code='views.search.form.fieldset.fSubNumberInfo'/>"); <%-- 자번호 정보--%>
	lang.fn.set("views.search.form.fieldset.fDataset", "<spring:message code='views.search.form.fieldset.fDataset'/>"); <%-- 데이터셋--%>
	lang.fn.set("views.search.form.fieldset.fModelName", "<spring:message code='views.search.form.fieldset.fModelName'/>"); <%-- 모델명--%>
	lang.fn.set("views.search.form.fieldset.fLDate", "<spring:message code='views.search.form.fieldset.fLDate'/>"); <%-- 학습요청날짜--%>
	lang.fn.set("views.search.form.fieldset.fLTime", "<spring:message code='views.search.form.fieldset.fLTime'/>"); <%-- 학습요청시간--%>
	lang.fn.set("views.search.form.fieldset.fCDate", "<spring:message code='views.search.form.fieldset.fCDate'/>"); <%-- 생성날짜--%>
	lang.fn.set("views.search.form.fieldset.fCTime", "<spring:message code='views.search.form.fieldset.fCTime'/>"); <%-- 생성시간--%>
	lang.fn.set("views.search.form.fieldset.fADate", "<spring:message code='views.search.form.fieldset.fADate'/>"); <%-- 적용날짜--%>
	lang.fn.set("views.search.form.fieldset.fATime", "<spring:message code='views.search.form.fieldset.fATime'/>"); <%-- 적용시간--%>
	lang.fn.set("views.search.form.fieldset.fSysInfo", "<spring:message code='views.search.form.fieldset.fSysInfo'/>"); <%-- 시스템정보 --%>
	lang.fn.set("views.search.form.fieldset.fModelInfo", "<spring:message code='views.search.form.fieldset.fModelInfo'/>"); <%-- 모델정보 --%>
	lang.fn.set("views.search.form.fieldset.fStatusInfo", "<spring:message code='views.search.form.fieldset.fStatusInfo'/>"); <%-- 상태정보 --%>
	lang.fn.set("views.search.form.fieldset.fRInfo", "<spring:message code='views.search.form.fieldset.fRInfo'/>"); <%-- 녹취 정보 --%>
	lang.fn.set("views.search.form.fieldset.fSttResult", "<spring:message code='views.search.form.fieldset.fSttResult'/>"); <%-- STT 결과 --%>
	
	lang.fn.set("views.monitoring.mgName", "<spring:message code='views.monitoring.mgName'/>"); <%-- 모니터링용 --%>
	
	
	//statistics
	lang.fn.set("statistics.dashboard.title.daily", "<spring:message code='statistics.dashboard.title.daily'/>"); <%-- 일간녹취통계 --%>
	lang.fn.set("statistics.dashboard.title.axisInbound", "<spring:message code='statistics.dashboard.title.axisInbound'/>"); <%-- 인바운드 --%>
	lang.fn.set("statistics.dashboard.title.axisOutbound", "<spring:message code='statistics.dashboard.title.axisOutbound'/>"); <%-- 아웃바운드 --%>
	lang.fn.set("statistics.dashboard.title.week", "<spring:message code='statistics.dashboard.title.week'/>"); <%-- 주간 녹취 통계 --%>
	lang.fn.set("statistics.dashboard.title.callPerFile", "<spring:message code='statistics.dashboard.title.callPerFile'/>"); <%-- 콜 대 녹취 파일 통계 --%>
	lang.fn.set("statistics.dashboard.title.axisCall", "<spring:message code='statistics.dashboard.title.axisCall'/>"); <%-- 콜 건수 --%>
	lang.fn.set("statistics.dashboard.title.axisFile", "<spring:message code='statistics.dashboard.title.axisFile'/>"); <%-- 녹취 파일 수 --%>
	lang.fn.set("statistics.dashboard.title.server", "<spring:message code='statistics.dashboard.title.server'/>"); <%-- 서버별 통화 시간 --%>
	lang.fn.set("statistics.dashboard.title.axisTime", "<spring:message code='statistics.dashboard.title.axisTime'/>"); <%-- 녹취 시간(시간) --%>
	lang.fn.set("statistics.unit.time", "<spring:message code='statistics.unit.time'/>"); <%-- 시간 --%>

	//common
	<%-- lang.fn.set("message.title.alert", "<spring:message code='message.title.alert'/>"); 경고
	lang.fn.set("message.title.notifications", "<spring:message code='message.title.notifications'/>"); 알림
	lang.fn.set("message.btn.ok", "<spring:message code='message.btn.ok'/>"); 확인
	lang.fn.set("message.btn.add", '<spring:message.btn.add'/>"); 추가
	lang.fn.set("message.btn.modify", "<spring:message code='message.btn.modify'/>"); 수정
	lang.fn.set("message.btn.del", "<spring:message code='message.btn.del'/>"); 삭제
	lang.fn.set("message.btn.apply", "<spring:message code='message.btn.apply'/>"); 적용
	lang.fn.set("message.btn.clear", "<spring:message code='message.btn.clear'/>"); 폼 초기화
	lang.fn.set("message.btn.excelDownload", "<spring:message code='message.btn.excelDownload'/>"); 엑셀 다운로드
	lang.fn.set("message.btn.testMailSend", "<spring:message code='message.btn.testMailSend'/>"); 테스트 메일 발송
	lang.fn.set("message.btn.save", "<spring:message code='message.btn.save'/>"); 저장
	lang.fn.set("message.btn.restart", "<spring:message code='message.btn.restart'/>"); 재시작
	lang.fn.set("message.btn.upload", "<spring:message code='message.btn.upload'/>"); 업로드 --%>

	//search manage
	<%-- lang.fn.set("management.searchCustomize.alert.sameColumn", "<spring:message code='management.searchCustomize.alert.sameColumn'/>"); 중복된 칼럼이 있습니다.
	lang.fn.set("management.searchCustomize.alert.empty", "<spring:message code='management.searchCustomize.alert.empty'/>"); 적용할 데이터가 없습니다.
	lang.fn.set("management.searchCustomize.alert.itemTypeEmpty", "<spring:message code='management.searchCustomize.alert.itemTypeEmpty'/>"); 적용할 데이터가 없습니다.
	lang.fn.set("management.searchCustomize.alert.success", '<spring:management.searchCustomize.alert.success'/>"); 적용되었습니다.
	lang.fn.set("management.searchCustomize.alert.error", "<spring:message code='management.searchCustomize.alert.error'/>"); 적용 중 오류가 발생하였습니다. --%>





	// admin

	lang.fn.set("admin.alert.userManage1", 	"<spring:message code='admin.alert.userManage1'/>");
	lang.fn.set("admin.alert.userManage2",	"<spring:message code='admin.alert.userManage2'/>");
	lang.fn.set("admin.alert.userManage3",	"<spring:message code='admin.alert.userManage3'/>");
	lang.fn.set("admin.alert.userManage4",	"<spring:message code='admin.alert.userManage4'/>");
	lang.fn.set("admin.alert.userManage5",	"<spring:message code='admin.alert.userManage5'/>");
	lang.fn.set("admin.alert.userManage6",	"<spring:message code='admin.alert.userManage6'/>");
	lang.fn.set("admin.alert.userManage7",	"<spring:message code='admin.alert.userManage7'/>");
	lang.fn.set("admin.alert.userManage8",	"<spring:message code='admin.alert.userManage8'/>");
	
	lang.fn.set("admin.alert.recUser1", 	"<spring:message code='admin.alert.recUser1'/>");
	lang.fn.set("admin.alert.recUser2",		"<spring:message code='admin.alert.recUser2'/>");
	lang.fn.set("admin.alert.recUser3",		"<spring:message code='admin.alert.recUser3'/>");
	lang.fn.set("admin.alert.recUser4",		"<spring:message code='admin.alert.recUser4'/>");
	lang.fn.set("admin.alert.recUser5",		"<spring:message code='admin.alert.recUser5'/>");
	lang.fn.set("admin.alert.recUser6",		"<spring:message code='admin.alert.recUser6'/>");
	lang.fn.set("admin.alert.recUser7",		"<spring:message code='admin.alert.recUser7'/>");
	lang.fn.set("admin.alert.recUser8",		"<spring:message code='admin.alert.recUser8'/>");
	lang.fn.set("admin.alert.recUser9",		"<spring:message code='admin.alert.recUser9'/>");
	lang.fn.set("admin.alert.recUser10",	"<spring:message code='admin.alert.recUser10'/>");
	lang.fn.set("admin.alert.recUser11",	"<spring:message code='admin.alert.recUser11'/>");
	lang.fn.set("admin.alert.recUser12",	"<spring:message code='admin.alert.recUser12'/>");
	lang.fn.set("admin.alert.recUser13",	"<spring:message code='admin.alert.recUser13'/>");
	lang.fn.set("admin.alert.recUser14",	"<spring:message code='admin.alert.recUser14'/>");
	lang.fn.set("admin.alert.recUser15",	"<spring:message code='admin.alert.recUser15'/>");
	lang.fn.set("admin.alert.recUser16",	"<spring:message code='admin.alert.recUser16'/>");
	lang.fn.set("admin.alert.recUser17",	"<spring:message code='admin.alert.recUser17'/>");
	lang.fn.set("admin.alert.recUser18",	"<spring:message code='admin.alert.recUser18'/>");
	lang.fn.set("admin.alert.recUser19",	"<spring:message code='admin.alert.recUser19'/>");
	lang.fn.set("admin.alert.recUser20",	"<spring:message code='admin.alert.recUser20'/>");
	lang.fn.set("admin.alert.recUser21",	"<spring:message code='admin.alert.recUser21'/>");
	lang.fn.set("admin.alert.recUser22",	"<spring:message code='admin.alert.recUser22'/>");
	lang.fn.set("admin.alert.rec23",		"<spring:message code='admin.alert.rec23'/>");
	lang.fn.set("admin.alert.recUser23",	"<spring:message code='admin.alert.recUser23'/>");
	lang.fn.set("admin.alert.recUser24",	"<spring:message code='admin.alert.recUser24'/>");
	lang.fn.set("admin.alert.recUser25",	"<spring:message code='admin.alert.recUser25'/>");
	lang.fn.set("admin.alert.recUser26",	"<spring:message code='admin.alert.recUser26'/>");
	lang.fn.set("admin.alert.recUser27",	"<spring:message code='admin.alert.recUser27'/>");
	lang.fn.set("admin.alert.recUser28",	"<spring:message code='admin.alert.recUser28'/>");
	lang.fn.set("admin.alert.recUser29",	"<spring:message code='admin.alert.recUser29'/>");
	lang.fn.set("admin.alert.recUser30",	"<spring:message code='admin.alert.recUser30'/>");
	lang.fn.set("admin.alert.recUser31",	"<spring:message code='admin.alert.recUser31'/>");
	lang.fn.set("admin.alert.recUser32",	"<spring:message code='admin.alert.recUser32'/>");
	lang.fn.set("admin.alert.recUser33",	"<spring:message code='admin.alert.recUser33'/>");
	lang.fn.set("admin.alert.recUser34",	"<spring:message code='admin.alert.recUser34'/>");

	lang.fn.set("admin.alert.uiManage1", 	"<spring:message code='admin.alert.uiManage1'/>");
	lang.fn.set("admin.alert.uiManage2",	"<spring:message code='admin.alert.uiManage2'/>");
	lang.fn.set("admin.alert.uiManage3",	"<spring:message code='admin.alert.uiManage3'/>");
	lang.fn.set("admin.alert.uiManage4",	"<spring:message code='admin.alert.uiManage4'/>");
	lang.fn.set("admin.alert.uiManage5",	"<spring:message code='admin.alert.uiManage5'/>");
	lang.fn.set("admin.alert.uiManage6",	"<spring:message code='admin.alert.uiManage6'/>");
	lang.fn.set("admin.alert.uiManage7",	"<spring:message code='admin.alert.uiManage7'/>");
	lang.fn.set("admin.alert.uiManage8",	"<spring:message code='admin.alert.uiManage8'/>");
	lang.fn.set("admin.alert.uiManage9",	"<spring:message code='admin.alert.uiManage9'/>");
	lang.fn.set("admin.alert.uiManage10",	"<spring:message code='admin.alert.uiManage10'/>");
	lang.fn.set("admin.alert.uiManage11",	"<spring:message code='admin.alert.uiManage11'/>");
	lang.fn.set("admin.alert.uiManage12",	"<spring:message code='admin.alert.uiManage12'/>");
	lang.fn.set("admin.alert.uiManage13",	"<spring:message code='admin.alert.uiManage13'/>");
	lang.fn.set("admin.alert.uiManage14",	"<spring:message code='admin.alert.uiManage14'/>");
	lang.fn.set("admin.alert.uiManage15",	"<spring:message code='admin.alert.uiManage15'/>");

	lang.fn.set("admin.alert.switchManage1", 	"<spring:message code='admin.alert.switchManage1'/>");
	lang.fn.set("admin.alert.switchManage2",	"<spring:message code='admin.alert.switchManage2'/>");
	lang.fn.set("admin.alert.switchManage3",	"<spring:message code='admin.alert.switchManage3'/>");
	lang.fn.set("admin.alert.switchManage4",	"<spring:message code='admin.alert.switchManage4'/>");
	lang.fn.set("admin.alert.switchManage5",	"<spring:message code='admin.alert.switchManage5'/>");
	lang.fn.set("admin.alert.switchManage6",	"<spring:message code='admin.alert.switchManage6'/>");
	lang.fn.set("admin.alert.switchManage7",	"<spring:message code='admin.alert.switchManage7'/>");
	lang.fn.set("admin.alert.switchManage8",	"<spring:message code='admin.alert.switchManage8'/>");
	lang.fn.set("admin.alert.switchManage9",	"<spring:message code='admin.alert.switchManage9'/>");
	lang.fn.set("admin.alert.switchManage10",	"<spring:message code='admin.alert.switchManage10'/>");
	lang.fn.set("admin.alert.switchManage11",	"<spring:message code='admin.alert.switchManage11'/>");
	lang.fn.set("admin.alert.switchManage12",	"<spring:message code='admin.alert.switchManage12'/>");
	lang.fn.set("admin.alert.switchManage13",	"<spring:message code='admin.alert.switchManage13'/>");
	lang.fn.set("admin.alert.switchManage14",	"<spring:message code='admin.alert.switchManage14'/>");
	lang.fn.set("admin.alert.switchManage15",	"<spring:message code='admin.alert.switchManage15'/>");
	lang.fn.set("admin.alert.switchManage16",	"<spring:message code='admin.alert.switchManage16'/>");
	lang.fn.set("admin.alert.switchManage17",	"<spring:message code='admin.alert.switchManage17'/>");
	lang.fn.set("admin.alert.switchManage18",	"<spring:message code='admin.alert.switchManage18'/>");
	lang.fn.set("admin.alert.switchManage19",	"<spring:message code='admin.alert.switchManage19'/>");

	lang.fn.set("admin.alert.subNumberManage1", "<spring:message code='admin.alert.subNumberManage1'/>");
	lang.fn.set("admin.alert.subNumberManage2",	"<spring:message code='admin.alert.subNumberManage2'/>");
	lang.fn.set("admin.alert.subNumberManage3",	"<spring:message code='admin.alert.subNumberManage3'/>");
	lang.fn.set("admin.alert.subNumberManage4",	"<spring:message code='admin.alert.subNumberManage4'/>");
	lang.fn.set("admin.alert.subNumberManage5",	"<spring:message code='admin.alert.subNumberManage5'/>");
	lang.fn.set("admin.alert.subNumberManage6",	"<spring:message code='admin.alert.subNumberManage6'/>");
	lang.fn.set("admin.alert.subNumberManage7",	"<spring:message code='admin.alert.subNumberManage7'/>");
	lang.fn.set("admin.alert.subNumberManage8",	"<spring:message code='admin.alert.subNumberManage8'/>");
	lang.fn.set("admin.alert.subNumberManage9",	"<spring:message code='admin.alert.subNumberManage9'/>");
	lang.fn.set("admin.alert.subNumberManage10", "<spring:message code='admin.alert.subNumberManage10'/>");
	lang.fn.set("admin.alert.subNumberManage11", "<spring:message code='admin.alert.subNumberManage11'/>");
	lang.fn.set("admin.alert.subNumberManage12", "<spring:message code='admin.alert.subNumberManage12'/>");
	lang.fn.set("admin.alert.subNumberManage13", "<spring:message code='admin.alert.subNumberManage13'/>");
	
	lang.fn.set("admin.alert.phoneMapping1", "<spring:message code='admin.alert.phoneMapping1'/>");
	lang.fn.set("admin.alert.phoneMapping2",	"<spring:message code='admin.alert.phoneMapping2'/>");
	lang.fn.set("admin.alert.phoneMapping3",	"<spring:message code='admin.alert.phoneMapping3'/>");
	lang.fn.set("admin.alert.phoneMapping4",	"<spring:message code='admin.alert.phoneMapping4'/>");
	lang.fn.set("admin.alert.phoneMapping5",	"<spring:message code='admin.alert.phoneMapping5'/>");
	lang.fn.set("admin.alert.phoneMapping6",	"<spring:message code='admin.alert.phoneMapping6'/>");
	lang.fn.set("admin.alert.phoneMapping7",	"<spring:message code='admin.alert.phoneMapping7'/>");
	lang.fn.set("admin.alert.phoneMapping8",	"<spring:message code='admin.alert.phoneMapping8'/>");
	lang.fn.set("admin.alert.phoneMapping9",	"<spring:message code='admin.alert.phoneMapping9'/>");
	lang.fn.set("admin.alert.phoneMapping10", "<spring:message code='admin.alert.phoneMapping10'/>");
	lang.fn.set("admin.alert.phoneMapping11", "<spring:message code='admin.alert.phoneMapping11'/>");
	lang.fn.set("admin.alert.phoneMapping12", "<spring:message code='admin.alert.phoneMapping12'/>");
	lang.fn.set("admin.alert.phoneMapping13", "<spring:message code='admin.alert.phoneMapping13'/>");
	
	lang.fn.set("admin.alert.serverManage1", 	"<spring:message code='admin.alert.serverManage1'/>");
	lang.fn.set("admin.alert.serverManage2",	"<spring:message code='admin.alert.serverManage2'/>");
	lang.fn.set("admin.alert.serverManage3",	"<spring:message code='admin.alert.serverManage3'/>");
	lang.fn.set("admin.alert.serverManage4",	"<spring:message code='admin.alert.serverManage4'/>");
	lang.fn.set("admin.alert.serverManage5",	"<spring:message code='admin.alert.serverManage5'/>");
	lang.fn.set("admin.alert.serverManage6",	"<spring:message code='admin.alert.serverManage6'/>");
	lang.fn.set("admin.alert.serverManage7",	"<spring:message code='admin.alert.serverManage7'/>");
	lang.fn.set("admin.alert.serverManage8",	"<spring:message code='admin.alert.serverManage8'/>");
	lang.fn.set("admin.alert.serverManage9",	"<spring:message code='admin.alert.serverManage9'/>");
	lang.fn.set("admin.alert.serverManage10",	"<spring:message code='admin.alert.serverManage10'/>");
	lang.fn.set("admin.alert.serverManage11",	"<spring:message code='admin.alert.serverManage11'/>");
	lang.fn.set("admin.alert.serverManage12",	"<spring:message code='admin.alert.serverManage12'/>");
	lang.fn.set("admin.alert.serverManage13",	"<spring:message code='admin.alert.serverManage13'/>");
	lang.fn.set("admin.alert.serverManage14",	"<spring:message code='admin.alert.serverManage14'/>");
	lang.fn.set("admin.alert.serverManage15",	"<spring:message code='admin.alert.serverManage15'/>");
	lang.fn.set("admin.alert.serverManage16",	"<spring:message code='admin.alert.serverManage16'/>");
	lang.fn.set("admin.alert.serverManage17",	"<spring:message code='admin.alert.serverManage17'/>");
	lang.fn.set("admin.alert.serverManage18",	"<spring:message code='admin.alert.serverManage18'/>");
	lang.fn.set("admin.alert.serverManage19",	"<spring:message code='admin.alert.serverManage19'/>");
	lang.fn.set("admin.alert.serverManage20",	"<spring:message code='admin.alert.serverManage20'/>");
	lang.fn.set("admin.alert.serverManage21",	"<spring:message code='admin.alert.serverManage21'/>");

	lang.fn.set("admin.alert.searchManage1", "<spring:message code='admin.alert.searchManage1'/>");
	lang.fn.set("admin.alert.searchManage2", "<spring:message code='admin.alert.searchManage2'/>");
	lang.fn.set("admin.alert.searchManage3", "<spring:message code='admin.alert.searchManage3'/>");
	lang.fn.set("admin.alert.searchManage4", "<spring:message code='admin.alert.searchManage4'/>");
	lang.fn.set("admin.alert.searchManage5", "<spring:message code='admin.alert.searchManage5'/>");
	lang.fn.set("admin.alert.searchManage6", "<spring:message code='admin.alert.searchManage6'/>");

	lang.fn.set("admin.alert.queManage1", 	"<spring:message code='admin.alert.queManage1'/>");
	lang.fn.set("admin.alert.queManage2",	"<spring:message code='admin.alert.queManage2'/>");
	lang.fn.set("admin.alert.queManage3",	"<spring:message code='admin.alert.queManage3'/>");
	lang.fn.set("admin.alert.queManage4",	"<spring:message code='admin.alert.queManage4'/>");
	lang.fn.set("admin.alert.queManage5",	"<spring:message code='admin.alert.queManage5'/>");
	lang.fn.set("admin.alert.queManage6",	"<spring:message code='admin.alert.queManage6'/>");
	lang.fn.set("admin.alert.queManage7",	"<spring:message code='admin.alert.queManage7'/>");
	lang.fn.set("admin.alert.queManage8",	"<spring:message code='admin.alert.queManage8'/>");
	lang.fn.set("admin.alert.queManage9",	"<spring:message code='admin.alert.queManage9'/>");

	lang.fn.set("admin.alert.monitoringManage1", 	"<spring:message code='admin.alert.monitoringManage1'/>");
	lang.fn.set("admin.alert.monitoringManage2",	"<spring:message code='admin.alert.monitoringManage2'/>");
	lang.fn.set("admin.alert.monitoringManage3",	"<spring:message code='admin.alert.monitoringManage3'/>");
	lang.fn.set("admin.alert.monitoringManage4",	"<spring:message code='admin.alert.monitoringManage4'/>");
	lang.fn.set("admin.alert.monitoringManage5",	"<spring:message code='admin.alert.monitoringManage5'/>");
	lang.fn.set("admin.alert.monitoringManage6",	"<spring:message code='admin.alert.monitoringManage6'/>");
	lang.fn.set("admin.alert.monitoringManage7",	"<spring:message code='admin.alert.monitoringManage7'/>");
	lang.fn.set("admin.alert.monitoringManage8",	"<spring:message code='admin.alert.monitoringManage8'/>");
	lang.fn.set("admin.alert.monitoringManage9",	"<spring:message code='admin.alert.monitoringManage9'/>");
	lang.fn.set("admin.alert.monitoringManage10",	"<spring:message code='admin.alert.monitoringManage10'/>");
	lang.fn.set("admin.alert.monitoringManage11",	"<spring:message code='admin.alert.monitoringManage11'/>");
	lang.fn.set("admin.alert.monitoringManage12",	"<spring:message code='admin.alert.monitoringManage12'/>");

	lang.fn.set("admin.alert.logManage1", 	"<spring:message code='admin.alert.logManage1'/>");
	lang.fn.set("admin.alert.logManage2",	"<spring:message code='admin.alert.logManage2'/>");

	lang.fn.set("admin.alert.itemManage1", 	"<spring:message code='admin.alert.itemManage1'/>");
	lang.fn.set("admin.alert.itemManage2",	"<spring:message code='admin.alert.itemManage2'/>");
	lang.fn.set("admin.alert.itemManage3",	"<spring:message code='admin.alert.itemManage3'/>");
	lang.fn.set("admin.alert.itemManage4",	"<spring:message code='admin.alert.itemManage4'/>");
	lang.fn.set("admin.alert.itemManage5",	"<spring:message code='admin.alert.itemManage5'/>");
	lang.fn.set("admin.alert.itemManage6",	"<spring:message code='admin.alert.itemManage6'/>");
	lang.fn.set("admin.alert.itemManage7",	"<spring:message code='admin.alert.itemManage7'/>");
	lang.fn.set("admin.alert.itemManage8",	"<spring:message code='admin.alert.itemManage8'/>");
	lang.fn.set("admin.alert.itemManage9",	"<spring:message code='admin.alert.itemManage9'/>");
	
	lang.fn.set("admin.alert.etcConfig1", 	"<spring:message code='admin.alert.etcConfig1'/>");
	lang.fn.set("admin.alert.etcConfig2",	"<spring:message code='admin.alert.etcConfig2'/>");
	lang.fn.set("admin.alert.etcConfig3",	"<spring:message code='admin.alert.etcConfig3'/>");
	lang.fn.set("admin.alert.etcConfig4",	"<spring:message code='admin.alert.etcConfig4'/>");
	lang.fn.set("admin.alert.etcConfig5",	"<spring:message code='admin.alert.etcConfig5'/>");
	lang.fn.set("admin.alert.etcConfig6",	"<spring:message code='admin.alert.etcConfig6'/>");
	lang.fn.set("admin.alert.etcConfig7",	"<spring:message code='admin.alert.etcConfig7'/>");
	lang.fn.set("admin.alert.etcConfig8",	"<spring:message code='admin.alert.etcConfig8'/>");
	lang.fn.set("admin.alert.etcConfig9",	"<spring:message code='admin.alert.etcConfig9'/>");
	lang.fn.set("admin.alert.etcConfig10",	"<spring:message code='admin.alert.etcConfig10'/>");
	lang.fn.set("admin.alert.etcConfig11",	"<spring:message code='admin.alert.etcConfig11'/>");
	lang.fn.set("admin.alert.etcConfig12",	"<spring:message code='admin.alert.etcConfig12'/>");

	lang.fn.set("admin.alert.comboManage1", 	"<spring:message code='admin.alert.comboManage1'/>");
	lang.fn.set("admin.alert.comboManage2",	"<spring:message code='admin.alert.comboManage2'/>");
	lang.fn.set("admin.alert.comboManage3",	"<spring:message code='admin.alert.comboManage3'/>");
	lang.fn.set("admin.alert.comboManage4",	"<spring:message code='admin.alert.comboManage4'/>");
	lang.fn.set("admin.alert.comboManage5",	"<spring:message code='admin.alert.comboManage5'/>");
	lang.fn.set("admin.alert.comboManage6",	"<spring:message code='admin.alert.comboManage6'/>");
	lang.fn.set("admin.alert.comboManage7",	"<spring:message code='admin.alert.comboManage7'/>");

	lang.fn.set("admin.alert.chennelManage1", 	"<spring:message code='admin.alert.chennelManage1'/>");
	lang.fn.set("admin.alert.chennelManage2",	"<spring:message code='admin.alert.chennelManage2'/>");
	lang.fn.set("admin.alert.chennelManage3",	"<spring:message code='admin.alert.chennelManage3'/>");
	lang.fn.set("admin.alert.chennelManage4",	"<spring:message code='admin.alert.chennelManage4'/>");
	lang.fn.set("admin.alert.chennelManage5",	"<spring:message code='admin.alert.chennelManage5'/>");
	lang.fn.set("admin.alert.chennelManage6",	"<spring:message code='admin.alert.chennelManage6'/>");
	lang.fn.set("admin.alert.chennelManage7",	"<spring:message code='admin.alert.chennelManage7'/>");
	lang.fn.set("admin.alert.chennelManage8",	"<spring:message code='admin.alert.chennelManage8'/>");
	lang.fn.set("admin.alert.chennelManage9",	"<spring:message code='admin.alert.chennelManage9'/>");
	lang.fn.set("admin.alert.chennelManage10",	"<spring:message code='admin.alert.chennelManage10'/>");
	lang.fn.set("admin.alert.chennelManage11",	"<spring:message code='admin.alert.chennelManage11'/>");
	lang.fn.set("admin.alert.chennelManage12",	"<spring:message code='admin.alert.chennelManage12'/>");
	lang.fn.set("admin.alert.chennelManage13",	"<spring:message code='admin.alert.chennelManage13'/>");
	lang.fn.set("admin.alert.chennelManage14",	"<spring:message code='admin.alert.chennelManage14'/>");
	lang.fn.set("admin.alert.chennelManage15",	"<spring:message code='admin.alert.chennelManage15'/>");
	lang.fn.set("admin.alert.chennelManage16",	"<spring:message code='admin.alert.chennelManage16'/>");
	lang.fn.set("admin.alert.chennelManage17",	"<spring:message code='admin.alert.chennelManage17'/>");
	lang.fn.set("admin.alert.chennelManage18",	"<spring:message code='admin.alert.chennelManage18'/>");
	lang.fn.set("admin.alert.chennelManage19",	"<spring:message code='admin.alert.chennelManage19'/>");
	lang.fn.set("admin.alert.chennelManage20",	"<spring:message code='admin.alert.chennelManage20'/>");
	lang.fn.set("admin.alert.chennelManage21",	"<spring:message code='admin.alert.chennelManage21'/>");
	lang.fn.set("admin.alert.chennelManage22",	"<spring:message code='admin.alert.chennelManage22'/>");
	lang.fn.set("admin.alert.chennelManage23",	"<spring:message code='admin.alert.chennelManage23'/>");
	lang.fn.set("admin.alert.chennelManage24",	"<spring:message code='admin.alert.chennelManage24'/>");
	lang.fn.set("admin.alert.chennelManage25",	"<spring:message code='admin.alert.chennelManage25'/>");
	lang.fn.set("admin.alert.chennelManage26",	"<spring:message code='admin.alert.chennelManage26'/>");
	lang.fn.set("admin.alert.chennelManage27",	"<spring:message code='admin.alert.chennelManage27'/>");
	lang.fn.set("admin.alert.chennelManage28",	"<spring:message code='admin.alert.chennelManage28'/>");
	lang.fn.set("admin.alert.chennelManage29",	"<spring:message code='admin.alert.chennelManage29'/>");
	lang.fn.set("admin.alert.chennelManage30",	"<spring:message code='admin.alert.chennelManage30'/>");

	lang.fn.set("admin.alert.approveManage1", 	"<spring:message code='admin.alert.approveManage1'/>");
	lang.fn.set("admin.alert.approveManage2",	"<spring:message code='admin.alert.approveManage2'/>");
	lang.fn.set("admin.alert.approveManage3",	"<spring:message code='admin.alert.approveManage3'/>");
	lang.fn.set("admin.alert.approveManage4",	"<spring:message code='admin.alert.approveManage4'/>");
	lang.fn.set("admin.alert.approveManage5",	"<spring:message code='admin.alert.approveManage5'/>");
	lang.fn.set("admin.alert.approveManage6",	"<spring:message code='admin.alert.approveManage6'/>");
	lang.fn.set("admin.alert.approveManage7",	"<spring:message code='admin.alert.approveManage7'/>");
	lang.fn.set("admin.alert.approveManage8",	"<spring:message code='admin.alert.approveManage8'/>");
	lang.fn.set("admin.alert.approveManage9",	"<spring:message code='admin.alert.approveManage9'/>");
	lang.fn.set("admin.alert.approveManage10",	"<spring:message code='admin.alert.approveManage10'/>");
	lang.fn.set("admin.alert.approveManage11",	"<spring:message code='admin.alert.approveManage11'/>");
	lang.fn.set("admin.alert.approveManage12",	"<spring:message code='admin.alert.approveManage12'/>");
	lang.fn.set("admin.alert.approveManage13",	"<spring:message code='admin.alert.approveManage13'/>");
	lang.fn.set("admin.alert.approveManage14",	"<spring:message code='admin.alert.approveManage14'/>");
	lang.fn.set("admin.alert.approveManage15",	"<spring:message code='admin.alert.approveManage15'/>");
	lang.fn.set("admin.alert.approveManage16",	"<spring:message code='admin.alert.approveManage16'/>");
	lang.fn.set("admin.alert.approveManage17",	"<spring:message code='admin.alert.approveManage17'/>");
	lang.fn.set("admin.alert.approveManage18",	"<spring:message code='admin.alert.approveManage18'/>");
	lang.fn.set("admin.alert.approveManage19",	"<spring:message code='admin.alert.approveManage19'/>");
	lang.fn.set("admin.alert.approveManage20",	"<spring:message code='admin.alert.approveManage20'/>");
	lang.fn.set("admin.alert.approveManage21",	"<spring:message code='admin.alert.approveManage21'/>");
	lang.fn.set("admin.alert.attrManage1", 	"<spring:message code='admin.alert.attrManage1'/>");
	lang.fn.set("admin.alert.attrManage2",	"<spring:message code='admin.alert.attrManage2'/>");
	lang.fn.set("admin.alert.attrManage3",	"<spring:message code='admin.alert.attrManage3'/>");
	lang.fn.set("admin.alert.attrManage4",	"<spring:message code='admin.alert.attrManage4'/>");
	lang.fn.set("admin.alert.attrManage5",	"<spring:message code='admin.alert.attrManage5'/>");
	lang.fn.set("admin.alert.attrManage6",	"<spring:message code='admin.alert.attrManage6'/>");
	lang.fn.set("admin.alert.attrManage7",	"<spring:message code='admin.alert.attrManage7'/>");
	lang.fn.set("admin.alert.attrManage8",	"<spring:message code='admin.alert.attrManage8'/>");
	lang.fn.set("admin.alert.attrManage9",	"<spring:message code='admin.alert.attrManage9'/>");
	lang.fn.set("admin.alert.attrManage10",	"<spring:message code='admin.alert.attrManage10'/>");
	lang.fn.set("admin.alert.attrManage11",	"<spring:message code='admin.alert.attrManage11'/>");
	lang.fn.set("admin.alert.attrManage12",	"<spring:message code='admin.alert.attrManage12'/>");
	lang.fn.set("admin.alert.attrManage13",	"<spring:message code='admin.alert.attrManage13'/>");
	lang.fn.set("admin.alert.attrManage14",	"<spring:message code='admin.alert.attrManage14'/>");
	lang.fn.set("admin.alert.attrManage15",	"<spring:message code='admin.alert.attrManage15'/>");
	lang.fn.set("admin.alert.attrManage16",	"<spring:message code='admin.alert.attrManage16'/>");
	lang.fn.set("admin.alert.authyManage1", 	"<spring:message code='admin.alert.authyManage1'/>");
	lang.fn.set("admin.alert.authyManage2",	"<spring:message code='admin.alert.authyManage2'/>");
	lang.fn.set("admin.alert.authyManage3",	"<spring:message code='admin.alert.authyManage3'/>");
	lang.fn.set("admin.alert.authyManage4",	"<spring:message code='admin.alert.authyManage4'/>");
	lang.fn.set("admin.alert.authyManage5",	"<spring:message code='admin.alert.authyManage5'/>");
	lang.fn.set("admin.alert.authyManage6",	"<spring:message code='admin.alert.authyManage6'/>");
	lang.fn.set("admin.alert.authyManage7",	"<spring:message code='admin.alert.authyManage7'/>");
	lang.fn.set("admin.alert.authyManage8",	"<spring:message code='admin.alert.authyManage8'/>");
	lang.fn.set("admin.alert.authyManage9",	"<spring:message code='admin.alert.authyManage9'/>");
	lang.fn.set("admin.alert.authyManage10",	"<spring:message code='admin.alert.authyManage10'/>");
	lang.fn.set("admin.alert.authyManage11",	"<spring:message code='admin.alert.authyManage11'/>");
	lang.fn.set("admin.alert.authyManage12",	"<spring:message code='admin.alert.authyManage12'/>");
	lang.fn.set("admin.alert.authyManage13",	"<spring:message code='admin.alert.authyManage13'/>");
	lang.fn.set("admin.alert.authyManage14",	"<spring:message code='admin.alert.authyManage14'/>");
	lang.fn.set("admin.alert.authyManage15",	"<spring:message code='admin.alert.authyManage15'/>");
	lang.fn.set("admin.alert.authyManage16",	"<spring:message code='admin.alert.authyManage16'/>");
	lang.fn.set("admin.alert.authyManage17",	"<spring:message code='admin.alert.authyManage17'/>");
	lang.fn.set("admin.alert.authyManage18",	"<spring:message code='admin.alert.authyManage18'/>");
	lang.fn.set("admin.alert.authyManage19",	"<spring:message code='admin.alert.authyManage19'/>");
	lang.fn.set("admin.alert.authyManage20",	"<spring:message code='admin.alert.authyManage20'/>");


	lang.fn.set("admin.alert.allowableRangeManage1",	"<spring:message code='admin.alert.allowableRangeManage1'/>");
	lang.fn.set("admin.alert.allowableRangeManage2",	"<spring:message code='admin.alert.allowableRangeManage2'/>");
	lang.fn.set("admin.alert.allowableRangeManage3",	"<spring:message code='admin.alert.allowableRangeManage3'/>");
	lang.fn.set("admin.alert.allowableRangeManage4",	"<spring:message code='admin.alert.allowableRangeManage4'/>");
	lang.fn.set("admin.alert.allowableRangeManage5",	"<spring:message code='admin.alert.allowableRangeManage5'/>");
	lang.fn.set("admin.alert.allowableRangeManage6",	"<spring:message code='admin.alert.allowableRangeManage6'/>");
	lang.fn.set("admin.alert.allowableRangeManage7",	"<spring:message code='admin.alert.allowableRangeManage7'/>");
	lang.fn.set("admin.alert.allowableRangeManage8",	"<spring:message code='admin.alert.allowableRangeManage8'/>");
	lang.fn.set("admin.alert.allowableRangeManage10",	"<spring:message code='admin.alert.allowableRangeManage10'/>");
	lang.fn.set("admin.alert.allowableRangeManage11",	"<spring:message code='admin.alert.allowableRangeManage11'/>");
	lang.fn.set("admin.alert.allowableRangeManage12",	"<spring:message code='admin.alert.allowableRangeManage12'/>");
	lang.fn.set("admin.alert.allowableRangeManage13",	"<spring:message code='admin.alert.allowableRangeManage13'/>");
	lang.fn.set("admin.alert.allowableRangeManage14",	"<spring:message code='admin.alert.allowableRangeManage14'/>");
	lang.fn.set("admin.alert.allowableRangeManage15",	"<spring:message code='admin.alert.allowableRangeManage15'/>");
	lang.fn.set("admin.alert.allowableRangeManage16",	"<spring:message code='admin.alert.allowableRangeManage16'/>");
	
	
	
	
	lang.fn.set("admin.label.mobileUsage",	"<spring:message code='admin.label.mobileUsage'/>");

	<%-- search - alert msg  //이거 for문으로 묶어보려고 했는데 spring:message쪽에서 에러가... --%>
	lang.fn.set("views.search.alert.msg1",	"<spring:message code='views.search.alert.msg1'/>");
	lang.fn.set("views.search.alert.msg2",	"<spring:message code='views.search.alert.msg2'/>");
	lang.fn.set("views.search.alert.msg3",	"<spring:message code='views.search.alert.msg3'/>");
	lang.fn.set("views.search.alert.msg4",	"<spring:message code='views.search.alert.msg4'/>");
	lang.fn.set("views.search.alert.msg5",	"<spring:message code='views.search.alert.msg5'/>");
	lang.fn.set("views.search.alert.msg6",	"<spring:message code='views.search.alert.msg6'/>");
	lang.fn.set("views.search.alert.msg7",	"<spring:message code='views.search.alert.msg7'/>");
	lang.fn.set("views.search.alert.msg8",	"<spring:message code='views.search.alert.msg8'/>");
	lang.fn.set("views.search.alert.msg9",	"<spring:message code='views.search.alert.msg9'/>");
	lang.fn.set("views.search.alert.msg10",	"<spring:message code='views.search.alert.msg10'/>");
	lang.fn.set("views.search.alert.msg11",	"<spring:message code='views.search.alert.msg11'/>");
	lang.fn.set("views.search.alert.msg12",	"<spring:message code='views.search.alert.msg12'/>");
	lang.fn.set("views.search.alert.msg13",	"<spring:message code='views.search.alert.msg13'/>");
	lang.fn.set("views.search.alert.msg14",	"<spring:message code='views.search.alert.msg14'/>");
	lang.fn.set("views.search.alert.msg15",	"<spring:message code='views.search.alert.msg15'/>");
	lang.fn.set("views.search.alert.msg16",	"<spring:message code='views.search.alert.msg16'/>");
	lang.fn.set("views.search.alert.msg17",	"<spring:message code='views.search.alert.msg17'/>");
	lang.fn.set("views.search.alert.msg18",	"<spring:message code='views.search.alert.msg18'/>");
	lang.fn.set("views.search.alert.msg19",	"<spring:message code='views.search.alert.msg19'/>");
	lang.fn.set("views.search.alert.msg20",	"<spring:message code='views.search.alert.msg20'/>");
	lang.fn.set("views.search.alert.msg21",	"<spring:message code='views.search.alert.msg21'/>");
	lang.fn.set("views.search.alert.msg22",	"<spring:message code='views.search.alert.msg22'/>");
	lang.fn.set("views.search.alert.msg23",	"<spring:message code='views.search.alert.msg23'/>");
	lang.fn.set("views.search.alert.msg24",	"<spring:message code='views.search.alert.msg24'/>");
	lang.fn.set("views.search.alert.msg25",	"<spring:message code='views.search.alert.msg25'/>");
	lang.fn.set("views.search.alert.msg26",	"<spring:message code='views.search.alert.msg26'/>");
	lang.fn.set("views.search.alert.msg27",	"<spring:message code='views.search.alert.msg27'/>");
	lang.fn.set("views.search.alert.msg28",	"<spring:message code='views.search.alert.msg28'/>");
	lang.fn.set("views.search.alert.msg29",	"<spring:message code='views.search.alert.msg29'/>");
	lang.fn.set("views.search.alert.msg30",	"<spring:message code='views.search.alert.msg30'/>");
	lang.fn.set("views.search.alert.msg31",	"<spring:message code='views.search.alert.msg31'/>");
	lang.fn.set("views.search.alert.msg32",	"<spring:message code='views.search.alert.msg32'/>");
	lang.fn.set("views.search.alert.msg33",	"<spring:message code='views.search.alert.msg33'/>");
	lang.fn.set("views.search.alert.msg34",	"<spring:message code='views.search.alert.msg34'/>");
	lang.fn.set("views.search.alert.msg35",	"<spring:message code='views.search.alert.msg35'/>");
	lang.fn.set("views.search.alert.msg36",	"<spring:message code='views.search.alert.msg36'/>");
	lang.fn.set("views.search.alert.msg37",	"<spring:message code='views.search.alert.msg37'/>");
	lang.fn.set("views.search.alert.msg38",	"<spring:message code='views.search.alert.msg38'/>");
	lang.fn.set("views.search.alert.msg39",	"<spring:message code='views.search.alert.msg39'/>");
	lang.fn.set("views.search.alert.msg40",	"<spring:message code='views.search.alert.msg40'/>");
	lang.fn.set("views.search.alert.msg41",	"<spring:message code='views.search.alert.msg41'/>");
	lang.fn.set("views.search.alert.msg42",	"<spring:message code='views.search.alert.msg42'/>");
	lang.fn.set("views.search.alert.msg43",	"<spring:message code='views.search.alert.msg43'/>");
	lang.fn.set("views.search.alert.msg44",	"<spring:message code='views.search.alert.msg44'/>");
	lang.fn.set("views.search.alert.msg45",	"<spring:message code='views.search.alert.msg45'/>");
	lang.fn.set("views.search.alert.msg46",	"<spring:message code='views.search.alert.msg46'/>");
	lang.fn.set("views.search.alert.msg47",	"<spring:message code='views.search.alert.msg47'/>");
	lang.fn.set("views.search.alert.msg48",	"<spring:message code='views.search.alert.msg48'/>");
	lang.fn.set("views.search.alert.msg49",	"<spring:message code='views.search.alert.msg49'/>");
	lang.fn.set("views.search.alert.msg50",	"<spring:message code='views.search.alert.msg50'/>");
	lang.fn.set("views.search.alert.msg51",	"<spring:message code='views.search.alert.msg51'/>");
	lang.fn.set("views.search.alert.msg52",	"<spring:message code='views.search.alert.msg52'/>");
	lang.fn.set("views.search.alert.msg53",	"<spring:message code='views.search.alert.msg53'/>");
	lang.fn.set("views.search.alert.msg54",	"<spring:message code='views.search.alert.msg54'/>");
	lang.fn.set("views.search.alert.msg55",	"<spring:message code='views.search.alert.msg55'/>");
	lang.fn.set("views.search.alert.msg56",	"<spring:message code='views.search.alert.msg56'/>");
	lang.fn.set("views.search.alert.msg57",	"<spring:message code='views.search.alert.msg57'/>");
	lang.fn.set("views.search.alert.msg58",	"<spring:message code='views.search.alert.msg58'/>");
	lang.fn.set("views.search.alert.msg59",	"<spring:message code='views.search.alert.msg59'/>");
	lang.fn.set("views.search.alert.msg60",	"<spring:message code='views.search.alert.msg60'/>");
	lang.fn.set("views.search.alert.msg61",	"<spring:message code='views.search.alert.msg61'/>");
	lang.fn.set("views.search.alert.msg62",	"<spring:message code='views.search.alert.msg62'/>");
	lang.fn.set("views.search.alert.msg63",	"<spring:message code='views.search.alert.msg63'/>");
	lang.fn.set("views.search.alert.msg64",	"<spring:message code='views.search.alert.msg64'/>");
	lang.fn.set("views.search.alert.msg65",	"<spring:message code='views.search.alert.msg65'/>");
	lang.fn.set("views.search.alert.msg66",	"<spring:message code='views.search.alert.msg66'/>");
	lang.fn.set("views.search.alert.msg67",	"<spring:message code='views.search.alert.msg67'/>");
	lang.fn.set("views.search.alert.msg68",	"<spring:message code='views.search.alert.msg68'/>");
	lang.fn.set("views.search.alert.msg69",	"<spring:message code='views.search.alert.msg69'/>");
	lang.fn.set("views.search.alert.msg70",	"<spring:message code='views.search.alert.msg70'/>");
	lang.fn.set("views.search.alert.msg71",	"<spring:message code='views.search.alert.msg71'/>");

	lang.fn.set("views.search.alert.msg79",	"<spring:message code='views.search.alert.msg79'/>");
	lang.fn.set("views.search.alert.msg80",	"<spring:message code='views.search.alert.msg80'/>");
	
	lang.fn.set("views.search.alert.msg81",	"<spring:message code='views.search.alert.msg81'/>");
	lang.fn.set("views.search.alert.msg82",	"<spring:message code='views.search.alert.msg82'/>");
	lang.fn.set("views.search.alert.msg83",	"<spring:message code='views.search.alert.msg83'/>");
	lang.fn.set("views.search.alert.msg84",	"<spring:message code='views.search.alert.msg84'/>");
	lang.fn.set("views.search.alert.msg85",	"<spring:message code='views.search.alert.msg85'/>");
	lang.fn.set("views.search.alert.msg86",	"<spring:message code='views.search.alert.msg86'/>");
	lang.fn.set("views.search.alert.msg87",	"<spring:message code='views.search.alert.msg87'/>");
	lang.fn.set("views.search.alert.msg88",	"<spring:message code='views.search.alert.msg88'/>");
	lang.fn.set("views.search.alert.msg89",	"<spring:message code='views.search.alert.msg89'/>");
	lang.fn.set("views.search.alert.msg90",	"<spring:message code='views.search.alert.msg90'/>");

	lang.fn.set("views.search.alert.msg91",	"<spring:message code='views.search.alert.msg91'/>");
	
	lang.fn.set("common.alert.noPlayerTags","<spring:message code='common.alert.noPlayerTags'/>");
	lang.fn.set("views.search.alert.msg92",	"<spring:message code='views.search.alert.msg92'/>");/* 파일삭제 */
	lang.fn.set("views.search.alert.msg93",	"<spring:message code='views.search.alert.msg93'/>");/* 선택파일 삭제 */
	lang.fn.set("views.search.alert.msg94",	"<spring:message code='views.search.alert.msg94'/>");/* 파일삭제 메뉴 */
	lang.fn.set("views.search.alert.msg95",	"<spring:message code='views.search.alert.msg95'/>");/* 삭제 유형 */
	lang.fn.set("views.search.alert.msg96",	"<spring:message code='views.search.alert.msg96'/>");/* 데이터 비활성 */
	lang.fn.set("views.search.alert.msg97",	"<spring:message code='views.search.alert.msg97'/>");/* 파일/데이터 삭제 */
	lang.fn.set("views.search.alert.msg98",	"<spring:message code='views.search.alert.msg98'/>");/* 삭제 */
	
	lang.fn.set("views.search.alert.msg99",		"<spring:message code='views.search.alert.msg99'/>");/* 현재 선택하신 녹취파일을 삭제하시겠습니까? */
	lang.fn.set("views.search.alert.msg100",	"<spring:message code='views.search.alert.msg100'/>");/* 현재 선택하신 녹취이력 또한 삭제하시겠습니까? */
	lang.fn.set("views.search.alert.msg101",	"<spring:message code='views.search.alert.msg101'/>");/* 비활성화할 대상이 없습니다. */
	lang.fn.set("views.search.alert.msg102",	"<spring:message code='views.search.alert.msg102'/>");/* 현재 조회하신 조건의 모든 녹취이력을 비활성시키겠습니까? */
	lang.fn.set("views.search.alert.msg103",	"<spring:message code='views.search.alert.msg103'/>");/* 현재 선택하신 녹취이력을 비활성시키겠습니까? */
	lang.fn.set("views.search.alert.msg104",	"<spring:message code='views.search.alert.msg104'/>");/* 비활성에 성공하였습니다. */
	lang.fn.set("views.search.alert.msg105",	"<spring:message code='views.search.alert.msg105'/>");/* 비활성에 실패하였습니다. */
	
	lang.fn.set("views.search.alert.msg106",	"<spring:message code='views.search.alert.msg106'/>");/* CSV 불러오기 */
	lang.fn.set("views.search.alert.msg107",	"<spring:message code='views.search.alert.msg107'/>");/* 파일을 선택하세요. */
	lang.fn.set("views.search.alert.msg108",	"<spring:message code='views.search.alert.msg108'/>");/* 파일찾기 */
	lang.fn.set("views.search.alert.msg109",	"<spring:message code='views.search.alert.msg109'/>");/* 양식받기 */
	lang.fn.set("views.search.alert.msg110",	"<spring:message code='views.search.alert.msg110'/>");/* 업로드 */
	lang.fn.set("views.search.alert.msg111",	"<spring:message code='views.search.alert.msg111'/>");/* 업로드 하는 파일은 csv 파일이 아닙니다. */
	lang.fn.set("views.search.alert.msg112",	"<spring:message code='views.search.alert.msg112'/>");/* 데이터가 없습니다. */
	lang.fn.set("views.search.alert.msg113",	"<spring:message code='views.search.alert.msg113'/>");/* 업로드 결과 */
	lang.fn.set("views.search.alert.msg114",	"<spring:message code='views.search.alert.msg114'/>");/* 검색 */
	lang.fn.set("views.search.alert.msg115",	"<spring:message code='views.search.alert.msg115'/>");/* 전체 */
	lang.fn.set("views.search.alert.msg116",	"<spring:message code='views.search.alert.msg116'/>");/* 유효 */
	lang.fn.set("views.search.alert.msg117",	"<spring:message code='views.search.alert.msg117'/>");/* 오류 */
	
	lang.fn.set("views.search.alert.listenPeriod1",	"<spring:message code='views.search.alert.listenPeriod1'/>");
	lang.fn.set("views.search.alert.listenPeriod2",	"<spring:message code='views.search.alert.listenPeriod2'/>");
	
	lang.fn.set("views.search.alert.noDownTarget",	"<spring:message code='views.search.alert.noDownTarget'/>");/* 다운로드할 대상이 없습니다. */
	lang.fn.set("views.search.alert.failFullDown2",	"<spring:message code='views.search.alert.failFullDown2'/>");/* 녹취 일괄 다운로드에 실패하였습니다. */
	lang.fn.set("views.search.alert.hours",	"<spring:message code='views.search.alert.hours'/>");/* 시간 */
	lang.fn.set("views.search.alert.minutes",	"<spring:message code='views.search.alert.minutes'/>");/* 분 */
	lang.fn.set("views.search.alert.second",	"<spring:message code='views.search.alert.second'/>");/* 초 */
	lang.fn.set("views.search.alert.comfrimFullDown",	"<spring:message code='views.search.alert.comfrimFullDown'/>");/* 현재 조회하신 조건의 모든 녹취파일을 다운로드하시겠습니까? */
	lang.fn.set("views.search.alert.caution2GB",	"<spring:message code='views.search.alert.caution2GB'/>");/* ***파일용량이 2GB이상일 경우 다운로드 할 수 없습니다*** */
	lang.fn.set("views.search.alert.selectCallTime",	"<spring:message code='views.search.alert.selectCallTime'/>");/* 선택한 파일의 총 통화시간                  :  */
	lang.fn.set("views.search.alert.downloadableCallTime",	"<spring:message code='views.search.alert.downloadableCallTime'/>");/* 다운로드 가능한 총 통화시간(예상)    : 250시간 */
	lang.fn.set("views.search.alert.completedZip",	"<spring:message code='views.search.alert.completedZip'/>");/* 압축파일 생성이 완료되었습니다. */
	lang.fn.set("views.search.alert.zipOver2GB",	"<spring:message code='views.search.alert.zipOver2GB'/>");/* 압축파일 크기가 2GB를 초과하여 다운로드에 실패하였습니다. */
	lang.fn.set("views.search.alert.successFileBefore",	"<spring:message code='views.search.alert.successFileBefore'/>");/* 2GB이전까지 압축 성공한 파일 개수:  */
	lang.fn.set("views.search.alert.zipProgress",	"<spring:message code='views.search.alert.zipProgress'/>");/* 압축파일을 생성하는 중입니다.(진행률:  */
	
	lang.fn.set("views.search.alert.selectGrid",	"<spring:message code='views.search.alert.selectGrid'/>");/* 복사할 범위를 선택해 주세요. */
	
	lang.fn.set("views.search.alert.noSelectRec",	"<spring:message code='views.search.alert.noSelectRec'/>");/* 선택된 녹취 파일이 없습니다. */
	lang.fn.set("views.search.alert.directUpload",	"<spring:message code='views.search.alert.directUpload'/>");/* 선택된 녹취 파일을 즉시 업로드 하시겠습니까? */
			
	lang.fn.set("views.search.alert.nameCheck",	"<spring:message code='views.search.alert.nameCheck'/>");/* 자 이상 입력해 주십시오 */		
	
	<%-- monitoring - alert msg --%>
	lang.fn.set("monitoring.alert.msg1",	"<spring:message code='monitoring.alert.msg1'/>");
	lang.fn.set("monitoring.alert.msg2",	"<spring:message code='monitoring.alert.msg2'/>");
	lang.fn.set("monitoring.alert.msg3",	"<spring:message code='monitoring.alert.msg3'/>");
	lang.fn.set("monitoring.alert.msg4",	"<spring:message code='monitoring.alert.msg4'/>");
	lang.fn.set("monitoring.alert.msg5",	"<spring:message code='monitoring.alert.msg5'/>");
	lang.fn.set("monitoring.alert.msg6",	"<spring:message code='monitoring.alert.msg6'/>");
	lang.fn.set("monitoring.alert.msg7",	"<spring:message code='monitoring.alert.msg7'/>");
	lang.fn.set("monitoring.alert.msg8",	"<spring:message code='monitoring.alert.msg8'/>");
	lang.fn.set("monitoring.alert.msg9",	"<spring:message code='monitoring.alert.msg9'/>");
	lang.fn.set("monitoring.alert.msg10",	"<spring:message code='monitoring.alert.msg10'/>");
	lang.fn.set("monitoring.alert.msg11",	"<spring:message code='monitoring.alert.msg11'/>");
	lang.fn.set("monitoring.alert.msg12",	"<spring:message code='monitoring.alert.msg12'/>");
	lang.fn.set("monitoring.alert.msg13",	"<spring:message code='monitoring.alert.msg13'/>");
	lang.fn.set("monitoring.alert.msg14",	"<spring:message code='monitoring.alert.msg14'/>");
	lang.fn.set("monitoring.alert.msg15",	"<spring:message code='monitoring.alert.msg15'/>");
	lang.fn.set("monitoring.alert.msg16",	"<spring:message code='monitoring.alert.msg16'/>");
	lang.fn.set("monitoring.alert.msg17",	"<spring:message code='monitoring.alert.msg17'/>");
	lang.fn.set("monitoring.alert.msg18",	"<spring:message code='monitoring.alert.msg18'/>");
	lang.fn.set("monitoring.alert.msg19",	"<spring:message code='monitoring.alert.msg19'/>");
	lang.fn.set("monitoring.alert.msg20",	"<spring:message code='monitoring.alert.msg20'/>");
	lang.fn.set("monitoring.alert.msg21",	"<spring:message code='monitoring.alert.msg21'/>");
	lang.fn.set("monitoring.alert.msg22",	"<spring:message code='monitoring.alert.msg22'/>");
	lang.fn.set("monitoring.alert.msg23",	"<spring:message code='monitoring.alert.msg23'/>");

	<%-- statistics - alert msg --%>
	lang.fn.set("statistics.js.alert.msg1",	"<spring:message code='statistics.js.alert.msg1'/>");
	lang.fn.set("statistics.js.alert.msg2",	"<spring:message code='statistics.js.alert.msg2'/>");
	lang.fn.set("statistics.js.alert.msg3",	"<spring:message code='statistics.js.alert.msg3'/>");
	lang.fn.set("statistics.js.alert.msg4",	"<spring:message code='statistics.js.alert.msg4'/>");
	lang.fn.set("statistics.js.alert.msg5",	"<spring:message code='statistics.js.alert.msg5'/>");
	lang.fn.set("statistics.js.alert.msg6",	"<spring:message code='statistics.js.alert.msg6'/>");
	lang.fn.set("statistics.js.alert.msg7",	"<spring:message code='statistics.js.alert.msg7'/>");
	lang.fn.set("statistics.js.alert.msg8",	"<spring:message code='statistics.js.alert.msg8'/>");
	lang.fn.set("statistics.js.alert.msg9",	"<spring:message code='statistics.js.alert.msg9'/>");
	lang.fn.set("statistics.js.alert.msg10",	"<spring:message code='statistics.js.alert.msg10'/>");
	lang.fn.set("statistics.js.alert.msg11",	"<spring:message code='statistics.js.alert.msg11'/>");
	lang.fn.set("statistics.js.alert.msg12",	"<spring:message code='statistics.js.alert.msg12'/>");
	lang.fn.set("statistics.js.alert.msg13",	"<spring:message code='statistics.js.alert.msg13'/>");
	lang.fn.set("statistics.js.alert.msg14",	"<spring:message code='statistics.js.alert.msg14'/>");
	lang.fn.set("statistics.js.alert.msg15",	"<spring:message code='statistics.js.alert.msg15'/>");
	lang.fn.set("statistics.js.alert.msg16",	"<spring:message code='statistics.js.alert.msg16'/>");
	lang.fn.set("statistics.js.alert.msg17",	"<spring:message code='statistics.js.alert.msg17'/>");
	lang.fn.set("statistics.js.alert.msg18",	"<spring:message code='statistics.js.alert.msg18'/>");
	lang.fn.set("statistics.js.alert.msg19",	"<spring:message code='statistics.js.alert.msg19'/>");
	lang.fn.set("statistics.js.alert.msg20",	"<spring:message code='statistics.js.alert.msg20'/>");
	lang.fn.set("statistics.js.alert.msg21",	"<spring:message code='statistics.js.alert.msg21'/>");
	lang.fn.set("statistics.js.alert.msg22",	"<spring:message code='statistics.js.alert.msg22'/>");
	lang.fn.set("statistics.js.alert.msg23",	"<spring:message code='statistics.js.alert.msg23'/>");
	lang.fn.set("statistics.js.alert.msg24",	"<spring:message code='statistics.js.alert.msg24'/>");
	lang.fn.set("statistics.js.alert.msg25",	"<spring:message code='statistics.js.alert.msg25'/>");
	lang.fn.set("statistics.js.alert.msg26",	"<spring:message code='statistics.js.alert.msg26'/>");
	lang.fn.set("statistics.js.alert.msg27",	"<spring:message code='statistics.js.alert.msg27'/>");
	lang.fn.set("statistics.js.alert.msg28",	"<spring:message code='statistics.js.alert.msg28'/>");
	lang.fn.set("statistics.js.alert.msg29",	"<spring:message code='statistics.js.alert.msg29'/>");
	lang.fn.set("statistics.js.alert.msg30",	"<spring:message code='statistics.js.alert.msg30'/>");
	lang.fn.set("statistics.js.alert.msg31",	"<spring:message code='statistics.js.alert.msg31'/>");
	lang.fn.set("statistics.js.alert.msg32",	"<spring:message code='statistics.js.alert.msg32'/>");
	lang.fn.set("statistics.js.alert.msg33",	"<spring:message code='statistics.js.alert.msg33'/>");
	lang.fn.set("statistics.js.alert.msg34",	"<spring:message code='statistics.js.alert.msg34'/>");
	lang.fn.set("statistics.js.alert.msg35",	"<spring:message code='statistics.js.alert.msg35'/>");
	lang.fn.set("statistics.js.alert.msg36",	"<spring:message code='statistics.js.alert.msg36'/>");
	lang.fn.set("statistics.js.alert.msg37",	"<spring:message code='statistics.js.alert.msg37'/>");
	lang.fn.set("statistics.js.alert.msg38",	"<spring:message code='statistics.js.alert.msg38'/>");
	lang.fn.set("statistics.js.alert.msg39",	"<spring:message code='statistics.js.alert.msg39'/>");
	lang.fn.set("statistics.js.alert.msg40",	"<spring:message code='statistics.js.alert.msg40'/>");
	lang.fn.set("statistics.js.alert.msg41",	"<spring:message code='statistics.js.alert.msg41'/>");


	<%-- evaluation - alert msg --%>
	lang.fn.set("evaluation.js.alert.msg1",	"<spring:message code='evaluation.js.alert.msg1'/>");
	lang.fn.set("evaluation.js.alert.msg2",	"<spring:message code='evaluation.js.alert.msg2'/>");
	lang.fn.set("evaluation.js.alert.msg3",	"<spring:message code='evaluation.js.alert.msg3'/>");
	lang.fn.set("evaluation.js.alert.msg4",	"<spring:message code='evaluation.js.alert.msg4'/>");
	lang.fn.set("evaluation.js.alert.msg5",	"<spring:message code='evaluation.js.alert.msg5'/>");
	lang.fn.set("evaluation.js.alert.msg6",	"<spring:message code='evaluation.js.alert.msg6'/>");
	lang.fn.set("evaluation.js.alert.msg7",	"<spring:message code='evaluation.js.alert.msg7'/>");
	lang.fn.set("evaluation.js.alert.msg8",	"<spring:message code='evaluation.js.alert.msg8'/>");
	lang.fn.set("evaluation.js.alert.msg9",	"<spring:message code='evaluation.js.alert.msg9'/>");
	lang.fn.set("evaluation.js.alert.msg10",	"<spring:message code='evaluation.js.alert.msg10'/>");
	lang.fn.set("evaluation.js.alert.msg11",	"<spring:message code='evaluation.js.alert.msg11'/>");
	lang.fn.set("evaluation.js.alert.msg12",	"<spring:message code='evaluation.js.alert.msg12'/>");
	lang.fn.set("evaluation.js.alert.msg13",	"<spring:message code='evaluation.js.alert.msg13'/>");
	lang.fn.set("evaluation.js.alert.msg14",	"<spring:message code='evaluation.js.alert.msg14'/>");
	lang.fn.set("evaluation.js.alert.msg15",	"<spring:message code='evaluation.js.alert.msg15'/>");
	lang.fn.set("evaluation.js.alert.msg16",	"<spring:message code='evaluation.js.alert.msg16'/>");
	lang.fn.set("evaluation.js.alert.msg17",	"<spring:message code='evaluation.js.alert.msg17'/>");
	lang.fn.set("evaluation.js.alert.msg18",	"<spring:message code='evaluation.js.alert.msg18'/>");
	lang.fn.set("evaluation.js.alert.msg19",	"<spring:message code='evaluation.js.alert.msg19'/>");
	lang.fn.set("evaluation.js.alert.msg20",	"<spring:message code='evaluation.js.alert.msg20'/>");
	lang.fn.set("evaluation.js.alert.msg21",	"<spring:message code='evaluation.js.alert.msg21'/>");
	lang.fn.set("evaluation.js.alert.msg22",	"<spring:message code='evaluation.js.alert.msg22'/>");
	lang.fn.set("evaluation.js.alert.msg23",	"<spring:message code='evaluation.js.alert.msg23'/>");
	lang.fn.set("evaluation.js.alert.msg24",	"<spring:message code='evaluation.js.alert.msg24'/>");
	lang.fn.set("evaluation.js.alert.msg25",	"<spring:message code='evaluation.js.alert.msg25'/>");
	lang.fn.set("evaluation.js.alert.msg26",	"<spring:message code='evaluation.js.alert.msg26'/>");
	lang.fn.set("evaluation.js.alert.msg27",	"<spring:message code='evaluation.js.alert.msg27'/>");
	lang.fn.set("evaluation.js.alert.msg28",	"<spring:message code='evaluation.js.alert.msg28'/>");
	lang.fn.set("evaluation.js.alert.msg29",	"<spring:message code='evaluation.js.alert.msg29'/>");
	lang.fn.set("evaluation.js.alert.msg30",	"<spring:message code='evaluation.js.alert.msg30'/>");
	lang.fn.set("evaluation.js.alert.msg31",	"<spring:message code='evaluation.js.alert.msg31'/>");
	lang.fn.set("evaluation.js.alert.msg32",	"<spring:message code='evaluation.js.alert.msg32'/>");
	lang.fn.set("evaluation.js.alert.msg33",	"<spring:message code='evaluation.js.alert.msg33'/>");
	lang.fn.set("evaluation.js.alert.msg34",	"<spring:message code='evaluation.js.alert.msg34'/>");
	lang.fn.set("evaluation.js.alert.msg35",	"<spring:message code='evaluation.js.alert.msg35'/>");
	lang.fn.set("evaluation.js.alert.msg36",	"<spring:message code='evaluation.js.alert.msg36'/>");
	lang.fn.set("evaluation.js.alert.msg37",	"<spring:message code='evaluation.js.alert.msg37'/>");
	lang.fn.set("evaluation.js.alert.msg38",	"<spring:message code='evaluation.js.alert.msg38'/>");
	lang.fn.set("evaluation.js.alert.msg39",	"<spring:message code='evaluation.js.alert.msg39'/>");
	lang.fn.set("evaluation.js.alert.msg40",	"<spring:message code='evaluation.js.alert.msg40'/>");
	lang.fn.set("evaluation.js.alert.msg41",	"<spring:message code='evaluation.js.alert.msg41'/>");
	lang.fn.set("evaluation.js.alert.msg42",	"<spring:message code='evaluation.js.alert.msg42'/>");
	lang.fn.set("evaluation.js.alert.msg43",	"<spring:message code='evaluation.js.alert.msg43'/>");
	lang.fn.set("evaluation.js.alert.msg44",	"<spring:message code='evaluation.js.alert.msg44'/>");
	lang.fn.set("evaluation.js.alert.msg45",	"<spring:message code='evaluation.js.alert.msg45'/>");
	lang.fn.set("evaluation.js.alert.msg46",	"<spring:message code='evaluation.js.alert.msg46'/>");
	lang.fn.set("evaluation.js.alert.msg47",	"<spring:message code='evaluation.js.alert.msg47'/>");
	lang.fn.set("evaluation.js.alert.msg48",	"<spring:message code='evaluation.js.alert.msg48'/>");
	lang.fn.set("evaluation.js.alert.msg49",	"<spring:message code='evaluation.js.alert.msg49'/>");
	lang.fn.set("evaluation.js.alert.msg50",	"<spring:message code='evaluation.js.alert.msg50'/>");
	lang.fn.set("evaluation.js.alert.msg51",	"<spring:message code='evaluation.js.alert.msg51'/>");
	lang.fn.set("evaluation.js.alert.msg52",	"<spring:message code='evaluation.js.alert.msg52'/>");
	lang.fn.set("evaluation.js.alert.msg53",	"<spring:message code='evaluation.js.alert.msg53'/>");
	lang.fn.set("evaluation.js.alert.msg54",	"<spring:message code='evaluation.js.alert.msg54'/>");
	lang.fn.set("evaluation.js.alert.msg55",	"<spring:message code='evaluation.js.alert.msg55'/>");
	lang.fn.set("evaluation.js.alert.msg56",	"<spring:message code='evaluation.js.alert.msg56'/>");
	lang.fn.set("evaluation.js.alert.msg57",	"<spring:message code='evaluation.js.alert.msg57'/>");
	lang.fn.set("evaluation.js.alert.msg58",	"<spring:message code='evaluation.js.alert.msg58'/>");
	lang.fn.set("evaluation.js.alert.msg59",	"<spring:message code='evaluation.js.alert.msg59'/>");
	lang.fn.set("evaluation.js.alert.msg60",	"<spring:message code='evaluation.js.alert.msg60'/>");
	lang.fn.set("evaluation.js.alert.msg61",	"<spring:message code='evaluation.js.alert.msg61'/>");
	lang.fn.set("evaluation.js.alert.msg62",	"<spring:message code='evaluation.js.alert.msg62'/>");
	lang.fn.set("evaluation.js.alert.msg63",	"<spring:message code='evaluation.js.alert.msg63'/>");
	lang.fn.set("evaluation.js.alert.msg64",	"<spring:message code='evaluation.js.alert.msg64'/>");
	lang.fn.set("evaluation.js.alert.msg65",	"<spring:message code='evaluation.js.alert.msg65'/>");
	lang.fn.set("evaluation.js.alert.msg66",	"<spring:message code='evaluation.js.alert.msg66'/>");
	lang.fn.set("evaluation.js.alert.msg67",	"<spring:message code='evaluation.js.alert.msg67'/>");
	lang.fn.set("evaluation.js.alert.msg68",	"<spring:message code='evaluation.js.alert.msg68'/>");
	lang.fn.set("evaluation.js.alert.msg69",	"<spring:message code='evaluation.js.alert.msg69'/>");
	lang.fn.set("evaluation.js.alert.msg70",	"<spring:message code='evaluation.js.alert.msg70'/>");
	lang.fn.set("evaluation.js.alert.msg71",	"<spring:message code='evaluation.js.alert.msg71'/>");
	lang.fn.set("evaluation.js.alert.msg72",	"<spring:message code='evaluation.js.alert.msg72'/>");
	lang.fn.set("evaluation.js.alert.msg73",	"<spring:message code='evaluation.js.alert.msg73'/>");
	lang.fn.set("evaluation.js.alert.msg74",	"<spring:message code='evaluation.js.alert.msg74'/>");
	lang.fn.set("evaluation.js.alert.msg75",	"<spring:message code='evaluation.js.alert.msg75'/>");
	lang.fn.set("evaluation.js.alert.msg76",	"<spring:message code='evaluation.js.alert.msg76'/>");
	lang.fn.set("evaluation.js.alert.msg77",	"<spring:message code='evaluation.js.alert.msg77'/>");
	lang.fn.set("evaluation.js.alert.msg78",	"<spring:message code='evaluation.js.alert.msg78'/>");
	lang.fn.set("evaluation.js.alert.msg79",	"<spring:message code='evaluation.js.alert.msg79'/>");
	lang.fn.set("evaluation.js.alert.msg80",	"<spring:message code='evaluation.js.alert.msg80'/>");
	lang.fn.set("evaluation.js.alert.msg81",	"<spring:message code='evaluation.js.alert.msg81'/>");
	lang.fn.set("evaluation.js.alert.msg82",	"<spring:message code='evaluation.js.alert.msg82'/>");
	lang.fn.set("evaluation.js.alert.msg83",	"<spring:message code='evaluation.js.alert.msg83'/>");
	lang.fn.set("evaluation.js.alert.msg84",	"<spring:message code='evaluation.js.alert.msg84'/>");
	lang.fn.set("evaluation.js.alert.msg85",	"<spring:message code='evaluation.js.alert.msg85'/>");
	lang.fn.set("evaluation.js.alert.msg86",	"<spring:message code='evaluation.js.alert.msg86'/>");
	lang.fn.set("evaluation.js.alert.msg87",	"<spring:message code='evaluation.js.alert.msg87'/>");
	lang.fn.set("evaluation.js.alert.msg88",	"<spring:message code='evaluation.js.alert.msg88'/>");
	lang.fn.set("evaluation.js.alert.msg89",	"<spring:message code='evaluation.js.alert.msg89'/>");
	lang.fn.set("evaluation.js.alert.msg90",	"<spring:message code='evaluation.js.alert.msg90'/>");
	lang.fn.set("evaluation.js.alert.msg91",	"<spring:message code='evaluation.js.alert.msg91'/>");
	lang.fn.set("evaluation.js.alert.msg92",	"<spring:message code='evaluation.js.alert.msg92'/>");
	lang.fn.set("evaluation.js.alert.msg93",	"<spring:message code='evaluation.js.alert.msg93'/>");
	lang.fn.set("evaluation.js.alert.msg94",	"<spring:message code='evaluation.js.alert.msg94'/>");
	lang.fn.set("evaluation.js.alert.msg95",	"<spring:message code='evaluation.js.alert.msg95'/>");
	lang.fn.set("evaluation.js.alert.msg96",	"<spring:message code='evaluation.js.alert.msg96'/>");
	lang.fn.set("evaluation.js.alert.msg97",	"<spring:message code='evaluation.js.alert.msg97'/>");
	lang.fn.set("evaluation.js.alert.msg98",	"<spring:message code='evaluation.js.alert.msg98'/>");
	lang.fn.set("evaluation.js.alert.msg99",	"<spring:message code='evaluation.js.alert.msg99'/>");
	lang.fn.set("evaluation.js.alert.msg100",	"<spring:message code='evaluation.js.alert.msg100'/>");
	lang.fn.set("evaluation.js.alert.msg101",	"<spring:message code='evaluation.js.alert.msg101'/>");
	lang.fn.set("evaluation.js.alert.msg102",	"<spring:message code='evaluation.js.alert.msg102'/>");
	lang.fn.set("evaluation.js.alert.msg103",	"<spring:message code='evaluation.js.alert.msg103'/>");
	lang.fn.set("evaluation.js.alert.msg104",	"<spring:message code='evaluation.js.alert.msg104'/>");
	lang.fn.set("evaluation.js.alert.msg132",	"<spring:message code='evaluation.js.alert.msg132'/>");

	<%-- 추가 적용 --%>
	lang.fn.set("admin.alert.authyManage21",	"<spring:message code='admin.alert.authyManage21'/>");
	lang.fn.set("admin.alert.authyManage22",	"<spring:message code='admin.alert.authyManage22'/>");
	lang.fn.set("admin.alert.authyManage23",	"<spring:message code='admin.alert.authyManage23'/>");
	lang.fn.set("admin.channel.title.multiChannelAdd",	"<spring:message code='admin.channel.title.multiChannelAdd'/>");
	lang.fn.set("admin.label.add",	"<spring:message code='admin.label.add'/>");
	lang.fn.set("admin.label.update",	"<spring:message code='admin.label.update'/>");
	lang.fn.set("admin.label.authy",	"<spring:message code='admin.label.authy'/>");
	lang.fn.set("admin.label.all",	"<spring:message code='admin.label.all'/>");
	lang.fn.set("admin.label.noAuthy",	"<spring:message code='admin.label.noAuthy'/>");

	lang.fn.set("common.excel.download",	"<spring:message code='common.excel.download'/>");


	//lang.fn.set("views.search.alert.msg72",	"<spring:message code='views.search.alert.msg72'/>");
	lang.fn.set("views.search.alert.msg72",	"<spring:message code='views.search.alert.msg72'/>");
	lang.fn.set("views.search.alert.msg73",	"<spring:message code='views.search.alert.msg73'/>");
	lang.fn.set("views.search.alert.msg74",	"<spring:message code='views.search.alert.msg74'/>");
	lang.fn.set("views.search.alert.msg75",	"<spring:message code='views.search.alert.msg75'/>");
	lang.fn.set("views.search.alert.msg76",	"<spring:message code='views.search.alert.msg76'/>");
	lang.fn.set("views.search.alert.msg77",	"<spring:message code='views.search.alert.msg77'/>");

	lang.fn.set("views.player.html.text1",	"<spring:message code='views.player.html.text1'/>");
	lang.fn.set("views.player.html.text2",	"<spring:message code='views.player.html.text2'/>");
	lang.fn.set("views.player.html.text3",	"<spring:message code='views.player.html.text3'/>");
	lang.fn.set("views.player.html.text4",	"<spring:message code='views.player.html.text4'/>");
	lang.fn.set("views.player.html.text5",	"<spring:message code='views.player.html.text5'/>");
	lang.fn.set("views.player.html.text6",	"<spring:message code='views.player.html.text6'/>");
	lang.fn.set("views.player.html.text7",	"<spring:message code='views.player.html.text7'/>");
	lang.fn.set("views.player.html.text8",	"<spring:message code='views.player.html.text8'/>");
	lang.fn.set("views.player.html.text9",	"<spring:message code='views.player.html.text9'/>")
	lang.fn.set("views.player.html.text10",	"<spring:message code='views.player.html.text10'/>")
	lang.fn.set("views.player.html.text11",	"<spring:message code='views.player.html.text11'/>")
	lang.fn.set("views.player.html.text12",	"<spring:message code='views.player.html.text12'/>")
	lang.fn.set("views.player.html.text13",	"<spring:message code='views.player.html.text13'/>")
	lang.fn.set("views.player.html.text14",	"<spring:message code='views.player.html.text14'/>")
	lang.fn.set("views.player.html.text15",	"<spring:message code='views.player.html.text15'/>")
	lang.fn.set("views.player.html.text16",	"<spring:message code='views.player.html.text16'/>")
	lang.fn.set("views.player.html.text17",	"<spring:message code='views.player.html.text17'/>")
	lang.fn.set("views.player.html.text18",	"<spring:message code='views.player.html.text18'/>")
	lang.fn.set("views.player.html.text19",	"<spring:message code='views.player.html.text19'/>")
	lang.fn.set("views.player.html.text20",	"<spring:message code='views.player.html.text20'/>")
	lang.fn.set("views.player.html.text21",	"<spring:message code='views.player.html.text21'/>")
	lang.fn.set("views.player.html.text22",	"<spring:message code='views.player.html.text22'/>")
	lang.fn.set("views.player.html.text23",	"<spring:message code='views.player.html.text23'/>")
	lang.fn.set("views.player.html.text24",	"<spring:message code='views.player.html.text24'/>")
	lang.fn.set("views.player.html.text25",	"<spring:message code='views.player.html.text25'/>")
	lang.fn.set("views.player.html.text26",	"<spring:message code='views.player.html.text26'/>")
	lang.fn.set("views.player.html.text27",	"<spring:message code='views.player.html.text27'/>")
	lang.fn.set("views.player.html.text28",	"<spring:message code='views.player.html.text28'/>")
	lang.fn.set("views.player.html.text29",	"<spring:message code='views.player.html.text29'/>")
	lang.fn.set("views.player.html.text30",	"<spring:message code='views.player.html.text30'/>")
	lang.fn.set("views.player.html.text31",	"<spring:message code='views.player.html.text31'/>")/* 플레이어 만료기간을 입력 해 주세요! */
	lang.fn.set("views.player.html.text32",	"<spring:message code='views.player.html.text32'/>")/* 플레이어 만료기간은 최소 1일 입니다. */
	lang.fn.set("views.player.html.text33",	"<spring:message code='views.player.html.text33'/>")/* 플레이어 만료기간은 최대 30일 입니다. */
	lang.fn.set("views.player.html.text34",	"<spring:message code='views.player.html.text34'/>")/* 2차 인증 암호를 입력 해 주세요! */
	lang.fn.set("views.player.html.text35",	"<spring:message code='views.player.html.text35'/>")/* 2차 인증 암호를 확인 해 주세요! */
	lang.fn.set("views.player.html.text36",	"<spring:message code='views.player.html.text36'/>")/* 플레이어를 다운로드 받으시겠습니까? */
	lang.fn.set("views.player.html.text37",	"<spring:message code='views.player.html.text37'/>")/* 플레이어를 다운로드 받으시겠습니까? */
	lang.fn.set("views.player.html.text38",	"<spring:message code='views.player.html.text38'/>")/* 플레이어를 다운로드 받으시겠습니까? */
	lang.fn.set("views.search.grid.alert.noChecked",	"<spring:message code='views.search.grid.alert.noChecked'/>")
	lang.fn.set("views.search.grid.alert.moreReason",	"<spring:message code='views.search.grid.alert.moreReason'/>")
	lang.fn.set("views.search.grid.alert.mobileNotUpload",	"<spring:message code='views.search.grid.alert.mobileNotUpload'/>")
	lang.fn.set("admin.label.notUse",	"<spring:message code='admin.label.notUse'/>")
	lang.fn.set("common.label.Noclassification",	"<spring:message code='common.label.Noclassification'/>")
	lang.fn.set("admin.alert.recUser35",	"<spring:message code='admin.alert.recUser35'/>")
	lang.fn.set("admin.alert.recUser36",	"<spring:message code='admin.alert.recUser36'/>")
	lang.fn.set("admin.alert.recUser37",	"<spring:message code='admin.alert.recUser37'/>")
	lang.fn.set("admin.alert.recUser38",	"<spring:message code='admin.alert.recUser38'/>")
	lang.fn.set("admin.alert.recUser39",	"<spring:message code='admin.alert.recUser39'/>")
	lang.fn.set("admin.alert.recUser40",	"<spring:message code='admin.alert.recUser40'/>")
	lang.fn.set("admin.alert.recUser41",	"<spring:message code='admin.alert.recUser41'/>")
	lang.fn.set("admin.alert.recUser42",	"<spring:message code='admin.alert.recUser42'/>")
	lang.fn.set("admin.alert.recUser43",	"<spring:message code='admin.alert.recUser43'/>")
	lang.fn.set("admin.alert.recUser44",	"<spring:message code='admin.alert.recUser44'/>")
	lang.fn.set("admin.alert.recUser45",	"<spring:message code='admin.alert.recUser45'/>");
	lang.fn.set("admin.alert.recUser46",	"<spring:message code='admin.alert.recUser46'/>");
	lang.fn.set("admin.alert.recUser47",	"<spring:message code='admin.alert.recUser47'/>");


	lang.fn.set("views.search.grid.head.R_REC_KEYWORD",	"<spring:message code='views.search.grid.head.R_REC_KEYWORD'/>"); <%-- 키워드 --%>
	lang.fn.set("views.search.grid.head.R_REC_CATEGORY",	"<spring:message code='views.search.grid.head.R_REC_CATEGORY'/>"); <%-- 카테고리 --%>
	
	lang.fn.set("combo.ALL",	"<spring:message code='combo.ALL'/>"); <%-- 전체 --%>
	lang.fn.set("admin.approve.label.addToMyfolder",	"<spring:message code='admin.approve.label.addToMyfolder'/>"); <%-- 선택파일 마이폴더 추가 --%>
	lang.fn.set("admin.approve.label.addToBestcall",	"<spring:message code='admin.approve.label.addToBestcall'/>"); <%-- 선택녹취 우수콜 추가 --%>
	lang.fn.set("admin.allowableRange.button.rangeAdd",	"<spring:message code='admin.allowableRange.button.rangeAdd'/>"); <%-- 허용 범위 추가 --%>
	lang.fn.set("admin.allowableRange.button.rangeModi",	"<spring:message code='admin.allowableRange.button.rangeModi'/>"); <%-- 허용 범위 수정 --%>
	lang.fn.set("admin.allowableRange.button.groupAdd",	"<spring:message code='admin.allowableRange.button.groupAdd'/>"); <%-- 선택 조직 추가 --%>
	lang.fn.set("admin.approve.label.transcript",      "<spring:message code='admin.approve.label.transcript'/>");<%-- 전사 --%>
	
	/* 마이폴더 */
	lang.fn.set("myfolder.alert.inputName",	"<spring:message code='myfolder.alert.inputName'/>"); <%-- 폴더명을 입력해주세요. --%>
	lang.fn.set("myfolder.alert.completingAdd",	"<spring:message code='myfolder.alert.completingAdd'/>"); <%-- 폴더 추가가 완료 되었습니다. --%>
	lang.fn.set("myfolder.alert.existSameFolder",	"<spring:message code='myfolder.alert.existSameFolder'/>"); <%-- 동일한 폴더가 존재합니다. 다시 입력해주세요. --%>
	lang.fn.set("myfolder.alert.selectFolder",	"<spring:message code='myfolder.alert.selectFolder'/>"); <%-- 폴더를 선택해주세요. --%>
	lang.fn.set("myfolder.alert.editingFolder",	"<spring:message code='myfolder.alert.editingFolder'/>"); <%-- 폴더 수정이 완료 되었습니다. --%>
	lang.fn.set("myfolder.alert.deletingFolder",	"<spring:message code='myfolder.alert.deletingFolder'/>"); <%-- 정말로 삭제하시겠습니까? --%>
	lang.fn.set("myfolder.alert.notExistFile",	"<spring:message code='myfolder.alert.notExistFile'/>"); <%-- 선택한 녹취 파일이 없습니다. --%>
	lang.fn.set("myfolder.alert.alreadyRecorded",	"<spring:message code='myfolder.alert.alreadyRecorded'/>"); <%-- 해당 폴더에 이미 녹취파일이 있습니다. --%>
	lang.fn.set("myfolder.button.editFolder",	"<spring:message code='myfolder.button.editFolder'/>"); <%-- 폴더 수정 --%>
	lang.fn.set("myfolder.button.deleteFolder",	"<spring:message code='myfolder.button.deleteFolder'/>"); <%-- 폴더 삭제 --%>
	lang.fn.set("myfolder.button.addFolder",	"<spring:message code='myfolder.button.addFolder'/>"); <%-- 폴더 추가 --%>
	lang.fn.set("myfolder.button.addFile",	"<spring:message code='myfolder.button.addFile'/>"); <%-- 선택파일 리스트에 추가 --%>
	lang.fn.set("myfolder.button.moveFile",	"<spring:message code='myfolder.button.moveFile'/>"); <%-- 선택파일 이동 --%>
	lang.fn.set("myfolder.button.removeFile",	"<spring:message code='myfolder.button.removeFile'/>"); <%-- 선택파일 마이폴더에서 제거 --%>
	lang.fn.set("common.label.folderName",	"<spring:message code='common.label.folderName'/>"); <%-- 폴더명 --%>
	lang.fn.set("common.label.selectFolder",	"<spring:message code='common.label.selectFolder'/>"); <%-- 폴더 선택 --%>
	
	/* 우수콜 */
	lang.fn.set("bestCall.text.share",	"<spring:message code='bestCall.text.share'/>"); <%-- 공유 --%>
	lang.fn.set("bestCall.text.modify",	"<spring:message code='bestCall.text.modify'/>"); <%-- 수정 --%>
	lang.fn.set("bestCall.text.modify",	"<spring:message code='bestCall.text.modify'/>"); <%-- 수정 --%>
	lang.fn.set("views.bestCall.form.shareYN.Y",	"<spring:message code='views.bestCall.form.shareYN.Y'/>"); <%-- 공유중 --%>
	lang.fn.set("views.bestCall.form.shareYN.N",	"<spring:message code='views.bestCall.form.shareYN.N'/>"); <%-- 공유기간 만료 --%>
	lang.fn.set("views.bestCall.form.approveBtn",	"<spring:message code='views.bestCall.form.approveBtn'/>"); <%-- 승인 --%>
	lang.fn.set("views.bestCall.form.rejectBtn",	"<spring:message code='views.bestCall.form.rejectBtn'/>"); <%-- 반려 --%>
	lang.fn.set("bestCall.alert.msg01",	"<spring:message code='bestCall.alert.msg01'/>"); <%-- 등록일자를 다음 형식에 맞게 입력 해 주세요!\n예)2018-01-01 --%>
	lang.fn.set("bestCall.alert.msg02",	"<spring:message code='bestCall.alert.msg02'/>"); <%-- 취소할 우수콜들을 체크해 주세요. --%>
	lang.fn.set("bestCall.alert.msg03",	"<spring:message code='bestCall.alert.msg03'/>"); <%-- 승인된 리스트가 포함되어 있습니다. --%>
	lang.fn.set("bestCall.alert.msg04",	"<spring:message code='bestCall.alert.msg04'/>"); <%-- 승인된 리스트입니다. --%>
	lang.fn.set("bestCall.alert.msg05",	"<spring:message code='bestCall.alert.msg05'/>"); <%-- 공유할 우수콜을 선택해 주세요. --%>
	lang.fn.set("bestCall.alert.msg06",	"<spring:message code='bestCall.alert.msg06'/>"); <%-- 공유중인 리스트가 포함되어 있습니다. --%>
	lang.fn.set("bestCall.alert.msg07",	"<spring:message code='bestCall.alert.msg07'/>"); <%-- 공유중인 리스트입니다. --%>
	lang.fn.set("bestCall.alert.msg08",	"<spring:message code='bestCall.alert.msg08'/>"); <%-- 공유기간이 만료된 리스트가 포함되어 있습니다. --%>
	lang.fn.set("bestCall.alert.msg09",	"<spring:message code='bestCall.alert.msg09'/>"); <%-- 공유기간이 만료된 리스트입니다. --%>
	lang.fn.set("bestCall.alert.msg10",	"<spring:message code='bestCall.alert.msg10'/>"); <%-- 공유기간을 입력해 주세요. --%>
	lang.fn.set("bestCall.alert.msg11",	"<spring:message code='bestCall.alert.msg11'/>"); <%-- 공유범위를 선택해 주세요. --%>
	lang.fn.set("bestCall.alert.msg12",	"<spring:message code='bestCall.alert.msg12'/>"); <%-- 수정할 우수콜을 선택해 주세요. --%>
	lang.fn.set("bestCall.alert.msg13",	"<spring:message code='bestCall.alert.msg13'/>"); <%-- 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. --%>
	lang.fn.set("bestCall.alert.msg14",	"<spring:message code='bestCall.alert.msg14'/>"); <%-- 네트워크 에러가 발생하였습니다. --%>
	lang.fn.set("bestCall.alert.msg15",	"<spring:message code='bestCall.alert.msg15'/>"); <%-- 삭제에 성공하였습니다. --%>
	lang.fn.set("bestCall.alert.msg16",	"<spring:message code='bestCall.alert.msg16'/>"); <%-- 선택한 우수콜을 --%>
	lang.fn.set("bestCall.alert.msg17",	"<spring:message code='bestCall.alert.msg17'/>"); <%-- 하시겠습니까? --%>
	lang.fn.set("bestCall.alert.msg18",	"<spring:message code='bestCall.alert.msg18'/>"); <%-- 에 성공하였습니다. --%>
	lang.fn.set("bestCall.alert.msg19",	"<spring:message code='bestCall.alert.msg19'/>"); <%-- 승인 또는 반려 할 우수콜을 선택해 주세요. --%>
	lang.fn.set("bestCall.alert.msg20",	"<spring:message code='bestCall.alert.msg20'/>"); <%-- 승인, 반려 대상이 아닌 리스트가 포함되어 있습니다. --%>
	lang.fn.set("bestCall.alert.msg21",	"<spring:message code='bestCall.alert.msg21'/>"); <%-- 승인, 반려 대상이 아닙니다. --%>
	lang.fn.set("bestCall.alert.msg22",	"<spring:message code='bestCall.alert.msg22'/>"); <%-- 공유기간이 설정되지 않은 리스트가 있습니다. --%>
	lang.fn.set("bestCall.alert.msg23",	"<spring:message code='bestCall.alert.msg23'/>"); <%-- 공유기간이 설정되지 않았습니다. --%>
	lang.fn.set("bestCall.alert.msg24",	"<spring:message code='bestCall.alert.msg24'/>"); <%-- 선택한 우수콜을 승인 하시겠습니까? --%>
	lang.fn.set("bestCall.alert.msg25",	"<spring:message code='bestCall.alert.msg25'/>"); <%-- 해당 리스트를 --%>
	lang.fn.set("bestCall.alert.msg26",	"<spring:message code='bestCall.alert.msg26'/>"); <%-- 하였습니다. --%>
	lang.fn.set("bestCall.alert.msg27",	"<spring:message code='bestCall.alert.msg27'/>"); <%-- 정말로 취소하시겠습니까? --%>
	lang.fn.set("bestCall.alert.msg28",	"<spring:message code='bestCall.alert.msg28'/>"); <%-- 우수콜 승인권한이 없습니다. --%>
	

	lang.fn.set("search.player.inputMsg",	"<spring:message code='search.player.inputMsg'/>"); <%-- 우수콜 제목을 25자 내외로 입력해 주세요 --%>
	lang.fn.set("search.player.addBestcall",	"<spring:message code='search.player.addBestcall'/>"); <%-- 우수콜 추가 --%>
	lang.fn.set("search.player.playerTag",	"<spring:message code='search.player.playerTag'/>"); <%-- 플레이어 태그 --%>
	lang.fn.set("search.player.repeat",	"<spring:message code='search.player.repeat'/>"); <%-- 반복 --%>
	lang.fn.set("search.player.cancelRepeat",	"<spring:message code='search.player.cancelRepeat'/>"); <%-- 반복해제 --%>
	lang.fn.set("search.player.down",	"<spring:message code='search.player.down'/>"); <%-- 다운 --%>
	lang.fn.set("search.player.mute",	"<spring:message code='search.player.mute'/>"); <%-- 묵음 --%>
	lang.fn.set("search.player.cancelMute",	"<spring:message code='search.player.cancelMute'/>"); <%-- 묵음해제 --%>
	lang.fn.set("search.player.remove",	"<spring:message code='search.player.remove'/>"); <%-- 제거 --%>
	lang.fn.set("search.player.cancelRemove",	"<spring:message code='search.player.cancelRemove'/>"); <%-- 제거해제 --%>
	lang.fn.set("search.player.del",	"<spring:message code='search.player.del'/>"); <%-- 삭제 --%>
	lang.fn.set("search.player.fileDown",	"<spring:message code='search.player.fileDown'/>"); <%-- 파일 다운로드 --%>
	lang.fn.set("search.player.PlayerDown",	"<spring:message code='search.player.PlayerDown'/>"); <%-- 플레이어 다운로드 --%>
	lang.fn.set("search.player.alert.msg1",	"<spring:message code='search.player.alert.msg1'/>"); <%-- 청취 가능 기간이 만료된 파일 입니다! --%>
	lang.fn.set("search.player.alert.msg2",	"<spring:message code='search.player.alert.msg2'/>"); <%-- 플레이어에서 재생이 불가능 한 파일 입니다.\\n플레이어에서 다운로드 한 파일 만 재생이 가능 합니다. --%>
	lang.fn.set("search.player.alert.msg3",	"<spring:message code='search.player.alert.msg3'/>"); <%-- 요청하신 녹취 파일을 찾을 수 없습니다. --%>
	lang.fn.set("search.player.alert.msg4",	"<spring:message code='search.player.alert.msg4'/>"); <%-- 더 이상 재생할 목록이 없습니다. --%>
	lang.fn.set("search.player.alert.msg5",	"<spring:message code='search.player.alert.msg5'/>"); <%-- 요청에 실패 하였습니다. --%>
	lang.fn.set("search.player.alert.msg6",	"<spring:message code='search.player.alert.msg6'/>"); <%-- 감청하실 대상의 청취 버튼을 클릭 해 주세요. --%>
	lang.fn.set("search.player.alert.msg7",	"<spring:message code='search.player.alert.msg7'/>"); <%-- 파일이 없습니다 --%>
	lang.fn.set("search.player.alert.msg8",	"<spring:message code='search.player.alert.msg8'/>"); <%-- 현재 실시간 감청중인 대상이 없습니다. --%>
	lang.fn.set("search.player.alert.msg9",	"<spring:message code='search.player.alert.msg9'/>"); <%-- 플레이어 태그 저장이 완료 되었습니다 --%>
	lang.fn.set("search.player.alert.msg10",	"<spring:message code='search.player.alert.msg10'/>"); <%-- 플레이어 태그 삭제가 완료 되었습니다 --%>
	lang.fn.set("search.player.alert.msg11",	"<spring:message code='search.player.alert.msg11'/>"); <%-- 우수콜 제목을 입력해 주세요 --%>
	lang.fn.set("search.player.alert.msg12",	"<spring:message code='search.player.alert.msg12'/>"); <%-- 재생 중인 파일이 없습니다. --%>
	lang.fn.set("search.player.alert.msg13",	"<spring:message code='search.player.alert.msg13'/>"); <%-- 녹취 파일의 묵음/제거 할 부분을 표시해 주세요. --%>
	lang.fn.set("search.player.alert.msg14",	"<spring:message code='search.player.alert.msg14'/>"); <%-- 우수콜 등재에 성공하였습니다. --%>
	lang.fn.set("search.player.alert.msg15",	"<spring:message code='search.player.alert.msg15'/>"); <%-- 우수콜 등재에 실패하였습니다. --%>
	lang.fn.set("search.player.alert.msg16",	"<spring:message code='search.player.alert.msg16'/>"); <%-- 청취 가능 기간이 만료된 파일 입니다! --%>
	lang.fn.set("search.player.alert.msg17",	"<spring:message code='search.player.alert.msg17'/>"); <%-- 우수콜 추가를 하시겠습니까? --%>
	lang.fn.set("search.player.alert.msg18",	"<spring:message code='search.player.alert.msg18'/>"); <%-- 삭제 하시겠습니까? --%>
	lang.fn.set("search.player.alert.msg19",	"<spring:message code='search.player.alert.msg19'/>"); <%-- 재생중이던 파일 입니다.\\n 이어서 재생 하시겠습니까? --%>
	lang.fn.set("search.player.alert.msg20",	"<spring:message code='search.player.alert.msg20'/>"); <%-- 현재 파일에 저장된 플레이어 태그가 모두 삭제 됩니다.\\n정말 삭제 하시겠습니까? --%>
	
	
	
	
	
	/* 청취 및 다운로드 요청 관리 */
	lang.fn.set("approveList.alert.msg01",	"<spring:message code='approveList.alert.msg01'/>");<%-- 요청 유형이 다운로드가 아닙니다. --%>
	lang.fn.set("approveList.alert.msg02",	"<spring:message code='approveList.alert.msg02'/>");<%-- 승인되지 않은 요청입니다. --%>
	lang.fn.set("approveList.alert.msg03",	"<spring:message code='approveList.alert.msg03'/>");<%-- 권한이 없습니다. --%>
	lang.fn.set("approveList.alert.msg04",	"<spring:message code='approveList.alert.msg04'/>");<%-- 다운로드 기한이 종료되었습니다. --%>
	lang.fn.set("approveList.alert.msg05",	"<spring:message code='approveList.alert.msg05'/>");<%-- 요청 유형이 청취가 아닙니다. --%>
	lang.fn.set("approveList.alert.msg06",	"<spring:message code='approveList.alert.msg06'/>");<%-- 청취 기간이 종료되었습니다. --%>
	lang.fn.set("approveList.alert.msg07",	"<spring:message code='approveList.alert.msg07'/>");<%-- 요청 일자를 다음 형식에 맞게 입력 해 주세요!\\n예)2018-01-01 --%>
	lang.fn.set("approveList.alert.msg08",	"<spring:message code='approveList.alert.msg08'/>");<%-- 이미 처리 완료된 건 입니다. --%>
	lang.fn.set("approveList.alert.msg09",	"<spring:message code='approveList.alert.msg09'/>");<%-- 선택하신 항목은 1차 승인 대기중인 항목 입니다.\\n1차 승인 권한이 없는 사용자는 현재 요청을 반영 할 수 없습니다. --%>
	lang.fn.set("approveList.alert.msg10",	"<spring:message code='approveList.alert.msg10'/>");<%-- 선택하신 항목은 2차 승인 대기중인 항목 입니다.\\n2차 승인 권한이 없는 사용자는 현재 요청을 반영 할 수 없습니다. --%>
	lang.fn.set("approveList.alert.msg11",	"<spring:message code='approveList.alert.msg11'/>");<%-- 선택하신 항목은 최종 승인 대기중인 항목 입니다.\\n최종 승인 권한이 없는 사용자는 현재 요청을 반영 할 수 없습니다. --%>
	lang.fn.set("approveList.alert.msg12",	"<spring:message code='approveList.alert.msg12'/>");<%-- 정상적으로 처리 되었습니다. --%>
	lang.fn.set("approveList.alert.msg13",	"<spring:message code='approveList.alert.msg13'/>");<%-- 요청에 실패하였습니다. --%>
	
	

	/* 업로드 현황(19.06.03) */
	lang.fn.set("views.search.alert.indivUpload",	"<spring:message code='views.search.alert.indivUpload'/>");<%-- 선택파일 개별 업로드 --%>
	lang.fn.set("views.search.alert.someReqFail",	"<spring:message code='views.search.alert.someReqFail'/>");<%-- 일부 신청이 실패 하였습니다. --%>
	lang.fn.set("views.search.alert.reqTrans",		"<spring:message code='views.search.alert.reqTrans'/>");<%-- 즉시 전송 신청을 하였습니다. --%>
	lang.fn.set("views.search.alert.transReqFail",	"<spring:message code='views.search.alert.transReqFail'/>");<%-- 즉시 전송 신청에 실패 하였습니다. --%>
	lang.fn.set("uploadstatus.alert.someReqFail",	"<spring:message code='uploadstatus.alert.someReqFail'/>");<%-- 일부 신청이 실패 하였습니다. --%>
	lang.fn.set("uploadstatus.alert.reqTrans",		"<spring:message code='uploadstatus.alert.reqTrans'/>");<%-- 즉시 전송 신청을 하였습니다. --%>
	lang.fn.set("uploadstatus.alert.transReqFail",	"<spring:message code='uploadstatus.alert.transReqFail'/>");<%-- 즉시 전송 신청에 실패 하였습니다. --%>

	/* 11번가 보안관련(19.08.13) */
	lang.fn.set("admin.alert.unlockSuccess",		"<spring:message code='admin.alert.unlockSuccess'/>");<%-- 잠금 해제에 성공 하였습니다. --%>
	lang.fn.set("admin.alert.unlockFail",			"<spring:message code='admin.alert.unlockFail'/>");<%-- 잠금 해제에 실패 하였습니다. --%>
	lang.fn.set("admin.alert.unlockConfirm",		"<spring:message code='admin.alert.unlockConfirm'/>");<%-- 선택하신 계정의 잠금을 해제 하시겠습니까? --%>

	
	
	lang.fn.set("admin.confirm.deletingMemo",		"<spring:message code='admin.confirm.deletingMemo'/>");<%-- 메모를 삭제 하시겠습니까? --%>
	lang.fn.set("admin.confirm.chkPlayingMemo",		"<spring:message code='admin.confirm.chkPlayingMemo'/>");<%-- 현재 재생중인 파일이 없거나, 재생중인 파일과 다릅니다. \\n선택하신 메모의 파일을 재생하시겠습니까? --%>
	lang.fn.set("common.alert.loginAgain",			"<spring:message code='common.alert.loginAgain'/>");<%-- 세션이 만료되어 로그인페이지로 이동합니다. \\n재 로그인 후 이용해주세요. --%>
	lang.fn.set("login.login.alert.alreadyLogin",	"<spring:message code='login.login.alert.alreadyLogin'/>");<%-- 해당 아이디는 이미 로그인 되어 있습니다.\\n 관리자에게 문의 하십시오. --%>
	lang.fn.set("common.alert.PartialRec",			"<spring:message code='common.alert.PartialRec'/>");<%-- 부분녹취는 듀얼 플레이어로 청취 하실 수 없습니다. --%>
	lang.fn.set("common.alert.noDevice",			"<spring:message code='common.alert.noDevice'/>");<%-- 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. --%>
	
	lang.fn.set("common.alert.savedMemo",			"<spring:message code='common.alert.savedMemo'/>");<%-- 메모 저장이 완료 되었습니다. --%>
	lang.fn.set("common.alert.deletedMemo",			"<spring:message code='common.alert.deletedMemo'/>");<%-- 메모 삭제가 완료 되었습니다. --%>
	lang.fn.set("common.alert.savedPlayerTag",		"<spring:message code='common.alert.savedPlayerTag'/>");<%-- 플레이어 태그 저장이 완료 되었습니다. --%>
	lang.fn.set("common.alert.failUpdateTag",		"<spring:message code='common.alert.failUpdateTag'/>");<%-- 태그 정보가 정상적으로 갱신되지 못하였습니다. \\n재 조회를 해주시면 태그 정보가 정상적으로 갱신 됩니다. --%>
	lang.fn.set("common.alert.inputTime",			"<spring:message code='common.alert.inputTime'/>");<%-- 다음과 같이 시분 초를 입력 해 주세요! --%>
	lang.fn.set("common.alert.ex",					"<spring:message code='common.alert.ex'/>");<%-- 예 --%>
	lang.fn.set("common.alert.chkDrawing",			"<spring:message code='common.alert.chkDrawing'/>");<%-- 도면을 먼저 조회해주세요. --%>
	lang.fn.set("common.alert.searchWord",			"<spring:message code='common.alert.searchWord'/>");<%-- 검색할 단어를 입력해주세요. --%>
	
	
	lang.fn.set("statistics.title.callRate",		"<spring:message code='statistics.title.callRate'/>");<%-- 콜 타입별 비율 --%>
	lang.fn.set("statistics.title.callTrends",		"<spring:message code='statistics.title.callTrends'/>");<%-- 통화 시간별 콜 추이 --%>
	lang.fn.set("statistics.title.selectEmployee",	"<spring:message code='statistics.title.selectEmployee'/>");<%-- 사원을 선택하세요 --%>
	lang.fn.set("statistics.title.total",			"<spring:message code='statistics.title.total'/>");<%-- 총 --%>
	lang.fn.set("statistics.title.selectPeople",	"<spring:message code='statistics.title.selectPeople'/>");<%-- 명 선택 --%>
	lang.fn.set("statistics.title.callTrends2",		"<spring:message code='statistics.title.callTrends2'/>");<%-- 콜 추이 --%>
	lang.fn.set("statistics.title.cases",			"<spring:message code='statistics.title.cases'/>");<%-- 건 --%>
	lang.fn.set("statistics.title.monthly",			"<spring:message code='statistics.title.monthly'/>");<%-- 월간 콜 추이 --%>
	
	

	lang.fn.set("statistics.title.callTransfer",		"<spring:message code='statistics.title.callTransfer'/>");<%-- 호전환 --%>
	lang.fn.set("statistics.title.extension",		"<spring:message code='statistics.title.extension'/>");<%-- 내선 --%>
	lang.fn.set("statistics.title.meeting",		"<spring:message code='statistics.title.meeting'/>");<%-- 회의 --%>
	lang.fn.set("statistics.title.timeOfTheDay",		"<spring:message code='statistics.title.timeOfTheDay'/>");<%-- 금일 시간에 따른 녹취 타입별 통계 --%>
	lang.fn.set("statistics.title.callCount",		"<spring:message code='statistics.title.callCount'/>");<%-- 콜 개수 --%>
	lang.fn.set("statistics.title.time",		"<spring:message code='statistics.title.time'/>");<%-- 시간 --%>
	lang.fn.set("statistics.title.weekly",		"<spring:message code='statistics.title.weekly'/>");<%-- 주간 녹취 타입별 통계 --%>
	lang.fn.set("statistics.title.date",		"<spring:message code='statistics.title.date'/>");<%-- 날짜 --%>
	lang.fn.set("statistics.title.hourOfTheDay",		"<spring:message code='statistics.title.hourOfTheDay'/>");<%-- 금일 시간별 녹취 통계 --%>
	// set, get을 fn 생략하고 쓸 수 있도록 하기
	
		
	lang.fn.set("monitoring.alert.noDrawing.selected",		"<spring:message code='monitoring.alert.noDrawing.selected'/>");<%-- 선택된 도면이 없습니다. --%>
	lang.fn.set("monitoring.alert.noUser.selected",		"<spring:message code='monitoring.alert.noUser.selected'/>");<%-- 선택된 사용자가 없습니다. --%>
	lang.fn.set("monitoring.alert.Drawing.completed",		"<spring:message code='monitoring.alert.Drawing.completed'/>");<%-- 도면 수정이 완료 되었습니다. --%>
	lang.fn.set("monitoring.alert.afterRegistering",		"<spring:message code='monitoring.alert.afterRegistering'/>");<%-- 상담사의 내선 등록후 이용해주세요. --%>
	lang.fn.set("monitoring.alert.Enter.drawingName",		"<spring:message code='monitoring.alert.Enter.drawingName'/>");<%-- 도면명을 입력해주세요 --%>
	lang.fn.set("monitoring.alert.addDrawing.complete",		"<spring:message code='monitoring.alert.addDrawing.complete'/>");<%-- 도면 추가가 완료 되었습니다. --%>
	lang.fn.set("monitoring.alert.addDrawing.problem",		"<spring:message code='monitoring.alert.addDrawing.problem'/>");<%-- 도면을 추가하는데 문제가 생겼습니다. \\n 다시 입력해주세요. --%>
	lang.fn.set("monitoring.alert.counselor.notOnTheLine",		"<spring:message code='monitoring.alert.counselor.notOnTheLine'/>");<%-- 해당 상담사가 통화중이 아닙니다. --%>
	lang.fn.set("monitoring.alert.getInfo.problems",		"<spring:message code='monitoring.alert.getInfo.problems'/>");<%-- 상담사 정보를 가져오는데 문제가 생겼습니다. \\n 잠시후 다시 이용해 주시기 바랍니다. --%>
	lang.fn.set("monitoring.alert.architect.notBusy",		"<spring:message code='monitoring.alert.architect.notBusy'/>");<%-- 선택한 설계사가 통화중이 아닙니다. --%>
	lang.fn.set("monitoring.alert.counselor.notOnTheLine",		"<spring:message code='monitoring.alert.counselor.notOnTheLine'/>");<%-- 해당 상담사가 통화중이 아닙니다. --%>
		
	
	lang.fn.set("common.alert.noUserID",		"<spring:message code='common.alert.noUserID'/>");<%-- 사용자 아이디 정보가 없습니다.\\n올바른 경로로 이용해 주세요!\\n확인을 누르시면, 로그인 페이지로 이동합니다. --%>
	lang.fn.set("common.alert.extension.succeeded",		"<spring:message code='common.alert.extension.succeeded'/>");<%-- 비밀번호 변경주기 연장에 성공 하였습니다.\\n확인을 누르시면 로그인페이지로 이동합니다.\\n재 로그인 해주세요! --%>
	lang.fn.set("common.alert.extend.Failed",		"<spring:message code='common.alert.extend.Failed'/>");<%-- 비밀번호 변경주기 연장에 실패 하였습니다. --%>
	lang.fn.set("common.alert.current.password",		"<spring:message code='common.alert.current.password'/>");<%-- 현재 비밀번호를 입력해주세요! --%>
	lang.fn.set("common.alert.newPassword",		"<spring:message code='common.alert.newPassword'/>");<%-- 새로운 비밀번호를 입력해주세요! --%>
	lang.fn.set("common.alert.notMatch.password",		"<spring:message code='common.alert.notMatch.password'/>");<%-- 새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다! --%>
	lang.fn.set("common.alert.change.succeeded",		"<spring:message code='common.alert.change.succeeded'/>");<%-- 비밀번호 변경에 성공 하였습니다.\\n확인을 누르시면 로그인페이지로 이동합니다.\\n변경된 비밀번호로 로그인 해주세요! --%>
	lang.fn.set("common.alert.wrong.password",		"<spring:message code='common.alert.wrong.password'/>");<%-- 현재 비밀번호가 일치 하지 않습니다. \\n 다시 입력해주세요! --%>
	lang.fn.set("common.alert.passwords.inThePast",		"<spring:message code='common.alert.passwords.inThePast'/>");<%-- 과거 비밀번호 재사용 허용 횟수가 넘었습니다.\\n 다른 비밀번호를 입력하여 주세요. --%>
	lang.fn.set("common.alert.previous.password",		"<spring:message code='common.alert.previous.password'/>");<%-- 이전에 사용하였던 비밀번호는 사용할 수 없습니다. \\n 다른 비밀번호를 입력하여 주세요. --%>
	lang.fn.set("common.alert.change.failed",		"<spring:message code='common.alert.change.failed'/>");<%-- 비밀번호 변경을 실패 하였습니다. --%>


	lang.fn.set("common.alert.noEvaluation",		"<spring:message code='common.alert.noEvaluation'/>");<%-- 평가분배 받은 캠페인이 없습니다. --%>
	lang.fn.set("common.alert.modification.completed",		"<spring:message code='common.alert.modification.completed'/>");<%-- 정보수정이 완료 되었습니다 --%>
	lang.fn.set("common.alert.enter.validDateFormat.",		"<spring:message code='common.alert.enter.validDateFormat'/>");<%-- 비밀번호 변경을 실패 하였습니다. --%>
	
	
	lang.fn.set("common.alert.disconnected.ID",		"<spring:message code='common.alert.disconnected.ID'/>");<%-- ID 중복 접속으로 기존에 접속된 세션이 해제 되었습니다. --%>
	lang.fn.set("common.alert.disconnected.IP",		"<spring:message code='common.alert.disconnected.IP'/>");<%-- IP 중복 접속으로 기존에 접속된 세션이 해제 되었습니다. --%>
	lang.fn.set("common.alert.Unauthorized.IP",		"<spring:message code='common.alert.Unauthorized.IP'/>");<%-- 허가되지 않은 ip입니다. 관리자에게 문의해주세요. --%>
	lang.fn.set("common.alert.notMatchPasswords",		"<spring:message code='common.alert.notMatchPasswords'/>");<%-- 비밀번호가 일치하지 않습니다 --%>
	lang.fn.set("common.alert.expired.password",		"<spring:message code='common.alert.expired.password'/>");<%-- 비밀번호 사용 기한이 만료되어 비밀번호 변경이 필요합니다. \\n 확인을 누르시면 비밀번호 변경 페이지로 이동합니다. --%>
	lang.fn.set("common.alert.required.changePwd",		"<spring:message code='common.alert.required.changePwd'/>");<%-- 비밀번호 변경이 필요합니다. \\n 확인을 누르시면 비밀번호 변경 페이지로 이동 합니다. --%>
	lang.fn.set("common.alert.locked.account",		"<spring:message code='common.alert.locked.account'/>");<%-- 오랫동안 미접속하거나 지속적인 로그인 실패로 계정이 잠긴 상태입니다. \\n 관리자에게 문의하여, 비밀번호를 수정 후 재 로그인 시도를 해 주세요. --%>
	lang.fn.set("common.alert.noPages.accessible",		"<spring:message code='common.alert.noPages.accessible'/>");<%-- 접근 가능한 페이지가 없습니다. 권한을 확인해 주세요. --%>
	lang.fn.set("common.alert.failedLogin.ID",		"<spring:message code='common.alert.failedLogin.ID'/>");<%-- ID 중복 접속으로 로그인에 실패 하였습니다. --%>
	lang.fn.set("common.alert.failedLogin.IP",		"<spring:message code='common.alert.failedLogin.IP'/>");<%-- IP 중복 접속으로 로그인에 실패 하였습니다. --%>
	lang.fn.set("common.alert.expired.session",		"<spring:message code='common.alert.expired.session'/>");<%-- 세션이 만료되었습니다. 다시 로그인 해 주세요. --%>
	
	
	lang.fn.set("common.alert.notListening",		"<spring:message code='common.alert.notListening'/>");<%-- 청취중이  아닙니다. --%>
	lang.fn.set("common.alert.saveDrawing",		"<spring:message code='common.alert.saveDrawing'/>");<%-- 도면을 저장 하시겠습니까? --%>
	lang.fn.set("common.alert.changePwdNext",		"<spring:message code='common.alert.changePwdNext'/>");<%-- 비밀번호를 다음에 변경 하시겠습니까? --%>
	lang.fn.set("common.alert.deleteMemo.file", "<spring:message code='common.alert.deleteMemo.file'/>"); <%-- 현재 파일에 저장된 메모가 모두 삭제됩니다. \\n 정말 삭제 하시겠습니까? --%>
	
	lang.fn.set("login.text.unregisteredId",		"<spring:message code='login.text.unregisteredId'/>");<%-- 미등록 ID --%>
	lang.fn.set("login.text.mismatchIp",		"<spring:message code='login.text.mismatchIp'/>");<%-- IP 불일치 --%>

	lang.fn.set("admin.alert.delType",		"<spring:message code='admin.alert.delType'/>");<%-- 삭제 유형을 선택해주세요. --%>
	lang.fn.set("admin.alert.delFileType",		"<spring:message code='admin.alert.delFileType'/>");<%-- 삭제 파일 유형을 선택해주세요. --%>
	lang.fn.set("admin.alert.delFilePathType",		"<spring:message code='admin.alert.delFilePathType'/>");<%-- 삭제 파일 위치를 선택해주세요. --%>
	lang.fn.set("admin.alert.backType",		"<spring:message code='admin.alert.backType'/>");<%-- 백업 유형을 선택해주세요. --%>
	lang.fn.set("admin.alert.indexType",		"<spring:message code='admin.alert.indexType'/>");<%-- 인덱스 생성 유형 주세요. --%>
	lang.fn.set("admin.alert.logType",		"<spring:message code='admin.alert.logType'/>");<%-- 로그 유형을 선택해주세요. --%>
	
	lang.fn.set("admin.alert.decType",		"<spring:message code='admin.alert.decType'/>");<%-- 로그 유형을 선택해주세요. --%>
	lang.fn.set("admin.alert.overWriteType",		"<spring:message code='admin.alert.overWriteType'/>");<%-- 로그 유형을 선택해주세요. --%>
	lang.fn.set("admin.alert.urlUpdateType",		"<spring:message code='admin.alert.urlUpdateType'/>");<%-- 로그 유형을 선택해주세요. --%>
	lang.fn.set("admin.alert.conformityType",		"<spring:message code='admin.alert.conformityType'/>");<%-- 로그 유형을 선택해주세요. --%>
	
	
	lang.fn.set("admin.alert.scheTime",		"<spring:message code='admin.alert.scheTime'/>");	
	lang.fn.set("admin.alert.scheDate",		"<spring:message code='admin.alert.scheDate'/>");	
	lang.fn.set("admin.alert.scheDayWeek",		"<spring:message code='admin.alert.scheDayWeek'/>");	
	lang.fn.set("admin.alert.delPeriod",		"<spring:message code='admin.alert.delPeriod'/>");<%-- 삭제 기간을 입력해주세요. --%>
	lang.fn.set("admin.alert.delPreriod.under11",		"<spring:message code='admin.alert.delPreriod.under11'/>");<%-- 삭제 개월수는 11 이하로만 입력이 가능합니다. --%>
	lang.fn.set("admin.alert.backupScheCreate",		"<spring:message code='admin.alert.backupScheCreate'/>");	
	lang.fn.set("admin.alert.urlCheck",		"<spring:message code='admin.alert.urlCheck'/>");
	lang.fn.set("admin.alert.del.scheTime",		"<spring:message code='admin.alert.del.scheTime'/>");	
	lang.fn.set("admin.alert.del.scheDate",		"<spring:message code='admin.alert.del.scheDate'/>");	
	lang.fn.set("admin.alert.del.scheDayWeek",		"<spring:message code='admin.alert.del.scheDayWeek'/>");	
	

	lang.fn.set("admin.title.delPeriod",		"<spring:message code='admin.title.delPeriod'/>");<%-- 삭제 기간 --%>
	lang.fn.set("admin.title.delType",		"<spring:message code='admin.title.delType'/>");<%-- 삭제 유형 --%>
	lang.fn.set("admin.title.logType",		"<spring:message code='admin.title.logType'/>");<%-- 로그 유형 --%>
	
	

	
	lang.fn.set("admin.select.title.backPath",		"<spring:message code='admin.select.title.backPath'/>");<%-- 그룹 선택 --%>
	lang.fn.set("admin.select.title.selectGroup",		"<spring:message code='admin.select.title.selectGroup'/>");<%-- 그룹 선택 --%>
	lang.fn.set("admin.select.title.useDelOption",		"<spring:message code='admin.select.title.useDelOption'/>");<%-- 선택 옵션 사용 유무 --%>
	lang.fn.set("admin.select.title.delType",		"<spring:message code='admin.select.title.delType'/>");<%-- 삭제 유형 선택 --%>
	lang.fn.set("admin.select.title.logType",		"<spring:message code='admin.select.title.logType'/>");<%-- 로그 유형 선택 --%>
	lang.fn.set("admin.select.title.delPeriod",		"<spring:message code='admin.select.title.delPeriod'/>");<%-- 기간 --%>
	

	lang.fn.set("admin.select.title.delYear",		"<spring:message code='admin.select.title.delYear'/>");<%-- 년 --%>
	lang.fn.set("admin.select.title.delMonth",		"<spring:message code='admin.select.title.delMonth'/>");<%-- 개월 --%>
	lang.fn.set("admin.select.title.delDay",		"<spring:message code='admin.select.title.delDay'/>");<%-- 일 --%>
	lang.fn.set("admin.select.value.selectType",		"<spring:message code='admin.select.value.selectType'/>");<%-- 유형 선택 --%>
	lang.fn.set("admin.select.value.delType.H",		"<spring:message code='admin.select.value.delType.H'/>");<%-- 내역 숨김 --%>
	lang.fn.set("admin.select.value.delType.D",		"<spring:message code='admin.select.value.delType.D'/>");<%-- 내역 삭제 --%>
	lang.fn.set("admin.select.value.delType.F",		"<spring:message code='admin.select.value.delType.F'/>");<%-- 파일 삭제 --%>
	lang.fn.set("admin.select.value.delType.A",		"<spring:message code='admin.select.value.delType.A'/>");<%-- 내역 및 파일 삭제 --%>
	lang.fn.set("admin.select.value.logType.A",		"<spring:message code='admin.select.value.logType.A'/>");<%-- 상세 기록--%>
	lang.fn.set("admin.select.value.logType.M",		"<spring:message code='admin.select.value.logType.M'/>");<%-- 간단히 기록 --%>



	lang.fn.set("admin.delRecfileInfo.saveSuccess",		"<spring:message code='admin.delRecfileInfo.saveSuccess'/>");<%-- 설정 저장 성공--%>
	lang.fn.set("admin.delRecfileInfo.saveFail",		"<spring:message code='admin.delRecfileInfo.saveFail'/>");<%-- 설정 저장 실패 --%>
	
	lang.fn.set("admin.delRecfileInfo.deleteSuccess",		"<spring:message code='admin.delRecfileInfo.deleteSuccess'/>");<%-- 설정 삭제 성공 --%>
	lang.fn.set("admin.delRecfileInfo.deleteFail",		"<spring:message code='admin.delRecfileInfo.deleteFail'/>");<%-- 설정 삭제 실패 --%>

	lang.fn.set("admin.label.manageEtc.delRecfile",		"<spring:message code='admin.label.manageEtc.delRecfile'/>");<%-- 녹취 삭제 설정 정보 수정 --%>
	lang.fn.set("admin.label.manageEtc.backRecfile",		"<spring:message code='admin.label.manageEtc.backRecfile'/>");<%-- 녹취 백업 설정 정보 수정 --%>
	
	lang.fn.set("admin.delRecfileInfo.modify",		"<spring:message code='admin.delRecfileInfo.modify'/>");<%-- 녹취 삭제 정보를 수정하시겠습니까? --%>

	lang.fn.set("admin.delRecfileInfo.modifySuccess",		"<spring:message code='admin.delRecfileInfo.modifySuccess'/>");<%-- 설정 수정 성공--%>
	lang.fn.set("admin.delRecfileInfo.modifyFail",		"<spring:message code='admin.delRecfileInfo.modifyFail'/>");<%-- 설정 수정 실패 --%>
	
	lang.fn.set("admin.backRecfileInfo.saveSuccess",		"<spring:message code='admin.backRecfileInfo.saveSuccess'/>");<%-- 설정 저장 성공--%>
	lang.fn.set("admin.backRecfileInfo.saveFail",		"<spring:message code='admin.backRecfileInfo.saveFail'/>");<%-- 설정 저장 실패 --%>
	
	lang.fn.set("admin.backRecfileInfo.deleteSuccess",		"<spring:message code='admin.backRecfileInfo.deleteSuccess'/>");<%-- 설정 삭제 성공 --%>
	lang.fn.set("admin.backRecfileInfo.deleteFail",		"<spring:message code='admin.backRecfileInfo.deleteFail'/>");<%-- 설정 삭제 실패 --%>

	lang.fn.set("admin.backRecfileInfo.modify",		"<spring:message code='admin.backRecfileInfo.modify'/>");<%-- 녹취 삭제 정보를 수정하시겠습니까? --%>

	lang.fn.set("admin.backRecfileInfo.modifySuccess",		"<spring:message code='admin.backRecfileInfo.modifySuccess'/>");<%-- 설정 수정 성공--%>
	lang.fn.set("admin.backRecfileInfo.modifyFail",		"<spring:message code='admin.backRecfileInfo.modifyFail'/>");<%-- 설정 수정 실패 --%>
	

	lang.fn.set("admin.etcConfig.label.prefix",				"<spring:message code='admin.etcConfig.label.prefix'/>");<%-- Prefix 제거 옵션 --%>
	lang.fn.set("admin.etcConfig.option.use",				"<spring:message code='admin.etcConfig.option.use'/>");<%-- 사용 --%>
	lang.fn.set("admin.etcConfig.option.nouse",				"<spring:message code='admin.etcConfig.option.nouse'/>");<%-- 사용안함 --%>
	lang.fn.set("admin.etcConfig.alert.prefix.emptyNum",	"<spring:message code='admin.etcConfig.alert.prefix.emptyNum'/>");<%-- 번호를 입력해주세요. --%>
	lang.fn.set("admin.etcConfig.alert.prefix.useNum",		"<spring:message code='admin.etcConfig.alert.prefix.useNum'/>");<%-- 이미 등록된 번호입니다. --%>
	lang.fn.set("admin.etcConfig.alert.prefix.chkDelNum",	"<spring:message code='admin.etcConfig.alert.prefix.chkDelNum'/>");<%-- 삭제할 번호를 선택해주세요. --%>
	

	lang.fn.set("admin.etcConfig.label.masking",				"<spring:message code='admin.etcConfig.label.masking'/>");<%-- 전화번호 마스킹 --%>
	lang.fn.set("admin.etcConfig.label.masking.from",			"<spring:message code='admin.etcConfig.label.masking.from'/>");<%-- 시작 위치 --%>
	lang.fn.set("admin.etcConfig.label.masking.count",			"<spring:message code='admin.etcConfig.label.masking.count'/>");<%-- 마스킹 개수 --%>
	lang.fn.set("admin.etcConfig.alert.masking.emptyFrom",		"<spring:message code='admin.etcConfig.alert.masking.emptyFrom'/>");<%-- 마스킹 할 시작 위치를 입력해주세요. --%>
	lang.fn.set("admin.etcConfig.alert.masking.emptyCount",		"<spring:message code='admin.etcConfig.alert.masking.emptyCount'/>");<%-- 마스킹 개수를 입력해주세요.  --%>
	

	lang.fn.set("admin.etcConfig.label.hyphen",				"<spring:message code='admin.etcConfig.label.hyphen'/>");<%-- 전화번호 표기옵션(-) --%>
	lang.fn.set("admin.etcConfig.option.hyphen.val2",			"<spring:message code='admin.etcConfig.option.hyphen.val2'/>");<%-- 2자리 --%>
	lang.fn.set("admin.etcConfig.option.hyphen.val3",			"<spring:message code='admin.etcConfig.option.hyphen.val3'/>");<%-- 3자리 --%>
	lang.fn.set("admin.etcConfig.option.hyphen.val4",			"<spring:message code='admin.etcConfig.option.hyphen.val4'/>");<%-- 4자리 --%>
	lang.fn.set("admin.etcConfig.option.direct",			"<spring:message code='admin.etcConfig.option.direct'/>");<%-- 직접입력 --%>
	lang.fn.set("admin.etcConfig.option.nouse",				"<spring:message code='admin.etcConfig.option.nouse'/>");<%-- 사용안함 --%>

	lang.fn.set("admin.etcConfig.alert.hyphen.emptyNum",	"<spring:message code='admin.etcConfig.alert.hyphen.emptyNum'/>");<%-- 첫번째 전화번호 표기 개수를 입력해주세요. --%>
	lang.fn.set("admin.etcConfig.alert.hyphen.under10",		"<spring:message code='admin.etcConfig.alert.hyphen.under10'/>");<%-- 첫번째 전화번호 표기 개수는 10이하로 입력해주세요. --%>


	lang.fn.set("admin.etcConfig.alert.addSuccess",		"<spring:message code='admin.etcConfig.alert.addSuccess'/>");<%-- 추가 성공 --%>
	lang.fn.set("admin.etcConfig.alert.addFail",		"<spring:message code='admin.etcConfig.alert.addFail'/>");<%-- 추가 실패 --%>
	lang.fn.set("admin.etcConfig.alert.delSuccess",		"<spring:message code='admin.etcConfig.alert.delSuccess'/>");<%-- 삭제 성공 --%>
	lang.fn.set("admin.etcConfig.alert.delFail",		"<spring:message code='admin.etcConfig.alert.delFail'/>");<%-- 삭제 실패 --%>
	lang.fn.set("admin.etcConfig.alert.modifySuccess",	"<spring:message code='admin.etcConfig.alert.modifySuccess'/>");<%-- 변경 성공 --%>
	lang.fn.set("admin.etcConfig.alert.modifyFail",		"<spring:message code='admin.etcConfig.alert.modifyFail'/>");<%-- 변경 실패 --%>
	

	lang.fn.set("admin.etcConfig.btn.add",		"<spring:message code='admin.etcConfig.btn.add'/>");<%-- 추가 --%>
	lang.fn.set("admin.etcConfig.btn.del",		"<spring:message code='admin.etcConfig.btn.del'/>");<%-- 삭제 --%>
	lang.fn.set("admin.etcConfig.btn.set",		"<spring:message code='admin.etcConfig.btn.set'/>");<%-- 설정 --%>
	
	lang.fn.set("admin.etcConfig.confirm.delRecfile1",      "<spring:message code='admin.etcConfig.confirm.delRecfile1'/>");<%-- 선택한 그룹의 --%>
	lang.fn.set("admin.etcConfig.confirm.delRecfile2",      "<spring:message code='admin.etcConfig.confirm.delRecfile2'/>");<%-- 개월 이전 파일이 삭제됩니다. --%>
	lang.fn.set("admin.etcConfig.confirm.delRecfile3",      "<spring:message code='admin.etcConfig.confirm.delRecfile3'/>");<%-- 정말 삭제하시겠습니까? --%>
	
	lang.fn.set("admin.etcConfig.confirm.backRecfile1",      "<spring:message code='admin.etcConfig.confirm.backRecfile1'/>");<%-- 선택한 그룹의 --%>
	lang.fn.set("admin.etcConfig.confirm.backRecfile2",      "<spring:message code='admin.etcConfig.confirm.backRecfile2'/>");<%-- 파일이 백업됩니다. --%>
	lang.fn.set("admin.etcConfig.confirm.backRecfile3",      "<spring:message code='admin.etcConfig.confirm.backRecfile3'/>");<%-- 정말 백업하시겠습니까? --%>

	lang.fn.set("views.search.grid.head.R_DIVISION",      "<spring:message code='views.search.grid.head.R_DIVISION'/>");<%-- 분배 --%>
	lang.fn.set("views.search.grid.head.R_REC_VISIBLE",      "<spring:message code='views.search.grid.head.R_REC_VISIBLE'/>");<%-- 삭제 파일 --%>
	lang.fn.set("views.search.grid.head.R_COMPANY_TELNO",      "<spring:message code='views.search.grid.head.R_COMPANY_TELNO'/>");<%-- 자번호 --%>
	lang.fn.set("views.search.grid.head.R_COMPANY_TELNO_NICK",      "<spring:message code='views.search.grid.head.R_COMPANY_TELNO_NICK'/>");<%-- 자번호닉네임 --%>


	lang.fn.set("views.search.alert.fileDivision",      "<spring:message code='views.search.alert.fileDivision'/>");<%-- 선택 파일 분배 --%>
	

	lang.fn.set("header.menu.label.transcript",      		 "<spring:message code='header.menu.label.transcript'/>");<%-- 전사 관리 --%>
	lang.fn.set("views.search.title.transcript",    		 "<spring:message code='views.search.title.transcript'/>");<%-- 전사 --%>

	lang.fn.set("header.menu.label.learningTranscript",      "<spring:message code='header.menu.label.learningTranscript'/>");<%-- 전사 학습관리 --%>
	lang.fn.set("header.menu.label.sttModel",     	 		 "<spring:message code='header.menu.label.sttModel'/>");<%-- STT모델 관리 --%>
	lang.fn.set("header.menu.label.sttEnginState",     		 "<spring:message code='header.menu.label.sttEnginState'/>");<%-- STT적용모델 정보 --%>
	lang.fn.set("header.menu.label.sttResultManage",     		 "<spring:message code='header.menu.label.sttResultManage'/>");<%-- STT 관리--%>
	
	lang.fn.set("views.search.title.noSelect",     			 "<spring:message code='views.search.title.noSelect'/>");<%-- 선택 안함 --%>
	
	lang.fn.set("evaluation.result.placeholder.rejectCount", "<spring:message code='evaluation.result.placeholder.rejectCount'/>");<%-- 거부 횟수 --%>
	lang.fn.set("evaluation.result.placeholder.rejectDate",  "<spring:message code='evaluation.result.placeholder.rejectDate'/>");<%-- 거부 날짜 --%>


	lang.fn.set("evaluation.result.title.first.evaluator",      "<spring:message code='evaluation.result.title.first.evaluator'/>");<%-- 1차 평가자--%>
	lang.fn.set("evaluation.result.title.first.evaluatorOp",      "<spring:message code='evaluation.result.title.first.evaluatorOp'/>");<%-- 1차 평가자 의견 --%>
	lang.fn.set("evaluation.result.title.second.evaluator",      "<spring:message code='evaluation.result.title.second.evaluator'/>");<%-- 2차 평가자 --%>
	lang.fn.set("evaluation.result.title.second.evaluatorOp",      "<spring:message code='evaluation.result.title.second.evaluatorOp'/>");<%-- 2차 평가자 의견 --%>
	lang.fn.set("evaluation.result.title.agentOp",     			 "<spring:message code='evaluation.result.title.agentOp'/>");<%-- 상담원 의견 --%>
	

	lang.fn.set("views.search.alert.select.agent",     			 	"<spring:message code='views.search.alert.select.agent'/>");<%-- 녹취 파일을 할당할 사용자를 선택해주세요. --%>
	lang.fn.set("views.search.alert.already.division",     			"<spring:message code='views.search.alert.already.division'/>");<%-- 선택한 파일 중 이미 분배된 녹취 파일이 있습니다. --%>
	lang.fn.set("views.transcript.alert.already.division",     			"<spring:message code='views.transcript.alert.already.division'/>");<%-- 선택한 파일 중 이미 분배된 녹취 파일이 있습니다. 재분배하시겠습니까?--%>
	
	lang.fn.set("views.search.alert.division.success",     			"<spring:message code='views.search.alert.division.success'/>");<%-- 분배리스트에 선택한 녹취파일을 추가하였습니다. --%>
	lang.fn.set("views.search.alert.division.fail",     			"<spring:message code='views.search.alert.division.fail'/>");<%-- 분배리스트에 선택한 녹취파일 추가를 실패 하였습니다. --%>
	
	lang.fn.set("views.transcript.alert.learning.success",     		"<spring:message code='views.transcript.alert.learning.success'/>");<%-- 학습 요청에 성공했습니다. --%>
	lang.fn.set("views.transcript.alert.learning.fail",     		"<spring:message code='views.transcript.alert.learning.fail'/>");<%-- 학습 요청에 실패했습니다. --%>
	lang.fn.set("views.transcript.confirm.learning",     			"<spring:message code='views.transcript.confirm.learning'/>");<%-- 선택한 파일들을 학습하시겠습니까? --%>
	lang.fn.set("views.transcript.confirm.remove",     			 	"<spring:message code='views.transcript.confirm.remove'/>");<%-- 파일들을 리스트에서 제거하시겠습니까? 전사된 파일의 경우 전사 내용이 삭제됩니다. --%>
	lang.fn.set("views.transcript.alert.remove.success",     		"<spring:message code='views.transcript.alert.remove.success'/>");<%-- 파일이 성공적으로 제거되었습니다. --%>
	lang.fn.set("views.transcript.alert.remove.fail",     			"<spring:message code='views.transcript.alert.remove.fail'/>");<%-- 파일 제거에 실패했습니다. --%>

	lang.fn.set("views.transcript.button.localFileUpload",     		"<spring:message code='views.transcript.button.localFileUpload'/>");<%-- 로컬파일 업로드 --%>
	lang.fn.set("views.transcript.button.remove",     			 	"<spring:message code='views.transcript.button.remove'/>");<%-- 분배 파일 제거 --%>
	lang.fn.set("views.transcript.button.dataset",     				"<spring:message code='views.transcript.button.dataset'/>");<%-- 데이터 셋 생성 --%>
	lang.fn.set("views.transcript.button.division",     			"<spring:message code='views.transcript.button.division'/>");<%-- 분배 --%>
	lang.fn.set("views.transcript.button.close",     				"<spring:message code='views.transcript.button.close'/>");<%-- 닫기 --%>

	lang.fn.set("views.transcript.alert.noFile",     			 	"<spring:message code='views.transcript.alert.noFile'/>");<%-- 파일이 없습니다. --%>
	lang.fn.set("views.transcript.alert.noTranscript",     			"<spring:message code='views.transcript.alert.noTranscript'/>");<%-- 전사 된 내용이 없습니다. --%>
	lang.fn.set("views.transcript.alert.noTranscript.cust",     	"<spring:message code='views.transcript.alert.noTranscript.cust'/>");<%-- 전사 된 고객 대화 내용이 없습니다. --%>
	lang.fn.set("views.transcript.alert.noTranscript.agent",     	"<spring:message code='views.transcript.alert.noTranscript.agent'/>");<%-- 전사 된 상담사 대화 내용이 없습니다. --%>
	
	lang.fn.set("views.transcript.alert.transcript.save.success",   "<spring:message code='views.transcript.alert.transcript.save.success'/>");<%-- 전사 내용이 저장되었습니다. --%>
	lang.fn.set("views.transcript.alert.transcript.save.fail",      "<spring:message code='views.transcript.alert.transcript.save.fail'/>");<%-- 전사 내용 저장에 실패했습니다. --%>
	
	lang.fn.set("views.transcript.alert.transcript.modify.success", "<spring:message code='views.transcript.alert.transcript.modify.success'/>");<%-- 전사 내용이 수정되었습니다. --%>
	lang.fn.set("views.transcript.alert.transcript.modify.fail",    "<spring:message code='views.transcript.alert.transcript.modify.fail'/>");<%-- 전사 내용 수정에 실패했습니다. --%>
	
	lang.fn.set("views.transcript.alert.transcript.del.success",    "<spring:message code='views.transcript.alert.transcript.del.success'/>");<%-- 전사 내용이 삭제되었습니다. --%>
	lang.fn.set("views.transcript.alert.transcript.del.fail",       "<spring:message code='views.transcript.alert.transcript.del.fail'/>");<%-- 전사 내용 삭제에 실패했습니다. --%>
	lang.fn.set("views.stt.alert.del.success",      				"<spring:message code='views.stt.alert.del.success'/>");<%-- 데에터셋이 삭제되었습니다. --%>
	lang.fn.set("views.stt.alert.del.fail",       					"<spring:message code='views.stt.alert.del.fail'/>");<%-- 데이터셋 삭제에 실패했습니다. --%>
	
	lang.fn.set("views.transcript.confirm.transcript.save",     	"<spring:message code='views.transcript.confirm.transcript.save'/>");<%-- 현재 파일의 전사 내용을 저장하시겠습니까? --%>
	lang.fn.set("views.transcript.confirm.transcript.modify",     	"<spring:message code='views.transcript.confirm.transcript.modify'/>");<%-- 현재 파일의 전사 내용을 수정하시겠습니까? --%>
	lang.fn.set("views.transcript.confirm.transcript.del",     		"<spring:message code='views.transcript.confirm.transcript.del'/>");<%-- 현재 파일의 전사 내용을 삭제하시겠습니까? --%>
	
	lang.fn.set("views.transcript.alert.noTranscriptFile",     		"<spring:message code='views.transcript.alert.noTranscriptFile'/>");<%-- 요청한 파일 중 전사되지 않은 파일이 존재합니다. --%>
	
	lang.fn.set("views.transcript.title.list",     					"<spring:message code='views.transcript.title.list'/>");<%-- 전사파일 리스트 --%>
	lang.fn.set("views.transcript.label.modelStatus",     			"<spring:message code='views.transcript.label.modelStatus'/>");<%-- 상태 --%>
	
	lang.fn.set("views.transcript.label.msg1",     					"<spring:message code='views.transcript.label.msg1'/>");<%-- 로컬파일 업로드 --%>
	lang.fn.set("views.transcript.label.msg2",     					"<spring:message code='views.transcript.label.msg2'/>");<%-- 파일을 선택하세요. --%>
	lang.fn.set("views.transcript.label.msg3",     					"<spring:message code='views.transcript.label.msg3'/>");<%-- 파일찾기 --%>
	lang.fn.set("views.transcript.label.msg4",     					"<spring:message code='views.transcript.label.msg4'/>");<%-- 업로드 --%>
	lang.fn.set("views.transcript.alert.msg1",     					"<spring:message code='views.transcript.alert.msg1'/>");<%-- 파일이 없습니다. --%>
	lang.fn.set("views.transcript.alert.msg2",     					"<spring:message code='views.transcript.alert.msg2'/>");<%-- 파일형식이 적합하지 않습니다.\\n(업로드 가능한 형식:mp3/mp4/wav) --%>
	lang.fn.set("views.transcript.alert.msg3",     					"<spring:message code='views.transcript.alert.msg3'/>");<%-- 파일형식이 적합하지 않습니다.\\n (업로드 가능한 형식:txt) --%>
	
	lang.fn.set("views.sttDataset.title.dataset",     				"<spring:message code='views.sttDataset.title.dataset'/>");<%-- 데이터셋 생성 --%>
	
	lang.fn.set("views.sttDataset.label.rDatasetName",     			"<spring:message code='views.sttDataset.label.rDatasetName'/>");<%-- 데이터셋명 --%>
	lang.fn.set("views.sttDataset.label.rDatasetDate",     			"<spring:message code='views.sttDataset.label.rDatasetDate'/>");<%-- 데이터셋 생성일자 --%>
	lang.fn.set("views.sttDataset.label.rDatasetTime",     			"<spring:message code='views.sttDataset.label.rDatasetTime'/>");<%-- 데이터셋 생성시간 --%>
	lang.fn.set("views.sttDataset.label.rDatasetSize",     			"<spring:message code='views.sttDataset.label.rDatasetSize'/>");<%-- 데이터 개수 --%>
	
	lang.fn.set("views.sttDataset.button.learn",     				"<spring:message code='views.sttDataset.button.learn'/>");<%-- 학습 --%>
	lang.fn.set("views.sttDataset.button.delete",     				"<spring:message code='views.sttDataset.button.delete'/>");<%-- 삭제 --%>
	lang.fn.set("views.sttDataset.button.dataset",     				"<spring:message code='views.sttDataset.button.dataset'/>");<%-- 생성 --%>

	lang.fn.set("views.sttDataset.search.R_DATASET_NAME",     		"<spring:message code='views.sttDataset.search.R_DATASET_NAME'/>");<%-- 데이터셋 --%>

	lang.fn.set("views.sttDataset.alert.msg1",     					"<spring:message code='views.sttDataset.alert.msg1'/>");<%-- 데이터셋명을 입력해주세요. --%>
	lang.fn.set("views.sttDataset.alert.msg2",     					"<spring:message code='views.sttDataset.alert.msg2'/>");<%-- 이미 등록된 데이터셋명 입니다. --%>
	lang.fn.set("views.sttDataset.alert.msg3",     					"<spring:message code='views.sttDataset.alert.msg3'/>");<%-- 데이터셋을 생성했습니다. --%>
	lang.fn.set("views.sttDataset.alert.msg4",     					"<spring:message code='views.sttDataset.alert.msg4'/>");<%-- 데이터셋 생성에 실패했습니다. --%>
	
	lang.fn.set("views.sttModel.title.model",     					"<spring:message code='views.sttModel.title.model'/>");<%-- 모델 생성 --%>
	
	lang.fn.set("views.sttModel.label.rCteateDate",     			"<spring:message code='views.sttModel.label.rCteateDate'/>");<%-- 학습요청 날짜 --%>
	lang.fn.set("views.sttModel.label.rCteateTime",     			"<spring:message code='views.sttModel.label.rCteateTime'/>");<%-- 학습요청 시간 --%>
	lang.fn.set("views.sttModel.label.rStartDate",     				"<spring:message code='views.sttModel.label.rStartDate'/>");<%-- 학습시작 날짜 --%>
	lang.fn.set("views.sttModel.label.rStartTime",     				"<spring:message code='views.sttModel.label.rStartTime'/>");<%-- 학습시작 시간 --%>
	lang.fn.set("views.sttModel.label.rEndDate",     				"<spring:message code='views.sttModel.label.rEndDate'/>");<%-- 학습종료 날짜 --%>
	lang.fn.set("views.sttModel.label.rEndTime",     				"<spring:message code='views.sttModel.label.rEndTime'/>");<%-- 학습시작 시간 --%>
	lang.fn.set("views.sttModel.label.rType",     					"<spring:message code='views.sttModel.label.rType'/>");<%-- 모델 타입 --%>
	lang.fn.set("views.sttModel.label.rModelName",     				"<spring:message code='views.sttModel.label.rModelName'/>");<%-- 모델명 --%>
	lang.fn.set("views.sttModel.label.rBaseModel",     				"<spring:message code='views.sttModel.label.rBaseModel'/>");<%-- 베이스 모델 --%>
	lang.fn.set("views.sttModel.label.rDataTotalCount",     		"<spring:message code='views.sttModel.label.rDataTotalCount'/>");<%-- 데이터 개수 --%>
	lang.fn.set("views.sttModel.label.rDataTotalTime",     			"<spring:message code='views.sttModel.label.rDataTotalTime'/>");<%-- 데이터 총 시간 --%>
	lang.fn.set("views.sttModel.label.rWorkStatus",     			"<spring:message code='views.sttModel.label.rWorkStatus'/>");<%-- 학습상태 --%>
	lang.fn.set("views.sttModel.label.rWorkStatusMsg",     			"<spring:message code='views.sttModel.label.rWorkStatusMsg'/>");<%-- 학습상태 메시지 --%>
	lang.fn.set("views.sttModel.label.rRecogRate",     				"<spring:message code='views.sttModel.label.rRecogRate'/>");<%-- 인식률 --%>
	lang.fn.set("views.sttModel.label.rRecogRateMsg",     			"<spring:message code='views.sttModel.label.rRecogRateMsg'/>");<%-- 인식률 메시지 --%>
	lang.fn.set("views.sttModel.label.rDatasetName",     			"<spring:message code='views.sttModel.label.rDatasetName'/>");<%-- 데이터셋 이름 --%>
	lang.fn.set("views.sttModel.label.all",     					"<spring:message code='views.sttModel.label.all'/>");<%-- 전체 --%>
	lang.fn.set("views.sttModel.label.am",     						"<spring:message code='views.sttModel.label.am'/>");<%-- 음향모델 --%>
	lang.fn.set("views.sttModel.label.lm",     						"<spring:message code='views.sttModel.label.lm'/>");<%-- 언어모델 --%>
	lang.fn.set("views.sttModel.label.recog",     					"<spring:message code='views.sttModel.label.recog'/>");<%-- 인식률테스트 --%>
	lang.fn.set("views.sttModel.label.f",     						"<spring:message code='views.sttModel.label.f'/>");<%-- 학습완료 --%>
	lang.fn.set("views.sttModel.label.w",     						"<spring:message code='views.sttModel.label.w'/>");<%-- 학습대기 --%>
	lang.fn.set("views.sttModel.label.r",     						"<spring:message code='views.sttModel.label.r'/>");<%-- 학습중 --%>
	lang.fn.set("views.sttModel.label.e",     						"<spring:message code='views.sttModel.label.e'/>");<%-- 학습실패 --%>
	lang.fn.set("views.sttModel.label.rApply",     					"<spring:message code='views.sttModel.label.rApply'/>");<%-- 적용 --%>
	lang.fn.set("views.sttModel.label.rApplyN",     				"<spring:message code='views.sttModel.label.rApplyN'/>");<%-- 적용요청 --%>
	lang.fn.set("views.sttModel.label.rApplyY",     				"<spring:message code='views.sttModel.label.rApplyY'/>");<%-- 모델적용 --%>
	lang.fn.set("views.sttModel.label.server",   	  				"<spring:message code='views.sttModel.label.server'/>");<%-- 서버 --%>
	lang.fn.set("views.sttModel.label.applyServer",   	  			"<spring:message code='views.sttModel.label.applyServer'/>");<%-- 적용서버 --%>
	lang.fn.set("views.sttModel.label.allServer",   	  			"<spring:message code='views.sttModel.label.allServer'/>");<%-- 전체서버적용 --%>

	lang.fn.set("views.sttModel.search.R_MODEL_NAME",     			"<spring:message code='views.sttModel.search.R_MODEL_NAME'/>");<%-- 모델명 --%>
	lang.fn.set("views.sttModel.search.R_TYPE",	 	    			"<spring:message code='views.sttModel.search.R_TYPE'/>");<%-- 모델타입 --%>
	lang.fn.set("views.sttModel.search.R_WORK_STATUS",	 	   		"<spring:message code='views.sttModel.search.R_WORK_STATUS'/>");<%-- 학습상태 --%>

	lang.fn.set("views.sttModel.alert.msg1",     					"<spring:message code='views.sttModel.alert.msg1'/>");<%-- 요청 일자를 다음 형식에 맞게 입력 해 주세요!\\n예)yyyy-mm-dd --%>
	lang.fn.set("views.sttModel.alert.msg2",     					"<spring:message code='views.sttModel.alert.msg2'/>");<%-- 선택된 모델이 없습니다. --%>
	lang.fn.set("views.sttModel.alert.msg3",     					"<spring:message code='views.sttModel.alert.msg3'/>");<%-- 모델명을 입력하세요. --%>
	lang.fn.set("views.sttModel.alert.msg4",     					"<spring:message code='views.sttModel.alert.msg4'/>");<%-- 모델타입을 선택하세요. --%>
	lang.fn.set("views.sttModel.alert.msg5",     					"<spring:message code='views.sttModel.alert.msg5'/>");<%-- 이미 등록된 모델명 입니다. --%>
	lang.fn.set("views.sttModel.alert.msg6",     					"<spring:message code='views.sttModel.alert.msg6'/>");<%-- 학습 요청에 성공했습니다. --%>
	lang.fn.set("views.sttModel.alert.msg7",     					"<spring:message code='views.sttModel.alert.msg7'/>");<%-- 학습 요청에 실패했습니다. --%>
	lang.fn.set("views.sttModel.alert.msg8",     					"<spring:message code='views.sttModel.alert.msg8'/>");<%-- 모델 적용 요청을 완료했습니다. --%>
	lang.fn.set("views.sttModel.alert.msg9",     					"<spring:message code='views.sttModel.alert.msg9'/>");<%-- 모델 적용 요청에 실패했습니다. --%>
	lang.fn.set("views.sttModel.alert.msg10",     					"<spring:message code='views.sttModel.alert.msg10'/>");<%-- 적용해제 요청을 완료했습니다. --%>
	lang.fn.set("views.sttModel.alert.msg11",     					"<spring:message code='views.sttModel.alert.msg11'/>");<%-- 적용해제 요청에 실패했습니다. --%>
	lang.fn.set("views.sttModel.alert.msg12",     					"<spring:message code='views.sttModel.alert.msg12'/>");<%-- 사용할 수 없는 모델명 입니다. --%>
	lang.fn.set("views.sttModel.alert.msg13",     					"<spring:message code='views.sttModel.alert.msg13'/>");<%-- 적용할 모델을 선택해 주세요. --%>

	lang.fn.set("views.sttModel.confirm.remove",     				"<spring:message code='views.sttModel.confirm.remove'/>");<%-- 선택한 모델을 제거하시겠습니까?\\n 해당 모델을 참조하는 모델이 전부 삭제 됩니다. --%>
	lang.fn.set("views.sttModel.confirm.msg1",     					"<spring:message code='views.sttModel.confirm.msg1'/>");<%-- 이미 적용된 모델이 있는 경우 해제 후 진행됩니다. --%>
	lang.fn.set("views.sttModel.confirm.msg2",     					"<spring:message code='views.sttModel.confirm.msg2'/>");<%-- 적용된 모델을 해제하시겠습니까? --%>

	lang.fn.set("views.sttModel.button.modelRemove",     			"<spring:message code='views.sttModel.button.modelRemove'/>");<%-- 선택모델삭제 --%>
	
	lang.fn.set("views.sttResult.label.all",     					"<spring:message code='views.sttResult.label.all'/>");<%-- 전체 --%>

	lang.fn.set("views.sttEnginState.label.rSysCode",     			"<spring:message code='views.sttEnginState.label.rSysCode'/>");<%-- 시스템코드 --%>
	lang.fn.set("views.sttEnginState.label.rSysName",     			"<spring:message code='views.sttEnginState.label.rSysName'/>");<%-- 시스템명 --%>
	lang.fn.set("views.sttEnginState.label.rSysIp",     			"<spring:message code='views.sttEnginState.label.rSysIp'/>");<%-- IP --%>
	lang.fn.set("views.sttEnginState.label.rUpdateReqDate",     	"<spring:message code='views.sttEnginState.label.rUpdateReqDate'/>");<%-- 모델적용 요청일자 --%>
	lang.fn.set("views.sttEnginState.label.rUpdateReqTime",     	"<spring:message code='views.sttEnginState.label.rUpdateReqTime'/>");<%-- 모델적용 요청시간 --%>
	lang.fn.set("views.sttEnginState.label.rUpdateProcDate",     	"<spring:message code='views.sttEnginState.label.rUpdateProcDate'/>");<%-- 적용날짜 --%>
	lang.fn.set("views.sttEnginState.label.rUpdateProcTime",     	"<spring:message code='views.sttEnginState.label.rUpdateProcTime'/>");<%-- 적용시간 --%>
	lang.fn.set("views.sttEnginState.label.rModelName",	    		"<spring:message code='views.sttEnginState.label.rModelName'/>");<%-- 모델명 --%>
	lang.fn.set("views.sttEnginState.label.rType",		    		"<spring:message code='views.sttEnginState.label.rType'/>");<%-- 모델타입 --%>
	lang.fn.set("views.sttEnginState.label.rWorkState",    			"<spring:message code='views.sttEnginState.label.rWorkState'/>");<%-- 상태 --%>
	lang.fn.set("views.sttEnginState.label.r",    					"<spring:message code='views.sttEnginState.label.r'/>");<%-- 적용중 --%>
	lang.fn.set("views.sttEnginState.label.u",    					"<spring:message code='views.sttEnginState.label.u'/>");<%-- 업데이트중 --%>
	lang.fn.set("views.sttEnginState.label.e",    					"<spring:message code='views.sttEnginState.label.e'/>");<%-- 적용실패 --%>
	lang.fn.set("views.sttEnginState.label.am",    					"<spring:message code='views.sttEnginState.label.am'/>");<%-- 음향모델 --%>
	lang.fn.set("views.sttEnginState.label.lm",    					"<spring:message code='views.sttEnginState.label.lm'/>");<%-- 언어모델 --%>

	lang.fn.set("views.sttEnginState.search.R_SYS_CODE",    		"<spring:message code='views.sttEnginState.search.R_SYS_CODE'/>");<%-- 시스템코드 --%>
	lang.fn.set("views.sttEnginState.search.R_SYS_IP",    			"<spring:message code='views.sttEnginState.search.R_SYS_IP'/>");<%-- IP --%>
	lang.fn.set("views.sttEnginState.search.R_MODEL_NAME",    		"<spring:message code='views.sttEnginState.search.R_MODEL_NAME'/>");<%-- 모델명 --%>
	lang.fn.set("views.sttEnginState.search.R_TYPE",    			"<spring:message code='views.sttEnginState.search.R_TYPE'/>");<%-- 모델타입 --%>
	lang.fn.set("views.sttEnginState.search.R_WORK_STATUS",    		"<spring:message code='views.sttEnginState.search.R_WORK_STATUS'/>");<%-- 상태 --%>
	
	lang.fn.set("admin.detail.option.daily",	 "<spring:message code='admin.detail.option.daily'/>");
	lang.fn.set("admin.detail.option.weekly",	 "<spring:message code='admin.detail.option.weekly'/>");
	lang.fn.set("admin.detail.option.monthly",	 "<spring:message code='admin.detail.option.monthly'/>");
	lang.fn.set("admin.detail.option.noSche",	 "<spring:message code='admin.detail.option.noSche'/>");	
	lang.fn.set("admin.backup.label.mon",		 "<spring:message code='admin.backup.label.mon'/>");
	lang.fn.set("admin.backup.label.week",		 "<spring:message code='admin.backup.label.week'/>");
	lang.fn.set("admin.backup.label.day",		 "<spring:message code='admin.backup.label.day'/>");
	lang.fn.set("admin.backup.label.time",		 "<spring:message code='admin.backup.label.time'/>");
	lang.fn.set("admin.backup.label.min",		 "<spring:message code='admin.backup.label.min'/>");
	lang.fn.set("views.search.alert.recfile.division.success",		 "<spring:message code='views.search.alert.recfile.division.success'/>");
	lang.fn.set("views.search.alert.recfile.division.fail",		 	"<spring:message code='views.search.alert.recfile.division.fail'/>");
	
	lang.fn.set("admin.label.bgName",		 	"<spring:message code='admin.label.bgName'/>");
	lang.fn.set("admin.label.mgName",		 	"<spring:message code='admin.label.mgName'/>");	
	
	lang.fn.set("log.ChannelInfo.ChannelInfo", "<spring:message code='log.ChannelInfo.ChannelInfo'/>");
	lang.fn.set("log.ChannelInfo.chNum", "<spring:message code='log.ChannelInfo.chNum'/>");
	lang.fn.set("log.ChannelInfo.oldSysCode", "<spring:message code='log.ChannelInfo.oldSysCode'/>");
	lang.fn.set("log.ChannelInfo.sysCode", "<spring:message code='log.ChannelInfo.sysCode'/>");
	lang.fn.set("log.ChannelInfo.extNum", "<spring:message code='log.ChannelInfo.extNum'/>");
	lang.fn.set("log.ChannelInfo.extIp", "<spring:message code='log.ChannelInfo.extIp'/>");
	lang.fn.set("log.ChannelInfo.recYn", "<spring:message code='log.ChannelInfo.recYn'/>");
	lang.fn.set("log.ChannelInfo.extKind", "<spring:message code='log.ChannelInfo.extKind'/>");
	lang.fn.set("log.ChannelInfo.recType", "<spring:message code='log.ChannelInfo.recType'/>");
	lang.fn.set("log.ChannelInfo.screenYn", "<spring:message code='log.ChannelInfo.screenYn'/>");
	lang.fn.set("log.DBManage.DBManage", "<spring:message code='log.DBManage.DBManage'/>");
	lang.fn.set("log.DBManage.dbName", "<spring:message code='log.DBManage.dbName'/>");
	lang.fn.set("log.DBManage.dbServer", "<spring:message code='log.DBManage.dbServer'/>");
	lang.fn.set("log.DBManage.url", "<spring:message code='log.DBManage.url'/>");
	lang.fn.set("log.DBManage.id", "<spring:message code='log.DBManage.id'/>");
	lang.fn.set("log.DBManage.pw", "<spring:message code='log.DBManage.pw'/>");
	lang.fn.set("log.DBManage.timeout", "<spring:message code='log.DBManage.timeout'/>");
	lang.fn.set("log.ExecuteManage.ExecuteManage", "<spring:message code='log.ExecuteManage.ExecuteManage'/>");
	lang.fn.set("log.ExecuteManage.executeName", "<spring:message code='log.ExecuteManage.executeName'/>");
	lang.fn.set("log.ExecuteManage.jobName", "<spring:message code='log.ExecuteManage.jobName'/>");
	lang.fn.set("log.ExecuteManage.rSchedulerSelect", "<spring:message code='log.ExecuteManage.rSchedulerSelect'/>");
	lang.fn.set("log.ExecuteManage.rSchedulerWeek", "<spring:message code='log.ExecuteManage.rSchedulerWeek'/>");
	lang.fn.set("log.ExecuteManage.rSchedulerDay", "<spring:message code='log.ExecuteManage.rSchedulerDay'/>");
	lang.fn.set("log.ExecuteManage.rSchedulerHour", "<spring:message code='log.ExecuteManage.rSchedulerHour'/>");
	lang.fn.set("log.ExecuteManage.rExecuteFlag", "<spring:message code='log.ExecuteManage.rExecuteFlag'/>");
	lang.fn.set("log.ExecuteManage.rExecuteDate", "<spring:message code='log.ExecuteManage.rExecuteDate'/>");
	lang.fn.set("log.ExecuteManage.rExecuteTime", "<spring:message code='log.ExecuteManage.rExecuteTime'/>");
	lang.fn.set("log.ExecuteManage.rCompleteDate", "<spring:message code='log.ExecuteManage.rCompleteDate'/>");
	lang.fn.set("log.ExecuteManage.rCompleteTime", "<spring:message code='log.ExecuteManage.rCompleteTime'/>");
	lang.fn.set("log.ExecuteManage.rErrorMessage", "<spring:message code='log.ExecuteManage.rErrorMessage'/>");
	lang.fn.set("log.JobManage.JobManage", "<spring:message code='log.JobManage.JobManage'/>");
	lang.fn.set("log.JobManage.jobName", "<spring:message code='log.JobManage.jobName'/>");
	lang.fn.set("log.JobManage.dbName", "<spring:message code='log.JobManage.dbName'/>");
	lang.fn.set("log.JobManage.sqlName", "<spring:message code='log.JobManage.sqlName'/>");
	lang.fn.set("log.RUserInfo.RUserInfo", "<spring:message code='log.RUserInfo.RUserInfo'/>");
	lang.fn.set("log.RUserInfo.logContents", "<spring:message code='log.RUserInfo.logContents'/>");
	lang.fn.set("log.RUserInfo.userId", "<spring:message code='log.RUserInfo.userId'/>");
	lang.fn.set("log.RUserInfo.userName", "<spring:message code='log.RUserInfo.userName'/>");
	lang.fn.set("log.RUserInfo.bgCode", "<spring:message code='log.RUserInfo.bgCode'/>");
	lang.fn.set("log.RUserInfo.mgCode", "<spring:message code='log.RUserInfo.mgCode'/>");
	lang.fn.set("log.RUserInfo.sgCode", "<spring:message code='log.RUserInfo.sgCode'/>");
	lang.fn.set("log.RUserInfo.bgName", "<spring:message code='log.RUserInfo.bgName'/>");
	lang.fn.set("log.RUserInfo.mgName", "<spring:message code='log.RUserInfo.mgName'/>");
	lang.fn.set("log.RUserInfo.sgName", "<spring:message code='log.RUserInfo.sgName'/>");
	lang.fn.set("log.RUserInfo.userPhone", "<spring:message code='log.RUserInfo.userPhone'/>");
	lang.fn.set("log.RUserInfo.userLevel", "<spring:message code='log.RUserInfo.userLevel'/>");
	lang.fn.set("log.RUserInfo.extNo", "<spring:message code='log.RUserInfo.extNo'/>");
	lang.fn.set("log.RUserInfo.userEmail", "<spring:message code='log.RUserInfo.userEmail'/>");
	lang.fn.set("log.RUserInfo.userSex", "<spring:message code='log.RUserInfo.userSex'/>");
	lang.fn.set("log.SQLManage.SQLManage", "<spring:message code='log.SQLManage.SQLManage'/>");
	lang.fn.set("log.SQLManage.sqlName", "<spring:message code='log.SQLManage.sqlName'/>");
	lang.fn.set("log.SQLManage.sql", "<spring:message code='log.SQLManage.sql'/>");
	lang.fn.set("log.UserDBInterface.UserDBInterface", "<spring:message code='log.UserDBInterface.UserDBInterface'/>");
	lang.fn.set("log.UserDBInterface.seq", "<spring:message code='log.UserDBInterface.seq'/>");
	lang.fn.set("log.UserDBInterface.rDbIp", "<spring:message code='log.UserDBInterface.rDbIp'/>");
	lang.fn.set("log.UserDBInterface.rDbUser", "<spring:message code='log.UserDBInterface.rDbUser'/>");
	lang.fn.set("log.UserDBInterface.rDbPwd", "<spring:message code='log.UserDBInterface.rDbPwd'/>");
	lang.fn.set("log.UserDBInterface.rDbPort", "<spring:message code='log.UserDBInterface.rDbPort'/>");
	lang.fn.set("log.UserDBInterface.rDbName", "<spring:message code='log.UserDBInterface.rDbName'/>");
	lang.fn.set("log.UserDBInterface.rTeamCode", "<spring:message code='log.UserDBInterface.rTeamCode'/>");
	lang.fn.set("log.UserDBInterface.rTeamName", "<spring:message code='log.UserDBInterface.rTeamName'/>");
	lang.fn.set("log.UserDBInterface.rHh", "<spring:message code='log.UserDBInterface.rHh'/>");
	lang.fn.set("log.UserDBInterface.rFlag", "<spring:message code='log.UserDBInterface.rFlag'/>");
	lang.fn.set("log.UserDBInterface.rDate", "<spring:message code='log.UserDBInterface.rDate'/>");
	lang.fn.set("log.BestCallInfo.BestCallInfo", "<spring:message code='log.BestCallInfo.BestCallInfo'/>");
	lang.fn.set("log.BestCallInfo.rShareDate", "<spring:message code='log.BestCallInfo.rShareDate'/>");
	lang.fn.set("log.BestCallInfo.rShareTime", "<spring:message code='log.BestCallInfo.rShareTime'/>");
	lang.fn.set("log.BestCallInfo.rUserId", "<spring:message code='log.BestCallInfo.rUserId'/>");
	lang.fn.set("log.BestCallInfo.rShareTitle", "<spring:message code='log.BestCallInfo.rShareTitle'/>");
	lang.fn.set("log.BestCallInfo.rSharePath", "<spring:message code='log.BestCallInfo.rSharePath'/>");
	lang.fn.set("log.BestCallInfo.rShareDueDate", "<spring:message code='log.BestCallInfo.rShareDueDate'/>");
	lang.fn.set("log.BestCallInfo.rShareTarget", "<spring:message code='log.BestCallInfo.rShareTarget'/>");
	lang.fn.set("log.CustConfigInfo.CustConfigInfo", "<spring:message code='log.CustConfigInfo.CustConfigInfo'/>");
	lang.fn.set("log.CustConfigInfo.rCustCode", "<spring:message code='log.CustConfigInfo.rCustCode'/>");
	lang.fn.set("log.CustConfigInfo.rConfigKey", "<spring:message code='log.CustConfigInfo.rConfigKey'/>");
	lang.fn.set("log.CustConfigInfo.rConfigValue", "<spring:message code='log.CustConfigInfo.rConfigValue'/>");
	lang.fn.set("log.CustConfigInfo.desc", "<spring:message code='log.CustConfigInfo.desc'/>");
	lang.fn.set("log.EtcConfigInfo.EtcConfigInfo", "<spring:message code='log.EtcConfigInfo.EtcConfigInfo'/>");
	lang.fn.set("log.EtcConfigInfo.groupKey", "<spring:message code='log.EtcConfigInfo.groupKey'/>");
	lang.fn.set("log.EtcConfigInfo.mGroupKey", "<spring:message code='log.EtcConfigInfo.mGroupKey'/>");
	lang.fn.set("log.EtcConfigInfo.configKey", "<spring:message code='log.EtcConfigInfo.configKey'/>");
	lang.fn.set("log.EtcConfigInfo.mConfigKey", "<spring:message code='log.EtcConfigInfo.mConfigKey'/>");
	lang.fn.set("log.EtcConfigInfo.configValue", "<spring:message code='log.EtcConfigInfo.configValue'/>");
	lang.fn.set("log.Log.Log", "<spring:message code='log.Log.Log'/>");
	lang.fn.set("log.Log.rLogDate", "<spring:message code='log.Log.rLogDate'/>");
	lang.fn.set("log.Log.rLogTime", "<spring:message code='log.Log.rLogTime'/>");
	lang.fn.set("log.Log.rLogCode", "<spring:message code='log.Log.rLogCode'/>");
	lang.fn.set("log.Log.rLogName", "<spring:message code='log.Log.rLogName'/>");
	lang.fn.set("log.Log.rLogContents", "<spring:message code='log.Log.rLogContents'/>");
	lang.fn.set("log.Log.rLogDetailCode", "<spring:message code='log.Log.rLogDetailCode'/>");
	lang.fn.set("log.Log.rLogLevel", "<spring:message code='log.Log.rLogLevel'/>");
	lang.fn.set("log.Log.rLogIp", "<spring:message code='log.Log.rLogIp'/>");
	lang.fn.set("log.Log.rLogServerIp", "<spring:message code='log.Log.rLogServerIp'/>");
	lang.fn.set("log.Log.rLogUserId", "<spring:message code='log.Log.rLogUserId'/>");
	lang.fn.set("log.Log.rLogEtc", "<spring:message code='log.Log.rLogEtc'/>");
	lang.fn.set("call.type.I",		 	"<spring:message code='call.type.I'/>");
	lang.fn.set("call.type.O",		 	"<spring:message code='call.type.O'/>");
	lang.fn.set("call.type.TR",		 	"<spring:message code='call.type.TR'/>");
	lang.fn.set("call.type.TS",		 	"<spring:message code='call.type.TS'/>");
	lang.fn.set("call.type.Z",		 	"<spring:message code='call.type.Z'/>");
	lang.fn.set("log.Log.sDate", "<spring:message code='log.Log.sDate'/>");
	lang.fn.set("log.Log.eDate", "<spring:message code='log.Log.eDate'/>");
	lang.fn.set("log.Log.sTime", "<spring:message code='log.Log.sTime'/>");
	lang.fn.set("log.Log.eTime", "<spring:message code='log.Log.eTime'/>");
	lang.fn.set("log.LoginVO.LoginVO", "<spring:message code='log.LoginVO.LoginVO'/>");
	lang.fn.set("log.LoginVO.failReason", "<spring:message code='log.LoginVO.failReason'/>");
	lang.fn.set("log.LoginVO.userId", "<spring:message code='log.LoginVO.userId'/>");
	lang.fn.set("log.LoginVO.clientIp", "<spring:message code='log.LoginVO.clientIp'/>");
	lang.fn.set("log.LoginVO.userLevel", "<spring:message code='log.LoginVO.userLevel'/>");
	lang.fn.set("log.LoginVO.pwEditDate", "<spring:message code='log.LoginVO.pwEditDate'/>");
	lang.fn.set("log.LoginVO.tryCount", "<spring:message code='log.LoginVO.tryCount'/>");
	lang.fn.set("log.LoginVO.lastLoginDate", "<spring:message code='log.LoginVO.lastLoginDate'/>");
	lang.fn.set("log.LoginVO.lockYn", "<spring:message code='log.LoginVO.lockYn'/>");
	lang.fn.set("log.MAccessLevelInfo.MAccessLevelInfo", "<spring:message code='log.MAccessLevelInfo.MAccessLevelInfo'/>");
	lang.fn.set("log.MAccessLevelInfo.levelCode", "<spring:message code='log.MAccessLevelInfo.levelCode'/>");
	lang.fn.set("log.MAccessLevelInfo.levelName", "<spring:message code='log.MAccessLevelInfo.levelName'/>");
	lang.fn.set("log.MMenuAccessInfo.MMenuAccessInfo", "<spring:message code='log.MMenuAccessInfo.MMenuAccessInfo'/>");
	lang.fn.set("log.MMenuAccessInfo.programCode", "<spring:message code='log.MMenuAccessInfo.programCode'/>");
	lang.fn.set("log.MMenuAccessInfo.levelCode", "<spring:message code='log.MMenuAccessInfo.levelCode'/>");
	lang.fn.set("log.MMenuAccessInfo.accessLevel", "<spring:message code='log.MMenuAccessInfo.accessLevel'/>");
	lang.fn.set("log.MMenuAccessInfo.readYn", "<spring:message code='log.MMenuAccessInfo.readYn'/>");
	lang.fn.set("log.MMenuAccessInfo.modiYn", "<spring:message code='log.MMenuAccessInfo.modiYn'/>");
	lang.fn.set("log.MMenuAccessInfo.delYn", "<spring:message code='log.MMenuAccessInfo.delYn'/>");
	lang.fn.set("log.MMenuAccessInfo.listenYn", "<spring:message code='log.MMenuAccessInfo.listenYn'/>");
	lang.fn.set("log.MMenuAccessInfo.downloadYn", "<spring:message code='log.MMenuAccessInfo.downloadYn'/>");
	lang.fn.set("log.MMenuAccessInfo.excelYn", "<spring:message code='log.MMenuAccessInfo.excelYn'/>");
	lang.fn.set("log.MMenuAccessInfo.maskingYn", "<spring:message code='log.MMenuAccessInfo.maskingYn'/>");
	lang.fn.set("log.MMenuAccessInfo.prereciptYn", "<spring:message code='log.MMenuAccessInfo.prereciptYn'/>");
	lang.fn.set("log.MMenuAccessInfo.reciptYn", "<spring:message code='log.MMenuAccessInfo.reciptYn'/>");
	lang.fn.set("log.MMenuAccessInfo.approveYn", "<spring:message code='log.MMenuAccessInfo.approveYn'/>");
	lang.fn.set("log.MMenuAccessInfo.encYn", "<spring:message code='log.MMenuAccessInfo.encYn'/>");
	lang.fn.set("log.MMenuAccessInfo.downloadApprove", "<spring:message code='log.MMenuAccessInfo.downloadApprove'/>");
	lang.fn.set("log.MMenuAccessInfo.feedbackModifyYn", "<spring:message code='log.MMenuAccessInfo.feedbackModifyYn'/>");
	lang.fn.set("log.MMenuAccessInfo.programTop", "<spring:message code='log.MMenuAccessInfo.programTop'/>");
	lang.fn.set("log.MMenuAccessInfo.programPath", "<spring:message code='log.MMenuAccessInfo.programPath'/>");
	lang.fn.set("log.MMenuAccessInfo.displayLevel", "<spring:message code='log.MMenuAccessInfo.displayLevel'/>");
	lang.fn.set("log.MMenuAccessInfo.topPriority", "<spring:message code='log.MMenuAccessInfo.topPriority'/>");
	lang.fn.set("log.PhoneMappingInfo.PhoneMappingInfo", "<spring:message code='log.PhoneMappingInfo.PhoneMappingInfo'/>");
	lang.fn.set("log.PhoneMappingInfo.custPhone", "<spring:message code='log.PhoneMappingInfo.custPhone'/>");
	lang.fn.set("log.PhoneMappingInfo.custNickName", "<spring:message code='log.PhoneMappingInfo.custNickName'/>");
	lang.fn.set("log.PhoneMappingInfo.useNickName", "<spring:message code='log.PhoneMappingInfo.useNickName'/>");
	lang.fn.set("log.PhoneMappingInfo.custPhoneKey", "<spring:message code='log.PhoneMappingInfo.custPhoneKey'/>");
	lang.fn.set("log.PhoneMappingInfo.procType", "<spring:message code='log.PhoneMappingInfo.procType'/>");
	lang.fn.set("log.PhoneMappingInfo.procPosition", "<spring:message code='log.PhoneMappingInfo.procPosition'/>");
	lang.fn.set("log.SubNumberInfo.SubNumberInfo", "<spring:message code='log.SubNumberInfo.SubNumberInfo'/>");
	lang.fn.set("log.SubNumberInfo.telNo", "<spring:message code='log.SubNumberInfo.telNo'/>");
	lang.fn.set("log.SubNumberInfo.nickName", "<spring:message code='log.SubNumberInfo.nickName'/>");
	lang.fn.set("log.SubNumberInfo.use", "<spring:message code='log.SubNumberInfo.use'/>");
	lang.fn.set("log.SubNumberInfo.idx", "<spring:message code='log.SubNumberInfo.idx'/>");
	lang.fn.set("log.SystemInfo.SystemInfo", "<spring:message code='log.SystemInfo.SystemInfo'/>");
	lang.fn.set("log.SystemInfo.sysId", "<spring:message code='log.SystemInfo.sysId'/>");
	lang.fn.set("log.SystemInfo.sysName", "<spring:message code='log.SystemInfo.sysName'/>");
	lang.fn.set("log.SystemInfo.sysIp", "<spring:message code='log.SystemInfo.sysIp'/>");
	lang.fn.set("log.SystemInfo.pbxId", "<spring:message code='log.SystemInfo.pbxId'/>");
	lang.fn.set("log.SystemInfo.storagePath", "<spring:message code='log.SystemInfo.storagePath'/>");
	lang.fn.set("log.PublicIpInfo.PublicIpInfo", "<spring:message code='log.PublicIpInfo.PublicIpInfo'/>");
	lang.fn.set("log.PublicIpInfo.rPublicIp", "<spring:message code='log.PublicIpInfo.rPublicIp'/>");
	lang.fn.set("log.PublicIpInfo.rPublicIpOld", "<spring:message code='log.PublicIpInfo.rPublicIpOld'/>");
	
	lang.fn.set("log.etc.noAuthy", "<spring:message code='log.etc.noAuthy'/>");
	
	lang.fn.set("log.etc.noEntryAuth", "<spring:message code='log.etc.noEntryAuth'/>");

	lang.fn.set("views.search.column.value.volume", "<spring:message code='views.search.column.value.volume'/>");
	lang.fn.set("views.search.column.value.volumeTwo", "<spring:message code='views.search.column.value.volumeTwo'/>");
	lang.fn.set("views.search.column.value.volumeThree", "<spring:message code='views.search.column.value.volumeThree'/>");
	
	
	lang.fn.set("admin.systemOption.label.settingAdd", "<spring:message code='admin.systemOption.label.settingAdd'/>");
	lang.fn.set("admin.systemOption.label.settingModi", "<spring:message code='admin.systemOption.label.settingModi'/>");
	
	lang.fn.set("admin.menu.li.userManage.mobileManage", "<spring:message code='admin.menu.li.userManage.mobileManage'/>");
	lang.fn.set("admin.fileRecover.state.N", "<spring:message code='admin.fileRecover.state.N'/>");
	lang.fn.set("admin.channel.alert.serverWorking", "<spring:message code='admin.channel.alert.serverWorking'/>"); <%-- 작업이 진행중입니다. 잠시 후 다시 시도해주세요. -->
	
	
	<%-- lang.fn.set("views.asterisk.grid.head.calldate",      		"<spring:message code='views.asterisk.grid.head.calldate'/>");1차 평가자
	lang.fn.set("views.asterisk.grid.head.clid",      			"<spring:message code='views.asterisk.grid.head.clid'/>");1차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.src",      			"<spring:message code='views.asterisk.grid.head.src'/>");2차 평가자
	lang.fn.set("views.asterisk.grid.head.dst",      			"<spring:message code='views.asterisk.grid.head.dst'/>");2차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.dcontext",     		"<spring:message code='views.asterisk.grid.head.dcontext'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.channel",      		"<spring:message code='views.asterisk.grid.head.channel'/>");1차 평가자
	lang.fn.set("views.asterisk.grid.head.dstchannel",      	"<spring:message code='views.asterisk.grid.head.dstchannel'/>");1차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.lastapp",      		"<spring:message code='views.asterisk.grid.head.lastapp'/>");2차 평가자
	lang.fn.set("views.asterisk.grid.head.duration",      		"<spring:message code='views.asterisk.grid.head.duration'/>");2차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.billsec",     		"<spring:message code='views.asterisk.grid.head.billsec'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.disposition",      	"<spring:message code='views.asterisk.grid.head.disposition'/>");1차 평가자
	lang.fn.set("views.asterisk.grid.head.amaflags",      		"<spring:message code='views.asterisk.grid.head.amaflags'/>");1차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.accountcode",      	"<spring:message code='views.asterisk.grid.head.accountcode'/>");2차 평가자
	lang.fn.set("views.asterisk.grid.head.uniqueid",      		"<spring:message code='views.asterisk.grid.head.uniqueid'/>");2차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.userfield",     		"<spring:message code='views.asterisk.grid.head.userfield'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.did",      			"<spring:message code='views.asterisk.grid.head.did'/>");1차 평가자
	lang.fn.set("views.asterisk.grid.head.recordingfile",     	"<spring:message code='views.asterisk.grid.head.recordingfile'/>");1차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.cnum",      			"<spring:message code='views.asterisk.grid.head.cnum'/>");2차 평가자
	lang.fn.set("views.asterisk.grid.head.cnam",     			"<spring:message code='views.asterisk.grid.head.cnam'/>");2차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.outbound_cnum",     	"<spring:message code='views.asterisk.grid.head.outbound_cnum'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.outbound_cnam",   	"<spring:message code='views.asterisk.grid.head.outbound_cnam'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.dst_cnam",      		"<spring:message code='views.asterisk.grid.head.dst_cnam'/>");1차 평가자
	lang.fn.set("views.asterisk.grid.head.linkedid",     		"<spring:message code='views.asterisk.grid.head.linkedid'/>");1차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.peeraccount",      	"<spring:message code='views.asterisk.grid.head.peeraccount'/>");2차 평가자
	lang.fn.set("views.asterisk.grid.head.sequence",     		"<spring:message code='views.asterisk.grid.head.sequence'/>");2차 평가자 의견
	lang.fn.set("views.asterisk.grid.head.R_COUNSEL_RESULT_BGCODE",     	"<spring:message code='views.asterisk.grid.head.R_COUNSEL_RESULT_BGCODE'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.R_COUNSEL_RESULT_MGCODE",     	"<spring:message code='views.asterisk.grid.head.R_COUNSEL_RESULT_MGCODE'/>");상담원 의견
	lang.fn.set("views.asterisk.grid.head.R_COUNSEL_RESULT_SGCODE",     	"<spring:message code='views.asterisk.grid.head.R_COUNSEL_RESULT_SGCODE'/>");상담원 의견 --%>
	$.each(lang.fn, function(name, fn) {
		lang[name] = fn;
	});

	return lang;
}())
</script>