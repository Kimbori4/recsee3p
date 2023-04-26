// 전역변수
var userManageGrid // 그리드
var treeView;
var check=0;
var nowSortingColumn="";

//	로드 함수
function userManageLoad(){
	treeView = new dhtmlXTreeObject("treeViewAgent","100%","100%",0);
	treeView.attachEvent("onXLS", function(){
		check=check+1;
		progress.on()
	});
	treeView.setImagePath("../resources/component/dhtmlxSuite/skins/skyblue/imgs/dhxtree_skyblue/");
	//treeView.enableCheckBoxes(1);
	treeView.enableThreeStateCheckboxes(true);
	treeView.enableSmartXMLParsing(true);
	//treeView.enableDistributedParsing(true,10,10);
	//treeView.load(contextPath+"/AgentTreeView.xml?aUser=N");
	//treeView.load(contextPath+"/AgentTreeView.xml?aUser=Y");
	treeView.load(contextPath+"/AgentTreeView.xml?aUser=N&pageName=userManageRec");
	treeView.attachEvent("onXLE", function(grid_obj,count){
		check=check-1;
		if(check==0){
			
			
			
			progress.off()
		}

	});
	
	treeView.attachEvent("onClick",function(id){
		var uri=contextPath+"/userManageRecGrid.xml"+formToSerialize();
		userManageGrid.clearAndLoad(uri, function(){
			userManageGrid.changePage(1)
			if(modiYn != "Y"){
				$('.userModiBtn').hide();
				$('.unlockUserBtn').hide();
			}
		//	selectOptionLoad($(userManageGrid.getFilterElement(8)), "authy", null,null,null,null,true);
		})
		progress.off()
	});

	//모바일 렉시의 경우 내선번호를 사용하지 않아 그리드 처리하는 부분에서 hidden처리
	userManageGrid = createGrid("userManageGrid", "userManagePaging", "userManageRecGrid","?header=true","",100,[],[]);
	

	gridFunction();
	formFunction();
	authyLoad();
	
}



// 모바일사용 현황 불러오기
function mobileUsage(){
	var mobileUsageCount;
	var mobileLicence;
	$.ajax({
		url:contextPath+"/mobileUsage.do?"+new Date().getTime(),
		data:{},
		type:"GET",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				mobileUsageCount=jRes.resData.mobileUsage;
				mobileLicence=jRes.resData.mobileLicence;
			}

		}
	});
	var htmlString ='<div id="mobileUsage"><label>'+lang.admin.label.mobileUsage/* 모바일사용 현황 */+'</label>&nbsp;:&nbsp;&nbsp;<label>'+mobileUsageCount+'</label>/<label>'+mobileLicence+'</label></div>';
	$(".dhxtoolbar_float_right").html(htmlString);
}


