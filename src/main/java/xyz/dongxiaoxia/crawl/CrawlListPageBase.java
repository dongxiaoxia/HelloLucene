package xyz.dongxiaoxia.crawl;

import xyz.dongxiaoxia.util.RegexUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public abstract class CrawlListPageBase extends CrawlBase{
    private String pageUrl;

    public CrawlListPageBase(String pageUrl, String charsetName) {
        readPageByGet(pageUrl,null,charsetName);
        this.pageUrl = pageUrl;
    }

    public CrawlListPageBase(String pageUrl, String charsetName,HashMap<String,String> params) {
        readPageByGet(pageUrl,params,charsetName);
        this.pageUrl = pageUrl;
    }

    public abstract String getUrlRegexStr();

    public abstract int getUrlRegexStrNum();

    public List<String> getPageUrl(){
        return RegexUtil.getArrayList(getPageSourceCode(),getUrlRegexStr(),this.pageUrl,getUrlRegexStrNum());
    }
}
