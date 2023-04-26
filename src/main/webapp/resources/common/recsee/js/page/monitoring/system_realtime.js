var sysInfo; // system 정보 변수 선언 (서버별 모니터링 데이터)
var realtimeSysInfo; // system 정보 변수 선언 (등록된 서버 정보)
var modeCombo; // 보기모드 선택 콤보박스 변수 선언
var context=new Array(); // cubism 차트 객체 변수 선언 (cubism)
var cpu=new Array(), memory=new Array(); // metrics data 객체
var cpuInit=new Array(), memoryInit=new Array(); // metrics data 초기화 여부 확인 객체
var cpuChart=new Array(), memoryChart=new Array(), diskChart=new Array(); // 시스템별 차드 객체 변수 선언 (liquid fill / circular gauge)
var dataInfo=new Array(); // data(숫자) form 객체 선언
var cpuLimit;
var memoryLimit;
var diskLimit; // 임계점

// 레디스 데이터 n초마다 가져오기
setInterval(function() {
	getSysRedis();
	if(modeCombo.getSelectedValue() == 1){ // 콜보박스가 liquid 일때 value 갱신 (적용 x)
		sysInfo.forEach(function(val,idx,arr){
			cpuChart[idx].update(Math.round(val[val.length-1].cpuUsage*1000)/10.0);
			memoryChart[idx].update(Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0);
			diskChart[idx].update(Math.round((val[val.length-1].diskTotal-val[val.length-1].diskFree)/val[val.length-1].diskTotal*1000)/10.0);
		});
	}else if(modeCombo.getSelectedValue() == 2){ // 콜보박스가 circular 일때 value 갱신 (적용 x)
		sysInfo.forEach(function(val,idx,arr){
			cpuChart[idx].option("value", Math.round(val[val.length-1].cpuUsage*1000)/10.0);
			memoryChart[idx].option("value", Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0);
			diskChart[idx].option("value", Math.round((val[val.length-1].diskTotal-val[val.length-1].diskFree)/val[val.length-1].diskTotal*1000)/10.0);
		});
	}else{
		sysInfo.forEach(function(val,idx,arr){
			// cpu linear gauge update
			cpuChart[idx].value(Math.round(val[val.length-1].cpuUsage*1000)/10.0);
			// 임계치 이상이면 title 붉은색
			if(Math.round(val[val.length-1].cpuUsage*1000)/10.0 > cpuLimit){
				cpuChart[idx].option("title",{text:"CPU("+Math.round(val[val.length-1].cpuUsage*1000)/10.0+"%)",font:{size:15, color:"red"}});
			}else{
				cpuChart[idx].option("title",{text:"CPU("+Math.round(val[val.length-1].cpuUsage*1000)/10.0+"%)",font:{size:15, color:"black"}});
			}
			
			// memory linear gauge update
			memoryChart[idx].value(Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0);
			// 임계치 이상이면 title 붉은색
			if(Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0 > memoryLimit){
				memoryChart[idx].option("title",{text:"Memory("+Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0+"%)",font:{size:15, color:"red"}});
			}else{
				memoryChart[idx].option("title",{text:"Memory("+Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0+"%)",font:{size:15, color:"black"}});
			}
			
			// data form cpu값 update
			dataInfo[idx].setItemValue("cpuUsage",Math.round(val[val.length-1].cpuUsage*1000)/10.0+"%");
			// data form의 cpu 임계치 이상이면 값 붉은색
			if(Math.round(val[val.length-1].cpuUsage*1000)/10.0>cpuLimit){
				$("#"+dataInfo[idx].getInput("cpuUsage").id).css("color","red");
			}else{
				$("#"+dataInfo[idx].getInput("cpuUsage").id).css("color","black");
			}
			// data form memory값 update 
			dataInfo[idx].setItemValue("memoryTotal",Math.round(val[val.length-1].memoryTotal/1024/1024/10.24)/100.0+"GB");
			dataInfo[idx].setItemValue("memoryUsage",Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/1024/1024/10.24)/100.0+"GB");
			// data form의 cpu 임계치 이상이면 값 붉은색
			if(Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0>memoryLimit){
				$("#"+dataInfo[idx].getInput("memoryUsage").id).css("color","red");
			} else {
				$("#"+dataInfo[idx].getInput("memoryUsage").id).css("color","black");
			}
			dataInfo[idx].setItemValue("memoryFree",Math.round(val[val.length-1].memoryFree/1024/1024/10.24)/100.0+"GB");
		});
	}
}, 10000); // 1000 ms = 1s

