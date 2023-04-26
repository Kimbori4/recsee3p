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
	<script type="text/javascript" src="${recseeResourcePath }/js/page/admin/backup_recfile_manage.js"></script>
	<script>
	var reader = new FileReader();
	var fakePath;
	var realPath;
		
	
	$(function() {
	    $(window).resize(function() {
	    	// ���� document ����
			var documentHeight = $(window).height();

			// �׸��� ���� ���� ��
			var gridCalcHeight = $("#backMenuUserManageGrid").offset().top;

			// ����¡�� ���� ����¡ ��ŭ ���ֱ�
			var gridResultHeight = documentHeight;

			// �� ��ȯ ���� ���� ������ ���Ƿ� ���
			if(tabMode=="Y"){
				$(".group_list_wrap").css({"height": + (gridResultHeight - 52) + "px"});
				$("#backMenuUserManageGrid").css({"height": + (gridResultHeight - 325) + "px"});
				$("#backMenuTreeViewAgent").css({"height": + (gridResultHeight - 53) + "px"});
			} else{
				$(".group_list_wrap").css({"height": + (gridResultHeight - 141) + "px"});
				$("#backMenuUserManageGrid").css({"height": + (gridResultHeight - 325) + "px"});
				$("#backMenuTreeViewAgent").css({"height": + (gridResultHeight - 142) + "px"});
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
        #backRecfileInfoModify {
        	width:400px;
        }
        
	</style>
</head>
<body onload="backupRecfileManageLoad()">
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
		            <div class="tree_agent_view" id="backMenuTreeViewAgent" style="position:absolute;">
	       				
	       			</div>
				</div>
			</div>
			<div class="ui_acrticle delRecfile_manage_pannel">
			    <div class="ui_pannel_row">
			    
			        <div class="ui_float_left" style="margin-top:3px; padding:2px;">
						<spring:message code="admin.select.title.useDelOption"/>
						<select id="recseeBackUp" style="width:70px; margin-top:3.5px; margin-left:8px; margin-right:15px;">
							<option value="Y" selected="selected"><spring:message code="admin.detail.option.use"/></option>
							<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
						</select>
						<div class="backOptionWrap" style="display:inline">				                                        
							<spring:message code="admin.select.value.selectType"/>
		                        <select id="backType" style="width:100px; margin-left:8px; margin-right:15px;">
									<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
									<option value="B"><spring:message code="admin.select.value.delType.B"/></option>
									<option value="M"><spring:message code="admin.select.value.delType.M"/></option>
								</select> 

							<spring:message code="admin.select.value.indexType"/>
			                    <select id="logType" style="width:100px; margin-left:8px; margin-right:15px;">
			                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
									<option value="Y"><spring:message code="admin.select.value.logType.Y"/></option>
									<option value="N"><spring:message code="admin.select.value.logType.N"/></option>
								</select>	
								
							<spring:message code="admin.select.value.decType"/>
			                    <select id="decType" style="width:100px; margin-left:8px; margin-right:15px;">
			                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
									<option value="Y"><spring:message code="admin.detail.option.use"/></option>
									<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
								</select>
							</div>
					</div>		
					
					<div class="delOptionWrap" style="display:inline">
			            <div class="ui_float_right">
							<button id="backMenuRangeSaveBtn" class="ui_main_btn_flat icon_btn_save_white"><spring:message code="admin.button.save"/></button>
							<button id="backMenuCheckedRangeDeleteBtn" class="ui_btn_white icon_btn_trash_gray"></button>
			            </div>
		            </div>
		        </div>
		        <div id="rowDisabled" style="width: 100%;height: 134px;background-color: lightgray;position: fixed;z-index: 0;opacity: 0.5;display: none;left:0;top: 136px;"></div>
		        <div class="ui_pannel_row">
		        	<div class="ui_float_left" style="margin-top:3px; padding:2px;">
		        		<label><spring:message code="admin.select.value.urlUpdateType"/></label>
		                    <select id="urlUpdateType" style="width:100px; margin-left:8px; margin-right:15px;">
		                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>	  
								
						<label><spring:message code="admin.select.value.overWriteType"/></label>
		                    <select id="overWriteType" style="width:100px; margin-left:8px; margin-right:15px;">
		                   	    <option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>	 
						
						<div style="display:none;">
						<label><spring:message code="admin.select.value.conformityType"/></label>
		                    <select id="conformityType" style="width:100px; margin-left:8px; margin-right:15px;">
		                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>	
						</div>
									
						<label>듀얼 서버 백업 여부</label>
		                    <select id="dualBackupType" style="width:100px; margin-left:8px; margin-right:15px;">
		                    	<option value="" selected="selected"><spring:message code="admin.select.value.selectType"/></option>
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>	
					</div>
		        </div>
		        
		        
		         <div class="ui_pannel_row">
		      	  		<div class="ui_float_left" style="margin-top:3px; padding:2px;">
					
							<label><spring:message code="admin.backup.label.backupSchedule"/></label>
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
	                  				                  			
	                  	</div>
					</div>
					
					 <div class="ui_pannel_row">
		      	  		<div class="ui_float_left" style="margin-top:3px; padding:2px;">
					
							<label><spring:message code="admin.backup.label.backupData"/></label>
			        			<select id=PreviousDataThird style="width:80px; margin-left:8px; margin-right:15px;" class="miniSelect">
	              					<option value="mon" ><spring:message code="admin.backup.label.mon"/></option>		                            
		                            <option value="day" selected><spring:message code="admin.backup.label.day"/></option>
		                            <%-- <option value="hour"><spring:message code="admin.backup.label.hour"/></option>--%>
		                            <option value="fromTo"><spring:message code="admin.backup.label.fromTo"/></option>
	                  			</select>
	                  			
	                        	<select id="PreviousDataMonth" style="width:50px; margin-left:8px; margin-right:15px;display:none;" class="miniSelect">
		                            <option value="" selected>-</option>
	                  			</select>                  			
	                        	<select id="PreviousDataDay" style="width:50px; margin-left:8px; margin-right:15px;" class="miniSelect">
		                            <option value="" selected>-</option>
	                  			</select>          				
								<select id="PreviousDataTime" class="miniSelect" style="display:none;width:50px; margin-left:8px; margin-right:15px;">
		                            <option value="" selected>-</option>
		                  		</select>     
		                  		
		                  		<input type="text" id="sDatebackup" style="display:none" class="ui_input_cal icon_input_cal" placeholder="<spring:message code="search.option.startDate"/>" />
								<input type="text" id="eDatebackup" style="display:none" class="ui_input_cal icon_input_cal" placeholder="<spring:message code="search.option.endDate"/>" />
	                  	</div>
					</div>
		        
		        <div class="ui_pannel_row">
		      	  	<div class="ui_float_left" style="margin-top:3px; padding:2px;">
		      	  		<spring:message code="admin.title.backPath"/> 
		      	  		<input id="backPath" type="text" style="width:700px; text-align:left; margin-left:8px; padding-right:5px;"/>
		      	  		&nbsp;&nbsp;<input class="urlCheck ui_main_btn_flat" style="width:100px;" type="button" onclick="UrlCheck()" value="<spring:message code="views.search.button.pathCheck"/>" />
		      	 	</div>
		      	 </div>
		        
				<div class="gridWrap">
					<div id="backMenuUserManageGrid" style="width: 100% !important;"></div>
				</div>
			</div>
        </div>
    </div>

	<div class="message_area">
	</div>
	
	
	
    <div id="backRecfileInfoModify" class="popup_obj">
        <div class="ui_popup_padding">
            <div class="popup_header">
                <div class="ui_pannel_popup_header">
                    <div class="ui_float_left">
                        <p class="ui_pannel_tit"><spring:message code="admin.label.manageEtc.backRecfile"/></p>
                    </div>
                    <div class="ui_float_right">
                        <button class="popup_close ui_btn_white icon_btn_exit_gray"></button>
                    </div>
                </div>
            </div>
            <div class="ui_article ui_row_input_wrap">
            	<div class="ui_pannel_row">
            		<div class="ui_padding">
                    	<label class="ui_label_essential bg_title"><spring:message code="views.search.grid.head.R_BG_CODE"/></label>
            				<input id="bg_name" type="text" readonly="readonly"/>
                    	<label class="ui_label_essential mg_title"><spring:message code="views.search.grid.head.R_MG_CODE"/></label>
            				<input id="mg_name" type="text" readonly="readonly"/>
                   		<label class="ui_label_essential sg_title"><spring:message code="views.search.grid.head.R_SG_CODE"/></label>
                       		<input id="sg_name" type="text" readonly="readonly"/>
                        	
                        	
                    	<label class="ui_label_essential"><spring:message code="admin.select.value.selectType"/></label>
	                        <select id="back_type" style="width:200px; margin-left:8px; margin-right:15px;">
								<option value="B"><spring:message code="admin.select.value.delType.B"/></option>		
								<option value="M"><spring:message code="admin.select.value.delType.M"/></option>			
							</select> 

						<label class="ui_label_essential"><spring:message code="admin.select.value.indexType"/></label>
		                    <select id="log_type" style="width:200px; margin-left:8px; margin-right:15px;">
								<option value="Y"><spring:message code="admin.select.value.logType.Y"/></option>
								<option value="N"><spring:message code="admin.select.value.logType.N"/></option>
							</select>
						
						<label class="ui_label_essential"><spring:message code="admin.select.value.decType"/></label>
		                    <select id="dec_Type" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>
							
						<label class="ui_label_essential"><spring:message code="admin.select.value.urlUpdateType"/></label>
		                    <select id="urlUpdate_Type" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>
							
						<label class="ui_label_essential"><spring:message code="admin.select.value.overWriteType"/></label>
		                    <select id="overWrite_Type" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>
						<div style="display:none;">
						<label class="ui_label_essential"><spring:message code="admin.select.value.conformityType"/></label>
		                    <select id="conformity_Type" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
							</select>
						</div> 
						
						<label class="ui_label_essential">듀얼 서버 백업 여부</label>
		                    <select id="dualBackup_Type" style="width:100px; margin-left:8px; margin-right:15px;">
								<option value="Y"><spring:message code="admin.detail.option.use"/></option>
								<option value="N"><spring:message code="admin.detail.option.notUse"/></option>
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
                			
                		<label class="ui_label_essential"><spring:message code="admin.backup.label.backupData"/></label>
		        			<select id=PreviousData_Third style="width:200px; margin-left:8px; margin-right:15px;" class="miniSelect">
              					<option value="mon" ><spring:message code="admin.backup.label.mon"/></option>		                            
	                            <option value="day" selected><spring:message code="admin.backup.label.day"/></option>
	                            <%-- <option value="hour"><spring:message code="admin.backup.label.hour"/></option> --%>
	                            <option value="fromTo"><spring:message code="admin.backup.label.fromTo"/></option>
                  			</select>
                  		<label class="ui_label_essential PreviousData_MonthBox" style="display:none"><spring:message code="admin.backup.label.mon"/></label>	
                        	<select id="PreviousData_Month" style="width:150px; margin-left:8px; margin-right:15px;display:none;" class="miniSelect">
	                            <option value="" selected>-</option>
                  			</select>                  			
                        <label class="ui_label_essential PreviousData_DayBox" style="display:none"><spring:message code="admin.backup.label.day"/></label>		
                        	<select id="PreviousData_Day" style="display:none; width:150px; margin-left:8px; margin-right:15px;" class="miniSelect">
	                            <option value="" selected>-</option>
                  			</select>          				
						<label class="ui_label_essential PreviousData_TimeBox" style="display:none"><spring:message code="admin.backup.label.hour"/></label>		
							<select id="PreviousData_Time" class="miniSelect" style="display:none;width:150px; margin-left:8px; margin-right:15px;">
	                            <option value="" selected>-</option>
	                  		</select> 
	                  		
	                  	<label class="ui_label_essential from_Date" style="display:none">FROM</label>	
                  			<input type="text" id="sDatebackup_Date" style="display:none;width:150px; margin-left:8px; margin-right:15px;" class="ui_input_cal icon_input_cal" placeholder="<spring:message code="search.option.startDate"/>" />
						
						<label class="ui_label_essential to_Date" style="display:none">TO</label>	
							<input type="text" id="eDatebackup_Date" style="display:none;width:150px; margin-left:8px; margin-right:15px;" class="ui_input_cal icon_input_cal" placeholder="<spring:message code="search.option.endDate"/>" />  
	                        
                       	<label class="ui_label_essential "><spring:message code="admin.title.backPath"/></label>
		      	  			<input id="back_path" type="text" style="width:230px; text-align:left; margin-left:8px; padding-right:5px;"/>
                       	
                    </div>
                </div>
            </div>
            <div class="ui_article">
                <div class="ui_pannel_row">
                    <div class="ui_float_right">
                        <button id="backRecfileModifyBtn" class="ui_main_btn_flat icon_btn_cube_white"><spring:message code="message.btn.modify"/></button>
                    </div>
                </div>
            </div>
        </div>
        <input id="backRecfileInfoSeq" type="hidden"/>
        <form id="download" name="download" method="post" action="/audioDownload" target="downloadFrame" style="display:none;">
			<input type="hidden" name="fileName" id="fileName" value=""/>
			<input type="hidden" name="format" id="format" value=""/>
		</form>
    </div>
</body>