package cn.jwq.dao;

import cn.itcast.commons.CommonUtils;
import cn.jwq.pojo.BigClass;
import cn.jwq.pojo.Customer;
import cn.jwq.pojo.SmallClass;
import cn.jwq.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jia
 * @Description
 * @date 2019-03-26-14:42
 */
public class SmallClassDao {

    private static QueryRunner qr = DBUtil.getQueryRunner();

    /**
     * 查询总记录数
     * @return 返回总记录数
     * @throws SQLException 异常
     */
    public int count() throws SQLException {
        String sql = "select count(id) from smallclass";
        return ((Number) qr.query(sql, new ScalarHandler())).intValue();
    }

    /**
     * 根据大分类名称,统计个数
     * @param name 名称
     * @return 个数
     * @throws SQLException 异常
     */
    public int count(String name) throws SQLException {
        String sql = "select count(id) from smallclass where smallName like ?";
        Object[] params = {name};
        return ((Number) qr.query(sql, new ScalarHandler(), params)).intValue();
    }

    /**
     * 查询所有小分类
     * @return 小分类结果集
     * @throws SQLException 异常
     */
    public List<SmallClass> list() throws SQLException {
        return list(0, Short.MAX_VALUE);
    }

    /**
     * 分页查询小分类
     * @param start 起始
     * @param count 个数
     * @return 结果集
     * @throws SQLException 异常
     */
    public List<SmallClass> list(int start, int count) throws SQLException {
        return list(null, null, start, count);
    }

    /**
     * 按照小分类名称分页查询
     * @param smallName 小分类名称
     * @param start 起始
     * @param count 个数
     * @return 结果集
     * @throws SQLException 异常
     */
    public List<SmallClass> list(String smallName,String bigNameId, int start, int count) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        boolean flag = false;
        String sql = " SELECT s.id,s.smallName,s.smallBigId,s.smallText,b.bigName,b.bigText  FROM smallclass s \n" +
                " INNER JOIN bigclass b ON s.smallBigId = b.id \n";
        if (smallName != null) {
            sql += " WHERE s.smallName LIKE ? ";
            flag = true;
            objects.add("%"+smallName+"%");
        }
        if (bigNameId != null) {
            sql += flag ? "and s.smallBigId = ? " : "where s.smallBigId = ? ";
            objects.add(Integer.parseInt(bigNameId));
        }
        sql += " ORDER BY id LIMIT ?, ? ";
        objects.add(start);
        objects.add(count);
        System.out.println(sql);
        Object[] params = objects.toArray();
        // 把结果集封装为 List<Map>
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), params);
        // 创建一个 List<SmallClass>
        List<SmallClass> smallClassList = new ArrayList<SmallClass>();
        for (Map map : mapList) {
            // 把Map中部分数据封装到SmallClass中
            SmallClass smallClass = CommonUtils.toBean(map, SmallClass.class);
            // 把Map中部分数据封装到BigClass中
            BigClass bigClass = CommonUtils.toBean(map, BigClass.class);
            bigClass.setId(smallClass.getSmallBigId());
            // 建立两个实体的关系
            smallClass.setBigClass(bigClass);
            smallClassList.add(smallClass);
        }
        return smallClassList;
    }

    /**
     * 查询小分类是否存在
     * @param name 小分类
     * @return 存在否
     * @throws SQLException 异常
     */
    public int isName(String name) throws SQLException {
        String sql = "select id from smallclass where smallName = ?";
        Object[] params = {name};
        Object object = qr.query(sql, new ScalarHandler(), params);
        if (object == null) {
            return 0;
        }
        return 1;
    }

    /**
     * 添加一个小分类
     * @param s 小分类对象
     */
    public void add(SmallClass s) throws SQLException {
        String sql = "insert into smallclass(smallName, smallBigId, smallText)\n" +
                "values (?, ?, ?)";
        Object[] params = {s.getSmallName(), s.getSmallBigId(), s.getSmallText()};
        qr.update(sql, params);
    }

    /**
     * 删除一个小分类
     * @param id 被删除的id
     * @throws SQLException 异常
     */
    public void delete(int id) throws SQLException {
        String sql = "delete from smallclass where id = ?";
        Object[] params = {id};
        qr.update(sql, params);
    }

    /**
     * 根据id查找小分类
     * @param id 小分类id
     * @return 小分类
     * @throws SQLException 异常
     */
    public SmallClass getSmallClass(int id) throws SQLException {
        String sql = "select * from smallclass where id = ?";
        Object[] params = {id};
        return qr.query(sql, new BeanHandler<SmallClass>(SmallClass.class), params);
    }

    /**
     * 修改用户
     * @param s 修改对象
     * @throws SQLException 异常
     */
    public void updateCustomer(SmallClass s) throws SQLException {
        String sql = "update smallclass set smallName=?, smallBigId=?, smallText=? where id = ?";
        Object[] params = {s.getSmallName(),s.getSmallBigId(), s.getSmallText(), s.getId()};
        qr.update(sql, params);
    }

    public List<SmallClass> getSmallName() throws SQLException {
        String sql = "select smallName,id from smallclass";
        return qr.query(sql, new BeanListHandler<SmallClass>(SmallClass.class));
    }

    public static void main(String[] args) throws SQLException {
        SmallClassDao smallClassDao = new SmallClassDao();
        System.out.println(smallClassDao.list("电", null, 0, 100));
    }
}