window.onload = function() {
	getLimitValue();
	$("#btnLimitSetOpen").click(function() {
		layer_popup("#setLimitPopup");
	});
	
	$("#setLimitBtn").click(function() {
		setLimit();
	});
	
	$("#setSysRedisIPBtn").click(function() {
		setSysRedisGroupCode();
		location.reload();
	});
	
	addCombo(); // 콤보박스 생성 (적용 x)
	getRealtimeSetting(); // 등록된 시스템 모니터링 서버 정보들 가져오기 (서버명 표시해주려고)
	getSysRedis(); // 레디스에서 system 데이터 가져오기
	
	// system 데이터 length 만큼 div 추가
	sysInfo.forEach(function(val,idx,arr){
		$(".chartWrap").append("<div class='sysInfoWrap' id='"+val[val.length-1].sysIp.replace(/\./gi,"")+"'></div>"); // id에 .(점)을 쓸 수 없어 replace
	});
	loadChart(); // 차트 불러오기 
};

function setSysRedisGroupCode() {
	$.ajax({
		url : contextPath + "/monitoring/setSysRedisGroupCode",
		dataType : "json",
		data : {
			"sysRedisGroupCode" : $("#sysRedisIP").val()
		},
		async: false,
		success : function(jRes){
			if(jRes.success=="Y"){
				redisIp = jRes.resData.redisIp;
			} else {
				alert("조회 데이터가 없습니다.");
			}
		}
	});
}

function setLimit() {
	var cpu = $("#cpuLimit").val();
	if (!limitValueChk(cpu)) {
		$("#cpuLimit").val("");
		$("#cpuLimit").focus();
		return false;
	} else {
		if(cpu.split('.').length == 1) {
			cpu = cpu + ".0";
		} else {
			if (cpu.split('.')[1].length > 1) {
				cpu = cpu.toFixed(1);
			}
		}
	}
	
	var memory = $("#memoryLimit").val();
	if (!limitValueChk(memory)) {
		$("#memoryLimit").val("");
		$("#memoryLimit").focus();
		return false;
	} else {
		if(memory.split('.').length == 1) {
			memory = memory + ".0";
		} else {
			if (cpu.split('.')[1].length > 1) {
				memory = memory.toFixed(1);
			}
		}
	}
	
	var disk = $("#diskLimit").val();
	if (!limitValueChk(disk)) {
		$("#diskLimit").val("");
		$("#diskLimit").focus();
		return false;
	} else {
		if(disk.split('.').length == 1) {
			disk = disk + ".0";
		} else {
			if (cpu.split('.')[1].length > 1) {
				disk = disk.toFixed(1);
			}
		}
	}
	
	$.ajax({
		url : contextPath + "/monitoring/setLimitValue",
		dataType : "json",
		data : {
			"cpu" : cpu,
			"memory" : memory,
			"disk" : disk
		},
		async: false,
		success : function(jRes){
			if(jRes.success=="Y"){
				alert("임계치가 설정되었습니다.");
			} else {
				alert("임계치가 설정에 실패하였습니다.");
			}
			layer_popup_close();
		}
	});
}

function limitValueChk(val) {
	 var valSplit = val.split('.');
	 if(valSplit[0].length > 3) {
		 alert("잘못된 수치입니다.");
		 return false;
	 } else if(valSplit[0] > 100){
		 alert("잘못된 수치입니다. 100.0 이하의 값을 입력해주세요.");
		 return false;
	 }
     return true;
}

