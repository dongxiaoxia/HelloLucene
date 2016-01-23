<%--
  Created by IntelliJ IDEA.
  User: dongxiaoxia
  Date: 2016/1/23
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
  <title>HelloLucene</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <!-- 新 Bootstrap 核心 CSS 文件 -->
  <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
  <!-- 可选的Bootstrap主题文件（一般不使用） -->
  <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap-theme.min.css"></script>
  <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
  <script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
  <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
  <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <link href="${pageContext.request.contextPath}/resources/css/detail.css" rel="stylesheet">
</head>
<body>
<div class="page-header">
  <h1>${document.name} <small>Subtext for header</small></h1>
</div>
<div>
    <div class="content">${document.content}</div>
</div>
</body>
</html>
