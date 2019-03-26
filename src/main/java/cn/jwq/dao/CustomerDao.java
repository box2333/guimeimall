package cn.jwq.dao;

import cn.jwq.pojo.Customer;
import cn.jwq.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-22:57
 */
public class CustomerDao {
    private static QueryRunner qr = DBUtil.getQueryRunner();

    /**
     * 查询总记录数
     * @return 返回总记录数
     * @throws SQLException 异常
     */
    public int count() throws SQLException {
        String sql = "select count(id) from customer";
        return ((Number) qr.query(sql, new ScalarHandler())).intValue();
    }

    /**
     * 查询总记录数
     * @param name 查询名字
     * @return 返回总记录数
     * @throws SQLException 异常
     */
    public int count(String name) throws SQLException {
        String sql = "select count(id) from customer where cusLoginName = ?";
        Object[] params = {name};
        return ((Number) qr.query(sql, new ScalarHandler(), params)).intValue();
    }

    /**
     * 查询所有用户信息
     * @return 用户列表
     * @throws SQLException 异常
     */
    public List<Customer> list() throws SQLException {
        return list(0, Short.MAX_VALUE);
    }
    /**
     * 分页查询所有用户
     * @param start 起始位置
     * @param count 多少页
     * @return  查询结果列表
     * @throws SQLException 异常
     */
    public List<Customer> list(int start, int count) throws SQLException {
        String sql = "select * from customer order by id limit ?, ?";
        Object[] params = {start, count};
        return qr.query(sql, new BeanListHandler<Customer>(Customer.class), params);
    }

    /**
     * 模糊查询用户
     * @param start 起始位置
     * @param count 多少页
     * @return  查询结果列表
     * @throws SQLException 异常
     */
    public List<Customer> list(String name, int start, int count) throws SQLException {
        String sql = "select * from customer where cusLoginName like ? order by id limit ?, ?";
        Object[] params = {"%"+name+"%", start, count};
        return qr.query(sql, new BeanListHandler<Customer>(Customer.class), params);
    }

    /**
     * 多条件查询
     * @param start 起始位置
     * @param count 多少页
     * @return  查询结果列表
     * @throws SQLException 异常
     */
    public List<Customer> list(String name, String sex, String id, int start, int count) throws SQLException {
        boolean flag = false;
        String sql = "select * from customer";
        List<Object> objects = new ArrayList<Object>();
        if (name != null) {
            sql += " where cusLoginName like ? ";
            flag = true;
            objects.add("%"+name+"%");
        }
        if (sex != null) {
            sql += flag ? " and cusSex = ? " : " where cusSex = ? ";
            flag = true;
            objects.add(sex);
        }
        if (id != null) {
            sql += flag ? " and id = ? " : " where cusSex = ? ";
            objects.add(Integer.parseInt(id));
        }
        sql += " order by id limit ?, ? ";
        System.out.println("-----------------"+sql);
        objects.add(start);
        objects.add(count);
        Object[] params = objects.toArray();
        return qr.query(sql, new BeanListHandler<Customer>(Customer.class), params);
    }

    /**
     * 添加一个用户信息
     * @param c 被添加的用户信息
     * @throws SQLException 异常
     */
    public void add(Customer c) throws SQLException {
        String sql = "insert into customer(cusName,cusLoginName,cusPassword,cusEmail,cusSex,cusPhoto,cusHobby,cusCode,cusBirthday)\n" +
                " values(?,?,?,?,?,?,?,?,?)";
        Object[] params = {c.getCusName(), c.getCusLoginName(), c.getCusPassword(), c.getCusEmail()
            , c.getCusSex(), c.getCusPhoto(), c.getCusHobby(), c.getCusCode(), c.getCusBirthday()};
        qr.update(sql, params);
    }

    /**
     * 根据id 得到一个用户
     * @param id 查询条件
     * @return  返回一个查到的对象
     * @throws SQLException 异常
     */
    public Customer getCustomer(int id) throws SQLException {
        String sql = "select * from customer where id = ?";
        Object[] params = {id};
        return qr.query(sql, new BeanHandler<Customer>(Customer.class), params);
    }

    /**
     * 根据id修改一个用户
     * @param c 用户对象
     * @throws SQLException 异常
     */
    public void updateCustomer(Customer c) throws SQLException {
        String sql = "update customer set cusName = ?, cusLoginName = ?" +
                "                , cusPassword = ?, cusEmail = ?, cusSex = ?, cusPhoto = ?" +
                "                , cusHobby = ?, cusCode = ?, cusBirthday = ?, cusPhone = ?" +
                "                where id = ?";
        Object[] params = {c.getCusName(), c.getCusLoginName(), c.getCusPassword(), c.getCusEmail()
                , c.getCusSex(), c.getCusPhoto(), c.getCusHobby(), c.getCusCode(), c.getCusBirthday()
                , c.getCusPhone(), c.getId()};
        qr.update(sql, params);
    }

    /**
     * 根据id删除一个用户
     * @param id 删除的id
     * @throws SQLException 异常
     */
    public void delete(int id) throws SQLException {
        String sql = "delete from customer where id = ?";
        Object[] params = {id};
        qr.update(sql, params);
    }

    public static void main(String[] args) throws SQLException {
        boolean flag = false;
        String name = null;
        String sex = "a";
        String id = null;
        String sql = "select * from customer ";

        System.out.println(new CustomerDao().list(name, sex, id, 0, 100));
    }
}
