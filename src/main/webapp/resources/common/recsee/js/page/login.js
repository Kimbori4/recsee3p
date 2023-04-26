$(document).ready(function() {

	// 쿠키에 시스템 코드, 계정 ID 값 확인
	var cookieUnqLang = getLogin("unqLang");
	var cookieUserId = getLogin("userId");
	// 쿠키에 값이 있으면 체크 버튼과 입력 창에 값 넣기 (언어)
	if(cookieUnqLang != "") {
		$("#unqLang").val(cookieUnqLang);
	} else {
		$("#unqLang").val("ko").change();
	}

	// 쿠키에 값이 있으면 체크 버튼과 입력 창에 값 넣기 (계정 ID)
	if(cookieUserId != "") {
		$("#userIdCheck").attr("checked", true);
		$("#userId").val(cookieUserId);
	}
	
	if(RecSeeVersion != "") {
		$(".login_footer").find("p").text("Version : RecSee_"+RecSeeVersion);
	}

	// 로그인 버튼 클릭 시 입력 데이터 확인
	// TODO : dhx alert 제거
	$("#confirmBtn").on("click", function() {
		// @saint: 텍스트 일단 초기화
		$(".alert_txt").text("");
		if ($("#userId").val() == "") {
			$(".alert_txt").text(lang.login.login.alert.not.input.id).show();
		} else if ($("#userPw").val() == "") {
			$(".alert_txt").text(lang.login.login.alert.not.input.pw).show();
		} else if($("#unqLang option:selected").val() == undefined ) {
			$(".alert_txt").text(lang.login.login.alert.not.input.lang).show();
		} else {
			// 입력 받은 데이터로 DB 조회
			login_chk();
		}

	});

	$("#unqLang").change(function(){
		$.ajax({
			url: contextPath+"/login/changeMessage.do",
			data: "changeLang="+$(this).val(),
			type: "POST",
			dataType: "json",
			success: function(jRes) {
				for( var msg in jRes.resData ) {
					//$("#"+msg).val(eval("jRes.resData."+msg));

					$("#userId").attr("placeholder", jRes.resData.loginloginplaceholderid);
					$("#userPw").attr("placeholder", jRes.resData.loginloginplaceholderpassword);
					$("#lblUserIdCheck").html(jRes.resData.loginlogintitlerememberMe);
				}
			}
		})
	});

	if($("#userId").val().length > 0) $("#userPw").get(0).focus();
	else $("#userId").get(0).focus();
	
	//킵얼라이브
	var inter = setInterval(function(){
		$.ajax({
			url: contextPath+"/keep_alive.do",
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){				
			}
		});
	},10000);
	
	
	$.ajax({
		url:contextPath+"/logoSetting.do",
		data:{
			"logoType":"login"
		},
		type:"POST",
		dataType:"json",
		success:function(jRes){			
			var logoChangeUse = jRes.resData.logoChangeUse;
			if (logoChangeUse != null && logoChangeUse == "Y") {
//				$(".logo_main").css("background-image", "url('"+siteResourcePath + "/images/project/main/logo/login_logo.png')");
			} else {
//				$(".logo_main").css("background-image", "url('"+siteResourcePath + "/images/project/main/logo/recsee_bi_white.png')");
			}
		}
	});
});

