package cn.jwq.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-15:17
 */
@WebServlet(name = "AdminServlet", urlPatterns = "/adminServlet")
public class AdminController extends BaseBackServlet {

    public String test(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("a", "bbbb");
        return "/404.jsp";
    }

    /**
     * 首页
     * @param request 请求
     * @param response 响应
     * @return 返回首页地址
     */
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "/pages/admin/index.jsp";
    }
}
