package xyz.dongxiaoxia.util;

import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;

import java.io.UnsupportedEncodingException;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class ClassUtil {
    public static String getClassPath(Class<?> c) {
        String path = c.getResource("").getPath().replace("%20", " ");
        try {
            return java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getClassRootPath(Class<?> c) {
        String path = c.getResource("/").getPath().replace("%20", " ");
        try {
            return java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(getClassRootPath(ZonghengDB.class));
    }
}
