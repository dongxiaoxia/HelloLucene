package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.CrawlBase;
import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelReadModel;
import xyz.dongxiaoxia.util.JsonUtil;
import xyz.dongxiaoxia.util.ParseMD5;
import xyz.dongxiaoxia.util.RegexUtil;

import java.util.HashMap;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class ReadPage extends CrawlBase {

    private String url;
    private static HashMap<String, String> params;
    private static final String TITLE = "chaptername=\"(.*?)\"";
    private static final String WORDCOUNT = "<span itemprop=\"wordCount\">(.*?)</span>";
    private static final String CONTENT = "<div id=\"chapterContent\" class=\"content\" itemprop=\"acticleBody\">(.*?)</div>";
    private static final String DESC = "<meta property=\"og:description\" content=\"(.*?)\"/> \n";
    private static final String TYPE = "<meta name=\"og:novel:category\" content=\"(.*?)\"/> \n";
    private static final String LASTCHARPTER = "<a class=\"chap\" href=\".*?\">(.*?)\n";
    private static final String KEYWORDS = "<div class=\"keyword\">(.*?)</div>\n";
    private static final String KEYWORD = "<a.*?>(.*?)</a>";
    private static final String CHAPTERLISTURL = "<meta name=\"og:novel:read_url\" content=\"(.*?)\"/>";

    static {
        params = new HashMap<String, String>();
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        params.put("Referer", "http://www.zongheng.com/");
        params.put("Host", "http://www.zongheng.com/");
    }

    public ReadPage(String url) {
        readPageByGet(url,params,"utf-8");
        this.url = url;
    }

    private String getTitle(){
        return RegexUtil.getFirstString(getPageSourceCode(),TITLE,1);
    }

    private int getWordCount(){
        String count = RegexUtil.getFirstString(getPageSourceCode(),WORDCOUNT,1);
        return Integer.parseInt(count);
    }

    private String getContent(){
        return RegexUtil.getFirstString(getPageSourceCode(), CONTENT, 1);
    }

    public NovelReadModel analyzer(){
        NovelReadModel bean = new NovelReadModel();
        bean.setUrl(url);
        bean.setId(ParseMD5.parseStrToMD5(url));
        bean.setTitle(getTitle());
        bean.setWordCount(getWordCount());
        bean.setContent(getContent());
        return bean;
    }
    public static void main(String[] args) {
        ReadPage readPage = new ReadPage("http://book.zongheng.com/chapter/527180/8571964.html");
        System.out.println(readPage.getTitle());
        System.out.println(readPage.getWordCount());
        System.out.println(readPage.getContent());
        System.out.println(JsonUtil.parseJson(readPage.analyzer()));
        NovelReadModel bean = readPage.analyzer();
        ZonghengDB db = new ZonghengDB();
        db.saveNovelRead(bean);
    }
}
