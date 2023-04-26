// onLoad 여러개 하기 위한 함수
function addLoadEvent(func) {
    var oldonload = window.onload;

    if(typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function() {
            oldonload();
            func();
        }
    }
}

// 새창열기 막음(크롬에선 안됨)
$(window).keydown(function(event){
    if (event.ctrlKey == true && event.keyCode == 78) {
    	return false;
    }
});

$(function() {
    // select 태그에 아래 클래스를 삽입하여 검색바 생성
    $(".select_search_single").select();

    // not allowed일 경우 선택 막을 클래스
    var formNotAllowed = $('.ui_input_hasinfo, ui_input_hasinfo_white')

    // input 선택 막기
    $(formNotAllowed).bind("focus", function() {$(this).blur();});
    // select 선택 막기
    $(formNotAllowed).attr("disabled", true);
});


// tabbar lib event
$(function() {
    $(".ui_tabbar").each(function() {
        var $parent = $(this);

        // 탭바 번호 달기
        $parent.find("li.tabbar_menu").each(function(i) {
            $(this).attr("data-target", i+1);
        });
        $parent.find(".tabbar_cont").each(function(i) {
            $(this).attr("data-target", i+1);
        });
    })

    $(".ui_tabbar").find(".tabbar_menu").click(function() {
        // 선택된 탭 재선택할 경우 중단
        if($(this).hasClass(".ui_tabbar_menu_active"))  return;

        var $parent = $(this).parents(".ui_tabbar");
        var $tabno = $(this).attr("data-target");

        // 기존 탭 비활성화
        $parent.find(".ui_tabbar_menu_active").removeClass("ui_tabbar_menu_active");
        $parent.find(".tabbar_cont").hide();

        // 클릭한 탭 활성화
        $(this).addClass("ui_tabbar_menu_active");
        $parent.find(".tabbar_cont[data-target="+$tabno+"]").show();
    });


    $(".ui_tabbar").each(function() {
        var $parent = $(this);

        // 첫번째 탭 기본 활성화
        $parent.find("li.tabbar_menu:eq(0)").click();
    });
});


/*
////////////////////////////////////////////////////////////////////

//tabbar lib html sample

<!-- tabbar lib start -->
<div class="ui_tabbar">
    <div class="ui_tabbar_header">
        <ul>
            <!-- tabbar menu -->
            <li class="tabbar_menu"><!-- add menu tit --></li>
            <li class="tabbar_menu"><!-- add menu tit --></li>
        </ul>
    </div>

    <!-- tabbar body -->
    <div class="ui_tabbar_section">
        <!-- tabbar content -->
        <div class="tabbar_cont">
            <!-- add content -->
        </div>

        <div class="tabbar_cont">
            <!-- add content -->
        </div>
    </div>
</div>
<!-- tabbar lib end -->

////////////////////////////////////////////////////////////////////
*/





//Custom input[type="file"]
$(function() {
 var fileTarget = $('.filebox .upload_hidden');

 // 값 변경 될 때 이벤트
 fileTarget.on('change', function(){
     if(window.FileReader){ // HTML5 브라우저 일 경우
         var filename = $(this)[0].files[0].name;
     } else { // IE9 미만일 경우
         var filename = $(this).val().split('/').pop().split('\\').pop();
     }
     
     if($(this)[0].files.length>1){
    	 $(this).siblings('.upload_name').val($(this)[0].files.length + " files");
     }else{
    	 // 추출한 파일명 삽입
    	 $(this).siblings('.upload_name').val(filename);
     }
 });
});

/*
////////////////////////////////////////////////////////////////////

//Custom input[type="file"] sample

<div class="filebox file_upload_wrap">
	<input class="upload_name" value="파일선택" disabled="disabled">
	<label for="upLoadFile">업로드</label>
  	<input type="file" id="upLoadFile" class="upload_hidden">
</div>

////////////////////////////////////////////////////////////////////
*/