function createGrid(objGrid, objPaging, url, strData, excelFileName, initPageRow, hiddenColumn, showColumn){
	var tempGridName = objGrid;
	var firstLoad = true;
	objGrid = new dhtmlXGridObject(objGrid);
	objGrid.setImagePath(recseeResourcePath + "/images/project/");
	// 페이징 유무에 따른 실행
	if(objPaging){
		objGrid.i18n.paging = i18nPaging[locale];
		objGrid.enablePaging(true,initPageRow, 5, objPaging, true);
		objGrid.setPagingWTMode(true,true,true,[100,250,500]);
	    objGrid.setPagingSkin("toolbar","dhx_web");
	}	
	//objGrid.enableRowspan(true);
	objGrid.enableColumnAutoSize(false);
	//objGrid.enablePreRendering(50);
	objGrid.setSkin("dhx_web");
	objGrid.init();
	
	objGrid.attachEvent("onXLS", function(){
		check=check+1;
		progress.on()
	});
	// 파싱완료
	objGrid.attachEvent("onXLE", function(grid_obj,count){
		
		if (objGrid.getRowsNum() > 0){
			var setResult = '<div style="width: 100%; text-align: center;">'+ objGrid.i18n.paging.results+ objGrid.getRowsNum()+objGrid.i18n.paging.found+'</div>'
			objGrid.aToolBar.setItemText("total", setResult)
		}
		ui_controller();
		check=check-1;
		if(check==0)
			progress.off();

		$(window).resize();
		objGrid.setSizes();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		if(firstLoad){
			var uri=contextPath+"/userManageRecGrid.xml";
			objGrid.clearAndLoad(uri, function(){
				userManageGrid.changePage(1)
				if(modiYn != "Y"){
					$('.userModiBtn').hide();
					$('.unlockUserBtn').hide();
				}

				if(recsee_mobile == "Y"){
					mobileUsage();
				}
				firstLoad = false;
			})
		}

		var arr_hidden_rUserInfo = hidden_rUserInfo.split(",");
		for(var i = 0; i < arr_hidden_rUserInfo.length; i++) {
			switch(arr_hidden_rUserInfo[i]) {
				case "extNo":
					$(".mExtNum").hide();
					var extNoIndex = userManageGrid.getColIndexById("extNo");
					if(extNoIndex != undefined){
						userManageGrid.setColumnHidden(extNoIndex,true); // 컬럼 안보이게
					}
					break;
				case "userPhone":
					$(".mUserPhone").hide();
					var userPhoneIndex = userManageGrid.getColIndexById("userPhone");
					if(userPhoneIndex != undefined){
						userManageGrid.setColumnHidden(userPhoneIndex,true); // 컬럼 안보이게
					}
					break;
				case "userSex":
					$(".mSex").hide();
					var userSexIndex = userManageGrid.getColIndexById("userSex");
					if(userSexIndex != undefined){
						userManageGrid.setColumnHidden(userSexIndex,true); // 컬럼 안보이게
					}
					break;
				case "userEmail":
					$(".mEmail").hide();
					var userEmailIndex = userManageGrid.getColIndexById("userEmail");
					if(userEmailIndex != undefined){
						userManageGrid.setColumnHidden(userEmailIndex,true); // 컬럼 안보이게
					}
					break;
				case "ctiId":
					$(".mCtiId").hide();
					var userEmailIndex = userManageGrid.getColIndexById("ctiId");
					if(userEmailIndex != undefined){
						userManageGrid.setColumnHidden(userEmailIndex,true); // 컬럼 안보이게
					}
					break;
			}
		}

	});
	objGrid.load(contextPath+"/"+url+".xml" + encodeURI(strData),function(){
		$("#userManageGrid tbody tr:nth-child(3) td:nth-child(1) input").addClass("inputFilter korFilter")
		$("#userManageGrid tbody tr:nth-child(3) td:nth-child(2) input").addClass("inputFilter numberFilter").attr("maxlength","8")
		$("#userManageGrid tbody tr:nth-child(3) td:nth-child(3) input").addClass("inputFilter numberFilter").attr("maxlength","5")
		$("#userManageGrid tbody tr:nth-child(3) td:nth-child(4) input").addClass("inputFilter numberFilter").attr("maxlength","11")
		var search_toolbar = objGrid.aToolBar;
		search_toolbar.addSpacer("perpagenum");
		search_toolbar.addText("total",0,'<div id="rowCount" style="width: 100%; text-align: center;">'+ userManageGrid.i18n.paging.results+userManageGrid.getRowsNum()+userManageGrid.i18n.paging.found+'</div>')
		search_toolbar.setWidth("total",80)
		

		

		
		//체크박스 전체 선택
		objGrid.attachEvent("onHeaderClick",function(ind, obj){
    		if(ind == 0 && obj.type == "click") {
    			if((/item_chk0/).test($("#allcheck>img").attr("src"))) {
    				this.setCheckedRows(0, 1);
    				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk1.gif");
    			} else {
    				this.setCheckedRows(0, 0);
    				$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
    			}
    		} else {
    			return true;
    		}
    	});
    	
		//필터 코드 변경
		selectOptionLoad($(objGrid.getFilterElement(8)), "authy", null,null,null,null,true);
		//selectOrganizationLoad($(objGrid.getFilterElement(9)), "bgCode", null, null, null, null, "callCenter", true);
		
		$(window).resize();
		$(window).resize(function() {
			objGrid.setSizes();
		});
		
		if(modiYn != "Y"){
			$('.userModiBtn').hide();
			$('.unlockUserBtn').hide();
		}

		// 페이지 체인지 이벤트
		objGrid.attachEvent("onBeforePageChanged", function(){
			if(!this.getRowsNum())
				return false;
			return true;
		});
		
		if(objPaging){
			objGrid.aToolBar.setMaxOpen("pages", 5);
		}
		
		ui_controller();
	});
	
	return objGrid;
}

//사용자 수정 팝업창
function modifyUser(rowId){
	var userId = 		userManageGrid.cells(rowId,userManageGrid.getColIndexById("userId")).getValue()	// 아이디
	var userName = 		userManageGrid.cells(rowId,userManageGrid.getColIndexById("userName")).getValue()	// 이름
	var password = 		userManageGrid.cells(rowId,5).getValue()	// 비번
	var passwordCheck = userManageGrid.cells(rowId,5).getValue()	// 비번확인
	var extNum = 		userManageGrid.cells(rowId,userManageGrid.getColIndexById("extNo")).getValue()	// 내선
	var authy = 		userManageGrid.cells(rowId,userManageGrid.getColIndexById("userLevel")).getValue()	// 권한
	var phoneNumber = 	userManageGrid.cells(rowId,userManageGrid.getColIndexById("userPhone")).getValue()	// 폰버노
	var sex = 			userManageGrid.cells(rowId,userManageGrid.getColIndexById("userSex")).getValue()	// 성별
	var bgCode = 		userManageGrid.cells(rowId,15).getValue()	// 대
	var mgCode = 		userManageGrid.cells(rowId,16).getValue()	// 중
	var sgCode = 		userManageGrid.cells(rowId,17).getValue()	// 소
	var empId = 		userManageGrid.cells(rowId,13).getValue()	// 사번
	var email =			userManageGrid.cells(rowId,userManageGrid.getColIndexById("userEmail")).getValue()	// 이멜
    var ctiId = 		userManageGrid.cells(rowId,userManageGrid.getColIndexById("ctiId")).getValue()	// cti id
    var clientIp = 		userManageGrid.cells(rowId,18).getValue()	// 클라이언트ip
    var EmploymentCategory = userManageGrid.cells(rowId,22).getValue()	// 재직유무
    var skinCode   = userManageGrid.cells(rowId,23).getValue()	// 신규녹취사용유무
    // var useYN = 		userManageGrid.cells(rowId,22).getValue()	// 사용여부

    $("#mUserId").val(userId).addClass("ui_input_hasinfo").attr("readonly",true)// 아이디
	$("#mUserName").val(userName)							// 이름
	$("#mPassword").val("")									// 비번
	$("#mPasswordCheck").val("")							// 비번확인
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
    
    $("#mClientIp").val(clientIp)                           // 클라이언트ip
    
    
    /*$(".mUseYN").show();
    $("#mUseYN").val(useYN)*/
    
    
    
    
	$("#addUser .ui_pannel_tit").text(lang.admin.alert.recUser30)
	
	$("#EmploymentCategory").val(EmploymentCategory);
    $("#mSkinCode").val(skinCode);
	
	$("#mUserAddBtn").hide();
	$("#mUserModifyBtn").show();
	if(recsee_mobile=="Y"){
		//모바일 렉시는 내선, 성별, 이메일을 사용하지 않으므로 hidden 처리
		$("#addUser").find(".mExtNum").hide();
		$("#addUser").find(".mSex").hide();
		$("#addUser").find(".mEmail").hide();
    }
    if(clientIpChk=="N"|modiYn!='Y'){
		//모바일 렉시는 내선을 사용하지 않으므로 hidden 처리
		$("#addUser").find(".mClientIp").hide();
    }
    

	$("#rowId").val(rowId);


	if(userInfoJson.userLevel == "E1002") { // 영업점
		$(".disableTarget").attr("disabled","true").addClass("ui_input_hasinfo");
	}
	
	layer_popup('#addUser');
}

