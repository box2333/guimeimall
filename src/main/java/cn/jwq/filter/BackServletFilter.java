package cn.jwq.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jia
 * @Description 请求Servlet 过滤器
 * @date 2019-03-23-13:58
 */
public class BackServletFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 把ServletRequest,ServletResponse 强转成http类型的
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 禁用浏览器缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);
        // 请求资源名
        String uri =  request.getRequestURI();
        // 如果是css,js文件不做处理,执行下一个过滤器
        System.out.println("请求地址----"+uri);
        if (uri.startsWith("/static") || uri.endsWith(".jsp") || uri.endsWith(".html")) {
            System.out.println("不处理地址----"+uri);
            filterChain.doFilter(request, response);
        }
        // 汉字转码
        response.setCharacterEncoding("utf-8");
        // 个人认为请求资源名"/a_a_a",最少要大于6
        int length = 6;
        // 请求 admin 的Servlet 的资源名
        String admin = "/admin_";
        // 请求 user 的Servlet 的资源名
        String user = "/mall_";
        // 判断是否有资源名
        if (uri.length() > length) {
            try {
                // 判断应该转发到哪里去
                if (uri.startsWith(admin) || uri.startsWith(user)) {
                    // 请求的Servlet
                    String servletPath = uri.substring(uri.indexOf("_") + 1, uri.lastIndexOf("_")) + "Servlet";
                    // 请求的方法名称
                    String method = uri.substring(uri.lastIndexOf("_") + 1);
                    // 请求的方法名称放到 request 里
                    request.setAttribute("method", method);
                    request.getRequestDispatcher("/" + servletPath).forward(request, response);
                }
            } catch (StringIndexOutOfBoundsException e) {
                // 请求的Servlet 与 请求的方法名称 不对转向404
                request.getRequestDispatcher("/404.jsp").forward(request,response);
            }
        } else {
            request.getRequestDispatcher("/404.jsp").forward(request,response);
        }
    }

    /**
     * 初始化调用的方法
     * @param filterConfig 过滤器配置
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    /**
     * 销毁的时候调用的方法
     */
    public void destroy() {

    }
}
