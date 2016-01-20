<%--
  Created by IntelliJ IDEA.
  User: dongxiaoxia
  Date: 2016/1/19
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>
<body>
<div class="container-fluid">
    <div class="row top">
        <div class="project-icon"></div>
        <div class="search-form col-md-8">
            <form action="/lucene/search" method="get">
                <input class="search" type="text" name="search">
                <button type="submit" class="btn btn-info">搜索</button>
            </form>
        </div>
        <div class="top-right col-md-4">
            <a href="/">返回首页</a>
            <a href="https://github.com/dongxiaoxia">view on GitHub</a>
        </div>
    </div>
    <div class="dividing-line col-md-12">HelloLucene为您找到相关结果${total}个，用时${time}毫秒</div>
    <div class="main container-fluid">
        <div class="content col-md-8">
            <div><a class="item-title" href="">名称名称名称名称名称名称名称名称名称名称名称名称名称名称名.doc</a></div>
            <p class="item-info">导航条是在您的应用或网站中作为导航页头的响应式基础组件。它们在移动设备上可以折叠（并且可开可关），且在视口（viewport）宽度增加时逐渐变为水平展开模式。导航条是在您的应用或网站中作为导航页头的响应式基础组件。它们在移动设备上可以折叠（并且可开可关），且在视口（viewport）宽度增加时逐渐变为水平展开模式。</p>
            <a class="item-url" href="">wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww</a>
            <a class="download" href="">全文下载</a>
        </div>
    </div>
</div>
</div>


<script type="text/javascript" src="/resources/js/main.js"></script>
</body>
</html>