//잠금 해제
function unlockUser(rowId){

	var userId = userManageGrid.cells(rowId,userManageGrid.getColIndexById("userId")).getValue();	// 아이디
	
	if(!confirm(lang.admin.alert.unlockConfirm/*"선택하신 계정의 잠금을 해제 하시겠습니까?"*/)){
		return;
	}

    $.ajax({
		url:contextPath+"/unlockUser.do",
		data:{
				"userId" : userId
			},
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.unlockSuccess);/*잠금 해제에 성공 하였습니다.*/
				
				// 그리드에 바로 반영
				var uri=encodeURI(contextPath+"/userManageRecGrid.xml"+formToSerialize())
				userManageGrid.clearAndLoad(uri, function(){
					if(modiYn != "Y"){
						$('.userModiBtn').hide();
						$('.unlockUserBtn').hide();
					}
				})
			}else{
				alert(lang.admin.alert.unlockFail + "\nError : "+jRes.Results);/*잠금 해제에 실패 하였습니다.*/
			}
		}
	});
}

//	그리드 적용 함수
function gridFunction(){
	userManageGrid.attachEvent("onRowDblClicked", function(id,cInd){
		if(modiYn == "Y"){
			modifyUser(id)
		}
	});
	
	// 소팅 이벤트 커스텀
	userManageGrid.attachEvent("onBeforeSorting", function(ind){
		
			var a_state = this.getSortingState()

			var direction = "asc"
			if(nowSortingColumn==ind)
				direction = ((a_state[1] == "asc") ? "desc" : "asc");

			var columnId = this.getColumnId(ind);

			var requestOrderby = columnId+"|"+direction;

			var nowPage = this.getStateOfView()[0]
			this.clearAndLoad(contextPath+"/userManageRecGrid.xml"+ formToSerialize(requestOrderby), function(){
				userManageGrid.changePage(1)
			});
			progress.off()
			this.setSortImgState(true,ind,direction)

			var a_state = this.getSortingState()
			nowSortingColumn = a_state[0];
		
	});
}

