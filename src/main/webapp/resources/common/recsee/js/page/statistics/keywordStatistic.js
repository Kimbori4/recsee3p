var lineChart;

window.onload = function() {
	
	drawKeyword();
	weekKeyword();
	formFunction();
}

function drawKeyword(){
	var selectArr = [];
	try{
		var data = $('#CategoryOrKeywordFilter').select2('data');
		if(data!=undefined && data.length > 0 ){
			for(var i = 0 ; i < data.length ; i++){
				selectArr.push(data[i].id);
			}
		}
	}catch(e){}
	
	$.ajax({
		url : contextPath + "/keywordStatisticDate.do",
		data : {
					type : $("#keywordFilter").val()
				,	keywords : selectArr.toString()
		},
		type : "POST",
		async : false,
		cache: false,
		beforeSend : function(){
			if(lineChart != null)
				lineChart.destroy();
		},
		complete : function(){
			
		},
		success : function(jRes){
			var res = jRes.resData.data;
			
			var label = [];
			var words = [];
			var data = [];
			
			for(var i = 0; i < res.length; i++){
				
				label.push(res[i].rDate.replace(/(\(|\))/gi,""));
				if($("#keywordFilter").val() == "category")
					if(res[i].rCategory != null) words.push(res[i].rCategory);
				else
					if(res[i].rWord != null) words.push(res[i].rWord);
				
				var obj = {};
				obj.date = res[i].rDate;
				obj.word = ($("#keywordFilter").val() == "category") ? res[i].rCategory : res[i].rWord;
				obj.value = res[i].rWordCount;
				data.push(obj);
			}
			
			label = uniqArray(label);
			words = uniqArray(words);
			createChartLine(label, words, data);
			
		},
		error : function(){
			
		}
		
	});
}


function createChartLine(label, words, data){
	var datasets = [];
	
	var colorArr = ["#ffcd56","#4bc0c0","#36a2eb","#ff6384","#ff9f40"];
	
	for(var i = 0; i <words.length; i++){
		var color = colorArr[Math.floor(Math.random() * colorArr.length)];
		var obj = {};
		obj.label = words[i];
		obj.backgroundColor = color;
		obj.borderColor = color;
		obj.fill = false;
//		obj.lineTension = 0;			직선 용
		obj.data = [];
		for(var k = 0; k < label.length; k++){
			obj.data[k] = 0;
			for(var j = 0; j < data.length; j++){
				if(words[i] == data[j].word && label[k] == data[j].date.replace(/(\(|\))/gi,"")){
					obj.data[k] = data[j].value;
				}
			}
		}
		
		console.log(obj);
		datasets.push(obj);
	}
	console.log(datasets);
	
	var config = {
		type: 'line',
		data: {
			labels: label,
			datasets: datasets
		},
		options: {
			responsive: true,
			title: {
				display: false
			},
			tooltips: {
				mode: 'index',
				intersect: false,
			},
			legend: {
				display: true,
	            labels: {
	                fontColor: 'rgb(0, 0, 0)',
	            	fontSize: 16,
	            },
	 			position: 'bottom'
			},
			hover: {
				mode: 'nearest',
				intersect: true
			},
			scales: {
				xAxes: [{
					display: true,
					scaleLabel: {
						display: true,
					}
				}],
				yAxes: [{
					display: true,
					ticks: {
	                    suggestedMin: 0,
	                    stepSize: 1
	                },
					scaleLabel: {
						display: true,
					}
				}]
			}
		}
	};
	
	var ctx = document.getElementById('weekChart').getContext('2d');
	lineChart = new Chart(ctx, config);
}


//폼 관련 기능
function formFunction(){
	
	getCategoryOrKerwordList();
	$("#keywordFilter").change(function(){
		getCategoryOrKerwordList();
	});
	
	// 조회 버튼 연동
	$("#searchBtn").click(function(){
		
		// Search
		drawKeyword();
	});
}

function getCategoryOrKerwordList(){
	try{
		$("#CategoryOrKeywordFilter").select2('destroy');
	}catch(e){}
	$.ajax({
		url : contextPath + "/getCategoryOrKeyword.do",
		data : {
					type : $("#keywordFilter").val()
		},
		type : "POST",
		async : false,
		cache: false,
		success : function(jRes){
			var res = jRes.resData.data;
			
			switch($("#keywordFilter").val()){
			case "category":
				var str = "";
				for(var i = 0; i < res.length; i++){
					if(res[i] != null)
						str += "<option value='"+res[i].rCategoryName+"'>" + res[i].rCategoryName + "</option>"
				}
				$("#CategoryOrKeywordFilter").html(str);
			break;
			case "keyword":
				var str = "";
				for(var i = 0; i < res.length; i++){
					if(res[i] != null)
						str += "<option value='"+res[i].rKeywordName+"'>" + res[i].rKeywordName + "</option>"
				}
				$("#CategoryOrKeywordFilter").html(str);
			break;
			}
			
			
			$("#CategoryOrKeywordFilter").attr("multiple","multiple").css("width","500").select2({
					placeholder: ''
				,	allowClear: true
			});
			
			$("#CategoryOrKeywordFilter").val('')
			$("#CategoryOrKeywordFilter").select2()
		},
		error : function(){
			
		}
		
	});
}

function weekKeyword(){
	//best
	$.ajax({
		url : contextPath + "/getBestOrWorstKeyword.do",
		data : {
			division : "Best"
		},
		type : "POST",
		async : true,
		cache: false,
		success : function(jRes){
			var res = jRes.resData.data;
			
			RankingInnerHTML("Best", res);
			
		},
		error : function(){
			
		}
	});
	
	//worst
	$.ajax({
		url : contextPath + "/getBestOrWorstKeyword.do",
		data : {
			division : "Worst"
		},
		type : "POST",
		async : true,
		cache: false,
		success : function(jRes){
			var res = jRes.resData.data;
			
			RankingInnerHTML("Worst", res);
			
		},
		error : function(){
			
		}
	});
}

function RankingInnerHTML(t, data){
	var str = "";
	
	for(var i = 1; i <= data.length; i++){
		var upCheck = (data[i-1].increase > 1) ? "up" : "down";
		var increase = (data[i-1].increase == null) ? "-" : data[i-1].increase + "" ;
		str += "<div class='inner_wrapper'>" +
				"<div class='rank-number'>" + i + "</div>" + 
				"<div class='word'>" + data[i-1].rWord + "</div>" +
				"<div class='value'>" + data[i-1].toweekcnt + "</div>" +
				"<div class='increase " + upCheck + "'>" + increase.substr(0,5) + "%</div>" +
			  "</div>";
	}
	if(t == "Best")
		$(".chart-wrap .box .best-rank").html(str);
	else
		$(".chart-wrap .box .worst-rank").html(str);
}

function uniqArray(names){
	var uniq = names.reduce(( a, b ) => {
		if( a.indexOf(b) < 0 ) a.push(b) ;
		return a ;
	}, []) ;

	// 한 줄로 표현
	return uniq;
}

