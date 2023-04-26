$(function(){
	
	// select박스에서 테이블 선택 시 해당 테이블 내 컬럼 가져오기
	$("#tableList").change(function(){
		$("#columnList").empty();
		$("#selectedColumn").empty();
		var dbName = $("#dbName").val();
		var table_name = $("#tableList").val();
		$.ajax({
			url : contextPath + "/selectColumnProc",
			data : {
				dbName : dbName,
				table_name : table_name
			},
			success : function(result){
				$(JSON.parse(result)).each(function(index, column){
					$("#columnList").append('<option value="'+column.column_name+'">'+column.column_name+'</option>');
				});
			}
		});
	});
	
	// 선택한 컬럼 리스트(selectedColumn)에 추가
	$("#addColumnList").click(function(){
		var column_name = $("#columnList").val();
		if(column_name==null){
			alert("null");
		}else{
			//중복체크
			for(var i = 0 ; i < $("#selectedColumn option").length ; i++){
				if($("#selectedColumn option")[i].text==column_name){
					alert("already");
					return;
				}
			}
			$("#selectedColumn").append('<option value="'+column_name+'">'+column_name+'</option>');
			onSelectSQLContentProc();
		}
	});
	
	// 선택한 컬럼 리스트(selectedColumn)에서 제거
	$("#takeAwayColumnList").click(function(){
		var column_name = $("#selectedColumn").val();
		if(column_name==null){
			alert("null");
		}else{
			for(var i = 0 ; i < column_name.length ; i++) {
				$("#selectedColumn option[value='"+column_name[i]+"']").remove();
			}
			if($("#selectedColumn option").length > 0){
				onSelectSQLContentProc();
			}else{
				$("#sqlContent").val("");
			}
		}
	});
	
	// select로 사용할 SQL 정보 저장
	$("#selectSQL").on("click", function(){
		var table_name = $("#tableList").val();
		var dbName = $("#dbName").val();
		var sqlName = $("#sqlName").val(); // null처리, 중복처리 추가해야함
		var sqlDescription = $("#sqlDescription").val();
		var column_list = new Array();
		for(var i = 0 ; i < $("#selectedColumn option").length ; i++){
			column_list.push($("#selectedColumn option")[i].text);
		}
		// 최소 1개 컬럼 선택
		if(column_list == ""){
			alert("Contain at least one column.")
			return
		}
		$.ajax({
			url : contextPath + "/selectSQLProc",
			data : {
				table_name : table_name,
				dbName : dbName,
				sqlName : sqlName,
				sqlDescription : sqlDescription,
				column_list : JSON.stringify(column_list)
			},
			success : function(result){
				location.reload();
				/*$("#selectedColumn").empty();
				$("#columnList").empty();
				$("#sqlName").val("");
				$("#sqlDescription").val("");
				$("#tableList").val("");*/
			}
		});
	});
	
	// select박스에서 테이블 선택 시 해당 테이블 내 컬럼 가져오기(insert)
	$("#tableList2").change(function(){
		$("#columnList2").empty();
		$("#selectedColumn2").empty();
		$("#selectedColumn3").empty();
		var dbName = $("#dbName").val();
		var table_name = $("#tableList2").val();
		$.ajax({
			url : contextPath + "/selectColumnProc",
			data : {
				dbName : dbName,
				table_name : table_name
			},
			success : function(result){
				$(JSON.parse(result)).each(function(index, column){
					$("#columnList2").append('<option value="'+column.column_name+'">'+column.column_name+'</option>');
				});
			}
		});
	});
	
	// 선택한 컬럼 리스트(selectedColumn2)에 추가(insert_key)
	$("#addColumnList2").click(function(){
		var column_name = $("#columnList2").val();
		if(column_name==null){
			alert("null");
		}else{
			//중복체크
			for(var i = 0 ; i < $("#selectedColumn2 option").length ; i++){
				if($("#selectedColumn2 option")[i].text==column_name){
					alert("already");
					return;
				}
			}
			for(var i = 0 ; i < $("#selectedColumn3 option").length ; i++){
				if($("#selectedColumn3 option")[i].text==column_name){
					alert("already");
					return;
				}
			}
			$("#selectedColumn2").append('<option value="'+column_name+'">'+column_name+'</option>');
			onUpsertSQLContentProc();
		}
	});
	
	// 선택한 컬럼 리스트(selectedColumn3)에 추가(insert_normal)
	$("#addColumnList3").click(function(){
		var column_name = $("#columnList2").val();
		if(column_name==null){
			alert("null");
		}else{
			//중복체크
			for(var i = 0 ; i < $("#selectedColumn2 option").length ; i++){
				if($("#selectedColumn2 option")[i].text==column_name){
					alert("already");
					return;
				}
			}
			for(var i = 0 ; i < $("#selectedColumn3 option").length ; i++){
				if($("#selectedColumn3 option")[i].text==column_name){
					alert("already");
					return;
				}
			}
			$("#selectedColumn3").append('<option value="'+column_name+'">'+column_name+'</option>');
			onUpsertSQLContentProc()
		}
	});
	
	// 선택한 컬럼 리스트(selectedColumn2)에서 제거(insert)
	$("#takeAwayColumnList2").click(function(){
		var column_name = $("#selectedColumn2").val();
		if(column_name==null){
			alert("null");
		}else{
			for(var i = 0 ; i < column_name.length ; i++) {
				$("#selectedColumn2 option[value='"+column_name[i]+"']").remove();
			}
			if($("#selectedColumn2 option").length > 0){
				onUpsertSQLContentProc()
			}else{
				$("#sqlContent2").val("");
			}
		}
	});
	
	// 선택한 컬럼 리스트(selectedColumn3)에서 제거(insert)
	$("#takeAwayColumnList3").click(function(){
		var column_name = $("#selectedColumn3").val();
		if(column_name==null){
			alert("null");
		}else{
			for(var i = 0 ; i < column_name.length ; i++) {
				$("#selectedColumn3 option[value='"+column_name[i]+"']").remove();
			}
			onUpsertSQLContentProc()
		}
	});
	
	// upsert로 사용할 SQL 정보 저장
	$("#upsertSQL").on("click", function(){
		var table_name = $("#tableList2").val();
		var dbName = $("#dbName").val();
		var sqlName = $("#sqlName2").val(); // null처리, 중복처리 추가해야함
		var sqlDescription = $("#sqlDescription2").val();
		var column_list_key = new Array();
		for(var i = 0 ; i < $("#selectedColumn2 option").length ; i++){
			column_list_key.push($("#selectedColumn2 option")[i].text);
		}
		var column_list_normal = new Array();
		for(var i = 0 ; i < $("#selectedColumn3 option").length ; i++){
			column_list_normal.push($("#selectedColumn3 option")[i].text);
		}
		// 최소 1개의 키 선택 조건
		if(column_list_key == ""){
			alert("Contain at least one key.")
			return
		}
		$.ajax({
			url : contextPath + "/upsertSQLProc",
			data : {
				table_name : table_name,
				dbName : dbName,
				sqlName : sqlName,
				sqlDescription : sqlDescription,
				column_list_key : JSON.stringify(column_list_key),
				column_list_normal : JSON.stringify(column_list_normal)
			},
			success : function(result){
				location.reload();
			}
		});
	});
	
});

