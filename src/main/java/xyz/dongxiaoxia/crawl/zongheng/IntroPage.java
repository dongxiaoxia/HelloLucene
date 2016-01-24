package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.CrawlBase;
import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelInfoModel;
import xyz.dongxiaoxia.util.JsonUtil;
import xyz.dongxiaoxia.util.ParseMD5;
import xyz.dongxiaoxia.util.RegexUtil;

import java.util.HashMap;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class IntroPage extends CrawlBase {
    private String url;
    private static HashMap<String, String> params;
    private static final String NAME = "<meta name=\"og:novel:book_name\" content=\"(.*?)\"/>";
    private static final String AUTHOR = "<meta name=\"og:novel:author\" content=\"(.*?)\"/> \n";
    private static final String DESC = "<meta property=\"og:description\" content=\"(.*?)\"/> \n";
    private static final String TYPE = "<meta name=\"og:novel:category\" content=\"(.*?)\"/> \n";
    private static final String LASTCHARPTER = "<a class=\"chap\" href=\".*?\">(.*?)\n";
    private static final String WORDCOUNT = "<span title=\"(\\d*?)字\">.*?</span>字\n";
    private static final String KEYWORDS = "<div class=\"keyword\">(.*?)</div>\n";
    private static final String KEYWORD = "<a.*?>(.*?)</a>";
    private static final String CHAPTERLISTURL = "<meta name=\"og:novel:read_url\" content=\"(.*?)\"/>";

    static {
        params = new HashMap<String, String>();
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        params.put("Referer", "http://www.zongheng.com/");
        params.put("Host", "http://www.zongheng.com/");
    }

    public IntroPage(String url) {
        readPageByGet(url,params,"utf-8");
        this.url = url;
    }

    private String getName(){
        return RegexUtil.getFirstString(getPageSourceCode(),NAME,1);
    }

    private String getAuthor(){
        return RegexUtil.getFirstString(getPageSourceCode(),AUTHOR,1);
    }

    private String getDesc(){
        return RegexUtil.getFirstString(getPageSourceCode(),DESC,1);
    }

    private String getType(){
        return RegexUtil.getFirstString(getPageSourceCode(),TYPE,1);
    }

    private String getLastCharpter(){
        return RegexUtil.getFirstString(getPageSourceCode(),LASTCHARPTER,1);
    }

    private int getWordCount() {
        String wordCount = RegexUtil.getFirstString(getPageSourceCode(), WORDCOUNT, 1);
        return Integer.parseInt(wordCount);
    }

    private String getKeyWordStr(){
        return RegexUtil.getFirstString(getPageSourceCode(), KEYWORDS, 1);
    }

    private String getKeyWord(){
        return RegexUtil.getString(getKeyWordStr(), KEYWORD, " ", 1);
    }

    private String getChapterlistUrl(){
        return RegexUtil.getFirstString(getPageSourceCode(), CHAPTERLISTURL, 1);
    }

    public NovelInfoModel analyzer() {
        NovelInfoModel bean = new NovelInfoModel();
        bean.setUrl(url);
        bean.setId(ParseMD5.parseStrToMD5(bean.getUrl()));
        bean.setName(getName());
        bean.setType(getType());
        bean.setAuthor(getAuthor());
        bean.setWordCount(getWordCount());
        bean.setChapterListUrl(getChapterlistUrl());
        bean.setDesc(getDesc());
        bean.setKeyWords(getKeyWord());
        bean.setLastChapter(getLastCharpter());
        return bean;
    }

    public static void main(String[] args) {
        IntroPage introPage = new IntroPage("http://book.zongheng.com/book/400791.html");
        System.out.println(introPage.getName());
        System.out.println(introPage.getAuthor());
        System.out.println(introPage.getDesc());
        System.out.println(introPage.getType());
        System.out.println(introPage.getLastCharpter());
        System.out.println(introPage.getWordCount());
        System.out.println(introPage.getKeyWord());
        System.out.println(introPage.getChapterlistUrl());

        System.out.println(JsonUtil.parseJson(introPage.analyzer()));
        ZonghengDB db = new ZonghengDB();
        db.updateNovelInfo(introPage.analyzer());
    }
}
