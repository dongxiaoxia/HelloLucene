package xyz.dongxiaoxia.util;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class HttpClientUtil {
//    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
//
//    private HttpClientUtil() {
//    }
//
//    /**
//     * 发送post请求
//     *
//     * @param url 请求地址
//     * @return
//     */
//    public static String sendHttpPost(String url) {
//        HttpPost httpPost = new HttpPost(url);//创建httpPost
//        return sendHttpPost(httpPost);
//    }
//
//    /**
//     * 发送post请求
//     *
//     * @param url    请求地址
//     * @param params 参数（格式：key1=vaule1&key2=value2）
//     * @return
//     */
//    public static String sendHttpPost(String url, String params) {
//        HttpPost httpPost = new HttpPost(url);
//        //设置参数
//        StringEntity stringEntity = new StringEntity(params, "UTF-8");
//        stringEntity.setContentType("application/x-www-form-urlencoded");
//        httpPost.setEntity(stringEntity);
//        return sendHttpPost(httpPost);
//    }
//
//    /**
//     * 发送post请求
//     *
//     * @param url
//     * @param map
//     * @return
//     */
//    public static String sendHttpPost(String url, Map<String, String> map) {
//        HttpPost httpPost = new HttpPost(url);
//        //创建参数队列
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        for (String key : map.keySet()) {
//            nameValuePairs.add(new BasicNameValuePair(key, map.get(key)));
//        }
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return sendHttpPost(httpPost);
//    }
//
////    /**
////     * 发送 post请求（带文件）
////     *
////     * @param url
////     * @param map
////     * @param fileList
////     * @return
////     */
////    public String sendHttpPost(String url, Map<String, String> map, List<File> fileList) {
////        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
////        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
////        for (String key : maps.keySet()) {
////            meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
////        }
////        for (File file : fileLists) {
////            FileBody fileBody = new FileBody(file);
////            meBuilder.addPart("files", fileBody);
////        }
////        HttpEntity reqEntity = meBuilder.build();
////        httpPost.setEntity(reqEntity);
////        return sendHttpPost(httpPost);
////    }
//
//    /**
//     * 发送Post请求
//     *
//     * @param httpPost
//     * @return
//     */
//    private static String sendHttpPost(HttpPost httpPost) {
//        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse httpResponse = null;
//        HttpEntity entity = null;
//        String responseContent = null;
//        try {
//            //创建默认的httpclient实例
//            httpClient = HttpClients.createDefault();
//            httpPost.setConfig(requestConfig);
//            //执行请求
//            httpResponse = httpClient.execute(httpPost);
//            entity = httpResponse.getEntity();
//            responseContent = EntityUtils.toString(entity, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                //关闭资源
//                if (httpResponse!=null){
//                    httpResponse.close();
//                }
//                if (httpClient!=null){
//                    httpClient.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return responseContent;
//    }
//
//    /**
//     * 发送Get请求Https
//     * @param httpGet
//     * @return
//     */
//    private static String sendHttpsGet(HttpGet httpGet) {
//        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        String responseContent = null;
//        try {
//            // 创建默认的httpClient实例.
//            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
//            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
//            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
//            httpGet.setConfig(requestConfig);
//            // 执行请求
//            response = httpClient.execute(httpGet);
//            entity = response.getEntity();
//            responseContent = EntityUtils.toString(entity, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 关闭连接,释放资源
//                if (response != null) {
//                    response.close();
//                }
//                if (httpClient != null) {
//                    httpClient.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return responseContent;
//    }
//
//    public static void main(String[] args) {
//        HttpGet httpGet = new HttpGet("http://www.dongxiaoxia.xyz");
//        System.out.println(HttpClientUtil.sendHttpsGet(httpGet));
//        System.out.println(HttpClientUtil.sendHttpPost("http://www.dongxiaoxia.xyz"));
//    }
}
