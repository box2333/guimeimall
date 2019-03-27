<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../include/head.jsp"%>
<body>
<!--全部主体内容-->
<div class="list-content">
    <!--块元素-->
    <div class="block">
        <!--修饰块元素名称-->
        <div class="banner">
            <p class="tab fixed">添加商品</p>
        </div>

        <!--正文内容-->
        <div class="main" style="height: 800px">
            <form id="from" method="post" action="/admin_admin_goodsAdd" >
                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">商品名称</p></div>
                    <div class="right">
                        <input id="goodsName" name="goodsName" type="text" class="text" data-type="必填" error-msg="必填项不能为空" error-pos="42" placeholder="请输入大分类名称" />
                    </div>
                </div>

                <!--下拉选择框-->
                <div class="unit clear">
                    <div class="left"><p class="subtitle">选择小分类</p></div>
                    <div class="right">
                        <select id="select" name="smallName">

                        </select>
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">商品价格</p></div>
                    <div class="right">
                        <input id="goodsMoney" name="goodsMoney" type="text" class="text" data-type="正小数|正整数" error-msg="必填正小数 或 正整数" error-pos="42" placeholder="请输入大分类名称" />
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">剩余数量</p></div>
                    <div class="right">
                        <input id="goodsNumber" name="goodsNumber" type="text" class="text" data-type="正整数" error-msg="必填正整数" error-pos="42" placeholder="请输入大分类名称" />
                    </div>
                </div>

                <!--文本框-->
                <div class="unit clear">
                    <div class="left"><span class="required">*</span><p class="subtitle">运费</p></div>
                    <div class="right">
                        <input id="goodsCarriage" name="goodsCarriage" type="text" class="text" data-type="正整数|正小数" error-msg="必填正小数 或 正整数" error-pos="42" placeholder="请输入大分类名称" />
                    </div>
                </div>

                <!--上传图片-->
                <div id="container" class="file-container">
                    <div class="cover">
                        <!--如果不需要回显图片，src留空即可-->
                        <img src=""/>
                        <input type="file" class="file" id="upload" accept="image/gif, image/jpeg, image/jpg, image/png" />
                        <input type="hidden" id="imgBase64" name="imgBase64" value=""/>
                    </div>
                </div>

                <!--提交按钮-->
                <div class="unit clear" style="width: 800px;">
                    <div style="text-align: center;">
                        <!--表单提交时，必须是input元素，并指定type类型为button，否则ajax提交时，会返回error回调函数-->
                        <input type="button" id="return" class="button no" value="返回" />
                        <input type="submit" id="save" class="button yes" value="保存" />
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // 显示小分类下拉列表
    $(function () {
        $.ajax({
            url: "/admin_admin_smallClassName",
            type: "POST",
            async: false,
            dataType: "json",
            success: function (small) {
                console.log(small);
                $("#select").empty();
                let html = '<option value="">请选择小分类</option>';
                for (let d in small) {
                    html += '<option value="'+small[d].id+'">'+small[d].smallName+'</option>';
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

    // 监听商品名称是否重复
    $('#goodsName').blur(function () {
        let goodsName = $('#goodsName').val();
        if (goodsName !== null && goodsName !== "" && goodsName !== undefined) {
            selectGoodsName(goodsName);
        }
    });

    // 监听点击保存按钮事件
    $('#from').submit(function () {
        let img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAAAIBJREFUSA3tl7ENwCAMBN9RpkvFQsyUNGE9IpIs8P8FBUZy58PmkLAAxFXqdY8QcewqCPRDZ4HNgR02Czv2KDZVU7qc5FTt2KPY9VTHN2G8B59y/CZHm6I6Ap1v9idKPfsIdYMpJx7NZmH1ymguVdPKVCBVq+Zobj3Vxt8pmjNlHkcEEcpxsEklAAAAAElFTkSuQmCC";
        // 表单验证函数
        if (!javaexVerify()) {
            return false;
        } else if (selectGoodsName($('#goodsName').val())) {
            return false;
        } else if ($('#select').val() === "") {
            javaex.message({
                content: "请选择小分类",
                type: "error"
            });
            return false;
        } else if ($("#container img").attr("src") === img) {
            javaex.message({
                content: "请上传图片",
                type: "error"
            });
            return false;
        }
        // 上传
        $('#imgBase64').val($("#container img").attr("src"));
        console.log($('#imgBase64').val());
        parent.javaex.close();
    });


    // 监听点击返回按钮事件
    $("#return").click(function() {
        parent.javaex.close();
    });

    function selectGoodsName(goodsName) {
        let flag = false;
        $.ajax({
            url: "/admin_admin_goodsNameSelect",
            type: "POST",
            data: "goodsName="+goodsName,
            async: false,
            dataType: "text",
            success: function (data) {
                if (data === "false") {
                    javaex.message({
                        content : "大分类已经存在",
                        type : "error"
                    });
                    flag = true;
                }
            }
        });
        return flag;
    }

    // 上传图片
    javaex.upload({
        type : "image",
        id : "upload",	// <input type="file" />的id
        containerId : "container",	// 容器id
        dataType : "base64",	// 返回的数据类型：base64 或 url
        uploadText : "上传商品图片",
        callback : function (rtn) {
//			console.log(rtn);
            $("#container img").attr("src", rtn);
//			if (rtn.code=="000000") {
//				$("#container img").attr("src", rtn.data.imgUrl);
//			} else {
//				javaex.optTip({
//					content : rtn.message,
//					type : "error"
//				});
//			}
        }
    });
</script>
</body>
</html>