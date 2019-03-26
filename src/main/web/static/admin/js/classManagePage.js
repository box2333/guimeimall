// 后台大分类管理分页js
$(function () {
    // 文档加载成功调用ajax生成表格
    ajax("", 0);
});

// 发送ajax请求
// pageNum  第几页, perPageCount 每页几条
function ajax(name, pageNum, u) {
    let currentPage = pageNum;
    // 对第几页进行处理
    pageNum = (pageNum-1)*10;
    if (pageNum < 0)
        pageNum = 0;
    // 声明url, 是否是查询
    let p = {}
        ,url = "/admin_admin_bigClass?start="+pageNum+"&name="+name;
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
                        "                    <td>" + data[i].bigName + "</td>\n" +
                        "                    <td>" + data[i].bigText + "</td>\n" +
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
                    totalNum : 10,		// 总条数，不填时，不显示
                    position : "center",
                    callback : function(rtn) {
                        console.log("当前选中的页数：" + rtn.pageNum);
                        console.log("每页显示条数：" + rtn.perPageCount);
                        // 点击分页控件触发的方法
                        ajax(name, rtn.pageNum);
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