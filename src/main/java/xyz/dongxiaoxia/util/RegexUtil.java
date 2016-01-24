package xyz.dongxiaoxia.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class RegexUtil {
    private static String rootUrlRegex = "(http://.*?/)";
    private static String currentUrlRegex = "(http://.*/)";
    private static String ChRegex = "([\u4e00-\u9fa5]+)";

    public static String getString(String dealStr, String regexStr, String splitStr, int n){
        String reStr = "";
        if (dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()){
            return reStr;
        }
        splitStr = (splitStr == null) ? "" : splitStr;
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            stringBuffer.append(matcher.group(n).trim());
            stringBuffer.append(splitStr);
        }
        reStr = stringBuffer.toString();
        if (splitStr != "" && reStr.endsWith(splitStr)){
            reStr = reStr.substring(0, reStr.length() - splitStr.length());
        }
        return reStr;
    }

    public static String getString(String dealStr, String regexStr, int n){
        return getString(dealStr, regexStr, null, n);
    }

    public static String getFirstString(String dealStr, String regexStr, int n) {
        if (dealStr == null || regexStr == null || n < 1) {
            return "";
        }
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        while (matcher.find()) {
            return matcher.group(n).trim();
        }
        return null;
    }

    public static List<String> getList(String dealStr, String regexStr, int n) {
        List<String> list = new ArrayList<String>();
        if (dealStr == null || regexStr == null || n < 1) {
            return list;
        }
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        while (matcher.find()) {
            list.add(matcher.group(n).trim());
        }
        return list;
    }

    public static List<String[]> getList(String dealStr, String regexStr, int[] array) {
        List<String[]> list = new ArrayList<String[]>();
        if (dealStr == null || regexStr == null || array == null) {
            return list;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 1) {
                return list;
            }
        }
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        while (matcher.find()) {
            String[] ss = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                ss[i] = matcher.group(array[i]).trim();
            }
            list.add(ss);
        }
        return list;
    }

    public static List<String> getStringArray(String dealStr, String regexStr, int[] array) {
        List<String> reStringList = new ArrayList<String>();
        if (dealStr == null || regexStr == null || array == null) {
            return reStringList;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 1) {
                return reStringList;
            }
        }
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        while (matcher.find()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                sb.append(matcher.group(array[i]).trim());
            }
            reStringList.add(sb.toString());
        }
        return reStringList;
    }

    public static String[] getFirstArray(String dealStr, String regexStr, int[] array) {
        if (dealStr == null || regexStr == null || array == null) {
            return null;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 1) {
                return null;
            }
        }
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        while (matcher.find()) {
            String[] ss = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                ss[i] = matcher.group(array[i]).trim();
            }
            return ss;
        }
        return null;
    }

    private static String getHttpUrl(String url, String currentUrl){
        try {
            //鏂板鐨剅eplaceAll  杞寲鏈変簺鍦板潃鎺ュ彛涓殑杞寲鍦板潃锛屽锛� \/test\/1.html
            url = encodeUrlCh(url).replaceAll("\\\\/", "/");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (url.indexOf("http") == 0){
            return url;
        }
        if  (url.indexOf("/") == 0){
            return getFirstString(currentUrl, rootUrlRegex, 1) + url.substring(1);
        }
        if  (url.indexOf("\\/") == 0){
            return getFirstString(currentUrl, rootUrlRegex, 1) + url.substring(2);
        }
        return getFirstString(currentUrl, currentUrlRegex, 1) + url;
    }

    public static List<String> getArrayList(String dealStr, String regexStr, String currentUrl, int n){
        List<String> reArrayList = new ArrayList<String>();
        if (dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()){
            return reArrayList;
        }
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(dealStr);
        while (matcher.find()) {
            reArrayList.add(getHttpUrl(matcher.group(n).trim(), currentUrl));
        }
        return reArrayList;
    }

    public static String encodeUrlCh (String url) throws UnsupportedEncodingException {
        while (true) {
            String s = getFirstString(url, ChRegex, 1);
            if ("".equals(s)){
                return url;
            }
            url = url.replaceAll(s, URLEncoder.encode(s, "utf-8"));
        }
    }

    public static void main(String[] args) {
        String dealStr = "ab1234asdv";
        String regexStr = "a(.*?)a";
        System.out.println(RegexUtil.getFirstString(dealStr, regexStr, 1));
    }
}
