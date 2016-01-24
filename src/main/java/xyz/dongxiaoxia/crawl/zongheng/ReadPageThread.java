package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelChapterModel;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelReadModel;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class ReadPageThread extends Thread {
    private boolean flag = false;

    public ReadPageThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        flag = true;
        ZonghengDB db = new ZonghengDB();
        while (flag){
            try {
                NovelChapterModel chapterModel = db.getRandChapter(1);
                if (chapterModel!=null){
                    ReadPage readPage = new ReadPage(chapterModel.getUrl());
                    NovelReadModel novelReadModel = readPage.analyzer();
                    if (novelReadModel == null) {
                        continue;
                    }
                    novelReadModel.setChapterId(chapterModel.getChapterId());
                    novelReadModel.setChapterTime(chapterModel.getChapterTime());
                    db.saveNovelRead(novelReadModel);
                    db.updateChapterState(chapterModel.getId(), 0);
                    Thread.sleep(500);
                }else {
                    Thread.sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.run();
    }

    public static void main(String[] args) {
        ReadPageThread thread = new ReadPageThread("s");
        thread.start();
    }
}
