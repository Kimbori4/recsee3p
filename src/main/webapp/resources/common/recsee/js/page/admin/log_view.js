/*백스페이스 방지 */
var a;
$(document).on("keydown", function (e) {
	if (e.which === 8 && !($(e.target).is("input, textarea") && ( $(e.target).attr("readOnly") == undefined || $(e.target).attr("readOnly").toUpperCase() != "READONLY" ) )) {
	e.preventDefault();
	}
});

function initTree(rootDir,rootIp){
	
	var dir =rootDir.replace(/\\/gi,'/');
	var rootName = dir.split('/').trim().pop()
	var rootHost = parseUri(rootIp).host;
	
	$("#filetree").append(''+
		'<ul>'+
			'<li>'+
				/*'<a id="/var/REC'+rootIp+rootDir+'" ip ="'+rootIp+'" path="'+rootDir+'" class="dir root" href="javascript:void( 0 );"> ('+rootHost+') '+rootName+' </a>'+*/
				'<a id="/var/REC'+rootIp+rootDir+'" ip ="'+rootIp+'" path="/var/REC/log/SIGNAL" class="dir root" href="javascript:void( 0 );">SIGNAL </a>'+
			'</li>'+
		'</ul>'
	);
}

function getList(strPath, ip){
	var returnObj = {
			"file" : ""
	};
	
	var dataStr = {
		//"url" : ip+"/list?path="+encodeURI(strPath.replace(/\\/gi,'/')),
		//"url" : "http://localhost:28881/list?path="+encodeURI(strPath.replace(/\\/gi,'/')),
			"path":encodeURI(strPath.replace(/\\/gi,'/'))
	};
	//FIXME :: 추후 서버의아이피로 변경하여 요청 할 것
	$.ajax({
		//url: ip+"/list",
		url:contextPath + "/logList.do",
		data:dataStr,
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.result == "success") {
				returnObj = jRes.data
			}else{
				alert(jRes.data);
			}
		}
	});
	
	return returnObj;
}

function getFile(strPath,ip){
	$( "pre#fileoutput" ).text("");
	var request = new XMLHttpRequest();
	
	var paggingSize = $("#paging").val() || "1";
	paggingSize = paggingSize*1024*1024;
	var page = $(".pagination .active").attr("num") || "1";
	request.open("GET", contextPath+"/getLog.do?url="+encodeURI(strPath.replace(/\\/gi,'/'))+"&page="+page+"&size="+paggingSize)
	
	
	request.send(null);
	progress.on();
	request.onreadystatechange = function(){
		if (request.readyState === 4 && request.status === 200){
			$(".lineline").remove();
			var $textArea = $('<textarea class="lined" id="fileoutput"></textarea>');
			
			$(".linedtextarea").append($textArea);
			a=request;
			var rText = request.responseText;
			$textArea.html(rText);
			$textArea.lineLine();
			progress.off();
			/*temp = request.responseText;*/
			/*$("#fileoutput, .codelines").children().remove()*/
			
			/*$( "#fileoutput" ).text(request.responseText.replace(/\r\n/));
			var lines = request.responseText.split(/\r\n/);
	    	var endIdx = lines.length
			var idx = 1;
	    	
	    	setTimeout(appendNum(),25);
	    				    	
	    	function appendNum(){
	    		$(".codelines").append(''+
					'<div class="lineno" idx="'+idx+'">'+idx+'</div>'
				);
	    		$( "#fileoutput" ).append(''+
	    			'<div class="linecontents" idx="'+idx+'">'+lines[idx-1]+'</div>'	
	    		);
	    		
				idx++;
	        	if(idx <= endIdx){
	        		setTimeout(appendNum(),25);
	        	}else{
	        		idx = 1;
	        		setTimeout(heightSync(),25);
	        	}
	    	}
	    	
	    	function heightSync(){
	    		// ie 샛긔..
	    		// css 높이값 소수점 못가지고옴 ㅎ;; 어이없넹..
	    		// 그래서 css 높이 제거후, 자기 높이 구하고(소수점 제거 해서 리턴됨).. 다시 자기한테 부여함 ㅋㅋ;; 
	    		// 그래야 라인넘버랑... 높이가 동일해짐;;ㅎㅎ
	    		// 쓰레기 ie...
	    		$(".linecontents[idx='"+idx+"']").css("height","");
	    		var height = $(".linecontents[idx='"+idx+"']").css("height")
	    		
        		$(".lineno[idx='"+idx+"']").css("height",height);
	    		$(".linecontents[idx='"+idx+"']").css("height",height);
	    		idx++;
	    		if(idx < endIdx){
	        		setTimeout(heightSync(),25);
	        	}
	    	}*/
		}
	}	
}

