<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.furence.recsee.common.model.MMenuAccessInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/include/commonVar.jsp" %>

	<%-- css page --%>
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/user_manage.css" />
	<link rel="stylesheet" type="text/css" href="${recseeResourcePath }/css/page/admin/admin.css" />

	<%-- js page --%>
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/delete_recfile_manage.js"></script>
	<script>
	$(function() {
	    $(window).resize(function() {
	    	// ���� document ����
			var documentHeight = $(window).height();

			// �׸��� ���� ���� ��
			var gridCalcHeight = $("#delMenuUserManageGrid").offset().top;

			// ����¡�� ���� ����¡ ��ŭ ���ֱ�
			var gridResultHeight = documentHeight;

			// �� ��ȯ ���� ���� ������ ���Ƿ� ���
			if(tabMode=="Y"){
				$(".group_list_wrap").css({"height": + (gridResultHeight - 52) + "px"});
				$("#delMenuUserManageGrid").css({"height": + (gridResultHeight - 53) + "px"});
				$("#delMenuTreeViewAgent").css({"height": + (gridResultHeight - 53) + "px"});
			} else{
				$(".group_list_wrap").css({"height": + (gridResultHeight - 141) + "px"});
				$("#delMenuUserManageGrid").css({"height": + (gridResultHeight - 310) + "px"});
				$("#delMenuTreeViewAgent").css({"height": + (gridResultHeight - 310) + "px"});
			}
	    }).resize();
	})
	</script>

	<style>
		.group_tree_pannel{
			border-left: 1px solid #bbbbbb;
		}
		.tree_view_wrap{
			margin-top: 48px;
		}
        #authyGrid{
            position: relative;
            clear: both;
            float: left;
			width: 100% !important;
			/* height: 858px !important; */
        }
        #delRecfileInfoModify {
        	width:422px;
        }
	</style>