function seconds2time (seconds, type) {
	var hours = 0;
    var time = "", chkTime = true;

	if ( type === undefined ) type = true;
	if ( type ) hours   = Math.floor(seconds / 3600);

	seconds = Math.floor( seconds, 0 );
	var minutes = Math.floor((seconds - (hours * 3600)) / 60);
    var seconds = seconds - (hours * 3600) - (minutes * 60);

	if ( type )
	{
		if (hours != 0) time = (hours < 10) ? "0"+hours+":" : hours+":";
		else time = "00:";

		if ( time !== "" ) chkTime = true;
		else chkTime = false;
	}

    if (minutes != 0 || chkTime )
	{
		minutes = (minutes < 10 && chkTime) ? "0"+minutes : String(minutes);
		time += minutes+":";
    } else {
		time += "00:";
	}

    if (time === "") time = seconds;
    else time += (seconds < 10) ? "0"+seconds : String(seconds);

    return time;
}

function time2seconds(hms) {
	var a = hms.split(':');

	var seconds = (+a[0]) * 60 * 60 + (+a[1]) * 60 + (+a[2]);

	return seconds;
}

Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|hh|mm|ss)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            default: return $1;
        }
    });
};
String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

Number.prototype.format = function(){
    if(this==0) return 0;

    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');

    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

    return n;
};

String.prototype.format = function(){
    var num = parseFloat(this);
    if( isNaN(num) ) return "0";

    return num.format();
};
/*String.prototype.isSpecialCharCheck = function(isSpace, exp) {
	var flag = true;
	var specialChars = "~`!@#$%^&*-=+|[](){};:\"'<.,>/?_";
	var specialCharsReg = "";

	if(isSpace == null) false;

	if(exp != null) {
		var expLength = exp.length;
		var tmpReg = "";

		for(var i=0; i<expLength; i++) {
			(function(){eval("tmpReg = /"+exp[i]+"/g");}())
			//eval("tmpReg = /"+exp[i]+"/g");
			specialChars = specialChars.replace(exp[i], "");
		}
	}
	var tmpSpecialChars = specialChars;
	specialChars = "";
	for(var i=0; i<tmpSpecialChars.length; i++) {
		specialChars += "\\" + tmpSpecialChars[i];
	}
	//eval("speicalCharsReg = /["+specialChars+"]/gi;");
	(function(){eval("speicalCharsReg = /["+specialChars+"]/gi;");}())
	if(speicalCharsReg.test(this)) flag = false;
	if(isSpace == true) {
		if((/s$/g).test(this)) flag = false;
	}

	return flag;
}*/
/*String.prototype.isEngCheck = function(minLen,maxLen) {
	var flag = true;
	if(minLen == null) minLen = 5;
	if(maxLen == null) maxLen = 15;
//	var engReg = eval("/^[a-zA-Z]+[a-zA-Z]{"+minLen+","+maxLen+"}$/g;");
	var engReg;
	(function(){eval("engReg=/^[a-zA-Z]+[a-zA-Z]{"+minLen+","+maxLen+"}$/g;");}())
	if(!engReg.test(this)) flag = false;

	return flag;
}
String.prototype.isIDCheck = function(minLen,maxLen) {
	var flag = true;
	if(minLen == null) minLen = 4;
	else minLen -= 1;
	if(maxLen == null) maxLen = 14;
	else maxLen -= 1;

	//var idReg = eval("/^[a-zA-Z]+[a-zA-Z0-9]{"+minLen+","+maxLen+"}$/g;");
	var idReg;
	(function(){eval("idReg = /^[a-zA-Z0-9]+[a-zA-Z0-9]{"+minLen+","+maxLen+"}$/g;");}())

	if(!idReg.test(this)) flag = false;

	return flag;
}*/
String.prototype.isNumber = function() {
	var flag = true;
	var num_check=/^[0-9]*$/g;
	if(!num_check.test(this)) flag = false;

	return flag;
}

function onFormSerialize(dForm) {
	var fData =  dForm.getFormData();
	var rstStr = "";
	for(key in fData) {
		if(rstStr != "") rstStr += "&";
		if(dForm.getItemType == "combo") {
			rstStr += key + "=" + dForm.getCombo(key).getSelectedValue();
		} else {
			rstStr += key + "=" +  encodeURI(dForm.getItemValue( key, true ));
		}
	}
	return rstStr;
}

