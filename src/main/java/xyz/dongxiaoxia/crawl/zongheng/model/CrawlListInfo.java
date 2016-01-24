package xyz.dongxiaoxia.crawl.zongheng.model;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class CrawlListInfo {
    private String url;
    private String info;
    private int frequency;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