// cpu, memory, disk 임계점 데이터 가져오기
function getLimitValue(){
	$.ajax({
		url : contextPath + "/monitoring/getLimitValue",
		dataType : "json",
		async: false,
		success : function(result){
			cpuLimit = result[0];
			$("#cpuLimit").val(cpuLimit);
			memoryLimit = result[1];
			$("#memoryLimit").val(memoryLimit);
			diskLimit = result[2];
			$("#diskLimit").val(diskLimit);
		}
	});
}

function getRealtimeSetting(){
	$.ajax({
		url : contextPath + "/monitoring/getRealtimeSetting",
		dataType : "json",
		async: false,
		success : function(result){
			realtimeSysInfo = result;
		}
	});
}

 // 콤보박스 정의 (적용 x)
function addCombo(){
	modeCombo = new dhtmlXCombo({
		parent: "mode",
		width: 230,
		filter: true,
		name: "mode",
	});
	
	// 콤보박스 항목 추가
	modeCombo.addOption("0","cubismChart", null, null, true); // selected
	modeCombo.addOption("1","liquidFillGauge");
	modeCombo.addOption("2","circularGauge");
	
	$.ajax({
		url : contextPath + "/monitoring/getSysRedisGroupCode",
		dataType : "json",
		async: false,
		success : function(result){
			var selectedVal = result[result.length-1];
			for(var i = 0; i < result.length-1; i++) {
				if (result[i] == selectedVal) {
					$("#sysRedisIP").append("<option value='"+result[i]+"' selected>"+result[i]+"</option>");
				} else {
					$("#sysRedisIP").append("<option value='"+result[i]+"'>"+result[i]+"</option>");
				}
			}
		}
	});
}

// 레디스 데이터 가져오기
function getSysRedis(){
	$.ajax({
		url : contextPath + "/monitoring/getSysRedis",
		data : {
			"redisIp" : redisIp,
			"redisPort" : redisPort,
			"redisPw" : redisPw
		},
		dataType : "json",
		async: false,
		success : function(result){
			sysInfo = result;
		}
	});
}

// 차트 불러오기
function loadChart(){
	// liquid 일 때 (적용 x)
	if(modeCombo.getSelectedValue() == 1){
		sysInfo.forEach(function(val,idx,arr){
			$("#"+val[val.length-1].sysIp.replace(/\./gi,"")).empty();
			$("#"+val[val.length-1].sysIp.replace(/\./gi,"")).append("<svg class='chartWrap_oneThird' id='cpuInfo"+idx+"'></svg>" +
					"<svg class='chartWrap_oneThird' id='memoryInfo"+idx+"'></svg>" +
					"<svg class='chartWrap_oneThird' id='diskInfo"+idx+"'></svg><div class='chartWrap_server'>"+val[val.length-1].sysIp+"</div>");
		});
		liquidFillGauge();
	// circular 일 때 (적용 x)
	}else if(modeCombo.getSelectedValue() == 2){
		sysInfo.forEach(function(val,idx,arr){
			$("#"+val[val.length-1].sysIp.replace(/\./gi,"")).empty();
			$("#"+val[val.length-1].sysIp.replace(/\./gi,"")).append("<div class='chartWrap_oneThird' id='cpuInfo"+idx+"'></div>" +
				"<div class='chartWrap_oneThird' id='memoryInfo"+idx+"'></div>" +
				"<div class='chartWrap_oneThird' id='diskInfo"+idx+"'></div><div class='chartWrap_server'>"+val[val.length-1].sysIp+"</div>");
		});
		circularGauge();
	// cubism 일 때 : default
	}else{
		/* 구조 : 위에서 sysIp를 id로 생성한 div 내부 
		 * ---id=sysIp-----------------------------------------------------------------------
		 * │ --cW_1st----  --cW_2nd------  ---cW_3rd------------------------  --cW_4th----  │
		 * ││ -dataInfo- ││ --cpuInfo--- ││ ---cubism---------------------- ││ -diskInfo- │ │
		 * │││          ││││            ││││                               ││││          ││ │
		 * │││          ││││            ││││                               ││││          ││ │
		 * │││          ││││-memoryInfo-││││                               ││││          ││ │
		 * │││          ││││            ││││                               ││││          ││ │
		 * │││          ││││            ││││                               ││││          ││ │
		 * ││ ---------- ││ ------------ ││ ------------------------------- ││ ---------- │ │
		 * │ ------------  --------------  ---------------------------------  ------------  │
		 * ----------------------------------------------------------------------------------
		 */
		sysInfo.forEach(function(val,idx,arr){ //<div class='chartWrap_cubism' id='server"+index+"'></div>
			$("#"+val[val.length-1].sysIp.replace(/\./gi,"")).empty();
			$("#"+val[val.length-1].sysIp.replace(/\./gi,"")).append("<div class='chartWrap_1st'><div id='dataInfo"+idx+"'></div></div>" +
					"<div class='chartWrap_2nd'><div class='chartWrap_linear' id='cpuInfo"+idx+"'></div><div class='chartWrap_linear' id='memoryInfo"+idx+"'></div></div>" +
					"<div class='chartWrap_3rd'><div class='chartWrap_cubism' id='cubism"+idx+"'></div></div>" +
					"<div class='chartWrap_4th'><div class='chartWrap_cubism' id='diskInfo"+idx+"'></div>");
			
		});
		loadDataForm();
		linearGauge();
		cubismChart();
		doughnutChart();
	}
}

