// 전역변수
var userManageGrid // 그리드
var treeView;
var check=0;
addLoadEvent(userManageLoad);
//	로드 함수
function userManageLoad(){

	treeView = new dhtmlXTreeObject("treeViewAgent","100%","100%",0);
	treeView.attachEvent("onXLS", function(){
		check=check+1;
		//alert("트리시작"+check)
		progress.on()
	});
	treeView.setImagePath("../resources/component/dhtmlxSuite/skins/skyblue/imgs/dhxtree_skyblue/");
	//treeView.enableCheckBoxes(1);
	treeView.enableThreeStateCheckboxes(true);
	treeView.enableSmartXMLParsing(true);
	//treeView.enableDistributedParsing(true,10,10);
	treeView.load(contextPath+"/AgentTreeView.xml");

	treeView.attachEvent("onXLE", function(grid_obj,count){
		check=check-1;
		//alert("트리끝"+check)
		if(check==0)
			progress.off()
	});

	treeView.attachEvent("onClick",function(id){
		var uri=contextPath+"/userManageGrid.xml"+formToSerialize();
		userManageGrid.clearAndLoad(uri, function(){
			userManageGrid.changePage(1)
			if(modiYn != "Y"){
				$('.userModiBtn').hide();
			}
		})
	})


	userManageGrid = new dhtmlXGridObject("userManageGrid");
	userManageGrid.setIconsPath(recseeResourcePath + "/images/project/");
	userManageGrid.setImagePath(recseeResourcePath + "/images/project/");
	userManageGrid.i18n.paging = i18nPaging[locale];
	userManageGrid.enablePaging(true, 100, 5, "userManagePaging", true);
	userManageGrid.setPagingWTMode(true,true,true,[100,200,500]);
	userManageGrid.setPagingSkin("toolbar", "dhx_web");
	userManageGrid.enableColumnAutoSize(false);
	userManageGrid.setSkin("dhx_web");
	userManageGrid.init();

	userManageGrid.load(contextPath+"/userManageGrid.xml?header=true", function(){

		var search_toolbar = userManageGrid.aToolBar;

		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div style="width: 100%; text-align: center;">'+ userManageGrid.i18n.paging.results+" "+userManageGrid.getRowsNum() + '</div>')
		search_toolbar.setWidth("total",150)


		//필터 코드 변경
		selectOptionLoad($(userManageGrid.getFilterElement(8)), "authy", null,null,null,null,true);
		selectOrganizationLoad($(userManageGrid.getFilterElement(9)), "bgCode", null, null, null, null, null, true);


		search_toolbar.setMaxOpen("pages", 5);
		$(window).resize();
		userManageGrid.setSizes();
		$(window).resize(function() {
			userManageGrid.setSizes();
		});

		userManageGrid.attachEvent("onXLS", function(){
			check=check+1;
			//alert("그리드시작"+check)
			progress.on()
		});


		// 파싱완료
		userManageGrid.attachEvent("onXLE", function(grid_obj,count){
			var setResult = '<div style="width: 100%; text-align: center;">'+ userManageGrid.i18n.paging.results+ userManageGrid.getRowsNum()+userManageGrid.i18n.paging.found+'</div>'
			userManageGrid.aToolBar.setItemText("total", setResult);
			ui_controller();
			check=check-1;
			//alert("그리드끝"+check)
			if(check==0)
				progress.off();

			$(window).resize();
			userManageGrid.setSizes();
			$(window).resize(function() {
				userManageGrid.setSizes();
			});

		});
		var arr_hidden_aUserInfo = hidden_aUserInfo.split(",");
		for(var i = 0; i < arr_hidden_aUserInfo.length; i++) {
			switch(arr_hidden_aUserInfo[i]) {
				case "extNo":
					$(".telExtNo").hide();
					$("#mExtNum").hide();
					var extNoIndex = userManageGrid.getColIndexById("extNo");
					if(extNoIndex != undefined){
						userManageGrid.setColumnHidden(extNoIndex,true); // 컬럼 안보이게
					}
					break;
				case "userPhone":
					$(".telPhoneNum").hide();
					$("#mPhoneNumber").hide();
					var userPhoneIndex = userManageGrid.getColIndexById("userPhone");
					if(userPhoneIndex != undefined){
						userManageGrid.setColumnHidden(userPhoneIndex,true); // 컬럼 안보이게
					}
					break;
				case "userSex":
					$(".telSex").hide();
					$("#mSex").hide();
					var userSexIndex = userManageGrid.getColIndexById("userSex");
					if(userSexIndex != undefined){
						userManageGrid.setColumnHidden(userSexIndex,true); // 컬럼 안보이게
					}
					break;
				case "userEmail":
					$(".telEmail").hide();
					$("#mEmail").hide();
					var userEmailIndex = userManageGrid.getColIndexById("userEmail");
					if(userEmailIndex != undefined){
						userManageGrid.setColumnHidden(userEmailIndex,true); // 컬럼 안보이게
					}
					break;
			}
		}
		if(modiYn != "Y"){
			$('.userModiBtn').hide();
		}
		// 페이지 체인지 이벤트
		userManageGrid.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum())
				return false;
			return true;
		});

		userManageGrid.attachEvent("onRowDblClicked", function(id,cInd){
			modifyUser(id)
		});

		// 소팅 이벤트 커스텀
		/*userManageGrid.attachEvent("onBeforeSorting", function(ind){

			var a_state = this.getSortingState()

			var direction = ((a_state[1] == "des") ? "asc" : "desc")

			var columnId = this.getColumnId(ind)
			var requestOrderby = columnId+"|"+direction;

			var nowPage = this.getStateOfView()[0]

			this.clearAndLoad(contextPath+"/log_list.xml?" + formSirealize(requestOrderby));
			this.setSortImgState(true,ind,direction)
		});*/

		search_toolbar.addSpacer("perpagenum");

	progress.off();
	}, 'xml');
	ui_controller();
	gridFunction();
	//loadTree();
	formFunction();
	
	if(telnoUse=='Y'){
		$(".group_tree_pannel").hide()
		$(".telBg").hide();
		$("#mBgCode").hide();
		$(".telMg").hide();
		$("#mMgCode").hide();
		$(".telSg").hide();
		$("#mSgCode").hide();
		$(".input_userid").hide();
		$(".telPhoneNum").hide();
		$("#mPhoneNumber").hide();
		$(".telSex").hide();
		$("#mSex").hide();
		
		
		userManageGrid.clearAndLoad(contextPath+"/userManageGrid.xml")
		progress.off()
	}
	
}

