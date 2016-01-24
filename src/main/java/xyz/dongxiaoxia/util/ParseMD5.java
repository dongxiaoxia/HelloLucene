package xyz.dongxiaoxia.util;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class ParseMD5 extends Encrypt{

    public static String parseStrToMD5(String str){
        return encrypy(str,MD5);
    }

    public static String parseStrToUpperMD5(String str){
        return parseStrToMD5(str).toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(ParseMD5.parseStrToMD5("abc"));
    }

}