$(function(){
	
	var rootPath = ($("#logPath").val()||"").split("|")
	var rootIp = ($("#logUrl").val()||"").split("|")
	
	if (rootPath.length > 0){
		for (var i = 0 ; i < rootPath.length ; i++){
			initTree(rootPath[i],rootIp[i]);
		}
	}
	
	
	
	// 텍스트 에어리어 스크롤 시 좌측 라인 넘버 스크롤 동기화
	$( "#fileoutput" ).scroll(function(){
		var scroll = $(this).scrollTop()
		$(".codelines").scrollTop(scroll)
	});
	
	// 라인넘버 스크롤 막기
	$(".codelines").on('scroll touchmove mousewheel', function(e){
		
		e.preventDefault();
		e.stopPropagation()
		
		var scroll = $("#fileoutput").scrollTop()
		$(this).scrollTop(scroll);
		
		return false;
		
	});
	
	$(document).on("click", "#filetree a", function(){
		var jLink = $( this );
		var jSubDir = jLink.siblings( "ul" );
		// dir이고, 리스트가 없을 경우에만 진행
		if(jLink.hasClass("dir")){
			if ((jLink.next( "ul" ).length == 0)){
				var path = this.id;
				var ip = jLink.attr("ip");
				
				if(jLink.hasClass("root"))
					path = jLink.attr("path");
				
				var fileObj = getList(path,ip).file;
				
				if (fileObj){
					
			    	var endIdx = fileObj.length
					var idx = 0;
			    	setTimeout(appendList(),25);
			    				    	
			    	function appendList(){

			    		var file = fileObj[idx];
			    		jLink.parent().append(''+
							'<ul>'+
								'<li>'+
									'<a id="'+file.path+'" class="'+file.type+'" size="'+file.size+'" ip="'+ip+'"  href="javascript:void( 0 );"> '+file.name+' </a>'+
									/*'<a href="javascript:void( 0 );"> REC </a>'+*/
								'</li>'+
							'</ul>'
						);
						idx++
			        	if(idx < endIdx){
			        		setTimeout(appendList(),25);
			        	}else{
			        		jSubDir = jLink.siblings( "ul" );
			        	}
			    	}
				}
			}
			jSubDir.toggle();
		}else{
			
			var jLink = $( this );
			$( "a.selected" ).removeClass( "selected" );
			jLink.addClass( "selected" );		
			getFile(this.id.substr(19), jLink.attr("ip"));
			
			// 기존 다른파일의 페이징 삭제
			$(".pagination a").not(".default").remove();
			
			var size = jLink.attr("size");
			var paggingSize = $("#paging").val() || "1"
			paggingSize = paggingSize*1024*1024
			
			var pageNum = Math.ceil(size/paggingSize) || 1;
			$(".pagination").data({"pageNum":pageNum});
			
			var idx = 0;
			
			setTimeout(function(){
				addPageNum()
			},25);
			
			function addPageNum(){
				$(".pagination a").eq(-2).before("<a class='pagingBtn "+(idx==0 ? 'active' : '')+"' href='#' num='"+(idx+1)+"'>"+(idx+1)+"</a>");
				idx++;
				if (idx < pageNum && idx < 10){
					addPageNum();
				}
			}
		
		}			
	});	
	
	$(document).on("click", ".pagination .pagingBtn", function(){
		$(".pagination .active").removeClass("active")
		$(this).addClass("active")
		getFile($( "a.selected" )[0].id.substr(19) ,$('#filetree a.selected').attr("ip"));
		$(".pagination").data({"now":$(this).attr("num")})
	});
	
	// 엔터 처리
	$("#filter").on("keydown",function(e){
		if(e.which == 13)
			filterWord();
	});
	// 클릭 처리
	$("#searchBtn").click(function(){
		filterWord();
	});
	
	// 단어 필터링~
	function filterWord(){
		var $filter = $("#filter");
		
		var filter= $filter.val();
		
		$(".filterLine").removeClass("filterLine");
		
		// 입력단어가 1자 이상일때
		if(filter.length > 0){
			// 필터링 단어와 일치하는 라인의 단어는 형광펜 색칠색칠
			$(".linecontents:contains('"+filter+"')").each(function(){
				var regex = new RegExp(filter,'gi');
				$(this).html( $(this).text().replace(regex,"<span class='text-yellow'>"+filter+"</span>"));
			});
			
			// 나머지는 tr td 구조이므로, tr을 숨김처리. (filterLine)
			$(".linecontents").not(":contains('"+filter+"')").each(function(){
				$(this).html( $(this).text().replace(/"<span class='text-yellow'>"/gi,'').replace(/"<\/span>"/gi,'') )
				$(this).parent().addClass("filterLine");
			});
		// 공백이라면 모든 필터링 처리 삭제 
		}else{
			$(".linecontents").each(function(){
				$(this).html( $(this).text().replace(/"<span class='text-yellow'>"/gi,'').replace(/"<\/span>"/gi,'') )				
			});
		}
	}
	
	// 페이징 용량 숫자 입력 처리
	$("#paging").on("keyup",function(e) {                    	
    	var value = this.value;
    		
    	// 숫자 이외 걸러 내기
    	value = value.replace(/[^0-9]/g,'');
    	
    	// 1~3 사이만 가능..
    	// 더한다면 성능 장담 X
    	if (value >= 3)
    		value = 3
    	
    	if (value === "0")
    		value = 1

    	this.value = value;
    });
	
	// 페이징 mb 설정 할때.. 값을 안쓰고 나오는 현상 방지 (페이징 파일 요청할때 디폴트 1로 수식 계산 해주긴 함..)
	$("#paging").on("blur",function(e) {
		if($(this).val() === ""){
			$(this).val("1")
		}
	});
	
	// 페이징 버튼 |< < > >| 에 대한 처리
	// 자세한 주석은 >| 에다가 달아놓음
	
	// 제일 처음 페이지로
	$(".pagination .absLeft").click(function(){
		
		// 여긴 그냥.. 파일 선택 했을 때 페이징 그려주는거랑 또옥 같다.
		// 다만, 현재 활성화된 페이징 버튼 바꿔주는 부분만 다를뿐..ㅎㅎ
		
		// 기존 다른파일의 페이징 삭제
		$(".pagination a").not(".default").remove();
		
		var size = $( "a.selected" ).attr("size");
		var paggingSize = $("#paging").val() || "1"
		paggingSize = paggingSize*1024*1024
		
		var pageNum = Math.ceil(size/paggingSize) || 1;
		
		$(".pagination").data({"pageNum":pageNum});
		
		var idx = 0;
		
		setTimeout(function(){
			addPageNum()
		},25);
		
		function addPageNum(){
			$(".pagination a").eq(-2).before("<a class='pagingBtn' href='#' num='"+(idx+1)+"'>"+(idx+1)+"</a>");
			idx++;
			
			if (idx < pageNum && idx < 10){
				addPageNum();
			}else{
				$( ".pagination .active" ).removeClass( "active" );
				var now = $(".pagination").data("now");
				$(".pagination a[num='"+now+"']").addClass( "active" );
			}
		}
	});
	
	// 이전 페이징으로
	$(".pagination .left").click(function(){
		
		// 페이지의 총 갯수
		var pageNum = $(".pagination").data("pageNum") || 1;
	
		// 현재 (클릭 전에..) 페이징중 마지막 페이지 넘버
		var idx = Math.ceil($(".pagination .pagingBtn.active").last().attr("num") || 1);
		// 순회 순회
		var count = 0;
		// 만약에 제일 마지막 페이징 그룹(10개씩 묶었을 경우)일 때에는... 전체 - 이전페이징그룹의 마지막 갯수 만큼만 그려준다 
		// (ex 총 46페이지 중 마지막 41~46그룹은 6개만 그려주므로 46(전체) - 40(이전페이징 그룹의 마지막 넘버)  > 6 )
		// ㅎㅎ..
		var limite = ((pageNum - idx) > 10 ? 10 : (pageNum - idx));
		// ...지금이 제일 마지막 그룹인데 또 그려줄라구 ?ㅎㅎㅎ
		setTimeout(function(){
			// 기존 페이징 삭제
			$('.pagingBtn[num='+Number(idx-1)+']').click();
			//$(".pagination a").not(".default").remove();
			//addPageNum()
		},25);
				
//		// 여긴 다음 페이지 구하는 거랑 다르게 첫번째 친구를 기준으로 한다
//		var idx = Number($(".pagination .pagingBtn").first().attr("num") || 1);
//		
//		var count = 0;
//		
//		// 이전 페이지는 무조껀 10개다... 상식적으로..
//		var limite = 10;
//		
//		// 이전으로 돌아가야되니..10개를 빼야지 
//		idx -= 10
//		// 1페이지 일때.. -가 되면 당연히 이상하것지..ㅎㅎ
//		if (idx > 0){
//			setTimeout(function(){
//				// 기존 페이징 삭제
//				$(".pagination a").not(".default").remove();
//				addPageNum()
//			},25);
//		}else{
//			$( ".pagination .active" ).removeClass( "active" );
//			var now = $(".pagination").data("now");
//			$(".pagination a[num='"+now+"']").addClass( "active" );
//		}
		
		// 이애만 idx 그대로 써준다.. 똑같이 하려면 11개를 빼던가...
		function addPageNum(){
			$(".pagination a").eq(-2).before("<a class='pagingBtn' href='#' num='"+(idx)+"'>"+(idx)+"</a>");
			idx++;
			count++;
			if (count < limite){
				addPageNum();
			}else{
				$( ".pagination .active" ).removeClass( "active" );
				var now = $(".pagination").data("now");
				$(".pagination a[num='"+now+"']").addClass( "active" );
			}
		}
	});

	// 제일 마지막 페이지로
	$(".pagination .absRight").click(function(){
		
		var pageNum = $(".pagination").data("pageNum") || 1;
		
		// 156/10*10 = 150이 된다..ㅎ 신기하지?
		// 결론은.. 값 보정을 위한 트릭..
		// 나머지는 다음 페이지로 이동 하는 부분이랑 또옥 같다..
		// 마지막 페이징 그룹 이전의 번호를 구해주는 수식
		var idx = Math.ceil(pageNum/10)*10 
		
		var count = 0;
		
		var limite = ((pageNum - idx) > 10 ? 10 : (pageNum - idx));
		
		setTimeout(function(){
			// 기존 페이징 삭제
			$(".pagination a").not(".default").remove();
			addPageNum()
		},25);
		
		function addPageNum(){
			$(".pagination a").eq(-2).before("<a class='pagingBtn' href='#' num='"+(idx)+"'>"+(idx)+"</a>");
			idx++;
			count++;
			if (count < limite){
				addPageNum();
			}else{
				$( ".pagination .active" ).removeClass( "active" );
				var now = $(".pagination").data("now");
				$(".pagination a[num='"+now+"']").addClass( "active" );
			}
		}
		
	});
	
	// 다음 페이징으로
	$(".pagination .right").click(function(){
		
		// 페이지의 총 갯수
		var pageNum = $(".pagination").data("pageNum") || 1;
	
		// 현재 (클릭 전에..) 페이징중 마지막 페이지 넘버
		var idx = Number($(".pagination .pagingBtn.active").last().attr("num") || 1);
		// 순회 순회
		var count = 0;
		// 만약에 제일 마지막 페이징 그룹(10개씩 묶었을 경우)일 때에는... 전체 - 이전페이징그룹의 마지막 갯수 만큼만 그려준다 
		// (ex 총 46페이지 중 마지막 41~46그룹은 6개만 그려주므로 46(전체) - 40(이전페이징 그룹의 마지막 넘버)  > 6 )
		// ㅎㅎ..
		var limite = ((pageNum - idx) > 10 ? 10 : (pageNum - idx));
		// ...지금이 제일 마지막 그룹인데 또 그려줄라구 ?ㅎㅎㅎ
		if(limite != 0){
			setTimeout(function(){
				// 기존 페이징 삭제
				$('.pagingBtn[num='+Number(idx+1)+']').click();
				//$(".pagination a").not(".default").remove();
				//addPageNum()
			},25);
		}

		function addPageNum(){
			// > >| 이거 두개 버튼이 있으므로, 뒤에서 2번째 전 애한테 페이징 붙여준당
			$(".pagination a").eq(-2).before("<a class='pagingBtn' href='#' num='"+(idx+1)+"'>"+(idx+1)+"</a>");
			idx++;
			count++;
			if (count < limite){
				addPageNum();
			// 현재 보여주고 있는 페이지가 속한 그룹 일 경우 버튼 active 모드로...
			}else{
				$( ".pagination .active" ).removeClass( "active" );
				var now = $(".pagination").data("now");
				$(".pagination a[num='"+now+"']").addClass( "active" );
			}
		}
	});
	
	ui_controller();
});