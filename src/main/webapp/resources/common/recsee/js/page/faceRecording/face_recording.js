var scriptList = new Map();
var scriptId = "";
var wavesurfer;
var scriptJsonArray = new Object();
var sendScriptSocketClient;
var isRestartFlag; 

window.onload = function() {
	wavesurfer = WaveSurfer.create({
		container: document.querySelector('#rec_wave'),
		progressColor:'#23527C',
		waveColor: '#449ed7',
		autoCenter : false,
		height:100,
		fillParent:true,
		responsive: true,
		scrollParent: true,
		normalize: true,
		skipLength: 5,
		interaction:true
//		,
//		plugins: [
//			WaveSurfer.cursor.create({
//	            showTime: true,
//	            opacity: 1,
//	            customShowTimeStyle: {
//	                'background-color': '#000',
//	                color: '#fff',
//	                padding: '2px',
//	                'font-size': '10px'
//	            }
//	        })
//        ]
	})
	
	$('#autoReadingBtn_human').click(function() {
		alert("성우클릭");
	});
	$('#autoReadingBtn_guide').click(function() {
		alert("가이드클릭");
	});
	$('#reading_exit').click(function() {
		alert("종료클릭");
	});
	

	var progress = {
		on: function(mask) {
			if($("#progress").length < 1) {
				$("html").append("<div id='progress'>")
			}
			$("#progress").css("opacity", "0");
			$("#progress").css("height", "100%");
			HoldOn.open({
					theme: "sk-circle"
				,	backgroundColor: "#FFFFFF"
			});
		}, off: function(mask) {
			HoldOn.close();
			$("#progress").remove();
		}
	}
		
	try {
		top.playerVisible(false);
	} catch (e) {

	}
	
	$("#custProduct").change(function() {
		// 판매상품 박스 채워주기
		var code = $("#custProduct option:selected").val();
		var name = $("#custProduct option:selected").text();

		if(code != "") {
			if(code == "direct") {
				layer_popup("#addProduct");
			} else {
				$("#sellProduct").val(name);		
			}
		} else {
			$("#sellProduct").val("");
		}
	})
	
	faceRecordingLoad();
	
	$("#search_script_btn").click(function() {
		var selectScript = $("#select_script").val();
		var code = $("#custProduct option:selected").val();
		if(code == "") {
			alert("고객 정보를 저장해주세요.");
			return false;
		}
		$.ajax({
			url:contextPath+"/getScriptList.do",
			data:{
				"selectScript" : selectScript,
				"productCode" : code
			},
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				$("#script_list option:gt(0)").remove("");
				if(jRes.success == "Y") {
					var result = jRes.resData.script;
					scriptJsonArray = result;
					var htmlString = "";
					if (scriptJsonArray != undefined && scriptJsonArray.length > 0) {
						for(var i = 0; i < scriptJsonArray.length; i++) {
							htmlString += "<option onclick='modifyScript(\""+scriptJsonArray[i].scriptCode+"\")' value='"+scriptJsonArray[i].scriptCode+"'>"+scriptJsonArray[i].scriptName+"</option>";						
						}
						htmlString += "<option onclick='modifyScript(\""+''+"\")' value=''>스크립트 추가</option>";
					}
					
					// 상품에 대한 스크립트 리스트 가져오기
					$("#script_list").append(htmlString);
				}
			}
		});
		
	})
	
	$("#cust_info_save_btn").click(function() {
		var custName = $("#custName").val();
		if(custName == undefined || custName == "") {
			alert("고객명을 입력해주세요.");
			return false;
		}
		var custNum = $("#custNum").val();
		if(custNum == undefined || custNum == "") {
			alert("고객번호을 입력해주세요.");
			return false;
		}
		var custPhone = $("#custPhone").val();
		if(custPhone == undefined || custPhone == "") {
			alert("전화번호을 입력해주세요.");
			return false;
		}
		var code = $("#custProduct option:selected").val();
		if(code == undefined || code == "") {
			alert("가입 상품을 선택해주세요.");
			return false;
		}
		
		var dataStr = {
			"custName" : custName,
			"custNum" : custNum,
			"custPhone" : custPhone,
			"productCode" : code
		}
		
		$.ajax({
			url:contextPath+"/getScriptList.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				sessionStorage.setItem("custName",custName);
				sessionStorage.setItem("custNum",custNum);
				sessionStorage.setItem("custPhone",custPhone);
				sessionStorage.setItem("productCode",code);
				$("#search_script_btn").trigger("click");
			}
		});
	})
	
	$("#productType").change(function() {
		var type = $("#productType option:selected").val();
		
		if (type == "direct") {
			$("#inputDirect").show();
		} else {
			$("#inputDirect").hide();
		}
	})
	
	$("#select_script_btn").click(function() {
		var selectedScriptCode = "";
		$("#script_list option:selected").each(function() {
			if($(this).val() != "" && $("#faceRecMenu ul").find("#"+$(this).val()).length == 0) {
			    $("#faceRecMenu ul").append("<li class='addedScript' onclick='showScript(\""+$(this).val()+"\")' id='"+$(this).val()+"'><input type='checkbox' class='chkImg' disabled='disabled'/>"+$(this).text()+"</li>");
			    selectedScriptCode += ","+$(this).val();
			}
		});
		
		if (selectedScriptCode != "") {
			$.ajax({
				url:contextPath+"/getScriptList.do",
				data:{"scriptCode":selectedScriptCode.substring(1)},
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						var result = jRes.resData.script;
						
						for (var i = 0; i < result.length; i++) {
						    var jsonData = result[i];
							scriptList[jsonData.scriptCode] = jsonData.script;
						}
					}
				}
			});
		}
	})
	
	$(".chkIcon").change(function(){
        if($(".chkIcon").is(":checked")){
            $("#"+scriptId+" .chkImg").prop('checked',true);
        }else{
            $("#"+scriptId+" .chkImg").prop('checked',false);
        }
    });
    
    $("#play_btn").click(function() {
    	if(wavesurfer.isReady == true) {
			if($("#play_btn").hasClass("play")) {
				sendScriptSocketClient.Socket.send($(".rec_script").text());
		    	wavesurfer.play();
				$("#play_btn").removeClass("play");
				$("#play_btn").addClass("pause");
				
				
				var totalTime = wavesurfer.getDuration();
			
				if(totalTime > 30) {
					var sendUrl ="http://192.168.153.36:7272/PcmReceiver?type=stop";
					$.ajax({
						url:sendUrl,
						type:"GET",
						timeout:5000,
						async: false,
						success:function(result){
							isRestartFlag = true;
						}, error:function(reason) {
							
						}
					});
				}
			} else {
				wavesurfer.pause();
				$("#play_btn").removeClass("pause");
				$("#play_btn").addClass("play");
			}
			
			
		}
    })

    $("#pre_btn").click(function() {
    	wavesurfer.skip(-10);
    })

    $("#next_btn").click(function() {
    	wavesurfer.skip(10);
    })

	wavesurfer.on('ready', function () {
	    $("#total_time").text(seconds2time(wavesurfer.getDuration()));
	    $("#pre_btn").removeClass("pre_btn_unload");
	    $("#pre_btn").addClass("pre_btn_load");
	    $("#next_btn").removeClass("next_btn_unload");
	    $("#next_btn").addClass("next_btn_load");
	});
	
	wavesurfer.on('destroy', function () {
	    $("#pre_btn").removeClass("pre_btn_load");
	    $("#pre_btn").addClass("pre_btn_unload");
	    $("#next_btn").removeClass("next_btn_load");
	    $("#next_btn").addClass("next_btn_unload");
	    $("#play_btn").removeClass("pause");
	    $("#play_btn").addClass("play");
	});
 
 	wavesurfer.on('finish', function () {
	    $("#play_btn").removeClass("pause");
	    $("#play_btn").addClass("play");
	    $("#now_time").text("00:00:00");
	});
 	 
 
	wavesurfer.on('audioprocess', function () {
		var totalTime = wavesurfer.getDuration();
		if (totalTime > 30 && totalTime - wavesurfer.getCurrentTime() <= 5) {
			if(isRestartFlag) {
				isRestartFlag = false;
				var sendUrl ="http://192.168.153.36:7272/PcmReceiver?type=restart";
				$.ajax({
					url:sendUrl,
					type:"GET",
					timeout:5000,
					async: false,
					success:function(result){
					
					}, error:function(reason) {
						
					}
				});
			}
		}
		
		
	    $("#now_time").text(seconds2time(wavesurfer.getCurrentTime()));
	});


	$("#upLoadFile_hidden1").change(function(){
		var inputFile = $("#upLoadFile_hidden1");
		var files = inputFile[0].files;
		
		var textFileName = files[0].name;
		$("#upload_name1").val(textFileName);
		
		processFile(files[0]);
	});
	
	
	$("#add_script_btn").click(function() {
		// mp3/audio file
		var formData = new FormData();
		var inputFile = $("#upLoadFile_hidden1");
		var files = inputFile[0].files;
		
		for (var i = 0; i < files.length; i++) {
			formData.append("uploadFile", files[i]);
			var textFileNames = files[i].name.split('.');
			var textFileNamesChk = textFileNames[textFileNames.length-1];
			if(!(textFileNamesChk.toLowerCase()=='txt')){
				alert(lang.views.transcript.alert.msg3/*"only txt"*/);
			}
		}
		
		var scriptCode = $("#scriptCode").val();
		if (scriptCode != undefined && scriptCode != "") {
			formData.append("scriptCode", scriptCode);
			formData.append("type", "update");
		} else {
			formData.append("type", "insert");
		}
		
		var scriptName = $("#scriptName").val();
		if (scriptName == undefined || scriptName == "") {
			alert("스크립트 명을 입력해주세요.");
			return false;
		}
		var scriptType = $("#scriptType option:selected").val();
		if (scriptType == undefined || scriptType == "") {
			alert("스크립트 타입을 선택해주세요.");
			return false;
		}
		
		for(var i = 0; i < scriptJsonArray.length; i++) {
			if(scriptJsonArray[i].scriptCode != scriptCode && scriptJsonArray[i].scriptName == scriptName && scriptJsonArray[i].scriptType == scriptType) {
				alert("현재 선택된 분류에 이미 등록된 스크립트명 입니다.");
				return false;
			}
		}
		
		var script = $("#script").val();
		
		if ((script == undefined || script == "") && (files.length < 1)) {
			alert("스크립트 파일 업로드 또는 내용을 직접 입력해주세요.");
			return false;
		}
		
		formData.append("scriptName", scriptName);
		formData.append("scriptType", scriptType);
		formData.append("script", script);
		formData.append("productType", $("#custProduct option:selected").val());
		
		$.ajax({
			url:contextPath+"/addScriptInfo.do",
			data:formData,
			dataType:"json",
			contentType: false,
			processData: false,
			type:"POST",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					$("#scriptName").val("");
					$("#scriptType").val("");
					$("#upload_name1").val("파일을 선택하세요.")
					$("#upLoadFile_hidden1").val("");
					$("#script").val("");
					
					if(formData.get("type") == "insert") {
						alert("스크립트가 추가되었습니다.");
					} else {
						alert("스크립트가 수정되었습니다.");
					}
					
					$("#search_script_btn").trigger("click");
				} else {
					alert("스크립트가 추가에 실패하였습니다.");
				}
			}
		});
	})
	
	$("#productAddBtn").click(function() {
		var dataStr = {
			"productName" : $("#productName").val(),
			"productType" : $("#productType").val()
		}
		$.ajax({
			url:contextPath+"/insertFaceRecProductInfo.do",
			data:dataStr,
			type:"POST",
			async: false,
			success:function(result){
				if(result.success="Y") {
					alert("상품이 추가되었습니다.");
					layer_popup_close();
					getProductList();
				} else {
					alert("상품이 추가에 실패하였습니다.");
				}
			}, error:function(reason) {
				if(reason == null || reason == undefined) {
					alert("상품이 추가에 실패하였습니다.");
				} else {
					alert("상품이 추가에 실패하였습니다.");
				}
			}
		})
	})
	ui_controller();
	sendScriptSocketClient = new WebSocketCallBack('ws://localhost:9988', OnSendScriptSocketError, OnSendScriptSocketConnect, OnSendScriptSocketDataReady, OnSendScriptSocketDisconnect);  
}
function modifyScript(id) {
	if (id == "") {
		$("#scriptName").val("");
		$("#scriptType").val("");
		//$("#upload_name1").val(scriptJsonArray[i].scriptName);
		$("#script").val("");
		$("#scriptCode").val("");
	} else {
		if (scriptJsonArray != undefined && scriptJsonArray.length > 0) {
			var obj = getListFilter(scriptJsonArray, "scriptCode",id)
			$("#scriptName").val(obj[0].scriptName);
			$("#scriptType").val(obj[0].scriptType);
			//$("#upload_name1").val(scriptJsonArray[i].scriptName);
			$("#script").val(obj[0].script);
			$("#scriptCode").val(id);
		}		
	}
}


