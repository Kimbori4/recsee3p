// form 관련 기능 정의
function formFunction(){

	if(type == 'F'){
		$("#afterBtn").hide();
	}else{
		$("#afterBtn").show();
	}
	
	// 다음에 변경 하기 클릭
	$("#afterBtn").click(function(){

		if(confirm(lang.common.alert.changePwdNext /* "비밀번호를 다음에 변경 하시겠습니까?" */)){
			
			
			if(!userId){
				alert(lang.common.alert.noUserID /* "사용자 아이디 정보가 없습니다.\n올바른 경로로 이용해 주세요!\n확인을 누르시면, 로그인 페이지로 이동합니다." */ );
				location.href =contextPath+"/login/init"
				return;
			}

			var rsa = new RSAKey();
			rsa.setPublic(RSAModulus, RSAExponent);
			
			$.ajax({
				url: contextPath+"/changePw.do",
				data: {"userId" : rsa.encrypt(userId)},
				type: "POST",
				dataType: "json",
				success: function(jRes) {
					if(jRes.success == "Y" ){
						alert(lang.common.alert.extension.succeeded /* "비밀번호 변경주기 연장에 성공 하였습니다.\n확인을 누르시면 로그인페이지로 이동합니다.\n재 로그인 해주세요!" */)
						location.href =contextPath+"/login/init";
					}else{
						lang.common.alert.extend.Failed /* "비밀번호 변경 주기 연장에 실패 하였습니다." */;
					}
				}
			})
		}else{
			return;
		}
	});
	
	// 확인 버튼 클릭 이벤트
	$("#okBtn").click(function(){
		var rsa = new RSAKey();
		rsa.setPublic(RSAModulus, RSAExponent);
		
		var nowPw = $("#nowPw").val();
		var newPw = $("#newPw").val();
		var newPwChk = $("#newPwChk").val();
		
		if(!userId){
			alert(lang.common.alert.noUserID /* "사용자 아이디 정보가 없습니다.\n올바른 경로로 이용해 주세요!\n확인을 누르시면, 로그인 페이지로 이동합니다." */ );
			location.href =contextPath+"/login/init"
			return;
		}

		if(!nowPw){
			alert(lang.common.alert.current.password /* "현재 비밀번호를 입력 해 주세요!" */);
			$("#nowPw").focus();
			return
		}

		if(!newPw){
			alert(lang.common.alert.newPassword /* "새로운 비밀번호를 입력 해 주세요!" */ )
			$("#newPw").focus();
			return
		}
		
		if(!newPwChk){
			alert(lang.admin.alert.recUser33 /* "비밀번호 확인을 입력 해 주세요!" */)
			$("#newPwChk").focus();
			return
		}
		
		if(newPw != newPwChk){
			alert(lang.common.alert.notMatch.password /* "새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다!" */)
			$("#newPwChk").focus();
			return
		}
		
		dataStr = {
				"userId" : rsa.encrypt(userId)
			,	"userPw" : rsa.encrypt(newPw)
			,	"nowPw"  : rsa.encrypt(nowPw)
		}
		
		$.ajax({
			url: contextPath+"/changePw.do",
			data: dataStr,
			type: "POST",
			dataType: "json",
			success: function(jRes) {
				if(jRes.success == "Y" ){
					alert(lang.common.alert.change.succeeded /* "비밀번호 변경에 성공 하였습니다.\n확인을 누르시면 로그인페이지로 이동합니다.\n변경된 비밀번호로 로그인 해주세요!" */)
					location.href =contextPath+"/login/init"
				}
				else {
					if(jRes.result=="NOMATCH") {
						alert(lang.common.alert.wrong.password /* "현재 비밀번호가 일치 하지 않습니다.\n다시 입력 해 주세요!" */);
						$("#nowPw").val("");
						$("#nowPw").focus();
					} else if(jRes.result=="PW Count Over") {
						alert(lang.common.alert.passwords.inThePast /* "과거 비밀번호 재사용 허용 횟수가 넘었습니다.\n다른 비밀번호를 입력하여 주세요." */);
						$("#newPw").val("");
						$("#newPwChk").val("");
						$("#newPw").focus();
					} else if(jRes.result=="PW Dont Use") {
						alert(lang.common.alert.previous.password /* "이전에 사용하였던 비밀번호는 사용할 수 없습니다.\n다른 비밀번호를 입력하여 주세요." */);
						$("#newPw").val("");
						$("#newPwChk").val("");
						$("#newPw").focus();
					} else if(jRes.result=="PASSWORD PATTERN IS MISS MATCH") {
						alert(jRes.resData.msg);
						$("#newPw").val("");
						$("#newPwChk").val("");
						$("#newPw").focus();
					}  else{ 
						alert(lang.common.alert.change.failed /* "비밀번호 변경을 실패 하였습니다." */);
					}
				}
			}
		})
	});
}

// 로드 함수
function changePwLoad(){
	formFunction();
}