package cn.jwq.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * @author jia
 * @Description
 * @date 2019-03-27-2:44
 */
public class ImageUtil {
    public static boolean generateImage(String imgStr, String path) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            System.out.println("ok");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
