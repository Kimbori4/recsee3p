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
				$("#delMenuUserManageGrid").css({"height": + (gridResultHeight - 142) + "px"});
				$("#delMenuTreeViewAgent").css({"height": + (gridResultHeight - 142) + "px"});
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
        	width:400px;
        }
	</style>
</head>
<body>
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
			
			<div class="ui_acrticle userInterface_manage_pannel">
				<div class="gridWrap">
				<h1>DBInfo Insert Page</h1>
			 	<form action="DBInsertProc.jsp" method=post> 
				 	<table>
				 			<tr>
								<td width = "100px" border= "2px">INSERT</td>
								<td></td>
							</tr>
							<tr>
								<td width = "100px">dbServer</td>
								<td><input type="text" name="dbServer"/></td>
							</tr>
							<tr>
								<td width = "100px">url</td>
								<td><input type="text" name="url"/></td>
							</tr>
							<tr>
								<td width = "100px">id</td>
								<td><input type="text" name="id"/></td>
							</tr>
							<tr>
								<td width = "100px">pw</td>
								<td><input type="text" name="pw"/></td>
							</tr>
							<tr>
								<td width = "100px">timeout</td>
								<td><input type="text" name="timeout"/></td>
							</tr>
							<tr>
							<td></td>
								<td>
									<input type="submit" value="Insert"/>
								</td>
							</tr>
						</table>
					</form><br/>
					<form action="DBDeleteProc.jsp" method=post> 
				 	<table>
							<tr>
								<td width = "100px" border= "2px">DELETE</td>
								<td></td>
							</tr>
							<tr>
								<td width = "100px">Index</td>
								<td><input type="text" name="Index"/></td>
							</tr>
							<tr>
								<td></td>
								<td>
									<input type="submit" value="Delete"/>
								</td>
							</tr>
						</table>
					</form><br/>
					<form action="DBClearProc.jsp" method=post> 
				 	<table>
							<tr>
								<td width = "100px" border= "2px">Clear</td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td>
									<input type="submit" value="Clear"/>
								</td>
							</tr>
						</table>
					</form><br/>
			        <textarea id="incomingMsgOutput" rows="10"  width="100%" ></textarea>
				</div>
			</div>
        </div>
    </div>

</body>