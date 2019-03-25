package cn.jwq.util;

import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-23:01
 */
public class DBUtil {
    private static QueryRunner queryRunner = new TxQueryRunner();

    private DBUtil() { }

    public static QueryRunner getQueryRunner() {
        return queryRunner;
    }
}
