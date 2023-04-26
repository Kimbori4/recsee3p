var myPalette = ['#7CBAB4', '#92C7E2', '#75B5D6', '#B78C9B', '#F2CA84', '#A7CA74'];
DevExpress.viz.core.registerPalette('mySuperPalette', myPalette);
var myCombo;
var strData;
var currentSelect;
var interval;
addLoadEvent(dashboardLoad);


function dashboardLoad() {

	myCombo = dhtmlXComboFromSelect("selectPeople", null,null,"checkbox");
	myCombo.setSkin("dhx_web")
	myCombo.setImagePath(commResourcePath + "/images/");
	//myCombo.enableAutocomplete(true);
	myCombo.enableFilteringMode(true);
	myCombo.setPlaceholder(lang.statistics.title.selectEmployee) /* "사원을 선택하세요" */
	myCombo.hide()

	myCombo.attachEvent("onCheck", function(value, state) {
		comboEvent(this.getIndexByValue(value),state);
		return false;
	});

	myCombo.attachEvent("onChange", function(value, text) {
		var indx=this.getSelectedIndex()
		comboEvent(indx,!this.isChecked(indx));
		return false;
	});

	function comboEvent(idx, state){

		myCombo.setChecked(idx,state);
		var count=myCombo.getChecked().length

		myCombo.unSelectOption();
		if(count!=0)
			myCombo.setComboText(lang.statistics.title.total /*"총 "*/+count+ lang.statistics.title.selectPeople /* "명 선택" */)
		myCombo.openSelect();
	}

	//alert(accessLevel)
	if(accessLevel=="U"){
		$("#dash").hide();
	}else if(accessLevel=="S"){
		$("#mBgCode").hide()
		$("#mMgCode").hide()
		$("#mSgCode").hide()
		comboLoadFunction(myCombo)
		myCombo.show()
		$("#mBgCode").val(userInfoJson.bgCode)
		$("#mMgCode").val(userInfoJson.mgCode)
		$("#mSgCode").val(userInfoJson.sgCode)
		currentSelect='selectPeople';
		$( "#codeFilter option[value='mBgCode']").remove()
		$( "#codeFilter option[value='mMgCode']").remove()
		$( "#codeFilter option[value='mSgCode']").remove()
	}else if(accessLevel=="M"){
		$("#mBgCode").hide()
		$("#mMgCode").hide()
		$("#mSgCode").show()
		$("#mBgCode").val(userInfoJson.bgCode)
		$("#mMgCode").val(userInfoJson.mgCode)
		currentSelect='mSgCode';
		$( "#codeFilter option[value='mBgCode']").remove()
		$( "#codeFilter option[value='mMgCode']").remove()
	}else if(accessLevel=="B"){
		$("#mBgCode").hide()
		$("#mBgCode").val(userInfoJson.bgCode)
		currentSelect='mMgCode';
		$( "#codeFilter option[value='mBgCode']").remove()
	}

	$("#searchBtn").click(function(){
		var data=null;
		var dataArray = new Array();

		var bgCode = $("#mBgCode :selected").val() ? "'"+$("#mBgCode :selected").val()+"'" : ""
		var mgCode = $("#mMgCode :selected").val() ? "'"+$("#mMgCode :selected").val()+"'" : ""
		var sgCode = $("#mSgCode :selected").val() ? "'"+$("#mSgCode :selected").val()+"'" : ""
		var userId = "";
		var sysCode=$("#sysCode  :selected").val();

		var selectedCode = $( "#codeFilter :selected" ).val()

		if($('#'+selectedCode).hasClass("select2-hidden-accessible")){
			data = $('#'+selectedCode).select2('data');
			for(var i = 0; i < data.length ; i++){
				dataArray.push("'"+data[i].id+"'")
			}
		}
		else {
			data=$('#'+selectedCode).val();
			dataArray.push("'"+data+"'")
		}

		switch(selectedCode){
		case"mBgCode":
			bgCode=dataArray.toString();
			break;
		case"mMgCode":
			mgCode=dataArray.toString();
			break;
		case"mSgCode":
			sgCode=dataArray.toString();
			break;
		case"selectPeople":
			for(var i=0;i<myCombo.getChecked().length;i++){
				if(i==myCombo.getChecked().length-1)
					userId+="'"+myCombo.getChecked()[i]+"'";
				else
					userId+="'"+myCombo.getChecked()[i]+"',";
			}
			break;
		}
		strData = "bgCode="+bgCode+"&mgCode="+mgCode+"&sgCode="+sgCode+"&userId="+userId+"&sysCode="+sysCode
		createChart(strData);

		window.clearInterval(interval);

		interval= window.setInterval(function() {

        	createChart(strData);
		} , 1000*60);
	});

	//서버옵션로드
	searchSelectOptionLoad($("#sysCode"),'/selectOption.do?comboType=system&comboType2=&ALL=');


	// 분류 코드 필터링
	$( "#codeFilter" ).change(function() {
		$(".select2").remove();
		$(".codeList").hide().removeAttr("multiple").removeClass("select2-hidden-accessible");
		var selectedCode = $( "#codeFilter :selected" ).val()

		switch(selectedCode){
		case "mBgCode":
			$("#mBgCode").show()
			$(".codeList").val("")
			myCombo.hide()
			myCombo.clearAll()
			myCombo.setComboText("")
			break;
		case "mMgCode":
			if(accessLevel=="B"){
				$("#mMgCode").show()
				$("#mBgCode").val(userInfoJson.bgCode).trigger("change");
			}
			else
				$("#mBgCode, #mMgCode").show()
			$(".codeList").val("")
			myCombo.hide()
			myCombo.clearAll()
			myCombo.setComboText("")
			break;
		case "mSgCode":
			if(accessLevel=="M"){
				$("#mSgCode").show()
				$("#mBgCode").val(userInfoJson.bgCode).trigger("change");
				$("#mMgCode").val(userInfoJson.mgCode).trigger("change");
			}
			else if(accessLevel=="B"){
				$("#mMgCode, #mSgCode").show()
				$("#mBgCode").val(userInfoJson.bgCode).trigger("change");
			}
			else
				$("#mBgCode, #mMgCode, #mSgCode").show()
			$(".codeList").val("")
			myCombo.hide()
			myCombo.clearAll()
			myCombo.setComboText("")
			break;
		case "selectPeople":
			$(".codeList").hide()
			$(".codeList").val("")
			myCombo.show()
			myCombo.clearAll()
			myCombo.setComboText("")
			comboLoadFunction(myCombo)
			break;
		}
		$("#"+selectedCode).attr("multiple","multiple").select2({
				placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
			,	allowClear: true
			,   dropdownAutoWidth : true
			,	width : '220px'
		});
	});

	// 대분류
	selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true)

	if(currentSelect!=null){
		switch (currentSelect) {
		case 'mBgCode':

			break;
		case 'mMgCode':
			selectOrganizationLoad($("#mMgCode"), "mgCode",userInfoJson.bgCode,undefined,undefined,true)
			break;
		case 'mSgCode':
			selectOrganizationLoad($("#mSgCode"), "sgCode",userInfoJson.bgCode,userInfoJson.mgCode,undefined,true)
			break;

		default:
			break;
		}

		$("#"+currentSelect).attr("multiple","multiple").css("width","220").select2({
			placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
				,	allowClear: true
		});

	}else{
		selectOrganizationLoad($("#mBgCode"), "bgCode",undefined,undefined,undefined,true)
		$("#"+"mBgCode").attr("multiple","multiple").css("width","220").select2({
			placeholder: lang.statistics.js.alert.msg4/*"대상 그룹을 선택 해 주세요"*/
				,	allowClear: true
		});
	}


	// 대분류 변화시 중분류 로드
	$( "#mBgCode" ).change(function() {
		$("#mSgCode").empty().val(null).trigger("change");
		selectOrganizationLoad($("#mMgCode"), "mgCode", $("#mBgCode").val(),undefined,undefined,true)
		myCombo.clearAll()
		myCombo.setComboText('')
		// 중분류 및의 소분류가 있다면 디폴트로 로드
	});

	// 중분류 변화시 소분류 로드
	$( "#mMgCode" ).change(function() {
		selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),undefined,true)
		myCombo.clearAll()
		myCombo.setComboText('')
	});


	dhtmlx.image_path='${compoResource}/dhtmlxSuite/codebase/imgs/';
	createChart();
	ui_controller();
	$(".dhxcombo_input").addClass("inputFilter korFilter engFilter numberFilter");
	select2InputFilter()
}

