<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="top.jsp"%>
<div style="margin:5px auto;width: 80%">
    <div>你现在的位置是：<a href="#">贵美商城</a> > 后台管理系统</div>
    <div style="margin:20px auto;width: 78%">
        <span>图标 </span>管理首页
        <hr>
        <div style="margin: 0 auto;width: 50%;text-align: center"><h1>欢迎登陆贵美商城系统</h1></div>
        <div style="margin: 0 auto;width: 50%;text-align: center">
            <form action="/admin_admin_login" method="post">
                <table style="border-spacing:0 30px;margin: 0 auto;text-align: center; font-size: 2em">
                    <tr>
                        <td>用户名：</td>
                        <td><input id="name" type="text" name="name"></td>
                    </tr>
                    <tr>
                        <td>密码：</td>
                        <td><input id="password" type="password" name="password"></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center">
                            <span style="color: red">${msg}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center">
                            <input type="submit" value="立即登陆">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>
