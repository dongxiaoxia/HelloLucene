<%--
  Created by IntelliJ IDEA.
  User: dongxiaoxia
  Date: 2016/1/18
  Time: 21:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HelloLucene</title>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <!-- 可选的Bootstrap主题文件（一般不使用） -->
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap-theme.min.css"></script>
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>

    <link href="${pageContext.request.contextPath}/resources/css/index.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="view">
        <div class="title">Hello Lucene</div>
        <div class="sec_title">全文检索系统</div>
        <div class="index_search">
            <input type="text" class="" placeholder="lucene一下">
            <button type="button" class="btn btn-info" name=""><span>搜索</span></button>
        </div>
    </div>
</div>
</body>
</html>
