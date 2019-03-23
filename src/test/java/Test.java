/**
 * @author jia
 * @Description
 * @date 2019-03-23-14:47
 */

public class Test {
    public static void main(String[] args) {
        String uri = "/admin_admin_list";
        System.out.println(
                uri.substring(uri.lastIndexOf("_")+1)
        );
    }
}
