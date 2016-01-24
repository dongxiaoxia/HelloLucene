package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelChapterModel;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelInfoModel;

import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class IntroPageThread extends Thread {
    private boolean flag;

    public IntroPageThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        flag = true;
        try {
            ZonghengDB db = new ZonghengDB();
            while (flag){
                String url = db.getRandIntroPageUrl(1);
                if (url != null) {
                    IntroPage introPage = new IntroPage(url);
                    NovelInfoModel bean = introPage.analyzer();
                    if (bean != null) {
                        ChapterPage chapterPage = new ChapterPage(bean.getChapterListUrl());
                        List<NovelChapterModel> chapterModels = chapterPage.analyzer();
                        db.updateNovelInfo(bean);
                        db.saveNovelChapter(chapterModels, bean.getId());
                    }
                    Thread.sleep(500);
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.run();
    }

    public static void main(String[] args) {
        IntroPageThread introPageThread = new IntroPageThread("name");
        introPageThread.start();
    }
}