// 콜백 함수
function OnSendScriptSocketError(error){
   console.log(error);
}

function OnSendScriptSocketConnect(){
	console.log("sendScriptSocketClient success");
}

function OnSendScriptSocketDataReady(data){
	console.log(data);
}

function OnSendScriptSocketDisconnect(){
   console.log("소켓이 끊어짐");
}


function processFile(file) {
    var reader = new FileReader();
    reader.onload = function () {
        $("#script").val(reader.result);
    };
    reader.readAsText(file, "UTF-8");
}

function showScript(id) {
	scriptId = id;
	var obj = getListFilter(scriptJsonArray, "scriptCode",id);
	if (id == "script_menu") {
		scriptId = "";
		$("#rec_script_box").hide();
		$("#select_script_box").show();
		$("#script_info_box").show();
	} else {
		$(".rec_script_title").text($("#"+id).text());
		$(".rec_script").text(obj[0].script);
		
		var isChecked = $("#"+scriptId+" .chkImg").prop('checked');
		if (isChecked == true) {
			$(".chkIcon").prop('checked', true);
		} else {
			$(".chkIcon").prop('checked', false);
		}
		$("#rec_script_box").show();
		$("#select_script_box").hide();
		$("#script_info_box").hide();	
		
	    $("#play_btn").removeClass("pause");
	    $("#play_btn").addClass("play");
		
		if (obj[0].audioFilePath != undefined && obj[0].audioFilePath != "") {
			wavesurfer.load(obj[0].audioFilePath);
		} else {
			progress.on();
			var dataStr = {
				"scriptCode" : scriptId,
				"productType" : $("#custProduct option:selected").val()
			}
			
			$.ajax({
				url:contextPath+"/playScriptTTS.do",
				data : dataStr,
				type:"POST",
				dataType:"json",
				timeout: 5000,
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						var result = jRes.resData.listenUrl;
		                wavesurfer.load(result);
					} else {
						wavesurfer.empty();
						$("#total_time").text("00:00:00");
						$("#now_time").text("00:00:00");
						alert("스크립트 오디오 파일을 찾을 수 없습니다.");
					}
					progress.off();
				},
				error : function(error) {
					progress.off();
					wavesurfer.empty();
					$("#total_time").text("00:00:00");
					$("#now_time").text("00:00:00");
					alert("스크립트 오디오 파일을 찾을 수 없습니다.");
			    }
			});
		}
	}
}
function faceRecordingLoad() {
	getProductList();
}

function getProductList() {
	$.ajax({
		url:contextPath+"/getProductList.do",
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				$("#custProduct").text("");
				var result = jRes.resData.product;
				var htmlString = "<option value='' id=''>가입 상품 선택</option>";
				for(var i = 0; i < result.length; i++) {
					htmlString += "<option value='"+result[i].productCode+"'>"+result[i].productName+"</option>";
				}
				htmlString += "<option value='direct' id='direct'>추가</option>";
				$("#custProduct").append(htmlString);
			}
		}
	});
}

function getListFilter(jsonArray, key,value) {
	return jsonArray.filter (function(object) {
		return object[key] === value;
	});
}

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