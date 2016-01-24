package xyz.dongxiaoxia.util;

import java.security.MessageDigest;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class Encrypt {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String MD5256 = "SHA-256";

    public static String encrypy(String str, String encName) {
        String reStr = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(encName);
            byte[] bytes = digest.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reStr;
    }
}
