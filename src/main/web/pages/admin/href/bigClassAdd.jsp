<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include/head.jsp"%>
<body>
<!--全部主体内容-->
<div class="list-content">
    <!--块元素-->
    <div class="block">
        <!--修饰块元素名称-->
        <div class="banner">
            <p class="tab fixed">添加大分类</p>
        </div>

        <!--正文内容-->
        <div class="main">
            <form id="from">
                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">分类名称</p></div>
                    <div class="right">
                        <input id="bigName" name="bigName" type="text" class="text" data-type="必填" error-msg="必填项不能为空" error-pos="42" placeholder="请输入大分类名称" />
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">介绍</p></div>
                    <div class="right">
                        <div class="tagbox"></div>
                        <input type="hidden" id="tag" name="bigText" value="" />
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
    // 标签
    javaex.tag({
        id : "tag",
        tags : tag,
        maxNum: 20
    });

    // 监听登陆用户名是否重复
    $('#bigName').blur(function () {
        let bigName = $('#bigName').val();
        if (bigName !== null && bigName !== "" && bigName !== undefined) {
            selectBigName(bigName);
        }
    });

    // 监听点击保存按钮事件
    $("#save").click(function() {
        if ($('.tagbox').text() === "" ) {
            javaex.message({
                content : "标签不能为空",
                type : "error"
            });
        }
        // 表单验证函数
        if (javaexVerify()) {
            // 返回错误信息时，可以添加自定义异常提示。参数为元素id和提示
            // addErrorMsg("username", "用户名不存在");
            // 提交
            if ($('.tagbox').text() === "" ) {
                javaex.message({
                    content : "标签不能为空",
                    type : "error"
                });
            } else {
                javaex.optTip({
                    content : "数据提交中，请稍候...",
                    type : "submit"
                });
                setTimeout(function(){
                    $.ajax({
                        url: "/admin_admin_bigClassAdd",
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
                            } else if (data === "name") {
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
                },1000);

            }
        }
    });

    // 监听点击返回按钮事件
    $("#return").click(function() {
        parent.javaex.close();
    });

    function selectBigName(bigName) {
        $.ajax({
            url: "/admin_admin_bigNameSelect",
            type: "POST",
            data: "bigName="+bigName,
            async: false,
            dataType: "text",
            success: function (data) {
                if (data === "false") {
                    javaex.message({
                        content : "大分类已经存在",
                        type : "error"
                    });
                }
            }
        });
    }
</script>
</body>
</html>