//사용자 수정 팝업창
function modifyUser(rowId){
	var userId = 		userManageGrid.cells(rowId,3).getValue()	// 아이디
	var userName = 		userManageGrid.cells(rowId,2).getValue()	// 이름
	var password = 		userManageGrid.cells(rowId,4).getValue()	// 비번
	var passwordCheck = userManageGrid.cells(rowId,4).getValue()	// 비번확인
	var extNum = 		userManageGrid.cells(rowId,5).getValue()	// 내선
	var authy = 		userManageGrid.cells(rowId,8).getValue()	// 권한
	var phoneNumber = 	userManageGrid.cells(rowId,6).getValue()	// 폰버노
	var sex = 			userManageGrid.cells(rowId,7).getValue()	// 성별
	var bgCode = 		userManageGrid.cells(rowId,16).getValue()	// 대
	var mgCode = 		userManageGrid.cells(rowId,17).getValue()	// 중
	var sgCode = 		userManageGrid.cells(rowId,18).getValue()	// 소
	var empId = 		userManageGrid.cells(rowId,12).getValue()	// 사번
	var email =			userManageGrid.cells(rowId,13).getValue()	// 이멜
	var ctiId = 		userManageGrid.cells(rowId,14).getValue()	// cti id

	$("#mUserId").val(userId).addClass("ui_input_hasinfo").attr("readonly",true)// 아이디
	$("#mUserName").val(userName)							// 이름
	$("#mPassword").val("")							// 비번
	$("#mPasswordCheck").val("")					// 비번확인
	$("#mExtNum").val(extNum)								// 내선
	$("#mAuthy").val(authy)									// 권한
	$("#mPhoneNumber").val(phoneNumber)						// 폰버노
	$("#mSex").val(sex)										// 성별

	$("#mBgCode").val(bgCode)								// 대
	selectOrganizationLoad($("#mMgCode"), "mgCode", $("#mBgCode").val(), "", mgCode,"","",'Y')					// 중
	selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $("#mMgCode").val(), sgCode,"","",'Y') 	// 소

	$("#mEmpId").val(empId)									// 사번
	$("#mEmail").val(email)									// 이멜
	$("#mCtiId").val(ctiId)									// cti id

	$("#addUser .ui_pannel_tit").text(lang.admin.alert.recUser30)
	$("#mUserAddBtn").hide();
	$("#mUserModifyBtn").show();

	$("#rowId").val(rowId);
	
	layer_popup('#addUser');
}

//	그리드 적용 함수
function gridFunction(){

}

// 폼 함수
function formFunction(){
	$('.main_form').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	})

	$("#searchBtn").click(function(event){
		if(telnoUse == 'Y'){
			userManageGrid.clearAndLoad(contextPath+"/userManageGrid.xml"+formToSerialize())
			progress.off()
		}else{
			if(treeView.getSelected()==null || treeView.getSelected()==''){
				alert(lang.admin.alert.userManage1)
			}else{
				userManageGrid.clearAndLoad(contextPath+"/userManageGrid.xml"+formToSerialize())
				progress.off()
			}
		}

	});
