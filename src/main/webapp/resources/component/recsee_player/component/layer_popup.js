/**
@Param name : selector
@Param resizeable : 리사이즈 가능여부 true(사용시에만 줘도 댐..) 
@Param resizeableOpt : 리사이즈 옵션 ex {	minWidth:875, minHeight:340 } ;; 더 필요한건 jquery resizeable 찾아보도록..
*/
function layer_popup(name, resizeable, resizeableOpt){

    // 레이어 오픈 링크 설정
    var popupObj = $(name).length?$(name):$('.popup_obj');  // name이 없으면 가지고 있는 팝업을 다 띄운다.

    // 페이드 인 속도 설정
    $(popupObj).fadeIn(300);

    // 팝업시 레이어 위치 설정 : 화면 중앙에 띄울 경우
    // @ezra: height가 길어질 경우 해상도가 넘어가면 잘리므로 top은 고정값 위치에서 출력되도록 변경
    //var popMargTop = ($(popupObj).height() + 24) / 2;
    var popMargLeft = ($(popupObj).width() + 24) / 2;

    $(popupObj).css({
        //'margin-top' : -popMargTop,
        'margin-left' : -popMargLeft
    });

    // 마스크 설정 : 팝업이 오픈 되고 배경을 클릭하지 못하도록 투명 마스크를 씌운다.
    // 첫 팝업이면 마스크 띄우기
    if(!$("#mask").length) {
        $('body').append('<div id="mask"></div>');
        $('#mask').fadeIn(300);
        popupObj.draggable({
        	handle: ".popup_header",
        	containment : "window"
		});
        popupObj.find("input:first").focus();
    }

	// 리사이즈 옵션 처리
    if(resizeable){
    	$( "#screenPopup" ).resizable(resizeableOpt);
    }
    return false;
};

// 팝업창 닫기
function layer_popup_close(name){
    // 기본적인 셀렉터
    var targetSelector = '.popup_obj';
    // 아이디가 있으면 추가선택
    if((name||"") != "") {
        targetSelector += name;
    }
    
    // 닫을 때 내용 초기화
    $(targetSelector).find('input').val("");
    $(targetSelector).find('select').val("");
    
    $(targetSelector).fadeOut(300 , function() {
        // 활성화된 팝업 갯수가 1개 미만이면 마스크 지우기
        if($(".popup_obj:visible").length < 1) {
            $('#mask').remove();
        }
    });
}

// 이벤트 처리
$(function() {
    // 닫기 이벤트 처리
   $(document).on("click", ".popup_close", function(e){
        layer_popup_close($(this).parents(".popup_obj").selector);
    });
});

// 팝업 여는 함수 jquery 함수로 만들기
$.fn.layerPopup = function() {
	layer_popup($(this).selector);
	return this;
}
// 팝업 닫는 함수 jquery 함수로 만들기
$.fn.layerPopupClose = function() {
	layer_popup_close($(this).selector);
	return this;
}