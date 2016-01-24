package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;
import xyz.dongxiaoxia.crawl.zongheng.model.CrawlListInfo;

import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class CrawlStart {
    private static  boolean booleanCrawlList =false;
    private static  boolean booleanCrawlIntro =false;
    private static boolean booleanCrawlRead = false;

    private static int crawlIntroThreadNum = 2;
    private static int crawlReadThreadNum = 10;

    public void startCrawlList(){
        if (booleanCrawlList){
            return;
        }
        booleanCrawlList = true;
        ZonghengDB db = new ZonghengDB();
        List<CrawlListInfo> infos = db.getCrawlListInfos();
        if (infos  == null){
            return;
        }
        for (CrawlListInfo info : infos){
            if (info.getUrl() == null || "".equals(info.getUrl())){
                continue;
            }
            UpdateListThread thread = new UpdateListThread(info.getInfo(),info.getUrl(),info.getFrequency());
            thread.start();
        }
    }

    public void startCrawlIntro(){
        if (booleanCrawlIntro){
            return;
        }
        booleanCrawlIntro = true;
        for (int i = 0 ;i < crawlIntroThreadNum;i++){
            IntroPageThread thread = new IntroPageThread("novel info page" + i);
            thread.start();
        }
    }

    public void startCrawlRead(){
        if (booleanCrawlRead){
            return;
        }
        booleanCrawlRead = true;
        for (int i = 0;i<crawlReadThreadNum;i++){
            ReadPageThread thread = new ReadPageThread("novel read page "+ i);
            thread.start();
        }
    }

    public static void main(String[] args) {
        CrawlStart start = new CrawlStart();
        start.startCrawlList();
        start.startCrawlIntro();
        start.startCrawlRead();
    }
}
