<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="include/head.jsp"%>
<body>
<dl id="leftbox">
    <dt>用户管理</dt>
    <dd><a href="${pageContext.request.contextPath}/pages/admin/href/userManage.jsp" target="right">用户管理</a><a class="a">新增</a></dd>
    <dt>商品分类管理</dt>
    <dd><a href="${pageContext.request.contextPath}/pages/admin/href/classManage.jsp" target="right">大分类管理</a><a class="a">新增</a></dd>
    <dd><a href="${pageContext.request.contextPath}/pages/admin/href/smallWareManage.jsp" target="right">小分类管理</a><a class="a">新增</a></dd>
    <%--<dt>订单管理</dt>--%>
    <%--<dd><a href="${pageContext.request.contextPath}/pages/admin/href/ordersShow.jsp" target="right">订单管理</a></dd>--%>
    <dt>商品详细管理</dt>
    <dd><a href="${pageContext.request.contextPath}/pages/admin/href/goodDetail.jsp" target="right">商品详细管理</a></dd>
    <dt>公告管理</dt>
    <dd><a href="${pageContext.request.contextPath}/pages/admin/href/announcementManage.jsp" target="right">公告管理</a><a class="a">新增</a></dd>
</dl>
                                                                                            
</body>
</html>
