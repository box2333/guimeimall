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
                <span>性别：</span>
                <select id="select">
                    <option value="0">全部</option>
                    <option value="男">男</option>
                    <option value="女">女</option>
                </select>
                <input id="name" type="text" class="text" value="" placeholder="输入用户名" />
                <input id="cid" type="text" class="text" value="" placeholder="请输入id" />
                <button class="button blue" onclick="select()">搜索</button>
            </div>
        </div>
        <!--表格上方的操作元素，添加、删除等-->
        <div class="operation-wrap">
            <div class="buttons-wrap">
                <button class="button blue radius-3" onclick="add()"><span class="icon-plus"></span> 添加</button>
                ---------------
                <button class="button red radius-3" onclick="deleteUsers(this, 'delete')"><span class="icon-close2"></span> 删除</button>
            </div>
        </div>
        <%-- 表格 --%>
        <table id="table" class="table color2  th-center td-center">
            <thead>
            <tr>
                <th class="checkbox"><input type="checkbox" class="fill listen-1" /> </th>
                <th>Id</th>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>性别</th>
                <th>Email</th>
                <th>手机</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="tbody">
            </tbody>
        </table>
        <%@include file="../include/page.jsp"%>
    </div>
</body>
<script src="${pageContext.request.contextPath}/static/admin/js/userManagePage.js" type="text/javascript"></script>
</html>
