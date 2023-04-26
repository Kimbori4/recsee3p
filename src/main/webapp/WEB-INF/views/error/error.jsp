<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<style>
    *{padding: 0; margin: 0;}
    ul, li{
        list-style-type: none;
    }
    body,html{
        width: 100%;
        height: 100%;
    }
    .errorWrap{
        position: absolute;
        width: 350px;
        height: 350px;
        top: 50%;
        left: 50%;
        margin: -200px 0 0 -200px;
    }
    .errorWrap .mark{
        position: absolute;
        width: 300px;
        height: 300px;
        background-image: url(${pageContext.request.contextPath}/resources/common/recsee/images/project/error/error-box.png);
        background-position: center;
        background-repeat: no-repeat;
        top: 50%;
        left: 50%;
        margin: -150px 0 0 -150px;
        z-index: 2;
    }
    .errorWrap .logo{
        position: absolute;
        width: 300px;
        height: 300px;
        background-image: url(${pageContext.request.contextPath}/resources/common/recsee/images/project/error/Recsee_BI_gray.png);
        background-position: center;
        background-repeat: no-repeat;
        top: 50%;
        left: 50%;
        margin: -150px 0 0 -150px;
        z-index: 1;
    }
    .errorWrap .alert{
        position: absolute;
        width: 100%;
        height: auto;
        bottom: 10px;
        text-align: center;
        font-size: 30px;
        font-weight: bold;
        color: #555555;
    }
</style>
<body>
    <div style="width:100%;height:100%;background-color:#dddddd;">
        <div class="errorWrap">
            <div class="mark"></div>
            <div class="logo"></div>
            <p class="alert">ERROR!</p>
        </div>
    </div>
</body>
</html>