// cubism metrics 데이터 정의
function metric(name, index) {
	  return context[index].metric(function(start, stop, step, callback) {
		   d3.json(contextPath + "/monitoring/getSysRedis?redisIp="+redisIp+"&redisPort="+redisPort+"&redisPw="+redisPw+"&redisTimeout="+redisTimeout, function(result) {
			   var values = [];
			   // 데이터 load 이후 update 시
			   if(name=='CPU' && cpuInit[index]){
				   for (var i = 8; i > 0; i--) { // length-8 부터 length-1 : 최신 8개 데이터, 과거 -> 현재 순으로 push
					// 최신 데이터 1개씩 update하려고 했지만, metrics 데이터가 포인트를 생성해서 update하는 방식이라고 함.(정확한 기준 잘 모르겠음)
					// 데이터 배열의 길이가 8개일 때 최신값을 뿌려서 8로 지정 
					values.push(Math.round(result[index][result[index].length-i].cpuUsage*1000)/10.0);
				   }
			   // 데이터 load 이후 update 시
			   }else if(name=='Memory' && memoryInit[index]){ // 상동
				   for (var i = 8; i > 0; i--) {
					   values.push(Math.round((result[index][result[index].length-i].memoryTotal-result[index][result[index].length-i].memoryFree)/result[index][result[index].length-i].memoryTotal*1000)/10.0);
				   }
			   // 최초 metrics 데이터 load 시
			   }else if(name=='CPU' && cpuInit[index] == undefined){ // getSysRedis 경로에서 받아온 전체 배열 push
				   result[index].forEach(function(val,idx,arr){
					   values.push(Math.round(val.cpuUsage*1000)/10.0);
				   });
				   cpuInit[index] = values.slice(-context[index].size()); // 데이터 로드여부 update
				   values = values.slice(-context[index].size());
				   if (context[index].size() > values.length) {
					   var length = context[index].size() - values.length;
					   var tempArr = new Array(length).fill(0);
					   values = tempArr.concat(values);
				   }
		       // 최초 metrics 데이터 load 시
			   }else if(name=='Memory' && memoryInit[index] == undefined){
				   result[index].forEach(function(val,idx,arr){
					   values.push(Math.round((val.memoryTotal-val.memoryFree)/val.memoryTotal*1000)/10.0);
				   });
				   memoryInit[index] = values.slice(-context[index].size());
				   values = values.slice(-context[index].size());
				   if (context[index].size() > values.length) {
					   var length = context[index].size() - values.length;
					   var tempArr = new Array(length).fill(0);
					   values = tempArr.concat(values);
				   }
			   }
			    // context 크기(810)로 자름 (마이너스는 맨뒤에서 부터 810번째앞까지)
		   		callback(null, values.slice(-context[index].size()));
		   }); 
	  }, name); 
}

