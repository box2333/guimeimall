package cn.jwq.dao;

import cn.itcast.commons.CommonUtils;
import cn.jwq.pojo.BigClass;
import cn.jwq.pojo.Goods;
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
 * @date 2019-03-26-23:14
 */
public class GoodsDao {
    private static QueryRunner qr = DBUtil.getQueryRunner();

    /**
     * 查询总记录数
     * @return 总记录数
     * @throws SQLException 异常
     */
    public int count() throws SQLException {
        String sql = "select count(id) from goods";
        return ((Number) qr.query(sql, new ScalarHandler())).intValue();
    }

    /**
     * 根据商品名称查询总记录数
     * @return 总记录数
     * @throws SQLException 异常
     */
    public int count(String name) throws SQLException {
        String sql = "select count(id) from goods where goodsName = ?";
        Object[] params = {name};
        return ((Number) qr.query(sql, new ScalarHandler(), params)).intValue();
    }

    /**
     * 分页查询所有商品
     * @param start 起使位置
     * @param count 个数
     * @return 结果集
     * @throws SQLException 异常
     */
    public List<Goods> list(int start, int count) throws SQLException {
        String sql = " select g.id,g.goodsName,g.goodsSmalId,g.goodsMoney,g.goodsNumber,g.goodsImage,g.goodsCarriage,g.goodsType,g.goodsDiscId,s.smallName from goods g\n" +
                " inner join smallclass s on g.goodsSmalId = s.id \n" +
                " GROUP BY g.id limit ?, ?";
        Object[] params = {start, count};
        // 把结果集封装为 List<Map>
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), params);
        return get(mapList);
    }

    /**
     * 条件分页查询所有商品
     * @param start 起使位置
     * @param count 个数
     * @return 结果集
     * @throws SQLException 异常
     */
    public List<Goods> list(String smallNameId, String name, int start, int count) throws SQLException {
        String sql = " select g.id,g.goodsName,g.goodsSmalId,g.goodsMoney,g.goodsNumber,g.goodsImage,g.goodsCarriage,g.goodsType,g.goodsDiscId,s.smallName from goods g\n" +
                " inner join smallclass s on g.goodsSmalId = s.id \n";
        boolean flag = false;
        List<Object> objects = new ArrayList<Object>();
        if (smallNameId != null) {
            sql += " where g.goodsSmalId = ? ";
            flag = true;
            objects.add(smallNameId);
        }
        if (name != null) {
            sql += flag ? " and g.goodsName like ? " : " where g.goodsName like ? ";
            objects.add("%"+name+"%");
        }
        sql += " GROUP BY g.id limit ?, ?";
        objects.add(start);
        objects.add(count);
        Object[] params = objects.toArray();
        // 把结果集封装为 List<Map>
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), params);
        System.out.println(get(mapList));
        return get(mapList);
    }

    private List<Goods> get(List<Map<String, Object>> mapList) {
        // 创建一个 List<Goods>
        List<Goods> goodsList = new ArrayList<Goods>();
        for (Map map : mapList) {
            // 把Map中部分数据封装到Goods中
            Goods goods = CommonUtils.toBean(map, Goods.class);
            // 把Map中部分数据封装到BigClass中
            SmallClass smallClass1 = CommonUtils.toBean(map, SmallClass.class);
            smallClass1.setId(goods.getGoodsSmalId());

            // 建立两个实体的关系
            goods.setSmallClass(smallClass1);
            goodsList.add(goods);
        }
        return goodsList;
    }

    /**
     * 查询商品名称是否重复
     * @param name 商品名称
     * @return 可用否
     * @throws SQLException 异常
     */
    public int isName(String name) throws SQLException {
        String sql = "select id from goods where goodsName = ?";
        Object[] params = {name};
        Object object = qr.query(sql, new ScalarHandler(), params);
        if (object == null) {
            return 0;
        }
        return 1;
    }

    /**
     * 添加一个商品
     * @param g 商品类
     * @return 成功否
     * @throws SQLException 异常
     */
    public int add(Goods g) throws SQLException {
        String sql = " insert into goods(goodsName,goodsSmalId,goodsMoney,goodsNumber,goodsImage,goodsCarriage,goodsType,goodsDiscId)" +
                " values(?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {g.getGoodsName(), g.getGoodsSmalId(), g.getGoodsMoney(), g.getGoodsNumber(), g.getGoodsImage()
            , g.getGoodsCarriage(), g.getGoodsType(), g.getGoodsDiscId()};
        return qr.update(sql, params);
    }

    /**
     * 删除一个用户
     * @param id 用户id
     * @return 成功否
     * @throws SQLException 异常
     */
    public int delete(int id) throws SQLException {
        String sql = "delete from goods where id = ?";
        Object[] params = {id};
        return qr.update(sql, params);
    }

    /**
     * 查询图片路径
     * @param id id
     * @return 路径
     * @throws SQLException 异常
     */
    public String getImagePath(int id) throws SQLException {
        String sql = "select goodsImage from goods where id = ?";
        Object[] params = {id};
        return (String) qr.query(sql, new ScalarHandler(), params);
    }

    /**
     * 根据id查询商品
     * @param id id
     * @return 商品对象
     * @throws SQLException 异常
     */
    public Goods getGoods(int id) throws SQLException {
        String sql = "select * from goods where id = ?";
        Object[] params = {id};
        return qr.query(sql, new BeanHandler<Goods>(Goods.class), params);
    }

    /**
     * 修改商品
     * @param g 商品
     * @return 成功否
     * @throws SQLException 异常
     */
    public int update(Goods g) throws SQLException {
        String sql = "update goods set goodsName = ?, goodsNumber = ?, goodsMoney = ?, goodsSmalId = ?, goodsCarriage = ?,goodsDiscId = ?, goodsImage = ?, goodsType = ? where id = ?";
        Object[] params = {g.getGoodsName(), g.getGoodsNumber(), g.getGoodsMoney(), g.getGoodsSmalId()
            , g.getGoodsCarriage(), g.getGoodsDiscId(), g.getGoodsImage(), g.getGoodsType(), g.getId()};
        return ((Number) qr.update(sql, params)).intValue();
    }
}
