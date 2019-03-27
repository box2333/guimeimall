package cn.jwq.controller;

import cn.itcast.commons.CommonUtils;
import cn.jwq.dao.*;
import cn.jwq.pojo.*;
import cn.jwq.util.ImageUtil;
import cn.jwq.util.Page;
import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

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
     * 管理员登陆
     *
     * @param request  请求
     * @param response 响应
     * @return 首页
     */
    public String login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("admin_admin_login--------------------->");
        AdminDao adminDao = new AdminDao();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Admin admin = null;
        if (name != null && password != null) {
            try {
                admin = adminDao.getAdmin(name, password);
                System.out.println(admin);
            } catch (SQLException e) {
                System.out.println("查询用户怎么可能报异常!!!!!!!!");
            }
        }
        if (admin != null) {
            request.getSession().setAttribute("admin", admin);
            return "f:/admin_admin_index";
        }
        request.setAttribute("msg", "用户名或者密码错误!");
        return "s:/pages/admin/login.jsp";
    }

    /**
     * 首页
     *
     * @param request  请求
     * @param response 响应
     * @return 返回首页地址
     */
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "s:/pages/admin/index.jsp";
    }

    /**
     * 查找所有用户
     *
     * @param request  请求
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
            } else if (sex == null && id == null) {
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
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + customerDao.count() + "},");
                System.out.println("userManageServlet" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + customerDao.count() + "}]";
            }
        } catch (SQLException e) {
            System.out.println("userManage-------> SQL 异常");
            return "";
        }
    }

    /**
     * 添加一个用户
     *
     * @param request  请求
     * @param response 响应
     * @return 返回成功或者失败
     */
    public String addUser(HttpServletRequest request, HttpServletResponse response) {
        CustomerDao customerDao = new CustomerDao();
        try {
            System.out.println("addUser------->");
            Customer customer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
            System.out.println("请求参数------>" + customer);
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
     *
     * @param request  请求
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
     *
     * @param request  请求
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
     *
     * @param request  请求
     * @param response 响应
     * @return 结果
     */
    public String userUpdate(HttpServletRequest request, HttpServletResponse response) {
        CustomerDao customerDao = new CustomerDao();
        try {
            System.out.println("updateUser------->");
            // 获取前台数据,封装成对象
            Customer fCustomer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
            System.out.println("前台数据------>" + fCustomer);
            // 根据id获取未修改数据
            Customer sCustomer = customerDao.getCustomer(fCustomer.getId());
            System.out.println("后台数据------>" + sCustomer);
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
     *
     * @param request  请求
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

    /**
     * 查询大分类
     *
     * @param request  请求
     * @param response 响应
     * @return 返回
     */
    public String bigClass(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("bigClass-------------------------------->");
        BigClassDao bigClassDao = new BigClassDao();
        int start = Integer.parseInt(request.getParameter("start"));
        Page page = new Page(start);
        List<BigClass> bigClassList = null;
        try {
            bigClassList = bigClassDao.list(page.getStart(), page.getCount());
            page.setTotal(bigClassDao.count());
            if (bigClassList != null) {
                // 转换json
                StringBuilder json = new StringBuilder(JSON.toJSONString(bigClassList));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + bigClassDao.count() + "},");
                System.out.println("userManageServlet" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + bigClassDao.count() + "}]";
            }
        } catch (SQLException e) {
            System.out.println("bigClassList查询失败----------------------" + bigClassList);
            return "f:404.jsp";
        }
    }

    /**
     * 查询大分类是否存在
     *
     * @param request  请求
     * @param response 响应
     * @return 是否存在
     */
    public String bigNameSelect(HttpServletRequest request, HttpServletResponse response) {
        String bigName = request.getParameter("bigName");
        BigClassDao bigClassDao = new BigClassDao();
        try {
            if (bigClassDao.isName(bigName) == 1) {
                return "false";
            }
        } catch (SQLException e) {
            System.out.println("bigName=================");
            return "false";
        }
        return "true";
    }

    /**
     * 添加一个大分类
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String bigClassAdd(HttpServletRequest request, HttpServletResponse response) {
        BigClassDao bigClassDao = new BigClassDao();
        // 把from表单数据封装成一个对象
        BigClass bigClass = CommonUtils.toBean(request.getParameterMap(), BigClass.class);
        // 判断是否传过来值
        if (bigClass.getBigName() != null) {
            try {
                bigClassDao.add(bigClass);
                return "true";
            } catch (SQLException e) {
                return "false";
            }
        }
        return "false";
    }

    /**
     * 根据id去删除大分类
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String bigClassDelete(HttpServletRequest request, HttpServletResponse response) {
        BigClassDao bigClassDao = new BigClassDao();
        String[] sid = request.getParameter("idArray").split(",");
        for (String aSid : sid) {
            try {
                bigClassDao.delete(Integer.parseInt(aSid));
            } catch (SQLException e) {
                System.out.println("删除失败");
            }
        }
        return "true";
    }

    /**
     * 根据条件查找大分类
     *
     * @param request  请求
     * @param response 响应
     * @return 结果集
     */
    public String bigClassSelect(HttpServletRequest request, HttpServletResponse response) {
        String bigName = request.getParameter("bigName");
        BigClassDao bigClassDao = new BigClassDao();
        List<BigClass> bigClassList;
        Page page = new Page(Integer.parseInt(request.getParameter("start")));
        try {
            bigClassList = bigClassDao.list(bigName, page.getStart(), page.getCount());
            if (bigClassList != null) {
                page.setTotal(bigClassDao.list(bigName, page.getStart(), Short.MAX_VALUE).size());
                // 转换json
                StringBuilder json = new StringBuilder(JSON.toJSONString(bigClassList));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + bigClassDao.count() + "},");
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + 0 + "}]";
            }
        } catch (SQLException e) {
            System.out.println("bigClassDao------------------------->SQL异常");
            return "";
        }
    }

    /**
     * 查找所有大分类名称
     *
     * @param request  请求
     * @param response 响应
     * @return 结果集
     */
    public String bigClassName(HttpServletRequest request, HttpServletResponse response) {
        BigClassDao bigClassDao = new BigClassDao();
        // 转换json
        try {
            return JSON.toJSONString(bigClassDao.getBigName());
        } catch (SQLException e) {
            System.out.println("查找所有大分类名称");
        }
        return "false";
    }

    /**
     * 查找单个BigClass
     *
     * @param request  请求
     * @param response 响应
     * @return 返回BigClass
     */
    public String bigClassEdit(HttpServletRequest request, HttpServletResponse response) {
        BigClassDao bigClassDao = new BigClassDao();
        BigClass bigClass = new BigClass();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            bigClass = bigClassDao.getBigClass(id);
            request.setAttribute("bigClass", bigClass);
        } catch (Exception e) {
            // 不带参访问,跳转404
            return "f:404.jsp";
        }
        return "s:/pages/admin/href/bigClassUpdate.jsp";
    }

    /**
     * 更新bigClass
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String bigClassUpdate(HttpServletRequest request, HttpServletResponse response) {
        BigClassDao bigClassDao = new BigClassDao();
        try {
            System.out.println("bigClassUpdate--------------------->");
            // 获取前台数据,封装成对象
            BigClass fBigClass = CommonUtils.toBean(request.getParameterMap(), BigClass.class);
            System.out.println("前台数据------>" + fBigClass);
            // 根据id获取未修改数据
            BigClass sBigClass = bigClassDao.getBigClass(fBigClass.getId());
            System.out.println("后台数据------>" + sBigClass);
            // 如果分类名相同直接去修改, 或者垓分类名没有占用
            if (fBigClass.getBigName().equals(sBigClass.getBigName()) ||
                    bigClassDao.count(fBigClass.getBigName()) < 1) {
                bigClassDao.updateCustomer(fBigClass);
                return "true";
            } else {
                // 用户名不同,去判断新用户名是否被占用
                return "name";
            }
        } catch (Exception e) {
            System.out.println("添加用户发生了异常");
            return "false";
        }
    }

    /**
     * 查询小分类
     *
     * @param request  请求
     * @param response 响应
     * @return 结果集
     */
    public String smallClass(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("smallClass-------------------------------->");
        SmallClassDao smallClassDao = new SmallClassDao();
        int start = Integer.parseInt(request.getParameter("start"));
        Page page = new Page(start);
        List<SmallClass> smallClassList = null;
        try {
            smallClassList = smallClassDao.list(page.getStart(), page.getCount());
            page.setTotal(smallClassDao.count());
            if (smallClassList != null) {
                // 转换json
                StringBuilder json = new StringBuilder(JSON.toJSONString(smallClassList));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + smallClassDao.count() + "},");
                System.out.println("smallClass--->" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + smallClassDao.count() + "}]";
            }
        } catch (SQLException e) {
            System.out.println("bigClassList查询失败----------------------" + smallClassList);
            return "f:404.jsp";
        }
    }

    /**
     * 查询小分类是否存在
     *
     * @param request  请求
     * @param response 响应
     * @return 是否存在
     */
    public String smallNameSelect(HttpServletRequest request, HttpServletResponse response) {
        String smallName = request.getParameter("smallName");
        SmallClassDao smallClassDao = new SmallClassDao();
        try {
            if (smallClassDao.isName(smallName) == 1) {
                return "false";
            }
        } catch (SQLException e) {
            System.out.println("smallNameSelect=================");
            return "false";
        }
        return "true";
    }

    /**
     * 添加一个小分类
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String smallNameAdd(HttpServletRequest request, HttpServletResponse response) {
        SmallClassDao smallClassDao = new SmallClassDao();
        BigClassDao bigClassDao = new BigClassDao();
        String smallName = request.getParameter("smallName");
        try {
            // 判断是否已经存在
            if (smallClassDao.isName(smallName) == 0) {
                String bigNameId = request.getParameter("select");
                String smallText = request.getParameter("smallText");
                // 获取对应的bigClassId
                int bigId = Integer.parseInt(bigNameId);
                // 创建SmallClass对象
                SmallClass smallClass = new SmallClass();
                smallClass.setSmallBigId(bigId);
                smallClass.setSmallName(smallName);
                smallClass.setSmallText(smallText);
                // 存数据库
                smallClassDao.add(smallClass);
                return "true";
            } else {
                return "name";
            }
        } catch (SQLException e) {
            return "false";
        }
    }

    /**
     * 删除一个小分类
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String smallClassDelete(HttpServletRequest request, HttpServletResponse response) {
        SmallClassDao smallClassDao = new SmallClassDao();
        System.out.println("smallClassDelete--------------------------->");
        String[] sid = request.getParameter("idArray").split(",");
        System.out.println(sid);
        for (String aSid : sid) {
            try {
                System.out.println(aSid);
                smallClassDao.delete(Integer.parseInt(aSid));
            } catch (SQLException e) {
                System.out.println("删除失败");
                return "false";
            }
        }
        return "true";
    }

    /**
     * 查找一个小分类
     *
     * @param request  请求
     * @param response 响应
     * @return 服务器跳转
     */
    public String smallClassEdit(HttpServletRequest request, HttpServletResponse response) {
        SmallClassDao smallClassDao = new SmallClassDao();
        BigClassDao bigClassDao = new BigClassDao();
        SmallClass smallClass = new SmallClass();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            smallClass = smallClassDao.getSmallClass(id);
            // 根据小分类取大分类
            smallClass.setBigClass(bigClassDao.getBigClass(smallClass.getSmallBigId()));
            request.setAttribute("smallClass", smallClass);
        } catch (Exception e) {
            // 不带参访问,跳转404
            return "f:404.jsp";
        }
        return "s:/pages/admin/href/smallUpdate.jsp";
    }

    /**
     * 更新小分类
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String smallUpdate(HttpServletRequest request, HttpServletResponse response) {
        SmallClassDao smallClassDao = new SmallClassDao();
        try {
            System.out.println("smallUpdate--------------------->");
            // 获取前台数据,封装成对象
            SmallClass fSmallClass = CommonUtils.toBean(request.getParameterMap(), SmallClass.class);
            System.out.println("前台数据------>" + fSmallClass);
            // 根据id获取未修改数据
            SmallClass sSmallClass = smallClassDao.getSmallClass(fSmallClass.getId());
            System.out.println("后台数据------>" + sSmallClass);
            // 如果分类名相同直接去修改, 或者改分类名没有占用
            if (fSmallClass.getSmallName().equals(sSmallClass.getSmallName()) ||
                    smallClassDao.count(fSmallClass.getSmallName()) < 1) {
                smallClassDao.updateCustomer(fSmallClass);
                return "true";
            } else {
                // 用户名不同,去判断新用户名是否被占用
                return "name";
            }
        } catch (Exception e) {
            System.out.println("添加小分类发生了异常");
            return "false";
        }
    }

    /**
     * 按照条件查询小分类
     *
     * @param request  请求
     * @param response 响应
     * @return 异常
     */
    public String smallClassSelect(HttpServletRequest request, HttpServletResponse response) {
        SmallClassDao smallClassDao = new SmallClassDao();
        List<SmallClass> smallClassList;
        String bigNameId = null;
        String smallName = null;
        int start = Integer.parseInt(request.getParameter("start"));
        Page page = new Page(start);
        try {
            bigNameId = request.getParameter("bigNameId");
            smallName = request.getParameter("smallName");
        } catch (Exception e) {
            System.out.println("取值异常,不管");
        }
        try {
            smallClassList = smallClassDao.list(smallName, bigNameId, page.getStart(), page.getCount());
            // 获取总记录数
            page.setTotal(smallClassDao.list(smallName, bigNameId, 0, Short.MAX_VALUE).size());
            if (!smallClassList.isEmpty()) {
                // 转换json
                System.out.println(smallClassList);
                StringBuilder json = new StringBuilder(JSON.toJSONString(smallClassList));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + page.getTotal() + "},");
                System.out.println("userManageServlet" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + page.getTotal() + "}]";
            }
        } catch (SQLException e) {
            System.out.println("----------------->smallClassSelect的SQL异常");
            return "";
        }
    }

    /**
     * 查找所有小分类名称
     *
     * @param request  请求
     * @param response 响应
     * @return 结果集
     */
    public String smallClassName(HttpServletRequest request, HttpServletResponse response) {
        SmallClassDao smallClassDao = new SmallClassDao();
        // 转换json
        try {
            return JSON.toJSONString(smallClassDao.getSmallName());
        } catch (SQLException e) {
            System.out.println("查找所有小分类名称");
        }
        return "false";
    }

    /**
     * 查找所有商品
     *
     * @param request  请求
     * @param response 响应
     * @return 商品结果集
     */
    public String goodDetail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("goodDetail-------------------------------->");
        GoodsDao goodsDao = new GoodsDao();
        int start = Integer.parseInt(request.getParameter("start"));
        Page page = new Page(start);
        List<Goods> goods = null;
        try {
            goods = goodsDao.list(page.getStart(), page.getCount());
            page.setTotal(goodsDao.count());
            if (goods != null) {
                // 转换json
                StringBuilder json = new StringBuilder(JSON.toJSONString(goods));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + goodsDao.count() + "},");
                System.out.println("goodDetail" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + goodsDao.count() + "}]";
            }
        } catch (SQLException e) {
            System.out.println("goods查询失败----------------------" + goods);
            return "f:404.jsp";
        }
    }

    /**
     * 查询商品名称是否重复
     *
     * @return 可用否
     */
    public String goodsNameSelect(HttpServletRequest request, HttpServletResponse response) {
        String goodsName = request.getParameter("goodsName");
        GoodsDao goodsDao = new GoodsDao();
        try {
            if (goodsDao.isName(goodsName) == 1) {
                return "false";
            }
        } catch (SQLException e) {
            System.out.println("goodsNameSelect=================");
            return "false";
        }
        return "true";
    }

    /**
     * 添加商品
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     * @throws IOException 异常
     */
    public String goodsAdd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取表单参数
        String imgBase64 = request.getParameter("imgBase64");
        String goodsName = request.getParameter("goodsName");
        String smallNameId = request.getParameter("smallName");
        String goodsMoney = request.getParameter("goodsMoney");
        String goodsNumber = request.getParameter("goodsNumber");
        String goodsCarriage = request.getParameter("goodsCarriage");
        // 创建商品对象
        GoodsDao goodsDao = new GoodsDao();

        Goods goods = new Goods();
        goods.setGoodsName(goodsName);
        goods.setGoodsSmalId(Integer.parseInt(smallNameId));
        goods.setGoodsMoney(Double.valueOf(goodsMoney));
        goods.setGoodsNumber(Integer.parseInt(goodsNumber));
        goods.setGoodsCarriage(Double.valueOf(goodsCarriage));
        goods.setGoodsType(0);
        goods.setGoodsDiscId(4);

        // 相对路径
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("static/fore/images"));
        goods.setGoodsImage(String.valueOf(System.currentTimeMillis()) + ".jpg");
        File file = new File(imageFolder, goods.getGoodsImage());
        // 去掉base64头部,不然失败
        imgBase64 = imgBase64.substring(imgBase64.indexOf(",") + 1);
        System.out.println(file.getCanonicalPath());
        int i = 0;
        try {
            System.out.println(goods);
            // 添加到数据库
            i = goodsDao.add(goods);
            if (i > 0) {
                // 成功后保存图片
                ImageUtil.generateImage(imgBase64, file.getCanonicalPath());
                response.getWriter().write("<script>alter('成功!');window.open('','_top')</script>");
            }
        } catch (SQLException e) {
            System.out.println("添加商品失败------------------------>");
            response.getWriter().write("<script>alter('失败!');window.open('','_top')</script>");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 删除摸个商品
     *
     * @param request  请求
     * @param response 响应
     * @return 成功否
     */
    public String goodsDelete(HttpServletRequest request, HttpServletResponse response) {
        GoodsDao goodsDao = new GoodsDao();
        String[] sid = request.getParameter("idArray").split(",");
        for (String aSid : sid) {
            try {
                int id = Integer.parseInt(aSid);
                // 查询图片路径
                String goodsImagePath = goodsDao.getImagePath(id);
                if (goodsDao.delete(id) >= 1) {
                    // 删除图片 相对路径
                    File imageFolder = new File(request.getSession().getServletContext().getRealPath("static/fore/images"));
                    ;
                    File file = new File(imageFolder, goodsImagePath);
                    file.delete();
                }
            } catch (Exception e) {
                System.out.println("删除失败");
                return "false";
            }
        }
        return "true";
    }

    /**
     * 根据id查询对应的商品
     *
     * @param request  请求
     * @param response 响应
     * @return 商品Class
     */
    public String goodsEdit(HttpServletRequest request, HttpServletResponse response) {
        GoodsDao goodsDao = new GoodsDao();
        Goods goods = new Goods();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            goods = goodsDao.getGoods(id);
            request.setAttribute("goods", goods);
        } catch (Exception e) {
            // 不带参访问,跳转404
            return "f:404.jsp";
        }
        return "s:/pages/admin/href/goodsUpdate.jsp";
    }

    /**
     * 更新商品
     * @param request 请求
     * @param response 响应
     * @return 无返回值
     */
    public String goodsUpdate(HttpServletRequest request, HttpServletResponse response) {
        // 获取表单参数
        String id = request.getParameter("id");
        String imgBase64 = request.getParameter("imgBase64");
        String goodsName = request.getParameter("goodsName");
        String smallNameId = request.getParameter("smallName");
        String goodsMoney = request.getParameter("goodsMoney");
        String goodsNumber = request.getParameter("goodsNumber");
        String goodsCarriage = request.getParameter("goodsCarriage");
        // 创建商品对象
        GoodsDao goodsDao = new GoodsDao();

        Goods goods = new Goods();
        goods.setId(Integer.parseInt(id));
        goods.setGoodsName(goodsName);
        goods.setGoodsSmalId(Integer.parseInt(smallNameId));
        goods.setGoodsMoney(Double.valueOf(goodsMoney));
        goods.setGoodsNumber(Integer.parseInt(goodsNumber));
        goods.setGoodsCarriage(Double.valueOf(goodsCarriage));
        goods.setGoodsType(0);
        goods.setGoodsDiscId(4);

        // 相对路径
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("static/fore/images"));
        goods.setGoodsImage(String.valueOf(System.currentTimeMillis()) + ".jpg");
        File file = new File(imageFolder, goods.getGoodsImage());
        // 去掉base64头部,不然失败
        imgBase64 = imgBase64.substring(imgBase64.indexOf(",") + 1);
        int i = 0;
        try {
            // 获取后台数据
            Goods sGoods = goodsDao.getGoods(goods.getId());
            // 当用户名与原来相同或者用户名没有被占用更新
            if (goods.getGoodsName().equals(sGoods.getGoodsName())
                    || goodsDao.count(goods.getGoodsName()) < 1) {
                // 添加到数据库
                i = goodsDao.update(goods);
                if (i > 0) {
                    // 成功后保存图片
                    System.out.println(imgBase64);
                    System.out.println(file.getCanonicalPath());
                    ImageUtil.generateImage(imgBase64, file.getCanonicalPath());
                    response.getWriter().write("<script>alter('成功!');window.open('','_top')</script>");
                } else {
                    response.getWriter().write("<script>alter('失败!');window.open('','_top')</script>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 按条件查询
     * @param request 请求
     * @param response 响应
     * @return 结果集
     */
    public String goodsSelect(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("======================goodsSelect==========================");
        GoodsDao goodsDao = new GoodsDao();
        List<Goods> goodsList = null;
        String smallName = null;
        String name = null;
        int start = Integer.parseInt(request.getParameter("start"));
        Page page = new Page(start);
        try {
            smallName = request.getParameter("smallName");
            name = request.getParameter("name");
        } catch (Exception e) {
            System.out.println("取值异常,不管");
        }
        try {
            goodsList = goodsDao.list(smallName, name, page.getStart(), page.getCount());
            // 获取总记录数
            page.setTotal(goodsDao.list(smallName, name, 0, Short.MAX_VALUE).size());
            if (!goodsList.isEmpty()) {
                // 转换json
                System.out.println(goodsList);
                StringBuilder json = new StringBuilder(JSON.toJSONString(goodsList));
                // 把分页对象添加到json里
                json.insert(1, "{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + page.getTotal() + "},");
                System.out.println("goodsSelect" + json);
                return json.toString();
            } else {
                return "[{\"totalPage\":" + page.getTotalPage() + ",\"total\":" + page.getTotal() + "}]";
            }
        } catch (SQLException e) {
            System.out.println("----------------->goodsSelect的SQL异常");
            return "";
        }
    }
}
