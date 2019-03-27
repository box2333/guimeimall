package cn.jwq.dao;

import cn.jwq.pojo.Admin;
import cn.jwq.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * @author jia
 * @Description
 * @date 2019-03-26-22:36
 */
public class AdminDao {
    private static QueryRunner qr = DBUtil.getQueryRunner();

    public Admin getAdmin(String name, String pwd) throws SQLException {
        String sql = "select * from admin where adminName = ? and adminPassword = ?";
        Object[] params = {name, pwd};
        return qr.query(sql, new BeanHandler<Admin>(Admin.class),params);
    }
}