function temp() {
	sysInfo.forEach(function(val,idx,arr){
		if(name=='CPU' && cpuInit[index] == undefined){ // getSysRedis 경로에서 받아온 전체 배열 push
		   result[index].forEach(function(val,idx,arr){
			   values.push(Math.round(val.cpuUsage*1000)/10.0);
		   });
		   cpuInit[index] = values.slice(-context[index].size()); // 데이터 로드여부 update
       // 최초 metrics 데이터 load 시
	   }else if(name=='Memory' && memoryInit[index] == undefined){
		   result[index].forEach(function(val,idx,arr){
			   values.push(Math.round((val.memoryTotal-val.memoryFree)/val.memoryTotal*1000)/10.0);
		   });
		   memoryInit[index] = values.slice(-context[index].size());
	   }
	
		// cubism context 정의
		cpu[idx] = metric("CPU", idx); // metrics 데이터 생성
		memory[idx] = metric("Memory", idx);
	});
		
}
// cubism 차트 정의
function cubismChart(){
	sysInfo.forEach(function(val,idx,arr){
		// cubism context 정의
		context[idx] = cubism.context().serverDelay(0).clientDelay(0).step(3000) // 1초에 한번씩 (1의 3승, 단위 ms)
		.size(+d3.select("#cubism"+idx).style("width").slice(0,-2)/*810*/);
		
		cpu[idx] = metric("CPU", idx); // metrics 데이터 생성
		memory[idx] = metric("Memory", idx);
		
		d3.select("#cubism"+idx).call(function(div) {
			
			div.append("div")
			.attr("class", "axis")
			.call(context[idx].axis().orient("top").ticks(10));
			
			div.selectAll(".horizon")
			.data([cpu[idx], memory[idx]])// 데이터
			.enter().append("div")
			.attr("class", "horizon")
			.call(context[idx].horizon().height(90/*context 크기*/).colors(["#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#BAE4B3","#74C476","#4C9A64","#FF8468"]/*4밴드 -n~n*/).extent([0, 100]/*세로축 범위*/));
			
			div.append("div")
			.attr("class", "rule")
			.call(context[idx].rule());
		});
		context[idx].on("focus", function(i) { // 마우스 포커싱 기능 정의
			d3.selectAll(".value").style("right", i == null ? null : context[idx].size() - i + "px");
		});
	});
}

// linear게이지 정의
function linearGauge(){
	sysInfo.forEach(function(val,idx,arr){
		var cpuPer = Math.round(val[val.length-1].cpuUsage*1000)/10.0;
		var cpuTitleColor = "black";
		// 임계치 이상 넘어가면 붉은색 표기
		if(cpuPer > cpuLimit){
			cpuTitleColor = "red";
		}
		var memoryPer = Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0;
		var memoryTitleColor = "black";
		if(memoryPer > memoryLimit){
			memoryTitleColor = "red";
		}
		// cpu linear 게이지 정의
		cpuChart[idx] = $("#cpuInfo"+idx).dxLinearGauge({
			 geometry: { orientation: "vertical" },// 세로 bar
			scale: {
				startValue: 0,
				endValue: 100,
				tickInterval: 5,
				color : "#74c476",
				tick: {
					color: "#536878"
				},
				label: {
					indentFromTick: -3
				}
			},
			rangeContainer: {
				offset: 10,
				ranges: [ // 범위 색 지정
					{ startValue: 0, endValue: 25, color: "#BAE4B3" },
					{ startValue: 25, endValue: 50, color: "#74C476" },
					{ startValue: 50, endValue: 75, color: "#4C9A64" },
					{ startValue: 75, endValue: 100, color: "#FF8468" }
					]
			},
			valueIndicator: {
				offset: 20
			},
			title: {
				text: "CPU("+cpuPer+"%)",
				font: { size: 15, color:cpuTitleColor }
			},
			value: cpuPer
		}).dxLinearGauge("instance");
		// memory linear 게이지 정의
		memoryChart[idx] = $("#memoryInfo"+idx).dxLinearGauge({
			 geometry: { orientation: "vertical" },
			scale: {
				startValue: 0, 
				endValue: 100,
				tickInterval: 5,
				color : "#74c476",
				tick: {
					color: "#536878"
				},
				label: {
					indentFromTick: -3
				}
			},
			rangeContainer: {
				offset: 10,
				ranges: [
					{ startValue: 0, endValue: 25, color: "#BAE4B3" },
					{ startValue: 25, endValue: 50, color: "#74C476" },
					{ startValue: 50, endValue: 75, color: "#4C9A64" },
					{ startValue: 75, endValue: 100, color: "#FF8468" }
					]
			},
			valueIndicator: {
				offset: 20
			},
			title: {
				text: "Memory("+memoryPer+"%)",
				font: { size: 15, color : memoryTitleColor }
			},
			value: memoryPer
		}).dxLinearGauge("instance");
	});
}

