<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/head.jsp"%>
<body>
    <h4 class="h4">用户管理</h4>
    <hr class="hr" size="4" color="#FF8239" />
    <div class="main">
        <!--表格上方的搜索操作-->
        <div class="admin-search">
            <div class="input-group">
                <input id="name" type="text" class="text" placeholder="输入用户名" />
                <button class="button blue" onclick="select()">搜索</button>
            </div>
        </div>
        <%-- 表格 --%>
        <table id="table" class="table color2">
            <thead>
            <tr>
                <th>Id</th>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>性别</th>
                <th>Email</th>
                <th>手机</th>
            </tr>
            </thead>
            <tbody id="tbody">
            </tbody>
        </table>
        <%@include file="../include/page.jsp"%>
    </div>
</body>
<script src="${pageContext.request.contextPath}/static_admin/js/userManagePage.js" type="text/javascript"></script>
</html>