// 셀렉트 옵션값 셋팅
	// 내선
	//selectOptionLoad($("#mExtNum"), "channel")
	// 권한
	selectOptionLoad($("#mAuthy"), "authy","","","","","all")
	// 대분류
	selectOrganizationLoad($("#mBgCode"), "bgCode","","","","","notCallCenter","Y")

	// 대분류 변화시 중분류 로드
	$( "#mBgCode" ).change(function() {
		selectOrganizationLoad($("#mMgCode"), "mgCode", $(this).val(),"","","","","Y")
		// 중분류 및의 소분류가 있다면 디폴트로 로드
		selectOrganizationLoad($("#mSgCode"), "sgCode", $(this).val(), $("#mMgCode").val(),"","","","Y")
	});

	// 중분류 변화시 소분류 로드
	$( "#mMgCode" ).change(function() {
		selectOrganizationLoad($("#mSgCode"), "sgCode", $("#mBgCode").val(), $(this).val(),"","","","Y")
	});


	// 추가
	$("#mUserAddBtn").click(function(){
		insertUserInfo();
	})

	// 수정
	$("#mUserModifyBtn").click(function(){
		modifyUserInfo();
	})

	// 삭제
	$("#userDeleteBtn").click(function(){
		deleteUserInfo();
	})

	// 사용자 추가 팝업 열기
	$("#userAddBtn").click(function(){
		$("#addUser .ui_pannel_tit").text(lang.admin.alert.userManage2)
		$("#mUserAddBtn").show();
		$("#mUserModifyBtn").hide();

		$("#mUserId").val("").removeClass("ui_input_hasinfo").attr("readonly",false)   // 아이디
		$("#mUserName").val("")									// 이름
		$("#mPassword").val("")									// 비번
		$("#mPasswordCheck").val("")							// 비번확인
		$("#mExtNum :nth-child(1)").prop('selected', true);		// 내선
		$("#mAuthy").val("")									// 권한
		$("#mPhoneNumber").val("")								// 폰버노
		$("#mSex :nth-child(1)").prop('selected', true);		// 성별
		$("#mBgCode").val("")									// 대
		$("#mMgCode").val("")									// 중
		$("#mSgCode").val("")									// 소
		$("#mEmpId").val("")									// 사번
		$("#mEmail").val("")									// 이멜
		$("#mCtiId").val("")									// cti id

		layer_popup('#addUser');
	})

	// *****************************************************************************
	// 비밀버노 정책 관련 ㅎㅎ

	// 정책 설정 팝업 버튼
	$("#pwPolicyBtn").click(function(){
		loadPwPolicy()
		layer_popup('#passwordManage');
	})

	// 정책 설정 적용 버튼
	$("#modifyPwPolicy").click(function(){
		modifyPwPolicy()
	})
	// PW 정책 사용
	$( "#pwPolicy" ).change(function() {

		$("#cyclePolicy").change();
		$("#pastPolicy").change();

		if($(this).children(":selected").attr("value") == "Y")
			$(".policyGroup").removeClass("ui_input_hasinfo").removeAttr("disabled");
		else
			$(".policySubGroup, .policyGroup").addClass("ui_input_hasinfo").attr({"disabled":"disabled"});

	});

	// 변경 주기 사용
	$( "#cyclePolicy" ).change(function() {
		if($(this).children(":selected").attr("value") == "Y" || $(this).children(":selected").attr("value") == "F")
			$(".cyclePolicy").removeClass("ui_input_hasinfo").removeAttr("disabled");
		else
			$(".cyclePolicy").addClass("ui_input_hasinfo").attr({"disabled":"disabled"});
	});

	// 이전 pw 허용
	$( "#pastPolicy" ).change(function() {
		if($(this).children(":selected").attr("value") == "C")
			$("#pastNum").removeClass("ui_input_hasinfo").removeAttr("disabled");
		else
			$("#pastNum").addClass("ui_input_hasinfo").attr({"disabled":"disabled"});
	});

	// 시도제한 변경
	$("#tryPolicy").change(function() {
		if($(this).children(":selected").attr("value") == "Y")
			$("#tryNum").removeClass("ui_input_hasinfo").removeAttr("disabled");
		else
			$("#tryNum").addClass("ui_input_hasinfo").attr({"disabled":"disabled"});
	});

	// 계정 잠금
	$("#lockPolicy").change(function() {
		if($(this).children(":selected").attr("value") == "Y"){
			$("#lockNum").removeClass("ui_input_hasinfo").removeAttr("disabled");
			$("#lockDate").removeClass("ui_input_hasinfo").removeAttr("disabled");
		}
		else {
			$("#lockNum").addClass("ui_input_hasinfo").attr({"disabled":"disabled"});
			$("#lockDate").addClass("ui_input_hasinfo").attr({"disabled":"disabled"});
		}
	});

	// 메인 그룹 추가 버튼
	$("#btnGroupAdd").click(function(){
		$("#addGroup").find("p").text(lang.admin.alert.recUser32/*"그룹 추가"*/);
		$("#seletCode, #bgGroup, #mgGroup").removeAttr("disabled").removeClass("ui_input_hasinfo");

		$("#seletCode").val("bgCode").change();

		$("#groupAddBtn").show();
		$("#groupModifyBtn").hide();
		$("#addGroup").data("bgCode","");
		
		layer_popup('#addGroup');
	});

	// 메인 그룹 수정 버튼
	$("#btnGroupModify").click(function(){

		$("#addGroup").find("p").text(lang.admin.alert.userManage3)

		var activeObj;
		var state;
		var bgCode;
		var mgCode;
		var sgCode;
		var groupName = treeView.getSelectedItemText();
		
		if(treeView.getSelected() != null && treeView.getSelected() != '' && treeView.getSelected() != "all"){
			var level = treeView.getLevel(treeView.getSelected());
			
			switch(level){
			case 2:
				bgCode = treeView.getSelected().split("_")[0];
				state = "bgCode";
				break;
			case 3:
				mgCode = treeView.getSelected().split("_")[0];
				bgCode = treeView.getParentId(treeView.getSelected().split("_")[0]);
				state = "mgCode";
				break;
			case 4:
				sgCode = treeView.getSelected().split("_")[0];
				mgCode = treeView.getParentId(treeView.getSelected().split("_")[0]);
				bgCode = treeView.getParentId(treeView.getParentId(treeView.getSelected().split("_")[0]));
				state = "sgCode";
				break;
			}			
		}else{
			alert(lang.admin.alert.recUser38/*"수정하실 그룹을 선택해주세요."*/);
			return;
		}
		
		$("#seletCode").val(state).change()
		$("#bgGroup").val(bgCode).change()
		$("#mgGroup").val(mgCode).change()

		$("#seletCode, #bgGroup, #mgGroup").attr("disabled","disabled").addClass("ui_input_hasinfo");
		$("#groupName").val(groupName);

		$("#groupAddBtn").hide();
		$("#groupModifyBtn").show();

		$("#addGroup").data("bgCode",bgCode)
		$("#addGroup").data("mgCode",mgCode)
		$("#addGroup").data("sgCode",sgCode)
		layer_popup('#addGroup');
		
//		var activeObj = $(".tree_user_part_active > .folder")
//		var state = activeObj.attr("state")
//		var bgCode = activeObj.attr("bg-code")
//		var mgCode = activeObj.attr("mg-code")
//		var sgCode = activeObj.attr("sg-code")
//		var groupName = activeObj.text()
//
//		$("#seletCode").val(state).change()
//		$("#bgGroup").val(bgCode).change()
//		$("#mgGroup").val(mgCode).change()
//
//		$("#seletCode, #bgGroup, #mgGroup").attr("disabled","disabled").addClass("ui_input_hasinfo");
//		$("#groupName").val(groupName);
//
//		$("#groupAddBtn").hide();
//		$("#groupModifyBtn").show();
//
//		$("#addGroup").data("bgCode",bgCode)
//		$("#addGroup").data("mgCode",mgCode)
//		$("#addGroup").data("sgCode",sgCode)
//		layer_popup('#addGroup');

	});
	
	// 메인 그룹 삭제
	$("#btnGroupDelete").click(function(){
		
		var state;
		var bgCode;
		var mgCode;
		var sgCode;
		var groupName = treeView.getSelectedItemText();
		
		if(treeView.getSelected() != null && treeView.getSelected() != '' && treeView.getSelected() != "all"){
			var level = treeView.getLevel(treeView.getSelected());
			
			switch(level){
			case 2:
				bgCode = treeView.getSelected().split("_")[0];
				state = "bgCode";
				break;
			case 3:
				mgCode = treeView.getSelected().split("_")[0];
				bgCode = treeView.getParentId(treeView.getSelected().split("_")[0]);
				state = "mgCode";
				break;
			case 4:
				sgCode = treeView.getSelected().split("_")[0];
				mgCode = treeView.getParentId(treeView.getSelected().split("_")[0]);
				bgCode = treeView.getParentId(treeView.getParentId(treeView.getSelected().split("_")[0]));
				state = "sgCode";
				break;
			}
			
			
			if(confirm(lang.admin.alert.recUser39/*"선택하신 그룹을 삭제하시겠습니까?"*/)){
				$("#seletCode").val(state).change()
				$("#bgGroup").val(bgCode).change()
				$("#mgGroup").val(mgCode).change()

				$("#seletCode, #bgGroup, #mgGroup").attr("disabled","disabled").addClass("ui_input_hasinfo");
				$("#groupName").val(groupName);

				$("#groupAddBtn").hide();
				$("#groupModifyBtn").show();

				$("#addGroup").data("bgCode",bgCode)
				$("#addGroup").data("mgCode",mgCode)
				$("#addGroup").data("sgCode",sgCode)
				
				procGroup("delete");
			}
		}else{
			alert(lang.admin.alert.recUser40/*"삭제하실 그룹을 선택해주세요."*/);
			return;
		}
	});

	// 팝업 그룹 추가 버튼
	$("#groupAddBtn").click(function(){
		procGroup("insert");
	});

	// 팝업 그룹 수정 버튼
	$("#groupModifyBtn").click(function(){
		procGroup("modify")
	});

	// 코드 선택시 parent 코드 로드
	$("#seletCode").change(function(){
		var code = $(this).val();

		switch(code){
		case "bgCode":
			$("#bgGroup, #mgGroup").empty().attr("disabled","disabled").off('change').addClass("ui_input_hasinfo");
			break;
		case "mgCode":
			$("#bgGroup").off('change').removeAttr("disabled").removeClass("ui_input_hasinfo");
			$("#mgGroup").empty().attr("disabled","disabled").addClass("ui_input_hasinfo");
			selectOrganizationLoad($("#bgGroup"), "bgCode","","","","","notCallCenter","empty")
			break;
		case "sgCode":
			$("#bgGroup, #mgGroup").empty().removeAttr("disabled").removeClass("ui_input_hasinfo");
//			selectOrganizationLoad($("#bgGroup"), "bgCode")
			selectOrganizationLoad($("#bgGroup"), "bgCode","","","","","notCallCenter","empty")
			// 대분류 변화시 중분류 로드
			$( "#bgGroup" ).on('change', function() {
				selectOrganizationLoad($("#mgGroup"), "mgCode",$(this).val(),"","","","notCallCenter","empty")
			});
			// 대분류
			$("#bgGroup").change()
			break;
		}

	});

	// 권한 추가 메인 버튼
	$("#authyAddBtn").click(function(){
		$("#addGroup").find("p").text(lang.admin.alert.userManage4)

		$("#modifyGroupBtn").hide();
		$("#addGroupBtn").show();
		layer_popup('#addJuri');
	});
	// 권한 수정 메인 버튼
	$("#authyModifyBtn").click(function(){
		$("#addGroup").find("p").text(lang.admin.alert.userManage5)

		$("#modifyGroupBtn").show();
		$("#addGroupBtn").hide();
		layer_popup('#addJuri');
	});
	// 권한 삭제 메인 버튼
	$("#authyDeleteBtn").click(function(){
		var level = $(".group_name_wrap_active").attr("level-code");
		var param = "proc=delete&levelCode=" + level

		if($(".group_name_wrap_active").length == 0){
			alert(lang.admin.alert.recUser4)
			return
		}

		if(confirm(lang.admin.alert.recUser5)){
			$.ajax({
				url:contextPath+"/access_proc.do",
				data:param,
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					// DB에 조회한 계정이 있으면
					if(jRes.success == "Y") {
						// 불러온 옵션 추가
						alert(lang.admin.alert.recUser6)
						loadAuthy()
						layer_popup_close()
					}else{
						alert(lang.admin.alert.recUser7)
					}
				}
			});
		}
	});
}