// 폼 함수
function formFunction(){
	$('.main_form').children().keyup(function(e) {
		if (e.keyCode == 13)
			$("#searchBtn").trigger("click");
	})
	
	$("#searchBtn").click(function(event){
		
		userManageGrid.clearAndLoad(contextPath+"/userManageRecGrid.xml"+formToSerialize(), function(){
			userManageGrid.changePage(1)
		})
		progress.off()
	});
	
	// 셀렉트 옵션값 셋팅
	// 내선
	selectOptionLoad($("#mExtNum"), "channel")
	// 검색용 권한
	selectOptionLoad($("#Authy"), "authy","","","","","none")
	// 권한
	selectOptionLoad($("#mAuthy"), "authy","","","","","all")
	// 대분류
	selectOrganizationLoad($("#mBgCode"), "bgCode","","","","","callCenter","empty")

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
	
	//그룹수정 대중소 불러오기
	selectOrganizationLoad($("#MbgGroup"), "bgCode","","","","","callCenter","empty")
	
	// 대분류 변화시 중분류 로드
	$( "#MbgGroup" ).change(function() {
		selectOrganizationLoad($("#MmgGroup"), "mgCode", $(this).val(),"","","","","Y")
		// 중분류 및의 소분류가 있다면 디폴트로 로드
		selectOrganizationLoad($("#MsgGroup"), "sgCode", $(this).val(), $("#MmgGroup").val(),"","","","Y")
	});

	// 중분류 변화시 소분류 로드
	$( "#MmgGroup" ).change(function() {
		selectOrganizationLoad($("#MsgGroup"), "sgCode", $("#MbgGroup").val(), $(this).val(),"","","","Y")
	});

	// 추가
	$("#mUserAddBtn").click(function(){
		insertUserInfo();
	})
	
	// 수정
	$("#mUserModifyBtn").click(function(){
		modifyUserInfo();
	})
	
	//다인그룹변경팝업열기
	$("#MultiModigroupBtn").click(function(){
		
		if(userManageGrid.getCheckedRows(0).length==0){
			alert(lang.admin.alert.recUser43)
			return false;
		}
		$("#MbgGroup").val("")
		$("#MmgGroup").val("")									// 중
		$("#MsgGroup").val("")									// 소
		
		layer_popup('#multiModifyGroup');
	})
	
	// 멀티그룹수정
	$("#MgroupModifyBtn").click(function(){
		multiGroupModify();
	})



	// 삭제
	$("#userDeleteBtn").click(function(){
		deleteUserInfo();
	})

	// 사용자 추가 팝업 열기
	$("#userAddBtn").click(function(){
		$("#addUser .ui_pannel_tit").text(lang.admin.alert.recUser31)
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
        $("#mClientIp").val("")									// 클라이언트ip
        $("#mSkinCode").val("Y")								// 신규 녹취 사용 
		
		if(recsee_mobile=="Y"){
			//모바일 렉시는 내선, 성별, 이메일을 사용하지 않으므로 hidden 처리
			$("#addUser").find(".mExtNum").hide();
			$("#addUser").find(".mSex").hide();
			$("#addUser").find(".mEmail").hide();
        }
		
        if(clientIpChk=="N"||modiYn!='Y'){
			$("#addUser").find(".mClientIp").hide();
		}else{
			$("#addUser").find(".mClientIp").show();
		}
        

		if(userInfoJson.userLevel == "E1002") { // 영업점
			$(".disableTarget").removeAttr("disabled").removeClass("ui_input_hasinfo");
		}
		layer_popup('#addUser');
	})
	
	// 엑셀 다운
	$("#excelDownBtn").click(function(){
		if(userManageGrid.getRowsNum() > 0) {					
			progress.on();
			
			var grid_link = contextPath+'/userManageRecExcel.do'+formToSerialize();
			
			$("#downloadFrame").attr("src", grid_link);

			if (window.dhx4.isIE) {
				$(window).blur(function(){
					progress.off()
				});
			} else {
				$("#downloadFrame").ready(function(){
					progress.off()
				});
			}
		} else {
			alert(lang.statistics.js.alert.msg29/*"조회된 결과가 없어 엑셀다운로드를 할 수 없습니다."*/)
		}
	});

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
		$("#tryPolicy").change();
		$("#lockPolicy").change();

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
		$("#addGroup").find("p").text(lang.admin.alert.recUser32);
		$("#seletCode, #bgGroup, #mgGroup").removeAttr("disabled").removeClass("ui_input_hasinfo");

		$("#seletCode").val("bgCode").change();
		$("#groupUseYN").val("Y").change();

		$("#groupAddBtn").show();
		$("#groupModifyBtn").hide();
		$("#addGroup").data("bgCode","");
		
		layer_popup('#addGroup');
	});

	// 메인 그룹 수정 버튼
	$("#btnGroupModify").click(function(){

		$("#addGroup").find("p").text(lang.admin.alert.recUser1)
		
		var activeObj;
		var state;
		var bgCode;
		var mgCode;
		var sgCode;
		var groupName = treeView.getSelectedItemText();
		var useYN = "Y";
		
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
		
		$.ajax({
			url:contextPath+"/selectGroupInfo.do",
			data:{
				"level" : level,
				"bgCode" : bgCode,
				"mgCode" : mgCode,
				"sgCode" : sgCode
			},
			type:"GET",
			dataType:"json",
			async: false,
			success:function(jRes){
				// DB에 조회한 계정이 있으면
				if(jRes.success == "Y") {
					useYN = jRes.resData.useYN;
				}
			}
		});
		
		
		$("#seletCode").val(state).change()
		$("#bgGroup").val(bgCode).change()
		$("#mgGroup").val(mgCode).change()
		$("#groupUseYN").val(useYN).change()

		$("#seletCode, #bgGroup, #mgGroup").attr("disabled","disabled").addClass("ui_input_hasinfo");
		$("#groupName").val(groupName);

		$("#groupAddBtn").hide();
		$("#groupModifyBtn").show();

		$("#addGroup").data("bgCode",bgCode)
		$("#addGroup").data("mgCode",mgCode)
		$("#addGroup").data("sgCode",sgCode)
		$("#addGroup").data("useYN",useYN)
		
		layer_popup('#addGroup');
		
//		var activeObj = $(".tree_user_part_active > .folder")
//		var state = activeObj.attr("state")
//		var bgCode = activeObj.attr("bg-code")
//		var mgCode = activeObj.attr("mg-code")
//		var sgCode = activeObj.attr("sg-code")
//		var groupName = activeObj.text()
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
//			selectOrganizationLoad($("#bgGroup"), "bgCode")
			selectOrganizationLoad($("#bgGroup"), "bgCode","","","","","callCenter","empty");
			break;
		case "sgCode":
			$("#bgGroup, #mgGroup").empty().removeAttr("disabled").removeClass("ui_input_hasinfo");
//			selectOrganizationLoad($("#bgGroup"), "bgCode")
			selectOrganizationLoad($("#bgGroup"), "bgCode","","","","","callCenter","empty")
			// 대분류 변화시 중분류 로드
			$( "#bgGroup" ).on('change', function() {
//				selectOrganizationLoad($("#mgGroup"), "mgCode", $(this).val())
				selectOrganizationLoad($("#mgGroup"), "mgCode",$(this).val(),"","","","callCenter","empty")
			});
			// 대분류
			$("#bgGroup").change()
			break;
		}

	});

	// 권한 추가 메인 버튼
	$("#authyAddBtn").click(function(){
		$("#addGroup").find("p").text(lang.admin.alert.recUser2)

		$("#modifyGroupBtn").hide();
		$("#addGroupBtn").show();
		layer_popup('#addJuri');
	});
	// 권한 수정 메인 버튼
	$("#authyModifyBtn").click(function(){
		$("#addGroup").find("p").text(lang.admin.alert.recUser3)

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
	
	
	//비밀번호 정책 유효성 체크 (음수 불가)
	$('#cycleNum, #pastNum, #tryNum, #lockNum').change(function(){
		if($(this).val()<1){
			
			var tartgetId=$(this).attr('id');
			
			switch (tartgetId) {
			case 'cycleNum':
				alert(lang.admin.alert.recUser22);
				break;
				
			case 'pastNum':
				alert(lang.admin.alert.rec23)	
				break;
				
			case 'tryNum':
				alert(lang.admin.alert.recUser23)
				break;
				
			case 'lockNum':
				alert(lang.admin.alert.recUser24)
				break;
			}
			
			$(this).focus();
			return false
		}
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
	var empId = $("#mEmpId").val()							// 사번
	var email = $("#mEmail").val()							// 이멜
    var ctiId = $("#mCtiId").val()							// cti id
    var clientIp = $("#mClientIp").val()							// clientIp
    var EmploymentCategory = $("#EmploymentCategory").val();
    var skinCode = $("#mSkinCode").val();
    var useYN = $("#mUseYN").val()							// clientIp
   
	var rowId = $("#rowId").val()							// 현재 열린 팝업 rowId
	
	var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/; // 이메일 정규식
	var authyCheck = 0;
	if(authy && userManageGrid.cells(userManageGrid.getSelectedId(),userManageGrid.getColIndexById("userLevel")).getValue()!=authy){
		authyCheck = 1;
	}
	var bgCodeOri = userManageGrid.cells(userManageGrid.getSelectedId(),15).getValue()	// 대
	var mgCodeOri = userManageGrid.cells(userManageGrid.getSelectedId(),16).getValue()	// 중
	var SgCodeOri = userManageGrid.cells(userManageGrid.getSelectedId(),17).getValue()	// 소
	
	var changeGroup = "N";
	
	if((bgCode+mgCode+sgCode).trim()!=(bgCodeOri+mgCodeOri+SgCodeOri))
		changeGroup="Y";

	var authyName = "";
	var bgName = "";
	var mgName = "";
	var sgName = "";
	
	var isChangedChk = false;
	/*var rsa = new RSAKey();
	rsa.setPublic(RSAModulus, RSAExponent);*/
	
	var rsa
	if(new RSAKey() != null){
		rsa = new RSAKey();
	}else{
		rsa = new top.RSAKey();
	}
	
	//var rsa = new RSAKey();
	if(RSAModulus != null){
		rsa.setPublic(RSAModulus, RSAExponent);
	}else{
		rsa.setPublic(top.RSAModulus, top.RSAExponent);
	}
	
	var dataStr ={
				"userId" : rsa.encrypt(userId)
			,	"userName" : userName
	        ,   "useYN" : useYN
	        ,   "authy" : authy
	        ,   "authyCheck" : authyCheck
	        ,   "empId" : empId
	        ,   "ctiId" : ctiId
		};
	
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
	
	if(password!='') {
		isChangedChk = true;
		dataStr.password = rsa.encrypt(password);
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

	if (authy) {
		authyName = $("#mAuthy").children(":selected").text();
	}
	if(!bgCode){
		alert(lang.admin.alert.recUser12)
		return;
	} else {
		bgName = $("#mBgCode").children(":selected").text();
	}
	if(mgCode){
		mgName = $("#mMgCode").children(":selected").text();
	}
	if(sgCode){
		sgName = $("#mSgCode").children(":selected").text();
	}
	/*if(!mgCode){
		alert("중분류를 선택 해 주세요")
		return;
	}
	if(!sgCode){
		alert("소분류를 선택 해 주세요")
		return;
	}*/
	if(email && !email.match(regExp)){
		alert(lang.admin.alert.recUser13  +"\nEx)furence@furence.com")
		$("#mEmail").focus();
		return;
    }
	
	if (!skinCode) {
		alert("신규 녹취 사용 여부를 선택해주세요.")
		$("#mSkinCode").focus();
		return;	
	} else {
		isChangedChk = true;
	}
    //아이피체크옵션 사용시 아이피 유형 체크
    if(clientIpChk=='Y'&& clientIp.length>0 &&!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(clientIp))){
        alert(lang.admin.alert.chennelManage1);
        return false;
    }
        
    if(userName != userManageGrid.cells(rowId,userManageGrid.getColIndexById("userName")).getValue()){ 
    	isChangedChk = true;
    }
    
	// 중복 처리
	if(userManageGrid.getRowsNum() > 0 ) {
		var isDuplId = false;
		userManageGrid.forEachRow(function(id){
			if(extNum != "" && (userId != this.cells(id,userManageGrid.getColIndexById("userId")).getValue()) && (extNum == this.cells(id,userManageGrid.getColIndexById("extNo")).getValue())) {
				isDuplId = true;
			}
		});
		if (isDuplId) {
			alert("이미 등록된 내선번호입니다."/*" 이미 등록된 내선번호 입니다."*/)
			$('#mExtNum').focus();
			return;
		}
	}
	
    if(extNum != userManageGrid.cells(rowId,userManageGrid.getColIndexById("extNo")).getValue()){ 
    	isChangedChk = true;
    	dataStr.extNum = extNum;
    	dataStr.extNumChanged = "Y";
    } else {
    	extNum = "";
    }
    if(phoneNumber != userManageGrid.cells(rowId,userManageGrid.getColIndexById("userPhone")).getValue()){ 
    	isChangedChk = true;
    	dataStr.phoneNumber = phoneNumber;
    	dataStr.isPhoneChanged = "Y";
    } else {
    	phoneNumber = "";
    }
    if($("#mSex").val() != userManageGrid.cells(rowId,userManageGrid.getColIndexById("userSex")).getValue()){ 
    	isChangedChk = true;
    	dataStr.sex = sex;
    } else {
    	sex = "";
    }
    if(ctiId != userManageGrid.cells(rowId,userManageGrid.getColIndexById("ctiId")).getValue()){ 
    	isChangedChk = true;
    	dataStr.ctiId = ctiId;
    	dataStr.ctiIdChanged = "Y";
    } else {
    	extNum = "";
    }
    if(authy != userManageGrid.cells(rowId,userManageGrid.getColIndexById("userLevel")).getValue()){ 
    	isChangedChk = true;
    	dataStr.authy = authy;
    	dataStr.authyName = authyName;
    } else {
    	authy = "";
    }
    if(bgCode != userManageGrid.cells(rowId,15).getValue()){ 
    	isChangedChk = true;
    	dataStr.bgCode = bgCode;
    	dataStr.bgName = bgName;
    } else {
    	bgCode = "";
    }
    if(mgCode != userManageGrid.cells(rowId,16).getValue()){ 
    	isChangedChk = true;
    	dataStr.mgCode = mgCode;
    	dataStr.mgName = mgName;
    } else {
    	mgCode = "";
    }
    if(sgCode != userManageGrid.cells(rowId,17).getValue()){ 
    	isChangedChk = true;
    	dataStr.sgCode = sgCode;
    	dataStr.sgName = sgName;
    } else {
    	sgCode = "";
    }
    if(email != userManageGrid.cells(rowId,userManageGrid.getColIndexById("userEmail")).getValue()){ 
    	isChangedChk = true;
    	dataStr.email = email;
    	dataStr.emailChanged = "Y";
    } else {
    	email = "";
    }
    if(clientIp != userManageGrid.cells(rowId,18).getValue()){ 
    	isChangedChk = true;
    	dataStr.clientIp = clientIp;
    } else {
    	clientIp = "";
    }
    
    if(EmploymentCategory != userManageGrid.cells(rowId,22).getValue()){ 
    	isChangedChk = true;
    	dataStr.EmploymentCategory = EmploymentCategory;
    } else {
    	EmploymentCategory = "";
    }
    if(skinCode != userManageGrid.cells(rowId,23).getValue()){
    	isChangedChk = true;
    	dataStr.skinCode = skinCode;
    } else {
    	skinCode = "";
    }
	
	if (!isChangedChk) {
		alert(lang.admin.alert.userManage8);
		return;
	}
    progress.on()
    
    $.ajax({
		url:contextPath+"/updateUserInfo.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: true,
		success:function(jRes){
			// DB에 조회한 계정이 있으면
			if(jRes.success == "Y") {
				alert(lang.admin.alert.recUser34);
				layer_popup_close();
				
				// 본인계정 수정시 바로 반영			
				if(userId==userInfoJson.userId){
					if(userName){
						$('.user_id p').html($('.user_id p').html().replace(userInfoJson.userName,userName))
						userInfoJson.userName=userName;
					}
					if(authy){
						userInfoJson.userLevel=authy;
					}
					if(bgCode){
						userInfoJson.bgCode=bgCode;
					}
					if(mgCode){
						userInfoJson.mgCode=mgCode;
					}
					if(sgCode){
						userInfoJson.sgCode=sgCode;
					}
				}
				
				if(recsee_mobile == "Y"){
					mobileUsage();
				}
				var uri=encodeURI(contextPath+"/userManageRecGrid.xml"+formToSerialize())
				userManageGrid.clearAndLoad(uri, function(){
					if(modiYn != "Y"){
						$('.userModiBtn').hide();
						$('.unlockUserBtn').hide();
					}
				})
			}else if(jRes.success == "N" && jRes.result == "PASSWORD PATTERN IS MISS MATCH"){
				alert(jRes.resData.msg);
				$("#mPassword").focus();
			}else if(jRes.success == "N" && jRes.result == "PASSWORD IS USED"){
				alert(lang.admin.alert.recUser27)
				$("#mPassword").focus();
			}else if(jRes.success == "N" && jRes.result == "NEW NUMBER REGISTRATION IS NOT POSSIBLE"){
				alert(lang.admin.alert.recUser45)///신규번호 등록이 불가능 합니다.
				$("#mPhoneNumber").focus();
			}else if(jRes.success == "N" && jRes.result == "IP IS USED"){
				alert(lang.admin.alert.recUser46)///입력하신 IP가 사용중입니다.
				$("#mClientIp").focus();
			}else{
				alert(lang.admin.alert.recUser28 + "\nError : "+jRes.Results);
			}
			progress.off()
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
	var empId = $("#mEmpId").val()							// 사번
	var email = $("#mEmail").val()							// 이멜
    var ctiId = $("#mCtiId").val()							// cti id
    var clientIp = $("#mClientIp").val()							// clientIp
	var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/; // 이메일 정규식
    var EmploymentCategory = $("#EmploymentCategory").val();
    var skinCode = $("#mSkinCode").val();

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
	
	/*var rsa = new RSAKey();
	rsa.setPublic(RSAModulus, RSAExponent);*/
	
	var rsa
	if(new RSAKey() != null){
		rsa = new RSAKey();
	}else{
		rsa = new top.RSAKey();
	}
	
	//var rsa = new RSAKey();
	if(RSAModulus != null){
		rsa.setPublic(RSAModulus, RSAExponent);
	}else{
		rsa.setPublic(top.RSAModulus, top.RSAExponent);
	}
	
	var dataStr = {
				"userId" : rsa.encrypt(userId)
			,	"userName" : userName
			,	"password" : rsa.encrypt(password)
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
            ,	"clientIp" : clientIp
            ,	"EmploymentCategory"	:	EmploymentCategory
            ,	"skinCode"	: skinCode
	}

	if(!userId){
		alert(lang.admin.alert.recUser10)
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
	if(!bgCode){
		alert(lang.admin.alert.recUser12)
		return;
	}
	/*if(!mgCode){
		alert("중분류를 선택 해 주세요")
		return;
	}
	if(!sgCode){
		alert("소분류를 선택 해 주세요")
		return;
	}*/
	if(email && !email.match(regExp)){
		alert(lang.admin.alert.recUser13  +"\nEx)furence@furence.com")
		$("#mEmail").focus();
		return;
    }
	
	if (!skinCode) {
		alert("신규 녹취 사용 여부를 선택해주세요.")
		$("#mSkinCode").focus();
		return;	
	}
	
    //아이피체크옵션 사용시 아이피 유형 체크
	if(clientIpChk=='Y'&& clientIp.length>0 &&!(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(clientIp))){
        alert(lang.admin.alert.chennelManage1);
        return false;
    }
    
	// 중복 처리
	if(userManageGrid.getRowsNum() > 0 ) {
		var isDuplId = false;
		userManageGrid.forEachRow(function(id){
			if(userId == this.cells(id,userManageGrid.getColIndexById("userId")).getValue()) {
				isDuplId = true;
			}
		});
		if (isDuplId) {
			alert(lang.admin.alert.recUser14);
			$('#mUserId').focus();
			return;
		}
	}
	// 중복 처리
	if(userManageGrid.getRowsNum() > 0 ) {
		var isDuplId = false;
		userManageGrid.forEachRow(function(id){
			if(extNum != "" && (userId != this.cells(id,userManageGrid.getColIndexById("userId")).getValue()) && (extNum == this.cells(id,userManageGrid.getColIndexById("extNo")).getValue())) {
				isDuplId = true;
			}
		});
		if (isDuplId) {
			alert("이미 등록된 내선번호입니다."/*" 이미 등록된 내선번호 입니다."*/)
			$('#mExtNum').focus();
			return;
		}
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
				if(recsee_mobile == "Y"){
					mobileUsage();
				}
				userManageGrid.clearAndLoad(contextPath+"/userManageRecGrid.xml"+formToSerialize(), function(){
					if(modiYn != "Y"){
						$('.userModiBtn').hide();
						$('.unlockUserBtn').hide();
					}
				});
				progress.off()
			}else if(jRes.success == "N" && jRes.result == "PASSWORD PATTERN IS MISS MATCH"){
				alert(jRes.resData.msg);
				$("#mPassword").focus();
			}else if(jRes.success == "N" && jRes.result == "THIS EMPLOYEE NUMBER IS ALREADY USED"){
				alert(lang.admin.alert.recUser16 + "\nError : "+jRes.result);
			}else if(jRes.success == "N" && jRes.result == "NEW NUMBER REGISTRATION IS NOT POSSIBLE"){
				alert(lang.admin.alert.recUser45)//신규번호 등록이 불가능 합니다.
				$("#mPhoneNumber").focus();
			}else if(jRes.success == "N" && jRes.result == "THIS IP IS ALREADY USED"){
				alert(lang.admin.alert.recUser46)//입력하신 IP가 사용중입니다.
				$("#mClientIp").focus();
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

// proc : insert / modify
function procGroup(proc){

	var code = $("#seletCode").val();
	var bgCode = ($("#bgGroup").val()  ? $("#bgGroup").val() : $("#addGroup").data("bgCode") );
	var mgCode = ($("#mgGroup").val()  ? $("#mgGroup").val() : $("#addGroup").data("mgCode") );
	var sgCode = $("#addGroup").data("sgCode");
	var groupName = $("#groupName").val();
	var groupUseYN = $("#groupUseYN").val();
	var mgCodeText = $("#mgGroup option:checked").text();
	
	dataStr = {
			"level": level
			,	"proc" : proc
			,	"classBgName" : groupName
			,	"classMgName" : groupName
			,	"classSgName" : groupName
			,	"classBgCode" : bgCode
			,	"classMgCode" : mgCode
			,	"classSgCode" : sgCode
			,	"groupUseYN"  : groupUseYN
			,	"groupType"   : "user_rec"

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
	/*중그룹없이 소그룹 추가시*/
	if(code == "sgCode" && (mgCodeText == null || mgCodeText == "")){
		alert(lang.evaluation.js.alert.msg131 /*"중그룹을 먼저 추가해주세요."*/);
		return;
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
				treeView.load(contextPath+"/AgentTreeView.xml?aUser=N&pageName=userManageRec");
				$("#groupName").val("");
				selectOrganizationLoad($("#mBgCode"), "bgCode","","","","","callCenter","empty")
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
		$('#MultiModigroupBtn').hide();
	}
	
	if(delYn != 'Y') {
		$('#authyDeleteBtn').hide();
		$('#userDeleteBtn').hide();
		$('#btnGroupDelete').hide();
	}
	if(excelYn != 'Y') {
		$('#excelDownBtn').hide();
	}
	if(recsee_mobile == "Y"){
		$('#extNo').hide();
	}
	if(updateGroupinfoYn=='Y'){
		$('#MultiModigroupBtn').hide();
	}
}


//사용자 정보 삭제
function deleteUserInfo(){

	if(userManageGrid.getCheckedRows(0) != "") {
		if(confirm(lang.admin.alert.recUser19)){
			var checked = userManageGrid.getCheckedRows(0).split(",");
			var rstUserId = new Array();
	
			for( var  i = 0; i<checked.length; i++) {
				// 
				rstUserId.push(userManageGrid.cells(checked[i],userManageGrid.getColIndexById("userId")).getTitle());
	//			rstUserId.push(userManageGrid.cells2(parseInt(checked[i])-1,3).getValue());
			}
			var rst = rstUserId.join(",");
	
			$.ajax({
				url:contextPath+"/deleteUserInfo.do?userList="+rst,
				data:{},
				type:"GET",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success == "Y") {
						alert(lang.admin.alert.recUser20)
						if(recsee_mobile == "Y"){
							mobileUsage();
						}
						userManageGrid.clearAndLoad(contextPath+"/userManageRecGrid.xml"+formToSerialize())
						progress.off()
					}
	
				}
			});
		}
	}else{
		alert(lang.admin.alert.recUser21)
	}
}
//멀티그룹수정
function multiGroupModify(){
	var idArry=userManageGrid.getCheckedRows(0).split(',');
	var idString="";
	
	for(var i=0; i<idArry.length;i++){
		if(i+1==idArry.length){
			idString+="'"+userManageGrid.cells(idArry[i],userManageGrid.getColIndexById("userId")).getValue()+"'"
		}else{
			idString+="'"+userManageGrid.cells(idArry[i],userManageGrid.getColIndexById("userId")).getValue()+"',"
		}
	}
	
	var bgcode=$('#MbgGroup').val();
	var mgcode=$('#MmgGroup').val();
	var sgcode=$('#MsgGroup').val();
	
	if(''==bgcode||null==bgcode){
		alert(lang.admin.alert.recUser44)
		return false;
	}
	
	dataStr={
			"userid":idString,
			"bgcode":bgcode,
			"mgcode":mgcode,
			"sgcode":sgcode
	}
	
	
	$.ajax({
		url:contextPath+"/multiGroupModify.do",
		data:dataStr,
		type:"POST",
		dataType:"json",
		async: false,
		success:function(jRes){
			if(jRes.success == "Y") {
				alert(lang.admin.alert.recUser34)
				layer_popup_close()
				userManageGrid.clearAndLoad(contextPath+"/userManageRecGrid.xml"+formToSerialize())
				progress.off()
			}else
				alert(lang.admin.alert.recUser25);
		}
	});
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

				$("#lockDate").val(rLockType);
				$("#lockNum").val(rLockCount);
				$("#lockPolicy").val(rLockUse).change();
				$("#tryNum").val(rTryCount);
				$("#tryPolicy").val(rTryUse).change();
				$("#pastNum").val(rPastPwCount);
				$("#pastPolicy").val(rPastPwUse).change();
				$("#cycleDate").val(rCycleType).change();
				$("#cycleNum").val(rCycle);
				$("#cyclePolicy").val(rCycleUse).change();
				$("#pwPolicy").val(rPolicyUse).change();
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


	if(cycleUse!="N" && (!cycle || cycle < 1)){
		alert(lang.admin.alert.recUser22)
		$("#cycleNum").focus();
		return;
	}else if(pastPwUse == "C" && (!pastPwCount || pastPwCount < 2)){
		alert(lang.admin.alert.rec23)
		$("#pastNum").focus();
		return;
	}else if(lockUse == "Y" && ( !lockCount || lockCount<1)) {
		alert(lang.admin.alert.recUser23)
		$("#lockNum").focus();
	}else if(lockUse == "Y" && ( !lockType || lockType<1)) {
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

function TreeEvent(){
	$(document).find('span').on("click",function(event){
//		event.stopPropagation();
//		event.stopImmediatePropagation();
//		$(document).find('.tree_user_part_active').removeClass('tree_user_part_active');
//		// Active 활성화
//		$(this).parents("li").addClass('tree_user_part_active');
//		setTimeout(function(){
//			$(document).find("li").removeClass('tree_user_part_active');
//		},50)

		$(this).parents("li").find("ul").css("display","");
		// 트리 클릭 시 우측 사용자 그리드 필터링 적용
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
		userManageGrid.filterBy(columnNum,clickedId)
	})
}


function sessionDestroyUser(rowId){
	// DB 조회 ajax 처리
		$.ajax({
		url:contextPath+"/disconnect.do",
		data:{
			userID : userManageGrid.cells(rowId,userManageGrid.getColIndexById("userId")).getValue(),
			ruser : "Y"
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

function formToSerialize(requestOrderBy){
	
	$(".inputFilter").trigger('blur');
	$(".inputFilter").trigger('keyup');
	
	var resultValue = "";

	$(".main_form").children().each(function (i){
		
		var id = this.id;
		var value = ($(this).val()) ? $(this).val().trim() : "";
		var $obj = $(this);
		
		if (value == "" || value == undefined || value == null)
			return;
			// 한글 파라미터의 경우 인코딩해서 보내야함
			resultValue += (resultValue.length > 0?"&":"?") + id+"="+encodeURI(encodeURIComponent(value));
		
	});

//	var authy = $("#Authy").val();
//	if(authy!=null){
//		resultValue += (resultValue.length > 0?"&":"?") +"userLevel="+userLevel;
//	}
	
	
	if (requestOrderBy != undefined && requestOrderBy.split("|").length == 2){
		var orderBy = requestOrderBy.split("|")[0]
		var direction = requestOrderBy.split("|")[1]
		resultValue += (resultValue.length > 0?"&":"?") + "orderBy="+orderBy + "&direction="+direction;

	}
	
	if(treeView.getSelected()!=null && treeView.getSelected()!=''&&treeView.getSelected()!='all'){
			var level = treeView.getLevel(treeView.getSelected());

		switch (level) {
		case 2:
			var bgCode = treeView.getSelected().split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"bgCode="+bgCode;
			break;
		case 3:
			var mgCode = treeView.getSelected().split("_")[0];
			var bgCode = treeView.getParentId(treeView.getSelected()).split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"mgCode="+mgCode+"&bgCode="+bgCode;
			break;
		case 4:
			var sgCode = treeView.getSelected().split("_")[0];
			var mgCode = treeView.getParentId(treeView.getSelected()).split("_")[0];
			var bgCode = treeView.getParentId(treeView.getParentId(treeView.getSelected())).split("_")[0];
			resultValue+= (resultValue.length > 0?"&":"?")+"sgCode="+sgCode+"&mgCode="+mgCode+"&bgCode="+bgCode;
			break;
		}
	}
	
	
	return resultValue;
}