</head>
<body onload="deleteRecfileManageLoad()">
	<c:choose>
		<c:when test="${tabMode eq 'Y'}">
			<%@ include file="../common/headerTab.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="../common/header.jsp" %>
		</c:otherwise>
	</c:choose>
    <div class="ui_layout_pannel">
		<%@ include file="./admin_menu.jsp" %>
	    <div class="main_contents admin_body">
			<div class="ui_acrticle group_tree_pannel">
				<div class="ui_pannel_row">
			        <div class="ui_float_left" style="margin:3.5px; padding:6px 10px;">
						<spring:message code="admin.select.title.selectGroup"/>
					</div>
		            <div class="ui_float_right">
		            </div>
		        </div>
	       		<div class="tree_view_wrap">
		            <div class="tree_agent_view" id="delMenuTreeViewAgent" style="position:absolute;">
	       				<div id="rowDisabled" style="width:100%; height:100%; background-color:lightgray; position:fixed; z-index:1; opacity:0.5; display:none;" ></div>
	       			</div>
				</div>
			</div>
			<div class="ui_acrticle delRecfile_manage_pannel">
			    <div class="ui_pannel_row">
			    
			        <div class="ui_float_left" >
						<spring:message code="admin.select.title.useDelOption"/>
						<select id="recseeDelete" style="width:70px; margin-top:3.5px; margin-left:8px; margin-right:15px;">
							<option value="Y" selected="selected"><spring:message code="admin.detail.option.use"/></option>
							<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
						</select>
					</div>
					<div class="delOptionWrap" style="display:inline">
			            <div class="ui_float_right">
							<button id="delMenuRangeSaveBtn" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="admin.button.save"/></button>
							<button id="delMenuCheckedRangeDeleteBtn" class="ui_btn_white icon_btn_trash_gray"></button>
			            </div>
		            </div>
		        </div>
		        
		        <div class="ui_pannel_row">
	      	  		<div class="ui_float_left" style="margin-top:3px; padding:2px;">
		      	  		<div class="delOptionWrap" style="display:inline">				                                        
		      	  			삭제 스케줄 이름 <input id="delScheduleName" type="text" style="width:150px; text-align:left; margin-left:8px; margin-right:15px; "/>
		      	  			
							<spring:message code="admin.select.title.delType"/>
		                        <select id="delType" style="width:200px; margin-left:8px; margin-right:15px;">
									<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
									<option value="H"><spring:message code="admin.select.value.delType.H"/></option>
									<option value="D"><spring:message code="admin.select.value.delType.D"/></option>
									<option value="F"><spring:message code="admin.select.value.delType.F"/></option>
									<option value="A"><spring:message code="admin.select.value.delType.A"/></option>
								</select> 
	
							<spring:message code="admin.select.title.delFileType"/>
		                        <select id="delFileType" style="width:200px; margin-left:8px; margin-right:15px;">
									<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
									<option value="M"><spring:message code="admin.select.value.delFileType.M"/></option>
									<option value="R"><spring:message code="admin.select.value.delFileType.R"/></option>
									<option value="A"><spring:message code="admin.select.value.delFileType.A"/></option>
								</select> 
								
							</div>
						</div>
					</div>
				<div class="ui_pannel_row">
					<div class="ui_float_left" style="margin-top: 3px; padding: 2px;">
						<spring:message code="admin.select.title.delFilePathType"/>
		                    <select id="delFilePathType" style="width:100px; margin-left:8px; margin-right:15px;">
		                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="R"><spring:message code="admin.select.value.delFilePathType.R"/></option>
								<option value="S"><spring:message code="admin.select.value.delFilePathType.S"/></option>
								<option value="A"><spring:message code="admin.select.value.delFilePathType.A"/></option>
							</select>
							
						<spring:message code="admin.select.title.delPeriod"/>
	                        <input id="delYear"  type="text" maxlength="1" style="width:30px; text-align:right; margin-left:8px; padding-right:5px;" title="" placeholder="0"/> <spring:message code="admin.select.title.delYear"/>
							<input id="delMonth" type="text" maxlength="2" style="width:30px; text-align:right; margin-left:8px; padding-right:5px;" title="" placeholder="0" /> <spring:message code="admin.select.title.delMonth"/>
							<input id="delDay" type="text" maxlength="3" style="width:30px; text-align:right; margin-left:8px; padding-right:5px;" title="" placeholder="0" /> <spring:message code="admin.select.title.delDay"/>
					
						<spring:message code="admin.select.title.delPeriodOffset"/>
	                        <input id="delYearOffset"  type="text" maxlength="1" style="width:30px; text-align:right; margin-left:8px; padding-right:5px;" title="" placeholder="0"/> <spring:message code="admin.select.title.delYear"/>
							<input id="delMonthOffset" type="text" maxlength="2" style="width:30px; text-align:right; margin-left:8px; padding-right:5px;" title="" placeholder="0" /> <spring:message code="admin.select.title.delMonth"/>
							<input id="delDayOffset" type="text" maxlength="3" style="width:30px; text-align:right; margin-left:8px; padding-right:5px;" title="" placeholder="0" /> <spring:message code="admin.select.title.delDay"/>
					</div>
				</div>
				<div class="ui_pannel_row">
		      	  	<div class="ui_float_left" style="margin-top:3px; padding:2px;">
		      	  		삭제 경로 <input id="delPath" type="text" style="width:350px; text-align:left; margin-left:8px; padding-right:5px;"/>
		      	  		
		      	  		스토리지 전송 체크
		                    <select id="storageSendChk" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="Y" ><spring:message code="admin.detail.option.use"/></option>
								<option value="N" selected="selected"><spring:message code="admin.detail.option.notUse"/></option>
							</select>
		      	 	</div>
		      	 </div>
				<div class="ui_pannel_row">
		      	  	<div class="ui_float_left" style="margin-top:3px; padding:2px;">
		      	  		<spring:message code="admin.select.title.logType"/>
		                    <select id="logType" style="width:100px; margin-left:8px; margin-right:15px;">
		                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="A"><spring:message code="admin.select.value.logType.A"/></option>
								<option value="M"><spring:message code="admin.select.value.logType.M"/></option>
							</select>
								
			      		 <label>삭제 일정</label>
		        			<select id="SchecheckType" style="width:90px; margin-left:8px; margin-right:15px;" class="miniSelect">
	             				<option value="D" selected><spring:message code="admin.detail.option.daily"/></option>		                            
	                            <option value="W"><spring:message code="admin.detail.option.weekly"/></option>
	                            <option value="M"><spring:message code="admin.detail.option.monthly"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.noSche"/></option>
                 			</select>
                 			
                     	<label class="backupWeekBox" style="display:none;"><spring:message code="admin.backup.label.week"/></label>
                        	<select id="backWeek" style="width:100px; margin-left:8px; margin-right:15px; display:none;" class="miniSelect">
                        		<option value="*" disabled selected>-</option>
              					<option value="1"><spring:message code="admin.detail.label.week.sunday"/></option>		                            
	                            <option value="2"><spring:message code="admin.detail.label.week.monday"/></option>	
	                            <option value="3"><spring:message code="admin.detail.label.week.tuesday"/></option>	
	                            <option value="4"><spring:message code="admin.detail.label.week.wednesday"/></option>	
	                            <option value="5"><spring:message code="admin.detail.label.week.thursday"/></option>	
	                            <option value="6"><spring:message code="admin.detail.label.week.friday"/></option>	
	                            <option value="7"><spring:message code="admin.detail.label.week.saturday"/></option>	
                 			</select>
                 				
                 			<label class="backupDayBox" style="display:none"><spring:message code="admin.backup.label.day"/></label>
                        	<select id="backScheduler" style="display:none; width:50px; margin-left:8px; margin-right:15px;" class="miniSelect">
                           		<option value="" selected>-</option>
             				</select>         
                					
	                    	<label class="backupHourBox"><spring:message code="admin.backup.label.time"/></label>	                
	                    	<select id="scheTime" class="miniSelect" style="width:50px; margin-left:8px; margin-right:15px;">
	                            <option value="" selected>-</option>
                  			</select>
                  			
                 			<label class="backupMinBox"><spring:message code="admin.backup.label.min"/></label>	                
	                    	<select id="scheTimeMin" class="miniSelect" style="width:50px; margin-left:8px; margin-right:15px;">
	                            <option value="" selected>-</option>
                  			</select>
                  			
                  			
               			삭제 기준
		                    <select id="delSearchType" style="width:100px; margin-left:8px; margin-right:15px;">
		                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="D">DB</option>
								<option value="F">파일</option>
							</select>
		      	 	</div>
		      	 </div>
				<div class="gridWrap">
					<div id="delMenuUserManageGrid" style="width: 100% !important;"></div>
				</div>
			</div>
        </div>
    </div>

	<div class="message_area">
	</div>
	
	
	
    <div id="delRecfileInfoModify" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc.delRecfile"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
            	<div class="ui_pannel_row">
            		<div class="ui_padding">
		      	 		<label class="ui_label_essential">삭제 스케줄 이름</label>
            				<input id="del_schedule_name" type="text" />
                    	<label class="ui_label_essential"><spring:message code="views.search.grid.head.R_BG_CODE"/></label>
            				<input id="bg_name" type="text" readonly="readonly"/>
                    	<label class="ui_label_essential"><spring:message code="views.search.grid.head.R_MG_CODE"/></label>
            				<input id="mg_name" type="text" readonly="readonly"/>
                   		<label class="ui_label_essential"><spring:message code="views.search.grid.head.R_SG_CODE"/></label>
                       		<input id="sg_name" type="text" readonly="readonly"/>
                        	
                        	
                    	<label class="ui_label_essential"><spring:message code="admin.select.title.delType"/></label>
                        <select id="del_type" style="width:200px; margin-left:8px; margin-right:15px;">
							<option value="H"><spring:message code="admin.select.value.delType.H"/></option>
							<option value="D"><spring:message code="admin.select.value.delType.D"/></option>
							<option value="F"><spring:message code="admin.select.value.delType.F"/></option>
							<option value="A"><spring:message code="admin.select.value.delType.A"/></option>
						</select> 

						<label class="ui_label_essential"><spring:message code="admin.select.title.delFileType"/></label>
                        <select id="del_file_type" style="width:200px; margin-left:8px; margin-right:15px;">
							<option value="M"><spring:message code="admin.select.value.delFileType.M"/></option>
							<option value="R"><spring:message code="admin.select.value.delFileType.R"/></option>
							<option value="A"><spring:message code="admin.select.value.delFileType.A"/></option>
						</select> 
						
						<label class="ui_label_essential"><spring:message code="admin.select.title.delFilePathType"/></label>
							<select id="del_file_path_type" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="R"><spring:message code="admin.select.value.delFilePathType.R"/></option>
								<option value="S"><spring:message code="admin.select.value.delFilePathType.S"/></option>
								<option value="A"><spring:message code="admin.select.value.delFilePathType.A"/></option>
							</select>
							
						<label class="ui_label_essential"><spring:message code="admin.select.title.logType"/></label>
		                    <select id="log_type" style="width:200px; margin-left:8px; margin-right:15px;">
								<option value="A"><spring:message code="admin.select.value.logType.A"/></option>
								<option value="M"><spring:message code="admin.select.value.logType.M"/></option>
							</select>
							
						<label class="ui_label_essential"><spring:message code="admin.backup.label.backupSchedule"/></label>
		        			<select id="Schecheck_Type" style="width:200px; margin-left:8px; margin-right:15px;" class="miniSelect">
              					<option value="D" selected><spring:message code="admin.detail.option.daily"/></option>		                            
	                            <option value="W"><spring:message code="admin.detail.option.weekly"/></option>
	                            <option value="M"><spring:message code="admin.detail.option.monthly"/></option>
	                            <option value="N"><spring:message code="admin.detail.option.noSche"/></option>
                  			</select>
	                  			
	                  			
                     	<label class="backup_WeekBox ui_label_essential" style="display:none;"><spring:message code="admin.backup.label.week"/></label>
                        	<select id="back_Week" style="width:150px; margin-left:8px; margin-right:15px; display:none;" class="miniSelect">
                        		<option value="*" disabled selected>-</option>
              					<option value="1"><spring:message code="admin.detail.label.week.sunday"/></option>		                            
	                            <option value="2"><spring:message code="admin.detail.label.week.monday"/></option>	
	                            <option value="3"><spring:message code="admin.detail.label.week.tuesday"/></option>	 
	                            <option value="4"><spring:message code="admin.detail.label.week.wednesday"/></option>	
	                            <option value="5"><spring:message code="admin.detail.label.week.thursday"/></option>	
	                            <option value="6"><spring:message code="admin.detail.label.week.friday"/></option>	
	                            <option value="7"><spring:message code="admin.detail.label.week.saturday"/></option>	
               				</select>
                 				
               			<label class="backup_DayBox ui_label_essential" style="display:none"><spring:message code="admin.backup.label.day"/></label>
                      		<select id="back_Scheduler" style="display:none; width:150px; margin-left:8px; margin-right:15px;" class="miniSelect">
	                            <option value="" selected>-</option>
                			</select> 		                     
                  		                
                  		<label class="backup_HourBox ui_label_essential"><spring:message code="admin.backup.label.time"/></label>	                
                  			<select id="sche_Time" class="miniSelect" style="width:150px; margin-left:8px; margin-right:15px;">
                          		<option value="" selected>-</option>
                			</select>
                			
                		<label class="backup_MinBox ui_label_essential"><spring:message code="admin.backup.label.min"/></label>	                
	                    	<select id="sche_Time_Min" class="miniSelect" style="width:150px; margin-left:8px; margin-right:15px;">
	                            <option value="" selected>-</option>
                  			</select>
                  			
               			<label class="ui_label_essential">삭제 기준</label>
		                    <select id="del_search_type" style="width:200px; margin-left:8px; margin-right:15px;">
								<option value="D">DB</option>
								<option value="F">파일</option>
							</select>
                			
               			<label class="ui_label_essential">스토리지 전송 체크</label>
		                    <select id="storage_send_chk" style="width:200px; margin-left:8px; margin-right:15px;">
								<option value="Y" ><spring:message code="admin.detail.option.use"/></option>
								<option value="N" selected="selected"><spring:message code="admin.detail.option.notUse"/></option>
							</select>
                        
						<label class="ui_label_essential"><spring:message code="admin.select.title.delPeriod"/></label>
	                        <input id="del_year"  type="text" maxlength="1" style="width:25px; text-align:right; margin-left:8px; padding:7px !important; float:left;" title="" placeholder="0"/> 
	                        <span style="width:10px; margin-right:8px; padding:7px !important; float:left;"><spring:message code="admin.select.title.delYear"/></span>
							
							<input id="del_month" type="text" maxlength="2" style="width:25px; text-align:right; margin-left:8px; padding:7px !important; float:left;" title="" placeholder="0" />
							<span style="margin-right:8px; padding:7px !important; float:left;"><spring:message code="admin.select.title.delMonth"/></span>
							
							<input id="del_day" type="text" maxlength="3" style="width:25px; text-align:right; padding:7px !important; float:left;" title="" placeholder="0" />
							<span style="margin-right:8px; padding:7px !important; float:left;"><spring:message code="admin.select.title.delDay"/></span>
							
						<label class="ui_label_essential"><spring:message code="admin.select.title.delPeriodOffset"/></label>
	                        <input id="del_year_offset"  type="text" maxlength="1" style="width:25px; text-align:right; margin-left:8px; padding:7px !important; float:left;" title="" placeholder="0"/> 
	                        <span style="width:10px; margin-right:8px; padding:7px !important; float:left;"><spring:message code="admin.select.title.delYear"/></span>
							
							<input id="del_month_offset" type="text" maxlength="2" style="width:25px; text-align:right; margin-left:8px; padding:7px !important; float:left;" title="" placeholder="0" />
							<span style="margin-right:8px; padding:7px !important; float:left;"><spring:message code="admin.select.title.delMonth"/></span>
                       	
                       		<input id="del_day_offset" type="text" maxlength="3" style="width:25px; text-align:right; padding:7px !important; float:left;" title="" placeholder="0" />
							<span style="margin-right:8px; padding:7px !important; float:left;"><spring:message code="admin.select.title.delDay"/></span>
                       	<label class="ui_label_essential">삭제 경로</label>
                  	     	<input id="del_path" type="text" />
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="delRecfileModifyBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.modify"/></button>
                    </div>
                </div>
            </div>
        </div>
        <input id="delRecfileInfoSeq" type="hidden"/>
    </div>
</body>