// 도넛차트 정의
function doughnutChart(){
	sysInfo.forEach(function(val,idx,arr){
		var diskTotal = val[val.length-1].diskTotal;
		var per = Math.round((val[val.length-1].diskTotal-val[val.length-1].diskFree)/val[val.length-1].diskTotal*1000)/10.0;
		var titleColor = "black";
		if(per>diskLimit){
			titleColor = "red";
		}
		// disk 도넛 차트 정의
		diskChart[idx] = $("#diskInfo"+idx).dxPieChart({
			type: "doughnut",
			palette: ["#2A89D0","#F54D69"], // 차트 색
			title: {
				text:"Disk("+per+"%)",
				font : {color : titleColor}
				},
			dataSource: [{agument:'free', percentage:val[val.length-1].diskFree},{agument:'usage', percentage:val[val.length-1].diskTotal-val[val.length-1].diskFree}],
			legend: {
				horizontalAlignment: "center",
				verticalAlignment: "bottom"
			},
			tooltip: {
				enabled: true,
				format: "percentage",
				customizeTooltip: function() {
					return { text: this.argumentText + "<br>" + Math.round(this.value/1024/1024/10.24)/100.0 + "GB" + "<br>" + Math.round((diskTotal-this.value)/diskTotal*1000)/10.0 + "%" };
				}
			},
			series: [{
				argumentField: "agument",
				valueField: "percentage"
					
			}]
		});
	});
}

