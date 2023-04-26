window.onload = function(){
	channelManageLoad();
}

var gridChannelManage;

// 채널관리 로드
function channelManageLoad() {
    /*// 채널관리 Grid
	gridChannelManage = new dhtmlXGridObject({
		parent: "gridChannelManage"
	});
	gridChannelManage.setHeader("체크,채널번호,시스템,내선번호,IP,녹취 사용 여부,녹취 종류,,");
    gridChannelManage.attachHeader("#rspan,#rspan,#text_filter,#text_filter,#text_filter,#select_filter,#select_filter,#rspan,#rspan");
	gridChannelManage.setInitWidths("50,100,150,150,250,100,150,150,*");
	gridChannelManage.setColAlign("center,center,center,center,center,center,center,center,center");
	gridChannelManage.setColTypes("ch,ed,ed,ed,ed,ed,ed,ed,ed");
    gridChannelManage.setImagePath(recseeResourcePath + "/images/project/");
    gridChannelManage.enablePaging(true, 20, 5, "pagingChannelManage", true);
    gridChannelManage.setPagingSkin("toolbar","dhx_web");
	gridChannelManage.init();
    gridChannelManage.parse(
            function(){
                return {
                    "rows":[
                        { "id":1001, "data":[
                            "ch",
                            "1",
                            "A001",
                            "1001",
                            "192.168.0.188",
                            "사용",
                            "전수녹취",
                            "<button class='ui_btn_white icon_btn_gear_gray'>수정</button>"
                        ]},
                        { "id":1002, "data":[
	                        "ch",
	                        "2",
	                        "A001",
	                        "1002",
	                        "192.168.0.188",
	                        "사용",
	                        "전수녹취",
	                        "<button class='ui_btn_white icon_btn_gear_gray'>수정</button>"
                        ]},
                        { "id":1003, "data":[
	                        "ch",
	                        "3",
	                        "A001",
	                        "1003",
	                        "192.168.0.188",
	                        "사용",
	                        "전수녹취",
	                        "<button class='ui_btn_white icon_btn_gear_gray'>수정</button>"
                        ]}
                    ]}
                }(), "json"
        );

    $('#startDate').datepicker();
    $('#endDate').datepicker();
*/
    ui_controller();

}