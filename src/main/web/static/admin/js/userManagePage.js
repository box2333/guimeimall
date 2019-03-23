// 后台用户管理分页js
$(function () {
    // 文档加载成功调用ajax生成表格
    ajax("", 0);
    // 调用javaEx 方法生成分页控件
});

function select() {
    let name = $('#name').val();
    if (name !== null && name !== undefined && name !== "") {
        ajax(name,1)
    } else {
        javaex.optTip({
            content : "请输入用户名",
            type : "error"
        });
    }
}
// 发送ajax请求
// pageNum  第几页, perPageCount 每页几条
function ajax(name, pageNum) {
    let currentPage = pageNum;
    // 对第几页进行处理
    pageNum = (pageNum-1)*10;
    if (pageNum < 0)
        pageNum = 0;
    // 声明url, 是否是查询
    let p = {}
        ,url = "/userManage?start="+pageNum+"&name="+name;
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
                        "                    <td>" + data[i].id + "</td>\n" +
                        "                    <td>" + data[i].cusLoginName + "</td>\n" +
                        "                    <td>" + data[i].cusName + "</td>\n" +
                        "                    <td>" + data[i].cusSex + "</td>\n" +
                        "                    <td>" + data[i].cusEmail + "</td>\n" +
                        "                    <td>" + data[i].cusBirthday + "</td>\n" +
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
                        ajax(name, rtn.pageNum, rtn.pageNum);
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