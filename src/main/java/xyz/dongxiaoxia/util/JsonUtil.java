package xyz.dongxiaoxia.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class JsonUtil {

    private static final String noData = "{\"result\" : null}";
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String parseJson(Object object) {
        if (object == null) {
            return noData;
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return noData;
        }
    }

    public JsonNode json2Object(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseJson(Object obj, String root) {

        if (obj == null) {
            return noData;
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"");
            sb.append(root);
            sb.append("\":");
            sb.append(mapper.writeValueAsString(obj));
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return noData;
        }
    }

    /**
     * @param json
     * @param var
     * @return ?????ar??ull????????????datas
     * @Author: lulei
     * @Description:??son?????????jsonp?????ar data={}???
     */
    public static String wrapperJsonp(String json, String var) {
        if (var == null) {
            var = "datas";
        }
        return new StringBuilder().append("var ").append(var).append("=").append(json).toString();
    }

    /**
     * @param args
     * @Author:lulei
     * @Description:
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Map map = new HashMap();
        map.put("a","123");
        System.out.println(JsonUtil.parseJson(map));
        System.out.println(JsonUtil.parseJson(null));
    }

}