function login_chk() {

	var rsa = new RSAKey();
	rsa.setPublic($("#RSAModulus").val(), $("#RSAExponent").val());	// 암호화 복호화

	var userId = rsa.encrypt($("#userId").val());	// 아이디 암호화
	var pwd = rsa.encrypt($("#userPw").val());		// 비밀번호 암호화

	// 계정 ID, PW, 시스템 코드, 내선번호
	var dataStr = {"userId":userId,"userPw":pwd,"unqLang":$("#unqLang").val()};
	// DB 조회 ajax 처리
	$.ajax({
		url:contextPath+"/login/loginChk.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				if(jRes.result == "preSessionId"){
					alert(lang.common.alert.disconnected.ID /* "ID 중복 접속으로 기존에 접속된 세션이 해제 되었습니다." */);
				}else if(jRes.result == "preSessionIp"){
					alert(lang.common.alert.disconnected.IP /* "IP 중복 접속으로 기존에 접속된 세션이 해제 되었습니다." */);
				}
				// 계정 ID 저장 체크 했으면
				if($("#userIdCheck").is(":checked") == true) {
					// 쿠키 저장
					setSave("userId", $("#userId").val(), 7);
				} else {
					// 쿠키 삭제
					setSave("userId", $("#userId").val(), -1);
				}
				// DB에서 가져온 메인 페이지 경로
				var mainPath = jRes.resData.mainPath;
				// 메인 페이지 이동
				//location.href = contextPath+"/main?path="+mainPath;
				$("#main input").val(contextPath+mainPath)
				document.main.submit();

			} else if(jRes.success =="N" && jRes.result == "no match ip"){
				alert(lang.common.alert.Unauthorized.IP /* "허가되지 않은 ip입니다. 관리자에게 문의해 주세요." */);
			} else if(jRes.success =="N" && jRes.result == "0" && jRes.resData.msg == "cryte module load fail"){
				alert("세션이 만료되었습니다. \n페이지 새로고침 후 다시 로그인해주세요.");
			} else if(jRes.success =="N" && jRes.result == "0"){
				alert(lang.common.alert.notMatchPasswords /* "비밀번호가 일치하지 않습니다." */);
			} else if(jRes.success =="N" && jRes.result == "PWCHANGE_Y"){
				alert(lang.common.alert.expired.password /* "비밀번호 사용 기한이 만료되어 비밀번호 변경이 필요합니다.\n확인을 누르시면 비밀번호 변경 페이지로 이동 합니다." */)
				location.href = contextPath+"/pwChange";
			} else if(jRes.success =="N" && jRes.result == "PWCHANGE_F"){
				alert(lang.common.alert.expired.password /* "비밀번호 사용 기한이 만료되어 비밀번호 변경이 필요합니다.\n확인을 누르시면 비밀번호 변경 페이지로 이동 합니다." */)
				location.href = contextPath+"/pwChange";
			} else if(jRes.success =="N" && jRes.result == "PWCHANGE"){
				alert(lang.common.alert.required.changePwd /* "비밀번호 변경이 필요합니다.\n확인을 누르시면 비밀번호 변경 페이지로 이동 합니다." */)
				location.href = contextPath+"/pwChange";
			} else if(jRes.success =="N" && jRes.result == "ID is Locked"){
				alert(lang.common.alert.locked.account /* "오랫동안 미접속하거나 지속적인 로그인 실패로 계정이 잠긴 상태입니다.\n관리자에게 문의하여, 비밀번호를 수정 후 재 로그인 시도를 해 주세요." */)
			}  else if(jRes.success =="N" && jRes.result == "noAuthy"){	
				alert(lang.common.alert.noPages.accessible /* "접근 가능한 페이지가 없습니다. 권한을 확인해주세요." */)
			}  else if(jRes.success =="N" && jRes.result == "dupId"){	
				alert(lang.common.alert.failedLogin.ID /* "ID 중복 접속으로 로그인에 실패 하였습니다." */)
			}  else if(jRes.success =="N" && jRes.result == "dupIp"){	
				alert(lang.common.alert.failedLogin.IP /* "IP 중복 접속으로 로그인에 실패 하였습니다." */ )
			}  else if(jRes.success =="N" && jRes.result == "session over"){	
				alert(lang.common.alert.expired.session /* "세션이 만료되었습니다. 다시 로그인 해 주세요." */)
				location.href = contextPath+"/login/init";
			}  else if(jRes.success =="N" && jRes.result == "no entry auth"){	
				alert(lang.log.etc.noEntryAuth /* "로그인이 불가능한 권한 입니다." */)
				location.href = contextPath+"/login/init";
			} else {
				// 입력한 데이터와 일치하는 계정 정보가 없으면
				/*alert("아이디 또는 패스워드를 확인해주세요.")*/
				alert(lang.login.login.alert.fail.login); /* 로그인에 실패했습니다. */
				// 입력된 계정 PW 제거
				$("#userPw").val("");
			}
		}
	});
}

// 쿠키에 저장할 이름, 값, 기간 설정
function setSave(name, value, expiredays) {
	var today = new Date();
	today.setDate( today.getDate() + expiredays );
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + today.toGMTString() + ";"
}

// 로그인 창 접속 시 쿠키에 데이터 있는 지 확인
function getLogin(cookie_name) {
	// userid 쿠키에서 id 값을 가져온다.
	var cook = document.cookie + ";";
	var idx = cook.indexOf(cookie_name, 0);
	var val = "";

	if(idx != -1) {
		cook = cook.substring(idx, cook.length);
		begin = cook.indexOf("=", 0) + 1;
		end = cook.indexOf(";", begin);
		val = unescape(cook.substring(begin, end));
	}
	return val;
}