// 데이터폼 정의
function loadDataForm(){
	sysInfo.forEach(function(val,idx,arr){
		var ip = val[val.length-1].sysIp.replaceAll("\.","");
		var sysName = realtimeSysInfo[ip];
		if (sysName == undefined) {
			sysName = val[val.length-1].sysIp;
		} else {
			sysName = sysName+"("+val[val.length-1].sysIp+")";
		}
		// 데이터 폼 형식 정의
		var dataForm = [
			{type:"settings", position:"label-top"},
			{type: "fieldset",name:"dataInfo", fontSize: 15, label: sysName, list:[
				{type:"input", name:"cpuUsage", label: "CPU Usage", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true},
				{type:"input", name:"memoryTotal", label: "Memory Total", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true},
				{type:"input", name:"memoryUsage", label: "Memory Usage", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true},
				{type:"input", name:"memoryFree", label: "Memory Free", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true},
				{type:"newcolumn", offset:20},
				{type:"input", name:"diskTotal", label: "Disk Total (" +val[val.length-1].sysPath+ ")", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true},
				{type:"input", name:"diskUsage", label: "Disk Usage", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true},
				{type:"input", name:"diskFree", label: "Disk Free", labelWidth:80, inputWidth:80, inputHeight:9, readonly:true}
				]}
			];
		// 폼 객체 생성
		dataInfo[idx] = new dhtmlXForm("dataInfo"+idx,dataForm);
		// field set 글자 크기 
		$("#dataInfo"+idx).find(".fs_legend").css("font-size","18px");
		// cpu, memory 값은 interval에서 넣어주는 방식과 동일
		dataInfo[idx].setItemValue("cpuUsage",Math.round(val[val.length-1].cpuUsage*1000)/10.0+"%");
		if(Math.round(val[val.length-1].cpuUsage*1000)/10.0>cpuLimit){
			$("#"+dataInfo[idx].getInput("cpuUsage").id).css("color","red");
		}	
		dataInfo[idx].setItemValue("memoryTotal",Math.round(val[val.length-1].memoryTotal/1024/1024/10.24)/100.0+"GB");
		dataInfo[idx].setItemValue("memoryUsage",Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/1024/1024/10.24)/100.0+"GB");
		if(Math.round((val[val.length-1].memoryTotal-val[val.length-1].memoryFree)/val[val.length-1].memoryTotal*1000)/10.0>memoryLimit){
			$("#"+dataInfo[idx].getInput("memoryUsage").id).css("color","red");
		}	
		dataInfo[idx].setItemValue("memoryFree",Math.round(val[val.length-1].memoryFree/1024/1024/10.24)/100.0+"GB");
		// disk는 실시간 반영 x
		dataInfo[idx].setItemValue("diskTotal",Math.round(val[val.length-1].diskTotal/1024/1024/10.24)/100.0+"GB");
		dataInfo[idx].setItemValue("diskUsage",Math.round((val[val.length-1].diskTotal-val[val.length-1].diskFree)/1024/1024/10.24)/100.0+"GB");
		// 임계치 이상이면 붉은색 title
		if(Math.round((val[val.length-1].diskTotal-val[val.length-1].diskFree)/val[val.length-1].diskTotal*1000)/10.0>diskLimit){
			$("#"+dataInfo[idx].getInput("diskUsage").id).css("color","red");
		}	
		dataInfo[idx].setItemValue("diskFree",Math.round(val[val.length-1].diskFree/1024/1024/10.24)/100.0+"GB");
	});
	
}

// 적용 x
function liquidFillGauge() {
	var cpuConfig=liquidFillGaugeDefaultSettings(), memoryConfig=liquidFillGaugeDefaultSettings(), diskConfig=liquidFillGaugeDefaultSettings();
	memoryConfig.circleColor = "#808015";
	memoryConfig.textColor = "#555500";
	memoryConfig.waveTextColor = "#FFFFAA";
	memoryConfig.waveColor = "#AAAA39";
	diskConfig.circleColor = "#6DA398";
	diskConfig.textColor = "#0E5144";
	diskConfig.waveTextColor = "#6DA398";
	diskConfig.waveColor = "#246D5F";
	sysInfo.forEach(function(value,index,array){
		cpuChart[index] = loadLiquidFillGauge("cpuInfo"+index, Math.round(value[value.length-1].cpuUsage*1000)/10.0, cpuConfig);
		memoryChart[index] = loadLiquidFillGauge("memoryInfo"+index, Math.round((value[value.length-1].memoryTotal-value[value.length-1].memoryFree)/value[value.length-1].memoryTotal*1000)/10.0, memoryConfig);
		diskChart[index] = loadLiquidFillGauge("diskInfo"+index, Math.round((value[value.length-1].diskTotal-value[value.length-1].diskFree)/value[value.length-1].diskTotal*1000)/10.0, diskConfig);
	});
}

