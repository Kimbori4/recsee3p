/*var includeComponent = (function(mode) {
	mode = mode||"";
	//var rp = commResourcePath||"";
	var soPath = siteOriginPath||"";
	var srPath = siteResourcePath||"";
	var componentPath = compoResourcePath||"";
	var siteResourceArray = ["ipecs", "nx"]; // 나중에 모드생기면 여기다 추가하기
	var siteRegexp = new RegExp("^(/|)(" + siteResourceArray.join("|") + ")")
	var compoRegexp = /^(|\/)component/;

	$("link[data-href*=css\\/], script[data-src*=js\\/]").each(function() {
		var attrName = (function(tagName) {
			if(tagName == "link") 			return "href";
			else if(tagName == "script")	return "src";
		}(($(this).prop("tagName")||"").toLowerCase()))
		var src = (function(dataSrc) {
			var basePath = (function(src) {
				var path;
				if(siteRegexp.test(src)) {
					// siteResourceArray 내의 값으로 시작하면  siteResourcePath을 붙이고
					path = srPath;
				} else if(compoRegexp.test(src)) {
					// component가 있으면 compoResourcePath를 붙이고,
					path = componentPath;
				} else {
					// 없으면 siteOriginPath을 붙임
					path = soPath;
				}
				// 끝에 / 붙이고
				return path + "/";
			}(dataSrc));
			// 처음에 / 없앰
			dataSrc = dataSrc.replace(siteRegexp, "").replace(compoRegexp, "").replace(/^\//, "");
			return basePath + dataSrc + (!mode?"":"?t=" + new Date().getTime());
		}($(this).attr("data-" + attrName)));
		$(this).attr(attrName, src);
	});
});
includeComponent();
*/
/*
function loadCSS(type, path) {
		// 첫 슬래시 지우기
		path = path.replace(/^\//, "");
		var basePath, path,
			link = '<link rel="stylesheet" type="text/css" href="{BASEPATH}/{PATH}" >';
		if(type == "component") {
			basePath = compoResourcePath;
		} else if(type == "common") {
			basePath = commResourcePath + "/recsee";
		} else if(type == "site") {
			basePath = siteResourcePath;
		} else {
			basePath = "";
		}
		link = link.replace(/\{BASEPATH\}/, basePath).replace(/\{PATH\}/, path);
		document.write(link);
	}
	loadCSS("site", "/css/page/login.css");
*/