package cn.jwq.dao;

import cn.jwq.pojo.BigClass;
import cn.jwq.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author jia
 * @Description
 * @date 2019-03-26-9:33
 */
public class BigClassDao {
    private static QueryRunner qr = DBUtil.getQueryRunner();


    /**
     * 查询总记录数
     * @return 返回总记录数
     * @throws SQLException 异常
     */
    public int count() throws SQLException {
        String sql = "select count(id) from bigclass";
        return ((Number) qr.query(sql, new ScalarHandler())).intValue();
    }

    public int count(String name) throws SQLException {
        String sql = "select count(id) from bigclass where bigName like ?";
        Object[] params = {name};
        return ((Number) qr.query(sql, new ScalarHandler(), params)).intValue();
    }

    /**
     * 查询所有bigClass
     * @return 返回bigClass 列表
     * @throws SQLException 异常
     */
    public List<BigClass> list() throws SQLException {
        return list(0, Short.MAX_VALUE);
    }

    /**
     * 分页查询所有大分类
     * @param start 起点
     * @param count 总数
     * @return 返回bigClass 列表
     * @throws SQLException 异常
     */
    public List<BigClass> list(int start, int count) throws SQLException {
        return list(null, start, count);
    }

    /**
     * 根据大分类名称分页查询所有大分类
     * @param start 起点
     * @param count 总数
     * @return 返回bigClass 列表
     * @throws SQLException 异常
     */
    public List<BigClass> list(String bigName, int start, int count) throws SQLException {
        String sql;
        Object[] params;
        if (bigName == null) {
            sql = "select * from bigclass order by id limit ?, ?";
            params = new Object[]{start, count};
        }  else {
            sql = "select * from bigclass where bigName like ? order by id limit ?, ?";
            params = new Object[]{"%" + bigName + "%", start, count};
        }
        return qr.query(sql, new BeanListHandler<BigClass>(BigClass.class), params);
    }

    /**
     * 根据大分类名称查询是否存在
     * @param name 大分类名称
     * @return 0不不存在,1存在
     * @throws SQLException
     */
    public int getId(String name) throws SQLException {
        String sql = "select id from bigclass where bigName = ?";
        Object[] params = {name};
        Object object = qr.query(sql, new ScalarHandler(), params);
        if (object == null) {
            return 0;
        }
        return 1;
    }

    /**
     * 添加一个用户
     * @param bigClass 用户对象
     * @throws SQLException 异常
     */
    public void add(BigClass bigClass) throws SQLException {
        String sql = "insert into bigclass(bigName,bigText) values(?, ?)";
        Object[] params = {bigClass.getBigName(), bigClass.getBigText()};
        qr.update(sql, params);
    }

    /**
     * 删除一个用户
     * @param id 用户id
     * @throws SQLException 异常
     */
    public void delete(int id) throws SQLException {
        String sql = "delete from bigclass where id = ?";
        Object[] params = {id};
        qr.update(sql, params);
    }

    /**
     * 根据id 得到一个bigClass
     * @param id id
     * @return BigClass
     * @throws SQLException 异常
     */
    public BigClass getBigClass(int id) throws SQLException {
        String sql = "select * from bigclass where id = ?";
        Object[] params = {id};
        return qr.query(sql, new BeanHandler<BigClass>(BigClass.class), params);
    }

    /**
     * 修改一个BigClass
     * @param bigClass 被修改
     */
    public void updateCustomer(BigClass bigClass) throws SQLException {
        String sql = "update bigclass set bigName = ?, bigText = ? where id = ?";
        Object[] params = {bigClass.getBigName(), bigClass.getBigText(), bigClass.getId()};
        qr.update(sql, params);
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(new BigClassDao().count("1"));
    }
}