//적용 x
function circularGauge() {
	sysInfo.forEach(function(value,index,array){
		// cpu 사용량 gauge차트 정의
		cpuChart[index] = $("#cpuInfo"+index).dxCircularGauge({
	        scale: {
	            startValue: 0,
	            endValue: 100,
	    		tickInterval: 10,
	            label: {
	            	useRangeColors: true,
	                customizeText: function (arg) {
	                    return arg.valueText + " %";
	                }
	            }
	        },
	        rangeContainer: {
	        	/*palette : "pastel",*/
	            ranges: [
	            	/*{startValue: 0, endValue: 60},
	            	{startValue: 60, endValue: 75},
	            	{startValue: 75, endValue: 90},
	            	{startValue: 90, endValue: 100},*/
	            	{ startValue: 0, endValue: 60, color: "#70B3A1" },
	                { startValue: 60, endValue: 75, color: "#FADCA5" },
	                { startValue: 75, endValue: 90, color: "#E6A055" },
	                { startValue: 90, endValue: 100, color: "#BB626A" }
	            ]
	        },
	        tooltip: {
	            enabled: true,
	            customizeTooltip: function (arg) {
	                return {
	                    text: arg.valueText + " %"
	                };
	            },
	            font: {
	                color: "#787878",
	                size: 15
	            }
	        },
	        /*"export": {
	        	enabled : true,
		    	printingEnabled : false,
		    	formats : ["JPEG"]
	        },*/
	        title: {
	            text: "CPU", // 언어팩 정의해야함
	            horizontalAlignment:'center',
		        font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
	        },
	        value: Math.round(value[value.length-1].cpuUsage*1000)/10.0
	    }).dxCircularGauge("instance");
		
		// memory 사용량 gauge차트 정의
		memoryChart[index] = $("#memoryInfo"+index).dxCircularGauge({
	        scale: {
	            startValue: 0,
	            endValue: 100,
	    		tickInterval: 10,
	            label: {
	            	useRangeColors: true,
	                customizeText: function (arg) {
	                    return arg.valueText + " %";
	                }
	            }
	        },
	        rangeContainer: {
	            ranges: [
	            	{ startValue: 0, endValue: 60, color: "#70B3A1" },
	                { startValue: 60, endValue: 75, color: "#FADCA5" },
	                { startValue: 75, endValue: 90, color: "#E6A055" },
	                { startValue: 90, endValue: 100, color: "#BB626A" }
	            ]
	        },
	        tooltip: {
	            enabled: true,
	            customizeTooltip: function (arg) {
	                return {
	                    text: arg.valueText + " %"
	                };
	            },
	            font: {
	                color: "#787878",
	                size: 15
	            }
	        },
	       /* "export": {
	        	enabled : true,
		    	printingEnabled : false,
		    	formats : ["JPEG"]
	        },*/
	        title: {
	            text: "Memory", // 언어팩 정의해야함
	            horizontalAlignment:'center',
		        font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
	        },
	        value: Math.round((value[value.length-1].memoryTotal-value[value.length-1].memoryFree)/value[value.length-1].memoryTotal*1000)/10.0
	    }).dxCircularGauge("instance");
		
		// disk 사용량 gauge차트 정의
		
		diskChart[index] = $("#diskInfo"+index).dxCircularGauge({
	        scale: {
	            startValue: 0,
	            endValue: 100,
	    		tickInterval: 10,
	            label: {
	            	useRangeColors: true,
	                customizeText: function (arg) {
	                    return arg.valueText + " %";
	                }
	            }
	        },
	        rangeContainer: {
	            ranges: [
	            	{ startValue: 0, endValue: 60, color: "#70B3A1" },
	                { startValue: 60, endValue: 75, color: "#FADCA5" },
	                { startValue: 75, endValue: 90, color: "#E6A055" },
	                { startValue: 90, endValue: 100, color: "#BB626A" }
	            ]
	        },
	        tooltip: {
	            enabled: true,
	            customizeTooltip: function (arg) {
	                return {
	                    text: arg.valueText + " %"
	                };
	            },
	            font: {
	                color: "#787878",
	                size: 15
	            }
	        },
	       /* "export": {
	        	enabled : true,
		    	printingEnabled : false,
		    	formats : ["JPEG"]
	        },*/
	        title: {
	            text: "Disk", // 언어팩 정의해야함
	            horizontalAlignment:'center',
		        font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
	        },
	        value: Math.round((value[value.length-1].diskTotal-value[value.length-1].diskFree)/value[value.length-1].diskTotal*1000)/10.0
	    }).dxCircularGauge("instance");
	});
}

