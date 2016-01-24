package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.CrawlBase;
import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelChapterModel;
import xyz.dongxiaoxia.util.JsonUtil;
import xyz.dongxiaoxia.util.ParseMD5;
import xyz.dongxiaoxia.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class ChapterPage extends CrawlBase {
    private String url;
    private static HashMap<String, String> params;
    private static final String CHAPTER = "<td class=\"chapterBean\" chapterid=\"\\d*\" chaptername=\"(.*?)\" chapterlevel=\"\\d*\" wordnum=\"(\\d*)\" updatetime=\"(\\d*)\"><a href=\"(.*?)\" title=\".*?>";
    private static final int[] array = {1, 2, 3, 4};

    static {
        params = new HashMap<String, String>();
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        params.put("Referer", "http://www.zongheng.com/");
        params.put("Host", "http://www.zongheng.com/");
    }

    public ChapterPage(String url) {
        readPageByGet(url, params, "utf-8");
        this.url = url;
    }

    public List<String[]> getChapters() {
        return RegexUtil.getList(getPageSourceCode(), CHAPTER, array);
    }

    public List<NovelChapterModel> analyzer() {
        List<NovelChapterModel> list = new ArrayList<NovelChapterModel>();
        List<String[]> arrays = getChapters();
        int i = 0;
        for (String[] array : arrays) {
            i++;
            list.add(analyzer(array, i));
        }
        return list;
    }

    private NovelChapterModel analyzer(String[] array, int i) {
        NovelChapterModel bean = new NovelChapterModel();
        bean.setUrl(array[3]);
        bean.setId(ParseMD5.parseStrToMD5(bean.getUrl()));
        bean.setTitle(array[0]);
        bean.setWordCount(Integer.parseInt(array[1]));
        bean.setChapterTime(Long.parseLong(array[2]));
        bean.setChapterId(i);
        return bean;
    }

    public static void main(String[] args) {
        ChapterPage chapterPage = new ChapterPage("http://book.zongheng.com/showchapter/527180.html");
        for (String[] ss : chapterPage.getChapters()) {
            for (String s : ss) {
                System.out.println(s);
            }
            System.out.println("-----------------------------");
        }
        System.out.println(JsonUtil.parseJson(chapterPage.analyzer()));

        ZonghengDB db = new ZonghengDB();
        db.saveNovelChapter(chapterPage.analyzer(),"7d03aab7512f7ee9bda9ced2644c883c");
    }

}
