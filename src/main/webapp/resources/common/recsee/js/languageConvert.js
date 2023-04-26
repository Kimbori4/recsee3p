
// 언어팩 변경 관련
// convertMsg : 변환할 MSG 코드
// siteId : 해당 siteId에 해당하는 언어팩 불러온다. null로 넣을시 로그인한 아이디의 siteId를 불러온다, site아이디가 없는 페이지(로그인 이전의 페이지)에서는 아무값도 않넣으면 conple로 처리된다.(common 컨트롤러 참고)
// locale : 변환할 MSG 코드 지역 , 일반적인 경우에 컨트롤단에 담고 있기 때문에 따로 넣어주지 않아 도 된다.
// 로그인시 선택된 언어 불러 올때 ex : convertLanguage("cpcrm.agent.title.counselservice") ;; siteId. 은 컨트롤러에서 붙여줌
// 강제로 로케일 설정해서 언어 불러올 때 ex : convertLanguage("cpcrm.agent.title.counselservice","ko") ;; siteId. 은 컨트롤러에서 붙여줌

function convertLanguage(convertMsgCode, pramSiteId, pramLocale){
	var convertedMsg = null;
	var convertLocale = null;
	var converSiteId = null;
		
	if(pramLocale != null && pramLocale != "" && pramLocale != undefined)
		convertLocale = pramLocale
	if(pramSiteId != null && pramSiteId != "" && pramSiteId != undefined)
		converSiteId = pramSiteId

	var dataStr = {
			"convertMsgCode": convertMsgCode
			
	};
	$.ajax({
		url:contextPath+"/msgConvert.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,  /* 비동기식으로 처리시 메시지 변환이 정상적으로 안되어서 null값으로 처리되는 현상이 있음  false로 처리 */
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y"){
				convertedMsg = jRes.resData.convertedMsg	
			}else if(jRes.success == "N" && jRes.resData.msg == "LoginFail"){
				alert('session has expired')
				location.href=contextPath+"/login/init.do";
			}else if(jRes.success == "N" || jRes.result == ""){
				convertedMsg = "메세지 변환 에러 : 반환할 메세지 소스가 존재 하지 않습니다.\n언어팩을 확인해 주세요!!"
			}
				
		}
	});

	return convertedMsg;
}