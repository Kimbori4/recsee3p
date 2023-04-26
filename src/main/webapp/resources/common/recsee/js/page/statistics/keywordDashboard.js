var myDoughnutChart;		
var myBarChart;
var keyWordGrid;


window.onload = function() {
	datepickerSetting(locale,'#sDate, #eDate');
	
	drawKeyword();
	
	keyWordGrid = keyWordgridLoad();
	
	// resize
	$(window).resize();
	
	formFunction();
	
	ui_controller();
	
	// sDate, eDate Event
	$("#sDate, #eDate").change(function(){fromTo($("#sDate"),$("#eDate"),this)})
	$("#sDate, #eDate").keyup(function(){if($("#sDate").val().replace(/[:-]/g,'').length==8&&$("#eDate").val().replace(/[:-]/g,'').length==8) fromTo($("#sDate"),$("#eDate"),this)})
}


function drawKeyword(){
	$.ajax({
		url : contextPath + "/keywordStatistic.do",
		data : {
				type : $("#keywordFilter").val()
			,	sDate : $("#sDate").val() 
			,	eDate : $("#eDate").val()
		},
		type : "POST",
		async : false,
		cache: false,
		beforeSend : function(){
			if(myDoughnutChart != null)
				myDoughnutChart.destroy();
			if(myBarChart != null)
				myBarChart.destroy();
			
			$(".all-count .count").html(0);
		},
		complete : function(){
			
		},
		success : function(jRes){
			if(jRes.success == "Y"){
				var res = jRes.resData.data;
				
				var label = [];
				var value = [];
				
				// 총건 표시
				$(".all-count .count").html(res[0].allCnt);
				
				for(var i = 0; i < res.length; i++){
					if($("#keywordFilter").val() == "category"){
						label.push(res[i].rCategoryName);
						value.push(res[i].rWordCount);
					}else{
						label.push(res[i].rWord);
						value.push(res[i].rWordCount);
					}
					
				}
				
				createChartDoughnut(label, value);
				createChartLine(label, value);
			}
		},
		error : function(){
			
		}
		
	})
}

function createChartDoughnut(l, v){
	  var ctx = document.getElementById("doughnutChart");
	  
	  var colorArr = [];
	  for(var i = 0; i < v.length; i++ ){
		  var p = ["#ffcd56","#4bc0c0","#36a2eb","#ff6384","#ff9f40"];
		  colorArr.push(p[i % p.length]);
	  }
	
	  var data = {
			  	labels: l,
			    datasets: [
			        {
			            data: v,
			            borderWidth:0,
			            backgroundColor: colorArr,
			            hoverBackgroundColor: colorArr
			        }]
			    };
	  
		myDoughnutChart = new Chart(ctx, {
		    type: 'doughnut',
		    data: data,
		    options: {
		    	legend: {
		            display: true,
		            labels: {
		                fontColor: 'rgb(0, 0, 0)',
	                	fontSize: 16,
		            },
		 			position: 'left'
		    	},
		          responsive: true,
		          tooltips: {enabled: true},
		          hover: {mode: null},
		          cutoutPercentage	 : 50
		        }
		});
}

function createChartLine(l, v){
	  var ctx = document.getElementById("HorizontalChart");
	  
	  var colorArr = [];
	  for(var i = 0; i < v.length; i++ ){
		  var p = ["#ffcd56","#4bc0c0","#36a2eb","#ff6384","#ff9f40"];
		  colorArr.push(p[i % p.length]);
	  }
	
	  var data = {
			  	labels: l,
			    datasets: [
			        {
			            data: v,
			            borderWidth:1,
			            fill : false,
			            backgroundColor: colorArr,
			            hoverBackgroundColor: colorArr
			        }]
			    };
	
	myBarChart = new Chart(ctx, {
	    type: 'horizontalBar',
	    data: data,
	    options: {
	    	legend: {
	            display: false,
	    	},
	    	"scales":{
	    		"xAxes":
	    			[{"ticks":{"beginAtZero":true}}]
    		}
	    }
	});
}

