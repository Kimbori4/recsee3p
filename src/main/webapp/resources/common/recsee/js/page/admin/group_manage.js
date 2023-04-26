window.onload = function(){
	groupManageLoad();
}

var gridGroupManage;

// 그룹 관리 로드
function groupManageLoad() {
    // 그룹 관리 Grid
	gridGroupManage = new dhtmlXGridObject({
		parent: "gridGroupManage"
	});
	gridGroupManage.setHeader("체크,순번,사용자 이름,사용자 ID,내선번호,연락처,권한등급,대분류,중분류,소분류,사원번호,이메일,CTI ID,");
    gridGroupManage.attachHeader("#rspan,#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#select_filter,#select_filter,#select_filter,#select_filter,#text_filter,#text_filter,#text_filter,#rspan");
	gridGroupManage.setInitWidths("50,50,100,150,100,100,150,100,80,100,100,180,100,*");
	gridGroupManage.setColAlign("center,center,center,center,center,center,center,center,center,center,center,center,center,center");
	gridGroupManage.setColTypes("ch,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed");
	gridGroupManage.setColSorting("str,str,str,str,str,str,str,str,str,str,str,str,str,str");
    gridGroupManage.setImagePath(recseeResourcePath + "/images/project/");
    gridGroupManage.enablePaging(true, 20, 5, "pagingGroupManage", true);
    gridGroupManage.setPagingSkin("toolbar","dhx_web");
	gridGroupManage.init();
    gridGroupManage.parse(
            function(){
                return {
                    "rows":[
                        { "id":1001, "data":[
                            "ch",
                            "1",
                            "퓨렌스",
                            "Agent",
                            "1004",
                            "010-1234-1234",
                            "미지정",
                            "A",
                            "B",
                            "C",
                            "16-124578",
                            "furence@furence.com",
                            "11-2017"
                        ]},
                        { "id":1002, "data":[
							"ch",
							"2",
							"퓨렌스",
							"Agent",
							"1004",
							"010-1234-1234",
							"일반 상담원",
							"A",
							"B",
							"C",
							"16-124578",
							"furence@furence.com",
							"11-2017"
                        ]},
                        { "id":1003, "data":[
							"ch",
							"3",
							"퓨렌스",
							"Admin",
							"1004",
							"010-1234-1234",
							"관리자",
							"A",
							"B",
							"C",
							"16-124578",
							"furence@furence.com",
							"11-2017"
                        ]}
                    ]}
                }(), "json"
        );

    ui_controller();

}