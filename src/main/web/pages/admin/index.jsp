<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="include/head.jsp"%>
<frameset rows="118px,*,35px" frameborder="no">
    <frame src="${pageContext.request.contextPath}/pages/admin/top.jsp" noresize="noresize" />
    <frameset cols="200px,*">
        <frame src="${pageContext.request.contextPath}/pages/admin/left.jsp" noresize="noresize" />
        <frame src="${pageContext.request.contextPath}/pages/admin/right.jsp" noresize="noresize" name="right" />
    </frameset>
    <frame src="${pageContext.request.contextPath}/pages/admin/foot.jsp" noresize="noresize" />
</frameset>
<noframes><body>浏览器不支持
</body></noframes>
</html>