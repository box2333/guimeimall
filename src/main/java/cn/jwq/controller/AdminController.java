package cn.jwq.controller;

import cn.itcast.commons.CommonUtils;
import cn.jwq.dao.CustomerDao;
import cn.jwq.pojo.Customer;
import cn.jwq.util.Page;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-15:17
 */
@WebServlet(name = "AdminServlet", urlPatterns = "/adminServlet")
public class AdminController extends BaseBackServlet {

    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "f:/404.jsp";
    }

    /**
     * 首页
     * @param request 请求
     * @param response 响应
     * @return 返回首页地址
     */
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "s:/pages/admin/index.jsp";
    }

    /**
     * 查找所有用户
     * @param request 请求
     * @param response 响应
     * @return 查询到用户列表
     */
    public String userManage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("userManage------>");
        List<Customer> customerList = new ArrayList<Customer>();
        CustomerDao customerDao = new CustomerDao();
        // 取出所有参数
        Map<String, String[]> map = request.getParameterMap();
        // 取出传递参数,并创建分页对象
        String start = map.get("start")[0];
        Page page = new Page(Integer.parseInt(start));
        // 取出性别
        String sex = null;
        if (map.containsKey("sex")) {
            sex = map.get("sex")[0];
        }
        // 取出用户名
        String name = null;
        if (map.containsKey("name")) {
            name = map.get("name")[0];
        }
        // 取出id
        String id = null;
        if (map.containsKey("cid")) {
            id = map.get("cid")[0];
        }

        // 查找数据
        try {
            boolean huiZong = ("".equals(name) || name == null) && sex == null && id == null;
            // 查找总记录数
            if (huiZong) {
                System.out.println(name);
                customerList = customerDao.list(page.getStart(), page.getCount());
                // 获取总记录数
                page.setTotal(customerDao.count());
            } else if (sex == null && id == null){
                // 按照name进行查询
                customerList = customerDao.list(name, page.getStart(), page.getCount());
                // 获取总记录数
                page.setTotal(customerDao.count(name));
            } else {
                customerList = customerDao.list(name, sex, id, page.getStart(), page.getCount());
                // 获取总记录数
                page.setTotal(customerDao.list(name, sex, id, 0, Short.MAX_VALUE).size());
            }
            if (!customerList.isEmpty()) {
                // 转换json
                System.out.println(customerList);
                StringBuilder json = new StringBuilder(JSON.toJSONString(customerList));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":"+page.getTotalPage()+",\"total\":"+customerDao.count()+"},");
                System.out.println("userManageServlet" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":"+page.getTotalPage()+",\"total\":"+customerDao.count()+"}]";
            }
        } catch (SQLException e) {
            System.out.println("userManage-------> SQL 异常");
            return "";
        }
    }

    /**
     * 添加一个用户
     * @param request 请求
     * @param response 响应
     * @return 返回成功或者失败
     */
    public String addUser(HttpServletRequest request, HttpServletResponse response) {
        CustomerDao customerDao = new CustomerDao();
        try {
            System.out.println("addUser------->");
            Customer customer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
            System.out.println("请求参数------>"+customer);
            // 判断用户名是否已经存在
            if (customerDao.count(customer.getCusLoginName()) > 0) {
                // 用户名已经存在返回值
                return "user";
            }
            if (customer.getCusLoginName() != null) {
                // 保存成功
                customerDao.add(customer);
                return "true";
            }
        } catch (Exception e) {
            System.out.println("添加用户发生了异常");
            return "false";
        }
        return "false";
    }

    /**
     * 查询用户名是否被占用
     * @param request 请求
     * @param response 响应
     * @return true 可以使用
     */
    public String selectLoginName(HttpServletRequest request, HttpServletResponse response) {
        String loginName = request.getParameter("loginName");
        CustomerDao customerDao = new CustomerDao();
        try {
            if (customerDao.count(loginName) > 0) {
                System.out.println(customerDao.count(loginName));
                return "false";
            }
        } catch (SQLException e) {
            System.out.println("selectLoginName -------> SQL 异常");
            return "false";
        }
        return "true";
    }

    /**
     * 编辑用户
     * @param request 请求
     * @param response 响应
     * @return 请求地址
     */
    public String editUser(HttpServletRequest request, HttpServletResponse response) {
        Customer customer;
        CustomerDao customerDao = new CustomerDao();
        // 获取传过来的 id
        int cid = Integer.parseInt(request.getParameter("id"));
        // 查询出对象
        try {
            customer = customerDao.getCustomer(cid);
            // 如果有结果存到 请求
            if (customer != null) {
                System.out.println(customer.getCusBirthday());
                request.setAttribute("customer", customer);
            } else {
                // 没有结果,非法入侵,跳转404
                return "f:404.jsp";
            }
        } catch (SQLException e) {
            System.out.println("userUpdate------> sql 失败");
            return "f:404.jsp";
        }
        return "s:/pages/admin/href/userUpdate.jsp";
    }

    /**
     * 更新一个用户
     * @param request 请求
     * @param response 响应
     * @return 结果
     */
    public String userUpdate(HttpServletRequest request, HttpServletResponse response) {
        CustomerDao customerDao = new CustomerDao();
        try {
            System.out.println("updateUser------->");
            // 获取前台数据,封装成对象
            Customer fCustomer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
            System.out.println("前台数据------>"+fCustomer);
            // 根据id获取未修改数据
            Customer sCustomer = customerDao.getCustomer(fCustomer.getId());
            System.out.println("后台数据------>"+sCustomer);
            // 如果用户名相同直接去修改
            if (fCustomer.getCusLoginName().equals(sCustomer.getCusLoginName()) ||
                    customerDao.count(fCustomer.getCusLoginName()) <= 1) {
                customerDao.updateCustomer(fCustomer);
                return "true";
            } else {
                // 用户名不同,去判断新用户名是否被占用
                return "user";
            }
        } catch (Exception e) {
            System.out.println("添加用户发生了异常");
            return "false";
        }
    }

    /**
     * 根据id删除user用户
     * @param request 请求
     * @param response 响应
     * @return 服务器跳转userManage.jsp
     */
    public String userDelete(HttpServletRequest request, HttpServletResponse response) {
        CustomerDao customerDao = new CustomerDao();
        String[] sid = request.getParameter("idArray").split(",");
        for (String aSid : sid) {
            try {
                customerDao.delete(Integer.parseInt(aSid));
            } catch (SQLException e) {
                System.out.println("删除失败");
            }
        }
        return "true";
    }
}
