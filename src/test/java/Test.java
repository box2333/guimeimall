import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.jwq.pojo.Admin;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-14:47
 */
@Logger
public class Test {
    public static void main(String[] args) {
        try {
            String sql = "select * from admin";
//            Object[] params = {};     // 给sql 中对应的参数
            QueryRunner qr = new TxQueryRunner();
            List<Admin> admins = qr.query(sql, new BeanListHandler<Admin>(Admin.class));
            for (Admin admin : admins) {
                System.out.println(admin.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