function onFormDataSerialize(dForm) {
	var fData =  dForm.getFormData();
	var formData = new FormData();

	for(key in fData) {
		if(dForm.getItemType(key) == "file") {
			formData.append(key, $(dForm.getInput(key))[0].files[0]);
		} else if(dForm.getItemType(key) == "combo") {
			formData.append(key, dForm.getCombo(key).getSelectedValue());
		} else {
			formData.append(key, dForm.getItemValue( key, true ));
		}
	}
	return formData;
}


function strPad(i, l, s) {
	var o = i.toString();
	if (!s) { s = '0'; }
	while (o.length < l) {
		o = s + o;
	}
	return o;
}

function keepAlive() {
	var arCurrentUrl = location.href.split("?");
	var arNowUrl = arCurrentUrl[0].split("/");
	var currentPage = arNowUrl.pop();

	if(currentPage != 'init.do' && currentPage != '') {
		var rst = window.dhx4.ajax.getSync(contextPath+"/keep_alive.do");

		// @saint: IE에서 keepAlive()의 결과가 제대로 담기지 않았을 때 처리하는 코드 추가 반영
		// == 기존 ==
		// var response = jQuery.parseJSON(rst.xmlDoc.response);
		// == 기존 끝 ==
		// == 변경 ==
		var response = (function() {
			try {
				return jQuery.parseJSON(rst.xmlDoc.response);
			} catch (e) {
				var res = {};
				$.ajax({
					url: contextPath+"/keep_alive.do",
					type: "GET",
					async: false,
					dataType: "json",
					success: function(jRes) {
						res = jRes;
					}, error: function() {
						res = {"success": "N", "result": "0"};
					}
				});
				return res;
			}
		}());
		// == 변경 끝

		if(response.result == "1") {
			window.setTimeout('keepAlive()', 15*60*1000);
		} else {
			top.location.href = contextPath;
		}
	}
}

(function($) {
    $.fn.hasScrollBar = function() {
        return this.get(0) ? this.get(0).scrollHeight > this.innerHeight() : false;
    }
})(jQuery);

// url 파싱 해주는 함수
// 익스 URL도 없냐..하....있는게 뭐야..?
function parseUri(str){
	
	var o = parseUri.options;
	var m = o.parser[o.stircMode ? "strict" : "loose"].exec(str)
	var uri = {}
	var i = 14;
	
	while(i--){
		uri[o.key[i]] = m[i] || "";
	}
	
	uri[o.q.name] = {};
	uri[o.key[12]].replace(o.q.parser, function ($0, $1, $2){
		if ($1){
			uri[o.q.name][$1] = $2;
		}
	});
	
	return uri;
}

parseUri.options = {
		stricMode:false
	,	key:["source","protocol","authority","userInfo","user","password","host","port","relative","path","directory","file","query","anchor"]
	,	q:{
				name:"queryKey"
			,	parser:/(?:^|&)([^&=]*)=?([^&]*)/g
		}
	,	parser:{
				strict:/^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/
			,	loose: /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/
		}
}

Array.prototype.trim = function(){
	while (( this[0] === undefined || this[0] === null || this[0] === '') && this.length ){
		this.shift();
	}
	var len = this.length - 1;
	while(( this[len] === undefined || this[len] === null || this[len] ==='') && this.length ){
		this.pop();
		len = this.length - 1;
	}
	return this;
}

