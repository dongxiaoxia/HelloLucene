package xyz.dongxiaoxia.crawl;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public abstract class CrawlBase {
    private static final Logger logger = LoggerFactory.getLogger(CrawlBase.class);
    private String pageSourceCode = "";
    private Header[] responseHeaders = null;
    private static int connectTimeOut = 10000;
    private static int readTimeOut = 10000;
    private static int maxConnectTimes = 3;
//    private static String charsetName = "iso-8859-1";
    private static String charsetName = "utf-8";
    private static MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
    private static HttpClient httpClient = new HttpClient(httpConnectionManager);

    public static Logger getLogger() {
        return logger;
    }

    public String getPageSourceCode() {
        return pageSourceCode;
    }

    public void setPageSourceCode(String pageSourceCode) {
        this.pageSourceCode = pageSourceCode;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public static int getConnectTimeOut() {
        return connectTimeOut;
    }

    public static void setConnectTimeOut(int connectTimeOut) {
        CrawlBase.connectTimeOut = connectTimeOut;
    }

    public static int getReadTimeOut() {
        return readTimeOut;
    }

    public static void setReadTimeOut(int readTimeOut) {
        CrawlBase.readTimeOut = readTimeOut;
    }

    public static int getMaxConnectTimes() {
        return maxConnectTimes;
    }

    public static void setMaxConnectTimes(int maxConnectTimes) {
        CrawlBase.maxConnectTimes = maxConnectTimes;
    }

    public static String getCharsetName() {
        return charsetName;
    }

    public static void setCharsetName(String charsetName) {
        CrawlBase.charsetName = charsetName;
    }

    public static MultiThreadedHttpConnectionManager getHttpConnectionManager() {
        return httpConnectionManager;
    }

    public static void setHttpConnectionManager(MultiThreadedHttpConnectionManager httpConnectionManager) {
        CrawlBase.httpConnectionManager = httpConnectionManager;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        CrawlBase.httpClient = httpClient;
    }

    static{
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeOut);
        httpClient.getParams().setContentCharset("utf-8");
    }

    private GetMethod createGetMethod(String urlStr,HashMap<String,String> params){
        GetMethod method = new GetMethod(urlStr);
        if (params == null){
            return method;
        }
        Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            method.setRequestHeader(key,value);
        }
        return method;
    }

    public boolean readPageByGet(String urlStr, HashMap<String,String> params, String charsetName){
        GetMethod method = createGetMethod(urlStr, params);
        return readPage(method,charsetName,urlStr);
    }

    public boolean readPageByPost(String urlStr, HashMap<String,String> params, String charsetName){
        GetMethod method = createGetMethod(urlStr, params);
        return readPage(method,charsetName,urlStr);
    }

    private boolean readPage(HttpMethod method, String defaultCharset, String urlStr){
        int n = maxConnectTimes;
        while (n>0){
            try {
                if (httpClient.executeMethod(method)!= HttpStatus.SC_OK){
                    logger.info("can not connect " + urlStr + (maxConnectTimes-n+1));
                }else {
                    responseHeaders = method.getResponseHeaders();
                    InputStream inputStream = method.getResponseBodyAsStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,charsetName));
                    StringBuffer stringBuffer = new StringBuffer();
                    String lineString = "";
                    while ((lineString = bufferedReader.readLine())!= null) {
                        stringBuffer.append(lineString);
                        stringBuffer.append("\n");
                    }
                    pageSourceCode = stringBuffer.toString();
                    InputStream in = new ByteArrayInputStream(pageSourceCode.getBytes(charsetName));
                    String charset = CharsetUtil.getStreamCharset(inputStream,defaultCharset);
                    if (!charsetName.toLowerCase().equals(charset.toLowerCase())){
                        pageSourceCode = new String(pageSourceCode.getBytes(charsetName),charset);
                    }
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error(urlStr + "can not connect " + (maxConnectTimes - n + 1));
                n--;
            }
        }
        return false;
    }

    private PostMethod createPostMethod(String urlStr,HashMap<String,String> params){
        PostMethod method = new PostMethod(urlStr);
        if (params == null){
            return method;
        }
        Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            method.setRequestHeader(key,value);
        }
        return method;
    }
}
