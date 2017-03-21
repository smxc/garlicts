package com.garlicts.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流操作工具类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class StreamUtil {

    private static final Logger logger = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 将输入流复制到输出流
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) {
        try {
            int length;
            byte[] buffer = new byte[4 * 1024];
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            logger.error("复制流出错！");
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                logger.error("释放资源出错！");
            }
        }
    }

    /**
     * 从输入流中获取字符串
     */
    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error("Stream 转 String 出错！");
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