/*//좌측 트리 리스트 로드
function loadTree(){

	$("#browser").empty();
	$.ajax({
		url:contextPath+"/getTreeList.do?aUser=Y",
		type:"get",
		data:{},
		dataType:"json",
		async: false,
		success: function(jRes) {
			if(jRes.success =="Y") {
				var bgList = jRes.resData.bgList;
				var mgList = jRes.resData.mgList;
				var sgList = jRes.resData.sgList;
				var userList = jRes.resData.userList;
				var bgListString = "";

				// bg로드
				for (var i = 0 ; i < bgList.length ; i++){
					var bgCode = bgList[i].rBgCode
					var bgName = bgList[i].rBgName
					bgListString ='<li><span state=bgCode bg-code="'+bgCode+'" class="folder">'+bgName+'</span><ul></ul></li>';
					$("#browser").append(bgListString);
				}

				mgLoadTree();
				$("#browser").treeview({ collapsed: true });
			}
		}
	});
}*/

function mgLoadTree(){
	$('[state=bgCode]').parents("li").click(function(event){
			event.stopPropagation();
			event.stopImmediatePropagation();
			$(document).find("li").removeClass('tree_user_part_active');
			$(this).addClass('tree_user_part_active');
			var bgCode = $(this).find("span").attr('bg-code');
			//$('.tree_user_part_active').removeClass('tree_user_part_active');
			// Active 활성화
//			$(this).parents("li").addClass('tree_user_part_active');

//			$(document).find('.tree_user_part_active').removeClass('tree_user_part_active');
//			// Active 활성화
//			$(this).parents("li").addClass('tree_user_part_active');
//			$(this).parents("li").find("ul").css("display","");
//
			if($(this).hasClass("expandable")){
				$(this).find('ul').css("display","none");
			}else{
				$.ajax({
					url:contextPath+"/getTreeList.do",
					type:"get",
					data:{
						bgCode : bgCode
					},
					dataType:"json",
					async: false,
					success: function(jRes) {
						if(jRes.success =="Y") {
							var mgList = jRes.resData.mgList;
							var mgListString = "";
							for (var i = 0 ; i < mgList.length ; i++){
								var mgCode = mgList[i].rMgCode;
								var mgName = mgList[i].rMgName;
								mgListString += '<li class="expandable"><span state="mgCode"  bg-code="'+bgCode+'" mg-code="'+mgCode+'" class="folder">'+mgName+'</span><ul></ul></li>'
							}
							$("[bg-code='"+bgCode+"'][state='bgCode']").next().html("");
							$("[bg-code='"+bgCode+"'][state='bgCode']").next().append(mgListString);
							$("#browser").treeview({ collapsed: false });
							sgLoadTree();
							TreeEvent();
						}
					}
				});
			}
	});
}

