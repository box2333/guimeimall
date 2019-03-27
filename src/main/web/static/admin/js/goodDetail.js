// 后台小分类管理分页js
$(function () {
    // 文档加载成功调用ajax生成表格
    ajax(0);
});
// 按条件查询
function select() {
    let smallName = $('#select').val();
    let name = $('#name').val();
    let url = "/admin_admin_goodsSelect?";
    if (smallName !== "" && smallName !== undefined) {
        url += "smallName="+smallName+"&";
    }
    if (name !== "" && name !== undefined) {
        url += "name="+name+"&";
    }
    if (url[url.length-1] === "?") {
        javaex.message({
            content : "有什么意思呢",
            type : "error"
        });
    } else {
        ajax(0, url)
    }
}
// 发送ajax请求
// pageNum  第几页, perPageCount 每页几条
function ajax(pageNum, u) {
    let currentPage = pageNum;
    // 对第几页进行处理
    pageNum = (pageNum-1)*10;
    if (pageNum < 0)
        pageNum = 0;
    // 声明url, 是否是查询
    let p = {}
        ,url = "/admin_admin_goodDetail?start="+pageNum;
    if (u !== undefined && u !== "") {
        url = u+"&start="+pageNum;
    }
    $.ajax({
        url: url,
        async: false,
        dataType: "json",
        success:function(data) {
            p.totalPage = data[0].totalPage;
            p.total = data[0].total;
            console.log(data);
            // 先清空再填充
            $("#tbody tr").empty();
            if (data.length > 1) {
                data.splice(0,1);
                for (let i in data) {
                    $('#tbody').append("<tr>\n" +
                        "<td class=\"checkbox\"><input type=\"checkbox\" class=\"fill listen-1-2\" value='"+data[i].id+"'/> </td>\n" +
                        "                    <td>" + data[i].id + "</td>\n" +
                        "                    <td>" + data[i].goodsName + "</td>\n" +
                        "                    <td>" + data[i].smallClass.smallName + "</td>\n" +
                        "                    <td>" + data[i].goodsMoney + "</td>\n" +
                        "                    <td>" + data[i].goodsNumber + "</td>\n" +
                        "                    <td><a href=\"javascript:;\" onclick=\"img(this)\">\n" +
                        "\t\t<img src=\"/static/fore/images/" + data[i].goodsImage + "\" width=\"100px\" height=\"100px\">\n" +
                        "\t</a></td>\n" +
                        "<td><button class=\"button green\" onclick='edit(" + data[i].id + ")'>编辑</button><button class=\"button red\" onclick='dele(" + data[i].id + ")'>删除</button></td>\n" +
                        "                </tr>");
                }
                // 清空分页控件
                $("#page").empty();
                // 总页数为 1 不显示分页
                javaex.page({
                    id : "page",
                    pageCount : p.totalPage,	// 总页数
                    currentPage : currentPage,// 默认选中第几页
                    isShowJumpPage : true,	// 是否显示跳页
                    // totalNum : p.totalPage*10,		// 总条数，不填时，不显示
                    position : "center",
                    callback : function(rtn) {
                        console.log("当前选中的页数：" + rtn.pageNum);
                        console.log("每页显示条数：" + rtn.perPageCount);
                        // 点击分页控件触发的方法
                        ajax(rtn.pageNum, u);
                    }
                });
            } else {
                // 清空分页控件
                $("#page").empty();
                javaex.optTip({
                    content : "没有数据",
                    type : "error"
                });
            }
            javaex.render();
        }
    });
}

// 点击图片效果
// obj为当前点击的元素对象
function img(obj) {
    javaex.dialog({
        type : "image",	// 弹出层类型
        url : obj.children[0].src	// 图片地址
    });
}

// 添加一个商品
function add() {
    javaex.dialog({
        type : "window",
        title: "添加商品",
        id : "add",	// 指定id，仅当页面存在多个弹出层，需要关闭指定弹出层时才使用
        url : "/pages/admin/href/goodsAdd.jsp",	// 页面地址或网址或请求地址
        width : "800",	// 弹出层宽度
        height : "500",	// 弹出层高度
        isClickMaskClose: true,
    });
}

// 修改商品
function edit(id) {
    javaex.dialog({
        type : "window",
        title: "修改小分类",
        id : "add",	// 指定id，仅当页面存在多个弹出层，需要关闭指定弹出层时才使用
        url : "/admin_admin_goodsEdit?id="+id,	// 页面地址或网址或请求地址
        width : "800",	// 弹出层宽度
        height : "500",	// 弹出层高度
        isClickMaskClose: true,
    });
}

// 删除单个
function dele(id) {
    var id = id;
    javaex.alert({
        content : "确定删除",
        callback : "callback('"+id+"')"
    });
}

// 删除选中用户
function deleteOrders(obj, id) {
    let idArray = [];
    if ($("thead input[type='checkbox']").eq(0).prop("checked")) {
        // 删除当前页面所有
        $("tbody input[type='checkbox']").each(function () {
            idArray.push($(this).val());
        });
    } else {
        $("tbody input[type='checkbox']").each(function () {
            // 删除当前
            if ($(this).prop("checked") === true) {
                idArray.push($(this).val());
            }
        });
    }
    if (idArray.length <= 0) {
        javaex.message({
            content : "请选择",
            type : "error"
        });
    } else {
        javaex.deleteDialog(
            obj,	// obj是必须的
            {
                content : "确定要删除么",
                callback : "callback('"+idArray+"')"
            }
        );
    }
}

function callback(idArray) {
    $.ajax({
        url: "/admin_admin_goodsDelete?idArray="+idArray.toString(),
        type: "post",
        async: false,
        dataType: "text",
        success: function (data) {
            if (data === "true") {
                ajax(0);
            }
        }
    });
    // 如果你想阻止弹出层关闭，直接 return false; 即可
    //return false;
}

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
