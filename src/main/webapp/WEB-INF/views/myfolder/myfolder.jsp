<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/myfolder/myfolder.css" />

	<script>
		var folderSize = "${myFolder.size()}";
		var myfolderSideBar,detailGrid,myfolder_toolbar,selectFolder,rc;

		$(function() {
			top.playerVisible(true);
		    $(window).resize(function() {
		    	// 현재 document 높이
				var documentHeight = $(window).height();
				var playerHeight = $('.player_pannel').height();

				// 그리드 위의 높이 값
				var sideBarCalcHeight = $("#myfolderSideBarObj").offset().top;

				// 페이징이 있음 페이징 만큼 뺴주깅
				var sideBarResultHeight = (documentHeight - sideBarCalcHeight - playerHeight);

				$("#myfolderSideBarObj").css({"height": + (sideBarResultHeight - 2) + "px"})

				if(myfolderSideBar != null)
					myfolderSideBar.setSizes(0);
		    }).resize();

		})

	    //	마이폴더 생성로직
	    function myfolderLoad(){
	    	myfolderSideBar = new dhtmlXSideBar({
	    		icons_path : "../resources/component/fontawesome-free-5.0.8/advanced-options/raw-svg/",
	    		skin : "dhx_web",
	    		template : "icons_text",
	    		width : 100,
	    		header : true,
	    		parent : "myfolderSideBarObj",
	    		items : [
	    			{id:"",text:"",icon: "",selected : true},
               		<c:forEach items="${myFolder}" var = "list" varStatus="status">
           			<c:choose>
           				<c:when test="${status.first}">
               					{id:"${list.getrFolderName()}",text:"${list.getrFolderName()}",icon: "regular/folder-open.svg",selected : true},
               				</c:when>
               				<c:otherwise>
               					{id:"${list.getrFolderName()}",text:"${list.getrFolderName()}",icon: "regular/folder.svg",selected : false},
               				</c:otherwise>
               			</c:choose>
        			</c:forEach>
	    		]
	    	})

    		$('.dhxsidebar_item')[0].style.display = "none";
	    	<c:forEach items="${myFolder}" var = "list" varStatus="status">
	    		<c:if test="${status.first}">
	    			selectFolder = "${list.getrFolderName()}";
	    			createGrid("${list.getrFolderName()}");
	    			//createSideBarHdrMenu();
	    		</c:if>
	    	</c:forEach>
    		createSideBarHdrMenu();
    		//myfolderSideBar.items("").hide();

	/*        	if(folderSize == "0"){
	       		setTimeout(function(){
	       			myfolderSideBar.cont.innerHTML +='<div class="dhx_cell_sidebar" style="left: 100px; top: 0px; width: 1167px; height: 693px; visibility: visible; z-index: 1;"><div class="dhx_cell_sidebar_hdr"><div class="dhx_cell_sidebar_hdr_text"></div></div></div>';
	       			createSideBarHdrMenu();
	       		},300)
	    	} */

	    	myfolderSideBar.attachEvent("onSelect", function(id){
	    		selectFolder = myfolderSideBar.cells(id).getText().text;
	    		//	폴더 선택시 아이콘 변경 로직
	    		myfolderSideBar.forEachCell(function(ids){
	    			if(ids._idd == id)
	    				ids.setText({icon:'/regular/folder-open.svg'})
	    			else
	    				ids.setText({icon:'/regular/folder.svg'})
	    		})

	    		createGrid(id);
	    		createSideBarHdrMenu();
	    	})

	    	ui_controller();
	    }


		//	그리드 그리기
		function createGrid(objGrid){
			//	사이드바 그리드 페이징  처리 해주기 위해서 처리
			myfolder_toolbar = myfolderSideBar.cells(objGrid).attachStatusBar({
				text : "<div id='"+objGrid+"pagingBox'></div>",
				paging : true
			})

			detailGrid = myfolderSideBar.cells(objGrid).attachGrid();
    		detailGrid.setImagePath(recseeResourcePath + "/images/project/");

    		// 그리드에 페이징
    		detailGrid.i18n.paging = i18nPaging[locale];
    		detailGrid.enablePaging(true,"100", 10, objGrid+"pagingBox", true);
    		detailGrid.setPagingWTMode(true,true,true,[100,250,500]);
    		detailGrid.setPagingSkin("toolbar","dhx_web");

	    	detailGrid.load(contextPath+"/myfolderGrid.xml?folderName="+encodeURIComponent(encodeURIComponent(objGrid)),function(){

		    	detailGrid.attachEvent("onXLS", function(){
					progress.on()
				});

				// 파싱완료
				detailGrid.attachEvent("onXLE", function(grid_obj,count){
					progress.off();
				});


		    	//체크박스 전체 선택
		    	detailGrid.attachEvent("onHeaderClick",function(ind, obj){
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

		    	//	더블 클릭시 파일 재생
		    	detailGrid.attachEvent("onRowDblClicked", function(id,ind){
		    		checkSoundDevice(this,id);
		    	});

		    	//버튼 추가
		    	try{
		    		addButton();
		    		myfolderSideBar.setSizes(0);
	    		}catch(e){}
	    	});
		}

		//	폴더 생성
		function createMyFolder(){
			layer_popup('#addMyfolderPopup');
		}

		function addMyfolder(str){
			if(str == ""){
				alert(lang.myfolder.alert.inputName); <%-- 폴더명을 입력해주세요. --%>
			}else{
				$.ajax({
						url : contextPath + "/createMyfolder.do"
					,	data : {
							folderName : str
						}
					,	type:"POST"
					,	dataType:"json"
					,	async: false
					,	success:function(jRes){
						if(jRes.success == "Y"){
							alert(lang.myfolder.alert.completingAdd); <%-- 폴더 추가가 완료 되었습니다. --%>
							myfolderSideBar.addItem({id:str,text:str,icon: "regular/folder.svg",selected : false})
							//	팝업 닫기
							layer_popup_close("#addMyfolderPopup")
							addFolderName.value = "";

							// 선택파일 이동 select 에도 추가
							myfolderSelect.innerHTML += '<option value='+str+'>'+str+'</option>';
						}else{
							alert(lang.myfolder.alert.existSameFolder); <%-- 동일한 폴더가 존재합니다. 다시 입력해주세요. --%>
						}
					}
				})
			}
		}
		//	폴더 수정
		function modifyMyfolderPopup(){
			selectFolder = myfolderSideBar.cells(myfolderSideBar.getActiveItem()).getText().text;
			modifyFolderName.value = selectFolder;
			if(selectFolder == ""){
				alert(lang.myfolder.alert.selectFolder); <%-- 폴더를 선택해주세요. --%>
				return false;
			}
			layer_popup('#modifyMyfolderPopup');
		}

		function modifyMyfolder(str,before){
			if(str == "" && before == ""){
				alert(lang.myfolder.alert.inputName); <%-- 폴더명을 입력해주세요. --%>
			}else{
				$.ajax({
					url : contextPath + "/modifyMyfolder.do"
				,	data : {
						oldrFolderName : before,
						folderName : str
					}
				,	type:"POST"
				,	dataType:"json"
				,	async: false
				,	success:function(jRes){

					alert(lang.myfolder.alert.editingFolder); <%-- 폴더 수정이 완료 되었습니다. --%>

					//	팝업 닫기
					myfolderSideBar.cells(myfolderSideBar.getActiveItem()).setText({id:str,text:str})

					//myfolderSideBar.cells(selectFolder).remove();
					//myfolderSideBar.addItem({id:str,text:str,icon: "regular/folder.svg",selected : true})

					//폴더 수정 하고 나면 폴더 이동시 폴더 이름이 변경되기 때문에 찾아서 바꿔줌
					var option = myfolderSelect.getElementsByTagName('option');

				 	for(var i = 0 ; i < option.length ; i++){
						 if(option[i].value == before){
							option[i].value = str;
							option[i].innerHTML = str;
						}
					}

					layer_popup_close("#modifyMyfolderPopup");
					//수정하고나면 오른쪽 상단 메뉴 없어져서 다시 만들어줌
					createSideBarHdrMenu();
				}
			})
			}
		}

		//	폴더 삭제
		function deleteMyFolder(){
			selectFolder = myfolderSideBar.cells(myfolderSideBar.getActiveItem()).getText().text;
			if(selectFolder == ""){
				alert(lang.myfolder.alert.selectFolder); <%-- 폴더를 선택해주세요. --%>
				return false;
			}

			if(confirm(lang.myfolder.alert.deletingFolder)){ <%-- 정말로 삭제하시겠습니까? --%>
				progress.on();
				$.ajax({
						url : contextPath + "/deleteMyfolder.do"
					,	data : {
							folderName : selectFolder
						}
					,	type:"POST"
					,	dataType:"json"
					,	async: true
					,	complete : function (){
						progress.off();
					}
					,	success:function(jRes){
						//	삭제후 마지막 폴더 오픈 이미지 변경
						myfolderSideBar.cells(selectFolder).remove();
						 setTimeout(function(){
							 try{
								 myfolderSideBar.cells(myfolderSideBar.getActiveItem()).setText({icon:'/regular/folder-open.svg'});
								 $('.dhx_cell_sidebar_hdr_text').find(".sidebar_float_right").remove();
								 createSideBarHdrMenu();
							 }catch(e){}
						},300)
					}
				})
			}

			//myfolderSideBar.cells(myfolderSideBar.getActiveItem()).setText({icon:'/regular/folder-open.svg'})
		}

		function createSideBarHdrMenu(){
			$('.dhx_cell_sidebar_hdr_text').append('<span class="sidebar_float_right" onclick="modifyMyfolderPopup()"><i class="far fa-edit sidebar_icon"></i>'+lang.myfolder.button.editFolder+'</span>'); <%-- 폴더 수정 --%>
			$('.dhx_cell_sidebar_hdr_text').append('<span class="sidebar_float_right">|</span>');
			$('.dhx_cell_sidebar_hdr_text').append('<span class="sidebar_float_right" onclick="deleteMyFolder()"><i class="fas fa-minus sidebar_icon"></i>'+lang.myfolder.button.deleteFolder+'</span>'); <%-- 폴더 삭제 --%>
			$('.dhx_cell_sidebar_hdr_text').append('<span class="sidebar_float_right">|</span>');
			$('.dhx_cell_sidebar_hdr_text').append('<span class="sidebar_float_right" onclick="createMyFolder()"><i class="fas fa-plus sidebar_icon"></i>'+lang.myfolder.button.addFolder+'</span>'); <%-- 폴더 추가 --%>
		}

		function addButton(){
    		var aToolbar = detailGrid.aToolBar;

    		aToolbar.addButton("addItem",12, lang.myfolder.button.addFile, "plus.svg", "plus.svg"); <%-- 선택파일 리스트에 추가 --%>
    		aToolbar.addButton("moveItem",13, lang.myfolder.button.moveFile, "exchange-alt.svg", "exchange-alt.svg"); <%-- 선택파일 이동 --%>
    		aToolbar.addButton("removeItem",14, lang.myfolder.button.removeFile, "trash-alt.svg", "trash-alt.svg"); <%-- 선택파일 마이폴더에서 제거 --%>

    		aToolbar.addSpacer("perpagenum");


    		aToolbar.attachEvent("onClick", function(name){
    			switch(name) {
    				case "addItem":
    					addPlayList();
					break;
    				case "moveItem":
    					selectFolder = myfolderSideBar.cells(myfolderSideBar.getActiveItem()).getText().text;
    					var checkItem  = detailGrid.getCheckedRows(0);
    					if(checkItem == ""){
    						alert(lang.myfolder.alert.notExistFile); /* 선택한 녹취 파일이 없습니다. */
    					}else{
    						layer_popup("#moveMyfolderPopup");
    						myfolderSelect.value = selectFolder;
    					}
					break;
    				case "removeItem":
    					var checkItem  = detailGrid.getCheckedRows(0);
    					if(checkItem == ""){
    						alert(lang.myfolder.alert.notExistFile); /* 선택한 녹취 파일이 없습니다. */
    					}else{
    						removeItem();
    					}
    				break;
    				default:
    				break;
    			}
    		})
		}

		/*
		|	아이템 관련 정리
		|
		*/

		//	선택파일 이동
		function moveItem(){
			var checkItem = detailGrid.getCheckedRows(0);
			$.ajax({
				url : contextPath + "/moveMyfolderItem.do"
			,	data : {
						itemIndex : checkItem
					,	rFolderName : myfolderSelect.value
				}
			,	type:"POST"
			,	dataType:"json"
			,	async: false
			,	success:function(jRes){
				if(jRes.success == "Y"){
					//	DB 삭제후 로우 삭제
					checkItem.split(",").forEach(function(element,index,array){
						detailGrid.deleteRow(element);
					})
					layer_popup_close("#moveMyfolderPopup");
				}else{
					alert(lang.myfolder.alert.alreadyRecorded); /* 해당 폴더에 이미 녹취파일이 있습니다. */
				}
			}
		})

		}

		function removeItem(){
			var checkItem = detailGrid.getCheckedRows(0);
			if(confirm(lang.myfolder.alert.deletingFolder)){ /* 정말로 삭제하시겠습니까? */
				$.ajax({
						url : contextPath + "/deleteMyfolderItem.do"
					,	data : {
								itemIndex : checkItem
							,	rFolderName : selectFolder
						}
					,	type:"POST"
					,	dataType:"json"
					,	async: false
					,	beforeSend : function(){
					}
					,	success:function(jRes){
						//	DB 삭제후 로우 삭제
						checkItem.split(",").forEach(function(element,index,array){
							detailGrid.deleteRow(element);
						})

						if($("#allcheck>img").attr("src").match("item_chk1.gif") != null){
							$("#allcheck>img").attr("src", recseeResourcePath + "/images/project/dhxgrid_web/item_chk0.gif");
						}

					}
				})
			}
		}

		/*
		|	파일 재생
		*/
		function checkSoundDevice(objGrid,id){
			try {
				var taudio = new Audio();
				var src = contextPath+"/getRecFileTest.do?fileName=beef.mp3"
				taudio.autoplay = true;
				taudio.volume = 0;
				taudio.src = src;
		    	taudio.pause();
				taudio.load();
				taudio.play();

				taudio.addEventListener('error',function(event) {
					taudio.pause();
					alert(lang.views.search.alert.msg32) /* 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. */
		        });

				taudio.addEventListener('loadeddata',function(){
					try {
						taudio.currentTime=0.1
						taudio.pause();
						play(objGrid, id);
						return true;
					} catch(e){
						taudio.pause();
						alert(lang.views.search.alert.msg32) /* 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. */
					}
				});
			}catch(e){
				taudio.pause();
				alert(lang.views.search.alert.msg32) /* 컴퓨터에 음성파일을 재생 가능한 장치가 없습니다.\n컴퓨터에 재생 가능한 장치를 먼저 연결 후 청취 시도를 해주세요. */
			}
		}

		function addPlayList(){
	 		if(detailGrid.getCheckedRows(0) != "" ) {
				var checked = detailGrid.getCheckedRows(0).split(",");

				for( var i = 0 ; i < checked.length ;i++ ) {
					rc = top.nowRc;
					var CheckMode=top.playerFrame.$("#playerObj .btn_list_del").css("display");
					 if(CheckMode!="none"){
						alert(views.search.alert.msg3); /* 플레이리스트 편집모드에서는 파일리스트 추가가 불가능 합니다. */
						return;
					}else{
						var id = checked[i];

						var recDate 		= (detailGrid.getColIndexById("r_rec_date")==undefined ? 	"" : detailGrid.cells(id,detailGrid.getColIndexById("r_rec_date")).getValue());
						var recTime			= (detailGrid.getColIndexById("r_rec_time")==undefined ? 	"" : detailGrid.cells(id,detailGrid.getColIndexById("r_rec_time")).getValue());
						var recExt			= (detailGrid.getColIndexById("r_ext_num")==undefined ? 	"" : detailGrid.cells(id,detailGrid.getColIndexById("r_ext_num")).getValue());
						var recCustPhone	= (detailGrid.getColIndexById("r_cust_phone1")==undefined?	"" : detailGrid.cells(id,detailGrid.getColIndexById("r_cust_phone1")).getValue());
						var recUserName 	= (detailGrid.getColIndexById("r_user_name")==undefined? 	"" : detailGrid.cells(id,detailGrid.getColIndexById("r_user_name")).getValue());

			 			// 녹취 파일 정보

						var recFileData = {
			 					"listenUrl"			: recDate+recTime+recExt
							,	"recDate"		: recDate.replace(/-/gi,'')			// 녹취일
							,	"recTime"		: recTime				// 녹취시간
							,	"recExt"		: recExt				// 내선버노
							,	"recCustPhone"	: recCustPhone			// 고객 저나버노
							,	"recUserName"	: recUserName			// 상담원 명
						}
						rc.addPlayList(recFileData);
					}
				}


			}
		}

		//	재생 이벤트
		function play(gridObj, id){
			rc = top.nowRc;
			var recFileData;
			var listenUrl = "";
			var recDate 		= (gridObj.getColIndexById("r_rec_date")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_rec_date")).getValue());
			var recTime			= (gridObj.getColIndexById("r_rec_time")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_rec_time")).getValue());
			var recExt			= (gridObj.getColIndexById("r_ext_num")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_ext_num")).getValue());
			var recUserName		= (gridObj.getColIndexById("r_user_name")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_user_name")).getValue());
			var recCustName		= (gridObj.getColIndexById("r_cust_name")==undefined ? 	"" : gridObj.cells(id,gridObj.getColIndexById("r_cust_name")).getValue());
			$.ajax({
				url:contextPath+"/getListenUrl.do",
				data:{
						"recDate" : recDate
					,	"recTime" : recTime
					,	"recExt" : recExt
					,	"recUserName" : recUserName
					,	"recCustName" : recCustName
				},
				type:"POST",
				dataType:"json",
				async: false,
				success:function(jRes){
					if(jRes.success=="Y"){
						listenUrl=jRes.resData.ListenUrl;
						recFileData = {
								"listenUrl"		: listenUrl
							,	"recDate"		: recDate	  			// 녹취일
							,	"recTime"		: recTime				// 녹취시간
							,	"recExt"		: recExt				// 내선버노
							,	"recUserName" 	: recUserName			// 상담사명
							,	"recCustName" 	: recCustName			// 고객명
						}
						listenLog(recFileData);
						$(top.myTabbar.tabs(top.myTabbar.getActiveTab()).cell).find('#playerFrame')[0].contentWindow.$(".play_list").find(".play_info").find("span").html(lang.views.player.html.text2/*"현재 재생 중인 파일 : "*/);

					}else {
						alert(lang.views.search.alert.msg26); /* 요청에 실패 하였습니다. */
					}
				}
			});
			rc.recFileData = recFileData;
			rc.setFile("audio", encodeURI(listenUrl), undefined, true);
			rc.listenUrl =encodeURI(listenUrl)
		}
		
		function listenLog(recFileData,justPath){
			var url = recFileData.listenUrl

			$.ajax({
				url:contextPath+"/listenLog.do",
				data:recFileData,
				type:"POST",
				dataType:"json",
				async: true,
				cache: false,
				success:function(jRes){}
			});

			return url;
		}

	</script>
</head>
<body onload='myfolderLoad();'>
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>

    <div class="main_contents">
		<div id="myfolderSideBarObj"></div>
	</div>

	<%-- 폴더  추가  --%>
	<div id="addMyfolderPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
					<div class="ui_pannel_popup_header">
							<div class="ui_float_left">
									<p class="ui_pannel_tit"><spring:message code="common.label.addFolder"/></p>
							</div>
							<div class="ui_float_right">
									<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
							</div>
					</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="common.label.folderName"/></label>
                    		<input class="" id="addFolderName" value="" type="text" maxlength="15"/>
                    </div>
                  </div>
			</div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addMyfolderItem" onclick="addMyfolder(addFolderName.value);" class="ui_main_btn_flat"><spring:message code="message.btn.add"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#addMyfolderPopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
		</div>
	</div>

	<%-- 폴더 수정--%>
	<div id="modifyMyfolderPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
					<div class="ui_pannel_popup_header">
							<div class="ui_float_left">
									<p class="ui_pannel_tit"><spring:message code="common.label.editFolder"/></p>
							</div>
							<div class="ui_float_right">
									<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
							</div>
					</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="common.label.folderName"/></label>
                    		<input class="" id="modifyFolderName" value="" type="text" maxlength="15"/>
                    </div>
                  </div>
			</div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="addMyfolderItem" onclick="modifyMyfolder(modifyFolderName.value,selectFolder);" class="ui_main_btn_flat"><spring:message code="message.btn.modify"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#modifyMyfolderPopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
		</div>
	</div>

	<%-- 선택파일 폴더 이동 --%>
	<div id="moveMyfolderPopup" class="popup_obj">
		<div class="ui_popup_padding">
			<div class="popup_header">
					<div class="ui_pannel_popup_header">
							<div class="ui_float_left">
									<p class="ui_pannel_tit"><spring:message code="common.label.selectMoveFile"/></p>
							</div>
							<div class="ui_float_right">
									<button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
							</div>
					</div>
			</div>
			<div class="ui_article ui_row_input_wrap">
				 <div class="ui_pannel_row">
                    <div class="ui_padding">
                    	<label class="ui_label_essential"><spring:message code="common.label.selectFolder"/></label>
                    	<select id='myfolderSelect'>
                    		<c:forEach items="${myFolder}" var = "list" >
                    			<option value="${list.getrFolderName()}">${list.getrFolderName()}</option>
                    		</c:forEach>
                   		</select>
                    </div>
                  </div>
			</div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="moveMyfolderItem" onclick="moveItem();" class="ui_main_btn_flat"><spring:message code="common.label.move"/></button>
                        <button class="ui_main_btn_flat" onclick='layer_popup_close("#moveMyfolderPopup")'><spring:message code="message.btn.cancel"/></button>
                    </div>
                </div>
            </div>
		</div>
	</div>

</body>
<script>


</script>
</html>