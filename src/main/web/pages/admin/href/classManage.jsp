<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/head.jsp"%>

<body>
    <h4 class="h4">大分类管理</h4>
    <hr class="hr" size="4" color="#FF8239" />
    <%-- 表格 --%>
    <div class="main">
        <!--表格上方的搜索操作-->
        <div class="admin-search">
            <div class="input-group">
                <input type="text" class="text" placeholder="分类名称" />
                <button class="button blue">搜索</button>
            </div>
        </div>

        <!--表格上方的操作元素，添加、删除等-->
        <div class="operation-wrap">
            <div class="buttons-wrap">
                <button class="button blue radius-3"><span class="icon-plus"></span> 添加</button>
                <button class="button red radius-3"><span class="icon-close2"></span> 删除</button>
            </div>
        </div>
        <table id="table" class="table color2">
            <thead>
            <tr>
                <th class="checkbox"><input type="checkbox" class="fill listen-1" /> </th>
                <th>编号</th>
                <th>分类名称</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        <%@include file="../include/page.jsp"%>
    </div>
</body>
<script src="${pageContext.request.contextPath}/static_admin/js/classManagePage.js" type="text/javascript"></script>
</html>