$(document).on("paste", ".inputFilter", function(e){
	
	var oriObj = $(this);
	var obj = (oriObj.is("input") ? oriObj : oriObj.find("input"));
	
	setTimeout(function(){
		var value = obj.val();
		
		// 숫자 영어 한글 일 경우 
		if(obj.hasClass("korFilter engFilter numberFilter")){
									
			value = value.replace(/[^가-힣|ㄱ-ㅎ|ㅏ-ㅣ|a-z|A-Z|0-9]/g,'');
			
		}else if(obj.hasClass("korFilter engFilter")){
			
			value = value.replace(/[^가-힣|ㄱ-ㅎ|ㅏ-ㅣ|a-z|A-Z]/g,'');
			
		}else if(obj.hasClass("korFilter numberFilter")){
			
			value = value.replace(/[^가-힣|ㄱ-ㅎ|ㅏ-ㅣ|0-9]/g,'');
			
		}else if(obj.hasClass("engFilter numberFilter")){
			
			value = value.replace(/[^a-z|A-Z|0-9]/g,'');
			
		}else if(obj.hasClass("numberFilter")){

			value = value.replace(/[^0-9]/g,'');
			
		}else if(obj.hasClass("engFilter")){
			
			value = value.replace(/[^a-z|A-Z]/g,'');
			
		}else if(obj.hasClass("korFilter")){
			
			value = value.replace(/[^가-힣|ㄱ-ㅎ|ㅏ-ㅣ]/g,'');
		}
		
		var tempValue = value
		var maxLength = Number(obj.attr("maxlength"));
		
		if (obj.hasClass("dateFilter")){
			tempValue = value.replace(/-/g,'');
			maxLength -= 2;
		}
		if (obj.hasClass("timeFilter")){
			tempValue = value.replace(/:/g,'');
			maxLength -= 2;
		}
		
		if (tempValue.length > maxLength){
			value = tempValue.substring(0,maxLength)
		}
			
		if (obj.hasClass("korFilter") || obj.hasClass("engFilter") || obj.hasClass("numberFilter"))
			obj.val(value.trim());
		
		oriObj.trigger("keyup")
	},0);
	
});

