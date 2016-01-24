package xyz.dongxiaoxia.crawl.zongheng;

import xyz.dongxiaoxia.crawl.zongheng.db.ZonghengDB;

import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class UpdateListThread extends Thread {
    private boolean flag = false;

    private String url = "";
    private int frequency;

    public UpdateListThread(String name, String url, int frequency) {
        super(name);
        this.url = url;
        this.frequency = frequency;
    }

    @Override
    public void run() {
        flag = true;
        ZonghengDB db = new ZonghengDB();
        while (flag) {
            try {
                UpdateList updateList = new UpdateList(this.url);
                List<String> urls = updateList.getPageUrl();
                db.saveInfoUrls(urls);
                Thread.sleep(frequency * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.run();
    }

    public static void main(String[] args) {
        UpdateListThread thread = new UpdateListThread("name1","http://www.zongheng.com/",1);
        thread.start();
    }
}