function onSelectSQLContentProc() {
	var dbName = $("#dbName").val();
	var table_name = $("#tableList").val();
	var column_list = new Array();
	for(var i = 0 ; i < $("#selectedColumn option").length ; i++){
		column_list.push($("#selectedColumn option")[i].text);
	}
	$.ajax({
		url : contextPath + "/selectSQLContentProc",
		data : {
			dbName : dbName,
			table_name : table_name,
			column_list : JSON.stringify(column_list)
		},
		success : function(result){
			$("#sqlContent").val(result.replace('"','').replace('"',''));
		}
	});
}

function onUpsertSQLContentProc() {
	var dbName = $("#dbName").val();
	var table_name = $("#tableList2").val();
	var column_list_key = new Array();
	for(var i = 0 ; i < $("#selectedColumn2 option").length ; i++){
		column_list_key.push($("#selectedColumn2 option")[i].text);
	}
	var column_list_normal = new Array();
	for(var i = 0 ; i < $("#selectedColumn3 option").length ; i++){
		column_list_normal.push($("#selectedColumn3 option")[i].text);
	}
	$.ajax({
		url : contextPath + "/upsertSQLContentProc",
		data : {
			dbName : dbName,
			table_name : table_name,
			column_list_key : JSON.stringify(column_list_key),
			column_list_normal : JSON.stringify(column_list_normal)
		},
		success : function(result){
			$("#sqlContent2").val(result.replace('"','').replace('"',''));
		}
	});
}
