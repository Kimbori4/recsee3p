var myPalette = ['#7CBAB4', '#92C7E2', '#75B5D6', '#B78C9B', '#F2CA84', '#A7CA74'];
var series;
DevExpress.viz.core.registerPalette('mySuperPalette', myPalette);


addLoadEvent(dashboardLoad);


function dashboardLoad() {

	createChart();
	ui_controller();
}

createChart = function() {
	dhx4.ajax.get(contextPath+'/statistics/dashboard_data.json', function(response) {
		var jsonData = "";
		
		if (response.xmlDoc.response != undefined) {
			jsonData = jQuery.parseJSON(response.xmlDoc.response);
		} else {
			jsonData = jQuery.parseJSON(response.xmlDoc.responseText);
		}
		
		$('#allCalls').html(jsonData.totalCalls.format());
		$('#allTime').html(seconds2time(jsonData.totalTime));

		// 일간 녹취 통계 차트 데이터 생성
		dataDaily = new DevExpress.data.DataSource({
			load: function(loadOption) {
				var d = $.Deferred();
				d.resolve(jsonData.daily);
				return d.promise();
			}
		});

        // 주간 녹취 통계 차트 데이터 생성
        dataWeekly = new DevExpress.data.DataSource({
            load: function(loadOption) {
                var d = $.Deferred();
                d.resolve(jsonData.weekly);
                return d.promise();
            }
        });

        // 콜 건수 비례 녹취 파일 통계 차트 데이터 생성
        dataRecfile = new DevExpress.data.DataSource({
            load: function(loadOption) {
                var d = $.Deferred();
				d.resolve(jsonData.callByfile);
                return d.promise();
            }
        });
       
        if(jsonData.hideConference==1&& jsonData.hideTranser==1){
        	if(telnoUse != 'Y'){		
	        	series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	                ,{valueField:'internal',name:lang.statistics.title.extension /* '내선' */, color:'#B78C9B'}
	            ]
        	}else{
        		series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ]
        	}
        }else if(jsonData.hideConference==1&& jsonData.hideTranser==0){
        	if(telnoUse != 'Y'){	
	        	series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ,{valueField:'transfer',name:lang.statistics.title.callTransfer /* '호전환' */, color:'#A7CA74'}
	               ,{valueField:'internal',name:lang.statistics.title.extension /* '내선' */, color:'#B78C9B'}
	            ]
        	}else{
        		series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ,{valueField:'transfer',name:lang.statistics.title.callTransfer /* '호전환' */, color:'#A7CA74'}
	              ]     		
        	}
        }else if(jsonData.hideConference==0&& jsonData.hideTranser==1){
        	if(telnoUse != 'Y'){	
	        	series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ,{valueField:'conference',name:lang.statistics.title.meeting /* '회의' */, color:'#F2CA84'}
	               ,{valueField:'internal',name:lang.statistics.title.extension /* '내선' */, color:'#B78C9B'}
	            ]
        	}else{
        		series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ,{valueField:'conference',name:lang.statistics.title.meeting /* '회의' */, color:'#F2CA84'}
	            ]
        	}
        }else{
        	if(telnoUse != 'Y'){
	        	series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ,{valueField:'transfer',name:lang.statistics.title.callTransfer /* '호전환' */, color:'#A7CA74'}
	               ,{valueField:'conference',name:lang.statistics.title.meeting /* '회의' */, color:'#F2CA84'}
	               ,{valueField:'internal',name:lang.statistics.title.extension /* '내선' */, color:'#B78C9B'}
	            ]
        	}else{
        		series=[
	                {valueField:'inbound',name:lang.fn.get("statistics.dashboard.title.axisInbound"), color:'#FFBB00'}
	               ,{valueField:'outbound',name:lang.fn.get("statistics.dashboard.title.axisOutbound"), color:'#F65314'}
	               ,{valueField:'transfer',name:lang.statistics.title.callTransfer /* '호전환' */, color:'#A7CA74'}
	               ,{valueField:'conference',name:lang.statistics.title.meeting /* '회의' */, color:'#F2CA84'}
	            ]
        	}
        }
        
        // 서버 별 통화 시간 데이터 생성
       /* dataStorage = new DevExpress.data.DataSource({
            load: function(loadOption) {
                var d = $.Deferred();
				d.resolve(jsonData.system);
                return d.promise();
            }
        });*/

        // 일간 녹취 통계 차트 생성
		dataDaily.load().done(function(resultData) {
			var chartDaily = $('#chartDaily').dxChart({
				dataSource: resultData,
				commonSeriesSettings: {
					argumentField: 'group',
					type:"line",
					// color: '#ffa500',
					label: {visible:false,format:'largeNumber',percision:0}
				},
	            commonPaneSettings: {
	                border: {visible: true}
	            },
				title: {
					text:lang.statistics.title.timeOfTheDay /* '금일 시간에 따른 녹취 타입별 통계' lang.fn.get("statistics.dashboard.title.daily")*/,
					horizontalAlignment:'center',
					font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
				},
				series: series,
				legend: {
					visible:true,verticalAlignment:'bottom',horizontalAlignment:'center'
				},
				valueAxis: {
					label: {visible: true},
					min: 0,
					title: lang.statistics.title.callCount /* "콜 개수" */
				},
				commonAxisSettings: {
					visible:true,opacity:0.3
				},
                argumentAxis: {
                    grid: {visible: true},
                    title:lang.statistics.title.time /* "시간" */
                },
			    tooltip: {
			    	enabled:true,
			        customizeTooltip:function (arg) {
			            return {text:arg.valueText};
			        }
			    },export:{
			    	enabled : true,
	            	printingEnabled : false,
	            	formats : ["JPEG"]
			    },
				palette: 'myPalette'
			});
		});

		// 주간 녹취 통계 차트 생성
		dataWeekly.load().done(function(resultData) {
	        var chartWeekly = $("#chartWeekly").dxChart({
	            dataSource: resultData,
	            commonSeriesSettings: {
	                argumentField: 'group',
	                type: "line",
	                // color: '#ffa500',
                    label: {visible:false,format:'largeNumber',percision:0}
	            },
                commonPaneSettings: {
                    border: {visible: true}
                },
                title: {
                	text:lang.statistics.title.weekly /* '주간 녹취 타입별 통계'lang.fn.get("statistics.dashboard.title.week")*/,
                	horizontalAlignment:'center',
                    font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
                },
	            series: series,
	            legend: {
	            	visible:true,
	            	verticalAlignment:'bottom',
	            	horizontalAlignment:'center'
	            },
	            valueAxis: {
	                label: {visible: true},
	                min: 0,
	                valueType: 'numeric',
	                title:lang.statistics.title.callCount /* "콜 개수" */
	                // tickInterval: 10
	            },
	            commonAxisSettings: {
	            	visible: true,
	            	opacity: 0.3
	            },
                argumentAxis: {
                    grid: {visible: true},
                    title:lang.statistics.title.date /* "날짜" */
                },
                "export":{
                	enabled:true,
	            	printingEnabled : false,
	            	formats : ["JPEG"]
                },
	            tooltip: {
	            	enabled:true,
	            	customizeTooltip: function (arg) {
	                    return {
	                        text: arg.valueText
	                    };
	                }
	            }
	        });
        });

		// 콜 건수 비례 녹취 파일 통계 차트 생성
		dataRecfile.load().done(function(resultData) {
	        var chartRecfile = $('#chartRecfile').dxChart({
	            dataSource: resultData,
	            commonSeriesSettings: {
	                argumentField: 'group',
	                type: 'bar',
	                // color: '#ffa500',
                    label: {visible:false,format:'largeNumber',percision:0}
	            },
                commonPaneSettings: {
                    border: {visible: true}
                },
	            title: {
                    text:lang.statistics.title.hourOfTheDay /* '금일 시간별 녹취 통계' lang.fn.get("statistics.dashboard.title.callPerFile")*/,
                    horizontalAlignment: 'center',
                    font: {color: 'steelblue', family: 'Noto Sans', opacity: 0.75, size: 25, weight: 'bold'}
                },
	            series: [
	                {valueField: 'calls', name: lang.fn.get("statistics.dashboard.title.axisCall"), color:'#00A1F1',label:{
						visible:true,
						customizeText: function() {
							return this.value + lang.statistics.title.cases /* " 건" */;
						}
					},font:{family: 'Noto Sans'}}
	                /*,
	                {valueField: 'recfile', name: lang.fn.get("statistics.dashboard.title.axisFile"), color:'#7CBB00'}*/
	            ],
                legend: {
                    visible: true,
                    verticalAlignment: 'bottom',
                    horizontalAlignment: 'center'
                },
	            valueAxis: {
	            	label: {visible: true},
	                min: 0,
	                title:lang.statistics.title.callCount /* "콜 개수" */
	            },
	            commonAxisSettings: {
	                visible: true,
	                opacity: 0.3
	            },
                argumentAxis: {
                    grid: {visible: true},
                    title:lang.statistics.title.time /* "시간" */
                },
	            tooltip: {
	                enabled: true,
	                customizeTooltip: function (arg) {
	                    return {
	                        text: arg.valueText
	                    };
	                }
	            },export :{
	            	enabled: true,
	            	printingEnabled : false,
	            	formats : ["JPEG"]
	            }
	            // palette: 'mySuperPalette'
	        });
        });


        // 서버 별 통화 시간 차트 생성
        /*dataStorage.load().done(function(resultData) {
        	var chartStorage = $("#chartStorage").dxChart({
	            dataSource: resultData,
	            commonSeriesSettings: {
	                argumentField: 'group',
	                type: 'bar',
	                // color: '#ffa500',
                    label: {visible:false,format:'largeNumber',percision:0}
	            },
                commonPaneSettings: {
                    border: {visible: true}
                },
                title: {
                    text:lang.fn.get("statistics.dashboard.title.server"),
                    horizontalAlignment:'center',
                    font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
                },
	            series: [
	                {valueField: 'calltime', name: lang.fn.get("statistics.dashboard.title.axisTime"), color:'#EDF798'}
	            ],
                legend: {
                    visible: true,
                    verticalAlignment: 'bottom',
                    horizontalAlignment: 'center'
                },
	            valueAxis: {
	                label: {
	                	format: 'largeNumber',
	                	customizeText: function(value) {
	                		  return value.value + lang.fn.get("statistics.unit.time");
	                	}
	                },
	                min: 0,
	                // tickInterval: 100
	            },
	            commonAxisSettings: {
	                visible: true,
	                opacity: 0.3
	            },
	            argumentAxis: {
	            	grid: {visible: true}
	            },
                tooltip: {
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.valueText
                        };
                    }
                }
	        });
        });*/
        window.setTimeout('createChart()', 1000*60);
	});
}