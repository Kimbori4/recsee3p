// IPECS IPCR default color
/*var baseMainColor= '60b3dc';
var baseSubColor= '2b94c8';
var baseMainTabbarColor= '2b94c8';
var baseMainTxtColor= 'ffffff';
var baseSubTxtColor= 'ffffff';
var baseActivColor= '60b3dc';
*/

// 신한생명 default color
/*var baseMainColor= 'd99103';
var baseSubColor= '2d71c4';
var baseMainTabbarColor= '2d71c4';
var baseMainTxtColor= 'ffffff';
var baseSubTxtColor= 'ffffff';
var baseActivColor= 'd99103';
*/




// Jquery Include 필수
function ui_controller(){

    // ::Main UI Control
    var ui_main_bg_flat         = $(".ui_main_bg_flat");
    var ui_main_bg_gradi        = $(".ui_main_bg_gradi");
    var ui_main_border          = $(".ui_main_border, .top_menu li ul, .ui_main_txt, .top_menu ul ul li:first-of-type");
    var ui_main_btn_flat        = $(".ui_main_btn_flat, .filebox label");
    var ui_main_btn_flat_white  = $(".ui_main_btn_flat_white");
    var ui_main_btn_gradi       = $(".ui_main_btn_gradi");
    var ui_main_txt             = $(".ui_main_txt");
    var ui_tabbar_bg_flat		= $(".ui_tabbar_header");


    $(ui_tabbar_bg_flat).css({
        "background-color" : "#" + baseMainTabbarColor,
        "color" : "#" + baseMainTxtColor
    });

    // ::Main Text Style
    $(ui_main_txt).css({
        "color" : "#" + baseMainColor
    });

    // ::Main Background Style - flat
    $(ui_main_bg_flat).css({
        "background-color" : "#" + baseMainColor,
        "color" : "#" + baseMainTxtColor
    });

    // ::Main Background Style - gradation
    $(ui_main_bg_gradi).css({
        "background-color" : "#" + baseMainColor,
        "background-image" : "linear-gradient(#" + baseMainColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-ms-linear-gradient(#" + baseMainColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-moz-linear-gradient(center top, #" + baseMainColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-o-linear-gradient(top, #" + baseMainColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-webkit-gradient(linear, left top, left bottom, from(#" + baseMainColor + "), to(#" + subtractColor(baseMainColor, 444444) + "))",
        "color" : "#" + baseMainTxtColor
    })

    // ::Main Border Style - flat
    $(ui_main_border).css({
        "border-color" : "#" + baseMainColor
    });

    // ::Main Button Style - flat
    $(ui_main_btn_flat).css({
        "background-color" : "#" + baseMainColor,
        "border-color": "#" + baseMainColor,
        "color" : "#" + baseMainTxtColor,
        "cursor" : "pointer",
        "transition" : "all ease .3s"
    });
    $(ui_main_btn_flat).hover(
        // mouseover
        function(){$(this).css({
                "background-color": "#" + subtractColor(baseMainColor, 111111)
            });
        },
        // mouseout
        function(){$(this).css({
                "background-color": "#" + baseMainColor
            });
        }
    );

    // ::Main Button Style - flat : white/main
    $(ui_main_btn_flat_white).css({
        "background-color" : "#ffffff",
        "border-color": "#dddddd",
        "color" : "#999999",
        "cursor" : "pointer",
        "transition" : "all ease .3s",
    });
    $(ui_main_btn_flat_white).hover(
        // mouseover
        function(){$(this).css({
                "background-color": "#" + baseMainColor,
                "border-color": "#" + baseMainColor,
                "color": "#ffffff"
            });
        },
        // mouseout
        function(){$(this).css({
                "background-color": "#ffffff",
                "border-color": "#dddddd",
                "color" : "#999999",
            });
        }
    );



    // Sub UI Control
    var ui_sub_bg_flat   = $(".ui_sub_bg_flat, .sys_monitroing_atc .sys_monitroing_atc_header .sys_monitroing_atc_tit, .ui_pannel_popup_header");
    var ui_sub_bg_gradi  = $(".ui_sub_bg_gradi");
    var ui_sub_border    = $(".ui_sub_border, div.gridbox_dhx_web.gridbox .xhdr");
    var ui_sub_btn_flat  = $(".ui_sub_btn_flat");
    var ui_sub_btn_gradi = $(".ui_sub_btn_gradi");

    // ::Sub Background Style - flat
    $(ui_sub_bg_flat).css({
        "background-color" : "#" + baseSubColor,
        "color" : "#" + baseSubTxtColor
    });

    // ::Sub Background Style - gradation
    $(ui_sub_bg_gradi).css({
        "background-color" : "#" + baseSubColor,
        "background-image" : "linear-gradient(#" + baseSubColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-ms-linear-gradient(#" + baseSubColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-moz-linear-gradient(center top, #" + baseSubColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-o-linear-gradient(top, #" + baseSubColor + ", #" + subtractColor(baseMainColor, 444444) + ")",
        "background" : "-webkit-gradient(linear, left top, left bottom, from(#" + baseSubColor + "), to(#" + subtractColor(baseMainColor, 444444) + "))",
        "color" : "#" + baseSubTxtColor
    })

    // ::Sub Border Style - flat
    $(ui_sub_border).css({
        "border-color" : "#" + baseSubColor
    });

    // ::Sub Button Style - flat
    $(ui_sub_btn_flat).css({
        "background-color" : "#" + baseSubColor,
        "border-color" : "#" + baseSubColor,
        "color" : "#" + baseSubTxtColor,
        "cursor" : "pointer",
        "transition" : "all ease .3s",
    });
    $(ui_sub_btn_flat).hover(
        // mouseover
        function(){$(this).css({
                "background-color": "#" + subtractColor(baseSubColor, 111111)
            });
        },
        // mouseout
        function(){$(this).css({
                "background-color": "#" + baseSubColor
            });
        }
    );
}


// 컬러코드 자동계산을 위한 16진수 계산식
function addColor() {
    var dec = arguments;
    if(!dec || !dec.length) return null;

    var result = {
        r: 0,
        g: 0,
        b: 0
    };

    for(var i=0; i<dec.length; i++) {
        if(!dec[i] || !dec[i].length) {
            continue;
        }
        var type = dec[i].charAt(0) == "-"?"-":"+";
        var nowColor = dec[i].replace(/[^(?=\dA-Fa-f)]/g, "");
        if(nowColor.length!= 6) {
            continue;
        }

        nowColor = lpad(nowColor, "0", 6);
        var r = nowColor.substr(0,2);
        var g = nowColor.substr(2,2);
        var b = nowColor.substr(4,2);

        result.r += (parseInt(r, 16) * (type == "-"?-1:1));
        result.g += (parseInt(g, 16) * (type == "-"?-1:1));
        result.b += (parseInt(b, 16) * (type == "-"?-1:1));
    }

    // 결과 16진수로 리턴
    result.r = lpad(handleOverflow(result.r).toString(16), "0", 2);
    result.g = lpad(handleOverflow(result.g).toString(16), "0", 2);
    result.b = lpad(handleOverflow(result.b).toString(16), "0", 2);

    return result.r + result.g + result.b;

    // lpading
    function lpad(data, char, len) {
        var result = "";
        for(var i=0; i<len; i++) {
            result += char;
        }

        result = (result + data).slice(-len);
        return result;
    }

    function handleOverflow(data) {
        // overflow 예외처리
        return data<0?0:(data>255?255:data);
    }
}

function subtractColor(a, b) {
    return addColor(a, "-" + b);
}

function select2InputFilter(){
	$(document).find('.select2 input').keydown(function(e){
		// 상단 숫자인데 쉬프트키를 눌렀을때
		
		if((e.keyCode>=48&&e.keyCode<=57&&e.shiftKey) || (e.keyCode==189 || e.keyCode==187 || e.keyCode==220 || e.keyCode== 192
				|| e.keyCode== 111 || e.keyCode== 106 || e.keyCode== 109 || e.keyCode== 107 )){ 
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	})
	$(document).find('.select2 input').keyup(function(e){
		var re=/[~!@#$%^&*\()\-\\=+_']/gi;
		var temp = $(this).val();
		if(re.test(temp)){
			$(this).val(temp.replace(re,""));
		}
	})
}