function sgLoadTree(){
	$('[state=mgCode]').parents("li").click(function(event){
		event.stopPropagation();
		event.stopImmediatePropagation();
		$(document).find("li").removeClass('tree_user_part_active');
		$(this).addClass('tree_user_part_active');

		var bgCode = $(this).find('span').attr('bg-code');
		var mgCode = $(this).find('span').attr('mg-code');
		var sgCode = $(this).find('span').attr('sg-code');

		if($(this).hasClass("expandable")){
			$(this).find('ul').css("display","none");
		}else{
			$.ajax({
				url:contextPath+"/getTreeList.do",
				type:"get",
				data:{
					mgCode : mgCode
				},
				dataType:"json",
				async: false,
				success: function(jRes) {
					if(jRes.success =="Y") {
						$(this).find('ul').css("display","");
						var sgList = jRes.resData.sgList;
						var sgListString = "";
						for (var i = 0 ; i < sgList.length ; i++){
							var bgCode = sgList[i].rBgCode
							var mgCode = sgList[i].rMgCode
							var sgCode = sgList[i].rSgCode
							var sgName = sgList[i].rSgName
							sgListString +=
								'<li><span state=sgCode bg-code="'+bgCode+'" mg-code="'+mgCode+'" sg-code="'+sgCode+'"class="folder">'+sgName+'</span><ul class="tree_user_part"></ul></li>';
						}
						$("[mg-code='"+mgCode+"'][state='mgCode']").next().html("");
						$("[mg-code='"+mgCode+"'][state='mgCode']").next().append(sgListString);

						//userLoadTree();
						TreeEvent();
					}
				}
			});
		}
	})
}

function userLoadTree(){
	$('[state=sgCode]').parents("li").click(function(event){
		event.stopPropagation();
		event.stopImmediatePropagation();

		$(document).find("li").removeClass('tree_user_part_active');
		$(this).addClass('tree_user_part_active');

		var bgCode = $(this).find('span').attr('bg-code');
		var mgCode = $(this).find('span').attr('mg-code');
		var sgCode = $(this).find('span').attr('sg-code');

		$.ajax({
			url:contextPath+"/getTreeList.do",
			type:"get",
			data:{
					sgCode : sgCode
				,	aUser : "Y"
			},
			dataType:"json",
			async: false,
			success: function(jRes) {
				if(jRes.success =="Y") {
					var userList = jRes.resData.userList;
					var userListString = "";
					var bgCode,mgCode,sgCode;

					for (var i = 0 ; i < userList.length ; i++){
						bgCode = userList[i].bgCode
						mgCode = userList[i].mgCode
						sgCode = userList[i].sgCode
						var userId = userList[i].userId
						var userName = userList[i].userName
						userListString += 	'<li><span state=agent user-id="'+userId+'" class="system_group_user">'+userName+'</span></li>';
					}
					$("[bg-code='"+bgCode+"'][mg-code='"+mgCode+"'][sg-code='"+sgCode+"'][state='sgCode']").next().html("");
					$("[bg-code='"+bgCode+"'][mg-code='"+mgCode+"'][sg-code='"+sgCode+"'][state='sgCode']").next().append(userListString);
					TreeEvent();
				}
			}
		});
	})
}


//사용자 정보 수정
function modifyUserInfo(){
	var userId = $("#mUserId").val()						// 아이디
	var userName = $("#mUserName").val()					// 이름
	var password = $("#mPassword").val()					// 비번
	var passwordCheck = $("#mPasswordCheck").val()			// 비번확인
	var extNum = $("#mExtNum").val()						// 내선
	var authy = $("#mAuthy").val()							// 권한
	var phoneNumber = $("#mPhoneNumber").val()				// 폰버노
	var sex = $("#mSex").children(":selected").attr("id");	// 성별
	var bgCode = $("#mBgCode").val()						// 대
	var mgCode = $("#mMgCode").val()						// 중
	var sgCode = $("#mSgCode").val()						// 소
	if(telnoUse=='Y'){
		bgCode = 'B001';								// 대
		mgCode = 'M001';								// 중
		sgCode = 'S001';								// 소
	}
	var empId = $("#mEmpId").val()							// 사번
	var email = $("#mEmail").val()							// 이멜
	var ctiId = $("#mCtiId").val()							// cti id
	
	var rowId = $("#rowId").val()							// 현재 열린 팝업 rowId
	
	var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/; // 이메일 정규식

	var isChangedChk = false;
	
	var authyName = "";
	var bgName = "";
	var mgName = "";
	var sgName = "";

	if(!userId){
		alert(lang.admin.alert.recUser8)
		$("#mUserId").focus();
		return;
	}
	if(!userName){
		alert(lang.admin.alert.recUser9)
		$("#mUserName").focus();
		return;
	}
	if(password){
		if(!passwordCheck){
			alert(lang.admin.alert.recUser33)
			$("#mPasswordCheck").focus();
			return;
		}
	}
	if(password != passwordCheck){
		alert(lang.admin.alert.recUser29)
		$("#mPasswordCheck").focus();
		return;
	}

	if (authy) {
		authyName = $("#mAuthy").children(":selected").text();
	}
	if (bgCode) {
		bgName = $("#mBgCode").children(":selected").text();
	}
	if (mgCode) {
		mgName = $("#mMgCode").children(":selected").text();
	}
	if (sgCode) {
		sgName = $("#mSgCode").children(":selected").text();
	}
	
	if(email){
		if(!email.match(regExp)){
			alert(lang.admin.alert.recUser13 + "\nEx)furence@furence.com")
			$("#mEmail").focus();
			return;
		}
	}

	if(authy != userManageGrid.cells(rowId,8).getValue()) {
		isChangedChk = true;
	}
	if(bgCode != userManageGrid.cells(rowId,16).getValue()) {
		isChangedChk = true;
	}
	if(mgCode != userManageGrid.cells(rowId,17).getValue()) {
		isChangedChk = true;
	}
	if(sgCode != userManageGrid.cells(rowId,18).getValue()) {
		isChangedChk = true;
	}
	/*if(!authy){
		alert(lang.admin.alert.recUser11)
		$("#mAuthy").focus();
		return;
	}*/
	/*if(!sex){
		alert("성별을 선택 해 주세요")
		return;
	}*/
	/*if(!bgCode){
		alert("대분류를 선택 해 주세요")
		return;
	}
	if(!mgCode){
		alert("중분류를 선택 해 주세요")
		return;
	}
	if(!sgCode){
		alert("소분류를 선택 해 주세요")
		return;
	}*/

	if (!isChangedChk) {
		alert(lang.admin.alert.userManage8);
		return;
	}
	
	var dataStr = {
			"userId" : userId
		,	"userName" : userName
		,	"password" : password
		,	"extNum" : extNum
		,	"authy" : authy
		,	"authyName" : authyName
		,	"phoneNumber" : phoneNumber
		,	"sex" : sex
		,	"bgCode" : bgCode
		,	"sgCode" : sgCode
		,	"mgCode" : mgCode
		,	"bgName" : bgName
		,	"sgName" : sgName
		,	"mgName" : mgName
		,	"empId" : empId
		,	"email" : email
		,	"ctiId" : ctiId
		,  "aUser": "Y"
	}
	
	$.ajax({
		url:contextPath+"/updateUserInfo.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.recUser34);
				layer_popup_close();
				//loadTree();
				var uri=encodeURI(contextPath+"/userManageGrid.xml")
				userManageGrid.clearAndLoad(uri, function(){
					if(modiYn != "Y"){
						$('.userModiBtn').hide();
					}
				})
				progress.off()
			}else if(jRes.success == "N" && jRes.result == "PASSWORD PATTERN IS MISS MATCH"){
				alert(jRes.resData.msg);
				$("#mPassword").focus();
			}else if(jRes.success == "N" && jRes.result == "PASSWORD IS USED"){
				alert(lang.admin.alert.recUser27)
				$("#mPassword").focus();
			}else{
				alert(lang.admin.alert.userManage7);
			}
		}
	});
}

