<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include/head.jsp"%>
<body>
<!--全部主体内容-->
<div class="list-content">
    <!--块元素-->
    <div class="block">
        <!--修饰块元素名称-->
        <div class="banner">
            <p class="tab fixed">添加小分类</p>
        </div>

        <!--正文内容-->
        <div class="main">
            <form id="from">
                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">小分类名称</p></div>
                    <div class="right">
                        <input id="smallName" name="smallName" type="text" class="text" data-type="必填" error-msg="必填项不能为空" error-pos="42" placeholder="请输入大分类名称" />
                    </div>
                </div>

                <!--下拉选择框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">大分类</p></div>
                    <div class="right">
                        <select id="select" name="bigName">

                        </select>
                    </div>
                </div>

                <!--文本域-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">小分类备注</p></div>
                    <div class="right">
                        <textarea class="desc" id="smallText" name="smallText" placeholder="请填写备注"></textarea>
                        <!--提示说明-->
                        <p class="hint">请填写备注。备注中不得包含令人反感的信息，且长度应在10到50个字符之间。</p>
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

    // 显示大分类下拉列表
    $(function () {
        $.ajax({
            url: "/admin_admin_bigClassName",
            type: "POST",
            async: false,
            dataType: "json",
            success: function (big) {
                console.log(big);
                $("#select").empty();
                let html = '<option value="">请选择大分类</option>';
                for (let d in big) {
                    html += '<option value="'+big[d].id+'">'+big[d].bigName+'</option>';
                }
                $("#select").append(html);
                javaex.select({
                    id: "select",
                });
            },
            error: function () {
                javaex.optTip({
                    content : "查询下拉列表失败,请刷新",
                    type : "error"
                });
            }
        });
    });

    // 监听小分类是否重复
    $('#smallName').blur(function () {
        let smallName = $('#smallName').val();
        if (smallName !== null && smallName !== "" && smallName !== undefined) {
            selectSmallName(smallName);
        }
    });

    // 监听点击保存按钮事件
    $("#save").click(function() {
        if ($('#select').val() === "" ) {
            javaex.message({
                content : "大分类不能为空",
                type : "error"
            });
        }
        // 表单验证函数
        if (javaexVerify()) {
            // 返回错误信息时，可以添加自定义异常提示。参数为元素id和提示
            // addErrorMsg("username", "用户名不存在");
            // 提交
            if ($('#select').val() === "" ) {
                javaex.message({
                    content : "大分类不能为空",
                    type : "error"
                });
            } else {
                javaex.optTip({
                    content : "数据提交中，请稍候...",
                    type : "submit"
                });
                let serialize = "smallName="+$('#smallName').val()+"&select="
                        + $('#select').val() + "&smallText=" +$('#smallText').val();
                setTimeout(function(){
                    $.ajax({
                        url: "/admin_admin_smallNameAdd",
                        type: "POST",
                        data: serialize,
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

    function selectSmallName(smallName) {
        $.ajax({
            url: "/admin_admin_selectSmallName",
            type: "POST",
            data: "smallName="+smallName,
            async: false,
            dataType: "text",
            success: function (data) {
                if (data === "false") {
                    javaex.message({
                        content : "小分类已经存在",
                        type : "error"
                    });
                }
            }
        });
    }
</script>
</body>
</html>