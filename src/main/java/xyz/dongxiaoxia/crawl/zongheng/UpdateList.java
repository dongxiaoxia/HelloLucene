package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.CrawlListPageBase;
import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class UpdateList extends CrawlListPageBase {
    private static HashMap<String,String> params;

    static{
        params = new HashMap<String, String>();
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        params.put("Referer","http://www.zongheng.com/");
        params.put("Host","http://www.zongheng.com/");
    }

    public UpdateList(String pageUrl) {
        super(pageUrl,"utf-8",params);
    }

    @Override
    public String getUrlRegexStr() {
        return "<a class=\"fs14\" href=\"(.*?)\"";
    }

    @Override
    public int getUrlRegexStrNum() {
        return 1;
    }

    public List<String> getPageUrl(boolean exceptOther){
        List<String> urls = getPageUrl();
        if (exceptOther){
            List<String> exceptUrls = new ArrayList<String>();
            for (String url: urls){
                if (url.indexOf("zongheng")!=-1){
                    exceptUrls.add(url);
                }
            }
            return exceptUrls;
        }
        return urls;
    }

    public static void main(String[] args) {
        UpdateList updateList = new UpdateList("http://www.zongheng.com/");
        for (String string : updateList.getPageUrl(true)){
            System.out.println(string);
        }
        ZonghengDB db = new ZonghengDB();
        db.saveInfoUrls(updateList.getPageUrl(true));
    }
}