/**
 * 사용자 정보 추가
 */
function insertUserInfo(){
	var userId = $("#mUserId").val()						// 아이디
	var userName = $("#mUserName").val()					// 이름
	var password = $("#mPassword").val()					// 비번
	var passwordCheck = $("#mPasswordCheck").val()			// 비번확인
	var extNum = $("#mExtNum").val()						// 내선
	var authy = $("#mAuthy").val()							// 권한
	var phoneNumber = $("#mPhoneNumber").val()				// 폰버노
	var sex = $("#mSex").children(":selected").attr("id");	// 성별
	var bgCode = $("#mBgCode").val()						// 대
	var mgCode = $("#mMgCode").val()						// 중
	var sgCode = $("#mSgCode").val()						// 소
	if(telnoUse=='Y'){
		bgCode = 'B001';								// 대
		mgCode = 'M001';								// 중
		sgCode = 'S001';								// 소
	}
	var empId = $("#mEmpId").val()							// 사번
	var email = $("#mEmail").val()							// 이멜
	var ctiId = $("#mCtiId").val()							// cti id
	var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/; // 이메일 정규식

	var authyName = "";
	var bgName = "";
	var mgName = "";
	var sgName = "";

	if (authy) {
		authyName = $("#mAuthy").children(":selected").text();
	}
	if (bgCode) {
		bgName = $("#mBgCode").children(":selected").text();
	}
	if (mgCode) {
		mgName = $("#mMgCode").children(":selected").text();
	}
	if (sgCode) {
		sgName = $("#mSgCode").children(":selected").text();
	}
	
	var dataStr = {
				"userId" : userId
			,	"userName" : userName
			,	"password" : password
			,	"extNum" : extNum
			,	"authy" : authy
			,	"authyName" : authyName
			,	"phoneNumber" : phoneNumber
			,	"sex" : sex
			,	"bgCode" : bgCode
			,	"sgCode" : sgCode
			,	"mgCode" : mgCode
			,	"bgName" : bgName
			,	"sgName" : sgName
			,	"mgName" : mgName
			,	"empId" : empId
			,	"email" : email
			,	"ctiId" : ctiId
			,  "aUser": "Y"
	}

	if(!userId){
		alert(lang.admin.alert.recUser8)
		$("#mUserId").focus();
		return;
	}
	if(!userName){
		alert(lang.admin.alert.recUser9)
		$("#mUserName").focus();
		return;
	}
//	if(!password){
//		alert("비밀번호를 입력 해 주세요!")
//		$("#mPassword").focus();
//		return;
//	}
//	if(!passwordCheck){
//		alert(lang.admin.alert.recUser33)
//		$("#mPasswordCheck").focus();
//		return;
//	}
//	if(password != passwordCheck){
//		alert(lang.admin.alert.recUser29)
//		$("#mPasswordCheck").focus();
//		return;
//	}
	/*if(!authy){
		alert(lang.admin.alert.recUser11)
		$("#mAuthy").focus();
		return;
	}*/
	/*if(!sex){
		alert("성별을 선택 해 주세요")
		return;
	}*/
	/*if(!bgCode){
		alert("대분류를 선택 해 주세요")
		return;
	}
	if(!mgCode){
		alert("중분류를 선택 해 주세요")
		return;
	}
	if(!sgCode){
		alert("소분류를 선택 해 주세요")
		return;
	}*/
	if(email && !email.match(regExp)){
		alert(lang.admin.alert.recUser13 + "\nEx)furence@furence.com")
		$("#mEmail").focus();
		return;
	}

	// 중복 처리
	if(userManageGrid.getRowsNum() > 0 ) {
		userManageGrid.forEachRow(function(id){
			if(userId == this.cells(id,2).getValue()) {
				alert(lang.admin.alert.recUser16);
				$('#mUserId').focus();
				return;
			}
		});
	}

	$.ajax({
		url:contextPath+"/insertUserInfo.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.recUser15)
				layer_popup_close();
				//loadTree();
				userManageGrid.clearAndLoad(contextPath+"/userManageGrid.xml", function(){
					if(modiYn != "Y"){
						$('.userModiBtn').hide();
					}
				});
				progress.off()
			}else if(jRes.success == "N" && jRes.result == "PASSWORD PATTERN IS MISS MATCH"){
				alert(jRes.resData.msg);
				$("#mPassword").focus();
			}else if(jRes.success == "N" && jRes.result == "THIS EMPLOYEE NUMBER IS ALREADY USED"){
				alert(lang.admin.alert.recUser16 + "\nError : "+jRes.Results);
			}else{
				alert(lang.admin.alert.recUser17 + "\nError : "+jRes.Results);
			}
		}
	});
}