$(document).on("keydown", ".inputFilter", function(e){
		
	if (e.ctrlKey && (e.keyCode==67||e.keyCode==86||e.keyCode==88))
		return true;
	
	var oriObj = $(this);
	var obj = (oriObj.is("input") ? oriObj : oriObj.find("input"));
	
	// 방향키+home+end+tab 정도는 넘겨줄수 있자나 ㅎㅎ..
	if ((e.keyCode >= 35 && e.keyCode <= 40) || e.keyCode == 9)
		return true;
	
	// 상단 숫자인데 쉬프트키를 눌렀을때
	if((e.keyCode>=48&&e.keyCode<=57&&e.shiftKey)){ 
		e.preventDefault();
		e.stopPropagation();
		return false;
	}
	
	// 숫자 영어 한글 일 경우 
	if(oriObj.hasClass("korFilter engFilter numberFilter")){
													                                                                           
		if(		
				(e.keyCode<48||e.keyCode>57) && 		// 숫자 패드
				(e.keyCode<96||e.keyCode>105) &&		// 상단 숫자
				(e.keyCode<65||e.keyCode>90) &&			// 영문자 키  
				(e.keyCode!=229) &&						// 한글 키
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=32) &&						// 스페이스
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}else if(oriObj.hasClass("korFilter engFilter")){
		
		if(		
				(e.keyCode<65||e.keyCode>90) &&			// 영문자 키  
				(e.keyCode!=229) &&						// 한글 키
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=32) &&						// 스페이스
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}else if(oriObj.hasClass("korFilter numberFilter")){
		
		if(		
				(e.keyCode<48||e.keyCode>57) && 		// 숫자 패드
				(e.keyCode<96||e.keyCode>105) &&		// 상단 숫자
				(e.keyCode!=229) &&						// 한글 키
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=32) &&						// 스페이스
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}else if(oriObj.hasClass("engFilter numberFilter")){
		
		if(e.keyCode == 229){
			obj[0].value = obj[0].value.replace(/[가-힣|ㄱ-ㅎ|ㅏ-ㅣ]/g,'');
			return false;
		}
		
		if(		
				(e.keyCode<48||e.keyCode>57) && 		// 숫자 패드
				(e.keyCode<96||e.keyCode>105) &&		// 상단 숫자
				(e.keyCode<65||e.keyCode>90) &&			// 영문자 키  
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=32) &&						// 스페이스
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}else if(oriObj.hasClass("numberFilter")){

		if(e.keyCode == 229){
			obj[0].value = obj[0].value.replace(/[가-힣|ㄱ-ㅎ|ㅏ-ㅣ]/g,'');
			return false;
		}

		if(		
				(e.keyCode<48||e.keyCode>57) && 		// 숫자 패드
				(e.keyCode<96||e.keyCode>105) &&		// 상단 숫자
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return true;
		}
		
	}else if(oriObj.hasClass("engFilter")){
		
		if(e.keyCode == 229){
			obj[0].value = obj[0].value.replace(/[가-힣|ㄱ-ㅎ|ㅏ-ㅣ]/g,'');
			return false;
		}
		
		if(		
				(e.keyCode<65||e.keyCode>90) &&			// 영문자 키  
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=32) &&						// 스페이스
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}else if(oriObj.hasClass("korFilter")){
		
		if(		
				(e.keyCode!=229) &&						// 한글 키
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=32) &&						// 스페이스
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}else if(oriObj.hasClass("ipFilter")){
		
		if(e.keyCode == 229){
			obj[0].value = obj[0].value.replace(/[가-힣|ㄱ-ㅎ|ㅏ-ㅣ]/g,'');
			return false;
		}
		
		if(		
				(e.keyCode<48||e.keyCode>57) && 		// 숫자 패드
				(e.keyCode<96||e.keyCode>105) &&		// 상단 숫자
				(e.keyCode!=8) &&						// 백스페이쑤
				(e.keyCode!=110 && e.keyCode!=190) &&	// 닷(.)
				(e.keyCode!=46)){						// 딜리트
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	} else if(oriObj.hasClass("timeFilter")){
		
		if(e.keyCode == 229){
			obj[0].value = obj[0].value.replace(/[가-힣|ㄱ-ㅎ|ㅏ-ㅣ]/g,'');
			return false;
		}
		
		if(		
				(e.keyCode<48||e.keyCode>57) && 		// 숫자 패드
				(e.keyCode<96||e.keyCode>105) &&		// 상단 숫자
				(e.keyCode==59) &&						// :
				(e.keyCode==45)){						// -
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
		
	}
});


$(document).on("focus", ".inputFilter", function(e){
	$(this).val($(this).val().trim());
	
	var oriObj = $(this);
	var obj = (oriObj.is("input") ? oriObj : oriObj.find("input"));
	var value = obj.val();
	
	if (obj.hasClass("input_time")){      
		
		 (function(time){
			 time = time.replace(/:/g,'');
				obj.val(time);
			}(value));
	}
	if (obj.hasClass("ui_input_cal")){      
		
		 (function(date){
			date = date.replace(/-/g,'');
			obj.val(date);
		}(value));
	}
	if(oriObj.parent().find("td").length==0)
		obj.val(sqlFilter(obj.val()));
});


$(document).on("blur", ".inputFilter", function(e){
	$(this).val($(this).val().trim());
	
	var oriObj = $(this);
	var obj = (oriObj.is("input") ? oriObj : oriObj.find("input"));
	var value = obj.val();
	
	if (obj.hasClass("input_time")){      
		
		 (function(time){
			time = time.replace(/:/g,'');
			var timeLength = time.length;
			var temp='';
		
			if (timeLength != 0 && time!='0'){
				var remainLength=6-timeLength;
				for(var i=0;i<remainLength;i++){
					temp+='0'
				}
				temp+=time;
				if(Number(temp.substring(2,4))>=60){
					alert(lang.views.search.alert.msg70)	/* 올바른 시간형식을 입력해 주세요 */
					obj.val("")
					obj.focus()
				}else if(Number(temp.substring(4,6))>=60){
					alert(lang.views.search.alert.msg70)	/* 올바른 시간형식을 입력해 주세요 */
					obj.val("")
					obj.focus()
				}else{
					obj.val(temp.substring(0,2)+":"+temp.substring(2,4)+":"+temp.substring(4,6));
				}
			}else{
				obj.val("");
				return "";
			}
			if(obj.attr("title").indexOf("까지")==-1&&obj.attr("title").indexOf("부터")>0){
				fromTo(obj,obj.next(),obj)
			}
			else if(obj.attr("title").indexOf("까지")>0&&obj.attr("title").indexOf("부터")==-1){
				fromTo(obj.prev(),obj,obj)
			}
		}(value));
	}
	if (obj.hasClass("ui_input_cal")){      
		
		 (function(date){
			date = date.replace(/-/g,'');
			var dateLength = date.length;
			var temp='';
			if (dateLength ==8){
				if(Number(date.substring(4,6))>12){
					alert(lang.common.alert.enter.validDateFormat /* "올바른 날짜형식을 입력해 주세요" */ )
					obj.val("")
					obj.focus()
				}else if(Number(date.substring(6,8))>31){
					alert(lang.common.alert.enter.validDateFormat /* "올바른 날짜형식을 입력해 주세요" */)
					obj.val("")
					obj.focus()
				}else{
					obj.val(date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
				}
			}else{
				obj.val("");
				return "";
			}
		}(value));
	}
	if (obj.hasClass("input_userid")){      
		
		 (function(userid){
			var useridLength = userid.length;
			var temp='';
		
			if (useridLength != 0 && userid!='0'){
				var remainLength=8-useridLength;
				for(var i=0;i<remainLength;i++){
					temp+='0'
				}
				temp+=userid;
				obj.val(temp);
			}else{
				obj.val("");
				return "";
			}
		}(value));
	}
	if (obj.hasClass("input_stock")){      
		
		 (function(stock){
			var stockLength = stock.length;
			var temp='';
		
			if (stockLength != 0 && stock!='0'){
				var remainLength=11-stockLength;
				for(var i=0;i<remainLength;i++){
					temp+='0'
				}
				temp+=stock;
				obj.val(temp);
			}else{
				obj.val("");
				return "";
			}
		}(value));
	}
	if(oriObj.parent().find("td").length==0)
		obj.val(sqlFilter(obj.val()));
});

function sqlFilter(str){
	var sqlFilter = new Array ("cmdshell","union","drop","select","xp_","sp_","\'", "\"", "--", ",", "\\(", "\\)", "\\#", ">", "<", "=", "\\*\\/", "\\/\\*", "\\+", "%", ",", "@", ";", "\\\\", "|", "\\[", "\\]", "&")
	for(var i=0; i< sqlFilter.length; i++){
		var regex = new RegExp ([sqlFilter[i]],'gi');
		str = str.replace(regex,"");
	}
	return str
}

$(document).on("keyup", ".inputFilter", function(e){
	if (e.ctrlKey && (e.keyCode==67||e.keyCode==86||e.keyCode==88))
		return true;
	
	var oriObj = $(this);
	var obj = (oriObj.is("input") ? oriObj : oriObj.find("input"));
	
	// 방향키+home+end+tab 정도는 넘겨줄수 있자나 ㅎㅎ..
	if ((e.keyCode >= 35 && e.keyCode <= 40) || e.keyCode == 9)
		return true;
	
	// 상단 숫자인데 쉬프트키를 눌렀을때
	if((e.keyCode>=48&&e.keyCode<=57&&e.shiftKey)){ 
		e.preventDefault();
		e.stopPropagation();
		return false;
	}
	
	if(oriObj.hasClass("dateFilter")){
		//var date= obj[0].value.replace(/-/gi,'').replace(/\//gi,'');

		//obj[0].value=date
	}else if (oriObj.hasClass("timeFilter")){
		//var value = obj[0].value;
		
		// 숫자 이외 걸러 내기
		//value = value.replace(/[^0-9]/g,'');
		
		//this.value = value;	
	}
});

//비교대상 폼(시작), 비교대상 폼(까지), 포커스 될 타겟 
function fromTo(obj_from, obj_to,focusTarget) {
	if(obj_from.val()!=''&&obj_from.val()!=null&&obj_to.val()!=''&&obj_to.val()!=null){
		var from, to;
		from=Number(obj_from.val().replace(/[-:]/g,''));
		to=Number(obj_to.val().replace(/[-:]/g,''));
		if(to-from<0&&$(focusTarget).attr("id").indexOf("ime")==-1){
			if($(focusTarget).attr("id")==obj_from.attr("id")){
					$(focusTarget).val(obj_to.val());
			}else{
				alert(lang.evaluation.js.alert.msg37/*"종료시점이 시작시점보다 이전입니다."*/)
				$(focusTarget).val('');
				$(focusTarget).focus()
				return false;
			}
		}else if(to-from<=0&&$(focusTarget).attr("id").indexOf("ime")!=-1){
			if(to-from==0 &&$(focusTarget).attr("id").indexOf("Rtime")!=-1 ){
				return true
			}
			alert(lang.evaluation.js.alert.msg38/*"종료시점이 시작시점과 같거나 이전입니다."*/)
			$(focusTarget).val('');
			$(focusTarget).focus()
			return false;
		}
	}
}


//한달 전, 또는 m 개월 전을 계산 한다 ㄱㄱ
function oneMonthBefore(s, m){
	var d;
	var c;
	
	if(s == undefined){
		d = new Date();
	}else{
		d = s; 
	}
	

	if(m == undefined){
		c = 1;
	}else{
		c = m; 
	}

	var lastDayofLastMonth = (new Date( d.getYear(), d.getMonth(), 0 )).getDate();

	if(d.getDate() > lastDayofLastMonth){
		d.setDate(lastDayofLastMonth);
	}

	var month = d.getMonth()-c;
	d.setMonth(month);
	
	return d;
}

function tryCatch(e) {
	var a=null;
}

//숫자(문자형)을 번호 형식으로 바꾸는 함수. type이 'masking'이면 가운데 자리 숨김
function phoneFomatter(num,type) {
	var formatNum = '';
	if(num.length==11){
		if(type=='masking'){
			formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-****-$3');
		}else{
			formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
		}
	}else if(num.length==8){
		if(type=='masking'){
			formatNum = num.replace(/(\d{4})(\d{4})/, '$1-****');
		}else{
			formatNum = num.replace(/(\d{4})(\d{4})/, '$1-$2');
		}
	}else if(num.length<8){
		if(type=='masking'){
			formatNum = num.replace(/[0-9]/g, '*');
		}else{
			formatNum = num;
		}
	}else{
		if(num.indexOf('02')==0){
			if(type=='masking'){
				formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-****-$3');
			}else{
				formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
			}
		}else{
			if(type=='masking'){
				formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-****-$3');
			}else{
				formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
			}
		}
	}
	return formatNum;
}

function datepickerSetting(setLang, obj, setDate){
	if(setLang =='en'){
		$.datepicker.regional['en'] = {
				closeText: "Done",
				prevText: "Prev",
				nextText: "Next",
				currentText: "nowMon",
				monthNames: [ "January","February","March","April","May","June", "July", "August","September","October","November","December" ],
				monthNamesShort: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ],
				dayNames: [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ],
				dayNamesShort: [ "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" ],
				dayNamesMin: [ "Su","Mo1","Tu","We","Th","Fr","Sa" ],
				weekHeader: "Wk",
				dateFormat: 'yy-mm-dd',
				firstDay: 1,
				isRTL: false,
				showMonthAfterYear: false,
				yearSuffix: ""
		};
		$.datepicker.setDefaults($.datepicker.regional['en']);
	} else if(setLang =='jp'){
		$.datepicker.regional['jp'] = {
				closeText : '閉じる',
			    prevText : '前の月',
			    nextText : '来月',
			    currentText : '今日',
			    monthNames : ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
			    monthNamesShort : ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
			    dayNames : ['日', '月', '火', '水', '木', '金', '土'],
			    dayNamesShort : ['日', '月', '火', '水', '木', '金', '土'],
			    dayNamesMin : ['日', '月', '火', '水', '木', '金', '土'],
			    weekHeader : 'Wk',
			    dateFormat : 'yy-mm-dd',
			    firstDay : 0,
			    isRTL : false,
			    showMonthAfterYear : false,
			    yearSuffix : '年'
		};
		$.datepicker.setDefaults($.datepicker.regional['jp']);
	} else{
		$.datepicker.regional['ko'] = {
			    closeText : '닫기',
			    prevText : '이전달',
			    nextText : '다음달',
			    currentText : '오늘',
			    monthNames : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			    monthNamesShort : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			    dayNames : ['일', '월', '화', '수', '목', '금', '토'],
			    dayNamesShort : ['일', '월', '화', '수', '목', '금', '토'],
			    dayNamesMin : ['일', '월', '화', '수', '목', '금', '토'],
			    weekHeader : 'Wk',
			    dateFormat : 'yy-mm-dd',
			    firstDay : 0,
			    isRTL : false,
			    showMonthAfterYear : false,
			    yearSuffix : '년'
		};
		$.datepicker.setDefaults($.datepicker.regional['ko']);
	}
	if(setDate !=undefined){
		$(obj).datepicker().datepicker("setDate", setDate);
	}else{
		$(obj).datepicker().datepicker("setDate", new Date());
	}

}