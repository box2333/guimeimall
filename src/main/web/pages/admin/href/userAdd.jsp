<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include/head.jsp"%>
<body>
<!--全部主体内容-->
<div class="list-content">
    <!--块元素-->
    <div class="block">
        <!--修饰块元素名称-->
        <div class="banner">
            <p class="tab fixed">添加用户</p>
        </div>

        <!--正文内容-->
        <div class="main">
            <form id="from">
                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">登录用户名</p></div>
                    <div class="right">
                        <input id="cusLoginName" name="cusLoginName" type="text" class="text" data-type="登录名" error-msg="只能输入5-10个以字母开头，可带数字的字符串" error-pos="42" placeholder="请输入登录用户名" />
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">真实姓名</p></div>
                    <div class="right">
                        <input name="cusName" type="text" class="text" data-type="中文|英文字母" error-msg="中文或者英文字母" error-pos="42" placeholder="请输入真实姓名" />
                    </div>
                </div>

                <!--密码框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">密码</p></div>
                    <div class="right">
                        <input id="pwd" name="cusPassword" type="password" class="text" data-type="密码" error-msg="6到16位字母或数字或它们的组合" error-pos="42" placeholder="请输入密码" />
                    </div>
                </div>
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">确认密码</p></div>
                    <div class="right">
                        <input id="newPwd" type="password" class="text" data-type="密码" error-msg="6到16位字母或数字或它们的组合" error-pos="42" placeholder="请确认密码" />
                    </div>
                </div>

                <!--单选框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">性别</p></div>
                    <div class="right">
                        <ul class="equal-8 clear">
                            <li><input type="radio" class="fill" name="cusSex" checked value="男"/>男</li>
                            <li><input type="radio" class="fill" name="cusSex" value="女"/>女</li>
                        </ul>
                    </div>
                </div>

                <!--日期选择框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">出生日期</p></div>
                    <div class="right">
                        <input name="cusBirthday" type="text" id="date" class="date" style="width: 200px;" value="" readonly/>
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">身份证</p></div>
                    <div class="right">
                        <input name="cusCode" type="text" class="text" data-type="空|身份证号" error-msg="只能输入身份证号" error-pos="42" placeholder="请输入身份证号" />
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">电子邮件</p></div>
                    <div class="right">
                        <input name="cusEmail" type="text" class="text" data-type="空|邮箱" error-msg="只能输入邮箱" error-pos="42" placeholder="请输入邮箱" />
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">手机号</p></div>
                    <div class="right">
                        <input name="cusPhoto" type="text" class="text" data-type="空|手机号" error-msg="只能输入手机号" error-pos="42" placeholder="请输入手机号" />
                    </div>
                </div>

                <!--提交按钮-->
                <div class="unit clear" style="width: 800px;">
                    <div style="text-align: center;">
                        <!--表单提交时，必须是input元素，并指定type类型为button，否则ajax提交时，会返回error回调函数-->
                        <input type="button" id="return" class="button no" value="返回" />
                        <input type="button" id="save" class="button yes" value="保存" />
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    javaex.date({
        id : "date",	// 承载日期组件的id
        isTime : true,
        date : "2018-04-15",	// 选择的日期
        // 重新选择日期之后返回一个时间对象
        callback : function(rtn) {
            alert(rtn.date);
        }
    });

    // 监听登陆用户名是否重复
    $('#cusLoginName').blur(function () {
        let loginName = $('#cusLoginName').val();
        if (loginName !== null && loginName !== "" && loginName !== undefined) {
            selectLoginName(loginName);
        }
    });

    // 监听点击保存按钮事件
    $("#save").click(function() {
        // 表单验证函数
        if (javaexVerify()) {
            // 返回错误信息时，可以添加自定义异常提示。参数为元素id和提示
            // addErrorMsg("username", "用户名不存在");
            // 提交
            if ($("#pwd").val() !== $('#newPwd').val()) {
                addErrorMsg("pwd", "两次密码不一致")
            } else {
                javaex.optTip({
                    content : "数据提交中，请稍候...",
                    type : "submit"
                });
                $.ajax({
                    url: "/admin_admin_addUser",
                    type: "POST",
                    data: $('#from input').serialize(),
                    async: false,
                    dataType: "text",
                    success:function(data) {
                        console.log(data);
                        if (data === "true") {
                            javaex.optTip({
                                content : "操作成功",
                                type : "success"
                            });
                        } else if (data === "user") {
                            javaex.message({
                                content : "用户名已经被占用",
                                type : "error"
                            });
                        } else {
                            javaex.optTip({
                                content : "操作失败",
                                type : "error"
                            });
                        }
                    },
                    error: function () {
                        javaex.optTip({
                            content : "操作失败",
                            type : "error"
                        });
                    }
                });
            }
        }
    });

    // 监听点击返回按钮事件
    $("#return").click(function() {
        parent.javaex.close();
    });
    function selectLoginName(loginName) {
        $.ajax({
            url: "/admin_admin_selectLoginName",
            type: "POST",
            data: "loginName="+loginName,
            async: false,
            dataType: "text",
            success: function (data) {
                if (data === "false") {
                    javaex.message({
                        content : "用户名已经被占用",
                        type : "error"
                    });
                }
            }
        });
    }
</script>
</body>
</html>