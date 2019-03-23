package cn.jwq.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-15:00
 */
public class BaseBackServlet extends HttpServlet {

    /**
     * 所有的servlet请求都会来到这里
     * 从这里判断应该去调用哪个servlet类的哪个方法
     * @param req 请求
     * @param resp 响应
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*借助反射，调用对应的方法*/
        // 方法名
        String method = (String) req.getAttribute("method");
        System.out.println(method);
        try {
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
                    javax.servlet.http.HttpServletResponse.class);
            String redirect = m.invoke(this,req, resp).toString();
            /*根据方法的返回值，进行相应的客户端跳转，服务端跳转，或者仅仅是输出字符串*/
            if(redirect.startsWith("@")) {
                // 进行客户端跳转
                resp.sendRedirect(redirect.substring(1));
            } else if(redirect.startsWith("%")) {
                // 直接输出字符串
                resp.getWriter().print(redirect.substring(1));
            } else {
                // 进行服务端跳转
                req.getRequestDispatcher(redirect).forward(req, resp);
            }
        } catch (NoSuchMethodException e) {
            // 没有请求的方法会到这里
            // 跳转到 404
            // 进行客户端跳转
            resp.sendRedirect("/404.jsp");
        } catch (IllegalAccessException e) {
            System.out.println("BaseServlet有问题");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("BaseServlet有问题");
            e.printStackTrace();
        }

    }
}