createChart = function(strData) {
	dhx4.ajax.get(contextPath+'/statistics/dashboard_data_user.json?'+strData, function(response) {
		var jsonData = "";

		if (response.xmlDoc.response != undefined) {
			jsonData = jQuery.parseJSON(response.xmlDoc.response);
		} else {
			jsonData = jQuery.parseJSON(response.xmlDoc.responseText);
		}

		$('#allCalls').html(jsonData.totalCalls.format());
		$('#allTime').html(seconds2time(jsonData.totalTime));
		$('#inboundCount').html(jsonData.inCalls.format());
		$('#outboundCount').html(jsonData.outCalls.format());


		// 콜 타입별 통계 차트 데이터 생성
		dataDaily = new DevExpress.data.DataSource({
			load: function(loadOption) {
				var d = $.Deferred();
				d.resolve(jsonData.jsonTypeData);
				return d.promise();
			}
		});

        // 통화시간별 통계 차트 데이터 생성
        dataWeekly = new DevExpress.data.DataSource({
            load: function(loadOption) {
                var d = $.Deferred();
                d.resolve(jsonData.jsonTimeData);
                return d.promise();
            }
        });

        // 일간 콜 개수 통계 차트 데이터 생성
        dataRecfile = new DevExpress.data.DataSource({
            load: function(loadOption) {
                var d = $.Deferred();
				d.resolve(jsonData.jsonDayData);
                return d.promise();
            }
        });


        // 콜 타입별 비율
		dataDaily.load().done(function(resultData) {
			var chartDaily = $('#chartDaily').dxPieChart({
				palette:"Soft Pastel",
	            dataSource: dataDaily,//[{callType:"인바운드",val:40},{callType:"아웃바운드",val:80},{callType:"호전환",val:298},{callType:"회의통화",val:140},{callType:"내선",val:14}],
                title: {
                	text: lang.statistics.js.alert.callRate , //"콜 타입별 비율",
                	horizontalAlignment:'center',
                    font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
                },
                tooltip: {
	            	enabled:true,
	            	customizeTooltip: function (arg) {
	                    return {
	                        text: arg.valueText
	                    };
	                }
	            },
	            legend: {
	            	verticalAlignment:'bottom',
	            	horizontalAlignment:'center',
	            },export:{
			    	enabled : true,
	            	printingEnabled : false,
	            	formats : ["JPEG"]
			    },
	            series: [{argumentField:'callType',type: 'doughnut',label:{
					visible:true,
					customizeText: function() {
						return this.value +" "+lang.statistics.js.alert.msg8/*"건"*/;
					}
				},font:{family: 'Noto Sans'}}]
	        });
        });

		// 통화 시간별 콜 개수
		dataWeekly.load().done(function(resultData) {
	        var chartWeekly = $("#chartWeekly").dxChart({
	        	palette: myPalette,
	        	dataSource: dataWeekly,//[{"time":"30초 미만","count":4},{"time":"3분 미만","count":6},{"time":"5분 미만","count":16},{"time":"10분 미만","count":4},{"time":"10분 이상","count":2}],
                title: {
                	text: lang.statistics.title.callTrends , /* "통화 시간별 콜 추이", */
                	horizontalAlignment:'center',
                    font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
                },
                series:{
                	argumentField:"time",
                	valueField:"count",
                	name: lang.statistics.title.callTrends2, /* "콜 추이", */
                	type:"bar",label:{
						visible:true,
						customizeText: function() {
							return this.value + lang.statistics.title.cases; /* " 건" */
						}
					},font:{family: 'Noto Sans'}
                },argumentAxis:{
					label:{
						overlappingBehavior:"stagger"
					}
				},
                tooltip: {
	            	enabled:true,
	            	customizeTooltip: function (arg) {
	                    return {
	                        text: arg.valueText
	                    };
	                }
	            },export:{
			    	enabled : true,
	            	printingEnabled : false,
	            	formats : ["JPEG"]
			    }
	        });
        });

		// 일별 콜 개수
        dataRecfile.load().done(function(resultData) {
	        var chartRecfile = $('#chartRecfile').dxRangeSelector({
	        	size:{
	        		height:250
	        	},
	            dataSource: dataRecfile,//[{"Date":"2017-11-01", "callCount":60},{"Date":"2017-11-02", "callCount":24},{"Date":"2017-11-03", "callCount":31},{"Date":"2017-11-04", "callCount":72},{"Date":"2017-11-05", "callCount":63},{"Date":"2017-11-06", "callCount":66},{"Date":"2017-11-07", "callCount":51},{"Date":"2017-11-08", "callCount":23},{"Date":"2017-11-09", "callCount":48},{"Date":"2017-11-10", "callCount":66},{"Date":"2017-11-11", "callCount":46},{"Date":"2017-11-12", "callCount":75},{"Date":"2017-11-13", "callCount":34},{"Date":"2017-11-14", "callCount":60},{"Date":"2017-11-15", "callCount":43},{"Date":"2017-11-16", "callCount":37},{"Date":"2017-11-17", "callCount":72},{"Date":"2017-11-18", "callCount":67},{"Date":"2017-11-19", "callCount":53},{"Date":"2017-11-20", "callCount":63},{"Date":"2017-11-21", "callCount":60}],
	            chart:{
	            	useAggregation:false,
	            	valueAxis:{valueType:"numeric"},
	            	series:{
	            		type:"line",
	            		valueField:"callCount",
	            		argumentField:"Date"
	            	}
	            },export:{
			    	enabled : true,
	            	printingEnabled : false,
	            	formats : ["JPEG"]
			    },
	            title: {
                	text: lang.statistics.title.monthly, /* "월간 콜 추이" */
                	horizontalAlignment:'center',
                    font: {color:'steelblue',family:'Noto Sans',opacity:0.75,size:25,weight:'bold'}
                },
                palette: myPalette
	        });
        });


	});
}

function searchSelectOptionLoad(objSelect, loadUrl){

	$.ajax({
		url:contextPath+loadUrl,
		data:{"recIp":parent.playerFrame.rc_player.requestIp},
		type:"GET",
		dataType:"json",
		async: false,
		cache: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				// 불러온 옵션 추가
				$(objSelect).append(jRes.resData.optionResult)
			}
		}
	});
}

function comboLoadFunction (comboObj){
		if (comboObj != null){
			if(accessLevel=='M')
				comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+userInfoJson.bgCode+"&mgCode="+userInfoJson.mgCode);
			else if(accessLevel=='S')
				comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+userInfoJson.bgCode+"&mgCode="+userInfoJson.mgCode+"&sgCode="+userInfoJson.sgCode);
			else if(accessLevel=='A')
				comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode=null");
			else
				comboObj.load(contextPath+"/opt/user_combo_option.xml?bgCode="+userInfoJson.bgCode);
			//comboObj.setSkin(skin);
			//comboObj.enableAutocomplete(autoComplate != true ? false : true );
			//comboObj.enableFilteringMode(filteringMode != true ? false : "between");
			//comboObj.readonly(readonly != true ? false : true);
			return true
		}else
			return false;
}