//
//
//
//function treevi(){
//	// 해당 함수를 호출안할시, 트리기능이 동작을 안함
//	//$("#browser").treeview({ collapsed: true });
//	// 클릭 시 Active 적용
//	$("#browser").find("li").on("click",function(event){
//		//event.stopPropagation();
//		//event.stopImmediatePropagation();
//		// 초기화
//		$('.tree_user_part_active').removeClass('tree_user_part_active');
//
//		// Active 활성화
//		$(this).addClass('tree_user_part_active');
//
//		// 트리 클릭 시 우측 사용자 그리드 필터링 적용
//		var treeDepth = $(this).children("span").attr("state")
//		var columnNum = 0;
//		var clickedId = 0;
//		// 클릭시 뎁스에 따라 맞는 컬럼 강제 필터링
//		switch(treeDepth){
//		case "bgCode":
//			columnNum = 16
//			clickedId = $(this).children("span").attr("bg-code");
//			break;
//		case "mgCode":
//			columnNum = 17
//			clickedId = $(this).children("span").attr("mg-code");
//			break;
//		case "sgCode":
//			columnNum = 18
//			clickedId = $(this).children("span").attr("sg-code");
//			break;
//		case "agent":
//			columnNum = 3
//			clickedId = $(this).children("span").attr("user-id");
//			break;
//		}
//		userManageGrid.filterBy(columnNum,clickedId)
//	});
//
//	// 다른 곳 클릭시 active 해제
//	/*$("body").click(function() {
//	    $('.tree_user_part li').removeClass("tree_user_part_active");
//	});*/
//}

function procGroup(proc){

	var code = $("#seletCode").val();
	var bgCode = ($("#bgGroup").val()  ? $("#bgGroup").val() : $("#addGroup").data("bgCode") );
	var mgCode = ($("#mgGroup").val()  ? $("#mgGroup").val() : $("#addGroup").data("mgCode") );
	var sgCode = $("#addGroup").data("sgCode");
	var groupName = $("#groupName").val();

	dataStr = {
			"level": level
			,	"proc" : proc
			,	"classBgName" : groupName
			,	"classMgName" : groupName
			,	"classSgName" : groupName
			,	"classBgCode" : bgCode
			,	"classMgCode" : mgCode
			,	"classSgCode" : sgCode
			,	"groupType"   : "user"

	}

	var level = 1 ;
	switch(code){
	case "bgCode":
		dataStr["level"] = 1
		delete dataStr["classMgName"]
		delete dataStr["classSgName"]
		delete dataStr["classMgCode"]
		delete dataStr["classSgCode"]
		break;
	case "mgCode":
		dataStr["level"] = 2
		delete dataStr["classBgName"]
		delete dataStr["classSgName"]
		delete dataStr["classSgCode"]
		break;
	case "sgCode":
		dataStr["level"] = 3
		delete dataStr["classBgName"]
		delete dataStr["classMgName"]
		break;
	}

	$.ajax({
		url:contextPath+"/group_proc.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {

				var string;
				switch(proc){
				case "insert":
					alert(lang.admin.alert.recUser35/*"그룹 추가에 성공하였습니다."*/);
					break;
				case "update":
					alert(lang.admin.alert.recUser36/*"그룹 수정에 성공하였습니다."*/);
					break;
				case "delete":
					alert(lang.admin.alert.recUser37/*"그룹 삭제에 성공하였습니다."*/);
					break;
				}
				treeView.deleteChildItems(0);
				treeView.load(contextPath+"/AgentTreeView.xml?aUser=Y")
				$("#groupName").val("");
				layer_popup_close()
			}
		}
	});
}

//권한 불러 오기
function authyLoad() {
	if(modiYn != "Y") {
		$('#btnGroupAdd').hide();
		$('#btnGroupModify').hide();
		$('#pwPolicyBtn').hide();
		$('#userAddBtn').hide();
		$('#authyAddBtn').hide();
		$('#authyModifyBtn').hide();
		$('#userAddBtn').hide();
	}

	if(delYn != 'Y') {
		$('#authyDeleteBtn').hide();
		$('#userDeleteBtn').hide();
		$('#btnGroupDelete').hide();
	}
}


//사용자 정보 삭제
function deleteUserInfo(){

	if(userManageGrid.getCheckedRows(0) != "") {
		if(confirm(lang.admin.alert.recUser19)){
		var checked = userManageGrid.getCheckedRows(0).split(",");
		var rstUserId = new Array();

		for( var  i = 0; i<checked.length; i++) {
			rstUserId.push(userManageGrid.cells(checked[i],3).getTitle());
//			rstUserId.push(userManageGrid.cells2(parseInt(checked[i])-1,3).getValue());
		}
		var rst = rstUserId.join(",");

		$.ajax({
			url:contextPath+"/deleteUserInfo.do?userList="+rst+"&aUser=Y",
			data:{},
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				if(jRes.success == "Y") {
					alert(lang.admin.alert.recUser20)
					$("#userId").val("");
					$("#userName").val("");
					userManageGrid.clearAndLoad(contextPath+"/userManageGrid.xml")
					progress.off()
				}

			}
		});
		}
	}else{
		alert(lang.admin.alert.recUser21)
	}
}



//비번 정책 불러오기
function loadPwPolicy(){
	$.ajax({
		url:contextPath+"/selectRPasswordPolicyInfo.do",
		data:{},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				var policyInfo = jRes.resData.policyInfo;

				var rPolicyUse		= policyInfo.rPolicyUse
				var rCycleUse		= policyInfo.rCycleUse
				var rCycle 			= policyInfo.rCycle
				var rCycleType 		= policyInfo.rCycleType
				var rPastPwUse		= policyInfo.rPastPwUse
				var rPastPwCount	= policyInfo.rPastPwCount
				var rTryUse			= policyInfo.rTryUse
				var rTryCount		= policyInfo.rTryCount
				var rLockUse		= policyInfo.rLockUse
				var rLockCount		= policyInfo.rLockCount
				var rLockType		= policyInfo.rLockType

				$("#pwPolicy").val(rPolicyUse).change();
				$("#cyclePolicy").val(rCycleUse).change();
				$("#cycleNum").val(rCycle);
				$("#cycleDate").val(rCycleType).change();
				$("#pastPolicy").val(rPastPwUse).change();
				$("#pastNum").val(rPastPwCount);
				$("#tryPolicy").val(rTryUse).change();
				$("#tryNum").val(rTryCount);
				$("#lockPolicy").val(rLockUse).change();
				$("#lockNum").val(rLockCount);
				$("#lockDate").val(rLockType);
			}
		}
	});
}