function keyWordgridLoad(){
	keywordTable = new dhtmlXGridObject("keywordTable");
	keywordTable.setIconsPath(recseeResourcePath + "/images/project/");
	keywordTable.setImagePath(recseeResourcePath + "/images/project/");
	keywordTable.i18n.paging = i18nPaging[locale];
	keywordTable.enablePaging(true, 20, 5, "pagingkeywordTable", true);
	keywordTable.setPagingWTMode(true,true,true,[20,40,240]);
	keywordTable.setPagingSkin("toolbar", "dhx_web");
	keywordTable.enableColumnAutoSize(false);
	keywordTable.setSkin("dhx_web");
	keywordTable.init();
	
	keywordTable.load(contextPath + "/statistics/keyword_dashboard_table.xml?head=true", function() {
		//keywordTable.aToolBar.addText("total",6,'<div style="width: 100%; text-align: center;">'+ gridCallUser.i18n.paging.results+'    '+ gridCallUser.getRowsNum()+lang.statistics.js.alert.msg8/*건*/+'</div>')
		keywordTable.aToolBar.setWidth("total",80)

		keywordTable.aToolBar.hideItem("perpagenum");

		keywordTable.aToolBar.setMaxOpen("pages", 5);
		keywordTable.attachEvent("onXLS", function(){
			progress.on()
		});

		/*gridCallUser.attachEvent("onDataReady", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});*/
		//그래프그릴때 아직 그리드가 파싱되지 않아서 오류 발생
		keywordTable.attachEvent("onPaging", function(){
			try{
				onGraphProc();
			}catch(e){
			}
		});

		keywordTable.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum()){
				return false;
			}
			return true;
		});
		// 소팅 이벤트 커스텀
		keywordTable.attachEvent("onBeforeSorting", function(ind){
				var a_state = this.getSortingState()

				var direction = "asc"
				if(nowSortingColumn==ind)
					direction = ((a_state[1] == "asc") ? "desc" : "asc");

				var columnId = this.getColumnId(ind);

				formData(columnId, direction)

				this.setSortImgState(true,ind,direction)
				var a_state = this.getSortingState()
				nowSortingColumn = a_state[0];

		});

//		keywordTable.attachEvent("onXLE", function(grid_obj,count){
//			var setResult = '<div style="width: 100%; text-align: center;">'+ gridCallUser.i18n.paging.results+ gridCallUser.getRowsNum()+gridCallUser.i18n.paging.found+'</div>'
//			keywordTable.aToolBar.setItemText("total", setResult)
//			progress.off();
//		})

		ui_controller();

		$(window).resize();
		$(window).resize(function() {
			keywordTable.setSizes();
		});

	}, 'xml');

	return keywordTable;
}


//폼 관련 기능
function formFunction(){
	// 조회 버튼 연동
	$("#searchBtn").click(function(){
		var Filter = $("#keywordFilter").val();
		
		if($("#sDate").val().length != 10 || $("#eDate").val().length != 10){
			alert(lang.views.search.alert.msg21/*"녹취 일자를 다음 형식에 맞게 입력 해 주세요!\n예)2018-01-01"*/);
		}else if(listenPeriod!="" && (checkDate-new Date($("#sDate").val()))>0){
			alert(lang.views.search.alert.listenPeriod1+listenPeriod+lang.views.search.alert.listenPeriod2/*"현재 날짜부터 listenPeriod개월 이전 이력은 검색할 수 없습니다."*/)
		}else{
			
			// Search
			drawKeyword();
			
			strData = "sDate="+$("#sDate").val()+"&eDate="+$("#eDate").val()+"&type="+$("#keywordFilter").val();
			
			keyWordGrid.clearAndLoad(contextPath+'/statistics/keyword_dashboard_table.xml?' + strData, function() {
				
			}, 'xml');
			
		}
	});
}