//비번 정책 수정하기
function modifyPwPolicy(){

	var policyUse		= $("#pwPolicy").val()
	var cycleUse		= $("#cyclePolicy").val()
	var cycle 			= $("#cycleNum").val()
	var cycleType 		= $("#cycleDate").val()
	var pastPwUse		= $("#pastPolicy").val()
	var pastPwCount		= $("#pastNum").val()
	var tryUse			= $("#tryPolicy").val()
	var tryCount		= $("#tryNum").val()
	var lockUse			= $("#lockPolicy").val()
	var lockCount		= $("#lockNum").val()
	var lockType		= $("#lockDate").val()

	var dataStr = {
		"policyUse" 	: policyUse
		,	"cycleUse"		: cycleUse
		,	"cycle"			: cycle
		,	"cycleType"		: cycleType
		,	"pastPwUse"		: pastPwUse
		,	"pastPwCount"	: pastPwCount
		,	"tryUse"		: tryUse
		,	"tryCount"		: tryCount
		,	"lockUse"		: lockUse
		,	"lockCount"		: lockCount
		,	"lockType"		: lockType

	}

	if(cycleUse=="Y" && (!cycle || cycle < 1)){
		alert(lang.admin.alert.recUser22)
		$("#cycleNum").focus();
		return;
	}else if(pastPwUse == "C" && (!pastPwCount || pastPwCount < 2)){
		alert(lang.admin.alert.userManage6)
		$("#pastNum").focus();
		return;
	}else if(lockUse == "Y" && ( !lockCount || lockCount<1)) {
		alert(lang.admin.alert.recUser23)
		$("#lockNum").focus();
	}else if(lockUse == "Y" && ( !lockType)) {
		alert(lang.admin.alert.recUser24)
		$("#lockDate").focus();
	}else {
		$.ajax({
			url:contextPath+"/updateRPasswordPolicyInfo.do",
			data:dataStr,
			type:"POST",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					alert(lang.admin.alert.recUser42/*"비밀번호 정책이 저장되었습니다."*/);
					layer_popup_close()
				}
				else
					alert(lang.admin.alert.recUser25);
			}
		});
	}
}

//
//function bgTreeEvent(){
//	$(document).find('#browser span').on("click",function(){
//		$('.tree_user_part_active').removeClass('tree_user_part_active');
//		// Active 활성화
//		$(this).parents("li").addClass('tree_user_part_active');
//
//		if($(this).parents("li").hasClass("expandable")){
//			$(this).parents("li").find('ul').css("display","none");
//			$(this).parents("li").hasClass("collapsable")
//		}
//
//		// 트리 클릭 시 우측 사용자 그리드 필터링 적용
//		var treeDepth = $(this).attr("state")
//		var columnNum = 0;
//		var clickedId = 0;
//		switch(treeDepth){
//		case "bgCode":
//			columnNum = 16
//			clickedId = $(this).attr("bg-code");
//			break;
//		case "mgCode":
//			columnNum = 17
//			clickedId = $(this).children("span").attr("mg-code");
//			break;
//		case "sgCode":
//			columnNum = 18
//			clickedId = $(this).children("span").attr("sg-code");
//			break;
//		case "agent":
//			columnNum = 3
//			clickedId = $(this).children("span").attr("user-id");
//			break;
//		}
//		userManageGrid.filterBy(columnNum,clickedId)
//	})
//}

function	TreeEvent(){
	$(document).find('span').on("click",function(event){
		event.stopPropagation();
		event.stopImmediatePropagation();
//		$(document).find('.tree_user_part_active').removeClass('tree_user_part_active');
//		// Active 활성화
//		$(this).parents("li").addClass('tree_user_part_active');
//		setTimeout(function(){
//			$(document).find("li").removeClass('tree_user_part_active');
//		},50)

		$(this).parents("li").find("ul").css("display","");
		// 트리 클릭 시 우측 사용자 그리드 필터링 적용
//		var treeDepth = $(this).attr("state")
//		var columnNum = 0;
//		var clickedId = 0;
//		switch(treeDepth){
//		case "bgCode":
//			columnNum = 16
//			clickedId = $(this).attr("bg-code");
//			break;
//		case "mgCode":
//			columnNum = 17
//			clickedId = $(this).attr("mg-code");
//			break;
//		case "sgCode":
//			columnNum = 18
//			clickedId = $(this).attr("sg-code");
//			break;
//		case "agent":
//			columnNum = 3
//			clickedId = $(this).attr("user-id");
//			break;
//		}
//		userManageGrid.filterBy(columnNum,clickedId)

		//	기존 방식은 페이징 처리가 아니라서 쿼리 방식으로 변경
		var treeDepth = $(this).attr("state")
		var columnNum = 0;
		var clickedId = 0;
		switch(treeDepth){
		case "bgCode":
			columnNum = 16
			clickedId = $(this).attr("bg-code");
			break;
		case "mgCode":
			columnNum = 17
			clickedId = $(this).attr("mg-code");
			break;
		case "sgCode":
			columnNum = 18
			clickedId = $(this).attr("sg-code");
			break;
		case "agent":
			columnNum = 3
			clickedId = $(this).attr("user-id");
			break;
		}
	})
}


function formToSerialize(){

	$(".inputFilter").trigger('blur');
	$(".inputFilter").trigger('keyup');

	var resultValue = "";

	$(".main_form").children().each(function (i){

		var id = this.id;
		var value = ($(this).val()) ? $(this).val().trim() : "";
		var $obj = $(this);

		if (value == "" || value == undefined || value == null)
			return;

			resultValue += (resultValue.length > 0?"&":"?") + id+"="+value;

	});
	if(treeView.getSelected()!=null && treeView.getSelected()!=''&&treeView.getSelected()!='all'){
			var level = treeView.getLevel(treeView.getSelected());

		switch (level) {
		case 2:
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+treeView.getSelected();
			break;
		case 3:
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+treeView.getSelected().split("_")[0];
			break;
		case 4:
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+treeView.getSelected();
			break;
		}
	}
	return encodeURI(resultValue);
}

function sessionDestroyUser(rowId){
	// DB 조회 ajax 처리
		$.ajax({
		url:contextPath+"/disconnect.do",
		data:{
			userID : userManageGrid.cells(rowId,3).getValue(),
			ruser : "N"
		},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y" && jRes.result == "DISSCONNECT") {
				alert(lang.admin.alert.recUser26)
			}
		}
	});
}
